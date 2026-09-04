package org.amerp.process;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.IProcessUI;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollUtils {
	
	public static String processAMNReceiptsSummary(List<AMNReceipts> receiptsList, IProcessUI processUI, Properties ctx) {
        if (receiptsList == null || receiptsList.isEmpty()) {
            return Msg.getMsg(ctx, "NoDataFound");
        }

        // 1. Acumuladores y Contadores
        int totalReceipts = receiptsList.size();
        int successCount = 0;
        int errorCount = 0;
        int invoicesCreatedCount = 0;
        int postedCount = 0;
        int totalLines = 0;

        for (AMNReceipts rec : receiptsList) {
            if (rec.Error != null && !rec.Error.trim().isEmpty()) {
                errorCount++;
            } else {
                successCount++;
            }
            if (rec.InvoiceIsCreated) invoicesCreatedCount++;
            if (rec.RecIsPosted) postedCount++;
            totalLines += rec.NoLines;
        }

        // 2. INICIO CON <html> (Esto fuerza a iDempiere a abrir el diálogo desplegado directamente)
        StringBuilder result = new StringBuilder();
        result.append(buildReceiptsSummaryTable(receiptsList));
        

//        // 4. Descarga directa de Excel (funciona igual que en tu ETL)
//        File excelFile = generateReceiptsSummaryExcel(receiptsList);
//        if (excelFile != null && excelFile.exists() && processUI != null) {
//            processUI.download(excelFile);
//        }

        return result.toString();
    }
	

	private static String buildReceiptsSummaryTable(List<AMNReceipts> receiptsList) {
	    if (receiptsList == null || receiptsList.isEmpty()) {
	        return "No se encontraron recibos para procesar.";
	    }

	    Properties ctx = Env.getCtx();

	    // Formateador de moneda (Ej: 1.234.567,89)
	    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
	    symbols.setGroupingSeparator('.');
	    symbols.setDecimalSeparator(',');
	    DecimalFormat moneyFormat = new DecimalFormat("#,##0.00", symbols);

	    // Contadores y métricas
	    long countSuccess = receiptsList.stream().filter(r -> r.Error == null || r.Error.trim().isEmpty()).count();
	    long countFail = receiptsList.size() - countSuccess;
	    long countInvoices = receiptsList.stream().filter(r -> r.InvoiceIsCreated).count();

	    StringBuilder sb = new StringBuilder();

	    // 1. Resumen en texto plano
	    sb.append("====================================================================================================================================================================\n");
	    sb.append(String.format(" PROCESAMIENTO DE NÓMINA FINALIZADO | Total Recibos: %d [Éxito: %d, Fallos: %d] | Facturas Creadas: %d\n",
	            receiptsList.size(), countSuccess, countFail, countInvoices));
	    sb.append("====================================================================================================================================================================\n\n");

	    // 2. Cabecera de la tabla con anchos fijos
	    sb.append(String.format("%-4s | %-12s | %-10s | %-28s | %-6s | %-14s | %-14s | %-14s | %-6s | %-6s | %-8s | %s\n",
	            Msg.translate(ctx, "No"), 
	            Msg.getElement(ctx, "AMN_Payroll_ID"), 
	            Msg.getElement(ctx, "Value"), 
	            Msg.getElement(ctx, "AMN_Employee_ID"), 
	            Msg.getElement(ctx, "Line") + "(s)", 
	            Msg.getElement(ctx, "AmountAllocated"), 
	            Msg.getElement(ctx, "AmountDeducted"), 
	            Msg.getElement(ctx, "AmountNetpaid"), 
	            Msg.getElement(ctx, "Posted"), 
	            Msg.translate(ctx, "InvoiceProcessed"), 
	            Msg.getElement(ctx, "C_Invoice_ID"), 
	            Msg.translate(ctx, "Messages") + " / " + Msg.translate(ctx, "Errors")
	    ));

	    sb.append("-----+--------------+------------+------------------------------+--------+----------------+----------------+----------------+--------+--------+----------+-------------------------------------\n");

	    // 3. Filas de Datos
	    int itemSeq = 1;
	    BigDecimal totalAllocated = BigDecimal.ZERO;
	    BigDecimal totalDeducted = BigDecimal.ZERO;
	    BigDecimal totalNet = BigDecimal.ZERO;

	    for (AMNReceipts rec : receiptsList) {
	        BigDecimal allocated = rec.AmountAllocated != null ? rec.AmountAllocated : BigDecimal.ZERO;
	        BigDecimal deducted = rec.AmountDeducted != null ? rec.AmountDeducted : BigDecimal.ZERO;
	        BigDecimal net = allocated.subtract(deducted);

	        totalAllocated = totalAllocated.add(allocated);
	        totalDeducted = totalDeducted.add(deducted);
	        totalNet = totalNet.add(net);

	        String docNo = rec.DocumentNo != null ? rec.DocumentNo : "";
	        String empCode = rec.Employee_Value != null ? rec.Employee_Value : "";
	        
	        // Truncar nombre si supera los 28 caracteres para no romper la tabla
	        String empName = rec.Employee_Name != null ? rec.Employee_Name : "";
	        if (empName.length() > 28) {
	            empName = empName.substring(0, 25) + "...";
	        }

	        String postedStr = rec.RecIsPosted ? "SÍ" : "NO";
	        String invoiceCreatedStr = rec.InvoiceIsCreated ? "SÍ" : "NO";
	        String invoiceIdStr = rec.C_Invoice_ID > 0 ? String.valueOf(rec.C_Invoice_ID) : "-";
	        
	        // Limpiar saltos de línea en el mensaje de error para mantener la fila uniforme
	        String errorMsg = rec.Error != null ? rec.Error.replace("\r", "").replace("\n", " ") : "OK";

	        sb.append(String.format("%-4d | %-12s | %-10s | %-28s | %6d | %14s | %14s | %14s | %-6s | %-6s | %-8s | %s\n",
	                itemSeq++,
	                docNo,
	                empCode,
	                empName,
	                rec.NoLines,
	                moneyFormat.format(allocated),
	                moneyFormat.format(deducted),
	                moneyFormat.format(net),
	                postedStr,
	                invoiceCreatedStr,
	                invoiceIdStr,
	                errorMsg
	        ));
	    }

	    // 4. Totales
	    sb.append("-----+--------------+------------+------------------------------+--------+----------------+----------------+----------------+--------+--------+----------+-------------------------------------\n");
	    sb.append(String.format("%-59s | %14s | %14s | %14s |\n",
	            "TOTALES ACUMULADOS:",
	            moneyFormat.format(totalAllocated),
	            moneyFormat.format(totalDeducted),
	            moneyFormat.format(totalNet)
	    ));
	    sb.append("====================================================================================================================================================================\n");

	    return sb.toString();
	}
	
	public static String processAMNReceiptsSummaryHTML(List<AMNReceipts> receiptsList, IProcessUI processUI, Properties ctx) {
	    if (receiptsList == null || receiptsList.isEmpty()) {
	        return Msg.getMsg(ctx, "NoDataFound");
	    }
	
	    // 1. Contadores y Acumuladores
	    int totalReceipts = receiptsList.size();
	    int successCount = 0;
	    int errorCount = 0;
	    int invoicesCreatedCount = 0;
	    int postedCount = 0;
	    int totalLines = 0;
	
	    for (AMNReceipts rec : receiptsList) {
	        if (rec.Error != null && !rec.Error.trim().isEmpty()) {
	            errorCount++;
	        } else {
	            successCount++;
	        }
	
	        if (rec.InvoiceIsCreated) {
	            invoicesCreatedCount++;
	        }
	
	        if (rec.RecIsPosted) {
	            postedCount++;
	        }
	
	        totalLines += rec.NoLines;
	    }
	
	    // 2. Construcción de la cabecera
	    StringBuilder result = new StringBuilder("** Procesamiento de Nómina Finalizado. ");
	    result.append("Recibos: ").append(totalReceipts)
	          .append(" [Éxito: ").append(successCount)
	          .append(", Fallos: ").append(errorCount).append("]");
	    result.append(" | ");
	    result.append("[Líneas: ").append(totalLines)
	          .append(", Contabilizados: ").append(postedCount)
	          .append(", Facturas Creadas: ").append(invoicesCreatedCount).append("]");
	
	    // 3. Anexar la tabla HTML
	    result.append(buildReceiptsSummaryTable(receiptsList));
	
	    // 4. Descarga del Excel en un hilo separado (Evita la colisión con WProcessDialog en iDempiere 11)
	    final File excelFile = generateReceiptsSummaryExcel(receiptsList);
	    if (excelFile != null && excelFile.exists() && processUI != null) {
	        new Thread(() -> {
	            try {
	                // Pausa de 500ms para permitir que iDempiere renderice la ventana modal con la tabla HTML
	                Thread.sleep(500); 
	                processUI.download(excelFile);
	            } catch (Exception e) {
	                // Log o manejo silencioso de la interrupción
	            }
	        }).start();
	    }
	
	    return result.toString();
	}

	private static String buildReceiptsSummaryTableHTML(List<AMNReceipts> receiptsList, Properties ctx) {
        StringBuilder html = new StringBuilder();

        // Contenedor que adapta el ancho y agrega scroll vertical/horizontal si la nómina es muy grande
        html.append("<div style='width:100%; max-height:480px; overflow:auto; border:1px solid #d0d0d0; border-radius:4px;'>");
        
        html.append("<table border='1' cellspacing='0' cellpadding='5' style='border-collapse:collapse; font-family:Arial, sans-serif; font-size:11px; width:100%; min-width:850px;'>");
        
        // Cabecera
        html.append("  <tr style='background-color:#004085; color:white; font-weight:bold; text-align:center;'>")
            .append("    <th>No</th>")
            .append("    <th>Recibo Nómina</th>")
            .append("    <th>Código</th>")
            .append("    <th>Trabajador</th>")
            .append("    <th>Línea(s)</th>")
            .append("    <th>Asignación</th>")
            .append("    <th>Deducción</th>")
            .append("    <th>Neto</th>")
            .append("    <th>Cont.</th>")
            .append("    <th>Fact.</th>")
            .append("    <th>Fact ID</th>")
            .append("    <th>Mensajes / Errores</th>")
            .append("  </tr>");

        int seq = 1;
        BigDecimal sumAllocated = BigDecimal.ZERO;
        BigDecimal sumDeducted = BigDecimal.ZERO;
        BigDecimal sumNet = BigDecimal.ZERO;
        int sumLines = 0;

        for (AMNReceipts rec : receiptsList) {
            BigDecimal allocated = rec.AmountAllocated != null ? rec.AmountAllocated : BigDecimal.ZERO;
            BigDecimal deducted = rec.AmountDeducted != null ? rec.AmountDeducted : BigDecimal.ZERO;
            BigDecimal net = allocated.subtract(deducted);

            sumAllocated = sumAllocated.add(allocated);
            sumDeducted = sumDeducted.add(deducted);
            sumNet = sumNet.add(net);
            sumLines += rec.NoLines;

            boolean hasError = rec.Error != null && !rec.Error.trim().isEmpty();
            String rowBg = hasError ? "background-color:#f8d7da;" : (seq % 2 == 0 ? "background-color:#f9f9f9;" : "background-color:#ffffff;");

            html.append("  <tr style='").append(rowBg).append("'>")
                .append("    <td style='text-align:center;'>").append(seq++).append("</td>")
                .append("    <td style='text-align:center;'>").append(rec.DocumentNo != null ? rec.DocumentNo : "").append("</td>")
                .append("    <td style='text-align:center;'>").append(rec.Employee_Value != null ? rec.Employee_Value : "").append("</td>")
                .append("    <td>").append(rec.Employee_Name != null ? rec.Employee_Name : "").append("</td>")
                .append("    <td style='text-align:right;'>").append(rec.NoLines).append("</td>")
                .append("    <td style='text-align:right;'>").append(DisplayType.getNumberFormat(DisplayType.Amount).format(allocated)).append("</td>")
                .append("    <td style='text-align:right;'>").append(DisplayType.getNumberFormat(DisplayType.Amount).format(deducted)).append("</td>")
                .append("    <td style='text-align:right; font-weight:bold;'>").append(DisplayType.getNumberFormat(DisplayType.Amount).format(net)).append("</td>")
                .append("    <td style='text-align:center;'>").append(rec.RecIsPosted ? "SÍ" : "NO").append("</td>")
                .append("    <td style='text-align:center;'>").append(rec.InvoiceIsCreated ? "SÍ" : "NO").append("</td>")
                .append("    <td style='text-align:center;'>").append(rec.C_Invoice_ID > 0 ? rec.C_Invoice_ID : "").append("</td>")
                .append("    <td style='color:#721c24; word-break:break-word; max-width:220px;'>").append(rec.Error != null ? rec.Error : "").append("</td>")
                .append("  </tr>");
        }

        // Fila de Totales
        html.append("  <tr style='background-color:#e2e3e5; font-weight:bold;'>")
            .append("    <td colspan='4' style='text-align:right;'>TOTALES:</td>")
            .append("    <td style='text-align:right;'>").append(sumLines).append("</td>")
            .append("    <td style='text-align:right;'>").append(DisplayType.getNumberFormat(DisplayType.Amount).format(sumAllocated)).append("</td>")
            .append("    <td style='text-align:right;'>").append(DisplayType.getNumberFormat(DisplayType.Amount).format(sumDeducted)).append("</td>")
            .append("    <td style='text-align:right; color:#004085;'>").append(DisplayType.getNumberFormat(DisplayType.Amount).format(sumNet)).append("</td>")
            .append("    <td colspan='4'></td>")
            .append("  </tr>");

        html.append("</table>");
        html.append("</div>");

        return html.toString();
    }
	
	public static File generateReceiptsSummaryExcel(List<AMNReceipts> receiptsList) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Resumen de Nómina");
        Properties ctx = Env.getCtx();
        
        // Estilos de Encabezado
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Cabecera de la Tabla (Se agrega Nº como col 0 e Contabilizado como col 8)
        String[] columns = {
            Msg.translate(ctx, "No"), 
            Msg.getElement(ctx, "AMN_Payroll_ID"), 
            Msg.getElement(ctx, "Value"), // "Ficha/Código"
            Msg.getElement(ctx, "AMN_Employee_ID"), //"Trabajador" 
            Msg.getElement(ctx, "Line")+"(s)",  //"Líneas", 
            Msg.getElement(ctx, "AmountAllocated"), //"Asignaciones", 
            Msg.getElement(ctx, "AmountDeducted"), //"Deducciones", 
            Msg.getElement(ctx, "AmountNetpaid"), //"Neto", 
            Msg.getElement(ctx, "Posted"),		//"Contabilizado", 
            Msg.translate(ctx, "InvoiceProcessed"), //"Factura Creada", 
            Msg.getElement(ctx, "C_Invoice_ID"),	//"ID Factura", 
            Msg.translate(ctx, "Messages")+ " / "+Msg.translate(ctx, "Errors"), //"Mensaje / Error"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Llenado de Filas
        int rowNum = 1;
        int itemSeq = 1; // Contador consecutivo

        for (AMNReceipts rec : receiptsList) {
            Row row = sheet.createRow(rowNum++);

            BigDecimal allocated = rec.AmountAllocated != null ? rec.AmountAllocated : BigDecimal.ZERO;
            BigDecimal deducted = rec.AmountDeducted != null ? rec.AmountDeducted : BigDecimal.ZERO;
            BigDecimal net = allocated.subtract(deducted);

            row.createCell(0).setCellValue(itemSeq++); // 1. Consecutivo (1, 2, 3...)
            row.createCell(1).setCellValue(rec.DocumentNo != null ? rec.DocumentNo : "");
            row.createCell(2).setCellValue(rec.Employee_Value != null ? rec.Employee_Value : "");
            row.createCell(3).setCellValue(rec.Employee_Name != null ? rec.Employee_Name : "");
            row.createCell(4).setCellValue(rec.NoLines);
            row.createCell(5).setCellValue(allocated.doubleValue());
            row.createCell(6).setCellValue(deducted.doubleValue());
            row.createCell(7).setCellValue(net.doubleValue());
            row.createCell(8).setCellValue(rec.RecIsPosted ? "SÍ" : "NO"); // 2. Columna RecIsPosted
            row.createCell(9).setCellValue(rec.InvoiceIsCreated ? "SÍ" : "NO");
            row.createCell(10).setCellValue(rec.C_Invoice_ID > 0 ? rec.C_Invoice_ID : 0);
            row.createCell(11).setCellValue(rec.Error != null ? rec.Error : "");
        }

        // Auto-ajustar ancho de columnas
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Guardar en archivo temporal
        try {
            File file = File.createTempFile("Resumen_Nomina_", ".xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
            workbook.close();
            return file;
        } catch (Exception e) {
            throw new AdempiereException("Error al generar archivo Excel de resumen: " + e.getMessage(), e);
        }
    }

}

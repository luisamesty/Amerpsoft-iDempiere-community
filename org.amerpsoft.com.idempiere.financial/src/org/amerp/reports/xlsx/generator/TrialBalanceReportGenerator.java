package org.amerp.reports.xlsx.generator;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.TrialBalanceLine;
import org.amerp.reports.xlsx.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class TrialBalanceReportGenerator extends AbstractXlsxGenerator {

	private static final CLogger log = CLogger.getCLogger(TrialBalanceReportGenerator.class);

	private static final int headerRows = 4;
	// Cabeceras, incluyendo saldos y organización
    private final String[] headers = { 
        "value", "name", "AD_Org_ID", 
        "SI", "dr", "cr", "per", "Balanace" 
    };
    //Anchos proporcionales para las  columnas
    private int[] maxLen = { 15, 30, 10, 15, 15, 15, 15, 15 };
    @Override
    public String getReportName() {
        return "TrialBalanceReport"; // Nombre del archivo y de la hoja
    }


    @Override
    protected void writeReportSpecificHeader(int AD_Client_ID) {

    	// --- 1️⃣ Leer constantes globales antes del bucle
        String cliName = "";
        String cliDescription = "";
        byte[] cliLogo = null;
        MClient mclient = new MClient(Env.getCtx(),AD_Client_ID, null);
        if (mclient != null ) {
        	cliName = mclient.getName();
        	cliDescription = mclient.getDescription() != null ? mclient.getDescription() : mclient.getName();
        	 // --- 2️⃣ Obtener información del cliente (AD_ClientInfo)
            MClientInfo ci = MClientInfo.get(Env.getCtx(), AD_Client_ID);
            if (ci != null && ci.getLogoReport_ID() > 0) {
                // --- 3️⃣ Obtener el logo (AD_Image)
                MImage img = new MImage(Env.getCtx(), ci.getLogoReport_ID(), null);
                if (img != null && img.getBinaryData() != null) {
                    cliLogo = img.getBinaryData();
                }
            }	
        }
        // Escribir el nombre del reporte y el nombre del cliente en las primeras filas
        if (cliLogo != null && cliLogo.length > 0) {
            try {
                int pictureIdx = workbook.addPicture(cliLogo, Workbook.PICTURE_TYPE_PNG);
                CreationHelper helper = workbook.getCreationHelper();
                Drawing<?> drawing = sheet.createDrawingPatriarch();

                // --- Reservar espacio para el logo
                sheet.setColumnWidth(0, 20 * 256);  // Aumenta ancho columna A
                for (int i = 0; i < 4; i++) {       // 4 filas de alto
                    SXSSFRow row = sheet.getRow(i);
                    if (row == null)
                        row = sheet.createRow(i);
                    row.setHeightInPoints(25);       // alto de fila visible
                }

                // --- Definir posición y tamaño exacto
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0); // columna inicial
                anchor.setRow1(0); // fila inicial
                anchor.setDx1(0);
                anchor.setDy1(0);
                anchor.setDx2(ExcelUtils.pixelToEMU(120)); 	// ancho 120
                anchor.setDy2(ExcelUtils.pixelToEMU(34));		// alto 34:x

                Picture pict = drawing.createPicture(anchor, pictureIdx);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Titulo del Reporte
        SXSSFRow titleRow = sheet.createRow(1); 
        Cell cellTitle = titleRow.createCell(1); 
        cellTitle.setCellValue("Trial Balance Report");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellTitle.setCellStyle(titleStyle);
        
        // --- Nombre de la empresa
        SXSSFRow nameRow = sheet.createRow(2);
        Cell cellName = nameRow.createCell(0); 
        cellName.setCellValue(cliName);
        CellStyle nameStyle = workbook.createCellStyle();
        Font nameFont = workbook.createFont();
        nameFont.setFontHeightInPoints((short) 14);
        nameFont.setBold(true);
        nameStyle.setFont(nameFont);
        nameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellName.setCellStyle(nameStyle);

        // --- Descripción de la empresa
        SXSSFRow descRow = sheet.createRow(3); // fila 1
        Cell cellDesc = descRow.createCell(0);
        cellDesc.setCellValue(cliDescription);
        CellStyle descStyle = workbook.createCellStyle();
        Font descFont = workbook.createFont();
        descFont.setFontHeightInPoints((short) 12);
        descStyle.setFont(descFont);
        cellDesc.setCellStyle(descStyle);

        // --- Opcional: hacer merge de celdas para nombre y descripción
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5)); // fila 0, columnas 1 a 5
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 5)); // fila 1, columnas 1 a 5
        

        // Escribir la cabecera de las columnas (Name, Description, Total, etc.)
        // --- Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
    }

    @Override
    protected void writeColumnHeader() {
        // La variable headerRows es 4 (fila 5 del Excel)
    	
        // Reajustar el ancho de las columnas
    	for (int col = 0; col < maxLen.length; col++) {
    	    sheet.setColumnWidth(col, maxLen[col] * 256);
    	}

        // Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 10); // Tamaño más pequeño para más columnas
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        // Escribir la cabecera en la fila 4
    	SXSSFRow headerRow = sheet.createRow(headerRows);
    	// Establecer una altura mínima  (ej. 15 puntos)
    	headerRow.setHeightInPoints(15.0f);
    	for (int i = 0; i < headers.length; i++) {
    	    String translated = Msg.getMsg(Env.getCtx(), headers[i]); 
    	    Cell cell = headerRow.createCell(i);
    	    cell.setCellValue(translated);
    	    cell.setCellStyle(headerStyle);
    	}
    }
    
    @Override
    protected void generateReportContent() {

        // --- 1. Obtener todos los parámetros necesarios para la Query
        Integer AD_Client_ID = (Integer) parameters.get("AD_Client_ID");
        Integer C_AcctSchema_ID = (Integer) parameters.get("C_AcctSchema_ID");
        Integer AD_Org_ID = (Integer) parameters.get("AD_Org_ID");
        Integer AD_OrgParent_ID = (Integer) parameters.get("AD_OrgParent_ID");
        Integer C_Period_ID = (Integer) parameters.get("C_Period_ID");
        String PostingType = (String) parameters.get("PostingType");
        Integer C_ElementValue_ID = (Integer) parameters.get("C_ElementValue_ID");
        Timestamp DateFrom = (Timestamp) parameters.get("DateFrom");
        Timestamp DateTo = (Timestamp) parameters.get("DateTo");
        String isShowZERO = (String) parameters.get("isShowZERO");
        String isShowOrganization = (String) parameters.get("isShowOrganization");
        String trxName = (String) parameters.get("AD_PInstance_ID"); // Usar PInstance como trxName
        
        List<TrialBalanceLine> reportData = DataPopulator.getTrialBalanceData(
                AD_Client_ID, C_AcctSchema_ID, AD_Org_ID, AD_OrgParent_ID, 
                C_Period_ID, PostingType, C_ElementValue_ID, 
                DateFrom, DateTo, isShowZERO, trxName);
        
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el Balance de Comprobación.");
            return;
        }

        // --- 3. Escribir Cabeceras
        writeClientHeader(AD_Client_ID); // Escribe Logo, Título y Nombre
//        writeColumnHeader();             // Escribe las cabeceras de columna (fila 4)

        int total = reportData.size();
        int batchSize = 100;
        int rowNum = headerRows + 1; // La data comienza en la fila 5

        // --- 4. Iterar y escribir el contenido
        for (int i = 0; i < total; i++) {
            TrialBalanceLine e = reportData.get(i);
            SXSSFRow row = sheet.createRow(rowNum++);

            // --- Determinar Estilos y Contenido
            int level = e.getLevel();
            String tipoRegistro = e.getTipoRegistro(); // R, 60, 50

            // Formatear la cuenta con sangría (Indentación)
            String paddedName = ExcelUtils.padLeft(e.getNombre(), level);
            String orgValue = e.getOrgValue() != null ? e.getOrgValue() : "";

            // Determinar estilo (Negrita para totales/resumen, cursiva para detalle org)
            boolean bold = "R".equals(tipoRegistro) || "60".equals(tipoRegistro);
            String key = (bold ? "B" : "") + ("50".equals(tipoRegistro) ? "I" : "N"); // B = Bold, I = Italic, N = Normal
            CellStyle style = styleMap.getOrDefault(key, styleMap.get("N")); 

            // Estilos para números (requiere crear NumberStyles con formato)
            //CellStyle numberStyle = styleMap.getOrDefault(key + "N", styleMap.get("N_NUM"));
            CellStyle numberStyle = styleMap.getOrDefault(key + "N", styleMap.get("N_STANDARD"));
            // --- Columna 0: Cód. Cuenta (con sangría)
            ExcelUtils.createStyledCell(row, 0, e.getCodigo(), style);
            ExcelUtils.updateMaxLen(maxLen, 0, e.getCodigo());

            // --- Columna 1: Nombre Cuenta
            ExcelUtils.createStyledCell(row, 1, paddedName, style);
            ExcelUtils.updateMaxLen(maxLen, 1, paddedName);

            // --- Columna 2: Organización (solo para tipo 50, nulo para R/60)
            ExcelUtils.createStyledCell(row, 2, orgValue, style);
            ExcelUtils.updateMaxLen(maxLen, 2, orgValue);
            // --- Columnas 3-7: Saldos (BigDecimals)
            int col = 3;
            ExcelUtils.createStyledCell(row, col++, e.getOpenBalance(), numberStyle);
            ExcelUtils.createStyledCell(row, col++, e.getAmtAcctDr(), numberStyle);
            ExcelUtils.createStyledCell(row, col++, e.getAmtAcctCr(), numberStyle);
            ExcelUtils.createStyledCell(row, col++, e.getBalancePeriodo(), numberStyle);
            ExcelUtils.createStyledCell(row, col++, e.getCloseBalance(), numberStyle);
            
            // Note: Las columnas de saldo no necesitan updateMaxLen si se usa formato de número fijo

            if ((i + 1) % batchSize == 0) {
                log.warning(Msg.getMsg(Env.getCtx(), "Processing")+": "+ (i + 1) + 
                		Msg.getMsg(Env.getCtx(), "of")+" "+total +
                		Msg.getMsg(Env.getCtx(), "Records"));
                
                try {
					this.sheet.flushRows(batchSize);
				} catch (IOException e1) {
					log.severe("Error flushing rows: " + e1.getMessage());
				}
            }
        }

        try {
			this.sheet.flushRows();
		} catch (IOException e) {
			log.severe("Error final flushing rows: " + e.getMessage());
		}

        // Ajuste de ancho de columna final (Solo aplica a las columnas de texto)
        for (int col = 0; col < 3; col++) { // Solo las primeras 3 columnas (Código, Nombre, Org)
            int chars = Math.min(100, Math.max(10, maxLen[col] + 2));
            sheet.setColumnWidth(col, chars * 256);
        }
    }
    
    @Override
    protected String[] getColumnHeaders() {
        return this.headers;
    }

    @Override
    protected int[] getColumnWidths() {
        // Nota: El viewer utiliza maxLen para widths
        return this.maxLen; 
    }

    @Override
    protected int getHeaderRowCount() {
        return headerRows;
    }

}

package org.amerp.reports.xlsx.generator;

import java.io.IOException;
import java.util.List;
import org.amerp.reports.AccountElementBasic;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.xlsx.util.ExcelUtils;
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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.CLogger;

public class AccountElementsReportGenerator extends AbstractXlsxGenerator {

	private static final CLogger log = CLogger.getCLogger(AccountElementsReportGenerator.class);
	private static final int headerRows = 4;
	int[] maxLen = { 15, 20, 10, 10, 10, 10, 15 }; // ancho aproximado proporcional
	private String[] headers = { "value", "description", "AccountType", "AccountSign", "IsDocControlled", "IsSummary", "Parent_ID" };

    @Override
    public String getReportName() {
        return "AccountElementsReport"; // Nombre del archivo y de la hoja
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
                    XSSFRow row = sheet.getRow(i);
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
        XSSFRow titleRow = sheet.createRow(1);
        Cell cellTitle = titleRow.createCell(1); 
        cellTitle.setCellValue("Account Elements Catalog");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellTitle.setCellStyle(titleStyle);
        
        // --- Nombre de la empresa
        XSSFRow nameRow = sheet.createRow(2); 
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
        XSSFRow descRow = sheet.createRow(3); // fila 1
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

    	 // Escribir la cabecera de las columnas (Name, Description, Total, etc.)
        // --- Crear estilo para encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        // Ancho columnas
    	for (int col = 0; col < maxLen.length; col++) {
    	    sheet.setColumnWidth(col, maxLen[col] * 256);
    	}
        // ESCRITURA DE LA CABECERA (usando la variable de INSTANCIA headerRows)
        XSSFRow headerRow = sheet.createRow(headerRows); // Usa la variable de INSTANCIA 'headerRows = 4'
        headerRow.setHeightInPoints(15.0f); // Altura mínima

        for (int i = 0; i < this.headers.length; i++) {
            String translated = Msg.translate(Env.getCtx(), this.headers[i]); 
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(translated);
            cell.setCellStyle(headerStyle);
        }

    }
    
    @Override
    protected void generateReportContent() {

    	// Obtener parámetros necesarios
        Integer AD_Client_ID = (Integer) parameters.get("AD_Client_ID");
        Integer C_AcctSchema_ID = (Integer) parameters.get("C_AcctSchema_ID");
        
        // Lógica Específica del Balance de Comprobación:
        List<AccountElementBasic> reportData = DataPopulator.getAccountElementBasicList(
                AD_Client_ID, C_AcctSchema_ID);
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el informe.");
            return;
        }

        // Escribir Cabecera Común (Implementado en AbstractXlsxGenerator)
        writeClientHeader(AD_Client_ID);

        int total = reportData.size();
        int batchSize = 100;
        int rowNum = headerRows + 1;

        for (int i = 0; i < total; i++) {
            AccountElementBasic e = reportData.get(i);
            XSSFRow row = sheet.createRow(rowNum++);

            Integer level = e.getLevel() != null ? e.getLevel() : 0;
            String v1 = ExcelUtils.safeString(e.getCodigo());
            String v2 = ExcelUtils.safeString(e.getDescription()); // o e.getName() si Description no existe
            String v3 = ExcelUtils.safeString(e.getAccountType());
            String v4 = ExcelUtils.safeString(e.getAccountSign()); // si no existe el método, usa un placeholder
            String v5 = ExcelUtils.safeString(e.getIsDocControlled());
            String v6 = ExcelUtils.safeString(e.getIsSummary());
            String v7 = "";
            String[] acctParent = e.getAcctParent();
            if (acctParent != null && acctParent.length > 1)
                v7 = acctParent[acctParent.length - 2];


            // --- Determinar estilo
            boolean bold = "Y".equalsIgnoreCase(v6);
            String key = "L" + Math.min(9, Math.max(1, level)) + (bold ? "B" : "");
            CellStyle style = styleMap.getOrDefault(key, styleMap.get("L6"));

            // --- Crear celdas con estilo
            ExcelUtils.createStyledCell(row, 0, v1, style);
            ExcelUtils.createStyledCell(row, 1, v2, style);
            ExcelUtils.createStyledCell(row, 2, v3, style);
            ExcelUtils.createStyledCell(row, 3, v4, style);
            ExcelUtils.createStyledCell(row, 4, v5, style);
            ExcelUtils.createStyledCell(row, 5, v6, style);
            ExcelUtils.createStyledCell(row, 6, v7, style);

            ExcelUtils.updateMaxLen(maxLen, 0, v1);
            ExcelUtils.updateMaxLen(maxLen, 1, v2);
            ExcelUtils.updateMaxLen(maxLen, 2, v3);
            ExcelUtils.updateMaxLen(maxLen, 3, v4);
            ExcelUtils.updateMaxLen(maxLen, 4, v5);
            ExcelUtils.updateMaxLen(maxLen, 5, v6);
            ExcelUtils.updateMaxLen(maxLen, 6, v7);

            if ((i + 1) % batchSize == 0) {
                log.warning(Msg.getMsg(Env.getCtx(), "Processing")+": "+ (i + 1) + 
                		Msg.getMsg(Env.getCtx(), "of")+" "+total +
                		Msg.getMsg(Env.getCtx(), "Records"));
      
            }
        }

        for (int col = 0; col < headers.length; col++) {
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
package org.amerp.reports.xlsx.generator;

import java.util.List;
import org.amerp.reports.AccountElementBasic;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.xlsx.util.ExcelUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
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
    	Row row;
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

                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0);
                anchor.setRow1(0);
                anchor.setCol2(1);
                anchor.setRow2(4);

                drawing.createPicture(anchor, pictureIdx);
            } catch (Exception e) {
                log.warning("Error insertando logo: " + e.getMessage());
            }
        }
        // --- TÍTULO DEL INFORME
        row = sheet.createRow(1);
        Cell cellTitle = row.createCell(1);
        cellTitle.setCellValue("Account Elements Catalog");
        CellStyle titleStyle = styleMap.get("L3B"); 
        cellTitle.setCellStyle(titleStyle);

        // --- NOMBRE CLIENTE
        row = sheet.createRow(2);
        Cell cellName = row.createCell(0);
        cellName.setCellValue(cliName);
        cellName.setCellStyle(styleMap.get("L3B"));

        // --- DESCRIPCIÓN
        row = sheet.createRow(3);
        Cell cellDesc = row.createCell(0);
        cellDesc.setCellValue(cliDescription);
        cellDesc.setCellStyle(styleMap.get("L3B"));
        
        
    }

    @Override
    protected void writeColumnHeader() {

        // Reajustar el ancho de las columnas
    	for (int col = 0; col < maxLen.length; col++) {
    	    sheet.setColumnWidth(col, maxLen[col] * 256);
    	}
        // Crear la fila del encabezado (fila 4 si headerRows = 4)
        Row headerRow = sheet.createRow(headerRows);
        headerRow.setHeightInPoints(15f); // Altura fija o mínima
        
        // Usamos estilo común ya definido o creamos uno solo (no por celda)
        CellStyle headerStyle = styleMap.get("HEADER");
        if (headerStyle == null) {
            headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.LEFT);
            styleMap.put("HEADER", headerStyle);
        }
        
        for (int i = 0; i < this.headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            String translated = Msg.translate(Env.getCtx(), this.headers[i]); 
            
            cell.setCellValue(translated);
            cell.setCellStyle(headerStyle);
        }

    }
    
    @Override
    protected void generateReportContent() {

    	// Obtener parámetros necesarios para la Query
        Integer AD_Client_ID = (Integer) parameters.get("AD_Client_ID");
        Integer C_AcctSchema_ID = (Integer) parameters.get("C_AcctSchema_ID");
        
        // Obtener Datos
        List<AccountElementBasic> reportData = DataPopulator.getAccountElementBasicList(
                AD_Client_ID, C_AcctSchema_ID);
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el informe.");
            return;
        }

        // Crear hoja y encabezados generales 
        writeClientHeader(AD_Client_ID);

        int total = reportData.size();
        int batchSize = 100;
        int rowNum = headerRows + 1;

        for (int i = 0; i < total; i++) {
            AccountElementBasic e = reportData.get(i);
            XSSFRow row = sheet.createRow(rowNum++);

            Integer level = e.getLevel() != null ? e.getLevel() : 0;
            String v1 = ExcelUtils.safeString(e.getCodigo());
            String v2 = ExcelUtils.safeString(e.getDescription());
            String v3 = ExcelUtils.safeString(e.getAccountType());
            String v4 = ExcelUtils.safeString(e.getAccountSign());
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
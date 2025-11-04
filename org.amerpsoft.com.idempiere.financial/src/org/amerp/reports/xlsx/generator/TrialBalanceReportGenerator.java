package org.amerp.reports.xlsx.generator;

import java.sql.Timestamp;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.TrialBalanceLine;
import org.amerp.reports.xlsx.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
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
        "BeginningBalance", "AmtAcctDr", "AmtAcctCr", "C_Period_ID", "Balance" 
    };
    //Anchos proporcionales para las  columnas
    private int[] maxLen = { 15, 25, 10, 16, 16, 16, 16, 16 };
    @Override
    public String getReportName() {
        return "TrialBalanceReport"; // Nombre del archivo y de la hoja
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
        cellTitle.setCellValue("Trial Balance Report");
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
 
        // Escribir cabeceras traducidas
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            String translated = Msg.translate(Env.getCtx(), headers[i]);

            cell.setCellValue(translated != null ? translated : headers[i]);
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
        
        // Obtener Datos
        List<TrialBalanceLine> reportData = DataPopulator.getTrialBalanceData(
                AD_Client_ID, C_AcctSchema_ID, AD_Org_ID, AD_OrgParent_ID, 
                C_Period_ID, PostingType, C_ElementValue_ID, 
                DateFrom, DateTo, isShowZERO, trxName);
        
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el Balance de Comprobación.");
            return;
        }

        // Crear hoja y encabezados generales ---
        writeClientHeader(AD_Client_ID); // Escribe Logo, Título y Nombre

        int total = reportData.size();
        int batchSize = 100;
        int rowNum = headerRows + 1;

        // Reusar estilos desde styleMap
        CellStyle textNormal = styleMap.get("TEXT_N");
        CellStyle textBold   = styleMap.get("TEXT_B");
        CellStyle numNormal  = styleMap.get("NUM_N");
        CellStyle numBold    = styleMap.get("NUM_B");
        
        //  Escribir filas del reporte
        for (int i = 0; i < total; i++) {
            TrialBalanceLine e = reportData.get(i);
            //
            int level = e.getLevel();
            String tipoRegistro = e.getTipoRegistro(); 

            // FILTRADO Organizacion
            boolean skipOrganization = "N".equalsIgnoreCase(isShowOrganization) && "50".equals(tipoRegistro);
            if (skipOrganization) {
                // Si isShowOrganization es 'N' y la línea es de tipo '50' (Organización), saltar esta iteración.
                continue; 
            }
            // Nueva Fila
            Row row = sheet.createRow(rowNum++);
            // Formatear la cuenta con sangría (Indentación)
            String paddedName = ExcelUtils.padLeft(e.getNombre(), level);
            String orgValue = e.getOrgValue() != null ? e.getOrgValue() : "";

            // Determinar estilo
            boolean bold = "R".equals(tipoRegistro) || "60".equals(tipoRegistro);
            CellStyle tStyle = bold ? textBold : textNormal;
            CellStyle nStyle = bold ? numBold : numNormal;

            // --- Columna 0: Cód. Cuenta (con sangría)
            ExcelUtils.createStyledCell(row, 0, e.getCodigo(), tStyle);
            ExcelUtils.updateMaxLen(maxLen, 0, e.getCodigo());

            // --- Columna 1: Nombre Cuenta
            ExcelUtils.createStyledCell(row, 1, paddedName, tStyle);
            ExcelUtils.updateMaxLen(maxLen, 1, paddedName);

            // --- Columna 2: Organización (solo para tipo 50, nulo para R/60)
            ExcelUtils.createStyledCell(row, 2, orgValue, tStyle);
            ExcelUtils.updateMaxLen(maxLen, 2, orgValue);
            // --- Columnas 3-7: Saldos (BigDecimals)
            int col = 3;
            ExcelUtils.createStyledCell(row, col++, e.getOpenBalance(), nStyle);
            ExcelUtils.createStyledCell(row, col++, e.getAmtAcctDr(), nStyle);
            ExcelUtils.createStyledCell(row, col++, e.getAmtAcctCr(), nStyle);
            ExcelUtils.createStyledCell(row, col++, e.getBalancePeriodo(), nStyle);
            ExcelUtils.createStyledCell(row, col++, e.getCloseBalance(), nStyle);
            
            if ((i + 1) % batchSize == 0) {
                log.warning(Msg.getMsg(Env.getCtx(), "Processing")+": "+ (i + 1) + 
                		Msg.getMsg(Env.getCtx(), "of")+" "+total +
                		Msg.getMsg(Env.getCtx(), "Records"));
            }
        }

        // Ajuste de ancho de columna final (Aplicar a todas)
        for (int col = 0; col < maxLen.length; col++) { 
            
            // Obtener el ancho deseado: Máximo de 100 caracteres, mínimo de 10, y añadir 2 de padding base.
            int chars = Math.min(100, Math.max(10, maxLen[col] + 2)); 
            
            // Unidades base POI: Caracteres * 256
            int desiredWidthUnits = chars * 256; 
            
            // Aplicar HOLGURA EXTRA (PADDING)
            int extraPadding = 1024; // 1024 unidades POI son aproximadamente 4 caracteres extra.
            
            if (desiredWidthUnits > 0) {
                this.sheet.setColumnWidth(col, desiredWidthUnits + extraPadding); 
            }
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

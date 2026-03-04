package org.amerp.reports.xlsx.generator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.OrgTree;
import org.amerp.reports.TrialBalanceLine;
import org.amerp.reports.xlsx.constants.FinancialReportConstants;
import org.amerp.reports.xlsx.util.ExcelUtils;
import org.amerp.reports.xlsx.util.MsgUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MCurrency;
import org.compiere.model.MImage;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;

public class RPTTrialBalanceByDates extends AbstractXlsxGenerator {

	private static final CLogger log = CLogger.getCLogger(RPTTrialBalanceByDates.class);

	private static final int headerRows = 4;
	// Cabeceras, incluyendo saldos y organización
    private final String[] headers = { 
        "value", "name", "AD_Org_ID", 
        "BeginningBalance", "AmtAcctDr", "AmtAcctCr", "C_Period_ID", "Balance" 
    };
    //Anchos proporcionales para las  columnas
    private int[] maxLen = { 15, 25, 10, 16, 16, 16, 16, 16 };
    private int orgColNameLen = 16;
    // Organizaciones seleccionadas
    List<OrgTree> orgs  = null;
    List<Integer> selectedOrgIDs = null;
    Map<Integer, String> orgValues = null;
    
    @Override
    public String getReportName() {
        return "RPTTrialBalanceByDates"; // Nombre del archivo y de la hoja
    }

	// ===================================================================
    // 📢 TÍTULOS (Usando parámetros)
    // ===================================================================

    @Override
    protected String getReportTitle(Map<String, Object> parameters) {
    	// Lee el valor traducido de los parámetros
        String title = (String) parameters.get("ReportTitle");
        return title != null ? title : "Trial Balance Report by Dates"; 
    }

    @Override
    protected String getReportSubTitle(Map<String, Object> parameters) {
    	String subTitle = "";
        String isShowSummaryElements = (String) parameters.get("isShowSummaryElements");
        subTitle = MsgUtils.getTranslatedPrintName("isShowSummaryElements",Env.getAD_Language(Env.getCtx()))+
        		"("+MsgUtils.getTranslatedYesNo(isShowSummaryElements)+")";
        return subTitle;
    }
    
    @Override
    protected void writeReportSpecificHeader(int AD_Client_ID,  Map<String, Object> parameters) {

    	// --- 1️⃣ Leer constantes globales antes del bucle
    	Row row;
        String cliName = "";
        String cliDescription = "";
        byte[] cliLogo = null;
        int C_Currency_ID = 0;
        String currencyName="";
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
            // Esquema Contable y Moneda
            Integer C_AcctSchema_ID = (Integer) parameters.get("C_AcctSchema_ID");
            if (C_AcctSchema_ID != null) {
            	MAcctSchema as = MAcctSchema.get (Env.getCtx(), C_AcctSchema_ID, null);
    			C_Currency_ID = as.getC_Currency_ID();
            }
            if (C_Currency_ID > 0) {
            	MCurrency currency = new MCurrency(ctx, C_Currency_ID, null);
            	currencyName = currency.getISO_Code() + " - " + currency.getDescription();
            }
        }
        // 🏆 INICIALIZACIÓN DE LA LISTA DE ORGANIZACIONES (Solo se ejecuta si es nula)
        if (this.orgs == null) {
            
            Integer AD_Org_ID = (Integer) parameters.get("AD_Org_ID");
            Integer AD_OrgParent_ID = (Integer) parameters.get("AD_OrgParent_ID");
            
            // Ejecutar y almacenar en la variable de instancia
            if (AD_Org_ID == 0 || AD_Org_ID == null) {
                this.orgs = DataPopulator.getOrgTreeListfromParent(AD_Client_ID, AD_OrgParent_ID);
            } else {
                this.orgs = DataPopulator.getOrgTreeList(AD_Client_ID, AD_Org_ID, AD_OrgParent_ID);
            }
            
            // Poner la lista en el mapa de parámetros para que otros métodos puedan acceder a ella
            this.parameters.put("OrgTreeList", this.orgs);
        }
        // Organizaciones Seleccionadas this.orgs Ya está disponible
        if (orgs.size() == 1) {
        	// Si solo hay una organización, usa el elemento en el índice 0.
            OrgTree singleOrg = orgs.get(0);
            // Concatenar orgValue y orgName
            cliDescription = cliDescription + singleOrg.getOrgValue() + " - " + singleOrg.getOrgName();
        } else if (orgs.size() > 1) {
        	// Si hay múltiples organizaciones, usa el valor de 'allOrgs' del primer elemento.
            cliDescription = cliDescription+orgs.get(0).getAllOrgs();
        } else {
        	cliDescription = cliDescription+ Msg.translate(Env.getCtx(), "NoOrgSelected");
        }
        // Obtener los nombres de las organizaciones (debe estar disponible)
        selectedOrgIDs = DataPopulator.getSelectedOrgIDs(orgs);
        orgValues = DataPopulator.getOrgValues(orgs);     
        // OBTENER Y FORMATEAR FECHAS DE PARÁMETROS
        String dateRange = "";
        Timestamp dateFromTimestamp = (Timestamp) parameters.get("DateFrom");
        Timestamp dateToTimestamp = (Timestamp) parameters.get("DateTo");
        // Formato de fecha legible (DD/MM/YYYY o similar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (dateFromTimestamp != null && dateToTimestamp != null) {
            dateRange = Msg.translate(Env.getCtx(), "C_Period_ID")+ " " +
            		Msg.translate(Env.getCtx(), "from")+ ": " +
            		dateFormat.format(dateFromTimestamp)+ " " + 
            		Msg.getMsg(Env.getCtx(), "to")+ ": " +
            		dateFormat.format(dateToTimestamp);
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
        row = sheet.createRow(0);
        Cell cellTitle = row.createCell(1);
        cellTitle.setCellValue(getReportTitle(parameters));
        CellStyle titleStyle = styleMap.get("L1B"); 
        cellTitle.setCellStyle(titleStyle);
        // 🏆 COMBINAR CELDAS DEL TITULO (Fila 0, Columnas 1 a 3)
   		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
        // --- 📅 ETIQUETA DE FECHA (Fila 1 - Columna 4)
        Cell cellDateLabel = row.createCell(4); 
        cellDateLabel.setCellValue(Msg.translate(Env.getCtx(), "Date"));
        cellDateLabel.setCellStyle(styleMap.get("L1B")); 
        // --- 🗓️ VALOR DE LA FECHA DEL REPORTE (Fila 1 - Columna 5)
        Cell cellDateValue = row.createCell(5);
        // Obtener la fecha del contexto de ejecución del reporte
        java.util.Date reportDate = Env.getContextAsDate(Env.getCtx(), "ReportDate"); 
        if (reportDate != null) {
            // Formatear la fecha al estilo de iDempiere
            String formattedDate = DisplayType.getDateFormat(DisplayType.Date).format(reportDate);
            cellDateValue.setCellValue(formattedDate);
        } else {
            cellDateValue.setCellValue("N/A");
        }
        cellDateValue.setCellStyle(styleMap.get("L1B"));
        // SUB-TITULO DEL INFORME (Parametros seleccionados)
        row = sheet.createRow(1);
        Cell cellSubTitle = row.createCell(1);
        cellSubTitle.setCellValue(getReportSubTitle(parameters));
        CellStyle subTitleStyle = styleMap.get("L3B"); 
        cellSubTitle.setCellStyle(subTitleStyle);
        // --- NOMBRE CLIENTE
        row = sheet.createRow(2);
        Cell cellName = row.createCell(0);
        cellName.setCellValue(cliName);
        cellName.setCellStyle(styleMap.get("L3B"));
        // --- RANGO DE PERÍODO (Fila 2 - Columna 1)
        Cell cellPeriod = row.createCell(1);
        cellPeriod.setCellValue(dateRange);
        cellPeriod.setCellStyle(styleMap.get("TEXT_N")); 
        // 🏆 COMBINAR CELDAS DEL PERÍODO (Fila 2, Columnas 1 a 3)
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
		// --- 💰 ETIQUETA DE MONEDA (Fila 2 - Columna 4)
		Cell cellCurrencyLabel = row.createCell(4); // Creamos en Columna 4
		// Obtener el nombre traducido del campo "C_Currency_ID"
		String currencyLabel = Msg.translate(Env.getCtx(), "C_Currency_ID");
		cellCurrencyLabel.setCellValue(currencyLabel + ":"); 
		cellCurrencyLabel.setCellStyle(styleMap.get("TEXT_B")); 

		// --- VALOR DE MONEDA (Fila 2 - Columna 5)
		Cell cellCurrencyValue = row.createCell(5); 
		cellCurrencyValue.setCellValue(currencyName); 
		cellCurrencyValue.setCellStyle(styleMap.get("TEXT_N")); 
        // --- DESCRIPCIÓN
        row = sheet.createRow(3);
        Cell cellDesc = row.createCell(0);
        cellDesc.setCellValue(cliDescription);
        cellDesc.setCellStyle(styleMap.get("TEXT_B_WRAP"));
        // 🏆 (Columna 0 hasta Columna 3 en la Fila 3)
        // CellRangeAddress(firstRow, lastRow, firstCol, lastCol)
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));
        
    }

    @Override
    protected void writeColumnHeader(Map<String, Object> parameters) {
        
    	// Indica si muestra Movimientos o Balance
        String isShowMovementsAmounts = (String)  parameters.get("isShowMovementsAmounts");
        String isShowCrosstab = (String) parameters.get("isShowCrosstab");  
        
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
 
        // Escribir cabeceras traducidas Columnas fijas
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            String translated = Msg.translate(Env.getCtx(), headers[i]);

            cell.setCellValue(translated != null ? translated : headers[i]);
            cell.setCellStyle(headerStyle);
        }
         
        
        // Escribir cabeceras traducidas
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            String translated = Msg.translate(Env.getCtx(), headers[i]);

            cell.setCellValue(translated != null ? translated : headers[i]);
            cell.setCellStyle(headerStyle);
        }
        // Escribir cabeceras Organizaciones
        if (isShowCrosstab != null && isShowCrosstab.compareToIgnoreCase("Y")==0) {
        	int colIndex = headers.length; // Columna inicial
        	// Prefijo Organizacion
        	String prefixOrgValue = isShowMovementsAmounts.compareToIgnoreCase("Y") == 0 ? 
        			Msg.translate(Env.getCtx(),"C_Period_ID")+"-" : 
        			Msg.translate(Env.getCtx(), "Balance")+"-";
            // 2. MODO CROSSTAB: Saldos Finales Dinámicos por Organización
            for (Integer orgID : selectedOrgIDs) {
                String orgValue = orgValues.get(orgID);
                String headerText =  prefixOrgValue+orgValue ;
                
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(headerText);
                cell.setCellStyle(headerStyle);
            }

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
        String isShowCrosstab = (String) parameters.get("isShowCrosstab");
        String trxName = (String) parameters.get("AD_PInstance_ID"); // Usar PInstance como trxName
        String isShowMovementsAmounts = (String)  parameters.get("isShowMovementsAmounts");
        String isShowSummaryElements = (String) parameters.get("isShowSummaryElements");
        String isShowSubTotal = (String) parameters.get("isShowSubTotal");
        String isPositiveBalance = (String) parameters.get("isPositiveBalance");
        String ReportTitle = (String) parameters.get("ReportTitle");
        
        // Obtener Datos  (C_ElementValue_ID = null para TrialBalance) 
        List<TrialBalanceLine> reportData = DataPopulator.getTrialBalanceDataByDates(
                AD_Client_ID, C_AcctSchema_ID, AD_Org_ID, AD_OrgParent_ID, 
                C_Period_ID, PostingType, null, 
                DateFrom, DateTo, isShowZERO, trxName);
        
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el Balance de Comprobación.");
            return;
        }
        Boolean isCrosstab = isShowCrosstab.compareToIgnoreCase("Y")==0;
        Boolean isOrganization = isShowOrganization.compareToIgnoreCase("Y")==0;
        Boolean isShowSummary = isShowSummaryElements.compareToIgnoreCase("Y")==0;
        
        // La columna donde comienza el Crosstab (después de Saldo Final Consolidado)
        final int CROSSTAB_START_COLUMN = headers.length; 
        // Crear el mapa OrgID -> Índice de Columna
        Map<Integer, Integer> orgColumnMap = new HashMap<>();
        int currentColIndex = CROSSTAB_START_COLUMN;
        for (Integer orgID : selectedOrgIDs) {
            orgColumnMap.put(orgID, currentColIndex++);
        }
        int total = reportData.size();
        int batchSize = 100;
        int rowNum = headerRows + 1;

        // Reusar estilos desde styleMap
        CellStyle textNormal = styleMap.get("TEXT_N");
        CellStyle textBold   = styleMap.get("TEXT_B");
        CellStyle numNormal  = styleMap.get("NUM_N");
        CellStyle numBold    = styleMap.get("NUM_B");
        
        ValueNamePair[] accountTypesArray =  FinancialReportConstants.getAccountTypeReferenceList(ctx);
        
        //  Escribir filas del reporte
        for (int i = 0; i < total; i++) {
            TrialBalanceLine tbl = reportData.get(i);
            //
            int level = tbl.getLevel();
            String tipoRegistro = tbl.getTipoRegistro(); 
       
            // Determinar estilo
            boolean bold = "10".equals(tipoRegistro) || "50".equals(tipoRegistro);
            CellStyle tStyle = bold ? textBold : textNormal;
            CellStyle nStyle = bold ? numBold : numNormal;
            // Si la línea NO es un detalle de Org (ej., es R o 60), escríbela como la línea principal
            if ("10".equals(tipoRegistro) || "50".equals(tipoRegistro)) {       
            	// Muestra si isSummary = 'Y' o si tbl.getIsSummary() ='N'
            	if (isShowSummary || (!isShowSummary && tbl.getIsSummary().compareToIgnoreCase("N")==0)) {
	                // Nueva Fila
	                Row row = sheet.createRow(rowNum++);
	                // Formatear la cuenta con sangría (Indentación)
	                String paddedName = ExcelUtils.padLeft(tbl.getNombre(), level);
	                String orgValue = tbl.getOrgValue() != null ? tbl.getOrgValue() : "";
	
	                // --- Columna 0: Cód. Cuenta (con sangría)
	                ExcelUtils.createStyledCell(row, 0, tbl.getCodigo(), tStyle);
	                ExcelUtils.updateMaxLen(maxLen, 0, tbl.getCodigo());
	
	                // --- Columna 1: Nombre Cuenta
	                ExcelUtils.createStyledCell(row, 1, paddedName, tStyle);
	                ExcelUtils.updateMaxLen(maxLen, 1, paddedName);
	
	                // --- Columna 2: Organización (solo para tipo 50, nulo para R/60)
	                ExcelUtils.createStyledCell(row, 2, orgValue, tStyle);
	                ExcelUtils.updateMaxLen(maxLen, 2, orgValue);
	                // --- Columnas 3-7: Saldos (BigDecimals)
	                int col = 3;
	                ExcelUtils.createStyledCell(row, col++, tbl.getOpenBalance(), nStyle);
	                ExcelUtils.createStyledCell(row, col++, tbl.getAmtAcctDr(), nStyle);
	                ExcelUtils.createStyledCell(row, col++, tbl.getAmtAcctCr(), nStyle);
	                ExcelUtils.createStyledCell(row, col++, tbl.getBalancePeriodo(), nStyle);
	                ExcelUtils.createStyledCell(row, col++, tbl.getCloseBalance(), nStyle);
            	}              
            // ⚠️ Nota: Para las líneas consolidadas, las columnas Crosstab (a partir de la 8) 
            // deben quedar vacías o puedes añadir una lógica de totales.
                
            } else if ("60".equals(tipoRegistro) ) { 
                // =============================
                // CASO 1: CROSSTAB
                // =============================
                if (isCrosstab) {
                    int currentOrgID = tbl.getAD_Org_ID();
                    BigDecimal orgBalance = tbl.getCloseBalance();
                    BigDecimal orgPeriodBalance = tbl.getBalancePeriodo();
                    // 1. Encontrar la Fila (Row) correcta para esta cuenta
                    Row targetRow = sheet.getRow(rowNum - 1);
                    if (targetRow != null && orgColumnMap.containsKey(currentOrgID)) {
                    	// 2. Obtener la columna de destino
                        int targetCol = orgColumnMap.get(currentOrgID);
                        // 3. Escribir el saldo en la columna de Crosstab
                        if (isShowMovementsAmounts.compareToIgnoreCase("Y")==0)
                        	ExcelUtils.createStyledOrgCell(targetRow, targetCol, orgPeriodBalance, nStyle);
                        else
                        	ExcelUtils.createStyledOrgCell(targetRow, targetCol, orgBalance, nStyle);
                    }
                }
                // =============================
                // CASO 2: DETALLE ORGANIZACIÓN
                // =============================
                else if (isOrganization) {
                    Row row = sheet.createRow(rowNum++);
                    int detailLevel = level + 1;
                    String paddedName =
                        ExcelUtils.padLeft(tbl.getNombre(), detailLevel);
                    String orgValue =
                        tbl.getOrgValue() != null ? tbl.getOrgValue() : "";
                    String codigo60 =
                        tbl.getCodigo() != null ? tbl.getCodigo() : "";
                    ExcelUtils.createStyledCell(row, 0, codigo60, tStyle);
                    ExcelUtils.updateMaxLen(maxLen, 0, codigo60);
                    ExcelUtils.createStyledCell(row, 1, paddedName, tStyle);
                    ExcelUtils.updateMaxLen(maxLen, 1, paddedName);
                    ExcelUtils.createStyledCell(row, 2, orgValue, tStyle);
                    ExcelUtils.updateMaxLen(maxLen, 2, orgValue);
                    int col = 3;
                    ExcelUtils.createStyledCell(row, col++, tbl.getOpenBalance(), nStyle);
                    ExcelUtils.createStyledCell(row, col++, tbl.getAmtAcctDr(), nStyle);
                    ExcelUtils.createStyledCell(row, col++, tbl.getAmtAcctCr(), nStyle);
                    ExcelUtils.createStyledCell(row, col++, tbl.getBalancePeriodo(), nStyle);
                    ExcelUtils.createStyledCell(row, col++, tbl.getCloseBalance(), nStyle);
                }
            	
            } else {
            	 continue; 
            }

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
        // Ajuste de ancho de columna Organizaciones
        if (isCrosstab) {
        	// Ancho Columnas Organizaciones
            for (int col = 0; col < orgColumnMap.size(); col++) { 
            	// Obtener el ancho deseado: Máximo de 100 caracteres, mínimo de 10, y añadir 2 de padding base.
                int chars = Math.min(100, Math.max(10, orgColNameLen + 2)); 
                // Unidades base POI: Caracteres * 256
                int desiredWidthUnits = chars * 256; 
                // Aplicar HOLGURA EXTRA (PADDING)
                int extraPadding = 1024; // 1024 unidades POI son aproximadamente 4 caracteres extra.
                if (desiredWidthUnits > 0) {
                    this.sheet.setColumnWidth(col+maxLen.length, desiredWidthUnits + extraPadding); 
                }
            }
        }
    }
    
    
    @Override
    protected String[] getColumnHeaders(Map<String, Object> parameters) {
        return this.headers;
    }

    @Override
    protected int[] getColumnWidths(Map<String, Object> parameters) {
        // Nota: El viewer utiliza maxLen para widths
        return this.maxLen; 
    }

    @Override
    protected int getHeaderRowCount(Map<String, Object> parameters) {
        return headerRows;
    }
}

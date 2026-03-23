package org.amerp.reports.xlsx.generator;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.OrgTree;
import org.amerp.reports.TrialBalanceLine12;
import org.amerp.reports.xlsx.constants.FinancialReportConstants;
import org.amerp.reports.xlsx.util.AccountUtils;
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
import org.compiere.model.MOrg;
import org.compiere.model.X_C_ElementValue;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class RPTStateFinancialIntegralResults12 extends AbstractXlsxGenerator {

	private static final CLogger log = CLogger.getCLogger(RPTStateFinancialIntegralResults12.class);

	private static final int headerRows = 4;
	// Cabeceras, 
	private static final String[] headers;
	private static int[] maxLen;

	static {
	    List<String> headerList = new ArrayList<>(Arrays.asList(
	    		 Msg.translate(Env.getCtx(), "value"),
	    		 Msg.translate(Env.getCtx(), "name"),
	    		 Msg.translate(Env.getCtx(), "AD_Org_ID")));
	    for (int i = 1; i <= 12; i++) {
	        headerList.add(Msg.translate(Env.getCtx(), "C_Period_ID") + " " +String.format("%02d", i));
	    }
	    headerList.add(Msg.translate(Env.getCtx(), "Total")+" "+Msg.translate(Env.getCtx(), "Year"));
	    headers = headerList.toArray(new String[0]);  
	    // Inicialización manual del array de anchos (16 posiciones)
	    maxLen = new int[] { 
	        15, 40, 12, // Fijos
	        16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 18 // P01-P12 + Anual
	    };
	}
    
    // Organizaciones seleccionadas
    List<OrgTree> orgs  = null;
    List<Integer> selectedOrgIDs = null;
    Map<Integer, String> orgValues = null;
    int batchSize = 100;
    final int CROSSTAB_START_COLUMN = headers.length; // La columna donde comienza el Crosstab (después de Saldo Final Consolidado) 

    @Override
    public String getReportName() {
    	return "StateFinancialIntegralResults_12Periods";
    }

	// ===================================================================
    // 📢 TÍTULOS (Usando parámetros)
    // ===================================================================

    @Override
    protected String getReportTitle(Map<String, Object> parameters) {
    	// Lee el valor traducido de los parámetros
        String title = (String) parameters.get("ReportTitle");
        return title != null ? title : "State Financial Integral Results for 12 Periods"; 
    }

    @Override
    protected String getReportSubTitle(Map<String, Object> parameters) {
    	String subTitle = "";
        String isShowSummaryElements = (String) parameters.get("isShowSummaryElements");
        String isPositiveBalance = (String) parameters.get("isPositiveBalance");
        subTitle = MsgUtils.getTranslatedPrintName("PositiveBalance",Env.getAD_Language(Env.getCtx()))+
        		"("+MsgUtils.getTranslatedYesNo(isPositiveBalance)+")"+
        		" - "+MsgUtils.getTranslatedPrintName("isShowSummaryElements",Env.getAD_Language(Env.getCtx()))+
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
            cliDescription = cliDescription+" "+orgs.get(0).getAllOrgs();
        } else {
        	cliDescription = cliDescription+" "+ Msg.translate(Env.getCtx(), "NoOrgSelected");
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
        // 🏆 (Columna 0 hasta Columna 3 en la Fila 1)
        // CellRangeAddress(firstRow, lastRow, firstCol, lastCol)
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 5));
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
        // 🏆 (Columna 0 hasta Columna 3 en la Fila 5)
        // CellRangeAddress(firstRow, lastRow, firstCol, lastCol)
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));
        
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
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        
        // Escribir cabeceras traducidas
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
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
        Integer C_Year_ID = (Integer) parameters.get("C_Year_ID");
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
        
        // Boolean Vars 
        Boolean isCrosstab = isShowCrosstab.compareToIgnoreCase("Y")==0;
        Boolean isOrganization = isShowOrganization.compareToIgnoreCase("Y")==0;
        Boolean isShowMovements = isShowMovementsAmounts.compareToIgnoreCase("Y")==0;
        Boolean isShowSummary = isShowSummaryElements.compareToIgnoreCase("Y")==0;
        Boolean isPositive = isPositiveBalance.compareToIgnoreCase("Y")==0;
	    
        // Obtener Datos  (C_ElementValue_ID = null para TrialBalance) 
        List<TrialBalanceLine12> reportData = DataPopulator.getTrialBalanceData12Periods(
                AD_Client_ID, C_AcctSchema_ID, AD_Org_ID, AD_OrgParent_ID, 
                C_Year_ID, C_Period_ID, PostingType, null, 
                DateFrom, DateTo, isShowZERO, trxName);
        
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el Balance de Comprobación.");
            return;
        }
        
     // Listas de clasificación
        List<TrialBalanceLine12> revenuesTbl = new ArrayList<>();
        List<TrialBalanceLine12> expensesTbl = new ArrayList<>();
        List<TrialBalanceLine12> memosTbl = new ArrayList<>();

        // Estructuras para totales de 12 meses + Anual (Índice 0-11: meses, Índice 12: Anual)
        BigDecimal[] totalsSaRevenue = new BigDecimal[13];
        BigDecimal[] totalsBalRevenue = new BigDecimal[13];
        BigDecimal[] totalsSaExpense = new BigDecimal[13];
        BigDecimal[] totalsBalExpense = new BigDecimal[13];
        BigDecimal[] totalsSaMemo = new BigDecimal[13];
        BigDecimal[] totalsBalMemo = new BigDecimal[13];

        // Inicializar arreglos con ZERO
        for (int i = 0; i <= 12; i++) {
            totalsSaRevenue[i] = totalsBalRevenue[i] = BigDecimal.ZERO;
            totalsSaExpense[i] = totalsBalExpense[i] = BigDecimal.ZERO;
            totalsSaMemo[i] = totalsBalMemo[i] = BigDecimal.ZERO;
        }
        
        String accountType = "";
        String tipoRegistro = "";
        
        for (TrialBalanceLine12 e : reportData) {
            accountType = e.getAccountType();
            tipoRegistro = e.getTipoRegistro();
            
            // Clasificar en listas para la impresión
            if (X_C_ElementValue.ACCOUNTTYPE_Revenue.equals(accountType)) revenuesTbl.add(e);
            else if (X_C_ElementValue.ACCOUNTTYPE_Expense.equals(accountType)) expensesTbl.add(e);
            else if (X_C_ElementValue.ACCOUNTTYPE_Memo.equals(accountType)) memosTbl.add(e);

            // Solo acumulamos para totales si el registro es consolidado (tipo "10" o "50" según tu SQL)
            if ("10".equals(tipoRegistro) || "50".equals(tipoRegistro)) {
                BigDecimal[] targetSa = null;
                BigDecimal[] targetBal = null;

                if (X_C_ElementValue.ACCOUNTTYPE_Revenue.equals(accountType)) {
                    targetSa = totalsSaRevenue; targetBal = totalsBalRevenue;
                } else if (X_C_ElementValue.ACCOUNTTYPE_Expense.equals(accountType)) {
                    targetSa = totalsSaExpense; targetBal = totalsBalExpense;
                } else if (X_C_ElementValue.ACCOUNTTYPE_Memo.equals(accountType)) {
                    targetSa = totalsSaMemo; targetBal = totalsBalMemo;
                }

                if (targetSa != null) {
                    // Acumular los 12 periodos
                    targetSa[0] = targetSa[0].add(e.getSa_p01()); targetBal[0] = targetBal[0].add(e.getBal_p01());
                    targetSa[1] = targetSa[1].add(e.getSa_p02()); targetBal[1] = targetBal[1].add(e.getBal_p02());
                    targetSa[2] = targetSa[2].add(e.getSa_p03()); targetBal[2] = targetBal[2].add(e.getBal_p03());
                    targetSa[3] = targetSa[3].add(e.getSa_p04()); targetBal[3] = targetBal[3].add(e.getBal_p04());
                    targetSa[4] = targetSa[4].add(e.getSa_p05()); targetBal[4] = targetBal[4].add(e.getBal_p05());
                    targetSa[5] = targetSa[5].add(e.getSa_p06()); targetBal[5] = targetBal[5].add(e.getBal_p06());
                    targetSa[6] = targetSa[6].add(e.getSa_p07()); targetBal[6] = targetBal[6].add(e.getBal_p07());
                    targetSa[7] = targetSa[7].add(e.getSa_p08()); targetBal[7] = targetBal[7].add(e.getBal_p08());
                    targetSa[8] = targetSa[8].add(e.getSa_p09()); targetBal[8] = targetBal[8].add(e.getBal_p09());
                    targetSa[9] = targetSa[9].add(e.getSa_p10()); targetBal[9] = targetBal[9].add(e.getBal_p10());
                    targetSa[10] = targetSa[10].add(e.getSa_p11()); targetBal[10] = targetBal[10].add(e.getBal_p11());
                    targetSa[11] = targetSa[11].add(e.getSa_p12()); targetBal[11] = targetBal[11].add(e.getBal_p12());
                    targetSa[12] = targetSa[12].add(e.getSa_anual()); targetBal[12] = targetBal[12].add(e.getBal_anual());
                }
            }
        }
        // Crear el mapa OrgID -> Índice de Columna
        Map<Integer, Integer> orgColumnMap = new HashMap<>();
        int currentColIndex = CROSSTAB_START_COLUMN;
        for (Integer orgID : selectedOrgIDs) {
            orgColumnMap.put(orgID, currentColIndex++);
        }
        
        // Reusar estilos desde styleMap
        CellStyle textBold   = styleMap.get("TEXT_B");
        CellStyle numBold    = styleMap.get("NUM_B");

        int rowNumGen = headerRows + 1;
     // Definir qué serie de datos usar (esto vendría de tus parámetros)
        String isSAorBAL = "SA"; // O "BAL"

        // === REVENUES ===
        accountType = X_C_ElementValue.ACCOUNTTYPE_Revenue;
        if (!revenuesTbl.isEmpty()) {
            rowNumGen = generateReportContent12Periods(rowNumGen, isOrganization, isShowSummary, accountType, isPositive, isSAorBAL, revenuesTbl);
            
            Row row = sheet.createRow(rowNumGen++);
            ExcelUtils.createStyledCell(row, 1, "== Total (" + FinancialReportConstants.getAccountTypeName(ctx, accountType) + ") ==", textBold);
            
            int col = 3;
            BigDecimal[] targetTotals = "SA".equals(isSAorBAL) ? totalsSaRevenue : totalsBalRevenue;
            
            for (int i = 0; i <= 12; i++) {
                ExcelUtils.createStyledCell(row, col++, 
                    AccountUtils.applyPositiveBalance(accountType, isPositive, targetTotals[i]), numBold);
            }
            rowNumGen++;
        }

        // === EXPENSES ===
        accountType = X_C_ElementValue.ACCOUNTTYPE_Expense;
        if (!expensesTbl.isEmpty()) {
            rowNumGen = generateReportContent12Periods(rowNumGen, isOrganization, isShowSummary, accountType, isPositive, isSAorBAL, expensesTbl);
            
            Row row = sheet.createRow(rowNumGen++);
            ExcelUtils.createStyledCell(row, 1, "== Total (" + FinancialReportConstants.getAccountTypeName(ctx, accountType) + ") ==", textBold);
            
            int col = 3;
            BigDecimal[] targetTotals = "SA".equals(isSAorBAL) ? totalsSaExpense : totalsBalExpense;
            
            for (int i = 0; i <= 12; i++) {
                ExcelUtils.createStyledCell(row, col++, 
                    AccountUtils.applyPositiveBalance(accountType, isPositive, targetTotals[i]), numBold);
            }
            rowNumGen++;
        }
        
        // === MEMO ===
        accountType = X_C_ElementValue.ACCOUNTTYPE_Memo;
        if (!memosTbl.isEmpty()) {
            // 1. Líneas de detalle (Carga SA o BAL según la variable)
            rowNumGen = generateReportContent12Periods(rowNumGen, isOrganization, isShowSummary, accountType, isPositive, isSAorBAL, memosTbl);
            
            // 2. Fila de Total de Grupo (Memo)
            Row row = sheet.createRow(rowNumGen++);
            ExcelUtils.createStyledCell(row, 1, "== Total (" + FinancialReportConstants.getAccountTypeName(ctx, accountType) + ") ==", textBold);
            
            int col = 3;
            // Seleccionar el array de acumulados correspondiente
            BigDecimal[] targetTotals = "SA".equals(isSAorBAL) ? totalsSaMemo : totalsBalMemo;
            
            for (int i = 0; i <= 12; i++) {
                ExcelUtils.createStyledCell(row, col++, 
                    AccountUtils.applyPositiveBalance(accountType, isPositive, targetTotals[i]), numBold);
            }
            rowNumGen++; // Espacio antes del gran total
        }

        // === TOTAL REPORTE (Suma de grupos) ===
        // Nota: Generalmente el Total Reporte es Revenue + Expense (considerando sus signos)
        Row rowTotalFinal = sheet.createRow(rowNumGen++);
        ExcelUtils.createStyledCell(rowTotalFinal, 1, "TOTAL REPORTE", textBold);

        int colFinal = 3;
        for (int i = 0; i <= 12; i++) {
            BigDecimal totalPeriodo;
            if ("SA".equals(isSAorBAL)) {
                // Suma de movimientos netos de los 3 grupos
                totalPeriodo = totalsSaRevenue[i].add(totalsSaExpense[i]).add(totalsSaMemo[i]);
            } else {
                // Suma de saldos finales de los 3 grupos
                totalPeriodo = totalsBalRevenue[i].add(totalsBalExpense[i]).add(totalsBalMemo[i]);
            }
            
            // El Total Reporte suele mostrarse con signo natural o según Revenue
            ExcelUtils.createStyledCell(rowTotalFinal, colFinal++, totalPeriodo, numBold);
        }

        // Iteramos los 12 meses + el total anual (índice 12)
        for (int i = 0; i <= 12; i++) {
            BigDecimal totalPeriodo = BigDecimal.ZERO;
            
            if ("SA".equals(isSAorBAL)) {
                // Sumamos los movimientos netos aplicándole el signo de "Positive Balance" individualmente a cada grupo
                totalPeriodo = AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Revenue, isPositive, totalsSaRevenue[i])
                        .add(AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Expense, isPositive, totalsSaExpense[i]));
                
                // Si quieres incluir Memos en el total general, descomenta la siguiente línea:
                // totalPeriodo = totalPeriodo.add(AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Memo, isPositive, totalsSaMemo[i]));
                
            } else {
                // Sumamos los saldos finales acumulados
                totalPeriodo = AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Revenue, isPositive, totalsBalRevenue[i])
                        .add(AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Expense, isPositive, totalsBalExpense[i]));
                
                // totalPeriodo = totalPeriodo.add(AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Memo, isPositive, totalsBalMemo[i]));
            }

            // Escribimos la celda para el periodo 'i'
            ExcelUtils.createStyledCell(rowTotalFinal, colFinal++, totalPeriodo, numBold);
        }
        
        
        if (isOrganization) {
            
            rowNumGen++; // Añadir un espacio visual
         // Mapas para acumular totales por Organización (12 meses + Anual)
            Map<Integer, BigDecimal[]> orgRevenueMap = new HashMap<>();
            Map<Integer, BigDecimal[]> orgExpenseMap = new HashMap<>();
            Map<Integer, BigDecimal[]> orgMemoMap    = new HashMap<>();
            
            // Iteramos sobre las organizaciones seleccionadas (u obtenidas del mapa de columnas)
            for (Integer orgID : orgColumnMap.keySet()) {
                
                // 1. Obtener los arreglos de 13 periodos para esta Org específica
                // Estos mapas deben haberse llenado en el bucle de clasificación inicial
                BigDecimal[] revOrgTotals = orgRevenueMap.getOrDefault(orgID, new BigDecimal[13]);
                BigDecimal[] expOrgTotals = orgExpenseMap.getOrDefault(orgID, new BigDecimal[13]);
                
                String orgName = MOrg.get(orgID).getName().trim();
                
                // 2. Crear la fila de Total por Organización
                Row rowOrg = sheet.createRow(rowNumGen++);
                ExcelUtils.createStyledCell(rowOrg, 1, "Total Reporte (" + orgName + ")", textBold);
                
                // 3. Escribir las 13 columnas (SA o BAL según la variable dinámica)
                int col = 3;
                for (int i = 0; i <= 12; i++) {
                    BigDecimal valRev = revOrgTotals[i] != null ? revOrgTotals[i] : BigDecimal.ZERO;
                    BigDecimal valExp = expOrgTotals[i] != null ? expOrgTotals[i] : BigDecimal.ZERO;
                    
                    // Aplicar lógica de signos y sumar (Revenue + Expense)
                    BigDecimal totalOrgPeriodo = AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Revenue, isPositive, valRev)
                            .add(AccountUtils.applyPositiveBalance(X_C_ElementValue.ACCOUNTTYPE_Expense, isPositive, valExp));
                    
                    ExcelUtils.createStyledCell(rowOrg, col++, totalOrgPeriodo, numBold);
                }
            }
        }
        
	    // === AJUSTE DE ANCHO DE COLUMNAS (Final) ===
	
	    // Definimos el total de columnas: 3 (Código, Nombre, Org) + 13 (Periodos sa o bal)
	    int totalColumnsUsed = 3 + 13; 
	
	    for (int col = 0; col < totalColumnsUsed; col++) {
	         // 1. Obtener el ancho máximo registrado para esta columna
	         // Asegúrate de que maxLen sea un array de al menos tamaño 16
	         int maxChars = (col < maxLen.length) ? maxLen[col] : 15; 
	
	         // 2. Calcular el ancho deseado: Entre 10 y 100 caracteres, + 2 de padding base
	         int chars = Math.min(100, Math.max(10, maxChars + 2)); 
	         
	         // 3. Convertir a unidades de POI (Caracteres * 256)
	         int desiredWidthUnits = chars * 256; 
	         
	         // 4. Aplicar holgura extra (aprox 4 caracteres más)
	         int extraPadding = 1024; 
	
	         if (desiredWidthUnits > 0) {
	             this.sheet.setColumnWidth(col, desiredWidthUnits + extraPadding); 
	         }
	    }

    }
    

    /**
     * generateReportContent12Periods
     * @param rowNum
     * @param isOrganization
     * @param isShowSummary
     * @param accountType
     * @param isPositive
     * @param isSAorBAL
     * @param accountTypeGroup
     * @return
     */
    protected int generateReportContent12Periods(int rowNum, boolean isOrganization, 
            boolean isShowSummary, String accountType, boolean isPositive, 
            String isSAorBAL, List<TrialBalanceLine12> accountTypeGroup) {

        CellStyle textNormal = styleMap.get("TEXT_N");
        CellStyle textBold   = styleMap.get("TEXT_B");
        CellStyle numNormal  = styleMap.get("NUM_N");
        CellStyle numBold    = styleMap.get("NUM_B");
        
        for (TrialBalanceLine12 tbl : accountTypeGroup) {
            String tipoRegistro = tbl.getTipoRegistro();
            boolean isSummaryLine = "Y".equalsIgnoreCase(tbl.getIsSummary());

            // Filtros de visibilidad
            if (!isShowSummary && isSummaryLine && !"60".equals(tipoRegistro)) continue;
            if (!isOrganization && "60".equals(tipoRegistro)) continue;

            Row row = sheet.createRow(rowNum++);
            boolean bold = "10".equals(tipoRegistro) || "50".equals(tipoRegistro);
            CellStyle tStyle = bold ? textBold : textNormal;
            CellStyle nStyle = bold ? numBold : numNormal;

            // Columnas fijas
            ExcelUtils.createStyledCell(row, 0, tbl.getCodigo(), tStyle);
            ExcelUtils.createStyledCell(row, 1, ExcelUtils.padLeft(tbl.getNombre(), tbl.getLevel()), tStyle);
            ExcelUtils.createStyledCell(row, 2, tbl.getOrgValue() != null ? tbl.getOrgValue() : "", tStyle);

            // --- Lógica Dinámica de Columnas ---
            int col = 3;
            BigDecimal[] valores;

            if ("SA".equals(isSAorBAL)) {
                valores = new BigDecimal[]{
                    tbl.getSa_p01(), tbl.getSa_p02(), tbl.getSa_p03(), tbl.getSa_p04(),
                    tbl.getSa_p05(), tbl.getSa_p06(), tbl.getSa_p07(), tbl.getSa_p08(),
                    tbl.getSa_p09(), tbl.getSa_p10(), tbl.getSa_p11(), tbl.getSa_p12(),
                    tbl.getSa_anual()
                };
            } else {
                valores = new BigDecimal[]{
                    tbl.getBal_p01(), tbl.getBal_p02(), tbl.getBal_p03(), tbl.getBal_p04(),
                    tbl.getBal_p05(), tbl.getBal_p06(), tbl.getBal_p07(), tbl.getBal_p08(),
                    tbl.getBal_p09(), tbl.getBal_p10(), tbl.getBal_p11(), tbl.getBal_p12(),
                    tbl.getBal_anual()
                };
            }

            for (BigDecimal val : valores) {
                ExcelUtils.createStyledCell(row, col++, 
                    AccountUtils.applyPositiveBalance(accountType, isPositive, val), nStyle);
            }
        }
        return rowNum;
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

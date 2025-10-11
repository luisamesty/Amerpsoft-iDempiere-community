package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.util.IProcessUI;
import org.amerp.reports.AccountElementBasic;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.OrgTree;
import org.amerp.reports.xlsx.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.process.ClientProcess;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Window;

import io.keikai.ui.Spreadsheet;

public class TrialBalanceOnePeriod_TreeOrg_xlsx  extends SvrProcess  implements ProcessCall, ClientProcess{

	private static final CLogger log = CLogger.getCLogger(TrialBalanceOnePeriod_TreeOrg_xlsx.class);
	
    private int p_AD_Client_ID = 0;
    private int p_AD_OrgParent_ID = 0;
    private int p_AD_Org_ID = 0;
    private int p_C_AcctSchema_ID = 0;
    private String p_PostingType ="";
    private int p_C_Period_ID = 0;
    private boolean p_isShowOrganization = false;
    private boolean p_isShowZERO = false;
    private String p_Action = null; // Preview o Download
    private IProcessUI processUI;
    
	@Override
	protected void prepare() {
	 	processUI = Env.getProcessUI(Env.getCtx());
        // Parameters
        ProcessInfoParameter[] para = getParameter();
        for (ProcessInfoParameter pp : para) {
            String name = pp.getParameterName();
            if ("AD_Client_ID".equals(name)) {
                p_AD_Client_ID = pp.getParameterAsInt();
            } else if ("AD_OrgParent_ID".equals(name)) {
            	p_AD_OrgParent_ID = pp.getParameterAsInt();
            } else if ("AD_Org_ID".equals(name)) {
            	p_AD_Org_ID = pp.getParameterAsInt();
            } else if ("C_AcctSchema_ID".equals(name)) {
                p_C_AcctSchema_ID = pp.getParameterAsInt();
            } else if ("PostingType".equals(name)) {
            	p_PostingType = pp.getParameterAsString();
            } else if ("C_Period_ID".equals(name)) {
            	p_C_Period_ID = pp.getParameterAsInt();
            } else if (name.equals("isShowOrganization")) {
            	p_isShowOrganization = "Y".equals(pp.getParameter());
            } else if (name.equals("isShowZERO")) {
            	p_isShowZERO = "Y".equals(pp.getParameter());
            } else if ("Action".equals(name)) {
                p_Action = pp.getParameterAsString();
            } 
        }

        // Valores por defecto si no se pasaron parámetros
        if (p_AD_Client_ID == 0) {
            p_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
        }
        if (p_AD_Org_ID == 0) {
            p_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
        }
        if (p_Action == null || p_Action.isEmpty()) {
            p_Action = "Download"; // Por defecto Download
        }
		
	}

	@Override
	protected String doIt() throws Exception {
		
        // 1️⃣ Crear archivo XLSX
        File xlsxFile = crearXlsx(getProcessInfo());
        //File xlsxFile = crearExcelTemporal();
        // 2️⃣ Ejecutar acción según parámetro
        if ("Download".equalsIgnoreCase(p_Action)) {
            downloadReport(xlsxFile);
        } else if ("Preview".equalsIgnoreCase(p_Action)){ // Preview por defecto
        	previewReportWeb(xlsxFile);
        }
        
        return "@OK@";
	}

	
    private File crearXlsx(ProcessInfo pInfo) throws IOException {
    	
    	List<OrgTree> orgs = DataPopulator.getOrgTreeList(p_AD_Client_ID, 0, 0);

    	for (OrgTree o : orgs) {
    	    System.out.println(o.getOrgValue() + " - " + o.getOrgName());
    	}
    	
        // --- 1️⃣ Leer constantes globales antes del bucle
        String cliName = "";
        String cliDescription = "";
        byte[] cliLogo = null;
        MClient mclient = new MClient(getCtx(),p_AD_Client_ID, get_TrxName());
        if (mclient != null ) {
        	cliName = mclient.getName();
        	cliDescription = mclient.getDescription() != null ? mclient.getDescription() : mclient.getName();
        	 // --- 2️⃣ Obtener información del cliente (AD_ClientInfo)
            MClientInfo ci = MClientInfo.get(getCtx(), p_AD_Client_ID);
            if (ci != null && ci.getLogoReport_ID() > 0) {
                // --- 3️⃣ Obtener el logo (AD_Image)
                MImage img = new MImage(Env.getCtx(), ci.getLogoReport_ID(), null);
                if (img != null && img.getBinaryData() != null) {
                    cliLogo = img.getBinaryData();
                }
            }	
        }
        
        List<AccountElementBasic> reportData = DataPopulator.getAccountElementBasicList(
                p_AD_Client_ID, p_C_AcctSchema_ID);

        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el informe.");
            return null;
        }

        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, pInfo.getTitle() + "_" + System.currentTimeMillis() + ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) {
            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("Account Elements");

            // --- Header con logo y nombre
         // --- Insertar logo en la parte superior izquierda
            if (cliLogo != null && cliLogo.length > 0) {
                try {
                    int pictureIdx = workbook.addPicture(cliLogo, Workbook.PICTURE_TYPE_PNG);
                    CreationHelper helper = workbook.getCreationHelper();
                    Drawing<?> drawing = sheet.createDrawingPatriarch();

                    // --- Reservar espacio para el logo
                    sheet.setColumnWidth(0, 20 * 256);  // Aumenta ancho columna A
                    for (int i = 0; i < 4; i++) {       // 4 filas de alto
                        Row row = sheet.getRow(i);
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
            
            // --- Nombre de la empresa
            Row nameRow = sheet.createRow(0); // fila 0
            Cell cellName = nameRow.createCell(1); // columna 1 (junto al logo)
            cellName.setCellValue(cliName);
            CellStyle nameStyle = workbook.createCellStyle();
            Font nameFont = workbook.createFont();
            nameFont.setFontHeightInPoints((short) 14);
            nameFont.setBold(true);
            nameStyle.setFont(nameFont);
            nameStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellName.setCellStyle(nameStyle);

            // --- Descripción de la empresa
            Row descRow = sheet.createRow(1); // fila 1
            Cell cellDesc = descRow.createCell(1);
            cellDesc.setCellValue(cliDescription);
            CellStyle descStyle = workbook.createCellStyle();
            Font descFont = workbook.createFont();
            descFont.setFontHeightInPoints((short) 12);
            descStyle.setFont(descFont);
            cellDesc.setCellStyle(descStyle);

            // --- Opcional: hacer merge de celdas para nombre y descripción
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 5)); // fila 0, columnas 1 a 5
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 5)); // fila 1, columnas 1 a 5
            
            // --- Crear estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            int headerStartRow = 4;
            
            // --- Encabezados (sin "Level")
            String[] headers = { 
            	    "value", 
            	    "description", 
            	    "AccountType", 
            	    "AccountSign", 
            	    "IsDocControlled", 
            	    "IsSummary", 
            	    "Parent_ID" 
            	};
        	int[] maxLen = { 30, 50, 20, 20, 20, 20, 25 }; // ancho aproximado proporcional
        	for (int col = 0; col < maxLen.length; col++) {
        	    sheet.setColumnWidth(col, maxLen[col] * 256);
        	}

        	Row headerRow = sheet.createRow(headerStartRow);
        	for (int i = 0; i < headers.length; i++) {
        	    String translated = Msg.getElement(getCtx(), headers[i]); 	//Traducciones
        	    Cell cell = headerRow.createCell(i);
        	    cell.setCellValue(translated);
        	    cell.setCellStyle(headerStyle);
        	}

            // --- Crear estilos de fuentes
            Map<String, CellStyle> styleMap = new HashMap<>();

            styleMap.put("L1", ExcelUtils.createStyle(workbook, 16, false));
            styleMap.put("L2", ExcelUtils.createStyle(workbook, 16, false));
            styleMap.put("L3", ExcelUtils.createStyle(workbook, 14, false));
            styleMap.put("L4", ExcelUtils.createStyle(workbook, 14, false));
            styleMap.put("L5", ExcelUtils.createStyle(workbook, 12, false));
            styleMap.put("L6", ExcelUtils.createStyle(workbook, 12, false));
            styleMap.put("L7", ExcelUtils.createStyle(workbook, 12, false));
            styleMap.put("L8", ExcelUtils.createStyle(workbook, 10, false));
            styleMap.put("L9", ExcelUtils.createStyle(workbook, 10, false));

            // Versiones en negrita (isSummary = 'Y')
            styleMap.put("L1B", ExcelUtils.createStyle(workbook, 16, true));
            styleMap.put("L2B", ExcelUtils.createStyle(workbook, 16, true));
            styleMap.put("L3B", ExcelUtils.createStyle(workbook, 14, true));
            styleMap.put("L4B", ExcelUtils.createStyle(workbook, 14, true));
            styleMap.put("L5B", ExcelUtils.createStyle(workbook, 12, true));
            styleMap.put("L6B", ExcelUtils.createStyle(workbook, 12, true));
            styleMap.put("L7B", ExcelUtils.createStyle(workbook, 12, true));
            styleMap.put("L8B", ExcelUtils.createStyle(workbook, 10, true));
            styleMap.put("L9B", ExcelUtils.createStyle(workbook, 10, true));

            int total = reportData.size();
            int batchSize = 100;
            int rowNum = headerStartRow + 1;

            for (int i = 0; i < total; i++) {
                AccountElementBasic e = reportData.get(i);
                Row row = sheet.createRow(rowNum++);

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
                boolean bold = "Y".equalsIgnoreCase(v5);
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
                    log.warning("Procesadas " + (i + 1) + " de " + total + " filas...");
                    sheet.flushRows(batchSize);
                }
            }

            sheet.flushRows();

            for (int col = 0; col < headers.length; col++) {
                int chars = Math.min(100, Math.max(10, maxLen[col] + 2));
                sheet.setColumnWidth(col, chars * 256);
            }

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }

            Env.setContext(Env.getCtx(), "#LastReportPath", tempFile.getAbsolutePath());
            pInfo.setExportFile(tempFile);
            pInfo.setExportFileExtension("xlsx");

            log.info("Excel generado correctamente: " + tempFile.getAbsolutePath());
            return tempFile;
        }
    }
		
    private void previewReportWeb(File xlsxFile) throws FileNotFoundException {
        
    	if (processUI == null) {
            log.warning("No hay UI activa, solo se puede descargar el archivo.");
            return;
        }
    	   	
        // Solo desde ZK Desktop activo
        Window win = new Window();
        win.setTitle("Vista Previa Excel");
        win.setWidth("800px");
        win.setHeight("600px");
        win.setClosable(true);
        win.setSizable(true);
        win.setBorder("normal");
//        win.setMode("modal");

        Spreadsheet spreadsheet = new Spreadsheet();
        spreadsheet.setWidth("100%");
        spreadsheet.setHeight("100%");

        AMedia media = new AMedia(
            xlsxFile.getName(),
            "xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            new FileInputStream(xlsxFile)
        );
        MyExcelMediaView mediaView = new MyExcelMediaView();
        // Usar visor de iDempiere
        mediaView.renderMediaView(spreadsheet, media, true);

        win.appendChild(spreadsheet);
        win.doModal();
    }

    private void downloadReport(File xlsxFile) {
        if (xlsxFile != null) {
			this.processUI.download(xlsxFile);
			this.processUI.statusUpdate("Excel preparado para descarga: " + xlsxFile.getName());
			log.info("Download preparado: " + xlsxFile.getAbsolutePath());
        } else {
            log.warning("Download: No hay archivo para descargar.");
        }
    }
	
}

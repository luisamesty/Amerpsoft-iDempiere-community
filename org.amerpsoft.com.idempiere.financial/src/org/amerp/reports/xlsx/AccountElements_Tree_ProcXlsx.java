package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.adempiere.util.Callback;
import org.adempiere.util.IProcessUI;
import org.adempiere.webui.session.SessionManager;
import org.amerp.reports.AccountElementBasic;
import org.amerp.reports.DataPopulator;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MImage;
import org.compiere.model.MQuery;
import org.compiere.model.X_AD_PInstance_Para;
import org.compiere.process.ClientProcess;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Div;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.amerp.reports.xlsx.util.ExcelUtils;
import java.util.logging.Logger;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.part.WindowContainer;
import org.adempiere.webui.session.SessionManager;

public class AccountElements_Tree_ProcXlsx implements ProcessCall, ClientProcess {

    private static final Logger log = Logger.getLogger(AccountElements_Tree_ProcXlsx.class.getName());

    private Properties ctx;
    private ProcessInfo pInfo;
    private Trx trx;
    private ProcessInfo processInfo;
    private IProcessUI m_processUI;
    
    private String fullPath;
	// --- Encabezados 
	private String[] headers = { "value", "description", "AccountType", "AccountSign", "IsDocControlled", "IsSummary",
			"Parent_ID" };
	private int[] maxLen = { 30, 50, 20, 20, 20, 20, 25 }; // ancho aproximado proporcional
	// Constructor simple
	public AccountElements_Tree_ProcXlsx() {
        
    }
    
    // Constructor llamado desde WebUI
    public AccountElements_Tree_ProcXlsx(Properties ctx, ProcessInfo pInfo, Trx trx) {
        this.ctx = ctx;
        this.pInfo = pInfo;
        this.trx = trx;
    }

    
    @Override
	public boolean startProcess(Properties ctx, ProcessInfo pInfo, Trx trx) {

    	
        try {
            // Aquí generas tu Excel
            File xlsxFile = crearXlsx(ctx, pInfo, null);
            if (xlsxFile == null)
                return false;
            final String fullPath = xlsxFile.getAbsolutePath();
            pInfo.setSummary("Archivo generado: " + fullPath);
            
            // Mostrar el form si hay processUI
            if (m_processUI != null && xlsxFile != null) {
            	previewXlsx(xlsxFile);
            	
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }
    
    
	/**
	 * crearXlsx : Con diferentes estilos y tamaños.
	 * @param pInfo
	 * @return
	 * @throws IOException
	*/
    private File crearXlsx(Properties ctx, ProcessInfo pInfo, Trx trx) throws IOException {
    	
        int AD_PInstance_ID=pInfo.getAD_PInstance_ID();
        String trxName = trx != null ? trx.getTrxName() : null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();	
		addProcessParameters(AD_PInstance_ID, parameters, trxName);
		addProcessInfoParameters(parameters, pInfo.getParameter());
		
        // --- 1. Obtener todos los parámetros necesarios para la Query
        Integer AD_Client_ID = (Integer) parameters.get("AD_Client_ID");
        Integer C_AcctSchema_ID = (Integer) parameters.get("C_AcctSchema_ID");

        // --- 1️⃣ Leer constantes globales antes del bucle
        String cliName = "";
        String cliDescription = "";
        byte[] cliLogo = null;
        MClient mclient = new MClient(ctx, AD_Client_ID, trxName);
        if (mclient != null ) {
        	cliName = mclient.getName();
        	cliDescription = mclient.getDescription() != null ? mclient.getDescription() : mclient.getName();
        	 // --- 2️⃣ Obtener información del cliente (AD_ClientInfo)
            MClientInfo ci = MClientInfo.get(ctx, AD_Client_ID);
            if (ci != null && ci.getLogoReport_ID() > 0) {
                // --- 3️⃣ Obtener el logo (AD_Image)
                MImage img = new MImage(Env.getCtx(), ci.getLogoReport_ID(), null);
                if (img != null && img.getBinaryData() != null) {
                    cliLogo = img.getBinaryData();
                }
            }	
        }
        
        List<AccountElementBasic> reportData = DataPopulator.getAccountElementBasicList(
                AD_Client_ID, C_AcctSchema_ID);
        if (reportData == null || reportData.isEmpty()) {
            log.warning("No se encontraron datos para el informe.");
            return null;
        }

        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, pInfo.getTitle() + "_" + System.currentTimeMillis() + ".xlsx");
        fullPath = tempFile.getAbsolutePath();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Account Elements");

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
            

        	for (int col = 0; col < maxLen.length; col++) {
        	    sheet.setColumnWidth(col, maxLen[col] * 256);
        	}

        	Row headerRow = sheet.createRow(headerStartRow);
        	for (int i = 0; i < headers.length; i++) {
        	    String translated = Msg.getElement(ctx, headers[i]); 	//Traducciones
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
                }
            }


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
    
    
    private void previewXlsx(File xlsxFile) throws Exception {
    	
		log.warning("El archivo en Process:"+xlsxFile);

	    m_processUI.ask("¿Deseas abrir el Excel en el visor interno?", new Callback<Boolean>() {
	       
	        public void onCallback(Boolean yes) {
	            if (yes) {
	                // Esta parte se ejecuta dentro del hilo del Desktop
	            	openExcelViewerInNewTab(xlsxFile);
	            } else {
	            	m_processUI.download(xlsxFile);
	            }
	        }
	    });
    }


    private void openExcelViewerInNewTab(File xlsxFile) {
        try {
            // 🖥️ Obtener el escritorio activo
            IDesktop desktop = SessionManager.getAppDesktop();
			
            if (desktop != null) {
                ExcelViewerWindow viewer = new ExcelViewerWindow(
                		xlsxFile,
                        headers,
                        maxLen,
                        4,
                        50000
                ); // tu constructor

                viewer.setAttribute(Window.MODE_KEY, Window.MODE_EMBEDDED);
                viewer.setAttribute(Window.INSERT_POSITION_KEY, Window.INSERT_NEXT);
                viewer.setAttribute(WindowContainer.DEFER_SET_SELECTED_TAB, Boolean.TRUE);
                
                desktop.registerWindow(viewer);
                desktop.showWindow(viewer);
                //AEnv.showCenterScreen(viewer);
            }

            log.info("✅ Visor Excel abierto en nuevo tab: " + xlsxFile.getAbsolutePath());
        } catch (Exception e) {
        	log.warning( "❌ Error al abrir formulario de visor Excel\n\r"+e.getMessage());
        }
    }



    
    private void downloadReport(File xlsxFile) {
        if (xlsxFile != null) {
			log.info("Download preparado: " + xlsxFile.getAbsolutePath());
        } else {
            log.warning("Download: No hay archivo para descargar.");
        }
    }

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	@Override
	public void setProcessUI(IProcessUI processUI) {
		m_processUI = processUI;
	}
	
    /**
     * Load Process Parameters into given params map
     * @param AD_PInstance_ID
     * @param params
     * @param trxName
     */
    private void addProcessParameters(int AD_PInstance_ID, Map<String, Object> params, String trxName)
    {
        final StringBuilder sql = new StringBuilder("SELECT ")
        				.append(" ").append(X_AD_PInstance_Para.COLUMNNAME_ParameterName)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_P_String)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_P_String_To)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_P_Number)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_P_Number_To)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_P_Date)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_P_Date_To)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_Info)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_Info_To)
        				.append(",").append(X_AD_PInstance_Para.COLUMNNAME_IsNotClause)
        				.append(" FROM ").append(X_AD_PInstance_Para.Table_Name)
        				.append(" WHERE ").append(X_AD_PInstance_Para.COLUMNNAME_AD_PInstance_ID+"=?");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = DB.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, trxName);
            pstmt.setInt(1, AD_PInstance_ID);
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                String name = rs.getString(1);
                String pStr = rs.getString(2);
                String pStrTo = rs.getString(3);
                BigDecimal pNum = rs.getBigDecimal(4);
                BigDecimal pNumTo = rs.getBigDecimal(5);

                Timestamp pDate = rs.getTimestamp(6);
                Timestamp pDateTo = rs.getTimestamp(7);
                if (pStr != null) {
                    if (pStrTo!=null) {
                        params.put( name+"1", pStr);
                        params.put( name+"2", pStrTo);
                    } else {
                        params.put( name, pStr);
                    }
                } else if (pDate != null) {
                    if (pDateTo!=null) {
                        params.put( name+"1", pDate);
                        params.put( name+"2", pDateTo);
                    } else {
                        params.put( name, pDate);
                    }
                } else if (pNum != null) {
                	if (name.endsWith("_ID")) {
                		if (pNumTo!=null) {
	                        params.put( name+"1", pNum.intValue());
	                        params.put( name+"2", pNumTo.intValue());
	                    } else {
	                        params.put( name, pNum.intValue());
	                    }
                	} else {
	                    if (pNumTo!=null) {
	                        params.put( name+"1", pNum);
	                        params.put( name+"2", pNumTo);
	                    } else {
	                        params.put( name, pNum);
	                    }
                	}
                }
                //
                // Add parameter info - teo_sarca FR [ 2581145 ]
                String info = rs.getString(8);
                String infoTo = rs.getString(9);
                String isNotClause = rs.getString(10);
        		params.put(name+"_Info1", (info != null ? info : ""));
        		params.put(name+"_Info2", (infoTo != null ? infoTo : ""));
        		params.put(name+"_NOT", isNotClause);
            }
        }
        catch (SQLException e)
        {
            throw new DBException(e, sql.toString());
        }
        finally
        {
            DB.close(rs, pstmt);
            rs = null; pstmt = null;
        }
    }
    
    private void addProcessInfoParameters(Map<String, Object> params, ProcessInfoParameter[] para)
    {
    	if (para != null) {
			for (int i = 0; i < para.length; i++) {
				if (para[i].getParameter_To() == null) {
					if (para[i].getParameterName().endsWith("_ID") && para[i].getParameter() instanceof BigDecimal) {
						params.put(para[i].getParameterName(), ((BigDecimal)para[i].getParameter()).intValue());
					} else {
						params.put(para[i].getParameterName(), para[i].getParameter());
					}
				} else {
					// range - from
					if (para[i].getParameterName().endsWith("_ID") && para[i].getParameter() != null && para[i].getParameter() instanceof BigDecimal) {
		                params.put( para[i].getParameterName()+"1", ((BigDecimal)para[i].getParameter()).intValue());
					} else {
		                params.put( para[i].getParameterName()+"1", para[i].getParameter());
					}
					// range - to
					if (para[i].getParameterName().endsWith("_ID") && para[i].getParameter_To() instanceof BigDecimal) {
		                params.put( para[i].getParameterName()+"2", ((BigDecimal)para[i].getParameter_To()).intValue());
					} else {
		                params.put( para[i].getParameterName()+"2", para[i].getParameter_To());
					}
				}
			}
    	}
	}
    

}
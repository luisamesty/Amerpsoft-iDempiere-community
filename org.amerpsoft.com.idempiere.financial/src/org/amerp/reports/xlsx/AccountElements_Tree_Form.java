package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.util.Callback;
import org.adempiere.util.IProcessUI;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.Dialog;
import org.amerp.reports.AccountElementBasic;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.xlsx.util.ExcelUtils;
import org.amerp.reports.xlsx.util.MsgUtils;
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
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MColumn;
import org.compiere.model.MImage;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;


public class AccountElements_Tree_Form implements IFormController, EventListener<Event>, IProcessUI , ValueChangeListener{

	private static final CLogger log = CLogger.getCLogger(AccountElements_Tree_Form.class);

	/** UI form instance */
	private CustomForm form = new CustomForm();
	// Column Names Valid AD Names
	private String[] headers = { "value", "description", "AccountType", "AccountSign", "IsDocControlled", "IsSummary", "Parent_ID" };
	// No of Row header (start with 0,  0-4)
	private int headerRows = 4;
	private int m_WindowNo = 0;

    /** Default constructor */
    public AccountElements_Tree_Form() {
    	this.m_WindowNo = form.getWindowNo();
        try {
            initForm();
            zkInit();
            dynInit();

        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
   
    private String fullPath = "";
    
	private Borderlayout mainLayout = new Borderlayout();
	// Áreas
    private North north = new North();   // Filtros
    private Center center = new Center(); // 
    private South south = new South();   // 

    // Controles y Filtros
    private Label fClientLabel = new Label();
    private WTableDirEditor fClient;
    private Label fAcctSchemaLabel = new Label();
    private WTableDirEditor fAcctSchema;
    private Label dateFromLabel = new Label();
    private WDateEditor dateFrom =new WDateEditor();
    private Label dateToLabel = new Label();
    private WDateEditor dateTo = new WDateEditor();
    private Label isBatchLabel = new Label();
    private Checkbox isBatch = new Checkbox();  // Indica si el proceso se ejecuta en Batch
    private Label northLabel = new Label();
	private Label centerLabel = new Label();
	private Label lblStatus = new Label();
	private Textbox textStatus = new Textbox();

	// Botones
	private Button refreshButton = new Button();
	private Button cancelButton = new Button();
	private Button resetButton = new Button();
	private Button processButton = new Button();
    private Button previewButton  = new Button();
    private Button downloadButton  = new Button();


    /** Initialize layout */
    protected void initForm() {
        // Layout general
        form.appendChild(mainLayout);
        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");
        
        // Propiedades de north (solo estructura base)
        north.setVisible(true);
        north.setHeight("160px");
        north.setCollapsible(true);
        north.setOpen(true);
        north.setTitle(Msg.getMsg(Env.getCtx(), "SearchCriteria"));
        northLabel.setText("Parámetros del Informe");
        
        // mainLayout
        mainLayout.appendChild(north);
        mainLayout.appendChild(center);
        mainLayout.appendChild(south);
        
        // Center
        centerLabel.setText("Centro del formulario (vista principal)");
        center.appendChild(centerLabel);
        
        // Barra de botones (Solo la estructura Hbox)
        Hbox hboxButtons = new Hbox();
        hboxButtons.appendChild(resetButton);
        hboxButtons.appendChild(processButton);
        hboxButtons.appendChild(downloadButton);
        hboxButtons.appendChild(cancelButton);
        // Se podrían agregar refreshButton y previewButton aquí si van en la misma barra
        south.appendChild(hboxButtons);
    }

    private void zkInit() {

        // =======================================================
        // === Creación de Lookups y Editores (MOVIDO DE dynInit) ===
        // (Se usa lookup simple para que el constructor de WTableDirEditor funcione)
        // =======================================================
        
        // Cliente
        int AD_Column_ID_Client = MColumn.getColumn_ID("AD_Client", "AD_Client_ID");
        MLookup lookupClient = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID_Client, DisplayType.TableDir);
        fClient = new WTableDirEditor("AD_Client_ID", true, false, true, lookupClient);

        // Esquema Contable
        int AD_Column_ID_AcctSchema = MColumn.getColumn_ID("C_AcctSchema", "C_AcctSchema_ID");
        MLookup lookupAcctSchema = MLookupFactory.get(Env.getCtx(), m_WindowNo, 0, AD_Column_ID_AcctSchema, DisplayType.TableDir);
        fAcctSchema = new WTableDirEditor("C_AcctSchema_ID", true, false, true, lookupAcctSchema);
        
        // ============================================================
        // ======== Configuración de Labels (Original de zkInit) ========
        // ============================================================
        dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
        dateToLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
        fClientLabel.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
        fAcctSchemaLabel.setText(Msg.translate(Env.getCtx(), "C_AcctSchema_ID"));
        isBatch.setText(Msg.getMsg(Env.getCtx(), "Run as Job"));
        isBatchLabel.setText(Msg.getMsg(Env.getCtx(), "Run as Job"));
        lblStatus.setText(Msg.getMsg(Env.getCtx(), "FileXLSX"));
        textStatus.setReadonly(true);
        textStatus.setStyle("text-align: left");
        // 
        
        // ============================================================
        // ======== Área de filtros (MOVIDO DE initForm) ========
        // ============================================================
        Grid filterGrid = new Grid();
        filterGrid.setWidth("100%");
        Rows rows = new Rows();
        
        // Fila 1: Cliente
        Row row1 = new Row();
        row1.appendChild(fClientLabel);
        row1.appendChild(fClient.getComponent()); // Componente YA EXISTE
        rows.appendChild(row1);

        // Fila 2: Esquema Contable
        Row row2 = new Row();
        row2.appendChild(fAcctSchemaLabel);
        row2.appendChild(fAcctSchema.getComponent()); // Componente YA EXISTE
        rows.appendChild(row2);
        
        // Fila 3: Status Textbox
        Row row3 = new Row();
        row3.appendChild(lblStatus);
        ZKUpdateUtil.setHflex(textStatus, "true");
        row3.appendCellChild(textStatus,2);
        rows.appendChild(row3);
        
        filterGrid.appendChild(rows);
        north.appendChild(filterGrid);
    }
    
    private void dynInit() {
        
        // =======================================================
        // === Campo AD_Client_ID (Valores por Defecto y Lógica) ===
        // =======================================================
        final String COLUMN_NAME_CLIENT = "AD_Client_ID";
        
        fClient.setValue(Env.getAD_Client_ID(Env.getCtx()));
        fClient.addValueChangeListener(this);
        // Tooltip
        String tooltipText = MsgUtils.getElementFullDescription(COLUMN_NAME_CLIENT);
        if (tooltipText == null || tooltipText.isEmpty()) {
            tooltipText = "Identificador del Cliente actual de la sesión."; 
        }
        org.zkoss.zk.ui.Component clientComponent = fClient.getComponent();
        if (clientComponent instanceof org.zkoss.zul.Combobox) {
            ((org.zkoss.zul.Combobox) clientComponent).setTooltiptext(tooltipText);
        } // ... y resto de la lógica del tooltip ...

        // =======================================================
        // === Campo C_AcctSchema_ID (Valores por Defecto y Lógica) ===
        // =======================================================
        fAcctSchema.setValue(Env.getContextAsInt(Env.getCtx(), "$C_AcctSchema_ID"));
        fAcctSchema.addValueChangeListener(this);
        ((org.zkoss.zul.Combobox) fAcctSchema.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("C_AcctSchema_ID"));
        
        // ======= isBatch  =======
        isBatch.setValue(false);
        isBatch.addActionListener(this);
        
        // ============================================================
        // ======== Lógica de Botones (MOVIDO DE initForm) ========
        // ============================================================
        
        // Reset Button
        resetButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Reset")));
        resetButton.setImage(ThemeManager.getThemeResource("images/Reset16.png"));
        resetButton.addActionListener(this);
        
        // Process Button
        processButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Process")));
        processButton.addActionListener(this);
        processButton.setImage(ThemeManager.getThemeResource("images/Process16.png"));
        processButton.addEventListener("onClick", this);

        // Download Button
        downloadButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Download")));
        downloadButton.setImage(ThemeManager.getThemeResource("images/Export16.png"));
        downloadButton.addActionListener(this);
        downloadButton.addEventListener("onClick", this);
        downloadButton.setEnabled(false);
        downloadButton.setVisible(true);
        
        // Cancel Button
        cancelButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Cancel")));
        cancelButton.setImage(ThemeManager.getThemeResource("images/Cancel16.png"));
        cancelButton.addActionListener(this);
        cancelButton.setVisible(true);
        
        // Refresh Button
        refreshButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")));
        refreshButton.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
        refreshButton.addActionListener(this);
        refreshButton.setAutodisable("self");

        // Preview Button
        previewButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Preview")));
        previewButton.setImage(ThemeManager.getThemeResource("images/Multi16.png"));
        previewButton.addActionListener(this);
        previewButton.addEventListener("onClick", this);
    }


	/** Evento de botones */
    @Override
    public void onEvent(Event event) throws Exception {
        Object source = event.getTarget();

        if (source == resetButton) {
        	resetReportForm();
        } else if (source == downloadButton) {
        	downloadReport();
        } else if (source == processButton) {
        	fullPath = runServerProcessForm();
        	previewReportWeb(fullPath);
        } else if (source == cancelButton) {
        	closeReportForm(); 
        }
        // Dowload Button
        if (fullPath != null && !fullPath.trim().isEmpty()) {
        	downloadButton.setEnabled(true);
            north.setOpen(false);
        } else {
            downloadButton.setEnabled(false);
            north.setOpen(true);
        }
    }

	
	@Override
	public ADForm getForm() {
		return form;
	}
	
	@Override
	public void valueChange(ValueChangeEvent evt) {
		log.info("Value changed: " + evt.getPropertyName());
	}
	
	private void closeReportForm() {
	    Dialog.ask(m_WindowNo, "ExitApplication?", new Callback<Boolean>() {
	        @Override
	        public void onCallback(Boolean result) {
	            if (Boolean.TRUE.equals(result)) {
	                
	                // 1. Clean up internal resources (best practice)
	                form.dispose(); 
	                
	                // 2. 🚀 FINAL FIX: Get the component that is two levels up.
	                // This is often the actual ZK Window or Tab Panel container.
	                org.zkoss.zk.ui.Component target = form.getParent();
	                
	                if (target != null && target.getParent() != null) {
	                    // Try detaching the grandparent, which is likely the Tab Content.
	                    target.getParent().detach();
	                } else if (target != null) {
	                    // Fallback to detaching the immediate parent.
	                    target.detach();
	                } else {
	                    // Fallback to detaching the form itself.
	                    form.detach(); 
	                }
	            }
	        }
	    });
	}
	
	private void resetReportForm() throws FileNotFoundException {
	        
		fullPath = "";
	    log.warning(Msg.getMsg(Env.getCtx(), "FileXLSX")+": "+fullPath);
	    // Limpio el centro
	    center.getChildren().clear();
        lblStatus.setText(Msg.getMsg(Env.getCtx(), "FileXLSX"));
        textStatus.setText(fullPath);
      
	}
	  
	private String runServerProcessForm() {
		String fullPath="";
		File xlsxFile = null;
		int AD_Client_ID = fClient.getValue() != null ? ((Integer) fClient.getValue()) : Env.getAD_Client_ID(Env.getCtx());
		int C_AcctSchema_ID = fAcctSchema.getValue() != null ? ((Integer) fAcctSchema.getValue()) : 0;
		// Crear un ProcessInfo simulado para lockUI/unlockUI
	    ProcessInfo pi = new ProcessInfo(Msg.getMsg(Env.getCtx(), "Processing"), 0);
		
		try {
			// 🔒 Bloquear la UI y deshabilitar campos
	        lockUI(pi);
			xlsxFile = crearXlsx( AD_Client_ID, C_AcctSchema_ID);
		} catch (IOException e) {
			// 
			e.printStackTrace();
			// Si hay error, asigna un error a ProcessInfo para unlockUI
			pi.setSummary(Msg.getMsg(Env.getCtx(), "Error") + 
					Msg.getMsg(Env.getCtx(), "FileXLSX") +" "+  
					e.getMessage());
	        pi.setError(true);
		} finally {
			// 🔓 Desbloquear la UI y re-habilitar campos, incluso si hubo un error
	        unlockUI(pi);
		}
		if (xlsxFile!=null)
			fullPath = xlsxFile.getAbsolutePath();
		return fullPath;
		
	}

    private void previewReportWeb(String fullPath) throws FileNotFoundException {
        
    	log.warning("El archivo en Form:"+fullPath);
        // Limpio el centro
        center.getChildren().clear();

        // Creo y agrego el visor      
        int[] widths = {12, 30, 10, 10, 10, 10, 15};
        int maxVisibleRows =5000;
        
        ExcelViewerPanel viewer = new ExcelViewerPanel(fullPath, headers, widths, headerRows, maxVisibleRows);
		center.appendChild(viewer);
		lblStatus.setText(Msg.getMsg(Env.getCtx(), "FileXLSX"));
		textStatus.setText(fullPath);
    }
    
    
    private void downloadReport() {
        if (Util.isEmpty(fullPath, true)) {
            Dialog.warn(form.getWindowNo(), Msg.getMsg(Env.getCtx(), "FileXLSX")+" "+
            		Msg.getMsg(Env.getCtx(), "No")+" "+
            		Msg.getMsg(Env.getCtx(), "Created")+"!! ");
            return;
        }

        File file = new File(fullPath);
        if (!file.exists()) {
            Dialog.error(form.getWindowNo(), Msg.getMsg(Env.getCtx(), "FileXLSX")+ " "+
            		Msg.getMsg(Env.getCtx(), "does not exist")+ " :\n"+
            		fullPath);
            return;
        }

        try {
            // ⚠️ NO usar try-with-resources, el stream debe quedar abierto
            FileInputStream fis = new FileInputStream(file);

            // Detecta extensión y MIME
            String extension = fullPath.toLowerCase().endsWith(".xlsx") ? "xlsx" : "xls";
            String mimeType = extension.equals("xlsx")
                    ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    : "application/vnd.ms-excel";

            // Crear objeto de medios ZK
            AMedia media = new AMedia(file.getName(), extension, mimeType, fis);

            // Disparar la descarga
            Filedownload.save(media);

            // ✅ Cierra el flujo después de un pequeño retardo en un hilo aparte
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    fis.close();
                } catch (Exception ignored) {}
            }).start();

        } catch (Exception e) {
            Dialog.error(form.getWindowNo(),  Msg.getMsg(Env.getCtx(), "Error")+ " "+
            		"Downloading File:\n" + e.getMessage());
        }
    }

    
	/**
	 * crearXlsx : Con diferentes estilos y tamaños.
	 * @param 
	 * @return
	 * @throws IOException
	*/
    private File crearXlsx(int p_AD_Client_ID, int p_C_AcctSchema_ID) throws IOException {
    	
        // --- 1️⃣ Leer constantes globales antes del bucle
        String cliName = "";
        String cliDescription = "";
        byte[] cliLogo = null;
        MClient mclient = new MClient(Env.getCtx(),p_AD_Client_ID, null);
        if (mclient != null ) {
        	cliName = mclient.getName();
        	cliDescription = mclient.getDescription() != null ? mclient.getDescription() : mclient.getName();
        	 // --- 2️⃣ Obtener información del cliente (AD_ClientInfo)
            MClientInfo ci = MClientInfo.get(Env.getCtx(), p_AD_Client_ID);
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
        
        File tempFile = new File(tempDir, this.getClass().getName() + "_" + System.currentTimeMillis() + ".xlsx");
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
            
            // --- Nombre de la empresa
            XSSFRow nameRow = sheet.createRow(2); // fila 0
            Cell cellName = nameRow.createCell(0); // columna 1 (junto al logo)
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
            
            // --- Crear estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            

        	int[] maxLen = { 30, 50, 20, 20, 20, 20, 25 }; // ancho aproximado proporcional
        	for (int col = 0; col < maxLen.length; col++) {
        	    sheet.setColumnWidth(col, maxLen[col] * 256);
        	}

        	XSSFRow headerRow = sheet.createRow(headerRows);
        	for (int i = 0; i < headers.length; i++) {
        	    String translated = Msg.getElement(Env.getCtx(), headers[i]); 	//Traducciones
        	    Cell cell = headerRow.createCell(i);
        	    cell.setCellValue(translated);
        	    cell.setCellStyle(headerStyle);
        	}

            // --- Crear estilos de fuentes
            Map<String, CellStyle> styleMap = new HashMap<>();

            styleMap.put("L1", ExcelUtils.createStyle(workbook, 14, false));
            styleMap.put("L2", ExcelUtils.createStyle(workbook, 14, false));
            styleMap.put("L3", ExcelUtils.createStyle(workbook, 12, false));
            styleMap.put("L4", ExcelUtils.createStyle(workbook, 12, false));
            styleMap.put("L5", ExcelUtils.createStyle(workbook, 10, false));
            styleMap.put("L6", ExcelUtils.createStyle(workbook, 10, false));
            styleMap.put("L7", ExcelUtils.createStyle(workbook, 10, false));
            styleMap.put("L8", ExcelUtils.createStyle(workbook, 10, false));
            styleMap.put("L9", ExcelUtils.createStyle(workbook, 10, false));

            // Versiones en negrita (isSummary = 'Y')
            styleMap.put("L1B", ExcelUtils.createStyle(workbook, 14, true));
            styleMap.put("L2B", ExcelUtils.createStyle(workbook, 14, true));
            styleMap.put("L3B", ExcelUtils.createStyle(workbook, 12, true));
            styleMap.put("L4B", ExcelUtils.createStyle(workbook, 12, true));
            styleMap.put("L5B", ExcelUtils.createStyle(workbook, 10, true));
            styleMap.put("L6B", ExcelUtils.createStyle(workbook, 10, true));
            styleMap.put("L7B", ExcelUtils.createStyle(workbook, 10, true));
            styleMap.put("L8B", ExcelUtils.createStyle(workbook, 10, true));
            styleMap.put("L9B", ExcelUtils.createStyle(workbook, 10, true));

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

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }

            log.info("Xlsx - " + Msg.getMsg(Env.getCtx(), "Success")+": "+ tempFile.getAbsolutePath());
            return tempFile;
        }
    }
    
    @Override
    public void lockUI(ProcessInfo pi) {
    	// Shows a busy message over the entire ZK desktop (or the closest root component).
    	// 1. Mostrar el indicador de "ocupado"
        Clients.showBusy(pi.getTitle()); 
        
        // 2. Deshabilitar Editores y Botones
        fClient.setReadWrite(false);
        fAcctSchema.setReadWrite(false);
        dateFrom.setReadWrite(false);
        dateTo.setReadWrite(false);
        isBatch.setDisabled(false); // Checkbox usa setDisabled
        
        // Deshabilitar botones de control
        resetButton.setDisabled(true);
        processButton.setDisabled(true);
        downloadButton.setDisabled(true); // Opcional, si quieres bloquearlo mientras procesa
        cancelButton.setDisabled(true);
    }

    @Override
    public void unlockUI(ProcessInfo pi) {
        // Clears the busy message.
    	// 1. Cerrar el indicador de "ocupado"
        Clients.clearBusy();
        
        // 2. Habilitar Editores y Botones
        fClient.setReadWrite(true);
        fAcctSchema.setReadWrite(true);
        dateFrom.setReadWrite(true);
        dateTo.setReadWrite(true);
        isBatch.setDisabled(true);
        
        // Habilitar botones de control (manteniendo la lógica de habilitación de downloadButton)
        resetButton.setDisabled(false);
        processButton.setDisabled(false);
        cancelButton.setDisabled(false);
        
        // Opcional: Mostrar un mensaje de éxito o error
        if (!pi.isError()) {
            Dialog.info(m_WindowNo, Msg.getMsg(Env.getCtx(), "Success"));
        } else {
            Dialog.error(m_WindowNo, pi.getSummary());
        }
    }

    private boolean uiIsLocked = false;
    
    @Override
    public boolean isUILocked() {
        return uiIsLocked;
    }


	@Override
	public void ask(String message, Callback<Boolean> callback) {
	    Executions.schedule(form.getDesktop(), new EventListener<Event>() {
	        @Override
	        public void onEvent(Event event) throws Exception {
	            //
	            org.adempiere.webui.window.Dialog.ask(m_WindowNo, message, callback);
	        }
	    }, new Event("onAsk"));		
	}

	@Override
	public void askForInput(String message, Callback<String> callback) {
	    Executions.schedule(form.getDesktop(), new EventListener<Event>() {
	        @Override
	        public void onEvent(Event event) throws Exception {
	            // FIX: Replaced deprecated FDialog.askForInput with Dialog.askForInput
	            org.adempiere.webui.window.Dialog.askForInput(m_WindowNo, message, callback);
	        }
	    }, new Event("onAskForInput"));
	}

	@Override
	public void askForSecretInput(String message, Callback<String> callback) {
	    // Schedule the call to ensure it runs on the ZK Event Dispatch Thread (EDT)
	    Executions.schedule(form.getDesktop(), new EventListener<Event>() {
	        @Override
	        public void onEvent(Event event) throws Exception {
	            // Use the standard Dialog utility for masked input
	            org.adempiere.webui.window.Dialog.askForSecretInput(m_WindowNo, message, callback);
	        }
	    }, new Event("onAskForSecretInput"));
	}



	@Override
	public void download(File file) {
	    if (file == null || !file.exists()) {
	        log.warning("File not found or null: " + (file != null ? file.getAbsolutePath() : "null"));
	        // You may want to show an error dialog here
	        org.adempiere.webui.window.Dialog.warn(m_WindowNo, "File not found or null.");
	        return;
	    }

	    try {
	        // 1. Create a FileInputStream
	        // ⚠️ Note: Do NOT use try-with-resources here, as the stream must remain open
	        // until the ZK file download process starts (managed by the Filedownload utility).
	        FileInputStream fis = new FileInputStream(file);

	        // 2. Determine MIME type (as you did in downloadReport)
	        String fullPath = file.getAbsolutePath();
	        String extension = fullPath.toLowerCase().endsWith(".xlsx") ? "xlsx" : "xls";
	        String mimeType = extension.equals("xlsx")
	                ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
	                : "application/vnd.ms-excel";
	        
	        // 3. Create ZK media object
	        AMedia media = new AMedia(file.getName(), extension, mimeType, fis);

	        // 4. Initiate the download (ZK standard utility)
	        org.zkoss.zul.Filedownload.save(media);

	        // 5. Asynchronously close the stream (optional, but good practice for cleanup)
	        new Thread(() -> {
	            try {
	                Thread.sleep(2000); // Give ZK time to start transmission
	                fis.close();
	            } catch (Exception ignored) {}
	        }).start();

	    } catch (Exception e) {
	        log.severe("Error downloading file: " + e.getMessage());
	        org.adempiere.webui.window.Dialog.error(m_WindowNo, "Error al descargar el archivo:\n" + e.getMessage());
	    }
	}


	@Override
	public void statusUpdate(String message) {
		textStatus.setValue(message); // Set the value directly
		// Post an empty event to force a redraw on the ZK event queue
	    org.zkoss.zk.ui.event.Events.postEvent(new org.zkoss.zk.ui.event.Event(
	        "onStatusChange", textStatus));
	}

    
}

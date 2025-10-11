package org.amerp.reports.xlsx;

import java.io.FileNotFoundException;

import org.adempiere.util.IProcessUI;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.window.Dialog;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.ServerProcessCtl;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;

public class AccountElements_Tree_Form implements IFormController, EventListener<Event>, ValueChangeListener {

    private static final long serialVersionUID = 5435458512781959819L;

	private static final CLogger log = CLogger.getCLogger(AccountElements_Tree_Form.class);

	/** UI form instance */
	private CustomForm form = new CustomForm();

	
    /** Default constructor */
    public AccountElements_Tree_Form() {
        try {
            dynInit();
            initForm();
            zkInit();
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
   
    private IProcessUI processUI;
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
	private Label centerLabel = new Label();
	private Label lblStatus = new Label();
	
    // Contenedor Sur 
// private Vbox southBox = new Vbox();
    
	/** Grid layout of {@link #parameterPanel} */
//	private Grid parameterLayout = GridFactory.newGridLayout();		

	// Botones
	private Button refreshButton = new Button();
	private Button cancelButton = new Button();
	private Button resetButton = new Button();
	private Button zoomButton = new Button();
	private Button selectButton = new Button();
	private Button processButton = new Button();
    private Button previewButton  = new Button();
    private Button downloadButton  = new Button();
	/** Charges. Part of {@link #allocationLayout}. */

    private void dynInit() {
        // === Campo AD_Client_ID ===
        int AD_Column_ID = MColumn.getColumn_ID("AD_Client", "AD_Client_ID");
        MLookup lookupClient = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
        fClient = new WTableDirEditor("AD_Client_ID", true, false, true, lookupClient);
        fClient.setValue(Env.getAD_Client_ID(Env.getCtx()));
        fClient.addValueChangeListener(this);

        // === Campo C_AcctSchema_ID ===
        AD_Column_ID = MColumn.getColumn_ID("C_AcctSchema", "C_AcctSchema_ID");
        MLookup lookupAcctSchema = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
        fAcctSchema = new WTableDirEditor("C_AcctSchema_ID", true, false, true, lookupAcctSchema);
        fAcctSchema.setValue(Env.getContextAsInt(Env.getCtx(), "$C_AcctSchema_ID"));
        fAcctSchema.addValueChangeListener(this);
        
	    // ======= isBatch  =======
	    isBatch.setValue(false);
	    isBatch.addActionListener(this);
	    // ======= processUI ======
	    processUI = Env.getProcessUI(Env.getCtx());
    }



    /** Initialize layout */
    protected void initForm() {
    	
        // Layout general
        form.appendChild(mainLayout);
        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");

        mainLayout.appendChild(north);
        mainLayout.appendChild(center);
        mainLayout.appendChild(south);
    	

    }

    private void zkInit() {

		// ============================================================
		// ======== Client AcctSchema========
		// ============================================================
		dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		dateToLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
    	fClientLabel.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
    	fAcctSchemaLabel.setText(Msg.translate(Env.getCtx(), "C_AcctSchema_ID"));
		isBatch.setText(Msg.getMsg(Env.getCtx(), "Run as Job"));
		isBatchLabel.setText(Msg.getMsg(Env.getCtx(), "Run as Job"));
		isBatch.addActionListener(this);
		lblStatus.setText("Mensaje de Proceso.......!");
		
    	// ============================================================
    	// ======== Center a========
    	// ============================================================
    	centerLabel.setText("Centro del formulario (vista principal)");
    	center.appendChild(centerLabel);
    	
		// ============================================================
		// ======== Barra botones ========
		// ============================================================
		Hbox hboxButtons = new Hbox();
		hboxButtons.appendChild(refreshButton);
		hboxButtons.appendChild(resetButton);
		hboxButtons.appendChild(zoomButton);
		hboxButtons.appendChild(processButton);
		hboxButtons.appendChild(previewButton);
		hboxButtons.appendChild(downloadButton);
		south.appendChild(hboxButtons);
		
		// Process Button
		processButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Process")));
		processButton.addActionListener(this);
		processButton.setImage(ThemeManager.getThemeResource("images/Process16.png"));
		processButton.addEventListener("onClick", this);
		// Reset Button
		resetButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Reset")));
		resetButton.setImage(ThemeManager.getThemeResource("images/Reset16.png"));
		resetButton.addActionListener(this);
		// Refresh Button
		refreshButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")));
		refreshButton.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
		refreshButton.addActionListener(this);
		refreshButton.setAutodisable("self");
		// Cancel Button
		cancelButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Cancel")));
		cancelButton.setImage(ThemeManager.getThemeResource("images/Cancel16.png"));
		cancelButton.addActionListener(this);
		// Preview Button
		previewButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Preview")));
		previewButton.setImage(ThemeManager.getThemeResource("images/Multi16.png"));
		previewButton.addActionListener(this);
        previewButton.addEventListener("onClick", this);
		// Preview Button
		downloadButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Download")));
		downloadButton.setImage(ThemeManager.getThemeResource("images/Export16.png"));
		downloadButton.addActionListener(this);
        downloadButton.addEventListener("onClick", this);

		// ============================================================
		// ======== Área de filtros ========
		// ============================================================
		
        Grid filterGrid = new Grid();
        filterGrid.setWidth("100%");
        Rows rows = new Rows();

        Row row1 = new Row();
        row1.appendChild(fClientLabel);
        row1.appendChild(fClient.getComponent());
        rows.appendChild(row1);

        Row row2 = new Row();
        row2.appendChild(fAcctSchemaLabel);
        row2.appendChild(fAcctSchema.getComponent());
        rows.appendChild(row2);
        
        Row row3 = new Row();
        row3.appendChild(lblStatus);
        rows.appendChild(row3);
        
        filterGrid.appendChild(rows);
		north.appendChild(filterGrid);
    }

    /** Evento de botones */
    @Override
    public void onEvent(Event event) throws Exception {
        Object source = event.getTarget();
        int AD_Client_ID = fClient.getValue() != null ? ((Integer) fClient.getValue()) : Env.getAD_Client_ID(Env.getCtx());
        int C_AcctSchema_ID = fAcctSchema.getValue() != null ? ((Integer) fAcctSchema.getValue()) : 0;

        if (source == previewButton) {
        	previewReportWeb(fullPath);
        } else if (source == downloadButton) {


        } else if (source == processButton) {
        	fullPath = runServerProcess();
        	previewReportWeb(fullPath);
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
	

	private String runServerProcess() {
	    int processId = 1000185;
	    MProcess.get(Env.getCtx(), 1000185);
	    // AD_Process_ID=1000185
	    // AD_Process_UU=785b18a0-3a15-44ef-b84c-fa62035ef540
	    if (processId <= 0) {
	        Dialog.warn(form.getWindowNo(), null, "No se encontró el proceso AccountElements_Tree_xlsx");
	        return null;
	    }

	    // Process Info
	    ProcessInfo pi = new ProcessInfo("AccountElements_Tree_xlsx", processId);
	    pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));
	    pi.setAD_User_ID(Env.getAD_User_ID(Env.getCtx()));
	    pi.setRecord_ID(0);

	    // Crear array de parámetros
	    ProcessInfoParameter[] params = new ProcessInfoParameter[3];
	    params[0] = new ProcessInfoParameter("AD_Client_ID", fClient.getValue(), null, null, null);
	    params[1] = new ProcessInfoParameter("C_AcctSchema_ID", fAcctSchema.getValue(), null, null, null);
	    params[2] = new ProcessInfoParameter("Action", "Preview", null, null, null);

	    // Asociar parámetros al ProcessInfo
	    pi.setParameter(params);
	    
	    pi.setIsBatch(isBatch.isChecked());
	    

	    try {
            // Ejecuta el proceso directamente en segundo plano
            ServerProcessCtl.process(pi, null);
            // Parametros de retorno
            ProcessInfoParameter[] paramret = pi.getParameter();
            for (ProcessInfoParameter p : paramret) {
                if ("FullPath".equals(p.getParameterName())) {
                    fullPath = (String) p.getParameter();
                    // path contiene el fullPath generado
                }
            }
            log.warning("Archivo xlsx generado :"+fullPath);
            Dialog.info(form.getWindowNo(), "Proceso ejecutado correctamente en segundo plano");
	    } catch (Exception e) {
	        Dialog.error(form.getWindowNo(), null, "Error al ejecutar: " + e.getMessage());
	    } finally {
	    	
	    }
	    return fullPath;
	}
	
    private void previewReportWeb(String fullPath) throws FileNotFoundException {
        
    	log.warning("El archivo en Form:"+fullPath);
//    	   	
//    	//ExcelViewerForm.openViewer(fullPath);
//        ExcelViewerForm viewer = new ExcelViewerForm(fullPath);
//        viewer.setTitle("Visor Excel");
//
//        // Usar el API moderna de iDempiere
//        AEnv.showWindow(viewer);

        // Limpio el centro
        center.getChildren().clear();

        // Creo y agrego el visor
        ExcelViewerPanel viewer = new ExcelViewerPanel(fullPath);
        center.appendChild(viewer);
    	
    }
}

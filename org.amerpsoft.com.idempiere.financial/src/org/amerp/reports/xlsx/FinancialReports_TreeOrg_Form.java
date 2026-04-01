package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.util.IProcessUI;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.event.ActionEvent;
import org.adempiere.webui.event.ActionListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.Dialog;
import org.amerp.reports.xlsx.constants.FinancialReportConstants;
import org.amerp.reports.xlsx.generator.IReportGenerator;
import org.amerp.reports.xlsx.generator.ReportGeneratorFactory;
import org.amerp.reports.xlsx.generator.ReportMetadata;
import org.amerp.reports.xlsx.util.AccountUtils;
import org.amerp.reports.xlsx.util.MsgUtils;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MColumn;
import org.compiere.model.MElementValue;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPeriod;
import org.compiere.model.MRefList;
import org.compiere.model.Query;
import org.compiere.model.X_C_ElementValue;
import org.compiere.model.X_Fact_Acct;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.compiere.util.ValueNamePair;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;


public class FinancialReports_TreeOrg_Form  implements IFormController, EventListener<Event>, IProcessUI , ValueChangeListener, ActionListener{

	private static final CLogger log = CLogger.getCLogger(FinancialReports_TreeOrg_Form.class);

	/** UI form instance */
	private CustomForm form = new CustomForm();
	// Column Names Valid AD Names
	private int m_WindowNo = 0;
	private static int AD_Reference_ID_ReportType=0;
	// Variable para guardar valores seleccionado por el usuario.
    private String m_postingType_value = "A"; // valor por defecto
    private String m_reportType_value = FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE;
    private String m_reportType_name = "Trial Balance Report";
    private String m_allyearPeriods = "--"+Msg.getMsg(Env.getCtx(), "AllPeriods").trim()+ "--";
    private ProcessInfo m_Pi = null;
    private int maxVisibleRows =5000;
    List<KeyNamePair> m_periodList = new ArrayList<>();
    Integer m_defaultPeriodID=0;
    Integer m_yearID = 0;
    
    public FinancialReports_TreeOrg_Form() {
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
    private Label fReportTypeLabel = new Label();
    private Combobox fReportType;
    private Label fClientLabel = new Label();
    private WTableDirEditor fClient;
    private Label fOrgParentLabel = new Label();
    private WTableDirEditor fOrgParent;
    private Label fOrgLabel = new Label();
    private WTableDirEditor fOrg;
    private Label fAcctSchemaLabel = new Label();
    private WTableDirEditor fAcctSchema;
    private Label fPostingTypeLabel = new Label();
    private WTableDirEditor fPostingType;
    private Label fYearLabel = new Label();
    private WTableDirEditor fYear;
    private Label fPeriodLabel = new Label();
    private WTableDirEditor fPeriod;
    private Label dateRangeLabel = new Label();
    private Label dateFromLabel = new Label();
    private WDateEditor dateFrom =new WDateEditor();
    private Label dateToLabel = new Label();
    private WDateEditor dateTo = new WDateEditor();
    private Label fAccountLabel = new Label();
    private WSearchEditor fAccount;
    private Checkbox isShowOrganization = new Checkbox();
    private Checkbox isShowCrosstab = new Checkbox();
    private Checkbox isShowSummaryElements = new Checkbox();
    private Checkbox isShowSubTotal = new Checkbox();
    private Checkbox isPositiveBalance = new Checkbox();
    private Checkbox isShowMovementsAmounts = new Checkbox();
    private Checkbox isShowZERO = new Checkbox();
    private Label isBatchLabel = new Label();
    private Checkbox isBatch = new Checkbox(); 
    // Areas de Formulario
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

    /**
     * initForm()
     * Modelo y Parámetros.
     * Esta función es la primera en ejecutarse cuando se crea la instancia del formulario. Se enfoca en la preparación del modelo de datos y las variables globales.
	 * Se Suele Colocar, Variables de Instancia, Inicialización de wrappers (fOrg, fPeriod, etc.) antes de que se conecten a la UI. 
	 * Recuperación de Parámetros: Obtener los parámetros iniciales pasados al formulario (si los hay).
	 * Configuración de la Sesión:Acceder al contexto (Env.getCtx()) R
	 * Realizar preparaciones a nivel de datos que no dependen de la UI
     */
    protected void initForm() {
    	
        // Layout general
        form.appendChild(mainLayout);
        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");
        
        // Configuramos el north
        north.setVisible(true);
        north.setCollapsible(true);
        north.setOpen(true);
        north.setTitle(Msg.getMsg(Env.getCtx(), "SearchCriteria"));
        north.setVflex("min");       // altura mínima según contenido
        north.setAutoscroll(true);    // scroll si el contenido es mayor
        northLabel.setText("Parámetros del Informe");
        
        // mainLayout
        mainLayout.appendChild(north);
        mainLayout.appendChild(center);
        mainLayout.appendChild(south);
        
    	// ============================================================
    	// ======== Center a========
    	// ============================================================
    	centerLabel.setText("Centro del formulario (vista principal)");
    	center.appendChild(centerLabel);
        
    	// ============================================================
    	// ======== Barra botones (Estructura Hbox) ========
    	// ============================================================
    	Hbox hboxButtons = new Hbox();
    	hboxButtons.appendChild(resetButton);
    	hboxButtons.appendChild(processButton);
    	hboxButtons.appendChild(downloadButton);
    	hboxButtons.appendChild(cancelButton);
    	south.appendChild(hboxButtons);

    }
    
	/**
	 * zkInit()
	 * Contenedores y Estructura Visual.
	 * Esta función es donde se construye el esqueleto visual del formulario (los contenedores ZK, como Grids, Rows, Boxes, etc.) y se cargan los componentes.
	 * Se Suele Colocar Propósito
	 * Carga de Componentes ZK, Inicialización del Grid, Rows, etc., Asignación de *Labels* , Inicializar y obtener los textos de las etiquetas (Msg.getMsg()).
	 * Adición de Componentes, Agregar los wrappers (fClient.getComponent(), fPeriod.getComponent()) a las filas (row.appendChild(...)).
	 * Estructura de *Layout* , Definir la disposición visual y el tamaño de los contenedores principales. 
	 */
    private void zkInit() {

		// =======================================================
		// FinancialReportType Type of report ====================
		// =======================================================
    	AD_Reference_ID_ReportType = FinancialReportConstants.getReferenceIDByUU(Env.getCtx(), FinancialReportConstants.AD_REFERENCE_REPORT_TYPE_UU); 
        fReportType = new Combobox();
        List<ValueNamePair> reportTypes = getSortedReportTypes();
        fReportType.setModel(new ListModelList<>(reportTypes));
        fReportType.setItemRenderer((item, data, index) -> {
            ValueNamePair vnp = (ValueNamePair) data; // <-- cast necesario
            item.setLabel(vnp.getName());
            item.setValue(vnp); 
        });
        // Seleccionar valor inicial
        String valueToSet = (m_reportType_value == null || m_reportType_value.isEmpty())
                            ? FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE
                            : m_reportType_value;
        for (ValueNamePair vnp : reportTypes) {
            if (vnp.getValue().equals(valueToSet)) {
                fReportType.setValue(vnp.getName());
                m_reportType_value = vnp.getValue();
                m_reportType_name  = vnp.getName();
                break;
            }
        }
        Events.postEvent(new Event("onChange", fReportType));
        // Tooltip
        fReportType.setTooltiptext(FinancialReportConstants.getReferenceToolTip(Env.getCtx()));
        // Listener
        fReportType.addEventListener("onChange", event -> {
            if (fReportType.getSelectedItem() != null) {
                ValueNamePair selected = (ValueNamePair) fReportType.getSelectedItem().getValue();
                m_reportType_value = selected.getValue();
                m_reportType_name = selected.getName();
                // 2. 🛑 LLAMADA AL MÉTODO DE LÓGICA 🛑
                reportTypeChanged(m_reportType_value);
            }
        });

	    // =======================================================
		// === Campo AD_Client_ID fClient ======
		// =======================================================
		final String COLUMN_NAME_CLIENT = "AD_Client_ID";
		int AD_Column_ID_Client = MColumn.getColumn_ID("AD_Client", COLUMN_NAME_CLIENT);
		MLookup lookupClient = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_Client, DisplayType.TableDir);
		fClient = new WTableDirEditor(COLUMN_NAME_CLIENT, true, false, true, lookupClient);
        fClient.addValueChangeListener(this);
        
        // fOrgParent
		// =======================================================
		// === Campo AD_OrgParent_ID (Organizaciones Resumen) ===
		// =======================================================
		int AD_Column_ID_OrgParent = MColumn.getColumn_ID("AD_Org", "AD_Org_ID");
		MLookup lookupOrgParent = null;
		// Definir el filtro estático
		String whereParent = "AD_Org.IsSummary='Y'";
		// El nombre de columna que necesita MLookupFactory
		final String COLUMN_NAME = "AD_Org_ID"; 
		try {
	         // Usamos la firma compleja, reemplazando 'null' por "AD_Org_ID"
	         lookupOrgParent = MLookupFactory.get(
	             Env.getCtx(), 
	             form.getWindowNo(), 
	             0, 
	             AD_Column_ID_OrgParent, 
	             Env.getLanguage(Env.getCtx()), // Language
	             COLUMN_NAME,                   
	             DisplayType.TableDir,          // AD_Reference_ID (DisplayType)
	             false,                         // IsParent
	             whereParent                    // Filtro
	         );
		} catch (Exception e) {
	         // Manejo de errores
	         e.printStackTrace();
		}
		fOrgParent = new WTableDirEditor("AD_OrgParent_ID", true, false, true, lookupOrgParent);
        fOrgParent.addValueChangeListener(this);
        
		// ===============================================================
		// === Campo AD_Org_ID (Organizaciones Hijas - Instanciación) ===
		// ===============================================================
		int AD_Column_ID = MColumn.getColumn_ID("AD_Org", "AD_Org_ID");
		MLookup lookupOrg = null;
		// Filtro inicial: Evita mostrar todas las organizaciones hasta que se seleccione el padre.
		String whereOrg = "AD_Org.IsSummary='N'"; // Muestra TODAS las organizaciones no-resumen al iniciar
		final String COLUMN_NAME2 = "AD_Org_ID"; 
		try {
		// Usar la única firma compleja que compila.
		lookupOrg = MLookupFactory.get(
		      Env.getCtx(), 
		      form.getWindowNo(), 
		      0, 
		      AD_Column_ID, 
		      Env.getLanguage(Env.getCtx()), 
		      COLUMN_NAME2,                 
		      DisplayType.TableDir,         // Este DisplayType se renderiza como Combobox en ZK
		          false, 
		          whereOrg                      
		      );
		} catch (Exception e) {
		      CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al inicializar MLookup para fOrg.", e);
		}
		// Inicializar el editor WTableDirEditor
		fOrg = new WTableDirEditor("AD_Org_ID", true, false, true, lookupOrg);
        fOrg.addValueChangeListener(this);
		
        // ============================================================
        // fAcctSchema
		// ============================================================
        int AD_Column_ID_AcctSchema = MColumn.getColumn_ID("C_AcctSchema", "C_AcctSchema_ID");
        MLookup lookupAcctSchema = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_AcctSchema, DisplayType.TableDir);
        fAcctSchema = new WTableDirEditor("C_AcctSchema_ID", true, false, true, lookupAcctSchema);
        fAcctSchema.addValueChangeListener(this);
        
        // ============================================================
        // fPostingType
        // ============================================================
        MLookup lookupPostingType = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, X_Fact_Acct.POSTINGTYPE_AD_Reference_ID, DisplayType.List);
        fPostingType = new WTableDirEditor("PostingType", true, false, true, lookupPostingType);
        // LLAMA AL MÉTODO PARA CARGAR Y RESTAURAR EL COMBO
        initPostingTypeCombo(); 
        //. REGISTRA EL LISTENER (Solo una vez)
        fPostingType.addValueChangeListener(this);
        
        // ============================================================
        // fYear
        // ============================================================
        int AD_Column_ID_Year = MColumn.getColumn_ID("C_Year", "C_Year_ID");
        MLookup lookupYear = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_Year, DisplayType.TableDir);
        fYear = new WTableDirEditor("C_Year_ID", true, false, true, lookupYear);
        fYear.addValueChangeListener(this);
        
        // ============================================================
        // fPeriod
        // ============================================================
        int AD_Column_ID_Period = MColumn.getColumn_ID("C_Period", "C_Period_ID");
        MLookup lookupPeriod = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_Period, DisplayType.TableDir);
        fPeriod = new WTableDirEditor("C_Period_ID", true, false, true, lookupPeriod);
        fPeriod.addValueChangeListener(this);

        // =======================================================
        // === INSTANCIACIÓN DE DATEFROM Y DATETO  ===
        // =======================================================
		// ======= DateFrom DateTo =======
        // Añadir listeners para que reaccionen a cambios posteriores
        if (dateFrom != null) dateFrom.addValueChangeListener(this);
        if (dateTo != null) dateTo.addValueChangeListener(this);

		
		// =======================================================
	    // === INICIALIZACIÓN: Cuenta Contable (fAccount) ===
	    // =======================================================
	    int AD_Column_ID_Account = MColumn.getColumn_ID(X_C_ElementValue.Table_Name, X_C_ElementValue.COLUMNNAME_C_ElementValue_ID);
		MLookup lookupAccount = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_Account, DisplayType.Search);
		fAccount = new WSearchEditor("C_ElementValue_ID", false, false, true, lookupAccount);
        fAccount.setMandatory(false);
        fAccount.setVisible(false);
        fAccountLabel.setVisible(false);
	    fAccount.addValueChangeListener(this);
	    
	    
        // ============================================================
        // ======== Label Settings  ========
        // ============================================================
		fReportTypeLabel.setText(FinancialReportConstants.getReferenceDescription(Env.getCtx()));
        fClientLabel.setText(Msg.translate(Env.getCtx(), "AD_Client_ID"));
        fOrgParentLabel.setText(Msg.translate(Env.getCtx(), "AD_OrgParent_ID"));
        fOrgLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
        fAcctSchemaLabel.setText(Msg.translate(Env.getCtx(), "C_AcctSchema_ID"));
        fPostingTypeLabel.setText(Msg.translate(Env.getCtx(), "PostingType"));
        fPeriodLabel.setText(Msg.translate(Env.getCtx(), "C_Period_ID"));
        fYearLabel.setText(Msg.translate(Env.getCtx(), "C_Year_ID"));
        dateRangeLabel.setText(Msg.translate(Env.getCtx(), "Date")+"(s)");
        dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
        dateToLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
        dateToLabel.setText(Msg.translate(Env.getCtx(),"DateTo"));
        String IncomeSummary_Acct_Element_Name = "IncomeSummary_Acct";
        fAccountLabel.setText(Msg.getElement(Env.getCtx(), IncomeSummary_Acct_Element_Name));
        isShowOrganization.setText(Msg.translate(Env.getCtx(), "isShowOrganization"));
        isShowOrganization.setTooltiptext(MsgUtils.getElementFullDescription("isShowOrganization"));
        isShowCrosstab.setText(Msg.translate(Env.getCtx(), "isShowCrosstab"));
        isShowCrosstab.setTooltiptext(MsgUtils.getElementFullDescription("isShowCrosstab"));
        isShowSummaryElements.setText(Msg.translate(Env.getCtx(),"isShowSummaryElements"));
        isShowSummaryElements.setTooltiptext(MsgUtils.getElementFullDescription("isShowSummaryElements"));
        isShowSubTotal.setText(Msg.translate(Env.getCtx(),"isShowSubTotal"));
        isShowSubTotal.setTooltiptext(MsgUtils.getElementFullDescription("isShowSubTotal"));
        isPositiveBalance.setText(MsgUtils.getElementName("PositiveBalance"));
        isPositiveBalance.setTooltiptext(MsgUtils.getElementFullDescription("PositiveBalance"));
        isShowMovementsAmounts.setText(Msg.translate(Env.getCtx(),"isShowMovementsAmounts"));
        isShowMovementsAmounts.setTooltiptext(MsgUtils.getElementFullDescription("isShowMovementsAmounts"));
        isShowZERO.setText(Msg.translate(Env.getCtx(), "isShowZERO"));
        isBatch.setText(Msg.translate(Env.getCtx(), "BackgroundJob"));
        isBatchLabel.setText(Msg.translate(Env.getCtx(), "BackgroundJob"));
        // isBatch.addActionListener(this); // <- ESTO DEBE IR EN dynInit()
        lblStatus.setText(Msg.getMsg(Env.getCtx(), "FileXLSX"));
        textStatus.setText("");
        textStatus.setReadonly(true);
        textStatus.setStyle("text-align: left");
        
        // ============================================================
        // ========  Área de filtros  ========
        // ============================================================
        
        Grid filterGrid = new Grid();
        filterGrid.setWidth("100%");
        
		// ==========================
		// Configuración de columnas
		// ==========================
		Columns columns = new Columns();
		Column col1 = new Column();
		col1.setWidth("250px"); // ancho fijo para labels
		columns.appendChild(col1);
		Column col2 = new Column();
		col2.setHflex("1");     // ancho flexible
		columns.appendChild(col2);
		Column col3 = new Column();
		col3.setHflex("1");     // ancho flexible
		columns.appendChild(col3);
		filterGrid.appendChild(columns);
        
        Rows rows = new Rows();
        
        Row row = new Row();
        row.appendChild(fReportTypeLabel);
        row.appendChild(fReportType);
        row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fReportType, "1");
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fClientLabel);
        row.appendChild(fClient.getComponent());
        row.appendChild(new Label("")); // tercera columna vacía
        ZKUpdateUtil.setHflex(fClient.getComponent(), "1"); // segunda columna ocupa todo
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fOrgParentLabel);
        row.appendChild(fOrgParent.getComponent());
        row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fOrgParent.getComponent(), "1");
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fOrgLabel);
        row.appendChild(fOrg.getComponent());
        row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fOrg.getComponent(), "1");
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fAcctSchemaLabel);
        row.appendChild(fAcctSchema.getComponent());
        row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fAcctSchema.getComponent(), "1");
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fPostingTypeLabel);
        row.appendChild(fPostingType.getComponent());
        row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fPostingType.getComponent(), "1");
        rows.appendChild(row);
        
        row = new Row();
        row.appendChild(fYearLabel);
        row.appendChild(fYear.getComponent());
        row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fYear.getComponent(), "1");
         rows.appendChild(row);

        // --- Rango de Fechas (Desde/Hasta) ---
        org.zkoss.zul.Hbox dateRangeDate = new org.zkoss.zul.Hbox();
        dateRangeDate.setSpacing("10px"); // Aumentamos el espacio para claridad
        // Agrupamos: [Desde] [dateFrom.getComponent()] [Hasta] [dateTo.getComponent()]
        dateRangeDate.appendChild(dateFromLabel);
        dateRangeDate.appendChild(dateFrom.getComponent());
        dateRangeDate.appendChild(dateToLabel);
        dateRangeDate.appendChild(dateTo.getComponent());
        row = new Row();
        row.appendChild(fPeriodLabel);
        row.appendChild(fPeriod.getComponent());
        ZKUpdateUtil.setHflex(fPeriod.getComponent(), "1");
        row.appendChild(dateRangeDate);
        ZKUpdateUtil.setHflex(dateRangeDate, "1");
        rows.appendChild(row);

        // --- Rango de Fechas (Desde/Hasta) ---
        org.zkoss.zul.Hbox dateFromRa = new org.zkoss.zul.Hbox();
        dateFromRa.setSpacing("10px"); // Aumentamos el espacio para claridad
        org.zkoss.zul.Hbox dateToRa = new org.zkoss.zul.Hbox();
        dateToRa.setSpacing("10px"); // Aumentamos el espacio para claridad
        // Agrupamos: [Desde] [dateFrom.getComponent()] [Hasta] [dateTo.getComponent()]
        dateFromRa.appendChild(dateFromLabel);
        dateFromRa.appendChild(dateFrom.getComponent());
        dateToRa.appendChild(dateToLabel);
        dateToRa.appendChild(dateTo.getComponent());
        row = new Row();
        // La HBox de rango de fechas ocupa la segunda celda de la Row.
        row.appendCellChild(dateRangeLabel);
        row.appendChild(dateFromRa);
        ZKUpdateUtil.setHflex(dateFromRa, "1");
        row.appendChild(dateToRa);
        ZKUpdateUtil.setHflex(dateToRa, "1");
        rows.appendChild(row);
        
        row = new Row();
	    row.appendChild(fAccountLabel);
	    row.appendChild(fAccount.getComponent());
	    row.appendChild(new Label(""));
        ZKUpdateUtil.setHflex(fAccount.getComponent(), "1");
	    rows.appendChild(row);
        
        // Checkboxes
        row = new Row();
        row.appendCellChild(isShowSummaryElements,1);	
        row.appendCellChild(isPositiveBalance,2);
        rows.appendChild(row);

	    row = new Row();
        row.appendChild(isShowOrganization);	
        row.appendChild(isShowCrosstab);
        row.appendChild(isShowMovementsAmounts);
        rows.appendChild(row);

        row = new Row();
        row.appendCellChild(isShowZERO,1);	
        rows.appendChild(row);
        
        // Status
        row = new Row();
        row.appendChild(lblStatus);
        ZKUpdateUtil.setHflex(textStatus, "true");
        row.appendCellChild(textStatus,2);
        rows.appendChild(row);
        
        filterGrid.appendChild(rows);
        north.appendChild(filterGrid);
    }
    
    /**
     * dynInit()
     * Datos, Lógica, Lookups y Valores por Defecto.
     */
    private void dynInit() {
        
	    // =======================================================
	    // === Campo FinancialReportType (fReportType) ===
	    // =======================================================
    	// Inicialización de fReportType: Carga modelo y establece el valor inicial ("TRB")
        initReportTypeCombo(); 
        // ... (Resto de la lógica, incluyendo PostingType y Tooltip) ...
        ((org.zkoss.zul.Combobox) fReportType).setTooltiptext(FinancialReportConstants.getReferenceToolTip(Env.getCtx()));
	     
        // =======================================================
        // === Campo AD_Client_ID ===
    	// =======================================================
        final String COLUMN_NAME_CLIENT = "AD_Client_ID";
        // MLookup y fClient ya están instanciados en zkInit()
        fClient.setValue(Env.getAD_Client_ID(Env.getCtx()));
        // Obtener la descripción usando la utilidad con el nombre de la columna.
        String tooltipText = MsgUtils.getElementFullDescription(COLUMN_NAME_CLIENT);
        if (tooltipText == null || tooltipText.isEmpty()) {
            tooltipText = "Identificador del Cliente actual de la sesión."; 
        }
        // Aplicar el Tooltip al componente ZK (Combobox) (Modo validacion previa)
        org.zkoss.zk.ui.Component clientComponent = fClient.getComponent();
        if (clientComponent instanceof org.zkoss.zul.Combobox) {
            ((org.zkoss.zul.Combobox) clientComponent).setTooltiptext(tooltipText);
        } else if (clientComponent instanceof org.zkoss.zul.Div) {
            ((org.zkoss.zul.Div) clientComponent).setTooltiptext(tooltipText);
        }
        
	     // =======================================================
	     // === Campo AD_OrgParent_ID (Organizaciones Resumen) ===
	     // =======================================================
	     // fOrgParent ya está instanciado en zkInit()
	     fOrgParent.setValue(Env.getContextAsInt(Env.getCtx(), "$AD_OrgParent_ID"));
	     // tooltip simple
	     ((org.zkoss.zul.Combobox) fOrgParent.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("AD_OrgParent_ID"));
	     
		 // ===============================================================
		 // === Campo AD_Org_ID (Organizaciones Hijas - Asignación de Valores) ===
		 // ===============================================================
		 // ** ¡ATENCIÓN! Se elimina toda la duplicación de MLookupFactory y la re-instanciación de fOrg. **
		 // Se usa el fOrg ya creado en zkInit().
		 fOrg.setValue(Env.getContextAsInt(Env.getCtx(), "$AD_Org_ID"));
		 ((org.zkoss.zul.Combobox) fOrg.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("AD_Org_ID"));
        
        // =======================================================
        // === Campo C_AcctSchema_ID ===
        // =======================================================
        // MLookup y fAcctSchema ya están instanciados en zkInit()
        fAcctSchema.setValue(Env.getContextAsInt(Env.getCtx(), "$C_AcctSchema_ID"));
        ((org.zkoss.zul.Combobox) fAcctSchema.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("C_AcctSchema_ID"));
        
        // =======================================================
        // === Campo PostingType ===
        // =======================================================
        // Obtener el valor de referencia de la lista
        // LLAMA AL MÉTODO PARA CARGAR Y RESTAURAR EL COMBO
        initPostingTypeCombo(); 
        
        // =======================================================
        // === Campo C_Year_ID (Carga directa ZK) ===
        // =======================================================
        final String COLUMN_NAME_YEAR = "FiscalYear"; 
        Integer C_Calendar_ID = 0;
        MClient mclient = new MClient(Env.getCtx(),Env.getAD_Client_ID(Env.getCtx()), null);
        if (mclient != null ) {
        	 // --- 2️⃣ Obtener información del cliente (AD_ClientInfo)
            MClientInfo ci = MClientInfo.get(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()));
            if (ci != null ) {
            	C_Calendar_ID = ci.getC_Calendar_ID();
            }	
        }
        // 2. Ejecutar consulta SQL para obtener la lista ordenada (Manualmente)
        List<KeyNamePair> yearList = new ArrayList<>();
        Integer defaultYearID = null; // Usaremos esto para el valor por defecto
        String sqlYears = 
        	      "SELECT C_Year_ID, "
        	    + "CASE "
        	    + "  WHEN Description IS NOT NULL THEN FiscalYear || ' ' || Description "
        	    + "  ELSE FiscalYear "
        	    + "END AS FiscalYearDesc "
        	    + "FROM C_Year "
        	    + "WHERE IsActive='Y' "
        	    + "AND AD_Client_ID IN (0, ?) "
        	    + "AND C_Calendar_ID = ? "
        	    + "ORDER BY FiscalYear DESC";
        try (PreparedStatement pstmt = DB.prepareStatement(sqlYears, null)) {
            pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            pstmt.setInt(2, C_Calendar_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int yearID = rs.getInt(1);
                    String yearName = rs.getString(2);
                    // Si es el primer resultado, lo guardamos como valor por defecto
                    if (defaultYearID == null) {
                        defaultYearID = yearID; 
                    }
                    yearList.add(new KeyNamePair(yearID, yearName));
                }
            }
        } catch (Exception e) {
            CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al cargar la lista de períodos.", e);
        }
        // 4. Asignar la lista y el valor por defecto directamente al Combobox ZK
        try {
            org.zkoss.zul.Combobox combobox = (org.zkoss.zul.Combobox) fYear.getComponent();       
            // Crear y asignar el modelo ZK (ListModelList)
            org.zkoss.zul.ListModelList<KeyNamePair> model = new org.zkoss.zul.ListModelList<>(yearList);
            combobox.setModel(model);          
            // 5. Establecer el valor por defecto: Contexto o el período más reciente (Primer elemento)
            Timestamp contextDate = Env.getContextAsDate(Env.getCtx(), "#Date");
            if (contextDate == null) {
                contextDate = new Timestamp(System.currentTimeMillis());
            }
//            Integer contextYearID = Env.getContextAsInt(Env.getCtx(), "$C_Year_ID");
            // 2. Buscar el C_Year_ID correspondiente a esa fecha
            Integer contextYearID = DB.getSQLValue(
            	    null,
            	    """
            	    SELECT y.C_Year_ID
            	    FROM C_Year y
            	    JOIN C_Period p ON p.C_Year_ID = y.C_Year_ID
            	    WHERE y.AD_Client_ID = ?
            	      AND y.C_Calendar_ID = ?
            	      AND ? BETWEEN p.StartDate AND p.EndDate
            	    FETCH FIRST 1 ROWS ONLY
            	    """,
            	    Env.getAD_Client_ID(Env.getCtx()),
            	    C_Calendar_ID,
            	    contextDate
            	);
            if (contextYearID != null && contextYearID.intValue() > 0) {
                fYear.setValue(contextYearID);
                m_yearID= contextYearID;
            } else {
                fYear.setValue(defaultYearID); 
                m_yearID= defaultYearID;
            }
        } catch (Exception e) {
            CLogger.getCLogger(getClass()).log(Level.SEVERE, 
                "Fallo al forzar el setModel() o la selección para fYear. El componente podría no ser un Combobox.", e);
        }
        // fYear.addValueChangeListener(this);
        ((org.zkoss.zul.Combobox) fYear.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription(COLUMN_NAME_YEAR));
        
        // =======================================================
        // === Campo C_Period_ID (Carga directa ZK) ===
        // =======================================================
        final String COLUMN_NAME_PERIOD = "C_Period_ID"; 
        // fPeriod ya está instanciado en zkInit() con el lookup base.
        // 2. Ejecutar consulta SQL para obtener la lista ordenada (Manualmente)
        List<KeyNamePair> periodList = new ArrayList<>();
        // AGREGAR OPCIÓN "TODO EL AÑO" AL PRINCIPIO
        // Usamos -1 para diferenciarlo de IDs reales de la DB
        periodList.add(new KeyNamePair(-1, m_allyearPeriods));
        //
        Integer defaultPeriodID = null; // Usaremos esto para el valor por defecto
        // Construir SQL dinámicamente
        StringBuilder sqlPeriods = new StringBuilder(
              "SELECT C_Period_ID, Name "
            + "FROM C_Period "
            + "WHERE IsActive='Y' "
            + "AND AD_Client_ID IN (0, ?) "
        );
        // Agregar filtro opcional
        if (defaultYearID != null) {
            sqlPeriods.append("AND C_Year_ID = ? ");
        }
        sqlPeriods.append("ORDER BY StartDate DESC");
        try (PreparedStatement pstmt = DB.prepareStatement(sqlPeriods.toString(), null)) {
            int paramIndex = 1;
            pstmt.setInt(paramIndex++, Env.getAD_Client_ID(Env.getCtx()));
            if (defaultYearID != null) {
                pstmt.setInt(paramIndex++, defaultYearID);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int periodID = rs.getInt("C_Period_ID");
                    String name = rs.getString("Name");
                    periodList.add(new KeyNamePair(periodID, name));
                    // Opcional: usar el primero como default
                    if (defaultPeriodID == null) {
                        defaultPeriodID = periodID;
                    }
                }
            }
        } catch (Exception e) {
            CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al cargar la lista de períodos.", e);
        }
        // 4. Asignar la lista y el valor por defecto directamente al Combobox ZK
        try {
            org.zkoss.zul.Combobox combobox = (org.zkoss.zul.Combobox) fPeriod.getComponent();       
            // Crear y asignar el modelo ZK (ListModelList)
            org.zkoss.zul.ListModelList<KeyNamePair> model = new org.zkoss.zul.ListModelList<>(periodList);
            combobox.setModel(model);          
            // 5. Establecer el valor por defecto: Período según la fecha de contexto
            Timestamp contextDate = Env.getContextAsDate(Env.getCtx(), "#Date");
            if (contextDate == null) {
                contextDate = new Timestamp(System.currentTimeMillis());
            }
            // Obtener el período según calendario y fecha
            int contextPeriodID = MPeriod.getC_Period_ID(
                    Env.getCtx(),
                    contextDate,
                    Env.getAD_Org_ID(Env.getCtx())
            );
            if (contextPeriodID > 0) {
                fPeriod.setValue(contextPeriodID);
            } else {
                fPeriod.setValue(defaultPeriodID);
            }
        } catch (Exception e) {
            CLogger.getCLogger(getClass()).log(Level.SEVERE, 
                "Fallo al forzar el setModel() o la selección para fPeriod. El componente podría no ser un Combobox.", e);
        }
        m_defaultPeriodID = defaultPeriodID;
  //      fPeriod.addValueChangeListener(this);
        ((org.zkoss.zul.Combobox) fPeriod.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription(COLUMN_NAME_PERIOD));


        // ==================================================================
        // === Lógica de Inicialización de dateFrom/dateTo de C_Period_ID ===
        // ==================================================================
        Integer initialPeriodID = (Integer) fPeriod.getValue(); 

        if (initialPeriodID != null && initialPeriodID.intValue() > 0) {
            // Si hay un período válido cargado inicialmente, usa SUS fechas.
            setDatesFromPeriod(initialPeriodID, m_reportType_value ,  periodList,m_yearID);
        
        } else {
            // Si no hay período válido, usa la lógica de "Primer y Último Día del Mes".
            Calendar cal = Calendar.getInstance();
            Timestamp ctxDate = Env.getContextAsDate(Env.getCtx(), "#Date");
            
            if (ctxDate != null) {
                cal.setTime(ctxDate);
            } 
            
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            Timestamp m_DateFrom = new Timestamp(cal.getTimeInMillis());
            
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Timestamp m_DateTo = new Timestamp(cal.getTimeInMillis());
            
            if (dateFrom != null) { 
                dateFrom.setValue(m_DateFrom);
            }
            if (dateTo != null) { 
                dateTo.setValue(m_DateTo);
            }
        }
        
        // =======================================================
        // === Cuenta Contable (fAccount) ===
        // =======================================================
        if (fAccount != null && fAcctSchema != null) {
            Integer C_AcctSchema_ID = (Integer) fAcctSchema.getValue(); 
            int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
            
            if (C_AcctSchema_ID != null && C_AcctSchema_ID.intValue() > 0) {
                
                int defaultAccountID = 0;
                
                // Usar la Cuenta de Resumen de Ingresos
                defaultAccountID = AccountUtils.getIncomeSummaryAccountID(C_AcctSchema_ID.intValue(), AD_Client_ID);
                
                // 2. FALLBACK: Si es nulo (0), usar la primera Cuenta de Detalle válida
                if (defaultAccountID <= 0) {
                    log.fine("IncomeSummary_Acct no encontrado o nulo. Cayendo a getDefaultDetailAccount.");
                    defaultAccountID = AccountUtils.getDefaultDetailAccount(C_AcctSchema_ID.intValue());
                }
                
                if (defaultAccountID > 0) {
                    // Asignar el ID de la cuenta (que ahora es el mejor disponible)
                    fAccount.setValue(defaultAccountID);
                }
            }
        }
        
        
        // =======================================================================
        // ======= isShowSummaryElements, isPositiveBalance =======
        // =======================================================================
        isShowSummaryElements.setChecked(true);
        isPositiveBalance.setChecked(true);
        
        // =======================================================
        // ======= isShowOrganization  =======
        // =======================================================
        isShowOrganization.setChecked(true);
        isShowOrganization.addActionListener(this);

        // =======================================================
        // ======= isShowCrosstab  =======
        // =======================================================
        isShowCrosstab.setChecked(true);
        isShowMovementsAmounts.setChecked(false);
        isShowCrosstab.addActionListener(this);

        // =======================================================
        // ======= isShowZERO  =======
        // =======================================================
        isShowZERO.setChecked(false);
        isShowZERO.addActionListener(this);
        
        // =======================================================
        // ======= isBatch  =======
        // =======================================================
        isBatch.setValue(false);
        isBatch.addActionListener(this); // <- Listener de isBatch
        
        
        // ============================================================
        // ======== Lógica de Botones  ========
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
        downloadButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "download.file")));
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

	@Override
	public ADForm getForm() {
		return form;
	}

	/** Evento de botones */
    @Override
    public void onEvent(Event event) throws Exception {
    	
    	log.warning("onEvent="+event.getName()+" Target="+ event.getTarget());
    	
    	Object source = event.getTarget();

        if (source == resetButton) {
        	resetReportForm();
        } else if (source == downloadButton) {
        	downloadReport();
        } else if (source == processButton) {
        	ReportMetadata metadata  = runServerProcessForm();
        	// Acceder a las variables para pasarlas al viewer
        	if (metadata == null) {
                if (m_Pi != null && m_Pi.isError()) {
                	Dialog.warn(form.getWindowNo(), "No se generó el archivo. \n\r"+m_Pi.getSummary());
                    return;
                } else {
                    Dialog.warn(form.getWindowNo(), "No se generó el archivo.");
                    return;
                }
        	}
        	fullPath = metadata.getFullPath();
        	String[] headers = metadata.getHeaders();
        	int[] widths = metadata.getWidths();
        	int headerRows = metadata.getHeaderRows();
        	previewReportWeb(fullPath, headers, widths, headerRows, maxVisibleRows);
        } else if (source == cancelButton) {
        	// Cerrar Formulario
        	closeReportForm(); 
        
        } else if (source == isShowOrganization ) {

        	Boolean showOrg = isShowOrganization.isChecked();
        	if (showOrg) {
	            // Habilitar y marcar Crosstab por defecto.
	        	isShowCrosstab.setDisabled(false);  // Habilitar
	            isShowCrosstab.setChecked(true);    // Marcar por defecto
	            isShowMovementsAmounts.setDisabled(false); 
	            isShowMovementsAmounts.setChecked(false);
	        } else {
	            // Deshabilitar (opaco) y desmarcar Crosstab.
	            isShowCrosstab.setChecked(false);  // Desmarcar
	            isShowCrosstab.setDisabled(true);  // Deshabilitar (opaco)
	            isShowMovementsAmounts.setDisabled(true);
	        }
	    }
        // Dowload Button
        if (fullPath != null && !fullPath.trim().isEmpty()) {
        	downloadButton.setEnabled(true);
            north.setOpen(false);
        } else {
            downloadButton.setEnabled(false);
            north.setOpen(true);
        }
        //initReportTypeCombo();
    }

	
	@Override
	public void valueChange(ValueChangeEvent event) {
	    
	    Object newValue = event.getNewValue();
	    Object oldValue = event.getOldValue();
	    log.warning("Value changed: " + event.getPropertyName()+" New Value="+newValue+"  Old Value="+oldValue);
	    
	    if ( event.getPropertyName().equals("AD_OrgParent_ID")) {
		    // ========================================================
			// === AD_OrgParent_ID (Organizacion Padre) ===
			// ========================================================
	        Integer parentOrgID = (Integer) event.getNewValue();
	        if (parentOrgID == null) parentOrgID = 0;
	        log.info("Nuevo AD_OrgParent_ID seleccionado: " + parentOrgID);
	        List<KeyNamePair> newOrgList = new ArrayList<>();
	        newOrgList.add(new KeyNamePair(0, "* (Sin Organización)")); 
	        // Determinar la condición del filtro
	        boolean filterByParent = (parentOrgID.intValue() > 0);
	        // La consulta siempre debe buscar organizaciones activas y no-resumen del cliente actual.
	        String sql = 
	              "SELECT DISTINCT ORG.AD_Org_ID, ORG.Name "
	            + "FROM AD_Org ORG ";
	        // Si filtramos por padre, necesitamos unir con las tablas de árbol (AD_TreeNode y AD_Tree)
	        if (filterByParent) {
	            sql += "JOIN AD_TreeNode NODE ON ORG.AD_Org_ID = NODE.Node_ID "
	                + "JOIN AD_Tree TREE ON NODE.AD_Tree_ID = TREE.AD_Tree_ID ";
	        }
	        sql += "WHERE ORG.IsSummary = 'N' "
	            + "AND ORG.AD_Client_ID = ? "; // Parámetro 1: ID del Cliente
	        if (filterByParent) {
	            // Se asume que el árbol 'OO' (Organization Org) es el correcto.
	            sql += "AND TREE.TreeType = 'OO' " 
	                + "AND NODE.Parent_ID = ? "; // Parámetro 2: ID del Padre
	        }
	        sql += "ORDER BY ORG.Name";
	        int paramIndex = 1;
	        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
	            // Asignar Parámetro 1 (AD_Client_ID)
	            pstmt.setInt(paramIndex++, Env.getAD_Client_ID(Env.getCtx()));
	            if (filterByParent) {
	                // Asignar Parámetro 2 (Parent_ID) solo si se usa el filtro
	                pstmt.setInt(paramIndex++, parentOrgID.intValue());
	            }
	            try (ResultSet rs = pstmt.executeQuery()) {
	                while (rs.next()) {
	                    int orgID = rs.getInt(1);
	                    String orgName = rs.getString(2);
	                    newOrgList.add(new KeyNamePair(orgID, orgName));
	                }
	            }
	        } catch (Exception e) {
	            CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al ejecutar consulta SQL para organizaciones hijas.", e);
	        }
	        // Forzar la actualización del componente ZK subyacente ---
	        try {
	            // A. Obtener el componente ZK nativo del wrapper fOrg
	            org.zkoss.zk.ui.Component zkComponent = fOrg.getComponent();
	            // B. Cast explícito a Combobox para manipular el modelo de datos ZK
	            if (zkComponent instanceof org.zkoss.zul.Combobox) {
	                org.zkoss.zul.Combobox combobox = (org.zkoss.zul.Combobox) zkComponent;
	                // C. Crear el modelo de lista ZK, reemplazando el modelo existente (evita duplicación)
	                org.zkoss.zul.ListModelList<org.compiere.util.KeyNamePair> model = 
	                    new org.zkoss.zul.ListModelList<>(newOrgList);
	                combobox.setModel(model); 
	                // D. Limpiar el valor seleccionado anteriormente, ya que podría no existir en la nueva lista
	                fOrg.setValue(null); // Limpiar el valor en el wrapper
	                combobox.setValue(null); // Limpiar el texto visual
	                combobox.setSelectedItem(null); // Limpiar el ítem seleccionado en el Combobox
	                // E. Forzar el refresco visual de ZK para que se pinte la nueva lista
	                combobox.invalidate(); 
	            } else {
	                CLogger.getCLogger(getClass()).log(Level.SEVERE, 
	                    "fOrg.getComponent() no es un Combobox. Tipo encontrado: " + zkComponent.getClass().getName());
	            }
	        } catch (Exception e) {
	            CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al actualizar el modelo de lista del componente ZK.", e);
	        }
	        // LLAMA AL MÉTODO PARA CARGAR Y RESTAURAR EL COMBO PostingType y reportType
	        initPostingTypeCombo();
	        //initReportTypeCombo();
	    } else if (event.getPropertyName().equals("C_Year_ID")) {
		    // ========================================================
			// === C_Year_ID (Año) para Períodos y Fechas ===
			// ========================================================
	        // 1. Obtener el ID
	        Integer yearID = null;
	        if (newValue instanceof org.compiere.util.KeyNamePair) {
	            yearID = ((org.compiere.util.KeyNamePair) newValue).getKey();
	        } else if (newValue instanceof Integer) {
	            yearID = (Integer) newValue;
	        } 
	        
	        log.info("C_Year_ID seleccionado: " + yearID);

	        // 2. LIMPIEZA PREVENTIVA: 
	        // Evita que el componente intente "recordar" el periodo del año anterior
	        if (fPeriod != null) {
	            fPeriod.setValue(null); // Limpia el valor interno de iDempiere
	            if (fPeriod.getComponent() instanceof org.zkoss.zul.Combobox) {
	                org.zkoss.zul.Combobox cb = (org.zkoss.zul.Combobox) fPeriod.getComponent();
	                cb.setRawValue(null); // Limpia el texto visual de ZK
	                cb.setSelectedItem(null);
	            }
	        }

	        // 3. Actualizar estado y recargar
	        this.m_yearID = yearID;
	        setPeriodsFromYear(this.m_yearID);
	    } else if (event.getPropertyName().equals("C_Period_ID")) {
		    // ========================================================
			// === C_Period_ID (Período) para Fechas ===
			// ========================================================
		    // Obtener el ID del Período seleccionado, manejando KeyNamePair/Integer
		    Integer periodID = null;
		    if (newValue instanceof org.compiere.util.KeyNamePair) {
		        periodID = ((org.compiere.util.KeyNamePair) newValue).getKey();
		    } else if (newValue instanceof Integer) {
		        periodID = (Integer) newValue;
		    } 
		    log.info("C_Period_ID seleccionado: " + periodID);
		    m_defaultPeriodID = periodID;
		    // Llama al método de servicio que consulta la DB y setea las fechas.
		    setDatesFromPeriod(periodID, m_reportType_value , m_periodList, m_yearID);
		    
	    } else if (event.getPropertyName().equals("Date")) {
		    // ========================================================
		    // === Lógica de Validación de Fechas ===
		    // ========================================================
	    	// Identificar cuál editor disparó el evento
	        final org.adempiere.webui.editor.WEditor targetEditor = (org.adempiere.webui.editor.WEditor) event.getSource();
	        Timestamp dateFromForm = (Timestamp) dateFrom.getValue();
	        Timestamp dateToForm = (Timestamp) dateTo.getValue();
	        Object periodValue = fPeriod.getValue(); 
	        Integer cPeriod = null;
	        // Lógica para extraer cPeriod de KeyNamePair o Integer (como se corrigió anteriormente)
	        if (periodValue instanceof Integer) {
	            cPeriod = (Integer) periodValue; 
	        } else if (periodValue instanceof org.compiere.util.KeyNamePair) {
	            cPeriod = ((org.compiere.util.KeyNamePair) periodValue).getKey();
	        } 
	        // Las variables temporales para la validación deben ser las fechas del formulario
	        Timestamp tmpDateFrom = dateFromForm;
	        Timestamp tmpDateTo = dateToForm;
	
	        // ----------------------------------------------------
	        // A. Validación: dateFrom debe ser anterior a dateTo
	        // ----------------------------------------------------
	        if (tmpDateFrom != null && tmpDateTo != null) {
	            if (tmpDateFrom.after(tmpDateTo)) {
	            	final Object oldDate = event.getOldValue();
	                final ValueChangeListener listener = this;
	                // Mostrar error modal (usamos la firma de Dialog.ask para Callback)
	                Dialog.ask(
	                    form.getWindowNo(),
	                    Msg.getMsg(Env.getCtx(), "DateRangeError"),
	                    Msg.getMsg(Env.getCtx(), "OK"),
	                    null,
	                    new Callback<Boolean>() {
	                        @Override
	                        public void onCallback(Boolean result) {
	                        	targetEditor.removeValuechangeListener(listener);
	                            targetEditor.setValue(oldDate);
	                            targetEditor.addValueChangeListener(listener);
	                        }
	                    }
	                );
	                return; // Detener la validación si hay error.
	            }
	        }

	        // ----------------------------------------------------
	        // B. Validación: Fechas  
	        // ----------------------------------------------------
			Set<String> periodValidationTypes = Set.of(
					FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE,
					FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_BALANCE,
					FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS,
					FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS_12PERIODS,
					FinancialReportConstants.REPORT_TYPE_ANALITIC_FINANCIAL_STATE
			);
			Set<String> fiscalYearValidationTypes = Set.of(
					FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_TWO_DATES
			// , FinancialReportConstants.OTRO
			);
	        	if (periodValidationTypes.contains(m_reportType_value) || !fiscalYearValidationTypes.contains(m_reportType_value)) {

			        // ----------------------------------------------------
			        // B.1. Validación: Fechas dentro del Periodo Contable
			        // ----------------------------------------------------
			        if (cPeriod != null && cPeriod.intValue() > 0 && tmpDateFrom != null && tmpDateTo != null) {
			            // Obtener el Periodo Contable
			            MPeriod period = new MPeriod(Env.getCtx(), cPeriod.intValue(), null);
			            Timestamp perStartDate = period.getStartDate();
			            Timestamp perEndDate = period.getEndDate();
			            if ((perStartDate.after(tmpDateFrom)) || (tmpDateTo.after(perEndDate)))  {
			                final Object oldDate = event.getOldValue();
			                final ValueChangeListener listener = this;
			                final int windowNo = form.getWindowNo();
			                Dialog.ask(
			                    windowNo,
			                    Msg.translate(Env.getCtx(), "C_Period_ID") + " " + Msg.getMsg(Env.getCtx(), "DateRangeError"),
			                    Msg.getMsg(Env.getCtx(), "OK"),
			                    null,
			                    new Callback<Boolean>() {
			                        @Override
			                        public void onCallback(Boolean result) {
			                            targetEditor.removeValuechangeListener(listener);
			                            targetEditor.setValue(oldDate);
			                            targetEditor.addValueChangeListener(listener);
			                        }
			                    }
			                );
			                return;
			            }
			        }


	        	}
	        	else if (fiscalYearValidationTypes.contains(m_reportType_value)) {

			        // ----------------------------------------------------
			        // B.1. Validación: Fechas dentro del Periodo Fiscal
			        // ----------------------------------------------------
			        if (cPeriod != null && cPeriod.intValue() > 0 && tmpDateFrom != null && tmpDateTo != null) {
			            // Obtener el Periodo Contable
			            MPeriod period = new MPeriod(Env.getCtx(), cPeriod.intValue(), null);
			            // Obtener el Año Fiscal
			            // Fechas del año fiscal
			            Timestamp fiscalStart = AccountUtils.getFiscalYearStart(Env.getCtx(), period.getC_Year_ID(), null);
			            Timestamp fiscalEnd   = AccountUtils.getFiscalYearEnd(Env.getCtx(), period.getC_Year_ID(), null);
			            
			            Timestamp perStartDate = period.getStartDate();
			            if ((perStartDate.before(fiscalStart)) || (tmpDateTo.after(fiscalEnd)))  {
			                final Object oldDate = event.getOldValue();
			                final ValueChangeListener listener = this;
			                final int windowNo = form.getWindowNo();
			                Dialog.ask(
			                    windowNo,
			                    Msg.translate(Env.getCtx(), "C_Year_ID") + " / " +
		                    		Msg.translate(Env.getCtx(), "C_Period_ID") + " " +
		                    		Msg.getMsg(Env.getCtx(), "DateRangeError"),
			                    Msg.getMsg(Env.getCtx(), "OK"),
			                    null,
			                    new Callback<Boolean>() {
			                        @Override
			                        public void onCallback(Boolean result) {
			                            targetEditor.removeValuechangeListener(listener);
			                            targetEditor.setValue(oldDate);
			                            targetEditor.addValueChangeListener(listener);
			                        }
			                    }
			                );
			                return;
			            }
			        }
	

	        	}
	        

	    } else if (event.getPropertyName().equals(X_C_ElementValue.COLUMNNAME_C_ElementValue_ID)) {
		    // ========================================================
		    // === 2. Lógica para fAccount (Validación IsSummary) ===
		    // ========================================================
	    	Integer C_ElementValue_ID = (Integer) event.getNewValue();
	        
	        if (C_ElementValue_ID != null && C_ElementValue_ID.intValue() > 0) {
	            
	            MElementValue ev = new MElementValue(Env.getCtx(), C_ElementValue_ID.intValue(), null);
	            boolean isSummary = ev.isSummary();
	            
	            if (isSummary) {
	                // Si el usuario selecciona una cuenta de resumen (IsSummary='Y')
	                final int windowNo = form.getWindowNo();
	                // --- USANDO EL PATRÓN DIALOG.ASK PARA FORZAR EL MODAL Y CALLBACK ---
	                // Parámetros de ask: (windowNo, message, yes/no label, callback)
	                Dialog.ask(
	                    windowNo, 
	                    Msg.getMsg(Env.getCtx(), "AccountSummaryNotAllowed"), // Mensaje del error
	                    Msg.getMsg(Env.getCtx(), "OK"),  // Etiqueta del botón (Simulamos un botón "Aceptar")
	                    null, // No necesitamos un segundo botón
	                    new Callback<Boolean>() {
	                        @Override
	                        public void onCallback(Boolean result) {
	                            // Este código se ejecuta cuando el usuario hace clic en "OK".
	                            
	                            // 2. REVERTIR: Restaurar el valor que estaba antes del cambio
	                            fAccount.setValue(oldValue);
	                        }
	                    }
	                );
	                
	                log.warning(Msg.getMsg(Env.getCtx(), "AccountSummaryNotAllowed")+Msg.getMsg(Env.getCtx(), "OldValue")+": " + oldValue);
	            }
	        }
	 	} else if (event.getPropertyName().equals("PostingType")) {
	            
	        if (newValue instanceof String) {
	            m_postingType_value = (String) newValue;
	        } else if (newValue instanceof ValueNamePair) {
	            m_postingType_value = ((ValueNamePair) newValue).getValue();
	        }
	    } 
	    
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.warning("actionPerformed="+e.getEventName());
        Boolean showOrg = isShowOrganization.isChecked();

		// Aquí puedes redirigir la lógica
	    if (e.getSource() == isShowOrganization) {
	       
	        // Asumimos que 'isShowCrosstab' es una variable de instancia (VCheck o similar)
	        
	        if (showOrg) {
	            // Caso: isShowOrganization está MARCADO (Mostrar Org en fila)
	            // Deshabilitar (opaco) y desmarcar Crosstab.
	            
	            // 2. Control del Checkbox isShowCrosstab
	            isShowCrosstab.setChecked(false);  // Desmarcar
	            isShowCrosstab.setDisabled(true);  // Deshabilitar (opaco)
	            isShowMovementsAmounts.setDisabled(false);
	        } else {
	            // Caso: isShowOrganization está DESMARCADO
	            // Habilitar y marcar Crosstab por defecto.
	            
	        	isShowCrosstab.setDisabled(false);  // Habilitar
	            isShowCrosstab.setChecked(true);    // Marcar por defecto
	            isShowMovementsAmounts.setDisabled(false);
	        }

	    } 
		
	}

	/**
	 * Lógica a ejecutar cuando el Tipo de Reporte cambia.
	 * @param reportType El valor del nuevo tipo de reporte seleccionado.
	 */
	private void reportTypeChanged(String reportType) {
	    log.warning(reportType);
		// Setting por defecto
	    // OrgParent - Org
	    fOrgParentLabel.setVisible(true);
	    fOrgParent.setVisible(true);
	    fOrgParent.setMandatory(true);
	    fOrgLabel.setVisible(true);
	    fOrg.setVisible(true);
	    fOrg.setMandatory(true);
	    // AcctSchema
	    fAcctSchemaLabel.setVisible(true);
	    fAcctSchema.setVisible(true);
	    // Posting Type
	    fPostingTypeLabel.setVisible(true);
	    fPostingType.setVisible(true);
	    fPostingType.setMandatory(true);
	    // Period Date
	    dateRangeLabel.setVisible(true);
	    fYearLabel.setVisible(true);
	    fYear.setVisible(true);
	    fPeriodLabel.setVisible(true);
	    fPeriod.setVisible(true);
	    fPeriod.setReadWrite(true);
	    fPeriod.setMandatory(true);
	    dateFromLabel.setVisible(true);
	    dateFrom.setVisible(true);
	    dateFrom.setReadWrite(false);
	    dateFrom.setMandatory(false);
	    dateToLabel.setVisible(true);
	    dateTo.setVisible(true);
	    dateTo.setMandatory(false);
	    dateTo.setReadWrite(false);
	    // Account
	    fAccount.setMandatory(false);
	    fAccount.setVisible(false);
	    fAccountLabel.setVisible(false);
	    // Checks
        isShowSummaryElements.setChecked(true);
        isShowSummaryElements.setVisible(true);
        isPositiveBalance.setVisible(false);
        isPositiveBalance.setChecked(false);
	    isShowOrganization.setVisible(true);
	    isShowCrosstab.setVisible(true);
	    isShowZERO.setVisible(true);
	    isShowMovementsAmounts.setChecked(true);
	    isShowMovementsAmounts.setVisible(true);
	    
	    // === LÓGICA ESPECÍFICA POR TIPO DE REPORTE ===
	    if (FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE.equals(reportType)) {
	        // Lógica específica para Balance de Comprobación de un período
	        // Ocultar el campo DateTo o activar DateFrom
	        dateFrom.setMandatory(true);
	        dateFrom.setReadWrite(true);
	        dateTo.setMandatory(true);
	        dateTo.setReadWrite(true);
	    	isPositiveBalance.setVisible(true);
	    	isPositiveBalance.setChecked(true);
	        
	    } else if (FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_TWO_DATES.equals(reportType)) {
	        // Lógica específica para Balance de Comprobación entre dos fechas
	        // Asegurar que los campos DateFrom y DateTo estén visibles/activos
	    	// Period Date
		    fPeriodLabel.setVisible(false);
		    fPeriod.setVisible(false);
	        dateFrom.setMandatory(true);
	        dateFrom.setReadWrite(true);
	        dateTo.setMandatory(true);
	        dateTo.setReadWrite(true);
	    	isPositiveBalance.setVisible(true);
	    	isPositiveBalance.setChecked(true);
	    	
	    } else if (FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_BALANCE.equals(reportType)) {
	        // SI se muestra la cuenta
	        fAccountLabel.setVisible(true);
	    	fAccount.setMandatory(true);
	        fAccount.setVisible(true);
	    	isPositiveBalance.setVisible(true);
	    	isPositiveBalance.setChecked(true);
	        dateFrom.setMandatory(true);
	        dateFrom.setReadWrite(true);
	        dateTo.setMandatory(true);
	        dateTo.setReadWrite(true);
	    } else if (FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS.equals(reportType)) {
	    	isPositiveBalance.setVisible(true);
	    	isPositiveBalance.setChecked(true);
	        dateFrom.setMandatory(true);
	        dateFrom.setReadWrite(true);
	        dateTo.setMandatory(true);
	        dateTo.setReadWrite(true);

	    } else if (FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS_12PERIODS.equals(reportType)) {
	    	isPositiveBalance.setVisible(true);
	    	isPositiveBalance.setChecked(true);
	        dateFrom.setMandatory(true);
	        dateFrom.setReadWrite(false);
	        dateTo.setMandatory(true);
	        dateTo.setReadWrite(false);
		    isShowCrosstab.setVisible(false);
		    isShowCrosstab.setChecked(false);
		    fPeriod.setReadWrite(false);
		    setPeriodsFromYear(m_yearID);
		    
	    } else if (FinancialReportConstants.REPORT_TYPE_ANALITIC_FINANCIAL_STATE.equals(reportType)) {
	        dateFrom.setMandatory(true);
	        dateFrom.setReadWrite(true);
	        dateTo.setMandatory(true);
	        dateTo.setReadWrite(true);

	    } else if (FinancialReportConstants.REPORT_TYPE_ACCOUNT_ELEMENTS.equals(reportType)) {
	        
	        // OrgParent - Org
	        fOrgParentLabel.setVisible(false);
	        fOrgParent.setVisible(false);
	        fOrgParent.setMandatory(false);
	        fOrgLabel.setVisible(false);
	        fOrg.setVisible(false);
	        fOrg.setMandatory(false);
	        // Posting Type
	        fPostingTypeLabel.setVisible(false);
	        fPostingType.setVisible(false);
	        fPostingType.setMandatory(false);
	        // Period Date
	        fYearLabel.setVisible(false);
		    fYear.setVisible(false);
	        fPeriodLabel.setVisible(false);
	        fPeriod.setVisible(false);
	        fPeriod.setMandatory(false);
	        dateRangeLabel.setVisible(false);
	        dateFromLabel.setVisible(false);
	        dateFrom.setVisible(false);
	        dateFrom.setReadWrite(false);
	        dateFrom.setMandatory(false);
	        dateToLabel.setVisible(false);
	        dateTo.setVisible(false);
	        dateTo.setMandatory(false);
	        dateTo.setReadWrite(false);
	        // Checks
	        isShowSummaryElements.setVisible(false);
	        isShowOrganization.setVisible(false);
	        isShowCrosstab.setVisible(false);
	        isShowZERO.setVisible(false);
	        isShowMovementsAmounts.setVisible(false);
	    }

	    // Set period and dates
	    if (m_defaultPeriodID != null && m_defaultPeriodID.intValue() > 0 && !m_periodList.isEmpty()) {
            // Si hay un período válido cargado inicialmente, usa SUS fechas.
            setDatesFromPeriod(m_defaultPeriodID, m_reportType_value , m_periodList, m_yearID);
        
        }
	}

	private void closeReportForm() {
	    Dialog.ask(m_WindowNo, "ExitApplication?", new Callback<Boolean>() {
	        @Override
	        public void onCallback(Boolean result) {
	            if (Boolean.TRUE.equals(result)) {

	                dispose(); 

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
        m_reportType_value = FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE;
        initReportTypeCombo();
        initPostingTypeCombo();
        reportTypeChanged(m_reportType_value);
        dynInit();
	}
	  
	private ReportMetadata runServerProcessForm() {
		String errorMsg = "";
		ReportMetadata reportMetadata = null;
		int AD_Client_ID = (fClient.getValue() instanceof KeyNamePair) 
                ? ((KeyNamePair) fClient.getValue()).getKey()
                : (fClient.getValue() != null && fClient.getValue() instanceof Integer)
                    ? (Integer) fClient.getValue()
                    : Env.getAD_Client_ID(Env.getCtx());
		// Obtener el ID del Esquema Contable (C_AcctSchema_ID)
		int C_AcctSchema_ID = 0;
		if (fAcctSchema.getValue() instanceof KeyNamePair) {
		    C_AcctSchema_ID = ((KeyNamePair) fAcctSchema.getValue()).getKey();
		} else if (fAcctSchema.getValue() instanceof Integer) {
		    C_AcctSchema_ID = (Integer) fAcctSchema.getValue();
		} 
		// AD_Org_ID 
		int AD_Org_ID = 0;
		if (fOrg.getValue() instanceof KeyNamePair) {
		    AD_Org_ID = ((KeyNamePair) fOrg.getValue()).getKey();
		} else if (fOrg.getValue() instanceof Integer) {
		    AD_Org_ID = (Integer) fOrg.getValue();
		} 
		// OrgParent_ID 
		int AD_OrgParent_ID = 0;
		if (fOrgParent.getValue() instanceof KeyNamePair) {
		    AD_OrgParent_ID = ((KeyNamePair) fOrgParent.getValue()).getKey();
		} else if (fOrgParent.getValue() instanceof Integer) {
		    AD_OrgParent_ID = (Integer) fOrgParent.getValue();
		}
		// Crear un ProcessInfo simulado para lockUI/unlockUI
	    ProcessInfo pi = new ProcessInfo(Msg.getMsg(Env.getCtx(), "Processing"), 0);
	    this.m_Pi = pi;
		// Recolección de Parámetros del Formulario
        Map<String, Object> parameters = new HashMap<>();
        // Report Type
        String reportTypeKey = m_reportType_value;
        //initReportTypeCombo();
        // Object PostingType =
        // Restaurar el modelo ZK (usa m_postingType_value para re-seleccionar)
        initPostingTypeCombo(); 
        // Leer el valor persistido para el reporte
        String PostingType = m_postingType_value;
        // C_Year_ID
        Integer C_Year_ID = null;
        Object objC_Year_ID = fYear.getValue();
        if (objC_Year_ID instanceof KeyNamePair) {
            // Si el valor es un KeyNamePair (caso más probable), extrae el Key (ID)
        	C_Year_ID = ((KeyNamePair) objC_Year_ID).getKey();
        } else if (objC_Year_ID instanceof Integer) {
            // Si ya es un Integer (caso de un campo DynInt simple o valor ya procesado)
        	C_Year_ID = (Integer) objC_Year_ID;
        }
        // C_Period_ID
        Integer C_Period_ID = null;
        Object objC_Period_ID = fPeriod.getValue();
        if (objC_Period_ID instanceof KeyNamePair) {
            // Si el valor es un KeyNamePair (caso más probable), extrae el Key (ID)
        	C_Period_ID = ((KeyNamePair) objC_Period_ID).getKey();
        } else if (objC_Period_ID instanceof Integer) {
            // Si ya es un Integer (caso de un campo DynInt simple o valor ya procesado)
        	C_Period_ID = (Integer) objC_Period_ID;
        }
        // C_ElementValue_ID
        int C_ElementValue_ID = 0; // Inicializar a 0 (o un valor seguro)
        Object accountValue = fAccount.getValue();
        if (accountValue != null) {
            if (accountValue instanceof KeyNamePair) {
                C_ElementValue_ID = ((KeyNamePair) accountValue).getKey();
            } else if (accountValue instanceof Integer) {
                C_ElementValue_ID = (Integer) accountValue;
            }
        }
        // Obtener el valor booleano (true/false)
        Boolean showZero = isShowZERO.isChecked();
        Boolean showOrg = isShowOrganization.isChecked();
        Boolean showCrosstab = isShowCrosstab.isChecked();
        Boolean ShowMovementsAmounts = isShowMovementsAmounts.isChecked();
        Boolean ShowSummaryElements = isShowSummaryElements.isChecked();
        Boolean ShowSubTotal = isShowSubTotal.isChecked();
        Boolean PositiveBalance = isPositiveBalance.isChecked();
        // Strings
        String isShowZERO_String = (showZero != null && showZero) ? "Y" : "N";
        String isShowOrganization_String = (showOrg != null && showOrg) ? "Y" : "N";
        String isShowCrosstab_String = (showCrosstab != null && showCrosstab) ? "Y" : "N";
        String isShowMovementsAmounts_String = (ShowMovementsAmounts != null && ShowMovementsAmounts) ? "Y" : "N";
        String isShowSummaryElements_String  = (ShowSummaryElements != null && ShowSummaryElements) ? "Y" : "N";
        String isShowSubTotal_String 		= (ShowSubTotal != null && ShowSubTotal) ? "Y" : "N";
        String isPositiveBalance_String 	= (PositiveBalance != null && PositiveBalance) ? "Y" : "N";
        // Parámetros Requeridos
        parameters.put("AD_Client_ID", AD_Client_ID);
        parameters.put("AD_OrgParent_ID", AD_OrgParent_ID);
        parameters.put("AD_Org_ID", AD_Org_ID);
        parameters.put("C_AcctSchema_ID", C_AcctSchema_ID);
        parameters.put("C_ElementValue_ID", C_ElementValue_ID);
        parameters.put("PostingType", (String) PostingType);
        parameters.put("C_Year_ID", C_Year_ID);
        parameters.put("C_Period_ID", C_Period_ID);
        parameters.put("DateFrom", (Timestamp) dateFrom.getValue());
        parameters.put("DateTo", (Timestamp) dateTo.getValue());
        parameters.put("isShowZERO", (String) isShowZERO_String);
        parameters.put("isShowOrganization", (String) isShowOrganization_String);
        parameters.put("isShowCrosstab", (String) isShowCrosstab_String);
        parameters.put("isShowMovementsAmounts", (String) isShowMovementsAmounts_String);
        parameters.put("isShowSummaryElements", (String) isShowSummaryElements_String);
        parameters.put("isShowSubTotal", (String) isShowSubTotal_String);
        parameters.put("isPositiveBalance", (String) isPositiveBalance_String);
        parameters.put("ReportTitle", (String) m_reportType_name);
        // Resto de parametros
        // Selección de la Estrategia (Generador)
        IReportGenerator generador = ReportGeneratorFactory.getGenerator(reportTypeKey);
        
        if (generador == null) {
        	errorMsg = "Tipo de reporte no soportado.\r\n"+m_reportType_name;
            //Dialog.error(form.getWindowNo(), errorMsg);
        	pi.setError(true);
            pi.setSummary(errorMsg); 
        	unlockUI(pi);
        	return reportMetadata;
        }
        // Generar Reporte
		try {
			// 🔒 Bloquear la UI y deshabilitar campos
	        lockUI(pi);
	        // 1️⃣ Validar parámetros ANTES de generar el reporte
	        String validationMsg = validateReportParameters(reportTypeKey, parameters);
	        if (validationMsg != null) {
	        	//Dialog.error(form.getWindowNo(), validationMsg);
	        	pi.setError(true);
	            pi.setSummary(validationMsg); 
	        	unlockUI(pi);
	        	return null;
	        } 
	        // 2️⃣ Si todo está bien, generar el reporte
	        // Llama al método generate(), que internamente llama a generateReportContent()
	        reportMetadata = generador.generate(Env.getCtx(), form.getWindowNo(), parameters);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Fallo al generar el archivo XLSX.", e);
            //Dialog.error(form.getWindowNo(), "Error de E/S al generar el reporte.");
			pi.setSummary(Msg.getMsg(Env.getCtx(), "Error") + 
					Msg.getMsg(Env.getCtx(), "FileXLSX") +" "+  
					e.getMessage());
	        pi.setError(true);
		} finally {
			// 🔓 Desbloquear la UI y re-habilitar campos, incluso si hubo un error
	        unlockUI(pi);
		}
		return reportMetadata;
	}

    private void previewReportWeb(String fullPath, String[] headers, int[] widths, int headerRowCount, int maxVisibleRow) throws Exception {
        
    	log.warning("El archivo en Form:"+fullPath);
        // Limpio el centro
        center.getChildren().clear();
//        ExcelViewerPanel viewer = new ExcelViewerPanel(fullPath, headers, widths, headerRowCount, maxVisibleRows);
//		center.appendChild(viewer);
        // Usar la Fábrica para obtener el visor
        // CONVERSIÓN DE STRING A FILE
        File reportFile = new File(fullPath);
        Div viewer = ExcelViewerFactory.createViewer(reportFile, headers, widths, headerRowCount, maxVisibleRow);
        
        // Mostrar el visor en el centro
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
     * Inicializa o restaura el componente fReportType cargando el modelo ZK
     * y forzando el valor seleccionado (usa m_reportType_value).
     */
    private void initReportTypeCombo() {
        if (fReportType == null) return;

        // 1. Obtener lista ya ordenada desde el nuevo método
        List<ValueNamePair> reportTypes = getSortedReportTypes();
        
        // 2. Determinar valor anterior o valor por defecto
        String valueToSet = (m_reportType_value == null || m_reportType_value.isEmpty())
                            ? FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE
                            : m_reportType_value;
        
        try {
            org.zkoss.zul.Combobox comboboxReport = (org.zkoss.zul.Combobox) fReportType;
            
            // Asignar lista al modelo del combobox
            org.zkoss.zul.ListModelList<ValueNamePair> modelReport = new org.zkoss.zul.ListModelList<>(reportTypes);
            comboboxReport.setModel(modelReport);
            
            // Seleccionar el item correspondiente
            ValueNamePair selectedVNP = null;
            for (ValueNamePair vnp : reportTypes) {
                if (vnp.getValue().equals(valueToSet)) {
                    selectedVNP = vnp;
                    break;
                }
            }

            if (selectedVNP != null) {
                fReportType.setValue(valueToSet);         
                comboboxReport.setValue(selectedVNP.getName());
                m_reportType_value = selectedVNP.getValue();
                m_reportType_name  = selectedVNP.getName();
            } else {
                fReportType.setValue(valueToSet);
                m_reportType_value = valueToSet;
                m_reportType_name = Msg.getMsg(Env.getCtx(), "Trial Balance Report");
            }

        } catch (Exception e) {
            CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al inicializar/restaurar fReportType.", e);
        }
    }
    
    /**
	 * Inicializa o restaura el componente fPostingType cargando el modelo ZK
	 * y forzando el valor seleccionado (usa 'A' como predeterminado).
	 */
	private void initPostingTypeCombo() {
	    // 1. Cargar la Lista de Valores
	    final int AD_REFERENCE_ID = X_Fact_Acct.POSTINGTYPE_AD_Reference_ID; // 125
	    ValueNamePair[] postingTypesArray = MRefList.getList(Env.getCtx(), AD_REFERENCE_ID, false);
	    List<ValueNamePair> postingTypes = java.util.Arrays.asList(postingTypesArray);
	
	    // 2. Determinar el valor a establecer (actual o por defecto)
	    String defaultValue = Env.getContext(Env.getCtx(), "$PostingType");
	    if (defaultValue == null || defaultValue.isEmpty()) {
	        defaultValue = "A"; 
	    }
	    
	    // Obtener el valor actualmente seleccionado (si existe)
	    // Cuando se llama desde ValueChange, fPostingType.getValue() tiene el valor.
	    Object currentValueObj = fPostingType.getValue();
	    String valueToSet = defaultValue; // Inicializar con el valor por defecto
	
	    if (currentValueObj != null) {
	        if (currentValueObj instanceof String) {
	            // Caso común: el valor interno es una String (ej: antes de la primera selección)
	            valueToSet = (String) currentValueObj;
	        } else if (currentValueObj instanceof ValueNamePair) {
	            // Caso de Lookup: después de la selección, devuelve el objeto completo
	            valueToSet = ((ValueNamePair) currentValueObj).getValue(); // <-- Extracción correcta del ID (Value)
	        } else {
	            // Fallback: si devuelve otro tipo inesperado, usamos el valor por defecto.
	            valueToSet = defaultValue;
	        }
	    }
	
	    // 3. Asignar la lista al Combobox ZK y forzar el valor
	    try {
	        org.zkoss.zul.Combobox combobox = (org.zkoss.zul.Combobox) fPostingType.getComponent();
	        
	        // REASIGNAR EL MODELO: CRÍTICO para que no se pierda.
	        org.zkoss.zul.ListModelList<ValueNamePair> model = new org.zkoss.zul.ListModelList<>(postingTypes);
	        combobox.setModel(model);
	        
	        // 4. Buscar el label y establecer el valor visible e interno.
	        String displayLabel = valueToSet; 
	        for (ValueNamePair vnp : postingTypes) {
	            if (vnp.getValue().equals(valueToSet)) {
	                displayLabel = vnp.getName(); 
	                break;
	            }
	        }
	
	        // Establecer la clave interna (el valor real)
	        fPostingType.setValue(valueToSet);
	        
	        // Forzar la visualización del label en el combo ZK
	        combobox.setValue(displayLabel);
	        
	    } catch (Exception e) {
	        CLogger.getCLogger(getClass()).log(Level.SEVERE, "Fallo al inicializar/restaurar fPostingType.", e);
	    }
	    // Tooltip
	    ((org.zkoss.zul.Combobox) fPostingType.getComponent()).setTooltiptext(FinancialReportConstants.getReferenceToolTip(Env.getCtx()));
	
	}

	/**
     * Genera y devuelve una lista ordenada alfabéticamente de ValueNamePair
     * para el tipo de reporte.
     * A partir de AD_Reference
     */
	private List<ValueNamePair> getSortedReportTypes() {

	    // Obtener lista desde MRefList
	    ValueNamePair[] reportTypesArray =
	        MRefList.getList(Env.getCtx(), AD_Reference_ID_ReportType, false);
	    List<ValueNamePair> reportTypes = new ArrayList<>(Arrays.asList(reportTypesArray));

	    // Map de prioridad para los valores conocidos
	    Map<String, Integer> priority = Map.of(
	        "TRB", 1, // Trial Balance One Period
	        "TRD", 2, // Trial Balance between two dates
	        "BAL", 3, // State Financial Balance
	        "GOP", 4, // State Financial Integral Results
	        "G12", 5, // State Financial Integral Results
	        "ANB", 6, // Analitic Financial State
	        "ACE", 98  // Account Elements catalog
	    );

	    // Ordenar según prioridad, los desconocidos van al final
	    reportTypes.sort(Comparator.comparingInt(v ->
	        priority.getOrDefault(v.getValue(), 99)
	    ));

	    return reportTypes;
	}
    
    
    /**
     * Valida los parámetros requeridos según el tipo de reporte.
     * 
     * @param reportTypeKey  Clave del tipo de reporte (TRB, TRD, BAL, etc.)
     * @param parameters     Mapa con los parámetros suministrados
     * @return               null si todo OK, o String con los errores encontrados
     */
	private String validateReportParameters(String reportTypeKey, Map<String, Object> parameters) {
	    List<String> missingParams = new ArrayList<>();

	    // 1. Parámetros básicos (Siempre obligatorios)
	    validate(parameters, missingParams, "AD_Client_ID");
	    validate(parameters, missingParams, "C_AcctSchema_ID");

	    // 2. Extraer valores para lógica condicional
	    Object periodObj = parameters.get("C_Period_ID");
	    Object dateFromObj = parameters.get("DateFrom");
	    Object dateToObj = parameters.get("DateTo");

	    // Consideramos que tiene periodo si es un Integer y no es 0
	    // (Si es -1 lo tomamos como "tiene valor")
	    boolean hasPeriod = (periodObj instanceof Integer && (Integer) periodObj != 0);
	    boolean hasDates = (dateFromObj != null && dateToObj != null);

	    // 3. Validación de Organización
	    if (!reportTypeKey.equals(FinancialReportConstants.REPORT_TYPE_ACCOUNT_ELEMENTS)) {
	        validate(parameters, missingParams, "AD_Org_ID");
	        validate(parameters, missingParams, "AD_OrgParent_ID");
	        String orgValidation = validateOrgAndOrgParent(parameters);
	        if (orgValidation != null) return orgValidation;
	    }

	    // 4. Lógica por tipo de Reporte
	    switch (reportTypeKey) {
	        case FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE:
	            // REGLA: Debe tener Periodo O tener Fechas (Caso "Todo el año")
	            if (!hasPeriod && !hasDates) {
	                missingParams.add("C_Period_ID o Rango de Fechas");
	            }
	            validate(parameters, missingParams, "PostingType");
	            break;

	        case FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_TWO_DATES:
	        case FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_BALANCE:
	        case FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS:
	        case FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS_12PERIODS:
	        case FinancialReportConstants.REPORT_TYPE_ANALITIC_FINANCIAL_STATE:
	            
	            // En estos reportes las fechas son el motor principal.
	            // Si no hay periodo Y no hay fechas, falta algo.
	            if (!hasPeriod && !hasDates) {
	                missingParams.add("C_Period_ID");
	            }
	            
	            // Forzar validación de fechas (ya que estos reportes las requieren siempre)
	            validate(parameters, missingParams, "DateFrom");
	            validate(parameters, missingParams, "DateTo");

	            if (reportTypeKey.equals(FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_TWO_DATES)) {
	                validate(parameters, missingParams, "PostingType");
	            }
	            break;

	        case FinancialReportConstants.REPORT_TYPE_ACCOUNT_ELEMENTS:
	            break;

	        default:
	            return "Tipo de reporte desconocido: " + reportTypeKey;
	    }

	    if (!missingParams.isEmpty()) {
	        return "Faltan los siguientes parámetros: " + String.join(", ", missingParams);
	    }

	    return null; 
	}
	
    /**
     * Helper: Verifica si un parámetro existe y no es null.
     */
    private void validate(Map<String, Object> params, List<String> errors, String key) {
        if (!params.containsKey(key) || params.get(key) == null) {
            errors.add(key);
        }
    }
    
    private String validateOrgAndOrgParent(Map<String, Object> parameters) {
        Integer org = getInt(parameters.get("AD_Org_ID"));
        Integer orgParent = getInt(parameters.get("AD_OrgParent_ID"));

        // Si ambos son 0 -> error
        if ((org == null || org == 0) && (orgParent == null || orgParent == 0)) {
            return "Debe seleccionar al menos una Organización (AD_Org_ID o AD_OrgParent_ID).";
        }
        return null; // OK
    }

    private Integer getInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer) return (Integer) value;
        try { return Integer.parseInt(value.toString()); }
        catch (Exception e) { return 0; }
    }


    /**
     * getPeriodsFromYear
     * @param yearID
     * @return
     */
    private  List<KeyNamePair> getPeriodsFromYear(Integer yearID)
    {
        CLogger log = CLogger.getCLogger(getClass());
        log.info("setPeriodsFromYear - C_Year_ID=" + yearID);
        // Lista de períodos
        List<KeyNamePair> periodList = new ArrayList<>();
        
        // --- NUEVO: Opción "Todo el Año" ---
        // Usamos -1 para identificarlo en los procesos posteriores
        periodList.add(new KeyNamePair(-1, m_allyearPeriods));
        
        StringBuilder sqlPeriods = new StringBuilder(
                "SELECT C_Period_ID, Name "
              + "FROM C_Period "
              + "WHERE IsActive='Y' "
              + "AND AD_Client_ID IN (0, ?) "
          );
          if (yearID != null) {
              sqlPeriods.append("AND C_Year_ID = ? ");
          }
          sqlPeriods.append("ORDER BY StartDate DESC");

          try (PreparedStatement pstmt = DB.prepareStatement(sqlPeriods.toString(), null)) {
              int paramIndex = 1;
              pstmt.setInt(paramIndex++, Env.getAD_Client_ID(Env.getCtx()));
              if (yearID != null) {
                  pstmt.setInt(paramIndex++, yearID);
              }
              try (ResultSet rs = pstmt.executeQuery()) {
                  while (rs.next()) {
                      periodList.add(new KeyNamePair(rs.getInt("C_Period_ID"), rs.getString("Name")));
                  }
              }
          } catch (Exception e) {
              log.log(Level.SEVERE, "Error cargando períodos", e);
          }
          return periodList;
    }

    /**
     * setPeriodsFromYear
     * @param yearID
     */
    private void setPeriodsFromYear(Integer yearID) {
        List<KeyNamePair> periodList = getPeriodsFromYear(yearID);
        
        if (fPeriod.getComponent() instanceof org.zkoss.zul.Combobox) {
            org.zkoss.zul.Combobox combobox = (org.zkoss.zul.Combobox) fPeriod.getComponent();
            
            // 1. Limpieza total
            combobox.setSelectedIndex(-1);
            combobox.setValue(null);
            
            // 2. Crear y asignar el modelo
            org.zkoss.zul.ListModelList<KeyNamePair> model = new org.zkoss.zul.ListModelList<>(periodList);
            combobox.setModel(model);
            
            // 3. Trabajar sobre la lista de datos, no sobre el componente visual
            if (periodList != null && !periodList.isEmpty()) {
                KeyNamePair allYearKNP = periodList.get(0); // El ID -1
                
                // 4. Intentar selección visual (con manejo de error silencioso)
                try {
                    model.addToSelection(allYearKNP);
                    combobox.setSelectedIndex(0);
                    combobox.setValue(allYearKNP.getName());
                } catch (Exception e) {
                    // Si ZK aún no renderiza, al menos forzamos el texto
                    combobox.setText(allYearKNP.getName());
                }
                
                // 5. ACTUALIZACIÓN DE DATOS (Crucial para las fechas)
                // Sincronizamos el valor interno de iDempiere
                fPeriod.setValue(allYearKNP.getKey());
                
                // Llamamos SIEMPRE a setDates independientemente de si el combo visual respondió
                log.info("Actualizando fechas para el año: " + yearID);
                setDatesFromPeriod(allYearKNP.getKey(), m_reportType_value, periodList, yearID);
            }
        }
    }

    /**
     * setDatesFromPeriod:
     * Consulta la base de datos para obtener las fechas de inicio y fin
     * de un C_Period_ID dado y las asigna a dateFrom y dateTo.
     * @param periodID El ID del período seleccionado.
     * @param reportType 
     */
 
    private void setDatesFromPeriod(Integer periodID, String reportType, List<KeyNamePair> periodList, Integer yearID) {
        final ValueChangeListener listener = this;
        if (dateFrom != null) dateFrom.removeValuechangeListener(listener);
        if (dateTo != null) dateTo.removeValuechangeListener(listener);

        try {
            Timestamp dbDateFromSel = null;
            Timestamp dbDateToSel = null;

            // CASO A: TODO EL AÑO (ID = -1)
            if (periodID != null && periodID == -1) {
                if (yearID != null && yearID > 0) {
                    // Buscamos todos los periodos del año ordenados por fecha
                    List<MPeriod> periods = new Query(Env.getCtx(), MPeriod.Table_Name, "C_Year_ID=? AND IsActive='Y'", null)
                                                .setParameters(yearID)
                                                .setOrderBy("StartDate ASC")
                                                .list();
                    
                    if (periods != null && !periods.isEmpty()) {
                        dbDateFromSel = periods.get(0).getStartDate();
                        dbDateToSel = periods.get(periods.size() - 1).getEndDate();
                    }
                }
            } 
            // CASO B: PERÍODO ESPECÍFICO (ID > 0)
            else if (periodID != null && periodID > 0) {
                MPeriod per = new MPeriod(Env.getCtx(), periodID, null);
                if (per.get_ID() > 0) {
                    dbDateFromSel = per.getStartDate();
                    dbDateToSel = per.getEndDate();
                }
            }

            // Asignación a los componentes de la interfaz
            if (dbDateFromSel != null && dbDateToSel != null) {
                dateFrom.setValue(dbDateFromSel);
                dateTo.setValue(dbDateToSel);
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error seteando fechas", e);
        } finally {
            if (dateFrom != null) dateFrom.addValueChangeListener(listener);
            if (dateTo != null) dateTo.addValueChangeListener(listener);
        }
    }
    

    public void dispose() {
    	
    	SessionManager.getAppDesktop().closeActiveWindow();
    	
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

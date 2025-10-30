package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.Dialog;
import org.amerp.reports.xlsx.constants.FinancialReportConstants;
import org.amerp.reports.xlsx.generator.IReportGenerator;
import org.amerp.reports.xlsx.generator.ReportGeneratorFactory;
import org.amerp.reports.xlsx.generator.ReportMetadata;
import org.amerp.reports.xlsx.util.AccountUtils;
import org.amerp.reports.xlsx.util.MsgUtils;
import org.compiere.model.MColumn;
import org.compiere.model.MElementValue;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MPeriod;
import org.compiere.model.MRefList;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;


public class FinancialReports_TreeOrg_Form  implements IFormController, EventListener<Event>, IProcessUI , ValueChangeListener{

	private static final CLogger log = CLogger.getCLogger(FinancialReports_TreeOrg_Form.class);

	/** UI form instance */
	private CustomForm form = new CustomForm();
	// Column Names Valid AD Names
	private int m_WindowNo = 0;
	// Variable para guardar valores seleccionado por el usuario.
    private String m_postingType_value = "A"; // valor por defecto
    private String m_reportType_value = FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_ONE_PERIOD;
    private int maxVisibleRows =5000;
    /** Default constructor */
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
    private WTableDirEditor fReportType;
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
    private Label fPeriodLabel = new Label();
    private WTableDirEditor fPeriod;
    private Label dateFromLabel = new Label();
    private WDateEditor dateFrom =new WDateEditor();
    private Label dateToLabel = new Label();
    private WDateEditor dateTo = new WDateEditor();
    private Label fAccountLabel = new Label();
    private WSearchEditor fAccount;
    private Checkbox isShowOrganization = new Checkbox();
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
        
        // Propiedades de north
        north.setVisible(true);
        north.setHeight("450px");
        north.setCollapsible(true);
        north.setOpen(true);
        north.setTitle(Msg.getMsg(Env.getCtx(), "SearchCriteria"));
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
		final int AD_Reference_ID_ReportType = FinancialReportConstants.AD_REFERENCE_REPORT_TYPE; 
		MLookup lookupReportType = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Reference_ID_ReportType,
				DisplayType.List);
		fReportType = new WTableDirEditor("ReportType", true, false, true, lookupReportType);
		// Inicialización de fReportType: Carga modelo y establece el valor inicial ("TRB")
	    initReportTypeCombo(); 
	    // Agregar Listener (Sólo una vez)
	    fReportType.addValueChangeListener(this);
	    // ... (Resto de la lógica, incluyendo PostingType y Tooltip) ...
	    ((org.zkoss.zul.Combobox) fReportType.getComponent()).setTooltiptext(FinancialReportConstants.getReferenceToolTip(Env.getCtx()));
		
	    // =======================================================
		// === Campo AD_Client_ID fClient ======
		// =======================================================
		final String COLUMN_NAME_CLIENT = "AD_Client_ID";
		int AD_Column_ID_Client = MColumn.getColumn_ID("AD_Client", COLUMN_NAME_CLIENT);
		MLookup lookupClient = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_Client, DisplayType.TableDir);
		fClient = new WTableDirEditor(COLUMN_NAME_CLIENT, true, false, true, lookupClient);

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

		// ============================================================
        // fAcctSchema
		// ============================================================
        int AD_Column_ID_AcctSchema = MColumn.getColumn_ID("C_AcctSchema", "C_AcctSchema_ID");
        MLookup lookupAcctSchema = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_AcctSchema, DisplayType.TableDir);
        fAcctSchema = new WTableDirEditor("C_AcctSchema_ID", true, false, true, lookupAcctSchema);

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
        // fPeriod
        // ============================================================
        int AD_Column_ID_Period = MColumn.getColumn_ID("C_Period", "C_Period_ID");
        MLookup lookupPeriod = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID_Period, DisplayType.TableDir);
        fPeriod = new WTableDirEditor("C_Period_ID", true, false, true, lookupPeriod);
        
        // =======================================================
        // === INSTANCIACIÓN DE DATEFROM Y DATETO  ===
        // =======================================================
		// ======= DateFrom DateTo =======

		
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
        dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
        dateToLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
        dateToLabel.setText(Msg.translate(Env.getCtx(),"DateTo"));
        // Cuenta de Resultado
        String IncomeSummary_Acct_Element_Name = "IncomeSummary_Acct";
        fAccountLabel.setText(Msg.getElement(Env.getCtx(), IncomeSummary_Acct_Element_Name));
        isShowOrganization.setText(Msg.translate(Env.getCtx(), "isShowOrganization"));
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
        Rows rows = new Rows();
        
        Row row = new Row();
        fReportType.getComponent().setWidth("400px");
        row.appendChild(fReportTypeLabel);
        row.appendChild(fReportType.getComponent());
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fClientLabel);
        row.appendChild(fClient.getComponent());
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fOrgParentLabel);
        row.appendChild(fOrgParent.getComponent());
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fOrgLabel);
        row.appendChild(fOrg.getComponent());
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fAcctSchemaLabel);
        row.appendChild(fAcctSchema.getComponent());
        rows.appendChild(row);

        row = new Row();
        row.appendChild(fPostingTypeLabel);
        row.appendChild(fPostingType.getComponent());
        rows.appendChild(row);
        
        // Fila Periodo y Fechas
        row = new Row();
        fPeriod.getComponent().setWidth("180px"); // Ancho fijo para el Periodo
        dateFrom.getComponent().setWidth("120px"); // Ancho fijo para la fecha
        dateTo.getComponent().setWidth("120px"); // Ancho fijo para la fecha
        // --- Celda 1: Período (Label + Component) ---
        org.zkoss.zul.Hbox periodBox = new org.zkoss.zul.Hbox();
        periodBox.setSpacing("0px"); // Resetear el espaciado global si usas Spacer
        periodBox.appendChild(fPeriodLabel);
        // Añadir un separador invisible (ej. 40 píxeles de ancho)
        org.zkoss.zul.Space spacer = new org.zkoss.zul.Space();
        spacer.setWidth("120px"); // Define la separación deseada
        periodBox.appendChild(spacer);
        // Añadir el Componente
        periodBox.appendChild(fPeriod.getComponent());
        row.appendChild(periodBox);
        // --- Celda 2: Rango de Fechas (Desde/Hasta) ---
        org.zkoss.zul.Hbox dateRangeBox = new org.zkoss.zul.Hbox();
        dateRangeBox.setSpacing("10px"); // Aumentamos el espacio para claridad
        // Agrupamos: [Desde] [dateFrom.getComponent()] [Hasta] [dateTo.getComponent()]
        dateRangeBox.appendChild(dateFromLabel);
        dateRangeBox.appendChild(dateFrom.getComponent());
        dateRangeBox.appendChild(dateToLabel);
        dateRangeBox.appendChild(dateTo.getComponent());
        // La HBox de rango de fechas ocupa la segunda celda de la Row.
        row.appendChild(dateRangeBox);
        rows.appendChild(row);
	    
        row = new Row();
	    row.appendChild(fAccountLabel);
	    row.appendChild(fAccount.getComponent());
	    rows.appendChild(row);
        
        // Checkboxes
        row = new Row();
        row.appendCellChild(isShowOrganization,1);	
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
        ((org.zkoss.zul.Combobox) fReportType.getComponent()).setTooltiptext(FinancialReportConstants.getReferenceToolTip(Env.getCtx()));
	     
        // =======================================================
        // === Campo AD_Client_ID ===
    	// =======================================================
        final String COLUMN_NAME_CLIENT = "AD_Client_ID";
        // MLookup y fClient ya están instanciados en zkInit()
        fClient.setValue(Env.getAD_Client_ID(Env.getCtx()));
        fClient.addValueChangeListener(this);
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
	     fOrgParent.addValueChangeListener(this);
	     // tooltip simple
	     ((org.zkoss.zul.Combobox) fOrgParent.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("AD_OrgParent_ID"));
	     
		 // ===============================================================
		 // === Campo AD_Org_ID (Organizaciones Hijas - Asignación de Valores) ===
		 // ===============================================================
		 // ** ¡ATENCIÓN! Se elimina toda la duplicación de MLookupFactory y la re-instanciación de fOrg. **
		 // Se usa el fOrg ya creado en zkInit().
		 fOrg.setValue(Env.getContextAsInt(Env.getCtx(), "$AD_Org_ID"));
		 fOrg.addValueChangeListener(this);
		 ((org.zkoss.zul.Combobox) fOrg.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("AD_Org_ID"));
        
        // =======================================================
        // === Campo C_AcctSchema_ID ===
        // =======================================================
        // MLookup y fAcctSchema ya están instanciados en zkInit()
        fAcctSchema.setValue(Env.getContextAsInt(Env.getCtx(), "$C_AcctSchema_ID"));
        fAcctSchema.addValueChangeListener(this);
        ((org.zkoss.zul.Combobox) fAcctSchema.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription("C_AcctSchema_ID"));
        
        // =======================================================
        // === Campo PostingType ===
        // =======================================================
        // Obtener el valor de referencia de la lista
        // LLAMA AL MÉTODO PARA CARGAR Y RESTAURAR EL COMBO
        initPostingTypeCombo(); 
        
        // =======================================================
        // === Campo C_Period_ID (Carga directa ZK) ===
        // =======================================================
        final String COLUMN_NAME_PERIOD = "C_Period_ID"; 
        // fPeriod ya está instanciado en zkInit() con el lookup base.
        // 2. Ejecutar consulta SQL para obtener la lista ordenada (Manualmente)
        List<KeyNamePair> periodList = new ArrayList<>();
        Integer defaultPeriodID = null; // Usaremos esto para el valor por defecto
        String sqlPeriods = "SELECT C_Period_ID, Name FROM C_Period "
                          + "WHERE IsActive='Y' AND AD_Client_ID IN (0, ?) "
                          + "ORDER BY StartDate DESC"; // <-- ¡Orden Descendente Garantizado!
        try (PreparedStatement pstmt = DB.prepareStatement(sqlPeriods, null)) {
            pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int periodID = rs.getInt(1);
                    String periodName = rs.getString(2);
                    // Si es el primer resultado, lo guardamos como valor por defecto
                    if (defaultPeriodID == null) {
                        defaultPeriodID = periodID; 
                    }
                    periodList.add(new KeyNamePair(periodID, periodName));
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
            // 5. Establecer el valor por defecto: Contexto o el período más reciente (Primer elemento)
            Integer contextPeriodID = Env.getContextAsInt(Env.getCtx(), "$C_Period_ID");
            if (contextPeriodID != null && contextPeriodID.intValue() > 0) {
                // Usar Contexto si existe
                fPeriod.setValue(contextPeriodID); 
            } else {
                // Usar el período más reciente (Primer elemento de la lista)
                fPeriod.setValue(defaultPeriodID); 
            }
        } catch (Exception e) {
            CLogger.getCLogger(getClass()).log(Level.SEVERE, 
                "Fallo al forzar el setModel() o la selección para fPeriod. El componente podría no ser un Combobox.", e);
        }
        fPeriod.addValueChangeListener(this);
        ((org.zkoss.zul.Combobox) fPeriod.getComponent()).setTooltiptext(MsgUtils.getElementFullDescription(COLUMN_NAME_PERIOD));


        // ==================================================================
        // === Lógica de Inicialización de dateFrom/dateTo de C_Period_ID ===
        // ==================================================================
        Integer initialPeriodID = (Integer) fPeriod.getValue(); 

        if (initialPeriodID != null && initialPeriodID.intValue() > 0) {
            // Si hay un período válido cargado inicialmente, usa SUS fechas.
            setDatesFromPeriod(initialPeriodID);
        
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
        // Añadir listeners para que reaccionen a cambios posteriores
        if (dateFrom != null) dateFrom.addValueChangeListener(this);
        if (dateTo != null) dateTo.addValueChangeListener(this);
        
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
        
        // =======================================================
        // ======= isShowOrganization  =======
        // =======================================================
        isShowOrganization.setValue(true);
        isShowOrganization.setChecked(true);
        isShowOrganization.addActionListener(this);

        // =======================================================
        // ======= isShowZERO  =======
        // =======================================================
        isShowZERO.setValue(false);
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

	/** Evento de botones */
    @Override
    public void onEvent(Event event) throws Exception {
        Object source = event.getTarget();

        if (source == resetButton) {
        	resetReportForm();
        } else if (source == downloadButton) {
        	downloadReport();
        } else if (source == processButton) {
        	ReportMetadata metadata  = runServerProcessForm();
        	// Acceder a las variables para pasarlas al viewer
        	fullPath = metadata.getFullPath();
        	String[] headers = metadata.getHeaders();
        	int[] widths = metadata.getWidths();
        	int headerRows = metadata.getHeaderRows();
        	previewReportWeb(fullPath, headers, widths, headerRows, maxVisibleRows);
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
	public void valueChange(ValueChangeEvent event) {
	    
	    Object newValue = event.getNewValue();
	    Object oldValue = event.getOldValue();
	    log.warning("Value changed: " + event.getPropertyName()+" New Value="+newValue+"  Old Value="+oldValue);
	    
	    if (event.getPropertyName().equals("ReportType")) {
		    // ========================================================
			// === ReportType (FinancialReportType) ===
			// ========================================================
		    String reportType = null;
	        // Verificar el tipo de objeto devuelto
	        if (newValue instanceof org.compiere.util.ValueNamePair) {
	            // Si el valor es un ValueNamePair (común con modelos ZK forzados), 
	            // extraemos la clave (String) que contiene "TRB", "BAL", etc.
	            reportType = ((org.compiere.util.ValueNamePair) newValue).getValue();
	        } else if (newValue instanceof String) {
	            // Si el valor es un String (común si la sincronización del wrapper funcionó, o si el valor es nulo)
	            reportType = (String) newValue;   
	        }
	        // Nos aseguramos de tener una clave válida para continuar
	        if (reportType == null || reportType.isEmpty()) {
	            return;
	        }
	        // Setear el valor de referencia
	        if (newValue instanceof String) {
                m_reportType_value = (String) newValue;
            } else if (newValue instanceof ValueNamePair) {
                m_reportType_value = ((ValueNamePair) newValue).getValue();
            }
	        // ========================================================
	        // === OBTENER NOMBRE Y DESCRIPCIÓN TRADUCIDOS (Limpio) ===
	        // ========================================================
	        // No se muestra la cuenta
	        fAccount.setMandatory(false);
	        fAccount.setVisible(false);
	        fAccountLabel.setVisible(false);
	        // === LÓGICA ESPECÍFICA POR TIPO DE REPORTE ===
	        if (FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_ONE_PERIOD.equals(reportType)) {
	            // Lógica específica para Balance de Comprobación de un período
	            // Ocultar el campo DateTo o activar DateFrom
	            log.warning(reportType);
	        } else if (FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_TWO_DATES.equals(reportType)) {
	            // Lógica específica para Balance de Comprobación entre dos fechas
	            // Asegurar que los campos DateFrom y DateTo estén visibles/activos

	        } else if (FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_BALANCE.equals(reportType)) {
		        // SI se muestra la cuenta
		        fAccount.setMandatory(true);
		        fAccount.setVisible(true);
		        fAccountLabel.setVisible(true);

	        } else if (FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS.equals(reportType)) {

	        } else if (FinancialReportConstants.REPORT_TYPE_ANALITIC_FINANCIAL_STATE.equals(reportType)) {

	        } else if (FinancialReportConstants.REPORT_TYPE_ACCOUNT_ELEMENTS.equals(reportType)) {

	        }
	        
	    } else if (event.getPropertyName().equals("AD_OrgParent_ID")) {
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
	        initReportTypeCombo();
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
		    // Llama al método de servicio que consulta la DB y setea las fechas.
		    setDatesFromPeriod(periodID);
		    
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
	        // B. Validación: Fechas dentro del Periodo Contable
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
        dynInit();
	}
	  
	private ReportMetadata runServerProcessForm() {
		String errorMsg = "";
		String fullPath="";
//		File xlsxFile = null;
		ReportMetadata reportMetadata = null;
		int AD_Client_ID = fClient.getValue() != null ? ((Integer) fClient.getValue()) : Env.getAD_Client_ID(Env.getCtx());
		int C_AcctSchema_ID = fAcctSchema.getValue() != null ? ((Integer) fAcctSchema.getValue()) : 0;
		int AD_Org_ID = fOrg.getValue() !=null ? ((Integer) fOrg.getValue()) : 0;
		int AD_OrgParent_ID = fOrgParent.getValue() !=null ? ((Integer) fOrgParent.getValue()) : 0;
		// Crear un ProcessInfo simulado para lockUI/unlockUI
	    ProcessInfo pi = new ProcessInfo(Msg.getMsg(Env.getCtx(), "Processing"), 0);
		// Recolección de Parámetros del Formulario
        Map<String, Object> parameters = new HashMap<>();
        // Report Type
        String reportTypeKey = m_reportType_value;
        initReportTypeCombo();
        // Object PostingType =
        // Restaurar el modelo ZK (usa m_postingType_value para re-seleccionar)
        initPostingTypeCombo(); 
        // Leer el valor persistido para el reporte
        String PostingType = m_postingType_value;
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
        // Obtener el valor booleano (true/false)
        Boolean showZero = (Boolean) isShowZERO.getValue();
        Boolean showOrg = (Boolean) isShowOrganization.getValue();
        // Conversión usando el operador ternario (forma más concisa)
        String isShowZERO_String = showZero ? "Y" : "N";
        String isShowOrganization_String = showOrg ? "Y" : "N";
        // Parámetros Requeridos
        parameters.put("AD_Client_ID", AD_Client_ID);
        parameters.put("AD_OrgParent_ID", AD_OrgParent_ID);
        parameters.put("AD_Org_ID", AD_Org_ID);
        parameters.put("C_AcctSchema_ID", C_AcctSchema_ID);
        parameters.put("PostingType", (String) PostingType);
        parameters.put("C_Period_ID", C_Period_ID);
        parameters.put("DateFrom", (Timestamp) dateFrom.getValue());
        parameters.put("DateTo", (Timestamp) dateTo.getValue());
        parameters.put("isShowZERO", (String) isShowZERO_String);
        parameters.put("isShowOrganization", (String) isShowOrganization_String);
        // Resto de parametros
        // Selección de la Estrategia (Generador)
        IReportGenerator generador = ReportGeneratorFactory.getGenerator(reportTypeKey);
        
        if (generador == null) {
        	errorMsg = "Tipo de reporte no soportado.";
            Dialog.error(form.getWindowNo(), errorMsg);
            return reportMetadata;
        }
        // Generar Reporte
		try {
			// 🔒 Bloquear la UI y deshabilitar campos
	        lockUI(pi);
	        // Llama al método generate(), que internamente llama a generateReportContent()
	        reportMetadata = generador.generate(Env.getCtx(), form.getWindowNo(), parameters);
	        
		} catch (IOException e) {
			log.log(Level.SEVERE, "Fallo al generar el archivo XLSX.", e);
            Dialog.error(form.getWindowNo(), "Error de E/S al generar el reporte.");
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

    private void previewReportWeb(String fullPath, String[] headers, int[] widths, int headerRowCount, int maxVisibleRow) throws FileNotFoundException {
        
    	log.warning("El archivo en Form:"+fullPath);
        // Limpio el centro
        center.getChildren().clear();
        ExcelViewerPanel viewer = new ExcelViewerPanel(fullPath, headers, widths, headerRowCount, maxVisibleRows);
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
        
        // 1. Cargar la Lista de Valores
        final int AD_REFERENCE_ID_REPORT = FinancialReportConstants.AD_REFERENCE_REPORT_TYPE; 
        ValueNamePair[] reportTypesArray = MRefList.getList(Env.getCtx(), AD_REFERENCE_ID_REPORT, false);
        List<ValueNamePair> reportTypes = new ArrayList<>(java.util.Arrays.asList(reportTypesArray));
        
        // 2. Ordenar Alfabéticamente
        Collections.sort(reportTypes, new Comparator<ValueNamePair>() {
             public int compare(ValueNamePair p1, ValueNamePair p2) {
                 return p1.getName().compareToIgnoreCase(p2.getName()); // Se ordena por Nombre, no por Valor.
             }
        });

        // 3. Determinar el valor a establecer (usa la variable persistida)
        String valueToSet = m_reportType_value;
        String defaultReportValue = FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_ONE_PERIOD;

        // Fallback si la variable está nula
        if (valueToSet == null || valueToSet.isEmpty()) {
            valueToSet = defaultReportValue;
        }
        
        // 4. Asignar la lista al Combobox ZK y forzar el valor
        try {
            org.zkoss.zul.Combobox comboboxReport = (org.zkoss.zul.Combobox) fReportType.getComponent();
            
            // REASIGNAR EL MODELO: CRÍTICO para que no se pierda.
            org.zkoss.zul.ListModelList<ValueNamePair> modelReport = new org.zkoss.zul.ListModelList<>(reportTypes);
            comboboxReport.setModel(modelReport);
            
            // 5. Buscar el VNP y establecer el valor visible e interno.
            ValueNamePair selectedVNP = null;
            for (ValueNamePair vnp : reportTypes) {
                if (vnp.getValue().equals(valueToSet)) {
                    selectedVNP = vnp;
                    break;
                }
            }
            
            if (selectedVNP != null) {
                // Establecer la clave interna (el valor real, ej: "TRB")
                fReportType.setValue(valueToSet);
                // Forzar la visualización del label en el combo ZK (ej: "Balance de Comprobación")
                comboboxReport.setValue(selectedVNP.getName());
            } else {
                // Si el valor guardado (m_reportType_value) no se encuentra, 
                // establecemos la clave interna sin forzar el texto visible (puede quedar vacío)
                fReportType.setValue(valueToSet);
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
     * Consulta la base de datos para obtener las fechas de inicio y fin
     * de un C_Period_ID dado y las asigna a dateFrom y dateTo.
     * @param periodID El ID del período seleccionado.
     */
    private void setDatesFromPeriod(Integer periodID) {
    	// Obtener el listener: Se refiere a la instancia de tu clase (ej., FinancialReports_TreeOrg_Form.this)
        final ValueChangeListener listener = this;
        // === DESHABILITAR LISTENERS DE FECHA ===
        if (dateFrom != null) {
            dateFrom.removeValuechangeListener(listener);
        }
        if (dateTo != null) {
            dateTo.removeValuechangeListener(listener);
        }
        
        try {
	        if (periodID == null || periodID.intValue() <= 0) {
	            // Limpiar fechas si no hay período válido
	            if (dateFrom != null) dateFrom.setValue(null);
	            if (dateTo != null) dateTo.setValue(null);
	            return;
	        }
	        MPeriod period = new MPeriod(Env.getCtx(), periodID.intValue(), null);
	        Timestamp dbDateFrom = period.getStartDate();
	        Timestamp dbDateTo = period.getEndDate();
	        // Asignar los valores a los componentes UI
	        if (dbDateFrom != null && dateFrom != null) {
	            dateFrom.setValue(dbDateFrom);
	        }
	        if (dbDateTo != null && dateTo != null) {
	            dateTo.setValue(dbDateTo);
	        }
        } finally {
        	// Asegura que los listeners se restablezcan incluso si hay una excepción.
            if (dateFrom != null) {
                dateFrom.addValueChangeListener(listener);
            }
            if (dateTo != null) {
                dateTo.addValueChangeListener(listener);
            }
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

package org.amerp.amnforms;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;


import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.Env;

import org.adempiere.util.Callback;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Tab;
import org.adempiere.webui.component.Tabbox;
import org.adempiere.webui.component.Tabpanel;
import org.adempiere.webui.component.Tabpanels;
import org.adempiere.webui.component.Tabs;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.Dialog;
import org.amerp.amnutilities.AmerpMsg;
import org.compiere.model.MColumn;
import org.compiere.util.DisplayType;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Vbox;

public class AMNPayrollAssistForm extends AMNPayrollAssist implements IFormController, EventListener<Event>, 
	WTableModelListener, ValueChangeListener {

    private static final long serialVersionUID = 25710426684184569L;

	/** UI form instance */
	private CustomForm form = new CustomForm();
	
	/**
	 *	Default constructor
	 */
	public AMNPayrollAssistForm()
	{
		super();
		try
		{
			super.dynInit();
			dynInit();
			initForm();
			zkInit();
		}
		catch(Exception e)
		{
			log.warning(e.getMessage());
		}
	}	//	init
	
	
	private Borderlayout mainLayout = new Borderlayout();

    // Áreas
    private North north = new North();   // Filtros
    private Center center = new Center(); // Lista empleados
    private South south = new South();   // Tabs y botones

    // Controles filtros
    private Label dateFromLabel = new Label();
    private WDateEditor dateFrom =new WDateEditor();
    private Label dateToLabel = new Label();
    private WDateEditor dateTo = new WDateEditor();
    private Button btnSearch = new Button("Buscar");
	private Label employeeLabel = new Label();
	private WSearchEditor employeeSearch = null;
	private Label statusLabel = new Label();
	private Listbox statusListbox = new Listbox();
	private Label contractLabel = new Label();
	private WTableDirEditor contractListEditor;
	private Label locationLabel = new Label();
	private WTableDirEditor locationListEditor;
	private Label sectorLabel = new Label();
	private WTableDirEditor sectorListEditor;
	private Listbox sectorListbox = new Listbox();
	private Checkbox isActive = new Checkbox();
	
	// Lista trabajadores y asistencias
	//private WListbox employeeList = new WListbox();
    private WListbox employeeTable = ListboxFactory.newDataTable();
	private WListbox payAssisTable = ListboxFactory.newDataTable();
	private WListbox payAssisRowTable = ListboxFactory.newDataTable();
	private WListbox payAssisProcTable = ListboxFactory.newDataTable();
	
    // Contenedor Sur 
    private Vbox southBox = new Vbox();
    
    // Tabs detalle
    private Tabbox tabbox = new Tabbox();
    private Tab tab1 = new Tab(AmerpMsg.getTranslatedTableName("AMN_Payroll_Assist_Proc"));
    private Tab tab2 = new Tab(AmerpMsg.getTranslatedTableName("AMN_Payroll_Assist"));
    private Tab tab3 = new Tab(AmerpMsg.getTranslatedTableName("AMN_Payroll_Assist_Row"));

    private Tabpanel panel1 = new Tabpanel();
    private Tabpanel panel2 = new Tabpanel();
    private Tabpanel panel3 = new Tabpanel();

    // Botones inferiores
	private Button refreshButton = new Button();
	private Button cancelButton = new Button();
	private Button resetButton = new Button();
	private Button zoomButton = new Button();
	private Button selectButton = new Button();
	private Button allocateButton = new Button();
	private Button undoButton = new Button();
	

	/**
	 *  Dynamic Init (prepare dynamic fields)
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dynInit() throws Exception
	{
		int AD_Column_ID = 0;
		//  AMN_Employee
		AD_Column_ID = MColumn.getColumn_ID("AMN_Employee", "AMN_Employee_ID"); // (1000446)  AMN_Employee_ID.AMN_Employee
		MLookup lookupEMP = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		employeeSearch = new WSearchEditor("AMN_Employee_ID", true, false, true, lookupEMP);
		employeeSearch.addValueChangeListener(this);

		// ======= DateFrom DateTo =======
		Calendar cal = Calendar.getInstance();
		Date ctxDate = Env.getContextAsDate(Env.getCtx(), "#Date");
		if (ctxDate != null) {
		    cal.setTime(ctxDate);
		} 
		// Primer día del mes
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		m_DateFrom = new Timestamp(cal.getTimeInMillis());
		// Último día del mes
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		m_DateTo = new Timestamp(cal.getTimeInMillis());
		// Asignar a los componentes UI
		dateFrom.setValue(m_DateFrom);
		dateFrom.addValueChangeListener(this);
		dateTo.setValue(m_DateTo);
		dateTo.addValueChangeListener(this);

		// =======  AMN_Contract - Payroll Contract ======= 
		// Obtener el rol del usuario actual desde el contexto
	    int roleID = Env.getAD_Role_ID(Env.getCtx());

	    // Inicialización
	    AD_Column_ID = MColumn.getColumn_ID("AMN_Employee", "AMN_Contract_ID");
		MLookup lookupContract = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		contractListEditor = new WTableDirEditor("AMN_Contract_ID", true, false, true, lookupContract);
	    // Listener centralizado
	    contractListEditor.addValueChangeListener(this);

		// ======= AMN_Location =======
	    AD_Column_ID = MColumn.getColumn_ID("AMN_Employee", "AMN_Location_ID");
		MLookup lookupLocation = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		locationListEditor = new WTableDirEditor("AMN_Location_ID", true, false, true, lookupLocation);
	    // Listener centralizado
		locationListEditor.addValueChangeListener(this);

		// ======= AMN_Sector =======
		sectorListbox = dynInitSectorListbox(m_AMN_Location_ID);
	    if (sectorListbox == null) {
	        log.warning("No se encontraron Localizaciones válidas.");
		}
		
		// ======= AMN_Status (A,R,S,V)  =======
	    statusListbox = dynInitStatusListbox();
	    if (statusListbox == null) {
	        log.warning("No se encontraron Status válidos.");
		}

	    // ======= AMN_isActive)  =======
	    isActive.setValue(true);
	    isActive.addActionListener(this);

	}

	private Listbox dynInitSectorListbox(int locationID) {
	    Integer defaultSectorID = getFirstActiveSectorID(locationID);
	    List<KeyNamePair> validSectors = getValidSectors(locationID);

	    if (validSectors.isEmpty()) {
	        log.warning("No se encontraron Sectores válidos.");
	        return null;
	    }

	    Listbox listbox = new Listbox();
	    listbox.setMold("select");  // Combo desplegable

	    // Elemento vacío al inicio
	    Listitem blankItem = new Listitem();
	    blankItem.setValue(null);
	    blankItem.appendChild(new Listcell(""));
	    listbox.appendChild(blankItem);

	    int indexToSelect = 0;  // Por defecto, ítem en blanco

	    for (int i = 0; i < validSectors.size(); i++) {
	        KeyNamePair pair = validSectors.get(i);
	        Listitem item = new Listitem();
	        item.setValue(pair.getKey());             // ID
	        item.appendChild(new Listcell(pair.getName())); // Nombre
	        listbox.appendChild(item);

	    }

	    listbox.setSelectedIndex(indexToSelect);
	    Events.postEvent("onSelect", listbox, null);

	    listbox.addEventListener("onSelect", new EventListener<Event>() {
	        @Override
	        public void onEvent(Event event) throws Exception {
	            Listitem selectedItem = listbox.getSelectedItem();
	            Integer selectedSectorID = selectedItem != null ? (Integer) selectedItem.getValue() : null;

	            if (selectedSectorID != null) {
	                m_AMN_Sector_ID = selectedSectorID;
	            } else {
	                m_AMN_Sector_ID = 0;
	            }
	            refresh();
	        }
	    });

	    return listbox;
	}

	private Listbox dynInitStatusListbox() {
	    List<KeyNamePair> validStatuses = getValidListReferenceTranslated("AMN_Employee", "Status");

	    if (validStatuses.isEmpty()) {
	        log.warning("No se encontraron Status válidos.");
	        return null;
	    }

	    Listbox listbox = new Listbox();
	    listbox.setMold("select");  // Combo desplegable

	    // Elemento vacío al inicio
	    Listitem blankItem = new Listitem();
	    blankItem.setValue(null);
	    blankItem.appendChild(new Listcell(""));
	    listbox.appendChild(blankItem);

	    int indexToSelect = 0;  // Por defecto, ítem en blanco

	    for (int i = 0; i < validStatuses.size(); i++) {
	        KeyNamePair pair = validStatuses.get(i);
	        Listitem item = new Listitem();
	        item.setValue(pair.getKey());      // secuencia numérica 1,2,3…
	        item.appendChild(new Listcell(pair.getName())); // "A-Activo", "R-Retirado", etc.
	        listbox.appendChild(item);

	        // Selección automática si coincide con valor por defecto
	        // Opcional: si quieres que el primer item sea el default, puedes implementarlo aquí
	    }

	    listbox.setSelectedIndex(indexToSelect);
	    Events.postEvent("onSelect", listbox, null);

	    listbox.addEventListener("onSelect", new EventListener<Event>() {
	        @Override
	        public void onEvent(Event event) throws Exception {
	            Listitem selectedItem = listbox.getSelectedItem();
	            Integer selectedStatusKey = selectedItem != null ? (Integer) selectedItem.getValue() : null;

	            if (selectedStatusKey != null) {
	            	m_Status_Item = selectedStatusKey; // define esta variable en tu clase
	            } else {
	            	m_Status_Item = 0;
	            }
	            refresh();
	        }
	    });

	    return listbox;
	}

	/**
	 * Main Layout
	 */
    protected void initForm() {
        //this.setTitle("Asistente de Nómina");

        // Layout general
        form.appendChild(mainLayout);
        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");

        mainLayout.appendChild(north);
        mainLayout.appendChild(center);
        mainLayout.appendChild(south);

        // ======== Área lista empleados ========
		ZKUpdateUtil.setWidth(employeeTable, "100%");
		ZKUpdateUtil.setVflex(employeeTable, "1");
		center.appendChild(employeeTable);
		
        // ======== Contenedor Sur ========
        southBox.setWidth("100%");
        south.appendChild(southBox);

        // ======== Tabs detalle ========
        tabbox.appendChild(new Tabs());
        tabbox.appendChild(new Tabpanels());

        tabbox.getTabs().appendChild(tab1);
        tabbox.getTabs().appendChild(tab2);
        tabbox.getTabs().appendChild(tab3);

        tabbox.getTabpanels().appendChild(panel1);
        tabbox.getTabpanels().appendChild(panel2);
        tabbox.getTabpanels().appendChild(panel3);

        southBox.appendChild(tabbox);
        
        // ======== Tab1: AMN_Payroll_Assist_Proc ========
        ZKUpdateUtil.setWidth(payAssisProcTable, "100%");
        ZKUpdateUtil.setVflex(payAssisProcTable, "1");
        panel1.appendChild(payAssisProcTable);

        // ======== Tab2: AMN_Payroll_Assist ========
        ZKUpdateUtil.setWidth(payAssisTable, "100%");
        ZKUpdateUtil.setVflex(payAssisTable, "1");
        panel2.appendChild(payAssisTable);

        // ======== Tab3: AMN_Payroll_Assist_Row ========
        ZKUpdateUtil.setWidth(payAssisRowTable, "100%");
        ZKUpdateUtil.setVflex(payAssisRowTable, "1");
        panel3.appendChild(payAssisRowTable);
    }

    /**
     * ADD Objects to Layout
     * @throws Exception
     */
	private void zkInit() throws Exception
	{

		// Filtros
		// Trabajador
		employeeLabel.setText(Msg.translate(Env.getCtx(), "AMN_Employee_ID"));
		//  AMN_Employee
		int AD_Column_ID = MColumn.getColumn_ID("AMN_Employee", "AMN_Employee_ID"); // (1000446)  AMN_Employee_ID.AMN_Employee
		MLookup lookupEMP = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		employeeSearch = new WSearchEditor("AMN_Employee_ID", true, false, true, lookupEMP);
		employeeSearch.addValueChangeListener(this);
		
		// ============================================================
		// ======== Barra botones ========
		// ============================================================
		Hbox hboxButtons = new Hbox();
		hboxButtons.appendChild(refreshButton);
		hboxButtons.appendChild(resetButton);
		hboxButtons.appendChild(zoomButton);
		hboxButtons.appendChild(undoButton);
		hboxButtons.appendChild(cancelButton);
		southBox.appendChild(hboxButtons);

		// Listeners
		refreshButton.addEventListener("onClick", this);
		resetButton.addEventListener("onClick", this);
		zoomButton.addEventListener("onClick", this);
		cancelButton.addEventListener("onClick", this);
		undoButton.addEventListener("onClick", this);
      
		// Allocate Button
		allocateButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Process")));
		allocateButton.addActionListener(this);
		allocateButton.setImage(ThemeManager.getThemeResource("images/Process16.png"));
		// Reset Button
		resetButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Reset")));
		resetButton.setImage(ThemeManager.getThemeResource("images/Reset16.png"));
		resetButton.addActionListener(this);
		// Refresh Button
		refreshButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Refresh")));
		refreshButton.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
		refreshButton.addActionListener(this);
		refreshButton.setAutodisable("self");
		// Select Button
		selectButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "SelectAll")));
		selectButton.setImage(ThemeManager.getThemeResource("images/SelectAll16.png"));
		selectButton.addActionListener(this);
		selectButton.setAutodisable("self");
		// ZOOM Button
		zoomButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Zoom")));
		zoomButton.setImage(ThemeManager.getThemeResource("images/Zoom16.png"));
		zoomButton.addActionListener(this);
		zoomButton.setAutodisable("self");
		// Undo Button
		undoButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Ignore")));
		undoButton.setImage(ThemeManager.getThemeResource("images/Ignore16.png"));
		undoButton.addActionListener(this);
		// Cancel Button
		cancelButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Close")));
		cancelButton.setImage(ThemeManager.getThemeResource("images/Cancel16.png"));
		cancelButton.addActionListener(this);
		
		// ============================================================
		// ======== Área de filtros ========
		// ============================================================
		// Inicio Etiquetas Payroll Contract - Location - Sector
		contractLabel.setText(Msg.translate(Env.getCtx(), "AMN_Contract_ID"));
		locationLabel.setText(Msg.translate(Env.getCtx(), "AMN_Location_ID"));
		sectorLabel.setText(Msg.translate(Env.getCtx(), "AMN_Sector_ID"));
		dateFromLabel.setText(Msg.translate(Env.getCtx(), "DateFrom"));
		dateToLabel.setText(Msg.translate(Env.getCtx(), "DateTo"));
		statusLabel.setText(Msg.translate(Env.getCtx(), "Status"));
		isActive.setText(Msg.getElement(Env.getCtx(), "IsActive"));

		
		Grid filterGrid = new Grid();
		Rows rows = new Rows();
		//  Payroll Contract - Location - Sector
		Row row = new Row();
		
		row.appendCellChild(contractLabel.rightAlign());
		ZKUpdateUtil.setHflex(contractListEditor.getComponent(), "true");
		row.appendCellChild(contractListEditor.getComponent(),1);
		row.appendCellChild(locationLabel.rightAlign());
		ZKUpdateUtil.setHflex(locationListEditor.getComponent(), "true");
		row.appendCellChild(locationListEditor.getComponent(),1);
		row.appendCellChild(sectorLabel.rightAlign());
		ZKUpdateUtil.setHflex(sectorListbox, "true");
		row.appendCellChild(sectorListbox, 1);
		rows.appendChild(row);
		
		// ==============================
		row = new Row();
		row.appendCellChild(dateFromLabel.rightAlign());
		row.appendChild(dateFrom.getComponent());
		row.appendCellChild(dateToLabel.rightAlign());
		row.appendChild(dateTo.getComponent());
		row.appendCellChild(statusLabel.rightAlign());
		ZKUpdateUtil.setHflex(statusListbox, "true");
		row.appendCellChild(statusListbox, 1);
		row.appendCellChild(isActive,1);	
		rows.appendChild(row);
		
		filterGrid.appendChild(rows);
		north.appendChild(filterGrid);
      
	}
	
	/**
	 * onEvent
	 * parameter: Event event
	 */
    @Override
    public void onEvent(Event event) throws Exception {
    	log.warning("onEvent="+event.getName());
        if (event.getTarget() == btnSearch) {
            //  ejecutar búsqueda de empleados
        } else if (event.getTarget() == refreshButton) {
            //  refrescar datos
        } else if (event.getTarget() == resetButton) {
        	// Reset 
			employeeTable.clear();
			m_AMN_Contract_ID = 0;
			m_AMN_Location_ID = 0;
			m_AMN_Sector_ID = 0;
			m_AMN_Employee_ID = 0;
			m_Status="A";
			m_isActive="Y";
        } else if (event.getTarget() == undoButton) {
            // undo form

        } else if (event.getTarget().equals(isActive)) 	{

        	if (isActive.isSelected() == true ) 
        		m_isActive="Y";
        	else
        		m_isActive="N";
        	
		} else if (event.getTarget() == cancelButton) {
	        // Cierra la pestaña que contiene este formulario
	    	Dialog.ask(m_WindowNo, Msg.getMsg(Env.getCtx(), "ExitApplication?"), new Callback<Boolean>() {
	    	    @Override
	    	    public void onCallback(Boolean result) {
	    	        if (result) {
	    	            dispose();
	    	            //	document No not updated
	    	        }
	    	    }
	    	});
		}
        log.warning("onEvent:"+" m_isActive="+m_isActive+" m_AMN_Sector_ID="+m_AMN_Sector_ID);
    }

    /**
     * valueChange
     * parameter: ValueChangeEvent e
     */
	@Override
	public void valueChange(ValueChangeEvent e) {
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		Object source = e.getSource();
		if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
		log.warning("name="+name+" Value="+value+"  source="+source);
		if (value == null &&
		   !(name.equals("AMN_Employee_ID") || name.equals("DateFrom") || name.equals("DateFrom") ||
		     name.equals("AMN_Contract_ID") || name.equals("AMN_Location_ID") || name.equals("AMN_Sector_ID")))
			return;
		
		// AMN_Contract_ID
		if (name.equals("AMN_Contract_ID"))
		{
			m_AMN_Contract_ID = ((Integer) value).intValue();
			loadEmployee();
		}
		// AMN_Location_ID
		if (name.equals("AMN_Location_ID"))
		{
			m_AMN_Location_ID = ((Integer) value).intValue();
			
			List<KeyNamePair> validSectors = getValidSectors(m_AMN_Location_ID);
			sectorListbox.getItems().clear();

			for (KeyNamePair pair : validSectors) {
			    Listitem item = new Listitem();
			    item.setValue(pair.getKey());
			    item.appendChild(new Listcell(pair.getName()));
			    sectorListbox.appendChild(item);
			}

			loadEmployee();
	        refresh();
		}
		// AMN_Sector_ID
		if (name.equals("AMN_Sector_ID"))
		{
			m_AMN_Sector_ID = ((Integer) value).intValue();
			loadEmployee();
		}
		// DateFrom DateTo
		if (name.equals("Date")) {
			if (source == dateFrom) {
				// Acción para dateFrom
				m_DateFrom = (Timestamp) dateFrom.getValue();
				log.warning("valueChange: dateFrom ...." + m_DateFrom);
			} else if (source == dateTo) {
				// Acción para dateTo
				m_DateTo = (Timestamp) dateTo.getValue();
				log.warning("valueChange: dateTo ...." + m_DateTo);
			}
		}
		log.warning("valueChange:"+" m_AMN_Contract_ID="+m_AMN_Contract_ID+" m_AMN_Location_ID="+m_AMN_Location_ID+" m_AMN_Sector_ID="+m_AMN_Sector_ID);
	}

	private void refresh ()
	{
		log.warning(".........Refresh........");
		// Mantener los valores actuales de las variables antes de refrescar
	    m_AMN_Employee_ID = (employeeSearch.getValue() != null) ? ((Integer)employeeSearch.getValue()).intValue() : 0;   
	    // Obtener IDs seleccionados en los listboxes
	    // AMN_Contract
//	    Listitem selectedContractItem = contractListbox.getSelectedItem();
//	    Integer selectedContractID = selectedContractItem != null ? (Integer) selectedContractItem.getValue() : null;
//	    m_AMN_Contract_ID = (selectedContractID != null) ? (Integer) selectedContractID : 0;
	    // AMN_Location
//	    Listitem selectedLocationItem = locationListEditor.getSelectedItem();
//	    Integer selectedLocationID = selectedLocationItem != null ? (Integer) selectedLocationItem.getValue() : null;
//	    m_AMN_Location_ID = (selectedLocationID != null) ? (Integer) selectedLocationID : 0;
	    // AMN_Sector
//	    Listitem selectedSectorItem = sectorListEditor.getSelectedItem();
//	    Integer selectedSectorID = selectedSectorItem != null ? (Integer) selectedSectorItem.getValue() : null;
//	    m_AMN_Sector_ID = (selectedSectorID != null) ? (Integer) selectedSectorID : 0;
	    // Refrescar las tablas y demás elementos
//		loadBPartner1();
//		loadBPartner2();
		
	}

	/**
	 *  Load Employee Data
	 */
	private void loadEmployee()
	{
		
		Vector<Vector<Object>> data = getEmployeeData();
		Vector<String> columnNames = getAMNEmployeeColumnNames();
		
		employeeTable.clear();
		
		//  Remove previous listeners
		employeeTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelEmp = new ListModelTable(data);
		modelEmp.addTableModelListener(this);
		
	
		employeeTable.setData(modelEmp, columnNames);
		setEmployeeColumnClass(employeeTable);

	}   //  loadEmployee
	
	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose
	
	@Override
	public ADForm getForm() {
		return form;
	}

	@Override
	public void tableChanged(WTableModelEvent event) {
		// TODO Auto-generated method stub
		
	}
}

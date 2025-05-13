package org.amerp.webform.amwebui;

import static org.adempiere.webui.ClientInfo.MEDIUM_WIDTH;
import static org.adempiere.webui.ClientInfo.SMALL_WIDTH;
import static org.adempiere.webui.ClientInfo.maxWidth;
import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_BPARTNER_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_CURRENCY_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_PERIOD_AD_ORG_ID;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.DocumentLink;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.editor.WSearchEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.Dialog;
import org.amerp.webform.amwgrid.AMFAllocation;
import org.compiere.apps.form.Allocation;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.compiere.util.Util;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

/**
 * Form to create allocation (C_AllocationHdr and C_AllocationLine).
 *
 * Contributor : Fabian Aguilar - OFBConsulting - Multiallocation 
 * Luis Amesty: Refactor 
 */
@org.idempiere.ui.zk.annotation.Form(name = "org.compiere.apps.form.VAllocation")
public class AMWFAllocation extends AMFAllocation
	implements IFormController, EventListener<Event>, WTableModelListener, ValueChangeListener
{
	/** UI form instance */
	private CustomForm form = new CustomForm();
	
	/**
	 *	Default constructor
	 */
	public AMWFAllocation()
	{
		try
		{
			super.dynInit();
			dynInit();
			zkInit();
			calculate();	
			southPanel.appendChild(new Separator());
			southPanel.appendChild(statusBar);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	init
	
	/** Main layout for {@link #form} */
	private Borderlayout mainLayout = new Borderlayout();
	//Parameter
	/** Parameter panel. North of {@link #mainLayout} */
	private Panel parameterPanel = new Panel();
	/** Grid layout of {@link #parameterPanel} */
	private Grid parameterLayout = GridFactory.newGridLayout();		
	private Label bpartnerLabel = new Label();
	/** Employee parameter **/
	private Label employeeLabel = new Label();
	private WSearchEditor employeeSearch = null;
	/** bpartner parameter */
	private WSearchEditor bpartnerSearch = null;
	private Label currencyLabel = new Label();
	/** Currency parameter */
	private WTableDirEditor currencyPick = null;
	/** Multi currency parameter */
	private Checkbox multiCurrency = new Checkbox();
	private Label chargeLabel = new Label();
	private Label dateLabel = new Label();
	/** Document date parameter */
	private WDateEditor dateField = new WDateEditor();
	/** Auto write off parameter */
	private Checkbox autoWriteOff = new Checkbox();
	private Label organizationLabel = new Label();
	/** Organization parameter */
	private WTableDirEditor organizationPick;
	/** Number of column for {@link #parameterLayout} */
	private int noOfColumn;
	/** Center of {@link #mainLayout}. */
	private Borderlayout infoPanel = new Borderlayout();
	/** North of {@link #infoPanel} */
	private Panel paymentPanel = new Panel();
	/** Center of {@link #infoPanel} */ 
	private Panel invoicePanel = new Panel();
	//Invoice 
	/** Layout of {@link #invoicePanel} */
	private Borderlayout invoiceLayout = new Borderlayout();
	/** North of {@link #invoiceLayout} */
	private Label invoiceLabel = new Label();
	/** Center of {@link #invoiceLayout}. List of invoice documents. */
	private WListbox invoiceTable = ListboxFactory.newDataTable();		
	/** South of {@link #invoiceLayout} */
	private Label invoiceInfo = new Label();
	//Payments	
	/** Layout of {@link #paymentPanel} */
	private Borderlayout paymentLayout = new Borderlayout();
	/** North of {@link #paymentLayout} */
	private Label paymentLabel = new Label();
	/** Center of {@link #paymentLayout}. List of payment documents. */
	private WListbox paymentTable = ListboxFactory.newDataTable();	
	/** South of {@link #paymentLayout} */
	private Label paymentInfo = new Label();
		
	//Allocation
	/** South of {@link #mainLayout} */
	private Panel allocationPanel = new Panel(); //footer
	/** Grid layout of {@link #allocationPanel} */
	private Grid allocationLayout = GridFactory.newGridLayout();
	private Label differenceLabel = new Label();
	/** Difference between payment and invoice. Part of {@link #allocationLayout}. */
	private Textbox differenceField = new Textbox();
	/** Button to apply allocation. Part of {@link #allocationLayout}. */
	private Button refreshButton = new Button();
	private Button cancelButton = new Button();
	private Button resetButton = new Button();
	private Button zoomButton = new Button();
	private Button selectButton = new Button();
	private Button allocateButton = new Button();
	/** Charges. Part of {@link #allocationLayout}. */
	private WTableDirEditor chargePick = null;
	private Label DocTypeLabel = new Label();
	/** Document types. Part of {@link #allocationLayout}. */
	private WTableDirEditor DocTypePick = null;
	private Label allocCurrencyLabel = new Label();
	/** Status bar, bottom of {@link #allocationPanel} */
	private Hlayout statusBar = new Hlayout();	
	private Panel southPanel = new Panel();
	private Label processLabel = new Label();
	private Listbox processListbox = new Listbox();
	private Label contractLabel = new Label();
	private Listbox contractListbox = new Listbox();
	private Label docTypeLabel = new Label();
	private Listbox docTypeListbox = new Listbox();
	private Label dateDocLabel = new Label();
	private WDateEditor dateDocField = new WDateEditor();
	private Label orgDocLabel = new Label();
	private WTableDirEditor orgDocPick;
	/**
	 *  Layout {@link #form}
	 *  @throws Exception
	 */
	private void zkInit() throws Exception
	{
		//
		form.appendChild(mainLayout);
		ZKUpdateUtil.setWidth(mainLayout, "100%");
		ZKUpdateUtil.setHeight(mainLayout, "100%");
		
		dateLabel.setText(Msg.getMsg(Env.getCtx(), "Date"));
		autoWriteOff.setSelected(false);
		autoWriteOff.setText(Msg.getMsg(Env.getCtx(), "AutoWriteOff", true));
		autoWriteOff.setTooltiptext(Msg.getMsg(Env.getCtx(), "AutoWriteOff", false));
		//
		parameterPanel.appendChild(parameterLayout);
		allocationPanel.appendChild(allocationLayout);
		bpartnerLabel.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID"));
		employeeLabel.setText(Msg.translate(Env.getCtx(), "AMN_Employee_ID"));
		paymentLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Payment_ID"));
		invoiceLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Invoice_ID"));
		paymentPanel.appendChild(paymentLayout);
		invoicePanel.appendChild(invoiceLayout);
		invoiceInfo.setText(".");
		paymentInfo.setText(".");
		chargeLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Charge_ID"));
		DocTypeLabel.setText(" " + Msg.translate(Env.getCtx(), "C_DocType_ID"));	
		differenceLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		differenceField.setText("0");
		differenceField.setReadonly(true);
		differenceField.setStyle("text-align: right");
		
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
		// Cancel Button
		cancelButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "Cancel")));
		cancelButton.setImage(ThemeManager.getThemeResource("images/Cancel16.png"));
		cancelButton.addActionListener(this);
		
		currencyLabel.setText(Msg.translate(Env.getCtx(), "C_Currency_ID"));
		multiCurrency.setText(Msg.getMsg(Env.getCtx(), "MultiCurrency"));
		multiCurrency.addActionListener(this);
		allocCurrencyLabel.setText(".");		
		organizationLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		orgDocLabel.setText("Org "+ Msg.translate(Env.getCtx(), "PayrollDocuments"));

		// Payroll Process
		processLabel.setText(Msg.translate(Env.getCtx(), "AMN_Process_ID"));
		// Payroll Contract
		contractLabel.setText(Msg.translate(Env.getCtx(), "AMN_Contract_ID"));
		// Document Type
		docTypeLabel.setText(Msg.translate(Env.getCtx(), "C_DocType_ID"));
		// Document date
		dateDocLabel.setText(Msg.getElement(Env.getCtx(), "DateDoc")+"(s)");
		// parameters layout
		North north = new North();
		north.setBorder("none");
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		
		Rows rows = null;
		Row row = null;
		ZKUpdateUtil.setWidth(parameterLayout,"80%");
		rows = parameterLayout.newRows();
		
		row = rows.newRow();
		row.appendChild(dateLabel.rightAlign());
		row.appendChild(dateField.getComponent());
		row.appendCellChild(organizationLabel.rightAlign());
		ZKUpdateUtil.setHflex(organizationPick.getComponent(), "true");
		row.appendCellChild(organizationPick.getComponent(),1);	
		
		row = rows.newRow();
		row.appendCellChild(bpartnerLabel.rightAlign());
		ZKUpdateUtil.setHflex(bpartnerSearch.getComponent(), "true");
		row.appendCellChild(bpartnerSearch.getComponent(),1);
		bpartnerSearch.showMenu();
		row.appendCellChild(employeeLabel.rightAlign());
		ZKUpdateUtil.setHflex(employeeSearch.getComponent(), "true");
		row.appendCellChild(employeeSearch.getComponent(),1);
		
		row = rows.newRow();
		row.appendCellChild(currencyLabel.rightAlign(),1);
		ZKUpdateUtil.setHflex(currencyPick.getComponent(), "true");
		row.appendCellChild(currencyPick.getComponent(),1);		
		row.appendCellChild(multiCurrency,1);		
		row.appendCellChild(autoWriteOff,2);
		row.appendCellChild(new Space(),1);	
		
		// PARAMETROS ADICIONALES Payroll
		row = rows.newRow();
		// Document Organization
		row.appendCellChild(orgDocLabel.rightAlign());  
		ZKUpdateUtil.setHflex(orgDocPick.getComponent(), "true");
		row.appendCellChild(orgDocPick.getComponent(),1);
		// Agregar la etiqueta de "process" y el componente "processSearch"
		row.appendCellChild(processLabel.rightAlign());
		ZKUpdateUtil.setHflex(processListbox, "true");
		row.appendCellChild(processListbox, 1);
		// Agregar la etiqueta de "contract" y el componente "contractSearch"
		row.appendCellChild(contractLabel.rightAlign());
		ZKUpdateUtil.setHflex(contractListbox, "true");
		row.appendCellChild(contractListbox, 1);

		// Crear una nueva fila
		row = rows.newRow();
		// docType
		row.appendCellChild(docTypeLabel.rightAlign());  
		ZKUpdateUtil.setHflex(docTypeListbox, "true");
		row.appendCellChild(docTypeListbox, 1);
		// DateDoc" y el componente "dateDocField"
		row.appendCellChild(dateDocLabel.rightAlign());
		ZKUpdateUtil.setHflex(dateDocField.getComponent(), "true");
		row.appendCellChild(dateDocField.getComponent(), 1);
		
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		south.appendChild(southPanel);
		southPanel.appendChild(allocationPanel);
		allocationPanel.appendChild(allocationLayout);
		allocationPanel.appendChild(statusBar);
		
		rows = allocationLayout.newRows();
		row = rows.newRow();
		row.appendCellChild(refreshButton);
		row.appendCellChild(resetButton);
		row.appendCellChild(selectButton);
		row.appendCellChild(zoomButton);
		
		row.appendCellChild(differenceLabel.rightAlign());
		row.appendCellChild(allocCurrencyLabel.rightAlign());
		//differenceField.setHflex("true");
		ZKUpdateUtil.setHflex(differenceLabel, "min");
		row.appendCellChild(differenceField);
		
		row = rows.newRow();
		row.appendCellChild(chargeLabel.rightAlign());
		//chargePick.getComponent().setHflex("true");
		ZKUpdateUtil.setHflex(chargePick.getComponent(), "true");
		row.appendCellChild(chargePick.getComponent(),2);
		

		row = rows.newRow();
		row.appendCellChild(DocTypeLabel.rightAlign());
		chargePick.showMenu();
		ZKUpdateUtil.setHflex(DocTypePick.getComponent(), "true");
		row.appendCellChild(DocTypePick.getComponent());


		row = rows.newRow();
		Hbox box = new Hbox();
		//allocateButton.setHflex("true");
		ZKUpdateUtil.setHflex(allocateButton, "true");
		row.appendCellChild(allocateButton);
		row.appendCellChild(cancelButton);

		// payment layout
		paymentPanel.appendChild(paymentLayout);
		ZKUpdateUtil.setWidth(paymentPanel, "100%");
		ZKUpdateUtil.setHeight(paymentPanel, "100%");
		ZKUpdateUtil.setWidth(paymentLayout, "100%");
		ZKUpdateUtil.setHeight(paymentLayout, "100%");
		paymentLayout.setStyle("border: none");
		ZKUpdateUtil.setVflex(paymentPanel, "1");
		ZKUpdateUtil.setVflex(paymentLayout, "1");
		
		// invoice layout
		invoicePanel.appendChild(invoiceLayout);
		ZKUpdateUtil.setWidth(invoicePanel, "100%");
		ZKUpdateUtil.setHeight(invoicePanel, "100%");;
		ZKUpdateUtil.setWidth(invoiceLayout, "100%");
		ZKUpdateUtil.setHeight(invoiceLayout, "100%");
		ZKUpdateUtil.setVflex(invoicePanel, "1");
		ZKUpdateUtil.setVflex(invoiceLayout, "1");
		invoiceLayout.setStyle("border: none");
		
		// payment layout north - label
		north = new North();
		north.setBorder("none");
		paymentLayout.appendChild(north);
		north.appendChild(paymentLabel);
		ZKUpdateUtil.setVflex(paymentLabel, "min");
		// payment layout south - sum
		//	South south = new South();
		south = new South();
		south.setStyle("border: none");
		paymentLayout.appendChild(south);
		south.appendChild(paymentInfo.rightAlign());
		ZKUpdateUtil.setVflex(paymentInfo, "min");
		//payment layout center - payment list
		Center center = new Center();
		paymentLayout.appendChild(center);
		center.appendChild(paymentTable);
		ZKUpdateUtil.setWidth(paymentTable, "100%");
		//ZKUpdateUtil.setHeight(paymentTable, "100%");
		ZKUpdateUtil.setVflex(paymentTable, "1");
		center.setStyle("border: none");
		
		// invoice layout north - label
		north = new North();
		north.setBorder("none");
		invoiceLayout.appendChild(north);
		north.appendChild(invoiceLabel);
		ZKUpdateUtil.setVflex(invoiceLabel, "min");
		// invoice layout south - sum
		south = new South();
		south.setStyle("border: none");
		invoiceLayout.appendChild(south);
		south.appendChild(invoiceInfo.rightAlign());
		ZKUpdateUtil.setVflex(invoiceInfo, "min");
		// invoice layout center - invoice list
		center = new Center();
		invoiceLayout.appendChild(center);
		center.appendChild(invoiceTable);
		ZKUpdateUtil.setWidth(invoiceTable, "100%");
		ZKUpdateUtil.setVflex(invoiceTable, "1");
		center.setStyle("border: none");
		
		// mainlayout center - payment + invoice 
		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoPanel);
		ZKUpdateUtil.setHflex(infoPanel, "1");
		ZKUpdateUtil.setVflex(infoPanel, "1");
		
		infoPanel.setStyle("border: none");
		ZKUpdateUtil.setWidth(infoPanel, "100%");
		ZKUpdateUtil.setHeight(infoPanel, "100%");
		
		// north of mainlayout center - payment
		north = new North();
		north.setBorder("none");
		infoPanel.appendChild(north);
		north.appendChild(paymentPanel);
		north.setAutoscroll(true);
		north.setSplittable(true);
		north.setSize("50%");
		north.setCollapsible(true);

		// center of mainlayout center - invoice
		center = new Center();
		center.setStyle("border: none");
		infoPanel.appendChild(center);
		center.appendChild(invoicePanel);
		
		ZKUpdateUtil.setHflex(invoicePanel, "1");
		ZKUpdateUtil.setVflex(invoicePanel, "1");
	}


	/**
	 *  Dynamic Init (prepare dynamic fields)
	 *  @throws Exception if Lookups cannot be initialized
	 */
	public void dynInit() throws Exception
	{
		//  Currency
		int AD_Column_ID = COLUMN_C_INVOICE_C_CURRENCY_ID;    //  C_Invoice.C_Currency_ID
		MLookup lookupCur = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		currencyPick = new WTableDirEditor("C_Currency_ID", true, false, true, lookupCur);
		currencyPick.setValue(getC_Currency_ID());
		currencyPick.addValueChangeListener(this);

		// Organization filter selection
		AD_Column_ID = COLUMN_C_PERIOD_AD_ORG_ID; //C_Period.AD_Org_ID (needed to allow org 0)
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new WTableDirEditor("AD_Org_ID", true, false, true, lookupOrg);
		organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.addValueChangeListener(this);
		
		//  BPartner
		AD_Column_ID = COLUMN_C_INVOICE_C_BPARTNER_ID;        //  C_Invoice.C_BPartner_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new WSearchEditor("C_BPartner_ID", true, false, true, lookupBP);
		bpartnerSearch.addValueChangeListener(this);

		//  AMN_Employee
		AD_Column_ID = MColumn.getColumn_ID("AMN_Employee", "AMN_Employee_ID"); // (1000446)  AMN_Employee_ID.AMN_Employee
		MLookup lookupEMP = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		employeeSearch = new WSearchEditor("AMN_Employee_ID", true, false, true, lookupEMP);
		employeeSearch.addValueChangeListener(this);
		
		//  Status bar
		statusBar.appendChild(new Label(Msg.getMsg(Env.getCtx(), "AllocateStatus")));
		ZKUpdateUtil.setVflex(statusBar, "min");
		
		//  Default dateField to Login Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(Env.getContextAsDate(Env.getCtx(), Env.DATE));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		dateField.setValue(new Timestamp(cal.getTimeInMillis()));
		dateField.addValueChangeListener(this);

		//  Charge
		AD_Column_ID = 61804;    //  C_AllocationLine.C_Charge_ID
		MLookup lookupCharge = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		chargePick = new WTableDirEditor("C_Charge_ID", false, false, true, lookupCharge);
		chargePick.setValue(getC_Charge_ID());
		chargePick.addValueChangeListener(this);
		
		//  Doc Type
		AD_Column_ID = 212213;    //  C_AllocationLine.C_DocType_ID
		MLookup lookupDocType = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		DocTypePick = new WTableDirEditor("C_DocType_ID", false, false, true, lookupDocType);
		DocTypePick.setValue(getC_DocType_ID());
		DocTypePick.addValueChangeListener(this);		
		
		// Organization for Documents filter selection
		AD_Column_ID = COLUMN_C_PERIOD_AD_ORG_ID; //C_Period.AD_Org_ID (needed to allow org 0)
		MLookup lookupDocOrg = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		orgDocPick = new WTableDirEditor("AD_Org_ID_Doc", true, false, true, lookupOrg);
		m_docAD_Org_ID = 0;
		orgDocPick.setValue(m_docAD_Org_ID);
		orgDocPick.addValueChangeListener(this);
		
		// DateDoc
		Timestamp lastDayOfMonth = null;
		cal = Calendar.getInstance();
		Date ctxDate = Env.getContextAsDate(Env.getCtx(), "#Date");

		if (ctxDate != null) {
		    cal.setTime(ctxDate);
		    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		} else {
		    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}

		lastDayOfMonth = new Timestamp(cal.getTimeInMillis());
		dateDocField.setValue(lastDayOfMonth);
		dateDocField.addValueChangeListener(this);
		m_DateDoc = lastDayOfMonth;
		
		// Payroll Process
		Integer defaultProcessID = getDefaultAMNProcessID(); // Tu método para obtener el valor inicial
		List<KeyNamePair> validProcesses = getValidProcesses();

		if (!validProcesses.isEmpty()) {
		    processListbox = new Listbox();
		    processListbox.setMold("select"); // Combo desplegable

		    // Agregar elemento vacío
		    Listitem blankItem = new Listitem();
		    blankItem.setValue(null);
		    blankItem.appendChild(new Listcell(""));
		    processListbox.appendChild(blankItem);

		    int indexToSelect = 0; // Por defecto, seleccionar el elemento en blanco

		    for (int i = 0; i < validProcesses.size(); i++) {
		        KeyNamePair pair = validProcesses.get(i);
		        Listitem item = new Listitem();
		        item.setValue(pair.getKey());  // AMN_Process_ID
		        item.appendChild(new Listcell(pair.getName()));
		        processListbox.appendChild(item);

		        // Verifica si este es el proceso por defecto
		        if (pair.getKey() == defaultProcessID) {
		            indexToSelect = i + 1; // +1 porque el índice 0 es el item en blanco
		        }
		    }

		    processListbox.setSelectedIndex(indexToSelect); // Seleccionar el proceso por defecto
		    Events.postEvent("onSelect", processListbox, null);

		    processListbox.addEventListener("onSelect", new EventListener<Event>() {
		        @Override
		        public void onEvent(Event event) throws Exception {
		            Listitem selectedItem = processListbox.getSelectedItem();
		            Integer selectedProcessID = selectedItem != null ? (Integer) selectedItem.getValue() : null;

		            if (selectedProcessID != null) {
		            	m_AMN_Process_ID = selectedProcessID;   
		            } else {
		            	m_AMN_Process_ID = 0;
		            }
		            refresh();
		        }
		    });
		} else {
		    log.warning("No se encontraron procesos válidos.");
		}

		// Payroll Contract
		// Obtener el rol del usuario actual desde el contexto
		int roleID = Env.getAD_Role_ID(Env.getCtx());
		
		Integer defaultContractID = getFirstActiveContractID(roleID); // Si tienes uno por defecto
		List<KeyNamePair> validContracts = getValidContracts(roleID);  // Lista de Contratos Valido

		if (!validContracts.isEmpty()) {
		    contractListbox = new Listbox();
		    contractListbox.setMold("select");  // Combo desplegable

		    // Elemento vacío al inicio
		    Listitem blankItem = new Listitem();
		    blankItem.setValue(null);
		    blankItem.appendChild(new Listcell(""));
		    contractListbox.appendChild(blankItem);

		    int indexToSelect = 0;  // Por defecto, ítem en blanco
		    
		    for (int i = 0; i < validContracts.size(); i++) {
		        KeyNamePair pair = validContracts.get(i);
		        Listitem item = new Listitem();
		        item.setValue(pair.getKey());  // ID
		        item.appendChild(new Listcell(pair.getName()));  // Nombre del contrato
		        contractListbox.appendChild(item);

		        // Selección automática si coincide con valor por defecto
		        if (pair.getKey() == defaultContractID) {
		            indexToSelect = i + 1; // +1 por el ítem en blanco
		        }
		    }

		    contractListbox.setSelectedIndex(indexToSelect);
		    Events.postEvent("onSelect", contractListbox, null);
		    
		    contractListbox.addEventListener("onSelect", new EventListener<Event>() {
		        @Override
		        public void onEvent(Event event) throws Exception {
		            Listitem selectedItem = contractListbox.getSelectedItem();
		            Integer selectedContractID = selectedItem != null ? (Integer) selectedItem.getValue() : null;

		            if (selectedContractID != null) {
		            	m_AMN_Contract_ID = selectedContractID;
		            } else {
		            	m_AMN_Contract_ID = 0;
		            }
		            refresh();
		        }
		    });
		} else {
		    log.warning("No se encontraron contratos válidos.");
		}


		// Payroll Document Type
		// Obtener los tipos de documento válidos
		List<Integer> validDocTypeIDs = getValidDocTypeIDs();

		// Si hay tipos de documento válidos
		if (!validDocTypeIDs.isEmpty()) {
		    // Crear un Listbox y convertirlo en un combo desplegable
		    docTypeListbox = new Listbox();
		    docTypeListbox.setMold("select");  // Esto hace que el Listbox se comporte como un combo desplegable

		    // Primer item en blanco para permitir que se pueda dejar en blanco
		    Listitem blankItem = new Listitem();
		    blankItem.setValue(null);  // Valor vacío
		    blankItem.appendChild(new Listcell(""));  // Texto vacío para el primer item
		    docTypeListbox.appendChild(blankItem);

		    // Iterar sobre los tipos de documento válidos y agregar el nombre
		    for (Integer docTypeID : validDocTypeIDs) {
		        // Obtener el nombre del tipo de documento
		        String docTypeName = getDocTypeName(docTypeID);  // Método que obtiene el nombre

		        Listitem item = new Listitem();
		        item.setValue(docTypeID);  // Valor de cada item (ID)
		        item.appendChild(new Listcell(docTypeName));  // Mostrar el nombre en el Listcell
		        docTypeListbox.appendChild(item);  // Agregar el item al Listbox
		    }

		    // Establecer el valor por defecto (puedes personalizar esto)
		    docTypeListbox.setSelectedIndex(0);  // Asignar el primer valor como selección por defecto
		    Events.postEvent("onSelect", docTypeListbox, null);

		    // Agregar el listener para cambios de valor
		    docTypeListbox.addEventListener("onSelect", new EventListener<Event>() {
		        @Override
		        public void onEvent(Event event) throws Exception {
		            // Obtener el valor seleccionado
		            Listitem selectedItem = docTypeListbox.getSelectedItem();
		            Integer selectedDocTypeID = selectedItem != null ? (Integer) selectedItem.getValue() : null;

		            // Realizar acciones con el valor seleccionado
		            if (selectedDocTypeID != null) {
		            	m_C_DocType_ID = selectedDocTypeID;
		            } else {
		            	m_C_DocType_ID = 0;
		            }
		            refresh();
		        }
		    });
		} else {
		    // Si no hay tipos válidos, puedes manejarlo como un error o mostrar un mensaje
			log.warning("No se encontraron tipos de documento válidos.");
		}

	}   //  dynInit
	
	/**
	 * Handle onClientInfo event from browser.
	 */
	protected void onClientInfo()
	{
		if (ClientInfo.isMobile() && form.getPage() != null) 
		{
			if (noOfColumn > 0 && parameterLayout.getRows() != null)
			{
				int t = 6;
				if (maxWidth(MEDIUM_WIDTH-1))
				{
					if (maxWidth(SMALL_WIDTH-1))
						t = 2;
					else
						t = 4;
				}
				if (t != noOfColumn)
				{
					parameterLayout.getRows().detach();
					if (parameterLayout.getColumns() != null)
						parameterLayout.getColumns().detach();
					if (mainLayout.getSouth() != null)
						mainLayout.getSouth().detach();
					if (allocationLayout.getRows() != null)
						allocationLayout.getRows().detach();
	
					form.invalidate();
				}
			}
		}
	}
	
	/**
	 *  Event listener
	 *  @param e event
	 */
	@Override
	public void onEvent(Event e)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("");
		if (e.getTarget().equals(multiCurrency))
			loadBPartner();
		//	Allocate
		else if (e.getTarget().equals(allocateButton))
		{
			allocateButton.setEnabled(false);
			MAllocationHdr allocation = saveData();
			loadBPartner();
			allocateButton.setEnabled(true);
			if (allocation != null) 
			{
				DocumentLink link = new DocumentLink(Msg.getElement(Env.getCtx(), MAllocationHdr.COLUMNNAME_C_AllocationHdr_ID) + ": " + allocation.getDocumentNo(), allocation.get_Table_ID(), allocation.get_ID());				
				statusBar.appendChild(link);
			}					
		}
		//	Reset Button
		else if (e.getTarget().equals(resetButton))
		{
			statusBar.getChildren().clear();
			invoiceTable.clear();
			paymentTable.clear();
			chargePick.setValue(null);
			parameterPanel.setFocus(true);
		}
		//	Select/Deselect
		else if (e.getTarget().equals(selectButton))
		{
			if (this.m_SelectStatus) {
				m_SelectStatus = false;
				invoiceSetResetSelection( invoiceTable,  autoWriteOff.isSelected(), false, m_SelectStatus);
				selectButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "SelectAll")));
				selectButton.setImage(ThemeManager.getThemeResource("images/SelectAll16.png"));
				log.warning("Select/Deselect pressed -------"+m_SelectStatus);
			} else {
				m_SelectStatus = true;
				selectButton.setLabel(Util.cleanAmp(Msg.getMsg(Env.getCtx(), "DeSelectAll")));
				selectButton.setImage(ThemeManager.getThemeResource("images/DeSelectAll16.png"));
				log.warning("Select/Deselect pressed -------"+m_SelectStatus);
			}
			// Execute St/Reset
			invoiceSetResetSelection( invoiceTable,  autoWriteOff.isSelected(), multiCurrency.isSelected(), m_SelectStatus);
		}
		else if (e.getTarget().equals(refreshButton))
		{
			loadBPartner();
		}
	}

	/**
	 *  Table Model Listener for {@link #paymentTable} and {@link #invoiceTable}
	 *  - Recalculate Totals
	 *  @param e event
	 */
	@Override
	public void tableChanged(WTableModelEvent e)
	{
		boolean isUpdate = (e.getType() == WTableModelEvent.CONTENTS_CHANGED);
		//  Not a table update
		if (!isUpdate)
		{
			calculate();
			return;
		}
		
		int row = e.getFirstRow();
		int col = e.getColumn();
	
		if (row < 0)
			return;
		
		boolean isInvoice = (e.getModel().equals(invoiceTable.getModel()));
		boolean isAutoWriteOff = autoWriteOff.isSelected();
		
		String msg = writeOff(row, col, isInvoice, paymentTable, invoiceTable, isAutoWriteOff);
		
		//render row
		ListModelTable model = isInvoice ? invoiceTable.getModel() : paymentTable.getModel(); 
		model.updateComponent(row);
	    
		if(msg != null && msg.length() > 0)
			Dialog.warn(form.getWindowNo(), "AllocationWriteOffWarn");
		
		calculate();
	}   //  tableChanged
	
	/**
	 *  Value change listener for parameter and allocation fields.
	 *  @param e event
	 */
	@Override
	public void valueChange (ValueChangeEvent e)
	{
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		Object source = e.getSource();
		if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
		if (value == null &&
				   !(name.equals("C_Charge_ID") || name.equals("C_Activity_ID") || name.equals("C_Project_ID") ||
				     name.equals("AMN_Employee_ID") || name.equals("C_BPartner_ID") || name.equals("Date") ||
				     name.equals("AMN_Process_ID") || name.equals("AMN_Contract_ID") || name.equals("C_DocType_ID")))
					return;
		
		// Organization
		if (name.equals("AD_Org_ID"))
		{
			setAD_Org_ID((int) value);
			
			loadBPartner();
		}
		//		Charge
		else if (name.equals("C_Charge_ID") )
		{
			setC_Charge_ID(value!=null? ((Integer) value).intValue() : 0);
			
			setAllocateButton();
		}

		else if (name.equals("C_DocType_ID") )
		{
			setC_DocType_ID(value!=null? ((Integer) value).intValue() : 0);			
		}

		//  BPartner
		if (name.equals("C_BPartner_ID"))
		{
			 // Verifica si el valor es nulo o vacío
	        if (value == null || value.toString().trim().isEmpty()) {
	        	m_C_BPartner_ID = 0;  // Establecer a 0 si el valor está vacío
	        } else {
	        	m_C_BPartner_ID = ((Integer)value).intValue();  // Establecer el valor si no está vacío
	        }
			bpartnerSearch.setValue(value);
			setC_BPartner_ID(m_C_BPartner_ID);
			loadBPartner();
		}
		//  Employee
		if (e.getSource().equals(employeeSearch))
		{
			 // Verifica si el valor es nulo o vacío
	        if (value == null || value.toString().trim().isEmpty()) {
	            m_AMN_Employee_ID = 0;  // Establecer a 0 si el valor está vacío
	        } else {
	            m_AMN_Employee_ID = ((Integer)value).intValue();  // Establecer el valor si no está vacío
	        }
			employeeSearch.setValue(value);
			loadBPartner();
		}
		//	Currency
		else if (name.equals("C_Currency_ID"))
		{
			setC_Currency_ID((int) value);
			loadBPartner();
		}
		//	Date for Multi-Currency
		else if (name.equals("Date") && multiCurrency.isSelected())
		{
			loadBPartner();
		}
		//  Payroll Process
		else if (e.getSource().equals(processListbox))
		{
				log.warning("Cambio Proceso....");
		}
		//  Payroll Contract
		else if (e.getSource().equals(contractListbox))
		{
			log.warning("Cambio Contrato....");
		}
		//  Document Type
		else if (e.getSource().equals(docTypeListbox))
		{
			log.warning("Cambio Tipo de Documento....");
		}
		// DateDoc
		else if (name.equals("Date"))
		{
			if (source == dateField) {
		        // Acción para dateField
				m_Date = dateDocField.getValue();
				log.warning("Cambio Date Doc ...."+m_Date);
		    } else if (source == dateDocField) {
		        // Acción para dateDocField
		    	m_DateDoc = dateDocField.getValue();
				log.warning("Cambio Date Doc ...."+m_DateDoc);
		    }
			loadBPartner();
			setAllocateButton();
		}
	}   //  vetoableChange
	
	/**
	 * Set {@link #allocateButton} to enable or disable.
	 */
	private void setAllocateButton() {
		if (isOkToAllocate() )
		{
			allocateButton.setEnabled(true);
		}
		else
		{
			allocateButton.setEnabled(false);
		}

		if ( getTotalDifference().signum() == 0 )
		{
			chargePick.setValue(null);
			setC_Charge_ID(0);
   		}
	}

	/**
	 *  Load Business Partner Info.
	 *  <ul>
	 *  <li>Payments</li>
	 *  <li>Invoices</li>
	 *  </ul>
	 */
	private void loadBPartner ()
	{
		checkBPartner();
		
		Vector<Vector<Object>> data = getPaymentData(multiCurrency.isSelected(), dateField.getValue(), (String)null);
		Vector<String> columnNames = getPaymentColumnNames(multiCurrency.isSelected());
		
		paymentTable.clear();
		
		//  Remove previous listeners
		paymentTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelP = new ListModelTable(data);
		modelP.addTableModelListener(this);
		paymentTable.setData(modelP, columnNames);
		setPaymentColumnClass(paymentTable, multiCurrency.isSelected());
		//

		data = getInvoiceData(multiCurrency.isSelected(), dateField.getValue(), (String)null);
		columnNames = getInvoiceColumnNames(multiCurrency.isSelected());
		
		invoiceTable.clear();
		
		//  Remove previous listeners
		invoiceTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelI = new ListModelTable(data);
		modelI.addTableModelListener(this);
		invoiceTable.setData(modelI, columnNames);
		setInvoiceColumnClass(invoiceTable, multiCurrency.isSelected());
		//
		
		//  Calculate Totals
		calculate();
		
		statusBar.getChildren().clear();
	}   //  loadBPartner
	
	/**
	 * perform allocation calculation
	 */
	public void calculate()
	{
		calculate(paymentTable, invoiceTable, multiCurrency.isSelected());
		
		paymentInfo.setText(getPaymentInfoText());
		invoiceInfo.setText(getInvoiceInfoText());
		differenceField.setText(format.format(getTotalDifference()));
		
		//	Set AllocationDate
		if (allocDate != null) {
			if (! allocDate.equals(dateField.getValue())) {
                Clients.showNotification(Msg.getMsg(Env.getCtx(), "AllocationDateUpdated"), Clients.NOTIFICATION_TYPE_INFO, dateField.getComponent(), "start_before", -1, false);       
                dateField.setValue(allocDate);
			}
		}

		//  Set Allocation Currency
		allocCurrencyLabel.setText(currencyPick.getDisplay());				

		setAllocateButton();
	}

	private void refresh ()
	{
		
		// Mantener los valores actuales de las variables antes de refrescar
	    m_C_BPartner_ID = (bpartnerSearch.getValue() != null) ? ((Integer)bpartnerSearch.getValue()).intValue() : 0;
	    m_AMN_Employee_ID = (employeeSearch.getValue() != null) ? ((Integer)employeeSearch.getValue()).intValue() : 0;
	    // Obtener IDs seleccionados en los listboxes
	    Listitem selectedProcessItem = processListbox.getSelectedItem();
	    Integer selectedProcessID = selectedProcessItem != null ? (Integer) selectedProcessItem.getValue() : null;
	    m_AMN_Process_ID =  (selectedProcessID != null) ? (Integer) selectedProcessID : 0;
	    // AMN_Contract
	    Listitem selectedContractItem = contractListbox.getSelectedItem();
	    Integer selectedContractID = selectedContractItem != null ? (Integer) selectedContractItem.getValue() : null;
	    m_AMN_Contract_ID = (selectedContractID != null) ? (Integer) selectedContractID : 0;
	    // C_DocType
	    Listitem selectedDocTypeItem = docTypeListbox.getSelectedItem();
	    Integer selectedDocTypeID = selectedDocTypeItem != null ? (Integer) selectedDocTypeItem.getValue() : null;
	    m_C_DocType_ID = selectedDocTypeID != null ? selectedDocTypeID : 0;
	    m_DateDoc = dateDocField.getValue();
	    // Refrescar las tablas y demás elementos
		loadBPartner();
		
	}
	
	/**
	 * Save Data to C_AllocationHdr and C_AllocationLine.
	 */
	private MAllocationHdr saveData()
	{
		if (getAD_Org_ID() > 0)
			Env.setContext(Env.getCtx(), form.getWindowNo(), "AD_Org_ID", getAD_Org_ID());
		else
			Env.setContext(Env.getCtx(), form.getWindowNo(), "AD_Org_ID", "");
		try
		{
			final MAllocationHdr[] allocation = new MAllocationHdr[1];
			Trx.run(new TrxRunnable() 
			{
				public void run(String trxName)
				{
					statusBar.getChildren().clear();
					allocation[0] = saveData(form.getWindowNo(), dateField.getValue(), paymentTable, invoiceTable, trxName);
					
				}
			});
			
			return allocation[0];
		}
		catch (Exception e)
		{
			Dialog.error(form.getWindowNo(), "Error", e.getLocalizedMessage());
			return null;
		}
	}   //  saveData
	
	/**
	 * Called by org.adempiere.webui.panel.ADForm.openForm(int)
	 * @return {@link ADForm}
	 */
	@Override
	public ADForm getForm()
	{
		return form;
	}
}   //  VAllocation

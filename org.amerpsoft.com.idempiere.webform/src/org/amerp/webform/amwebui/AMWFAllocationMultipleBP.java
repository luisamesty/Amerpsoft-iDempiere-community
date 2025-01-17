
package org.amerp.webform.amwebui;

import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_BPARTNER_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_INVOICE_C_CURRENCY_ID;
import static org.compiere.model.SystemIDs.COLUMN_C_PERIOD_AD_ORG_ID;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
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
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.amerp.webform.amwgrid.AMFPayAllocationMultipleBP;
import org.amerp.webform.amwmodel.OrgInfo;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.compiere.util.Util;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.North;
import org.zkoss.zul.Separator;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;

/**
 * Allocation Form
 *
 * @author  Jorg Janke
 * @version $Id: VAllocation.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 * 
 * Original Contributor : Fabian Aguilar - Multi Business Partner
 * Luis Amesty: Two Allocations generated using a common Charge 
 */
public class AMWFAllocationMultipleBP extends AMFPayAllocationMultipleBP
	implements IFormController, EventListener<Event>, WTableModelListener, ValueChangeListener
{

	private CustomForm form = new CustomForm();

	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame frame
	 */
	public AMWFAllocationMultipleBP()
	{
		// See NF8.2 Inject Context Variables From Menu
		//IsSOTrx = Env.getContext(Env.getCtx(), "+IsSOTrx");
		//IsMultipleAllocation = Env.getContext(Env.getCtx(), m_WindowNo, "+IsMultpleAllocation");
		//IsMultipleAllocation = Env.getContext(Env.getCtx(),  "+IsMultpleAllocation");
		// Env.setContext(Env.getCtx(), form.getWindowNo(), "IsSOTrx", IsSOTrx);   //  defaults to no
		// Env.setContext(Env.getCtx(), form.getWindowNo(), "IsSOTrx", "Y");   //  defaults to no
		log.warning("Initial IsSOTrx="+IsSOTrx + "  IsMultipleAllocation="+IsMultipleAllocation);
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
	
	//
	private Borderlayout mainLayout = new Borderlayout();
	private Panel parameterPanel = new Panel();
	private Panel allocationPanel = new Panel();
	private Grid parameterLayout = GridFactory.newGridLayout();
	private Label bpartnerLabel = new Label();
	private WSearchEditor bpartnerSearch = null;
	private Label bpartnerLabel2 = new Label();
	private WSearchEditor bpartnerSearch2 = null;
	private WListbox invoiceTable = ListboxFactory.newDataTable();
	private WListbox paymentTable = ListboxFactory.newDataTable();
	private Borderlayout infoPanel = new Borderlayout();
	private Panel paymentPanel = new Panel();
	private Panel invoicePanel = new Panel();
	private Label paymentLabel = new Label();
	private Label invoiceLabel = new Label();
	private Borderlayout paymentLayout = new Borderlayout();
	private Borderlayout invoiceLayout = new Borderlayout();
	private Label paymentInfo = new Label();
	private Label invoiceInfo = new Label();
	private Grid allocationLayout = GridFactory.newGridLayout();
	private Label differenceLabel = new Label();
	private Textbox differenceField = new Textbox();
	private Button allocateButton = new Button();
	private Label descriptionLabel = new Label();
	private Textbox descriptionField = new Textbox();
	private Button refreshButton = new Button();
	private Button cancelButton = new Button();
	private Button resetButton = new Button();
	private Button zoomButton = new Button();
	private Label currencyLabel = new Label();
	private WTableDirEditor currencyPick = null;
	private Checkbox multiCurrency = new Checkbox();
	private Label chargeLabel = new Label();
	private WTableDirEditor chargePick = null;
	private Label allocCurrencyLabel = new Label();
	private Hlayout statusBar = new Hlayout();
	private Label dateLabel = new Label();
	private WDateEditor dateField = new WDateEditor();
	private Checkbox autoWriteOff = new Checkbox();
	private Label organizationLabel = new Label();
	private WTableDirEditor organizationPick;
	private Label activityLabel = new Label();
	private Label projectLabel = new Label();
	private WTableDirEditor activityPick = null;
	private WTableDirEditor projectPick = null;
	private Panel southPanel = new Panel();

	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void zkInit() throws Exception
	{
		//
		form.appendChild(mainLayout);
		//mainLayout.setWidth("99%");
		//mainLayout.setHeight("100%");
		ZKUpdateUtil.setWidth(mainLayout, "100%");
		ZKUpdateUtil.setHeight(mainLayout, "100%");
		dateLabel.setText(Msg.getMsg(Env.getCtx(), "Date"));
		autoWriteOff.setSelected(false);
		autoWriteOff.setText(Msg.getMsg(Env.getCtx(), "AutoWriteOff", true));
		autoWriteOff.setTooltiptext(Msg.getMsg(Env.getCtx(), "AutoWriteOff", false));
		//
		parameterPanel.appendChild(parameterLayout);
		allocationPanel.appendChild(allocationLayout);
		bpartnerLabel.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID")+" ("+Msg.translate(Env.getCtx(), "C_Payment_ID")+"s)");
		bpartnerLabel2.setText(Msg.translate(Env.getCtx(), "C_BPartner_ID")+" ("+Msg.translate(Env.getCtx(), "C_Invoice_ID")+"s)");
		paymentLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Payment_ID"));
		invoiceLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Invoice_ID"));
		paymentPanel.appendChild(paymentLayout);
		invoicePanel.appendChild(invoiceLayout);
		invoiceInfo.setText(".");
		paymentInfo.setText(".");
		chargeLabel.setText(" " + Msg.translate(Env.getCtx(), "C_Charge_ID"));
		differenceLabel.setText(Msg.getMsg(Env.getCtx(), "Difference"));
		differenceField.setText("0");
		differenceField.setReadonly(true);
		differenceField.setStyle("text-align: right");
		// Description Field
		descriptionLabel.setText(Msg.translate(Env.getCtx(), "description"));
		descriptionField.setText("");
		descriptionField.setReadonly(false);
		descriptionField.setStyle("text-align: left");
		// Activity
		activityLabel.setText(Msg.translate(Env.getCtx(), "C_Activity_ID"));
		// Project
		projectLabel.setText(Msg.translate(Env.getCtx(), "C_Project_ID"));
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
		
		North north = new North();
		north.setStyle("border: none");
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);
		
		Rows rows = null;
		Row row = null;
		
		//parameterLayout.setWidth("80%");
		ZKUpdateUtil.setWidth(parameterLayout,"80%");
		rows = parameterLayout.newRows();
		row = rows.newRow();
		row.appendCellChild(dateLabel.rightAlign());
		row.appendCellChild(dateField.getComponent());
		row.appendCellChild(organizationLabel.rightAlign());
		
		//organizationPick.getComponent().setHflex("true");
		ZKUpdateUtil.setHflex(organizationPick.getComponent(), "true");
		row.appendCellChild(organizationPick.getComponent(),2);
		row = rows.newRow();
		row.appendCellChild(bpartnerLabel.rightAlign());
		//bpartnerSearch.getComponent().setHflex("true");
		ZKUpdateUtil.setHflex(bpartnerSearch.getComponent(), "true");
		row.appendCellChild(bpartnerSearch.getComponent(),2);
		row.appendCellChild(bpartnerLabel2.rightAlign());
		//bpartnerSearch2.getComponent().setHflex("true");
		ZKUpdateUtil.setHflex(bpartnerSearch2.getComponent(), "true");
		row.appendCellChild(bpartnerSearch2.getComponent(),2);
		
		row = rows.newRow();
		row.appendCellChild(currencyLabel.rightAlign(),1);
		//currencyPick.getComponent().setHflex("true");
		ZKUpdateUtil.setHflex(currencyPick.getComponent(), "true");
		row.appendCellChild(currencyPick.getComponent(),1);		
		row.appendCellChild(multiCurrency,1);		
		row.appendCellChild(autoWriteOff,2);
		row.appendCellChild(new Space(),1);		
		
		South south = new South();
		south.setStyle("border: none");
		mainLayout.appendChild(south);
		south.appendChild(southPanel);
		southPanel.appendChild(allocationPanel);
		allocationPanel.appendChild(allocationLayout);
		
		//allocationLayout.setHflex("min");
		// OJO ZKUpdateUtil.setHflex(allocationLayout, "min");
		rows = allocationLayout.newRows();
		row = rows.newRow();
		row.appendCellChild(refreshButton);
		row.appendCellChild(resetButton);
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
		//descriptionLabel.setHflex("min");
		ZKUpdateUtil.setHflex(descriptionLabel, "min");
		row.appendCellChild(descriptionLabel.rightAlign());
		//descriptionField.setHflex("true");
		ZKUpdateUtil.setHflex(descriptionField, "min");
		row.appendCellChild(descriptionField,2);
		
// OJO
		row = rows.newRow();
		row.appendCellChild(activityLabel.rightAlign());
		ZKUpdateUtil.setHflex(activityPick.getComponent(), "true");
		row.appendCellChild(activityPick.getComponent(),2);
		row.appendCellChild(projectLabel.rightAlign());
		ZKUpdateUtil.setHflex(projectPick.getComponent(), "true");
		row.appendCellChild(projectPick.getComponent(),2);
		
		//allocateButton.setHflex("true");
		ZKUpdateUtil.setHflex(allocateButton, "true");
		row.appendCellChild(allocateButton);
		row.appendCellChild(cancelButton);
// OJO
		
		paymentPanel.appendChild(paymentLayout);
		//paymentPanel.setWidth("100%");
		//paymentPanel.setHeight("100%");
		ZKUpdateUtil.setWidth(paymentPanel, "100%");
		ZKUpdateUtil.setHeight(paymentPanel, "100%");
		//paymentLayout.setWidth("100%");
		//paymentLayout.setHeight("100%");
		ZKUpdateUtil.setWidth(paymentLayout, "100%");
		ZKUpdateUtil.setHeight(paymentLayout, "100%");
		paymentLayout.setStyle("border: none");
		// Optional from WAllocation.java
		ZKUpdateUtil.setVflex(paymentPanel, "1");
		ZKUpdateUtil.setVflex(paymentLayout, "1");
		
		invoicePanel.appendChild(invoiceLayout);
		//invoicePanel.setWidth("100%");
		//invoicePanel.setHeight("100%");
		ZKUpdateUtil.setWidth(invoicePanel, "100%");
		ZKUpdateUtil.setHeight(invoicePanel, "100%");
		//invoiceLayout.setWidth("100%");
		//invoiceLayout.setHeight("100%");
		ZKUpdateUtil.setWidth(invoiceLayout, "100%");
		ZKUpdateUtil.setHeight(invoiceLayout, "100%");
		ZKUpdateUtil.setVflex(invoicePanel, "1");
		ZKUpdateUtil.setVflex(invoiceLayout, "1");
		invoiceLayout.setStyle("border: none");
		
		north = new North();
		north.setStyle("border: none");
		paymentLayout.appendChild(north);
		north.appendChild(paymentLabel);
		ZKUpdateUtil.setVflex(paymentLabel, "min");
		
		south = new South();
		south.setStyle("border: none");
		paymentLayout.appendChild(south);
		south.appendChild(paymentInfo.rightAlign());
		ZKUpdateUtil.setVflex(paymentInfo, "min");
		
		Center center = new Center();
		paymentLayout.appendChild(center);
		center.appendChild(paymentTable);
		//paymentTable.setWidth("99%");
		//paymentTable.setHeight("99%");
		ZKUpdateUtil.setWidth(paymentTable, "100%");
		//ZKUpdateUtil.setHeight(paymentTable, "100%");
		ZKUpdateUtil.setVflex(paymentTable, "1");
		center.setStyle("border: none");
		
		north = new North();
		north.setStyle("border: none");
		invoiceLayout.appendChild(north);
		north.appendChild(invoiceLabel);
		ZKUpdateUtil.setVflex(invoiceLabel, "min");		
		
		south = new South();
		south.setStyle("border: none");
		invoiceLayout.appendChild(south);
		south.appendChild(invoiceInfo.rightAlign());
		ZKUpdateUtil.setVflex(invoiceInfo, "min");
		
		center = new Center();
		invoiceLayout.appendChild(center);
		center.appendChild(invoiceTable);
		//invoiceTable.setWidth("99%");
		//invoiceTable.setHeight("99%");
		ZKUpdateUtil.setWidth(invoiceTable, "100%");
		//ZKUpdateUtil.setHeight(invoiceTable, "100%");
		ZKUpdateUtil.setVflex(invoiceTable, "1");
		center.setStyle("border: none");
		//
		center = new Center();
		mainLayout.appendChild(center);
		center.appendChild(infoPanel);
		
		//infoPanel.setHflex("1");
		//infoPanel.setVflex("1");
		ZKUpdateUtil.setHflex(infoPanel, "1");
		ZKUpdateUtil.setVflex(infoPanel, "1");
		
		infoPanel.setStyle("border: none");
		//infoPanel.setWidth("100%");
		//infoPanel.setHeight("100%");
		ZKUpdateUtil.setWidth(infoPanel, "100%");
		ZKUpdateUtil.setHeight(infoPanel, "100%");
		
		north = new North();
		north.setStyle("border: none");
		//north.setHeight("49%");
		ZKUpdateUtil.setHeight(north,"49%");
		infoPanel.appendChild(north);
		north.appendChild(paymentPanel);
		north.setSplittable(true);
		center = new Center();
		center.setStyle("border: none");
		infoPanel.appendChild(center);
		center.appendChild(invoicePanel);
		
		//invoicePanel.setHflex("1");
		//invoicePanel.setVflex("1");
		ZKUpdateUtil.setHflex(invoicePanel, "1");
		ZKUpdateUtil.setVflex(invoicePanel, "1");
		
	}   //  jbInit

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
		currencyPick.setValue(new Integer(m_C_Currency_ID));
		currencyPick.addValueChangeListener(this);

		// Organization filter selection
		AD_Column_ID = COLUMN_C_PERIOD_AD_ORG_ID; //C_Period.AD_Org_ID (needed to allow org 0)
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new WTableDirEditor("AD_Org_ID", true, false, true, lookupOrg);
//		organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
//		organizationPick.addValueChangeListener(this);
		// Organization filter selection
		// Organization IF Env AD_Org_ID = 0 load all Orgs ELSE load one Org
		ArrayList<OrgInfo> orgData = getOrgData(Env.getAD_Org_ID(Env.getCtx()));
		for(OrgInfo oi : orgData) {
			m_AD_Org_ID = oi.getAD_Org_ID();	
		}
		//organizationPick.setValue(Env.getAD_Org_ID(Env.getCtx()));
		organizationPick.setValue(m_AD_Org_ID);
		organizationPick.addValueChangeListener(this);
		//  BPartner
		AD_Column_ID = COLUMN_C_INVOICE_C_BPARTNER_ID;        //  C_Invoice.C_BPartner_ID
		MLookup lookupBP = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch = new WSearchEditor("C_BPartner_ID", true, false, true, lookupBP);
		bpartnerSearch.addValueChangeListener(this);
		
	    //  BPartner2
		AD_Column_ID = COLUMN_C_INVOICE_C_BPARTNER_ID;        //  C_Invoice.C_BPartner_ID
		MLookup lookupBP2 = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.Search);
		bpartnerSearch2 = new WSearchEditor("C_BPartner_ID", true, false, true, lookupBP2);
		bpartnerSearch2.addValueChangeListener(this);

		//  Translation
		statusBar.appendChild(new Label(Msg.getMsg(Env.getCtx(), "AllocateStatus")));
		//statusBar.setVflex("min");
		ZKUpdateUtil.setVflex(statusBar, "min");
		
		//  Date set to Login Date
		dateField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		dateField.addValueChangeListener(this);

		//  Charge
		AD_Column_ID = MColumn.getColumn_ID("C_AllocationLine", "C_Charge_ID");
//		AD_Column_ID = 61804;    //  C_AllocationLine.C_Charge_ID
		MLookup lookupCharge = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		chargePick = new WTableDirEditor("C_Charge_ID", false, false, true, lookupCharge);
		chargePick.setValue(m_C_Charge_ID);
		chargePick.addValueChangeListener(this);

		// Activity
		AD_Column_ID = MColumn.getColumn_ID("C_AllocationLine", "C_Activity_ID"); // C_AllocationLine.C_Activity_ID 
		MLookup lookupActivity = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		activityPick = new WTableDirEditor("C_Activity_ID", false, false, true, lookupActivity);
		activityPick.setValue(m_C_Activity_ID);
		activityPick.addValueChangeListener(this);
		activityPick.setMandatory(true);
		
		// Project
		AD_Column_ID = MColumn.getColumn_ID("C_AllocationLine", "C_Project_ID"); // C_AllocationLine.C_Project_ID 
		MLookup lookupProject = MLookupFactory.get (Env.getCtx(), form.getWindowNo(), 0, AD_Column_ID, DisplayType.TableDir);
		projectPick = new WTableDirEditor("C_Project_ID", false, false, true, lookupProject);
		projectPick.setValue(m_C_Project_ID);
		projectPick.addValueChangeListener(this);	;
	}   //  dynInit
	
	/**************************************************************************
	 *  Action Listener.
	 *  - MultiCurrency
	 *  - Allocate
	 *  @param e event
	 */
	public void onEvent(Event e)
	{
		log.config("");
		if (e.getTarget().equals(multiCurrency))
		{
			loadBPartner1();
			loadBPartner2();
		}
//		Reset
		else if (e.getTarget().equals(resetButton))
		{
			statusBar.getChildren().clear();
			bpartnerSearch.setValue("");
			bpartnerSearch2.setValue("");
			invoiceTable.clear();
			paymentTable.clear();
			descriptionField.setValue("");
			m_C_BPartner_ID=0;
			m_C_BPartner2_ID=0;
			m_C_Activity_ID=0;
			m_C_Project_ID=0;
			chargePick.setValue(null);
			projectPick.setValue(null);
			parameterPanel.setFocus(true);
		}
		//	Allocate
		else if (e.getTarget().equals(allocateButton))
		{
			allocateButton.setEnabled(false);
			MAllocationHdr[] allocationPair = saveData();
			MAllocationHdr allocation = null;
			loadBPartner1();
			loadBPartner2();
			allocateButton.setEnabled(true);
			// Get result Allocation Headers
			allocationPair = getAllocationHeader();
			if (allocationPair != null) 
			{
				// Allocation N 1
				allocation = allocationPair[0];
				//A link = new A(allocation.getDocumentNo());
				A link0 = new A(allocationPair[0].getDocumentNo());
				link0.setAttribute("Record_ID", allocation.get_ID());
				link0.setAttribute("AD_Table_ID", allocation.get_Table_ID());
				link0.addEventListener(Events.ON_CLICK, new EventListener<Event>() 
						{
					@Override
					public void onEvent(Event event) throws Exception 
					{
						Component comp = event.getTarget();
						Integer Record_ID = (Integer) comp.getAttribute("Record_ID");
						Integer AD_Table_ID = (Integer) comp.getAttribute("AD_Table_ID");
						if (Record_ID != null && Record_ID > 0 && AD_Table_ID != null && AD_Table_ID > 0)
						{
							AEnv.zoom(AD_Table_ID, Record_ID);
						}
					}
				});
				statusBar.appendChild(link0);
				// Allocation N 2 IF Miltiple Allocation is Y
				allocation = allocationPair[1];
				A link1 = new A(allocationPair[1].getDocumentNo());
				link1.setAttribute("Record_ID", allocation.get_ID());
				link1.setAttribute("AD_Table_ID", allocation.get_Table_ID());
				link1.addEventListener(Events.ON_CLICK, new EventListener<Event>() 
						{
					@Override
					public void onEvent(Event event) throws Exception 
					{
						Component comp = event.getTarget();
						Integer Record_ID = (Integer) comp.getAttribute("Record_ID");
						Integer AD_Table_ID = (Integer) comp.getAttribute("AD_Table_ID");
						if (Record_ID != null && Record_ID > 0 && AD_Table_ID != null && AD_Table_ID > 0)
						{
							AEnv.zoom(AD_Table_ID, Record_ID);
						}
					}
				});
				statusBar.appendChild(link1);
			}
		}
		else if (e.getTarget().equals(refreshButton))
		{
			loadBPartner1();
			loadBPartner2();
		}
		//	Cancel
		else if (e.getTarget().equals(cancelButton))
		{
			FDialog.ask(m_WindowNo, form, Msg.getMsg(Env.getCtx(),"ExitApplication?"), new Callback<Boolean>() {
				@Override
				public void onCallback(Boolean result) 
				{
					if (result)
					{
						dispose();
						//	document No not updated
					}	
				}
			});
			//dispose();
		}
		//	Zoom
		else if (e.getTarget().equals(zoomButton))
		{
			// invoiceTable  (Invoices) 
			// paymentTable  (Payments)
			zoom(paymentTable,invoiceTable);
			loadBPartner1();
		}
	}   //  actionPerformed

	/**
	 *  Table Model Listener.
	 *  - Recalculate Totals
	 *  @param e event
	 */
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
			FDialog.warn(form.getWindowNo(), "AllocationWriteOffWarn");
		
		calculate();
	}   //  tableChanged
	
	/**
	 *  Vetoable Change Listener.
	 *  - Business Partner
	 *  - Currency
	 * 	- Date
	 *  @param e event
	 */
	public void valueChange (ValueChangeEvent e)
	{
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		if (log.isLoggable(Level.CONFIG)) log.config(name + "=" + value);
		if (value == null && ( !name.equals("C_Charge_ID") && !name.equals("C_Activity_ID") && !name.equals("C_Project_ID") ) )
			return;
		
		// Organization
		if (name.equals("AD_Org_ID"))
		{
			m_AD_Org_ID = ((Integer) value).intValue();
			
			loadBPartner1();
			loadBPartner2 ();
			setAllocateButton();
		}
		//		Charge
		else if (name.equals("C_Charge_ID") )
		{
			m_C_Charge_ID = value!=null? ((Integer) value).intValue() : 0;	
			setAllocateButton();
		}
		//		C_Activity_ID
		else if (name.equals("C_Activity_ID") )
		{
			m_C_Activity_ID = value!=null? ((Integer) value).intValue() : 0;
			
			setAllocateButton();
		}
		//		C_Project_ID
		else if (name.equals("C_Project_ID") )
		{
			m_C_Project_ID = value!=null? ((Integer) value).intValue() : 0;
			
			setAllocateButton();
		}
		//  BPartner1
		if (e.getSource().equals(bpartnerSearch))
		{
			bpartnerSearch.setValue(value);
			m_C_BPartner_ID = ((Integer)value).intValue();
			loadBPartner1();
			setAllocateButton();
		}
		//  BPartner2
		else if (e.getSource().equals(bpartnerSearch2))
		{
			bpartnerSearch2.setValue(value);
			m_C_BPartner2_ID = ((Integer)value).intValue();
			loadBPartner2 ();
			setAllocateButton();
		}
		//	Currency
		else if (name.equals("C_Currency_ID"))
		{
			m_C_Currency_ID = ((Integer)value).intValue();
			loadBPartner1();
			loadBPartner2 ();
			setAllocateButton();
		}
		//	Date for Multi-Currency
		else if (name.equals("Date") && multiCurrency.isSelected())
		{
			loadBPartner1();
			loadBPartner2 ();
			setAllocateButton();
		}
	}   //  vetoableChange
	
	/**
	 * setAllocateButton
	 * Activate Button if conditions are set to acomplish Allocation
	 * Payments and Invoices selected are balanced and also Charge selected
	 * 
	 */
	private void setAllocateButton() {
		// Enables only if Both Payments and Invoices are selected
		if ( totalPay.signum() != 0 && totalInv.signum() != 0) {
			if (totalDiff.signum() == 0 && m_C_Charge_ID > 0  && (m_C_Activity_ID > 0 || mandatoryActivity == false) )
			{
				allocateButton.setEnabled(true);
			// chargePick.setValue(m_C_Charge_ID);
			}
			else
			{
				allocateButton.setEnabled(false);
			}
		} else {
			allocateButton.setEnabled(false);
		}
//			if ( totalDiff.signum() == 0 )
//			{
//					chargePick.setValue(null);
//					m_C_Charge_ID = 0;
//	   		}
	}
	/**
	 *  Load Business Partner Info
	 *  - Payments
	 */
	private void loadBPartner1 ()
	{
		checkBPartner();
		
		Vector<Vector<Object>> data = getPaymentData(multiCurrency.isSelected(), dateField.getValue(), paymentTable);
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
		
		calculate(multiCurrency.isSelected());
		
		//  Calculate Totals
		calculate();
		
		statusBar.getChildren().clear();
		
		// Clear Description C_Charge_ID C_Activity_ID
		descriptionField.setValue("");
		m_C_Charge_ID=0;
		m_C_Activity_ID=0;
		m_C_Project_ID=0;
		activityPick.setValue(null);
		chargePick.setValue(null);
		projectPick.setValue(null);
	}   //  loadBPartner1
	
	/**
	 *  Load Business Partner Info
	 *  - Invoices
	 */
	private void loadBPartner2 ()
	{
		checkBPartner();

		Vector<Vector<Object>> data = getInvoiceData(multiCurrency.isSelected(), dateField.getValue(), invoiceTable);
		Vector<String> columnNames = getInvoiceColumnNames(multiCurrency.isSelected());
		
		invoiceTable.clear();
		
		//  Remove previous listeners
		invoiceTable.getModel().removeTableModelListener(this);
		
		//  Set Model
		ListModelTable modelI = new ListModelTable(data);
		modelI.addTableModelListener(this);
		invoiceTable.setData(modelI, columnNames);
		setInvoiceColumnClass(invoiceTable, multiCurrency.isSelected());
		//
		
		calculate(multiCurrency.isSelected());
		
		//  Calculate Totals
		calculate();
		
		statusBar.getChildren().clear();
		// Clear Description C_Charge_ID C_Activity_ID
		descriptionField.setValue("");
		m_C_Charge_ID=0;
		m_C_Activity_ID=0;
		m_C_Project_ID=0;
		activityPick.setValue(null);
		chargePick.setValue(null);
		projectPick.setValue(null);
	}   //  loadBPartner
	
	public void calculate()
	{
		allocDate = null;
		
		paymentInfo.setText(calculatePayment(paymentTable, multiCurrency.isSelected()));
		invoiceInfo.setText(calculateInvoice(invoiceTable, multiCurrency.isSelected()));
		
		//	Set AllocationDate
		if (allocDate != null)
			dateField.setValue(allocDate);
		//  Set Allocation Currency
		allocCurrencyLabel.setText(currencyPick.getDisplay());
		//  Difference
		totalDiff = totalPay.subtract(totalInv);
		differenceField.setText(format.format(totalDiff));		
		//
		setAllocateButton();
	}
	
	/**************************************************************************
	 *  Save Data
	 */
	private MAllocationHdr[] saveData()
	{
		String Message = "";
		String Title = "";
		
		if (m_AD_Org_ID > 0)
			Env.setContext(Env.getCtx(), form.getWindowNo(), "AD_Org_ID", m_AD_Org_ID);
		else
			Env.setContext(Env.getCtx(), form.getWindowNo(), "AD_Org_ID", "");
		try
		{
			Trx.run(new TrxRunnable() 
			{
				public void run(String trxName)
				{
					statusBar.getChildren().clear();
					final boolean allocation = saveDataAllocation(form.getWindowNo(), dateField.getValue(), descriptionField.getText(),
							paymentTable, invoiceTable, m_C_Charge_ID,  trxName);		
				}
			});

			//mallocation = allocation[1];
			if ( allocationHeader[0].getDocumentNo() != null && allocationHeader[1].getDocumentNo() != null ) {
				Message = " *** " + Msg.getElement(Env.getCtx(), "C_AllocationHdr_ID")+ "(s) **** "+ "\r\n";
				Message = Message + Msg.translate(Env.getCtx(), "AllocationProcessed")+ "\r\n";
				Message = Message + Msg.getElement(Env.getCtx(), "C_AllocationHdr_ID")+" No 1 ="+allocationHeader[0].getDocumentNo() + "\r\n" +
						allocationHeader[0].getDescription() + "\r\n" ;
				Message = Message+Msg.getElement(Env.getCtx(), "C_AllocationHdr_ID")+" No 2 ="+allocationHeader[1].getDocumentNo() +  "\r\n" +
						allocationHeader[1].getDescription() + "\r\n" ;
				Title=Msg.translate(Env.getCtx(), "Message") + " " + Msg.translate(Env.getCtx(), "to") + " "+ Msg.getElement(Env.getCtx(), "AD_User_ID");
				FDialog.info(m_WindowNo, form, Message);
			} else {
				Message = " *** "+Msg.getElement(Env.getCtx(), "C_AllocationHdr_ID")+ "(s) **** "+ "\r\n";
				Message = Message + Msg.translate(Env.getCtx(), "NotMatched")+ "\r\n";
				Message = Message+ " **** ERROR ****" ;
				Title=Msg.translate(Env.getCtx(), "Message") + " " + Msg.translate(Env.getCtx(), "of") + " *** ERROR *** "+ 
						Msg.translate(Env.getCtx(), "to") + " " + Msg.getElement(Env.getCtx(), "AD_User_ID");
				FDialog.info(m_WindowNo, form, Message, Title);
			}
//			log.warning(".....OK.....");
//			log.warning("allocation1="+allocationHeader[0].getDocumentNo()+" "+allocationHeader[0].getDescription());
//			log.warning("allocation2="+allocationHeader[1].getDocumentNo()+" "+allocationHeader[1].getDescription());
			//mallocation = allocationHeader[1];
			return allocationHeader;
		}
		catch (Exception e)
		{
			FDialog.error(form.getWindowNo(), form, "Error", e.getLocalizedMessage());
			return null;
		}
	}   //  saveData
	
	/**
	 * Called by org.adempiere.webui.panel.ADForm.openForm(int)
	 * @return
	 */
	public ADForm getForm()
	{
		return form;
	}

	/**
	 * 	Dispose
	 */
	public void dispose()
	{
		SessionManager.getAppDesktop().closeActiveWindow();
	}	//	dispose
}   //  VAllocation

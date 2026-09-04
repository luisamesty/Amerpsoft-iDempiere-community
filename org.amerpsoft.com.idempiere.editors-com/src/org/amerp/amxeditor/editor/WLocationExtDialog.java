/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.amxeditor.editor;

import java.util.*;
import java.util.logging.Level;

import org.adempiere.util.Callback;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.*;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.event.ActionEvent;
import org.adempiere.webui.event.ActionListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.*;
//
import org.amerp.amxeditor.model.*;
import org.amerp.amxeditor.service.LocationAddressUtil;
import org.amerp.amxeditor.service.LocationAddressUtil.LocationData;
import org.compiere.model.*;
//
import org.compiere.util.*;
//import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Executions;

//import org.zkoss.zul.*;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Center;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.South;
import org.zkoss.zul.Vbox;

/**
 * @author luisamesty
 *
 */
public class WLocationExtDialog extends Window implements IFormController, EventListener<Event>, ValueChangeListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5368065537791919302L;
	
	private static final String LABEL_STYLE = "white-space: nowrap;";
	/** Logger          */
	private static CLogger log = CLogger.getCLogger(WLocationExtDialog.class);
	/** UI form instance */
	private CustomForm form = new CustomForm();
	private Label lblAddress1;
	private Label lblAddress2;
	private Label lblAddress3;
	private Label lblAddress4;
	private Label lblCity;
	private Label lblZip;
	private Label lblRegion;
	private Label lblPostal;
	private Label lblPostalAdd;
	private Label lblCountry;
	// ADDED FIELDS
	private Label lblMunicipality;
	private Label lblParish;
	

	private Textbox txtAddress1;
	private Textbox txtAddress2;
	private Textbox txtAddress3;
	private Textbox txtAddress4;
	private WAutoCompleterCity txtCity;
	private Textbox txtPostal;
	private Textbox txtPostalAdd;
	private Listbox lstRegion;
	private Listbox lstCountry;
	// ADDED FIELDS
	private Listbox lstMunicipality;
	private Listbox lstParish;
	
	// Componentes de Geolocalización (Solo Lectura)
	private Label lblLatitude;
	private Label lblLongitude;
	private Label lblGeocodingStatus;

	private Textbox txtLatitude;
	private Textbox txtLongitude;
    private Combobox cmbGeocodingStatus;
    private String m_GeoCodingStatus;
	private ConfirmPanel confirmPanel;
	private Grid mainPanel;

	private boolean     m_change = false;
	private MLocationExt   m_location;
	private int         m_origCountry_ID;
	private int         m_origRegion_ID;
	private int         m_origMunicipality_ID;
	private int         m_selectedRegion_ID;
	private int         m_selectedMunicipality_ID;
	private int         s_oldCountry_ID = 0;
	private int         s_oldRegion_ID = 0;
	private int         s_oldMunicipality_ID = 0;
	
	private int m_WindowNo = 0;

	private boolean isCityMandatory = false;
	private boolean isRegionMandatory = false;
	private boolean isAddress1Mandatory = false;
	private boolean isAddress2Mandatory = false;
	private boolean isAddress3Mandatory = false;
	private boolean isAddress4Mandatory = false;
	private boolean isPostalMandatory = false;
	private boolean isPostalAddMandatory = false;
	//
	private boolean isMunicipalityMandatory = false;
	private boolean isParishMandatory = false;

	private boolean inCountryAction;
	private boolean inOKAction;

	private Button toLink;
	private Button toRoute;
	private Button toLinkOpenStreet;
	
	private Listbox lstAddressValidation;
	private Button btnOnline;
	private Textbox txtResult;
	private Checkbox cbxValid;
	private ArrayList<String> enabledCountryList = new ArrayList<String>();
	
	private GridField m_GridField = null;
	private boolean onSaveError = false;
	// Map Frame
    private Iframe mapIframe;
    //END

	public WLocationExtDialog(String title, MLocationExt location)
	{
		this (title, location, null);
	}

	public WLocationExtDialog(String title, MLocationExt location, GridField gridField) {
		m_GridField  = gridField;
		m_location = location;
		if (m_location == null)
			m_location = new MLocationExt (Env.getCtx(), 0, null);
		//  Overwrite title 
		if (m_location.getC_Location_ID() == 0)
			setTitle("Ext-"+Msg.getMsg(Env.getCtx(), "LocationNew"));
		else
			setTitle("Ext-"+Msg.getMsg(Env.getCtx(), "LocationUpdate"));    
		//
		// Reset TAB_INFO context
		Env.setContext(Env.getCtx(), m_WindowNo, Env.TAB_INFO, "C_Region_ID", null);
		Env.setContext(Env.getCtx(), m_WindowNo, Env.TAB_INFO, "C_Country_ID", null);
		//
		initComponents();
		init();
		//      Current Country
		for (MCountryExt country: MCountryExt.getCountries(Env.getCtx()))
		{
			lstCountry.appendItem(country.toString(), country);
		}
		setCountry();
		lstCountry.addEventListener(Events.ON_SELECT,this);
		lstRegion.addEventListener(Events.ON_SELECT,this);
		lstMunicipality.addEventListener(Events.ON_SELECT,this);
		//lstParish.addEventListener(Events.ON_SELECT,this);
		m_origCountry_ID = m_location.getC_Country_ID();
		//  Current Region
		lstRegion.appendItem("", null);
		for (MRegion region : MRegion.getRegions(m_origCountry_ID))
		{
			lstRegion.appendItem(region.getName(),region);
		}
		if (m_location.getCountryExt().isHasRegion()) {
			if (m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName) != null
					&& m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName).trim().length() > 0)
				lblRegion.setValue(m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName));
			else
				lblRegion.setValue(Msg.getMsg(Env.getCtx(), "Region"));
		}
		setRegion();
		// Municipality
		m_origRegion_ID = m_location.getC_Region_ID();
		m_selectedRegion_ID = m_origRegion_ID;	// lstRegion.getSelectedCount();;
		// 
		lstMunicipality.appendItem("", null); 
		for (MMunicipality municipality : MMunicipality.getSQLMunicipalitys(Env.getCtx(), m_selectedRegion_ID))
		{
			lstMunicipality.appendItem(municipality.getName(),municipality);
		}
		setMunicipality();
		// Parish
		m_origMunicipality_ID =  m_location.getC_Municipality_ID();
		m_selectedMunicipality_ID = m_origMunicipality_ID;	// lstMunicipality.getSelectedCount();	
		//
		lstParish.appendItem("", null); 
		for (MParish parish : MParish.getSQLParishs(Env.getCtx(), m_selectedMunicipality_ID,m_selectedRegion_ID))
		{
			lstParish.appendItem(parish.getName(),parish);
		}
		setParish();
		// initLocation First Time
		initLocation();
		//  
		if (!ThemeManager.isUseCSSForWindowSize()) 
		{
			ZKUpdateUtil.setWindowWidthX(this, 550);
			ZKUpdateUtil.setWindowHeightX(this, 520); 
		}
		else
		{
			addCallback(AFTER_PAGE_ATTACHED, t -> {
				ZKUpdateUtil.setCSSHeight(this);
				ZKUpdateUtil.setCSSWidth(this);
			});
		}
		//
		ZKUpdateUtil.setWidth(this, "550px");
		ZKUpdateUtil.setHeight(this, "580px");
		
		this.setSclass("popup-dialog");
		this.setClosable(true);
		this.setBorder("normal");
		this.setShadow(true);
		this.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
	}
	
	/**
	 * initComponents()
	 * Set Form's Components associated with their fields on c_location thru MLocationExt 
	 *
	 */
	private void initComponents()
	{
		lblAddress1     = new Label(Msg.getElement(Env.getCtx(), "Address1"));
		lblAddress1.setStyle(LABEL_STYLE);
		lblAddress2     = new Label(Msg.getElement(Env.getCtx(), "Address2"));
		lblAddress2.setStyle(LABEL_STYLE);
		lblAddress3     = new Label(Msg.getElement(Env.getCtx(), "Address3"));
		lblAddress3.setStyle(LABEL_STYLE);
		lblAddress4     = new Label(Msg.getElement(Env.getCtx(), "Address4"));
		lblAddress4.setStyle(LABEL_STYLE);
		lblCity         = new Label(Msg.getMsg(Env.getCtx(), "City"));
		lblCity.setStyle(LABEL_STYLE);
		lblZip          = new Label(Msg.getMsg(Env.getCtx(), "Postal"));
		lblZip.setStyle(LABEL_STYLE);
		lblRegion       = new Label(Msg.getMsg(Env.getCtx(), "Region"));
		lblRegion.setStyle(LABEL_STYLE);
		lblPostal       = new Label(Msg.getMsg(Env.getCtx(), "Postal"));
		lblPostal.setStyle(LABEL_STYLE);
		lblPostalAdd    = new Label(Msg.getMsg(Env.getCtx(), "PostalAdd"));
		lblPostalAdd.setStyle(LABEL_STYLE);
		lblCountry      = new Label(Msg.getMsg(Env.getCtx(), "Country"));
		lblCountry.setStyle(LABEL_STYLE);
		lblMunicipality = new Label(Msg.getMsg(Env.getCtx(), "Municipality"));
		lblMunicipality.setStyle(LABEL_STYLE);
		lblParish		= new Label(Msg.getMsg(Env.getCtx(), "Parish"));
		lblParish.setStyle(LABEL_STYLE);
		
		txtAddress1 = new Textbox();
		txtAddress1.setCols(20);
		txtAddress1.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_Address1));
		txtAddress2 = new Textbox();
		txtAddress2.setCols(20);
		txtAddress2.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_Address2));
		txtAddress3 = new Textbox();
		txtAddress3.setCols(20);
		txtAddress3.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_Address3));
		txtAddress4 = new Textbox();
		txtAddress4.setCols(20);
		txtAddress4.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_Address4));

		//autocomplete City
		txtCity = new WAutoCompleterCity(m_WindowNo);
		txtCity.setCols(20);
		txtCity.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_City));
		txtCity.setAutodrop(true);
		txtCity.setAutocomplete(true);
		txtCity.addEventListener(Events.ON_CHANGING, this);
		//txtCity

		txtPostal = new Textbox();
		txtPostal.setCols(20);
		txtPostal.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_Postal));
		txtPostalAdd = new Textbox();
		txtPostalAdd.setCols(20);
		txtPostalAdd.setMaxlength(MLocationExt.getFieldLength(MLocationExt.COLUMNNAME_Postal_Add));

		lstRegion    = new Listbox();
		lstRegion.setMold("select");
		//lstRegion.setWidth("154px");
		ZKUpdateUtil.setWidth(lstRegion, "154px");
		lstRegion.setRows(0);

		lstCountry  = new Listbox();
		lstCountry.setMold("select");
		//lstCountry.setWidth("154px");
		ZKUpdateUtil.setWidth(lstCountry, "154px");		
		lstCountry.setRows(0);
		
		lstMunicipality    = new Listbox();
		lstMunicipality.setMold("select");
		//lstMunicipality.setWidth("154px");
		ZKUpdateUtil.setWidth(lstMunicipality, "154px");	
		lstMunicipality.setRows(0);

		lstParish    = new Listbox();
		lstParish.setMold("select");
		//lstParish.setWidth("154px");
		ZKUpdateUtil.setWidth(lstParish, "154px");	
		lstParish.setRows(0);

		confirmPanel = new ConfirmPanel(true);
		confirmPanel.addActionListener(this);

		// Configured Map (Google)
		toLink = new Button(Msg.getMsg(Env.getCtx(), "Map"));
		LayoutUtils.addSclass("txt-btn", toLink);
		toLink.addEventListener(Events.ON_CLICK,this);
		// OpenStreet Map
		toLinkOpenStreet = new Button(Msg.getMsg(Env.getCtx(), "Map")+" (OpenStreet)");
		LayoutUtils.addSclass("txt-btn", toLinkOpenStreet);
		toLinkOpenStreet.addEventListener(Events.ON_CLICK,this);
		// Configured Route
		toRoute = new Button(Msg.getMsg(Env.getCtx(), "Route"));
		LayoutUtils.addSclass("txt-btn", toRoute);
		toRoute.addEventListener(Events.ON_CLICK,this);
		// Mapa Frame
		mapIframe = new Iframe();
        mapIframe.setWidth("100%");
        mapIframe.setHeight("350px");
        mapIframe.setStyle("border: 1px solid #ccc; border-radius: 4px;");
		
		btnOnline = new Button(Msg.getElement(Env.getCtx(), "ValidateAddress"));
		LayoutUtils.addSclass("txt-btn", btnOnline);
		btnOnline.addEventListener(Events.ON_CLICK,this);
		
		txtResult = new Textbox();
		txtResult.setCols(2);
		txtResult.setRows(3);
		txtResult.setReadonly(true);
		
		cbxValid = new Checkbox();
		cbxValid.setText(Msg.getElement(Env.getCtx(), "IsValid"));
		cbxValid.setDisabled(true);
		
		lstAddressValidation = new Listbox();
		lstAddressValidation.setMold("select");
		ZKUpdateUtil.setWidth(lstAddressValidation, "154px");	
		lstAddressValidation.setRows(0);		
		
		lblLatitude = new Label(Msg.getElement(Env.getCtx(), "Latitude"));
		lblLatitude.setStyle(LABEL_STYLE);
		lblLongitude = new Label(Msg.getElement(Env.getCtx(), "Longitude"));
		lblLongitude.setStyle(LABEL_STYLE);
		lblGeocodingStatus = new Label(Msg.getElement(Env.getCtx(), "GeocodingStatus"));
		lblGeocodingStatus.setStyle(LABEL_STYLE);

		txtLatitude = new Textbox();
		txtLatitude.setCols(20);
		txtLatitude.setReadonly(false);

		txtLongitude = new Textbox();
		txtLongitude.setCols(20);
		txtLongitude.setReadonly(false);

		cmbGeocodingStatus = new Combobox();
		List<ValueNamePair> geoCodingStatusTypes = this.getGeocodeStatusArray();
		cmbGeocodingStatus.setModel(new ListModelList<>(geoCodingStatusTypes));
		cmbGeocodingStatus.setItemRenderer((item, data, index) -> {
            ValueNamePair vnp = (ValueNamePair) data;
            item.setLabel(vnp.getName());
            item.setValue(vnp); 
        });
        // Seleccionar valor inicial
        String valueToSet = (m_GeoCodingStatus == null || m_GeoCodingStatus.isEmpty())
                            ? MLocationExt.GEOCODING_STATUS_OK
                            : m_GeoCodingStatus;
        for (ValueNamePair vnp : geoCodingStatusTypes) {
            if (vnp.getValue().equals(valueToSet)) {
            	cmbGeocodingStatus.setValue(vnp.getName());
            	m_GeoCodingStatus = vnp.getValue();
                break;
            }
        }
        Events.postEvent(new Event("onChange", cmbGeocodingStatus));
        // Listener
        cmbGeocodingStatus.addEventListener("onChange", event -> {
            if (cmbGeocodingStatus.getSelectedItem() != null) {
                ValueNamePair selected = (ValueNamePair) cmbGeocodingStatus.getSelectedItem().getValue();
                m_GeoCodingStatus = selected.getValue();
            }
        });
		
		mainPanel = GridFactory.newGridLayout();
	}
	
	/**
	 * init()
	 *
	 */
	private void init()
	{
		Columns columns = new Columns();
		mainPanel.appendChild(columns);
		
		Column column = new Column();
		columns.appendChild(column);
		//column.setWidth("30%");
		ZKUpdateUtil.setWidth(column, "30%");	
		
		column = new Column();
		columns.appendChild(column);
		//column.setWidth("70%");
		ZKUpdateUtil.setWidth(column, "70%");	
		
		Row pnlAddress1 = new Row();
		pnlAddress1.appendChild(lblAddress1.rightAlign());
		pnlAddress1.appendChild(txtAddress1);
		//txtAddress1.setHflex("1");
		ZKUpdateUtil.setHflex(txtAddress1, "1");


		Row pnlAddress2 = new Row();
		pnlAddress2.appendChild(lblAddress2.rightAlign());
		pnlAddress2.appendChild(txtAddress2);
		//txtAddress2.setHflex("1");
		ZKUpdateUtil.setHflex(txtAddress2, "1");
		
		Row pnlAddress3 = new Row();
		pnlAddress3.appendChild(lblAddress3.rightAlign());
		pnlAddress3.appendChild(txtAddress3);
		//txtAddress3.setHflex("1");
		ZKUpdateUtil.setHflex(txtAddress3, "1");
		
		Row pnlAddress4 = new Row();
		pnlAddress4.appendChild(lblAddress4.rightAlign());
		pnlAddress4.appendChild(txtAddress4);
		//txtAddress4.setHflex("1");
		ZKUpdateUtil.setHflex(txtAddress4, "1");

		Row pnlCity     = new Row();
		pnlCity.appendChild(lblCity.rightAlign());
		pnlCity.appendChild(txtCity);
		//txtCity.setHflex("1");
		ZKUpdateUtil.setHflex(txtCity, "1");
		
		Row pnlMunicipality     = new Row();
		pnlMunicipality.appendChild(lblMunicipality.rightAlign());
		pnlMunicipality.appendChild(lstMunicipality);
		//lstMunicipality.setHflex("1");
		ZKUpdateUtil.setHflex(lstMunicipality, "1");
		
		Row pnlParish     = new Row();
		pnlParish.appendChild(lblParish.rightAlign());
		pnlParish.appendChild(lstParish);
		//lstParish.setHflex("1");
		ZKUpdateUtil.setHflex(lstParish, "1");
		
		Row pnlPostal   = new Row();
		pnlPostal.appendChild(lblPostal.rightAlign());
		pnlPostal.appendChild(txtPostal);
		//txtPostal.setHflex("1");
		ZKUpdateUtil.setHflex(txtPostal, "1");
		
		Row pnlPostalAdd = new Row();
		pnlPostalAdd.appendChild(lblPostalAdd.rightAlign());
		pnlPostalAdd.appendChild(txtPostalAdd);
		//txtPostalAdd.setHflex("1");
		ZKUpdateUtil.setHflex(txtPostalAdd, "1");

		Row pnlRegion    = new Row();
		pnlRegion.appendChild(lblRegion.rightAlign());
		pnlRegion.appendChild(lstRegion);
		//lstRegion.setHflex("1");
		ZKUpdateUtil.setHflex(lstRegion, "1");
		
		Row pnlCountry  = new Row();
		pnlCountry.appendChild(lblCountry.rightAlign());
		pnlCountry.appendChild(lstCountry);
		//lstCountry.setHflex("1");
		ZKUpdateUtil.setHflex(lstCountry, "1");
		
		Row pnlGeoStatus = new Row();
		pnlGeoStatus.appendChild(lblGeocodingStatus.rightAlign());
		pnlGeoStatus.appendChild(cmbGeocodingStatus);
		ZKUpdateUtil.setHflex(cmbGeocodingStatus, "1");

		Row pnlLat = new Row();
		pnlLat.appendChild(lblLatitude.rightAlign());
		pnlLat.appendChild(txtLatitude);
		ZKUpdateUtil.setHflex(txtLatitude, "1");

		Row pnlLon = new Row();
		pnlLon.appendChild(lblLongitude.rightAlign());
		pnlLon.appendChild(txtLongitude);
		ZKUpdateUtil.setHflex(txtLongitude, "1");

		Panel pnlLinks    = new Panel();
		pnlLinks.appendChild(toLink);
		if (MLocationExt.LOCATION_MAPS_URL_PREFIX == null)
			toLink.setVisible(false);
		pnlLinks.appendChild(toLinkOpenStreet);
		if (MLocationExt.LOCATION_MAPS_URL_PREFIX_NOMINATIM == null)
			toLinkOpenStreet.setVisible(false);
		pnlLinks.appendChild(toRoute);
		if (MLocationExt.LOCATION_MAPS_ROUTE_PREFIX == null || Env.getAD_Org_ID(Env.getCtx()) <= 0)
			toRoute.setVisible(false);
		//pnlLinks.setWidth("100%");
		ZKUpdateUtil.setWidth(pnlLinks, "100%");
		pnlLinks.setStyle("text-align:right");
		
		Borderlayout borderlayout = new Borderlayout();
        ZKUpdateUtil.setHflex(borderlayout, "1");
        ZKUpdateUtil.setVflex(borderlayout, "1");
        this.appendChild(borderlayout);
        
        Center centerPane = new Center();
        centerPane.setSclass("dialog-content");
        centerPane.setAutoscroll(true); // Scroll interno en pantallas pequeñas
        ZKUpdateUtil.setVflex(centerPane, "1");
        borderlayout.appendChild(centerPane);

        // Vbox como ÚNICO hijo directo de centerPane
        Vbox vbox = new Vbox();
        ZKUpdateUtil.setHflex(vbox, "1");
        centerPane.appendChild(vbox);
        
        vbox.appendChild(mainPanel);
        if (MLocationExt.LOCATION_MAPS_URL_PREFIX != null || MLocationExt.LOCATION_MAPS_ROUTE_PREFIX != null)
            vbox.appendChild(pnlLinks);
        
        String addressValidation = MSysConfig.getValue(MSysConfig.ADDRESS_VALIDATION, null, Env.getAD_Client_ID(Env.getCtx()));
        enabledCountryList.clear();
        if (addressValidation != null && addressValidation.trim().length() > 0)
        {
            StringTokenizer st = new StringTokenizer(addressValidation, ";");
            while (st.hasMoreTokens())
            {
                String token = st.nextToken().trim();
                enabledCountryList.add(token);
            }
        }
            
        if (enabledCountryList.size() > 0)
        {
            // Usar una variable diferente o la instancia creada aquí
            Grid gridLayout = GridFactory.newGridLayout();
            vbox.appendChild(gridLayout);
			
			columns = new Columns();
			gridLayout.appendChild(columns);
			
			Rows rows = new Rows();
			gridLayout.appendChild(rows);
			
			Row row = new Row();
			rows.appendChild(row);
			row.appendCellChild(lstAddressValidation, 2);
			//lstAddressValidation.setHflex("1");			
			ZKUpdateUtil.setHflex(lstAddressValidation, "1");			
			
			MAddressValidationExt[] validations = MAddressValidationExt.getAddressValidation(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()), null);
			for (MAddressValidationExt validation : validations)
			{
				ListItem li = lstAddressValidation.appendItem(validation.getName(), validation);
				if (m_location.getC_AddressValidation_ID() == validation.getC_AddressValidation_ID())
					lstAddressValidation.setSelectedItem(li);
			}
			
			if (lstAddressValidation.getSelectedIndex() == -1 && lstAddressValidation.getChildren().size() > 0)
				lstAddressValidation.setSelectedIndex(0);
						
			row = new Row();
			rows.appendChild(row);
			row.appendCellChild(txtResult, 2);
			//txtResult.setHflex("1");
			ZKUpdateUtil.setHflex(txtResult, "1");
			txtResult.setText(m_location.getResult());
			
			row = new Row();
			rows.appendChild(row);
			row.appendChild(cbxValid);
			cbxValid.setChecked(m_location.isValid());
			Cell cell = new Cell();
			cell.setColspan(1);
			cell.setRowspan(1);
			cell.appendChild(btnOnline);
			cell.setAlign("right");
			row.appendChild(cell);
			
			if (!enabledCountryList.isEmpty())
			{
				boolean isEnabled = false;
				if (m_location.getCountryExt() != null)
				{
					for (String enabledCountry : enabledCountryList)
					{
						if (enabledCountry.equals(m_location.getCountryExt().getCountryCode().trim()))
						{
							isEnabled = true;
							break;
						}
					}
				}
				btnOnline.setEnabled(isEnabled);
			}
		}
		
		ZKUpdateUtil.setVflex(vbox, "1");
		ZKUpdateUtil.setHflex(vbox, "1");
		
		South southPane = new South();
		southPane.setSclass("dialog-footer");
		borderlayout.appendChild(southPane);
		southPane.appendChild(confirmPanel);
		
		addEventListener("onSaveError", this);
	}
	
	/**
	 * Dynamically add fields to the Location dialog box
	 * @param panel panel to add
	 *
	 */
	private void addComponents(Row row)
	{
		if (mainPanel.getRows() != null)
			mainPanel.getRows().appendChild(row);
		else
			mainPanel.newRows().appendChild(row);
	}
	
	/**
	* initLocation()
	* Init Location Form as Tokens indicate on Capture Sequence
	* Country Variables 
	*/
	private void initLocation()
	{
		if (mainPanel.getRows() != null)
			mainPanel.getRows().getChildren().clear();

		MCountryExt country =  m_location.getCountryExt();
		if (log.isLoggable(Level.FINE)) log.fine(country.getName() + ", Region=" + country.isHasRegion() + " " + country.getCaptureSequence()
				+ ", C_Location_ID=" + m_location.getC_Location_ID());
		//  new Country
		if (m_location.getC_Country_ID() != s_oldCountry_ID)
		{
			lstRegion.getChildren().clear();
			if (country.isHasRegion()) {
				lstRegion.appendItem("", null);
				for (MRegionExt region : MRegionExt.getRegions(Env.getCtx(), country.getC_Country_ID()))
				{
					lstRegion.appendItem(region.getName(),region);
				}
				if (m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName) != null
						&& m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName).trim().length() > 0)
					lblRegion.setValue(m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName));
				else
					lblRegion.setValue(Msg.getMsg(Env.getCtx(), "Region"));
			}
			s_oldCountry_ID = m_location.getC_Country_ID();
		}
		//  new Region
		if (m_location.getC_Region_ID() > 0 && m_location.getC_Region().getC_Country_ID() == country.getC_Country_ID()) {
			setRegion();
		} else {
			lstRegion.setSelectedItem(null);
			m_location.setC_Region_ID(0);
		}

		if (country.isHasRegion() && m_location.getC_Region_ID() > 0)
		{
			Env.setContext(Env.getCtx(), m_WindowNo, Env.TAB_INFO, "C_Region_ID", String.valueOf(m_location.getC_Region_ID()));
		} else {
			Env.setContext(Env.getCtx(), m_WindowNo, Env.TAB_INFO, "C_Region_ID", "0");
		}
		// log.warning("....C_Country_ID"+String.valueOf(country.get_ID()));
		Env.setContext(Env.getCtx(), m_WindowNo, Env.TAB_INFO, "C_Country_ID", String.valueOf(country.get_ID()));
		
		// City Filllist
		txtCity.fillList();

		// actualiza cuando cambia la region	
		if (m_location.getC_Region_ID() != s_oldRegion_ID)
		{	
			lstMunicipality.getChildren().clear();
			lstMunicipality.appendItem("", null);
			
			lstParish.getChildren().clear();
			lstParish.appendItem("", null);
			for (MMunicipality municipality : MMunicipality.getSQLMunicipalitys(Env.getCtx(), m_location.getC_Region_ID()))
			{
				lstMunicipality.appendItem(municipality.getName(),municipality);			
			}
			//setMunicipality();	
			if(m_location.getC_Municipality_ID()>0){
				setMunicipality();
			} else {
				lstMunicipality.setSelectedItem(null);
				m_location.setC_Municipality_ID(0);
			}
			s_oldRegion_ID = m_location.getC_Region_ID();
		}
		//  new Municipality
		// actualiza cuando cambia el municipio
		if (m_location.getC_Municipality_ID() != s_oldMunicipality_ID)
		{
			// Clear Parish
			lstParish.getChildren().clear();
			lstParish.appendItem("", null); 
			for (MParish parish : MParish.getSQLParishs(Env.getCtx(),  m_location.getC_Municipality_ID(),m_location.getC_Region_ID()))
			{
				lstParish.appendItem(parish.getName(),parish);
			}
			setParish();
			s_oldMunicipality_ID = m_location.getC_Municipality_ID();
		}
		//  new Parish
		String ds = country.getCaptureSequence();
		if (ds == null || ds.length() == 0)
		{
			log.log(Level.SEVERE, "CaptureSequence empty - " + country);
			ds = "";    //  @C@,  @P@
		}
		// Init Mandatory fields variables FIRST STAGE
		this.initMandatoryFromAD();

		// Review Mandatory fields variables SECOND STAGE IF CONFIGURED
		StringTokenizer st = new StringTokenizer(ds, "@", false);
		while (st.hasMoreTokens())
		{
			String s = st.nextToken();
			if (s.startsWith("CO")) {
				//  Country Last
				addComponents((Row)lstCountry.getParent());
				// if (m_location.getCountryExt().isPostcodeLookup()) {
					// addLine(line++, lOnline, fOnline);
				// }
			} else if (s.startsWith("A1")) {
				addComponents((Row)txtAddress1.getParent());
				isAddress1Mandatory = s.endsWith("!");
			} else if (s.startsWith("A2")) {
				addComponents((Row)txtAddress2.getParent());
				isAddress2Mandatory = s.endsWith("!");
			} else if (s.startsWith("A3")) {
				addComponents((Row)txtAddress3.getParent());
				isAddress3Mandatory = s.endsWith("!");
			} else if (s.startsWith("A4")) {
				addComponents((Row)txtAddress4.getParent());
				isAddress4Mandatory = s.endsWith("!");
			} else if (s.startsWith("C")) {
				addComponents((Row)txtCity.getParent());
				isCityMandatory = s.endsWith("!");
			} else if (s.startsWith("MU") && m_location.getCountryExt().isHasRegion() && m_location.getCountryExt().isHasMunicipality()) {
				addComponents((Row)lstMunicipality.getParent());
				isMunicipalityMandatory = s.endsWith("!");
			}else if (s.startsWith("PA") && s.trim().equalsIgnoreCase("PA") && m_location.getCountryExt().isHasRegion() && m_location.getCountryExt().isHasParish()) {
				addComponents((Row)lstParish.getParent());
				isParishMandatory = s.endsWith("!");
			} else if (s.startsWith("P") && s.trim().equalsIgnoreCase("P")) {
				addComponents((Row)txtPostal.getParent());
				isPostalMandatory = s.endsWith("!");
			} else if (s.startsWith("A")) {
				addComponents((Row)txtPostalAdd.getParent());
				isPostalAddMandatory = s.endsWith("!");
			} else if (s.startsWith("R") && m_location.getCountryExt().isHasRegion()) {
				addComponents((Row)lstRegion.getParent());
				isRegionMandatory = s.endsWith("!");
			}
		}

		// --- SECCIÓN GEOLOCALIZACIÓN (Al final del formulario) ---
        addComponents((Row) cmbGeocodingStatus.getParent());
        addComponents((Row) txtLatitude.getParent());
        addComponents((Row) txtLongitude.getParent());

        //      Fill it
		if (m_location.getC_Location_ID() != 0)
		{
			txtAddress1.setText(m_location.getAddress1());
			txtAddress2.setText(m_location.getAddress2());
			txtAddress3.setText(m_location.getAddress3());
			txtAddress4.setText(m_location.getAddress4());
			txtCity.setText(m_location.getCity());
			txtPostal.setText(m_location.getPostal());
			txtPostalAdd.setText(m_location.getPostal_Add());
			if (m_location.getCountryExt().isHasRegion())
			{
				if (m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName) != null
						&& m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName).trim().length() > 0)
					lblRegion.setValue(m_location.getCountryExt().get_Translation(MCountryExt.COLUMNNAME_RegionName));
				else
					lblRegion.setValue(Msg.getMsg(Env.getCtx(), "Region"));

				setRegion();                
			}
			setCountry();

			// 1. Cargar coordenadas
		    if (txtLatitude != null)
		        txtLatitude.setText(m_location.getLatitude());
		    if (txtLongitude != null)
		        txtLongitude.setText(m_location.getLongitude());

		    // 2. Cargar estado de geolocalización desde BD (evitando el valor por defecto)
		    m_GeoCodingStatus = m_location.getGeocodingStatus();
		    
		    // Si la BD no tiene valor grabado aún, asignamos PENDING explícitamente
		    if (Util.isEmpty(m_GeoCodingStatus)) {
		    	m_GeoCodingStatus = MLocationExt.GEOCODING_STATUS_PENDING;
		    }
		    
		    setGeocodingStatus(m_GeoCodingStatus);
		    // Inicia el Variable  con el valor leido de la tabla
		    m_location.setGeocodingStatus(m_GeoCodingStatus);
		    // Inicia el Combo con el valor leido de la tabla
		    setSelectedGeocodingStatus(cmbGeocodingStatus, m_GeoCodingStatus);
		}
	}
	
	/**
	 * setCountry
	 */
	private void setCountry()
	{
		List<?> listCountry = lstCountry.getChildren();
		Iterator<?> iter = listCountry.iterator();
		while (iter.hasNext())
		{
			ListItem listitem = (ListItem)iter.next();
			if (m_location.getCountryExt().equals(listitem.getValue()))
			{
				lstCountry.setSelectedItem(listitem);
			}
		}
	}

	/**
	* setRegion
	*/
	private void setRegion()
	{
		if (m_location.getRegionExt() != null) 
		{
			List<?> listState = lstRegion.getChildren();
			Iterator<?> iter = listState.iterator();
			while (iter.hasNext())
			{
				ListItem listitem = (ListItem)iter.next();
				if (m_location.getRegionExt().equals(listitem.getValue()))
				{
					lstRegion.setSelectedItem(listitem);
				}
			}
		}
		else
		{
			lstRegion.setSelectedItem(null);
		}        
	}
	
	/**
	 * setMunicipality
	 */
	private void setMunicipality()
	{
		if (m_location.getMunicipality() != null ) 
		{
			List<?> listMun = lstMunicipality.getChildren();
			Iterator<?> iter = listMun.iterator();
			while (iter.hasNext())
			{
				ListItem listitem = (ListItem)iter.next();
				if (m_location.getMunicipality().equals(listitem.getValue()))
				{
					lstMunicipality.setSelectedItem(listitem);
				}
			}
		}
		else
		{
			lstMunicipality.setSelectedItem(null);
		}        
	}
	
	/**
	 *  setParish
	 */
	private void setParish()
	{
		if (m_location.getParish() != null) 
		{
			List<?> listParr = lstParish.getChildren();
			Iterator<?> iter = listParr.iterator();
			while (iter.hasNext())
			{
				ListItem listitem = (ListItem)iter.next();
				if (m_location.getParish().equals(listitem.getValue()))
				{
					lstParish.setSelectedItem(listitem);
				}
			}
		}
		else
		{
			lstParish.setSelectedItem(null);
		}        
	}
	
	/**
	 *  Get result
	 *  @return true, if changed
	 */
	public boolean isChanged()
	{
		return m_change;
	}   //  getChange
	
	/**
	 *  Get edited Value (MLocationExt)
	 *  @return location
	 */
	public MLocationExt getValue()
	{
		return m_location;
	}   

	public void onEvent(Event event) throws Exception
	{
		if (event.getTarget() == confirmPanel.getButton(ConfirmPanel.A_OK)) 
		{
			onSaveError = false;
			
			inOKAction = true;
			
			if (m_location.getCountryExt().isHasRegion() && lstRegion.getSelectedItem() == null) {
				if (txtCity.getC_Region_ID() > 0 && txtCity.getC_Region_ID() != m_location.getC_Region_ID()) {
					m_location.setRegion(MRegionExt.get(Env.getCtx(), txtCity.getC_Region_ID()));
					setRegion();
				}
			}
			
			String msg = validate_Mandatory_OK();
			if (msg != null) {
			    onSaveError = true;
			    
			    // Firma correcta de Dialog.error para iDempiere 10+
			    Dialog.error(m_WindowNo, "FillMandatory", Msg.parseTranslation(Env.getCtx(), msg), evt -> {
			        Events.echoEvent("onSaveError", WLocationExtDialog.this, null);
			    });
			    
			    inOKAction = false;
			    return;
			}
			
			if (action_OK())
			{
				m_change = true;
				inOKAction = false;
				this.dispose();
			}
			else
			{
				onSaveError = true;
				Dialog.error(m_WindowNo, "AddressNotProcessed", (String) null, evt -> {
					Events.echoEvent("onSaveError", WLocationExtDialog.this, null);
				});
			}
			inOKAction = false;
		}
		else if (event.getTarget() == confirmPanel.getButton(ConfirmPanel.A_CANCEL))
		{
			m_change = false;
			this.dispose();
		}
		else if (toLink.equals(event.getTarget()))
		{
			String urlString = MLocationExt.LOCATION_MAPS_URL_PREFIX + getFullAddress();
			String message = null;

			try {
				Executions.getCurrent().sendRedirect(urlString, "_blank");
			}
			catch (Exception e) {
				message = e.getMessage();
				Dialog.warn(m_WindowNo, "URLnotValid", message);
			}
		}
		else if (toLinkOpenStreet.equals(event.getTarget()))
		{
			
			if (!Util.isEmpty(getFullAddress()) && !getGeocodingStatus().equals(MLocationExt.GEOCODING_STATUS_OK)) {
				String urlString = MLocationExt.LOCATION_MAPS_URL_PREFIX_NOMINATIM + getFullAddress();
				String message = null;

				try {
					Executions.getCurrent().sendRedirect(urlString, "_blank");
				}
				catch (Exception e) {
					message = e.getMessage();
					Dialog.warn(m_WindowNo, "URLnotValid", message);
				}

			}
		}
		else if (toRoute.equals(event.getTarget()))
		{
			int AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
			if (AD_Org_ID != 0){
				MOrgInfo orgInfo = 	MOrgInfo.get(Env.getCtx(), AD_Org_ID,null);
				MLocationExt orgLocation = new MLocationExt(Env.getCtx(),orgInfo.getC_Location_ID(),null);

				String urlString = MLocationExt.LOCATION_MAPS_ROUTE_PREFIX +
						         MLocationExt.LOCATION_MAPS_SOURCE_ADDRESS + orgLocation.getMapsLocation() + //org
						         MLocationExt.LOCATION_MAPS_DESTINATION_ADDRESS + getFullAddress(); //partner
				String message = null;
				try {
					Executions.getCurrent().sendRedirect(urlString, "_blank");
				}
				catch (Exception e) {
					message = e.getMessage();
					Dialog.warn(m_WindowNo, "URLnotValid", message);
				}
			}
		}
		else if (btnOnline.equals(event.getTarget()))
		{
			btnOnline.setEnabled(false);
			
			onSaveError = false;
			
			inOKAction = true;
			
			if (m_location.getCountryExt().isHasRegion() && lstRegion.getSelectedItem() == null) {
				if (txtCity.getC_Region_ID() > 0 && txtCity.getC_Region_ID() != m_location.getC_Region_ID()) {
					m_location.setRegion(MRegionExt.get(Env.getCtx(), txtCity.getC_Region_ID()));
					setRegion();
				}
			}
			// VALIDATION MESSAGE USING FDialog Class
			String msg = validate_Mandatory_OK();
			if (msg != null) {
				onSaveError = true;
				onSaveError = true;
				Dialog.error(m_WindowNo, "FillMandatory", Msg.parseTranslation(Env.getCtx(), msg), evt -> {
					Events.echoEvent("onSaveError", WLocationExtDialog.this, null);
				});
				inOKAction = false;
				return;
			}
			
			MLocationExt m_location = new MLocationExt(Env.getCtx(), 0, null);
			m_location.setAddress1(txtAddress1.getValue());
			m_location.setAddress2(txtAddress2.getValue());
			m_location.setAddress3(txtAddress3.getValue());
			m_location.setAddress4(txtAddress4.getValue());
			m_location.setC_City_ID(txtCity.getC_City_ID()); 
			m_location.setCity(txtCity.getValue());
			m_location.setPostal(txtPostal.getValue());
			//  Country/Region
			MCountryExt country = (MCountryExt) lstCountry.getSelectedItem().getValue();
			m_location.setCountry(country);
			if (country.isHasRegion() && lstRegion.getSelectedItem() != null)
			{
				MRegionExt r = (MRegionExt)lstRegion.getSelectedItem().getValue();
				m_location.setRegion(r);
			}
			else
			{
				m_location.setC_Region_ID(0);
			}			
				

			MAddressValidationExt validation = lstAddressValidation.getSelectedItem().getValue();
			if (validation == null && lstAddressValidation.getChildren().size() > 0)
				validation = lstAddressValidation.getItemAtIndex(0).getValue();			
			if (validation != null)
			{
				boolean ok = m_location.processOnline(validation.getC_AddressValidation_ID());
				
				txtResult.setText(m_location.getResult());
				cbxValid.setChecked(m_location.isValid());
				
				List<?> list = lstAddressValidation.getChildren();
				Iterator<?> iter = list.iterator();
				while (iter.hasNext())
				{
					ListItem listitem = (ListItem)iter.next();
					if (m_location.getC_AddressValidation().equals(listitem.getValue()))
					{
						lstAddressValidation.setSelectedItem(listitem);
						break;
					}
				}
				if (!ok)
				{
					onSaveError = true;
					onSaveError = true;
					Dialog.error(m_WindowNo, "Error", m_location.getErrorMessage(), evt -> {
						Events.echoEvent("onSaveError", WLocationExtDialog.this, null);
					});
				}
			}
			
			inOKAction = false;
			
			btnOnline.setEnabled(true);
		}
		//  Country Changed - display in new Format
		else if (lstCountry.equals(event.getTarget()))
		{
			inCountryAction = true;
			MCountryExt c = (MCountryExt)lstCountry.getSelectedItem().getValue();
			m_location.setCountry(c);
			m_location.setC_City_ID(0);
			m_location.setCity(null);
			//  refresh
			initLocation();
			
			if (!enabledCountryList.isEmpty())
			{
				boolean isEnabled = false;
				if (c != null)
				{
					for (String enabledCountry : enabledCountryList)
					{
						if (enabledCountry.equals(c.getCountryCode().trim()))
						{
							isEnabled = true;
							break;
						}
					}
				}
				btnOnline.setEnabled(isEnabled);
			}
			
			inCountryAction = false;
			lstCountry.focus();
		}
		//  Region Changed 
		else if (lstRegion.equals(event.getTarget()))
		{
			if (inCountryAction || inOKAction)
				return;
			MRegionExt r = (MRegionExt) lstRegion.getSelectedItem().getValue();
			m_location.setRegion(r);
			m_location.setC_City_ID(0);
			m_location.setCity(null);
			//  refresh
			initLocation();
			lstRegion.focus();
		}
		//  Municipality Changed 
		else if (lstMunicipality.equals(event.getTarget()))
		{
			if (inCountryAction || inOKAction)
				return;
			MMunicipality m = (MMunicipality) lstMunicipality.getSelectedItem().getValue();
			m_location.setMunicipality(m);
			m_location.setC_Parish_ID(0);
			m_location.setParish(null);
			//  refresh
			initLocation();
			lstMunicipality.focus();
		}
		else if ("onSaveError".equals(event.getName())) {
			onSaveError = false;
			doPopup();
			focus();			
		}
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
	    String propertyName = event.getPropertyName();
	    
	    // Si el evento viene de nuestro combo de estado de geolocalización
	    if ("GeocodingStatus".equalsIgnoreCase(propertyName) || event.getSource() == cmbGeocodingStatus) {
	        String newValue = getGeocodingStatus();
	        
	        // Actualizar el valor en el modelo de MLocationExt (M_Location / C_Location)
	        if (m_location != null) {
	            m_location.set_ValueOfColumn("GeocodingStatus", newValue);
	        }
	    }
	}
	
	// LCO - address 1, region and city required
	private String validate_Mandatory_OK() {
		String fields = "";
		if (isAddress1Mandatory && txtAddress1.getText().trim().length() == 0) {
			fields = fields + " " + "@Address1@, ";
		}
		if (isAddress2Mandatory && txtAddress2.getText().trim().length() == 0) {
			fields = fields + " " + "@Address2@, ";
		}
		if (isAddress3Mandatory && txtAddress3.getText().trim().length() == 0) {
			fields = fields + " " + "@Address3@, ";
		}
		if (isAddress4Mandatory && txtAddress4.getText().trim().length() == 0) {
			fields = fields + " " + "@Address4@, ";
		}
		if (isCityMandatory && txtCity.getValue().trim().length() == 0) {
			fields = fields + " " + "@City@, ";
		}
		if (isParishMandatory && lstParish.getName().trim().length() == 0) {
			fields = fields + " " + "@Parish@, ";
		}
		if (isMunicipalityMandatory && lstMunicipality.getName().trim().length() == 0) {
			fields = fields + " " + "@Municipality@, ";
		}
		if (isRegionMandatory && lstRegion.getSelectedItem() == null) {
			fields = fields + " " + "@Region@, ";
		}
		if (isPostalMandatory && txtPostal.getText().trim().length() == 0) {
			fields = fields + " " + "@Postal@, ";
		}
		if (isPostalAddMandatory && txtPostalAdd.getText().trim().length() == 0) {
			fields = fields + " " + "@PostalAdd@, ";
		}
		
		if (fields.trim().length() > 0)
			return fields.substring(0, fields.length() -2);

		return null;
	}

	/**
	 *  OK - check for changes (save them) & Exit
	 */
	private boolean action_OK()
	{
		Trx trx = Trx.get(Trx.createTrxName("WLocationExtDialog"), true);
		m_location.set_TrxName(trx.getTrxName());
		m_location.setAddress1(txtAddress1.getValue());
		m_location.setAddress2(txtAddress2.getValue());
		m_location.setAddress3(txtAddress3.getValue());
		m_location.setAddress4(txtAddress4.getValue());
		m_location.setC_City_ID(txtCity.getC_City_ID()); 
		m_location.setCity(txtCity.getValue());
		m_location.setPostal(txtPostal.getValue());
		m_location.setPostal_Add(txtPostalAdd.getValue());
		//  Country/Region
		MCountryExt country = (MCountryExt)lstCountry.getSelectedItem().getValue();
		m_location.setCountry(country);
		if (country.isHasRegion() ) {
			//if (!lstRegion.getSelectedItem().equals(null)) {
			if (lstRegion.getSelectedItem() != null) {
				MRegionExt r = (MRegionExt)lstRegion.getSelectedItem().getValue();
				m_location.setRegion(r); 
			} else {
				m_location.setC_Region_ID(0);
			}
		} else {
			m_location.setC_Region_ID(0);
		}
		// Municipality
		if (lstMunicipality.getSelectedItem() != null ) {
			MMunicipality m=(MMunicipality)lstMunicipality.getSelectedItem().getValue();
			m_location.setMunicipality(m);
		} else {
			m_location.setMunicipality(null);
		}
		// Parish
		if ( lstParish.getSelectedItem() != null) {
			MParish p=(MParish)lstParish.getSelectedItem().getValue();
			m_location.setParish(p);
		} else {
			m_location.setParish(null);
		}
		// 1. Asignar variables de geolocalización
		m_location.setLatitude(txtLatitude.getValue());
		m_location.setLongitude(txtLongitude.getValue());
		m_location.setGeocodingStatus(getGeocodingStatus());
		log.warning("Antes de salvar GeoCodingStatus = " + m_location.getGeocodingStatus());
		boolean success = false;
		// 2. Guardar m_location utilizando la transacción activa
		if (m_location.save())
		{
			// Obtener C_BPartner_Location_ID dentro de la misma transacción
			String sql = "SELECT C_BPartner_Location_ID FROM C_BPartner_Location WHERE C_Location_ID = ?";
			int bplID = DB.getSQLValueEx(trx.getTrxName(), sql, m_location.getC_Location_ID());
			MBPartnerLocationExt bpl = new MBPartnerLocationExt(Env.getCtx(), bplID, trx.getTrxName());
			if (m_GridField != null && m_GridField.getGridTab() != null
        			&& "C_BPartner_Location".equals(m_GridField.getGridTab().getTableName())) {
        		if (!bpl.isPreserveCustomName())
        			m_GridField.getGridTab().setValue("Name", ".");
				success = true;
    		} else {
    			//Update BP_Location name IDEMPIERE 417
    			if (bplID>0) {
    				if (bpl.save())
    					success = true;
    			} else {
					success = true;
    			}
    		}
		}
		// 3. Confirmar o revertir la transacción
		if (success) {
			trx.commit();
		} else {
			trx.rollback();
		}
		trx.close();

		return success;
	}   //  actionOK

	public boolean isOnSaveError() {
		return onSaveError;
	}
	
	@Override
	public void dispose()
	{
		if (!m_change && m_location != null && !m_location.is_new())
		{
			m_location = new MLocationExt(m_location.getCtx(), m_location.get_ID(), null);
		}	
		super.dispose();
	}
	
	/** returns a string that contains all fields of current form */
	String getFullAddress() {
	    String txtCountry = "";
	    String txtRegion = "";
	    String txtMunicipality = "";
	    String txtParish = "";

	    // 1. Validar y extraer País
	    if (lstCountry != null && lstCountry.getSelectedItem() != null) {
	        Object val = lstCountry.getSelectedItem().getValue();
	        if (val instanceof MCountryExt country) {
	            txtCountry = country.getName() != null ? country.getName() : "";
	        }
	    }

	    // 2. Validar y extraer Región
	    if (lstRegion != null && lstRegion.getSelectedItem() != null) {
	        Object val = lstRegion.getSelectedItem().getValue();
	        if (val instanceof MRegionExt region) {
	            txtRegion = region.getName() != null ? region.getName() : "";
	        }
	    }

	    // 3. Validar y extraer Municipio
	    if (lstMunicipality != null && lstMunicipality.getSelectedItem() != null) {
	        Object val = lstMunicipality.getSelectedItem().getValue();
	        if (val instanceof MMunicipality municipality) {
	            txtMunicipality = municipality.getName() != null ? municipality.getName() : "";
	        }
	    }

	    // 4. Validar y extraer Parroquia
	    if (lstParish != null && lstParish.getSelectedItem() != null) {
	        Object val = lstParish.getSelectedItem().getValue();
	        if (val instanceof MParish parish) {
	            txtParish = parish.getName() != null ? parish.getName() : "";
	        }
	    }

	    // 5. Construir DTO con textos seguros usando helper para los Textbox
	    LocationAddressUtil.LocationData locData = new LocationData(
	            getTextSafe(txtAddress1), 
	            getTextSafe(txtAddress2), 
	            getTextSafe(txtAddress3),
	            getTextSafe(txtAddress4),
	            txtMunicipality, 
	            txtParish, 
	            getTextSafe(txtCity), 
	            txtRegion,
	            txtCountry
	    );

	    return LocationAddressUtil.buildFullAddress(locData, ",");
	}

	/**
	 * Método auxiliar para obtener el texto de un Textbox evitando NullPointerException.
	 */
	private String getTextSafe(Textbox txt) {
	    if (txt != null && txt.getText() != null) {
	        return txt.getText().trim();
	    }
	    return "";
	}
	
	/**
	 * Sobrecarga de getTextSafe para componentes WAutoCompleterCity.
	 */
	private String getTextSafe(WAutoCompleterCity autoCompleter) {
	    if (autoCompleter != null && autoCompleter.getText() != null) {
	        return autoCompleter.getText().trim();
	    }
	    return "";
	}
	
	private List<ValueNamePair> getGeocodeStatusArray() {
	    // 1. Obtener el AD_Reference_ID dinámicamente por Nombre
	    String sql = "SELECT AD_Reference_ID FROM AD_Reference WHERE Name = ? AND IsActive = 'Y'";
	    int adReferenceId = DB.getSQLValue(null, sql, "GeocodingStatus");

	    if (adReferenceId <= 0) {
	        // Manejo de error si no existe la referencia en el diccionario de datos
	        return Collections.emptyList();
	    }

	    // 2. Obtener los ítems traducidos según el idioma actual
	    ValueNamePair[] list = MRefList.getList(Env.getCtx(), adReferenceId, false);
	    List<ValueNamePair> geoCodeStatusList = new ArrayList<>(Arrays.asList(list));

	    // 3. Ordenar por prioridad personalizada
	    Map<String, Integer> priority = Map.of(
	        MLocationExt.GEOCODING_STATUS_OK, 1,
	        MLocationExt.GEOCODING_STATUS_ERROR, 2,
	        MLocationExt.GEOCODING_STATUS_NOT_FOUND, 3,
	        MLocationExt.GEOCODING_STATUS_PENDING, 4
	    );

	    geoCodeStatusList.sort(Comparator.comparingInt(
	        v -> priority.getOrDefault(v.getValue(), 99)
	    ));

	    return geoCodeStatusList;
	}
	
	/**
	 * Establece la opción seleccionada según el código de estado (ej: "OK", "NOT_FOUND").
	 */
	private void setGeocodingStatus(String status) {
	    if (cmbGeocodingStatus == null) return;

	    if (Util.isEmpty(status)) {
	        cmbGeocodingStatus.setSelectedIndex(-1);
	        return;
	    }

	    for (int i = 0; i < cmbGeocodingStatus.getItemCount(); i++) {
	        Comboitem item = cmbGeocodingStatus.getItemAtIndex(i);
	        Object rawVal = item.getValue();
	        String code = null;

	        if (rawVal instanceof ValueNamePair vnp) {
	            code = vnp.getValue();
	        } else if (rawVal instanceof String str) {
	            code = str;
	        }

	        if (code != null && status.equalsIgnoreCase(code)) {
	            cmbGeocodingStatus.setSelectedIndex(i);
	            return;
	        }
	    }
	}

	/**
	 * Obtiene el código del estado seleccionado (retorna String para guardar en BD).
	 */
	private String getGeocodingStatus() {
	    if (cmbGeocodingStatus != null) {
	        // 1. Obtener el ítem seleccionado actualmente en la UI
	        Comboitem selectedItem = cmbGeocodingStatus.getSelectedItem();
	        
	        if (selectedItem != null) {
	            Object rawVal = selectedItem.getValue();
	            
	            // Si le asignaste el String directo mediante item.setValue("OK")
	            if (rawVal instanceof String str && !str.isBlank()) {
	                return str.trim();
	            }
	            
	            // En caso de que se haya guardado el objeto ValueNamePair completo
	            if (rawVal instanceof ValueNamePair vnp) {
	                return vnp.getValue();
	            }
	        }

	        // 2. Fallback: Si no hay selectedItem pero hay un valor interno asignado al combo
	        Object rawComboVal = cmbGeocodingStatus.getValue();
	        if (rawComboVal instanceof String strVal && !strVal.isBlank()) {
	            return strVal.trim();
	        }
	    }
	    
	    return "";
	}

	
	/**
	 * Selecciona el elemento correspondiente en el combo de estado de geocodificación
	 * basándose en el valor leído de la base de datos.
	 * 
	 * @param cmbGeocodingStatus Componente Combobox ZK
	 * @param initialStatus Valor leído de BD (m_GeoCodingStatus)
	 * @return El valor seleccionado de BD o el valor por defecto (OK)
	 */
	private String setSelectedGeocodingStatus(Combobox cmbGeocodingStatus, String initialStatus) {
	    // 1. Determinar el valor a buscar
	    String valueToSet = (initialStatus == null || initialStatus.trim().isEmpty())
	                        ? MLocationExt.GEOCODING_STATUS_OK
	                        : initialStatus;
	                        
	    List<ValueNamePair> geoCodingStatusTypes = this.getGeocodeStatusArray();

	    // 2. Buscar y seleccionar la coincidencia en el combo
	    if (cmbGeocodingStatus != null && geoCodingStatusTypes != null) {
	        for (ValueNamePair vnp : geoCodingStatusTypes) {
	            if (vnp.getValue().equals(valueToSet)) {
	                
	                // Buscar el Comboitem equivalente en el Combobox para seleccionarlo formalmente
	                Comboitem itemToSelect = cmbGeocodingStatus.getItems().stream()
	                        .filter(item -> vnp.getValue().equals(item.getValue()))
	                        .findFirst()
	                        .orElse(null);

	                if (itemToSelect != null) {
	                    cmbGeocodingStatus.setSelectedItem(itemToSelect);
	                } else {
	                    cmbGeocodingStatus.setValue(vnp.getName());
	                }
	                
	                return vnp.getValue();
	            }
	        }
	    }

	    return valueToSet;
	}

	/**
	 * initMandatoryFromAD
	 * Review Mandatory fields from table C_Location (MLocationExt)
	 */
	private void initMandatoryFromAD() {
	    int tableId = MTable.getTable_ID(MLocationExt.Table_Name);

	    isAddress1Mandatory     = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_Address1);
	    isAddress2Mandatory     = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_Address2);
	    isAddress3Mandatory     = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_Address3);
	    isAddress4Mandatory     = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_Address4);
	    isCityMandatory         = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_City);
	    isRegionMandatory       = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_C_Region_ID);
	    isPostalMandatory       = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_Postal);
	    isPostalAddMandatory    = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_Postal_Add);
	    isMunicipalityMandatory = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_C_Municipality_ID);
	    isParishMandatory       = isColumnMandatory(tableId, MLocationExt.COLUMNNAME_C_Parish_ID);
	}

	private boolean isColumnMandatory(int tableId, String columnName) {
	    MColumn col = MColumn.get(Env.getCtx(), tableId, columnName);
	    return col != null && col.isMandatory();
	}
	
	@Override
	public ADForm getForm() {
		return form;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		log.warning("actionPerformed="+event.getEventName());
	}

}

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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.ValuePreference;
import org.adempiere.webui.component.Locationbox;
import org.adempiere.webui.editor.*;
import org.adempiere.webui.event.*;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.window.WFieldRecordInfo;
import org.adempiere.webui.window.WLocationDialog;
import org.amerp.amxeditor.factory.DisplayTypeFactory;
import org.amerp.amxeditor.model.MLocationExt;
import org.amerp.amxeditor.model.MLocationLookupExt;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
//import org.compiere.model.*;
import org.compiere.util.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;


/**
 * @author luisamesty
 *
 */
public class WLocationExtEditor extends WEditor implements EventListener<Event>, PropertyChangeListener, ContextMenuListener
{
	

	private static final String[] LISTENER_EVENTS = {Events.ON_CLICK};
    
    private static CLogger log = CLogger.getCLogger(WLocationEditor.class);
    private MLocationLookupExt     m_Location;
    private MLocationExt           m_value;

    /**
     * Constructor without GridField
     * @param columnName    column name
     * @param mandatory     mandatory
     * @param isReadOnly    read only
     * @param isUpdateable  updateable
     * @param mLocation     location model
    **/
    public WLocationExtEditor(String columnName, boolean mandatory, boolean isReadOnly, boolean isUpdateable,
    		MLocationLookupExt mLocation)
    {
        super(new Locationbox(), "Address","",mandatory,isReadOnly,isUpdateable);
       
        setColumnName(columnName);
        m_Location = mLocation;
        init();
    }

    /**
     * 
     * @param gridField
     * @param p_gridTab 
     */
    public WLocationExtEditor(GridField gridField, GridTab p_gridTab) {
		super(new Locationbox(), gridField);
		m_Location = (MLocationLookupExt)gridField.getLookup();
        init();
	}

    private void init()
    {
    	getComponent().setButtonImage(ThemeManager.getThemeResource("images/Location16.png"));
    	
    	popupMenu = new WEditorPopupMenu(false, false, isShowPreference());
    	popupMenu.addMenuListener(this);
    	addChangeLogMenu(popupMenu);
    }
    
	@Override
    public String getDisplay()
    {
        return getComponent().getText();
    }

    @Override
    public Object getValue()
    {
        if (m_value == null)
            return null;
        return new Integer(m_value.getC_Location_ID());
    }

    @Override
    public void setValue(Object value)
    {
        if (value == null)
        {
            m_value = null;
            getComponent().setText(null);
        }
        else
        {
        	String trxName = null; // could be null if called from a form
        	if (this.gridField != null)
        		trxName = this.gridField.getGridTab().getTableModel().get_TrxName();
            
        	m_value = this.getLocation(value, trxName);
            if (m_value == null)
                getComponent().setText("<" + value + ">");
            else
                getComponent().setText(m_value.toString());
        }
    }
    
    public MLocationExt getLocation (Object key, String trxName)
	{
		if (key == null)
			return null;
		int C_Location_ID = 0;
		if (key instanceof Integer)
			C_Location_ID = ((Integer)key).intValue();
		else if (key != null)
			C_Location_ID = Integer.parseInt(key.toString());
		//
		return getLocation(C_Location_ID, trxName);
	}	//	getLocation
    
    public MLocationExt getLocation (int C_Location_ID, String trxName)
	{
    	m_value = (MLocationExt) MLocationExt.get(Env.getCtx(), C_Location_ID, trxName);
		return m_value;
	}
    
    @Override
	public Locationbox getComponent() {
		return (Locationbox) component;
	}

	@Override
	public boolean isReadWrite() {
		return getComponent().isEnabled();
	}

	@Override
	public void setReadWrite(boolean readWrite) {
		getComponent().setEnabled(readWrite);
	}

	/**
     *  Return Editor value
     *  @return value
     */
    public int getC_Location_ID()
    {
        if (m_value == null)
            return 0;
        return m_value.getC_Location_ID();
    }   
    
    /**
     *  Property Change Listener
     *  @param evt PropertyChangeEvent
     */
    public void propertyChange (PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
        	setValue(evt.getNewValue());
    }
    
    public void onEvent(Event event) throws Exception
    {    
        //
        if ("onClick".equals(event.getName()))
        {
            if (log.isLoggable(Level.CONFIG)) log.config( "actionPerformed - " + m_value);
            final WLocationExtDialog ld = new WLocationExtDialog(Msg.getMsg(Env.getCtx(), "Location"), m_value, gridField);
            ld.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					getComponent().getTextbox().focus();
					m_value = ld.getValue();
		            //
					if (!ld.isChanged())
		                return;
		    
		            //  Data Binding
		            int C_Location_ID = 0;
		            if (m_value != null)
		                C_Location_ID = m_value.getC_Location_ID();
		            Integer ii = new Integer(C_Location_ID);
		            //  force Change - user does not realize that embedded object is already saved.
		            ValueChangeEvent valuechange = new ValueChangeEvent(WLocationExtEditor.this,getColumnName(),null,null);
		            fireValueChange(valuechange);   //  resets m_mLocation
		            if (C_Location_ID != 0)
		            {
		                ValueChangeEvent vc = new ValueChangeEvent(WLocationExtEditor.this,getColumnName(),null,ii);
		                fireValueChange(vc);
		            }
		            setValue(ii);					
				}
			});
            ld.addEventListener(Events.ON_OPEN, new EventListener<OpenEvent>() {
				@Override
				public void onEvent(OpenEvent event) throws Exception {
					if (!event.isOpen() && !ld.isOnSaveError()) {
						ld.detach();
					}
				}
			});
            ld.setTitle(null);
            LayoutUtils.openPopupWindow(getComponent(), ld);
        }
    }
    
    /**
     * return listener events to be associated with editor component
     */
    public String[] getEvents()
    {
        return LISTENER_EVENTS;
    }

    @Override
	public void onMenu(ContextMenuEvent evt) {
		if (WEditorPopupMenu.CHANGE_LOG_EVENT.equals(evt.getContextEvent()))
		{
			WFieldRecordInfo.start(gridField);
		}
		else if (WEditorPopupMenu.PREFERENCE_EVENT.equals(evt.getContextEvent()))
		{
			if (isShowPreference())
				ValuePreference.start (getComponent(), this.getGridField(), getValue());
		}
	}

	@Override
	public void setTableEditor(boolean b) {
		super.setTableEditor(b);
		getComponent().setTableEditorMode(b);
	}
}

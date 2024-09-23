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
package org.amerp.amncallouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

/**
 * @author luisamesty
 *
 */
public class AMN_Process_callout implements IColumnCallout{

	CLogger log = CLogger.getCLogger(AMN_Process_callout.class);
	
	String Process_Value="";
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
	    // ************************
    	// FieldRef: value
    	// ************************
		if (p_mTab.getValue(MAMN_Process.COLUMNNAME_Value) != null) {
		    Process_Value= (String) p_mTab.getValue(MAMN_Process.COLUMNNAME_Value);
		    //log.warning("Process Value:"+Process_Value);

		}
	    // ******************************
	    // Set field's AMN_Process_Value 
	    // ******************************		
		p_mTab.setValue("AMN_Process_Value", Process_Value);

	    return null;
    }

}

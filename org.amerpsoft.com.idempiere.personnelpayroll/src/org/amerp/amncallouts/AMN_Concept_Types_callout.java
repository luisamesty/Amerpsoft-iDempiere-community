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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.adempiere.webui.component.Window;
import org.amerp.amnmodel.*;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class AMN_Concept_Types_callout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(AMN_Concept_Types_callout.class);
	
	Integer AMN_Concept_Types_Proc_ID=0;
	Integer AMN_Concept_Types_ID=0;
	String sql,Value_Concept,Variable_Concept, Name_Concept;
	Integer CalcOrder=0;
	
	@Override
	public String start(Properties p_ctx, int WindowNo, GridTab p_mTab,
			GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		// ******************************
		// FieldRef: Value - 
	    //	(Verify if null)	    		
		// ******************************
	    if ((p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Value) != null)  &&
			( p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID) != null) ){
	    	Value_Concept = (String) p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Value);
	    	if (p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Value) != null) {
	    		p_mTab.setValue("variable",Value_Concept.trim());
	    	}
	    	// Get AMN_Concept_Types_ID
	    	AMN_Concept_Types_ID = (int) p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID);
	    	MAMN_Concept_Types amct = new MAMN_Concept_Types(p_ctx, AMN_Concept_Types_ID, null);
	    	// UPDATE ALL AMNConcept_Types_pro
	    	amct.updateALL_AMNConcept_Types_Proc(AMN_Concept_Types_ID);
	    	// UPDATE ALL AMN_Concept_Types_contract
	    	amct.updateALL_AMNConcept_Types_Contract(AMN_Concept_Types_ID);
	    	// Refresh ALL Dependent TABS
	    	// GridWindow Class
	    	GridWindow gridwin = p_mTab.getGridWindow();
	    	int notabs = gridwin.getTabCount();
	    	// Current TAB is nor Refreshed itab=0
	    	for (int itab = 1; itab < notabs; itab++)
			{
	    		GridTab tabChild = gridwin.getTab(itab);
	    		tabChild.dataRefresh();
	    		//tabChild.dataRefreshAll();
			}
	    	
	    }

	    // ******************************
		// FieldRef: Name - 
	    //	(Verify if null)	    		
		// ******************************
	    if ((p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Name) != null)  &&
			( p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID) != null) ) {
	    	Name_Concept = (String) p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_Name);
	    	p_mTab.setValue("Name",Name_Concept.trim());
	    	// Get AMN_Concept_Types_ID
	    	AMN_Concept_Types_ID = (int) p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID);
	    	MAMN_Concept_Types amct = new MAMN_Concept_Types(p_ctx, AMN_Concept_Types_ID, null);
	    	// UPDATE ALL AMNConcept_Types_pro
	    	amct.updateALL_AMNConcept_Types_Proc(AMN_Concept_Types_ID);
	    	// UPDATE ALL AMN_Concept_Types_contract
	    	amct.updateALL_AMNConcept_Types_Contract(AMN_Concept_Types_ID);
	    	// Refresh ALL Dependent TABS
	    	// GridWindow Class
	    	GridWindow gridwin = p_mTab.getGridWindow();
	    	int notabs = gridwin.getTabCount();
	    	// Current TAB is nor Refreshed itab=0
	    	for (int itab = 1; itab < notabs; itab++)
			{
	    		GridTab tabChild = gridwin.getTab(itab);
	    		tabChild.dataRefresh();
	    		//tabChild.dataRefreshAll();
			}
	    }

		// ******************************
		// FieldRef: CalcOrder
	    //	(Verify if null)	    		
		// ******************************
	    if ( (p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_CalcOrder) != null) &&
	    		( p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID) != null) )  {
	    	CalcOrder=(Integer) p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_CalcOrder);
	    	if (CalcOrder != 0) {
		    	p_mTab.setValue("CalcOrder",CalcOrder);
		    	AMN_Concept_Types_ID = (int) p_mTab.getValue(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID);
		    	MAMN_Concept_Types amct = new MAMN_Concept_Types(p_ctx, AMN_Concept_Types_ID, null);
		    	// UPDATE ALL AMNConcept_Types_pro
		    	amct.updateALL_AMNConcept_Types_Proc(AMN_Concept_Types_ID);
		    	// UPDATE ALL AMN_Concept_Types_contract
		    	amct.updateALL_AMNConcept_Types_Contract(AMN_Concept_Types_ID);
		    	// Refresh ALL Dependent TABS
		    	// GridWindow Class
		    	GridWindow gridwin = p_mTab.getGridWindow();
		    	int notabs = gridwin.getTabCount();
		    	// Current TAB is nor Refreshed itab=0
		    	for (int itab = 1; itab < notabs; itab++)
				{
		    		GridTab tabChild = gridwin.getTab(itab);
		    		tabChild.dataRefresh();
		    		//tabChild.dataRefreshAll();
				}
	    	}
	    }

	    return null;
	}


    
}





// AMN_Concept_Type_Proc_ID



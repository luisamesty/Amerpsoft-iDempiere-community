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
import org.amerp.amnmodel.*;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

public class AMN_Concept_Types_Proc_callout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(AMN_Concept_Types_Proc_callout.class);
	
	int Concept_Type_Proc_ID=0;
	int Concept_Type_ID=0;
	int Process_Type_ID=0;
	String sql,Concept_Type_Proc_Name;
	
	@Override
	public String start(Properties p_ctx, int WindowNo, GridTab p_mTab,
			GridField mField, Object value, Object oldValue) {
		// 
		// ******************************
		// FieldRef: AMN_Concept_Types_ID - 
	    //	(Verify if null)	    		
		// ******************************
	    if (p_mTab.getValue( MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Concept_Types_ID) != null) {
	    	Concept_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Concept_Types_ID);
	    	MAMN_Concept_Types amnconcepttypes = new MAMN_Concept_Types(p_ctx, Concept_Type_ID, null);
	    	if (p_mTab.getValue(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Process_ID) != null ) {
		    	Process_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Process_ID);
		    	MAMN_Process amnprocess = new MAMN_Process(p_ctx, Process_Type_ID, null);
		    	p_mTab.setValue("Value", amnprocess.getValue().trim()+amnconcepttypes.getCalcOrder());
		    	Concept_Type_Proc_Name = AmerpUtilities.truncate(String.format("%08d", amnconcepttypes.getCalcOrder()) +'-'+amnconcepttypes.getName().trim() , 59);
		    	p_mTab.setValue("Name", Concept_Type_Proc_Name);
		    	p_mTab.setValue("Description", Concept_Type_Proc_Name);
		    }
	    }
		// ******************************
		// FieldRef: AMN_Contract_ID - 
	    //	(Verify if null)	    		
		// ******************************
	    if (p_mTab.getValue( MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Process_ID) != null) {
	    	Process_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Process_ID);
	    	MAMN_Process amnprocess = new MAMN_Process(p_ctx, Process_Type_ID, null);	    	
	    	if (p_mTab.getValue(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Concept_Types_ID) != null) {
		    	Concept_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Concept_Types_ID);
		    	MAMN_Concept_Types amnconcepttypes = new MAMN_Concept_Types(p_ctx, Concept_Type_ID, null);
		    	p_mTab.setValue("Value", amnprocess.getValue().trim()+amnconcepttypes.getCalcOrder());
		    	Concept_Type_Proc_Name = AmerpUtilities.truncate( String.format("%08d", amnconcepttypes.getCalcOrder())+'-'+amnconcepttypes.getName().trim() , 59);
		    	p_mTab.setValue("Name", Concept_Type_Proc_Name);
		    	p_mTab.setValue("Description", Concept_Type_Proc_Name);
	    	}
	    }
	    return null;
	}
}

// AMN_Concept_Type_Proc_ID



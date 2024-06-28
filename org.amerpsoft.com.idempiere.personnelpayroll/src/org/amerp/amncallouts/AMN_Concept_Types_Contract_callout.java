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

/**
 * @author luisamesty
 *
 */
public class AMN_Concept_Types_Contract_callout implements IColumnCallout {

	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
	int Concept_Type_Contract_ID=0;
	int Concept_Type_ID=0;
	int Contract_Type_ID=0;
	String sql,Concept_Type_Contract_Name;
	
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
	    // TODO Auto-generated method stub
		// ******************************
		// FieldRef: AMN_Concept_Types_ID - 
	    //	(Verify if null)	    		
		// ******************************
	    if (p_mTab.getValue( MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Concept_Types_ID) != null) {
	    	Concept_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Concept_Types_ID);
	    	MAMN_Concept_Types amnconcepttypes = new MAMN_Concept_Types(p_ctx, Concept_Type_ID, null);
	    	if (p_mTab.getValue(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Contract_ID) != null ) {
		    	Contract_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Contract_ID);
		    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_Type_ID, null);
		    	p_mTab.setValue("Value", amncontract.getValue().trim()+amnconcepttypes.getCalcOrder());
		    	Concept_Type_Contract_Name = AmerpUtilities.truncate( amncontract.getValue().trim()+'-'+amnconcepttypes.getName().trim(), 59);
		    	p_mTab.setValue("Name", Concept_Type_Contract_Name);
		    	p_mTab.setValue("Description", Concept_Type_Contract_Name);
	    	}
		}
		// ******************************
		// FieldRef: AMN_Contract_ID - 
	    //	(Verify if null)	    		
		// ******************************
	    if (p_mTab.getValue( MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Contract_ID) != null) {
	    	Contract_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Contract_ID);
	    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_Type_ID, null);
	    	if (p_mTab.getValue(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Concept_Types_ID) != null) {
		    	Concept_Type_ID= (Integer) p_mTab.getValue(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Concept_Types_ID);
		    	MAMN_Concept_Types amnconcepttypes = new MAMN_Concept_Types(p_ctx, Concept_Type_ID, null);
		    	p_mTab.setValue("Value", amncontract.getValue().trim()+amnconcepttypes.getCalcOrder());
		    	Concept_Type_Contract_Name = AmerpUtilities.truncate( amncontract.getValue().trim()+'-'+amnconcepttypes.getName().trim(), 59);
		    	p_mTab.setValue("Name", Concept_Type_Contract_Name);
		    	p_mTab.setValue("Description", Concept_Type_Contract_Name);
		    }
	    }
	    return null;

    }

}

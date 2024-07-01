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
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.amerp.amnmodel.*;

public class AMN_Role_Access_callout implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(AMN_Role_Access_callout.class);
	
	Integer ID_Role = 0 ;
	Integer ID_Process = 0 ;
	Integer ID_Contract = 0 ;
	String Value_Role = "" ;
	String Value_Process = "NN" ;
	String Value_Contract = "" ;
	String Name_Role = "" ;
	String Name_Process = "" ;
	String Name_Contract = "" ;
	String sql ="";
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		
		// TODO 
		// ******************************
    	// FieldRef: AMN_Role_Access_ID
		// ******************************
	    if (mTab.getValue(MAMN_Role_Access.COLUMNNAME_AD_Role_ID) != null) {
		    ID_Role = (Integer) mTab.getValue(MAMN_Role_Access.COLUMNNAME_AD_Role_ID);
			sql = "SELECT name FROM ad_role WHERE ad_role_id=?" ;
			Value_Role = DB.getSQLValueString(null, sql, ID_Role);
			Name_Role = Value_Role;
	    }
		
		// ******************************
		// FieldRef: AMN_Process_ID - 
	    //	(Verify if null)	    		
		// ******************************
	    if (mTab.getValue(MAMN_Role_Access.COLUMNNAME_AMN_Process_ID) != null) {
		    ID_Process = (Integer) mTab.getValue(MAMN_Role_Access.COLUMNNAME_AMN_Process_ID);
		    sql = "select VALUE from amn_process WHERE amn_process_id=?" ;
		    Value_Process = DB.getSQLValueString(null, sql, ID_Process);
		    sql = "select NAME from amn_process WHERE amn_process_id=?" ;
		    Name_Process =  DB.getSQLValueString(null, sql, ID_Process);
	    }
	    // ******************************
		// FieldRef: AMN_Contract_ID 
	    //  (Verify if null)
	    // ******************************
	    if (mTab.getValue(MAMN_Role_Access.COLUMNNAME_AMN_Contract_ID) != null) {
		    ID_Contract = (Integer) mTab.getValue(MAMN_Role_Access.COLUMNNAME_AMN_Contract_ID);
		    sql = "select VALUE from amn_contract WHERE amn_contract_id=?" ;
		    Value_Contract = DB.getSQLValueString(null, sql, ID_Contract);
		    sql = "select NAME from amn_contract WHERE amn_contract_id=?" ;
		    Name_Contract =  DB.getSQLValueString(null, sql, ID_Contract);
		}
	    // ******************************
	    // Set field's values
	    // ******************************
	    //		log.warning("ID         :"+ID_Role+ " - " + ID_Process + " - " + ID_Contract);
	    //		log.warning("Name       :"+Value_Role+ " - " + Value_Process + " - " + Value_Contract);
	    //		log.warning("Description:"+Name_Role+ " - " + Name_Process + " - " + Name_Contract);
		// Set Name-Description
		mTab.setValue(MAMN_Role_Access.COLUMNNAME_Name, Value_Role+ " - " + Value_Process + " - " + Value_Contract);
		mTab.setValue(MAMN_Role_Access.COLUMNNAME_Description, Name_Role+ " - " + Name_Process + " - " + Name_Contract);
		// AMN_Process_Value
		mTab.setValue(MAMN_Role_Access.COLUMNNAME_AMN_Process_Value, Value_Process);
		return null;
	}

}


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
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Concept;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class AMN_Concept_callout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(AMN_Concept_callout.class);
	
	Integer Concept_Type_Proc_ID=0;
	Integer Concept_Type_ID=0;
	String sql,sql1,sql2;
	Integer contador=0;
	Integer sequence=0;
	String Concept_Value = "" ;
	String Concept_Name = "Nombre del Concepto" ;
	String Concept_Formula = "Formula del Concepto" ;
	String Concept_Description = "Descripcon del Concepto" ;
	String Concept_Script = "Script del Concepto" ;
	String Process_Value="";
	Integer Process_ID = 0;
	Integer ID_Process = 0 ;
	String Value_Process,Name_Process = "";
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab,
			GridField mField, Object value, Object oldValue) {
		//
		// ******************************
		// FieldRef: AMN_Process_ID - 
	    //	(Verify if null)	    		
		// ******************************
		
	return null;
	}
}





// AMN_Concept_Type_ID



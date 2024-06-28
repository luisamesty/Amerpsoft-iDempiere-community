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
		// TODO Auto-generated method stub
		// ******************************
		// FieldRef: AMN_Process_ID - 
	    //	(Verify if null)	    		
		// ******************************
	    if (mTab.getValue(MAMN_Concept.COLUMNNAME_AMN_Process_ID) != null) {
		    ID_Process = (Integer) mTab.getValue(MAMN_Concept.COLUMNNAME_AMN_Process_ID);
		    sql = "select VALUE from amn_process WHERE amn_process_id=?" ;
		    Value_Process = DB.getSQLValueString(null, sql, ID_Process);
		    sql = "select NAME from amn_process WHERE amn_process_id=?" ;
		    Name_Process =  DB.getSQLValueString(null, sql, ID_Process);
			mTab.setValue("amn_process_value",Value_Process);
//			log.warning("AMN_Concept_callout................");
//			log.warning("Value_Process:"+Value_Process);
//			log.warning("Name_Process:"+Name_Process);
//			log.warning("sql:"+sql);
	    }
		
		// ****************************************
    	// FieldRef:  AMN_Concept_Types_Proc_ID
		// ****************************************
		if (mTab.getValue(MAMN_Concept.COLUMNNAME_AMN_Concept_Types_Proc_ID) != null) {
		    // *************************************
		    // Get field's values
			// Name,Description,Formula,Calcorder
		    // *************************************
			Concept_Type_Proc_ID = (Integer) mTab.getValue(MAMN_Concept.COLUMNNAME_AMN_Concept_Types_Proc_ID);
		    String sql1 = "SELECT  ctp.value, coalesce(ctp.name,cti.name,'') as name,"
	                   + "coalesce(ctp.description,cti.description,'') as description,"
	                   + "coalesce(cti.formula,'') as formula,"
	                   + "coalesce(cti.calcorder,100) as calcorder,"
	                   + "cti.amn_concept_types_id "
	                   + "FROM amn_concept_types_proc as ctp "
	                   + "LEFT JOIN amn_concept_types as cti ON (ctp.amn_concept_types_id = cti.amn_concept_types_id) "
	                   + "WHERE ctp.amn_concept_types_proc_id=? " 
	                   + " AND cti.isactive ='Y' "
	                   + " AND ctp.isactive ='Y' ";
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    try
		    {
		            pstmt = DB.prepareStatement(sql1, null);
		            pstmt.setInt(1, Concept_Type_Proc_ID);
		            rs = pstmt.executeQuery();
		            if (rs.next())
		            {
		                Concept_Value= rs.getString(1);
		                Concept_Name = rs.getString(2);
		                Concept_Description = rs.getString(3);
		                Concept_Formula = rs.getString(4);
		                // Concept_Script = sql ;
		                sequence=rs.getInt(5);
		                Concept_Type_ID=rs.getInt(6);
		            }
		    }
		    catch (SQLException e)
		    {
		        Concept_Value= Concept_Type_Proc_ID.toString();
		        Concept_Name = "Error - Nombre del Concepto " ;
		    }
		    finally
		    {
		            DB.close(rs, pstmt);
		            rs = null; pstmt = null;
		    }
		    // *************************************
		    // Get field's values
			// AMN_Process_Value (NN,NP,NV,NU....)
		    // *************************************
		    Process_ID = (Integer) mTab.getValue(MAMN_Concept.COLUMNNAME_AMN_Process_ID);
		    sql2 = "select VALUE from amn_process WHERE amn_process_id=?" ;
		    Process_Value = DB.getSQLValueString(null, sql2, Process_ID);
		    // ******************************
		    // Set field's values
		    // ******************************		    
			//log.warning("AMN_Concept_callout................");
			//log.warning("sql1:"+sql1);
			//log.warning("sql2:"+sql2);
			//log.warning("Concept_Type_ID:"+Concept_Type_ID);
			//log.warning("Concept_Type_Proc_ID:"+Concept_Type_Proc_ID);
			//log.warning("Process_Value:"+Process_Value);
			// Set Sequence-Value-Name-Formula
			mTab.setValue("SeqNo", sequence);
			mTab.setValue("Value", Integer.toString(sequence*100));
			mTab.setValue("Name", Concept_Name);
			mTab.setValue("formula", Concept_Formula);
			mTab.setValue("Description",Process_Value + " - " + Concept_Value + " - "+ Concept_Description);
			mTab.setValue("Script",Concept_Script );
			// Set field's values: AMN_Process_Value
			mTab.setValue("AMN_Concept_Types_id",Concept_Type_ID);
		}
	return null;
	}
}





// AMN_Concept_Type_ID



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

import java.util.*;

import javax.script.ScriptException;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.*;
import org.compiere.model.*;
import org.compiere.util.CLogger;

/**
 * @author luisamesty
 *
 */
public class AMN_Payroll_Dates_callout implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(AMN_Payroll_Dates_callout.class);
	// Main Variables
	String sql;
	// AMN_Employee Attributes Variables
	Integer Client_ID = 0;
	Integer AMN_Payroll_ID = 0;	
	Integer AMN_Employee_ID = 0;
	Integer AMN_Process_ID =0;
	Integer AMN_Contract_ID=0;
	Integer AMN_Period_ID=0;
	String PayDocumentNO ="";
	// AMN Dates
	Date AMNDateIni;		// AMNDateIni
	Date AMNDateEnd;		// AMNDateEnd
	boolean isModified = false;
	boolean updateRec = true;
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
	    // TODO Auto-generated method stub
    	//log.warning(".......AMN_Payroll_Dates_callout.......");
		if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Payroll_ID) != null) {
			// AMN_Payroll_ID
			AMN_Payroll_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Payroll_ID);
			AMN_Employee_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Employee_ID);
			AMN_Process_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Process_ID);
			AMN_Contract_ID=(Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Contract_ID);
			AMN_Period_ID=(Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Period_ID);
			PayDocumentNO=  (String) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DocumentNo);
		}
		// ******************************
	 	// FieldRef: InvDateIni - 
	 	//	(Verify if null)	    		
	 	// ******************************
	    if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni) != null) {
			// ******************************
		    // Set field's invdateini 
		    // ******************************	
	    	AMNDateIni=(Date) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni);
			p_mTab.setValue("InvDateIni", AMNDateIni);
	    }
	    // ******************************
	 	// FieldRef: InvDateEnd - 
	 	//	(Verify if null)	    		
	 	// ******************************
	    if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateEnd) != null) {
			// ******************************
	    	// Set field's invdateend 
		    // ******************************	
	    	AMNDateEnd=(Date) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateEnd);
			p_mTab.setValue("InvDateEnd", AMNDateEnd);
	    }
	    return null;
    }
}

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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.*;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.*;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * @author luisamesty
 *
 */
public class AMN_Payroll_callout implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(AMN_Payroll_callout.class);
	// Main Variables
	String sql;
	Integer contador=0;
	Integer sequence=0;
	String Value_Process,Name_Process = "";
	GregorianCalendar cal = new GregorianCalendar();
	// AMN_Employee Attributes Variables
	Integer Client_ID = 0;
	Integer Org_ID =0;
	Integer OrgTo_ID =0;
	Integer Employee_ID = 0;
	Integer Location_ID = 0;
	Integer Jobtitle_ID = 0;
	Integer Jobstation_ID = 0;
	Integer Department_ID = 0;
	Integer Project_ID = 0;
	Integer salesRegion_ID = 0;
	// AMN_Payroll Attributes Variables
	Integer Process_ID = 0;
	Integer Contract_ID = 0;
	String Contract_Value="";
	Integer Period_ID = 0;
	Integer ID_Process = 0 ;
	String Process_Value="";
	String DocumentNO="";
	String PayrollValue="";
	String PayrollName="";
	String PayrollDescription="";
	
	// AMN_Period Attributes Variables
	Date AMNDateIni;		// AMNDateIni
	Date AMNDateEnd;		// AMNDateEnd
	Date AMNDateAcct;		// AMNDateAcct
	Timestamp TSDateAcct;
	// C_Doctype
	Integer DocType_ID;
	// AMN_Contract Attributes Variables
	BigDecimal PayRollDays;
	Integer PayRollDaysInt;
	Integer AcctDow;
	Integer InitDow;
	// 
	MAMN_Employee amnemployee;
	MAMN_Payroll amnpayroll;
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
	    // TODO Auto-generated method stub
    	//log.warning(".......AMN_Payroll_callout.......");
	    // ******************************
	 	// FieldRef: AMN_Employee_ID - 
	 	//	(Verify if null)	    		
	 	// ******************************
	    if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Employee_ID) != null) {
	    	// AMN_Employee
	    	Client_ID = (Integer) p_mTab.getValue(MAMN_Process.COLUMNNAME_AD_Client_ID);
	    	Process_ID = (Integer) p_mTab.getValue(MAMN_Process.COLUMNNAME_AMN_Process_ID);
	    	Contract_ID = (Integer) p_mTab.getValue(MAMN_Contract.COLUMNNAME_AMN_Contract_ID);
	    	Employee_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Employee_ID);
	    	Period_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Period_ID);
	    	// AMN_Employee Cache
	    	MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, Employee_ID, null);
	    	// AD_Org
	    	Org_ID = amnemployee.getAD_Org_ID();
	    	OrgTo_ID = amnemployee.getAD_OrgTo_ID();
	    	if (Org_ID == 0 && OrgTo_ID !=0) {
	    		p_mTab.setValue("AD_Org_ID", OrgTo_ID);
	    	}
	    	// Location_ID = MAMN_Employee.
	    	Location_ID = amnemployee.getAMN_Location_ID();
	    	// Jobtitle_ID = MAMN_Employee.
	    	Jobtitle_ID = amnemployee.getAMN_Jobtitle_ID();
	    	// Jobstation_ID = MAMN_Employee.
	    	Jobstation_ID = amnemployee.getAMN_Jobstation_ID();
	    	// sql = "select amn_location_id from amn_employee WHERE amn_employee_id=?" ;
	    	// Location_ID = DB.getSQLValue(null, sql, Employee_ID);
	    	// Location_ID = amnemployee.sqlGetAMNEmployeeLocationID(Employee_ID);
	    	// Deparment_ID = MAMN_Employee.
	    	Department_ID = amnemployee.getAMN_Department_ID();
	    	// sql = "select amn_department_id from amn_employee WHERE amn_employee_id=?" ;
	    	// Department_ID =  DB.getSQLValue(null, sql, Employee_ID);
	    	// AMN_Period Cache
	    	MAMN_Period amnperiod = new MAMN_Period(p_ctx, Period_ID, null);
	    	// AMN_Period Get AMNDateIni & AMNDateEnd
	    	AMNDateIni = amnperiod.getAMNDateIni();
	    	AMNDateEnd = amnperiod.getAMNDateEnd();
	    	// sql = "select AMNDateIni from amn_period WHERE amn_period_id=?" ;
	    	// AMNDateIni = (Date) DB.getSQLValueTS(null, sql, Period_ID);
	    	// sql = "select AMNDateEnd from amn_period WHERE amn_period_id=?" ;
	    	// AMNDateEnd = (Date) DB.getSQLValueTS(null, sql, Period_ID);	    	
	    	// AMN_Process Cache
	    	MAMN_Process amnprocess = new MAMN_Process(p_ctx, Process_ID, null);
	    	// AMN_Process
	    	// sql = "select value from amn_process WHERE amn_process_id=?" ;
	    	// Process_Value = DB.getSQLValueString(null, sql, Process_ID).trim();	
	    	Process_Value = amnprocess.getValue().trim();
	    	// AMN_Contract Cache
	    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_ID, null);
	    	// AMN_Contract
	    	// sql = "select value from amn_contract WHERE amn_contract_id=?" ;
	    	// Contract_Value = DB.getSQLValueString(null, sql, Contract_ID).trim();	
	    	// sql = "select PayRollDays from amn_contract WHERE amn_contract_id=?" ;
	    	// PayRollDays = (Integer) DB.getSQLValue(null, sql, Contract_ID);	
	    	// sql = "select AcctDow from amn_contract WHERE amn_contract_id=?" ;
	    	// AcctDow = (Integer) DB.getSQLValue(null, sql, Contract_ID);	
	    	// sql = "select InitDow from amn_contract WHERE amn_contract_id=?" ;
	    	// InitDow = (Integer) DB.getSQLValue(null, sql, Contract_ID);	
	    	Contract_Value = amncontract.getValue().trim();
	    	PayRollDays = amncontract.getPayRollDays();
	    	AcctDow = Integer.parseInt(amncontract.getAcctDow().trim());	    	
	    	InitDow = Integer.parseInt(amncontract.getInitDow().trim());	 
	    	// C_Doctype_ID
	    	sql = "select c_doctype_id from c_doctype WHERE ad_client_id="+Client_ID+"  AND docbasetype='HRP' " ;
	    	DocType_ID = (Integer) DB.getSQLValue(null, sql);
	    	
	    	// **************************************************
		    // Set field's AMN_Location_ID 
	    	// Set field's AMN_Department_ID 
	    	// Set field's AMN_Jobtitle_ID, AMN_Jobtitle_ID
		    // **************************************************		
			p_mTab.setValue("AMN_Location_ID", Location_ID);
			p_mTab.setValue("AMN_Department_ID", Department_ID);	
			p_mTab.setValue("AMN_Jobtitle_ID",Jobtitle_ID);
			p_mTab.setValue("AMN_Jobstation_ID",Jobstation_ID);
			// *******************************************
		    // Set field's invdateini 
	    	// Set field's invdateend 
		    // *******************************************		
			p_mTab.setValue("InvDateIni", AMNDateIni);
			p_mTab.setValue("InvDateEnd", AMNDateEnd);
			// ******************************
	    	// Set field's DateAcct 
		    // ******************************
			// Convert To Integer
			PayRollDaysInt =Integer.valueOf(PayRollDays.intValue());
			switch (PayRollDaysInt) {
				case 7: {
						if (InitDow <= AcctDow ) {
							cal.setTime(AMNDateIni);
							cal.add(Calendar.DAY_OF_YEAR,  AcctDow - InitDow );
						} else {
							cal.setTime(AMNDateEnd);
							cal.add(Calendar.DAY_OF_YEAR, AcctDow - InitDow );
						}
					}
					break;
				default:{
            			cal.setTime(AMNDateEnd);
					}
					break;
			}  
			AMNDateAcct= (Date) cal.getTime();
			TSDateAcct = new Timestamp(cal.getTimeInMillis());
			p_mTab.setValue("DateAcct",	 TSDateAcct);
			// ************************************************
	    	// Set field's C_DocType_ID , C_DocTypeTarget_ID
		    // ************************************************
			p_mTab.setValue("C_DocType_ID", DocType_ID);
			p_mTab.setValue("C_DocTypeTarget_ID", DocType_ID);
			// ************************************************
	    	// Set field's Value , Name, Description
		    // ************************************************
			String PayrollValue=AmerpUtilities.truncate((Process_Value+'-'+Contract_Value+'-'+amnemployee.getValue().trim()+'-'+amnperiod.getValue().trim()),39);
			String PayrollName=AmerpUtilities.truncate((Process_Value+'-'+Contract_Value+'-'+amnemployee.getName().trim()),59);
			String PayrollDescription=AmerpUtilities.truncate((Process_Value+'-'+Contract_Value+'-'+amnemployee.getName().trim()+'-'+amnperiod.getValue().trim()),255);
			p_mTab.setValue("Value",PayrollValue);
			p_mTab.setValue("Name",PayrollName);
			p_mTab.setValue("Description",PayrollDescription);
			// TRAZA DE VARIABLES
			//			log.warning("AMN_Payroll_callout................");
			//			log.warning("AMNDateIni:"+AMNDateIni);
			//			log.warning("AMNDateEnd:"+AMNDateEnd);
			//			log.warning("AMNDateAcct:"+AMNDateAcct);
			//			log.warning("TSDateAcct:"+TSDateAcct);
			//			log.warning("PayRollDays:"+PayRollDays);
			//			log.warning("AcctDow:"+AcctDow);
			//			log.warning("InitDow:"+InitDow);

	    }
	    return null;
    }
}

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
  *****************************************************************************/
package org.amerp.process;

import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.amerp.amnmodel.*;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/** AMNPayrollProcessPayrollAssistOneEmployee
 * Description: Procedure called from iDempiere AD
 * 			Process Payroll Attendance for One Employee
 * 			For One Process, One Contract and One Employee
 * 
 * Result:	Create Payroll Attendance on AMN_Payroll_Assist_Proc records 
 * 			according to AMN_Payroll_Assist Inputs and Output
 * 
 * @author luisamesty
 *
 */
public class AMNPayrollProcessPayrollAssistOneEmployee extends SvrProcess {
	
	static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollAssistOneEmployee.class);
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Payroll_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private String p_AMN_Assist_Process_Mode="1";
	private Timestamp p_RefDateIni = null;
	private Timestamp p_RefDateEnd = null;

	String Employee_Name,AMN_Process_Value="";
	String sql="";
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
    @Override
    protected void prepare() {
	    // TODO Auto-generated method stub
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
				p_AMN_Period_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Employee_ID"))
				p_AMN_Employee_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Assist_Process_Mode"))
				p_AMN_Assist_Process_Mode = para.getParameterAsString();
			else if (paraName.equals("RefDateIni"))
				p_RefDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("RefDateEnd"))
				p_RefDateEnd = para.getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		//log.warning("AMN_Employee_ID:"+p_AMN_Employee_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);	    
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {
	    // TODO Auto-generated method stub
    	String Msg_Value="";
    	String eol = System.getProperty("line.separator");
		String Employee_Name="";
	    Msg_Value="";
	    Timestamp p_AMNDateIni;
	    Timestamp p_AMNDateEnd;
	    Timestamp p_currDate;
	    String Period_Name="";
		GregorianCalendar cal = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();		
	    // Determines AD_Org_ID Employee Name
		MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
		Employee_Name=amnemployee.getName();
	    // Get Employeee AMN_Shift_ID by default
	    int defAMN_Shift_ID = amnemployee.getAMN_Shift_ID();
		// Determines AMN_Period - AMN_DateIni and AMNDateEnd
		MAMN_Period amnperiod = new MAMN_Period(Env.getCtx(), p_AMN_Period_ID, null);
		// Take Ref's Dates instead of Period's Dates
		p_AMNDateIni = p_RefDateIni;  // Parameter Value intead of amnperiod.getAMNDateIni();
		p_AMNDateEnd = p_RefDateEnd;  // Parameter Value intead ofamnperiod.getAMNDateEnd();
		Period_Name = amnperiod.getName().trim();
		// Shift
		MAMN_Shift amnshift = new MAMN_Shift(Env.getCtx(), defAMN_Shift_ID, null);
		// AMN_Payroll Must Be DRAFT DR (Not Processed)
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null);
		if (!amnpayroll.getDocStatus().equalsIgnoreCase("DR")) {
			Msg_Value=Msg_Value+Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+amnpayroll.getName();
			Msg_Value=Msg_Value+Msg.getElement(Env.getCtx(),"DocStatus")+":"+amnpayroll.getDocStatus()+
					"  "+" ***** ALREADY PROCESSED ****";
			addLog(Msg_Value);
			return null;
		}
		// AMNPayrollCreatePayrollAssistProc
		Msg_Value=Msg_Value+"  "+(Msg.getElement(Env.getCtx(), "AMN_Employee_ID"))+":"+p_AMN_Employee_ID+" "+Employee_Name.trim()+eol;
		addLog(Msg_Value);
		if (defAMN_Shift_ID==0) {
			Msg_Value = "        ****** "+Msg.getElement(Env.getCtx(), "AMN_Shift_ID")+" = 0   *****";
			addLog(Msg_Value);
		} else {
			Msg_Value=Msg.getElement(Env.getCtx(),"AMN_Shift_ID")+": "+defAMN_Shift_ID+"  "+amnshift.getValue()+"-"+amnshift.getName();
			addLog(Msg_Value);
		}
		Msg_Value=(Msg.getElement(Env.getCtx(), "AMNDateIni"))+": "+(p_AMNDateIni.toString().substring(0,10)+
				"  "+(Msg.getElement(Env.getCtx(), "AMNDateEnd"))+": "+p_AMNDateEnd.toString().substring(0,10)+"  Mode:"+p_AMN_Assist_Process_Mode);
		Msg_Value=Msg_Value+eol;
		addLog(Msg_Value);
		cal.setTime(p_AMNDateIni);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal2.setTime(p_AMNDateEnd);
		cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);
		// Process MODE: p_AMN_Assist_Process_Mode
		// *   "0": Clean Records on AMN_Payroll_Assist_Proc 
		// *   "1": Create or Update Records Only from  AMN_Payroll_Assist
		// *   "2": Create or Update Records from  AMN_Payroll_Assist and Fill Default Values from AMN_Shift_Detail
		// *   "32: Create or Update Records from Only from  AMN_Shift_Detail
    	// Payroll Assist  (One Employee One Date)
		while (cal.getTimeInMillis() <= cal2.getTimeInMillis())   {
			// ARRAY EMPLOYEE - CONTRACT
			p_currDate=new Timestamp(cal.getTimeInMillis());
			Msg_Value = "";
			//Msg_Value = Msg.getMsg(Env.getCtx(), "Date")+": "+p_currDate.toString().substring(0,10)+ "  ";
	    	Msg_Value = Msg_Value + AMNPayrollProcessPayrollAssistProc.CreatePayrollDocumentsAssistProcforEmployeeOneDay(
	    			p_AMN_Contract_ID, p_currDate, p_AMN_Employee_ID, p_AMN_Assist_Process_Mode);
	    	addLog(Msg_Value);
	    	cal.add(Calendar.DAY_OF_MONTH, 1);
		}   	

	    return null;
    }

}
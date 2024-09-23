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

package org.amerp.process;

/**
 * AMNPayrollTransferPayrollAssistOneEmployee
 * Description: Procedure called from iDempiere AD
 * 			Transfer Payroll Attendance Processed and Generated for One Employee
 * 			For One Process, One Contract and One Employee
 * 
 * Result:	Update Payroll Receipt on AMN_Payroll_Detail records 
 * 			according to AMN_Payroll_Assist_Proc Previously Processed.
 * 
 * @author luisamesty
 *
 */

import java.sql.Timestamp;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Shift;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.adempiere.util.Callback;


/*
 * * Description: Procedure called from iDempiere AD
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * @author luisamesty
 */
public class AMNPayrollTransferPayrollAssistOneEmployee extends SvrProcess {
	
	static CLogger log = CLogger.getCLogger(AMNPayrollTransferPayrollAssistOneEmployee.class);
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Payroll_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private Timestamp p_RefDateIni = null;
	private Timestamp p_RefDateEnd = null;
	private String p_AMN_Assist_Process_Mode="1";
	String Employee_Name,AMN_Process_Value="";
	String sql="";
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

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
	   	String Msg_Value="";
    	String eol = System.getProperty("line.separator");
		String Employee_Name="";
	    String retMsg_Value="";
	    Timestamp p_AMNDateIni;
	    Timestamp p_AMNDateEnd;	
	    // Determines AD_Org_ID Employee Name
		MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
		Employee_Name=amnemployee.getName();
	    // Get Employee AMN_Shift_ID by default
	    int defAMN_Shift_ID = amnemployee.getAMN_Shift_ID();
		// Determines AMN_Period - AMN_DateIni and AMNDateEnd
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), p_AMN_Period_ID, null);
		p_AMNDateIni = amnperiod.getAMNDateIni();
		p_AMNDateEnd = amnperiod.getAMNDateEnd();
		// Shift
		MAMN_Shift amnshift = new MAMN_Shift(getCtx(), defAMN_Shift_ID, null);
		// AMN_Payroll Must Be DRAFT DR (Not Processed)
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null);
		if (!amnpayroll.getDocStatus().equalsIgnoreCase("DR")) {
			Msg_Value=Msg_Value+Msg.getElement(getCtx(),"AMN_Payroll_ID")+":"+amnpayroll.getName();
			Msg_Value=Msg_Value+Msg.getElement(getCtx(),"DocStatus")+":"+amnpayroll.getDocStatus()+
					"  "+" ***** ALREADY PROCESSED ****";
			addLog(Msg_Value);
			return null;
		}
		// AMNPayrollCreatePayrollAssistProc
		Msg_Value=Msg_Value+"  "+(Msg.getElement(getCtx(), "AMN_Employee_ID"))+":"+p_AMN_Employee_ID+" "+Employee_Name.trim()+eol;
		addLog(Msg_Value);
		if (defAMN_Shift_ID==0) {
			Msg_Value = "        ****** "+Msg.getElement(getCtx(), "AMN_Shift_ID")+" = 0   *****";
			addLog(Msg_Value);
		} else {
			Msg_Value=Msg.getElement(getCtx(),"AMN_Shift_ID")+": "+defAMN_Shift_ID+"  "+amnshift.getValue()+"-"+amnshift.getName();
			addLog(Msg_Value);
		}
		Msg_Value=(Msg.getElement(getCtx(), "AMNDateIni"))+": "+(p_AMNDateIni.toString().substring(0,10)+
				"  "+(Msg.getElement(getCtx(), "AMNDateEnd"))+": "+p_AMNDateEnd.toString().substring(0,10)+"  Mode:"+p_AMN_Assist_Process_Mode);
		Msg_Value=Msg_Value+eol;
		Msg_Value = Msg_Value + "Prueba String :"+Msg.getAmtInWords(Env.getLoginLanguage(getCtx()), "1234567.89");
		addLog(Msg_Value);
		//
		Msg_Value=(Msg.getElement(getCtx(), "AMN_Employee_ID"))+":"+p_AMN_Employee_ID+" "+Employee_Name.trim()+eol;		
		// Start Attendance Day and end Attendance day from period, payroll
		addLog(Msg_Value);
		// Verifies if Completed !
		if (amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
		{	
			retMsg_Value=" ** ALREADY PROCESSED ** "+
					Msg.getElement(getCtx(),"AMN_Payroll_ID")+":"+amnpayroll.getName()+" \r\n";
					//" Payroll:"+Payroll_Name+" \r\n";
		} else {
			// UPDATE HOUNRS Call  UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod
			retMsg_Value=AMNPayrollTransferPayrollAssist.UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod(
					getCtx(), p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID, p_RefDateIni, p_RefDateEnd, get_TrxName());
					}
		Msg_Value=Msg_Value+"  "+retMsg_Value;
		addLog(Msg_Value);
		return null;
	}
}

// Not Used Code for Asking Parameter

//private void waitFor(StringBuffer string, int timeoutInSeconds) {
//	int sleepms = 200;
//	int maxcycles = timeoutInSeconds * 1000 / sleepms;
//	int cycles = 0;
//	while (string.length() == 0) {
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {}
//		cycles++;
//		if (cycles > maxcycles)
//			throw new AdempiereUserError("Timeout waiting for user answer");
//	}
//}
// IF Null set Star and end Dates from Period
//StartAttendanceDate = amnpayroll.getRefDateIni();
//EndAttendanceDate = amnperiod.getRefDateEnd();
//if (StartAttendanceDate == null)
//		StartAttendanceDate = amnperiod.getRefDateIni();
//if (StartAttendanceDate == null)
//	StartAttendanceDate = amnperiod.getAMNDateIni();
//if (EndAttendanceDate == null)
//	EndAttendanceDate = amnperiod.getRefDateEnd();
//if (EndAttendanceDate == null)
//	EndAttendanceDate = amnperiod.getAMNDateEnd();
// example about how to use the new feature IDEMPIERE-1773 Asking for input from within a process
//final StringBuffer yesno = new StringBuffer();
//final StringBuffer string = new StringBuffer();
//string.append(StartAttendanceDate.toString());
//final StringBuffer stringcaptured = new StringBuffer();
//processUI.ask("Process Attendance, do you want to commit?", new Callback<Boolean>() {
//	@Override
//	public void onCallback(final Boolean result) {
//		addLog("You decided to " + (result ? "commit" : "rollback"));
//		yesno.append(result);
//		if (result) {
//			processUI.askForInput("Please enter Start Date for Attendance Processing:", new Callback<String>() {
//				@Override
//				public void onCallback(String result) {
//					addLog("You entered Start Date for Attendance Processing: " + result);
//					string.append(result);			
//				}
//			});
//			processUI.askForInput("Please enter End Date for Attendance Processing:", new Callback<String>() {
//				@Override
//				public void onCallback(String result) {
//					addLog("You entered End Date for Attendance Processing: " + result);
//					stringcaptured.append("true");
//				}
//			});
//			
//		}
//	}
//});
////wait for answer
//// please note at this moment iDempiere has locks on many tables, some with high-contention,
//// this is why a timeout is implemented
//waitFor(yesno, 50);
//if ("false".equals(yesno.toString())) {
//	throw new AdempiereUserError("User decided to rollback");
//} else {
//	waitFor(stringcaptured, 50);


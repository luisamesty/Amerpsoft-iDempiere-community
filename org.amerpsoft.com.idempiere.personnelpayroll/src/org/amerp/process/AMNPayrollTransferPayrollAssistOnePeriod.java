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
 * AMNPayrollTransferPayrollAssistOnePeriod
 * Description: Procedure called from iDempiere AD
 * 			Transfer Payroll Attendance Processed and Generated for One Period
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * Result:	Update Payroll Receipts on AMN_Payroll_Detail records 
 * 			according to AMN_Payroll_Assist_Proc Previously Processed.
 * 
 * @author luisamesty
 *
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollTransferPayrollAssistOnePeriod extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrollTransferPayrollAssistOnePeriod.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private String p_AMN_Assist_Process_Mode="1";
	private Timestamp p_RefDateIni = null;
	private Timestamp p_RefDateEnd = null;
	String Employee_Name,AMN_Process_Value="";
	String sql="";
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
				p_AMN_Period_ID = para.getParameterAsInt();
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
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID); 	
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
	    // TODO Auto-generated method stub
	    String sql="";
	    String AMN_Process_Value="NN";
	    String AMN_Process_Name="";
	    String Msg_Value="";
	    String retMsg_Value="";
	    int AMN_Employee_ID=0;
	    int AMN_Payroll_ID=0;
	    int p_AD_Client_ID=0;
	    int p_AD_Org_ID=0;
	    Timestamp p_AMNDateIni,p_AMNDateEnd;
	    Timestamp p_currDate;
	    String Period_Name="";
	    String Contract_Name="";
	    String Employee_Name="";
		// Determines AD_Client_ID
		sql = "SELECT ad_client_id FROM amn_contract WHERE amn_contract_id=?" ;
		p_AD_Client_ID=DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		// Determines AD_Org_ID
		sql = "SELECT ad_org_id FROM amn_contract WHERE amn_contract_id=?" ;
		p_AD_Org_ID=DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		// Determines Process Value to see if NN & Name
		//sql = "SELECT amn_process_value FROM amn_process WHERE amn_process_id=?" ;
		//AMN_Process_Value = DB.getSQLValueString(null, sql, p_AMN_Process_ID).trim();
		MAMN_Process amprocess = new MAMN_Process(Env.getCtx(), p_AMN_Process_ID, null);
		AMN_Process_Name = amprocess.getName().trim();
		AMN_Process_Value = amprocess.getValue().trim();
		// AMN_contract
		MAMN_Contract amncontract = new MAMN_Contract(Env.getCtx(), p_AMN_Contract_ID, null);
		Contract_Name = amncontract.getName().trim();
		// Determines AMN_Period - AMN_DateIni and AMNDateEnd
		MAMN_Period amnperiod = new MAMN_Period(Env.getCtx(), p_AMN_Period_ID, null);
		p_AMNDateIni = amnperiod.getAMNDateIni();
		p_AMNDateEnd = amnperiod.getAMNDateEnd();
		Period_Name = amnperiod.getName().trim();
		//
		IProcessUI processMonitor = Env.getProcessUI(Env.getCtx());
		// Message Value Init
		Msg_Value=(Msg.getMsg(Env.getCtx(), "Process")+": "+AMN_Process_Value.trim())+"-"+AMN_Process_Name+" \n";
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(), "AMN_Contract_ID")+": "+Contract_Name+" \r\n";
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(), "AMN_Period_ID")+": "+Period_Name+"  "+
				Msg.getElement(Env.getCtx(), "AMNDateIni")+": "+p_AMNDateIni.toString().substring(0,10)+"  "+
				Msg.getElement(Env.getCtx(), "AMNDateEnd")+": "+p_AMNDateEnd.toString().substring(0,10)+" \r\n";
		addLog(Msg_Value);
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Day")+": "+p_PeriodDate.toString());
			processMonitor.statusUpdate(Msg_Value);
		}
		// ARRAY 
		sql = "SELECT \n" + 
				"amnpr.amn_payroll_id, \n" + 
				"amnpr.amn_employee_id, \n" + 
				"amnpr.name as payroll_name  \n" + 
				"FROM  amn_payroll as amnpr \n" + 
				"WHERE amnpr.amn_period_id=? \n" + 
				"ORDER BY amnpr.value "
				;        		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Period_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Payroll_ID= rs.getInt(1);
				AMN_Employee_ID = rs.getInt(2);
				// AMN_Employee_ID
				MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), AMN_Employee_ID, null);
				Employee_Name = amnemployee.getName();
				p_AMN_Employee_ID=AMN_Employee_ID;
				Msg_Value=Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+":"+amnemployee.getValue().trim()+"-"+
						Employee_Name+" \r\n";				
				addLog(Msg_Value);
				if (processMonitor != null)
				{
					//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Day")+": "+p_PeriodDate.toString());
					processMonitor.statusUpdate(Msg_Value);
				}
				// AMN_Payroll Must Be DRAFT DR (Not Processed)
				MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, null);
				if (!amnpayroll.getDocStatus().equalsIgnoreCase("DR")) {
					Msg_Value=Msg_Value+Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+amnpayroll.getName();
					Msg_Value=Msg_Value+Msg.getElement(Env.getCtx(),"DocStatus")+":"+amnpayroll.getDocStatus()+
							"  "+" ***** ALREADY PROCESSED ****";
					addLog(Msg_Value);
				} else {
					//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
					//log.warning("----- Payroll:"+Payroll_Name+"  AMN_Employee_ID:"+AMN_Employee_ID+"  p_AMN_Assist_Process_Mode:"+p_AMN_Assist_Process_Mode);
					// UPDATE HOUNRS Call  UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod

					if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
					{
						// UPDATE HOUNRS Call  UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod
						retMsg_Value=AMNPayrollTransferPayrollAssist.UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod(
								getCtx(), p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID, p_RefDateIni, p_RefDateEnd, get_TrxName());
					} else {
						retMsg_Value=" ** ALREADY PROCESSED ** "+
							Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+amnpayroll.getName()+" \r\n";
							//" Payroll:"+Payroll_Name+" \r\n";
					}
					Msg_Value=Msg_Value+"  "+retMsg_Value;
					addLog(Msg_Value);
					//Msg_Value="----- Payroll:"+Payroll_Name+"  AMN_Employee_ID:"+AMN_Employee_ID+"  p_AMN_Assist_Process_Mode:"+p_AMN_Assist_Process_Mode;
					//addLog(Msg_Value);
				}
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return Msg_Value;
		}
}

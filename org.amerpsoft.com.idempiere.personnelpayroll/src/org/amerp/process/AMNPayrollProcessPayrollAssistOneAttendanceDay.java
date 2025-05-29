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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Row;
import org.compiere.model.MMessage;
import org.compiere.model.MNote;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * AMNPayrollProcessPayrollAssistOneAttendanceDay
 * Description: Procedure called from iDempiere AD
 * 			Process Payroll Attendance for One Day
 * 			For One Process, All Employees and Contracts
 * 
 * Result:	Create Payroll Attendance on AMN_Payroll_Assist_Proc records 
 * 			according to AMN_Payroll_Assist Inputs and Output
 * 
 * @author luisamesty
 *
 */
public class AMNPayrollProcessPayrollAssistOneAttendanceDay extends SvrProcess {
	
	static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollAssistOneAttendanceDay.class);
	String Msg_Value="";
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private Timestamp p_AMNDateAssist;
	private String p_AMN_Assist_Process_Mode="1";
	private boolean	p_IsScheduled = false;
	
	String Employee_Name="";
	String sql="";
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// DATE
			if (paraName.equals("AMNDateAssist"))
				p_AMNDateAssist =  para.getParameterAsTimestamp();
			else if (paraName.equals("AMN_Assist_Process_Mode"))
				p_AMN_Assist_Process_Mode = para.getParameterAsString();
			else if (paraName.equals("IsScheduled"))
				p_IsScheduled = para.getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		//log.warning("p_AMNDateAssist:"+p_AMNDateAssist+"  p_AMN_Assist_Process_Mode:"+p_AMN_Assist_Process_Mode);	    
	}

	@Override
	protected String doIt() throws Exception {

		// ARRAY Assist for one Day EMPLOYEE - CONTRACT
		// ARRAY 
		//
        String messagetoNotify= "";
	    Properties ctx = getCtx();
        //
		IProcessUI processMonitor = Env.getProcessUI(Env.getCtx());
		sql = "SELECT DISTINCT "+
				"amnem.amn_employee_id, "+
				"amnem.amn_contract_id "+
				"FROM amn_employee amnem "+
 				"LEFT JOIN amn_payroll_assist  amnpa "+
				" ON (amnem.amn_employee_id = amnpa.amn_employee_id) "+
				"WHERE event_date =  ? " +
				"AND amnpa.isactive ='Y' "+
				"ORDER BY amnem.amn_employee_id "
				;        		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setTimestamp(1, p_AMNDateAssist);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				p_AMN_Employee_ID = rs.getInt(1);
				p_AMN_Contract_ID = rs.getInt(2);
				// AMN_Employee_ID
				MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
				Employee_Name = amnemployee.getName();
				Msg_Value=Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+":"+amnemployee.getValue().trim()+"-"+
						Employee_Name+" \r\n";	
				messagetoNotify = messagetoNotify+ Msg_Value;
				if (!p_IsScheduled)
					addLog(Msg_Value);
				if (processMonitor != null)
				{
					// processMonitor
					if (!p_IsScheduled && processMonitor != null) {
						processMonitor.statusUpdate(Msg_Value);
					}
				}
				Msg_Value = "";
				//Msg_Value = Msg.getMsg(Env.getCtx(), "Date")+": "+p_currDate.toString().substring(0,10)+ "  ";
		    	Msg_Value = Msg_Value + AMNPayrollProcessPayrollAssistProc.CreatePayrollDocumentsAssistProcforEmployeeOneDay(
		    			p_AMN_Contract_ID, p_AMNDateAssist, p_AMN_Employee_ID, p_AMN_Assist_Process_Mode, p_IsScheduled);
		    	if (!p_IsScheduled)
		    		addLog(Msg_Value);
		    	messagetoNotify = messagetoNotify+ Msg_Value;
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
		// Send Notification
		messagetoNotify = messagetoNotify + Msg_Value;
        sendNotification(ctx, "N", messagetoNotify);
        log.warning("Notification:\r\n"+ messagetoNotify);
		return messagetoNotify;
	}

	/**
	 * sendNotification
	 * N Notice : E Email : B Both - E Email and Notice : X None
	 * @param leave
	 * @param notificationType
	 * @return
	 */
	private boolean sendNotification(Properties ctx, String notificationType , String notifactionMessage) {
		
		boolean retValue = true;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    	// MOre Option (FUTURE
		// N Notice
    	if (notificationType.compareToIgnoreCase("N") == 0) {
        	MNote note = new MNote(ctx, "ProcessAttendance",getAD_User_ID(), null);
        	note.setTextMsg(notifactionMessage);
        	note.setDescription(MMessage.get(ctx, "Process") +" "+ 
        			Msg.getElement(ctx, "AMN_Payroll_Assist")+" "+ 
        	        Msg.getElement(ctx, "RefDateIni") + ":" + dateFormat.format(p_AMNDateAssist) );
        	note.setAD_Table_ID(MAMN_Payroll_Assist_Proc.Table_ID);
    		note.setRecord_ID(0);
    		note.setAD_Org_ID(0);
    		note.setAD_User_ID(getAD_User_ID());
    		note.saveEx();	
    	} 
    	// E Email
    	else if (notificationType.compareToIgnoreCase("E") == 0) {
    		
    	}
    	// B Both - E Email and Notice
    	else if(notificationType.compareToIgnoreCase("B") == 0) {
    		
    	}
    	// X None
    	else if (notificationType.compareToIgnoreCase("X") == 0) {
    		
    	}
	
		return retValue;
		
	}
}

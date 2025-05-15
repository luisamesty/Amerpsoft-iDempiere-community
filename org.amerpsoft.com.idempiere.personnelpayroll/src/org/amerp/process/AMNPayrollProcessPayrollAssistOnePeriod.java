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
 * AMNPayrollProcessPayrollAssistOnePeriod
 * Description: Procedure called from iDempiere AD
 * 			Process Payroll Attendance for One Period
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * 
 * Result:	Create Payroll Attendance on AMN_Payroll_Assist_Proc records 
 * 			according to AMN_Payroll_Assist Inputs and Output
 * 
 * @author luisamesty
 *
 */
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.*;
import org.compiere.model.MMessage;
import org.compiere.model.MNote;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 * Description: Procedure called from iDempiere AD
 * 			Process Payroll Attendance for OnePeriod 
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * Result:	Create all Payroll Attendance on AMN_Payroll_Assist_Proc records 
 * 			according to AMN_Payroll_Assist Inputs and Output
 * @author luisamesty
 *
 */

public class AMNPayrollProcessPayrollAssistOnePeriod extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollAssistOnePeriod.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private String p_AMN_Assist_Process_Mode="1";
	private Timestamp p_RefDateIni = null;
	private Timestamp p_RefDateEnd = null;
	private boolean	p_IsScheduled = false;
	
	String Employee_Name,AMN_Process_Value="";
	String sql="";
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
    @Override
    protected void prepare() {
	    // TODO Auto-generated method stub
      	// log.warning("...........Toma de Parametros...................");
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
			else if (paraName.equals("IsScheduled"))
				p_IsScheduled = para.getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID); 
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {
	    // 
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    String sql="";
	    String AMN_Process_Value="NN";
	    String AMN_Process_Name="";
	    String Msg_Value="";
        String messagetoNotify= "";
	    String messagetoShow="";
	    Properties ctx = getCtx();
	    int AMN_Employee_ID=0;
	    int AMN_Payroll_ID=0;
	    Timestamp p_currDate;
	    String Period_Name="";
	    String Contract_Name="";
	    String Employee_Name="";
		// Determines Process Value to see if NN & Name
		MAMN_Process amprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
		AMN_Process_Name = amprocess.getName().trim();
		AMN_Process_Value = amprocess.getValue().trim();
		// AMN_contract
		MAMN_Contract amncontract = new MAMN_Contract(ctx, p_AMN_Contract_ID, null);
		Contract_Name = amncontract.getName().trim();
		// Determines AMN_Period - AMN_DateIni and AMNDateEnd
		MAMN_Period amnperiod = new MAMN_Period(ctx, p_AMN_Period_ID, null);
		// Si no se proporcionaron fechas, usa las del período
		if (p_RefDateIni == null && p_RefDateEnd == null) {
		    p_RefDateIni = amnperiod.getAMNDateIni();
		    p_RefDateEnd = amnperiod.getAMNDateEnd();
		}
		// Validar que las fechas estén dentro del período
		boolean fechasInvalidas = p_RefDateIni.after(p_RefDateEnd)
		        || p_RefDateIni.before(amnperiod.getAMNDateIni())
		        || p_RefDateIni.after(amnperiod.getAMNDateEnd())
		        || p_RefDateEnd.after(amnperiod.getAMNDateEnd())
		        || p_RefDateEnd.before(amnperiod.getAMNDateIni());

		if (fechasInvalidas) {
		    Msg_Value = Msg.getElement(ctx, "RefDateIni") + ": " + dateFormat.format(p_RefDateIni) + "\r\n";
		    addLogIFNotScheduled(Msg_Value);
		    Msg_Value = Msg.getElement(ctx, "RefDateEnd") + ": " + dateFormat.format(p_RefDateEnd) + "\r\n";
		    addLogIFNotScheduled(Msg_Value);
		    addLogIFNotScheduled(" ***** DATE REFERENCE ERROR ****");
		    return "@Error@ ";
		}	
		Period_Name = amnperiod.getName().trim();
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// Message Value Init
		Msg_Value=(Msg.getMsg(ctx, "Process")+": "+AMN_Process_Value.trim())+"-"+AMN_Process_Name+"\r\n";
		addLogIFNotScheduled(Msg_Value);
		messagetoNotify = Msg_Value;
		Msg_Value=Msg.getElement(ctx, "AMN_Contract_ID")+": "+Contract_Name+" \r\n";
		addLogIFNotScheduled(Msg_Value);
		messagetoNotify = messagetoNotify + Msg_Value;
		Msg_Value=Msg.getElement(ctx, "AMN_Period_ID")+": "+Period_Name+"  "+
				Msg.getElement(ctx, "AMNDateIni")+": "+dateFormat.format(p_RefDateIni)+"  "+
				Msg.getElement(ctx, "AMNDateEnd")+": "+dateFormat.format(p_RefDateEnd)+" \r\n";
		addLogIFNotScheduled(Msg_Value);
		messagetoNotify = messagetoNotify + Msg_Value;
		if (!p_IsScheduled && processMonitor != null) {
			processMonitor.statusUpdate(Msg_Value);
		}
		
        // Determina el Numero de Registros
        int rowCount = 0;
        String countSql = "SELECT COUNT(*) FROM ( "
        		+ "	SELECT DISTINCT "
        		+ "	amnpr.amn_payroll_id, "
        		+ "	amnpr.amn_employee_id "
        		+ "	FROM  amn_payroll as amnpr "
        		+ "	WHERE amnpr.amn_period_id=? "
        		+ "	GROUP BY amnpr.amn_payroll_id, amnpr.amn_employee_id "
        		+ ") AS recs";
        
        PreparedStatement countStmt = DB.prepareStatement(countSql, get_TrxName());
        countStmt.setInt (1, p_AMN_Period_ID);
        ResultSet countRs = countStmt.executeQuery();
        if (countRs.next()) {
        	rowCount = countRs.getInt(1);  // Obtiene el total de filas
        }
        DB.close(countRs, countStmt); // Cierra el ResultSet y el PreparedStatement
		
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
        int percent = 0;
        int rowNumber = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Period_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				rowNumber++; // Incrementamos en cada iteración
				AMN_Payroll_ID= rs.getInt(1);
				AMN_Employee_ID = rs.getInt(2);
				// AMN_Employee_ID
				MAMN_Employee amnemployee = new MAMN_Employee(ctx, AMN_Employee_ID, null);
				Employee_Name = String.format("%-30.30s", amnemployee.getName()); // Se asegura que tenga 30 caracteres
				p_AMN_Employee_ID=AMN_Employee_ID;
				Msg_Value=Msg.getElement(ctx, "AMN_Employee_ID")+":"+amnemployee.getValue().trim()+"-"+
						Employee_Name+" \r\n";				
				addLogIFNotScheduled(Msg_Value);
				// Percentage Monitor
                percent = (int) (100.0 * rowNumber / rowCount);
                int totalLength = 100; // Longitud deseada
				// AMN_Payroll Must Be DRAFT DR (Not Processed)
				MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, null);
				if (!amnpayroll.getDocStatus().equalsIgnoreCase("DR")) {
					Msg_Value=Msg_Value+Msg.getElement(ctx,"AMN_Payroll_ID")+":"+amnpayroll.getName();
					Msg_Value=Msg_Value+Msg.getElement(ctx,"DocStatus")+":"+amnpayroll.getDocStatus()+
							"  "+" ***** ALREADY PROCESSED ****";
					addLogIFNotScheduled(Msg_Value);
					if (!p_IsScheduled && processMonitor != null) {
						messagetoShow = String.format("%-10s", rowNumber) + "/" + 
				                String.format("%-10s", rowCount) + " ( " + 
				                String.format("%6s", percent) + "% ) : " + 
				                String.format("%-10s", amnemployee.getValue()) + " - " + 
				                String.format("%-30s", amnemployee.getName().trim()) + " - " + 
								String.format("%-40s",Msg.getElement(ctx,"DocStatus")+":"+amnpayroll.getDocStatus()+
								"  "+" ***** ALREADY PROCESSED ****");
		                messagetoShow = String.format("%-" + totalLength + "s", messagetoShow);
						processMonitor.statusUpdate(Msg_Value);
					}
				} else {
					//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
					//log.warning("----- Payroll:"+Payroll_Name+"  AMN_Employee_ID:"+AMN_Employee_ID+"  p_AMN_Assist_Process_Mode:"+p_AMN_Assist_Process_Mode);
					//  PROCESS MAMN_Payroll_Assist
					// Process MODE: p_AMN_Assist_Process_Mode
					// *   "0": Clean Records on AMN_Payroll_Assist_Proc 
					// *   "1": Create or Update Records Only from  AMN_Payroll_Assist
					// *   "2": Create or Update Records from  AMN_Payroll_Assist and Fill Default Values from AMN_Shift_Detail
					// *   "32: Create or Update Records from Only from  AMN_Shift_Detail
			    	// Payroll Assist  (One Employee One Date)
					GregorianCalendar cal = new GregorianCalendar();
					GregorianCalendar cal2 = new GregorianCalendar();	
					cal.setTime(p_RefDateIni);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					cal2.setTime(p_RefDateEnd);
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
						//Msg_Value = Msg.getMsg(ctx, "Date")+": "+p_currDate.toString().substring(0,10)+ "  ";
				    	Msg_Value = Msg_Value + AMNPayrollProcessPayrollAssistProc.CreatePayrollDocumentsAssistProcforEmployeeOneDay(
				    			p_AMN_Contract_ID, p_currDate, p_AMN_Employee_ID, p_AMN_Assist_Process_Mode, p_IsScheduled);
				    	addLogIFNotScheduled(Msg_Value);
				    	if (!p_IsScheduled && processMonitor != null) {
							messagetoShow = String.format("%-10s", rowNumber) + "/" + 
					                String.format("%-10s", rowCount) + " ( " + 
					                String.format("%6s", percent) + "% ) : " + 
					                String.format("%-10s", amnemployee.getValue()) + " - " + 
					                String.format("%-30s", amnemployee.getName().trim()) + " - " + 
					                String.format("%-20s", p_currDate) + 
					                String.format("%-20s", " Processed ");
			                messagetoShow = String.format("%-" + totalLength + "s", messagetoShow);
							processMonitor.statusUpdate(messagetoShow);
						}
				    	cal.add(Calendar.DAY_OF_MONTH, 1);
					}
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
        	note.setDescription(MMessage.get(ctx, "ProcessAttendance") +
        	        Msg.translate(ctx, "From") + " " +
        	        Msg.getElement(ctx, "RefDateIni") + ":" + dateFormat.format(p_RefDateIni) + " " +
        	        Msg.translate(ctx, "To") + " " +
        	        Msg.getElement(ctx, "RefDateEnd") + ":" + dateFormat.format(p_RefDateEnd));
        	note.setAD_Table_ID(MAMN_Payroll_Assist_Row.Table_ID);
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
	
	/**
	 * addLogIFNotScheduled
	 * @param message
	 * @param isScheduled
	 */
	private void addLogIFNotScheduled(String message) {
		
		if (!p_IsScheduled) {
			addLog(message);
		}
		
	}

}

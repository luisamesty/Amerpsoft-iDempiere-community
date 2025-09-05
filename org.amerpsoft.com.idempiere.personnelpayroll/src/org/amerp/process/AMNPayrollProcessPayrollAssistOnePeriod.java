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

        final int BATCH_SIZE = 20;
        int offset = 0;
        boolean hasMoreRows = true;
        int rowNumber = 0;
        int rowCount = 0;
        int percent = 0;
	    String messagetoShow="";
	    
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Properties ctx = getCtx();
        IProcessUI processMonitor = Env.getProcessUI(ctx);
        String messagetoNotify = "";
        String Msg_Value = "";

        // Validaciones iniciales de Periodo
        MAMN_Process amprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
        MAMN_Contract amncontract = new MAMN_Contract(ctx, p_AMN_Contract_ID, null);
        MAMN_Period amnperiod = new MAMN_Period(ctx, p_AMN_Period_ID, null);

        if (p_RefDateIni == null && p_RefDateEnd == null) {
            p_RefDateIni = amnperiod.getAMNDateIni();
            p_RefDateEnd = amnperiod.getAMNDateEnd();
        }

        boolean fechasInvalidas = p_RefDateIni.after(p_RefDateEnd)
                || p_RefDateIni.before(amnperiod.getAMNDateIni())
                || p_RefDateEnd.after(amnperiod.getAMNDateEnd());

        if (fechasInvalidas) {
            addLog(Msg.getMsg(ctx, "DateReferenceError"));
            return "@Error@";
        }

        // Conteo total de empleados
        String countSql = 
            "SELECT COUNT(DISTINCT emp.amn_employee_id) " +
            "FROM amn_period prd " +
            "JOIN amn_contract cnt ON cnt.amn_contract_id = prd.amn_contract_id " +
            "JOIN amn_employee emp ON emp.amn_contract_id = cnt.amn_contract_id " +
            "WHERE prd.amn_process_id = ? AND prd.amn_period_id = ? AND emp.isactive='Y'";

        try (PreparedStatement countStmt = DB.prepareStatement(countSql, get_TrxName())) {
            countStmt.setInt(1, p_AMN_Process_ID);
            countStmt.setInt(2, p_AMN_Period_ID);
            try (ResultSet countRs = countStmt.executeQuery()) {
                if (countRs.next()) {
                    rowCount = countRs.getInt(1);
                }
            }
        }

        // Query principal con LIMIT/OFFSET
        String sql = 
            "SELECT emp.amn_employee_id, emp.name, COALESCE(prl.amn_payroll_id,0) " +
            "FROM amn_period prd " +
            "JOIN amn_contract cnt ON cnt.amn_contract_id = prd.amn_contract_id " +
            "JOIN amn_employee emp ON emp.amn_contract_id = cnt.amn_contract_id " +
            "LEFT JOIN amn_payroll prl ON prl.amn_employee_id = emp.amn_employee_id " +
            "   AND prl.amn_period_id = prd.amn_period_id " +
            "WHERE prd.amn_process_id = ? AND prd.amn_period_id = ? AND emp.isactive='Y' " +
            "ORDER BY emp.name LIMIT ? OFFSET ?";

        while (hasMoreRows) {
            int rowsThisBatch = 0;
            Trx trx = Trx.get(Trx.createTrxName("AMNPayrollBatch"), true);

            try (PreparedStatement pstmt = DB.prepareStatement(sql, trx.getTrxName())) {
                pstmt.setInt(1, p_AMN_Process_ID);
                pstmt.setInt(2, p_AMN_Period_ID);
                pstmt.setInt(3, BATCH_SIZE);
                pstmt.setInt(4, offset);

                try (ResultSet rs = pstmt.executeQuery()) {
                	while (rs.next()) {
                	    rowsThisBatch++;
                	    rowNumber++;

                	    int employeeId = rs.getInt(1);
                	    String employeeName = rs.getString(2);
                	    int payrollId = rs.getInt(3);

                	    MAMN_Employee amnemployee = new MAMN_Employee(ctx, employeeId, trx.getTrxName());

                	    // Calcular porcentaje
                	    percent = (int)(100.0 * rowNumber / rowCount);
                	    int totalLength = 100;

                	    // Verificar si la nómina ya está procesada
                	    if (payrollId != 0) {
                	        MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, payrollId, trx.getTrxName());
                	        if (!"DR".equalsIgnoreCase(amnpayroll.getDocStatus())) {
                	            Msg_Value = Msg.getElement(ctx, "AMN_Employee_ID") + ": " + amnemployee.getValue() + " - " +
                	                    String.format("%-30s", amnemployee.getName().trim()) + " ";
                	            Msg_Value = Msg_Value + Msg.getElement(ctx,"AMN_Payroll_ID") + ":" + amnpayroll.getName() + " ";
                	            Msg_Value = Msg_Value + Msg.getElement(ctx,"DocStatus") + ":" + amnpayroll.getDocStatus() +
                	                    "  ***** ALREADY PROCESSED ****";
                	            addLogIFNotScheduled(Msg_Value);

                	            if (!p_IsScheduled && processMonitor != null) {
                	                messagetoShow = String.format("%-10s", rowNumber) + "/" + 
                	                        String.format("%-10s", rowCount) + " ( " + 
                	                        String.format("%6s", percent) + "% ) : " + 
                	                        String.format("%-10s", amnemployee.getValue()) + " - " + 
                	                        String.format("%-30s", amnemployee.getName().trim()) + " - " + 
                	                        String.format("%-40s",
                	                                Msg.getElement(ctx,"DocStatus") + ":" + amnpayroll.getDocStatus() +
                	                                "  ***** ALREADY PROCESSED ****");
                	                messagetoShow = String.format("%-" + totalLength + "s", messagetoShow);
                	                processMonitor.statusUpdate(messagetoShow);
                	            }
                	            continue;
                	        }
                	    }

                	    // Procesar cada día
                	    GregorianCalendar cal = new GregorianCalendar();
                	    GregorianCalendar cal2 = new GregorianCalendar();
                	    cal.setTime(p_RefDateIni);
                	    cal2.setTime(p_RefDateEnd);
                	    cal.set(Calendar.HOUR_OF_DAY, 0);
                	    cal2.set(Calendar.HOUR_OF_DAY, 0);

                	    while (!cal.after(cal2)) {
                	        Timestamp currDate = new Timestamp(cal.getTimeInMillis());
//                	        String result = AMNPayrollProcessPayrollAssistProc.CreatePayrollDocumentsAssistProcforEmployeeOneDay(
//                	                ctx,
//                	                amnemployee.getAD_Client_ID(),
//                	                amnemployee.getAD_Org_ID(),
//                	                p_AMN_Contract_ID,
//                	                currDate,
//                	                employeeId,
//                	                p_AMN_Assist_Process_Mode,
//                	                p_IsScheduled
//                	        );
                	        
            		    	AMNPayrollProcessPayrollAssistProcess pppa = new AMNPayrollProcessPayrollAssistProcess();		    	
            		    	String result = pppa.CreatePayrollDocumentsAssistProcforEmployeeOneDay(
            		    			ctx, 
                	                amnemployee.getAD_Client_ID(),
                	                amnemployee.getAD_Org_ID(),
            		    			p_AMN_Contract_ID, 
            		    			currDate, 
            		    			employeeId,
            		    			p_AMN_Assist_Process_Mode, 
            		    			p_IsScheduled);
            	        
                	        addLogIFNotScheduled(result);

                	        if (!p_IsScheduled && processMonitor != null) {
                	            messagetoShow = String.format("%-10s", rowNumber) + "/" + 
                	                    String.format("%-10s", rowCount) + " ( " + 
                	                    String.format("%6s", percent) + "% ) : " + 
                	                    String.format("%-10s", amnemployee.getValue()) + " - " + 
                	                    String.format("%-30s", amnemployee.getName().trim()) + " - " + 
                	                    String.format("%-20s", currDate.toString().substring(0,10)) + " Processed";
                	            messagetoShow = String.format("%-" + totalLength + "s", messagetoShow);
                	            processMonitor.statusUpdate(messagetoShow);
                	        }

                	        cal.add(Calendar.DAY_OF_MONTH, 1);
                	    }
                	}

                }

                trx.commit();

            } catch (Exception e) {
                trx.rollback();
                throw e;
            } finally {
                trx.close();
            }

            hasMoreRows = rowsThisBatch == BATCH_SIZE;
            offset += BATCH_SIZE;

            Env.clearWinContext(ctx, 0);
            System.gc();
            Thread.sleep(500);
        }

        // Mensaje final
        messagetoNotify = Msg.getMsg(ctx, "ProcessCompleted") + ": " + rowNumber + " " + Msg.getElement(ctx, "RecordsProcessed");
        sendNotification(ctx, "N", messagetoNotify);

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

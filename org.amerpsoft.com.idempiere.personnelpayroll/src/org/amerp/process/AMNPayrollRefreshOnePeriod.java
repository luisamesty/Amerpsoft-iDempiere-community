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
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.amerp.amnutilities.AmerpPayrollCalcArray;
import org.amerp.amnutilities.PayrollScriptEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/** AMNPayrollRefreshOnePeriod
 * Description: Procedure called from iDempiere AD
 * 			Refresh or Recalculate Payroll Receipts
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * Result:	Recalculate all PayrollReceipt according to Payroll Concepts Rules
 * 			Update Header on AMN_Payroll and Lines on AMN_Payroll_detail
 * 			Using AmerpPayrollCalc Class
 * 
 * @author luisamesty
 *
 */
public class AMNPayrollRefreshOnePeriod extends SvrProcess{
	
	static CLogger log = CLogger.getCLogger(AMNPayrollRefreshOnePeriod.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	String Employee_Name,AMN_Process_Value="";
	String sql="";
	private String MessagetoShow = "";
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
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
				p_AMN_Period_ID = para.getParameterAsInt();
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
		AmerpPayrollCalcArray amerpPayrollCalcArray = new AmerpPayrollCalcArray();
	    String sql="";
	    String AMN_Process_Value="NN";
	    String Payroll_Name="";
	    String Msg_Value="";
	    int AMN_Payroll_ID=0;
		sql = "SELECT amn_process_value FROM amn_process WHERE amn_process_id=?" ;
		AMN_Process_Value = DB.getSQLValueString(null, sql, p_AMN_Process_ID).trim();
		// Message Value Init
		Msg_Value=Msg_Value+(Msg.getMsg(Env.getCtx(), "Process")+":"+AMN_Process_Value.trim())+" \n";
		IProcessUI processMonitor = Env.getProcessUI(getCtx());
		// ARRAY DOCS FOR EMPLOYEE - CONTRACT
		sql = "SELECT \n" + 
				"amnpr.amn_payroll_id, \n" + 
				"amnpr.amn_employee_id, \n" + 
				"amnpr.name as payroll_name  \n" + 
				"FROM  amn_payroll as amnpr \n" + 
				"WHERE amnpr.amn_period_id=? \n" + 
				"ORDER BY payroll_name"
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
				Payroll_Name = rs.getString(3).trim();
				Msg_Value=(Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+Payroll_Name).trim()+" \r\n";
				MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, null);
				MAMN_Employee employee = new MAMN_Employee(getCtx(), amnpayroll.getAMN_Employee_ID(), null);
				//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
				if (processMonitor != null) {
					MessagetoShow = Msg.translate(Env.getCtx(), "AMN_Payroll_ID")+":"+String.format("%-20s", amnpayroll.getDocumentNo().trim()).replace(' ', '_') +"-"+
					String.format("%-20s", employee.getValue().trim()).replace(' ', '_') +"-" +
					String.format("%-40s", employee.getName().trim()).replace(' ', '_') ;;
					processMonitor.statusUpdate(MessagetoShow);
				}
				//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
				//log.warning("MessagetoShow:"+MessagetoShow);
				if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
				{	
			    	amerpPayrollCalcArray.PayrollEvaluationArrayCalculate(getCtx(), AMN_Payroll_ID);
				} else {
					Msg_Value=Msg_Value+" ** ALREADY PROCESSED - CAN'T BE RECALCULATED ** "+
							Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+Payroll_Name+" \r\n";
				}
				addLog(Msg_Value);
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


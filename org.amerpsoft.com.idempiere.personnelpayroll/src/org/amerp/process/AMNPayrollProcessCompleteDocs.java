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

import java.sql.*;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.amerp.amnmodel.MAMN_Period;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 * Description: Procedure called from iDempiere AD
 * 			Process and Accounts Payroll Receipts for One Period
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * Result:	Update Payroll Receipts on AMN_Payroll records 
 * 			From DR Draft Mode to CO Completed.
 * 
 * @author luisamesty
 *
 */

public class AMNPayrollProcessCompleteDocs extends SvrProcess{

	static CLogger log = CLogger.getCLogger(AMNPayrollProcessCompleteDocs.class);

	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
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
	    // TODO Auto-generated method stub
	    String sql="";
	    String AMN_Process_Value="NN";
	    String Payroll_Name="";
	    String Msg_Value="";
	    String Msg_Value0="";
	    int AMN_Payroll_ID=0;
	    // Determines AD_Client_ID
		sql = "SELECT ad_client_id FROM amn_contract WHERE amn_contract_id=?" ;
		DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		// Determines AD_Org_ID
		sql = "SELECT ad_org_id FROM amn_contract WHERE amn_contract_id=?" ;
		DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		// Determines Process Value to see if NN
		sql = "SELECT amn_process_value FROM amn_process WHERE amn_process_id=?" ;
		AMN_Process_Value = DB.getSQLValueString(null, sql, p_AMN_Process_ID).trim();
		// Message Value Init
		Msg_Value0=(Msg.getMsg(Env.getCtx(), "Process")+":"+AMN_Process_Value.trim())+" \n";
		addLog(Msg_Value0);
		Msg_Value=Msg_Value+Msg_Value0;	
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), p_AMN_Period_ID, null);
		// ARRAY DOCS FOR EMPLOYEE - CONTRACT
		sql = "SELECT \n" + 
				"amnpr.amn_payroll_id, \n" + 
				"amnpr.amn_employee_id, \n" + 
				"amnpr.name as payroll_name  \n" + 
				"FROM  amn_payroll as amnpr \n" + 
				"WHERE amnpr.amn_period_id=? \n" + 
				"ORDER BY amnpr.value"
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
				rs.getInt(2);
				Payroll_Name = rs.getString(3).trim();
				Msg_Value0=(Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+Payroll_Name).trim()+" \r\n";
				addLog(Msg_Value0);
				Msg_Value=Msg_Value+Msg_Value0;	
				MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, null);
				MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(getCtx(), 0, get_TrxName());
				//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
				//log.warning("------------------Payroll:"+Payroll_Name);
				//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
				if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)
						&& MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(AMN_Payroll_ID) > 0)
				{
					// Process document
					boolean ok = amnpayroll.processIt(MAMN_Payroll.DOCACTION_Complete);
					amnpayroll.saveEx();
					if (!ok)
					{
						//Msg_Value=Msg_Value+" ** ERROR PROCESSING **  Payroll:"+Payroll_Name+" \r\n";
						Msg_Value0=" ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **   \r\n";
					} else {
						Msg_Value0=" ** "+Msg.getMsg(Env.getCtx(), "Success")+" **   \r\n";
					}
					addLog(Msg_Value0);
					Msg_Value=Msg_Value+Msg_Value0;
				    // Nominal Salary UPDATE ONLY ON NN Process
					if (AMN_Process_Value.equalsIgnoreCase("NN")) {
						Msg_Value0= amnpayrollhistoric.updateSalaryAmnPayrollHistoric(getCtx(), null, amnpayroll.getAMN_Employee_ID(), 
					    		amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), amnpayroll.getC_Currency_ID(), get_TrxName())+"\r\n";
						Msg_Value=Msg_Value+Msg_Value0;
					}
				    // Nominal Salary UPDATE END
				} else {
					if (amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)) {
						Msg_Value0=" *** ADVERTENCIA "+Msg.getMsg(Env.getCtx(),"completed")+" *** \r\n";
						addLog(Msg_Value0);
						Msg_Value=Msg_Value+Msg_Value0;						}
					if (MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(AMN_Payroll_ID) <= 0) {
						Msg_Value0="*** ERROR "+Msg.getMsg(Env.getCtx(),"NoOfLines")+" = 0 *** \r\n";
						addLog(Msg_Value0);
						Msg_Value=Msg_Value+Msg_Value0;						}
						//" Payroll:"+Payroll_Name+" \r\n";
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
		//addLog(Msg_Value);
		return Msg_Value;
    }

}

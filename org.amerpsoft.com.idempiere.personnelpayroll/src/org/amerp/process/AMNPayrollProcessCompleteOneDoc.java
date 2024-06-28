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
/** AMNPayrollProcessOneDoc
 * Description: Procedure called from iDempiere AD
 * 			Process and Accounts Payroll Receipt for One Employee
 * 			For One Process, One Contract and One Employee 
 * 			For One Employee on Contract and Period.
 * Result:	Update Payroll Receipt on AMN_Payroll record
 * 			From DR Draft Mode to CO Completed.
 * 
 * @author luisamesty
 *
 */
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */
public class AMNPayrollProcessCompleteOneDoc extends SvrProcess{
	
	static CLogger log = CLogger.getCLogger(AMNPayrollProcessCompleteOneDoc.class);

	int p_AMN_Payroll_ID=0;
	String Msg_Value="";
	String Msg_Value0="";
	String AMN_Payroll_Value="";
	String AMN_Process_Value="";
	String AMN_Payroll_Name="";
	String AMN_Payroll_Description="";
	String sql="";

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
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
			if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID =  para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}
		//log.warning("------------------Payroll_ID:"+p_AMN_Payroll_ID);
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {
	    // TODO Auto-generated method stub
//    	sql = "SELECT value FROM amn_payroll WHERE amn_payroll_id=?" ;
//		AMN_Payroll_Value = DB.getSQLValueString(null, sql, p_AMN_Payroll_ID).trim();
//    	sql = "SELECT name FROM amn_payroll WHERE amn_payroll_id=?" ;
//		AMN_Payroll_Name = DB.getSQLValueString(null, sql, p_AMN_Payroll_ID).trim();
//    	sql = "SELECT description FROM amn_payroll WHERE amn_payroll_id=?" ;
//		AMN_Payroll_Description = DB.getSQLValueString(null, sql, p_AMN_Payroll_ID).trim();
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null);
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), amnpayroll.getAMN_Period_ID(), null);
		MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(getCtx(), 0, get_TrxName());
		MAMN_Process amnprocess = new MAMN_Process(getCtx(), amnpayroll.getAMN_Process_ID(), null);
		AMN_Payroll_Value = amnpayroll.getValue();
		AMN_Payroll_Name = amnpayroll.getName();
		AMN_Payroll_Description = amnpayroll.getDescription();
		AMN_Process_Value = amnprocess.getAMN_Process_Value();
		Msg_Value0=Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \r\n";
		addLog(Msg_Value0);
		Msg_Value=Msg_Value+Msg_Value0;
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
		//log.warning("------------------Payroll_ID:"+p_AMN_Payroll_ID+"  AMN_Payroll_Name:"+AMN_Payroll_Name);
		//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
		if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)
				&& MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(p_AMN_Payroll_ID) > 0)
		{
			// Process document
			Msg_Value0=amnpayroll.getSummary();
			addLog(Msg_Value0);
			Msg_Value=Msg_Value+Msg_Value0;
			boolean ok = amnpayroll.processIt(MAMN_Payroll.DOCACTION_Complete);
			amnpayroll.saveEx();
			if (!ok)
			{
				//Msg_Value=Msg_Value+" ** ERROR PROCESSING **  Payroll:"+AMN_Payroll_Name+" \r\n";
				Msg_Value0=" ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **   \r\n";
			} else {
				Msg_Value0=" ** "+Msg.getMsg(Env.getCtx(), "Success")+" **   \r\n";
			}
			addLog(Msg_Value0);
			Msg_Value=Msg_Value+Msg_Value0;	
			// UPDATE SALARY IF NN
		    // Nominal Salary UPDATE ONLY ON NN Process
			if (AMN_Process_Value.equalsIgnoreCase("NN")) {
				Msg_Value0= amnpayrollhistoric.updateSalaryAmnPayrollHistoric(getCtx(), null, amnpayroll.getAMN_Employee_ID(), 
			    		amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), amnpayroll.getC_Currency_ID(), get_TrxName())+"\r\n";
				Msg_Value=Msg_Value+Msg_Value0;
			}
			// Nominal Salary UPDATE END
		} else {
			if (amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)) {
				Msg_Value0="*** ADVERTENCIA "+Msg.getMsg(Env.getCtx(),"completed")+" *** \r\n";
				addLog(Msg_Value0);
				Msg_Value=Msg_Value+Msg_Value0;			}
			if (MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(p_AMN_Payroll_ID) <= 0) {
				Msg_Value0="*** ERROR "+Msg.getMsg(Env.getCtx(),"NoOfLines")+" = 0 *** \r\n";
				addLog(Msg_Value0);
				Msg_Value=Msg_Value+Msg_Value0;			}
//			Msg_Value=Msg_Value+" ** ALREADY PROCESSED **  Payroll:"+AMN_Payroll_Name+" \r\n";
//			Msg_Value=Msg_Value+" ** ALREADY PROCESSED ** "+
//					Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+AMN_Payroll_Name+" \r\n";
		}

	    return null;
    }

}

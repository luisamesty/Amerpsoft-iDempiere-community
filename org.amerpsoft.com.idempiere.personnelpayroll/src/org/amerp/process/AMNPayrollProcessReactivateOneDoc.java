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

/** AMNPayrollProcessReactivateOneDoc
 * Description: Procedure called from iDempiere AD
 * 			Process and Accounts Payroll Receipt for One Employee
 * 			For One Process, One Contract and One Employee 
 * 			For One Employee on Contract and Period.
 * Result:	Reactivate Payroll Receipt on AMN_Payroll record
 * 			From CO Completed to DR Draft Mode.
 * 
 * @author luisamesty
 *
 */
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Payroll;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */

public class AMNPayrollProcessReactivateOneDoc extends SvrProcess {

	int p_AMN_Payroll_ID=0;
	String Msg_Value="";
	String AMN_Payroll_Value="";
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
	    
    }


	@Override
	protected String doIt() throws Exception {
	    // TODO Auto-generated method stub
		// Message Value Init
    	// Determines Process Value to see if NN
    	sql = "SELECT value FROM amn_payroll WHERE amn_payroll_id=?" ;
		AMN_Payroll_Value = DB.getSQLValueString(null, sql, p_AMN_Payroll_ID).trim();
    	sql = "SELECT name FROM amn_payroll WHERE amn_payroll_id=?" ;
		AMN_Payroll_Name = DB.getSQLValueString(null, sql, p_AMN_Payroll_ID).trim();
    	sql = "SELECT description FROM amn_payroll WHERE amn_payroll_id=?" ;
		AMN_Payroll_Description = DB.getSQLValueString(null, sql, p_AMN_Payroll_ID).trim();

		Msg_Value=Msg_Value+ Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \n";
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null);
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
		//log.warning("------------------Payroll_ID:"+p_AMN_Payroll_ID+"  AMN_Payroll_Name:"+AMN_Payroll_Name);
		//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
		if (amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
		{
			// Process document
			Msg_Value=Msg_Value+amnpayroll.getSummary();
			boolean ok = amnpayroll.reActivateIt();
			amnpayroll.saveEx();
			if (!ok)
			{
				//Msg_Value=Msg_Value+" ** ERROR PROCESSING **  Payroll:"+AMN_Payroll_Name+" \r\n";
				Msg_Value=Msg_Value+" ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **  "+
						Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+AMN_Payroll_Name+" \r\n";
			}
		} else {
			Msg_Value=Msg_Value+" ** NOT PROCESSED **  Payroll:"+AMN_Payroll_Name+" \r\n";
			Msg_Value=Msg_Value+" ** NOT PROCESSED ** "+
					Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+AMN_Payroll_Name+" \r\n";
		}

	    return null;
	}

}

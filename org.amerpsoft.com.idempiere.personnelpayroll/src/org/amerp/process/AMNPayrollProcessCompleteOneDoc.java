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
	String sql="";
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
    @Override
    protected void prepare() {
	    // Parameters
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID =  para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {
	    // Get Payroll Attributes
		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, get_TrxName()); 
		MAMN_Process amnprocess = new MAMN_Process(getCtx(), amnpayroll.getAMN_Process_ID(), get_TrxName());
		AMN_Payroll_Value = amnpayroll.getValue();
		AMN_Process_Value = amnprocess.getAMN_Process_Value();
		Msg_Value0=Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \r\n";
		addLog(Msg_Value0);
		Msg_Value=Msg_Value+Msg_Value0;
		//  PROCESS MAMN_Payroll (DOCUMENT HEADER) AND associated Documents
		if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)
				&& MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(p_AMN_Payroll_ID, get_TrxName()) > 0) {
			// Process AMN_Payroll and Invoice if apply
			Msg_Value0 = amnpayroll.processAMN_Payroll(getCtx(), amnpayroll, amnprocess, get_TrxName());
			Msg_Value=Msg_Value+Msg_Value0;	
		} else {
			if (amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)) {
				Msg_Value0="*** ADVERTENCIA "+Msg.getMsg(Env.getCtx(),"completed")+" *** \r\n";
				addLog(Msg_Value0);
				Msg_Value=Msg_Value+Msg_Value0;			}
			if (MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(p_AMN_Payroll_ID, get_TrxName()) <= 0) {
				Msg_Value0="*** ERROR "+Msg.getMsg(Env.getCtx(),"NoOfLines")+" = 0 *** \r\n";
				addLog(Msg_Value0);
				Msg_Value=Msg_Value+Msg_Value0;			}
		}
		addLog(Msg_Value);
	    return  Msg_Value;
    }

}

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
import org.amerp.amnmodel.MAMN_Process;
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
	String Msg_Value0="";
	String AMN_Payroll_Value="";
	String AMN_Payroll_Name="";
	String sql="";
	boolean ok = false;
   	boolean okinvoice = false;
	String AMN_Process_Value="";
	
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

		MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, get_TrxName()); 
		MAMN_Process amnprocess = new MAMN_Process(getCtx(), amnpayroll.getAMN_Process_ID(), get_TrxName());
		AMN_Payroll_Value = amnpayroll.getValue();
		AMN_Payroll_Name = amnpayroll.getName();
		AMN_Process_Value = amnprocess.getAMN_Process_Value();
		
		Msg_Value=Msg_Value+ Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \r\n";
		addLog(Msg_Value);
		
		Msg_Value0=Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \r\n";
		addLog(amnpayroll.getSummary());
		Msg_Value=Msg_Value+Msg_Value0;
		// Reactivate MAMN_Payroll (DOCUMENT HEADER) AND associated Documents
		Msg_Value0 = amnpayroll.reactivateAMN_Payroll(getCtx(), amnpayroll, amnprocess, get_TrxName());
		Msg_Value=Msg_Value+Msg_Value0;
		addLog(Msg_Value);
	    return  Msg_Value;
	}

}

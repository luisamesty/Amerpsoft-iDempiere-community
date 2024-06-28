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
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Employee_Salary;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.X_AMN_Concept_Types;
import org.amerp.amnmodel.X_AMN_Process;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
/** AMNPayrollProcessHistoricSalaryOneDoc
 * Description: Procedure called from iDempiere AD
 * 			Process Social Benefit Payroll Receipt for One Employee
 * 			For One Process, One Contract and One Employee 
 * 			For One Employee on Contract and Period.
 * Result:	Update Employee Salary Historic accumulating Social Benefits 
 * 			Resumed values.
 * 
 * @author luisamesty
 *
 */
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollProcessHistoricSalaryOneDoc extends SvrProcess{

	static CLogger log = CLogger.getCLogger(AMNPayrollProcessHistoricSalaryOneDoc.class);

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

		//  MAcctSchema Select Client Default 
		MClientInfo info = MClientInfo.get(Env.getCtx(), amnpayroll.getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// Currency	
		int p_currency =  as.getC_Currency_ID() ; //Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		//
		AMN_Payroll_Value = amnpayroll.getValue();
		AMN_Payroll_Name = amnpayroll.getName();
		AMN_Payroll_Description = amnpayroll.getDescription();
		Msg_Value=Msg_Value+ Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \n";
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
		//log.warning("------------------Payroll_ID:"+p_AMN_Payroll_ID+"  AMN_Payroll_Name:"+AMN_Payroll_Name);
		//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
		if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
		{
			// Process document
			Msg_Value=Msg_Value+" ** COMPLETE RECEIPT BEFORE **  Payroll:"+AMN_Payroll_Name+" \r\n";

		} else {
			Msg_Value=Msg_Value+amnpayroll.getSummary();
			//boolean ok = amnpayroll.processIt(MAMN_Payroll.DOCACTION_Complete);
//			Msg_Value=Msg_Value+" ** ALREADY PROCESSED **  Payroll:"+AMN_Payroll_Name+" \r\n";
//			Msg_Value=Msg_Value+" ** ALREADY PROCESSED ** "+
//					Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+":"+AMN_Payroll_Name+" \r\n";

			// AMN_Employee_salary EMPLOYEEHISTORIC TABLE
//log.warning("AMN_Payroll_ID():"+amnpayroll.getAMN_Payroll_ID()+"  p_currency="+p_currency);
			MAMN_Employee_Salary.updateAMN_Employee_Salary(Env.getCtx(), Env.getLanguage(Env.getCtx()).getLocale(), 
					amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID(), 
					p_currency, amnpayroll.getAMN_Payroll_ID());
		}

	    return null;
    }

}

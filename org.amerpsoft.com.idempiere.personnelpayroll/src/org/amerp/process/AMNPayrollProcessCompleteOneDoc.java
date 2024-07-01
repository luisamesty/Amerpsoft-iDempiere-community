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
import java.util.Properties;
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
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrg;
import org.compiere.process.DocAction;
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
	boolean okprocess = false;
	boolean okinvoice = false;
	
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
		//  PROCESS MAMN_Payroll (DOCUMENT HEADER)
		if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed)
				&& MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(p_AMN_Payroll_ID, get_TrxName()) > 0) {
			// Process AMN_Payroll and Invoice if apply
			Msg_Value0 = processAMN_Payroll(getCtx(), amnpayroll, amnprocess, get_TrxName());
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
		if (okinvoice &&  okprocess)
			Msg_Value = "@Ok@";
		else 
			Msg_Value = "@KO@";
	    return  Msg_Value;
    }

    /**
     * processAMN_Payroll
     * Process AMN_Payroll and C_Invoice if apply
     * @param ctx
     * @param amnpayroll
     * @param amnprocess
     * @param trxName
     * @return
     */
    private String processAMN_Payroll(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocess, String trxName) {
    	
    	int C_Invoice_ID = 0;
    	MInvoice invoice = null;
    	String P_Msg_Value="";
		MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(getCtx(), 0, trxName);
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), amnpayroll.getAMN_Period_ID(), trxName);
		MClient cli = new MClient(getCtx(), amnpayroll.getAD_Client_ID(), trxName);
		MOrg org = new MOrg(getCtx(),  amnpayroll.getAD_Org_ID(), trxName);  	
    	String Msg_Value1 =amnpayroll.getSummary();
		addLog(Msg_Value1);
		P_Msg_Value=P_Msg_Value+Msg_Value1;
		C_Invoice_ID = amnpayroll.getC_Invoice_ID();
		// Verify if it is Document Controlled
		if (amnprocess.isDocControlled()) {
			// Creates Invoice and Complete it
			if(C_Invoice_ID != 0) {
				invoice = MInvoice.get(getCtx(),C_Invoice_ID);
				if (invoice == null) {
					C_Invoice_ID = 0;
					invoice = new MInvoice(getCtx(), C_Invoice_ID, trxName);
				}
			} else {
				invoice = new MInvoice(getCtx(), C_Invoice_ID, trxName);
			}
			// Invoice Header
			amnpayroll.createCInvoice(getCtx(), cli.getAD_Client_ID(), org.getAD_Org_ID(), p_AMN_Payroll_ID,  invoice, trxName);
			log.warning("Invoice:"+invoice.getDocumentNo() +"  AD_Org_ID:"+ invoice.getAD_Org_ID() );
			// Invoice Lines
			okinvoice = amnpayroll.createCInvoiceLines(getCtx(), invoice, cli.getAD_Client_ID(),  org.getAD_Org_ID(), amnpayroll.getAMN_Payroll_ID(),  amnprocess.getAMN_Process_ID(), trxName);
			// Complete Invoice
			if (invoice.getDocStatus().compareToIgnoreCase(DocAction.STATUS_Drafted) ==0) {
				// Process INVOICE
				okinvoice = invoice.processIt(DocAction.STATUS_Completed);
			}
		} else {
			okinvoice = true;
		}
		// Process AMN_Payroll
		try {
			okprocess = amnpayroll.processIt(MAMN_Payroll.DOCACTION_Complete);
		} catch (Exception e) {
			// 
			Msg_Value1 = e.getMessage();
		}
		P_Msg_Value=P_Msg_Value+Msg_Value1;
		// Set C_Invoice_ID y Payroll Header
		if (amnprocess.isDocControlled()) {
			amnpayroll.setC_Invoice_ID(invoice.getC_Invoice_ID());
		}
		amnpayroll.saveEx();
		// Verify if ERRORs
		if ( !okprocess || !okinvoice)
		{
			//Msg_Value=Msg_Value+" ** ERROR PROCESSING **  Payroll:"+AMN_Payroll_Name+" \r\n";
			Msg_Value1=" ** "+Msg.getMsg(Env.getCtx(), "PocessFailed")+" **   \r\n";	
		} else {
			Msg_Value1=" ** "+Msg.getMsg(Env.getCtx(), "Success")+" **   \r\n";
		}
		addLog(Msg_Value1);
		P_Msg_Value=P_Msg_Value+Msg_Value1;	
		// UPDATE SALARY IF NN
	    // Nominal Salary UPDATE ONLY ON NN Process
		if (AMN_Process_Value.equalsIgnoreCase("NN")) {
			Msg_Value1= amnpayrollhistoric.updateSalaryAmnPayrollHistoric(getCtx(), null, amnpayroll.getAMN_Employee_ID(), 
		    		amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), amnpayroll.getC_Currency_ID(), trxName)+"\r\n";
			P_Msg_Value=P_Msg_Value+Msg_Value1;
		}
		// 
		return P_Msg_Value;
    }
}

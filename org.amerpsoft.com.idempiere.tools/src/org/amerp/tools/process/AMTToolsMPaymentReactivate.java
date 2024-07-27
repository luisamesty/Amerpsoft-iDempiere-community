package org.amerp.tools.process;

import java.util.logging.Level;

import org.amerp.tools.amtmodel.AMTToolsMInvoice;
import org.amerp.tools.amtmodel.AMTToolsMPayment;
import org.compiere.model.MBPartner;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsMPaymentReactivate  extends SvrProcess {
	
	int p_C_Payment_ID=0;
	String Msg_Value="";
	String sql="";
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
			if (paraName.equals("C_Payment_ID"))
				p_C_Payment_ID =  para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		boolean isAllocPay = true;	
		boolean isBSLPay = true;
		MPayment mpayment = new MPayment(getCtx(), p_C_Payment_ID, null);
//log.warning("------------------C_Payment_ID:"+p_C_Payment_ID+"  DocumentNo:"+mpayment.getDocumentNo());
		MBPartner mbpartner = new MBPartner(getCtx(), mpayment.getC_BPartner_ID(), null);
		Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+mpayment.getDocumentNo();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DateAcct")+":"+mpayment.getDateAcct()+"  "+Msg.getElement(Env.getCtx(),"DateTrx")+":"+mpayment.getDateTrx();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+mpayment.getDescription();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"C_BPartner_ID")+":"+mbpartner.getValue().trim()+"-"+mbpartner.getName().trim();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DocStatus")+":"+mpayment.getDocStatus();
		addLog(Msg_Value);
		// INVOICE HEADER
		AMTToolsMPayment amtmpayment = new AMTToolsMPayment(getCtx(), p_C_Payment_ID, null);
		// VERIFY DOC STATUS
		if (amtmpayment.DOCSTATUS_Drafted.equals(amtmpayment.getDocStatus())
				// || minvoice.DOCSTATUS_Invalid.equals(minvoice.getDocStatus())
				// || minvoice.DOCSTATUS_InProgress.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_Approved.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_NotApproved.equals(minvoice.getDocStatus()) 
				)
		{
			Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+amtmpayment.getDocStatus()+" **** ESTA SIN COMPLETAR ****";
			addLog(Msg_Value);
		} else {
			// Verify C_AllocationLine
			String AllocPay = amtmpayment.C_AllocationLine_C_Payment(p_C_Payment_ID, get_TrxName());
			if (AllocPay.compareToIgnoreCase("OK")==0) 
				isAllocPay = false;
			else
				isAllocPay = true;
			// Verify C_BankStatementLine
			String BankStatementLinePay = amtmpayment.C_BankStatementLine_C_Payment(p_C_Payment_ID, get_TrxName());
			if (BankStatementLinePay.compareToIgnoreCase("OK")==0) 
				isBSLPay = false;
			else
				isBSLPay = true;
			if (isAllocPay == false && isBSLPay == false) {
				// Message from C_AllocationLine Table
				Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+" **** NO TIENE ASIGNACION DE PAGOS****";
				addLog(Msg_Value);
				Msg_Value= AllocPay.trim();
				addLog(Msg_Value);
				// Message from C_BankStatementLine Table
				Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+" **** NO TIENE ESTADO DE CUENTA BANCARIO CARGADO ****";
				addLog(Msg_Value);
				Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+" ****         EN ESTADO COMPLETADO               ****";
				addLog(Msg_Value);
				Msg_Value= BankStatementLinePay.trim();
				addLog(Msg_Value);	
				// UPDATE REVERSAL_ID IF EXISTS
				AMTToolsMPayment.updateC_Payment_Reversal_ID(p_C_Payment_ID, get_TrxName());
				// UPDATE IsReconciled
				AMTToolsMPayment.updateC_Payment_IsReconciled(p_C_Payment_ID, get_TrxName());
				// REACTIVATE INVOICE HEADER
				amtmpayment.reActivateIt( get_TrxName());
				// 
				Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+mpayment.getDocumentNo().trim()+"  **** ESTA REACTIVADO ****";
				addLog(Msg_Value);

			} else {
				if (isAllocPay == true ) {
					Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+" **** TIENE ASIGNACION DE PAGOS****";
					addLog(Msg_Value);
					Msg_Value= AllocPay.trim();
					addLog(Msg_Value);
				}
				if (isBSLPay == true ) {
					Msg_Value=Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"+" **** TIENE ESTADO DE CUENTA CARGADO ****";
					addLog(Msg_Value);
					Msg_Value= AllocPay.trim();
					addLog(Msg_Value);
				}
			}
		}
		return null;
	}

}

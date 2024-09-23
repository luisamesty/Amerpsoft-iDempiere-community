package org.amerp.tools.process;

import java.util.logging.Level;

import org.amerp.tools.amtmodel.AMTToolsMBankStatement;
import org.compiere.model.MBankAccount;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsCBankAccountReactivate  extends SvrProcess {

	int p_C_BankStatement_ID=0;
	int C_BankAccount_ID=0;
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
			if (paraName.equals("C_BankStatement_ID"))
				p_C_BankStatement_ID =  para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		AMTToolsMBankStatement mbankstatement = new AMTToolsMBankStatement(getCtx(), p_C_BankStatement_ID, null);
log.warning("------------------C_BankStatement_ID:"+p_C_BankStatement_ID+"  DocumentNo:"+mbankstatement.getDocumentNo());
		MBankAccount mbankaacount = new MBankAccount(getCtx(), mbankstatement.getC_BankAccount_ID(), null);
		Msg_Value=Msg.translate(Env.getCtx(),"C_BankStatement_ID")+":"+mbankstatement.getDocumentNo();
		addLog(Msg_Value);
		Msg_Value=Msg.translate(Env.getCtx(),"DateAcct")+":"+mbankstatement.getDateAcct()+"  "+Msg.getElement(Env.getCtx(),"DateAcct")+":"+mbankstatement.getDateAcct();
		addLog(Msg_Value);
		Msg_Value=Msg.translate(Env.getCtx(),"Description")+":"+mbankstatement.getDescription();
		addLog(Msg_Value);
		Msg_Value=Msg.translate(Env.getCtx(),"C_BankAccount_ID")+":"+mbankaacount.getValue().trim()+"-"+mbankaacount.getName().trim();
		addLog(Msg_Value);
		Msg_Value=Msg.translate(Env.getCtx(),"AccountNo")+":"+mbankaacount.getAccountNo().trim();
		addLog(Msg_Value);
		Msg_Value=Msg.translate(Env.getCtx(),"DocStatus")+":"+mbankstatement.getDocStatus();
		addLog(Msg_Value);
		// BANK STATEMENT HEADER
		// VERIFY DOC STATUS
		if (mbankstatement.DOCSTATUS_Drafted.equals(mbankstatement.getDocStatus()) ) {
			Msg_Value=Msg.translate(Env.getCtx(),"C_BankStatement_ID")+":"+mbankstatement.getDocStatus()+ "\r\n"
					+Msg.translate(Env.getCtx(),"status")+":"+
					" **** "+Msg.translate(Env.getCtx(),"document.status.drafted")+" ****";
			addLog(Msg_Value);
		} else {	
			// REACTIVATE INVOICE HEADER
			mbankstatement.reActivateIt( get_TrxName());
			// 
			Msg_Value=Msg.getElement(Env.getCtx(),"C_BankStatement_ID")+":"+mbankstatement.getDocumentNo().trim()+
					"  **** ESTA REACTIVADO ****";
			addLog(Msg_Value);
			// UPDATE processed = 'N'
			AMTToolsMBankStatement.updateC_BankStatement_ID(getCtx(), p_C_BankStatement_ID, get_TrxName());
			// UPDATE isreconciled = 'N'
			AMTToolsMBankStatement.updateC_Payment_ID_isreconciled(getCtx(), p_C_BankStatement_ID, get_TrxName());
		}
		return null;
	}
}

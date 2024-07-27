package org.amerp.tools.process;

import java.util.logging.Level;

import org.amerp.tools.amtmodel.AMTToolsMJournal;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsGLJournalReactivate extends SvrProcess{
	
	int p_GL_Journal_ID=0;
	String Msg_Value="";
	String sql="";
	int no = 0;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	ProcessInfoParameter[] paras = getParameter();
			for (ProcessInfoParameter para : paras)
			{
				String paraName = para.getParameterName();
				if (paraName.equals("GL_Journal_ID"))
					p_GL_Journal_ID =  para.getParameterAsInt();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}	 
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		AMTToolsMJournal gljournal = new AMTToolsMJournal(getCtx(), p_GL_Journal_ID, null);
//log.warning("------------------GL_Journal_ID:"+p_GL_Journal_ID+"  DocumentNo:"+gljournal.getDocumentNo());
		Msg_Value=Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+gljournal.getDocumentNo();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DateAcct")+":"+gljournal.getDateAcct()+
				" "+Msg.getElement(Env.getCtx(),"DateDoc")+gljournal.getDateDoc();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+gljournal.getDescription();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DocStatus")+":"+gljournal.getDocStatus();
		addLog(Msg_Value);
		// VERIFY DOC STATUS
		if (gljournal.DOCSTATUS_Drafted.equals(gljournal.getDocStatus())
				// || minvoice.DOCSTATUS_Invalid.equals(minvoice.getDocStatus())
				// || minvoice.DOCSTATUS_InProgress.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_Approved.equals(minvoice.getDocStatus())
				//|| minvoice.DOCSTATUS_NotApproved.equals(minvoice.getDocStatus()) 
				)
		{
			Msg_Value=Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+" **** ESTA SIN COMPLETAR ****";
			addLog(Msg_Value);
		} else {
			if (gljournal.DOCSTATUS_Reversed.equals(gljournal.getDocStatus())) {
				
				no = AMTToolsMJournal.updateGL_Journal_Reversal_ID(p_GL_Journal_ID, get_TrxName());
				//
				if (no > 0) {
					Msg_Value=Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+gljournal.getDocumentNo();
					addLog(Msg_Value);
					Msg_Value=Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+" *** TIENE DOCUMENTO DE REVERSO ****";
					addLog(Msg_Value);
					Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+gljournal.getDescription();
					addLog(Msg_Value);
				}
				// REACTIVATE INVOICE HEADER
				gljournal.reActivateIt( get_TrxName());
			} else {
				// REACTIVATE INVOICE HEADER
				gljournal.reActivateIt( get_TrxName());
				
			}
		}
		return null;
	}

}

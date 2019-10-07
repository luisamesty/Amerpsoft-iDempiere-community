package org.globalqss.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.globalqss.model.MLCOInvoiceWHDoc;
import org.globalqss.model.MLCOInvoiceWHDocLines;

public class LCO_InvoiceWHDocOpen extends SvrProcess {
	
	int p_LCO_InvoiceWHDoc_ID=0;
	String p_DocStatus="";
	String p_Processed="";
	private String isProcessed = "";
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	
	@Override
	protected void prepare() {
	   	ProcessInfoParameter[] paras = getParameter();
			for (ProcessInfoParameter para : paras)
			{
				String paraName = para.getParameterName();
				if (paraName.equals("LCO_InvoiceWHDoc_ID"))
					p_LCO_InvoiceWHDoc_ID =  para.getParameterAsInt();
				else if (paraName.equals("DocStatus"))
					p_DocStatus =  para.getParameterAsString();
				else if (paraName.equals("Processed"))
					p_Processed =  para.getParameterAsString();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}	 
		
	}

	@Override
	protected String doIt() throws Exception {
		MLCOInvoiceWHDoc  whdoc = new MLCOInvoiceWHDoc (getCtx(), p_LCO_InvoiceWHDoc_ID, get_TrxName());

		if (log.isLoggable(Level.INFO)) log.info("doIt - " + whdoc);
		// Verify IF NOT Processed
		if (!whdoc.isProcessed()) {
			   addLog( " ** "+Msg.getMsg(Env.getCtx(), "ProcessFailed")+" **  "+  " \r\n" );
			   addLog( Msg.getElement(Env.getCtx(),"LCO_InvoiceWHDoc_ID") +": "+ whdoc.getDocumentNo()+  " \r\n" );
			   addLog( Msg.getElement(Env.getCtx(),"DateDoc") +": "+ whdoc.getDateDoc()+  " \r\n" );
			   if (whdoc.isProcessed())  isProcessed= "(SI)" ;
					else isProcessed= "(NO)"; 
			   addLog( Msg.getElement(Env.getCtx(),"Processed") +": "+ isProcessed + "\r\n" );
			   addLog( " ** "+Msg.getMsg(Env.getCtx(),"AlreadyPosted") +" **  "+  " \r\n" );
		} else {
			
			MLCOInvoiceWHDocLines[] whdoclines = whdoc.getLines();
			
			//	Close lines
			for (int line = 0; line < whdoclines.length; line++)
			{
				whdoclines[line].setProcessed(false);
				whdoclines[line].saveEx();
			}
			whdoc.setProcessed(false);
			whdoc.setDocStatus(DOCSTATUS_Drafted);
			whdoc.saveEx();
			// Final Message
		    addLog( " ** "+Msg.getMsg(Env.getCtx(), "ProcessOK")+" **  "+  " \r\n" );
			   addLog( Msg.getElement(Env.getCtx(),"LCO_InvoiceWHDoc_ID") +": "+ whdoc.getDocumentNo()+  " \r\n" );
			   addLog( Msg.getElement(Env.getCtx(),"DateDoc") +": "+ whdoc.getDateDoc()+  " \r\n" );
			   if (whdoc.isProcessed())  isProcessed= "(SI)" ;
					else isProcessed= "(NO)";  
		    addLog( Msg.getElement(Env.getCtx(),"Processed") +": "+ isProcessed + "\r\n" );			

		}
		return "";
	}

}

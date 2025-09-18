package org.amerp.process;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Docs;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpMsg;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollDeleteOneDoc extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrollDeleteOneDoc.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Payroll_ID = 0;

	String AMN_Process_Value="";


    int NoRecs = 0;
    int NoRecsLines = 0;
    String Msg_Error="";
	static Timestamp p_DateAcct = null;
	static Timestamp p_InvDateEnd = null; 
	static Timestamp p_InvDateIni = null;
	static Timestamp p_RefDateEnd = null;
	static Timestamp p_RefDateIni = null;
	// Receipt List
	List<AMNReceipts> ReceiptsGenList = new ArrayList<AMNReceipts>();
	// Receipt Lines List
	List<AMNReceiptLines> ReceiptConcepts = new ArrayList<AMNReceiptLines>();
	
	@Override
	protected void prepare() {
	    //log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
				p_AMN_Period_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID = para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}


	@Override
	protected String doIt() throws Exception {
		
	    String Msg_Value="";
	    String MessagetoShow="";
	    boolean okReceipts = false;
	    int AMN_Payroll_ID = 0;
	    MAMN_Payroll amnp = null;
	    MAMN_Employee emp = null;
	    String DocumentNo = "";
	    MAMN_Process amnprocess = new MAMN_Process(getCtx(), p_AMN_Process_ID, null); 
	    AMN_Process_Value = amnprocess.getAMN_Process_Value();
		MAMN_Period amnperiod = new MAMN_Period(getCtx(), p_AMN_Period_ID, null);
		// Default Account Schema
		MClientInfo info = MClientInfo.get(getCtx(), amnprocess.getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (getCtx(), info.getC_AcctSchema1_ID(), null);
		as.getC_AcctSchema_ID();
   		
   		// ************************
    	// Process VALIDATION	
    	// ************************
	    if (AMN_Process_Value.equalsIgnoreCase("NN") ||
	    		AMN_Process_Value.equalsIgnoreCase("TI") ||
	    		AMN_Process_Value.equalsIgnoreCase("NO") || 
	    		AMN_Process_Value.equalsIgnoreCase("NU") ||
	    		AMN_Process_Value.equalsIgnoreCase("NP") ){
	    	// *************************
	    	// Process NN PO TI NO NU NP	
	    	// *************************
	    	// log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    	Msg_Error="";
	    	okReceipts = AMNPayrollDeleteDocs.amnPayrollDeleteReceiptsGeneratePayrollArrays(getCtx(), amnperiod, p_AMN_Payroll_ID, 
	    			ReceiptsGenList, ReceiptConcepts, Msg_Error, get_TrxName());
	    } else {
	    	Msg_Value = (Msg.getMsg(getCtx(), "Process")+":"+AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n");
	    }
	    // UN SOLO RECIBO
	    if ( okReceipts && ReceiptsGenList.size() == 1 ) {
	    	
			if( Msg_Error.isEmpty() ) {
				
			    IProcessUI processMonitor = Env.getProcessUI(getCtx());
			    int Percent = 0;
		    	// END MESSAGE
				Msg_Value = Msg.getElement(getCtx(), "AMN_Process_ID")+":"+amnprocess.getName().trim();
			    addLog(Msg_Value);
			    Msg_Value = Msg.getElement(getCtx(), "AMN_Payroll_ID")+" "+
			    		Msg.getElement(getCtx(), "Qty")+
			    		" ("+ReceiptsGenList.size()+") :";
			    addLog(Msg_Value);		
			    Msg_Value = Msg.getElement(getCtx(), "AMN_Payroll_Detail_ID")+" "+
			    		Msg.getElement(getCtx(), "Qty")+
			    		" ("+ReceiptConcepts.size()+") :";
			    addLog(Msg_Value);	
			    // DELETE AMN_Payroll_Detail LINES
			    NoRecsLines = ReceiptConcepts.size();
			    if(NoRecsLines > 0) {
				    AMN_Payroll_ID = ReceiptConcepts.get(0).getAMN_Payroll_ID();
				    // FOR all Rreceipt Lines
				    for (int i=0 ; i < ReceiptConcepts.size() ; i++) {
				    	// Check if same AMN_Payroll_ID
						if (ReceiptConcepts.get(i).getAMN_Payroll_ID() != AMN_Payroll_ID) {
							// Change
							AMN_Payroll_ID = ReceiptConcepts.get(i).getAMN_Payroll_ID();
						}
						amnp = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, get_TrxName());
						emp = new MAMN_Employee(getCtx(), amnp.getAMN_Employee_ID(), get_TrxName());
					    DocumentNo= amnp.getDocumentNo();
					    // log.warning("Deleting Lines i="+i+"  AMN_Payroll_ID="+AMN_Payroll_ID+ "  DocumentNo="+DocumentNo);
					    // Percent to show
						if(NoRecsLines > 0)
							Percent = 100 * (i / NoRecsLines);
						// Percentage Monitor
						MessagetoShow = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_Detail_ID")+": "+
								String.format("%-4s",i)+"/"+String.format("%-4s",NoRecsLines)+
								" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+DocumentNo+" "+
								AmerpMsg.getElementPrintText(getCtx(), "AMN_Employee_ID")+":"+
								String.format("%-50s",emp.getValue().trim()+"_"+emp.getName().trim().replace(' ', '_') +"_"+" ");
						if (processMonitor != null) {
							processMonitor.statusUpdate(MessagetoShow);
						}
						// DELETE Receipt Lines
						Msg_Value = Msg_Value + AMNPayrollDeleteDocs.amnPayrollDeleteInvoicesAllProcessOneLine(getCtx(), amnp, ReceiptConcepts.get(i).getAMN_Payroll_Detail_ID(),  get_TrxName());
						log.warning("Updating Final Message"+Msg_Value);
					}
			    }
			    Percent = 0;
			    NoRecs = ReceiptsGenList.size();
			    // DELETE PAYROLL DOCS
			    if (NoRecs > 0) {
				    for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
				    	// Check if same AMN_Payroll_ID
						AMN_Payroll_ID = ReceiptsGenList.get(i).getAMN_Payroll_ID();
						amnp = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, get_TrxName());
						emp = new MAMN_Employee(getCtx(), amnp.getAMN_Employee_ID(), get_TrxName());
				        // Busca Documentos en AMN_Payroll_Docs
				        List<MAMN_Payroll_Docs> amnpdocs = MAMN_Payroll_Docs.getByPayrollID(getCtx(), AMN_Payroll_ID, get_TrxName());
				        if (!amnpdocs.isEmpty()) {
					        for (MAMN_Payroll_Docs doc : amnpdocs) {
					            MInvoice invoice = new MInvoice(getCtx(),doc.getC_Invoice_ID(),get_TrxName());
					            if (invoice != null && !invoice.isPaid()) {
					            	amnp.deleteInvoiceLines(getCtx(), invoice, DocumentNo);
					            	doc.delete(true);
					            	invoice.delete(true);
					            } else {
					            	log.warning("Factura procesada o con asignaciones.");
					            }
					        }
				       }
			        }
			    }
			    // DELETE AMN_Payroll Header
			    if (NoRecs > 0) {
				    for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
				    	// Check if same AMN_Payroll_ID
						AMN_Payroll_ID = ReceiptsGenList.get(i).getAMN_Payroll_ID();
						amnp = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, get_TrxName());
						emp = new MAMN_Employee(getCtx(), amnp.getAMN_Employee_ID(), get_TrxName());
						// Percent to show
						if(NoRecs > 0)
							Percent = 100 * (i / NoRecs);
						// log.warning("Deleting Headers i="+i+"  AMN_Payroll_ID="+AMN_Payroll_ID+ "  DocumentNo="+ReceiptsGenList.get(i).getDocumentNo());
						// Percentage Monitor
						MessagetoShow = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_Detail_ID")+": "+
								String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
								" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
								ReceiptsGenList.get(i).getDocumentNo()+" "+
								AmerpMsg.getElementPrintText(getCtx(), "AMN_Employee_ID")+":"+
								String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim().replace(' ', '_') +"_"+" ");
						if (processMonitor != null) {
							processMonitor.statusUpdate(MessagetoShow);
						}
						// DELETE Receipt Header
						AMNPayrollDeleteDocs.amnPayrollDeleteInvoicesAllProcessOneHeader(getCtx(), amnp,  get_TrxName());
						log.warning("Updating Final Message"+Msg_Value);
					}
			    }
				Msg_Value = Msg_Value + "OK";
				
			} else {
				Msg_Value = Msg_Value + Msg.getMsg(getCtx(), "Errors")+":  \r\n";
				addLog(Msg_Value);	
				Msg_Value = "***** "+ AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+" - "+Msg.getMsg(getCtx(), "completed")+"(s) ***** \r\n";
				addLog(Msg_Value);	
				Msg_Value = "*** "+Msg.translate(getCtx(), "Warning")+" *** \r\n";  
				addLog(Msg_Value);	
				Msg_Value = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+"(s)"+ NoRecs+ " ";
				Msg_Value = Msg_Value + AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+"(s) "+ Msg.getMsg(Env.getCtx(),"completed")+":  \r\n";
				addLog(Msg_Value);	
				Msg_Value = Msg_Value +Msg_Error;
				addLog(Msg_Value);	
				
			}

	    } else {	    	
	    	Msg_Value = Msg_Value + AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+"(s)"+ NoRecs+ " ";
	    	addLog(Msg_Value);
	    }
	    
		return Msg_Value;
    
	}

}

package org.amerp.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.acct.Doc;
import org.compiere.acct.Doc_Invoice;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MBankStatement;
import org.compiere.model.MClientInfo;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMFDocumentAcctProcessor extends SvrProcess {

	/**	Client Parameter		*/
	private static int		p_AD_Client_ID = 0;
	/* The Table  C_AllocationHdr, C_Payment, C_Invoice, C_BankStatement */
	private static int  p_AD_Table_ID;
	/* record ID (C_AllocationHdr_ID, C_Payment_ID, C_Invoice_ID, C_BankStatement_ID, ..) */
	private static int p_Record_ID;
	/*  Account Schema */
	private static int p_C_AcctSchema_ID =0;
	/*  Posted Status */
	private static String p_Posted ="";
	/*  Other vars */
	private static String Msg_Value ="";
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("AD_Table_ID"))
				p_AD_Table_ID = para[i].getParameterAsInt();
			else if (name.equals("Record_ID")) 
				p_Record_ID =  para[i].getParameterAsInt();
			else if (name.equals("Posted")) 
				p_Posted =  para[i].getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		String Message="";
		String postReturn = "";
		String mpDocStatus = "";
		// Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null); 
		//MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		MAcctSchema asdef = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		if (p_C_AcctSchema_ID == 0 ) {
			// Get Client Acct Schema Default
			p_C_AcctSchema_ID=asdef.getC_AcctSchema_ID();
		} 
		// Set Target Account Schema
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), p_C_AcctSchema_ID , null);
		// ******************
		// *** INVOICES
		// ******************
		if (p_AD_Table_ID == MInvoice.Table_ID) {
			MInvoice minvoice = new MInvoice(Env.getCtx(), p_Record_ID, null);
			Msg_Value =Msg.getElement(Env.getCtx(), "C_Invoice_ID")+":"+minvoice.getDocumentNo().trim()+
					" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", minvoice.getDateAcct() );
			mpDocStatus = minvoice.getDocStatus();
			if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
					mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
				Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
			} else {
				// DOCS
				if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {		
					Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, p_Record_ID, this.get_TrxName());
					postReturn = doc.post(true, true);
					Msg_Value=Msg_Value+ " ** "+postReturn+" **";
				} else {
					Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
				}
			}					
			// Document Header
			addLog(Msg_Value);
		}
		// *********************
		// *** PAYMENTS
		// *********************
		else if  (p_AD_Table_ID == MPayment.Table_ID) {
			MPayment mpayment = new MPayment(Env.getCtx(), p_Record_ID, null);
			Msg_Value =Msg.getElement(Env.getCtx(), "C_Payment_ID")+":"+mpayment.getDocumentNo().trim()+
					" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", mpayment.getDateAcct() );
			mpDocStatus = mpayment.getDocStatus();
			if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
					mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
				Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
			} else {
				// DOCS
				if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed) ) {
					Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, p_Record_ID, this.get_TrxName());
					postReturn = doc.post(true, true);
					Msg_Value=Msg_Value+ " ** "+postReturn+" **";

				} else {
					Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
				}
			}					
			// Document Header
			addLog(Msg_Value);
		} 
		// *********************
		// *** BANK STATEMENT
		// *********************
		else if (p_AD_Table_ID == MBankStatement.Table_ID) {
			MBankStatement mbs = new MBankStatement(Env.getCtx(), p_Record_ID, null);
			Msg_Value =Msg.getElement(Env.getCtx(), "C_Payment_ID")+":"+mbs.getDocumentNo().trim()+
					" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", mbs.getDateAcct() );
			mpDocStatus = mbs.getDocStatus();
			if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
					mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
				Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
			} else {
				// DOCS
				if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {		
					Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, p_Record_ID, this.get_TrxName());
					postReturn = doc.post(true, true);
					Msg_Value=Msg_Value+ " ** "+postReturn+" **";
				} else {
					Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
				}
			}					
			// Document Header
			addLog(Msg_Value);
		}
		// *******************************
		// *** ALLOCATIONS
		// ** Different Account Process
		// ** Due to multiple AcctSchemas
		// *******************************
		// 	AMF_MAllocationHdr  MAllocationHdr
		else if (p_AD_Table_ID == MAllocationHdr.Table_ID) {		
			MAllocationHdr mallochdr = new MAllocationHdr(Env.getCtx(), p_Record_ID, null);
			Msg_Value =Msg.getElement(Env.getCtx(), "C_AllocationHdr_ID")+":"+mallochdr.getDocumentNo().trim()+
					" "+Msg.translate(Env.getCtx(), "Date")+":"+String.format("%-20s", mallochdr.getDateAcct() );
			mpDocStatus = mallochdr.getDocStatus();
			if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
					mpDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ) {
				Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
			} else {
				// DOCS
				if (mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
						mpDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {		
					Doc doc =  (Doc) Doc_Invoice.get(as, p_AD_Table_ID, p_Record_ID, this.get_TrxName());
					postReturn = doc.post(true, true);
					Msg_Value=Msg_Value+ " ** "+postReturn+" **";

				} else {
					Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+mpDocStatus+" **   \r\n";
				}
			}					
			// Document Header
			addLog(Msg_Value);
			// log.warning(" Process End="+Msg_Value);
		}
		return null;
	}
}

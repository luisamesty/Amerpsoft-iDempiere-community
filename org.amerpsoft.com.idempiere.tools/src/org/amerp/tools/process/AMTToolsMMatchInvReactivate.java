package org.amerp.tools.process;

import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.amerp.tools.amtmodel.AMTToolsMCostDetail;
import org.amerp.tools.amtmodel.AMTToolsMMatchInv;
import org.compiere.acct.Doc;
import org.compiere.model.MCostDetail;
import org.compiere.model.MDocType;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPeriod;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsMMatchInvReactivate extends SvrProcess {
	
	int p_M_MatchInv_ID=0;
	int M_MatchInv_Reversal_ID = 0;
	int C_InvoiceLine_ID = 0;
	int C_Invoice_ID = 0;
	int M_CostDetail_ID = 0;
	int M_CostHistory_ID = 0;
	int M_InOutLine_ID = 0;
	int M_InOut_ID = 0;
	String Msg_Value="";
	String sql="";
	int no = 0;
	boolean Okdelete=false;
	
	static CLogger log = CLogger.getCLogger(AMTToolsMMatchInvReactivate.class);
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	ProcessInfoParameter[] paras = getParameter();
			for (ProcessInfoParameter para : paras)
			{
				String paraName = para.getParameterName();
				if (paraName.equals("M_MatchInv_ID"))
					p_M_MatchInv_ID =  para.getParameterAsInt();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}	
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		AMTToolsMMatchInv mmatchinv = new AMTToolsMMatchInv(getCtx(), p_M_MatchInv_ID, null);
// log.setLevel(Level.WARNING);
// log.warning("------------------M_MatchInv_ID:"+p_M_MatchInv_ID+"  DocumentNo:"+mmatchinv.getDocumentNo());
		Msg_Value=Msg.getElement(Env.getCtx(),"M_MatchInv_ID")+":"+mmatchinv.getDocumentNo();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DateAcct")+":"+mmatchinv.getDateAcct();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+mmatchinv.getDescription();
		addLog(Msg_Value);
		Msg_Value=Msg.getElement(Env.getCtx(),"DocStatus")+":"+mmatchinv.getDocStatus();
		addLog(Msg_Value);
		
		// VERIFY DOC STATUS
		if (!mmatchinv.isPosted())
		{
			Msg_Value=Msg.getElement(Env.getCtx(),"M_MatchInv_ID")+":"+" **** ESTA SIN COMPLETAR ****";
			addLog(Msg_Value);
		} else {
			M_MatchInv_Reversal_ID = mmatchinv.getReversal_ID();
			if (M_MatchInv_Reversal_ID > 0 ) {
				no = AMTToolsMMatchInv.updateM_MatchInv_Reversal_ID(p_M_MatchInv_ID, get_TrxName());
				//
				if (no > 0) {
					Msg_Value=Msg.getElement(Env.getCtx(),"M_MatchInv_ID")+":"+mmatchinv.getDocumentNo();
					addLog(Msg_Value);
					Msg_Value=Msg.getElement(Env.getCtx(),"M_MatchInv_ID")+":"+" *** TIENE DOCUMENTO DE REVERSO ****";
					addLog(Msg_Value);
					Msg_Value=Msg.getElement(Env.getCtx(),"Description")+":"+mmatchinv.getDescription();
					addLog(Msg_Value);
				}
			}
			// REACTIVATE MATCH INVOICE HEADER
			int M_CostDetail_ID =0;
			int C_AcctSchema_ID=0;
			int no = 0;
			C_InvoiceLine_ID = mmatchinv.getC_InvoiceLine_ID();
			MInvoiceLine minvlin = new MInvoiceLine(Env.getCtx(), C_InvoiceLine_ID, null);
			C_Invoice_ID = minvlin.getC_Invoice_ID();
			MInvoice minv = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
			
			M_InOutLine_ID = mmatchinv.getM_InOutLine_ID();
			MInOutLine minoutline = new MInOutLine(Env.getCtx(), M_InOutLine_ID, Msg_Value);
			MInOut minout = new MInOut(Env.getCtx(), minoutline.getM_InOut_ID(),null);

			// DELETE FACTS AND REACTIVATE
			String doctype = Doc.DOCTYPE_MatMatchInv;
			// DocType MInOut C_Invoice
			MDocType mdoctypeinv = new MDocType(Env.getCtx(), minv.getC_DocType_ID(), doctype);
			MDocType mdoctypemin = new MDocType(Env.getCtx(), minout.getC_DocType_ID(), doctype);
			// Verify Period if open for Invoice
			Okdelete=true;
			if (! MPeriod.isOpen(Env.getCtx(), minv.getDateAcct(), mdoctypeinv.getDocBaseType(), minv.getAD_Client_ID())) {
				Msg_Value = "  ***("+Msg.translate(Env.getCtx(), "Error")+")***  \r\n"+
						Msg.translate(Env.getCtx(), "C_Period_ID")+": "+ " \r\n"+
						"  ***"+Msg.getMsg(Env.getCtx(), "PeriodClosed")+"***  \r\n"+
						Msg.translate(Env.getCtx(), "C_Invoice_ID")+"-"+
						Msg.translate(Env.getCtx(), "DocumentNo")+" No:"+minv.getDocumentNo() + " \r\n"+
						Msg.translate(Env.getCtx(), "DateAcct")+":"+minv.getDateAcct() + " \r\n"+
						Msg.translate(Env.getCtx(), "C_DocType_ID")+": "+ mdoctypeinv.getNameTrl().trim() + " \r\n"+
						Msg.translate(Env.getCtx(), "DocBaseType").trim()+"("+ mdoctypeinv.getDocBaseType()+") "+mdoctypeinv.getName();
				//FDialog.error(0, "Error", 	Message );
				addLog(Msg_Value);
				Okdelete=false;
			} 
			// Verify Period if open for MinOut
			if (! MPeriod.isOpen(Env.getCtx(), minout.getDateAcct(), mdoctypemin.getDocBaseType(), minout.getAD_Client_ID())) {
				Msg_Value = "  ***("+Msg.translate(Env.getCtx(), "Error")+")***  \r\n"+
						Msg.translate(Env.getCtx(), "C_Period_ID")+": "+ " \r\n"+
						"  ***"+Msg.getMsg(Env.getCtx(), "PeriodClosed")+"***  \r\n"+
						Msg.translate(Env.getCtx(), "M_InOut_ID")+"-"+
						Msg.translate(Env.getCtx(), "DocumentNo")+" No:"+minout.getDocumentNo() + " \r\n"+
						Msg.translate(Env.getCtx(), "DateAcct")+":"+minout.getDateAcct() + " \r\n"+
						Msg.translate(Env.getCtx(), "C_DocType_ID")+": "+ mdoctypemin.getNameTrl().trim() + " \r\n"+
						Msg.translate(Env.getCtx(), "DocBaseType").trim()+"("+ mdoctypemin.getDocBaseType()+") "+mdoctypemin.getName();
				//FDialog.error(0, "Error", 	Message );
				addLog(Msg_Value);
				Okdelete=false;
			} 
			// Delete if Both Periods are open
			if (Okdelete){
				// DELETE M_CostDetail M_CostHistory
				AMTToolsMCostDetail.deleteCostDetail(C_InvoiceLine_ID, get_TrxName());
//				M_CostDetail_ID = AMTMCostDetail.sqlGetM_CostDetail_ID(C_InvoiceLine_ID);
//				deleteCostDetailbyM_CostDetail_ID(M_CostDetail_ID, get_TrxName());
				// UPDATE C_InvoiceLineID SET M_InOutLineID = NULL
				updateC_InvoiceLine_M_InOutLine_ID(C_InvoiceLine_ID, get_TrxName());
				// REACTIVATE INVOICE HEADER
				mmatchinv.reActivateIt( get_TrxName());
				
				if (mmatchinv.delete(true)){
		//			mmatchinv.saveEx();
					Msg_Value = Msg.translate(Env.getCtx(), "DocumentNo")+" No:"+minv.getDocumentNo() + " \r\n"+
					Msg.translate(Env.getCtx(), "DateAcct")+":"+minv.getDateAcct() + " \r\n"+
					Msg.translate(Env.getCtx(), "C_DocType_ID")+": "+ mdoctypeinv.getNameTrl().trim() + " \r\n"+
					Msg.translate(Env.getCtx(), "DocBaseType").trim()+"("+ mdoctypeinv.getDocBaseType()+") "+mdoctypeinv.getName();
					addLog(Msg_Value);
					Msg_Value = Msg.translate(Env.getCtx(), "DocumentNo")+" No:"+minout.getDocumentNo() + " \r\n"+
					Msg.translate(Env.getCtx(), "DateAcct")+":"+minout.getDateAcct() + " \r\n"+
					Msg.translate(Env.getCtx(), "C_DocType_ID")+": "+ mdoctypemin.getNameTrl().trim() + " \r\n"+
					Msg.translate(Env.getCtx(), "DocBaseType").trim()+"("+ mdoctypemin.getDocBaseType()+") "+mdoctypemin.getName();
					addLog(Msg_Value);
					Msg_Value = "  ***("+Msg.translate(Env.getCtx(), "Updated")+")***  \r\n";
					addLog(Msg_Value);
					return "@OK@";	
				}
				return "@Error@";
			}
		}
		return null;
	}
	
	/**
	 * updateC_InvoiceLine_M_InOutLine_ID
	 * Replace with Null M_InOutLine_ID
	 * @param p_GL_Journal_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateC_InvoiceLine_M_InOutLine_ID(int C_InvoiceLine_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;

		// SET reversal_id
		sql = "UPDATE C_InvoiceLine SET M_InOutLine_ID = null WHERE  C_InvoiceLine_ID="+C_InvoiceLine_ID;	
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateC_InvoiceLine_M_InOutLine_ID for C_InvoiceLine_ID:"+C_InvoiceLine_ID+"  UPDATED ");
		return no;
	}
	
	/**
	 * Delete deleteCostDetailbyM_CostDetail_ID 
	 * @param p_M_CostDetail_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteCostDetailbyM_CostDetail_ID(int p_M_CostDetail_ID, String trxName)
	throws DBException
	{
		// M_CostDetail
		String sql = "DELETE FROM M_CostDetail WHERE M_CostDetail_ID= "+p_M_CostDetail_ID;	
		int no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----M_CostDetail for M_CostDetail_ID:"+p_M_CostDetail_ID+"  DELETED ");
		return no;
	
	}
}

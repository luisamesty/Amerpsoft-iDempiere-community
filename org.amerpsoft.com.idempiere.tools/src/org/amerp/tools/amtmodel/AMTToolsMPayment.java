package org.amerp.tools.amtmodel;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MFactAcct;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

@SuppressWarnings({ "serial", "serial" })
public class AMTToolsMPayment extends MPayment implements DocAction, DocOptions{

	/**	Process Message 			*/
	private String		am_processMsg = null;
	
	static CLogger log = CLogger.getCLogger(AMTToolsMPayment.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3088317406005121405L;

	public AMTToolsMPayment(Properties ctx, int C_Payment_ID, String trxName) {
		super(ctx, C_Payment_ID, trxName);
		// TODO Auto-generated constructor stub
	} 
	
	public AMTToolsMPayment(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * sqlGet C_AllocationLine_C_Payment (int p_C_Payment_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 * Verify if Invoice has C_Allocationline or C_Payment Records associated
	 */
	public String C_AllocationLine_C_Payment (int p_C_Payment_ID, String trxName)
	
	{
		String sql;
		int C_AllocationLine_ID=0;
		int C_Payment_ID=0;
		String retValue="";
		// LCO_InvoiceWHDocLines
    	sql = "SELECT DISTINCT C_AllocationLine_ID FROM C_AllocationLine WHERE C_Payment_ID=?" ;
    	C_AllocationLine_ID = DB.getSQLValue(null, sql, p_C_Payment_ID);	
//log.warning("sql:"+sql+"  p_C_Payment_ID:"+p_C_Payment_ID+"  C_AllocationLine_ID:"+C_AllocationLine_ID);
		if (C_AllocationLine_ID > 0) {
			retValue=Msg.getElement(Env.getCtx(),"C_AllocationLine_ID")+":"+C_AllocationLine_ID;
		   	sql = "SELECT DISTINCT C_Payment_ID FROM C_AllocationLine WHERE C_Payment_ID=?" ;
    		C_Payment_ID = DB.getSQLValue(null, sql, p_C_Payment_ID);	
//log.warning("sql:"+sql+"  p_C_Payment_ID:"+p_C_Payment_ID+"  C_Payment_ID:"+C_Payment_ID);
    		if (C_Payment_ID > 0) {
    			MPayment mpayment = new MPayment(getCtx(), C_Payment_ID, null);
    			retValue=retValue+"  "+Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"
    					+ mpayment.getDocumentNo()+"-"+mpayment.getDescription();
    		}
		} else {
			//retValue=Msg.getElement(Env.getCtx(),"C_AllocationLine_ID")+": ** NO TIENE **";
			retValue="OK";
		}
    	return retValue;	
	}

	/**
	 * sqlGet C_BankStatementLine_C_Payment (int p_C_Payment_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 * Verify if Invoice has C_Allocationline or C_Payment Records associated
	 * C_BankStatementLine_ID
	 */
	public String C_BankStatementLine_C_Payment (int p_C_Payment_ID, String trxName)
	
	{
		String sql;
		int C_BankStatementLine_ID=0;
		String retValue="";
		// LCO_InvoiceWHDocLines
    	sql = "SELECT DISTINCT C_BankStatementLine_ID FROM C_BankStatementLine WHERE C_Payment_ID=?" ;
    	C_BankStatementLine_ID = DB.getSQLValue(null, sql, p_C_Payment_ID);	
//log.warning("sql:"+sql+"  p_C_Payment_ID:"+p_C_Payment_ID+"  C_BankStatementLine_ID:"+C_BankStatementLine_ID);
    	MBankStatementLine bsl = new MBankStatementLine(p_ctx, C_BankStatementLine_ID, null);
    	MBankStatement bs = new MBankStatement(p_ctx, bsl.getC_BankStatement_ID(), null);
    	if (bs.isComplete() ) {
			retValue=Msg.getElement(Env.getCtx(),"C_BankStatementLine_ID")+":"+C_BankStatementLine_ID;
			retValue=retValue+"  "+Msg.getElement(Env.getCtx(),"DocStatus")+": "+DOCSTATUS_Completed;
//		   	sql = "SELECT DISTINCT C_Payment_ID FROM C_BankStatementLine_ID WHERE C_Payment_ID=?" ;
//    		C_Payment_ID = DB.getSQLValue(null, sql, p_C_Payment_ID);	
//log.warning("sql:"+sql+"  p_C_Payment_ID:"+p_C_Payment_ID+"  C_Payment_ID:"+C_Payment_ID);
    		if (p_C_Payment_ID > 0) {
    			MPayment mpayment = new MPayment(getCtx(), p_C_Payment_ID, null);
    			retValue=retValue+"  "+Msg.getElement(Env.getCtx(),"C_Payment_ID")+":"
    					+ mpayment.getDocumentNo()+"-"+mpayment.getDescription();
    		}
		} else {
			//retValue=Msg.getElement(Env.getCtx(),"C_AllocationLine_ID")+": ** NO TIENE **";
			retValue="OK";
		}
    	return retValue;	
	}

	public int get_C_BankStatmentLine_ID(int p_C_Payment_ID, String trxName) {
		
		String sql;
		int C_BankStatementLine_ID=0;
		int C_Payment_ID=0;
		String retValue="";
		// LCO_InvoiceWHDocLines
    	sql = "SELECT DISTINCT C_BankStatementLine_ID FROM C_BankStatementLine WHERE C_Payment_ID=?" ;
    	C_BankStatementLine_ID = DB.getSQLValue(null, sql, p_C_Payment_ID);	
		
		
		return C_BankStatementLine_ID;
		
	}
	
	/**
	 * 	Re-activate
	 * 	@return false
	 */
	public boolean reActivateIt(String trxName)
	{
		if (log.isLoggable(Level.INFO)) log.info(toString());
		// Before reActivate
		am_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REACTIVATE);
//log.warning("get_ID():"+get_ID()+"reactivateIt   am_processMsg:"+am_processMsg);
		
		if (am_processMsg != null)
			return false;

		// Reactivate HEADER
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		MFactAcct.deleteEx(AMTToolsMPayment.Table_ID, get_ID(), trxName);
		setPosted(false);
		setProcessed(false);
		setDocAction("CL");
		setDocStatus("DR");
		saveEx(trxName);
		// Reactivate LINES
//		AMTMInvoiceLine[] lines = (AMTMInvoiceLine[]) getLines(false);
//		for (int i = 0; i < lines.length; i++)
//		{
//			AMTMInvoiceLine line = lines[i];
//log.warning("reactivateIt Lines:" +line.getName());
//			line.setProcessed(false);
//			line.save(get_TrxName());
//		}
		// After reActivate
		am_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (am_processMsg != null)
			return false;
//log.warning("reactivateIt:");

		return false;

	}	//	reActivateIt
	
//	UPDATE adempiere.c_invoice SET reversal_id = null WHERE  C_Invoice_ID=1002650 

	/**
	 * updateC_Payment_Reversal_ID
	 * Replace with Null Reversal_ID
	 * @param p_C_Payment_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateC_Payment_Reversal_ID(int p_C_Payment_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		int Reversal_ID=0;
		// GET REVERSED INVOICE IF EXISTS
    	sql = "SELECT count(*) FROM adempiere.c_payment WHERE  Reversal_ID="+p_C_Payment_ID;	
    	no = DB.getSQLValue(null, sql);	
    	if (no > 0) {
    		sql = "SELECT DISTINCT C_Payment_ID FROM adempiere.c_payment WHERE  Reversal_ID="+p_C_Payment_ID;	
			Reversal_ID = DB.getSQLValue(null, sql, p_C_Payment_ID);
			if ( Reversal_ID > 0) {
				// SET reversal_id to reversed Invoice 
				sql = "UPDATE C_Payment SET reversal_id = null WHERE  C_Payment_ID="+Reversal_ID;	
				no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateC_Payment_Reversal_ID for Reversal_ID:"+Reversal_ID+"  no:"+no+"  UPDATED ");
			}
    	}
		// SET reversal_id
		sql = "UPDATE C_Payment SET reversal_id = null WHERE  C_Payment_ID="+p_C_Payment_ID;	
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateC_Payment_Reversal_ID for p_C_Payment_ID:"+p_C_Payment_ID+"  no:"+no+"  UPDATED ");
		return no;
	
	}
	
	/**
	 * updateC_Payment_IsReconciled
	 * Replace with 'N' IsReconciled
	 * @param p_C_Payment_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateC_Payment_IsReconciled(int p_C_Payment_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// SET IsReconciled
		sql = "UPDATE C_Payment SET IsReconciled = 'N' WHERE  C_Payment_ID="+p_C_Payment_ID;	
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateC_Payment_Reversal_ID for p_C_Payment_ID:"+p_C_Payment_ID+"  no:"+no+"  UPDATED ");
		return no;
	
	}
}

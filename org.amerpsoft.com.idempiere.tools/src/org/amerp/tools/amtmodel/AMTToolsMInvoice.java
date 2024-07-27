package org.amerp.tools.amtmodel;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MFactAcct;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.MSysConfig;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMTToolsMInvoice extends MInvoice implements DocAction, DocOptions{
	
	
	/**	Process Message 			*/
	private String		am_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		am_justPrepared = false;

	/**	Invoice Lines			*/
	private static MInvoiceLine[]	am_lines;

	static CLogger log = CLogger.getCLogger(AMTToolsMInvoice.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782648276040097417L;
	
	public AMTToolsMInvoice(Properties ctx, int C_Invoice_ID, String trxName) {
		super(ctx, C_Invoice_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public AMTToolsMInvoice(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
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
//log.warning("reactivateIt   am_processMsg:"+am_processMsg);
		
		if (am_processMsg != null)
			return false;

		// Reactivate HEADER
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		MFactAcct.deleteEx(AMTToolsMInvoice.Table_ID, get_ID(), trxName);
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
	@Override
	public int customizeValidActions(String docStatus, Object processing,
			String orderType, String isSOTrx, int AD_Table_ID,
			String[] docAction, String[] options, int index) {
		// TODO Auto-generated method stub
		return 0;
	}


	/**
	 * deleteLCO_InvoiceWithholding
	 * Delete LCO_InvoiceWithholding Lines
	 * @param p_C_Invoice_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteLCO_InvoiceWithholding(int p_C_Invoice_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// LCO_InvoiceWHDocLines No Records
		if (MSysConfig.getBooleanValue("LCO_USE_WITHHOLDINGS", false, Env.getAD_Client_ID(Env.getCtx()))) {
	    	sql = "SELECT count(*) FROM LCO_InvoiceWithholding WHERE C_Invoice_ID=?" ;
	    	no = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
	    	if (no > 0) {
				// LCO_InvoiceWithholding
				sql = "DELETE LCO_InvoiceWithholding WHERE C_Invoice_ID= "+p_C_Invoice_ID;	
				no = DB.executeUpdateEx(sql, trxName);
	//log.warning("-----LCO_InvoiceWithholding for p_C_Invoice_ID:"+p_C_Invoice_ID+"  no:"+no+"  DELETED ");
	    	}
		}
    	return no;		
	}

//	UPDATE adempiere.c_invoice SET reversal_id = null WHERE  C_Invoice_ID=1002650 
	/**
	 * updateC_Invoice_Reversal_ID
	 * Replace with Null Reversal_ID
	 * @param p_C_Invoice_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//SELECT DISTINCT C_Invoice_ID FROM adempiere.c_invoice WHERE  Reversal_ID=
	public static int updateC_Invoice_Reversal_ID(int p_C_Invoice_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		int Reversal_ID=0;
		// GET REVERSED INVOICE IF EXISTS
    	sql = "SELECT count(*) FROM adempiere.c_invoice WHERE  Reversal_ID="+p_C_Invoice_ID;	
    	no = DB.getSQLValue(null, sql);	
    	if (no > 0) {
    		sql = "SELECT DISTINCT C_Invoice_ID FROM adempiere.c_invoice WHERE  Reversal_ID="+p_C_Invoice_ID;	
			Reversal_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);
			if ( Reversal_ID > 0) {
				// SET reversal_id to reversed Invoice 
				sql = "UPDATE C_Invoice SET reversal_id = null WHERE  C_Invoice_ID="+Reversal_ID;	
				no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateC_Invoice_Reversal_ID for p_C_Invoice_ID:"+p_C_Invoice_ID+"  no:"+no+"  UPDATED ");
			}
    	}
		// SET reversal_id
		sql = "UPDATE C_Invoice SET reversal_id = null WHERE  C_Invoice_ID="+p_C_Invoice_ID;	
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateC_Invoice_Reversal_ID for p_C_Invoice_ID:"+p_C_Invoice_ID+"  no:"+no+"  UPDATED ");
		return no;
	
	}
	
	/**
	 * deleteC_InvoiceTax
	 * Delete C_InvoiceTax Lines
	 * @param p_C_Invoice_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteC_InvoiceTax(int p_C_Invoice_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// 
		sql = "DELETE C_InvoiceTax WHERE C_Invoice_ID= "+p_C_Invoice_ID;
		
		
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----C_InvoiceTax for p_C_Invoice_ID:"+p_C_Invoice_ID+"  no:"+no+"  DELETED ");
		return no;
	
	}
	
	/**
	 *  deleteC_InvoiceTaxWHLines
	 * @param p_C_Invoice_ID
	 * @param trxName
	 * @return
	 * @throws DBException
	 */
	
	public static int  deleteC_InvoiceTaxWHLines (int p_C_Invoice_ID, String trxName)
			throws DBException
			{
		String sql;
		int no=0;
		if (MSysConfig.getBooleanValue("LCO_USE_WITHHOLDINGS", false, Env.getAD_Client_ID(Env.getCtx()))) {
			// 
			sql = "DELETE C_InvoiceTax WHERE C_Invoice_ID= "+p_C_Invoice_ID +
					" AND C_Tax_ID IN ( " +
					"   SELECT tax.C_Tax_ID " +
					"   FROM C_InvoiceTax as ivt " +
					"   LEFT JOIN C_Tax tax ON tax.C_Tax_ID= ivt.C_Tax_ID " +
					"   LEFT JOIN C_Taxcategory tac ON tac.C_Taxcategory_ID = tax.C_Taxcategory_ID  " +
					"   WHERE tac.iswithholding='Y' AND ivt.C_Invoice_ID=" +p_C_Invoice_ID +
					"  )";
	//log.warning("-----C_InvoiceTax for p_C_Invoice_ID:"+p_C_Invoice_ID+"  sql:"+sql);
			no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----C_InvoiceTax for p_C_Invoice_ID:"+p_C_Invoice_ID+"  no:"+no+"  DELETED ");
		}
		return no;
			}
	/**
	 * sqlGetLCO_InvoiceWHDocLines (int p_C_Invoice_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 */
	public int sqlGetLCO_InvoiceWHDocLines (int p_C_Invoice_ID, String trxName)
	
	{
		String sql;
		int LCO_InvoiceWHDocLines_ID=0;
		if (MSysConfig.getBooleanValue("LCO_USE_WITHHOLDINGS", false, Env.getAD_Client_ID(Env.getCtx()))) {
		// LCO_InvoiceWHDocLines
	    	sql = "SELECT DISTINCT LCO_InvoiceWHDocLines_ID FROM LCO_InvoiceWHDocLines WHERE C_Invoice_ID=?" ;
	    	LCO_InvoiceWHDocLines_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
//log.warning("sql:"+sql+"  p_C_Invoice_ID:"+p_C_Invoice_ID+"  LCO_InvoiceWHDocLines_ID:"+LCO_InvoiceWHDocLines_ID);
		}
    	return LCO_InvoiceWHDocLines_ID;	
	}
	
	/**
	 * sqlGetLCO_InvoiceWHDoc (int p_C_Invoice_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 */
	public int sqlGetLCO_InvoiceWHDoc (int p_C_Invoice_ID, String trxName)
	
	{
		String sql;
		int LCO_InvoiceWHDoc_ID=0;
		//LCO_InvoiceWHDoc
		if (MSysConfig.getBooleanValue("LCO_USE_WITHHOLDINGS", false, Env.getAD_Client_ID(Env.getCtx()))) {
			sql = "select LCO_InvoiceWHDoc_ID FROM LCO_InvoiceWHDocLines WHERE C_Invoice_ID=?" ;
			LCO_InvoiceWHDoc_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
			log.warning("LCO_InvoiceWHDoc_ID:"+LCO_InvoiceWHDoc_ID);
		}
		return LCO_InvoiceWHDoc_ID;	
	}
	
	/**
	 * sqlGetLCO_InvoiceWHDoc_DocumentNo (int p_C_Invoice_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 */
	public String sqlGetLCO_InvoiceWHDoc_DocumentNo (int p_C_Invoice_ID, String trxName)
	
	{
		String sql;
		int LCO_InvoiceWHDoc_ID=0;
		String Documentno ="";
		if (MSysConfig.getBooleanValue("LCO_USE_WITHHOLDINGS", false, Env.getAD_Client_ID(Env.getCtx()))) {
			//MLCOInvoiceWHDocLines mlcoinwhdoc = new MLCOInvoiceWHDocLines();
			// LCO_InvoiceWHDoc_ID
	    	sql = "select DISTINCT LCO_InvoiceWHDoc_ID from LCO_InvoiceWHDocLines WHERE C_Invoice_ID=?" ;
	    	LCO_InvoiceWHDoc_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
	//log.warning("LCO_InvoiceWHDoc_ID:"+LCO_InvoiceWHDoc_ID);
			//LCO_InvoiceWHDocLines get 
			sql = "select DISTINCT Documentno from LCO_InvoiceWHDoc WHERE LCO_InvoiceWHDoc_ID=?" ;
			Documentno = DB.getSQLValueString(null, sql, LCO_InvoiceWHDoc_ID);	
			log.warning("LCO_InvoiceWHDoc_ID:"+LCO_InvoiceWHDoc_ID+ "Documentno:"+Documentno);
		}
		return Documentno;	
	}
	
	/**
	 * sqlGet C_AllocationLine_C_Payment (int p_C_Invoice_ID)
	 * @param p_C_Invoice_ID	Invoice ID
	 * Verify if Invoice has C_Allocationline or C_Payment Records associated
	 */
	public String C_AllocationLine_C_Payment (int p_C_Invoice_ID, String trxName)
	
	{
		String sql;
		int C_AllocationLine_ID=0;
		int C_Payment_ID=0;
		String retValue="";
		// LCO_InvoiceWHDocLines
    	sql = "SELECT DISTINCT C_AllocationLine_ID FROM C_AllocationLine WHERE C_Invoice_ID=?" ;
    	C_AllocationLine_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
//log.warning("sql:"+sql+"  p_C_Invoice_ID:"+p_C_Invoice_ID+"  C_AllocationLine_ID:"+C_AllocationLine_ID);
		if (C_AllocationLine_ID > 0) {
			retValue=Msg.getElement(Env.getCtx(),"C_AllocationLine_ID")+":"+C_AllocationLine_ID;
		   	sql = "SELECT DISTINCT C_Payment_ID FROM C_AllocationLine WHERE C_Invoice_ID=?" ;
    		C_Payment_ID = DB.getSQLValue(null, sql, p_C_Invoice_ID);	
//log.warning("sql:"+sql+"  p_C_Invoice_ID:"+p_C_Invoice_ID+"  C_Payment_ID:"+C_Payment_ID);
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
	
}

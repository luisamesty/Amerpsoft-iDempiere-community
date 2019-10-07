package org.globalqss.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author luisamesty
 *
 */
public class MLCOInvoiceWHDocLines extends X_LCO_InvoiceWHDocLines {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static CLogger	s_log	= CLogger.getCLogger (MLCOInvoiceWHDocLines.class);

	public MLCOInvoiceWHDocLines(Properties ctx, int LCO_InvoiceWHDocLines_ID,
			String trxName) {
		super(ctx, LCO_InvoiceWHDocLines_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MLCOInvoiceWHDocLines(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * createLCOInvoiceWHDocLines
	 * @param ctx
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_LCO_InvoiceWHDoc_ID
	 * @param p_C_Invoice_ID
	 * @param p_Line
	 * @param p_TotalLines
	 * @param p_GrandTotal
	 * @param p_TaxBaseAmt
	 * @param p_TaxAmt
	 * @param p_WithholdingAmt
	 * @param p_LCO_InvoiceWithholding_ID
	 * @param p_Percent
	 * @param trxName
	 * @return
	 */
	public boolean createLCOInvoiceWHDocLines(Properties ctx,
			int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_LCO_InvoiceWHDoc_ID, int p_C_Invoice_ID, int p_Line, BigDecimal p_TotalLines,
			BigDecimal p_GrandTotal, BigDecimal p_TaxBaseAmt, BigDecimal p_TaxAmt, BigDecimal p_WithholdingAmt,
			int p_LCO_InvoiceWithholding_ID, BigDecimal p_Percent, String trxName) {
		
		// Invoice
		LCO_MInvoice lcominvoice = new LCO_MInvoice(ctx, p_C_Invoice_ID, null);
		// NEW RECORD
		MLCOInvoiceWHDocLines lcoiwdl = new MLCOInvoiceWHDocLines(getCtx(), getLCO_InvoiceWHDocLines_ID(), get_TrxName());
		lcoiwdl.setAD_Client_ID(p_AD_Client_ID);
		lcoiwdl.setAD_Org_ID(p_AD_Org_ID);
		lcoiwdl.setLCO_InvoiceWHDoc_ID(p_LCO_InvoiceWHDoc_ID);
		lcoiwdl.setDescription(Msg.getElement(Env.getCtx(), "C_Invoice_ID")+":"+lcominvoice.getDocumentNo()+" "+
				Msg.getElement(Env.getCtx(), "DateInvoiced")+":"+lcominvoice.getDateInvoiced().toString().substring(0,10)+"  "+
				Msg.getElement(Env.getCtx(), "DateAcct")+":"+lcominvoice.getDateAcct().toString().substring(0,10)+" "+
				lcominvoice.getDescription());
		lcoiwdl.setLine(p_Line);
		lcoiwdl.setC_Invoice_ID(p_C_Invoice_ID);
		lcoiwdl.setTotalLines(p_TotalLines);
		lcoiwdl.setGrandTotal(p_GrandTotal);
		lcoiwdl.setTaxBaseAmt(p_TaxBaseAmt);
		lcoiwdl.setTaxAmt(p_TaxAmt);
		lcoiwdl.setWithholdingAmt(p_WithholdingAmt);
		lcoiwdl.setLCO_InvoiceWithholding_ID(p_LCO_InvoiceWithholding_ID);
		lcoiwdl.setPercent(p_Percent);
		// SAVES NEW
		lcoiwdl.save(get_TrxName());

		return false;
		
	}
	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if it can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getLCO_InvoiceWHDocLines_ID());
			setLine (ii);
		}
		return true;
	} //	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;

		return MLCOInvoiceWHDoc.updateHeaderLCOInvoiveWHDoc(getLCO_InvoiceWHDoc_ID(), get_TrxName());
	}	//	afterSave
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		
		return MLCOInvoiceWHDoc.updateHeaderLCOInvoiveWHDoc(getLCO_InvoiceWHDoc_ID(), get_TrxName());
	}	//	afterDelete
}

package org.amerp.tools.amtmodel;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.MDocType;
import org.compiere.model.MFactAcct;
import org.compiere.model.MMatchInv;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class AMTToolsMMatchInv extends MMatchInv implements DocAction, DocOptions{

	String am_processMsg="";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8963081631642605628L;

	static CLogger log = CLogger.getCLogger(AMTToolsMMatchInv.class);
	
	public AMTToolsMMatchInv(Properties ctx, int M_MatchInv_ID, String trxName) {
		super(ctx, M_MatchInv_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public AMTToolsMMatchInv(Properties ctx, ResultSet rs, String trxName) {
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

	@Override
	public void setDocStatus(String newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processIt(String action) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String prepareIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
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
// log.warning("get_ID():"+get_ID()+"reactivateIt   am_processMsg:"+am_processMsg);
		
		if (am_processMsg != null)
			return false;

		// Reactivate HEADER;
		MFactAcct.deleteEx(AMTToolsMMatchInv.Table_ID, get_ID(), trxName);
		setPosted(false);
		setDocStatus("DR");
		saveEx(trxName);
		// After reActivate
		am_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (am_processMsg != null)
			return false;
// log.warning("reactivateIt: am_processMsg="+am_processMsg);

		return false;

	}	//	reActivateIt

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocAction() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * updateM_MatchInv_Reversal_ID
	 * Replace with Null Reversal_ID
	 * @param p_M_MatchInv_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateM_MatchInv_Reversal_ID(int p_M_MatchInv_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		int Reversal_ID=0;

		// GET REVERSED GL_Journal IF EXISTS
    	sql = "SELECT count(*) FROM adempiere.M_MatchInv WHERE  Reversal_ID="+p_M_MatchInv_ID;	
    	no = DB.getSQLValue(null, sql);	
    	if (no > 0) {
    		sql = "SELECT DISTINCT M_MatchInv_ID FROM adempiere.M_MatchInv WHERE  Reversal_ID="+p_M_MatchInv_ID;	
    		Reversal_ID = DB.getSQLValue(trxName, sql) ;
			if ( Reversal_ID > 0) {
				// SET reversal_id to reversed Invoice 
				sql = "UPDATE M_MatchInv SET reversal_id = null WHERE  M_MatchInv_ID="+Reversal_ID;	
				no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateM_MatchInv_Reversal_ID for Reversal_ID:"+Reversal_ID+"  no:"+no+"  UPDATED ");
			}
    	}
		// SET reversal_id
		sql = "UPDATE M_MatchInv SET reversal_id = null WHERE  M_MatchInv_ID="+Reversal_ID;	
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateM_MatchInv_Reversal_ID for p_M_MatchInv_ID:"+p_M_MatchInv_ID+"  no:"+no+"  UPDATED ");
		return no;
	}

	/**
	 * 	Before Delete
	 *	@return true if acct was deleted
	 */
	protected boolean beforeDelete ()
	{
		if (isPosted())
		{
			MPeriod.testPeriodOpen(getCtx(), getDateTrx(), MDocType.DOCBASETYPE_MatchInvoice, getAD_Org_ID());
			setPosted(false);
			MFactAcct.deleteEx (Table_ID, get_ID(), get_TrxName());
		}
		
//		-- DELETE M_CostDetail 
//		DELETE FROM m_costdetail WHERE M_MatchInv_ID= 1002389 ;
//		-- DELETE M_MatchInv
//		UPDATE M_MatchInv SET Reversal_ID=null WHERE Reversal_ID= 1002389 ;
//		DELETE FROM M_MatchInv WHERE M_MatchInv_ID= 1002389 ;
		return true;
	}	//	beforeDelete
	
	/**
	 * 	After Delete
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
		{
			// AZ Goodwill
			deleteMatchInvCostDetail();
			// end AZ			
		}
		return success;
	}	//	afterDelete
	
	//
	//AZ Goodwill
	protected String deleteMatchInvCostDetail()
	{
		// Get Account Schemas to delete MCostDetail
		MAcctSchema[] acctschemas = MAcctSchema.getClientAcctSchema(getCtx(), getAD_Client_ID());
		for(int asn = 0; asn < acctschemas.length; asn++)
		{
			MAcctSchema as = acctschemas[asn];
//log.warning("deleteMatchInvCostDetail: AcctSchema="+as.getName());			
			if (as.isSkipOrg(getAD_Org_ID()))
			{
				continue;
			}
			
			MCostDetail cd = MCostDetail.get (getCtx(), "M_MatchInv_ID=?", 
					getM_MatchInv_ID(), getM_AttributeSetInstance_ID(), as.getC_AcctSchema_ID(), get_TrxName());
			if (cd != null)
			{
				cd.deleteEx(true);
			}
		}
		
		return "";
	}

	@Override
	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return false;
	}
	

	
}

package org.amerp.tools.amtmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MBankStatement;
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


public class AMTToolsMBankStatement extends MBankStatement implements DocAction, DocOptions{

	/**	Process Message 			*/
	private String		am_processMsg = null;
	
	static CLogger log = CLogger.getCLogger(AMTToolsMBankStatement.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3088317406005121405L;

	public AMTToolsMBankStatement(Properties ctx, int C_BankStatement_ID, String trxName) {
		super(ctx, C_BankStatement_ID, trxName);
		// TODO Auto-generated constructor stub
	} 
	
	public AMTToolsMBankStatement(Properties ctx, ResultSet rs, String trxName) {
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
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), "CMB", getAD_Org_ID());
		MFactAcct.deleteEx(AMTToolsMBankStatement.Table_ID, get_ID(), trxName);
		setPosted(false);
		setProcessed(false);
		setDocAction("CL");
		setDocStatus("DR");
		saveEx(trxName);
		// Reactivate LINES
		// After reActivate
		am_processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REACTIVATE);
		if (am_processMsg != null)
			return false;
//log.warning("reactivateIt:");

		return false;

	}	//	reActivateIt
	
//	UPDATE adempiere.c_invoice SET reversal_id = null WHERE  C_Invoice_ID=1002650 

	/**
	 * updateC_BankStatement_ID
	 * Replace with Null Reversal_ID
	 * @param p_C_BankStatement_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateC_BankStatement_ID(Properties ctx, int p_C_BankStatement_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		// SET processed
		//sql = "UPDATE C_BankStatementLine SET processed = 'N' WHERE  C_BankStatement_ID="+p_C_BankStatement_ID;	
		//no = DB.executeUpdate(sql, p_C_BankStatement_ID, null);
		MBankStatement mbankstatement = new MBankStatement(ctx, p_C_BankStatement_ID, null);
		if (mbankstatement != null) {
//log.warning("-----updateC_BankStatement_ID for p_C_BankStatement_ID:"+p_C_BankStatement_ID+"  no:"+no+"  UPDATED ");
			mbankstatement.setProcessed(false);
			mbankstatement.saveEx(trxName);
		}
		return no;
	}
	
	/**
	 * updateC_Payment_ID
	 * Replace with Null Reversal_ID
	 * @param p_C_Payment_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateC_Payment_ID(Properties ctx, int p_C_Payment_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=1;
		// SET processed
		//sql = "UPDATE C_Payment SET isreconciled = 'N' WHERE  C_Payment_ID="+p_C_Payment_ID;	
		//no = DB.executeUpdate(sql, p_C_Payment_ID, null);
		MPayment mpayment = new MPayment(ctx, p_C_Payment_ID, null);
		if (mpayment != null) {
//log.warning("-----updateC_Payment_ID for p_C_Payment_ID:"+p_C_Payment_ID+"  DocumentNo:"+mpayment.getDocumentNo()+"  UPDATED ");
			//mpayment.setIsReconciled(false);
			//mpayment.saveEx(trxName);
			// Changed to Direct DB UPDATE
			String Mess= setIsReconciledMPayment(p_C_Payment_ID,false);
		}
		return no;
	}
	
	/**
	 * updateC_Payment_ID_isreconciled
	 * Replace with Null Reversal_ID
	 * @param p_C_Payment_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateC_Payment_ID_isreconciled(Properties ctx,int p_C_BankStatement_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		int C_Payment_ID=0;
		// SET processed

		sql ="SELECT pay.C_Payment_ID "+
				" FROM C_BankStatementLine bsl "+
				" LEFT JOIN C_BankStatement bss ON (bss.C_BankStatement_id = bsl.C_BankStatement_ID) "+
				" LEFT JOIN C_Payment pay ON (pay.C_Payment_ID = bsl.C_Payment_ID ) "+
				" WHERE  bsl.C_BankStatement_ID=? " 
				;
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_C_BankStatement_ID);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				C_Payment_ID = rsod1.getInt(1);
//log.warning("----- WHILE C_Payment_ID:"+C_Payment_ID);
				//  CREATE MAMN_Payroll Detail
				if (C_Payment_ID > 0) {
					no=updateC_Payment_ID(ctx, C_Payment_ID, null);
				}
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; pstmt1 = null;
		}		
		return no;
	}
	
	/**
	 * setIsReconciledMPayment
	 * @param p_C_Payment_ID
	 * @param IsReconciled
	 * @return
	 */
	public static String setIsReconciledMPayment (int p_C_Payment_ID, boolean IsReconciled) 
	{
		
		String retValue = "";
		String sql = "";
		if (IsReconciled) 
			sql = "UPDATE C_Payment SET isreconciled = 'Y' WHERE  C_Payment_ID="+p_C_Payment_ID;	
		else
			sql = "UPDATE C_Payment SET isreconciled = 'N' WHERE  C_Payment_ID="+p_C_Payment_ID;	
		// 
		int no = DB.executeUpdate(sql, null);
		//
		return retValue;
		
	}
}

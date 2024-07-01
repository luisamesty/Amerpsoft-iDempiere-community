package org.amerp.tools.amtmodel;


import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MFactAcct;
import org.compiere.model.MJournal;
import org.compiere.model.MPeriod;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class AMTToolsMJournal extends MJournal implements DocAction, DocOptions{
	
	/**	Process Message 			*/
	private String		am_processMsg = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2156900168570358214L;
	
	static CLogger log = CLogger.getCLogger(AMTToolsMJournal.class);
	
	public AMTToolsMJournal(Properties ctx, int GL_Journal_ID, String trxName) {
		super(ctx, GL_Journal_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	public AMTToolsMJournal(Properties ctx, ResultSet rs, String trxName) {
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
	 * updateGL_Journal_Reversal_ID
	 * Replace with Null Reversal_ID
	 * @param p_GL_Journal_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	//
	public static int updateGL_Journal_Reversal_ID(int p_GL_Journal_ID, String trxName)
	throws DBException
	{
		String sql;
		int no=0;
		int Reversal_ID=0;

		// GET REVERSED GL_Journal IF EXISTS
    	sql = "SELECT count(*) FROM adempiere.GL_Journal WHERE  Reversal_ID="+p_GL_Journal_ID;	
    	no = DB.getSQLValue(null, sql);	
    	if (no > 0) {
    		sql = "SELECT DISTINCT GL_Journal_ID FROM adempiere.GL_Journal WHERE  Reversal_ID="+p_GL_Journal_ID;	
    		Reversal_ID = DB.getSQLValue(trxName, sql) ; //DB.getSQLValue(trxName, sql, p_GL_Journal_ID);
			if ( Reversal_ID > 0) {
				// SET reversal_id to reversed Invoice 
				sql = "UPDATE GL_Journal SET reversal_id = null WHERE  GL_Journal_ID="+Reversal_ID;	
				no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateGL_Journal_Reversal_ID for Reversal_ID:"+Reversal_ID+"  no:"+no+"  UPDATED ");
			}
    	}
		// SET reversal_id
		sql = "UPDATE GL_Journal SET reversal_id = null WHERE  GL_Journal_ID="+p_GL_Journal_ID;	
		no = DB.executeUpdateEx(sql, trxName);
//log.warning("-----updateGL_Journal_Reversal_ID for p_GL_Journal_ID:"+p_GL_Journal_ID+"  no:"+no+"  UPDATED ");
		return no;
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
log.warning("get_ID():"+get_ID()+"reactivateIt   am_processMsg:"+am_processMsg);
		
		if (am_processMsg != null)
			return false;

		// Reactivate HEADER
		MPeriod.testPeriodOpen(getCtx(), getDateAcct(), getC_DocType_ID(), getAD_Org_ID());
		MFactAcct.deleteEx(AMTToolsMJournal.Table_ID, get_ID(), trxName);
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
}

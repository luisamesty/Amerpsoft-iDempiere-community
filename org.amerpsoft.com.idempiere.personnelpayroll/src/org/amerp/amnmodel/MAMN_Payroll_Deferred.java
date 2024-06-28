package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Properties;

import org.adempiere.util.IProcessUI;
import org.compiere.model.MClient;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;

public class MAMN_Payroll_Deferred extends X_AMN_Payroll_Deferred {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5757997611959319285L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Deferred.class);
	
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Deferred> s_cache = new CCache<Integer,MAMN_Payroll_Deferred>(Table_Name, 10);

	public MAMN_Payroll_Deferred(Properties ctx, int AMN_Payroll_Deferred_ID,
			String trxName) {
		super(ctx, AMN_Payroll_Deferred_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	public MAMN_Payroll_Deferred(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * createAmnPayrollDeferred
	 * @param ctx
	 * @param locale
	 * @param int p_AD_Client_ID
	 * @param int p_AD_Org_ID
	 * @param int p_AMN_Process_ID	Payroll Process
	 * @param int p_AMN_Contract_ID	Payroll Contract
	 * @param int p_AMN_Payroll_ID
	 * @param int p_AMN_Concept_Types_Proc_ID
	 * @param BigDecimal p_AmountQuota
	 * @return boolean
	 */
	public boolean createAmnPayrollDeferred(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,  int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID, int p_AMN_Period_ID, 
			BigDecimal p_AmountQuota, String p_Value, String p_Name, String p_Description, String trxName) {
		
		String Concept_Value = p_Value ;
		String Concept_Name = p_Name ;
		String Concept_Description = p_Description ;
		BigDecimal Concept_DefaultValue=BigDecimal.valueOf(0.00);
		int AMN_Employee_ID = 0;
		// GET Payrolldays from AMN_Contract (Valid Round Value if NOT Zero)
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		AMN_Employee_ID=amnpayroll.getAMN_Employee_ID();
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
  		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Payroll_Deferred amnpayrolldeferred = new MAMN_Payroll_Deferred(getCtx(), getAMN_Payroll_Deferred_ID(), get_TrxName());
		//log.warning("................Values in MAMN_Payroll_Deferred (NUEVO)...................");
		//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  AMN_Employee_ID:"+AMN_Employee_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
		//log.warning("ANTES DE EVALUAR p_AMN_Concept_Types_Proc_ID:"+p_AMN_Concept_Types_Proc_ID+"  Concept_DefaultValue:"+Concept_DefaultValue);
		// ***
		//log.warning("DESPUES DE EVALUAR p_AMN_Concept_Types_Proc_ID:"+p_AMN_Concept_Types_Proc_ID+"  Concept_DefaultValue:"+Concept_DefaultValue);
		amnpayrolldeferred.setAD_Client_ID(p_AD_Client_ID);
		amnpayrolldeferred.setAD_Org_ID(p_AD_Org_ID);
		amnpayrolldeferred.setAMN_Payroll_ID(p_AMN_Payroll_ID);
		amnpayrolldeferred.setAMN_Concept_Types_Proc_ID(p_AMN_Concept_Types_Proc_ID);
		amnpayrolldeferred.setAMN_Period_ID(p_AMN_Period_ID);
		amnpayrolldeferred.setAMN_Process_ID(p_AMN_Process_ID);
		amnpayrolldeferred.setAMN_Employee_ID(AMN_Employee_ID);
		amnpayrolldeferred.setValue(Concept_Value);
		amnpayrolldeferred.setName(Concept_Name);
		amnpayrolldeferred.setDescription(Concept_Description);
		amnpayrolldeferred.setQtyValue(Concept_DefaultValue);
		amnpayrolldeferred.setAmountCalculated(p_AmountQuota);
		amnpayrolldeferred.setAmountDeducted(p_AmountQuota);
		// p_mTab.setValue("Script",Concept_Script );			
		// SAVES NEW
		amnpayrolldeferred.save(get_TrxName());
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Payroll_Deferred_ID")+": "+
					amnpayrolldeferred.getValue()+"-"+amnpayrolldeferred.getName());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	createAmnPayrollDetail
	
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
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM AMN_Payroll_Deferred WHERE AMN_Payroll_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getAMN_Payroll_ID());
			setLine (ii);
		}
		return true;
	} //	beforeSave
}

package org.amerp.amfmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.acct.FactLine;
import org.compiere.model.X_Fact_Acct;
import org.compiere.util.CLogger;

public class AMF_MFactAcct extends X_Fact_Acct {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4915282956716439215L;

	/**	Static Logger	*/
	private static CLogger	log	= CLogger.getCLogger (AMF_MFactAcct.class);
	public AMF_MFactAcct(Properties ctx, int Fact_Acct_ID, String trxName) {
		super(ctx, Fact_Acct_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public AMF_MFactAcct(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param ctx
	 * @param locale
	 * @param int AD_Table_ID
	 * @param FactLine factline
	 * @param String trxName
	 */
	public static boolean createFactAcct(Properties ctx, int AD_Table_ID, 
			FactLine factline, String trxName) {
		FactLine fllocal = factline;
		//log.warning("factline:"+factline);
		fllocal.setAD_Table_ID(AD_Table_ID);
		fllocal.save(trxName);
		return false;
	}
	

}

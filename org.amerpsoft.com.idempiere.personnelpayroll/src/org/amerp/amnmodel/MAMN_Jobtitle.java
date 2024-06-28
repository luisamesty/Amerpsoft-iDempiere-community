package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Jobtitle extends X_AMN_Jobtitle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 358318982472880212L;
	static CLogger log = CLogger.getCLogger(MAMN_Jobtitle.class);

	public MAMN_Jobtitle(Properties ctx, int AMN_Jobtitle_ID, String trxName) {
		super(ctx, AMN_Jobtitle_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Jobtitle(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}


}

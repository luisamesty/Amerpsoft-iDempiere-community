package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Jobunit extends  X_AMN_Jobunit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2134103454035233812L;
	static CLogger log = CLogger.getCLogger(MAMN_Jobunit.class);

	public MAMN_Jobunit(Properties ctx, int AMN_Jobunit_ID, String trxName) {
		super(ctx, AMN_Jobunit_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	public MAMN_Jobunit(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}


}

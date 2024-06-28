package org.amerp.amnmodel;


import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Jobstation extends X_AMN_Jobstation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4407934892959079227L;
	static CLogger log = CLogger.getCLogger(MAMN_Jobstation.class);

	public MAMN_Jobstation(Properties ctx, int AMN_Jobstation_ID, String trxName) {
		super(ctx, AMN_Jobstation_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Jobstation(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}


}

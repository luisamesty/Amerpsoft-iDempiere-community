package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Leaves_Types extends X_AMN_Leaves_Types {

	private static final long serialVersionUID = -8988970381810939053L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves_Types.class);
	
	public MAMN_Leaves_Types(Properties ctx, int AMN_Leaves_Types_ID, String trxName) {
		super(ctx, AMN_Leaves_Types_ID, trxName);
		// 
	}

	public MAMN_Leaves_Types(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

}

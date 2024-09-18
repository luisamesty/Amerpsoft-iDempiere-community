package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Leaves  extends X_AMN_Leaves {

	private static final long serialVersionUID = -7169854017581281267L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves.class);
	
	public MAMN_Leaves(Properties ctx, int AMN_Leaves_ID, String trxName) {
		super(ctx, AMN_Leaves_ID, trxName);
		// 
	}
	
	public MAMN_Leaves(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

}

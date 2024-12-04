package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Leaves_Flow extends X_AMN_Leaves_Flow{
	

	private static final long serialVersionUID = -7618221848539898450L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves_Flow.class);
	
	public MAMN_Leaves_Flow(Properties ctx, int AMN_Leaves_Flow_ID, String trxName) {
		super(ctx, AMN_Leaves_Flow_ID, trxName);
		// 
	}
	
	public MAMN_Leaves_Flow(Properties ctx, int AMN_Leaves_Flow_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Leaves_Flow_ID, trxName, virtualColumns);
		// 
	}

	public MAMN_Leaves_Flow(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		//
	}

	public MAMN_Leaves_Flow(Properties ctx, String AMN_Leaves_Flow_UU, String trxName) {
		super(ctx, AMN_Leaves_Flow_UU, trxName);
		//
	}

	public MAMN_Leaves_Flow(Properties ctx, String AMN_Leaves_Flow_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Leaves_Flow_UU, trxName, virtualColumns);
		// 
	}


}

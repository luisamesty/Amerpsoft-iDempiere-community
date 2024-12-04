package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Leaves_Access extends X_AMN_Leaves_Access {

	private static final long serialVersionUID = -3445227725382124365L;

	static CLogger log = CLogger.getCLogger(MAMN_Leaves_Access.class);

	public MAMN_Leaves_Access(Properties ctx, int AMN_Leaves_Access_ID, String trxName) {
		super(ctx, AMN_Leaves_Access_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	
	public MAMN_Leaves_Access(Properties ctx, int AMN_Leaves_Access_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Leaves_Access_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}


	
	public MAMN_Leaves_Access(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}


	public MAMN_Leaves_Access(Properties ctx, String AMN_Leaves_Access_UU, String trxName) {
		super(ctx, AMN_Leaves_Access_UU, trxName);
		// TODO Auto-generated constructor stub
	}
	

	public MAMN_Leaves_Access(Properties ctx, String AMN_Leaves_Access_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Leaves_Access_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}


}

package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Leaves_Nodes extends X_AMN_Leaves_Nodes{


	private static final long serialVersionUID = -8300509494442666564L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Leaves_Nodes.class);
	
	public MAMN_Leaves_Nodes(Properties ctx, int AMN_Leaves_Nodes_ID, String trxName) {
		super(ctx, AMN_Leaves_Nodes_ID, trxName);
		// 
	}
	
	public MAMN_Leaves_Nodes(Properties ctx, int AMN_Leaves_Nodes_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Leaves_Nodes_ID, trxName, virtualColumns);
		// 
	}

	
	public MAMN_Leaves_Nodes(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	public MAMN_Leaves_Nodes(Properties ctx, String AMN_Leaves_Nodes_UU, String trxName) {
		super(ctx, AMN_Leaves_Nodes_UU, trxName);
		// 
	}

	
	public MAMN_Leaves_Nodes(Properties ctx, String AMN_Leaves_Nodes_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMN_Leaves_Nodes_UU, trxName, virtualColumns);
		// 
	}

	
}

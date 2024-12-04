package org.amerp.workflow.amwmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMW_WorkFlow extends X_AMW_WorkFlow {

	private static final long serialVersionUID = 4526550064084127549L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WorkFlow.class);

	public MAMW_WorkFlow(Properties ctx, int AMW_WorkFlow_ID, String trxName) {
		super(ctx, AMW_WorkFlow_ID, trxName);
		// 
	}

	public MAMW_WorkFlow(Properties ctx, int AMW_WorkFlow_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WorkFlow_ID, trxName, virtualColumns);
		// 
	}
	
	public MAMW_WorkFlow(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}
	
	public MAMW_WorkFlow(Properties ctx, String AMW_WorkFlow_UU, String trxName) {
		super(ctx, AMW_WorkFlow_UU, trxName);
		// 
	}

	public MAMW_WorkFlow(Properties ctx, String AMW_WorkFlow_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WorkFlow_UU, trxName, virtualColumns);
		// 
	}


}

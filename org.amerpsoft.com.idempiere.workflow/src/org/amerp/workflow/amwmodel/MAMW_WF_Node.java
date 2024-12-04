package org.amerp.workflow.amwmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMW_WF_Node extends X_AMW_WF_Node{

	private static final long serialVersionUID = -8132020842201252418L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WF_Node.class);
	
	public MAMW_WF_Node(Properties ctx, int AMW_WF_Node_ID, String trxName) {
		super(ctx, AMW_WF_Node_ID, trxName);
		// 
	}

	public MAMW_WF_Node(Properties ctx, int AMW_WF_Node_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_Node_ID, trxName, virtualColumns);
		// 
	}

	public MAMW_WF_Node(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	public MAMW_WF_Node(Properties ctx, String AMW_WF_Node_UU, String trxName) {
		super(ctx, AMW_WF_Node_UU, trxName);
		// 
	}

	public MAMW_WF_Node(Properties ctx, String AMW_WF_Node_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_Node_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

}

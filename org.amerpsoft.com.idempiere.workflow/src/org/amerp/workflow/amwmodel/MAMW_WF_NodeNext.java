package org.amerp.workflow.amwmodel;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.CLogger;

public class MAMW_WF_NodeNext extends X_AMW_WF_NodeNext {


	private static final long serialVersionUID = 4097662513001973429L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WF_NodeNext.class);
	
	public MAMW_WF_NodeNext(Properties ctx, int AMW_WF_NodeNext_ID, String trxName) {
		super(ctx, AMW_WF_NodeNext_ID, trxName);
		// 
	}
	
	public MAMW_WF_NodeNext(Properties ctx, int AMW_WF_NodeNext_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_NodeNext_ID, trxName, virtualColumns);
		// 
	}	
	
	public MAMW_WF_NodeNext(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}
	
	public MAMW_WF_NodeNext(Properties ctx, String AMW_WF_NodeNext_UU, String trxName) {
		super(ctx, AMW_WF_NodeNext_UU, trxName);
		// 
	}

	public MAMW_WF_NodeNext(Properties ctx, String AMW_WF_NodeNext_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_NodeNext_UU, trxName, virtualColumns);
		// 
	}





}

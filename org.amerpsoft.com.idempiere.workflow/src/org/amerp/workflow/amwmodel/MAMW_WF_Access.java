package org.amerp.workflow.amwmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMW_WF_Access extends X_AMW_WF_Access {

	
	private static final long serialVersionUID = -6363415806147629841L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WF_Access.class);
			
	public MAMW_WF_Access(Properties ctx, int AMW_WF_Access_ID, String trxName) {
		super(ctx, AMW_WF_Access_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMW_WF_Access(Properties ctx, int AMW_WF_Access_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_Access_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MAMW_WF_Access(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMW_WF_Access(Properties ctx, String AMW_WF_Access_UU, String trxName) {
		super(ctx, AMW_WF_Access_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMW_WF_Access(Properties ctx, String AMW_WF_Access_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_Access_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}


}

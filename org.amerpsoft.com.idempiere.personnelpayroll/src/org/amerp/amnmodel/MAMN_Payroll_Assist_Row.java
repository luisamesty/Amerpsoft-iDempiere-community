package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Payroll_Assist_Row extends X_AMN_Payroll_Assist_Row{


	private static final long serialVersionUID = -7351075011668189707L;

	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Assist_Row.class);
	
	public MAMN_Payroll_Assist_Row(Properties ctx, int AMN_Payroll_Assist_Row_ID, String trxName) {
		super(ctx, AMN_Payroll_Assist_Row_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Payroll_Assist_Row(Properties ctx, int AMN_Payroll_Assist_Row_ID, String trxName,
			String[] virtualColumns) {
		super(ctx, AMN_Payroll_Assist_Row_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Payroll_Assist_Row(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Payroll_Assist_Row(Properties ctx, String AMN_Payroll_Assist_Row_UU, String trxName) {
		super(ctx, AMN_Payroll_Assist_Row_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Payroll_Assist_Row(Properties ctx, String AMN_Payroll_Assist_Row_UU, String trxName,
			String[] virtualColumns) {
		super(ctx, AMN_Payroll_Assist_Row_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

}

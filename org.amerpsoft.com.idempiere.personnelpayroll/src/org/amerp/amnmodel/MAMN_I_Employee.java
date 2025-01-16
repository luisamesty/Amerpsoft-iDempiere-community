package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_I_Employee extends X_AMN_I_Employee{

	private static final long serialVersionUID = 9207960560504176549L;
	static CLogger log = CLogger.getCLogger(MAMN_I_Employee.class);

	public MAMN_I_Employee(Properties ctx, int AMN_I_Employee_ID, String trxName) {
		super(ctx, AMN_I_Employee_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_I_Employee(Properties ctx, int AMN_I_Employee_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMN_I_Employee_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MAMN_I_Employee(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_I_Employee(Properties ctx, String AMN_I_Employee_UU, String trxName) {
		super(ctx, AMN_I_Employee_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_I_Employee(Properties ctx, String AMN_I_Employee_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMN_I_Employee_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

}

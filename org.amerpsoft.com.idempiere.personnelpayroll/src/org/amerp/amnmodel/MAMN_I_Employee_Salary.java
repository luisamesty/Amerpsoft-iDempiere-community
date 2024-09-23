package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_I_Employee_Salary extends X_AMN_I_Employee_Salary {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8875933887755531642L;
	
	static CLogger log = CLogger.getCLogger(MAMN_I_Employee_Salary.class);

	public MAMN_I_Employee_Salary(Properties ctx, int AMN_I_Employee_Salary_ID, String trxName) {
		super(ctx, AMN_I_Employee_Salary_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_I_Employee_Salary(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}



}

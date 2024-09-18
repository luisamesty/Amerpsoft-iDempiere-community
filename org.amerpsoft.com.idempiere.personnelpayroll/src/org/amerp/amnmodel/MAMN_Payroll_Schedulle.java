package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Payroll_Schedulle extends X_AMN_Payroll_Schedulle{

	private static final long serialVersionUID = 2549346462615910945L;

	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Schedulle.class);
	
	public MAMN_Payroll_Schedulle(Properties ctx, int AMN_Payroll_Schedulle_ID, String trxName) {
		super(ctx, AMN_Payroll_Schedulle_ID, trxName);
		// 
	}
	
	public MAMN_Payroll_Schedulle(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	public boolean createAmnPayrollSchedulle(Properties ctx, 
			int p_C_Year_ID, int p_C_Period_ID, int p_AMN_Contract_ID, int p_AMN_Employee_ID, 
			String trxName) {
		
		return false;
	}
		
}

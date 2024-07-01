package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Payroll_Lot extends X_AMN_Payroll_Lot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783930916118012342L;
	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Lot.class);
	
	public MAMN_Payroll_Lot(Properties ctx, int AMN_Payroll_Lot_ID,
			String trxName) {
		super(ctx, AMN_Payroll_Lot_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Payroll_Lot(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	

}

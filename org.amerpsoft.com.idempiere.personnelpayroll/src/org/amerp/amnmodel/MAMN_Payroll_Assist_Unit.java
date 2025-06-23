package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMN_Payroll_Assist_Unit extends X_AMN_Payroll_Assist_Unit{


	private static final long serialVersionUID = -8509537407768802238L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Assist_Unit.class);

	public MAMN_Payroll_Assist_Unit(Properties ctx, int AMN_Payroll_Assist_Unit_ID, String trxName) {
		super(ctx, AMN_Payroll_Assist_Unit_ID, trxName);
		// 
	}

	public MAMN_Payroll_Assist_Unit(Properties ctx, int AMN_Payroll_Assist_Unit_ID, String trxName,
			String[] virtualColumns) {
		super(ctx, AMN_Payroll_Assist_Unit_ID, trxName, virtualColumns);
		// 
	}

	public MAMN_Payroll_Assist_Unit(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	public MAMN_Payroll_Assist_Unit(Properties ctx, String AMN_Payroll_Assist_Unit_UU, String trxName) {
		super(ctx, AMN_Payroll_Assist_Unit_UU, trxName);
		//
	}

	public MAMN_Payroll_Assist_Unit(Properties ctx, String AMN_Payroll_Assist_Unit_UU, String trxName,
			String[] virtualColumns) {
		super(ctx, AMN_Payroll_Assist_Unit_UU, trxName, virtualColumns);
		// 
	}
	
	// Constructor para ser llamado desde entorno System
	public MAMN_Payroll_Assist_Unit(Properties ctx, int AD_Client_ID, int AD_Org_ID, String trxName) {
		super(ctx, 0, trxName);
	    setAD_Client_ID(AD_Client_ID);  // ← Visible porque estás dentro de la subclase
	    setAD_Org_ID(AD_Org_ID);
	    setIsActive(true);
	}


}

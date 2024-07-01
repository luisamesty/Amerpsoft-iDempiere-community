package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMN_Charge extends X_AMN_Charge{

	
	private static final long serialVersionUID = 1L;

	static CLogger log = CLogger.getCLogger(MAMN_Charge.class);
	
	public MAMN_Charge(Properties ctx, int AMN_Charge_ID, String trxName) {
		super(ctx, AMN_Charge_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Charge(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * findC_Charge_ID: 
	 * Returns unique C_Charge_ID from a given Payroll Process and work force
	 * @param ctx
	 * @param AMN_Process_ID
	 * @param workForce
	 * @return
	 */
	public int findC_Charge_ID(Properties ctx, int AMN_Process_ID, String workForce) {
		
		int retValue = 0;
		MAMN_Charge mch = findAMNCharge(ctx, AMN_Process_ID);
		// Administrative Workforce
		if (workForce.compareToIgnoreCase("A") == 0  ) { 
			retValue = mch.getC_Charge_AW_ID();
		}
		// Direct WorkForce
		else if (workForce.compareToIgnoreCase("D") == 0 ) {  
			retValue = mch.getC_Charge_DW_ID();
		}
		// Indirect Workforce
		else if (workForce.compareToIgnoreCase("I") == 0 ) {
			retValue = mch.getC_Charge_IW_ID();
		}
		// Sales Workforce
		else if (workForce.compareToIgnoreCase("S") == 0  ) { 
			retValue = mch.getC_Charge_SW_ID();
		}
		// Directors Workforce
		else if (workForce.compareToIgnoreCase("M") == 0  ) { 
			retValue = mch.getC_Charge_MW_ID();
		} 
		// Default Administrative workforce
		else {
			retValue = mch.getC_Charge_AW_ID();
		}
		return retValue;
		
	}
	
	/**
	 * findAMNCharge
	 * Return Unique AMN_Charge form a given process
	 * @param ctx
	 * @param AMN_Process_ID
	 * @return
	 */
	public MAMN_Charge findAMNCharge(Properties ctx, int AMN_Process_ID) {

		MAMN_Charge retValue = null;
		String sql = "SELECT AMN_Charge_ID "
				+ " FROM AMN_Charge " 
				+ " WHERE AMN_Process_ID=" + AMN_Process_ID
				+ " AND isActive='Y' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// log.warning("sql="+sql);
		try {
			pstmt = DB.prepareStatement(sql, null);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				MAMN_Charge amncharge = new MAMN_Charge(ctx, rs.getInt(1),
						null);
				// log.warning("MAMN_Concept_Types_Acct_ID="+rs.getInt(1));
				if (amncharge.isActive()) 
					retValue = amncharge;
			}
		} catch (SQLException e) {
			retValue = null;
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		return retValue;
	}

}

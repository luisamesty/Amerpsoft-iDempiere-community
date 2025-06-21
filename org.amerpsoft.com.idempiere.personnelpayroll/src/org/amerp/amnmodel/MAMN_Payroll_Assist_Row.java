package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.amerp.amnutilities.AmerpPayrollCalcUtilDVFormulas;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;

public class MAMN_Payroll_Assist_Row extends X_AMN_Payroll_Assist_Row{


	private static final long serialVersionUID = -7351075011668189707L;

	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Assist_Row.class);
	
	public MAMN_Payroll_Assist_Row(Properties ctx, int AMN_Payroll_Assist_Row_ID, String trxName) {
		super(ctx, AMN_Payroll_Assist_Row_ID, trxName);
		// 
	}
	
	public MAMN_Payroll_Assist_Row(Properties ctx, int AMN_Payroll_Assist_Row_ID, String trxName,
			String[] virtualColumns) {
		super(ctx, AMN_Payroll_Assist_Row_ID, trxName, virtualColumns);
		// 
	}

	public MAMN_Payroll_Assist_Row(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	public MAMN_Payroll_Assist_Row(Properties ctx, String AMN_Payroll_Assist_Row_UU, String trxName) {
		super(ctx, AMN_Payroll_Assist_Row_UU, trxName);
		// 
	}

	public MAMN_Payroll_Assist_Row(Properties ctx, String AMN_Payroll_Assist_Row_UU, String trxName,
			String[] virtualColumns) {
		super(ctx, AMN_Payroll_Assist_Row_UU, trxName, virtualColumns);
		// 
	}

	/**
	 * updateAMNPayrollAssistRow
	 * Updates AMN_Payroll_Assist_Row IsVerified with 'Y'
	 * Updates isverified to 'Y'
	 * @param ctx
	 * @param p_AMN_Payroll_Assist_Row_ID
	 * @param trxName
	 * @return
	 */
	public int updateAMNPayrollAssistRow(Properties ctx, int p_AMN_Payroll_Assist_Row_ID, String trxName) {
		
			// Update AMN_Payroll_Assist_Row
            String sql = "UPDATE AMN_Payroll_Assist_Row "
            		+ " set IsVerified='Y'"
					+ " where amn_payroll_assist_row_id ="+p_AMN_Payroll_Assist_Row_ID;
            //
            return DB.executeUpdateEx(sql, null);
	}
}

package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMN_CommissionGroup extends X_AMN_CommissionGroup{


	private static final long serialVersionUID = 2448927996258691464L;
	
	static CLogger s_log = CLogger.getCLogger(MAMN_CommissionGroup.class);

	public MAMN_CommissionGroup(Properties ctx, int AMN_CommissionGroup_ID, String trxName) {
		super(ctx, AMN_CommissionGroup_ID, trxName);
		// 
	}
	
	public MAMN_CommissionGroup(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	/**
	 * calculateCTL_QtyReceipts:
	 *  Return getCTL_QtyReceipts from Payroll for same CommissionGroup Value in One period for all contracts
	 *  Example OCBA Value
	 *  Default Value 1
	 * @param CurFrom_ID
	 * @param CurTo_ID
	 * @param PeriodDate
	 * @param ConversionType_ID
	 * @param AD_Client_ID
	 * @return
	 */
	public Integer calculateCTL_QtyReceipts(int AD_Client_ID, String CommissionGroup, int AMN_Concept_Types_ID, int AMN_Concept_Types_Limit_ID) {

		// Get Rate
		String sql = "	SELECT COUNT(DISTINCT AMN_Payroll_ID) AS noRecibos FROM ( "
				+ "	SELECT ap.AMN_Payroll_ID , ap.name , apd.qtyvalue, ac.value , ac.amn_commissiongroup_id "
				+ "	FROM amn_payroll ap "
				+ "	INNER JOIN amn_payroll_detail apd ON apd.amn_payroll_id = ap.amn_payroll_id "
				+ "	INNER JOIN ( "
				+ "		SELECT DISTINCT ap.amn_period_id, ap.amndateini, ap.amndateend "
				+ "		FROM amn_period ap "
				+ "		INNER JOIN amn_contract cnt ON cnt.amn_contract_id = ap.amn_contract_id  "
				+ "		INNER JOIN amn_concept_types_limit amctl ON amctl.ad_client_id = ap.ad_client_id "
				+ "		WHERE ap.amndateini >= amctl.validfrom AND ap.amndateend <= amctl.validto "
				+ "		AND ap.amn_contract_id IN ( "
				+ "			SELECT AMN_Contract_ID 	FROM amn_concept_types_contract	WHERE AMN_Concept_Types_ID="+AMN_Concept_Types_ID
				+ "		) "
				+ "		AND ap.amn_process_id IN ( "
				+ "			SELECT AMN_Process_ID FROM amn_concept_types_proc WHERE AMN_Concept_Types_ID="+AMN_Concept_Types_ID
				+ "		) "
				+ "		AND amctl.amn_concept_types_limit_id ="+AMN_Concept_Types_Limit_ID
				+ "	) AS periodo ON periodo.amn_period_id = ap.amn_period_id "
				+ "	INNER JOIN amn_employee emp ON emp.amn_employee_id = ap.amn_employee_id  "
				+ "	INNER JOIN amn_contract ac2 ON ac2.amn_contract_id = emp.amn_contract_id "
				+ "	INNER JOIN amn_commissiongroup ac ON ac.amn_commissiongroup_id = emp.amn_commissiongroup_id "
				+ "	INNER JOIN amn_concept_types_proc ctp ON ctp.amn_concept_types_proc_id = apd.amn_concept_types_proc_id "
				+ "	INNER JOIN amn_concept_types ct ON ct.amn_concept_types_id =ctp.amn_concept_types_id AND ct.amn_concept_types_id = "+AMN_Concept_Types_Limit_ID
				+ "	WHERE ac.value = '"+CommissionGroup+"'"
				+ ") AS norec";
		Integer retValue = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);

			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getInt(1);
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "getRate", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (retValue == 0)
			if (s_log.isLoggable(Level.INFO))
				s_log.info("getAMN_Concept_Types_Limit_ID_ - not found - AMN_Concept_Types_ID="+AMN_Concept_Types_ID+" - Client="
								+ AD_Client_ID + " - CommissionGroup="+CommissionGroup+"  AMN_Concept_Types_ID="+AMN_Concept_Types_ID);
		return retValue;
	} // calculateCTL_QtyReceipts	
}

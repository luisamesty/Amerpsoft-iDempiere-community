package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMN_Concept_Types_Charge extends X_AMN_Concept_Types_Charge{

	
	private static final long serialVersionUID = 1L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Concept_Types_Charge.class);
	
	public MAMN_Concept_Types_Charge(Properties ctx, int AMN_Concept_Types_Charge_ID, String trxName) {
		super(ctx, AMN_Concept_Types_Charge_ID, trxName);
		// 
	}

	public MAMN_Concept_Types_Charge(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}
	
	/**
	 * findAMNConceptTypesCharge
	 * 
	 * @param ctx
	 * @param AMN_Concept_Types_ID
	 * @return
	 */
	public static MAMN_Concept_Types_Charge findAMNConceptTypesCharge(Properties ctx, int AMN_Concept_Types_ID) {

		MAMN_Concept_Types_Charge retValue = null;
		String sql = "SELECT AMN_Concept_Types_Charge_ID, " + " AMN_Concept_Types_ID "
				+ " FROM AMN_Concept_Types_Charge " + " WHERE AMN_Concept_Types_ID=" + AMN_Concept_Types_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// log.warning("sql="+sql);
		try {
			pstmt = DB.prepareStatement(sql, null);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				MAMN_Concept_Types_Charge amnconcepttypescharge = new MAMN_Concept_Types_Charge(ctx, rs.getInt(1),
						null);
				// log.warning("MAMN_Concept_Types_Acct_ID="+rs.getInt(1));
				if (amnconcepttypescharge.isActive())
					retValue = amnconcepttypescharge;
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

	/**
	 * Copy copyChargesFrom settings from another ConceptTypes overwrites existing
	 * data
	 * 
	 * @param source
	 */
	public void copyChargesFrom( MAMN_Concept_Types_Charge source) {

		if (log.isLoggable(Level.FINE))
			log.log(Level.FINE, "Copying from:" + source + ", to: " + this);

		setC_Charge_AW_ID(source.getC_Charge_AW_ID());
		setC_Charge_DW_ID(source.getC_Charge_DW_ID());
		setC_Charge_SW_ID(source.getC_Charge_SW_ID());
		setC_Charge_MW_ID(source.getC_Charge_MW_ID());
		setC_Charge_IW_ID(source.getC_Charge_IW_ID());
		saveEx();

	}

}

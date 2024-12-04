package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MConversionType;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.TimeUtil;

public class MAMN_Concept_Types_Limit  extends X_AMN_Concept_Types_Limit {

	private static final long serialVersionUID = -4568823390001083325L;
	
	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (MAMN_Concept_Types_Limit.class);

	private int CTL_Currency_ID;
	private BigDecimal CTL_AmountAllocated;
	private BigDecimal CTL_QtyTimes;
	private BigDecimal CTL_Rate;
	
	
	public MAMN_Concept_Types_Limit(Properties ctx, int AMN_Concept_Types_Limit_ID, String trxName) {
		super(ctx, AMN_Concept_Types_Limit_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Concept_Types_Limit(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * getCTL_AmountAllocated:
	 * Return AmountAllocated from AMN_Concept_Types_Limit
	 * @param CurFrom_ID
	 * @param CurTo_ID
	 * @param PeriodDate
	 * @param ConversionType_ID
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return
	 */
	public static BigDecimal getCTL_AmountAllocated(int AMN_Concept_Types_Limit_ID, int CurFrom_ID, int CurTo_ID, Timestamp PeriodDate,
			int ConversionType_ID, int AD_Client_ID, int AD_Org_ID) {

		// Conversion Type
		int C_ConversionType_ID = ConversionType_ID;
		if (C_ConversionType_ID == 0)
			C_ConversionType_ID = MConversionType.getDefault(AD_Client_ID);
		// Conversion Date
		if (PeriodDate == null)
			return BigDecimal.ZERO;

		// Get Rate
		String sql = "	SELECT amountallocated " 
				+ "	FROM amn_concept_types_limit actl "
				+ "	WHERE C_Currency_ID=?" 
				+ "	 AND C_Currency_ID_To=?" 
				+ "	 AND	C_ConversionType_ID=?"
				+ "	 AND	? BETWEEN ValidFrom AND ValidTo " + "	 AND AD_Client_ID IN (0,?) "
				+ "	 AND AD_Org_ID IN (0,?) " 
				+ "	 AND IsActive = 'Y' "
				+ "  AND amn_concept_types_limit_id = ? "
				+ "	ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, ValidFrom DESC";
		BigDecimal retValue = BigDecimal.ZERO;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, CurFrom_ID);
			pstmt.setInt(2, CurTo_ID);
			pstmt.setInt(3, C_ConversionType_ID);
			pstmt.setTimestamp(4, TimeUtil.getDay(PeriodDate));
			pstmt.setInt(5, AD_Client_ID);
			pstmt.setInt(6, AD_Org_ID);
			pstmt.setInt(7, AMN_Concept_Types_Limit_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "getRate", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (retValue == null)
			if (s_log.isLoggable(Level.INFO))
				s_log.info("getRate - not found - CurFrom=" + CurFrom_ID + ", CurTo=" + CurTo_ID + ", " + PeriodDate
						+ ", Type=" + ConversionType_ID
						+ (ConversionType_ID == C_ConversionType_ID ? "" : "->" + C_ConversionType_ID) + ", Client="
						+ AD_Client_ID + ", Org=" + AD_Org_ID);
		return retValue;
	} // getCTL_AmountAllocated
	
	/**
	 * getCTL_Rate:
	 *  Return Rate from AMN_Concept_Types_Limit
	 * @param CurFrom_ID
	 * @param CurTo_ID
	 * @param PeriodDate
	 * @param ConversionType_ID
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return
	 */
	public static BigDecimal getCTL_Rate(int CurFrom_ID, int CurTo_ID, Timestamp PeriodDate,
			int ConversionType_ID, int AD_Client_ID, int AD_Org_ID) {

		// Conversion Type
		int C_ConversionType_ID = ConversionType_ID;
		if (C_ConversionType_ID == 0)
			C_ConversionType_ID = MConversionType.getDefault(AD_Client_ID);
		// Conversion Date
		if (PeriodDate == null)
			return BigDecimal.ZERO;

		// Get Rate
		String sql = "	SELECT rate " 
				+ "	FROM amn_concept_types_limit actl "
				+ "	WHERE C_Currency_ID=?" 
				+ "	 AND C_Currency_ID_To=?" + "	 AND	C_ConversionType_ID=?"
				+ "	 AND	? BETWEEN ValidFrom AND ValidTo " + "	 AND AD_Client_ID IN (0,?) "
				+ "	 AND AD_Org_ID IN (0,?) " 
				+ "	 AND IsActive = 'Y' "
				+ "	ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, ValidFrom DESC";
		BigDecimal retValue = BigDecimal.ZERO;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, CurFrom_ID);
			pstmt.setInt(2, CurTo_ID);
			pstmt.setInt(3, C_ConversionType_ID);
			pstmt.setTimestamp(4, TimeUtil.getDay(PeriodDate));
			pstmt.setInt(5, AD_Client_ID);
			pstmt.setInt(6, AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "getRate", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (retValue == null)
			if (s_log.isLoggable(Level.INFO))
				s_log.info("getRate - not found - CurFrom=" + CurFrom_ID + ", CurTo=" + CurTo_ID + ", " + PeriodDate
						+ ", Type=" + ConversionType_ID
						+ (ConversionType_ID == C_ConversionType_ID ? "" : "->" + C_ConversionType_ID) + ", Client="
						+ AD_Client_ID + ", Org=" + AD_Org_ID);
		return retValue;
	} // getCTL_Rate
	
	/**
	 * getCTL_QtyTimes:
	 *  Return QtyTimes from AMN_Concept_Types_Limit
	 *  Default Value 1
	 * @param CurFrom_ID
	 * @param CurTo_ID
	 * @param PeriodDate
	 * @param ConversionType_ID
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @return
	 */
	public static BigDecimal getCTL_QtyTimes(int CurFrom_ID, int CurTo_ID, Timestamp PeriodDate,
			int ConversionType_ID, int AD_Client_ID, int AD_Org_ID) {

		// Conversion Type
		int C_ConversionType_ID = ConversionType_ID;
		if (C_ConversionType_ID == 0)
			C_ConversionType_ID = MConversionType.getDefault(AD_Client_ID);
		// Conversion Date
		if (PeriodDate == null)
			return BigDecimal.ZERO;

		// Get Rate
		String sql = "	SELECT QtyTimes " 
				+ "	FROM amn_concept_types_limit actl "
				+ "	WHERE C_Currency_ID=?" 
				+ "	 AND C_Currency_ID_To=?" 
				+ "	 AND	C_ConversionType_ID=?"
				+ "	 AND	? BETWEEN ValidFrom AND ValidTo " 
				+ "	 AND AD_Client_ID IN (0,?) "
				+ "	 AND AD_Org_ID IN (0,?) " 
				+ "	 AND IsActive = 'Y' "
				+ "	ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, ValidFrom DESC";
		BigDecimal retValue = BigDecimal.ONE;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, CurFrom_ID);
			pstmt.setInt(2, CurTo_ID);
			pstmt.setInt(3, C_ConversionType_ID);
			pstmt.setTimestamp(4, TimeUtil.getDay(PeriodDate));
			pstmt.setInt(5, AD_Client_ID);
			pstmt.setInt(6, AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
		} catch (Exception e) {
			s_log.log(Level.SEVERE, "getRate", e);
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (retValue == null)
			if (s_log.isLoggable(Level.INFO))
				s_log.info("getRate - not found - CurFrom=" + CurFrom_ID + ", CurTo=" + CurTo_ID + ", " + PeriodDate
						+ ", Type=" + ConversionType_ID
						+ (ConversionType_ID == C_ConversionType_ID ? "" : "->" + C_ConversionType_ID) + ", Client="
						+ AD_Client_ID + ", Org=" + AD_Org_ID);
		return retValue;
	} // CTL_QtyTimes
	
	
	/**
	 * searchAMN_Concept_Types_Limit_ID_
	 * Return UNIQUE AMN_Concept_Types_Limit_ID giving DateFrom, DateTo and AMN_Concept_Types_ID
	 * @param AMN_Concept_Types_ID
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @param p_validFrom
	 * @param p_validTo
	 * @return
	 */
	public static Integer searchAMN_Concept_Types_Limit_ID_(int AMN_Concept_Types_ID, int AD_Client_ID, int AD_Org_ID, 
			Timestamp p_validFrom, Timestamp p_validTo ) {

		// Get ID
		String sql = "SELECT DISTINCT AMN_Concept_Types_Limit_ID FROM amn_concept_types_limit actl "
				+ " WHERE AMN_Concept_Types_ID = "+AMN_Concept_Types_ID
				+ " AND AD_Client_ID = "+ AD_Client_ID
				+ " AND AD_Org_ID =  "+ AD_Org_ID
				+ " AND actl.validfrom  >= '" +p_validFrom+"' AND actl.validto  <=  '"+p_validTo+"'";
		Integer retValue =0;
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
						+ AD_Client_ID + ", Org=" + AD_Org_ID+ " - Date From="+p_validFrom+" - Date To"+p_validTo);
		return retValue;
	} // getCTL_Rate
	
	
	public int getCTL_Currency_ID() {
		return CTL_Currency_ID;
	}

	public void setCTL_Currency_ID(int cTL_Currency_ID) {
		CTL_Currency_ID = cTL_Currency_ID;
	}

	public BigDecimal getCTL_AmountAllocated() {
		return CTL_AmountAllocated;
	}

	public void setCTL_AmountAllocated(BigDecimal cTL_AmountAllocated) {
		CTL_AmountAllocated = cTL_AmountAllocated;
	}

	public BigDecimal getCTL_QtyTimes() {
		return CTL_QtyTimes;
	}

	public void setCTL_QtyTimes(BigDecimal cTL_QtyTimes) {
		CTL_QtyTimes = cTL_QtyTimes;
	}

	public BigDecimal getCTL_Rate() {
		return CTL_Rate;
	}

	public void setCTL_Rate(BigDecimal cTL_Rate) {
		CTL_Rate = cTL_Rate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}

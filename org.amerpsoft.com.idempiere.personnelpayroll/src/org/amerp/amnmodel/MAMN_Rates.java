/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.MConversionRate;
import org.compiere.util.*;

public class MAMN_Rates extends X_AMN_Rates {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7759017755714909352L;

	public MAMN_Rates(Properties ctx, int AMN_Rates_ID, String trxName) {
		super(ctx, AMN_Rates_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Rates(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 *	Get SQL sqlGetActive_Int_Rate 
	 *  @param  p_PayrollDate    The Payroll End Date date - if null - use current date
	 *  				Changed to StartDate Nov 2015 LUis Amesty
	 * 	@param	p_AD_Client_ID client
	 * 	@param  p_AD_Org_ID	organization
	 *  @return p_currency Rate or null
	 */
	public static BigDecimal sqlGetActive_Int_Rate (Timestamp p_PayrollDate, 
			int p_currency, int p_AD_Client_ID, int p_AD_Org_ID)
	{
		/**	Logger						*/
		CLogger		log = CLogger.getCLogger (MAMN_Rates.class);

		//	p_PayrollDate Date
		if (p_PayrollDate == null)
			p_PayrollDate = new Timestamp (System.currentTimeMillis());
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		//	Get Rate
		String sql = "SELECT Active_Int_Rate " + 
				"FROM AMN_Rates " + 
				"WHERE C_Currency_ID=? " + 
				"AND ? BETWEEN startdate AND enddate " + 
				"AND AD_Client_ID IN (0,?) " + 
				"AND AD_Org_ID IN (0,?) " + 
				"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, startdate DESC" ;
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, p_currency);
			pstmt.setTimestamp(2, p_PayrollDate);
			pstmt.setInt(3, p_AD_Client_ID);
			pstmt.setInt(4, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "sqlGetActive_Int_Rate", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (retValue == null) {
			if (log.isLoggable(Level.INFO)) log.info ("sqlGetActive_Int_Rate - not found - " 
			  + ", Currency=" + p_currency
			  + ", PayrollDate=" + p_PayrollDate 
			  + ", Client=" + p_AD_Client_ID 
			  + ", Org=" + p_AD_Org_ID);
			retValue=BigDecimal.valueOf(0.00);
		}
		return retValue;
	}	//	sqlGetActive_Int_Rate

	/**
	 *	Get SQL sqlGetActive_Int_Rate2 
	 *  @param  p_PayrollDate    The Payroll End Date date - if null - use current date
	 *  				Changed to StartDate Nov 2015 LUis Amesty
	 * 	@param	p_AD_Client_ID client
	 * 	@param  p_AD_Org_ID	organization
	 *  @return p_currency Rate or null
	 */
	public static BigDecimal sqlGetActive_Int_Rate2 (Timestamp p_PayrollDate, 
			int p_currency, int p_AD_Client_ID, int p_AD_Org_ID)
	{
		/**	Logger						*/
		CLogger		log = CLogger.getCLogger (MAMN_Rates.class);

		//	p_PayrollDate Date
		if (p_PayrollDate == null)
			p_PayrollDate = new Timestamp (System.currentTimeMillis());
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		//	Get Rate
		String sql = "SELECT Active_Int_Rate2 " + 
				"FROM AMN_Rates " + 
				"WHERE C_Currency_ID=? " + 
				"AND ? BETWEEN startdate AND enddate " + 
				"AND AD_Client_ID IN (0,?) " + 
				"AND AD_Org_ID IN (0,?) " + 
				"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, startdate DESC" ;
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, p_currency);
			pstmt.setTimestamp(2, p_PayrollDate);
			pstmt.setInt(3, p_AD_Client_ID);
			pstmt.setInt(4, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				retValue = rs.getBigDecimal(1);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "sqlGetActive_Int_Rate2", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (retValue == null) {
			if (log.isLoggable(Level.INFO)) log.info ("sqlGetActive_Int_Rate2 - not found - " 
			  + ", Currency=" + p_currency
			  + ", PayrollDate=" + p_PayrollDate 
			  + ", Client=" + p_AD_Client_ID 
			  + ", Org=" + p_AD_Org_ID);
			retValue=BigDecimal.valueOf(0.00);
		}
		return retValue;
	}	//	sqlGetActive_Int_Rate2

	
	/**
	 *	Get SQL Salary_MO 
	 *  @param  p_PayrollDate    The Payroll End Date date - if null - use current date
	 *  				Changed to StartDate Nov 2015 LUis Amesty
	 * 	@param	p_currency
	 *  @param	p_conversiontype_id
	 *  @param	p_AD_Client_ID client
	 * 	@param  p_AD_Org_ID	organization
	 *  @return Bigdecimal Salary MO
	 */
	public static BigDecimal sqlGetSalary_MO (Timestamp p_PayrollDate, 
			int p_currency, int p_conversiontype_id, int p_AD_Client_ID, int p_AD_Org_ID)
	{
		/**	Logger						*/
		CLogger		log = CLogger.getCLogger (MAMN_Rates.class);
		BigDecimal multiplyFactor = BigDecimal.ZERO;
		if (p_PayrollDate == null)
			p_PayrollDate = new Timestamp (System.currentTimeMillis());
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		//	Get Rate
		String sql = "SELECT salary_mo, C_Currency_ID, C_ConversionType_ID " + 
				"FROM AMN_Rates " + 
				"WHERE  ? BETWEEN startdate AND enddate " + 
				"AND AD_Client_ID IN (0,?) " + 
				"AND AD_Org_ID IN (0,?) " + 
				"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC, startdate DESC" ;
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//log.warning("SQL SALARY MO:"+sql);
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setTimestamp(1, p_PayrollDate);
			pstmt.setInt(2, p_AD_Client_ID);
			pstmt.setInt(3, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (p_currency == rs.getInt(2)) {
					retValue = rs.getBigDecimal(1);
//log.warning("SALARIO MO:"+retValue);
					break;
				} else {
					multiplyFactor = MConversionRate.getRate(rs.getInt(2), p_currency, p_PayrollDate, p_conversiontype_id, p_AD_Client_ID, p_AD_Org_ID);
					retValue = rs.getBigDecimal(1).multiply(multiplyFactor);
//log.warning("SALARIO MO:"+retValue);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "sqlGetSalary_MO", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (retValue == null) {
			if (log.isLoggable(Level.INFO)) log.info ("sqlGetSalary_MO - not found - " 
					  + ", Currency=" + p_currency
					  + ", PayrollDate=" + p_PayrollDate 
					  + ", Client=" + p_AD_Client_ID 
					  + ", Org=" + p_AD_Org_ID);
					retValue=BigDecimal.valueOf(0.00);
		}
		return retValue;
		

	}	//	getSalary_MO

	/**
	 *	Get getSQLTaxUnit 
	 *  @param  p_PayrollDate    The Payroll End Date date - if null - use current date
	 * 	@param	p_currency
	 *  @param	p_conversiontype_id
	 * 	@param	p_AD_Client_ID client
	 * 	@param  p_AD_Org_ID	organization
	 *  @return TaxUnit Bigdecimal
	 */
	public static BigDecimal sqlGetTaxUnit (Timestamp p_PayrollDate, 
			int p_currency, int p_conversiontype_id, int p_AD_Client_ID, int p_AD_Org_ID)
	{
		/**	Logger						*/
		CLogger		log = CLogger.getCLogger (MAMN_Rates.class);
		BigDecimal multiplyFactor = BigDecimal.ZERO;

		//	p_PayrollDate Date
		if (p_PayrollDate == null)
			p_PayrollDate = new Timestamp (System.currentTimeMillis());
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		//	Get Rate
		String sql = "SELECT taxunit , C_Currency_ID, C_ConversionType_ID " + 
				"FROM AMN_Rates " + 
				"WHERE ? BETWEEN startdate AND enddate " + 
				"AND AD_Client_ID IN (0,?) " + 
				"AND AD_Org_ID IN (0,?) " + 
				"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC" ;
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setTimestamp(1, p_PayrollDate);
			pstmt.setInt(2, p_AD_Client_ID);
			pstmt.setInt(3, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			//log.warning("SQL UNIDADTRIBUTARIA:"+sql);
			while (rs.next()) {

				if (p_currency == rs.getInt(2)) {
					retValue = rs.getBigDecimal(1);
					//log.warning("SQL UNIDADTRIBUTARIA:"+retValue);
					break;
				} else {
					multiplyFactor = MConversionRate.getRate(rs.getInt(2), p_currency, p_PayrollDate, p_conversiontype_id, p_AD_Client_ID, p_AD_Org_ID);
					retValue = rs.getBigDecimal(1).multiply(multiplyFactor);
					//log.warning("SQL UNIDADTRIBUTARIA:"+retValue);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "sqlGetTaxUnit", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (retValue == null) {
			if (log.isLoggable(Level.INFO)) log.info ("sqlGetTaxUnit - not found - " 
			  + ", Currency=" + p_currency
			  + ", PayrollDate=" + p_PayrollDate 
			  + ", Client=" + p_AD_Client_ID 
			  + ", Org=" + p_AD_Org_ID);
			retValue=BigDecimal.valueOf(0.00);
		}
		return retValue;
	}	//	getSQLTaxUnit
}

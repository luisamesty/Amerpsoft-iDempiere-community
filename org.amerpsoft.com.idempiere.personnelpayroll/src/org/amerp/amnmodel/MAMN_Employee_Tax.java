package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMN_Employee_Tax extends X_AMN_Employee_Tax {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2101587633275285195L;

	
	/**	Cache		MAMN_Employee					*/
	private static CCache<Integer,MAMN_Employee_Tax> s_cache = new CCache<Integer,MAMN_Employee_Tax >(Table_Name, 10);

	static CLogger log = CLogger.getCLogger(MAMN_Employee_Tax.class);

	public MAMN_Employee_Tax(Properties ctx, int AMN_Employee_Tax_ID,
			String trxName) {
		super(ctx, AMN_Employee_Tax_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Employee_Tax(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get Employee Tax from Cache
	 * @param ctx context
	 * @param p_MAMN_Employee_ID
	 * @return Memployee
	 */
	public static MAMN_Employee_Tax get (Properties ctx, int p_MAMN_Employee_ID)
	{
		if (p_MAMN_Employee_ID <= 0)
			return null;
		//
		Integer key = new Integer(p_MAMN_Employee_ID);
		MAMN_Employee_Tax retValue = (MAMN_Employee_Tax) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Employee_Tax (ctx, p_MAMN_Employee_ID, null);
		if (retValue.getAMN_Employee_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get
	
	/**
	 *	Get getSQLEmployeeTaxRate
	 *  @param  p_PayrollDate    The Payroll End Date date - if null - use current date
	 * 	@param	p_AD_Client_ID client
	 * 	@param  p_AD_Org_ID	organization
	 *  @param  p_AMN_Employee_ID
	 *  @return p_currency Rate or null
	 *  
	 */
	public static BigDecimal getSQLEmployeeTaxRate (Timestamp p_PayrollDate, 
			int p_currency, int p_AD_Client_ID, int p_AD_Org_ID, int p_AMN_Employee_ID)
	{

		//log.warning("getSQLEmployeeTaxRate p_PayrollDate:"+p_PayrollDate+"  p_currency:"+p_currency+" "+p_AD_Client_ID+"  "+p_AD_Org_ID);
		//	p_PayrollDate Date
		if (p_PayrollDate == null)
			p_PayrollDate = new Timestamp (System.currentTimeMillis());
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		//	Get Employee Tax Rate Between Dates
		String sql = "SELECT taxrate " + 
				"FROM AMN_Employee_Tax " + 
				"WHERE C_Currency_ID=? " + 
				"AND AMN_Employee_ID=? " + 
				"AND ? BETWEEN validfrom AND validto " + 
				"AND AD_Client_ID IN (0,?) " + 
				"AND AD_Org_ID IN (0,?) " + 
				"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC" ;
		BigDecimal retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, p_currency);
			pstmt.setInt(2, p_AMN_Employee_ID);
			pstmt.setTimestamp(3, p_PayrollDate);
			pstmt.setInt(4, p_AD_Client_ID);
			pstmt.setInt(5, p_AD_Org_ID);
			//log.warning("SQL TAXRATE:"+sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retValue = rs.getBigDecimal(1);
				//log.warning("SQL TAXRATE retValue:"+retValue);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getSQLEmployeeTaxRate", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (retValue == null) {
			if (log.isLoggable(Level.INFO)) log.info ("getSQLEmployeeTaxRate - not found - " 
			  + ", Currency=" + p_currency
			  + ", PayrollDate=" + p_PayrollDate 
			  + ", Client=" + p_AD_Client_ID 
			  + ", Org=" + p_AD_Org_ID);
			retValue=BigDecimal.valueOf(0.00);
		}
		return retValue;
	}	//	getSQLEmployeeTaxRate

}

/**
 * 
 */
package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Payroll;
import org.compiere.model.MConversionRate;
import org.compiere.model.MConversionType;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * @author luisamesty
 *
 */
public class AmerpUtilities {


	static CLogger log = CLogger.getCLogger(AmerpUtilities.class);
	
	/**
	  Truncate a String to the given length with no warnings
	  @param  value String to be truncated
	  @param  length  Maximum length of string
	  @return Returns value if value is null or value.length() is less or equal to than length, otherwise a String representing
	    value truncated to length.
	*/
	public static String truncate(String value, int length)
	{
	  if (value != null && value.length() > length)
	    value = value.substring(0, length);
	  return value;
	}
	
	/**
	 * defaultOrgCurrency
	 * @param p_AD_Org_ID
	 * @return
	 */
	public static int defaultOrgCurrency(int p_AD_Org_ID) {
		int Currency_ID=0;
		String sql="";
    	// Determines AD_Org_ID
		sql = "SELECT count.c_currency_id as c_currency_id " + 
				"FROM " + 
				"ad_org as adorg " + 
				"LEFT JOIN ad_orginfo  adoin ON adorg.ad_org_id = adoin.ad_org_id " + 
				"LEFT JOIN c_location cloca ON cloca.c_location_id = adoin.c_location_id " + 
				"LEFT JOIN c_country count ON cloca.c_country_id = count.c_country_id " + 
				"WHERE " + 
				"adorg.ad_org_id=?" ;
		Currency_ID=DB.getSQLValue(null, sql, p_AD_Org_ID);
				
		return Currency_ID;
		
	}
	
	/**
	 * defaultAcctSchemaCurrency
	 * @param p_AD_Client_ID
	 * @return
	 */
	public static int defaultAcctSchemaCurrency(int p_AD_Client_ID) {
		int Currency_ID=0;
		String sql="";
    	// Determines Currency for AD_Org_ID
		sql = "SELECT C_Currency_ID " + 
				" FROM " + 
				" C_AcctSchema sch " + 
				" LEFT JOIN AD_ClientInfo cli ON cli.AD_Client_ID = sch.AD_Client_ID " + 
				" WHERE sch.C_AcctSchema_ID = cli.C_AcctSchema1_ID  " +
				" AND cli.AD_Client_ID = ?" ;
		Currency_ID=DB.getSQLValue(null, sql, p_AD_Client_ID);
		//log.warning("defaultAcctSchemaCurrency="+Currency_ID);	
		return Currency_ID;
	}

	/**
	 * 
	 * @param p_AMN_Contract_ID
	 * @return
	 */
	public static int defaultAMNContractCurrency(int p_AMN_Contract_ID) {
		
		int Currency_ID=0;
		String sql="";
    	// Determines AD_Org_ID
		sql = "SELECT C_Currency_ID " + 
				" FROM " + 
				" AMN_Contract  " + 
				" WHERE AMN_Contract_ID = ?" ;
		Currency_ID=DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		//log.warning("defaultAMNContractCurrency="+Currency_ID);
		if (Currency_ID > 0 ) {
			return Currency_ID;
		} else {
			MAMN_Contract amnc = new MAMN_Contract(Env.getCtx(),p_AMN_Contract_ID,null);
			Currency_ID= defaultAcctSchemaCurrency(amnc.getAD_Client_ID());
		}
		//log.warning("defaultAMNContractCurrency="+Currency_ID);
		return Currency_ID;	
	}
	
	/**
	 * defaultAMNContractConversionType
	 * @param p_AMN_Contract_ID
	 * @return
	 */
	public static int defaultAMNContractConversionType(int p_AMN_Contract_ID) {
		
		int ConversionType_ID = 0;
		String sql="";
    	// Determines AD_Org_ID
		sql = "SELECT C_ConversionType_ID " + 
				" FROM " + 
				" AMN_Contract  " + 
				" WHERE AMN_Contract_ID = ?" ;
		ConversionType_ID=DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		//log.warning("defaultAMNContractConversionType="+ConversionType_ID);
		if (ConversionType_ID > 0)
			return ConversionType_ID;
		else
			return MConversionType.TYPE_SPOT;
	}
	

}

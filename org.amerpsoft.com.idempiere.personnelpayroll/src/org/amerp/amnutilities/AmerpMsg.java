package org.amerp.amnutilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;

public class AmerpMsg {
	
	/**	Singleton						*/
	private static	AmerpMsg	s_msg = null;
	
	/**
	 * 	Get Message Object
	 *	@return Msg
	 */
	private static synchronized AmerpMsg get()
	{
		if (s_msg == null)
			s_msg = new AmerpMsg();
		return s_msg;
	}	//	get
	
	/**************************************************************************
	 *	Constructor
	 */
	private AmerpMsg()
	{
		
	}	//	Mag
	
	/**  The Map                    */
	private CCache<String,CCache<String,String>> m_languages 
		= new CCache<String,CCache<String,String>>(null, "msg_lang", 2, 0, false);
	
	private CCache<String,CCache<String,String>> m_elementCache 
		= new CCache<String,CCache<String,String>>(null, "msg_element", 2, 0, false);
	
	/**	Logger							*/
	private static CLogger			log = CLogger.getCLogger (AmerpMsg.class);
	
	private CCache<String,String> getElementMap (String ad_language)
	{
		String AD_Language = ad_language;
		if (AD_Language == null || AD_Language.length() == 0)
			AD_Language = Language.getBaseAD_Language();
		//  Do we have the language ?
		CCache<String,String> retValue = (CCache<String,String>)m_elementCache.get(AD_Language);
		if (retValue != null && retValue.size() > 0)
			return retValue;

		retValue = new CCache<String, String>("element", 100);
		m_elementCache.put(AD_Language, retValue);
		return retValue;
	}
	/**************************************************************************
	 *	getParameterMsg Get translated text for AD_Process_Para
	 *  @param  Properties ctx - for Language
	 *  @param	AD_Process_Para - Message Key
	 *  @return translated text
	 */
	public static String getParameterMsg (Properties ctx, String AD_Process_Para)
	{
		// AD_Process_Para_ID for Name=AD_Process_Para
		int AD_Process_Para_ID = sqlGetADProcessParaIDFromName(AD_Process_Para);
		String retStr="";
		String AD_Language = Env.getAD_Language(ctx); 
		// If not Found() Returns Same String
		if (AD_Process_Para_ID == 0) {
			retStr= AD_Process_Para.trim();
		} else {
			if (AD_Language == null || AD_Language.length() == 0)
				AD_Language = Language.getBaseAD_Language();
			//
			retStr = sqlGetADProcessParaTRL(AD_Process_Para_ID, AD_Language);
			//
			if (retStr == null || retStr.length() == 0)
			{
				log.warning("NOT found Parameter Trl: " + AD_Process_Para);
				retStr = AD_Process_Para;
			}
		}
		//log.warning("AD_Process_Para_ID."+AD_Process_Para_ID+"  AD_Process_Para:"+AD_Process_Para+"  AD_Language:"+AD_Language);
		return retStr;
	}	//	getMsg

	
	/**
	 * sqlGetADProcessParaIDFromName (String p_AD_Process_Para)
	 * @param String p_AD_Process_Para
	 * return:int AD_Process_Para_ID
	 */
	public static Integer sqlGetADProcessParaIDFromName (String p_AD_Process_Para)
	
	{
		String sql;
		int AD_Process_Para_ID=0;
		// AD_Process_Para
    	sql = "select ad_process_para_id from ad_process_para " +
    			"WHERE name='"+p_AD_Process_Para+"'";
    	AD_Process_Para_ID = DB.getSQLValue(null, sql);	
    	//log.warning(p_AD_Process_Para+":"+AD_Process_Para_ID);
		return AD_Process_Para_ID;	
	}
	
	/**
	 * sqlGetADProcessParaTRL (int p_AD_Process_Para_ID, String p_AD_Language)
	 * @param int p_AD_Process_Para_ID
	 * @param String p_AD_Language
	 * return:String AD_Process_Para Translation 
	 */
	public static String sqlGetADProcessParaTRL (int p_AD_Process_Para_ID,
			String p_AD_Language)
	
	{
		String sql;
		String retValue="";
		String AD_Process_Para_Trl="";
		// AD_Process_Para
    	sql = "select name from adempiere.ad_process_para_trl "+
    			"where ad_process_para_id = ? and ad_language='"+p_AD_Language+"' " 
			;
    	AD_Process_Para_Trl=DB.getSQLValueString(null, sql, p_AD_Process_Para_ID);
    	if (AD_Process_Para_Trl == null || AD_Process_Para_Trl.length() == 0)
    		retValue="";
    	else 
    		retValue=AD_Process_Para_Trl;
    	return retValue;
    	
	}
	
	/**************************************************************************
	 *  Get Translation for Element Print Text
	 *  @param ad_language language
	 *  @param ColumnName column name
	 *  @param isSOTrx if false PO terminology is used (if exists)
	 *  @return Name of the Column or "" if not found
	 */
	public static String getElementPrintText (String ad_language, String ColumnName, boolean isSOTrx)
	{
		if (ColumnName == null || ColumnName.equals(""))
			return "";
		String AD_Language = ad_language;
		if (AD_Language == null || AD_Language.length() == 0)
			AD_Language = Language.getBaseAD_Language();
		
		AmerpMsg msg = get();
		CCache<String, String> cache = msg.getElementMap(AD_Language);
		String key = ColumnName+"|"+isSOTrx;
		String retStr = cache.get(key);
		if (retStr != null)
			return retStr;

		//	Check AD_Element
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			if (AD_Language == null || AD_Language.length() == 0 || Env.isBaseLanguage(AD_Language, "AD_Element"))
				pstmt = DB.prepareStatement("SELECT PrintName, PO_PrintName FROM AD_Element WHERE UPPER(ColumnName)=?", null);
			else
			{
				pstmt = DB.prepareStatement("SELECT t.PrintName, t.PO_PrintName FROM AD_Element_Trl t, AD_Element e "
					+ "WHERE t.AD_Element_ID=e.AD_Element_ID AND UPPER(e.ColumnName)=? "
					+ "AND t.AD_Language=?", null);
				pstmt.setString(2, AD_Language);
			}

			pstmt.setString(1, ColumnName.toUpperCase());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retStr = rs.getString(1);
				if (!isSOTrx)
				{
					String temp = rs.getString(2);
					if (temp != null && temp.length() > 0)
						retStr = temp;
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "getElementPrintText", e);
			return "";
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		retStr = retStr == null ? "" : retStr.trim();
		cache.put(key, retStr);
		return retStr;
		
	}   //  getElement

	/**
	 *  Get Translation for Element Print Text using Sales terminology
	 *  @param ctx context
	 *  @param ColumnName column name
	 *  @return Name of the Column or "" if not found
	 */
	public static String getElementPrintText (Properties ctx, String ColumnName)
	{
		return getElementPrintText (Env.getAD_Language(ctx), ColumnName, true);
	}   //  getElement

	/**
	 *  Get Translation for Element Print Text
	 *  @param ctx context
	 *  @param ColumnName column name
	 *  @param isSOTrx sales transaction
	 *  @return Name of the Column or "" if not found
	 */
	public static String getElementPrintText (Properties ctx, String ColumnName, boolean isSOTrx)
	{
		return getElementPrintText (Env.getAD_Language(ctx), ColumnName, isSOTrx);
	}   //  getElement


}

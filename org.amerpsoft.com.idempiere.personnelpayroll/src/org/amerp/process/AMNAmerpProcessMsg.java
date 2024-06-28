package org.amerp.process;

import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;

public class AMNAmerpProcessMsg {

	/**	Logger							*/
	private static CLogger			log = CLogger.getCLogger (AMNAmerpProcessMsg.class);
	
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

	/**
	 * sqlGetADProcessTRL (int p_AD_Process_ID, String p_AD_Language)
	 * @param int p_AD_Process_ID
	 * @param String p_AD_Language
	 * return:String AD_Process Translation 
	 */
	public static String sqlGetADProcessTRL (int p_AD_Process_ID,
			String p_AD_Language)
	
	{
		String sql;
		String retValue="";
		String AD_Process_Trl="";
		// AD_Process_Para
    	sql = "select name from adempiere.ad_process_trl "+
    			"where ad_process_id = ? and ad_language='"+p_AD_Language+"' " 
			;
    	AD_Process_Trl=DB.getSQLValueString(null, sql, p_AD_Process_ID);
    	if (AD_Process_Trl == null || AD_Process_Trl.length() == 0)
    		retValue="";
    	else 
    		retValue=AD_Process_Trl;
    	return retValue;
    	
	}
}

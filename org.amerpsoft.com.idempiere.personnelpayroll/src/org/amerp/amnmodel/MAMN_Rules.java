package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.script.ScriptEngine;

import org.adempiere.base.Core;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Util;

public class MAMN_Rules extends X_AMN_Rules{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6410376785277725218L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Rules.class);

	//global or login context variable prefix
	public final static String GLOBAL_CONTEXT_PREFIX = "G_";
	//window context variable prefix
	public final static String WINDOW_CONTEXT_PREFIX = "W_";
	//method call arguments prefix
	public final static String ARGUMENTS_PREFIX = "A_";
	//process parameters prefix
	public final static String PARAMETERS_PREFIX = "P_";
	
	public static final String SCRIPT_PREFIX = "@script:";
	
	public MAMN_Rules(Properties ctx, int AMN_Rules_ID, String trxName) {
		super(ctx, AMN_Rules_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Rules(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Get Rule from Cache
	 *	@param ctx context
	 *	@param AMN_Rules_ID id
	 *	@return MAMN_Rules
	 */
	public static MAMN_Rules get (Properties ctx, int AMN_Rules_ID)
	{
		Integer key = new Integer (AMN_Rules_ID);
		MAMN_Rules retValue = (MAMN_Rules) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MAMN_Rules (ctx, AMN_Rules_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get Rule from Cache
	 *	@param ctx context
	 *	@param ruleValue case sensitive rule Value
	 *	@return MAMN_Rules
	 */
	public static MAMN_Rules get (Properties ctx, String ruleValue, int AD_Client_ID)
	{
		Integer AMN_Rules_ID = 0;
		if (ruleValue == null)
			return null;
		//
	  	String sql = "SELECT DISTINCT AMN_Rules_ID "+
	  			"FROM AMN_Rules  " +
	  			"WHERE Value='"+ruleValue.trim() +"' "+
	  			" AND AD_Client_ID="+ AD_Client_ID ;
	  	//log.warning("ruleValue="+ruleValue+"  sql="+sql);
	  	PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try
	    {
	            pstmt = DB.prepareStatement(sql, null);
		            rs = pstmt.executeQuery();
	            if (rs.next())
	            {
	            	AMN_Rules_ID =rs.getInt(1);
	            }
	    }
	    catch (SQLException e)
	    {
	    	AMN_Rules_ID = 0 ;
	    }
	    finally
	    {
	            DB.close(rs, pstmt);
	            rs = null; pstmt = null;
	    }
	    if (AMN_Rules_ID == null)
	    	return null;
	    MAMN_Rules retValue = new MAMN_Rules(ctx, AMN_Rules_ID, null);
		if (retValue != null)
		{
			Integer key = new Integer (retValue.getAMN_Rules_ID());
			s_cache.put (key, retValue);
		}
		return retValue;
	}	//	get
	
	/**
	 * 	Get Model Validation Login Rules
	 *	@param ctx context
	 *	@return Rule
	 */
	public static List<MAMN_Rules> getModelValidatorLoginRules (Properties ctx)
	{
		final String whereClause = "EventType=?";
		List<MAMN_Rules> rules = new Query(ctx,I_AMN_Rules.Table_Name,whereClause,null)
		.setParameters(EVENTTYPE_ModelValidatorLoginEvent)
		.setOnlyActiveRecords(true)
		.list();
		if (rules != null && rules.size() > 0)
			return rules;
		else
			return null;
	}	//	getModelValidatorLoginRules

	/**	Cache						*/
	private static CCache<Integer,MAMN_Rules> s_cache = new CCache<Integer,MAMN_Rules>(Table_Name, 20);
	

	/* The Engine */
	ScriptEngine engine = null;

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		// Validate format for scripts
		// must be engine:name
		// where engine can be groovy, jython or beanshell
		if (getRuleType().equals(RULETYPE_JSR223ScriptingAPIs)) {
			String engineName = getEngineName();
			if (engineName == null || 
					(!   (engineName.equalsIgnoreCase("groovy")
							|| engineName.equalsIgnoreCase("jython") 
							|| engineName.equalsIgnoreCase("beanshell")))) {
				log.saveError("Error", Msg.getMsg(getCtx(), "WrongScriptValue"));
				return false;
			}
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder ("MAMN_Rule[");
		sb.append (get_ID()).append ("-").append (getValue()).append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Script Engine for this rule
	 *	@return ScriptEngine
	 */
	public ScriptEngine getScriptEngine() {
		
		String engineName = getEngineName();
		
		if(engineName != null)
			engine = Core.getScriptEngine(engineName);
		
		return engine;
	}

	public String getEngineName() {
		int colonPosition = getValue().indexOf(":");
		if (colonPosition < 0)
			return null;
		return getValue().substring(0, colonPosition);
	}
	
	/**************************************************************************
	 *	Set Context ctx to the engine based on windowNo
	 *  @param engine ScriptEngine
	 *  @param ctx context
	 *  @param windowNo window number
	 */
	public static void setContext(ScriptEngine engine, Properties ctx, int windowNo) {
		Enumeration<Object> en = ctx.keys();
		while (en.hasMoreElements())
		{
			String key = en.nextElement().toString();
			//  filter
			if (key == null || key.length() == 0
					|| key.startsWith("P")              //  Preferences
					|| (key.indexOf('|') != -1 && !key.startsWith(String.valueOf(windowNo)))    //  other Window Settings
					|| (key.indexOf('|') != -1 && key.indexOf('|') != key.lastIndexOf('|')) //other tab
			)
				continue;
			Object value = ctx.get(key);
			if (value != null) {
				if (value instanceof Boolean)
					engine.put(convertKey(key, windowNo), ((Boolean)value).booleanValue());
				else if (value instanceof Integer)
					engine.put(convertKey(key, windowNo), ((Integer)value).intValue());
				else if (value instanceof Double)
					engine.put(convertKey(key, windowNo), ((Double)value).doubleValue());
				else
					engine.put(convertKey(key, windowNo), value);
			}
		}
	}

	/**
	 *  Convert Key
	 *  # -> _
	 *  @param key
	 * @param m_windowNo 
	 *  @return converted key
	 */
	public static String convertKey (String key, int m_windowNo)
	{
		String k = m_windowNo + "|";
		if (key.startsWith(k))
		{
			String retValue = WINDOW_CONTEXT_PREFIX + key.substring(k.length());
			retValue = Util.replace(retValue, "|", "_");
			return retValue;
		}
		else
		{
			String retValue = null;
			if (key.startsWith("#"))
				retValue = GLOBAL_CONTEXT_PREFIX + key.substring(1);
			else
				retValue = key;
			retValue = Util.replace(retValue, "#", "_");
			return retValue;
		}
	}   //  convertKey
}

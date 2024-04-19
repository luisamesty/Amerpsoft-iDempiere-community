/**
 * 
 */
package org.amerp.amxeditor.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import org.compiere.process.DocAction;
import org.compiere.util.*;
import org.amerp.amxeditor.model.MParish;

/**
 * @author luisamesty
 *
 */
public class MParish extends X_C_Parish {


	/**
	 * 
	 */
    private static final long serialVersionUID = 405155106507714662L;
    
	/**	Parish Cache				*/
	private static CCache<String,MParish> s_Parishs = null;
	/** Default Parish				*/
	private static MParish		s_default = null;
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MParish.class);
	
	/**
	 * 	Load Parishs (cached)
	 *	@param ctx context
	 */
	private static void loadAllParishs (Properties ctx)
	{
		s_Parishs = new CCache<String,MParish>("C_Parish", 500);
		String sql = "SELECT * FROM C_Parish WHERE IsActive='Y'";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				MParish r = new MParish (ctx, rs, null);
				s_Parishs.put(String.valueOf(r.getC_Parish_ID()), r);
				if (r.isDefault())
					s_default = r;
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		s_log.fine(s_Parishs.size() + " - default=" + s_default);
	}	//	loadAllParishs

	/**
	 * 	Get Country (cached)
	 * 	@param ctx context
	 *	@param C_Parish_ID ID
	 *	@return Country
	 */
	public static MParish get (Properties ctx, int C_Parish_ID)
	{
		if (s_Parishs == null || s_Parishs.size() == 0)
			loadAllParishs(ctx);
		String key = String.valueOf(C_Parish_ID);
		MParish r = (MParish)s_Parishs.get(key);
		if (r != null)
			return r;
		r = new MParish (ctx, C_Parish_ID, null);
		if (r.getC_Parish_ID() == C_Parish_ID)
		{
			s_Parishs.put(key, r);
			return r;
		}
		return null;
	}	//	get

	/**
	 * 	Get Default Parish
	 * 	@param ctx context
	 *	@return Parish or null
	 */
	public static MParish getDefault (Properties ctx)
	{
		if (s_Parishs == null || s_Parishs.size() == 0)
			loadAllParishs(ctx);
		return s_default;
	}	//	get

	/**
	 *	Return Parishs as Array
	 * 	@param ctx context
	 *  @return MCountry Array
	 */
	//@SuppressWarnings("unchecked")
	public static MParish[] getParishs(Properties ctx)
	{
		if (s_Parishs == null || s_Parishs.size() == 0)
			loadAllParishs(ctx);
		MParish[] retValue = new MParish[s_Parishs.size()];
		s_Parishs.values().toArray(retValue);
		Arrays.sort(retValue, new MParish(ctx, 0, null));
		return retValue;
	}	//	getParishs


	/**
	 *	Return Array of Parishs of C_Municipality_ID and C_Region_ID
	 * @param ctx
	 * @param C_Municipality_ID
	 * @param C_Region_ID
	 * @return
	 */
	//@SuppressWarnings("unchecked")
	public static MParish[] getParishs (Properties ctx, int C_Municipality_ID, int C_Region_ID)
	{
		if (s_Parishs == null || s_Parishs.size() == 0)
			loadAllParishs(ctx);
		ArrayList<MParish> list = new ArrayList<MParish>();
		Iterator it = s_Parishs.values().iterator();
		while (it.hasNext())
		{
			MParish r = (MParish)it.next();
			if (r.getC_Municipality_ID() == C_Municipality_ID && r.getC_Region_ID()==C_Region_ID)
				list.add(r);
		}
		//  Sort it
		MParish[] retValue = new MParish[list.size()];
		list.toArray(retValue);
		Arrays.sort(retValue, new MParish(ctx, 0, null));
		return retValue;
	}	//	getParishs


	/**
	 * Return Array of Parishs of C_Municipality_ID and C_Region_ID by SQL
	 * @param ctx
	 * @param C_Municipality_ID
	 * @param C_Region_ID
	 * @return
	 */
	//@SuppressWarnings("unchecked")
	public static MParish[] getSQLParishs (Properties ctx, int C_Municipality_ID, int C_Region_ID)
	{
		if (s_Parishs == null || s_Parishs.size() == 0)
			loadAllParishs(ctx);
		ArrayList<MParish> list = new ArrayList<MParish>();
		//
		String sql = "SELECT * FROM C_Parish "+
				" WHERE IsActive='Y' "+
				" AND C_Municipality_ID="+C_Municipality_ID +
				" AND C_Region_ID="+C_Region_ID;
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				MParish r = new MParish (ctx, rs, null);
				if (r.getC_Municipality_ID() == C_Municipality_ID && r.getC_Region_ID()==C_Region_ID)
					list.add(r);
			}
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		
		//  Sort it
		MParish[] retValue = new MParish[list.size()];
		list.toArray(retValue);
		Arrays.sort(retValue, new MParish(ctx, 0, null));
		return retValue;
	}	//	getSQLParishs
	
	/**
	 * @param p_ctx
	 * @param C_Parish_ID
	 * @param p_trxName
	 */
    public MParish(Properties p_ctx, int C_Parish_ID, String p_trxName) {
	    super(p_ctx, C_Parish_ID, p_trxName);
	    
    }
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MParish(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    
    }

}

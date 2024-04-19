/**
 * 
 */
package org.amerp.amxeditor.model;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import org.compiere.model.MRegion;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */
public class MRegionExt extends MRegion implements I_C_Region_Amerp {

	private String COLUMNNAME_C_Community_ID = I_C_Region_Amerp.COLUMNNAME_C_Community_ID;
	
	/**
	 * 
	 */
    private static final long serialVersionUID = 2538079694040142922L;


	/**
	 * 	Load Regions (cached)
	 *	@param ctx context
	 */
	private static void loadAllRegions (Properties ctx)
	{
		s_regions = new CCache<String,MRegionExt>(Table_Name, 100);
		String sql = "SELECT * FROM C_Region WHERE IsActive='Y'";
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			stmt = DB.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				MRegionExt r = new MRegionExt (ctx, rs, null);
				s_regions.put(String.valueOf(r.getC_Region_ID()), r);
				if (r.isDefault())
					s_default = r;
			}
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null;
			stmt = null;
		}
		if (s_log.isLoggable(Level.FINE)) s_log.fine(s_regions.size() + " - default=" + s_default);
	}	//	loadAllRegions

	/**
	 * 	Get Country (cached)
	 * 	@param ctx context
	 *	@param C_Region_ID ID
	 *	@return Country
	 */
	public static MRegionExt get (Properties ctx, int C_Region_ID)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		String key = String.valueOf(C_Region_ID);
		MRegionExt r = (MRegionExt)s_regions.get(key);
	
		if (r != null)
			return r;
		r = new MRegionExt (ctx, C_Region_ID, null);
		if (r.getC_Region_ID() == C_Region_ID)
		{
			s_regions.put(key, r);
			return r;
		}
		return null;
	}	//	get

	/**
	 * 	Get Default Region
	 * 	@param ctx context
	 *	@return Region or null
	 */
	public static MRegion getDefault (Properties ctx)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		return s_default;
	}	//	get

	/**
	 *	Return Regions as Array
	 * 	@param ctx context
	 *  @return MCountryExt Array
	 */
	public static MRegionExt[] getRegions(Properties ctx)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		MRegionExt[] retValue = new MRegionExt[s_regions.size()];
		s_regions.values().toArray(retValue);
		Arrays.sort(retValue, new MRegionExt(ctx, 0, null));
		return retValue;
	}	//	getRegions

	/**
	 *	Return Array of Regions of Country
	 * 	@param ctx context
	 *  @param C_Country_ID country
	 *  @return MRegionExt Array
	 */
	public static MRegionExt[] getRegions (Properties ctx, int C_Country_ID)
	{
		if (s_regions == null || s_regions.size() == 0)
			loadAllRegions(ctx);
		ArrayList<MRegionExt> list = new ArrayList<MRegionExt>();
		Iterator<MRegionExt> it = s_regions.values().iterator();
		while (it.hasNext())
		{
			MRegionExt r = it.next();
			if (r.getC_Country_ID() == C_Country_ID)
				list.add(r);
		}
		//  Sort it
		MRegionExt[] retValue = new MRegionExt[list.size()];
		list.toArray(retValue);
		Arrays.sort(retValue, new MRegionExt(ctx, 0, null));
		return retValue;
	}	//	getRegions

	/**	Region Cache				*/
	private static CCache<String,MRegionExt> s_regions = null;
	/** Default Region				*/
	private static MRegionExt		s_default = null;
	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MRegionExt.class);

	/**
	 * @param p_ctx
	 * @param C_Region_ID
	 * @param p_trxName
	 */
    public MRegionExt(Properties p_ctx, int C_Region_ID, String p_trxName) {
	    super(p_ctx, C_Region_ID, p_trxName);
	    // 
    }
    
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MRegionExt(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);

    }

	@Override
	public void setC_Community_ID(int C_Community_ID) {
		
		set_Value (COLUMNNAME_C_Community_ID,C_Community_ID);
	}

	@Override
	public int getC_Community_ID() {

		Object oo = get_Value(COLUMNNAME_C_Community_ID);
		if (oo != null) 
		{
			return (int) (oo); 
		}
		return 0;
	}

}

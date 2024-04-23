/**
 * 
 */

package org.amerp.amxeditor.model;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MCountry;
import org.compiere.model.MLocation;
import org.compiere.model.MRegion;
import org.compiere.model.MSysConfig;
import org.compiere.process.DocAction;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.amerp.amxeditor.model.MCountryExt;
import org.amerp.amxeditor.model.MRegionExt;

/**
 *	Location (Address)
 *	
 *  @author Jorg Janke
 *  @version $Id: MLocation.java,v 1.3 2006/07/30 00:54:54 jjanke Exp $
 *  
 *  @author Michael Judd (Akuna Ltd)
 * 				<li>BF [ 2695078 ] Country is not translated on invoice
 * 				<li>FR [2794312 ] Location AutoComplete - check if allow cities out of list
 * 
 * @author Teo Sarca, teo.sarca@gmail.com
 * 		<li>BF [ 3002736 ] MLocation.get cache all MLocations
 * 			https://sourceforge.net/tracker/?func=detail&aid=3002736&group_id=176962&atid=879332
 */
public class MLocationExt extends MLocation implements I_C_Location_Amerp, DocAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8332515185354248079L;

	// http://jira.idempiere.com/browse/IDEMPIERE-147
	public static String LOCATION_MAPS_URL_PREFIX     = MSysConfig.getValue("LOCATION_MAPS_URL_PREFIX");
	public static String LOCATION_MAPS_ROUTE_PREFIX   = MSysConfig.getValue("LOCATION_MAPS_ROUTE_PREFIX");
	public static String LOCATION_MAPS_SOURCE_ADDRESS      = MSysConfig.getValue("LOCATION_MAPS_SOURCE_ADDRESS");
	public static String LOCATION_MAPS_DESTINATION_ADDRESS = MSysConfig.getValue("LOCATION_MAPS_DESTINATION_ADDRESS");
	public static final String COLUMNNAME_C_Municipality_ID = "C_Municipality_ID";
	//public static final String COLUMNNAME_MunicipalityName = "municipalityname";
	public static final String COLUMNNAME_C_Parish_ID = "C_Parish_ID";
	//public static final String COLUMNNAME_ParishName = "parishname";
	
	static private 	MCountryExt		m_c = null;
	private 	MRegionExt		m_r = null;
	private 	MMunicipality	m_m = null;
	private 	MParish			m_p = null;	
	
	
	/**
	 * 	Get Location from Cache
	 *	@param ctx context
	 *	@param C_Location_ID id
	 *	@param trxName transaction
	 *	@return MLocation
	 */
	public static MLocationExt get (Properties ctx, int C_Location_ID, String trxName)
	{
		//	New
		if (C_Location_ID == 0)
			return new MLocationExt(ctx, C_Location_ID, trxName);
		//
		Integer key = Integer.valueOf(C_Location_ID);
		MLocationExt retValue = null;
		if (trxName == null)
			retValue = (MLocationExt) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MLocationExt (ctx, C_Location_ID, trxName);
		if (retValue.get_ID () != 0)		//	found
		{
			if (trxName == null)
				s_cache.put (key, retValue);
			return retValue;
		}
		return null;					//	not found
	}	//	get

	/**
	 *	Load Location with ID if Business Partner Location
	 *	@param ctx context
	 *  @param C_BPartner_Location_ID Business Partner Location
	 *	@param trxName transaction
	 *  @return location or null
	 */
	public static MLocationExt getBPLocation (Properties ctx, int C_BPartner_Location_ID, String trxName)
	{
		if (C_BPartner_Location_ID == 0)					//	load default
			return null;

		MLocationExt loc = null;
		String sql = "SELECT * FROM C_Location l "
			+ "WHERE C_Location_ID IN (SELECT C_Location_ID FROM C_BPartner_Location WHERE C_BPartner_Location_ID=?)";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, C_BPartner_Location_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				loc = new MLocationExt (ctx, rs, trxName);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql + " - " + C_BPartner_Location_ID, e);
			loc = null;
		}
		return loc;
	}	//	getBPLocation

	/**	Cache						*/
	private static CCache<Integer,MLocationExt> s_cache = new CCache<Integer,MLocationExt>("C_Location", 100, 30);
	/**	Static Logger				*/
	private static CLogger	s_log = CLogger.getCLogger(MLocationExt.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Location_ID id
	 *	@param trxName transaction
	 */
	public MLocationExt (Properties ctx, int C_Location_ID, String trxName)
	{
		super (ctx, C_Location_ID, trxName);
		if (C_Location_ID == 0)
		{
			MCountry defaultCountry = MCountry.getDefault(getCtx()); 
			setCountry(defaultCountry);
			MRegion defaultRegion = MRegion.getDefault(getCtx());
			if (defaultRegion != null 
				&& defaultRegion.getC_Country_ID() == defaultCountry.getC_Country_ID())
				setRegion(defaultRegion);
		}
	}	//	MLocation

	/**
	 * 	Parent Constructor
	 *	@param country mandatory country
	 *	@param region optional region
	 */
	public MLocationExt (MCountryExt country, MRegionExt region)
	{
		super (country.getCtx(), 0, country.get_TrxName());
		setCountry (country);
		setRegion (region);
	}	//	MLocation

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param C_Country_ID country
	 *	@param C_Region_ID region
	 *	@param city city
	 *	@param trxName transaction
	 */
	public MLocationExt (Properties ctx, int C_Country_ID, int C_Region_ID, String city, String trxName)
	{
		super(ctx, 0, trxName);
		setC_Country_ID(C_Country_ID);
		setC_Region_ID(C_Region_ID);
		setCity(city);
	}	//	MLocation

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MLocationExt (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MLocation

	/**
	 * 	Set Country
	 *	@param country
	 */
	public void setCountry (MCountryExt country)
	{
		if (country != null)
			m_c = country;
		else
			m_c = MCountryExt.getDefault(getCtx());
		super.setC_Country_ID (m_c.getC_Country_ID());
	}	//	setCountry
	/**
	 * 	Set C_Country_ID
	 *	@param C_Country_ID id
	 */
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID >= 0)
			setCountry (MCountryExt.get(getCtx(), C_Country_ID));
	}	//	setCountry
	/**
	 * 	Get Country
	 *	@return country
	 */	
	public MCountryExt getCountryExt()
	{
		// Reset country if not match
		if (m_c != null && m_c.get_ID() != getC_Country_ID())
			m_c = null;
		// Load
		if (m_c == null)
		{
			if (getC_Country_ID() != 0)
				m_c = MCountryExt.get(getCtx(), getC_Country_ID());
			else
				m_c = MCountryExt.getDefault(getCtx());
		}
		return m_c;
	}	//	getCountry
	/**
	 * 	Get Country Name
	 *	@return	Country Name
	 */
	public String getCountryName()
	{
		return getCountry().getName();
	}	//	getCountryName
	/**
	 * 	Get Country Line
	 * 	@param local if true only foreign country is returned
	 * 	@return country or null
	 */
	/*public String getCountry (boolean local)
	{
		if (local 
			&& getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID())
			return null;
		return getCountryName();
	}	//	getCountry
	/**
	 * 	Get Country Line
	 * 	@param local if true only foreign country is returned
	 * 	@return country or null
	 */
	/*public String getCountry (boolean local, String language)
	{
		if (local 
			&& getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID())
			return null;
		MCountry mc = getCountry();
		return mc.getTrlName(language);
	
	}	//	getCountry
	
	/**
	 * 	Set Region
	 *	@param region
	 */
	//	Region
	/**
	 * setRegion
	 * @param region
	 */
	public void setRegion (MRegionExt region)
	{
		m_r = region;
		if (region == null)
		{
			super.setC_Region_ID(0);
		}
		else
		{
			super.setC_Region_ID(m_r.getC_Region_ID());
			setRegionName(m_r.getName());
			if (m_r.getC_Country_ID() != getC_Country_ID())
			{
				log.info("Region(" + region + ") C_Country_ID=" + region.getC_Country_ID()
						+ " - From  C_Country_ID=" + getC_Country_ID());
				setC_Country_ID(region.getC_Country_ID());
			}
		}
	} // setRegion
	
	/**
	 * 	Set C_Region_ID
	 *	@param C_Region_ID region
	 */
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID == 0)
			setRegion(null);
		//	Country defined
		else if (getC_Country_ID() != 0)
		{
			MCountry cc = getCountry();
			if (cc.isValidRegion(C_Region_ID))
				super.setC_Region_ID(C_Region_ID);
			else
				setRegion(null);
		}
		else
			setRegion (MRegion.get(C_Region_ID));
	}	//	setC_Region_ID
	/**
	 * 	Get Region
	 *	@return region
	 */
	public MRegionExt getRegionExt()
	{
		// Reset region if not match
		if (m_r != null && m_r.get_ID() != getC_Region_ID())
			m_r = null;
		//
		if (m_r == null && getC_Region_ID() != 0)
			m_r = MRegionExt.get(getCtx(), getC_Region_ID());
		return m_r;
	}	//	getRegion
	//	Municipality
	/**
	 * setMunicipality
	 * @param municipality
	 */
	public void setMunicipality (MMunicipality municipality)
	{
		m_m = municipality;
		if (municipality == null)
		{
			this.setC_Municipality_ID(0);
			//set_Value (COLUMNNAME_C_Municipality_ID, null);
		}
		else
		{
			this.setC_Municipality_ID(m_m.getC_Municipality_ID());
			setMunicipalityName(m_m.getName());
			if (m_m.getC_Country_ID() != getC_Country_ID())
			{
				log.info("Municipality(" + municipality + ") C_Country_ID=" + municipality.getC_Country_ID()
						+ " - From  C_Country_ID=" + getC_Country_ID());
				setC_Country_ID(municipality.getC_Country_ID());
			}
		}
	}	
	/**
	 * setMunicipalityName
	 * @param MunicipalityName
	 */
	public void setMunicipalityName (String MunicipalityName)
	{
		//set_Value (COLUMNNAME_MunicipalityName, MunicipalityName);
		return;
	}
	/**
	 * setC_Municipality_ID
	 * @param C_Municipality_ID
	 */
	public void setC_Municipality_ID(int C_Municipality_ID) {
		if (C_Municipality_ID < 1) 
			set_Value (COLUMNNAME_C_Municipality_ID, null);
		else 
			set_Value (COLUMNNAME_C_Municipality_ID, Integer.valueOf(C_Municipality_ID));
	}	
	// Parish
	/**
	 * 	setParish
	 *  @param parish
	 */
	public void setParish (MParish parish)
	{
		m_p = parish;
		if (parish == null)
		{
			this.setC_Parish_ID(0);
		}
		else
		{
			this.setC_Parish_ID(m_p.getC_Parish_ID());
			setParishName(m_p.getName());
			if (m_p.getC_Country_ID() != getC_Country_ID())
			{
				log.info("Parish(" + parish + ") C_Country_ID=" + parish.getC_Country_ID()
						+ " - From  C_Country_ID=" + getC_Country_ID());
				setC_Country_ID(parish.getC_Country_ID());
			}
		}
	}	//	setParish
	/**
	 * 	setC_Parish_ID
	 *  @param C_Parish_ID
	 */
	public void setC_Parish_ID(int C_Parish_ID) {
		if (C_Parish_ID < 1) 
			set_Value (COLUMNNAME_C_Parish_ID, null);
		else 
			set_Value (COLUMNNAME_C_Parish_ID, Integer.valueOf(C_Parish_ID));
		
	}
	/**
	 * 	setParishName
	 * @param ParishName
	 */
	public void setParishName (String ParishName)
	{
		//set_Value (COLUMNNAME_ParishName, ParishName);
		return;
	}



	/**
	 * 	Get getMunicipality
	 *	@return MMunicipality
	 */
	public MMunicipality getMunicipality()
	{
		// Reset municipality if not match
		if (m_m != null && m_m.get_ID() != getC_Municipality_ID())
			m_m = null;
		//
		if (m_m == null && getC_Municipality_ID() != 0)
			m_m = MMunicipality.get(getCtx(), getC_Municipality_ID());
		return m_m;
	}	
	/**
	 * 	Get getC_Municipality_ID
	 *	@return C_Municipality_ID
	 */
	public int getC_Municipality_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Municipality_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
	/**
	 * 	Get getParish
	 *	@return MParish
	 */
	public MParish getParish()
	{
		// Reset parish if not match
		if (m_p != null && m_p.get_ID() != getC_Parish_ID())
			m_p = null;
		//
		if (m_p == null && getC_Parish_ID() != 0)
			m_p = MParish.get(getCtx(), getC_Parish_ID());
		return m_p;
	}	
	/**
	 * 	Get getC_Parish_ID
	 *	@return C_Parish_ID
	 */
	public int getC_Parish_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Parish_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/**
	 * 	Get (local) Region Name
	 *	@return	region Name or ""
	 */

	@Override
	public void setDocStatus(String newStatus) {
		
		
	}

	@Override
	public String getDocStatus() {
		
		return null;
	}

	@Override
	public boolean processIt(String action) throws Exception {
		
		return false;
	}

	@Override
	public boolean unlockIt() {
		
		return false;
	}

	@Override
	public boolean invalidateIt() {
		
		return false;
	}

	@Override
	public String prepareIt() {
		
		return null;
	}

	@Override
	public boolean approveIt() {
		
		return false;
	}

	@Override
	public boolean rejectIt() {
		
		return false;
	}

	@Override
	public String completeIt() {
		
		return null;
	}

	@Override
	public boolean voidIt() {
		
		return false;
	}

	@Override
	public boolean closeIt() {
		
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		
		return false;
	}

	@Override
	public boolean reActivateIt() {
		
		return false;
	}

	@Override
	public String getSummary() {
		
		return null;
	}

	@Override
	public String getDocumentNo() {
		
		return null;
	}

	@Override
	public String getDocumentInfo() {
		
		return null;
	}

	@Override
	public File createPDF() {
		
		return null;
	}

	@Override
	public String getProcessMsg() {
		
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		
		return null;
	}

	@Override
	public String getDocAction() {
		
		return null;
	}
}	//	MLocation

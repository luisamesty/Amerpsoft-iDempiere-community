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

import org.compiere.model.MLocation;
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
public class MLocationExt extends MLocation implements DocAction
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
			MCountryExt defaultCountry = MCountryExt.getDefault(getCtx()); 
			setCountry(defaultCountry);
			MRegionExt defaultRegion = MRegionExt.getDefault(getCtx());
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
		if (C_Region_ID >=0 ) {
			setRegion (MRegionExt.get(getCtx(), C_Region_ID));
		}
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
	/*public String getRegionName()
	{
		return getRegionName(false);
	}	//	getRegionName

	/**
	 * 	Get Region Name
	 * 	@param getFroMRegionExt get from region (not locally)
	 *	@return	region Name or ""
	 */
	/*public String getRegionName (boolean getFroMRegionExt)
	{
		if (getFroMRegionExt && getCountry().isHasRegion() 
			&& getRegion() != null)
		{
			super.setRegionName("");	//	avoid duplicates
			return getRegion().getName();
		}
		//
		String regionName = super.getRegionName();
		if (regionName == null)
			regionName = "";
		return regionName;
	}	//	getRegionName

	
	/**
	 * 	Compares to current record
	 *	@param C_Country_ID if 0 ignored
	 *	@param C_Region_ID if 0 ignored
	 *	@param Postal match postal
	 *	@param Postal_Add match postal add
	 *	@param City match city
	 *	@param Address1 match address 1
	 *	@param Address2 match address 2
	 *	@return true if equals
	 */
	/*public boolean equals (int C_Country_ID, int C_Region_ID, 
		String Postal, String Postal_Add, String City, String Address1, String Address2)
	{
		if (C_Country_ID != 0 && getC_Country_ID() != C_Country_ID)
			return false;
		if (C_Region_ID != 0 && getC_Region_ID() != C_Region_ID)
			return false;
		//	must match
		if (!equalsNull(Postal, getPostal()))
			return false;
		if (!equalsNull(Postal_Add, getPostal_Add()))
			return false;
		if (!equalsNull(City, getCity()))
			return false;
		if (!equalsNull(Address1, getAddress1()))
			return false;
		if (!equalsNull(Address2, getAddress2()))
			return false;
		return true;
	}	//	equals
	
	/**
	 * 	Equals if "" or Null
	 *	@param c1 c1
	 *	@param c2 c2
	 *	@return true if equal (ignore case)
	 */
	/*private boolean equalsNull (String c1, String c2)
	{
		if (c1 == null)
			c1 = "";
		if (c2 == null)
			c2 = "";
		return c1.equalsIgnoreCase(c2);
	}	//	equalsNull
	
	/**
	 * 	Equals
	 * 	@param cmp comparator
	 * 	@return true if ID the same
	 */
	/*public boolean equals (Object cmp)
	{
		if (cmp == null)
			return false;
		if (cmp.getClass().equals(this.getClass()))
			return ((PO)cmp).get_ID() == get_ID();
		return equals(cmp);
	}	//	equals

	/**
	 * 	Print Address Reverse Order
	 *	@return true if reverse depending on country
	 */
	/*public boolean isAddressLinesReverse()
	{
		//	Local
		if (getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID())
			return getCountry().isAddressLinesLocalReverse();
		return getCountry().isAddressLinesReverse();
	}	//	isAddressLinesReverse

	
	/**
	 * 	Get formatted City Region Postal line
	 * 	@return City, Region Postal
	 */
	/*public String getCityRegionPostal()
	{
		return parseCRP (getCountry());
	}	//	getCityRegionPostal
	
	/**
	 *	Parse according City/Postal/Region according to displaySequence.
	 *	@C@ - City		@R@ - Region	@P@ - Postal  @A@ - PostalAdd
	 *  @param c country
	 *  @return parsed String
	 */
	/*private String parseCRP (MCountry c)
	{
		if (c == null)
			return "CountryNotFound";

		boolean local = getC_Country_ID() == MCountry.getDefault(getCtx()).getC_Country_ID();
		String inStr = local ? c.getDisplaySequenceLocal() : c.getDisplaySequence();
		StringBuffer outStr = new StringBuffer();

		String token;
		int i = inStr.indexOf('@');
		while (i != -1)
		{
			outStr.append (inStr.substring(0, i));			// up to @
			inStr = inStr.substring(i+1, inStr.length());	// from first @

			int j = inStr.indexOf('@');						// next @
			if (j < 0)
			{
				token = "";									//	no second tag
				j = i+1;
			}
			else
				token = inStr.substring(0, j);
			//	Tokens
			if (token.equals("C"))
			{
				if (getCity() != null)
					outStr.append(getCity());
			}
			else if (token.equals("R"))
			{
				if (getRegion() != null)					//	we have a region
					outStr.append(getRegion().getName());
				else if (super.getRegionName() != null && super.getRegionName().length() > 0)
					outStr.append(super.getRegionName());	//	local region name
			}
			else if (token.equals("P"))
			{
				if (getPostal() != null)
					outStr.append(getPostal());
			}
			else if (token.equals("A"))
			{
				String add = getPostal_Add();
				if (add != null && add.length() > 0)
					outStr.append("-").append(add);
			}
			else
				outStr.append("@").append(token).append("@");

			inStr = inStr.substring(j+1, inStr.length());	// from second @
			i = inStr.indexOf('@');
		}
		outStr.append(inStr);						// add the rest of the string

		//	Print Region Name if entered and not part of pattern
		if (c.getDisplaySequence().indexOf("@R@") == -1
			&& super.getRegionName() != null && super.getRegionName().length() > 0)
			outStr.append(" ").append(super.getRegionName());

		String retValue = Util.replace(outStr.toString(), "\\n", "\n");
		log.finest("parseCRP - " + c.getDisplaySequence() + " -> " +  retValue);
		return retValue;
	}	//	parseContext

	
	/**************************************************************************
	 *	Return printable String representation
	 *  @return String
	 */
	/*public String toString()
	{
		StringBuffer retStr = new StringBuffer();
		if (isAddressLinesReverse())
		{
			//	City, Region, Postal
			retStr.append(", ").append(parseCRP (getCountry()));
			if (getAddress4() != null && getAddress4().length() > 0)
				retStr.append(", ").append(getAddress4());
			if (getAddress3() != null && getAddress3().length() > 0)
				retStr.append(", ").append(getAddress3());
			if (getAddress2() != null && getAddress2().length() > 0)
				retStr.append(", ").append(getAddress2());
			if (getAddress1() != null)
				retStr.append(getAddress1());
		}
		else
		{
			if (getAddress1() != null)
				retStr.append(getAddress1());
			if (getAddress2() != null && getAddress2().length() > 0)
				retStr.append(", ").append(getAddress2());
			if (getAddress3() != null && getAddress3().length() > 0)
				retStr.append(", ").append(getAddress3());
			if (getAddress4() != null && getAddress4().length() > 0)
				retStr.append(", ").append(getAddress4());
			//	City, Region, Postal
			retStr.append(", ").append(parseCRP (getCountry()));
			//	Add Country would come here
		}
		return retStr.toString();
	}	//	toString

	/**
	 *	Return String representation with CR at line end
	 *  @return String
	 */
	/*public String toStringCR()
	{
		StringBuffer retStr = new StringBuffer();
		if (isAddressLinesReverse())
		{
			//	City, Region, Postal
			retStr.append(parseCRP (getCountry()));
			if (getAddress4() != null && getAddress4().length() > 0)
				retStr.append("\n").append(getAddress4());
			if (getAddress3() != null && getAddress3().length() > 0)
				retStr.append("\n").append(getAddress3());
			if (getAddress2() != null && getAddress2().length() > 0)
				retStr.append("\n").append(getAddress2());
			if (getAddress1() != null)
				retStr.append("\n").append(getAddress1());
		}
		else
		{
			if (getAddress1() != null)
				retStr.append(getAddress1());
			if (getAddress2() != null && getAddress2().length() > 0)
				retStr.append("\n").append(getAddress2());
			if (getAddress3() != null && getAddress3().length() > 0)
				retStr.append("\n").append(getAddress3());
			if (getAddress4() != null && getAddress4().length() > 0)
				retStr.append("\n").append(getAddress4());
			//	City, Region, Postal
			retStr.append("\n").append(parseCRP (getCountry()));
			//	Add Country would come here
		}
		return retStr.toString();
	}	//	toStringCR

	/**
	 *	Return detailed String representation
	 *  @return String
	 */
	/*public String toStringX()
	{
		StringBuffer sb = new StringBuffer("MLocation=[");
		sb.append(get_ID())
			.append(",C_Country_ID=").append(getC_Country_ID())
			.append(",C_Region_ID=").append(getC_Region_ID())
			.append(",Postal=").append(getPostal())
			.append ("]");
		return sb.toString();
	}   //  toStringX

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	/*protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		//	Region Check
		if (getC_Region_ID() != 0)
		{
			if (m_c == null || m_c.getC_Country_ID() != getC_Country_ID())
				getCountry();
			if (!m_c.isHasRegion())
				setC_Region_ID(0);
		}
		if (getC_City_ID() <= 0 && getCity() != null && getCity().length() > 0) {
			int city_id = DB.getSQLValue(
					get_TrxName(),
					"SELECT C_City_ID FROM C_City WHERE C_Country_ID=? AND COALESCE(C_Region_ID,0)=? AND Name=?",
					new Object[] {getC_Country_ID(), getC_Region_ID(), getCity()});
			if (city_id > 0)
				setC_City_ID(city_id);
		}

		//check city
		if (m_c != null && !m_c.isAllowCitiesOutOfList() && getC_City_ID()<=0) {
			log.saveError("CityNotFound", Msg.translate(getCtx(), "CityNotFound"));
			return false;
		}
		
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	/*protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Value/Name change in Account
		if (!newRecord
			&& ("Y".equals(Env.getContext(getCtx(), "$Element_LF")) 
				|| "Y".equals(Env.getContext(getCtx(), "$Element_LT")))
			&& (is_ValueChanged("Postal") || is_ValueChanged("City"))
			)
			MAccount.updateValueDescription(getCtx(), 
				"(C_LocFrom_ID=" + getC_Location_ID() 
				+ " OR C_LocTo_ID=" + getC_Location_ID() + ")", get_TrxName());
		
		//Update BP_Location name IDEMPIERE 417
		if (get_TrxName().startsWith("POSave")) { // saved without trx
			int bplID = DB.getSQLValueEx(get_TrxName(), "SELECT C_BPartner_Location_ID FROM C_BPartner_Location WHERE C_Location_ID = " + getC_Location_ID());
			if (bplID>0)
			{
				MBPartnerLocation bpl = new MBPartnerLocation(getCtx(), bplID, get_TrxName());
				Integer id=this.get_ID();
				
//				bpl.setName(bpl.getBPLocName(this));
				bpl.saveEx();
			}
		}
		return success;
	}	//	afterSave

	/**
	 * 	Get edited Value (MLocation) for GoogleMaps / IDEMPIERE-147
	 *  @param MLocationExt location
	 *	@return String address
	 */
	/*public String getMapsLocation() {

		MRegionExt region = new MRegionExt(Env.getCtx(), getC_Region_ID(), get_TrxName());
		String address = "";
		address = address + (getAddress1() != null ? getAddress1() + ", " : "");
		address = address + (getAddress2() != null ? getAddress2() + ", " : "");
		address = address + (getCity() != null ? getCity() + ", " : "");
		address = address + (region.getName() != null ? region.getName() + ", " : "");
		address = address + (getCountryName() != null ? getCountryName() : "");

		return address.replace(" ", "+");
	}

	@Override
	public void setDocStatus(String newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processIt(String action) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String prepareIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentNo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocAction() {
		// TODO Auto-generated method stub
		return null;
	}
*/

	@Override
	public void setDocStatus(String newStatus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processIt(String action) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlockIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean invalidateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String prepareIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean approveIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rejectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String completeIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean voidIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseCorrectIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reverseAccrualIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reActivateIt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentNo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocumentInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File createPDF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessMsg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDoc_User_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getC_Currency_ID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BigDecimal getApprovalAmt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocAction() {
		// TODO Auto-generated method stub
		return null;
	}
}	//	MLocation

package org.amerp.amxeditor.model;

import org.compiere.model.I_C_Location;

public interface I_C_Location_Amerp extends I_C_Location{
	
    /** Column name C_Municipality_ID */
    public static final String COLUMNNAME_C_Municipality_ID = "C_Municipality_ID";

	/** Set C_Municipality	  */
	public void setC_Municipality_ID (int C_Municipality_ID);

	/** Get C_Municipality	  */
	public int getC_Municipality_ID();
	
    /** Column name C_Parish_ID */
    public static final String COLUMNNAME_C_Parish_ID = "C_Parish_ID";

	/** Set c_Parish	  */
	public void setC_Parish_ID (int C_Parish_ID);

	/** Get c_Parish	  */
	public int getC_Parish_ID();


    /** Column name GeocodingStatus */
    public static final String COLUMNNAME_GeocodingStatus = "GeocodingStatus";

	/** Set Geocoding Status.
	  * Status or result obtained from the automatic geocoding process.
	  */
	public void setGeocodingStatus (String GeocodingStatus);

	/** Get Geocoding Status.
	  * Status or result obtained from the automatic geocoding process.
	  */
	public String getGeocodingStatus();

    /** Column name Latitude */
    public static final String COLUMNNAME_Latitude = "Latitude";

	/** Set Latitude.
	  * Geographic latitude coordinate of the location.
	  */
	public void setLatitude (String Latitude);

	/** Get Latitude.
	  * Geographic latitude coordinate of the location.
	  */
	public String getLatitude();

    /** Column name Longitude */
    public static final String COLUMNNAME_Longitude = "Longitude";

	/** Set Longitude.
	  * Geographic longitude coordinate of the location.
	  */
	public void setLongitude (String Longitude);

	/** Get Longitude.
	  * Geographic longitude coordinate of the location.
	  */
	public String getLongitude();

	/** Column name GeocodingStatus */
    public static final String COLUMNNAME_GeoSearchAddress = "GeoSearchAddress";

	/** Set Search Address .
	  * Search Address  or result obtained from the automatic geocoding process.
	*/
	public void setGeoSearchAddress (String GeoSearchAddress);

	/** Get GeoSearchAddress.
	  * Search Address or result obtained from the automatic geocoding process.
	*/
	public String getGeoSearchAddress();

    /** Column name OpenGeoMap */
    public static final String COLUMNNAME_OpenGeoMap = "OpenGeoMap";

	/** Set Open GeoMap.
	  * Open GeoMap for Update
	  */
	public void setOpenGeoMap (String OpenGeoMap);

	/** Get OpenGeoMap
	  * Open GeoMap for Update
	  */
	public String getOpenGeoMap();
	
}

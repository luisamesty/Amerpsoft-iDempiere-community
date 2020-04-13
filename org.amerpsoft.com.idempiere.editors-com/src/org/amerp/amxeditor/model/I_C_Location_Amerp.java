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


}

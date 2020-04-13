package org.amerp.amxeditor.model;

import org.compiere.model.I_C_Region;

public interface I_C_Region_Amerp extends I_C_Region {

	   /** Column name C_Community_ID */
    public static final String COLUMNNAME_C_Community_ID = "C_Community_ID";

	/** Set Community	  */
	public void setC_Community_ID (int C_Community_ID);

	/** Get Community	  */
	public int getC_Community_ID();
	
}

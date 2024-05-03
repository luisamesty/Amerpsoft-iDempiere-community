package org.amerp.amxeditor.model;

import org.compiere.model.I_C_City;

public interface I_C_City_Amerp extends I_C_City {

	/** Column name Value */
	public static final String COLUMNNAME_Value = "Value";

	/** Set Value */
	public void setValue(String Value);

	/** Get Value */
	public String getValue();

}

package org.amerp.amxeditor.model;

import org.compiere.model.I_C_Country;

public interface I_C_Country_Amerp extends I_C_Country {

    /** Column name countrycode3 */
    public static final String COLUMNNAME_countrycode3 = "countrycode3";

	/** Set countrycode3	  */
	public void setcountrycode3 (String countrycode3);

	/** Get countrycode3	  */
	public String getcountrycode3();
	
	   /** Column name HasCommunity */
    public static final String COLUMNNAME_HasCommunity = "HasCommunity";

	/** Set HasCommunity	  */
	public void setHasCommunity (boolean HasCommunity);

	/** Get HasCommunity	  */
	public boolean isHasCommunity();

    /** Column name HasMunicipality */
    public static final String COLUMNNAME_HasMunicipality = "HasMunicipality";

	/** Set HasMunicipality	  */
	public void setHasMunicipality (boolean HasMunicipality);

	/** Get HasMunicipality	  */
	public boolean isHasMunicipality();

    /** Column name HasParish */
    public static final String COLUMNNAME_HasParish = "HasParish";

	/** Set HasParish	  */
	public void setHasParish (boolean HasParish);

	/** Get HasParish	  */
	public boolean isHasParish();

}

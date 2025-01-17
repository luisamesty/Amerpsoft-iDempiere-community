package org.amerp.webform.amwmodel;

public class OrgInfo {

	/**
	 * 	OrgInfo
	 *	@param newAD_Org_ID
	 */
	public OrgInfo (int newAD_Org_ID, String newName)
	{
		AD_Org_ID = newAD_Org_ID;
		Name = newName;
	}
	int AD_Org_ID;
	String Name;

	/**
	 * 	to String
	 *	@return info
	 */
	public String toString()
	{
		return Name;
	}

	public int getAD_Org_ID() {
		return AD_Org_ID;
	}

	public void setAD_Org_ID(int aD_Org_ID) {
		AD_Org_ID = aD_Org_ID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
}   //  OrgInfo
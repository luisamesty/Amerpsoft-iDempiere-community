package org.amerp.amncallouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Employee_Tax;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class AMN_Employee_DetailedNames_callout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(AMN_Employee_DetailedNames_callout.class);

	private String fn1 ="";
	private String fn2 ="";
	private String ln1 ="";
	private String ln2 ="";
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		//
		fn1 = getFn1(mTab);
		fn2 = getFn2(mTab);
		ln1 = getLn1(mTab);
		ln2 = getLn2(mTab);
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		String fullName = "";
		
		// ******************************
		// FieldRef: LastName1
		// (Verify if null)
		// ******************************
		if (mTab.getValue(MAMN_Employee.COLUMNNAME_FirstName1) != null ||
				mTab.getValue(MAMN_Employee.COLUMNNAME_LastName1) != null) {
			fullName = getFullName(fn1,fn2, ln1, ln2, AD_Client_ID);
			mTab.setValue(MBPartner.COLUMNNAME_Name, fullName);
		}
		return null;
	}

	public final String SPACE = " ";

	public String getFullName(String fn1, String fn2, String ln1, String ln2, int AD_Client_ID) {
		StringBuilder fullName = new StringBuilder();
	
		String nameSeparator = " ";
		if (fn1.length() == 0 && ln1.length() == 0)
			return null;

		fullName.append(fn1).append(nameSeparator)
			.append(fn2).append(nameSeparator)
			.append(ln1).append(nameSeparator)
			.append(ln2);
	
		return fullName.toString();
	}

	public String getFn1(GridTab mTab) {
		fn1="";
		if ( mTab.getValue(MAMN_Employee.COLUMNNAME_FirstName1) != null)
			fn1 = (String) mTab.getValue(MAMN_Employee.COLUMNNAME_FirstName1);
		return fn1;
	}

	public String getFn2(GridTab mTab) {
		fn2="";
		if (mTab.getValue(MAMN_Employee.COLUMNNAME_FirstName2) !=null)
			fn2 =(String)  mTab.getValue(MAMN_Employee.COLUMNNAME_FirstName2);
		return fn2;
	}

	public String getLn1(GridTab mTab) {
		ln1="";
		if (mTab.getValue(MAMN_Employee.COLUMNNAME_LastName1) != null)
			ln1 = (String) mTab.getValue(MAMN_Employee.COLUMNNAME_LastName1);
		return ln1;
	}


	public String getLn2(GridTab mTab) {
		ln2="";
		if (mTab.getValue(MAMN_Employee.COLUMNNAME_LastName2) != null) 
			ln2 = (String) mTab.getValue(MAMN_Employee.COLUMNNAME_LastName2);
		return ln2;
	}

	public void setFn1(GridTab mTab, String fn1) {
		this.fn1 = fn1;
	}

	public void setFn2(GridTab mTab, String fn2) {
		this.fn2 = fn2;
	}

	public void setLn1(GridTab mTab, String ln1) {
		this.ln1 = ln1;
	}

	public void setLn2(GridTab mTab, String ln2) {
		this.ln2 = ln2;
	}
	
}

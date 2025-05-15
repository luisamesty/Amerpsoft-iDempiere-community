package org.amerp.amncallouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Leaves;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class AMN_Employee_callout implements IColumnCallout {

	int AD_Client_ID =0;
	int AD_Org_ID=0;
	String BioCode ="";
	String empValue= "";
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// 
		String columnName = mField.getColumnName();
		// Value
		if (columnName.equalsIgnoreCase(MAMN_Employee.COLUMNNAME_Value)) {
			if (mTab.getValue(MAMN_Employee.COLUMNNAME_Value)  != null ) {
				empValue = (String) mTab.getValue(MAMN_Employee.COLUMNNAME_Value);
				BioCode = (String) mTab.getValue(MAMN_Employee.COLUMNNAME_BioCode);
				if ( BioCode == null || BioCode.isEmpty() || BioCode.compareTo("0") == 0){
					mTab.setValue(MAMN_Employee.COLUMNNAME_BioCode, empValue);
				}
			}
		}
		return null;
	}

}

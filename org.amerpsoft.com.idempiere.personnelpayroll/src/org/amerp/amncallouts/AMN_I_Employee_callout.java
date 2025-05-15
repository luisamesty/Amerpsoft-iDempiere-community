package org.amerp.amncallouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_I_Employee;
import org.amerp.amnmodel.MAMN_Leaves;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

public class AMN_I_Employee_callout implements IColumnCallout {

	int AD_Client_ID =0;
	int AD_Org_ID=0;
	String BioCode ="";
	String empValue= "";
	String bpValue= "";
	String billBpValue="";
	
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// 
		String columnName = mField.getColumnName();
		// Value
		if (columnName.equalsIgnoreCase(MAMN_I_Employee.COLUMNNAME_Value)) {
			if (mTab.getValue(MAMN_I_Employee.COLUMNNAME_Value)  != null ) {
				empValue = (String) mTab.getValue(MAMN_I_Employee.COLUMNNAME_Value);
				BioCode = (String) mTab.getValue(MAMN_I_Employee.COLUMNNAME_BioCode);
				billBpValue =  (String) mTab.getValue(MAMN_I_Employee.COLUMNNAME_Bill_BPValue);
				bpValue =  (String)  mTab.getValue(MAMN_I_Employee.COLUMNNAME_BPValue);
				if ( BioCode == null || BioCode.isEmpty() || BioCode.compareTo("0") == 0 || BioCode.compareTo(empValue) != 0){
					mTab.setValue(MAMN_I_Employee.COLUMNNAME_BioCode, empValue);
				}
				if ( bpValue == null || bpValue.isEmpty() || bpValue.compareTo(empValue) != 0){
					mTab.setValue(MAMN_I_Employee.COLUMNNAME_BPValue, empValue);
				}
				if ( billBpValue == null || billBpValue.isEmpty() || billBpValue.compareTo("0") == 0){
					mTab.setValue(MAMN_I_Employee.COLUMNNAME_Bill_BPValue, MAMN_I_Employee.DEFAULT_Bill_BPValue);
				}
			}
		}
		return null;
	}

}

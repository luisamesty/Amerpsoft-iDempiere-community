package org.amerp.amncallouts;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Payroll_Deferred;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

public class AMN_Payroll_Deferred_callout implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(AMN_Payroll_Deferred_callout.class);
	
	
	@Override
	public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
		// TODO Auto-generated method stub
		// *************************************
		// FieldRef: AMN_Concept_Types_Proc_ID
		// *************************************
		if (p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Concept_Types_Proc_ID) != null 
				&& p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Period_ID) != null  )
		{
			
		}
		return null;
	}

}

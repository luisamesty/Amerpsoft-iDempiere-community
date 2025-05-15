package org.amerp.amncallouts;

import java.math.BigDecimal;
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
		//
		String columnName = p_mField.getColumnName();
		int AMN_Payroll_ID=0;
		int AMN_Payroll_Detail_ID=0;
		int AMN_Concept_Types_Proc_ID=0;
		if (p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Concept_Types_Proc_ID) != null )
			AMN_Concept_Types_Proc_ID = (int) p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Concept_Types_Proc_ID);
		if (p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Payroll_ID) != null )
			AMN_Payroll_ID = (int) p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Payroll_ID);
		
		// *************************************
		//  AMN_Concept_Types_Proc_ID
		// *************************************
		// AMN_Concept_Types_Proc_ID
		if (columnName.equalsIgnoreCase(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Concept_Types_Proc_ID)) {
			if (p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Concept_Types_Proc_ID) != null 
					&& p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Period_ID) != null  )
			{

			}
		}
		
		// *************************************
		// DueDate
		// *************************************
		if (columnName.equalsIgnoreCase(MAMN_Payroll_Deferred.COLUMNNAME_DueDate)) {
			
		}
			
		// QtyValue
		if (columnName.equalsIgnoreCase(MAMN_Payroll_Deferred.COLUMNNAME_QtyValue)) {
			if (p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_QtyValue) != null ) {
				p_mTab.setValue(MAMN_Payroll_Deferred.COLUMNNAME_AmountCalculated, p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_QtyValue) );
				p_mTab.setValue(MAMN_Payroll_Deferred.COLUMNNAME_AmountAllocated, BigDecimal.ZERO );
				p_mTab.setValue(MAMN_Payroll_Deferred.COLUMNNAME_AmountDeducted, p_mTab.getValue(MAMN_Payroll_Deferred.COLUMNNAME_QtyValue) );			
//				if (AMN_Payroll_ID >0)
//					MAMN_Payroll_Deferred.updatePayrollDeferredSums(p_ctx, AMN_Payroll_ID, null);
			}
		}
		return null;
	}

}

/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.pp.factories;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.amerp.amnmodel.*;
import org.amerp.amncallouts.*;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class AMNCalloutFactory implements IColumnCalloutFactory {
	
	static CLogger log = CLogger.getCLogger(AMNCalloutFactory.class);
	@Override
	public IColumnCallout[] getColumnCallouts(String tableName,
			String columnName) {
		// TODO Auto-generated method stub	
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		// *********************************
		// TableRef: amn_role_access
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Role_Access.Table_Name)) {
			// FieldRef: amn_role_access_id
			if (columnName.equalsIgnoreCase(MAMN_Role_Access.COLUMNNAME_AD_Role_ID))
				list.add(new AMN_Role_Access_callout());
			// FieldRef: amn_process_id
			if (columnName.equalsIgnoreCase(MAMN_Role_Access.COLUMNNAME_AMN_Process_ID))
				list.add(new AMN_Role_Access_callout());
			// FieldRef: amn_contract_id
			if (columnName.equalsIgnoreCase(MAMN_Role_Access.COLUMNNAME_AMN_Contract_ID))
				list.add(new AMN_Role_Access_callout());
			// FieldRef: name
			if (columnName.equalsIgnoreCase(MAMN_Role_Access.COLUMNNAME_Name))
				list.add(new AMN_Role_Access_callout());
		}

		// *********************************
		// TableRef: amn_concept
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Concept.Table_Name)) {
			// NO callout Field, for future use
		}
		// *********************************
		// TableRef: amn_concept_types_
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Concept_Types.Table_Name)) {
			// FieldRef: amn_concept_types_ID
			if (columnName.equalsIgnoreCase(MAMN_Concept_Types.COLUMNNAME_AMN_Concept_Types_ID))
				list.add(new AMN_Concept_Types_callout());
			// FieldRef: aValue
			if (columnName.equalsIgnoreCase(MAMN_Concept_Types.COLUMNNAME_Value))
				list.add(new AMN_Concept_Types_callout());
		}		
		
		// *********************************
		// TableRef: amn_concept_types_proc
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Concept_Types_Proc.Table_Name)) {
			// FieldRef: amn_concept_types_ID
			if (columnName.equalsIgnoreCase(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Concept_Types_ID))
				list.add(new AMN_Concept_Types_Proc_callout());
			// FieldRef: AMN_Process_ID
			if (columnName.equalsIgnoreCase(MAMN_Concept_Types_Proc.COLUMNNAME_AMN_Process_ID))
				list.add(new AMN_Concept_Types_Proc_callout());
		}		
		
		// *************************************
		// TableRef: amn_concept_types_contract
		// *************************************
		if (tableName.equalsIgnoreCase(MAMN_Concept_Types_Contract.Table_Name)) {
			// FieldRef: amn_concept_types_ID
			if (columnName.equalsIgnoreCase(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Concept_Types_ID))
				list.add(new AMN_Concept_Types_Contract_callout());
			// FieldRef: AMN_Contract_ID
			if (columnName.equalsIgnoreCase(MAMN_Concept_Types_Contract.COLUMNNAME_AMN_Contract_ID))
				list.add(new AMN_Concept_Types_Contract_callout());
		}		
		
		// *********************************
		// TableRef: amn_process
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Process.Table_Name)) {
			// FieldRef: Value 
			if (columnName.equalsIgnoreCase(MAMN_Process.COLUMNNAME_Value))
				list.add(new AMN_Process_callout());
		}
		
		// *********************************
		// TableRef: amn_payroll
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Payroll.Table_Name)) {
			// FieldRef: AMN_Employee_ID 
			if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_AMN_Employee_ID))
				list.add(new AMN_Payroll_callout());
			// FieldRef: AMN_Employee_ID 
			if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_InvDateIni))
				list.add(new AMN_Payroll_Dates_callout());
			// FieldRef: AMN_Employee_ID 
			if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_InvDateEnd))
				list.add(new AMN_Payroll_Dates_callout());

		}
		
		// *********************************
		// TableRef: amn_period
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Period.Table_Name)) {
			// FieldRef: amndateini
			if (columnName.equalsIgnoreCase(MAMN_Period.COLUMNNAME_AMNDateIni))
				list.add(new AMN_Period_callout());
			// FieldRef: amndateend
			if (columnName.equalsIgnoreCase(MAMN_Period.COLUMNNAME_AMNDateEnd))
				list.add(new AMN_Period_callout());
		}
		// *********************************
		// TableRef: amn_period_assist
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Period_Assist.Table_Name)) {
			// FieldRef: amndateini
			if (columnName.equalsIgnoreCase(MAMN_Period.COLUMNNAME_AMNDateIni))
				list.add(new AMN_Period_callout());
			// FieldRef: amndateend
			if (columnName.equalsIgnoreCase(MAMN_Period.COLUMNNAME_AMNDateEnd))
				list.add(new AMN_Period_callout());
		}	
		// *********************************
		// TableRef: amn_payroll_detail
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Payroll_Detail.Table_Name)) {
			// Add  fields here
			// FieldRef: COLUMNNAME_AMN_Concept_Types_Proc_ID
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Detail.COLUMNNAME_AMN_Concept_Types_Proc_ID))
				list.add(new AMN_Payroll_Detail_callout());
			// FieldRef: COLUMNNAME_QtyValue
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Detail.COLUMNNAME_QtyValue)) {
				list.add(new AMN_Payroll_Detail_Qty_callout());
			}		
		}
		// *********************************
		// TableRef: amn_payroll_deferred
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Payroll_Deferred.Table_Name)) {
			// Add  fields here
			// FieldRef: COLUMNNAME_AMN_Period_ID
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Period_ID)) {
				list.add(new AMN_Payroll_Deferred_callout());
			}	
			// FieldRef: COLUMNNAME_AMN_Concept_Types_Proc_ID
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Deferred.COLUMNNAME_AMN_Concept_Types_Proc_ID))
				list.add(new AMN_Payroll_Deferred_callout());
		}		
		// *********************************
		// TableRef: amn_payroll_assist
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Payroll_Assist.Table_Name)) {
			// FieldRef: AMN_Employee_ID
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist.COLUMNNAME_AMN_Employee_ID))
				list.add(new AMN_Payroll_Assist_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist.COLUMNNAME_BioCode))
				list.add(new AMN_Payroll_Assist_BioCode_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist.COLUMNNAME_AMN_Shift_ID))
				list.add(new AMN_Payroll_Assist_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist.COLUMNNAME_Event_Type))
				list.add(new AMN_Payroll_Assist_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist.COLUMNNAME_Event_Time))
				list.add(new AMN_Payroll_Assist_callout());
		}	
		// *********************************
		// TableRef: amn_payroll_assist_proc
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.Table_Name)) {
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In1))
				list.add(new AMN_Payroll_Assist_Proc_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out1))
				list.add(new AMN_Payroll_Assist_Proc_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In2))
				list.add(new AMN_Payroll_Assist_Proc_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out2))
				list.add(new AMN_Payroll_Assist_Proc_callout());
		}
		
		// *********************************
		// TableRef: amn_employee_tax
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Employee_Tax.Table_Name)) {
			if (columnName.equalsIgnoreCase(MAMN_Employee_Tax.COLUMNNAME_FiscalYear))
				list.add(new AMN_Employee_Tax_callout());
			if (columnName.equalsIgnoreCase(MAMN_Employee_Tax.COLUMNNAME_ValidFrom))
				list.add(new AMN_Employee_Tax_callout());
			if (columnName.equalsIgnoreCase(MAMN_Employee_Tax.COLUMNNAME_ValidTo))
				list.add(new AMN_Employee_Tax_callout());
		}
		
		// *********************************
		// TableRef: amn_employee
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Employee.Table_Name)) {
			// Detailed Name
			if ( columnName.equalsIgnoreCase(MAMN_Employee.COLUMNNAME_FirstName1) || 
					columnName.equalsIgnoreCase(MAMN_Employee.COLUMNNAME_FirstName2) ||
					columnName.equalsIgnoreCase(MAMN_Employee.COLUMNNAME_LastName1) || 
					columnName.equalsIgnoreCase(MAMN_Employee.COLUMNNAME_LastName2))
				list.add(new AMN_Employee_DetailedNames_callout());
		}
		
		// *********************************
		// TableRef: amn_payroll_lot
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Payroll_Lot.Table_Name)) {
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Lot.COLUMNNAME_AMN_Process_ID))
				list.add(new AMN_Payroll_Lot_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Lot.COLUMNNAME_C_Year_ID))
				list.add(new AMN_Payroll_Lot_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Lot.COLUMNNAME_C_Period_ID))
				list.add(new AMN_Payroll_Lot_callout());
			if (columnName.equalsIgnoreCase(MAMN_Payroll_Lot.COLUMNNAME_DateAcct))
				list.add(new AMN_Payroll_Lot_callout());		}

		
		// *********************************
		// TableRef: AMN_Leaves
		// *********************************
		if (tableName.equalsIgnoreCase(MAMN_Leaves.Table_Name)) {
			
			if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID) ) {
		 		list.add(new AMN_Leaves_callout());
		 	}
			if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_AMN_Leaves_ID) ) {
		 		list.add(new AMN_Leaves_callout());
		 	}
	    	if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DocStatus)) {
		 		list.add(new AMN_Leaves_callout());
	    	}
	    	if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DateFrom)) {
	    		list.add(new AMN_Leaves_callout());
	    	}
	    	if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DateTo)) {
	    		list.add(new AMN_Leaves_callout());
	    	}
	    	if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DaysTo)) {
	    		list.add(new AMN_Leaves_callout());
	    	}
//	    	if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_Note)) {
//		 		list.add(new AMN_Leaves_callout());
//	    	}
		}
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}

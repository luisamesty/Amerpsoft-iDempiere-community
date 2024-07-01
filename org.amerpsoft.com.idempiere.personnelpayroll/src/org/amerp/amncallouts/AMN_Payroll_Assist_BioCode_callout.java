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
package org.amerp.amncallouts;

import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll_Assist;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

public class AMN_Payroll_Assist_BioCode_callout implements IColumnCallout{

	Integer Employee_ID = 0;
	String BioCode;
	Integer Shift_ID =0;
	Integer EmpShift_ID =0;
	String AssistDay ="";
	String AssistDayName ="";
	String EventType="";
	Timestamp AssistDate=null;
	Timestamp AssistTime=null;
	
	String AssistRecord="";
	static CLogger log = CLogger.getCLogger(AMN_Payroll_Assist_BioCode_callout.class);
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
    	//log.warning("................AMN_Payroll_Assist_callout............");
    	// TODO Auto-generated method stub
    	// *****************************************************
		// * BioCode
		// *****************************************************
    	AssistRecord="";
    	if (p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_BioCode) != null )
		{
    		MAMN_Employee amnemployeebc = null;
    		BioCode = (String) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_BioCode);
    		Employee_ID = MAMN_Employee.findAMN_Employee_IDbyBioCode(BioCode);
    		if (Employee_ID > 0) {
    			amnemployeebc = new MAMN_Employee(p_ctx, Employee_ID, null);
    	    	EmpShift_ID = amnemployeebc.getAMN_Shift_ID();
    	    	Employee_ID = amnemployeebc.getAMN_Employee_ID();
        		// *****************************************************
        		// * Event_Date
        		// *****************************************************
        		if (p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Date) != null ) {
        			AssistDate = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Date);
        			AssistDay = MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(AssistDate);	
        			AssistDayName = MAMN_Payroll_Assist.getPayrollAssist_DayofWeekName(AssistDate);	
        			//log.warning("AssistDay:"+AssistDay+" AssistDate:"+AssistDate+" "+AssistDayName);
        			AssistRecord=AssistRecord+"-"+AssistDayName.trim()+" "+AssistDate;
        		    // ******************************	
        	    	// Set field's DayOfWeek 
        		    // ******************************	
        			p_mTab.setValue("DayOfWeek",AssistDay );	
        		}    	    	
    		} else {
    			amnemployeebc = null;
    		}
	    	//log.warning("EmpShift_ID:"+EmpShift_ID+" Employee_ID:"+Employee_ID+"-"+amnemployee.getName());
	    	// ******************************	
	    	// Set field's AMN_Shift_ID AND
    		//    			AMN_Employee_ID
		    // ******************************	
			if (amnemployeebc !=  null) {
				p_mTab.setValue("AMN_Shift_ID", EmpShift_ID);	
				p_mTab.setValue("AMN_Employee_ID", Employee_ID);
				p_mTab.setValue("BioCode",BioCode);
				AssistRecord=AssistRecord+amnemployeebc.getValue().trim();
			}
		}
    	//log.warning("AssistDay:"+AssistDay+"  BioCode:"+BioCode+"  Employee_ID:"+Employee_ID+"  EmpShift_ID:"+EmpShift_ID);
		return null;
    }

}

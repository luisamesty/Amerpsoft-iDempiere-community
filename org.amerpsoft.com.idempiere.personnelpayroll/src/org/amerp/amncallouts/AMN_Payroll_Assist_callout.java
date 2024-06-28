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


import java.util.*;
import java.sql.Timestamp;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.*;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;

/**
 * @author luisamesty
 *
 */
public class AMN_Payroll_Assist_callout implements IColumnCallout {

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
	static CLogger log = CLogger.getCLogger(AMN_Payroll_Assist_callout.class);
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
    	//log.warning("................AMN_Payroll_Assist_callout............");
    	// TODO Auto-generated method stub
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
		// *****************************************************
		// * AMN_Employee_ID
		// *****************************************************
    	AssistRecord="";
    	if (p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_AMN_Employee_ID) != null )
		{
			Employee_ID =   (Integer) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_AMN_Employee_ID);
	    	// AMN_Employee Cache
	    	MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, Employee_ID, null);
	    	EmpShift_ID = amnemployee.getAMN_Shift_ID();
	    	BioCode = amnemployee.getBioCode();
	    	//log.warning("EmpShift_ID:"+EmpShift_ID+" Employee_ID:"+Employee_ID+"-"+amnemployee.getName());
	    	// ******************************	
	    	// Set field's AMN_Shift_ID and 
	    	// 		Biocode
		    // ******************************	
			//if (EmpShift_ID !=0 && EmpShift_ID != null) 
			p_mTab.setValue("AMN_Shift_ID", EmpShift_ID);
			p_mTab.setValue("Biocode",BioCode);
			AssistRecord=AssistRecord+amnemployee.getValue().trim();
		}
    	
		// *****************************************************
		// * AMN_Shift_ID
		// *****************************************************
    	if (p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_AMN_Shift_ID) != null ) {
    		Shift_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_AMN_Shift_ID);
    		MAMN_Shift amnshift = new MAMN_Shift(p_ctx, Shift_ID, null);
    		if (Shift_ID !=0 && Shift_ID != null) 
				p_mTab.setValue("AMN_Shift_ID", Shift_ID);
    		AssistRecord=AssistRecord+"-"+amnshift.getValue().trim();
    		//log.warning("..........AMN_Shift_ID"+ EmpShift_ID);
		}
		// *****************************************************
		// * Event_Type
		// *****************************************************
			
		if (p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Type) != null ) {
			EventType = (String) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Type);
			AssistRecord=AssistRecord+"-"+EventType.trim();
		}
		// *****************************************************
		// * Event_Time
		// *****************************************************
		if (p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Time) != null ) {
			AssistTime = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Time);
			AssistDate = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist.COLUMNNAME_Event_Time);
			//log.warning("AssistDate:"+AssistDate);			
			AssistDay = MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(AssistDate);	
			AssistDayName = MAMN_Payroll_Assist.getPayrollAssist_DayofWeekName(AssistDate);	
			if (!AssistDate.equals(null)) {
				// Set AssistDate Var
				GregorianCalendar dCal = new GregorianCalendar();
				dCal.setTime(AssistDate);
				// Set AssistTime Var
				Calendar tCal = Calendar.getInstance();
				tCal.setTime(AssistTime);
				// Set AssistTime Values to AssistDate Var
				dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
				dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
				dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
				dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
				AssistTime = new Timestamp(dCal.getTimeInMillis());
			}
			p_mTab.setValue("Event_Time",AssistTime);
			AssistRecord=AssistRecord+"-"+AssistTime;
		}

		// *****************************************************
		// * Description
		// *****************************************************
		if (!AssistRecord.isEmpty())
			p_mTab.setValue("AMN_AssistRecord",AssistRecord );	
		//log.warning("..........AMN_Shift_ID:"+ EmpShift_ID+ "  AMN_AssistRecord:"+AssistRecord);		
		return null;
    }

}

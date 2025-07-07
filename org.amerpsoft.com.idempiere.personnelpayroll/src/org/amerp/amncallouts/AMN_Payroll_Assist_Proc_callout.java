package org.amerp.amncallouts;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Leaves;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Proc;
import org.amerp.amnmodel.MAMN_Shift_Detail;
import org.amerp.process.AMNPayrollProcessPayrollAssistProc;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMN_Payroll_Assist_Proc_callout implements IColumnCallout{
	
	BigDecimal BDZero = BigDecimal.valueOf(0);
	Integer empAMN_Shift_ID=0;
	Integer attAMN_Shift_ID=0;
	Integer AMN_Employee_ID=0;
	Integer AMN_Payroll_Assist_Proc_ID=0;
	Integer C_Country_ID=100;
    BigDecimal Shift_HED = BDZero;
    BigDecimal Shift_HEN = BDZero;
    BigDecimal Shift_HND = BDZero;
    BigDecimal Shift_HNN = BDZero;
    BigDecimal Shift_Attendance = BDZero;
    BigDecimal Shift_AttendanceBonus = BDZero;
	Timestamp Shift_In1=null;
	Timestamp Shift_In2=null;
	Timestamp Shift_Out1=null;
	Timestamp Shift_Out2=null;
	Timestamp Event_Date = null;
	MAMN_Payroll_Assist_Proc atthours = new MAMN_Payroll_Assist_Proc(Env.getCtx(), null);
	MAMN_Payroll_Assist_Proc amnpayrollassistproc = null;
	MAMN_Employee amnemployee = null;
    MAMN_Shift_Detail amnshiftdetail = null;
	String Description ="";
	String AssistRecord="";
	Boolean isDescanso=false;
	Boolean isExcused=false;
	static CLogger log = CLogger.getCLogger(AMN_Payroll_Assist_Proc_callout.class);
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
	public String start(Properties p_ctx, int WindowNo, 
			GridTab p_mTab, GridField p_mField,
			Object p_value, Object p_oldValue) {
    	// Column Name
    	boolean updateTabs = false;
    	String columnName = p_mField.getColumnName();
		// AMN_Payroll_Assist_Proc_ID
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Payroll_Assist_Proc_ID) != null ) {
    		AMN_Payroll_Assist_Proc_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Payroll_Assist_Proc_ID);
    	}
	    // Event_Date
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Event_Date) != null) {
    		Event_Date = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Event_Date);
    	}
    	// AMN_Employee_ID
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Employee_ID) != null ) {
    		AMN_Employee_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Employee_ID);
    		amnpayrollassistproc = new MAMN_Payroll_Assist_Proc(Env.getCtx(), AMN_Payroll_Assist_Proc_ID, null);
    	    amnemployee = new MAMN_Employee(Env.getCtx(), AMN_Employee_ID, null);
    	    C_Country_ID = amnemployee.getC_Country_ID();
    	    // Get Employee AMN_Shift_ID by default
    	    empAMN_Shift_ID = amnemployee.getAMN_Shift_ID();
    	    // MAMN_Shift_Detail
		    amnshiftdetail = MAMN_Shift_Detail.findByEventDate(Env.getCtx(), empAMN_Shift_ID, Event_Date);
		    isDescanso= amnshiftdetail.isDescanso();	
    	}
    	// Attendance AMN_Shift_ID set 
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Shift_ID) != null)	{
    		attAMN_Shift_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Shift_ID);
    	    // MAMN_Shift_Detail
		    amnshiftdetail = MAMN_Shift_Detail.findByEventDate(Env.getCtx(), attAMN_Shift_ID, Event_Date);
		    isDescanso= amnshiftdetail.isDescanso();	
    	}
    	
	    // Shift_In1
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In1) != null) {
    		Shift_In1 = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In1);
    		// Normalize to Even_Date
    		Shift_In1 = AMNPayrollProcessPayrollAssistProc.getTimestampShifdetailEventTime(Event_Date,Shift_In1);
    	}
	    // Shift_Out1
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out1) != null) {
    		Shift_Out1 = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out1);
    		// Normalize to Even_Date
    		Shift_Out1 = AMNPayrollProcessPayrollAssistProc.getTimestampShifdetailEventTime(Event_Date,Shift_Out1);
    	}
	    // Shift_In2
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In2) != null) {
    		Shift_In2 = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In2);
    		// Normalize to Even_Date
    		Shift_In2 = AMNPayrollProcessPayrollAssistProc.getTimestampShifdetailEventTime(Event_Date,Shift_In2);
    	}	 
	    // Shift_Out2
    	if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out2) != null) {
    		Shift_Out2 = (Timestamp) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out2);
    		// Normalize to Even_Date
    		Shift_Out2 = AMNPayrollProcessPayrollAssistProc.getTimestampShifdetailEventTime(Event_Date,Shift_Out2);;
    	}
		// *****************************************************
		// Calculate Hours
	    // *****************************************************
    	// AMN_Shift_ID
    	if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Shift_ID)) {
    		updateTabs = true;
    		if (p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Shift_ID) != null) {
    			attAMN_Shift_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Assist_Proc.COLUMNNAME_AMN_Shift_ID);
    			// MAMN_Shift_Detail
    		    amnshiftdetail = MAMN_Shift_Detail.findByEventDate(Env.getCtx(), attAMN_Shift_ID, Event_Date);
        	}
    	}
    	
    	// Event_Date Shift_In1 Shift_Out1 
    	if (columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In1)
    			|| columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out1)
     			|| columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In2)
     			|| columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out2)
     			|| columnName.equalsIgnoreCase(MAMN_Payroll_Assist_Proc.COLUMNNAME_Event_Date)
    			) {
	    	if (Shift_In1!=null && Shift_Out1!=null 
	    		&& Shift_In2!=null && Shift_Out2!=null 
	    		&& Event_Date != null && attAMN_Shift_ID>0 
	    		&& AMN_Payroll_Assist_Proc_ID >0 )	{
				atthours= AMNPayrollProcessPayrollAssistProc.calcAttendanceValuesofPayrollVars(
						Env.getCtx(), amnemployee.getAD_Client_ID(), amnemployee.getAD_Org_ID(), C_Country_ID, Event_Date, attAMN_Shift_ID, 
						Shift_In1, Shift_Out1, Shift_In2, Shift_Out2);
	    		updateTabs = true;
	    	}		
    	}
    	// // Update Tabs
    	if (updateTabs) {
    		// Update Tabs
			Shift_HED = atthours.getShift_HED();
			Shift_HEN = atthours.getShift_HEN();
			Shift_HND = atthours.getShift_HND();
			Shift_HNN = atthours.getShift_HNN();
			Shift_Attendance = atthours.getShift_Attendance();
			Shift_AttendanceBonus = atthours.getShift_AttendanceBonus();
			Description=Msg.getMsg(Env.getCtx(), "Updated");
			p_mTab.setValue("Protected",true);
			p_mTab.setValue("Shift_In1",Shift_In1 );
			p_mTab.setValue("Shift_Out1",Shift_Out1 );
			p_mTab.setValue("Shift_In2",Shift_In2 );
			p_mTab.setValue("Shift_Out2",Shift_Out2 );
			p_mTab.setValue("Shift_Attendance",Shift_Attendance );
			p_mTab.setValue("Shift_AttendanceBonus",Shift_AttendanceBonus );
			p_mTab.setValue("Shift_HED",Shift_HED );
			p_mTab.setValue("Shift_HEN",Shift_HEN );
			p_mTab.setValue("Shift_HND",Shift_HND );
			p_mTab.setValue("Shift_HNN",Shift_HNN );
			p_mTab.setValue("Description",Description );
			// ADDITIONAL VAlues
			p_mTab.setValue("Shift_HT", atthours.getShift_HT());       // Total Work Hours (Shift_HT)
			p_mTab.setValue("Shift_HC", atthours.getShift_HC());       // Complete Hours (Shift_HC)
			p_mTab.setValue("Shift_HLGT15", atthours.getShift_HLGT15()); // Free Hours Greater than 15 (Shift_HLGT15)
			p_mTab.setValue("Shift_HLLT15", atthours.getShift_HLLT15()); // Free Hours Less than 15 (Shift_HLLT15)
			p_mTab.setValue("Shift_THL", atthours.getShift_THL());     // Free Hours (Shift_THL)
			p_mTab.setValue("Shift_LTA", atthours.getShift_LTA());     // Late Arrivals (Shift_LTA)
			p_mTab.setValue("Shift_EDE", atthours.getShift_EDE());     // Early Departure (Shift_EDE)
			p_mTab.setValue("Shift_HER", atthours.getShift_HER());     // Extra Clock Hours (Shift_HER)
			p_mTab.setValue("Shift_HEF", atthours.getShift_HEF());     // Extra Holiday Hours (Shift_HEF)
    	}
    	return null;
	}
}


package org.amerp.amncallouts;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Proc;
import org.amerp.amnmodel.MAMN_Shift_Detail;
import org.amerp.amnutilities.AttendanceHours;
import org.amerp.process.AMNPayrollProcessPayrollAssistProc;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMN_Payroll_Assist_Proc_callout implements IColumnCallout{
	
	BigDecimal BDZero = BigDecimal.valueOf(0);
	Integer AD_Org_ID=0;
	Integer AD_Client_ID=0;
	Integer AMN_Shift_ID=0;
	Integer AMN_Employee_ID=0;
	Integer AMN_Payroll_Assist_Proc_ID=0;
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
	AttendanceHours atthours = new AttendanceHours(BDZero, BDZero, BDZero, BDZero, BDZero, BDZero, "");
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
    	    // Get Employee AMN_Shift_ID by default
    	    AMN_Shift_ID = amnemployee.getAMN_Shift_ID();
    	    AD_Org_ID=amnemployee.getAD_Org_ID();
    	    AD_Client_ID=amnemployee.getAD_Client_ID();
    	    // MAMN_Shift_Detail
		    amnshiftdetail = MAMN_Shift_Detail.findByEventDate(Env.getCtx(), AMN_Shift_ID, Event_Date);
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
    	if (Shift_In1!=null && Shift_Out1!=null 
    		&& Shift_In2!=null &&Shift_Out2!=null 
    		&& Event_Date != null && AMN_Shift_ID>0 
    		&& AMN_Payroll_Assist_Proc_ID >0 )	{
			atthours= AMNPayrollProcessPayrollAssistProc.calcAttendanceValuesofPayrollVars(
					Event_Date, AMN_Shift_ID, 
					Shift_In1, Shift_Out1, Shift_In2, Shift_Out2);
			Shift_HED = atthours.getHR_HED();
			Shift_HEN = atthours.getHR_HEN();
			Shift_HND = atthours.getHR_HND();
			Shift_HNN = atthours.getHR_HNN();
			Shift_Attendance = atthours.getDAY_ATT();
			Shift_AttendanceBonus = atthours.getDAY_ATTB();
			Description=Msg.getMsg(Env.getCtx(), "Updated");
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
    	}		
		return null;
	}
}


package org.amerp.amncallouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Leaves;
import org.amerp.amnmodel.MAMN_Leaves_Types;
import org.amerp.amnmodel.MAMN_NonBusinessDay;
import org.amerp.workflow.amwmodel.MAMW_WF_Node;
import org.amerp.workflow.amwmodel.MAMW_WorkFlow;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MField;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMN_Leaves_callout  implements IColumnCallout {

	int AMN_Leaves_ID=0;
	int AMN_Leaves_Types_ID=0;
	int AD_Client_ID =0;
	int AD_Org_ID=0;
	BigDecimal DaysTo = BigDecimal.ZERO;
	BigDecimal HoursDay = BigDecimal.ZERO;
	String DocStatus = "";
	String lNote="";
	int AMW_WorkFlow_ID=0;
	int AMW_WF_Node_ID=0;
	MAMW_WF_Node amwf = null;
	Timestamp AMNLeavesStartDate;
	Timestamp AMNLeavesEndDate;
	String DaysMode = "";
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {

		DocStatus = (String) mTab.getValue(MAMN_Leaves.COLUMNNAME_DocStatus);
		AMW_WorkFlow_ID = (int) mTab.getValue(MAMW_WorkFlow.COLUMNNAME_AMW_WorkFlow_ID);
		String columnName = mField.getColumnName();
		AD_Client_ID = (int) mTab.getValue(MAMN_Leaves.COLUMNNAME_AD_Client_ID);
		AD_Org_ID = (int) mTab.getValue(MAMN_Leaves.COLUMNNAME_AD_Org_ID);
		boolean overrideCalc = (boolean) mTab.getValue(MAMN_Leaves.COLUMNNAME_IsOverrideCalc);
		// GLOBAL Leaves_types DaysMode VARS
		if (mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID)  != null ) {
			// Set Default Days 
			AMN_Leaves_Types_ID =  (int) mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID) ;
			MAMN_Leaves_Types leavetype = new MAMN_Leaves_Types(Env.getCtx(), AMN_Leaves_Types_ID, null);
			DaysMode = leavetype.getDaysMode();
		}
		
		// AMN_Leaves_Types_ID
		if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID)) {
			if (mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID)  != null ) {
				// Set Default Days 
				AMN_Leaves_Types_ID =  (int) mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID) ;
				MAMN_Leaves_Types leavetype = new MAMN_Leaves_Types(Env.getCtx(), AMN_Leaves_Types_ID, null);
				DaysMode = leavetype.getDaysMode();
				// DateTo
				if (leavetype.getDefaultDays()!=null && leavetype.getDefaultDays().compareTo(BigDecimal.ZERO) > 0 ) {
					DaysTo = (BigDecimal) leavetype.getDefaultDays();
					mTab.setValue(MAMN_Leaves.COLUMNNAME_DaysTo, DaysTo);
					AMNLeavesStartDate = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom);
					if (AMNLeavesStartDate != null) {
						if (DaysMode.compareToIgnoreCase("B")==0) {
							AMNLeavesEndDate = MAMN_NonBusinessDay.getNextBusinessDay(AMNLeavesStartDate, DaysTo, AD_Client_ID, AD_Org_ID);
						} else {
							AMNLeavesEndDate = MAMN_NonBusinessDay.getNextCalendarDay(AMNLeavesStartDate, DaysTo, AD_Client_ID, AD_Org_ID);
						}
						mTab.setValue(MAMN_Leaves.COLUMNNAME_DateTo,AMNLeavesEndDate);
					}
				}
				// HoursDay
				if (leavetype.getHoursDay()!=null && leavetype.getHoursDay().compareTo(BigDecimal.ZERO) > 0 ) {
					HoursDay = (BigDecimal) leavetype.getHoursDay();
					mTab.setValue(MAMN_Leaves.COLUMNNAME_HoursDay, HoursDay);
				}
			}
		}
		
		// AMN_Leaves_ID
		if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_AMN_Leaves_ID)) {
	    	if (mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_ID) != null) {
	    		AMN_Leaves_ID =  (int) mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_ID) ;
	    		MAMN_Leaves leave = new MAMN_Leaves(Env.getCtx(), AMN_Leaves_ID, null);
	    		// Get Node Form WorkFlow ID and Doc Status
	    		amwf = leave.getAMWWorkFlowNodeFromDocStatusSQL(leave.getAMW_WorkFlow_ID(), leave.getDocStatus());
		    }
			if (amwf != null  && !DocStatus.isEmpty() &&  DocStatus != null) {
				mTab.setValue("DocStatus",DocStatus);

			}
		}
		
    	// DocStatus
		if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DocStatus)) {
	    	if (mTab.getValue(MAMN_Leaves.COLUMNNAME_DocStatus) != null) {
	    		DocStatus = (String) mTab.getValue(MAMN_Leaves.COLUMNNAME_DocStatus);
	    	}
		}
		
		// DateFrom
		if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DateFrom) ) {
			if (mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom) != null &&
					mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID) != null &&
					mTab.getValue(MAMN_Leaves.COLUMNNAME_DaysTo) != null &&
					!overrideCalc) {
				AMNLeavesStartDate = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom);
				DaysTo = (BigDecimal) mTab.getValue(MAMN_Leaves.COLUMNNAME_DaysTo);
				// Get Next Business Day
				if (AMNLeavesStartDate != null) {
					if (DaysMode.compareToIgnoreCase("B")==0) {
						AMNLeavesEndDate = MAMN_NonBusinessDay.getNextBusinessDay(AMNLeavesStartDate, DaysTo, AD_Client_ID, AD_Org_ID);
					} else {
						AMNLeavesEndDate = MAMN_NonBusinessDay.getNextCalendarDay(AMNLeavesStartDate, DaysTo, AD_Client_ID, AD_Org_ID);
					}
					mTab.setValue(MAMN_Leaves.COLUMNNAME_DateTo,AMNLeavesEndDate);
				}
			}
		}
		// DateTo
		if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DateTo)) {
			if (mTab.getValue(MAMN_Leaves.COLUMNNAME_DateTo) != null &&
					mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID) != null &&
					!overrideCalc) {
				AMNLeavesStartDate = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom);
				AMNLeavesEndDate   = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateTo);
				BigDecimal DTLeave = BigDecimal.ZERO;
				if (DaysMode.compareToIgnoreCase("B")==0) {
					DTLeave =MAMN_NonBusinessDay.sqlGetBusinessDaysBetween(AMNLeavesStartDate, AMNLeavesEndDate, AD_Client_ID, AD_Org_ID);
				} else  {
					DTLeave =MAMN_NonBusinessDay.getDaysBetween(AMNLeavesStartDate, AMNLeavesEndDate);
				}
				mTab.setValue(MAMN_Leaves.COLUMNNAME_DaysTo,DTLeave);
			}
		}
		// DaysTo
		if (columnName.equalsIgnoreCase(MAMN_Leaves.COLUMNNAME_DaysTo) ) {
			if (mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom) != null &&
					mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_Types_ID) != null &&
					!overrideCalc) {
				AMNLeavesStartDate = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom);
				DaysTo = (BigDecimal) mTab.getValue(MAMN_Leaves.COLUMNNAME_DaysTo);
				// Get Next Business Day
				if (AMNLeavesStartDate != null) {
					if (DaysMode.compareToIgnoreCase("B")==0) {
						AMNLeavesEndDate = MAMN_NonBusinessDay.getNextBusinessDay(AMNLeavesStartDate, DaysTo, AD_Client_ID, AD_Org_ID);
					} else {
						AMNLeavesEndDate = MAMN_NonBusinessDay.getNextCalendarDay(AMNLeavesStartDate, DaysTo, AD_Client_ID, AD_Org_ID);
					}
					mTab.setValue(MAMN_Leaves.COLUMNNAME_DateTo,AMNLeavesEndDate);
				}
			}
		}
	    return null;
    
	    
	}

}

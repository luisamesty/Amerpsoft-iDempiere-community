package org.amerp.amncallouts;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.MAMN_Leaves;
import org.amerp.amnmodel.MAMN_NonBusinessDay;
import org.amerp.workflow.amwmodel.MAMW_WF_Node;
import org.amerp.workflow.amwmodel.MAMW_WorkFlow;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMN_Leaves_Dates_callout  implements IColumnCallout {

	Timestamp AMNLeavesStartDate;
	Timestamp AMNLeavesEndDate;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		// DateFrom - DateTo
		if (mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom) != null &&
				mTab.getValue(MAMN_Leaves.COLUMNNAME_DateTo) != null ) {
			AMNLeavesStartDate = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateFrom);
			AMNLeavesEndDate   = (Timestamp) mTab.getValue(MAMN_Leaves.COLUMNNAME_DateTo);
			Double DTLeave =1.00+ MAMN_NonBusinessDay.getDaysBetween(AMNLeavesStartDate, AMNLeavesEndDate).doubleValue();
			int DTLeaveInt = (int) Math.round(DTLeave);
			mTab.setValue(MAMN_Leaves.COLUMNNAME_DaysTo,DTLeaveInt);
		}

	    return null;
    
	    
	}

}

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

public class AMN_Leaves_callout  implements IColumnCallout {

	int AMN_Leaves_ID=0;
	String DocStatus = "";
	String lNote="";
	int AMW_WorkFlow_ID=0;
	int AMW_WF_Node_ID=0;
	MAMW_WF_Node amwf = null;
	Timestamp AMNLeavesStartDate;
	Timestamp AMNLeavesEndDate;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {

		DocStatus = (String) mTab.getValue(MAMN_Leaves.COLUMNNAME_DocStatus);
		AMW_WorkFlow_ID = (int) mTab.getValue(MAMW_WorkFlow.COLUMNNAME_AMW_WorkFlow_ID);
		
    	if (mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_ID) != null) {
    		AMN_Leaves_ID =  (int) mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Leaves_ID) ;
    		MAMN_Leaves leave = new MAMN_Leaves(Env.getCtx(), AMN_Leaves_ID, null);
    		// Get Node Form WorkFlow ID and Doc Status
    		amwf = leave.getAMWWorkFlowNodeFromDocStatusSQL(leave.getAMW_WorkFlow_ID(), leave.getDocStatus());
	    }
    	
    	if (mTab.getValue(MAMN_Leaves.COLUMNNAME_DocStatus) != null) {
    		DocStatus = (String) mTab.getValue(MAMN_Leaves.COLUMNNAME_DocStatus);
    	}

		if (amwf != null  && !DocStatus.isEmpty() &&  DocStatus != null) {
			mTab.setValue("DocStatus",DocStatus);

		}
	    return null;
    
	    
	}

}

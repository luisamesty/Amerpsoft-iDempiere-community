package org.amerp.workflow.amwmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMW_WF_Node extends X_AMW_WF_Node{

	private static final long serialVersionUID = -8132020842201252418L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WF_Node.class);
	
	public MAMW_WF_Node(Properties ctx, int AMW_WF_Node_ID, String trxName) {
		super(ctx, AMW_WF_Node_ID, trxName);
		// 
	}

	public MAMW_WF_Node(Properties ctx, int AMW_WF_Node_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_Node_ID, trxName, virtualColumns);
		// 
	}

	public MAMW_WF_Node(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}

	public MAMW_WF_Node(Properties ctx, String AMW_WF_Node_UU, String trxName) {
		super(ctx, AMW_WF_Node_UU, trxName);
		// 
	}

	public MAMW_WF_Node(Properties ctx, String AMW_WF_Node_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_Node_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	/**
	 * check_AMW_WF_Node
	 * Verify if Node exists in array
	 * @param AMW_WF_Node_ID
	 * @param wflnodes
	 * @return
	 */
	public boolean check_AMW_WF_Node (int AMW_WF_Node_ID, MAMW_WF_Node[] wflnodes) {
		
		boolean retValue = false;
        for (MAMW_WF_Node wflnode : wflnodes) {
            if (wflnode.getAMW_WF_Node_ID() == AMW_WF_Node_ID) {
            	retValue = true;
                break;
            }
        }
		return retValue;
	}
	
	/**
	 * getAMWWorkFlowNodeSQL
	 * Returns Node from a given flow and node id
	 * @param AMW_WorkFlow_ID
	 * @param AMW_WF_Node_ID
	 * @return
	 */
	public MAMW_WF_Node getAMWWorkFlowNodeSQL (int AMW_WorkFlow_ID, int AMW_WF_Node_ID )
	{
		MAMW_WF_Node retValue = null;
		String sql= "SELECT DISTINCT * "+
				" FROM AMw_WF_Node " +
				" WHERE AMW_WorkFlow_ID=? "+
				" AND AMW_WF_Node_ID = ? " +
				" ORDER BY SeqNo ASC ";
		ArrayList<MAMW_WF_Node> list = new ArrayList<MAMW_WF_Node>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, AMW_WorkFlow_ID);
			pstmt.setInt (2, AMW_WF_Node_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				retValue = new MAMW_WF_Node(getCtx(), rs, get_TrxName());
				//log.warning("MAMW_WF_Node Name:"+line.getName());
			}
		} 
		catch (Exception e)
		{
			log.severe(e.getMessage());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		return retValue;
	}
}

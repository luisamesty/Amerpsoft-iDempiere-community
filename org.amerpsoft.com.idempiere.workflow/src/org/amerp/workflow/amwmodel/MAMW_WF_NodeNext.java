package org.amerp.workflow.amwmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMW_WF_NodeNext extends X_AMW_WF_NodeNext {


	private static final long serialVersionUID = 4097662513001973429L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WF_NodeNext.class);
	
	public MAMW_WF_NodeNext(Properties ctx, int AMW_WF_NodeNext_ID, String trxName) {
		super(ctx, AMW_WF_NodeNext_ID, trxName);
		// 
	}
	
	public MAMW_WF_NodeNext(Properties ctx, int AMW_WF_NodeNext_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_NodeNext_ID, trxName, virtualColumns);
		// 
	}	
	
	public MAMW_WF_NodeNext(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}
	
	public MAMW_WF_NodeNext(Properties ctx, String AMW_WF_NodeNext_UU, String trxName) {
		super(ctx, AMW_WF_NodeNext_UU, trxName);
		// 
	}

	public MAMW_WF_NodeNext(Properties ctx, String AMW_WF_NodeNext_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WF_NodeNext_UU, trxName, virtualColumns);
		// 
	}

	/**	 Lines			*/
	private MAMW_WF_NodeNext[]	m_lines;
	
	/**
	 * check_AMW_WF_NodeNext
	 * Verify Work Flow Node Next if exists giving AMW_WF_Node_ID and AMW_WF_NodeNext_ID
	 * @param AMW_WF_Node_ID
	 * @param AMW_WF_NodeNext_ID
	 * @param wflnodesnext
	 * @return
	 */
	public boolean check_AMW_WF_NodeNext (int AMW_WF_Node_ID, int AMW_WF_NodeNext_ID, MAMW_WF_NodeNext[] wflnodesnext) {
		
		boolean retValue = false;
        for (MAMW_WF_NodeNext wflnodenext : wflnodesnext) {
            if (wflnodenext.getAMW_WF_NodeNext_ID() == AMW_WF_NodeNext_ID 
            		&& wflnodenext.getAMW_WF_Node_ID()== AMW_WF_Node_ID ) {
            	retValue = true;
                break;
            }
        }
		return retValue;
	}

	/**
	 * getAMWNodesNext
	 * @param AMW_WF_Node_ID
	 * @return
	 */
	public MAMW_WF_NodeNext[] getAMWNodesNext (int AMW_WF_Node_ID)
	{
		MAMW_WF_NodeNext[] retValue = null;
		String whereClauseFinal = "AMW_WF_Node_ID=? ";
		if (AMW_WF_Node_ID > 0) {
			List<MAMW_WF_Node> list = new Query(getCtx(), I_AMW_WF_NodeNext.Table_Name, whereClauseFinal, get_TrxName())
					.setParameters(AMW_WF_Node_ID)
					.setOrderBy("SeqNo")
					.list();		
			retValue = list.toArray(new MAMW_WF_NodeNext[list.size()]);
		} 
		return retValue;
	}	//	getAMNNodes

	/**
	 * getAMWNodesNextSQL
	 * Get NodesNext from a given WorkFlow and a given AMW_WF_Node_ID (AMW_WorkFlow_ID)
	 * @param AMW_WorkFlow_ID
	 * @return
	 */
	public MAMW_WF_NodeNext[] getAMWNodesNextSQL (int AMW_WF_Node_ID)
	{
		String sql= "SELECT * "+
				" FROM AMW_WF_NodeNext WHERE AMW_WF_Node_ID=?"+
				" ORDER BY SeqNo ASC ";
		ArrayList<MAMW_WF_NodeNext> list = new ArrayList<MAMW_WF_NodeNext>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, AMW_WF_Node_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAMW_WF_NodeNext line = new MAMW_WF_NodeNext(getCtx(), rs, get_TrxName());
				list.add (line);
				log.warning("MAMW_WF_NodeNext Description:"+line.getDescription());
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
		m_lines = new MAMW_WF_NodeNext[list.size ()];
		list.toArray (m_lines);
		
		return m_lines;
	}

}

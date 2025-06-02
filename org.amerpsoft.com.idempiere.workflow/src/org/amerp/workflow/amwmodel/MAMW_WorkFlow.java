package org.amerp.workflow.amwmodel;

import java.lang.System.Logger.Level;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.compiere.model.MInvoiceLine;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MAMW_WorkFlow extends X_AMW_WorkFlow {

	private static final long serialVersionUID = 4526550064084127549L;
	
	static CLogger log = CLogger.getCLogger(MAMW_WorkFlow.class);

	public MAMW_WorkFlow(Properties ctx, int AMW_WorkFlow_ID, String trxName) {
		super(ctx, AMW_WorkFlow_ID, trxName);
		// 
	}

	public MAMW_WorkFlow(Properties ctx, int AMW_WorkFlow_ID, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WorkFlow_ID, trxName, virtualColumns);
		// 
	}
	
	public MAMW_WorkFlow(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// 
	}
	
	public MAMW_WorkFlow(Properties ctx, String AMW_WorkFlow_UU, String trxName) {
		super(ctx, AMW_WorkFlow_UU, trxName);
		// 
	}

	public MAMW_WorkFlow(Properties ctx, String AMW_WorkFlow_UU, String trxName, String[] virtualColumns) {
		super(ctx, AMW_WorkFlow_UU, trxName, virtualColumns);
		// 
	}

	/**	 Lines			*/
	private MAMW_WF_Node[]	m_lines;
	
	/**
	 * getAMWNodes.
	 * Get Nodes from AMW_WorkFlow by AMW_WorkFlow_ID
	 * @param AMW_WorkFlow_ID
	 * @return
	 */
	public MAMW_WF_Node[] getAMWNodes (int AMW_WorkFlow_ID)
	{
		String whereClauseFinal = "AMW_WorkFlow_ID=? ";
		List<MAMW_WF_Node> list = new Query(getCtx(), I_AMW_WF_Node.Table_Name, whereClauseFinal, get_TrxName())
					.setParameters(AMW_WorkFlow_ID)
					.setOrderBy(I_AMW_WF_Node.COLUMNNAME_SeqNo)
					.list();		
		m_lines = list.toArray(new MAMW_WF_Node[list.size()]);
		return m_lines;
	}	//	getAMNNodes
	
	/**
	 * getAMWNodesSQL
	 * Get Nodes from a given WorkFlow (AMW_WorkFlow_ID)
	 * @param AMW_WorkFlow_ID
	 * @return
	 */
	public MAMW_WF_Node[] getAMWNodesSQL (int AMW_WorkFlow_ID)
	{
		String sql= "SELECT * "+
				" FROM AMw_WF_Node WHERE AMW_WorkFlow_ID=?"+
				" ORDER BY SeqNo ASC ";
		ArrayList<MAMW_WF_Node> list = new ArrayList<MAMW_WF_Node>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, AMW_WorkFlow_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MAMW_WF_Node line = new MAMW_WF_Node(getCtx(), rs, get_TrxName());
				list.add (line);
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
		m_lines = new MAMW_WF_Node[list.size ()];
		list.toArray (m_lines);
		
		return m_lines;
	}
}

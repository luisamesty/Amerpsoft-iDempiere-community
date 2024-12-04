package org.amerp.workflow.factories;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.amerp.workflow.amwmodel.MAMW_WF_Access;
import org.amerp.workflow.amwmodel.MAMW_WF_Node;
import org.amerp.workflow.amwmodel.MAMW_WF_NodeNext;
import org.amerp.workflow.amwmodel.MAMW_WorkFlow;
import org.compiere.model.PO;
import org.compiere.util.Env;

public class AMWWorkFlowModelFactory implements IModelFactory {

	@Override
	public Class<?> getClass(String tableName) { 
		// MAMW_WorkFlow
		if(tableName.equalsIgnoreCase(MAMW_WorkFlow.Table_Name))
			return MAMW_WorkFlow.class;
		// MAMW_WF_Node
		if(tableName.equalsIgnoreCase(MAMW_WF_Node.Table_Name))
			return MAMW_WF_Node.class;		
		// MAMW_WF_NodeNext
		if(tableName.equalsIgnoreCase(MAMW_WF_NodeNext.Table_Name))
			return MAMW_WF_NodeNext.class;	
		// MAMW_WF_Access
		if(tableName.equalsIgnoreCase(MAMW_WF_Access.Table_Name))
			return MAMW_WF_Access.class;	
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		// MAMW_WorkFlow
		if(tableName.equalsIgnoreCase(MAMW_WorkFlow.Table_Name))
			return new MAMW_WorkFlow(Env.getCtx(),Record_ID, trxName);
		// MAMW_WF_Node
		if(tableName.equalsIgnoreCase(MAMW_WF_Node.Table_Name))
			return new MAMW_WF_Node(Env.getCtx(),Record_ID, trxName);
		// MAMW_WF_NodeNext
		if(tableName.equalsIgnoreCase(MAMW_WF_NodeNext.Table_Name))
			return new MAMW_WF_NodeNext(Env.getCtx(),Record_ID, trxName);
		// MAMW_WF_Access
		if(tableName.equalsIgnoreCase(MAMW_WF_Access.Table_Name))
			return new MAMW_WF_Access(Env.getCtx(),Record_ID, trxName);
		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
		// MAMW_WorkFlow
		if(tableName.equalsIgnoreCase(MAMW_WorkFlow.Table_Name))
			return new MAMW_WorkFlow(Env.getCtx(),rs, trxName);
		// MAMW_WF_Node
		if(tableName.equalsIgnoreCase(MAMW_WF_Node.Table_Name))
			return new MAMW_WF_Node(Env.getCtx(),rs, trxName);		
		// MAMW_WF_NodeNext
		if(tableName.equalsIgnoreCase(MAMW_WF_NodeNext.Table_Name))
			return new MAMW_WF_NodeNext(Env.getCtx(),rs, trxName);
		// MAMW_WF_Access
		if(tableName.equalsIgnoreCase(MAMW_WF_Access.Table_Name))
			return new MAMW_WF_Access(Env.getCtx(),rs, trxName);
		return null;
	}

}

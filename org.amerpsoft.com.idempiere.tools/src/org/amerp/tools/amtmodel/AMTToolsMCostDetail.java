package org.amerp.tools.amtmodel;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MCostDetail;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class AMTToolsMCostDetail extends MCostDetail {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4877458761909190806L;
	
	static CLogger log = CLogger.getCLogger(AMTToolsMCostDetail.class);

	public AMTToolsMCostDetail(MAcctSchema as, int AD_Org_ID, int M_Product_ID,
			int M_AttributeSetInstance_ID, int M_CostElement_ID,
			BigDecimal Amt, BigDecimal Qty, String Description, String trxName) {
		super(as, AD_Org_ID, M_Product_ID, M_AttributeSetInstance_ID, M_CostElement_ID,
				Amt, Qty, Description, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public AMTToolsMCostDetail(Properties ctx, int M_CostDetail_ID, String trxName) {
		super(ctx, M_CostDetail_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public AMTToolsMCostDetail(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Delete CostDetail M_CostHistory
	 * @param p_C_InvoiceLine_ID 
	 * @param trxName transaction
	 * @return number of rows deleted
	 * @throws DBException on database exception
	 */
	public static int deleteCostDetail(int p_C_InvoiceLine_ID, String trxName)
	throws DBException
	{
		int M_CostDetail_ID =0;
		int C_AcctSchema_ID=0;
		int no = 0;
		// MInvoiceLine
		MInvoiceLine mivl = new MInvoiceLine(Env.getCtx(),p_C_InvoiceLine_ID,  null);
		// AD_Client
		int AD_Client_ID = mivl.getAD_Client_ID();
//		MClient mc = new MClient(Env.getCtx(), AD_Client_ID ,null);
		//	Costing Methods on AS level
		MAcctSchema[] ass = MAcctSchema.getClientAcctSchema(Env.getCtx(), AD_Client_ID);
		for (int i = 0; i < ass.length; i++)
		{
			C_AcctSchema_ID=ass[i].getC_AcctSchema_ID();
			// GetM_CostDetail_ID
			M_CostDetail_ID = sqlGetM_CostDetail_ID(p_C_InvoiceLine_ID, C_AcctSchema_ID);
			// M_CostHistory
			String sql = "DELETE FROM M_CostHistory WHERE M_CostDetail_ID= "+M_CostDetail_ID;	
			no = no + DB.executeUpdateEx(sql, trxName);
			// M_CostDetail
			sql = "DELETE FROM M_CostDetail WHERE M_CostDetail_ID= "+M_CostDetail_ID;	
			no = no + DB.executeUpdateEx(sql, trxName);
//log.warning("-----M_CostDetail for M_CostDetail_ID:"+M_CostDetail_ID+"  DELETED ");		
		}	
		return no;
	}
	
	/**
	 * sqlGetM_CostDetail_ID (int C_InvoiceLine_ID, int C_AcctSchema_ID)
	 * @param C_InvoiceLine_ID
	 * @param C_AcctSchema_ID
	 * @return
	 */
	public static Integer sqlGetM_CostDetail_ID (int C_InvoiceLine_ID, int C_AcctSchema_ID)
	
	{
		String sql;
		Integer M_CostDetail_ID = 0;
		// M_CostDetail
    	sql = "select DISTINCT M_CostDetail_id from M_CostDetail "+
    			"WHERE C_AcctSchema_ID="+C_AcctSchema_ID+" AND C_InvoiceLine_ID=?" ;
    	M_CostDetail_ID = DB.getSQLValue(null, sql, C_InvoiceLine_ID);	
		return M_CostDetail_ID;	
	}
}

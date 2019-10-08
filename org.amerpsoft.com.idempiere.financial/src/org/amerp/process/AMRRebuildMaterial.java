/*****************************************************************************************
 * Tables:
 * M_Product_Acct: Accounting for M_Product
 * M_Product_Category_Acct: Accounting for M_Product_category
 * M_cost:  All Cost Elemente Values 
 * M_CostDetail: All Cost Details
 * M_CostHistory: Historic transaction Cost
 * M_CostHistoryPeriod: New Table for Cost and Balance for Products
 * 
 *****************************************************************************************/
package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MConversionRate;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MCurrency;
import org.compiere.model.MOrg;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategory;
import org.compiere.model.MProductCategoryAcct;
import org.compiere.model.MWarehouse;
import org.compiere.model.Query;
import org.compiere.model.X_M_Product_Acct;
import org.compiere.model.X_M_Product_Category_Acct;
import org.compiere.model.X_M_Warehouse_Acct;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class AMRRebuildMaterial {

	/**	Logger			*/
	protected static CLogger	log = CLogger.getCLogger(AMRRebuildMaterial.class);
	private ProcessInfo			m_pi;	// Process Info ID
	private static Trx		m_trx = Trx.get(Trx.createTrxName("Setup"), true);
	private Properties      m_ctx;
	private String          m_lang;
	private String m_trxName = null;
	private static StringBuffer m_info = new StringBuffer();
	//
	private MClient			m_client;
	private int AD_Client_ID;
	private MOrg			m_org;
	// Account Schemas
	private int SourceAcctSchema_ID;
	private MAcctSchema SourceAS;
	private int TargetAcctSchema_ID;
	private MAcctSchema TargetAS;
	// Currencies
	private int SourceCurrency_ID;
	private int TargetCurrency_ID;
	private MCurrency sourceCurr;
	private MCurrency targetCurr;
	// Target Conversion Rate
	private int C_Conversion_Rate_ID;
	// Conversion Factor
	private BigDecimal convFactorMultiply;
	private BigDecimal convFactorDivide;
	// 
	private int M_Product_ID = 0;
	private int M_Product_Category_ID = 0;
	private int M_Warehouse_ID=0;
	private String MessagetoShow="";
	// Product Count Vars
	int Product_Count = 0 ;
	int ProductNo = 0;
	// Product Category Count Vars
	int ProductCat_Count = 0 ;
	int ProductCatNo = 0;
	// Warehouse
	int WH_Count = 0;
	int WHNo = 0;
	int Percent = 0;
	// Counts
	BigDecimal insertM_Cost=BigDecimal.ZERO;
	BigDecimal updateM_Cost=BigDecimal.ZERO;
	BigDecimal errorM_Cost=BigDecimal.ZERO;
	// M_Product
	BigDecimal insertM_Product=BigDecimal.ZERO;
	BigDecimal updateM_Product=BigDecimal.ZERO;
	BigDecimal errorM_Product=BigDecimal.ZERO;
	// M_Product_Category
	BigDecimal insertM_Product_Category=BigDecimal.ZERO;
	BigDecimal updateM_Product_Category=BigDecimal.ZERO;
	BigDecimal errorM_Product_Category=BigDecimal.ZERO;

	/**
	 * dupM_Product_Acct
	 * @return
	 * @throws Exception
	 */
	public boolean dupM_Product_Acct () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(MProduct.Table_Name+"(s):").append("\n");
		Product_Count = getProductRecs(getAD_Client_ID());
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		ProductNo = 1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT M_Product_ID, Value, Name ")	//	1	
			.append(" FROM M_Product ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				M_Product_ID = rs.getInt(1);
//log.warning("ProductValue="+rs.getString(2)+"  ProductName="+rs.getString(3));		
				// getTargetAcctSchema_ID()
				// Percentage Monitor
				ProductNo = ProductNo+1;
				if (Product_Count != 0 ) {
					Percent =  (ProductNo * 100 /Product_Count);
				} else {
					Percent = 100;
					ProductNo=1;
				}
				// Copy Product Accounting
				copyM_Product_Acct(M_Product_ID, SourceAS, TargetAS);
//				if(ProductNo==5)
//					break;
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
//log.warning("noProductRecords:"+Product_Count);

		return false;
	}
	
	/**
	 * dupM_Product_Category_Acct
	 * @return
	 * @throws Exception
	 */
	public boolean dupM_Product_Category_Acct () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(MProductCategory.Table_Name+"(s):").append("\n");
		ProductCat_Count = getProductCategoryRecs(getAD_Client_ID());
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		ProductCatNo = 1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT M_Product_Category_ID, Value, Name ")	//	1	
			.append(" FROM M_Product_Category ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				M_Product_Category_ID = rs.getInt(1);
//log.warning("ProductCatNo="+ProductCatNo+" Product Category="+rs.getString(2)+"-"+rs.getString(3));		
				ProductCatNo = ProductCatNo+1;
				if (ProductCat_Count != 0 ) {
					Percent =  (ProductCatNo * 100 /ProductCat_Count);
				} else {
					Percent = 100;
					ProductCatNo=1;
				}
				// Copy Prodiuct Category Accounting
				copyM_Product_Category_Acct(M_Product_Category_ID, SourceAS, TargetAS);
//				if(ProductCatNo==5)
//					break;
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
//		//log.warning("noProductRecords:"+Product_Count);
//		m_info.append(X_M_Cost.Table_Name+":"+Msg.translate(Env.getCtx(), "Records")+" "+Msg.translate(Env.getCtx(), "Created")).append("\n");
//		m_info.append(getInsertM_Cost());
//		m_info.append(X_M_Cost.Table_Name+":"+Msg.translate(Env.getCtx(), "Records")+" "+Msg.translate(Env.getCtx(), "Updated"));
//		m_info.append(getUpdateM_Cost());
//		m_info.append( X_M_Cost.Table_Name+":"+Msg.translate(Env.getCtx(), "Records")+" "+Msg.translate(Env.getCtx(), "Error"));	
//		m_info.append(getErrorM_Cost());
		return false;
	}
	
	public boolean dupM_Warehouse_Acct () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(MWarehouse.Table_Name+"(s):").append("\n");
		WH_Count = getWarehouseRecs(getAD_Client_ID());
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		WHNo = 1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT M_Warehouse_ID, Value, Name ")	//	1	
			.append(" FROM M_Warehouse ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				M_Warehouse_ID = rs.getInt(1);
//log.warning("WHValue="+rs.getString(2)+"  WHName="+rs.getString(3));		
				// getTargetAcctSchema_ID()
				// Percentage Monitor
				WHNo = WHNo+1;
				if (WH_Count != 0 ) {
					Percent =  (WHNo * 100 /WH_Count);
				} else {
					Percent = 100;
					WHNo=1;
				}
				// Copy Product Accounting
				copyM_Warehouse_Acct(M_Warehouse_ID, SourceAS, TargetAS);
//				if(ProductNo==5)
//					break;
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
//log.warning("noProductRecords:"+Product_Count);

		return false;
	}
	
	/**
	 * dupM_Product_Cost
	 * @return
	 * @throws Exception
	 */
	public boolean dupM_Product_Cost () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(MProduct.Table_Name+"(s):").append("\n");
		Product_Count = getProductRecs(getAD_Client_ID());
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		ProductNo = 1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT M_Product_ID, Value, Name ")	//	1	
			.append(" FROM M_Product ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				M_Product_ID = rs.getInt(1);
//log.warning("ProductNo="+ProductNo+"  ProductValue="+rs.getString(2)+"  ProductName="+rs.getString(3));		
				// getTargetAcctSchema_ID()
				// Percentage Monitor
				ProductNo = ProductNo+1;
				if (Product_Count != 0 ) {
					Percent =  (ProductNo * 100 /Product_Count);
				} else {
					Percent = 100;
					ProductNo=1;
				}
				// M_Cost
				copyMCost();
//				if(ProductNo==5)
//					break;
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
//log.warning("noProductRecords:"+Product_Count);

		return false;
	}
	
	/**
	 * copyM_Product_Acct
	 * @param M_Product_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	
	private static void copyM_Product_Acct(int M_Product_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MProduct mp = new MProduct(Env.getCtx(),M_Product_ID,null);
		X_M_Product_Acct source = getM_Product_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), M_Product_ID);
		X_M_Product_Acct target = getM_Product_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), M_Product_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Standard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mp.getAD_Client_ID()) + ","+String.valueOf(mp.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source M_Product_ID="+source.getM_Product_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target M_Product_ID="+target.getM_Product_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getMProductAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO M_Product_Acct(");
		sqlCmdI1.append(stdColumns).append(",M_Product_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+M_Product_ID+","+targetAS.getC_AcctSchema_ID());
		if (target == null) {
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
				if (sourceC_ValidCombination_ID!= 0) {
					sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
					//targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
					AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
					// CREATE C_ValidCombination records
					targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
					if (targetAccount== null) {
						// CREATE New Valid Combination for the New Account Schema
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
						// log.warning("vctarget (null)="+vctarget	);				
					} else {
						// UPDATE New Valid Combination for the New Account Schema
						// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
					}
				} else {
					sourceAccount = MAccount.getDefault(sourceAS, false);
					targetAccount = MAccount.getDefault(sourceAS, false);
				}
				sqlCmdI1.append(","+columnName );
				sqlCmdI2.append(","+String.valueOf(targetAccount.getC_ValidCombination_ID()));
			}
			sqlCmdI1.append(", M_Product_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE M_Product_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" M_Product_ID="+M_Product_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
				if (sourceC_ValidCombination_ID!= 0) {
					sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
					//targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
					AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
					// CREATE C_ValidCombination records
					targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
					if (targetAccount== null) {
						// CREATE New Valid Combination for the New Account Schema
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
						// log.warning("vctarget (null)="+vctarget	);				
					} else {
						// UPDATE New Valid Combination for the New Account Schema
						// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
					}

				} else {
					sourceAccount = MAccount.getDefault(sourceAS, false);
					targetAccount = MAccount.getDefault(sourceAS, false);
				}
				if (i==0)
					sqlCmdI1.append("SET "+columnName+"=" );
				else
					sqlCmdI1.append(","+columnName+"=" );
				sqlCmdI1.append(String.valueOf(targetAccount.getC_ValidCombination_ID()));
			}
		}
//log.warning("sql="+sqlCmdI1+" "+sqlCmdI2);
		no = DB.executeUpdateEx(sqlCmdI1+" "+sqlCmdI2, null);
		if (no == 1) {
			m_info.append(Msg.translate( Env.getCtx(), "M_Product_ID")).append("=").append(mp.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"product="+mp.getValue()+" Default in AcctSchemaElement NOT updated");

	}	//	copyM_Product_Category_Acct
	
	/**
	 * copyM_Product_Category_Acct
	 * @param M_Product_Category_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyM_Product_Category_Acct2(int M_Product_Category_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		
	}

	private static void copyM_Product_Category_Acct(int M_Product_Category_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no=0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MProductCategory mpc = new MProductCategory(Env.getCtx(),M_Product_Category_ID,null);
		String ConstingLevel ="";
		String CostingMethod ="";
log.warning("Source   C_AcctSchema_ID="+sourceAS.getC_AcctSchema_ID());
log.warning("Target   C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
log.warning("M_Product_Category_ID="+M_Product_Category_ID);

		X_M_Product_Category_Acct source = getM_Product_Category_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), M_Product_Category_ID);
		ConstingLevel =source.getCostingLevel();
		CostingMethod =source.getCostingMethod();
		X_M_Product_Category_Acct target = getM_Product_Category_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), M_Product_Category_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Standard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mpc.getAD_Client_ID()) + ","+String.valueOf(mpc.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source M_Product_Category_ID="+source.getM_Product_Category_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target M_Product_Category_ID="+target.getM_Product_Category_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getMProductCategoryAcctInfo(source);
		if (target == null) {
			sqlCmdI1 = new StringBuffer ("INSERT INTO M_Product_Category_Acct(");
			sqlCmdI1.append(stdColumns).append(",M_Product_Category_ID,C_AcctSchema_ID");
			if (ConstingLevel != null)
				sqlCmdI1.append(", CostingLevel");
			if (CostingMethod != null)
				sqlCmdI1.append(", CostingMethod");
			sqlCmdI2 = new StringBuffer (" VALUES (");
			sqlCmdI2.append(stdValues).append(","+M_Product_Category_ID+","+targetAS.getC_AcctSchema_ID());
			if (ConstingLevel != null)
				sqlCmdI2.append(",'"+source.getCostingLevel()+"'");
			if (CostingMethod != null)
				sqlCmdI2.append(",'"+source.getCostingMethod()+"'");
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
				if (sourceC_ValidCombination_ID!= 0) {
					sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
					// targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
					AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
					// CREATE C_ValidCombination records
					targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
					if (targetAccount== null) {
						// CREATE New Valid Combination for the New Account Schema
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
						// log.warning("vctarget (null)="+vctarget	);				
					} else {
						// UPDATE New Valid Combination for the New Account Schema
						// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
					}

				} else {
					sourceAccount = MAccount.getDefault(sourceAS, false);
					targetAccount = MAccount.getDefault(sourceAS, false);
				}
				sqlCmdI1.append(","+columnName );
				sqlCmdI2.append(","+String.valueOf(targetAccount.getC_ValidCombination_ID()));
			}
			sqlCmdI1.append(", M_Product_Category_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE M_Product_Category_Acct ");
			sqlCmdI1.append("SET ");
			if (ConstingLevel != null)
				sqlCmdI1.append("CostingLevel='"+source.getCostingLevel()+"'");
			if (CostingMethod != null && ConstingLevel == null)
				sqlCmdI1.append("CostingMethod='"+source.getCostingMethod()+"'");
			if (CostingMethod != null && ConstingLevel != null)
				sqlCmdI1.append(", CostingMethod='"+source.getCostingMethod()+"'");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" M_Product_Category_ID="+M_Product_Category_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
			//sqlCmdI2.append(stdValues).append(","+M_Product_Category_ID+","+targetAS.getC_AcctSchema_ID());
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
				if (sourceC_ValidCombination_ID!= 0) {
					sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
					// targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
					AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
					// CREATE C_ValidCombination records
					targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
					if (targetAccount== null) {
						// CREATE New Valid Combination for the New Account Schema
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
						// log.warning("vctarget (null)="+vctarget	);				
					} else {
						// UPDATE New Valid Combination for the New Account Schema
						// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
					}
				} else {
					sourceAccount = MAccount.getDefault(sourceAS, false);
					targetAccount = MAccount.getDefault(sourceAS, false);
				}
				if ( i== 0 && ConstingLevel == null && CostingMethod == null)
					sqlCmdI1.append(" "+columnName+"=" );	
				else 
					sqlCmdI1.append(", "+columnName+"=" );	
				sqlCmdI1.append(String.valueOf(targetAccount.getC_ValidCombination_ID()));
			}
		}
//log.warning("sql="+sqlCmdI1+" "+sqlCmdI2);
		no = DB.executeUpdateEx(sqlCmdI1+" "+sqlCmdI2, null);
		if (no == 1) {
			m_info.append(Msg.translate( Env.getCtx(), "M_Product_Category_ID")).append("=").append(mpc.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else {
			log.log(Level.SEVERE,"product_category="+mpc.getValue()+" Default in AcctSchemaElement NOT updated");
		}
	}	//	copyM_Product_Category_Acct
	
	/**
	 * copyM_Warehouse_Acct
	 * @param M_Warehouse_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyM_Warehouse_Acct(int M_Warehouse_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MWarehouse mw = new MWarehouse(Env.getCtx(),M_Warehouse_ID,null);
		X_M_Warehouse_Acct source = getM_Warehouse_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), M_Warehouse_ID);
		X_M_Warehouse_Acct target = getM_Warehouse_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), M_Warehouse_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mw.getAD_Client_ID()) + ","+String.valueOf(mw.getAD_Org_ID())+ ",'Y',SysDate,"+
				String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source M_Product_ID="+source.getM_Product_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target M_Product_ID="+target.getM_Product_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getMWarehouseAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO M_Warehouse_Acct(");
		sqlCmdI1.append(stdColumns).append(",M_Warehouse_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+M_Warehouse_ID+","+targetAS.getC_AcctSchema_ID());
		if (target == null) {
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
				if (sourceC_ValidCombination_ID!= 0) {
					sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
					// targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
					AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
					// CREATE C_ValidCombination records
					targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
					if (targetAccount== null) {
						// CREATE New Valid Combination for the New Account Schema
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
						// log.warning("vctarget (null)="+vctarget	);				
					} else {
						// UPDATE New Valid Combination for the New Account Schema
						// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
					}
				} else {
					sourceAccount = MAccount.getDefault(sourceAS, false);
					targetAccount = MAccount.getDefault(sourceAS, false);
				}
				sqlCmdI1.append(","+columnName );
				sqlCmdI2.append(","+String.valueOf(targetAccount.getC_ValidCombination_ID()));
			}
			sqlCmdI1.append(", M_Warehouse_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE M_Warehouse_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" M_Warehouse_ID="+M_Warehouse_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
				if (sourceC_ValidCombination_ID!= 0) {
					sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
					// targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
					AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
					// CREATE C_ValidCombination records
					targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
					if (targetAccount== null) {
						// CREATE New Valid Combination for the New Account Schema
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
						// log.warning("vctarget (null)="+vctarget	);				
					} else {
						// UPDATE New Valid Combination for the New Account Schema
						// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
						targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
					}
				} else {
					sourceAccount = MAccount.getDefault(sourceAS, false);
					targetAccount = MAccount.getDefault(sourceAS, false);
				}
				if (i==0)
					sqlCmdI1.append("SET "+columnName+"=" );
				else
					sqlCmdI1.append(","+columnName+"=" );
				sqlCmdI1.append(String.valueOf(targetAccount.getC_ValidCombination_ID()));
			}
		}
//log.warning("sql="+sqlCmdI1+" "+sqlCmdI2);
		no = DB.executeUpdateEx(sqlCmdI1+" "+sqlCmdI2, null);
		if (no == 1) {
			m_info.append(Msg.translate( Env.getCtx(), "M_Warehouse_ID")).append("=").append(mw.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"warehouse="+mw.getValue()+" Default in AcctSchemaElement NOT updated");

	}	//	copyM_Warehouse_Category_Acct

//	/**
//	 * 	Create Account
//	 *	@param targetAS target AS
//	 *	@param sourceAcct source account
//	 *	@return target account
//	 */
//	private static MAccount createAccount(MAcctSchema sourceAS, MAcctSchema targetAS, MAccount sourceAcct)
//	{
//		MAccount retAccount = null;
//		int AD_Client_ID = targetAS.getAD_Client_ID(); 
//		int C_AcctSchema_ID = targetAS.getC_AcctSchema_ID();
//		//
//		int AD_Org_ID = 0;
//		int Account_ID = 0;
//		int C_SubAcct_ID = 0;
//		int M_Product_ID = 0;
//		int C_BPartner_ID = 0;
//		int AD_OrgTrx_ID = 0;
//		int C_LocFrom_ID = 0;
//		int C_LocTo_ID = 0;
//		int C_SalesRegion_ID = 0; 
//		int C_Project_ID = 0;
//		int C_Campaign_ID = 0;
//		int C_Activity_ID = 0;
//		int User1_ID = 0;
//		int User2_ID = 0;
//		int UserElement1_ID = 0;
//		int UserElement2_ID = 0;
//		//
//		//  Active Elements
//		MAcctSchemaElement[] elements = sourceAS.getAcctSchemaElements();
//		for (int i = 0; i < elements.length; i++)
//		{
//			MAcctSchemaElement ase = elements[i];
//			String elementType = ase.getElementType();
//			//
//			if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization))
//				AD_Org_ID = sourceAcct.getAD_Org_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
//				Account_ID = sourceAcct.getAccount_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_SubAccount))
//				C_SubAcct_ID = sourceAcct.getC_SubAcct_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_BPartner))
//				C_BPartner_ID = sourceAcct.getC_BPartner_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Product))
//				M_Product_ID = sourceAcct.getM_Product_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Activity))
//				C_Activity_ID = sourceAcct.getC_Activity_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_LocationFrom))
//				C_LocFrom_ID = sourceAcct.getC_LocFrom_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_LocationTo))
//				C_LocTo_ID = sourceAcct.getC_LocTo_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Campaign))
//				C_Campaign_ID = sourceAcct.getC_Campaign_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_OrgTrx))
//				AD_OrgTrx_ID = sourceAcct.getAD_OrgTrx_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Project))
//				C_Project_ID = sourceAcct.getC_Project_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_SalesRegion))
//				C_SalesRegion_ID = sourceAcct.getC_SalesRegion_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList1))
//				User1_ID = sourceAcct.getUser1_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList2))
//				User2_ID = sourceAcct.getUser2_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserColumn1))
//				UserElement1_ID = sourceAcct.getUserElement1_ID();
//			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserColumn2))
//				UserElement2_ID = sourceAcct.getUserElement2_ID();
//			//	No UserElement
//		}
//		//
//		retAccount = MAccount.get(Env.getCtx(), AD_Client_ID, AD_Org_ID,
//			C_AcctSchema_ID, Account_ID, C_SubAcct_ID,
//			M_Product_ID, C_BPartner_ID, AD_OrgTrx_ID,
//			C_LocFrom_ID, C_LocTo_ID, C_SalesRegion_ID, 
//			C_Project_ID, C_Campaign_ID, C_Activity_ID,
//			User1_ID, User2_ID, UserElement1_ID, UserElement2_ID,
//			get_TrxName());
////log.warning("retAccount="+retAccount);
//		return retAccount;
//	}	//	createAccount
	
	
	/**
	 * Return the main transaction of the current process.
	 * @return the transaction name
	 */
	public static String get_TrxName()
	{
		if (m_trx != null)
			return m_trx.getTrxName();
		return null;
	}	//	get_TrxN
	
	
	/**
	 * 	Get Acct Info list 
	 *	@return list
	 */
	public static ArrayList<KeyNamePair> getMProductAcctInfo(X_M_Product_Acct mpa)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < mpa.get_ColumnCount(); i++)
		{
			String columnName = mpa.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( mpa.get_Value(i)!= null)
					id =  ((Integer)mpa.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" mpa.get_Value(i)="+mpa.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getAcctInfo
	
	/**
	 * 	Get Acct Info list 
	 *	@return list
	 */
	public static ArrayList<KeyNamePair> getMProductCategoryAcctInfo(X_M_Product_Category_Acct mpca)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < mpca.get_ColumnCount(); i++)
		{
			String columnName = mpca.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( mpca.get_Value(i)!= null)
					id =  ((Integer)mpca.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" mpca.get_Value(i)="+mpca.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getAcctInfo
	
	/**
	 * 
	 * @param mwa
	 * @return
	 */
	public static ArrayList<KeyNamePair> getMWarehouseAcctInfo(X_M_Warehouse_Acct mwa)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < mwa.get_ColumnCount(); i++)
		{
			String columnName = mwa.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( mwa.get_Value(i)!= null)
					id =  ((Integer)mwa.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" mwa.get_Value(i)="+mwa.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getAcctInfo
	
	/**
	 * getM_Product_Category_Acct
	 * @param ctx
	 * @param C_AcctSchema_ID
	 * @param M_Product_Category_ID
	 * @return AMRMProductCategoryAcct (X_M_Product_Category Extends)
	 */

	public static X_M_Product_Category_Acct getM_Product_Category_Acct (Properties ctx, int C_AcctSchema_ID, int M_Product_Category_ID)
	{
log.warning("C_AcctSchema_ID="+C_AcctSchema_ID+" M_Product_Category_ID="+M_Product_Category_ID);
		X_M_Product_Category_Acct retValue = null;
//		final String whereClause = "C_AcctSchema_ID=? AND M_Product_Category_ID=?";
//		retValue =  new Query(ctx,MProductCategory.Table_Name,whereClause,null)
//		.setParameters(C_AcctSchema_ID,M_Product_Category_ID)
//		.firstOnly();
//		 return retValue;

		final String whereClause = "C_AcctSchema_ID="+C_AcctSchema_ID+" AND M_Product_Category_ID="+M_Product_Category_ID;
		retValue =  new Query(ctx,X_M_Product_Category_Acct.Table_Name,whereClause,null)
//		.setParameters(C_AcctSchema_ID,M_Product_Category_ID)
		.firstOnly();
		 return retValue;

	
	}	//	get
	

	/**
	 * getM_Product_Acct
	 * @param ctx
	 * @param C_AcctSchema_ID
	 * @param M_Product_Category_ID
	 * @return AMRMProductAcct (X_M_Product Extends)
	 */

	public static X_M_Product_Acct getM_Product_Acct (Properties ctx, int C_AcctSchema_ID, int M_Product_ID)
	{
		X_M_Product_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND M_Product_ID=?";
		retValue =  new Query(ctx,X_M_Product_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,M_Product_ID)
		.firstOnly();
		return retValue;
	}	//	get
	
	public static X_M_Warehouse_Acct getM_Warehouse_Acct (Properties ctx, int C_AcctSchema_ID, int M_Warehouse_ID)
	{
		X_M_Warehouse_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND M_Warehouse_ID=?";
		retValue =  new Query(ctx,X_M_Warehouse_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,M_Warehouse_ID)
		.firstOnly();
		return retValue;
	}	//	get
	
	/** 
	 * copyMCost
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private  void copyMCost () throws Exception
	{
		// Get Cost
		MCostElement[] ce= MCostElement.getElements(Env.getCtx(), "Mcost");
		// M_product
		MProduct mprod = new MProduct(Env.getCtx(), M_Product_ID,null);
		//		
		for (int i = 0; i < ce.length; i++)
		{			
			MCostElement cel = ce[i];
			MAcctSchema as = MAcctSchema.get (Env.getCtx(), SourceAcctSchema_ID, null);
			MAcctSchema astarget = MAcctSchema.get (Env.getCtx(), TargetAcctSchema_ID, null);
			// get MCost Source
			MCost mc = MCost.get (mprod, mprod.getM_AttributeSetInstance_ID(),
					as, 0, cel.getM_CostElement_ID(), mprod.get_TrxName());
			// Get or Create MCost
			MCost mctarget = MCost.get (mprod, mprod.getM_AttributeSetInstance_ID(),
					astarget, 0, cel.getM_CostElement_ID(), mprod.get_TrxName());
			// Update Values
			//DB.getDatabase().forUpdate(mctarget, 120);
			mctarget.setC_AcctSchema_ID(TargetAcctSchema_ID);
			mctarget.setAD_Org_ID(mc.getAD_Org_ID());
			mctarget.setM_Product_ID(mprod.getM_Product_ID());
			mctarget.setM_CostElement_ID(mc.getM_CostElement_ID());
			mctarget.setM_CostType_ID(mc.getM_CostType_ID());
			// 
			mctarget.setCurrentCostPrice(convertAmount(mc.getCurrentCostPrice()));
			mctarget.setCurrentCostPriceLL(convertAmount(mc.getCurrentCostPriceLL()));
			mctarget.setCurrentQty(mc.getCurrentQty());
			// IF 0.00 Then 0.01
			if (mctarget.getCurrentCostPrice().compareTo(BigDecimal.valueOf(0.01)) < 0
					|| mctarget.getCurrentCostPrice().compareTo(BigDecimal.ZERO)== 0) {
				mctarget.setCurrentCostPrice(BigDecimal.valueOf(0.01));
				mctarget.setCumulatedAmt(mctarget.getCumulatedQty().multiply(BigDecimal.valueOf(0.01)));
			} else {
				mctarget.setCumulatedAmt(convertAmount(mc.getCumulatedAmt()));				
			}
			mctarget.setCumulatedQty(mc.getCumulatedQty());
			mctarget.setFutureCostPrice(convertAmount(mc.getFutureCostPrice()));
			mctarget.setFutureCostPriceLL(convertAmount(mc.getFutureCostPriceLL()));
			mctarget.setIsCostFrozen(mc.isCostFrozen());
			// LOcal MCostSave
			mctarget.save();	
//log.warning("Product="+mprod.getValue()+"  M_CostElement_ID="+mc.getM_CostElement_ID()+" cost="+mc.getCurrentCostPrice()+"  New="+mctarget.getCurrentCostPrice()+"  ConvFactorMultiply="+getConvFactorMultiply());
		}

	}	//	copyMCost
	
	
//	/**
//	 * 
//	 * @throws Exception
//	 */
//	private  void copyMCostDetail () throws Exception
//	{
//		// Get Cost
//		MCostElement[] ce= MCostElement.getElements(Env.getCtx(), "Mcost");
//		// M_product
//		MM_Product mprod = new MM_Product(Env.getCtx(), M_Product_ID,null);
//
//		//
//		MM_MCostDetail[] mcd = getMCostDetailForProduct(Env.getCtx(), M_Product_ID, SourceAcctSchema_ID, "McostDetail");
//		//
//		for (int i = 0; i < mcd.length; i++)
//		{			
//
//		}
//
////log.warning("targetSave="+targetSave);
//	}	//	copyMCostDetail
//	
	
	/**
	 * getProductRecs
	 * @param AD_Client_ID
	 * @return
	 */
	@SuppressWarnings("null")
	public static int getProductRecs (int AD_Client_ID )
	{
		//
		Integer noProductRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM M_Product ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				noProductRecords = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		if ( noProductRecords == null ) {
			noProductRecords = 0;
		}
		//log.warning("noMcostRecords:"+noProductRecords);
		return noProductRecords;
	}	//	getProductRecs
	
	/**
	 * 
	 * @param AD_Client_ID
	 * @return
	 */
	public static int getWarehouseRecs (int AD_Client_ID )
	{
		//
		Integer noWHRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM M_Warehouse ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				noWHRecords = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		if ( noWHRecords == null ) {
			noWHRecords = 0;
		}
		//log.warning("noWHRecords:"+noWHRecords);
		return noWHRecords;
	}	//	getProductRecs
	/**
	 * getProductCategoryRecs
	 * @param AD_Client_ID
	 * @return
	 */
	@SuppressWarnings("null")
	public static int getProductCategoryRecs (int AD_Client_ID )
	{
		//
		Integer noProductCatRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM M_Product_Category ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				noProductCatRecords = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		if ( noProductCatRecords == null ) {
			noProductCatRecords = 0;
		}
		//log.warning("noMcostRecords:"+noProductRecords);
		return noProductCatRecords;
	}	//	getProductCategoryRecs
	
//	/**
//	 * getMCostForProduct
//	 * Get MCost for Product
//	 * @param ctx context
//	 * @param M_Product_ID
//	 * @param trxName  
//	 * @return array of locations 
//	 */
//	public static MM_MCost[] getMCostForProduct(Properties ctx,
//			int M_Product_ID, int C_AcctSchema_ID, String trxName) {
//		final String whereClause = "M_Product_ID=? AND C_AcctSchema_ID=?";
//		List<MCost> list = new Query(ctx, "M_Cost",
//				whereClause, trxName).setParameters(M_Product_ID,C_AcctSchema_ID).list();
//		MM_MCost[] retValue = new MM_MCost[list.size()];
//		list.toArray(retValue);
//		return retValue;
//	} // getMCostForProduct

//	/**
//	 * getMCostDetailForProduct
//	 * Get MCost for Product
//	 * @param ctx context
//	 * @param M_Product_ID
//	 * @param trxName  
//	 * @return array of locations 
//	 */
//	public static MM_MCostDetail[] getMCostDetailForProduct(Properties ctx,
//			int M_Product_ID, int C_AcctSchema_ID, String trxName) {
//		final String whereClause = "M_Product_ID=? AND C_AcctSchema_ID=?";
//		List<MCost> list = new Query(ctx, "M_CostDetail",
//				whereClause, trxName).setParameters(M_Product_ID,C_AcctSchema_ID).list();
//		MM_MCostDetail[] retValue = new MM_MCostDetail[list.size()];
//		list.toArray(retValue);
//		return retValue;
//	} // getMCostForProduct
	
//	/**
//	 * saveMCost
//	 * @param mcost
//	 * @return I Insert U Update D delete E Error
//	 */
//	public String saveMCost(MM_MCost mcost)
//	{
//		//
//		StringBuilder sql = null ;
//		String retValue="";
//		//
//		MM_MCost mc = null;
//		mc = (MM_MCost) MM_MCost.get (Env.getCtx(), AD_Client_ID, mcost.getAD_Org_ID(), mcost.getM_Product_ID(),
//				mcost.getM_CostType_ID(), mcost.getC_AcctSchema_ID(),mcost.getM_CostElement_ID(),
//					mcost.getM_AttributeSetInstance_ID(),
//					null);
//		if (mc == (null)) { 
//			//	CREATE NEW M_Cost Record With New Reconverted Values
//			UUID uuid = UUID.randomUUID();
//			
//			sql= new StringBuilder("INSERT INTO M_Cost (")
//				.append("ad_client_id, ad_org_id, m_product_id, m_costtype_id, c_acctschema_id, ")
//				.append("m_costelement_id, m_attributesetinstance_id, isactive, created, ")
//				.append("createdby, updated, updatedby, currentcostprice, currentqty, ")
//				.append("cumulatedamt, cumulatedqty, futurecostprice, description, percent, ")
//				.append("currentcostpricell, futurecostpricell, iscostfrozen, m_cost_uu) VALUES (")
//				.append(AD_Client_ID).append(",")
//				.append(mcost.getAD_Org_ID()).append(", ")
//				.append(mcost.getM_Product_ID()).append(", ")
//				.append(mcost.getM_CostType_ID()).append(", ")
//				.append(mcost.getC_AcctSchema_ID()).append(", ")
//				.append(mcost.getM_CostElement_ID()).append(", ")
//				.append(mcost.getM_AttributeSetInstance_ID()).append(", ")
//				.append(" 'Y', SysDate, ")
//				.append(Env.getAD_User_ID(Env.getCtx())).append(", ")
//				.append(" SysDate, ")
//				.append(Env.getAD_User_ID(Env.getCtx())).append(", ")
//				.append(mcost.getCurrentCostPrice()).append(", ")
//				.append(mcost.getCurrentQty()).append(", ")
//				.append(mcost.getCumulatedAmt()).append(", ")
//				.append(mcost.getCumulatedQty()).append(", ")
//				.append(mcost.getFutureCostPrice()).append(", ")
//				.append(mcost.getDescription()).append(", ")
//				.append(mcost.getPercent()).append(",")
//				.append(mcost.getCurrentCostPriceLL()).append(",")
//				.append(mcost.getFutureCostPriceLL()).append(",'N', ")
//				.append("'"+uuid.toString().trim()+"')");
//			int no = DB.executeUpdate(sql.toString(), m_trxName);
//			if (no == 1) {
//				retValue="I";
//				setInsertM_Cost(getInsertM_Cost().add(BigDecimal.ONE));
//			} else {
//				retValue="E";
//				setUpdateM_Cost(getErrorM_Cost().add(BigDecimal.ONE));
//			}
//			return retValue;
//		} else {
//			// UPDATE Exists M_Cost Record With New Reconverted Values
//			mc.setCurrentCostPrice(convertAmount(mcost.getCurrentCostPrice()));
//			mc.setCurrentCostPriceLL(convertAmount(mcost.getCurrentCostPriceLL()));
//			mc.setCurrentQty(mcost.getCurrentQty());
//			mc.setCumulatedAmt(convertAmount(mcost.getCumulatedAmt()));
//			mc.setCumulatedQty(mcost.getCumulatedQty());
//			mc.setFutureCostPrice(convertAmount(mcost.getFutureCostPrice()));
//			mc.setFutureCostPriceLL(convertAmount(mcost.getFutureCostPriceLL()));
//			if (mc.save()) {
//				retValue="U";
//				setUpdateM_Cost(getUpdateM_Cost().add(BigDecimal.ONE));
//			} else {
//				retValue="E";
//				setUpdateM_Cost(getErrorM_Cost().add(BigDecimal.ONE));
//			}
//			return retValue;
//		}
//	}
	
	/**
	 * setConversionRates
	 * @param ctx
	 * @param SourceCurrency_ID
	 * @param TargetCurrency_ID
	 * @param trxName
	 * @return
	 */
	public BigDecimal setConversionRates(Properties ctx, int SourceCurrency_ID, 
			int TargetCurrency_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;
		int C_Conversion_Rate_ID = 0;
		//log.warning("C_Currency_ID="+TargetCurrency_ID+"  C_Currency_ID_TO Rate="+SourceCurrency_ID);
		StringBuilder sql = new StringBuilder("SELECT C_Conversion_Rate_ID FROM C_Conversion_Rate ")
		.append(" WHERE c_currency_ID ="+SourceCurrency_ID)
		.append(" AND c_currency_id_to= "+ TargetCurrency_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//log.warning("sql="+sql.toString());
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				C_Conversion_Rate_ID = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		MConversionRate mconvrtd = new MConversionRate(Env.getCtx(), C_Conversion_Rate_ID,null);
		retValue=mconvrtd.getMultiplyRate();
		setConvFactorMultiply(mconvrtd.getMultiplyRate());
		setConvFactorDivide(mconvrtd.getDivideRate());
		setC_Conversion_Rate_ID(mconvrtd.getC_Conversion_Rate_ID());
		//log.warning("C_Conversion_Rate_ID="+C_Conversion_Rate_ID+"  Conversion Rate="+retValue);
		return retValue;
	}
	
	public BigDecimal convertAmount(BigDecimal Amount) {
		
		BigDecimal retValue = BigDecimal.ZERO;
		if (Amount.compareTo(BigDecimal.ZERO) != 0 ) {
			retValue=Amount.multiply(getConvFactorMultiply());
		}
		return retValue;
		}
	
	public ProcessInfo getM_pi() {
		return m_pi;
	}

	public void setM_pi(ProcessInfo m_pi) {
		this.m_pi = m_pi;
	}

	public MClient getM_client() {
		return m_client;
	}

	public void setM_client(MClient m_client) {
		this.m_client = m_client;
	}

	public int getAD_Client_ID() {
		return AD_Client_ID;
	}

	public void setAD_Client_ID(int aD_Client_ID) {
		AD_Client_ID = aD_Client_ID;
	}

	public int getSourceAcctSchema_ID() {
		return SourceAcctSchema_ID;
	}

	public void setSourceAcctSchema_ID(int sourceAcctSchema_ID) {
		SourceAcctSchema_ID = sourceAcctSchema_ID;
	}

	public MAcctSchema getSourceAS() {
		return SourceAS;
	}

	public void setSourceAS(MAcctSchema sourceAS) {
		SourceAS = sourceAS;
	}

	public int getTargetAcctSchema_ID() {
		return TargetAcctSchema_ID;
	}

	public void setTargetAcctSchema_ID(int targetAcctSchema_ID) {
		TargetAcctSchema_ID = targetAcctSchema_ID;
	}

	public MAcctSchema getTargetAS() {
		return TargetAS;
	}

	public void setTargetAS(MAcctSchema targetAS) {
		TargetAS = targetAS;
	}

	public int getSourceCurrency_ID() {
		return SourceCurrency_ID;
	}

	public void setSourceCurrency_ID(int sourceCurrency_ID) {
		SourceCurrency_ID = sourceCurrency_ID;
	}

	public int getTargetCurrency_ID() {
		return TargetCurrency_ID;
	}

	public void setTargetCurrency_ID(int targetCurrency_ID) {
		TargetCurrency_ID = targetCurrency_ID;
	}

	public MCurrency getSourceCurr() {
		return sourceCurr;
	}

	public void setSourceCurr(MCurrency sourceCurr) {
		this.sourceCurr = sourceCurr;
	}

	public MCurrency getTargetCurr() {
		return targetCurr;
	}

	public void setTargetCurr(MCurrency targetCurr) {
		this.targetCurr = targetCurr;
	}

	public BigDecimal getConvFactorMultiply() {
		return convFactorMultiply;
	}

	public void setConvFactorMultiply(BigDecimal convFactorMultiply) {
		this.convFactorMultiply = convFactorMultiply;
	}

	public BigDecimal getConvFactorDivide() {
		return convFactorDivide;
	}

	public void setConvFactorDivide(BigDecimal convFactorDivide) {
		this.convFactorDivide = convFactorDivide;
	}
	public int getM_Product_ID() {
		return M_Product_ID;
	}

	public void setM_Product_ID(int m_Product_ID) {
		M_Product_ID = m_Product_ID;
	}

	public int getC_Conversion_Rate_ID() {
		return C_Conversion_Rate_ID;
	}

	public void setC_Conversion_Rate_ID(int c_Conversion_Rate_ID) {
		C_Conversion_Rate_ID = c_Conversion_Rate_ID;
	}

	public void resetCounters() {
		setInsertM_Cost(BigDecimal.ZERO);
		setUpdateM_Cost(BigDecimal.ZERO);
		setErrorM_Cost(BigDecimal.ZERO);
	}
	
	public BigDecimal getInsertM_Cost() {
		return insertM_Cost;
	}

	public void setInsertM_Cost(BigDecimal insertM_Cost) {
		this.insertM_Cost = insertM_Cost;
	}

	public BigDecimal getUpdateM_Cost() {
		return updateM_Cost;
	}

	public void setUpdateM_Cost(BigDecimal updateM_Cost) {
		this.updateM_Cost = updateM_Cost;
	}

	public BigDecimal getErrorM_Cost() {
		return errorM_Cost;
	}

	public void setErrorM_Cost(BigDecimal errorM_Cost) {
		this.errorM_Cost = errorM_Cost;
	}
	
	
	/**
	 * 	Get Info
	 *	@return Info
	 */
	public String getInfo()
	{
		return m_info.toString();
	}

}

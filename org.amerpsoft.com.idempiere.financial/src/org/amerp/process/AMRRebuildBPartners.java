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
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.model.Query;
import org.compiere.model.X_C_BP_Customer_Acct;
import org.compiere.model.X_C_BP_Employee_Acct;
import org.compiere.model.X_C_BP_Group;
import org.compiere.model.X_C_BP_Group_Acct;
import org.compiere.model.X_C_BP_Vendor_Acct;
import org.compiere.model.X_C_BPartner;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class AMRRebuildBPartners {

	/**	Logger			*/
	protected static CLogger	log = CLogger.getCLogger(AMRRebuildBPartners.class);
	private ProcessInfo			m_pi;	// Process Info ID
	private static Trx		m_trx = Trx.get(Trx.createTrxName("Setup"), true);
	private static StringBuffer m_info = new StringBuffer();
	//
	private MClient			m_client;
	private int AD_Client_ID;
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
	// Conversion Factor
	private BigDecimal convFactorMultiply;
	// 
	private int C_BPartner_ID = 0;
	private int C_BP_Group_ID = 0;
	// BPartner Count Vars
	int BPartner_Count = 0 ;
	int BPartnerNo = 0;
	// BPartner Groups Count Vars
	int BPGroup_Count = 0 ;
	int BPGroupNo = 0;
	int Percent = 0;
	// BPartner
	BigDecimal insertC_BPartner=BigDecimal.ZERO;
	BigDecimal updateC_BPartner=BigDecimal.ZERO;
	BigDecimal errorC_BPartner=BigDecimal.ZERO;
	// BPartner Groups
	BigDecimal insertC_BPGroup=BigDecimal.ZERO;
	BigDecimal updateC_BPGroup=BigDecimal.ZERO;
	BigDecimal errorC_BPGroup=BigDecimal.ZERO;

	/**
	 * dupC_BPartner_Acct
	 * @return
	 * @throws Exception
	 */
	public boolean dupC_BPartner_Acct () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(X_C_BPartner.Table_Name+"(s):").append("\n");
		BPartner_Count = getBPartnerRecs(getAD_Client_ID());
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		BPartnerNo = BPartnerNo+1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT C_BPartner_ID, Value, Name ")	//	1	
			.append(" FROM C_BPartner ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				C_BPartner_ID = rs.getInt(1);
log.warning("BPValue="+rs.getString(2)+"  BPName="+rs.getString(3));		
				// getTargetAcctSchema_ID()
				// Percentage Monitor
				BPartnerNo = BPartnerNo+1;
				if (BPartner_Count != 0 ) {
					Percent =  (BPartnerNo * 100 /BPartner_Count);
				} else {
					Percent = 100;
					BPartnerNo=1;
				}
				// Copy Acct records
				copyC_BP_Customer_Acct(C_BPartner_ID, SourceAS, TargetAS);
				copyC_BP_Vendor_Acct(C_BPartner_ID, SourceAS, TargetAS);
				copyC_BP_Employee_Acct(C_BPartner_ID, SourceAS, TargetAS);
//				if(BPartnerNo==5)
//				break;
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
//log.warning("noBPRecords:"+BPartner_Count);
		return false;
		
	}
	
	/**
	 * dupC_BP_Group_Acct
	 * @return
	 * @throws Exception
	 */
	public boolean dupC_BP_Group_Acct () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(X_C_BP_Group.Table_Name+"(s):").append("\n");
		BPGroup_Count = getBPGroupsRecs(getAD_Client_ID());
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		BPGroupNo = BPGroupNo+1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT C_BP_Group_ID, Value, Name ")	//	1	
			.append(" FROM C_BP_Group ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				C_BP_Group_ID = rs.getInt(1);
				//log.warning("BP Group="+rs.getString(2)+"-"+rs.getString(3));		
				BPGroupNo = BPGroupNo+1;
				if (BPGroup_Count != 0 ) {
					Percent =  (BPGroupNo * 100 /BPGroup_Count);
				} else {
					Percent = 100;
					BPGroupNo=1;
				}
				// Copy Prodiuct Category Accounting
				copyC_BP_Group_Acct(C_BP_Group_ID, SourceAS, TargetAS);
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
		return false;
	}
	
	/**
	 * copyC_BP_Customer_Acct
	 * @param C_BPartner_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyC_BP_Customer_Acct(int C_BPartner_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MBPartner bpv = new MBPartner(Env.getCtx(),C_BPartner_ID,null);
		X_C_BP_Customer_Acct source = getC_BP_Customer_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_BPartner_ID);
		X_C_BP_Customer_Acct target = getC_BP_Customer_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_BPartner_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(bpv.getAD_Client_ID()) + ","+String.valueOf(bpv.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_BPartner_ID="+source.getC_BPartner_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_BPartner_ID="+target.getC_BPartner_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getCBPCustomerAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_BP_Customer_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_BPartner_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_BPartner_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_BP_Customer_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_BP_Customer_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_BPartner_ID="+C_BPartner_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_BPartnerr_ID")).append("(Cus)=").append(bpv.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_BP_Customer="+bpv.getValue()+" Default in AcctSchemaElement NOT updated");

	}	//	copyC_BP_Customer_Acct
	
	/**
	 *  copyC_BP_Vendor_Acct
	 * @param C_BPartner_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyC_BP_Vendor_Acct(int C_BPartner_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MBPartner bpv = new MBPartner(Env.getCtx(),C_BPartner_ID,null);
		X_C_BP_Vendor_Acct source = getC_BP_Vendor_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_BPartner_ID);
		X_C_BP_Vendor_Acct target = getC_BP_Vendor_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_BPartner_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(bpv.getAD_Client_ID()) + ","+String.valueOf(bpv.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_BPartner_ID="+source.getC_BPartner_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_BPartner_ID="+target.getC_BPartner_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getCBPVendorAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_BP_Vendor_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_BPartner_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_BPartner_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_BP_Vendor_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_BP_Vendor_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_BPartner_ID="+C_BPartner_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_BPartner_ID")).append("(Ven)=").append(bpv.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_BP_Vendor="+bpv.getValue()+" Default in AcctSchemaElement NOT updated");
	}	//	copyC_BP_Vendor_Acct
	
	/**
	 *  copyC_BP_Employee_Acct
	 * @param C_BPartner_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyC_BP_Employee_Acct(int C_BPartner_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MBPartner bpe = new MBPartner(Env.getCtx(),C_BPartner_ID,null);
		X_C_BP_Employee_Acct source = getC_BP_Employee_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_BPartner_ID);
		X_C_BP_Employee_Acct target = getC_BP_Employee_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_BPartner_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(bpe.getAD_Client_ID()) + ","+String.valueOf(bpe.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_BPartner_ID="+source.getC_BPartner_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_BPartner_ID="+target.getC_BPartner_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getCBPEmployeeAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_BP_Employee_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_BPartner_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_BPartner_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_BP_Employee_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_BP_Employee_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_BPartner_ID="+C_BPartner_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_BPartner_ID")).append("(Emp)=").append(bpe.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_BP_Employee="+bpe.getValue()+" Default in AcctSchemaElement NOT updated");
	}	//	copyC_BP_Employee_Acct

	/**
	 *  copyC_BP_Group_Acct
	 * @param C_BP_Group_ID
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyC_BP_Group_Acct(int C_BP_Group_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MBPGroup bpg = new MBPGroup(Env.getCtx(),C_BP_Group_ID,null);
		X_C_BP_Group_Acct source = getC_BP_Group_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_BP_Group_ID);
		X_C_BP_Group_Acct target = getC_BP_Group_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_BP_Group_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(bpg.getAD_Client_ID()) + ","+String.valueOf(bpg.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_BP_Group_ID="+source.getC_BP_Group_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_BP_Group_ID="+target.getC_BP_Group_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getCBPGroupAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_BP_Group_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_BP_Group_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_BP_Group_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_BP_Group_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_BP_Group_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_BP_Group_ID="+C_BP_Group_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_BP_Group_ID")).append("=").append(bpg.getValue());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_BP_Group="+bpg.getValue()+" Default in AcctSchemaElement NOT updated");
	}	//	copyC_BP_Group_Acct

	
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
	 * getC_BP_Customer_Acct
	 * @param ctx
	 * @param C_AcctSchema_ID
	 * @param C_BPartner_ID
	 * @return AMRCBPCustomerAcct (X_C_BP_Customer_Acct Extends)
	 */
	public static X_C_BP_Customer_Acct getC_BP_Customer_Acct (Properties ctx, int C_AcctSchema_ID, int C_BPartner_ID)
	{
		X_C_BP_Customer_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_BPartner_ID=?";
		retValue=new Query(ctx,X_C_BP_Customer_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_BPartner_ID)
		.firstOnly();
		return retValue;
	}	//	getC_BP_Customer_Acct
	
	/**
	 * getCBPCustomerAcctInfo
	 * @param cbpc
	 * @return
	 */
	public static ArrayList<KeyNamePair> getCBPCustomerAcctInfo(X_C_BP_Customer_Acct cbpc)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < cbpc.get_ColumnCount(); i++)
		{
			String columnName = cbpc.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( cbpc.get_Value(i)!= null)
					id =  ((Integer)cbpc.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" cbpc.get_Value(i)="+cbpc.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getCBPVendorAcctInfo
	
	/**
	 * getC_BP_Vendor_Acct
	 * @param ctx
	 * @param C_AcctSchema_ID
	 * @param C_BPartner_ID
	 * @return AMRCBPVendorAcct (X_C_BP_Vendor_Acct Extends)
	 */
	public static X_C_BP_Vendor_Acct getC_BP_Vendor_Acct (Properties ctx, int C_AcctSchema_ID, int C_BPartner_ID)
	{
		X_C_BP_Vendor_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_BPartner_ID=?";
		retValue=new Query(ctx,X_C_BP_Vendor_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_BPartner_ID)
		.firstOnly();
		return retValue;
	}	//	get
	
	/**
	 * getCBPVendorAcctInfo
	 * @param cbpv
	 * @return
	 */
	public static ArrayList<KeyNamePair> getCBPVendorAcctInfo(X_C_BP_Vendor_Acct cbpv)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < cbpv.get_ColumnCount(); i++)
		{
			String columnName = cbpv.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( cbpv.get_Value(i)!= null)
					id =  ((Integer)cbpv.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" cbpv.get_Value(i)="+cbpv.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getCBPVendorAcctInfo
	
	/**
	 * getC_BP_Employee_Acct
	 * @param ctx
	 * @param C_AcctSchema_ID
	 * @param C_BPartner_ID
	 * @return X_C_BP_Employee_Acct (X_C_BP_Employee_Acct Extends)
	 */
	public static X_C_BP_Employee_Acct getC_BP_Employee_Acct (Properties ctx, int C_AcctSchema_ID, int C_BPartner_ID)
	{
		X_C_BP_Employee_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_BPartner_ID=?";
		retValue=new Query(ctx,X_C_BP_Employee_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_BPartner_ID)
		.firstOnly();
		return retValue;
	}	//	getC_BP_Employee_Acct
	
	/**
	 * getCBPEmployeeAcctInfo
	 * @param cbpe
	 * @return
	 */
	public static ArrayList<KeyNamePair> getCBPEmployeeAcctInfo(X_C_BP_Employee_Acct cbpe)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < cbpe.get_ColumnCount(); i++)
		{
			String columnName = cbpe.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( cbpe.get_Value(i)!= null)
					id =  ((Integer)cbpe.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" cbpg.get_Value(i)="+cbpg.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getCBPEmployeeAcctInfo
	
	/**
	 * getC_BP_Group_Acct
	 * @param ctx
	 * @param C_AcctSchema_ID
	 * @param C_BP_Group_ID
	 * @return X_C_BP_Group_Acct (X_C_BP_Employee_Acct Extends)
	 */
	public static X_C_BP_Group_Acct getC_BP_Group_Acct (Properties ctx, int C_AcctSchema_ID, int C_BP_Group_ID)
	{
		X_C_BP_Group_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_BP_Group_ID=?";
		retValue=new Query(ctx,X_C_BP_Group_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_BP_Group_ID)
		.firstOnly();
		return retValue;
		
	}	//	getC_BP_Group_Acct

	/**
	 * getCBPGroupAcctInfo
	 * @param cbpg
	 * @return
	 */
	public static ArrayList<KeyNamePair> getCBPGroupAcctInfo(X_C_BP_Group_Acct cbpg)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < cbpg.get_ColumnCount(); i++)
		{
			String columnName = cbpg.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( cbpg.get_Value(i)!= null)
					id =  ((Integer)cbpg.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" cbpg.get_Value(i)="+cbpg.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getCBPGroupAcctInfo
	
	/**
	 * getBPartnerRecs
	 * @param AD_Client_ID
	 * @return
	 */
	@SuppressWarnings("null")
	public static int getBPartnerRecs (int AD_Client_ID )
	{
		//
		Integer noBPartnerRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_BPartner ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				noBPartnerRecords = rs.getInt(1);
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
		if ( noBPartnerRecords == null ) {
			noBPartnerRecords = 0;
		}
		//log.warning("noBPartnerRecords:"+noBPartnerRecords);
		return noBPartnerRecords;
	}	//	getBPartnerRecs
	
	/**
	 * getBPGroupsRecs
	 * @param AD_Client_ID
	 * @return
	 */
	@SuppressWarnings("null")
	public static int getBPGroupsRecs (int AD_Client_ID )
	{
		//
		Integer noBPGroupRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_BP_Group ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				noBPGroupRecords = rs.getInt(1);
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
		if ( noBPGroupRecords == null ) {
			noBPGroupRecords = 0;
		}
		//log.warning("noBPGroupRecords:"+noBPGroupRecords);
		return noBPGroupRecords;
	}	//	getBPGroupsRecs

	public void resetCounters() {
		// Reset Counters for C_BPartners and Groups
		setInsertC_BPartner(BigDecimal.ZERO);
		setUpdateC_BPartner(BigDecimal.ZERO);
		setErrorC_BPartner(BigDecimal.ZERO);
		setInsertC_BPGroup(BigDecimal.ZERO);
		setUpdateC_BPGroup(BigDecimal.ZERO);
		setErrorC_BPGroup(BigDecimal.ZERO);
	}
	
	
	public BigDecimal getInsertC_BPartner() {
		return insertC_BPartner;
	}

	public void setInsertC_BPartner(BigDecimal insertC_BPartner) {
		this.insertC_BPartner = insertC_BPartner;
	}

	public BigDecimal getUpdateC_BPartner() {
		return updateC_BPartner;
	}

	public void setUpdateC_BPartner(BigDecimal updateC_BPartner) {
		this.updateC_BPartner = updateC_BPartner;
	}

	public BigDecimal getErrorC_BPartner() {
		return errorC_BPartner;
	}

	public void setErrorC_BPartner(BigDecimal errorC_BPartner) {
		this.errorC_BPartner = errorC_BPartner;
	}

	public BigDecimal getInsertC_BPGroup() {
		return insertC_BPGroup;
	}

	public void setInsertC_BPGroup(BigDecimal insertC_BPGroup) {
		this.insertC_BPGroup = insertC_BPGroup;
	}

	public BigDecimal getUpdateC_BPGroup() {
		return updateC_BPGroup;
	}

	public void setUpdateC_BPGroup(BigDecimal updateC_BPGroup) {
		this.updateC_BPGroup = updateC_BPGroup;
	}

	public BigDecimal getErrorC_BPGroup() {
		return errorC_BPGroup;
	}

	public void setErrorC_BPGroup(BigDecimal errorC_BPGroup) {
		this.errorC_BPGroup = errorC_BPGroup;
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

	/**
	 * 	Get Info
	 *	@return Info
	 */
	public String getInfo()
	{
		return m_info.toString();
	}
}

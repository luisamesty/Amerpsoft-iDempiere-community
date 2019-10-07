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
import org.compiere.model.MAsset;
import org.compiere.model.MAssetGroup;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCharge;
import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.model.MProject;
import org.compiere.model.MTax;
import org.compiere.model.Query;
import org.compiere.model.X_A_Asset;
import org.compiere.model.X_A_Asset_Acct;
import org.compiere.model.X_A_Asset_Group;
import org.compiere.model.X_A_Asset_Group_Acct;
import org.compiere.model.X_C_BankAccount;
import org.compiere.model.X_C_BankAccount_Acct;
import org.compiere.model.X_C_Charge;
import org.compiere.model.X_C_Charge_Acct;
import org.compiere.model.X_C_Project;
import org.compiere.model.X_C_Project_Acct;
import org.compiere.model.X_C_Tax;
import org.compiere.model.X_C_Tax_Acct;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class AMRRebuildBankAccounting {

	/**	Logger			*/
	protected static CLogger	log = CLogger.getCLogger(AMRRebuildBankAccounting.class);
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
	// 
	private int C_Charge_ID = 0;
	private int C_Tax_ID = 0;
	private int C_BankAccount_ID = 0;
	private static int A_Asset_Acct_ID=0;
	private static int A_Asset_Group_Acct_ID=0;
	private int A_Asset_ID = 0 ;
	private int A_Asset_Group_ID = 0 ;
	private int C_Project_ID = 0 ;
	// Percent
	int Percent = 0;
	// c_charge_acct
	int Charge_Count = 0 ;
	int ChargeNo = 0;
	// c_tax_acct 
	int Tax_Count = 0 ;
	int TaxNo = 0;
	// c_bankaccount_acct
	int BankAccount_Count = 0 ;
	int BankAccountNo = 0;
	// asset_acct
	int Asset_Count = 0;
	int AssetNo = 0;
	// a_asset_group_acct
	int AssetGroup_Count = 0;
	int AssetGroupNo = 0;	
	// c_project_acct
	int Project_Count = 0;
	int ProjectNo = 0;
	
	public boolean dupC_Charge_Acct () throws Exception
	{
		m_info = new StringBuffer();
		m_info.append(X_C_Charge.Table_Name+"(s):").append("\n");
		Charge_Count = getC_ChargeRecs(getAD_Client_ID());
		// ChargeNo
		ChargeNo = ChargeNo+1;
		// Get Charge From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT C_Charge_ID, Name, Description ")	//	1	
			.append(" FROM C_Charge ")
			.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				C_Charge_ID = rs.getInt(1);
//log.warning("CHName="+rs.getString(2)+"  CNdescription="+rs.getString(3));		
				// getTargetAcctSchema_ID()
				// Percentage Monitor
				ChargeNo = ChargeNo+1;
				if (Charge_Count != 0 ) {
					Percent =  (ChargeNo * 100 /Charge_Count);
				} else {
					Percent = 100;
					ChargeNo=1;
				}
				// Copy Acct records
				copyC_Charge_Acct(C_Charge_ID, SourceAS, TargetAS);
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
//log.warning("noRecords:"+Charge_Count);
		return false;
	}
	
	public boolean dupC_Tax_Acct () throws Exception
		{
			m_info = new StringBuffer();
			m_info.append(X_C_Tax.Table_Name+"(s):").append("\n");
			Tax_Count = getC_TaxRecs(getAD_Client_ID());
			// TaxNo
			TaxNo = TaxNo+1;
			// Get Tax From AD_Client_ID
			StringBuilder sql = new StringBuilder("SELECT C_Tax_ID, Name, description ")	//	1	
				.append(" FROM C_Tax ")
				.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql.toString(), null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					C_Tax_ID = rs.getInt(1);
//	log.warning("CTName="+rs.getString(2)+"  CTDescription="+rs.getString(3));		
					// getTargetAcctSchema_ID()
					// Percentage Monitor
					TaxNo = TaxNo+1;
					if (Tax_Count != 0 ) {
						Percent =  (ChargeNo * 100 /Tax_Count);
					} else {
						Percent = 100;
						ChargeNo=1;
					}
					// Copy Acct records
					copyC_Tax_Acct(C_Tax_ID, SourceAS, TargetAS);
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
	//log.warning("noRecords:"+Tax_Count);
			return false;
		}

	public boolean dupC_BankAccount_Acct () throws Exception
		{
			m_info = new StringBuffer();
			m_info.append(X_C_BankAccount.Table_Name+"(s):").append("\n");
			BankAccount_Count = getC_BankAccountRecs(getAD_Client_ID());
			// ProductNo
			BankAccountNo = BankAccountNo+1;
			// Get Product From AD_Client_ID
			StringBuilder sql = new StringBuilder("SELECT C_BankAccount_ID, Value, Name ")	//	1	
				.append(" FROM C_BankAccount ")
				.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql.toString(), null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					C_BankAccount_ID = rs.getInt(1);
//	log.warning("CBAValue="+rs.getString(2)+"  CBAName="+rs.getString(3));		
					// getTargetAcctSchema_ID()
					// Percentage Monitor
					BankAccountNo = BankAccountNo+1;
					if (Charge_Count != 0 ) {
						Percent =  (BankAccountNo * 100 /BankAccount_Count);
					} else {
						Percent = 100;
						ChargeNo=1;
					}
					// Copy Acct records
					copyC_BankAccount_Acct(C_BankAccount_ID, SourceAS, TargetAS);
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
	//log.warning("noRecords:"+BankAccount_Count);
			return false;
		}

	public boolean dupA_Asset_Acct () throws Exception
		{
			m_info = new StringBuffer();
			m_info.append(X_A_Asset.Table_Name+"(s):").append("\n");
			Asset_Count = getA_AssetRecs(getAD_Client_ID());
			// ProductNo
			AssetNo = AssetNo+1;
			// Get Product From AD_Client_ID
			StringBuilder sql = new StringBuilder("SELECT A_Asset_ID, Value, Name ")	//	1	
				.append(" FROM A_Asset ")
				.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql.toString(), null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					A_Asset_ID = rs.getInt(1);
//	log.warning("AssetValue="+rs.getString(2)+"  AssetName="+rs.getString(3));		
					// getTargetAcctSchema_ID()
					// Percentage Monitor
					AssetNo = AssetNo+1;
					if (Asset_Count != 0 ) {
						Percent =  (AssetNo * 100 /Asset_Count);
					} else {
						Percent = 100;
						AssetNo=1;
					}
					// Copy Acct records
					copyA_Asset_Acct(A_Asset_ID, SourceAS, TargetAS);
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
	//log.warning("noRecords:"+Asset_Count);
			return false;
		}

	public boolean dupA_Asset_Group_Acct () throws Exception
		{
			m_info = new StringBuffer();
			m_info.append(X_A_Asset_Group.Table_Name+"(s):").append("\n");
			AssetGroup_Count = getA_Asset_GroupRecs(getAD_Client_ID());
			// ProductNo
			AssetGroupNo = AssetGroupNo+1;
			// Get Product From AD_Client_ID
			StringBuilder sql = new StringBuilder("SELECT A_Asset_Group_ID, Name, Description ")	//	1	
				.append(" FROM A_Asset_Group ")
				.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql.toString(), null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					A_Asset_Group_ID = rs.getInt(1);
//	log.warning("AGName="+rs.getString(2)+"  AGDescription="+rs.getString(3));		
					// getTargetAcctSchema_ID()
					// Percentage Monitor
					AssetGroupNo = AssetGroupNo+1;
					if (AssetGroup_Count != 0 ) {
						Percent =  (AssetGroupNo * 100 /AssetGroup_Count);
					} else {
						Percent = 100;
						AssetGroupNo=1;
					}
					// Copy Acct records
					copyA_Asset_Group_Acct(A_Asset_Group_ID, SourceAS, TargetAS);
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
	//log.warning("noRecords:"+Charge_Count);
			return false;
		}

	public boolean dupC_Project_Acct () throws Exception
		{
			m_info = new StringBuffer();
			m_info.append(X_C_Project.Table_Name+"(s):").append("\n");
			Project_Count = getC_ProjectRecs(getAD_Client_ID());
			// ProductNo
			ProjectNo = ProjectNo+1;
			// Get Product From AD_Client_ID
			StringBuilder sql = new StringBuilder("SELECT C_Project_ID, Value, Name ")	//	1	
				.append(" FROM C_Project ")
				.append(" WHERE AD_Client_ID= "+getAD_Client_ID());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql.toString(), null);
				rs = pstmt.executeQuery ();
				while (rs.next ())
				{
					C_Project_ID = rs.getInt(1);
//	log.warning("PRValue="+rs.getString(2)+"  PRName="+rs.getString(3));		
					// getTargetAcctSchema_ID()
					// Percentage Monitor
					ProjectNo = ProjectNo+1;
					if (Project_Count != 0 ) {
						Percent =  (ProjectNo * 100 /Project_Count);
					} else {
						Percent = 100;
						ProjectNo=1;
					}
					// Copy Acct records
					copyC_Project_Acct(C_Project_ID, SourceAS, TargetAS);
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
	//log.warning("noRecords:"+Project_Count);
			return false;
		}

	private static void copyC_Charge_Acct(int C_Charge_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MCharge cha = new MCharge(Env.getCtx(),C_Charge_ID,null);
		X_C_Charge_Acct source = getC_Charge_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_Charge_ID);
		X_C_Charge_Acct target = getC_Charge_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_Charge_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(cha.getAD_Client_ID()) + ","+String.valueOf(cha.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_Charge_ID="+source.getC_Charge_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_Charge_ID="+target.getC_Charge_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getC_Charge_AcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_Charge_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_Charge_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_Charge_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_Charge_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_Charge_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_Charge_ID="+C_Charge_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_Charge_ID")).append("=").append(cha.getName());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_Charge_ID="+cha.getName()+" Default in AcctSchemaElement NOT updated");

	}

	private static void copyC_Tax_Acct(int C_Tax_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MTax tax = new MTax(Env.getCtx(),C_Tax_ID,null);
		X_C_Tax_Acct source = getC_Tax_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_Tax_ID);
		X_C_Tax_Acct target = getC_Tax_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_Tax_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(tax.getAD_Client_ID()) + ","+String.valueOf(tax.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_Tax_ID="+source.getC_Tax_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_Tax_ID="+target.getC_Tax_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getC_Tax_AcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_Tax_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_Tax_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_Tax_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_Tax_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_Tax_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_Tax_ID="+C_Tax_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_Tax_ID")).append("=").append(tax.getName());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_Tax_ID="+tax.getName()+" Default in AcctSchemaElement NOT updated");
	}

	private static void copyC_BankAccount_Acct(int C_BankAccount_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MBankAccount mba = new MBankAccount(Env.getCtx(),C_BankAccount_ID,null);
		X_C_BankAccount_Acct source = getC_BankAccount_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_BankAccount_ID);
		X_C_BankAccount_Acct target = getC_BankAccount_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_BankAccount_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mba.getAD_Client_ID()) + ","+String.valueOf(mba.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_BankAccount_ID="+source.getC_BankAccount_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_BankAccount_ID="+target.getC_BankAccount_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getC_BankAccount_AcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_BankAccount_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_BankAccount_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_BankAccount_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_BankAccount_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_BankAccount_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_BankAccount_ID="+C_BankAccount_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
log.warning("sql="+sqlCmdI1+" "+sqlCmdI2);
		no = DB.executeUpdateEx(sqlCmdI1+" "+sqlCmdI2, null);
		if (no == 1) {
			m_info.append(Msg.translate( Env.getCtx(), "C_BankAccount_ID")).append("=").append(mba.getName());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_BankAccount_ID="+mba.getName()+" Default in AcctSchemaElement NOT updated");
	}

	private static void copyA_Asset_Acct(int A_Asset_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MAsset mas = new MAsset(Env.getCtx(),A_Asset_ID,null);
		X_A_Asset_Acct source = getA_Asset_Acct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), A_Asset_ID);
		X_A_Asset_Acct target = getA_Asset_Acct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), A_Asset_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mas.getAD_Client_ID()) + ","+String.valueOf(mas.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_Charge_ID="+source.getC_Charge_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_Charge_ID="+target.getC_Charge_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getA_Asset_AcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO A_Asset_Acct (");
		sqlCmdI1.append(stdColumns).append(",A_Asset_ID, C_AcctSchema_ID");
		if (target==null) {
			A_Asset_Acct_ID = getNextID(mas.getAD_Client_ID(), "A_Asset_Acct");
			sqlCmdI1.append(",A_Asset_Acct_ID");
		}
		// Additional Fields
		sqlCmdI1.append(",A_Depreciation_ID, A_Depreciation_F_ID");
		sqlCmdI1.append(",A_Depreciation_Manual_Amount, A_Depreciation_Manual_Period");
		sqlCmdI1.append(",A_Depreciation_Variable_Perc, A_Depreciation_Variable_Perc_F");
		sqlCmdI1.append(",A_Disposal_Gain, A_Disposal_Loss, A_Disposal_Revenue");
		sqlCmdI1.append(",A_Period_End, A_Period_Start");
		sqlCmdI1.append(",A_Reval_Accumdep_Offset_Cur, A_Reval_Accumdep_Offset_Prior, A_Reval_Cal_Method");
		sqlCmdI1.append(",A_Reval_Cost_Offset, A_Reval_Cost_Offset_Prior, A_Reval_Depexp_Offset, A_Salvage_Value");
		sqlCmdI1.append(",A_Split_Percent, PostingType, ValidFrom ");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+A_Asset_ID+","+targetAS.getC_AcctSchema_ID());
		if (target==null) {
			sqlCmdI2.append(","+A_Asset_Acct_ID);
		}
		// Additional Fields
		sqlCmdI2.append(","+source.getA_Depreciation_ID()+","+source.getA_Depreciation_F_ID());
		sqlCmdI2.append(","+source.getA_Depreciation_Manual_Amount()+","+source.getA_Depreciation_Manual_Period());
		sqlCmdI2.append(","+source.getA_Depreciation_Variable_Perc()+","+source.getA_Depreciation_Variable_Perc_F());
		sqlCmdI2.append(","+source.getA_Disposal_Gain()+","+source.getA_Disposal_Loss()+","+source.getA_Disposal_Revenue());
		sqlCmdI2.append(","+source.getA_Period_End()+","+source.getA_Period_Start());
		sqlCmdI2.append(","+source.getA_Reval_Accumdep_Offset_Cur()+","+source.getA_Reval_Accumdep_Offset_Prior()+","+source.getA_Reval_Cal_Method());
		sqlCmdI2.append(","+source.getA_Reval_Cost_Offset()+","+source.getA_Reval_Cost_Offset_Prior()+","+source.getA_Reval_Depexp_Offset()+","+source.getA_Salvage_Value());
		sqlCmdI2.append(","+source.getA_Split_Percent()+",'"+source.getPostingType()+"','"+source.getValidFrom()+"'");
		if (target == null) {
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
log.warning("columnName="+columnName+"  sourceC_ValidCombination_ID="+sourceC_ValidCombination_ID);
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
			sqlCmdI1.append(", A_Asset_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE A_Asset_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" A_Asset_ID="+A_Asset_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
			for (int i = 0; i < list.size(); i++)
			{
				KeyNamePair pp = list.get(i);
				int sourceC_ValidCombination_ID = pp.getKey();
				String columnName = pp.getName();
log.warning("columnName="+columnName+"  sourceC_ValidCombination_ID="+sourceC_ValidCombination_ID);
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
log.warning("sql="+sqlCmdI1+" "+sqlCmdI2);
		no = DB.executeUpdateEx(sqlCmdI1+" "+sqlCmdI2, null);
		if (no == 1) {
			m_info.append(Msg.translate( Env.getCtx(), "A_Asset_ID")).append("=").append(mas.getName());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"A_Asset_ID="+mas.getName()+" Default in AcctSchemaElement NOT updated");

	}

	private static void copyA_Asset_Group_Acct(int A_Asset_Group_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MAssetGroup mas = new MAssetGroup(Env.getCtx(),A_Asset_Group_ID,null);
		X_A_Asset_Group_Acct source = getA_Asset_GroupAcct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), A_Asset_Group_ID);
		X_A_Asset_Group_Acct target = getA_Asset_GroupAcct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), A_Asset_Group_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mas.getAD_Client_ID()) + ","+String.valueOf(mas.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_Charge_ID="+source.getC_Charge_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_Charge_ID="+target.getC_Charge_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getA_Asset_GroupAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO A_Asset_Group_Acct (");
		sqlCmdI1.append(stdColumns).append(",A_Asset_Group_ID,C_AcctSchema_ID");
		if (target==null) {
			A_Asset_Group_Acct_ID = getNextID(mas.getAD_Client_ID(), "A_Asset_Group_Acct");
			sqlCmdI1.append(",A_Asset_Group_Acct_ID");
		}
		// Additional Fields
		sqlCmdI1.append(",A_Depreciation_ID");
		sqlCmdI1.append(",A_Depreciation_F_ID, PostingType");
		sqlCmdI1.append(",UseLifeMonths, UseLifeYears");
		sqlCmdI1.append(",UseLifeMonths_F, UseLifeYears_F");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+A_Asset_Group_ID+","+targetAS.getC_AcctSchema_ID());
		if (target==null) {
			sqlCmdI2.append(","+A_Asset_Group_Acct_ID);
		}
		// Additional Fields
		sqlCmdI2.append(","+source.getA_Depreciation_ID());
		sqlCmdI2.append(","+source.getA_Depreciation_F_ID()+",'"+source.getPostingType()+"'");
		sqlCmdI2.append(","+source.getUseLifeMonths()+","+source.getUseLifeYears());
		sqlCmdI2.append(","+source.getUseLifeMonths_F()+","+source.getUseLifeYears_F());
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
			sqlCmdI1.append(", A_Asset_Group_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE A_Asset_Group_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" A_Asset_Group_ID="+A_Asset_Group_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "A_Asset_Group_ID")).append("=").append(mas.getName());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"A_Asset_Group="+mas.getName()+" Default in AcctSchemaElement NOT updated");

	}

	private static void copyC_Project_Acct(int C_Project_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		int no = 0;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;
		MProject mpr = new MProject(Env.getCtx(),C_Project_ID,null);
		X_C_Project_Acct source = getC_ProjectAcct(Env.getCtx(),sourceAS.getC_AcctSchema_ID(), C_Project_ID);
		X_C_Project_Acct target = getC_ProjectAcct(Env.getCtx(),targetAS.getC_AcctSchema_ID(), C_Project_ID);
		StringBuffer  sqlCmdI1 = null;
		StringBuffer  sqlCmdI2 = null;
		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(mpr.getAD_Client_ID()) + ","+String.valueOf(mpr.getAD_Org_ID())+ ",'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
//log.warning("Source C_Charge_ID="+source.getC_Charge_ID()+"  C_AcctSchema_ID="+source.getC_AcctSchema_ID());
//log.warning("Target C_Charge_ID="+target.getC_Charge_ID()+"  C_AcctSchema_ID="+target.getC_AcctSchema_ID());
		// GET Account Values Array List
		if (source == null)
			return;
		ArrayList<KeyNamePair> list = getC_ProjectAcctInfo(source);
		sqlCmdI1 = new StringBuffer ("INSERT INTO C_Project_Acct (");
		sqlCmdI1.append(stdColumns).append(",C_Project_ID,C_AcctSchema_ID");
		sqlCmdI2 = new StringBuffer (" VALUES (");
		sqlCmdI2.append(stdValues).append(","+C_Project_ID+","+targetAS.getC_AcctSchema_ID());
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
			sqlCmdI1.append(", C_Project_Acct_UU)");
			sqlCmdI2.append(","+DB.TO_STRING(UUID.randomUUID().toString())).append(")");
		} else {
			sqlCmdI1 = new StringBuffer ("UPDATE C_Project_Acct ");
			sqlCmdI2 = new StringBuffer (" WHERE ");
			sqlCmdI2.append(" C_Project_ID="+C_Project_ID+" AND C_AcctSchema_ID="+targetAS.getC_AcctSchema_ID());
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
			m_info.append(Msg.translate( Env.getCtx(), "C_Project_ID")).append("=").append(mpr.getName());
			if (target == null)
				m_info.append("Ins, \r\n");
			else
				m_info.append("Upd, \r\n");
		} else
			log.log(Level.SEVERE,"C_Project_ID="+mpr.getName()+" Default in AcctSchemaElement NOT updated");

	}

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
//	
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
	
	public static int getC_ChargeRecs (int AD_Client_ID )
	{
		//
		Integer noRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_Charge ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				noRecords = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_ChargeRecs", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (noRecords== null)
			noRecords=0;
		return noRecords;
	}	//	getC_ChargeRecs	
	
	public static X_C_Charge_Acct getC_Charge_Acct (Properties ctx, int C_AcctSchema_ID, int C_Charge_ID)
	{
		X_C_Charge_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_Charge_ID=?";
		retValue=new Query(ctx,X_C_Charge_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_Charge_ID)
		.firstOnly();
		return retValue;
	}	//	getC_Charge_Acct
	
	public static ArrayList<KeyNamePair> getC_Charge_AcctInfo(X_C_Charge_Acct chacc)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < chacc.get_ColumnCount(); i++)
		{
			String columnName = chacc.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( chacc.get_Value(i)!= null)
					id =  ((Integer)chacc.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" chacc.get_Value(i)="+chacc.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getC_ChargeInfo
	
	public static int getC_BankAccountRecs (int AD_Client_ID )
	{
		//
		Integer noRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_BankAccount ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				noRecords = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_ChargeRecs", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		

		if (noRecords== null)
			noRecords=0;
		return noRecords;
	}	//	getC_BankAccountRecs
	

	public static X_C_BankAccount_Acct getC_BankAccount_Acct (Properties ctx, int C_AcctSchema_ID, int C_BankAccount_ID)
	{
		X_C_BankAccount_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_BankAccount_ID=?";
		retValue=new Query(ctx,X_C_BankAccount_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_BankAccount_ID)
		.firstOnly();
		return retValue;
	}	//	getC_BankAccount_Acct
	
	public static ArrayList<KeyNamePair> getC_BankAccount_AcctInfo(X_C_BankAccount_Acct cbacc)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < cbacc.get_ColumnCount(); i++)
		{
			String columnName = cbacc.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( cbacc.get_Value(i)!= null)
					id =  ((Integer)cbacc.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" cbacc.get_Value(i)="+cbacc.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getC_BankAccount_AcctInfo
	
	public static int getC_TaxRecs (int AD_Client_ID )
	{
		//
		Integer noRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_Tax ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				noRecords = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_ChargeRecs", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (noRecords== null)
			noRecords=0;
		return noRecords;
	}	//	getC_TaxRecs

	public static X_C_Tax_Acct getC_Tax_Acct (Properties ctx, int C_AcctSchema_ID, int C_Tax_ID)
	{
		X_C_Tax_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_Tax_ID=?";
		retValue=new Query(ctx,X_C_Tax_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_Tax_ID)
		.firstOnly();
		return retValue;
	}	//	getC_Tax_Acct
	
	public static ArrayList<KeyNamePair> getC_Tax_AcctInfo(X_C_Tax_Acct ctaacc)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < ctaacc.get_ColumnCount(); i++)
		{
			String columnName = ctaacc.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( ctaacc.get_Value(i)!= null)
					id =  ((Integer)ctaacc.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" ctaacc.get_Value(i)="+ctaacc.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getC_Tax_AcctInfo
	
	public static int getA_AssetRecs (int AD_Client_ID )
	{
		//
		Integer noRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM A_Asset ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				noRecords = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_ChargeRecs", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (noRecords== null)
			noRecords=0;
		return noRecords;
	}	//	getA_AssetRecs

	public static X_A_Asset_Acct getA_Asset_Acct (Properties ctx, int C_AcctSchema_ID, int A_Asset_ID)
	{
		X_A_Asset_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND A_Asset_ID=?";
		retValue=new Query(ctx,X_A_Asset_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,A_Asset_ID)
		.firstOnly();
		return retValue;
	}	//	getA_Asset_Acct
		
	public static ArrayList<KeyNamePair> getA_Asset_AcctInfo(X_A_Asset_Acct aaac)
	{
		String onlyAcct="A_Asset_Acct,A_Accumdepreciation_Acct,A_Depreciation_Acct,A_Disposal_Revenue_Acct,A_Disposal_Loss_Acct";
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < aaac.get_ColumnCount(); i++)
		{
			id = 0;
			String columnName = aaac.get_ColumnName(i);
			if (columnName.endsWith("Acct") && onlyAcct.contains(columnName))
			{
				if ( aaac.get_Value(i)!= null)
					id =  ((Integer)aaac.get_Value(i));
				else 
					id = 0;
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getA_Asset_AcctInfo
	
	public static int getA_Asset_GroupRecs (int AD_Client_ID )
	{
		//
		Integer noRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM A_Asset_Group ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				noRecords = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_ChargeRecs", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (noRecords== null)
			noRecords=0;
		return noRecords;
	}	//	getA_Asset_GroupRecs
	
	public static X_A_Asset_Group_Acct getA_Asset_GroupAcct (Properties ctx, int C_AcctSchema_ID, int A_Asset_Group_ID)
	{
		X_A_Asset_Group_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND A_Asset_Group_ID=?";
		retValue=new Query(ctx,X_A_Asset_Group_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,A_Asset_Group_ID)
		.firstOnly();
		return retValue;
	}	//	getA_Asset_GroupAcct
	
	public static ArrayList<KeyNamePair> getA_Asset_GroupAcctInfo(X_A_Asset_Group_Acct aaga)
	{
		String onlyAcct="A_Accumdepreciation_Acct,A_Asset_Acct,A_Depreciation_Acct,A_Disposal_Loss_Acct,A_Disposal_Revenue_Acct";
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < aaga.get_ColumnCount(); i++)
		{
			String columnName = aaga.get_ColumnName(i);
			if (columnName.endsWith("Acct") && onlyAcct.contains(columnName))
			{
				if ( aaga.get_Value(i)!= null)
					id =  ((Integer)aaga.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" aaga.get_Value(i)="+aaga.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getA_Asset_GroupAcctInfo
	
	public static int getC_ProjectRecs (int AD_Client_ID )
	{
		//
		Integer noRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_Project ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				noRecords = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "getC_ChargeRecs", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}		
		if (noRecords== null)
			noRecords=0;
		return noRecords;
	}	//	getC_ProjectGroupRecs
	
	public static X_C_Project_Acct getC_ProjectAcct (Properties ctx, int C_AcctSchema_ID, int C_Project_ID)
	{
		X_C_Project_Acct retValue = null;
		final String whereClause = "C_AcctSchema_ID=? AND C_Project_ID=?";
		retValue=new Query(ctx,X_C_Project_Acct.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID,C_Project_ID)
		.firstOnly();
		return retValue;
	}	//	getC_ProjectAcct
	
	public static ArrayList<KeyNamePair> getC_ProjectAcctInfo(X_C_Project_Acct cproj)
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		int id = 0;
		for (int i = 0; i < cproj.get_ColumnCount(); i++)
		{
			String columnName = cproj.get_ColumnName(i);
			if (columnName.endsWith("Acct"))
			{
				if ( cproj.get_Value(i)!= null)
					id =  ((Integer)cproj.get_Value(i));
				else 
					id = 0;
//log.warning("columnName="+columnName+" cproj.get_Value(i)="+cproj.get_Value(i)+"  id="+id);
				list.add(new KeyNamePair (id, columnName));
			}
		}
		return list;
	}	//	getC_ProjectAcctInfo
	
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

	/**
	 * 	Get Info
	 *	@return Info
	 */
	public String getInfo()
	{
		return m_info.toString();
	}
	
	/**************************************************************************
	 * 	Get Next ID
	 * 	@param AD_Client_ID client
	 * 	@param TableName table name
	 * 	@return id
	 */
	private static int getNextID (int AD_Client_ID, String TableName)
	{
		//	TODO: Exception 
		return DB.getNextID (AD_Client_ID, TableName, m_trx.getTrxName());
	}	//	getNextID
}

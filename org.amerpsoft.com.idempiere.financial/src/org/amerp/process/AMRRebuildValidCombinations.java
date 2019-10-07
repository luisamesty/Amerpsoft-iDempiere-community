package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.adempiere.util.IProcessUI;
import org.compiere.model.I_C_ValidCombination;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MClient;
import org.compiere.model.MCurrency;
import org.compiere.model.MElementValue;
import org.compiere.model.MOrg;
import org.compiere.model.Query;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMRRebuildValidCombinations {

	/**	Logger			*/
	protected static CLogger	log = CLogger.getCLogger(AMRRebuildValidCombinations.class);
	private ProcessInfo			m_pi;	// Process Info ID
	//
	private MClient			m_client;
	private int AD_Client_ID=0;
	private MOrg			m_org;
	private int AD_Org_ID=0;
	private String m_trxName = "ValidCombination";
	// Account Schemas
	private int SourceAcctSchema_ID;
	private MAcctSchema SourceAS;
	private int TargetAcctSchema_ID;
	private MAcctSchema TargetAS;
	// Currencies
	private MCurrency targetCurr;
	private static StringBuffer m_info = new StringBuffer();
	// Conversion Factor
	private BigDecimal convFactorMultiply;
	private BigDecimal convFactorDivide;
	// Valid Combinations Count Vars
	int VC_Count = 0 ;
	int VCNo = 0;
	int Percent = 0;
	// Counts
	BigDecimal InsertC_ValidCombination=BigDecimal.ZERO;
	BigDecimal UpdateC_ValidCombination=BigDecimal.ZERO;
	BigDecimal ErrorC_ValidCombination=BigDecimal.ZERO;
	
	//IProcessUI processMonitor = Env.getProcessUI(getCtx());
	//processMonitor.statusUpdate(MessagetoShow);
	
	public boolean dupC_ValidCombination () throws Exception
	{
		ProcessInfo srv = m_pi;
		m_info = new StringBuffer();
		VC_Count = getValidCombinationRecs(getAD_Client_ID(), SourceAcctSchema_ID);
		// Reset record Count for I Insert U Update E Error
		resetCounters();
		// ProductNo
		VCNo = VCNo+1;
		// Get Product From AD_Client_ID
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ON (AD_Client_ID, C_AcctSchema_ID, Account_ID) ")	//	1	
		.append(" C_ValidCombination_ID, combination, ")
		.append(" AD_Client_ID, C_AcctSchema_ID, Account_ID ")
		.append(" FROM C_ValidCombination ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID)
		.append(" AND C_AcctSchema_ID= "+SourceAcctSchema_ID)
		.append(" ORDER BY AD_Client_ID, C_AcctSchema_ID, Account_ID ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int C_ValidCombination_ID = rs.getInt(1);
				String combination = rs.getString(2);
				VCNo = VCNo+1;
				if (VC_Count != 0 ) {
					Percent =  (VCNo * 100 /VC_Count);
				} else {
					Percent = 100;
					VCNo=1;
				}
				//
				MAccount vcsource = new MAccount(Env.getCtx(),C_ValidCombination_ID,null );
				// CREATE C_ValidCombination records
				MAccount vctarget = getFirstVCcombination(Env.getCtx(),rs.getInt(3),TargetAcctSchema_ID, rs.getInt(5), combination);
				if (vctarget== null) {
					// CREATE New Valid Combination for the New Account Schema
					vctarget = createAccount(SourceAS, TargetAS, vcsource, vctarget);
					// log.warning("vctarget (null)="+vctarget	);				
				} else {
					// UPDATE New Valid Combination for the New Account Schema
					// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
					vctarget = createAccount(SourceAS, TargetAS, vcsource, vctarget);
				}
//				if (VCNo==5)
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
		//log.warning("VC_Count:"+VC_Count);
		m_info.append(X_C_ValidCombination.Table_Name+": ");
		m_info.append(Msg.translate(Env.getCtx(), "C_ValidCombination_ID")+"   "+Msg.translate(Env.getCtx(), "Created")).append("\r\n");
		m_info.append("(s) = "+getInsertC_ValidCombination()).append("\r\n");
		m_info.append(Msg.translate(Env.getCtx(), "C_ValidCombination_ID")+"   "+Msg.translate(Env.getCtx(), "Updated")).append("\r\n");
		m_info.append("(s) = "+getUpdateC_ValidCombination()).append("\r\n");
		m_info.append(Msg.translate(Env.getCtx(), "C_ValidCombination_ID")+"   "+Msg.translate(Env.getCtx(), "Error")).append("\r\n");
		m_info.append("(s) = "+getErrorC_ValidCombination()).append("\r\n");

		return false;
	}
	

	/**
	 * 	Create Account
	 *	@param targetAS target AS
	 *	@param sourceAcct source account
	 *	@return target account
	 */
	MAccount createAccount(MAcctSchema sourceAS, MAcctSchema targetAS, MAccount sourceAcct, MAccount targetAcct)
	{
		int Account_ID = 0;
		int C_SubAcct_ID = 0;
		int M_Product_ID = 0;
		int C_BPartner_ID = 0;
		int AD_OrgTrx_ID = 0;
		int C_LocFrom_ID = 0;
		int C_LocTo_ID = 0;
		int C_SalesRegion_ID = 0; 
		int C_Project_ID = 0;
		int C_Campaign_ID = 0;
		int C_Activity_ID = 0;
		int User1_ID = 0;
		int User2_ID = 0;
		int UserElement1_ID = 0;
		int UserElement2_ID = 0;
		//
		String Symbol="";
		String Description ="";
		// Currencies
		MCurrency tCurr = new MCurrency(Env.getCtx(), targetAS.getC_Currency_ID(), null);
		setTargetCurr(tCurr);
		if (targetCurr.getCurSymbol()!= null) {
			Symbol=targetCurr.getCurSymbol();
		}
		//  Active Elements
		MAcctSchemaElement[] elements = sourceAS.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			MAcctSchemaElement ase = elements[i];
			String elementType = ase.getElementType();
			//
			if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization)) {
			} else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
				Account_ID = sourceAcct.getAccount_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_SubAccount))
				C_SubAcct_ID = sourceAcct.getC_SubAcct_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_BPartner))
				C_BPartner_ID = sourceAcct.getC_BPartner_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Product))
				M_Product_ID = sourceAcct.getM_Product_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Activity))
				C_Activity_ID = sourceAcct.getC_Activity_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_LocationFrom))
				C_LocFrom_ID = sourceAcct.getC_LocFrom_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_LocationTo))
				C_LocTo_ID = sourceAcct.getC_LocTo_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Campaign))
				C_Campaign_ID = sourceAcct.getC_Campaign_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_OrgTrx))
				AD_OrgTrx_ID = sourceAcct.getAD_OrgTrx_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Project))
				C_Project_ID = sourceAcct.getC_Project_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_SalesRegion))
				C_SalesRegion_ID = sourceAcct.getC_SalesRegion_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList1))
				User1_ID = sourceAcct.getUser1_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserElementList2))
				User2_ID = sourceAcct.getUser2_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserColumn1))
				UserElement1_ID = sourceAcct.getUserElement1_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_UserColumn2))
				UserElement2_ID = sourceAcct.getUserElement2_ID();
			//	No UserElement
		}
		// Verify if combination already exist with TargetAcctSchema_ID
		if (targetAcct==null) {
			// Create 
			targetAcct = new MAccount(Env.getCtx(), 0, null);
			setInsertC_ValidCombination(InsertC_ValidCombination.add(BigDecimal.ONE));
		} else {
			setUpdateC_ValidCombination(UpdateC_ValidCombination.add(BigDecimal.ONE));
		}
		targetAcct.setAD_Org_ID(sourceAcct.getAD_Org_ID());
		targetAcct.setC_AcctSchema_ID(targetAS.getC_AcctSchema_ID());
		targetAcct.setAccount_ID(Account_ID);
		targetAcct.setC_SubAcct_ID(C_SubAcct_ID);
		targetAcct.setM_Product_ID(M_Product_ID);
		targetAcct.setC_BPartner_ID(C_BPartner_ID);
		targetAcct.setAD_OrgTrx_ID(AD_OrgTrx_ID);
		targetAcct.setC_LocFrom_ID(C_LocFrom_ID);
		targetAcct.setC_LocTo_ID(C_LocTo_ID);
		targetAcct.setC_SalesRegion_ID(C_SalesRegion_ID);
		targetAcct.setC_Project_ID(C_Project_ID);
		targetAcct.setC_Campaign_ID(C_Campaign_ID);
		targetAcct.setC_Activity_ID(C_Activity_ID);
		targetAcct.setUser1_ID(User1_ID);
		targetAcct.setUser2_ID(User2_ID);
		targetAcct.setUserElement1_ID(UserElement1_ID);
		targetAcct.setUserElement2_ID(UserElement2_ID);
		MElementValue melv = new MElementValue(Env.getCtx(),Account_ID,null);
		Description= melv.getValue()+" "+melv.getName();
		// Alias
		if (sourceAcct.getAlias()!=null) {
			targetAcct.setAlias(sourceAcct.getAlias().trim()+" "+Symbol);
		} else {
			targetAcct.setAlias(Description+" "+Symbol);
		}
		// Combination
		targetAcct.setCombination(sourceAcct.getCombination());
		targetAcct.setDescription(sourceAcct.getDescription());
//log.warning("Combination="+sourceAcct.getCombination()+" "+sourceAcct.getDescription());
		targetAcct.save();
		// return
		return targetAcct;
	}	//	createAccount
	
	/**
	 * getValidCombinationRecs
	 * @param AD_Client_ID
	 * @return
	 */
	@SuppressWarnings("null")
	public static int getValidCombinationRecs (int AD_Client_ID, int TargetAcctSchema_ID )
	{
		//
		Integer noVCRecords = 0;
		StringBuilder sql = new StringBuilder("SELECT count(*) as recnum ")
		.append(" FROM C_ValidCombination ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID)
		.append(" AND C_AcctSchema_ID= "+TargetAcctSchema_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				noVCRecords = rs.getInt(1);
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
		if ( noVCRecords == null ) {
			noVCRecords = 0;
		}
		//log.warning("noVCRecords:"+noVCRecords);
		return noVCRecords;
	}	//	getValidCombinationRecs

	/**
	 * 	Get first with combination
	 * @param ctx
	 * @param AD_Client_ID
	 * @param C_AcctSchema_ID
	 * @param Account_ID
	 * @param combination
	 *@return C_validCombination_ID
	 *
	 * Class for C_validCombination:  MAccount
	 */

	public  MAccount getFirstVCcombination (Properties ctx, int AD_Client_ID, int C_AcctSchema_ID, int Account_ID, String combination)
	{
		MAccount retValue = null;
		int C_ValidCombination_ID=0;
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ON (AD_Client_ID, C_AcctSchema_ID, Account_ID) ")	//	1	
		.append(" C_ValidCombination_ID ")
		.append(" FROM C_ValidCombination ")
		.append(" WHERE AD_Client_ID= "+AD_Client_ID)
		.append(" AND C_AcctSchema_ID= "+C_AcctSchema_ID)
		//.append(" AND combination= '"+combination+"'");
		.append(" AND Account_ID= "+Account_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				C_ValidCombination_ID = rs.getInt(1);
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
		if (C_ValidCombination_ID > 0)
			retValue = new MAccount(Env.getCtx(),C_ValidCombination_ID,null);
		//log.warning("C_ValidCombination_ID="+C_ValidCombination_ID+"  MAccount="+retValue);	
		return retValue;
	}	//	get

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


	public int getAD_Org_ID() {
		return AD_Org_ID;
	}


	public void setAD_Org_ID(int aD_Org_ID) {
		AD_Org_ID = aD_Org_ID;
	}


	public void setAD_Client_ID(int aD_Client_ID) {
		AD_Client_ID = aD_Client_ID;
	}


	public MOrg getM_org() {
		return m_org;
	}


	public void setM_org(MOrg m_org) {
		this.m_org = m_org;
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

	
	public void resetCounters() {
		setInsertC_ValidCombination(BigDecimal.ZERO);
		setUpdateC_ValidCombination(BigDecimal.ZERO);
		setErrorC_ValidCombination(BigDecimal.ZERO);
	}


	public BigDecimal getInsertC_ValidCombination() {
		return InsertC_ValidCombination;
	}


	public void setInsertC_ValidCombination(BigDecimal insertC_ValidCombination) {
		InsertC_ValidCombination = insertC_ValidCombination;
	}


	public BigDecimal getUpdateC_ValidCombination() {
		return UpdateC_ValidCombination;
	}


	public void setUpdateC_ValidCombination(BigDecimal updateC_ValidCombination) {
		UpdateC_ValidCombination = updateC_ValidCombination;
	}


	public BigDecimal getErrorC_ValidCombination() {
		return ErrorC_ValidCombination;
	}


	public void setErrorC_ValidCombination(BigDecimal errorC_ValidCombination) {
		ErrorC_ValidCombination = errorC_ValidCombination;
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

}

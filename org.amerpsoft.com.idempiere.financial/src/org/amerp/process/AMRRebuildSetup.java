package org.amerp.process;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaDefault;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MAcctSchemaGL;
import org.compiere.model.MClient;
import org.compiere.model.MCost;
import org.compiere.model.MElement;
import org.compiere.model.MElementValue;
import org.compiere.model.MOrg;
import org.compiere.model.Query;
import org.compiere.model.X_C_AcctSchema_Default;
import org.compiere.model.X_C_AcctSchema_Element;
import org.compiere.model.X_C_AcctSchema_GL;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

/**
 * 
 * @author luisamesty
 *
 */
public class AMRRebuildSetup {
	/**
	 *  Constructor
	 *  @param ctx context
	 *  @param WindowNo window
	 */
	public AMRRebuildSetup(Properties ctx, int WindowNo)
	{
		m_ctx = new Properties(ctx);	//	copy
		m_lang = Env.getAD_Language(m_ctx);
	}   //  MSetup

	/**	Logger			*/
	protected static CLogger	log = CLogger.getCLogger(AMRRebuildSetup.class);
	//
	private static Trx		m_trx = Trx.get(Trx.createTrxName("Setup"), true);
	private Properties      m_ctx;
	private static String          m_lang;
	private static StringBuffer    m_info = new StringBuffer();;
	//
	private static MClient			m_client;
	private static MOrg			m_org;
	//
	private int     		AD_User_ID;
	//

	/**
	 * createClientSchema
	 * @param AD_Client_ID
	 * @param p_SourceAcctSchema_ID
	 * @param p_TargetAcctSchema_ID
	 * @return
	 * @throws Exception
	 */
	public static boolean createClientSchemaStructure (int AD_Client_ID, int p_SourceAcctSchema_ID, int p_TargetAcctSchema_ID) throws Exception
	{

		//m_trx.start();
		//  info header
		//log.warning("paso...");
		MAcctSchema source = MAcctSchema.get(Env.getCtx(), p_SourceAcctSchema_ID, null);
		// Verify if Source C_AcctSchema_ID is valid
		if (source.get_ID() == 0)
			throw new AdempiereSystemError("NotFound Source C_AcctSchema_ID=" + p_SourceAcctSchema_ID);
		MAcctSchema target = new MAcctSchema (Env.getCtx(), p_TargetAcctSchema_ID, get_TrxName());
		// Verify if target C_AcctSchema_ID is valid
		if (target.get_ID() == 0)
			throw new AdempiereSystemError("NotFound Target C_AcctSchema_ID=" + p_TargetAcctSchema_ID);
		// List Target elements 
log.warning("p_SourceAcctSchema_ID"+p_SourceAcctSchema_ID+" p_TargetAcctSchema_ID"+p_TargetAcctSchema_ID);
		MAcctSchemaElement[] targetElements = target.getAcctSchemaElements();
		// Create Target AC C_AcctSchema_Element
		copyAccSchemaElements(AD_Client_ID, source, target);
		//  
//		if (MAcctSchemaGL.get(Env.getCtx(), p_TargetAcctSchema_ID) == null){
			copyAccSchemaGL(source, target);
//		}
//		if (MAcctSchemaDefault.get(Env.getCtx(), p_TargetAcctSchema_ID) == null){
			copyAccSchemaDefault(source, target);
//		}		
		return true;
		
	}   //  createClient
	
	
	/**
	 * 	Get Info
	 *	@return Info
	 */
	public static String getInfo()
	{
		return m_info.toString();
	}

	/**
	 * 	Rollback Internal Transaction
	 */
	public void rollback() {
		try {
			m_trx.rollback();
			m_trx.close();
		} catch (Exception e) {}
	}

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
	 * 
	 * Create AccountingSchema Elements (Structure)
	 * @param sourceAS
	 * @param targetAS
	 * @return
	 */
	private static boolean copyAccSchemaElements (int AD_Client_ID, MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception {
		//
		m_info.append(X_C_AcctSchema_Element.Table_Name).append(": ").append("\r\n");
		//  Standard columns
		String name = null;
		String sql = null;
		StringBuffer  sqlCmd = null;
		int no = 0;
		int SourceAcctSchema_Element_ID = 0;
		int C_AcctSchema_Element_ID = 0;
		boolean isnew=false;
		int C_ElementValue_ID = 0;
		int C_Element_ID =  0;
		int Org_ID = 0;

		// 	Stansard Columns
		String stdColumns = "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy";
		//	Standard Values
		String stdValues = String.valueOf(AD_Client_ID) + ",0 ,'Y',SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()))+",SysDate,"+String.valueOf(Env.getAD_User_ID(Env.getCtx()));
		// C_Element_ID First on Array
		MElement[] mel = sqlGetC_Element_ID(Env.getCtx(), AD_Client_ID, "C_Element");
		MElement meldef = mel[0];
		MAcctSchemaElement masele = null;
		MAcctSchemaElement tmasele = null;
		C_Element_ID = 0;
		// Account Schema Elements
		sql = "SELECT C_AcctSchema_Element_ID, ElementType, Name FROM C_AcctSchema_Element WHERE C_AcctSchema_ID = "+ sourceAS.getC_AcctSchema_ID();
		PreparedStatement stmt = null;
		ResultSet rs = null;
//log.warning("sql="+sql);
		try
		{
			stmt = DB.prepareStatement(sql, m_trx.getTrxName());
			rs = stmt.executeQuery();
			while (rs.next())
			{
				SourceAcctSchema_Element_ID = rs.getInt(1);
				String ElementType = rs.getString(2);
				name = rs.getString(3);
				//
				String IsMandatory = null;
				String IsBalanced = "N";
				int SeqNo = 0;
				masele = new MAcctSchemaElement(Env.getCtx(),SourceAcctSchema_Element_ID,null);
				tmasele = sqlGetM_AcctSchema_Element( targetAS,  ElementType);
				if (tmasele == null ) {
					isnew=true;
					C_AcctSchema_Element_ID=0;
				} else {
					C_AcctSchema_Element_ID=tmasele.getC_AcctSchema_Element_ID();					
				}
				if (masele.isMandatory()) IsMandatory =  "Y"; else IsMandatory =  "N";
				if (masele.isBalanced()) IsBalanced =  "Y"; else IsBalanced =  "N";
				if (ElementType.equals("OO"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory =  "Y";
					//IsBalanced = "Y";
					SeqNo = 10;
					Org_ID = masele.getOrg_ID();
				}
				else if (ElementType.equals("AC"))
				{
					if (isnew)
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "Y";
					SeqNo = 20;
					C_Element_ID = masele.getC_Element_ID();
					C_ElementValue_ID = masele .getC_ElementValue_ID();
				}
				else if (ElementType.equals("PR"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "N";
					SeqNo = 30;
				}
				else if (ElementType.equals("BP"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "N";
					SeqNo = 40;
				}
				else if (ElementType.equals("PJ"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "N";
					SeqNo = 50;
				}
				else if (ElementType.equals("MC"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "N";
					SeqNo = 60;
				}
				else if (ElementType.equals("SR"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "N";
					SeqNo = 70;
				}
				else if (ElementType.equals("AY"))
				{
					if (isnew )
						C_AcctSchema_Element_ID = getNextID(AD_Client_ID, "C_AcctSchema_Element");
					//IsMandatory = "N";
					SeqNo = 80;
				}
				//	Not OT, LF, LT, U1, U2
		
				if (IsMandatory != null)
				{
					// CREATES NEW C_AcctSchema_Element IF NOT Exists
					if (isnew ) {
						sqlCmd = new StringBuffer ("INSERT INTO C_AcctSchema_Element(");
						sqlCmd.append(stdColumns).append(",C_AcctSchema_Element_ID,C_AcctSchema_ID,")
							.append("ElementType,Name,SeqNo,IsMandatory,IsBalanced,C_AcctSchema_Element_UU) VALUES (");
						sqlCmd.append(stdValues).append(",").append(C_AcctSchema_Element_ID).append(",").append(targetAS.getC_AcctSchema_ID()).append(",")
							.append("'").append(ElementType).append("','").append(name).append("',").append(SeqNo).append(",'")
							.append(IsMandatory).append("','").append(IsBalanced).append("',").append(DB.TO_STRING(UUID.randomUUID().toString())).append(")");
//log.warning("sqlCmd INSERT="+sqlCmd.toString());
						no = DB.executeUpdateEx(sqlCmd.toString(),null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
//log.warning("ElementType="+ElementType+"  no="+no);		
					}
					// UPDATE C_AcctSchema_Element IF
					/** Default value for mandatory elements: OO and AC */
					if (ElementType.equals("OO"))
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET Org_ID=");
						sqlCmd.append(Org_ID).append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
						else
							log.log(Level.SEVERE, sqlCmd+" Default Org in AcctSchemaElement NOT updated");
//log.warning("sqlCmd UPDATE OO="+sqlCmd.toString()+"  no="+no);
					}
					if (ElementType.equals("AC"))
					{
						if (C_ElementValue_ID > 0 && C_Element_ID > 0) {
							sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_ElementValue_ID=");
							sqlCmd.append(C_ElementValue_ID).append(", C_Element_ID=").append(C_Element_ID);
							sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
							no = DB.executeUpdateEx(sqlCmd.toString(), null);
							if (no == 1)
								m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
							else
								log.log(Level.SEVERE, sqlCmd+" Default C_ElementValue_ID in AcctSchemaElement NOT updated");
						}
						if (C_Element_ID > 0) {
							sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_Element_ID=");
							sqlCmd.append(C_Element_ID);
							sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
							no = DB.executeUpdateEx(sqlCmd.toString(), null);
							if (no == 1)
								m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
							else
								log.log(Level.SEVERE, sqlCmd+" Default C_Element_ID in AcctSchemaElement NOT updated");
						}
//log.warning("sqlCmd UPDATE AC="+sqlCmd.toString()+"  no="+no);
					}
					if (ElementType.equals("PR") && masele.getM_Product_ID() > 0)
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET M_Product_ID=");
						sqlCmd.append(masele.getM_Product_ID());
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
						else
							log.log(Level.SEVERE, sqlCmd+" Default M_Product_ID in AcctSchemaElement NOT updated");
					}
					if (ElementType.equals("BP") && masele.getC_BPartner_ID() > 0)
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_BPartner_ID=");
						sqlCmd.append(masele.getC_BPartner_ID());
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
						else
							log.log(Level.SEVERE, sqlCmd+" Default C_BPartner_ID in AcctSchemaElement NOT updated");
					}
					if (ElementType.equals("PJ") && masele.getC_Project_ID() > 0)
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_Project_ID=");
						sqlCmd.append(masele.getC_Project_ID());
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no != 1)
							log.log(Level.SEVERE, sqlCmd+" Default C_Project_ID in AcctSchemaElement NOT updated");
					}
					if (ElementType.equals("MC") && masele.getC_Campaign_ID() > 0)
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_Campaign_ID=");
						sqlCmd.append(masele.getC_Campaign_ID());
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
						else
							log.log(Level.SEVERE, sqlCmd+" Default C_Campaign_ID in AcctSchemaElement NOT updated");		
					}
					if (ElementType.equals("SR") && masele.getC_SalesRegion_ID() > 0)
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_SalesRegion_ID=");
						sqlCmd.append(masele.getC_SalesRegion_ID());
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
						else
							log.log(Level.SEVERE, sqlCmd+" Default C_SalesRegion_ID in AcctSchemaElement NOT updated");
					}
					if (ElementType.equals("AY") && masele.getC_Activity_ID() > 0)
					{
						sqlCmd = new StringBuffer ("UPDATE C_AcctSchema_Element SET C_Activity_ID=");
						sqlCmd.append(masele.getC_Activity_ID());
						sqlCmd.append(" WHERE C_AcctSchema_Element_ID=").append(C_AcctSchema_Element_ID);
						no = DB.executeUpdateEx(sqlCmd.toString(), null);
						if (no == 1)
							m_info.append(Msg.translate(m_lang, "C_AcctSchema_Element_ID")).append("=").append(name).append("\r\n");
						else
							log.log(Level.SEVERE, sqlCmd+" Default C_Activity_ID in AcctSchemaElement NOT updated");
					}
				}

			}
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "Elements", e1);
			m_info.append(e1.getMessage());
			m_trx.rollback();
			m_trx.close();
			return false;
		}
		finally
		{
			DB.close(rs, stmt);
			rs = null; stmt = null;
		}
		return false;

	}//  Create AcctSchema


	/** 
	 * copyGL
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyAccSchemaGL (MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		boolean targetSave = false;
		MAcctSchemaGL source = MAcctSchemaGL.get(Env.getCtx(), sourceAS.getC_AcctSchema_ID());		
		MAcctSchemaGL target = MAcctSchemaGL.get(Env.getCtx(), targetAS.getC_AcctSchema_ID());
		if (target == null)
			target = new MAcctSchemaGL(Env.getCtx(), 0, null);
		target.setC_AcctSchema_ID(targetAS.getC_AcctSchema_ID());
		target.setIsActive(true);
		target.setUseCurrencyBalancing(true);
		target.setUseSuspenseBalancing(true);
		ArrayList<KeyNamePair> list = source.getAcctInfo();
		for (int i = 0; i < list.size(); i++)
		{
			KeyNamePair pp = list.get(i);
			int sourceC_ValidCombination_ID = pp.getKey();
			String columnName = pp.getName();
			MAccount sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
//			MAccount targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
			AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
			// CREATE C_ValidCombination records
			MAccount targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
			if (targetAccount== null) {
				// CREATE New Valid Combination for the New Account Schema
				targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				// log.warning("vctarget (null)="+vctarget	);				
			} else {
				// UPDATE New Valid Combination for the New Account Schema
				// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
				targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
			}
			target.setValue(columnName, new Integer(targetAccount.getC_ValidCombination_ID()));
			//log.warning("columnName="+columnName);
		}
		targetSave = target.save();
		//
		if (!targetSave)
			throw new AdempiereSystemError("Could not Save GL");
		//
		m_info.append(X_C_AcctSchema_GL.Table_Name).append(": ").append("\r\n");
		m_info.append(Msg.translate(Env.getCtx(), "Updated")).append("\r\n");

		//log.warning("targetSave="+targetSave);
	}	//	copyGL
	
	/**
	 * copyDefault
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyAccSchemaDefault(MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		MAcctSchemaDefault source = MAcctSchemaDefault.get(Env.getCtx(), sourceAS.getC_AcctSchema_ID());
		MAcctSchemaDefault target = MAcctSchemaDefault.get(Env.getCtx(), targetAS.getC_AcctSchema_ID());
		if (target == null)
			target = new MAcctSchemaDefault(Env.getCtx(), 0, null);
		target.setC_AcctSchema_ID(targetAS.getC_AcctSchema_ID());
		ArrayList<KeyNamePair> list = source.getAcctInfo();
		for (int i = 0; i < list.size(); i++)
		{
			KeyNamePair pp = list.get(i);
			int sourceC_ValidCombination_ID = pp.getKey();
			String columnName = pp.getName();
			MAccount sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
//			MAccount targetAccount = createAccount(sourceAS, targetAS, sourceAccount);
			AMRRebuildValidCombinations rvc = new AMRRebuildValidCombinations();
			// CREATE C_ValidCombination records
			MAccount targetAccount = rvc.getFirstVCcombination(Env.getCtx(),sourceAS.getAD_Client_ID(),targetAS.getC_AcctSchema_ID(), sourceAccount.getAccount_ID(), sourceAccount.getCombination());
			if (targetAccount== null) {
				// CREATE New Valid Combination for the New Account Schema
				targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				// log.warning("vctarget (null)="+vctarget	);				
			} else {
				// UPDATE New Valid Combination for the New Account Schema
				// log.warning("vctarget (not Null) Account_ID="+vctarget.getAccount_ID());
				targetAccount = rvc.createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
			}
			target.setValue(columnName, new Integer(targetAccount.getC_ValidCombination_ID()));
		}
		if (!target.save())
			throw new AdempiereSystemError("Could not Save Default");
		//
		m_info.append(X_C_AcctSchema_Default.Table_Name).append(": ").append("\r\n");
		m_info.append(Msg.translate(Env.getCtx(), "Updated")).append("\r\n");
	}	//	copyDefault
	
//	/**
//	 * 	Create Account
//	 *	@param targetAS target AS
//	 *	@param sourceAcct source account
//	 *	@return target account
//	 */
//	private static MAccount createAccount(MAcctSchema sourceAS, MAcctSchema targetAS, MAccount sourceAcct)
//	{
//		
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

//	/**
//	 * 	Create Account
//	 *	@param targetAS target AS
//	 *	@param sourceAcct source account
//	 *	@return target account
//	 */
//	private static MAccount createAccount(MAcctSchema sourceAS, MAcctSchema targetAS, MAccount sourceAcct, MAccount targetAcct)
//	{
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
//		String Symbol="";
//		String Description ="";
//		//  Active Elements
//		MAcctSchemaElement[] elements = sourceAS.getAcctSchemaElements();
//		for (int i = 0; i < elements.length; i++)
//		{
//			MAcctSchemaElement ase = elements[i];
//			String elementType = ase.getElementType();
//			//
//			if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization)) {
//			} else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
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
//		// Verify if combination already exist with TargetAcctSchema_ID
//		if (targetAcct==null) {
//			// Create 
//			targetAcct = new MAccount(Env.getCtx(), 0, null);
//		} else {
//		}
//		targetAcct.setAD_Org_ID(sourceAcct.getAD_Org_ID());
//		targetAcct.setC_AcctSchema_ID(targetAS.getC_AcctSchema_ID());
//		targetAcct.setAccount_ID(Account_ID);
//		targetAcct.setC_SubAcct_ID(C_SubAcct_ID);
//		targetAcct.setM_Product_ID(M_Product_ID);
//		targetAcct.setC_BPartner_ID(C_BPartner_ID);
//		targetAcct.setAD_OrgTrx_ID(AD_OrgTrx_ID);
//		targetAcct.setC_LocFrom_ID(C_LocFrom_ID);
//		targetAcct.setC_LocTo_ID(C_LocTo_ID);
//		targetAcct.setC_SalesRegion_ID(C_SalesRegion_ID);
//		targetAcct.setC_Project_ID(C_Project_ID);
//		targetAcct.setC_Campaign_ID(C_Campaign_ID);
//		targetAcct.setC_Activity_ID(C_Activity_ID);
//		targetAcct.setUser1_ID(User1_ID);
//		targetAcct.setUser2_ID(User2_ID);
//		targetAcct.setUserElement1_ID(UserElement1_ID);
//		targetAcct.setUserElement2_ID(UserElement2_ID);
//		MElementValue melv = new MElementValue(Env.getCtx(),Account_ID,null);
//		Description= melv.getValue()+" "+melv.getName();
//		// Alias
//		if (sourceAcct.getAlias()!=null) {
//			targetAcct.setAlias(sourceAcct.getAlias().trim()+" "+Symbol);
//		} else {
//			targetAcct.setAlias(Description+" "+Symbol);
//		}
//		// Combination
//		targetAcct.setCombination(sourceAcct.getCombination());
//		targetAcct.setDescription(sourceAcct.getDescription());
//		//log.warning("Combination="+sourceAcct.getCombination()+" "+sourceAcct.getDescription());
//		targetAcct.save();
//		// return
//		return targetAcct;
//	}	//	createAccount
//	
	
	/**
	 * sqlGetC_Element_ID
	 * @param ctx
	 * @param AD_Client_ID
	 * @param trxName
	 * @return
	 */
	
	public static MElement[] sqlGetC_Element_ID (Properties ctx,int AD_Client_ID, String trxName)
	
	{
		final String whereClause = "AD_Client_ID=? ";
		List<MCost> list = new Query(Env.getCtx(), "C_Element",
				whereClause, get_TrxName()).setParameters(AD_Client_ID).list();
		MElement[] retValue = new MElement[list.size()];
		list.toArray(retValue);
		return retValue;	
	}
	
	public static MAcctSchemaElement sqlGetM_AcctSchema_Element( MAcctSchema targetAS, String ElementType)
	
	{
		int C_AcctSchema_Element_ID=0;
		MAcctSchemaElement masel = null;
		// Account Schema Elements
		String sql = "SELECT C_AcctSchema_Element_ID, ElementType, Name " +
				" FROM C_AcctSchema_Element " +
				" WHERE C_AcctSchema_ID = "+ targetAS.getC_AcctSchema_ID() +
				" AND ElementType='"+ElementType+"'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//log.warning("sql="+sql);
		try
		{
			pstmt = DB.prepareStatement (sql.toString(), null);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				C_AcctSchema_Element_ID = rs.getInt(1);
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
		if (C_AcctSchema_Element_ID>0)
			masel = new MAcctSchemaElement(Env.getCtx(),C_AcctSchema_Element_ID,null);
		return masel;		
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

//targetElements = target.getAcctSchemaElements();
//if (targetElements.length == 0)
//	throw new AdempiereUserError("NotFound Target C_AcctSchema_Element");		
//
////	Accounting Element must be the same
//MAcctSchemaElement sourceAcctElement = source.getAcctSchemaElement(MAcctSchemaElement.ELEMENTTYPE_Account);
//if (sourceAcctElement == null)
//	throw new AdempiereUserError("NotFound Source AC C_AcctSchema_Element");
//MAcctSchemaElement targetAcctElement = target.getAcctSchemaElement(MAcctSchemaElement.ELEMENTTYPE_Account);
//
//// Verify Target AC C_AcctSchema_Element
//if (targetElements.length == 0)
//	throw new AdempiereUserError("NotFound Target AC C_AcctSchema_Element");
//
//if (sourceAcctElement.getC_Element_ID() != targetAcctElement.getC_Element_ID())
//	throw new AdempiereUserError("@C_Element_ID@ different");
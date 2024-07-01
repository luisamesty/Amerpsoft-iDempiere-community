package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.amerp.amnmodel.MAMN_Concept_Types;
import org.amerp.amnmodel.MAMN_Concept_Types_Acct;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnmodel.MAMN_Schema;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaDefault;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MCurrency;
import org.compiere.model.X_C_AcctSchema_Default;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class AMNPayrollReconversion extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int	p_SourceAcctSchema_ID = 0;
	private int	p_TargetAcctSchema_ID = 0;
	private int SourceCurrency_ID=0;
	private int TargetCurrency_ID=0;
	private String p_SetupAMNSchema="N";
	private String p_AMNConceptTypes="N";
	//
	private static StringBuffer    m_info = new StringBuffer();
	static CLogger log = CLogger.getCLogger(AMNPayrollReconversion.class);

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AD_Client_ID")) 
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("SourceAcctSchema_ID")) 
				p_SourceAcctSchema_ID =  para.getParameterAsInt();
			else if (paraName.equals("TargetAcctSchema_ID")) 
				p_TargetAcctSchema_ID =  para.getParameterAsInt();
			else if (paraName.equals("SetupAMNSchema")) 
				p_SetupAMNSchema =  para.getParameterAsString();
			else if (paraName.equals("AMNConceptTypes")) 
				p_AMNConceptTypes =  para.getParameterAsString();
						else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
//log.warning("...........Parametros.........AD_Client_ID="+p_AD_Client_ID+"  p_SourceAcctSchema_ID"+p_SourceAcctSchema_ID+"  p_TargetAcctSchema_ID"+p_TargetAcctSchema_ID);

	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		String  Msg_Header = AMNAmerpProcessMsg.sqlGetADProcessTRL(this.getProcessInfo().getAD_Process_ID(), Env.getAD_Language(Env.getCtx()))+"\r\n";
		// Status UPDATE
		this.statusUpdate(Msg_Header.trim()+": "+Msg.translate(Env.getCtx(), "Processing"));		
		String MessagetoShow = "";
		addLog(Msg_Header);
		// Client and AcctSchema
		MClient mc = new MClient(Env.getCtx(), p_AD_Client_ID, null);
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null);
		MCurrency curr = new MCurrency(Env.getCtx(), info.getC_Currency_ID(), null);
		// Client Name 
		Msg_Header = Msg.translate(Env.getCtx(), "AD_Client_ID")+":"+mc.getName();
		addLog(Msg_Header);
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// default AcctSchema 
		Msg_Header = Msg.translate(Env.getCtx(), "C_AcctSchema_ID")+" "+
				 Msg.translate(Env.getCtx(), "default")+
				 ":"+as.getName();
		addLog(Msg_Header);
		// default Currency
		Msg_Header = Msg.translate(Env.getCtx(), "C_Currency_ID")+" "+
				 Msg.translate(Env.getCtx(), "default")+
				 ":"+curr.getISO_Code()+"-"+curr.getCurSymbol()+"-"+curr.getDescription();
		addLog(Msg_Header);
		// Verify Parameters
		MAcctSchema source = MAcctSchema.get(Env.getCtx(), p_SourceAcctSchema_ID, null);
		if (source.get_ID() == 0)
			throw new AdempiereSystemError("NotFound Source C_AcctSchema_ID=" + p_SourceAcctSchema_ID);
		MAcctSchema target = new MAcctSchema (Env.getCtx(), p_TargetAcctSchema_ID, get_TrxName());
		if (target.get_ID() == 0)
			throw new AdempiereSystemError("NotFound Target C_AcctSchema_ID=" + p_TargetAcctSchema_ID);		
		// Verify Source AcctSchema
		if (p_SourceAcctSchema_ID != info.getC_AcctSchema1_ID()) {
			throw new AdempiereSystemError("Default C_AcctSchema_ID=" + info.getC_AcctSchema1_ID()+
					" Differs from source received = "+ p_SourceAcctSchema_ID);		
		}
		// Currencies
		SourceCurrency_ID=source.getC_Currency_ID();
		MCurrency scurr = new MCurrency(Env.getCtx(), SourceCurrency_ID, null);
		TargetCurrency_ID=target.getC_Currency_ID();
		MCurrency tcurr = new MCurrency(Env.getCtx(), TargetCurrency_ID, null);

		// Source Received
		Msg_Header = Msg.translate(Env.getCtx(), "C_AcctSchema_ID")+" "+
				 Msg.translate(Env.getCtx(), "From")+
				 ":"+source.getName();
		addLog(Msg_Header);
		Msg_Header = Msg.translate(Env.getCtx(), "C_Currency_ID")+" "+
				 Msg.translate(Env.getCtx(), "From")+
				 ":"+scurr.getISO_Code()+"-"+scurr.getCurSymbol()+"-"+scurr.getDescription();
		addLog(Msg_Header);
		// Destination Received
		Msg_Header = Msg.translate(Env.getCtx(), "C_AcctSchema_ID")+" "+
				 Msg.translate(Env.getCtx(), "To")+
				 ":"+target.getName();
		addLog(Msg_Header);
		Msg_Header = Msg.translate(Env.getCtx(), "C_Currency_ID")+" "+
				 Msg.translate(Env.getCtx(), "To")+
				 ":"+tcurr.getISO_Code()+"-"+tcurr.getCurSymbol()+"-"+tcurr.getDescription();
		addLog(Msg_Header);
		// START
		m_info = new StringBuffer();
		// ***********************************************************
		// SetupAMNSchema: AMN_Schema Table Dup Setup
		// ***********************************************************
		if (p_SetupAMNSchema.compareToIgnoreCase("Y") == 0 ) {
log.warning("p_SetupAMNSchema...OK");
			copyAMNAccSchemaDefault(source, target);
		}
		
		// ***********************************************************
		// SetupAMN_ConceptTypes: AMN_Concept_Types Table Dup Setup
		// ***********************************************************
		if (p_AMNConceptTypes.compareToIgnoreCase("Y") == 0 ) {
log.warning("p_AMNConceptTypes...OK");
			copyAMNConceptTypes(source, target);
		}
		addLog(getM_info().toString());
		return null;
	}

	
	/**
	 * copyAMNAccSchemaDefault
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyAMNAccSchemaDefault(MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{
		MAMN_Schema source = MAMN_Schema.get(Env.getCtx(), sourceAS.getC_AcctSchema_ID());
		MCurrency targetcurr = new MCurrency(Env.getCtx(),targetAS.getC_Currency_ID(),null);
		MAMN_Schema target = MAMN_Schema.get(Env.getCtx(), targetAS.getC_AcctSchema_ID());
		//log.warning("source="+source+" target="+target);
		if (target == null)
			target = new MAMN_Schema(Env.getCtx(), 0, null);
		target.setC_AcctSchema_ID(targetAS.getC_AcctSchema_ID());
		target.setName(source.getName()+" "+targetcurr.getISO_Code()+"-"+targetcurr.getCurSymbol());
		target.setDescription(source.getDescription()+" "+targetcurr.getISO_Code()+"-"+targetcurr.getCurSymbol());
		ArrayList<KeyNamePair> list = source.getAcctInfo();
		for (int i = 0; i < list.size(); i++)
		{
			KeyNamePair pp = list.get(i);
			int sourceC_ValidCombination_ID = pp.getKey();
			String columnName = pp.getName();
			MAccount sourceAccount = MAccount.get(Env.getCtx(), sourceC_ValidCombination_ID);
			MAccount targetAccount = MAMN_Schema.createAccount(sourceAS, targetAS, sourceAccount);
			target.setValue(columnName, new Integer(targetAccount.getC_ValidCombination_ID()));
			//log.warning("columnName="+columnName+"  sourceAccount="+sourceAccount);
		}
		if (!target.save())
			throw new AdempiereSystemError("Could not Save Default");
		//
		m_info.append(MAMN_Schema.Table_Name).append(": ").append("\r\n");
		m_info.append(Msg.translate(Env.getCtx(), "Updated")).append("\r\n");
	}	//	copyDefault
	
	//
	/**
	 * copyAMNConceptTypes
	 * @param sourceAS
	 * @param targetAS
	 * @throws Exception
	 */
	private static void copyAMNConceptTypes(MAcctSchema sourceAS, MAcctSchema targetAS) throws Exception
	{

		//
		m_info.append(MAMN_Concept_Types.Table_Name).append(": ").append("\r\n");
		m_info.append(Msg.translate(Env.getCtx(), "Updated")).append("\r\n");
		//
		MAMN_Schema source = MAMN_Schema.get(Env.getCtx(), sourceAS.getC_AcctSchema_ID());
		MAMN_Concept_Types_Acct actasource = null;
		MAMN_Concept_Types_Acct actatarget = null;
		MAccount sourceAccount = null;
		MAccount targetAccount = null;

		String CoValue="";
		String CoDescription="";
	    String sql="";
	    int AMN_Concept_Types_ID =0;
		//
		sql ="SELECT  " + 
			"cty.amn_concept_types_id, " + 
			"cty.value,  " + 
			"cty.description, " + 
			"cty.calcorder " + 
			"FROM amn_concept_types as cty " + 
			"WHERE cty.ad_client_id = " + source.getAD_Client_ID()  +
			" AND cty.isactive ='Y' "+
			"ORDER BY cty.calcorder" 
			;
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				AMN_Concept_Types_ID = rsod1.getInt(1);
				CoValue=rsod1.getString(2);
				CoDescription=rsod1.getString(3);
				MAMN_Concept_Types amcty = new MAMN_Concept_Types(Env.getCtx(),AMN_Concept_Types_ID,null);
				//  COPY OLD Account to C_AcctSchema_ID Acct
				m_info.append(CoValue+CoDescription).append("\r\n");
				actasource = MAMN_Concept_Types_Acct.findAMNConceptTypesAcct(Env.getCtx(), AMN_Concept_Types_ID, sourceAS.getC_AcctSchema_ID());
				if (actasource== null) {
					// get Old Default Account Combinations
					actasource = new MAMN_Concept_Types_Acct(Env.getCtx(),0,null);
					actasource.setAMN_Concept_Types_ID(AMN_Concept_Types_ID);
					actasource.setC_AcctSchema_ID(sourceAS.getC_AcctSchema_ID());
					actasource.setAD_Org_ID(amcty.getAD_Org_ID());
					actasource.setAMN_Cre_Acct(amcty.getAMN_Cre_Acct());
					actasource.setAMN_Cre_DW_Acct(amcty.getAMN_Cre_DW_Acct());
					actasource.setAMN_Cre_IW_Acct(amcty.getAMN_Cre_IW_Acct());
					actasource.setAMN_Cre_MW_Acct(amcty.getAMN_Cre_MW_Acct());
					actasource.setAMN_Cre_SW_Acct(amcty.getAMN_Cre_SW_Acct());
					actasource.setAMN_Deb_Acct(amcty.getAMN_Deb_Acct());
					actasource.setAMN_Deb_DW_Acct(amcty.getAMN_Deb_DW_Acct());
					actasource.setAMN_Deb_IW_Acct(amcty.getAMN_Deb_IW_Acct());
					actasource.setAMN_Deb_MW_Acct(amcty.getAMN_Deb_MW_Acct());
					actasource.setAMN_Deb_SW_Acct(amcty.getAMN_Deb_SW_Acct());
					actasource.save();
				}
log.warning("Concepto:"+rsod1.getString(4)+'-'+CoValue+'-'+CoDescription+"  acta="+actasource);
				actatarget = MAMN_Concept_Types_Acct.findAMNConceptTypesAcct(Env.getCtx(), AMN_Concept_Types_ID, targetAS.getC_AcctSchema_ID());
				if (actatarget== null) {
					actatarget = new MAMN_Concept_Types_Acct(Env.getCtx(),0,null);
				}
				actatarget.setAMN_Concept_Types_ID(AMN_Concept_Types_ID);
				actatarget.setC_AcctSchema_ID(targetAS.getC_AcctSchema_ID());
				actatarget.setAD_Org_ID(actasource.getAD_Org_ID());
				// AMN_Cre_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Cre_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Cre_Acct(targetAccount.getC_ValidCombination_ID());
				// AMN_Cre_DW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Cre_DW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Cre_DW_Acct(targetAccount.getC_ValidCombination_ID());
				// AMN_Cre_IW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Cre_IW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Cre_IW_Acct(targetAccount.getC_ValidCombination_ID());				
				// AMN_Cre_MW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Cre_MW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Cre_MW_Acct(targetAccount.getC_ValidCombination_ID());				
				// AMN_Cre_SW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Cre_SW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Cre_SW_Acct(targetAccount.getC_ValidCombination_ID());			
				// AMN_Deb_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Deb_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Deb_Acct(targetAccount.getC_ValidCombination_ID());
				// AMN_Deb_DW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Deb_DW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Deb_DW_Acct(targetAccount.getC_ValidCombination_ID());
				// AMN_Deb_IW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Deb_IW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Deb_IW_Acct(targetAccount.getC_ValidCombination_ID());				
				// AMN_Deb_MW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Deb_MW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Deb_MW_Acct(targetAccount.getC_ValidCombination_ID());				
				// AMN_Deb_SW_Acct
				sourceAccount = MAccount.get(Env.getCtx(), actasource.getAMN_Deb_SW_Acct());
				if (sourceAccount.getC_ValidCombination_ID()!= 0)
					targetAccount = getFirstVCcombination(Env.getCtx(),targetAS.getC_AcctSchema_ID(),sourceAccount.getCombination());
				targetAccount = createAccount(sourceAS, targetAS, sourceAccount, targetAccount);
				actatarget.setAMN_Deb_SW_Acct(targetAccount.getC_ValidCombination_ID());			
				
				actatarget.save();

			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; pstmt1 = null;
		}
		return;
		
		
	}	//	copyDefault


	/**
	 * 	Create Account
	 *	@param targetAS target AS
	 *	@param sourceAcct source account
	 *	@return target account
	 */
	private static MAccount createAccount(MAcctSchema sourceAS, MAcctSchema targetAS, MAccount sourceAcct, MAccount targetAcct)
	{
		MCurrency targetcurr = new MCurrency(Env.getCtx(),targetAS.getC_Currency_ID(),null);
		MAMN_Schema target = MAMN_Schema.get(Env.getCtx(), targetAS.getC_AcctSchema_ID());
		int AD_Client_ID = targetAS.getAD_Client_ID(); 
		int C_AcctSchema_ID = targetAS.getC_AcctSchema_ID();
		//
		int AD_Org_ID = 0;
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
		
		//  Active Elements
		MAcctSchemaElement[] elements = sourceAS.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			MAcctSchemaElement ase = elements[i];
			String elementType = ase.getElementType();
			//
			if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Organization))
				AD_Org_ID = sourceAcct.getAD_Org_ID();
			else if (elementType.equals(MAcctSchemaElement.ELEMENTTYPE_Account))
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
		if (sourceAcct.getAlias()!=null && targetcurr.getCurSymbol() != null)
			targetAcct.setAlias(sourceAcct.getAlias().trim()+" "+targetcurr.getCurSymbol());
		targetAcct.save();

		return targetAcct;
	}	//	createAccount
	
	/**
	 * 	Get first with combination
	 *	@param ctx context
	 *	@param C_AcctSchema_ID as
 	 *	@param combination combination
	 *	@return account
	 *
	 * Class for C_validCombination:  MAccount
	 */
	public static  MAccount getFirstVCcombination (Properties ctx, int C_AcctSchema_ID, String combination)
	{
		MAccount retValue = null;
		MAcctSchema mas = new MAcctSchema(ctx,C_AcctSchema_ID, null);
		
		int C_ValidCombination_ID=0;
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ON (combination) ")	//	1	
		.append(" C_ValidCombination_ID ")
		.append(" FROM C_ValidCombination ")
		.append(" WHERE AD_Client_ID= "+mas.getAD_Client_ID())
		.append(" AND C_AcctSchema_ID= "+C_AcctSchema_ID)
		.append(" AND combination= '"+combination+"'");
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

	public static StringBuffer getM_info() {
		return m_info;
	}

	public static void setM_info(StringBuffer m_info) {
		AMNPayrollReconversion.m_info = m_info;
	}
}

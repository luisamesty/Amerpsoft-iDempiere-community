package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.compiere.model.I_AD_ClientInfo;
import org.compiere.model.I_C_AcctSchema_Default;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaDefault;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.Query;
import org.compiere.model.X_C_AcctSchema_Default;
import org.compiere.util.AdempiereSystemError;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class MAMN_Schema extends X_AMN_Schema{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8985513130251543007L;
	/** Cache of Client AcctSchema Arrays		**/
	private static CCache<Integer,MAMN_Schema[]> s_schema = new CCache<Integer,MAMN_Schema[]>(I_AD_ClientInfo.Table_Name, 3);	//  3 clients
	/**	Cache of AcctSchemas 					**/
	private static CCache<Integer,MAMN_Schema> s_cache = new CCache<Integer,MAMN_Schema>(Table_Name, 3);	//  3 accounting schemas

	static CLogger log = CLogger.getCLogger(MAMN_Schema.class);

	public MAMN_Schema(Properties ctx, int AMN_Schema_ID, String trxName) {
		super(ctx, AMN_Schema_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMN_Schema(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 *  Get AMN AccountSchema of Client
	 * 	@param ctx context
	 *  @param C_AcctSchema_ID schema id
	 *  @param trxName optional trx
	 *  @return Accounting schema
	 */
	public static MAMN_Schema get (Properties ctx, int C_AcctSchema_ID, String trxName)
	{
		//  Check Cache
		Integer AMN_Schema_ID;
		MAMN_Schema retValue = null;
		String sql;
		sql = " SELECT DISTINCT ON (C_AcctSchema_ID) AMN_Schema_ID " +
			   " FROM AMN_Schema " + 
			   " WHERE C_AcctSchema_ID = "+ C_AcctSchema_ID;
		AMN_Schema_ID = DB.getSQLValue(null, sql);	
		if (AMN_Schema_ID != null) {
			retValue = new MAMN_Schema(ctx,AMN_Schema_ID,trxName);
		}
		return retValue;
		
	}	//	get
	
	/**
	 * 	Get AMN Accounting Schema Default Info
	 *	@param ctx context
	 *	@param C_AcctSchema_ID id
	 *	@return defaults
	 */
	public static MAMN_Schema get (Properties ctx, int C_AcctSchema_ID)
	{
		final String whereClause = "C_AcctSchema_ID=?";
		return new Query(ctx,MAMN_Schema.Table_Name,whereClause,null)
		.setParameters(C_AcctSchema_ID)
		.firstOnly();
	}	//	get
	/**
	 * 	Create Account
	 *	@param targetAS target AS
	 *	@param sourceAcct source account
	 *	@return target account
	 */
	public static MAccount createAccount(MAcctSchema sourceAS, MAcctSchema targetAS, MAccount sourceAcct)
	{
		
		MAccount retAccount = null;
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
		//
		retAccount = MAccount.get(Env.getCtx(), AD_Client_ID, AD_Org_ID,
			C_AcctSchema_ID, Account_ID, C_SubAcct_ID,
			M_Product_ID, C_BPartner_ID, AD_OrgTrx_ID,
			C_LocFrom_ID, C_LocTo_ID, C_SalesRegion_ID, 
			C_Project_ID, C_Campaign_ID, C_Activity_ID,
			User1_ID, User2_ID, UserElement1_ID, UserElement2_ID,
			null);
//log.warning("retAccount="+retAccount);
		return retAccount;
	}	//	createAccount

	
	/**
	 * 	Get Acct Info list 
	 *	@return list
	 */
	public ArrayList<KeyNamePair> getAcctInfo()
	{
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();
		for (int i = 0; i < get_ColumnCount(); i++)
		{
			String columnName = get_ColumnName(i);
			if (columnName.startsWith("AMN_P_"))
			{
				int id = ((Integer)get_Value(i));
				list.add(new KeyNamePair (id, columnName));
			}
			//log.warning("getAcctInfo ..columnName="+columnName);
		}
		return list;
	}	//	getAcctInfo

	/**
	 * 	Set Value (don't use)
	 *	@param columnName column name
	 *	@param value value
	 *	@return true if value set
	 */
	public boolean setValue (String columnName, Integer value)
	{
		return super.set_Value (columnName, value);
	}	//	setValue


}

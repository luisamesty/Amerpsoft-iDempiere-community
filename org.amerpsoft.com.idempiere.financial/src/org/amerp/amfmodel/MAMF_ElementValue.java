package org.amerp.amfmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MAMF_ElementValue extends X_C_ElementValue{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4472698395209169079L;
	// CLogger
	CLogger log = CLogger.getCLogger(MAMF_ElementValue.class);
		
	public MAMF_ElementValue(Properties ctx, int C_ElementValue_ID, String trxName) {
		super(ctx, C_ElementValue_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MAMF_ElementValue(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param Value value
	 *	@param Name name
	 *	@param Description description
	 *	@param AccountType account type
	 *	@param AccountSign account sign
	 *	@param IsDocControlled doc controlled
	 *	@param IsSummary summary
	 *	@param trxName transaction
	 */
	public MAMF_ElementValue (Properties ctx, String Value, String Name, String Description,
		String AccountType, String AccountSign,
		boolean IsDocControlled, boolean IsSummary, String trxName, String Value_Parent)
	{
		this (ctx, 0, trxName);
		setValue(Value);
		setName(Name);
		setDescription(Description);
		setAccountType(AccountType);
		setAccountSign(AccountSign);
		setIsDocControlled(IsDocControlled);
		setIsSummary(IsSummary);
		setValue_Parent(Value_Parent);
	}	//	MElementValue
	

}

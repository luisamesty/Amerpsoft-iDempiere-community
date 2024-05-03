package org.amerp.amxeditor.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MCity;


public class MCityExt extends MCity implements I_C_City_Amerp{

	private static final long serialVersionUID = 1144816383331439439L;

	public MCityExt(Properties ctx, int C_City_ID, String trxName) {
		super(ctx, C_City_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 *	Create City from current row in ResultSet
	 * 	@param ctx context
	 *  @param rs result set
	 *	@param trxName transaction
	 */
	public MCityExt (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCity
	
	/**
	 * Set Search Key.
	 * 
	 * @param Value Search key for the record in the format required - must be
	 *              unique
	 */
	public void setValue(String Value) {
		set_Value(COLUMNNAME_Value, Value);
	}

	/**
	 * Get Search Key.
	 * 
	 * @return Search key for the record in the format required - must be unique
	 */
	public String getValue() {
		return (String) get_Value(COLUMNNAME_Value);
	}

}

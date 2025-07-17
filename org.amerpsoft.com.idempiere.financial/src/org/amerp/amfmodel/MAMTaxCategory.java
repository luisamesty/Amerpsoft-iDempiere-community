package org.amerp.amfmodel;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MTaxCategory;
import org.compiere.util.CLogger;

public class MAMTaxCategory extends MTaxCategory implements I_C_TaxCategory{

	private static final long serialVersionUID = 3700181983962238364L;

	static CLogger log = CLogger.getCLogger(MAMTaxCategory.class);
	
	public MAMTaxCategory(Properties ctx, int C_TaxCategory_ID, String trxName) {
		super(ctx, C_TaxCategory_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMTaxCategory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMTaxCategory(Properties ctx, String C_TaxCategory_UU, String trxName) {
		super(ctx, C_TaxCategory_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Set Is Withholding.
	 * 
	 * @param IsWithholding Is Withholding
	 */
	public void setIsWithholding(boolean IsWithholding) {
		set_Value(COLUMNNAME_IsWithholding, Boolean.valueOf(IsWithholding));
	}

	/**
	 * Get Is Withholding.
	 * 
	 * @return Is Withholding
	 */
	public boolean isWithholding() {
		Object oo = get_Value(COLUMNNAME_IsWithholding);
		if (oo != null) {
			if (oo instanceof Boolean)
				return ((Boolean) oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** ISLR Withholding = ISLR */
	public static final String WITHHOLDINGFORMAT_ISLRWithholding = "ISLR";
	/** IVA Withholding = IVA */
	public static final String WITHHOLDINGFORMAT_IVAWithholding = "IVA";
	/** MUNICIPAL Withholding = MUNICIPAL */
	public static final String WITHHOLDINGFORMAT_MUNICIPALWithholding = "MUNICIPAL";
	
	/** Set Withholding Format.
		@param withholdingformat Indicates type of Withholding Format applied to Withholding Transaction
	*/
	public void setwithholdingformat (String withholdingformat)
	{

		set_Value (COLUMNNAME_withholdingformat, withholdingformat);
	}

	/** Get Withholding Format.
		@return Indicates type of Withholding Format applied to Withholding Transaction
	  */
	public String getwithholdingformat()
	{
		return (String)get_Value(COLUMNNAME_withholdingformat);
	}
	
}

package org.globalqss.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MLCOTaxCategory extends X_C_TaxCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5510958518866942946L;
	@SuppressWarnings("unused")
	private static CLogger	s_log	= CLogger.getCLogger (MLCOInvoiceWHDocLines.class);

	public MLCOTaxCategory(Properties ctx, int C_TaxCategory_ID, String trxName) {
		super(ctx, C_TaxCategory_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MLCOTaxCategory(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}



}

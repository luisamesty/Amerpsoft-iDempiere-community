/**********************************************************************
* This file is part of iDempiere ERP Open Source                      *
* http://www.idempiere.org                                            *
*                                                                     *
* Copyright (C) Contributors                                          *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
*                                                                     *
* Contributors:                                                       *
* - Carlos Ruiz - globalqss                                           *
**********************************************************************/

package org.globalqss.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MPriceList;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTax;
import org.compiere.model.MTaxCategory;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	LCO_CalloutWithholding
 *
 *  @author Carlos Ruiz - globalqss - Quality Systems & Solutions - http://globalqss.com
 */
public class LCO_CalloutWithholding implements IColumnCalloutFactory
{
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(LCO_CalloutWithholding.class);

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {
		if (! MSysConfig.getBooleanValue("LCO_USE_WITHHOLDINGS", true, Env.getAD_Client_ID(Env.getCtx())))
			return null;

		if (tableName.equalsIgnoreCase(I_LCO_WithholdingRule.Table_Name)) {
			if (columnName.equalsIgnoreCase(I_LCO_WithholdingRule.COLUMNNAME_LCO_WithholdingType_ID))
				return new IColumnCallout[]{new FillIsUse()};
		} else if (tableName.equalsIgnoreCase(I_LCO_InvoiceWithholding.Table_Name)) {
			if (columnName.equalsIgnoreCase(I_LCO_InvoiceWithholding.COLUMNNAME_C_Tax_ID))
				return new IColumnCallout[]{new FillPercentFromTax()};
			if (columnName.equalsIgnoreCase(I_LCO_InvoiceWithholding.COLUMNNAME_TaxBaseAmt))
				return new IColumnCallout[]{new Recalc_TaxAmt()};
		}
		else if (tableName.equalsIgnoreCase(I_LCO_InvoiceWHDoc.Table_Name)) {
			// Todo LCO_InvoiceWHDoc
		}
		else if (tableName.equalsIgnoreCase(I_LCO_InvoiceWHDocLines.Table_Name)) {
			// Todo LCO_InvoiceWHDocLines
			if (columnName.equalsIgnoreCase(I_LCO_InvoiceWHDocLines.COLUMNNAME_C_Invoice_ID))
				return new IColumnCallout[]{new InvoiceDocWHValues()};
		}
		return null;
	}

  private static class FillIsUse implements IColumnCallout {
	@Override
	public String start (Properties ctx, int WindowNo,
		GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		log.info("");
		int wht_id = ((Integer) mTab.getValue("LCO_WithholdingType_ID")).intValue();

		String sql = "SELECT IsUseBPISIC, IsUseBPTaxPayerType, IsUseBPCity, IsUseOrgISIC, IsUseOrgTaxPayerType, IsUseOrgCity, IsUseWithholdingCategory, IsUseProductTaxCategory "
			           + "FROM LCO_WithholdingRuleConf WHERE LCO_WithholdingType_ID=?";		//	#1

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, wht_id);
			rs = pstmt.executeQuery();
			//
			if (rs.next()) {
				mTab.setValue("IsUseBPISIC", rs.getString("IsUseBPISIC"));
				mTab.setValue("IsUseBPTaxPayerType", rs.getString("IsUseBPTaxPayerType"));
				mTab.setValue("IsUseBPCity", rs.getString("IsUseBPCity"));
				mTab.setValue("IsUseOrgISIC", rs.getString("IsUseOrgISIC"));
				mTab.setValue("IsUseOrgTaxPayerType", rs.getString("IsUseOrgTaxPayerType"));
				mTab.setValue("IsUseOrgCity", rs.getString("IsUseOrgCity"));
				mTab.setValue("IsUseWithholdingCategory", rs.getString("IsUseWithholdingCategory"));
				mTab.setValue("IsUseProductTaxCategory", rs.getString("IsUseProductTaxCategory"));
			} else {
				mTab.setValue("IsUseBPISIC", "N");
				mTab.setValue("IsUseBPTaxPayerType", "N");
				mTab.setValue("IsUseBPCity", "N");
				mTab.setValue("IsUseOrgISIC", "N");
				mTab.setValue("IsUseOrgTaxPayerType", "N");
				mTab.setValue("IsUseOrgCity", "N");
				mTab.setValue("IsUseWithholdingCategory", "N");
				mTab.setValue("IsUseProductTaxCategory", "N");
				log.warning("Rule not configured for withholding type");
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			return e.getLocalizedMessage();
		}
		finally
		{
			DB.close(rs, pstmt);
		}

		return "";
	}	//	fillIsUse
  }

  private static class FillPercentFromTax implements IColumnCallout {
	// Called from LCO_InvoiceWithholding.C_Tax_ID
	@Override
	public String start (Properties ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		log.info("");
		int taxint = 0;
		BigDecimal percent = null;
		if (value != null)
			taxint = ((Integer) value).intValue();
		if (taxint != 0) {
			MTax tax = new MTax(ctx, taxint, null);
			percent = tax.getRate();
		}
		mTab.setValue(MLCOInvoiceWithholding.COLUMNNAME_Percent, percent);

		return recalc_taxamt(ctx, WindowNo, mTab, mField, value, oldValue);
	}	//	fillPercentFromTax
  }

  private static class Recalc_TaxAmt implements IColumnCallout {
	// Called from LCO_InvoiceWithholding.C_Tax_ID and tax base
	@Override
	public String start(Properties ctx, int WindowNo,
			GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		log.info("");

		return recalc_taxamt(ctx, WindowNo, mTab, mField, value, oldValue);
	}	//	recalc_taxamt

  }

	private static String recalc_taxamt(Properties ctx, int WindowNo, GridTab mTab, GridField mField,
			Object value, Object oldValue) {
		// don't recalculate if callout called from field TaxBaseAmt and didn't change
		if (mField.getColumnName().equals("TaxBaseAmt") && value != null && oldValue != null) {
			BigDecimal newtaxbaseamt = (BigDecimal) value;
			BigDecimal oldtaxbaseamt = (BigDecimal) oldValue;
			if (newtaxbaseamt.compareTo(oldtaxbaseamt) == 0) {
				// the field hasn't changed, don't recalc
				return "";
			}
		}

		BigDecimal percent = (BigDecimal) mTab.getValue(MLCOInvoiceWithholding.COLUMNNAME_Percent);
		BigDecimal taxbaseamt = (BigDecimal) mTab.getValue(MLCOInvoiceWithholding.COLUMNNAME_TaxBaseAmt);

		BigDecimal taxamt = null;
		if (percent != null && taxbaseamt != null) {
			int pricelist_id = DB.getSQLValue(null,
					"SELECT M_PriceList_ID FROM C_Invoice WHERE C_Invoice_ID=?",
					((Integer) mTab.getValue(MLCOInvoiceWithholding.COLUMNNAME_C_Invoice_ID)).intValue());
			taxamt = percent.multiply(taxbaseamt).divide(Env.ONEHUNDRED);
			int stdPrecision = MPriceList.getStandardPrecision(ctx, pricelist_id);
			taxamt = taxamt.setScale(stdPrecision, BigDecimal.ROUND_HALF_UP);
		}
		mTab.setValue(MLCOInvoiceWithholding.COLUMNNAME_TaxAmt, taxamt);

		return "";
	}

	// NEW INvoiceWithholdingDocs
	private static class InvoiceDocWHValues implements IColumnCallout {
		// Called from LCO_InvoiceWithholding.C_Tax_ID
		@Override
		public String start (Properties ctx, int WindowNo,
				GridTab mTab, GridField mField, Object value, Object oldValue)
		{
			
			if ((Integer) mTab.getValue(MLCOInvoiceWHDocLines.COLUMNNAME_C_Invoice_ID) != null) {
				int C_Invoice_ID = (Integer) mTab.getValue(MLCOInvoiceWHDocLines.COLUMNNAME_C_Invoice_ID);
				LCO_MInvoice lcominvoice = new LCO_MInvoice(ctx, C_Invoice_ID , null);
				BigDecimal GrandTotal = lcominvoice.getGrandTotal(); 
				BigDecimal TotalLines = lcominvoice.getTotalLines();
				BigDecimal WithholdingAmt = DB.getSQLValueBD(null,
						"SELECT WithholdingAmt FROM C_Invoice WHERE C_Invoice_ID=?",
						lcominvoice.getC_Invoice_ID());
				//log.warning("C_Invoice_ID:"+C_Invoice_ID+"  GrandTotal:"+GrandTotal+"  TotalLines:"+TotalLines+"  WithholdingAmt:"+WithholdingAmt);
				// PArent TAB
				GridTab p_mTabp = mTab.getParentTab();
				// Header Values
				//log.warning("p_mTabp.getRecord_ID()="+p_mTabp.getRecord_ID());	
				int LCO_InvoiceWHDoc_ID=p_mTabp.getRecord_ID();
				MLCOInvoiceWHDoc lcowhdoc = new MLCOInvoiceWHDoc(ctx, LCO_InvoiceWHDoc_ID, null);
				Integer C_TaxCategory_ID = lcowhdoc.getC_TaxCategory_ID();
				String WithHoldingFormat = "IVA";  //Default Value When TaxCategory_ID is Empty for Old Records
				if (C_TaxCategory_ID != 0) {
					MLCOTaxCategory tcat = new MLCOTaxCategory(ctx, C_TaxCategory_ID, null);
					WithHoldingFormat = tcat.getwithholdingformat().trim();
				}
				// Set Set Invoice Values
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_GrandTotal,GrandTotal);
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_TotalLines,TotalLines);
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_WithholdingAmt,WithholdingAmt);
				String sql = "";
				// LCO_InvoiceWithholding
//				sql = "SELECT LCO_InvoiceWithholding_ID " +
//						"FROM adempiere.LCO_InvoiceWithholding as liw "+
//						"LEFT JOIN C_Tax as ctx ON (ctx.C_Tax_ID = liw.C_Tax_ID ) "+ 
//						"WHERE ctx.C_TaxCategory_ID IN (SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE WithHoldingFormat='IVA' ) " +
//						"AND C_Invoice_ID=? " ;
				sql = "SELECT LCO_InvoiceWithholding_ID " +
						"FROM adempiere.LCO_InvoiceWithholding as liw "+
						"LEFT JOIN C_Tax as ctx ON (ctx.C_Tax_ID = liw.C_Tax_ID ) "+ 
						"WHERE ctx.C_TaxCategory_ID IN (SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE WithHoldingFormat='"+WithHoldingFormat+"' ) " +
						"AND C_Invoice_ID=? " ;
				int LCO_InvoiceWithholding_ID = DB.getSQLValue(null,sql,C_Invoice_ID);
				// MLCOInvoiceWithholding
				MLCOInvoiceWithholding lcoinvoicewh = new MLCOInvoiceWithholding(ctx, LCO_InvoiceWithholding_ID, null);
				// Set Invoice Withholding Values
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_Percent, lcoinvoicewh.getPercent());
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_TaxAmt, lcoinvoicewh.getTaxAmt());
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_TaxBaseAmt, lcoinvoicewh.getTaxBaseAmt());
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_LCO_InvoiceWithholding_ID, lcoinvoicewh.getLCO_InvoiceWithholding_ID());
				boolean isSOTrx = false;
				String Description =  Msg.getElement(ctx, "C_Invoice_ID", isSOTrx )+":"+lcominvoice.getDocumentNo()+"  "+ Msg.getElement(ctx, "DateInvoiced", isSOTrx ) +":"+lcominvoice.getDateInvoiced();
				mTab.setValue(MLCOInvoiceWHDocLines.COLUMNNAME_Description, Description);

			}
			return "";
		}	//	
	  }

}	//	LCO_CalloutWithholding

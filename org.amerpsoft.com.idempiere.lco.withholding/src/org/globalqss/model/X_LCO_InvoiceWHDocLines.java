/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.globalqss.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for LCO_InvoiceWHDocLines
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_LCO_InvoiceWHDocLines extends PO implements I_LCO_InvoiceWHDocLines, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190718L;

    /** Standard Constructor */
    public X_LCO_InvoiceWHDocLines (Properties ctx, int LCO_InvoiceWHDocLines_ID, String trxName)
    {
      super (ctx, LCO_InvoiceWHDocLines_ID, trxName);
      /** if (LCO_InvoiceWHDocLines_ID == 0)
        {
			setC_Invoice_ID (0);
			setLCO_InvoiceWHDocLines_ID (0);
			setLCO_InvoiceWithholding_ID (0);
        } */
    }

    /** Load Constructor */
    public X_LCO_InvoiceWHDocLines (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_LCO_InvoiceWHDocLines[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Grand Total.
		@param GrandTotal 
		Total amount of document
	  */
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_ValueNoCheck (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Invoice Withholding Document Lines.
		@param LCO_InvoiceWHDocLines_ID Invoice Withholding Document Lines	  */
	public void setLCO_InvoiceWHDocLines_ID (int LCO_InvoiceWHDocLines_ID)
	{
		if (LCO_InvoiceWHDocLines_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LCO_InvoiceWHDocLines_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LCO_InvoiceWHDocLines_ID, Integer.valueOf(LCO_InvoiceWHDocLines_ID));
	}

	/** Get Invoice Withholding Document Lines.
		@return Invoice Withholding Document Lines	  */
	public int getLCO_InvoiceWHDocLines_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_InvoiceWHDocLines_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set LCO_InvoiceWHDocLines_UU.
		@param LCO_InvoiceWHDocLines_UU LCO_InvoiceWHDocLines_UU	  */
	public void setLCO_InvoiceWHDocLines_UU (String LCO_InvoiceWHDocLines_UU)
	{
		set_Value (COLUMNNAME_LCO_InvoiceWHDocLines_UU, LCO_InvoiceWHDocLines_UU);
	}

	/** Get LCO_InvoiceWHDocLines_UU.
		@return LCO_InvoiceWHDocLines_UU	  */
	public String getLCO_InvoiceWHDocLines_UU () 
	{
		return (String)get_Value(COLUMNNAME_LCO_InvoiceWHDocLines_UU);
	}

	public org.globalqss.model.I_LCO_InvoiceWHDoc getLCO_InvoiceWHDoc() throws RuntimeException
    {
		return (org.globalqss.model.I_LCO_InvoiceWHDoc)MTable.get(getCtx(), org.globalqss.model.I_LCO_InvoiceWHDoc.Table_Name)
			.getPO(getLCO_InvoiceWHDoc_ID(), get_TrxName());	}

	/** Set Invoice Withholding Document.
		@param LCO_InvoiceWHDoc_ID Invoice Withholding Document	  */
	public void setLCO_InvoiceWHDoc_ID (int LCO_InvoiceWHDoc_ID)
	{
		if (LCO_InvoiceWHDoc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LCO_InvoiceWHDoc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LCO_InvoiceWHDoc_ID, Integer.valueOf(LCO_InvoiceWHDoc_ID));
	}

	/** Get Invoice Withholding Document.
		@return Invoice Withholding Document	  */
	public int getLCO_InvoiceWHDoc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_InvoiceWHDoc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.globalqss.model.I_LCO_InvoiceWithholding getLCO_InvoiceWithholding() throws RuntimeException
    {
		return (org.globalqss.model.I_LCO_InvoiceWithholding)MTable.get(getCtx(), org.globalqss.model.I_LCO_InvoiceWithholding.Table_Name)
			.getPO(getLCO_InvoiceWithholding_ID(), get_TrxName());	}

	/** Set Invoice Withholding.
		@param LCO_InvoiceWithholding_ID Invoice Withholding	  */
	public void setLCO_InvoiceWithholding_ID (int LCO_InvoiceWithholding_ID)
	{
		if (LCO_InvoiceWithholding_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_LCO_InvoiceWithholding_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_LCO_InvoiceWithholding_ID, Integer.valueOf(LCO_InvoiceWithholding_ID));
	}

	/** Get Invoice Withholding.
		@return Invoice Withholding	  */
	public int getLCO_InvoiceWithholding_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LCO_InvoiceWithholding_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line No.
		@param Line 
		Unique line for this document
	  */
	public void setLine (int Line)
	{
		set_ValueNoCheck (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Percent.
		@param Percent 
		Percentage
	  */
	public void setPercent (BigDecimal Percent)
	{
		set_ValueNoCheck (COLUMNNAME_Percent, Percent);
	}

	/** Get Percent.
		@return Percentage
	  */
	public BigDecimal getPercent () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Tax Amount.
		@param TaxAmt 
		Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_ValueNoCheck (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	public BigDecimal getTaxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax base Amount.
		@param TaxBaseAmt 
		Base for calculating the tax amount
	  */
	public void setTaxBaseAmt (BigDecimal TaxBaseAmt)
	{
		set_ValueNoCheck (COLUMNNAME_TaxBaseAmt, TaxBaseAmt);
	}

	/** Get Tax base Amount.
		@return Base for calculating the tax amount
	  */
	public BigDecimal getTaxBaseAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxBaseAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Total Lines.
		@param TotalLines 
		Total of all document lines
	  */
	public void setTotalLines (BigDecimal TotalLines)
	{
		set_ValueNoCheck (COLUMNNAME_TotalLines, TotalLines);
	}

	/** Get Total Lines.
		@return Total of all document lines
	  */
	public BigDecimal getTotalLines () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalLines);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Withholding Amount.
		@param WithholdingAmt Withholding Amount	  */
	public void setWithholdingAmt (BigDecimal WithholdingAmt)
	{
		set_ValueNoCheck (COLUMNNAME_WithholdingAmt, WithholdingAmt);
	}

	/** Get Withholding Amount.
		@return Withholding Amount	  */
	public BigDecimal getWithholdingAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_WithholdingAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
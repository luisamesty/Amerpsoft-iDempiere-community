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
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for AMN_Payroll_Docs
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Payroll_Docs")
public class X_AMN_Payroll_Docs extends PO implements I_AMN_Payroll_Docs, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250213L;

    /** Standard Constructor */
    public X_AMN_Payroll_Docs (Properties ctx, int AMN_Payroll_Docs_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Docs_ID, trxName);
      /** if (AMN_Payroll_Docs_ID == 0)
        {
			setAMN_Payroll_Docs_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Docs (Properties ctx, int AMN_Payroll_Docs_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Docs_ID, trxName, virtualColumns);
      /** if (AMN_Payroll_Docs_ID == 0)
        {
			setAMN_Payroll_Docs_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Docs (Properties ctx, String AMN_Payroll_Docs_UU, String trxName)
    {
      super (ctx, AMN_Payroll_Docs_UU, trxName);
      /** if (AMN_Payroll_Docs_UU == null)
        {
			setAMN_Payroll_Docs_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Docs (Properties ctx, String AMN_Payroll_Docs_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Docs_UU, trxName, virtualColumns);
      /** if (AMN_Payroll_Docs_UU == null)
        {
			setAMN_Payroll_Docs_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Docs (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
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
      StringBuilder sb = new StringBuilder ("X_AMN_Payroll_Docs[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public I_AMN_Concept_Types getAMN_Concept_Types() throws RuntimeException
	{
		return (I_AMN_Concept_Types)MTable.get(getCtx(), I_AMN_Concept_Types.Table_ID)
			.getPO(getAMN_Concept_Types_ID(), get_TrxName());
	}

	/** Set Payroll Concepts Types.
		@param AMN_Concept_Types_ID Payroll Concepts Types
	*/
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID)
	{
		if (AMN_Concept_Types_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_ID, Integer.valueOf(AMN_Concept_Types_ID));
	}

	/** Get Payroll Concepts Types.
		@return Payroll Concepts Types	  */
	public int getAMN_Concept_Types_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Documents Associated.
		@param AMN_Payroll_Docs_ID Payroll Documents Associated with a Payroll Receipt
	*/
	public void setAMN_Payroll_Docs_ID (int AMN_Payroll_Docs_ID)
	{
		if (AMN_Payroll_Docs_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Docs_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Docs_ID, Integer.valueOf(AMN_Payroll_Docs_ID));
	}

	/** Get Payroll Documents Associated.
		@return Payroll Documents Associated with a Payroll Receipt
	  */
	public int getAMN_Payroll_Docs_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Docs_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Docs_UU.
		@param AMN_Payroll_Docs_UU AMN_Payroll_Docs_UU
	*/
	public void setAMN_Payroll_Docs_UU (String AMN_Payroll_Docs_UU)
	{
		set_Value (COLUMNNAME_AMN_Payroll_Docs_UU, AMN_Payroll_Docs_UU);
	}

	/** Get AMN_Payroll_Docs_UU.
		@return AMN_Payroll_Docs_UU	  */
	public String getAMN_Payroll_Docs_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Docs_UU);
	}

	public I_AMN_Payroll getAMN_Payroll() throws RuntimeException
	{
		return (I_AMN_Payroll)MTable.get(getCtx(), I_AMN_Payroll.Table_ID)
			.getPO(getAMN_Payroll_ID(), get_TrxName());
	}

	/** Set Payroll Invoice.
		@param AMN_Payroll_ID Payroll Invoice
	*/
	public void setAMN_Payroll_ID (int AMN_Payroll_ID)
	{
		if (AMN_Payroll_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_ID, Integer.valueOf(AMN_Payroll_ID));
	}

	/** Get Payroll Invoice.
		@return Payroll Invoice	  */
	public int getAMN_Payroll_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocType_ID(), get_TrxName());
	}

	/** Set Document Type.
		@param C_DocType_ID Document type or rules
	*/
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0)
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException
	{
		return (org.compiere.model.I_C_Invoice)MTable.get(getCtx(), org.compiere.model.I_C_Invoice.Table_ID)
			.getPO(getC_Invoice_ID(), get_TrxName());
	}

	/** Set Invoice.
		@param C_Invoice_ID Invoice Identifier
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
	public int getC_Invoice_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Grand Total.
		@param GrandTotal Total amount of document
	*/
	public void setGrandTotal (BigDecimal GrandTotal)
	{
		set_ValueNoCheck (COLUMNNAME_GrandTotal, GrandTotal);
	}

	/** Get Grand Total.
		@return Total amount of document
	  */
	public BigDecimal getGrandTotal()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_GrandTotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
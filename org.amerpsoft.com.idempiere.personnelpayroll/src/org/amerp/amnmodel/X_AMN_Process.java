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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for AMN_Process
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Process")
public class X_AMN_Process extends PO implements I_AMN_Process, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250205L;

    /** Standard Constructor */
    public X_AMN_Process (Properties ctx, int AMN_Process_ID, String trxName)
    {
      super (ctx, AMN_Process_ID, trxName);
      /** if (AMN_Process_ID == 0)
        {
			setAMN_Process_ID (0);
			setAMN_Process_Value (null);
			setisAllowLotInvoices (false);
// N
			setIsDocControlled (false);
// N
			setName (null);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Process (Properties ctx, int AMN_Process_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Process_ID, trxName, virtualColumns);
      /** if (AMN_Process_ID == 0)
        {
			setAMN_Process_ID (0);
			setAMN_Process_Value (null);
			setisAllowLotInvoices (false);
// N
			setIsDocControlled (false);
// N
			setName (null);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Process (Properties ctx, String AMN_Process_UU, String trxName)
    {
      super (ctx, AMN_Process_UU, trxName);
      /** if (AMN_Process_UU == null)
        {
			setAMN_Process_ID (0);
			setAMN_Process_Value (null);
			setisAllowLotInvoices (false);
// N
			setIsDocControlled (false);
// N
			setName (null);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Process (Properties ctx, String AMN_Process_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Process_UU, trxName, virtualColumns);
      /** if (AMN_Process_UU == null)
        {
			setAMN_Process_ID (0);
			setAMN_Process_Value (null);
			setisAllowLotInvoices (false);
// N
			setIsDocControlled (false);
// N
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Process (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client
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
      StringBuilder sb = new StringBuilder ("X_AMN_Process[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Payroll Process.
		@param AMN_Process_ID Payroll Process
	*/
	public void setAMN_Process_ID (int AMN_Process_ID)
	{
		if (AMN_Process_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Process_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Process_ID, Integer.valueOf(AMN_Process_ID));
	}

	/** Get Payroll Process.
		@return Payroll Process	  */
	public int getAMN_Process_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Process_UU.
		@param AMN_Process_UU AMN_Process_UU
	*/
	public void setAMN_Process_UU (String AMN_Process_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Process_UU, AMN_Process_UU);
	}

	/** Get AMN_Process_UU.
		@return AMN_Process_UU	  */
	public String getAMN_Process_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Process_UU);
	}

	/** Set Process Value.
		@param AMN_Process_Value Process Value
	*/
	public void setAMN_Process_Value (String AMN_Process_Value)
	{
		set_Value (COLUMNNAME_AMN_Process_Value, AMN_Process_Value);
	}

	/** Get Process Value.
		@return Process Value	  */
	public String getAMN_Process_Value()
	{
		return (String)get_Value(COLUMNNAME_AMN_Process_Value);
	}

	public org.compiere.model.I_C_DocType getC_DocTypeCreditMemo() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocTypeCreditMemo_ID(), get_TrxName());
	}

	/** Set Document Type for Credit Memo.
		@param C_DocTypeCreditMemo_ID Document type used for credit memo generated from this document
	*/
	public void setC_DocTypeCreditMemo_ID (int C_DocTypeCreditMemo_ID)
	{
		if (C_DocTypeCreditMemo_ID < 1)
			set_Value (COLUMNNAME_C_DocTypeCreditMemo_ID, null);
		else
			set_Value (COLUMNNAME_C_DocTypeCreditMemo_ID, Integer.valueOf(C_DocTypeCreditMemo_ID));
	}

	/** Get Document Type for Credit Memo.
		@return Document type used for credit memo generated from this document
	  */
	public int getC_DocTypeCreditMemo_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeCreditMemo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocTypeTarget_ID(), get_TrxName());
	}

	/** Set Target Document Type.
		@param C_DocTypeTarget_ID Target document type for conversing documents
	*/
	public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
	{
		if (C_DocTypeTarget_ID < 1)
			set_Value (COLUMNNAME_C_DocTypeTarget_ID, null);
		else
			set_Value (COLUMNNAME_C_DocTypeTarget_ID, Integer.valueOf(C_DocTypeTarget_ID));
	}

	/** Get Target Document Type.
		@return Target document type for conversing documents
	  */
	public int getC_DocTypeTarget_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeTarget_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Allow Lot Invoices.
		@param isAllowLotInvoices Allow Lot Invoices
	*/
	public void setisAllowLotInvoices (boolean isAllowLotInvoices)
	{
		set_Value (COLUMNNAME_isAllowLotInvoices, Boolean.valueOf(isAllowLotInvoices));
	}

	/** Get Allow Lot Invoices.
		@return Allow Lot Invoices	  */
	public boolean isAllowLotInvoices()
	{
		Object oo = get_Value(COLUMNNAME_isAllowLotInvoices);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Document Controlled.
		@param IsDocControlled Control account - If an account is controlled by a document, you cannot post manually to it
	*/
	public void setIsDocControlled (boolean IsDocControlled)
	{
		set_Value (COLUMNNAME_IsDocControlled, Boolean.valueOf(IsDocControlled));
	}

	/** Get Document Controlled.
		@return Control account - If an account is controlled by a document, you cannot post manually to it
	  */
	public boolean isDocControlled()
	{
		Object oo = get_Value(COLUMNNAME_IsDocControlled);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_ValueNoCheck (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
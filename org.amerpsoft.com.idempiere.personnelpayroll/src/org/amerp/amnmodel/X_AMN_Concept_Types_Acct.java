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

/** Generated Model for AMN_Concept_Types_Acct
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Concept_Types_Acct extends PO implements I_AMN_Concept_Types_Acct, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180529L;

    /** Standard Constructor */
    public X_AMN_Concept_Types_Acct (Properties ctx, int AMN_Concept_Types_Acct_ID, String trxName)
    {
      super (ctx, AMN_Concept_Types_Acct_ID, trxName);
      /** if (AMN_Concept_Types_Acct_ID == 0)
        {
			setAMN_Concept_Types_Acct_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AMN_Concept_Types_Acct (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Concept_Types_Acct[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_ElementValue getAMN_CT_Cre_Acct() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAMN_CT_Cre_Acct_ID(), get_TrxName());	}

	/** Set Payroll Concept Type Credit Account.
		@param AMN_CT_Cre_Acct_ID 
		Payroll Concept Type Credit Account
	  */
	public void setAMN_CT_Cre_Acct_ID (int AMN_CT_Cre_Acct_ID)
	{
		if (AMN_CT_Cre_Acct_ID < 1) 
			set_Value (COLUMNNAME_AMN_CT_Cre_Acct_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_CT_Cre_Acct_ID, Integer.valueOf(AMN_CT_Cre_Acct_ID));
	}

	/** Get Payroll Concept Type Credit Account.
		@return Payroll Concept Type Credit Account
	  */
	public int getAMN_CT_Cre_Acct_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_CT_Cre_Acct_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ElementValue getAMN_CT_Deb_Acct() throws RuntimeException
    {
		return (org.compiere.model.I_C_ElementValue)MTable.get(getCtx(), org.compiere.model.I_C_ElementValue.Table_Name)
			.getPO(getAMN_CT_Deb_Acct_ID(), get_TrxName());	}

	/** Set Payroll Concept Type Debit Account.
		@param AMN_CT_Deb_Acct_ID 
		Payroll Concept Type Debit Account
	  */
	public void setAMN_CT_Deb_Acct_ID (int AMN_CT_Deb_Acct_ID)
	{
		if (AMN_CT_Deb_Acct_ID < 1) 
			set_Value (COLUMNNAME_AMN_CT_Deb_Acct_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_CT_Deb_Acct_ID, Integer.valueOf(AMN_CT_Deb_Acct_ID));
	}

	/** Get Payroll Concept Type Debit Account.
		@return Payroll Concept Type Debit Account
	  */
	public int getAMN_CT_Deb_Acct_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_CT_Deb_Acct_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Concepts Types Accounts.
		@param AMN_Concept_Types_Acct_ID Payroll Concepts Types Accounts	  */
	public void setAMN_Concept_Types_Acct_ID (int AMN_Concept_Types_Acct_ID)
	{
		if (AMN_Concept_Types_Acct_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Acct_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Acct_ID, Integer.valueOf(AMN_Concept_Types_Acct_ID));
	}

	/** Get Payroll Concepts Types Accounts.
		@return Payroll Concepts Types Accounts	  */
	public int getAMN_Concept_Types_Acct_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_Acct_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Concept_Types_Acct_UU.
		@param AMN_Concept_Types_Acct_UU AMN_Concept_Types_Acct_UU	  */
	public void setAMN_Concept_Types_Acct_UU (String AMN_Concept_Types_Acct_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Acct_UU, AMN_Concept_Types_Acct_UU);
	}

	/** Get AMN_Concept_Types_Acct_UU.
		@return AMN_Concept_Types_Acct_UU	  */
	public String getAMN_Concept_Types_Acct_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Concept_Types_Acct_UU);
	}

	/** Set Payroll Concepts Types.
		@param AMN_Concept_Types_ID Payroll Concepts Types	  */
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID)
	{
		if (AMN_Concept_Types_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_ID, Integer.valueOf(AMN_Concept_Types_ID));
	}

	/** Get Payroll Concepts Types.
		@return Payroll Concepts Types	  */
	public int getAMN_Concept_Types_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Cre_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Cre_Acct(), get_TrxName());	}

	/** Set Concept Credit Administrative Work Force Account.
		@param AMN_Cre_Acct 
		Concept Credit Administrative Work Force Account
	  */
	public void setAMN_Cre_Acct (int AMN_Cre_Acct)
	{
		set_Value (COLUMNNAME_AMN_Cre_Acct, Integer.valueOf(AMN_Cre_Acct));
	}

	/** Get Concept Credit Administrative Work Force Account.
		@return Concept Credit Administrative Work Force Account
	  */
	public int getAMN_Cre_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Cre_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Cre_DW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Cre_DW_Acct(), get_TrxName());	}

	/** Set Concept Credit Direct Work Force Account.
		@param AMN_Cre_DW_Acct 
		Concept Credit Direct Work Force Account
	  */
	public void setAMN_Cre_DW_Acct (int AMN_Cre_DW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Cre_DW_Acct, Integer.valueOf(AMN_Cre_DW_Acct));
	}

	/** Get Concept Credit Direct Work Force Account.
		@return Concept Credit Direct Work Force Account
	  */
	public int getAMN_Cre_DW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Cre_DW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Cre_IW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Cre_IW_Acct(), get_TrxName());	}

	/** Set Concept Credit Indirect Work Force Account.
		@param AMN_Cre_IW_Acct 
		Concept Credit Indirect Work Force Account
	  */
	public void setAMN_Cre_IW_Acct (int AMN_Cre_IW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Cre_IW_Acct, Integer.valueOf(AMN_Cre_IW_Acct));
	}

	/** Get Concept Credit Indirect Work Force Account.
		@return Concept Credit Indirect Work Force Account
	  */
	public int getAMN_Cre_IW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Cre_IW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Cre_MW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Cre_MW_Acct(), get_TrxName());	}

	/** Set Concept Credit Management Work Force Account.
		@param AMN_Cre_MW_Acct 
		Concept Credit Management Work Force Account
	  */
	public void setAMN_Cre_MW_Acct (int AMN_Cre_MW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Cre_MW_Acct, Integer.valueOf(AMN_Cre_MW_Acct));
	}

	/** Get Concept Credit Management Work Force Account.
		@return Concept Credit Management Work Force Account
	  */
	public int getAMN_Cre_MW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Cre_MW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Cre_SW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Cre_SW_Acct(), get_TrxName());	}

	/** Set Concept Credit Sales Work Force Account.
		@param AMN_Cre_SW_Acct 
		Concept Credit Sales Work Force Account
	  */
	public void setAMN_Cre_SW_Acct (int AMN_Cre_SW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Cre_SW_Acct, Integer.valueOf(AMN_Cre_SW_Acct));
	}

	/** Get Concept Credit Sales Work Force Account.
		@return Concept Credit Sales Work Force Account
	  */
	public int getAMN_Cre_SW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Cre_SW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Deb_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Deb_Acct(), get_TrxName());	}

	/** Set Concept Debit Administrative Work Force  Account.
		@param AMN_Deb_Acct 
		Concept Debit Administrative Work Force  Account
	  */
	public void setAMN_Deb_Acct (int AMN_Deb_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_Acct, Integer.valueOf(AMN_Deb_Acct));
	}

	/** Get Concept Debit Administrative Work Force  Account.
		@return Concept Debit Administrative Work Force  Account
	  */
	public int getAMN_Deb_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Deb_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Deb_DW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Deb_DW_Acct(), get_TrxName());	}

	/** Set Concept Debit Direct Work Force Account.
		@param AMN_Deb_DW_Acct 
		Concept Debit Direct Work Force Debit Account
	  */
	public void setAMN_Deb_DW_Acct (int AMN_Deb_DW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_DW_Acct, Integer.valueOf(AMN_Deb_DW_Acct));
	}

	/** Get Concept Debit Direct Work Force Account.
		@return Concept Debit Direct Work Force Debit Account
	  */
	public int getAMN_Deb_DW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Deb_DW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Deb_IW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Deb_IW_Acct(), get_TrxName());	}

	/** Set Concept Debit Indirect Work Force Account.
		@param AMN_Deb_IW_Acct 
		Concept Debit Indirect Work Force Debit Account
	  */
	public void setAMN_Deb_IW_Acct (int AMN_Deb_IW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_IW_Acct, Integer.valueOf(AMN_Deb_IW_Acct));
	}

	/** Get Concept Debit Indirect Work Force Account.
		@return Concept Debit Indirect Work Force Debit Account
	  */
	public int getAMN_Deb_IW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Deb_IW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Deb_MW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Deb_MW_Acct(), get_TrxName());	}

	/** Set Concept Debit Management Work Force Account.
		@param AMN_Deb_MW_Acct 
		Concept Debit Management Work Force Debit Account
	  */
	public void setAMN_Deb_MW_Acct (int AMN_Deb_MW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_MW_Acct, Integer.valueOf(AMN_Deb_MW_Acct));
	}

	/** Get Concept Debit Management Work Force Account.
		@return Concept Debit Management Work Force Debit Account
	  */
	public int getAMN_Deb_MW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Deb_MW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_Deb_SW_A() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getAMN_Deb_SW_Acct(), get_TrxName());	}

	/** Set Concept Debit Sales Work Force Account.
		@param AMN_Deb_SW_Acct 
		Concept Debit Sales Work Force  Debit Account
	  */
	public void setAMN_Deb_SW_Acct (int AMN_Deb_SW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_SW_Acct, Integer.valueOf(AMN_Deb_SW_Acct));
	}

	/** Get Concept Debit Sales Work Force Account.
		@return Concept Debit Sales Work Force  Debit Account
	  */
	public int getAMN_Deb_SW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Deb_SW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException
    {
		return (org.compiere.model.I_C_AcctSchema)MTable.get(getCtx(), org.compiere.model.I_C_AcctSchema.Table_Name)
			.getPO(getC_AcctSchema_ID(), get_TrxName());	}

	/** Set Accounting Schema.
		@param C_AcctSchema_ID 
		Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID)
	{
		if (C_AcctSchema_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, Integer.valueOf(C_AcctSchema_ID));
	}

	/** Get Accounting Schema.
		@return Rules for accounting
	  */
	public int getC_AcctSchema_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AcctSchema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Copy From.
		@param CopyFrom 
		Copy From Record
	  */
	public void setCopyFrom (String CopyFrom)
	{
		set_Value (COLUMNNAME_CopyFrom, CopyFrom);
	}

	/** Get Copy From.
		@return Copy From Record
	  */
	public String getCopyFrom () 
	{
		return (String)get_Value(COLUMNNAME_CopyFrom);
	}
}
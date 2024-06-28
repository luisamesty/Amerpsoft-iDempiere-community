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

/** Generated Model for AMN_Concept_Types
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Concept_Types extends PO implements I_AMN_Concept_Types, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170404L;

    /** Standard Constructor */
    public X_AMN_Concept_Types (Properties ctx, int AMN_Concept_Types_ID, String trxName)
    {
      super (ctx, AMN_Concept_Types_ID, trxName);
      /** if (AMN_Concept_Types_ID == 0)
        {
			setAMN_Concept_Types_ID (0);
			setAMN_Concept_Uom_ID (0);
			setCalcOrder (0);
			setName (null);
			setValue (null);
			setdaydiscriminated (false);
			setformula (null);
			setisDate (false);
			setisQty (false);
			setisValue (false);
			setisrepeat (null);
			setisshow (null);
			setoptmode (null);
			setrule (null);
			setsign (null);
			setvariable (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Concept_Types (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Concept_Types[")
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

	/** Set AMN_Concept_Types_UU.
		@param AMN_Concept_Types_UU AMN_Concept_Types_UU	  */
	public void setAMN_Concept_Types_UU (String AMN_Concept_Types_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_UU, AMN_Concept_Types_UU);
	}

	/** Get AMN_Concept_Types_UU.
		@return AMN_Concept_Types_UU	  */
	public String getAMN_Concept_Types_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Concept_Types_UU);
	}

	/** Set Payroll Concepts UOM.
		@param AMN_Concept_Uom_ID Payroll Concepts UOM	  */
	public void setAMN_Concept_Uom_ID (int AMN_Concept_Uom_ID)
	{
		if (AMN_Concept_Uom_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Uom_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Uom_ID, Integer.valueOf(AMN_Concept_Uom_ID));
	}

	/** Get Payroll Concepts UOM.
		@return Payroll Concepts UOM	  */
	public int getAMN_Concept_Uom_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Uom_ID);
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
		Concept Credit Administrative Work Force  Account
	  */
	public void setAMN_Cre_Acct (int AMN_Cre_Acct)
	{
		set_Value (COLUMNNAME_AMN_Cre_Acct, Integer.valueOf(AMN_Cre_Acct));
	}

	/** Get Concept Credit Administrative Work Force Account.
		@return Concept Credit Administrative Work Force  Account
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
		Concept Debit Administrative Work Force   Account
	  */
	public void setAMN_Deb_Acct (int AMN_Deb_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_Acct, Integer.valueOf(AMN_Deb_Acct));
	}

	/** Get Concept Debit Administrative Work Force  Account.
		@return Concept Debit Administrative Work Force   Account
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
		Concept Debit Direct Work Account
	  */
	public void setAMN_Deb_DW_Acct (int AMN_Deb_DW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_DW_Acct, Integer.valueOf(AMN_Deb_DW_Acct));
	}

	/** Get Concept Debit Direct Work Force Account.
		@return Concept Debit Direct Work Account
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
		Concept Debit Indirect Work Account
	  */
	public void setAMN_Deb_IW_Acct (int AMN_Deb_IW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_IW_Acct, Integer.valueOf(AMN_Deb_IW_Acct));
	}

	/** Get Concept Debit Indirect Work Force Account.
		@return Concept Debit Indirect Work Account
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
		Concept Debit Management Work Account
	  */
	public void setAMN_Deb_MW_Acct (int AMN_Deb_MW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_MW_Acct, Integer.valueOf(AMN_Deb_MW_Acct));
	}

	/** Get Concept Debit Management Work Force Account.
		@return Concept Debit Management Work Account
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
		Concept Debit Sales Work Account
	  */
	public void setAMN_Deb_SW_Acct (int AMN_Deb_SW_Acct)
	{
		set_Value (COLUMNNAME_AMN_Deb_SW_Acct, Integer.valueOf(AMN_Deb_SW_Acct));
	}

	/** Get Concept Debit Sales Work Force Account.
		@return Concept Debit Sales Work Account
	  */
	public int getAMN_Deb_SW_Acct () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Deb_SW_Acct);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
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
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set CalcOrder.
		@param CalcOrder CalcOrder	  */
	public void setCalcOrder (int CalcOrder)
	{
		set_Value (COLUMNNAME_CalcOrder, Integer.valueOf(CalcOrder));
	}

	/** Get CalcOrder.
		@return CalcOrder	  */
	public int getCalcOrder () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CalcOrder);
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

	/** Set Default Logic.
		@param DefaultValue 
		Default value hierarchy, separated by ;
	  */
	public void setDefaultValue (String DefaultValue)
	{
		set_Value (COLUMNNAME_DefaultValue, DefaultValue);
	}

	/** Get Default Logic.
		@return Default value hierarchy, separated by ;
	  */
	public String getDefaultValue () 
	{
		return (String)get_Value(COLUMNNAME_DefaultValue);
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
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

	/** Set Note.
		@param Note 
		Optional additional user defined information
	  */
	public void setNote (String Note)
	{
		set_Value (COLUMNNAME_Note, Note);
	}

	/** Get Note.
		@return Optional additional user defined information
	  */
	public String getNote () 
	{
		return (String)get_Value(COLUMNNAME_Note);
	}

	/** Set Script.
		@param Script 
		Dynamic Java Language Script to calculate result
	  */
	public void setScript (String Script)
	{
		set_Value (COLUMNNAME_Script, Script);
	}

	/** Get Script.
		@return Dynamic Java Language Script to calculate result
	  */
	public String getScript () 
	{
		return (String)get_Value(COLUMNNAME_Script);
	}

	/** Set Script Default Value.
		@param ScriptDefaultValue 
		Dynamic Java Language Script to calculate result for Default Value  (alternative to DefaultValue)
	  */
	public void setScriptDefaultValue (String ScriptDefaultValue)
	{
		set_Value (COLUMNNAME_ScriptDefaultValue, ScriptDefaultValue);
	}

	/** Get Script Default Value.
		@return Dynamic Java Language Script to calculate result for Default Value  (alternative to DefaultValue)
	  */
	public String getScriptDefaultValue () 
	{
		return (String)get_Value(COLUMNNAME_ScriptDefaultValue);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set arc.
		@param arc arc	  */
	public void setarc (boolean arc)
	{
		set_Value (COLUMNNAME_arc, Boolean.valueOf(arc));
	}

	/** Get arc.
		@return arc	  */
	public boolean isarc () 
	{
		Object oo = get_Value(COLUMNNAME_arc);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** LOTT Organic Labor Law = L */
	public static final String CONCEPTSOURCE_LOTTOrganicLaborLaw = "L";
	/** Collective Job Agreement = C */
	public static final String CONCEPTSOURCE_CollectiveJobAgreement = "C";
	/** Set Concept Source.
		@param conceptsource 
		Origen or Source for Concept Type
	  */
	public void setconceptsource (String conceptsource)
	{

		set_Value (COLUMNNAME_conceptsource, conceptsource);
	}

	/** Get Concept Source.
		@return Origen or Source for Concept Type
	  */
	public String getconceptsource () 
	{
		return (String)get_Value(COLUMNNAME_conceptsource);
	}

	/** Set daydiscriminated.
		@param daydiscriminated daydiscriminated	  */
	public void setdaydiscriminated (boolean daydiscriminated)
	{
		set_Value (COLUMNNAME_daydiscriminated, Boolean.valueOf(daydiscriminated));
	}

	/** Get daydiscriminated.
		@return daydiscriminated	  */
	public boolean isdaydiscriminated () 
	{
		Object oo = get_Value(COLUMNNAME_daydiscriminated);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set faov.
		@param faov faov	  */
	public void setfaov (boolean faov)
	{
		set_Value (COLUMNNAME_faov, Boolean.valueOf(faov));
	}

	/** Get faov.
		@return faov	  */
	public boolean isfaov () 
	{
		Object oo = get_Value(COLUMNNAME_faov);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Holiday.
		@param feriado 
		Indicates id Apllies for Holidays Calc
	  */
	public void setferiado (boolean feriado)
	{
		set_Value (COLUMNNAME_feriado, Boolean.valueOf(feriado));
	}

	/** Get Holiday.
		@return Indicates id Apllies for Holidays Calc
	  */
	public boolean isferiado () 
	{
		Object oo = get_Value(COLUMNNAME_feriado);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set formula.
		@param formula formula	  */
	public void setformula (String formula)
	{
		set_Value (COLUMNNAME_formula, formula);
	}

	/** Get formula.
		@return formula	  */
	public String getformula () 
	{
		return (String)get_Value(COLUMNNAME_formula);
	}

	/** Set ince.
		@param ince ince	  */
	public void setince (boolean ince)
	{
		set_Value (COLUMNNAME_ince, Boolean.valueOf(ince));
	}

	/** Get ince.
		@return ince	  */
	public boolean isince () 
	{
		Object oo = get_Value(COLUMNNAME_ince);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isDate.
		@param isDate isDate	  */
	public void setisDate (boolean isDate)
	{
		set_Value (COLUMNNAME_isDate, Boolean.valueOf(isDate));
	}

	/** Get isDate.
		@return isDate	  */
	public boolean isDate () 
	{
		Object oo = get_Value(COLUMNNAME_isDate);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isQty.
		@param isQty isQty	  */
	public void setisQty (boolean isQty)
	{
		set_Value (COLUMNNAME_isQty, Boolean.valueOf(isQty));
	}

	/** Get isQty.
		@return isQty	  */
	public boolean isQty () 
	{
		Object oo = get_Value(COLUMNNAME_isQty);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isValue.
		@param isValue isValue	  */
	public void setisValue (boolean isValue)
	{
		set_Value (COLUMNNAME_isValue, Boolean.valueOf(isValue));
	}

	/** Get isValue.
		@return isValue	  */
	public boolean isValue () 
	{
		Object oo = get_Value(COLUMNNAME_isValue);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Non Repeatable Concept = N */
	public static final String ISREPEAT_NonRepeatableConcept = "N";
	/** Repeatable Concept = Y */
	public static final String ISREPEAT_RepeatableConcept = "Y";
	/** Set isrepeat.
		@param isrepeat isrepeat	  */
	public void setisrepeat (String isrepeat)
	{

		set_Value (COLUMNNAME_isrepeat, isrepeat);
	}

	/** Get isrepeat.
		@return isrepeat	  */
	public String getisrepeat () 
	{
		return (String)get_Value(COLUMNNAME_isrepeat);
	}

	/** Yes = Y */
	public static final String ISSHOW_Yes = "Y";
	/** NO = N */
	public static final String ISSHOW_NO = "N";
	/** Set isshow.
		@param isshow isshow	  */
	public void setisshow (String isshow)
	{

		set_Value (COLUMNNAME_isshow, isshow);
	}

	/** Get isshow.
		@return isshow	  */
	public String getisshow () 
	{
		return (String)get_Value(COLUMNNAME_isshow);
	}

	/** Reference = R */
	public static final String OPTMODE_Reference = "R";
	/** WithHolding = W */
	public static final String OPTMODE_WithHolding = "W";
	/** Deduction = D */
	public static final String OPTMODE_Deduction = "D";
	/** Allocation = A */
	public static final String OPTMODE_Allocation = "A";
	/** Provision = P */
	public static final String OPTMODE_Provision = "P";
	/** Balance = B */
	public static final String OPTMODE_Balance = "B";
	/** Set optmode.
		@param optmode optmode	  */
	public void setoptmode (String optmode)
	{

		set_Value (COLUMNNAME_optmode, optmode);
	}

	/** Get optmode.
		@return optmode	  */
	public String getoptmode () 
	{
		return (String)get_Value(COLUMNNAME_optmode);
	}

	/** Set prestacion.
		@param prestacion prestacion	  */
	public void setprestacion (boolean prestacion)
	{
		set_Value (COLUMNNAME_prestacion, Boolean.valueOf(prestacion));
	}

	/** Get prestacion.
		@return prestacion	  */
	public boolean isprestacion () 
	{
		Object oo = get_Value(COLUMNNAME_prestacion);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Fixed = F */
	public static final String RULE_Fixed = "F";
	/** Variable = V */
	public static final String RULE_Variable = "V";
	/** Set rule.
		@param rule rule	  */
	public void setrule (String rule)
	{

		set_Value (COLUMNNAME_rule, rule);
	}

	/** Get rule.
		@return rule	  */
	public String getrule () 
	{
		return (String)get_Value(COLUMNNAME_rule);
	}

	/** Set salario.
		@param salario salario	  */
	public void setsalario (boolean salario)
	{
		set_Value (COLUMNNAME_salario, Boolean.valueOf(salario));
	}

	/** Get salario.
		@return salario	  */
	public boolean issalario () 
	{
		Object oo = get_Value(COLUMNNAME_salario);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Debit = D */
	public static final String SIGN_Debit = "D";
	/** Credit = C */
	public static final String SIGN_Credit = "C";
	/** Set sign.
		@param sign sign	  */
	public void setsign (String sign)
	{

		set_Value (COLUMNNAME_sign, sign);
	}

	/** Get sign.
		@return sign	  */
	public String getsign () 
	{
		return (String)get_Value(COLUMNNAME_sign);
	}

	/** Set spf.
		@param spf spf	  */
	public void setspf (boolean spf)
	{
		set_Value (COLUMNNAME_spf, Boolean.valueOf(spf));
	}

	/** Get spf.
		@return spf	  */
	public boolean isspf () 
	{
		Object oo = get_Value(COLUMNNAME_spf);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set sso.
		@param sso sso	  */
	public void setsso (boolean sso)
	{
		set_Value (COLUMNNAME_sso, Boolean.valueOf(sso));
	}

	/** Get sso.
		@return sso	  */
	public boolean issso () 
	{
		Object oo = get_Value(COLUMNNAME_sso);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set utilidad.
		@param utilidad utilidad	  */
	public void setutilidad (boolean utilidad)
	{
		set_Value (COLUMNNAME_utilidad, Boolean.valueOf(utilidad));
	}

	/** Get utilidad.
		@return utilidad	  */
	public boolean isutilidad () 
	{
		Object oo = get_Value(COLUMNNAME_utilidad);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set vacacion.
		@param vacacion vacacion	  */
	public void setvacacion (boolean vacacion)
	{
		set_Value (COLUMNNAME_vacacion, Boolean.valueOf(vacacion));
	}

	/** Get vacacion.
		@return vacacion	  */
	public boolean isvacacion () 
	{
		Object oo = get_Value(COLUMNNAME_vacacion);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set variable.
		@param variable variable	  */
	public void setvariable (String variable)
	{
		set_Value (COLUMNNAME_variable, variable);
	}

	/** Get variable.
		@return variable	  */
	public String getvariable () 
	{
		return (String)get_Value(COLUMNNAME_variable);
	}
}
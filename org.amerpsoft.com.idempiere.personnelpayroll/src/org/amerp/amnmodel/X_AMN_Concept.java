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

/** Generated Model for AMN_Concept
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Concept extends PO implements I_AMN_Concept, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Concept (Properties ctx, int AMN_Concept_ID, String trxName)
    {
      super (ctx, AMN_Concept_ID, trxName);
      /** if (AMN_Concept_ID == 0)
        {
			setAMN_Concept_ID (0);
			setAMN_Concept_Types_ID (0);
			setAMN_Process_ID (0);
			setAMN_Process_Value (null);
// @AMN_Process_ID@
			setName (null);
			setSeqNo (0);
			setformula (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Concept (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Concept[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Concepts.
		@param AMN_Concept_ID Payroll Concepts	  */
	public void setAMN_Concept_ID (int AMN_Concept_ID)
	{
		if (AMN_Concept_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_ID, Integer.valueOf(AMN_Concept_ID));
	}

	/** Get Payroll Concepts.
		@return Payroll Concepts	  */
	public int getAMN_Concept_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_ID);
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

	public I_AMN_Concept_Types_Proc getAMN_Concept_Types_Proc() throws RuntimeException
    {
		return (I_AMN_Concept_Types_Proc)MTable.get(getCtx(), I_AMN_Concept_Types_Proc.Table_Name)
			.getPO(getAMN_Concept_Types_Proc_ID(), get_TrxName());	}

	/** Set Payroll Concept Types Process.
		@param AMN_Concept_Types_Proc_ID Payroll Concept Types Process	  */
	public void setAMN_Concept_Types_Proc_ID (int AMN_Concept_Types_Proc_ID)
	{
		if (AMN_Concept_Types_Proc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Proc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Proc_ID, Integer.valueOf(AMN_Concept_Types_Proc_ID));
	}

	/** Get Payroll Concept Types Process.
		@return Payroll Concept Types Process	  */
	public int getAMN_Concept_Types_Proc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Concept_UU.
		@param AMN_Concept_UU AMN_Concept_UU	  */
	public void setAMN_Concept_UU (String AMN_Concept_UU)
	{
		set_Value (COLUMNNAME_AMN_Concept_UU, AMN_Concept_UU);
	}

	/** Get AMN_Concept_UU.
		@return AMN_Concept_UU	  */
	public String getAMN_Concept_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Concept_UU);
	}

	/** Set Payroll Process.
		@param AMN_Process_ID Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID)
	{
		if (AMN_Process_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Process_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Process_ID, Integer.valueOf(AMN_Process_ID));
	}

	/** Get Payroll Process.
		@return Payroll Process	  */
	public int getAMN_Process_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Process Value.
		@param AMN_Process_Value Process Value	  */
	public void setAMN_Process_Value (String AMN_Process_Value)
	{
		set_Value (COLUMNNAME_AMN_Process_Value, AMN_Process_Value);
	}

	/** Get Process Value.
		@return Process Value	  */
	public String getAMN_Process_Value () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Process_Value);
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

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}
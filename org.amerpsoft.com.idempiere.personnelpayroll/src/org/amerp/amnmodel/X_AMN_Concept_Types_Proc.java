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

/** Generated Model for AMN_Concept_Types_Proc
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Concept_Types_Proc extends PO implements I_AMN_Concept_Types_Proc, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170404L;

    /** Standard Constructor */
    public X_AMN_Concept_Types_Proc (Properties ctx, int AMN_Concept_Types_Proc_ID, String trxName)
    {
      super (ctx, AMN_Concept_Types_Proc_ID, trxName);
      /** if (AMN_Concept_Types_Proc_ID == 0)
        {
			setAMN_Concept_Types_ID (0);
			setAMN_Concept_Types_Proc_ID (0);
			setAMN_Process_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Concept_Types_Proc (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Concept_Types_Proc[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set AMN_Concept_Types_Proc_UU.
		@param AMN_Concept_Types_Proc_UU AMN_Concept_Types_Proc_UU	  */
	public void setAMN_Concept_Types_Proc_UU (String AMN_Concept_Types_Proc_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Proc_UU, AMN_Concept_Types_Proc_UU);
	}

	/** Get AMN_Concept_Types_Proc_UU.
		@return AMN_Concept_Types_Proc_UU	  */
	public String getAMN_Concept_Types_Proc_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Concept_Types_Proc_UU);
	}

	public I_AMN_Process getAMN_Process() throws RuntimeException
    {
		return (I_AMN_Process)MTable.get(getCtx(), I_AMN_Process.Table_Name)
			.getPO(getAMN_Process_ID(), get_TrxName());	}

	/** Set Payroll Process.
		@param AMN_Process_ID Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID)
	{
		if (AMN_Process_ID < 1) 
			set_Value (COLUMNNAME_AMN_Process_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Process_ID, Integer.valueOf(AMN_Process_ID));
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
}
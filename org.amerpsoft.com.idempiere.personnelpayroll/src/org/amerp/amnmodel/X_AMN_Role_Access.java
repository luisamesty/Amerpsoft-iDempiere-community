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

/** Generated Model for AMN_Role_Access
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Role_Access extends PO implements I_AMN_Role_Access, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Role_Access (Properties ctx, int AMN_Role_Access_ID, String trxName)
    {
      super (ctx, AMN_Role_Access_ID, trxName);
      /** if (AMN_Role_Access_ID == 0)
        {
			setAD_Role_ID (0);
			setAMN_Contract_ID (0);
			setAMN_Process_ID (0);
			setAMN_Process_Value (null);
			setAMN_Role_Access_ID (0);
			setAMN_Role_Access_UU (null);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Role_Access (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Role_Access[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_Name)
			.getPO(getAD_Role_ID(), get_TrxName());	}

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Contract.
		@param AMN_Contract_ID Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID)
	{
		if (AMN_Contract_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Contract_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Contract_ID, Integer.valueOf(AMN_Contract_ID));
	}

	/** Get Payroll Contract.
		@return Payroll Contract	  */
	public int getAMN_Contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Payroll Role Access .
		@param AMN_Role_Access_ID Payroll Role Access 	  */
	public void setAMN_Role_Access_ID (int AMN_Role_Access_ID)
	{
		if (AMN_Role_Access_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Role_Access_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Role_Access_ID, Integer.valueOf(AMN_Role_Access_ID));
	}

	/** Get Payroll Role Access .
		@return Payroll Role Access 	  */
	public int getAMN_Role_Access_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Role_Access_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Role_Access_UU.
		@param AMN_Role_Access_UU AMN_Role_Access_UU	  */
	public void setAMN_Role_Access_UU (String AMN_Role_Access_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Role_Access_UU, AMN_Role_Access_UU);
	}

	/** Get AMN_Role_Access_UU.
		@return AMN_Role_Access_UU	  */
	public String getAMN_Role_Access_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Role_Access_UU);
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
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

/** Generated Model for AMN_Sector
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Sector")
public class X_AMN_Sector extends PO implements I_AMN_Sector, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240823L;

    /** Standard Constructor */
    public X_AMN_Sector (Properties ctx, int AMN_Sector_ID, String trxName)
    {
      super (ctx, AMN_Sector_ID, trxName);
      /** if (AMN_Sector_ID == 0)
        {
			setAMN_Location_ID (0);
			setAMN_Sector_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Sector (Properties ctx, int AMN_Sector_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Sector_ID, trxName, virtualColumns);
      /** if (AMN_Sector_ID == 0)
        {
			setAMN_Location_ID (0);
			setAMN_Sector_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Sector (Properties ctx, String AMN_Sector_UU, String trxName)
    {
      super (ctx, AMN_Sector_UU, trxName);
      /** if (AMN_Sector_UU == null)
        {
			setAMN_Location_ID (0);
			setAMN_Sector_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Sector (Properties ctx, String AMN_Sector_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Sector_UU, trxName, virtualColumns);
      /** if (AMN_Sector_UU == null)
        {
			setAMN_Location_ID (0);
			setAMN_Sector_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Sector (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Sector[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Payroll Location.
		@param AMN_Location_ID Payroll Location
	*/
	public void setAMN_Location_ID (int AMN_Location_ID)
	{
		if (AMN_Location_ID < 1)
			set_Value (COLUMNNAME_AMN_Location_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Location_ID, Integer.valueOf(AMN_Location_ID));
	}

	/** Get Payroll Location.
		@return Payroll Location	  */
	public int getAMN_Location_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Work Sector .
		@param AMN_Sector_ID Work Sector in Location
	*/
	public void setAMN_Sector_ID (int AMN_Sector_ID)
	{
		if (AMN_Sector_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Sector_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Sector_ID, Integer.valueOf(AMN_Sector_ID));
	}

	/** Get Work Sector .
		@return Work Sector in Location
	  */
	public int getAMN_Sector_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Sector_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Sector_UU.
		@param AMN_Sector_UU AMN_Sector_UU
	*/
	public void setAMN_Sector_UU (String AMN_Sector_UU)
	{
		set_Value (COLUMNNAME_AMN_Sector_UU, AMN_Sector_UU);
	}

	/** Get AMN_Sector_UU.
		@return AMN_Sector_UU	  */
	public String getAMN_Sector_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Sector_UU);
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

	/** Set Comment/Help.
		@param Help Comment or Hint
	*/
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp()
	{
		return (String)get_Value(COLUMNNAME_Help);
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
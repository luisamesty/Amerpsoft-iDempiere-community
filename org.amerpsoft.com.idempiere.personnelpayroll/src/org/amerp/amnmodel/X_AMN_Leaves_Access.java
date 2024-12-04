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

/** Generated Model for AMN_Leaves_Access
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Leaves_Access")
public class X_AMN_Leaves_Access extends PO implements I_AMN_Leaves_Access, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241205L;

    /** Standard Constructor */
    public X_AMN_Leaves_Access (Properties ctx, int AMN_Leaves_Access_ID, String trxName)
    {
      super (ctx, AMN_Leaves_Access_ID, trxName);
      /** if (AMN_Leaves_Access_ID == 0)
        {
			setAMN_Leaves_Access_ID (0);
			setIsReadWrite (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Access (Properties ctx, int AMN_Leaves_Access_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_Access_ID, trxName, virtualColumns);
      /** if (AMN_Leaves_Access_ID == 0)
        {
			setAMN_Leaves_Access_ID (0);
			setIsReadWrite (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Access (Properties ctx, String AMN_Leaves_Access_UU, String trxName)
    {
      super (ctx, AMN_Leaves_Access_UU, trxName);
      /** if (AMN_Leaves_Access_UU == null)
        {
			setAMN_Leaves_Access_ID (0);
			setIsReadWrite (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Access (Properties ctx, String AMN_Leaves_Access_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_Access_UU, trxName, virtualColumns);
      /** if (AMN_Leaves_Access_UU == null)
        {
			setAMN_Leaves_Access_ID (0);
			setIsReadWrite (false);
// N
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Leaves_Access (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Leaves_Access[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_ID)
			.getPO(getAD_Role_ID(), get_TrxName());
	}

	/** Set Role.
		@param AD_Role_ID Responsibility Role
	*/
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0)
			set_Value (COLUMNNAME_AD_Role_ID, null);
		else
			set_Value (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Leaves and Licences Work Flow Nodes Access.
		@param AMN_Leaves_Access_ID Leaves and Licences Work Flow Nodes Access
	*/
	public void setAMN_Leaves_Access_ID (int AMN_Leaves_Access_ID)
	{
		if (AMN_Leaves_Access_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Access_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Access_ID, Integer.valueOf(AMN_Leaves_Access_ID));
	}

	/** Get Leaves and Licences Work Flow Nodes Access.
		@return Leaves and Licences Work Flow Nodes Access
	  */
	public int getAMN_Leaves_Access_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_Access_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Leaves_Access_UU.
		@param AMN_Leaves_Access_UU AMN_Leaves_Access_UU
	*/
	public void setAMN_Leaves_Access_UU (String AMN_Leaves_Access_UU)
	{
		set_Value (COLUMNNAME_AMN_Leaves_Access_UU, AMN_Leaves_Access_UU);
	}

	/** Get AMN_Leaves_Access_UU.
		@return AMN_Leaves_Access_UU	  */
	public String getAMN_Leaves_Access_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Leaves_Access_UU);
	}

	public I_AMN_Leaves_Flow getAMN_Leaves_Flow() throws RuntimeException
	{
		return (I_AMN_Leaves_Flow)MTable.get(getCtx(), I_AMN_Leaves_Flow.Table_ID)
			.getPO(getAMN_Leaves_Flow_ID(), get_TrxName());
	}

	/** Set Leaves and Licences Work Flow.
		@param AMN_Leaves_Flow_ID Leaves and Licences Work Flow
	*/
	public void setAMN_Leaves_Flow_ID (int AMN_Leaves_Flow_ID)
	{
		if (AMN_Leaves_Flow_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Flow_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Flow_ID, Integer.valueOf(AMN_Leaves_Flow_ID));
	}

	/** Get Leaves and Licences Work Flow.
		@return Leaves and Licences Work Flow
	  */
	public int getAMN_Leaves_Flow_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_Flow_ID);
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

	/** Set Read Write.
		@param IsReadWrite Field is read / write
	*/
	public void setIsReadWrite (boolean IsReadWrite)
	{
		set_Value (COLUMNNAME_IsReadWrite, Boolean.valueOf(IsReadWrite));
	}

	/** Get Read Write.
		@return Field is read / write
	  */
	public boolean isReadWrite()
	{
		Object oo = get_Value(COLUMNNAME_IsReadWrite);
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
}
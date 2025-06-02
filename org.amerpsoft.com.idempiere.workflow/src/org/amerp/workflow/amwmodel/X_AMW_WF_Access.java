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
package org.amerp.workflow.amwmodel;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for AMW_WF_Access
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMW_WF_Access")
public class X_AMW_WF_Access extends PO implements I_AMW_WF_Access, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241214L;

    /** Standard Constructor */
    public X_AMW_WF_Access (Properties ctx, int AMW_WF_Access_ID, String trxName)
    {
      super (ctx, AMW_WF_Access_ID, trxName);
      /** if (AMW_WF_Access_ID == 0)
        {
			setAMW_WF_Access_ID (0);
			setDescription (null);
			setIsReadWrite (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_Access (Properties ctx, int AMW_WF_Access_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WF_Access_ID, trxName, virtualColumns);
      /** if (AMW_WF_Access_ID == 0)
        {
			setAMW_WF_Access_ID (0);
			setDescription (null);
			setIsReadWrite (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_Access (Properties ctx, String AMW_WF_Access_UU, String trxName)
    {
      super (ctx, AMW_WF_Access_UU, trxName);
      /** if (AMW_WF_Access_UU == null)
        {
			setAMW_WF_Access_ID (0);
			setDescription (null);
			setIsReadWrite (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_Access (Properties ctx, String AMW_WF_Access_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WF_Access_UU, trxName, virtualColumns);
      /** if (AMW_WF_Access_UU == null)
        {
			setAMW_WF_Access_ID (0);
			setDescription (null);
			setIsReadWrite (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AMW_WF_Access (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMW_WF_Access[")
        .append(get_ID()).append("]");
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
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
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

	/** Set AMW Work Flow Access.
		@param AMW_WF_Access_ID AMW Work Flow Access
	*/
	public void setAMW_WF_Access_ID (int AMW_WF_Access_ID)
	{
		if (AMW_WF_Access_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMW_WF_Access_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMW_WF_Access_ID, Integer.valueOf(AMW_WF_Access_ID));
	}

	/** Get AMW Work Flow Access.
		@return AMW Work Flow Access
	  */
	public int getAMW_WF_Access_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WF_Access_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMW_WF_Access_UU.
		@param AMW_WF_Access_UU AMW_WF_Access_UU
	*/
	public void setAMW_WF_Access_UU (String AMW_WF_Access_UU)
	{
		set_Value (COLUMNNAME_AMW_WF_Access_UU, AMW_WF_Access_UU);
	}

	/** Get AMW_WF_Access_UU.
		@return AMW_WF_Access_UU	  */
	public String getAMW_WF_Access_UU()
	{
		return (String)get_Value(COLUMNNAME_AMW_WF_Access_UU);
	}

	public I_AMW_WF_Node getAMW_WF_Node() throws RuntimeException
	{
		return (I_AMW_WF_Node)MTable.get(getCtx(), I_AMW_WF_Node.Table_ID)
			.getPO(getAMW_WF_Node_ID(), get_TrxName());
	}

	/** Set AMW Work Flow Nodes.
		@param AMW_WF_Node_ID AMW Work Flow Nodes
	*/
	public void setAMW_WF_Node_ID (int AMW_WF_Node_ID)
	{
		if (AMW_WF_Node_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMW_WF_Node_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMW_WF_Node_ID, Integer.valueOf(AMW_WF_Node_ID));
	}

	/** Get AMW Work Flow Nodes.
		@return AMW Work Flow Nodes
	  */
	public int getAMW_WF_Node_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WF_Node_ID);
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
}
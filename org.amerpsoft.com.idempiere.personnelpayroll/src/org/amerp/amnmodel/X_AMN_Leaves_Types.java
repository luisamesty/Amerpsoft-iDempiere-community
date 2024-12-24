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

/** Generated Model for AMN_Leaves_Types
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Leaves_Types")
public class X_AMN_Leaves_Types extends PO implements I_AMN_Leaves_Types, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241220L;

    /** Standard Constructor */
    public X_AMN_Leaves_Types (Properties ctx, int AMN_Leaves_Types_ID, String trxName)
    {
      super (ctx, AMN_Leaves_Types_ID, trxName);
      /** if (AMN_Leaves_Types_ID == 0)
        {
			setAMN_Leaves_Types_ID (0);
			setMinimumDays (0);
// 0
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Types (Properties ctx, int AMN_Leaves_Types_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_Types_ID, trxName, virtualColumns);
      /** if (AMN_Leaves_Types_ID == 0)
        {
			setAMN_Leaves_Types_ID (0);
			setMinimumDays (0);
// 0
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Types (Properties ctx, String AMN_Leaves_Types_UU, String trxName)
    {
      super (ctx, AMN_Leaves_Types_UU, trxName);
      /** if (AMN_Leaves_Types_UU == null)
        {
			setAMN_Leaves_Types_ID (0);
			setMinimumDays (0);
// 0
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Types (Properties ctx, String AMN_Leaves_Types_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_Types_UU, trxName, virtualColumns);
      /** if (AMN_Leaves_Types_UU == null)
        {
			setAMN_Leaves_Types_ID (0);
			setMinimumDays (0);
// 0
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Leaves_Types (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Leaves_Types[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Payroll Concepts Types.
		@param AMN_Concept_Types_ID Payroll Concepts Types
	*/
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID)
	{
		if (AMN_Concept_Types_ID < 1)
			set_Value (COLUMNNAME_AMN_Concept_Types_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Concept_Types_ID, Integer.valueOf(AMN_Concept_Types_ID));
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

	/** Set Leaves Types.
		@param AMN_Leaves_Types_ID Leaves Types
	*/
	public void setAMN_Leaves_Types_ID (int AMN_Leaves_Types_ID)
	{
		if (AMN_Leaves_Types_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Types_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Types_ID, Integer.valueOf(AMN_Leaves_Types_ID));
	}

	/** Get Leaves Types.
		@return Leaves Types	  */
	public int getAMN_Leaves_Types_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_Types_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Leaves_Types_UU.
		@param AMN_Leaves_Types_UU AMN_Leaves_Types_UU
	*/
	public void setAMN_Leaves_Types_UU (String AMN_Leaves_Types_UU)
	{
		set_Value (COLUMNNAME_AMN_Leaves_Types_UU, AMN_Leaves_Types_UU);
	}

	/** Get AMN_Leaves_Types_UU.
		@return AMN_Leaves_Types_UU	  */
	public String getAMN_Leaves_Types_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Leaves_Types_UU);
	}

	/** Set Default Days.
		@param DefaultDays Default Days Quantity
	*/
	public void setDefaultDays (BigDecimal DefaultDays)
	{
		set_Value (COLUMNNAME_DefaultDays, DefaultDays);
	}

	/** Get Default Days.
		@return Default Days Quantity
	  */
	public BigDecimal getDefaultDays()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DefaultDays);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Lead Time.
		@param LeadTime Lead TimeLead time for a request
	*/
	public void setLeadTime (int LeadTime)
	{
		set_Value (COLUMNNAME_LeadTime, Integer.valueOf(LeadTime));
	}

	/** Get Lead Time.
		@return Lead TimeLead time for a request
	  */
	public int getLeadTime()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LeadTime);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Minimum Days.
		@param MinimumDays Minimum Days for requiring Leaves
	*/
	public void setMinimumDays (int MinimumDays)
	{
		set_Value (COLUMNNAME_MinimumDays, Integer.valueOf(MinimumDays));
	}

	/** Get Minimum Days.
		@return Minimum Days for requiring Leaves
	  */
	public int getMinimumDays()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MinimumDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Response Time.
		@param ResponseTime Request Response Time
	*/
	public void setResponseTime (int ResponseTime)
	{
		set_Value (COLUMNNAME_ResponseTime, Integer.valueOf(ResponseTime));
	}

	/** Get Response Time.
		@return Request Response Time
	  */
	public int getResponseTime()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ResponseTime);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
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

/** Generated Model for AMN_Jobstation
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Jobstation extends PO implements I_AMN_Jobstation, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Jobstation (Properties ctx, int AMN_Jobstation_ID, String trxName)
    {
      super (ctx, AMN_Jobstation_ID, trxName);
      /** if (AMN_Jobstation_ID == 0)
        {
			setAMN_Jobstation_ID (0);
			setAMN_Jobunit_ID (0);
			setName (null);
			setNumberofJobs (0);
// 1
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Jobstation (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Jobstation[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Job Station.
		@param AMN_Jobstation_ID Payroll Job Station	  */
	public void setAMN_Jobstation_ID (int AMN_Jobstation_ID)
	{
		if (AMN_Jobstation_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Jobstation_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Jobstation_ID, Integer.valueOf(AMN_Jobstation_ID));
	}

	/** Get Payroll Job Station.
		@return Payroll Job Station	  */
	public int getAMN_Jobstation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobstation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Jobstation_UU.
		@param AMN_Jobstation_UU AMN_Jobstation_UU	  */
	public void setAMN_Jobstation_UU (String AMN_Jobstation_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Jobstation_UU, AMN_Jobstation_UU);
	}

	/** Get AMN_Jobstation_UU.
		@return AMN_Jobstation_UU	  */
	public String getAMN_Jobstation_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Jobstation_UU);
	}

	public I_AMN_Jobunit getAMN_Jobunit() throws RuntimeException
    {
		return (I_AMN_Jobunit)MTable.get(getCtx(), I_AMN_Jobunit.Table_Name)
			.getPO(getAMN_Jobunit_ID(), get_TrxName());	}

	/** Set Payroll Job Unit.
		@param AMN_Jobunit_ID Payroll Job Unit	  */
	public void setAMN_Jobunit_ID (int AMN_Jobunit_ID)
	{
		if (AMN_Jobunit_ID < 1) 
			set_Value (COLUMNNAME_AMN_Jobunit_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Jobunit_ID, Integer.valueOf(AMN_Jobunit_ID));
	}

	/** Get Payroll Job Unit.
		@return Payroll Job Unit	  */
	public int getAMN_Jobunit_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobunit_ID);
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

	/** Set Number of Jobs.
		@param NumberofJobs Number of Jobs	  */
	public void setNumberofJobs (int NumberofJobs)
	{
		set_Value (COLUMNNAME_NumberofJobs, Integer.valueOf(NumberofJobs));
	}

	/** Get Number of Jobs.
		@return Number of Jobs	  */
	public int getNumberofJobs () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NumberofJobs);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_S_Resource getS_Resource() throws RuntimeException
    {
		return (org.compiere.model.I_S_Resource)MTable.get(getCtx(), org.compiere.model.I_S_Resource.Table_Name)
			.getPO(getS_Resource_ID(), get_TrxName());	}

	/** Set Resource.
		@param S_Resource_ID 
		Resource
	  */
	public void setS_Resource_ID (int S_Resource_ID)
	{
		if (S_Resource_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_S_Resource_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_S_Resource_ID, Integer.valueOf(S_Resource_ID));
	}

	/** Get Resource.
		@return Resource
	  */
	public int getS_Resource_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_S_Resource_ID);
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
}
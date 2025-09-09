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

/** Generated Model for AMN_Payroll_Assist_Unit
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Payroll_Assist_Unit")
public class X_AMN_Payroll_Assist_Unit extends PO implements I_AMN_Payroll_Assist_Unit, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250829L;

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Unit (Properties ctx, int AMN_Payroll_Assist_Unit_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Unit_ID, trxName);
      /** if (AMN_Payroll_Assist_Unit_ID == 0)
        {
			setAMN_Payroll_Assist_Unit_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Unit (Properties ctx, int AMN_Payroll_Assist_Unit_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Assist_Unit_ID, trxName, virtualColumns);
      /** if (AMN_Payroll_Assist_Unit_ID == 0)
        {
			setAMN_Payroll_Assist_Unit_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Unit (Properties ctx, String AMN_Payroll_Assist_Unit_UU, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Unit_UU, trxName);
      /** if (AMN_Payroll_Assist_Unit_UU == null)
        {
			setAMN_Payroll_Assist_Unit_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Unit (Properties ctx, String AMN_Payroll_Assist_Unit_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Assist_Unit_UU, trxName, virtualColumns);
      /** if (AMN_Payroll_Assist_Unit_UU == null)
        {
			setAMN_Payroll_Assist_Unit_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Assist_Unit (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Payroll_Assist_Unit[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set ComKey.
		@param AMN_ComKey ComKey
	*/
	public void setAMN_ComKey (String AMN_ComKey)
	{
		set_Value (COLUMNNAME_AMN_ComKey, AMN_ComKey);
	}

	/** Get ComKey.
		@return ComKey	  */
	public String getAMN_ComKey()
	{
		return (String)get_Value(COLUMNNAME_AMN_ComKey);
	}

	/** Set Host.
		@param AMN_Host Host
	*/
	public void setAMN_Host (String AMN_Host)
	{
		set_Value (COLUMNNAME_AMN_Host, AMN_Host);
	}

	/** Get Host.
		@return Host	  */
	public String getAMN_Host()
	{
		return (String)get_Value(COLUMNNAME_AMN_Host);
	}

	/** Set LoginPassword.
		@param AMN_LoginPassword LoginPassword
	*/
	public void setAMN_LoginPassword (String AMN_LoginPassword)
	{
		set_Value (COLUMNNAME_AMN_LoginPassword, AMN_LoginPassword);
	}

	/** Get LoginPassword.
		@return LoginPassword	  */
	public String getAMN_LoginPassword()
	{
		return (String)get_Value(COLUMNNAME_AMN_LoginPassword);
	}

	/** Set LoginUser.
		@param AMN_LoginUser LoginUser
	*/
	public void setAMN_LoginUser (String AMN_LoginUser)
	{
		set_Value (COLUMNNAME_AMN_LoginUser, AMN_LoginUser);
	}

	/** Get LoginUser.
		@return LoginUser	  */
	public String getAMN_LoginUser()
	{
		return (String)get_Value(COLUMNNAME_AMN_LoginUser);
	}

	/** Set Payroll Assist Unit.
		@param AMN_Payroll_Assist_Unit_ID Payroll Attendance Unit data
	*/
	public void setAMN_Payroll_Assist_Unit_ID (int AMN_Payroll_Assist_Unit_ID)
	{
		if (AMN_Payroll_Assist_Unit_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Unit_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Unit_ID, Integer.valueOf(AMN_Payroll_Assist_Unit_ID));
	}

	/** Get Payroll Assist Unit.
		@return Payroll Attendance Unit data
	  */
	public int getAMN_Payroll_Assist_Unit_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Assist_Unit_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_Unit_UU.
		@param AMN_Payroll_Assist_Unit_UU AMN_Payroll_Assist_Unit_UU
	*/
	public void setAMN_Payroll_Assist_Unit_UU (String AMN_Payroll_Assist_Unit_UU)
	{
		set_Value (COLUMNNAME_AMN_Payroll_Assist_Unit_UU, AMN_Payroll_Assist_Unit_UU);
	}

	/** Get AMN_Payroll_Assist_Unit_UU.
		@return AMN_Payroll_Assist_Unit_UU	  */
	public String getAMN_Payroll_Assist_Unit_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Assist_Unit_UU);
	}

	/** Set Port.
		@param AMN_Port Port
	*/
	public void setAMN_Port (int AMN_Port)
	{
		set_Value (COLUMNNAME_AMN_Port, Integer.valueOf(AMN_Port));
	}

	/** Get Port.
		@return Port	  */
	public int getAMN_Port()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Port);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Address.
		@param C_Location_ID Location or Address. Organization OrgInfo address.
	*/
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1)
			set_Value (COLUMNNAME_C_Location_ID, null);
		else
			set_Value (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
	}

	/** Get Address.
		@return Location or Address. Organization OrgInfo address.
	  */
	public int getC_Location_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_ID);
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

	/** Set WorkCode.
		@param WorkCode Work Code
	*/
	public void setWorkCode (String WorkCode)
	{
		set_Value (COLUMNNAME_WorkCode, WorkCode);
	}

	/** Get WorkCode.
		@return Work Code
	  */
	public String getWorkCode()
	{
		return (String)get_Value(COLUMNNAME_WorkCode);
	}

	/** Set Latitude.
		@param lat Latitude Degrees
	*/
	public void setlat (String lat)
	{
		set_Value (COLUMNNAME_lat, lat);
	}

	/** Get Latitude.
		@return Latitude Degrees
	  */
	public String getlat()
	{
		return (String)get_Value(COLUMNNAME_lat);
	}

	/** Set Longitude.
		@param lng Longitude in degrees
	*/
	public void setlng (String lng)
	{
		set_Value (COLUMNNAME_lng, lng);
	}

	/** Get Longitude.
		@return Longitude in degrees
	  */
	public String getlng()
	{
		return (String)get_Value(COLUMNNAME_lng);
	}
}
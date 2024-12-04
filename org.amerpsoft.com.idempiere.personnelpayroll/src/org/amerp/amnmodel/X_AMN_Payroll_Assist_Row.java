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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for AMN_Payroll_Assist_Row
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Payroll_Assist_Row")
public class X_AMN_Payroll_Assist_Row extends PO implements I_AMN_Payroll_Assist_Row, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241205L;

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Row (Properties ctx, int AMN_Payroll_Assist_Row_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Row_ID, trxName);
      /** if (AMN_Payroll_Assist_Row_ID == 0)
        {
			setAMN_Payroll_Assist_Row_ID (0);
			setIsVerified (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Row (Properties ctx, int AMN_Payroll_Assist_Row_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Assist_Row_ID, trxName, virtualColumns);
      /** if (AMN_Payroll_Assist_Row_ID == 0)
        {
			setAMN_Payroll_Assist_Row_ID (0);
			setIsVerified (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Row (Properties ctx, String AMN_Payroll_Assist_Row_UU, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Row_UU, trxName);
      /** if (AMN_Payroll_Assist_Row_UU == null)
        {
			setAMN_Payroll_Assist_Row_ID (0);
			setIsVerified (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Row (Properties ctx, String AMN_Payroll_Assist_Row_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Assist_Row_UU, trxName, virtualColumns);
      /** if (AMN_Payroll_Assist_Row_UU == null)
        {
			setAMN_Payroll_Assist_Row_ID (0);
			setIsVerified (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Assist_Row (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Payroll_Assist_Row[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Assist Row.
		@param AMN_Payroll_Assist_Row_ID Payroll Attendance Row data
	*/
	public void setAMN_Payroll_Assist_Row_ID (int AMN_Payroll_Assist_Row_ID)
	{
		if (AMN_Payroll_Assist_Row_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Row_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Row_ID, Integer.valueOf(AMN_Payroll_Assist_Row_ID));
	}

	/** Get Payroll Assist Row.
		@return Payroll Attendance Row data
	  */
	public int getAMN_Payroll_Assist_Row_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Assist_Row_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_Row_UU.
		@param AMN_Payroll_Assist_Row_UU AMN_Payroll_Assist_Row_UU
	*/
	public void setAMN_Payroll_Assist_Row_UU (String AMN_Payroll_Assist_Row_UU)
	{
		set_Value (COLUMNNAME_AMN_Payroll_Assist_Row_UU, AMN_Payroll_Assist_Row_UU);
	}

	/** Get AMN_Payroll_Assist_Row_UU.
		@return AMN_Payroll_Assist_Row_UU	  */
	public String getAMN_Payroll_Assist_Row_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Assist_Row_UU);
	}

	/** Set Payroll Assist Unit.
		@param AMN_Payroll_Assist_Unit_ID Payroll Attendance Unit data
	*/
	public void setAMN_Payroll_Assist_Unit_ID (String AMN_Payroll_Assist_Unit_ID)
	{

		set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Unit_ID, AMN_Payroll_Assist_Unit_ID);
	}

	/** Get Payroll Assist Unit.
		@return Payroll Attendance Unit data
	  */
	public String getAMN_Payroll_Assist_Unit_ID()
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Assist_Unit_ID);
	}

	/** Set Transaction Date.
		@param DateTrx Transaction Date
	*/
	public void setDateTrx (Timestamp DateTrx)
	{
		set_ValueNoCheck (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Verified.
		@param IsVerified The BOM configuration has been verified
	*/
	public void setIsVerified (boolean IsVerified)
	{
		set_Value (COLUMNNAME_IsVerified, Boolean.valueOf(IsVerified));
	}

	/** Get Verified.
		@return The BOM configuration has been verified
	  */
	public boolean isVerified()
	{
		Object oo = get_Value(COLUMNNAME_IsVerified);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PIN.
		@param PIN Personal Identification Number
	*/
	public void setPIN (String PIN)
	{
		set_Value (COLUMNNAME_PIN, PIN);
	}

	/** Get PIN.
		@return Personal Identification Number
	  */
	public String getPIN()
	{
		return (String)get_Value(COLUMNNAME_PIN);
	}

	/** Set Status.
		@param Status Status of the currently running check
	*/
	public void setStatus (int Status)
	{
		set_ValueNoCheck (COLUMNNAME_Status, Integer.valueOf(Status));
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public int getStatus()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Status);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
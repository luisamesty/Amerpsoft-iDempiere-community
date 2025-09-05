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

/** Generated Model for AMN_Employee_Shift
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Employee_Shift")
public class X_AMN_Employee_Shift extends PO implements I_AMN_Employee_Shift, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250902L;

    /** Standard Constructor */
    public X_AMN_Employee_Shift (Properties ctx, int AMN_Employee_Shift_ID, String trxName)
    {
      super (ctx, AMN_Employee_Shift_ID, trxName);
      /** if (AMN_Employee_Shift_ID == 0)
        {
			setAMN_Employee_Shift_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Employee_Shift (Properties ctx, int AMN_Employee_Shift_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Employee_Shift_ID, trxName, virtualColumns);
      /** if (AMN_Employee_Shift_ID == 0)
        {
			setAMN_Employee_Shift_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Employee_Shift (Properties ctx, String AMN_Employee_Shift_UU, String trxName)
    {
      super (ctx, AMN_Employee_Shift_UU, trxName);
      /** if (AMN_Employee_Shift_UU == null)
        {
			setAMN_Employee_Shift_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Employee_Shift (Properties ctx, String AMN_Employee_Shift_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Employee_Shift_UU, trxName, virtualColumns);
      /** if (AMN_Employee_Shift_UU == null)
        {
			setAMN_Employee_Shift_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AMN_Employee_Shift (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Employee_Shift[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AMN_Employee getAMN_Employee() throws RuntimeException
	{
		return (I_AMN_Employee)MTable.get(getCtx(), I_AMN_Employee.Table_ID)
			.getPO(getAMN_Employee_ID(), get_TrxName());
	}

	/** Set Payroll employee.
		@param AMN_Employee_ID Payroll employee
	*/
	public void setAMN_Employee_ID (int AMN_Employee_ID)
	{
		if (AMN_Employee_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
	}

	/** Get Payroll employee.
		@return Payroll employee	  */
	public int getAMN_Employee_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Employee Shift list ID.
		@param AMN_Employee_Shift_ID Employee Shift list ID
	*/
	public void setAMN_Employee_Shift_ID (int AMN_Employee_Shift_ID)
	{
		if (AMN_Employee_Shift_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_Shift_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_Shift_ID, Integer.valueOf(AMN_Employee_Shift_ID));
	}

	/** Get Employee Shift list ID.
		@return Employee Shift list ID
	  */
	public int getAMN_Employee_Shift_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_Shift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Employee_Shift_UU.
		@param AMN_Employee_Shift_UU AMN_Employee_Shift_UU
	*/
	public void setAMN_Employee_Shift_UU (String AMN_Employee_Shift_UU)
	{
		set_Value (COLUMNNAME_AMN_Employee_Shift_UU, AMN_Employee_Shift_UU);
	}

	/** Get AMN_Employee_Shift_UU.
		@return AMN_Employee_Shift_UU	  */
	public String getAMN_Employee_Shift_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Employee_Shift_UU);
	}

	/** Set Shift.
		@param AMN_Shift_ID Shift
	*/
	public void setAMN_Shift_ID (int AMN_Shift_ID)
	{
		if (AMN_Shift_ID < 1)
			set_Value (COLUMNNAME_AMN_Shift_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Shift_ID, Integer.valueOf(AMN_Shift_ID));
	}

	/** Get Shift.
		@return Shift	  */
	public int getAMN_Shift_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Shift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
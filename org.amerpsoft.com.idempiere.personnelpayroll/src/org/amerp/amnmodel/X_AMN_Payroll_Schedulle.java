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

/** Generated Model for AMN_Payroll_Schedulle
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Payroll_Schedulle")
public class X_AMN_Payroll_Schedulle extends PO implements I_AMN_Payroll_Schedulle, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240918L;

    /** Standard Constructor */
    public X_AMN_Payroll_Schedulle (Properties ctx, int AMN_Payroll_Schedulle_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Schedulle_ID, trxName);
      /** if (AMN_Payroll_Schedulle_ID == 0)
        {
			setAMN_Payroll_Schedulle_ID (0);
			setIsNonBusinessDay (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Schedulle (Properties ctx, int AMN_Payroll_Schedulle_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Schedulle_ID, trxName, virtualColumns);
      /** if (AMN_Payroll_Schedulle_ID == 0)
        {
			setAMN_Payroll_Schedulle_ID (0);
			setIsNonBusinessDay (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Schedulle (Properties ctx, String AMN_Payroll_Schedulle_UU, String trxName)
    {
      super (ctx, AMN_Payroll_Schedulle_UU, trxName);
      /** if (AMN_Payroll_Schedulle_UU == null)
        {
			setAMN_Payroll_Schedulle_ID (0);
			setIsNonBusinessDay (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Schedulle (Properties ctx, String AMN_Payroll_Schedulle_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Schedulle_UU, trxName, virtualColumns);
      /** if (AMN_Payroll_Schedulle_UU == null)
        {
			setAMN_Payroll_Schedulle_ID (0);
			setIsNonBusinessDay (false);
// N
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Schedulle (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Payroll_Schedulle[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Assist Date.
		@param AMNDateAssist Assist Date
	*/
	public void setAMNDateAssist (Timestamp AMNDateAssist)
	{
		set_Value (COLUMNNAME_AMNDateAssist, AMNDateAssist);
	}

	/** Get Assist Date.
		@return Assist Date	  */
	public Timestamp getAMNDateAssist()
	{
		return (Timestamp)get_Value(COLUMNNAME_AMNDateAssist);
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

	/** Set Payroll Assist Schedulle.
		@param AMN_Payroll_Schedulle_ID Payroll Assist Schedulle
	*/
	public void setAMN_Payroll_Schedulle_ID (int AMN_Payroll_Schedulle_ID)
	{
		if (AMN_Payroll_Schedulle_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Schedulle_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Schedulle_ID, Integer.valueOf(AMN_Payroll_Schedulle_ID));
	}

	/** Get Payroll Assist Schedulle.
		@return Payroll Assist Schedulle	  */
	public int getAMN_Payroll_Schedulle_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Schedulle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Schedulle_UU.
		@param AMN_Payroll_Schedulle_UU AMN_Payroll_Schedulle_UU
	*/
	public void setAMN_Payroll_Schedulle_UU (String AMN_Payroll_Schedulle_UU)
	{
		set_Value (COLUMNNAME_AMN_Payroll_Schedulle_UU, AMN_Payroll_Schedulle_UU);
	}

	/** Get AMN_Payroll_Schedulle_UU.
		@return AMN_Payroll_Schedulle_UU	  */
	public String getAMN_Payroll_Schedulle_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Schedulle_UU);
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

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
	{
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_ID)
			.getPO(getC_Period_ID(), get_TrxName());
	}

	/** Set Period.
		@param C_Period_ID Period of the Calendar
	*/
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Year getC_Year() throws RuntimeException
	{
		return (org.compiere.model.I_C_Year)MTable.get(getCtx(), org.compiere.model.I_C_Year.Table_ID)
			.getPO(getC_Year_ID(), get_TrxName());
	}

	/** Set Year.
		@param C_Year_ID Calendar Year
	*/
	public void setC_Year_ID (int C_Year_ID)
	{
		if (C_Year_ID < 1)
			set_Value (COLUMNNAME_C_Year_ID, null);
		else
			set_Value (COLUMNNAME_C_Year_ID, Integer.valueOf(C_Year_ID));
	}

	/** Get Year.
		@return Calendar Year
	  */
	public int getC_Year_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Year_ID);
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

	/** Set Non Business Day.
		@param IsNonBusinessDay Non Business Day
	*/
	public void setIsNonBusinessDay (boolean IsNonBusinessDay)
	{
		set_Value (COLUMNNAME_IsNonBusinessDay, Boolean.valueOf(IsNonBusinessDay));
	}

	/** Get Non Business Day.
		@return Non Business Day	  */
	public boolean isNonBusinessDay()
	{
		Object oo = get_Value(COLUMNNAME_IsNonBusinessDay);
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
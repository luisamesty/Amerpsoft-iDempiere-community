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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for AMN_Rates
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Rates extends PO implements I_AMN_Rates, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Rates (Properties ctx, int AMN_Rates_ID, String trxName)
    {
      super (ctx, AMN_Rates_ID, trxName);
      /** if (AMN_Rates_ID == 0)
        {
			setAMN_Rates_ID (0);
			setC_Currency_ID (0);
			setEndDate (new Timestamp( System.currentTimeMillis() ));
			setName (null);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
			setmonth (0);
			setyear (0);
        } */
    }

    /** Load Constructor */
    public X_AMN_Rates (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
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
      StringBuffer sb = new StringBuffer ("X_AMN_Rates[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Rates.
		@param AMN_Rates_ID Payroll Rates	  */
	public void setAMN_Rates_ID (int AMN_Rates_ID)
	{
		if (AMN_Rates_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Rates_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Rates_ID, Integer.valueOf(AMN_Rates_ID));
	}

	/** Get Payroll Rates.
		@return Payroll Rates	  */
	public int getAMN_Rates_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Rates_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Rates_UU.
		@param AMN_Rates_UU AMN_Rates_UU	  */
	public void setAMN_Rates_UU (String AMN_Rates_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Rates_UU, AMN_Rates_UU);
	}

	/** Get AMN_Rates_UU.
		@return AMN_Rates_UU	  */
	public String getAMN_Rates_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Rates_UU);
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
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

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
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

	/** Set Salary Day.
		@param Salary_Day Salary Day	  */
	public void setSalary_Day (BigDecimal Salary_Day)
	{
		set_Value (COLUMNNAME_Salary_Day, Salary_Day);
	}

	/** Get Salary Day.
		@return Salary Day	  */
	public BigDecimal getSalary_Day () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Day);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
	}

	/** Set TaxUnit.
		@param TaxUnit TaxUnit	  */
	public void setTaxUnit (BigDecimal TaxUnit)
	{
		set_Value (COLUMNNAME_TaxUnit, TaxUnit);
	}

	/** Get TaxUnit.
		@return TaxUnit	  */
	public BigDecimal getTaxUnit () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxUnit);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set active_int_rate.
		@param active_int_rate active_int_rate	  */
	public void setactive_int_rate (BigDecimal active_int_rate)
	{
		set_Value (COLUMNNAME_active_int_rate, active_int_rate);
	}

	/** Get active_int_rate.
		@return active_int_rate	  */
	public BigDecimal getactive_int_rate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_active_int_rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set active_int_rate2.
		@param active_int_rate2 active_int_rate2	  */
	public void setactive_int_rate2 (BigDecimal active_int_rate2)
	{
		set_Value (COLUMNNAME_active_int_rate2, active_int_rate2);
	}

	/** Get active_int_rate2.
		@return active_int_rate2	  */
	public BigDecimal getactive_int_rate2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_active_int_rate2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set inflation.
		@param inflation inflation	  */
	public void setinflation (BigDecimal inflation)
	{
		set_Value (COLUMNNAME_inflation, inflation);
	}

	/** Get inflation.
		@return inflation	  */
	public BigDecimal getinflation () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_inflation);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ipc.
		@param ipc ipc	  */
	public void setipc (BigDecimal ipc)
	{
		set_Value (COLUMNNAME_ipc, ipc);
	}

	/** Get ipc.
		@return ipc	  */
	public BigDecimal getipc () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ipc);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set month.
		@param month month	  */
	public void setmonth (int month)
	{
		set_Value (COLUMNNAME_month, Integer.valueOf(month));
	}

	/** Get month.
		@return month	  */
	public int getmonth () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_month);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set salary_day_20.
		@param salary_day_20 salary_day_20	  */
	public void setsalary_day_20 (BigDecimal salary_day_20)
	{
		set_Value (COLUMNNAME_salary_day_20, salary_day_20);
	}

	/** Get salary_day_20.
		@return salary_day_20	  */
	public BigDecimal getsalary_day_20 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_salary_day_20);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary Rural.
		@param salary_day_ru Salary Rural	  */
	public void setsalary_day_ru (BigDecimal salary_day_ru)
	{
		set_Value (COLUMNNAME_salary_day_ru, salary_day_ru);
	}

	/** Get Salary Rural.
		@return Salary Rural	  */
	public BigDecimal getsalary_day_ru () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_salary_day_ru);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set salary_mo.
		@param salary_mo salary_mo	  */
	public void setsalary_mo (BigDecimal salary_mo)
	{
		set_Value (COLUMNNAME_salary_mo, salary_mo);
	}

	/** Get salary_mo.
		@return salary_mo	  */
	public BigDecimal getsalary_mo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_salary_mo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set salary_mo_20.
		@param salary_mo_20 
		Salary Mo < 20T
	  */
	public void setsalary_mo_20 (BigDecimal salary_mo_20)
	{
		set_Value (COLUMNNAME_salary_mo_20, salary_mo_20);
	}

	/** Get salary_mo_20.
		@return Salary Mo < 20T
	  */
	public BigDecimal getsalary_mo_20 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_salary_mo_20);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set salary_mo_ru.
		@param salary_mo_ru salary_mo_ru	  */
	public void setsalary_mo_ru (BigDecimal salary_mo_ru)
	{
		set_Value (COLUMNNAME_salary_mo_ru, salary_mo_ru);
	}

	/** Get salary_mo_ru.
		@return salary_mo_ru	  */
	public BigDecimal getsalary_mo_ru () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_salary_mo_ru);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set year.
		@param year year	  */
	public void setyear (int year)
	{
		set_Value (COLUMNNAME_year, Integer.valueOf(year));
	}

	/** Get year.
		@return year	  */
	public int getyear () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_year);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
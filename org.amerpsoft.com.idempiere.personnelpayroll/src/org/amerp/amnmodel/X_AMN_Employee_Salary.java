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

/** Generated Model for AMN_Employee_Salary
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Employee_Salary extends PO implements I_AMN_Employee_Salary, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160427L;

    /** Standard Constructor */
    public X_AMN_Employee_Salary (Properties ctx, int AMN_Employee_Salary_ID, String trxName)
    {
      super (ctx, AMN_Employee_Salary_ID, trxName);
      /** if (AMN_Employee_Salary_ID == 0)
        {
			setAMN_Employee_ID (0);
// @AMN_Employee_ID@
			setAMN_Employee_Salary_ID (0);
			setAMN_Employee_Salary_UU (null);
			setC_Currency_ID (0);
			setDescription (null);
			setIsLess20 (false);
// N
			setIsRural (false);
// N
			setPrestacionDays (0);
// 0
			setUtilityDays (0);
// 0
			setVacationDays (0);
// 0
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_AMN_Employee_Salary (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Employee_Salary[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AMN_Concept_Types_Proc getAMN_Concept_Types_Proc() throws RuntimeException
    {
		return (I_AMN_Concept_Types_Proc)MTable.get(getCtx(), I_AMN_Concept_Types_Proc.Table_Name)
			.getPO(getAMN_Concept_Types_Proc_ID(), get_TrxName());	}

	/** Set Payroll Concept Types Process.
		@param AMN_Concept_Types_Proc_ID Payroll Concept Types Process	  */
	public void setAMN_Concept_Types_Proc_ID (int AMN_Concept_Types_Proc_ID)
	{
		if (AMN_Concept_Types_Proc_ID < 1) 
			set_Value (COLUMNNAME_AMN_Concept_Types_Proc_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Concept_Types_Proc_ID, Integer.valueOf(AMN_Concept_Types_Proc_ID));
	}

	/** Get Payroll Concept Types Process.
		@return Payroll Concept Types Process	  */
	public int getAMN_Concept_Types_Proc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_Employee getAMN_Employee() throws RuntimeException
    {
		return (I_AMN_Employee)MTable.get(getCtx(), I_AMN_Employee.Table_Name)
			.getPO(getAMN_Employee_ID(), get_TrxName());	}

	/** Set Payroll employee.
		@param AMN_Employee_ID Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID)
	{
		if (AMN_Employee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
	}

	/** Get Payroll employee.
		@return Payroll employee	  */
	public int getAMN_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Employee Salary.
		@param AMN_Employee_Salary_ID Employee Salary	  */
	public void setAMN_Employee_Salary_ID (int AMN_Employee_Salary_ID)
	{
		if (AMN_Employee_Salary_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_Salary_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_Salary_ID, Integer.valueOf(AMN_Employee_Salary_ID));
	}

	/** Get Employee Salary.
		@return Employee Salary	  */
	public int getAMN_Employee_Salary_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_Salary_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Employee_Salary_UU.
		@param AMN_Employee_Salary_UU AMN_Employee_Salary_UU	  */
	public void setAMN_Employee_Salary_UU (String AMN_Employee_Salary_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Employee_Salary_UU, AMN_Employee_Salary_UU);
	}

	/** Get AMN_Employee_Salary_UU.
		@return AMN_Employee_Salary_UU	  */
	public String getAMN_Employee_Salary_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Employee_Salary_UU);
	}

	public I_AMN_Payroll getAMN_Payroll() throws RuntimeException
    {
		return (I_AMN_Payroll)MTable.get(getCtx(), I_AMN_Payroll.Table_Name)
			.getPO(getAMN_Payroll_ID(), get_TrxName());	}

	/** Set Payroll Invoices.
		@param AMN_Payroll_ID Payroll Invoices	  */
	public void setAMN_Payroll_ID (int AMN_Payroll_ID)
	{
		if (AMN_Payroll_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_ID, Integer.valueOf(AMN_Payroll_ID));
	}

	/** Get Payroll Invoices.
		@return Payroll Invoices	  */
	public int getAMN_Payroll_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Is Less 20.
		@param IsLess20 Is Less 20	  */
	public void setIsLess20 (boolean IsLess20)
	{
		set_Value (COLUMNNAME_IsLess20, Boolean.valueOf(IsLess20));
	}

	/** Get Is Less 20.
		@return Is Less 20	  */
	public boolean isLess20 () 
	{
		Object oo = get_Value(COLUMNNAME_IsLess20);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Rural.
		@param IsRural Is Rural	  */
	public void setIsRural (boolean IsRural)
	{
		set_Value (COLUMNNAME_IsRural, Boolean.valueOf(IsRural));
	}

	/** Get Is Rural.
		@return Is Rural	  */
	public boolean isRural () 
	{
		Object oo = get_Value(COLUMNNAME_IsRural);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Prestacion Accumulated.
		@param PrestacionAccumulated Prestacion Accumulated	  */
	public void setPrestacionAccumulated (BigDecimal PrestacionAccumulated)
	{
		set_Value (COLUMNNAME_PrestacionAccumulated, PrestacionAccumulated);
	}

	/** Get Prestacion Accumulated.
		@return Prestacion Accumulated	  */
	public BigDecimal getPrestacionAccumulated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionAccumulated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prestacion Adjustment.
		@param PrestacionAdjustment Prestacion Adjustment	  */
	public void setPrestacionAdjustment (BigDecimal PrestacionAdjustment)
	{
		set_Value (COLUMNNAME_PrestacionAdjustment, PrestacionAdjustment);
	}

	/** Get Prestacion Adjustment.
		@return Prestacion Adjustment	  */
	public BigDecimal getPrestacionAdjustment () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionAdjustment);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prestacion Advance.
		@param PrestacionAdvance Prestacion Advance	  */
	public void setPrestacionAdvance (BigDecimal PrestacionAdvance)
	{
		set_Value (COLUMNNAME_PrestacionAdvance, PrestacionAdvance);
	}

	/** Get Prestacion Advance.
		@return Prestacion Advance	  */
	public BigDecimal getPrestacionAdvance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionAdvance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prestacion Amount.
		@param PrestacionAmount Prestacion Amount	  */
	public void setPrestacionAmount (BigDecimal PrestacionAmount)
	{
		set_Value (COLUMNNAME_PrestacionAmount, PrestacionAmount);
	}

	/** Get Prestacion Amount.
		@return Prestacion Amount	  */
	public BigDecimal getPrestacionAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prestacion Days.
		@param PrestacionDays Prestacion Days	  */
	public void setPrestacionDays (int PrestacionDays)
	{
		set_Value (COLUMNNAME_PrestacionDays, Integer.valueOf(PrestacionDays));
	}

	/** Get Prestacion Days.
		@return Prestacion Days	  */
	public int getPrestacionDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PrestacionDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Prestacion Interest.
		@param PrestacionInterest Prestacion Interest	  */
	public void setPrestacionInterest (BigDecimal PrestacionInterest)
	{
		set_Value (COLUMNNAME_PrestacionInterest, PrestacionInterest);
	}

	/** Get Prestacion Interest.
		@return Prestacion Interest	  */
	public BigDecimal getPrestacionInterest () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionInterest);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prestacion Interest Adjustment.
		@param PrestacionInterestAdjustment Prestacion Interest Adjustment	  */
	public void setPrestacionInterestAdjustment (BigDecimal PrestacionInterestAdjustment)
	{
		set_Value (COLUMNNAME_PrestacionInterestAdjustment, PrestacionInterestAdjustment);
	}

	/** Get Prestacion Interest Adjustment.
		@return Prestacion Interest Adjustment	  */
	public BigDecimal getPrestacionInterestAdjustment () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionInterestAdjustment);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Prestacion Interest Advance.
		@param PrestacionInterestAdvance Prestacion Interest Advance	  */
	public void setPrestacionInterestAdvance (BigDecimal PrestacionInterestAdvance)
	{
		set_Value (COLUMNNAME_PrestacionInterestAdvance, PrestacionInterestAdvance);
	}

	/** Get Prestacion Interest Advance.
		@return Prestacion Interest Advance	  */
	public BigDecimal getPrestacionInterestAdvance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PrestacionInterestAdvance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary.
		@param Salary Salary	  */
	public void setSalary (BigDecimal Salary)
	{
		set_Value (COLUMNNAME_Salary, Salary);
	}

	/** Get Salary.
		@return Salary	  */
	public BigDecimal getSalary () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Tax Rate.
		@param TaxRate Tax Rate	  */
	public void setTaxRate (BigDecimal TaxRate)
	{
		set_Value (COLUMNNAME_TaxRate, TaxRate);
	}

	/** Get Tax Rate.
		@return Tax Rate	  */
	public BigDecimal getTaxRate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Utility Days.
		@param UtilityDays Utility Days	  */
	public void setUtilityDays (int UtilityDays)
	{
		set_Value (COLUMNNAME_UtilityDays, Integer.valueOf(UtilityDays));
	}

	/** Get Utility Days.
		@return Utility Days	  */
	public int getUtilityDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_UtilityDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Vacation Days.
		@param VacationDays Vacation Days	  */
	public void setVacationDays (int VacationDays)
	{
		set_Value (COLUMNNAME_VacationDays, Integer.valueOf(VacationDays));
	}

	/** Get Vacation Days.
		@return Vacation Days	  */
	public int getVacationDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_VacationDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo 
		Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
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
}
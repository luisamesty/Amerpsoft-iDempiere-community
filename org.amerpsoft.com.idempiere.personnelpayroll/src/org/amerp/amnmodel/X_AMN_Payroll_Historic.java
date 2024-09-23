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

/** Generated Model for AMN_Payroll_Historic
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Payroll_Historic extends PO implements I_AMN_Payroll_Historic, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180717L;

    /** Standard Constructor */
    public X_AMN_Payroll_Historic (Properties ctx, int AMN_Payroll_Historic_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Historic_ID, trxName);
      /** if (AMN_Payroll_Historic_ID == 0)
        {
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Historic (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Payroll_Historic[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Payroll Historic Accumulated .
		@param AMN_Payroll_Historic_ID Payroll Historic Accumulated 	  */
	public void setAMN_Payroll_Historic_ID (int AMN_Payroll_Historic_ID)
	{
		if (AMN_Payroll_Historic_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Historic_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Historic_ID, Integer.valueOf(AMN_Payroll_Historic_ID));
	}

	/** Get Payroll Historic Accumulated .
		@return Payroll Historic Accumulated 	  */
	public int getAMN_Payroll_Historic_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Historic_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Period YYYY-MM.
		@param AMN_Period_YYYYMM 
		Payroll Period in YYYY and MM
	  */
	public void setAMN_Period_YYYYMM (String AMN_Period_YYYYMM)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Period_YYYYMM, AMN_Period_YYYYMM);
	}

	/** Get Payroll Period YYYY-MM.
		@return Payroll Period in YYYY and MM
	  */
	public String getAMN_Period_YYYYMM () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Period_YYYYMM);
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_ValueNoCheck (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
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

	/** Set Salary_Base.
		@param Salary_Base Salary_Base	  */
	public void setSalary_Base (BigDecimal Salary_Base)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Base, Salary_Base);
	}

	/** Get Salary_Base.
		@return Salary_Base	  */
	public BigDecimal getSalary_Base () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Base);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Integral.
		@param Salary_Integral 
		Integral Salary
	  */
	public void setSalary_Integral (BigDecimal Salary_Integral)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Integral, Salary_Integral);
	}

	/** Get Salary_Integral.
		@return Integral Salary
	  */
	public BigDecimal getSalary_Integral () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Integral);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Socialbenefits.
		@param Salary_Socialbenefits 
		Salary based to calc Social Benefits
	  */
	public void setSalary_Socialbenefits (BigDecimal Salary_Socialbenefits)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Socialbenefits, Salary_Socialbenefits);
	}

	/** Get Salary_Socialbenefits.
		@return Salary based to calc Social Benefits
	  */
	public BigDecimal getSalary_Socialbenefits () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Socialbenefits);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Socialbenefits_NN.
		@param Salary_Socialbenefits_NN Salary_Socialbenefits_NN	  */
	public void setSalary_Socialbenefits_NN (BigDecimal Salary_Socialbenefits_NN)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Socialbenefits_NN, Salary_Socialbenefits_NN);
	}

	/** Get Salary_Socialbenefits_NN.
		@return Salary_Socialbenefits_NN	  */
	public BigDecimal getSalary_Socialbenefits_NN () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Socialbenefits_NN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Socialbenefits_NU.
		@param Salary_Socialbenefits_NU 
		Salary based to calc Social Benefits portion from NU (Utilities)
	  */
	public void setSalary_Socialbenefits_NU (BigDecimal Salary_Socialbenefits_NU)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Socialbenefits_NU, Salary_Socialbenefits_NU);
	}

	/** Get Salary_Socialbenefits_NU.
		@return Salary based to calc Social Benefits portion from NU (Utilities)
	  */
	public BigDecimal getSalary_Socialbenefits_NU () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Socialbenefits_NU);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Socialbenefits_NV.
		@param Salary_Socialbenefits_NV 
		Salary based to calc Social Benefits portion from NV (Vacations)
	  */
	public void setSalary_Socialbenefits_NV (BigDecimal Salary_Socialbenefits_NV)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Socialbenefits_NV, Salary_Socialbenefits_NV);
	}

	/** Get Salary_Socialbenefits_NV.
		@return Salary based to calc Social Benefits portion from NV (Vacations)
	  */
	public BigDecimal getSalary_Socialbenefits_NV () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Socialbenefits_NV);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Socialbenefits_Updated.
		@param Salary_Socialbenefits_Updated 
		Salary Updated by User based to calc Social Benefits
	  */
	public void setSalary_Socialbenefits_Updated (BigDecimal Salary_Socialbenefits_Updated)
	{
		set_Value (COLUMNNAME_Salary_Socialbenefits_Updated, Salary_Socialbenefits_Updated);
	}

	/** Get Salary_Socialbenefits_Updated.
		@return Salary Updated by User based to calc Social Benefits
	  */
	public BigDecimal getSalary_Socialbenefits_Updated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Socialbenefits_Updated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Utilities.
		@param Salary_Utilities 
		Salary based to calc Utilities
	  */
	public void setSalary_Utilities (BigDecimal Salary_Utilities)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Utilities, Salary_Utilities);
	}

	/** Get Salary_Utilities.
		@return Salary based to calc Utilities
	  */
	public BigDecimal getSalary_Utilities () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Utilities);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Utilities_NN.
		@param Salary_Utilities_NN 
		Salary based to calc Utilities portion from NN
	  */
	public void setSalary_Utilities_NN (BigDecimal Salary_Utilities_NN)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Utilities_NN, Salary_Utilities_NN);
	}

	/** Get Salary_Utilities_NN.
		@return Salary based to calc Utilities portion from NN
	  */
	public BigDecimal getSalary_Utilities_NN () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Utilities_NN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Utilities_NV.
		@param Salary_Utilities_NV 
		Salary based to calc Utilities portion from NV (Vacation)
	  */
	public void setSalary_Utilities_NV (BigDecimal Salary_Utilities_NV)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Utilities_NV, Salary_Utilities_NV);
	}

	/** Get Salary_Utilities_NV.
		@return Salary based to calc Utilities portion from NV (Vacation)
	  */
	public BigDecimal getSalary_Utilities_NV () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Utilities_NV);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Utilities_Updated.
		@param Salary_Utilities_Updated 
		Salary Updated by User based to calc Utilities
	  */
	public void setSalary_Utilities_Updated (BigDecimal Salary_Utilities_Updated)
	{
		set_Value (COLUMNNAME_Salary_Utilities_Updated, Salary_Utilities_Updated);
	}

	/** Get Salary_Utilities_Updated.
		@return Salary Updated by User based to calc Utilities
	  */
	public BigDecimal getSalary_Utilities_Updated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Utilities_Updated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Salary_Vacation.
		@param Salary_Vacation 
		Salary based to calc Vacation
	  */
	public void setSalary_Vacation (BigDecimal Salary_Vacation)
	{
		set_ValueNoCheck (COLUMNNAME_Salary_Vacation, Salary_Vacation);
	}

	/** Get Salary_Vacation.
		@return Salary based to calc Vacation
	  */
	public BigDecimal getSalary_Vacation () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Vacation);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_ValueNoCheck (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
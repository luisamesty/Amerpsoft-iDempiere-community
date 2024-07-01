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

/** Generated Model for AMN_Employee_Tax
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Employee_Tax extends PO implements I_AMN_Employee_Tax, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160223L;

    /** Standard Constructor */
    public X_AMN_Employee_Tax (Properties ctx, int AMN_Employee_Tax_ID, String trxName)
    {
      super (ctx, AMN_Employee_Tax_ID, trxName);
      /** if (AMN_Employee_Tax_ID == 0)
        {
			setAMN_Employee_ID (0);
// @AMN_Employee_ID@
			setAMN_Employee_Tax_ID (0);
			setAMN_Employee_Tax_UU (null);
			setC_Currency_ID (0);
			setFiscalYear (null);
			setSalary_Yr (Env.ZERO);
// 0
			setTaxRate (Env.ZERO);
// 0
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setValidTo (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_AMN_Employee_Tax (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Employee_Tax[")
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

	/** Set Employee Tax Definition.
		@param AMN_Employee_Tax_ID Employee Tax Definition	  */
	public void setAMN_Employee_Tax_ID (int AMN_Employee_Tax_ID)
	{
		if (AMN_Employee_Tax_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_Tax_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_Tax_ID, Integer.valueOf(AMN_Employee_Tax_ID));
	}

	/** Get Employee Tax Definition.
		@return Employee Tax Definition	  */
	public int getAMN_Employee_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_Tax_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Employee_Tax_UU.
		@param AMN_Employee_Tax_UU AMN_Employee_Tax_UU	  */
	public void setAMN_Employee_Tax_UU (String AMN_Employee_Tax_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Employee_Tax_UU, AMN_Employee_Tax_UU);
	}

	/** Get AMN_Employee_Tax_UU.
		@return AMN_Employee_Tax_UU	  */
	public String getAMN_Employee_Tax_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Employee_Tax_UU);
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

	/** Set Year.
		@param FiscalYear 
		The Fiscal Year
	  */
	public void setFiscalYear (String FiscalYear)
	{
		set_Value (COLUMNNAME_FiscalYear, FiscalYear);
	}

	/** Get Year.
		@return The Fiscal Year
	  */
	public String getFiscalYear () 
	{
		return (String)get_Value(COLUMNNAME_FiscalYear);
	}

	/** Set Salary Year Estimated.
		@param Salary_Yr Salary Year Estimated	  */
	public void setSalary_Yr (BigDecimal Salary_Yr)
	{
		set_Value (COLUMNNAME_Salary_Yr, Salary_Yr);
	}

	/** Get Salary Year Estimated.
		@return Salary Year Estimated	  */
	public BigDecimal getSalary_Yr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary_Yr);
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
}
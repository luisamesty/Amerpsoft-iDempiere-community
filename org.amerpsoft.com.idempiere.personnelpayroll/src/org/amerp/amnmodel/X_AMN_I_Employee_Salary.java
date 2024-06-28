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

/** Generated Model for AMN_I_Employee_Salary
 *  @author iDempiere (generated) 
 *  @version Release 5.1 - $Id$ */
public class X_AMN_I_Employee_Salary extends PO implements I_AMN_I_Employee_Salary, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20190308L;

    /** Standard Constructor */
    public X_AMN_I_Employee_Salary (Properties ctx, int AMN_I_Employee_Salary_ID, String trxName)
    {
      super (ctx, AMN_I_Employee_Salary_ID, trxName);
      /** if (AMN_I_Employee_Salary_ID == 0)
        {
			setAMN_I_Employee_Salary_ID (0);
			setPrestacionDays (0);
// 0
			setUtilityDays (0);
// 0
			setVacationDays (0);
// 0
        } */
    }

    /** Load Constructor */
    public X_AMN_I_Employee_Salary (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_I_Employee_Salary[")
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

	/** Set Import Employee Salary for Accrued Social Benefits.
		@param AMN_I_Employee_Salary_ID Import Employee Salary for Accrued Social Benefits	  */
	public void setAMN_I_Employee_Salary_ID (int AMN_I_Employee_Salary_ID)
	{
		if (AMN_I_Employee_Salary_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_I_Employee_Salary_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_I_Employee_Salary_ID, Integer.valueOf(AMN_I_Employee_Salary_ID));
	}

	/** Get Import Employee Salary for Accrued Social Benefits.
		@return Import Employee Salary for Accrued Social Benefits	  */
	public int getAMN_I_Employee_Salary_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_I_Employee_Salary_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_I_Employee_Salary_UU.
		@param AMN_I_Employee_Salary_UU AMN_I_Employee_Salary_UU	  */
	public void setAMN_I_Employee_Salary_UU (String AMN_I_Employee_Salary_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_I_Employee_Salary_UU, AMN_I_Employee_Salary_UU);
	}

	/** Get AMN_I_Employee_Salary_UU.
		@return AMN_I_Employee_Salary_UU	  */
	public String getAMN_I_Employee_Salary_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_I_Employee_Salary_UU);
	}

	public I_AMN_Payroll getAMN_Payroll() throws RuntimeException
    {
		return (I_AMN_Payroll)MTable.get(getCtx(), I_AMN_Payroll.Table_Name)
			.getPO(getAMN_Payroll_ID(), get_TrxName());	}

	/** Set Payroll Invoice.
		@param AMN_Payroll_ID Payroll Invoice	  */
	public void setAMN_Payroll_ID (int AMN_Payroll_ID)
	{
		if (AMN_Payroll_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_ID, Integer.valueOf(AMN_Payroll_ID));
	}

	/** Get Payroll Invoice.
		@return Payroll Invoice	  */
	public int getAMN_Payroll_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_ConversionType() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
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

	/** Set Concept Value.
		@param ConceptValue 
		Value of the Concept
	  */
	public void setConceptValue (String ConceptValue)
	{
		set_Value (COLUMNNAME_ConceptValue, ConceptValue);
	}

	/** Get Concept Value.
		@return Value of the Concept
	  */
	public String getConceptValue () 
	{
		return (String)get_Value(COLUMNNAME_ConceptValue);
	}

	/** Set Delete All Records.
		@param DeleteAllRecords 
		Delete All records from table special for impo
	  */
	public void setDeleteAllRecords (String DeleteAllRecords)
	{
		set_Value (COLUMNNAME_DeleteAllRecords, DeleteAllRecords);
	}

	/** Get Delete All Records.
		@return Delete All records from table special for impo
	  */
	public String getDeleteAllRecords () 
	{
		return (String)get_Value(COLUMNNAME_DeleteAllRecords);
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

	/** Set Import Error Message.
		@param I_ErrorMsg 
		Messages generated from import process
	  */
	public void setI_ErrorMsg (String I_ErrorMsg)
	{
		set_Value (COLUMNNAME_I_ErrorMsg, I_ErrorMsg);
	}

	/** Get Import Error Message.
		@return Messages generated from import process
	  */
	public String getI_ErrorMsg () 
	{
		return (String)get_Value(COLUMNNAME_I_ErrorMsg);
	}

	/** Set Imported.
		@param I_IsImported 
		Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported)
	{
		set_Value (COLUMNNAME_I_IsImported, Boolean.valueOf(I_IsImported));
	}

	/** Get Imported.
		@return Has this import been processed
	  */
	public boolean isI_IsImported () 
	{
		Object oo = get_Value(COLUMNNAME_I_IsImported);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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
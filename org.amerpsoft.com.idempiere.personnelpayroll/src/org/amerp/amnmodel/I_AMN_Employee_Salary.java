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
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AMN_Employee_Salary
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Employee_Salary 
{

    /** TableName=AMN_Employee_Salary */
    public static final String Table_Name = "AMN_Employee_Salary";

    /** AD_Table_ID=1000059 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AMN_Concept_Types_Proc_ID */
    public static final String COLUMNNAME_AMN_Concept_Types_Proc_ID = "AMN_Concept_Types_Proc_ID";

	/** Set Payroll Concept Types Process	  */
	public void setAMN_Concept_Types_Proc_ID (int AMN_Concept_Types_Proc_ID);

	/** Get Payroll Concept Types Process	  */
	public int getAMN_Concept_Types_Proc_ID();

	public I_AMN_Concept_Types_Proc getAMN_Concept_Types_Proc() throws RuntimeException;

    /** Column name AMN_Employee_ID */
    public static final String COLUMNNAME_AMN_Employee_ID = "AMN_Employee_ID";

	/** Set Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID);

	/** Get Payroll employee	  */
	public int getAMN_Employee_ID();

	public I_AMN_Employee getAMN_Employee() throws RuntimeException;

    /** Column name AMN_Employee_Salary_ID */
    public static final String COLUMNNAME_AMN_Employee_Salary_ID = "AMN_Employee_Salary_ID";

	/** Set Employee Salary	  */
	public void setAMN_Employee_Salary_ID (int AMN_Employee_Salary_ID);

	/** Get Employee Salary	  */
	public int getAMN_Employee_Salary_ID();

    /** Column name AMN_Employee_Salary_UU */
    public static final String COLUMNNAME_AMN_Employee_Salary_UU = "AMN_Employee_Salary_UU";

	/** Set AMN_Employee_Salary_UU	  */
	public void setAMN_Employee_Salary_UU (String AMN_Employee_Salary_UU);

	/** Get AMN_Employee_Salary_UU	  */
	public String getAMN_Employee_Salary_UU();

    /** Column name AMN_Payroll_ID */
    public static final String COLUMNNAME_AMN_Payroll_ID = "AMN_Payroll_ID";

	/** Set Payroll Invoices	  */
	public void setAMN_Payroll_ID (int AMN_Payroll_ID);

	/** Get Payroll Invoices	  */
	public int getAMN_Payroll_ID();

	public I_AMN_Payroll getAMN_Payroll() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsLess20 */
    public static final String COLUMNNAME_IsLess20 = "IsLess20";

	/** Set Is Less 20	  */
	public void setIsLess20 (boolean IsLess20);

	/** Get Is Less 20	  */
	public boolean isLess20();

    /** Column name IsRural */
    public static final String COLUMNNAME_IsRural = "IsRural";

	/** Set Is Rural	  */
	public void setIsRural (boolean IsRural);

	/** Get Is Rural	  */
	public boolean isRural();

    /** Column name PrestacionAccumulated */
    public static final String COLUMNNAME_PrestacionAccumulated = "PrestacionAccumulated";

	/** Set Prestacion Accumulated	  */
	public void setPrestacionAccumulated (BigDecimal PrestacionAccumulated);

	/** Get Prestacion Accumulated	  */
	public BigDecimal getPrestacionAccumulated();

    /** Column name PrestacionAdjustment */
    public static final String COLUMNNAME_PrestacionAdjustment = "PrestacionAdjustment";

	/** Set Prestacion Adjustment	  */
	public void setPrestacionAdjustment (BigDecimal PrestacionAdjustment);

	/** Get Prestacion Adjustment	  */
	public BigDecimal getPrestacionAdjustment();

    /** Column name PrestacionAdvance */
    public static final String COLUMNNAME_PrestacionAdvance = "PrestacionAdvance";

	/** Set Prestacion Advance	  */
	public void setPrestacionAdvance (BigDecimal PrestacionAdvance);

	/** Get Prestacion Advance	  */
	public BigDecimal getPrestacionAdvance();

    /** Column name PrestacionAmount */
    public static final String COLUMNNAME_PrestacionAmount = "PrestacionAmount";

	/** Set Prestacion Amount	  */
	public void setPrestacionAmount (BigDecimal PrestacionAmount);

	/** Get Prestacion Amount	  */
	public BigDecimal getPrestacionAmount();

    /** Column name PrestacionDays */
    public static final String COLUMNNAME_PrestacionDays = "PrestacionDays";

	/** Set Prestacion Days	  */
	public void setPrestacionDays (int PrestacionDays);

	/** Get Prestacion Days	  */
	public int getPrestacionDays();

    /** Column name PrestacionInterest */
    public static final String COLUMNNAME_PrestacionInterest = "PrestacionInterest";

	/** Set Prestacion Interest	  */
	public void setPrestacionInterest (BigDecimal PrestacionInterest);

	/** Get Prestacion Interest	  */
	public BigDecimal getPrestacionInterest();

    /** Column name PrestacionInterestAdjustment */
    public static final String COLUMNNAME_PrestacionInterestAdjustment = "PrestacionInterestAdjustment";

	/** Set Prestacion Interest Adjustment	  */
	public void setPrestacionInterestAdjustment (BigDecimal PrestacionInterestAdjustment);

	/** Get Prestacion Interest Adjustment	  */
	public BigDecimal getPrestacionInterestAdjustment();

    /** Column name PrestacionInterestAdvance */
    public static final String COLUMNNAME_PrestacionInterestAdvance = "PrestacionInterestAdvance";

	/** Set Prestacion Interest Advance	  */
	public void setPrestacionInterestAdvance (BigDecimal PrestacionInterestAdvance);

	/** Get Prestacion Interest Advance	  */
	public BigDecimal getPrestacionInterestAdvance();

    /** Column name Salary */
    public static final String COLUMNNAME_Salary = "Salary";

	/** Set Salary	  */
	public void setSalary (BigDecimal Salary);

	/** Get Salary	  */
	public BigDecimal getSalary();

    /** Column name Salary_Day */
    public static final String COLUMNNAME_Salary_Day = "Salary_Day";

	/** Set Salary Day	  */
	public void setSalary_Day (BigDecimal Salary_Day);

	/** Get Salary Day	  */
	public BigDecimal getSalary_Day();

    /** Column name TaxRate */
    public static final String COLUMNNAME_TaxRate = "TaxRate";

	/** Set Tax Rate	  */
	public void setTaxRate (BigDecimal TaxRate);

	/** Get Tax Rate	  */
	public BigDecimal getTaxRate();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UtilityDays */
    public static final String COLUMNNAME_UtilityDays = "UtilityDays";

	/** Set Utility Days	  */
	public void setUtilityDays (int UtilityDays);

	/** Get Utility Days	  */
	public int getUtilityDays();

    /** Column name VacationDays */
    public static final String COLUMNNAME_VacationDays = "VacationDays";

	/** Set Vacation Days	  */
	public void setVacationDays (int VacationDays);

	/** Get Vacation Days	  */
	public int getVacationDays();

    /** Column name ValidFrom */
    public static final String COLUMNNAME_ValidFrom = "ValidFrom";

	/** Set Valid from.
	  * Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom);

	/** Get Valid from.
	  * Valid from including this date (first day)
	  */
	public Timestamp getValidFrom();

    /** Column name ValidTo */
    public static final String COLUMNNAME_ValidTo = "ValidTo";

	/** Set Valid to.
	  * Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo);

	/** Get Valid to.
	  * Valid to including this date (last day)
	  */
	public Timestamp getValidTo();

    /** Column name active_int_rate */
    public static final String COLUMNNAME_active_int_rate = "active_int_rate";

	/** Set active_int_rate	  */
	public void setactive_int_rate (BigDecimal active_int_rate);

	/** Get active_int_rate	  */
	public BigDecimal getactive_int_rate();

    /** Column name active_int_rate2 */
    public static final String COLUMNNAME_active_int_rate2 = "active_int_rate2";

	/** Set active_int_rate2	  */
	public void setactive_int_rate2 (BigDecimal active_int_rate2);

	/** Get active_int_rate2	  */
	public BigDecimal getactive_int_rate2();
}

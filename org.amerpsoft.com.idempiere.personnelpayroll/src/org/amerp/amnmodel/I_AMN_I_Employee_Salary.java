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

/** Generated Interface for AMN_I_Employee_Salary
 *  @author iDempiere (generated) 
 *  @version Release 5.1
 */
@SuppressWarnings("all")
public interface I_AMN_I_Employee_Salary 
{

    /** TableName=AMN_I_Employee_Salary */
    public static final String Table_Name = "AMN_I_Employee_Salary";

    /** AD_Table_ID=1000140 */
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

    /** Column name AMN_I_Employee_Salary_ID */
    public static final String COLUMNNAME_AMN_I_Employee_Salary_ID = "AMN_I_Employee_Salary_ID";

	/** Set Import Employee Salary for Accrued Social Benefits	  */
	public void setAMN_I_Employee_Salary_ID (int AMN_I_Employee_Salary_ID);

	/** Get Import Employee Salary for Accrued Social Benefits	  */
	public int getAMN_I_Employee_Salary_ID();

    /** Column name AMN_I_Employee_Salary_UU */
    public static final String COLUMNNAME_AMN_I_Employee_Salary_UU = "AMN_I_Employee_Salary_UU";

	/** Set AMN_I_Employee_Salary_UU	  */
	public void setAMN_I_Employee_Salary_UU (String AMN_I_Employee_Salary_UU);

	/** Get AMN_I_Employee_Salary_UU	  */
	public String getAMN_I_Employee_Salary_UU();

    /** Column name AMN_Payroll_ID */
    public static final String COLUMNNAME_AMN_Payroll_ID = "AMN_Payroll_ID";

	/** Set Payroll Invoice	  */
	public void setAMN_Payroll_ID (int AMN_Payroll_ID);

	/** Get Payroll Invoice	  */
	public int getAMN_Payroll_ID();

	public I_AMN_Payroll getAMN_Payroll() throws RuntimeException;

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public org.compiere.model.I_C_Currency getC_ConversionType() throws RuntimeException;

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

    /** Column name ConceptValue */
    public static final String COLUMNNAME_ConceptValue = "ConceptValue";

	/** Set Concept Value.
	  * Value of the Concept
	  */
	public void setConceptValue (String ConceptValue);

	/** Get Concept Value.
	  * Value of the Concept
	  */
	public String getConceptValue();

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

    /** Column name DeleteAllRecords */
    public static final String COLUMNNAME_DeleteAllRecords = "DeleteAllRecords";

	/** Set Delete All Records.
	  * Delete All records from table special for impo
	  */
	public void setDeleteAllRecords (String DeleteAllRecords);

	/** Get Delete All Records.
	  * Delete All records from table special for impo
	  */
	public String getDeleteAllRecords();

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

    /** Column name I_ErrorMsg */
    public static final String COLUMNNAME_I_ErrorMsg = "I_ErrorMsg";

	/** Set Import Error Message.
	  * Messages generated from import process
	  */
	public void setI_ErrorMsg (String I_ErrorMsg);

	/** Get Import Error Message.
	  * Messages generated from import process
	  */
	public String getI_ErrorMsg();

    /** Column name I_IsImported */
    public static final String COLUMNNAME_I_IsImported = "I_IsImported";

	/** Set Imported.
	  * Has this import been processed
	  */
	public void setI_IsImported (boolean I_IsImported);

	/** Get Imported.
	  * Has this import been processed
	  */
	public boolean isI_IsImported();

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

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}

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

/** Generated Interface for AMN_Payroll
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Payroll 
{

    /** TableName=AMN_Payroll */
    public static final String Table_Name = "AMN_Payroll";

    /** AD_Table_ID=1000028 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

    /** Column name AMN_Contract_ID */
    public static final String COLUMNNAME_AMN_Contract_ID = "AMN_Contract_ID";

	/** Set Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID);

	/** Get Payroll Contract	  */
	public int getAMN_Contract_ID();

    /** Column name AMN_Department_ID */
    public static final String COLUMNNAME_AMN_Department_ID = "AMN_Department_ID";

	/** Set Payroll Department	  */
	public void setAMN_Department_ID (int AMN_Department_ID);

	/** Get Payroll Department	  */
	public int getAMN_Department_ID();

    /** Column name AMN_Employee_ID */
    public static final String COLUMNNAME_AMN_Employee_ID = "AMN_Employee_ID";

	/** Set Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID);

	/** Get Payroll employee	  */
	public int getAMN_Employee_ID();

    /** Column name AMN_Jobstation_ID */
    public static final String COLUMNNAME_AMN_Jobstation_ID = "AMN_Jobstation_ID";

	/** Set Payroll Job Station	  */
	public void setAMN_Jobstation_ID (int AMN_Jobstation_ID);

	/** Get Payroll Job Station	  */
	public int getAMN_Jobstation_ID();

	public I_AMN_Jobstation getAMN_Jobstation() throws RuntimeException;

    /** Column name AMN_Jobtitle_ID */
    public static final String COLUMNNAME_AMN_Jobtitle_ID = "AMN_Jobtitle_ID";

	/** Set Payroll Job Title	  */
	public void setAMN_Jobtitle_ID (int AMN_Jobtitle_ID);

	/** Get Payroll Job Title	  */
	public int getAMN_Jobtitle_ID();

	public I_AMN_Jobtitle getAMN_Jobtitle() throws RuntimeException;

    /** Column name AMN_Location_ID */
    public static final String COLUMNNAME_AMN_Location_ID = "AMN_Location_ID";

	/** Set Payroll Location	  */
	public void setAMN_Location_ID (int AMN_Location_ID);

	/** Get Payroll Location	  */
	public int getAMN_Location_ID();

    /** Column name AMN_Payroll_ID */
    public static final String COLUMNNAME_AMN_Payroll_ID = "AMN_Payroll_ID";

	/** Set Payroll Invoice	  */
	public void setAMN_Payroll_ID (int AMN_Payroll_ID);

	/** Get Payroll Invoice	  */
	public int getAMN_Payroll_ID();

    /** Column name AMN_Payroll_Lot_ID */
    public static final String COLUMNNAME_AMN_Payroll_Lot_ID = "AMN_Payroll_Lot_ID";

	/** Set Payroll Receipt Lot	  */
	public void setAMN_Payroll_Lot_ID (int AMN_Payroll_Lot_ID);

	/** Get Payroll Receipt Lot	  */
	public int getAMN_Payroll_Lot_ID();

	public I_AMN_Payroll_Lot getAMN_Payroll_Lot() throws RuntimeException;

    /** Column name AMN_Payroll_UU */
    public static final String COLUMNNAME_AMN_Payroll_UU = "AMN_Payroll_UU";

	/** Set AMN_Payroll_UU	  */
	public void setAMN_Payroll_UU (String AMN_Payroll_UU);

	/** Get AMN_Payroll_UU	  */
	public String getAMN_Payroll_UU();

    /** Column name AMN_Period_ID */
    public static final String COLUMNNAME_AMN_Period_ID = "AMN_Period_ID";

	/** Set Payroll Period	  */
	public void setAMN_Period_ID (int AMN_Period_ID);

	/** Get Payroll Period	  */
	public int getAMN_Period_ID();

    /** Column name AMN_Process_ID */
    public static final String COLUMNNAME_AMN_Process_ID = "AMN_Process_ID";

	/** Set Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID);

	/** Get Payroll Process	  */
	public int getAMN_Process_ID();

	public I_AMN_Process getAMN_Process() throws RuntimeException;

    /** Column name AmountAllocated */
    public static final String COLUMNNAME_AmountAllocated = "AmountAllocated";

	/** Set AmountAllocated.
	  * Payroll Allocation
	  */
	public void setAmountAllocated (BigDecimal AmountAllocated);

	/** Get AmountAllocated.
	  * Payroll Allocation
	  */
	public BigDecimal getAmountAllocated();

    /** Column name AmountCalculated */
    public static final String COLUMNNAME_AmountCalculated = "AmountCalculated";

	/** Set AmountCalculated	  */
	public void setAmountCalculated (BigDecimal AmountCalculated);

	/** Get AmountCalculated	  */
	public BigDecimal getAmountCalculated();

    /** Column name AmountDeducted */
    public static final String COLUMNNAME_AmountDeducted = "AmountDeducted";

	/** Set AmountDeducted.
	  * Payroll Deduction
	  */
	public void setAmountDeducted (BigDecimal AmountDeducted);

	/** Get AmountDeducted.
	  * Payroll Deduction
	  */
	public BigDecimal getAmountDeducted();

    /** Column name AmountNetpaid */
    public static final String COLUMNNAME_AmountNetpaid = "AmountNetpaid";

	/** Set Amount Net Paid	  */
	public void setAmountNetpaid (BigDecimal AmountNetpaid);

	/** Get Amount Net Paid	  */
	public BigDecimal getAmountNetpaid();

    /** Column name C_Activity_ID */
    public static final String COLUMNNAME_C_Activity_ID = "C_Activity_ID";

	/** Set Activity.
	  * Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID);

	/** Get Activity.
	  * Business Activity
	  */
	public int getC_Activity_ID();

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException;

    /** Column name C_Campaign_ID */
    public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";

	/** Set Campaign.
	  * Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID);

	/** Get Campaign.
	  * Marketing Campaign
	  */
	public int getC_Campaign_ID();

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException;

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

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException;

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

    /** Column name C_Currency_ID_To */
    public static final String COLUMNNAME_C_Currency_ID_To = "C_Currency_ID_To";

	/** Set Currency To.
	  * Target currency
	  */
	public void setC_Currency_ID_To (int C_Currency_ID_To);

	/** Get Currency To.
	  * Target currency
	  */
	public int getC_Currency_ID_To();

	public org.compiere.model.I_C_Currency getC_Currency_To() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_DocTypeTarget_ID */
    public static final String COLUMNNAME_C_DocTypeTarget_ID = "C_DocTypeTarget_ID";

	/** Set Target Document Type.
	  * Target document type for conversing documents
	  */
	public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID);

	/** Get Target Document Type.
	  * Target document type for conversing documents
	  */
	public int getC_DocTypeTarget_ID();

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException;

    /** Column name C_Invoice_ID */
    public static final String COLUMNNAME_C_Invoice_ID = "C_Invoice_ID";

	/** Set Invoice.
	  * Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID);

	/** Get Invoice.
	  * Invoice Identifier
	  */
	public int getC_Invoice_ID();

	public org.compiere.model.I_C_Invoice getC_Invoice() throws RuntimeException;

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException;

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException;

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

    /** Column name C_SalesRegion_ID */
    public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";

	/** Set Sales Region.
	  * Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID);

	/** Get Sales Region.
	  * Sales coverage region
	  */
	public int getC_SalesRegion_ID();

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException;

    /** Column name CurrencyRate */
    public static final String COLUMNNAME_CurrencyRate = "CurrencyRate";

	/** Set Rate.
	  * Currency Conversion Rate
	  */
	public void setCurrencyRate (BigDecimal CurrencyRate);

	/** Get Rate.
	  * Currency Conversion Rate
	  */
	public BigDecimal getCurrencyRate();

    /** Column name DateAcct */
    public static final String COLUMNNAME_DateAcct = "DateAcct";

	/** Set Account Date.
	  * Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct);

	/** Get Account Date.
	  * Accounting Date
	  */
	public Timestamp getDateAcct();

    /** Column name DateApplication */
    public static final String COLUMNNAME_DateApplication = "DateApplication";

	/** Set Application Date.
	  * Application Date for period
	  */
	public void setDateApplication (Timestamp DateApplication);

	/** Get Application Date.
	  * Application Date for period
	  */
	public Timestamp getDateApplication();

    /** Column name DateReEntry */
    public static final String COLUMNNAME_DateReEntry = "DateReEntry";

	/** Set ReEntry Date.
	  * Entry Date for Vacation period
	  */
	public void setDateReEntry (Timestamp DateReEntry);

	/** Get ReEntry Date.
	  * Entry Date for Vacation period
	  */
	public Timestamp getDateReEntry();

    /** Column name DateReEntryReal */
    public static final String COLUMNNAME_DateReEntryReal = "DateReEntryReal";

	/** Set ReEntry Date Real.
	  * Real ReEntry Date for Employee
	  */
	public void setDateReEntryReal (Timestamp DateReEntryReal);

	/** Get ReEntry Date Real.
	  * Real ReEntry Date for Employee
	  */
	public Timestamp getDateReEntryReal();

    /** Column name DaysVacation */
    public static final String COLUMNNAME_DaysVacation = "DaysVacation";

	/** Set DaysVacation.
	  * Number of days for Vacations
	  */
	public void setDaysVacation (int DaysVacation);

	/** Get DaysVacation.
	  * Number of days for Vacations
	  */
	public int getDaysVacation();

    /** Column name DaysVacationCollective */
    public static final String COLUMNNAME_DaysVacationCollective = "DaysVacationCollective";

	/** Set Days Vacation Collective.
	  * Number of days for Collective Vacations
	  */
	public void setDaysVacationCollective (int DaysVacationCollective);

	/** Get Days Vacation Collective.
	  * Number of days for Collective Vacations
	  */
	public int getDaysVacationCollective();

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

    /** Column name DocAction */
    public static final String COLUMNNAME_DocAction = "DocAction";

	/** Set Document Action.
	  * The targeted status of the document
	  */
	public void setDocAction (String DocAction);

	/** Get Document Action.
	  * The targeted status of the document
	  */
	public String getDocAction();

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name InvDateEnd */
    public static final String COLUMNNAME_InvDateEnd = "InvDateEnd";

	/** Set InvDateEnd	  */
	public void setInvDateEnd (Timestamp InvDateEnd);

	/** Get InvDateEnd	  */
	public Timestamp getInvDateEnd();

    /** Column name InvDateIni */
    public static final String COLUMNNAME_InvDateIni = "InvDateIni";

	/** Set InvDateIni	  */
	public void setInvDateIni (Timestamp InvDateIni);

	/** Get InvDateIni	  */
	public Timestamp getInvDateIni();

    /** Column name InvDateRec */
    public static final String COLUMNNAME_InvDateRec = "InvDateRec";

	/** Set Invoice Date Receipt.
	  * Invoice Date of  Receipt
	  */
	public void setInvDateRec (Timestamp InvDateRec);

	/** Get Invoice Date Receipt.
	  * Invoice Date of  Receipt
	  */
	public Timestamp getInvDateRec();

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

    /** Column name IsApproved */
    public static final String COLUMNNAME_IsApproved = "IsApproved";

	/** Set Approved.
	  * Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved);

	/** Get Approved.
	  * Indicates if this document requires approval
	  */
	public boolean isApproved();

    /** Column name IsOverrideCalc */
    public static final String COLUMNNAME_IsOverrideCalc = "IsOverrideCalc";

	/** Set Override Calculations.
	  * Override Calculations on Dates
	  */
	public void setIsOverrideCalc (boolean IsOverrideCalc);

	/** Get Override Calculations.
	  * Override Calculations on Dates
	  */
	public boolean isOverrideCalc();

    /** Column name IsOverrideCurrencyRate */
    public static final String COLUMNNAME_IsOverrideCurrencyRate = "IsOverrideCurrencyRate";

	/** Set Override Currency Conversion Rate.
	  * Override Currency Conversion Rate
	  */
	public void setIsOverrideCurrencyRate (boolean IsOverrideCurrencyRate);

	/** Get Override Currency Conversion Rate.
	  * Override Currency Conversion Rate
	  */
	public boolean isOverrideCurrencyRate();

    /** Column name IsPaid */
    public static final String COLUMNNAME_IsPaid = "IsPaid";

	/** Set Paid.
	  * The document is fully paid
	  */
	public void setIsPaid (boolean IsPaid);

	/** Get Paid.
	  * The document is fully paid
	  */
	public boolean isPaid();

    /** Column name IsPrinted */
    public static final String COLUMNNAME_IsPrinted = "IsPrinted";

	/** Set Printed.
	  * Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted);

	/** Get Printed.
	  * Indicates if this document / line is printed
	  */
	public boolean isPrinted();

    /** Column name month */
    public static final String COLUMNNAME_month = "month";

	/** Set month	  */
	public void setmonth (int month);

	/** Get month	  */
	public int getmonth();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Posted */
    public static final String COLUMNNAME_Posted = "Posted";

	/** Set Posted.
	  * Posting status
	  */
	public void setPosted (boolean Posted);

	/** Get Posted.
	  * Posting status
	  */
	public boolean isPosted();

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

    /** Column name ProcessedOn */
    public static final String COLUMNNAME_ProcessedOn = "ProcessedOn";

	/** Set Processed On.
	  * The date+time (expressed in decimal format) when the document has been processed
	  */
	public void setProcessedOn (BigDecimal ProcessedOn);

	/** Get Processed On.
	  * The date+time (expressed in decimal format) when the document has been processed
	  */
	public BigDecimal getProcessedOn();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name RefDateEnd */
    public static final String COLUMNNAME_RefDateEnd = "RefDateEnd";

	/** Set RefDateEnd.
	  * End Reference Date
	  */
	public void setRefDateEnd (Timestamp RefDateEnd);

	/** Get RefDateEnd.
	  * End Reference Date
	  */
	public Timestamp getRefDateEnd();

    /** Column name RefDateIni */
    public static final String COLUMNNAME_RefDateIni = "RefDateIni";

	/** Set RefDateIni.
	  * Initial Reference Date
	  */
	public void setRefDateIni (Timestamp RefDateIni);

	/** Get RefDateIni.
	  * Initial Reference Date
	  */
	public Timestamp getRefDateIni();

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

    /** Column name year */
    public static final String COLUMNNAME_year = "year";

	/** Set year	  */
	public void setyear (int year);

	/** Get year	  */
	public int getyear();
}

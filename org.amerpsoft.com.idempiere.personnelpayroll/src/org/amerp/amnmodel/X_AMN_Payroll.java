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

/** Generated Model for AMN_Payroll
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Payroll extends PO implements I_AMN_Payroll, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180717L;

    /** Standard Constructor */
    public X_AMN_Payroll (Properties ctx, int AMN_Payroll_ID, String trxName)
    {
      super (ctx, AMN_Payroll_ID, trxName);
      /** if (AMN_Payroll_ID == 0)
        {
			setAMN_Contract_ID (0);
			setAMN_Department_ID (0);
			setAMN_Employee_ID (0);
			setAMN_Location_ID (0);
			setAMN_Payroll_ID (0);
			setAMN_Payroll_UU (null);
			setAMN_Period_ID (0);
			setAMN_Process_ID (0);
			setC_Currency_ID (0);
// @C_Currency_ID@
			setC_DocType_ID (0);
// 0
			setDocAction (null);
// CO
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setInvDateEnd (new Timestamp( System.currentTimeMillis() ));
			setInvDateIni (new Timestamp( System.currentTimeMillis() ));
			setIsApproved (false);
// @IsApproved@
			setIsPaid (false);
// N
			setIsPrinted (false);
			setName (null);
			setPosted (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Payroll[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Contract.
		@param AMN_Contract_ID Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID)
	{
		if (AMN_Contract_ID < 1) 
			set_Value (COLUMNNAME_AMN_Contract_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Contract_ID, Integer.valueOf(AMN_Contract_ID));
	}

	/** Get Payroll Contract.
		@return Payroll Contract	  */
	public int getAMN_Contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Department.
		@param AMN_Department_ID Payroll Department	  */
	public void setAMN_Department_ID (int AMN_Department_ID)
	{
		if (AMN_Department_ID < 1) 
			set_Value (COLUMNNAME_AMN_Department_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Department_ID, Integer.valueOf(AMN_Department_ID));
	}

	/** Get Payroll Department.
		@return Payroll Department	  */
	public int getAMN_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll employee.
		@param AMN_Employee_ID Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID)
	{
		if (AMN_Employee_ID < 1) 
			set_Value (COLUMNNAME_AMN_Employee_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
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

	public I_AMN_Jobstation getAMN_Jobstation() throws RuntimeException
    {
		return (I_AMN_Jobstation)MTable.get(getCtx(), I_AMN_Jobstation.Table_Name)
			.getPO(getAMN_Jobstation_ID(), get_TrxName());	}

	/** Set Payroll Job Station.
		@param AMN_Jobstation_ID Payroll Job Station	  */
	public void setAMN_Jobstation_ID (int AMN_Jobstation_ID)
	{
		if (AMN_Jobstation_ID < 1) 
			set_Value (COLUMNNAME_AMN_Jobstation_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Jobstation_ID, Integer.valueOf(AMN_Jobstation_ID));
	}

	/** Get Payroll Job Station.
		@return Payroll Job Station	  */
	public int getAMN_Jobstation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobstation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_Jobtitle getAMN_Jobtitle() throws RuntimeException
    {
		return (I_AMN_Jobtitle)MTable.get(getCtx(), I_AMN_Jobtitle.Table_Name)
			.getPO(getAMN_Jobtitle_ID(), get_TrxName());	}

	/** Set Payroll Job Title.
		@param AMN_Jobtitle_ID Payroll Job Title	  */
	public void setAMN_Jobtitle_ID (int AMN_Jobtitle_ID)
	{
		if (AMN_Jobtitle_ID < 1) 
			set_Value (COLUMNNAME_AMN_Jobtitle_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Jobtitle_ID, Integer.valueOf(AMN_Jobtitle_ID));
	}

	/** Get Payroll Job Title.
		@return Payroll Job Title	  */
	public int getAMN_Jobtitle_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobtitle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Location.
		@param AMN_Location_ID Payroll Location	  */
	public void setAMN_Location_ID (int AMN_Location_ID)
	{
		if (AMN_Location_ID < 1) 
			set_Value (COLUMNNAME_AMN_Location_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Location_ID, Integer.valueOf(AMN_Location_ID));
	}

	/** Get Payroll Location.
		@return Payroll Location	  */
	public int getAMN_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

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

	public I_AMN_Payroll_Lot getAMN_Payroll_Lot() throws RuntimeException
    {
		return (I_AMN_Payroll_Lot)MTable.get(getCtx(), I_AMN_Payroll_Lot.Table_Name)
			.getPO(getAMN_Payroll_Lot_ID(), get_TrxName());	}

	/** Set Payroll Receipt Lot.
		@param AMN_Payroll_Lot_ID Payroll Receipt Lot	  */
	public void setAMN_Payroll_Lot_ID (int AMN_Payroll_Lot_ID)
	{
		if (AMN_Payroll_Lot_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Lot_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Lot_ID, Integer.valueOf(AMN_Payroll_Lot_ID));
	}

	/** Get Payroll Receipt Lot.
		@return Payroll Receipt Lot	  */
	public int getAMN_Payroll_Lot_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Lot_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_UU.
		@param AMN_Payroll_UU AMN_Payroll_UU	  */
	public void setAMN_Payroll_UU (String AMN_Payroll_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Payroll_UU, AMN_Payroll_UU);
	}

	/** Get AMN_Payroll_UU.
		@return AMN_Payroll_UU	  */
	public String getAMN_Payroll_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_UU);
	}

	/** Set Payroll Period.
		@param AMN_Period_ID Payroll Period	  */
	public void setAMN_Period_ID (int AMN_Period_ID)
	{
		if (AMN_Period_ID < 1) 
			set_Value (COLUMNNAME_AMN_Period_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Period_ID, Integer.valueOf(AMN_Period_ID));
	}

	/** Get Payroll Period.
		@return Payroll Period	  */
	public int getAMN_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_Process getAMN_Process() throws RuntimeException
    {
		return (I_AMN_Process)MTable.get(getCtx(), I_AMN_Process.Table_Name)
			.getPO(getAMN_Process_ID(), get_TrxName());	}

	/** Set Payroll Process.
		@param AMN_Process_ID Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID)
	{
		if (AMN_Process_ID < 1) 
			set_Value (COLUMNNAME_AMN_Process_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Process_ID, Integer.valueOf(AMN_Process_ID));
	}

	/** Get Payroll Process.
		@return Payroll Process	  */
	public int getAMN_Process_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AmountAllocated.
		@param AmountAllocated 
		Payroll Allocation
	  */
	public void setAmountAllocated (BigDecimal AmountAllocated)
	{
		set_Value (COLUMNNAME_AmountAllocated, AmountAllocated);
	}

	/** Get AmountAllocated.
		@return Payroll Allocation
	  */
	public BigDecimal getAmountAllocated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountAllocated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmountCalculated.
		@param AmountCalculated AmountCalculated	  */
	public void setAmountCalculated (BigDecimal AmountCalculated)
	{
		set_Value (COLUMNNAME_AmountCalculated, AmountCalculated);
	}

	/** Get AmountCalculated.
		@return AmountCalculated	  */
	public BigDecimal getAmountCalculated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountCalculated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmountDeducted.
		@param AmountDeducted 
		Payroll Deduction
	  */
	public void setAmountDeducted (BigDecimal AmountDeducted)
	{
		set_Value (COLUMNNAME_AmountDeducted, AmountDeducted);
	}

	/** Get AmountDeducted.
		@return Payroll Deduction
	  */
	public BigDecimal getAmountDeducted () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountDeducted);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Amount Net Paid.
		@param AmountNetpaid Amount Net Paid	  */
	public void setAmountNetpaid (BigDecimal AmountNetpaid)
	{
		set_Value (COLUMNNAME_AmountNetpaid, AmountNetpaid);
	}

	/** Get Amount Net Paid.
		@return Amount Net Paid	  */
	public BigDecimal getAmountNetpaid () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountNetpaid);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException
    {
		return (org.compiere.model.I_C_Activity)MTable.get(getCtx(), org.compiere.model.I_C_Activity.Table_Name)
			.getPO(getC_Activity_ID(), get_TrxName());	}

	/** Set Activity.
		@param C_Activity_ID 
		Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID)
	{
		if (C_Activity_ID < 1) 
			set_Value (COLUMNNAME_C_Activity_ID, null);
		else 
			set_Value (COLUMNNAME_C_Activity_ID, Integer.valueOf(C_Activity_ID));
	}

	/** Get Activity.
		@return Business Activity
	  */
	public int getC_Activity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Activity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException
    {
		return (org.compiere.model.I_C_Campaign)MTable.get(getCtx(), org.compiere.model.I_C_Campaign.Table_Name)
			.getPO(getC_Campaign_ID(), get_TrxName());	}

	/** Set Campaign.
		@param C_Campaign_ID 
		Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID)
	{
		if (C_Campaign_ID < 1) 
			set_Value (COLUMNNAME_C_Campaign_ID, null);
		else 
			set_Value (COLUMNNAME_C_Campaign_ID, Integer.valueOf(C_Campaign_ID));
	}

	/** Get Campaign.
		@return Marketing Campaign
	  */
	public int getC_Campaign_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocTypeTarget_ID(), get_TrxName());	}

	/** Set Target Document Type.
		@param C_DocTypeTarget_ID 
		Target document type for conversing documents
	  */
	public void setC_DocTypeTarget_ID (int C_DocTypeTarget_ID)
	{
		if (C_DocTypeTarget_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeTarget_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocTypeTarget_ID, Integer.valueOf(C_DocTypeTarget_ID));
	}

	/** Get Target Document Type.
		@return Target document type for conversing documents
	  */
	public int getC_DocTypeTarget_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeTarget_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
    {
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
    {
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_Name)
			.getPO(getC_SalesRegion_ID(), get_TrxName());	}

	/** Set Sales Region.
		@param C_SalesRegion_ID 
		Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID)
	{
		if (C_SalesRegion_ID < 1) 
			set_Value (COLUMNNAME_C_SalesRegion_ID, null);
		else 
			set_Value (COLUMNNAME_C_SalesRegion_ID, Integer.valueOf(C_SalesRegion_ID));
	}

	/** Get Sales Region.
		@return Sales coverage region
	  */
	public int getC_SalesRegion_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_ValueNoCheck (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
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

	/** DocAction AD_Reference_ID=135 */
	public static final int DOCACTION_AD_Reference_ID=135;
	/** Complete = CO */
	public static final String DOCACTION_Complete = "CO";
	/** Approve = AP */
	public static final String DOCACTION_Approve = "AP";
	/** Reject = RJ */
	public static final String DOCACTION_Reject = "RJ";
	/** Post = PO */
	public static final String DOCACTION_Post = "PO";
	/** Void = VO */
	public static final String DOCACTION_Void = "VO";
	/** Close = CL */
	public static final String DOCACTION_Close = "CL";
	/** Reverse - Correct = RC */
	public static final String DOCACTION_Reverse_Correct = "RC";
	/** Reverse - Accrual = RA */
	public static final String DOCACTION_Reverse_Accrual = "RA";
	/** Invalidate = IN */
	public static final String DOCACTION_Invalidate = "IN";
	/** Re-activate = RE */
	public static final String DOCACTION_Re_Activate = "RE";
	/** <None> = -- */
	public static final String DOCACTION_None = "--";
	/** Prepare = PR */
	public static final String DOCACTION_Prepare = "PR";
	/** Unlock = XL */
	public static final String DOCACTION_Unlock = "XL";
	/** Wait Complete = WC */
	public static final String DOCACTION_WaitComplete = "WC";
	/** Set Document Action.
		@param DocAction 
		The targeted status of the document
	  */
	public void setDocAction (String DocAction)
	{

		set_Value (COLUMNNAME_DocAction, DocAction);
	}

	/** Get Document Action.
		@return The targeted status of the document
	  */
	public String getDocAction () 
	{
		return (String)get_Value(COLUMNNAME_DocAction);
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_ValueNoCheck (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set InvDateEnd.
		@param InvDateEnd InvDateEnd	  */
	public void setInvDateEnd (Timestamp InvDateEnd)
	{
		set_Value (COLUMNNAME_InvDateEnd, InvDateEnd);
	}

	/** Get InvDateEnd.
		@return InvDateEnd	  */
	public Timestamp getInvDateEnd () 
	{
		return (Timestamp)get_Value(COLUMNNAME_InvDateEnd);
	}

	/** Set InvDateIni.
		@param InvDateIni InvDateIni	  */
	public void setInvDateIni (Timestamp InvDateIni)
	{
		set_Value (COLUMNNAME_InvDateIni, InvDateIni);
	}

	/** Get InvDateIni.
		@return InvDateIni	  */
	public Timestamp getInvDateIni () 
	{
		return (Timestamp)get_Value(COLUMNNAME_InvDateIni);
	}

	/** Set Approved.
		@param IsApproved 
		Indicates if this document requires approval
	  */
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved () 
	{
		Object oo = get_Value(COLUMNNAME_IsApproved);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Paid.
		@param IsPaid 
		The document is paid
	  */
	public void setIsPaid (boolean IsPaid)
	{
		set_Value (COLUMNNAME_IsPaid, Boolean.valueOf(IsPaid));
	}

	/** Get Paid.
		@return The document is paid
	  */
	public boolean isPaid () 
	{
		Object oo = get_Value(COLUMNNAME_IsPaid);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Printed.
		@param IsPrinted 
		Indicates if this document / line is printed
	  */
	public void setIsPrinted (boolean IsPrinted)
	{
		set_Value (COLUMNNAME_IsPrinted, Boolean.valueOf(IsPrinted));
	}

	/** Get Printed.
		@return Indicates if this document / line is printed
	  */
	public boolean isPrinted () 
	{
		Object oo = get_Value(COLUMNNAME_IsPrinted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Posted.
		@param Posted 
		Posting status
	  */
	public void setPosted (boolean Posted)
	{
		set_Value (COLUMNNAME_Posted, Boolean.valueOf(Posted));
	}

	/** Get Posted.
		@return Posting status
	  */
	public boolean isPosted () 
	{
		Object oo = get_Value(COLUMNNAME_Posted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Processed On.
		@param ProcessedOn 
		The date+time (expressed in decimal format) when the document has been processed
	  */
	public void setProcessedOn (BigDecimal ProcessedOn)
	{
		set_Value (COLUMNNAME_ProcessedOn, ProcessedOn);
	}

	/** Get Processed On.
		@return The date+time (expressed in decimal format) when the document has been processed
	  */
	public BigDecimal getProcessedOn () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ProcessedOn);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set RefDateEnd.
		@param RefDateEnd 
		End Reference Date
	  */
	public void setRefDateEnd (Timestamp RefDateEnd)
	{
		set_Value (COLUMNNAME_RefDateEnd, RefDateEnd);
	}

	/** Get RefDateEnd.
		@return End Reference Date
	  */
	public Timestamp getRefDateEnd () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RefDateEnd);
	}

	/** Set RefDateIni.
		@param RefDateIni 
		Initial Reference Date
	  */
	public void setRefDateIni (Timestamp RefDateIni)
	{
		set_Value (COLUMNNAME_RefDateIni, RefDateIni);
	}

	/** Get RefDateIni.
		@return Initial Reference Date
	  */
	public Timestamp getRefDateIni () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RefDateIni);
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
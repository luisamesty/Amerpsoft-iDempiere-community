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

/** Generated Model for AMN_Leaves
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Leaves")
public class X_AMN_Leaves extends PO implements I_AMN_Leaves, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241221L;

    /** Standard Constructor */
    public X_AMN_Leaves (Properties ctx, int AMN_Leaves_ID, String trxName)
    {
      super (ctx, AMN_Leaves_ID, trxName);
      /** if (AMN_Leaves_ID == 0)
        {
			setAMN_Employee_ID (0);
			setAMN_Leaves_ID (0);
			setAMN_Leaves_Types_ID (0);
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDateTo (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setIsOverrideCalc (false);
// N
			setProcessed (false);
// N
			setProcessing (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves (Properties ctx, int AMN_Leaves_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_ID, trxName, virtualColumns);
      /** if (AMN_Leaves_ID == 0)
        {
			setAMN_Employee_ID (0);
			setAMN_Leaves_ID (0);
			setAMN_Leaves_Types_ID (0);
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDateTo (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setIsOverrideCalc (false);
// N
			setProcessed (false);
// N
			setProcessing (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves (Properties ctx, String AMN_Leaves_UU, String trxName)
    {
      super (ctx, AMN_Leaves_UU, trxName);
      /** if (AMN_Leaves_UU == null)
        {
			setAMN_Employee_ID (0);
			setAMN_Leaves_ID (0);
			setAMN_Leaves_Types_ID (0);
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDateTo (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setIsOverrideCalc (false);
// N
			setProcessed (false);
// N
			setProcessing (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves (Properties ctx, String AMN_Leaves_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_UU, trxName, virtualColumns);
      /** if (AMN_Leaves_UU == null)
        {
			setAMN_Employee_ID (0);
			setAMN_Leaves_ID (0);
			setAMN_Leaves_Types_ID (0);
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDateTo (new Timestamp( System.currentTimeMillis() ));
// @SQL=SELECT TRUNC(SYSDATE)
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setIsApproved (false);
// N
			setIsOverrideCalc (false);
// N
			setProcessed (false);
// N
			setProcessing (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AMN_Leaves (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Leaves[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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
			set_Value (COLUMNNAME_AMN_Employee_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
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

	/** Set Leaves and Licences.
		@param AMN_Leaves_ID Leaves and Licences
	*/
	public void setAMN_Leaves_ID (int AMN_Leaves_ID)
	{
		if (AMN_Leaves_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_ID, Integer.valueOf(AMN_Leaves_ID));
	}

	/** Get Leaves and Licences.
		@return Leaves and Licences
	  */
	public int getAMN_Leaves_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Leaves Process.
		@param AMN_Leaves_Process Leaves Process to next valid step
	*/
	public void setAMN_Leaves_Process (String AMN_Leaves_Process)
	{
		set_Value (COLUMNNAME_AMN_Leaves_Process, AMN_Leaves_Process);
	}

	/** Get Leaves Process.
		@return Leaves Process to next valid step
	  */
	public String getAMN_Leaves_Process()
	{
		return (String)get_Value(COLUMNNAME_AMN_Leaves_Process);
	}

	/** Set Leaves Types.
		@param AMN_Leaves_Types_ID Leaves Types
	*/
	public void setAMN_Leaves_Types_ID (int AMN_Leaves_Types_ID)
	{
		if (AMN_Leaves_Types_ID < 1)
			set_Value (COLUMNNAME_AMN_Leaves_Types_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Leaves_Types_ID, Integer.valueOf(AMN_Leaves_Types_ID));
	}

	/** Get Leaves Types.
		@return Leaves Types	  */
	public int getAMN_Leaves_Types_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_Types_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Leaves_UU.
		@param AMN_Leaves_UU AMN_Leaves_UU
	*/
	public void setAMN_Leaves_UU (String AMN_Leaves_UU)
	{
		set_Value (COLUMNNAME_AMN_Leaves_UU, AMN_Leaves_UU);
	}

	/** Get AMN_Leaves_UU.
		@return AMN_Leaves_UU	  */
	public String getAMN_Leaves_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Leaves_UU);
	}

	/** Set AMW Work Flows.
		@param AMW_WorkFlow_ID Plugin AMW Work Flows defined by User
	*/
	public void setAMW_WorkFlow_ID (int AMW_WorkFlow_ID)
	{
		if (AMW_WorkFlow_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMW_WorkFlow_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMW_WorkFlow_ID, Integer.valueOf(AMW_WorkFlow_ID));
	}

	/** Get AMW Work Flows.
		@return Plugin AMW Work Flows defined by User
	  */
	public int getAMW_WorkFlow_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WorkFlow_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocType_ID(), get_TrxName());
	}

	/** Set Document Type.
		@param C_DocType_ID Document type or rules
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
	public int getC_DocType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocTypeTarget() throws RuntimeException
	{
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_ID)
			.getPO(getC_DocTypeTarget_ID(), get_TrxName());
	}

	/** Set Target Document Type.
		@param C_DocTypeTarget_ID Target document type for conversing documents
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
	public int getC_DocTypeTarget_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocTypeTarget_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc Date of the Document
	*/
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Date From.
		@param DateFrom Starting date for a range
	*/
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Date To.
		@param DateTo End date of a date range
	*/
	public void setDateTo (Timestamp DateTo)
	{
		set_Value (COLUMNNAME_DateTo, DateTo);
	}

	/** Get Date To.
		@return End date of a date range
	  */
	public Timestamp getDateTo()
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTo);
	}

	/** Set Days To.
		@param DaysTo Number of days To 
	*/
	public void setDaysTo (BigDecimal DaysTo)
	{
		set_Value (COLUMNNAME_DaysTo, DaysTo);
	}

	/** Get Days To.
		@return Number of days To 
	  */
	public BigDecimal getDaysTo()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DaysTo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** Filed = AR */
	public static final String DOCSTATUS_Filed = "AR";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Draft = DR */
	public static final String DOCSTATUS_Draft = "DR";
	/** HR Approved = RA */
	public static final String DOCSTATUS_HRApproved = "RA";
	/** HR Review = RH */
	public static final String DOCSTATUS_HRReview = "RH";
	/** HR Rejected = RR */
	public static final String DOCSTATUS_HRRejected = "RR";
	/** Supervisor Approved = SA */
	public static final String DOCSTATUS_SupervisorApproved = "SA";
	/** Supervisor Rejected = SR */
	public static final String DOCSTATUS_SupervisorRejected = "SR";
	/** Supervisor Review = SU */
	public static final String DOCSTATUS_SupervisorReview = "SU";
	/** Set Document Status.
		@param DocStatus The current status of the document
	*/
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus()
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Document No.
		@param DocumentNo Document sequence number of the document
	*/
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Hours per day in a Shift.
		@param HoursDay Hours per day in a Shift
	*/
	public void setHoursDay (BigDecimal HoursDay)
	{
		set_Value (COLUMNNAME_HoursDay, HoursDay);
	}

	/** Get Hours per day in a Shift.
		@return Hours per day in a Shift	  */
	public BigDecimal getHoursDay()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HoursDay);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Approved.
		@param IsApproved Indicates if this document requires approval
	*/
	public void setIsApproved (boolean IsApproved)
	{
		set_Value (COLUMNNAME_IsApproved, Boolean.valueOf(IsApproved));
	}

	/** Get Approved.
		@return Indicates if this document requires approval
	  */
	public boolean isApproved()
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

	/** Set Override Calculations.
		@param IsOverrideCalc Override Calculations on Dates
	*/
	public void setIsOverrideCalc (boolean IsOverrideCalc)
	{
		set_Value (COLUMNNAME_IsOverrideCalc, Boolean.valueOf(IsOverrideCalc));
	}

	/** Get Override Calculations.
		@return Override Calculations on Dates
	  */
	public boolean isOverrideCalc()
	{
		Object oo = get_Value(COLUMNNAME_IsOverrideCalc);
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

	/** Set Note.
		@param Note Optional additional user defined information
	*/
	public void setNote (String Note)
	{
		set_Value (COLUMNNAME_Note, Note);
	}

	/** Get Note.
		@return Optional additional user defined information
	  */
	public String getNote()
	{
		return (String)get_Value(COLUMNNAME_Note);
	}

	/** Set Processed.
		@param Processed The document has been processed
	*/
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed()
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
		@param ProcessedOn The date+time (expressed in decimal format) when the document has been processed
	*/
	public void setProcessedOn (BigDecimal ProcessedOn)
	{
		set_Value (COLUMNNAME_ProcessedOn, ProcessedOn);
	}

	/** Get Processed On.
		@return The date+time (expressed in decimal format) when the document has been processed
	  */
	public BigDecimal getProcessedOn()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ProcessedOn);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Process Now.
		@param Processing Process Now
	*/
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing()
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
}
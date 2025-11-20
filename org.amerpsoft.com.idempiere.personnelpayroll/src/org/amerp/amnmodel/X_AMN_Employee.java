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

/** Generated Model for AMN_Employee
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Employee")
public class X_AMN_Employee extends PO implements I_AMN_Employee, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20251120L;

    /** Standard Constructor */
    public X_AMN_Employee (Properties ctx, int AMN_Employee_ID, String trxName)
    {
      super (ctx, AMN_Employee_ID, trxName);
      /** if (AMN_Employee_ID == 0)
        {
			setAMN_Contract_ID (0);
			setAMN_Department_ID (0);
			setAMN_Employee_ID (0);
			setAMN_Jobstation_ID (0);
			setAMN_Jobtitle_ID (0);
			setAMN_Location_ID (0);
			setAMN_Position_ID (0);
			setAMN_Shift_ID (0);
			setBioCode (null);
// 0
			setBirthday (new Timestamp( System.currentTimeMillis() ));
			setC_Activity_ID (0);
			setC_BPartner_ID (0);
			setC_Country_ID (0);
			setIsDetailedNames (false);
// N
			setName (null);
			setStatus (null);
			setUseLenses (false);
// N
			setValue (null);
			setincomedate (new Timestamp( System.currentTimeMillis() ));
			setisMedicated (false);
// N
			setisPensioned (false);
// N
			setisStudying (false);
// N
			setjobcondition (null);
			setpayrollmode (null);
			setprivateassist (null);
			setsex (null);
			setspouse (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Employee (Properties ctx, int AMN_Employee_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Employee_ID, trxName, virtualColumns);
      /** if (AMN_Employee_ID == 0)
        {
			setAMN_Contract_ID (0);
			setAMN_Department_ID (0);
			setAMN_Employee_ID (0);
			setAMN_Jobstation_ID (0);
			setAMN_Jobtitle_ID (0);
			setAMN_Location_ID (0);
			setAMN_Position_ID (0);
			setAMN_Shift_ID (0);
			setBioCode (null);
// 0
			setBirthday (new Timestamp( System.currentTimeMillis() ));
			setC_Activity_ID (0);
			setC_BPartner_ID (0);
			setC_Country_ID (0);
			setIsDetailedNames (false);
// N
			setName (null);
			setStatus (null);
			setUseLenses (false);
// N
			setValue (null);
			setincomedate (new Timestamp( System.currentTimeMillis() ));
			setisMedicated (false);
// N
			setisPensioned (false);
// N
			setisStudying (false);
// N
			setjobcondition (null);
			setpayrollmode (null);
			setprivateassist (null);
			setsex (null);
			setspouse (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Employee (Properties ctx, String AMN_Employee_UU, String trxName)
    {
      super (ctx, AMN_Employee_UU, trxName);
      /** if (AMN_Employee_UU == null)
        {
			setAMN_Contract_ID (0);
			setAMN_Department_ID (0);
			setAMN_Employee_ID (0);
			setAMN_Jobstation_ID (0);
			setAMN_Jobtitle_ID (0);
			setAMN_Location_ID (0);
			setAMN_Position_ID (0);
			setAMN_Shift_ID (0);
			setBioCode (null);
// 0
			setBirthday (new Timestamp( System.currentTimeMillis() ));
			setC_Activity_ID (0);
			setC_BPartner_ID (0);
			setC_Country_ID (0);
			setIsDetailedNames (false);
// N
			setName (null);
			setStatus (null);
			setUseLenses (false);
// N
			setValue (null);
			setincomedate (new Timestamp( System.currentTimeMillis() ));
			setisMedicated (false);
// N
			setisPensioned (false);
// N
			setisStudying (false);
// N
			setjobcondition (null);
			setpayrollmode (null);
			setprivateassist (null);
			setsex (null);
			setspouse (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Employee (Properties ctx, String AMN_Employee_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Employee_UU, trxName, virtualColumns);
      /** if (AMN_Employee_UU == null)
        {
			setAMN_Contract_ID (0);
			setAMN_Department_ID (0);
			setAMN_Employee_ID (0);
			setAMN_Jobstation_ID (0);
			setAMN_Jobtitle_ID (0);
			setAMN_Location_ID (0);
			setAMN_Position_ID (0);
			setAMN_Shift_ID (0);
			setBioCode (null);
// 0
			setBirthday (new Timestamp( System.currentTimeMillis() ));
			setC_Activity_ID (0);
			setC_BPartner_ID (0);
			setC_Country_ID (0);
			setIsDetailedNames (false);
// N
			setName (null);
			setStatus (null);
			setUseLenses (false);
// N
			setValue (null);
			setincomedate (new Timestamp( System.currentTimeMillis() ));
			setisMedicated (false);
// N
			setisPensioned (false);
// N
			setisStudying (false);
// N
			setjobcondition (null);
			setpayrollmode (null);
			setprivateassist (null);
			setsex (null);
			setspouse (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Employee (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Employee[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Inter-Organization.
		@param AD_OrgTo_ID Organization valid for intercompany documents
	*/
	public void setAD_OrgTo_ID (int AD_OrgTo_ID)
	{
		if (AD_OrgTo_ID < 1)
			set_Value (COLUMNNAME_AD_OrgTo_ID, null);
		else
			set_Value (COLUMNNAME_AD_OrgTo_ID, Integer.valueOf(AD_OrgTo_ID));
	}

	/** Get Inter-Organization.
		@return Organization valid for intercompany documents
	  */
	public int getAD_OrgTo_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_OrgTo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_CommissionGroup getAMN_CommissionGroup() throws RuntimeException
	{
		return (I_AMN_CommissionGroup)MTable.get(getCtx(), I_AMN_CommissionGroup.Table_ID)
			.getPO(getAMN_CommissionGroup_ID(), get_TrxName());
	}

	/** Set Commission Group.
		@param AMN_CommissionGroup_ID Payroll Employee commission Group OrgSector
	*/
	public void setAMN_CommissionGroup_ID (int AMN_CommissionGroup_ID)
	{
		if (AMN_CommissionGroup_ID < 1)
			set_Value (COLUMNNAME_AMN_CommissionGroup_ID, null);
		else
			set_Value (COLUMNNAME_AMN_CommissionGroup_ID, Integer.valueOf(AMN_CommissionGroup_ID));
	}

	/** Get Commission Group.
		@return Payroll Employee commission Group OrgSector
	  */
	public int getAMN_CommissionGroup_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_CommissionGroup_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Contract.
		@param AMN_Contract_ID Payroll Contract
	*/
	public void setAMN_Contract_ID (int AMN_Contract_ID)
	{
		if (AMN_Contract_ID < 1)
			set_Value (COLUMNNAME_AMN_Contract_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Contract_ID, Integer.valueOf(AMN_Contract_ID));
	}

	/** Get Payroll Contract.
		@return Payroll Contract	  */
	public int getAMN_Contract_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Department.
		@param AMN_Department_ID Payroll Department
	*/
	public void setAMN_Department_ID (int AMN_Department_ID)
	{
		if (AMN_Department_ID < 1)
			set_Value (COLUMNNAME_AMN_Department_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Department_ID, Integer.valueOf(AMN_Department_ID));
	}

	/** Get Payroll Department.
		@return Payroll Department	  */
	public int getAMN_Department_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll employee.
		@param AMN_Employee_ID Payroll employee
	*/
	public void setAMN_Employee_ID (int AMN_Employee_ID)
	{
		if (AMN_Employee_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
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

	/** Set AMN_Employee_UU.
		@param AMN_Employee_UU AMN_Employee_UU
	*/
	public void setAMN_Employee_UU (String AMN_Employee_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Employee_UU, AMN_Employee_UU);
	}

	/** Get AMN_Employee_UU.
		@return AMN_Employee_UU	  */
	public String getAMN_Employee_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Employee_UU);
	}

	public I_AMN_Jobstation getAMN_Jobstation() throws RuntimeException
	{
		return (I_AMN_Jobstation)MTable.get(getCtx(), I_AMN_Jobstation.Table_ID)
			.getPO(getAMN_Jobstation_ID(), get_TrxName());
	}

	/** Set Payroll Job Station.
		@param AMN_Jobstation_ID Payroll Job Station
	*/
	public void setAMN_Jobstation_ID (int AMN_Jobstation_ID)
	{
		if (AMN_Jobstation_ID < 1)
			set_Value (COLUMNNAME_AMN_Jobstation_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Jobstation_ID, Integer.valueOf(AMN_Jobstation_ID));
	}

	/** Get Payroll Job Station.
		@return Payroll Job Station	  */
	public int getAMN_Jobstation_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobstation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_Jobtitle getAMN_Jobtitle() throws RuntimeException
	{
		return (I_AMN_Jobtitle)MTable.get(getCtx(), I_AMN_Jobtitle.Table_ID)
			.getPO(getAMN_Jobtitle_ID(), get_TrxName());
	}

	/** Set Payroll Job Title.
		@param AMN_Jobtitle_ID Payroll Job Title
	*/
	public void setAMN_Jobtitle_ID (int AMN_Jobtitle_ID)
	{
		if (AMN_Jobtitle_ID < 1)
			set_Value (COLUMNNAME_AMN_Jobtitle_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Jobtitle_ID, Integer.valueOf(AMN_Jobtitle_ID));
	}

	/** Get Payroll Job Title.
		@return Payroll Job Title	  */
	public int getAMN_Jobtitle_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobtitle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Location.
		@param AMN_Location_ID Payroll Location
	*/
	public void setAMN_Location_ID (int AMN_Location_ID)
	{
		if (AMN_Location_ID < 1)
			set_Value (COLUMNNAME_AMN_Location_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Location_ID, Integer.valueOf(AMN_Location_ID));
	}

	/** Get Payroll Location.
		@return Payroll Location	  */
	public int getAMN_Location_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Position.
		@param AMN_Position_ID Payroll Position
	*/
	public void setAMN_Position_ID (int AMN_Position_ID)
	{
		if (AMN_Position_ID < 1)
			set_Value (COLUMNNAME_AMN_Position_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Position_ID, Integer.valueOf(AMN_Position_ID));
	}

	/** Get Payroll Position.
		@return Payroll Position	  */
	public int getAMN_Position_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Position_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_Sector getAMN_Sector() throws RuntimeException
	{
		return (I_AMN_Sector)MTable.get(getCtx(), I_AMN_Sector.Table_ID)
			.getPO(getAMN_Sector_ID(), get_TrxName());
	}

	/** Set Work Sector .
		@param AMN_Sector_ID Work Sector in Location
	*/
	public void setAMN_Sector_ID (int AMN_Sector_ID)
	{
		if (AMN_Sector_ID < 1)
			set_Value (COLUMNNAME_AMN_Sector_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Sector_ID, Integer.valueOf(AMN_Sector_ID));
	}

	/** Get Work Sector .
		@return Work Sector in Location
	  */
	public int getAMN_Sector_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Sector_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Shift.
		@param AMN_Shift_ID Shift
	*/
	public void setAMN_Shift_ID (int AMN_Shift_ID)
	{
		if (AMN_Shift_ID < 1)
			set_Value (COLUMNNAME_AMN_Shift_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Shift_ID, Integer.valueOf(AMN_Shift_ID));
	}

	/** Get Shift.
		@return Shift	  */
	public int getAMN_Shift_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Shift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Alergic.
		@param Alergic Alergic to chemicals ór Medicament
	*/
	public void setAlergic (String Alergic)
	{
		set_Value (COLUMNNAME_Alergic, Alergic);
	}

	/** Get Alergic.
		@return Alergic to chemicals ór Medicament
	  */
	public String getAlergic()
	{
		return (String)get_Value(COLUMNNAME_Alergic);
	}

	public org.compiere.model.I_C_BPartner getBill_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getBill_BPartner_ID(), get_TrxName());
	}

	/** Set Invoice Partner.
		@param Bill_BPartner_ID Business Partner to be invoiced
	*/
	public void setBill_BPartner_ID (int Bill_BPartner_ID)
	{
		if (Bill_BPartner_ID < 1)
			set_Value (COLUMNNAME_Bill_BPartner_ID, null);
		else
			set_Value (COLUMNNAME_Bill_BPartner_ID, Integer.valueOf(Bill_BPartner_ID));
	}

	/** Get Invoice Partner.
		@return Business Partner to be invoiced
	  */
	public int getBill_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Bill_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BioCode.
		@param BioCode Biometric Code from Attendance Control Scanners
	*/
	public void setBioCode (String BioCode)
	{
		set_Value (COLUMNNAME_BioCode, BioCode);
	}

	/** Get BioCode.
		@return Biometric Code from Attendance Control Scanners
	  */
	public String getBioCode()
	{
		return (String)get_Value(COLUMNNAME_BioCode);
	}

	/** Set Birthday.
		@param Birthday Birthday or Anniversary day
	*/
	public void setBirthday (Timestamp Birthday)
	{
		set_Value (COLUMNNAME_Birthday, Birthday);
	}

	/** Get Birthday.
		@return Birthday or Anniversary day
	  */
	public Timestamp getBirthday()
	{
		return (Timestamp)get_Value(COLUMNNAME_Birthday);
	}

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException
	{
		return (org.compiere.model.I_C_Activity)MTable.get(getCtx(), org.compiere.model.I_C_Activity.Table_ID)
			.getPO(getC_Activity_ID(), get_TrxName());
	}

	/** Set Activity.
		@param C_Activity_ID Business Activity
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
	public int getC_Activity_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Activity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner.
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException
	{
		return (org.compiere.model.I_C_Campaign)MTable.get(getCtx(), org.compiere.model.I_C_Campaign.Table_ID)
			.getPO(getC_Campaign_ID(), get_TrxName());
	}

	/** Set Campaign.
		@param C_Campaign_ID Marketing Campaign
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
	public int getC_Campaign_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Campaign_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_CONSOLIDATIONREFERENCE_ID.
		@param C_ConsolidationReference_ID C_CONSOLIDATIONREFERENCE_ID
	*/
	public void setC_ConsolidationReference_ID (int C_ConsolidationReference_ID)
	{
		if (C_ConsolidationReference_ID < 1)
			set_Value (COLUMNNAME_C_ConsolidationReference_ID, null);
		else
			set_Value (COLUMNNAME_C_ConsolidationReference_ID, Integer.valueOf(C_ConsolidationReference_ID));
	}

	/** Get C_CONSOLIDATIONREFERENCE_ID.
		@return C_CONSOLIDATIONREFERENCE_ID	  */
	public int getC_ConsolidationReference_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConsolidationReference_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Country getC_Country() throws RuntimeException
	{
		return (org.compiere.model.I_C_Country)MTable.get(getCtx(), org.compiere.model.I_C_Country.Table_ID)
			.getPO(getC_Country_ID(), get_TrxName());
	}

	/** Set Country.
		@param C_Country_ID Country 
	*/
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID < 1)
			set_Value (COLUMNNAME_C_Country_ID, null);
		else
			set_Value (COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
	}

	/** Get Country.
		@return Country 
	  */
	public int getC_Country_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
	{
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_ID)
			.getPO(getC_Currency_ID(), get_TrxName());
	}

	/** Set Currency.
		@param C_Currency_ID The Currency for this record
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
	public int getC_Currency_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_JOBSHIFT_ID.
		@param C_Jobshift_ID C_JOBSHIFT_ID
	*/
	public void setC_Jobshift_ID (int C_Jobshift_ID)
	{
		if (C_Jobshift_ID < 1)
			set_Value (COLUMNNAME_C_Jobshift_ID, null);
		else
			set_Value (COLUMNNAME_C_Jobshift_ID, Integer.valueOf(C_Jobshift_ID));
	}

	/** Get C_JOBSHIFT_ID.
		@return C_JOBSHIFT_ID	  */
	public int getC_Jobshift_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Jobshift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
	{
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_ID)
			.getPO(getC_Project_ID(), get_TrxName());
	}

	/** Set Project.
		@param C_Project_ID Financial Project
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
	public int getC_Project_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException
	{
		return (org.compiere.model.I_C_SalesRegion)MTable.get(getCtx(), org.compiere.model.I_C_SalesRegion.Table_ID)
			.getPO(getC_SalesRegion_ID(), get_TrxName());
	}

	/** Set Sales Region.
		@param C_SalesRegion_ID Sales coverage region
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
	public int getC_SalesRegion_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_SalesRegion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set COMMISSIONSGROUP.
		@param CommissionsGroup COMMISSIONSGROUP
	*/
	public void setCommissionsGroup (String CommissionsGroup)
	{
		set_Value (COLUMNNAME_CommissionsGroup, CommissionsGroup);
	}

	/** Get COMMISSIONSGROUP.
		@return COMMISSIONSGROUP	  */
	public String getCommissionsGroup()
	{
		return (String)get_Value(COLUMNNAME_CommissionsGroup);
	}

	/** Set Nacionality.
		@param CountryofNacionality_ID Nacionality
	*/
	public void setCountryofNacionality_ID (int CountryofNacionality_ID)
	{
		if (CountryofNacionality_ID < 1)
			set_Value (COLUMNNAME_CountryofNacionality_ID, null);
		else
			set_Value (COLUMNNAME_CountryofNacionality_ID, Integer.valueOf(CountryofNacionality_ID));
	}

	/** Get Nacionality.
		@return Nacionality	  */
	public int getCountryofNacionality_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_CountryofNacionality_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Deseases.
		@param Deseases Deseases affected for person
	*/
	public void setDeseases (String Deseases)
	{
		set_Value (COLUMNNAME_Deseases, Deseases);
	}

	/** Get Deseases.
		@return Deseases affected for person
	  */
	public String getDeseases()
	{
		return (String)get_Value(COLUMNNAME_Deseases);
	}

	/** Set EMail Address.
		@param EMail Electronic Mail Address
	*/
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail()
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set EMail Address 2.
		@param EMail2 Electronic Mail Address Corporative
	*/
	public void setEMail2 (String EMail2)
	{
		set_Value (COLUMNNAME_EMail2, EMail2);
	}

	/** Get EMail Address 2.
		@return Electronic Mail Address Corporative
	  */
	public String getEMail2()
	{
		return (String)get_Value(COLUMNNAME_EMail2);
	}

	/** Set First Name 1.
		@param FirstName1 First Name 1
	*/
	public void setFirstName1 (String FirstName1)
	{
		set_Value (COLUMNNAME_FirstName1, FirstName1);
	}

	/** Get First Name 1.
		@return First Name 1	  */
	public String getFirstName1()
	{
		return (String)get_Value(COLUMNNAME_FirstName1);
	}

	/** Set First Name 2.
		@param FirstName2 First Name 2
	*/
	public void setFirstName2 (String FirstName2)
	{
		set_Value (COLUMNNAME_FirstName2, FirstName2);
	}

	/** Get First Name 2.
		@return First Name 2	  */
	public String getFirstName2()
	{
		return (String)get_Value(COLUMNNAME_FirstName2);
	}

	/** Both Hands = B */
	public static final String HANDUSE_BothHands = "B";
	/** Left = L */
	public static final String HANDUSE_Left = "L";
	/** Right = R */
	public static final String HANDUSE_Right = "R";
	/** Set HandUse.
		@param HandUse Indicates if person uses Left hand, right hand or both 
	*/
	public void setHandUse (String HandUse)
	{

		set_Value (COLUMNNAME_HandUse, HandUse);
	}

	/** Get HandUse.
		@return Indicates if person uses Left hand, right hand or both 
	  */
	public String getHandUse()
	{
		return (String)get_Value(COLUMNNAME_HandUse);
	}

	/** Set Height.
		@param Height Height
	*/
	public void setHeight (BigDecimal Height)
	{
		set_Value (COLUMNNAME_Height, Height);
	}

	/** Get Height.
		@return Height	  */
	public BigDecimal getHeight()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Height);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Hobbyes.
		@param Hobbyes Hobbyes practicated 
	*/
	public void setHobbyes (String Hobbyes)
	{
		set_Value (COLUMNNAME_Hobbyes, Hobbyes);
	}

	/** Get Hobbyes.
		@return Hobbyes practicated 
	  */
	public String getHobbyes()
	{
		return (String)get_Value(COLUMNNAME_Hobbyes);
	}

	/** Set IDCardAux.
		@param IDCardAux IDCardAux
	*/
	public void setIDCardAux (String IDCardAux)
	{
		set_Value (COLUMNNAME_IDCardAux, IDCardAux);
	}

	/** Get IDCardAux.
		@return IDCardAux	  */
	public String getIDCardAux()
	{
		return (String)get_Value(COLUMNNAME_IDCardAux);
	}

	/** Set IDCardNo.
		@param IDCardNo IDCardNo
	*/
	public void setIDCardNo (String IDCardNo)
	{
		set_Value (COLUMNNAME_IDCardNo, IDCardNo);
	}

	/** Get IDCardNo.
		@return IDCardNo	  */
	public String getIDCardNo()
	{
		return (String)get_Value(COLUMNNAME_IDCardNo);
	}

	/** Set IDNumber.
		@param IDNumber IDNumber
	*/
	public void setIDNumber (String IDNumber)
	{
		set_Value (COLUMNNAME_IDNumber, IDNumber);
	}

	/** Get IDNumber.
		@return IDNumber	  */
	public String getIDNumber()
	{
		return (String)get_Value(COLUMNNAME_IDNumber);
	}

	/** Set IDType.
		@param IDType IDType
	*/
	public void setIDType (String IDType)
	{
		set_Value (COLUMNNAME_IDType, IDType);
	}

	/** Get IDType.
		@return IDType	  */
	public String getIDType()
	{
		return (String)get_Value(COLUMNNAME_IDType);
	}

	/** Set Detailed Names.
		@param IsDetailedNames Detailed Names
	*/
	public void setIsDetailedNames (boolean IsDetailedNames)
	{
		set_Value (COLUMNNAME_IsDetailedNames, Boolean.valueOf(IsDetailedNames));
	}

	/** Get Detailed Names.
		@return Detailed Names	  */
	public boolean isDetailedNames()
	{
		Object oo = get_Value(COLUMNNAME_IsDetailedNames);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Last Name 1.
		@param LastName1 Last Name 1
	*/
	public void setLastName1 (String LastName1)
	{
		set_Value (COLUMNNAME_LastName1, LastName1);
	}

	/** Get Last Name 1.
		@return Last Name 1	  */
	public String getLastName1()
	{
		return (String)get_Value(COLUMNNAME_LastName1);
	}

	/** Set Last Name 2.
		@param LastName2 Last Name 2
	*/
	public void setLastName2 (String LastName2)
	{
		set_Value (COLUMNNAME_LastName2, LastName2);
	}

	/** Get Last Name 2.
		@return Last Name 2	  */
	public String getLastName2()
	{
		return (String)get_Value(COLUMNNAME_LastName2);
	}

	/** Set NAME_IDCARD.
		@param NAME_IDCARD NAME_IDCARD
	*/
	public void setNAME_IDCARD (String NAME_IDCARD)
	{
		set_Value (COLUMNNAME_NAME_IDCARD, NAME_IDCARD);
	}

	/** Get NAME_IDCARD.
		@return NAME_IDCARD	  */
	public String getNAME_IDCARD()
	{
		return (String)get_Value(COLUMNNAME_NAME_IDCARD);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Central Office - Group A = OCAA */
	public static final String ORGSECTOR_CentralOffice_GroupA = "OCAA";
	/** Central Office - Group B = OCBA */
	public static final String ORGSECTOR_CentralOffice_GroupB = "OCBA";
	/** Central Office - Group C = OCCA */
	public static final String ORGSECTOR_CentralOffice_GroupC = "OCCA";
	/** Set OrgSector.
		@param OrgSector OrgSector
	*/
	public void setOrgSector (String OrgSector)
	{

		set_Value (COLUMNNAME_OrgSector, OrgSector);
	}

	/** Get OrgSector.
		@return OrgSector	  */
	public String getOrgSector()
	{
		return (String)get_Value(COLUMNNAME_OrgSector);
	}

	/** Set PIN.
		@param PIN Personal Identification Number
	*/
	public void setPIN (String PIN)
	{
		set_Value (COLUMNNAME_PIN, PIN);
	}

	/** Get PIN.
		@return Personal Identification Number
	  */
	public String getPIN()
	{
		return (String)get_Value(COLUMNNAME_PIN);
	}

	/** Set Salary.
		@param Salary Salary
	*/
	public void setSalary (BigDecimal Salary)
	{
		set_Value (COLUMNNAME_Salary, Salary);
	}

	/** Get Salary.
		@return Salary	  */
	public BigDecimal getSalary()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Salary);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Size Pant.
		@param SizePant Pant size for Person
	*/
	public void setSizePant (String SizePant)
	{
		set_Value (COLUMNNAME_SizePant, SizePant);
	}

	/** Get Size Pant.
		@return Pant size for Person
	  */
	public String getSizePant()
	{
		return (String)get_Value(COLUMNNAME_SizePant);
	}

	/** Set Size Shirt.
		@param SizeShirt Size Shirt for Person
	*/
	public void setSizeShirt (String SizeShirt)
	{
		set_Value (COLUMNNAME_SizeShirt, SizeShirt);
	}

	/** Get Size Shirt.
		@return Size Shirt for Person
	  */
	public String getSizeShirt()
	{
		return (String)get_Value(COLUMNNAME_SizeShirt);
	}

	/** Set SizeShoe.
		@param SizeShoe Shoe size for Person
	*/
	public void setSizeShoe (String SizeShoe)
	{
		set_Value (COLUMNNAME_SizeShoe, SizeShoe);
	}

	/** Get SizeShoe.
		@return Shoe size for Person
	  */
	public String getSizeShoe()
	{
		return (String)get_Value(COLUMNNAME_SizeShoe);
	}

	/** Set Sports.
		@param Sports Sports practicated
	*/
	public void setSports (String Sports)
	{
		set_Value (COLUMNNAME_Sports, Sports);
	}

	/** Get Sports.
		@return Sports practicated
	  */
	public String getSports()
	{
		return (String)get_Value(COLUMNNAME_Sports);
	}

	/** Active = A */
	public static final String STATUS_Active = "A";
	/** Retired = R */
	public static final String STATUS_Retired = "R";
	/** Suspended = S */
	public static final String STATUS_Suspended = "S";
	/** Vacations = V */
	public static final String STATUS_Vacations = "V";
	/** Set Status.
		@param Status Status of the currently running check
	*/
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus()
	{
		return (String)get_Value(COLUMNNAME_Status);
	}

	/** Set Use Lenses.
		@param UseLenses Indicates if person uses lenses
	*/
	public void setUseLenses (boolean UseLenses)
	{
		set_Value (COLUMNNAME_UseLenses, Boolean.valueOf(UseLenses));
	}

	/** Get Use Lenses.
		@return Indicates if person uses lenses
	  */
	public boolean isUseLenses()
	{
		Object oo = get_Value(COLUMNNAME_UseLenses);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set Weight.
		@param Weight Weight of a product
	*/
	public void setWeight (BigDecimal Weight)
	{
		set_Value (COLUMNNAME_Weight, Weight);
	}

	/** Get Weight.
		@return Weight of a product
	  */
	public BigDecimal getWeight()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Weight);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set birthplace.
		@param birthplace birthplace
	*/
	public void setbirthplace (String birthplace)
	{
		set_Value (COLUMNNAME_birthplace, birthplace);
	}

	/** Get birthplace.
		@return birthplace	  */
	public String getbirthplace()
	{
		return (String)get_Value(COLUMNNAME_birthplace);
	}

	/** RH (+) Positive = + */
	public static final String BLOODRH_RHPlusPositive = "+";
	/** RH (-) Negative = - */
	public static final String BLOODRH_RH_Negative = "-";
	/** Set bloodrh.
		@param bloodrh bloodrh
	*/
	public void setbloodrh (String bloodrh)
	{

		set_Value (COLUMNNAME_bloodrh, bloodrh);
	}

	/** Get bloodrh.
		@return bloodrh	  */
	public String getbloodrh()
	{
		return (String)get_Value(COLUMNNAME_bloodrh);
	}

	/** A Type = A  */
	public static final String BLOODTYPE_AType = "A ";
	/** AB Type = AB */
	public static final String BLOODTYPE_ABType = "AB";
	/** B Type = B  */
	public static final String BLOODTYPE_BType = "B ";
	/** OType = O  */
	public static final String BLOODTYPE_OType = "O ";
	/** Set bloodtype.
		@param bloodtype bloodtype
	*/
	public void setbloodtype (String bloodtype)
	{

		set_Value (COLUMNNAME_bloodtype, bloodtype);
	}

	/** Get bloodtype.
		@return bloodtype	  */
	public String getbloodtype()
	{
		return (String)get_Value(COLUMNNAME_bloodtype);
	}

	/** Divorced = D */
	public static final String CIVILSTATUS_Divorced = "D";
	/** Married = M */
	public static final String CIVILSTATUS_Married = "M";
	/** Other = O */
	public static final String CIVILSTATUS_Other = "O";
	/** Singled = S */
	public static final String CIVILSTATUS_Singled = "S";
	/** Widowed = W */
	public static final String CIVILSTATUS_Widowed = "W";
	/** Set civilstatus.
		@param civilstatus civilstatus
	*/
	public void setcivilstatus (String civilstatus)
	{

		set_Value (COLUMNNAME_civilstatus, civilstatus);
	}

	/** Get civilstatus.
		@return civilstatus	  */
	public String getcivilstatus()
	{
		return (String)get_Value(COLUMNNAME_civilstatus);
	}

	/** Set downwardloads.
		@param downwardloads downwardloads
	*/
	public void setdownwardloads (BigDecimal downwardloads)
	{
		set_Value (COLUMNNAME_downwardloads, downwardloads);
	}

	/** Get downwardloads.
		@return downwardloads	  */
	public BigDecimal getdownwardloads()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_downwardloads);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Bachellor = B */
	public static final String EDUCATIONGRADE_Bachellor = "B";
	/** University Graduate = E */
	public static final String EDUCATIONGRADE_UniversityGraduate = "E";
	/** Elementary = P */
	public static final String EDUCATIONGRADE_Elementary = "P";
	/** Technical Superior = S */
	public static final String EDUCATIONGRADE_TechnicalSuperior = "S";
	/** Technical = T */
	public static final String EDUCATIONGRADE_Technical = "T";
	/** University = U */
	public static final String EDUCATIONGRADE_University = "U";
	/** Post Graduated = X */
	public static final String EDUCATIONGRADE_PostGraduated = "X";
	/** Set educationgrade.
		@param educationgrade educationgrade
	*/
	public void seteducationgrade (String educationgrade)
	{

		set_Value (COLUMNNAME_educationgrade, educationgrade);
	}

	/** Get educationgrade.
		@return educationgrade	  */
	public String geteducationgrade()
	{
		return (String)get_Value(COLUMNNAME_educationgrade);
	}

	/** Set educationlevel.
		@param educationlevel educationlevel
	*/
	public void seteducationlevel (String educationlevel)
	{
		set_Value (COLUMNNAME_educationlevel, educationlevel);
	}

	/** Get educationlevel.
		@return educationlevel	  */
	public String geteducationlevel()
	{
		return (String)get_Value(COLUMNNAME_educationlevel);
	}

	/** Dismissed = D */
	public static final String EGRESSCONDITION_Dismissed = "D";
	/** Deceased = F */
	public static final String EGRESSCONDITION_Deceased = "F";
	/** Retired = J */
	public static final String EGRESSCONDITION_Retired = "J";
	/** None = N */
	public static final String EGRESSCONDITION_None = "N";
	/** Pensioned = P */
	public static final String EGRESSCONDITION_Pensioned = "P";
	/** Resignation = R */
	public static final String EGRESSCONDITION_Resignation = "R";
	/** Transfer = T */
	public static final String EGRESSCONDITION_Transfer = "T";
	/** Set egresscondition.
		@param egresscondition egresscondition
	*/
	public void setegresscondition (String egresscondition)
	{

		set_Value (COLUMNNAME_egresscondition, egresscondition);
	}

	/** Get egresscondition.
		@return egresscondition	  */
	public String getegresscondition()
	{
		return (String)get_Value(COLUMNNAME_egresscondition);
	}

	/** Set egressdate.
		@param egressdate egressdate
	*/
	public void setegressdate (Timestamp egressdate)
	{
		set_Value (COLUMNNAME_egressdate, egressdate);
	}

	/** Get egressdate.
		@return egressdate	  */
	public Timestamp getegressdate()
	{
		return (Timestamp)get_Value(COLUMNNAME_egressdate);
	}

	/** Set Employee Image 1.
		@param empimg1_ID Employee Image 1
	*/
	public void setempimg1_ID (int empimg1_ID)
	{
		if (empimg1_ID < 1)
			set_Value (COLUMNNAME_empimg1_ID, null);
		else
			set_Value (COLUMNNAME_empimg1_ID, Integer.valueOf(empimg1_ID));
	}

	/** Get Employee Image 1.
		@return Employee Image 1
	  */
	public int getempimg1_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_empimg1_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Employee Image 2.
		@param empimg2_ID Employee Image 2
	*/
	public void setempimg2_ID (int empimg2_ID)
	{
		if (empimg2_ID < 1)
			set_Value (COLUMNNAME_empimg2_ID, null);
		else
			set_Value (COLUMNNAME_empimg2_ID, Integer.valueOf(empimg2_ID));
	}

	/** Get Employee Image 2.
		@return Employee Image 2
	  */
	public int getempimg2_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_empimg2_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set incomedate.
		@param incomedate incomedate
	*/
	public void setincomedate (Timestamp incomedate)
	{
		set_Value (COLUMNNAME_incomedate, incomedate);
	}

	/** Get incomedate.
		@return incomedate	  */
	public Timestamp getincomedate()
	{
		return (Timestamp)get_Value(COLUMNNAME_incomedate);
	}

	/** Set increasingloads.
		@param increasingloads increasingloads
	*/
	public void setincreasingloads (BigDecimal increasingloads)
	{
		set_Value (COLUMNNAME_increasingloads, increasingloads);
	}

	/** Get increasingloads.
		@return increasingloads	  */
	public BigDecimal getincreasingloads()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_increasingloads);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Is Medicated.
		@param isMedicated Indicates if person is Medicated
	*/
	public void setisMedicated (boolean isMedicated)
	{
		set_Value (COLUMNNAME_isMedicated, Boolean.valueOf(isMedicated));
	}

	/** Get Is Medicated.
		@return Indicates if person is Medicated
	  */
	public boolean isMedicated()
	{
		Object oo = get_Value(COLUMNNAME_isMedicated);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Is Pensioned.
		@param isPensioned Indicates if employee is Pensioned by Social Security
	*/
	public void setisPensioned (boolean isPensioned)
	{
		set_Value (COLUMNNAME_isPensioned, Boolean.valueOf(isPensioned));
	}

	/** Get Is Pensioned.
		@return Indicates if employee is Pensioned by Social Security
	  */
	public boolean isPensioned()
	{
		Object oo = get_Value(COLUMNNAME_isPensioned);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set isStudying.
		@param isStudying Indicates if is studying
	*/
	public void setisStudying (boolean isStudying)
	{
		set_Value (COLUMNNAME_isStudying, Boolean.valueOf(isStudying));
	}

	/** Get isStudying.
		@return Indicates if is studying
	  */
	public boolean isStudying()
	{
		Object oo = get_Value(COLUMNNAME_isStudying);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Temporary Contract = C */
	public static final String JOBCONDITION_TemporaryContract = "C";
	/** Fixed Contract = F */
	public static final String JOBCONDITION_FixedContract = "F";
	/** FreeLance = I */
	public static final String JOBCONDITION_FreeLance = "I";
	/** Part Time = P */
	public static final String JOBCONDITION_PartTime = "P";
	/** Set jobcondition.
		@param jobcondition jobcondition
	*/
	public void setjobcondition (String jobcondition)
	{

		set_Value (COLUMNNAME_jobcondition, jobcondition);
	}

	/** Get jobcondition.
		@return jobcondition	  */
	public String getjobcondition()
	{
		return (String)get_Value(COLUMNNAME_jobcondition);
	}

	/** Check = C */
	public static final String PAYMENTTYPE_Check = "C";
	/** Deposit = D */
	public static final String PAYMENTTYPE_Deposit = "D";
	/** Cash = E */
	public static final String PAYMENTTYPE_Cash = "E";
	/** Other = O */
	public static final String PAYMENTTYPE_Other = "O";
	/** Set paymenttype.
		@param paymenttype paymenttype
	*/
	public void setpaymenttype (String paymenttype)
	{

		set_Value (COLUMNNAME_paymenttype, paymenttype);
	}

	/** Get paymenttype.
		@return paymenttype	  */
	public String getpaymenttype()
	{
		return (String)get_Value(COLUMNNAME_paymenttype);
	}

	/** Assistance = A */
	public static final String PAYROLLMODE_Assistance = "A";
	/** Both = B */
	public static final String PAYROLLMODE_Both = "B";
	/** Hours = H */
	public static final String PAYROLLMODE_Hours = "H";
	/** Standard = S */
	public static final String PAYROLLMODE_Standard = "S";
	/** Set payrollmode.
		@param payrollmode payrollmode
	*/
	public void setpayrollmode (String payrollmode)
	{

		set_Value (COLUMNNAME_payrollmode, payrollmode);
	}

	/** Get payrollmode.
		@return payrollmode	  */
	public String getpayrollmode()
	{
		return (String)get_Value(COLUMNNAME_payrollmode);
	}

	/** Private Hospital Insurance = H */
	public static final String PRIVATEASSIST_PrivateHospitalInsurance = "H";
	/** None = N */
	public static final String PRIVATEASSIST_None = "N";
	/** Set privateassist.
		@param privateassist privateassist
	*/
	public void setprivateassist (String privateassist)
	{

		set_Value (COLUMNNAME_privateassist, privateassist);
	}

	/** Get privateassist.
		@return privateassist	  */
	public String getprivateassist()
	{
		return (String)get_Value(COLUMNNAME_privateassist);
	}

	/** Set profession.
		@param profession profession
	*/
	public void setprofession (String profession)
	{
		set_Value (COLUMNNAME_profession, profession);
	}

	/** Get profession.
		@return profession	  */
	public String getprofession()
	{
		return (String)get_Value(COLUMNNAME_profession);
	}

	/** Female = F */
	public static final String SEX_Female = "F";
	/** Male = M */
	public static final String SEX_Male = "M";
	/** Set Sex at Birth.
		@param sex Sex at Birth
	*/
	public void setsex (String sex)
	{

		set_Value (COLUMNNAME_sex, sex);
	}

	/** Get Sex at Birth.
		@return Sex at Birth	  */
	public String getsex()
	{
		return (String)get_Value(COLUMNNAME_sex);
	}

	/** No = N */
	public static final String SPOUSE_No = "N";
	/** Yes = Y */
	public static final String SPOUSE_Yes = "Y";
	/** Set spouse.
		@param spouse spouse
	*/
	public void setspouse (String spouse)
	{

		set_Value (COLUMNNAME_spouse, spouse);
	}

	/** Get spouse.
		@return spouse	  */
	public String getspouse()
	{
		return (String)get_Value(COLUMNNAME_spouse);
	}

	/** Set zodiacsign.
		@param zodiacsign zodiacsign
	*/
	public void setzodiacsign (String zodiacsign)
	{
		set_Value (COLUMNNAME_zodiacsign, zodiacsign);
	}

	/** Get zodiacsign.
		@return zodiacsign	  */
	public String getzodiacsign()
	{
		return (String)get_Value(COLUMNNAME_zodiacsign);
	}
}
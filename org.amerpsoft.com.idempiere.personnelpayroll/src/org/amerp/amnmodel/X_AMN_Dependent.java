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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for AMN_Dependent
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Dependent extends PO implements I_AMN_Dependent, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Dependent (Properties ctx, int AMN_Dependent_ID, String trxName)
    {
      super (ctx, AMN_Dependent_ID, trxName);
      /** if (AMN_Dependent_ID == 0)
        {
			setAMN_Dependent_ID (0);
			setAMN_Dependent_type_ID (0);
			setBirthday (new Timestamp( System.currentTimeMillis() ));
			setC_BPartner_ID (0);
			setName (null);
			setTaxID (null);
			setValue (null);
			setsex (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Dependent (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Dependent[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Employee Dependent.
		@param AMN_Dependent_ID Employee Dependent	  */
	public void setAMN_Dependent_ID (int AMN_Dependent_ID)
	{
		if (AMN_Dependent_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Dependent_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Dependent_ID, Integer.valueOf(AMN_Dependent_ID));
	}

	/** Get Employee Dependent.
		@return Employee Dependent	  */
	public int getAMN_Dependent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Dependent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Dependent_UU.
		@param AMN_Dependent_UU AMN_Dependent_UU	  */
	public void setAMN_Dependent_UU (String AMN_Dependent_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Dependent_UU, AMN_Dependent_UU);
	}

	/** Get AMN_Dependent_UU.
		@return AMN_Dependent_UU	  */
	public String getAMN_Dependent_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Dependent_UU);
	}

	public I_AMN_Dependent_type getAMN_Dependent_type() throws RuntimeException
    {
		return (I_AMN_Dependent_type)MTable.get(getCtx(), I_AMN_Dependent_type.Table_Name)
			.getPO(getAMN_Dependent_type_ID(), get_TrxName());	}

	/** Set Employee Dependent Type.
		@param AMN_Dependent_type_ID Employee Dependent Type	  */
	public void setAMN_Dependent_type_ID (int AMN_Dependent_type_ID)
	{
		if (AMN_Dependent_type_ID < 1) 
			set_Value (COLUMNNAME_AMN_Dependent_type_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Dependent_type_ID, Integer.valueOf(AMN_Dependent_type_ID));
	}

	/** Get Employee Dependent Type.
		@return Employee Dependent Type	  */
	public int getAMN_Dependent_type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Dependent_type_ID);
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

	/** Set Birthday.
		@param Birthday 
		Birthday or Anniversary day
	  */
	public void setBirthday (Timestamp Birthday)
	{
		set_Value (COLUMNNAME_Birthday, Birthday);
	}

	/** Get Birthday.
		@return Birthday or Anniversary day
	  */
	public Timestamp getBirthday () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Birthday);
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner_Location getC_BPartner_Location() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner_Location)MTable.get(getCtx(), org.compiere.model.I_C_BPartner_Location.Table_Name)
			.getPO(getC_BPartner_Location_ID(), get_TrxName());	}

	/** Set Partner Location.
		@param C_BPartner_Location_ID 
		Identifies the (ship to) address for this Business Partner
	  */
	public void setC_BPartner_Location_ID (int C_BPartner_Location_ID)
	{
		if (C_BPartner_Location_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_Location_ID, Integer.valueOf(C_BPartner_Location_ID));
	}

	/** Get Partner Location.
		@return Identifies the (ship to) address for this Business Partner
	  */
	public int getC_BPartner_Location_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_Location_ID);
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

	/** Set EMail Address.
		@param EMail 
		Electronic Mail Address
	  */
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail () 
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set Fax.
		@param Fax 
		Facsimile number
	  */
	public void setFax (String Fax)
	{
		set_Value (COLUMNNAME_Fax, Fax);
	}

	/** Get Fax.
		@return Facsimile number
	  */
	public String getFax () 
	{
		return (String)get_Value(COLUMNNAME_Fax);
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

	/** NotificationType AD_Reference_ID=344 */
	public static final int NOTIFICATIONTYPE_AD_Reference_ID=344;
	/** EMail = E */
	public static final String NOTIFICATIONTYPE_EMail = "E";
	/** Notice = N */
	public static final String NOTIFICATIONTYPE_Notice = "N";
	/** None = X */
	public static final String NOTIFICATIONTYPE_None = "X";
	/** EMail+Notice = B */
	public static final String NOTIFICATIONTYPE_EMailPlusNotice = "B";
	/** Set Notification Type.
		@param NotificationType 
		Type of Notifications
	  */
	public void setNotificationType (String NotificationType)
	{

		set_Value (COLUMNNAME_NotificationType, NotificationType);
	}

	/** Get Notification Type.
		@return Type of Notifications
	  */
	public String getNotificationType () 
	{
		return (String)get_Value(COLUMNNAME_NotificationType);
	}

	/** Set Phone.
		@param Phone 
		Identifies a telephone number
	  */
	public void setPhone (String Phone)
	{
		set_Value (COLUMNNAME_Phone, Phone);
	}

	/** Get Phone.
		@return Identifies a telephone number
	  */
	public String getPhone () 
	{
		return (String)get_Value(COLUMNNAME_Phone);
	}

	/** Set 2nd Phone.
		@param Phone2 
		Identifies an alternate telephone number.
	  */
	public void setPhone2 (String Phone2)
	{
		set_Value (COLUMNNAME_Phone2, Phone2);
	}

	/** Get 2nd Phone.
		@return Identifies an alternate telephone number.
	  */
	public String getPhone2 () 
	{
		return (String)get_Value(COLUMNNAME_Phone2);
	}

	/** Set Tax ID.
		@param TaxID 
		Tax Identification
	  */
	public void setTaxID (String TaxID)
	{
		set_Value (COLUMNNAME_TaxID, TaxID);
	}

	/** Get Tax ID.
		@return Tax Identification
	  */
	public String getTaxID () 
	{
		return (String)get_Value(COLUMNNAME_TaxID);
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

	/** Bachellor = B */
	public static final String EDUCATIONGRADE_Bachellor = "B";
	/** Technical = T */
	public static final String EDUCATIONGRADE_Technical = "T";
	/** Technical Superior = S */
	public static final String EDUCATIONGRADE_TechnicalSuperior = "S";
	/** University = U */
	public static final String EDUCATIONGRADE_University = "U";
	/** Elementary = P */
	public static final String EDUCATIONGRADE_Elementary = "P";
	/** University Graduate = E */
	public static final String EDUCATIONGRADE_UniversityGraduate = "E";
	/** Post Graduated = X */
	public static final String EDUCATIONGRADE_PostGraduated = "X";
	/** Set educationgrade.
		@param educationgrade educationgrade	  */
	public void seteducationgrade (String educationgrade)
	{

		set_Value (COLUMNNAME_educationgrade, educationgrade);
	}

	/** Get educationgrade.
		@return educationgrade	  */
	public String geteducationgrade () 
	{
		return (String)get_Value(COLUMNNAME_educationgrade);
	}

	/** Set isStudying.
		@param isStudying 
		Indicates if is studying
	  */
	public void setisStudying (boolean isStudying)
	{
		set_Value (COLUMNNAME_isStudying, Boolean.valueOf(isStudying));
	}

	/** Get isStudying.
		@return Indicates if is studying
	  */
	public boolean isStudying () 
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

	/** Set Is Working.
		@param isWorking 
		Indicates if Person Is Working
	  */
	public void setisWorking (boolean isWorking)
	{
		set_Value (COLUMNNAME_isWorking, Boolean.valueOf(isWorking));
	}

	/** Get Is Working.
		@return Indicates if Person Is Working
	  */
	public boolean isWorking () 
	{
		Object oo = get_Value(COLUMNNAME_isWorking);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Female = F */
	public static final String SEX_Female = "F";
	/** Male = M */
	public static final String SEX_Male = "M";
	/** Set sex.
		@param sex sex	  */
	public void setsex (String sex)
	{

		set_Value (COLUMNNAME_sex, sex);
	}

	/** Get sex.
		@return sex	  */
	public String getsex () 
	{
		return (String)get_Value(COLUMNNAME_sex);
	}
}
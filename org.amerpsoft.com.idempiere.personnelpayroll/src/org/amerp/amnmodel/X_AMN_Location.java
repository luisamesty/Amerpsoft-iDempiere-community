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
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for AMN_Location
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Location")
public class X_AMN_Location extends PO implements I_AMN_Location, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250829L;

    /** Standard Constructor */
    public X_AMN_Location (Properties ctx, int AMN_Location_ID, String trxName)
    {
      super (ctx, AMN_Location_ID, trxName);
      /** if (AMN_Location_ID == 0)
        {
			setAMN_Location_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Location (Properties ctx, int AMN_Location_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Location_ID, trxName, virtualColumns);
      /** if (AMN_Location_ID == 0)
        {
			setAMN_Location_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Location (Properties ctx, String AMN_Location_UU, String trxName)
    {
      super (ctx, AMN_Location_UU, trxName);
      /** if (AMN_Location_UU == null)
        {
			setAMN_Location_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Location (Properties ctx, String AMN_Location_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Location_UU, trxName, virtualColumns);
      /** if (AMN_Location_UU == null)
        {
			setAMN_Location_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Location (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Location[")
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

	/** Set Payroll Location.
		@param AMN_Location_ID Payroll Location
	*/
	public void setAMN_Location_ID (int AMN_Location_ID)
	{
		if (AMN_Location_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Location_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Location_ID, Integer.valueOf(AMN_Location_ID));
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

	/** Set AMN_Location_UU.
		@param AMN_Location_UU AMN_Location_UU
	*/
	public void setAMN_Location_UU (String AMN_Location_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Location_UU, AMN_Location_UU);
	}

	/** Get AMN_Location_UU.
		@return AMN_Location_UU	  */
	public String getAMN_Location_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Location_UU);
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

	/** Set Fiscal Address.
		@param C_Location_FA_ID Location or Address for Fiscal purposes. Parent  organization OrgInfo address.
	*/
	public void setC_Location_FA_ID (int C_Location_FA_ID)
	{
		if (C_Location_FA_ID < 1)
			set_Value (COLUMNNAME_C_Location_FA_ID, null);
		else
			set_Value (COLUMNNAME_C_Location_FA_ID, Integer.valueOf(C_Location_FA_ID));
	}

	/** Get Fiscal Address.
		@return Location or Address for Fiscal purposes. Parent  organization OrgInfo address.
	  */
	public int getC_Location_FA_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_FA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Address.
		@param C_Location_ID Location or Address. Organization OrgInfo address.
	*/
	public void setC_Location_ID (int C_Location_ID)
	{
		if (C_Location_ID < 1)
			set_Value (COLUMNNAME_C_Location_ID, null);
		else
			set_Value (COLUMNNAME_C_Location_ID, Integer.valueOf(C_Location_ID));
	}

	/** Get Address.
		@return Location or Address. Organization OrgInfo address.
	  */
	public int getC_Location_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Social Security Address.
		@param C_Location_SS_ID Location or Address for Social Security
	*/
	public void setC_Location_SS_ID (int C_Location_SS_ID)
	{
		if (C_Location_SS_ID < 1)
			set_Value (COLUMNNAME_C_Location_SS_ID, null);
		else
			set_Value (COLUMNNAME_C_Location_SS_ID, Integer.valueOf(C_Location_SS_ID));
	}

	/** Get Social Security Address.
		@return Location or Address for Social Security
	  */
	public int getC_Location_SS_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Location_SS_ID);
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

	/** Set Organization Name.
		@param OrgName Name of the Organization
	*/
	public void setOrgName (String OrgName)
	{
		set_Value (COLUMNNAME_OrgName, OrgName);
	}

	/** Get Organization Name.
		@return Name of the Organization
	  */
	public String getOrgName()
	{
		return (String)get_Value(COLUMNNAME_OrgName);
	}

	/** Set Phone.
		@param Phone Identifies a telephone number
	*/
	public void setPhone (String Phone)
	{
		set_Value (COLUMNNAME_Phone, Phone);
	}

	/** Get Phone.
		@return Identifies a telephone number
	  */
	public String getPhone()
	{
		return (String)get_Value(COLUMNNAME_Phone);
	}

	/** Set Social Security ID.
		@param SocialSecurityID Social Security ID Number
	*/
	public void setSocialSecurityID (String SocialSecurityID)
	{
		set_Value (COLUMNNAME_SocialSecurityID, SocialSecurityID);
	}

	/** Get Social Security ID.
		@return Social Security ID Number
	  */
	public String getSocialSecurityID()
	{
		return (String)get_Value(COLUMNNAME_SocialSecurityID);
	}

	/** Set Social Security MTESS.
		@param SocialSecurityMTESS Social Security ID Number for Ministry of Labor, Employment and Social Security.
	*/
	public void setSocialSecurityMTESS (String SocialSecurityMTESS)
	{
		set_Value (COLUMNNAME_SocialSecurityMTESS, SocialSecurityMTESS);
	}

	/** Get Social Security MTESS.
		@return Social Security ID Number for Ministry of Labor, Employment and Social Security.
	  */
	public String getSocialSecurityMTESS()
	{
		return (String)get_Value(COLUMNNAME_SocialSecurityMTESS);
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
}
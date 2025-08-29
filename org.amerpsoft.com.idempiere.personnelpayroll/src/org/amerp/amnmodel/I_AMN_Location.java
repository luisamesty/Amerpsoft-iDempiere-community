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

/** Generated Interface for AMN_Location
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Location 
{

    /** TableName=AMN_Location */
    public static final String Table_Name = "AMN_Location";

    /** AD_Table_ID=1000010 */
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

    /** Column name AD_OrgTo_ID */
    public static final String COLUMNNAME_AD_OrgTo_ID = "AD_OrgTo_ID";

	/** Set Inter-Organization.
	  * Organization valid for intercompany documents
	  */
	public void setAD_OrgTo_ID (int AD_OrgTo_ID);

	/** Get Inter-Organization.
	  * Organization valid for intercompany documents
	  */
	public int getAD_OrgTo_ID();

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

    /** Column name AMN_Location_ID */
    public static final String COLUMNNAME_AMN_Location_ID = "AMN_Location_ID";

	/** Set Payroll Location	  */
	public void setAMN_Location_ID (int AMN_Location_ID);

	/** Get Payroll Location	  */
	public int getAMN_Location_ID();

    /** Column name AMN_Location_UU */
    public static final String COLUMNNAME_AMN_Location_UU = "AMN_Location_UU";

	/** Set AMN_Location_UU	  */
	public void setAMN_Location_UU (String AMN_Location_UU);

	/** Get AMN_Location_UU	  */
	public String getAMN_Location_UU();

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

    /** Column name C_Location_FA_ID */
    public static final String COLUMNNAME_C_Location_FA_ID = "C_Location_FA_ID";

	/** Set Fiscal Address.
	  * Location or Address for Fiscal purposes. Parent  organization OrgInfo address.
	  */
	public void setC_Location_FA_ID (int C_Location_FA_ID);

	/** Get Fiscal Address.
	  * Location or Address for Fiscal purposes. Parent  organization OrgInfo address.
	  */
	public int getC_Location_FA_ID();

    /** Column name C_Location_ID */
    public static final String COLUMNNAME_C_Location_ID = "C_Location_ID";

	/** Set Address.
	  * Location or Address. Organization OrgInfo address.
	  */
	public void setC_Location_ID (int C_Location_ID);

	/** Get Address.
	  * Location or Address. Organization OrgInfo address.
	  */
	public int getC_Location_ID();

    /** Column name C_Location_SS_ID */
    public static final String COLUMNNAME_C_Location_SS_ID = "C_Location_SS_ID";

	/** Set Social Security Address.
	  * Location or Address for Social Security
	  */
	public void setC_Location_SS_ID (int C_Location_SS_ID);

	/** Get Social Security Address.
	  * Location or Address for Social Security
	  */
	public int getC_Location_SS_ID();

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

    /** Column name EMail */
    public static final String COLUMNNAME_EMail = "EMail";

	/** Set EMail Address.
	  * Electronic Mail Address
	  */
	public void setEMail (String EMail);

	/** Get EMail Address.
	  * Electronic Mail Address
	  */
	public String getEMail();

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

    /** Column name OrgName */
    public static final String COLUMNNAME_OrgName = "OrgName";

	/** Set Organization Name.
	  * Name of the Organization
	  */
	public void setOrgName (String OrgName);

	/** Get Organization Name.
	  * Name of the Organization
	  */
	public String getOrgName();

    /** Column name Phone */
    public static final String COLUMNNAME_Phone = "Phone";

	/** Set Phone.
	  * Identifies a telephone number
	  */
	public void setPhone (String Phone);

	/** Get Phone.
	  * Identifies a telephone number
	  */
	public String getPhone();

    /** Column name SocialSecurityID */
    public static final String COLUMNNAME_SocialSecurityID = "SocialSecurityID";

	/** Set Social Security ID.
	  * Social Security ID Number
	  */
	public void setSocialSecurityID (String SocialSecurityID);

	/** Get Social Security ID.
	  * Social Security ID Number
	  */
	public String getSocialSecurityID();

    /** Column name SocialSecurityMTESS */
    public static final String COLUMNNAME_SocialSecurityMTESS = "SocialSecurityMTESS";

	/** Set Social Security MTESS.
	  * Social Security ID Number for Ministry of Labor, Employment and Social Security.
	  */
	public void setSocialSecurityMTESS (String SocialSecurityMTESS);

	/** Get Social Security MTESS.
	  * Social Security ID Number for Ministry of Labor, Employment and Social Security.
	  */
	public String getSocialSecurityMTESS();

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
}

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

/** Generated Interface for AMN_Leaves_Types
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Leaves_Types 
{

    /** TableName=AMN_Leaves_Types */
    public static final String Table_Name = "AMN_Leaves_Types";

    /** AD_Table_ID=1000105 */
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

    /** Column name AMN_Concept_Types_ID */
    public static final String COLUMNNAME_AMN_Concept_Types_ID = "AMN_Concept_Types_ID";

	/** Set Payroll Concepts Types	  */
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID);

	/** Get Payroll Concepts Types	  */
	public int getAMN_Concept_Types_ID();

    /** Column name AMN_Leaves_Types_ID */
    public static final String COLUMNNAME_AMN_Leaves_Types_ID = "AMN_Leaves_Types_ID";

	/** Set Leaves Types	  */
	public void setAMN_Leaves_Types_ID (int AMN_Leaves_Types_ID);

	/** Get Leaves Types	  */
	public int getAMN_Leaves_Types_ID();

    /** Column name AMN_Leaves_Types_UU */
    public static final String COLUMNNAME_AMN_Leaves_Types_UU = "AMN_Leaves_Types_UU";

	/** Set AMN_Leaves_Types_UU	  */
	public void setAMN_Leaves_Types_UU (String AMN_Leaves_Types_UU);

	/** Get AMN_Leaves_Types_UU	  */
	public String getAMN_Leaves_Types_UU();

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

    /** Column name DaysMode */
    public static final String COLUMNNAME_DaysMode = "DaysMode";

	/** Set Days Calculation Mode.
	  * Days Calculation Mode using Calendar  or Business Days
	  */
	public void setDaysMode (String DaysMode);

	/** Get Days Calculation Mode.
	  * Days Calculation Mode using Calendar  or Business Days
	  */
	public String getDaysMode();

    /** Column name DefaultDays */
    public static final String COLUMNNAME_DefaultDays = "DefaultDays";

	/** Set Default Days.
	  * Default Days Quantity
	  */
	public void setDefaultDays (BigDecimal DefaultDays);

	/** Get Default Days.
	  * Default Days Quantity
	  */
	public BigDecimal getDefaultDays();

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

    /** Column name HoursDay */
    public static final String COLUMNNAME_HoursDay = "HoursDay";

	/** Set Hours per day in a Shift	  */
	public void setHoursDay (BigDecimal HoursDay);

	/** Get Hours per day in a Shift	  */
	public BigDecimal getHoursDay();

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

    /** Column name LeadTime */
    public static final String COLUMNNAME_LeadTime = "LeadTime";

	/** Set Lead Time.
	  * Lead TimeLead time for a request
	  */
	public void setLeadTime (int LeadTime);

	/** Get Lead Time.
	  * Lead TimeLead time for a request
	  */
	public int getLeadTime();

    /** Column name MaximunDaysToPay */
    public static final String COLUMNNAME_MaximunDaysToPay = "MaximunDaysToPay";

	/** Set Maximun Days To Pay.
	  * Maximun Days To Pay witin a month
	  */
	public void setMaximunDaysToPay (int MaximunDaysToPay);

	/** Get Maximun Days To Pay.
	  * Maximun Days To Pay witin a month
	  */
	public int getMaximunDaysToPay();

    /** Column name MinimumDays */
    public static final String COLUMNNAME_MinimumDays = "MinimumDays";

	/** Set Minimum Days.
	  * Minimum Days for requiring Leaves
	  */
	public void setMinimumDays (int MinimumDays);

	/** Get Minimum Days.
	  * Minimum Days for requiring Leaves
	  */
	public int getMinimumDays();

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

    /** Column name ResponseTime */
    public static final String COLUMNNAME_ResponseTime = "ResponseTime";

	/** Set Response Time.
	  * Request Response Time
	  */
	public void setResponseTime (int ResponseTime);

	/** Get Response Time.
	  * Request Response Time
	  */
	public int getResponseTime();

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

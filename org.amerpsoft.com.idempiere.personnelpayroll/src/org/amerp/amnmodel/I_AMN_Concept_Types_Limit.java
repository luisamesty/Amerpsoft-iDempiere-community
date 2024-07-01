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

/** Generated Interface for AMN_Concept_Types_Limit
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Concept_Types_Limit 
{

    /** TableName=AMN_Concept_Types_Limit */
    public static final String Table_Name = "AMN_Concept_Types_Limit";

    /** AD_Table_ID=1000018 */
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

	public I_AMN_Concept_Types getAMN_Concept_Types() throws RuntimeException;

    /** Column name AMN_Concept_Types_Limit_ID */
    public static final String COLUMNNAME_AMN_Concept_Types_Limit_ID = "AMN_Concept_Types_Limit_ID";

	/** Set Concept Types Limits and Dates	  */
	public void setAMN_Concept_Types_Limit_ID (int AMN_Concept_Types_Limit_ID);

	/** Get Concept Types Limits and Dates	  */
	public int getAMN_Concept_Types_Limit_ID();

    /** Column name AMN_Concept_Types_Limit_UU */
    public static final String COLUMNNAME_AMN_Concept_Types_Limit_UU = "AMN_Concept_Types_Limit_UU";

	/** Set AMN_Concept_Types_Limit_UU	  */
	public void setAMN_Concept_Types_Limit_UU (String AMN_Concept_Types_Limit_UU);

	/** Get AMN_Concept_Types_Limit_UU	  */
	public String getAMN_Concept_Types_Limit_UU();

    /** Column name AMN_Period_Status */
    public static final String COLUMNNAME_AMN_Period_Status = "AMN_Period_Status";

	/** Set Period Status	  */
	public void setAMN_Period_Status (String AMN_Period_Status);

	/** Get Period Status	  */
	public String getAMN_Period_Status();

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

    /** Column name QtyTimes */
    public static final String COLUMNNAME_QtyTimes = "QtyTimes";

	/** Set Qty Times	  */
	public void setQtyTimes (BigDecimal QtyTimes);

	/** Get Qty Times	  */
	public BigDecimal getQtyTimes();

    /** Column name Rate */
    public static final String COLUMNNAME_Rate = "Rate";

	/** Set Rate.
	  * Rate or Tax or Exchange
	  */
	public void setRate (BigDecimal Rate);

	/** Get Rate.
	  * Rate or Tax or Exchange
	  */
	public BigDecimal getRate();

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
}

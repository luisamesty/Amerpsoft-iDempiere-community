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

/** Generated Interface for AMN_Concept_Types_Acct
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Concept_Types_Acct 
{

    /** TableName=AMN_Concept_Types_Acct */
    public static final String Table_Name = "AMN_Concept_Types_Acct";

    /** AD_Table_ID=1000090 */
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

    /** Column name AMN_CT_Cre_Acct_ID */
    public static final String COLUMNNAME_AMN_CT_Cre_Acct_ID = "AMN_CT_Cre_Acct_ID";

	/** Set Payroll Concept Type Credit Account.
	  * Payroll Concept Type Credit Account
	  */
	public void setAMN_CT_Cre_Acct_ID (int AMN_CT_Cre_Acct_ID);

	/** Get Payroll Concept Type Credit Account.
	  * Payroll Concept Type Credit Account
	  */
	public int getAMN_CT_Cre_Acct_ID();

	public org.compiere.model.I_C_ElementValue getAMN_CT_Cre_Acct() throws RuntimeException;

    /** Column name AMN_CT_Deb_Acct_ID */
    public static final String COLUMNNAME_AMN_CT_Deb_Acct_ID = "AMN_CT_Deb_Acct_ID";

	/** Set Payroll Concept Type Debit Account.
	  * Payroll Concept Type Debit Account
	  */
	public void setAMN_CT_Deb_Acct_ID (int AMN_CT_Deb_Acct_ID);

	/** Get Payroll Concept Type Debit Account.
	  * Payroll Concept Type Debit Account
	  */
	public int getAMN_CT_Deb_Acct_ID();

	public org.compiere.model.I_C_ElementValue getAMN_CT_Deb_Acct() throws RuntimeException;

    /** Column name AMN_Concept_Types_Acct_ID */
    public static final String COLUMNNAME_AMN_Concept_Types_Acct_ID = "AMN_Concept_Types_Acct_ID";

	/** Set Payroll Concepts Types Accounts	  */
	public void setAMN_Concept_Types_Acct_ID (int AMN_Concept_Types_Acct_ID);

	/** Get Payroll Concepts Types Accounts	  */
	public int getAMN_Concept_Types_Acct_ID();

    /** Column name AMN_Concept_Types_Acct_UU */
    public static final String COLUMNNAME_AMN_Concept_Types_Acct_UU = "AMN_Concept_Types_Acct_UU";

	/** Set AMN_Concept_Types_Acct_UU	  */
	public void setAMN_Concept_Types_Acct_UU (String AMN_Concept_Types_Acct_UU);

	/** Get AMN_Concept_Types_Acct_UU	  */
	public String getAMN_Concept_Types_Acct_UU();

    /** Column name AMN_Concept_Types_ID */
    public static final String COLUMNNAME_AMN_Concept_Types_ID = "AMN_Concept_Types_ID";

	/** Set Payroll Concepts Types	  */
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID);

	/** Get Payroll Concepts Types	  */
	public int getAMN_Concept_Types_ID();

    /** Column name AMN_Cre_Acct */
    public static final String COLUMNNAME_AMN_Cre_Acct = "AMN_Cre_Acct";

	/** Set Concept Credit Administrative Work Force Account.
	  * Concept Credit Administrative Work Force Account
	  */
	public void setAMN_Cre_Acct (int AMN_Cre_Acct);

	/** Get Concept Credit Administrative Work Force Account.
	  * Concept Credit Administrative Work Force Account
	  */
	public int getAMN_Cre_Acct();

	public I_C_ValidCombination getAMN_Cre_A() throws RuntimeException;

    /** Column name AMN_Cre_DW_Acct */
    public static final String COLUMNNAME_AMN_Cre_DW_Acct = "AMN_Cre_DW_Acct";

	/** Set Concept Credit Direct Work Force Account.
	  * Concept Credit Direct Work Force Account
	  */
	public void setAMN_Cre_DW_Acct (int AMN_Cre_DW_Acct);

	/** Get Concept Credit Direct Work Force Account.
	  * Concept Credit Direct Work Force Account
	  */
	public int getAMN_Cre_DW_Acct();

	public I_C_ValidCombination getAMN_Cre_DW_A() throws RuntimeException;

    /** Column name AMN_Cre_IW_Acct */
    public static final String COLUMNNAME_AMN_Cre_IW_Acct = "AMN_Cre_IW_Acct";

	/** Set Concept Credit Indirect Work Force Account.
	  * Concept Credit Indirect Work Force Account
	  */
	public void setAMN_Cre_IW_Acct (int AMN_Cre_IW_Acct);

	/** Get Concept Credit Indirect Work Force Account.
	  * Concept Credit Indirect Work Force Account
	  */
	public int getAMN_Cre_IW_Acct();

	public I_C_ValidCombination getAMN_Cre_IW_A() throws RuntimeException;

    /** Column name AMN_Cre_MW_Acct */
    public static final String COLUMNNAME_AMN_Cre_MW_Acct = "AMN_Cre_MW_Acct";

	/** Set Concept Credit Management Work Force Account.
	  * Concept Credit Management Work Force Account
	  */
	public void setAMN_Cre_MW_Acct (int AMN_Cre_MW_Acct);

	/** Get Concept Credit Management Work Force Account.
	  * Concept Credit Management Work Force Account
	  */
	public int getAMN_Cre_MW_Acct();

	public I_C_ValidCombination getAMN_Cre_MW_A() throws RuntimeException;

    /** Column name AMN_Cre_SW_Acct */
    public static final String COLUMNNAME_AMN_Cre_SW_Acct = "AMN_Cre_SW_Acct";

	/** Set Concept Credit Sales Work Force Account.
	  * Concept Credit Sales Work Force Account
	  */
	public void setAMN_Cre_SW_Acct (int AMN_Cre_SW_Acct);

	/** Get Concept Credit Sales Work Force Account.
	  * Concept Credit Sales Work Force Account
	  */
	public int getAMN_Cre_SW_Acct();

	public I_C_ValidCombination getAMN_Cre_SW_A() throws RuntimeException;

    /** Column name AMN_Deb_Acct */
    public static final String COLUMNNAME_AMN_Deb_Acct = "AMN_Deb_Acct";

	/** Set Concept Debit Administrative Work Force  Account.
	  * Concept Debit Administrative Work Force  Account
	  */
	public void setAMN_Deb_Acct (int AMN_Deb_Acct);

	/** Get Concept Debit Administrative Work Force  Account.
	  * Concept Debit Administrative Work Force  Account
	  */
	public int getAMN_Deb_Acct();

	public I_C_ValidCombination getAMN_Deb_A() throws RuntimeException;

    /** Column name AMN_Deb_DW_Acct */
    public static final String COLUMNNAME_AMN_Deb_DW_Acct = "AMN_Deb_DW_Acct";

	/** Set Concept Debit Direct Work Force Account.
	  * Concept Debit Direct Work Force Debit Account
	  */
	public void setAMN_Deb_DW_Acct (int AMN_Deb_DW_Acct);

	/** Get Concept Debit Direct Work Force Account.
	  * Concept Debit Direct Work Force Debit Account
	  */
	public int getAMN_Deb_DW_Acct();

	public I_C_ValidCombination getAMN_Deb_DW_A() throws RuntimeException;

    /** Column name AMN_Deb_IW_Acct */
    public static final String COLUMNNAME_AMN_Deb_IW_Acct = "AMN_Deb_IW_Acct";

	/** Set Concept Debit Indirect Work Force Account.
	  * Concept Debit Indirect Work Force Debit Account
	  */
	public void setAMN_Deb_IW_Acct (int AMN_Deb_IW_Acct);

	/** Get Concept Debit Indirect Work Force Account.
	  * Concept Debit Indirect Work Force Debit Account
	  */
	public int getAMN_Deb_IW_Acct();

	public I_C_ValidCombination getAMN_Deb_IW_A() throws RuntimeException;

    /** Column name AMN_Deb_MW_Acct */
    public static final String COLUMNNAME_AMN_Deb_MW_Acct = "AMN_Deb_MW_Acct";

	/** Set Concept Debit Management Work Force Account.
	  * Concept Debit Management Work Force Debit Account
	  */
	public void setAMN_Deb_MW_Acct (int AMN_Deb_MW_Acct);

	/** Get Concept Debit Management Work Force Account.
	  * Concept Debit Management Work Force Debit Account
	  */
	public int getAMN_Deb_MW_Acct();

	public I_C_ValidCombination getAMN_Deb_MW_A() throws RuntimeException;

    /** Column name AMN_Deb_SW_Acct */
    public static final String COLUMNNAME_AMN_Deb_SW_Acct = "AMN_Deb_SW_Acct";

	/** Set Concept Debit Sales Work Force Account.
	  * Concept Debit Sales Work Force  Debit Account
	  */
	public void setAMN_Deb_SW_Acct (int AMN_Deb_SW_Acct);

	/** Get Concept Debit Sales Work Force Account.
	  * Concept Debit Sales Work Force  Debit Account
	  */
	public int getAMN_Deb_SW_Acct();

	public I_C_ValidCombination getAMN_Deb_SW_A() throws RuntimeException;

    /** Column name C_AcctSchema_ID */
    public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";

	/** Set Accounting Schema.
	  * Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID);

	/** Get Accounting Schema.
	  * Rules for accounting
	  */
	public int getC_AcctSchema_ID();

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException;

    /** Column name CopyFrom */
    public static final String COLUMNNAME_CopyFrom = "CopyFrom";

	/** Set Copy From.
	  * Copy From Record
	  */
	public void setCopyFrom (String CopyFrom);

	/** Get Copy From.
	  * Copy From Record
	  */
	public String getCopyFrom();

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
}

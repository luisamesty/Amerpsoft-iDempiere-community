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

/** Generated Interface for AMN_Charge
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Charge 
{

    /** TableName=AMN_Charge */
    public static final String Table_Name = "AMN_Charge";

    /** AD_Table_ID=1000076 */
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

    /** Column name AMN_Charge_ID */
    public static final String COLUMNNAME_AMN_Charge_ID = "AMN_Charge_ID";

	/** Set Payroll Charge	  */
	public void setAMN_Charge_ID (int AMN_Charge_ID);

	/** Get Payroll Charge	  */
	public int getAMN_Charge_ID();

    /** Column name AMN_Charge_UU */
    public static final String COLUMNNAME_AMN_Charge_UU = "AMN_Charge_UU";

	/** Set AMN_Charge_UU	  */
	public void setAMN_Charge_UU (String AMN_Charge_UU);

	/** Get AMN_Charge_UU	  */
	public String getAMN_Charge_UU();

    /** Column name AMN_Process_ID */
    public static final String COLUMNNAME_AMN_Process_ID = "AMN_Process_ID";

	/** Set Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID);

	/** Get Payroll Process	  */
	public int getAMN_Process_ID();

	public I_AMN_Process getAMN_Process() throws RuntimeException;

    /** Column name C_Charge_AW_ID */
    public static final String COLUMNNAME_C_Charge_AW_ID = "C_Charge_AW_ID";

	/** Set Charge Administrative Work Force .
	  * Charge for Administrative Work Force 
	  */
	public void setC_Charge_AW_ID (int C_Charge_AW_ID);

	/** Get Charge Administrative Work Force .
	  * Charge for Administrative Work Force 
	  */
	public int getC_Charge_AW_ID();

	public org.compiere.model.I_C_Charge getC_Charge_AW() throws RuntimeException;

    /** Column name C_Charge_DW_ID */
    public static final String COLUMNNAME_C_Charge_DW_ID = "C_Charge_DW_ID";

	/** Set Charge Direct Workforce.
	  * Charge for Direct Workforce
	  */
	public void setC_Charge_DW_ID (int C_Charge_DW_ID);

	/** Get Charge Direct Workforce.
	  * Charge for Direct Workforce
	  */
	public int getC_Charge_DW_ID();

	public org.compiere.model.I_C_Charge getC_Charge_DW() throws RuntimeException;

    /** Column name C_Charge_IW_ID */
    public static final String COLUMNNAME_C_Charge_IW_ID = "C_Charge_IW_ID";

	/** Set Charge Indirect Work Force.
	  * Charge Indirect Work Force
	  */
	public void setC_Charge_IW_ID (int C_Charge_IW_ID);

	/** Get Charge Indirect Work Force.
	  * Charge Indirect Work Force
	  */
	public int getC_Charge_IW_ID();

	public org.compiere.model.I_C_Charge getC_Charge_IW() throws RuntimeException;

    /** Column name C_Charge_MW_ID */
    public static final String COLUMNNAME_C_Charge_MW_ID = "C_Charge_MW_ID";

	/** Set Charge Management Work Force .
	  * Charge for Management Work Force 
	  */
	public void setC_Charge_MW_ID (int C_Charge_MW_ID);

	/** Get Charge Management Work Force .
	  * Charge for Management Work Force 
	  */
	public int getC_Charge_MW_ID();

	public org.compiere.model.I_C_Charge getC_Charge_MW() throws RuntimeException;

    /** Column name C_Charge_SW_ID */
    public static final String COLUMNNAME_C_Charge_SW_ID = "C_Charge_SW_ID";

	/** Set Charge Sales Work Force .
	  * Charge for Sales Work Force 
	  */
	public void setC_Charge_SW_ID (int C_Charge_SW_ID);

	/** Get Charge Sales Work Force .
	  * Charge for Sales Work Force 
	  */
	public int getC_Charge_SW_ID();

	public org.compiere.model.I_C_Charge getC_Charge_SW() throws RuntimeException;

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

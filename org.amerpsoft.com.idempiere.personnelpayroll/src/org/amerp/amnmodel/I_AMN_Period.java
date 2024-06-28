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

/** Generated Interface for AMN_Period
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Period 
{

    /** TableName=AMN_Period */
    public static final String Table_Name = "AMN_Period";

    /** AD_Table_ID=1000041 */
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

    /** Column name AMNDateEnd */
    public static final String COLUMNNAME_AMNDateEnd = "AMNDateEnd";

	/** Set AMNDateEnd	  */
	public void setAMNDateEnd (Timestamp AMNDateEnd);

	/** Get AMNDateEnd	  */
	public Timestamp getAMNDateEnd();

    /** Column name AMNDateIni */
    public static final String COLUMNNAME_AMNDateIni = "AMNDateIni";

	/** Set AMNDateIni	  */
	public void setAMNDateIni (Timestamp AMNDateIni);

	/** Get AMNDateIni	  */
	public Timestamp getAMNDateIni();

    /** Column name AMN_Contract_ID */
    public static final String COLUMNNAME_AMN_Contract_ID = "AMN_Contract_ID";

	/** Set Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID);

	/** Get Payroll Contract	  */
	public int getAMN_Contract_ID();

    /** Column name AMN_Period_ID */
    public static final String COLUMNNAME_AMN_Period_ID = "AMN_Period_ID";

	/** Set Payroll Period	  */
	public void setAMN_Period_ID (int AMN_Period_ID);

	/** Get Payroll Period	  */
	public int getAMN_Period_ID();

    /** Column name AMN_Period_Processed */
    public static final String COLUMNNAME_AMN_Period_Processed = "AMN_Period_Processed";

	/** Set Period Processed	  */
	public void setAMN_Period_Processed (String AMN_Period_Processed);

	/** Get Period Processed	  */
	public String getAMN_Period_Processed();

    /** Column name AMN_Period_Status */
    public static final String COLUMNNAME_AMN_Period_Status = "AMN_Period_Status";

	/** Set Period Status	  */
	public void setAMN_Period_Status (String AMN_Period_Status);

	/** Get Period Status	  */
	public String getAMN_Period_Status();

    /** Column name AMN_Period_UU */
    public static final String COLUMNNAME_AMN_Period_UU = "AMN_Period_UU";

	/** Set AMN_Period_UU	  */
	public void setAMN_Period_UU (String AMN_Period_UU);

	/** Get AMN_Period_UU	  */
	public String getAMN_Period_UU();

    /** Column name AMN_Process_ID */
    public static final String COLUMNNAME_AMN_Process_ID = "AMN_Process_ID";

	/** Set Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID);

	/** Get Payroll Process	  */
	public int getAMN_Process_ID();

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException;

    /** Column name C_Year_ID */
    public static final String COLUMNNAME_C_Year_ID = "C_Year_ID";

	/** Set Year.
	  * Calendar Year
	  */
	public void setC_Year_ID (int C_Year_ID);

	/** Get Year.
	  * Calendar Year
	  */
	public int getC_Year_ID();

	public org.compiere.model.I_C_Year getC_Year() throws RuntimeException;

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

    /** Column name RefDateEnd */
    public static final String COLUMNNAME_RefDateEnd = "RefDateEnd";

	/** Set RefDateEnd	  */
	public void setRefDateEnd (Timestamp RefDateEnd);

	/** Get RefDateEnd	  */
	public Timestamp getRefDateEnd();

    /** Column name RefDateIni */
    public static final String COLUMNNAME_RefDateIni = "RefDateIni";

	/** Set RefDateIni	  */
	public void setRefDateIni (Timestamp RefDateIni);

	/** Get RefDateIni	  */
	public Timestamp getRefDateIni();

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

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

/** Generated Interface for AMN_Jobunit
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Jobunit 
{

    /** TableName=AMN_Jobunit */
    public static final String Table_Name = "AMN_Jobunit";

    /** AD_Table_ID=1000047 */
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

    /** Column name AMN_Department_ID */
    public static final String COLUMNNAME_AMN_Department_ID = "AMN_Department_ID";

	/** Set Payroll Department	  */
	public void setAMN_Department_ID (int AMN_Department_ID);

	/** Get Payroll Department	  */
	public int getAMN_Department_ID();

    /** Column name AMN_Jobunit_ID */
    public static final String COLUMNNAME_AMN_Jobunit_ID = "AMN_Jobunit_ID";

	/** Set Payroll Job Unit	  */
	public void setAMN_Jobunit_ID (int AMN_Jobunit_ID);

	/** Get Payroll Job Unit	  */
	public int getAMN_Jobunit_ID();

    /** Column name AMN_Jobunit_UU */
    public static final String COLUMNNAME_AMN_Jobunit_UU = "AMN_Jobunit_UU";

	/** Set AMN_Jobunit_UU	  */
	public void setAMN_Jobunit_UU (String AMN_Jobunit_UU);

	/** Get AMN_Jobunit_UU	  */
	public String getAMN_Jobunit_UU();

//    /** Column name AMN_Parentjobunit_ID */
//    public static final String COLUMNNAME_AMN_Parentjobunit_ID = "AMN_Parentjobunit_ID";
//
//	/** Set AMN_Parentjobunit_ID	  */
//	public void setAMN_Parentjobunit_ID (int AMN_Parentjobunit_ID);
//
//	/** Get AMN_Parentjobunit_ID	  */
//	public int getAMN_Parentjobunit_ID();
//
//	public I_AMN_Jobunit getAMN_Parentjobunit() throws RuntimeException;

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

//    /** Column name value_parent */
//    public static final String COLUMNNAME_value_parent = "value_parent";
//
//	/** Set value_parent	  */
//	public void setvalue_parent (String value_parent);
//
//	/** Get value_parent	  */
//	public String getvalue_parent();
}

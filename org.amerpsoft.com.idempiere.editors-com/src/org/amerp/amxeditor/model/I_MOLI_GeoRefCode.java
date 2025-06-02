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
package org.amerp.amxeditor.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for MOLI_GeoRefCode
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_MOLI_GeoRefCode 
{

    /** TableName=MOLI_GeoRefCode */
    public static final String Table_Name = "MOLI_GeoRefCode";

    /** AD_Table_ID=1000068 */
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

    /** Column name MOLI_GeoRefCode_DepartmentKey */
    public static final String COLUMNNAME_MOLI_GeoRefCode_DepartmentKey = "MOLI_GeoRefCode_DepartmentKey";

	/** Set Geo Ref Code Department Key	  */
	public void setMOLI_GeoRefCode_DepartmentKey (String MOLI_GeoRefCode_DepartmentKey);

	/** Get Geo Ref Code Department Key	  */
	public String getMOLI_GeoRefCode_DepartmentKey();

    /** Column name MOLI_GeoRefCode_DepartmentName */
    public static final String COLUMNNAME_MOLI_GeoRefCode_DepartmentName = "MOLI_GeoRefCode_DepartmentName";

	/** Set Geo Ref Code Department Name	  */
	public void setMOLI_GeoRefCode_DepartmentName (String MOLI_GeoRefCode_DepartmentName);

	/** Get Geo Ref Code Department Name	  */
	public String getMOLI_GeoRefCode_DepartmentName();

    /** Column name MOLI_GeoRefCode_ID */
    public static final String COLUMNNAME_MOLI_GeoRefCode_ID = "MOLI_GeoRefCode_ID";

	/** Set Geo Ref Code ID	  */
	public void setMOLI_GeoRefCode_ID (int MOLI_GeoRefCode_ID);

	/** Get Geo Ref Code ID	  */
	public int getMOLI_GeoRefCode_ID();

    /** Column name MOLI_GeoRefCode_SectionKey */
    public static final String COLUMNNAME_MOLI_GeoRefCode_SectionKey = "MOLI_GeoRefCode_SectionKey";

	/** Set Geo Ref Code Section Key	  */
	public void setMOLI_GeoRefCode_SectionKey (String MOLI_GeoRefCode_SectionKey);

	/** Get Geo Ref Code Section Key	  */
	public String getMOLI_GeoRefCode_SectionKey();

    /** Column name MOLI_GeoRefCode_SectionName */
    public static final String COLUMNNAME_MOLI_GeoRefCode_SectionName = "MOLI_GeoRefCode_SectionName";

	/** Set Geo Ref Code Section Name	  */
	public void setMOLI_GeoRefCode_SectionName (String MOLI_GeoRefCode_SectionName);

	/** Get Geo Ref Code Section Name	  */
	public String getMOLI_GeoRefCode_SectionName();

    /** Column name MOLI_GeoRefCode_UU */
    public static final String COLUMNNAME_MOLI_GeoRefCode_UU = "MOLI_GeoRefCode_UU";

	/** Set Geo Ref Code UUID	  */
	public void setMOLI_GeoRefCode_UU (String MOLI_GeoRefCode_UU);

	/** Get Geo Ref Code UUID	  */
	public String getMOLI_GeoRefCode_UU();

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

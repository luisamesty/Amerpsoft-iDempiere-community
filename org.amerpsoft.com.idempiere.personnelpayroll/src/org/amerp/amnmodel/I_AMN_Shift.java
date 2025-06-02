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

/** Generated Interface for AMN_Shift
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Shift 
{

    /** TableName=AMN_Shift */
    public static final String Table_Name = "AMN_Shift";

    /** AD_Table_ID=1000014 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

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

    /** Column name AMN_Shift_ID */
    public static final String COLUMNNAME_AMN_Shift_ID = "AMN_Shift_ID";

	/** Set Shift	  */
	public void setAMN_Shift_ID (int AMN_Shift_ID);

	/** Get Shift	  */
	public int getAMN_Shift_ID();

    /** Column name AMN_Shift_UU */
    public static final String COLUMNNAME_AMN_Shift_UU = "AMN_Shift_UU";

	/** Set AMN_Shift_UU	  */
	public void setAMN_Shift_UU (String AMN_Shift_UU);

	/** Get AMN_Shift_UU	  */
	public String getAMN_Shift_UU();

    /** Column name C_Overheadindex_ID */
    public static final String COLUMNNAME_C_Overheadindex_ID = "C_Overheadindex_ID";

	/** Set Overhead Index ID	  */
	public void setC_Overheadindex_ID (int C_Overheadindex_ID);

	/** Get Overhead Index ID	  */
	public int getC_Overheadindex_ID();

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

    /** Column name HEFranquicia */
    public static final String COLUMNNAME_HEFranquicia = "HEFranquicia";

	/** Set HEFranquicia.
	  * Extra Hours Franquicia
	  */
	public void setHEFranquicia (BigDecimal HEFranquicia);

	/** Get HEFranquicia.
	  * Extra Hours Franquicia
	  */
	public BigDecimal getHEFranquicia();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name HoursDay */
    public static final String COLUMNNAME_HoursDay = "HoursDay";

	/** Set Hours per day in a Shift	  */
	public void setHoursDay (BigDecimal HoursDay);

	/** Get Hours per day in a Shift	  */
	public BigDecimal getHoursDay();

    /** Column name HoursWeek */
    public static final String COLUMNNAME_HoursWeek = "HoursWeek";

	/** Set Hours of work per week.
	  * Hours of work per week
	  */
	public void setHoursWeek (BigDecimal HoursWeek);

	/** Get Hours of work per week.
	  * Hours of work per week
	  */
	public BigDecimal getHoursWeek();

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

    /** Column name IsDefault */
    public static final String COLUMNNAME_IsDefault = "IsDefault";

	/** Set Default.
	  * Default value
	  */
	public void setIsDefault (boolean IsDefault);

	/** Get Default.
	  * Default value
	  */
	public boolean isDefault();

    /** Column name isEntryTimeLimit */
    public static final String COLUMNNAME_isEntryTimeLimit = "isEntryTimeLimit";

	/** Set Entry Time Limit.
	  * Entry Time Limit
	  */
	public void setisEntryTimeLimit (boolean isEntryTimeLimit);

	/** Get Entry Time Limit.
	  * Entry Time Limit
	  */
	public boolean isEntryTimeLimit();

    /** Column name isFlexible */
    public static final String COLUMNNAME_isFlexible = "isFlexible";

	/** Set Flexible.
	  * Indicates if it is Flexible
	  */
	public void setisFlexible (boolean isFlexible);

	/** Get Flexible.
	  * Indicates if it is Flexible
	  */
	public boolean isFlexible();

    /** Column name isFlotingShift */
    public static final String COLUMNNAME_isFlotingShift = "isFlotingShift";

	/** Set Floting Shift.
	  * Indicates if it is Floting Shift
	  */
	public void setisFlotingShift (boolean isFlotingShift);

	/** Get Floting Shift.
	  * Indicates if it is Floting Shift
	  */
	public boolean isFlotingShift();

    /** Column name isOverhead */
    public static final String COLUMNNAME_isOverhead = "isOverhead";

	/** Set Overhead.
	  * Indicates if it is Overhead
	  */
	public void setisOverhead (boolean isOverhead);

	/** Get Overhead.
	  * Indicates if it is Overhead
	  */
	public boolean isOverhead();

    /** Column name isOverheadLimit */
    public static final String COLUMNNAME_isOverheadLimit = "isOverheadLimit";

	/** Set Overhead Limit.
	  * Indicates if it is Overhead Limit
	  */
	public void setisOverheadLimit (boolean isOverheadLimit);

	/** Get Overhead Limit.
	  * Indicates if it is Overhead Limit
	  */
	public boolean isOverheadLimit();

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

    /** Column name OverheadLimit */
    public static final String COLUMNNAME_OverheadLimit = "OverheadLimit";

	/** Set Overhead Limit Value.
	  * Overhead Limit Value
	  */
	public void setOverheadLimit (BigDecimal OverheadLimit);

	/** Get Overhead Limit Value.
	  * Overhead Limit Value
	  */
	public BigDecimal getOverheadLimit();

    /** Column name TimeControl */
    public static final String COLUMNNAME_TimeControl = "TimeControl";

	/** Set TimeControl.
	  * Indicates if it is Time Controlled
	  */
	public void setTimeControl (boolean TimeControl);

	/** Get TimeControl.
	  * Indicates if it is Time Controlled
	  */
	public boolean isTimeControl();

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

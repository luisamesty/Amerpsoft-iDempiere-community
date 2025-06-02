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

/** Generated Interface for AMN_Shift_Detail
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Shift_Detail 
{

    /** TableName=AMN_Shift_Detail */
    public static final String Table_Name = "AMN_Shift_Detail";

    /** AD_Table_ID=1000015 */
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

    /** Column name AMN_Shift_Detail_ID */
    public static final String COLUMNNAME_AMN_Shift_Detail_ID = "AMN_Shift_Detail_ID";

	/** Set Shift Detail	  */
	public void setAMN_Shift_Detail_ID (int AMN_Shift_Detail_ID);

	/** Get Shift Detail	  */
	public int getAMN_Shift_Detail_ID();

    /** Column name AMN_Shift_Detail_UU */
    public static final String COLUMNNAME_AMN_Shift_Detail_UU = "AMN_Shift_Detail_UU";

	/** Set AMN_Shift_Detail_UU	  */
	public void setAMN_Shift_Detail_UU (String AMN_Shift_Detail_UU);

	/** Get AMN_Shift_Detail_UU	  */
	public String getAMN_Shift_Detail_UU();

    /** Column name AMN_Shift_ID */
    public static final String COLUMNNAME_AMN_Shift_ID = "AMN_Shift_ID";

	/** Set Shift	  */
	public void setAMN_Shift_ID (int AMN_Shift_ID);

	/** Get Shift	  */
	public int getAMN_Shift_ID();

    /** Column name BreakMinutes */
    public static final String COLUMNNAME_BreakMinutes = "BreakMinutes";

	/** Set Break Minutes.
	  * Break time in Minutes
	  */
	public void setBreakMinutes (int BreakMinutes);

	/** Get Break Minutes.
	  * Break time in Minutes
	  */
	public int getBreakMinutes();

    /** Column name BreakStart */
    public static final String COLUMNNAME_BreakStart = "BreakStart";

	/** Set Break Start.
	  * Break start time
	  */
	public void setBreakStart (Timestamp BreakStart);

	/** Get Break Start.
	  * Break start time
	  */
	public Timestamp getBreakStart();

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

    /** Column name dayofweek */
    public static final String COLUMNNAME_dayofweek = "dayofweek";

	/** Set dayofweek	  */
	public void setdayofweek (String dayofweek);

	/** Get dayofweek	  */
	public String getdayofweek();

    /** Column name Descanso */
    public static final String COLUMNNAME_Descanso = "Descanso";

	/** Set Descanso.
	  * Indicate if Day is a Non-Working Day for rest
	  */
	public void setDescanso (boolean Descanso);

	/** Get Descanso.
	  * Indicate if Day is a Non-Working Day for rest
	  */
	public boolean isDescanso();

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

    /** Column name EntryTime */
    public static final String COLUMNNAME_EntryTime = "EntryTime";

	/** Set Entry Time.
	  * Entry Time
	  */
	public void setEntryTime (Timestamp EntryTime);

	/** Get Entry Time.
	  * Entry Time
	  */
	public Timestamp getEntryTime();

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

    /** Column name isNextDay */
    public static final String COLUMNNAME_isNextDay = "isNextDay";

	/** Set Next Day.
	  * Indicates if it is Next Day
	  */
	public void setisNextDay (boolean isNextDay);

	/** Get Next Day.
	  * Indicates if it is Next Day
	  */
	public boolean isNextDay();

    /** Column name isOutTimeFlexible */
    public static final String COLUMNNAME_isOutTimeFlexible = "isOutTimeFlexible";

	/** Set Out Time Flexible.
	  * Indicates if it is Out Time Flexible
	  */
	public void setisOutTimeFlexible (boolean isOutTimeFlexible);

	/** Get Out Time Flexible.
	  * Indicates if it is Out Time Flexible
	  */
	public boolean isOutTimeFlexible();

    /** Column name isShiftOffset */
    public static final String COLUMNNAME_isShiftOffset = "isShiftOffset";

	/** Set Shift Offset.
	  * Indicates if it is Shift Offset
	  */
	public void setisShiftOffset (boolean isShiftOffset);

	/** Get Shift Offset.
	  * Indicates if it is Shift Offset
	  */
	public boolean isShiftOffset();

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

    /** Column name Shift_In1 */
    public static final String COLUMNNAME_Shift_In1 = "Shift_In1";

	/** Set Shift_In1	  */
	public void setShift_In1 (Timestamp Shift_In1);

	/** Get Shift_In1	  */
	public Timestamp getShift_In1();

    /** Column name Shift_In2 */
    public static final String COLUMNNAME_Shift_In2 = "Shift_In2";

	/** Set Shift_In2	  */
	public void setShift_In2 (Timestamp Shift_In2);

	/** Get Shift_In2	  */
	public Timestamp getShift_In2();

    /** Column name ShiftOffsetET */
    public static final String COLUMNNAME_ShiftOffsetET = "ShiftOffsetET";

	/** Set Shift Offset ET Value.
	  * Indicates Shift Offset ET Value
	  */
	public void setShiftOffsetET (int ShiftOffsetET);

	/** Get Shift Offset ET Value.
	  * Indicates Shift Offset ET Value
	  */
	public int getShiftOffsetET();

    /** Column name ShiftOffsetOT */
    public static final String COLUMNNAME_ShiftOffsetOT = "ShiftOffsetOT";

	/** Set Shift Offset OT Value.
	  * Indicates Shift Offset OT Value
	  */
	public void setShiftOffsetOT (int ShiftOffsetOT);

	/** Get Shift Offset OT Value.
	  * Indicates Shift Offset OT Value
	  */
	public int getShiftOffsetOT();

    /** Column name Shift_Out1 */
    public static final String COLUMNNAME_Shift_Out1 = "Shift_Out1";

	/** Set Shift_Out1	  */
	public void setShift_Out1 (Timestamp Shift_Out1);

	/** Get Shift_Out1	  */
	public Timestamp getShift_Out1();

    /** Column name Shift_Out2 */
    public static final String COLUMNNAME_Shift_Out2 = "Shift_Out2";

	/** Set Shift_Out2	  */
	public void setShift_Out2 (Timestamp Shift_Out2);

	/** Get Shift_Out2	  */
	public Timestamp getShift_Out2();

    /** Column name TimeOut */
    public static final String COLUMNNAME_TimeOut = "TimeOut";

	/** Set Time Out.
	  * Shift Time Out
	  */
	public void setTimeOut (Timestamp TimeOut);

	/** Get Time Out.
	  * Shift Time Out
	  */
	public Timestamp getTimeOut();

    /** Column name Tolerance */
    public static final String COLUMNNAME_Tolerance = "Tolerance";

	/** Set Tolerance.
	  * Tolerance in minutes
	  */
	public void setTolerance (int Tolerance);

	/** Get Tolerance.
	  * Tolerance in minutes
	  */
	public int getTolerance();

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

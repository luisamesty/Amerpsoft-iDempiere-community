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

/** Generated Interface for AMN_Payroll_Assist_Proc
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Payroll_Assist_Proc 
{

    /** TableName=AMN_Payroll_Assist_Proc */
    public static final String Table_Name = "AMN_Payroll_Assist_Proc";

    /** AD_Table_ID=1000031 */
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

    /** Column name AMN_Employee_ID */
    public static final String COLUMNNAME_AMN_Employee_ID = "AMN_Employee_ID";

	/** Set Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID);

	/** Get Payroll employee	  */
	public int getAMN_Employee_ID();

    /** Column name AMN_Payroll_Assist_Proc_ID */
    public static final String COLUMNNAME_AMN_Payroll_Assist_Proc_ID = "AMN_Payroll_Assist_Proc_ID";

	/** Set AMN_Payroll_Assist_Proc_ID	  */
	public void setAMN_Payroll_Assist_Proc_ID (int AMN_Payroll_Assist_Proc_ID);

	/** Get AMN_Payroll_Assist_Proc_ID	  */
	public int getAMN_Payroll_Assist_Proc_ID();

    /** Column name AMN_Payroll_Assist_Proc_UU */
    public static final String COLUMNNAME_AMN_Payroll_Assist_Proc_UU = "AMN_Payroll_Assist_Proc_UU";

	/** Set AMN_Payroll_Assist_Proc_UU	  */
	public void setAMN_Payroll_Assist_Proc_UU (String AMN_Payroll_Assist_Proc_UU);

	/** Get AMN_Payroll_Assist_Proc_UU	  */
	public String getAMN_Payroll_Assist_Proc_UU();

    /** Column name AMN_Shift_ID */
    public static final String COLUMNNAME_AMN_Shift_ID = "AMN_Shift_ID";

	/** Set Shift	  */
	public void setAMN_Shift_ID (int AMN_Shift_ID);

	/** Get Shift	  */
	public int getAMN_Shift_ID();

    /** Column name BioCode */
    public static final String COLUMNNAME_BioCode = "BioCode";

	/** Set BioCode.
	  * Biometric Code from Attendance Control Scanners
	  */
	public void setBioCode (String BioCode);

	/** Get BioCode.
	  * Biometric Code from Attendance Control Scanners
	  */
	public String getBioCode();

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

    /** Column name Event_Date */
    public static final String COLUMNNAME_Event_Date = "Event_Date";

	/** Set Event_Date	  */
	public void setEvent_Date (Timestamp Event_Date);

	/** Get Event_Date	  */
	public Timestamp getEvent_Date();

    /** Column name Excused */
    public static final String COLUMNNAME_Excused = "Excused";

	/** Set Excused Absence	  */
	public void setExcused (boolean Excused);

	/** Get Excused Absence	  */
	public boolean isExcused();

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

    /** Column name IsNonBusinessDay */
    public static final String COLUMNNAME_IsNonBusinessDay = "IsNonBusinessDay";

	/** Set Non Business Day	  */
	public void setIsNonBusinessDay (boolean IsNonBusinessDay);

	/** Get Non Business Day	  */
	public boolean isNonBusinessDay();

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

    /** Column name Protected */
    public static final String COLUMNNAME_Protected = "Protected";

	/** Set Protected.
	  * Protected to write
	  */
	public void setProtected (boolean Protected);

	/** Get Protected.
	  * Protected to write
	  */
	public boolean isProtected();

    /** Column name Shift_Attendance */
    public static final String COLUMNNAME_Shift_Attendance = "Shift_Attendance";

	/** Set Shift_Attendance.
	  * Indicates Employee&#039;
s Atthendance during one day
	  */
	public void setShift_Attendance (BigDecimal Shift_Attendance);

	/** Get Shift_Attendance.
	  * Indicates Employee&#039;
s Atthendance during one day
	  */
	public BigDecimal getShift_Attendance();

    /** Column name Shift_AttendanceBonus */
    public static final String COLUMNNAME_Shift_AttendanceBonus = "Shift_AttendanceBonus";

	/** Set Shift_AttendanceBonus.
	  * Indicates Employee&#039;
s Atthendance Bonus during one day
	  */
	public void setShift_AttendanceBonus (BigDecimal Shift_AttendanceBonus);

	/** Get Shift_AttendanceBonus.
	  * Indicates Employee&#039;
s Atthendance Bonus during one day
	  */
	public BigDecimal getShift_AttendanceBonus();

    /** Column name Shift_EDE */
    public static final String COLUMNNAME_Shift_EDE = "Shift_EDE";

	/** Set EDE.
	  * Shift_EDE Early Departures
	  */
	public void setShift_EDE (BigDecimal Shift_EDE);

	/** Get EDE.
	  * Shift_EDE Early Departures
	  */
	public BigDecimal getShift_EDE();

    /** Column name Shift_HC */
    public static final String COLUMNNAME_Shift_HC = "Shift_HC";

	/** Set HC.
	  * Shift_HC Complete Hours
	  */
	public void setShift_HC (BigDecimal Shift_HC);

	/** Get HC.
	  * Shift_HC Complete Hours
	  */
	public BigDecimal getShift_HC();

    /** Column name Shift_HED */
    public static final String COLUMNNAME_Shift_HED = "Shift_HED";

	/** Set HED.
	  * Extra Daytime Hours
	  */
	public void setShift_HED (BigDecimal Shift_HED);

	/** Get HED.
	  * Extra Daytime Hours
	  */
	public BigDecimal getShift_HED();

    /** Column name Shift_HEF */
    public static final String COLUMNNAME_Shift_HEF = "Shift_HEF";

	/** Set HEF.
	  * Shift_HEF  Extra Holliday Hours	
	  */
	public void setShift_HEF (BigDecimal Shift_HEF);

	/** Get HEF.
	  * Shift_HEF  Extra Holliday Hours	
	  */
	public BigDecimal getShift_HEF();

    /** Column name Shift_HEN */
    public static final String COLUMNNAME_Shift_HEN = "Shift_HEN";

	/** Set HEN.
	  * Extra Nighttime Hours
	  */
	public void setShift_HEN (BigDecimal Shift_HEN);

	/** Get HEN.
	  * Extra Nighttime Hours
	  */
	public BigDecimal getShift_HEN();

    /** Column name Shift_HER */
    public static final String COLUMNNAME_Shift_HER = "Shift_HER";

	/** Set HER.
	  * Shift_HER  Extra Clock Hours
	  */
	public void setShift_HER (BigDecimal Shift_HER);

	/** Get HER.
	  * Shift_HER  Extra Clock Hours
	  */
	public BigDecimal getShift_HER();

    /** Column name Shift_HLGT15 */
    public static final String COLUMNNAME_Shift_HLGT15 = "Shift_HLGT15";

	/** Set HLGT15.
	  * Shift_HLGT15 Free Hours Greater than 15&#039;

	  */
	public void setShift_HLGT15 (BigDecimal Shift_HLGT15);

	/** Get HLGT15.
	  * Shift_HLGT15 Free Hours Greater than 15&#039;

	  */
	public BigDecimal getShift_HLGT15();

    /** Column name Shift_HLLT15 */
    public static final String COLUMNNAME_Shift_HLLT15 = "Shift_HLLT15";

	/** Set HLLT15.
	  * Shift_HLLT15 Free Hours Less than 15&#039;

	  */
	public void setShift_HLLT15 (BigDecimal Shift_HLLT15);

	/** Get HLLT15.
	  * Shift_HLLT15 Free Hours Less than 15&#039;

	  */
	public BigDecimal getShift_HLLT15();

    /** Column name Shift_HND */
    public static final String COLUMNNAME_Shift_HND = "Shift_HND";

	/** Set HND.
	  * Normal Daytime Hours
	  */
	public void setShift_HND (BigDecimal Shift_HND);

	/** Get HND.
	  * Normal Daytime Hours
	  */
	public BigDecimal getShift_HND();

    /** Column name Shift_HNN */
    public static final String COLUMNNAME_Shift_HNN = "Shift_HNN";

	/** Set HNN.
	  * Normal Nighttime Hours
	  */
	public void setShift_HNN (BigDecimal Shift_HNN);

	/** Get HNN.
	  * Normal Nighttime Hours
	  */
	public BigDecimal getShift_HNN();

    /** Column name Shift_HT */
    public static final String COLUMNNAME_Shift_HT = "Shift_HT";

	/** Set HT.
	  * Shift_HT Total Work Hours	
	  */
	public void setShift_HT (BigDecimal Shift_HT);

	/** Get HT.
	  * Shift_HT Total Work Hours	
	  */
	public BigDecimal getShift_HT();

    /** Column name Shift_In1 */
    public static final String COLUMNNAME_Shift_In1 = "Shift_In1";

	/** Set Shift_In1.
	  * First Shift Entry
	  */
	public void setShift_In1 (Timestamp Shift_In1);

	/** Get Shift_In1.
	  * First Shift Entry
	  */
	public Timestamp getShift_In1();

    /** Column name Shift_In2 */
    public static final String COLUMNNAME_Shift_In2 = "Shift_In2";

	/** Set Shift_In2.
	  * Second Shift Entry
	  */
	public void setShift_In2 (Timestamp Shift_In2);

	/** Get Shift_In2.
	  * Second Shift Entry
	  */
	public Timestamp getShift_In2();

    /** Column name Shift_LTA */
    public static final String COLUMNNAME_Shift_LTA = "Shift_LTA";

	/** Set LT.
	  * HR_LTA Late Arrivals
	  */
	public void setShift_LTA (BigDecimal Shift_LTA);

	/** Get LT.
	  * HR_LTA Late Arrivals
	  */
	public BigDecimal getShift_LTA();

    /** Column name Shift_Out1 */
    public static final String COLUMNNAME_Shift_Out1 = "Shift_Out1";

	/** Set Shift_Out1.
	  * First Shift Output
	  */
	public void setShift_Out1 (Timestamp Shift_Out1);

	/** Get Shift_Out1.
	  * First Shift Output
	  */
	public Timestamp getShift_Out1();

    /** Column name Shift_Out2 */
    public static final String COLUMNNAME_Shift_Out2 = "Shift_Out2";

	/** Set Shift_Out2.
	  * Second Shift Output
	  */
	public void setShift_Out2 (Timestamp Shift_Out2);

	/** Get Shift_Out2.
	  * Second Shift Output
	  */
	public Timestamp getShift_Out2();

    /** Column name Shift_THL */
    public static final String COLUMNNAME_Shift_THL = "Shift_THL";

	/** Set THL.
	  * HR_THL Free Hours
	  */
	public void setShift_THL (BigDecimal Shift_THL);

	/** Get THL.
	  * HR_THL Free Hours
	  */
	public BigDecimal getShift_THL();

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

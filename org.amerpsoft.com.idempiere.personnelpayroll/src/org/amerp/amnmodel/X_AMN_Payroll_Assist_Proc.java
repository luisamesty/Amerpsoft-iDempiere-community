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
/** Generated Model - DO NOT CHANGE */
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for AMN_Payroll_Assist_Proc
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Payroll_Assist_Proc")
public class X_AMN_Payroll_Assist_Proc extends PO implements I_AMN_Payroll_Assist_Proc, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250311L;

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Proc (Properties ctx, int AMN_Payroll_Assist_Proc_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Proc_ID, trxName);
      /** if (AMN_Payroll_Assist_Proc_ID == 0)
        {
			setAMN_Payroll_Assist_Proc_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setExcused (false);
// N
			setIsNonBusinessDay (false);
// N
			setName (null);
			setProtected (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Proc (Properties ctx, int AMN_Payroll_Assist_Proc_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Assist_Proc_ID, trxName, virtualColumns);
      /** if (AMN_Payroll_Assist_Proc_ID == 0)
        {
			setAMN_Payroll_Assist_Proc_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setExcused (false);
// N
			setIsNonBusinessDay (false);
// N
			setName (null);
			setProtected (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Proc (Properties ctx, String AMN_Payroll_Assist_Proc_UU, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Proc_UU, trxName);
      /** if (AMN_Payroll_Assist_Proc_UU == null)
        {
			setAMN_Payroll_Assist_Proc_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setExcused (false);
// N
			setIsNonBusinessDay (false);
// N
			setName (null);
			setProtected (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Proc (Properties ctx, String AMN_Payroll_Assist_Proc_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Payroll_Assist_Proc_UU, trxName, virtualColumns);
      /** if (AMN_Payroll_Assist_Proc_UU == null)
        {
			setAMN_Payroll_Assist_Proc_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setExcused (false);
// N
			setIsNonBusinessDay (false);
// N
			setName (null);
			setProtected (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Assist_Proc (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_AMN_Payroll_Assist_Proc[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Payroll employee.
		@param AMN_Employee_ID Payroll employee
	*/
	public void setAMN_Employee_ID (int AMN_Employee_ID)
	{
		if (AMN_Employee_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
	}

	/** Get Payroll employee.
		@return Payroll employee	  */
	public int getAMN_Employee_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_Proc_ID.
		@param AMN_Payroll_Assist_Proc_ID AMN_Payroll_Assist_Proc_ID
	*/
	public void setAMN_Payroll_Assist_Proc_ID (int AMN_Payroll_Assist_Proc_ID)
	{
		if (AMN_Payroll_Assist_Proc_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Proc_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Proc_ID, Integer.valueOf(AMN_Payroll_Assist_Proc_ID));
	}

	/** Get AMN_Payroll_Assist_Proc_ID.
		@return AMN_Payroll_Assist_Proc_ID	  */
	public int getAMN_Payroll_Assist_Proc_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Assist_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_Proc_UU.
		@param AMN_Payroll_Assist_Proc_UU AMN_Payroll_Assist_Proc_UU
	*/
	public void setAMN_Payroll_Assist_Proc_UU (String AMN_Payroll_Assist_Proc_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Proc_UU, AMN_Payroll_Assist_Proc_UU);
	}

	/** Get AMN_Payroll_Assist_Proc_UU.
		@return AMN_Payroll_Assist_Proc_UU	  */
	public String getAMN_Payroll_Assist_Proc_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Assist_Proc_UU);
	}

	/** Set Shift.
		@param AMN_Shift_ID Shift
	*/
	public void setAMN_Shift_ID (int AMN_Shift_ID)
	{
		if (AMN_Shift_ID < 1)
			set_Value (COLUMNNAME_AMN_Shift_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Shift_ID, Integer.valueOf(AMN_Shift_ID));
	}

	/** Get Shift.
		@return Shift	  */
	public int getAMN_Shift_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Shift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BioCode.
		@param BioCode Biometric Code from Attendance Control Scanners
	*/
	public void setBioCode (String BioCode)
	{
		set_Value (COLUMNNAME_BioCode, BioCode);
	}

	/** Get BioCode.
		@return Biometric Code from Attendance Control Scanners
	  */
	public String getBioCode()
	{
		return (String)get_Value(COLUMNNAME_BioCode);
	}

	/** Sunday = 1 */
	public static final String DAYOFWEEK_Sunday = "1";
	/** Monday = 2 */
	public static final String DAYOFWEEK_Monday = "2";
	/** Tuesday = 3 */
	public static final String DAYOFWEEK_Tuesday = "3";
	/** Wednesday = 4 */
	public static final String DAYOFWEEK_Wednesday = "4";
	/** Thursday = 5 */
	public static final String DAYOFWEEK_Thursday = "5";
	/** Friday = 6 */
	public static final String DAYOFWEEK_Friday = "6";
	/** Saturday = 7 */
	public static final String DAYOFWEEK_Saturday = "7";
	/** Set dayofweek.
		@param dayofweek dayofweek
	*/
	public void setdayofweek (String dayofweek)
	{

		set_Value (COLUMNNAME_dayofweek, dayofweek);
	}

	/** Get dayofweek.
		@return dayofweek	  */
	public String getdayofweek()
	{
		return (String)get_Value(COLUMNNAME_dayofweek);
	}

	/** Set Descanso.
		@param Descanso Indicate if Day is a Non-Working Day for rest
	*/
	public void setDescanso (boolean Descanso)
	{
		set_Value (COLUMNNAME_Descanso, Boolean.valueOf(Descanso));
	}

	/** Get Descanso.
		@return Indicate if Day is a Non-Working Day for rest
	  */
	public boolean isDescanso()
	{
		Object oo = get_Value(COLUMNNAME_Descanso);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Event_Date.
		@param Event_Date Event_Date
	*/
	public void setEvent_Date (Timestamp Event_Date)
	{
		set_Value (COLUMNNAME_Event_Date, Event_Date);
	}

	/** Get Event_Date.
		@return Event_Date	  */
	public Timestamp getEvent_Date()
	{
		return (Timestamp)get_Value(COLUMNNAME_Event_Date);
	}

	/** Set Excused Absence.
		@param Excused Excused Absence
	*/
	public void setExcused (boolean Excused)
	{
		set_Value (COLUMNNAME_Excused, Boolean.valueOf(Excused));
	}

	/** Get Excused Absence.
		@return Excused Absence	  */
	public boolean isExcused()
	{
		Object oo = get_Value(COLUMNNAME_Excused);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Non Business Day.
		@param IsNonBusinessDay Non Business Day
	*/
	public void setIsNonBusinessDay (boolean IsNonBusinessDay)
	{
		set_Value (COLUMNNAME_IsNonBusinessDay, Boolean.valueOf(IsNonBusinessDay));
	}

	/** Get Non Business Day.
		@return Non Business Day	  */
	public boolean isNonBusinessDay()
	{
		Object oo = get_Value(COLUMNNAME_IsNonBusinessDay);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Protected.
		@param Protected Protected to write
	*/
	public void setProtected (boolean Protected)
	{
		set_Value (COLUMNNAME_Protected, Boolean.valueOf(Protected));
	}

	/** Get Protected.
		@return Protected to write
	  */
	public boolean isProtected()
	{
		Object oo = get_Value(COLUMNNAME_Protected);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Shift_Attendance.
		@param Shift_Attendance Indicates Employee&#039;s Atthendance during one day
	*/
	public void setShift_Attendance (BigDecimal Shift_Attendance)
	{
		set_Value (COLUMNNAME_Shift_Attendance, Shift_Attendance);
	}

	/** Get Shift_Attendance.
		@return Indicates Employee&#039;s Atthendance during one day
	  */
	public BigDecimal getShift_Attendance()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_Attendance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Shift_AttendanceBonus.
		@param Shift_AttendanceBonus Indicates Employee&#039;s Atthendance Bonus during one day
	*/
	public void setShift_AttendanceBonus (BigDecimal Shift_AttendanceBonus)
	{
		set_Value (COLUMNNAME_Shift_AttendanceBonus, Shift_AttendanceBonus);
	}

	/** Get Shift_AttendanceBonus.
		@return Indicates Employee&#039;s Atthendance Bonus during one day
	  */
	public BigDecimal getShift_AttendanceBonus()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_AttendanceBonus);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set EDE.
		@param Shift_EDE Shift_EDE Early Departures
	*/
	public void setShift_EDE (BigDecimal Shift_EDE)
	{
		set_Value (COLUMNNAME_Shift_EDE, Shift_EDE);
	}

	/** Get EDE.
		@return Shift_EDE Early Departures
	  */
	public BigDecimal getShift_EDE()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_EDE);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HC.
		@param Shift_HC Shift_HC Complete Hours
	*/
	public void setShift_HC (BigDecimal Shift_HC)
	{
		set_Value (COLUMNNAME_Shift_HC, Shift_HC);
	}

	/** Get HC.
		@return Shift_HC Complete Hours
	  */
	public BigDecimal getShift_HC()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HC);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HED.
		@param Shift_HED Extra Daytime Hours
	*/
	public void setShift_HED (BigDecimal Shift_HED)
	{
		set_Value (COLUMNNAME_Shift_HED, Shift_HED);
	}

	/** Get HED.
		@return Extra Daytime Hours
	  */
	public BigDecimal getShift_HED()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HED);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HEF.
		@param Shift_HEF Shift_HEF  Extra Holliday Hours	
	*/
	public void setShift_HEF (BigDecimal Shift_HEF)
	{
		set_Value (COLUMNNAME_Shift_HEF, Shift_HEF);
	}

	/** Get HEF.
		@return Shift_HEF  Extra Holliday Hours	
	  */
	public BigDecimal getShift_HEF()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HEF);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HEN.
		@param Shift_HEN Extra Nighttime Hours
	*/
	public void setShift_HEN (BigDecimal Shift_HEN)
	{
		set_Value (COLUMNNAME_Shift_HEN, Shift_HEN);
	}

	/** Get HEN.
		@return Extra Nighttime Hours
	  */
	public BigDecimal getShift_HEN()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HEN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HER.
		@param Shift_HER Shift_HER  Extra Clock Hours
	*/
	public void setShift_HER (BigDecimal Shift_HER)
	{
		set_Value (COLUMNNAME_Shift_HER, Shift_HER);
	}

	/** Get HER.
		@return Shift_HER  Extra Clock Hours
	  */
	public BigDecimal getShift_HER()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HER);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HLGT15.
		@param Shift_HLGT15 Shift_HLGT15 Free Hours Greater than 15&#039;
	*/
	public void setShift_HLGT15 (BigDecimal Shift_HLGT15)
	{
		set_Value (COLUMNNAME_Shift_HLGT15, Shift_HLGT15);
	}

	/** Get HLGT15.
		@return Shift_HLGT15 Free Hours Greater than 15&#039;
	  */
	public BigDecimal getShift_HLGT15()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HLGT15);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HLLT15.
		@param Shift_HLLT15 Shift_HLLT15 Free Hours Less than 15&#039;
	*/
	public void setShift_HLLT15 (BigDecimal Shift_HLLT15)
	{
		set_Value (COLUMNNAME_Shift_HLLT15, Shift_HLLT15);
	}

	/** Get HLLT15.
		@return Shift_HLLT15 Free Hours Less than 15&#039;
	  */
	public BigDecimal getShift_HLLT15()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HLLT15);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HND.
		@param Shift_HND Normal Daytime Hours
	*/
	public void setShift_HND (BigDecimal Shift_HND)
	{
		set_Value (COLUMNNAME_Shift_HND, Shift_HND);
	}

	/** Get HND.
		@return Normal Daytime Hours
	  */
	public BigDecimal getShift_HND()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HND);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HNN.
		@param Shift_HNN Normal Nighttime Hours
	*/
	public void setShift_HNN (BigDecimal Shift_HNN)
	{
		set_Value (COLUMNNAME_Shift_HNN, Shift_HNN);
	}

	/** Get HNN.
		@return Normal Nighttime Hours
	  */
	public BigDecimal getShift_HNN()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HNN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set HT.
		@param Shift_HT Shift_HT Total Work Hours	
	*/
	public void setShift_HT (BigDecimal Shift_HT)
	{
		set_Value (COLUMNNAME_Shift_HT, Shift_HT);
	}

	/** Get HT.
		@return Shift_HT Total Work Hours	
	  */
	public BigDecimal getShift_HT()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HT);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Shift_In1.
		@param Shift_In1 First Shift Entry
	*/
	public void setShift_In1 (Timestamp Shift_In1)
	{
		set_Value (COLUMNNAME_Shift_In1, Shift_In1);
	}

	/** Get Shift_In1.
		@return First Shift Entry
	  */
	public Timestamp getShift_In1()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_In1);
	}

	/** Set Shift_In2.
		@param Shift_In2 Second Shift Entry
	*/
	public void setShift_In2 (Timestamp Shift_In2)
	{
		set_Value (COLUMNNAME_Shift_In2, Shift_In2);
	}

	/** Get Shift_In2.
		@return Second Shift Entry
	  */
	public Timestamp getShift_In2()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_In2);
	}

	/** Set LT.
		@param Shift_LTA HR_LTA Late Arrivals
	*/
	public void setShift_LTA (BigDecimal Shift_LTA)
	{
		set_Value (COLUMNNAME_Shift_LTA, Shift_LTA);
	}

	/** Get LT.
		@return HR_LTA Late Arrivals
	  */
	public BigDecimal getShift_LTA()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_LTA);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Shift_Out1.
		@param Shift_Out1 First Shift Output
	*/
	public void setShift_Out1 (Timestamp Shift_Out1)
	{
		set_Value (COLUMNNAME_Shift_Out1, Shift_Out1);
	}

	/** Get Shift_Out1.
		@return First Shift Output
	  */
	public Timestamp getShift_Out1()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_Out1);
	}

	/** Set Shift_Out2.
		@param Shift_Out2 Second Shift Output
	*/
	public void setShift_Out2 (Timestamp Shift_Out2)
	{
		set_Value (COLUMNNAME_Shift_Out2, Shift_Out2);
	}

	/** Get Shift_Out2.
		@return Second Shift Output
	  */
	public Timestamp getShift_Out2()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_Out2);
	}

	/** Set THL.
		@param Shift_THL HR_THL Free Hours
	*/
	public void setShift_THL (BigDecimal Shift_THL)
	{
		set_Value (COLUMNNAME_Shift_THL, Shift_THL);
	}

	/** Get THL.
		@return HR_THL Free Hours
	  */
	public BigDecimal getShift_THL()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_THL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}
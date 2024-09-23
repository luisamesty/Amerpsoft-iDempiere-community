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
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Payroll_Assist_Proc extends PO implements I_AMN_Payroll_Assist_Proc, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180717L;

    /** Standard Constructor */
    public X_AMN_Payroll_Assist_Proc (Properties ctx, int AMN_Payroll_Assist_Proc_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_Proc_ID, trxName);
      /** if (AMN_Payroll_Assist_Proc_ID == 0)
        {
			setAMN_Payroll_Assist_Proc_ID (0);
			setAMN_Shift_ID (0);
			setName (null);
			setdayofweek (null);
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
      StringBuffer sb = new StringBuffer ("X_AMN_Payroll_Assist_Proc[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll employee.
		@param AMN_Employee_ID Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID)
	{
		if (AMN_Employee_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Employee_ID, Integer.valueOf(AMN_Employee_ID));
	}

	/** Get Payroll employee.
		@return Payroll employee	  */
	public int getAMN_Employee_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Employee_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_Proc_ID.
		@param AMN_Payroll_Assist_Proc_ID AMN_Payroll_Assist_Proc_ID	  */
	public void setAMN_Payroll_Assist_Proc_ID (int AMN_Payroll_Assist_Proc_ID)
	{
		if (AMN_Payroll_Assist_Proc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Proc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Proc_ID, Integer.valueOf(AMN_Payroll_Assist_Proc_ID));
	}

	/** Get AMN_Payroll_Assist_Proc_ID.
		@return AMN_Payroll_Assist_Proc_ID	  */
	public int getAMN_Payroll_Assist_Proc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Assist_Proc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_Proc_UU.
		@param AMN_Payroll_Assist_Proc_UU AMN_Payroll_Assist_Proc_UU	  */
	public void setAMN_Payroll_Assist_Proc_UU (String AMN_Payroll_Assist_Proc_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_Proc_UU, AMN_Payroll_Assist_Proc_UU);
	}

	/** Get AMN_Payroll_Assist_Proc_UU.
		@return AMN_Payroll_Assist_Proc_UU	  */
	public String getAMN_Payroll_Assist_Proc_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Assist_Proc_UU);
	}

	/** Set Shift.
		@param AMN_Shift_ID Shift	  */
	public void setAMN_Shift_ID (int AMN_Shift_ID)
	{
		if (AMN_Shift_ID < 1) 
			set_Value (COLUMNNAME_AMN_Shift_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Shift_ID, Integer.valueOf(AMN_Shift_ID));
	}

	/** Get Shift.
		@return Shift	  */
	public int getAMN_Shift_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Shift_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set BioCode.
		@param BioCode 
		Biometric Code from Attendance Control Scanners
	  */
	public void setBioCode (String BioCode)
	{
		set_Value (COLUMNNAME_BioCode, BioCode);
	}

	/** Get BioCode.
		@return Biometric Code from Attendance Control Scanners
	  */
	public String getBioCode () 
	{
		return (String)get_Value(COLUMNNAME_BioCode);
	}

	/** Set Descanso.
		@param Descanso 
		Indicate if Day is a Non-Working Day for rest
	  */
	public void setDescanso (boolean Descanso)
	{
		set_Value (COLUMNNAME_Descanso, Boolean.valueOf(Descanso));
	}

	/** Get Descanso.
		@return Indicate if Day is a Non-Working Day for rest
	  */
	public boolean isDescanso () 
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
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Event_Date.
		@param Event_Date Event_Date	  */
	public void setEvent_Date (Timestamp Event_Date)
	{
		set_Value (COLUMNNAME_Event_Date, Event_Date);
	}

	/** Get Event_Date.
		@return Event_Date	  */
	public Timestamp getEvent_Date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Event_Date);
	}

	/** Set Excused Absence.
		@param Excused Excused Absence	  */
	public void setExcused (boolean Excused)
	{
		set_Value (COLUMNNAME_Excused, Boolean.valueOf(Excused));
	}

	/** Get Excused Absence.
		@return Excused Absence	  */
	public boolean isExcused () 
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
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

	/** Set Shift_Attendance.
		@param Shift_Attendance 
		Indicates Employee's Atthendance during one day
	  */
	public void setShift_Attendance (BigDecimal Shift_Attendance)
	{
		set_Value (COLUMNNAME_Shift_Attendance, Shift_Attendance);
	}

	/** Get Shift_Attendance.
		@return Indicates Employee's Atthendance during one day
	  */
	public BigDecimal getShift_Attendance () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_Attendance);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Shift_AttendanceBonus.
		@param Shift_AttendanceBonus 
		Indicates Employee's Atthendance Bonus during one day
	  */
	public void setShift_AttendanceBonus (BigDecimal Shift_AttendanceBonus)
	{
		set_Value (COLUMNNAME_Shift_AttendanceBonus, Shift_AttendanceBonus);
	}

	/** Get Shift_AttendanceBonus.
		@return Indicates Employee's Atthendance Bonus during one day
	  */
	public BigDecimal getShift_AttendanceBonus () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_AttendanceBonus);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Extra Daytime Hours.
		@param Shift_HED 
		Extra Daytime Hours
	  */
	public void setShift_HED (BigDecimal Shift_HED)
	{
		set_Value (COLUMNNAME_Shift_HED, Shift_HED);
	}

	/** Get Extra Daytime Hours.
		@return Extra Daytime Hours
	  */
	public BigDecimal getShift_HED () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HED);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Extra Nighttime Hours.
		@param Shift_HEN 
		Extra Nighttime Hours
	  */
	public void setShift_HEN (BigDecimal Shift_HEN)
	{
		set_Value (COLUMNNAME_Shift_HEN, Shift_HEN);
	}

	/** Get Extra Nighttime Hours.
		@return Extra Nighttime Hours
	  */
	public BigDecimal getShift_HEN () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HEN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Normal Daytime Hours.
		@param Shift_HND 
		Normal Daytime Hours
	  */
	public void setShift_HND (BigDecimal Shift_HND)
	{
		set_Value (COLUMNNAME_Shift_HND, Shift_HND);
	}

	/** Get Normal Daytime Hours.
		@return Normal Daytime Hours
	  */
	public BigDecimal getShift_HND () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HND);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Normal Nighttime Hours.
		@param Shift_HNN 
		Normal Nighttime Hours
	  */
	public void setShift_HNN (BigDecimal Shift_HNN)
	{
		set_Value (COLUMNNAME_Shift_HNN, Shift_HNN);
	}

	/** Get Normal Nighttime Hours.
		@return Normal Nighttime Hours
	  */
	public BigDecimal getShift_HNN () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Shift_HNN);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Shift_In1.
		@param Shift_In1 Shift_In1	  */
	public void setShift_In1 (Timestamp Shift_In1)
	{
		set_Value (COLUMNNAME_Shift_In1, Shift_In1);
	}

	/** Get Shift_In1.
		@return Shift_In1	  */
	public Timestamp getShift_In1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_In1);
	}

	/** Set Shift_In2.
		@param Shift_In2 Shift_In2	  */
	public void setShift_In2 (Timestamp Shift_In2)
	{
		set_Value (COLUMNNAME_Shift_In2, Shift_In2);
	}

	/** Get Shift_In2.
		@return Shift_In2	  */
	public Timestamp getShift_In2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_In2);
	}

	/** Set Shift_Out1.
		@param Shift_Out1 Shift_Out1	  */
	public void setShift_Out1 (Timestamp Shift_Out1)
	{
		set_Value (COLUMNNAME_Shift_Out1, Shift_Out1);
	}

	/** Get Shift_Out1.
		@return Shift_Out1	  */
	public Timestamp getShift_Out1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_Out1);
	}

	/** Set Shift_Out2.
		@param Shift_Out2 Shift_Out2	  */
	public void setShift_Out2 (Timestamp Shift_Out2)
	{
		set_Value (COLUMNNAME_Shift_Out2, Shift_Out2);
	}

	/** Get Shift_Out2.
		@return Shift_Out2	  */
	public Timestamp getShift_Out2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_Out2);
	}

	/** Sunday = 0 */
	public static final String DAYOFWEEK_Sunday = "0";
	/** Monday = 1 */
	public static final String DAYOFWEEK_Monday = "1";
	/** Tuesday = 2 */
	public static final String DAYOFWEEK_Tuesday = "2";
	/** Wednesday = 3 */
	public static final String DAYOFWEEK_Wednesday = "3";
	/** Thursday = 4 */
	public static final String DAYOFWEEK_Thursday = "4";
	/** Friday = 5 */
	public static final String DAYOFWEEK_Friday = "5";
	/** Saturday = 6 */
	public static final String DAYOFWEEK_Saturday = "6";
	/** Set dayofweek.
		@param dayofweek dayofweek	  */
	public void setdayofweek (String dayofweek)
	{

		set_Value (COLUMNNAME_dayofweek, dayofweek);
	}

	/** Get dayofweek.
		@return dayofweek	  */
	public String getdayofweek () 
	{
		return (String)get_Value(COLUMNNAME_dayofweek);
	}
}
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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for AMN_Payroll_Assist
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Payroll_Assist extends PO implements I_AMN_Payroll_Assist, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180717L;

    /** Standard Constructor */
    public X_AMN_Payroll_Assist (Properties ctx, int AMN_Payroll_Assist_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Assist_ID, trxName);
      /** if (AMN_Payroll_Assist_ID == 0)
        {
			setAMN_AssistRecord (null);
			setAMN_Employee_ID (0);
// @AMN_Employee_ID@
			setAMN_Payroll_Assist_ID (0);
			setAMN_Payroll_Assist_UU (null);
			setAMN_Shift_ID (0);
			setDescanso (false);
// N
			setEvent_Date (new Timestamp( System.currentTimeMillis() ));
			setEvent_Type (null);
			setdayofweek (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Assist (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Payroll_Assist[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AMN_AssistRecord.
		@param AMN_AssistRecord AMN_AssistRecord	  */
	public void setAMN_AssistRecord (String AMN_AssistRecord)
	{
		set_Value (COLUMNNAME_AMN_AssistRecord, AMN_AssistRecord);
	}

	/** Get AMN_AssistRecord.
		@return AMN_AssistRecord	  */
	public String getAMN_AssistRecord () 
	{
		return (String)get_Value(COLUMNNAME_AMN_AssistRecord);
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

	/** Set Payroll Assist .
		@param AMN_Payroll_Assist_ID 
		Payroll Attendance 
	  */
	public void setAMN_Payroll_Assist_ID (int AMN_Payroll_Assist_ID)
	{
		if (AMN_Payroll_Assist_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_ID, Integer.valueOf(AMN_Payroll_Assist_ID));
	}

	/** Get Payroll Assist .
		@return Payroll Attendance 
	  */
	public int getAMN_Payroll_Assist_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Assist_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Assist_UU.
		@param AMN_Payroll_Assist_UU 
		Payroll Attendance UU
	  */
	public void setAMN_Payroll_Assist_UU (String AMN_Payroll_Assist_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Assist_UU, AMN_Payroll_Assist_UU);
	}

	/** Get AMN_Payroll_Assist_UU.
		@return Payroll Attendance UU
	  */
	public String getAMN_Payroll_Assist_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Assist_UU);
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

	/** Set Event Time.
		@param Event_Time Event Time	  */
	public void setEvent_Time (Timestamp Event_Time)
	{
		set_Value (COLUMNNAME_Event_Time, Event_Time);
	}

	/** Get Event Time.
		@return Event Time	  */
	public Timestamp getEvent_Time () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Event_Time);
	}

	/** Input = I */
	public static final String EVENT_TYPE_Input = "I";
	/** Output = O */
	public static final String EVENT_TYPE_Output = "O";
	/** Set Event Type.
		@param Event_Type Event Type	  */
	public void setEvent_Type (String Event_Type)
	{

		set_Value (COLUMNNAME_Event_Type, Event_Type);
	}

	/** Get Event Type.
		@return Event Type	  */
	public String getEvent_Type () 
	{
		return (String)get_Value(COLUMNNAME_Event_Type);
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
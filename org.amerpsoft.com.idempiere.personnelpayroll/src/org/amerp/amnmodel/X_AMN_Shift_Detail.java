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
import org.compiere.util.KeyNamePair;

/** Generated Model for AMN_Shift_Detail
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Shift_Detail")
public class X_AMN_Shift_Detail extends PO implements I_AMN_Shift_Detail, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241224L;

    /** Standard Constructor */
    public X_AMN_Shift_Detail (Properties ctx, int AMN_Shift_Detail_ID, String trxName)
    {
      super (ctx, AMN_Shift_Detail_ID, trxName);
      /** if (AMN_Shift_Detail_ID == 0)
        {
			setAMN_Shift_Detail_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setisFlexible (false);
// N
			setisNextDay (false);
// N
			setisOutTimeFlexible (false);
// N
			setisShiftOffset (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Shift_Detail (Properties ctx, int AMN_Shift_Detail_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Shift_Detail_ID, trxName, virtualColumns);
      /** if (AMN_Shift_Detail_ID == 0)
        {
			setAMN_Shift_Detail_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setisFlexible (false);
// N
			setisNextDay (false);
// N
			setisOutTimeFlexible (false);
// N
			setisShiftOffset (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Shift_Detail (Properties ctx, String AMN_Shift_Detail_UU, String trxName)
    {
      super (ctx, AMN_Shift_Detail_UU, trxName);
      /** if (AMN_Shift_Detail_UU == null)
        {
			setAMN_Shift_Detail_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setisFlexible (false);
// N
			setisNextDay (false);
// N
			setisOutTimeFlexible (false);
// N
			setisShiftOffset (false);
// N
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Shift_Detail (Properties ctx, String AMN_Shift_Detail_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Shift_Detail_UU, trxName, virtualColumns);
      /** if (AMN_Shift_Detail_UU == null)
        {
			setAMN_Shift_Detail_ID (0);
			setAMN_Shift_ID (0);
			setdayofweek (null);
			setDescanso (false);
// N
			setisFlexible (false);
// N
			setisNextDay (false);
// N
			setisOutTimeFlexible (false);
// N
			setisShiftOffset (false);
// N
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Shift_Detail (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client
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
      StringBuilder sb = new StringBuilder ("X_AMN_Shift_Detail[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Shift Detail.
		@param AMN_Shift_Detail_ID Shift Detail
	*/
	public void setAMN_Shift_Detail_ID (int AMN_Shift_Detail_ID)
	{
		if (AMN_Shift_Detail_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Shift_Detail_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Shift_Detail_ID, Integer.valueOf(AMN_Shift_Detail_ID));
	}

	/** Get Shift Detail.
		@return Shift Detail	  */
	public int getAMN_Shift_Detail_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Shift_Detail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Shift_Detail_UU.
		@param AMN_Shift_Detail_UU AMN_Shift_Detail_UU
	*/
	public void setAMN_Shift_Detail_UU (String AMN_Shift_Detail_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Shift_Detail_UU, AMN_Shift_Detail_UU);
	}

	/** Get AMN_Shift_Detail_UU.
		@return AMN_Shift_Detail_UU	  */
	public String getAMN_Shift_Detail_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Shift_Detail_UU);
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

	/** Set Break Minutes.
		@param BreakMinutes Break time in Minutes
	*/
	public void setBreakMinutes (int BreakMinutes)
	{
		set_Value (COLUMNNAME_BreakMinutes, Integer.valueOf(BreakMinutes));
	}

	/** Get Break Minutes.
		@return Break time in Minutes
	  */
	public int getBreakMinutes()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_BreakMinutes);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Break Start.
		@param BreakStart Break start time
	*/
	public void setBreakStart (Timestamp BreakStart)
	{
		set_Value (COLUMNNAME_BreakStart, BreakStart);
	}

	/** Get Break Start.
		@return Break start time
	  */
	public Timestamp getBreakStart()
	{
		return (Timestamp)get_Value(COLUMNNAME_BreakStart);
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

	/** Set Entry Time.
		@param EntryTime Entry Time
	*/
	public void setEntryTime (Timestamp EntryTime)
	{
		set_Value (COLUMNNAME_EntryTime, EntryTime);
	}

	/** Get Entry Time.
		@return Entry Time
	  */
	public Timestamp getEntryTime()
	{
		return (Timestamp)get_Value(COLUMNNAME_EntryTime);
	}

	/** Set Flexible.
		@param isFlexible Indicates if it is Flexible
	*/
	public void setisFlexible (boolean isFlexible)
	{
		set_Value (COLUMNNAME_isFlexible, Boolean.valueOf(isFlexible));
	}

	/** Get Flexible.
		@return Indicates if it is Flexible
	  */
	public boolean isFlexible()
	{
		Object oo = get_Value(COLUMNNAME_isFlexible);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Next Day.
		@param isNextDay Indicates if it is Next Day
	*/
	public void setisNextDay (boolean isNextDay)
	{
		set_Value (COLUMNNAME_isNextDay, Boolean.valueOf(isNextDay));
	}

	/** Get Next Day.
		@return Indicates if it is Next Day
	  */
	public boolean isNextDay()
	{
		Object oo = get_Value(COLUMNNAME_isNextDay);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Out Time Flexible.
		@param isOutTimeFlexible Indicates if it is Out Time Flexible
	*/
	public void setisOutTimeFlexible (boolean isOutTimeFlexible)
	{
		set_Value (COLUMNNAME_isOutTimeFlexible, Boolean.valueOf(isOutTimeFlexible));
	}

	/** Get Out Time Flexible.
		@return Indicates if it is Out Time Flexible
	  */
	public boolean isOutTimeFlexible()
	{
		Object oo = get_Value(COLUMNNAME_isOutTimeFlexible);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Shift Offset.
		@param isShiftOffset Indicates if it is Shift Offset
	*/
	public void setisShiftOffset (boolean isShiftOffset)
	{
		set_Value (COLUMNNAME_isShiftOffset, Boolean.valueOf(isShiftOffset));
	}

	/** Get Shift Offset.
		@return Indicates if it is Shift Offset
	  */
	public boolean isShiftOffset()
	{
		Object oo = get_Value(COLUMNNAME_isShiftOffset);
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

	/** Set Shift_In1.
		@param Shift_In1 Shift_In1
	*/
	public void setShift_In1 (Timestamp Shift_In1)
	{
		set_Value (COLUMNNAME_Shift_In1, Shift_In1);
	}

	/** Get Shift_In1.
		@return Shift_In1	  */
	public Timestamp getShift_In1()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_In1);
	}

	/** Set Shift_In2.
		@param Shift_In2 Shift_In2
	*/
	public void setShift_In2 (Timestamp Shift_In2)
	{
		set_Value (COLUMNNAME_Shift_In2, Shift_In2);
	}

	/** Get Shift_In2.
		@return Shift_In2	  */
	public Timestamp getShift_In2()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_In2);
	}

	/** Set Shift Offset ET Value.
		@param ShiftOffsetET Indicates Shift Offset ET Value
	*/
	public void setShiftOffsetET (int ShiftOffsetET)
	{
		set_Value (COLUMNNAME_ShiftOffsetET, Integer.valueOf(ShiftOffsetET));
	}

	/** Get Shift Offset ET Value.
		@return Indicates Shift Offset ET Value
	  */
	public int getShiftOffsetET()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ShiftOffsetET);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Shift Offset OT Value.
		@param ShiftOffsetOT Indicates Shift Offset OT Value
	*/
	public void setShiftOffsetOT (int ShiftOffsetOT)
	{
		set_Value (COLUMNNAME_ShiftOffsetOT, Integer.valueOf(ShiftOffsetOT));
	}

	/** Get Shift Offset OT Value.
		@return Indicates Shift Offset OT Value
	  */
	public int getShiftOffsetOT()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ShiftOffsetOT);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Shift_Out1.
		@param Shift_Out1 Shift_Out1
	*/
	public void setShift_Out1 (Timestamp Shift_Out1)
	{
		set_Value (COLUMNNAME_Shift_Out1, Shift_Out1);
	}

	/** Get Shift_Out1.
		@return Shift_Out1	  */
	public Timestamp getShift_Out1()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_Out1);
	}

	/** Set Shift_Out2.
		@param Shift_Out2 Shift_Out2
	*/
	public void setShift_Out2 (Timestamp Shift_Out2)
	{
		set_Value (COLUMNNAME_Shift_Out2, Shift_Out2);
	}

	/** Get Shift_Out2.
		@return Shift_Out2	  */
	public Timestamp getShift_Out2()
	{
		return (Timestamp)get_Value(COLUMNNAME_Shift_Out2);
	}

	/** Set Time Out.
		@param TimeOut Shift Time Out
	*/
	public void setTimeOut (Timestamp TimeOut)
	{
		set_Value (COLUMNNAME_TimeOut, TimeOut);
	}

	/** Get Time Out.
		@return Shift Time Out
	  */
	public Timestamp getTimeOut()
	{
		return (Timestamp)get_Value(COLUMNNAME_TimeOut);
	}

	/** Set Tolerance.
		@param Tolerance Tolerance in minutes
	*/
	public void setTolerance (int Tolerance)
	{
		set_Value (COLUMNNAME_Tolerance, Integer.valueOf(Tolerance));
	}

	/** Get Tolerance.
		@return Tolerance in minutes
	  */
	public int getTolerance()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Tolerance);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Valid from.
		@param ValidFrom Valid from including this date (first day)
	*/
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom()
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo Valid to including this date (last day)
	*/
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo()
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
	}
}
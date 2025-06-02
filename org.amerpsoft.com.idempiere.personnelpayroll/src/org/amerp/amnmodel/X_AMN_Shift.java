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

/** Generated Model for AMN_Shift
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Shift")
public class X_AMN_Shift extends PO implements I_AMN_Shift, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241228L;

    /** Standard Constructor */
    public X_AMN_Shift (Properties ctx, int AMN_Shift_ID, String trxName)
    {
      super (ctx, AMN_Shift_ID, trxName);
      /** if (AMN_Shift_ID == 0)
        {
			setAMN_Shift_ID (0);
			setIsDefault (false);
// N
			setisEntryTimeLimit (false);
// N
			setisFlexible (false);
// N
			setisFlotingShift (false);
// N
			setisOverhead (false);
// N
			setisOverheadLimit (false);
// N
			setName (null);
			setTimeControl (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Shift (Properties ctx, int AMN_Shift_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Shift_ID, trxName, virtualColumns);
      /** if (AMN_Shift_ID == 0)
        {
			setAMN_Shift_ID (0);
			setIsDefault (false);
// N
			setisEntryTimeLimit (false);
// N
			setisFlexible (false);
// N
			setisFlotingShift (false);
// N
			setisOverhead (false);
// N
			setisOverheadLimit (false);
// N
			setName (null);
			setTimeControl (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Shift (Properties ctx, String AMN_Shift_UU, String trxName)
    {
      super (ctx, AMN_Shift_UU, trxName);
      /** if (AMN_Shift_UU == null)
        {
			setAMN_Shift_ID (0);
			setIsDefault (false);
// N
			setisEntryTimeLimit (false);
// N
			setisFlexible (false);
// N
			setisFlotingShift (false);
// N
			setisOverhead (false);
// N
			setisOverheadLimit (false);
// N
			setName (null);
			setTimeControl (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_AMN_Shift (Properties ctx, String AMN_Shift_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Shift_UU, trxName, virtualColumns);
      /** if (AMN_Shift_UU == null)
        {
			setAMN_Shift_ID (0);
			setIsDefault (false);
// N
			setisEntryTimeLimit (false);
// N
			setisFlexible (false);
// N
			setisFlotingShift (false);
// N
			setisOverhead (false);
// N
			setisOverheadLimit (false);
// N
			setName (null);
			setTimeControl (false);
// N
        } */
    }

    /** Load Constructor */
    public X_AMN_Shift (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Shift[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Shift.
		@param AMN_Shift_ID Shift
	*/
	public void setAMN_Shift_ID (int AMN_Shift_ID)
	{
		if (AMN_Shift_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Shift_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Shift_ID, Integer.valueOf(AMN_Shift_ID));
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

	/** Set AMN_Shift_UU.
		@param AMN_Shift_UU AMN_Shift_UU
	*/
	public void setAMN_Shift_UU (String AMN_Shift_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Shift_UU, AMN_Shift_UU);
	}

	/** Get AMN_Shift_UU.
		@return AMN_Shift_UU	  */
	public String getAMN_Shift_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Shift_UU);
	}

	/** Set Overhead Index ID.
		@param C_Overheadindex_ID Overhead Index ID
	*/
	public void setC_Overheadindex_ID (int C_Overheadindex_ID)
	{
		if (C_Overheadindex_ID < 1)
			set_Value (COLUMNNAME_C_Overheadindex_ID, null);
		else
			set_Value (COLUMNNAME_C_Overheadindex_ID, Integer.valueOf(C_Overheadindex_ID));
	}

	/** Get Overhead Index ID.
		@return Overhead Index ID	  */
	public int getC_Overheadindex_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Overheadindex_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set HEFranquicia.
		@param HEFranquicia Extra Hours Franquicia
	*/
	public void setHEFranquicia (BigDecimal HEFranquicia)
	{
		set_Value (COLUMNNAME_HEFranquicia, HEFranquicia);
	}

	/** Get HEFranquicia.
		@return Extra Hours Franquicia
	  */
	public BigDecimal getHEFranquicia()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HEFranquicia);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Comment/Help.
		@param Help Comment or Hint
	*/
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp()
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set Hours per day in a Shift.
		@param HoursDay Hours per day in a Shift
	*/
	public void setHoursDay (BigDecimal HoursDay)
	{
		set_Value (COLUMNNAME_HoursDay, HoursDay);
	}

	/** Get Hours per day in a Shift.
		@return Hours per day in a Shift	  */
	public BigDecimal getHoursDay()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HoursDay);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Hours of work per week.
		@param HoursWeek Hours of work per week
	*/
	public void setHoursWeek (BigDecimal HoursWeek)
	{
		set_Value (COLUMNNAME_HoursWeek, HoursWeek);
	}

	/** Get Hours of work per week.
		@return Hours of work per week
	  */
	public BigDecimal getHoursWeek()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_HoursWeek);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Default.
		@param IsDefault Default value
	*/
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	public boolean isDefault()
	{
		Object oo = get_Value(COLUMNNAME_IsDefault);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Entry Time Limit.
		@param isEntryTimeLimit Entry Time Limit
	*/
	public void setisEntryTimeLimit (boolean isEntryTimeLimit)
	{
		set_Value (COLUMNNAME_isEntryTimeLimit, Boolean.valueOf(isEntryTimeLimit));
	}

	/** Get Entry Time Limit.
		@return Entry Time Limit
	  */
	public boolean isEntryTimeLimit()
	{
		Object oo = get_Value(COLUMNNAME_isEntryTimeLimit);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Floting Shift.
		@param isFlotingShift Indicates if it is Floting Shift
	*/
	public void setisFlotingShift (boolean isFlotingShift)
	{
		set_Value (COLUMNNAME_isFlotingShift, Boolean.valueOf(isFlotingShift));
	}

	/** Get Floting Shift.
		@return Indicates if it is Floting Shift
	  */
	public boolean isFlotingShift()
	{
		Object oo = get_Value(COLUMNNAME_isFlotingShift);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Overhead.
		@param isOverhead Indicates if it is Overhead
	*/
	public void setisOverhead (boolean isOverhead)
	{
		set_Value (COLUMNNAME_isOverhead, Boolean.valueOf(isOverhead));
	}

	/** Get Overhead.
		@return Indicates if it is Overhead
	  */
	public boolean isOverhead()
	{
		Object oo = get_Value(COLUMNNAME_isOverhead);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Overhead Limit.
		@param isOverheadLimit Indicates if it is Overhead Limit
	*/
	public void setisOverheadLimit (boolean isOverheadLimit)
	{
		set_Value (COLUMNNAME_isOverheadLimit, Boolean.valueOf(isOverheadLimit));
	}

	/** Get Overhead Limit.
		@return Indicates if it is Overhead Limit
	  */
	public boolean isOverheadLimit()
	{
		Object oo = get_Value(COLUMNNAME_isOverheadLimit);
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

	/** Set Overhead Limit Value.
		@param OverheadLimit Overhead Limit Value
	*/
	public void setOverheadLimit (BigDecimal OverheadLimit)
	{
		set_Value (COLUMNNAME_OverheadLimit, OverheadLimit);
	}

	/** Get Overhead Limit Value.
		@return Overhead Limit Value
	  */
	public BigDecimal getOverheadLimit()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_OverheadLimit);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set TimeControl.
		@param TimeControl Indicates if it is Time Controlled
	*/
	public void setTimeControl (boolean TimeControl)
	{
		set_Value (COLUMNNAME_TimeControl, Boolean.valueOf(TimeControl));
	}

	/** Get TimeControl.
		@return Indicates if it is Time Controlled
	  */
	public boolean isTimeControl()
	{
		Object oo = get_Value(COLUMNNAME_TimeControl);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
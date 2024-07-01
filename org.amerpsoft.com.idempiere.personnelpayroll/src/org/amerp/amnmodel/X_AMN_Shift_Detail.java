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
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Shift_Detail extends PO implements I_AMN_Shift_Detail, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151222L;

    /** Standard Constructor */
    public X_AMN_Shift_Detail (Properties ctx, int AMN_Shift_Detail_ID, String trxName)
    {
      super (ctx, AMN_Shift_Detail_ID, trxName);
      /** if (AMN_Shift_Detail_ID == 0)
        {
			setAMN_Shift_Detail_ID (0);
			setAMN_Shift_ID (0);
			setName (null);
			setdayofweek (null);
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
      StringBuffer sb = new StringBuffer ("X_AMN_Shift_Detail[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Shift Detail.
		@param AMN_Shift_Detail_ID Shift Detail	  */
	public void setAMN_Shift_Detail_ID (int AMN_Shift_Detail_ID)
	{
		if (AMN_Shift_Detail_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Shift_Detail_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Shift_Detail_ID, Integer.valueOf(AMN_Shift_Detail_ID));
	}

	/** Get Shift Detail.
		@return Shift Detail	  */
	public int getAMN_Shift_Detail_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Shift_Detail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Shift_Detail_UU.
		@param AMN_Shift_Detail_UU AMN_Shift_Detail_UU	  */
	public void setAMN_Shift_Detail_UU (String AMN_Shift_Detail_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Shift_Detail_UU, AMN_Shift_Detail_UU);
	}

	/** Get AMN_Shift_Detail_UU.
		@return AMN_Shift_Detail_UU	  */
	public String getAMN_Shift_Detail_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Shift_Detail_UU);
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
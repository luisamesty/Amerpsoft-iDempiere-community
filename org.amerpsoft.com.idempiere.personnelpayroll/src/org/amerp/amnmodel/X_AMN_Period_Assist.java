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

/** Generated Model for AMN_Period_Assist
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Period_Assist extends PO implements I_AMN_Period_Assist, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Period_Assist (Properties ctx, int AMN_Period_Assist_ID, String trxName)
    {
      super (ctx, AMN_Period_Assist_ID, trxName);
      /** if (AMN_Period_Assist_ID == 0)
        {
			setAMN_Period_Assist_ID (0);
			setAMN_Period_Assist_Status (false);
// N
			setAMN_Period_Assist_UU (null);
			setC_Period_ID (0);
			setC_Year_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Period_Assist (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Period_Assist[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Assist Date.
		@param AMNDateAssist Assist Date	  */
	public void setAMNDateAssist (Timestamp AMNDateAssist)
	{
		set_Value (COLUMNNAME_AMNDateAssist, AMNDateAssist);
	}

	/** Get Assist Date.
		@return Assist Date	  */
	public Timestamp getAMNDateAssist () 
	{
		return (Timestamp)get_Value(COLUMNNAME_AMNDateAssist);
	}

	/** Set Payroll Period Assist.
		@param AMN_Period_Assist_ID Payroll Period Assist	  */
	public void setAMN_Period_Assist_ID (int AMN_Period_Assist_ID)
	{
		if (AMN_Period_Assist_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Period_Assist_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Period_Assist_ID, Integer.valueOf(AMN_Period_Assist_ID));
	}

	/** Get Payroll Period Assist.
		@return Payroll Period Assist	  */
	public int getAMN_Period_Assist_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Period_Assist_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Period Assist Status.
		@param AMN_Period_Assist_Status Period Assist Status	  */
	public void setAMN_Period_Assist_Status (boolean AMN_Period_Assist_Status)
	{
		set_Value (COLUMNNAME_AMN_Period_Assist_Status, Boolean.valueOf(AMN_Period_Assist_Status));
	}

	/** Get Period Assist Status.
		@return Period Assist Status	  */
	public boolean isAMN_Period_Assist_Status () 
	{
		Object oo = get_Value(COLUMNNAME_AMN_Period_Assist_Status);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AMN_Period_Assist_UU.
		@param AMN_Period_Assist_UU AMN_Period_Assist_UU	  */
	public void setAMN_Period_Assist_UU (String AMN_Period_Assist_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Period_Assist_UU, AMN_Period_Assist_UU);
	}

	/** Get AMN_Period_Assist_UU.
		@return AMN_Period_Assist_UU	  */
	public String getAMN_Period_Assist_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Period_Assist_UU);
	}

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Year getC_Year() throws RuntimeException
    {
		return (org.compiere.model.I_C_Year)MTable.get(getCtx(), org.compiere.model.I_C_Year.Table_Name)
			.getPO(getC_Year_ID(), get_TrxName());	}

	/** Set Year.
		@param C_Year_ID 
		Calendar Year
	  */
	public void setC_Year_ID (int C_Year_ID)
	{
		if (C_Year_ID < 1) 
			set_Value (COLUMNNAME_C_Year_ID, null);
		else 
			set_Value (COLUMNNAME_C_Year_ID, Integer.valueOf(C_Year_ID));
	}

	/** Get Year.
		@return Calendar Year
	  */
	public int getC_Year_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Year_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Non Business Day.
		@param IsNonBusinessDay Non Business Day	  */
	public void setIsNonBusinessDay (boolean IsNonBusinessDay)
	{
		set_Value (COLUMNNAME_IsNonBusinessDay, Boolean.valueOf(IsNonBusinessDay));
	}

	/** Get Non Business Day.
		@return Non Business Day	  */
	public boolean isNonBusinessDay () 
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
}
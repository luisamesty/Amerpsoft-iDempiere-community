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

/** Generated Model for AMN_Payroll_Lot
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Payroll_Lot extends PO implements I_AMN_Payroll_Lot, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180717L;

    /** Standard Constructor */
    public X_AMN_Payroll_Lot (Properties ctx, int AMN_Payroll_Lot_ID, String trxName)
    {
      super (ctx, AMN_Payroll_Lot_ID, trxName);
      /** if (AMN_Payroll_Lot_ID == 0)
        {
			setAMN_Payroll_Lot_ID (0);
			setC_Period_ID (0);
			setC_Year_ID (0);
			setDateAcct (new Timestamp( System.currentTimeMillis() ));
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Payroll_Lot (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Payroll_Lot[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Receipt Lot.
		@param AMN_Payroll_Lot_ID Payroll Receipt Lot	  */
	public void setAMN_Payroll_Lot_ID (int AMN_Payroll_Lot_ID)
	{
		if (AMN_Payroll_Lot_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Lot_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Lot_ID, Integer.valueOf(AMN_Payroll_Lot_ID));
	}

	/** Get Payroll Receipt Lot.
		@return Payroll Receipt Lot	  */
	public int getAMN_Payroll_Lot_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Payroll_Lot_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Payroll_Lot_UU.
		@param AMN_Payroll_Lot_UU AMN_Payroll_Lot_UU	  */
	public void setAMN_Payroll_Lot_UU (String AMN_Payroll_Lot_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Payroll_Lot_UU, AMN_Payroll_Lot_UU);
	}

	/** Get AMN_Payroll_Lot_UU.
		@return AMN_Payroll_Lot_UU	  */
	public String getAMN_Payroll_Lot_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Payroll_Lot_UU);
	}

	public I_AMN_Process getAMN_Process() throws RuntimeException
    {
		return (I_AMN_Process)MTable.get(getCtx(), I_AMN_Process.Table_Name)
			.getPO(getAMN_Process_ID(), get_TrxName());	}

	/** Set Payroll Process.
		@param AMN_Process_ID Payroll Process	  */
	public void setAMN_Process_ID (int AMN_Process_ID)
	{
		if (AMN_Process_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Process_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Process_ID, Integer.valueOf(AMN_Process_ID));
	}

	/** Get Payroll Process.
		@return Payroll Process	  */
	public int getAMN_Process_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
			set_ValueNoCheck (COLUMNNAME_C_Year_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Year_ID, Integer.valueOf(C_Year_ID));
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

	/** Set Account Date.
		@param DateAcct 
		Accounting Date
	  */
	public void setDateAcct (Timestamp DateAcct)
	{
		set_Value (COLUMNNAME_DateAcct, DateAcct);
	}

	/** Get Account Date.
		@return Accounting Date
	  */
	public Timestamp getDateAcct () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateAcct);
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_ValueNoCheck (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
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

/** Generated Model for AMN_Period
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Period extends PO implements I_AMN_Period, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Period (Properties ctx, int AMN_Period_ID, String trxName)
    {
      super (ctx, AMN_Period_ID, trxName);
      /** if (AMN_Period_ID == 0)
        {
			setAMNDateEnd (new Timestamp( System.currentTimeMillis() ));
			setAMNDateIni (new Timestamp( System.currentTimeMillis() ));
			setAMN_Contract_ID (0);
			setAMN_Period_ID (0);
			setAMN_Period_Status (null);
			setAMN_Period_UU (null);
			setAMN_Process_ID (0);
			setC_Period_ID (0);
			setC_Year_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Period (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Period[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AMNDateEnd.
		@param AMNDateEnd AMNDateEnd	  */
	public void setAMNDateEnd (Timestamp AMNDateEnd)
	{
		set_Value (COLUMNNAME_AMNDateEnd, AMNDateEnd);
	}

	/** Get AMNDateEnd.
		@return AMNDateEnd	  */
	public Timestamp getAMNDateEnd () 
	{
		return (Timestamp)get_Value(COLUMNNAME_AMNDateEnd);
	}

	/** Set AMNDateIni.
		@param AMNDateIni AMNDateIni	  */
	public void setAMNDateIni (Timestamp AMNDateIni)
	{
		set_Value (COLUMNNAME_AMNDateIni, AMNDateIni);
	}

	/** Get AMNDateIni.
		@return AMNDateIni	  */
	public Timestamp getAMNDateIni () 
	{
		return (Timestamp)get_Value(COLUMNNAME_AMNDateIni);
	}

	/** Set Payroll Contract.
		@param AMN_Contract_ID Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID)
	{
		if (AMN_Contract_ID < 1) 
			set_Value (COLUMNNAME_AMN_Contract_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Contract_ID, Integer.valueOf(AMN_Contract_ID));
	}

	/** Get Payroll Contract.
		@return Payroll Contract	  */
	public int getAMN_Contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Period.
		@param AMN_Period_ID Payroll Period	  */
	public void setAMN_Period_ID (int AMN_Period_ID)
	{
		if (AMN_Period_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Period_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Period_ID, Integer.valueOf(AMN_Period_ID));
	}

	/** Get Payroll Period.
		@return Payroll Period	  */
	public int getAMN_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Yes  = Y */
	public static final String AMN_PERIOD_PROCESSED_Yes = "Y";
	/** No = N */
	public static final String AMN_PERIOD_PROCESSED_No = "N";
	/** Set Period Processed.
		@param AMN_Period_Processed Period Processed	  */
	public void setAMN_Period_Processed (String AMN_Period_Processed)
	{

		set_Value (COLUMNNAME_AMN_Period_Processed, AMN_Period_Processed);
	}

	/** Get Period Processed.
		@return Period Processed	  */
	public String getAMN_Period_Processed () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Period_Processed);
	}

	/** Closed Period = C */
	public static final String AMN_PERIOD_STATUS_ClosedPeriod = "C";
	/** Open Period = O */
	public static final String AMN_PERIOD_STATUS_OpenPeriod = "O";
	/** Set Period Status.
		@param AMN_Period_Status Period Status	  */
	public void setAMN_Period_Status (String AMN_Period_Status)
	{

		set_Value (COLUMNNAME_AMN_Period_Status, AMN_Period_Status);
	}

	/** Get Period Status.
		@return Period Status	  */
	public String getAMN_Period_Status () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Period_Status);
	}

	/** Set AMN_Period_UU.
		@param AMN_Period_UU AMN_Period_UU	  */
	public void setAMN_Period_UU (String AMN_Period_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Period_UU, AMN_Period_UU);
	}

	/** Get AMN_Period_UU.
		@return AMN_Period_UU	  */
	public String getAMN_Period_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Period_UU);
	}

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
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
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

	/** Set RefDateEnd.
		@param RefDateEnd RefDateEnd	  */
	public void setRefDateEnd (Timestamp RefDateEnd)
	{
		set_Value (COLUMNNAME_RefDateEnd, RefDateEnd);
	}

	/** Get RefDateEnd.
		@return RefDateEnd	  */
	public Timestamp getRefDateEnd () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RefDateEnd);
	}

	/** Set RefDateIni.
		@param RefDateIni RefDateIni	  */
	public void setRefDateIni (Timestamp RefDateIni)
	{
		set_Value (COLUMNNAME_RefDateIni, RefDateIni);
	}

	/** Get RefDateIni.
		@return RefDateIni	  */
	public Timestamp getRefDateIni () 
	{
		return (Timestamp)get_Value(COLUMNNAME_RefDateIni);
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
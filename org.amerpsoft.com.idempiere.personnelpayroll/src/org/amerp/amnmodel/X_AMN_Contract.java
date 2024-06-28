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

/** Generated Model for AMN_Contract
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Contract extends PO implements I_AMN_Contract, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20180703L;

    /** Standard Constructor */
    public X_AMN_Contract (Properties ctx, int AMN_Contract_ID, String trxName)
    {
      super (ctx, AMN_Contract_ID, trxName);
      /** if (AMN_Contract_ID == 0)
        {
			setAMN_Contract_ID (0);
			setAcctDow (null);
// 0
			setInitDow (null);
// 5
			setName (null);
			setNetDays (0);
        } */
    }

    /** Load Constructor */
    public X_AMN_Contract (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Contract[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Contract.
		@param AMN_Contract_ID Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID)
	{
		if (AMN_Contract_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Contract_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Contract_ID, Integer.valueOf(AMN_Contract_ID));
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

	/** Set AMN_Contract_UU.
		@param AMN_Contract_UU AMN_Contract_UU	  */
	public void setAMN_Contract_UU (String AMN_Contract_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Contract_UU, AMN_Contract_UU);
	}

	/** Get AMN_Contract_UU.
		@return AMN_Contract_UU	  */
	public String getAMN_Contract_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Contract_UU);
	}

	/** Sunday = 0 */
	public static final String ACCTDOW_Sunday = "0";
	/** Monday = 1 */
	public static final String ACCTDOW_Monday = "1";
	/** Tuesday = 2 */
	public static final String ACCTDOW_Tuesday = "2";
	/** Wednesday = 3 */
	public static final String ACCTDOW_Wednesday = "3";
	/** Thursday = 4 */
	public static final String ACCTDOW_Thursday = "4";
	/** Friday = 5 */
	public static final String ACCTDOW_Friday = "5";
	/** Saturday = 6 */
	public static final String ACCTDOW_Saturday = "6";
	/** Set AcctDow.
		@param AcctDow 
		Accounting Day of Week
	  */
	public void setAcctDow (String AcctDow)
	{

		set_Value (COLUMNNAME_AcctDow, AcctDow);
	}

	/** Get AcctDow.
		@return Accounting Day of Week
	  */
	public String getAcctDow () 
	{
		return (String)get_Value(COLUMNNAME_AcctDow);
	}

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException
    {
		return (org.compiere.model.I_C_Activity)MTable.get(getCtx(), org.compiere.model.I_C_Activity.Table_Name)
			.getPO(getC_Activity_ID(), get_TrxName());	}

	/** Set Activity.
		@param C_Activity_ID 
		Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID)
	{
		if (C_Activity_ID < 1) 
			set_Value (COLUMNNAME_C_Activity_ID, null);
		else 
			set_Value (COLUMNNAME_C_Activity_ID, Integer.valueOf(C_Activity_ID));
	}

	/** Get Activity.
		@return Business Activity
	  */
	public int getC_Activity_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Activity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
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

	/** Sunday = 0 */
	public static final String INITDOW_Sunday = "0";
	/** Monday = 1 */
	public static final String INITDOW_Monday = "1";
	/** Tuesday = 2 */
	public static final String INITDOW_Tuesday = "2";
	/** Wednesday = 3 */
	public static final String INITDOW_Wednesday = "3";
	/** Thursday = 4 */
	public static final String INITDOW_Thursday = "4";
	/** Friday = 5 */
	public static final String INITDOW_Friday = "5";
	/** Saturday = 6 */
	public static final String INITDOW_Saturday = "6";
	/** Set InitDow.
		@param InitDow 
		Init Day of Week
	  */
	public void setInitDow (String InitDow)
	{

		set_Value (COLUMNNAME_InitDow, InitDow);
	}

	/** Get InitDow.
		@return Init Day of Week
	  */
	public String getInitDow () 
	{
		return (String)get_Value(COLUMNNAME_InitDow);
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

	/** Set Net Days.
		@param NetDays 
		Net Days in which payment is due
	  */
	public void setNetDays (int NetDays)
	{
		set_Value (COLUMNNAME_NetDays, Integer.valueOf(NetDays));
	}

	/** Get Net Days.
		@return Net Days in which payment is due
	  */
	public int getNetDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NetDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set PayRollDays.
		@param PayRollDays 
		Payroll Days of Period in Contract
	  */
	public void setPayRollDays (BigDecimal PayRollDays)
	{
		set_Value (COLUMNNAME_PayRollDays, PayRollDays);
	}

	/** Get PayRollDays.
		@return Payroll Days of Period in Contract
	  */
	public BigDecimal getPayRollDays () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PayRollDays);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Slack Days to Calculate Payroll.
		@param SlackDays 
		Minimun days transcurred from the end of Attendance and the End of the Period
	  */
	public void setSlackDays (int SlackDays)
	{
		set_Value (COLUMNNAME_SlackDays, Integer.valueOf(SlackDays));
	}

	/** Get Slack Days to Calculate Payroll.
		@return Minimun days transcurred from the end of Attendance and the End of the Period
	  */
	public int getSlackDays () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SlackDays);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom)
	{
		set_Value (COLUMNNAME_ValidFrom, ValidFrom);
	}

	/** Get Valid from.
		@return Valid from including this date (first day)
	  */
	public Timestamp getValidFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidFrom);
	}

	/** Set Valid to.
		@param ValidTo 
		Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo)
	{
		set_Value (COLUMNNAME_ValidTo, ValidTo);
	}

	/** Get Valid to.
		@return Valid to including this date (last day)
	  */
	public Timestamp getValidTo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ValidTo);
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

	/** Set Allow Lot Invoices.
		@param isAllowLotInvoices Allow Lot Invoices	  */
	public void setisAllowLotInvoices (boolean isAllowLotInvoices)
	{
		set_Value (COLUMNNAME_isAllowLotInvoices, Boolean.valueOf(isAllowLotInvoices));
	}

	/** Get Allow Lot Invoices.
		@return Allow Lot Invoices	  */
	public boolean isAllowLotInvoices () 
	{
		Object oo = get_Value(COLUMNNAME_isAllowLotInvoices);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}
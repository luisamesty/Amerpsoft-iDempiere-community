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

/** Generated Model for AMN_Concept_Types_Limit
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Concept_Types_Limit")
public class X_AMN_Concept_Types_Limit extends PO implements I_AMN_Concept_Types_Limit, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240831L;

    /** Standard Constructor */
    public X_AMN_Concept_Types_Limit (Properties ctx, int AMN_Concept_Types_Limit_ID, String trxName)
    {
      super (ctx, AMN_Concept_Types_Limit_ID, trxName);
      /** if (AMN_Concept_Types_Limit_ID == 0)
        {
			setAMN_Concept_Types_Limit_ID (0);
			setAMN_Concept_Types_Limit_UU (null);
			setAMN_Period_Status (null);
// O
			setC_Currency_ID (0);
			setQtyTimes (Env.ZERO);
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setValidTo (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Standard Constructor */
    public X_AMN_Concept_Types_Limit (Properties ctx, int AMN_Concept_Types_Limit_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Concept_Types_Limit_ID, trxName, virtualColumns);
      /** if (AMN_Concept_Types_Limit_ID == 0)
        {
			setAMN_Concept_Types_Limit_ID (0);
			setAMN_Concept_Types_Limit_UU (null);
			setAMN_Period_Status (null);
// O
			setC_Currency_ID (0);
			setQtyTimes (Env.ZERO);
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setValidTo (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Standard Constructor */
    public X_AMN_Concept_Types_Limit (Properties ctx, String AMN_Concept_Types_Limit_UU, String trxName)
    {
      super (ctx, AMN_Concept_Types_Limit_UU, trxName);
      /** if (AMN_Concept_Types_Limit_UU == null)
        {
			setAMN_Concept_Types_Limit_ID (0);
			setAMN_Concept_Types_Limit_UU (null);
			setAMN_Period_Status (null);
// O
			setC_Currency_ID (0);
			setQtyTimes (Env.ZERO);
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setValidTo (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Standard Constructor */
    public X_AMN_Concept_Types_Limit (Properties ctx, String AMN_Concept_Types_Limit_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Concept_Types_Limit_UU, trxName, virtualColumns);
      /** if (AMN_Concept_Types_Limit_UU == null)
        {
			setAMN_Concept_Types_Limit_ID (0);
			setAMN_Concept_Types_Limit_UU (null);
			setAMN_Period_Status (null);
// O
			setC_Currency_ID (0);
			setQtyTimes (Env.ZERO);
			setRate (Env.ZERO);
			setValidFrom (new Timestamp( System.currentTimeMillis() ));
			setValidTo (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_AMN_Concept_Types_Limit (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Concept_Types_Limit[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AMN_Concept_Types getAMN_Concept_Types() throws RuntimeException
	{
		return (I_AMN_Concept_Types)MTable.get(getCtx(), I_AMN_Concept_Types.Table_ID)
			.getPO(getAMN_Concept_Types_ID(), get_TrxName());
	}

	/** Set Payroll Concepts Types.
		@param AMN_Concept_Types_ID Payroll Concepts Types
	*/
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID)
	{
		if (AMN_Concept_Types_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_ID, Integer.valueOf(AMN_Concept_Types_ID));
	}

	/** Get Payroll Concepts Types.
		@return Payroll Concepts Types	  */
	public int getAMN_Concept_Types_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Concept Types Limits and Dates.
		@param AMN_Concept_Types_Limit_ID Concept Types Limits and Dates
	*/
	public void setAMN_Concept_Types_Limit_ID (int AMN_Concept_Types_Limit_ID)
	{
		if (AMN_Concept_Types_Limit_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Limit_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Limit_ID, Integer.valueOf(AMN_Concept_Types_Limit_ID));
	}

	/** Get Concept Types Limits and Dates.
		@return Concept Types Limits and Dates	  */
	public int getAMN_Concept_Types_Limit_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_Limit_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Concept_Types_Limit_UU.
		@param AMN_Concept_Types_Limit_UU AMN_Concept_Types_Limit_UU
	*/
	public void setAMN_Concept_Types_Limit_UU (String AMN_Concept_Types_Limit_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Limit_UU, AMN_Concept_Types_Limit_UU);
	}

	/** Get AMN_Concept_Types_Limit_UU.
		@return AMN_Concept_Types_Limit_UU	  */
	public String getAMN_Concept_Types_Limit_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Concept_Types_Limit_UU);
	}

	/** Closed Period = C */
	public static final String AMN_PERIOD_STATUS_ClosedPeriod = "C";
	/** Open Period = O */
	public static final String AMN_PERIOD_STATUS_OpenPeriod = "O";
	/** Set Period Status.
		@param AMN_Period_Status Period Status
	*/
	public void setAMN_Period_Status (String AMN_Period_Status)
	{

		set_Value (COLUMNNAME_AMN_Period_Status, AMN_Period_Status);
	}

	/** Get Period Status.
		@return Period Status	  */
	public String getAMN_Period_Status()
	{
		return (String)get_Value(COLUMNNAME_AMN_Period_Status);
	}

	/** Set AmountAllocated.
		@param AmountAllocated Payroll Allocation
	*/
	public void setAmountAllocated (BigDecimal AmountAllocated)
	{
		set_Value (COLUMNNAME_AmountAllocated, AmountAllocated);
	}

	/** Get AmountAllocated.
		@return Payroll Allocation
	  */
	public BigDecimal getAmountAllocated()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountAllocated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException
	{
		return (org.compiere.model.I_C_ConversionType)MTable.get(getCtx(), org.compiere.model.I_C_ConversionType.Table_ID)
			.getPO(getC_ConversionType_ID(), get_TrxName());
	}

	/** Set Currency Type.
		@param C_ConversionType_ID Currency Conversion Rate Type
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
	public int getC_ConversionType_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
	{
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_ID)
			.getPO(getC_Currency_ID(), get_TrxName());
	}

	/** Set Currency.
		@param C_Currency_ID The Currency for this record
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
	public int getC_Currency_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
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

	/** Set Qty Times.
		@param QtyTimes Qty Times
	*/
	public void setQtyTimes (BigDecimal QtyTimes)
	{
		set_Value (COLUMNNAME_QtyTimes, QtyTimes);
	}

	/** Get Qty Times.
		@return Qty Times	  */
	public BigDecimal getQtyTimes()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_QtyTimes);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Rate.
		@param Rate Rate or Tax or Exchange
	*/
	public void setRate (BigDecimal Rate)
	{
		set_Value (COLUMNNAME_Rate, Rate);
	}

	/** Get Rate.
		@return Rate or Tax or Exchange
	  */
	public BigDecimal getRate()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Rate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
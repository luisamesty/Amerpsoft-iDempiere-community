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
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for AMN_Concept_Types_Charge
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Concept_Types_Charge")
public class X_AMN_Concept_Types_Charge extends PO implements I_AMN_Concept_Types_Charge, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20240716L;

    /** Standard Constructor */
    public X_AMN_Concept_Types_Charge (Properties ctx, int AMN_Concept_Types_Charge_ID, String trxName)
    {
      super (ctx, AMN_Concept_Types_Charge_ID, trxName);
      /** if (AMN_Concept_Types_Charge_ID == 0)
        {
			setAMN_Concept_Types_Charge_ID (0);
			setC_Charge_AW_ID (0);
			setC_Charge_DW_ID (0);
			setC_Charge_IW_ID (0);
			setC_Charge_MW_ID (0);
			setC_Charge_SW_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Concept_Types_Charge (Properties ctx, int AMN_Concept_Types_Charge_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Concept_Types_Charge_ID, trxName, virtualColumns);
      /** if (AMN_Concept_Types_Charge_ID == 0)
        {
			setAMN_Concept_Types_Charge_ID (0);
			setC_Charge_AW_ID (0);
			setC_Charge_DW_ID (0);
			setC_Charge_IW_ID (0);
			setC_Charge_MW_ID (0);
			setC_Charge_SW_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Concept_Types_Charge (Properties ctx, String AMN_Concept_Types_Charge_UU, String trxName)
    {
      super (ctx, AMN_Concept_Types_Charge_UU, trxName);
      /** if (AMN_Concept_Types_Charge_UU == null)
        {
			setAMN_Concept_Types_Charge_ID (0);
			setC_Charge_AW_ID (0);
			setC_Charge_DW_ID (0);
			setC_Charge_IW_ID (0);
			setC_Charge_MW_ID (0);
			setC_Charge_SW_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Concept_Types_Charge (Properties ctx, String AMN_Concept_Types_Charge_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Concept_Types_Charge_UU, trxName, virtualColumns);
      /** if (AMN_Concept_Types_Charge_UU == null)
        {
			setAMN_Concept_Types_Charge_ID (0);
			setC_Charge_AW_ID (0);
			setC_Charge_DW_ID (0);
			setC_Charge_IW_ID (0);
			setC_Charge_MW_ID (0);
			setC_Charge_SW_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Concept_Types_Charge (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Concept_Types_Charge[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Payroll  Concept Types Charges.
		@param AMN_Concept_Types_Charge_ID Charges for AMN_Concept_Types
	*/
	public void setAMN_Concept_Types_Charge_ID (int AMN_Concept_Types_Charge_ID)
	{
		if (AMN_Concept_Types_Charge_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Charge_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Concept_Types_Charge_ID, Integer.valueOf(AMN_Concept_Types_Charge_ID));
	}

	/** Get Payroll  Concept Types Charges.
		@return Charges for AMN_Concept_Types
	  */
	public int getAMN_Concept_Types_Charge_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Concept_Types_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Concept_Types_Charge_UU.
		@param AMN_Concept_Types_Charge_UU AMN_Concept_Types_Charge_UU
	*/
	public void setAMN_Concept_Types_Charge_UU (String AMN_Concept_Types_Charge_UU)
	{
		set_Value (COLUMNNAME_AMN_Concept_Types_Charge_UU, AMN_Concept_Types_Charge_UU);
	}

	/** Get AMN_Concept_Types_Charge_UU.
		@return AMN_Concept_Types_Charge_UU	  */
	public String getAMN_Concept_Types_Charge_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Concept_Types_Charge_UU);
	}

	public org.compiere.model.I_C_Charge getC_Charge_AW() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_AW_ID(), get_TrxName());
	}

	/** Set Charge Administrative Work Force .
		@param C_Charge_AW_ID Charge for Administrative Work Force 
	*/
	public void setC_Charge_AW_ID (int C_Charge_AW_ID)
	{
		if (C_Charge_AW_ID < 1)
			set_Value (COLUMNNAME_C_Charge_AW_ID, null);
		else
			set_Value (COLUMNNAME_C_Charge_AW_ID, Integer.valueOf(C_Charge_AW_ID));
	}

	/** Get Charge Administrative Work Force .
		@return Charge for Administrative Work Force 
	  */
	public int getC_Charge_AW_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_AW_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge_DW() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_DW_ID(), get_TrxName());
	}

	/** Set Charge Direct Workforce.
		@param C_Charge_DW_ID Charge for Direct Workforce
	*/
	public void setC_Charge_DW_ID (int C_Charge_DW_ID)
	{
		if (C_Charge_DW_ID < 1)
			set_Value (COLUMNNAME_C_Charge_DW_ID, null);
		else
			set_Value (COLUMNNAME_C_Charge_DW_ID, Integer.valueOf(C_Charge_DW_ID));
	}

	/** Get Charge Direct Workforce.
		@return Charge for Direct Workforce
	  */
	public int getC_Charge_DW_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_DW_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge_IW() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_IW_ID(), get_TrxName());
	}

	/** Set Charge Indirect Work Force.
		@param C_Charge_IW_ID Charge Indirect Work Force
	*/
	public void setC_Charge_IW_ID (int C_Charge_IW_ID)
	{
		if (C_Charge_IW_ID < 1)
			set_Value (COLUMNNAME_C_Charge_IW_ID, null);
		else
			set_Value (COLUMNNAME_C_Charge_IW_ID, Integer.valueOf(C_Charge_IW_ID));
	}

	/** Get Charge Indirect Work Force.
		@return Charge Indirect Work Force
	  */
	public int getC_Charge_IW_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_IW_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge_MW() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_MW_ID(), get_TrxName());
	}

	/** Set Charge Management Work Force .
		@param C_Charge_MW_ID Charge for Management Work Force 
	*/
	public void setC_Charge_MW_ID (int C_Charge_MW_ID)
	{
		if (C_Charge_MW_ID < 1)
			set_Value (COLUMNNAME_C_Charge_MW_ID, null);
		else
			set_Value (COLUMNNAME_C_Charge_MW_ID, Integer.valueOf(C_Charge_MW_ID));
	}

	/** Get Charge Management Work Force .
		@return Charge for Management Work Force 
	  */
	public int getC_Charge_MW_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_MW_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge_SW() throws RuntimeException
	{
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_ID)
			.getPO(getC_Charge_SW_ID(), get_TrxName());
	}

	/** Set Charge Sales Work Force .
		@param C_Charge_SW_ID Charge for Sales Work Force 
	*/
	public void setC_Charge_SW_ID (int C_Charge_SW_ID)
	{
		if (C_Charge_SW_ID < 1)
			set_Value (COLUMNNAME_C_Charge_SW_ID, null);
		else
			set_Value (COLUMNNAME_C_Charge_SW_ID, Integer.valueOf(C_Charge_SW_ID));
	}

	/** Get Charge Sales Work Force .
		@return Charge for Sales Work Force 
	  */
	public int getC_Charge_SW_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_SW_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Copy From.
		@param CopyFrom Copy From Record
	*/
	public void setCopyFrom (String CopyFrom)
	{
		set_Value (COLUMNNAME_CopyFrom, CopyFrom);
	}

	/** Get Copy From.
		@return Copy From Record
	  */
	public String getCopyFrom()
	{
		return (String)get_Value(COLUMNNAME_CopyFrom);
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
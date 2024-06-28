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

/** Generated Model for AMN_Jobunit
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Jobunit extends PO implements I_AMN_Jobunit, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Jobunit (Properties ctx, int AMN_Jobunit_ID, String trxName)
    {
      super (ctx, AMN_Jobunit_ID, trxName);
      /** if (AMN_Jobunit_ID == 0)
        {
			setAMN_Department_ID (0);
			setAMN_Jobunit_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Jobunit (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Jobunit[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Payroll Department.
		@param AMN_Department_ID Payroll Department	  */
	public void setAMN_Department_ID (int AMN_Department_ID)
	{
		if (AMN_Department_ID < 1) 
			set_Value (COLUMNNAME_AMN_Department_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Department_ID, Integer.valueOf(AMN_Department_ID));
	}

	/** Get Payroll Department.
		@return Payroll Department	  */
	public int getAMN_Department_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Department_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Job Unit.
		@param AMN_Jobunit_ID Payroll Job Unit	  */
	public void setAMN_Jobunit_ID (int AMN_Jobunit_ID)
	{
		if (AMN_Jobunit_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Jobunit_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Jobunit_ID, Integer.valueOf(AMN_Jobunit_ID));
	}

	/** Get Payroll Job Unit.
		@return Payroll Job Unit	  */
	public int getAMN_Jobunit_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobunit_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Jobunit_UU.
		@param AMN_Jobunit_UU AMN_Jobunit_UU	  */
	public void setAMN_Jobunit_UU (String AMN_Jobunit_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Jobunit_UU, AMN_Jobunit_UU);
	}

	/** Get AMN_Jobunit_UU.
		@return AMN_Jobunit_UU	  */
	public String getAMN_Jobunit_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Jobunit_UU);
	}

//	public I_AMN_Jobunit getAMN_Parentjobunit() throws RuntimeException
//    {
//		return (I_AMN_Jobunit)MTable.get(getCtx(), I_AMN_Jobunit.Table_Name)
//			.getPO(getAMN_Parentjobunit_ID(), get_TrxName());	}
//
//	/** Set AMN_Parentjobunit_ID.
//		@param AMN_Parentjobunit_ID AMN_Parentjobunit_ID	  */
//	public void setAMN_Parentjobunit_ID (int AMN_Parentjobunit_ID)
//	{
//		if (AMN_Parentjobunit_ID < 1) 
//			set_Value (COLUMNNAME_AMN_Parentjobunit_ID, null);
//		else 
//			set_Value (COLUMNNAME_AMN_Parentjobunit_ID, Integer.valueOf(AMN_Parentjobunit_ID));
//	}
//
//	/** Get AMN_Parentjobunit_ID.
//		@return AMN_Parentjobunit_ID	  */
//	public int getAMN_Parentjobunit_ID () 
//	{
//		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Parentjobunit_ID);
//		if (ii == null)
//			 return 0;
//		return ii.intValue();
//	}

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

//	/** Set value_parent.
//		@param value_parent value_parent	  */
//	public void setvalue_parent (String value_parent)
//	{
//		set_Value (COLUMNNAME_value_parent, value_parent);
//	}
//
//	/** Get value_parent.
//		@return value_parent	  */
//	public String getvalue_parent () 
//	{
//		return (String)get_Value(COLUMNNAME_value_parent);
//	}
}
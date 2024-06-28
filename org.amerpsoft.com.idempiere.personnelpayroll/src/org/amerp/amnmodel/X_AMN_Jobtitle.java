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

/** Generated Model for AMN_Jobtitle
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Jobtitle extends PO implements I_AMN_Jobtitle, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170309L;

    /** Standard Constructor */
    public X_AMN_Jobtitle (Properties ctx, int AMN_Jobtitle_ID, String trxName)
    {
      super (ctx, AMN_Jobtitle_ID, trxName);
      /** if (AMN_Jobtitle_ID == 0)
        {
			setAMN_Jobtitle_ID (0);
			setName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Jobtitle (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Jobtitle[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AMN_Jobstation getAMN_Jobstation() throws RuntimeException
    {
		return (I_AMN_Jobstation)MTable.get(getCtx(), I_AMN_Jobstation.Table_Name)
			.getPO(getAMN_Jobstation_ID(), get_TrxName());	}

	/** Set Payroll Job Station.
		@param AMN_Jobstation_ID Payroll Job Station	  */
	public void setAMN_Jobstation_ID (int AMN_Jobstation_ID)
	{
		if (AMN_Jobstation_ID < 1) 
			set_Value (COLUMNNAME_AMN_Jobstation_ID, null);
		else 
			set_Value (COLUMNNAME_AMN_Jobstation_ID, Integer.valueOf(AMN_Jobstation_ID));
	}

	/** Get Payroll Job Station.
		@return Payroll Job Station	  */
	public int getAMN_Jobstation_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobstation_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Payroll Job Title.
		@param AMN_Jobtitle_ID Payroll Job Title	  */
	public void setAMN_Jobtitle_ID (int AMN_Jobtitle_ID)
	{
		if (AMN_Jobtitle_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Jobtitle_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Jobtitle_ID, Integer.valueOf(AMN_Jobtitle_ID));
	}

	/** Get Payroll Job Title.
		@return Payroll Job Title	  */
	public int getAMN_Jobtitle_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Jobtitle_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Jobtitle_UU.
		@param AMN_Jobtitle_UU AMN_Jobtitle_UU	  */
	public void setAMN_Jobtitle_UU (String AMN_Jobtitle_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Jobtitle_UU, AMN_Jobtitle_UU);
	}

	/** Get AMN_Jobtitle_UU.
		@return AMN_Jobtitle_UU	  */
	public String getAMN_Jobtitle_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Jobtitle_UU);
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

	/** Direct Workforce = D */
	public static final String WORKFORCE_DirectWorkforce = "D";
	/** Indirect Workforce = I */
	public static final String WORKFORCE_IndirectWorkforce = "I";
	/** Sales Workforce = S */
	public static final String WORKFORCE_SalesWorkforce = "S";
	/** Administrative Workforce = A */
	public static final String WORKFORCE_AdministrativeWorkforce = "A";
	/** Set Workforce.
		@param Workforce 
		Indicates Workforce kind Direct-Indirect-Sales-Administrative
	  */
	public void setWorkforce (String Workforce)
	{

		set_Value (COLUMNNAME_Workforce, Workforce);
	}

	/** Get Workforce.
		@return Indicates Workforce kind Direct-Indirect-Sales-Administrative
	  */
	public String getWorkforce () 
	{
		return (String)get_Value(COLUMNNAME_Workforce);
	}
}
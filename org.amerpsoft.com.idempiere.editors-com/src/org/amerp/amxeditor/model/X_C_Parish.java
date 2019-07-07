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
package org.amerp.amxeditor.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_Parish
 *  @author iDempiere (generated) 
 *  @version Release 2.0 - $Id$ */
public class X_C_Parish extends PO implements I_C_Parish, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150827L;

    /** Standard Constructor */
    public X_C_Parish (Properties ctx, int C_Parish_ID, String trxName)
    {
      super (ctx, C_Parish_ID, trxName);
      /** if (C_Parish_ID == 0)
        {
			setC_Country_ID (0);
			setC_Municipality_ID (0);
			setC_Parish_ID (0);
			setC_Region_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_C_Parish (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_Parish[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Country.
		@param C_Country_ID 
		Country 
	  */
	public void setC_Country_ID (int C_Country_ID)
	{
		if (C_Country_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Country_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
	}

	/** Get Country.
		@return Country 
	  */
	public int getC_Country_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Country_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Municipality.
		@param C_Municipality_ID C_Municipality	  */
	public void setC_Municipality_ID (int C_Municipality_ID)
	{
		if (C_Municipality_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Municipality_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Municipality_ID, Integer.valueOf(C_Municipality_ID));
	}

	/** Get C_Municipality.
		@return C_Municipality	  */
	public int getC_Municipality_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Municipality_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Parish.
		@param C_Parish_ID C_Parish	  */
	public void setC_Parish_ID (int C_Parish_ID)
	{
		if (C_Parish_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Parish_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Parish_ID, Integer.valueOf(C_Parish_ID));
	}

	/** Get C_Parish.
		@return C_Parish	  */
	public int getC_Parish_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Parish_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Parish_UU.
		@param C_Parish_UU C_Parish_UU	  */
	public void setC_Parish_UU (String C_Parish_UU)
	{
		set_Value (COLUMNNAME_C_Parish_UU, C_Parish_UU);
	}

	/** Get C_Parish_UU.
		@return C_Parish_UU	  */
	public String getC_Parish_UU () 
	{
		return (String)get_Value(COLUMNNAME_C_Parish_UU);
	}

	public org.compiere.model.I_C_Region getC_Region() throws RuntimeException
    {
		return (org.compiere.model.I_C_Region)MTable.get(getCtx(), org.compiere.model.I_C_Region.Table_Name)
			.getPO(getC_Region_ID(), get_TrxName());	}

	/** Set Region.
		@param C_Region_ID 
		Identifies a geographical Region
	  */
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Region_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Region_ID, Integer.valueOf(C_Region_ID));
	}

	/** Get Region.
		@return Identifies a geographical Region
	  */
	public int getC_Region_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Region_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Default.
		@param IsDefault 
		Default value
	  */
	public void setIsDefault (boolean IsDefault)
	{
		set_Value (COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
	}

	/** Get Default.
		@return Default value
	  */
	public boolean isDefault () 
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
}
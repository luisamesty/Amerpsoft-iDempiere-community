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

/** Generated Model for MOLI_GeoRefCode
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="MOLI_GeoRefCode")
public class X_MOLI_GeoRefCode extends PO implements I_MOLI_GeoRefCode, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250213L;

    /** Standard Constructor */
    public X_MOLI_GeoRefCode (Properties ctx, int MOLI_GeoRefCode_ID, String trxName)
    {
      super (ctx, MOLI_GeoRefCode_ID, trxName);
      /** if (MOLI_GeoRefCode_ID == 0)
        {
			setMOLI_GeoRefCode_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_MOLI_GeoRefCode (Properties ctx, int MOLI_GeoRefCode_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, MOLI_GeoRefCode_ID, trxName, virtualColumns);
      /** if (MOLI_GeoRefCode_ID == 0)
        {
			setMOLI_GeoRefCode_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_MOLI_GeoRefCode (Properties ctx, String MOLI_GeoRefCode_UU, String trxName)
    {
      super (ctx, MOLI_GeoRefCode_UU, trxName);
      /** if (MOLI_GeoRefCode_UU == null)
        {
			setMOLI_GeoRefCode_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_MOLI_GeoRefCode (Properties ctx, String MOLI_GeoRefCode_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, MOLI_GeoRefCode_UU, trxName, virtualColumns);
      /** if (MOLI_GeoRefCode_UU == null)
        {
			setMOLI_GeoRefCode_ID (0);
        } */
    }

    /** Load Constructor */
    public X_MOLI_GeoRefCode (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_MOLI_GeoRefCode[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Geo Ref Code Department Key.
		@param MOLI_GeoRefCode_DepartmentKey Geo Ref Code Department Key
	*/
	public void setMOLI_GeoRefCode_DepartmentKey (String MOLI_GeoRefCode_DepartmentKey)
	{
		set_Value (COLUMNNAME_MOLI_GeoRefCode_DepartmentKey, MOLI_GeoRefCode_DepartmentKey);
	}

	/** Get Geo Ref Code Department Key.
		@return Geo Ref Code Department Key	  */
	public String getMOLI_GeoRefCode_DepartmentKey()
	{
		return (String)get_Value(COLUMNNAME_MOLI_GeoRefCode_DepartmentKey);
	}

	/** Set Geo Ref Code Department Name.
		@param MOLI_GeoRefCode_DepartmentName Geo Ref Code Department Name
	*/
	public void setMOLI_GeoRefCode_DepartmentName (String MOLI_GeoRefCode_DepartmentName)
	{
		set_Value (COLUMNNAME_MOLI_GeoRefCode_DepartmentName, MOLI_GeoRefCode_DepartmentName);
	}

	/** Get Geo Ref Code Department Name.
		@return Geo Ref Code Department Name	  */
	public String getMOLI_GeoRefCode_DepartmentName()
	{
		return (String)get_Value(COLUMNNAME_MOLI_GeoRefCode_DepartmentName);
	}

	/** Set Geo Ref Code ID.
		@param MOLI_GeoRefCode_ID Geo Ref Code ID
	*/
	public void setMOLI_GeoRefCode_ID (int MOLI_GeoRefCode_ID)
	{
		if (MOLI_GeoRefCode_ID < 1)
			set_ValueNoCheck (COLUMNNAME_MOLI_GeoRefCode_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_MOLI_GeoRefCode_ID, Integer.valueOf(MOLI_GeoRefCode_ID));
	}

	/** Get Geo Ref Code ID.
		@return Geo Ref Code ID	  */
	public int getMOLI_GeoRefCode_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MOLI_GeoRefCode_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Geo Ref Code Section Key.
		@param MOLI_GeoRefCode_SectionKey Geo Ref Code Section Key
	*/
	public void setMOLI_GeoRefCode_SectionKey (String MOLI_GeoRefCode_SectionKey)
	{
		set_Value (COLUMNNAME_MOLI_GeoRefCode_SectionKey, MOLI_GeoRefCode_SectionKey);
	}

	/** Get Geo Ref Code Section Key.
		@return Geo Ref Code Section Key	  */
	public String getMOLI_GeoRefCode_SectionKey()
	{
		return (String)get_Value(COLUMNNAME_MOLI_GeoRefCode_SectionKey);
	}

	/** Set Geo Ref Code Section Name.
		@param MOLI_GeoRefCode_SectionName Geo Ref Code Section Name
	*/
	public void setMOLI_GeoRefCode_SectionName (String MOLI_GeoRefCode_SectionName)
	{
		set_Value (COLUMNNAME_MOLI_GeoRefCode_SectionName, MOLI_GeoRefCode_SectionName);
	}

	/** Get Geo Ref Code Section Name.
		@return Geo Ref Code Section Name	  */
	public String getMOLI_GeoRefCode_SectionName()
	{
		return (String)get_Value(COLUMNNAME_MOLI_GeoRefCode_SectionName);
	}

	/** Set Geo Ref Code UUID.
		@param MOLI_GeoRefCode_UU Geo Ref Code UUID
	*/
	public void setMOLI_GeoRefCode_UU (String MOLI_GeoRefCode_UU)
	{
		set_Value (COLUMNNAME_MOLI_GeoRefCode_UU, MOLI_GeoRefCode_UU);
	}

	/** Get Geo Ref Code UUID.
		@return Geo Ref Code UUID	  */
	public String getMOLI_GeoRefCode_UU()
	{
		return (String)get_Value(COLUMNNAME_MOLI_GeoRefCode_UU);
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
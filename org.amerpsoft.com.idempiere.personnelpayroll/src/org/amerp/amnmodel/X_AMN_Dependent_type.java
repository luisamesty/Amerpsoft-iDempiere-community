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

/** Generated Model for AMN_Dependent_type
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_AMN_Dependent_type extends PO implements I_AMN_Dependent_type, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20151113L;

    /** Standard Constructor */
    public X_AMN_Dependent_type (Properties ctx, int AMN_Dependent_type_ID, String trxName)
    {
      super (ctx, AMN_Dependent_type_ID, trxName);
      /** if (AMN_Dependent_type_ID == 0)
        {
			setAMN_Dependent_type_ID (0);
			setName (null);
			setNotificationType (null);
			setValue (null);
			setsex (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Dependent_type (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AMN_Dependent_type[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Employee Dependent Type.
		@param AMN_Dependent_type_ID Employee Dependent Type	  */
	public void setAMN_Dependent_type_ID (int AMN_Dependent_type_ID)
	{
		if (AMN_Dependent_type_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AMN_Dependent_type_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AMN_Dependent_type_ID, Integer.valueOf(AMN_Dependent_type_ID));
	}

	/** Get Employee Dependent Type.
		@return Employee Dependent Type	  */
	public int getAMN_Dependent_type_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Dependent_type_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Dependent_type_UU.
		@param AMN_Dependent_type_UU AMN_Dependent_type_UU	  */
	public void setAMN_Dependent_type_UU (String AMN_Dependent_type_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Dependent_type_UU, AMN_Dependent_type_UU);
	}

	/** Get AMN_Dependent_type_UU.
		@return AMN_Dependent_type_UU	  */
	public String getAMN_Dependent_type_UU () 
	{
		return (String)get_Value(COLUMNNAME_AMN_Dependent_type_UU);
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

	/** NotificationType AD_Reference_ID=344 */
	public static final int NOTIFICATIONTYPE_AD_Reference_ID=344;
	/** EMail = E */
	public static final String NOTIFICATIONTYPE_EMail = "E";
	/** Notice = N */
	public static final String NOTIFICATIONTYPE_Notice = "N";
	/** None = X */
	public static final String NOTIFICATIONTYPE_None = "X";
	/** EMail+Notice = B */
	public static final String NOTIFICATIONTYPE_EMailPlusNotice = "B";
	/** Set Notification Type.
		@param NotificationType 
		Type of Notifications
	  */
	public void setNotificationType (String NotificationType)
	{

		set_Value (COLUMNNAME_NotificationType, NotificationType);
	}

	/** Get Notification Type.
		@return Type of Notifications
	  */
	public String getNotificationType () 
	{
		return (String)get_Value(COLUMNNAME_NotificationType);
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

	/** Female = F */
	public static final String SEX_Female = "F";
	/** Male = M */
	public static final String SEX_Male = "M";
	/** Set sex.
		@param sex sex	  */
	public void setsex (String sex)
	{

		set_Value (COLUMNNAME_sex, sex);
	}

	/** Get sex.
		@return sex	  */
	public String getsex () 
	{
		return (String)get_Value(COLUMNNAME_sex);
	}
}
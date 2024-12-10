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
package org.amerp.workflow.amwmodel;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for AMW_WF_Node
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMW_WF_Node")
public class X_AMW_WF_Node extends PO implements I_AMW_WF_Node, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241214L;

    /** Standard Constructor */
    public X_AMW_WF_Node (Properties ctx, int AMW_WF_Node_ID, String trxName)
    {
      super (ctx, AMW_WF_Node_ID, trxName);
      /** if (AMW_WF_Node_ID == 0)
        {
			setAMW_WF_Node_ID (0);
			setName (null);
			setSeqNo (0);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_Node (Properties ctx, int AMW_WF_Node_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WF_Node_ID, trxName, virtualColumns);
      /** if (AMW_WF_Node_ID == 0)
        {
			setAMW_WF_Node_ID (0);
			setName (null);
			setSeqNo (0);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_Node (Properties ctx, String AMW_WF_Node_UU, String trxName)
    {
      super (ctx, AMW_WF_Node_UU, trxName);
      /** if (AMW_WF_Node_UU == null)
        {
			setAMW_WF_Node_ID (0);
			setName (null);
			setSeqNo (0);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_Node (Properties ctx, String AMW_WF_Node_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WF_Node_UU, trxName, virtualColumns);
      /** if (AMW_WF_Node_UU == null)
        {
			setAMW_WF_Node_ID (0);
			setName (null);
			setSeqNo (0);
        } */
    }

    /** Load Constructor */
    public X_AMW_WF_Node (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMW_WF_Node[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set AMW Work Flow Nodes.
		@param AMW_WF_Node_ID AMW Work Flow Nodes
	*/
	public void setAMW_WF_Node_ID (int AMW_WF_Node_ID)
	{
		if (AMW_WF_Node_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMW_WF_Node_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMW_WF_Node_ID, Integer.valueOf(AMW_WF_Node_ID));
	}

	/** Get AMW Work Flow Nodes.
		@return AMW Work Flow Nodes
	  */
	public int getAMW_WF_Node_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WF_Node_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMW_WF_Node_UU.
		@param AMW_WF_Node_UU AMW_WF_Node_UU
	*/
	public void setAMW_WF_Node_UU (String AMW_WF_Node_UU)
	{
		set_Value (COLUMNNAME_AMW_WF_Node_UU, AMW_WF_Node_UU);
	}

	/** Get AMW_WF_Node_UU.
		@return AMW_WF_Node_UU	  */
	public String getAMW_WF_Node_UU()
	{
		return (String)get_Value(COLUMNNAME_AMW_WF_Node_UU);
	}

	public I_AMW_WorkFlow getAMW_WorkFlow() throws RuntimeException
	{
		return (I_AMW_WorkFlow)MTable.get(getCtx(), I_AMW_WorkFlow.Table_ID)
			.getPO(getAMW_WorkFlow_ID(), get_TrxName());
	}

	/** Set AMW Work Flows.
		@param AMW_WorkFlow_ID Plugin AMW Work Flows defined by User
	*/
	public void setAMW_WorkFlow_ID (int AMW_WorkFlow_ID)
	{
		if (AMW_WorkFlow_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMW_WorkFlow_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMW_WorkFlow_ID, Integer.valueOf(AMW_WorkFlow_ID));
	}

	/** Get AMW Work Flows.
		@return Plugin AMW Work Flows defined by User
	  */
	public int getAMW_WorkFlow_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WorkFlow_ID);
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

	/** NotificationType AD_Reference_ID=344 */
	public static final int NOTIFICATIONTYPE_AD_Reference_ID=344;
	/** EMail+Notice = B */
	public static final String NOTIFICATIONTYPE_EMailPlusNotice = "B";
	/** EMail = E */
	public static final String NOTIFICATIONTYPE_EMail = "E";
	/** Notice = N */
	public static final String NOTIFICATIONTYPE_Notice = "N";
	/** None = X */
	public static final String NOTIFICATIONTYPE_None = "X";
	/** Set Notification Type.
		@param NotificationType Type of Notifications
	*/
	public void setNotificationType (String NotificationType)
	{

		set_Value (COLUMNNAME_NotificationType, NotificationType);
	}

	/** Get Notification Type.
		@return Type of Notifications
	  */
	public String getNotificationType()
	{
		return (String)get_Value(COLUMNNAME_NotificationType);
	}

	/** Set Sequence.
		@param SeqNo Method of ordering records; lowest number comes first
	*/
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
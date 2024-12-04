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

/** Generated Model for AMN_Leaves_Nodes
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Leaves_Nodes")
public class X_AMN_Leaves_Nodes extends PO implements I_AMN_Leaves_Nodes, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241205L;

    /** Standard Constructor */
    public X_AMN_Leaves_Nodes (Properties ctx, int AMN_Leaves_Nodes_ID, String trxName)
    {
      super (ctx, AMN_Leaves_Nodes_ID, trxName);
      /** if (AMN_Leaves_Nodes_ID == 0)
        {
			setAMN_Leaves_Nodes_ID (0);
			setAMN_Leaves_NodesNext_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Nodes (Properties ctx, int AMN_Leaves_Nodes_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_Nodes_ID, trxName, virtualColumns);
      /** if (AMN_Leaves_Nodes_ID == 0)
        {
			setAMN_Leaves_Nodes_ID (0);
			setAMN_Leaves_NodesNext_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Nodes (Properties ctx, String AMN_Leaves_Nodes_UU, String trxName)
    {
      super (ctx, AMN_Leaves_Nodes_UU, trxName);
      /** if (AMN_Leaves_Nodes_UU == null)
        {
			setAMN_Leaves_Nodes_ID (0);
			setAMN_Leaves_NodesNext_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Leaves_Nodes (Properties ctx, String AMN_Leaves_Nodes_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Leaves_Nodes_UU, trxName, virtualColumns);
      /** if (AMN_Leaves_Nodes_UU == null)
        {
			setAMN_Leaves_Nodes_ID (0);
			setAMN_Leaves_NodesNext_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Leaves_Nodes (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Leaves_Nodes[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public I_AMN_Leaves_Flow getAMN_Leaves_Flow() throws RuntimeException
	{
		return (I_AMN_Leaves_Flow)MTable.get(getCtx(), I_AMN_Leaves_Flow.Table_ID)
			.getPO(getAMN_Leaves_Flow_ID(), get_TrxName());
	}

	/** Set Leaves and Licences Work Flow.
		@param AMN_Leaves_Flow_ID Leaves and Licences Work Flow
	*/
	public void setAMN_Leaves_Flow_ID (int AMN_Leaves_Flow_ID)
	{
		if (AMN_Leaves_Flow_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Flow_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Flow_ID, Integer.valueOf(AMN_Leaves_Flow_ID));
	}

	/** Get Leaves and Licences Work Flow.
		@return Leaves and Licences Work Flow
	  */
	public int getAMN_Leaves_Flow_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_Flow_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Leaves and Licences Work Flow Nodes.
		@param AMN_Leaves_Nodes_ID Leaves and Licences Work Flow Nodes
	*/
	public void setAMN_Leaves_Nodes_ID (int AMN_Leaves_Nodes_ID)
	{
		if (AMN_Leaves_Nodes_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Nodes_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Leaves_Nodes_ID, Integer.valueOf(AMN_Leaves_Nodes_ID));
	}

	/** Get Leaves and Licences Work Flow Nodes.
		@return Leaves and Licences Work Flow Nodes
	  */
	public int getAMN_Leaves_Nodes_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_Nodes_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMN_Leaves_Flow getAMN_Leaves_NodesNext() throws RuntimeException
	{
		return (I_AMN_Leaves_Flow)MTable.get(getCtx(), I_AMN_Leaves_Flow.Table_ID)
			.getPO(getAMN_Leaves_NodesNext_ID(), get_TrxName());
	}

	/** Set Leaves and Licences Work Flow Nodes Next.
		@param AMN_Leaves_NodesNext_ID Leaves and Licences Work Flow Nodes Next
	*/
	public void setAMN_Leaves_NodesNext_ID (int AMN_Leaves_NodesNext_ID)
	{
		if (AMN_Leaves_NodesNext_ID < 1)
			set_Value (COLUMNNAME_AMN_Leaves_NodesNext_ID, null);
		else
			set_Value (COLUMNNAME_AMN_Leaves_NodesNext_ID, Integer.valueOf(AMN_Leaves_NodesNext_ID));
	}

	/** Get Leaves and Licences Work Flow Nodes Next.
		@return Leaves and Licences Work Flow Nodes Next
	  */
	public int getAMN_Leaves_NodesNext_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Leaves_NodesNext_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Leaves_Nodes_UU.
		@param AMN_Leaves_Nodes_UU AMN_Leaves_Nodes_UU
	*/
	public void setAMN_Leaves_Nodes_UU (String AMN_Leaves_Nodes_UU)
	{
		set_Value (COLUMNNAME_AMN_Leaves_Nodes_UU, AMN_Leaves_Nodes_UU);
	}

	/** Get AMN_Leaves_Nodes_UU.
		@return AMN_Leaves_Nodes_UU	  */
	public String getAMN_Leaves_Nodes_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Leaves_Nodes_UU);
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
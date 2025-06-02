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

/** Generated Model for AMW_WF_NodeNext
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMW_WF_NodeNext")
public class X_AMW_WF_NodeNext extends PO implements I_AMW_WF_NodeNext, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241214L;

    /** Standard Constructor */
    public X_AMW_WF_NodeNext (Properties ctx, int AMW_WF_NodeNext_ID, String trxName)
    {
      super (ctx, AMW_WF_NodeNext_ID, trxName);
      /** if (AMW_WF_NodeNext_ID == 0)
        {
			setAMW_WF_NodeNext_ID (0);
			setDescription (null);
			setSeqNo (0);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_NodeNext (Properties ctx, int AMW_WF_NodeNext_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WF_NodeNext_ID, trxName, virtualColumns);
      /** if (AMW_WF_NodeNext_ID == 0)
        {
			setAMW_WF_NodeNext_ID (0);
			setDescription (null);
			setSeqNo (0);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_NodeNext (Properties ctx, String AMW_WF_NodeNext_UU, String trxName)
    {
      super (ctx, AMW_WF_NodeNext_UU, trxName);
      /** if (AMW_WF_NodeNext_UU == null)
        {
			setAMW_WF_NodeNext_ID (0);
			setDescription (null);
			setSeqNo (0);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WF_NodeNext (Properties ctx, String AMW_WF_NodeNext_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WF_NodeNext_UU, trxName, virtualColumns);
      /** if (AMW_WF_NodeNext_UU == null)
        {
			setAMW_WF_NodeNext_ID (0);
			setDescription (null);
			setSeqNo (0);
        } */
    }

    /** Load Constructor */
    public X_AMW_WF_NodeNext (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMW_WF_NodeNext[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set AMW Work Flow Next ID.
		@param AMW_WF_Next_ID AMW Work Flow Next ID
	*/
	public void setAMW_WF_Next_ID (int AMW_WF_Next_ID)
	{
		if (AMW_WF_Next_ID < 1)
			set_Value (COLUMNNAME_AMW_WF_Next_ID, null);
		else
			set_Value (COLUMNNAME_AMW_WF_Next_ID, Integer.valueOf(AMW_WF_Next_ID));
	}

	/** Get AMW Work Flow Next ID.
		@return AMW Work Flow Next ID
	  */
	public int getAMW_WF_Next_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WF_Next_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AMW_WF_Node getAMW_WF_Node() throws RuntimeException
	{
		return (I_AMW_WF_Node)MTable.get(getCtx(), I_AMW_WF_Node.Table_ID)
			.getPO(getAMW_WF_Node_ID(), get_TrxName());
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

	/** Set AMW Work Flow Node Next ID.
		@param AMW_WF_NodeNext_ID AMW Work Flow Node Next ID
	*/
	public void setAMW_WF_NodeNext_ID (int AMW_WF_NodeNext_ID)
	{
		if (AMW_WF_NodeNext_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMW_WF_NodeNext_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMW_WF_NodeNext_ID, Integer.valueOf(AMW_WF_NodeNext_ID));
	}

	/** Get AMW Work Flow Node Next ID.
		@return AMW Work Flow Node Next ID
	  */
	public int getAMW_WF_NodeNext_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMW_WF_NodeNext_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMW_WF_NodeNext_UU.
		@param AMW_WF_NodeNext_UU AMW_WF_NodeNext_UU
	*/
	public void setAMW_WF_NodeNext_UU (String AMW_WF_NodeNext_UU)
	{
		set_Value (COLUMNNAME_AMW_WF_NodeNext_UU, AMW_WF_NodeNext_UU);
	}

	/** Get AMW_WF_NodeNext_UU.
		@return AMW_WF_NodeNext_UU	  */
	public String getAMW_WF_NodeNext_UU()
	{
		return (String)get_Value(COLUMNNAME_AMW_WF_NodeNext_UU);
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
}
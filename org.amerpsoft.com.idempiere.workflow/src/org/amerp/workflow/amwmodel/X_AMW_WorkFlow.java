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

/** Generated Model for AMW_WorkFlow
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMW_WorkFlow")
public class X_AMW_WorkFlow extends PO implements I_AMW_WorkFlow, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241214L;

    /** Standard Constructor */
    public X_AMW_WorkFlow (Properties ctx, int AMW_WorkFlow_ID, String trxName)
    {
      super (ctx, AMW_WorkFlow_ID, trxName);
      /** if (AMW_WorkFlow_ID == 0)
        {
			setAMW_WorkFlow_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WorkFlow (Properties ctx, int AMW_WorkFlow_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WorkFlow_ID, trxName, virtualColumns);
      /** if (AMW_WorkFlow_ID == 0)
        {
			setAMW_WorkFlow_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WorkFlow (Properties ctx, String AMW_WorkFlow_UU, String trxName)
    {
      super (ctx, AMW_WorkFlow_UU, trxName);
      /** if (AMW_WorkFlow_UU == null)
        {
			setAMW_WorkFlow_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMW_WorkFlow (Properties ctx, String AMW_WorkFlow_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMW_WorkFlow_UU, trxName, virtualColumns);
      /** if (AMW_WorkFlow_UU == null)
        {
			setAMW_WorkFlow_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMW_WorkFlow (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMW_WorkFlow[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_ID)
			.getPO(getAD_Table_ID(), get_TrxName());
	}

	/** Set Table.
		@param AD_Table_ID Database Table information
	*/
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1)
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set AMT_WorkFlow_UU.
		@param AMW_WorkFlow_UU AMT_WorkFlow_UU
	*/
	public void setAMW_WorkFlow_UU (String AMW_WorkFlow_UU)
	{
		set_Value (COLUMNNAME_AMW_WorkFlow_UU, AMW_WorkFlow_UU);
	}

	/** Get AMT_WorkFlow_UU.
		@return AMT_WorkFlow_UU	  */
	public String getAMW_WorkFlow_UU()
	{
		return (String)get_Value(COLUMNNAME_AMW_WorkFlow_UU);
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

	/** Set Validate Workflow.
		@param ValidateWorkflow Validate Workflow
	*/
	public void setValidateWorkflow (String ValidateWorkflow)
	{
		set_Value (COLUMNNAME_ValidateWorkflow, ValidateWorkflow);
	}

	/** Get Validate Workflow.
		@return Validate Workflow	  */
	public String getValidateWorkflow()
	{
		return (String)get_Value(COLUMNNAME_ValidateWorkflow);
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

	/** WorkflowType AD_Reference_ID=108 */
	public static final int WORKFLOWTYPE_AD_Reference_ID=108;
	/** Maintain = M */
	public static final String WORKFLOWTYPE_Maintain = "M";
	/** Query Only = Q */
	public static final String WORKFLOWTYPE_QueryOnly = "Q";
	/** Single Record = S */
	public static final String WORKFLOWTYPE_SingleRecord = "S";
	/** Transaction = T */
	public static final String WORKFLOWTYPE_Transaction = "T";
	/** Set Workflow Type.
		@param WorkflowType Type of Workflow
	*/
	public void setWorkflowType (String WorkflowType)
	{

		set_Value (COLUMNNAME_WorkflowType, WorkflowType);
	}

	/** Get Workflow Type.
		@return Type of Workflow
	  */
	public String getWorkflowType()
	{
		return (String)get_Value(COLUMNNAME_WorkflowType);
	}
}
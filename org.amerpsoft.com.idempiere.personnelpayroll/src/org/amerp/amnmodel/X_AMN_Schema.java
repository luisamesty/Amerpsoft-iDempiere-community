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
import org.compiere.util.KeyNamePair;

/** Generated Model for AMN_Schema
 *  @author iDempiere (generated)
 *  @version Release 11 - $Id$ */
@org.adempiere.base.Model(table="AMN_Schema")
public class X_AMN_Schema extends PO implements I_AMN_Schema, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20241204L;

    /** Standard Constructor */
    public X_AMN_Schema (Properties ctx, int AMN_Schema_ID, String trxName)
    {
      super (ctx, AMN_Schema_ID, trxName);
      /** if (AMN_Schema_ID == 0)
        {
			setAMN_Schema_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Schema (Properties ctx, int AMN_Schema_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Schema_ID, trxName, virtualColumns);
      /** if (AMN_Schema_ID == 0)
        {
			setAMN_Schema_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Schema (Properties ctx, String AMN_Schema_UU, String trxName)
    {
      super (ctx, AMN_Schema_UU, trxName);
      /** if (AMN_Schema_UU == null)
        {
			setAMN_Schema_ID (0);
			setName (null);
        } */
    }

    /** Standard Constructor */
    public X_AMN_Schema (Properties ctx, String AMN_Schema_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, AMN_Schema_UU, trxName, virtualColumns);
      /** if (AMN_Schema_UU == null)
        {
			setAMN_Schema_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_AMN_Schema (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_AMN_Schema[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public I_C_ValidCombination getAMN_P_Education_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Education_AW_Expense(), get_TrxName());
	}

	/** Set Education Expense Account for Administrative Workforce.
		@param AMN_P_Education_AW_Expense House Education Account for Administrative Workforce Default Value
	*/
	public void setAMN_P_Education_AW_Expense (int AMN_P_Education_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Education_AW_Expense, Integer.valueOf(AMN_P_Education_AW_Expense));
	}

	/** Get Education Expense Account for Administrative Workforce.
		@return House Education Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_Education_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Education_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Education_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Education_DW_Expense(), get_TrxName());
	}

	/** Set Education Expense Account for Direct Workforce.
		@param AMN_P_Education_DW_Expense Education Expense Account for Direct Workforce
	*/
	public void setAMN_P_Education_DW_Expense (int AMN_P_Education_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Education_DW_Expense, Integer.valueOf(AMN_P_Education_DW_Expense));
	}

	/** Get Education Expense Account for Direct Workforce.
		@return Education Expense Account for Direct Workforce	  */
	public int getAMN_P_Education_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Education_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Education_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Education_IW_Expense(), get_TrxName());
	}

	/** Set Education Expense Account for Indirect Workforce.
		@param AMN_P_Education_IW_Expense Education Expense Account for Indirect Workforce
	*/
	public void setAMN_P_Education_IW_Expense (int AMN_P_Education_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Education_IW_Expense, Integer.valueOf(AMN_P_Education_IW_Expense));
	}

	/** Get Education Expense Account for Indirect Workforce.
		@return Education Expense Account for Indirect Workforce	  */
	public int getAMN_P_Education_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Education_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Education_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Education_MW_Expense(), get_TrxName());
	}

	/** Set Education Expense Account for Management Work Force.
		@param AMN_P_Education_MW_Expense Education Expense Account for Management Work Force
	*/
	public void setAMN_P_Education_MW_Expense (int AMN_P_Education_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Education_MW_Expense, Integer.valueOf(AMN_P_Education_MW_Expense));
	}

	/** Get Education Expense Account for Management Work Force.
		@return Education Expense Account for Management Work Force	  */
	public int getAMN_P_Education_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Education_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Education_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Education_SW_Expense(), get_TrxName());
	}

	/** Set Education Expense Account for Sales Workforce.
		@param AMN_P_Education_SW_Expense Education Expense Account for Sales Workforce
	*/
	public void setAMN_P_Education_SW_Expense (int AMN_P_Education_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Education_SW_Expense, Integer.valueOf(AMN_P_Education_SW_Expense));
	}

	/** Get Education Expense Account for Sales Workforce.
		@return Education Expense Account for Sales Workforce	  */
	public int getAMN_P_Education_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Education_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_HouseSaving_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_HouseSaving_AW_Expense(), get_TrxName());
	}

	/** Set House Saving Expense Account for Administrative Workforce.
		@param AMN_P_HouseSaving_AW_Expense House Saving Expense Account for Administrative Workforce Default Value
	*/
	public void setAMN_P_HouseSaving_AW_Expense (int AMN_P_HouseSaving_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_HouseSaving_AW_Expense, Integer.valueOf(AMN_P_HouseSaving_AW_Expense));
	}

	/** Get House Saving Expense Account for Administrative Workforce.
		@return House Saving Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_HouseSaving_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_HouseSaving_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_HouseSaving_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_HouseSaving_DW_Expense(), get_TrxName());
	}

	/** Set House Saving Expense Account for Direct Workforce.
		@param AMN_P_HouseSaving_DW_Expense House Saving Expense Account for Direct Workforce
	*/
	public void setAMN_P_HouseSaving_DW_Expense (int AMN_P_HouseSaving_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_HouseSaving_DW_Expense, Integer.valueOf(AMN_P_HouseSaving_DW_Expense));
	}

	/** Get House Saving Expense Account for Direct Workforce.
		@return House Saving Expense Account for Direct Workforce	  */
	public int getAMN_P_HouseSaving_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_HouseSaving_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_HouseSaving_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_HouseSaving_IW_Expense(), get_TrxName());
	}

	/** Set House Saving Expense Account for Indirect Workforce.
		@param AMN_P_HouseSaving_IW_Expense House Saving Expense Account for Indirect Workforce
	*/
	public void setAMN_P_HouseSaving_IW_Expense (int AMN_P_HouseSaving_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_HouseSaving_IW_Expense, Integer.valueOf(AMN_P_HouseSaving_IW_Expense));
	}

	/** Get House Saving Expense Account for Indirect Workforce.
		@return House Saving Expense Account for Indirect Workforce	  */
	public int getAMN_P_HouseSaving_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_HouseSaving_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_HouseSaving_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_HouseSaving_MW_Expense(), get_TrxName());
	}

	/** Set House Saving Expense Account for Management Work Force.
		@param AMN_P_HouseSaving_MW_Expense House Saving Expense Account for Management Work Force
	*/
	public void setAMN_P_HouseSaving_MW_Expense (int AMN_P_HouseSaving_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_HouseSaving_MW_Expense, Integer.valueOf(AMN_P_HouseSaving_MW_Expense));
	}

	/** Get House Saving Expense Account for Management Work Force.
		@return House Saving Expense Account for Management Work Force	  */
	public int getAMN_P_HouseSaving_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_HouseSaving_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_HouseSaving_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_HouseSaving_SW_Expense(), get_TrxName());
	}

	/** Set House Saving Expense Account for Sales Workforce.
		@param AMN_P_HouseSaving_SW_Expense House Saving Expense Account for Sales Workforce
	*/
	public void setAMN_P_HouseSaving_SW_Expense (int AMN_P_HouseSaving_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_HouseSaving_SW_Expense, Integer.valueOf(AMN_P_HouseSaving_SW_Expense));
	}

	/** Get House Saving Expense Account for Sales Workforce.
		@return House Saving Expense Account for Sales Workforce	  */
	public int getAMN_P_HouseSaving_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_HouseSaving_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Advan() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Advances(), get_TrxName());
	}

	/** Set Advances to Employees Short Term Account.
		@param AMN_P_Liability_Advances Advances Liability short term account for PO process
	*/
	public void setAMN_P_Liability_Advances (int AMN_P_Liability_Advances)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Advances, Integer.valueOf(AMN_P_Liability_Advances));
	}

	/** Get Advances to Employees Short Term Account.
		@return Advances Liability short term account for PO process
	  */
	public int getAMN_P_Liability_Advances()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Advances);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Bo() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Bonus(), get_TrxName());
	}

	/** Set Bonus Short Term Account.
		@param AMN_P_Liability_Bonus Bonus Short Term Account
	*/
	public void setAMN_P_Liability_Bonus (int AMN_P_Liability_Bonus)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Bonus, Integer.valueOf(AMN_P_Liability_Bonus));
	}

	/** Get Bonus Short Term Account.
		@return Bonus Short Term Account	  */
	public int getAMN_P_Liability_Bonus()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Bonus);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_ContrBenef() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_ContrBenefits(), get_TrxName());
	}

	/** Set Other Contract Benefits TO.
		@param AMN_P_Liability_ContrBenefits Other Contract Benefits Liability short term account for TO process
	*/
	public void setAMN_P_Liability_ContrBenefits (int AMN_P_Liability_ContrBenefits)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_ContrBenefits, Integer.valueOf(AMN_P_Liability_ContrBenefits));
	}

	/** Get Other Contract Benefits TO.
		@return Other Contract Benefits Liability short term account for TO process
	  */
	public int getAMN_P_Liability_ContrBenefits()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_ContrBenefits);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Ot() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Other(), get_TrxName());
	}

	/** Set Other Food Benefits to Pay TI.
		@param AMN_P_Liability_Other Other Food Benefits to Pay short term account for TI process
	*/
	public void setAMN_P_Liability_Other (int AMN_P_Liability_Other)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Other, Integer.valueOf(AMN_P_Liability_Other));
	}

	/** Get Other Food Benefits to Pay TI.
		@return Other Food Benefits to Pay short term account for TI process
	  */
	public int getAMN_P_Liability_Other()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Other);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Sal() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Salary(), get_TrxName());
	}

	/** Set Salary Short Term to pay NN.
		@param AMN_P_Liability_Salary Salary Liability short term account for NN process
	*/
	public void setAMN_P_Liability_Salary (int AMN_P_Liability_Salary)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Salary, Integer.valueOf(AMN_P_Liability_Salary));
	}

	/** Get Salary Short Term to pay NN.
		@return Salary Liability short term account for NN process
	  */
	public int getAMN_P_Liability_Salary()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Salary);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_SalaryOt() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_SalaryOther(), get_TrxName());
	}

	/** Set Other Salary to pay NO.
		@param AMN_P_Liability_SalaryOther Other Salary Liability short term account for NO process
	*/
	public void setAMN_P_Liability_SalaryOther (int AMN_P_Liability_SalaryOther)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_SalaryOther, Integer.valueOf(AMN_P_Liability_SalaryOther));
	}

	/** Get Other Salary to pay NO.
		@return Other Salary Liability short term account for NO process
	  */
	public int getAMN_P_Liability_SalaryOther()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_SalaryOther);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Soc() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Social(), get_TrxName());
	}

	/** Set Social Benefits to Pay PI-PL-PR.
		@param AMN_P_Liability_Social Social Benefits Short Term Account for PI-PL-PR Processes
	*/
	public void setAMN_P_Liability_Social (int AMN_P_Liability_Social)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Social, Integer.valueOf(AMN_P_Liability_Social));
	}

	/** Get Social Benefits to Pay PI-PL-PR.
		@return Social Benefits Short Term Account for PI-PL-PR Processes
	  */
	public int getAMN_P_Liability_Social()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Social);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Utilit() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Utilities(), get_TrxName());
	}

	/** Set Utilities Short Term Account.
		@param AMN_P_Liability_Utilities Utilities short term account for NU process
	*/
	public void setAMN_P_Liability_Utilities (int AMN_P_Liability_Utilities)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Utilities, Integer.valueOf(AMN_P_Liability_Utilities));
	}

	/** Get Utilities Short Term Account.
		@return Utilities short term account for NU process
	  */
	public int getAMN_P_Liability_Utilities()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Utilities);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Liability_Vacat() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Liability_Vacation(), get_TrxName());
	}

	/** Set Vacation Short Term Account.
		@param AMN_P_Liability_Vacation Vacation to Pay short term account for NU process
	*/
	public void setAMN_P_Liability_Vacation (int AMN_P_Liability_Vacation)
	{
		set_Value (COLUMNNAME_AMN_P_Liability_Vacation, Integer.valueOf(AMN_P_Liability_Vacation));
	}

	/** Get Vacation Short Term Account.
		@return Vacation to Pay short term account for NU process
	  */
	public int getAMN_P_Liability_Vacation()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Liability_Vacation);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Provision_Soc() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Provision_Social(), get_TrxName());
	}

	/** Set Accrued Social Benefits Long Term Account.
		@param AMN_P_Provision_Social Accrued Social Benefits Long Term Account
	*/
	public void setAMN_P_Provision_Social (int AMN_P_Provision_Social)
	{
		set_Value (COLUMNNAME_AMN_P_Provision_Social, Integer.valueOf(AMN_P_Provision_Social));
	}

	/** Get Accrued Social Benefits Long Term Account.
		@return Accrued Social Benefits Long Term Account	  */
	public int getAMN_P_Provision_Social()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Provision_Social);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Provision_Utilit() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Provision_Utilities(), get_TrxName());
	}

	/** Set Accrued Utilities Provision Account.
		@param AMN_P_Provision_Utilities Accrued Utilities Provision Account
	*/
	public void setAMN_P_Provision_Utilities (int AMN_P_Provision_Utilities)
	{
		set_Value (COLUMNNAME_AMN_P_Provision_Utilities, Integer.valueOf(AMN_P_Provision_Utilities));
	}

	/** Get Accrued Utilities Provision Account.
		@return Accrued Utilities Provision Account	  */
	public int getAMN_P_Provision_Utilities()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Provision_Utilities);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Provision_Vacat() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Provision_Vacation(), get_TrxName());
	}

	/** Set Accrued Vacation Provision Liability Account.
		@param AMN_P_Provision_Vacation Accrued Vacation Provision Liability Account
	*/
	public void setAMN_P_Provision_Vacation (int AMN_P_Provision_Vacation)
	{
		set_Value (COLUMNNAME_AMN_P_Provision_Vacation, Integer.valueOf(AMN_P_Provision_Vacation));
	}

	/** Get Accrued Vacation Provision Liability Account.
		@return Accrued Vacation Provision Liability Account	  */
	public int getAMN_P_Provision_Vacation()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Provision_Vacation);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Salary_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Salary_AW_Expense(), get_TrxName());
	}

	/** Set Salary Expense Account for Administrative Workforce.
		@param AMN_P_Salary_AW_Expense Salary Expense Account for Administrative Workforce Default Value
	*/
	public void setAMN_P_Salary_AW_Expense (int AMN_P_Salary_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Salary_AW_Expense, Integer.valueOf(AMN_P_Salary_AW_Expense));
	}

	/** Get Salary Expense Account for Administrative Workforce.
		@return Salary Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_Salary_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Salary_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Salary_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Salary_DW_Expense(), get_TrxName());
	}

	/** Set Salary Expense Account for Direct Workforce.
		@param AMN_P_Salary_DW_Expense Salary Expense Account for Direct Workforce
	*/
	public void setAMN_P_Salary_DW_Expense (int AMN_P_Salary_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Salary_DW_Expense, Integer.valueOf(AMN_P_Salary_DW_Expense));
	}

	/** Get Salary Expense Account for Direct Workforce.
		@return Salary Expense Account for Direct Workforce	  */
	public int getAMN_P_Salary_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Salary_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Salary_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Salary_IW_Expense(), get_TrxName());
	}

	/** Set Salary Expense Account for Indirect Workforce.
		@param AMN_P_Salary_IW_Expense Salary Expense Account for Indirect Workforce
	*/
	public void setAMN_P_Salary_IW_Expense (int AMN_P_Salary_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Salary_IW_Expense, Integer.valueOf(AMN_P_Salary_IW_Expense));
	}

	/** Get Salary Expense Account for Indirect Workforce.
		@return Salary Expense Account for Indirect Workforce	  */
	public int getAMN_P_Salary_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Salary_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Salary_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Salary_MW_Expense(), get_TrxName());
	}

	/** Set Salary Expense Account for Management Work Force.
		@param AMN_P_Salary_MW_Expense Salary Expense Account for Management Work Force
	*/
	public void setAMN_P_Salary_MW_Expense (int AMN_P_Salary_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Salary_MW_Expense, Integer.valueOf(AMN_P_Salary_MW_Expense));
	}

	/** Get Salary Expense Account for Management Work Force.
		@return Salary Expense Account for Management Work Force	  */
	public int getAMN_P_Salary_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Salary_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Salary_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Salary_SW_Expense(), get_TrxName());
	}

	/** Set Salary Expense Account for Sales Workforce.
		@param AMN_P_Salary_SW_Expense Salary Expense Account for Sales Workforce
	*/
	public void setAMN_P_Salary_SW_Expense (int AMN_P_Salary_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Salary_SW_Expense, Integer.valueOf(AMN_P_Salary_SW_Expense));
	}

	/** Get Salary Expense Account for Sales Workforce.
		@return Salary Expense Account for Sales Workforce	  */
	public int getAMN_P_Salary_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Salary_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocBen_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocBen_AW_Expense(), get_TrxName());
	}

	/** Set Social Benefits Expense Account for Administrative Workforce.
		@param AMN_P_SocBen_AW_Expense Social Benefits Expense Account for Administrative Workforce Default Value
	*/
	public void setAMN_P_SocBen_AW_Expense (int AMN_P_SocBen_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocBen_AW_Expense, Integer.valueOf(AMN_P_SocBen_AW_Expense));
	}

	/** Get Social Benefits Expense Account for Administrative Workforce.
		@return Social Benefits Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_SocBen_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocBen_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocBen_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocBen_DW_Expense(), get_TrxName());
	}

	/** Set Social Benefits Expense Account for Direct Workforce.
		@param AMN_P_SocBen_DW_Expense Social Benefits Expense Account for Direct Workforce
	*/
	public void setAMN_P_SocBen_DW_Expense (int AMN_P_SocBen_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocBen_DW_Expense, Integer.valueOf(AMN_P_SocBen_DW_Expense));
	}

	/** Get Social Benefits Expense Account for Direct Workforce.
		@return Social Benefits Expense Account for Direct Workforce	  */
	public int getAMN_P_SocBen_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocBen_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocBen_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocBen_IW_Expense(), get_TrxName());
	}

	/** Set Social Benefits Expense Account for Indirect Workforce.
		@param AMN_P_SocBen_IW_Expense Social Benefits Expense Account for Indirect Workforce
	*/
	public void setAMN_P_SocBen_IW_Expense (int AMN_P_SocBen_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocBen_IW_Expense, Integer.valueOf(AMN_P_SocBen_IW_Expense));
	}

	/** Get Social Benefits Expense Account for Indirect Workforce.
		@return Social Benefits Expense Account for Indirect Workforce	  */
	public int getAMN_P_SocBen_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocBen_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocBen_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocBen_MW_Expense(), get_TrxName());
	}

	/** Set Social Benefits Expense Account for Management Work Force.
		@param AMN_P_SocBen_MW_Expense Social Benefits Expense Account for Management Work Force
	*/
	public void setAMN_P_SocBen_MW_Expense (int AMN_P_SocBen_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocBen_MW_Expense, Integer.valueOf(AMN_P_SocBen_MW_Expense));
	}

	/** Get Social Benefits Expense Account for Management Work Force.
		@return Social Benefits Expense Account for Management Work Force	  */
	public int getAMN_P_SocBen_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocBen_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocBen_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocBen_SW_Expense(), get_TrxName());
	}

	/** Set Social Benefits Expense Account for Sales Workforce.
		@param AMN_P_SocBen_SW_Expense Social Benefits Expense Account for Sales Workforce
	*/
	public void setAMN_P_SocBen_SW_Expense (int AMN_P_SocBen_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocBen_SW_Expense, Integer.valueOf(AMN_P_SocBen_SW_Expense));
	}

	/** Get Social Benefits Expense Account for Sales Workforce.
		@return Social Benefits Expense Account for Sales Workforce	  */
	public int getAMN_P_SocBen_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocBen_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocSec_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocSec_AW_Expense(), get_TrxName());
	}

	/** Set Social Security Expense Account for Administrative Workforce.
		@param AMN_P_SocSec_AW_Expense Social Security Expense Account for Administrative Workforce Default Value
	*/
	public void setAMN_P_SocSec_AW_Expense (int AMN_P_SocSec_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocSec_AW_Expense, Integer.valueOf(AMN_P_SocSec_AW_Expense));
	}

	/** Get Social Security Expense Account for Administrative Workforce.
		@return Social Security Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_SocSec_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocSec_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocSec_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocSec_DW_Expense(), get_TrxName());
	}

	/** Set Social Security Expense Account for Direct Workforce.
		@param AMN_P_SocSec_DW_Expense Social Security Expense Account for Direct Workforce
	*/
	public void setAMN_P_SocSec_DW_Expense (int AMN_P_SocSec_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocSec_DW_Expense, Integer.valueOf(AMN_P_SocSec_DW_Expense));
	}

	/** Get Social Security Expense Account for Direct Workforce.
		@return Social Security Expense Account for Direct Workforce	  */
	public int getAMN_P_SocSec_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocSec_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocSec_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocSec_IW_Expense(), get_TrxName());
	}

	/** Set Social Security Expense Account for Indirect Workforce.
		@param AMN_P_SocSec_IW_Expense Social Security Expense Account for Indirect Workforce
	*/
	public void setAMN_P_SocSec_IW_Expense (int AMN_P_SocSec_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocSec_IW_Expense, Integer.valueOf(AMN_P_SocSec_IW_Expense));
	}

	/** Get Social Security Expense Account for Indirect Workforce.
		@return Social Security Expense Account for Indirect Workforce	  */
	public int getAMN_P_SocSec_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocSec_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocSec_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocSec_MW_Expense(), get_TrxName());
	}

	/** Set Social Security Expense Account for Management Work Force.
		@param AMN_P_SocSec_MW_Expense Social Security Expense Account for Management Work Force
	*/
	public void setAMN_P_SocSec_MW_Expense (int AMN_P_SocSec_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocSec_MW_Expense, Integer.valueOf(AMN_P_SocSec_MW_Expense));
	}

	/** Get Social Security Expense Account for Management Work Force.
		@return Social Security Expense Account for Management Work Force	  */
	public int getAMN_P_SocSec_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocSec_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_SocSec_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_SocSec_SW_Expense(), get_TrxName());
	}

	/** Set Social Security Expense Account for Sales Workforce.
		@param AMN_P_SocSec_SW_Expense Social Security Expense Account for Sales Workforce
	*/
	public void setAMN_P_SocSec_SW_Expense (int AMN_P_SocSec_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_SocSec_SW_Expense, Integer.valueOf(AMN_P_SocSec_SW_Expense));
	}

	/** Get Social Security Expense Account for Sales Workforce.
		@return Social Security Expense Account for Sales Workforce	  */
	public int getAMN_P_SocSec_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_SocSec_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Utilities_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Utilities_AW_Expense(), get_TrxName());
	}

	/** Set Utilities Expense Account for Administrative Workforce.
		@param AMN_P_Utilities_AW_Expense Utilities Expense Account for Administrative Workforce Default Value
	*/
	public void setAMN_P_Utilities_AW_Expense (int AMN_P_Utilities_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Utilities_AW_Expense, Integer.valueOf(AMN_P_Utilities_AW_Expense));
	}

	/** Get Utilities Expense Account for Administrative Workforce.
		@return Utilities Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_Utilities_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Utilities_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Utilities_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Utilities_DW_Expense(), get_TrxName());
	}

	/** Set Utilities Expense Account for Direct Workforce.
		@param AMN_P_Utilities_DW_Expense Utilities Expense Account for Direct Workforce
	*/
	public void setAMN_P_Utilities_DW_Expense (int AMN_P_Utilities_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Utilities_DW_Expense, Integer.valueOf(AMN_P_Utilities_DW_Expense));
	}

	/** Get Utilities Expense Account for Direct Workforce.
		@return Utilities Expense Account for Direct Workforce	  */
	public int getAMN_P_Utilities_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Utilities_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Utilities_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Utilities_IW_Expense(), get_TrxName());
	}

	/** Set Utilities Expense Account for Indirect Workforce.
		@param AMN_P_Utilities_IW_Expense Utilities Expense Account for Indirect Workforce
	*/
	public void setAMN_P_Utilities_IW_Expense (int AMN_P_Utilities_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Utilities_IW_Expense, Integer.valueOf(AMN_P_Utilities_IW_Expense));
	}

	/** Get Utilities Expense Account for Indirect Workforce.
		@return Utilities Expense Account for Indirect Workforce	  */
	public int getAMN_P_Utilities_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Utilities_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Utilities_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Utilities_MW_Expense(), get_TrxName());
	}

	/** Set Utilities Expense Account for Management Work Force.
		@param AMN_P_Utilities_MW_Expense Utilities Expense Account for Management Work Force
	*/
	public void setAMN_P_Utilities_MW_Expense (int AMN_P_Utilities_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Utilities_MW_Expense, Integer.valueOf(AMN_P_Utilities_MW_Expense));
	}

	/** Get Utilities Expense Account for Management Work Force.
		@return Utilities Expense Account for Management Work Force	  */
	public int getAMN_P_Utilities_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Utilities_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Utilities_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Utilities_SW_Expense(), get_TrxName());
	}

	/** Set Utilities Expense Account for Sales Workforce.
		@param AMN_P_Utilities_SW_Expense Utilities Expense Account for Sales Workforce
	*/
	public void setAMN_P_Utilities_SW_Expense (int AMN_P_Utilities_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Utilities_SW_Expense, Integer.valueOf(AMN_P_Utilities_SW_Expense));
	}

	/** Get Utilities Expense Account for Sales Workforce.
		@return Utilities Expense Account for Sales Workforce	  */
	public int getAMN_P_Utilities_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Utilities_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Vacation_AW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Vacation_AW_Expense(), get_TrxName());
	}

	/** Set VacationExpense Account for Administrative Workforce.
		@param AMN_P_Vacation_AW_Expense Vacation Expense Account fro Administrative Workforce Default Value
	*/
	public void setAMN_P_Vacation_AW_Expense (int AMN_P_Vacation_AW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Vacation_AW_Expense, Integer.valueOf(AMN_P_Vacation_AW_Expense));
	}

	/** Get VacationExpense Account for Administrative Workforce.
		@return Vacation Expense Account fro Administrative Workforce Default Value
	  */
	public int getAMN_P_Vacation_AW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Vacation_AW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Vacation_DW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Vacation_DW_Expense(), get_TrxName());
	}

	/** Set Vacation Expense Account for Direct Workforce.
		@param AMN_P_Vacation_DW_Expense Vacation Expense Account for Direct Workforce
	*/
	public void setAMN_P_Vacation_DW_Expense (int AMN_P_Vacation_DW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Vacation_DW_Expense, Integer.valueOf(AMN_P_Vacation_DW_Expense));
	}

	/** Get Vacation Expense Account for Direct Workforce.
		@return Vacation Expense Account for Direct Workforce	  */
	public int getAMN_P_Vacation_DW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Vacation_DW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Vacation_IW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Vacation_IW_Expense(), get_TrxName());
	}

	/** Set Vacation Expense Account for Indirect Workforce.
		@param AMN_P_Vacation_IW_Expense Vacation Expense Account for Indirect Workforce
	*/
	public void setAMN_P_Vacation_IW_Expense (int AMN_P_Vacation_IW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Vacation_IW_Expense, Integer.valueOf(AMN_P_Vacation_IW_Expense));
	}

	/** Get Vacation Expense Account for Indirect Workforce.
		@return Vacation Expense Account for Indirect Workforce	  */
	public int getAMN_P_Vacation_IW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Vacation_IW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Vacation_MW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Vacation_MW_Expense(), get_TrxName());
	}

	/** Set Vacation Expense Account for Management Work Force.
		@param AMN_P_Vacation_MW_Expense Vacation Expense Account for Management Work Force
	*/
	public void setAMN_P_Vacation_MW_Expense (int AMN_P_Vacation_MW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Vacation_MW_Expense, Integer.valueOf(AMN_P_Vacation_MW_Expense));
	}

	/** Get Vacation Expense Account for Management Work Force.
		@return Vacation Expense Account for Management Work Force	  */
	public int getAMN_P_Vacation_MW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Vacation_MW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_ValidCombination getAMN_P_Vacation_SW_Expe() throws RuntimeException
	{
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_ID)
			.getPO(getAMN_P_Vacation_SW_Expense(), get_TrxName());
	}

	/** Set Vacation Expense Account for Sales Workforce.
		@param AMN_P_Vacation_SW_Expense Vacation Expense Account for Sales Workforce
	*/
	public void setAMN_P_Vacation_SW_Expense (int AMN_P_Vacation_SW_Expense)
	{
		set_Value (COLUMNNAME_AMN_P_Vacation_SW_Expense, Integer.valueOf(AMN_P_Vacation_SW_Expense));
	}

	/** Get Vacation Expense Account for Sales Workforce.
		@return Vacation Expense Account for Sales Workforce	  */
	public int getAMN_P_Vacation_SW_Expense()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_P_Vacation_SW_Expense);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Personnel and Payroll Schema .
		@param AMN_Schema_ID Personnel and Payroll Schema 
	*/
	public void setAMN_Schema_ID (int AMN_Schema_ID)
	{
		if (AMN_Schema_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AMN_Schema_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AMN_Schema_ID, Integer.valueOf(AMN_Schema_ID));
	}

	/** Get Personnel and Payroll Schema .
		@return Personnel and Payroll Schema 	  */
	public int getAMN_Schema_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AMN_Schema_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set AMN_Schema_UU.
		@param AMN_Schema_UU AMN_Schema_UU
	*/
	public void setAMN_Schema_UU (String AMN_Schema_UU)
	{
		set_ValueNoCheck (COLUMNNAME_AMN_Schema_UU, AMN_Schema_UU);
	}

	/** Get AMN_Schema_UU.
		@return AMN_Schema_UU	  */
	public String getAMN_Schema_UU()
	{
		return (String)get_Value(COLUMNNAME_AMN_Schema_UU);
	}

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException
	{
		return (org.compiere.model.I_C_AcctSchema)MTable.get(getCtx(), org.compiere.model.I_C_AcctSchema.Table_ID)
			.getPO(getC_AcctSchema_ID(), get_TrxName());
	}

	/** Set Accounting Schema.
		@param C_AcctSchema_ID Rules for accounting
	*/
	public void setC_AcctSchema_ID (int C_AcctSchema_ID)
	{
		if (C_AcctSchema_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_AcctSchema_ID, Integer.valueOf(C_AcctSchema_ID));
	}

	/** Get Accounting Schema.
		@return Rules for accounting
	  */
	public int getC_AcctSchema_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_AcctSchema_ID);
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

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair()
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Standard Precision.
		@param StdPrecision Rule for rounding  calculated amounts
	*/
	public void setStdPrecision (int StdPrecision)
	{
		set_Value (COLUMNNAME_StdPrecision, Integer.valueOf(StdPrecision));
	}

	/** Get Standard Precision.
		@return Rule for rounding  calculated amounts
	  */
	public int getStdPrecision()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_StdPrecision);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
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
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AMN_Schema
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Schema 
{

    /** TableName=AMN_Schema */
    public static final String Table_Name = "AMN_Schema";

    /** AD_Table_ID=1000003 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AMN_P_Education_AW_Expense */
    public static final String COLUMNNAME_AMN_P_Education_AW_Expense = "AMN_P_Education_AW_Expense";

	/** Set Education Expense Account for Administrative Workforce.
	  * House Education Account for Administrative Workforce Default Value
	  */
	public void setAMN_P_Education_AW_Expense (int AMN_P_Education_AW_Expense);

	/** Get Education Expense Account for Administrative Workforce.
	  * House Education Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_Education_AW_Expense();

	public I_C_ValidCombination getAMN_P_Education_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_Education_DW_Expense */
    public static final String COLUMNNAME_AMN_P_Education_DW_Expense = "AMN_P_Education_DW_Expense";

	/** Set Education Expense Account for Direct Workforce	  */
	public void setAMN_P_Education_DW_Expense (int AMN_P_Education_DW_Expense);

	/** Get Education Expense Account for Direct Workforce	  */
	public int getAMN_P_Education_DW_Expense();

	public I_C_ValidCombination getAMN_P_Education_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_Education_IW_Expense */
    public static final String COLUMNNAME_AMN_P_Education_IW_Expense = "AMN_P_Education_IW_Expense";

	/** Set Education Expense Account for Indirect Workforce	  */
	public void setAMN_P_Education_IW_Expense (int AMN_P_Education_IW_Expense);

	/** Get Education Expense Account for Indirect Workforce	  */
	public int getAMN_P_Education_IW_Expense();

	public I_C_ValidCombination getAMN_P_Education_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_Education_MW_Expense */
    public static final String COLUMNNAME_AMN_P_Education_MW_Expense = "AMN_P_Education_MW_Expense";

	/** Set Education Expense Account for Management Work Force	  */
	public void setAMN_P_Education_MW_Expense (int AMN_P_Education_MW_Expense);

	/** Get Education Expense Account for Management Work Force	  */
	public int getAMN_P_Education_MW_Expense();

	public I_C_ValidCombination getAMN_P_Education_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_Education_SW_Expense */
    public static final String COLUMNNAME_AMN_P_Education_SW_Expense = "AMN_P_Education_SW_Expense";

	/** Set Education Expense Account for Sales Workforce	  */
	public void setAMN_P_Education_SW_Expense (int AMN_P_Education_SW_Expense);

	/** Get Education Expense Account for Sales Workforce	  */
	public int getAMN_P_Education_SW_Expense();

	public I_C_ValidCombination getAMN_P_Education_SW_Expe() throws RuntimeException;

    /** Column name AMN_P_HouseSaving_AW_Expense */
    public static final String COLUMNNAME_AMN_P_HouseSaving_AW_Expense = "AMN_P_HouseSaving_AW_Expense";

	/** Set House Saving Expense Account for Administrative Workforce.
	  * House Saving Expense Account for Administrative Workforce Default Value
	  */
	public void setAMN_P_HouseSaving_AW_Expense (int AMN_P_HouseSaving_AW_Expense);

	/** Get House Saving Expense Account for Administrative Workforce.
	  * House Saving Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_HouseSaving_AW_Expense();

	public I_C_ValidCombination getAMN_P_HouseSaving_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_HouseSaving_DW_Expense */
    public static final String COLUMNNAME_AMN_P_HouseSaving_DW_Expense = "AMN_P_HouseSaving_DW_Expense";

	/** Set House Saving Expense Account for Direct Workforce	  */
	public void setAMN_P_HouseSaving_DW_Expense (int AMN_P_HouseSaving_DW_Expense);

	/** Get House Saving Expense Account for Direct Workforce	  */
	public int getAMN_P_HouseSaving_DW_Expense();

	public I_C_ValidCombination getAMN_P_HouseSaving_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_HouseSaving_IW_Expense */
    public static final String COLUMNNAME_AMN_P_HouseSaving_IW_Expense = "AMN_P_HouseSaving_IW_Expense";

	/** Set House Saving Expense Account for Indirect Workforce	  */
	public void setAMN_P_HouseSaving_IW_Expense (int AMN_P_HouseSaving_IW_Expense);

	/** Get House Saving Expense Account for Indirect Workforce	  */
	public int getAMN_P_HouseSaving_IW_Expense();

	public I_C_ValidCombination getAMN_P_HouseSaving_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_HouseSaving_MW_Expense */
    public static final String COLUMNNAME_AMN_P_HouseSaving_MW_Expense = "AMN_P_HouseSaving_MW_Expense";

	/** Set House Saving Expense Account for Management Work Force	  */
	public void setAMN_P_HouseSaving_MW_Expense (int AMN_P_HouseSaving_MW_Expense);

	/** Get House Saving Expense Account for Management Work Force	  */
	public int getAMN_P_HouseSaving_MW_Expense();

	public I_C_ValidCombination getAMN_P_HouseSaving_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_HouseSaving_SW_Expense */
    public static final String COLUMNNAME_AMN_P_HouseSaving_SW_Expense = "AMN_P_HouseSaving_SW_Expense";

	/** Set House Saving Expense Account for Sales Workforce	  */
	public void setAMN_P_HouseSaving_SW_Expense (int AMN_P_HouseSaving_SW_Expense);

	/** Get House Saving Expense Account for Sales Workforce	  */
	public int getAMN_P_HouseSaving_SW_Expense();

	public I_C_ValidCombination getAMN_P_HouseSaving_SW_Expe() throws RuntimeException;

    /** Column name AMN_P_Liability_Advances */
    public static final String COLUMNNAME_AMN_P_Liability_Advances = "AMN_P_Liability_Advances";

	/** Set Advances to Employees Short Term Account.
	  * Advances Liability short term account for PO process
	  */
	public void setAMN_P_Liability_Advances (int AMN_P_Liability_Advances);

	/** Get Advances to Employees Short Term Account.
	  * Advances Liability short term account for PO process
	  */
	public int getAMN_P_Liability_Advances();

	public I_C_ValidCombination getAMN_P_Liability_Advan() throws RuntimeException;

    /** Column name AMN_P_Liability_Bonus */
    public static final String COLUMNNAME_AMN_P_Liability_Bonus = "AMN_P_Liability_Bonus";

	/** Set Bonus Short Term Account	  */
	public void setAMN_P_Liability_Bonus (int AMN_P_Liability_Bonus);

	/** Get Bonus Short Term Account	  */
	public int getAMN_P_Liability_Bonus();

	public I_C_ValidCombination getAMN_P_Liability_Bo() throws RuntimeException;

    /** Column name AMN_P_Liability_ContrBenefits */
    public static final String COLUMNNAME_AMN_P_Liability_ContrBenefits = "AMN_P_Liability_ContrBenefits";

	/** Set Other Contract Benefits TO.
	  * Other Contract Benefits Liability short term account for TO process
	  */
	public void setAMN_P_Liability_ContrBenefits (int AMN_P_Liability_ContrBenefits);

	/** Get Other Contract Benefits TO.
	  * Other Contract Benefits Liability short term account for TO process
	  */
	public int getAMN_P_Liability_ContrBenefits();

	public I_C_ValidCombination getAMN_P_Liability_ContrBenef() throws RuntimeException;

    /** Column name AMN_P_Liability_Other */
    public static final String COLUMNNAME_AMN_P_Liability_Other = "AMN_P_Liability_Other";

	/** Set Other Food Benefits to Pay TI.
	  * Other Food Benefits to Pay short term account for TI process
	  */
	public void setAMN_P_Liability_Other (int AMN_P_Liability_Other);

	/** Get Other Food Benefits to Pay TI.
	  * Other Food Benefits to Pay short term account for TI process
	  */
	public int getAMN_P_Liability_Other();

	public I_C_ValidCombination getAMN_P_Liability_Ot() throws RuntimeException;

    /** Column name AMN_P_Liability_Salary */
    public static final String COLUMNNAME_AMN_P_Liability_Salary = "AMN_P_Liability_Salary";

	/** Set Salary Short Term to pay NN.
	  * Salary Liability short term account for NN process
	  */
	public void setAMN_P_Liability_Salary (int AMN_P_Liability_Salary);

	/** Get Salary Short Term to pay NN.
	  * Salary Liability short term account for NN process
	  */
	public int getAMN_P_Liability_Salary();

	public I_C_ValidCombination getAMN_P_Liability_Sal() throws RuntimeException;

    /** Column name AMN_P_Liability_SalaryOther */
    public static final String COLUMNNAME_AMN_P_Liability_SalaryOther = "AMN_P_Liability_SalaryOther";

	/** Set Other Salary to pay NO.
	  * Other Salary Liability short term account for NO process
	  */
	public void setAMN_P_Liability_SalaryOther (int AMN_P_Liability_SalaryOther);

	/** Get Other Salary to pay NO.
	  * Other Salary Liability short term account for NO process
	  */
	public int getAMN_P_Liability_SalaryOther();

	public I_C_ValidCombination getAMN_P_Liability_SalaryOt() throws RuntimeException;

    /** Column name AMN_P_Liability_Social */
    public static final String COLUMNNAME_AMN_P_Liability_Social = "AMN_P_Liability_Social";

	/** Set Social Benefits to Pay PI-PL-PR.
	  * Social Benefits Short Term Account for PI-PL-PR Processes
	  */
	public void setAMN_P_Liability_Social (int AMN_P_Liability_Social);

	/** Get Social Benefits to Pay PI-PL-PR.
	  * Social Benefits Short Term Account for PI-PL-PR Processes
	  */
	public int getAMN_P_Liability_Social();

	public I_C_ValidCombination getAMN_P_Liability_Soc() throws RuntimeException;

    /** Column name AMN_P_Liability_Utilities */
    public static final String COLUMNNAME_AMN_P_Liability_Utilities = "AMN_P_Liability_Utilities";

	/** Set Utilities Short Term Account.
	  * Utilities short term account for NU process
	  */
	public void setAMN_P_Liability_Utilities (int AMN_P_Liability_Utilities);

	/** Get Utilities Short Term Account.
	  * Utilities short term account for NU process
	  */
	public int getAMN_P_Liability_Utilities();

	public I_C_ValidCombination getAMN_P_Liability_Utilit() throws RuntimeException;

    /** Column name AMN_P_Liability_Vacation */
    public static final String COLUMNNAME_AMN_P_Liability_Vacation = "AMN_P_Liability_Vacation";

	/** Set Vacation Short Term Account.
	  * Vacation to Pay short term account for NU process
	  */
	public void setAMN_P_Liability_Vacation (int AMN_P_Liability_Vacation);

	/** Get Vacation Short Term Account.
	  * Vacation to Pay short term account for NU process
	  */
	public int getAMN_P_Liability_Vacation();

	public I_C_ValidCombination getAMN_P_Liability_Vacat() throws RuntimeException;

    /** Column name AMN_P_Provision_Social */
    public static final String COLUMNNAME_AMN_P_Provision_Social = "AMN_P_Provision_Social";

	/** Set Accrued Social Benefits Long Term Account	  */
	public void setAMN_P_Provision_Social (int AMN_P_Provision_Social);

	/** Get Accrued Social Benefits Long Term Account	  */
	public int getAMN_P_Provision_Social();

	public I_C_ValidCombination getAMN_P_Provision_Soc() throws RuntimeException;

    /** Column name AMN_P_Provision_Utilities */
    public static final String COLUMNNAME_AMN_P_Provision_Utilities = "AMN_P_Provision_Utilities";

	/** Set Accrued Utilities Provision Account	  */
	public void setAMN_P_Provision_Utilities (int AMN_P_Provision_Utilities);

	/** Get Accrued Utilities Provision Account	  */
	public int getAMN_P_Provision_Utilities();

	public I_C_ValidCombination getAMN_P_Provision_Utilit() throws RuntimeException;

    /** Column name AMN_P_Provision_Vacation */
    public static final String COLUMNNAME_AMN_P_Provision_Vacation = "AMN_P_Provision_Vacation";

	/** Set Accrued Vacation Provision Liability Account	  */
	public void setAMN_P_Provision_Vacation (int AMN_P_Provision_Vacation);

	/** Get Accrued Vacation Provision Liability Account	  */
	public int getAMN_P_Provision_Vacation();

	public I_C_ValidCombination getAMN_P_Provision_Vacat() throws RuntimeException;

    /** Column name AMN_P_Salary_AW_Expense */
    public static final String COLUMNNAME_AMN_P_Salary_AW_Expense = "AMN_P_Salary_AW_Expense";

	/** Set Salary Expense Account for Administrative Workforce.
	  * Salary Expense Account for Administrative Workforce Default Value
	  */
	public void setAMN_P_Salary_AW_Expense (int AMN_P_Salary_AW_Expense);

	/** Get Salary Expense Account for Administrative Workforce.
	  * Salary Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_Salary_AW_Expense();

	public I_C_ValidCombination getAMN_P_Salary_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_Salary_DW_Expense */
    public static final String COLUMNNAME_AMN_P_Salary_DW_Expense = "AMN_P_Salary_DW_Expense";

	/** Set Salary Expense Account for Direct Workforce	  */
	public void setAMN_P_Salary_DW_Expense (int AMN_P_Salary_DW_Expense);

	/** Get Salary Expense Account for Direct Workforce	  */
	public int getAMN_P_Salary_DW_Expense();

	public I_C_ValidCombination getAMN_P_Salary_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_Salary_IW_Expense */
    public static final String COLUMNNAME_AMN_P_Salary_IW_Expense = "AMN_P_Salary_IW_Expense";

	/** Set Salary Expense Account for Indirect Workforce	  */
	public void setAMN_P_Salary_IW_Expense (int AMN_P_Salary_IW_Expense);

	/** Get Salary Expense Account for Indirect Workforce	  */
	public int getAMN_P_Salary_IW_Expense();

	public I_C_ValidCombination getAMN_P_Salary_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_Salary_MW_Expense */
    public static final String COLUMNNAME_AMN_P_Salary_MW_Expense = "AMN_P_Salary_MW_Expense";

	/** Set Salary Expense Account for Management Work Force	  */
	public void setAMN_P_Salary_MW_Expense (int AMN_P_Salary_MW_Expense);

	/** Get Salary Expense Account for Management Work Force	  */
	public int getAMN_P_Salary_MW_Expense();

	public I_C_ValidCombination getAMN_P_Salary_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_Salary_SW_Expense */
    public static final String COLUMNNAME_AMN_P_Salary_SW_Expense = "AMN_P_Salary_SW_Expense";

	/** Set Salary Expense Account for Sales Workforce	  */
	public void setAMN_P_Salary_SW_Expense (int AMN_P_Salary_SW_Expense);

	/** Get Salary Expense Account for Sales Workforce	  */
	public int getAMN_P_Salary_SW_Expense();

	public I_C_ValidCombination getAMN_P_Salary_SW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocBen_AW_Expense */
    public static final String COLUMNNAME_AMN_P_SocBen_AW_Expense = "AMN_P_SocBen_AW_Expense";

	/** Set Social Benefits Expense Account for Administrative Workforce.
	  * Social Benefits Expense Account for Administrative Workforce Default Value
	  */
	public void setAMN_P_SocBen_AW_Expense (int AMN_P_SocBen_AW_Expense);

	/** Get Social Benefits Expense Account for Administrative Workforce.
	  * Social Benefits Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_SocBen_AW_Expense();

	public I_C_ValidCombination getAMN_P_SocBen_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocBen_DW_Expense */
    public static final String COLUMNNAME_AMN_P_SocBen_DW_Expense = "AMN_P_SocBen_DW_Expense";

	/** Set Social Benefits Expense Account for Direct Workforce	  */
	public void setAMN_P_SocBen_DW_Expense (int AMN_P_SocBen_DW_Expense);

	/** Get Social Benefits Expense Account for Direct Workforce	  */
	public int getAMN_P_SocBen_DW_Expense();

	public I_C_ValidCombination getAMN_P_SocBen_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocBen_IW_Expense */
    public static final String COLUMNNAME_AMN_P_SocBen_IW_Expense = "AMN_P_SocBen_IW_Expense";

	/** Set Social Benefits Expense Account for Indirect Workforce	  */
	public void setAMN_P_SocBen_IW_Expense (int AMN_P_SocBen_IW_Expense);

	/** Get Social Benefits Expense Account for Indirect Workforce	  */
	public int getAMN_P_SocBen_IW_Expense();

	public I_C_ValidCombination getAMN_P_SocBen_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocBen_MW_Expense */
    public static final String COLUMNNAME_AMN_P_SocBen_MW_Expense = "AMN_P_SocBen_MW_Expense";

	/** Set Social Benefits Expense Account for Management Work Force	  */
	public void setAMN_P_SocBen_MW_Expense (int AMN_P_SocBen_MW_Expense);

	/** Get Social Benefits Expense Account for Management Work Force	  */
	public int getAMN_P_SocBen_MW_Expense();

	public I_C_ValidCombination getAMN_P_SocBen_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocBen_SW_Expense */
    public static final String COLUMNNAME_AMN_P_SocBen_SW_Expense = "AMN_P_SocBen_SW_Expense";

	/** Set Social Benefits Expense Account for Sales Workforce	  */
	public void setAMN_P_SocBen_SW_Expense (int AMN_P_SocBen_SW_Expense);

	/** Get Social Benefits Expense Account for Sales Workforce	  */
	public int getAMN_P_SocBen_SW_Expense();

	public I_C_ValidCombination getAMN_P_SocBen_SW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocSec_AW_Expense */
    public static final String COLUMNNAME_AMN_P_SocSec_AW_Expense = "AMN_P_SocSec_AW_Expense";

	/** Set Social Security Expense Account for Administrative Workforce.
	  * Social Security Expense Account for Administrative Workforce Default Value
	  */
	public void setAMN_P_SocSec_AW_Expense (int AMN_P_SocSec_AW_Expense);

	/** Get Social Security Expense Account for Administrative Workforce.
	  * Social Security Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_SocSec_AW_Expense();

	public I_C_ValidCombination getAMN_P_SocSec_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocSec_DW_Expense */
    public static final String COLUMNNAME_AMN_P_SocSec_DW_Expense = "AMN_P_SocSec_DW_Expense";

	/** Set Social Security Expense Account for Direct Workforce	  */
	public void setAMN_P_SocSec_DW_Expense (int AMN_P_SocSec_DW_Expense);

	/** Get Social Security Expense Account for Direct Workforce	  */
	public int getAMN_P_SocSec_DW_Expense();

	public I_C_ValidCombination getAMN_P_SocSec_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocSec_IW_Expense */
    public static final String COLUMNNAME_AMN_P_SocSec_IW_Expense = "AMN_P_SocSec_IW_Expense";

	/** Set Social Security Expense Account for Indirect Workforce	  */
	public void setAMN_P_SocSec_IW_Expense (int AMN_P_SocSec_IW_Expense);

	/** Get Social Security Expense Account for Indirect Workforce	  */
	public int getAMN_P_SocSec_IW_Expense();

	public I_C_ValidCombination getAMN_P_SocSec_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocSec_MW_Expense */
    public static final String COLUMNNAME_AMN_P_SocSec_MW_Expense = "AMN_P_SocSec_MW_Expense";

	/** Set Social Security Expense Account for Management Work Force	  */
	public void setAMN_P_SocSec_MW_Expense (int AMN_P_SocSec_MW_Expense);

	/** Get Social Security Expense Account for Management Work Force	  */
	public int getAMN_P_SocSec_MW_Expense();

	public I_C_ValidCombination getAMN_P_SocSec_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_SocSec_SW_Expense */
    public static final String COLUMNNAME_AMN_P_SocSec_SW_Expense = "AMN_P_SocSec_SW_Expense";

	/** Set Social Security Expense Account for Sales Workforce	  */
	public void setAMN_P_SocSec_SW_Expense (int AMN_P_SocSec_SW_Expense);

	/** Get Social Security Expense Account for Sales Workforce	  */
	public int getAMN_P_SocSec_SW_Expense();

	public I_C_ValidCombination getAMN_P_SocSec_SW_Expe() throws RuntimeException;

    /** Column name AMN_P_Utilities_AW_Expense */
    public static final String COLUMNNAME_AMN_P_Utilities_AW_Expense = "AMN_P_Utilities_AW_Expense";

	/** Set Utilities Expense Account for Administrative Workforce.
	  * Utilities Expense Account for Administrative Workforce Default Value
	  */
	public void setAMN_P_Utilities_AW_Expense (int AMN_P_Utilities_AW_Expense);

	/** Get Utilities Expense Account for Administrative Workforce.
	  * Utilities Expense Account for Administrative Workforce Default Value
	  */
	public int getAMN_P_Utilities_AW_Expense();

	public I_C_ValidCombination getAMN_P_Utilities_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_Utilities_DW_Expense */
    public static final String COLUMNNAME_AMN_P_Utilities_DW_Expense = "AMN_P_Utilities_DW_Expense";

	/** Set Utilities Expense Account for Direct Workforce	  */
	public void setAMN_P_Utilities_DW_Expense (int AMN_P_Utilities_DW_Expense);

	/** Get Utilities Expense Account for Direct Workforce	  */
	public int getAMN_P_Utilities_DW_Expense();

	public I_C_ValidCombination getAMN_P_Utilities_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_Utilities_IW_Expense */
    public static final String COLUMNNAME_AMN_P_Utilities_IW_Expense = "AMN_P_Utilities_IW_Expense";

	/** Set Utilities Expense Account for Indirect Workforce	  */
	public void setAMN_P_Utilities_IW_Expense (int AMN_P_Utilities_IW_Expense);

	/** Get Utilities Expense Account for Indirect Workforce	  */
	public int getAMN_P_Utilities_IW_Expense();

	public I_C_ValidCombination getAMN_P_Utilities_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_Utilities_MW_Expense */
    public static final String COLUMNNAME_AMN_P_Utilities_MW_Expense = "AMN_P_Utilities_MW_Expense";

	/** Set Utilities Expense Account for Management Work Force	  */
	public void setAMN_P_Utilities_MW_Expense (int AMN_P_Utilities_MW_Expense);

	/** Get Utilities Expense Account for Management Work Force	  */
	public int getAMN_P_Utilities_MW_Expense();

	public I_C_ValidCombination getAMN_P_Utilities_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_Utilities_SW_Expense */
    public static final String COLUMNNAME_AMN_P_Utilities_SW_Expense = "AMN_P_Utilities_SW_Expense";

	/** Set Utilities Expense Account for Sales Workforce	  */
	public void setAMN_P_Utilities_SW_Expense (int AMN_P_Utilities_SW_Expense);

	/** Get Utilities Expense Account for Sales Workforce	  */
	public int getAMN_P_Utilities_SW_Expense();

	public I_C_ValidCombination getAMN_P_Utilities_SW_Expe() throws RuntimeException;

    /** Column name AMN_P_Vacation_AW_Expense */
    public static final String COLUMNNAME_AMN_P_Vacation_AW_Expense = "AMN_P_Vacation_AW_Expense";

	/** Set VacationExpense Account for Administrative Workforce.
	  * Vacation Expense Account fro Administrative Workforce Default Value
	  */
	public void setAMN_P_Vacation_AW_Expense (int AMN_P_Vacation_AW_Expense);

	/** Get VacationExpense Account for Administrative Workforce.
	  * Vacation Expense Account fro Administrative Workforce Default Value
	  */
	public int getAMN_P_Vacation_AW_Expense();

	public I_C_ValidCombination getAMN_P_Vacation_AW_Expe() throws RuntimeException;

    /** Column name AMN_P_Vacation_DW_Expense */
    public static final String COLUMNNAME_AMN_P_Vacation_DW_Expense = "AMN_P_Vacation_DW_Expense";

	/** Set Vacation Expense Account for Direct Workforce	  */
	public void setAMN_P_Vacation_DW_Expense (int AMN_P_Vacation_DW_Expense);

	/** Get Vacation Expense Account for Direct Workforce	  */
	public int getAMN_P_Vacation_DW_Expense();

	public I_C_ValidCombination getAMN_P_Vacation_DW_Expe() throws RuntimeException;

    /** Column name AMN_P_Vacation_IW_Expense */
    public static final String COLUMNNAME_AMN_P_Vacation_IW_Expense = "AMN_P_Vacation_IW_Expense";

	/** Set Vacation Expense Account for Indirect Workforce	  */
	public void setAMN_P_Vacation_IW_Expense (int AMN_P_Vacation_IW_Expense);

	/** Get Vacation Expense Account for Indirect Workforce	  */
	public int getAMN_P_Vacation_IW_Expense();

	public I_C_ValidCombination getAMN_P_Vacation_IW_Expe() throws RuntimeException;

    /** Column name AMN_P_Vacation_MW_Expense */
    public static final String COLUMNNAME_AMN_P_Vacation_MW_Expense = "AMN_P_Vacation_MW_Expense";

	/** Set Vacation Expense Account for Management Work Force	  */
	public void setAMN_P_Vacation_MW_Expense (int AMN_P_Vacation_MW_Expense);

	/** Get Vacation Expense Account for Management Work Force	  */
	public int getAMN_P_Vacation_MW_Expense();

	public I_C_ValidCombination getAMN_P_Vacation_MW_Expe() throws RuntimeException;

    /** Column name AMN_P_Vacation_SW_Expense */
    public static final String COLUMNNAME_AMN_P_Vacation_SW_Expense = "AMN_P_Vacation_SW_Expense";

	/** Set Vacation Expense Account for Sales Workforce	  */
	public void setAMN_P_Vacation_SW_Expense (int AMN_P_Vacation_SW_Expense);

	/** Get Vacation Expense Account for Sales Workforce	  */
	public int getAMN_P_Vacation_SW_Expense();

	public I_C_ValidCombination getAMN_P_Vacation_SW_Expe() throws RuntimeException;

    /** Column name AMN_Schema_ID */
    public static final String COLUMNNAME_AMN_Schema_ID = "AMN_Schema_ID";

	/** Set Personnel and Payroll Schema 	  */
	public void setAMN_Schema_ID (int AMN_Schema_ID);

	/** Get Personnel and Payroll Schema 	  */
	public int getAMN_Schema_ID();

    /** Column name AMN_Schema_UU */
    public static final String COLUMNNAME_AMN_Schema_UU = "AMN_Schema_UU";

	/** Set AMN_Schema_UU	  */
	public void setAMN_Schema_UU (String AMN_Schema_UU);

	/** Get AMN_Schema_UU	  */
	public String getAMN_Schema_UU();

    /** Column name C_AcctSchema_ID */
    public static final String COLUMNNAME_C_AcctSchema_ID = "C_AcctSchema_ID";

	/** Set Accounting Schema.
	  * Rules for accounting
	  */
	public void setC_AcctSchema_ID (int C_AcctSchema_ID);

	/** Get Accounting Schema.
	  * Rules for accounting
	  */
	public int getC_AcctSchema_ID();

	public org.compiere.model.I_C_AcctSchema getC_AcctSchema() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name StdPrecision */
    public static final String COLUMNNAME_StdPrecision = "StdPrecision";

	/** Set Standard Precision.
	  * Rule for rounding  calculated amounts
	  */
	public void setStdPrecision (int StdPrecision);

	/** Get Standard Precision.
	  * Rule for rounding  calculated amounts
	  */
	public int getStdPrecision();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}

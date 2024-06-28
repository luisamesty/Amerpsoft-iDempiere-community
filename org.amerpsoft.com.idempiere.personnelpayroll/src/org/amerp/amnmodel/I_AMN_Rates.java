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

/** Generated Interface for AMN_Rates
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Rates 
{

    /** TableName=AMN_Rates */
    public static final String Table_Name = "AMN_Rates";

    /** AD_Table_ID=1000038 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

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

    /** Column name AMN_Rates_ID */
    public static final String COLUMNNAME_AMN_Rates_ID = "AMN_Rates_ID";

	/** Set Payroll Rates	  */
	public void setAMN_Rates_ID (int AMN_Rates_ID);

	/** Get Payroll Rates	  */
	public int getAMN_Rates_ID();

    /** Column name AMN_Rates_UU */
    public static final String COLUMNNAME_AMN_Rates_UU = "AMN_Rates_UU";

	/** Set AMN_Rates_UU	  */
	public void setAMN_Rates_UU (String AMN_Rates_UU);

	/** Get AMN_Rates_UU	  */
	public String getAMN_Rates_UU();

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException;

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

    /** Column name EndDate */
    public static final String COLUMNNAME_EndDate = "EndDate";

	/** Set End Date.
	  * Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate);

	/** Get End Date.
	  * Last effective date (inclusive)
	  */
	public Timestamp getEndDate();

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

    /** Column name Salary_Day */
    public static final String COLUMNNAME_Salary_Day = "Salary_Day";

	/** Set Salary Day	  */
	public void setSalary_Day (BigDecimal Salary_Day);

	/** Get Salary Day	  */
	public BigDecimal getSalary_Day();

    /** Column name StartDate */
    public static final String COLUMNNAME_StartDate = "StartDate";

	/** Set Start Date.
	  * First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate);

	/** Get Start Date.
	  * First effective day (inclusive)
	  */
	public Timestamp getStartDate();

    /** Column name TaxUnit */
    public static final String COLUMNNAME_TaxUnit = "TaxUnit";

	/** Set TaxUnit	  */
	public void setTaxUnit (BigDecimal TaxUnit);

	/** Get TaxUnit	  */
	public BigDecimal getTaxUnit();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();

    /** Column name active_int_rate */
    public static final String COLUMNNAME_active_int_rate = "active_int_rate";

	/** Set active_int_rate	  */
	public void setactive_int_rate (BigDecimal active_int_rate);

	/** Get active_int_rate	  */
	public BigDecimal getactive_int_rate();

    /** Column name active_int_rate2 */
    public static final String COLUMNNAME_active_int_rate2 = "active_int_rate2";

	/** Set active_int_rate2	  */
	public void setactive_int_rate2 (BigDecimal active_int_rate2);

	/** Get active_int_rate2	  */
	public BigDecimal getactive_int_rate2();

    /** Column name inflation */
    public static final String COLUMNNAME_inflation = "inflation";

	/** Set inflation	  */
	public void setinflation (BigDecimal inflation);

	/** Get inflation	  */
	public BigDecimal getinflation();

    /** Column name ipc */
    public static final String COLUMNNAME_ipc = "ipc";

	/** Set ipc	  */
	public void setipc (BigDecimal ipc);

	/** Get ipc	  */
	public BigDecimal getipc();

    /** Column name month */
    public static final String COLUMNNAME_month = "month";

	/** Set month	  */
	public void setmonth (int month);

	/** Get month	  */
	public int getmonth();

    /** Column name salary_day_20 */
    public static final String COLUMNNAME_salary_day_20 = "salary_day_20";

	/** Set salary_day_20	  */
	public void setsalary_day_20 (BigDecimal salary_day_20);

	/** Get salary_day_20	  */
	public BigDecimal getsalary_day_20();

    /** Column name salary_day_ru */
    public static final String COLUMNNAME_salary_day_ru = "salary_day_ru";

	/** Set Salary Rural	  */
	public void setsalary_day_ru (BigDecimal salary_day_ru);

	/** Get Salary Rural	  */
	public BigDecimal getsalary_day_ru();

    /** Column name salary_mo */
    public static final String COLUMNNAME_salary_mo = "salary_mo";

	/** Set salary_mo	  */
	public void setsalary_mo (BigDecimal salary_mo);

	/** Get salary_mo	  */
	public BigDecimal getsalary_mo();

    /** Column name salary_mo_20 */
    public static final String COLUMNNAME_salary_mo_20 = "salary_mo_20";

	/** Set salary_mo_20.
	  * Salary Mo < 20T
	  */
	public void setsalary_mo_20 (BigDecimal salary_mo_20);

	/** Get salary_mo_20.
	  * Salary Mo < 20T
	  */
	public BigDecimal getsalary_mo_20();

    /** Column name salary_mo_ru */
    public static final String COLUMNNAME_salary_mo_ru = "salary_mo_ru";

	/** Set salary_mo_ru	  */
	public void setsalary_mo_ru (BigDecimal salary_mo_ru);

	/** Get salary_mo_ru	  */
	public BigDecimal getsalary_mo_ru();

    /** Column name year */
    public static final String COLUMNNAME_year = "year";

	/** Set year	  */
	public void setyear (int year);

	/** Get year	  */
	public int getyear();
}

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

/** Generated Interface for AMN_Payroll_Historic
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Payroll_Historic 
{

    /** TableName=AMN_Payroll_Historic */
    public static final String Table_Name = "AMN_Payroll_Historic";

    /** AD_Table_ID=1000063 */
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

    /** Column name AMN_Employee_ID */
    public static final String COLUMNNAME_AMN_Employee_ID = "AMN_Employee_ID";

	/** Set Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID);

	/** Get Payroll employee	  */
	public int getAMN_Employee_ID();

	public I_AMN_Employee getAMN_Employee() throws RuntimeException;

    /** Column name AMN_Payroll_Historic_ID */
    public static final String COLUMNNAME_AMN_Payroll_Historic_ID = "AMN_Payroll_Historic_ID";

	/** Set Payroll Historic Accumulated 	  */
	public void setAMN_Payroll_Historic_ID (int AMN_Payroll_Historic_ID);

	/** Get Payroll Historic Accumulated 	  */
	public int getAMN_Payroll_Historic_ID();

    /** Column name AMN_Period_YYYYMM */
    public static final String COLUMNNAME_AMN_Period_YYYYMM = "AMN_Period_YYYYMM";

	/** Set Payroll Period YYYY-MM.
	  * Payroll Period in YYYY and MM
	  */
	public void setAMN_Period_YYYYMM (String AMN_Period_YYYYMM);

	/** Get Payroll Period YYYY-MM.
	  * Payroll Period in YYYY and MM
	  */
	public String getAMN_Period_YYYYMM();

    /** Column name C_ConversionType_ID */
    public static final String COLUMNNAME_C_ConversionType_ID = "C_ConversionType_ID";

	/** Set Currency Type.
	  * Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID);

	/** Get Currency Type.
	  * Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID();

	public org.compiere.model.I_C_ConversionType getC_ConversionType() throws RuntimeException;

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

    /** Column name Salary */
    public static final String COLUMNNAME_Salary = "Salary";

	/** Set Salary	  */
	public void setSalary (BigDecimal Salary);

	/** Get Salary	  */
	public BigDecimal getSalary();

    /** Column name Salary_Base */
    public static final String COLUMNNAME_Salary_Base = "Salary_Base";

	/** Set Salary_Base	  */
	public void setSalary_Base (BigDecimal Salary_Base);

	/** Get Salary_Base	  */
	public BigDecimal getSalary_Base();

    /** Column name Salary_Integral */
    public static final String COLUMNNAME_Salary_Integral = "Salary_Integral";

	/** Set Salary_Integral.
	  * Integral Salary
	  */
	public void setSalary_Integral (BigDecimal Salary_Integral);

	/** Get Salary_Integral.
	  * Integral Salary
	  */
	public BigDecimal getSalary_Integral();

    /** Column name Salary_Socialbenefits */
    public static final String COLUMNNAME_Salary_Socialbenefits = "Salary_Socialbenefits";

	/** Set Salary_Socialbenefits.
	  * Salary based to calc Social Benefits
	  */
	public void setSalary_Socialbenefits (BigDecimal Salary_Socialbenefits);

	/** Get Salary_Socialbenefits.
	  * Salary based to calc Social Benefits
	  */
	public BigDecimal getSalary_Socialbenefits();

    /** Column name Salary_Socialbenefits_NN */
    public static final String COLUMNNAME_Salary_Socialbenefits_NN = "Salary_Socialbenefits_NN";

	/** Set Salary_Socialbenefits_NN	  */
	public void setSalary_Socialbenefits_NN (BigDecimal Salary_Socialbenefits_NN);

	/** Get Salary_Socialbenefits_NN	  */
	public BigDecimal getSalary_Socialbenefits_NN();

    /** Column name Salary_Socialbenefits_NU */
    public static final String COLUMNNAME_Salary_Socialbenefits_NU = "Salary_Socialbenefits_NU";

	/** Set Salary_Socialbenefits_NU.
	  * Salary based to calc Social Benefits portion from NU (Utilities)
	  */
	public void setSalary_Socialbenefits_NU (BigDecimal Salary_Socialbenefits_NU);

	/** Get Salary_Socialbenefits_NU.
	  * Salary based to calc Social Benefits portion from NU (Utilities)
	  */
	public BigDecimal getSalary_Socialbenefits_NU();

    /** Column name Salary_Socialbenefits_NV */
    public static final String COLUMNNAME_Salary_Socialbenefits_NV = "Salary_Socialbenefits_NV";

	/** Set Salary_Socialbenefits_NV.
	  * Salary based to calc Social Benefits portion from NV (Vacations)
	  */
	public void setSalary_Socialbenefits_NV (BigDecimal Salary_Socialbenefits_NV);

	/** Get Salary_Socialbenefits_NV.
	  * Salary based to calc Social Benefits portion from NV (Vacations)
	  */
	public BigDecimal getSalary_Socialbenefits_NV();

    /** Column name Salary_Socialbenefits_Updated */
    public static final String COLUMNNAME_Salary_Socialbenefits_Updated = "Salary_Socialbenefits_Updated";

	/** Set Salary_Socialbenefits_Updated.
	  * Salary Updated by User based to calc Social Benefits
	  */
	public void setSalary_Socialbenefits_Updated (BigDecimal Salary_Socialbenefits_Updated);

	/** Get Salary_Socialbenefits_Updated.
	  * Salary Updated by User based to calc Social Benefits
	  */
	public BigDecimal getSalary_Socialbenefits_Updated();

    /** Column name Salary_Utilities */
    public static final String COLUMNNAME_Salary_Utilities = "Salary_Utilities";

	/** Set Salary_Utilities.
	  * Salary based to calc Utilities
	  */
	public void setSalary_Utilities (BigDecimal Salary_Utilities);

	/** Get Salary_Utilities.
	  * Salary based to calc Utilities
	  */
	public BigDecimal getSalary_Utilities();

    /** Column name Salary_Utilities_NN */
    public static final String COLUMNNAME_Salary_Utilities_NN = "Salary_Utilities_NN";

	/** Set Salary_Utilities_NN.
	  * Salary based to calc Utilities portion from NN
	  */
	public void setSalary_Utilities_NN (BigDecimal Salary_Utilities_NN);

	/** Get Salary_Utilities_NN.
	  * Salary based to calc Utilities portion from NN
	  */
	public BigDecimal getSalary_Utilities_NN();

    /** Column name Salary_Utilities_NV */
    public static final String COLUMNNAME_Salary_Utilities_NV = "Salary_Utilities_NV";

	/** Set Salary_Utilities_NV.
	  * Salary based to calc Utilities portion from NV (Vacation)
	  */
	public void setSalary_Utilities_NV (BigDecimal Salary_Utilities_NV);

	/** Get Salary_Utilities_NV.
	  * Salary based to calc Utilities portion from NV (Vacation)
	  */
	public BigDecimal getSalary_Utilities_NV();

    /** Column name Salary_Utilities_Updated */
    public static final String COLUMNNAME_Salary_Utilities_Updated = "Salary_Utilities_Updated";

	/** Set Salary_Utilities_Updated.
	  * Salary Updated by User based to calc Utilities
	  */
	public void setSalary_Utilities_Updated (BigDecimal Salary_Utilities_Updated);

	/** Get Salary_Utilities_Updated.
	  * Salary Updated by User based to calc Utilities
	  */
	public BigDecimal getSalary_Utilities_Updated();

    /** Column name Salary_Vacation */
    public static final String COLUMNNAME_Salary_Vacation = "Salary_Vacation";

	/** Set Salary_Vacation.
	  * Salary based to calc Vacation
	  */
	public void setSalary_Vacation (BigDecimal Salary_Vacation);

	/** Get Salary_Vacation.
	  * Salary based to calc Vacation
	  */
	public BigDecimal getSalary_Vacation();

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

    /** Column name ValidFrom */
    public static final String COLUMNNAME_ValidFrom = "ValidFrom";

	/** Set Valid from.
	  * Valid from including this date (first day)
	  */
	public void setValidFrom (Timestamp ValidFrom);

	/** Get Valid from.
	  * Valid from including this date (first day)
	  */
	public Timestamp getValidFrom();

    /** Column name ValidTo */
    public static final String COLUMNNAME_ValidTo = "ValidTo";

	/** Set Valid to.
	  * Valid to including this date (last day)
	  */
	public void setValidTo (Timestamp ValidTo);

	/** Get Valid to.
	  * Valid to including this date (last day)
	  */
	public Timestamp getValidTo();

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
}

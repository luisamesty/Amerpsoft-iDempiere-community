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

/** Generated Interface for AMN_Concept_Types
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_AMN_Concept_Types 
{

    /** TableName=AMN_Concept_Types */
    public static final String Table_Name = "AMN_Concept_Types";

    /** AD_Table_ID=1000035 */
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

    /** Column name AMN_CT_Cre_Acct_ID */
    public static final String COLUMNNAME_AMN_CT_Cre_Acct_ID = "AMN_CT_Cre_Acct_ID";

	/** Set Payroll Concept Type Credit Account.
	  * Payroll Concept Type Credit Account
	  */
	public void setAMN_CT_Cre_Acct_ID (int AMN_CT_Cre_Acct_ID);

	/** Get Payroll Concept Type Credit Account.
	  * Payroll Concept Type Credit Account
	  */
	public int getAMN_CT_Cre_Acct_ID();

	public org.compiere.model.I_C_ElementValue getAMN_CT_Cre_Acct() throws RuntimeException;

    /** Column name AMN_CT_Deb_Acct_ID */
    public static final String COLUMNNAME_AMN_CT_Deb_Acct_ID = "AMN_CT_Deb_Acct_ID";

	/** Set Payroll Concept Type Debit Account.
	  * Payroll Concept Type Debit Account
	  */
	public void setAMN_CT_Deb_Acct_ID (int AMN_CT_Deb_Acct_ID);

	/** Get Payroll Concept Type Debit Account.
	  * Payroll Concept Type Debit Account
	  */
	public int getAMN_CT_Deb_Acct_ID();

	public org.compiere.model.I_C_ElementValue getAMN_CT_Deb_Acct() throws RuntimeException;

    /** Column name AMN_Concept_Types_ID */
    public static final String COLUMNNAME_AMN_Concept_Types_ID = "AMN_Concept_Types_ID";

	/** Set Payroll Concepts Types	  */
	public void setAMN_Concept_Types_ID (int AMN_Concept_Types_ID);

	/** Get Payroll Concepts Types	  */
	public int getAMN_Concept_Types_ID();

    /** Column name AMN_Concept_Types_UU */
    public static final String COLUMNNAME_AMN_Concept_Types_UU = "AMN_Concept_Types_UU";

	/** Set AMN_Concept_Types_UU	  */
	public void setAMN_Concept_Types_UU (String AMN_Concept_Types_UU);

	/** Get AMN_Concept_Types_UU	  */
	public String getAMN_Concept_Types_UU();

    /** Column name AMN_Concept_Uom_ID */
    public static final String COLUMNNAME_AMN_Concept_Uom_ID = "AMN_Concept_Uom_ID";

	/** Set Payroll Concepts UOM	  */
	public void setAMN_Concept_Uom_ID (int AMN_Concept_Uom_ID);

	/** Get Payroll Concepts UOM	  */
	public int getAMN_Concept_Uom_ID();

    /** Column name AMN_Cre_Acct */
    public static final String COLUMNNAME_AMN_Cre_Acct = "AMN_Cre_Acct";

	/** Set Concept Credit Administrative Work Force Account.
	  * Concept Credit Administrative Work Force  Account
	  */
	public void setAMN_Cre_Acct (int AMN_Cre_Acct);

	/** Get Concept Credit Administrative Work Force Account.
	  * Concept Credit Administrative Work Force  Account
	  */
	public int getAMN_Cre_Acct();

	public I_C_ValidCombination getAMN_Cre_A() throws RuntimeException;

    /** Column name AMN_Cre_DW_Acct */
    public static final String COLUMNNAME_AMN_Cre_DW_Acct = "AMN_Cre_DW_Acct";

	/** Set Concept Credit Direct Work Force Account.
	  * Concept Credit Direct Work Force Account
	  */
	public void setAMN_Cre_DW_Acct (int AMN_Cre_DW_Acct);

	/** Get Concept Credit Direct Work Force Account.
	  * Concept Credit Direct Work Force Account
	  */
	public int getAMN_Cre_DW_Acct();

	public I_C_ValidCombination getAMN_Cre_DW_A() throws RuntimeException;

    /** Column name AMN_Cre_IW_Acct */
    public static final String COLUMNNAME_AMN_Cre_IW_Acct = "AMN_Cre_IW_Acct";

	/** Set Concept Credit Indirect Work Force Account.
	  * Concept Credit Indirect Work Force Account
	  */
	public void setAMN_Cre_IW_Acct (int AMN_Cre_IW_Acct);

	/** Get Concept Credit Indirect Work Force Account.
	  * Concept Credit Indirect Work Force Account
	  */
	public int getAMN_Cre_IW_Acct();

	public I_C_ValidCombination getAMN_Cre_IW_A() throws RuntimeException;

    /** Column name AMN_Cre_MW_Acct */
    public static final String COLUMNNAME_AMN_Cre_MW_Acct = "AMN_Cre_MW_Acct";

	/** Set Concept Credit Management Work Force Account.
	  * Concept Credit Management Work Force Account
	  */
	public void setAMN_Cre_MW_Acct (int AMN_Cre_MW_Acct);

	/** Get Concept Credit Management Work Force Account.
	  * Concept Credit Management Work Force Account
	  */
	public int getAMN_Cre_MW_Acct();

	public I_C_ValidCombination getAMN_Cre_MW_A() throws RuntimeException;

    /** Column name AMN_Cre_SW_Acct */
    public static final String COLUMNNAME_AMN_Cre_SW_Acct = "AMN_Cre_SW_Acct";

	/** Set Concept Credit Sales Work Force Account.
	  * Concept Credit Sales Work Force Account
	  */
	public void setAMN_Cre_SW_Acct (int AMN_Cre_SW_Acct);

	/** Get Concept Credit Sales Work Force Account.
	  * Concept Credit Sales Work Force Account
	  */
	public int getAMN_Cre_SW_Acct();

	public I_C_ValidCombination getAMN_Cre_SW_A() throws RuntimeException;

    /** Column name AMN_Deb_Acct */
    public static final String COLUMNNAME_AMN_Deb_Acct = "AMN_Deb_Acct";

	/** Set Concept Debit Administrative Work Force  Account.
	  * Concept Debit Administrative Work Force   Account
	  */
	public void setAMN_Deb_Acct (int AMN_Deb_Acct);

	/** Get Concept Debit Administrative Work Force  Account.
	  * Concept Debit Administrative Work Force   Account
	  */
	public int getAMN_Deb_Acct();

	public I_C_ValidCombination getAMN_Deb_A() throws RuntimeException;

    /** Column name AMN_Deb_DW_Acct */
    public static final String COLUMNNAME_AMN_Deb_DW_Acct = "AMN_Deb_DW_Acct";

	/** Set Concept Debit Direct Work Force Account.
	  * Concept Debit Direct Work Account
	  */
	public void setAMN_Deb_DW_Acct (int AMN_Deb_DW_Acct);

	/** Get Concept Debit Direct Work Force Account.
	  * Concept Debit Direct Work Account
	  */
	public int getAMN_Deb_DW_Acct();

	public I_C_ValidCombination getAMN_Deb_DW_A() throws RuntimeException;

    /** Column name AMN_Deb_IW_Acct */
    public static final String COLUMNNAME_AMN_Deb_IW_Acct = "AMN_Deb_IW_Acct";

	/** Set Concept Debit Indirect Work Force Account.
	  * Concept Debit Indirect Work Account
	  */
	public void setAMN_Deb_IW_Acct (int AMN_Deb_IW_Acct);

	/** Get Concept Debit Indirect Work Force Account.
	  * Concept Debit Indirect Work Account
	  */
	public int getAMN_Deb_IW_Acct();

	public I_C_ValidCombination getAMN_Deb_IW_A() throws RuntimeException;

    /** Column name AMN_Deb_MW_Acct */
    public static final String COLUMNNAME_AMN_Deb_MW_Acct = "AMN_Deb_MW_Acct";

	/** Set Concept Debit Management Work Force Account.
	  * Concept Debit Management Work Account
	  */
	public void setAMN_Deb_MW_Acct (int AMN_Deb_MW_Acct);

	/** Get Concept Debit Management Work Force Account.
	  * Concept Debit Management Work Account
	  */
	public int getAMN_Deb_MW_Acct();

	public I_C_ValidCombination getAMN_Deb_MW_A() throws RuntimeException;

    /** Column name AMN_Deb_SW_Acct */
    public static final String COLUMNNAME_AMN_Deb_SW_Acct = "AMN_Deb_SW_Acct";

	/** Set Concept Debit Sales Work Force Account.
	  * Concept Debit Sales Work Account
	  */
	public void setAMN_Deb_SW_Acct (int AMN_Deb_SW_Acct);

	/** Get Concept Debit Sales Work Force Account.
	  * Concept Debit Sales Work Account
	  */
	public int getAMN_Deb_SW_Acct();

	public I_C_ValidCombination getAMN_Deb_SW_A() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name CalcOrder */
    public static final String COLUMNNAME_CalcOrder = "CalcOrder";

	/** Set CalcOrder	  */
	public void setCalcOrder (int CalcOrder);

	/** Get CalcOrder	  */
	public int getCalcOrder();

    /** Column name CopyFrom */
    public static final String COLUMNNAME_CopyFrom = "CopyFrom";

	/** Set Copy From.
	  * Copy From Record
	  */
	public void setCopyFrom (String CopyFrom);

	/** Get Copy From.
	  * Copy From Record
	  */
	public String getCopyFrom();

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

    /** Column name DefaultValue */
    public static final String COLUMNNAME_DefaultValue = "DefaultValue";

	/** Set Default Logic.
	  * Default value hierarchy, separated by ;

	  */
	public void setDefaultValue (String DefaultValue);

	/** Get Default Logic.
	  * Default value hierarchy, separated by ;

	  */
	public String getDefaultValue();

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

    /** Column name Note */
    public static final String COLUMNNAME_Note = "Note";

	/** Set Note.
	  * Optional additional user defined information
	  */
	public void setNote (String Note);

	/** Get Note.
	  * Optional additional user defined information
	  */
	public String getNote();

    /** Column name Script */
    public static final String COLUMNNAME_Script = "Script";

	/** Set Script.
	  * Dynamic Java Language Script to calculate result
	  */
	public void setScript (String Script);

	/** Get Script.
	  * Dynamic Java Language Script to calculate result
	  */
	public String getScript();

    /** Column name ScriptDefaultValue */
    public static final String COLUMNNAME_ScriptDefaultValue = "ScriptDefaultValue";

	/** Set Script Default Value.
	  * Dynamic Java Language Script to calculate result for Default Value  (alternative to DefaultValue)
	  */
	public void setScriptDefaultValue (String ScriptDefaultValue);

	/** Get Script Default Value.
	  * Dynamic Java Language Script to calculate result for Default Value  (alternative to DefaultValue)
	  */
	public String getScriptDefaultValue();

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

    /** Column name arc */
    public static final String COLUMNNAME_arc = "arc";

	/** Set arc	  */
	public void setarc (boolean arc);

	/** Get arc	  */
	public boolean isarc();

    /** Column name conceptsource */
    public static final String COLUMNNAME_conceptsource = "conceptsource";

	/** Set Concept Source.
	  * Origen or Source for Concept Type
	  */
	public void setconceptsource (String conceptsource);

	/** Get Concept Source.
	  * Origen or Source for Concept Type
	  */
	public String getconceptsource();

    /** Column name daydiscriminated */
    public static final String COLUMNNAME_daydiscriminated = "daydiscriminated";

	/** Set daydiscriminated	  */
	public void setdaydiscriminated (boolean daydiscriminated);

	/** Get daydiscriminated	  */
	public boolean isdaydiscriminated();

    /** Column name faov */
    public static final String COLUMNNAME_faov = "faov";

	/** Set faov	  */
	public void setfaov (boolean faov);

	/** Get faov	  */
	public boolean isfaov();

    /** Column name feriado */
    public static final String COLUMNNAME_feriado = "feriado";

	/** Set Holiday.
	  * Indicates id Apllies for Holidays Calc
	  */
	public void setferiado (boolean feriado);

	/** Get Holiday.
	  * Indicates id Apllies for Holidays Calc
	  */
	public boolean isferiado();

    /** Column name formula */
    public static final String COLUMNNAME_formula = "formula";

	/** Set formula	  */
	public void setformula (String formula);

	/** Get formula	  */
	public String getformula();

    /** Column name ince */
    public static final String COLUMNNAME_ince = "ince";

	/** Set ince	  */
	public void setince (boolean ince);

	/** Get ince	  */
	public boolean isince();

    /** Column name isDate */
    public static final String COLUMNNAME_isDate = "isDate";

	/** Set isDate	  */
	public void setisDate (boolean isDate);

	/** Get isDate	  */
	public boolean isDate();

    /** Column name isQty */
    public static final String COLUMNNAME_isQty = "isQty";

	/** Set isQty	  */
	public void setisQty (boolean isQty);

	/** Get isQty	  */
	public boolean isQty();

    /** Column name isValue */
    public static final String COLUMNNAME_isValue = "isValue";

	/** Set isValue	  */
	public void setisValue (boolean isValue);

	/** Get isValue	  */
	public boolean isValue();

    /** Column name isrepeat */
    public static final String COLUMNNAME_isrepeat = "isrepeat";

	/** Set isrepeat	  */
	public void setisrepeat (String isrepeat);

	/** Get isrepeat	  */
	public String getisrepeat();

    /** Column name isshow */
    public static final String COLUMNNAME_isshow = "isshow";

	/** Set isshow	  */
	public void setisshow (String isshow);

	/** Get isshow	  */
	public String getisshow();

    /** Column name optmode */
    public static final String COLUMNNAME_optmode = "optmode";

	/** Set optmode	  */
	public void setoptmode (String optmode);

	/** Get optmode	  */
	public String getoptmode();

    /** Column name prestacion */
    public static final String COLUMNNAME_prestacion = "prestacion";

	/** Set prestacion	  */
	public void setprestacion (boolean prestacion);

	/** Get prestacion	  */
	public boolean isprestacion();

    /** Column name rule */
    public static final String COLUMNNAME_rule = "rule";

	/** Set rule	  */
	public void setrule (String rule);

	/** Get rule	  */
	public String getrule();

    /** Column name salario */
    public static final String COLUMNNAME_salario = "salario";

	/** Set salario	  */
	public void setsalario (boolean salario);

	/** Get salario	  */
	public boolean issalario();

    /** Column name sign */
    public static final String COLUMNNAME_sign = "sign";

	/** Set sign	  */
	public void setsign (String sign);

	/** Get sign	  */
	public String getsign();

    /** Column name spf */
    public static final String COLUMNNAME_spf = "spf";

	/** Set spf	  */
	public void setspf (boolean spf);

	/** Get spf	  */
	public boolean isspf();

    /** Column name sso */
    public static final String COLUMNNAME_sso = "sso";

	/** Set sso	  */
	public void setsso (boolean sso);

	/** Get sso	  */
	public boolean issso();

    /** Column name utilidad */
    public static final String COLUMNNAME_utilidad = "utilidad";

	/** Set utilidad	  */
	public void setutilidad (boolean utilidad);

	/** Get utilidad	  */
	public boolean isutilidad();

    /** Column name vacacion */
    public static final String COLUMNNAME_vacacion = "vacacion";

	/** Set vacacion	  */
	public void setvacacion (boolean vacacion);

	/** Get vacacion	  */
	public boolean isvacacion();

    /** Column name variable */
    public static final String COLUMNNAME_variable = "variable";

	/** Set variable	  */
	public void setvariable (String variable);

	/** Get variable	  */
	public String getvariable();
}

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

/** Generated Interface for AMN_Employee
 *  @author iDempiere (generated) 
 *  @version Release 11
 */
@SuppressWarnings("all")
public interface I_AMN_Employee 
{

    /** TableName=AMN_Employee */
    public static final String Table_Name = "AMN_Employee";

    /** AD_Table_ID=1000022 */
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

    /** Column name AD_OrgTo_ID */
    public static final String COLUMNNAME_AD_OrgTo_ID = "AD_OrgTo_ID";

	/** Set Inter-Organization.
	  * Organization valid for intercompany documents
	  */
	public void setAD_OrgTo_ID (int AD_OrgTo_ID);

	/** Get Inter-Organization.
	  * Organization valid for intercompany documents
	  */
	public int getAD_OrgTo_ID();

    /** Column name Alergic */
    public static final String COLUMNNAME_Alergic = "Alergic";

	/** Set Alergic.
	  * Alergic to chemicals ór Medicament
	  */
	public void setAlergic (String Alergic);

	/** Get Alergic.
	  * Alergic to chemicals ór Medicament
	  */
	public String getAlergic();

    /** Column name AMN_CommissionGroup_ID */
    public static final String COLUMNNAME_AMN_CommissionGroup_ID = "AMN_CommissionGroup_ID";

	/** Set Commission Group.
	  * Payroll Employee commission Group OrgSector
	  */
	public void setAMN_CommissionGroup_ID (int AMN_CommissionGroup_ID);

	/** Get Commission Group.
	  * Payroll Employee commission Group OrgSector
	  */
	public int getAMN_CommissionGroup_ID();

	public I_AMN_CommissionGroup getAMN_CommissionGroup() throws RuntimeException;

    /** Column name AMN_Contract_ID */
    public static final String COLUMNNAME_AMN_Contract_ID = "AMN_Contract_ID";

	/** Set Payroll Contract	  */
	public void setAMN_Contract_ID (int AMN_Contract_ID);

	/** Get Payroll Contract	  */
	public int getAMN_Contract_ID();

    /** Column name AMN_Department_ID */
    public static final String COLUMNNAME_AMN_Department_ID = "AMN_Department_ID";

	/** Set Payroll Department	  */
	public void setAMN_Department_ID (int AMN_Department_ID);

	/** Get Payroll Department	  */
	public int getAMN_Department_ID();

    /** Column name AMN_Employee_ID */
    public static final String COLUMNNAME_AMN_Employee_ID = "AMN_Employee_ID";

	/** Set Payroll employee	  */
	public void setAMN_Employee_ID (int AMN_Employee_ID);

	/** Get Payroll employee	  */
	public int getAMN_Employee_ID();

    /** Column name AMN_Employee_UU */
    public static final String COLUMNNAME_AMN_Employee_UU = "AMN_Employee_UU";

	/** Set AMN_Employee_UU	  */
	public void setAMN_Employee_UU (String AMN_Employee_UU);

	/** Get AMN_Employee_UU	  */
	public String getAMN_Employee_UU();

    /** Column name AMN_Jobstation_ID */
    public static final String COLUMNNAME_AMN_Jobstation_ID = "AMN_Jobstation_ID";

	/** Set Payroll Job Station	  */
	public void setAMN_Jobstation_ID (int AMN_Jobstation_ID);

	/** Get Payroll Job Station	  */
	public int getAMN_Jobstation_ID();

	public I_AMN_Jobstation getAMN_Jobstation() throws RuntimeException;

    /** Column name AMN_Jobtitle_ID */
    public static final String COLUMNNAME_AMN_Jobtitle_ID = "AMN_Jobtitle_ID";

	/** Set Payroll Job Title	  */
	public void setAMN_Jobtitle_ID (int AMN_Jobtitle_ID);

	/** Get Payroll Job Title	  */
	public int getAMN_Jobtitle_ID();

	public I_AMN_Jobtitle getAMN_Jobtitle() throws RuntimeException;

    /** Column name AMN_Location_ID */
    public static final String COLUMNNAME_AMN_Location_ID = "AMN_Location_ID";

	/** Set Payroll Location	  */
	public void setAMN_Location_ID (int AMN_Location_ID);

	/** Get Payroll Location	  */
	public int getAMN_Location_ID();

    /** Column name AMN_Position_ID */
    public static final String COLUMNNAME_AMN_Position_ID = "AMN_Position_ID";

	/** Set Payroll Position	  */
	public void setAMN_Position_ID (int AMN_Position_ID);

	/** Get Payroll Position	  */
	public int getAMN_Position_ID();

    /** Column name AMN_Sector_ID */
    public static final String COLUMNNAME_AMN_Sector_ID = "AMN_Sector_ID";

	/** Set Work Sector .
	  * Work Sector in Location
	  */
	public void setAMN_Sector_ID (int AMN_Sector_ID);

	/** Get Work Sector .
	  * Work Sector in Location
	  */
	public int getAMN_Sector_ID();

	public I_AMN_Sector getAMN_Sector() throws RuntimeException;

    /** Column name AMN_Shift_ID */
    public static final String COLUMNNAME_AMN_Shift_ID = "AMN_Shift_ID";

	/** Set Shift	  */
	public void setAMN_Shift_ID (int AMN_Shift_ID);

	/** Get Shift	  */
	public int getAMN_Shift_ID();

    /** Column name Bill_BPartner_ID */
    public static final String COLUMNNAME_Bill_BPartner_ID = "Bill_BPartner_ID";

	/** Set Invoice Partner.
	  * Business Partner to be invoiced
	  */
	public void setBill_BPartner_ID (int Bill_BPartner_ID);

	/** Get Invoice Partner.
	  * Business Partner to be invoiced
	  */
	public int getBill_BPartner_ID();

	public org.compiere.model.I_C_BPartner getBill_BPartner() throws RuntimeException;

    /** Column name BioCode */
    public static final String COLUMNNAME_BioCode = "BioCode";

	/** Set BioCode.
	  * Biometric Code from Attendance Control Scanners
	  */
	public void setBioCode (String BioCode);

	/** Get BioCode.
	  * Biometric Code from Attendance Control Scanners
	  */
	public String getBioCode();

    /** Column name Birthday */
    public static final String COLUMNNAME_Birthday = "Birthday";

	/** Set Birthday.
	  * Birthday or Anniversary day
	  */
	public void setBirthday (Timestamp Birthday);

	/** Get Birthday.
	  * Birthday or Anniversary day
	  */
	public Timestamp getBirthday();

    /** Column name birthplace */
    public static final String COLUMNNAME_birthplace = "birthplace";

	/** Set birthplace	  */
	public void setbirthplace (String birthplace);

	/** Get birthplace	  */
	public String getbirthplace();

    /** Column name bloodrh */
    public static final String COLUMNNAME_bloodrh = "bloodrh";

	/** Set bloodrh	  */
	public void setbloodrh (String bloodrh);

	/** Get bloodrh	  */
	public String getbloodrh();

    /** Column name bloodtype */
    public static final String COLUMNNAME_bloodtype = "bloodtype";

	/** Set bloodtype	  */
	public void setbloodtype (String bloodtype);

	/** Get bloodtype	  */
	public String getbloodtype();

    /** Column name C_Activity_ID */
    public static final String COLUMNNAME_C_Activity_ID = "C_Activity_ID";

	/** Set Activity.
	  * Business Activity
	  */
	public void setC_Activity_ID (int C_Activity_ID);

	/** Get Activity.
	  * Business Activity
	  */
	public int getC_Activity_ID();

	public org.compiere.model.I_C_Activity getC_Activity() throws RuntimeException;

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner.
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner.
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name C_Bpartner_TO_ID */
    public static final String COLUMNNAME_C_Bpartner_TO_ID = "C_Bpartner_TO_ID";

	/** Set C_Bpartner_TO_ID	  */
	public void setC_Bpartner_TO_ID (int C_Bpartner_TO_ID);

	/** Get C_Bpartner_TO_ID	  */
	public int getC_Bpartner_TO_ID();

    /** Column name C_Campaign_ID */
    public static final String COLUMNNAME_C_Campaign_ID = "C_Campaign_ID";

	/** Set Campaign.
	  * Marketing Campaign
	  */
	public void setC_Campaign_ID (int C_Campaign_ID);

	/** Get Campaign.
	  * Marketing Campaign
	  */
	public int getC_Campaign_ID();

	public org.compiere.model.I_C_Campaign getC_Campaign() throws RuntimeException;

    /** Column name C_ConsolidationReference_ID */
    public static final String COLUMNNAME_C_ConsolidationReference_ID = "C_ConsolidationReference_ID";

	/** Set C_CONSOLIDATIONREFERENCE_ID	  */
	public void setC_ConsolidationReference_ID (int C_ConsolidationReference_ID);

	/** Get C_CONSOLIDATIONREFERENCE_ID	  */
	public int getC_ConsolidationReference_ID();

    /** Column name C_Country_ID */
    public static final String COLUMNNAME_C_Country_ID = "C_Country_ID";

	/** Set Country.
	  * Country 
	  */
	public void setC_Country_ID (int C_Country_ID);

	/** Get Country.
	  * Country 
	  */
	public int getC_Country_ID();

	public org.compiere.model.I_C_Country getC_Country() throws RuntimeException;

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

    /** Column name C_GMR_KEZO_BAR */
    public static final String COLUMNNAME_C_GMR_KEZO_BAR = "C_GMR_KEZO_BAR";

	/** Set C_GMR_KEZO_BAR	  */
	public void setC_GMR_KEZO_BAR (String C_GMR_KEZO_BAR);

	/** Get C_GMR_KEZO_BAR	  */
	public String getC_GMR_KEZO_BAR();

    /** Column name civilstatus */
    public static final String COLUMNNAME_civilstatus = "civilstatus";

	/** Set civilstatus	  */
	public void setcivilstatus (String civilstatus);

	/** Get civilstatus	  */
	public String getcivilstatus();

    /** Column name C_Jobshift_ID */
    public static final String COLUMNNAME_C_Jobshift_ID = "C_Jobshift_ID";

	/** Set C_JOBSHIFT_ID	  */
	public void setC_Jobshift_ID (int C_Jobshift_ID);

	/** Get C_JOBSHIFT_ID	  */
	public int getC_Jobshift_ID();

    /** Column name COMMISIONGOAL */
    public static final String COLUMNNAME_COMMISIONGOAL = "COMMISIONGOAL";

	/** Set COMMISIONGOAL	  */
	public void setCOMMISIONGOAL (BigDecimal COMMISIONGOAL);

	/** Get COMMISIONGOAL	  */
	public BigDecimal getCOMMISIONGOAL();

    /** Column name CommissionsGroup */
    public static final String COLUMNNAME_CommissionsGroup = "CommissionsGroup";

	/** Set COMMISSIONSGROUP	  */
	public void setCommissionsGroup (String CommissionsGroup);

	/** Get COMMISSIONSGROUP	  */
	public String getCommissionsGroup();

    /** Column name CountryofNacionality_ID */
    public static final String COLUMNNAME_CountryofNacionality_ID = "CountryofNacionality_ID";

	/** Set Nacionality	  */
	public void setCountryofNacionality_ID (int CountryofNacionality_ID);

	/** Get Nacionality	  */
	public int getCountryofNacionality_ID();

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException;

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

    /** Column name C_SalesRegion_ID */
    public static final String COLUMNNAME_C_SalesRegion_ID = "C_SalesRegion_ID";

	/** Set Sales Region.
	  * Sales coverage region
	  */
	public void setC_SalesRegion_ID (int C_SalesRegion_ID);

	/** Get Sales Region.
	  * Sales coverage region
	  */
	public int getC_SalesRegion_ID();

	public org.compiere.model.I_C_SalesRegion getC_SalesRegion() throws RuntimeException;

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

    /** Column name Deseases */
    public static final String COLUMNNAME_Deseases = "Deseases";

	/** Set Deseases.
	  * Deseases affected for person
	  */
	public void setDeseases (String Deseases);

	/** Get Deseases.
	  * Deseases affected for person
	  */
	public String getDeseases();

    /** Column name downwardloads */
    public static final String COLUMNNAME_downwardloads = "downwardloads";

	/** Set downwardloads	  */
	public void setdownwardloads (BigDecimal downwardloads);

	/** Get downwardloads	  */
	public BigDecimal getdownwardloads();

    /** Column name educationgrade */
    public static final String COLUMNNAME_educationgrade = "educationgrade";

	/** Set educationgrade	  */
	public void seteducationgrade (String educationgrade);

	/** Get educationgrade	  */
	public String geteducationgrade();

    /** Column name educationlevel */
    public static final String COLUMNNAME_educationlevel = "educationlevel";

	/** Set educationlevel	  */
	public void seteducationlevel (String educationlevel);

	/** Get educationlevel	  */
	public String geteducationlevel();

    /** Column name egresscondition */
    public static final String COLUMNNAME_egresscondition = "egresscondition";

	/** Set egresscondition	  */
	public void setegresscondition (String egresscondition);

	/** Get egresscondition	  */
	public String getegresscondition();

    /** Column name egressdate */
    public static final String COLUMNNAME_egressdate = "egressdate";

	/** Set egressdate	  */
	public void setegressdate (Timestamp egressdate);

	/** Get egressdate	  */
	public Timestamp getegressdate();

    /** Column name EMail */
    public static final String COLUMNNAME_EMail = "EMail";

	/** Set EMail Address.
	  * Electronic Mail Address
	  */
	public void setEMail (String EMail);

	/** Get EMail Address.
	  * Electronic Mail Address
	  */
	public String getEMail();

    /** Column name empimg1_ID */
    public static final String COLUMNNAME_empimg1_ID = "empimg1_ID";

	/** Set Employee Image 1.
	  * Employee Image 1
	  */
	public void setempimg1_ID (int empimg1_ID);

	/** Get Employee Image 1.
	  * Employee Image 1
	  */
	public int getempimg1_ID();

    /** Column name empimg2_ID */
    public static final String COLUMNNAME_empimg2_ID = "empimg2_ID";

	/** Set Employee Image 2.
	  * Employee Image 2
	  */
	public void setempimg2_ID (int empimg2_ID);

	/** Get Employee Image 2.
	  * Employee Image 2
	  */
	public int getempimg2_ID();

    /** Column name FIRSTHIREDATE */
    public static final String COLUMNNAME_FIRSTHIREDATE = "FIRSTHIREDATE";

	/** Set FIRSTHIREDATE	  */
	public void setFIRSTHIREDATE (String FIRSTHIREDATE);

	/** Get FIRSTHIREDATE	  */
	public String getFIRSTHIREDATE();

    /** Column name FirstName1 */
    public static final String COLUMNNAME_FirstName1 = "FirstName1";

	/** Set First Name 1	  */
	public void setFirstName1 (String FirstName1);

	/** Get First Name 1	  */
	public String getFirstName1();

    /** Column name FirstName2 */
    public static final String COLUMNNAME_FirstName2 = "FirstName2";

	/** Set First Name 2	  */
	public void setFirstName2 (String FirstName2);

	/** Get First Name 2	  */
	public String getFirstName2();

    /** Column name HandUse */
    public static final String COLUMNNAME_HandUse = "HandUse";

	/** Set HandUse.
	  * Indicates if person uses Left hand, right hand or both 
	  */
	public void setHandUse (String HandUse);

	/** Get HandUse.
	  * Indicates if person uses Left hand, right hand or both 
	  */
	public String getHandUse();

    /** Column name Height */
    public static final String COLUMNNAME_Height = "Height";

	/** Set Height	  */
	public void setHeight (BigDecimal Height);

	/** Get Height	  */
	public BigDecimal getHeight();

    /** Column name HIREDATE */
    public static final String COLUMNNAME_HIREDATE = "HIREDATE";

	/** Set HIREDATE	  */
	public void setHIREDATE (String HIREDATE);

	/** Get HIREDATE	  */
	public String getHIREDATE();

    /** Column name Hobbyes */
    public static final String COLUMNNAME_Hobbyes = "Hobbyes";

	/** Set Hobbyes.
	  * Hobbyes practicated 
	  */
	public void setHobbyes (String Hobbyes);

	/** Get Hobbyes.
	  * Hobbyes practicated 
	  */
	public String getHobbyes();

    /** Column name IDCardAux */
    public static final String COLUMNNAME_IDCardAux = "IDCardAux";

	/** Set IDCardAux	  */
	public void setIDCardAux (String IDCardAux);

	/** Get IDCardAux	  */
	public String getIDCardAux();

    /** Column name IDCardNo */
    public static final String COLUMNNAME_IDCardNo = "IDCardNo";

	/** Set IDCardNo	  */
	public void setIDCardNo (String IDCardNo);

	/** Get IDCardNo	  */
	public String getIDCardNo();

    /** Column name IDCOUNTRY */
    public static final String COLUMNNAME_IDCOUNTRY = "IDCOUNTRY";

	/** Set IDCOUNTRY	  */
	public void setIDCOUNTRY (int IDCOUNTRY);

	/** Get IDCOUNTRY	  */
	public int getIDCOUNTRY();

    /** Column name IDNumber */
    public static final String COLUMNNAME_IDNumber = "IDNumber";

	/** Set IDNumber	  */
	public void setIDNumber (String IDNumber);

	/** Get IDNumber	  */
	public String getIDNumber();

    /** Column name IDType */
    public static final String COLUMNNAME_IDType = "IDType";

	/** Set IDType	  */
	public void setIDType (String IDType);

	/** Get IDType	  */
	public String getIDType();

    /** Column name incomedate */
    public static final String COLUMNNAME_incomedate = "incomedate";

	/** Set incomedate	  */
	public void setincomedate (Timestamp incomedate);

	/** Get incomedate	  */
	public Timestamp getincomedate();

    /** Column name increasingloads */
    public static final String COLUMNNAME_increasingloads = "increasingloads";

	/** Set increasingloads	  */
	public void setincreasingloads (BigDecimal increasingloads);

	/** Get increasingloads	  */
	public BigDecimal getincreasingloads();

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

    /** Column name ISALLOWBLANKETPO */
    public static final String COLUMNNAME_ISALLOWBLANKETPO = "ISALLOWBLANKETPO";

	/** Set ISALLOWBLANKETPO	  */
	public void setISALLOWBLANKETPO (boolean ISALLOWBLANKETPO);

	/** Get ISALLOWBLANKETPO	  */
	public boolean isALLOWBLANKETPO();

    /** Column name ISALLOWBLANKETSO */
    public static final String COLUMNNAME_ISALLOWBLANKETSO = "ISALLOWBLANKETSO";

	/** Set ISALLOWBLANKETSO	  */
	public void setISALLOWBLANKETSO (boolean ISALLOWBLANKETSO);

	/** Get ISALLOWBLANKETSO	  */
	public boolean isALLOWBLANKETSO();

    /** Column name ISALLOWCUSTOMERCONSIGNED */
    public static final String COLUMNNAME_ISALLOWCUSTOMERCONSIGNED = "ISALLOWCUSTOMERCONSIGNED";

	/** Set ISALLOWCUSTOMERCONSIGNED	  */
	public void setISALLOWCUSTOMERCONSIGNED (boolean ISALLOWCUSTOMERCONSIGNED);

	/** Get ISALLOWCUSTOMERCONSIGNED	  */
	public boolean isALLOWCUSTOMERCONSIGNED();

    /** Column name ISALLOWVENDORCONSIGNED */
    public static final String COLUMNNAME_ISALLOWVENDORCONSIGNED = "ISALLOWVENDORCONSIGNED";

	/** Set ISALLOWVENDORCONSIGNED	  */
	public void setISALLOWVENDORCONSIGNED (boolean ISALLOWVENDORCONSIGNED);

	/** Get ISALLOWVENDORCONSIGNED	  */
	public boolean isALLOWVENDORCONSIGNED();

    /** Column name ISBPASSIGNED */
    public static final String COLUMNNAME_ISBPASSIGNED = "ISBPASSIGNED";

	/** Set ISBPASSIGNED	  */
	public void setISBPASSIGNED (boolean ISBPASSIGNED);

	/** Get ISBPASSIGNED	  */
	public boolean isBPASSIGNED();

    /** Column name IsDetailedNames */
    public static final String COLUMNNAME_IsDetailedNames = "IsDetailedNames";

	/** Set Detailed Names	  */
	public void setIsDetailedNames (boolean IsDetailedNames);

	/** Get Detailed Names	  */
	public boolean isDetailedNames();

    /** Column name ISEDI */
    public static final String COLUMNNAME_ISEDI = "ISEDI";

	/** Set ISEDI	  */
	public void setISEDI (boolean ISEDI);

	/** Get ISEDI	  */
	public boolean isEDI();

    /** Column name ISFIRSTHIREDATE */
    public static final String COLUMNNAME_ISFIRSTHIREDATE = "ISFIRSTHIREDATE";

	/** Set ISFIRSTHIREDATE	  */
	public void setISFIRSTHIREDATE (boolean ISFIRSTHIREDATE);

	/** Get ISFIRSTHIREDATE	  */
	public boolean isFIRSTHIREDATE();

    /** Column name ISGMRKENZO */
    public static final String COLUMNNAME_ISGMRKENZO = "ISGMRKENZO";

	/** Set ISGMRKENZO	  */
	public void setISGMRKENZO (boolean ISGMRKENZO);

	/** Get ISGMRKENZO	  */
	public boolean isGMRKENZO();

    /** Column name isMedicated */
    public static final String COLUMNNAME_isMedicated = "isMedicated";

	/** Set Is Medicated.
	  * Indicates if person is Medicated
	  */
	public void setisMedicated (boolean isMedicated);

	/** Get Is Medicated.
	  * Indicates if person is Medicated
	  */
	public boolean isMedicated();

    /** Column name ISMINIMUMWAGE */
    public static final String COLUMNNAME_ISMINIMUMWAGE = "ISMINIMUMWAGE";

	/** Set ISMINIMUMWAGE	  */
	public void setISMINIMUMWAGE (boolean ISMINIMUMWAGE);

	/** Get ISMINIMUMWAGE	  */
	public boolean isMINIMUMWAGE();

    /** Column name isPensioned */
    public static final String COLUMNNAME_isPensioned = "isPensioned";

	/** Set Is Pensioned.
	  * Indicates if employee is Pensioned by Social Security
	  */
	public void setisPensioned (boolean isPensioned);

	/** Get Is Pensioned.
	  * Indicates if employee is Pensioned by Social Security
	  */
	public boolean isPensioned();

    /** Column name ISPERMITREQUIRED */
    public static final String COLUMNNAME_ISPERMITREQUIRED = "ISPERMITREQUIRED";

	/** Set ISPERMITREQUIRED	  */
	public void setISPERMITREQUIRED (boolean ISPERMITREQUIRED);

	/** Get ISPERMITREQUIRED	  */
	public boolean isPERMITREQUIRED();

    /** Column name ISPROMOTER */
    public static final String COLUMNNAME_ISPROMOTER = "ISPROMOTER";

	/** Set ISPROMOTER	  */
	public void setISPROMOTER (boolean ISPROMOTER);

	/** Get ISPROMOTER	  */
	public boolean isPROMOTER();

    /** Column name ISSNDHIREDATE */
    public static final String COLUMNNAME_ISSNDHIREDATE = "ISSNDHIREDATE";

	/** Set ISSNDHIREDATE	  */
	public void setISSNDHIREDATE (boolean ISSNDHIREDATE);

	/** Get ISSNDHIREDATE	  */
	public boolean isSNDHIREDATE();

    /** Column name isSocialSecurity */
    public static final String COLUMNNAME_isSocialSecurity = "isSocialSecurity";

	/** Set isSocialSecurity	  */
	public void setisSocialSecurity (boolean isSocialSecurity);

	/** Get isSocialSecurity	  */
	public boolean isSocialSecurity();

    /** Column name ISSSPRIVATE */
    public static final String COLUMNNAME_ISSSPRIVATE = "ISSSPRIVATE";

	/** Set ISSSPRIVATE	  */
	public void setISSSPRIVATE (boolean ISSSPRIVATE);

	/** Get ISSSPRIVATE	  */
	public boolean isSSPRIVATE();

    /** Column name ISSSPROPORTIONAL */
    public static final String COLUMNNAME_ISSSPROPORTIONAL = "ISSSPROPORTIONAL";

	/** Set ISSSPROPORTIONAL	  */
	public void setISSSPROPORTIONAL (boolean ISSSPROPORTIONAL);

	/** Get ISSSPROPORTIONAL	  */
	public boolean isSSPROPORTIONAL();

    /** Column name isStudying */
    public static final String COLUMNNAME_isStudying = "isStudying";

	/** Set isStudying.
	  * Indicates if is studying
	  */
	public void setisStudying (boolean isStudying);

	/** Get isStudying.
	  * Indicates if is studying
	  */
	public boolean isStudying();

    /** Column name jobcondition */
    public static final String COLUMNNAME_jobcondition = "jobcondition";

	/** Set jobcondition	  */
	public void setjobcondition (String jobcondition);

	/** Get jobcondition	  */
	public String getjobcondition();

    /** Column name JobRemuneration */
    public static final String COLUMNNAME_JobRemuneration = "JobRemuneration";

	/** Set JobRemuneration	  */
	public void setJobRemuneration (BigDecimal JobRemuneration);

	/** Get JobRemuneration	  */
	public BigDecimal getJobRemuneration();

    /** Column name JRCURRENCY_ID */
    public static final String COLUMNNAME_JRCURRENCY_ID = "JRCURRENCY_ID";

	/** Set JRCURRENCY_ID	  */
	public void setJRCURRENCY_ID (int JRCURRENCY_ID);

	/** Get JRCURRENCY_ID	  */
	public int getJRCURRENCY_ID();

    /** Column name LastName1 */
    public static final String COLUMNNAME_LastName1 = "LastName1";

	/** Set Last Name 1	  */
	public void setLastName1 (String LastName1);

	/** Get Last Name 1	  */
	public String getLastName1();

    /** Column name LastName2 */
    public static final String COLUMNNAME_LastName2 = "LastName2";

	/** Set Last Name 2	  */
	public void setLastName2 (String LastName2);

	/** Get Last Name 2	  */
	public String getLastName2();

    /** Column name MOLI_BPARTNERTYPE */
    public static final String COLUMNNAME_MOLI_BPARTNERTYPE = "MOLI_BPARTNERTYPE";

	/** Set MOLI_BPARTNERTYPE	  */
	public void setMOLI_BPARTNERTYPE (String MOLI_BPARTNERTYPE);

	/** Get MOLI_BPARTNERTYPE	  */
	public String getMOLI_BPARTNERTYPE();

    /** Column name MOLI_EMPLOYERID */
    public static final String COLUMNNAME_MOLI_EMPLOYERID = "MOLI_EMPLOYERID";

	/** Set MOLI_EMPLOYERID	  */
	public void setMOLI_EMPLOYERID (int MOLI_EMPLOYERID);

	/** Get MOLI_EMPLOYERID	  */
	public int getMOLI_EMPLOYERID();

    /** Column name MOLI_IRPPERCENT */
    public static final String COLUMNNAME_MOLI_IRPPERCENT = "MOLI_IRPPERCENT";

	/** Set MOLI_IRPPERCENT	  */
	public void setMOLI_IRPPERCENT (BigDecimal MOLI_IRPPERCENT);

	/** Get MOLI_IRPPERCENT	  */
	public BigDecimal getMOLI_IRPPERCENT();

    /** Column name MOLI_ISIRP */
    public static final String COLUMNNAME_MOLI_ISIRP = "MOLI_ISIRP";

	/** Set MOLI_ISIRP	  */
	public void setMOLI_ISIRP (boolean MOLI_ISIRP);

	/** Get MOLI_ISIRP	  */
	public boolean isMOLI_ISIRP();

    /** Column name MOLI_PHOTOID */
    public static final String COLUMNNAME_MOLI_PHOTOID = "MOLI_PHOTOID";

	/** Set MOLI_PHOTOID	  */
	public void setMOLI_PHOTOID (int MOLI_PHOTOID);

	/** Get MOLI_PHOTOID	  */
	public int getMOLI_PHOTOID();

    /** Column name MOLI_SSID */
    public static final String COLUMNNAME_MOLI_SSID = "MOLI_SSID";

	/** Set MOLI_SSID	  */
	public void setMOLI_SSID (String MOLI_SSID);

	/** Get MOLI_SSID	  */
	public String getMOLI_SSID();

    /** Column name MOLI_TYPEDOCUMENT */
    public static final String COLUMNNAME_MOLI_TYPEDOCUMENT = "MOLI_TYPEDOCUMENT";

	/** Set MOLI_TYPEDOCUMENT	  */
	public void setMOLI_TYPEDOCUMENT (String MOLI_TYPEDOCUMENT);

	/** Get MOLI_TYPEDOCUMENT	  */
	public String getMOLI_TYPEDOCUMENT();

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

    /** Column name NAME_IDCARD */
    public static final String COLUMNNAME_NAME_IDCARD = "NAME_IDCARD";

	/** Set NAME_IDCARD	  */
	public void setNAME_IDCARD (String NAME_IDCARD);

	/** Get NAME_IDCARD	  */
	public String getNAME_IDCARD();

    /** Column name OrgSector */
    public static final String COLUMNNAME_OrgSector = "OrgSector";

	/** Set OrgSector	  */
	public void setOrgSector (String OrgSector);

	/** Get OrgSector	  */
	public String getOrgSector();

    /** Column name paymenttype */
    public static final String COLUMNNAME_paymenttype = "paymenttype";

	/** Set paymenttype	  */
	public void setpaymenttype (String paymenttype);

	/** Get paymenttype	  */
	public String getpaymenttype();

    /** Column name payrollmode */
    public static final String COLUMNNAME_payrollmode = "payrollmode";

	/** Set payrollmode	  */
	public void setpayrollmode (String payrollmode);

	/** Get payrollmode	  */
	public String getpayrollmode();

    /** Column name PIN */
    public static final String COLUMNNAME_PIN = "PIN";

	/** Set PIN.
	  * Personal Identification Number
	  */
	public void setPIN (String PIN);

	/** Get PIN.
	  * Personal Identification Number
	  */
	public String getPIN();

    /** Column name privateassist */
    public static final String COLUMNNAME_privateassist = "privateassist";

	/** Set privateassist	  */
	public void setprivateassist (String privateassist);

	/** Get privateassist	  */
	public String getprivateassist();

    /** Column name profession */
    public static final String COLUMNNAME_profession = "profession";

	/** Set profession	  */
	public void setprofession (String profession);

	/** Get profession	  */
	public String getprofession();

    /** Column name Salary */
    public static final String COLUMNNAME_Salary = "Salary";

	/** Set Salary	  */
	public void setSalary (BigDecimal Salary);

	/** Get Salary	  */
	public BigDecimal getSalary();

    /** Column name sex */
    public static final String COLUMNNAME_sex = "sex";

	/** Set sex	  */
	public void setsex (String sex);

	/** Get sex	  */
	public String getsex();

    /** Column name SizePant */
    public static final String COLUMNNAME_SizePant = "SizePant";

	/** Set Size Pant.
	  * Pant size for Person
	  */
	public void setSizePant (String SizePant);

	/** Get Size Pant.
	  * Pant size for Person
	  */
	public String getSizePant();

    /** Column name SizeShirt */
    public static final String COLUMNNAME_SizeShirt = "SizeShirt";

	/** Set Size Shirt.
	  * Size Shirt for Person
	  */
	public void setSizeShirt (String SizeShirt);

	/** Get Size Shirt.
	  * Size Shirt for Person
	  */
	public String getSizeShirt();

    /** Column name SizeShoe */
    public static final String COLUMNNAME_SizeShoe = "SizeShoe";

	/** Set SizeShoe.
	  * Shoe size for Person
	  */
	public void setSizeShoe (String SizeShoe);

	/** Get SizeShoe.
	  * Shoe size for Person
	  */
	public String getSizeShoe();

    /** Column name SNDHIREDATE */
    public static final String COLUMNNAME_SNDHIREDATE = "SNDHIREDATE";

	/** Set SNDHIREDATE	  */
	public void setSNDHIREDATE (String SNDHIREDATE);

	/** Get SNDHIREDATE	  */
	public String getSNDHIREDATE();

    /** Column name SocialSecurityNO */
    public static final String COLUMNNAME_SocialSecurityNO = "SocialSecurityNO";

	/** Set SocialSecurityNO	  */
	public void setSocialSecurityNO (String SocialSecurityNO);

	/** Get SocialSecurityNO	  */
	public String getSocialSecurityNO();

    /** Column name Sports */
    public static final String COLUMNNAME_Sports = "Sports";

	/** Set Sports.
	  * Sports practicated
	  */
	public void setSports (String Sports);

	/** Get Sports.
	  * Sports practicated
	  */
	public String getSports();

    /** Column name spouse */
    public static final String COLUMNNAME_spouse = "spouse";

	/** Set spouse	  */
	public void setspouse (String spouse);

	/** Get spouse	  */
	public String getspouse();

    /** Column name SSAMOUNT */
    public static final String COLUMNNAME_SSAMOUNT = "SSAMOUNT";

	/** Set SSAMOUNT	  */
	public void setSSAMOUNT (BigDecimal SSAMOUNT);

	/** Get SSAMOUNT	  */
	public BigDecimal getSSAMOUNT();

    /** Column name SSCURRENCY_ID */
    public static final String COLUMNNAME_SSCURRENCY_ID = "SSCURRENCY_ID";

	/** Set SSCURRENCY_ID	  */
	public void setSSCURRENCY_ID (int SSCURRENCY_ID);

	/** Get SSCURRENCY_ID	  */
	public int getSSCURRENCY_ID();

    /** Column name SSENTITY */
    public static final String COLUMNNAME_SSENTITY = "SSENTITY";

	/** Set SSENTITY	  */
	public void setSSENTITY (String SSENTITY);

	/** Get SSENTITY	  */
	public String getSSENTITY();

    /** Column name SSPRIVATEAMOUNT */
    public static final String COLUMNNAME_SSPRIVATEAMOUNT = "SSPRIVATEAMOUNT";

	/** Set SSPRIVATEAMOUNT	  */
	public void setSSPRIVATEAMOUNT (BigDecimal SSPRIVATEAMOUNT);

	/** Get SSPRIVATEAMOUNT	  */
	public BigDecimal getSSPRIVATEAMOUNT();

    /** Column name SSPRIVATECURRENCY_ID */
    public static final String COLUMNNAME_SSPRIVATECURRENCY_ID = "SSPRIVATECURRENCY_ID";

	/** Set SSPRIVATECURRENCY_ID	  */
	public void setSSPRIVATECURRENCY_ID (int SSPRIVATECURRENCY_ID);

	/** Get SSPRIVATECURRENCY_ID	  */
	public int getSSPRIVATECURRENCY_ID();

    /** Column name SSPRIVATEENTITY */
    public static final String COLUMNNAME_SSPRIVATEENTITY = "SSPRIVATEENTITY";

	/** Set SSPRIVATEENTITY	  */
	public void setSSPRIVATEENTITY (String SSPRIVATEENTITY);

	/** Get SSPRIVATEENTITY	  */
	public String getSSPRIVATEENTITY();

    /** Column name SSPRIVATENO */
    public static final String COLUMNNAME_SSPRIVATENO = "SSPRIVATENO";

	/** Set SSPRIVATENO	  */
	public void setSSPRIVATENO (String SSPRIVATENO);

	/** Get SSPRIVATENO	  */
	public String getSSPRIVATENO();

    /** Column name SSPROPORTIONALAMT */
    public static final String COLUMNNAME_SSPROPORTIONALAMT = "SSPROPORTIONALAMT";

	/** Set SSPROPORTIONALAMT	  */
	public void setSSPROPORTIONALAMT (BigDecimal SSPROPORTIONALAMT);

	/** Get SSPROPORTIONALAMT	  */
	public BigDecimal getSSPROPORTIONALAMT();

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

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

    /** Column name URL */
    public static final String COLUMNNAME_URL = "URL";

	/** Set URL.
	  * Full URL address - e.g. http://www.idempiere.org
	  */
	public void setURL (String URL);

	/** Get URL.
	  * Full URL address - e.g. http://www.idempiere.org
	  */
	public String getURL();

    /** Column name UseLenses */
    public static final String COLUMNNAME_UseLenses = "UseLenses";

	/** Set Use Lenses.
	  * Indicates if person uses lenses
	  */
	public void setUseLenses (boolean UseLenses);

	/** Get Use Lenses.
	  * Indicates if person uses lenses
	  */
	public boolean isUseLenses();

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

    /** Column name Weight */
    public static final String COLUMNNAME_Weight = "Weight";

	/** Set Weight.
	  * Weight of a product
	  */
	public void setWeight (BigDecimal Weight);

	/** Get Weight.
	  * Weight of a product
	  */
	public BigDecimal getWeight();

    /** Column name zodiacsign */
    public static final String COLUMNNAME_zodiacsign = "zodiacsign";

	/** Set zodiacsign	  */
	public void setzodiacsign (String zodiacsign);

	/** Get zodiacsign	  */
	public String getzodiacsign();
}

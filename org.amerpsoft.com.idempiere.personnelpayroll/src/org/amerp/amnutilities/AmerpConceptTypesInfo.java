package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.I_AMN_Jobstation;
import org.amerp.amnmodel.I_AMN_Jobtitle;
import org.amerp.amnmodel.MAMN_Concept_Types;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Jobtitle;
import org.compiere.acct.Doc;
import org.compiere.model.I_C_Activity;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Campaign;
import org.compiere.model.I_C_Country;
import org.compiere.model.I_C_Project;
import org.compiere.model.I_C_SalesRegion;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAttachment;
import org.compiere.model.MClientInfo;
import org.compiere.model.MProduct;
import org.compiere.model.ProductCost;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.w3c.dom.Document;

/**
 * @author luisamesty
 *
 */

public class AmerpConceptTypesInfo {


	/** The Concept Types ID				*/
	private int				m_AMN_Concept_Types_ID = 0;
	/** The Concept			MAMN_Concept_Types class	*/
	private MAMN_Concept_Types		m_MAMN_Concept_Types = null;
	/** Valid Combination MAccount C_ValidCombination_ID */
	private int m_C_ValidCombination_ID = 0;
	/** Employee							*/
	private int				m_AMN_Employee_ID = 0;
	private MAMN_Employee 	m_AMN_Employee = null;
	/** Calc Order							*/
	private int CalcOrder;
	/** Transaction							*/
	private String 			m_trxName = null;
	/** Context							*/
	private Properties m_ctx = null;
	/**  ConceptVariable  */
	private String ConceptVariable;
	/**	Logger					*/
	private static CLogger 	log = CLogger.getCLogger (ProductCost.class);

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param AMN_Concept_Types_ID Concept Type
	 *	@param trxName trx
	 */
	public AmerpConceptTypesInfo(Properties ctx, int AMN_Concept_Types_ID, String trxName) {
		super();
		
		m_AMN_Concept_Types_ID = AMN_Concept_Types_ID;

		if (AMN_Concept_Types_ID != 0)
			m_MAMN_Concept_Types = new MAMN_Concept_Types(ctx, AMN_Concept_Types_ID, trxName);
		m_trxName = trxName;
		m_ctx = ctx;
	}
	
	
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param AMN_Concept_Types_ID Concept Type
	 *  @param MN_Employee_ID
	 *	@param trxName trx
	 */

	public AmerpConceptTypesInfo(Properties m_ctx, int m_AMN_Concept_Types_ID,
			int m_AMN_Employee_ID, String m_trxName ) {
		super();
		this.m_AMN_Concept_Types_ID = m_AMN_Concept_Types_ID;
		this.m_AMN_Employee_ID = m_AMN_Employee_ID;
		this.m_trxName = m_trxName;
		this.m_ctx = m_ctx;
		if (m_AMN_Concept_Types_ID != 0)
			m_MAMN_Concept_Types = new MAMN_Concept_Types(m_ctx, m_AMN_Concept_Types_ID, m_trxName);
		if (m_AMN_Employee_ID != 0)
			m_AMN_Employee = new MAMN_Employee(m_ctx, m_AMN_Employee_ID, m_trxName);
	}

	/** Administrative Work Force DEB Acct    */
	public static final int ACCTTYPE_Admin_WF_DEB    = 1;
	/** Administrative Work Force CRE Acct    */
	public static final int ACCTTYPE_Admin_WF_CRE    = 2;
	/** Direct Work Force DEB Acct     */
	public static final int ACCTTYPE_Direct_WF_DEB   = 3;
	/** Direct Work Force CRE Acct       */
	public static final int ACCTTYPE_Direct_WF_CRE   = 4;
	/** Indirect Work Force DEB Acct */
	public static final int ACCTTYPE_Indirect_WF_DEB = 5;
	/** Indirect Work Force CRE Acct  */
	public static final int ACCTTYPE_Indirect_WF_CRE = 6;
	/** Sales Work Force DEB Acct  */
	public static final int ACCTTYPE_Sales_WF_DEB    = 7;
	/** Sales Work Force CRE Acct    */
	public static final int ACCTTYPE_Sales_WF_CRE    = 8;

	/**
	 *  Line Account from Concept Type Table
	 *  @param  AcctType see ACCTTYPE_* (1..8)
	 *  @param as Accounting Schema
	 *  @return C_ValidCombination_ID
	 */
	public int getAccount(int AcctType)
	{
		MAMN_Concept_Types amnconcepttypes = new MAMN_Concept_Types(m_ctx, m_AMN_Concept_Types_ID, m_trxName);
		//  MAcctSchema Select Client Default 
		MClientInfo info = MClientInfo.get(Env.getCtx(), amnconcepttypes.getAD_Client_ID(), m_trxName); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		MAccount maccount = null;
		int C_ValidCombination_ID = 0;
		// 
		switch (AcctType) {
			 /** Administrative Work Force DEB Acct   1 */
			 case 1:  
				 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_Acct(), m_trxName);
	         break;
	    	 /** Administrative Work Force CRE Acct   2 */
	         case 2:
				 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_Acct(), m_trxName);
              break;
	    	 /** Direct Work Force DEB Acct     		 3 */
		     case 3:  
		    	 if (amnconcepttypes.getAMN_Deb_DW_Acct() != 0)
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_DW_Acct(), m_trxName);
		    	 else
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_Acct(), m_trxName);
		         break;
			 /** Direct Work Force CRE Acct       	 4 */
		     case 4:  
		    	 if (amnconcepttypes.getAMN_Cre_DW_Acct() != 0)
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_DW_Acct(), m_trxName);
		    	 else
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_Acct(), m_trxName);
	              break;
			 /** Indirect Work Force DEB Acct 		 5 */
		     case 5: 
		    	 if (amnconcepttypes.getAMN_Deb_IW_Acct() != 0)
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_IW_Acct(), m_trxName);
		    	 else
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_Acct(), m_trxName);
		         break;
			 /** Indirect Work Force CRE Acct  		 6 */
		     case 6: 
		    	 if (amnconcepttypes.getAMN_Cre_IW_Acct() != 0)
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_IW_Acct(), m_trxName);
		    	 else
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_Acct(), m_trxName);
	              break;
			 /** Sales Work Force DEB Acct  			 7 */
		     case 7: 
		    	 if (amnconcepttypes.getAMN_Deb_SW_Acct() != 0)
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_SW_Acct(), m_trxName);
		    	 else
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Deb_Acct(), m_trxName);
		         break;
			 /** Sales Work Force CRE Acct    		 8 */
		     case 8:  
		    	 if (amnconcepttypes.getAMN_Cre_SW_Acct() != 0)
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_SW_Acct(), m_trxName);
		    	 else
		    		 maccount= new MAccount(m_ctx, amnconcepttypes.getAMN_Cre_Acct(), m_trxName);
	              break;
		}
		C_ValidCombination_ID = maccount.getC_ValidCombination_ID();
		return C_ValidCombination_ID;
	}   //  getAccount

	/**
	 *  EmployeeAccountDEB Account from Concept Type Table
	 *  Depending on Employee WorkForce Info
	 *  @return C_ValidCombination_ID
	 */
	public int getEmployeeAccountDEB()
	{
		MAMN_Employee amnemployee = new MAMN_Employee(m_ctx, m_AMN_Employee_ID,  m_trxName);
		MAMN_Jobtitle amnjobtitle = new MAMN_Jobtitle(m_ctx, amnemployee.getAMN_Jobtitle_ID(),  m_trxName) ;
		int C_ValidCombination_ID = 0;
		String EmployeeWorkforce = amnjobtitle.getWorkforce();
		if (EmployeeWorkforce.compareToIgnoreCase("A") == 0)
			C_ValidCombination_ID= getAccount(1);
		else if (EmployeeWorkforce.compareToIgnoreCase("D") == 0)
			C_ValidCombination_ID= getAccount(3);
		else if (EmployeeWorkforce.compareToIgnoreCase("I") == 0)
			C_ValidCombination_ID= getAccount(5);
		else if (EmployeeWorkforce.compareToIgnoreCase("S") == 0)
			C_ValidCombination_ID= getAccount(7);
		return C_ValidCombination_ID;
	}   //  getAccount
	/**
	 *  EmployeeAccountCRE Account from Concept Type Table
	 *  Depending on Employee WorkForce Info
	 *  @param p_AMN_Employee_ID
	 *  @param p_AMN_Concept_Types_ID
	 *  @return C_ValidCombination_ID
	 */
	public int getEmployeeAccountCRE()
	{
		MAMN_Employee amnemployee = new MAMN_Employee(m_ctx, m_AMN_Employee_ID,  m_trxName);
		MAMN_Jobtitle amnjobtitle = new MAMN_Jobtitle(m_ctx, amnemployee.getAMN_Jobtitle_ID(),  m_trxName) ;
		int C_ValidCombination_ID = 0;
		String EmployeeWorkforce = amnjobtitle.getWorkforce();
		if (EmployeeWorkforce.compareToIgnoreCase("A") == 0)
			C_ValidCombination_ID= getAccount(1);
		else if (EmployeeWorkforce.compareToIgnoreCase("D") == 0)
			C_ValidCombination_ID= getAccount(3);
		else if (EmployeeWorkforce.compareToIgnoreCase("I") == 0)
			C_ValidCombination_ID= getAccount(5);
		else if (EmployeeWorkforce.compareToIgnoreCase("S") == 0)
			C_ValidCombination_ID= getAccount(7);
		return C_ValidCombination_ID;
	}   //  getAccount


	public String sqlGetAMNEmployeeValue(int p_AMN_Employee_ID) {
		return m_AMN_Employee.sqlGetAMNEmployeeValue(p_AMN_Employee_ID);
	}


	public String sqlGetAMNEmployeeName(int p_AMN_Employee_ID) {
		return m_AMN_Employee.sqlGetAMNEmployeeName(p_AMN_Employee_ID);
	}


	public String toString() {
		return m_AMN_Employee.toString();
	}


	public void setAMN_Contract_ID(int AMN_Contract_ID) {
		m_AMN_Employee.setAMN_Contract_ID(AMN_Contract_ID);
	}


	public Integer sqlGetAMNEmployeeContractID(int p_AMN_Employee_ID) {
		return m_AMN_Employee.sqlGetAMNEmployeeContractID(p_AMN_Employee_ID);
	}


	public int getAMN_Contract_ID() {
		return m_AMN_Employee.getAMN_Contract_ID();
	}


	public void setAMN_Department_ID(int AMN_Department_ID) {
		m_AMN_Employee.setAMN_Department_ID(AMN_Department_ID);
	}


	public Integer sqlGetAMNEmployeeLocationID(int p_AMN_Employee_ID) {
		return m_AMN_Employee.sqlGetAMNEmployeeLocationID(p_AMN_Employee_ID);
	}


	public int getAMN_Department_ID() {
		return m_AMN_Employee.getAMN_Department_ID();
	}


	public void setAMN_Employee_ID(int AMN_Employee_ID) {
		if (m_AMN_Employee_ID != 0)
			m_AMN_Employee = new MAMN_Employee(m_ctx, m_AMN_Employee_ID, m_trxName);
		m_AMN_Employee.setAMN_Employee_ID(AMN_Employee_ID);
	}


	public int getAMN_Employee_ID() {
		return m_AMN_Employee.getAMN_Employee_ID();
	}


	public void setAMN_Employee_UU(String AMN_Employee_UU) {
		m_AMN_Employee.setAMN_Employee_UU(AMN_Employee_UU);
	}


	public String getAMN_Employee_UU() {
		return m_AMN_Employee.getAMN_Employee_UU();
	}


	public I_AMN_Jobstation getAMN_Jobstation() throws RuntimeException {
		return m_AMN_Employee.getAMN_Jobstation();
	}


	public void setAMN_Jobstation_ID(int AMN_Jobstation_ID) {
		m_AMN_Employee.setAMN_Jobstation_ID(AMN_Jobstation_ID);
	}


	public int getAMN_Jobstation_ID() {
		return m_AMN_Employee.getAMN_Jobstation_ID();
	}


	public I_AMN_Jobtitle getAMN_Jobtitle() throws RuntimeException {
		return m_AMN_Employee.getAMN_Jobtitle();
	}


	public void setAMN_Jobtitle_ID(int AMN_Jobtitle_ID) {
		m_AMN_Employee.setAMN_Jobtitle_ID(AMN_Jobtitle_ID);
	}


	public int getAMN_Jobtitle_ID() {
		return m_AMN_Employee.getAMN_Jobtitle_ID();
	}


	public void setAMN_Location_ID(int AMN_Location_ID) {
		m_AMN_Employee.setAMN_Location_ID(AMN_Location_ID);
	}


	public int getAMN_Location_ID() {
		return m_AMN_Employee.getAMN_Location_ID();
	}


	public void setAMN_Position_ID(int AMN_Position_ID) {
		m_AMN_Employee.setAMN_Position_ID(AMN_Position_ID);
	}


	public int getAMN_Position_ID() {
		return m_AMN_Employee.getAMN_Position_ID();
	}


	public void setAMN_Shift_ID(int AMN_Shift_ID) {
		m_AMN_Employee.setAMN_Shift_ID(AMN_Shift_ID);
	}


	public int getAMN_Shift_ID() {
		return m_AMN_Employee.getAMN_Shift_ID();
	}


	public void setAlergic(String Alergic) {
		m_AMN_Employee.setAlergic(Alergic);
	}


	public String getAlergic() {
		return m_AMN_Employee.getAlergic();
	}


	public void setBioCode(String BioCode) {
		m_AMN_Employee.setBioCode(BioCode);
	}


	public String getBioCode() {
		return m_AMN_Employee.getBioCode();
	}


	public void setBirthday(Timestamp Birthday) {
		m_AMN_Employee.setBirthday(Birthday);
	}


	public Timestamp getBirthday() {
		return m_AMN_Employee.getBirthday();
	}


	public I_C_Activity getC_Activity() throws RuntimeException {
		return m_AMN_Employee.getC_Activity();
	}


	public void setC_Activity_ID(int C_Activity_ID) {
		m_AMN_Employee.setC_Activity_ID(C_Activity_ID);
	}


	public int getC_Activity_ID() {
		return m_AMN_Employee.getC_Activity_ID();
	}


	public I_C_BPartner getC_BPartner() throws RuntimeException {
		return m_AMN_Employee.getC_BPartner();
	}


	public void setC_BPartner_ID(int C_BPartner_ID) {
		m_AMN_Employee.setC_BPartner_ID(C_BPartner_ID);
	}


	public int getC_BPartner_ID() {
		return m_AMN_Employee.getC_BPartner_ID();
	}


	public I_C_Campaign getC_Campaign() throws RuntimeException {
		return m_AMN_Employee.getC_Campaign();
	}


	public boolean equals(Object cmp) {
		return m_AMN_Employee.equals(cmp);
	}


	public void setC_Campaign_ID(int C_Campaign_ID) {
		m_AMN_Employee.setC_Campaign_ID(C_Campaign_ID);
	}


	public int getC_Campaign_ID() {
		return m_AMN_Employee.getC_Campaign_ID();
	}


	public int hashCode() {
		return m_AMN_Employee.hashCode();
	}


	public I_C_Country getC_Country() throws RuntimeException {
		return m_AMN_Employee.getC_Country();
	}


	public int compare(Object o1, Object o2) {
		return m_AMN_Employee.compare(o1, o2);
	}


	public void setC_Country_ID(int C_Country_ID) {
		m_AMN_Employee.setC_Country_ID(C_Country_ID);
	}


	public int getC_Country_ID() {
		return m_AMN_Employee.getC_Country_ID();
	}


	public I_C_Project getC_Project() throws RuntimeException {
		return m_AMN_Employee.getC_Project();
	}


	public void setC_Project_ID(int C_Project_ID) {
		m_AMN_Employee.setC_Project_ID(C_Project_ID);
	}


	public String get_TableName() {
		return m_AMN_Employee.get_TableName();
	}


	public int getC_Project_ID() {
		return m_AMN_Employee.getC_Project_ID();
	}


	public String[] get_KeyColumns() {
		return m_AMN_Employee.get_KeyColumns();
	}


	public I_C_SalesRegion getC_SalesRegion() throws RuntimeException {
		return m_AMN_Employee.getC_SalesRegion();
	}


	public int get_Table_ID() {
		return m_AMN_Employee.get_Table_ID();
	}


	public int get_ID() {
		return m_AMN_Employee.get_ID();
	}


	public void setC_SalesRegion_ID(int C_SalesRegion_ID) {
		m_AMN_Employee.setC_SalesRegion_ID(C_SalesRegion_ID);
	}


	public int get_IDOld() {
		return m_AMN_Employee.get_IDOld();
	}


	public Properties getCtx() {
		return m_AMN_Employee.getCtx();
	}


	public int getC_SalesRegion_ID() {
		return m_AMN_Employee.getC_SalesRegion_ID();
	}


	public CLogger get_Logger() {
		return m_AMN_Employee.get_Logger();
	}


	public final Object get_Value(int index) {
		return m_AMN_Employee.get_Value(index);
	}


	public void setCountryofNacionality_ID(int CountryofNacionality_ID) {
		m_AMN_Employee.setCountryofNacionality_ID(CountryofNacionality_ID);
	}


	public int getCountryofNacionality_ID() {
		return m_AMN_Employee.getCountryofNacionality_ID();
	}


	public int get_ValueAsInt(int index) {
		return m_AMN_Employee.get_ValueAsInt(index);
	}


	public void setDescription(String Description) {
		m_AMN_Employee.setDescription(Description);
	}


	public String getDescription() {
		return m_AMN_Employee.getDescription();
	}


	public final Object get_Value(String columnName) {
		return m_AMN_Employee.get_Value(columnName);
	}


	public void setDeseases(String Deseases) {
		m_AMN_Employee.setDeseases(Deseases);
	}


	public String getDeseases() {
		return m_AMN_Employee.getDeseases();
	}


	public void setEMail(String EMail) {
		m_AMN_Employee.setEMail(EMail);
	}


	public String getEMail() {
		return m_AMN_Employee.getEMail();
	}


	public String get_ValueAsString(String variableName) {
		return m_AMN_Employee.get_ValueAsString(variableName);
	}


	public final Object get_ValueOfColumn(int AD_Column_ID) {
		return m_AMN_Employee.get_ValueOfColumn(AD_Column_ID);
	}


	public void setHandUse(String HandUse) {
		m_AMN_Employee.setHandUse(HandUse);
	}


	public String getHandUse() {
		return m_AMN_Employee.getHandUse();
	}


	public final Object get_ValueOld(int index) {
		return m_AMN_Employee.get_ValueOld(index);
	}


	public void setHeight(BigDecimal Height) {
		m_AMN_Employee.setHeight(Height);
	}


	public BigDecimal getHeight() {
		return m_AMN_Employee.getHeight();
	}


	public final Object get_ValueOld(String columnName) {
		return m_AMN_Employee.get_ValueOld(columnName);
	}


	public void setHobbyes(String Hobbyes) {
		m_AMN_Employee.setHobbyes(Hobbyes);
	}


	public String getHobbyes() {
		return m_AMN_Employee.getHobbyes();
	}


	public void setName(String Name) {
		m_AMN_Employee.setName(Name);
	}


	public int get_ValueOldAsInt(String columnName) {
		return m_AMN_Employee.get_ValueOldAsInt(columnName);
	}


	public String getName() {
		return m_AMN_Employee.getName();
	}


	public KeyNamePair getKeyNamePair() {
		return m_AMN_Employee.getKeyNamePair();
	}


	public void setSalary(BigDecimal Salary) {
		m_AMN_Employee.setSalary(Salary);
	}


	public final boolean is_ValueChanged(int index) {
		return m_AMN_Employee.is_ValueChanged(index);
	}


	public BigDecimal getSalary() {
		return m_AMN_Employee.getSalary();
	}


	public void setSizePant(String SizePant) {
		m_AMN_Employee.setSizePant(SizePant);
	}


	public String getSizePant() {
		return m_AMN_Employee.getSizePant();
	}


	public final boolean is_ValueChanged(String columnName) {
		return m_AMN_Employee.is_ValueChanged(columnName);
	}


	public void setSizeShirt(String SizeShirt) {
		m_AMN_Employee.setSizeShirt(SizeShirt);
	}


	public String getSizeShirt() {
		return m_AMN_Employee.getSizeShirt();
	}


	public final Object get_ValueDifference(int index) {
		return m_AMN_Employee.get_ValueDifference(index);
	}


	public void setSizeShoe(String SizeShoe) {
		m_AMN_Employee.setSizeShoe(SizeShoe);
	}


	public String getSizeShoe() {
		return m_AMN_Employee.getSizeShoe();
	}


	public void setSports(String Sports) {
		m_AMN_Employee.setSports(Sports);
	}


	public String getSports() {
		return m_AMN_Employee.getSports();
	}


	public void setStatus(String Status) {
		m_AMN_Employee.setStatus(Status);
	}


	public final Object get_ValueDifference(String columnName) {
		return m_AMN_Employee.get_ValueDifference(columnName);
	}


	public String getStatus() {
		return m_AMN_Employee.getStatus();
	}


	public void setUseLenses(boolean UseLenses) {
		m_AMN_Employee.setUseLenses(UseLenses);
	}


	public boolean isUseLenses() {
		return m_AMN_Employee.isUseLenses();
	}


	public void setValue(String Value) {
		m_AMN_Employee.setValue(Value);
	}


	public String getValue() {
		return m_AMN_Employee.getValue();
	}


	public void setWeight(BigDecimal Weight) {
		m_AMN_Employee.setWeight(Weight);
	}


	public BigDecimal getWeight() {
		return m_AMN_Employee.getWeight();
	}


	public void setbirthplace(String birthplace) {
		m_AMN_Employee.setbirthplace(birthplace);
	}


	public String getbirthplace() {
		return m_AMN_Employee.getbirthplace();
	}


	public void setbloodrh(String bloodrh) {
		m_AMN_Employee.setbloodrh(bloodrh);
	}


	public String getbloodrh() {
		return m_AMN_Employee.getbloodrh();
	}


	public void setbloodtype(String bloodtype) {
		m_AMN_Employee.setbloodtype(bloodtype);
	}


	public String getbloodtype() {
		return m_AMN_Employee.getbloodtype();
	}


	public void setcivilstatus(String civilstatus) {
		m_AMN_Employee.setcivilstatus(civilstatus);
	}


	public String getcivilstatus() {
		return m_AMN_Employee.getcivilstatus();
	}


	public void setdownwardloads(BigDecimal downwardloads) {
		m_AMN_Employee.setdownwardloads(downwardloads);
	}


	public BigDecimal getdownwardloads() {
		return m_AMN_Employee.getdownwardloads();
	}


	public void seteducationgrade(String educationgrade) {
		m_AMN_Employee.seteducationgrade(educationgrade);
	}


	public String geteducationgrade() {
		return m_AMN_Employee.geteducationgrade();
	}


	public void seteducationlevel(String educationlevel) {
		m_AMN_Employee.seteducationlevel(educationlevel);
	}


	public String geteducationlevel() {
		return m_AMN_Employee.geteducationlevel();
	}


	public void setegresscondition(String egresscondition) {
		m_AMN_Employee.setegresscondition(egresscondition);
	}


	public String getegresscondition() {
		return m_AMN_Employee.getegresscondition();
	}


	public void setegressdate(Timestamp egressdate) {
		m_AMN_Employee.setegressdate(egressdate);
	}


	public Timestamp getegressdate() {
		return m_AMN_Employee.getegressdate();
	}


	public void setempimg1_ID(int empimg1_ID) {
		m_AMN_Employee.setempimg1_ID(empimg1_ID);
	}


	public int getempimg1_ID() {
		return m_AMN_Employee.getempimg1_ID();
	}


	public void setempimg2_ID(int empimg2_ID) {
		m_AMN_Employee.setempimg2_ID(empimg2_ID);
	}


	public int getempimg2_ID() {
		return m_AMN_Employee.getempimg2_ID();
	}


	public void setincomedate(Timestamp incomedate) {
		m_AMN_Employee.setincomedate(incomedate);
	}


	public Timestamp getincomedate() {
		return m_AMN_Employee.getincomedate();
	}


	public void setincreasingloads(BigDecimal increasingloads) {
		m_AMN_Employee.setincreasingloads(increasingloads);
	}


	public BigDecimal getincreasingloads() {
		return m_AMN_Employee.getincreasingloads();
	}


	public void setisMedicated(boolean isMedicated) {
		m_AMN_Employee.setisMedicated(isMedicated);
	}


	public boolean isMedicated() {
		return m_AMN_Employee.isMedicated();
	}


	public void setProcessedOn(String ColumnName, Object value, Object oldValue) {
		m_AMN_Employee.setProcessedOn(ColumnName, value, oldValue);
	}


	public void setisPensioned(boolean isPensioned) {
		m_AMN_Employee.setisPensioned(isPensioned);
	}


	public boolean isPensioned() {
		return m_AMN_Employee.isPensioned();
	}


	public void setisStudying(boolean isStudying) {
		m_AMN_Employee.setisStudying(isStudying);
	}


	public boolean isStudying() {
		return m_AMN_Employee.isStudying();
	}


	public final boolean set_ValueNoCheck(String ColumnName, Object value) {
		return m_AMN_Employee.set_ValueNoCheck(ColumnName, value);
	}


	public void setjobcondition(String jobcondition) {
		m_AMN_Employee.setjobcondition(jobcondition);
	}


	public final void set_ValueOfColumn(String columnName, Object value) {
		m_AMN_Employee.set_ValueOfColumn(columnName, value);
	}


	public String getjobcondition() {
		return m_AMN_Employee.getjobcondition();
	}


	public final boolean set_ValueOfColumnReturningBoolean(String columnName,
			Object value) {
		return m_AMN_Employee.set_ValueOfColumnReturningBoolean(columnName,
				value);
	}


	public void setpaymenttype(String paymenttype) {
		m_AMN_Employee.setpaymenttype(paymenttype);
	}


	public final void set_ValueOfColumn(int AD_Column_ID, Object value) {
		m_AMN_Employee.set_ValueOfColumn(AD_Column_ID, value);
	}


	public String getpaymenttype() {
		return m_AMN_Employee.getpaymenttype();
	}


	public final boolean set_ValueOfColumnReturningBoolean(int AD_Column_ID,
			Object value) {
		return m_AMN_Employee.set_ValueOfColumnReturningBoolean(AD_Column_ID,
				value);
	}


	public void setpayrollmode(String payrollmode) {
		m_AMN_Employee.setpayrollmode(payrollmode);
	}


	public String getpayrollmode() {
		return m_AMN_Employee.getpayrollmode();
	}


	public final void set_CustomColumn(String columnName, Object value) {
		m_AMN_Employee.set_CustomColumn(columnName, value);
	}


	public void setprivateassist(String privateassist) {
		m_AMN_Employee.setprivateassist(privateassist);
	}


	public final boolean set_CustomColumnReturningBoolean(String columnName,
			Object value) {
		return m_AMN_Employee.set_CustomColumnReturningBoolean(columnName,
				value);
	}


	public String getprivateassist() {
		return m_AMN_Employee.getprivateassist();
	}


	public void setprofession(String profession) {
		m_AMN_Employee.setprofession(profession);
	}


	public String getprofession() {
		return m_AMN_Employee.getprofession();
	}


	public void setsex(String sex) {
		m_AMN_Employee.setsex(sex);
	}


	public String getsex() {
		return m_AMN_Employee.getsex();
	}


	public void setspouse(String spouse) {
		m_AMN_Employee.setspouse(spouse);
	}


	public String getspouse() {
		return m_AMN_Employee.getspouse();
	}


	public void setzodiacsign(String zodiacsign) {
		m_AMN_Employee.setzodiacsign(zodiacsign);
	}


	public String getzodiacsign() {
		return m_AMN_Employee.getzodiacsign();
	}


	public int get_ColumnCount() {
		return m_AMN_Employee.get_ColumnCount();
	}


	public String get_ColumnName(int index) {
		return m_AMN_Employee.get_ColumnName(index);
	}


	public final int get_ColumnIndex(String columnName) {
		return m_AMN_Employee.get_ColumnIndex(columnName);
	}


	public String get_DisplayValue(String columnName, boolean currentValue) {
		return m_AMN_Employee.get_DisplayValue(columnName, currentValue);
	}


	public boolean load(String trxName) {
		return m_AMN_Employee.load(trxName);
	}


	public final int getAD_Client_ID() {
		return m_AMN_Employee.getAD_Client_ID();
	}


	public final void setAD_Org_ID(int AD_Org_ID) {
		m_AMN_Employee.setAD_Org_ID(AD_Org_ID);
	}


	public int getAD_Org_ID() {
		return m_AMN_Employee.getAD_Org_ID();
	}


	public final void setIsActive(boolean active) {
		m_AMN_Employee.setIsActive(active);
	}


	public final boolean isActive() {
		return m_AMN_Employee.isActive();
	}


	public final Timestamp getCreated() {
		return m_AMN_Employee.getCreated();
	}


	public final Timestamp getUpdated() {
		return m_AMN_Employee.getUpdated();
	}


	public final int getCreatedBy() {
		return m_AMN_Employee.getCreatedBy();
	}


	public final int getUpdatedBy() {
		return m_AMN_Employee.getUpdatedBy();
	}


	public String get_Translation(String columnName, String AD_Language) {
		return m_AMN_Employee.get_Translation(columnName, AD_Language);
	}


	public String get_Translation(String columnName, String AD_Language,
			boolean reload) {
		return m_AMN_Employee.get_Translation(columnName, AD_Language, reload);
	}


	public String get_Translation(String columnName) {
		return m_AMN_Employee.get_Translation(columnName);
	}


	public boolean is_new() {
		return m_AMN_Employee.is_new();
	}


	public boolean save() {
		return m_AMN_Employee.save();
	}


	public void saveEx() throws AdempiereException {
		m_AMN_Employee.saveEx();
	}


	public boolean save(String trxName) {
		return m_AMN_Employee.save(trxName);
	}


	public void saveReplica(boolean isFromReplication)
			throws AdempiereException {
		m_AMN_Employee.saveReplica(isFromReplication);
	}


	public void saveEx(String trxName) throws AdempiereException {
		m_AMN_Employee.saveEx(trxName);
	}


	public boolean is_Changed() {
		return m_AMN_Employee.is_Changed();
	}


	public String get_WhereClause(boolean withValues) {
		return m_AMN_Employee.get_WhereClause(withValues);
	}


	public boolean delete(boolean force) {
		return m_AMN_Employee.delete(force);
	}


	public void deleteEx(boolean force) throws AdempiereException {
		m_AMN_Employee.deleteEx(force);
	}


	public boolean delete(boolean force, String trxName) {
		return m_AMN_Employee.delete(force, trxName);
	}


	public void deleteEx(boolean force, String trxName)
			throws AdempiereException {
		m_AMN_Employee.deleteEx(force, trxName);
	}


	public boolean lock() {
		return m_AMN_Employee.lock();
	}


	public boolean unlock(String trxName) {
		return m_AMN_Employee.unlock(trxName);
	}


	public void set_TrxName(String trxName) {
		m_AMN_Employee.set_TrxName(trxName);
	}


	public String get_TrxName() {
		return m_AMN_Employee.get_TrxName();
	}


	public MAttachment getAttachment() {
		return m_AMN_Employee.getAttachment();
	}


	public MAttachment getAttachment(boolean requery) {
		return m_AMN_Employee.getAttachment(requery);
	}


	public MAttachment createAttachment() {
		return m_AMN_Employee.createAttachment();
	}


	public boolean isAttachment(String extension) {
		return m_AMN_Employee.isAttachment(extension);
	}


	public byte[] getAttachmentData(String extension) {
		return m_AMN_Employee.getAttachmentData(extension);
	}


	public boolean isPdfAttachment() {
		return m_AMN_Employee.isPdfAttachment();
	}


	public byte[] getPdfAttachment() {
		return m_AMN_Employee.getPdfAttachment();
	}


	public void dump() {
		m_AMN_Employee.dump();
	}


	public void dump(int index) {
		m_AMN_Employee.dump(index);
	}


	public StringBuffer get_xmlString(StringBuffer xml) {
		return m_AMN_Employee.get_xmlString(xml);
	}


	public Document get_xmlDocument(boolean noComment) {
		return m_AMN_Employee.get_xmlDocument(noComment);
	}


	public void setDoc(Doc doc) {
		m_AMN_Employee.setDoc(doc);
	}


	public void setReplication(boolean isFromReplication) {
		m_AMN_Employee.setReplication(isFromReplication);
	}


	public boolean isReplication() {
		return m_AMN_Employee.isReplication();
	}


	public Doc getDoc() {
		return m_AMN_Employee.getDoc();
	}


	public int get_ValueAsInt(String columnName) {
		return m_AMN_Employee.get_ValueAsInt(columnName);
	}


	public boolean get_ValueAsBoolean(String columnName) {
		return m_AMN_Employee.get_ValueAsBoolean(columnName);
	}


	public String getUUIDColumnName() {
		return m_AMN_Employee.getUUIDColumnName();
	}


	public void set_Attribute(String columnName, Object value) {
		m_AMN_Employee.set_Attribute(columnName, value);
	}


	public Object get_Attribute(String columnName) {
		return m_AMN_Employee.get_Attribute(columnName);
	}


	public HashMap<String, Object> get_Attributes() {
		return m_AMN_Employee.get_Attributes();
	}
	
	
}

package org.amerp.process.imp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.adempiere.model.ImportValidator;
import org.amerp.amnmodel.I_AMN_I_Employee;
import org.amerp.amnmodel.I_AMN_I_Employee_Salary;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_I_Employee;
import org.amerp.amnmodel.X_AMN_I_Employee;
import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MContactInterest;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.X_I_BPartner;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class AMNImportEmployee extends SvrProcess{

	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;
	/**	Only validate, don't import		*/
	private boolean			p_IsValidateOnly = false;
	/** BDZero	 						*/
	BigDecimal BDZero = BigDecimal.ZERO;
	
	@Override
	protected void prepare() {
		// Pararmeters
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.equals("AD_Client_ID"))
				m_AD_Client_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(para[i].getParameter());
			else if (name.equals("IsValidateOnly"))
				p_IsValidateOnly = para[i].getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		//Process
		StringBuilder sql = null;
		int no = 0;
		String clientCheck = getWhereClause();

		//	****	Prepare	****

		// -----------------------------------------
		// Validate Process
		// -----------------------------------------
		//	Delete Old Imported
		if (m_deleteOldImported)
		{
			sql = new StringBuilder ("DELETE AMN_I_Employee_Salary ")
					.append("WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdateEx(sql.toString(), get_TrxName());
			if (log.isLoggable(Level.FINE)) log.fine("Delete Old Impored =" + no);
		}

		//		Set Client, Org, IsActive, Created/Updated
		sql = new StringBuilder ("UPDATE AMN_I_Employee ")
				.append("SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),")
						.append(" AD_Org_ID = COALESCE (AD_Org_ID, 0),")
						.append(" IsActive = COALESCE (IsActive, 'Y'),")
						.append(" Created = COALESCE (Created, getDate()),")
						.append(" CreatedBy = COALESCE (CreatedBy, 0),")
						.append(" Updated = COALESCE (Updated, getDate()),")
						.append(" UpdatedBy = COALESCE (UpdatedBy, 0),")
						.append(" I_ErrorMsg = ' ',")
						.append(" I_IsImported = 'N' ")
						.append("WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Reset=" + no);
	
		commitEx();
		
		if (p_IsValidateOnly)
		{
			return "Validated";
		}

		// -------------------------------------------------------------------
		// Import Process
		// -------------------------------------------------------------------
		
		int noInsert = 0;
		int noInsertBP = 0;
		int noUpdate = 0;

		//	Go through Records
		sql = new StringBuilder ("SELECT * FROM AMN_I_Employee ")
				.append("WHERE I_IsImported='N'").append(clientCheck);
		// gody: 20070113 - Order so the same values are consecutive.
		sql.append(" ORDER BY Value ");
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();

			// Remember Previous Employee Value BP is only first one, others are contacts.
			String Old_EmployeeValue = "" ; 
			MAMN_Employee emp = null;
			MBPartnerLocation bpl = null;
			int C_BPartner_ID = 0;
			int Bill_BPartner_ID = 0;
			MBPartner impBP1 = null;
			MBPartner impBP2 = null;
			int AMN_I_Employee_ID = 0;

			while (rs.next())
			{	
				
				//C_BPartner
				C_BPartner_ID = rs.getInt("C_BPartner_ID") ;
				Bill_BPartner_ID = rs.getInt("C_BPartner_ID") ;
				impBP1 = new MBPartner (getCtx(), C_BPartner_ID, get_TrxName());
				impBP2 = new MBPartner (getCtx(), Bill_BPartner_ID, get_TrxName());
				// 
				String userError="";
	
				// Remember Value - only first occurance of the value is Employee
				String New_EmployeeValue = rs.getString("Value") ;

//				X_AMN_I_Employee impEmployee = new X_AMN_I_Employee (getCtx(), rs, get_TrxName());
				MAMN_I_Employee impEmployee = new MAMN_I_Employee (getCtx(), rs, get_TrxName());
				AMN_I_Employee_ID = impEmployee.getAMN_I_Employee_ID();
				StringBuilder msglog = new StringBuilder("AMN_I_Employee_ID=") .append(impEmployee.getAMN_I_Employee_ID())
						.append(", AMN_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
				if (log.isLoggable(Level.FINE)) log.fine(msglog.toString());

				// -------------------------------------------------------------------
				// C_BPartner (Business Partner) 
				// -------------------------------------------------------------------
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
					
					//	****	Create/Update C_BPartner	****
					if (impBP1.getC_BPartner_ID() == 0)	{
					//	Insert new BPartner
						setBPartnerFromImpEmployee(getCtx(), impBP1, impEmployee, get_TrxName());
						try {
							if (impBP1.save()) {
								impEmployee.setC_BPartner_ID(impBP1.getC_BPartner_ID());
								msglog = new StringBuilder("Insert BPartner - ").append(impBP1.getC_BPartner_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								noInsertBP++;
							} else	{
//								sql = new StringBuilder ("UPDATE AMN_I_Employee i ")
//										.append("SET I_IsImported='N', I_ErrorMsg=I_ErrorMsg||")
//								.append("'Cannot Insert BPartner, ' ")
//								.append("WHERE AMN_I_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
//								DB.executeUpdateEx(sql.toString(), get_TrxName());
								updateErrorMessage(false, "Cannot Insert Bill BPartner, ", impEmployee);
								continue;
							}
						} finally {
								
						}
					} else {				
					//	Update existing BPartner
					
					}
				}
				// -------------------------------------------------------------------
				// Bill_BPartner (Bill Business Partner) 
				// -------------------------------------------------------------------
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {		
					//	****	Create/Update Bill C_BPartner	****
					if (impBP2.getC_BPartner_ID() == 0)	{
						// Verify Bill_BPValue
						Bill_BPartner_ID = getBPartnerIDByValue(getCtx(), impEmployee.getBill_BPValue(), m_AD_Client_ID, get_TrxName());
						if (Bill_BPartner_ID  ==  0 ) {
							setBillBPartnerFromImpEmployee(getCtx(), impBP2, impEmployee, get_TrxName());
							try {
								if (impBP2.save()) {
									impEmployee.setBill_BPartner_ID(impBP2.getC_BPartner_ID());
									msglog = new StringBuilder("Insert Bill BPartner - ").append(impBP2.getC_BPartner_ID());
									if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								} else	{
//									sql = new StringBuilder ("AMN_I_Employee i ")
//											.append("SET I_IsImported='N', I_ErrorMsg=I_ErrorMsg||")
//									.append("'Cannot Insert BPartner, ' ")
//									.append("WHERE AMN_I_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
//									DB.executeUpdateEx(sql.toString(), get_TrxName());
									updateErrorMessage(false, "Cannot Insert Bill BPartner, ", impEmployee);
									continue;
								}
							}
							finally {
								
							}	
						} else {
							impEmployee.setBill_BPartner_ID(Bill_BPartner_ID);
						}
					} else {				
					//	Update existing BPartner
					
					}
					
				}
				
				// -------------------------------------------------------------------
				// AMN_Employee 
				// -------------------------------------------------------------------
				
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
					//	****	Create/Update Employee	****
					emp = null;

					if (impEmployee.getAMN_Employee_ID() == 0)	//	Insert new Employee
					{
						emp = new MAMN_Employee(impEmployee);
						try {
							if (emp.save()) {
								impEmployee.setAMN_Employee_ID(emp.getAMN_Employee_ID());
								msglog = new StringBuilder("Insert Employee - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
//								sql = new StringBuilder ("UPDATE AMN_I_Employee i ")
//										.append("SET I_IsImported='N', I_ErrorMsg=I_ErrorMsg||")
//								.append("'Cannot Insert Employee, ' ")
//								.append("WHERE AMN_I_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
//								DB.executeUpdateEx(sql.toString(), get_TrxName());
								updateErrorMessage(false, "Cannot Insert Employee, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
					}
					else				//	Update existing BPartner
					{
						emp = new MAMN_Employee(getCtx(), impEmployee.getAMN_Employee_ID(), get_TrxName());
						try {
							if (emp.save()) {
								msglog = new StringBuilder("Update Employee - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
//								sql = new StringBuilder ("UPDATE AMN_I_Employee i ")
//										.append("SET I_IsImported='N', I_ErrorMsg=I_ErrorMsg||")
//								.append("'Cannot Insert Employee, ' ")
//								.append("WHERE AMN_I_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
//								DB.executeUpdateEx(sql.toString(), get_TrxName());
								// Updates Error
								updateErrorMessage(false, "Cannot Update Employee, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
					}

				}
				
				// -------------------------------------------------------------------
				// ****	Create/Update Contact	****
				// -------------------------------------------------------------------
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
					
					MUser[]  users =  impBP1.getContacts(true);
					if ( users.length == 0) {
						MUser defaultUser = new MUser(getCtx(), 0, get_TrxName());
						// Set Default User for Employee.
						setUserFromImpEmployee(getCtx(), defaultUser, impEmployee, get_TrxName());
						try {
							if (defaultUser.save()) {
								msglog = new StringBuilder("Insert User - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert default User, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}

					}				
				}
				
				// -------------------------------------------------------------------
				// ****	Create/Update Location	****
				// -------------------------------------------------------------------
				
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
				
					MBPartnerLocation[] locations = impBP1.getLocations(true);
					
					if ( locations.length == 0) {
						MBPartnerLocation location = new MBPartnerLocation(getCtx(), 0, get_TrxName());
				        MLocation loc = new MLocation(getCtx(), 0, get_TrxName());
						// Set Default User for Employee.
						setBPLocationFromImpEmployee(getCtx(), location, loc, impEmployee, get_TrxName());
						try {
							if (loc.save()) {
								msglog = new StringBuilder("Insert Location - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert default Location, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
						location.setC_Location_ID(loc.getC_Location_ID());
						try {
							if (location.save()) {
								msglog = new StringBuilder("Insert BPartnerLocation - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert default BPartnerLocation, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
				
					}
				}
				
				// -------------------------------------------------------------------
				// ****	Create/Update BankAccount	****
				// -------------------------------------------------------------------
			
				// Get Default C_BP_BankAccount for Employee.
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
					
					MBPBankAccount[] mbpbank = impBP1.getBankAccounts(false);
					
					if ( mbpbank.length == 0) {
						MBPBankAccount bank1 = new MBPBankAccount(getCtx(), 0, get_TrxName());
						MBPBankAccount bank2 = new MBPBankAccount(getCtx(), 0, get_TrxName());
						// Set Default Bank Accounts for Employee.
						setBPBankAccountsFromImpEmployee(getCtx(), bank1, bank2, impEmployee, get_TrxName());
						// Bank Account CR ("B");
						try {
							if (bank1.save()) {
								msglog = new StringBuilder("Insert Bank Account - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert Bank Account, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
						// Bank Account DB ("N");
						try {
							if (bank2.save()) {
								msglog = new StringBuilder("Insert Bank Account - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert Bank Account, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
					}	
				}
				
				// 
				// -------------------------------------------------------------------
				// ****	Final Commit
				// -------------------------------------------------------------------
				
				
				Old_EmployeeValue = New_EmployeeValue ;
//				impEmployee.setI_IsImported(true);
//				impEmployee.setProcessed(true);
//				impEmployee.setProcessing(false);
				impEmployee.saveEx();
				commitEx();
			}
			DB.close(rs, pstmt);
		}
		catch (SQLException e)
		{
			rollback();
			//log.log(Level.SEVERE, "", e);
			throw new DBException(e, sql.toString());
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
			//	Set Error to indicator to not imported
			sql = new StringBuilder ("UPDATE I_BPartner ")
					.append("SET I_IsImported='N', Updated=getDate() ")
					.append("WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdateEx(sql.toString(), get_TrxName());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@C_BPartner_ID@: @Inserted@");
			addLog (0, null, new BigDecimal (noUpdate), "@C_BPartner_ID@: @Updated@");
		}
		return "";
	}

	//@Override
	public String getWhereClause()
	{
		StringBuilder msgreturn = new StringBuilder(" AND AD_Client_ID=").append(m_AD_Client_ID);
		return msgreturn.toString();
	}


	//@Override
	public String getImportTableName()
	{
		return I_AMN_I_Employee.Table_Name;
		//return "AMN_I_Employee_Salary";
	}
	
	/**
	 * setBPartnerFromImpEmployee
	 * @param bp
	 * @param impEmployee
	 */
	private void setBPartnerFromImpEmployee(Properties ctx, MBPartner bp, MAMN_I_Employee impEmployee, String trxName) {
		
		MBPGroup bpg = MBPGroup.getDefault(ctx);
		//
		String value = impEmployee.getValue();
		if (value == null || value.length() == 0)
			value = impEmployee.getEMail();
		if (value == null || value.length() == 0)
			value = impEmployee.getName();
		bp.setValue(value);
		String name = impEmployee.getName();
		if (name == null || name.length() == 0)
			name = impEmployee.getDescription();
		if (name == null || name.length() == 0)
			name = impEmployee.getEMail();
		bp.setName(name);
		bp.setName2("");
		bp.setDescription(impEmployee.getDescription());
		bp.setTaxID(impEmployee.getIDNumber());
		bp.setIsCustomer(true);
		bp.setIsVendor(true);
		bp.setIsEmployee(true);
		bp.setC_BP_Group_ID(bpg.getC_BP_Group_ID());
		
	}
	
	private void setBillBPartnerFromImpEmployee(Properties ctx, MBPartner bp, MAMN_I_Employee impEmployee, String trxName) {
		
		MBPartner bbp = new MBPartner(ctx);
		MBPGroup bpg = MBPGroup.getDefault(ctx);
		//
		String value = impEmployee.getValue();
		if (value == null || value.length() == 0)
			value = impEmployee.getEMail();
		if (value == null || value.length() == 0)
			value = impEmployee.getName();
		bp.setValue(value);
		String name = impEmployee.getName();
		if (name == null || name.length() == 0)
			name = impEmployee.getDescription();
		if (name == null || name.length() == 0)
			name = impEmployee.getEMail();
		bp.setName(name);
		bp.setName2("");
		bp.setDescription(impEmployee.getDescription());
		bp.setTaxID(impEmployee.getIDNumber());
		bp.setIsCustomer(true);
		bp.setIsVendor(true);
		bp.setIsEmployee(true);
		bp.setC_BP_Group_ID(bpg.getC_BP_Group_ID());
		
	}
	
	 /**
     * Busca un tercero (Business Partner) por su valor (Value) y devuelve su C_BPartner_ID.
     *
     * @param ctx      Contexto del sistema.
     * @param value    Valor único del tercero (Value)
     * @param AD_Client_ID grupo empresarial
     * @param trxName  Nombre de la transacción.
     * @return ID del tercero (C_BPartner_ID) o 0 si no se encuentra.
     */
    private static int getBPartnerIDByValue(Properties ctx, String value, int AD_Client_ID, String trxName) {
        if (value == null || value.trim().isEmpty()) {
            return 0; // Devuelve 0 si el valor es nulo o vacío
        }

        // Query para buscar el ID del Business Partner basado en el Value
        String sql = "SELECT C_BPartner_ID FROM C_BPartner WHERE Value = ? AND AD_Client_ID = ?";

        // Ejecuta la consulta
        int cBPartnerID = DB.getSQLValue(trxName, sql, value, AD_Client_ID);

        // Si no se encuentra, DB.getSQLValue devuelve -1
        return cBPartnerID > 0 ? cBPartnerID : 0;
    }
    
    /**
     * setUserFromImpEmployee.
     * Set user fields from Import Employee
     * @param ctx
     * @param user
     * @param impEmployee
     * @param trxName
     */
    private void setUserFromImpEmployee(Properties ctx, MUser user, MAMN_I_Employee impEmployee, String trxName) {
		
    	user.setValue(impEmployee.getValue());
    	user.setName(impEmployee.getValue());
    	user.setDescription(impEmployee.getName());
    	user.setEMail(impEmployee.getEMail());
    	user.setPassword("123456");
    	user.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
    	
    }
    
    /**
     * setBPLocationFromImpEmployee
     * Set location fields from Import Employee
     * @param ctx
     * @param bploc
     * @param impEmployee
     * @param trxName
     */
    private void setBPLocationFromImpEmployee(Properties ctx, MBPartnerLocation bploc,  MLocation loc, MAMN_I_Employee impEmployee, String trxName) {
		
    	// C_BPartnerLocation table
    	bploc.setName(impEmployee.getValue());
    	bploc.setPhone(impEmployee.getPhone());
       	bploc.setPhone2(impEmployee.getPhone2());
       	bploc.setFax(impEmployee.getFax());
        bploc.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
        // C_Location table
        loc.setC_Country_ID(impEmployee.getC_Country_ID());
        loc.setC_Region_ID(impEmployee.getC_Region_ID());
        loc.setAddress1(impEmployee.getAddress1());
        loc.setAddress2(impEmployee.getAddress2());
        loc.setC_City_ID(impEmployee.getC_City_ID());
        
    }

    /**
     * setBPBankAccountsFromImpEmployee
     * Set bank accounts fields from Import Employee
     * @param ctx
     * @param bank1
     * @param bank2
     * @param impEmployee
     * @param trxName
     */
    private void setBPBankAccountsFromImpEmployee(Properties ctx, MBPBankAccount bank1,  MBPBankAccount bank2, MAMN_I_Employee impEmployee, String trxName) {
    	
    	// Bank Account CR Use (B)
    	bank1.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
    	bank1.setAccountNo(impEmployee.getAccountNo_B());
    	bank1.setBPBankAcctUse("B");
    	// Bank Account DB Use (N)
    	bank2.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
    	bank2.setAccountNo(impEmployee.getAccountNo_N());
    	bank1.setBPBankAcctUse("N");
    }
    
    
    /**
     * updateErrorMessage
     * Updates error message field for table AMN_I_Employee
     * @param isImported
     * @param newErrorMsg
     * @param impEmployee
     */
    private void updateErrorMessage(boolean isImported, String newErrorMsg, MAMN_I_Employee impEmployee ) {
    	
		StringBuilder sql = new StringBuilder ("UPDATE AMN_I_Employee i ");
		
		sql.append(" SET I_IsImported='N', "); 
		sql.append(" I_ErrorMsg = I_ErrorMsg || ");
		sql.append("'"+newErrorMsg+"'");
		sql.append(" WHERE AMN_I_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
		DB.executeUpdateEx(sql.toString(), get_TrxName());
		
    }
}

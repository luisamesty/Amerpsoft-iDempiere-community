package org.amerp.process.imp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.amerp.amnmodel.I_AMN_I_Employee;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_I_Employee;
import org.amerp.amnmodel.MAMN_Location;
import org.compiere.model.MBPBankAccount;
import org.compiere.model.MBPGroup;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MUser;
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
			sql = new StringBuilder ("DELETE FROM AMN_I_Employee ")
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
			int C_BPartner_ID = 0;
			int Bill_BPartner_ID = 0;
			MBPartner impBP1 = null;
			MBPartner impBP2 = null;
			
			while (rs.next())
			{	
				
				//C_BPartner
				C_BPartner_ID = rs.getInt("C_BPartner_ID") ;
				Bill_BPartner_ID = rs.getInt("Bill_BPartner_ID") ;
				impBP1 = new MBPartner (getCtx(), C_BPartner_ID, get_TrxName());
				impBP2 = new MBPartner (getCtx(), Bill_BPartner_ID, get_TrxName());
				// Variables de Validaciones 
				boolean okBP1 = false;
				boolean okBP2 = false;
				boolean okEMP = false;
				boolean okUSR = false;
				boolean okADD = false;
				boolean okBKA = false;
	
				// Remember Value - only first occurance of the value is Employee
				String New_EmployeeValue = rs.getString("Value") ;

//				X_AMN_I_Employee impEmployee = new X_AMN_I_Employee (getCtx(), rs, get_TrxName());
				MAMN_I_Employee impEmployee = new MAMN_I_Employee (getCtx(), rs, get_TrxName());
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
						C_BPartner_ID = getBPartnerIDByValue(getCtx(), impEmployee.getBPValue(), m_AD_Client_ID, get_TrxName());
						if (C_BPartner_ID != 0)
							impBP1 = new MBPartner (getCtx(), C_BPartner_ID, get_TrxName());
						setBPartnerFromImpEmployee(getCtx(), impBP1, impEmployee, get_TrxName());
						try {
							if (impBP1.save()) {
								impEmployee.setC_BPartner_ID(impBP1.getC_BPartner_ID());
								msglog = new StringBuilder("Insert BPartner - ").append(impBP1.getC_BPartner_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								okBP1=true;
							} else	{
								updateErrorMessage(false, "Cannot Insert Bill BPartner, ", impEmployee);
								continue;
							}
						} finally {
								
						}
					} else {				
					//	Update existing BPartner
						impBP1 = new MBPartner (getCtx(), C_BPartner_ID, get_TrxName());
						setBPartnerFromImpEmployee(getCtx(), impBP1, impEmployee, get_TrxName());
						try {
							if (impBP1.save()) {
								impEmployee.setC_BPartner_ID(impBP1.getC_BPartner_ID());
								msglog = new StringBuilder("Update BPartner - ").append(impBP1.getC_BPartner_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								okBP1=true;
							} else	{
								updateErrorMessage(false, "Cannot Update Bill BPartner, ", impEmployee);
								continue;
							}
						} finally {
								
						}
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
						if (Bill_BPartner_ID != 0)
							impBP2 = new MBPartner (getCtx(), Bill_BPartner_ID, get_TrxName());
						if (Bill_BPartner_ID  ==  0 ) {
							setBillBPartnerFromImpEmployee(getCtx(), impBP2, impEmployee, get_TrxName());
							try {
								if (impBP2.save()) {
									impEmployee.setBill_BPartner_ID(impBP2.getC_BPartner_ID());
									msglog = new StringBuilder("Insert Bill BPartner - ").append(impBP2.getC_BPartner_ID());
									if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
									okBP2=true;
								} else	{
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
						// Bill_BP 
						okBP2=true;
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
								noInsert++;
								okEMP=true;
							} else	{
								updateErrorMessage(false, "Cannot Insert Employee, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}
					} else {
						//	Update existing Employee
						emp = new MAMN_Employee(getCtx(), impEmployee.getAMN_Employee_ID(), get_TrxName());
						try {
							if (emp.save()) {
								msglog = new StringBuilder("Update Employee - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								okEMP=true;
								noUpdate++;
							} else	{
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
								okUSR=true;
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert default User, ", impEmployee);
								continue;
							}
						}
						finally {
							
						}

					} else {
						okUSR=true;
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
							log.warning("Default Location Inserted");
						}
						location.setC_Location_ID(loc.getC_Location_ID());
						try {
							if (location.save()) {
								msglog = new StringBuilder("Insert BPartnerLocation - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								okADD=true;
							} else	{
								// Updates Error
								updateErrorMessage(false, "Cannot Insert default BPartnerLocation, ", impEmployee);
								continue;
							}
						}
						finally {
							log.warning("Default BPartnerLocation Inserted");
						}
						
						// Employee AMN_Location Address 2
						MBPartnerLocation location2 = new MBPartnerLocation(getCtx(), 0, get_TrxName());
						MAMN_Location emploc = new MAMN_Location(getCtx(), impEmployee.getAMN_Location_ID(), get_TrxName());
						if (emploc != null ) {
							MLocation loc2 = new MLocation(getCtx(), emploc.getC_Location_ID(), get_TrxName());
							setBPLocationFromAMN_Location(getCtx(), location2, loc2, impEmployee, emploc,  get_TrxName());
							try {
								if (location2.save()) {
									msglog = new StringBuilder("Insert BPartnerLocation 2 - ").append(emp.getAMN_Employee_ID());
									if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
									okADD=true;
								} else	{
									// Updates Error
									updateErrorMessage(false, "Cannot Insert default BPartnerLocation 2, ", impEmployee);
									continue;
								}
							}
							finally {
								log.warning("Default BPartnerLocation 2 Inserted");
							}
							
						}
					} else {
						okADD=true;
					}
				}
				
				// -------------------------------------------------------------------
				// ****	Create/Update BankAccount	****
				// -------------------------------------------------------------------
			
				// Get Default C_BP_BankAccount for Employee.
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
					
					MBPBankAccount[] mbpbankacct = impBP1.getBankAccounts(false);
					
					if ( mbpbankacct.length == 0) {
						MBPBankAccount bankacctB = new MBPBankAccount(getCtx(), 0, get_TrxName());
						MBPBankAccount bankacctN = new MBPBankAccount(getCtx(), 0, get_TrxName());
						// Set Default Bank Accounts for Employee.
						setBPBankAccountsFromImpEmployee(getCtx(), bankacctB, bankacctN, impEmployee, get_TrxName());
						// Bank Account CR ("B");
						try {
							if (bankacctB.save()) {
								msglog = new StringBuilder("Insert Bank Account - ").append(emp.getAMN_Employee_ID());
								if (log.isLoggable(Level.FINEST)) log.finest(msglog.toString());
								okBKA=true;
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
							if (bankacctN.save()) {
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
					} else {
						okBKA=true;
					}
				}
				
				// 
				// -------------------------------------------------------------------
				// ****	Final Commit
				// -------------------------------------------------------------------
				
				
				Old_EmployeeValue = New_EmployeeValue ;
				if (okBP1 && okBP2 && okEMP && okUSR && okADD && okBKA) {
					impEmployee.setI_IsImported(true);
					impEmployee.setProcessed(true);
					impEmployee.setProcessing(false);
				}
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
			sql = new StringBuilder ("UPDATE AMN_I_Employee ")
					.append("SET I_IsImported='N', Updated=getDate() ")
					.append("WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdateEx(sql.toString(), get_TrxName());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@AMN_Employee_ID@: @Inserted@");
			addLog (0, null, new BigDecimal (noUpdate), "@AMN_Employee_ID@: @Updated@");
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
	
	/**
	 * setBillBPartnerFromImpEmployee
	 * @param ctx
	 * @param bp
	 * @param impEmployee
	 * @param trxName
	 */
	private void setBillBPartnerFromImpEmployee(Properties ctx, MBPartner bp, MAMN_I_Employee impEmployee, String trxName) {
		
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
     * getBankIDByName
     * @param ctx
     * @param bankName
     * @param AD_Client_ID
     * @param trxName
     * @return
     */
    private static int getBankIDByName(Properties ctx, String bankName, int AD_Client_ID, String trxName) {
        if (bankName == null || bankName.trim().isEmpty()) {
            return 0; // Devuelve 0 si el nombre del banco es nulo o vacío
        }

        // Consulta SQL para obtener el ID del banco basado en el nombre
        String sql = "SELECT C_Bank_ID FROM C_Bank WHERE Name = ? AND AD_Client_ID = ?";

        // Ejecuta la consulta y obtiene el ID del banco
        int cBankID = DB.getSQLValue(trxName, sql, bankName, AD_Client_ID);

        // Si no se encuentra, DB.getSQLValue devuelve -1
        return cBankID > 0 ? cBankID : 0;
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
        bploc.setIsBillTo(true);
        bploc.setIsPayFrom(false);
        bploc.setIsRemitTo(true);
        bploc.setIsShipTo(true);
        // C_Location table
        loc.setC_Country_ID(impEmployee.getC_Country_ID());
        loc.setC_Region_ID(impEmployee.getC_Region_ID());
        loc.setAddress1(impEmployee.getAddress1());
        loc.setAddress2(impEmployee.getAddress2());
        loc.setC_City_ID(impEmployee.getC_City_ID());
        
    }

    /**
     * setBPLocationFromAMN_Location
     * Set BPartner Location from Employee AMN_Location
     * @param ctx
     * @param bploc2
     * @param loc2
     * @param impEmployee
     * @param emploc
     * @param trxName
     */
    private void setBPLocationFromAMN_Location(Properties ctx, MBPartnerLocation bploc2,  MLocation loc2, MAMN_I_Employee impEmployee, MAMN_Location emploc, String trxName) {
		
    	// C_BPartnerLocation table
    	bploc2.setName(impEmployee.getValue());
        bploc2.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
        
        bploc2.setC_Location_ID(loc2.getC_Location_ID());
        bploc2.setName(impEmployee.getValue()+"-"+emploc.getValue());
        bploc2.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
        // 
        bploc2.setIsBillTo(false);
        bploc2.setIsPayFrom(true);
        bploc2.setIsRemitTo(false);
        bploc2.setIsShipTo(false);
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
    private void setBPBankAccountsFromImpEmployee(Properties ctx, MBPBankAccount bankacctB,  MBPBankAccount bankacctN, MAMN_I_Employee impEmployee, String trxName) {
    	
    	
    	// Bank Account CR Use (B)
    	String bankname;
    	int C_Bank_ID =getBankIDByName(ctx, impEmployee.getBankName_B(), m_AD_Client_ID, get_TrxName());
    	bankacctB.setC_Bank_ID(C_Bank_ID);
    	bankacctB.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
    	bankacctB.setAccountNo(impEmployee.getAccountNo_B());
    	bankacctB.setBPBankAcctUse("B");
    	bankname = getTranslatedBPBankAcctUse(ctx, m_AD_Client_ID, "B");
    	bankacctB.setA_Name(bankname);
    	
    	// Bank Account DB Use (N)
    	C_Bank_ID =getBankIDByName(ctx, impEmployee.getBankName_N(), m_AD_Client_ID, get_TrxName());
    	bankacctN.setC_Bank_ID(C_Bank_ID);
    	bankacctN.setC_BPartner_ID(impEmployee.getC_BPartner_ID());
    	bankacctN.setAccountNo(impEmployee.getAccountNo_N());
    	bankacctN.setBPBankAcctUse("N");
    	bankname = getTranslatedBPBankAcctUse(ctx, m_AD_Client_ID, "N");
    	bankacctN.setA_Name(bankname);
    }
    
    /**
     * Obtiene la traducción de un valor de la referencia BPBankAcctUse.
     *
     * @param ctx   El contexto de la aplicación.
     * @param value El valor del elemento de la lista (por ejemplo, "B" para Débito, "C" para Crédito).
     * @return La traducción del valor según el idioma del contexto, o el valor original si no se encuentra traducción.
     */
    public static String getTranslatedBPBankAcctUse(Properties ctx,  int AD_Client_ID, String value) {
        if (value == null || value.trim().isEmpty()) {
            return value; // Devuelve el valor original si es nulo o vacío
        }

        String translatedValue = value;
        String sql = "SELECT rlt.Name "
                   + "FROM AD_Ref_List rl "
                   + "INNER JOIN AD_Ref_List_trl rlt ON rlt.AD_Ref_List_id = rl.AD_Ref_List_ID "
                   + "JOIN AD_Reference r ON rl.AD_Reference_ID = r.AD_Reference_ID "
                   + "WHERE r.Name = 'AMN_BPBankAcctUse' AND rl.Value = '"+value+"'"
                   + "AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID= "+AD_Client_ID+") ";

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null);
            ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                translatedValue = rs.getString("Name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return translatedValue;
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

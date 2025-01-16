package org.amerp.process.imp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.adempiere.model.ImportValidator;
import org.amerp.amnmodel.I_AMN_I_Employee;
import org.amerp.amnmodel.I_AMN_I_Employee_Salary;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.X_AMN_I_Employee;
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

			while (rs.next())
			{	
				// Remember Value - only first occurance of the value is Employee
				String New_EmployeeValue = rs.getString("Value") ;

				X_AMN_I_Employee impEmployee = new X_AMN_I_Employee (getCtx(), rs, get_TrxName());
				StringBuilder msglog = new StringBuilder("AMN_I_Employee_ID=") .append(impEmployee.getAMN_I_Employee_ID())
						.append(", AMN_Employee_ID=").append(impEmployee.getAMN_I_Employee_ID());
				if (log.isLoggable(Level.FINE)) log.fine(msglog.toString());

				// -------------------------------------------------------------------
				// AMN_Employee 
				// -------------------------------------------------------------------
				
				if ( ! New_EmployeeValue.equals(Old_EmployeeValue)) {
					//	****	Create/Update Employee	****
					emp = null;

					if (impEmployee.getAMN_Employee_ID() == 0)	//	Insert new Employee
					{
						emp = new MAMN_Employee(impEmployee);
						
					}
					else				//	Update existing BPartner
					{
	
					}

				}
				
				Old_EmployeeValue = New_EmployeeValue ;

				
				// -------------------------------------------------------------------
				// ****	Create/Update Contact	****
				// -------------------------------------------------------------------
				MUser defaultUser = null;
				// Get Default User for Employee.
				
				// 
				if (defaultUser == null)
				{
					

				}
				
				// -------------------------------------------------------------------
				// ****	Create/Update Location	****
				// -------------------------------------------------------------------
				
				MLocation defaultLocation = null;
				
				//	****	Create/Update Employee Location	****
				if(defaultLocation == null) {
					MUser user = null;
	
				}
				
				// -------------------------------------------------------------------
				// ****	Create/Update BankAccount	****
				// -------------------------------------------------------------------
			
				// Get Default Accounts for Employee.
				
				// 
				if (defaultUser == null)
				{
					

				}
				// -------------------------------------------------------------------
				// ****	Final Commit
				// -------------------------------------------------------------------
				
				impEmployee.setI_IsImported(true);
				impEmployee.setProcessed(true);
				impEmployee.setProcessing(false);
				impEmployee.saveEx();
				commitEx();
				DB.close(rs, pstmt);
			}
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
}

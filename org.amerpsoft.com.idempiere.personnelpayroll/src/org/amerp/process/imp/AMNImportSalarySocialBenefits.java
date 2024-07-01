package org.amerp.process.imp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.adempiere.model.ImportValidator;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.session.SessionManager;
import org.amerp.amnmodel.I_AMN_I_Employee_Salary;
import org.amerp.amnmodel.MAMN_Employee_Salary;
import org.amerp.amnmodel.MAMN_I_Employee_Salary;
import org.compiere.model.ModelValidationEngine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
/**
 * AMNImportSalarySocialBenefits
 * Import all records from MAN_I_Employee_Salary Table
 * @author luisamesty
 *
 */
public class AMNImportSalarySocialBenefits extends SvrProcess {
	
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
		// TODO Auto-generated method stub
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
		sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary ")
				.append("SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),")
						.append(" AD_Org_ID = COALESCE (AD_Org_ID, 0),")
						.append(" IsActive = COALESCE (IsActive, 'Y'),")
						.append(" Created = COALESCE (Created, SysDate),")
						.append(" CreatedBy = COALESCE (CreatedBy, 0),")
						.append(" Updated = COALESCE (Updated, SysDate),")
						.append(" UpdatedBy = COALESCE (UpdatedBy, 0),")
						.append(" I_ErrorMsg = ' ',")
						.append(" I_IsImported = 'N' ")
						.append("WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Reset=" + no);

		//	Existing Employee ?
		sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary i ")
				.append("SET AMN_EMPLOYEE_ID=")
				.append("(SELECT AMN_EMPLOYEE_ID FROM AMN_EMPLOYEE u ")
				.append("WHERE i.Value=u.Value AND u.AD_Client_ID=i.AD_Client_ID) ");
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Found Employees =" + no);
		
		//	Existing AMN_Concept_Types_Proc
		sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary i ")
				.append("SET AMN_Concept_Types_Proc_ID=")
				.append("(SELECT AMN_Concept_Types_Proc_ID FROM AMN_Concept_Types_Proc u ")
				.append("WHERE i.ConceptValue=u.Value AND u.AD_Client_ID=i.AD_Client_ID) ");
		no = DB.executeUpdateEx(sql.toString(), get_TrxName());
		if (log.isLoggable(Level.FINE)) log.fine("Found AMN_Concept_Types_Proc =" + no);
		commitEx();
		if (p_IsValidateOnly)
		{
			return "Validated";
		}
		// -------------------------------------------------------------------
		// -----------------------------------------
		// Import Process
		// -----------------------------------------
		int noInsert = 0;
		int noUpdate = 0;
		no = 0;
		//	Go through Records
		sql = new StringBuilder ("SELECT * FROM AMN_I_Employee_Salary ")
				.append("WHERE I_IsImported='N'").append(clientCheck);
		sql.append(" ORDER BY AMN_Employee_ID, ValidTo ");
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			rs = pstmt.executeQuery();

			String errorMsg = "" ; 
			MAMN_Employee_Salary amnes = null;
			Boolean OKwrite=false;
			while (rs.next())
			{	
				MAMN_I_Employee_Salary impES = new MAMN_I_Employee_Salary (getCtx(), rs, get_TrxName());
				// log.warning("AMN_I_Employee_Salary_ID="+impES.getAMN_I_Employee_Salary_ID());
				amnes =MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollDate(impES.getC_Currency_ID(), impES.getAMN_Employee_ID(), impES.getValidFrom(), impES.getAMN_Concept_Types_Proc_ID());
				// UPDATE 
				if (amnes != null ) {
					// Verify if Initial value for Social benefits
					if (amnes.getAMN_Concept_Types_Proc_ID() != 0	&&					
						amnes.getAMN_Concept_Types_Proc_ID()== impES.getAMN_Concept_Types_Proc_ID()) {
						errorMsg="OK ** UPDATED ** "+ Msg.getElement(getCtx(), "AMN_Concept_Types_Proc_ID()");
						OKwrite=true;	
						noUpdate=noUpdate+1;
					} else {
						OKwrite=false;
						errorMsg="**** ERROR **** "+ Msg.getElement(getCtx(), "AMN_Concept_Types_Proc_ID()");
					}
				} else {
				// NEW 
					amnes = new MAMN_Employee_Salary(getCtx(),0,get_TrxName());
					OKwrite=true;
					noInsert=noInsert+1;
					errorMsg="OK ** INSERTED ** "+ Msg.getElement(getCtx(), "AMN_Concept_Types_Proc_ID()");
				}
				// OKwrite ??
				if (OKwrite) {
					amnes.setAD_Org_ID(impES.getAD_Org_ID());
					amnes.setAMN_Employee_ID(impES.getAMN_Employee_ID());
					amnes.setAMN_Concept_Types_Proc_ID(impES.getAMN_Concept_Types_Proc_ID());
					amnes.setC_Currency_ID(impES.getC_Currency_ID());
					amnes.setAMN_Payroll_ID(impES.getAMN_Payroll_ID());
					amnes.setDescription(impES.getDescription());
					// Prestacion Salary
					amnes.setSalary(impES.getSalary());
					amnes.setSalary_Day(impES.getSalary_Day());
					amnes.setVacationDays(impES.getVacationDays());
					amnes.setUtilityDays(impES.getUtilityDays());
					amnes.setPrestacionDays(impES.getPrestacionDays());
					// Prestacion Amount Values to 0
					amnes.setPrestacionAmount(impES.getPrestacionAmount());
					amnes.setPrestacionAdjustment(impES.getPrestacionInterestAdjustment());
					amnes.setPrestacionAdvance(impES.getPrestacionAdvance());
					amnes.setPrestacionAccumulated(BDZero);
					// Prestacion Interes Values to 0
					amnes.setPrestacionInterest(impES.getPrestacionInterest());
					amnes.setPrestacionInterestAdjustment(impES.getPrestacionInterestAdjustment());
					amnes.setPrestacionInterestAdvance(impES.getPrestacionInterestAdvance());
					// Interes Rates
					amnes.setactive_int_rate(BDZero);
					amnes.setactive_int_rate2(BDZero);
					// Dates
					amnes.setValidFrom(impES.getValidFrom());			
					amnes.setValidTo(impES.getValidTo());			
					// SAVES NEW
					if(amnes.save() ) {
						sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary i ")
						.append(" SET I_IsImported='Y', I_ErrorMsg='")
						.append(errorMsg).append("' ")
						.append(" WHERE AMN_I_Employee_Salary_ID=").append(impES.getAMN_I_Employee_Salary_ID());
					} else {
						errorMsg=errorMsg+"  BUT ** NOT INSERTED **";
						sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary i ")
						.append(" SET I_IsImported='E', I_ErrorMsg='")
						.append(errorMsg).append("' ")
						.append("WHERE AMN_I_Employee_Salary_ID=").append(impES.getAMN_I_Employee_Salary_ID());
					}
					DB.executeUpdateEx(sql.toString(), get_TrxName());
				} else {
					sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary i ")
					.append(" SET I_IsImported='E', I_ErrorMsg='")
					.append(errorMsg).append("' ")
					.append("WHERE AMN_I_Employee_Salary_ID=").append(impES.getAMN_I_Employee_Salary_ID());				
				}
				commitEx();
//				commitEx();
			}	//	rollback();
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
			sql = new StringBuilder ("UPDATE AMN_I_Employee_Salary ")
					.append("SET I_IsImported='N', Updated=SysDate ")
					.append("WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdateEx(sql.toString(), get_TrxName());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), Msg.getElement(getCtx(), "AMN_Employee_Salary")+": @Inserted@");
			addLog (0, null, new BigDecimal (noUpdate), Msg.getElement(getCtx(), "AMN_Employee_Salary")+": @Updated@");
		}
		
		//
		return null;
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
		return I_AMN_I_Employee_Salary.Table_Name;
		//return "AMN_I_Employee_Salary";
	}
	

}

/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
  *****************************************************************************/
package org.amerp.process;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Payroll;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * AMNPayrollCreateOneLot
 * Description: Procedure called from iDempiere AD
 * 			Create Payroll Receipts
 * 			For One Fiscal Period on all Contracts and Selected Process
 * 			For All Active Employees including Vacation.
 * Result:	Create all PayrollReceipt according to Payroll Concepts Rules
 * 			Create Header on AMN_Payroll and Lines on AMN_Payroll_detail
 * 			Using AMNPayrollCreateDocs Class
 * @author luisamesty
 *
 */
public class AMNPayrollCreateOneLotHeaders extends SvrProcess{

	static CLogger log = CLogger.getCLogger(AMNPayrollCreateOneLotHeaders.class);
	// Parameter Vars
	private int p_AMN_Process_ID = 0;
	private int p_C_Period_ID = 0;
	private int p_AMN_Payroll_Lot_ID = 0;
	static Timestamp p_DateAcct = null;
	static Timestamp p_InvDateEnd = null; 
	static Timestamp p_InvDateIni = null;
	static Timestamp p_RefDateEnd = null;
	static Timestamp p_RefDateIni = null;
	private int p_AMN_Concept_Types_Proc_ID=0;
	private BigDecimal p_LoanAmount = BigDecimal.valueOf(0);
	private int p_LoanQuotaNo = 0;
	private int p_AMN_FirstPeriod_ID = 0;
	private String p_LoanDescription="Descripción Pago Diferido";
	@Override
	protected void prepare() {
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Period_ID"))
				p_C_Period_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Payroll_Lot_ID"))
				p_AMN_Payroll_Lot_ID = para.getParameterAsInt();
			else if (paraName.equals("DateAcct"))
				p_DateAcct = para.getParameterAsTimestamp();
			else if (paraName.equals("LoanAmount"))
				p_LoanAmount = para.getParameterAsBigDecimal();
			else if (paraName.equals("LoanQuotaNo"))
				p_LoanQuotaNo = para.getParameterAsInt();
			else if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Concept_Types_Proc_ID"))
				p_AMN_Concept_Types_Proc_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_FirstPeriod_ID"))
				p_AMN_FirstPeriod_ID = para.getParameterAsInt();
			else if (paraName.equals("LoanDescription"))
				p_LoanDescription = para.getParameterAsString();
			else if (paraName.equals("DateAcct"))
				p_DateAcct = para.getParameterAsTimestamp();
			else if (paraName.equals("InvDateIni"))
				p_InvDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("InvDateEnd"))
				p_InvDateEnd = para.getParameterAsTimestamp();
			else if (paraName.equals("RefDateIni"))
				p_RefDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("RefDateEnd"))
				p_RefDateEnd = para.getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  p_C_Period_ID:"+p_C_Period_ID+"  p_AMN_Payroll_Lot_ID:"+p_AMN_Payroll_Lot_ID);
	}

	@Override
	protected String doIt() throws Exception {
	   	String Msg_Header="";
	    String Msg_Verified="";
	    Boolean isError = false;
	    String Msg_Updates="";
	    int AMN_Employee_ID=0;
	    int AMN_Contract_ID = 0;
	    int AMN_Period_ID = 0;
	    int AMN_Payroll_ID=0;
	    MAMN_Payroll amnpayrollCK = null;
		// Other Vars
		String Employee_Name;
		String AMN_Process_Value="";
		String Employee_Value="";
		String Employee_Status="";
		String Contract_Name="";
		String sql="";
		String sql1="";
		
    	// ARRAY EMPLOYEE - PROCESS - PERIOD
		sql = "SELECT " + 
			" amnemp.amn_employee_id , " + 
			" amnemp.name as employee_name,  " +  
			" amnemp.value as employee_value,  " + 
			" amnemp.status,  " + 
			" amncon.amn_contract_id,   " + 
			" amncon.name as contract_name, " + 
			" amnpro.amn_process_id, " + 
			" amnpro.amn_process_value, " + 
			" COALESCE(amnper.amn_period_id,0) as amn_period_id,  " + 
			" COALESCE(amnper.name,'') as period_name " + 
			" FROM  amn_employee as amnemp  " + 
			" LEFT JOIN amn_contract amncon on (amnemp.amn_contract_id = amncon.amn_contract_id )  " + 
			" LEFT JOIN (SELECT ad_client_id, amn_process_id, value, name, amn_process_value FROM amn_process WHERE amn_process_id=" + p_AMN_Process_ID + " )  " + 
			" 	amnpro on (amnpro.ad_client_id = amnemp.ad_client_id) " + 
			" LEFT JOIN (SELECT * FROM amn_period WHERE c_period_id = " + p_C_Period_ID + " AND amn_process_id=" + p_AMN_Process_ID + ") " + 
			" 	amnper on (amnper.amn_contract_id = amnemp.amn_contract_id) " + 
			" WHERE amnemp.isactive='Y' AND amnemp.status IN ('A','V') " + 
			" AND amncon.isallowlotinvoices='Y' " +
			" ORDER BY employee_value " ;
	    // SQL Sentence
		// log.warning(sql);
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		// Verify Docs Headers 
		// **********************
		// Verify Headers
		// **********************
	    Msg_Verified = Msg.getElement(getCtx(), "AMN_Process_ID")+":("+AMN_Process_Value+")   ** Verify **";
	    addLog(Msg_Verified);		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rspc = pstmt.executeQuery();
			while (rspc.next())
			{
				AMN_Employee_ID = rspc.getInt(1);
				Employee_Name   = rspc.getString(2).trim();
				Employee_Value  = rspc.getString(3).trim();
				Employee_Status = rspc.getString(4).trim();
				AMN_Contract_ID = rspc.getInt(5);
				Contract_Name   = rspc.getString(6).trim();
				AMN_Period_ID   = rspc.getInt(9);
				//log.warning("Verifying Employee="+Employee_Name+Employee_Value);
				// Verify Period Created fro Process and Contract
				if (AMN_Period_ID == 0) {
					addLog(Msg.getElement(getCtx(), "AMN_Employee_ID")+":"+Employee_Value+"-"+Employee_Name.trim()+" Estado:"+Employee_Status);
					Msg_Verified = Msg.getElement(getCtx(), "AMN_Contract_ID")+":"+Contract_Name.trim()+" "+
							Msg.translate(getCtx(), "FindZeroRecords");
				    addLog(Msg_Verified);
				    isError = true;
				}
				// Verify if AMN_Payroll_ID is created and POSTED
				amnpayrollCK = MAMN_Payroll.findByAMNPayroll(getCtx(), Env.getLanguage(getCtx()).getLocale(), 
						p_AMN_Process_ID, AMN_Contract_ID, AMN_Period_ID, AMN_Employee_ID);
				if (amnpayrollCK != null && amnpayrollCK.isPosted()) {
					addLog(Msg.getElement(getCtx(), "AMN_Employee_ID")+":"+Employee_Value+"-"+Employee_Name.trim()+" Estado:"+Employee_Status);
					Msg_Verified = Msg.getElement(getCtx(), "AMN_Payroll_ID")+":"+amnpayrollCK.getDocumentNo().trim()+" "+
							amnpayrollCK.getValue().trim()+" "+amnpayrollCK.getName().trim();
				    addLog(Msg_Verified);
					Msg_Verified = Msg.translate(getCtx(), "Error")+Msg.translate(getCtx(), "document.status.completed")+" ("+amnpayrollCK.getDocStatus()+") ";
				    addLog(Msg_Verified);
				    isError = true;
				}
				//
			}				
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Create Docs Headers 
		// if no errors
		if (!isError) {
			pstmt = null;
			rspc = null;		
			// **********************
			// Document Headers
			// **********************
		    Msg_Header = Msg.getElement(getCtx(), "AMN_Process_ID")+":("+AMN_Process_Value+")   ** Create Receipts **";
		    addLog(Msg_Header);		
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				rspc = pstmt.executeQuery();
				while (rspc.next())
				{
					AMN_Employee_ID = rspc.getInt(1);
					Employee_Name   = rspc.getString(2).trim();
					Employee_Value  = rspc.getString(3).trim();
					Employee_Status = rspc.getString(4).trim();
					AMN_Contract_ID = rspc.getInt(5);
					AMN_Period_ID   = rspc.getInt(9);
					//log.warning("Creating Employee="+Employee_Name+Employee_Value);
				    // Verify if AMN_Payroll_ID is created and POSTED
					amnpayrollCK = MAMN_Payroll.findByAMNPayroll(getCtx(), Env.getLanguage(getCtx()).getLocale(), 
							p_AMN_Process_ID, AMN_Contract_ID, AMN_Period_ID, AMN_Employee_ID);
					if (amnpayrollCK != null ) {
						if (amnpayrollCK.isPosted()) {
							Msg_Header = Msg.getElement(getCtx(), "AMN_Payroll_ID")+":"+amnpayrollCK.getDocumentNo().trim()+" "+
									amnpayrollCK.getValue().trim()+" "+amnpayrollCK.getName().trim();
						    addLog(Msg_Header);
							Msg_Header = Msg.translate(getCtx(), "Error")+Msg.translate(getCtx(), "document.status.completed")+" ("+amnpayrollCK.getDocStatus()+") ";
						    addLog(Msg_Header);
						} else {
					    	// Document Header
							addLog(Msg.getElement(getCtx(), "AMN_Employee_ID")+":"+Employee_Value+"-"+Employee_Name);
							AMN_Payroll_ID=  AMNPayrollCreateDocs.CreatePayrollOneDocument(getCtx(), p_AMN_Process_ID, AMN_Contract_ID, AMN_Period_ID, p_AMN_Payroll_Lot_ID, AMN_Employee_ID, amnpayrollCK.getAMN_Payroll_ID(), p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, get_TrxName());
							Msg_Header= AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(getCtx(), p_AMN_Process_ID, AMN_Contract_ID, AMN_Period_ID, p_AMN_Payroll_Lot_ID, AMN_Employee_ID, amnpayrollCK.getAMN_Payroll_ID(), p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd,  get_TrxName());
							addLog(Msg_Header);
						}
					} else {
						addLog(Msg.getElement(getCtx(), "AMN_Employee_ID")+":"+Employee_Value+"-"+Employee_Name);
						Msg_Header = Msg.getElement(getCtx(), "AMN_Payroll_ID")+":"+Msg.translate(getCtx(), "invalid")+" "+
								amnpayrollCK.getValue().trim()+" "+amnpayrollCK.getName().trim();
					    addLog(Msg_Header);				
					}
				}				
			}
		    catch (SQLException e)
		    {
		    }
			finally
			{
				DB.close(rspc, pstmt);
				rspc = null; pstmt = null;
			}
		}
		return Msg_Updates;
	}

}

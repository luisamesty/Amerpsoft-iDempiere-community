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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.ScriptException;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.amerp.amnutilities.AmerpPayrollCalc.scriptResult;
import org.compiere.model.MSysConfig;
import org.compiere.util.*;

/* Description: Generic Class AMNPayrollCreateDocs
 * Called from: AMNPayrollCreate (Creates Receipts for Period)
 * 				AMNPayrollCreateOneDoc (Creates One Receipt)
 * 
 * Use for Both Single and Multiple Documents:
 * CreatePayrollOneDocument
 * CreatePayrollOneDocumentLines
 * CreatePayrollOneDocDetailLines
 * CalculateOnePayrollDocument
 * 
 * @author luisamesty
 *
 */
public class AMNPayrollCreateDocs {
	
	// PUBLIC VARS
	static String Msg_Value="";
	static CLogger log = CLogger.getCLogger(AMNPayrollCreateDocs.class);


	/**
	 * CreatePayrollOneDocument
	 * Description: Creates One Record on Table AMN_PAyroll  
	 * @param int p_AMN_Process_ID
	 * @param int p_AMN_Contract_ID
	 * @param int p_AMN_Period_ID
	 * @param int p_AMN_Payroll_Lot_ID
	 * @param int p_AMN_Employee_ID
	 * @param int p_AMN_Payroll_ID
	 * @return AMN_Payroll_ID
	 */
	public static int CreatePayrollOneDocument (Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, 
			Timestamp p_DateAcct, Timestamp p_InvDateIni, Timestamp p_InvDateEnd, Timestamp p_RefDateIni, Timestamp p_RefDateEnd,
			String trxName) {
	    String AMN_Process_Value="NN";
	    Msg_Value="";
		// Determines Process Value to see if NN
		MAMN_Process amnprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
		AMN_Process_Value = amnprocess.getAMN_Process_Value();
		MAMN_Period amnperiod = new MAMN_Period(ctx,p_AMN_Period_ID,null);
		MAMN_Payroll amnpayroll = null;
		//  CREATE MAMN_Payroll (DOCUMENT HEADER)
		if (p_AMN_Payroll_ID == 0) {
			amnpayroll = new MAMN_Payroll(ctx, 0, null);
		} else {
			amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		}
		amnpayroll.createAmnPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
				amnperiod.getAD_Client_ID(), amnperiod.getAD_Org_ID(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,
				p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, 
				p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd,
				trxName);	
		// Return Messages
		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
				AMN_Process_Value.equalsIgnoreCase("TI") ) {
			// ************************
			// Process NNN an TI	
			// ************************		
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// ************************
			// Process NV	
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// ************************
			// Process NP
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
			// ************************
			// Process NU
			// ************************
		} else {
			Msg_Value=Msg_Value+(Msg.getMsg(ctx, "Process")+":"+AMN_Process_Value.trim()+"NotAvailable"+" \n");
		}
		return amnpayroll.getAMN_Payroll_ID();
	}

//	/**
//	 * UpdatePayrollOneDocument 
//	 * 
//	 * @param ctx
//	 * @param p_AMN_Process_ID
//	 * @param p_AMN_Contract_ID
//	 * @param p_AMN_Period_ID
//	 * @param p_AMN_Payroll_Lot_ID
//	 * @param p_AMN_Employee_ID
//	 * @param p_AMN_Payroll_ID
//	 * @param p_DateAcct
//	 * @param p_InvDateEnd
//	 * @param p_InvDateIni
//	 * @param p_RefDateEnd
//	 * @param p_RefDateIni
//	 * @param trxName
//	 * @return
//	 */
//	public static int UpdatePayrollOneDocument (Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
//			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, 
//			Timestamp p_DateAcct, Timestamp p_InvDateIni, Timestamp p_InvDateEnd, Timestamp p_RefDateIni, Timestamp p_RefDateEnd,
//			String trxName) {
//	    String AMN_Process_Value="NN";
//	    Msg_Value="";
//		// Determines Process Value to see if NN
//		MAMN_Process amnprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
//		AMN_Process_Value = amnprocess.getAMN_Process_Value();
//		MAMN_Period amnperiod = new MAMN_Period(ctx,p_AMN_Period_ID,null);
//		// UPDATE 
//		MAMN_Payroll amnpayroll = null;
//		//  CREATE MAMN_Payroll (DOCUMENT HEADER)
//		if (p_AMN_Payroll_ID > 0) {
//			amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
//			amnpayroll.updateAmnPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
//					amnperiod.getAD_Client_ID(), amnperiod.getAD_Org_ID(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,
//					p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, 
//					p_DateAcct, p_InvDateEnd, p_InvDateIni, p_RefDateEnd, p_RefDateIni, trxName);	
//			// Return Messages
//			if (AMN_Process_Value.equalsIgnoreCase("NN") ||
//					AMN_Process_Value.equalsIgnoreCase("TI") ) {
//				// ************************
//				// Process NNN an TI	
//				// ************************		
//				
//			} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
//				// ************************
//				// Process NV	
//				// ************************
//				
//			} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
//				// ************************
//				// Process NP
//				// ************************
//				
//			} else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
//				// ************************
//				// Process NU
//				// ************************
//			} else {
//				Msg_Value=Msg_Value+(Msg.getMsg(ctx, "Process")+":"+AMN_Process_Value.trim()+"NotAvailable"+" \n");
//			}
//		}
//		return amnpayroll.getAMN_Payroll_ID();
//	}

	/**
	 * CreatePayrollOneDocumentLines
	 * Description: Creates Records on Table AMN_PAyroll 
	 * @param int p_AMN_Process_ID
	 * @param int p_AMN_Contract_ID
	 * @param int p_AMN_Period_ID
	 * @param int p_AMN_Employee_ID
	 * @param int p_AMN_Payroll_ID
	 * @return Msg_Value
	 */
	public static String CreatePayrollOneDocumentLines (Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, 
			Timestamp p_DateAcct, Timestamp p_InvDateIni, Timestamp p_InvDateEnd, Timestamp p_RefDateIni, Timestamp p_RefDateEnd,
			String trxName) {
	    String AMN_Process_Value="NN";
	    Msg_Value="";
		// MAMN_Contract
		MAMN_Contract amncontract = new MAMN_Contract(ctx,p_AMN_Contract_ID,null);		
		// Determines Process Value to see if NN
		MAMN_Process amnprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
		AMN_Process_Value = amnprocess.getAMN_Process_Value();
		//sql = "SELECT amn_process_value FROM amn_process WHERE amn_process_id=?" ;
		//AMN_Process_Value = DB.getSQLValueString(null, sql, p_AMN_Process_ID).trim();
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, 0, null);
		// Verify if p_AMN_Payroll_ID in parameter
		if (p_AMN_Payroll_ID == 0) {
			//  CREATE MAMN_Payroll (DOCUMENT HEADER)
			amnpayroll.createAmnPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
					amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,
					p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, 
					p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd,
					trxName);
			// GET AMN_Payroll_ID FROM DOCUMENT CREATED
			amnpayroll = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
					p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
			
		} else {
			amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		}
		// Messsage
		Msg_Value=Msg_Value+Msg.getElement(ctx, "AMN_Employee_ID")+":"+amnpayroll.getName().trim()+" \r\n";
		// CREATE MAMN_Payroll_Detail (DOCUMENT LINES)
		CreatePayrollOneDocDetailLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, amnpayroll.getAMN_Payroll_ID(), trxName);
		// LOANS 		
		// VERIFY AMN_Payroll_Deferred and Create MAMN_Payroll_Detail (DEFERRED DOCUMENT LINES)
		CreatePayrollOneDocDetailDeferredLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, amnpayroll.getAMN_Payroll_ID(), trxName);
		//
		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
				AMN_Process_Value.equalsIgnoreCase("TI") ) {
			// ************************
			// Process NNN an TI	
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// ************************
			// Process NV	
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// ************************
			// Process NP
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
			// ************************
			// Process NU
			// ************************
		} else {
			Msg_Value=Msg_Value+(Msg.getMsg(ctx, "Process")+":"+AMN_Process_Value.trim()+"NotAvailable"+" \r\n");
		}
		return Msg_Value;
	}

	/**
	 * CreatePayrollOneDocDetailLines
	 * Description: Creates Records on Table AMN_PAyroll_Detail 
	 * @param int p_AMN_Process_ID,
	 * @param int p_AMN_Contract_ID
	 * @param int p_AMN_Payroll_ID
	 * return String Msg_Value
	 */
	public static String CreatePayrollOneDocDetailLines 
		(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			 int p_AMN_Payroll_ID, String trxName ) {
		
		// Receipt Lines List
		AMNReceiptLines ReceiptLines = null;
		List<AMNReceiptLines> ReceiptConcepts = new ArrayList<AMNReceiptLines>();
		BigDecimal Concept_DefaultValue = BigDecimal.ZERO;
		// SQL
	    String sql="";
	    MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(ctx, 0, null);
		// MAMN_Contract
		MAMN_Contract amncontract = new MAMN_Contract(ctx,p_AMN_Contract_ID,null);		
		new MAMN_Process(ctx, p_AMN_Process_ID, null);
		//
		//BigDecimal PayrolldaysC from AMN_Contract
		BigDecimal PayrolldaysC = amncontract.getPayRollDays();
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		// GET Employee_ID
    	int AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, AMN_Employee_ID, null);
		// GET Salary from AMN_Employee
		//BigDecimal Salary= MAMN_Employee.sqlGetAMNSalary(AMN_Employee_ID);
		BigDecimal Salary=amnemployee.getSalary();
		// GET Payrolldays from AMN_Payroll
		//log.warning(" MAMN_Payroll_Detail---p_AMN_Payroll_ID="+p_AMN_Payroll_ID+"  fechas INI="+amnpayroll.getInvDateIni()+"  End="+amnpayroll.getInvDateEnd());	
		// deprecated BigDecimal Payrolldays = MAMN_Payroll.sqlGetAMNPayrollDays(p_AMN_Payroll_ID);    
		BigDecimal Payrolldays = amnpayroll.getAMNPayrollDays(p_AMN_Payroll_ID ); 
		if (PayrolldaysC.equals(Payrolldays) ) {
			Payrolldays = PayrolldaysC;
		}		
		//
		scriptResult RetVal = new scriptResult();
		// Rules only on PL
		boolean forceRulesInit=false;
		boolean forceDVInit=true;
		int lin = 0;
       	// MSysConfig AMERP_Payroll_Rules_Apply 
		String apra = MSysConfig.getValue("AMERP_Payroll_Rules_Apply","N",amncontract.getAD_Client_ID());
		if (apra.compareToIgnoreCase("Y")==0)
			forceRulesInit=true;
		else
			forceRulesInit=false;
		//
		sql ="SELECT  " + 
			"cty.calcorder,  " + 
			"cty.amn_concept_types_id, " + 
			"ctp.amn_process_id, " + 
			"ctp.amn_concept_types_proc_id,  " + 
			"cty.defaultvalue, " + 
			"cty.scriptdefaultvalue, " + 
			"cty.value, " + 
			"cty.name, " + 
			"cty.description, " + 
			"cty.AMN_Concept_Uom_ID "+
			"FROM amn_concept_types as cty " + 
			"LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id = cty.amn_concept_types_id) " + 
			"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) " + 
			"LEFT JOIN amn_concept_types_contract as ctc ON (ctc.amn_concept_types_id = cty.amn_concept_types_id) " +
			"WHERE cty.rule = 'F' AND ctp.amn_process_id = ? " + 
			" AND ctc.amn_contract_id =? "+
			" AND cty.isactive ='Y' "+
			" AND ctp.isactive ='Y' "+
			" AND ctc.isactive ='Y' "+			
			"ORDER BY cty.calcorder" 
			;
		//log.warning("sql="+sql);
		//log.warning("p_AMN_Process_ID="+p_AMN_Process_ID+"   p_AMN_Contract_ID="+p_AMN_Contract_ID+"  AMN_Payroll_ID="+p_AMN_Payroll_ID);
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		// ----------------
		// FILL ARRAY
		// ----------------
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Process_ID);
            pstmt1.setInt (2, p_AMN_Contract_ID);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				lin = +1;
				// log.warning("  AMN_Payroll_ID="+p_AMN_Payroll_ID+"  AMN_Concept_Types_Proc_ID="+AMN_Concept_Types_Proc_ID);
				// CREATE MAMN_Payroll Detail
				ReceiptLines = new AMNReceiptLines();
				ReceiptLines.setCalcOrder(rsod1.getInt(1));
				ReceiptLines.setAMN_Concept_Type_ID(rsod1.getInt(2));
				ReceiptLines.setAMN_Process_ID(rsod1.getInt(3));
				ReceiptLines.setAMN_Concept_Type_Proc_ID(rsod1.getInt(4));
				ReceiptLines.setDefaultValue(rsod1.getString(5));
				ReceiptLines.setScriptDefaultValue(rsod1.getString(6));
				ReceiptLines.setConceptValue(rsod1.getString(7));
				ReceiptLines.setConceptName(rsod1.getString(8));
				ReceiptLines.setConceptDescription(rsod1.getString(9));
				ReceiptLines.setAMN_Concept_Uom_ID(rsod1.getInt(10));
				ReceiptLines.setAMN_Contract_ID(p_AMN_Contract_ID);
				// QTY DEFAULT VALUE
				Concept_DefaultValue = BigDecimal.valueOf(1.00);;
				// CALC RULES AND DV VAR ONLY ON FIRST LINE
				if (lin > 1) {
					forceRulesInit=false;
					forceDVInit=false;
				}
				// CALCULATE DEFAULT VALUE
				try {
					AmerpPayrollCalc.PayrollEvaluation(ctx, p_AMN_Payroll_ID, ReceiptLines.getCalcOrder(), forceRulesInit, forceDVInit, false);
					// Evauate Concept_ScriptDefaultValueST if Empty
					if (ReceiptLines.getScriptDefaultValue()==null || ReceiptLines.getScriptDefaultValue().isEmpty()) {
						RetVal=AmerpPayrollCalc.FormulaEvaluationScript(
								p_AMN_Payroll_ID, ReceiptLines.getConceptValue(), ReceiptLines.getDefaultValue(), Concept_DefaultValue, Salary, Payrolldays, "", false);
						Concept_DefaultValue = RetVal.getBDCalcAmnt();
					} else {
						RetVal=AmerpPayrollCalc.FormulaEvaluationScript(
								p_AMN_Payroll_ID, ReceiptLines.getConceptValue(), ReceiptLines.getScriptDefaultValue(), Concept_DefaultValue, Salary, Payrolldays, "", false);
						Concept_DefaultValue = RetVal.getBDCalcAmnt();				
					}
				}
		        catch (ScriptException ex) {
		            // TODO Auto-generated catch block
		        	Concept_DefaultValue = BigDecimal.valueOf(1.00);
		            //ex.printStackTrace();
		            log.log(Level.WARNING, "** ERROR ON ** FormulaEvaluationScript" + ReceiptLines.getConceptValue(), ex);
		        }
				// SET QtyValue of Array
				ReceiptLines.setQtyValue(Concept_DefaultValue);
				// log.warning("  AMN_Payroll_ID="+p_AMN_Payroll_ID+"  CONCEPTO="+ ReceiptLines.getConceptValue()+" Concept_DefaultValue="+Concept_DefaultValue);
				// ADD to ReceiptConcepts Array
				ReceiptConcepts.add(ReceiptLines);
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; 
			pstmt1 = null;
		}
		DB.close(rsod1, pstmt1);

		// ----------------
		// COPY ARRAY TO DB
		// ----------------
		//  CREATE MAMN_Payroll Detail
		for (int j=0 ; j < ReceiptConcepts.size() ; j++) {
			// log.warning("Concepts j=("+j+") "+ReceiptConcepts.get(j).getConceptValue()+" "+ReceiptConcepts.get(j).getConceptName()+" "+ReceiptConcepts.get(j).getCalcOrder());
			// RECEIPT LINES
			amnpayrolldetail.createAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
					amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID(),  ReceiptConcepts.get(j).getAMN_Process_ID(), ReceiptConcepts.get(j).getAMN_Contract_ID(),
					p_AMN_Payroll_ID, ReceiptConcepts.get(j).getAMN_Concept_Types_Proc_ID(), 
					ReceiptConcepts.get(j).getConceptValue(), ReceiptConcepts.get(j).getCalcOrder(), 
					ReceiptConcepts.get(j).getConceptName(), ReceiptConcepts.get(j).getConceptName(), 
					ReceiptConcepts.get(j).getAMN_Concept_Uom_ID(),ReceiptConcepts.get(j).getQtyValue(), 
					trxName);
			// RECEIPT LINES FOR LOANS PAYMENTS
			// VERIFY AMN_Payroll_Deferred and Create MAMN_Payroll_Detail (DEFERRED DOCUMENT LINES)
			// CreatePayrollOneDocDetailDeferredLines(ctx, ReceiptConcepts.get(j).getAMN_Process_ID(), ReceiptConcepts.get(j).getAMN_Contract_ID(), amnpayroll.getAMN_Payroll_ID(), trxName);
			// LOANS 		
		}	
		
		return Msg_Value;
	}

	/**
	 * CalculatePayrollDocuments
	 * Calculate Document for One Employee
	 * @param int p_AMN_Process_ID
	 * @param int p_AMN_Contract_ID
	 * @param int p_AMN_Period_ID
	 * @param int p_AMN_Employee_ID
	 * @param int p_AMN_Payroll_ID
	 * @return Msg_Value
	 */
	public static String CalculateOnePayrollDocument (
		Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
		int p_AMN_Period_ID, int p_AMN_Employee_ID, 
		int p_AMN_Payroll_ID, String trxName ) {
	    String AMN_Process_Value="";
	    String EmpName="";
	    Msg_Value="";
		IProcessUI processMonitor = Env.getProcessUI(Env.getCtx());
		// Employee
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		EmpName = amnemployee.getValue()+"-"+amnemployee.getName();
		// Determines Process Value to see if NN
		MAMN_Process amnprocess = new MAMN_Process(ctx, p_AMN_Process_ID, null);
		AMN_Process_Value = amnprocess.getAMN_Process_Value();
		MAMN_Payroll amnpayroll = null;
		// Verify if p_AMN_Payroll_ID is not in parameter
		if (p_AMN_Payroll_ID == 0) {
			amnpayroll = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
			p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
			p_AMN_Payroll_ID = amnpayroll.getAMN_Payroll_ID();
		}
		Msg_Value=Msg_Value+(Msg.getElement(ctx, "AMN_Payroll_ID")+":"+p_AMN_Employee_ID+" \r\n").trim();
		//log.warning(" BEGIN CalculateOnePayrollDocument...p_AMN_Payroll_ID="+p_AMN_Payroll_ID+"\r\n"+Msg_Value);
		// RECALC DOCUMENT
		try {
			// OJO AMN_Payroll_ID
            AmerpPayrollCalc.PayrollEvaluationArrayCalculate(ctx, p_AMN_Payroll_ID);
        }
        catch (ScriptException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
		if (processMonitor != null)
		{
			processMonitor.statusUpdate(
					Msg.getElement(ctx,"AMN_Payroll_ID")+": "+p_AMN_Employee_ID+" "+EmpName);
		}

		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
				AMN_Process_Value.equalsIgnoreCase("TI") ) {
			// ************************
			// Process NNN an TI	
			// ************************		
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// ************************
			// Process NV	
			// ************************
			// Update Header
			amnpayroll.updateAMNPayroll(ctx, AMN_Process_Value, p_AMN_Payroll_ID, trxName);

		} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// ************************
			// Process NP
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
			// ************************
			// Process NU
			// ************************
		} else {
			Msg_Value=Msg_Value+(Msg.getMsg(ctx, "Process")+":"+AMN_Process_Value.trim()+"NotAvailable"+" \n");
		}
		return Msg_Value;
	}

	/**
	 * CreatePayrollOneDocDetailDeferredLines
	 * Description: Creates Records on Table AMN_PAyroll_Detail from AMN_Payroll_deferred
	 * 				For LOANS
	 * @param int p_AMN_Process_ID,
	 * @param int p_AMN_Contract_ID
	 * @param int p_AMN_Payroll_ID
	 * return String Msg_Value
	 */
	public static String CreatePayrollOneDocDetailDeferredLines 
		(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			 int p_AMN_Payroll_ID, String trxName) {
		
	    String sql="";
	    int AMN_Concept_Types_Proc_ID =0;
	    int AMN_Period_ID = 0;
	    int AMN_Employee_ID = 0;
	    int DeferredAMN_Payroll_ID =0;
	    BigDecimal AmountCalculated = BigDecimal.valueOf(0);
	    
		MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(ctx, 0, null);
		// MAMN_Contract
		MAMN_Contract amncontract = new MAMN_Contract(ctx,p_AMN_Contract_ID,null);		
		new MAMN_Process(ctx, p_AMN_Process_ID, null);
		// MAMN_Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		// MAMN_Period
		AMN_Period_ID = amnpayroll.getAMN_Period_ID();
		// MAMN_Employee
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), null);
		AMN_Employee_ID = amnemployee.getAMN_Employee_ID();
		sql ="SELECT   " +
				"def.amn_concept_types_proc_id,  " +
				"def.amountcalculated,  " +
				"def.amountallocated,  " +
				"def.amountdeducted,  " +
				"def.qtyvalue, " +
				"def.value, def.name,  " +
				"def.description,  " +
				"def.amn_payroll_id , " +
				"det.amn_payroll_detail_id " +
				"FROM adempiere.AMN_Payroll_Deferred as def " +
				"LEFT JOIN adempiere.AMN_Payroll_Detail as det ON ( def.AMN_Payroll_ID = det.AMN_Payroll_ID) " +
				"WHERE def.AMN_Process_ID = ? "+
				"AND def.AMN_Period_ID = ? " +
				"AND def.AMN_Employee_ID = ? "
				;
		//log.warning("Loan for Employee:"+AMN_Employee_ID+" "+amnemployee.getName());
		//log.warning("sql:"+sql);
		//log.warning("Deferred...p_AMN_Payroll_ID:"+p_AMN_Payroll_ID+"  p_AMN_Process_ID"+p_AMN_Process_ID);
		//log.warning("Deferred.. AMN_Period_ID:"+AMN_Period_ID+"  sql:"+sql);
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Process_ID);
            pstmt1.setInt (2, AMN_Period_ID);
            pstmt1.setInt (3, AMN_Employee_ID);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				AMN_Concept_Types_Proc_ID = rsod1.getInt(1);
				AmountCalculated = rsod1.getBigDecimal(2);
				DeferredAMN_Payroll_ID = rsod1.getInt(10);
				//log.warning("Deferred...amn_payroll_id:"+DeferredAMN_Payroll_ID+" AmountCalculated:"+AmountCalculated);
				//log.warning("prestamo:"+AmountCalculated);
				//  CREATE MAMN_Payroll Detail
				amnpayrolldetail.createAmnPayrollDetailDeferred(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
						amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(),  p_AMN_Process_ID, p_AMN_Contract_ID,
						p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, AmountCalculated ,DeferredAMN_Payroll_ID, trxName);
//				//  UPDATE MAMN_Payroll Detail
//				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
//						amncontract.getAD_Client_ID(), amncontract.getAD_Org_ID(),  p_AMN_Process_ID, p_AMN_Contract_ID,
//						p_AMN_Payroll_ID,  AMN_Concept_Types_Proc_ID,  AmountCalculated);
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; pstmt1 = null;
		}
		return Msg_Value;
	}

	/**
	 * CreatePayrollOneDocumentLinesFromArray: 
	 * Similar CreatePayrollOneDocumentLines method but using Array
	 * 		List<AMNReceiptLines> ReceiptConcepts
	 * @param ctx
	 * @param List<AMNReceiptLines> ReceiptConcepts
	 * @param int p_AMN_Period_ID
	 * @param int p_AMN_Payroll_Lot_ID
	 * @param int p_AMN_Employee_ID
	 * @param int p_AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static String CreatePayrollOneDocumentLinesFromArray (Properties ctx, List<AMNReceiptLines> ReceiptConcepts, int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, String trxName) {
	    String AMN_Process_Value="NN";
	    Msg_Value="";
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(ctx, 0, null);
		BigDecimal Concept_DefaultValue = BigDecimal.ZERO;
		// MAMN_Contract
		MAMN_Contract amncontract = new MAMN_Contract(ctx,amnpayroll.getAMN_Contract_ID(),null);		
		new MAMN_Process(ctx, amnpayroll.getAMN_Process_ID(), null);
		//BigDecimal PayrolldaysC from AMN_Contract
		BigDecimal PayrolldaysC = amncontract.getPayRollDays();
		// GET Salary from AMN_Employee
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		BigDecimal Salary=amnemployee.getSalary();
		// GET Payrolldays from AMN_Payroll
		//log.warning(" MAMN_Payroll_Detail---p_AMN_Payroll_ID="+p_AMN_Payroll_ID+"  fechas INI="+amnpayroll.getInvDateIni()+"  End="+amnpayroll.getInvDateEnd());	
		// deprecated BigDecimal Payrolldays = MAMN_Payroll.sqlGetAMNPayrollDays(p_AMN_Payroll_ID);    
		BigDecimal Payrolldays = amnpayroll.getAMNPayrollDays(p_AMN_Payroll_ID ); 
		if (PayrolldaysC.equals(Payrolldays) ) {
			Payrolldays = PayrolldaysC;
		}	
		scriptResult RetVal = new scriptResult();
		// Rules only on PL
		boolean forceRulesInit=false;
		boolean forceDVInit=true;
		int lin = 0;
       	// MSysConfig AMERP_Payroll_Rules_Apply 
		String apra = MSysConfig.getValue("AMERP_Payroll_Rules_Apply","N",amnpayroll.getAD_Client_ID());
		if (apra.compareToIgnoreCase("Y")==0)
			forceRulesInit=true;
		else
			forceRulesInit=false;
		// 
		// UPDATE ARRAY
		//
		for (int j=0 ; j < ReceiptConcepts.size() ; j++) {
			// QTY DEFAULT VALUE
			Concept_DefaultValue = BigDecimal.valueOf(1.00);;
			// CALC RULES AND DV VAR ONLY ON FIRST LINE
			if (lin > 1) {
				forceRulesInit=false;
				forceDVInit=false;
			}
			// CALCULATE DEFAULT VALUES
			try {
				AmerpPayrollCalc.PayrollEvaluation(ctx, p_AMN_Payroll_ID, ReceiptConcepts.get(j).getCalcOrder(), forceRulesInit, forceDVInit, false);
				// Evauate Concept_ScriptDefaultValueST if Empty
				if (ReceiptConcepts.get(j).getScriptDefaultValue()==null || ReceiptConcepts.get(j).getScriptDefaultValue().isEmpty()) {
					RetVal=AmerpPayrollCalc.FormulaEvaluationScript(
							p_AMN_Payroll_ID, ReceiptConcepts.get(j).getConceptValue(), ReceiptConcepts.get(j).getDefaultValue(), Concept_DefaultValue, Salary, Payrolldays, "", false);
					Concept_DefaultValue = RetVal.getBDCalcAmnt();
				} else {
					RetVal=AmerpPayrollCalc.FormulaEvaluationScript(
							p_AMN_Payroll_ID, ReceiptConcepts.get(j).getConceptValue(), ReceiptConcepts.get(j).getScriptDefaultValue(), Concept_DefaultValue, Salary, Payrolldays, "", false);
					Concept_DefaultValue = RetVal.getBDCalcAmnt();				
				}
			}
	        catch (ScriptException ex) {
	            // TODO Auto-generated catch block
	        	Concept_DefaultValue = BigDecimal.valueOf(1.00);
	            //ex.printStackTrace();
	            log.log(Level.WARNING, "** ERROR ON ** FormulaEvaluationScript" + ReceiptConcepts.get(j).getConceptValue(), ex);
	        }
			// SET QtyValue of Array
			ReceiptConcepts.get(j).setQtyValue(Concept_DefaultValue);
		}
		// ---------------------------------------------
		//  CREATE MAMN_Payroll Detail
		// 	CREATE MAMN_Payroll_Detail (DOCUMENT LINES)
		// ---------------------------------------------
		for (int j=0 ; j < ReceiptConcepts.size() ; j++) {
			//log.warning("Concepts j=("+j+") "+ReceiptConcepts.get(j).getConceptValue()+" "+ReceiptConcepts.get(j).getConceptName()+" "+ReceiptConcepts.get(j).getCalcOrder());
			// RECEIPT LINES
//			amnpayrolldetail.createAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
//					amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID(),  ReceiptConcepts.get(j).getAMN_Process_ID(), ReceiptConcepts.get(j).getAMN_Contract_ID(),
//					p_AMN_Payroll_ID, ReceiptConcepts.get(j).getAMN_Concept_Types_Proc_ID(), trxName);
			// RECEIPT LINES
			amnpayrolldetail.createAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
					amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID(),  ReceiptConcepts.get(j).getAMN_Process_ID(), ReceiptConcepts.get(j).getAMN_Contract_ID(),
					p_AMN_Payroll_ID, ReceiptConcepts.get(j).getAMN_Concept_Types_Proc_ID(), 
					ReceiptConcepts.get(j).getConceptValue(), ReceiptConcepts.get(j).getCalcOrder(), 
					ReceiptConcepts.get(j).getConceptName(), ReceiptConcepts.get(j).getConceptName(), 
					ReceiptConcepts.get(j).getAMN_Concept_Uom_ID(),ReceiptConcepts.get(j).getQtyValue(), 
					trxName);
			// RECEIPT LINES FOR LOANS PAYMENTS
			// VERIFY AMN_Payroll_Deferred and Create MAMN_Payroll_Detail (DEFERRED DOCUMENT LINES)
			CreatePayrollOneDocDetailDeferredLines(ctx, ReceiptConcepts.get(j).getAMN_Process_ID(), ReceiptConcepts.get(j).getAMN_Contract_ID(), amnpayroll.getAMN_Payroll_ID(), trxName);
			// LOANS 		
		}	
		//CreatePayrollOneDocDetailLines(ctx, ReceiptLine.getAMN_Process_ID(), ReceiptLine.getAMN_Contract_ID(), amnpayroll.getAMN_Payroll_ID(), trxName);
		//
		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
				AMN_Process_Value.equalsIgnoreCase("TI") ) {
			// ************************
			// Process NNN an TI	
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// ************************
			// Process NV	
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// ************************
			// Process NP
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
			// ************************
			// Process NU
			// ************************
		} else {
			Msg_Value=Msg_Value+(Msg.getMsg(ctx, "Process")+":"+AMN_Process_Value.trim()+"NotAvailable"+" \r\n");
		}
		return Msg_Value;
	}
	
}

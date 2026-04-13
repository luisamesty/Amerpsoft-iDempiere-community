package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.script.ScriptException;

import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class AmerpPayrollCalcArray {

	CLogger log = CLogger.getCLogger(AmerpPayrollCalcArray.class);
	
	/*******************************************************************************************
	 * Payroll Evaluation Array Generate Array & Evaluate 
	 * Parameters: p_ctx ,  p_AMN_Payroll_ID 
	 * Notes: this Method is Used by AMNPayrollRefresh
	 * Process Called from Payroll Preparing // Window // Also Called by
	 * PayrollDetail Callout on Concept_Types_Proc Value 
	 * Also Called by Event AMN Payroll event //
	 ***************************************************************************************/
	
	public void PayrollEvaluationArrayCalculate(Properties p_ctx, int p_AMN_Payroll_ID) throws ScriptException {
		CLogger.getCLogger(AmerpPayrollCalc.class);

		PayrollVariables pyVars = new PayrollVariables(true);
		AmerpPayrollCalc amerpPayrollCalc = new AmerpPayrollCalc();
		PayrollScriptEngine pyScriptEngine = new PayrollScriptEngine();
		ScriptResult RetVal = new ScriptResult();
		String IS_FAOV, IS_SALARIO, IS_INCES, IS_SSO, IS_ARC, IS_SPF, IS_FERIADO, IS_UTILIDAD, IS_PRESTACION,
				IS_VACACION = "N";
		String ErrorMessage = "";
		int AD_Client_ID;
		BigDecimal calcAmnt, calcAmntDR, calcAmntCR, calcAmntNet;
		String Concept_Variable;
		String Concept_Script;
		int Payroll_Detail_ID;
		Integer CalcOrder, WorkingDays;
		BigDecimal va_SB;
		String sql1 = "", sql2 = "", sql3 = "";
		String ConceptOptmode, ConceptSign, ConceptIsshow = "";
		String Formula, Concept_Reference = "";
		BigDecimal WorkingDaysBD = BigDecimal.valueOf(0);
		Integer Period_ID = 0, Process_ID = 0, Contract_ID = 0, Payroll_ID = 0;
		Double QtyValue;
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		// AMN_Employee
		// amnpayroll.getAMN_Employee_ID();
		MAMN_Employee employee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), null);
		Period_ID = amnpayroll.getAMN_Period_ID();
		Process_ID = amnpayroll.getAMN_Process_ID();
		Payroll_ID = amnpayroll.getAMN_Payroll_ID();
		AD_Client_ID = amnpayroll.getAD_Client_ID();
		// Init Variables
		pyVars.VariablesInit(AD_Client_ID);
		// AMN_Period
		MAMN_Period amnperiod = new MAMN_Period(p_ctx, Period_ID, null);
		amnperiod.getAMNDateIni();
		amnperiod.getAMNDateEnd();
		new MAMN_Process(p_ctx, Process_ID, null);
		// Process_Value = amnprocess.getValue().trim();
		// Initialize Total DR CR
		calcAmntDR = BigDecimal.valueOf(0);
		calcAmntCR = BigDecimal.valueOf(0);
		calcAmntNet = BigDecimal.valueOf(0);
		// ***************************
		// * Get Employee´s Salary SB
		// ***************************
		va_SB = employee.getSalary();
		// Payroll Detail Array
		sql2 = "SELECT " + "pad.qtyvalue, " + "cty.formula,  " + "ctp.name as conceptname, "
				+ "cty.calcorder as calcorder, " + "cty.faov as isfaov, " + "coalesce(cty.feriado,'N') as isferiado, "
				+ "coalesce(cty.ince,'N') as isince, " + "coalesce(cty.prestacion,'N') as isprestacion, "
				+ "coalesce(cty.salario,'N') as issalario, " + "coalesce(cty.sso,'N') as issso, "
				+ "coalesce(cty.spf,'N') as isspf, " + "coalesce(cty.arc,'N') as isarc, "
				+ "coalesce(cty.vacacion,'N') as isvacacion, " + "coalesce(cty.utilidad,'N') as isutilidad, "
				+ "pay.invdateini, " + "pay.invdateend, "
				+ "CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
				+ "cum.value as cuom, " + "cty.optmode as coptmode, " + "cty.sign as csign, " + "cty.rule as crule, "
				+ "cty.isrepeat as isrepeat, " + "cty.isshow as isshow, " + "pad.amountallocated, "
				+ "pad.amountdeducted, " + "cty.value as creference, "
				+ "coalesce(cty.variable,cty.value,'') as cvariable, " + "coalesce(cty.script,'') as script, "
				+ "pay.amn_payroll_id, " + "pad.amn_payroll_detail_id " + "FROM amn_payroll as pay "
				+ "LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) "
				+ "LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) "
				+ "LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) "
				+ "LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
				+ "WHERE pay.amn_payroll_id = ? " + " AND cty.calcorder < 999999999 " + "ORDER BY cty.calcorder";

		PreparedStatement pstmtc = null;
		ResultSet rsc = null;
		try {
			pstmtc = DB.prepareStatement(sql2, null);
			pstmtc.setInt(1, p_AMN_Payroll_ID);

			rsc = pstmtc.executeQuery();
			// Reference Concepts Only
			while (rsc.next()) {
				QtyValue = rsc.getDouble(1);
				Formula = rsc.getString(2);
				rsc.getString(3);
				CalcOrder = rsc.getInt(4);
				IS_FAOV = rsc.getString(5);
				IS_FERIADO = rsc.getString(6);
				IS_INCES = rsc.getString(7);
				IS_PRESTACION = rsc.getString(8);
				IS_SALARIO = rsc.getString(9);
				IS_SSO = rsc.getString(10);
				IS_SPF = rsc.getString(11);
				IS_ARC = rsc.getString(12);
				IS_VACACION = rsc.getString(13);
				IS_UTILIDAD = rsc.getString(14);
				rsc.getDate(15);
				rsc.getDate(16);
				WorkingDays = rsc.getInt(17);
				rsc.getString(18);
				ConceptOptmode = rsc.getString(19);
				ConceptSign = rsc.getString(20);
				rsc.getString(21);
				rsc.getString(22);
				ConceptIsshow = rsc.getString(23);
				rsc.getDouble(24);
				rsc.getDouble(25);
				Concept_Reference = rsc.getString(26);
				Concept_Variable = rsc.getString(27).trim();
				Concept_Script = rsc.getString(28).trim();
				Payroll_ID = rsc.getInt(29);
				Payroll_Detail_ID = rsc.getInt(30);
				// *******************************************************
				// Calculate Concepts VARIABLES
				// *******************************************************
				WorkingDaysBD = BigDecimal.valueOf((double) WorkingDays);
				// log.warning("Row:"+rsc.getRow()+ "......antes .....");
				// Force Init Rules Variables and SQL Variables only for first Row
				boolean forceRulesInit = false;
				boolean forceDVInit = false;
				// Verify if first row on receipt
				if (rsc.getRow() == 1) {
					// MSysConfig AMERP_Payroll_Rules_Apply
					String apra = MSysConfig.getValue("AMERP_Payroll_Rules_Apply", "N", amnpayroll.getAD_Client_ID());
					if (apra.compareToIgnoreCase("Y") == 0)
						forceRulesInit = true;
					else
						forceRulesInit = false;
					forceDVInit = true;
				} else {
					forceRulesInit = false;
					forceDVInit = false;
				}
				pyVars = amerpPayrollCalc.PayrollEvaluation(p_ctx, Payroll_ID, CalcOrder, forceRulesInit, forceDVInit, true);
				// CALCULATE VALUE from Concept_Script
				if (Formula.trim().equalsIgnoreCase("script")
						|| ((!Concept_Script.isEmpty() && Concept_Script != null))) {
					RetVal = pyScriptEngine.FormulaEvaluationScript(Payroll_ID, pyVars, Concept_Reference, Concept_Script,
							BigDecimal.valueOf(QtyValue), va_SB, WorkingDaysBD, ConceptOptmode, true);
					ErrorMessage = RetVal.getErrorMessage();
					calcAmnt = RetVal.getBDCalcAmnt();
					// CALCULATE Value FROM Formula
				} else {
					RetVal = pyScriptEngine.FormulaEvaluationScript(Payroll_ID, pyVars, Concept_Reference, Formula,
							BigDecimal.valueOf(QtyValue), va_SB, WorkingDaysBD, ConceptOptmode, true);
					ErrorMessage = RetVal.getErrorMessage();
					calcAmnt = RetVal.getBDCalcAmnt();
				}

				pyVars.CTaddConceptResults(CalcOrder, Concept_Variable, BigDecimal.valueOf(QtyValue), calcAmnt, ErrorMessage);
				// Update AMN_Payroll_Detail - Invoice Line
				// Verify is ConceptType Applies on Results
				// FIRTS VERIFY if Reference
				if (ConceptOptmode.equalsIgnoreCase("R")) {
					// + " set amountcalculated= "+calcAmnt+","
					sql3 = "UPDATE AMN_Payroll_Detail " + " set amountcalculated= " + calcAmnt + ","
							+ " amountallocated= 0," + " amountdeducted=0 " + " where amn_payroll_detail_id ="
							+ Payroll_Detail_ID;
					// ALLOCATION
					// calcAmntDR=calcAmntDR.add(calcAmnt);
				} else {
					if (ConceptIsshow.equalsIgnoreCase("N")) {
						if (ConceptSign.equalsIgnoreCase("D")) {
							sql3 = "UPDATE AMN_Payroll_Detail " + " set amountcalculated= " + calcAmnt + ","
									+ " amountallocated= 0," + " amountdeducted=0 " + " where amn_payroll_detail_id ="
									+ Payroll_Detail_ID;
							// ALLOCATION
							// calcAmntDR=calcAmntDR.add(calcAmnt);

						} else {
							sql3 = "UPDATE AMN_Payroll_Detail " + " set amountcalculated= " + calcAmnt + ","
									+ " amountallocated=0 ," + " amountdeducted=0 " + " where amn_payroll_detail_id ="
									+ Payroll_Detail_ID;
							// DEDUCTION
							// calcAmntCR=calcAmntCR.add(calcAmnt);
						}
					} else {
						if (ConceptSign.equalsIgnoreCase("D")) {
							sql3 = "UPDATE AMN_Payroll_Detail " + " set amountcalculated= " + calcAmnt + ","
									+ " amountallocated= " + calcAmnt + "," + " amountdeducted=0 "
									+ " where amn_payroll_detail_id =" + Payroll_Detail_ID;
							// ALLOCATION
							calcAmntDR = calcAmntDR.add(calcAmnt);

						} else {
							sql3 = "UPDATE AMN_Payroll_Detail " + " set amountcalculated= " + calcAmnt + ","
									+ " amountallocated=0 ," + " amountdeducted=" + calcAmnt
									+ " where amn_payroll_detail_id =" + Payroll_Detail_ID;
							// DEDUCTION
							calcAmntCR = calcAmntCR.add(calcAmnt);
						}
					}
				}
				DB.executeUpdateEx(sql3, null);
				// log.warning(".....PayrollEvaluationArrayCalculate.."+"
				// CalcOrder="+CalcOrder+" ConceptOptmode:"+ConceptOptmode+"
				// QtyValue:"+QtyValue+" calcAmnt"+calcAmnt+"..");
			}
			// Update AMN_Payroll (HEADER VALUES)
			calcAmntNet = BigDecimal.valueOf(0);
			calcAmntNet = calcAmntNet.add(calcAmntDR);
			calcAmntNet = calcAmntNet.subtract(calcAmntCR);
			sql3 = "UPDATE AMN_Payroll " + " set amountnetpaid=" + calcAmntNet + "," + " amountcalculated="
					+ calcAmntNet + "," + " amountallocated=" + calcAmntDR + "," + " amountdeducted=" + calcAmntCR
					+ " where amn_payroll_id =" + p_AMN_Payroll_ID;
			DB.executeUpdateEx(sql3, null);
		} catch (SQLException e) {
			// log.warning("catch");
			QtyValue = 0.00;
			Formula = "";
			CalcOrder = 0;
		} finally {
			DB.close(rsc, pstmtc);
			rsc = null;
			pstmtc = null;
		}
		// Show Variables
		// pyVars.logVariablesShow();
		// log.warning("...Rules at End ...");
		// AmerpPayrollCalc.logRulesShow();
	} // PayrollEvaluationArrayCalculate

}
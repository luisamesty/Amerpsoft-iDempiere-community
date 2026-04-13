package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.ScriptException;

import org.amerp.amnmodel.MAMN_Concept_Types;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class PayrollScriptEngine {

	public PayrollScriptEngine() {
		super();
		//
	}
	
	CLogger log = CLogger.getCLogger(PayrollScriptEngine.class);
	
	/*************************************************************************
   	// Formula Evaluation Script using Script Engine Manager for Formulas
   	//
	// Parameters:
	// 	String 		p_Concept_Reference
	//  String		p_script or p_formula Depending of Formula Long
	// 	Bigdecimal	p_qtyValueRead
	// 	Bigdecimal	p_va_SB
	//	Integer		p_workdays
	//  String 		p_OptMode ("DV" or "")
	//  forceRounding: force when true Currency roundind when false default 2
	//
	// 	Returns: scriptResult RetVal 
	//				(BigDecimal) BDCalcAmnt
	// 				String ErrorMessage
	// 
	// All Variables and Calculation are Made with  Double Class 
   	//***********************************************************************/
	public ScriptResult FormulaEvaluationScript 	(
			int p_AMN_Payroll_ID, PayrollVariables pyVars,
			String p_Concept_Reference, String p_script, BigDecimal p_qtyValueRead, 
			BigDecimal p_va_SB, BigDecimal p_workdays, String p_OptMode, boolean forceRounding) throws ScriptException
	{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_ID, null);
		MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), amnpayroll.getAMN_Employee_ID(), null);
		MAMN_Contract amncontract = new MAMN_Contract(Env.getCtx(), amnpayroll.getAMN_Contract_ID(), null);
		int AMN_Concept_Types_ID = MAMN_Concept_Types.sqlGetAMNConceptTypesID(amnpayroll.getAD_Client_ID(),p_Concept_Reference);
		// Rounding Mode
		int roundingMode = 2;
		if (forceRounding )  
			// roundingMode = curr.getStdPrecision();
			// Precision from Contract Variable
			roundingMode = amncontract.getStdPrecision();
		// SHOW VARIABLES
		// Script Engine Manager
		// Rhino Context
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		Object Result= null;
		// Calc Value
		double CND = p_qtyValueRead.doubleValue();
		double SBD = p_va_SB.doubleValue();
		double QT_SB=SBD;
		double WorkDays = p_workdays.doubleValue(); // The double value of p_workdays
		Double CalcAmnt=0.00;
		BigDecimal BDCalcAmnt;
		String ErrorMessage="";
		Properties p_ctx = Env.getCtx();
		ScriptResult RetVal = new ScriptResult();
		// Reference Concepts Returns OK 
		if (!p_script.isEmpty() && p_script!=null) {
			// ***********************************
			// PUT STANDARD VARIABLES VALUES--------
			// ***********************************
			// va_CN: Quantity qtyValueRead converted to Double for interprete
			//log.warning("CND:"+CND);
			if (p_script.contains("CN")) {
				//log.warning("Cantidad CN:"+p_qtyValueRead);
				if (p_OptMode.equalsIgnoreCase("DV")) {
					// IF DEFAULTVALUE CALC ALWAYS SEt TO 1
					// interprete.put <--> interprete.put
					ScriptableObject.putProperty(scope, "CN", 1);
					//interprete.put("CN", 1);
					//ctx.setAttribute("CN", 1, ScriptContext.ENGINE_SCOPE);
				} else {
					ScriptableObject.putProperty(scope, "CN", CND);
					//interprete.put("CN", CND);
					//ctx.setAttribute("CN", CND, ScriptContext.ENGINE_SCOPE);
				}
			}
			// va_SA: Salary converted to Double for interprete
			if (p_script.contains("SB")) {
				//log.warning("Salario SB:"+p_va_SB);
				ScriptableObject.putProperty(scope, "SB", SBD);
				//interprete.put("SB", SBD);
				//ctx.setAttribute("CN", SBD, ScriptContext.ENGINE_SCOPE);
			}
			// Salary converted to Double for interprete
			if (p_script.contains("QT_SB")) {
				//log.warning("Salario SB:"+p_va_SB);
				if (QT_SB == 0) {
					QT_SB = SBD;
				}
				ScriptableObject.putProperty(scope,"QT_SB", QT_SB);
				//ctx.setAttribute("QT_SB", QT_SB, ScriptContext.ENGINE_SCOPE);
			}			// DT: VALUE FROM CONCEPT TYPE DIAS
			if (p_script.contains("DT")) {
				ScriptableObject.putProperty(scope,"DT", pyVars.get("WorkingDaysDT"));
				//ctx.setAttribute("DT", WorkingDaysDT, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", pyVars.get("DT").doubleValue());
			}
			// DT: VALUE FROM CONCEPT TYPE DIAS
			if (p_script.contains("DIAS")) {
				ScriptableObject.putProperty(scope,"DIAS", pyVars.get("WorkingDaysDT"));
				//ctx.setAttribute("DIAS", WorkingDaysDT, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", pyVars.get("DT").doubleValue());
			}
			// DTOK: Days Worked OK COMPLETED converted to Double for interprete
			if (p_script.contains("DTOK")) {
				ScriptableObject.putProperty(scope,"DTOK", WorkDays);
				//ctx.setAttribute("DTOK", WorkDays, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", pyVars.get("DT").doubleValue());
			}
			// DTREC: Days on Payroll Receipt
			if (p_script.contains("DTREC")) {
				ScriptableObject.putProperty(scope,"DTREC", WorkDays);
				//ctx.setAttribute("DTREC", WorkDays, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", pyVars.get("DT").doubleValue());
			}
			// DTPER: Days from Contract Period 
			if (p_script.contains("DTPER")) {
				ScriptableObject.putProperty(scope,"DTPER", pyVars.get("DTPER"));
				//ctx.setAttribute("DTPER", DTPER, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", pyVars.get("DT").doubleValue());
			}
			//log.warning(".....................AmerpPayrollCalca.java...FormulaEvaluationScript...................................");
			//log.warning("p_OptMode:"+p_OptMode+"  CND:"+(CND)+"  SBD:"+SBD+"    WorkDays:"+WorkDays);				
			// R_ASIG: Total DB
			if (p_script.contains("R_ASIG")) {
				ScriptableObject.putProperty(scope,"R_ASIG", BigDecimal.valueOf(pyVars.get("R_ASIG")));
				//ctx.setAttribute("R_ASIG", BigDecimal.valueOf(R_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			// R_DEDUC: Total CR
			if (p_script.contains("R_DEDUC")) {
				ScriptableObject.putProperty(scope,"R_DEDUC", BigDecimal.valueOf(pyVars.get("R_DEDUC")));
				//ctx.setAttribute("R_DEDUC", BigDecimal.valueOf(R_DEDUC), ScriptContext.ENGINE_SCOPE);
			}
			// R_TOTAL: Total NET
			if (p_script.contains("R_TOTAL")) {
				ScriptableObject.putProperty(scope,"R_TOTAL", BigDecimal.valueOf(pyVars.get("RA_ASIG")));
				//ctx.setAttribute("R_TOTAL", BigDecimal.valueOf(RA_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			// RA_ASIG: Total DB Not Shown
			if (p_script.contains("RA_ASIG")) {
				ScriptableObject.putProperty(scope,"RA_ASIG", BigDecimal.valueOf(pyVars.get("R_ASIG")));
				//ctx.setAttribute("RA_ASIG", BigDecimal.valueOf(R_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			// RA_DEDUC: Total CR Not Shown
			if (p_script.contains("RA_DEDUC")) {
				ScriptableObject.putProperty(scope,"RA_DEDUC", BigDecimal.valueOf(pyVars.get("RA_DEDUC")));
				//ctx.setAttribute("RA_DEDUC", BigDecimal.valueOf(RA_DEDUC), ScriptContext.ENGINE_SCOPE);
			}
			// RA_TOTAL: Total Net Not Shown
			if (p_script.contains("RA_TOTAL")) {
				ScriptableObject.putProperty(scope,"RA_TOTAL", BigDecimal.valueOf(pyVars.get("RA_TOTAL")));
				//ctx.setAttribute("RA_TOTAL", BigDecimal.valueOf(RA_TOTAL), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RE_ASIG")) {
				ScriptableObject.putProperty(scope,"RE_ASIG", BigDecimal.valueOf(pyVars.get("RE_ASIG")));
				//ctx.setAttribute("RE_ASIG", BigDecimal.valueOf(RE_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RE_DEDUC")) {
				ScriptableObject.putProperty(scope,"RE_DEDUC", BigDecimal.valueOf(pyVars.get("RE_DEDUC")));
				//ctx.setAttribute("RE_DEDUC", BigDecimal.valueOf(RE_DEDUC), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RE_TOTAL")) {
				ScriptableObject.putProperty(scope,"RE_TOTAL", BigDecimal.valueOf(pyVars.get("RE_TOTAL")));
				//ctx.setAttribute("RE_TOTAL", BigDecimal.valueOf(RE_TOTAL), ScriptContext.ENGINE_SCOPE);
			}			
			if (p_script.contains("R_FAOV")) {
				ScriptableObject.putProperty(scope,"R_FAOV", BigDecimal.valueOf(pyVars.get("R_FAOV")));
				//ctx.setAttribute("R_FAOV", BigDecimal.valueOf(R_FAOV), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_SALARIO")) {
				ScriptableObject.putProperty(scope,"R_SALARIO", BigDecimal.valueOf(pyVars.get("R_SALARIO")));
				//ctx.setAttribute("R_SALARIO", BigDecimal.valueOf(R_SALARIO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_INCES")) {
				ScriptableObject.putProperty(scope,"R_INCES", BigDecimal.valueOf(pyVars.get("R_INCES")));
				//ctx.setAttribute("R_INCES", BigDecimal.valueOf(R_INCES), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_SSO")) {
				ScriptableObject.putProperty(scope,"R_SSO", BigDecimal.valueOf(pyVars.get("R_SSO")));
				//ctx.setAttribute("R_SSO", BigDecimal.valueOf(R_SSO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_ARC")) {
				ScriptableObject.putProperty(scope,"R_ARC", BigDecimal.valueOf(pyVars.get("R_ARC")));
				//ctx.setAttribute("R_ARC", BigDecimal.valueOf(R_ARC), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_SPF")) {
				ScriptableObject.putProperty(scope,"R_SPF", BigDecimal.valueOf(pyVars.get("R_SPF")));
				//ctx.setAttribute("R_SPF", BigDecimal.valueOf(R_SPF), ScriptContext.ENGINE_SCOPE);
			}
			// VARIABLE R_DESCANSO EQUAL TO R_FERIADO
			if (p_script.contains("R_DESCANSO")) {
				ScriptableObject.putProperty(scope,"R_DESCANSO", BigDecimal.valueOf(pyVars.get("R_DESCANSO")));
				//ctx.setAttribute("R_DESCANSO", BigDecimal.valueOf(R_FERIADO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_FERIADO")) {
				ScriptableObject.putProperty(scope,"R_FERIADO", BigDecimal.valueOf(pyVars.get("R_FERIADO")));
				//ctx.setAttribute("R_FERIADO", BigDecimal.valueOf(R_FERIADO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_UTILIDAD")) {
				ScriptableObject.putProperty(scope,"R_UTILIDAD", BigDecimal.valueOf(pyVars.get("R_UTILIDAD")));
				//ctx.setAttribute("R_UTILIDAD", BigDecimal.valueOf(R_UTILIDAD), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_VACACION")) {
				ScriptableObject.putProperty(scope,"R_VACACION", BigDecimal.valueOf(pyVars.get("R_VACACION")));
				//ctx.setAttribute("R_VACACION",  BigDecimal.valueOf(R_VACACION), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_PRESTACION")) {
				ScriptableObject.putProperty(scope,"R_PRESTACION", BigDecimal.valueOf(pyVars.get("R_PRESTACION")));
				//ctx.setAttribute("R_PRESTACION", BigDecimal.valueOf(R_PRESTACION), ScriptContext.ENGINE_SCOPE);
			}
        	/* Change on Business Logic
        	 * Variable RA_TAX, RD_TAX, RA_SSO, RD_SSO
        	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
        	 */
			// RA_TAX
			if (p_script.contains("RA_TAX")) {
			ScriptableObject.putProperty(scope,"RA_TAX", BigDecimal.valueOf(pyVars.get("RA_TAX")));
			//ctx.setAttribute("R_ARC", BigDecimal.valueOf(R_ARC), ScriptContext.ENGINE_SCOPE);
			}
			// RD_TAX
			if (p_script.contains("RD_TAX")) {
			ScriptableObject.putProperty(scope,"RD_TAX", BigDecimal.valueOf(pyVars.get("RD_TAX")));
			}
			// RA_SSO
			if (p_script.contains("RA_SSO")) {
			ScriptableObject.putProperty(scope,"RA_SSO", BigDecimal.valueOf(pyVars.get("RA_SSO")));
			}
			// RD_SSO
			if (p_script.contains("RD_SSO")) {
			ScriptableObject.putProperty(scope,"RD_SSO", BigDecimal.valueOf(pyVars.get("RD_SSO")));
			}	
			/* Change on Business Logic
        	 * Variable RA_TAX, RD_TAX, RA_SSO, RD_SSO
        	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
        	 */	
			if (p_script.contains("SBMIN")) {
				ScriptableObject.putProperty(scope,"SBMIN", BigDecimal.valueOf(pyVars.get("SBMIN")));
				//ctx.setAttribute("SBMIN", BigDecimal.valueOf(SBMIN), ScriptContext.ENGINE_SCOPE);
			}
			// Process Value (NN,NP,NU,NV)
			if (p_script.contains("AM_Process")) {
				ScriptableObject.putProperty(scope,"AM_Process", pyVars.get("AM_Process"));
				//ctx.setAttribute("AM_Process", AM_Process, ScriptContext.ENGINE_SCOPE);
			}
			// Contract Value (S,Q,ME,MQ,..)
			if (p_script.contains("AM_Contract")) {
				ScriptableObject.putProperty(scope,"AM_Contract", pyVars.get("AM_Contract"));
				//ctx.setAttribute("AM_Contract", AM_Contract, ScriptContext.ENGINE_SCOPE);
			}
			// EMPLOYEE's VARS
			if (p_script.contains("AM_Shift")) {
				ScriptableObject.putProperty(scope,"AM_Shift", pyVars.get("AM_Shift"));
				//ctx.setAttribute("AM_Shift", AM_Shift, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_PayrollMode")) {
				ScriptableObject.putProperty(scope,"AM_PayrollMode", pyVars.get("AM_PayrollMode"));
				//ctx.setAttribute("AM_PayrollMode", AM_PayrollMode, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Status")) {
				ScriptableObject.putProperty(scope,"AM_Status", pyVars.get("AM_Status"));
				//ctx.setAttribute("AM_Status", AM_Status, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IncomeDate")) {
				ScriptableObject.putProperty(scope,"AM_IncomeDate", pyVars.get("AM_IncomeDate"));
				//ctx.setAttribute("AM_IncomeDate", AM_IncomeDate, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_PaymentType")) {
				ScriptableObject.putProperty(scope,"AM_PaymentType", pyVars.get("AM_PaymentType"));
				//ctx.setAttribute("AM_PaymentType", AM_PaymentType, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_CivilStatus")) {
				ScriptableObject.putProperty(scope,"AM_CivilStatus", pyVars.get("AM_CivilStatus"));
				//ctx.setAttribute("AM_CivilStatus", AM_CivilStatus, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Sex")) {
				ScriptableObject.putProperty(scope,"AM_Sex", pyVars.get("AM_Sex"));
				//ctx.setAttribute("AM_Sex", AM_Sex, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Spouse")) {
				ScriptableObject.putProperty(scope,"AM_Spouse", pyVars.get("AM_Spouse"));
				//ctx.setAttribute("AM_Spouse", AM_Spouse, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IsPensioned")) {
				ScriptableObject.putProperty(scope,"AM_IsPensioned", pyVars.get("AM_IsPensioned"));
				//ctx.setAttribute("AM_IsPensioned", AM_IsPensioned, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IsStudyng")) {
				ScriptableObject.putProperty(scope,"AM_IsStudyng", pyVars.get("AM_IsStudyng"));
				//ctx.setAttribute("AM_IsStudyng", AM_IsStudyng, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IsMedicated")) {
				ScriptableObject.putProperty(scope,"AM_IsMedicated", pyVars.get("AM_IsMedicated"));
				//ctx.setAttribute("AM_IsMedicated", AM_IsMedicated, ScriptContext.ENGINE_SCOPE);
			}
			// AM_BirthDate
			if (p_script.contains("AM_BirthDate")) {
				ScriptableObject.putProperty(scope,"AM_BirthDate", pyVars.get("AM_BirthDate"));
				//ctx.setAttribute("AM_BirthDate", AM_BirthDate, ScriptContext.ENGINE_SCOPE);
			}
			// AM_Workforce
			if (p_script.contains("AM_Workforce")) {
				//log.warning("AM_Workforce="+AM_Workforce);
				ScriptableObject.putProperty(scope,"AM_Workforce", pyVars.get("AM_Workforce"));
				//ctx.setAttribute("AM_Workforce", AM_Workforce, ScriptContext.ENGINE_SCOPE);
			}			
			// AM_Department
			if (p_script.contains("AM_Department")) {
				//log.warning("AM_Department="+AM_Department);
				ScriptableObject.putProperty(scope,"AM_Department", pyVars.get("AM_Department"));
				//ctx.setAttribute("AM_Department", AM_Department, ScriptContext.ENGINE_SCOPE);
			}	
			// AM_Location
			if (p_script.contains("AM_Location")) {
				ScriptableObject.putProperty(scope,"AM_Location", pyVars.get("AM_Location"));
				//ctx.setAttribute("AM_Location", AM_Location, ScriptContext.ENGINE_SCOPE);
			}	
			// AM_Project
			if (p_script.contains("AM_Project")) {
				ScriptableObject.putProperty(scope,"AM_Project", pyVars.get("AM_Project"));
				//ctx.setAttribute("AM_Project", AM_Project, ScriptContext.ENGINE_SCOPE);
			}			
			// AM_Activity
			if (p_script.contains("AM_Activity")) {
				ScriptableObject.putProperty(scope,"AM_Activity", pyVars.get("AM_Activity"));
				//ctx.setAttribute("AM_Activity", AM_Activity, ScriptContext.ENGINE_SCOPE);
			}	
			// AM_Jobtitle
			if (p_script.contains("AM_Jobtitle")) {
				ScriptableObject.putProperty(scope,"AM_Jobtitle", pyVars.get("AM_Jobtitle"));
				//ctx.setAttribute("AM_Jobtitle", AM_Jobtitle, ScriptContext.ENGINE_SCOPE);
			}
			// AM_Jobstation
			if (p_script.contains("AM_Jobstation")) {
				ScriptableObject.putProperty(scope,"AM_Jobstation", pyVars.get("AM_Jobstation"));
				//ctx.setAttribute("AM_Jobtitle", AM_Jobtitle, ScriptContext.ENGINE_SCOPE);
			}
			// AM_Jobunit
			if (p_script.contains("AM_Jobunit")) {
				ScriptableObject.putProperty(scope,"AM_Jobunit", pyVars.get("AM_Jobunit"));
				//ctx.setAttribute("AM_Jobunit", AM_Jobunit, ScriptContext.ENGINE_SCOPE);
			}
			// REC_InitDate
			if (p_script.contains("REC_InitDate")) {
				ScriptableObject.putProperty(scope,"REC_InitDate", pyVars.get("REC_InitDate"));
				//ctx.setAttribute("REC_InitDate", REC_InitDate, ScriptContext.ENGINE_SCOPE);
			}
			// REC_EndDate
			if (p_script.contains("REC_EndDate")) {
				ScriptableObject.putProperty(scope,"REC_EndDate", pyVars.get("REC_EndDate"));
				//ctx.setAttribute("REC_EndDate", REC_EndDate, ScriptContext.ENGINE_SCOPE);
			}
			// ACCT_Date
			if (p_script.contains("ACCT_Date")) {
				//log.warning("ACCT_Date="+ACCT_Date);
				ScriptableObject.putProperty(scope,"ACCT_Date", pyVars.get("ACCT_Date"));
				//ctx.setAttribute("ACCT_Date", ACCT_Date, ScriptContext.ENGINE_SCOPE);
			}
			// REF_InitDate
			if (p_script.contains("REF_InitDate")) {
				ScriptableObject.putProperty(scope,"REF_InitDate", pyVars.get("REF_InitDate"));
				//ctx.setAttribute("REF_InitDate", REF_InitDate, ScriptContext.ENGINE_SCOPE);
			}
			// REF_EndDate
			if (p_script.contains("REF_EndDate")) {
				ScriptableObject.putProperty(scope,"REF_EndDate", pyVars.get("REF_EndDate"));
				//ctx.setAttribute("REF_EndDate", REF_EndDate, ScriptContext.ENGINE_SCOPE);
			}	
			// AMN_downwardloads
			if (p_script.contains("AMN_downwardloads") ) {					
				// log.warning("p_script:"+p_script.trim()+"  AMN_downwardloads:"+AMN_downwardloads);
				ScriptableObject.putProperty(scope,"AMN_downwardloads", BigDecimal.valueOf(pyVars.get("AMN_downwardloads")));
				//ctx.setAttribute("NLUNES", BigDecimal.valueOf(NLUNES), ScriptContext.ENGINE_SCOPE);
			}
			// AMN_increasingloads
			if (p_script.contains("AMN_increasingloads") ) {					
				// log.warning("p_script:"+p_script.trim()+"  AMN_increasingloads:"+AMN_increasingloads);
				ScriptableObject.putProperty(scope,"NLUNES", BigDecimal.valueOf(pyVars.get("AMN_increasingloads")));
				//ctx.setAttribute("NLUNES", BigDecimal.valueOf(NLUNES), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Currency")) {
				ScriptableObject.putProperty(scope,"AM_Currency", pyVars.get("AM_Currency"));
				//ctx.setAttribute("AM_Currency", AM_Currency, ScriptContext.ENGINE_SCOPE);
			}
			//log.warning("p_script:"+p_script +"  AM_Process:"+AM_Process.trim()+"  AM_Contract:"+AM_Contract);			
			if (p_script.contains("UNIDADTRIBUTARIA")) {
				//log.warning("p_script:"+p_script +"  UNIDADTRIBUTARIA:"+UNIDADTRIBUTARIA);			
				ScriptableObject.putProperty(scope,"UNIDADTRIBUTARIA", BigDecimal.valueOf(pyVars.get("UNIDADTRIBUTARIA")));
				//ctx.setAttribute("UNIDADTRIBUTARIA", BigDecimal.valueOf(UNIDADTRIBUTARIA), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("TAXRATE")) {
				//log.warning("p_script:"+p_script +"  TAXRATE:"+TAXRATE+"  UNIDADTRIBUTARIA:"+UNIDADTRIBUTARIA);			
				ScriptableObject.putProperty(scope,"TAXRATE", BigDecimal.valueOf(pyVars.get("TAXRATE")));
				//ctx.setAttribute("TAXRATE", BigDecimal.valueOf(TAXRATE), ScriptContext.ENGINE_SCOPE);
			}
			//log.warning(".....................AmerpPayrollCalca.java...FormulaEvaluationScript...................................");
			//log.warning("DTREC:"+DTREC+"  HOLLIDAYS:"+HOLLIDAYS+"  NONLABORDAYS:"+NONLABORDAYS+"    LABORDAYS:"+LABORDAYS);				
			if (p_script.contains("LABORDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  LABORDAYS:"+LABORDAYS);
				ScriptableObject.putProperty(scope,"LABORDAYS", BigDecimal.valueOf(pyVars.get("LABORDAYS")));
				//ctx.setAttribute("LABORDAYS", BigDecimal.valueOf(LABORDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("HOLLIDAYS")) {					
				//log.warning("p_script:"+p_script.trim()+"  HOLLIDAYS:"+HOLLIDAYS);
				ScriptableObject.putProperty(scope,"HOLLIDAYS", BigDecimal.valueOf(pyVars.get("HOLLIDAYS")));
				//ctx.setAttribute("HOLLIDAYS", BigDecimal.valueOf(HOLLIDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NONLABORDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  NONLABORDAYS:"+NONLABORDAYS);
				ScriptableObject.putProperty(scope,"NONLABORDAYS", BigDecimal.valueOf(pyVars.get("NONLABORDAYS")));
				//ctx.setAttribute("NONLABORDAYS", BigDecimal.valueOf(NONLABORDAYS), ScriptContext.ENGINE_SCOPE);
			}
			// NEW VARIABLES
			if (p_script.contains("BUSINESSDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  BUSINESSDAYS:"+BUSINESSDAYS);
				ScriptableObject.putProperty(scope,"BUSINESSDAYS", BigDecimal.valueOf(pyVars.get("BUSINESSDAYS")));
				//ctx.setAttribute("BUSINESSDAYS", BigDecimal.valueOf(BUSINESSDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NONBUSINESSDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  NONBUSINESSDAYS:"+NONBUSINESSDAYS);
				ScriptableObject.putProperty(scope,"NONBUSINESSDAYS", BigDecimal.valueOf(pyVars.get("NONBUSINESSDAYS")));
				//ctx.setAttribute("NONBUSINESSDAYS", BigDecimal.valueOf(NONBUSINESSDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("WEEKENDDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  WEEKENDDAYS:"+WEEKENDDAYS);
				ScriptableObject.putProperty(scope,"WEEKENDDAYS", BigDecimal.valueOf(pyVars.get("WEEKENDDAYS")));
				//ctx.setAttribute("WEEKENDDAYS", BigDecimal.valueOf(WEEKENDDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NONWEEKENDDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  NONWEEKENDDAYS:"+NONWEEKENDDAYS);
				ScriptableObject.putProperty(scope,"NONWEEKENDDAYS", BigDecimal.valueOf(pyVars.get("NONWEEKENDDAYS")));
				//ctx.setAttribute("NONWEEKENDDAYS", BigDecimal.valueOf(NONWEEKENDDAYS), ScriptContext.ENGINE_SCOPE);
			}
			// NLUNES
			if (p_script.contains("NLUNES") ) {					
				// log.warning("p_script:"+p_script.trim()+"  NLUNES:"+NLUNES);
				ScriptableObject.putProperty(scope,"NLUNES", BigDecimal.valueOf(pyVars.get("NLUNES")));
				//ctx.setAttribute("NLUNES", BigDecimal.valueOf(NLUNES), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NMONDAY") ) {					
				//log.warning("p_script:"+p_script.trim()+"  NLUNES:"+NLUNES);
				ScriptableObject.putProperty(scope,"NMONDAY", BigDecimal.valueOf(pyVars.get("NMONDAY")));
				//ctx.setAttribute("NMONDAY", BigDecimal.valueOf(NMONDAY), ScriptContext.ENGINE_SCOPE);
			}
			// PUT FIXED VARIABLES VALUES--------
			if (p_script.contains("QTY_HND")) {
				ScriptableObject.putProperty(scope,"QTY_HND", BigDecimal.valueOf(pyVars.get("QTY_HND")));
				//ctx.setAttribute("QTY_HND", BigDecimal.valueOf(QTY_HND), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("QTY_HNN")) {
				ScriptableObject.putProperty(scope,"QTY_HNN", BigDecimal.valueOf(pyVars.get("QTY_HNN")));
				//ctx.setAttribute("QTY_HNN", BigDecimal.valueOf(QTY_HNN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("QTY_HED")) {
				ScriptableObject.putProperty(scope,"QTY_HED", BigDecimal.valueOf(pyVars.get("QTY_HED")));
				//ctx.setAttribute("QTY_HED", BigDecimal.valueOf(QTY_HED), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("QTY_HEN")) {
				ScriptableObject.putProperty(scope,"QTY_HEN", BigDecimal.valueOf(pyVars.get("QTY_HEN")));
				//ctx.setAttribute("QTY_HEN", BigDecimal.valueOf(QTY_HEN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HND")) {
				ScriptableObject.putProperty(scope,"RSU_HND", BigDecimal.valueOf(pyVars.get("RSU_HND")));
				//ctx.setAttribute("RSU_HND", BigDecimal.valueOf(RSU_HND), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HNN")) {
				ScriptableObject.putProperty(scope,"RSU_HNN", BigDecimal.valueOf(pyVars.get("RSU_HNN")));
				//ctx.setAttribute("RSU_HNN", BigDecimal.valueOf(RSU_HNN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HED")) {
				ScriptableObject.putProperty(scope,"RSU_HED", BigDecimal.valueOf(pyVars.get("RSU_HED")));
				//ctx.setAttribute("RSU_HED", BigDecimal.valueOf(RSU_HED), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HEN")) {
				ScriptableObject.putProperty(scope,"RSU_HEN", BigDecimal.valueOf(pyVars.get("RSU_HEN")));
				//ctx.setAttribute("RSU_HEN",  BigDecimal.valueOf(RSU_HEN), ScriptContext.ENGINE_SCOPE);
			}
			// AMN_Concept_Types_Limits CTLD
			if (p_script.contains("CTL_AmountAllocated")) {
				ScriptableObject.putProperty(scope,"CTL_AmountAllocated", BigDecimal.valueOf(pyVars.get("CTL_AmountAllocated")));
				//ctx.setAttribute("RSU_HEN",  BigDecimal.valueOf(RSU_HEN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("CTL_QtyTimes")) {
				ScriptableObject.putProperty(scope,"CTL_QtyTimes", BigDecimal.valueOf(pyVars.get("CTL_QtyTimes")));
				//ctx.setAttribute("RSU_HEN",  BigDecimal.valueOf(RSU_HEN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("CTL_Rate")) {
				ScriptableObject.putProperty(scope,"CTL_Rate", BigDecimal.valueOf(pyVars.get("CTL_Rate")));
				//ctx.setAttribute("RSU_HEN",  BigDecimal.valueOf(RSU_HEN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("CTL_QtyReceipts")) {
				ScriptableObject.putProperty(scope,"CTL_QtyReceipts", BigDecimal.valueOf(pyVars.get("CTL_QtyReceipts")));
				//ctx.setAttribute("RSU_HEN",  BigDecimal.valueOf(RSU_HEN), ScriptContext.ENGINE_SCOPE);
			}
			// ORGSECTOR
			if (p_script.contains("ORGSECTOR")) {
				ScriptableObject.putProperty(scope,"ORGSECTOR", pyVars.get("ORGSECTOR"));
				//ctx.setAttribute("ORGSECTOR", ORGSECTOR, ScriptContext.ENGINE_SCOPE);
			}
			// **********************************************************
			// PUT FIXED VARIABLES VALUES
			// Qty Starts with 				QT_
			// ResultValues starts with 	RS_
			// **********************************************************
			if (p_script.contains("QT_") || p_script.contains("RS_")) {
				String CTVar="",CTVar1="",CTVar2="";
				BigDecimal CTQty,CTRSValue;
				for (int index = 0; index < pyVars.getConIndex(); ++index)  {
					CTVar="";
					CTVar1="";
					CTVar2="";
					CTQty=BigDecimal.valueOf(0.00);
					CTRSValue=BigDecimal.valueOf(0.00);
					try {
	                    CTVar=pyVars.ConceptTypes[index].getConceptVariable();
	                    CTQty=pyVars.ConceptTypes[index].getQtyValue();
	                    CTRSValue=pyVars.ConceptTypes[index].getResultValue();
	                    CTVar1="QT_"+CTVar.trim();
						CTVar2="RS_"+CTVar.trim();
	               } catch (Exception ex) {
	            	   String msg = "Formula Script Invalid: " + ex.toString();
	                   log.log(Level.WARNING, msg, ex);
	                }
					// Verify if QT_SB
					if (CTVar1.equalsIgnoreCase("QT_SB")) {
						if (CTQty.doubleValue() == 0) {
							QT_SB=SBD;
							CTQty=BigDecimal.valueOf(SBD);
							ScriptableObject.putProperty(scope,"QT_SB", QT_SB);
							//ctx.setAttribute("QT_SB",  QT_SB, ScriptContext.ENGINE_SCOPE);
						} else {
							ScriptableObject.putProperty(scope,CTVar1, CTQty.doubleValue());
							//ctx.setAttribute(CTVar1, CTQty.doubleValue(), ScriptContext.ENGINE_SCOPE);
						}
					} else {
						if (!(CTVar.equalsIgnoreCase("")) || p_script.contains(CTVar1)) {
							//if (CTVar1.equalsIgnoreCase("QT_SALVACAC") || CTVar1.equalsIgnoreCase("QT_VACAC190") || CTVar1.equalsIgnoreCase("QT_VACAC192A") || CTVar1.equalsIgnoreCase("QT_VACACDIADIC"))
							//	log.warning("Concept_Reference:"+p_Concept_Reference+".....CTVar1 ... :"+CTVar1+"  CTQty.doubleValue():"+CTQty.doubleValue());
							ScriptableObject.putProperty(scope,CTVar1, CTQty.doubleValue());
							//ctx.setAttribute(CTVar1, CTQty.doubleValue(), ScriptContext.ENGINE_SCOPE);
						}
					}
					if (CTVar2.equalsIgnoreCase("RS_SB")) {
						if (CTRSValue.doubleValue() == 0) {
							pyVars.set("RS_SB",SBD);
							CTRSValue=BigDecimal.valueOf(SBD);
							ScriptableObject.putProperty(scope,"RS_SB", pyVars.get("RS_SB"));
							//ctx.setAttribute("RS_SB",  RS_SB, ScriptContext.ENGINE_SCOPE);
						} else {
							ScriptableObject.putProperty(scope,CTVar2, CTRSValue.doubleValue());
							//ctx.setAttribute(CTVar1, CTRSValue.doubleValue(), ScriptContext.ENGINE_SCOPE);
						}
					} else {
						if (!(CTVar.equalsIgnoreCase("")) || p_script.contains(CTVar2)) {
							ScriptableObject.putProperty(scope,CTVar2, CTRSValue.doubleValue());
							//ctx.setAttribute(CTVar2, CTRSValue.doubleValue(), ScriptContext.ENGINE_SCOPE);
						}
					}
				}
			}
			// **********************************************************
			// PUT FIXED RULES VALUES  AMN_Rules Table
			// RulesTypes ARRAY
			// ResultValues starts with 	RV_
			// **********************************************************
			if (p_script.contains("RV_")) {
				String RTRule="";
				BigDecimal RTRSValue;
				for (int index = 0; index < pyVars.RulIndex; ++index)  {
					RTRule="";
					RTRSValue=BigDecimal.valueOf(0.00);
					try {
						RTRule=pyVars.RulesTypes[index].getRuleVariable();
	                    RTRSValue=pyVars.RulesTypes[index].getResultValue();
					}
	                catch (Exception ex) {
	                    String msg = "AMN_Rules Script Invalid: " + ex.toString();
	                    log.log(Level.WARNING, msg, ex);
	                }
					//log.warning("index="+index+"  RTRule="+RTRule+"  RTRSValue="+RTRSValue);
					if (RTRSValue==null)
						RTRSValue=BigDecimal.valueOf(0.00);
					if (!(RTRule.equalsIgnoreCase("")) || p_script.contains(RTRule)) {
						ScriptableObject.putProperty(scope,RTRule, RTRSValue.doubleValue());
						//ctx.setAttribute(CTVar1, CTQty.doubleValue(), ScriptContext.ENGINE_SCOPE);
					}
				}
			}
			// *******************************************
			// VERIFY DefaultValue Class in All Default ARRAY
			// DVRulesTypes ARRAY
			// ALL DEFAULTS VALUES START WITH DV_ PREFIX
			// *******************************************
			if (p_script.contains("DV_")) {
				String DVRule="";
				BigDecimal DVRSValue;
				for (int index = 0; index < pyVars.DVRulIndex; ++index)  {
					DVRule="";
					DVRSValue=BigDecimal.valueOf(0.00);
					try {
						DVRule=pyVars.DVRulesTypes[index].getRuleVariable();
	                    DVRSValue=pyVars.DVRulesTypes[index].getResultValue();
					}
	                catch (Exception ex) {
	                    //ex.printStackTrace();
	                    String msg = "DV_XXXXX Script Invalid !! index="+index;
	                    log.warning(msg);
	                    log.log(Level.WARNING, msg, ex);
	                }
					//log.warning("index="+index+"  RTRule="+RTRule+"  RTRSValue="+RTRSValue);
					if (DVRSValue==null)
						DVRSValue=BigDecimal.valueOf(0.00);
					if (!(DVRule.equalsIgnoreCase("")) || p_script.contains(DVRule)) {
						ScriptableObject.putProperty(scope,DVRule, DVRSValue.doubleValue());
					}
				}
			}
			// ******************************************************
			// VERIFY LastValue Class in Formula
			// ALL LAST VALUES 	(QT_LASTVALUE OR LASTVALOE FOR QTY)
			//					(RS_LASTVALUE FOR RESULT)
			// ******************************************************
			BigDecimal QTYLastValueBD=BigDecimal.valueOf(1);
			BigDecimal RSLastValueBD=BigDecimal.valueOf(1);
			p_ctx = Env.getCtx();
			if (p_script.contains("LASTVALUE") ) {	
				// RESULT LAST VALUE
				if (p_script.contains("RS_LASTVALUE") ) {
					try {
						RSLastValueBD = AmerpPayrollCalcUtilDVFormulas.processLastValueQTY(p_ctx, amnpayroll.getAMN_Process_ID(), amnpayroll.getAMN_Contract_ID(),  p_AMN_Payroll_ID, AMN_Concept_Types_ID, amnpayroll.get_TrxName() );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (RSLastValueBD == null) {
						RSLastValueBD = BigDecimal.ZERO;
					}
					//log.warning("p_script:"+p_script.trim()+"  QT_LASTVALUE:"+QTYLastValueBD);
					//interprete.put("RS_LASTVALUE", RSLastValueBD.doubleValue());
					ScriptableObject.putProperty(scope,"RS_LASTVALUE", RSLastValueBD.doubleValue());
				}
				// QTY LAST VALUE
				if (p_script.contains("QT_LASTVALUE") || p_script.contains("LASTVALUE") ) {
					try {
						QTYLastValueBD = AmerpPayrollCalcUtilDVFormulas.processLastValueQTY(p_ctx, amnpayroll.getAMN_Process_ID(), amnpayroll.getAMN_Contract_ID(),  p_AMN_Payroll_ID, AMN_Concept_Types_ID, amnpayroll.get_TrxName() );
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (QTYLastValueBD == null) {
						QTYLastValueBD = BigDecimal.ZERO;
					}
					//log.warning("p_script:"+p_script.trim()+"  QT_LASTVALUE:"+QTYLastValueBD);
					ScriptableObject.putProperty(scope,"QT_LASTVALUE", QTYLastValueBD.doubleValue());
					//interprete.put("QT_LASTVALUE", QTYLastValueBD.doubleValue());
				}
			}
			// ***********************************
			// Calculate Value  from Formula
			// ***********************************
			try {
				//Result =  interprete.eval(p_script);
				//Result =  interprete.eval(p_script, ctx);
				//Result = interprete.eval(p_script);
				//Result = js.run();
				// Execute the script
				Result =  cx.evaluateString(scope, p_script, "p_script", 1, null);
				//log.warning("Result="+Result);
				// Integer
				if (Result instanceof Integer ) {
					Integer ResultI = (Integer) Result; 
					CalcAmnt = ResultI.doubleValue();
					//CalcAmnt = (double) Result;
				} 
				// Double
				if (Result instanceof Double ){
					CalcAmnt = (double) Result;
				}
				// Bigdecimal
				if (Result instanceof BigDecimal ){
					CalcAmnt= (Double) Result;
				}
				ErrorMessage="OK".trim();
				// CHANGE FOR IF CONDITIONS - CalcAmnt=  (double)  Result;
				//log.warning("CalcAmnt_OK:"+CalcAmnt);
			} catch (Exception e) {
				CalcAmnt = 0.00;
				ErrorMessage="*** ERROR ON FORMULA ***";
				String msg = "AMN_Concept_Types Script Invalid: " + 
						"Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+"\r\n"+
						"ScriptException p_script:"+p_script+"\r\n"+e.getMessage();
				log.log(Level.WARNING, msg, e);
				log.warning("Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim());
				log.warning("ScriptException p_script:"+p_script);
				//e.printStackTrace();
			}
			// Convert to BigDecimal
			BDCalcAmnt = BigDecimal.valueOf(CalcAmnt);
			// ROUND two Decimals
			//BDCalcAmnt= BDCalcAmnt.round(new MathContext(2, RoundingMode.CEILING));
			BDCalcAmnt = BDCalcAmnt.setScale(roundingMode, RoundingMode.HALF_UP);
			//return BDCalcAmnt;
			RetVal.setBDCalcAmnt(BDCalcAmnt);
			RetVal.setErrorMessage(ErrorMessage.trim());			
			
		} else {
			//return BigDecimal.valueOf(CalcAmnt);			
			ErrorMessage="Formula VACIA ó ERROR en Formula";
			RetVal.setBDCalcAmnt(BigDecimal.valueOf(CalcAmnt));
			RetVal.setErrorMessage(ErrorMessage.trim());	
		}
		return RetVal ;
	}  
	
}

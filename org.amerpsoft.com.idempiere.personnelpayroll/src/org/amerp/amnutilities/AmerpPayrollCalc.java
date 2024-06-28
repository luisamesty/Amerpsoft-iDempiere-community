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

package org.amerp.amnutilities;

import java.math.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import javax.script.ScriptException;

import org.amerp.amnmodel.*;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MActivity;
import org.compiere.model.MClientInfo;
//import org.compiere.model.MConversionType;
import org.compiere.model.MCurrency;
import org.compiere.model.MProject;
import org.compiere.model.MSysConfig;
import org.compiere.util.*;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.compiere.util.DB;

/**
 * @author luisamesty
 * Payroll Calc Methods for Personnel Plugin
 * 
 */

public class AmerpPayrollCalc  {
	// FORMULA DOUBLE VARIABLES
	public static Double R_ASIG, R_DEDUC, R_TOTAL ;
	public static Double RA_ASIG, RA_DEDUC, RA_TOTAL, RE_ASIG, RE_DEDUC, RE_TOTAL;	
	public static Double WorkingDaysDT=0.00;
	public static Double QT_SB=0.00;
	public static Double RS_SB=0.00;
	public static Double ZEROPLUS=0.00;
	public static Double DIAS,HORAS,PNRM,DT,DTREC,DTPER,DTOK,NONLABORDAYS,LABORDAYS,HOLLIDAYS;
	public static Double BUSINESSDAYS, NONBUSINESSDAYS, WEEKENDDAYS, NONWEEKENDDAYS;
	public static Double NLUNES,NMONDAY;
	public static Double LASTVALUE;
	public static Double UNIDADTRIBUTARIA,SBMIN,TAXRATE;
	public static Double QTY_HND,QTY_HNN,QTY_HED,QTY_HEN;
	public static Double RSU_HND,RSU_HNN,RSU_HED,RSU_HEN;
	public static String IS_FAOV,IS_SALARIO,IS_INCES,IS_SSO,IS_ARC, IS_SPF, IS_DESCANSO, IS_FERIADO,IS_UTILIDAD,IS_PRESTACION, IS_VACACION = "N";
	public static String AM_Contract="";
	public static String AM_Process="";
	// Employee Variables
	public static String AM_Shift ="";
	public static String AM_PayrollMode="";
	public static String AM_Status ="";
	public static String AM_IncomeDate="";
	public static String AM_PaymentType="";
	public static String AM_CivilStatus="";
	public static String AM_Sex="";
	public static String AM_Spouse="";
	public static String AM_IsPensioned="";
	public static String AM_IsStudyng="";
	public static String AM_IsMedicated="";
	public static String AM_BirthDate="";
	public static String REC_InitDate="";
	public static String REC_EndDate="";
	public static String ACCT_Date="";
	public static String REF_InitDate="";
	public static String REF_EndDate="";
	// Work Force (M.A.D.I.S), Department Value, Location Value, Project Value, 
	// Activity Value, Jobtitle Value, Jobstation Value
	public static String AM_Workforce="A";
	public static String AM_Department="";
	public static String AM_Location="";
	public static String AM_Project="";
	public static String AM_Activity="";
	public static String AM_Jobtitle="";
	public static String AM_Jobstation="";
	public static String AM_Jobunit="";
	
	public static String AM_Currency="";
	//public static AmerpQtyResultConcepts  ConceptTypes  = new  AmerpQtyResultConcepts(null);
	public static Double R_FAOV,R_SALARIO,R_INCES,R_SSO,R_ARC,R_SPF, R_DESCANSO,R_FERIADO,R_UTILIDAD,R_VACACION, R_PRESTACION ;
	// Cummulated Values for TAX and SSO  (TAX == ARC)
	public static Double RA_TAX, RD_TAX, RA_SSO, RD_SSO; 
	public static int ConIndex=0,ConIndexFT=0;
	static public AmerpConceptTypes[] ConceptTypes = new AmerpConceptTypes[500];
	public static int RulIndex=0,RulIndexFT=0;
	static public AmerpRulesTypes[] RulesTypes = new AmerpRulesTypes[50];
	public static int DVRulIndex=0,DVRulIndexFT=0;
	static public AmerpRulesTypes[] DVRulesTypes = new AmerpRulesTypes[50];
	// listMemoryVariables  Memory Variables Array (futute use)
	public static List<memoryVars> listMemoryVariables = new ArrayList<memoryVars>();
	
	static CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);

	
	/*******************************************************************************************
   	 * Payroll Evaluation using Script Engine Manager
	 * Parameters:
	 * 	Properties p_ctx
	 * 	int p_AMN_Payroll_ID
	 *  int p_calcorder ( Calculate all concepts until calcorder)
	 *  boolean forceRulesInit 
	 *  boolean forceDVInit
	 *  (Force Rules and Vars Init, usually true on first payroll 
	 *  	row receipt only)   ** DISABLED BY MOMENT ***
	 * Notes: This Method is Used by PayrollDetail Callout on Concept_Types_Proc Value
	 *******************************************************************************************/
	public static BigDecimal PayrollEvaluation 	(Properties p_ctx, int p_AMN_Payroll_ID , 
			int p_calcorder, boolean forceRulesInit, boolean forceDVInit) //throws ScriptException
	{
		//log.warning(".PayrollEvaluation..Init forceRulesInit="+forceRulesInit+"  p_calcorder="+p_calcorder+"...............................");
		//log.warning("Parameters: p_AMN_Payroll_ID="+p_AMN_Payroll_ID+"  p_calcorder="+p_calcorder);	
		Integer CalcOrder;
		String ConceptOptmode,ConceptSign,ConceptIsshow="";	
		String Concept_Reference="";
		String Concept_Variable="";
		BigDecimal.valueOf(0);
		Integer Employee_ID=0,Period_ID=0,Process_ID=0,Contract_ID=0;
		Integer AD_Client_ID=0,AD_Org_ID=0,C_Currency_ID=0;
		Timestamp AMNPayrollStartDate,AMNPayrollEndDate;
		String IS_FAOV,IS_SALARIO,IS_INCES,IS_SSO,IS_ARC, IS_SPF, IS_FERIADO,IS_UTILIDAD,IS_PRESTACION, IS_VACACION = "N";
		Double QtyValue, AmountAD, AmountAllocated, AmountDeducted, AmountCalculated;
		String dbType = "PostgreSQL";	// "PostgreSQL, Oracle or UnKnown
		//PayrollEvaluationArray(p_ctx, p_AMN_Payroll_ID);
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		Employee_ID = amnpayroll.getAMN_Employee_ID();
		Period_ID = amnpayroll.getAMN_Period_ID();
		Process_ID = amnpayroll.getAMN_Process_ID();
		Contract_ID = amnpayroll.getAMN_Contract_ID();
    	AD_Client_ID=amnpayroll.getAD_Client_ID();
    	AD_Org_ID=amnpayroll.getAD_Org_ID();
    	C_Currency_ID=amnpayroll.getC_Currency_ID();
		// Default Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), amnpayroll.getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
//		int C_AcctSchema_ID= as.getC_AcctSchema_ID();
   		// Default ConversionType for Contract
   		Integer C_ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(amnpayroll.getAMN_Contract_ID());
//   		if (C_ConversionType_ID == null)	
//   			C_ConversionType_ID = MConversionType.TYPE_SPOT;
		// Init Rules
   		// Executes only one time per payroll Receipt i forceRulesInit=true
    	if (forceRulesInit)
    		RTInit(AD_Client_ID, p_AMN_Payroll_ID, Process_ID);
		// Init DV_XXXX Variables if forceDVInit=true
    	if(forceDVInit) {
    		DVInit(AD_Client_ID,p_AMN_Payroll_ID, Process_ID);
    		ConIndexFT = 0;   // Force VariablesInit procedure to force init array 
    	}
//    	// Variables allways init
   		VariablesInit(AD_Client_ID);
    	new MAMN_Employee(p_ctx, Employee_ID, null);
    	//amnperiod.getAMNDateIni();
    	//amnperiod.getAMNDateEnd(); 	
    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_ID, null);
    	//amncontract.getValue();
    	// SET FIXED VARIABLES UNIDADTRIBUTARIA & SBMIN & TAXRATE
    	// Executes only one time per payroll Receipt
//    	if (forceRulesInit) {
    		//log.warning(".......Call VariablesGetSQLValues......");
    		VariablesGetSQLValues(p_AMN_Payroll_ID, C_Currency_ID, C_ConversionType_ID, AD_Client_ID,  AD_Org_ID);
//    	}
    	// SET FIXED VARIABLE NONLABORDAYS Double
		AMNPayrollStartDate=amnpayroll.getInvDateIni();
		AMNPayrollEndDate=amnpayroll.getInvDateEnd();
		HOLLIDAYS = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate, AD_Client_ID, AD_Org_ID).doubleValue();
	    // SET FIXED VARIABLES LABORDAYS,HOLLIDAYS,DTREC Double
		LABORDAYS =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate, AD_Client_ID, null).doubleValue();
		DTREC =1.00+ MAMN_NonBusinessDay.getDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate).doubleValue();
		NONLABORDAYS = DTREC - LABORDAYS;
		// SET OTHER FIXED VARIABLES BUSINESSDAYS, NONBUSINESSDAYS, WEEKENDDAYS, NONWEEKENDDAYS;
		BUSINESSDAYS=MAMN_NonBusinessDay.sqlGetBusinessDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate, AD_Client_ID, AD_Org_ID).doubleValue();
		NONBUSINESSDAYS=MAMN_NonBusinessDay.sqlGetNonBusinessDayBetween(AMNPayrollStartDate, AMNPayrollEndDate, AD_Client_ID, AD_Org_ID).doubleValue();
		WEEKENDDAYS=MAMN_NonBusinessDay.sqlGetWeekEndDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate, AD_Client_ID, null).doubleValue();
		NONWEEKENDDAYS=MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate, AD_Client_ID, null).doubleValue();
		// SET FIXED VARIABLE DTPER (PayrollDays from Contract)
		DTPER = amncontract.getPayRollDays().doubleValue();
		DTOK=DTREC; // Alternative
	    // SET FIXED VARIABLES NLUNES,NMONDAY
		NLUNES = AmerpDateUtils.getWeekDaysBetween(2,AMNPayrollStartDate, AMNPayrollEndDate).doubleValue();
		NMONDAY = AmerpDateUtils.getWeekDaysBetween(2,AMNPayrollStartDate, AMNPayrollEndDate).doubleValue();
		// Payroll Detail Array
		// Verify Database Type
		if ( DB.isPostgreSQL() ) {
			dbType = "PostgreSQL";
		} else if ( DB.isPostgreSQL() ) {
			dbType = "Oracle";
		} else {
			dbType = "UnKnown";
			return null ;
		}
    	String sql1 = "SELECT " 
                +	"pad.qtyvalue, "
                +	"cty.formula,  "
                +	"ctp.name as conceptname, "
                +	"cty.calcorder as calcorder, "
                +	"cty.faov as isfaov, "
                +	"coalesce(cty.feriado,'N') as isferiado, "
                +	"coalesce(cty.ince,'N') as isince, " 
                +	"coalesce(cty.prestacion,'N') as isprestacion, "
                +	"coalesce(cty.salario,'N') as issalario, "
                +	"coalesce(cty.sso,'N') as issso, " 
                +	"coalesce(cty.spf,'N') as isspf, "
                +	"coalesce(cty.arc,'N') as isarc, "
                +	"coalesce(cty.vacacion,'N') as isvacacion, " 
                +	"coalesce(cty.utilidad,'N') as isutilidad, "
                +	"pay.invdateini, "
                +	"pay.invdateend, " 
                +	"CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
                +	"cum.value as cuom, "
                +	"cty.optmode as coptmode, "
                +	"cty.sign as csign, "
                +	"cty.rule as crule, "
                +	"cty.isrepeat as isrepeat, "
                +	"cty.isshow as isshow, "
                +	"pad.amountallocated, "
                +	"pad.amountdeducted, "
                +	"cty.value as creference, "
                +	"coalesce(cty.variable,cty.value,'') as cvariable, "
                +	"pad.amountcalculated "
                +	"FROM amn_payroll as pay "
                +	"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " 
                +	"LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) " 
                +	"LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) " 
                +	"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
                +	"WHERE pay.amn_payroll_id = ? "
                +	" AND cty.calcorder < ? " 
                +	"ORDER BY cty.calcorder";
	    
	    PreparedStatement pstmte = null;
	    ResultSet rse = null;
	    try
	    {
	    		pstmte = DB.prepareStatement(sql1, null);
	            pstmte.setInt(1, p_AMN_Payroll_ID);
	            pstmte.setInt(2, p_calcorder);
	            rse = pstmte.executeQuery();	            
	            // Reference Concepts Only
	            while (rse.next()) {
	            	QtyValue= rse.getDouble(1);
	                rse.getString(2);
	                rse.getString(3);
	                CalcOrder = rse.getInt(4);
	                IS_FAOV = rse.getString(5);
	                IS_FERIADO = rse.getString(6);
	                IS_INCES = rse.getString(7);
	                IS_PRESTACION = rse.getString(8);
	                IS_SALARIO = rse.getString(9);
	                IS_SSO = rse.getString(10);
	                IS_SPF = rse.getString(11);
	                IS_ARC = rse.getString(12);
	                IS_VACACION = rse.getString(13);
	                IS_UTILIDAD = rse.getString(14);
	                rse.getDate(15);
	                rse.getDate(16);
	                ConceptOptmode = rse.getString(19);
	                ConceptSign = rse.getString(20);
	                rse.getString(21);
	                rse.getString(22);
	                ConceptIsshow = rse.getString(23);
	                AmountAllocated=rse.getDouble(24);
	                AmountDeducted=rse.getDouble(25);
	                Concept_Reference = rse.getString(26);
	                Concept_Variable = rse.getString(27).trim();
	                AmountCalculated=rse.getDouble(28);
	                // Reference Concepts DIAS,HORAS,PNRM (Only)
                	if (ConceptOptmode.equalsIgnoreCase("R")) {
	            		AmountAD = AmountCalculated;
	        			if (Concept_Reference.equalsIgnoreCase("DIAS")) {  
	        				DIAS = QtyValue;  //BigDecimal.valueOf(QtyValue);
	        				DT = DIAS;
	        				WorkingDaysDT=QtyValue;
	        				BigDecimal.valueOf(QtyValue);
	        			}         
	        			if (Concept_Reference.equalsIgnoreCase("HORAS")) {  
	        				HORAS = QtyValue;  //BigDecimal.valueOf(QtyValue);
	        			}         
	        			if (Concept_Reference.equalsIgnoreCase("PNRM")) {  
	        				PNRM = QtyValue;  //BigDecimal.valueOf(QtyValue);
	        			}         
            		} 
	                // Evaluate CalcOrder less than p_calcorder
	                // For Avoid Circular References
	                if (CalcOrder <= p_calcorder) {
	                	//log.warning("Concept_Name:"+Concept_Name+"  DT:"+DT);
	                	// TOTAL ASSING DEDUC ALLLOCATED DEDUCTED ACCUMULATION
	                	R_ASIG = R_ASIG + AmountAllocated;
	                	R_DEDUC= R_DEDUC + AmountDeducted;
	                	R_TOTAL = R_TOTAL + AmountAllocated - AmountDeducted;
	                	// RA_XXXX ACCOUNTING ALLLOCATED DEDUCTED ACCUMULATION
	                	// RE_XXXX Employee Payroll Receipt ALLLOCATED DEDUCTED ACCUMULATION
	                	if (ConceptIsshow.equalsIgnoreCase("Y")) {
		                	RE_ASIG = RE_ASIG + AmountAllocated;
		                	RE_DEDUC= RE_DEDUC + AmountDeducted;
		                	RE_TOTAL = RE_TOTAL + AmountAllocated - AmountDeducted;
	                	} else {
		                	RA_ASIG = RA_ASIG + AmountAllocated;
		                	RA_DEDUC= RA_DEDUC + AmountDeducted;
		                	RA_TOTAL = RA_TOTAL + AmountAllocated - AmountDeducted;	                		
	                	}
	                	// REST OF ALLLOCATED VARIABLES ACCUMULATION
	                	if (ConceptOptmode.equalsIgnoreCase("R")) {
	                		AmountAD = AmountCalculated;
	                	} else {
		                	if (ConceptSign.equalsIgnoreCase("D")) {
		                		AmountAD = AmountAllocated;
		                	} else {
		                		// Accumulate Value for Credits too
		                		AmountAD = AmountDeducted; //(Double) 0.00;
		                	}
	                	}
	                	/* Change on Business Logic
	                	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
	                	 * Variable R_FAOV,R_SALARIO,R_INCES,R_SSO,R_ARC,R_SPF, R_DESCANSO,R_FERIADO,R_UTILIDAD,R_VACACION, R_PRESTACION
						 * Only cummulates on Allocation
	                	 */
	                	if (IS_FAOV.equalsIgnoreCase("Y")) { 
            				R_FAOV =  R_FAOV  + AmountAllocated;
            			}
	                	if (IS_FERIADO.equalsIgnoreCase("Y")) {  
            				R_FERIADO = R_FERIADO + AmountAllocated;
            				R_DESCANSO = R_DESCANSO + AmountAllocated;
            			}	                	
	                	if (IS_INCES.equalsIgnoreCase("Y")) {  
            				R_INCES = R_INCES + AmountAllocated;
            			}
	                	if (IS_PRESTACION.equalsIgnoreCase("Y")) {  
            				R_PRESTACION = R_PRESTACION + AmountAllocated;
            			}
	                	if (IS_SALARIO.equalsIgnoreCase("Y")) {  
            				R_SALARIO = R_SALARIO + AmountAllocated;
            			}
	                	if (IS_SSO.equalsIgnoreCase("Y")) {  
            				R_SSO = R_SSO + AmountAllocated;
            			}
	                	if (IS_SPF.equalsIgnoreCase("Y")) {  
            				R_SPF = R_SPF + AmountAllocated;
            			}
	                	if (IS_ARC.equalsIgnoreCase("Y")) {  
            				R_ARC = R_ARC + AmountAllocated;
            			}
	                	if (IS_VACACION.equalsIgnoreCase("Y")) {  
            				R_VACACION = R_VACACION + AmountAllocated;
            			}
	                	if (IS_UTILIDAD.equalsIgnoreCase("Y")) {  
            				R_UTILIDAD = R_UTILIDAD + AmountAllocated;
            			}
	                	/* Change on Business Logic
	                	 * Variable RA_TAX, RD_TAX, RA_SSO, RD_SSO
	                	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
						 * Cummulates on Allocation and Deduction Depending on Sign
	                	 */
	                	if (IS_ARC.equalsIgnoreCase("Y")) {  
	                		RA_TAX = RA_TAX + AmountAllocated;
	                		RD_TAX = RD_TAX + AmountDeducted;
            			}
	                	if (IS_SSO.equalsIgnoreCase("Y")) {  
            				RA_SSO = RA_SSO + AmountAllocated;
            				RD_SSO = RD_SSO + AmountAllocated;
            			}
	                	// FIXED CONCEPT VARIABLES
	                	if (Concept_Variable.equalsIgnoreCase("HNN")) {
                        	QTY_HNN=QtyValue ;
                        	RSU_HNN= AmountAD;
	                	}
	                	if (Concept_Variable.equalsIgnoreCase("HND")) {
                        	QTY_HND=QtyValue ;
                        	RSU_HND= AmountAD;
	                	}	                
	                	if (Concept_Variable.equalsIgnoreCase("HED")) {
                        	QTY_HED=QtyValue ;
                        	RSU_HED= AmountAD;
	                	}
	                	if (Concept_Variable.equalsIgnoreCase("HEN")) {
                        	QTY_HEN=QtyValue ;
                        	RSU_HEN= AmountAD;
	                	}
	                	// ************************************
	                	// QT_XXXXX AND RS_XXXXXX
	                	// DYNAMIC CONCEPT VARABLES ON ARRAY
	                	// ************************************
						//if (Concept_Variable.compareToIgnoreCase("RV_FVACAC190")==0) {
						//	log.warning(".....Concept_Variable ... :"+Concept_Variable+"   TrxName:"+amnpayroll.get_TrxName()+"  p_AMN_Payroll_ID:"+p_AMN_Payroll_ID);
						//	}
	    				int index=CTcontains(Concept_Variable);
	    				if (index > 0) {
	    					CTupdateConceptResults(Concept_Variable, BigDecimal.valueOf(QtyValue), BigDecimal.valueOf(AmountAD), "");
	    					//log.warning(".....Concept_Variable Array UPDATE... :"+Concept_Variable+"  QtyValue="+QtyValue+"  AmountAD="+AmountAD);
	    				} else {
	    					CTaddConceptResults(CalcOrder, Concept_Variable, BigDecimal.valueOf(QtyValue), BigDecimal.valueOf(AmountAD), "Add by PayrollEvaluation ..");
	    					//log.warning(".....Concept_Variable Array ADD   ... :"+Concept_Variable+"  QtyValue="+QtyValue+"  AmountAD="+AmountAD);
	    				}
                	}
               	}
	    }
	    catch (SQLException e)
	    {
	    	//log.warning("catch");
	    	QtyValue= 0.00;
	    	CalcOrder=0;
	    }
	    finally
	    {
	            DB.close(rse, pstmte);
	            rse = null; 
	            pstmte = null;
	    }
	    //log.warning("...........AmerpPayrollCalc.java...PayrollEvaluation..Finish."+"  p_calcorder="+p_calcorder+"................................");
		return null;

	}
	
	
	/*******************************************************************************************
   	// Payroll Evaluation Array Generate Array & Evaluate 
	// Parameters:
	// 	p_ctx
	// 	p_AMN_Payroll_ID
	//  Notes: this Method is Used by AMNPayrollRefresh Process Called from Payroll Preparing
	//			Window 
	//  Also Called by  PayrollDetail Callout on Concept_Types_Proc Value
	//  Also Called by Event AMN Payroll event
	//***************************************************************************************/
	
	public static void PayrollEvaluationArrayCalculate (Properties p_ctx, int p_AMN_Payroll_ID ) throws ScriptException
	{
		CLogger.getCLogger(AmerpPayrollCalc.class);
		//log.warning("Parameters: p_AMN_Payroll_ID="+p_AMN_Payroll_ID);	
		scriptResult RetVal = new scriptResult();
		String ErrorMessage="";
		int AD_Client_ID;
		BigDecimal calcAmnt,calcAmntDR,calcAmntCR,calcAmntNet;
        String Concept_Variable;
        String Concept_Script;
        int Payroll_Detail_ID;
        Integer CalcOrder,WorkingDays;
        BigDecimal va_SB;
        String sql1="",sql2="",sql3="";
        String ConceptOptmode,ConceptSign,ConceptIsshow="";	
        String Formula,Concept_Reference="";
        BigDecimal WorkingDaysBD=BigDecimal.valueOf(0);
        Integer Period_ID=0,Process_ID=0,Contract_ID=0,Payroll_ID=0;
        Double QtyValue;
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		// AMN_Employee 
		//amnpayroll.getAMN_Employee_ID();
		MAMN_Employee employee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), null);
		Period_ID = amnpayroll.getAMN_Period_ID();
		Process_ID = amnpayroll.getAMN_Process_ID();
		Payroll_ID = amnpayroll.getAMN_Payroll_ID();
		AD_Client_ID=amnpayroll.getAD_Client_ID();
        // Init Variables
		VariablesInit(AD_Client_ID);	
    	// AMN_Period
    	MAMN_Period amnperiod = new MAMN_Period(p_ctx, Period_ID, null);
    	amnperiod.getAMNDateIni();
    	amnperiod.getAMNDateEnd(); 	
    	new MAMN_Process(p_ctx, Process_ID, null);
    	//Process_Value = amnprocess.getValue().trim();
    	// Initialize Total DR CR
    	calcAmntDR=BigDecimal.valueOf(0);
    	calcAmntCR=BigDecimal.valueOf(0);
    	calcAmntNet=BigDecimal.valueOf(0);
    	// ***************************
	    // * Get Employee´s Salary SB
	    // ***************************
    	va_SB = employee.getSalary();
    	// Payroll Detail Array
    	sql2 = "SELECT " 
                +	"pad.qtyvalue, "
                +	"cty.formula,  "
                +	"ctp.name as conceptname, "
                +	"cty.calcorder as calcorder, "
                +	"cty.faov as isfaov, "
                +	"coalesce(cty.feriado,'N') as isferiado, "
                +	"coalesce(cty.ince,'N') as isince, " 
                +	"coalesce(cty.prestacion,'N') as isprestacion, "
                +	"coalesce(cty.salario,'N') as issalario, "
                +	"coalesce(cty.sso,'N') as issso, " 
                +	"coalesce(cty.spf,'N') as isspf, "
                +	"coalesce(cty.arc,'N') as isarc, "
                +	"coalesce(cty.vacacion,'N') as isvacacion, " 
                +	"coalesce(cty.utilidad,'N') as isutilidad, "
                +	"pay.invdateini, "
                +	"pay.invdateend, " 
                +	"CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
                +	"cum.value as cuom, "
                +	"cty.optmode as coptmode, "
                +	"cty.sign as csign, "
                +	"cty.rule as crule, "
                +	"cty.isrepeat as isrepeat, "
                +	"cty.isshow as isshow, "
                +	"pad.amountallocated, "
                +	"pad.amountdeducted, "
                +	"cty.value as creference, "
    			+	"coalesce(cty.variable,cty.value,'') as cvariable, "
    			+	"coalesce(cty.script,'') as script, "
                +	"pay.amn_payroll_id, "
                +	"pad.amn_payroll_detail_id "
    			+	"FROM amn_payroll as pay "
                +	"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " 
                +	"LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) " 
                +	"LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) " 
                +	"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
                +	"WHERE pay.amn_payroll_id = ? "
                +	" AND cty.calcorder < 999999999 " 
                +	"ORDER BY cty.calcorder";
	    
    	PreparedStatement pstmtc = null;
	    ResultSet rsc = null;
	    try
	    {
    		pstmtc = DB.prepareStatement(sql2, null);
            pstmtc.setInt(1, p_AMN_Payroll_ID);

            rsc = pstmtc.executeQuery();	            
            // Reference Concepts Only
            while (rsc.next()) {
            	QtyValue= rsc.getDouble(1);
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
            	WorkingDaysBD= BigDecimal.valueOf((double) WorkingDays);
            	//log.warning("Row:"+rsc.getRow()+ "......antes .....");
            	// Force Init Rules Variables and SQL Variables only for first Row
    			boolean forceRulesInit=false;
    			boolean forceDVInit=false;
    			// Verify if first row on receipt
            	if ( rsc.getRow() == 1) {
                	// MSysConfig AMERP_Payroll_Rules_Apply 
        			String apra = MSysConfig.getValue("AMERP_Payroll_Rules_Apply","N",amnpayroll.getAD_Client_ID());
//log.warning("AMERP_Payroll_Rules_Apply = "+apra);
        			if (apra.compareToIgnoreCase("Y")==0)
        				forceRulesInit=true;
        			else
        				forceRulesInit=false;
            		forceDVInit=true;
            	} else { 
            		forceRulesInit=false;
            		forceDVInit=false;
            	}
    			AmerpPayrollCalc.PayrollEvaluation(p_ctx,Payroll_ID,CalcOrder, forceRulesInit, forceDVInit);
            	// CALCULATE VALUE from Concept_Script
                if (Formula.trim().equalsIgnoreCase("script") || ((!Concept_Script.isEmpty() && Concept_Script!=null))) {
       				RetVal = AmerpPayrollCalc.FormulaEvaluationScript(Payroll_ID, Concept_Reference, Concept_Script, BigDecimal.valueOf(QtyValue), va_SB, WorkingDaysBD,ConceptOptmode);
       				ErrorMessage = RetVal.getErrorMessage();
       				calcAmnt = RetVal.getBDCalcAmnt();
       			// CALCULATE Value FROM Formula
       			} else {	       				
       				RetVal = AmerpPayrollCalc.FormulaEvaluationScript(Payroll_ID, Concept_Reference, Formula, BigDecimal.valueOf(QtyValue), va_SB, WorkingDaysBD,ConceptOptmode);
       				ErrorMessage = RetVal.getErrorMessage();
       				calcAmnt = RetVal.getBDCalcAmnt();
       			}
                //log.warning("Row:"+rsc.getRow()+"...OptMode="+ConceptOptmode+"  CalcOrder="+CalcOrder+"  ConceptOptmode:"+ConceptOptmode+"  va_SB:"+va_SB+"  QtyValue:"+QtyValue+"  WorkingDaysBD"+WorkingDaysBD+" calcAmnt:"+calcAmnt+"...........");
				//if (ConceptOptmode.compareToIgnoreCase("R") == 0  || 
				//Concept_Variable.compareToIgnoreCase("UTILFRAC") == 0){
				//	log.warning(".....PayrollEvaluationArrayCalculate..Finish...."+"  CalcOrder="+CalcOrder+"  ConceptOptmode:"+ConceptOptmode+"  va_SB:"+va_SB+"  QtyValue:"+QtyValue+"  WorkingDaysBD"+WorkingDaysBD+" calcAmnt:"+calcAmnt+".....");
				//	}
				//if (BigDecimal.valueOf(QtyValue).compareTo(BigDecimal.ZERO) != 0 ||
				//		calcAmnt.compareTo(BigDecimal.ZERO) != 0 ) {
				//log.warning("Concepto="+CalcOrder+" "+Concept_Reference+" "+" QTY="+QtyValue+" Result="+ calcAmnt+"\r\n") ;
				//}
        		CTaddConceptResults(CalcOrder, Concept_Variable, BigDecimal.valueOf(QtyValue), calcAmnt, ErrorMessage);
				//	Update AMN_Payroll_Detail - Invoice Line
				// Verify is ConceptType Applies on Results 
        		// FIRTS VERIFY  if Reference
                if (ConceptOptmode.equalsIgnoreCase("R")) {
						//+ " set amountcalculated= "+calcAmnt+","                	
						sql3 = "UPDATE AMN_Payroll_Detail "
								+ " set amountcalculated= "+calcAmnt+","                	
								+ " amountallocated= 0,"
								+ " amountdeducted=0 "
								+ " where amn_payroll_detail_id ="+Payroll_Detail_ID;
						// ALLOCATION
						//calcAmntDR=calcAmntDR.add(calcAmnt);
                } else {
	                if (ConceptIsshow.equalsIgnoreCase("N")) {
						if (ConceptSign.equalsIgnoreCase("D")) {
							sql3 = "UPDATE AMN_Payroll_Detail "
									+ " set amountcalculated= "+calcAmnt+","
									+ " amountallocated= 0,"
									+ " amountdeducted=0 "
									+ " where amn_payroll_detail_id ="+Payroll_Detail_ID;
							// ALLOCATION
							//calcAmntDR=calcAmntDR.add(calcAmnt);
					    	
	                	} else {
	                		sql3 = "UPDATE AMN_Payroll_Detail "
	    							+ " set amountcalculated= "+calcAmnt+","
	    							+ " amountallocated=0 ,"
	    							+ " amountdeducted=0 "
	    							+ " where amn_payroll_detail_id ="+Payroll_Detail_ID;
							// DEDUCTION
							//calcAmntCR=calcAmntCR.add(calcAmnt);
	                	}
					} else {
						if (ConceptSign.equalsIgnoreCase("D")) {
							sql3 = "UPDATE AMN_Payroll_Detail "
									+ " set amountcalculated= "+calcAmnt+","
									+ " amountallocated= "+calcAmnt+","
									+ " amountdeducted=0 "
									+ " where amn_payroll_detail_id ="+Payroll_Detail_ID;
							// ALLOCATION
							calcAmntDR=calcAmntDR.add(calcAmnt);
					    	
	                	} else {
	                		sql3 = "UPDATE AMN_Payroll_Detail "
	    							+ " set amountcalculated= "+calcAmnt+","
	    							+ " amountallocated=0 ,"
	    							+ " amountdeducted="+calcAmnt
	    							+ " where amn_payroll_detail_id ="+Payroll_Detail_ID;
							// DEDUCTION
							calcAmntCR=calcAmntCR.add(calcAmnt);
	                	}
					}
                }
				DB.executeUpdateEx(sql3, null);
				//log.warning(".....PayrollEvaluationArrayCalculate.."+"  CalcOrder="+CalcOrder+"  ConceptOptmode:"+ConceptOptmode+"  QtyValue:"+QtyValue+"  calcAmnt"+calcAmnt+"..");
            }
            // Update AMN_Payroll (HEADER VALUES)
            calcAmntNet=BigDecimal.valueOf(0);
            calcAmntNet = calcAmntNet.add(calcAmntDR);
            calcAmntNet = calcAmntNet.subtract(calcAmntCR);
            sql3 = "UPDATE AMN_Payroll "
            		+ " set amountnetpaid="+calcAmntNet+","
					+ " amountallocated="+calcAmntDR+","
					+ " amountdeducted="+calcAmntCR
					+ " where amn_payroll_id ="+p_AMN_Payroll_ID;
            DB.executeUpdateEx(sql3, null);
	    }
	    catch (SQLException e)
	    {
	    	//log.warning("catch");
	    	QtyValue= 0.00;
	    	Formula = "" ;
	    	CalcOrder=0;
	    }
	    finally
	    {
	            DB.close(rsc, pstmtc);
	            rsc = null; pstmtc = null;
	    }
	    // Show Variables
	    // logVariablesShow();
	    // log.warning("...Rules at End ...");
 		// AmerpPayrollCalc.logRulesShow();
	} // PayrollEvaluationArrayCalculate

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
	//
	// 	Returns: scriptResult RetVal 
	//				(BigDecimal) BDCalcAmnt
	// 				String ErrorMessage
	// 
	// All Variables and Calculation are Made with  Double Class 
   	//***********************************************************************/
	public static scriptResult FormulaEvaluationScript 	(
			int p_AMN_Payroll_ID,
			String p_Concept_Reference, String p_script, BigDecimal p_qtyValueRead, 
			BigDecimal p_va_SB, BigDecimal p_workdays, String p_OptMode ) throws ScriptException
	{
		CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// Script Engine Manager
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine interprete = manager.getEngineByName("javascript");
		// Rhino Context
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		Object Result= null;
		// Calc Value
		double CND = p_qtyValueRead.doubleValue();
		double SBD = p_va_SB.doubleValue();
		QT_SB=SBD;
		double WorkDays = p_workdays.doubleValue(); // The double value of p_workdays
		Double CalcAmnt=0.00;
		BigDecimal BDCalcAmnt;
		String ErrorMessage="";
		Properties p_ctx = Env.getCtx();
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		REC_InitDate=dateFormat.format(amnpayroll.getInvDateIni());
		REC_EndDate=dateFormat.format(amnpayroll.getInvDateEnd());
		ACCT_Date=dateFormat.format(amnpayroll.getDateAcct());
		if (amnpayroll.getRefDateIni()==null)
			REF_InitDate=dateFormat.format(amnpayroll.getInvDateIni());
		else
			REF_InitDate=dateFormat.format(amnpayroll.getRefDateIni());
		if (amnpayroll.getRefDateEnd()==null)
			REF_EndDate=dateFormat.format(amnpayroll.getInvDateEnd());
		else
			REF_EndDate=dateFormat.format(amnpayroll.getRefDateEnd());
		MAMN_Contract amncontract = new MAMN_Contract(p_ctx, amnpayroll.getAMN_Contract_ID(), null);
		MAMN_Process amnprocess = new MAMN_Process(p_ctx, amnpayroll.getAMN_Process_ID(), null);
		int AMN_Concept_Types_ID = MAMN_Concept_Types.sqlGetAMNConceptTypesID(amnpayroll.getAD_Client_ID(),p_Concept_Reference);
		MCurrency curr = new MCurrency(p_ctx,amnpayroll.getC_Currency_ID(),null);
		AM_Contract=amncontract.getValue().trim();
		AM_Process=amnprocess.getValue().trim();
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), null);
		MAMN_Shift amnshift = new MAMN_Shift(p_ctx,amnemployee.getAMN_Shift_ID(), null);
		AM_Shift = amnshift.getValue();
		AM_PayrollMode=amnemployee.getpayrollmode();
		AM_Status =amnemployee.getStatus();
		AM_IncomeDate=dateFormat.format(amnemployee.getincomedate());
		AM_PaymentType=amnemployee.getpaymenttype();
		AM_CivilStatus=amnemployee.getcivilstatus();
		AM_Sex=amnemployee.getsex();
		AM_Spouse=amnemployee.getspouse();
		if (curr.getISO_Code()!=null)
			AM_Currency=curr.getISO_Code();
		else
			AM_Currency="VES";
		// SHOW VARIABLES
		//logVariablesShow();
		//log.warning("AM_Currency="+AM_Currency);
		//
		if (amnemployee.isStudying())	AM_IsStudyng="Y";  else		AM_IsStudyng="N";
		if (amnemployee.isPensioned())	AM_IsPensioned="Y";  else	AM_IsPensioned="N";
		if (amnemployee.isPensioned())	AM_IsMedicated="Y";  else	AM_IsMedicated="N";
		// Birth date
		AM_BirthDate=dateFormat.format(amnemployee.getBirthday());
		// Work Force (, Department Value, Location Value, Project Value,
		// Activity Value, Jobtitle Value, Jobstation Value , Shift Value
		MAMN_Jobtitle amnjt = new MAMN_Jobtitle(p_ctx, amnemployee.getAMN_Jobtitle_ID(), null);
		MAMN_Jobstation amnjs = new MAMN_Jobstation(p_ctx, amnemployee.getAMN_Jobstation_ID(), null);
		MAMN_Jobunit amnju = new MAMN_Jobunit(p_ctx, amnjs.getAMN_Jobunit_ID(),null);
		MAMN_Location amnlo = new MAMN_Location(p_ctx, amnemployee.getAMN_Location_ID(), null);
		MAMN_Department amnde = new MAMN_Department(p_ctx, amnemployee.getAMN_Department_ID(),null);
		MActivity act = new MActivity(p_ctx, amnemployee.getC_Activity_ID(),null);
		MProject proj = new MProject(p_ctx, amnemployee.getC_Project_ID(), null);
		AM_Workforce=amnjt.getWorkforce();
		AM_Department=amnde.getValue();
		AM_Location=amnlo.getValue();
		AM_Project=proj.getValue();
		AM_Activity=act.getValue();
		AM_Jobtitle=amnjt.getValue();
		AM_Jobstation=amnjs.getValue();
		AM_Jobunit=amnju.getValue();
		//scriptResult RetVal = null ;
		scriptResult RetVal = new scriptResult();
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
					QT_SB=SBD;
				}
				ScriptableObject.putProperty(scope,"QT_SB", QT_SB);
				//ctx.setAttribute("QT_SB", QT_SB, ScriptContext.ENGINE_SCOPE);
			}			// DT: VALUE FROM CONCEPT TYPE DIAS
			if (p_script.contains("DT")) {
				ScriptableObject.putProperty(scope,"DT", WorkingDaysDT);
				//ctx.setAttribute("DT", WorkingDaysDT, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", DT.doubleValue());
			}
			// DT: VALUE FROM CONCEPT TYPE DIAS
			if (p_script.contains("DIAS")) {
				ScriptableObject.putProperty(scope,"DIAS", WorkingDaysDT);
				//ctx.setAttribute("DIAS", WorkingDaysDT, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", DT.doubleValue());
			}
			// DTOK: Days Worked OK COMPLETED converted to Double for interprete
			if (p_script.contains("DTOK")) {
				ScriptableObject.putProperty(scope,"DTOK", WorkDays);
				//ctx.setAttribute("DTOK", WorkDays, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", DT.doubleValue());
			}
			// DTREC: Days on Payroll Receipt
			if (p_script.contains("DTREC")) {
				ScriptableObject.putProperty(scope,"DTREC", WorkDays);
				//ctx.setAttribute("DTREC", WorkDays, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", DT.doubleValue());
			}
			// DTPER: Days from Contract Period 
			if (p_script.contains("DTPER")) {
				ScriptableObject.putProperty(scope,"DTPER", DTPER);
				//ctx.setAttribute("DTPER", DTPER, ScriptContext.ENGINE_SCOPE);
				ScriptableObject.putProperty(scope,"DT", DT.doubleValue());
			}
			//log.warning(".....................AmerpPayrollCalca.java...FormulaEvaluationScript...................................");
			//log.warning("p_OptMode:"+p_OptMode+"  CND:"+(CND)+"  SBD:"+SBD+"    WorkDays:"+WorkDays);				
			// R_ASIG: Total DB
			if (p_script.contains("R_ASIG")) {
				ScriptableObject.putProperty(scope,"R_ASIG", BigDecimal.valueOf(R_ASIG));
				//ctx.setAttribute("R_ASIG", BigDecimal.valueOf(R_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			// R_DEDUC: Total CR
			if (p_script.contains("R_DEDUC")) {
				ScriptableObject.putProperty(scope,"R_DEDUC", BigDecimal.valueOf(R_DEDUC));
				//ctx.setAttribute("R_DEDUC", BigDecimal.valueOf(R_DEDUC), ScriptContext.ENGINE_SCOPE);
			}
			// R_TOTAL: Total NET
			if (p_script.contains("R_TOTAL")) {
				ScriptableObject.putProperty(scope,"R_TOTAL", BigDecimal.valueOf(RA_ASIG));
				//ctx.setAttribute("R_TOTAL", BigDecimal.valueOf(RA_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			// RA_ASIG: Total DB Not Shown
			if (p_script.contains("RA_ASIG")) {
				ScriptableObject.putProperty(scope,"RA_ASIG", BigDecimal.valueOf(R_ASIG));
				//ctx.setAttribute("RA_ASIG", BigDecimal.valueOf(R_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			// RA_DEDUC: Total CR Not Shown
			if (p_script.contains("RA_DEDUC")) {
				ScriptableObject.putProperty(scope,"RA_DEDUC", BigDecimal.valueOf(RA_DEDUC));
				//ctx.setAttribute("RA_DEDUC", BigDecimal.valueOf(RA_DEDUC), ScriptContext.ENGINE_SCOPE);
			}
			// RA_TOTAL: Total Net Not Shown
			if (p_script.contains("RA_TOTAL")) {
				ScriptableObject.putProperty(scope,"RA_TOTAL", BigDecimal.valueOf(RA_TOTAL));
				//ctx.setAttribute("RA_TOTAL", BigDecimal.valueOf(RA_TOTAL), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RE_ASIG")) {
				ScriptableObject.putProperty(scope,"RE_ASIG", BigDecimal.valueOf(RE_ASIG));
				//ctx.setAttribute("RE_ASIG", BigDecimal.valueOf(RE_ASIG), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RE_DEDUC")) {
				ScriptableObject.putProperty(scope,"RE_DEDUC", BigDecimal.valueOf(RE_DEDUC));
				//ctx.setAttribute("RE_DEDUC", BigDecimal.valueOf(RE_DEDUC), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RE_TOTAL")) {
				ScriptableObject.putProperty(scope,"RE_TOTAL", BigDecimal.valueOf(RE_TOTAL));
				//ctx.setAttribute("RE_TOTAL", BigDecimal.valueOf(RE_TOTAL), ScriptContext.ENGINE_SCOPE);
			}			
			if (p_script.contains("R_FAOV")) {
				ScriptableObject.putProperty(scope,"R_FAOV", BigDecimal.valueOf(R_FAOV));
				//ctx.setAttribute("R_FAOV", BigDecimal.valueOf(R_FAOV), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_SALARIO")) {
				ScriptableObject.putProperty(scope,"R_SALARIO", BigDecimal.valueOf(R_SALARIO));
				//ctx.setAttribute("R_SALARIO", BigDecimal.valueOf(R_SALARIO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_INCES")) {
				ScriptableObject.putProperty(scope,"R_INCES", BigDecimal.valueOf(R_INCES));
				//ctx.setAttribute("R_INCES", BigDecimal.valueOf(R_INCES), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_SSO")) {
				ScriptableObject.putProperty(scope,"R_SSO", BigDecimal.valueOf(R_SSO));
				//ctx.setAttribute("R_SSO", BigDecimal.valueOf(R_SSO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_ARC")) {
				ScriptableObject.putProperty(scope,"R_ARC", BigDecimal.valueOf(R_ARC));
				//ctx.setAttribute("R_ARC", BigDecimal.valueOf(R_ARC), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_SPF")) {
				ScriptableObject.putProperty(scope,"R_SPF", BigDecimal.valueOf(R_SPF));
				//ctx.setAttribute("R_SPF", BigDecimal.valueOf(R_SPF), ScriptContext.ENGINE_SCOPE);
			}
			// VARIABLE R_DESCANSO EQUAL TO R_FERIADO
			if (p_script.contains("R_DESCANSO")) {
				ScriptableObject.putProperty(scope,"R_DESCANSO", BigDecimal.valueOf(R_FERIADO));
				//ctx.setAttribute("R_DESCANSO", BigDecimal.valueOf(R_FERIADO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_FERIADO")) {
				ScriptableObject.putProperty(scope,"R_FERIADO", BigDecimal.valueOf(R_FERIADO));
				//ctx.setAttribute("R_FERIADO", BigDecimal.valueOf(R_FERIADO), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_UTILIDAD")) {
				ScriptableObject.putProperty(scope,"R_UTILIDAD", BigDecimal.valueOf(R_UTILIDAD));
				//ctx.setAttribute("R_UTILIDAD", BigDecimal.valueOf(R_UTILIDAD), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_VACACION")) {
				ScriptableObject.putProperty(scope,"R_VACACION", BigDecimal.valueOf(R_VACACION));
				//ctx.setAttribute("R_VACACION",  BigDecimal.valueOf(R_VACACION), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("R_PRESTACION")) {
				ScriptableObject.putProperty(scope,"R_PRESTACION", BigDecimal.valueOf(R_PRESTACION));
				//ctx.setAttribute("R_PRESTACION", BigDecimal.valueOf(R_PRESTACION), ScriptContext.ENGINE_SCOPE);
			}
        	/* Change on Business Logic
        	 * Variable RA_TAX, RD_TAX, RA_SSO, RD_SSO
        	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
        	 */
			// RA_TAX
			if (p_script.contains("RA_TAX")) {
			ScriptableObject.putProperty(scope,"RA_TAX", BigDecimal.valueOf(RA_TAX));
			//ctx.setAttribute("R_ARC", BigDecimal.valueOf(R_ARC), ScriptContext.ENGINE_SCOPE);
			}
			// RD_TAX
			if (p_script.contains("RD_TAX")) {
			ScriptableObject.putProperty(scope,"RD_TAX", BigDecimal.valueOf(RD_TAX));
			}
			// RA_SSO
			if (p_script.contains("RA_SSO")) {
			ScriptableObject.putProperty(scope,"RA_SSO", BigDecimal.valueOf(RA_SSO));
			}
			// RD_SSO
			if (p_script.contains("RD_SSO")) {
			ScriptableObject.putProperty(scope,"RD_SSO", BigDecimal.valueOf(RD_SSO));
			}	
			/* Change on Business Logic
        	 * Variable RA_TAX, RD_TAX, RA_SSO, RD_SSO
        	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
        	 */	
			if (p_script.contains("SBMIN")) {
				ScriptableObject.putProperty(scope,"SBMIN", BigDecimal.valueOf(SBMIN));
				//ctx.setAttribute("SBMIN", BigDecimal.valueOf(SBMIN), ScriptContext.ENGINE_SCOPE);
			}
			// Process Value (NN,NP,NU,NV)
			if (p_script.contains("AM_Process")) {
				ScriptableObject.putProperty(scope,"AM_Process", AM_Process);
				//ctx.setAttribute("AM_Process", AM_Process, ScriptContext.ENGINE_SCOPE);
			}
			// Contract Value (S,Q,ME,MQ,..)
			if (p_script.contains("AM_Contract")) {
				ScriptableObject.putProperty(scope,"AM_Contract", AM_Contract );
				//ctx.setAttribute("AM_Contract", AM_Contract, ScriptContext.ENGINE_SCOPE);
			}
			// EMPLOYEE's VARS
			if (p_script.contains("AM_Shift")) {
				ScriptableObject.putProperty(scope,"AM_Shift", AM_Shift );
				//ctx.setAttribute("AM_Shift", AM_Shift, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_PayrollMode")) {
				ScriptableObject.putProperty(scope,"AM_PayrollMode", AM_PayrollMode );
				//ctx.setAttribute("AM_PayrollMode", AM_PayrollMode, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Status")) {
				ScriptableObject.putProperty(scope,"AM_Status", AM_Status );
				//ctx.setAttribute("AM_Status", AM_Status, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IncomeDate")) {
				ScriptableObject.putProperty(scope,"AM_IncomeDate", AM_IncomeDate );
				//ctx.setAttribute("AM_IncomeDate", AM_IncomeDate, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_PaymentType")) {
				ScriptableObject.putProperty(scope,"AM_PaymentType", AM_PaymentType );
				//ctx.setAttribute("AM_PaymentType", AM_PaymentType, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_CivilStatus")) {
				ScriptableObject.putProperty(scope,"AM_CivilStatus", AM_CivilStatus );
				//ctx.setAttribute("AM_CivilStatus", AM_CivilStatus, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Sex")) {
				ScriptableObject.putProperty(scope,"AM_Sex", AM_Sex );
				//ctx.setAttribute("AM_Sex", AM_Sex, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Spouse")) {
				ScriptableObject.putProperty(scope,"AM_Spouse", AM_Spouse );
				//ctx.setAttribute("AM_Spouse", AM_Spouse, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IsPensioned")) {
				ScriptableObject.putProperty(scope,"AM_IsPensioned", AM_IsPensioned );
				//ctx.setAttribute("AM_IsPensioned", AM_IsPensioned, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IsStudyng")) {
				ScriptableObject.putProperty(scope,"AM_IsStudyng", AM_IsStudyng );
				//ctx.setAttribute("AM_IsStudyng", AM_IsStudyng, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_IsMedicated")) {
				ScriptableObject.putProperty(scope,"AM_IsMedicated", AM_IsMedicated );
				//ctx.setAttribute("AM_IsMedicated", AM_IsMedicated, ScriptContext.ENGINE_SCOPE);
			}
			// AM_BirthDate
			if (p_script.contains("AM_BirthDate")) {
				ScriptableObject.putProperty(scope,"AM_BirthDate", AM_BirthDate );
				//ctx.setAttribute("AM_BirthDate", AM_BirthDate, ScriptContext.ENGINE_SCOPE);
			}
			// AM_Workforce
			if (p_script.contains("AM_Workforce")) {
				//log.warning("AM_Workforce="+AM_Workforce);
				ScriptableObject.putProperty(scope,"AM_Workforce", AM_Workforce );
				//ctx.setAttribute("AM_Workforce", AM_Workforce, ScriptContext.ENGINE_SCOPE);
			}			
			// AM_Department
			if (p_script.contains("AM_Department")) {
				//log.warning("AM_Department="+AM_Department);
				ScriptableObject.putProperty(scope,"AM_Department", AM_Department );
				//ctx.setAttribute("AM_Department", AM_Department, ScriptContext.ENGINE_SCOPE);
			}	
			// AM_Location
			if (p_script.contains("AM_Location")) {
				ScriptableObject.putProperty(scope,"AM_Location", AM_Location );
				//ctx.setAttribute("AM_Location", AM_Location, ScriptContext.ENGINE_SCOPE);
			}	
			// AM_Project
			if (p_script.contains("AM_Project")) {
				ScriptableObject.putProperty(scope,"AM_Project", AM_Project );
				//ctx.setAttribute("AM_Project", AM_Project, ScriptContext.ENGINE_SCOPE);
			}			
			// AM_Activity
			if (p_script.contains("AM_Activity")) {
				ScriptableObject.putProperty(scope,"AM_Activity", AM_Activity );
				//ctx.setAttribute("AM_Activity", AM_Activity, ScriptContext.ENGINE_SCOPE);
			}	
			// AM_Jobtitle
			if (p_script.contains("AM_Jobtitle")) {
				ScriptableObject.putProperty(scope,"AM_Jobtitle", AM_Jobtitle );
				//ctx.setAttribute("AM_Jobtitle", AM_Jobtitle, ScriptContext.ENGINE_SCOPE);
			}
			// AM_Jobstation
			if (p_script.contains("AM_Jobstation")) {
				ScriptableObject.putProperty(scope,"AM_Jobstation", AM_Jobstation );
				//ctx.setAttribute("AM_Jobtitle", AM_Jobtitle, ScriptContext.ENGINE_SCOPE);
			}
			// AM_Jobunit
			if (p_script.contains("AM_Jobunit")) {
				ScriptableObject.putProperty(scope,"AM_Jobunit", AM_Jobunit );
				//ctx.setAttribute("AM_Jobunit", AM_Jobunit, ScriptContext.ENGINE_SCOPE);
			}
			// REC_InitDate
			if (p_script.contains("REC_InitDate")) {
				ScriptableObject.putProperty(scope,"REC_InitDate", REC_InitDate );
				//ctx.setAttribute("REC_InitDate", REC_InitDate, ScriptContext.ENGINE_SCOPE);
			}
			// REC_EndDate
			if (p_script.contains("REC_EndDate")) {
				ScriptableObject.putProperty(scope,"REC_EndDate", REC_EndDate );
				//ctx.setAttribute("REC_EndDate", REC_EndDate, ScriptContext.ENGINE_SCOPE);
			}
			// ACCT_Date
			if (p_script.contains("ACCT_Date")) {
				//log.warning("ACCT_Date="+ACCT_Date);
				ScriptableObject.putProperty(scope,"ACCT_Date", ACCT_Date );
				//ctx.setAttribute("ACCT_Date", ACCT_Date, ScriptContext.ENGINE_SCOPE);
			}
			// REF_InitDate
			if (p_script.contains("REF_InitDate")) {
				ScriptableObject.putProperty(scope,"REF_InitDate", REF_InitDate );
				//ctx.setAttribute("REF_InitDate", REF_InitDate, ScriptContext.ENGINE_SCOPE);
			}
			// REF_EndDate
			if (p_script.contains("REF_EndDate")) {
				ScriptableObject.putProperty(scope,"REF_EndDate", REF_EndDate );
				//ctx.setAttribute("REF_EndDate", REF_EndDate, ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("AM_Currency")) {
				ScriptableObject.putProperty(scope,"AM_Currency", AM_Currency );
				//ctx.setAttribute("AM_Currency", AM_Currency, ScriptContext.ENGINE_SCOPE);
			}
			//log.warning("p_script:"+p_script +"  AM_Process:"+AM_Process.trim()+"  AM_Contract:"+AM_Contract);			
			if (p_script.contains("UNIDADTRIBUTARIA")) {
				//log.warning("p_script:"+p_script +"  UNIDADTRIBUTARIA:"+UNIDADTRIBUTARIA);			
				ScriptableObject.putProperty(scope,"UNIDADTRIBUTARIA", BigDecimal.valueOf(UNIDADTRIBUTARIA));
				//ctx.setAttribute("UNIDADTRIBUTARIA", BigDecimal.valueOf(UNIDADTRIBUTARIA), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("TAXRATE")) {
				//log.warning("p_script:"+p_script +"  TAXRATE:"+TAXRATE+"  UNIDADTRIBUTARIA:"+UNIDADTRIBUTARIA);			
				ScriptableObject.putProperty(scope,"TAXRATE", BigDecimal.valueOf(TAXRATE));
				//ctx.setAttribute("TAXRATE", BigDecimal.valueOf(TAXRATE), ScriptContext.ENGINE_SCOPE);
			}
			//log.warning(".....................AmerpPayrollCalca.java...FormulaEvaluationScript...................................");
			//log.warning("DTREC:"+DTREC+"  HOLLIDAYS:"+HOLLIDAYS+"  NONLABORDAYS:"+NONLABORDAYS+"    LABORDAYS:"+LABORDAYS);				
			if (p_script.contains("LABORDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  LABORDAYS:"+LABORDAYS);
				ScriptableObject.putProperty(scope,"LABORDAYS", BigDecimal.valueOf(LABORDAYS));
				//ctx.setAttribute("LABORDAYS", BigDecimal.valueOf(LABORDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("HOLLIDAYS")) {					
				//log.warning("p_script:"+p_script.trim()+"  HOLLIDAYS:"+HOLLIDAYS);
				ScriptableObject.putProperty(scope,"HOLLIDAYS", BigDecimal.valueOf(HOLLIDAYS));
				//ctx.setAttribute("HOLLIDAYS", BigDecimal.valueOf(HOLLIDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NONLABORDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  NONLABORDAYS:"+NONLABORDAYS);
				ScriptableObject.putProperty(scope,"NONLABORDAYS", BigDecimal.valueOf(NONLABORDAYS));
				//ctx.setAttribute("NONLABORDAYS", BigDecimal.valueOf(NONLABORDAYS), ScriptContext.ENGINE_SCOPE);
			}
			// NEW VARIABLES
			if (p_script.contains("BUSINESSDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  BUSINESSDAYS:"+BUSINESSDAYS);
				ScriptableObject.putProperty(scope,"BUSINESSDAYS", BigDecimal.valueOf(BUSINESSDAYS));
				//ctx.setAttribute("BUSINESSDAYS", BigDecimal.valueOf(BUSINESSDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NONBUSINESSDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  NONBUSINESSDAYS:"+NONBUSINESSDAYS);
				ScriptableObject.putProperty(scope,"NONBUSINESSDAYS", BigDecimal.valueOf(NONBUSINESSDAYS));
				//ctx.setAttribute("NONBUSINESSDAYS", BigDecimal.valueOf(NONBUSINESSDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("WEEKENDDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  WEEKENDDAYS:"+WEEKENDDAYS);
				ScriptableObject.putProperty(scope,"WEEKENDDAYS", BigDecimal.valueOf(WEEKENDDAYS));
				//ctx.setAttribute("WEEKENDDAYS", BigDecimal.valueOf(WEEKENDDAYS), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NONWEEKENDDAYS")) {
				//log.warning("p_script:"+p_script.trim()+"  NONWEEKENDDAYS:"+NONWEEKENDDAYS);
				ScriptableObject.putProperty(scope,"NONWEEKENDDAYS", BigDecimal.valueOf(NONWEEKENDDAYS));
				//ctx.setAttribute("NONWEEKENDDAYS", BigDecimal.valueOf(NONWEEKENDDAYS), ScriptContext.ENGINE_SCOPE);
			}
			// NLUNES
			if (p_script.contains("NLUNES") ) {					
				// log.warning("p_script:"+p_script.trim()+"  NLUNES:"+NLUNES);
				ScriptableObject.putProperty(scope,"NLUNES", BigDecimal.valueOf(NLUNES));
				//ctx.setAttribute("NLUNES", BigDecimal.valueOf(NLUNES), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("NMONDAY") ) {					
				//log.warning("p_script:"+p_script.trim()+"  NLUNES:"+NLUNES);
				ScriptableObject.putProperty(scope,"NMONDAY", BigDecimal.valueOf(NMONDAY));
				//ctx.setAttribute("NMONDAY", BigDecimal.valueOf(NMONDAY), ScriptContext.ENGINE_SCOPE);
			}
			// PUT FIXED VARIABLES VALUES--------
			if (p_script.contains("QTY_HND")) {
				ScriptableObject.putProperty(scope,"QTY_HND", BigDecimal.valueOf(QTY_HND));
				//ctx.setAttribute("QTY_HND", BigDecimal.valueOf(QTY_HND), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("QTY_HNN")) {
				ScriptableObject.putProperty(scope,"QTY_HNN", BigDecimal.valueOf(QTY_HNN));
				//ctx.setAttribute("QTY_HNN", BigDecimal.valueOf(QTY_HNN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("QTY_HED")) {
				ScriptableObject.putProperty(scope,"QTY_HED", BigDecimal.valueOf(QTY_HED));
				//ctx.setAttribute("QTY_HED", BigDecimal.valueOf(QTY_HED), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("QTY_HEN")) {
				ScriptableObject.putProperty(scope,"QTY_HEN", BigDecimal.valueOf(QTY_HEN));
				//ctx.setAttribute("QTY_HEN", BigDecimal.valueOf(QTY_HEN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HND")) {
				ScriptableObject.putProperty(scope,"RSU_HND", BigDecimal.valueOf(RSU_HND));
				//ctx.setAttribute("RSU_HND", BigDecimal.valueOf(RSU_HND), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HNN")) {
				ScriptableObject.putProperty(scope,"RSU_HNN", BigDecimal.valueOf(RSU_HNN));
				//ctx.setAttribute("RSU_HNN", BigDecimal.valueOf(RSU_HNN), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HED")) {
				ScriptableObject.putProperty(scope,"RSU_HED", BigDecimal.valueOf(RSU_HED));
				//ctx.setAttribute("RSU_HED", BigDecimal.valueOf(RSU_HED), ScriptContext.ENGINE_SCOPE);
			}
			if (p_script.contains("RSU_HEN")) {
				ScriptableObject.putProperty(scope,"RSU_HEN", BigDecimal.valueOf(RSU_HEN));
				//ctx.setAttribute("RSU_HEN",  BigDecimal.valueOf(RSU_HEN), ScriptContext.ENGINE_SCOPE);
			}
			// **********************************************************
			// PUT FIXED VARIABLES VALUES
			// Qty Starts with 				QT_
			// ResultValues starts with 	RS_
			// **********************************************************
			if (p_script.contains("QT_") || p_script.contains("RS_")) {
				String CTVar="",CTVar1="",CTVar2="";
				BigDecimal CTQty,CTRSValue;
				for (int index = 0; index < ConIndex; ++index)  {
					CTVar="";
					CTVar1="";
					CTVar2="";
					CTQty=BigDecimal.valueOf(0.00);
					CTRSValue=BigDecimal.valueOf(0.00);
					try {
	                    CTVar=ConceptTypes[index].getConceptVariable();
	                    CTQty=ConceptTypes[index].getQtyValue();
	                    CTRSValue=ConceptTypes[index].getResultValue();
	                    CTVar1="QT_"+CTVar.trim();
						CTVar2="RS_"+CTVar.trim();
	               } catch (Exception ex) {
	                    // TODO Auto-generated catch block
	                    //ex.printStackTrace();
	                    CTQty=BigDecimal.ZERO;
	                    CTRSValue=BigDecimal.ZERO;
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
							RS_SB=SBD;
							CTRSValue=BigDecimal.valueOf(SBD);
							ScriptableObject.putProperty(scope,"RS_SB", RS_SB);
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
//if (CTVar1.equalsIgnoreCase("QT_PLSALMEN ") || 
//		CTVar1.equalsIgnoreCase("QT_SALMENLIQ") ||
//		CTVar1.equalsIgnoreCase("QT_SALBASLIQ"))
//	log.warning("Concepto="+p_Concept_Reference.trim()+"  index="+index+"  Variable="+CTVar1+" Qty="+CTQty.doubleValue()+"  CTRSValue="+CTRSValue);
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
				for (int index = 0; index < RulIndex; ++index)  {
					RTRule="";
					RTRSValue=BigDecimal.valueOf(0.00);
					try {
						RTRule=RulesTypes[index].getRuleVariable();
	                    RTRSValue=RulesTypes[index].getResultValue();
					}
	                catch (Exception ex) {
	                    // TODO Auto-generated catch block
	                    //ex.printStackTrace();
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
				for (int index = 0; index < DVRulIndex; ++index)  {
					DVRule="";
					DVRSValue=BigDecimal.valueOf(0.00);
					try {
						DVRule=DVRulesTypes[index].getRuleVariable();
	                    DVRSValue=DVRulesTypes[index].getResultValue();
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
						RSLastValueBD = AmerpPayrollCalcUtilDVFormulas.processLastValueQTY(p_ctx, amnprocess.getAMN_Process_ID(), amncontract.getAMN_Contract_ID(),  p_AMN_Payroll_ID, AMN_Concept_Types_ID, amnpayroll.get_TrxName() );
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
						QTYLastValueBD = AmerpPayrollCalcUtilDVFormulas.processLastValueQTY(p_ctx, amnprocess.getAMN_Process_ID(), amncontract.getAMN_Contract_ID(),  p_AMN_Payroll_ID, AMN_Concept_Types_ID, amnpayroll.get_TrxName() );
					} catch (Exception e) {
						// TODO Auto-generated catch block
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
			BDCalcAmnt = BDCalcAmnt.setScale(2, RoundingMode.CEILING);
			//return BDCalcAmnt;
			RetVal.setBDCalcAmnt(BDCalcAmnt);
			RetVal.setErrorMessage(ErrorMessage.trim());			
			
		} else {
			//return BigDecimal.valueOf(CalcAmnt);			
			ErrorMessage="Formula VACIA ó ERROR en Formula";
			RetVal.setBDCalcAmnt(BigDecimal.valueOf(CalcAmnt));
			RetVal.setErrorMessage(ErrorMessage.trim());	
		}
//log.warning("Concepto="+p_Concept_Reference.trim()+"  CalcAmnt="+CalcAmnt);
//		}
		return RetVal ;
	}  
    //************************************************
   	// logVariablesShow
	// Parameters: none
	//************************************************
	  
	public static String logVariablesShow() {
	    
		CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);
		String Message ="";
		log.warning("SBMIN:"+SBMIN);
		log.warning("QT_SB:"+QT_SB);
		log.warning("RS_SB:"+RS_SB);
//		log.warning("R_ASIG:"+R_ASIG);
//	    log.warning("R_DEDUC:"+R_DEDUC);
//	    log.warning("R_TOTAL:"+R_TOTAL);
//	    log.warning("RA_ASIG:"+RA_ASIG);
//	    log.warning("RA_DEDUC:"+RA_DEDUC);
//	    log.warning("RA_TOTAL:"+RA_TOTAL);
//	    log.warning("RE_ASIG:"+RE_ASIG);
//	    log.warning("RE_DEDUC:"+RE_DEDUC);
//	    log.warning("RE_TOTAL:"+RE_TOTAL);
//	    log.warning("R_SALARIO:"+R_SALARIO);
//	    log.warning("R_FAOV:"+R_FAOV);
//	    log.warning("R_INCES:"+R_INCES);
//	    log.warning("R_SSO:"+R_SSO);
//	    log.warning("R_ARC:"+R_ARC);
//	    log.warning("R_SPF:"+R_SPF);
//	    log.warning("R_DESCANSO:"+R_DESCANSO);
//	    log.warning("R_FERIADO:"+R_FERIADO);
//	    log.warning("R_UTILIDAD:"+R_UTILIDAD);
//	    log.warning("R_VACACION:"+R_VACACION);
//	    log.warning("R_PRESTACION:"+R_PRESTACION);
//	    log.warning("AM_Currency:"+AM_Currency);

		log.warning("Variables list ( "+ConIndex+1+" )");
		for (int index = 0; index < ConIndex; ++index)  {
			if (ConceptTypes[index].getQtyValue().compareTo(BigDecimal.ZERO) != 0 ||
					ConceptTypes[index].getResultValue().compareTo(BigDecimal.ZERO) != 0 ) {
				Message = Message + "Concepto="+ConceptTypes[index].getCalcOrder()+" "+
						ConceptTypes[index].getConceptVariable()+
						" QTY="+ConceptTypes[index].getQtyValue()+
						" Result="+ ConceptTypes[index].getResultValue()+"\r\n" ;
			}
		}
		log.warning(Message);
		return Message;
	}
	
	
	public static String logRulesShow() {
	    
		CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);
		String Message ="\r\n";
		log.warning("Rules list ( "+RulIndex+" )");
		for (int index = 0; index < RulIndex; ++index)  {
			if (RulesTypes[index].getQtyValue().compareTo(BigDecimal.ZERO) != 0 ||
					RulesTypes[index].getResultValue().compareTo(BigDecimal.ZERO) != 0 ) {
				Message = Message + "AMN_Payroll_ID="+RulesTypes[index].getAMN_Payroll_ID()+
						" Order="+RulesTypes[index].getRuleOrder()+
						" Variable="+RulesTypes[index].getRuleVariable()+
						" QTY="+RulesTypes[index].getQtyValue()+
						" Result="+ RulesTypes[index].getResultValue()+"\r\n" ;
			}
		}
		log.warning(Message);
		return Message;
	}
		
	//*************************************************
   	// Init Payroll Public variables
   	// Default Values 
 	// Parameters: none
	// @param p_aD_Client_ID 
	// ************************************************
	/**
	 * 
	 * @param p_aD_Client_ID
	 */
	public static void VariablesInit (Integer p_aD_Client_ID) {
		
		//listMemoryVariables = new ArrayList<memoryVars>();
		// future use
		// BEGIN OF LIST
		// NN PAYROLL
		Double Zero= 0.00;
		Double One= 1.00;
	    // Init Variables Values
//		QT_SB=(Zero); 		// Default SB Concept
//		RS_SB=(Zero); 		// Default SB Concept
		R_ASIG=(Zero);		// All Concepts
		R_DEDUC=(Zero);		// All Concepts
		R_TOTAL=(Zero);		// All Concepts
		RA_ASIG=(Zero);		// All Concepts (Accounting Porpousses)
		RA_DEDUC=(Zero);	// All Concepts (Accounting Porpousses)
		RA_TOTAL=(Zero);	// All Concepts (Accounting Porpousses)
		RE_ASIG=(Zero);		// All Concepts (Employee Receipt Porpousses)
		RE_DEDUC=(Zero);	// All Concepts (Employee Receipt Porpousses)
		RE_TOTAL=(Zero);	// All Concepts (Employee Receipt Porpousses)
		R_FAOV=(Zero);		// Fondo Vivienda
		R_SALARIO=(Zero);	// Salario Integral
		R_INCES=(Zero);		// INCES
		R_SSO=(Zero);		// Seguro Social Obligatorio
		R_ARC=(Zero);		// ARC Impuesto sobre la renta  ISLR
		R_SPF=(Zero);		// Seguro Paro Forzozo
		R_DESCANSO=(Zero);	// Descansos
		R_FERIADO=(Zero);	// Feriados
		R_UTILIDAD=(Zero);	// Utilidades
		R_VACACION=(Zero);	// Vacaciones
		R_PRESTACION=(Zero);// Prestaciones
		RA_TAX=(Zero);		// Tax Acumulado Asignaciones
		RD_TAX=(Zero);		// Tax Acumulado Deducciones
		RA_SSO=(Zero);		// SSO Acumulado Asignaciones
		RD_SSO=(Zero);		// SSO Acumulado Deducciones
		WorkingDaysDT=(Zero);
		DIAS=(Zero);
		HORAS=(Zero);
		PNRM=(Zero);
		DT=(Zero);
		DTREC=(Zero);
		DTPER=(Zero);
		DTOK=(Zero);
		NONLABORDAYS=(One);
		LABORDAYS=(One);
		HOLLIDAYS=(Zero);
//		NLUNES=(Zero);
		NMONDAY=(Zero);
		QTY_HND=(Zero);
		QTY_HNN=(Zero);
		QTY_HED=(Zero);
		QTY_HEN=(Zero);
		RSU_HND=(Zero);
		RSU_HNN=(Zero);
		RSU_HED=(Zero);
		RSU_HEN=(Zero);		
		UNIDADTRIBUTARIA=(Zero);
		SBMIN=(Zero);
		TAXRATE=(Zero);
		BUSINESSDAYS=(Zero); 
		NONBUSINESSDAYS =(Zero);
		WEEKENDDAYS=(Zero);
		NONWEEKENDDAYS=(Zero);
		LASTVALUE=(Zero);
		
		// Logical Variables
		IS_FAOV ="N" ;
		IS_SALARIO ="N" ;
		IS_INCES ="N" ;
		IS_SSO ="N" ;
		IS_ARC ="N" ;
		IS_SPF ="N" ;
		IS_DESCANSO ="N" ;
		IS_FERIADO ="N" ;
		IS_UTILIDAD ="N";
		IS_VACACION = "N";
		IS_PRESTACION = "N";
		AM_Contract="";
		AM_Process="";
		// Employee
		AM_Shift ="";
		AM_PayrollMode="";
		AM_Status ="";
		AM_IncomeDate="";
		AM_PaymentType="";
		AM_CivilStatus="";
		AM_Sex="";
		AM_Spouse="";
		AM_IsPensioned="";
		AM_IsStudyng="";
		AM_IsMedicated="";
		AM_BirthDate="";
		AM_Currency="";
		//ConIndex=0;
		// Init Array
		// Date Vars
		REC_InitDate="";
		REC_EndDate="";
		ACCT_Date="";
		REF_InitDate="";
		REF_EndDate="";
		// Work Force (, Department Value, Location Value, Project Value, 
		// Activity Value, Jobtitle Value, Jobstation Value , Shift Value
		AM_Workforce="A";
		AM_Department="";
		AM_Location="";
		AM_Project="";
		AM_Activity="";
		AM_Jobtitle="";
		AM_Jobstation="";
		AM_Jobunit="";
		
		CTInit(p_aD_Client_ID);
	} // VariablesInit
	
	

	/*************************************************
   	* VariablesGetSQLValues Payroll Public variables
   	* Default Values 
 	* Parameters: 
 	* Timestamp p_PayrollStartDate
 	* Timestamp p_PayrollEndDate
 	* int p_currency
 	* int p_AD_Client_ID
 	* int p_AD_Org_ID
 	* Return: String ErrorMess
	//************************************************/
	public static String VariablesGetSQLValues (int p_AMN_Payroll_ID, 
			 int p_currency, int p_conversiontype_id, int p_AD_Client_ID, int p_AD_Org_ID) 
	{
		BigDecimal UNIDADTRIBUTARIA_BD = BigDecimal.valueOf(0.00);
		BigDecimal SBMIN_BD = BigDecimal.valueOf(0.00);
		BigDecimal TAXRATE_BD = BigDecimal.valueOf(0.00);
		Timestamp PayrollStartDate;
		int AMN_Employee_ID;
		//log.warning("------ VariablesGetSQLValues ----");
		//MAMN_Payroll.sqlGetAMNPayrollInvDateIni(p_AMN_Payroll_ID);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_ID, null);
		//p_PayrollEndDate = MAMN_Payroll.sqlGetAMNPayrollInvDateEnd(p_AMN_Payroll_ID);
		PayrollStartDate = amnpayroll.getInvDateIni();
		AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		String  ErrorMess ="";
		// UNIDADTRIBUTARIA
		UNIDADTRIBUTARIA_BD= MAMN_Rates.sqlGetTaxUnit(PayrollStartDate, 
				p_currency, p_conversiontype_id, p_AD_Client_ID, p_AD_Org_ID);
		if (UNIDADTRIBUTARIA_BD.doubleValue()== 0.00) {
			UNIDADTRIBUTARIA = 0.00;
			ErrorMess=ErrorMess+" UNIDADTRIBUTARIA VARIABLE ERROR ..... \n";
		} else {
			UNIDADTRIBUTARIA =  UNIDADTRIBUTARIA_BD.doubleValue();
		}
		// SBMIN
		SBMIN_BD= MAMN_Rates.sqlGetSalary_MO(PayrollStartDate, 
				p_currency, p_conversiontype_id, p_AD_Client_ID, p_AD_Org_ID);
		if (SBMIN_BD.doubleValue()== 0.00) {
			SBMIN = 0.00;
			ErrorMess=ErrorMess+" SBMIN VARIABLE ERROR ..... \n";
		} else {
			SBMIN =  SBMIN_BD.doubleValue();
		}
		// TAXRATE
		TAXRATE_BD= MAMN_Employee_Tax.getSQLEmployeeTaxRate(PayrollStartDate, 
				p_currency, p_AD_Client_ID, p_AD_Org_ID, AMN_Employee_ID);
		if (TAXRATE_BD.doubleValue() == 0.00) {
			TAXRATE = 0.00;
			ErrorMess=ErrorMess+" TAXRATE VARIABLE ERROR ..... \n";
		} else {
			TAXRATE =  TAXRATE_BD.doubleValue();
		}
		
		return ErrorMess;
		
	}
	
	/****************************************************
	 * memoryVars (FUTURE USE )
	 * @author luisamesty
	 * Return two parameters:
	 * Sring  MemVar_Name 
	 * Double MemVar_Value
	 * 
	 ***************************************************/
	public static class memoryVars {
		String MemVar_Name ;
		Double MemVar_Value;
		
		public memoryVars(String memVar_Name, Double memVar_Value) {
			super();
			MemVar_Name = memVar_Name;
			MemVar_Value = memVar_Value;
		}

		public String getMemVar_Name() {
			return MemVar_Name;
		}

		public void setMemVar_Name(String memVar_Name) {
			MemVar_Name = memVar_Name;
		}

		public Double getMemVar_Value() {
			return MemVar_Value;
		}

		public void setMemVar_Value(Double memVar_Value) {
			MemVar_Value = memVar_Value;
		}
		
		
	}
	
    /**************************************************
	 * Array Class Formula evaluation
	 * Return two parameters: 
	 * BigDecimal BDCalcAmnt; String errorMessage="";
	 **************************************************/
	public static class scriptResult {

		public BigDecimal BDCalcAmnt;
		public String errorMessage="";
		/**
		 * @return the bDCalcAmnt
		 */
		public BigDecimal getBDCalcAmnt() {
			return BDCalcAmnt;
		}
		/**
		 * @param p_bDCalcAmnt the bDCalcAmnt to set
		 */
		public void setBDCalcAmnt(BigDecimal p_bDCalcAmnt) {
			BDCalcAmnt = p_bDCalcAmnt;
		}
		/**
		 * @return the errorMessage
		 */
		public String getErrorMessage() {
			return errorMessage;
		}
		/**
		 * @param p_errorMessage the errorMessage to set
		 */
		public void setErrorMessage(String p_errorMessage) {
			errorMessage = p_errorMessage;
		}

	}
	
    /**
     * CTInit. 
     * @param p_aD_Client_ID 
     */
	static public boolean CTInit(Integer p_aD_Client_ID) {
		int i =0;
		if (ConIndexFT == 0) {
			AmerpConceptTypes CT = new AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
			String ConceptVariable;
			//log.warning("........PASO....CTInit....");
			int CalcOrder;
			String sql1 = "SELECT " 
	                +	"value as codigo, "
	                +	"COALESCE(variable,value,'') as ctyvariable, "
	                +	"calcorder "
	                +	"FROM amn_concept_types "
	                +	"WHERE "
	                +	"ad_client_id IN (0,?)" 
	                +	"ORDER BY calcorder "
	                ;
		    PreparedStatement pstmte = null;
		    ResultSet rse = null;
		    try
		    {
	    		pstmte = DB.prepareStatement(sql1, null);
	            pstmte.setInt(1, p_aD_Client_ID);
	            rse = pstmte.executeQuery();	            
	            // Reference Concepts Only
	            while (rse.next()) {
	            	rse.getString(1).trim();
	            	ConceptVariable = rse.getString(2).trim();
	            	CalcOrder= rse.getInt(3);
	    			CT.setCalcOrder(CalcOrder);
	    			CT.setConceptVariable(ConceptVariable);
	    			CT.setErrorMessage("");
	    			CT.setQtyValue(BigDecimal.valueOf(0.00));
	    			CT.setResultValue(BigDecimal.valueOf(0.00));
	    			ConceptTypes[i] = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
	    			//ConceptTypes[i]=CT;
	    			ConceptTypes[i].setCalcOrder(CalcOrder);
	    			ConceptTypes[i].setConceptVariable(ConceptVariable);
	    			ConceptTypes[i].setErrorMessage("");
	    			ConceptTypes[i].setQtyValue(BigDecimal.valueOf(0.00));
	    			ConceptTypes[i].setResultValue(BigDecimal.valueOf(0.00));
//if (CalcOrder >= 150000 && CalcOrder <= 150999) {
//	log.warning("....CTInit CReate...i:"+i+"   ConceptVariable:"+ConceptVariable+"  CalcOrder:"+ConceptTypes[i].getCalcOrder()+"  QtyValue:"+ConceptTypes[i].getQtyValue()+"  ResultValue:"+ConceptTypes[i].getResultValue());
//}
					i=i+1;
	            }
	            ConIndex=i;
	            ConIndexFT=i;
		    }
		    catch (SQLException e)
		    {
		    	//log.warning("catch");
		    }
			finally
			{
				DB.close(rse, pstmte);
				rse = null; 
				pstmte = null;
			}
//for (i = 0; i < ConIndex; ++i) {
//	log.warning("....CTInit.Review...i:"+i+"  ConIndex:"+ConIndex+"  CT.CalcOrder:"+ConceptTypes[i].getCalcOrder()+"  CT.ConceptVariable:"+ConceptTypes[i].getConceptVariable()+"  CT.QtyValue:"+ConceptTypes[i].getQtyValue()+"  CT.ResultValue:"+ConceptTypes[i].getResultValue());
//}
	
		} else {
			for (i=0; i < ConIndex; i++) {
    			ConceptTypes[i].setErrorMessage("");
    			ConceptTypes[i].setQtyValue(BigDecimal.valueOf(0.00));
    			ConceptTypes[i].setResultValue(BigDecimal.valueOf(0.00));
			}
		}
		
	    return true;
	}
	
    /**
     * The number of ConceptTypes in the list. 
     */
	static public int CTgetLength() {
//        return ConceptTypes.length;
        return ConIndex;
    }
	 
	/**
	 * 
	 * @param p_CalcOrder
	 * @param p_ConceptVariable
	 * @param p_QtyValue
	 * @param p_ResultValue
	 * @param p_ErrorMessage
	 * @return
	 */
	static public boolean CTaddConceptResults (int p_CalcOrder, String p_ConceptVariable, 
    		BigDecimal p_QtyValue, BigDecimal p_ResultValue, String p_ErrorMessage) 
    {
		//if (p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADCM") || p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADPA")) {
		//	log.warning("...ADD... :"+p_ConceptVariable+"  p_QtyValue="+p_QtyValue+"  p_ResultValue="+p_ResultValue);
		//	}
		int Index = 0;
		if (CTcontains(p_ConceptVariable) < 0 ) {
			// CREATE NEW RECORD IF NOT EXISTS
			Index = ConIndex+1;
			ConceptTypes[Index] = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
//			if (p_ConceptVariable.contains("QT_") || p_ConceptVariable.contains("RS_")) {
//				log.warning("....CTaddConceptResults.CREATE..ConceptVariable:"+p_ConceptVariable+"  CalcOrder:"+p_CalcOrder+"  QtyValue:"+p_QtyValue+"  ResultValue:"+p_ResultValue);
//			}
		} else {
			// UPDATE RECORD IF EXIST
			Index= CTcontains(p_ConceptVariable);
//			if (p_ConceptVariable.contains("QT_") || p_ConceptVariable.contains("RS_")) {
//				log.warning("....CTaddConceptResults.UPDATE..ConceptVariable:"+p_ConceptVariable+"  CalcOrder:"+p_CalcOrder+"  QtyValue:"+p_QtyValue+"  ResultValue:"+p_ResultValue);
//			}
		}
		//ConceptTypes[i]=CT;
		ConceptTypes[Index].setCalcOrder(p_CalcOrder);
		ConceptTypes[Index].setConceptVariable(p_ConceptVariable);
		ConceptTypes[Index].setErrorMessage(p_ErrorMessage);
		ConceptTypes[Index].setQtyValue(p_QtyValue);
		ConceptTypes[Index].setResultValue(p_ResultValue);
		
    	return true;
    }
	
	/**
	 * 
	 * @param p_ConceptVariable
	 * @param p_QtyValue
	 * @param p_ResultValue
	 * @param p_ErrorMessage
	 * @return
	 */
	public static boolean CTupdateConceptResults ( String p_ConceptVariable, 
    		BigDecimal p_QtyValue, BigDecimal p_ResultValue, String p_ErrorMessage) 
    {
		//if (p_ConceptVariable.contains("QT_") || p_ConceptVariable.contains("RS_")) {
		//log.warning("....CTupdateConceptResults...ConceptVariable:"+p_ConceptVariable+"  QtyValue:"+p_QtyValue+"  ResultValue:"+p_ResultValue);
		//}
		//if (p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADCM") || p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADPA")) {
		//log.warning("...UPDATE... :"+p_ConceptVariable+"  p_QtyValue="+p_QtyValue+"  p_ResultValue="+p_ResultValue);
		//}
		int Index = 0;
		Index=CTcontains(p_ConceptVariable);
		if ( Index > 0 ) {
			//if (p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADCM") || p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADPA")) {
			//log.warning("    Antes... :"+p_ConceptVariable+"  QtyValue="+ConceptTypes[Index].getQtyValue()+"  p_ResultValue="+ConceptTypes[Index].getResultValue()); }
			// UPDATE RECORD IF EXIST
			ConceptTypes[Index].setQtyValue(p_QtyValue);
			ConceptTypes[Index].setResultValue(p_ResultValue);
			if (!p_ErrorMessage.equalsIgnoreCase("") && p_ErrorMessage != null) {
				ConceptTypes[Index].setErrorMessage(p_ErrorMessage);
			}
			//if (p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADCM") || p_ConceptVariable.equalsIgnoreCase("ANTIGUEDADPA")) {
			//log.warning("   Despues.. :"+p_ConceptVariable+"  QtyValue="+ConceptTypes[Index].getQtyValue()+"  p_ResultValue="+ConceptTypes[Index].getResultValue()); }
		}
		return false;
    }

    /**
     * Checks if the ConceptVariable is a member of this list. 
     * @param ConceptVariable  Strig whose presence in this list is to be tested. 
     * @return  index if this list contains the String -1 if not. 
     */
	public static int CTcontains(String ConceptVariable) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    	for (int i = 0; i < ConIndex; ++i) {
    		CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT = ConceptTypes[i];
    		//log.warning("....CTcontains...i:"+i+"  ConIndex:"+ConIndex+"   ConceptVariable:"+ConceptVariable+"  CT.ConceptVariable:"+CT.getConceptVariable()+"  CT.QtyValue:"+CT.getQtyValue()+"  CT.ResultValue:"+CT.getResultValue());
            if ( CT.getConceptVariable().equalsIgnoreCase(ConceptVariable) ) {
                return i;
            }
        }
        return -1;
    }
	/**
	 *  DVInit
	 * @param p_AD_Client_ID
	 * @param p_AMN_Payroll_ID
	 * @return
	 */
	static public boolean DVInit(Integer p_AD_Client_ID, Integer p_AMN_Payroll_ID, Integer p_AMN_Process_ID) {

		// Default Values Init
		int i =0;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_ID, null);
		BigDecimal VarDefValueBD = BigDecimal.ZERO;
		
		List<String> DV_Variables = AmerpPayrollCalcUtilDVFormulas.initDV_Variables (Env.getCtx(), p_AMN_Process_ID);
		String Mess ="\r\n";
		String errorResult="";
		String element="";
		for (i = 0; i < DV_Variables.size() ; i++) {
			element=DV_Variables.get(i);
			DVRulesTypes[i] = new  AmerpRulesTypes(0,0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
			//ConceptTypes[i]=CT;
			DVRulesTypes[i].setRuleOrder(i);
			DVRulesTypes[i].setAMN_Payroll_ID(p_AMN_Payroll_ID);;
			DVRulesTypes[i].setRuleVariable(element);
			DVRulesTypes[i].setRuleDescription("");
			DVRulesTypes[i].setErrorMessage("");
			DVRulesTypes[i].setQtyValue(BigDecimal.ONE);
			DVRulesTypes[i].setResultValue(BigDecimal.ZERO);
		}
		for (i = 0; i < DV_Variables.size() ; i++) {
			errorResult="OK";
			element=DVRulesTypes[i].getRuleVariable();
			// Calculates element DV_XXXXX
			try {
				VarDefValueBD = AmerpPayrollCalcUtilDVFormulas.processDefaultValue(Env.getCtx(), element, p_AMN_Payroll_ID, amnpayroll.get_TrxName() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorResult="** Default Values Init - Error on calc **";
			}
			if (VarDefValueBD == null) {
				VarDefValueBD = BigDecimal.ZERO;
			}
			DVRulesTypes[i].setResultValue(VarDefValueBD.setScale(2, RoundingMode.HALF_UP));
			DVRulesTypes[i].setErrorMessage(errorResult);
			Mess = Mess + "No.="+i+"  "+element + "(created)="+VarDefValueBD+"\r\n";
		}
        DVRulIndex=i;
        DVRulIndexFT=i;
        //log.warning(Mess);
	    return true;
	}
	
    /**
     * RTInit. Rules Array Init
     * @param p_aD_Client_ID 
     * @param p_AMN_Payroll_ID
     */
	static public boolean RTInit(Integer p_AD_Client_ID, Integer p_AMN_Payroll_ID, Integer p_AMN_Process_ID) {
		int i =0;
		String RuleVariable="";
		String RuleDescription="";
		int RuleOrder=0;
		BigDecimal RTVarMRuleBD = BigDecimal.ZERO;
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_ID, null);
		// sql1
		String sql1 = "SELECT "
				+   "name as codigo, "
                +	"description as description, "
                +	"ruleorder as ruleorder "
                +	"FROM AMN_Rules "
                +	"WHERE IsActive='Y' AND ad_client_id = ? " 
                +	"ORDER BY ruleorder "
                ;
		//log.warning("sql="+sql1);
		PreparedStatement pstmte = null;
	    ResultSet rse = null;
	    try
	    {
    		pstmte = DB.prepareStatement(sql1, null);
            pstmte.setInt(1, p_AD_Client_ID);
            rse = pstmte.executeQuery();	            
            // Reference Concepts Only
            while (rse.next()) {
    			RuleVariable = rse.getString(1).trim();
    			RuleDescription = rse.getString(2).trim();
            	RuleOrder= rse.getInt(3);
    			//log.warning("i="+i+ "  RuleVariable="+RuleVariable);
    			RulesTypes[i] = new  AmerpRulesTypes(0,0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    			//ConceptTypes[i]=CT;
    			RulesTypes[i].setRuleOrder(RuleOrder);
    			RulesTypes[i].setAMN_Payroll_ID(p_AMN_Payroll_ID);;
    			RulesTypes[i].setRuleVariable(RuleVariable);
    			RulesTypes[i].setRuleDescription(RuleDescription);
    			RulesTypes[i].setErrorMessage("");
	    		RulesTypes[i].setQtyValue(BigDecimal.ONE);
	    		RulesTypes[i].setResultValue(RTVarMRuleBD);
	    		//log.warning("....RTInit CReate...i:"+i+"   RuleVariable:"+RuleVariable+"  RuleOrder:"+RulesTypes[i].getRuleOrder()+"  QtyValue:"+RulesTypes[i].getQtyValue()+"  ResultValue:"+RulesTypes[i].getResultValue());
				i=i+1;
            }
            RulIndex=i;
            RulIndexFT=i;
	    }
	    catch (SQLException e)
	    {
	    	//log.warning("catch");
	    }
		finally
		{
			DB.close(rse, pstmte);
			rse = null; 
			pstmte = null;
		}
	    //log.warning("RulIndex="+RulIndex);
		for (i = 0; i < RulIndex; ++i) {
			try {
				RuleVariable = RulesTypes[i].getRuleVariable();
				RTVarMRuleBD = AmerpPayrollCalcRules.processRule(Env.getCtx(), RuleVariable, p_AMN_Payroll_ID, amnpayroll.get_TrxName() );
	    		RulesTypes[i].setResultValue(RTVarMRuleBD.setScale(2, RoundingMode.HALF_UP));
	    		//log.warning("....RTInit CReate...i:"+i+"   RuleVariable:"+RuleVariable+"  RuleOrder:"+RulesTypes[i].getRuleOrder()+"  QtyValue:"+RulesTypes[i].getQtyValue()+"  ResultValue:("+RTVarMRuleBD+")="+RulesTypes[i].getResultValue());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				RulesTypes[i].setResultValue(BigDecimal.ZERO);
				RulesTypes[i].setErrorMessage("*** ERROR ON AMN_Rules ***");
				String msg = "AMN_Rules Script Invalid: " + e.toString();
				//log.log(Level.SEVERE, msg, e);
				log.log(Level.WARNING, msg, e);
			}
			//log.warning("....RTInit.Review...i:"+i+"  RulIndex:"+RulIndex+"  RT.RuleOrder:"+RulesTypes[i].getRuleOrder()+"  RT.RuleVariable:"+RulesTypes[i].getRuleVariable()+"  RT.QtyValue:"+RulesTypes[i].getQtyValue()+"  RT.ResultValue:"+RulesTypes[i].getResultValue());
		}
		//log.warning("...Rules after INIT ...");
		//AmerpPayrollCalc.logRulesShow();
	    return true;
	}

	
	/**
     * Returns the <code>indexth item in the collection. The index 
     * starts at 0. 
     * @param index  index into the collection. 
     * @return  QtyValue , ResultValue , ErrorMessage
     * @exception XSException
     *   INDEX_SIZE_ERR: if <code>index is greater than or equal to the 
     *   number of objects in the list.
     */
    /* CalcOrder */
	public int getCalcOrder(int index) 
        throws Exception {
        
        if(index < 0 || index > ConIndex - 1) {
            throw new RuntimeException("CONCEPT INDEX SIZE ERR");
        }
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[index];
		return CT.getCalcOrder();
    }
    public static void setCalcOrder(int p_index, int p_CalcOrder) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[p_index];
    	CT.setCalcOrder(p_CalcOrder);
    	ConceptTypes[p_index]=CT;
    }
    /* ConceptVariable */
	public static String getConceptVariable(int index) 
            throws Exception {
	            if(index < 0 || index > ConIndex - 1) {
	                throw new RuntimeException("CONCEPT INDEX SIZE ERR");
	        }
    		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT=ConceptTypes[index];
            return CT.ConceptVariable;
    }
	public static void setConceptVariable(int p_index, String p_ConceptVariable) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[p_index];
		CT.setConceptVariable(p_ConceptVariable);
		ConceptTypes[p_index]=CT;
    }

    /* QtyValue */
	public static BigDecimal getQtyValue(int index) 
        throws Exception {
        
        if(index < 0 || index > ConIndex - 1) {
            throw new RuntimeException("CONCEPT INDEX SIZE ERR");
        }
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[index];
        return CT.getQtyValue();
    }
	public static void setQtyValue(int p_index, BigDecimal p_QtyValue) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[p_index];
    	CT.setQtyValue(p_QtyValue);
    	ConceptTypes[p_index]=CT;
    }
    /* ResultValue */
	public static BigDecimal getResultValue(int index) 
            throws Exception {
            
            if(index < 0 || index > ConIndex - 1) {
                throw new RuntimeException("CONCEPT INDEX SIZE ERR");
            }
    		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT=ConceptTypes[index];
            return CT.getResultValue();
    }
	public static void setResultValue(int p_index, BigDecimal p_ResultValue) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[p_index];
    	CT.setResultValue(p_ResultValue);
    	ConceptTypes[p_index]=CT;
    }
   
    /* ErrorMessage */
	public String getErrorMessage(int index) 
            throws Exception {
            
            if(index < 0 || index > ConIndex - 1) {
                throw new RuntimeException("CONCEPT INDEX SIZE ERR");
            }
    		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT=ConceptTypes[index];
            return CT.getErrorMessage();
    }
	public static void setErrorMessage(int p_index, String p_ErrorMessage) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=ConceptTypes[p_index];
    	CT.setErrorMessage(p_ErrorMessage);
    	ConceptTypes[p_index]=CT;
    }

}



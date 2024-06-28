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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Rules;
import org.amerp.amnmodel.MAMN_Shift;
//import org.compiere.model.MRule;
import org.compiere.model.Scriptlet;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 * @author luisamesty
 * Payroll Calc Rule Methods for Payroll and Personnel Plugin
 * Using MRule Class an AD_Rule table
 * Changed to AMN_Rules New AMN Class copied (not extended) from AD_Rule 
 * 
 */
public class AmerpPayrollCalcRules {

	static CLogger log = CLogger.getCLogger(AmerpPayrollCalcRules.class);
	/**
	 *  processRule:
	 * 	Process the source calling the Rule expression and returning the value
	 *
	 * @throws Exception
	 */
	static public BigDecimal processRule(
		Properties p_ctx, String p_CTVarMRule, int p_AMN_Payroll_ID, String trxName) throws Exception {
		
		BigDecimal retValue = null; //BigDecimal.valueOf(1);
		String retValueStr="";
		String msg = null;
		String ruleValue ="beanshell:"+p_CTVarMRule.trim();
		//String ruleValue = p_CTVarMRule.trim();
		log.info("Processing:=" + p_CTVarMRule );

		// Get some Variables to be Parameters on Rule Scripts
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		Timestamp InvDateAcct;
		Timestamp RefDateIni;
		Timestamp RefDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateEnd=amnpayroll.getInvDateEnd();
		InvDateAcct=amnpayroll.getDateAcct();
		RefDateIni=amnpayroll.getRefDateIni();
		RefDateEnd=amnpayroll.getRefDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		MAMN_Shift amnshift = new MAMN_Shift(p_ctx, amnemployee.getAMN_Shift_ID(), ruleValue);
		// Calculates Years
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
		int EmployeeNYears = list.get(0);
		//log.warning("ruleValue="+ruleValue);
		MAMN_Rules rule = MAMN_Rules.get(p_ctx, ruleValue, amnpayroll.getAD_Client_ID());
		
        ScriptEngineManager manager = new ScriptEngineManager ();
        ScriptEngine engine = manager.getEngineByName ("java");
		engine = rule.getScriptEngine();

		// SET PARAMETERS
		// Window context are    W_
		// Login context  are    G_
		MAMN_Rules.setContext(engine, p_ctx, 0);
		// Method arguments context are A_
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "Ctx", p_ctx);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "AMN_Payroll_ID", p_AMN_Payroll_ID);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "AMN_Employee_ID", amnpayroll.getAMN_Employee_ID());
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "trxName", amnpayroll.get_TrxName());
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "InvDateIni", InvDateIni);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "InvDateEnd", InvDateEnd);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "InvDateAcc", InvDateAcct);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "RefDateIni", RefDateIni);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "RefDateEnd", RefDateEnd);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "EmployeeIncomeDate", EmployeeIncomeDate);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "EmployeeNYears", EmployeeNYears);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "Record_ID", p_AMN_Payroll_ID);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "AD_Client_ID", amnpayroll.getAD_Client_ID());
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "AD_Org_ID", amnpayroll.getAD_Org_ID());
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "AD_User_ID", Env.AD_USER_ID);
		engine.put(MAMN_Rules.ARGUMENTS_PREFIX + "AMN_Shift_ID", amnshift.getAMN_Shift_ID());
		//engine.put(MRule.ARGUMENTS_PREFIX + "AD_PInstance_ID", pi.getAD_PInstance_ID());
		//engine.put(MRule.ARGUMENTS_PREFIX + "Table_ID", pi.getTable_ID());
		//log.warning("Antes..CTVarMRule:"+p_CTVarMRule+"  AMN_Payroll_ID:"+p_AMN_Payroll_ID+"   retValue:"+retValue);
		// now add the callout parameters windowNo, tab, field, value, oldValue to the engine
		try
		{
			retValueStr = engine.eval(rule.getScript()).toString();
			retValue = new BigDecimal(retValueStr);
		}
		catch (Exception e)
		{
			msg = "AMN_Rules Script Invalid: " + e.toString();
			//log.log(Level.SEVERE, msg, e);
			log.log(Level.WARNING, msg, e);
			//throw new AdempiereUserError("Error executing script " + ruleValue);
		}
		//log.warning("Despues..CTVarMRule:"+p_CTVarMRule+"  AMN_Payroll_ID:"+p_AMN_Payroll_ID+"   retValue:"+retValue);
		return retValue;
	}
	
	static public double getAbsoluteValue() {
		double absValue = 0;
		absValue = 50 ;
		return absValue;
	}
	
}

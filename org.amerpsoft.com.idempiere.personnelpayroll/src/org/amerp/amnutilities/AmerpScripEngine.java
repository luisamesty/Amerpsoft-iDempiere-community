package org.amerp.amnutilities;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.compiere.model.MRule;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class AmerpScripEngine {
	
	static CLogger log = CLogger.getCLogger(AmerpScripEngine.class);

	/**
	 *  processRule:
	 * 	Process the source calling the Rule expression and returning the value
	 *
	 * @throws Exception
	 */
	static public BigDecimal processRule(Properties p_ctx, String p_CTVarMRule, int p_AMN_Payroll_ID, String trxName) throws Exception {
		
		BigDecimal retValue = null; //BigDecimal.valueOf(1);
		String retValueStr="";
		String msg = null;
		String ruleValue ="beanshell:"+p_CTVarMRule.trim();
		//String ruleValue = p_CTVarMRule.trim();
		log.info("Processing:=" + p_CTVarMRule );

		// Get some Variables to be Parameters on Rule Scripts
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Calculates Years
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
		list.get(0);
		//
		if (ruleValue == null || ruleValue.length() == 0) {
			msg = "FieldExpression not defined";
			throw new AdempiereUserError(msg);
		}
		// Find MRule by Name
		//log.warning("ruleValue:"+ruleValue);
		MRule rule = MRule.get(p_ctx, ruleValue);
		
        ScriptEngineManager manager = new ScriptEngineManager ();
        ScriptEngine engine = manager.getEngineByName ("java");
        String script = rule.getScript();
System.out.println("rule.getScript(): "+script);
        // Execute Script
        try
		{
			retValueStr = engine.eval(script).toString();
			retValue = new BigDecimal(retValueStr);
		} catch (Exception e)
		{
			msg = "Script Invalid: " + e.toString();
			log.log(Level.SEVERE, msg, e);
			throw new AdempiereUserError("Error executing script " + ruleValue);
		}
System.out.println("CTVarMRule:"+p_CTVarMRule+"  AMN_Payroll_ID:"+p_AMN_Payroll_ID+"   retValue:"+retValue);
		return retValue;
	}

}
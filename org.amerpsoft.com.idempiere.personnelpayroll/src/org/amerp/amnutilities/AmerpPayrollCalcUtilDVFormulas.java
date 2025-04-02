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
import org.amerp.amnmodel.MAMN_Concept_Types;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Employee_Salary;
import org.amerp.amnmodel.MAMN_NonBusinessDay;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Assist;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnmodel.MAMN_Shift;
import org.compiere.model.MCountry;
import org.compiere.model.MCurrency;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import java.lang.System.Logger.Level;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

/**
 * @author luisamesty
 * AmerpPayrollCalcUtilFormulas
 *  Util Formulas to be applied using 
 *  External MRule formulas
 * 	Using MRule Class an AD_Rule table
 * Common Parameters:
 * 	Properties Ctx
 *  int AMN_Payroll_ID
 *  String trxName
 * Default Names: 
 * DV_XXXXXXX  for these Functions Values
 * RV_XXXXXXX	for MRule Values
 * 
 * 25-11-2019 Luis Amesty Added three new variables DV_SALARYYTD  DV_SALARYTAXYTD  DV_TAXYTD DV_SSOYTD
 * Year to Day Salary Ammount Pay and Tax deduct from SQL Function  amp_special_wh_hist_calc 
 * 	DV_SALARYYTD: 	'salary_base' 
 * 	DV_SALARYTAXYTD	'salary_gravable'
 *  DV_TAXYTD 'Tax cumulated YTD'
 *  DV_SSOYTD 'Social Security cumulated YTD'
 */
public class AmerpPayrollCalcUtilDVFormulas {
	
	static CLogger log = CLogger.getCLogger(AmerpPayrollCalcUtilDVFormulas.class);
	/**
	 *  processRule:
	 * 	Process the source calling the Rule expression and returning the value
	 *
	 * @throws Exception
	 */
	
	public static List<String> listDV_Variables = new ArrayList<String>();
	
    // Indicates if Saturday is considered Business Day */
    public static boolean isSaturdayBusinessDay = MAMN_Payroll_Assist.SaturdayBusinessDay;
    public static boolean SundayBusinessDay = MAMN_Payroll_Assist.SundayBusinessDay;
    
    /**
	 * initDV_Variables
	 * @return List<String>
	 */
	public static List<String> initDV_Variables (Properties p_ctx, Integer p_AMN_Process_ID) {
		listDV_Variables = new ArrayList<String>();
		
		String Process_Value = "NN";
		MAMN_Process amnprocess = new MAMN_Process(p_ctx,p_AMN_Process_ID, null);
		Process_Value= amnprocess.getAMN_Process_Value();
		
		// BEGIN OF LIST
		// YTD Year to Date (Date Before Recipt Date)
		listDV_Variables.add("DV_SALARYYTD"); 		// 'salary_base' 
		listDV_Variables.add("DV_SALARYTAXYTD");	// 'salary_gravable'
		listDV_Variables.add("DV_TAXYTD");			// 'Tax cumulated YTD'
		listDV_Variables.add("DV_SSOYTD");			// 'Social Security cumulated YTD'
		// NN PAYROLL AND COMMON VARIABLES
		listDV_Variables.add("DV_SALARY");
		listDV_Variables.add("DV_SALARIO");
		listDV_Variables.add("DV_DAYS");
		listDV_Variables.add("DV_DIAS");
		listDV_Variables.add("DV_HOURS");
		listDV_Variables.add("DV_HORAS");
		listDV_Variables.add("DV_TRANSPORTBONUS");
		listDV_Variables.add("DV_BONOTRANSPOR");
		listDV_Variables.add("DV_ATTENDANCEBONUS");
		listDV_Variables.add("DV_BONOASIST");
		listDV_Variables.add("DV_RESTDAYS");
		listDV_Variables.add("DV_DESCANSO");
		listDV_Variables.add("DV_HOLLIDAYS");
		listDV_Variables.add("DV_FERIADO");

		//  NV VACATION
		if (Process_Value.compareToIgnoreCase("NV")==0) {
			listDV_Variables.add("DV_SALVACAC");
			listDV_Variables.add("DV_VACACION");
			listDV_Variables.add("DV_VACAC190");
			listDV_Variables.add("DV_VACAC192");
			listDV_Variables.add("DV_VACAC192A");
			listDV_Variables.add("DV_VACACFER");
			listDV_Variables.add("DV_VACACDES");
			listDV_Variables.add("DV_VACACDIADIC");
			listDV_Variables.add("DV_VACACDIA45");
		}
		
		// NP Social Benenfits
		if (Process_Value.compareToIgnoreCase("NP")==0 ) {
			listDV_Variables.add("DV_SALARYSB");
			listDV_Variables.add("DV_SALARIOP");
			listDV_Variables.add("DV_SALPREST");
			listDV_Variables.add("DV_SALARYLAST");
			listDV_Variables.add("DV_ABONOPS");
			listDV_Variables.add("DV_SALARY12SB");
			listDV_Variables.add("DV_SALARIO12P");
			listDV_Variables.add("DV_SAL12PREST");
			listDV_Variables.add("DV_ABONOPSA");
			listDV_Variables.add("DV_ABONOMPSA");
			listDV_Variables.add("DV_INCUTILID");
			listDV_Variables.add("DV_INCVACAC");
			listDV_Variables.add("DV_INCVACACADIC");
		}
		// PI-PL
		if (Process_Value.compareToIgnoreCase("PI")==0 ||
				Process_Value.compareToIgnoreCase("PL")==0) {
			listDV_Variables.add("DV_INTERPRESTAC");
		}
		// PR-PL
		if (Process_Value.compareToIgnoreCase("PR")==0 ||
				Process_Value.compareToIgnoreCase("PL")==0) {
			listDV_Variables.add("DV_ACUMPRESTAC");
			listDV_Variables.add("DV_ACUANTPRESTAC");
		}
		// PL ONLY
		if (Process_Value.compareToIgnoreCase("PL")==0) {
			// 
			listDV_Variables.add("DV_FSALARY6M");
			listDV_Variables.add("DV_FVACAC190");
			listDV_Variables.add("DV_FVACAC190ADI");
			listDV_Variables.add("DV_FVACAC192");
			listDV_Variables.add("DV_FVACAC192A");
			listDV_Variables.add("DV_FUTILIDDEV11");
			listDV_Variables.add("DV_FUTILIDDEV12");
			listDV_Variables.add("DV_FDAYSFRAC");
			listDV_Variables.add("DV_FDAYS_THISYEAR");
			listDV_Variables.add("DV_FYEAR_SERVICE");
			listDV_Variables.add("DV_FDAYS_SERVICE");
		}
		// NU
		if (Process_Value.compareToIgnoreCase("NU")==0) {
			listDV_Variables.add("DV_SALUTIL");
			listDV_Variables.add("DV_UTILIDADES");
			listDV_Variables.add("DV_UTILIDDEV");
			listDV_Variables.add("DV_UTILIDDEV6M");
			listDV_Variables.add("DV_UTILIDDEV12M");
			listDV_Variables.add("DV_UTILANT");
			listDV_Variables.add("DV_UTILDEF");
			listDV_Variables.add("DV_UTILADIC");
		}
		// END OF LIST
		return listDV_Variables;
	}
	
	static public BigDecimal processDefaultValue(Properties p_ctx, String p_CTVarDefValue, int p_AMN_Payroll_ID, String trxName) throws Exception {
		
		BigDecimal retValue = BigDecimal.ZERO; //BigDecimal.valueOf(1);
		//log.warning("p_CTVarDefValue="+p_CTVarDefValue+"  p_AMN_Payroll_ID="+ p_AMN_Payroll_ID);		
		// ***********************
		// NN Default Values VAR
		// ***********************
		if 	((p_CTVarDefValue.equalsIgnoreCase("DV_SALARY")) || (p_CTVarDefValue.equalsIgnoreCase("DV_SALARIO"))) {
			retValue = DV_SALARY(p_ctx, p_AMN_Payroll_ID, trxName);
		} else if  	((p_CTVarDefValue.equalsIgnoreCase("DV_DAYS")) || (p_CTVarDefValue.equalsIgnoreCase("DV_DIAS")))  {
			retValue = DV_DAYS(p_ctx, p_AMN_Payroll_ID, trxName);
		} else if  	((p_CTVarDefValue.equalsIgnoreCase("DV_HOURS")) || (p_CTVarDefValue.equalsIgnoreCase("DV_HORAS")))  {
			retValue = DV_DAYS(p_ctx, p_AMN_Payroll_ID, trxName);
		} else if  	((p_CTVarDefValue.equalsIgnoreCase("DV_TRANSPORTBONUS")) || (p_CTVarDefValue.equalsIgnoreCase("DV_BONOTRANSPOR"))) {
				retValue = DV_TRANSPORTBONUS(p_ctx, p_AMN_Payroll_ID, trxName);
		} else if   ((p_CTVarDefValue.equalsIgnoreCase("DV_ATTENDANCEBONUS"))  || (p_CTVarDefValue.equalsIgnoreCase("DV_BONOASIST"))) {
			retValue = DV_ATTENDANCEBONUS(p_ctx, p_AMN_Payroll_ID, trxName);
		} else if  	((p_CTVarDefValue.equalsIgnoreCase("DV_RESTDAYS")) ||(p_CTVarDefValue.equalsIgnoreCase("DV_DESCANSO"))) {
			retValue = DV_RESTDAYS(p_ctx, p_AMN_Payroll_ID, trxName);
		} else if  	((p_CTVarDefValue.equalsIgnoreCase("DV_HOLLIDAYS")) ||(p_CTVarDefValue.equalsIgnoreCase("DV_FERIADO"))) {
			retValue = DV_HOLLIDAYS(p_ctx, p_AMN_Payroll_ID, trxName);
		}

		// ***********************
		// NV default Values 
		// ***********************
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_SALVACAC")) {
			retValue = DV_SALVACAC(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACACION")) {
			retValue = DV_VACACION(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACAC190")) {
			retValue = DV_VACAC190(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACAC192")) {
			retValue = DV_VACAC192(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACAC192A")) {
			retValue = DV_VACAC192A(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACACFER")) {
			retValue = DV_VACACFER(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACACDES")) {
			retValue = DV_VACACDES(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACACDIADIC")) {
			retValue = DV_VACACDIADIC(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_VACACDIA45")) {
			retValue = DV_VACACDIA45(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		// **********************************
		// NP default Values Social Benefits
		// **********************************
		else if 	((p_CTVarDefValue.equalsIgnoreCase("DV_SALARYSB")) 
				|| (p_CTVarDefValue.equalsIgnoreCase("DV_SALARIOP"))
				|| (p_CTVarDefValue.equalsIgnoreCase("DV_SALPREST"))) {
			retValue = DV_SALARYSB(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_SALARYLAST")) {
			retValue = DV_SALARYLAST(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_ABONOPS")) {
			retValue = DV_ABONOPS(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	((p_CTVarDefValue.equalsIgnoreCase("DV_SALARY12SB")) 
				|| (p_CTVarDefValue.equalsIgnoreCase("DV_SALARIO12P"))
				|| (p_CTVarDefValue.equalsIgnoreCase("DV_SAL12PREST"))) {
			retValue = DV_SALARY12SB(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	((p_CTVarDefValue.equalsIgnoreCase("DV_ABONOPSA"))
			|| (p_CTVarDefValue.equalsIgnoreCase("DV_ABONOMPSA"))) {
			retValue = DV_ABONOPSA(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_INCUTILID")) {
			retValue = DV_INCUTILID(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	((p_CTVarDefValue.equalsIgnoreCase("DV_INCVACAC"))
				|| (p_CTVarDefValue.equalsIgnoreCase("DV_INCVACACADIC"))) {
			retValue = DV_INCVACAC(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		// *******************************************
		// PI default Value Interest Social Benefits
		// *******************************************
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_INTERPRESTAC")) {
			retValue = DV_INTERPRESTAC(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		// ***********************************************************
		// PR default Value Anticipate payment Social Benefits
		// ***********************************************************
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_ACUMPRESTAC")) {
			//log.warning("......DV_ACUMPRESTAC");
			retValue = DV_ACUMPRESTAC(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_ACUANTPRESTAC")) {
			//log.warning("......DV_ACUANTPRESTAC");
			retValue = DV_ACUANTPRESTAC(p_ctx, p_AMN_Payroll_ID, trxName);
		}

		// ***********************
		// NU default Values 
		// ***********************
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_SALUTIL")) {
			retValue = DV_SALUTIL(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILIDADES")) {
			retValue = DV_UTILIDADES(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILIDDEV")) {
			retValue = DV_UTILIDDEV(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILIDDEV6M")) {
			retValue = DV_UTILIDDEV6M(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILIDDEV12M")) {
			retValue = DV_UTILIDDEV12M(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILANT")) {
			retValue = DV_UTILANT(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILDEF")) {
			retValue = DV_UTILDEF(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_UTILADIC")) {
			retValue = DV_UTILADIC(p_ctx, p_AMN_Payroll_ID, trxName);
		}

		// ***********************
		// YTD Default Values VAR
		// ***********************
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_SALARYYTD")) {
			retValue = DV_SALARYYTD(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_SALARYTAXYTD")) {
			retValue = DV_SALARYTAXYTD(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_TAXYTD")) {
			retValue = DV_TAXYTD(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_SSOYTD")) {
			retValue = DV_SSOYTD(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		
		// ***********************
		// PL default Values 
		// ***********************
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FSALARY6M")) {
			retValue = DV_FSALARY6M(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FVACAC190")) {
			retValue = DV_FVACAC190(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FVACAC190ADI")) {
			retValue = DV_FVACAC190ADI(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FVACAC192")) {
			retValue = DV_FVACAC192(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FVACAC192A")) {
			retValue = DV_FVACAC192A(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FUTILIDDEV11")) {
			retValue = DV_FUTILIDDEV11(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FUTILIDDEV12")) {
			retValue = DV_FUTILIDDEV12(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FDAYSFRAC")) {
			retValue = DV_FDAYSFRAC(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FDAYS_THISYEAR")) {
			retValue = DV_FDAYS_THISYEAR(p_ctx, p_AMN_Payroll_ID, trxName);
		}
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FYEAR_SERVICE")) {
			retValue = DV_FYEAR_SERVICE(p_ctx, p_AMN_Payroll_ID, trxName);
		}	
		else if 	(p_CTVarDefValue.equalsIgnoreCase("DV_FDAYS_SERVICE")) {
			retValue = DV_FDAYS_SERVICE(p_ctx, p_AMN_Payroll_ID, trxName);
		}	
		// ***********************
		// END
		// ***********************
		
		else
			retValue = BigDecimal.valueOf(0);
		//log.warning("CTVarDefValue:"+p_CTVarDefValue+"  AMN_Payroll_ID:"+p_AMN_Payroll_ID+"   retValue:"+retValue);
		if (retValue.equals(null))
			return BigDecimal.ZERO;
		//return retValue.setScale(2, RoundingMode.HALF_UP);
		return retValue;
	}
	
	/**
	 * processLastValueQTY : Return Last Value QTY from AMN_Payroll_Detail Table
	 * @param p_ctx
	 * @param p_AMN_Processs_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_ID
	 * @param trxName
	 * @return
	 * @throws Exception
	 */
	static public BigDecimal processLastValueQTY(Properties p_ctx, int p_AMN_Processs_ID, int p_AMN_Contract_ID, int p_AMN_Payroll_ID, int p_AMN_Concept_Types_ID, String trxName) throws Exception {
		// Local Variables
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, trxName);
		int AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		Timestamp DateAcct = amnpayroll.getDateAcct();
		BigDecimal retValue = BigDecimal.ZERO;
		// SQL 
		String sql = "SELECT DISTINCT ON ( cty.AMN_Concept_Types_ID ) "+
		" pd.qtyvalue "+
		" FROM AMN_Payroll as pa "+
		" LEFT JOIN AMN_Employee as emp ON emp.AMN_Employee_ID = pa.AMN_Employee_ID "+
		" LEFT JOIN AMN_Payroll_Detail as pd ON pd.AMN_Payroll_ID = pa.AMN_Payroll_ID "+
		" LEFT JOIN AMN_Concept_Types_Proc as ctp ON ctp.AMN_Concept_Types_Proc_ID = pd.AMN_Concept_Types_Proc_ID "+
		" LEFT JOIN AMN_Concept_Types cty ON cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID "+
		" WHERE pa.AMN_Payroll_ID <> " + p_AMN_Payroll_ID +
		" AND pa.DateAcct < '" + DateAcct + "'" +
		" AND ctp.AMN_Concept_Types_ID = " + p_AMN_Concept_Types_ID +
		" AND pa.AMN_Process_ID = " + p_AMN_Processs_ID +
		" AND pa.AMN_Contract_ID= "+ p_AMN_Contract_ID +
		" AND pa.AMN_Contract_ID= "+ p_AMN_Contract_ID +
		" AND emp.AMN_Employee_ID= "+ AMN_Employee_ID +
		" ORDER BY  cty.AMN_Concept_Types_ID, pa.InvDateEnd DESC ";
		//log.warning("sql2="+sql);

		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				retValue = rspc.getBigDecimal(1);
			}				
		}
		catch (SQLException e)
		{
			retValue = BigDecimal.valueOf(0);
		}
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		if (retValue.equals(null))
			return BigDecimal.ZERO;
		return retValue;
	}

	/**
	 * processLastValueRS: Return Last Value RESULT from AMN_Payroll_Detail Table
	 * @param p_ctx
	 * @param p_AMN_Processs_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_ID
	 * @param trxName
	 * @return
	 * @throws Exception
	 */
	static public BigDecimal processLastValueRS(Properties p_ctx, int p_AMN_Processs_ID, int p_AMN_Contract_ID, int p_AMN_Payroll_ID, int p_AMN_Concept_Types_ID, String trxName) throws Exception {
		// Local Variables
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, trxName);
		int AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		Timestamp DateAcct = amnpayroll.getDateAcct();
		BigDecimal retValue = BigDecimal.ZERO;
		// SQL 
		String sql = "SELECT DISTINCT ON ( cty.AMN_Concept_Types_ID ) "+
		" pd.amountcalculated "+
		" FROM AMN_Payroll as pa "+
		" LEFT JOIN AMN_Employee as emp ON emp.AMN_Employee_ID = pa.AMN_Employee_ID "+
		" LEFT JOIN AMN_Payroll_Detail as pd ON pd.AMN_Payroll_ID = pa.AMN_Payroll_ID "+
		" LEFT JOIN AMN_Concept_Types_Proc as ctp ON ctp.AMN_Concept_Types_Proc_ID = pd.AMN_Concept_Types_Proc_ID "+
		" LEFT JOIN AMN_Concept_Types cty ON cty.AMN_Concept_Types_ID = ctp.AMN_Concept_Types_ID "+
		" WHERE pa.AMN_Payroll_ID <> " + p_AMN_Payroll_ID +
		" AND pa.DateAcct <  '" + DateAcct + "'" +
		" AND ctp.AMN_Concept_Types_ID = " + p_AMN_Concept_Types_ID +
		" AND pa.AMN_Process_ID = " + p_AMN_Processs_ID +
		" AND pa.AMN_Contract_ID= "+ p_AMN_Contract_ID +
		" AND pa.AMN_Contract_ID= "+ p_AMN_Contract_ID +
		" AND emp.AMN_Employee_ID= "+ AMN_Employee_ID +
		" ORDER BY  cty.AMN_Concept_Types_ID, pa.InvDateEnd DESC ";
		//log.warning("sql="+sql);
		
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				retValue = rspc.getBigDecimal(1);
			}				
		}
		catch (SQLException e)
		{
			retValue = BigDecimal.valueOf(0);
		}
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		if (retValue.equals(null))
			return BigDecimal.ZERO;
		return retValue;
	}

	// ********************************************************
	// ********************************************************
	// PROCESSES	PAYROLL   NN
	// ********************************************************
	// ********************************************************
	/**
	 *  processDefaultValue: DV_SALARY
	 * 	Description: Employee Monthly Salary.
	 * 		Calculates Average last 6 months
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_SALARY (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Employee_Salary = BigDecimal.ZERO;
		BigDecimal Historic_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		Employee_Salary=amnemployee.getSalary();
		if (Employee_Salary == null)
			Employee_Salary = BigDecimal.ZERO;
		// Calculates Average last 6 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateIni());
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//StartDate=
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//EndDate	= cal.
		cal.add (Calendar.MONTH, -6);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_base' , ? , ? , ? , ? , ?) / 6 ";
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
			rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Employee_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Historic Salary on Employee Salary Table
		if (Historic_Salary == null)
			Historic_Salary = BigDecimal.ZERO;
		if (Historic_Salary.compareTo(Employee_Salary) < 0) 
			retValue = Employee_Salary;
		else
			retValue = Historic_Salary;
		return retValue;
	}

	/**
	 *  processDefaultValue: DV_DAYS
	 * 	Description: Payroll Period Days.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_DAYS (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		double retValue = 0;	
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateEnd=amnpayroll.getInvDateEnd();
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(InvDateEnd, InvDateIni);
		retValue = list.get(2);
		return BigDecimal.valueOf(retValue);
	}
	/**
	 *  processDefaultValue: DV_HOURS
	 * 	Description: Payroll Period in HOURS Efective (LaborDAYS * 8).
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_HOURS (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		double retValue = 0;
		double LABORDAYS=0;

		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateEnd=amnpayroll.getInvDateEnd();
		// Verify isSaturdayBusinessDay
		MAMN_Shift shift = new MAMN_Shift();
		isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
		LABORDAYS =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, amnpayroll.getAD_Client_ID(), null).doubleValue();
		retValue = LABORDAYS*8;
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_TRANSPORTBONUS
	 * 	Description: Transport Bonus Calculated depending on Attendance days
	 *  For Fast Calc it Returns Working Days from the Payroll Period.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_TRANSPORTBONUS (Properties Ctx, int AMN_Payroll_ID, String trxName) {

			double LABORDAYS;	
			Timestamp InvDateIni;
			Timestamp InvDateEnd;
			int AD_Client_ID;
			MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
			InvDateIni=amnpayroll.getInvDateIni();
			InvDateEnd=amnpayroll.getInvDateEnd();;
			AD_Client_ID=amnpayroll.getAD_Client_ID();
			// Calculates LABORDAY from MAMN_Payroll Class
			// Verify isSaturdayBusinessDay
			MAMN_Shift shift = new MAMN_Shift();
			isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
			LABORDAYS =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, AD_Client_ID, trxName).doubleValue();
			// Return BigDecimal Value
			return BigDecimal.valueOf(LABORDAYS);

	}
	
	/**
	 *  processDefaultValue: DV_ATTENDANCEBONUS
	 * 	Description: Attendance Bonus Calculated depending on Attendance days
	 *  For Fast Calc it Returns Working Days from the Payroll Period.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_ATTENDANCEBONUS (Properties Ctx, int AMN_Payroll_ID, String trxName) {

			double LABORDAYS;	
			Timestamp InvDateIni;
			Timestamp InvDateEnd;
			int AD_Client_ID;
			MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
			InvDateIni=amnpayroll.getInvDateIni();
			InvDateEnd=amnpayroll.getInvDateEnd();;
			AD_Client_ID=amnpayroll.getAD_Client_ID();
			// Verify isSaturdayBusinessDay
			MAMN_Shift shift = new MAMN_Shift();
			isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
			// Calculates LABORDAY from MAMN_Payroll Class
			LABORDAYS =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, AD_Client_ID, trxName).doubleValue();
			// Return BigDecimal Value
			return BigDecimal.valueOf(LABORDAYS);

	}
	
	/**
	 *  processDefaultValue: DV_RESTDAYS
	 * 	Description: Rest Days Calculated depending on Attendance days
	 *  For Fast Calc it Returns Working Days from the Payroll Period.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_RESTDAYS (Properties Ctx, int AMN_Payroll_ID, String trxName) {

			double LABORDAYS,DTREC,NONLABORDAYS,HOLLIDAYS;	
			Timestamp InvDateIni;
			Timestamp InvDateEnd;
			int AD_Client_ID;
			int AD_Org_ID=0;
			MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
			InvDateIni=amnpayroll.getInvDateIni();
			InvDateEnd=amnpayroll.getInvDateEnd();;
			AD_Client_ID=amnpayroll.getAD_Client_ID();
			AD_Org_ID=amnpayroll.getAD_Org_ID();
			// Verify isSaturdayBusinessDay
			MAMN_Shift shift = new MAMN_Shift();
			isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
			// Calculates LABORDAY ,DTREC,NONLABORDAYS,HOLLIDAYS from MAMN_NonBusinessDay Class
			HOLLIDAYS = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, AD_Client_ID, AD_Org_ID).doubleValue();
			LABORDAYS =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, AD_Client_ID, trxName).doubleValue();
			DTREC =1.00+ MAMN_NonBusinessDay.getDaysBetween(InvDateIni, InvDateEnd).doubleValue();
			NONLABORDAYS = DTREC - LABORDAYS;
			// Return BigDecimal Value
			return BigDecimal.valueOf(NONLABORDAYS-HOLLIDAYS);
	}
	/**
	 *  processDefaultValue: DV_HOLLIDAYS
	 * 	Description: Hollidays Calculated depending on Attendance days
	 *  For Fast Calc it Returns Non Working Days from the Payroll Period.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_HOLLIDAYS (Properties Ctx, int AMN_Payroll_ID, String trxName) {

			double HOLLIDAYS;	
			Timestamp InvDateIni;
			Timestamp InvDateEnd;
			int AD_Client_ID;
			int AD_Org_ID=0;
			MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
			InvDateIni=amnpayroll.getInvDateIni();
			InvDateEnd=amnpayroll.getInvDateEnd();;
			AD_Client_ID=amnpayroll.getAD_Client_ID();
			AD_Org_ID=amnpayroll.getAD_Org_ID();
			// Verify isSaturdayBusinessDay
			MAMN_Shift shift = new MAMN_Shift();
			isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
			// Calculates LABORDAY ,DTREC,NONLABORDAYS,HOLLIDAYS from MAMN_NonBusinessDay Class
			HOLLIDAYS = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, AD_Client_ID, AD_Org_ID).doubleValue();
			// Return BigDecimal Value
			return BigDecimal.valueOf(HOLLIDAYS);
	}
	
	// ********************************************************
	// ********************************************************
	// PROCESSES	VACATION   NV
	// ********************************************************
	// ********************************************************
	/**
	 *  processDefaultValue: DV_SALVAC
	 * 	Description: Returns Accrued Salary during last period in therm of Monthly Salary
	 *  Summarizing all Concepts Types where IS_VACAION ='Y'
	 *  For Fast Calc it Returns Salary on Employee on AMN_Employee_ table.
	 *  A Standard Plublic Var similar is R_SALARIO 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_SALVACAC (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Employee_Salary = BigDecimal.ZERO;
		BigDecimal Historic_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		Employee_Salary=amnemployee.getSalary();
		if (Employee_Salary==null)
			Employee_Salary=BigDecimal.ZERO;
		// Calculates Average last 6 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateIni());
		cal.set(Calendar.DAY_OF_MONTH, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//StartDate=
		cal.add (Calendar.MONTH, -1);
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//EndDate	= cal.
		cal.add (Calendar.MONTH, -6);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		//log.warning("Employee:"+amnemployee.getAMN_Employee_ID()+" StartDate:"+StartDate+"  EndDate"+EndDate);
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_vacation' , ? , ? , ? , ?, ? ) / 6 ";
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary=BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NV Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary=BigDecimal.ZERO;
		if (Historic_Salary.compareTo(Employee_Salary) <  0) 
			retValue = Employee_Salary;
		else
			retValue = Historic_Salary;
		return retValue;
	}
	

	/**
	 *  processDefaultValue: DV_VACACION
	 * 	Description: Returns 15 days according to Venezuela Labor Law Art. 190
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACACION (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		
		double retValue = 15;	
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		MCountry country = MCountry.get(Env.getCtx(), amnemployee.getC_Country_ID());
		// VE Venezuela
		if (country.getCountryCode().compareToIgnoreCase("VE")==0 ) {
			retValue = 15;	
		// PARAGUAY
		} else if (country.getCountryCode().compareToIgnoreCase("PY")==0 ) {
			/* Calculates years of Antiquity */
			BigDecimal yearsAnt = AmerpDateUtils.getYearsBetween(amnemployee.getincomedate(), amnpayroll.getInvDateIni());
			//	DE         Hasta        Derecho de Vacaciones 
			//	1 ano    5 años      12 días hábiles corridos. 
			//	6 años 10 años     18 días hábiles corridos. 
			//	11 años Adelante 30 días hábiles corridos.
			/* Compare with Table */
			// Lógica de cálculo
	        if (yearsAnt.compareTo(BigDecimal.ONE) >= 0 && yearsAnt.compareTo(new BigDecimal("5")) <= 0) {
	        	retValue = 12; // 1 año a 5 años: 12 días hábiles corridos
	        } else if (yearsAnt.compareTo(new BigDecimal("6")) >= 0 && yearsAnt.compareTo(new BigDecimal("10")) <= 0) {
	        	retValue = 18; // 6 años a 10 años: 18 días hábiles corridos
	        } else if (yearsAnt.compareTo(new BigDecimal("11")) >= 0) {
	        	retValue = 30; // 11 años en adelante: 30 días hábiles corridos
	        } else {
	        	retValue = 0; // Por debajo de 1 año, 0 días
	        }
		} else if (country.getCountryCode().compareToIgnoreCase("ES")==0 ){
			retValue = 30;	
		}
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_VACAC190
	 * 	Description: Returns Additional Days according to Labor Law Art. 190
	 *  Maximum 15 days
	 *  One Day paid for each year of Employee's service 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACAC190 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;	
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		
		Timestamp InvDateIni;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		EmployeeIncomeDate=amnemployee.getincomedate();
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
		retValue = list.get(0);
		// Corrected on getYearsMonthDaysElapsedBetween
		if (retValue > 0)
			retValue = retValue -1;
		if (retValue > 15)
			retValue = 15;
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_VACAC192
	 * 	Description: Returns 15 Days according to Labor Law Art. 192
	 *  Constant Value 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACAC192 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 15;	
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_VACAC192A
	 * 	Description: Returns Additional Days according to Labor Law Art. 192
	 *  Maximum 15 days
	 *  One Day paid for each year of Employee's service 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACAC192A (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;	
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Calculates Years
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
		retValue = list.get(0);
		// Corrected on getYearsMonthDaysElapsedBetween
		if (retValue > 0)
			retValue = retValue -1;
		if (retValue > 15)
			retValue = 15;
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_VACACFER
	 * 	Description: Returns Hollidays Days Betwen Vacation Period
	 *  Normally National Hollidays 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACACFER (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 15;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		// Verify isSaturdayBusinessDay
		MAMN_Shift shift = new MAMN_Shift();
		isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
		// HOLLIDAYS ON VACATION PERIOD
		retValue = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(isSaturdayBusinessDay, amnpayroll.getInvDateIni(), amnpayroll.getInvDateEnd(), amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID()).doubleValue();

		return BigDecimal.valueOf(retValue);
	}

	/**
	 *  processDefaultValue: DV_VACACDES
	 * 	Description: Returns NonLabor Days Betwen Vacation Period
	 *  Normally Saturdays and Sunday and National Hollidays 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACACDES (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		// Business Days ON VACATION PERIOD
		// Verify isSaturdayBusinessDay
		MAMN_Shift shift = new MAMN_Shift();
		isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
		//retValue = MAMN_NonBusinessDay.sqlGetNonBusinessDay(amnpayroll.getInvDateIni(), amnpayroll.getInvDateEnd(), amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID()).doubleValue();
		double	LABORDAYS = MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, amnpayroll.getInvDateIni(), amnpayroll.getInvDateEnd(), amnpayroll.getAD_Client_ID(), null).doubleValue();
		double DTREC = 1.00+ MAMN_NonBusinessDay.getDaysBetween(amnpayroll.getInvDateIni(), amnpayroll.getInvDateEnd()).doubleValue();
		return BigDecimal.valueOf(DTREC - LABORDAYS);
	}
	/**
	 *  processDefaultValue: DV_VACACDIADIC
	 * 	Description: Returns 3 Days according to Labor Agreement 
	 *  One Day paid for each year of Employee's service 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACACDIADIC (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 3;	
		return BigDecimal.valueOf(retValue);
	}

	/**
	 *  processDefaultValue: DV_VACACDIA45
	 * 	Description: Returns the necessary Days for completing 45 Days in total
	 *  	according to Labor Agreement.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_VACACDIA45 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 1;	
		return BigDecimal.valueOf(retValue);
	}
	
	// ********************************************************
	// ********************************************************
	// PROCESSES	SOCIAL BENEFITS   NP
	// ********************************************************
	// ********************************************************
	/**
	 *  processDefaultValue: DV_SALARYSB  - DV_SALPREST - DV_SALARIOP
	 * 	Description: Returns Salary Amount Base to calc Social Benefits 
	 * 		(Prestaciones Sociales)
	 *  	according to Labor Law.according to Labor Law 142 Lit A.
	 *  	BASED ON 3 MONTH AVERAGE
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_SALARYSB (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 3 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 2 Month Before
		cal.add(Calendar.MONTH, -2);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_socialbenefits' , ? , ? , ? , ? , ? ) / 3 ";
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Null
		if (Historic_Salary == null)
			Historic_Salary = BigDecimal.ZERO;
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		retValue = Historic_Salary;
		return retValue;
	}

	/**
	 *  processDefaultValue: DV_SALARYLAST
	 * 	Description: Return Salary Amount Base to calc Social Benefits 
	 * 		(Prestaciones Sociales)
	 *  	according to Labor Law.according to Labor Law 142 Lit A.
	 *  	BASED ON LAST MONTH SALARY
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_SALARYLAST (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 3 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day same Month 
		cal.add(Calendar.MONTH, -2);
		//cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_socialbenefits' , ? , ? , ? , ? , ? ) ";
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		if (Historic_Salary == null)
			Historic_Salary = BigDecimal.ZERO;
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		retValue = Historic_Salary;
		return retValue;
	}

	/**
	 *  processDefaultValue: DV_ABONOPS
	 * 	Description: For Amount Accumulated Prestaciones Sociales according to Labor Law.
	 *  Returns the 15 Days each Trimester depending on employee’s startup date 
	 *  or Zero (0) if not
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_ABONOPS (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;	
		new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp EmployeeIncomeDate;
		int InvDateMonth=0;
		int EmpMonth1=0;
		int EmpMonth2=0;
		int EmpMonth3=0;
		int EmpMonth4=0;
		GregorianCalendar refcal = new GregorianCalendar();
		
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Set Period Init to refcal Gregorian Calendar variable 
		refcal.setTime(InvDateIni);
		// Get Invoice Month (0-11 0:Jan 3:APR 6:JUL 9:OCT 11:DEC)
		InvDateMonth = refcal.get(Calendar.MONTH);
		// Employees Before 1997-06-18
		// VERIFY IF PERIOD IS ON MONTHS: JUL-OCT-ENE-APR
		if (EmployeeIncomeDate.before(Timestamp.valueOf("1997-06-18 00:00:00"))) {
			if (InvDateMonth == 0 || InvDateMonth == 3 || InvDateMonth == 6 | InvDateMonth == 9) {
//			if (InvDateMonth == 1 || InvDateMonth == 4 || InvDateMonth == 7 | InvDateMonth == 10) {
				retValue = 15;
			}
		} else {
			// Employees Before 2012-05-04
			// VERIFY IF PERIOD IS ON MONTHS: JUL-OCT-ENE-APR
			if (EmployeeIncomeDate.before(Timestamp.valueOf("2012-05-04 00:00:00"))) {
				if (InvDateMonth == 0 || InvDateMonth == 3 || InvDateMonth == 6 | InvDateMonth == 9) {
	//			if (InvDateMonth == 1 || InvDateMonth == 4 || InvDateMonth == 7 | InvDateMonth == 10) {
					retValue = 15;
				}			
			}
			// Employees After 2012-05-04
			// DEPENDING ON EMPLOYEE INCOME DATE
			if (EmployeeIncomeDate.after(Timestamp.valueOf("2012-05-04 00:00:00"))) {
				// Calculates Employe Years to see if >= 1
				//list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
				// Get Employee Income Month where trimester apply
				refcal.setTime(EmployeeIncomeDate);
				// Next Month
	//				refcal.add(Calendar.MONTH, 1);
				EmpMonth1=refcal.get(Calendar.MONTH);
				refcal.add(Calendar.MONTH, 3);
				EmpMonth2=refcal.get(Calendar.MONTH);
				refcal.add(Calendar.MONTH, 3);
				EmpMonth3=refcal.get(Calendar.MONTH);
				refcal.add(Calendar.MONTH, 3);
				EmpMonth4=refcal.get(Calendar.MONTH);
				if (InvDateMonth == EmpMonth1 || InvDateMonth == EmpMonth2 || InvDateMonth == EmpMonth3 | InvDateMonth == EmpMonth4)  {
					retValue = 15;
				}
			}
		}
		return BigDecimal.valueOf(retValue);
	}

	/**
	 *  processDefaultValue: DV_SALARY12SB
	 * 	Description: Return Salary Amount Base to calc Social Benefits 
	 * 		(Prestaciones Sociales) last 12 Months
	 *  	according to Labor Law 142 Lit B.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_SALARY12SB (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.ZERO;	
		BigDecimal Historic_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 12 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 11 Month Before
		cal.add(Calendar.MONTH, -11);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_socialbenefits' , ? , ? , ? , ? , ? ) / 12 ";
		PreparedStatement pstmt = null;
//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
//log.warning("Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary = BigDecimal.ZERO;
		retValue = Historic_Salary;
//		if (Historic_Salary.compareTo(Employee_Salary) < 0 ) 
//			retValue = Employee_Salary;
//		else
//			retValue = Historic_Salary;
		return retValue;
	}
	
	/**
	 *  processDefaultValue: DV_ABONOPSA
	 * 	Description: For Amount Accumulated Prestaciones Sociales according to Labor Law. 
	 *  Returns the Two Days additional per service years depending on employee’s startup date or Zero if not. 
	 *  30 days Maximum
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_ABONOPSA (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;	
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		int InvDateMonth=0;
		int EmpMonthofYear=0;
		int NYears=0;
		GregorianCalendar refcal = new GregorianCalendar();
		Timestamp DateLOTT1 = Timestamp.valueOf("1997-06-18 00:00:00");
		Timestamp DateLOTT2 = Timestamp.valueOf("2012-05-04 00:00:00");
		String Message="";
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Set Period Init to refcal Gregorian Calendar variable 
		refcal.setTime(InvDateIni);
		// Get Invoice Month (0-11 0:Jan 3:APR 6:JUL 9:OCT 11:DEC)
		InvDateMonth = refcal.get(Calendar.MONTH);
		// Get Employee Income Month where trimester apply
		refcal.setTime(EmployeeIncomeDate);
		// The Month of EmployeeIncomeDate
		// refcal.add(Calendar.MONTH, 1);
		EmpMonthofYear=refcal.get(Calendar.MONTH);	
		// Employees Before 1997-06-18
		// VERIFY IF PERIOD IS ON MONTHS: JUL-OCT-ENE-APR
		if (EmployeeIncomeDate.before(DateLOTT1)) {
			list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);		
				Message="BEFORE 1997-06-18-EmployeeIncomeDate:"+EmployeeIncomeDate+"InvDateEnd:"+InvDateEnd;
				// OLD EMPLOYEES AL ON JUNE
				if (InvDateMonth == 5) {
					retValue=30;
				}
		} else {
			if (EmployeeIncomeDate.before(DateLOTT2)) {
				// Employees Before 2012-05-04
				// Calculates Employe Years BEFORE LOTT2
				list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
				NYears = list.get(0);
				//log.warning("Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+"  InvDateMonth:"+InvDateMonth+"  EmpMonthofYear:"+EmpMonthofYear);
				if (NYears >= 1) {
					// Starts on Second Year
					if (InvDateMonth == EmpMonthofYear)  {
						// Calculates 2 Days per Year
						retValue = NYears*2;
					}
				}
				Message="BEFORE 2012-05-04-EmployeeIncomeDate:"+EmployeeIncomeDate+"InvDateEnd:"+InvDateEnd+"   retValue:"+retValue;	
			} else {
				// Employees After 2012-05-04
				// DEPENDING ON EMPLOYEE INCOME DATE
				// Calculates Employe Years
				list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
				NYears = list.get(0);
				//log.warning("Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+"  InvDateMonth:"+InvDateMonth+"  EmpMonthofYear:"+EmpMonthofYear);
				if (NYears >= 1) {
					// Starts on Second Year
					if (InvDateMonth == EmpMonthofYear)  {
						// Calculates 2 Days per Year
						retValue = NYears*2;
					}
				}
				Message="AFTER 2012-05-04-EmployeeIncomeDate:"+EmployeeIncomeDate+"InvDateEnd:"+InvDateEnd+"   retValue:"+retValue;	
			}
		}
		
		
		
		
		// Calculates Employe Years 
		if (retValue > 30)
			retValue = 30;
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_INCUTILID
	 * 	Description: Amount of days Related to UTILIDADES en Prestaciones Sociales
	 *  	according to Labor Law.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_INCUTILID (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 30;	
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_INCVACAC
	 * 	Description: Amount of days Related to VACATIONS o Social Benefits
	 *  	according to Labor Law.
	 * 		 Additional Days according to Labor Law Art. 190
	 *  	 Additional Days according to Labor Law Art. 192
	 *  	 Additional Days according to Contract 192A
	 *  Maximum 15+15 = 30 days
	 *  One Day paid for each year of Employee's service 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_INCVACAC (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;
		double retValue190 = 0;	
		double retValue192 = 15;	
		double retValue192A = 0;	
		int NYears=0;
		String Message="";
		Timestamp DateLOTT1 = Timestamp.valueOf("1997-06-18 00:00:00");
		Timestamp DateLOTT2 = Timestamp.valueOf("2012-05-04 00:00:00");
		//DV_VACACDIADIC = 3
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Employees Before 1997-06-18
		// VERIFY IF PERIOD IS ON MONTHS: JUL-OCT-ENE-APR
		if (EmployeeIncomeDate.before(DateLOTT1)) {
			list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);		
				retValue = 30;
				Message="BEFORE 1997-06-18-EmployeeIncomeDate:"+EmployeeIncomeDate+"InvDateEnd:"+InvDateEnd;
		} else {
			if (EmployeeIncomeDate.before(DateLOTT2)) {
				// Employees Before 2012-05-04
				// Calculates Employe Years BEFORE LOTT2
				list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, DateLOTT2);
				NYears = list.get(0);
				retValue192A = NYears ;
				if (retValue192A < 15) {
					// Calculates Employe Years FROM LOTT2
					list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(DateLOTT2, InvDateEnd);
// OJO +1
					retValue192A = retValue192A + list.get(0)+1;
					if (retValue192A > 15)
						retValue192A = 15;
				} else {
					retValue192A = 15;
				}
				Message="BEFORE 2012-05-04-EmployeeIncomeDate:"+EmployeeIncomeDate+"InvDateEnd:"+InvDateEnd+"   retValue192A:"+retValue192A;	
			} else {
				// Employees After 2012-05-04
				// DEPENDING ON EMPLOYEE INCOME DATE
				// Calculates Employe Years
				list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateEnd);
				retValue192A = list.get(0);
// OJO +1
				// 
				if (retValue192A > 15)
					retValue192A = 15;
				// // Calculates Years  -  Additional Days according to Labor Law Art. 190
				retValue190 = list.get(0);
				// 
				if (retValue190 > 15)
					retValue190 = 15;
				Message="AFTER 2012-05-04-EmployeeIncomeDate:"+EmployeeIncomeDate+"InvDateEnd:"+InvDateEnd+"   retValue192A:"+retValue192A;	
			}
			// Return Value
			retValue = retValue192+retValue192A;
		}
		Message=Message+"   retValue:"+retValue;
		//log.warning(Message);
		// Calculates Years  -  Additional Days according to Labor Law Art. 192A
		return BigDecimal.valueOf(retValue);
	}
	
	
	// ********************************************************
	// ********************************************************
	// PROCESSES	SOCIAL BENEFITS   PI Interest
	// ********************************************************
	// ********************************************************
	/**
	 *  processDefaultValue: DV_INTERPRESTAC
	 * 	Description: Amount Accumulated Interest Prestaciones Sociales Additional
	 *  	according to Labor Law.
	 *  	When Employee New Year
	 *  Amount Accumulated on AMN_Employee_Salary table
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_INTERPRESTAC (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;	
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp EmployeeIncomeDate;
		int NYears=0;
		GregorianCalendar refcal = new GregorianCalendar();
		
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Set Period Init to refcal Gregorian Calendar variable 
		refcal.setTime(InvDateIni);
		refcal.get(Calendar.MONTH);
		// Get Employee Income Month where trimester apply
		refcal.setTime(EmployeeIncomeDate);
		refcal.get(Calendar.MONTH);	
		// Calculates Employe Years 
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
		NYears = list.get(0);
//OJO
		int AMN_Concept_Types_Proc_ID = MAMN_Concept_Types.sqlGetAMNConceptTypesABONOPS(amnpayroll.getAD_Client_ID());
//OJO
		MAMN_Employee_Salary amnemployeesal = null;
		amnemployeesal = MAMN_Employee_Salary.findAMN_Employee_Salary_byConceptTypeProcID(
				amnemployee.getAD_Client_ID(), amnemployee.getAD_Org_ID(), amnpayroll.getC_Currency_ID(), 
				amnemployee.getAMN_Employee_ID(), AMN_Concept_Types_Proc_ID);
		// TO BE CALCULATED ON FUTURE
//log.warning("Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+"  InvDateMonth:"+InvDateMonth+"  EmpMonthofYear:"+EmpMonthofYear);
		if (NYears >= 1) {
			// Get Interest Accumulated 
				retValue = 0;

		}
		return BigDecimal.valueOf(retValue);
	}

	// ********************************************************
	// ********************************************************
	// PROCESSES SOCIAL BENEFITS   PR Social Benefit Anticipate
	// ********************************************************
	// ********************************************************

	/**
	 *  processDefaultValue: DV_ACUMPRESTAC
	 * 	Description: Amount Accumulated  Prestaciones Sociales 
	 *  	before this period
	 *  Amount Accumulated on AMN_Employee_Salary table
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_ACUMPRESTAC (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);
		BigDecimal AcumPrestac = BigDecimal.valueOf(0);
		Timestamp InvDateIni;
		Timestamp InvDateAcct;
		Timestamp EmployeeIncomeDate;
		GregorianCalendar refcal = new GregorianCalendar();

		GregorianCalendar cal = new GregorianCalendar(2018, 4, 1);
		long millis = cal.getTimeInMillis();
		Timestamp ts = new Timestamp(millis);
		
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateAcct = amnpayroll.getDateAcct();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Set Period Init to refcal Gregorian Calendar variable 
		refcal.setTime(InvDateIni);
		refcal.get(Calendar.MONTH);
		// Get Employee Income Month where trimester apply
		refcal.setTime(EmployeeIncomeDate);
		refcal.get(Calendar.MONTH);	
//log.warning("Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+"  InvDateIni:"+InvDateIni+"  InvDateEnd:"+InvDateEnd);
		// Get 'salary_base' from amp_salary_hist_calc function
		// OLD QUERY
		String sql1 = " SELECT "+
//    		" sum((((salario/30) + incbv + incu) * abono_dias) )  - sum(adelanto) + sum(ajuste) as acumulado "+
//       		" COALESCE(sum((((salario/30) + incbv + incu) * abono_dias) ) - sum(adelanto) + sum(ajuste),0) as acumulado "+
       		" sum((((salario/30) + incbv + incu) * abono_dias) ) - sum(adelanto) + sum(ajuste) as acumulado "+
       		" FROM "+
    		" ( SELECT DISTINCT "+
    		"emp_sal.validto as fecha,  "+
    		"emp_sal.salary as salario,  "+
    		"emp_sal.salary_day as salario_diario,  "+
    		"emp_sal.vacationdays as dias_incbv,  "+
    		"CASE WHEN amc.payrolldays=7  THEN (((emp_sal.salary/30)*emp_sal.vacationdays)/365) "+
    		" ELSE (((emp_sal.salary/30)*emp_sal.vacationdays)/360) END as incbv,	    "+
    		" emp_sal.utilitydays as dias_incu,  "+
    		"CASE WHEN amc.payrolldays=7  THEN (((emp_sal.salary/30)*emp_sal.utilitydays)/365)  "+
    		" ELSE (((emp_sal.salary/30)*emp_sal.utilitydays)/360) END as incu, "+
    		" emp_sal.prestacionadvance as adelanto, emp_sal.prestacioninterestadvance as anticipo,  "+
    		"emp_sal.prestaciondays as abono_dias, emp_sal.prestacionamount as abono_monto, "+
    		"emp_sal.prestacioninterestadjustment as ajuste_int, emp_sal.prestacionadjustment as ajuste "+
    		"FROM adempiere.amn_employee  		 as emp "+
    		"LEFT JOIN adempiere.amn_contract   	 as amc     ON (emp.amn_contract_id= amc.amn_contract_id)	  "+
    		"LEFT JOIN adempiere.amn_employee_salary as emp_sal ON (emp.amn_employee_id = emp_sal.amn_employee_id)	 "+
    		"WHERE emp.amn_employee_id = ? and emp_sal.validto <= ? "+
    		" )  as salary "
     		;
//log.warning("....SQL:"+sql1+"  AMN_Employee_ID():"+amnemployee.getAMN_Employee_ID()+"  InvDateIni:"+InvDateIni);
       	// Currency Conversion SQL Query
		String sql2 = " SELECT "+
           	" COALESCE(sum((((salario/30) + incbv + incu) * abono_dias) ) - sum(adelanto) + sum(ajuste),0) as acumulado "+
       		" FROM "+
    		" ( SELECT DISTINCT "+
    		"emp_sal.validto as fecha,  "+
    		"currencyConvert(emp_sal.salary, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as salario, "+
    		"currencyConvert(emp_sal.salary_day, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as salario_diario, " +
        	"emp_sal.vacationdays as dias_incbv,  "+
        	"CASE WHEN amc.payrolldays=7  "+
    			" THEN (((currencyConvert(emp_sal.salary, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id)/30)*emp_sal.vacationdays)/365) "+
        		" ELSE (((currencyConvert(emp_sal.salary, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id)/30)*emp_sal.vacationdays)/360) "+ 
    			" END as incbv,	"+
        	" emp_sal.utilitydays as dias_incu,  "+
        	"CASE WHEN amc.payrolldays=7  "+
    			" THEN (((currencyConvert(emp_sal.salary, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id)/30)*emp_sal.utilitydays)/365)  "+
        		" ELSE (((currencyConvert(emp_sal.salary, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id)/30)*emp_sal.utilitydays)/360)  "+
    			" END as incu, "+
        	"currencyConvert(emp_sal.prestacionadvance, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as adelanto, " +
    		"currencyConvert(emp_sal.prestacioninterestadvance, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as anticipo, " +
    		"emp_sal.prestaciondays as abono_dias, " +
    		"currencyConvert(emp_sal.prestacionamount, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as abono_monto, " +
    		"currencyConvert(emp_sal.prestacioninterestadjustment, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as ajuste_int, " +
    		"currencyConvert(emp_sal.prestacionadjustment, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as ajuste " +
    		"FROM adempiere.amn_employee  as emp "+
    		"LEFT JOIN adempiere.amn_contract   as amc     ON (emp.amn_contract_id= amc.amn_contract_id)	  "+
    		"LEFT JOIN adempiere.amn_employee_salary as emp_sal ON (emp.amn_employee_id = emp_sal.amn_employee_id)	 "+
    		"WHERE emp.amn_employee_id = ? and emp_sal.validto <= ? "+
    		" )  as salary "
    		;
		//	Currency Conversion date	
		if (InvDateAcct.before(ts) )  {
//log.warning("..DV_ACUMPRESTAC..SQL sql1:"+sql1);
//log.warning("AMN_Employee_ID():"+amnemployee.getAMN_Employee_ID()+"  InvDateAcct:"+InvDateAcct);
	    	PreparedStatement pstmt = null;
			// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
			ResultSet rspc = null;		
			try
			{
				pstmt = DB.prepareStatement(sql1, null);
	            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
	            pstmt.setTimestamp(2, InvDateIni);
	            rspc = pstmt.executeQuery();
				if (rspc.next())
				{
					AcumPrestac = rspc.getBigDecimal(1);
//log.warning("Historic_Salary Query Result sql1:"+AcumPrestac);
				}				
			}
		    catch (SQLException e)
		    {
		    	AcumPrestac = BigDecimal.valueOf(0);
		    }
			finally
			{
				DB.close(rspc, pstmt);
				rspc = null; pstmt = null;
			}
		} else {
//log.warning("..DV_ACUMPRESTAC..SQL sql2:"+sql2);
//log.warning("AMN_Employee_ID():"+amnemployee.getAMN_Employee_ID()+"  InvDateAcct:"+InvDateAcct+" Currency="+amnpayroll.getC_Currency_ID());
			PreparedStatement pstmt = null;
			// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
			ResultSet rspc = null;		
			try
			{
				pstmt = DB.prepareStatement(sql2, null);
	            pstmt.setInt (1, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (2, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (3, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (5, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (6, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (7, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (8, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (9, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (10, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (11, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (12, amnemployee.getAMN_Employee_ID());
	            pstmt.setTimestamp(13, InvDateAcct);
	            rspc = pstmt.executeQuery();
				if (rspc.next())
				{
					AcumPrestac = rspc.getBigDecimal(1);
//log.warning("Historic_Salary Query Result sql2:"+AcumPrestac);
				}				
			}
		    catch (SQLException e)
		    {
		    	AcumPrestac = BigDecimal.valueOf(0);
		    }
			finally
			{
				DB.close(rspc, pstmt);
				rspc = null; pstmt = null;
			}
		}
		// Compare to Salary on Employee Table
//log.warning("Historic_Salary function result:"+AcumPrestac);
		retValue = AcumPrestac;
		if (retValue==null)
			retValue=BigDecimal.ZERO;
		//
		return retValue;
	}
	
	
	/**
	 *  processDefaultValue: DV_ACUANTPRESTAC
	 * 	Description: Amount Accumulated  Anticipated Payments Prestaciones Sociales 
	 *  	before this period
	 *  Amount Accumulated Anticipated Payments on AMN_Employee_Salary table
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_ACUANTPRESTAC (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);
		BigDecimal AcumAnticPrestac = BigDecimal.valueOf(0);
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		Timestamp InvDateAcct;
		Timestamp EmployeeIncomeDate;
		GregorianCalendar refcal = new GregorianCalendar();
		
		GregorianCalendar cal = new GregorianCalendar(2018, 4, 1);
		long millis = cal.getTimeInMillis();
		Timestamp ts = new Timestamp(millis);

		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		InvDateEnd=amnpayroll.getInvDateEnd();
		InvDateAcct = amnpayroll.getDateAcct();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Set Period Init to refcal Gregorian Calendar variable 
		refcal.setTime(InvDateIni);
		refcal.get(Calendar.MONTH);
		// Get Employee Income Month where trimester apply
		refcal.setTime(EmployeeIncomeDate);
		refcal.get(Calendar.MONTH);	
		// Calculates Employe Years 
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
//log.warning("Employee:"+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+
//		" Ref:"+amnpayroll.getValue()+"  InvDateIni:"+InvDateIni+"  InvDateEnd:"+InvDateEnd);
		// Get 'salary_base' from amp_salary_hist_calc function
    	// OLD QUERY
		String sql1 = " SELECT "+
    		" sum(adelanto)  as acumulado "+
    		" FROM "+
    		" ( SELECT DISTINCT "+
    		"emp_sal.validto as fecha,  "+
    		"emp_sal.salary as salario,  "+
    		"emp_sal.salary_day as salario_diario,  "+
    		"emp_sal.vacationdays as dias_incbv,  "+
    		"CASE WHEN amc.payrolldays=7  THEN (((emp_sal.salary/30)*emp_sal.vacationdays)/365) "+
    		" ELSE (((emp_sal.salary/30)*emp_sal.vacationdays)/360) END as incbv,	    "+
    		" emp_sal.utilitydays as dias_incu,  "+
    		"CASE WHEN amc.payrolldays=7  THEN (((emp_sal.salary/30)*emp_sal.utilitydays)/365)  "+
    		" ELSE (((emp_sal.salary/30)*emp_sal.utilitydays)/360) END as incu, "+
    		" emp_sal.prestacionadvance as adelanto, emp_sal.prestacioninterestadvance as anticipo,  "+
    		"emp_sal.prestaciondays as abono_dias, emp_sal.prestacionamount as abono_monto, "+
    		"emp_sal.prestacioninterestadjustment as ajuste_int, emp_sal.prestacionadjustment as ajuste "+
    		"FROM adempiere.amn_employee  		 as emp "+
    		"LEFT JOIN adempiere.amn_contract   	 as amc     ON (emp.amn_contract_id= amc.amn_contract_id)	  "+
    		"LEFT JOIN adempiere.amn_employee_salary as emp_sal ON (emp.amn_employee_id = emp_sal.amn_employee_id)	 "+
    		"WHERE emp.amn_employee_id = ? and emp_sal.validto <= ? "+
    		" )  as salary "
     		;
//log.warning("....SQL:"+sql+"  AMN_Employee_ID():"+amnemployee.getAMN_Employee_ID()+"  InvDateIni:"+InvDateIni);
       	// Currency Conversion SQL Query
		String sql2 = " SELECT "+
    		" sum(adelanto)  as acumulado "+
       		" FROM "+
    		" ( SELECT DISTINCT "+
    		"emp_sal.validto as fecha,  "+
        	"currencyConvert(emp_sal.prestacionadvance, emp_sal.c_currency_id, ? , emp_sal.validto, emp_sal.C_ConversionType_ID,emp_sal.ad_client_id, emp_sal.ad_org_id) as adelanto " +
    		"FROM adempiere.amn_employee  		 as emp "+
    		"LEFT JOIN adempiere.amn_contract   	 as amc     ON (emp.amn_contract_id= amc.amn_contract_id)	  "+
    		"LEFT JOIN adempiere.amn_employee_salary as emp_sal ON (emp.amn_employee_id = emp_sal.amn_employee_id)	 "+
    		"WHERE emp.amn_employee_id = ? and emp_sal.validto <= ? "+
    		" )  as salary "
    		;
		//	Currency Conversion date	
		if (InvDateAcct.before(ts) )  {
//log.warning("..DV_ACUANTPRESTAC..SQL sql1:"+sql1);
//log.warning("AMN_Employee_ID():"+amnemployee.getAMN_Employee_ID()+"  InvDateAcct:"+InvDateAcct);
		   	PreparedStatement pstmt = null;
			// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
			ResultSet rspc = null;		
			try
			{
				pstmt = DB.prepareStatement(sql1, null);
	            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
	            pstmt.setTimestamp(2, InvDateIni);
	            rspc = pstmt.executeQuery();
				if (rspc.next())
				{
					AcumAnticPrestac = rspc.getBigDecimal(1);
//log.warning("Historic_Salary Query Result sql1:"+AcumAnticPrestac);	
				}				
			}
		    catch (SQLException e)
		    {
		    	AcumAnticPrestac = BigDecimal.valueOf(0);
		    }
			finally
			{
				DB.close(rspc, pstmt);
				rspc = null; pstmt = null;
			}
		
		} else {
//log.warning("..DV_ACUANTPRESTAC..SQL sql2:"+sql2);
//log.warning("AMN_Employee_ID():"+amnemployee.getAMN_Employee_ID()+"  InvDateAcct:"+InvDateAcct+" Currency="+amnpayroll.getC_Currency_ID());
	    	PreparedStatement pstmt = null;
			// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
			ResultSet rspc = null;		
			try
			{
				pstmt = DB.prepareStatement(sql2, null);
	            pstmt.setInt (1, amnpayroll.getC_Currency_ID());
	            pstmt.setInt (2, amnemployee.getAMN_Employee_ID());
	            pstmt.setTimestamp(3, InvDateIni);
	            rspc = pstmt.executeQuery();
				if (rspc.next())
				{
					AcumAnticPrestac = rspc.getBigDecimal(1);
//log.warning("Historic_Salary Query Result sql2:"+AcumAnticPrestac);				
				}				
			}
		    catch (SQLException e)
		    {
		    	AcumAnticPrestac = BigDecimal.valueOf(0);
		    }
			finally
			{
				DB.close(rspc, pstmt);
				rspc = null; pstmt = null;
			}
		}
		// Compare to Salary on Employee Table
//log.warning("Historic_Salary  function result:"+AcumAnticPrestac);
		retValue = AcumAnticPrestac;
		if (retValue==null)
			retValue=BigDecimal.ZERO;
		//
		return retValue;
	}
	
	// ********************************************************
	// ********************************************************
	// PROCESSES	UTILIDAD - UTILITIES   NU
	// ********************************************************
	// ********************************************************
	/**
	 *  processDefaultValue: DV_SALUTIL
	 * 	Description: Returns Average Salary during last period in therm of Monthly Salary
	 *  Summarizing all Concepts Types where IS_UTILIDAD ='Y'
	 *  Returns Salary on Employee on AMN_Employee table if not exists historic payments.
	 *  A Standard Plublic Var similar is R_SALARIO 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_SALUTIL (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		MAMN_Contract amncontract = new MAMN_Contract(Ctx, amnpayroll.getAMN_Contract_ID(), trxName);
		BigDecimal PayrollDays =  BigDecimal.ZERO;
		String sql = "";
		// Calculates Average last 12 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 11 Month Before
		//cal.add(Calendar.MONTH, -11);
		//StartDate	= First Day next 12 Month Before (CHANGED)
		// 12 Moth Before (old criteria)
		//cal.add(Calendar.MONTH, -12);
		//cal.set(Calendar.DAY_OF_MONTH, 1);
		// Changed to Payroll Receipt Init Date
		cal.setTime(amnpayroll.getInvDateIni());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
		// PayrollDays
		PayrollDays= amncontract.getPayRollDays();
		if ( PayrollDays == BigDecimal.valueOf(7) )
			sql = "select amp_salary_hist_calc('salary_utilities' , ? , ? , ? , ? , ?) * 30 / 365 as salaryUT ";
		else
			sql = "select amp_salary_hist_calc('salary_utilities' , ? , ? , ? , ? , ?) / 12 as salaryUT ";
		//
		PreparedStatement pstmt = null;
		// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("DV_SALUTIL Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary=BigDecimal.ZERO;
		retValue = Historic_Salary;
		return retValue;
	}

	/**
	 *  processDefaultValue: DV_UTILIDDEV
	 * 	Description: Return Accrued Payments during last 12 period in term of Monthly Salary
	 *  All Paymentens during the period
	 *  Summarizing all Concepts Types where IS_UTILIDAD ='Y'
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILIDDEV (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 12 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 11 Month Before
		//cal.add(Calendar.MONTH, -11);
		//StartDate	= First Day next 12 Month Before (CHANGED)
		// 12 Moth Before (old criteria)
		//cal.add(Calendar.MONTH, -12);
		//cal.set(Calendar.DAY_OF_MONTH, 1);
		// Changed to Payroll Receipt Init Date
		cal.setTime(amnpayroll.getInvDateIni());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get All Payments from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_utilities' , ? , ? , ? , ? , ? )  ";
		PreparedStatement pstmt = null;
		// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary=BigDecimal.ZERO;
		retValue = Historic_Salary;
		return retValue;
	}

	/**
	 *  processDefaultValue: DV_UTILIDDEV6M
	 * 	Description: Return Accrued Payments during last 6 period in term of Monthly Salary
	 *  All Paymentens during the period
	 *  Summarizing all Concepts Types where IS_UTILIDAD ='Y'
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILIDDEV6M(Properties Ctx, int AMN_Payroll_ID, String trxName) {
	    BigDecimal retValue = BigDecimal.ZERO;
	    BigDecimal Historic_Salary = BigDecimal.ZERO;
	    
	    MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
	    MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);

	    // Configuración de fechas: Final = getRefDateEnd(), Inicial = 6 meses antes
	    GregorianCalendar cal = new GregorianCalendar();
	    cal.setTime(amnpayroll.getRefDateEnd());  // Usamos getRefDateEnd() en lugar de getInvDateEnd()
	    
	    // Normalizar hora a 00:00:00
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    
	    Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
	    
	    // Calcular fecha inicial (6 meses antes)
	    cal.add(Calendar.MONTH, -6);  // Restamos 6 meses exactos
	    cal.set(Calendar.DAY_OF_MONTH, 1);    // Forzar primer día del mes
	    Timestamp StartDate = new Timestamp(cal.getTimeInMillis());

	    // Consulta SQL (igual que el original)
	    String sql = "select amp_salary_hist_calc('salary_utilities', ?, ?, ?, ?, ?)";
	    PreparedStatement pstmt = null;
	    ResultSet rspc = null;
	    
	    try {
	        pstmt = DB.prepareStatement(sql, null);
	        pstmt.setInt(1, amnemployee.getAMN_Employee_ID());
	        pstmt.setTimestamp(2, StartDate);
	        pstmt.setTimestamp(3, EndDate);
	        pstmt.setInt(4, amnpayroll.getC_Currency_ID());
	        pstmt.setInt(5, amnpayroll.getC_ConversionType_ID());
	        
	        rspc = pstmt.executeQuery();
	        if (rspc.next()) {
	            Historic_Salary = rspc.getBigDecimal(1);
	        }
	    } catch (SQLException e) {
	    	log.warning("Error en DV_UTILIDDEV_6Months_FirstDay "+e.getMessage());
	        Historic_Salary = BigDecimal.ZERO;
	    } finally {
	        DB.close(rspc, pstmt);
	    }

	    // Validar resultado
	    retValue = Historic_Salary != null ? Historic_Salary : BigDecimal.ZERO;
	    return retValue;
	}

	/**
	 *  processDefaultValue: DV_UTILIDDEV12M
	 * 	Description: Return Accrued Payments during last 12 period in term of Monthly Salary
	 *  All Paymentens during the period
	 *  Summarizing all Concepts Types where IS_UTILIDAD ='Y'
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILIDDEV12M(Properties Ctx, int AMN_Payroll_ID, String trxName) {
	    BigDecimal retValue = BigDecimal.ZERO;
	    BigDecimal Historic_Salary = BigDecimal.ZERO;
	    
	    MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
	    MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);

	    // Configuración de fechas: Final = getRefDateEnd(), Inicial = 6 meses antes
	    GregorianCalendar cal = new GregorianCalendar();
	    cal.setTime(amnpayroll.getRefDateEnd());  // Usamos getRefDateEnd() en lugar de getInvDateEnd()
	    
	    // Normalizar hora a 00:00:00
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    
	    Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
	    
	    // Calcular fecha inicial (12 meses antes)
	    cal.setTime(amnpayroll.getRefDateIni());  // Usamos getRefDateIni() en lugar de getInvDateIni()

	    // Normalizar hora a 00:00:00
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);

	    Timestamp StartDate = new Timestamp(cal.getTimeInMillis());

	    // Consulta SQL (igual que el original)
	    String sql = "select amp_salary_hist_calc('salary_utilities', ?, ?, ?, ?, ?)";
	    PreparedStatement pstmt = null;
	    ResultSet rspc = null;
	    
	    try {
	        pstmt = DB.prepareStatement(sql, null);
	        pstmt.setInt(1, amnemployee.getAMN_Employee_ID());
	        pstmt.setTimestamp(2, StartDate);
	        pstmt.setTimestamp(3, EndDate);
	        pstmt.setInt(4, amnpayroll.getC_Currency_ID());
	        pstmt.setInt(5, amnpayroll.getC_ConversionType_ID());
	        
	        rspc = pstmt.executeQuery();
	        if (rspc.next()) {
	            Historic_Salary = rspc.getBigDecimal(1);
	        }
	    } catch (SQLException e) {
	    	log.warning("Error en DV_UTILIDDEV_6Months_FirstDay "+e.getMessage());
	        Historic_Salary = BigDecimal.ZERO;
	    } finally {
	        DB.close(rspc, pstmt);
	    }

	    // Validar resultado
	    retValue = Historic_Salary != null ? Historic_Salary : BigDecimal.ZERO;
	    return retValue;
	}
	/**
	 *  processDefaultValue: DV_UTILIDADES
	 * 	Description: Return 120 days according to Labor Law Art. 131 y 132 LOTT
	 *					and Labor Contract
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILIDADES (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		double retValue = 120;	
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_UTILANT
	 * 	Description: Return 30 days according to Labor Law Art. 131 y 132 LOTT
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILANT (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		double retValue = 30;	
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_UTILDEF
	 * 	Description: Return 0 days additional according to Financial Results
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILDEF (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;	
		return retValue;
	}
	
	/**
	 *  processDefaultValue: DV_UTILADIC
	 * 	Description: Return 0 days additional according to Financial Results
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_UTILADIC (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;	
		return retValue;
	}
	
	// ********************************************************
	// ********************************************************
	// PROCESSES	YEAR TO DATE CUMULATED (TAX AND SSO)
	// ********************************************************
	// ********************************************************

	
	/**
	 * processDefaultValue: DV_SALARYYTD
	 * Returns Salary Cumulated Year to Date prior to this receipt
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_SALARYYTD (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;	
		BigDecimal YTD_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		
		// Calculates Year to Date Salary
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateIni());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// get previous InvDateIni date
		cal.add(Calendar.DATE, -1);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day of the year
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as salario,
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', ?, ?, ?, ?, ? ) as salario,
    	String sql = "select amp_special_wh_hist_calc( 'salario', 'allocated','NN', ?, ?, ?, ?, ? ) ";
		PreparedStatement pstmt = null;
//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				YTD_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	YTD_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (YTD_Salary==null)
			YTD_Salary = BigDecimal.ZERO;
		retValue = YTD_Salary;
		
		return retValue;
	}
	
	/**
	 * processDefaultValue: DV_SALARYTAXYTD
	 * Returns Salary Gravable (tax affected) Cumulated Year to Date prior to this receipt
	 * Note: arc parameter means salary tax affected
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_SALARYTAXYTD (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;	
		BigDecimal YTD_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		
		// Calculates Year to Date Salary
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateIni());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// get previous InvDateIni date
		cal.add(Calendar.DATE, -1);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day of the year
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as salario,
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', ?, ?, ?, ?, ? ) as salario,
    	String sql = "select amp_special_wh_hist_calc( 'arc', 'allocated','NN', ?, ?, ?, ?, ? ) ";
		PreparedStatement pstmt = null;
//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				YTD_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	YTD_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (YTD_Salary==null)
			YTD_Salary = BigDecimal.ZERO;
		retValue = YTD_Salary;
		
		return retValue;
	}
	
	/**
	 * processDefaultValue: DV_TAXYTD
	 * Returns Tax retained Cumulated Year to Date prior to this receipt
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_TAXYTD (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;	
		BigDecimal YTD_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		
		// Calculates Year to Date Salary
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateIni());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// get previous InvDateIni date
		cal.add(Calendar.DATE, -1);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day of the year
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as salario,
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', ?, ?, ?, ?, ? ) as salario,
    	String sql = "select amp_special_wh_hist_calc( 'arc', 'deducted','NN', ?, ?, ?, ?, ? ) ";
		PreparedStatement pstmt = null;
//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				YTD_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	YTD_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (YTD_Salary==null)
			YTD_Salary = BigDecimal.ZERO;
		retValue = YTD_Salary;
				
		return retValue;
	}
	
	/**
	 * processDefaultValue: DV_SSOYTD
	 * Returns Social Security retained Cumulated Year to Date prior to this receipt
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_SSOYTD (Properties Ctx, int AMN_Payroll_ID, String trxName) {
		BigDecimal retValue = BigDecimal.ZERO;	
		BigDecimal YTD_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		
		// Calculates Year to Date Salary
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateIni());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// get previous InvDateIni date
		cal.add(Calendar.DATE, -1);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day of the year
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as salario,
//		amp_special_wh_hist_calc( 'salario', 'allocated','NN', ?, ?, ?, ?, ? ) as salario,
    	String sql = "select amp_special_wh_hist_calc( 'sso', 'deducted','NN', ?, ?, ?, ?, ? ) ";
		PreparedStatement pstmt = null;
//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				YTD_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	YTD_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (YTD_Salary==null)
			YTD_Salary = BigDecimal.ZERO;
		retValue = YTD_Salary;
		return retValue;
	}
	
	/**
	 * 
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_SAMPLE (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue;	
		Timestamp InvDateIni;
		Timestamp InvDateEnd;
		int AD_Client_ID;
		String sql="";
		// InvDateIni
		sql = "SELECT InvDateIni FROM amn_payroll WHERE amn_payroll_id=?" ;
		InvDateIni=DB.getSQLValueTS(trxName, sql, AMN_Payroll_ID);
		// InvDateEnd
		sql = "SELECT InvDateEnd FROM amn_payroll WHERE amn_payroll_id=?" ;
		InvDateEnd=DB.getSQLValueTS(trxName, sql, AMN_Payroll_ID);
		// AD_Client_ID
		sql = "SELECT AD_Client_ID FROM amn_payroll WHERE amn_payroll_id=?" ;
		AD_Client_ID=DB.getSQLValue(trxName, sql, AMN_Payroll_ID);
		// Verify isSaturdayBusinessDay
		MAMN_Shift shift = new MAMN_Shift();
		isSaturdayBusinessDay = shift.isSaturdayBusinessDayfromPayroll(Ctx, AMN_Payroll_ID);
		// Calculates LABORDAY from MAMN_Payroll Class
		retValue =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, InvDateIni, InvDateEnd, AD_Client_ID, null).doubleValue();
		// Return BigDecimal Value
		return BigDecimal.valueOf(retValue);
	}
	
	// ********************************************************
	// ********************************************************
	// PROCESSES	PL Social Benefits Final Payment
	// RULES VALUES REPLACED RV_ with DV_
	// ********************************************************
	// ********************************************************

	/**
	 *  processDefaultValue: DV_FSALARY6M
	 * 	Description: Return Salary Amount Base to calc Social Benefits 
	 * 		(Prestaciones Sociales) last 6 Months
	 *  	according to Labor Law 142 Lit B.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_FSALARY6M (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.ZERO;	
		BigDecimal Historic_Salary = BigDecimal.ZERO;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Total Remunerations last 6 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		// LAST DAY FOR MONTH BEFORE
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 5 Month Before
		cal.add(Calendar.MONTH, -5);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_socialbenefits' , ? , ? , ? , ? , ? ) ";
		PreparedStatement pstmt = null;
		//log.warning("Employee_ID="+amnemployee.getAMN_Employee_ID()+" Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		//log.warning("Currency=" +amnpayroll.getC_Currency_ID()+"   ConversionType_ID="+amnpayroll.getC_ConversionType_ID());
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.ZERO;
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary = BigDecimal.ZERO;
		retValue = Historic_Salary;
		return retValue;
	}

	/**
	 *  DV_FVACAC190
	 * 	REPLACE ORIGINAL RV_FVACAC190
	 *  RV_FVACAC190 Vacaciones fraccionadas para liquidacion de prestaciones
	 *  Fraccion de Vacciones desde la ultima fecha de cumplimiento de años de servicios del trabajador
	 *  No Días = 15 ( Dias desde el cumpleaños de servicio / 360 )
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	*/
	public static BigDecimal DV_FVACAC190 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		//double retValue = 0;
		
		// BEGIN
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		int roundingMode = curr.getStdPrecision();
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		
		// CalIni Employee Income date
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(EmployeeIncomeDate);
		int firstDayValue = calIni.get(Calendar.DAY_OF_YEAR);
		// CalEnd A_InvDateEnd
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		int secondDayValue = calEnd.get(Calendar.DAY_OF_YEAR);
		// Calc Diff in days elapsed
		int i360 = 360;
		int i15 = 15;
		int iZero = 0;
		int diffdayslast = 0;
		if (secondDayValue >= firstDayValue )
			diffdayslast = secondDayValue - firstDayValue;
		if (secondDayValue < firstDayValue )
			diffdayslast = secondDayValue + (i360 - firstDayValue);
		// Calc Fraction in Big Decimal
		BigDecimal BDVacacDays = new BigDecimal(iZero);
		// Fraccion de mes completo
		diffdayslast= (diffdayslast / 30 ) * 30 ;
		// 
		BigDecimal DBdiffDays= new BigDecimal(diffdayslast);
		BigDecimal BD360 = new BigDecimal(i360);
		BigDecimal BD15 = new BigDecimal(i15);
		BDVacacDays = DBdiffDays.divide(BD360,6, RoundingMode.HALF_UP);
		BDVacacDays = BDVacacDays.multiply(BD15);
		BDVacacDays.setScale(roundingMode, RoundingMode.HALF_UP);
		// 
		return BDVacacDays;

	}
	

	/**
	  * DV_FVACAC190ADI
	  * REPLACE ORIGINAL RV_FVACAC190ADI
	  * RV_FDAYSFRAC Dás transcurridos desde el ultimo aniversario del trabajador
	  *  Días transcurridos desde el ultmo aniversario del trabajador
	  *  Util para calcular valores fraccionados como VACACIONES y otros beneficios
	  * @param Ctx
	  * @param AMN_Payroll_ID
	  * @param trxName
	  * @return
	*/
	public static BigDecimal DV_FVACAC190ADI (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		// BEGIN
		//Timestamp start =A_EmployeeIncomeDate;
		//Timestamp end =A_InvDateEnd;
		// CalIni Employee Income date
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		int roundingMode = curr.getStdPrecision();
		// calIni EmployeeIncomeDate
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(EmployeeIncomeDate);
		int firstYearValue = calIni.get(Calendar.YEAR);
		int firstDayValue = calIni.get(Calendar.DAY_OF_YEAR);
		// CalEnd InvDateEnd
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		int secondYearValue = calEnd.get(Calendar.YEAR);
		int secondDayValue = calEnd.get(Calendar.DAY_OF_YEAR);
		// Calc Diff in days elapsed
		int i360 = 360;
		int diffdayslast = 0;
		if (secondDayValue >= firstDayValue )
			diffdayslast = secondDayValue - firstDayValue;
		if (secondDayValue < firstDayValue )
			diffdayslast = secondDayValue + (i360 - firstDayValue);
		// Fraccion de mes completo
		diffdayslast= (diffdayslast / 30 ) * 30 ;
		// 
		// Calculates No of years
		int diffyears = secondYearValue - firstYearValue;
		// Year Validation > 180  means one year
		// Calculates No of years (Max to 15)
		if (diffyears >= 1 )
		   diffyears = diffyears - 1;
		if (diffyears > 15 )
		    diffyears=15;
		// Final Big Decimal Calc
		BigDecimal BDdiffyears= new BigDecimal(diffyears);
		BigDecimal BDdiffdayslast= new BigDecimal(diffdayslast);

		BigDecimal BD360 = new BigDecimal(i360);
		BigDecimal DBBonVacacFrac = BDdiffdayslast.divide(BD360,6, RoundingMode.HALF_UP);
		DBBonVacacFrac = DBBonVacacFrac.multiply(BDdiffyears);
		DBBonVacacFrac.setScale(roundingMode, RoundingMode.HALF_UP);
		return DBBonVacacFrac;
		
	}
	 
	 
 	/**
 	 * DV_FVACAC192
 	 * RV_FVACAC192
 	 * RV_FVACAC192 Vacaciones fraccionadas para liquidacion de prestaciones
 	 * Fraccion de Vacciones desde la ultima fecha de cumplimiento de años de servicios del trabajador
 	 * No Días = 15 ( Dias desde el cumpleaños de servicio / 365 )
 	 * @param Ctx
 	 * @param AMN_Payroll_ID
 	 * @param trxName
 	 * @return
 	 */
	public static BigDecimal DV_FVACAC192 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		// BEGIN
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		int roundingMode = curr.getStdPrecision();	
		//Timestamp start =A_EmployeeIncomeDate;
		//Timestamp end =A_InvDateEnd;
		// CalIni Employee Income date
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(EmployeeIncomeDate);
		int firstYearValue = calIni.get(Calendar.YEAR);
		int firstDayValue = calIni.get(Calendar.DAY_OF_YEAR);
		// CalEnd A_InvDateEnd
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		int secondYearValue = calEnd.get(Calendar.YEAR);
		int secondDayValue = calEnd.get(Calendar.DAY_OF_YEAR);
		// Calc Diff in days elapsed
		int i360 = 360;
		int i15 = 15;
		int iZero = 0;
		int diffdayslast = 0;
		if (secondDayValue >= firstDayValue )
			diffdayslast = secondDayValue - firstDayValue;
		if (secondDayValue < firstDayValue )
			diffdayslast = secondDayValue + (i360 - firstDayValue);
		// Fraccion de mes completo
		diffdayslast= (diffdayslast / 30 ) * 30 ;
		// 
		// Calc Fraction in Big Decimal
		BigDecimal BDVacacDays = new BigDecimal(iZero);
		BigDecimal DBdiffDays= new BigDecimal(diffdayslast);
		BigDecimal BD360 = new BigDecimal(i360);
		BigDecimal BD15 = new BigDecimal(i15);
		BDVacacDays = DBdiffDays.divide(BD360,6, RoundingMode.HALF_UP);
		// Calculates No of years
		int diffyears = secondYearValue - firstYearValue;
		//
		if (diffyears > 15 )
		    diffyears=15;
		// Final Big Decimal Calc
		BigDecimal BDdiffyears= new BigDecimal(diffyears);
		// RESULTADO
		BD15 = BD15.add(BDdiffyears);
		BDVacacDays = BDVacacDays.multiply(BD15);
		BDVacacDays.setScale(roundingMode, RoundingMode.HALF_UP);
		// TRAZA
		return BDVacacDays;
	}	

	/**
	 *  processDefaultValue: DV_FVACAC192A
	 * 	Description: Returns Additional Days according to Labor Law Art. 192
	 *  Maximum 15 days
	 *  One Day paid for each year of Employee's service 
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_FVACAC192A (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		double retValue = 0;	
		ArrayList<Integer> list = new ArrayList<Integer>(3);
		Timestamp InvDateIni;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateIni=amnpayroll.getInvDateIni();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Calculates Years
		list =  AmerpDateUtils.getYearsMonthDaysElapsedBetween(EmployeeIncomeDate, InvDateIni);
		retValue = list.get(0);
		// Corrected on getYearsMonthDaysElapsedBetween
		if (retValue > 0)
			retValue = retValue;
		if (retValue > 15)
			retValue = 15;
		return BigDecimal.valueOf(retValue);
	}
	
	/**
	 *  processDefaultValue: DV_FUTILIDDEV11
	 * 	Description: Return Accrued Payments during last N MONTH in term of Monthly Salary
	 *  All Paymentens during the period
	 *  Summarizing all Concepts Types where IS_UTILIDAD ='Y'
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_FUTILIDDEV11 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Since November Year before
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		cal.setTime(amnpayroll.getInvDateIni());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.YEAR,-1);
		cal.set(Calendar.MONTH, 10);  // NOVEMBER
		cal.set(Calendar.DAY_OF_MONTH,1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get All Payments from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_utilities' , ? , ? , ? , ? , ? )  ";
		PreparedStatement pstmt = null;
		// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary=BigDecimal.ZERO;
		retValue = Historic_Salary;
		return retValue;
	}
	
	/**
	 *  processDefaultValue: DV_FUTILIDDEV11
	 * 	Description: Return Accrued Payments during last N MONTH in term of Monthly Salary
	 *  All Paymentens during the period
	 *  Summarizing all Concepts Types where IS_UTILIDAD ='Y'
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal DV_FUTILIDDEV12 (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Since December Year before
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		cal.setTime(amnpayroll.getInvDateIni());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.YEAR,-1);
		cal.set(Calendar.MONTH, 11);  // DECEMBER
		cal.set(Calendar.DAY_OF_MONTH,1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get All Payments from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_calc('salary_utilities' , ? , ? , ? , ? , ? )  ";
		PreparedStatement pstmt = null;
		// log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("Historic_Salary:"+Historic_Salary);
		if (Historic_Salary==null)
			Historic_Salary=BigDecimal.ZERO;
		retValue = Historic_Salary;
		return retValue;
	}
	
	/**
	 * DV_FDAYSFRAC
	 * REPLACE ORIGINAL RV_FDAYSFRAC
	 * RV_FDAYSFRAC Días transcurridos desde el ultmo aniversario del trabajador
	 * Dás transcurridos desde el ultmo aniversario del trabajador
	 * Util para calcular valores fraccionados como VACACIONES y otros beneficios
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	*/
	public static BigDecimal DV_FDAYSFRAC (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		// BEGIN
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		int roundingMode = curr.getStdPrecision();	
		// CalIni Employee Income date
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(EmployeeIncomeDate);
		int firstDayValue = calIni.get(Calendar.DAY_OF_YEAR);
		// CalEnd A_InvDateEnd
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		int secondDayValue = calEnd.get(Calendar.DAY_OF_YEAR);
		// Calc Diff in days elapsed
		int i365 = 365;
		int diffdayslast = 0;
		if (secondDayValue >= firstDayValue )
			diffdayslast = secondDayValue - firstDayValue;
		if (secondDayValue < firstDayValue )
			diffdayslast = secondDayValue + (i365 - firstDayValue);
		// Fraccion de mes completo
		diffdayslast= (diffdayslast / 30 ) * 30 ;
		//
		BigDecimal DBdiffDays = new BigDecimal(diffdayslast);
		DBdiffDays.setScale(roundingMode, RoundingMode.HALF_UP);
		return DBdiffDays;
	}
		
	/**
	 * DV_FDAYS_THISYEAR
	 * REPLACE ORIGINAL RV_FDAYS_THISYEAR
	 * RV_FDAYS_THISYEAR Días transcurridos desde el 01/01/XXXX
	 * Días transcurridos desde el 01/01/XXXX
	 * Util para calcular valores fraccionados como VACACIONES, UTILIDADES y otros beneficios
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_FDAYS_THISYEAR (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		// BEGIN
		//Timestamp end =A_InvDateEnd;
		Timestamp InvDateEnd;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		InvDateEnd=amnpayroll.getInvDateEnd();
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		int roundingMode = curr.getStdPrecision();	
		// CalIni Fist day of YEAR
		Calendar calIni = Calendar.getInstance();
		calIni.set(Calendar.DAY_OF_YEAR, 1);
		// CalEnd A_InvDateEnd
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		int secondDayValue = calEnd.get(Calendar.DAY_OF_YEAR);
		// Calculates Days from Begining of the year
		int firstDayofYear = calIni.get(Calendar.DAY_OF_YEAR);
		int diffdayslast = secondDayValue - firstDayofYear;
		if (diffdayslast < 0 )
		    diffdayslast=0;
		diffdayslast = diffdayslast +1;
		BigDecimal DBdiffDays = new BigDecimal(diffdayslast);
		DBdiffDays.setScale(roundingMode, RoundingMode.HALF_UP);
		return DBdiffDays;

	}	
	
	/**
	 * DV_FYEAR_SERVICE
	 * REPLACE ORIGINAL RV_FYEAR_SERVICE
	 * RV_FYEAR_SERVICE Numero de Años de servicio para el cálculo de la indemnización por prestaciones sociales
	 * Numero de Años de servicio para el cálculo de la indemnización por prestaciones sociales.
	 * Si el numero de días es superio a 180 el primer año, entonces se calculará como 1.
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_FYEAR_SERVICE (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		// BEGIN
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		BigDecimal DByearService = BigDecimal.ZERO;
		int diffdays = 0;
		int diffyears = 0;
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		int roundingMode = curr.getStdPrecision();	
		// Calculates using Calendar class
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(EmployeeIncomeDate);
		int firstYearValue = calIni.get(Calendar.YEAR);
		int firstDayValue = calIni.get(Calendar.DAY_OF_YEAR);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		int secondYearValue = calEnd.get(Calendar.YEAR);
		int secondDayValue = calEnd.get(Calendar.DAY_OF_YEAR);
		// Calculates Days from Income Date
		if (secondDayValue > firstDayValue) {
			diffdays = secondDayValue - firstDayValue;
		} else {
			diffdays = secondDayValue + (365 - firstDayValue);
		}
		// Calculates Years elapsed from Income date
//		diffyears = secondYearValue - firstYearValue -1;
		diffyears = secondYearValue - firstYearValue ;
		// Year Validation > 180  means one year
		if (diffyears < 0 ) {
			DByearService = BigDecimal.ZERO;
		}
		if (diffyears > 0  ) {
			// IF Fragment of days is greater than 180 then add a year
			if (diffdays > 180) {
				diffyears = diffyears + 1;
			}
			DByearService = new BigDecimal(diffyears);
		}
		// IF Fragment of days is greater than 180 then add a year
		if (diffyears == 0 && diffdays > 180) {
			diffyears = diffyears + 1;
		}
		DByearService = new BigDecimal(diffyears);
		DByearService.setScale(roundingMode, RoundingMode.HALF_UP);
		return DByearService;

	}	
	
	/**
	 * DV_FDAYS_SERVICE
	 * REPLACE ORIGINAL RV_FDAYS_SERVICE
	 * RV_FDAYS_SERVICE Numero de días de servicio para el cálculo de la indemnización por prestaciones sociales
	 * Numero de Días de servicio para el cálculo de la indemnización por prestaciones sociales.
	 * Para el caso de que el Número de años sea cero, considerando menor a seis meses de servicio.
	 * @param Ctx
	 * @param AMN_Payroll_ID
	 * @param trxName
	 * @return
	 */
	public static BigDecimal DV_FDAYS_SERVICE (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		// BEGIN
		Timestamp InvDateEnd;
		Timestamp EmployeeIncomeDate;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		InvDateEnd=amnpayroll.getInvDateEnd();
		EmployeeIncomeDate=amnemployee.getincomedate();
		BigDecimal BDdaysService = BigDecimal.ZERO;
		boolean negative = false;
		int diffdays = 0;
		if (InvDateEnd.before(EmployeeIncomeDate))  {
		   negative = true;
		   Timestamp temp = EmployeeIncomeDate;
		   EmployeeIncomeDate = InvDateEnd;
		   InvDateEnd = temp;
		}
		// Calculates using Calendar class
		// start  (calIni)
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(EmployeeIncomeDate);
		calIni.set(Calendar.HOUR_OF_DAY, 0);
		calIni.set(Calendar.MINUTE, 0);
		calIni.set(Calendar.SECOND, 0);
		calIni.set(Calendar.MILLISECOND, 0);
		// end (calEnd)
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(InvDateEnd);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);
		// Calculates days
		diffdays = 0;
		while (calEnd.after(calIni))    {
		      calIni.add (Calendar.DAY_OF_YEAR, 1);
		      //calIni.add (Calendar.DATE, 1 );
		      diffdays++;
		}
		if (negative)
		     diffdays = diffdays * -1;
		// Convert to Big Decimal
		BDdaysService = new BigDecimal(diffdays);
		//BDdaysService.setScale(0, RoundingMode.HALF_UP);
		return BDdaysService;

	}
	

	
}

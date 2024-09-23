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
 ******************************************************************************/
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.compiere.acct.DocLine;
import org.compiere.model.MAccount;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * @author luisamesty
 *
 */
@SuppressWarnings("serial")
public class MAMN_Employee_Salary extends X_AMN_Employee_Salary {
	
	private BigDecimal BDZero = BigDecimal.valueOf(0);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**	Cache		MAMN_Employee_Salary					*/
	private static CCache<Integer,MAMN_Employee_Salary> s_cache = new CCache<Integer,MAMN_Employee_Salary >(Table_Name, 10);

	static CLogger log = CLogger.getCLogger(MAMN_Employee_Salary.class);
	/**
	 * @param p_ctx
	 * @param AMN_Employee_Salary_ID
	 * @param p_trxName
	 */
    public MAMN_Employee_Salary(Properties p_ctx, int AMN_Employee_Salary_ID, String p_trxName) {
	    super(p_ctx, AMN_Employee_Salary_ID, p_trxName);
	    // TODO Auto-generated constructor stub
    }

	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Employee_Salary(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // TODO Auto-generated constructor stub
    }

    /**	
	 * findAMN_Employee_Salary_byPayrollDate
	 * @param ctx
	 * @param locale
	 * @param int p_currency
	 * @param int p_AMN_Employee_ID
	 * @param Timestamp p_PayrollDate
	 * @param int p_AMN_Concept_Types_Proc_ID
	 * @return MAMN_Employee_Salary
	 */
	public static MAMN_Employee_Salary findAMN_Employee_Salary_byPayrollDate(
			int p_currency, int p_AMN_Employee_ID, 
			Timestamp p_PayrollDate,  int p_AMN_Concept_Types_Proc_ID) {
				
		MAMN_Employee_Salary retValue = null;
		String sql = "";
		int AD_Client_ID=Env.getAD_Client_ID(Env.getCtx());
		int AD_Org_ID=Env.getAD_Org_ID(Env.getCtx()); 
		if (p_PayrollDate == null)
			p_PayrollDate = new Timestamp (System.currentTimeMillis());
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		sql = "SELECT * " + 
			"FROM AMN_Employee_Salary " + 
			"WHERE C_Currency_ID=? " + 
			"AND AMN_Employee_ID=? " + 
			"AND AMN_Concept_Types_Proc_ID=? " + 
			"AND ? BETWEEN validfrom AND validto " + 
			"AND AD_Client_ID=? " + 
			"AND AD_Org_ID IN (0,?) " + 
			"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC"
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_currency);
            pstmt.setInt (2, p_AMN_Employee_ID);
            pstmt.setInt (3, p_AMN_Concept_Types_Proc_ID);
            pstmt.setTimestamp(4, p_PayrollDate);
			pstmt.setInt(5, AD_Client_ID);
			pstmt.setInt(6, AD_Org_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Employee_Salary amnemployeesalary = new MAMN_Employee_Salary(Env.getCtx(), rs, null);
				Integer key = new Integer(amnemployeesalary.getAMN_Employee_Salary_ID());
				s_cache.put (key, amnemployeesalary);
				if (amnemployeesalary.isActive())
					retValue = amnemployeesalary;
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
	}
	
    /**	
	 * findAMN_Employee_Salary_byPayrollID
	 * @param ctx
	 * @param locale
	 * @param int p_currency
	 * @param int p_AMN_Employee_ID
	 * @param int p_AMN_Payroll_ID
	 * @param int p_AMN_Concept_Types_Proc_ID
	 * @return MAMN_Employee_Salary
	 */
	public static MAMN_Employee_Salary findAMN_Employee_Salary_byPayrollID(
			int p_AD_Client_ID, int p_AD_Org_ID, int p_currency, int p_AMN_Employee_ID, 
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID) {
				
		MAMN_Employee_Salary retValue = null;
		String sql = "";
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		sql = "SELECT * " + 
			"FROM AMN_Employee_Salary " + 
			"WHERE C_Currency_ID=? " + 
			"AND AMN_Employee_ID=? " + 
			"AND AMN_Concept_Types_Proc_ID=? " + 
			"AND AMN_Payroll_ID= ? " + 
			"AND AD_Client_ID= ? " + 
			"AND AD_Org_ID IN (0,?) " + 
			"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC"
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_currency);
            pstmt.setInt (2, p_AMN_Employee_ID);
            pstmt.setInt (3, p_AMN_Concept_Types_Proc_ID);
            pstmt.setInt(4, p_AMN_Payroll_ID);
			pstmt.setInt(5, p_AD_Client_ID);
			pstmt.setInt(6, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//log.warning("...Encontro...p_AMN_Concept_Types_Proc_ID:"+p_AMN_Concept_Types_Proc_ID);
				MAMN_Employee_Salary amnemployeesalary = new MAMN_Employee_Salary(Env.getCtx(), rs, null);
				Integer key = new Integer(amnemployeesalary.getAMN_Employee_Salary_ID());
				s_cache.put (key, amnemployeesalary);
				if (amnemployeesalary.isActive())
					retValue = amnemployeesalary;
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
	}
	
    /**	
     * 
     * @param p_AD_Client_ID
     * @param p_AD_Org_ID
     * @param p_currency
     * @param p_AMN_Employee_ID
     * @param p_AMN_Concept_Types_Proc_ID
     * @return
     */
	public static MAMN_Employee_Salary findAMN_Employee_Salary_byConceptTypeProcID(
			int p_AD_Client_ID, int p_AD_Org_ID, int p_currency, int p_AMN_Employee_ID, 
		    int p_AMN_Concept_Types_Proc_ID) {
				
		MAMN_Employee_Salary retValue = null;
		String sql = "";
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
		sql = "SELECT * " + 
			"FROM AMN_Employee_Salary " + 
			"WHERE C_Currency_ID=? " + 
			"AND AMN_Employee_ID=? " + 
			"AND AMN_Concept_Types_Proc_ID=? " + 
			"AND AD_Client_ID= ? " + 
			"AND AD_Org_ID IN (0,?) " + 
			"ORDER BY AD_Client_ID DESC, AD_Org_ID DESC"
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_currency);
            pstmt.setInt (2, p_AMN_Employee_ID);
            pstmt.setInt (3, p_AMN_Concept_Types_Proc_ID);
			pstmt.setInt (4, p_AD_Client_ID);
			pstmt.setInt (5, p_AD_Org_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//log.warning("...Encontro...p_AMN_Concept_Types_Proc_ID:"+p_AMN_Concept_Types_Proc_ID);
				MAMN_Employee_Salary amnemployeesalary = new MAMN_Employee_Salary(Env.getCtx(), rs, null);
				Integer key = new Integer(amnemployeesalary.getAMN_Employee_Salary_ID());
				s_cache.put (key, amnemployeesalary);
				if (amnemployeesalary.isActive())
					retValue = amnemployeesalary;
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
	}
	
	/**
	 * createAMN_Employee_Salary
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param int p_currency
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @return boolean
	 * Env.getCtx(), Env.getLanguage(Env.getCtx()).getLocale()
	 */
	public static  boolean createAMN_Employee_Salary(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_currency, int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID) {
		BigDecimal BDZero = BigDecimal.valueOf(0);
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// PAYROLL
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		// Employee
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(),null);
		// Employee Salary Historic
		MAMN_Employee_Salary amnemployeesalary = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
				amnemployee.getAD_Client_ID(), amnemployee.getAD_Org_ID(), p_currency, amnemployee.getAMN_Employee_ID(), p_AMN_Payroll_ID , p_AMN_Concept_Types_Proc_ID);
		BigDecimal Active_Int_Rate = MAMN_Rates.sqlGetActive_Int_Rate(amnpayroll.getInvDateIni(), p_currency, p_AD_Client_ID, p_AD_Org_ID);
		BigDecimal Active_Int_Rate2 = MAMN_Rates.sqlGetActive_Int_Rate2(amnpayroll.getInvDateIni(), p_currency, p_AD_Client_ID, p_AD_Org_ID);
		
		if (amnemployeesalary == null) {
			//log.warning("................Values in MAMN_Payroll_Detail (NUEVO)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			//log.warning("ANTES DE EVALUAR Concept_DefaultValueST:"+Concept_DefaultValueST+"  Concept_DefaultValue:"+Concept_DefaultValue);
			// ***
			//log.warning("DESPUES DE EVALUAR Concept_DefaultValueST:"+Concept_DefaultValueST+"  Concept_DefaultValue:"+Concept_DefaultValue);
			// default KEYS Values
			amnemployeesalary = new MAMN_Employee_Salary(Env.getCtx(), 0, null);
			amnemployeesalary.setAD_Client_ID(p_AD_Client_ID);
			amnemployeesalary.setAD_Org_ID(p_AD_Org_ID);
			amnemployeesalary.setAMN_Employee_ID(amnemployee.getAMN_Employee_ID());
			amnemployeesalary.setAMN_Concept_Types_Proc_ID(p_AMN_Concept_Types_Proc_ID);
			amnemployeesalary.setC_Currency_ID(p_currency);
			amnemployeesalary.setAMN_Payroll_ID(p_AMN_Payroll_ID);
			amnemployeesalary.setDescription(amnpayroll.getDescription());
			// Prestacion Salary
			amnemployeesalary.setSalary(BDZero);
			amnemployeesalary.setSalary_Day(BDZero);
			amnemployeesalary.setVacationDays(0);
			amnemployeesalary.setUtilityDays(0);
			amnemployeesalary.setPrestacionDays(0);
			// Prestacion Amount Values to 0
			amnemployeesalary.setPrestacionAmount(BDZero);
			amnemployeesalary.setPrestacionAdjustment(BDZero);
			amnemployeesalary.setPrestacionAdvance(BDZero);
			amnemployeesalary.setPrestacionAccumulated(BDZero);
			// Prestacion Interes Values to 0
			amnemployeesalary.setPrestacionInterest(BDZero);
			amnemployeesalary.setPrestacionInterestAdjustment(BDZero);
			amnemployeesalary.setPrestacionInterestAdvance(BDZero);
			// Interes Rates
			amnemployeesalary.setactive_int_rate(Active_Int_Rate);
			amnemployeesalary.setactive_int_rate2(Active_Int_Rate2);
			// Dates
			amnemployeesalary.setValidFrom(amnpayroll.getInvDateIni());			
			amnemployeesalary.setValidTo(amnpayroll.getInvDateEnd());			
			// SAVES NEW
			amnemployeesalary.save();
		} else {
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");
			amnemployeesalary.setDescription(amnpayroll.getDescription());
		}
		//
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+" - "+amnemployeesalary.getDescription());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control
	
		return true;
	
	}	//	createAMN_Employee_Salary

	/**
	 * updateAMN_Employee_Salary
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param int p_currency
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @return boolean
	 */
	public static boolean updateAMN_Employee_Salary(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_currency, int p_AMN_Payroll_ID) {
		BigDecimal BDZero = BigDecimal.valueOf(0);
		String Error_Mess = "OK";
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// PAYROLL Process
		MAMN_Process amnprocess = null;
		// PAYROLL
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		// Employee
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(),null);
		// employee Salary Historic
		MAMN_Employee_Salary amnemployeesalaryABONOPS = null;
		MAMN_Employee_Salary amnemployeesalaryABONOPSA = null;
		MAMN_Employee_Salary amnemployeesalaryABONOMPSA = null;
		MAMN_Employee_Salary amnemployeesalaryANTICPRESTAC = null;
		MAMN_Employee_Salary amnemployeesalaryREINTPRESTAC = null;
		MAMN_Employee_Salary amnemployeesalaryINTERPRESTAC = null;
		// Concept Types
		// DEFAULT VALUES FOR NP on AMN_Employee_Salary
		// AMN_Concept_Types_ID for ABONOPS="ABONOPS"
		//int AMN_Concept_Types_ID_ABONOPS = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOPS'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ABONOPS =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOPS");
		int AMN_Concept_Types_Proc_ID_ABONOPS = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOPS(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ABONOPS);
		// AMN_Concept_Types_ID for ABONOPSA="ABONOMPSA"
		//int AMN_Concept_Types_ID_ABONOPSA = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOPSA'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ABONOPSA =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOPSA");
		int AMN_Concept_Types_Proc_ID_ABONOPSA = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ABONOPSA);
		//int AMN_Concept_Types_ID_ABONOMPSA = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOMPSA'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ABONOMPSA =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOMPSA");
		int AMN_Concept_Types_Proc_ID_ABONOMPSA = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ABONOMPSA);
		// ANTICPRESTAC
		//int AMN_Concept_Types_ID_ANTICPRESTAC = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ANTICPRESTAC'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ANTICPRESTAC =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ANTICPRESTAC");
		int AMN_Concept_Types_Proc_ID_ANTICPRESTAC = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ANTICPRESTAC);
		// REINTPRESTAC
		//int AMN_Concept_Types_ID_REINTPRESTAC = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='REINTPRESTAC'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_REINTPRESTAC =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"REINTPRESTAC");
		int AMN_Concept_Types_Proc_ID_REINTPRESTAC = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_REINTPRESTAC);
		// INTERPRESTAC
		//int AMN_Concept_Types_ID_INTERPRESTAC = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='INTERPRESTAC'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_INTERPRESTAC =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"INTERPRESTAC");
		int AMN_Concept_Types_Proc_ID_INTERPRESTAC = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_INTERPRESTAC);
		// Verify AMN_Payroll Process ONLY NP, PI , PR
		amnprocess = new MAMN_Process(ctx, amnpayroll.getAMN_Process_ID(), null);
		// NP LOCAL VARS ** ABONOPS **
		BigDecimal PS_Salary=BDZero;
		BigDecimal PS_Salary_Day=BDZero;
		int PS_VacationDays=0;
		int PS_UtilityDays=0;
		int PS_PrestacionDays=0;
		// Prestacion Values to 0
		BigDecimal PS_PrestacionAmount=BDZero;
		BigDecimal PS_PrestacionAdjustment=BDZero;
		BigDecimal PS_PrestacionAdvance=BDZero;
		BigDecimal PS_PrestacionReimbursement=BDZero;
		Boolean isPSA=false;
		Boolean isMPSA=false;
		Boolean isAnticPrest =false;
		Boolean isReintPrest =false;
		// Prestacion Interes Values to 0
		BigDecimal PS_PrestacionInterestAdjustment=BDZero;
		BigDecimal PS_PrestacionInterestAdvance=BDZero;
		Boolean isIntPrest =false;
		//
		BigDecimal PSA_Salary=BDZero;
		BigDecimal PSA_Salary_Day=BDZero;
		int PSA_VacationDays=0;
		int PSA_UtilityDays=0;
		int PSA_PrestacionDays=0;
		// Prestacion Values to 0
		BigDecimal PSA_PrestacionAmount=BDZero;
		// Interes Rates
		BigDecimal Active_Int_Rate = MAMN_Rates.sqlGetActive_Int_Rate(amnpayroll.getInvDateIni(), p_currency, p_AD_Client_ID, p_AD_Org_ID);
		BigDecimal Active_Int_Rate2 = MAMN_Rates.sqlGetActive_Int_Rate2(amnpayroll.getInvDateIni(), p_currency, p_AD_Client_ID, p_AD_Org_ID);
		// TAXRATE
		BigDecimal TAXRATE_BD= MAMN_Employee_Tax.getSQLEmployeeTaxRate(amnpayroll.getInvDateIni(), 
			p_currency, p_AD_Client_ID, p_AD_Org_ID, amnemployee.getAMN_Employee_ID());
				
		/**	Payroll Invoice Lines			*/
		MAMN_Payroll_Detail[] prlines = amnpayroll.getPayrollLinesAll(p_AMN_Payroll_ID);
		// *******************************
		// PROCESS NP SOCIAL BENEFITS  ACC
		// *******************************
		//	log.warning("E ...p_AMN_Payroll_ID:"+p_AMN_Payroll_ID+"  Process:"+amnprocess.getAMN_Process_Value()+"  AMN_Concept_Types_ID_ABONOPS:"+AMN_Concept_Types_ID_ABONOPS+"  AMN_Concept_Types_Proc_ID_ABONOPS:"+AMN_Concept_Types_Proc_ID_ABONOPS);
		if ( amnprocess.getAMN_Process_Value().equalsIgnoreCase("NP") ) {
			// Payroll Detail
			for (int i = 0; i < prlines.length; i++) 
			{
				// *******
				MAMN_Payroll_Detail amnpayrolldetailline = prlines[i];	
				MAMN_Concept_Types_Proc amnconcepttypesproc = new MAMN_Concept_Types_Proc(ctx, amnpayrolldetailline.getAMN_Concept_Types_Proc_ID(), null);
				MAMN_Concept_Types amnconceptypes = new MAMN_Concept_Types(ctx, amnconcepttypesproc.getAMN_Concept_Types_ID(), null);
//log.warning("i:"+i+" AMN_Concept_Types_Proc_ID:"+amnpayrolldetailline.getAMN_Concept_Types_Proc_ID()+" Value:"+amnconceptypes.getValue()+" Qty:"+amnpayrolldetailline.getQtyValue()+" Amount:"+amnpayrolldetailline.getAmountCalculated());
//log.warning("L"+i+" ...p_AMN_Payroll_ID:"+p_AMN_Payroll_ID+"  Process:"+amnprocess.getAMN_Process_Value()+"  AMN_Concept_Types_ID_ABONOPS:"+AMN_Concept_Types_ID_ABONOPS+"-"+amnconceptypes.getValue()+"  AMN_Concept_Types_Proc_ID_ABONOPS:"+AMN_Concept_Types_Proc_ID_ABONOPS);
				switch (amnconceptypes.getValue()) {
					case "SALPREST" : {
						PS_Salary_Day=amnpayrolldetailline.getQtyValue();	
						PS_Salary=PS_Salary_Day.multiply(BigDecimal.valueOf(30));
						PSA_Salary_Day =PS_Salary_Day;
						PSA_Salary =PS_Salary;
						}
						break;
					case "INCUTILID" : {
						PS_UtilityDays=amnpayrolldetailline.getQtyValue().intValue();
						PSA_UtilityDays=PS_UtilityDays;
						}
						break;
					case "INCVACAC" : {
						PS_VacationDays=amnpayrolldetailline.getQtyValue().intValue();
						PSA_VacationDays=PS_VacationDays;
						}
						break;
					case "ABONOPS" : {
						PS_PrestacionDays=amnpayrolldetailline.getQtyValue().intValue();
						PS_PrestacionAmount=amnpayrolldetailline.getAmountCalculated();
						}
						break;
						// Standard ABONOPSA Concept
					case "ABONOPSA" : {
						isPSA=false;
						PSA_PrestacionDays=amnpayrolldetailline.getQtyValue().intValue();
						PSA_PrestacionAmount=amnpayrolldetailline.getAmountCalculated();
						if (PSA_PrestacionAmount.compareTo(BDZero) > 0) 
							isPSA=true;
						}
						break;
						// Additional Concept create for non historic values calc purpouse
					case "ABONOMPSA" : {
						isMPSA=false;
						PSA_PrestacionDays=amnpayrolldetailline.getQtyValue().intValue();
						PSA_PrestacionAmount=amnpayrolldetailline.getAmountCalculated();
						if (PSA_PrestacionAmount.compareTo(BDZero) > 0) 
							isMPSA=true;
						}
						break;
					case "SAL12PREST" : {
						PSA_Salary_Day=amnpayrolldetailline.getQtyValue();	
						PSA_Salary=PS_Salary_Day.multiply(BigDecimal.valueOf(30));
						}
						break;
					case "AJUSTEPSDB" : {
						PS_PrestacionAdjustment=PS_PrestacionAdjustment.add(amnpayrolldetailline.getAmountCalculated());
						}
						break;
					case "AJUSTEPSCR" : {
						PS_PrestacionAdjustment=PS_PrestacionAdjustment.subtract(amnpayrolldetailline.getAmountCalculated());
						}
						break;
					case "INTERPRESTDB" : {
						PS_PrestacionInterestAdjustment=PS_PrestacionInterestAdjustment.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionInterestAdjustment.compareTo(BDZero) != 0) 
							isIntPrest=true;
						}
						break;
					case "INTERPRESTCR" : {
						PS_PrestacionInterestAdjustment=PS_PrestacionInterestAdjustment.subtract(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionInterestAdjustment.compareTo(BDZero) != 0) 
							isIntPrest=true;
						}
						break;
					case "INTERPRESTAC" : {
						PS_PrestacionInterestAdvance=PS_PrestacionInterestAdvance.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionInterestAdvance.compareTo(BDZero) != 0) 
							isIntPrest=true;
						}
						break;
					case "ANTICPRESTAC" : {
						PS_PrestacionAdvance=PS_PrestacionAdvance.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionAdvance.compareTo(BDZero) > 0) 
							isReintPrest=true;
						}
						break;
	
					case "REINTPRESTAC" : {
						PS_PrestacionReimbursement=PS_PrestacionReimbursement.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionReimbursement.compareTo(BDZero) > 0) 
							isReintPrest=true;
						}
						break;
					default: 
						break;	
				}
			}
			// Employee Salary Historic ABONOPS
			amnemployeesalaryABONOPS = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPS);
			if (amnemployeesalaryABONOPS == null) {
				createAMN_Employee_Salary(ctx, locale, p_AD_Client_ID, p_AD_Org_ID, 
						p_currency, p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPS);
			} 
			// ABONOPS
			amnemployeesalaryABONOPS = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPS);
			//	log.warning("amnemployeesalaryABONOPS:"+amnemployeesalaryABONOPS+"  PSA_PrestacionAmount="+PSA_PrestacionAmount);
			if (amnemployeesalaryABONOPS != null) {
				// Prestacion Salary
				amnemployeesalaryABONOPS.setSalary(PS_Salary);
				amnemployeesalaryABONOPS.setSalary_Day(PS_Salary_Day);
				amnemployeesalaryABONOPS.setVacationDays(PS_VacationDays);
				amnemployeesalaryABONOPS.setUtilityDays(PS_UtilityDays);
				amnemployeesalaryABONOPS.setPrestacionDays(PS_PrestacionDays);
				// Prestacion Amount Values to 0
				amnemployeesalaryABONOPS.setPrestacionAmount(PS_PrestacionAmount);
				amnemployeesalaryABONOPS.setPrestacionAdjustment(PS_PrestacionAdjustment);
				amnemployeesalaryABONOPS.setPrestacionAdvance(PS_PrestacionAdvance);
				amnemployeesalaryABONOPS.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
				// Prestacion Interes Values to 0
				amnemployeesalaryABONOPS.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
				amnemployeesalaryABONOPS.setPrestacionInterestAdjustment(PS_PrestacionInterestAdjustment);
				amnemployeesalaryABONOPS.setPrestacionInterestAdvance(PS_PrestacionInterestAdvance);
				// Interes Rates
				amnemployeesalaryABONOPS.setactive_int_rate(Active_Int_Rate);
				amnemployeesalaryABONOPS.setactive_int_rate2(Active_Int_Rate2);
				// TAXRATE_BD
				amnemployeesalaryABONOPS.setTaxRate(TAXRATE_BD);
				// Dates
				amnemployeesalaryABONOPS.setValidFrom(amnpayroll.getInvDateIni());			
				amnemployeesalaryABONOPS.setValidTo(amnpayroll.getInvDateEnd());
				amnemployeesalaryABONOPS.save();
			}
			// ABONOPSA Default Concept
			if (isPSA) {
				// log.warning("amnemployeesalaryABONOPSA:"+amnemployeesalaryABONOPSA);
				// Employee Salary Historic ABONOPSA
				amnemployeesalaryABONOPSA = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPSA);
				if (amnemployeesalaryABONOPSA == null) {
					createAMN_Employee_Salary(ctx, locale, p_AD_Client_ID, p_AD_Org_ID, 
							p_currency, p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPSA);
				} 
				amnemployeesalaryABONOPSA = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPSA);
				if (amnemployeesalaryABONOPSA != null) {
					// Prestacion Salary
					amnemployeesalaryABONOPSA.setSalary(PSA_Salary);
					amnemployeesalaryABONOPSA.setSalary_Day(PSA_Salary_Day);
					amnemployeesalaryABONOPSA.setVacationDays(PSA_VacationDays);
					amnemployeesalaryABONOPSA.setUtilityDays(PSA_UtilityDays);
					amnemployeesalaryABONOPSA.setPrestacionDays(PSA_PrestacionDays);
					// Prestacion Amount Values to 0
					amnemployeesalaryABONOPSA.setPrestacionAmount(PSA_PrestacionAmount);
					amnemployeesalaryABONOPSA.setPrestacionAdjustment(BDZero);
					amnemployeesalaryABONOPSA.setPrestacionAdvance(BDZero);
					amnemployeesalaryABONOPSA.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryABONOPSA.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryABONOPSA.setPrestacionInterestAdjustment(BDZero);
					amnemployeesalaryABONOPSA.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryABONOPSA.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryABONOPSA.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryABONOPSA.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryABONOPSA.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryABONOPSA.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryABONOPSA.save();
				}
			}
			// ABONOMPSA Additional Default Concept for historic incompleted purpouse
			if (isMPSA) {
				//	log.warning("amnemployeesalaryABONOMPSA:"+amnemployeesalaryABONOMPSA);
				// Employee Salary Historic ABONOMPSA
				amnemployeesalaryABONOMPSA = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOMPSA);
				if (amnemployeesalaryABONOMPSA == null) {
					createAMN_Employee_Salary(ctx, locale, p_AD_Client_ID, p_AD_Org_ID, 
							p_currency, p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOMPSA);
				} 
				amnemployeesalaryABONOMPSA = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOMPSA);
				if (amnemployeesalaryABONOMPSA != null) {
					// Prestacion Salary
					amnemployeesalaryABONOMPSA.setSalary(PSA_Salary);
					amnemployeesalaryABONOMPSA.setSalary_Day(PSA_Salary_Day);
					amnemployeesalaryABONOMPSA.setVacationDays(PSA_VacationDays);
					amnemployeesalaryABONOMPSA.setUtilityDays(PSA_UtilityDays);
					amnemployeesalaryABONOMPSA.setPrestacionDays(PSA_PrestacionDays);
					// Prestacion Amount Values to 0
					amnemployeesalaryABONOMPSA.setPrestacionAmount(PSA_PrestacionAmount);
					amnemployeesalaryABONOMPSA.setPrestacionAdjustment(BDZero);
					amnemployeesalaryABONOMPSA.setPrestacionAdvance(BDZero);
					amnemployeesalaryABONOMPSA.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryABONOMPSA.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryABONOMPSA.setPrestacionInterestAdjustment(BDZero);
					amnemployeesalaryABONOMPSA.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryABONOMPSA.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryABONOMPSA.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryABONOMPSA.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryABONOMPSA.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryABONOMPSA.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryABONOMPSA.save();
				}
			}
	
		}

		// *********************************************************
		// PROCESS PI SOCIAL BENEFITS INTEREST PAYMENT
		// CONCEPT TAG:INTERPRESTAC-INTERPRESTDB-INTERPRESTCR
		// *********************************************************
		if ( amnprocess.getAMN_Process_Value().equalsIgnoreCase("PI") ) {
			// Verifiy Employee Salary Historic ABONOPS
			amnemployeesalaryINTERPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_INTERPRESTAC);
			
			// Payroll Detail
			for (int i = 0; i < prlines.length; i++) 
			{
				// *******
				MAMN_Payroll_Detail amnpayrolldetailline = prlines[i];	
				MAMN_Concept_Types_Proc amnconcepttypesproc = new MAMN_Concept_Types_Proc(ctx, amnpayrolldetailline.getAMN_Concept_Types_Proc_ID(), null);
				MAMN_Concept_Types amnconceptypes = new MAMN_Concept_Types(ctx, amnconcepttypesproc.getAMN_Concept_Types_ID(), null);
//log.warning("i:"+i+" AMN_Concept_Types_Proc_ID:"+amnpayrolldetailline.getAMN_Concept_Types_Proc_ID()+" Value:"+amnconceptypes.getValue()+" Qty:"+amnpayrolldetailline.getQtyValue()+" Amount:"+amnpayrolldetailline.getAmountCalculated());
				switch (amnconceptypes.getValue()) {
					case "SALPREST" : {
						PS_Salary_Day=amnpayrolldetailline.getQtyValue();	
						PS_Salary=PS_Salary_Day.multiply(BigDecimal.valueOf(30));
						PSA_Salary_Day =PS_Salary_Day;
						PSA_Salary =PS_Salary;
						}
						break;
					case "INTERPRESTAC" : {
						PS_PrestacionInterestAdvance=PS_PrestacionInterestAdvance.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionInterestAdvance.compareTo(BDZero) != 0) 
							isIntPrest=true;
						}
						break;
					case "INTERPRESTDB" : {
						PS_PrestacionInterestAdjustment=PS_PrestacionInterestAdjustment.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionInterestAdjustment.compareTo(BDZero) != 0) 
							isIntPrest=true;
						}
						break;
					case "INTERPRESTCR" : {
						PS_PrestacionInterestAdjustment=PS_PrestacionInterestAdjustment.subtract(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionInterestAdjustment.compareTo(BDZero) != 0) 
							isIntPrest=true;
						}
						break;
					default:
					break;	
				}
			}
			// INTERPRESTAC
			if (amnemployeesalaryINTERPRESTAC == null) {
				createAMN_Employee_Salary(ctx, locale, p_AD_Client_ID, p_AD_Org_ID, 
						p_currency, p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_INTERPRESTAC);
			} 
			amnemployeesalaryINTERPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_INTERPRESTAC);
			// INTERPRESTAC
			if (amnemployeesalaryINTERPRESTAC != null) {
				// Employee Salary Historic INTERPRESTAC
				// Prestacion Salary
				amnemployeesalaryINTERPRESTAC.setSalary(PS_Salary);
				amnemployeesalaryINTERPRESTAC.setSalary_Day(PS_Salary_Day);
				amnemployeesalaryINTERPRESTAC.setVacationDays(0);
				amnemployeesalaryINTERPRESTAC.setUtilityDays(PS_UtilityDays);
				amnemployeesalaryINTERPRESTAC.setPrestacionDays(PS_PrestacionDays);
				// Prestacion Salary
				amnemployeesalaryINTERPRESTAC.setSalary(PS_Salary);
				amnemployeesalaryINTERPRESTAC.setSalary_Day(PS_Salary_Day);
				// Prestacion Interes Values to 0
				amnemployeesalaryINTERPRESTAC.setPrestacionInterestAdvance(PS_PrestacionInterestAdvance);
				amnemployeesalaryINTERPRESTAC.setPrestacionInterestAdjustment(PS_PrestacionInterestAdjustment);
				// Interes Rates
				amnemployeesalaryINTERPRESTAC.setactive_int_rate(Active_Int_Rate);
				amnemployeesalaryINTERPRESTAC.setactive_int_rate2(Active_Int_Rate2);
				// TAXRATE_BD
				amnemployeesalaryINTERPRESTAC.setTaxRate(TAXRATE_BD);
				// Dates
				amnemployeesalaryINTERPRESTAC.setValidFrom(amnpayroll.getInvDateIni());			
				amnemployeesalaryINTERPRESTAC.setValidTo(amnpayroll.getInvDateEnd());
				amnemployeesalaryINTERPRESTAC.save();
			}
		}
		
		// ********************************************
		// PROCESS PR SOCIAL BENEFITS PARTIAL PAYMENTS 
		//	CONCEPT TAG: ANTICPRESTAC
		// ********************************************
		// amnemployeesalaryANTICPRESTAC = null;
		// amnemployeesalaryREINTPRESTAC = null;
		if ( amnprocess.getAMN_Process_Value().equalsIgnoreCase("PR") ) {
			// Verifiy Employee Salary Historic ANTICPRESTAC
			amnemployeesalaryANTICPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ANTICPRESTAC);
			amnemployeesalaryREINTPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_REINTPRESTAC);

			// Payroll Detail
			for (int i = 0; i < prlines.length; i++) 
			{
				// *******
				MAMN_Payroll_Detail amnpayrolldetailline = prlines[i];	
				MAMN_Concept_Types_Proc amnconcepttypesproc = new MAMN_Concept_Types_Proc(ctx, amnpayrolldetailline.getAMN_Concept_Types_Proc_ID(), null);
				MAMN_Concept_Types amnconceptypes = new MAMN_Concept_Types(ctx, amnconcepttypesproc.getAMN_Concept_Types_ID(), null);
//log.warning("i:"+i+" AMN_Concept_Types_Proc_ID:"+amnpayrolldetailline.getAMN_Concept_Types_Proc_ID()+" Value:"+amnconceptypes.getValue()+" Qty:"+amnpayrolldetailline.getQtyValue()+" Amount:"+amnpayrolldetailline.getAmountCalculated());
//log.warning("1 amnemployeesalaryANTICPRESTAC"+amnemployeesalaryANTICPRESTAC+"  amnemployeesalaryREINTPRESTAC"+amnemployeesalaryREINTPRESTAC);
				switch (amnconceptypes.getValue()) {
					case "SALPREST" : {
							PS_Salary_Day=amnpayrolldetailline.getQtyValue();	
							PS_Salary=PS_Salary_Day.multiply(BigDecimal.valueOf(30));
							PSA_Salary_Day =PS_Salary_Day;
							PSA_Salary =PS_Salary;
							}
						break;
					case "ANTICPRESTAC" : {
						PS_PrestacionAdvance=PS_PrestacionAdvance.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionAdvance.compareTo(BDZero) > 0) 
							isAnticPrest=true;
						}
						break;
					case "REINTPRESTAC" : {
						PS_PrestacionReimbursement=PS_PrestacionReimbursement.add(amnpayrolldetailline.getAmountCalculated());
						if (PS_PrestacionReimbursement.compareTo(BDZero) > 0) 
							isReintPrest=true;
						}
						break;
					default: 
					break;	
				}
			}
			
			// ANTICPRESTAC
			if (isAnticPrest) {
				// VERIFY IF CREATED 
				if (amnemployeesalaryANTICPRESTAC == null) {
					createAMN_Employee_Salary(ctx, locale, p_AD_Client_ID, p_AD_Org_ID, 
							p_currency, p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ANTICPRESTAC);
				} 
				amnemployeesalaryANTICPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(),  p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ANTICPRESTAC);
				//
				if ( PS_PrestacionAdvance.compareTo(BigDecimal.ZERO) > 0 
						&& amnemployeesalaryANTICPRESTAC != null )
				 {
					// Employee Salary Historic ANTICPRESTAC
					// Prestacion Salary
					amnemployeesalaryANTICPRESTAC.setSalary(PS_Salary);
					amnemployeesalaryANTICPRESTAC.setSalary_Day(PS_Salary_Day);
					// Prestacion Amount Values to 0
					amnemployeesalaryANTICPRESTAC.setPrestacionAmount(BDZero);
					amnemployeesalaryANTICPRESTAC.setPrestacionAdjustment(BDZero);
					amnemployeesalaryANTICPRESTAC.setPrestacionAdvance(PS_PrestacionAdvance);
					amnemployeesalaryANTICPRESTAC.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryANTICPRESTAC.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryANTICPRESTAC.setPrestacionInterestAdjustment(BDZero);
					amnemployeesalaryANTICPRESTAC.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryANTICPRESTAC.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryANTICPRESTAC.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryANTICPRESTAC.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryANTICPRESTAC.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryANTICPRESTAC.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryANTICPRESTAC.save();
				}
			}
			// REINTPRESTAC (ONLY IF CONCEPT IS on RECEIPT)
			if (isReintPrest) {
				if (amnemployeesalaryREINTPRESTAC == null) {
					createAMN_Employee_Salary(ctx, locale, p_AD_Client_ID, p_AD_Org_ID, 
							p_currency, p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_REINTPRESTAC);
				} 
				
				amnemployeesalaryREINTPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_REINTPRESTAC);

				//
				if ( PS_PrestacionReimbursement.compareTo(BigDecimal.ZERO) > 0 
						&& amnemployeesalaryREINTPRESTAC != null) {
					// Employee Salary Historic REINTPRESTAC
					// Prestacion Salary
					amnemployeesalaryREINTPRESTAC.setSalary(PSA_Salary);
					amnemployeesalaryREINTPRESTAC.setSalary_Day(PSA_Salary_Day);
					
					
					// Prestacion Amount Values to 0
					amnemployeesalaryREINTPRESTAC.setPrestacionAmount(BDZero);
					amnemployeesalaryREINTPRESTAC.setPrestacionAdjustment(BDZero);
					amnemployeesalaryREINTPRESTAC.setPrestacionAdvance(BDZero);
					amnemployeesalaryREINTPRESTAC.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryREINTPRESTAC.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryREINTPRESTAC.setPrestacionInterestAdjustment(PS_PrestacionReimbursement);
					amnemployeesalaryREINTPRESTAC.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryREINTPRESTAC.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryREINTPRESTAC.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryREINTPRESTAC.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryREINTPRESTAC.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryREINTPRESTAC.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryREINTPRESTAC.save();
				}
			}

//log.warning("2 amnemployeesalaryANTICPRESTAC"+amnemployeesalaryANTICPRESTAC+"  amnemployeesalaryREINTPRESTAC"+amnemployeesalaryREINTPRESTAC);
//log.warning("PS_PrestacionAdvance="+PS_PrestacionAdvance+"  PS_PrestacionReimbursement="+PS_PrestacionReimbursement);

		}
//log.warning("amnemployeesalaryABONOPS="+amnemployeesalaryABONOPS+"  amnemployeesalaryABONOPSA="+amnemployeesalaryABONOPSA+"  amnemployeesalaryABONOMPSA="+amnemployeesalaryABONOMPSA);				
//log.warning("AMN_Concept_Types_Proc_ID_ANTICPRESTAC="+AMN_Concept_Types_Proc_ID_ANTICPRESTAC+"  AMN_Concept_Types_Proc_ID_REINTPRESTAC="+AMN_Concept_Types_Proc_ID_REINTPRESTAC);
		//
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+" ERROR:"+Error_Mess.trim());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control
	
		return true;
	
	}	//	updateAMN_Employee_Salary
	
	
	/**
	 * resetAMN_Employee_Salary  Reset to Zero Values when Reactivate Payroll Receipt
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param int p_currency
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @return boolean
	 */
	public static boolean resetAMN_Employee_Salary(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID, 
			int p_currency, int p_AMN_Payroll_ID) {
		BigDecimal BDZero = BigDecimal.valueOf(0);
		String Error_Mess = "OK";
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// PAYROLL Process
		MAMN_Process amnprocess = null;
		// PAYROLL
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
		// Employee
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(),null);
		// employee Salary Historic
		MAMN_Employee_Salary amnemployeesalaryABONOPS = null;
		MAMN_Employee_Salary amnemployeesalaryABONOPSA = null;
		MAMN_Employee_Salary amnemployeesalaryABONOMPSA = null;
		MAMN_Employee_Salary amnemployeesalaryANTICPRESTAC = null;
		MAMN_Employee_Salary amnemployeesalaryREINTPRESTAC = null;
		MAMN_Employee_Salary amnemployeesalaryINTERPRESTAC = null;
		// Concept Types
		// DEFAULT VALUES FOR NP on AMN_Employee_Salary
		// AMN_Concept_Types_ID for ABONOPS="ABONOPS"
		//int AMN_Concept_Types_ID_ABONOPS = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOPS'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ABONOPS =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOPS");
		int AMN_Concept_Types_Proc_ID_ABONOPS = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOPS(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ABONOPS);
		// AMN_Concept_Types_ID for ABONOPSA="ABONOMPSA"
		//int AMN_Concept_Types_ID_ABONOPSA = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOPSA'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ABONOPSA =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOPSA");
		int AMN_Concept_Types_Proc_ID_ABONOPSA = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ABONOPSA);
		//int AMN_Concept_Types_ID_ABONOMPSA = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ABONOMPSA'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ABONOMPSA =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ABONOMPSA");
		int AMN_Concept_Types_Proc_ID_ABONOMPSA = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ABONOMPSA);
		// ANTICPRESTAC
		//int AMN_Concept_Types_ID_ANTICPRESTAC = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='ANTICPRESTAC'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_ANTICPRESTAC =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"ANTICPRESTAC");
		int AMN_Concept_Types_Proc_ID_ANTICPRESTAC = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_ANTICPRESTAC);
		// REINTPRESTAC
		//int AMN_Concept_Types_ID_REINTPRESTAC = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='REINTPRESTAC'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_REINTPRESTAC =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"REINTPRESTAC");
		int AMN_Concept_Types_Proc_ID_REINTPRESTAC = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_REINTPRESTAC);
		// INTERPRESTAC
		//int AMN_Concept_Types_ID_INTERPRESTAC = ((X_AMN_Concept_Types)new Query(Env.getCtx(),X_AMN_Concept_Types.Table_Name,"Value='INTERPRESTAC'",null).first()).getAMN_Concept_Types_ID();
		int AMN_Concept_Types_ID_INTERPRESTAC =  MAMN_Concept_Types.sqlGetAMNConceptTypesByValue(p_AD_Client_ID,"INTERPRESTAC");
		int AMN_Concept_Types_Proc_ID_INTERPRESTAC = MAMN_Concept_Types_Proc.sqlGetAMNConceptTypesProcABONOMPSA(p_AD_Client_ID, p_AD_Org_ID, AMN_Concept_Types_ID_INTERPRESTAC);
		// Verify AMN_Payroll Process ONLY NP, PI , PR
		amnprocess = new MAMN_Process(ctx, amnpayroll.getAMN_Process_ID(), null);
		// NP LOCAL VARS ** ABONOPS **
		BigDecimal PS_Salary=BDZero;
		BigDecimal PS_Salary_Day=BDZero;
		int PS_VacationDays=0;
		int PS_UtilityDays=0;
		int PS_PrestacionDays=0;
		// Prestacion Values to 0
		BigDecimal PS_PrestacionAmount=BDZero;
		BigDecimal PS_PrestacionAdjustment=BDZero;
		BigDecimal PS_PrestacionAdvance=BDZero;
		BigDecimal PS_PrestacionReimbursement=BDZero;
		// Prestacion Interes Values to 0
		BigDecimal PS_PrestacionInterestAdjustment=BDZero;
		BigDecimal PS_PrestacionInterestAdvance=BDZero;
		// NP LOCAL VARS ** ABONOPSA  ADDITIONAL 2nd REC **
		Boolean isPSA=false;
		BigDecimal PSA_Salary=BDZero;
		BigDecimal PSA_Salary_Day=BDZero;
		int PSA_VacationDays=0;
		int PSA_UtilityDays=0;
		int PSA_PrestacionDays=0;
		// Prestacion Values to 0
		BigDecimal PSA_PrestacionAmount=BDZero;
		// Interes Rates
		BigDecimal Active_Int_Rate = MAMN_Rates.sqlGetActive_Int_Rate(amnpayroll.getInvDateIni(), p_currency, p_AD_Client_ID, p_AD_Org_ID);
		BigDecimal Active_Int_Rate2 = MAMN_Rates.sqlGetActive_Int_Rate2(amnpayroll.getInvDateIni(), p_currency, p_AD_Client_ID, p_AD_Org_ID);
		// TAXRATE
		BigDecimal TAXRATE_BD= MAMN_Employee_Tax.getSQLEmployeeTaxRate(amnpayroll.getInvDateIni(), 
			p_currency, p_AD_Client_ID, p_AD_Org_ID, amnemployee.getAMN_Employee_ID());

		/**	Payroll Invoice Lines			*/
		MAMN_Payroll_Detail[] prlines = amnpayroll.getPayrollLinesAll(p_AMN_Payroll_ID);
				
		//log.warning("...p_AMN_Payroll_ID:"+p_AMN_Payroll_ID+"  Process:"+amnprocess.getAMN_Process_Value()+"  AMN_Concept_Types_ID_ABONOPS:"+AMN_Concept_Types_ID_ABONOPS+"  AMN_Concept_Types_Proc_ID_ABONOPS:"+AMN_Concept_Types_Proc_ID_ABONOPS);
		// *******************************
		// PROCESS NP SOCIAL BENEFITS  ACC
		// *******************************
		if ( amnprocess.getAMN_Process_Value().equalsIgnoreCase("NP") ) {
			// Employee Salary Historic ABONOPS
			// FIND ABONOPS REC
			amnemployeesalaryABONOPS = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPS);
			if (amnemployeesalaryABONOPS != null) {
				// Prestacion Salary
				amnemployeesalaryABONOPS.setSalary(PS_Salary);
				amnemployeesalaryABONOPS.setSalary_Day(PS_Salary_Day);
				amnemployeesalaryABONOPS.setVacationDays(PS_VacationDays);
				amnemployeesalaryABONOPS.setUtilityDays(PS_UtilityDays);
				amnemployeesalaryABONOPS.setPrestacionDays(PS_PrestacionDays);
				// Prestacion Amount Values to 0
				amnemployeesalaryABONOPS.setPrestacionAmount(PS_PrestacionAmount);
				amnemployeesalaryABONOPS.setPrestacionAdjustment(PS_PrestacionAdjustment);
				amnemployeesalaryABONOPS.setPrestacionAdvance(PS_PrestacionAdvance);
				amnemployeesalaryABONOPS.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
				// Prestacion Interes Values to 0
				amnemployeesalaryABONOPS.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
				amnemployeesalaryABONOPS.setPrestacionInterestAdjustment(PS_PrestacionInterestAdjustment);
				amnemployeesalaryABONOPS.setPrestacionInterestAdvance(PS_PrestacionInterestAdvance);
				// Interes Rates
				amnemployeesalaryABONOPS.setactive_int_rate(Active_Int_Rate);
				amnemployeesalaryABONOPS.setactive_int_rate2(Active_Int_Rate2);
				// TAXRATE_BD
				amnemployeesalaryABONOPS.setTaxRate(TAXRATE_BD);
				// Dates
				amnemployeesalaryABONOPS.setValidFrom(amnpayroll.getInvDateIni());			
				amnemployeesalaryABONOPS.setValidTo(amnpayroll.getInvDateEnd());
				amnemployeesalaryABONOPS.save();
			}
			// FIND ABONOPSA REC
			amnemployeesalaryABONOPSA = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOPSA);
			if (amnemployeesalaryABONOPSA != null) {
				// Prestacion Salary
				amnemployeesalaryABONOPSA.setSalary(PSA_Salary);
				amnemployeesalaryABONOPSA.setSalary_Day(PSA_Salary_Day);
				amnemployeesalaryABONOPSA.setVacationDays(PSA_VacationDays);
				amnemployeesalaryABONOPSA.setUtilityDays(PSA_UtilityDays);
				amnemployeesalaryABONOPSA.setPrestacionDays(PSA_PrestacionDays);
				// Prestacion Amount Values to 0
				amnemployeesalaryABONOPSA.setPrestacionAmount(PSA_PrestacionAmount);
				amnemployeesalaryABONOPSA.setPrestacionAdjustment(BDZero);
				amnemployeesalaryABONOPSA.setPrestacionAdvance(BDZero);
				amnemployeesalaryABONOPSA.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
				// Prestacion Interes Values to 0
				amnemployeesalaryABONOPSA.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
				amnemployeesalaryABONOPSA.setPrestacionInterestAdjustment(BDZero);
				amnemployeesalaryABONOPSA.setPrestacionInterestAdvance(BDZero);
				// Interes Rates
				amnemployeesalaryABONOPSA.setactive_int_rate(Active_Int_Rate);
				amnemployeesalaryABONOPSA.setactive_int_rate2(Active_Int_Rate2);
				// TAXRATE_BD
				amnemployeesalaryABONOPSA.setTaxRate(TAXRATE_BD);
				// Dates
				amnemployeesalaryABONOPSA.setValidFrom(amnpayroll.getInvDateIni());			
				amnemployeesalaryABONOPSA.setValidTo(amnpayroll.getInvDateEnd());
				amnemployeesalaryABONOPSA.save();
			}			
			
			// FIND ABONOMPSA REC (Additional Concept )
			amnemployeesalaryABONOMPSA = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
					p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ABONOMPSA);
			if (amnemployeesalaryABONOMPSA != null) {
				// Prestacion Salary
				amnemployeesalaryABONOMPSA.setSalary(PSA_Salary);
				amnemployeesalaryABONOMPSA.setSalary_Day(PSA_Salary_Day);
				amnemployeesalaryABONOMPSA.setVacationDays(PSA_VacationDays);
				amnemployeesalaryABONOMPSA.setUtilityDays(PSA_UtilityDays);
				amnemployeesalaryABONOMPSA.setPrestacionDays(PSA_PrestacionDays);
				// Prestacion Amount Values to 0
				amnemployeesalaryABONOMPSA.setPrestacionAmount(PSA_PrestacionAmount);
				amnemployeesalaryABONOMPSA.setPrestacionAdjustment(BDZero);
				amnemployeesalaryABONOMPSA.setPrestacionAdvance(BDZero);
				amnemployeesalaryABONOMPSA.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
				// Prestacion Interes Values to 0
				amnemployeesalaryABONOMPSA.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
				amnemployeesalaryABONOMPSA.setPrestacionInterestAdjustment(BDZero);
				amnemployeesalaryABONOMPSA.setPrestacionInterestAdvance(BDZero);
				// Interes Rates
				amnemployeesalaryABONOMPSA.setactive_int_rate(Active_Int_Rate);
				amnemployeesalaryABONOMPSA.setactive_int_rate2(Active_Int_Rate2);
				// TAXRATE_BD
				amnemployeesalaryABONOMPSA.setTaxRate(TAXRATE_BD);
				// Dates
				amnemployeesalaryABONOMPSA.setValidFrom(amnpayroll.getInvDateIni());			
				amnemployeesalaryABONOMPSA.setValidTo(amnpayroll.getInvDateEnd());
				amnemployeesalaryABONOMPSA.save();
			}			

			// ********************************************
			// PROCESS PI SOCIAL BENEFITS INTEREST PAYMENT
			// INTERPRESTAC
			// ********************************************
			if ( amnprocess.getAMN_Process_Value().equalsIgnoreCase("PI") ) {
				// Employee Salary Historic ABONOPS
				// FIND INTERPRESTAC REC
				amnemployeesalaryINTERPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_INTERPRESTAC);
				if (amnemployeesalaryINTERPRESTAC != null) {
					// Prestacion Salary
					amnemployeesalaryINTERPRESTAC.setSalary(PSA_Salary);
					amnemployeesalaryINTERPRESTAC.setSalary_Day(PSA_Salary_Day);
					amnemployeesalaryINTERPRESTAC.setVacationDays(PSA_VacationDays);
					amnemployeesalaryINTERPRESTAC.setUtilityDays(PSA_UtilityDays);
					amnemployeesalaryINTERPRESTAC.setPrestacionDays(PSA_PrestacionDays);
					// Prestacion Amount Values to 0
					amnemployeesalaryINTERPRESTAC.setPrestacionAmount(PSA_PrestacionAmount);
					amnemployeesalaryINTERPRESTAC.setPrestacionAdjustment(BDZero);
					amnemployeesalaryINTERPRESTAC.setPrestacionAdvance(BDZero);
					amnemployeesalaryINTERPRESTAC.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryINTERPRESTAC.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryINTERPRESTAC.setPrestacionInterestAdjustment(BDZero);
					amnemployeesalaryINTERPRESTAC.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryINTERPRESTAC.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryINTERPRESTAC.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryINTERPRESTAC.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryINTERPRESTAC.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryINTERPRESTAC.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryINTERPRESTAC.save();
				}
			}			
			// ********************************************
			// PROCESS PR SOCIAL BENEFITS PARTIAL PAYMENTS 
			// ANTICPRESTAC 
			// REINTPRESTAC
			// ********************************************
			if ( amnprocess.getAMN_Process_Value().equalsIgnoreCase("PR") ) {
				// Employee Salary Historic ABONOPS
				// FIND ANTICPRESTAC REC
				amnemployeesalaryANTICPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_ANTICPRESTAC);
				if (amnemployeesalaryINTERPRESTAC != null) {
					// Prestacion Salary
					amnemployeesalaryANTICPRESTAC.setSalary(PSA_Salary);
					amnemployeesalaryANTICPRESTAC.setSalary_Day(PSA_Salary_Day);
					amnemployeesalaryANTICPRESTAC.setVacationDays(PSA_VacationDays);
					amnemployeesalaryANTICPRESTAC.setUtilityDays(PSA_UtilityDays);
					amnemployeesalaryANTICPRESTAC.setPrestacionDays(PSA_PrestacionDays);
					// Prestacion Amount Values to 0
					amnemployeesalaryANTICPRESTAC.setPrestacionAmount(PSA_PrestacionAmount);
					amnemployeesalaryANTICPRESTAC.setPrestacionAdjustment(BDZero);
					amnemployeesalaryANTICPRESTAC.setPrestacionAdvance(BDZero);
					amnemployeesalaryANTICPRESTAC.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryANTICPRESTAC.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryANTICPRESTAC.setPrestacionInterestAdjustment(BDZero);
					amnemployeesalaryANTICPRESTAC.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryANTICPRESTAC.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryANTICPRESTAC.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryANTICPRESTAC.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryANTICPRESTAC.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryANTICPRESTAC.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryANTICPRESTAC.save();
				}
				// FIND REINTPRESTAC REC
				amnemployeesalaryREINTPRESTAC = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
						p_AD_Client_ID, p_AD_Org_ID, p_currency, amnpayroll.getAMN_Employee_ID(), p_AMN_Payroll_ID, AMN_Concept_Types_Proc_ID_REINTPRESTAC);
				if (amnemployeesalaryREINTPRESTAC != null) {
					// Prestacion Salary
					amnemployeesalaryREINTPRESTAC.setSalary(PSA_Salary);
					amnemployeesalaryREINTPRESTAC.setSalary_Day(PSA_Salary_Day);
					amnemployeesalaryREINTPRESTAC.setVacationDays(PSA_VacationDays);
					amnemployeesalaryREINTPRESTAC.setUtilityDays(PSA_UtilityDays);
					amnemployeesalaryREINTPRESTAC.setPrestacionDays(PSA_PrestacionDays);
					// Prestacion Amount Values to 0
					amnemployeesalaryREINTPRESTAC.setPrestacionAmount(PSA_PrestacionAmount);
					amnemployeesalaryREINTPRESTAC.setPrestacionAdjustment(BDZero);
					amnemployeesalaryREINTPRESTAC.setPrestacionAdvance(BDZero);
					amnemployeesalaryREINTPRESTAC.setPrestacionAccumulated(BDZero); // AMOUNT CALCULATED BY AD
					// Prestacion Interes Values to 0
					amnemployeesalaryREINTPRESTAC.setPrestacionInterest(BDZero); // AMOUNT CALCULATED BY AD
					amnemployeesalaryREINTPRESTAC.setPrestacionInterestAdjustment(BDZero);
					amnemployeesalaryREINTPRESTAC.setPrestacionInterestAdvance(BDZero);
					// Interes Rates
					amnemployeesalaryREINTPRESTAC.setactive_int_rate(Active_Int_Rate);
					amnemployeesalaryREINTPRESTAC.setactive_int_rate2(Active_Int_Rate2);
					// TAXRATE_BD
					amnemployeesalaryREINTPRESTAC.setTaxRate(TAXRATE_BD);
					// Dates
					amnemployeesalaryREINTPRESTAC.setValidFrom(amnpayroll.getInvDateIni());			
					amnemployeesalaryREINTPRESTAC.setValidTo(amnpayroll.getInvDateEnd());
					amnemployeesalaryREINTPRESTAC.save();
				}
			}			

		}
		//
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					amnemployee.getValue().trim()+"-"+amnemployee.getName().trim());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control
		return true;
		}	//	resetAMN_Employee_Salary

		/**
	 *	Get getSQLEmployeeInterest
	 * 	@param	p_AD_Client_ID client
	 * 	@param  p_AD_Org_ID	organization
	 *  @param  p_AMN_Employee_ID
	 *   @param  p_AMN_Payroll_ID    
	 *  @param  p_AMN_Concept_Types_Proc_ID
	 *  @return p_currency Rate or null
	 *  
	 */
	public static BigDecimal getSQLEmployeeInterest (
			int p_currency, int p_AD_Client_ID, int p_AD_Org_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, int p_AMN_Concept_Types_Proc_ID)
	{
		BigDecimal retValue = BigDecimal.valueOf(0);
		//log.warning("getSQLEmployeeTaxRate p_PayrollDate:"+p_PayrollDate+"  p_currency:"+p_currency+" "+p_AD_Client_ID+"  "+p_AD_Org_ID);
		// p_currency
		if (p_currency == 0) {
			p_currency =  Integer.parseInt(System.getenv(COLUMNNAME_C_Currency_ID));
		}
//		public static MAMN_Employee_Salary findAMN_Employee_Salary_byPayrollID(
//				int p_currency, int p_AMN_Employee_ID, 
//				int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID) {
//			
//		}

		MAMN_Employee_Salary amnemployeesalary = MAMN_Employee_Salary.findAMN_Employee_Salary_byPayrollID( 
				p_AD_Client_ID, p_AD_Org_ID, p_currency, p_AMN_Concept_Types_Proc_ID, p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_ID);
		retValue = amnemployeesalary.getPrestacionInterest();
		return retValue;
	}	//	getSQLEmployeeInterest
	
	
}

package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.amerp.amnmodel.MAMN_CommissionGroup;
import org.amerp.amnmodel.MAMN_Concept_Types;
import org.amerp.amnmodel.MAMN_Concept_Types_Limit;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Department;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Employee_Tax;
import org.amerp.amnmodel.MAMN_Jobstation;
import org.amerp.amnmodel.MAMN_Jobtitle;
import org.amerp.amnmodel.MAMN_Jobunit;
import org.amerp.amnmodel.MAMN_Location;
import org.amerp.amnmodel.MAMN_NonBusinessDay;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnmodel.MAMN_Rates;
import org.amerp.amnmodel.MAMN_Shift;
import org.compiere.model.MActivity;
import org.compiere.model.MCurrency;
import org.compiere.model.MProject;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class PayrollVariablesLoad {

	public PayrollVariablesLoad() {
		super();
		// 
	}

	CLogger log = CLogger.getCLogger(PayrollVariablesLoad.class);
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * loadPayrollVariables
	 * @param p_ctx
	 * @param pyVars
	 * @param p_AMN_Payroll_ID
	 * @param p_Concept_Reference
	 * @param p_script
	 * @param p_va_SB
	 * @param forceRounding
	 */
	void setPayrollVariablesMain(Properties p_ctx, PayrollVariables pyVars, int p_AMN_Payroll_ID,
			String p_Concept_Reference, String p_script,
			BigDecimal p_va_SB,  boolean forceRounding) {
		
		double SBD = p_va_SB.doubleValue();
		int roundingMode = 2;
		pyVars.set("QT_SB", SBD);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		pyVars.set("REC_InitDate", dateFormat.format(amnpayroll.getInvDateIni()));
		pyVars.set("REC_EndDate", dateFormat.format(amnpayroll.getInvDateEnd()));
		pyVars.set("ACCT_Date", dateFormat.format(amnpayroll.getDateAcct()));
		if (amnpayroll.getRefDateIni() == null)
			pyVars.set("REF_InitDate", dateFormat.format(amnpayroll.getInvDateIni()));
		else
			pyVars.set("REF_InitDate", dateFormat.format(amnpayroll.getRefDateIni()));
		if (amnpayroll.getRefDateEnd() == null)
			pyVars.set("REF_EndDate", dateFormat.format(amnpayroll.getInvDateEnd()));
		else
			pyVars.set("REF_EndDate", dateFormat.format(amnpayroll.getRefDateEnd()));
		
		MAMN_Contract amncontract = new MAMN_Contract(p_ctx, amnpayroll.getAMN_Contract_ID(), null);
		MAMN_Process amnprocess = new MAMN_Process(p_ctx, amnpayroll.getAMN_Process_ID(), null);
		int AMN_Concept_Types_ID = MAMN_Concept_Types.sqlGetAMNConceptTypesID(amnpayroll.getAD_Client_ID(), p_Concept_Reference);
		MCurrency curr = new MCurrency(p_ctx, amnpayroll.getC_Currency_ID(), null);
		pyVars.set("AM_Contract", amncontract.getValue().trim());
		pyVars.set("AM_Process", amnprocess.getValue().trim());
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), null);
		MAMN_Shift amnshift = new MAMN_Shift(p_ctx, amnemployee.getAMN_Shift_ID(), null);
		pyVars.set("AM_Shift", amnshift.getValue());
		pyVars.set("AM_PayrollMode", amnemployee.getpayrollmode());
		pyVars.set("AM_Status", amnemployee.getStatus());
		pyVars.set("AMN_downwardloads", amnemployee.getdownwardloads().doubleValue());
		pyVars.set("AMN_increasingloads", amnemployee.getincreasingloads().doubleValue());
		pyVars.set("AM_IncomeDate", dateFormat.format(amnemployee.getincomedate()));
		pyVars.set("AM_PaymentType", amnemployee.getpaymenttype());
		pyVars.set("AM_CivilStatus", amnemployee.getcivilstatus());
		pyVars.set("AM_Sex", amnemployee.getsex());
		pyVars.set("ORGSECTOR", amnemployee.getOrgSector());
		pyVars.set("AM_Spouse", amnemployee.getspouse());
		if (curr.getISO_Code() != null)
			pyVars.set("AM_Currency", curr.getISO_Code());
		else
			pyVars.set("AM_Currency", "VES");
		
		// Rounding Mode
		if (forceRounding)  
			roundingMode = amncontract.getStdPrecision();
		
		if (amnemployee.isStudying()) pyVars.set("AM_IsStudyng", "Y");
		else pyVars.set("AM_IsStudyng", "N");
		
		if (amnemployee.isPensioned()) pyVars.set("AM_IsPensioned", "Y");
		else pyVars.set("AM_IsPensioned", "N");
		
		if (amnemployee.isPensioned()) pyVars.set("AM_IsMedicated", "Y");
		else pyVars.set("AM_IsMedicated", "N");
		
		// Birth date
		pyVars.set("AM_BirthDate", dateFormat.format(amnemployee.getBirthday()));
		
		// Work Force (, Department Value, Location Value, Project Value,
		// Activity Value, Jobtitle Value, Jobstation Value , Shift Value
		MAMN_Concept_Types amnct = new MAMN_Concept_Types(p_ctx, AMN_Concept_Types_ID, null);
		MAMN_Jobtitle amnjt = new MAMN_Jobtitle(p_ctx, amnemployee.getAMN_Jobtitle_ID(), null);
		MAMN_Jobstation amnjs = new MAMN_Jobstation(p_ctx, amnemployee.getAMN_Jobstation_ID(), null);
		MAMN_Jobunit amnju = new MAMN_Jobunit(p_ctx, amnjs.getAMN_Jobunit_ID(), null);
		MAMN_Location amnlo = new MAMN_Location(p_ctx, amnemployee.getAMN_Location_ID(), null);
		MAMN_Department amnde = new MAMN_Department(p_ctx, amnemployee.getAMN_Department_ID(), null);
		MActivity act = new MActivity(p_ctx, amnemployee.getC_Activity_ID(), null);
		MProject proj = new MProject(p_ctx, amnemployee.getC_Project_ID(), null);
		pyVars.set("AM_Workforce", amnjt.getWorkforce());
		pyVars.set("AM_Department", amnde.getValue());
		pyVars.set("AM_Location", amnlo.getValue());
		pyVars.set("AM_Project", proj.getValue());
		pyVars.set("AM_Activity", act.getValue());
		pyVars.set("AM_Jobtitle", amnjt.getValue());
		pyVars.set("AM_Jobstation", amnjs.getValue());
		pyVars.set("AM_Jobunit", amnju.getValue());
		
		// AMN_Concept_Types_Limit
		int C_Currency_ID = amnpayroll.getC_Currency_ID();
		int C_Currency_ID_To = C_Currency_ID;
		if (amnpayroll.getC_Currency_ID_To() != 0)
			C_Currency_ID_To = amnpayroll.getC_Currency_ID_To();
		
		// COMMISSION CONCEPTS
		// AMN_Concept_Types_Limits CTLD
		pyVars.set("CTL_AmountAllocated", 0.00); 
		pyVars.set("CTL_QtyTimes", 1.00);
		pyVars.set("CTL_Rate", 0.0);
		pyVars.set("CTL_QtyReceipts", 0.00);
		
		Integer AMN_CommissionGroup_ID = amnemployee.getAMN_CommissionGroup_ID();
		Integer AMN_Concept_Types_Limit_ID = 0;
		// Verify if contains CTL_AmountAllocated
		if (AMN_CommissionGroup_ID != null && AMN_CommissionGroup_ID > 0) {
			// Commission Group
			MAMN_CommissionGroup amncomgru = new MAMN_CommissionGroup(p_ctx, AMN_CommissionGroup_ID, null);
			// Search AMN_Concept_Types_Limit_ID
			AMN_Concept_Types_Limit_ID = MAMN_Concept_Types_Limit.searchAMN_Concept_Types_Limit_ID_(AMN_Concept_Types_ID, amnpayroll.getAD_Client_ID(), 0, amnpayroll.getInvDateIni(), amnpayroll.getInvDateEnd(), amnpayroll.getC_Currency_ID());
			if (amncomgru != null && AMN_Concept_Types_Limit_ID != null && AMN_Concept_Types_Limit_ID > 0) {
				// CTL_AmountAllocated
				BigDecimal amountAllocated = MAMN_Concept_Types_Limit.getCTL_AmountAllocated(AMN_Concept_Types_Limit_ID, C_Currency_ID, C_Currency_ID_To, amnpayroll.getDateAcct(), amnpayroll.getC_ConversionType_ID(), amnpayroll.getAD_Client_ID(), amnct.getAD_Org_ID());
				if (amountAllocated != null)
					pyVars.set("CTL_AmountAllocated", amountAllocated.doubleValue());
				else
					log.warning("CTL_AmountAllocated es null para ID=" + AMN_Concept_Types_Limit_ID);
				// CTL_QtyTimes
				BigDecimal qtyTimes = MAMN_Concept_Types_Limit.getCTL_QtyTimes(C_Currency_ID, C_Currency_ID_To,  amnpayroll.getDateAcct(), amnpayroll.getC_ConversionType_ID(), amnpayroll.getAD_Client_ID(), amnct.getAD_Org_ID());
				if (qtyTimes != null)
					pyVars.set("CTL_QtyTimes", qtyTimes.doubleValue());
				else
					log.warning("CTL_QtyTimes es null para ID=" + AMN_Concept_Types_Limit_ID);
				// CTL_Rate
				if (amncomgru.getCommission() != null)
					pyVars.set("CTL_Rate", amncomgru.getCommission().doubleValue());
				else
					log.warning("CTL_Rate es null para ID=" + AMN_Concept_Types_Limit_ID);
				// CTL_QtyReceipts
				if (p_script.contains("CTL_QtyReceipts")) {
					Integer qtyReceipts = amncomgru.calculateCTL_QtyReceipts(amnpayroll.getAD_Client_ID(), amncomgru.getValue(), AMN_Concept_Types_ID, AMN_Concept_Types_Limit_ID);
					if (qtyReceipts != null)
						pyVars.set("CTL_QtyReceipts", qtyReceipts.doubleValue());
					else
						log.warning("CTL_QtyReceipts es null para ID=" + AMN_Concept_Types_Limit_ID);
				}
			}
		}
	}
	
	/**
	 * setPayrolllVariablesDays
	 * @param p_ctx
	 * @param pyVars
	 * @param p_AMN_Payroll_ID
	 */
	void setPayrolllVariablesDays(Properties p_ctx, PayrollVariables pyVars, int p_AMN_Payroll_ID) {
	    // AMN Payroll
	    MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
	    MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, amnpayroll.getAMN_Employee_ID(), null);
	    MAMN_Shift amnshift = new MAMN_Shift(p_ctx, amnemployee.getAMN_Shift_ID(), null);
	    MAMN_Contract amncontract = new MAMN_Contract(p_ctx, amnpayroll.getAMN_Contract_ID(), null);
	    boolean isSaturdayBusinessDay = amnshift.isSaturdayBusinessDay(amnshift.getAMN_Shift_ID());
	    
	    Timestamp AMNPayrollStartDate, AMNPayrollEndDate;
	    // SET FIXED VARIABLE NONLABORDAYS Double
	    AMNPayrollStartDate = amnpayroll.getInvDateIni();
	    AMNPayrollEndDate = amnpayroll.getInvDateEnd();
	    pyVars.set("HOLLIDAYS", MAMN_NonBusinessDay.sqlGetHolliDaysBetween(isSaturdayBusinessDay, AMNPayrollStartDate, AMNPayrollEndDate, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID()).doubleValue());
	    
	    // SET FIXED VARIABLES LABORDAYS,HOLLIDAYS,DTREC Double
	    pyVars.set("LABORDAYS", MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, AMNPayrollStartDate, AMNPayrollEndDate, amnpayroll.getAD_Client_ID(), null).doubleValue());
	    pyVars.set("DTREC", 1.00 + MAMN_NonBusinessDay.getDaysBetween(AMNPayrollStartDate, AMNPayrollEndDate).doubleValue());
	    pyVars.set("NONLABORDAYS", pyVars.get("DTREC") - pyVars.get("LABORDAYS"));
	    
	    // SET OTHER FIXED VARIABLES BUSINESSDAYS, NONBUSINESSDAYS, WEEKENDDAYS, NONWEEKENDDAYS;
	    pyVars.set("BUSINESSDAYS", MAMN_NonBusinessDay.sqlGetBusinessDaysBetween(isSaturdayBusinessDay, AMNPayrollStartDate, AMNPayrollEndDate, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID()).doubleValue());
	    pyVars.set("NONBUSINESSDAYS", MAMN_NonBusinessDay.sqlGetNonBusinessDayBetween(isSaturdayBusinessDay, AMNPayrollStartDate, AMNPayrollEndDate, amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID()).doubleValue());
	    pyVars.set("WEEKENDDAYS", MAMN_NonBusinessDay.sqlGetWeekEndDaysBetween(isSaturdayBusinessDay, AMNPayrollStartDate, AMNPayrollEndDate, amnpayroll.getAD_Client_ID(), null).doubleValue());
	    pyVars.set("NONWEEKENDDAYS", MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(isSaturdayBusinessDay, AMNPayrollStartDate, AMNPayrollEndDate, amnpayroll.getAD_Client_ID(), null).doubleValue());
	    
	    // SET FIXED VARIABLE DTPER (PayrollDays from Contract)
	    pyVars.set("DTPER", amncontract.getPayRollDays().doubleValue());
	    pyVars.set("DTOK", pyVars.get("DTREC")); // Alternative
	    
	    // SET FIXED VARIABLES NLUNES, NMONDAY
	    pyVars.set("NLUNES", AmerpDateUtils.getWeekDaysBetween(2, AMNPayrollStartDate, AMNPayrollEndDate).doubleValue());
	    pyVars.set("NMONDAY", AmerpDateUtils.getWeekDaysBetween(2, AMNPayrollStartDate, AMNPayrollEndDate).doubleValue());
	}

	

	/**
	 * setPayrolllVariablesSQLValues
	 * 
	 * @param p_ctx
	 * @param pyVars
	 * @param p_AMN_Payroll_ID
	 * @param p_currency
	 * @param p_conversiontype_id
	 * @return
	 */
	public String setPayrolllVariablesSQLValues(Properties p_ctx, PayrollVariables pyVars, int p_AMN_Payroll_ID,
			int p_currency, int p_conversiontype_id) {
		BigDecimal UNIDADTRIBUTARIA_BD = BigDecimal.valueOf(0.00);
		BigDecimal SBMIN_BD = BigDecimal.valueOf(0.00);
		BigDecimal TAXRATE_BD = BigDecimal.valueOf(0.00);
		Timestamp PayrollStartDate;
		int AMN_Employee_ID;
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		PayrollStartDate = amnpayroll.getInvDateIni();
		AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		String ErrorMess = "";

		// UNIDADTRIBUTARIA
		UNIDADTRIBUTARIA_BD = MAMN_Rates.sqlGetTaxUnit(PayrollStartDate, p_currency, p_conversiontype_id,
				amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
		if (UNIDADTRIBUTARIA_BD.doubleValue() == 0.00) {
			pyVars.set("UNIDADTRIBUTARIA", 0.00);
			ErrorMess = ErrorMess + " UNIDADTRIBUTARIA VARIABLE ERROR ..... \n";
		} else {
			pyVars.set("UNIDADTRIBUTARIA", UNIDADTRIBUTARIA_BD.doubleValue());
		}

		// SBMIN
		SBMIN_BD = MAMN_Rates.sqlGetSalary_MO(PayrollStartDate, p_currency, p_conversiontype_id,
				amnpayroll.getAD_Client_ID(), amnpayroll.getAD_Org_ID());
		if (SBMIN_BD.doubleValue() == 0.00) {
			pyVars.set("SBMIN", 0.00);
			ErrorMess = ErrorMess + " SBMIN VARIABLE ERROR ..... \n";
		} else {
			pyVars.set("SBMIN", SBMIN_BD.doubleValue());
		}

		// TAXRATE
		TAXRATE_BD = MAMN_Employee_Tax.getSQLEmployeeTaxRate(PayrollStartDate, p_currency, amnpayroll.getAD_Client_ID(),
				amnpayroll.getAD_Org_ID(), AMN_Employee_ID);
		if (TAXRATE_BD.doubleValue() == 0.00) {
			pyVars.set("TAXRATE", 0.00);
			ErrorMess = ErrorMess + " TAXRATE VARIABLE ERROR ..... \n";
		} else {
			pyVars.set("TAXRATE", TAXRATE_BD.doubleValue());
		}

		return ErrorMess;
	}

}

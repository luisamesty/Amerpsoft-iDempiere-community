package org.amerp.reports.xlsx.generator;

import org.amerp.reports.xlsx.constants.FinancialReportConstants;

//Clase auxiliar para seleccionar la estrategia correcta
public class ReportGeneratorFactory {
 public static IReportGenerator getGenerator(String reportTypeKey) {
	if (reportTypeKey == null) return null;

	switch (reportTypeKey) {
     	// Trial Balance
		case FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_ONE_PERIOD: 
			return new RPTTrialBalance();
     	// Trial Balance
		case FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_TWO_DATES: 
			return new RPTTrialBalance();
		// State Financial Balance
		case FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_BALANCE: 
			return new RPTStateFinancialBalance();
		// State Financial Integral Results
		case FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS: 
				return new RPTStateFinancialIntegralResults();
		// State Financial Integral Results
//		case FinancialReportConstants.REPORT_TYPE_STATE_FINANCIAL_BALANCE: 
//				return new RPTStateFinancialBalance();
		// Analitic Financial State
//		case FinancialReportConstants.REPORT_TYPE_ANALITIC_FINANCIAL_STATE: 
//				return new RPTAnaliticFinancialState();
		// Accounts Elements
		case FinancialReportConstants.REPORT_TYPE_ACCOUNT_ELEMENTS: 
			return new RPTAccountElements();
         default:
             return null;
     }
 }
}


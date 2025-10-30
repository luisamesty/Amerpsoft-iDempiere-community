package org.amerp.reports.xlsx.generator;

import org.amerp.reports.xlsx.constants.FinancialReportConstants;

//Clase auxiliar para seleccionar la estrategia correcta
public class ReportGeneratorFactory {
 public static IReportGenerator getGenerator(String reportTypeKey) {
     if (reportTypeKey == null) return null;

     switch (reportTypeKey) {
         case FinancialReportConstants.REPORT_TYPE_TRIAL_BALANCE_ONE_PERIOD: 
             return new TrialBalanceReportGenerator();
         // Agrega más casos a medida que creas más reportes
         case FinancialReportConstants.REPORT_TYPE_ACCOUNT_ELEMENTS: 
             return new AccountElementsReportGenerator();
         default:
             return null;
     }
 }
}

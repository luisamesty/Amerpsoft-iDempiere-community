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
package org.amerp.pp.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.compiere.util.CLogger;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// Import all your Process classes
import org.amerp.process.*;
import org.amerp.process.imp.*;

public class AMNProcessFactory implements IProcessFactory {

    private static final CLogger log = CLogger.getCLogger(AMNProcessFactory.class);
    private static final Map<String, Class<? extends ProcessCall>> processClasses = new HashMap<>();

    static {
        // Map the class names to the actual Class objects
        processClasses.put("org.amerp.process.AMNYearCreatePeriods", AMNYearCreatePeriods.class);
        processClasses.put("org.amerp.process.AMNYearCreatePeriodsFromPeriod", AMNYearCreatePeriodsFromPeriod.class);
        processClasses.put("org.amerp.process.AMNPayrollCreatePeriodAssistPeriods", AMNPayrollCreatePeriodAssistPeriods.class);
        // ... add all other classes here
        processClasses.put("org.amerp.process.AMNPayrollRefresh", AMNPayrollRefresh.class);
        processClasses.put("org.amerp.process.AMNPayrollRefreshOnePeriod", AMNPayrollRefreshOnePeriod.class);
        processClasses.put("org.amerp.process.AMNPayrollCreate", AMNPayrollCreateOnePeriod.class);
        processClasses.put("org.amerp.process.AMNPayrollCreateOneLotHeaders", AMNPayrollCreateOneLotHeaders.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessCompleteDocs", AMNPayrollProcessCompleteDocs.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessCompleteOneDoc", AMNPayrollProcessCompleteOneDoc.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessReactivateDocs", AMNPayrollProcessReactivateDocs.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessReactivateOneDoc", AMNPayrollProcessReactivateOneDoc.class);
        processClasses.put("org.amerp.process.AMNPayrollCreateOneDoc", AMNPayrollCreateOneDoc.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessPayrollAssistOneEmployee", AMNPayrollProcessPayrollAssistOneEmployee.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessPayrollAssistOnePeriod", AMNPayrollProcessPayrollAssistOnePeriod.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessPayrollAssistOneAttendanceDay", AMNPayrollProcessPayrollAssistOneAttendanceDay.class);
        processClasses.put("org.amerp.process.AMNPayrollTransferPayrollAssistOneEmployee", AMNPayrollTransferPayrollAssistOneEmployee.class);
        processClasses.put("org.amerp.process.AMNPayrollTransferPayrollAssistOnePeriod", AMNPayrollTransferPayrollAssistOnePeriod.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessPayrollDeferredOneEmployee", AMNPayrollProcessPayrollDeferredOneEmployee.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessHistoricSalaryOneDoc", AMNPayrollProcessHistoricSalaryOneDoc.class);
        processClasses.put("org.amerp.process.AMNPayrollProcessHistoricSalaryOnePeriod", AMNPayrollProcessHistoricSalaryOnePeriod.class);
        processClasses.put("org.amerp.process.AMNConceptTypesCopyAccounts", AMNConceptTypesCopyAccounts.class);
        processClasses.put("org.amerp.process.AMNConceptTypesAcctCopyAccounts", AMNConceptTypesAcctCopyAccounts.class);
        processClasses.put("org.amerp.process.AMNConceptTypesChargeCopyCharges", AMNConceptTypesChargeCopyCharges.class);
        processClasses.put("org.amerp.process.AMNPayrollProvision", AMNPayrollProvision.class);
        processClasses.put("org.amerp.process.AMNPayrollReconversion", AMNPayrollReconversion.class);
        processClasses.put("org.amerp.process.AMNYearCreateSchedule", AMNYearCreateSchedule.class);
        processClasses.put("org.amerp.process.AMNPayrollRefreshOnePeriodConcept", AMNPayrollRefreshOnePeriodConcept.class);
        processClasses.put("org.amerp.process.AMNPayrollDeleteOnePeriod", AMNPayrollDeleteOnePeriod.class);
        processClasses.put("org.amerp.process.AMNPayrollDeleteOneDoc", AMNPayrollDeleteOneDoc.class);
        processClasses.put("org.amerp.process.AMNPayrolLeavesProcess", AMNPayrolLeavesProcess.class);
        processClasses.put("org.amerp.process.AMNPayrolLeavesCreatesFromPayroll", AMNPayrolLeavesCreatesFromPayroll.class);
        processClasses.put("org.amerp.process.AMNPayrollRefreshHistoric", AMNPayrollRefreshHistoric.class);
        processClasses.put("org.amerp.process.SetClientAccountingMode", SetClientAccountingMode.class);
        processClasses.put("org.amerp.process.imp.AMNImportDeleteRecords", AMNImportDeleteRecords.class);
        processClasses.put("org.amerp.process.imp.AMNImportSalarySocialBenefits", AMNImportSalarySocialBenefits.class);
        processClasses.put("org.amerp.process.imp.AMNImportEmployee", AMNImportEmployee.class);
        processClasses.put("org.amerp.process.imp.AMNImportPayrollAssistRow", AMNImportPayrollAssistRow.class);
    }

    @Override
    public ProcessCall newProcessInstance(String p_className) {
        Class<? extends ProcessCall> clazz = processClasses.get(p_className);
        if (clazz == null) {
            return null;
        }

        try {
            Constructor<? extends ProcessCall> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            // Log the error for better debugging
            log.warning("Could not instantiate process class " + p_className + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
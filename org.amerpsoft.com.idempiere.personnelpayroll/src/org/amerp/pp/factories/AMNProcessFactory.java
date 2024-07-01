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
import org.amerp.process.*;
import org.amerp.process.imp.AMNImportDeleteRecords;
import org.amerp.process.imp.AMNImportSalarySocialBenefits;
import org.compiere.process.ProcessCall;
import org.compiere.util.CLogger;

/**
 * @author luisamesty
 *
 */
public class AMNProcessFactory implements IProcessFactory {
	
	static CLogger log = CLogger.getCLogger(AMNProcessFactory.class);
	
	/* (non-Javadoc)
	 * @see org.adempiere.base.IProcessFactory#newProcessInstance(java.lang.String)
	 */
    @Override
    public ProcessCall newProcessInstance(String p_className) {
        // **************************
        // Operation Processes 
        // **************************
    	ProcessCall process = null;
    	//log.warning(".......................................");
    	if (p_className.equals("org.amerp.process.AMNYearCreatePeriods"))
    		try {
				process =   AMNYearCreatePeriods.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNYearCreatePeriods();
    	if (p_className.equals("org.amerp.process.AMNYearCreatePeriodsFromPeriod"))
    		try {
				process =   AMNYearCreatePeriodsFromPeriod.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNYearCreatePeriodsFromPeriod();
    	if (p_className.equals("org.amerp.process.AMNPayrollCreatePeriodAssistPeriods"))
    		try {
				process =   AMNPayrollCreatePeriodAssistPeriods.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollCreatePeriodAssistPeriods();
    	if (p_className.equals("org.amerp.process.AMNPayrollRefresh"))
    		try {
				process =   AMNPayrollRefresh.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollRefresh();
    	if (p_className.equals("org.amerp.process.AMNPayrollRefreshOnePeriod"))
    		try {
				process =   AMNPayrollRefreshOnePeriod.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollRefreshOnePeriod();   	
    	if (p_className.equals("org.amerp.process.AMNPayrollCreate"))
    		try {
				process =   AMNPayrollCreateOnePeriod.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollCreate();
    	if (p_className.equals("org.amerp.process.AMNPayrollCreateOneLotHeaders"))
    		try {
				process =   AMNPayrollCreateOneLotHeaders.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollCreateOneLotHeaders();
    	if (p_className.equals("org.amerp.process.AMNPayrollProcessCompleteDocs"))
    		try {
				process =   AMNPayrollProcessCompleteDocs.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessCompleteDocs();
    	if (p_className.equals("org.amerp.process.AMNPayrollProcessCompleteOneDoc"))	 
    		try {
				process =   AMNPayrollProcessCompleteOneDoc.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessCompleteOneDoc();
    	if (p_className.equals("org.amerp.process.AMNPayrollProcessReactivateDocs"))	 
    		try {
				process =   AMNPayrollProcessReactivateDocs.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessReactivateDocs();
    	if (p_className.equals("org.amerp.process.AMNPayrollProcessReactivateOneDoc"))	 
    		try {
				process =   AMNPayrollProcessReactivateOneDoc.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessReactivateOneDoc();
       	if (p_className.equals("org.amerp.process.AMNPayrollCreateOneDoc"))
    		try {
				process =   AMNPayrollCreateOneDoc.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollCreate();
      	if (p_className.equals("org.amerp.process.AMNPayrollProcessPayrollAssistOneEmployee"))
    		try {
				process =   AMNPayrollProcessPayrollAssistOneEmployee.class.newInstance();
			} catch (Exception e) {}
    		//return new PayrollProcessPayrollAssistOneEmployee();
      	if (p_className.equals("org.amerp.process.AMNPayrollProcessPayrollAssistOnePeriod"))
    		try {
				process =   AMNPayrollProcessPayrollAssistOnePeriod.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessPayrollAssistOnePeriod();
      	if (p_className.equals("org.amerp.process.AMNPayrollProcessPayrollAssistOneAttendanceDay"))
    		try {
				process =   AMNPayrollProcessPayrollAssistOneAttendanceDay.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessPayrollAssistOneAttendanceDay();
      	if (p_className.equals("org.amerp.process.AMNPayrollTransferPayrollAssistOneEmployee"))
    		try {
				process =   AMNPayrollTransferPayrollAssistOneEmployee.class.newInstance();
			} catch (Exception e) {}
    		//return new PayrollProcessPayrollAssistOneEmployee();
      	if (p_className.equals("org.amerp.process.AMNPayrollTransferPayrollAssistOnePeriod"))
    		try {
				process =   AMNPayrollTransferPayrollAssistOnePeriod.class.newInstance();
			} catch (Exception e) {}
    		//return new AMNPayrollProcessPayrollAssistOnePeriod(); 
      	if (p_className.equals("org.amerp.process.AMNPayrollProcessPayrollDeferredOneEmployee")) {
//log.warning("AmperProcPayrollProcessPayrollDeferredOE........PAso");
      		try {
				process =   AMNPayrollProcessPayrollDeferredOneEmployee.class.newInstance();
			} catch (Exception e) {}
      	}
    		//return new AMNPayrollProcessPayrollDeferredOneEmployee();
        if (p_className.equals("org.amerp.process.AMNPayrollProcessHistoricSalaryOneDoc")) {
//log.warning("AMNPayrollProcessHistoricSalaryOneDoc........PAso");
          	try {
    			process =   AMNPayrollProcessHistoricSalaryOneDoc.class.newInstance();
    		} catch (Exception e) {}
        }
        		//return new AMNPayrollProcessHistoricSalaryOneDoc();
        if (p_className.equals("org.amerp.process.AMNPayrollProcessHistoricSalaryOnePeriod")) 
          	//log.warning("AMNPayrollProcessHistoricSalaryOnePeriod........PAso");
          	try {
    			process =   AMNPayrollProcessHistoricSalaryOnePeriod.class.newInstance();
    		} catch (Exception e) {}
        		//return new AMNPayrollProcessHistoricSalaryOnePeriod();     	
        // return AMNConceptTypesCopyAccounts Deprecated 
        if (p_className.equals("org.amerp.process.AMNConceptTypesCopyAccounts")) 
          	//log.warning("AMNConceptTypesCopyAccounts........PAso");
          	try {
    			process =   AMNConceptTypesCopyAccounts.class.newInstance();
    		} catch (Exception e) {}
        		//return new AMNConceptTypesCopyAccounts();     	
        // return AMNConceptTypesAcctCopyAccounts
        if (p_className.equals("org.amerp.process.AMNConceptTypesAcctCopyAccounts")) 
          	//log.warning("AMNConceptTypesAcctCopyAccounts........PAso");
          	try {
    			process =   AMNConceptTypesAcctCopyAccounts.class.newInstance();
    		} catch (Exception e) {}
        		//return new AMNConceptTypesCopyAccounts();   
        // return AMNConceptTypesChargeCopyCharges
        if (p_className.equals("org.amerp.process.AMNConceptTypesChargeCopyCharges")) 
          	//log.warning("AMNConceptTypesChargeCopyCharges........PAso");
          	try {
    			process =   AMNConceptTypesChargeCopyCharges.class.newInstance();
    		} catch (Exception e) {}
        		//return new AMNConceptTypesChargeCopyCharges();    
        //AMNPayrollProvision
        if (p_className.equals("org.amerp.process.AMNPayrollProvision")) 
          	//log.warning("AMNPayrollProvision........PAso");
          	try {
    			process =   AMNPayrollProvision.class.newInstance();
    		} catch (Exception e) {}
        // AMNPayrollReconversion
        if (p_className.equals("org.amerp.process.AMNPayrollReconversion")) 
          	//log.warning("AMNPayrollReconversion........PAso");
          	try {
    			process =   AMNPayrollReconversion.class.newInstance();
    		} catch (Exception e) {}
        // AMNYearCreateSchedule
        if (p_className.equals("org.amerp.process.AMNYearCreateSchedule"))
    		try {
				process =   AMNYearCreateSchedule.class.newInstance();
			} catch (Exception e) {}
        // **************************
        // Import Processes 
        // **************************
        // AMNImportDeleteRecords
        if (p_className.equals("org.amerp.process.imp.AMNImportDeleteRecords")) 
          	//log.warning("AMNImportDeleteRecords........PAso");
          	try {
    			process =   AMNImportDeleteRecords.class.newInstance();
    		} catch (Exception e) {}
        // AMNImportSalarySocialBenefits
        if (p_className.equals("org.amerp.process.imp.AMNImportSalarySocialBenefits")) 
          	//log.warning("AMNImportSalarySocialBenefits........PAso");
          	try {
    			process =   AMNImportSalarySocialBenefits.class.newInstance();
    		} catch (Exception e) {}
          return process;
    }
}

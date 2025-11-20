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
package org.amerp.fin.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;


/**
 * @author luisamesty
 *
 */
public class AMFProcessfactory implements IProcessFactory{

	/* (non-Javadoc)
	 * @see org.adempiere.base.IProcessFactory#newProcessInstance(java.lang.String)
	 */
    @Override
    public ProcessCall newProcessInstance(String p_className) {
	    // TODO Auto-generated method stub
    	ProcessCall process = null;
		// AMFFactAcctReset;
    	if(p_className.equals("org.amerp.process.AMFFactAcctReset"))
    		return new org.amerp.process.AMFFactAcctReset();
    	// AMFClientAcctProcessor
    	if(p_className.equals("org.amerp.process.AMFClientAcctProcessor"))
    		return new org.amerp.process.AMFClientAcctProcessor();
    	// AMFGLJournalAnnualClosing
    	if(p_className.equals("org.amerp.process.AMFGLJournalAnnualClosing"))
    		return new org.amerp.process.AMFGLJournalAnnualClosing();
    	// AMFAccountingClientSetup
    	if(p_className.equals("org.amerp.process.AMFAccountingClientSetup"))
    		return new org.amerp.process.AMFAccountingClientSetup();
    	// AMFCurrencyCoversionRateCopy
    	if(p_className.equals("org.amerp.process.AMFCurrencyConversionRateCopy"))
    		return new org.amerp.process.AMFCurrencyConversionRateCopy();
    	// AMFDocumentAcctProcessor
    	if(p_className.equals("org.amerp.process.AMFDocumentAcctProcessor"))
    		return new org.amerp.process.AMFDocumentAcctProcessor();
    	// AMFRebuildANewClientSchema
    	if(p_className.equals("org.amerp.process.AMFRebuildANewClientSchema"))
    		return new org.amerp.process.AMFRebuildANewClientSchema();
    	// REPORTS JASPER
    	// AccountElements_Tree 
        if (p_className.equals("org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Std"))
            return new org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Std();
        if (p_className.equals("org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Pojo"))
            return new org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree_Pojo();
        // 
        if (p_className.equals("org.amerp.reports.xlsx.AccountElements_Tree_ProcXlsx"))
            return new org.amerp.reports.xlsx.AccountElements_Tree_ProcXlsx();
        return process;
    }
}

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
import org.amerp.process.AMFAccountingClientSetup;
import org.amerp.process.AMFClientAcctProcessor;
import org.amerp.process.AMFCurrencyConversionRateCopy;
import org.amerp.process.AMFDocumentAcctProcessor;
import org.amerp.process.AMFFactAcctReset;
import org.amerp.process.AMFGLJournalAnnualClosing;
import org.amerp.process.AMFRebuildANewClientSchema;
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
    		try {
    			process =   (ProcessCall) AMFFactAcctReset.class.newInstance();
    		} catch (Exception e) {}
    	// AMFClientAcctProcessor
    	if(p_className.equals("org.amerp.process.AMFClientAcctProcessor"))
    		try {
    			process =   (ProcessCall) AMFClientAcctProcessor.class.newInstance();
    		} catch (Exception e) {}
    	// AMFGLJournalAnnualClosing
    	if(p_className.equals("org.amerp.process.AMFGLJournalAnnualClosing"))
    		try {
    			process =   (ProcessCall) AMFGLJournalAnnualClosing.class.newInstance();
    		} catch (Exception e) {}
    	// AMFAccountingClientSetup
    	if(p_className.equals("org.amerp.process.AMFAccountingClientSetup"))
    		try {
    			process =   (ProcessCall) AMFAccountingClientSetup.class.newInstance();
    		} catch (Exception e) {}
    	// AMFCurrencyCoversionRateCopy
    	if(p_className.equals("org.amerp.process.AMFCurrencyConversionRateCopy"))
    		try {
    			process =   (ProcessCall) AMFCurrencyConversionRateCopy.class.newInstance();
    		} catch (Exception e) {}
    	// AMFDocumentAcctProcessor
    	if(p_className.equals("org.amerp.process.AMFDocumentAcctProcessor"))
    		try {
    			process =   (ProcessCall) AMFDocumentAcctProcessor.class.newInstance();
    		} catch (Exception e) {}
    	// AMFRebuildANewClientSchema
    	if(p_className.equals("org.amerp.process.AMFRebuildANewClientSchema"))
//    		try {
//    			process =   (ProcessCall) AMFRebuildANewClientSchema.class.newInstance();
//    		} catch (Exception e) {}
    		return new org.amerp.process.AMFRebuildANewClientSchema();
    	// REPORTS 
    	// AccountElements_Tree 
        if (p_className.equals("org.amerp.reports.AccountElements_Tree.AccountElements_Tree"))
            return new org.amerp.reports.AccountElements_Tree.AccountElements_Tree();
    	return process;
    }
}

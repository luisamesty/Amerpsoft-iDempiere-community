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
package org.amerp.amnevents;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;


/**
 * @author luisamesty
 *
 */
public class AMNPayrollEvent extends AbstractEventHandler{

	/* (non-Javadoc)
	 * @see org.adempiere.base.event.AbstractEventHandler#doHandleEvent(org.adempiere.base.event.Event)
	 */
    CLogger log =  CLogger.getCLogger(AMNPayrollEvent.class);
    
	@Override
    protected void doHandleEvent(Event p_event) {
	    // TODO Auto-generated method stub
	    PO po = getPO(p_event);
	    // MAMNPayroll
	    if (po instanceof MAMN_Payroll) {

	    }
	    // MAMNPayroll_Detail
	    if (po instanceof MAMN_Payroll_Detail) {
	    	MAMN_Payroll_Detail prd = (MAMN_Payroll_Detail)po;
	    	prd.getAMN_Payroll_ID();
	   	}
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.event.AbstractEventHandler#initialize()
	 */
    @Override
    protected void initialize() {
	    // MAMN_Payroll
	    registerTableEvent(IEventTopics.PO_AFTER_CHANGE, MAMN_Payroll.Table_Name);
	    registerTableEvent(IEventTopics.PO_AFTER_NEW , MAMN_Payroll.Table_Name);
	    // MAMN_Payroll_Detail
	    registerTableEvent(IEventTopics.PO_AFTER_CHANGE, MAMN_Payroll_Detail.Table_Name);
	    registerTableEvent(IEventTopics.PO_AFTER_NEW , MAMN_Payroll_Detail.Table_Name);
    }

}

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
package org.amerp.amxeditor.factory;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.amerp.amxeditor.model.MLocationExt;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;


/**
 * @author luisamesty
 *
 */
public class EventFactory extends AbstractEventHandler{

	/* (non-Javadoc)
	 * @see org.adempiere.base.event.AbstractEventHandler#doHandleEvent(org.osgi.service.event.Event)
	 */
    CLogger log =  CLogger.getCLogger(EventFactory.class);
    
    @Override
    protected void doHandleEvent(Event p_event) {
	    PO po = getPO(p_event);
	    
	    // MAMNPayroll
	    if (po instanceof MLocationExt) {
	    	MLocationExt pr = (MLocationExt)po;
	    }    
    }

	/* (non-Javadoc)
	 * @see org.adempiere.base.event.AbstractEventHandler#initialize()
	 */
    @Override
    protected void initialize() {
	    // MLocationExt
	    registerTableEvent(IEventTopics.PO_AFTER_CHANGE, MLocationExt.Table_Name);
	    registerTableEvent(IEventTopics.PO_AFTER_NEW , MLocationExt.Table_Name);
	    registerTableEvent(IEventTopics.PO_BEFORE_NEW , MLocationExt.Table_Name);
    }

}

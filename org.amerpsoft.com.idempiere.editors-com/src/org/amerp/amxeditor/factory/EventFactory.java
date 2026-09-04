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
import org.amerp.amxeditor.service.NominatimService;
import org.amerp.amxeditor.service.NominatimService.GeocodingResult;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Util;
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
	    MLocationExt loc = null;
	    
	    // MLocationExt
	    if (po instanceof MLocationExt) {
	    	loc = (MLocationExt)po;
	    }    
	    
		if (loc != null && 	p_event.getTopic().equals(IEventTopics.PO_BEFORE_NEW) 
				|| (loc.is_ValueChanged(MLocationExt.COLUMNNAME_Address1) 
						|| loc.is_ValueChanged(MLocationExt.COLUMNNAME_Address2)
						|| loc.is_ValueChanged(MLocationExt.COLUMNNAME_Address3) 
						|| loc.is_ValueChanged(MLocationExt.COLUMNNAME_Address4)
						|| loc.is_ValueChanged(MLocationExt.COLUMNNAME_Postal) 
						|| loc.is_ValueChanged(MLocationExt.COLUMNNAME_City)
						)
				)
		 {

			String fullAddress = loc.toString();
	        Integer clientObj = (Integer) loc.getAD_Client_ID();
	        int AD_Client_ID = (clientObj != null) ? clientObj : 0;
            log.warning("EVENT C_Location_ID=" + loc.getC_Location_ID() + " (" + loc.getGeocodingStatus() + ") \r\n" +
            		"latitude="+loc.getLatitude() +"  longittude="+loc.getLongitude()+"\r\n");	
	        if (!Util.isEmpty(fullAddress) && !loc.getGeocodingStatus().equals(MLocationExt.GEOCODING_STATUS_OK)) {
		        NominatimService service = new NominatimService();
		        GeocodingResult result = service.geocode(fullAddress.toString(), AD_Client_ID);
		        // Si cuentas con campos personalizados de Lat/Long en MLocationExt:
		        if (MLocationExt.GEOCODING_STATUS_OK.equals(result.geocodingResultStatus())) {
		            loc.set_ValueOfColumn("Latitude", result.latitude());
		            loc.set_ValueOfColumn("Longitude", result.longitude());
		            loc.set_ValueOfColumn("GeocodingStatus", result.geocodingResultStatus());
		        } else {
		            loc.set_ValueOfColumn("Latitude", null);
		            loc.set_ValueOfColumn("Longitude", null);
		            loc.set_ValueOfColumn("GeocodingStatus", result.geocodingResultStatus());
		        }
		        loc.saveEx();
	        }
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

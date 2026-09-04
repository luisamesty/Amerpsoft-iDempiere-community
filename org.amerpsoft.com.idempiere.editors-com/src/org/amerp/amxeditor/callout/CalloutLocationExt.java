package org.amerp.amxeditor.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.adempiere.webui.apps.AEnv;
import org.amerp.amxeditor.model.MBPartnerLocationExt;
import org.amerp.amxeditor.model.MLocationExt;
import org.amerp.amxeditor.service.LocationAddressUtil;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MQuery;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Executions;

public class CalloutLocationExt implements IColumnCallout {

    private static final CLogger log = CLogger.getCLogger(CalloutLocationExt.class);
    private static final int C_LOCATION_WINDOW_ID = 121; // ID de la Ventana C_Location en iDempiere

    @Override
    public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
        if (value == null || value.equals(oldValue)) {
            return "";
        }

        String tableName = mTab.getTableName();
        String columnName = mField.getColumnName();
        LocationAddressUtil.LocationData locData = LocationAddressUtil.getLocationDataFromTab(ctx, mTab, mField, value);
        String fullAddress = "";
        
        // =========================================================================
        // CASO 1: Pestaña nativa de la ubicación (C_Location / MLocationExt)
        // =========================================================================
        if (MLocationExt.Table_Name.equalsIgnoreCase(tableName)) {
            if (columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address1)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address2)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address3)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address4)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_City)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_C_Region_ID)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_C_Parish_ID)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_C_Municipality_ID)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Postal))
            {
	            // 1. Extraer dirección
	            locData = LocationAddressUtil.getLocationDataFromTab(ctx, mTab, mField, value);
	            fullAddress = LocationAddressUtil.buildFullAddress(locData, ",");
	            // Future USE
        	}            
	        // Subcaso 2.2: Acción sobre el botón / campo OpenGeoMap (Zoom)
	        if (MLocationExt.COLUMNNAME_OpenGeoMap.equalsIgnoreCase(columnName)) {

	            // Obtener dirección del registro C_Location
				locData = LocationAddressUtil.getLocationDataFromTab(ctx, mTab, mField, value);
	            fullAddress = LocationAddressUtil.buildFullAddress(locData, ",");
	    		String urlString = MSysConfig.getValue("LOCATION_MAPS_URL_PREFIX") + fullAddress;
				String message = null;

				try {
					Executions.getCurrent().sendRedirect(urlString, "_blank");
				}
				catch (Exception e) {
					message = e.getMessage();
					log.warning(Msg.translate(ctx,"URLnotValid")+ message);
				}
                log.warning("Table="+tableName+"  Column="+columnName);	
	        }
	        // Subcaso 2.3: GeocodingStatus
	        if (MLocationExt.COLUMNNAME_GeocodingStatus.equalsIgnoreCase(columnName)
	        		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Latitude)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Longitude)) {
	            // Future USE
	        }
            return "";
        }

        // ================================================================================
        // CASO 2: Otras Pestañas secundarias (ej: C_BPartner_Location, M_Warehouse, etc.)
        // ================================================================================
        if (!MLocationExt.Table_Name.equalsIgnoreCase(tableName)) {
        	Integer locObj = (Integer) mTab.getValue("C_Location_ID");
            int locationID = (locObj != null) ? locObj : 0;
	        // Subcaso 2.1: Cambio en la Foreign Key de C_Location_ID
	        if (MBPartnerLocationExt.COLUMNNAME_C_Location_ID.equalsIgnoreCase(columnName)) {
	            // Obtener dirección del registro C_Location
                MLocationExt loc = new MLocationExt(ctx, locationID, null);
                log.warning("Table="+tableName+" C_Location_ID=" + locationID + " (" + loc.getGeocodingStatus() + ") \r\n" +
                		"latitude="+loc.getLatitude() +"  longitude="+loc.getLongitude()+"\r\n");	
  	        } 
	        // Subcaso 2.2: Acción sobre el botón / campo OpenLocation (Zoom)
	        if (MBPartnerLocationExt.COLUMNNAME_OpenLocation.equalsIgnoreCase(columnName)) {
	            if (locationID > 0) {
	                MQuery query = new MQuery("C_Location");
	                query.addRestriction("C_Location_ID", MQuery.EQUAL, locationID);
	                AEnv.zoom(C_LOCATION_WINDOW_ID, query);
	            }
	        }
        }
        
        return "";
    }
}
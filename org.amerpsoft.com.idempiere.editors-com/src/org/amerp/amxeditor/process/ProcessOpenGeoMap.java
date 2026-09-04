package org.amerp.amxeditor.process;

import org.amerp.amxeditor.service.LocationAddressUtil;
import org.compiere.model.MSysConfig;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Executions;

/**
 * Proceso para abrir la ventana Geolocalizacion
 * Google Maps por ejemplo
 */
public class ProcessOpenGeoMap extends SvrProcess {

    private int p_C_Location_ID = 0;
    
    @Override
    protected void prepare() {
        p_C_Location_ID = getRecord_ID();
    }

    @Override
    protected String doIt() throws Exception {

        LocationAddressUtil.LocationData locData = LocationAddressUtil.getLocationDataByID(getCtx(), p_C_Location_ID, get_TrxName());
        String fullAddress = "";
        // Obtener C_Location
		locData = LocationAddressUtil.getLocationDataByID(getCtx(), p_C_Location_ID, null);
        fullAddress = LocationAddressUtil.buildFullAddress(locData, ",");
		String urlString = MSysConfig.getValue("LOCATION_MAPS_URL_PREFIX") + fullAddress;
		String message = null;

		try {
			Executions.getCurrent().sendRedirect(urlString, "_blank");
		}
		catch (Exception e) {
			message = e.getMessage();
			log.warning(Msg.translate(getCtx(),"URLnotValid")+ message);
		}

        return "@Success@";
    }
}
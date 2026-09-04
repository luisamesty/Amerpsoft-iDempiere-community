package org.amerp.amxeditor.process;

import org.amerp.amxeditor.model.MLocationExt;
import org.amerp.amxeditor.service.LocationAddressUtil;
import org.amerp.amxeditor.service.NominatimService;
import org.amerp.amxeditor.service.NominatimService.GeocodingResult;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.SvrProcess;
import org.compiere.util.Msg;
import org.compiere.util.Util;

public class ProcessSingleLocationGeocoding extends SvrProcess {

    private int p_C_Location_ID = 0;

    @Override
    protected void prepare() {
        // Si el proceso es invocado desde un boton en la ventana C_Location,
        // getRecord_ID() devuelve directamente el ID de la localizacion seleccionada.
        p_C_Location_ID = getRecord_ID();
    }

    @Override
    protected String doIt(){
    	String retMess ="";
        if (p_C_Location_ID <= 0) {
            throw new AdempiereException("Debe seleccionar un registro de Localización válido.");
        }

        MLocationExt location = new MLocationExt(getCtx(), p_C_Location_ID, get_TrxName());
        // Obtener dirección del registro C_Location
        LocationAddressUtil.LocationData locData = LocationAddressUtil.getLocationDataByID(getCtx(), p_C_Location_ID, null);
        String fullAddress = LocationAddressUtil.buildFullAddress(locData, ",");
        addLog(Msg.translate(getCtx(), "Address"));
        addLog(fullAddress);
        if (Util.isEmpty(fullAddress)) {
        	retMess = "La localización no posee campos de dirección/ciudad configurados.";
        	return null;
        }
        if (!locData.geocodingStatus().equals(MLocationExt.GEOCODING_STATUS_OK)) {
            NominatimService service = new NominatimService();
            GeocodingResult result = service.geocode(fullAddress.toString(), location.getAD_Client_ID());

            // Actualizar la localizacion
            location.set_ValueOfColumn("GeocodingStatus", result.geocodingResultStatus());

            if (MLocationExt.GEOCODING_STATUS_OK.equals(result.geocodingResultStatus())) {
                location.set_ValueOfColumn("Latitude", result.latitude());
                location.set_ValueOfColumn("Longitude", result.longitude());
                location.saveEx();
                
                retMess = "Geocodificación exitosa: "+"\r\n"
                		+"Lat (" + result.latitude() + ") " +"\r\n"
                		+"Lon (" + result.longitude() + ")";
            } else {
                location.set_ValueOfColumn("Latitude", null);
                location.set_ValueOfColumn("Longitude", null);
                location.saveEx();
                
            	retMess =  "No se encontraron coordenadas para la dirección especificada"+"\r\n"+
            			" (Estado: " + result.geocodingResultStatus() + ").";
            }
        } else {
        	retMess =  "Las Coordenadas de dirección especificada NO SE HAN MODIFICADO"+"\r\n"+
        			" (Estado: " + MLocationExt.GEOCODING_STATUS_OK+ ").";
        }
        addLog(retMess);
		return null;

    }
}
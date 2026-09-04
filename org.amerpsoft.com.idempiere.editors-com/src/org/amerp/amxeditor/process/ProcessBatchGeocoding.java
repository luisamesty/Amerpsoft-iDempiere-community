package org.amerp.amxeditor.process;

import org.amerp.amxeditor.model.MLocationExt;
import org.amerp.amxeditor.service.LocationAddressUtil;
import org.amerp.amxeditor.service.NominatimService;
import org.amerp.amxeditor.service.NominatimService.GeocodingResult;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Util;

public class ProcessBatchGeocoding extends SvrProcess {

	private List<Integer> m_selectionKeys = new ArrayList<>();

    @Override
    protected void prepare() {
        // 1. Intentar obtener IDs seleccionados desde T_Selection usando el AD_PInstance_ID
        String sql = "SELECT T_Selection_ID FROM T_Selection WHERE AD_PInstance_ID = ?";
        int[] selectedIDs = DB.getIDsEx(get_TrxName(), sql, getAD_PInstance_ID());

        if (selectedIDs != null && selectedIDs.length > 0) {
            for (int id : selectedIDs) {
                m_selectionKeys.add(id);
            }
        } 
        // 2. Si no hay selección múltiple en T_Selection, tomar el registro activo
        else if (getRecord_ID() > 0) {
            m_selectionKeys.add(getRecord_ID());
        }
    }

    @Override
    protected String doIt() throws Exception {
        if (m_selectionKeys.isEmpty()) {
            throw new AdempiereException("Debe seleccionar al menos un registro en la grilla.");
        }

        NominatimService service = new NominatimService();
        StringBuilder summary = new StringBuilder();
        
        int successCount = 0;
        int errorCount = 0;
        int skippedCount = 0;

        for (int cLocationID : m_selectionKeys) {
            if (cLocationID <= 0) continue;

            MLocationExt location = new MLocationExt(getCtx(), cLocationID, get_TrxName());
            LocationAddressUtil.LocationData locData = LocationAddressUtil.getLocationDataByID(getCtx(), cLocationID, get_TrxName());
            String fullAddress = LocationAddressUtil.buildFullAddress(locData, ", ");

            StringBuilder itemLog = new StringBuilder();
            itemLog.append("----------------------------------------\r\n");
            itemLog.append("ID Localización: ").append(cLocationID).append("\r\n");
            itemLog.append("Dirección: ").append(Util.isEmpty(fullAddress) ? "[Sin Dirección]" : fullAddress).append("\r\n");

            if (Util.isEmpty(fullAddress)) {
                itemLog.append("Estado: OMITE (Dirección vacía)\r\n");
                skippedCount++;
                addLog(itemLog.toString());
                summary.append(itemLog);
                continue;
            }

            // Procesar si no está en estado OK previo
            if (!MLocationExt.GEOCODING_STATUS_OK.equals(locData.geocodingStatus())) {
                GeocodingResult result = service.geocode(fullAddress, location.getAD_Client_ID());

                location.set_ValueOfColumn("GeocodingStatus", result.geocodingResultStatus());

                if (MLocationExt.GEOCODING_STATUS_OK.equals(result.geocodingResultStatus())) {
                    location.set_ValueOfColumn("Latitude", result.latitude());
                    location.set_ValueOfColumn("Longitude", result.longitude());
                    location.saveEx();

                    itemLog.append("Estado Geolocalización: ").append(result.geocodingResultStatus()).append("\r\n");
                    itemLog.append("Coordenadas: Lat (").append(result.latitude()).append("), Lon (").append(result.longitude()).append(")\r\n");
                    successCount++;
                } else {
                    location.set_ValueOfColumn("Latitude", null);
                    location.set_ValueOfColumn("Longitude", null);
                    location.saveEx();

                    itemLog.append("Estado Geolocalización: ").append(result.geocodingResultStatus()).append("\r\n");
                    itemLog.append("Coordenadas: [No encontradas]\n");
                    errorCount++;
                }
            } else {
                itemLog.append("Estado Geolocalización: ").append(MLocationExt.GEOCODING_STATUS_OK).append(" (No modificadas)\n");
                itemLog.append("Coordenadas: Lat (").append(locData.latitude()).append("), Lon (").append(locData.longitude()).append(")\r\n");
                skippedCount++;
            }

            addLog(itemLog.toString());
            summary.append(itemLog);
        }

        // Eliminar la selección temporal de T_Selection al finalizar
        DB.executeUpdateEx("DELETE FROM T_Selection WHERE AD_PInstance_ID = ?", new Object[]{getAD_PInstance_ID()}, get_TrxName());

        String msgHeader = String.format("Proceso finalizado. Total: %d | Exitosos: %d | Errores: %d | Omitidos: %d\r\n\r\n",
                m_selectionKeys.size(), successCount, errorCount, skippedCount);
        return msgHeader + summary.toString();
    }
}
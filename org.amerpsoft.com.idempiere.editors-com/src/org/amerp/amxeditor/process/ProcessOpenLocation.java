package org.amerp.amxeditor.process;

import java.util.logging.Level;
import org.adempiere.webui.apps.AEnv;
import org.amerp.amxeditor.model.MBPartnerLocationExt;
import org.compiere.model.MQuery;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.zkoss.zk.ui.Executions;

/**
 * Proceso para abrir la ventana de mantenimiento de C_Location
 * filtrada por la ubicación seleccionada en la pestaña C_BPartner_Location.
 */
public class ProcessOpenLocation extends SvrProcess {

    // ID estándar de la ventana de mantenimiento de Location / Dirección
    private static final int LOCATION_WINDOW_ID = 121; 

    @Override
    protected void prepare() {
        // En caso de recibir parámetros desde la definición del proceso en el Dictionary
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) {
                continue;
            }
            log.log(Level.INFO, "Param: " + name + " = " + para[i].getParameter());
        }
    }

    @Override
    protected String doIt() throws Exception {
        // Obtener el ID del registro actual de la pestaña (C_BPartner_Location)
        int recordCBPLocID = getRecord_ID();
        
        if (recordCBPLocID <= 0) {
            return "No se ha seleccionado ningún registro de dirección.";
        }

        // Instanciar el modelo de C_BPartner_Location pasándole el contexto y el ID
        MBPartnerLocationExt bpLocation = new MBPartnerLocationExt(getCtx(), recordCBPLocID, get_TrxName());
        
        // Obtener la clave foránea C_Location_ID desde el PO
        int locationID = bpLocation.getC_Location_ID();

        if (locationID <= 0) {
            return "El registro de localización del tercero no tiene una ubicación (C_Location_ID) asociada.";
        }

        if (locationID <= 0) {
            return "El registro actual no tiene una ubicación (C_Location_ID) asignada.";
        }

        // Crear la consulta filtrando por la clave primaria C_Location_ID
        MQuery query = new MQuery("C_Location");
        query.addRestriction("C_Location_ID", MQuery.EQUAL, locationID);

     // Ejecutar AEnv.zoom en el hilo UI de ZK
        Executions.schedule(AEnv.getDesktop(), desktop -> {
            AEnv.zoom(LOCATION_WINDOW_ID, query);
        }, new org.zkoss.zk.ui.event.Event("onZoom"));

        return "@Success@";
    }
}
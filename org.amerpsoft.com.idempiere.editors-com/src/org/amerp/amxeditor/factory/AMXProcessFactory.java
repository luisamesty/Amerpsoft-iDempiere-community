package org.amerp.amxeditor.factory;

import org.adempiere.base.IProcessFactory;
import org.amerp.amxeditor.process.ProcessBatchGeocoding;
import org.amerp.amxeditor.process.ProcessOpenGeoMap;
import org.amerp.amxeditor.process.ProcessOpenLocation;
import org.amerp.amxeditor.process.ProcessSingleLocationGeocoding;
import org.compiere.process.ProcessCall;

public class AMXProcessFactory implements IProcessFactory {

    @Override
    public ProcessCall newProcessInstance(String className) {
        if (className == null || className.isEmpty()) {
            return null;
        }

        // ProcessBatchGeocoding Proceso Masivo
        if (className.equals(ProcessBatchGeocoding.class.getName())) {
            return new ProcessBatchGeocoding();
        }

        // ProcessSingleLocationGeocoding Proceso Unitario (Mantenimiento C_Location)
        if (className.equals(ProcessSingleLocationGeocoding.class.getName())) {
            return new ProcessSingleLocationGeocoding();
        }

        // ProcessOpenLocation
        if (className.equals(ProcessOpenLocation.class.getName())) {
            return new ProcessOpenLocation();
        }
        
        // ProcessOpenGeoMap
        if (className.equals(ProcessOpenGeoMap.class.getName())) {
            return new ProcessOpenGeoMap();
        }
        
        return null;
    }
}
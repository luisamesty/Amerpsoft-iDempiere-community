package org.amerp.amxeditor.factory;


import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.amerp.amxeditor.callout.CalloutLocationExt;
import org.amerp.amxeditor.model.MBPartnerLocationExt;
import org.amerp.amxeditor.model.MLocationExt;

public class AMXCalloutFactory implements IColumnCalloutFactory {

    @Override
    public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {
        List<IColumnCallout> list = new ArrayList<>();

        // 1. Eventos directos sobre la tabla C_Location
        if (MLocationExt.Table_Name.equalsIgnoreCase(tableName)) {
            if (columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address1)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address2)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address3)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Address4)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_City)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_C_Region_ID)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_C_Parish_ID)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_C_Municipality_ID)
                    || columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Postal)) {
                
                list.add(new CalloutLocationExt());
            }
            if (columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_OpenGeoMap)) {
                list.add(new CalloutLocationExt());
            }
            if (columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_GeocodingStatus)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Latitude)
            		|| columnName.equalsIgnoreCase(MLocationExt.COLUMNNAME_Longitude)) {
                list.add(new CalloutLocationExt());
            }
        }

        // 2. Evento desde la pestaña de Dirección de Tercero (C_BPartner_Location)
        // Al cerrar/guardar el modal de dirección, la UI actualiza C_Location_ID en C_BPartner_Location
        if (MBPartnerLocationExt.Table_Name.equalsIgnoreCase(tableName)) {
            if (columnName.equalsIgnoreCase(MBPartnerLocationExt.COLUMNNAME_C_Location_ID)) {
                list.add(new CalloutLocationExt());
            }
            if (columnName.equalsIgnoreCase(MBPartnerLocationExt.COLUMNNAME_OpenLocation)) {
                list.add(new CalloutLocationExt());
            }
            
        }
        return list.toArray(new IColumnCallout[0]);
    }
}
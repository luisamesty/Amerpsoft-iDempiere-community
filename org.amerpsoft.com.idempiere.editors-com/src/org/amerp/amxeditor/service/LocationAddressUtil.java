package org.amerp.amxeditor.service;

import java.util.Properties;

import org.amerp.amxeditor.model.MBPartnerLocationExt;
import org.amerp.amxeditor.model.MLocationExt;
import org.amerp.amxeditor.model.MMunicipality;
import org.amerp.amxeditor.model.MParish;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MCountry;
import org.compiere.model.MRegion;
import org.compiere.util.Util;

public final class LocationAddressUtil {

    // Constructor privado para prevenir instanciación
    private LocationAddressUtil() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada.");
    }

    /**
     * DTO inmutable para transportar los componentes de la dirección y su geolocalización.
     */
    public record LocationData(
        String address1, 
        String address2, 
        String address3, 
        String address4, 
        String municipality, 
        String parish, 
        String city, 
        String region,
        String country,
        String geoSearchAddress,
        String geocodingStatus,
        String latitude,
        String longitude
    ) {
    	
    	// Constructor compacto: permite aplicar validaciones o sanitizaciones globales si se requiere
        public LocationData {
            // Asignación implícita de los 13 campos al finalizar el bloque
        }
        
        /**
         * Constructor secundario conveniente para instanciar con solo 2 direcciones, 
         * omitiendo address3 y address4.
         */
        public LocationData(
            String address1, String address2, String municipality, String parish, 
            String city, String region, String country,
            String geoSearchAddress, String geocodingStatus, String latitude, String longitude
        ) {
            this(address1, address2, null, null, 
            	municipality, parish, city, region, country, 
                geoSearchAddress, geocodingStatus, latitude, longitude);
        }

        /**
         * Constructor secundario conveniente para instanciar solo con la dirección física
         * antes de ejecutar la geocodificación (asigna null a las variables de geolocalización).
         */
        public LocationData(
            String address1, String address2, String address3, String address4, 
            String municipality, String parish, String city, String region, String country
        ) {
            this(address1, address2, address3, address4, 
                 municipality, parish, city, region, country, 
                 null, null, null, null);
        }

    }

    /**
     * Construye la dirección completa con un separador personalizado (ej. ", ").
     * 
     * @param data DTO con los campos de la dirección
     * @param separator Separador entre componentes (ej. ", " o " ")
     * @return Cadena formateada sin acentos
     */
    public static String buildFullAddress(LocationData data, String separator) {
        if (data == null) return "";

        String sep = (separator != null) ? separator : " ";
        StringBuilder fullAddress = new StringBuilder();

        appendComponent(fullAddress, data.address1(), sep);
        appendComponent(fullAddress, data.address2(), " "); // address2 suele ir con espacio simple
        appendComponent(fullAddress, data.address3(), " ");
        appendComponent(fullAddress, data.address4(), " ");
        appendComponent(fullAddress, data.municipality(), sep);
        appendComponent(fullAddress, data.parish(), sep);
        appendComponent(fullAddress, data.city(), sep);
        appendComponent(fullAddress, data.region(), sep);
        appendComponent(fullAddress, data.country(), sep);

        return Util.deleteAccents(fullAddress.toString());
    }

    /**
     * Sobrecarga predeterminada utilizando espacio simple como separador.
     */
    public static String buildFullAddress(LocationData data) {
        return buildFullAddress(data, " ");
    }

    /**
     * Helper privado para evitar repetición de código al concatenar.
     */
    private static void appendComponent(StringBuilder sb, String value, String separator) {
        if (!Util.isEmpty(value)) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(value);
        }
    }

    /**
     * Sobrecarga directa para invocaciones que no utilicen el DTO LocationData.
     */
    public static String buildFullAddress(String address1, String address2, String address3, String address4, 
                                         String municipality, String parish, String city, String region, String country) {
        return buildFullAddress(new LocationData(address1, address2, address3, address4, 
                                                 municipality, parish, city, region, country));
    }

    /**
     * Sanitiza la dirección eliminando términos locales que entorpecen la búsqueda en Nominatim.
     */
    public static String cleanAddress(String rawAddress) {
        if (Util.isEmpty(rawAddress)) return "";

        String cleaned = rawAddress;

        // 1. Eliminar etiquetas de piso, local, apartamento o rif/compañía con sus números/códigos
        cleaned = cleaned.replaceAll("(?i)\\b(PISO|LOCAL|APTO|APT|OFICINA|OFIC)\\s+[^\\s,]+", "");

        // 2. Omitir solo las palabras clave "EDIF", "EDIFICIO", "C.A." pero CONSERVAR el nombre
        cleaned = cleaned.replaceAll("(?i)\\b(EDIF|EDIFICIO|C\\.?A\\.?)\\b", "");

        // 3. Eliminar caracteres especiales que rompen la sintaxis de búsqueda
        cleaned = cleaned.replaceAll("[/#%]", " ");

        // 4. Normalizar espacios en blanco y comas
        cleaned = cleaned.replaceAll("\\s+,", ",").replaceAll(",\\s*,", ",").replaceAll("\\s+", " ").trim();

        return cleaned;
    }
    
    /**
     * Obtiene los datos de dirección y geolocalización desde la BD a partir del ID de C_Location.
     */
    public static LocationData getLocationDataByID(Properties ctx, int cLocationID, String trxName) {
        if (cLocationID <= 0) {
            return new LocationData(null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        MLocationExt loc = new MLocationExt(ctx, cLocationID, trxName);
        if (loc.get_ID() == 0) {
            return new LocationData(null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        // 1. Componentes de Dirección
        String address1 = loc.getAddress1();
        String address2 = loc.getAddress2();
        String address3 = loc.getAddress3();
        String address4 = loc.getAddress4();
        String city = loc.getCity();

        MMunicipality mmun = new MMunicipality(ctx, loc.getC_Municipality_ID(), trxName);
        String municipality = mmun.getName() != null ? mmun.getName() : "";
        
        MParish mpar = new MParish(ctx, loc.getC_Parish_ID(), trxName);
        String parish = mpar.getName() != null ? mpar.getName() : "";

        String region = null;
        if (loc.getC_Region_ID() > 0) {
            MRegion reg = MRegion.get(ctx, loc.getC_Region_ID());
            if (reg != null) region = reg.getName();
        }
        if (Util.isEmpty(region)) {
            region = loc.getRegionName();
        }

        String country = null;
        if (loc.getC_Country_ID() > 0) {
            MCountry c = MCountry.get(ctx, loc.getC_Country_ID());
            if (c != null) country = c.getName();
        }

        // 2. Componentes de Geolocalización
        String geoAddress = loc.getGeoSearchAddress() != null ? (String) loc.getGeoSearchAddress() : null;
        String geoStatus = loc.getGeocodingStatus() != null ? (String) loc.getGeocodingStatus() : null;
        String latitude = loc.getLatitude() != null ? (String) loc.getLatitude() : null;
        String longitude = loc.getLongitude() != null ? (String) loc.getLongitude() : null;

        return new LocationData(
            address1, 
            address2, 
            address3,
            address4,
            municipality, 
            parish, 
            city, 
            region, 
            country,
            geoAddress,
            geoStatus,
            latitude,
            longitude
        );
    }
    
    /**
     * Extrae los datos completos de geolocalización y dirección basándose en la pestaña y campo activo de iDempiere.
     */
    public static LocationData getLocationDataFromTab(Properties ctx, GridTab mTab, GridField mField, Object value) {
        if (mTab == null) {
            return new LocationData(null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        String tableName = mTab.getTableName();
        
        // Usar null permite a iDempiere asociar la consulta a la transacción activa del hilo
        String trxName = null; 
        
        LocationData locData = null;
        int locationID = 0;
        
        // CASO 1: Ejecución directa sobre la tabla C_Location
        if (MLocationExt.Table_Name.equalsIgnoreCase(tableName)) {
            Object locObj = mTab.getValue("C_Location_ID");
            if (locObj instanceof Integer) {
                locationID = (Integer) locObj;
            }
        } 
        // CASO 2: Ejecución desde C_BPartner_Location o pestañas secundarias con referencia a C_Location_ID
        else if (mField != null && MBPartnerLocationExt.COLUMNNAME_C_Location_ID.equalsIgnoreCase(mField.getColumnName())) {
            if (value instanceof Integer) {
                locationID = (Integer) value;
            } else {
                Object locObj = mTab.getValue("C_Location_ID");
                if (locObj instanceof Integer) {
                    locationID = (Integer) locObj;
                }
            }
        }

        if (locationID > 0) {
            locData = getLocationDataByID(ctx, locationID, trxName);
        }

        // Retornar un objeto vacío con estructura completa si no se encontró ubicación válida
        return (locData != null) ? locData : new LocationData(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
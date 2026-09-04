package org.amerp.amxeditor.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.amerp.amxeditor.model.MLocationExt;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

public class NominatimService {

    private static final CLogger log = CLogger.getCLogger(NominatimService.class);
    
    /**
     * Obtiene la URL base desde las Preferencias del Sistema (SysConfig)
     * Ej. Local: http://192.168.1.100:8080
     * Ej. Público: https://nominatim.openstreetmap.org
     */
    private String getBaseUrl(int AD_Client_ID) {
        return MSysConfig.getValue("LOCATION_MAPS_URL_PREFIX_NOMINATIM", "https://nominatim.openstreetmap.org", AD_Client_ID);
    }

    public GeocodingResult geocode(String address, int AD_Client_ID) {
        if (Util.isEmpty(address)) {
            return new GeocodingResult(null, null, null, MLocationExt.GEOCODING_STATUS_NOT_FOUND);
        }

        // 1. Primer intento con la dirección sanitizada
        // String cleanedAddress = cleanAddress(address);
        String cleanedAddress = Util.deleteAccents(address);
        log.info("Nominatim - Intento 1 (Limpia sin comas): " + cleanedAddress);
        GeocodingResult result = executeHttpRequest(cleanedAddress, AD_Client_ID);

        if (MLocationExt.GEOCODING_STATUS_OK.equals(result.geocodingResultStatus())) {
            return result;
        }

        // 2. Segundo intento (Fallback): Si falla o da NOT_FOUND, usar componentes generales (Ciudad, Estado)
        String fallbackAddress = buildFallbackQuery(address);
        if (!Util.isEmpty(fallbackAddress) && !fallbackAddress.equalsIgnoreCase(cleanedAddress)) {
            log.info("Nominatim - Intento 2 (Fallback): " + fallbackAddress);
            GeocodingResult fallbackResult = executeHttpRequest(fallbackAddress, AD_Client_ID);
            if (MLocationExt.GEOCODING_STATUS_OK.equals(fallbackResult.geocodingResultStatus())) {
                return fallbackResult;
            }
        }

        return result;
    }

    /**
     * Realiza la petición HTTP a la API de Nominatim
     */
    private GeocodingResult executeHttpRequest(String queryAddress, int AD_Client_ID) {
        try {
            String baseUrl = getBaseUrl(AD_Client_ID);
            String encodedAddress = URLEncoder.encode(queryAddress, StandardCharsets.UTF_8);
            String url = String.format("%s/search?q=%s&format=json&limit=1", baseUrl, encodedAddress);

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "iDempiere-NominatimPlugin/1.0") // Requerido por la política de OSM
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONArray results = new JSONArray(response.body());
                if (results.length() > 0) {
                    JSONObject match = results.getJSONObject(0);
                    
                    String lat = match.getString("lat");
                    String lon = match.getString("lon");
                    String displayName = match.optString("display_name", "");

                    return new GeocodingResult(lat, lon, displayName, MLocationExt.GEOCODING_STATUS_OK);
                } else {
                    // HTTP 200 pero sin resultados en Nominatim
                    return new GeocodingResult(null, null, null, MLocationExt.GEOCODING_STATUS_NOT_FOUND);
                }
            } else {
                log.warning("Nominatim retornó código de respuesta HTTP: " + response.statusCode());
                return new GeocodingResult(null, null, null, MLocationExt.GEOCODING_STATUS_ERROR);
            }
        } catch (Exception e) {
            log.severe("Error al consultar Nominatim: " + e.getMessage());
            return new GeocodingResult(null, null, null, MLocationExt.GEOCODING_STATUS_ERROR);
        }
    }

    /**
     * Extrae las partes más generales de la dirección (últimas comas: Ciudad, Región, País)
     * // Ej: "PISO P/B", "PISO 2", "LOCAL 3-A", "APTO 12" -> se eliminan
     */
    private String buildFallbackQuery(String rawAddress) {
        if (Util.isEmpty(rawAddress)) return "";

        String retAddress = "";
        
        String[] parts = rawAddress.split(",");
        if (parts.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = Math.max(0, parts.length - 2); i < parts.length; i++) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(parts[i].trim());
            }
            retAddress = sb.toString();
        }
        retAddress = LocationAddressUtil.cleanAddress(retAddress);
        return retAddress;
    }

    // DTO utilizando String directamente para facilitar la asignación a MLocationExt / C_Location
    public record GeocodingResult(String latitude, String longitude, String displayName, String geocodingResultStatus) {}
}
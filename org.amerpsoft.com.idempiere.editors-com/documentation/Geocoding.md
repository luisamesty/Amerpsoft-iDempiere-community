# Geo-Codificación 

Ajustes del diccionario de aplicaciones.

## Tabla C_Location

Para integrar la geocodificación de manera completa y estructurada en la tabla **`C_Location`**, los dos campos principales que necesitas (y que ya venían en el pack) son **Latitud** y **Longitud**, pero es altamente recomendable añadir un tercer campo auxiliar de **Estado de Geocodificación** para controlar qué registros ya fueron procesados de forma óptima.

A continuación tienes la definición técnica detallada para crearlos en el **Diccionario de Datos de iDempiere**:

---

### 1. Campo: Latitud (`Latitude`)

* **Nombre (`Name`):** Latitud
* **Nombre de Columna BD (`ColumnName`):** `Latitude`
* **Descripción (`Description`):** Coordenada geográfica de latitud de la ubicación.
* **Ayuda (`Help`):** Latitud obtenida mediante el servicio de geocodificación (Nominatim/OpenStreetMap).
* **Tipo de Referencia (`AD_Reference_ID`):** `String` (ID: `10`)
* **Longitud (`FieldLength`):** `30`
* **Obligatorio (`IsMandatory`):** `No`
* **Actualizable (`IsUpdateable`):** `Sí`
* **Sincronizar BD (`IsSyncDatabase`):** `Sí` (Genera `latitude VARCHAR(30)` en PostgreSQL)

---

### 2. Campo: Longitud (`Longitude`)

* **Nombre (`Name`):** Longitud
* **Nombre de Columna BD (`ColumnName`):** `Longitude`
* **Descripción (`Description`):** Coordenada geográfica de longitud de la ubicación.
* **Ayuda (`Help`):** Longitud obtenida mediante el servicio de geocodificación (Nominatim/OpenStreetMap).
* **Tipo de Referencia (`AD_Reference_ID`):** `String` (ID: `10`)
* **Longitud (`FieldLength`):** `30`
* **Obligatorio (`IsMandatory`):** `No`
* **Actualizable (`IsUpdateable`):** `Sí`
* **Sincronizar BD (`IsSyncDatabase`):** `Sí` (Genera `longitude VARCHAR(30)` en PostgreSQL)

---

### 3. Campo Recomendado: Estado / Resultado Geocodificación (`GeocodingStatus`)

*(Este campo opcional te servirá para que el proceso en lote no reintente continuamente aquellas direcciones que Nominatim no logra encontrar o que están mal redactadas).*

* **Nombre (`Name`):** Estado de Geocodificación
* **Nombre de Columna BD (`ColumnName`):** `GeocodingStatus`
* **Descripción (`Description`):** Estado o resultado obtenido del proceso de geocodificación automática.
* **Ayuda (`Help`):** Muestra el resultado de la consulta (ej. OK, NOT_FOUND, ERROR).
* **Tipo de Referencia (`AD_Reference_ID`):** `String` (ID: `10`)
* **Longitud (`FieldLength`):** `20`
* **Obligatorio (`IsMandatory`):** `No`
* **Actualizable (`IsUpdateable`):** `Sí`
* **Sincronizar BD (`IsSyncDatabase`):** `Sí` (Genera `geocodingstatus VARCHAR(20)` en PostgreSQL)

---

### Resumen del Script SQL resultante en PostgreSQL

Si ejecutas la sincronización desde iDempiere o directamente en PostgreSQL, las columnas quedan definidas así:

```sql
ALTER TABLE c_location 
    ADD COLUMN IF NOT EXISTS latitude VARCHAR(30) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS longitude VARCHAR(30) DEFAULT NULL,
    ADD COLUMN IF NOT EXISTS geocodingstatus VARCHAR(20) DEFAULT NULL;

```

## Ventana 


## Editor de Dirección extendida.

## Busqueda Manual

Para ver las coordenadas directamente sobre una interfaz gráfica con mapa (tanto OpenStreetMap como Google Maps), tienes dos opciones según lo que necesites en este momento:

---

### Probarlas Rápidamente en la Web (Enlaces directos)

Puedes abrir cualquiera de estos dos enlaces en tu navegador reemplazando `LATITUD` y `LONGITUD` con los valores que te devolvió el servicio:

* **Ver en OpenStreetMap:**

```text
https://www.openstreetmap.org/?mlat=LATITUD&mlon=LONGITUD#map=16/LATITUD/LONGITUD

```


* **Ver en Google Maps:**

```text
https://www.google.com/maps?q=LATITUD,LONGITUD

```

Sí, la aplicación web de Google Maps permite obtener las coordenadas exactas de cualquier punto de manera directa.

* **Hacer clic en cualquier lugar del mapa:** Haz clic derecho sobre el punto exacto que te interesa en la pantalla y verás un menú contextual con la latitud y longitud en formato decimal en la primera línea. Al hacer clic sobre los números, **se copiarán automáticamente al portapapeles**.
* **Seleccionar un lugar o marcador existente:** Haz clic izquierdo sobre un edificio, negocio o pin. Al desplegarse el panel lateral izquierdo con la información del sitio, las coordenadas aparecerán en la barra de dirección URL de tu navegador web con la estructura `@latitud,longitud` (ejemplo: `[https://www.google.com/maps/place/.../@9.5583,-69.2125,17z](https://www.google.com/maps/place/.../@9.5583,-69.2125,17z)`).


---

### Abrir el Mapa Directamente desde la UI de iDempiere

Si quieres que el usuario pueda hacer clic en la propia ventana de iDempiere para ver la ubicación en un mapa sin copiar y pegar texto, puedes aprovechar la lógica estándar de los botones de mapa de `MLocationExt`.

Agrega este método utilitario en tu clase de la ventana modal (`WLocationExtDialog` o el editor asociado):

```java
import org.zkoss.zk.ui.Executions;

/**
 * Abre el mapa en una pestaña nueva del navegador con las coordenadas obtenidas.
 */
private void openMap(String latitude, String longitude) {
    if (Util.isEmpty(latitude) || Util.isEmpty(longitude)) {
        return;
    }
    
    // URL para abrir Google Maps centrado en las coordenadas
    String mapUrl = String.format("https://www.google.com/maps?q=%s,%s", latitude, longitude);
    
    // Abrir en una pestaña nueva de ZK WebUI
    Executions.getCurrent().sendRedirect(mapUrl, "_blank");
}

```

Simplemente vinculas este método al botón de **"Ver en Mapa"** o **"Google Maps"** del panel inferior de tu diálogo modal para inspeccionar el punto de geolocalización al instante.

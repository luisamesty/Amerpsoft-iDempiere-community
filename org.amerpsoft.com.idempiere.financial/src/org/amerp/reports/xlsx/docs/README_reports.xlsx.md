# Reportes XLS/XLSX Idempiere

Ejemplo de clases `AccountElements_Tree_Form.java` (el controlador de formulario) y `ExcelViewerPanel.java` (el componente de vista previa), aquí tienes un resumen instructivo de los pasos clave para crear y desplegar un reporte XLSX en iDempiere.

Este proceso se basa en el patrón de usar un **Formulario Personalizado (CustomForm)** para los parámetros y generar el archivo usando POI (`crearXlsx`).

## 📋 Resumen Instructivo para Reporte XLSX en iDempiere

-----

### 1\. 🏗️ Estructura del Proyecto y Dependencias

Asegúrate de que tu plugin tenga las dependencias correctas para generar y mostrar el Excel:

  * **Generación:** Debes incluir las bibliotecas **Apache POI** (específicamente `poi-ooxml` para XLSX).
  * **Web UI:** Tu plugin debe depender de `org.adempiere.webui` y `org.zkoss.zk.library` (ya visible por tus *imports* ZK).

-----

### 2\. 🎛️ El Controlador de Parámetros (`AccountElements_Tree_Form.java`)

Este es el punto de entrada que maneja la lógica, los parámetros y la interacción con el usuario.

  * **Clase Base:** Implementa `IFormController` y extiende `CustomForm` o `ADForm`. También implementas `IProcessUI` para gestionar el bloqueo de la interfaz.
  * **Método `zkInit()`:** Define los filtros (e.g., `fClient`, `fAcctSchema`) dentro del área **`North`** de tu `Borderlayout`.
  * **Método `onEvent()`:** Maneja el botón de proceso (`processButton`).
    ```java
    // Lógica clave en onEvent
    if (source == processButton) {
        // 1. Llama al método que genera el archivo (en un bloque try-finally para desbloqueo)
        fullPath = runServerProcessForm(); 
        // 2. Muestra el reporte en la interfaz
        previewReportWeb(fullPath); 
    } 
    ```
  * **Bloqueo UI:** Los métodos `lockUI`/`unlockUI` deben deshabilitar/habilitar los campos de entrada (`setReadonly`/`setDisabled`) y usar `Clients.showBusy()`/`Clients.clearBusy()` para dar *feedback* al usuario.

-----

### 3\. 📝 Generación del Archivo (`crearXlsx(...)`)

Este método, llamado desde `runServerProcessForm()`, realiza la lógica de negocio y genera el archivo.

  * **Tecnología:** Utiliza **Apache POI SXSSF** (`SXSSFWorkbook`, `SXSSFSheet`) para manejar grandes volúmenes de datos con bajo uso de memoria.
  * **Flujo:**
    1.  Obtener datos (`reportData`).
    2.  Crear `SXSSFWorkbook` y `SXSSFSheet`.
    3.  Iterar sobre `reportData` y crear `SXSSFRow` y `Cell` (donde usas tu `ExcelUtils.createStyledCell`).
    4.  Escribir el `SXSSFWorkbook` en un archivo temporal (`File.createTempFile`).
    5.  Retornar la ruta (`fullPath`) del archivo temporal.
  * **Optimización:** Usar `sheet.flushRows(batchSize)` periódicamente es crucial para el rendimiento.

-----

### 4\. 🖼️ El Visor de Reporte (`ExcelViewerPanel.java`)

Esta clase es responsable de leer el archivo XLSX temporal y mostrar una vista previa paginada en la interfaz web de iDempiere.

  * **Clase Base:** Extiende un componente ZK, como `Div`, para ser un contenedor.
  * **Método `loadExcel()`:**
    1.  Lee el archivo temporal (`filePath`) usando **Apache POI XSSF** (`XSSFWorkbook`) (no SXSSF, ya que necesitas leer).
    2.  Crea un `Grid` ZK con una sección fija (`topGrid` para encabezados) y una sección *scrolleable* (`bodyGrid` para datos).
    3.  Crea filas (`Row`) de ZK y celdas (`Label`) para mapear el contenido del Excel al HTML/ZK.
    4.  Implementa la lógica de **paginación** (ocultando/mostrando filas de `bodyRowList` basado en `currentIndex`) para mejorar el rendimiento de la vista previa.

-----

### 5\. 🚀 Despliegue en iDempiere

Para que el reporte sea accesible, debes registrar el formulario y la clase:

1.  **Registro del Formulario:** Crea un registro en la ventana **Ventana, Pestaña y Campo (AD\_Window)** de iDempiere:
      * Crea un nuevo **Formulario (AD\_Form)**.
      * En el campo **Clase de la URL**, ingresa el nombre de tu clase controladora: `org.amerp.reports.xlsx.AccountElements_Tree_Form`.
2.  **Crear Menú:** Asocia el nuevo `AD_Form` a un elemento de menú en la ventana **Elemento de Menú (AD\_Menu)**.
3.  **Acceso:** El reporte será accesible haciendo clic en el nuevo elemento del menú, lo que cargará tu `CustomForm` en una nueva pestaña.

-----

### Resumen del Flujo de Ejecución

1.  **Usuario Clic:** El usuario hace clic en el menú (llama a `AccountElements_Tree_Form`).
2.  **Parámetros:** El usuario selecciona filtros y hace clic en **Process**.
3.  **Controlador (`onEvent`):** Llama a `lockUI()` y luego a `crearXlsx()`.
4.  **Generación:** `crearXlsx()` genera el XLSX físico en el servidor.
5.  **Desbloqueo:** Llama a `unlockUI()`.
6.  **Vista Previa:** `previewReportWeb()` crea una nueva ventana ZK que utiliza `ExcelViewerPanel` para leer y mostrar el archivo recién generado.
7.  **Descarga:** El `downloadButton` usa `Filedownload.save(media)` para enviar el archivo al navegador.

¿Te gustaría que profundicemos en alguna de las clases o en el manejo del *lock/unlock* de la interfaz?
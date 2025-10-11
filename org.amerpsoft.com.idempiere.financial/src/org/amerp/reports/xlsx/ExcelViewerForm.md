Excelente 👏 — la **opción 2 (visor embebido ZK)** es la más práctica y totalmente integrada con iDempiere, sin depender de servicios externos.
Vamos a construirla paso a paso, con una explicación completa y un ejemplo de código **listo para probar** en iDempiere 12.

---

## 🎯 Objetivo

Mostrar dentro de una ventana o formulario ZK (`CustomForm`) el contenido de un archivo `.xlsx`, renderizado como una tabla (usando `Grid` o `Listbox`), **con scroll, estilos básicos y sin salir de iDempiere**.

---

## 🧱 Estructura general

Crearemos una clase como esta:

```
org.amerp.reports.viewer.ExcelViewerForm
```

que extiende `CustomForm` e incluye:

* Un `Borderlayout` (layout principal).
* Un `North` con el nombre del archivo o botones.
* Un `Center` con un `Grid` que muestra el contenido del Excel.

---

## 🧩 Código completo comentado

```java
package org.amerp.reports.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.adempiere.webui.component.*;
import org.adempiere.webui.panel.CustomForm;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import org.apache.poi.ss.usermodel.*;

public class ExcelViewerForm extends CustomForm {

    private static final long serialVersionUID = 1L;

    private Borderlayout mainLayout = new Borderlayout();
    private Center center = new Center();
    private North north = new North();

    private Grid grid = new Grid();
    private Rows rows = new Rows();

    private String filePath; // Ruta del Excel a mostrar

    public ExcelViewerForm(String filePath) {
        this.filePath = filePath;
        init();
    }

    private void init() {
        this.appendChild(mainLayout);
        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");

        // Encabezado
        Label lbl = new Label("Visualizando archivo: " + filePath);
        lbl.setStyle("font-weight:bold; color:#003366; font-size:14px");
        north.appendChild(lbl);
        mainLayout.appendChild(north);

        // Contenedor central
        center.setAutoscroll(true);
        center.setStyle("overflow:auto; padding:5px");
        mainLayout.appendChild(center);

        // Construir el grid
        grid.setSizedByContent(true);
        grid.setVflex("1");
        grid.setHflex("1");
        grid.appendChild(rows);
        center.appendChild(grid);

        loadExcel();
    }

    /**
     * Carga el archivo Excel y lo convierte en filas/columnas del Grid
     */
    private void loadExcel() {
        File file = new File(filePath);
        if (!file.exists()) {
            Messagebox.show("Archivo no encontrado: " + filePath, "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            boolean isHeader = true;
            for (Row row : sheet) {
                Row zkRow = new Row();

                for (Cell cell : row) {
                    String text = getCellValue(cell);
                    Label lbl = new Label(text);

                    if (isHeader) {
                        lbl.setStyle("font-weight:bold; background-color:#ddeeff; border:1px solid #ccc; padding:2px");
                    } else {
                        lbl.setStyle("border:1px solid #eee; padding:2px");
                    }

                    zkRow.appendChild(lbl);
                }

                rows.appendChild(zkRow);
                isHeader = false;
            }

            workbook.close();

        } catch (IOException e) {
            Messagebox.show("Error leyendo el archivo Excel:\n" + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Convierte un valor de celda a String legible
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                else
                    return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }
}
```

---

## ⚙️ Cómo usarlo en iDempiere

Puedes llamarlo desde un proceso o una ventana ZK, por ejemplo:

```java
File excelFile = new File("/tmp/reporte.xlsx");
ExcelViewerForm viewer = new ExcelViewerForm(excelFile.getAbsolutePath());
SessionManager.getAppDesktop().showWindow(viewer);
```

---

## 💅 Mejoras opcionales

1. **Agregar botones arriba**:

   * Un botón para "Refrescar".
   * Otro para "Descargar el Excel original".

   ```java
   Toolbar toolbar = new Toolbar();
   Button btnRefresh = new Button("Refrescar");
   btnRefresh.addEventListener(Events.ON_CLICK, e -> reload());
   toolbar.appendChild(btnRefresh);
   north.appendChild(toolbar);
   ```

2. **Limitación de filas** para evitar bloqueos con ficheros grandes:

   ```java
   int rowCount = 0;
   for (Row row : sheet) {
       if (rowCount++ > 500) break; // solo primeras 500 filas
   }
   ```

3. **Ancho automático**:

   ```java
   grid.setSizedByContent(true);
   grid.setHflex("min");
   ```

4. **Scroll optimizado**:

   ```java
   center.setStyle("overflow:auto; max-height:90vh");
   ```

---

## 🧰 Dependencias necesarias

Tu plugin ya usa Apache POI, pero asegúrate de incluir (en `MANIFEST.MF`):

```
Require-Bundle: org.apache.poi;bundle-version="4.1.2"
```

Y si usas SXSSF para exportar grandes volúmenes, añade también:

```
Require-Bundle: org.apache.poi.ooxml;bundle-version="4.1.2"
```

---

## 🚀 Resultado

Tendrás una ventana dentro de iDempiere que muestra así:

🖥️
📄 **Visualizando archivo: /tmp/reporte.xlsx**
│ [tabla con encabezados azules y celdas alineadas]

✅ Totalmente integrada en la interfaz ZK.
✅ Sin salir del entorno.
✅ Sin dependencias externas.

---

Perfecto ✅
Aquí tienes una versión **completa y lista para usar en iDempiere 12**, con todos los **imports correctos**, estructura estándar de un `CustomForm` (compatible con el registro en el diccionario como ventana ZK), y el visor de Excel completamente funcional.

---

## 📁 Clase completa: `ExcelViewerForm.java`

```java
package org.amerp.reports.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Center;
import org.adempiere.webui.component.CustomForm;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.North;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.session.SessionManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Button;
import org.zkoss.zk.ui.event.Events;

/**
 * Visor de archivos XLSX dentro de iDempiere
 * @author Luis
 */
public class ExcelViewerForm extends CustomForm {

    private static final long serialVersionUID = 1L;

    private Borderlayout mainLayout = new Borderlayout();
    private Center center = new Center();
    private North north = new North();

    private Grid grid = new Grid();
    private Rows rows = new Rows();

    private String filePath;

    // === Constructor ===
    public ExcelViewerForm(String filePath) {
        this.filePath = filePath;
        initForm();
    }

    @Override
    protected void initForm() {
        // Layout principal
        this.appendChild(mainLayout);
        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");

        // --- NORTH (Título + botones)
        Toolbar toolbar = new Toolbar();
        toolbar.setStyle("padding:4px; background-color:#f5f5f5; border-bottom:1px solid #ccc;");

        Label lblTitle = new Label("Visualizando archivo Excel: " + filePath);
        lblTitle.setStyle("font-weight:bold; color:#003366; font-size:14px; margin-right:20px;");
        toolbar.appendChild(lblTitle);

        Button btnRefresh = new Button("Refrescar");
        btnRefresh.addEventListener(Events.ON_CLICK, event -> reload());
        toolbar.appendChild(btnRefresh);

        north.appendChild(toolbar);
        mainLayout.appendChild(north);

        // --- CENTER (Contenido del Excel)
        center.setAutoscroll(true);
        center.setStyle("overflow:auto; padding:6px; background:white;");
        mainLayout.appendChild(center);

        grid.setSizedByContent(true);
        grid.setVflex("1");
        grid.setHflex("1");
        grid.appendChild(rows);
        center.appendChild(grid);

        loadExcel();
    }

    /**
     * Recarga el archivo
     */
    private void reload() {
        rows.getChildren().clear();
        loadExcel();
    }

    /**
     * Carga el archivo Excel y lo representa en el Grid
     */
    private void loadExcel() {
        File file = new File(filePath);
        if (!file.exists()) {
            Messagebox.show("Archivo no encontrado:\n" + filePath, "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            boolean isHeader = true;
            int rowCount = 0;

            for (org.apache.poi.ss.usermodel.Row excelRow : sheet) {
                if (rowCount++ > 500) break; // Límite de filas visibles

                Row zkRow = new Row();

                int cellCount = excelRow.getLastCellNum();
                for (int i = 0; i < cellCount; i++) {
                    Cell cell = excelRow.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String text = getCellValue(cell);

                    Label lbl = new Label(text);
                    lbl.setStyle(isHeader
                        ? "font-weight:bold; background-color:#ddeeff; border:1px solid #ccc; padding:2px;"
                        : "border:1px solid #eee; padding:2px;");
                    zkRow.appendChild(lbl);
                }

                rows.appendChild(zkRow);
                isHeader = false;
            }

            workbook.close();

        } catch (IOException e) {
            Messagebox.show("Error leyendo el archivo Excel:\n" + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    /**
     * Convierte el valor de celda a texto
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                else
                    return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Abre el visor desde otra clase o proceso
     */
    public static void openViewer(String filePath) {
        ExcelViewerForm viewer = new ExcelViewerForm(filePath);
        SessionManager.getAppDesktop().showWindow(viewer);
    }
}
```

---

## 🧭 Cómo usarlo en iDempiere

Puedes abrir el visor directamente desde cualquier proceso Java o clase con:

```java
ExcelViewerForm.openViewer("/opt/idempiere/reports/TrialBalance.xlsx");
```

Esto abrirá una **ventana ZK con scroll**, mostrando la hoja Excel en formato tabla.

---

## 🧩 Registro como Ventana en el Diccionario

Si quieres abrirlo **desde el menú de iDempiere**:

1. En **Ventanas, Pestañas y Campos**, crea una ventana:

   * **Nombre:** Visor Excel
   * **Tipo de Ventana:** Custom Form

2. En **Custom Form**, crea una entrada:

   * **Nombre:** Excel Viewer
   * **Clase:** `org.amerp.reports.viewer.ExcelViewerForm`

3. Agrega la ventana al **menú**.

Cuando se abra, puedes pasarle una ruta fija o mejorar la clase para seleccionar el fichero con un `Fileupload`.

---

## 🧰 Dependencias OSGi

En tu `MANIFEST.MF`, asegúrate de tener:

```properties
Require-Bundle: 
 org.adempiere.base,
 org.adempiere.webui,
 org.apache.poi;bundle-version="4.1.2",
 org.apache.poi.ooxml;bundle-version="4.1.2"
```

---

## 🚀 Resultado visual

* Cabecera con nombre del fichero y botón **Refrescar**
* Tabla con el contenido de la hoja Excel (máx. 500 filas)
* Scroll automático
* Fondo blanco y celdas delineadas

---

¿Quieres que te extienda esta clase para permitir **seleccionar el archivo Excel mediante un botón “Cargar Archivo”** (con `Fileupload.get()`)?
Así podrías abrirla sin ruta fija y dejar que el usuario escoja el fichero Excel desde su escritorio.

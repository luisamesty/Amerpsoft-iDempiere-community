# XSSFWorkbook vs SXSSFWorkbook
Apache POI te ofrece dos implementaciones principales para generar archivos Excel en formato XLSX:

| Clase             | Nombre completo                               | ¿Qué es?                                                                        | ¿Cuándo usar?                                                          |
| ----------------- | --------------------------------------------- | ------------------------------------------------------------------------------- | ---------------------------------------------------------------------- |
| **XSSFWorkbook**  | `org.apache.poi.xssf.usermodel.XSSFWorkbook`  | Manejo tradicional en memoria                                                   | Cuando el archivo no es muy grande (pocos MB o < 100 000 filas aprox.) |
| **SXSSFWorkbook** | `org.apache.poi.xssf.streaming.SXSSFWorkbook` | Versión *streaming*, escribe datos en disco temporal para no consumir tanta RAM | Cuando necesitas generar archivos grandes (miles o millones de filas)  |

---

### ✅ **SXSSFWorkbook (Streaming XSSF Workbook)**

**✔ Ventajas (Pros)**

* Ideal para **grandes volúmenes de datos**.
* Sólo mantiene en memoria un número limitado de filas (por defecto 100).
* Evita `OutOfMemoryError` porque almacena en **disco los datos antiguos** (archivo temporal).
* Muy útil para procesos en servidores (ej: en un proceso de iDempiere).

**✘ Desventajas (Contras)**

* **No puedes leer o modificar** una fila después de que ha sido escrita y vaciada del buffer.
* **No soporta ciertas funciones avanzadas** (auto-filtros sobre columnas ya escritas, fórmulas complejas, imágenes, etc.).
* El archivo temporal debe eliminarse manualmente (con `dispose()`) al final.

---

### ✅ **XSSFWorkbook (Clásico - NO streaming)**

**✔ Ventajas (Pros)**

* Todo el libro Excel está **en memoria**, por lo que permite:

  * Leer, modificar, borrar cualquier dato.
  * Aplicar estilos, imágenes, fórmulas, autoajustes, comentarios.
* Muy flexible para reportes complejos.

**✘ Desventajas (Contras)**

* Cada celda, hoja y estilo se almacena en RAM → **consume mucha memoria**.
* Con archivos grandes (>150MB o más de 200 000 filas) puedes obtener `OutOfMemoryError`.

---

### 📌 **Resumen en tabla**

| Característica                        | `XSSFWorkbook`                | `SXSSFWorkbook`                          |
| ------------------------------------- | ----------------------------- | ---------------------------------------- |
| Memoria RAM                           | Alta                          | Muy baja                                 |
| Tamaño permitido                      | Archivos pequeños/medios      | Archivos muy grandes (millones de filas) |
| Lectura y modificación posterior      | ✔ Sí                          | ✘ No (sólo escritura)                    |
| Velocidad                             | Más lento en archivos grandes | Rápido para archivos grandes             |
| Uso de disco temporal                 | ✘ No                          | ✔ Sí                                     |
| Estilos, imágenes, fórmulas complejas | ✔ Completo                    | Parcial o limitado                       |
| Necesita `dispose()`                  | ✘ No                          | ✔ Sí                                     |

---

### 🛠 **¿Cuándo usar cada uno?**

| Necesidad                                                              | Usa             |
| ---------------------------------------------------------------------- | --------------- |
| Reporte pequeño de hasta unas 50 000 filas con estilo, logos, fórmulas | `XSSFWorkbook`  |
| Archivo grande (ej: 200 000 - 1 000 000 filas)                         | `SXSSFWorkbook` |
| Debes leer y escribir Excel a la vez                                   | `XSSFWorkbook`  |
| Exportar millones de registros desde iDempiere sin explotar la RAM     | `SXSSFWorkbook` |

---

### 💡 Ejemplo rápido

```java
// Para archivos grandes
SXSSFWorkbook wb = new SXSSFWorkbook(100); // 100 filas en memoria máx.
Sheet sheet = wb.createSheet("Datos");

for (int r = 0; r < 1000000; r++) {
    Row row = sheet.createRow(r);
    Cell cell = row.createCell(0);
    cell.setCellValue("Fila " + r);
}

FileOutputStream out = new FileOutputStream("/tmp/reporte.xlsx");
wb.write(out);
out.close();
wb.dispose(); // Limpia archivos temporales
```

---

### ✅ **Conclusión**

* **Usa `XSSFWorkbook`** para reportes normales, donde necesitas formato, imágenes o manipulación posterior.
* **Usa `SXSSFWorkbook`** cuando generas archivos MUY grandes y sólo escribes datos secuencialmente.

---


Eso que te ocurre es bastante común cuando se usa **SXSSFWorkbook** de Apache POI: el archivo se genera, Excel lo abre, pero **LibreOffice, OnlyOffice o WPS Office lo consideran “corrupto” y lo reparan**. Esto suele ocurrir por algunas limitaciones del modo *streaming*.

---

### 📌 **¿Por qué SXSSFWorkbook puede generar archivos incompatibles con LibreOffice?**

SXSSFWorkbook no escribe directamente un .xlsx limpio como XSSFWorkbook, sino que:

✔ Guarda temporalmente partes del Excel en archivos XML en disco.
✔ Luego comprime esos XML a formato `.xlsx`.
✘ Si se manipula mal algún estilo, fórmula o estructura, el XML final puede tener inconsistencias pequeñas que Excel ignora pero LibreOffice no.

Los problemas más comunes son:

| Causa                                                         | Descripción                                                                                   |
| ------------------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| **Estilos duplicados o corruptos**                            | Crear estilos dentro del bucle por cada fila → genera miles de estilos inválidos.             |
| **Fórmulas incorrectas o sin recalcular**                     | Algunas versiones de LibreOffice fallan si no se invoca `setForceFormulaRecalculation(true)`. |
| **Strings demasiado largos o sin escapado XML**               | Caracteres especiales (&, <, >) sin escapado correcto.                                        |
| **No cerrar o no llamar a `dispose()`**                       | Deja archivos temporales incompletos o XML mal cerrados.                                      |
| **Incompatibilidad con comentarios, imágenes o auto-filtros** | Estos elementos no están bien soportados en SXSSFWorkbook.                                    |

---

### ✅ **Buenas prácticas para evitar archivos “corruptos”**

✔ 1. **Solo un estilo por tipo de celda (NO en cada iteración)**

```java
CellStyle styleHeader = wb.createCellStyle();
// Configurar solo una vez fuera del bucle
```

✔ 2. **Forzar recálculo de fórmulas (si las hay)**

```java
sheet.setForceFormulaRecalculation(true);
```

✔ 3. **Eliminar archivos temporales correctamente**

```java
wb.write(out);
out.close();
wb.dispose();  // ⚠ Muy importante
wb.close();
```

✔ 4. **No usar comentarios, imágenes o auto-filtros con SXSSF**
Estos elementos están parcialmente soportados y generan XML no estándar.

✔ 5. **Si escribes más de 1 millón de filas, definir el número de filas en memoria:**

```java
SXSSFWorkbook wb = new SXSSFWorkbook(500); // 500 rows en memoria
wb.setCompressTempFiles(true); // Opcional: comprime XML temporal
```

---

### 💡 **Alternativa híbrida: XSSFWorkbook + Streaming manual**

Si necesitas compatibilidad perfecta con LibreOffice y archivos grandes:

* Genera el libro con `XSSFWorkbook` (100% compatible)
* Divide en múltiples hojas si supera las 200k filas
* O usa `poi-ooxml-lite` para reducir consumo de memoria

---

### 🎯 **Conclusión**

✅ SXSSFWorkbook es excelente para archivos grandes,
❌ pero puede generar `.xlsx` que **LibreOffice marca como corruptos** si no se respetan sus limitaciones.

📌 **Solución ideal**:

* Usar SXSSFWorkbook solo para datos simples (sin estilos complejos, imágenes, ni fórmulas),
* O usar XSSFWorkbook si necesitas máxima compatibilidad y formato.

---




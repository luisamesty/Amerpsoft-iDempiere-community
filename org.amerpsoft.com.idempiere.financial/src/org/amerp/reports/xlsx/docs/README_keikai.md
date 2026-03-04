Sí, esto tiene una explicación clara: **Keikai utiliza internamente un motor similar a LibreOffice para validar y abrir los archivos Excel**.
Si LibreOffice lo marca como “corrupto” y Keikai también, entonces el problema **no es de Keikai**, sino del **archivo generado por SXSSFWorkbook**, que contiene algún XML no estándar o incompleto.

---

## ✅ ¿Qué puedes hacer para evitar archivos dañados con SXSSFWorkbook + iDempiere + Keikai?

### 🔹 1. **Confirmar si el problema es SXSSF (streaming)**

Si pruebas con **XSSFWorkbook** (modo normal) y Keikai abre el Excel sin errores, entonces ya sabemos con certeza que el problema es por SXSSF.

📌 **Recomendación:**
Usa SXSSFWorkbook solo si realmente tienes más de **200.000 filas** o riesgo de `OutOfMemoryError`.
Para reportes medianos (10.000 a 100.000 filas) es mejor **XSSFWorkbook** → 100 % compatible con Keikai.

---

### 🔹 2. **Si necesitas SXSSFWorkbook sí o sí (grandes volúmenes), sigue estas reglas:**

| Problema frecuente                    | Solución recomendada                                    |
| ------------------------------------- | ------------------------------------------------------- |
| Estilos dentro del bucle              | Crear estilos 1 sola vez, fuera del `for`               |
| No se cierra bien el archivo          | Siempre usar `wb.write()`, `wb.dispose()`, `wb.close()` |
| Fórmulas                              | Activar `sheet.setForceFormulaRecalculation(true)`      |
| Caracteres especiales (&, <, >)       | Usar `StringEscapeUtils.escapeXml11()`                  |
| Comentarios o imágenes                | **NO están soportados en SXSSF**                        |
| Auto-size column (`autoSizeColumn()`) | Rompe el XML con SXSSF, evitarlo completamente          |

---

### ✅ **Plantilla correcta de exportación (sin archivo corrupto)**

```java
// Workbook en streaming (solo si es necesario por tamaño)
SXSSFWorkbook wb = new SXSSFWorkbook(200); // 200 filas en memoria
wb.setCompressTempFiles(true); // Opcional, mejora el XML

Sheet sheet = wb.createSheet("Datos");

// 1. Crear estilos SOLO una vez
CellStyle headerStyle = wb.createCellStyle();
Font headerFont = wb.createFont();
headerFont.setBold(true);
headerStyle.setFont(headerFont);

// 2. Escribir el header
Row header = sheet.createRow(0);
Cell cell = header.createCell(0);
cell.setCellValue("Columna 1");
cell.setCellStyle(headerStyle);

// 3. Escribir datos (sin crear estilos nuevos dentro del bucle)
for (int i = 1; i < 100000; i++) {
    Row r = sheet.createRow(i);
    r.createCell(0).setCellValue("Dato " + i);
}

// 4. Preparar para cálculo de fórmulas (si tienes)
sheet.setForceFormulaRecalculation(true);

// 5. Grabar y limpiar correctamente
FileOutputStream out = new FileOutputStream("/tmp/reporte.xlsx");
wb.write(out);
out.close();
wb.dispose(); // Limpia archivos temporales del streaming
wb.close();
```

---

### 🔹 3. **Alternativa más estable para iDempiere + Keikai**

Si necesitas fiabilidad total:

✅ Genera el Excel con **XSSFWorkbook (no streaming)**.
✅ Y si hay muchas filas → **crear varias hojas de 100 000 filas** o paginar como hace Keikai.
✅ Keikai permite cargar datos dinámicamente desde la BD sin generar el XLSX completo en disco (más eficiente todavía).

Si quieres, te puedo preparar un ejemplo:
✅ Exportación directa de ResultSet a Keikai **sin generar archivo XLSX**, o
✅ **Exportar desde iDempiere → File → Excel (estilo Jasper/ReportEngine)**, que sí es 100 % compatible.

---

### 🎯 **Conclusión**

✔ El error no es de Keikai, sino del archivo generado por SXSSFWorkbook.
✔ Si lo abres con XSSFWorkbook y funciona → confirma el diagnóstico.
✔ O usas SXSSF con mucho cuidado (limitaciones), o cambias a XSSF o generación directa Keikai.

---


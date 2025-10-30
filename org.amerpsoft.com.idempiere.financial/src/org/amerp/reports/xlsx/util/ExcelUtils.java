package org.amerp.reports.xlsx.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utilidades para creación de estilos y celdas en reportes Excel.
 * Reutilizable en todos los reportes XLSX del proyecto Amerpsoft.
 */
public class ExcelUtils {

    // --- Crear estilo de celda con tamaño y negrita opcional
    public static CellStyle createStyle(SXSSFWorkbook workbook, int fontSize, boolean bold) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setBold(bold);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);
        return style;
    }

    // --- Crear una celda con valor y estilo
    public static void createStyledCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    // --- Valor seguro (evita NullPointerException)
    public static String safeString(String s) {
        return s != null ? s : "";
    }

    // --- Conversión de píxeles a EMUs (para imágenes o anclajes)
    public static int pixelToEMU(int pixels) {
        return (int) (pixels * 9525); // 1 pixel = 9525 EMUs
    }

    // --- Actualizar longitud máxima (para ajustar ancho de columnas)
    public static void updateMaxLen(int[] maxLen, int col, String val) {
        if (val != null) {
            maxLen[col] = Math.max(maxLen[col], val.length());
        }
    }

    // --- Ajustar anchos de columnas según longitudes máximas
    public static void adjustColumnWidths(Sheet sheet, int[] maxLen, int[] minWidth) {
        for (int i = 0; i < maxLen.length; i++) {
            int width = Math.max(maxLen[i], minWidth != null && i < minWidth.length ? minWidth[i] : 10);
            sheet.setColumnWidth(i, width * 256); // ancho en unidades Excel
        }
    }
}

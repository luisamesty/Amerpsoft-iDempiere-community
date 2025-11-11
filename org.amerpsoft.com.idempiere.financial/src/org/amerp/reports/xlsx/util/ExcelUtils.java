package org.amerp.reports.xlsx.util;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utilidades para creación de estilos y celdas en reportes Excel.
 * Reutilizable en todos los reportes XLSX del proyecto Amerpsoft.
 */
public class ExcelUtils {

    // --- Crear estilo de celda con tamaño y negrita opcional
    public static CellStyle createStyle(XSSFWorkbook workbook, int fontSize, boolean bold) {
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setBold(bold);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
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
    /**
     * Crea una celda y establece su valor como numérico (BigDecimal).
     */
    public static void createStyledCell(Row row, int colIndex, BigDecimal value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        if (value != null) {
            // POI usa double para valores numéricos.
            cell.setCellValue(value.doubleValue()); 
        } else {
            cell.setCellValue(0.0); // O deja en blanco si prefieres
        }
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

    /**
     * Añade una sangría (espacios) al lado izquierdo de la cadena 
     * basada en el nivel jerárquico.
     * * @param codigo El código de la cuenta.
     * @param level El nivel de profundidad de la cuenta (1, 2, 3, etc.).
     * @return El código con la sangría aplicada.
     */
    public static String padLeft(String codigo, int level) {
        if (codigo == null || codigo.isEmpty()) {
            return "";
        }
        
        // Determina el número de espacios. 
        // Se resta 1 o 2 para que el nivel más alto (ej: nivel 1) tenga poca o ninguna sangría.
        // Usamos Math.max para asegurar que el nivel no sea negativo.
        int indentLevel = Math.max(0, level - 2); 
        
        // Multiplica el nivel de sangría por un factor (ej: 3 espacios por nivel)
        int numSpaces = indentLevel * 3; 

        // Crea la cadena de espacios
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numSpaces; i++) {
            sb.append(" ");
        }
        
        // Retorna la sangría más el código
        return sb.append(codigo).toString();
    }
}

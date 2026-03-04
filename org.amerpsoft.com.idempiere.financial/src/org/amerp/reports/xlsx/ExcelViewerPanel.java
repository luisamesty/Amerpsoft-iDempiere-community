package org.amerp.reports.xlsx;

import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.adempiere.webui.component.*;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.window.Dialog;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

public class ExcelViewerPanel extends Div {

    private static final long serialVersionUID = 1L;

    private String filePath;
    private String[] columnHeaders;
    private int[] maxLen;
    private int headerRowCount;
    private int maxVisibleRows;
    
    public ExcelViewerPanel(String filePath,  String[] columnHeaders, int[] maxLen, int headerRowCount, int maxVisibleRows) {
        this.filePath = filePath;
        this.columnHeaders = columnHeaders;
        this.maxLen = maxLen;
        this.headerRowCount = headerRowCount;
        this.maxVisibleRows = maxVisibleRows;
        // Traduce los elementos del arreglo columnHeaders y reemplaza los originales
        for (int i = 0; i < columnHeaders.length; i++) {
            String translated = Msg.getElement(Env.getCtx(), columnHeaders[i]);
            columnHeaders[i] = translated;
        }
        setVflex("1");
        setHflex("1");
        setStyle("overflow:auto; padding:6px; background:white;");

        loadExcel();
    }

    private void loadExcel() {
        try (FileInputStream fis = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            // DataFormatter
            DataFormatter cellFormatter = new DataFormatter();
            
            // --- Toolbar encima del grid
            Div toolbar = new Div();
            toolbar.setStyle("padding:4px; background-color:#f5f5f5; border-bottom:1px solid #ccc; display:flex; gap:4px; align-items:center;");

            // Botones de navegación
            Button btnStart      = new Button("Inicio");
            btnStart.setImage(ThemeManager.getThemeResource("images/wfStart24.png"));
            Button btnPagePrev   = new Button("Página -");

            Button btnPrevLine   = new Button("Línea");
            btnPrevLine.setImage(ThemeManager.getThemeResource("images/wfBack24.png"));
            Button btnNextLine   = new Button("Línea");
            btnNextLine.setImage(ThemeManager.getThemeResource("images/wfNext24.png"));
            Button btnPageNext   = new Button("Página +");
            Button btnEnd        = new Button("Fin");
            btnEnd.setImage(ThemeManager.getThemeResource("images/wfEnd24.png"));
            Label lblCurrentLine = new Label("Línea: 1");
            
            // Añadir todos al toolbar
            toolbar.appendChild(btnStart);
            toolbar.appendChild(btnPagePrev);
            toolbar.appendChild(btnPrevLine);
            lblCurrentLine.setStyle("margin-left:10px; font-weight:bold;");
            toolbar.appendChild(lblCurrentLine);
            toolbar.appendChild(btnNextLine);
            toolbar.appendChild(btnPageNext);
            toolbar.appendChild(btnEnd);

            // Agregar toolbar al panel antes del container con los grids
            this.appendChild(toolbar);

            // Contenedores para top y body
            Div container = new Div();
            container.setVflex("1");
            container.setHflex("1");

            Grid topGrid = new Grid();
            topGrid.setVflex("min");
            topGrid.setHflex("1");
            Grid bodyGrid = new Grid();
            bodyGrid.setVflex("1");
            bodyGrid.setHflex("1");

            // --- Columnas (para ambos grids)
            Columns topColumns = new Columns();
            Columns bodyColumns = new Columns();
            // Factor de conversión de Caracteres a Ancho ZK (em)
            // 0.85 a 0.90 es un buen rango para fuentes monospace/proporcionales.
            final double CHAR_TO_EM_FACTOR = 1.05; 
            // PADDING FIJO: Caracteres de holgura que se añaden a TODAS las columnas.
            final int PADDING_CHARS = 5;
            for (int i = 0; i < maxLen.length; i++) {
                // Obtener el ancho de caracteres deseado (ej. 15, 30, 10...)
                int desiredChars = maxLen[i];
                // Calcular el ancho base (caracteres reales + padding)
                double baseWidth = desiredChars + PADDING_CHARS; 
                // Aplicar el factor de escala, y asegurar un MINIMO (ej. 6 caracteres)
                double emWidth = Math.max(6.0, baseWidth) * CHAR_TO_EM_FACTOR; 
                // Formatear la cadena con dos decimales para ZK
                String width = String.format("%.2f", emWidth) + "em";

                Column colTop = new Column();
                colTop.setWidth(width);
                topColumns.appendChild(colTop);

                Column colBody = new Column();
                colBody.setWidth(width);
                bodyColumns.appendChild(colBody);
            }
            topGrid.appendChild(topColumns);
            bodyGrid.appendChild(bodyColumns);

            // --- Filas
            Rows topRows = new Rows();
            Rows bodyRows = new Rows();

            for (org.apache.poi.ss.usermodel.Row excelRow : sheet) {
                if (rowCount > maxVisibleRows) break;

                org.adempiere.webui.component.Row zkRow = new org.adempiere.webui.component.Row();
                // Esto intenta anular el estilo de la tabla/grid de ZK.
                zkRow.setStyle("min-height:14px !important; height:14px !important;");
                int cellCount = Math.min(excelRow.getLastCellNum(), maxLen.length);

                for (int i = 0; i < cellCount; i++) {
                    Cell cell = excelRow.getCell(i, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // Si es la fila de títulos, reemplaza por el encabezado traducido
                    String text;
                    if (rowCount == headerRowCount && i < columnHeaders.length) {
                        text = columnHeaders[i];
                    } else {
                        text = getCellValue(cell, cellFormatter);
                    }
                    // 
                    Label lbl = new Label(text);

                    boolean isTop = rowCount <= headerRowCount;        // filas 0-headerRowCount van al top
                    boolean isTitle = rowCount == headerRowCount;      // fila headerRowCount como título
                    lbl.setStyle(buildCellStyle(cell, workbook, isTop, isTitle));

                    zkRow.appendChild(lbl);
                }

                if (rowCount <= 4) topRows.appendChild(zkRow);
                else bodyRows.appendChild(zkRow);

                rowCount++;
            }

            topGrid.appendChild(topRows);
            bodyGrid.appendChild(bodyRows);

            container.appendChild(topGrid);
            container.appendChild(bodyGrid);
            this.appendChild(container);

            // NUEVO --- Scroll del bodyGrid
            bodyGrid.setVflex("1");
            bodyGrid.setHflex("1");
            bodyGrid.setStyle("overflow:auto;");

            // Guardamos las filas del body
            List<org.adempiere.webui.component.Row> bodyRowList = bodyRows.getChildren().stream()
                    .map(r -> (org.adempiere.webui.component.Row) r)
                    .collect(Collectors.toList());

            final int[] currentIndex = {0}; // fila visible inicial

            final int pageSize = 20;

	         // Función auxiliar para mostrar filas actuales
	         Runnable showCurrentRows = () -> {
	             for (int i = 0; i < bodyRowList.size(); i++) {
	                 bodyRowList.get(i).setVisible(i >= currentIndex[0] && i < currentIndex[0] + pageSize);
	             }
	          // Sumamos headerRowCount (ej. 5) y 1 (porque el índice inicia en 0)
	             int displayLine = currentIndex[0] + headerRowCount + 1; 
	             lblCurrentLine.setValue(Msg.getElement(Env.getCtx(), "Line") +": "+ displayLine+"/"+bodyRowList.size());
	         };
	         
	        // Inicializa mostrando las primeras filas
	        showCurrentRows.run();
            // Botones de navegación
            btnStart.addEventListener(Events.ON_CLICK, e -> {
                currentIndex[0] = 0;
                showCurrentRows.run();
            });

            btnPrevLine.addEventListener(Events.ON_CLICK, e -> {
                if (currentIndex[0] > 0) currentIndex[0]--;
                showCurrentRows.run();
            });

            btnNextLine.addEventListener(Events.ON_CLICK, e -> {
                if (currentIndex[0] < bodyRowList.size() - 1) currentIndex[0]++;
                showCurrentRows.run();
            });

            btnEnd.addEventListener(Events.ON_CLICK, e -> {
                currentIndex[0] = bodyRowList.size() - 1;
                showCurrentRows.run();
            });

            // Para página completa (ej. 20 filas)
 
            btnPagePrev.addEventListener(Events.ON_CLICK, e -> {
                currentIndex[0] = Math.max(0, currentIndex[0] - pageSize);
                showCurrentRows.run();
            });

            btnPageNext.addEventListener(Events.ON_CLICK, e -> {
                currentIndex[0] = Math.min(bodyRowList.size() - 1, currentIndex[0] + pageSize);
                showCurrentRows.run();
            });


        } catch (Exception e) {
            Dialog.error(0, "Error leyendo Excel: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell, DataFormatter formatter) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                String val = cell.getStringCellValue();
                if (val == null) return "";
                return val.replace(" ", "\u00a0"); // NBSP
            case NUMERIC:
            	// Usar DataFormatter. Esto aplica el formato de Excel (#,##0.00)
                return formatter.formatCellValue(cell).replace(" ", "\u00a0");
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }

    private String buildCellStyle(Cell cell, XSSFWorkbook workbook, boolean isHeader, boolean isTitle) {
    	// Reducir padding al mínimo
        StringBuilder css = new StringBuilder("padding:0px 4px;"); 
        // Forzar el Label a ser un bloque completo
        css.append("display:block;");
        // Forzar la altura de línea a un valor pequeño (ej. 1.0)
        css.append("line-height:1.0;");
        css.append("height:12px !important;");
        if (isHeader) css.append("font-weight:bold; background-color:#e0e0e0;");
        else if (isTitle) css.append("font-weight:bold; background-color:#f0f0f0;");

        XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();
        // Determinar el tipo de contenido de la celda
        boolean isNumeric = cell.getCellType() == CellType.NUMERIC;
        // Obtener el valor de texto para la verificación secundaria
        String cellValue = getCellValue(cell, new DataFormatter());
        
        // Lógica de Alineación
        switch (style.getAlignment()) {
	        case RIGHT:
	        case FILL: 
	        case DISTRIBUTED:
	            css.append("text-align:right !important;"); 
	            break;
	        // ... (otros casos con !important) ...
	        case GENERAL:
	        default:
	            if (isNumeric) {
	                // Forzamos la alineación a la DERECHA
	                css.append("text-align:right !important;"); 
	            } else {
	                css.append("text-align:left !important;");
	            }
	            break;
	    }
        XSSFFont font = style.getFont();
        if (font != null) {
            css.append("font-family:").append(font.getFontName()).append(";");
            css.append("font-size:").append(font.getFontHeightInPoints()).append("pt;");
            if (font.getBold()) css.append("font-weight:bold;");
            if (font.getItalic()) css.append("font-style:italic;");
            XSSFColor color = font.getXSSFColor();
            if (color != null && color.getRGB() != null) {
                byte[] rgb = color.getRGB();
                css.append(String.format("color:rgb(%d,%d,%d);", rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF));
            }
        }

        XSSFColor bg = style.getFillForegroundXSSFColor();
        if (bg != null && bg.getRGB() != null) {
            byte[] rgb = bg.getRGB();
            css.append(String.format("background-color:rgb(%d,%d,%d);", rgb[0] & 0xFF, rgb[1] & 0xFF, rgb[2] & 0xFF));
        }

        return css.toString();
    }
    
    /**
     * Verifica si una cadena de texto tiene formato de número (ej: 12,345.67 o 2024-01-01).
     * Esta es una verificación simplificada para nuestro propósito.
     */
    private boolean isLikelyNumber(String s) {
        if (s == null || s.isEmpty()) return false;
        
        // Elimina caracteres de formato comunes que no son dígitos (separadores de miles)
        String cleanS = s.replaceAll("[^0-9\\.,\\-]", ""); 
        
        // Verifica si es un número válido. Usamos try-catch para ser robustos.
        try {
            // Se puede usar Double.parseDouble o verificar si cumple con un patrón numérico
            // Aquí, buscamos un patrón simple: contiene al menos un dígito
            return cleanS.matches(".*[0-9].*"); 
        } catch (Exception e) {
            return false;
        }
    }
}

package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileInputStream;

import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.window.Dialog;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

public class ExcelViewerPanel extends Div {

    private static final long serialVersionUID = 1L;

    private String filePath;
    private Grid grid = new Grid();
    private Rows rows = new Rows();
    private Button btnRefresh = new Button("Refrescar");
    private Button btnClose = new Button("Cerrar");

    public ExcelViewerPanel(String filePath) {
        this.filePath = filePath;
        setVflex("1");
        setHflex("1");
        setStyle("overflow:auto; padding:6px; background:white;");

        initToolbar();
        initGrid();
        loadExcel();
    }

    private void initToolbar() {
        Div toolbar = new Div();
        toolbar.setStyle("padding:4px; background-color:#f5f5f5; border-bottom:1px solid #ccc;");

        btnRefresh.addEventListener(Events.ON_CLICK, evt -> {
            rows.getChildren().clear();
            loadExcel();
        });
        btnClose.addEventListener(Events.ON_CLICK, evt -> this.detach());

        toolbar.appendChild(btnRefresh);
        toolbar.appendChild(btnClose);
        this.appendChild(toolbar);
    }

    private void initGrid() {
        grid.setSizedByContent(true);
        grid.setVflex("1");
        grid.setHflex("1");
        grid.appendChild(rows);
        this.appendChild(grid);
    }

    private void loadExcel() {
        File file = new File(filePath);
        if (!file.exists()) {
            Dialog.error(0, "Archivo no encontrado:\n" + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;
            int rowCount = 0;
            int maxVisibleRows = 500; // límite filas visibles

            for (org.apache.poi.ss.usermodel.Row excelRow : sheet) {
                if (rowCount++ > maxVisibleRows) break;

                Row zkRow = new Row();
                int cellCount = excelRow.getLastCellNum();

                for (int i = 0; i < cellCount; i++) {
                    Cell cell = excelRow.getCell(i, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String text = getCellValue(cell);

                    Label lbl = new Label(text);

                    StringBuilder style = new StringBuilder("padding:1px 4px;");

                    // --- fuente
                    Font font = getFont(cell, workbook);
                    if (font != null) {
                        style.append("font-family:").append(font.getFontName()).append(";");
                        style.append("font-size:").append(font.getFontHeightInPoints()).append("pt;");
                        if (font.getBold()) style.append("font-weight:bold;");
                        if (font.getItalic()) style.append("font-style:italic;");
                        String fontColor = getFontColor(cell, workbook);
                        if (fontColor != null) style.append("color:").append(fontColor).append(";");
                    }

                    // --- fondo celda
                    String bgColor = getBackgroundColor(cell);
                    if (bgColor != null) style.append("background-color:").append(bgColor).append(";");

                    // --- header resaltado
                    if (isHeader) style.append("font-weight:bold; background-color:#f0f0f0;");

                    lbl.setStyle(style.toString());
                    zkRow.appendChild(lbl);
                }

                rows.appendChild(zkRow);
                isHeader = false;
            }

        } catch (Exception e) {
            Dialog.error(0, "Error leyendo Excel: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                    return cell.getDateCellValue().toString();
                else
                    return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }

    private Font getFont(Cell cell, Workbook workbook) {
        if (cell == null || workbook == null) return null;
        CellStyle style = cell.getCellStyle();
        if (style == null) return null;

        if (workbook instanceof org.apache.poi.xssf.usermodel.XSSFWorkbook) {
            org.apache.poi.xssf.usermodel.XSSFFont xFont = ((org.apache.poi.xssf.usermodel.XSSFWorkbook) workbook)
                    .getFontAt(style.getFontIndexAsInt());
            return xFont;
        } else {
            return workbook.getFontAt(style.getFontIndexAsInt());
        }
    }

    private String getFontColor(Cell cell, Workbook workbook) {
        if (cell == null || workbook == null) return null;
        if (workbook instanceof org.apache.poi.xssf.usermodel.XSSFWorkbook) {
            org.apache.poi.xssf.usermodel.XSSFCellStyle xStyle =
                    (org.apache.poi.xssf.usermodel.XSSFCellStyle) cell.getCellStyle();
            XSSFColor color = xStyle.getFont().getXSSFColor();
            if (color != null && color.getARGBHex() != null) {
                return "#" + color.getARGBHex().substring(2);
            }
        } else if (workbook instanceof org.apache.poi.hssf.usermodel.HSSFWorkbook) {
            org.apache.poi.hssf.usermodel.HSSFCellStyle hStyle =
                    (org.apache.poi.hssf.usermodel.HSSFCellStyle) cell.getCellStyle();
            org.apache.poi.hssf.usermodel.HSSFFont hFont =
                    ((org.apache.poi.hssf.usermodel.HSSFWorkbook) workbook).getFontAt(hStyle.getFontIndex());
            short colorIndex = hFont.getColor();
            HSSFColor hColor = HSSFColor.getIndexHash().get(colorIndex);
            if (hColor != null) {
                short[] rgb = hColor.getTriplet();
                return String.format("rgb(%d,%d,%d)", rgb[0], rgb[1], rgb[2]);
            }
        }
        return null;
    }

    private String getBackgroundColor(Cell cell) {
        if (cell == null) return null;
        CellStyle style = cell.getCellStyle();
        if (style == null) return null;

        if (style instanceof org.apache.poi.xssf.usermodel.XSSFCellStyle) {
            XSSFColor bg = ((org.apache.poi.xssf.usermodel.XSSFCellStyle) style).getFillForegroundXSSFColor();
            if (bg != null && bg.getARGBHex() != null)
                return "#" + bg.getARGBHex().substring(2);
        } else if (style instanceof org.apache.poi.hssf.usermodel.HSSFCellStyle) {
            org.apache.poi.hssf.usermodel.HSSFCellStyle hStyle = (org.apache.poi.hssf.usermodel.HSSFCellStyle) style;
            short colorIndex = hStyle.getFillForegroundColor();
            HSSFColor hColor = HSSFColor.getIndexHash().get(colorIndex);
            if (hColor != null) {
                short[] rgb = hColor.getTriplet();
                return String.format("rgb(%d,%d,%d)", rgb[0], rgb[1], rgb[2]);
            }
        }
        return null;
    }
}

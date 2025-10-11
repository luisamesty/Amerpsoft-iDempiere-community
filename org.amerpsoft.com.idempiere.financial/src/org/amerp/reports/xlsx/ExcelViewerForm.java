package org.amerp.reports.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.panel.CustomForm;
import org.apache.poi.ss.usermodel.*;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;


public class ExcelViewerForm extends CustomForm {

    private static final long serialVersionUID = 1L;

    private Borderlayout mainLayout = new Borderlayout();
    private Center center = new Center();
    private Grid grid = new Grid();
    private Rows rows = new Rows();
    private String filePath;

    public ExcelViewerForm(String filePath) {
        this.filePath = filePath;
        initForm();
    }

    protected void initForm() {
        this.setHeight("100%");
        this.setWidth("100%");

        mainLayout.setHeight("100%");
        mainLayout.setWidth("100%");
        this.appendChild(mainLayout);

        center.setAutoscroll(true);
        center.appendChild(grid);
        grid.appendChild(rows);
        mainLayout.appendChild(center);

        loadExcel();
    }

    private void loadExcel() {
        File file = new File(filePath);
        if (!file.exists()) {
            Messagebox.show("Archivo no encontrado:\n" + filePath, "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;

            for (org.apache.poi.ss.usermodel.Row excelRow : sheet) {
                Row zkRow = new Row();
                for (int i = 0; i < excelRow.getLastCellNum(); i++) {
                	Cell cell = excelRow.getCell(i, MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = getCellValue(cell);
                    Label lbl = new Label(value);
                    if (isHeader) {
                        lbl.setStyle("font-weight:bold; background-color:#ddeeff; border:1px solid #ccc; padding:2px;");
                    } else {
                        lbl.setStyle("border:1px solid #eee; padding:2px;");
                    }
                    zkRow.appendChild(lbl);
                }
                rows.appendChild(zkRow);
                isHeader = false;
            }

        } catch (IOException e) {
            Messagebox.show("Error leyendo el archivo Excel:\n" + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toLocalDate().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }

    public static void openViewer(String filePath) {
        ExcelViewerForm form = new ExcelViewerForm(filePath);
        // Esto abre en ventana modal de iDempiere
        AEnv.showWindow(form);
    }
}

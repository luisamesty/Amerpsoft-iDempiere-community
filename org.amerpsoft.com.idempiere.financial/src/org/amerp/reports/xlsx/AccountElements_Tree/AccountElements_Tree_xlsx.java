package org.amerp.reports.xlsx.AccountElements_Tree;

import java.io.File;
import java.io.FileOutputStream; // Necessary for writing the Excel file
import java.util.List;
import java.util.logging.Level;

import org.amerp.reports.AccountElement;
import org.amerp.reports.DataPopulator;
import org.compiere.model.PrintInfo;
import org.compiere.process.ClientProcess;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AccountElements_Tree_xlsx extends SvrProcess implements ProcessCall, ClientProcess{
	
    private static final CLogger log = CLogger.getCLogger(AccountElements_Tree_xlsx.class);

    private int p_AD_Client_ID = 0;
    private int p_C_AcctSchema_ID = 0;
	private PrintInfo    printInfo;
	ProcessInfo processInfo;
    // Ruta archivo .jasper
	String jrxmlPath ="";

    @Override
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (ProcessInfoParameter pp : para) {
            String name = pp.getParameterName();
            if (name.equals("AD_Client_ID")) {
                p_AD_Client_ID = pp.getParameterAsInt();
            } else if (name.equals("C_AcctSchema_ID")) {
                p_C_AcctSchema_ID = pp.getParameterAsInt();
            }
        }
        // Si no se pasaron como parámetro, obtener del contexto actual de iDempiere
        if (p_AD_Client_ID == 0) {
            p_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
        }
        // Para C_AcctSchema_ID, si no viene como parámetro, podría requerir lógica para obtener el predeterminado
        // o ser un parámetro obligatorio en la ventana del proceso. Aquí lo dejamos como 0 si no se pasa.
    }

    @Override
    protected String doIt() throws Exception {
        log.info("Iniciando generación de informe de Árbol de Cuentas para Cliente: " + p_AD_Client_ID + ", Esquema Contable: " + p_C_AcctSchema_ID);

        ProcessInfo pInfo = getProcessInfo();
        if (pInfo == null) {
            throw new IllegalStateException("ProcessInfo no inicializado. Este proceso debe ejecutarse desde el entorno de iDempiere.");
        }

        // --- PASO 1: Obtener datos ---
        List<AccountElement> reportData = DataPopulator.getAccountElementsFromiDempiereDB(p_AD_Client_ID, p_C_AcctSchema_ID);
        if (reportData.isEmpty()) {
            return "No se encontraron datos para el informe.";
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Account Elements");
        String[] headers = {"Level", "Value", "Name", "Description", "Account Type", "Is Summary", "Value Parent"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (AccountElement e : reportData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getLevel() != null ? e.getLevel() : 0);
            row.createCell(1).setCellValue(e.getValue());
            row.createCell(2).setCellValue(e.getName());
            row.createCell(3).setCellValue(e.getDescription());
            row.createCell(4).setCellValue(e.getAccounttype());
            row.createCell(5).setCellValue(e.getIssummary());
            row.createCell(6).setCellValue(e.getValue_parent());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // --- PASO 2: Guardar ---
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFile = new File(tempDir, pInfo.getTitle() + "_" + System.currentTimeMillis() + ".xlsx");

        try (FileOutputStream fileOut = new FileOutputStream(tempFile)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        log.info("Excel generado en: " + tempFile.getAbsolutePath());

        // --- PASO 3: Entregar a iDempiere ---
        pInfo.setExportFile(tempFile);
        pInfo.setExportFileExtension("xlsx");

        // Si estás depurando desde Eclipse:
        // org.compiere.print.ReportViewerProvider.openViewer("Excel", pInfo);

        return "Informe Árbol de Cuentas generado con éxito.";
    }

}
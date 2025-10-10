package org.amerp.reports.jasper.AccountElements_Tree;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream; // Importar para cargar el .jasper
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.adempiere.base.Service;
import org.adempiere.report.jasper.JRViewerProvider;
import org.amerp.reports.AccountElement;
import org.amerp.reports.DataPopulator;
import org.amerp.reports.JasperUtils;
import org.compiere.model.PrintInfo;
import org.compiere.process.ClientProcess;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

import net.sf.jasperreports.engine.JRException; // Importar si no está
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class AccountElements_Tree_Pojo extends SvrProcess implements ProcessCall, ClientProcess{
	
    private static final CLogger log = CLogger.getCLogger(AccountElements_Tree_Pojo.class);

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

        // --- PASO 0: Copia ficheros a temporal
        // Carpeta temporal
        JasperUtils jasperUtils = new JasperUtils();
        String tmpFolder = jasperUtils.getTempFolder();
        JasperReport jasperReport = null;
        
        // Lista de recursos a copiar
        String[] resourcesToCopy = new String[]{
            "org/amerp/reports/jasper/AccountElements_Tree/AccountElements_Tree.jrxml",
            "org/amerp/reports/jasper/AccountElements_Tree/AccountElements_Tree.properties",
            "org/amerp/reports/jasper/AccountElements_Tree/AccountElements_Tree_es.properties",
            "org/amerp/reports/jasper/AccountElements_Tree/AccountElements_Tree_fr.properties"
            // Si hay imágenes o subreports, añádelos aquí
        };

        // Copiar físicamente cada recurso al tmpFolder
        for (String resource : resourcesToCopy) {
        	jasperUtils.copyResourceToTmp(resource, tmpFolder);
        }
        // Prueba que el archivo ahora existe físicamente
        jrxmlPath = tmpFolder + "org_amerp_reports_jasper_AccountElements_Tree" + File.separator + "AccountElements_Tree.jrxml";
        File jrxmlFile = new File(jrxmlPath);
        if (!jrxmlFile.exists()) {
            throw new Exception("No existe el archivo jrxml en tmp: " + jrxmlFile.getAbsolutePath());
        }
        
        // --- PASO 1: Obtener los datos del POJO ---
        List<AccountElement> reportData = DataPopulator.getAccountElementsFromiDempiereDB(p_AD_Client_ID, p_C_AcctSchema_ID);

        if (reportData.isEmpty()) {
            String msg = "No se encontraron datos para el informe con el Cliente: " + p_AD_Client_ID + " y Esquema Contable: " + p_C_AcctSchema_ID;
            log.log(Level.INFO, msg);
            return msg; // Mensaje que se mostrará al usuario en iDempiere
        }
        log.info("Datos obtenidos: " + reportData.size() + " elementos.");

        // --- PASO 2: Compilar y Cargar el archivo .jasper 
        // Se usa la ruta física para compilar el reporte
        String jrxmlPath = tmpFolder + "org_amerp_reports_jasper_AccountElements_Tree" + File.separator + "AccountElements_Tree.jrxml";

        try (InputStream reportStream = new FileInputStream(jrxmlPath)) {
            jasperReport = JasperCompileManager.compileReport(reportStream);    
            log.info("Archivo .jasper cargado exitosamente.");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al cargar y/o compilar el archivo .jrxml desde: " + jrxmlPath, e);
            throw e; // Relanza la excepción para que iDempiere la maneje
        }

        // --- PASO 3: Crear el JRBeanCollectionDataSource a partir de tu lista de POJOs ---
        // ESTE ES EL PUNTO CRÍTICO PARA ASIGNAR TU POJO AL REPORTE
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        log.info("JRBeanCollectionDataSource creado con " + dataSource.getRecordCount() + " registros.");


        // --- PASO 4: Preparar los parámetros del informe (si los tienes) ---
        // Estos parámetros son los que defines en el .jrxml y no son parte de los campos de detalle.
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("AD_Client_ID", p_AD_Client_ID);
        parameters.put("C_AcctSchema_ID",p_C_AcctSchema_ID);
        parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(
        	    "org.amerp.reports.jasper.AccountElements_Tree.AccountElements_Tree",
        	    Locale.getDefault()
        	));
        // Puedes añadir más parámetros, como el logo de la compañía (si no viene del POJO)
        // o información del usuario, etc.
        log.info("Parámetros del informe preparados.");

        // --- PASO 5: Llenar el informe ---
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            log.info("Informe de JasperReports llenado exitosamente.");
        } catch (JRException e) {
            log.log(Level.SEVERE, "Error al llenar el informe de JasperReports.", e);
            throw e; // Relanza la excepción
        }

        // --- PASO 6: Iniciar la visualización/exportación del informe en iDempiere ---
        // El tercer parámetro es el nombre sugerido para el archivo al guardar.
        
        try (Connection conn = DB.getConnectionRO()) {

            // export to viewer
            processInfo = getProcessInfo();
            printInfo = new PrintInfo(processInfo);
			// view the report
			JRViewerProvider viewerLauncher = Service.locator().locate(JRViewerProvider.class).getService();
			viewerLauncher.openViewer(jasperPrint, processInfo.getTitle(), printInfo);
        }

        return "Informe generado con éxito.";
    }
}
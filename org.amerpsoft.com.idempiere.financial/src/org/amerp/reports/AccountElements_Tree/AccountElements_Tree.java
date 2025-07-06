/**
 * @author luisamesty
 *
 */
package org.amerp.reports.AccountElements_Tree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.adempiere.base.IServiceReferenceHolder;
import org.adempiere.base.Service;
import org.adempiere.report.jasper.JRViewerProvider;
import org.compiere.model.PrintInfo;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ServerReportCtl;
import org.compiere.process.ClientProcess;
import org.compiere.process.ProcessCall;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class AccountElements_Tree extends SvrProcess implements ProcessCall, ClientProcess {

    private int p_AD_Client_ID = 0;
    private int p_C_AcctSchema_ID = 0;
    // ATRIBUTO para guardar el JasperPrint
    private JasperPrint jasperPrint;
    ProcessInfoParameter[] pip = null;
	String printerName = null;
	MPrintFormat printFormat = null;   
	private PrintInfo    printInfo;
	ProcessInfo processInfo;
	public static final String IDEMPIERE_REPORT_TYPE = "IDEMPIERE_REPORT_TYPE";
	
    @Override
    protected void prepare() {
    	// Parametros delreporte
    	pip = getParameter();
        for (ProcessInfoParameter param : pip) {
            String name = param.getParameterName();
            if (param.getParameter() == null)
                ;
            else if ("AD_Client_ID".equalsIgnoreCase(name))
                p_AD_Client_ID = param.getParameterAsInt();
            else if ("C_AcctSchema_ID".equalsIgnoreCase(name))
                p_C_AcctSchema_ID = param.getParameterAsInt();
            else
                log.severe("Unknown Parameter: " + name);
        }
        // Additional Parameters
    	if (pip!=null) {
    		for (int i=0; i<pip.length; i++) {
    			if (ServerReportCtl.PARAM_PRINT_FORMAT.equalsIgnoreCase(pip[i].getParameterName())) {
    				printFormat = (MPrintFormat)pip[i].getParameter();
    			}
    			if (ServerReportCtl.PARAM_PRINT_INFO.equalsIgnoreCase(pip[i].getParameterName())) {
    				printInfo = (PrintInfo)pip[i].getParameter();
    			}
    			if (ServerReportCtl.PARAM_PRINTER_NAME.equalsIgnoreCase(pip[i].getParameterName())) {
    				printerName = (String)pip[i].getParameter();
    			}
    		}
    	}
        processInfo = getProcessInfo();
        log.info("Process prepared with AD_Client_ID=" + p_AD_Client_ID + "  and C_AcctSchema_ID=" + p_C_AcctSchema_ID);
    }

    @Override
    protected String doIt() throws Exception {
        // Carpeta temporal
        String tmpFolder = System.getProperty("java.io.tmpdir") + File.separator + "idempiere_reports" + File.separator;

        // Crear carpeta base
        File tmpFolderFile = new File(tmpFolder);
        if (!tmpFolderFile.exists()) {
            boolean created = tmpFolderFile.mkdirs();
            if (!created) {
                throw new Exception("No se pudo crear el directorio temporal: " + tmpFolder);
            }
        }

        // Lista de recursos a copiar
        String[] resourcesToCopy = new String[]{
            "org/amerp/reports/AccountElements_Tree/AccountElements_Tree.jrxml",
            "org/amerp/reports/AccountElements_Tree/AccountElements_Tree.properties",
            "org/amerp/reports/AccountElements_Tree/AccountElements_Tree_es.properties",
            "org/amerp/reports/AccountElements_Tree/AccountElements_Tree_fr.properties"
            // Si hay imágenes o subreports, añádelos aquí
        };

        // Copiar físicamente cada recurso al tmpFolder
        for (String resource : resourcesToCopy) {
            copyResourceToTmp(resource, tmpFolder);
        }
        
        // Prueba que el archivo ahora existe físicamente
        File jrxmlFile = new File(tmpFolder + "org_amerp_reports_AccountElements_Tree" + File.separator + "AccountElements_Tree.jrxml");
        if (!jrxmlFile.exists()) {
            throw new Exception("No existe el archivo jrxml en tmp: " + jrxmlFile.getAbsolutePath());
        }

        // Ahora puedes usar la ruta física para compilar el reporte
        String jrxmlPath = tmpFolder + "org_amerp_reports_AccountElements_Tree" + File.separator + "AccountElements_Tree.jrxml";
        
        
        try (InputStream reportStream = new FileInputStream(jrxmlPath)) {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Parámetros, conexión, etc.
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("AD_Client_ID", p_AD_Client_ID);
            parameters.put("C_AcctSchema_ID",p_C_AcctSchema_ID);
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, ResourceBundle.getBundle(
            	    "org.amerp.reports.AccountElements_Tree.AccountElements_Tree",
            	    Locale.getDefault()
            	));

            try (Connection conn = DB.getConnectionRO()) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                    // export to viewer
                    processInfo = getProcessInfo();
                    printInfo = new PrintInfo(processInfo);
    				// view the report
    				JRViewerProvider viewerLauncher = Service.locator().locate(JRViewerProvider.class).getService();
    				viewerLauncher.openViewer(jasperPrint, processInfo.getTitle(), printInfo);
           }
        }
        return "@ReportGenerated@";
    }

	private static IServiceReferenceHolder<JRViewerProvider> s_viewerProviderReference = null;
	
	/**
	 * 
	 * @return {@link JRViewerProvider}
	 */
	public static synchronized JRViewerProvider getViewerProvider() {
		JRViewerProvider viewerLauncher = null;
		if (s_viewerProviderReference != null) {
			viewerLauncher = s_viewerProviderReference.getService();
			if (viewerLauncher != null)
				return viewerLauncher;
		}
		IServiceReferenceHolder<JRViewerProvider> viewerReference = Service.locator().locate(JRViewerProvider.class).getServiceReference();
		if (viewerReference != null) {
			viewerLauncher = viewerReference.getService();
			s_viewerProviderReference = viewerReference;
		}
		return viewerLauncher;
	}	
	
    private void copyResourceToTmp(String resourceName, String tmpFolder) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);
        if (resourceUrl == null) {
            throw new IOException("No se encontró el recurso: " + resourceName);
        }
        String localName = resourceName;
        if (localName.startsWith("/")) {
            localName = localName.substring(1);
        }
        // Convertir la carpeta padre en un nombre con _
        String parentPath = localName.substring(0, localName.lastIndexOf("/")).replace("/", "_");
        String fileName = localName.substring(localName.lastIndexOf("/") + 1);

        Path targetFolder = Path.of(tmpFolder, parentPath);
        if (!Files.exists(targetFolder)) {
            Files.createDirectories(targetFolder);
        }
        Path destFile = targetFolder.resolve(fileName);

        try (InputStream in = resourceUrl.openStream()) {
            Files.copy(in, destFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

}

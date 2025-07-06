package org.amerp.amfinancial;

import java.io.InputStream;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import net.sf.jasperreports.engine.JasperCompileManager;

public class Activator implements BundleActivator {
    
	@Override
    public void start(BundleContext context) throws Exception {
        // 1. Forzar la carga de JasperReportsConfig
		System.out.println("✅ Financial Activator: Class org.amerp.reports.JasperReportsConfig loading...");
        Class.forName("org.amerp.reports.JasperReportsConfig");
        System.out.println("✅ Financial Activator: inicializando en arranque del bundle...");
        // 2. Compilar un reporte dummy
        try (InputStream in = getClass().getResourceAsStream("/main/resources/dummy.jrxml")) {
            if (in == null) {
                throw new RuntimeException("dummy.jrxml no encontrado en resources");
            }
            try {
				JasperCompileManager.compileReport(in);
				System.out.println("✅ Financial Activator: Jasper dummy report compilado.");
			} catch (Exception e) {
			    System.out.println("✅ Financial Activator: ⚠ Error compilando dummy.jrxml: ");
			}
        }
	}

	@Override
    public void stop(BundleContext context) throws Exception {
        // Nada que hacer al parar
    }
}
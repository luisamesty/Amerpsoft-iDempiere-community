package org.amerp.amnpersonnel;

import org.adempiere.plugin.utils.Incremental2PackActivator;
import org.osgi.framework.BundleContext;
import net.sf.jasperreports.engine.JasperReportsContext;

public class Activator extends Incremental2PackActivator {
	
	// Aquí se guarda el contexto en una variable estática
    private static BundleContext context;

    // Si se quiere acceder al contexto desde otras clases
    public static BundleContext getBundleContext() {
        return context;
    }
    
	JasperReportsContext jasperReportContext = null;
	
	@Override
    public void start(BundleContext context) throws Exception { 
		
		// Llamada a Incremental2PackActivator
        super.start(context);
		System.out.println("✅ Personnel-Payroll Activator: Bundle started.");
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		 System.out.println("✅ Personnel-Payroll: Bundle stopped.");
		 // Llamada a Incremental2PackActivator
	     super.stop(context);
	     
        // Limpiar la referencia
        Activator.context = null;
	}
}
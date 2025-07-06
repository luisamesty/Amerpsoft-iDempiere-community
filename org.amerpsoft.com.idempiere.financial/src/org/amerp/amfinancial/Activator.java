package org.amerp.amfinancial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import net.sf.jasperreports.engine.JasperReportsContext;

public class Activator implements BundleActivator {
	
	JasperReportsContext jasperReportContext = null;
	
	@Override
    public void start(BundleContext context) throws Exception { 
		System.out.println("✅ Financial Activator: Bundle started.");
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		 System.out.println("✅ Financial Activator: Bundle stopped.");
    }
	
}
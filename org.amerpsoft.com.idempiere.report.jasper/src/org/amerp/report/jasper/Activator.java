package org.amerp.report.jasper;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private static BundleContext context;

    public static BundleContext getBundleContext() {
        return context;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }
}


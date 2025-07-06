package org.amerp.report.jasper;

import org.adempiere.base.IProcessFactory;
import org.adempiere.util.ProcessUtil;
import org.compiere.process.ProcessCall;

/**
 * ProcessFactory to start the ReportStarter class.
 * 
 * Before this Factory was initiated the class was started with the
 * DefaultProcessFactory because its package namespace was exported and joined
 * into the org.compiere.report package of the org.adempiere.base plugin via
 * Split Packages (through the Require-Bundle technique) See
 * http://wiki.osgi.org/wiki/Split_Packages why this is not the best idea.
 * Especially this prevents us from exchange the JasperReports plugin with
 * another implementation.
 * 
 */
public class ProcessFactory implements IProcessFactory {
	
	public static final String AMERP_JASPER_STARTER_CLASS = "org.amerp.report.jasper.ReportStarter";
	public static final String JASPER_STARTER_CLASS_DEPRECATED = "org.compiere.report.ReportStarter";

	@Override
	public ProcessCall newProcessInstance(String className) {
		if (className == null)
			return null;
		/*
		 * Special code to use this as an replacement for the standard
		 * JasperReports starter class
		 */
		 // Si AMERP_JASPER, se devuelve este
	    if (AMERP_JASPER_STARTER_CLASS.equals(className))
	        return new ReportStarter();
		// Si es el starter de iDempiere, se devuelve  el original
	    if (ProcessUtil.JASPER_STARTER_CLASS.equals(className)) {
	        return new org.adempiere.report.jasper.ReportStarter();
	    }
		// this is for compatibility with older installations
		if (JASPER_STARTER_CLASS_DEPRECATED.equals(className))
			return new ReportStarter();
		return null;
	}
}

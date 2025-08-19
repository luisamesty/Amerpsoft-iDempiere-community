package org.amerp.pp.factories;

import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.compiere.util.CLogger;
import java.util.logging.Level;
import org.adempiere.webui.panel.IFormController;

public class AMNFormFactory implements IFormFactory {

	private static final CLogger log = CLogger.getCLogger(AMNFormFactory.class); 

	@Override
	public ADForm newFormInstance(String formName) {

		// ******************************
		// AMNFileImport
		// ******************************
		if (formName.equals("org.amerp.amnforms.AMNFileImport")) {
			Object AMform2 = null;
			String webClassName2="org.amerp.amnforms.AMNFileImport";
			Class<?> clazz2 = null; 
			ClassLoader loader2 = getClass().getClassLoader();
			try
			{
	    		clazz2 = loader2.loadClass(webClassName2);
			}
			catch (Exception e)
			{
				log.log(Level.FINE, "Load Form Class Failed in org.amerp.amnforms.AMNFileImport", e);
			}
			if (clazz2 != null) {
			    try {
			        // Usar el constructor vacío (default constructor)
			        AMform2 = clazz2.getDeclaredConstructor().newInstance();
			    } catch (Exception e) {
			        log.log(Level.FINE, 
			            "Load Form Class Failed in org.amerp.amnforms.AMNPayrollAssistForm", e);
			    }
			}
			if (AMform2 != null) {
				if (AMform2 instanceof ADForm) {
					//log.warning(".....AMform instanceof ADForm....");
					return (ADForm)AMform2;
				} else if (AMform2 instanceof IFormController) {
					//log.warning(".....AAMform instanceof IFormController....");
					IFormController controller = (IFormController) AMform2;
					ADForm adForm = controller.getForm();
					return adForm;
				}
			}
		}	// AMNFileImport
		
		// ******************************
		// AMNPayrollAssistForm
		// ******************************
		if (formName.equals("org.amerp.amnforms.AMNPayrollAssistForm")) {
			Object AMform2 = null;
			String webClassName2="org.amerp.amnforms.AMNPayrollAssistForm";
			Class<?> clazz2 = null; 
			ClassLoader loader2 = getClass().getClassLoader();
			try
			{
	    		clazz2 = loader2.loadClass(webClassName2);
			}
			catch (Exception e)
			{
				log.log(Level.FINE, "Load Form Class Failed in org.amerp.amnforms.AMNPayrollAssistForm", e);
			}
			if (clazz2 != null) {
			    try {
			        // Usar el constructor vacío (default constructor)
			        AMform2 = clazz2.getDeclaredConstructor().newInstance();
			    } catch (Exception e) {
			        log.log(Level.FINE, 
			            "Load Form Class Failed in org.amerp.amnforms.AMNPayrollAssistForm", e);
			    }
			}
			if (AMform2 != null) {
				if (AMform2 instanceof ADForm) {
					return (ADForm)AMform2;
				} else if (AMform2 instanceof IFormController) {
					IFormController controller = (IFormController) AMform2;
					ADForm adForm = controller.getForm();
					return adForm;
				}
			}
		}	// AMNPayrollAssistForm
		// 
		return null;
	}	
}


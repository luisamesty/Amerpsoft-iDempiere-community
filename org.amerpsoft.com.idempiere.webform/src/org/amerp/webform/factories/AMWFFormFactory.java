package org.amerp.webform.factories;

import java.util.logging.Level;

import org.adempiere.base.equinox.EquinoxExtensionLocator;
import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.util.CLogger;

/* SEE DefaultFormFactory */
public class AMWFFormFactory implements IFormFactory {
	
	private static final CLogger log = CLogger.getCLogger(AMWFFormFactory.class); 

	@Override
	public ADForm newFormInstance(String formName) {
		// TODO Auto-generated method stub
		//log.warning(".....Paso General...formName:"+formName);
		// ******************************
		// AMWFPayAllocationMultipleBP
		// ******************************
		if (formName.equals("org.amerp.webform.amwebui.AMWFAllocationMultipleBP")) {
			//log.warning(".....isEqual........formName:"+formName);
			//log.warning(".....Igual....");
			//Object AMform = EquinoxExtensionLocator.instance().locate(Object.class, formName, null).getExtension();		
			Object AMform = null;
			String webClassName="org.amerp.webform.amwebui.AMWFAllocationMultipleBP";
			Class<?> clazz = null; 
			ClassLoader loader = getClass().getClassLoader();
    		try
    		{
        		clazz = loader.loadClass(webClassName);
    		}
    		catch (Exception e)
    		{
    			log.log(Level.FINE, "Load Form Class Failed in org.amerp.webform.amwebui.AMWFAllocationMultipleBP", e);
    		}
			if (clazz != null) {
				//log.warning(".....clazz != null....");
				try
	    		{
					AMform = clazz.newInstance();
	    		}
	    		catch (Exception e)
	    		{
	    			log.log(Level.FINE, "Load Form Class Failed in org.amerp.webform.amwebui.AMWFAllocationMultipleBP", e);	   
	    		}
			}

			if (AMform != null) {
				if (AMform instanceof ADForm) {
//log.warning(".....AMform instanceof ADForm....");
					return (ADForm)AMform;
				} else if (AMform instanceof IFormController) {
//log.warning(".....AAMform instanceof IFormController....");
					IFormController controller = (IFormController) AMform;
					ADForm adForm = controller.getForm();
					return adForm;
				}
			}
		}
		// ******************************
		// AMWFAllocation (Extended)
		// ******************************
		if (formName.equals("org.amerp.webform.amwebui.AMWFAllocation")) {
			//log.warning(".....isEqual........formName:"+formName);
			//log.warning(".....Igual....");
			//Object AMform = EquinoxExtensionLocator.instance().locate(Object.class, formName, null).getExtension();		
			Object AMform = null;
			String webClassName="org.amerp.webform.amwebui.AMWFAllocation";
			Class<?> clazz = null; 
			ClassLoader loader = getClass().getClassLoader();
    		try
    		{
        		clazz = loader.loadClass(webClassName);
    		}
    		catch (Exception e)
    		{
    			log.log(Level.FINE, "Load Form Class Failed in org.amerp.webform.amwebui.AMWFAllocation", e);
    		}
			if (clazz != null) {
				//log.warning(".....clazz != null....");
				try
	    		{
					AMform = clazz.newInstance();
	    		}
	    		catch (Exception e)
	    		{
	    			log.log(Level.FINE, "Load Form Class Failed in org.amerp.webform.amwebui.AMWFAllocation", e);	   
	    		}
			}

			if (AMform != null) {
				if (AMform instanceof ADForm) {
//log.warning(".....AMform instanceof ADForm....");
					return (ADForm)AMform;
				} else if (AMform instanceof IFormController) {
//log.warning(".....AAMform instanceof IFormController....");
					IFormController controller = (IFormController) AMform;
					ADForm adForm = controller.getForm();
					return adForm;
				}
			}
		}

		return null;
	}	
}



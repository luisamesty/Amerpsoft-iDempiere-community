package org.amerp.fin.factories;

import java.util.logging.Level;

import org.adempiere.webui.factory.IFormFactory;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.util.CLogger;

/* SEE DefaultFormFactory */
public class AMFFormFactory implements IFormFactory {
	
	private static final CLogger log = CLogger.getCLogger(AMFFormFactory.class); 

	@Override
	public ADForm newFormInstance(String formName) {
		// TODO Auto-generated method stub
		//log.warning(".....Paso General...formName:"+formName);
		// ******************************
		// AccountElements_Tree_Form
		// ******************************
		if (formName.equals("org.amerp.reports.xlsx.AccountElements_Tree_Form")) {
			//log.warning(".....isEqual........formName:"+formName);
			//log.warning(".....Igual....");
			//Object AMform = EquinoxExtensionLocator.instance().locate(Object.class, formName, null).getExtension();		
			Object AMform = null;
			String webClassName="org.amerp.reports.xlsx.AccountElements_Tree_Form";
			Class<?> clazz = null; 
			ClassLoader loader = getClass().getClassLoader();
    		try
    		{
        		clazz = loader.loadClass(webClassName);
    		}
    		catch (Exception e)
    		{
    			log.log(Level.FINE, "Load Form Class Failed in org.amerp.reports.xlsx.AccountElements_Tree_Form", e);
    		}
			if (clazz != null) {
				//log.warning(".....clazz != null....");
				try
	    		{
					AMform = clazz.newInstance();
	    		}
	    		catch (Exception e)
	    		{
	    			log.log(Level.FINE, "Load Form Class Failed in org.amerp.reports.xlsx.AccountElements_Tree_Form", e);	   
	    		}
			}

			if (AMform != null) {
				if (AMform instanceof ADForm) {
					return (ADForm)AMform;
				} else if (AMform instanceof IFormController) {
					IFormController controller = (IFormController) AMform;
					ADForm adForm = controller.getForm();
					return adForm;
				}
			}
		}
		return null;
	}	
}



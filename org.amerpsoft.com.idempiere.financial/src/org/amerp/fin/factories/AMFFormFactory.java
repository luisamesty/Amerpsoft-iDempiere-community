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
		
		// Lista de formularios a manejar por esta fábrica
		String[] handledForms = {
			"org.amerp.reports.xlsx.AccountElements_Tree_Form",
			"org.amerp.reports.xlsx.FinancialReports_TreeOrg_Form"
		};
		
		// Comprobar si el formName solicitado está en la lista
		for (String webClassName : handledForms) {
			if (formName.equals(webClassName)) {
				return createFormInstance(webClassName);
			}
		}
		
		// Si no es ninguno de los formularios manejados, retorna null
		return null;
	}
	
	/**
	 * Crea una instancia del formulario a partir del nombre de la clase.
	 * @param webClassName El nombre completamente calificado de la clase del formulario.
	 * @return Una instancia de ADForm si se crea con éxito, o null.
	 */
	private ADForm createFormInstance(String webClassName) {
		Object AMform = null;
		Class<?> clazz = null; 
		ClassLoader loader = getClass().getClassLoader();
		
		try {
			// 1. Cargar la clase
			clazz = loader.loadClass(webClassName);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Load Form Class Failed: " + webClassName, e);
			return null;
		}
		
		if (clazz != null) {
			try {
				// 2. Crear una nueva instancia (usando el constructor vacío)
				AMform = clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				log.log(Level.SEVERE, "Instantiate Form Class Failed: " + webClassName, e);	   
			}
		}

		if (AMform != null) {
			// 3. Devolver la instancia como ADForm
			if (AMform instanceof ADForm) {
				return (ADForm) AMform;
			} else if (AMform instanceof IFormController) {
				IFormController controller = (IFormController) AMform;
				ADForm adForm = controller.getForm();
				return adForm;
			}
		}
		
		return null;
	}
}
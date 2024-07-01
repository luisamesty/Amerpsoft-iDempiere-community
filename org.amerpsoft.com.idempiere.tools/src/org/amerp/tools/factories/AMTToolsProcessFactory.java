package org.amerp.tools.factories;

import org.adempiere.base.IProcessFactory;
import org.amerp.tools.process.AMTToolsCBankAccountReactivate;
import org.amerp.tools.process.AMTToolsGLJournalReactivate;
import org.amerp.tools.process.AMTToolsMInvoiceReactivate;
import org.amerp.tools.process.AMTToolsMMatchInvReactivate;
import org.amerp.tools.process.AMTToolsMPaymentReactivate;
import org.amerp.tools.process.AMTToolsMProductionReactivate;
import org.compiere.process.ProcessCall;
import org.compiere.util.CLogger;

public class AMTToolsProcessFactory implements IProcessFactory {
	
	static CLogger log = CLogger.getCLogger(AMTToolsProcessFactory.class);
	
	@Override
	public ProcessCall newProcessInstance(String p_className) {
		// TODO Auto-generated method stub
    	ProcessCall process = null;
    	//log.warning(".......................................");
    	if (p_className.equals("org.amerp.process.AMTToolsMInvoiceReactivate"))
    		try {
				process =   AMTToolsMInvoiceReactivate.class.newInstance();
			} catch (Exception e) {}
    		//return new AMTMInvoiceReactivate();
    	if (p_className.equals("org.amerp.process.AMTToolsMPaymentReactivate"))
    		try {
				process =   AMTToolsMPaymentReactivate.class.newInstance();
			} catch (Exception e) {}
    		//return new AMTMPaymentReactivate();
      	if (p_className.equals("org.amerp.process.AMTToolsCBankAccountReactivate"))
    		try {
				process =   AMTToolsCBankAccountReactivate.class.newInstance();
			} catch (Exception e) {}
    		//return new AMTCBankAccountReactivate();
       	if (p_className.equals("org.amerp.process.AMTToolsMProductionReactivate"))
    		try {
				process =   AMTToolsMProductionReactivate.class.newInstance();
			} catch (Exception e) {}
       		// return AMTProductionReactivate
       	if (p_className.equals("org.amerp.process.AMTToolsGLJournalReactivate"))
    		try {
				process =   AMTToolsGLJournalReactivate.class.newInstance();
			} catch (Exception e) {}
       		// return AMTGLJournalReactivate
       	if (p_className.equals("org.amerp.process.AMTToolsMMatchInvReactivate"))
    		try {
				process =   AMTToolsMMatchInvReactivate.class.newInstance();
			} catch (Exception e) {}
       		// return AMTMMatchInvReactivate
    	return process;
	}

}

package org.amerp.workflow.factories;

import org.adempiere.base.IProcessFactory;
import org.compiere.process.ProcessCall;
import org.compiere.util.CLogger;

public class AMWWorkFlowProcessFactory implements IProcessFactory{

	static CLogger log = CLogger.getCLogger(AMWWorkFlowProcessFactory.class);
	
	@Override
	public ProcessCall newProcessInstance(String className) {
		// TODO Auto-generated method stub
		ProcessCall process = null;
	  	//log.warning(".......................................");
		// TEMPLATE
//    	if (p_className.equals("org.amerp.process.AMWWorkFlowProcesss"))
//    		try {
//				process =   AMWWorkFlowProcesss.class.newInstance();
//			} catch (Exception e) {}
		return process;
	}

}

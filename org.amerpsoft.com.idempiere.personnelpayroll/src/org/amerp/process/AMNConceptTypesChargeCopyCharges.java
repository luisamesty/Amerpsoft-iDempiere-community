package org.amerp.process;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.MAMN_Concept_Types_Charge;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNConceptTypesChargeCopyCharges  extends SvrProcess{

	static CLogger log = CLogger.getCLogger(AMNConceptTypesChargeCopyCharges.class);
	private int p_AMN_Concept_Types_From_ID = 0;
	private int p_AMN_Concept_Types_To_ID = 0;
	
	@Override
	protected void prepare() {
		// Parameters
				ProcessInfoParameter[] params = getParameter();
				for (ProcessInfoParameter parameter : params)
				{
					String para = parameter.getParameterName();
					if ( para.equals("AMN_Concept_Types_From_ID") )
						p_AMN_Concept_Types_From_ID = parameter.getParameterAsInt();
					else if ( para.equals("AMN_Concept_Types_To_ID"))
						p_AMN_Concept_Types_To_ID = parameter.getParameterAsInt();
					else
						log.log(Level.WARNING, "Unknown paramter: " + para);
				}
				
				if ( p_AMN_Concept_Types_To_ID == 0 )
					p_AMN_Concept_Types_To_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {
		MAMN_Concept_Types_Charge source = MAMN_Concept_Types_Charge.findAMNConceptTypesCharge(Env.getCtx(), p_AMN_Concept_Types_From_ID);
		MAMN_Concept_Types_Charge target = MAMN_Concept_Types_Charge.findAMNConceptTypesCharge(Env.getCtx(), p_AMN_Concept_Types_To_ID);
		//log.warning("source="+source+" "+source.getAMN_Concept_Types_Acct_ID()+
		//		"  target="+target+" "+target.getAMN_Concept_Types_Acct_ID());
		
		if ( p_AMN_Concept_Types_From_ID <= 0 || p_AMN_Concept_Types_To_ID <= 0 || source == null || target == null )
			throw new AdempiereException(Msg.getMsg(getCtx(), "CopyProcessRequired"));
		
		target.copyChargesFrom(source);  // saves 
		
		return "@OK@";
	}

}

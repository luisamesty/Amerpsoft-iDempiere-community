package org.amerp.process;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.MAMN_Concept_Types;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;

// Deprecated, New Process Includes C_AcctSchema_ID Parameter
// Now Using AMNConceptTypesAcctCopyAccounts
public class AMNConceptTypesCopyAccounts extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNConceptTypesCopyAccounts.class);
	private int p_AMN_Concept_Types_From_ID = 0;
	private int p_AMN_Concept_Types_To_ID = 0;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
		MAMN_Concept_Types source = new MAMN_Concept_Types(getCtx(), p_AMN_Concept_Types_From_ID, get_TrxName());
		MAMN_Concept_Types target = new MAMN_Concept_Types(getCtx(), p_AMN_Concept_Types_To_ID, get_TrxName());
		
		if ( p_AMN_Concept_Types_From_ID <= 0 || p_AMN_Concept_Types_To_ID <= 0 || source == null || target == null )
			throw new AdempiereException(Msg.getMsg(getCtx(), "CopyProcessRequired"));
		
		target.copyAccountsFrom(source);  // saves automatically
		
		return "@OK@";
	}

}

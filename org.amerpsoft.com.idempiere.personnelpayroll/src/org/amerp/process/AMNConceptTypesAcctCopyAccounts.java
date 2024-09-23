package org.amerp.process;

import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.MAMN_Concept_Types_Acct;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNConceptTypesAcctCopyAccounts extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNConceptTypesCopyAccounts.class);
	private int p_SourceAcctSchema_ID=0;
	private int p_TargetAcctSchema_ID=0;
	private int p_AMN_Concept_Types_From_ID = 0;
	private int p_AMN_Concept_Types_To_ID = 0;
	@Override
	protected void prepare() {
		// Parameters
		ProcessInfoParameter[] params = getParameter();
		for (ProcessInfoParameter parameter : params)
		{
			String para = parameter.getParameterName();
			if ( para.equals("SourceAcctSchema_ID") )
				p_SourceAcctSchema_ID = parameter.getParameterAsInt();
			else if ( para.equals("AMN_Concept_Types_From_ID") )
				p_AMN_Concept_Types_From_ID = parameter.getParameterAsInt();
			else if ( para.equals("TargetAcctSchema_ID") )
				p_TargetAcctSchema_ID = parameter.getParameterAsInt();
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
		// 
		//log.warning("...........Parametros...................");
		//log.warning("SourceAcctSchema_ID="+p_SourceAcctSchema_ID+"  p_AMN_Concept_Types_From_ID:"+p_AMN_Concept_Types_From_ID+"  p_AMN_Concept_Types_To_ID:"+p_AMN_Concept_Types_To_ID);
		
		MAMN_Concept_Types_Acct source = MAMN_Concept_Types_Acct.findAMNConceptTypesAcct(Env.getCtx(), p_AMN_Concept_Types_From_ID, p_SourceAcctSchema_ID);
		MAMN_Concept_Types_Acct target = MAMN_Concept_Types_Acct.findAMNConceptTypesAcct(Env.getCtx(), p_AMN_Concept_Types_To_ID, p_TargetAcctSchema_ID);
		//log.warning("source="+source+" "+source.getAMN_Concept_Types_Acct_ID()+
		//		"  target="+target+" "+target.getAMN_Concept_Types_Acct_ID());
		
		if ( p_AMN_Concept_Types_From_ID <= 0 || p_AMN_Concept_Types_To_ID <= 0 || source == null || target == null )
			throw new AdempiereException(Msg.getMsg(getCtx(), "CopyProcessRequired"));
		
		target.copyAccountsFrom(p_SourceAcctSchema_ID, source);  // saves 
		
		return "@OK@";
	}

}

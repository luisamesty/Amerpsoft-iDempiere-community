package org.amerp.process;

import java.io.File;
import java.math.BigDecimal;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amfmodel.AMFNaturalAccountMap;
import org.amerp.amfmodel.MAMF_ElementValue;
import org.compiere.model.NaturalAccountMap;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class AMFAccountingClientSetup extends SvrProcess{
	
	/**	Client Parameter		*/
	private int	p_AD_Client_ID = 0;
	private int p_AD_Org_ID=0;
	private int p_C_Element_ID=0;
	private String p_CoAFile = null;
	// CLogger
	CLogger log = CLogger.getCLogger(AMFAccountingClientSetup.class);
    //
	//
	private AMFNaturalAccountMap<String,MAMF_ElementValue> m_nap = null;
	private Trx				m_trx = Trx.get(Trx.createTrxName("Setup"), true);
	private Properties      m_ctx;
	private String          m_lang;
	private int             m_WindowNo;
	private StringBuffer    m_info;
	private File AccountingFile;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AD_Client_ID")) 
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("AD_Org_ID")) 
				p_AD_Org_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Element_ID")) 
				p_C_Element_ID =  para.getParameterAsInt();
			else if (paraName.equals("CoAFile")) 
				p_CoAFile =  para.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}

	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		// Accounting File
		AccountingFile = new File(p_CoAFile);
		if (!AccountingFile.exists())
			throw new AdempiereException("CoaFile " + p_CoAFile + " does not exist");
		if (!AccountingFile.canRead())
			throw new AdempiereException("Cannot read CoaFile " + p_CoAFile);
		if (!AccountingFile.isFile())
			throw new AdempiereException("CoaFile " + p_CoAFile + " is not a file");
		if (AccountingFile.length() <= 0L)
			throw new AdempiereException("CoaFile " + p_CoAFile + " is empty");
		//	Create Account Values Not Included
		m_nap = new AMFNaturalAccountMap<String,MAMF_ElementValue>(m_ctx, m_trx.getTrxName());
		String errMsg = m_nap.parseFile(AccountingFile);
		if (errMsg.length() != 0)
		{
			log.log(Level.SEVERE, errMsg);
			m_info.append(errMsg);
			m_trx.rollback();
			m_trx.close();
		}
		if (m_nap.saveAccounts(getAD_Client_ID(), p_AD_Org_ID, p_C_Element_ID, true)) {
		//	m_info.append(Msg.translate(Env.getCtx(), "C_ElementValue_ID")).append("\n");
			m_trx.commit();
			return null;
		} else	{
			String err = "Acct Element Values NOT inserted";
			log.log(Level.SEVERE, err);
			m_info.append(err);
			m_trx.rollback();
			m_trx.close();
		}
		return null;
	}

	
}

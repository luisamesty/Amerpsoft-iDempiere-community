package org.amerp.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.compiere.acct.Doc_GLJournal;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MConversionType;
import org.compiere.model.MDocType;
import org.compiere.model.MGLCategory;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalLine;
import org.compiere.model.MPeriod;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMFGLJournalAnnualClosing extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMFGLJournalAnnualClosing.class);
	
	private int p_C_Period_ID = 0;
	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_C_ElementValue_ID = 0;
	private int p_TargetAcctSchema_ID = 0;
	private int C_Currency_ID=0;
	private int C_AcctSchema_ID=0;
	private int C_ElementValue_ID = 0;
	private int PR_DocType_ID = 0;
	private int GL_DocType_ID = 0;
	private int C_ValidCombination_ID = 0;	
	private String p_DocumentNo="";
	private String Account_Value="";
	private String Account_Name="";
	private int GL_Journal_ID=0;
	private BigDecimal resultAmount = BigDecimal.ZERO;
	private String irpp ="";
	int lineNo = 10;
	private MJournal mjournal = null;
	private int Account_Count=0;
	BigDecimal debitAmt = BigDecimal.ZERO;
	BigDecimal creditAmt =  BigDecimal.ZERO;
	BigDecimal debitAmtTot = BigDecimal.ZERO;
	BigDecimal creditAmtTot =  BigDecimal.ZERO;
	MAccount maccount = null;
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	//log.warning("........Here I'm in the prerare() - method");		
			ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AD_Client_ID")) 
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("AD_Org_ID")) 
				p_AD_Org_ID =  para.getParameterAsInt();
			else if (paraName.equals("TargetAcctSchema_ID")) 
				p_TargetAcctSchema_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Period_ID")) 
				p_C_Period_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_ElementValue_ID")) 
				p_C_ElementValue_ID =  para.getParameterAsInt();
			else if (paraName.equals("DocumentNo")) 
				p_DocumentNo =  para.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		// Process Monitor
		IProcessUI processMonitor = Env.getProcessUI(getCtx());
		String Message="";
		// TODO Auto-generated method stub
		MPeriod mperiod = new MPeriod(Env.getCtx(),p_C_Period_ID, null);
		Timestamp DateIni = mperiod.getStartDate();
		Timestamp DateEnd = mperiod.getEndDate();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
		//  MAcctSchema Select Client Default 
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_AD_Client_ID, null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		C_Currency_ID = as.getC_Currency_ID();
		// C_AcctSchema_ID
		if (p_TargetAcctSchema_ID >= 0)
			C_AcctSchema_ID = p_TargetAcctSchema_ID;
		else
			C_AcctSchema_ID = as.getC_AcctSchema_ID();
//log.warning("p_TargetAcctSchema_ID="+p_TargetAcctSchema_ID+" C_AcctSchema_ID="+C_AcctSchema_ID);
		// Default GL Category
		MGLCategory glcat =  MGLCategory.getDefault(Env.getCtx(), null);
		// GL_Journal - GL_JournalLine		
		GL_DocType_ID = MDocType.getDocType(Doc_GLJournal.DOCTYPE_GLJournal);

		// Generate GL_Journal record Verify IF EXISTS
		// FIND findGLJournalByDocumentNo
		mjournal = findGLJournalByDocumentNo(Env.getCtx(), p_AD_Client_ID, p_DocumentNo);
		if (mjournal != null ) {
			// Document Header
			addLog( Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+mjournal.getDocumentNo().trim()+
					" ("+Msg.translate(Env.getCtx(), "DocStatus")+":"+mjournal.getDocStatus()+") " +
					Msg.translate(Env.getCtx(), "Date")+":"+
					String.format("%-20s", mjournal.getDateAcct()).replace(' ', '_'));
			addLog(Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+mjournal.getDocumentNo().trim()+
					":"+" **** YA EXISTE UN DOCUMENTO CON ESE NUMERO ****");
			addLog(Msg.getElement(Env.getCtx(),"GL_Journal_ID")+":"+mjournal.getDocumentNo().trim()+
					":"+" **** DEBE REACTIVAR Y ELIMINAR ****");
			return null;
		} else {
			// * Create a GL Journal
			// conversion type = default
			mjournal = new MJournal(getCtx(), 0, get_TrxName());
			mjournal.setAD_Org_ID(p_AD_Org_ID);
			mjournal.setC_Currency_ID(as.getC_Currency_ID());
	        mjournal.setGL_Category_ID(glcat.getGL_Category_ID());
			mjournal.setControlAmt(Env.ZERO);
			mjournal.setDateAcct(DateEnd);
			mjournal.setC_DocType_ID(GL_DocType_ID);
			mjournal.setC_Period_ID(p_C_Period_ID);
			mjournal.setDateDoc(DateEnd);
			mjournal.setDescription("Comprobante de Cierre Anual Periodo "+dt1.format(DateIni)+" al "+dt1.format(DateEnd));
			mjournal.setDocumentNo(p_DocumentNo);
			mjournal.setPostingType("A");
			mjournal.setC_AcctSchema_ID(C_AcctSchema_ID);
			mjournal.setC_ConversionType_ID(MConversionType.getDefault(as.getAD_Client_ID()));
			mjournal.saveEx();
			//
			GL_Journal_ID= mjournal.getGL_Journal_ID();
		}	
		// Generate GL_JournalLine	
		// 
    	// PR_DocType_ID
    	String sql1 = "select c_doctype_id from c_doctype WHERE ad_client_id="+p_AD_Client_ID+"  AND docbasetype='GLJ' " ;
    	PR_DocType_ID = (Integer) DB.getSQLValue(null, sql1);  	
		MDocType pr_doctype = new MDocType(Env.getCtx(),PR_DocType_ID,null);
		String PR_DocTypeName = pr_doctype.getPrintName(Env.getAD_Language(Env.getCtx())).trim();
		// Total Provision
		resultAmount = BigDecimal.ZERO;
		// Show 
		addLog( PR_DocTypeName+"  No:"+Account_Count);
		// SQL
//		String sqls = " SELECT * FROM ( "+
//				" SELECT  " +
//				" c_elementvalue_id, ele.value, ele.name, "+
//				" SUM(CASE WHEN ( fas.postingtype = 'A' AND fas.DateAcct <= ? ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance "+
//				" FROM c_elementvalue ele "+
//				" LEFT JOIN fact_acct fas ON (ele.c_element_id=fas.c_acctschema_id AND ele.c_elementvalue_id=fas.account_id AND ele.ad_client_id=fas.ad_client_id ) "+
//				" WHERE ele.issummary='N' AND fas.ad_client_id=? AND fas.ad_org_id=? AND fas.c_acctschema_id=? "+
//				" GROUP BY c_elementvalue_id "+
//				" ORDER BY ele.value ) as saldos " +
//				" WHERE saldos.value >='4' AND saldos.closebalance <> 0 "
//				;
		String sqls = "SELECT * FROM (  "+
				" SELECT   "+
				"   c_elementvalue_id, ele.value, ele.name,  "+
				"   SUM(CASE WHEN ( fas.postingtype = 'A' AND fas.DateAcct <= ? ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance  "+
				"   FROM c_elementvalue ele  "+
				"   LEFT JOIN ( "+
				"   	SELECT * FROM fact_acct WHERE c_acctschema_id = ?  "+
				"   ) fas ON ele.c_elementvalue_id=fas.account_id "+
				"   WHERE ele.issummary='N' AND fas.ad_client_id=? AND fas.ad_org_id=? AND fas.c_acctschema_id=? "+
				"   GROUP BY c_elementvalue_id  "+
				"   ORDER BY ele.value ) as saldos  "+
				"   WHERE saldos.value >='4' AND saldos.closebalance <> 0  "
				;
				   //log.warning("sql="+sqls);
		//log.warning("IO_DocType_ID="+IO_DocType_ID+"  p_AD_Client_ID="+p_AD_Client_ID+"  p_AD_Org_ID="+p_AD_Org_ID);
		PreparedStatement pstmts = null;
		ResultSet rss = null;
		try
		{			
			pstmts = DB.prepareStatement(sqls, null);
			pstmts.setTimestamp(1,DateEnd);
			pstmts.setInt(2,C_AcctSchema_ID);
			pstmts.setInt(3,p_AD_Client_ID);
			pstmts.setInt(4,p_AD_Org_ID);
			pstmts.setInt(5,C_AcctSchema_ID);
			rss = pstmts.executeQuery();
			while (rss.next())
			{
				C_ElementValue_ID= rss.getInt(1);
				Account_Value = rss.getString(2);
				Account_Name = rss.getString(3);
				BigDecimal CloseBalance = rss.getBigDecimal(4);
				// CloseBalance
				if (CloseBalance.compareTo(BigDecimal.ZERO) != 0) {
					// ------------------------
					// CREATE GL Line
					// Account Combination
					// MAccount (Default Value )
					MAccount defExp =new MAccount(Env.getCtx(), C_ValidCombination_ID , null);
					C_ValidCombination_ID = defExp.getAccount_ID();
					// MAccount (GET OR CREATE )
					maccount = MAccount.get(getCtx(), p_AD_Client_ID,
							p_AD_Org_ID, as.getC_AcctSchema_ID(), C_ElementValue_ID,
							0, 0, 0,
							0, 0, 0,
							0, 0, 0,
							0, 0, 0, 0,
							0,
							get_TrxName());
					//
					maccount.setAD_Org_ID(p_AD_Client_ID);
					maccount.setC_BPartner_ID(0);
					maccount.setC_SalesRegion_ID(0);
					maccount.setC_Project_ID(0);
					maccount.setC_Campaign_ID(0);
					maccount.setC_Activity_ID(0);
					// ROUND 2 DECIMALS
					if (CloseBalance.compareTo(BigDecimal.ZERO) > 0) {
						debitAmt = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
						creditAmt = CloseBalance.setScale(2, RoundingMode.CEILING);
						creditAmtTot=creditAmtTot.add(CloseBalance);
					} else {
						debitAmt = CloseBalance.negate().setScale(2, RoundingMode.CEILING);
						creditAmt = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
						debitAmtTot=debitAmtTot.add(CloseBalance.negate());
					}
					resultAmount= resultAmount.add(CloseBalance);
					// CREATE JOURNAL LINE DEBIT OR CREDIT DEPENDING ON SIGN
					irpp = createJournalLine(mjournal, lineNo, as, Account_Value.trim()+"-"+Account_Name.trim(),  
								"Cierre de Ejercicio Fiscal al "+dt1.format(DateEnd), DateEnd, C_Currency_ID, maccount, 
								debitAmt, creditAmt, debitAmt, creditAmt, C_ValidCombination_ID);
					lineNo += 10;
					// 
					// Process Monitor
					if (processMonitor != null)
					{
						Message = Msg.translate(Env.getCtx(), "C_ElementValue_ID")+":"+Account_Value.trim()+"-"+Account_Name.trim()+
								Msg.translate(Env.getCtx(), "Date")+":"+dt1.format(DateEnd);
						processMonitor.statusUpdate(Message);
					}
					// Document 
					addLog( PR_DocTypeName+":"+" Saldo del Mes ("+ Account_Value+"/"+Account_Name+") " +
							Msg.translate(Env.getCtx(), "Date")+":"+
							dt1.format(DateEnd));
					addLog("   "+irpp);
				}
			}	
			// MAccount (Default Value )
			MAccount defExp =new MAccount(Env.getCtx(), C_ValidCombination_ID , null);
			C_ValidCombination_ID = defExp.getAccount_ID();
			// MAccount (GET OR CREATE )
			maccount = MAccount.get(getCtx(), p_AD_Client_ID,
					p_AD_Org_ID, as.getC_AcctSchema_ID(), p_C_ElementValue_ID,
					0, 0, 0,
					0, 0, 0,
					0, 0, 0,
					0, 0, 0, 0,
					0,
					get_TrxName());
			//
			maccount.setAD_Org_ID(p_AD_Client_ID);
			maccount.setC_BPartner_ID(0);
			maccount.setC_SalesRegion_ID(0);
			maccount.setC_Project_ID(0);
			maccount.setC_Campaign_ID(0);
			maccount.setC_Activity_ID(0);
			// ROUND 2 DECIMALS
			if (resultAmount.compareTo(BigDecimal.ZERO) > 0) {
				debitAmt = resultAmount.setScale(2, RoundingMode.CEILING);
				creditAmt = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
				debitAmtTot=debitAmtTot.add(resultAmount);
			} else {
				debitAmt = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
				creditAmt = resultAmount.negate().setScale(2, RoundingMode.CEILING);
				creditAmtTot=creditAmtTot.add(resultAmount.negate());
			}
			// CREATE JOURNAL LINE DEBIT OR CREDIT DEPENDING ON SIGN ON RESULT
			irpp = createJournalLine(mjournal, lineNo, as, Account_Value.trim()+"-"+Account_Name.trim(),  
						"Cirre de Ejercicio Fiscal al "+dt1.format(DateEnd), DateEnd, C_Currency_ID, maccount, 
						debitAmt, creditAmt, debitAmt, creditAmt, C_ValidCombination_ID);

			// MJournal Header Add Total 
			mjournal.setControlAmt(debitAmtTot);
			mjournal.saveEx();
		}
		catch (Exception ee)
		{
			log.log(Level.SEVERE, sqls, ee);
		}
		finally
		{
			DB.close(rss, pstmts);
			rss = null;
			pstmts = null;
		}
		return null;
	}


	/**
	 *  findGLJournalByDocumentNo
	 * @param ctx
	 * @param locale
	 * @param AD_Client_ID
	 * @param DocumentNo
	 * @return
	 */
	
	public static MJournal findGLJournalByDocumentNo(Properties ctx,
				int AD_Client_ID, String DocumentNo) {
				
		MJournal retValue = null;
		String sql = "SELECT * "
			+ " FROM GL_Journal "
			+ " WHERE DocumentNo='"+DocumentNo+"' " 
			+ " AND AD_Client_ID=?"
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, AD_Client_ID);

			rs = pstmt.executeQuery();
			if (rs.next())
			{
				retValue = new MJournal(Env.getCtx(), rs, null);
				//log.warning("  C_Period_ID:"+rs.getInt(1)+line.getName());
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//log.warning("  DocumentNo="+DocumentNo+"  retValue="+retValue);
		return retValue;
	}
	


	/**
	 * 
	 * @param mjournal
	 * @param lineNo
	 * @param as
	 * @param Employee_description
	 * @param Pay_description
	 * @param DateAcct
	 * @param C_Currency_ID
	 * @param ma
	 * @param amtsourcedr
	 * @param amtsourcecr
	 * @param amtacctdr
	 * @param amtacctcr
	 * @param C_ValidCombination_ID
	 * @return
	 */

	public String createJournalLine (MJournal mjournal, int lineNo, MAcctSchema as,  
		String First_description, String Second_description, Timestamp DateAcct,
		int C_Currency_ID,  MAccount ma, BigDecimal amtsourcedr, BigDecimal amtsourcecr,
		BigDecimal amtacctdr, BigDecimal amtacctcr, int C_ValidCombination_ID) {

		String retValue ="";
		// Create the journal lines
		MJournalLine mjournalline = new MJournalLine(mjournal);
		// Fill Journal Lines Attributes
		mjournalline.setLine(lineNo);
		mjournalline.setDescription(First_description.trim()+" "+Second_description.trim());
		mjournalline.setC_ValidCombination_ID(ma);	
		//
		mjournalline.setAmtSourceDr(amtsourcedr);
		mjournalline.setAmtAcctDr(amtacctdr);
		mjournalline.setAmtSourceCr(amtsourcecr);
		mjournalline.setAmtAcctCr(amtacctcr);
		mjournalline.saveEx();
		retValue = mjournalline.getDescription();
	    return retValue;
	}
}

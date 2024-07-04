/**
 * 
 */
package org.amerp.amndocument;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.amerp.amnmodel.*;
import org.compiere.acct.*;
import org.compiere.model.*;
import org.compiere.util.Env;

/**
 * @author luisamesty
 *
 */
public class Doc_AMNPayroll extends Doc {
	/** Posting Type				*/
	private String			m_PostingType = "A";
	private int				m_C_AcctSchema_ID = 1000000;
	
	public Doc_AMNPayroll (MAcctSchema as, ResultSet rs, String trxName)
	{
		super (as, MAMN_Payroll.class, rs, null, trxName);
	}

	/**
	 *  Load Specific AMN_Payroll Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails() {
		MAMN_Payroll amnpayroll = (MAMN_Payroll)getPO();
		// Set Doc Date 
		//log.warning("====== loadDocumentDetails ============== AMN_Payroll_ID:"+amnpayroll.getAMN_Payroll_ID());
		setDateDoc(amnpayroll.getDateAcct());
		//	Contained Objects
		p_lines = loadLines(amnpayroll);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 *	Load AMN_Payroll_Detail Lines
	 *	@param p_amnpayroll journal
	 *  @return DocLine Array cahnged to MAMN_Docline
	 */
	private MAMN_Docline[] loadLines(MAMN_Payroll p_amnpayroll)
	{
//log.warning("====loadLines===== AMN_Payroll_ID:"+p_amnpayroll.getAMN_Payroll_ID());
		// Decimal Values
		MAMN_Docline linea = (null);
		MAMN_Concept_Types amnct = null ;
		MAMN_Concept_Types_Proc amnctpr = null ;
		BigDecimal Zero= BigDecimal.valueOf(0.00);
		BigDecimal AmountAccDebres = Zero;
		BigDecimal AmountAccCreres = Zero;
		BigDecimal AmountAccSrcDebres = Zero;
		BigDecimal AmountAccSrcCreres = Zero;
		String AMN_Process_Value="NN";
		// Employee from AMN_Payroll
		MAMN_Employee amnemployee = new MAMN_Employee(getCtx(), p_amnpayroll.getAMN_Employee_ID(), null);
		// Default Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_amnpayroll.getAD_Client_ID(), null); 
		MAcctSchema asdef = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		MAcctSchema as = asdef;
		m_C_AcctSchema_ID= as.getC_AcctSchema_ID();
		// get AMN_Schema
		MAMN_Schema amnas =  MAMN_Schema.get(Env.getCtx(), as.getC_AcctSchema_ID(), null);
		MAMN_Process amnpr = new MAMN_Process(Env.getCtx(), p_amnpayroll.getAMN_Process_ID(), null);
		AMN_Process_Value = amnpr.getAMN_Process_Value();
		amnemployee.getValue().trim();
		amnemployee.getName().trim();	
		// MASTER POINTERS
		MAccount MASTERAccountCR = (null);
		MAccount defMASTERAccountCR = (null);
		//MAccount accountRES = (null);
		MAMN_Docline MasterdocLineCR = (null);
    	// Process VALIDATION	
	    if (AMN_Process_Value.equalsIgnoreCase("NN")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Salary(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("NO")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_SalaryOther(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("TO")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_ContrBenefits(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("TI")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Other(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Vacation(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Provision_Social(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Utilities(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PO")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Advances(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PI")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Social(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PL")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Social(),null);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PR")) {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Social(),null);
	    } else {
	    	defMASTERAccountCR = new MAccount(Env.getCtx(), amnas.getAMN_P_Liability_Salary(),null);
	    }
//log.warning("====loadLines===== AMN_Process_Value:"+AMN_Process_Value+"  defMASTERAccountCR="+defMASTERAccountCR);
	    // Payroll Detail Lines
		ArrayList<MAMN_Docline> list = new ArrayList<MAMN_Docline>();
		ArrayList<MAMN_Docline> listres = new ArrayList<MAMN_Docline>();
		MAMN_Payroll_Detail[] lines = p_amnpayroll.getLines(false);
		MAMN_Payroll_Detail[] linesref = null;
		// -----------------------------------------------------
		// Verify  ZERO Receipt No Lines with Values Allocated
		// -----------------------------------------------------
		// Get Firts Reference Line
		//log.warning("Std lines size:"+lines.length);
		if(lines.length == 0) {
			linesref= p_amnpayroll.getFirstReferenceLine(false);
//log.warning("Zero Lines Payroll Description:"+p_amnpayroll.getDescription()+"  Firts Reference lines length:"+linesref.length);
			// Verify if at least one Ref Concept.
//if (linesref != null)
//	log.warning("linesref ...:"+linesref.length);

			if (linesref.length==0) {
				MAMN_Docline[] dlsnull = new MAMN_Docline[0];
				list.toArray(dlsnull);
				return dlsnull;
			}
			MAMN_Payroll_Detail lineREF = linesref[0];
			//log.warning("Reference lines size:"+lines.length);
			if(linesref.length != 0) {
//Inmutable				MASTERAccountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineREF, as));
				MASTERAccountCR = new MAccount(Env.getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineREF, as),null);
			} 
			// Default reference Concept
			else {
				linesref= p_amnpayroll.getFirstLine(false);
//Inmutable				MASTERAccountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineREF, as));
				MASTERAccountCR = new MAccount(Env.getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineREF, as), null);
			}
			//log.warning("MASTERAccountCR:"+MASTERAccountCR.getDescription());
			// HEADER  int i = 0
			MasterdocLineCR = new MAMN_Docline (lineREF, this);
			MasterdocLineCR.setAccount(MASTERAccountCR);
			BigDecimal.valueOf(0);
			BigDecimal.valueOf(0);
			// -- Quantity
			MasterdocLineCR.setQty(lineREF.getQtyValue(), false);
			lineREF.getQtyValue();
			//  --  Source Amounts Zero
			MasterdocLineCR.setAmount (Zero,Zero);
			//  --  Converted Amounts Zero 
			MasterdocLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, Zero);
			// Business Partner - SalesRegion - Project - Campaign -Activity
			if (amnemployee.getBill_BPartner_ID() != 0) {
				MASTERAccountCR.setC_BPartner_ID(amnemployee.getBill_BPartner_ID());
			} else {
				MASTERAccountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
			}
			MASTERAccountCR.setC_SalesRegion_ID(p_amnpayroll.getC_SalesRegion_ID());
			MASTERAccountCR.setC_Project_ID(p_amnpayroll.getC_Project_ID());
			MASTERAccountCR.setC_Campaign_ID(p_amnpayroll.getC_Campaign_ID());
			MASTERAccountCR.setC_Activity_ID(p_amnpayroll.getC_Activity_ID());
			//	--	Organization of Line was set to Org of Account
			list.add(MasterdocLineCR);		
			//	Return Array of One Reference Element
			int sizeres = list.size();
			MAMN_Docline[] dls = new MAMN_Docline[sizeres];
			// Create dls		
			list.toArray(dls);
			return dls;
		}

		// -----------------------------------------------------
		// Verify NON ZERO Receipt Lines with Values Allocated
		// -----------------------------------------------------
		MAMN_Payroll_Detail lineM = lines[0];
		// IF Default Master Account IS NUL Takes the first Line Master Account
		if (defMASTERAccountCR != null) 
			MASTERAccountCR = defMASTERAccountCR ;
		else
// Inmutable			MASTERAccountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineM,as));
			MASTERAccountCR = new MAccount(Env.getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineM,as), null);
		// --------------
		// CREDITS
		// --------------
		for (int i = 0; i < lines.length; i++) 
		{
			// *******  PAyroll CREDIT *********
			MAccount accountCR = (null);
			MAMN_Payroll_Detail line = lines[i];
			amnctpr = new MAMN_Concept_Types_Proc(getCtx(),line.getAMN_Concept_Types_Proc_ID(),null);
			amnct = new MAMN_Concept_Types(getCtx(),amnctpr.getAMN_Concept_Types_ID(),null);
			// Credit c_validcombination_id
			// Take First ONE MASTERAccountCR For RESUME HEADER ACCOUNT		
// Inmutable	accountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(line,as));
			accountCR = new MAccount(Env.getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(line,as),null);
			MAMN_Docline docLineCR = new MAMN_Docline (line, this);
			docLineCR.setAccount(accountCR);				// -- Quantity
			docLineCR.setQty(line.getQtyValue(), false);
			//  --  Source Amounts
			docLineCR.setAmount (Zero,line.getAmountCalculated());
			//  --  Converted Amounts
			docLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, line.getAmountCalculated());
			//  --  Account
			if (accountCR.getC_BPartner_ID() == 0) {
				if (amnemployee.getBill_BPartner_ID() != 0) {
					accountCR.setC_BPartner_ID(amnemployee.getBill_BPartner_ID());
				} else {
					accountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
				}
			}				
			accountCR.setC_SalesRegion_ID(p_amnpayroll.getC_SalesRegion_ID());
			accountCR.setC_Project_ID(p_amnpayroll.getC_Project_ID());
			accountCR.setC_Campaign_ID(p_amnpayroll.getC_Campaign_ID());
			accountCR.setC_Activity_ID(p_amnpayroll.getC_Activity_ID());
			docLineCR.setAccount(accountCR);
			if (amnct != null) {
				docLineCR.setConcept_CalcOrder(amnct.getCalcOrder());
				docLineCR.setConcept_Value(amnct.getValue());
				docLineCR.setConcept_Name(amnct.getName());
			}
//log.warning("CREDITS Concepto="+line.getValue()+"  accountCR="+accountCR.getDescription()+" Amount="+line.getAmountCalculated());
			list.add(docLineCR);	
//log.warning(list.toString());
		}
		// --------------
		//  DEBITS
		// --------------
		for (int i = 0; i < lines.length; i++)
		{
			// DEBIT CREDIT OBJECTS
			MAccount accountDB = (null);
			MAMN_Payroll_Detail line = lines[i];
			amnctpr = new MAMN_Concept_Types_Proc(getCtx(),line.getAMN_Concept_Types_Proc_ID(),null);
			amnct = new MAMN_Concept_Types(getCtx(),amnctpr.getAMN_Concept_Types_ID(),null);
		// *******  PAyroll DEBIT *********
// Inmutable			accountDB = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Deb_Acct(line,as));
			accountDB = new MAccount(Env.getCtx(), MAMN_Payroll_Detail.getAMN_Deb_Acct(line,as),null);			
			MAMN_Docline docLineDB = new MAMN_Docline (line, this);
			// -- Quantity
			docLineDB.setQty(line.getQtyValue(), false);
			//  --  Source Amounts
			docLineDB.setAmount(line.getAmountCalculated(), Zero);
			//  --  Converted Amounts
			docLineDB.setConvertedAmt (m_C_AcctSchema_ID, line.getAmountCalculated(), Zero);
			//  --  Account
// Inmutable accountDB = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Deb_Acct(line,as));
			// Business Partner - SalesRegion - Project - Campaign -Activity
			if (accountDB.getC_BPartner_ID() == 0) {
				if (amnemployee.getBill_BPartner_ID() != 0) {
					accountDB.setC_BPartner_ID(amnemployee.getBill_BPartner_ID());
				} else {
					accountDB.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
				}
			}
			accountDB.setC_SalesRegion_ID(p_amnpayroll.getC_SalesRegion_ID());
			accountDB.setC_Project_ID(p_amnpayroll.getC_Project_ID());
			accountDB.setC_Campaign_ID(p_amnpayroll.getC_Campaign_ID());
			accountDB.setC_Activity_ID(p_amnpayroll.getC_Activity_ID());
			docLineDB.setAccount (accountDB);
			if (amnct != null) {
				docLineDB.setConcept_CalcOrder(amnct.getCalcOrder());
				docLineDB.setConcept_Value(amnct.getValue());
				docLineDB.setConcept_Name(amnct.getName());
			}
//log.warning("DEBITS Concepto="+line.getValue()+"  accountDB="+accountDB.getDescription()+" Amount="+line.getAmountCalculated());
			list.add(docLineDB);
//log.warning(list.toString());
		}
		// ----------------------------------
		// RESUME DUPLICITY Master Account
		// MASTERAccountCR
		// ----------------------------------
		// 
//log.warning("list.size()="+list.size());		
		Boolean isResume = false;
		if ( list.size() > 2)
			isResume = true;
		if (isResume) {
			linea = new MAMN_Docline(p_po, null);
			int reslin=0;
			BigDecimal Qtyres = BigDecimal.ONE;
			AmountAccDebres=Zero;
			AmountAccCreres=Zero;
			AmountAccSrcDebres=Zero;
			AmountAccSrcCreres=Zero;
			// CREATE listres REGISTERS
//log.warning("Master account="+MASTERAccountCR.getAccount());
			for (int j = 0; j < list.size(); j++)
			{
				linea = list.get(j);
				if (linea.getAccount().getAccount().equals(MASTERAccountCR.getAccount()) ) {
					// Check Sign
					// CR
					if (list.get(j).getAmtSourceDr().compareTo(Zero) == 0 && list.get(j).getAmtSourceCr().compareTo(Zero) != 0) {
						AmountAccCreres = AmountAccCreres.add(linea.getAmtAcctCr());
						AmountAccSrcCreres = AmountAccSrcCreres.add(linea.getAmtSourceCr());
					} else if (list.get(j).getAmtSourceDr().compareTo(Zero) != 0 && list.get(j).getAmtSourceCr().compareTo(Zero) == 0) {
						AmountAccDebres = AmountAccDebres.add(linea.getAmtAcctDr());		
						AmountAccSrcDebres = AmountAccSrcDebres.add(linea.getAmtSourceDr());
					// Zero
					} else {
						
					}
//log.warning("account"+linea.getAccount()+"  Reg:"+reslin+" j:"+j +
//"  DB:"+list.get(j).getAmtAcctDr()+
//"  CR:"+list.get(j).getAmtAcctCr()+
//"  DB SOURCE:"+list.get(j).getAmtSourceDr()+
//"  CR SOURCE:"+list.get(j).getAmtSourceCr());					
				} else {
					// -- Quantity  --  Source Amounts --  Converted Amounts --  Account
					linea.setQty(list.get(j).getQty(), false);
					linea.setAmount(list.get(j).getAmtSourceDr(), list.get(j).getAmtSourceCr());
					linea.setConvertedAmt (m_C_AcctSchema_ID, list.get(j).getAmtAcctDr(), list.get(j).getAmtAcctCr());
					linea.setAccount (list.get(j).getAccount());
					MAccount accountLIN = list.get(j).getAccount();
					if (accountLIN.getC_BPartner_ID() == 0) {
						if (amnemployee.getBill_BPartner_ID() != 0) {
							accountLIN.setC_BPartner_ID(amnemployee.getBill_BPartner_ID());
						} else {
							accountLIN.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
						}
					}
					accountLIN.setC_SalesRegion_ID(p_amnpayroll.getC_SalesRegion_ID());
					accountLIN.setC_Project_ID(p_amnpayroll.getC_Project_ID());
					accountLIN.setC_Campaign_ID(p_amnpayroll.getC_Campaign_ID());
					accountLIN.setC_Activity_ID(p_amnpayroll.getC_Activity_ID());	
					accountLIN.setDescription(amnemployee.getValue()+"_"+amnemployee.getName()+"_"+linea.getDescription());
					// ADD linea
					listres.add(linea);
					// Increment reslin
					reslin=reslin+1;
				} 
//log.warning("account="+linea.getAccount().getAccount()+" AmountDB="+linea.getAmtAcctDr()+" AmountCR="+linea.getAmtAcctCr()+
//		" ResDB="+AmountAccDebres+" ResCR="+AmountAccCreres+" SoResDB="+AmountAccSrcDebres+" SoResCR="+AmountAccSrcCreres);
			}	//	END VERIFY DUPLICITY	
//log.warning("Resumen: ResDB="+AmountAccDebres+" ResCR="+AmountAccCreres+" SoResDB="+AmountAccSrcDebres+" SoResCR="+AmountAccSrcCreres);
			// ADD MASTERAccountCR
			MAMN_Docline lineares = new MAMN_Docline(p_po, null);
			lineares = list.get(0);
			lineares.setAccount(MASTERAccountCR);
			lineares.setQty(Qtyres, false);
			if (AmountAccSrcDebres.compareTo(AmountAccSrcCreres) < 0 )
				lineares.setAmount(Zero, AmountAccSrcCreres.subtract(AmountAccSrcDebres));
			else 
				lineares.setAmount(AmountAccSrcDebres.subtract(AmountAccSrcCreres), Zero);
			if (AmountAccDebres.compareTo(AmountAccCreres) < 0 )
				lineares.setConvertedAmt (m_C_AcctSchema_ID, Zero, AmountAccCreres.subtract(AmountAccDebres));
			else
				lineares.setConvertedAmt (m_C_AcctSchema_ID, AmountAccDebres.subtract(AmountAccCreres), Zero);
			if (MASTERAccountCR.getC_BPartner_ID() == 0)
				MASTERAccountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
			MASTERAccountCR.setC_SalesRegion_ID(p_amnpayroll.getC_SalesRegion_ID());
			MASTERAccountCR.setC_Project_ID(p_amnpayroll.getC_Project_ID());
			MASTERAccountCR.setC_Campaign_ID(p_amnpayroll.getC_Campaign_ID());
			MASTERAccountCR.setC_Activity_ID(p_amnpayroll.getC_Activity_ID());	
			MASTERAccountCR.setDescription(amnemployee.getValue()+"_"+amnemployee.getName()+"_"+linea.getDescription());

			reslin=reslin+1;
			// ADD lineares
			listres.add(lineares);
		} else {
			// DO NOT RESUME BECAUSE OF Future EXTERNAL VAriable 
			listres=list;
		}
		//	Return Array Res
		int sizeres = listres.size();
		MAMN_Docline[] dls = new MAMN_Docline[sizeres];
		// Create dls		
		listres.toArray(dls);
		return dls;

	}	//	loadLines

	public BigDecimal getBalance() {
//log.warning("=== getBalance ==========");
		BigDecimal retValue = Env.ZERO;
		StringBuilder sb = new StringBuilder (" [");
		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.add(p_lines[i].getAmtSource());
			sb.append("+").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		if (log.isLoggable(Level.FINE)) log.fine(toString() + " Balance=" + retValue + sb.toString());
//log.warning("========== Balance=" + retValue + sb.toString());
//		return retValue;
		return Env.ZERO;
		
	}
	/**
	 *  createFacts
	 */
	public ArrayList<Fact> createFacts(MAcctSchema as) {
//log.warning("===== createFacts =========");
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//int c_elementvalue_id =0;
		//  create Fact Header
		Fact fact = new Fact (this, as, m_PostingType);
log.warning("MAcctSchema="+as.getC_AcctSchema_ID()+"  m_PostingType:"+m_PostingType+"  p_lines.length:"+p_lines.length);
		//  account     DR      CR
		for (int i = 0; i < p_lines.length; i++)
		{
log.warning(" AmtDr:"+p_lines[i].getAmtAcctDr().toString()+
			" AmtCr:"+p_lines[i].getAmtAcctCr().toString()+
			" Currency:"+p_lines[i].getC_Currency_ID () + //getC_Currency_ID()+
			" AS="+p_lines[i].getC_AcctSchema_ID () +
			" AmtSourceDr:"+p_lines[i].getAmtSourceDr().toString()+
			" AmtSourceCr:"+p_lines[i].getAmtSourceCr().toString()	);
			@SuppressWarnings("unused")
//			FactLine line = fact.createLine (p_lines[i],
//								p_lines[i].getAccount (),
//								getC_Currency_ID(),
//								p_lines[i].getAmtAcctDr (),
//								p_lines[i].getAmtAcctCr ());
			FactLine line =	fact.createLine (p_lines[i],
									p_lines[i].getAccount(),
									p_lines[i].getC_Currency_ID(),
									p_lines[i].getAmtSourceDr (),
									p_lines[i].getAmtSourceCr ());
				// ADD DESCRIPTION 
//			if(p_lines[i].getDescription() == null)
//				line.addDescription("");
//			else
//				line.addDescription(p_lines[i].getDescription());

		}	//	for all lines
		//
		facts.add(fact);
		return facts;
	}
	
	public boolean isDuplicate(ArrayList<MAMN_Docline[]> lista){

		for(int i=0;i<lista.size()-1;i++){
			for(int j=i+1;j<lista.size();j++){
				if(lista.get(i)==lista.get(j))				
				return true;
			}
		}
			return false;
		}
	
	public void addUnique(ArrayList<String> lista, String elemento){

		if(!lista.contains(elemento))
			lista.add(elemento);
		}
}


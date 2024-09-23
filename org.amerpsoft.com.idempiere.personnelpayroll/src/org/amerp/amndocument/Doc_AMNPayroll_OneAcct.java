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
public class Doc_AMNPayroll_OneAcct extends Doc {
	/** Posting Type				*/
	private String			m_PostingType = "A";
	private int				m_C_AcctSchema_ID = 1000000;
	
	public Doc_AMNPayroll_OneAcct (MAcctSchema as, ResultSet rs, String trxName)
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
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MAMN_Payroll p_amnpayroll)
	{
log.warning("====loadLines===== AMN_Payroll_ID:"+p_amnpayroll.getAMN_Payroll_ID());
		// Decimal Values
		DocLine linea = (null);
		BigDecimal Zero= BigDecimal.valueOf(0.00);
		BigDecimal AmountAccDebres = Zero;
		BigDecimal AmountAccCreres = Zero;
		BigDecimal AmountAccSrcDebres = Zero;
		BigDecimal AmountAccSrcCreres = Zero;		
		BigDecimal QtyAcc = BigDecimal.valueOf(0);
		BigDecimal Qtyres = BigDecimal.valueOf(0);
		BigDecimal MASTERAmountAccCre = BigDecimal.valueOf(0);
		BigDecimal MASTERQtyAcc = BigDecimal.valueOf(0);
		boolean iScontainDB = false;
		boolean iScontainCR = false;
		// Employee Attibutes
		int BPartner_ID=0;
		int SalesRegion_ID=0;
		int Project_ID=0;
		int Campaign_ID=0;
		int Activity_ID=0;	
		// Employee from AMN_Payroll
		MAMN_Employee amnemployee = new MAMN_Employee(getCtx(), p_amnpayroll.getAMN_Employee_ID(), null);
		// Default Account Schema
		MClientInfo info = MClientInfo.get(Env.getCtx(), p_amnpayroll.getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		String Employee_Value=amnemployee.getValue().trim();
		String Employee_Name=amnemployee.getName().trim();	
		// MASTER POINTERS
		MAccount MASTERAccountCR = (null);
		MAccount accountRES = (null);
		DocLine MasterdocLineCR = (null);
		// Payroll Detail Lines
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		ArrayList<DocLine> listres = new ArrayList<DocLine>();
		MAMN_Payroll_Detail[] lines = p_amnpayroll.getLines(false);
		MAMN_Payroll_Detail[] linesref = null;
		// -----------------------------------------------------
		// Verify  ZERO Receipt No Lines with Values Allocated
		// -----------------------------------------------------
		// Get Firts Reference Line
		//log.warning("Std lines size:"+lines.length);
		if(lines.length == 0) {
			linesref= p_amnpayroll.getFirstReferenceLine(false);
			log.warning("Zero Lines Payroll Description:"+p_amnpayroll.getDescription()+"  Firts Reference lines length:"+linesref.length);
			// Verify if at least one Ref Concept.
			MAMN_Payroll_Detail lineREF = linesref[0];
			//log.warning("Reference lines size:"+lines.length);
			if(linesref.length != 0) {
				MASTERAccountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineREF, as));
			} 
			// Default reference Concept
			else {
				linesref= p_amnpayroll.getFirstLine(false);
				MASTERAccountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineREF, as));
			}
			//log.warning("MASTERAccountCR:"+MASTERAccountCR.getDescription());
			// HEADER  int i = 0
			MasterdocLineCR = new DocLine (lineREF, this);
			MasterdocLineCR.setAccount(MASTERAccountCR);
			MASTERAmountAccCre = BigDecimal.valueOf(0);
			MASTERQtyAcc = BigDecimal.valueOf(0);
			// -- Quantity
			MasterdocLineCR.setQty(lineREF.getQtyValue(), false);
			MASTERQtyAcc = lineREF.getQtyValue();
			//  --  Source Amounts Zero
			MasterdocLineCR.setAmount (Zero,Zero);
			//  --  Converted Amounts Zero 
			MasterdocLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, Zero);
			// Business Partner - SalesRegion - Project - Campaign -Activity
			MASTERAccountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
			MASTERAccountCR.setC_SalesRegion_ID(amnemployee.getC_SalesRegion_ID());
			MASTERAccountCR.setC_Project_ID(amnemployee.getC_Project_ID());
			MASTERAccountCR.setC_Campaign_ID(amnemployee.getC_Campaign_ID());
			MASTERAccountCR.setC_Activity_ID(amnemployee.getC_Activity_ID());
			//	--	Organization of Line was set to Org of Account
			list.add(MasterdocLineCR);		
			//	Return Array of One Reference Element
			int sizeres = list.size();
			DocLine[] dls = new DocLine[sizeres];
			// Create dls		
			list.toArray(dls);
			return dls;
		}

		// -----------------------------------------------------
		// Verify NON ZERO Receipt Lines with Values Allocated
		// -----------------------------------------------------
		MAMN_Payroll_Detail lineM = lines[0];
		MASTERAccountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(lineM, as));
//log.warning("MASTERAccountCR:"+MASTERAccountCR.getDescription());
		// HEADER  int i = 0
		MasterdocLineCR = new DocLine (lineM, this);
		MasterdocLineCR.setAccount(MASTERAccountCR);
		MASTERAmountAccCre = BigDecimal.valueOf(0);
		MASTERQtyAcc = BigDecimal.valueOf(0);
		// -- Quantity
		MasterdocLineCR.setQty(lineM.getQtyValue(), false);
		MASTERQtyAcc = lineM.getQtyValue();
		//  --  Source Amounts
		MasterdocLineCR.setAmount (Zero,lineM.getAmountCalculated());
		MASTERAmountAccCre = lineM.getAmountCalculated();
		//  --  Converted Amounts
		MasterdocLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, lineM.getAmountCalculated());
		// Business Partner - SalesRegion - Project - Campaign -Activity
		MASTERAccountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
		MASTERAccountCR.setC_SalesRegion_ID(amnemployee.getC_SalesRegion_ID());
		MASTERAccountCR.setC_Project_ID(amnemployee.getC_Project_ID());
		MASTERAccountCR.setC_Campaign_ID(amnemployee.getC_Campaign_ID());
		MASTERAccountCR.setC_Activity_ID(amnemployee.getC_Activity_ID());
		BPartner_ID=amnemployee.getC_BPartner_ID();
		SalesRegion_ID=amnemployee.getC_SalesRegion_ID();
		Project_ID=amnemployee.getC_Project_ID();
		Campaign_ID=amnemployee.getC_Campaign_ID();
		Activity_ID=getC_Activity_ID();
		
		//	--	Organization of Line was set to Org of Account
		list.add(MasterdocLineCR);		
		// --------------
		// CREDITS
		// --------------
		for (int i = 1; i < lines.length; i++) 
		{
			// *******  PAyroll CREDIT *********
			MAccount accountCR = (null);
			MAMN_Payroll_Detail line = lines[i];		
			// Credit c_validcombination_id
			// Take First ONE MASTERAccountCR For RESUME HEADER ACCOUNT		
			accountCR = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Cre_Acct(line,as));
			if (accountCR.equals(MASTERAccountCR) ) {
				// -- Quantity
				MASTERQtyAcc = MASTERQtyAcc.add(lineM.getQtyValue());
				MasterdocLineCR.setQty(MASTERQtyAcc, false);
				//  --  Source Amounts
				MASTERAmountAccCre = MASTERAmountAccCre.add(line.getAmountAllocated());
				MasterdocLineCR.setAmount (Zero,MASTERAmountAccCre);
				//  --  Converted Amounts
				MasterdocLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, MASTERAmountAccCre);

				list.set(0, MasterdocLineCR);				
			} else {
				DocLine docLineCR = new DocLine (line, this);
				docLineCR.setAccount(accountCR);				// -- Quantity
				docLineCR.setQty(line.getQtyValue(), false);
				//  --  Source Amounts
				docLineCR.setAmount (Zero,line.getAmountCalculated());
				//  --  Converted Amounts
				docLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, line.getAmountCalculated());
				//  --  Account
				//p_AMN_Payroll_Detail_ID=line.getAMN_Payroll_Detail_ID();
				// Credit c_elementvalue_id
				//c_elementvalue_id = MAMN_Payroll_Detail.getAMN_CT_Cre_Acct(line);
				// Business Partner - SalesRegion - Project - Campaign -Activity
//OJO				accountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
				if (accountCR.getC_BPartner_ID() == 0)
					accountCR.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
//OJO
				accountCR.setC_SalesRegion_ID(amnemployee.getC_SalesRegion_ID());
				accountCR.setC_Project_ID(amnemployee.getC_Project_ID());
				accountCR.setC_Campaign_ID(amnemployee.getC_Campaign_ID());
				accountCR.setC_Activity_ID(amnemployee.getC_Activity_ID());
				docLineCR.setAccount(accountCR);
				//	--	Organization of Line was set to Org of Account
				list.add(docLineCR);			
			}
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
			accountDB = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Deb_Acct(line,as));
			if (accountDB.equals(MASTERAccountCR) ) {
				// -- Quantity Substract
				MASTERQtyAcc = MASTERQtyAcc.subtract(lineM.getQtyValue());
				MasterdocLineCR.setQty(MASTERQtyAcc, false);
				//  --  Source Amounts Substract
				MASTERAmountAccCre = MASTERAmountAccCre.subtract(line.getAmountDeducted());
				MasterdocLineCR.setAmount (Zero,MASTERAmountAccCre);
				//  --  Converted Amounts Substract
				MasterdocLineCR.setConvertedAmt (m_C_AcctSchema_ID, Zero, MASTERAmountAccCre);
				list.set(0, MasterdocLineCR);				
			} else {						
				// *******  PAyroll DEBIT *********
				DocLine docLineDB = new DocLine (line, this);
				// -- Quantity
				docLineDB.setQty(line.getQtyValue(), false);
				//  --  Source Amounts
				docLineDB.setAmount(line.getAmountCalculated(), Zero);
				//  --  Converted Amounts
				docLineDB.setConvertedAmt (m_C_AcctSchema_ID, line.getAmountCalculated(), Zero);
				//  --  Account
				//p_AMN_Payroll_Detail_ID=line.getAMN_Payroll_Detail_ID();
				// Debit c_elementvalue_id
				//c_elementvalue_id = MAMN_Payroll_Detail.getAMN_CT_Deb_Acct(line);
				// Debit c_validcombination_id
				accountDB = MAccount.get(getCtx(), MAMN_Payroll_Detail.getAMN_Deb_Acct(line,as));
				// Business Partner - SalesRegion - Project - Campaign -Activity
//OJO				accountDB.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
				if (accountDB.getC_BPartner_ID() == 0)
					accountDB.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
//OJO
				accountDB.setC_SalesRegion_ID(amnemployee.getC_SalesRegion_ID());
				accountDB.setC_Project_ID(amnemployee.getC_Project_ID());
				accountDB.setC_Campaign_ID(amnemployee.getC_Campaign_ID());
				accountDB.setC_Activity_ID(amnemployee.getC_Activity_ID());
				docLineDB.setAccount (accountDB);
				//docLineDB.setAMNPayrollDetailElementValue
				//	--	Organization of Line was set to Org of Account
				list.add(docLineDB);
			}
		}
		// -----------------------
		// VERIFIY DUPLICITY
		// -----------------------
		// 
		linea = new DocLine(p_po, null);
		linea = list.get(0);
		int reslin=0;
		int iSreslin =0;
		int C_BP_RES=0;
		int C_BP=0;
		// CREATE listres REGISTERS
		for (int j = 0; j < list.size(); j++)
		{
			linea = list.get(j);
			accountRES = list.get(j).getAccount();
//OJO
			C_BP_RES = list.get(j).getAccount().getC_BPartner_ID() ;
//OJO			
			QtyAcc = list.get(j).getQty();
			iScontainDB = false;
			iScontainCR = false;
			AmountAccDebres=Zero;
			AmountAccCreres=Zero;
			AmountAccSrcDebres=Zero;
			AmountAccSrcCreres=Zero;
			// Verify if it is in Res Array
			//for (int k = 0; k < listres.size(); k++) {
			for (int k = 0; k < listres.size(); k++) {
				//log.warning("BUSQUEDA k:"+k+"  DB"+list.get(k).getAmtAcctDr()+"  CR"+list.get(k).getAmtAcctCr());
				//OJO
				C_BP = listres.get(k).getAccount().getC_BPartner_ID() ;
				//OJO				
				if (listres.get(k).getAccount().getAccount().equals(accountRES.getAccount()) 
						&& C_BP == C_BP_RES) 
				{					
					if ((list.get(j).getAmtAcctDr().equals(Zero) && listres.get(k).getAmtAcctDr().equals(Zero) )
						&& (!list.get(j).getAmtAcctCr().equals(Zero) && !listres.get(k).getAmtAcctCr().equals(Zero)))
						iScontainCR = true;
					if ((!list.get(j).getAmtAcctDr().equals(Zero) && !listres.get(k).getAmtAcctDr().equals(Zero) )
							&& (list.get(j).getAmtAcctCr().equals(Zero) && listres.get(k).getAmtAcctCr().equals(Zero)))
						iScontainDB = true;
					Qtyres=listres.get(k).getQty();
					AmountAccDebres = listres.get(k).getAmtAcctDr();
					AmountAccCreres = listres.get(k).getAmtAcctCr();
					AmountAccSrcDebres=listres.get(k).getAmtSourceDr();
					AmountAccSrcCreres=listres.get(k).getAmtSourceCr();
					linea = listres.get(k);
					iSreslin=k;
					break;
				}
			}
			// IF Exist Update else CREATE
			if (j == 0) {
				// -- Quantity
				linea.setQty(list.get(j).getQty(), false);
				//  --  Source Amounts
				linea.setAmount(list.get(j).getAmtSourceDr(), list.get(j).getAmtSourceCr());
				//  --  Converted Amounts
				linea.setConvertedAmt (m_C_AcctSchema_ID, list.get(j).getAmtAcctDr(), list.get(j).getAmtAcctCr());
				//  --  Account
				accountRES = list.get(j).getAccount();
				linea.setAccount (accountRES);
				// Business Partner - SalesRegion - Project - Campaign -Activity

//OJO				accountRES.setC_BPartner_ID(BPartner_ID);
				if (accountRES.getC_BPartner_ID() == 0)
					accountRES.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
//OJO
				accountRES.setC_SalesRegion_ID(SalesRegion_ID);
				accountRES.setC_Project_ID(Project_ID);
				accountRES.setC_Campaign_ID(Campaign_ID);
				accountRES.setC_Activity_ID(Activity_ID);
				accountRES.setDescription(Employee_Value+" "+Employee_Name);
				// ADD linea
				listres.add(linea);
//log.warning("account"+accountRES+"  Reg:"+reslin+" j:"+j +
//		"  Acc: "+(iScontain || iScontainDB || iScontainCR ) + 
//		"  DB:"+list.get(j).getAmtAcctDr()+
//		"  CR:"+list.get(j).getAmtAcctCr()+
//		"  DB SOURCE:"+list.get(j).getAmtSourceDr()+
//		"  CR SOURCE:"+list.get(j).getAmtSourceCr());				
				// Increment reslin
				reslin=reslin+1;
			} else {
					if (iScontainDB || iScontainCR ) {
						// -- Quantity Update
						linea.setQty(QtyAcc.add(Qtyres), false);
						//  --  Source Amounts Update
						if (iScontainDB) {
							linea.setAmount(list.get(j).getAmtSourceDr().add(AmountAccSrcDebres), Zero);
							}
						if (iScontainCR) {
							linea.setAmount(Zero, list.get(j).getAmtSourceCr().add(AmountAccSrcCreres));
							}
						//  --  Converted Amounts Substract
						if (iScontainDB) {
							linea.setConvertedAmt (m_C_AcctSchema_ID, list.get(j).getAmtAcctDr().add(AmountAccDebres), Zero);
						}
						if (iScontainCR) {
							linea.setConvertedAmt (m_C_AcctSchema_ID, Zero, list.get(j).getAmtAcctCr().add(AmountAccCreres));
						}
//log.warning("account"+accountRES+"  Reg:"+iSreslin+" j:"+j +
//		"  Acc: "+(iScontain || iScontainDB || iScontainCR ) + 
//		"  DB:"+list.get(j).getAmtAcctDr()+
//		"  CR:"+list.get(j).getAmtAcctCr()+
//		"  DB SOURCE:"+list.get(j).getAmtSourceDr()+
//		"  CR SOURCE:"+list.get(j).getAmtSourceCr()+
//		"  ACUMULADO DB"+list.get(j).getAmtAcctDr().add(AmountAccDebres)+
//		"  CR"+list.get(j).getAmtAcctCr().add(AmountAccCreres)+
//		"  DB SOURCE"+list.get(j).getAmtSourceDr().add(AmountAccSrcDebres)+
//		"  CR SOURCE"+list.get(j).getAmtSourceCr().add(AmountAccSrcCreres));						
						// UPDATE RES REGISTER
						listres.set(iSreslin,linea);
					} else {				
						// -- Quantity
						linea.setQty(list.get(j).getQty(), false);
						//  --  Source Amounts
						linea.setAmount(list.get(j).getAmtSourceDr(), list.get(j).getAmtSourceCr());
						//  --  Converted Amounts
						linea.setConvertedAmt (m_C_AcctSchema_ID, list.get(j).getAmtAcctDr(), list.get(j).getAmtAcctCr());
						//  --  Account
						accountRES = list.get(j).getAccount();
						linea.setAccount (accountRES);
						// Business Partner - SalesRegion - Project - Campaign -Activity
//OJO				accountRES.setC_BPartner_ID(BPartner_ID);
						if (accountRES.getC_BPartner_ID() == 0)
							accountRES.setC_BPartner_ID(amnemployee.getC_BPartner_ID());
//OJO
						accountRES.setC_SalesRegion_ID(SalesRegion_ID);
						accountRES.setC_Project_ID(Project_ID);
						accountRES.setC_Campaign_ID(Campaign_ID);
						accountRES.setC_Activity_ID(Activity_ID);	
						accountRES.setDescription(Employee_Value+" "+Employee_Name);
						// ADD RES REGISTER
						listres.add(linea);
//log.warning("account"+accountRES+"  Reg:"+reslin+" j:"+j +
//		"  Acc: "+(iScontain || iScontainDB || iScontainCR ) + 
//		"  DB:"+list.get(j).getAmtAcctDr()+
//		"  CR:"+list.get(j).getAmtAcctCr()+
//		"  DB SOURCE:"+list.get(j).getAmtSourceDr()+
//		"  CR SOURCE:"+list.get(j).getAmtSourceCr()+
//		"  ACUMULADO DB"+list.get(j).getAmtAcctDr()+
//		"  CR"+list.get(j).getAmtAcctCr()+
//		"  DB SOURCE"+list.get(j).getAmtSourceDr()+
//		"  CR SOURCE"+list.get(j).getAmtSourceCr());
						// Increment reslin
						reslin=reslin+1;
					}

			}		
		}	//	END VERIFY DUPLICITY
		//	Return Array
		int sizeres = listres.size();
		DocLine[] dls = new DocLine[sizeres];
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

	public ArrayList<Fact> createFacts(MAcctSchema as) {
//log.warning("===== createFacts =========");
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//int c_elementvalue_id =0;
		//  create Fact Header
		Fact fact = new Fact (this, as, m_PostingType);
//log.warning("m_PostingType:"+m_PostingType+"  p_lines.length:"+p_lines.length);
		//  account     DR      CR
		for (int i = 0; i < p_lines.length; i++)
		{
				FactLine line = fact.createLine (p_lines[i],
								p_lines[i].getAccount (),
								getC_Currency_ID(),
								p_lines[i].getAmtSourceDr (),
								p_lines[i].getAmtSourceCr ());
				// ADD DESCRIPTION 
				line.addDescription(p_lines[i].getDescription());
//log.warning("Account:"+p_lines[i].getAccount()+
//		" AmtDr:"+p_lines[i].getAmtAcctDr().toString()+
//		" AmtCr:"+p_lines[i].getAmtAcctCr().toString()+
//		" AmtSourceDr:"+p_lines[i].getAmtSourceDr().toString()+
//		" AmtSourceCr:"+p_lines[i].getAmtSourceCr().toString()		);
		}	//	for all lines
		//
		facts.add(fact);
		return facts;
	}
	
	public boolean isDuplicate(ArrayList<DocLine[]> lista){

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

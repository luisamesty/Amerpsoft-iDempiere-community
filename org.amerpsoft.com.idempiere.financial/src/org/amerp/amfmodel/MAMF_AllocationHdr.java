package org.amerp.amfmodel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.amerp.amfdocument.Doc_AMFInvoice;
import org.amerp.amfdocument.Doc_AMFOrder;
import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.DocLine_Allocation;
import org.compiere.acct.Doc_AllocationHdr;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MCashLine;
import org.compiere.model.MConversionRate;
import org.compiere.model.MElementValue;
import org.compiere.model.MFactAcct;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MPeriod;
import org.compiere.model.PO;
import org.compiere.model.X_C_ValidCombination;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MAMF_AllocationHdr  { 

	public static DocLine[] p_lines = null;
	public static ArrayList<Fact> facts = null;
	public static ResultSet rs= null;
	public static String m_trxName=null;
	/**	Tolerance G&L				*/
	private static final BigDecimal	TOLERANCE = BigDecimal.valueOf(0.02);
	/** Error Message			*/
	protected String			p_Error = null;
	Doc_AllocationHdr doc = null;
	public int m_C_BankAccount_ID = 0;
	public int m_C_BPartner_ID = 0;
	public int m_C_CashBook_ID = 0;
	/** The Document				*/
	protected PO				p_po = null;
	/**	AP Payment              */
	public static final String 	DOCTYPE_APPayment       = "APP";
	/**
	 * 
	 */
	private static final long serialVersionUID = -3264647736156224011L;
	// CLogger
	CLogger log = CLogger.getCLogger(MAMF_AllocationHdr.class);
	
	public MAMF_AllocationHdr(String m_trxName, String p_Error,
				Doc_AllocationHdr doc, int m_C_BankAccount_ID,
				int m_C_BPartner_ID, int m_C_CashBook_ID, PO p_po, CLogger log) {
			super();
			this.m_trxName = m_trxName;
			this.p_Error = p_Error;
			this.doc = doc;
			this.m_C_BankAccount_ID = m_C_BankAccount_ID;
			this.m_C_BPartner_ID = m_C_BPartner_ID;
			this.m_C_CashBook_ID = m_C_CashBook_ID;
			this.p_po = p_po;
			this.log = log;
		}

	public MAMF_AllocationHdr() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * RePostMAMFAllocationHeader 
	 * @param p_C_AllocationHdr_ID
	 * @param astarget
	 * @return
	 */
	public String RePostMAMFAllocationHeader (int p_C_AllocationHdr_ID , MAcctSchema astarget , String trxName) {

		String Msg_Value ="";	
		boolean isError = false;
		//ArrayList<Fact> facts = null;
		m_trxName= trxName;
		//MAMF_AllocationHdr ah = new MAMF_AllocationHdr(Env.getCtx(),p_C_AllocationHdr_ID, null);
		MAllocationHdr ah = new MAllocationHdr(Env.getCtx(),p_C_AllocationHdr_ID, trxName);
		/**	Contained Doc Lines			*/
		String ahDocStatus = ah.getDocStatus();
		String ahDocAction = ah.getDocAction();
		
		if (ahDocStatus.equalsIgnoreCase(DocAction.STATUS_Drafted) ||
				ahDocStatus.equalsIgnoreCase(DocAction.ACTION_Void) ||
				ahDocStatus.equalsIgnoreCase(DocAction.ACTION_Close)) {
			Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+ahDocStatus+" **   \r\n";
			return Msg_Value;
		}
		//  MAcctSchema Select Client Default 
		// MClientInfo info = MClientInfo.get(Env.getCtx(), ah.getAD_Client_ID(), null); 
		//MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		// Use astarget
		// Process document
		// Msg_Value=( ah.getDocumentNo().trim() +" ");
		//log.warning("C_AllocationHdr_ID="+p_C_AllocationHdr_ID+"  DocumentNo="+ah.getDocumentNo()+" ahDocStatus="+ahDocStatus+"  ahDocAction="+ahDocAction+"  ");
		//DocLine[] doclines = AMMloadLines(mp);
		// Period OPen Test
		MPeriod.testPeriodOpen(Env.getCtx(), ah.getDateAcct(), Doc.DOCTYPE_Allocation, ah.getAD_Org_ID());

		// DOCS
		if (ahDocStatus.equalsIgnoreCase(DocAction.STATUS_Completed) ||
				ahDocStatus.equalsIgnoreCase(DocAction.STATUS_Closed) ||
				ahDocStatus.equalsIgnoreCase(DocAction.STATUS_Reversed)) {	
			// Delete old Facts
			MFactAcct.deleteEx(MAllocationHdr.Table_ID,p_C_AllocationHdr_ID, trxName);
			// Doc
			doc =   (Doc_AllocationHdr) Doc_AllocationHdr.get(astarget, MAllocationHdr.Table_ID, p_C_AllocationHdr_ID, null);
			//Doc_AMFAllocationHdr doc =   new Doc_AMFAllocationHdr(astarget, null, null, Doc.DOCTYPE_Allocation, get_TrxName());
			// set DocumentDetails same as loadDocumentDetails()
			//((Doc) doc).loadLines(mp);
			doc.setDateAcct(ah.getDateAcct());
			doc.setDateDoc(ah.getDateTrx());
			doc.setC_Currency_ID (ah.getC_Currency_ID());
			//doc.setC_BPartner_ID(1000000);
			doc.setC_BPartner_ID(0);
			doc.setC_CashBook_ID(0);
			doc.setC_BankAccount_ID(0);
			// Create NEW FActs
			//  Line pointer
			p_lines = (DocLine[]) AMFloadLines(doc, ah);
			//
			AMFloadDocumentDetails(doc, ah);
			// Crreate Facts Lines fro Account Schema as
			facts = AMFcreateFacts(doc, ah, astarget);
			// Create Fact Accounts
			Fact fs = facts.get(0);
			if (fs.isAcctBalanced()) {
				FactLine[] flines = fs.getLines();
				for (int i = 0 ; i < flines.length ; i=i+1) {
					FactLine fl = flines[i];
					//MAccount ma = fl.getAccount();
					MElementValue ev =  new MElementValue(Env.getCtx(), fl.getAccount().getAccount_ID(), null);
					//log.warning("linea="+i+" Account="+ev.getValue()+"  DR="+fl.getAmtAcctDr()+"  CR="+fl.getAmtAcctCr());
					// CREATE Fact_Acct
					AMF_MFactAcct.createFactAcct(Env.getCtx(), ah.get_Table_ID(), fl, getTrx());
				}
				// SET NEW DOC STATUS
				ah.setPosted(true);
				ah.setProcessed(true);
				ah.setDocAction(ahDocAction);
				ah.setDocStatus(ahDocStatus);
				ah.saveEx();
				isError = false;
			} else {
				isError = true;
				Msg_Value = Msg_Value + "** NOT BALANCED *** ";
			}
			// Create or Update M_CostDetails
		} else {
			Msg_Value=Msg_Value+ " ** "+Msg.translate(Env.getCtx(), "DocStatus")+":"+ahDocStatus+" **   \r\n";
		}
		if (!isError) {
			Msg_Value=Msg_Value+ " ** "+Msg.getMsg(Env.getCtx(), "Success")+" **   \r\n";
		} else {
			Msg_Value=Msg_Value+ " ** "+Msg.getMsg(Env.getCtx(), "Error")+" **   \r\n";
		}
		//log.warning("RePostMAMFAllocationHeader="+Msg_Value);
		return Msg_Value;
	}

	/**
	 * AMFloadDocumentDetails
	 * @param doc
	 * @param alloc
	 * @return
	 */
	public String AMFloadDocumentDetails (Doc_AllocationHdr doc, MAllocationHdr alloc)
	{
		doc.setC_Currency_ID (alloc.getC_Currency_ID());
		doc.setDateDoc (alloc.getDateTrx());
		doc.setDateAcct(alloc.getDateAcct());
		//	Contained Objects
		p_lines =  AMFloadLines(doc,alloc);
		if (log.isLoggable(Level.FINE)) log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails

	/**
	 * AMFloadLines Allocation Header Load Lines
	 * @param doc
	 * @param alloc
	 * @return
	 */
	public DocLine[] AMFloadLines(Doc_AllocationHdr doc, MAllocationHdr alloc)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MAllocationLine[] lines = alloc.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MAllocationLine line = lines[i];
			MAMF_DocLine_Allocation docLine = new MAMF_DocLine_Allocation(line, doc);
			//log.warning("line="+i+"  IDs="+line.getC_AllocationHdr_ID()+"/"+line.getC_AllocationLine_ID()+"  BP="+line.getC_BPartner_ID()+
			//		" Doc="+line.getC_Invoice_ID()+" Amount="+line.getAmount());
			//	Get Payment Conversion Rate
			if (line.getC_Payment_ID() != 0)
			{
				MPayment payment = new MPayment (Env.getCtx(), line.getC_Payment_ID(), null);
				int C_ConversionType_ID = payment.getC_ConversionType_ID();
				docLine.setC_ConversionType_ID(C_ConversionType_ID);
			}
			//
			if (log.isLoggable(Level.FINE)) log.fine(docLine.toString());
			list.add (docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines
	
		
	/**
	 *  Create Facts (the accounting logic) for
	 *  CMA.
	 *  <pre>
	 *  AR_Invoice_Payment
	 *      UnAllocatedCash DR
	 *      or C_Prepayment
	 *      DiscountExp     DR
	 *      WriteOff        DR
	 *      Receivables             CR
	 *  AR_Invoice_Cash
	 *      CashTransfer    DR
	 *      DiscountExp     DR
	 *      WriteOff        DR
	 *      Receivables             CR
	 *
	 *  AP_Invoice_Payment
	 *      Liability       DR
	 *      DiscountRev             CR
	 *      WriteOff                CR
	 *      PaymentSelect           CR
	 *      or V_Prepayment
	 *  AP_Invoice_Cash
	 *      Liability       DR
	 *      DiscountRev             CR
	 *      WriteOff                CR
	 *      CashTransfer            CR
	 *  CashBankTransfer
	 *      -
	 *  ==============================
	 *  Realized Gain & Loss
	 * 		AR/AP			DR		CR
	 * 		Realized G/L	DR		CR
	 *
	 *
	 *  </pre>
	 *  Tax needs to be corrected for discount & write-off;
	 *  Currency gain & loss is realized here.
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> AMFcreateFacts( Doc_AllocationHdr doc, MAllocationHdr ah, MAcctSchema as) 
	{
		//  create Fact Header
		Fact fact = new Fact(doc, as, Fact.POST_Actual);
		Fact factForRGL = new Fact(doc, as, Fact.POST_Actual); // dummy fact (not posted) to calculate Realized Gain & Loss
		boolean isInterOrg = isInterOrg(as);
		//log.warning("AMFCreateFacts Beginning lines="+p_lines.length+" C_AcctSchema_ID="+as.getC_AcctSchema_ID()+" "+as.getName());
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine_Allocation line = (DocLine_Allocation)p_lines[i];
			doc.setC_BPartner_ID(line.getC_BPartner_ID());
			setC_BPartner_ID(line.getC_BPartner_ID());
			//  CashBankTransfer - all references null and Discount/WriteOff = 0
			if (line.getC_Payment_ID() != 0
				&& line.getC_Invoice_ID() == 0 && line.getC_Order_ID() == 0
				&& line.getC_CashLine_ID() == 0 && line.getC_BPartner_ID() == 0
				&& Env.ZERO.compareTo(line.getDiscountAmt()) == 0
				&& Env.ZERO.compareTo(line.getWriteOffAmt()) == 0)
				continue;

			//	Receivables/Liability Amt
			BigDecimal allocationSource = line.getAmtSource()
				.add(line.getDiscountAmt())
				.add(line.getWriteOffAmt());
			BigDecimal allocationSourceForRGL = allocationSource; // for realized gain & loss purposes
			BigDecimal allocationAccounted = Env.ZERO;	// AR/AP balance corrected
			@SuppressWarnings("unused")
			BigDecimal allocationAccountedForRGL = Env.ZERO; // for realized gain & loss purposes

			FactLine fl = null;
			FactLine flForRGL = null;
			MAccount bpAcct = null;		//	Liability/Receivables
			//
			MPayment payment = null;
			if (line.getC_Payment_ID() != 0)
				payment = new MPayment (Env.getCtx(), line.getC_Payment_ID(), m_trxName);
			MInvoice invoice = null;
			if (line.getC_Invoice_ID() != 0)
				invoice = new MInvoice (Env.getCtx(), line.getC_Invoice_ID(), m_trxName);

			//	No Invoice
			if (invoice == null)
			{
					//	adaxa-pb: allocate to charges
			    	// Charge Only 
				if (line.getC_Invoice_ID() == 0 && line.getC_Payment_ID() == 0 && line.getC_Charge_ID() != 0 )
				{
					fl = fact.createLine (line, line.getChargeAccount(as, line.getAmtSource()),
						ah.getC_Currency_ID(), line.getAmtSource());
				}
				//	Payment Only
				else if (line.getC_Invoice_ID() == 0 && line.getC_Payment_ID() != 0)
				{
					fl = fact.createLine (line, getPaymentAcct(as, line.getC_Payment_ID()),
						ah.getC_Currency_ID(), line.getAmtSource(), null);
					if (fl != null && payment != null)
						fl.setAD_Org_ID(payment.getAD_Org_ID());
				}
				else
				{
					p_Error = "Cannot determine SO/PO";
					log.log(Level.SEVERE, p_Error);
					return null;
				}
			}
			//	Sales Invoice
			else if (invoice.isSOTrx())
			{
				// Avoid usage of clearing accounts
				// If both accounts Unallocated Cash and Receivable are equal
				// then don't post

				MAccount acct_unallocated_cash = null;
				if (line.getC_Payment_ID() != 0)
					acct_unallocated_cash =  getPaymentAcct(as, line.getC_Payment_ID());
				else if (line.getC_CashLine_ID() != 0)
					acct_unallocated_cash =  getCashAcct(as, line.getC_CashLine_ID());
				MAccount acct_receivable = doc.getAccount(Doc.ACCTTYPE_C_Receivable, as);
				//log.warning("AMFCreateFacts Sales Invoices After getAccount: Doc.Acct="+acct_receivable.getAccount_ID()+"__"+acct_receivable);

				if ((!as.isPostIfClearingEqual()) && acct_unallocated_cash != null && acct_unallocated_cash.equals(acct_receivable) && (!isInterOrg)) {

					// if not using clearing accounts, then don't post amtsource
					// change the allocationsource to be writeoff + discount
					allocationSource = line.getDiscountAmt().add(line.getWriteOffAmt());


				} else {

					// Normal behavior -- unchanged if using clearing accounts

					//	Payment/Cash	DR
					if (line.getC_Payment_ID() != 0)
					{
						fl = fact.createLine (line, getPaymentAcct(as, line.getC_Payment_ID()),
							ah.getC_Currency_ID(), line.getAmtSource(), null);
						if (fl != null && payment != null)
							fl.setAD_Org_ID(payment.getAD_Org_ID());
					}
					else if (line.getC_CashLine_ID() != 0)
					{
						fl = fact.createLine (line, getCashAcct(as, line.getC_CashLine_ID()),
							ah.getC_Currency_ID(), line.getAmtSource(), null);
						MCashLine cashLine = new MCashLine (Env.getCtx(), line.getC_CashLine_ID(), m_trxName);
						if (fl != null && cashLine.get_ID() != 0)
							fl.setAD_Org_ID(cashLine.getAD_Org_ID());
					}

				}
				// End Avoid usage of clearing accounts

				//	Discount		DR
				if (Env.ZERO.compareTo(line.getDiscountAmt()) != 0)
				{
					fl = fact.createLine (line, doc.getAccount(Doc.ACCTTYPE_DiscountExp, as),
						ah.getC_Currency_ID(), line.getDiscountAmt(), null);
					if (fl != null && payment != null)
						fl.setAD_Org_ID(payment.getAD_Org_ID());
				}
				//	Write off		DR
				if (Env.ZERO.compareTo(line.getWriteOffAmt()) != 0)
				{
					fl = fact.createLine (line, doc.getAccount(Doc.ACCTTYPE_WriteOff, as),
						ah.getC_Currency_ID(), line.getWriteOffAmt(), null);
					if (fl != null && payment != null)
						fl.setAD_Org_ID(payment.getAD_Org_ID());
				}

				//	AR Invoice Amount	CR
				if (as.isAccrual())
				{
					bpAcct = doc.getAccount(Doc.ACCTTYPE_C_Receivable, as);
					fl = fact.createLine (line, bpAcct,
						ah.getC_Currency_ID(), null, allocationSource);		//	payment currency
					if (fl != null)
						allocationAccounted = fl.getAcctBalance().negate();
					if (fl != null && invoice != null)
						fl.setAD_Org_ID(invoice.getAD_Org_ID());

					// for Realized Gain & Loss
					flForRGL = factForRGL.createLine (line, bpAcct,
						ah.getC_Currency_ID(), null, allocationSourceForRGL);		//	payment currency
					if (flForRGL != null)
						allocationAccountedForRGL = flForRGL.getAcctBalance().negate();
				}
				else	//	Cash Based
				{
					allocationAccounted = createCashBasedAcct (as, fact,
						invoice, allocationSource);
					allocationAccountedForRGL = allocationAccounted;
				}
				//log.warning("AMFCreateFacts Sales Invoices line="+i+"  Account="+fl.getAccount()+"  BP:"+fl.getC_BPartner_ID()+" DR="+fl.getAmtAcctDr()+" CR="+fl.getAmtAcctCr());
			}
			//	Purchase Invoice
			else
			{
				// Avoid usage of clearing accounts
				// If both accounts Payment Select and Liability are equal
				// then don't post

				MAccount acct_payment_select = null;
				if (line.getC_Payment_ID() != 0)
					acct_payment_select = getPaymentAcct(as, line.getC_Payment_ID());
				else if (line.getC_CashLine_ID() != 0)
					acct_payment_select = getCashAcct(as, line.getC_CashLine_ID());
				MAccount acct_liability = doc.getAccount(Doc.ACCTTYPE_V_Liability, as);
				boolean isUsingClearing = true;

				// Save original allocation source for realized gain & loss purposes
				allocationSourceForRGL = allocationSourceForRGL.negate();

				if ((!as.isPostIfClearingEqual()) && acct_payment_select != null && acct_payment_select.equals(acct_liability) && (!isInterOrg)) {

					// if not using clearing accounts, then don't post amtsource
					// change the allocationsource to be writeoff + discount
					allocationSource = line.getDiscountAmt().add(line.getWriteOffAmt());
					isUsingClearing = false;

				}
				// End Avoid usage of clearing accounts

				allocationSource = allocationSource.negate();	//	allocation is negative
				//	AP Invoice Amount	DR
				if (as.isAccrual())
				{
					bpAcct = doc.getAccount(Doc.ACCTTYPE_V_Liability, as);
					fl = fact.createLine (line, bpAcct,
						ah.getC_Currency_ID(), allocationSource, null);		//	payment currency
					if (fl != null)
						allocationAccounted = fl.getAcctBalance();
					if (fl != null && invoice != null)
						fl.setAD_Org_ID(invoice.getAD_Org_ID());

					// for Realized Gain & Loss
					flForRGL = factForRGL.createLine (line, bpAcct,
						ah.getC_Currency_ID(), allocationSourceForRGL, null);		//	payment currency
					if (flForRGL != null)
						allocationAccountedForRGL = flForRGL.getAcctBalance();
				}
				else	//	Cash Based
				{
					allocationAccounted = createCashBasedAcct (as, fact,
						invoice, allocationSource);
					allocationAccountedForRGL = allocationAccounted;
				}

				//	Discount		CR
				if (Env.ZERO.compareTo(line.getDiscountAmt()) != 0)
				{
					fl = fact.createLine (line, doc.getAccount(Doc.ACCTTYPE_DiscountRev, as),
						ah.getC_Currency_ID(), null, line.getDiscountAmt().negate());
					if (fl != null && payment != null)
						fl.setAD_Org_ID(payment.getAD_Org_ID());
				}
				//	Write off		CR
				if (Env.ZERO.compareTo(line.getWriteOffAmt()) != 0)
				{
					fl = fact.createLine (line, doc.getAccount(Doc.ACCTTYPE_WriteOff, as),
						ah.getC_Currency_ID(), null, line.getWriteOffAmt().negate());
					if (fl != null && payment != null)
						fl.setAD_Org_ID(payment.getAD_Org_ID());
				}
				//	Payment/Cash	CR
				if (isUsingClearing && line.getC_Payment_ID() != 0) // Avoid usage of clearing accounts
				{
					fl = fact.createLine (line, getPaymentAcct(as, line.getC_Payment_ID()),
						ah.getC_Currency_ID(), null, line.getAmtSource().negate());
					if (fl != null && payment != null)
						fl.setAD_Org_ID(payment.getAD_Org_ID());
				}
				else if (isUsingClearing && line.getC_CashLine_ID() != 0) // Avoid usage of clearing accounts
				{
					fl = fact.createLine (line, getCashAcct(as, line.getC_CashLine_ID()),
						ah.getC_Currency_ID(), null, line.getAmtSource().negate());
					MCashLine cashLine = new MCashLine (Env.getCtx(), line.getC_CashLine_ID(), m_trxName);
					if (fl != null && cashLine.get_ID() != 0)
						fl.setAD_Org_ID(cashLine.getAD_Org_ID());
				}
			}

			//	VAT Tax Correction
			if (invoice != null && as.isTaxCorrection())
			{
				BigDecimal taxCorrectionAmt = Env.ZERO;
				if (as.isTaxCorrectionDiscount())
					taxCorrectionAmt = line.getDiscountAmt();
				if (as.isTaxCorrectionWriteOff())
					taxCorrectionAmt = taxCorrectionAmt.add(line.getWriteOffAmt());
				//
				if (taxCorrectionAmt.signum() != 0)
				{
					if (!createTaxCorrection(as, fact, line,
						doc.getAccount(invoice.isSOTrx() ? Doc.ACCTTYPE_DiscountExp : Doc.ACCTTYPE_DiscountRev, as),
						doc.getAccount(Doc.ACCTTYPE_WriteOff, as), invoice.isSOTrx()))
					{
						p_Error = "Cannot create Tax correction";
						return null;
					}
				}
			}

			//	Realized Gain & Loss
			if (invoice != null
				&& (ah.getC_Currency_ID() != as.getC_Currency_ID()			//	payment allocation in foreign currency
					|| ah.getC_Currency_ID() != line.getInvoiceC_Currency_ID()))	//	allocation <> invoice currency
			{
				p_Error = createRealizedGainLoss (line, ah, as, fact, bpAcct, invoice,
					allocationSource, allocationAccounted);
				if (p_Error != null)
					return null;
			}
			//log.warning("AMFCreateFacts END line="+i+"  Account="+fl.getAccount()+"  BP:"+fl.getC_BPartner_ID()+" DR="+fl.getAmtAcctDr()+" CR="+fl.getAmtAcctCr());
		}	//	for all lines

		// FR [ 1840016 ] Avoid usage of clearing accounts - subject to C_AcctSchema.IsPostIfClearingEqual
		if ( (!as.isPostIfClearingEqual()) && p_lines.length > 0 && (!isInterOrg)) {
			boolean allEquals = true;
			// more than one line (i.e. crossing one payment+ with a payment-, or an invoice against a credit memo)
			// verify if the sum of all facts is zero net
			FactLine[] factlines = fact.getLines();
			BigDecimal netBalance = Env.ZERO;
			FactLine prevFactLine = null;
			for (FactLine factLine : factlines) {
				netBalance = netBalance.add(factLine.getAmtSourceDr()).subtract(factLine.getAmtSourceCr());
				if (prevFactLine != null) {
					if (! equalFactLineIDs(prevFactLine, factLine)) {
						allEquals = false;
						break;
					}
				}
				prevFactLine = factLine;
			}
			if (netBalance.compareTo(Env.ZERO) == 0 && allEquals) {
				// delete the postings
				for (FactLine factline : factlines)
					fact.remove(factline);
			}
		}

		//	reset line info
		//setC_BPartner_ID(0);
		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

	/** Verify if the posting involves two or more organizations
	@return true if there are more than one org involved on the posting
	 */
	private boolean isInterOrg(MAcctSchema as) {
		MAcctSchemaElement elementorg = as.getAcctSchemaElement(MAcctSchemaElement.ELEMENTTYPE_Organization);
		if (elementorg == null || !elementorg.isBalanced()) {
			// no org element or not need to be balanced
			return false;
		}

		if (p_lines.length <= 0) {
			// no lines
			return false;
		}

		int startorg = p_lines[0].getAD_Org_ID();
		// validate if the allocation involves more than one org
		for (int i = 0; i < p_lines.length; i++) {
			DocLine_Allocation line = (DocLine_Allocation)p_lines[i];
			int orgpayment = startorg;
			MPayment payment = null;
			if (line.getC_Payment_ID() != 0) {
				payment = new MPayment (Env.getCtx(), line.getC_Payment_ID(), m_trxName);
				orgpayment = payment.getAD_Org_ID();
			}
			int orginvoice = startorg;
			MInvoice invoice = null;
			if (line.getC_Invoice_ID() != 0) {
				invoice = new MInvoice (Env.getCtx(), line.getC_Invoice_ID(), m_trxName);
				orginvoice = invoice.getAD_Org_ID();
			}
			int orgcashline = startorg;
			MCashLine cashline = null;
			if (line.getC_CashLine_ID() != 0) {
				cashline = new MCashLine (Env.getCtx(), line.getC_CashLine_ID(), m_trxName);
				orgcashline = cashline.getAD_Org_ID();
			}
			int orgorder = startorg;
			MOrder order = null;
			if (line.getC_Order_ID() != 0) {
				order = new MOrder (Env.getCtx(), line.getC_Order_ID(), m_trxName);
				orgorder = order.getAD_Org_ID();
			}

			if (   line.getAD_Org_ID() != startorg
				|| orgpayment != startorg
				|| orginvoice != startorg
				|| orgcashline != startorg
				|| orgorder != startorg)
				return true;
		}

		return false;
	}

	/**
	 * Compare the dimension ID's from two factlines
	 * @param allEquals
	 * @param prevFactLine
	 * @param factLine
	 * @return boolean indicating if both dimension ID's are equal
	 */
	private boolean equalFactLineIDs(FactLine prevFactLine, FactLine factLine) {
		return (factLine.getA_Asset_ID() == prevFactLine.getA_Asset_ID()
				&& factLine.getAccount_ID() == prevFactLine.getAccount_ID()
				&& factLine.getAD_Client_ID() == prevFactLine.getAD_Client_ID()
				&& factLine.getAD_Org_ID() == prevFactLine.getAD_Org_ID()
				&& factLine.getAD_OrgTrx_ID() == prevFactLine.getAD_OrgTrx_ID()
				&& factLine.getC_AcctSchema_ID() == prevFactLine.getC_AcctSchema_ID()
				&& factLine.getC_Activity_ID() == prevFactLine.getC_Activity_ID()
				&& factLine.getC_BPartner_ID() == prevFactLine.getC_BPartner_ID()
				&& factLine.getC_Campaign_ID() == prevFactLine.getC_Campaign_ID()
				&& factLine.getC_Currency_ID() == prevFactLine.getC_Currency_ID()
				&& factLine.getC_LocFrom_ID() == prevFactLine.getC_LocFrom_ID()
				&& factLine.getC_LocTo_ID() == prevFactLine.getC_LocTo_ID()
				&& factLine.getC_Period_ID() == prevFactLine.getC_Period_ID()
				&& factLine.getC_Project_ID() == prevFactLine.getC_Project_ID()
				&& factLine.getC_ProjectPhase_ID() == prevFactLine.getC_ProjectPhase_ID()
				&& factLine.getC_ProjectTask_ID() == prevFactLine.getC_ProjectTask_ID()
				&& factLine.getC_SalesRegion_ID() == prevFactLine.getC_SalesRegion_ID()
				&& factLine.getC_SubAcct_ID() == prevFactLine.getC_SubAcct_ID()
				&& factLine.getC_Tax_ID() == prevFactLine.getC_Tax_ID()
				&& factLine.getC_UOM_ID() == prevFactLine.getC_UOM_ID()
				&& factLine.getGL_Budget_ID() == prevFactLine.getGL_Budget_ID()
				&& factLine.getGL_Category_ID() == prevFactLine.getGL_Category_ID()
				&& factLine.getM_Locator_ID() == prevFactLine.getM_Locator_ID()
				&& factLine.getM_Product_ID() == prevFactLine.getM_Product_ID()
				&& factLine.getUserElement1_ID() == prevFactLine.getUserElement1_ID()
				&& factLine.getUserElement2_ID() == prevFactLine.getUserElement2_ID()
				&& factLine.getUser1_ID() == prevFactLine.getUser1_ID()
				&& factLine.getUser2_ID() == prevFactLine.getUser2_ID());
	}

	/**
	 * 	Create Cash Based Acct
	 * 	@param as accounting schema
	 *	@param fact fact
	 *	@param invoice invoice
	 *	@param allocationSource allocation amount (incl discount, writeoff)
	 *	@return Accounted Amt
	 */
	private BigDecimal createCashBasedAcct (MAcctSchema as, Fact fact, MInvoice invoice,
		BigDecimal allocationSource)
	{
		BigDecimal allocationAccounted = Env.ZERO;
		//	Multiplier
		double percent = invoice.getGrandTotal().doubleValue() / allocationSource.doubleValue();
		if (percent > 0.99 && percent < 1.01)
			percent = 1.0;
		if (log.isLoggable(Level.CONFIG)) log.config("Multiplier=" + percent + " - GrandTotal=" + invoice.getGrandTotal()
			+ " - Allocation Source=" + allocationSource);

		//	Get Invoice Postings
		Doc_AMFInvoice docInvoice = (Doc_AMFInvoice)Doc.get(as,
			MInvoice.Table_ID, invoice.getC_Invoice_ID(), m_trxName);
		docInvoice.loadDocumentDetails();
		allocationAccounted = docInvoice.createFactCash(as, fact, BigDecimal.valueOf(percent));
		if (log.isLoggable(Level.CONFIG)) log.config("Allocation Accounted=" + allocationAccounted);

		//	Cash Based Commitment Release
		if (as.isCreatePOCommitment() && !invoice.isSOTrx())
		{
			MInvoiceLine[] lines = invoice.getLines();
			for (int i = 0; i < lines.length; i++)
			{
				Fact factC = Doc_AMFOrder.getCommitmentRelease(as, doc,
					lines[i].getQtyInvoiced(), lines[i].getC_InvoiceLine_ID(), BigDecimal.valueOf(percent));
				if (factC == null)
					return null;
				facts.add(factC);
			}
		}	//	Commitment

		return allocationAccounted;
	}	//	createCashBasedAcct


	/**
	 * 	Get Payment (Unallocated Payment or Payment Selection) Acct of Bank Account
	 *	@param as accounting schema
	 *	@param C_Payment_ID payment
	 *	@return acct
	 */
	private MAccount getPaymentAcct (MAcctSchema as, int C_Payment_ID)
	{
		setC_BankAccount_ID(0);
		//	Doc.ACCTTYPE_UnallocatedCash (AR) or C_Prepayment
		//	or Doc.ACCTTYPE_PaymentSelect (AP) or V_Prepayment
		int accountType = Doc.ACCTTYPE_UnallocatedCash;
		//
		String sql = "SELECT p.C_BankAccount_ID, d.DocBaseType, p.IsReceipt, p.IsPrepayment "
				+ "FROM C_Payment p INNER JOIN C_DocType d ON (p.C_DocType_ID=d.C_DocType_ID) "
				+ "WHERE C_Payment_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, m_trxName);
			pstmt.setInt (1, C_Payment_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				doc.setC_BankAccount_ID(rs.getInt(1));
				setC_BankAccount_ID(rs.getInt(1));
				if (DOCTYPE_APPayment.equals(rs.getString(2)))
					accountType = Doc.ACCTTYPE_PaymentSelect;
				//	Prepayment
				if ("Y".equals(rs.getString(4)))		//	Prepayment
				{
					if ("Y".equals(rs.getString(3)))	//	Receipt
						accountType = Doc.ACCTTYPE_C_Prepayment;
					else
						accountType = Doc.ACCTTYPE_V_Prepayment;
				}
			}
 		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		//
		if (getC_BankAccount_ID() <= 0)
		{
			log.log(Level.SEVERE, "NONE for C_Payment_ID=" + C_Payment_ID);
			return null;
		}
		return doc.getAccount (accountType, as);
	}	//	getPaymentAcct

	/**
	 * 	Get Cash (Transfer) Acct of CashBook
	 *	@param as accounting schema
	 *	@param C_CashLine_ID
	 *	@return acct
	 */
	private MAccount getCashAcct (MAcctSchema as, int C_CashLine_ID)
	{
		String sql = "SELECT c.C_CashBook_ID "
				+ "FROM C_Cash c, C_CashLine cl "
				+ "WHERE c.C_Cash_ID=cl.C_Cash_ID AND cl.C_CashLine_ID=?";
		setC_CashBook_ID(DB.getSQLValue(null, sql, C_CashLine_ID));

		if (getC_CashBook_ID() <= 0)
		{
			log.log(Level.SEVERE, "NONE for C_CashLine_ID=" + C_CashLine_ID);
			return null;
		}
		return doc.getAccount(Doc.ACCTTYPE_CashTransfer, as);
	}	//	getCashAcct


	/**************************************************************************
	 * 	Create Realized Gain & Loss.
	 * 	Compares the Accounted Amount of the Invoice to the
	 * 	Accounted Amount of the Allocation
	 *	@param as accounting schema
	 *	@param fact fact
	 *	@param acct account
	 *	@param invoice invoice
	 *	@param allocationSource source amt
	 *	@param allocationAccounted acct amt
	 *	@return Error Message or null if OK
	 */
	private String createRealizedGainLoss (DocLine line, MAllocationHdr ah, MAcctSchema as, Fact fact, MAccount acct,
		MInvoice invoice, BigDecimal allocationSource, BigDecimal allocationAccounted)
	{
		//log.warning("createRealizedGainLoss:  line="+line.get_ID()+"  allocationSource="+allocationSource+"  allocationAccounted"+allocationAccounted);
		BigDecimal invoiceSource = null;
		BigDecimal invoiceAccounted = null;
		//
		StringBuilder sql = new StringBuilder("SELECT ")
			.append(invoice.isSOTrx()
				? "SUM(AmtSourceDr), SUM(AmtAcctDr)"	//	so
				: "SUM(AmtSourceCr), SUM(AmtAcctCr)")	//	po
			.append(" FROM Fact_Acct ")
			.append("WHERE AD_Table_ID=318 AND Record_ID=?")	//	Invoice
			.append(" AND C_AcctSchema_ID=?")
			.append(" AND PostingType='A'");
			//AND C_Currency_ID=102
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), m_trxName);
			pstmt.setInt(1, invoice.getC_Invoice_ID());
			pstmt.setInt(2, as.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				invoiceSource = rs.getBigDecimal(1);
				invoiceAccounted = rs.getBigDecimal(2);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		// 	Requires that Invoice is Posted
		if (invoiceSource == null || invoiceAccounted == null) { 
			invoiceSource = BigDecimal.ZERO;
			invoiceAccounted = BigDecimal.ZERO;
			if (!invoice.isPosted()) {
				return "Gain/Loss - Invoice not posted yet";
			} else {
				log.fine("No Difference");
				return null;
			}
		} 
		//
		StringBuilder description = new StringBuilder("Invoice=(").append(invoice.getC_Currency_ID()).append(")").append(invoiceSource).append("/").append(invoiceAccounted)
			.append(" - Allocation=(").append(ah.getC_Currency_ID()).append(")").append(allocationSource).append("/").append(allocationAccounted);
		if (log.isLoggable(Level.FINE)) log.fine(description.toString());
		//	Allocation not Invoice Currency
		if (ah.getC_Currency_ID() != invoice.getC_Currency_ID())
		{
			BigDecimal allocationSourceNew = MConversionRate.convert(Env.getCtx(),
				allocationSource, ah.getC_Currency_ID(),
				invoice.getC_Currency_ID(), ah.getDateAcct(),
				invoice.getC_ConversionType_ID(), invoice.getAD_Client_ID(), invoice.getAD_Org_ID());
			if (allocationSourceNew == null)
				return "Gain/Loss - No Conversion from Allocation->Invoice";
			StringBuilder d2 = new StringBuilder("Allocation=(").append(ah.getC_Currency_ID()).append(")").append(allocationSource)
				.append("->(").append(invoice.getC_Currency_ID()).append(")").append(allocationSourceNew);
			if (log.isLoggable(Level.FINE)) log.fine(d2.toString());
			description.append(" - ").append(d2);
			allocationSource = allocationSourceNew;
		}

		BigDecimal acctDifference = null;	//	gain is negative
		double multiplier = 0;
		//	Full Payment in currency
		if (allocationSource.compareTo(invoiceSource) == 0)
		{
			acctDifference = invoiceAccounted.subtract(allocationAccounted);	//	gain is negative
			StringBuilder d2 = new StringBuilder("(full) = ").append(acctDifference);
			if (log.isLoggable(Level.FINE)) log.fine(d2.toString());
			description.append(" - ").append(d2);
		}
		else	//	partial or MC
		{
			//	percent of total payment

			if (invoiceSource.compareTo(BigDecimal.ZERO) != 0)
				multiplier = allocationSource.doubleValue() / invoiceSource.doubleValue();
			//	Reduce Orig Invoice Accounted
			invoiceAccounted = invoiceAccounted.multiply(BigDecimal.valueOf(multiplier));
			//	Difference based on percentage of Orig Invoice
			acctDifference = invoiceAccounted.subtract(allocationAccounted);	//	gain is negative
			//	ignore Tolerance
			if (acctDifference.abs().compareTo(TOLERANCE) < 0)
				acctDifference = Env.ZERO;
			//	Round
			int precision = as.getStdPrecision();
			if (acctDifference.scale() > precision)
				acctDifference = acctDifference.setScale(precision, BigDecimal.ROUND_HALF_UP);
			StringBuilder d2 = new StringBuilder("(partial) = ").append(acctDifference).append(" - Multiplier=").append(multiplier);
			if (log.isLoggable(Level.FINE)) log.fine(d2.toString());
			description.append(" - ").append(d2);
		}
		//log.warning("line="+line.get_ID()+" acctDifference="+acctDifference+"  invoiceAccounted="+invoiceAccounted+"  multiplier="+multiplier);
		if (acctDifference.signum() == 0)
		{
			log.fine("No Difference");
			return null;
		}

		MAccount gain = MAccount.get (as.getCtx(), as.getAcctSchemaDefault().getRealizedGain_Acct());
		MAccount loss = MAccount.get (as.getCtx(), as.getAcctSchemaDefault().getRealizedLoss_Acct());
		//
		if (invoice.isSOTrx())
		{
			FactLine fl = fact.createLine (line, loss, gain,
				as.getC_Currency_ID(), acctDifference);
			fl.setDescription(description.toString());
			fact.createLine (line, acct,
				as.getC_Currency_ID(), acctDifference.negate());
			fl.setDescription(description.toString());
		}
		else
		{
			fact.createLine (line, acct,
				as.getC_Currency_ID(), acctDifference);
			@SuppressWarnings("unused")
			FactLine fl = fact.createLine (line, loss, gain,
				as.getC_Currency_ID(), acctDifference.negate());
		}
		return null;
	}	//	createRealizedGainLoss


	/**************************************************************************
	 * 	Create Tax Correction.
	 * 	Requirement: Adjust the tax amount, if you did not receive the full
	 * 	amount of the invoice (payment discount, write-off).
	 * 	Applies to many countries with VAT.
	 * 	Example:
	 * 		Invoice:	Net $100 + Tax1 $15 + Tax2 $5 = Total $120
	 * 		Payment:	$115 (i.e. $5 underpayment)
	 * 		Tax Adjustment = Tax1 = 0.63 (15/120*5) Tax2 = 0.21 (5/120/5)
	 *
	 * 	@param as accounting schema
	 * 	@param fact fact
	 * 	@param line Allocation line
	 *	@param DiscountAccount discount acct
	 *	@param WriteOffAccoint write off acct
	 *	@return true if created
	 */
	private boolean createTaxCorrection (MAcctSchema as, Fact fact,
		DocLine_Allocation line,
		MAccount DiscountAccount, MAccount WriteOffAccoint, boolean isSOTrx)
	{
		if (log.isLoggable(Level.INFO)) log.info (line.toString());
		BigDecimal discount = Env.ZERO;
		if (as.isTaxCorrectionDiscount())
			discount = line.getDiscountAmt();
		BigDecimal writeOff = Env.ZERO;
		if (as.isTaxCorrectionWriteOff())
			writeOff = line.getWriteOffAmt();

		Doc_AllocationTax tax = new Doc_AllocationTax (
			DiscountAccount, discount, 	WriteOffAccoint, writeOff, isSOTrx);

		//	Get Source Amounts with account
		String sql = "SELECT * "
				+ "FROM Fact_Acct "
				+ "WHERE AD_Table_ID=318 AND Record_ID=?"	//	Invoice
				+ " AND C_AcctSchema_ID=?"
				+ " AND Line_ID IS NULL";	//	header lines like tax or total
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, m_trxName);
			pstmt.setInt(1, line.getC_Invoice_ID());
			pstmt.setInt(2, as.getC_AcctSchema_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				tax.addInvoiceFact (new MFactAcct(Env.getCtx(), rs, fact.get_TrxName()));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//	Invoice Not posted
		if (tax.getLineCount() == 0)
		{
			log.warning ("Invoice not posted yet - " + line);
			return false;
		}
		//	size = 1 if no tax
		if (tax.getLineCount() < 2)
			return true;
		return tax.createEntries (as, fact, line);

	}	//	createTaxCorrection
	/**
	 * 	Set C_BankAccount_ID
	 *	@param C_BankAccount_ID bank acct
	 */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		m_C_BankAccount_ID = C_BankAccount_ID;
	}	//	setC_BankAccount_ID
	/**
	 * 	Get C_BankAccount_ID
	 *	@return BankAccount
	 */
	public int getC_BankAccount_ID()
	{
		if (m_C_BankAccount_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_BankAccount_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_BankAccount_ID = ii.intValue();
			}
			if (m_C_BankAccount_ID == -1)
				m_C_BankAccount_ID = 0;
		}
		return m_C_BankAccount_ID;
	}	//	getC_BankAccount_ID
	/**
	 * 	Set C_BPartner_ID
	 *	@param C_BPartner_ID bp
	 */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		m_C_BPartner_ID = C_BPartner_ID;
	}	//	setC_BPartner_ID	
	
	/**
	 * 	Set C_CashBook_ID
	 *	@param C_CashBook_ID cash book
	 */
	public void setC_CashBook_ID (int C_CashBook_ID)
	{
		m_C_CashBook_ID = C_CashBook_ID;
	}	//	setC_CashBook_ID
	
	/**
	 * 	Get C_CashBook_ID
	 *	@return CashBook
	 */
	public int getC_CashBook_ID()
	{
		if (m_C_CashBook_ID == -1)
		{
			int index = p_po.get_ColumnIndex("C_CashBook_ID");
			if (index != -1)
			{
				Integer ii = (Integer)p_po.get_Value(index);
				if (ii != null)
					m_C_CashBook_ID = ii.intValue();
			}
			if (m_C_CashBook_ID == -1)
				m_C_CashBook_ID = 0;
		}
		return m_C_CashBook_ID;
	}	//	getC_CashBook_ID
	
	private static String getTrx() {
		// TODO Auto-generated method stub
		return m_trxName;
	}
	
	public static String getM_trxName() {
		return m_trxName;
	}

	public static void setM_trxName(String m_trxName) {
		MAMF_AllocationHdr.m_trxName = m_trxName;
	}

	/**
	 * 	Allocation Document Tax Handing
	 *
	 *  @author Jorg Janke
	 *  @version $Id: Doc_Allocation.java,v 1.6 2006/07/30 00:53:33 jjanke Exp $
	 */
	class Doc_AllocationTax
	{
		/**
		 * 	Allocation Tax Adjustment
		 *	@param DiscountAccount discount acct
		 *	@param DiscountAmt discount amt
		 *	@param WriteOffAccount write off acct
		 *	@param WriteOffAmt write off amt
		 */
		public Doc_AllocationTax (MAccount DiscountAccount, BigDecimal DiscountAmt,
			MAccount WriteOffAccount, BigDecimal WriteOffAmt, boolean isSOTrx)
		{
			m_DiscountAccount = DiscountAccount;
			m_DiscountAmt = DiscountAmt;
			m_WriteOffAccount = WriteOffAccount;
			m_WriteOffAmt = WriteOffAmt;
			m_IsSOTrx = isSOTrx;
		}	//	Doc_AllocationTax

		private CLogger				log = CLogger.getCLogger(getClass());

		private MAccount			m_DiscountAccount;
		private BigDecimal 			m_DiscountAmt;
		private MAccount			m_WriteOffAccount;
		private BigDecimal 			m_WriteOffAmt;
		private boolean 			m_IsSOTrx;

		private ArrayList<MFactAcct>	m_facts  = new ArrayList<MFactAcct>();
		private int					m_totalIndex = 0;

		/**
		 * 	Add Invoice Fact Line
		 *	@param fact fact line
		 */
		public void addInvoiceFact (MFactAcct fact)
		{
			m_facts.add(fact);
		}	//	addInvoiceLine

		/**
		 * 	Get Line Count
		 *	@return number of lines
		 */
		public int getLineCount()
		{
			return m_facts.size();
		}	//	getLineCount

		/**
		 * 	Create Accounting Entries
		 *	@param as account schema
		 *	@param fact fact to add lines
		 *	@param line line
		 *	@return true if created
		 */
		public boolean createEntries (MAcctSchema as, Fact fact, DocLine line)
		{
			//	get total index (the Receivables/Liabilities line)
			BigDecimal total = Env.ZERO;
			for (int i = 0; i < m_facts.size(); i++)
			{
				MFactAcct factAcct = (MFactAcct)m_facts.get(i);
				if (factAcct.getAmtSourceDr().compareTo(total) > 0)
				{
					total = factAcct.getAmtSourceDr();
					m_totalIndex = i;
				}
				if (factAcct.getAmtSourceCr().compareTo(total) > 0)
				{
					total = factAcct.getAmtSourceCr();
					m_totalIndex = i;
				}
			}

			MFactAcct factAcct = (MFactAcct)m_facts.get(m_totalIndex);
			if (log.isLoggable(Level.INFO)) log.info ("Total Invoice = " + total + " - " +  factAcct);
			int precision = as.getStdPrecision();
			for (int i = 0; i < m_facts.size(); i++)
			{
				//	No Tax Line
				if (i == m_totalIndex)
					continue;

				factAcct = (MFactAcct)m_facts.get(i);
				if (log.isLoggable(Level.INFO)) log.info (i + ": " + factAcct);

				//	Create Tax Account
				MAccount taxAcct = factAcct.getMAccount();
				if (taxAcct == null || taxAcct.get_ID() == 0)
				{
					log.severe ("Tax Account not found/created");
					return false;
				}


//				Discount Amount
				if (m_DiscountAmt.signum() != 0)
				{
					//	Original Tax is DR - need to correct it CR
					if (Env.ZERO.compareTo(factAcct.getAmtSourceDr()) != 0)
					{
						BigDecimal amount = calcAmount(factAcct.getAmtSourceDr(),
							total, m_DiscountAmt, precision);
						if (amount.signum() != 0)
						{
							//for sales actions
							if (m_IsSOTrx) {
								fact.createLine (line, m_DiscountAccount,
									as.getC_Currency_ID(), amount, null);
								fact.createLine (line, taxAcct,
									as.getC_Currency_ID(), null, amount);
							} else {
							//for purchase actions
								fact.createLine (line, m_DiscountAccount,
									as.getC_Currency_ID(), amount.negate(), null);
								fact.createLine (line, taxAcct,
									as.getC_Currency_ID(), null, amount.negate());
							}

						}
					}
					//	Original Tax is CR - need to correct it DR
					else
					{
						BigDecimal amount = calcAmount(factAcct.getAmtSourceCr(),
							total, m_DiscountAmt, precision);
						if (amount.signum() != 0)
						{
//							for sales actions
							if (m_IsSOTrx) {
								fact.createLine (line, taxAcct,
									as.getC_Currency_ID(), amount, null);
								fact.createLine (line, m_DiscountAccount,
									as.getC_Currency_ID(), null, amount);
							} else {
								fact.createLine (line, taxAcct,
									as.getC_Currency_ID(), amount.negate(), null);
								fact.createLine (line, m_DiscountAccount,
									as.getC_Currency_ID(), null, amount.negate());
							}
						}
					}
				}	//	Discount

				//	WriteOff Amount
				if (m_WriteOffAmt.signum() != 0)
				{
					//	Original Tax is DR - need to correct it CR
					if (Env.ZERO.compareTo(factAcct.getAmtSourceDr()) != 0)
					{
						BigDecimal amount = calcAmount(factAcct.getAmtSourceDr(),
							total, m_WriteOffAmt, precision);
						if (amount.signum() != 0)
						{
							fact.createLine (line, m_WriteOffAccount,
								as.getC_Currency_ID(), amount, null);
							fact.createLine (line, taxAcct,
								as.getC_Currency_ID(), null, amount);
						}
					}
					//	Original Tax is CR - need to correct it DR
					else
					{
						BigDecimal amount = calcAmount(factAcct.getAmtSourceCr(),
							total, m_WriteOffAmt, precision);
						if (amount.signum() != 0)
						{
							fact.createLine (line, taxAcct,
								as.getC_Currency_ID(), amount, null);
							fact.createLine (line, m_WriteOffAccount,
								as.getC_Currency_ID(), null, amount);
						}
					}
				}	//	WriteOff

			}	//	for all lines
			return true;
		}	//	createEntries

		/**
		 * 	Calc Amount tax / total * amt
		 *	@param tax tax
		 *	@param total total
		 *	@param amt reduction amt
		 *	@param precision precision
		 *	@return tax / total * amt
		 */
		private BigDecimal calcAmount (BigDecimal tax, BigDecimal total, BigDecimal amt, int precision)
		{
			if (log.isLoggable(Level.FINE)) log.fine("Amt=" + amt + " - Total=" + total + ", Tax=" + tax);
			if (tax.signum() == 0
				|| total.signum() == 0
				|| amt.signum() == 0)
				return Env.ZERO;
			//
			BigDecimal multiplier = tax.divide(total, 10, BigDecimal.ROUND_HALF_UP);
			BigDecimal retValue = multiplier.multiply(amt);
			if (retValue.scale() > precision)
				retValue = retValue.setScale(precision, BigDecimal.ROUND_HALF_UP);
			if (log.isLoggable(Level.FINE)) log.fine(retValue + " (Mult=" + multiplier + "(Prec=" + precision + ")");
			return retValue;
		}	//	calcAmount
	}
}

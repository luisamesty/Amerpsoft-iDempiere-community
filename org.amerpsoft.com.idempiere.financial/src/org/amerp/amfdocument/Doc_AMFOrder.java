package org.amerp.amfdocument;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.Doc_Order;
import org.compiere.acct.Fact;
import org.compiere.acct.FactLine;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.ProductCost;
import org.compiere.util.Env;

public class Doc_AMFOrder extends Doc_Order{

	public Doc_AMFOrder(MAcctSchema as, ResultSet rs, String trxName) {
		super(as, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/** Error Message			*/
	public static String			p_AMF_Error = null;
	public static String			p_Error = null;

	/**
	 * 	Get Commitment Release.
	 * 	Called from MatchInv for accrual and Allocation for Cash Based
	 *	@param as accounting schema
	 *	@param doc doc
	 *	@param Qty qty invoiced/matched
	 *	@param C_InvoiceLine_ID line
	 *	@param multiplier 1 for accrual
	 *	@return Fact
	 */
	public static Fact getCommitmentRelease(MAcctSchema as, Doc doc,
		BigDecimal Qty, int C_InvoiceLine_ID, BigDecimal multiplier)
	{
		Fact fact = new Fact(doc, as, Fact.POST_Commitment);
		DocLine[] commitments = Doc_AMFOrder.getCommitments(doc, Qty,
				C_InvoiceLine_ID);

		BigDecimal total = Env.ZERO;
		@SuppressWarnings("unused")
		FactLine fl = null;
		int C_Currency_ID = -1;
		for (int i = 0; i < commitments.length; i++)
		{
			DocLine line = commitments[i];
			if (C_Currency_ID == -1)
				C_Currency_ID = line.getC_Currency_ID();
			else if (C_Currency_ID != line.getC_Currency_ID())
			{
				p_Error = "Different Currencies of Order Lines";
				s_log.log(Level.SEVERE, p_Error);
				return null;
			}
			BigDecimal cost = line.getAmtSource().multiply(multiplier);
			total = total.add(cost);

			//	Account
			MAccount expense = line.getAccount(ProductCost.ACCTTYPE_P_Expense, as);
			fl = fact.createLine (line, expense,
				C_Currency_ID, null, cost);
		}
		//	Offset
		MAccount offset = doc.getAccount(ACCTTYPE_CommitmentOffset, as);
		if (offset == null)
		{
			p_Error = "@NotFound@ @CommitmentOffset_Acct@";
			s_log.log(Level.SEVERE, p_Error);
			return null;
		}
		fact.createLine (null, offset,
			C_Currency_ID, total, null);
		return fact;
	}	//	getCommitmentRelease

}

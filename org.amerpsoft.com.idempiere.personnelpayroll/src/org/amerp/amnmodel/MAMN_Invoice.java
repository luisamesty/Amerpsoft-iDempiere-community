package org.amerp.amnmodel;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_Persistent;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceBatch;
import org.compiere.model.MInvoiceBatchLine;
import org.compiere.model.MOrder;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;

public class MAMN_Invoice extends MInvoice  implements DocAction, DocOptions {

	private static final long serialVersionUID = 1L;
	
	static CLogger log = CLogger.getCLogger(MAMN_Invoice.class);
	
	/**	Process Message 			*/
	private String		am_processMsg = null;
	/**	Process Message 			*/
	private String		m_processMsg = null;
	
	public MAMN_Invoice(MInOut ship, Timestamp invoiceDate) {
		super(ship, invoiceDate);
		// 
	}

	public MAMN_Invoice(MInvoice copy) {
		super(copy);
		// 
	}

	public MAMN_Invoice(MInvoiceBatch batch, MInvoiceBatchLine line) {
		super(batch, line);
		// 
	}

	public MAMN_Invoice(MOrder order, int C_DocTypeTarget_ID, Timestamp invoiceDate) {
		super(order, C_DocTypeTarget_ID, invoiceDate);
		// 
	}
	
	public MAMN_Invoice(Properties ctx, int C_Invoice_ID, String trxName) {
		super(ctx, C_Invoice_ID, trxName);
		// 
	}

	public MAMN_Invoice(Properties ctx, int C_Invoice_ID, String trxName, String[] virtualColumns) {
		super(ctx, C_Invoice_ID, trxName, virtualColumns);
		// 
	}

	public MAMN_Invoice(Properties ctx, MInvoice copy) {
		super(ctx, copy);
		
	}


	public MAMN_Invoice(Properties ctx, MInvoice copy, String trxName) {
		super(ctx, copy, trxName);
		
	}

	public MAMN_Invoice(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		
	}

	public MAMN_Invoice(Properties ctx, String C_Invoice_UU, String trxName) {
		super(ctx, C_Invoice_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	@Override
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	process

	@Override
	public int customizeValidActions(String docStatus, Object processing, String orderType, String isSOTrx,
			int AD_Table_ID, String[] docAction, String[] options, int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}

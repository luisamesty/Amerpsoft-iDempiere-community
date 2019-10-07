package org.globalqss.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MTaxCategory;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.globalqss.model.LCO_MInvoice;
import org.globalqss.model.MLCOInvoiceWHDoc;
import org.globalqss.model.MLCOInvoiceWHDocLines;
import org.globalqss.model.MLCOInvoiceWithholding;
import org.globalqss.model.MLCOTaxCategory;

public class LCO_GenerateWithholdingDocLines extends SvrProcess {

	// Parameter Vars
	int p_LCO_InvoiceWHDoc_ID=0;
	int p_C_BPartner_ID = 0;
	Timestamp p_FirstDate  =null;
	Timestamp p_LastDate  =null;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
	   	ProcessInfoParameter[] paras = getParameter();
			for (ProcessInfoParameter para : paras)
			{
				String paraName = para.getParameterName();
				if (paraName.equals("LCO_InvoiceWHDoc_ID"))
					p_LCO_InvoiceWHDoc_ID =  para.getParameterAsInt();
				else if (paraName.equals("C_BPartner_ID"))
					p_C_BPartner_ID =  para.getParameterAsInt();
				else if (paraName.equals("FirstDate"))
					p_FirstDate =  para.getParameterAsTimestamp();
				else if (paraName.equals("LastDate"))
					p_LastDate =  para.getParameterAsTimestamp();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
			}	 
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		Properties ctx = Env.getCtx();
		Integer mtaxcatid = 0;
		int Line = 0;
		MLCOTaxCategory mtaxcat = null;
		String DateIni = p_FirstDate.toString().substring(0,10);
		String DateEnd = p_LastDate.toString().substring(0,10);
		String WithHoldingFormat="IVA";   // Posible values IVA ISLR MUNICIPAL
		String Message = "";
		int LCO_InvoiceWithholding_ID=0;
		// LCO_InvoiceWHDoc_ID
		MLCOInvoiceWHDoc lcoinvoicewhdoc = new MLCOInvoiceWHDoc(ctx, p_LCO_InvoiceWHDoc_ID , null);
		MBPartner bpa = new MBPartner(ctx, p_C_BPartner_ID,null);
		// C_TaxCategory_ID
		mtaxcatid= lcoinvoicewhdoc.getC_TaxCategory_ID();
		if ( mtaxcatid != null)  {
			mtaxcat = new MLCOTaxCategory(ctx, lcoinvoicewhdoc.getC_TaxCategory_ID(),null);
			WithHoldingFormat = mtaxcat.getwithholdingformat();
		}
		// Check IF WithHoldingFormat NUll
		if (WithHoldingFormat == null)
			WithHoldingFormat = sqlGetWithHoldingFormat(lcoinvoicewhdoc.getC_DocTypeTarget_ID());
		// 
		// log.warning("WithHoldingFormat="+WithHoldingFormat+"  LCO_InvoiceWHDoc_ID:"+p_LCO_InvoiceWHDoc_ID+"  GrandTotal:"+GrandTotal+"  TotalLines:"+TotalLines+"  WithholdingAmt:"+WithholdingAmt);
	   // Verify if NO LINES in document
	    Line = sqlGetLCOInvoiceWHDocLinesNo(p_LCO_InvoiceWHDoc_ID);
		if ( Line != 0) {
			Message = Msg.getElement(Env.getCtx(), "LCO_InvoiceWHDoc_ID")+":" + p_LCO_InvoiceWHDoc_ID ;
			addLog(Message);	
			Message =  "******* "+Msg.getMsg(Env.getCtx(), "ProcessCancelled")+" *******";
			addLog(Message);
			Message=Msg.getElement(Env.getCtx(), "AcctDate")+"  "+Msg.getMsg(Env.getCtx(), "From")+"/"+Msg.getMsg(Env.getCtx(), "to")+
					":" +DateIni+" / "+ DateEnd ;
			addLog(Message);
			Message=Msg.getMsg(Env.getCtx(), "NoOfLines")+": (" + Line + ") "+" ";
			addLog(Message);
			return null;
		} else {
			Message = Msg.getElement(Env.getCtx(), "LCO_InvoiceWHDoc_ID")+":" + p_LCO_InvoiceWHDoc_ID ;
			addLog(Message);		
		}
		Line = 0;
		String sql = "";
		// LCO_InvoiceWithholding
		sql = "SELECT liw.LCO_InvoiceWithholding_ID, liw.C_Tax_ID, civ.C_Invoice_ID, civ.C_BPartner_ID " +
				" FROM LCO_InvoiceWithholding liw "+
				" LEFT JOIN C_Invoice civ ON civ.C_Invoice_ID = liw.C_Invoice_ID " +
				" LEFT JOIN C_Tax cta ON cta.C_Tax_ID = liw.C_Tax_ID "+
				" LEFT JOIN C_TaxCategory ctc ON ctc.C_TaxCategory_ID = cta.C_TaxCategory_ID " +
				" WHERE C_BPartner_ID= " + p_C_BPartner_ID + 
				" AND civ.DateAcct Between '"+DateIni+"' "+
				" AND '"+DateEnd+"' " +
				" AND civ.reversal_id IS  NULL "+
				" AND ctc.WithHoldingFormat = '" + WithHoldingFormat+ "' " +
				" AND liw.LCO_InvoiceWithholding_ID NOT IN ( " +
				" 	SELECT LCO_InvoiceWithholding_ID " +
				" 	FROM LCO_InvoiceWHDocLines liwdl " + 
				" 	LEFT JOIN LCO_InvoiceWHDoc liwdo ON liwdo.LCO_InvoiceWHDoc_ID = liwdl.LCO_InvoiceWHDoc_ID " +
				" 	WHERE liwdo.C_BPartner_ID= "+ p_C_BPartner_ID + 
				" 	) ";
		// log.warning(sql);
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		// Create Docs Header and Lines
		// **********************
		// Document Header
		// **********************
		// FOR FUTURE PROCEDURE
		// **********************
		// Document Lines
		// **********************
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rspc = pstmt.executeQuery();
			while (rspc.next())
			{
				// Get Invoice Withholding Line
				Line = Line + 10;
				//int p_LCO_InvoiceWHDoc_ID, int p_C_Invoice_ID, int p_Line, BigDecimal p_TotalLines,
				//BigDecimal p_GrandTotal, BigDecimal p_TaxBaseAmt, BigDecimal p_TaxAmt, BigDecimal p_WithholdingAmt,
				//int p_LCO_InvoiceWithholding_ID, BigDecimal p_Percent, String trxName
				
				LCO_InvoiceWithholding_ID = rspc.getInt(1);
				MLCOInvoiceWithholding lcoiwh = new MLCOInvoiceWithholding(getCtx(), LCO_InvoiceWithholding_ID,null);
				// Create the MLCOInvoiceWHDocLines line
				MLCOInvoiceWHDocLines lcoiwdl = new MLCOInvoiceWHDocLines(getCtx(), 0, get_TrxName());
				LCO_MInvoice lcoinv = new LCO_MInvoice(getCtx(), rspc.getInt(3), null);
				// createLCOInvoiceWHDocLines
				/**
				 * createLCOInvoiceWHDocLines
				 * @param ctx
				 * @param p_AD_Client_ID
				 * @param p_AD_Org_ID
				 * @param p_LCO_InvoiceWHDoc_ID
				 * @param p_C_Invoice_ID
				 * @param p_Line
				 * @param p_TotalLines
				 * @param p_GrandTotal
				 * @param p_TaxBaseAmt
				 * @param p_TaxAmt
				 * @param p_WithholdingAmt
				 * @param p_LCO_InvoiceWithholding_ID
				 * @param p_Percent
				 * @param trxName
				 * @return
				 */
				lcoiwdl.createLCOInvoiceWHDocLines(ctx, lcoinv.getAD_Client_ID(),lcoinv.getAD_Org_ID(), 
						p_LCO_InvoiceWHDoc_ID, lcoinv.getC_Invoice_ID(), Line, 
						lcoinv.getTotalLines(), lcoinv.getGrandTotal(), 
						lcoiwh.getTaxBaseAmt(), lcoiwh.getTaxAmt(), lcoiwh.getTaxAmt(), 
						LCO_InvoiceWithholding_ID, 
						lcoiwh.getPercent(), get_TrxName());
				//log.warning("Line="+Line+" p_LCO_InvoiceWHDoc_ID="+p_LCO_InvoiceWHDoc_ID);	
				Message = Msg.getElement(Env.getCtx(), "Line")+":" + Line + " " +
						Msg.getElement(Env.getCtx(), "C_Invoice_ID")+":"+lcoinv.getDocumentNo()+" "+
						Msg.getElement(Env.getCtx(), "DateInvoiced")+":"+lcoinv.getDateInvoiced().toString().substring(0,10)+"  "+
						Msg.getElement(Env.getCtx(), "DateAcct")+":"+lcoinv.getDateAcct().toString().substring(0,10);
			    addLog(Message);
			}				
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Final Message
		if (Line==0) {
			Message=Msg.getElement(Env.getCtx(), "Line")+"(s): (" + Line + ")   *** "+
					Msg.getMsg(Env.getCtx(), "UnprocessedDocs")+" *** " ;
			addLog(Message);	
			Message=Msg.getElement(Env.getCtx(), "AcctDate")+"  "+Msg.getMsg(Env.getCtx(), "From")+"/"+Msg.getMsg(Env.getCtx(), "to")+
					":" +DateIni+" / "+ DateEnd ;
			addLog(Message);	
		} else {
			Line = Line / 10;
			Message=Msg.getMsg(Env.getCtx(), "NoOfLines")+": (" + Line + ") "+" ";
			addLog(Message);	
			Message=Msg.getElement(Env.getCtx(), "AcctDate")+"  "+Msg.getMsg(Env.getCtx(), "From")+"/"+Msg.getMsg(Env.getCtx(), "to")+
					":" +DateIni+" / "+ DateEnd ;
			addLog(Message);	
		}
		return null;
	} //  doIt()

	/**
	 * sqlGetWithHoldingFormat
	 * @param p_DocType_ID
	 * @return
	 */
	public String sqlGetWithHoldingFormat (int p_DocType_ID)
	
	{
		String sql;
		String WithHoldingFormat="";
		// C_Doctype
		sql = "SELECT DocSubTypeWH FROM C_DocType WHERE C_DocType_ID = "+p_DocType_ID;
		WithHoldingFormat = DB.getSQLValueString(null, sql);	
		return WithHoldingFormat;	
	} // sqlGetWithHoldingFormat
	
	/**
	 * sqlGetLCOInvoiceWHDocLinesNo
	 * @param p_LCO_InvoiceWHDoc_ID
	 * @return
	 */
	public int sqlGetLCOInvoiceWHDocLinesNo( int p_LCO_InvoiceWHDoc_ID) {
		int NoLines = 0;
		String sql;
		sql ="SELECT count(*) as NoLines FROM adempiere.lco_invoicewhdoclines WHERE LCO_InvoiceWHDoc_ID="+p_LCO_InvoiceWHDoc_ID;
		//log.warning("sal="+sql);
		NoLines = (int) DB.getSQLValue(null, sql);
		return NoLines;
	} // sqlGetLCOInvoiceWHDocLinesNo
}

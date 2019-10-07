package org.globalqss.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MLCOInvoiceWHDoc extends X_LCO_InvoiceWHDoc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static CLogger	log	= CLogger.getCLogger (MLCOInvoiceWHDoc.class);

	public MLCOInvoiceWHDoc(Properties ctx, int LCO_InvoiceWHDoc_ID,
			String trxName) {
		super(ctx, LCO_InvoiceWHDoc_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MLCOInvoiceWHDoc(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 *	Update updateHeader InvoiceWHDoc
	 *	@return true if header updated with withholding
	 */
	public static boolean updateHeaderLCOInvoiveWHDoc(int LCO_InvoiceWHDoc_ID, String trxName)
	{
		// Get Total Values
		String sql = "";
		sql = "SELECT SUM(GrandTotal) as grandtotal FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID= ?";
		BigDecimal GrandTotal = DB.getSQLValueBD(trxName, sql, LCO_InvoiceWHDoc_ID) ;
		sql = "SELECT SUM(TotalLines) as TotalLines FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID= ?";
		BigDecimal TotalLines = DB.getSQLValueBD(trxName, sql, LCO_InvoiceWHDoc_ID) ;
		sql = "SELECT SUM(WithholdingAmt) as WithholdingAmt FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID= ?";
		BigDecimal WithholdingAmt = DB.getSQLValueBD(trxName, sql, LCO_InvoiceWHDoc_ID) ;
		sql = "SELECT SUM(TaxAmt) as TaxAmt FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID= ?";
		BigDecimal TaxAmt = DB.getSQLValueBD(trxName, sql, LCO_InvoiceWHDoc_ID) ;
		sql = "SELECT SUM(TaxBaseAmt) as TaxBaseAmt FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID= ?";
		BigDecimal TaxBaseAmt = DB.getSQLValueBD(trxName, sql, LCO_InvoiceWHDoc_ID) ;
//log.warning("updateHeaderLCOInvoiveWHDoc.... LCO_InvoiceWHDoc_ID:"+LCO_InvoiceWHDoc_ID);

		//	Update Invoice Header
		sql =
			"UPDATE LCO_InvoiceWHDoc "
			+ " SET WithholdingAmt=" + WithholdingAmt + ",  "
			+ " GrandTotal=" + GrandTotal + ",  "
			+ " TotalLines=" + TotalLines + ",  "
			+ " TaxAmt=" + TaxAmt + ",  "
			+ " TaxBaseAmt=" + TaxBaseAmt + "  "
			+ "WHERE LCO_InvoiceWHDoc_ID=?";
//log.warning("sql:"+sql);
		int no = DB.executeUpdateEx(sql, new Object[] {LCO_InvoiceWHDoc_ID}, trxName);
		return no == 1;
	}	//	updateHeader InvoiceWHDoc

	/**************************************************************************
	 * 	Get LCO_InvoiceWHDoc Lines
	 *	@return Array of lines
	 */
	public MLCOInvoiceWHDocLines[] getLines()
	{
		ArrayList<MLCOInvoiceWHDocLines> list = new ArrayList<MLCOInvoiceWHDocLines>();
		String sql = "SELECT * FROM LCO_InvoiceWHDocLines WHERE LCO_InvoiceWHDoc_ID=? ORDER BY Line";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getLCO_InvoiceWHDoc_ID());
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MLCOInvoiceWHDocLines (getCtx(), rs, get_TrxName()));
		}
		catch (SQLException ex)
		{
			log.log(Level.SEVERE, sql, ex);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		
		//
		MLCOInvoiceWHDocLines[] retValue = new MLCOInvoiceWHDocLines[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

}

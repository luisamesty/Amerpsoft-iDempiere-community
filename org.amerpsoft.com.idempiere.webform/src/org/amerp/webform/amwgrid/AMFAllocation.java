/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.amerp.webform.amwgrid;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MRole;
import org.compiere.model.MSysConfig;
import org.compiere.model.Query;
import org.compiere.process.DocAction;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;

/**
 * 
 * @author hengsin
 *
 */
public class AMFAllocation
{
	protected DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);

	/**	Logger			*/
	protected static final CLogger log = CLogger.getCLogger(AMFAllocation.class);

	private boolean     m_calculating = false;
	protected int       m_C_Currency_ID = 0;
	protected int       m_C_Charge_ID = 0;
	protected int       m_C_DocType_ID = 0;
	protected int       m_C_BPartner_ID = 0;
	public int         	m_AMN_Employee_ID = 0;
	private int         m_noInvoices = 0;
	private int         m_noPayments = 0;
	public int         	m_AMN_Process_ID = 0;
	public int         	m_AMN_Contract_ID = 0;
	public Timestamp 	m_Date = null;
	public Timestamp 	m_DateDoc = null;
	public boolean		m_SelectStatus = false;
	protected BigDecimal	totalInv = Env.ZERO;
	protected BigDecimal 	totalPay = Env.ZERO;
	protected BigDecimal	totalDiff = Env.ZERO;
	
	protected Timestamp allocDate = null;

	//  Index	changed if multi-currency
	private int         i_payment = 7;
	//
	private int         i_open = 6;
	private int         i_discount = 7;
	private int         i_writeOff = 8; 
	private int         i_applied = 9;
	private int 		i_overUnder = 10;
	
	protected int       m_AD_Org_ID = 0;
	public int         	m_docAD_Org_ID = 0;  // Org for Documents

	private ArrayList<Integer>	m_bpartnerCheck = new ArrayList<Integer>(); 

	protected void dynInit() throws Exception
	{
		m_C_Currency_ID = Env.getContextAsInt(Env.getCtx(), Env.C_CURRENCY_ID);   //  default
		//
		if (log.isLoggable(Level.INFO)) log.info("Currency=" + m_C_Currency_ID);
		
		m_AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());
		m_C_DocType_ID= MDocType.getDocType(MDocType.DOCBASETYPE_PaymentAllocation);
		
	}
	
	/**
	 *  Load Business Partner Info
	 *  - Payments
	 *  - Invoices
	 */
	public void checkBPartner()
	{		
		if (log.isLoggable(Level.CONFIG)) log.config("BPartner=" + m_C_BPartner_ID + ", Cur=" + m_C_Currency_ID);
		//  Need to have both values
		if (m_C_BPartner_ID == 0 || m_C_Currency_ID == 0)
			return;

		//	Async BPartner Test
		Integer key = Integer.valueOf(m_C_BPartner_ID);
		if (!m_bpartnerCheck.contains(key))
		{
			new Thread()
			{
				public void run()
				{
					MPayment.setIsAllocated (Env.getCtx(), m_C_BPartner_ID, null);
					MInvoice.setIsPaid (Env.getCtx(), m_C_BPartner_ID, null);
				}
			}.start();
			m_bpartnerCheck.add(key);
		}
	}
	
	/**
	 * @deprecated
	 * @param isMultiCurrency
	 * @param date
	 * @param paymentTable not used
	 * @return list of payment record
	 */
	public Vector<Vector<Object>> getPaymentData(boolean isMultiCurrency, Object date, IMiniTable paymentTable)
	{
		return getPaymentData(isMultiCurrency, (Timestamp) date, (String)null);
	}
	
	/**
	 * 
	 * @param isMultiCurrency
	 * @param date
	 * @param trxName optional trx name
	 * @return list of payment record
	 */
	public Vector<Vector<Object>> getPaymentData(boolean isMultiCurrency, Timestamp date, String trxName)
	{		
		return getUnAllocatedPaymentData(m_C_BPartner_ID, m_C_Currency_ID, isMultiCurrency, date, m_AD_Org_ID, trxName);
	}
	
	/**
	 * 
	 * @param C_BPartner_ID mandatory bpartner filter
	 * @param C_Currency_ID 0 to use login currency. use for payment filter if isMultiCurrency=false
	 * @param isMultiCurrency false to apply currency filter
	 * @param date payment allocation as at date
	 * @param AD_Org_ID 0 for all org
	 * @param trxName optional transaction name
	 * @return list of unallocated payment records.<br/>
	 * - Payment record: Boolean.False, DateTrx, KeyNamePair(C_Payment_ID,DocumentNo), Currency ISO_Code, PayAmt, Converted Amt,Open Amt, 0 <br/> 
	 * - Without Currency ISO_Code and PayAmt if isMultiCurrency is false.
	 */
	public static Vector<Vector<Object>> getUnAllocatedPaymentData(int C_BPartner_ID, int C_Currency_ID, boolean isMultiCurrency, 
			Timestamp date, int AD_Org_ID, String trxName)
	{
		if (C_Currency_ID==0)
			C_Currency_ID = Env.getContextAsInt(Env.getCtx(), Env.C_CURRENCY_ID);   //  default
		
		/********************************
		 *  Load unallocated Payments
		 ********************************/
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder sql = new StringBuilder("SELECT p.DateTrx,p.DocumentNo,p.C_Payment_ID,"  //  1..3
			+ "c.ISO_Code,p.PayAmt,"                            //  4..5
			+ "currencyConvertPayment(p.C_Payment_ID,?,null,?),"//  6   #1, #2
			+ "currencyConvertPayment(p.C_Payment_ID,?,paymentAvailable(p.C_Payment_ID),?),"  //  7   #3, #4
			+ "p.MultiplierAP, "
			+ "p.description, "
	        + "org.Name AS OrgName "                        // ← Organización
			+ "FROM C_Payment_v p"		//	Corrected for AP/AR
			+ " INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID) "
			+ " INNER JOIN AD_Org org ON (p.AD_Org_ID = org.AD_Org_ID) "   
			+ "WHERE p.IsAllocated='N' AND p.Processed='Y'"
			+ " AND p.C_Charge_ID IS NULL"		//	Prepayments OK
			+ " AND p.C_BPartner_ID=?");                   		//      #5
		if (!isMultiCurrency)
			sql.append(" AND p.C_Currency_ID=?");				//      #6
		if (AD_Org_ID != 0 )
			sql.append(" AND p.AD_Org_ID=" + AD_Org_ID);
		sql.append(" ORDER BY p.DateTrx,p.DocumentNo");
		
		// role security
		sql = new StringBuilder( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "p", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		if (log.isLoggable(Level.FINE)) log.fine("PaySQL=" + sql.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trxName);
			pstmt.setInt(1, C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)date);
			pstmt.setInt(3, C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)date);
			pstmt.setInt(5, C_BPartner_ID);
			if (!isMultiCurrency)
				pstmt.setInt(6, C_Currency_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(Boolean.FALSE);       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
				line.add(pp);                       //  2-DocumentNo
				if (isMultiCurrency)
				{
					line.add(rs.getString(4));      //  3-Currency
					line.add(rs.getBigDecimal(5));  //  4-PayAmt
				}
				line.add(rs.getBigDecimal(6));      //  3/5-ConvAmt
				BigDecimal available = rs.getBigDecimal(7);
				if (available == null || available.signum() == 0)	//	nothing available
					continue;
				line.add(available);				//  4/6-ConvOpen/Available
				line.add(Env.ZERO);					//  5/7-Applied
				//
				line.add(rs.getString(10));			//  6/8 Organizacion
				line.add(rs.getString(9));			//  7/9 Description
				//
				data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		
		return data;
	}
	
	/**
	 * 
	 * @param isMultiCurrency
	 * @return column name list for payment data
	 */
	public Vector<String> getPaymentColumnNames(boolean isMultiCurrency)
	{	
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		if (isMultiCurrency)
		{
			columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
			columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		}
		columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
		columnNames.add(Msg.translate(Env.getCtx(), "AD_Org_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		return columnNames;
	}
	
	/**
	 * 
	 * @param paymentTable
	 * @param isMultiCurrency
	 */
	public void setPaymentColumnClass(IMiniTable paymentTable, boolean isMultiCurrency)
	{
		int i = 0;
		paymentTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		paymentTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		paymentTable.setColumnClass(i++, String.class, true);           //  2-Value
		if (isMultiCurrency)
		{
			paymentTable.setColumnClass(i++, String.class, true);       //  3-Currency
			paymentTable.setColumnClass(i++, BigDecimal.class, true);   //  4-PayAmt
		}
		paymentTable.setColumnClass(i++, BigDecimal.class, true);       //  5-ConvAmt
		paymentTable.setColumnClass(i++, BigDecimal.class, true);       //  6-ConvOpen
		paymentTable.setColumnClass(i++, BigDecimal.class, false);      //  7-Allocated
		paymentTable.setColumnClass(i++, String.class, true);      		//  8-Organization
		paymentTable.setColumnClass(i++, String.class, true);      		//  9-Description
		//
		i_payment = isMultiCurrency ? 7 : 5;
		

		//  Table UI
		paymentTable.autoSize();
	}
	
	/**
	 * @deprecated
	 * @param isMultiCurrency
	 * @param date
	 * @param invoiceTable not use
	 * @return list of unpaid invoice data
	 */
	public Vector<Vector<Object>> getInvoiceData(boolean isMultiCurrency, Object date, IMiniTable invoiceTable)
	{
		return getInvoiceData(isMultiCurrency, (Timestamp) date, (String)null);
	}
	
	/**
	 * 
	 * @param isMultiCurrency
	 * @param date
	 * @param trxName optional trx name
	 * @return list of unpaid invoice data
	 */
	public Vector<Vector<Object>> getInvoiceData(boolean isMultiCurrency, Timestamp date, String trxName)
	{
		return getUnpaidInvoiceData(isMultiCurrency, date, m_AD_Org_ID, m_C_Currency_ID, m_C_BPartner_ID, m_AMN_Employee_ID, trxName);
	}

	/**
	 * Get unpaid invoices
	 * @param isMultiCurrency false to apply currency filter
	 * @param date invoice open amount as at date
	 * @param AD_Org_ID 0 for all orgs
	 * @param C_Currency_ID mandatory, use as invoice document filter if isMultiCurrency is false
	 * @param C_BPartner_ID mandatory bpartner filter
	 * @param trxName optional trx name
	 * @return list of unpaid invoice data
	 */
	public Vector<Vector<Object>> getUnpaidInvoiceData(boolean isMultiCurrency, Timestamp date, int AD_Org_ID, int C_Currency_ID, 
			int C_BPartner_ID, int m_AMN_Employee_ID, String trxName)
	{
		/********************************
		 *  Load unpaid Invoices
		 *     
		 *********************************/
		String adLanguage = Env.getAD_Language(Env.getCtx()); // Obtén el idioma del entorno
		// Verificar si la base de datos es PostgreSQL
		boolean isPostgreSQL = DB.isPostgreSQL();
		// Construir la parte de la consulta que varía según el tipo de base de datos
		String docTypeCase = isPostgreSQL
		        ? " COALESCE(dt_trl.PrintName, dt.PrintName, dt.Name) AS DocTypeName "  // Para PostgreSQL
		        : " NVL(dt_trl.PrintName, dt.PrintName, dt.Name) AS DocTypeName ";      // Para Oracle
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder sql = new StringBuilder("SELECT i.DateInvoiced,i.DocumentNo,i.C_Invoice_ID," //  1..3
			+ "c.ISO_Code,i.GrandTotal*i.MultiplierAP, "                            //  4..5    Orig Currency
			+ "currencyConvertInvoice(i.C_Invoice_ID,?,i.GrandTotal*i.MultiplierAP,?), " //  6   #1  Converted, #2 Date
			+ "currencyConvertInvoice(i.C_Invoice_ID,?,invoiceOpen(i.C_Invoice_ID,C_InvoicePaySchedule_ID),?)*i.MultiplierAP, "  //  7   #3, #4  Converted Open
			+ "currencyConvertInvoice(i.C_Invoice_ID"                               //  8       AllowedDiscount
			+ ",?,invoiceDiscount(i.C_Invoice_ID,?,C_InvoicePaySchedule_ID),i.DateInvoiced)*i.Multiplier*i.MultiplierAP,"               //  #5, #6
			+ "i.MultiplierAP, "
			+ "e.Value || ' - ' || e.Name AS EmployeeFullName, "
			+ "bp.Value || ' - ' || bp.Name AS Tercero, "
			+  docTypeCase
			+ "FROM C_Invoice_v i"		//  corrected for CM/Split
			+ " INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) "
			+ " INNER JOIN AMN_Payroll_Docs pd ON (pd.c_invoice_id = i.c_invoice_id ) "
			+ " INNER JOIN AMN_Payroll p ON (p.amn_payroll_id  = pd.amn_payroll_id) "
			+ " INNER JOIN AMN_Employee e ON (e.amn_employee_id = p.amn_employee_id) "
			+ " INNER JOIN C_BPartner bp ON (bp.C_BPartner_ID = i.C_BPartner_ID) "
			+ " INNER JOIN C_DocType dt ON (dt.C_DocType_ID = i.C_DocType_ID) "
			+ " LEFT JOIN C_DocType_Trl dt_trl ON (dt_trl.C_DocType_ID = dt.C_DocType_ID AND dt_trl.AD_Language = '" + adLanguage + "') "
			+ "WHERE i.IsPaid='N' AND i.Processed='Y'"
			+ " AND i.C_BPartner_ID=?" );                                      		//  #7
		if (m_AMN_Employee_ID != 0) {
			sql.append(" AND e.AMN_Employee_ID="+m_AMN_Employee_ID);
		}
		if (!isMultiCurrency)
			sql.append(" AND i.C_Currency_ID=?");                                   //  #8
		if (m_docAD_Org_ID != 0 ) 
			sql.append(" AND i.AD_Org_ID=" + m_docAD_Org_ID);
		if (m_C_DocType_ID != 0) {
		    sql.append(" AND i.C_DocType_ID=").append(m_C_DocType_ID);
		}
		if (m_AMN_Process_ID != 0) {
		    sql.append(" AND p.AMN_Process_ID=").append(m_AMN_Process_ID);
		}
		if (m_AMN_Contract_ID != 0) {
		    sql.append(" AND e.AMN_Contract_ID=").append(m_AMN_Contract_ID);
		}
		if (m_DateDoc != null) {
		    String dateCondition = "";
		    // Determina el tipo de base de datos
		    if (DB.isPostgreSQL()) {
		        // Para PostgreSQL usamos DATE() para comparar solo las fechas
		        dateCondition = " AND DATE(i.DateAcct) = '" + new java.sql.Date(m_DateDoc.getTime()) + "'";
		    } else if (DB.isOracle()) {
		        // Para Oracle usamos TRUNC() para comparar solo las fechas
		        dateCondition = " AND TRUNC(i.DateAcct) = '" + new java.sql.Date(m_DateDoc.getTime()) + "'";
		    }   
		    // Añadir la condición de la fecha a la consulta
		    sql.append(dateCondition);
		}
		sql.append(" ORDER BY i.DateInvoiced, i.DocumentNo");
		if (log.isLoggable(Level.FINE)) log.fine("InvSQL=" + sql.toString());
		
		// role security
		sql = new StringBuilder( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "i", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
log.warning("Sql="+sql);
log.warning("m_AMN_Employee_ID="+m_AMN_Employee_ID);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), trxName);
			pstmt.setInt(1, C_Currency_ID);
			pstmt.setTimestamp(2, date);
			pstmt.setInt(3, C_Currency_ID);
			pstmt.setTimestamp(4, date);
			pstmt.setInt(5, C_Currency_ID);
			pstmt.setTimestamp(6, date);
			pstmt.setInt(7, C_BPartner_ID);
			if (!isMultiCurrency)
				pstmt.setInt(8, C_Currency_ID);
			
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(Boolean.FALSE);       //  0-Selection
				line.add(rs.getTimestamp(1));       //  1-TrxDate
				KeyNamePair pp = new KeyNamePair(rs.getInt(3), rs.getString(2));
				line.add(pp);                       //  2-Value
				if (isMultiCurrency)
				{
					line.add(rs.getString(4));      //  3-Currency
					line.add(rs.getBigDecimal(5));  //  4-Orig Amount
				}
				line.add(rs.getBigDecimal(6));      //  3/5-ConvAmt
				BigDecimal open = rs.getBigDecimal(7);
				if (open == null)		//	no conversion rate
					open = Env.ZERO;
				line.add(open);      				//  4/6-ConvOpen
				BigDecimal discount = rs.getBigDecimal(8);
				if (discount == null)	//	no concersion rate
					discount = Env.ZERO;
				line.add(discount);					//  5/7-ConvAllowedDisc
				line.add(Env.ZERO);      			//  6/8-WriteOff
				line.add(Env.ZERO);					// 7/9-Applied
				line.add(open);				        //  8/10-OverUnder
				line.add(rs.getString(10));			//  9/11-Employee
				line.add(rs.getString(11));			//  10/12-Bill_BPartner
				line.add(rs.getString(12));			//  11/13-DocType
				//	Add when open <> 0 (i.e. not if no conversion rate)
				if (Env.ZERO.compareTo(open) != 0)
					data.add(line);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		
		return data;
	}
	
	
	/**
	 * 
	 * @param isMultiCurrency
	 * @return list of column name/header
	 */
	public Vector<String> getInvoiceColumnNames(boolean isMultiCurrency)
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>();
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.translate(Env.getCtx(), "Date"));
		columnNames.add(Util.cleanAmp(Msg.translate(Env.getCtx(), "DocumentNo")));
		if (isMultiCurrency)
		{
			columnNames.add(Msg.getMsg(Env.getCtx(), "TrxCurrency"));
			columnNames.add(Msg.translate(Env.getCtx(), "Amount"));
		}
		columnNames.add(Msg.getMsg(Env.getCtx(), "ConvertedAmount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OpenAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "Discount"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "WriteOff"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "AppliedAmt"));
		columnNames.add(Msg.getMsg(Env.getCtx(), "OverUnderAmt"));
		columnNames.add(Msg.translate(Env.getCtx(), "AMN_Employee_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "Bill_BPartner_ID"));
		columnNames.add(Msg.translate(Env.getCtx(), "C_DocType_ID"));
		return columnNames;
	}
	
	/**
	 * set class type for each column
	 * @param invoiceTable
	 * @param isMultiCurrency
	 */
	public void setInvoiceColumnClass(IMiniTable invoiceTable, boolean isMultiCurrency)
	{
		int i = 0;
		invoiceTable.setColumnClass(i++, Boolean.class, false);         //  0-Selection
		invoiceTable.setColumnClass(i++, Timestamp.class, true);        //  1-TrxDate
		invoiceTable.setColumnClass(i++, String.class, true);           //  2-Value
		if (isMultiCurrency)
		{
			invoiceTable.setColumnClass(i++, String.class, true);       //  3-Currency
			invoiceTable.setColumnClass(i++, BigDecimal.class, true);   //  4-Amt
		}
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);       //  5-ConvAmt
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);       //  6-ConvAmt Open
		invoiceTable.setColumnClass(i++, BigDecimal.class, false);      //  7-Conv Discount
		invoiceTable.setColumnClass(i++, BigDecimal.class, false);      //  8-Conv WriteOff
		invoiceTable.setColumnClass(i++, BigDecimal.class, false);      //  9-Conv OverUnder
		invoiceTable.setColumnClass(i++, BigDecimal.class, true);		//	10-Conv Applied
		invoiceTable.setColumnClass(i++, String.class, true);      		//  11-Employee
		invoiceTable.setColumnClass(i++, String.class, true);      		//  12-BillBPartner
		invoiceTable.setColumnClass(i++, String.class, true);      		//  13-C_DocType
		//  Table UI
		invoiceTable.autoSize();
	}
	
	/**
	 * set column index for single or multi currency
	 * @param isMultiCurrency
	 */
	protected void prepareForCalculate(boolean isMultiCurrency)
	{
		i_open = isMultiCurrency ? 6 : 4;
		i_discount = isMultiCurrency ? 7 : 5;
		i_writeOff = isMultiCurrency ? 8 : 6;
		i_applied = isMultiCurrency ? 9 : 7;
		i_overUnder = isMultiCurrency ? 10 : 8;
	}   //  loadBPartner
	
	/**
	 * update payment or invoice applied and write off amount
	 * @param row row to update
	 * @param col change is trigger by selected or applied column
	 * @param isInvoice update invoice or payment applied amount
	 * @param payment
	 * @param invoice
	 * @param isAutoWriteOff true to write off difference, false to use over/under for difference
	 * @return warning message (if any)
	 */
	public String writeOff(int row, int col, boolean isInvoice, IMiniTable payment, IMiniTable invoice, boolean isAutoWriteOff)
	{
		String msg = "";
		/**
		 *  Setting defaults
		 */
		if (m_calculating)  //  Avoid recursive calls
			return msg;
		m_calculating = true;
		
		if (log.isLoggable(Level.CONFIG)) log.config("Row=" + row 
			+ ", Col=" + col + ", InvoiceTable=" + isInvoice);
        
		//  Payments
		if (!isInvoice)
		{
			BigDecimal open = (BigDecimal)payment.getValueAt(row, i_open);
			BigDecimal applied = (BigDecimal)payment.getValueAt(row, i_payment);
			
			if (col == 0)
			{
				// selection of payment row
				if (((Boolean)payment.getValueAt(row, 0)).booleanValue())
				{
					applied = open;   //  Open Amount
					if (totalDiff.abs().compareTo(applied.abs()) < 0			// where less is available to allocate than open
							&& totalDiff.signum() == -applied.signum() )    	// and the available amount has the opposite sign
						applied = totalDiff.negate();						// reduce the amount applied to what's available
					
				}
				else    //  de-selected
					applied = Env.ZERO;
			}
			
			
			if (col == i_payment)
			{
				if (! MSysConfig.getBooleanValue(MSysConfig.ALLOW_APPLY_PAYMENT_TO_CREDITMEMO, false, Env.getAD_Client_ID(Env.getCtx())) 
						&& open.signum() > 0 && applied.signum() == -open.signum() )
					applied = applied.negate();
				if (! MSysConfig.getBooleanValue(MSysConfig.ALLOW_OVER_APPLIED_PAYMENT, false, Env.getAD_Client_ID(Env.getCtx())))
					if ( open.abs().compareTo( applied.abs() ) < 0 )
						applied = open;
			}
			
			payment.setValueAt(applied, row, i_payment);
		}

		//  Invoice
		else 
		{
			boolean selected = ((Boolean) invoice.getValueAt(row, 0)).booleanValue();
			BigDecimal open = (BigDecimal)invoice.getValueAt(row, i_open);
			BigDecimal discount = (BigDecimal)invoice.getValueAt(row, i_discount);
			BigDecimal applied = (BigDecimal)invoice.getValueAt(row, i_applied);
			BigDecimal writeOff = (BigDecimal) invoice.getValueAt(row, i_writeOff);
			BigDecimal overUnder = (BigDecimal) invoice.getValueAt(row, i_overUnder);
			int openSign = open.signum();
			
			if (col == 0)  //selection
			{
				//  selected - set applied amount
				if ( selected )
				{
					applied = open;    //  Open Amount
					applied = applied.subtract(discount);
					writeOff = Env.ZERO;  //  to be sure
					overUnder = Env.ZERO;
					totalDiff = Env.ZERO;

					if (totalDiff.abs().compareTo(applied.abs()) < 0			// where less is available to allocate than open
							&& totalDiff.signum() == applied.signum() )     	// and the available amount has the same sign
						applied = totalDiff;									// reduce the amount applied to what's available

					if ( isAutoWriteOff )
						writeOff = open.subtract(applied.add(discount));
					else
						overUnder = open.subtract(applied.add(discount));
				}
				else    //  de-selected
				{
					writeOff = Env.ZERO;
					applied = Env.ZERO;
					overUnder = Env.ZERO;
				}
			}
			
			// check entered values are sensible and possibly auto write-off
			if ( selected && col != 0 )
			{
				
				// values should have same sign as open except possibly over/under
				if ( discount.signum() == -openSign )
					discount = discount.negate();
				if ( writeOff.signum() == -openSign)
					writeOff = writeOff.negate();
				if ( applied.signum() == -openSign )
					applied = applied.negate();
				
				// discount and write-off must be less than open amount
				if ( discount.abs().compareTo(open.abs()) > 0)
					discount = open;
				if ( writeOff.abs().compareTo(open.abs()) > 0)
					writeOff = open;
				
				
				/*
				 * Two rules to maintain:
				 *
				 * 1) |writeOff + discount| < |open| 
				 * 2) discount + writeOff + overUnder + applied = 0
				 *
				 *   As only one column is edited at a time and the initial position was one of compliance
				 *   with the rules, we only need to redistribute the increase/decrease in the edited column to 
				 *   the others.
				*/
				BigDecimal newTotal = discount.add(writeOff).add(applied).add(overUnder);  // all have same sign
				BigDecimal difference = newTotal.subtract(open);
				
				// rule 2
				BigDecimal diffWOD = writeOff.add(discount).subtract(open);
										
				if ( diffWOD.signum() == open.signum() )  // writeOff and discount are too large
				{
					if ( col == i_discount )       // then edit writeoff
					{
						writeOff = writeOff.subtract(diffWOD);
					} 
					else                            // col = i_writeoff
					{
						discount = discount.subtract(diffWOD);
					}
					
					difference = difference.subtract(diffWOD);
				}
				
				// rule 1
				if ( col == i_applied )
					overUnder = overUnder.subtract(difference);
				else
					applied = applied.subtract(difference);
				
			}
			
			//	Warning if write Off > 30%
			if (isAutoWriteOff && writeOff.doubleValue()/open.doubleValue() > .30)
				msg = "AllocationWriteOffWarn";

			invoice.setValueAt(discount, row, i_discount);
			invoice.setValueAt(applied, row, i_applied);
			invoice.setValueAt(writeOff, row, i_writeOff);
			invoice.setValueAt(overUnder, row, i_overUnder);
		}

		m_calculating = false;
		
		return msg;
	}
	
	/**
	 * perform allocation calculation
	 * @param paymentTable
	 * @param invoiceTable
	 * @param isMultiCurrency
	 */
	public void calculate(IMiniTable paymentTable, IMiniTable invoiceTable, boolean isMultiCurrency)
	{
		allocDate = null;
		prepareForCalculate(isMultiCurrency);
		calculatePayment(paymentTable, isMultiCurrency);
		calculateInvoice(invoiceTable, isMultiCurrency);
		calculateDifference();
	}
	
	/**
	 * Calculate selected payment total
	 * @param payment
	 * @param isMultiCurrency
	 * @return payment summary
	 */
	public String calculatePayment(IMiniTable payment, boolean isMultiCurrency)
	{
		if (log.isLoggable(Level.CONFIG)) log.config("");

		//  Payment
		totalPay = Env.ZERO;
		int rows = payment.getRowCount();
		m_noPayments = 0;
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				Timestamp ts = (Timestamp)payment.getValueAt(i, 1);
				if ( !isMultiCurrency )  // the converted amounts are only valid for the selected date
					allocDate = TimeUtil.max(allocDate, ts);
				BigDecimal bd = (BigDecimal)payment.getValueAt(i, i_payment);
				totalPay = totalPay.add(bd);  //  Applied Pay
				m_noPayments++;
				if (log.isLoggable(Level.FINE)) log.fine("Payment_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		return getPaymentInfoText();
	}

	/**
	 * 
	 * @return summary info for payment selected and total applied
	 */
	public String getPaymentInfoText() {
		return String.valueOf(m_noPayments) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalPay) + " ";
	}
	
	/**
	 * calculate selected invoice total
	 * @param invoice
	 * @param isMultiCurrency
	 * @return invoice summary
	 */
	public String calculateInvoice(IMiniTable invoice, boolean isMultiCurrency)
	{		
		//  Invoices
		totalInv = Env.ZERO;
		int rows = invoice.getRowCount();
		m_noInvoices = 0;

		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				Timestamp ts = (Timestamp)invoice.getValueAt(i, 1);
				if ( !isMultiCurrency )  // converted amounts only valid for selected date
					allocDate = TimeUtil.max(allocDate, ts);
				BigDecimal bd = (BigDecimal)invoice.getValueAt(i, i_applied);
				totalInv = totalInv.add(bd);  //  Applied Inv
				m_noInvoices++;
				if (log.isLoggable(Level.FINE)) log.fine("Invoice_" + i + " = " + bd + " - Total=" + totalPay);
			}
		}
		return getInvoiceInfoText();
	}

	/**
	 * invoiceSetResetSelection
	 * @param invoice
	 * @param isAutoWriteOff
	 * @param isMultiCurrency
	 * @param setSelected
	 * @return
	 */
	public String invoiceSetResetSelection( IMiniTable invoice, boolean isAutoWriteOff, boolean isMultiCurrency, boolean setSelected)
	{		
		//  Invoices
		int rows = invoice.getRowCount();
		String msg ="";
		for (int i = 0; i < rows; i++)
		{
			boolean selected = ((Boolean) invoice.getValueAt(i, 0)).booleanValue();
			BigDecimal bd = (BigDecimal)invoice.getValueAt(i, i_applied);
			
			if (setSelected) {
				if (!selected){
					invoice.setValueAt(true, i, 0);
					msg = writeOff(i, 0, true, null, invoice, isAutoWriteOff);
				}
			} else {
				if (selected){
					invoice.setValueAt(false, i, 0);
					msg = writeOff(i, 0, true, null, invoice, isAutoWriteOff);
				}
			}
			if (log.isLoggable(Level.FINE)) log.fine("Invoice_" + i + " = " + bd + " - Total=" + totalPay);
		}
		calculateInvoice(invoice, isMultiCurrency);
		return String.valueOf(m_noInvoices) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalInv) + " ";
	}
	
	/**
	 * 
	 * @return summary info for invoice selected and total applied
	 */
	public String getInvoiceInfoText() {
		return String.valueOf(m_noInvoices) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalInv) + " ";
	}
	
	/**
	 * Save allocation data
	 * @param m_WindowNo
	 * @param dateTrx
	 * @param payment
	 * @param invoice
	 * @param trxName
	 * @return {@link MAllocationHdr}
	 */
	public MAllocationHdr saveData(int m_WindowNo, Timestamp dateTrx, IMiniTable payment, IMiniTable invoice, String trxName)
	{
		if (m_noInvoices + m_noPayments == 0)
			return null;

		//  fixed fields
		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Org_ID");
		int C_BPartner_ID = m_C_BPartner_ID;
		int C_Order_ID = 0;
		int C_CashLine_ID = 0;
		int C_Currency_ID = m_C_Currency_ID;	//	the allocation currency
		//
		if (AD_Org_ID == 0)
		{
			//ADialog.error(m_WindowNo, this, "Org0NotAllowed", null);
			throw new AdempiereException("@Org0NotAllowed@");
		}
		//
		if (log.isLoggable(Level.CONFIG)) log.config("Client=" + AD_Client_ID + ", Org=" + AD_Org_ID
			+ ", BPartner=" + C_BPartner_ID + ", Date=" + dateTrx);

		//  Payment - Loop and add them to paymentList/amountList
		int pRows = payment.getRowCount();
		ArrayList<Integer> paymentList = new ArrayList<Integer>(pRows);
		ArrayList<BigDecimal> amountList = new ArrayList<BigDecimal>(pRows);
		BigDecimal paymentAppliedAmt = Env.ZERO;
		for (int i = 0; i < pRows; i++)
		{
			//  Payment line is selected
			if (((Boolean)payment.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)payment.getValueAt(i, 2);   //  Value
				//  Payment variables
				int C_Payment_ID = pp.getKey();
				paymentList.add(Integer.valueOf(C_Payment_ID));
				//
				BigDecimal PaymentAmt = (BigDecimal)payment.getValueAt(i, i_payment);  //  Applied Payment
				amountList.add(PaymentAmt);
				//
				paymentAppliedAmt = paymentAppliedAmt.add(PaymentAmt);
				//
				if (log.isLoggable(Level.FINE)) log.fine("C_Payment_ID=" + C_Payment_ID 
					+ " - PaymentAmt=" + PaymentAmt); // + " * " + Multiplier + " = " + PaymentAmtAbs);
			}
		}
		if (log.isLoggable(Level.CONFIG)) log.config("Number of Payments=" + paymentList.size() + " - Total=" + paymentAppliedAmt);

		//  Invoices - Loop and generate allocations
		int iRows = invoice.getRowCount();
		
		//	Create Allocation
		MAllocationHdr alloc = new MAllocationHdr (Env.getCtx(), true,	//	manual
			dateTrx, C_Currency_ID, Env.getContext(Env.getCtx(), Env.AD_USER_NAME), trxName);
		alloc.setAD_Org_ID(AD_Org_ID);
		alloc.setC_DocType_ID(m_C_DocType_ID);
		alloc.setDescription(alloc.getDescriptionForManualAllocation(m_C_BPartner_ID, trxName));
		alloc.saveEx();
		//	For all invoices
		BigDecimal unmatchedApplied = Env.ZERO;
		for (int i = 0; i < iRows; i++)
		{
			//  Invoice line is selected
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)invoice.getValueAt(i, 2);    //  Value
				//  Invoice variables
				int C_Invoice_ID = pp.getKey();
				BigDecimal AppliedAmt = (BigDecimal)invoice.getValueAt(i, i_applied);
				//  semi-fixed fields (reset after first invoice)
				BigDecimal DiscountAmt = (BigDecimal)invoice.getValueAt(i, i_discount);
				BigDecimal WriteOffAmt = (BigDecimal)invoice.getValueAt(i, i_writeOff);
				//	OverUnderAmt needs to be in Allocation Currency
				BigDecimal OverUnderAmt = ((BigDecimal)invoice.getValueAt(i, i_open))
					.subtract(AppliedAmt).subtract(DiscountAmt).subtract(WriteOffAmt);
				
				if (log.isLoggable(Level.CONFIG)) log.config("Invoice #" + i + " - AppliedAmt=" + AppliedAmt);// + " -> " + AppliedAbs);
				//  loop through all payments until invoice applied
				
				for (int j = 0; j < paymentList.size() && AppliedAmt.signum() != 0; j++)
				{
					int C_Payment_ID = ((Integer)paymentList.get(j)).intValue();
					BigDecimal PaymentAmt = (BigDecimal)amountList.get(j);
					if (PaymentAmt.signum() == AppliedAmt.signum())	// only match same sign (otherwise appliedAmt increases)
					{												// and not zero (appliedAmt was checked earlier)
						if (log.isLoggable(Level.CONFIG)) log.config(".. with payment #" + j + ", Amt=" + PaymentAmt);
						
						BigDecimal amount = AppliedAmt;
						if (amount.abs().compareTo(PaymentAmt.abs()) > 0)  // if there's more open on the invoice
							amount = PaymentAmt;							// than left in the payment
						
						//	Allocation Line
						MAllocationLine aLine = new MAllocationLine (alloc, amount, 
							DiscountAmt, WriteOffAmt, OverUnderAmt);
						aLine.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
						aLine.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
						aLine.saveEx();

						//  Apply Discounts and WriteOff only first time
						DiscountAmt = Env.ZERO;
						WriteOffAmt = Env.ZERO;
						//  subtract amount from Payment/Invoice
						AppliedAmt = AppliedAmt.subtract(amount);
						PaymentAmt = PaymentAmt.subtract(amount);
						if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + amount + " - Remaining  Applied=" + AppliedAmt + ", Payment=" + PaymentAmt);
						amountList.set(j, PaymentAmt);  //  update
					}	//	for all applied amounts
				}	//	loop through payments for invoice
				
				if ( AppliedAmt.signum() == 0 && DiscountAmt.signum() == 0 && WriteOffAmt.signum() == 0)
					continue;
				else {			// remainder will need to match against other invoices
					int C_Payment_ID = 0;
					
					//	Allocation Line
					MAllocationLine aLine = new MAllocationLine (alloc, AppliedAmt, 
						DiscountAmt, WriteOffAmt, OverUnderAmt);
					aLine.setDocInfo(C_BPartner_ID, C_Order_ID, C_Invoice_ID);
					aLine.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
					aLine.saveEx();
					if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + AppliedAmt);
					unmatchedApplied = unmatchedApplied.add(AppliedAmt);
				}
			}   //  invoice selected
		}   //  invoice loop

		// check for unapplied payment amounts (eg from payment reversals)
		for (int i = 0; i < paymentList.size(); i++)	{
			BigDecimal payAmt = (BigDecimal) amountList.get(i);
			if ( payAmt.signum() == 0 )
					continue;
			int C_Payment_ID = ((Integer)paymentList.get(i)).intValue();
			if (log.isLoggable(Level.FINE)) log.fine("Payment=" + C_Payment_ID  
					+ ", Amount=" + payAmt);

			//	Allocation Line
			MAllocationLine aLine = new MAllocationLine (alloc, payAmt, 
				Env.ZERO, Env.ZERO, Env.ZERO);
			aLine.setDocInfo(C_BPartner_ID, 0, 0);
			aLine.setPaymentInfo(C_Payment_ID, 0);
			aLine.saveEx();
			unmatchedApplied = unmatchedApplied.subtract(payAmt);
		}		
		
		// check for charge amount
		if ( m_C_Charge_ID > 0 && unmatchedApplied.compareTo(Env.ZERO) != 0 )
		{
			BigDecimal chargeAmt = totalDiff;
	
		//	Allocation Line
			MAllocationLine aLine = new MAllocationLine (alloc, chargeAmt.negate(), 
				Env.ZERO, Env.ZERO, Env.ZERO);
			aLine.setC_Charge_ID(m_C_Charge_ID);
			aLine.setC_BPartner_ID(m_C_BPartner_ID);
			if (!aLine.save(trxName)) {
				StringBuilder msg = new StringBuilder("Allocation Line not saved - Charge=").append(m_C_Charge_ID);
				throw new AdempiereException(msg.toString());
			}
			unmatchedApplied = unmatchedApplied.add(chargeAmt);
		}	
		
		if ( unmatchedApplied.signum() != 0 )
			throw new AdempiereException("Allocation not balanced -- out by " + unmatchedApplied);

		//	Should start WF
		if (alloc.get_ID() != 0)
		{
			if (!alloc.processIt(DocAction.ACTION_Complete))
				throw new AdempiereException("Cannot complete allocation: " + alloc.getProcessMsg());
			alloc.saveEx();
		}
		
		//  Test/Set IsPaid for Invoice - requires that allocation is posted
		for (int i = 0; i < iRows; i++)
		{
			//  Invoice line is selected
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue())
			{
				KeyNamePair pp = (KeyNamePair)invoice.getValueAt(i, 2);    //  Value
				//  Invoice variables
				int C_Invoice_ID = pp.getKey();
				String sql = "SELECT invoiceOpen(C_Invoice_ID, 0) "
					+ "FROM C_Invoice WHERE C_Invoice_ID=?";
				BigDecimal open = DB.getSQLValueBD(trxName, sql, C_Invoice_ID);
				if (open != null && open.signum() == 0)	 {
					sql = "UPDATE C_Invoice SET IsPaid='Y' "
						+ "WHERE C_Invoice_ID=" + C_Invoice_ID;
					int no = DB.executeUpdate(sql, trxName);
					if (log.isLoggable(Level.CONFIG)) log.config("Invoice #" + i + " is paid - updated=" + no);
				} else {
					if (log.isLoggable(Level.CONFIG)) log.config("Invoice #" + i + " is not paid - " + open);
				}
			}
		}
		//  Test/Set Payment is fully allocated
		for (int i = 0; i < paymentList.size(); i++)
		{
			int C_Payment_ID = ((Integer)paymentList.get(i)).intValue();
			MPayment pay = new MPayment (Env.getCtx(), C_Payment_ID, trxName);
			if (pay.testAllocation())
				pay.saveEx();
			if (log.isLoggable(Level.CONFIG)) log.config("Payment #" + i + (pay.isAllocated() ? " not" : " is") 
					+ " fully allocated");
		}
		paymentList.clear();
		amountList.clear();
		
		return alloc;
	}   //  saveData

	/**
	 * 
	 * @return C_BPartner_ID
	 */
	public int getC_BPartner_ID() {
		return m_C_BPartner_ID;
	}

	/**
	 * 
	 * @param C_BPartner_ID
	 */
	public void setC_BPartner_ID(int C_BPartner_ID) {
		this.m_C_BPartner_ID = C_BPartner_ID;
	}

	/**
	 * 
	 * @return C_Currency_ID
	 */
	public int getC_Currency_ID() {
		return m_C_Currency_ID;
	}

	/**
	 * 
	 * @param C_Currency_ID
	 */
	public void setC_Currency_ID(int C_Currency_ID) {
		this.m_C_Currency_ID = C_Currency_ID;
	}

	/**
	 * 
	 * @return C_DocType_ID
	 */
	public int getC_DocType_ID() {
		return m_C_DocType_ID;
	}

	/**
	 * 
	 * @param C_DocType_ID
	 */
	public void setC_DocType_ID(int C_DocType_ID) {
		this.m_C_DocType_ID = C_DocType_ID;
	}

	/**
	 * 
	 * @return C_Charge_ID
	 */
	public int getC_Charge_ID() {
		return m_C_Charge_ID;
	}

	/**
	 * 
	 * @param C_Charge_ID
	 */
	public void setC_Charge_ID(int C_Charge_ID) {
		this.m_C_Charge_ID = C_Charge_ID;
	}

	/**
	 * 
	 * @return AD_Org_ID
	 */
	public int getAD_Org_ID() {
		return m_AD_Org_ID;
	}

	/**
	 * 
	 * @param AD_Org_ID
	 */
	public void setAD_Org_ID(int AD_Org_ID) {
		this.m_AD_Org_ID = AD_Org_ID;
	}

	/**
	 * 
	 * @return number of selected invoice
	 */
	public int getSelectedInvoiceCount() {
		return m_noInvoices;
	}

	/**
	 * 
	 * @return number of selected payment
	 */
	public int getSelectedPaymentCount() {
		return m_noPayments;
	}

	/**
	 * 
	 * @return total of invoice applied amount
	 */
	public BigDecimal getInvoiceAppliedTotal() {
		return totalInv;
	}

	/**
	 * 
	 * @return total of payment applied amount
	 */
	public BigDecimal getPaymentAppliedTotal() {
		return totalPay;
	}

	/**
	 * 
	 * @return true if all condition is meet to proceed with allocation
	 */
	public boolean isOkToAllocate() {
		return totalDiff.signum() == 0 || getC_Charge_ID() > 0;
	}

	/**
	 * 
	 * @return difference between invoice and payment applied amount
	 */
	public BigDecimal getTotalDifference() {
		return totalDiff;
	}

	/**
	 * calculate difference between invoice and payment applied amount
	 */
	public void calculateDifference() {
		totalDiff = totalPay.subtract(totalInv);
	}
	
	
	public int getDefaultAMNProcessID() {
	    // Obtener solo el ID usando Query para que sea más eficiente
	    Integer processID = new Query(Env.getCtx(), MAMN_Process.Table_Name, 
	            "IsActive='Y' AND AMN_Process_Value='NN' AND AD_Client_ID=?", null)
	            .setParameters(Env.getAD_Client_ID(Env.getCtx()))
	            .setOrderBy("AMN_Process_ID")
	            .setOnlyActiveRecords(true)
	            .firstId();

	    if (processID != null && processID > 0) {
	        // Cargar el registro completo si es necesario en otro lugar
	        MAMN_Process process = new MAMN_Process(Env.getCtx(), processID, null);
	        return process.getAMN_Process_ID(); // O simplemente `return processID;`
	    }

	    return 0;
	}

    protected List<KeyNamePair> getValidProcesses() {
	    List<KeyNamePair> processList = new ArrayList<>();
	
	    String sql = "SELECT DISTINCT p.AMN_Process_ID, p.Name " +
	                 "FROM AMN_Process p " +
	                 "INNER JOIN AMN_Period per ON per.AMN_Process_ID = p.AMN_Process_ID " +
	                 "WHERE p.AD_Client_ID = ?";
	
	    try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
	        pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
	
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                int processID = rs.getInt("AMN_Process_ID");
	                String name = rs.getString("Name");
	                processList.add(new KeyNamePair(processID, name));
	            }
	        }
	    } catch (SQLException e) {
	        log.log(Level.SEVERE, "Error fetching valid processes", e);
	    }
	
	    return processList;
	}

	// Método para obtener el valor predeterminado de AMN_Contract_ID
    public int getFirstActiveContractID(int roleID) {
        List<KeyNamePair> validContracts = getValidContracts(roleID);
        return !validContracts.isEmpty() ? validContracts.get(0).getKey() : 0;
    }

    protected List<KeyNamePair> getValidContracts(int AD_Role_ID) {
        List<KeyNamePair> list = new ArrayList<>();
        String sql = """
            SELECT c.AMN_Contract_ID, c.Name
            FROM AMN_Contract c
            WHERE c.IsActive='Y'
              AND c.AD_Client_ID=?
              AND c.AMN_Contract_ID IN (
                SELECT DISTINCT ra.AMN_Contract_ID
                FROM AMN_Role_Access ra
                WHERE ra.AD_Role_ID=?
                  AND ra.AMN_Process_ID IN (
                    SELECT p.AMN_Process_ID
                    FROM AMN_Process p
                    WHERE p.AMN_Process_Value='NN'
                  )
              )
            """;

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            pstmt.setInt(2, AD_Role_ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error cargando contratos", e);
        }

        return list;
    }

    /**
     * getAD_Column_ID devuelve la columna
     * @param tableName
     * @param columnName
     * @return
     */
    protected int getAD_Column_ID(String tableName, String columnName) {
        String sql = "SELECT AD_Column_ID FROM AD_Column WHERE AD_Table_ID = " +
                     "(SELECT AD_Table_ID FROM AD_Table WHERE TableName=?) " +
                     "AND ColumnName=?";
        try (PreparedStatement ps = DB.prepareStatement(sql, null)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("AD_Column_ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * getValidDocTypeIDs
     * @return
     */
    protected List<Integer> getValidDocTypeIDs() {
        List<Integer> docTypeIDs = new ArrayList<>();
        String sql = """
            SELECT DISTINCT C_DocTypeTarget_ID, C_DocTypeCreditMemo_ID
            FROM AMN_Process
            WHERE AD_Client_ID = ?
            AND (C_DocTypeTarget_ID IS NOT NULL OR C_DocTypeCreditMemo_ID IS NOT NULL)
        """;

        try (PreparedStatement ps = DB.prepareStatement(sql, null)) {
            ps.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int targetID = rs.getInt("C_DocTypeTarget_ID");
                    if (targetID != 0) {
                        docTypeIDs.add(targetID);
                    }
                    int creditMemoID = rs.getInt("C_DocTypeCreditMemo_ID");
                    if (creditMemoID != 0) {
                        docTypeIDs.add(creditMemoID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return docTypeIDs;
    }

    protected Integer getFirstActiveDocTypeID() {
        List<Integer> validIDs = getValidDocTypeIDs();
        if (validIDs.isEmpty()) {
            return null;
        }

        // Convierte la lista en una cadena de IDs separados por coma, para usar en la cláusula IN
        String inClause = validIDs.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String sql = "SELECT C_DocType_ID FROM C_DocType " +
                     "WHERE IsActive='Y' AND AD_Client_ID=? AND C_DocType_ID IN (" + inClause + ") " +
                     "ORDER BY Name";

        return DB.getSQLValue(null, sql, Env.getAD_Client_ID(Env.getCtx()));
    }

    protected String getDocTypeName(Integer docTypeID) {
        String docTypeName = null;
        String sql = "SELECT Name FROM C_DocType WHERE C_DocType_ID = ?";

        try (PreparedStatement ps = DB.prepareStatement(sql, null)) {
            ps.setInt(1, docTypeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    docTypeName = rs.getString("Name");  // Obtener el nombre del tipo de documento
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return docTypeName;
    }
    
}

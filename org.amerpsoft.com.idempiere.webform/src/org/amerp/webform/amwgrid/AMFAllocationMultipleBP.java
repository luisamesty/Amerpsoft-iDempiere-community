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

import javax.swing.JTable;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.window.FDialog;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.webform.amwmodel.OrgInfo;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MAllocationHdr;
import org.compiere.model.MAllocationLine;
import org.compiere.model.MCharge;
import org.compiere.model.MClientInfo;
import org.compiere.model.MInvoice;
import org.compiere.model.MPayment;
import org.compiere.model.MQuery;
import org.compiere.model.MRole;
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

public class AMFAllocationMultipleBP
{
	public DecimalFormat format = DisplayType.getNumberFormat(DisplayType.Amount);

	/**	Logger			*/
	public static CLogger log = CLogger.getCLogger(AMFAllocationMultipleBP.class);

	private boolean     m_calculating = false;
	public int         	m_C_Currency_ID = 0;
	public int          m_C_Charge_ID = 0;
	public int         	m_C_BPartner_ID = 0;
	public int         	m_C_BPartner2_ID = 0;
	public int         	m_AMN_Employee_ID = 0;
	public int          m_C_Activity_ID = 0;
	public int         	m_C_Project_ID = 0;
	public int         	m_AMN_Process_ID = 0;
	public int         	m_AMN_Contract_ID = 0;
	public int         	m_C_DocType_ID = 0;
	public Timestamp 	m_Date = null;
	public Timestamp 	m_DateDoc = null;
	public boolean		m_SelectStatus = false;
	private int         m_noInvoices = 0;
	private int         m_noPayments = 0;
	public int 			m_WindowNo = 0;
	public BigDecimal	totalInv = Env.ZERO;
	public BigDecimal 	totalPay = Env.ZERO;
	public BigDecimal	totalDiff = Env.ZERO;
	public MAllocationHdr[] allocationHeader = new MAllocationHdr[2];
	public Timestamp allocDate = null;
	public boolean mandatoryActivity = false;
	public boolean mandatoryProject = false;
	
	//  Index	changed if multi-currency
	private int         i_payment = 7;
	//
	private int         i_open = 6;
	private int         i_discount = 7;
	private int         i_writeOff = 8; 
	private int         i_applied = 9;
	private int 		i_overUnder = 10;
//	private int			i_multiplier = 10;
	public	String		allocDescription = "";
	
	public int         	m_AD_Org_ID = 0;  // Org for Payments
	public int         	m_docAD_Org_ID = 0;  // Org for Documents
	/**	SO Zoom Window						*/
	private int					m_SO_Window_ID = -1;
	/**	PO Zoom Window						*/
	private int					m_PO_Window_ID = -1;
	/**	PO Zoom Window C_Payment						*/
	private int					m_Payment_Window_ID = -1;
	private ArrayList<Integer>	m_bpartnerCheck = new ArrayList<Integer>(); 
	public String IsSOTrx = "Y";
	public String IsMultipleAllocation = "N";
	
	public void dynInit() throws Exception
	{
		m_C_Currency_ID = Env.getContextAsInt(Env.getCtx(), "$C_Currency_ID");   //  default
		//
		if (log.isLoggable(Level.INFO)) log.info("Currency=" + m_C_Currency_ID);
		// Client and AcctSchema
		MClientInfo info = MClientInfo.get(Env.getCtx(), Env.getAD_Client_ID(Env.getCtx()), null);
		MAcctSchema as = MAcctSchema.get (Env.getCtx(), info.getC_AcctSchema1_ID(), null);
		MAcctSchemaElement[] elements = as.getAcctSchemaElements();
		//  Check all Schema Elements
		mandatoryActivity = false;
		mandatoryProject = false;
		for (int i = 0; i < elements.length; i++)
		{
			MAcctSchemaElement ase = elements[i];
			if (ase.getElementType().compareToIgnoreCase("AY") == 0 &&  ase.isMandatory() == true)
				mandatoryActivity = true;
			if (ase.getElementType().compareToIgnoreCase("PJ") == 0 &&  ase.isMandatory() == true)
				mandatoryProject = true;
			//log.warning("ase.getElementType() = "+ase.getElementType() +"  ase.isMandatory() ="+ ase.isMandatory());
		}
		// m_AD_Org_ID Init value
		m_AD_Org_ID = 0; //Env.getAD_Org_ID(Env.getCtx());
	}
	
	/**
	 *  Load Business Partner Info
	 *  - Payments
	 */
	public void checkBPartner()
	{		
		if (log.isLoggable(Level.CONFIG)) log.config("BPartner=" + m_C_BPartner_ID + ", Cur=" + m_C_Currency_ID);
		//  Need to have both values
		if (m_C_BPartner_ID == 0 || m_C_Currency_ID == 0)
			return;

		//	Async BPartner Test
		Integer key = new Integer(m_C_BPartner_ID);
		if (!m_bpartnerCheck.contains(key))
		{
			new Thread()
			{
				public void run()
				{
					MPayment.setIsAllocated (Env.getCtx(), m_C_BPartner_ID, null);
					MInvoice.setIsPaid (Env.getCtx(), m_C_BPartner2_ID, null);
				}
			}.start();
			m_bpartnerCheck.add(key);
		}
	}
	
	/**
	 *  Load Business Partner Info
	 *  - Invoices
	 */
	public void checkBPartner2()
	{		
		if (log.isLoggable(Level.CONFIG)) log.config("BPartner=" + m_C_BPartner2_ID + ", Cur=" + m_C_Currency_ID);
		//  Need to have both values
		if (m_C_BPartner2_ID == 0 || m_C_Currency_ID == 0)
			return;

		//	Async BPartner Test
		Integer key = new Integer(m_C_BPartner2_ID);
		if (!m_bpartnerCheck.contains(key))
		{
			new Thread()
			{
				public void run()
				{
					MPayment.setIsAllocated (Env.getCtx(), m_C_BPartner_ID, null);
					MInvoice.setIsPaid (Env.getCtx(), m_C_BPartner2_ID, null);
				}
			}.start();
			m_bpartnerCheck.add(key);
		}
	}
	
	public Vector<Vector<Object>> getPaymentData(boolean isMultiCurrency, Object date, IMiniTable paymentTable)
	{		
		/********************************
		 *  Load unallocated Payments
		 ********************************/
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		StringBuilder sql = new StringBuilder("SELECT p.DateTrx,p.DocumentNo,p.C_Payment_ID,"  //  1..3
				+ "c.ISO_Code,p.PayAmt,"                            //  4..5
				+ "currencyConvertPayment(p.C_Payment_ID,?,null,?) as payamount,"//  6   #1, #2
				+ "currencyConvertPayment(p.C_Payment_ID,?,paymentAvailable(p.C_Payment_ID),?) as payavailable,"  //  7   #3, #4
				+ "p.MultiplierAP, "
		        + "p.Description, "
		        + "org.Name AS OrgName "                        // ← Organización
				+ "FROM C_Payment_v p"		//	Corrected for AP/AR
				+ " INNER JOIN C_Currency c ON (p.C_Currency_ID=c.C_Currency_ID) "
				+ " INNER JOIN AD_Org org ON (p.AD_Org_ID = org.AD_Org_ID) "   
				+ " WHERE p.IsAllocated='N' AND p.Processed='Y'"
				+ " AND p.C_Charge_ID IS NULL"		//	Prepayments OK
				+ " AND p.C_BPartner_ID=?");                   		//      #5
			if (!isMultiCurrency)
				sql.append(" AND p.C_Currency_ID=?");				//      #6
			if (m_AD_Org_ID != 0 )
				sql.append(" AND p.AD_Org_ID=" + m_AD_Org_ID);
			sql.append(" ORDER BY p.DateTrx,p.DocumentNo");
		// role security
		sql = new StringBuilder( MRole.getDefault(Env.getCtx(), false).addAccessSQL( sql.toString(), "p", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO ) );
		
		if (log.isLoggable(Level.FINE)) log.fine("PaySQL=" + sql.toString());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)date);
			pstmt.setInt(3, m_C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)date);
			pstmt.setInt(5, m_C_BPartner_ID);
			if (!isMultiCurrency)
				pstmt.setInt(6, m_C_Currency_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
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
				line.add(Env.ZERO);					//  5/7-Payment
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
	
	public Vector<Vector<Object>> getInvoiceData(boolean isMultiCurrency, Object date, IMiniTable invoiceTable)
	{
		/********************************
		 *  Load unpaid Invoices
		 *********************************/
		String adLanguage = Env.getAD_Language(Env.getCtx()); // Obtén el idioma del entorno
		// Verificar si la base de datos es PostgreSQL
		boolean isPostgreSQL = DB.isPostgreSQL();
		// Construir la parte de la consulta que varía según el tipo de base de datos
		String docTypeCase = isPostgreSQL
		        ? " COALESCE(dt_trl.PrintName, dt.PrintName, dt.Name) AS DocTypeName "  // Para PostgreSQL
		        : " NVL(dt_trl.PrintName, dt.PrintName, dt.Name) AS DocTypeName ";      // Para Oracle
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		// Construccion sql
		StringBuilder sql = new StringBuilder("SELECT i.DateAcct,i.DocumentNo,i.C_Invoice_ID," //  1..3
				+ "c.ISO_Code,i.GrandTotal*i.MultiplierAP, "                            //  4..5    Orig Currency
				+ "currencyConvertInvoice(i.C_Invoice_ID,?,i.GrandTotal*i.MultiplierAP,?), " //  6   #1  Converted, #2 Date
				+ "currencyConvert(invoiceOpen(i.C_Invoice_ID,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,?,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP, "  //  7   #3, #4  Converted Open
				+ "currencyConvert(invoiceDiscount"                               //  8       AllowedDiscount
				+ "(i.C_Invoice_ID,?,C_InvoicePaySchedule_ID),i.C_Currency_ID,?,i.DateInvoiced,i.C_ConversionType_ID,i.AD_Client_ID,i.AD_Org_ID)*i.Multiplier*i.MultiplierAP,"               //  #5, #6
				+ "i.MultiplierAP, "
				+ "e.Value || ' - ' || e.Name AS EmployeeFullName,"
				+ "bp.Value || ' - ' || bp.Name AS Tercero, "
				+  docTypeCase
				+ "FROM C_Invoice_V i"		//  corrected for CM/Split
				+ " INNER JOIN C_Currency c ON (i.C_Currency_ID=c.C_Currency_ID) "
				+ " INNER JOIN AMN_Payroll_Docs pd ON (pd.c_invoice_id = i.c_invoice_id ) "
				+ " INNER JOIN AMN_Payroll p ON (p.amn_payroll_id  = pd.amn_payroll_id) "
				+ " INNER JOIN AMN_Employee e ON (e.amn_employee_id = p.amn_employee_id) "
				+ " INNER JOIN C_BPartner bp ON (bp.C_BPartner_ID = i.C_BPartner_ID) "
				+ " INNER JOIN C_DocType dt ON (dt.C_DocType_ID = i.C_DocType_ID) "
				+ " LEFT JOIN C_DocType_Trl dt_trl ON (dt_trl.C_DocType_ID = dt.C_DocType_ID AND dt_trl.AD_Language = '" + adLanguage + "') "
				+ " WHERE i.IsPaid='N' AND i.Processed='Y'"
				+ " AND i.C_BPartner_ID=?");	                                        //  #7
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
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			pstmt.setInt(1, m_C_Currency_ID);
			pstmt.setTimestamp(2, (Timestamp)date);
			pstmt.setInt(3, m_C_Currency_ID);
			pstmt.setTimestamp(4, (Timestamp)date);
			pstmt.setTimestamp(5, (Timestamp)date);
			pstmt.setInt(6, m_C_Currency_ID);
			pstmt.setInt(7, m_C_BPartner2_ID);
			pstmt.setInt(8, m_AMN_Employee_ID);
			if (!isMultiCurrency)
				pstmt.setInt(8, m_C_Currency_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				Vector<Object> line = new Vector<Object>();
				line.add(new Boolean(false));       //  0-Selection
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
				line.add(Env.ZERO);					//  7/9-Applied
				line.add(Env.ZERO);				    //  8/10-OverUnder
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
		// Ajustar anchos de columna
		if (invoiceTable instanceof JTable) {
			JTable jTable = (JTable) invoiceTable;
			jTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // Value
			// etc.
		}

		// Repetir para el resto según index calculado y si isMultiCurrency aplica
	}
	
	public void calculate(boolean isMultiCurrency)
	{
		i_open = isMultiCurrency ? 6 : 4;
		i_discount = isMultiCurrency ? 7 : 5;
		i_writeOff = isMultiCurrency ? 8 : 6;
		i_applied = isMultiCurrency ? 9 : 7;
		i_overUnder = isMultiCurrency ? 10 : 8;
//		i_multiplier = isMultiCurrency ? 10 : 8;
	}   //  loadBPartner
	
	/**
	 * writeOff 
	 * @param row
	 * @param col
	 * @param isInvoice
	 * @param payment
	 * @param invoice
	 * @param isAutoWriteOff
	 * @return
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
				if ( applied.signum() == -open.signum() )
					applied = applied.negate();
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
	 *	Zoom Document 
	 * @param allocationTable
	 */
	public void zoom (IMiniTable paymentTable, IMiniTable invoiceTable)
	{
		// invoiceTable  (Invoices) 
		// paymentTable  (Payments)
		log.info( "C_Invoice_ID.zoom");
		KeyNamePair pp = null ;
		int selectedRowI =-1;
		int selectedRowP =-1;
		// TAKE selectedRowP
		// OLD:selectedRowP = paymentTable.getSelectedRow();
		int rows = paymentTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)paymentTable.getValueAt(i, 0)).booleanValue())
			{
				selectedRowP = i;
				break;
			}
		}
		//OLD:selectedRowI = invoiceTable.getSelectedRow();
		rows = invoiceTable.getRowCount();
		for (int i = 0; i < rows; i++)
		{
			if (((Boolean)invoiceTable.getValueAt(i, 0)).booleanValue())
			{
				selectedRowI = i;
				break;
			}
		}
		// log.warning("selectedRowI="+selectedRowI+"  selectedRowP="+selectedRowP);
		// IF No row Selected The Return
		if (selectedRowI == -1 && selectedRowP == -1 ) {
			FDialog.info(m_WindowNo, null, 
					Msg.translate(Env.getCtx(), "Select")+" "+
					Msg.translate(Env.getCtx(), "C_Payment_ID")+ " \r\n "+ 
					"      "+Msg.translate(Env.getCtx(), "OR")+" \r\n"+
					Msg.translate(Env.getCtx(), "Select")+" "+
					Msg.translate(Env.getCtx(), "C_Invoice_ID")+ " \r\n ");
			return;
		}
		// Invoice row
		Integer C_Doc_IDI = 0;
		if (selectedRowI >= 0) {
			// Invoices Column No 2 (Document No) 
			pp = (KeyNamePair) invoiceTable.getValueAt(selectedRowI, 2);
			C_Doc_IDI = pp.getKey();
		}
		// Payment Row
		Integer C_Doc_IDP = 0;
		if (selectedRowP >= 0) {
			// Payments
			pp = (KeyNamePair) paymentTable.getValueAt(selectedRowP, 2);
			C_Doc_IDP = pp.getKey();
		}
		//log.warning("   C_Doc_IDI:"+C_Doc_IDI+"   C_Doc_IDP:"+C_Doc_IDP);
		// 
		// Verify if not NULL C_Payment_ID and C_Invoice_ID
		if (C_Doc_IDI == null || C_Doc_IDP == null ) {
			FDialog.info(m_WindowNo, null, " Select Valid Paymet or Invoice");
			FDialog.info(m_WindowNo, null, 
					Msg.translate(Env.getCtx(), "Error")+" "+
					Msg.translate(Env.getCtx(), "C_Payment_ID")+ " "+C_Doc_IDI+ " \r\n "+ 
					Msg.translate(Env.getCtx(), "Error")+" "+
					Msg.translate(Env.getCtx(), "C_Invoice_ID")+ " "+C_Doc_IDI+" \r\n ");
			return;
		}
		// 
		Integer C_Invoice_ID = null ;
		Integer C_Payment_ID = null ;
		//log.warning("Zomm docBaseType:"+docBaseType+ " C_DocType_ID:"+C_DoctType_ID+" C_Doc_ID:"+C_Doc_ID);
		// INVOICES
		// Invoices All Sales and vendors
		if ( C_Doc_IDI != null && C_Doc_IDI > 0  ) {
			C_Invoice_ID = C_Doc_IDI;
			MInvoice minvoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
			MQuery query = new MQuery("C_Invoice");
			query.addRestriction("C_Invoice_ID", MQuery.EQUAL, C_Invoice_ID);
			query.setRecordCount(1);
			// Get AD_Window_ID or PO_Window_ID depending on minvoice.isSOTrx()
			int AD_WindowNo = getC_Invoice_AD_Window_ID("C_Invoice", minvoice.isSOTrx());
			//log.warning("Zomm ARI ARC docBaseType:"+docBaseType+ " C_Invoice_ID:"+C_Invoice_ID+" AD_WindowNo="+AD_WindowNo);
			AEnv.zoom (AD_WindowNo, query);
		}
		// PAYMENTS
		// Payments All
		if (C_Doc_IDP != null && C_Doc_IDP > 0  ) {
			C_Payment_ID = C_Doc_IDP;
			MQuery query = new MQuery("C_Payment");
			query.addRestriction("C_Payment_ID", MQuery.EQUAL, C_Payment_ID);
			query.setRecordCount(1);
			int AD_WindowNo = getC_Payment_AD_Window_ID("C_Payment");
			//log.warning("Zomm ARR docBaseType="+docBaseType+ " C_Payment_ID="+C_Payment_ID +" AD_WindowNo="+AD_WindowNo);
			AEnv.zoom (AD_WindowNo, query);
		}
	}	//	zoom
	
	/**
	 *  getC_Invoice_AD_Window_ID
	 * 	Get Zoom Window for C_Invoice
	 *	@param tableName table name
	 *	@param isSOTrx sales trx
	 *	@return AD_Window_ID
	 */
	protected int getC_Invoice_AD_Window_ID (String tableName, boolean isSOTrx)
	{
		if (!isSOTrx && m_PO_Window_ID > 0)
			return m_PO_Window_ID;
		if (m_SO_Window_ID > 0)
			return m_SO_Window_ID;
		//
		String sql = "SELECT AD_Window_ID, PO_Window_ID FROM AD_Table WHERE TableName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_SO_Window_ID = rs.getInt(1);
				m_PO_Window_ID = rs.getInt(2);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		if (!isSOTrx && m_PO_Window_ID > 0)
			return m_PO_Window_ID;
		return m_SO_Window_ID;
	}	//	getAD_Window_ID

	
	/**
	 *  getC_Payment_AD_Window_ID
	 * 	Get Zoom Window for C_Payment
	 *	@param tableName table name
	 *	@return AD_Window_ID
	 */
	protected int getC_Payment_AD_Window_ID (String tableName)
	{

		String sql = "SELECT AD_Window_ID FROM AD_Table WHERE TableName=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setString(1, tableName);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				m_Payment_Window_ID = rs.getInt(1);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		//
		return m_Payment_Window_ID;
	}	//	getAD_Window_ID
	

	
	/**
	 *  Calculate Allocation info
	 */
	public String calculatePayment(IMiniTable payment, boolean isMultiCurrency)
	{
		log.config("");

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
		return String.valueOf(m_noPayments) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalPay) + " ";
	}
	
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
		return String.valueOf(m_noInvoices) + " - "
			+ Msg.getMsg(Env.getCtx(), "Sum") + "  " + format.format(totalInv) + " ";
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
	
	public boolean saveDataAllocation(int m_WindowNo, Object date, String description,
			IMiniTable payment, IMiniTable invoice, int C_Charge_ID, String trxName)
	{
		boolean retValue=false;
		if (IsMultipleAllocation.compareToIgnoreCase("Y")==0)
			retValue = saveDataTwoAllocation( m_WindowNo, date, description,	payment, invoice, C_Charge_ID, trxName);
		else
			retValue = saveDataOneAllocation( m_WindowNo, date, description,	payment, invoice, C_Charge_ID, trxName);
		// Return true
		return retValue;
	}
	
	/**************************************************************************
	 *  Save Data with ONE Allocation
	 **************************************************************************/
	public boolean saveDataOneAllocation(int m_WindowNo, Object date, String description,
			IMiniTable payment, IMiniTable invoice, int C_Charge_ID, String trxName)
	{
		//
		MAllocationHdr[] retValue = new MAllocationHdr[2];
		MCharge charge = new MCharge(Env.getCtx(),m_C_Charge_ID, null);
		//
		if (m_noInvoices + m_noPayments == 0)
			return false;
		//  fixed fields
		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Org_ID");
		int C_BPartner_ID = m_C_BPartner_ID;
		int C_Order_ID = 0;
		int C_CashLine_ID = 0;
		int C_Activity_ID = m_C_Activity_ID;
		int C_Project_ID = m_C_Project_ID;
		String sqlAll = "";
		Timestamp DateTrx = (Timestamp)date;
		int C_Currency_ID = m_C_Currency_ID;	//	the allocation currency
		// allocDescription
		allocDescription = description.trim();
		if (allocDescription.equalsIgnoreCase("")) {
			allocDescription= Msg.getElement(Env.getCtx(), "C_Charge_ID")+":"+charge.getName().trim() + " " + Env.getContext(Env.getCtx(), "#AD_User_Name").trim();
		} else {
			allocDescription= description.trim() +Msg.getElement(Env.getCtx(), "C_Charge_ID")+":"+charge.getName().trim() + " " +" ("+Env.getContext(Env.getCtx(), "#AD_User_Name").trim()+")";
		}
		//
		if (AD_Org_ID == 0)
		{
			//ADialog.error(m_WindowNo, this, "Org0NotAllowed", null);
			throw new AdempiereException("@Org0NotAllowed@");
		}
		//
		if (log.isLoggable(Level.CONFIG)) log.config("Client=" + AD_Client_ID + ", Org=" + AD_Org_ID
			+ ", BPartner=" + C_BPartner_ID + ", Date=" + DateTrx);

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
				paymentList.add(new Integer(C_Payment_ID));
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

		//	------------------------------------------------
		//  Create Allocation HEADER 
		//	------------------------------------------------
		MAllocationHdr alloch = new MAllocationHdr (Env.getCtx(), true,	//	manual
			DateTrx, C_Currency_ID, allocDescription + "  "+Msg.getElement(Env.getCtx(), "C_Payment_ID")+"s", trxName);
		// Fill Allocation Header 1 
		retValue[0] = alloch;
		alloch.setAD_Org_ID(AD_Org_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			alloch.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			alloch.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		alloch.saveEx();
		retValue[0] = alloch;
		retValue[1] = alloch;
		//	------------------------------------------------
		//	Create Allocation Lines for payments
		//	------------------------------------------------
		for (int j = 0; j < paymentList.size() ; j++)
		{
			int C_Payment_ID = ((Integer)paymentList.get(j)).intValue();
			BigDecimal PaymentAmt = (BigDecimal)amountList.get(j);
			// C_Payment
			MPayment pay = new MPayment( Env.getCtx(),C_Payment_ID, null);
			C_Activity_ID = pay.getC_Activity_ID();
			C_Project_ID = pay.getC_Project_ID();
			if (C_Activity_ID == 0 && m_C_Activity_ID > 0)
				C_Activity_ID= m_C_Activity_ID;
			if (C_Project_ID == 0 && m_C_Project_ID > 0)
				C_Project_ID= m_C_Project_ID;
			//log.warning("J="+j+" Pay Lines ..C_BPartner_ID="+m_C_BPartner_ID+"  C_Payment_ID="+C_Payment_ID+"  AppliedAmt="+PaymentAmt+"  C_Charge_ID="+C_Charge_ID);
			if ( PaymentAmt.signum() != 0)
			{
				// log.warning("Pay Lines ..C_BPartner_ID="+m_C_BPartner_ID+"  C_Payment_ID="+C_Payment_ID+"  AppliedAmt="+PaymentAmt+"  C_Charge_ID="+C_Charge_ID);
				//	Allocation Line
				MAllocationLine aLinep = new MAllocationLine (alloch, PaymentAmt, 
						 Env.ZERO,  Env.ZERO,  Env.ZERO);
				aLinep.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
				aLinep.setC_Charge_ID(C_Charge_ID);
				aLinep.setC_BPartner_ID(m_C_BPartner_ID);
				// AMERP New Fields
				if (C_Project_ID > 0)
					aLinep.set_ValueOfColumn("C_Project_ID", C_Project_ID);
				if (C_Activity_ID > 0)
					aLinep.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
				aLinep.saveEx();
				if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + PaymentAmt + ", Payment=" + PaymentAmt);
				amountList.set(j, PaymentAmt);  //  update
			}
		}	//	loop through payments for invoice
		//log.warning("Charge Line ...C_BPartner_ID="+m_C_BPartner_ID+"  paymentAppliedAmt="+paymentAppliedAmt+"  C_Charge_ID="+C_Charge_ID);
		//	Allocation Line for Charge 1
		MAllocationLine aLinep = new MAllocationLine (alloch, paymentAppliedAmt.negate(), 
				Env.ZERO,  Env.ZERO,  Env.ZERO);
		// Default Form Values
		C_Activity_ID = m_C_Activity_ID;
		C_Project_ID = m_C_Project_ID;
		// Allocation Line for Charge 1
		aLinep.setC_Charge_ID(C_Charge_ID);
		aLinep.setC_BPartner_ID(m_C_BPartner_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			aLinep.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			aLinep.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		aLinep.saveEx();
		
		if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + paymentAppliedAmt);

		//	------------------------------------------------
		//	Create Allocation Lines for invoices
		//	------------------------------------------------
		int iRows = invoice.getRowCount();
		BigDecimal invoiceAppliedAmt = Env.ZERO;

		//	Allocation Lines  for all invoices
		BigDecimal unmatchedApplied = Env.ZERO;
		for (int i = 0; i < iRows; i++)
		{
			//  Invoice line is selected
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
			MInvoice inv = new MInvoice(Env.getCtx(),C_Invoice_ID, null);
			C_Activity_ID = inv.getC_Activity_ID();
			C_Project_ID = inv.getC_Project_ID();
			if (C_Activity_ID == 0 && m_C_Activity_ID > 0)
				C_Activity_ID= m_C_Activity_ID;
			if (C_Project_ID == 0 && m_C_Project_ID > 0)
				C_Project_ID= m_C_Project_ID;
			//log.warning("I="+i+" Inv Lines ..C_BPartner_ID="+m_C_BPartner2_ID+"  AppliedAmt="+AppliedAmt+" C_Invoice_ID="+C_Invoice_ID+"  C_Charge_ID="+C_Charge_ID);
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue() && AppliedAmt.signum() != 0)
			{
				if (log.isLoggable(Level.CONFIG)) log.config("Invoice #" + i + " - AppliedAmt=" + AppliedAmt);// + " -> " + AppliedAbs);
				//	Allocation Line
				MAllocationLine aLinei = new MAllocationLine (alloch, AppliedAmt, 
					DiscountAmt, WriteOffAmt, OverUnderAmt);
				aLinei.setDocInfo(m_C_BPartner2_ID, C_Order_ID, C_Invoice_ID);
				aLinei.setC_Charge_ID(C_Charge_ID);
				aLinei.setC_BPartner_ID(m_C_BPartner2_ID);
				// AMERP New Fields
				if (C_Project_ID > 0)
					aLinei.set_ValueOfColumn("C_Project_ID", C_Project_ID);
				if (C_Activity_ID > 0)
					aLinei.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
				aLinei.saveEx();
				//  Apply Discounts and WriteOff only first time
				DiscountAmt = Env.ZERO;
				WriteOffAmt = Env.ZERO;
				//  
				invoiceAppliedAmt = invoiceAppliedAmt.add(AppliedAmt);
				if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + AppliedAmt + " - Remaining  Applied=" + AppliedAmt );
			}	
		}
		//	Allocation Line for Charge 2
		//log.warning("Charge Line ...C_BPartner_ID="+m_C_BPartner2_ID+"  invoiceAppliedAmt="+invoiceAppliedAmt+"  C_Charge_ID="+C_Charge_ID);
		// Default Form Values
		C_Activity_ID = m_C_Activity_ID;
		C_Project_ID = m_C_Project_ID;
		// Allocation Line for Charge 2
		MAllocationLine aLinei = new MAllocationLine (alloch, invoiceAppliedAmt, 
				Env.ZERO,  Env.ZERO,  Env.ZERO);
		aLinei.setC_Charge_ID(C_Charge_ID);
		aLinei.setC_BPartner_ID(m_C_BPartner2_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			aLinei.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			aLinei.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		aLinei.saveEx();
		if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + invoiceAppliedAmt);
	
		if ( unmatchedApplied.signum() != 0 )
			log.log(Level.SEVERE, "Allocation not balanced -- out by " + unmatchedApplied );
		
		//	-----------------------------------------------------------
		//	Complete and UPDATE isPaid Filed for payment and invoices
		//	----------------------------------------------------------
		//	Should start WF for Payments an Invoices
		if (alloch.get_ID() != 0)
		{
			if (!alloch.processIt(DocAction.ACTION_Complete))
				throw new AdempiereException("Cannot complete allocation: " + alloch.getProcessMsg());
			alloch.saveEx();
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
		// Set Public MAllocationHdr
		setAllocationHeader(retValue);
		// Return true
		return true;

	}
	
	/**************************************************************************
	 *  Save Data with Two Allocations using Charges
	 **************************************************************************/
	public boolean saveDataTwoAllocation(int m_WindowNo, Object date, String description,
			IMiniTable payment, IMiniTable invoice, int C_Charge_ID, String trxName)
	{
		//
		MAllocationHdr[] retValue = new MAllocationHdr[2];
		MCharge charge = new MCharge(Env.getCtx(),m_C_Charge_ID, null);
		//
		if (m_noInvoices + m_noPayments == 0)
			return false;
		//  fixed fields
		int AD_Client_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Client_ID");
		int AD_Org_ID = Env.getContextAsInt(Env.getCtx(), m_WindowNo, "AD_Org_ID");
		int C_BPartner_ID = m_C_BPartner_ID;
		int C_Order_ID = 0;
		int C_CashLine_ID = 0;
		int C_Activity_ID = m_C_Activity_ID;
		int C_Project_ID = m_C_Project_ID;
		String sqlAll = "";
		Timestamp DateTrx = (Timestamp)date;
		int C_Currency_ID = m_C_Currency_ID;	//	the allocation currency
		// allocDescription
		allocDescription = description.trim();
		if (allocDescription.equalsIgnoreCase("")) {
			allocDescription= Msg.getElement(Env.getCtx(), "C_Charge_ID")+":"+charge.getName().trim() + " " + Env.getContext(Env.getCtx(), "#AD_User_Name").trim();
		} else {
			allocDescription= description.trim() +Msg.getElement(Env.getCtx(), "C_Charge_ID")+":"+charge.getName().trim() + " " +" ("+Env.getContext(Env.getCtx(), "#AD_User_Name").trim()+")";
		}
		//
		if (AD_Org_ID == 0)
		{
			//ADialog.error(m_WindowNo, this, "Org0NotAllowed", null);
			throw new AdempiereException("@Org0NotAllowed@");
		}
		//
		if (log.isLoggable(Level.CONFIG)) log.config("Client=" + AD_Client_ID + ", Org=" + AD_Org_ID
			+ ", BPartner=" + C_BPartner_ID + ", Date=" + DateTrx);

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
				paymentList.add(new Integer(C_Payment_ID));
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

		//	------------------------------------------------
		//  Create Allocation HEADER 1 for Paymenyts
		//	------------------------------------------------
		MAllocationHdr allocp = new MAllocationHdr (Env.getCtx(), true,	//	manual
			DateTrx, C_Currency_ID, allocDescription + "  "+Msg.getElement(Env.getCtx(), "C_Payment_ID")+"s", trxName);
		// Fill Allocation Header 1 
		retValue[0] = allocp;
		allocp.setAD_Org_ID(AD_Org_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			allocp.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			allocp.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		allocp.saveEx();
		retValue[0] = allocp;
		//	Allocation Lines 1 for payments
		for (int j = 0; j < paymentList.size() ; j++)
		{
			int C_Payment_ID = ((Integer)paymentList.get(j)).intValue();
			BigDecimal PaymentAmt = (BigDecimal)amountList.get(j);
			// C_Payment
			MPayment pay = new MPayment( Env.getCtx(),C_Payment_ID, null);
			C_Activity_ID = pay.getC_Activity_ID();
			C_Project_ID = pay.getC_Project_ID();
			if (C_Activity_ID == 0 && m_C_Activity_ID > 0)
				C_Activity_ID= m_C_Activity_ID;
			if (C_Project_ID == 0 && m_C_Project_ID > 0)
				C_Project_ID= m_C_Project_ID;
			//log.warning("J="+j+" Pay Lines ..C_BPartner_ID="+m_C_BPartner_ID+"  C_Payment_ID="+C_Payment_ID+"  AppliedAmt="+PaymentAmt+"  C_Charge_ID="+C_Charge_ID);
			if ( PaymentAmt.signum() != 0)
			{
				// log.warning("Pay Lines ..C_BPartner_ID="+m_C_BPartner_ID+"  C_Payment_ID="+C_Payment_ID+"  AppliedAmt="+PaymentAmt+"  C_Charge_ID="+C_Charge_ID);
				//	Allocation Line
				MAllocationLine aLinep = new MAllocationLine (allocp, PaymentAmt, 
						 Env.ZERO,  Env.ZERO,  Env.ZERO);
				aLinep.setPaymentInfo(C_Payment_ID, C_CashLine_ID);
				aLinep.setC_Charge_ID(C_Charge_ID);
				aLinep.setC_BPartner_ID(m_C_BPartner_ID);
				// AMERP New Fields
				if (C_Project_ID > 0)
					aLinep.set_ValueOfColumn("C_Project_ID", C_Project_ID);
				if (C_Activity_ID > 0)
					aLinep.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
				aLinep.saveEx();
				if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + PaymentAmt + ", Payment=" + PaymentAmt);
				amountList.set(j, PaymentAmt);  //  update
			}
		}	//	loop through payments for invoice
		//log.warning("Charge Line ...C_BPartner_ID="+m_C_BPartner_ID+"  paymentAppliedAmt="+paymentAppliedAmt+"  C_Charge_ID="+C_Charge_ID);
		//	Allocation Line for Charge 1
		MAllocationLine aLinep = new MAllocationLine (allocp, paymentAppliedAmt.negate(), 
				Env.ZERO,  Env.ZERO,  Env.ZERO);
		// Default Form Values
		C_Activity_ID = m_C_Activity_ID;
		C_Project_ID = m_C_Project_ID;
		// Allocation Line for Charge 1
		aLinep.setC_Charge_ID(C_Charge_ID);
		aLinep.setC_BPartner_ID(m_C_BPartner_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			aLinep.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			aLinep.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		aLinep.saveEx();
		
		if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + paymentAppliedAmt);

		//	------------------------------------------------
		//	Create Allocation HEADER 2 for invoices
		//	------------------------------------------------
		int iRows = invoice.getRowCount();
		BigDecimal invoiceAppliedAmt = Env.ZERO;
		MAllocationHdr alloci = new MAllocationHdr (Env.getCtx(), true,	//	manual
			DateTrx, C_Currency_ID, allocDescription+ "  "+Msg.getElement(Env.getCtx(), "C_Invoice_ID")+"s", trxName);
		alloci.setAD_Org_ID(AD_Org_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			alloci.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			alloci.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		alloci.saveEx();
		retValue[1] = alloci;
		// Fill Allocation Header 2
		retValue[1] = alloci;
		//	Allocation Lines 2 for all invoices
		BigDecimal unmatchedApplied = Env.ZERO;
		for (int i = 0; i < iRows; i++)
		{
			//  Invoice line is selected
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
			MInvoice inv = new MInvoice(Env.getCtx(),C_Invoice_ID, null);
			C_Activity_ID = inv.getC_Activity_ID();
			C_Project_ID = inv.getC_Project_ID();
			if (C_Activity_ID == 0 && m_C_Activity_ID > 0)
				C_Activity_ID= m_C_Activity_ID;
			if (C_Project_ID == 0 && m_C_Project_ID > 0)
				C_Project_ID= m_C_Project_ID;
			//log.warning("I="+i+" Inv Lines ..C_BPartner_ID="+m_C_BPartner2_ID+"  AppliedAmt="+AppliedAmt+" C_Invoice_ID="+C_Invoice_ID+"  C_Charge_ID="+C_Charge_ID);
			if (((Boolean)invoice.getValueAt(i, 0)).booleanValue() && AppliedAmt.signum() != 0)
			{
				if (log.isLoggable(Level.CONFIG)) log.config("Invoice #" + i + " - AppliedAmt=" + AppliedAmt);// + " -> " + AppliedAbs);
				//	Allocation Line
				MAllocationLine aLinei = new MAllocationLine (alloci, AppliedAmt, 
					DiscountAmt, WriteOffAmt, OverUnderAmt);
				aLinei.setDocInfo(m_C_BPartner2_ID, C_Order_ID, C_Invoice_ID);
				aLinei.setC_Charge_ID(C_Charge_ID);
				aLinei.setC_BPartner_ID(m_C_BPartner2_ID);
				// AMERP New Fields
				if (C_Project_ID > 0)
					aLinei.set_ValueOfColumn("C_Project_ID", C_Project_ID);
				if (C_Activity_ID > 0)
					aLinei.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
				aLinei.saveEx();
				//  Apply Discounts and WriteOff only first time
				DiscountAmt = Env.ZERO;
				WriteOffAmt = Env.ZERO;
				//  
				invoiceAppliedAmt = invoiceAppliedAmt.add(AppliedAmt);
				if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + AppliedAmt + " - Remaining  Applied=" + AppliedAmt );
			}	
		}
		//	Allocation Line for Charge 2
		//log.warning("Charge Line ...C_BPartner_ID="+m_C_BPartner2_ID+"  invoiceAppliedAmt="+invoiceAppliedAmt+"  C_Charge_ID="+C_Charge_ID);
		// Default Form Values
		C_Activity_ID = m_C_Activity_ID;
		C_Project_ID = m_C_Project_ID;
		// Allocation Line for Charge 2
		MAllocationLine aLinei = new MAllocationLine (alloci, invoiceAppliedAmt, 
				Env.ZERO,  Env.ZERO,  Env.ZERO);
		aLinei.setC_Charge_ID(C_Charge_ID);
		aLinei.setC_BPartner_ID(m_C_BPartner2_ID);
		// AMERP New Fields
		if (C_Project_ID > 0)
			aLinei.set_ValueOfColumn("C_Project_ID", C_Project_ID);
		if (C_Activity_ID > 0)
			aLinei.set_ValueOfColumn("C_Activity_ID", C_Activity_ID);
		aLinei.saveEx();
		
		if (log.isLoggable(Level.FINE)) log.fine("Allocation Amount=" + invoiceAppliedAmt);

		
		if ( unmatchedApplied.signum() != 0 )
			log.log(Level.SEVERE, "Allocation not balanced -- out by " + unmatchedApplied );
		
		//	-----------------------------------------------------------
		//	Complete and UPDATE isPaid Filed for payment and invoices
		//	----------------------------------------------------------
		//	Should start WF for Payments
		if (allocp.get_ID() != 0)
		{
			if (!allocp.processIt(DocAction.ACTION_Complete))
				throw new AdempiereException("Cannot complete allocation: " + allocp.getProcessMsg());
			allocp.saveEx();
		}
		//	Should start WF for Invoices
		if (alloci.get_ID() != 0)
		{
			if (!alloci.processIt(DocAction.ACTION_Complete))
				throw new AdempiereException("Cannot complete allocation: " + alloci.getProcessMsg());
			alloci.saveEx();
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
		// Set Public MAllocationHdr
		setAllocationHeader(retValue);
		// Return true
		return true;

	}

	public MAllocationHdr[] getAllocationHeader() {
		return allocationHeader;
	}

	public void setAllocationHeader(MAllocationHdr[] allocationHeader) {
		this.allocationHeader = allocationHeader;
	}
	
	public ArrayList<OrgInfo> getOrgData(int p_AD_Org_ID)
	{
		ArrayList<OrgInfo> data = new ArrayList<OrgInfo>();
		//
		int AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		int AD_Org_ID=0;
		String sql = "";
		//  Org Info
		if (p_AD_Org_ID == 0) {
			sql = "SELECT o.AD_Org_ID, " 	//  1
			+ "o.Name AS Name "          	//  2
			+ "FROM AD_Org as o "
			+ "WHERE o.IsActive='Y' AND o.AD_Client_ID = "
			+ AD_Client_ID ;			
		} else {
			sql = "SELECT o.AD_Org_ID, "    //  1
			+ " o.Name AS Name "         	//  2
			+ " FROM AD_Org as o "
			+ " WHERE o.IsActive='Y' "
			+ " AND o.AD_Client_ID = "+ AD_Client_ID 
			+ " AND o.AD_Org_ID=" +p_AD_Org_ID;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				OrgInfo oi = new OrgInfo (rs.getInt(1),rs.getString(2)+"("+rs.getInt(1)+")");
				data.add(oi);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		// Get Firts Org and set as default
		AD_Org_ID=data.get(0).getAD_Org_ID();
		this.setM_AD_Org_ID(AD_Org_ID);
		return data;
	}
	
	/*
	 *  Default m_AD_Org_ID
	 */
	public int getM_AD_Org_ID() {
		return m_AD_Org_ID;
	}
	public void setM_AD_Org_ID(int m_AD_Org_ID) {
		this.m_AD_Org_ID = m_AD_Org_ID;
	}
	
	// Método para obtener el valor predeterminado de AMN_Process_ID
	public int getDefaultAMNProcessID() {
	    // Obtener solo el ID utilizando Query para mejorar la eficiencia
	    Integer processID = new Query(Env.getCtx(), MAMN_Process.Table_Name, 
	            "IsActive='Y' AND AMN_Process_Value='NN' AND AD_Client_ID=?", null)
	            .setParameters(Env.getAD_Client_ID(Env.getCtx()))
	            .setOrderBy("AMN_Process_ID")
	            .firstId();  // Devuelve solo el ID en lugar de todo el objeto

	    // Si el ID es válido, cargar el objeto completo, si es necesario
	    if (processID != null && processID > 0) {
	        MAMN_Process process = new MAMN_Process(Env.getCtx(), processID, null);
	        return process.getAMN_Process_ID();
	    }

	    return 0;  // Si no se encuentra, devuelve 0
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



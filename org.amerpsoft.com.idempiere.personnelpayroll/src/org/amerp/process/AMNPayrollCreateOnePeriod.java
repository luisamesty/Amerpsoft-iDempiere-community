/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
  *****************************************************************************/
package org.amerp.process;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.ScriptException;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpMsg;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 * AMNPayrollCreateOnePeriod
 * Description: Procedure called from iDempiere AD
 * 			Create Payroll Receipts
 * 			For One Process, One Contract and One Period
 * 			For All Employees on Contract and Period.
 * Result:	Create all PayrollReceipt according to Payroll Concepts Rules
 * 			Create Header on AMN_Payroll and Lines on AMN_Payroll_detail
 * 			Using AMNPayrollCreateDocs Class
 * @author luisamesty
 *
 */
public class AMNPayrollCreateOnePeriod extends SvrProcess{
	
	static CLogger log = CLogger.getCLogger(AMNPayrollCreateOnePeriod.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Payroll_Lot_ID = 0;
	String Employee_Name,AMN_Process_Value="";
	String Employee_Value="";
	String sql="";
	String sql1="";
	static Timestamp p_DateAcct = null;
	static Timestamp p_InvDateEnd = null; 
	static Timestamp p_InvDateIni = null;
	static Timestamp p_RefDateEnd = null;
	static Timestamp p_RefDateIni = null;
	// Receipt List
	AMNReceipts Receipt = null;
	List<AMNReceipts> ReceiptsGenList = new ArrayList<AMNReceipts>();
	// Receipt Lines List
	AMNReceiptLines ReceiptLines = null;
	List<AMNReceiptLines> ReceiptConcepts = new ArrayList<AMNReceiptLines>();

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
    @Override
    protected void prepare() {
	    // TODO Auto-generated method stub
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
				p_AMN_Period_ID = para.getParameterAsInt();
			else if (paraName.equals("DateAcct"))
				p_DateAcct = para.getParameterAsTimestamp();
			else if (paraName.equals("InvDateIni"))
				p_InvDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("InvDateEnd"))
				p_InvDateEnd = para.getParameterAsTimestamp();
			else if (paraName.equals("RefDateIni"))
				p_RefDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("RefDateEnd"))
				p_RefDateEnd = para.getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {

	    String Msg_Header="";
	    String Msg_Value="";
	    boolean okProcess = false;
	    MAMN_Process amnprocess = new MAMN_Process(getCtx(), p_AMN_Process_ID, null); 
	    AMN_Process_Value = amnprocess.getAMN_Process_Value();
		MAMN_Contract amncontract = new MAMN_Contract(getCtx(), p_AMN_Contract_ID, null);
		// Default Account Schema
		MClientInfo info = MClientInfo.get(getCtx(), amnprocess.getAD_Client_ID(), null); 
		MAcctSchema as = MAcctSchema.get (getCtx(), info.getC_AcctSchema1_ID(), null);
		as.getC_AcctSchema_ID();
   		// Default Currency  for Contract
   		Integer Currency_ID = AmerpUtilities.defaultAMNContractCurrency(p_AMN_Contract_ID);
   		// Default ConversionType for Contract
   		Integer ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(amncontract.getAMN_Contract_ID());
   		// ************************
    	// Process VALIDATION	
    	// ************************
	    if (AMN_Process_Value.equalsIgnoreCase("NN") ||
	    		AMN_Process_Value.equalsIgnoreCase("TI") ||
	    		AMN_Process_Value.equalsIgnoreCase("NO") ) {
	    	// ************************
	    	// Process NN PO and TI	
	    	// ************************
	    	// log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    	okProcess = AMNPayrollCreateInvoicesNN(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, Currency_ID, ConversionType_ID, get_TrxName());
	    } else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
	    	// ************************
	    	// Process NV	
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicesNV(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, Currency_ID, ConversionType_ID, get_TrxName());
	    } else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
	    	// ************************
	    	// Process NP
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicesNP(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, Currency_ID, ConversionType_ID, get_TrxName());
	    } else if (AMN_Process_Value.equalsIgnoreCase("PI")) {
	    	// ************************
	    	// Process PI
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicesPI(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, Currency_ID, ConversionType_ID, get_TrxName());
	    } else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
	    	// ************************
	    	// Process NU
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicesNU(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, Currency_ID, ConversionType_ID, get_TrxName());
	    } else {
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n");
	    }
	    if ( okProcess ) {
	    	// END MESSAGE
	    	Msg_Header = Msg.getElement(getCtx(), "AMN_Process_ID")+":"+amnprocess.getName().trim();
		    addLog(Msg_Header);
		    Msg_Header = Msg.getElement(getCtx(), "AMN_Payroll_ID")+
		    		Msg.getElement(getCtx(), "Qty")+
		    		" ("+ReceiptsGenList.size()+") :";
		    addLog(Msg_Header);		
			for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
			    Msg_Header = Msg.getElement(getCtx(), "AMN_Payroll_ID")+":"+
			    		Msg.getElement(getCtx(), "AMN_Employee_ID")+":"+ReceiptsGenList.get(i).getEmployee_Value()+"-"+ReceiptsGenList.get(i).getEmployee_Name();
			    addLog(Msg_Header);
			    Msg_Header = Msg.getElement(getCtx(), " ")+":"+
			    		ReceiptsGenList.get(i).getDocumentNo()+"-"+ReceiptsGenList.get(i).getError();
			    addLog(Msg_Header);
			    log.warning("Updating Final Message"+Msg_Header);
			}
	    	return "OK";
	    } else {	    	
	    	return null;
	    }
    }  

     /**
     * AMNPayrollCreateInvoicesNN
     * Standard Payroll Invoices
     * 
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Period_ID
     * @param C_Currency_ID
     * @param C_ConversionType_ID
     * @return
     */
    public boolean AMNPayrollCreateInvoicesNN(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	// AllProcess
    	AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    	return true;
    }
    
    /**
     * AMNPayrollCreateInvoicesNP
     * Social Benefits Payroll Invoices
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Period_ID
     * @param C_Currency_ID
     * @param C_ConversionType_ID
     * @return
     */
    public boolean AMNPayrollCreateInvoicesNP(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	// AllProcess 
    	AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    	return true;
    }
    
    /**
     * AMNPayrollCreateInvoicesPI
     * Social Benefits Interest Paymenet Payroll Invoices
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Period_ID
     * @param C_Currency_ID
     * @param C_ConversionType_ID
     * @return
     */
    public boolean AMNPayrollCreateInvoicesPI(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	// AllProcess 
    	AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    	return true;
    }
    
     /**
     * AMNPayrollCreateInvoicesNV
     * VACATIONS Payroll Invoices
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Period_ID
     * @param C_Currency_ID
     * @param C_ConversionType_ID
     * @return
     */
    public boolean AMNPayrollCreateInvoicesNV(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	// AllProcess
    	AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    	return true;
    }
 
    /**
     * AMNPayrollCreateInvoicesNU
     * Social Benefits Payroll Invoices
     * 
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Period_ID
     * @param C_Currency_ID
     * @param C_ConversionType_ID
     * @return
     */
    public boolean AMNPayrollCreateInvoicesNU(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	// AllProcess
    	AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    	return true;
    }
    
     /**
     * AMNPayrollCreateInvoicesAllProcess
     * All Standard Payroll Invoices
     * 
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Period_ID
     * @param C_Currency_ID
     * @param C_ConversionType_ID
     * @return
     */
    public boolean AMNPayrollCreateInvoicesAllProcess(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, 
    		String trxName) {
	    
    	String Msg_Header="";
    	String Msg_Lines="";
	    String Msg_Updates="";
	    String MessagetoShow ="";
		String trxNameLine = "";
	    int AMN_Employee_ID=0;
		int AMN_Payroll_ID = 0;
		Timestamp NextPPDateIni = null;
		Timestamp NextPPDateEnd = null;
	    MAMN_Payroll amnpayrollCK = null;
	    MAMN_Payroll amnpayroll = null;
	    MAMN_Period  			amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
	    MAMN_Payroll_Historic 	amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
	    IProcessUI processMonitor = Env.getProcessUI(getCtx());
	    int Percent = 0;
	    int NoRecs = 0;
	    // ARRAY EMPLOYEE - CONTRACT
		sql = "SELECT " + 
				"amnemp.amn_employee_id as amn_employee_id,   " + 
				"amnemp.name	as employee_name,  " + 
				"amnemp.value	as employee_value,  " + 
				"amncon.amn_contract_id,  " + 
				"amncon.name as contract_name  " + 
				"FROM  amn_employee as amnemp " + 
				"LEFT JOIN amn_contract amncon on (amnemp.amn_contract_id = amncon.amn_contract_id )  " ;
	    // Select Employees upon Status (Active, Vacation, Suspended, Retired)
		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
	    		AMN_Process_Value.equalsIgnoreCase("TI") ||
	    		AMN_Process_Value.equalsIgnoreCase("NO") ) {
			// Only Active Employees 
			sql1 = "WHERE amnemp.isactive='Y' and amnemp.status = 'A' and amncon.amn_contract_id=? "  +        		
				"ORDER BY amnemp.value ";   
	    } if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// Only Vacation Employees 
			sql1 = "WHERE amnemp.isactive='Y' and amnemp.status = 'V' and amncon.amn_contract_id=? "  +        		
				"ORDER BY amnemp.value ";
	    } if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// Only Non-Retired Employees and Non-Suspended (Active and Vacation Employees )
			sql1 = "WHERE amnemp.isactive='Y' and  (amnemp.status = 'A' or amnemp.status = 'V' or amnemp.status = 'S') and amncon.amn_contract_id=? "  +        		
				"ORDER BY amnemp.value ";
	    } if (AMN_Process_Value.equalsIgnoreCase("PI")) {
			// Only Non-Retired Employees and Non-Suspended (Active and Vacation Employees )
			sql1 = "WHERE amnemp.isactive='Y' and  (amnemp.status = 'A' or amnemp.status = 'V' or amnemp.status = 'S') and amncon.amn_contract_id=? "  +        		
				"ORDER BY amnemp.value ";
	    } if (AMN_Process_Value.equalsIgnoreCase("NU")) {
			// Only Non-Retired Employees and Non-Suspended (Active and Vacation Employees )
			sql1 = "WHERE amnemp.isactive='Y' and (amnemp.status = 'A' or amnemp.status = 'V') and amncon.amn_contract_id=? "  +        		
				"ORDER BY amnemp.value ";

	    }
	    // Complete SQL Sentence
		sql = sql + sql1;
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		// Create Docs Headers and Lines
		// log.warning("sql="+sql);
		// **********************
		// Document Headers
		// **********************
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Contract_ID);
			rspc = pstmt.executeQuery();
			while (rspc.next())
			{
				AMN_Employee_ID = rspc.getInt(1);
				Employee_Name = rspc.getString(2).trim();
				Employee_Value= rspc.getString(3).trim();
				String trxNameHdr = trxName+"_"+AMN_Employee_ID;
		    	// Document Header
			    // Verify if AMN_Payroll_ID is created and POSTED
				Receipt = new AMNReceipts();
				Receipt.setAMN_Employee_ID(AMN_Employee_ID);
				Receipt.setEmployee_Value(Employee_Value);
				Receipt.setEmployee_Name(Employee_Name);
				Receipt.setNoLines(0);
				amnpayrollCK = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
						p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, AMN_Employee_ID);
				if (amnpayrollCK != null ) {
					Receipt.setAMN_Payroll_ID(amnpayrollCK.getAMN_Payroll_ID());
					Receipt.setDocumentNo(amnpayrollCK.getDocumentNo().trim());
					Receipt.setDescription(amnpayrollCK.getValue().trim()+" "+amnpayrollCK.getName().trim());
					if (amnpayrollCK.isPosted()) {
						Msg_Header = Msg.translate(ctx, "Error")+Msg.translate(ctx, "document.status.completed")+" ("+amnpayrollCK.getDocStatus()+") ";
					    Receipt.setRecIsPosted(true);
					    Receipt.setError(Msg_Header);
					} else {
						AMN_Payroll_ID =  AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, AMN_Employee_ID, amnpayrollCK.getAMN_Payroll_ID(), p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameHdr);
						Msg_Header = "("+AmerpMsg.getElementPrintText(ctx, "AMN_Payroll_ID")+" **** "+Msg.translate(ctx, "Updated")+ " **** )";
						Receipt.setRecIsPosted(false);
						Receipt.setError(Msg_Header);
					}
				}	else {
					AMN_Payroll_ID=  AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, AMN_Employee_ID, 0, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, get_TrxName());
					amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, get_TrxName());
					amnpayroll = MAMN_Payroll.findByAMNPayroll(Env.getCtx(), Env.getLanguage(Env.getCtx()).getLocale(), 
							p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, AMN_Employee_ID);
					AMN_Payroll_ID = amnpayroll.getAMN_Payroll_ID();
					Receipt.setAMN_Payroll_ID(AMN_Payroll_ID);
					Receipt.setRecIsPosted(false);
					Receipt.setAMN_Payroll_ID(amnpayroll.getAMN_Payroll_ID());
					Receipt.setDocumentNo(amnpayroll.getDocumentNo().trim());
					Receipt.setDescription(amnpayroll.getValue().trim()+" "+amnpayroll.getName().trim());
					Msg_Header = "("+AmerpMsg.getElementPrintText(ctx, "AMN_Payroll_ID")+" **** "+Msg.translate(ctx, "New")+ " **** )";
				    Receipt.setError(Msg_Header);
				}
				ReceiptsGenList.add(Receipt);
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
		DB.close(rspc, pstmt);
		rspc = null; pstmt = null;
		// NoRecs Number of receipts created or updated
		NoRecs = ReceiptsGenList.size();
		//log.warning("...Headers Completed....("+NoRecs+")");
		// **********************
		// Document Lines	
		// **********************
		// Create Array for Receipt Concepts
		// ReceiptConcepts  array of ReceiptLines
		AMNPayrollCreateReceiptLinesArray(ctx, p_AMN_Process_ID, p_AMN_Contract_ID,  trxNameLine);
		//for (int j=0 ; j < ReceiptConcepts.size() ; j++) {
		//	log.warning("Concepts j=("+j+") "+ReceiptConcepts.get(j).getConceptValue()+" "+ReceiptConcepts.get(j).getConceptName()+" "+ReceiptConcepts.get(j).getCalcOrder());}		
		for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
			Percent = 100 * (i / NoRecs);
			AMN_Employee_ID=ReceiptsGenList.get(i).getAMN_Employee_ID();
			//log.warning("Document Lines i=("+i+") Percent="+Percent+" "+ReceiptsGenList.get(i).getAMN_Payroll_ID()+" "+ReceiptsGenList.get(i).getDescription()+" "+ReceiptsGenList.get(i).getError());
			trxNameLine = trxName+"_"+AMN_Employee_ID;
			// Percentage Monitor
			MessagetoShow = String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
					" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
					ReceiptsGenList.get(i).getDocumentNo().trim()+" "+
					AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID")+":"+
					String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim()).replace(' ', '_') +"_"+
					" ";
			if (processMonitor != null) {
				processMonitor.statusUpdate(MessagetoShow);
			}
			if (!ReceiptsGenList.get(i).getRecIsPosted()) {
				//Msg_Lines=  AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, ReceiptsGenList.get(i).getAMN_Employee_ID(), ReceiptsGenList.get(i).getAMN_Payroll_ID(), trxNameLine);
				Msg_Lines=  AMNPayrollCreateDocs.CreatePayrollOneDocumentLinesFromArray(ctx, ReceiptConcepts, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, ReceiptsGenList.get(i).getAMN_Employee_ID(), ReceiptsGenList.get(i).getAMN_Payroll_ID(), trxNameLine);
				try {
					AmerpPayrollCalc.PayrollEvaluationArrayCalculate(ctx, ReceiptsGenList.get(i).getAMN_Payroll_ID());
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					Msg_Lines = Msg_Lines + "** ERROR ** PayrollEvaluationArrayCalculate ";
					//e.printStackTrace();
				}
			}
		}
		// log.warning("...Lines Completed....");
		// **********************
		// Calculate Document
		// Payroll Historic
		// **********************
		for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
			Percent = 100 * (i / NoRecs);
			AMN_Employee_ID=ReceiptsGenList.get(i).getAMN_Employee_ID();
log.warning("Payroll Historic i=("+i+") NoLines="+ReceiptsGenList.get(i).getNoLines()+" "+ReceiptsGenList.get(i).getAMN_Payroll_ID()+" "+ReceiptsGenList.get(i).getDescription()+" "+ReceiptsGenList.get(i).getError());
			trxNameLine = trxName+"_"+AMN_Employee_ID;
			Percent = 100 * (i / ReceiptsGenList.size());
			// Percentage Monitor
			MessagetoShow = String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
					" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
					ReceiptsGenList.get(i).getDocumentNo().trim()+" "+
					AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID")+":"+	
					String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim()).replace(' ', '_') +"_"+
					" ";
			if (processMonitor != null) {
				processMonitor.statusUpdate(MessagetoShow);
			}
			if (!ReceiptsGenList.get(i).getRecIsPosted()) {
log.warning("Employee="+ReceiptsGenList.get(i).getAMN_Employee_ID()+" period="+amnperiod.getAMNDateIni()+"  "+amnperiod.getAMNDateEnd());
				amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, ReceiptsGenList.get(i).getAMN_Employee_ID(), amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLine);
			}
		}
log.warning("...Payroll Historic Completed....");
		// ********************************************
		// Nominal Salary UPDATE ONLY ON NN Process
		// ********************************************
		if (AMN_Process_Value.equalsIgnoreCase("NN")) {
			for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
				Percent = 100 * (i / NoRecs);
				AMN_Payroll_ID=ReceiptsGenList.get(i).getAMN_Payroll_ID();
				amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, null);
				AMN_Employee_ID=ReceiptsGenList.get(i).getAMN_Employee_ID();
				trxNameLine = trxName+"_"+AMN_Employee_ID;
log.warning("Nominal Salary UPDATE ONLY ON NN Process i=("+i+") NoLines="+ReceiptsGenList.get(i).getNoLines()+" "+
		ReceiptsGenList.get(i).getAMN_Payroll_ID()+" "+ReceiptsGenList.get(i).getDescription()+" "+ReceiptsGenList.get(i).getError());
				Percent = 100 * (i / ReceiptsGenList.size());
				// Percentage Monitor
				MessagetoShow = String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
						" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
						ReceiptsGenList.get(i).getDocumentNo().trim()+" "+
						AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID")+":"+
						String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim()).replace(' ', '_') +"_"+
						" ";
				if (processMonitor != null) {
					processMonitor.statusUpdate(MessagetoShow);
				}
				if (!ReceiptsGenList.get(i).getRecIsPosted()) {
					Msg_Updates=  AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,ReceiptsGenList.get(i).getAMN_Employee_ID(), ReceiptsGenList.get(i).getAMN_Payroll_ID(), trxNameLine);
				}
				// SALARY NOMINAL HISTORIC
		    	if (!ReceiptsGenList.get(i).getRecIsPosted()) {
//log.warning("Nominal Salary UPDATE 	"+	amnpayroll.getDescription()    );		
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.updateSalaryAmnPayrollHistoric(ctx, null, AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxName);
				}
				// END OF SALARY HISTORIC
			}
		}
//log.warning("...FIN updateSalaryAmnPayrollHistoric....");

		// *******************************************************
		// RESET SOCIAL BENEFIT HISTORIC UPDATED  ON NP Process
		// *******************************************************
		if (AMN_Process_Value.equalsIgnoreCase("NP") ) {
			for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
				Percent = 100 * (i / NoRecs);
//log.warning("RESET SOCIAL BENEFIT HISTORIC UPDATED  ON NP Process i=("+i+") NoLines="+ReceiptsGenList.get(i).getNoLines()+" "+ReceiptsGenList.get(i).getAMN_Payroll_ID()+" "+ReceiptsGenList.get(i).getDescription()+" "+ReceiptsGenList.get(i).getError());
				NextPPDateIni = null;
				NextPPDateEnd = null;
				trxNameLine = trxName+"_"+AMN_Employee_ID;
				AMN_Payroll_ID=ReceiptsGenList.get(i).getAMN_Payroll_ID();
				amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, null);
				AMN_Employee_ID=ReceiptsGenList.get(i).getAMN_Employee_ID();
				Percent = 100 * (i / ReceiptsGenList.size());
				// Percentage Monitor
				MessagetoShow = String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
						" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
						ReceiptsGenList.get(i).getDocumentNo().trim()+" "+
						AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID")+":"+
						String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim()).replace(' ', '_') +"_"+
						" ";
				if (processMonitor != null) {
					processMonitor.statusUpdate(MessagetoShow);
				}
				if (!ReceiptsGenList.get(i).getRecIsPosted()) {
					if (amnpayroll.getAmountAllocated().compareTo(BigDecimal.valueOf(0)) > 0 ) {
					    // SOCIAL BENEFIT HISTORIC UPDATED Current Period and Two Previouss Periods
					    // Current Period
				    	Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, ReceiptsGenList.get(i).getAMN_Employee_ID(), amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxNameLine);
					    // Next Previuss Period
						// Calculates Average last 12 months
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(amnperiod.getAMNDateIni());
						// SET HOUR 00:00:00 
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
					} else {
					    // SOCIAL BENEFIT HISTORIC UPDATED Current Period ONLY
					    // Current Period ONLY
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxName);
					}
				}
			}
		}

		// *******************************************************
		// RESET SOCIAL BENEFIT HISTORIC UPDATED  ON NU Process
		// *******************************************************
		if (AMN_Process_Value.equalsIgnoreCase("NU") ) {
			for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
				Percent = 100 * (i / NoRecs);
				AMN_Employee_ID=ReceiptsGenList.get(i).getAMN_Employee_ID();
//log.warning("RESET SOCIAL BENEFIT HISTORIC UPDATED  ON NU Process i=("+i+") NoLines="+ReceiptsGenList.get(i).getNoLines()+" "+ReceiptsGenList.get(i).getAMN_Payroll_ID()+" "+ReceiptsGenList.get(i).getDescription()+" "+ReceiptsGenList.get(i).getError());
				NextPPDateIni = null;
				NextPPDateEnd = null;
				trxNameLine = trxName+"_"+AMN_Employee_ID;
				Percent = 100 * (i / ReceiptsGenList.size());
				// Percentage Monitor
				MessagetoShow = String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
						" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
						ReceiptsGenList.get(i).getDocumentNo().trim()+" "+
						AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID")+":"+
						String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim()).replace(' ', '_') +"_"+
						" ";
				if (processMonitor != null) {
					processMonitor.statusUpdate(MessagetoShow);
				}
				if (!ReceiptsGenList.get(i).getRecIsPosted()) {
					if (amnpayroll.getAmountAllocated().compareTo(BigDecimal.valueOf(0)) > 0 ) {
					    // UTILITIES HISTORIC UPDATED
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, ReceiptsGenList.get(i).getAMN_Employee_ID(), amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxNameLine);
					    // Next Previuss Period
						// Calculates Average last 12 months
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(amnperiod.getAMNDateIni());
						// SET HOUR 00:00:00 
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
					    Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
						// Next Previuss Period
						cal.add(Calendar.DAY_OF_YEAR, -1);
						NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
						cal.set(Calendar.DAY_OF_MONTH, 1);
						NextPPDateIni = new Timestamp(cal.getTimeInMillis());
						Msg_Updates= Msg_Updates+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLine);
					}
				}
			}
		}
		
		//AMNPayrollCreateInvoicesAllProcess
		return true;
	} 

    /**
     * AMNPayrollCreateReceiptLinesArray
     * @param ctx
     * @param p_AMN_Process_ID
     * @param p_AMN_Contract_ID
     * @param p_AMN_Payroll_ID
     * @param trxName
     * @return
     */
    public boolean AMNPayrollCreateReceiptLinesArray(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, String trxName )  {
    	// ReceiptConcepts
	    
    	
    	String sql="";
		//
		sql ="SELECT  " + 
			"cty.calcorder,  " + 
			"cty.amn_concept_types_id, " + 
			"ctp.amn_process_id, " + 
			"ctp.amn_concept_types_proc_id,  " + 
			"cty.defaultvalue, " + 
			"cty.scriptdefaultvalue, " + 
			"cty.value, " + 
			"cty.name, " + 
			"cty.description, " + 
			"cty.AMN_Concept_Uom_ID "+ 
			"FROM amn_concept_types as cty " + 
			"LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id = cty.amn_concept_types_id) " + 
			"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) " + 
			"LEFT JOIN amn_concept_types_contract as ctc ON (ctc.amn_concept_types_id = cty.amn_concept_types_id) " +
			"WHERE cty.rule = 'F' AND ctp.amn_process_id = ?" + 
			" AND ctc.amn_contract_id =? "+
			" AND cty.isactive ='Y' "+
			" AND ctp.isactive ='Y' "+
			" AND ctc.isactive ='Y' "+	
			"ORDER BY cty.calcorder" 
			;
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Process_ID);
            pstmt1.setInt (2, p_AMN_Contract_ID);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				ReceiptLines = new AMNReceiptLines();
				ReceiptLines.setCalcOrder(rsod1.getInt(1));
				ReceiptLines.setAMN_Concept_Type_ID(rsod1.getInt(2));
				ReceiptLines.setAMN_Process_ID(rsod1.getInt(3));
				ReceiptLines.setAMN_Concept_Type_Proc_ID(rsod1.getInt(4));
				ReceiptLines.setDefaultValue(rsod1.getString(5));
				ReceiptLines.setScriptDefaultValue(rsod1.getString(6));
				ReceiptLines.setConceptValue(rsod1.getString(7));
				ReceiptLines.setConceptName(rsod1.getString(8));
				ReceiptLines.setConceptDescription(rsod1.getString(9));
				ReceiptLines.setAMN_Concept_Uom_ID(rsod1.getInt(10));

				ReceiptLines.setAMN_Contract_ID(p_AMN_Contract_ID);
				// ADD to ReceiptConcepts Array
				ReceiptConcepts.add(ReceiptLines);
			}
		}
	    catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rsod1, pstmt1);
			rsod1 = null; 
			pstmt1 = null;
		}
    	return false;
    }
}



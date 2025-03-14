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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpMsg;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/** AMNPayrollCreateOneDoc
 *  Description: Procedure called from iDempiere AD
 * 			Create Payroll Receipt
 * 			For One Process, One Contract, One Period and One Employee
 * * 
 * Result:	Create PayrollReceipt according to Payroll Concepts Rules
 * 			Create Header on AMN_Payroll and Lines on AMN_Payroll_detail
 * 			Using AMNPayrollCreateDocs Class 
 * @author luisamesty
 *
 */
public class AMNPayrollCreateOneDoc extends SvrProcess{
	
	static CLogger log = CLogger.getCLogger(AMNPayrollCreateOneDoc.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private int p_AMN_Payroll_Lot_ID = 0;
	private int p_AMN_Payroll_ID = 0;
	// New Possible Parameters 
	// Only Updated on Receipt header if NOT NULL
	// DateAcct, InvDateEnd, InvDateIni, RefDateEnd, RefDateIni
	static Timestamp p_DateAcct = null;
	static Timestamp p_InvDateEnd = null; 
	static Timestamp p_InvDateIni = null;
	static Timestamp p_RefDateEnd = null;
	static Timestamp p_RefDateIni = null;
	String Employee_Name,AMN_Process_Value="";
	String sql="";
    String Msg_Value="";
   	static String Msg_Value1 = Msg.getElement(Env.getCtx(), "AMN_Payroll_ID")+":";
   	static String Msg_Value2 = Msg.getElement(Env.getCtx(), "AMN_Payroll_Detail_ID")+":";
   	static String Msg_Value3 = Msg.getMsg(Env.getCtx(), "Refreshed")+":";
	
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
			else if (paraName.equals("AMN_Employee_ID"))
				p_AMN_Employee_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID = para.getParameterAsInt();
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
		//log.warning(".....Parametros....AMN_Payroll_ID:"+p_AMN_Payroll_ID);
		//log.warning("Process_ID:"+p_AMN_Process_ID+"  AMN_Contract_ID:"+p_AMN_Contract_ID+"  AMN_Period_ID:"+p_AMN_Period_ID);
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected String doIt() throws Exception {

	    boolean okProcess = false;
	    String Msg_Header = "";
	    MAMN_Process amnprocess = new MAMN_Process(getCtx(), p_AMN_Process_ID, null); 
    	MAMN_Period  amnperiod  = new MAMN_Period(getCtx(), p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(getCtx(), 0, get_TrxName());
		MAMN_Employee amnemployee = new MAMN_Employee(getCtx(), p_AMN_Employee_ID, null);
		// Default Currency  for Contract
   		Integer Currency_ID = AmerpUtilities.defaultAMNContractCurrency(amnemployee.getAMN_Contract_ID());
   		// Default ConversionType for Contract
   		Integer ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(amnemployee.getAMN_Contract_ID());
   		//
	   	AMN_Process_Value = amnprocess.getAMN_Process_Value();
	   	Msg_Header = Msg.getElement(getCtx(), "AMN_Process_ID")+":"+amnprocess.getName().trim();
	    addLog(Msg_Header);
	    MAMN_Payroll amnpayroll = null;
	    if (p_AMN_Payroll_ID == 0) {
		    // Verify if any AMN_Payroll_ID is created
			amnpayroll = MAMN_Payroll.findByAMNPayroll(getCtx(), Env.getLanguage(Env.getCtx()).getLocale(), 
					p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
			if (amnpayroll != null ) 
				p_AMN_Payroll_ID = amnpayroll.getAMN_Payroll_ID();
	    } else {
	    	amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null);
	    }
	    // Verify is Posted
		if (amnpayroll != null && amnpayroll.isPosted()) {
			Msg_Header = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+":"+amnpayroll.getDocumentNo().trim()+" "+
					amnpayroll.getValue().trim()+" "+amnpayroll.getName().trim();
		    addLog(Msg_Header);
			Msg_Header = Msg.translate(getCtx(), "Error")+Msg.translate(getCtx(), "document.status.completed")+" ("+amnpayroll.getDocStatus()+") ";
		    addLog(Msg_Header);
		    return "ERROR";
		}
	    // ************************
    	// Process VALIDATION	
    	// ************************
	    if (AMN_Process_Value.equalsIgnoreCase("NN") ||
	    		AMN_Process_Value.equalsIgnoreCase("TI") ||
	    		AMN_Process_Value.equalsIgnoreCase("NO")) {
	    	// ************************
	    	// Process NN, NO and TI	
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+AMN_Process_Value.trim()+"-"+amnprocess.getName().trim());
	    	//log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    	okProcess = AMNPayrollCreateInvoiceNN(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, Currency_ID, ConversionType_ID, get_TrxName());
		    // Nominal Salary UPDATE ONLY ON NN Process
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.updateSalaryAmnPayrollHistoric(getCtx(), null, p_AMN_Employee_ID, 
		    		amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), Currency_ID, get_TrxName())+"\r\n";
		    // Nominal Salary UPDATE END

	    } else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
	    	// ************************
	    	// Process NV	
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    			AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    			Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoiceNV(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID,Currency_ID, ConversionType_ID, get_TrxName());
	    } else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
	    	// ************************
	    	// Process NP
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoiceNP(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, Currency_ID, ConversionType_ID, get_TrxName());
	    } else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
	    	// ************************
	    	// Process NU
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	//log.warning(".........NU..........");
	    	okProcess = AMNPayrollCreateInvoiceNU(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID,Currency_ID, ConversionType_ID, get_TrxName());
	    	//log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PI")) {
	    	// ************************
	    	// Process PI
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicePI(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID,Currency_ID, ConversionType_ID, get_TrxName());
	    	//log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PL")) {
	    	// ************************
	    	// Process PL 
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicePL(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID,Currency_ID, ConversionType_ID, get_TrxName());
	    	//log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    } else if (AMN_Process_Value.equalsIgnoreCase("PR")) {
	    	// ************************
	    	// Process PR
	    	// ************************
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+
	    	AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n")+
	    	Msg.getMsg(getCtx(),"Description")+":"+amnprocess.getDescription();
	    	okProcess = AMNPayrollCreateInvoicePR(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, 
	    			p_AMN_Payroll_ID,Currency_ID, ConversionType_ID, get_TrxName());
	    	//log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    } else {
	    	Msg_Value=Msg_Value+(Msg.getMsg(getCtx(), "Process")+":"+AMN_Process_Value.trim()+"-"+amnprocess.getName().trim()+" "+Msg.getMsg(getCtx(),"NotAvailable")+" \n");
	    }
	    addLog(Msg_Value);
	    addLog(Msg_Value1);
	    addLog(Msg_Value2);
	    addLog(Msg_Value3);
	    if ( okProcess ) 
	    	return "OK";
	    else	    	
	    	return null;
    }

    
    /*
     * AMNPayrollCreateInvoicesNN
     * Standard Payroll Invoices
     */
    static public boolean AMNPayrollCreateInvoiceNN(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, 
    		Timestamp p_DateAcct, Timestamp p_InvDateIni, Timestamp p_InvDateEnd, Timestamp p_RefDateIni, Timestamp p_RefDateEnd,
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	
    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
    	MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
    	String Message_Loc="";
    	// Verify if Employee Status 'ACTIVE'
    	if ( amnemployee.isActive() && amnemployee.getStatus().equalsIgnoreCase("A") ) {
    		// Nueva transacción por lote
    	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsNN"), true);
    	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
	    	// Create Docs Headers and Lines
    		// Document Header
	     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, 
	     			p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID,
	     			p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd,
	     			trxNameLocal)+"\r\n"; 
	     	trx.commit(); // Guarda los cambios
	     	//log.warning("After CreatePayrollOneDocument ="+Msg_Value1);
	     	// Document Lines
		    Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, 
		    		p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID,
		    		p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, 
		    		trxNameLocal)+"\r\n";
		    trx.commit(); // Guarda los cambios
			    //log.warning("After CreatePayrollOneDocumentLines ="+Msg_Value2);
		    // Calculate Document
		    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID,
		    		p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal)+"\r\n";
	     	trx.commit(); // Guarda los cambios
		    //log.warning("After CalculateOnePayrollDocument ="+Msg_Value3);
			// SALARY HISTORIC
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, p_AMN_Employee_ID, 
		    		amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal)+"\r\n";
	     	trx.commit(); // Guarda los cambios
		    //log.warning("After createAmnPayrollHistoric ="+Msg_Value3);
			// SALARY HISTORIC END
	     	// Cerrar transacción
	     	trx.close();  
		    return true;
    	} else {
    		Message_Loc = Msg.getElement(ctx, "AMN_Employee_ID")+":"+
    				amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+ 
    				Msg.getElement(ctx, " Status")+":"+ amnemployee.getStatus() +
    				"-"+MAMN_Employee.findAMN_Employee_Status(p_AMN_Employee_ID) ;
    		Msg_Value1 = Msg_Value1 + Message_Loc;
    		Msg_Value2 = Msg_Value2 + Message_Loc;
    		Msg_Value3 = Msg_Value3 + Message_Loc;
		    return true;
    	}
    }
    /*
     * AMNPayrollCreateInvoiceNP: SAME NN
     * Social Benefits Payroll Invoices
     */
    public boolean AMNPayrollCreateInvoiceNP(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID,
    		Timestamp p_DateAcct, Timestamp p_InvDateIni, Timestamp p_InvDateEnd, Timestamp p_RefDateIni, Timestamp p_RefDateEnd,
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {

    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
    	Timestamp NextPPDateIni = null;
    	Timestamp NextPPDateEnd = null;

    	// Create Docs Headers and Lines
    	// Nueva transacción por lote
	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsNV"), true);
	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
    	// Document Header
     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal)+"\r\n"; 
    	trx.commit(); // Guarda los cambios
    	// Document Lines
	    Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal)+"\r\n";
	    trx.commit(); // Guarda los cambios
    	// Calculate Document
	    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal)+"\r\n";
	    trx.commit(); // Guarda los cambios
    	// SALARY HISTORIC
	    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx , null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal)+"\r\n";
	    trx.commit(); // Guarda los cambios
    	// SALARY HISTORIC END
	    // Find AMN_Payroll_ID 
	    // Verify if Amount Allocated Total is > 0
		MAMN_Payroll amnpayroll = null;
		amnpayroll = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(Env.getCtx()).getLocale(), 
				p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
		if (amnpayroll.getAmountAllocated().compareTo(BigDecimal.valueOf(0)) > 0 ) {
		    // SOCIAL BENEFIT HISTORIC UPDATED Current Period and Two Previouss Periods
		    // Current Period
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
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
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	    	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
		} else {
		    // SOCIAL BENEFIT HISTORIC UPDATED Current Period ONLY
		    // Current Period ONLY
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	    }
		trx.close(); // Cierra 
    	return true;
    	
    }
    /*
     * AMNPayrollCreateInvoiceNV: SAME NN
     * Vacation Payroll Invoices
     */
    public boolean AMNPayrollCreateInvoiceNV(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID, 
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {
  
    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
		//MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		//log.warning("Recibo="+amnperiod.getName()+"  Employee="+amnemployee.getValue()+"_"+amnemployee.getName());    	

    	// Create Docs Headers and Lines
    	// Nueva transacción por lote
	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsNV"), true);
	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
    	// Document Header
     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal); 
     	trx.commit(); // Guarda los cambios
	    // Document Lines
	    Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID,p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal);
     	trx.commit(); // Guarda los cambios
	    // Calculate Document
	    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal);
     	trx.commit(); // Guarda los cambios
		// SALARY HISTORIC
	    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal);
     	trx.commit(); // Guarda los cambios
		// SALARY HISTORIC END
     	trx.close(); // Cierra
    	return true;
    	}
    /*
     * AMNPayrollCreateInvoiceNP: SAME NU
     * Utilities Payroll Invoices
     */
    public boolean AMNPayrollCreateInvoiceNU(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID,
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {
  
    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
    	Timestamp NextPPDateIni = null;
    	Timestamp NextPPDateEnd = null;
   	
    	// Create Docs Headers and Lines
    	// Nueva transacción por lote
	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsNU"), true);
	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
    	// Document Header
     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal); 
     	trx.commit(); // Guarda los cambios
     	// Document Lines
     	Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal);
     	trx.commit(); // Guarda los cambios
     	// Calculate Document
	    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal);
	    trx.commit(); // Guarda los cambios
     	// SALARY HISTORIC
	    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx , null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal);
	    trx.commit(); // Guarda los cambios
     	// SALARY HISTORIC END
	    // Find AMN_Payroll_ID 
	    // Verify if Amount Allocated Total is > 0
		MAMN_Payroll amnpayroll = null;
		amnpayroll = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(getCtx()).getLocale(), 
				p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
		if (amnpayroll.getAmountAllocated().compareTo(BigDecimal.valueOf(0)) > 0 ) {
		    // UTILITIES HISTORIC UPDATED
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx,  null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
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
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
	     	// Next Previuss Period
			cal.add(Calendar.DAY_OF_YEAR, -1);
			NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			NextPPDateIni = new Timestamp(cal.getTimeInMillis());
		    Msg_Value3= Msg_Value3+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, p_AMN_Employee_ID, NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxNameLocal);
		    trx.commit(); // Guarda los cambios
		}
		trx.close(); // Cierra
	    return true;
    }
    
    /*
     * AMNPayrollCreateInvoicePL: SAME NN
     * Payroll Invoices FOR PI Process Social Benefit Interest 
     */
    public boolean AMNPayrollCreateInvoicePI(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID,
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {
  
    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
    	
    	// Create Docs Headers and Lines
   		// Nueva transacción por lote
	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsPI"), true);
	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
    	// Document Header
     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal); 
     	trx.commit(); // Guarda los cambios
	    // Document Lines
	    Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd,trxNameLocal);
	    trx.commit(); // Guarda los cambios
	    // Calculate Document
	    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal);
	    trx.commit(); // Guarda los cambios
	    // SALARY HISTORIC
	    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal);
	    trx.commit(); // Guarda los cambios
	    // SALARY HISTORIC END
	    trx.close();	// Cierra trx
    	return true;
    	}
    /*
     * AMNPayrollCreateInvoicePL: SAME NN
     * Payroll Invoices FOR PL Process
     */
    public boolean AMNPayrollCreateInvoicePL(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID,
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {
  
    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
    	
    	// Create Docs Headers and Lines
   		// Nueva transacción por lote
	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsPL"), true);
	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
    	// Document Header
     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal); 
    	trx.commit(); // Guarda los cambios
     	// Document Lines
	    Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal);
    	trx.commit(); // Guarda los cambios
	    // Calculate Document
	    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal);
    	trx.commit(); // Guarda los cambios
	    // SALARY HISTORIC
	    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal);
    	trx.commit(); // Guarda los cambios
	    // SALARY HISTORIC END
    	trx.close();	// Cierra trx
    	return true;
    	}
    /*
     * AMNPayrollCreateInvoicePR: SAME NN
     * Payroll Invoices FOR PR Social Benefit Anticipate Process
     */
    public boolean AMNPayrollCreateInvoicePR(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Employee_ID, int p_AMN_Payroll_ID,
    		int C_Currency_ID, int C_ConversionType_ID, String trxName) {
  
    	MAMN_Period  amnperiod  = new MAMN_Period(ctx, p_AMN_Period_ID, null);
    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, null);
    	
    	// Create Docs Headers and Lines
 		// Nueva transacción por lote
	    Trx trx = Trx.get(Trx.createTrxName("AMNPayrollCreateDocsPR"), true);
	    String trxNameLocal = trx.getTrxName();  // ✅ Usar esta transacción en todo el proceso
    	// Document Header
     	Msg_Value1 = Msg_Value1 + AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal); 
     	trx.commit(); 
     	// Guarda los cambios
     	// Document Lines
	    Msg_Value2 =  Msg_Value2 + AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, p_AMN_Employee_ID, p_AMN_Payroll_ID, p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, trxNameLocal);
	    trx.commit(); 
     	// Calculate Document
	    Msg_Value3= Msg_Value3 + AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID,p_AMN_Employee_ID, p_AMN_Payroll_ID, trxNameLocal);
	    trx.commit(); 
     	// SALARY HISTORIC
	    Msg_Value3= Msg_Value3+ amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, p_AMN_Employee_ID, amnperiod.getAMNDateIni(), amnperiod.getAMNDateEnd(), trxNameLocal);
	    trx.commit(); 
     	// SALARY HISTORIC END
    	trx.close();
	    return true;
    	}
}


package org.amerp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpMsg;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClientInfo;
import org.compiere.model.MProductionLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrollDeleteOnePeriod  extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrollDeleteOnePeriod.class);
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	private int p_AMN_Period_ID = 0;
	private int p_AMN_Payroll_Lot_ID = 0;
	String Employee_Name ="";
	String AMN_Process_Value="";
	String Employee_Value="";
	String sql="";
    int NoRecs = 0;
    int NoRecsLines = 0;
    String Msg_Error="";
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
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {


	    String Msg_Value="";
	    String MessagetoShow="";
	    boolean okReceipts = false;
	    int AMN_Payroll_ID = 0;
	    String DocumentNo = "";
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
	    		AMN_Process_Value.equalsIgnoreCase("NO") || 
	    		AMN_Process_Value.equalsIgnoreCase("NU") ||
	    		AMN_Process_Value.equalsIgnoreCase("NP") ){
	    	// *************************
	    	// Process NN PO TI NO NU NP	
	    	// *************************
	    	// log.warning("p_AMN_Process_ID:"+p_AMN_Process_ID+"  p_AMN_Contract_ID"+ p_AMN_Contract_ID+"  p_AMN_Period_ID"+ p_AMN_Period_ID);
	    	okReceipts = amnPayrollDeleteInvoicesGeneratePayrollArrays(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, Currency_ID, ConversionType_ID, get_TrxName());

	    } else {
	    	Msg_Value = (Msg.getMsg(getCtx(), "Process")+":"+AMN_Process_Value.trim()+Msg.getMsg(getCtx(),"NotAvailable")+" \n");
	    }
	    //
	    if ( okReceipts ) {
	    	
			if( Msg_Error.isEmpty() ) {
				
			    IProcessUI processMonitor = Env.getProcessUI(getCtx());
			    int Percent = 0;
		    	// END MESSAGE
				Msg_Value = Msg.getElement(getCtx(), "AMN_Process_ID")+":"+amnprocess.getName().trim();
			    addLog(Msg_Value);
			    Msg_Value = Msg.getElement(getCtx(), "AMN_Payroll_ID")+" "+
			    		Msg.getElement(getCtx(), "Qty")+
			    		" ("+ReceiptsGenList.size()+") :";
			    addLog(Msg_Value);		
			    Msg_Value = Msg.getElement(getCtx(), "AMN_Payroll_Detail_ID")+" "+
			    		Msg.getElement(getCtx(), "Qty")+
			    		" ("+ReceiptConcepts.size()+") :";
			    addLog(Msg_Value);	
			    // DELETE LINES
			    NoRecsLines = ReceiptConcepts.size();
			    if(NoRecsLines > 0) {
				    AMN_Payroll_ID = ReceiptConcepts.get(0).getAMN_Payroll_ID();
				    // FOR all Rreceipt Lines
				    for (int i=0 ; i < ReceiptConcepts.size() ; i++) {
				    	// Check if same AMN_Payroll_ID
						if (ReceiptConcepts.get(i).getAMN_Payroll_ID() != AMN_Payroll_ID) {
							// Change
							AMN_Payroll_ID = ReceiptConcepts.get(i).getAMN_Payroll_ID();
						}
						MAMN_Payroll amnp = new MAMN_Payroll(getCtx(), AMN_Payroll_ID, get_TrxName());
						MAMN_Employee emp = new MAMN_Employee(getCtx(), amnp.getAMN_Employee_ID(), get_TrxName());
					    DocumentNo= amnp.getDocumentNo();
					    // log.warning("Deleting Lines i="+i+"  AMN_Payroll_ID="+AMN_Payroll_ID+ "  DocumentNo="+DocumentNo);
					    // Percent to show
						if(NoRecsLines > 0)
							Percent = 100 * (i / NoRecsLines);
						// Percentage Monitor
						MessagetoShow = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_Detail_ID")+": "+
								String.format("%-4s",i)+"/"+String.format("%-4s",NoRecsLines)+
								" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+DocumentNo+" "+
								AmerpMsg.getElementPrintText(getCtx(), "AMN_Employee_ID")+":"+
								String.format("%-50s",emp.getValue().trim()+"_"+emp.getName().trim().replace(' ', '_') +"_"+" ");
						if (processMonitor != null) {
							processMonitor.statusUpdate(MessagetoShow);
						}
						// DELETE Receipt Lines
						amnPayrollDeleteInvoicesAllProcessOneLine(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, ReceiptConcepts.get(i).getAMN_Payroll_Detail_ID(),  get_TrxName());
						log.warning("Updating Final Message"+Msg_Value);
					}
			    }
			    Percent = 0;
			    NoRecs = ReceiptsGenList.size();	
			    // FOR all Rreceipt Headeres
			    if (NoRecs > 0) {
				    for (int i=0 ; i < ReceiptsGenList.size() ; i++) {
				    	// Check if same AMN_Payroll_ID
						AMN_Payroll_ID = ReceiptsGenList.get(i).getAMN_Payroll_ID();
						// Percent to show
						if(NoRecs > 0)
							Percent = 100 * (i / NoRecs);
						// log.warning("Deleting Headers i="+i+"  AMN_Payroll_ID="+AMN_Payroll_ID+ "  DocumentNo="+ReceiptsGenList.get(i).getDocumentNo());
						// Percentage Monitor
						MessagetoShow = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_Detail_ID")+": "+
								String.format("%-4s",i)+"/"+String.format("%-4s",NoRecs)+
								" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
								ReceiptsGenList.get(i).getDocumentNo()+" "+
								AmerpMsg.getElementPrintText(getCtx(), "AMN_Employee_ID")+":"+
								String.format("%-50s",ReceiptsGenList.get(i).getEmployee_Value().trim()+"_"+ReceiptsGenList.get(i).getEmployee_Name().trim().replace(' ', '_') +"_"+" ");
						if (processMonitor != null) {
							processMonitor.statusUpdate(MessagetoShow);
						}
						// DELETE Receipt Header
						amnPayrollDeleteInvoicesAllProcessOneHeader(getCtx(), p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, ReceiptsGenList.get(i).getAMN_Payroll_ID(),  get_TrxName());
						log.warning("Updating Final Message"+Msg_Value);
					}
			    }
				Msg_Value = "OK";
				
			} else {
				Msg_Value = Msg_Value + Msg.getMsg(getCtx(), "Errors")+":  \r\n";
				addLog(Msg_Value);	
				Msg_Value = "***** "+ AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+" - "+Msg.getMsg(getCtx(), "completed")+"(s) ***** \r\n";
				addLog(Msg_Value);	
				Msg_Value = "*** "+Msg.translate(getCtx(), "Warning")+" *** \r\n";  
				addLog(Msg_Value);	
				Msg_Value = AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+"(s)"+ NoRecs+ " ";
				Msg_Value = Msg_Value + AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+"(s) "+ Msg.getMsg(Env.getCtx(),"completed")+":  \r\n";
				addLog(Msg_Value);	
				Msg_Value = Msg_Value +Msg_Error;
				addLog(Msg_Value);	
				
			}

	    } else {	    	
	    	Msg_Value = Msg_Value + AmerpMsg.getElementPrintText(getCtx(), "AMN_Payroll_ID")+"(s)"+ NoRecs+ " ";
	    	addLog(Msg_Value);
	    }
	    
		return Msg_Value;
    
	}

	private boolean amnPayrollDeleteInvoicesAllProcessOneHeader(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Payroll_ID, String trxName) {
	
		MAMN_Payroll amnp = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
		amnp.delete(true);
		return true;
	
	}
	
	private boolean amnPayrollDeleteInvoicesAllProcessOneLine(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int p_AMN_Payroll_Detail_ID, String trxName) {
	
		MAMN_Payroll_Detail amnpd = new MAMN_Payroll_Detail(ctx, p_AMN_Payroll_Detail_ID, trxName);
		amnpd.delete(true);
		return true;
	
	}

	/**
	 * 
	 * @param ctx
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Payroll_Lot_ID
	 * @param C_Currency_ID
	 * @param C_ConversionType_ID
	 * @param trxName
	 * @return
	 */
	private boolean amnPayrollDeleteInvoicesGeneratePayrollArrays(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
		
		if (amnPayrollCreateReceiptArray(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, 0, trxName)
				&& amnPayrollCreateReceiptLinesArray(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, 0, trxName) ) 
			return true;
		else 
			return false;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Payroll_Lot_ID
	 * @param trxName
	 * @return
	 */
	private boolean amnPayrollCreateReceiptArray(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID,
				int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, String trxName) {
			
	    int AMN_Employee_ID=0;
		int AMN_Payroll_ID = 0;
		String DocumentNo ="";
		String DocStatus  ="";
		// RECEIPTS FROM AMN_Period_ID
		sql = "SELECT \n"
				+ " pyr.amn_payroll_id, "
				+ " pyr.documentno, "
				+ " pyr.description, "
				+ " pyr.docstatus , "
				+ " emp.AMN_Employee_ID, "
				+ " emp.value AS employee_value, "
				+ " emp.name AS employee_name "
				+ " FROM amn_period aper  "
				+ " INNER JOIN amn_payroll pyr ON pyr.amn_period_id = aper.amn_period_id "
				+ " INNER JOIN amn_employee emp ON emp.amn_employee_id = pyr.amn_employee_id "
				+ " WHERE aper.AMN_Period_ID=?  " ;
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		// **********************
		// Document Headers
		// **********************
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Period_ID);
			rspc = pstmt.executeQuery();
			while (rspc.next())
			{
				AMN_Payroll_ID = rspc.getInt(1);
				DocumentNo = rspc.getString(2).trim();
				DocStatus  = rspc.getString(4).trim();
				AMN_Employee_ID = rspc.getInt(5);
				Employee_Value= rspc.getString(6).trim();
				Employee_Name = rspc.getString(7).trim();
				String trxNameHdr = trxName+"_"+AMN_Employee_ID;
		    	// Document Header
			    // Verify if AMN_Payroll_ID is created and POSTED
				if (DocStatus.compareToIgnoreCase("DR")!=0)
					Msg_Error = Msg_Error + "\r\nDocument: "+DocumentNo+"("+DocStatus+")  "+Employee_Value.trim()+"-"+Employee_Name.trim();
				Receipt = new AMNReceipts();
				Receipt.setAMN_Employee_ID(AMN_Employee_ID);
				Receipt.setEmployee_Value(Employee_Value);
				Receipt.setEmployee_Name(Employee_Name);
				Receipt.setAMN_Payroll_ID(AMN_Payroll_ID);
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
		if (NoRecs > 0 )
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param ctx
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Payroll_Lot_ID
	 * @param trxName
	 * @return
	 */
	public boolean amnPayrollCreateReceiptLinesArray(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
			int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, String trxName )  {
    	// ReceiptConcepts
    	
    	String sql="";
		//
		sql ="SELECT "
				+ " pyr.amn_payroll_id, "
				+"  pyr_d.amn_payroll_detail_id, "
				+ " pyr_d.calcorder , "
				+ " pyr_d.amn_concept_types_proc_id , "
				+ " pyr_d.qtyvalue  "
				+ " FROM amn_period aper "
				+ " INNER JOIN amn_payroll pyr ON pyr.amn_period_id = aper.amn_period_id "
				+ " INNER JOIN amn_employee emp ON emp.amn_employee_id = pyr.amn_employee_id "
				+ " INNER JOIN amn_payroll_detail pyr_d ON pyr_d.amn_payroll_id = pyr.amn_payroll_id "
				+ " WHERE aper.AMN_Period_ID=?" 
			;
		PreparedStatement pstmt1 = null;
		ResultSet rsod1 = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Period_ID);
            rsod1 = pstmt1.executeQuery();
			while (rsod1.next())
			{
				ReceiptLines = new AMNReceiptLines();
				ReceiptLines.setAMN_Payroll_ID(rsod1.getInt(1));
				ReceiptLines.setAMN_Payroll_Detail_ID(rsod1.getInt(2));
				ReceiptLines.setCalcOrder(rsod1.getInt(3));
				ReceiptLines.setAMN_Concept_Type_Proc_ID(rsod1.getInt(4));
				ReceiptLines.setQtyValue(rsod1.getBigDecimal(5));
				ReceiptLines.setAMN_Process_ID(p_AMN_Process_ID);
				ReceiptLines.setAMN_Contract_ID(p_AMN_Contract_ID);
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
		// NoRecs Number of receipts created or updated
		NoRecsLines = ReceiptConcepts.size();
		if (NoRecsLines > 0 )
			return true;
		else
			return false;
    }
}

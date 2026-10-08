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

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpMsg;
import org.amerp.amnutilities.AmerpUtilities;
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
	String Employee_Name, AMN_Process_Value = "";
	String Employee_Value = "";
	String sql = "";
	String sql1 = "";
	static Timestamp p_DateAcct = null;
	static Timestamp p_InvDateEnd = null; 
	static Timestamp p_InvDateIni = null;
	static Timestamp p_RefDateEnd = null;
	static Timestamp p_RefDateIni = null;
	private boolean	keepExistingConcepts = false;
	
	// Receipt List
	AMNReceipts Receipt = null;
	List<AMNReceipts> ReceiptsGenList = new ArrayList<AMNReceipts>();
	// Receipt Lines List
	AMNReceiptLines ReceiptLines = null;
	List<AMNReceiptLines> ReceiptConcepts = new ArrayList<AMNReceiptLines>();

    @Override
    protected void prepare() {
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras) {
			String paraName = para.getParameterName();
			if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID = para.getParameterAsInt();
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
			else if (paraName.equals("keepExistingConcepts"))
				keepExistingConcepts = para.getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
    }

    @Override
    protected String doIt() throws Exception {
    	String trxName = get_TrxName();
    	Properties ctx = getCtx();
    	
	    MAMN_Process amnprocess = new MAMN_Process(ctx, p_AMN_Process_ID, trxName); 
	    AMN_Process_Value = amnprocess.getAMN_Process_Value();
		MAMN_Contract amncontract = new MAMN_Contract(ctx, p_AMN_Contract_ID, trxName);
		
		
   		Integer currencyID = AmerpUtilities.defaultAMNContractCurrency(p_AMN_Contract_ID);
   		Integer conversionTypeID = AmerpUtilities.defaultAMNContractConversionType(amncontract.getAMN_Contract_ID());

	    boolean okProcess = false;

	    // Cargar el objeto del periodo
	    MAMN_Period amnPeriod = new MAMN_Period(getCtx(), p_AMN_Period_ID, get_TrxName());

	    // Validación Período
	    if (amnPeriod != null && amnPeriod.get_ID() > 0) {
	            p_DateAcct = amnPeriod.getAMNDateEnd(); 
	            p_InvDateIni = amnPeriod.getAMNDateIni();
	            p_InvDateEnd = amnPeriod.getAMNDateEnd();
	            p_RefDateIni = amnPeriod.getRefDateIni();
	            p_RefDateEnd = amnPeriod.getRefDateEnd();
	    } else {
	        throw new AdempiereException("Periodo de nómina no encontrado (AMN_Period_ID=" + p_AMN_Period_ID + ")");
	    }
	    
	    switch (AMN_Process_Value.toUpperCase()) {
	    	case "NN":
	    	case "TI":
	    	case "NO":
	    		okProcess = AMNPayrollCreateInvoicesNN(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, currencyID, conversionTypeID, trxName);
	    		break;
	    	case "NV":
	    		okProcess = AMNPayrollCreateInvoicesNV(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, currencyID, conversionTypeID, trxName);
	    		break;
	    	case "NP":
	    		okProcess = AMNPayrollCreateInvoicesNP(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, currencyID, conversionTypeID, trxName);
	    		break;
	    	case "PI":
	    		okProcess = AMNPayrollCreateInvoicesPI(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, currencyID, conversionTypeID, trxName);
	    		break;
	    	case "NU":
	    		okProcess = AMNPayrollCreateInvoicesNU(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, currencyID, conversionTypeID, trxName);
	    		break;
	    	default:
	    		addLog(Msg.getMsg(ctx, "Process") + ":" + AMN_Process_Value.trim() + Msg.getMsg(ctx, "NotAvailable"));
	    		return null;
	    }

	    if (okProcess) {
	        
	        // 1. (Opcional) Si quieres enviar el archivo Excel a descargas de la UI:
	        File excelFile = AMNPayrollUtils.generateReceiptsSummaryExcel(ReceiptsGenList);
	        if (excelFile != null && excelFile.exists()) {
	            processUI.download(excelFile);
	        }

	        // 2. Llenar la pantalla del Log fila por fila mediante addLog(...)
	        
	        // Encabezado principal del log (Fila 1)
	        addLog("PROCESAMIENTO DE NÓMINA FINALIZADO");
	        
	        // Métricas generales (Fila 2)
	        long countSuccess = ReceiptsGenList.stream().filter(r -> r.Error == null || r.Error.trim().isEmpty()).count();
	        long countFail = ReceiptsGenList.size() - countSuccess;
	        long countInvoices = ReceiptsGenList.stream().filter(r -> r.InvoiceIsCreated).count();
	        
	        addLog(String.format("Recibos Generados: %d | Éxito: %d | Fallos: %d | Facturas Creadas: %d", 
	                ReceiptsGenList.size(), countSuccess, countFail, countInvoices));

	        // Separador visual
	        addLog("----------------------------------------------------------------------------------");

	        // Recorrer la lista y hacer addLog por cada recibo (cada uno será una fila en la tabla)
	        int itemSeq = 1;
	        for (AMNReceipts rec : ReceiptsGenList) {
	            
	            BigDecimal allocated = rec.AmountAllocated != null ? rec.AmountAllocated : BigDecimal.ZERO;
	            BigDecimal deducted = rec.AmountDeducted != null ? rec.AmountDeducted : BigDecimal.ZERO;
	            BigDecimal net = allocated.subtract(deducted);

	            String status = (rec.Error == null || rec.Error.trim().isEmpty()) ? "OK" : "ERROR: " + rec.Error;
	            
	            // Construir la línea informativa de la fila
	            String lineMsg = String.format("No:%d | Recibo:%s | Ficha:%s | Trabajador:%s | Líneas:%d | Asig:%.2f | Ded:%.2f | Neto:%.2f | Contabilizado:%s | Factura:%s | %s",
	                    itemSeq++,
	                    rec.DocumentNo != null ? rec.DocumentNo : "",
	                    rec.Employee_Value != null ? rec.Employee_Value : "",
	                    rec.Employee_Name != null ? rec.Employee_Name : "",
	                    rec.NoLines,
	                    allocated,
	                    deducted,
	                    net,
	                    rec.RecIsPosted ? "SÍ" : "NO",
	                    rec.InvoiceIsCreated ? "SÍ" : "NO",
	                    status
	            );

	            // CADA addLog CREA UNA FILA EN LA TABLA DEL RESULTADO
	            addLog(lineMsg);
	        }

	        addLog("----------------------------------------------------------------------------------");
	        addLog("Proceso completado exitosamente.");
	    }

	    // OBLIGATORIO: Devolver null para que iDempiere renderice la pestaña de Log
	    return null; 
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
     * @param trxName
     * @return
     */
    public boolean AMNPayrollCreateInvoicesAllProcess(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, 
    		String trxName) {
	    
    	String Msg_Header="";
	    String MessagetoShow ="";
	    
	    int AMN_Employee_ID=0;
		int AMN_Payroll_ID = 0;
	    MAMN_Payroll amnpayrollCK = null;
	    MAMN_Payroll amnpayroll = null;
	    IProcessUI processMonitor = Env.getProcessUI(ctx);
	    int recNo = 0;
	    int Percent = 0;
	    Integer NoRecs = 0;
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
		// **********************
		// Document Headers
		// **********************
		Trx trxHdr = null;
		try
		{
			// Definir un nombre base único concatenando el ID del empleado
			String trxNameHdrBase = trxName + "_Hdr_";
			// Generar el nombre de transacción único para iDempiere con UUID interno
			String trxNameHdr = Trx.createTrxName(trxNameHdrBase);
			// Instanciar y registrar el objeto Trx (el segundo parámetro 'true' la crea si no existe)
			trxHdr = Trx.get(trxNameHdr, true);
			// Obtener el número total de registros ejecutando un COUNT con el parámetro
			String sqlCount = "SELECT COUNT(*) FROM (" + sql + ") t";
			NoRecs = DB.getSQLValue(trxName, sqlCount, p_AMN_Contract_ID);
			// Preparar y ejecutar la consulta principal
			pstmt = DB.prepareStatement(sql, trxName);
            pstmt.setInt (1, p_AMN_Contract_ID);
			rspc = pstmt.executeQuery();
			while (rspc.next())
			{
				recNo++;
				AMN_Employee_ID = rspc.getInt(1);
				Employee_Name = rspc.getString(2).trim();
				Employee_Value= rspc.getString(3).trim();
				
		    	// Document Header
			    // Verify if AMN_Payroll_ID is created and POSTED
				Receipt = new AMNReceipts();
				Receipt.setAMN_Employee_ID(AMN_Employee_ID);
				Receipt.setEmployee_Value(Employee_Value);
				Receipt.setEmployee_Name(Employee_Name);
				Receipt.setNoLines(0);
				Receipt.setError("");
				Receipt.setDateAcct(p_DateAcct);
				Receipt.setInvDateIni(p_InvDateIni);
				Receipt.setInvDateEnd(p_InvDateEnd);
				Receipt.setRefDateIni(p_RefDateIni);
				Receipt.setRefDateEnd(p_RefDateEnd);

				amnpayrollCK = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(ctx).getLocale(), 
						p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, AMN_Employee_ID, trxName);
				if (amnpayrollCK != null ) {
					Receipt.setAMN_Payroll_ID(amnpayrollCK.getAMN_Payroll_ID());
					Receipt.setDocumentNo(amnpayrollCK.getDocumentNo().trim());
					Receipt.setDescription(amnpayrollCK.getValue().trim()+" "+amnpayrollCK.getName().trim());
					Receipt.setDateAcct(amnpayrollCK.getDateAcct());
					Receipt.setInvDateIni(amnpayrollCK.getInvDateIni());
					Receipt.setInvDateEnd(amnpayrollCK.getInvDateEnd());
					Receipt.setRefDateIni(amnpayrollCK.getRefDateIni());
					Receipt.setRefDateEnd(amnpayrollCK.getRefDateEnd());
					if (amnpayrollCK.isPosted() || amnpayrollCK.isProcessed()) {
						Msg_Header = Msg.translate(ctx, "Error")+Msg.translate(ctx, "document.status.completed")+" ("+amnpayrollCK.getDocStatus()+") ";
					    Receipt.setRecIsPosted(true);
					} else {
						trxHdr.start();
						AMN_Payroll_ID =  AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, 
								AMN_Employee_ID, amnpayrollCK.getAMN_Payroll_ID(), 
								amnpayrollCK.getDateAcct(), amnpayrollCK.getInvDateIni(), amnpayrollCK.getInvDateEnd(), amnpayrollCK.getRefDateIni(), amnpayrollCK.getRefDateEnd(), 
								trxNameHdr);
						Msg_Header = "("+AmerpMsg.getElementPrintText(ctx, "AMN_Payroll_ID")+" **** "+Msg.translate(ctx, "Updated")+ " **** )";
						Receipt.setRecIsPosted(false);
						Receipt.setError(Msg_Header);
						trxHdr.commit();
					}
					Receipt.setError(Receipt.getError()+" "+Msg_Header.trim());
				} else {
					// Iniciar la transacción de la cabecera/empleado
					try {
					    // Crear la cabecera dentro de trxNameHdr
					    AMN_Payroll_ID = AMNPayrollCreateDocs.CreatePayrollOneDocument(
					            ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, 
					            p_AMN_Payroll_Lot_ID, AMN_Employee_ID, 0, 
					            p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, 
					            trxNameHdr
					    );
					    // 
					    if (AMN_Payroll_ID > 0) {
					        amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, trxNameHdr);
					    } else {
						    // 3. Obtener el objeto PO dentro de la MISMA transacción trxNameHdr
						    // (Asegúrate de pasar trxNameHdr al findByAMNPayroll)
						    amnpayroll = MAMN_Payroll.findByAMNPayroll(
						            ctx, 
						            Env.getLanguage(ctx).getLocale(), 
						            p_AMN_Process_ID, 
						            p_AMN_Contract_ID, 
						            p_AMN_Period_ID, 
						            AMN_Employee_ID,
						            trxNameHdr
						    );
						    AMN_Payroll_ID = amnpayroll.getAMN_Payroll_ID();
					    }

					    if (!Receipt.RecIsPosted) {
						    Receipt.setAMN_Payroll_ID(AMN_Payroll_ID);
						    Receipt.setRecIsPosted(false);
						    Receipt.setDocumentNo(amnpayroll.getDocumentNo().trim());
						    Receipt.setDescription(amnpayroll.getValue().trim() + " " + amnpayroll.getName().trim());
						    
						    Msg_Header = "(" + AmerpMsg.getElementPrintText(ctx, "AMN_Payroll_ID") + " **** " + Msg.translate(ctx, "New") + " **** )";
						    Receipt.setError(Msg_Header);
					    }

					    // 6. Hacer COMMIT SOLO cuando la cabecera Y los detalles estén guardados
					    trxHdr.commit();
					} catch (Exception eh) {
					    log.log(Level.SEVERE, "Error al crear nómina para empleado: " + AMN_Employee_ID, eh);
					    Receipt.setError(Msg_Header);
					}
				}
				// Percentage Monitor
				MessagetoShow = String.format("%-4s",recNo)+"/"+String.format("%-4s",NoRecs)+
						" ( "+String.format("%-5s",Percent)+"% )  RecNo:"+
						Receipt.getDocumentNo().trim()+" "+
						AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID")+":"+
						String.format("%-50s",Receipt.getEmployee_Value().trim()+"_"+Receipt.getEmployee_Name().trim()).replace(' ', '_') +"_"+
						" ";
				if (processMonitor != null) {
					processMonitor.statusUpdate(MessagetoShow);
				}
			    // =========================================================================
			    // CREACIÓN DE LÍNEAS HIJAS (DETALLES Y VALES)
			    // Paarámetros ctx, Receipt y trxNameHdr
			    // =========================================================================
			    Msg_Header = AMNPayrollCreateAMNPAyrollDetail(ctx, Receipt, keepExistingConcepts,  trxNameHdr);
			    trxHdr.commit();
			    Msg_Header = Msg_Header + AMNPayrollCalculateAMNPAyrollDetail(ctx, Receipt,  trxNameHdr);
			    trxHdr.commit();
			    // AMNPayrollCalculateHistoric
			    Msg_Header = Msg_Header + AMNPayrollCalculateHistoric(ctx, Receipt,  trxNameHdr);
			    trxHdr.commit();
			    Msg_Header = Msg_Header + AMNPayrolNominalSalary(ctx, Receipt, C_Currency_ID, trxNameHdr);
			    trxHdr.commit();
			    // Social Benefits NP
			    if (AMN_Process_Value.equalsIgnoreCase("NP") ) {
			    	Msg_Header = Msg_Header + AMNPayrolNominalResetSocialBenefitsNP(ctx, Receipt, C_Currency_ID, trxNameHdr);
				    trxHdr.commit();
			    }
			    // Social Benefits NU
			    if (AMN_Process_Value.equalsIgnoreCase("NU") ) {
			    	Msg_Header = Msg_Header + AMNPayrolNominalResetSocialBenefitsNU(ctx, Receipt, C_Currency_ID, trxNameHdr);
				    trxHdr.commit();
			    }
			    // Actualiza Valores del recibo No. Línea(s)	Asignación	Deducción	Neto Pagado
			    if (Receipt.getAMN_Payroll_ID() > 0) {
			        amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, trxNameHdr);
			        if (amnpayroll !=null ) {
			        	Receipt.setAmountAllocated(amnpayroll.getAmountAllocated());
			        	Receipt.setAmountDeducted(amnpayroll.getAmountDeducted());
			        	Receipt.setAmountNetPaid(amnpayroll.getAmountNetpaid());
			        	Receipt.setNoLines(MAMN_Payroll.sqlGetAMNPayrollDetailNoLines (AMN_Payroll_ID, trxName));
			        }
			    }
			    Receipt.setError(Receipt.getError()+" "+Msg_Header.trim());
			    // Agrega registro de recibos procesados
			    ReceiptsGenList.add(Receipt);
			}				
		}
	    catch (SQLException e)
	    {
	    	if (trxHdr != null) {
	            trxHdr.rollback();
	        }
	        log.log(Level.SEVERE, "Error procesando Encabezado de Recibo de Trabajador: " + AMN_Employee_ID, e);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
			if (trxHdr != null) {
		        trxHdr.close();
		    }
		}
		//AMNPayrollCreateInvoicesAllProcess
		return true;
	} 

	// ************************
	// Document Lines CREATES
	// ************************
    private String AMNPayrollCreateAMNPAyrollDetail(Properties ctx, AMNReceipts prReceipt, boolean keepExistingConcepts, 
    		String trxName) {
	
    	String retValue="";
		if (!prReceipt.getRecIsPosted()) {
			// CREATE MAMN_Payroll_Detail (DOCUMENT LINES)
	     	// Document Lines
			retValue= AMNPayrollCreateDocs.CreatePayrollOneDocumentLines(ctx, 
					p_AMN_Process_ID, 
					p_AMN_Contract_ID, 
		    		p_AMN_Period_ID, 
		    		p_AMN_Payroll_Lot_ID, 
		    		prReceipt.getAMN_Employee_ID(), 
		    		prReceipt.getAMN_Payroll_ID(),
		    		prReceipt.getDateAcct(), prReceipt.getInvDateIni(), prReceipt.getInvDateEnd(), prReceipt.getRefDateIni(), prReceipt.getRefDateEnd(),  
		    		keepExistingConcepts,
		    		trxName);
		}
		return retValue;   	
    }
    
	// ************************************
	// Document Lines REFRESH / CALCULATES
	// ************************************
    private String AMNPayrollCalculateAMNPAyrollDetail(Properties ctx, AMNReceipts prReceipt,  String trxName) {
    	
    	String retValue="";
		if (!prReceipt.getRecIsPosted()) {
			// Calculate Document
			retValue= AMNPayrollCreateDocs.CalculateOnePayrollDocument(ctx, 
					p_AMN_Process_ID, 
					p_AMN_Contract_ID, 
					p_AMN_Period_ID,
		    		prReceipt.getAMN_Employee_ID(), 
		    		prReceipt.getAMN_Payroll_ID(),
					trxName);
		} else {
			retValue = Msg.getElement(ctx,"AMN_Payroll_ID")+": "+prReceipt.getAMN_Payroll_ID()+" "+prReceipt.getEmployee_Value() + " "+ Msg.translate(ctx, "AlreadyPosted");
		}
		return retValue;   	
    }
    
	// **************************************
	// Calculate Document Payroll Historic
	// *************************************
    private String AMNPayrollCalculateHistoric(Properties ctx, AMNReceipts prReceipt,  String trxName) {

    	String retValue="";
    	MAMN_Payroll_Historic 	amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, trxName);
    	if (!prReceipt.getRecIsPosted()) {
    		amnpayrollhistoric.createAmnPayrollHistoric(ctx, null, prReceipt.getAMN_Employee_ID(), prReceipt.getInvDateIni(), prReceipt.getInvDateEnd(), trxName);
		}
    	return retValue;
    }
    
	// ********************************************
	// Nominal Salary UPDATE ONLY ON NN Process
	// ********************************************
    private String AMNPayrolNominalSalary(Properties ctx, AMNReceipts prReceipt,  int C_Currency_ID, String trxName) {

    	String retValue="";
    	MAMN_Payroll_Historic 	amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, trxName);
    	if (!prReceipt.getRecIsPosted() && amnpayrollhistoric != null) {
    		if (amnpayrollhistoric.updateSalaryAmnPayrollHistoric(ctx, Env.getLanguage(ctx).getLocale(), prReceipt.getAMN_Employee_ID(), prReceipt.getInvDateIni(), prReceipt.getInvDateEnd(), C_Currency_ID, trxName))
    			retValue=Msg.getElement(ctx,"Salary")+" "+"OK";
		} 
    	return retValue;
    }
    
	// *******************************************************
	// RESET SOCIAL BENEFIT HISTORIC UPDATED  ON NP Process
	// *******************************************************
    private String AMNPayrolNominalResetSocialBenefitsNP(Properties ctx, AMNReceipts prReceipt, int C_Currency_ID, String trxName) {

    	String retValue="";
    	MAMN_Payroll_Historic 	amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, trxName);
    	MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, prReceipt.getAMN_Payroll_ID(), trxName);
    	if (!prReceipt.getRecIsPosted() && amnpayrollhistoric != null) {
			if (amnpayroll.getAmountAllocated().compareTo(BigDecimal.valueOf(0)) > 0 ) {
			    // SOCIAL BENEFIT HISTORIC UPDATED Current Period and Two Previouss Periods
			    // Current Period
				if (amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, 
						Env.getLanguage(ctx).getLocale(), prReceipt.getAMN_Employee_ID(), 
						prReceipt.getInvDateIni(), prReceipt.getInvDateEnd(), C_Currency_ID, trxName))
						retValue ="OK";
			    // Next Previuss Period
				// Calculates Average last 12 months
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(prReceipt.getInvDateIni());
				// SET HOUR 00:00:00 
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				Timestamp NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				Timestamp NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
			} else {
			    // SOCIAL BENEFIT HISTORIC UPDATED Current Period ONLY
			    // Current Period ONLY
				retValue= retValue+ amnpayrollhistoric.resetSocialbenefitsUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), prReceipt.getInvDateIni(), prReceipt.getInvDateEnd(), C_Currency_ID, trxName);
			}

    	}
    	return retValue;
    }
    
	// *******************************************************
	// RESET SOCIAL BENEFIT HISTORIC UPDATED  ON NU Process
	// *******************************************************
    
    private String AMNPayrolNominalResetSocialBenefitsNU(Properties ctx, AMNReceipts prReceipt, int C_Currency_ID, String trxName) {

    	String retValue="";
    	MAMN_Payroll_Historic 	amnpayrollhistoric = new MAMN_Payroll_Historic(ctx, 0, trxName);
    	MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, prReceipt.getAMN_Payroll_ID(), trxName);
    	
    	
    	if (!prReceipt.getRecIsPosted() && amnpayrollhistoric != null && amnpayroll != null) {
    		MAMN_Period amnperiod = new MAMN_Period(ctx, amnpayroll.getAMN_Period_ID(), trxName);
			if (amnperiod != null && amnpayroll.getAmountAllocated().compareTo(BigDecimal.valueOf(0)) > 0 ) {
			    // UTILITIES HISTORIC UPDATED
				if(amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), 
						prReceipt.getInvDateIni(), prReceipt.getInvDateEnd(), C_Currency_ID, trxName))
					retValue="OK";
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
				Timestamp NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				Timestamp NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
			    retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
				// Next Previuss Period
				cal.add(Calendar.DAY_OF_YEAR, -1);
				NextPPDateEnd = new Timestamp(cal.getTimeInMillis());
				cal.set(Calendar.DAY_OF_MONTH, 1);
				NextPPDateIni = new Timestamp(cal.getTimeInMillis());
				retValue= retValue+ amnpayrollhistoric.resetUtilitiesUpdatedValue(ctx, null, prReceipt.getAMN_Employee_ID(), NextPPDateIni, NextPPDateEnd, C_Currency_ID, trxName);
			}

    	}
    	return retValue;
    }
    
    private boolean AMNPayrollCreateInvoicesNN(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	return AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    }
    
    private boolean AMNPayrollCreateInvoicesNP(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	return AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    }
    
    private boolean AMNPayrollCreateInvoicesPI(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	return AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    }
    
    private boolean AMNPayrollCreateInvoicesNV(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	return AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    }
 
    private boolean AMNPayrollCreateInvoicesNU(Properties ctx, int p_AMN_Process_ID, int p_AMN_Contract_ID, 
    		int p_AMN_Period_ID, int p_AMN_Payroll_Lot_ID, int C_Currency_ID, int C_ConversionType_ID, String trxName) {
    	return AMNPayrollCreateInvoicesAllProcess(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, C_Currency_ID, C_ConversionType_ID, trxName);
    }

}
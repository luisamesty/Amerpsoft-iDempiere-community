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

        String Msg_Header = "";
        String MessagetoShow = "";
        
        int AMN_Employee_ID = 0;
        int AMN_Payroll_ID = 0;
        MAMN_Payroll amnpayrollCK = null;
        MAMN_Payroll amnpayroll = null;
        IProcessUI processMonitor = Env.getProcessUI(ctx);
        int recNo = 0;
        int Percent = 0;
        
        // 1. Construcción de la consulta base según el filtro de estado del empleado
        String baseSql = "SELECT " + 
                "amnemp.amn_employee_id as amn_employee_id, " + 
                "amnemp.name as employee_name, " + 
                "amnemp.value as employee_value, " + 
                "amncon.amn_contract_id, " + 
                "amncon.name as contract_name " + 
                "FROM amn_employee as amnemp " + 
                "LEFT JOIN amn_contract amncon ON (amnemp.amn_contract_id = amncon.amn_contract_id) ";

        String sql1 = "";
        if (AMN_Process_Value.equalsIgnoreCase("NN") ||
            AMN_Process_Value.equalsIgnoreCase("TI") ||
            AMN_Process_Value.equalsIgnoreCase("NO")) {
            sql1 = "WHERE amnemp.isactive='Y' AND amnemp.status = 'A' AND amncon.amn_contract_id=? ";   
        } else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
            sql1 = "WHERE amnemp.isactive='Y' AND amnemp.status = 'V' AND amncon.amn_contract_id=? ";
        } else if (AMN_Process_Value.equalsIgnoreCase("NP") || AMN_Process_Value.equalsIgnoreCase("PI")) {
            sql1 = "WHERE amnemp.isactive='Y' AND (amnemp.status = 'A' OR amnemp.status = 'V' OR amnemp.status = 'S') AND amncon.amn_contract_id=? ";
        } else if (AMN_Process_Value.equalsIgnoreCase("NU")) {
            sql1 = "WHERE amnemp.isactive='Y' AND (amnemp.status = 'A' OR amnemp.status = 'V') AND amncon.amn_contract_id=? ";
        }

        String whereSql = sql1 + " ORDER BY amnemp.value ";
        String fullSql = baseSql + whereSql;

        // 2. Obtener el total de registros para el monitor de progreso
        String sqlCount = "SELECT COUNT(*) FROM (" + fullSql + ") t";
        int NoRecs = DB.getSQLValue(trxName, sqlCount, p_AMN_Contract_ID);

        if (NoRecs <= 0) {
            return true;
        }

        // 3. Configuración de procesamiento por lotes (Batch Size = 50)
        int batchSize = 25;
        int offset = 0;
        boolean hasMoreRows = true;

        // Consulta por lote con LIMIT y OFFSET
        String batchSql = fullSql + " LIMIT ? OFFSET ? ";

        while (hasMoreRows) {
            // Generar un nombre de transacción independiente para este lote de 50
            String batchTrxName = Trx.createTrxName("AMN_InvoiceBatch_");
            Trx batchTrx = Trx.get(batchTrxName, true);

            try (PreparedStatement pstmt = DB.prepareStatement(batchSql, batchTrxName)) {
                pstmt.setInt(1, p_AMN_Contract_ID);
                pstmt.setInt(2, batchSize);
                pstmt.setInt(3, offset);

                try (ResultSet rspc = pstmt.executeQuery()) {
                    boolean rowsInBatch = false;

                    while (rspc.next()) {
                        rowsInBatch = true;
                        recNo++;

                        AMN_Employee_ID = rspc.getInt("amn_employee_id");
                        String Employee_Name = rspc.getString("employee_name") != null ? rspc.getString("employee_name").trim() : "";
                        String Employee_Value = rspc.getString("employee_value") != null ? rspc.getString("employee_value").trim() : "";

                        AMNReceipts Receipt = new AMNReceipts();
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

                        // Buscar si la nómina ya existe dentro de la transacción del lote
                        amnpayrollCK = MAMN_Payroll.findByAMNPayroll(ctx, Env.getLanguage(ctx).getLocale(), 
                                p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, AMN_Employee_ID, batchTrxName);

                        if (amnpayrollCK != null) {
                            Receipt.setAMN_Payroll_ID(amnpayrollCK.getAMN_Payroll_ID());
                            Receipt.setDocumentNo(amnpayrollCK.getDocumentNo().trim());
                            Receipt.setDescription(amnpayrollCK.getValue().trim() + " " + amnpayrollCK.getName().trim());
                            Receipt.setDateAcct(amnpayrollCK.getDateAcct());
                            Receipt.setInvDateIni(amnpayrollCK.getInvDateIni());
                            Receipt.setInvDateEnd(amnpayrollCK.getInvDateEnd());
                            Receipt.setRefDateIni(amnpayrollCK.getRefDateIni());
                            Receipt.setRefDateEnd(amnpayrollCK.getRefDateEnd());

                            if (amnpayrollCK.isPosted() || amnpayrollCK.isProcessed()) {
                                Msg_Header = Msg.translate(ctx, "Error") + Msg.translate(ctx, "document.status.completed") + " (" + amnpayrollCK.getDocStatus() + ") ";
                                Receipt.setRecIsPosted(true);
                            } else {
                                AMN_Payroll_ID = AMNPayrollCreateDocs.CreatePayrollOneDocument(ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Payroll_Lot_ID, 
                                        AMN_Employee_ID, amnpayrollCK.getAMN_Payroll_ID(), 
                                        amnpayrollCK.getDateAcct(), amnpayrollCK.getInvDateIni(), amnpayrollCK.getInvDateEnd(), amnpayrollCK.getRefDateIni(), amnpayrollCK.getRefDateEnd(), 
                                        batchTrxName);
                                Msg_Header = "(" + AmerpMsg.getElementPrintText(ctx, "AMN_Payroll_ID") + " **** " + Msg.translate(ctx, "Updated") + " **** )";
                                Receipt.setRecIsPosted(false);
                                Receipt.setError(Msg_Header);
                            }
                            Receipt.setError(Receipt.getError() + " " + Msg_Header.trim());
                        } else {
                            // Crear cabecera si no existe
                            try {
                                AMN_Payroll_ID = AMNPayrollCreateDocs.CreatePayrollOneDocument(
                                        ctx, p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, 
                                        p_AMN_Payroll_Lot_ID, AMN_Employee_ID, 0, 
                                        p_DateAcct, p_InvDateIni, p_InvDateEnd, p_RefDateIni, p_RefDateEnd, 
                                        batchTrxName
                                );

                                if (AMN_Payroll_ID > 0) {
                                    amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, batchTrxName);
                                } else {
                                    amnpayroll = MAMN_Payroll.findByAMNPayroll(
                                            ctx, Env.getLanguage(ctx).getLocale(), 
                                            p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, 
                                            AMN_Employee_ID, batchTrxName
                                    );
                                    if (amnpayroll != null) {
                                        AMN_Payroll_ID = amnpayroll.getAMN_Payroll_ID();
                                    }
                                }

                                if (amnpayroll != null && !Receipt.RecIsPosted) {
                                    Receipt.setAMN_Payroll_ID(AMN_Payroll_ID);
                                    Receipt.setRecIsPosted(false);
                                    Receipt.setDocumentNo(amnpayroll.getDocumentNo() != null ? amnpayroll.getDocumentNo().trim() : "");
                                    Receipt.setDescription((amnpayroll.getValue() != null ? amnpayroll.getValue().trim() : "") + " " + 
                                                           (amnpayroll.getName() != null ? amnpayroll.getName().trim() : ""));
                                    
                                    Msg_Header = "(" + AmerpMsg.getElementPrintText(ctx, "AMN_Payroll_ID") + " **** " + Msg.translate(ctx, "New") + " **** )";
                                    Receipt.setError(Msg_Header);
                                }
                            } catch (Exception eh) {
                                log.log(Level.SEVERE, "Error al crear nómina para empleado ID: " + AMN_Employee_ID, eh);
                                Receipt.setError("Error cabecera empleado " + AMN_Employee_ID);
                            }
                        }

                        // Actualizar el Monitor de Progreso
                        if (processMonitor != null) {
                            Percent = (int) (100.0 * recNo / NoRecs);
                            MessagetoShow = String.format("%-4s", recNo) + "/" + String.format("%-4s", NoRecs) +
                                    " ( " + String.format("%-5s", Percent) + "% ) RecNo:" +
                                    (Receipt.getDocumentNo() != null ? Receipt.getDocumentNo().trim() : "") + " " +
                                    AmerpMsg.getElementPrintText(ctx, "AMN_Employee_ID") + ":" +
                                    String.format("%-50s", Receipt.getEmployee_Value().trim() + "_" + Receipt.getEmployee_Name().trim()).replace(' ', '_');
                            processMonitor.statusUpdate(MessagetoShow);
                        }

                        // =========================================================================
                        // CREACIÓN Y CÁLCULO DE LÍNEAS HIJAS
                        // =========================================================================
                        if (Receipt.getAMN_Payroll_ID() > 0) {
                            Msg_Header = AMNPayrollCreateAMNPAyrollDetail(ctx, Receipt, keepExistingConcepts, batchTrxName);
                            Msg_Header += AMNPayrollCalculateAMNPAyrollDetail(ctx, Receipt, batchTrxName);
                            Msg_Header += AMNPayrollCalculateHistoric(ctx, Receipt, batchTrxName);
                            Msg_Header += AMNPayrolNominalSalary(ctx, Receipt, C_Currency_ID, batchTrxName);

                            if (AMN_Process_Value.equalsIgnoreCase("NP")) {
                                Msg_Header += AMNPayrolNominalResetSocialBenefitsNP(ctx, Receipt, C_Currency_ID, batchTrxName);
                            }
                            if (AMN_Process_Value.equalsIgnoreCase("NU")) {
                                Msg_Header += AMNPayrolNominalResetSocialBenefitsNU(ctx, Receipt, C_Currency_ID, batchTrxName);
                            }

                            amnpayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, batchTrxName);
                            if (amnpayroll != null) {
                                Receipt.setAmountAllocated(amnpayroll.getAmountAllocated());
                                Receipt.setAmountDeducted(amnpayroll.getAmountDeducted());
                                Receipt.setAmountNetPaid(amnpayroll.getAmountNetpaid());
                                Receipt.setNoLines(MAMN_Payroll.sqlGetAMNPayrollDetailNoLines(AMN_Payroll_ID, batchTrxName));
                            }
                        }

                        Receipt.setError(Receipt.getError() + " " + Msg_Header.trim());
                        ReceiptsGenList.add(Receipt);
                    }

                    if (!rowsInBatch) {
                        hasMoreRows = false;
                    }

                }

                // Commit al finalizar todo el lote de 50 registros
                batchTrx.commit();

            } catch (Exception e) {
                batchTrx.rollback();
                log.log(Level.SEVERE, "Error en lote de nóminas con offset: " + offset, e);
            } finally {
                batchTrx.close(); // Cierra la transacción de este lote
            }

            offset += batchSize;
            if (recNo >= NoRecs) {
                hasMoreRows = false;
            }

            // Pequeña pausa opcional de 100ms para permitir liberación de memoria/hilos si la base de datos es sensible
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

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
package org.amerp.process;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Docs;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.model.MInvoice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class AMNPayrollDeleteOnePeriod extends SvrProcess {

    private static CLogger log = CLogger.getCLogger(AMNPayrollDeleteOnePeriod.class);
    private int p_AMN_Process_ID = 0;
    private int p_AMN_Period_ID = 0;
    private int p_AMN_Payroll_ID = 0;

    private String AMN_Process_Value = "";

    private List<AMNReceipts> ReceiptsGenList = new ArrayList<>();
    private List<AMNReceiptLines> ReceiptConcepts = new ArrayList<>();

    @Override
    protected void prepare() {
        ProcessInfoParameter[] paras = getParameter();
        for (ProcessInfoParameter para : paras) {
            String paraName = para.getParameterName();
            if (paraName.equals("AMN_Process_ID"))
                p_AMN_Process_ID = para.getParameterAsInt();
            else if (paraName.equals("AMN_Contract_ID"))
				para.getParameterAsInt();
			else if (paraName.equals("AMN_Period_ID"))
                p_AMN_Period_ID = para.getParameterAsInt();
            else
                log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
        }   
    }

    @Override
    protected String doIt() throws Exception {
        MAMN_Process amnprocess = new MAMN_Process(getCtx(), p_AMN_Process_ID, get_TrxName()); 
        AMN_Process_Value = amnprocess.getAMN_Process_Value();
        MAMN_Period amnperiod = new MAMN_Period(getCtx(), p_AMN_Period_ID, get_TrxName());

        if (!AMN_Process_Value.equalsIgnoreCase("NN") &&
            !AMN_Process_Value.equalsIgnoreCase("TI") &&
            !AMN_Process_Value.equalsIgnoreCase("NO") && 
            !AMN_Process_Value.equalsIgnoreCase("NU") &&
            !AMN_Process_Value.equalsIgnoreCase("NP")) {
            return Msg.getMsg(getCtx(), "Process") + ":" + AMN_Process_Value.trim() + Msg.getMsg(getCtx(), "NotAvailable");
        }

        String msgError = "";
        boolean okReceipts = AMNPayrollDeleteDocs.amnPayrollDeleteReceiptsGeneratePayrollArrays(
                getCtx(), amnperiod, p_AMN_Payroll_ID, ReceiptsGenList, ReceiptConcepts, msgError, get_TrxName());

        if (!okReceipts || !msgError.isEmpty()) {
            addLog("Error cargando estructuras para eliminar: " + msgError);
            return "Error";
        }

        IProcessUI processMonitor = Env.getProcessUI(getCtx());

        // 1. ELIMINACIÓN DE LÍNEAS (CON SUB-TRANSACTION POR BLOQUE DE SEGURIDAD)
        int noRecsLines = ReceiptConcepts.size();
        if (noRecsLines > 0) {
            for (int i = 0; i < noRecsLines; i++) {
                AMNReceiptLines line = ReceiptConcepts.get(i);
                
                int percent = (100 * (i + 1)) / noRecsLines; // Corrige división entera
                if (processMonitor != null) {
                    processMonitor.statusUpdate("Eliminando Líneas: " + (i + 1) + "/" + noRecsLines + " (" + percent + "%)");
                }

                MAMN_Payroll amnp = new MAMN_Payroll(getCtx(), line.getAMN_Payroll_ID(), get_TrxName());
                if (MAMN_Payroll.STATUS_Completed.equalsIgnoreCase(amnp.getDocStatus())) {
                    addLog("*** ADVERTENCIA: Documento completado ID=" + amnp.getAMN_Payroll_ID() + " ***");
                    break;
                }

                // Borrar línea en sub-transacción segura
                String trxLineName = Trx.createTrxName("DelLine_" + line.getAMN_Payroll_Detail_ID());
                Trx trxLine = Trx.get(trxLineName, true);
                try {
                    AMNPayrollDeleteDocs.amnPayrollDeleteInvoicesAllProcessOneLine(getCtx(), amnp, line.getAMN_Payroll_Detail_ID(), trxLineName);
                    trxLine.commit(true);
                } catch (Exception e) {
                    trxLine.rollback();
                    log.log(Level.SEVERE, "Error eliminando línea detail_id=" + line.getAMN_Payroll_Detail_ID(), e);
                } finally {
                    trxLine.close();
                }
            }
        }

        // 2. ELIMINACIÓN DE FACTURAS Y CABECERAS
        int noRecs = ReceiptsGenList.size();
        if (noRecs > 0) {
            for (int i = 0; i < noRecs; i++) {
                AMNReceipts rec = ReceiptsGenList.get(i);
                int payrollID = rec.getAMN_Payroll_ID();

                int percent = (100 * (i + 1)) / noRecs;
                if (processMonitor != null) {
                    processMonitor.statusUpdate("Eliminando Cabeceras: " + (i + 1) + "/" + noRecs + " (" + percent + "%)");
                }

                String trxHdrName = Trx.createTrxName("DelHdr_" + payrollID);
                Trx trxHdr = Trx.get(trxHdrName, true);

                try {
                    MAMN_Payroll amnp = new MAMN_Payroll(getCtx(), payrollID, trxHdrName);
                    if (!MAMN_Payroll.STATUS_Completed.equalsIgnoreCase(amnp.getDocStatus())) {
                        
                        // Eliminar facturas asociadas
                        List<MAMN_Payroll_Docs> amnpdocs = MAMN_Payroll_Docs.getByPayrollID(getCtx(), payrollID, trxHdrName);
                        for (MAMN_Payroll_Docs doc : amnpdocs) {
                            MInvoice invoice = new MInvoice(getCtx(), doc.getC_Invoice_ID(), trxHdrName);
                            if (invoice.get_ID() > 0 && !invoice.isPaid()) {
                                amnp.deleteInvoiceLines(getCtx(), invoice, amnp.getDocumentNo());
                                doc.deleteEx(true, trxHdrName);
                                invoice.deleteEx(true, trxHdrName);
                            }
                        }

                        // Eliminar cabecera de nómina
                        AMNPayrollDeleteDocs.amnPayrollDeleteInvoicesAllProcessOneHeader(getCtx(), amnp, trxHdrName);
                    }
                    trxHdr.commit(true);
                } catch (Exception e) {
                    trxHdr.rollback();
                    log.log(Level.SEVERE, "Error eliminando cabecera de nómina ID: " + payrollID, e);
                } finally {
                    trxHdr.close();
                }
            }
        }

        return "OK";
    }
}
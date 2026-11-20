package org.amerp.process.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.logging.Level;

import org.adempiere.util.IProcessUI;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class AMNHouseKeepingPayrollAssist extends SvrProcess {

    private int p_AD_Client_ID = 0;
    private int p_AD_Org_ID = 0;
    private Timestamp p_CutoffDate = null;
    private int p_BatchSize = 50000;
    private boolean p_RunVacuum = true;
    private boolean p_IsScheduled = false;

    @Override
    protected void prepare() {
        ProcessInfoParameter[] paras = getParameter();
        for (ProcessInfoParameter para : paras) {
            String name = para.getParameterName();
            if ("AD_Client_ID".equals(name)) {
                p_AD_Client_ID = para.getParameterAsInt();
            } else if ("AD_Org_ID".equals(name)) {
                p_AD_Org_ID = para.getParameterAsInt();
            } else if ("CutoffDate".equals(name) || "AMNDateAssist".equals(name) || "RefDateEnd".equals(name)) {
                p_CutoffDate = para.getParameterAsTimestamp();
            } else if ("BatchSize".equals(name)) {
                p_BatchSize = para.getParameterAsInt();
            } else if ("RunVacuum".equals(name)) {
                p_RunVacuum = para.getParameterAsBoolean();
            } else if ("IsScheduled".equals(name)) {
                p_IsScheduled = para.getParameterAsBoolean();
            } else {
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
            }
        }

        if (p_AD_Client_ID == 0) {
            p_AD_Client_ID = Env.getAD_Client_ID(getCtx());
        }
        if (p_BatchSize <= 0) {
            p_BatchSize = 50000;
        }
        if (p_CutoffDate == null) {
            // Valor por defecto de seguridad: 2026-09-01 00:00:00
            p_CutoffDate = Timestamp.valueOf(LocalDate.of(2026, 9, 1).atStartOfDay());
        }
    }

    @Override
    protected String doIt() throws Exception {
        IProcessUI processMonitor = p_IsScheduled ? null : Env.getProcessUI(getCtx());

        String orgFilterAssist = (p_AD_Org_ID > 0) ? " AND a.ad_org_id = ? " : "";
        String orgFilterRow    = (p_AD_Org_ID > 0) ? " AND r.ad_org_id = ? " : "";

        // 1. Borrar por lotes en adempiere.amn_payroll_assist
        String deleteAssistSql =
                "WITH batch AS ( "
                + "  SELECT a.amn_payroll_assist_id "
                + "  FROM adempiere.amn_payroll_assist a "
                + "  WHERE a.ad_client_id = ? "
                + orgFilterAssist
                + "    AND a.event_date < ? "
                + "  ORDER BY a.amn_payroll_assist_id "
                + "  LIMIT ? "
                + ") "
                + "DELETE FROM adempiere.amn_payroll_assist t "
                + "USING batch b "
                + "WHERE t.amn_payroll_assist_id = b.amn_payroll_assist_id";

        updateMonitor(processMonitor, "Iniciando limpieza de adempiere.amn_payroll_assist (< " + p_CutoffDate + ")...");
        long totalAssistDeleted = deleteInBatches(
                "amn_payroll_assist", deleteAssistSql,
                p_AD_Client_ID, p_AD_Org_ID, p_CutoffDate, p_BatchSize, processMonitor);

        // Asegurar índice sobre FK en amn_payroll_assist para acelerar el NOT EXISTS
        ensureAssistRowIndex(processMonitor);

        // 2. Borrar por lotes en adempiere.amn_payroll_assist_row (no referenciados en amn_payroll_assist)
        String deleteRowSql =
                "WITH batch AS ( "
                + "  SELECT r.amn_payroll_assist_row_id "
                + "  FROM adempiere.amn_payroll_assist_row r "
                + "  WHERE r.ad_client_id = ? "
                + orgFilterRow
                + "    AND r.amn_datetime < ? "
                + "    AND NOT EXISTS ( "
                + "      SELECT 1 "
                + "      FROM adempiere.amn_payroll_assist a "
                + "      WHERE a.ad_client_id = r.ad_client_id "
                + "        AND a.amn_payroll_assist_row_id IS NOT NULL "
                + "        AND a.amn_payroll_assist_row_id = r.amn_payroll_assist_row_id "
                + "    ) "
                + "  ORDER BY r.amn_payroll_assist_row_id "
                + "  LIMIT ? "
                + ") "
                + "DELETE FROM adempiere.amn_payroll_assist_row t "
                + "USING batch b "
                + "WHERE t.amn_payroll_assist_row_id = b.amn_payroll_assist_row_id";

        updateMonitor(processMonitor, "Iniciando limpieza de adempiere.amn_payroll_assist_row (< " + p_CutoffDate + ")...");
        long totalRowDeleted = deleteInBatches(
                "amn_payroll_assist_row", deleteRowSql,
                p_AD_Client_ID, p_AD_Org_ID, p_CutoffDate, p_BatchSize, processMonitor);

        // 3. VACUUM ANALYZE fuera de transacción (autoCommit = true)
        if (p_RunVacuum) {
            updateMonitor(processMonitor, "Ejecutando VACUUM (ANALYZE) en adempiere.amn_payroll_assist...");
            runVacuumAnalyze("adempiere.amn_payroll_assist");

            updateMonitor(processMonitor, "Ejecutando VACUUM (ANALYZE) en adempiere.amn_payroll_assist_row...");
            runVacuumAnalyze("adempiere.amn_payroll_assist_row");
        }

        String summary = String.format(
                "HouseKeeping completado (AMNDateAssist < %s) | amn_payroll_assist borrados: %d | amn_payroll_assist_row borrados: %d",
                p_CutoffDate, totalAssistDeleted, totalRowDeleted);

        updateMonitor(processMonitor, summary);

        if (!p_IsScheduled) {
            addLog(summary);
        }
        log.warning(summary);

        return Msg.getMsg(getCtx(), "ProcessOK") + " - " + summary;
    }

    private long deleteInBatches(
            String label,
            String deleteSql,
            int adClientId,
            int adOrgId,
            Timestamp cutoffDate,
            int batchSize,
            IProcessUI processMonitor) throws Exception {

        long totalDeleted = 0;
        int batchNumber = 0;
        long startTimeMs = System.currentTimeMillis();

        while (true) {
            batchNumber++;
            long batchStartMs = System.currentTimeMillis();
            updateMonitor(processMonitor, String.format("[%s] Ejecutando lote #%d (máx %d filas)...", label, batchNumber, batchSize));

            Trx trx = Trx.get(Trx.createTrxName("HK_" + label + "_" + batchNumber), true);
            int deleted;
            try {
                Object[] params = (adOrgId > 0)
                        ? new Object[]{adClientId, adOrgId, cutoffDate, batchSize}
                        : new Object[]{adClientId, cutoffDate, batchSize};
                deleted = DB.executeUpdateEx(deleteSql, params, trx.getTrxName());
                trx.commit();
            } catch (Exception e) {
                trx.rollback();
                log.log(Level.SEVERE, "Error en lote #" + batchNumber + " de " + label, e);
                throw e;
            } finally {
                trx.close();
            }

            if (deleted <= 0) {
                break;
            }

            totalDeleted += deleted;
            long batchElapsedMs = System.currentTimeMillis() - batchStartMs;
            long totalElapsedSec = Math.max(1L, (System.currentTimeMillis() - startTimeMs) / 1000L);
            long rowsPerSec = totalDeleted / totalElapsedSec;

            String msg = String.format(
                    "[%s] Lote #%d: -%d filas (%d ms) | Acumulado: %d | ~%d filas/s",
                    label, batchNumber, deleted, batchElapsedMs, totalDeleted, rowsPerSec);

            log.info(msg);
            updateMonitor(processMonitor, msg);

            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        String doneMsg = String.format("[%s] Finalizado: %d filas eliminadas en %d lote(s).",
                label, totalDeleted, Math.max(0, batchNumber - 1));
        log.info(doneMsg);
        updateMonitor(processMonitor, doneMsg);

        return totalDeleted;
    }

    private void ensureAssistRowIndex(IProcessUI processMonitor) {
        String checkSql =
                "SELECT COUNT(*) FROM pg_indexes "
                + "WHERE schemaname = 'adempiere' "
                + "  AND tablename = 'amn_payroll_assist' "
                + "  AND indexdef ILIKE '%(amn_payroll_assist_row_id)%'";
        int exists = DB.getSQLValue(null, checkSql);
        if (exists > 0) {
            return;
        }

        String createIdxSql =
                "CREATE INDEX IF NOT EXISTS idx_amn_payroll_assist_row_id "
                + "ON adempiere.amn_payroll_assist (amn_payroll_assist_row_id)";
        updateMonitor(processMonitor, "Creando índice idx_amn_payroll_assist_row_id para acelerar limpieza...");
        try (Connection conn = DB.getConnectionRW()) {
            boolean prevAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(true);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createIdxSql);
                    log.info("Índice creado: idx_amn_payroll_assist_row_id");
                }
            } finally {
                try {
                    conn.setAutoCommit(prevAutoCommit);
                } catch (SQLException ignored) {
                }
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "No se pudo crear idx_amn_payroll_assist_row_id automáticamente: " + e.getMessage(), e);
        }
    }

    private void updateMonitor(IProcessUI processMonitor, String message) {
        if (!p_IsScheduled && processMonitor != null) {
            processMonitor.statusUpdate(message);
        }
    }

    private void runVacuumAnalyze(String fullTableName) {
        String vacuumSql = "VACUUM (ANALYZE) " + fullTableName;
        try (Connection conn = DB.getConnectionRW()) {
            boolean prevAutoCommit = conn.getAutoCommit();
            try {
                conn.setAutoCommit(true);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(vacuumSql);
                    log.info("Ejecutado: " + vacuumSql);
                }
            } finally {
                try {
                    conn.setAutoCommit(prevAutoCommit);
                } catch (SQLException ignored) {
                }
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "No se pudo ejecutar " + vacuumSql + " desde el proceso: " + e.getMessage(), e);
        }
    }
}
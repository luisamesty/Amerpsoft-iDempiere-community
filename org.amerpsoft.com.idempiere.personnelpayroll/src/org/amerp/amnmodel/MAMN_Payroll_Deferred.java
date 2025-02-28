package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.adempiere.util.IProcessUI;
import org.amerp.amnutilities.LoanPeriods;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MAMN_Payroll_Deferred extends X_AMN_Payroll_Deferred {

    private static final long serialVersionUID = -5757997611959319285L;
    static CLogger log = CLogger.getCLogger(MAMN_Payroll_Deferred.class);
    private static CCache<Integer, MAMN_Payroll_Deferred> s_cache = new CCache<>(Table_Name, 10);

    public MAMN_Payroll_Deferred(Properties ctx, int AMN_Payroll_Deferred_ID, String trxName) {
        super(ctx, AMN_Payroll_Deferred_ID, trxName);
    }

    public MAMN_Payroll_Deferred(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Crea un nuevo registro en AMN_Payroll_Deferred
     */
    public boolean createAmnPayrollDeferred(Properties ctx, MAMN_Payroll amnpayroll, MAMN_Process amnprocessde,
                                            MAMN_Concept_Types_Proc amnconcepttypesCR, MAMN_Period amnperiod,
                                            LoanPeriods loanPeriodData, BigDecimal p_LoanAmount, String trxName) {
        if (trxName == null || trxName.isEmpty()) {
            trxName = get_TrxName();
        }

        try {
            MAMN_Employee amnemployee = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), trxName);
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            IProcessUI processMonitor = Env.getProcessUI(ctx);

            MAMN_Payroll_Deferred amnpayrolldeferred = new MAMN_Payroll_Deferred(ctx, 0, trxName);
            amnpayrolldeferred.setLine(loanPeriodData.getLoanCuotaNo()*10);
            amnpayrolldeferred.setAD_Client_ID(amnpayroll.getAD_Client_ID());
            amnpayrolldeferred.setAD_Org_ID(amnpayroll.getAD_Org_ID());
            amnpayrolldeferred.setAMN_Payroll_ID(amnpayroll.getAMN_Payroll_ID());
            amnpayrolldeferred.setAMN_Concept_Types_Proc_ID(amnconcepttypesCR.getAMN_Concept_Types_Proc_ID());
            amnpayrolldeferred.setAMN_Period_ID(amnperiod.getAMN_Period_ID());
            amnpayrolldeferred.setDueDate(amnperiod.getAMNDateEnd());
            amnpayrolldeferred.setAMN_Process_ID(amnprocessde.getAMN_Process_ID());
            amnpayrolldeferred.setAMN_Employee_ID(amnpayroll.getAMN_Employee_ID());
            amnpayrolldeferred.setValue(loanPeriodData.getPeriodValue() + " " + sdf.format(amnperiod.getAMNDateEnd()));
            amnpayrolldeferred.setName(amnemployee.getValue().trim() + " " + sdf.format(amnperiod.getAMNDateEnd()) +
                    " (" + decimalFormat.format(p_LoanAmount) + ") " + loanPeriodData.getPeriodValue().trim() +
                    " " + amnconcepttypesCR.getName());
            amnpayrolldeferred.setDescription(amnconcepttypesCR.getDescription());
            amnpayrolldeferred.setQtyValue(loanPeriodData.getCuotaAmount());
            amnpayrolldeferred.setAmountCalculated(loanPeriodData.getCuotaAmount());
            amnpayrolldeferred.setAmountDeducted(loanPeriodData.getCuotaAmount());
            amnpayrolldeferred.setAmountBalance(loanPeriodData.getBalanceAmount());

            amnpayrolldeferred.saveEx(trxName);

            if (processMonitor != null) {
                processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Payroll_Deferred_ID") + ": " +
                        amnpayrolldeferred.getValue() + "-" + amnpayrolldeferred.getName());
            }

            return true;
        } catch (Exception e) {
            log.severe("Error al crear AMN_Payroll_Deferred: " + e.getMessage());
            return false;
        }
    }

    /**
     * afterSave: Ejecuta cálculos después de guardar
     */
    @Override
    protected boolean afterSave(boolean p_newRecord, boolean p_success) {
        if (!p_success) {
            return false;
        }

        updatePayrollDeferredSums(getCtx(), getAMN_Payroll_ID(), get_TrxName());
        return super.afterSave(p_newRecord, p_success);
    }

    /**
     * Actualiza los valores acumulados en AMN_Payroll_Detail
     * 
     * @param ctx
     * @param AMN_Payroll_ID
     * @param trxName
     */
    public static void updatePayrollDeferredSums(Properties ctx, int AMN_Payroll_ID, String trxName) {
        String sql = "SELECT COALESCE(SUM(QtyValue), 0), " +
                     "COALESCE(SUM(AmountAllocated), 0), " +
                     "COALESCE(SUM(AmountDeducted), 0), " +
                     "COALESCE(SUM(AmountCalculated), 0) " +
                     "FROM AMN_Payroll_Deferred WHERE AMN_Payroll_ID = ?";

        try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
            pstmt.setInt(1, AMN_Payroll_ID); // ✅ Mover antes de executeQuery

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal totalQtyValue = rs.getBigDecimal(1);
                    BigDecimal totalAmountAllocated = rs.getBigDecimal(2);
                    BigDecimal totalAmountDeducted = rs.getBigDecimal(3);
                    BigDecimal totalAmountCalculated = rs.getBigDecimal(4);

                    MAMN_Payroll amnPayroll = new MAMN_Payroll(ctx, AMN_Payroll_ID, trxName);
                    amnPayroll.setAmountNetpaid(totalAmountAllocated.subtract(totalAmountDeducted)); // ✅ Corrección lógica
                    amnPayroll.setAmountAllocated(totalAmountAllocated);
                    amnPayroll.setAmountDeducted(totalAmountDeducted);
                    amnPayroll.setAmountCalculated(totalAmountCalculated);
                    amnPayroll.saveEx(trxName);
                }
            }
        } catch (Exception e) {
        	log.severe("Error al actualizar Payroll Deferred Sums: " + e.getMessage());
        }
    }


    /**
     * recalculateCumulativeAmountBalance
     * @param AMN_Payroll_ID
     * @param trxName
     */
    public static void recalculateCumulativeAmountBalance(Properties ctx, int AMN_Payroll_ID, String trxName) {
        String sql = "SELECT AMN_Payroll_Deferred_ID, AmountAllocated, AmountDeducted, duedate " +
                     "FROM AMN_Payroll_Deferred " +
                     "WHERE AMN_Payroll_ID = ? " +
                     "ORDER BY duedate ASC";  // Ordenar por fecha de vencimiento

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal cumulativeBalance = BigDecimal.ZERO;  // Inicia el saldo acumulado

        // UBICA EL SALDO INICIAL
        MAMN_Payroll amnpayroll = new MAMN_Payroll( ctx, AMN_Payroll_ID,  trxName);
        if (amnpayroll != null && amnpayroll.getAmountNetpaid().compareTo(BigDecimal.ZERO) > 0) {
        	// Initial Balance
        	cumulativeBalance =  amnpayroll.getAmountNetpaid();
	        try {
	            pstmt = DB.prepareStatement(sql, trxName);
	            pstmt.setInt(1, AMN_Payroll_ID);
	            rs = pstmt.executeQuery();
	
	            while (rs.next()) {
	                int deferredID = rs.getInt("AMN_Payroll_Deferred_ID");
	                BigDecimal amountAllocated = rs.getBigDecimal("AmountAllocated");
	                if (amountAllocated== null) 
	                	amountAllocated = BigDecimal.ZERO;
	                BigDecimal amountDeducted = rs.getBigDecimal("AmountDeducted");
	                if (amountDeducted== null) 
	                	amountDeducted = BigDecimal.ZERO;
	
	                // Calcular el nuevo balance acumulativo
	                BigDecimal newBalance = cumulativeBalance.add(amountAllocated).subtract(amountDeducted);
	
	                // Actualizar el saldo en la BD
	                String updateSQL = "UPDATE AMN_Payroll_Deferred SET AmountBalance = ? WHERE AMN_Payroll_Deferred_ID = ?";
	                DB.executeUpdate(updateSQL, new Object[]{newBalance, deferredID}, false, trxName);
	
	                // Actualizar el saldo acumulado para la siguiente línea
	                cumulativeBalance = newBalance;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            DB.close(rs, pstmt);
	        }
        }
    }
}

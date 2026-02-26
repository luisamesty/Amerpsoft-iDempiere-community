package org.amerp.reports.xlsx.util;

import org.compiere.util.DB;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Set;

import org.compiere.model.X_C_ElementValue;

public final class AccountUtils {

    private AccountUtils() {
        // Clase de utilidad
    }

    // ===============================================================
    // ===  (IncomeSummary_Acct) ===
    // ===============================================================
    /**
     * Obtiene el C_ElementValue_ID de la Cuenta de Resumen de Ingresos del Esquema Contable.
     * Esta cuenta es IsSummary='Y' y NO es apta para el campo fAccount.
     */
    public static int getIncomeSummaryAccountID(int C_AcctSchema_ID, int AD_Client_ID) {
        if (C_AcctSchema_ID <= 0) {
            return 0;
        }

        String sql = "SELECT cev.C_ElementValue_ID "
                   + "FROM C_AcctSchema_GL accsch "
                   + "LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = accsch.IncomeSummary_Acct "
                   + "LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID) "
                   + "WHERE accsch.AD_Client_ID = ? AND accsch.C_acctSchema_ID = ?";
        
        // Usamos DB.getSQLValue para ejecutar la consulta
        return DB.getSQLValue(null, sql, AD_Client_ID, C_AcctSchema_ID);
    }

    // ===============================================================
    // ===  CUENTA DE DETALLE VÁLIDA PARA fAccount (Default Field) ===
    // ===============================================================
    /**
     * Obtiene el C_ElementValue_ID de la primera Cuenta de Detalle (IsSummary='N') activa 
     * que es válida para el Esquema Contable. Este es el valor que DEBE usarse en fAccount.
     */
    public static int getDefaultDetailAccount(int C_AcctSchema_ID) {
        if (C_AcctSchema_ID <= 0) {
            return 0;
        }

        String sql = "SELECT ev.C_ElementValue_ID "
                   + "FROM C_ElementValue ev "
                   + "INNER JOIN C_AcctSchema_Element ase ON (ev.C_Element_ID = ase.C_Element_ID) "
                   + "WHERE ev.IsSummary='N' AND ev.IsActive='Y' AND ase.C_AcctSchema_ID = ? "
                   + "ORDER BY ev.Value"; // No usamos LIMIT 1, confiando en getSQLValue
                   
        return DB.getSQLValue(null, sql, C_AcctSchema_ID);
    }
    
    /**
     * getFiscalYearStart
     * @param ctx
     * @param cYearId
     * @param trxName
     * @return
     */
    public static Timestamp getFiscalYearStart(Properties ctx, int cYearId, String trxName) {

        return DB.getSQLValueTS(
            trxName,
            "SELECT MIN(StartDate) FROM C_Period WHERE C_Year_ID=?",
            cYearId
        );
    }

    /**
     * getFiscalYearEnd
     * @param ctx
     * @param cYearId
     * @param trxName
     * @return
     */
    public static Timestamp getFiscalYearEnd(Properties ctx, int cYearId, String trxName) {

        return DB.getSQLValueTS(
            trxName,
            "SELECT MAX(EndDate) FROM C_Period WHERE C_Year_ID=?",
            cYearId
        );
    }

    /**
     * invertSignTypes
     * Tipo a invertir el signo
     */
    private static final Set<String> invertSignTypes = Set.of(
    		X_C_ElementValue.ACCOUNTTYPE_Liability,
    		X_C_ElementValue.ACCOUNTTYPE_OwnerSEquity,
    		X_C_ElementValue.ACCOUNTTYPE_Revenue);

    /**
     * applyPositiveBalance
     * Aplica Lógica de valor positivo segun tipo de cuenta
     * @param accountType
     * @param positiveBalance
     * @param value
     * @return
     */
    public static BigDecimal applyPositiveBalance(String accountType, boolean positiveBalance, BigDecimal value) {
        if (value == null) return null;
        if (!positiveBalance) return value;
        return invertSignTypes.contains(accountType) ? value.negate() : value;
    }
}
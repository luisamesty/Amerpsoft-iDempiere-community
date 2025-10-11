package org.amerp.reports.xlsx.constants;

import java.util.Properties;

import org.compiere.model.MReference;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.CLogger;
import java.util.logging.Level;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class FinancialReportConstants {

    private FinancialReportConstants() {} // Evitar instanciación

    private static final CLogger log = CLogger.getCLogger(FinancialReportConstants.class);
    
    // ID de la Referencia
    public static final int AD_REFERENCE_REPORT_TYPE = 1000152;
    public static final String AD_REFERENCE_REPORT_TYPE_UU = "0b14e05d-d39e-4757-a410-154f73b261d1";
    // Claves de la Lista de Tipos de Reporte
    // Clave: Trial Balance One Period (TRB)
    public static final String REPORT_TYPE_TRIAL_BALANCE_ONE_PERIOD = "TRB";

    // Clave: Trial Balance between two dates (TRD)
    public static final String REPORT_TYPE_TRIAL_BALANCE_TWO_DATES = "TRD";

    // Clave: State Financial Balance (BAL)
    public static final String REPORT_TYPE_STATE_FINANCIAL_BALANCE = "BAL";

    // Clave: State Financial Integral Results (GOP)
    public static final String REPORT_TYPE_STATE_FINANCIAL_INTEGRAL_RESULTS = "GOP";

    // Clave: Analitic Financial State (ANB)
    public static final String REPORT_TYPE_ANALITIC_FINANCIAL_STATE = "ANB";
    
    // Clave: Income Result Accout for State Finanacial Balanace.
    public static final String IncomeSummary_Acct_Element_Name = "IncomeSummary_Acct";
    
 // ===============================================================
    // === 2. Métodos de Utilidad para obtener Nombre y Descripción ===
    // ===============================================================

    /**
     * Obtiene el Nombre (Name) de la Referencia (AD_Reference).
     * @param ctx Contexto de la sesión.
     * @return El nombre de la referencia o una cadena vacía si falla.
     */
    public static String getReferenceName(Properties ctx) {
        return getReferenceProperty(ctx, "Name");
    }

    /**
     * Obtiene la Descripción (Description) de la Referencia (AD_Reference).
     * @param ctx Contexto de la sesión.
     * @return La descripción de la referencia o una cadena vacía si falla.
     */
    public static String getReferenceDescription(Properties ctx) {
        return getReferenceProperty(ctx, "Description");
    }
    
    /**
     * Método auxiliar genérico para obtener una propiedad traducida de la tabla AD_Reference.
     * @param ctx Contexto de la sesión.
     * @param columnName Nombre de la columna a obtener (Name o Description).
     * @return El valor traducido de la columna o una cadena vacía si falla.
     */
    private static String getReferenceProperty(Properties ctx, String columnName) {
        String value = "";
        final int referenceID = FinancialReportConstants.AD_REFERENCE_REPORT_TYPE; 
        
        // Obtener el lenguaje actual del contexto para la traducción
        String AD_Language = Env.getAD_Language(ctx); 

        try {
            // Obtenemos el objeto modelo MReference. MReference.get(ctx, ID) ya maneja la traducción del Name.
            MReference ref = MReference.get(ctx, referenceID);
            
            if (ref != null) {
                if ("Name".equals(columnName)) {
                    // MReference.getName() es el getter estándar y maneja la traducción del nombre
                    value = ref.getName(); 
                    
                } else if ("Description".equals(columnName)) {
                    // Para obtener la Description traducida, consultamos directamente AD_Reference_Trl.
                    
                    String sql = "SELECT Description "
                               + "FROM AD_Reference_Trl "
                               + "WHERE AD_Reference_ID = ? "
                               + "AND AD_Language = ?";
                               
                    // Usamos DB.getSQLValueString para obtener la descripción traducida
                    String translatedValue = DB.getSQLValueString(null, sql, referenceID, AD_Language);
                    
                    if (translatedValue != null) {
                        value = translatedValue;
                    } else {
                        // Si no hay traducción en la tabla, obtenemos el valor base (AD_Reference.Description)
                        String baseSql = "SELECT Description FROM AD_Reference WHERE AD_Reference_ID = ?";
                        value = DB.getSQLValueString(null, baseSql, referenceID);
                    }
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Fallo al obtener la propiedad '" + columnName + 
                    "' traducida de la Referencia ID " + referenceID, e);
        }
        // Aseguramos que se devuelve una cadena no nula
        return value != null ? value : "";
    }    
    
    // ===============================================================
    // === MÉTODOS DE UTILIDAD PARA OBTENER TRADUCCIONES DE LA LISTA ===
    // ===============================================================

    /**
     * Obtiene el Nombre traducido de una entrada específica en la lista de Reportes.
     * @param ctx Contexto de la sesión.
     * @param reportType Clave del reporte (ej: "TRB", "BAL").
     * @return El nombre traducido o la clave si no se encuentra.
     */
    public static String getReportTypeName(Properties ctx, String reportType) {
        return getTranslatedProperty(ctx, "Name", reportType);
    }

    /**
     * Obtiene la Descripción traducida de una entrada específica en la lista de Reportes.
     * @param ctx Contexto de la sesión.
     * @param reportType Clave del reporte (ej: "TRB", "BAL").
     * @return La descripción traducida o una cadena vacía si no se encuentra.
     */
    public static String getReportTypeDescription(Properties ctx, String reportType) {
        return getTranslatedProperty(ctx, "Description", reportType);
    }
    
    /**
     * Método auxiliar genérico para obtener un campo traducido de AD_Ref_List_Trl.
     */
    private static String getTranslatedProperty(Properties ctx, String columnName, String reportType) {
        String translatedValue = "";
        String AD_Language = Env.getAD_Language(ctx); 
        
        // Query para obtener el valor traducido (COALESCE usa el valor base si no hay traducción)
        String sql = 
            "SELECT COALESCE(trl." + columnName + ", list." + columnName + ") " +
            "FROM AD_Ref_List list " +
            "LEFT JOIN AD_Ref_List_Trl trl ON (list.AD_Ref_List_ID = trl.AD_Ref_List_ID AND trl.AD_Language = ?) " +
            "WHERE list.AD_Reference_ID = ? AND list.Value = ?";

        try (PreparedStatement pstmt = DB.prepareStatement(sql, null)) {
            // Parámetros: AD_Language, AD_Reference_ID, Clave (Value)
            pstmt.setString(1, AD_Language);
            pstmt.setInt(2, AD_REFERENCE_REPORT_TYPE);
            pstmt.setString(3, reportType);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    translatedValue = rs.getString(1); // El primer resultado de COALESCE
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Fallo al obtener la propiedad '" + columnName + 
                    "' traducida para el ReportType: " + reportType, e);
        }
        
        // Devolvemos el valor o la clave (Name) / cadena vacía (Description) si es nulo
        if (translatedValue == null) {
             return "Name".equals(columnName) ? reportType : "";
        }
        return translatedValue;
    }
}
package org.amerp.reports.xlsx.constants;

import java.util.Properties;

import org.compiere.model.MReference;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.CLogger;
import java.util.logging.Level;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.compiere.model.MRefList;
import org.compiere.util.ValueNamePair;

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
    
    // Clave: Account Elements (ACE)
    public static final String REPORT_TYPE_ACCOUNT_ELEMENTS = "ACE";
    
    // Clave: Income Result Accout for State Finanacial Balanace.
    public static final String IncomeSummary_Acct_Element_Name = "IncomeSummary_Acct";
    
    // ==================================================================
    // === Métodos de Utilidad para obtener Nombre Descripción y Help===
    // =================================================================

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
     * Obtiene  Help de la Referencia (AD_Reference).
     * @param ctx Contexto de la sesión.
     * @return El Help de la referencia o una cadena vacía si falla.
     */
    public static String getReferenceHelp(Properties ctx) {
        return getReferenceProperty(ctx, "Help");
    }
    
    /**
     * Obtiene el ToolTip (Sugerencia) de la Referencia, devolviendo el primer valor 
     * no vacío encontrado en el orden: Help, Description, Name.
     * @param ctx Contexto de la sesión.
     * @return El Help, Descripción, o Nombre de la referencia, o una cadena vacía si todos son nulos/vacíos.
     */
    public static String getReferenceToolTip(Properties ctx) {
        String toolTip = "";

        // 1. Intentar obtener Help (Prioridad Alta)
        toolTip = getReferenceHelp(ctx);
        if (toolTip != null && toolTip.length() > 0) {
            return toolTip;
        }

        // 2. Intentar obtener Description (Prioridad Media)
        toolTip = getReferenceDescription(ctx);
        if (toolTip != null && toolTip.length() > 0) {
            return toolTip;
        }

        // 3. Intentar obtener Name (Prioridad Baja/Último Recurso)
        toolTip = getReferenceName(ctx);
        if (toolTip != null && toolTip.length() > 0) {
            return toolTip;
        }

        // Si todos fallaron, devuelve una cadena vacía
        return "";
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
            // Obtenemos el objeto modelo MReference.
            MReference ref = MReference.get(ctx, referenceID);
            
            if (ref != null) {
                
                if ("Name".equals(columnName)) {
                    // Name lo obtiene directamente MReference
                    value = ref.getName(); 
                    
                } else if ("Description".equals(columnName) || "Help".equals(columnName)) {
                    // === LÓGICA PARA OBTENER DESCRIPTION O HELP TRADUCIDOS ===
                    
                    // 1. Intentar obtener el valor traducido (Description o Help)
                    String sql = "SELECT " + columnName + " " // Usamos columnName dinámicamente
                               + "FROM AD_Reference_Trl "
                               + "WHERE AD_Reference_ID = ? "
                               + "AND AD_Language = ?";
                               
                    String translatedValue = DB.getSQLValueString(null, sql, referenceID, AD_Language);
                    
                    if (translatedValue != null) {
                        value = translatedValue;
                    } else {
                        // 2. Si no hay traducción, obtener el valor base (AD_Reference)
                        String baseSql = "SELECT " + columnName + " FROM AD_Reference WHERE AD_Reference_ID = ?";
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
    
    private static final int ACCOUNT_TYPE_REFERENCE_ID = 117;
    
    /**
     * Obtiene la lista estática de Tipos de Cuenta (AD_Reference_ID=117)
     * como una lista de ValueNamePair.
     * * @param ctx El contexto (Properties) que define el idioma de la traducción.
     * @return Una lista de ValueNamePair, donde Value es el código (A, L, O, R, E) 
     * y Name es la clave de traducción (Ej: Asset).
     */
    public static ValueNamePair[]  getAccountTypeReferenceList(Properties ctx) {
    	
    	ValueNamePair[] accountTypesArray = null;
    	accountTypesArray = MRefList.getList(Env.getCtx(), ACCOUNT_TYPE_REFERENCE_ID, false);
    	return accountTypesArray;
    }
    
    /**
     * Busca el código del Tipo de Cuenta en la lista y devuelve su nombre traducido.
     * * @param ctx El contexto que define el idioma de la traducción.
     * @param accountTypeCode El código del Tipo de Cuenta (Ej: "A", "L", "E").
     * @return El nombre traducido (Ej: "Activo"), o una cadena vacía si no se encuentra.
     */
    public static String getAccountTypeName(Properties ctx, String accountTypeCode) {
        if (accountTypeCode == null || accountTypeCode.trim().isEmpty()) {
            return "";
        }
        
        ValueNamePair[] accountTypesArray = getAccountTypeReferenceList(ctx);
        for (ValueNamePair type : accountTypesArray) {

            if (accountTypeCode.equalsIgnoreCase(type.getValue())) {
                String claveMensaje = type.getName();
                String nombreTraducido = Msg.getMsg(ctx, claveMensaje);
                return nombreTraducido;
            }
        }
        return  Msg.translate(ctx,"account.type")+" "+Msg.translate(ctx,"invalid");
    }
    
}
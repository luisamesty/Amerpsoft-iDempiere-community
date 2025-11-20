package org.amerp.reports.xlsx.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Utilidades de traducción (nombre/description/help) para AD_Element.
 * Usa PO (X_AD_Element_Trl) cuando está disponible; si no, hace fallback a SQL.
 *
 * - Requiere: org.compiere.model.X_AD_Element_Trl si existe (generado por el model generator).
 * - Si no existe, se ejecuta una consulta SQL a AD_Element_Trl.
 *
 * @author Luis Amesty (adaptado)
 */
public class MsgUtils {

    /**
     * Obtiene el nombre traducido (Name) del elemento.
     */
    public static String getElementName(int adElementId, String language) {
        if (adElementId <= 0) return "";
        // 1) Try using generated PO X_AD_Element_Trl via Query (preferred)
        try {
            // Reflection-free Query approach that expects X_AD_Element_Trl to be on classpath
            // The Query will return a map-like PO (X_AD_Element_Trl) if exists.
            Object trl = tryGetTranslationPO(adElementId, language);
            if (trl != null) {
                // We do reflection to read the Name property to avoid hard compile dependency
                Object val = getProperty(trl, "getName");
                if (val instanceof String && ((String) val).length() > 0) return (String) val;
            }
        } catch (Throwable t) {
            // ignore and fallback to SQL
        }

        // 2) Fallback: direct SQL read (safe)
        String sql = "SELECT COALESCE(trl.Name, e.Name) AS Name " +
                     "FROM AD_Element e " +
                     "LEFT JOIN AD_Element_Trl trl ON (e.AD_Element_ID = trl.AD_Element_ID AND trl.AD_Language = ?) " +
                     "WHERE e.AD_Element_ID = ?";
        return getTranslatedValueSQL(sql, adElementId, language, "Name");
    }

    /**
     * Obtiene la description traducida.
     */
    public static String getElementDescription(int adElementId, String language) {
        if (adElementId <= 0) return "";
        try {
            Object trl = tryGetTranslationPO(adElementId, language);
            if (trl != null) {
                Object val = getProperty(trl, "getDescription");
                if (val instanceof String && ((String) val).length() > 0) return (String) val;
            }
        } catch (Throwable t) {
            // ignore and fallback
        }

        String sql = "SELECT COALESCE(trl.Description, e.Description) AS Description " +
                     "FROM AD_Element e " +
                     "LEFT JOIN AD_Element_Trl trl ON (e.AD_Element_ID = trl.AD_Element_ID AND trl.AD_Language = ?) " +
                     "WHERE e.AD_Element_ID = ?";
        return getTranslatedValueSQL(sql, adElementId, language, "Description");
    }

    /**
     * Obtiene el help traducido.
     */
    public static String getElementHelp(int adElementId, String language) {
        if (adElementId <= 0) return "";
        try {
            Object trl = tryGetTranslationPO(adElementId, language);
            if (trl != null) {
                Object val = getProperty(trl, "getHelp");
                if (val instanceof String && ((String) val).length() > 0) return (String) val;
            }
        } catch (Throwable t) {
            // ignore and fallback
        }

        String sql = "SELECT COALESCE(trl.Help, e.Help) AS Help " +
                     "FROM AD_Element e " +
                     "LEFT JOIN AD_Element_Trl trl ON (e.AD_Element_ID = trl.AD_Element_ID AND trl.AD_Language = ?) " +
                     "WHERE e.AD_Element_ID = ?";
        return getTranslatedValueSQL(sql, adElementId, language, "Help");
    }

    // -------------------- Helpers --------------------

    /** Usa SQL directo y devuelve la columna pedida (fallback). */
    private static String getTranslatedValueSQL(String sql, int adElementId, String language, String columnName) {
        String value = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql, null);
            pstmt.setString(1, language);
            pstmt.setInt(2, adElementId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                value = rs.getString(columnName);
            }
        } catch (SQLException e) {
            Env.getCtx().put("MsgUtilsError", e.getMessage()); // log minimal, evita dependencia logger
        } finally {
            DB.close(rs, pstmt);
        }
        return value != null ? value : "";
    }

    /**
     * Intentar obtener el PO de traducción usando Query (devuelve X_AD_Element_Trl si existe).
     * Retorna null si no existe la clase PO o no hay fila.
     */
    private static Object tryGetTranslationPO(int adElementId, String language) {
        try {
            // Query busca por AD_Element_ID y AD_Language
            // Importante: esta clase Query es parte del core y usará la clase X_ si existe.
            // Nota: usamos Object para evitar dependencia de clase concreta en la compilación.
            Query q = new Query(Env.getCtx(), "AD_Element_Trl", "AD_Element_ID=? AND AD_Language=?", null);
            Object po = q.setParameters(adElementId, language).first();
            return po; // puede ser instancia de X_AD_Element_Trl si clase existe
        } catch (Throwable t) {
            // Si Query o la clase X_ no están disponibles devuelve null
            return null;
        }
    }

    /**
     * Invoca por reflexión el método getter indicado en el PO.
     * Ej: getProperty(trl, "getName") devolverá trl.getName() si existe.
     */
    private static Object getProperty(Object po, String getterName) {
        if (po == null) return null;
        try {
            return po.getClass().getMethod(getterName).invoke(po);
        } catch (Exception e) {
            return null;
        }
    }

    // ---------- Sobrecargas para idioma actual ----------

    public static String getElementName(int adElementId) {
        return getElementName(adElementId, Env.getAD_Language(Env.getCtx()));
    }

    public static String getElementDescription(int adElementId) {
        return getElementDescription(adElementId, Env.getAD_Language(Env.getCtx()));
    }

    public static String getElementHelp(int adElementId) {
        return getElementHelp(adElementId, Env.getAD_Language(Env.getCtx()));
    }
    
	 // Insertar esta nueva función helper:
	 // -------------------- Nuevo Helper --------------------
	     
	 /**
	  * Obtiene el AD_Element_ID a partir del ColumnName (Valor del Elemento).
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  * @return El AD_Element_ID o 0 si no se encuentra.
	  */
	 private static int getAD_Element_ID(String columnName) {
	     if (columnName == null || columnName.isEmpty()) {
	         return 0;
	     }
	     String sql = "SELECT AD_Element_ID FROM AD_Element WHERE ColumnName = ?";
	     // Usamos getSQLValue para buscar el ID de la tabla
	     Integer id = DB.getSQLValue(null, sql, columnName);
	     return id != null ? id.intValue() : 0;
	 }
	
	 // -------------------- Sobrecargas por Element Value --------------------
	
	 /**
	  * Obtiene el nombre traducido (Name) del elemento usando el valor de la columna.
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  */
	 public static String getElementName(String columnName, String language) {
	     int adElementId = getAD_Element_ID(columnName);
	     return getElementName(adElementId, language);
	 }
	
	 /**
	  * Obtiene la description traducida usando el valor de la columna.
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  */
	 public static String getElementDescription(String columnName, String language) {
	     int adElementId = getAD_Element_ID(columnName);
	     return getElementDescription(adElementId, language);
	 }
	
	 /**
	  * Obtiene el help traducido usando el valor de la columna.
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  */
	 public static String getElementHelp(String columnName, String language) {
	     int adElementId = getAD_Element_ID(columnName);
	     return getElementHelp(adElementId, language);
	 }
	
	
	 // -------------------- Sobrecargas por Element Value (Idioma Actual) --------------------
	
	 /**
	  * Obtiene el nombre traducido (Name) del elemento usando el valor de la columna (Idioma actual).
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  */
	 public static String getElementName(String columnName) {
	     return getElementName(columnName, Env.getAD_Language(Env.getCtx()));
	 }
	
	 /**
	  * Obtiene la description traducida usando el valor de la columna (Idioma actual).
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  */
	 public static String getElementDescription(String columnName) {
	     return getElementDescription(columnName, Env.getAD_Language(Env.getCtx()));
	 }
	
	 /**
	  * Obtiene el help traducido usando el valor de la columna (Idioma actual).
	  * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
	  */
	 public static String getElementHelp(String columnName) {
	     return getElementHelp(columnName, Env.getAD_Language(Env.getCtx()));
	 }
	 
	/**
	* getElementFullDescription:
    * Obtiene una descripción completa del elemento concatenando Nombre, Description y Help,
    * separados por saltos de línea, e ignorando campos nulos/vacíos.
    * @param columnName El valor de la columna (e.g., 'AD_Client_ID').
    * @return String con la descripción completa.
    */
    public static String getElementFullDescription(String columnName) {
        // Usamos el idioma actual de la sesión, aprovechando las sobrecargas existentes.
        
        String name = getElementName(columnName);
        String description = getElementDescription(columnName);
        String help = getElementHelp(columnName);
        
        StringBuilder fullDescription = new StringBuilder();

        // 1. Agregar el Nombre (Value)
        if (name != null && !name.isEmpty()) {
            fullDescription.append(name);
        }

        // 2. Agregar la Descripción
        if (description != null && !description.isEmpty()) {
            // Si ya hay contenido, agregar un salto de línea
            if (fullDescription.length() > 0) {
                fullDescription.append("\n");
            }
            fullDescription.append(description);
        }

        // 3. Agregar el Help (Ayuda)
        if (help != null && !help.isEmpty()) {
            // Si ya hay contenido, agregar un salto de línea
            if (fullDescription.length() > 0) {
                fullDescription.append("\n");
            }
            fullDescription.append(help);
        }
        
        return fullDescription.toString();
    }
}

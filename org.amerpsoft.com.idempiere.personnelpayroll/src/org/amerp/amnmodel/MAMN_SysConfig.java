package org.amerp.amnmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MSysConfig;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

/**
 * Clase para gestionar valores de SysConfig a nivel de cliente.
 */
public class MAMN_SysConfig extends MSysConfig {

    private static final long serialVersionUID = 1L;

	public static String CLIENT_ACCOUNTING_QUEUE = "Q";
	public static String CLIENT_ACCOUNTING_IMMEDIATE = "I";
	
    public MAMN_SysConfig(Properties ctx, int AD_SysConfig_ID, String trxName) {
        super(ctx, AD_SysConfig_ID, trxName);
    }

    public MAMN_SysConfig(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Retorna el valor de configuración para un cliente.
     */
    public static String getValue(String name, String defaultValue, int AD_Client_ID, String trxName) {
        String sql = "SELECT Value FROM AD_SysConfig WHERE Name = ? AND AD_Client_ID = ?";
        String value = DB.getSQLValueStringEx(trxName, sql, name, AD_Client_ID);
        return (value != null) ? value : defaultValue;
    }


    /**
     * Crea o actualiza un valor de configuración para un cliente.
     */
    public static void setValue(String name, String value, int AD_Client_ID, String trxName) {
        final String sql = "SELECT AD_SysConfig_ID FROM AD_SysConfig WHERE Name = ? AND AD_Client_ID = ?";
        int id = DB.getSQLValueEx(trxName, sql, name, AD_Client_ID);

        MAMN_SysConfig config;

        if (id > 0) {
            // Actualizar existente
            config = new MAMN_SysConfig(Env.getCtx(), id, trxName);
        } else {
            // Crear nuevo
            config = new MAMN_SysConfig(Env.getCtx(), 0, trxName);
            config.setName(name);
            config.setAD_Client_ID(AD_Client_ID);
        }

        config.setValue(value);
        config.saveEx(trxName);
    }
}

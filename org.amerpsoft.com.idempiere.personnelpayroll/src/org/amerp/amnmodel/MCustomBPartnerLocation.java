package org.amerp.amnmodel;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class MCustomBPartnerLocation extends X_C_BPartner_Location {

    private static final long serialVersionUID = 1L;
    
	CLogger log = CLogger.getCLogger(MCustomBPartnerLocation.class);
	
	public MCustomBPartnerLocation(Properties ctx, int C_BPartner_Location_ID, String trxName) {
        super(ctx, C_BPartner_Location_ID, trxName);
    }

    public MCustomBPartnerLocation(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        // Solo aplica si el BP es un trabajador
        if (!isBPEmployee(getC_BPartner_ID())) {
            return true;
        }

        // Definimos los métodos y campos a validar
        String[][] flags = {
            {"isBillTo", "IsBillTo", Msg.getMsg(Env.getCtx(), "BPartnerBillToAddressUnique")},
            {"isPayFrom", "IsPayFrom", Msg.getMsg(Env.getCtx(), "BPartnerPayToAddressUnique")},
            {"isShipTo", "IsShipTo", Msg.getMsg(Env.getCtx(), "BPartnerShipToAddressUnique")},
            {"isRemitTo", "IsRemitTo", Msg.getMsg(Env.getCtx(), "BPartnerRemitToAddressUnique")}
        };

        Integer locID = getC_BPartner_Location_ID();
        int bpID = getC_BPartner_ID();

        for (String[] flag : flags) {
            String methodName = flag[0];
            String columnName = flag[1];
            String errorMessage = flag[2];

            try {
                // Invocar método como isBillTo(), isPayFrom(), etc.
                Method method = this.getClass().getMethod(methodName);
                boolean isSet = (boolean) method.invoke(this);

                if (!isSet)
                    continue;

                // Consultar si ya existe otra dirección con el mismo flag
                int count = DB.getSQLValue(get_TrxName(),
                    "SELECT COUNT(*) FROM C_BPartner_Location " +
                    "WHERE C_BPartner_ID=? AND " + columnName + "='Y' AND C_BPartner_Location_ID<>?",
                    bpID, locID);

                if (count > 0) {
                    log.saveError("Error", errorMessage);
                    return false;
                }
            } catch (Exception e) {
                log.severe("Error al validar flag " + columnName + ": " + e.getMessage());
                return false;
            }
        }

        return true;
    }


    private boolean isBPEmployee(int C_BPartner_ID) {
        // 1. Verificar si eltercero está marcado como empleado
        String sqlIsEmployee = "SELECT IsEmployee FROM C_BPartner WHERE C_BPartner_ID=?";
        if (!"Y".equals(DB.getSQLValueStringEx(null, sqlIsEmployee, C_BPartner_ID))) {
            return false;
        }

        // 2. Verificar si tiene un registro en AMN_Employee
        String sqlInEmployeeTable = "SELECT COUNT(*) FROM AMN_Employee WHERE C_BPartner_ID=?";
        int count = DB.getSQLValue(null, sqlInEmployeeTable, C_BPartner_ID);

        return count > 0;
    }

}

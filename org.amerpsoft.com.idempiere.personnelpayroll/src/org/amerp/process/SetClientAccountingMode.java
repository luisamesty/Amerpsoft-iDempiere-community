package org.amerp.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.sql.Timestamp;

public class SetClientAccountingMode extends SvrProcess {

    private int p_AD_Client_ID = 0;
    private String p_ClientAccountingMode = null;

    @Override
    protected void prepare() {
        ProcessInfoParameter[] params = getParameter();
        for (ProcessInfoParameter param : params) {
            if (param.getParameterName() == null)
                continue;

            switch (param.getParameterName()) {
                case "AD_Client_ID":
                    p_AD_Client_ID = param.getParameterAsInt();
                    break;

                case "ClientAccountingMode":
                    p_ClientAccountingMode = (String) param.getParameter();
                    break;

                default:
                    log.warning("Parámetro no procesado: " + param.getParameterName());
                    break;
            }
        }

    }

    @Override
    protected String doIt() throws Exception {
        if (p_ClientAccountingMode == null || !p_ClientAccountingMode.matches("[QID]")) {
            throw new IllegalArgumentException("Valor inválido para ClientAccounting_Mode: " + p_ClientAccountingMode);
        }

        String configKey = "CLIENT_ACCOUNTING";
        int updated = DB.executeUpdateEx(
            "UPDATE AD_SysConfig SET Value=? WHERE Name=? AND AD_Client_ID=?",
            new Object[]{p_ClientAccountingMode, configKey, p_AD_Client_ID},
            get_TrxName()
        );

        if (updated == 0) {
            int newID = DB.getNextID(p_AD_Client_ID, "AD_SysConfig", get_TrxName());
            int userId = Env.getAD_User_ID(getCtx());
            Timestamp now = new Timestamp(System.currentTimeMillis());

            DB.executeUpdateEx(
                "INSERT INTO AD_SysConfig " +
                "(AD_SysConfig_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy, Name, Value, Description) " +
                "VALUES (?, ?, 0, 'Y', ?, ?, ?, ?, ?, ?, ?)",
                new Object[]{
                    newID, p_AD_Client_ID,
                    now, userId, now, userId,
                    configKey, p_ClientAccountingMode,
                    "Set by process: " + getClass().getSimpleName()
                },
                get_TrxName()
            );
        }

        return "CLIENT_ACCOUNTING = '" + p_ClientAccountingMode + "' configurado para el cliente ID: " + p_AD_Client_ID;
    }
}

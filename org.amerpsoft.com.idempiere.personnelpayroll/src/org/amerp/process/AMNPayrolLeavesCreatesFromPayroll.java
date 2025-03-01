package org.amerp.process;

import org.compiere.process.SvrProcess;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.session.SessionManager;
import org.amerp.amnmodel.MAMN_Leaves;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Process;
import org.compiere.model.MMessage;
import org.compiere.model.MNote;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class AMNPayrolLeavesCreatesFromPayroll extends SvrProcess {

	private int p_AMN_Payroll_ID=0;
	private Timestamp p_InvDateIni;
	private BigDecimal p_DaysTo = BigDecimal.ZERO;

	@Override
	protected void prepare() {
	    // TODO Auto-generated method stub
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID =  para.getParameterAsInt();
			else if (paraName.equals("InvDateIni"))
				p_InvDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("DaysTo"))
				p_DaysTo = para.getParameterAsBigDecimal();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
	}

	@Override
	protected String doIt() throws Exception {
		
		Properties ctx = getCtx();
		String trxName = get_TrxName();
		String retMsg ="";
		
		if (p_AMN_Payroll_ID <= 0) {
			return "@Error@ " + Msg.parseTranslation(ctx, "No valid Payroll ID provided");
        }
		// Obtener la tabla AMN_Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, trxName);
		// Obtener la tabla AMN_Process
		MAMN_Process amnprocess = new MAMN_Process(ctx, amnpayroll.getAMN_Process_ID(), trxName);
		// Verifica si hay registros para ese PayrollID
		List<MAMN_Leaves> leaves = MAMN_Leaves.findAllByPayrollID(p_AMN_Payroll_ID);
		if (leaves.isEmpty()) {
			// Process Value NV
			if (amnprocess.getAMN_Process_Value().compareToIgnoreCase("NV")== 0) {
				if (p_DaysTo == null || p_DaysTo.compareTo(BigDecimal.ZERO) <= 0) {
					 return "@Error@ " + Msg.parseTranslation(ctx, "No leave days available");
				}
				// Crear un nuevo registro en AMN_Leaves
				MAMN_Leaves amnleaves = MAMN_Leaves.createLeaveFromPayroll(ctx, p_AMN_Payroll_ID, p_InvDateIni, p_DaysTo, trxName);
				// Hacer zoom a la ventana de AMN_Leaves
				if(amnleaves != null && amnleaves.getAMN_Leaves_ID() > 0) {
					// Confirma la transacción actual
				    Trx.get(trxName, false).commit();
				    // Zoom to Leave
					try {
						 // Verifica si hay un contexto de UI disponible
					    if (SessionManager.getAppDesktop() != null) {
					        AEnv.zoom(MAMN_Leaves.Table_ID, amnleaves.getAMN_Leaves_ID());
					    } else {
					        addLog("No se puede abrir la ventana: No hay un contexto de UI disponible.");
					    }
					    // Mensaje de la notificación
				        String message = "Se ha creado un nuevo registro con ID: " + amnleaves.getAMN_Leaves_ID();
				        createNote(amnleaves, message);
				        // 
				        addLog(amnleaves.toString());
				    } catch (Exception e) {
				        addLog("Error al llamar a AEnv.zoom: " + e.getMessage());
				        e.printStackTrace();
				    }
					addLog(amnleaves.toString());
					retMsg = "Leave record created successfully";
					addLog(retMsg);
				} else {
					retMsg = "@Error@ " + Msg.parseTranslation(ctx, "Leave creation failed");
					addLog(retMsg);
					retMsg = "ProcessFailed";
				}
			} else {
				// Invalid Process
				retMsg = "@Error@ " + Msg.parseTranslation(ctx, "Invalid Process: " + amnprocess.getAMN_Process_Value());
				addLog(retMsg);
			}
		} else {
			// Existent Registers
			retMsg = "@Error@ " + Msg.parseTranslation(ctx, "Payroll ID " + amnpayroll.getAMN_Payroll_ID() + " already has leaves assigned.");
            addLog(retMsg);
            addLog("Si desea crear varias Justificaciones / Ausencias para ese recibo, debe hacerlo por la Opción de Justificaciones / Ausencias.");
		}
		return retMsg;
    }
	
	
	private void createNote(MAMN_Leaves amnleaves, String message) {
		
        // Obtener el usuario actual
        int AD_User_ID = Env.getAD_User_ID(Env.getCtx());
   
        // Crear la notificación
        MMessage msg = MMessage.get(getCtx(), "LeavesWorkFlow");
    	MNote note = new MNote(getCtx(), msg.getAD_Message_ID() ,AD_User_ID, null);
    	note.setDescription(amnleaves.getDescription());
    	note.setTextMsg(message);
    	note.setAD_Table_ID(MAMN_Leaves.Table_ID);
		note.setRecord_ID(amnleaves.getAMN_Leaves_ID());
		note.setAD_Org_ID(amnleaves.getAD_Org_ID());
		note.setAD_User_ID(amnleaves.getCreatedBy());
		note.saveEx();	
        addLog("Notificación enviada: " + message);
		
	}
}

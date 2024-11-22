package org.amerp.process;

import java.sql.Timestamp;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Leaves;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;

public class AMNPayrolLeavesProcess extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrolLeavesProcess.class);
	private int p_AMN_Employee_ID = 0;
	private int p_AMN_Leaves_ID = 0;
	private Timestamp p_DateDoc = null;
	private String p_AMN_Leaves_Status = "";
	
	@Override
	protected void prepare() {    // TODO Auto-generated method stub
    	//log.warning("...........Toma de Parametros...................");
    	ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			// SPECIAL CASE BECAUSE FIRST TWO PARAMETERS DOESN'T COME FROM TABLE
			// THEY COME FROM AMN_PROCESS_CONTRACT_V  (VIEW)
			// THIRD PARAMETER COMES FROM AMN_PERIOD TABLE
			if (paraName.equals("AMN_Employee_ID"))
				p_AMN_Employee_ID = para.getParameterAsInt();
			else if (paraName.equals("AMN_Leaves_ID"))
				p_AMN_Leaves_ID = para.getParameterAsInt();
			else if (paraName.equals("DateDoc"))
				p_DateDoc = para.getParameterAsTimestamp();
			else if (paraName.equals("AMN_Leaves_Status"))
				p_AMN_Leaves_Status = para.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
    }

	@Override
	protected String doIt() throws Exception {
		//
		String retValue= "Workflow Ejecutado Correctamente";
		String possibleValues="";
	    // Obtiene la Orden Ausencia
        MAMN_Leaves leave = new MAMN_Leaves(getCtx(), p_AMN_Leaves_ID, get_TrxName());
        // Lógica de Revisión de Status
        // AMN_Leaves_Draft = "DR"; 		
        // DR Draft - Borrador	Emitida por el Usuario, o en condición de Devuelta
        if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Draft)!=0) {
        	possibleValues = MAMN_Leaves.AMN_Leaves_Supervisor;
        	if (possibleValues.contains(p_AMN_Leaves_Status)) {
        		
        	}
        
       	// AMN_Leaves_Supervisor = "SU"; 	
        //	SU  Revisión Supervisor	En revisión por el Supervisor del Trabajador
        } else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Supervisor)!=0) {
            // 

        }
        //  AMN_Leaves_Rejected = "SR"; 		
        //	SR Solicitud Rechazada	Rechazada por el Supervisor del Trabajador
        else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Rejected)!=0) {
        
        }
        //  AMN_Leaves_HHRR = "RH"; 			
        //	RH Revisión Recursos Humanos	En Revisión por Recursos Humanos
        else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_HHRR)!=0) {
            
        }
        //  String AMN_Leaves_HHRR_Rejected = "RR";	
        //	RR Rechazada RRHH	Rechazada por Recursos Humanos
        else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_HHRR_Rejected)!=0) {
            
        }
        //  AMN_Leaves_Filed = "AR";			
        //	AR Archivo.	Archivo
        else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Filed)!=0) {
            
        }
        //  AMN_Leaves_Complete = "CO";		
        // 	CO Completada	Completada
        else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Complete)!=0) {
            
        }
        //  AMN_Leaves_Close = "CL";			
        // 	CL Closed 
        else if (leave.getAMN_Leaves_Status().compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Close)!=0) {
            
        }      
        
        leave.set_CustomColumn("ApprovalStatus", "Pending");
    	leave.set_CustomColumn("ApprovalStatus", "Approved");
        leave.setDocStatus(MAMN_Leaves.DOCSTATUS_Completed);
        leave.saveEx();
        return retValue;
		
	}

}

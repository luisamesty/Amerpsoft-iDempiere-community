package org.amerp.process;

import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Leaves;
import org.amerp.workflow.amwmodel.MAMW_WF_Node;
import org.amerp.workflow.amwmodel.MAMW_WF_NodeNext;
import org.amerp.workflow.amwmodel.MAMW_WorkFlow;
import org.compiere.model.MMessage;
import org.compiere.model.MNote;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class AMNPayrolLeavesProcess extends SvrProcess {

	static CLogger log = CLogger.getCLogger(AMNPayrolLeavesProcess.class);

	private int p_AMN_Leaves_ID = 0;
	private int p_AMW_WorkFlow_ID = 0;
	private int p_AMW_WF_Node_ID = 0;
	private int p_AMW_WF_NodeNext_ID = 0;
	private String p_Note="";
	
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
			if (paraName.equals("AMN_Leaves_ID"))
				p_AMN_Leaves_ID = para.getParameterAsInt();
			else if (paraName.equals("AMW_WorkFlow_ID"))
				p_AMW_WorkFlow_ID = para.getParameterAsInt();
			else if (paraName.equals("AMW_WF_Node_ID"))
				p_AMW_WF_Node_ID = para.getParameterAsInt();
			else if (paraName.equals("AMW_WF_NodeNext_ID"))
				p_AMW_WF_NodeNext_ID = para.getParameterAsInt();
			else if (paraName.equals("Note"))
				p_Note = para.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
    }

	@Override
	protected String doIt() throws Exception {
		//
		String prcValue= "Flujo Ejecutado";
		String possibleValues="";
		String Msg_Error= prcValue +" - "+Msg.translate(getCtx(), "Error");
		String Msg_User = Msg.translate(getCtx(), "ProcessOK");
		String MsgNote = "";
		// Get Window
		int windowId = Env.getContextAsInt(getCtx(), "AD_Window_ID");
		if (windowId > 0) {
			Msg_Error =   Msg.translate(getCtx(), "Window")+ ": " + windowId + "\r\n " + Msg_Error ;
			Msg_User  =   Msg.translate(getCtx(), "Window")+ ": " + windowId + "\r\n " + Msg_User;
        }
		// Record ID and TAb ID
		int tabId = Env.getContextAsInt(getCtx(), "#AD_Tab_ID");
		int recordId = getRecord_ID(); // Método proporcionado por la clase base SvrProcess.

	    // Obtiene la Orden Ausencia
        MAMN_Leaves leave = new MAMN_Leaves(getCtx(), p_AMN_Leaves_ID, get_TrxName());
        // AMW_Flow Verify Flow Parameters
        if (p_AMW_WorkFlow_ID > 0 && p_AMW_WF_Node_ID > 0) {
	        MAMW_WorkFlow awfl = new MAMW_WorkFlow(getCtx(), p_AMW_WorkFlow_ID, get_TrxName());
	        MAMW_WF_Node awfn = new MAMW_WF_Node(getCtx(), p_AMW_WF_Node_ID, get_TrxName());
	        MAMW_WF_Node[] awflonodes = awfl.getAMWNodesSQL(p_AMW_WorkFlow_ID);
	        if( awflonodes.length > 0 && awfn.check_AMW_WF_Node(p_AMW_WF_Node_ID, awflonodes )) {
	        	MAMW_WF_NodeNext awfnn = new MAMW_WF_NodeNext(getCtx(), p_AMW_WF_NodeNext_ID, get_TrxName());
	        	if (p_AMW_WF_NodeNext_ID > 0) {
	        		MAMW_WF_NodeNext[] awflonodesnext = awfnn.getAMWNodesNextSQL(p_AMW_WF_Node_ID);
	        		if (awflonodesnext.length > 0 && awfnn.check_AMW_WF_NodeNext(p_AMW_WF_Node_ID,  p_AMW_WF_NodeNext_ID, awflonodesnext )) {
	        			
	        			// 
	        			MAMW_WF_Node awfno = new MAMW_WF_Node(getCtx(), awfnn.getAMW_WF_Next_ID(), get_TrxName());
	        			String nextStatus = awfno.getValue().trim();
	        			if (leave.getNote()!=null)
	        				MsgNote = leave.getNote();
	        			if (!p_Note.isEmpty() && p_Note != null)
	        				MsgNote = MsgNote+"\r\n"+p_Note;
	        			// Set Doc Status.
	        			leave.setDocStatus(nextStatus);
	        	        // STATUS 
	        	        // DR Draft - Borrador	Emitida por el Usuario, o en condición de Devuelta
	        	        if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Draft)==0) {
	        	        	// Re Activate It
	        	        	leave.reActivateIt();
	        	       	// AMN_Leaves_Supervisor = "SU"; 	
	        	        //	SU  Revisión Supervisor	En revisión por el Supervisor del Trabajador
	        	        } else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Supervisor)==0) {
	        	            // 

	        	        }
	        	        //  AMN_Leaves_Rejected = "SR"; 		
	        	        //	SR Solicitud Rechazada	Rechazada por el Supervisor del Trabajador
	        	        else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Rejected)==0) {
	        	        
	        	        }
	        	        //  AMN_Leaves_HHRR = "RH"; 			
	        	        //	RH Revisión Recursos Humanos	En Revisión por Recursos Humanos
	        	        else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_HHRR)==0) {
	        	            
	        	        }
	        	        //  String AMN_Leaves_HHRR_Rejected = "RR";	
	        	        //	RR Rechazada RRHH	Rechazada por Recursos Humanos
	        	        else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_HHRR_Rejected)==0) {
	        	            
	        	        }
	        	        //  AMN_Leaves_Filed = "AR";			
	        	        //	AR Archivo.	Archivo
	        	        else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Filed)==0) {
	        	            
	        	        }
	        	        //  AMN_Leaves_Complete = "CO";		
	        	        // 	CO Completada	Completada
	        	        else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Complete)!=0) {
	        	        	// 
	        	        	leave.approveIt();
	        	        	leave.completeIt();
	        	        }
	        	        //  AMN_Leaves_Close = "CL";			
	        	        // 	CL Closed 
	        	        else if (nextStatus.compareToIgnoreCase(MAMN_Leaves.AMN_Leaves_Close)!=0) {
	        	            leave.closeIt();
	        	        }      
	        	        // Saves
	        	        leave.setNote(MsgNote);
	        	        if (!leave.save())
							throw new RuntimeException("Error saving Leave Update");
	        	        // Solicitar refresco en la ventana/pestaña
	        	        ProcessInfo processInfo = getProcessInfo();
	        	        processInfo.setSummary("REFRESH", true); // Indicativo para refrescar la UI
	        	        ProcessInfoParameter[] pips = getParameter();
	        	        for (ProcessInfoParameter pip : pips) {
	        	        	log.warning(pip.getInfo());
	        	        }
	        	        // Updates array
	        	        awflonodesnext = awfnn.getAMWNodesNextSQL(awfno.getAMW_WF_Node_ID());
	        	        // Message - Email User
	        	        if (awfn.getNotificationType()!= null) {
	        	        	// Send Notification
	        	        	sendNotification(leave, awfno);
	        	        }
	        	        // Process Message to the user
	        	        Msg_User = Msg_User + awfno.getValue()+" - "+awfno.getName();
	        	        addLog(Msg_User);
	        	        if (awfnn.getDescription()!=null)
	        	        	Msg_User = awfnn.getDescription();
	        	        else 
	        	        	Msg_User = awfno.getDescription();
	        	        addLog(Msg_User);
	        			// 
	        		}
	        	} else {
		        	Msg_Error = Msg_Error +
		        			Msg.translate(getCtx(), "AMW_WF_Node_ID")+" ("+p_AMW_WF_Node_ID+") ";
		        	addLog(Msg_Error);
	        		Msg_Error = Msg.translate(getCtx(), "AMW_WF_NodeNext_ID")+" ("+p_AMW_WF_NodeNext_ID+") ";
		        	addLog(Msg_Error);
		        	Msg_Error = Msg.translate(getCtx(), "invalid");
		        	addLog(Msg_Error);
	        	}
	        } else {
	        	Msg_Error = Msg_Error +
	        			Msg.translate(getCtx(), "AMW_WF_Node_ID")+" ("+p_AMW_WF_Node_ID+") ";
	        	addLog(Msg_Error);
	        	Msg_Error = Msg.translate(getCtx(), "invalid");
	        	addLog(Msg_Error);
	        }
        } else {
        	Msg_Error = Msg_Error +
        			Msg.translate(getCtx(), "AMW_WorkFlow_ID")+" ("+p_AMW_WorkFlow_ID+") "	+
        			Msg.translate(getCtx(), "AMW_WF_Node_ID")+" ("+p_AMW_WF_Node_ID+") ";
        	 addLog(Msg_Error);
        }
        return prcValue;
		
	}

	/**
	 * sendNotification
	 * N Notice : E Email : B Both - E Email and Notice : X None
	 * @param leave
	 * @param awfno
	 * @return
	 */
	private boolean sendNotification(MAMN_Leaves leave, MAMW_WF_Node awfno ) {
		
		boolean retValue = true;
    	// MOre Option (FUTURE
		// N Notice
    	if (awfno.getNotificationType().compareToIgnoreCase("N") == 0) {
    		MMessage msg = MMessage.get(getCtx(), "LeavesWorkFlow");
        	MNote note = new MNote(getCtx(), msg.getAD_Message_ID() , leave.getCreatedBy(), null);
        	note.setTextMsg(leave.getNote());
        	note.setDescription(awfno.getValue().trim()+" -"+awfno.getDescription());
        	note.setAD_Table_ID(MAMN_Leaves.Table_ID);
    		note.setRecord_ID(leave.getAMN_Leaves_ID());
    		note.setAD_Org_ID(leave.getAD_Org_ID());
    		note.setAD_User_ID(leave.getCreatedBy());
    		note.saveEx();	
    	} 
    	// E Email
    	else if (awfno.getNotificationType().compareToIgnoreCase("E") == 0) {
    		
    	}
    	// B Both - E Email and Notice
    	else if (awfno.getNotificationType().compareToIgnoreCase("B") == 0) {
    		
    	}
    	// X None
    	else if (awfno.getNotificationType().compareToIgnoreCase("X") == 0) {
    		
    	}
	
		return retValue;
		
	}
}

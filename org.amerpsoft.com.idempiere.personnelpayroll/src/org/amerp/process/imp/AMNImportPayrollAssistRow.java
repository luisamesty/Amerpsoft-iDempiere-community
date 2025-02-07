package org.amerp.process.imp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll_Assist;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Row;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Unit;
import org.amerp.amnutilities.PayrollAssistRowImport;
import org.compiere.model.MMessage;
import org.compiere.model.MNote;

public class AMNImportPayrollAssistRow extends SvrProcess {

	private Timestamp p_RefDateIni = null;
	private Timestamp p_RefDateEnd = null;
	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private boolean	p_IsScheduled = false;
	IProcessUI processMonitor = null;
	 	// Crear una lista para almacenar los PIN rechazados
    List<PayrollAssistRowImport> payrollassistrowRejected = new ArrayList<>();
    int recMAMN_Payroll_Assist_Row_ID = 0;
    Map<String, Integer> pinToEmployeeMap = new HashMap<>();
    
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
			if (paraName.equals("AD_Client_ID"))
				p_AD_Client_ID =  para.getParameterAsInt();
			else if (paraName.equals("AD_Org_ID"))
				p_AD_Org_ID =  para.getParameterAsInt();
			else if (paraName.equals("RefDateIni"))
				p_RefDateIni = para.getParameterAsTimestamp();
			else if (paraName.equals("RefDateEnd"))
				p_RefDateEnd = para.getParameterAsTimestamp();
			else if (paraName.equals("IsScheduled"))
				p_IsScheduled = para.getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}	 
		//log.warning("...........Parametros...................");
		
    }

    @Override
    protected String doIt() throws Exception {
    	
    	MAMN_Employee amnemployee = null;
        String messagetoShow = "";
        String messagetoNotify= "";
        int rowsUpdated = 0;
        Properties ctx = getCtx();
        String trxName = get_TrxName();  // Mantener una única transacción g
        List<Integer> processedRows = new ArrayList<>();
        List<MAMN_Payroll_Assist> batchList = new ArrayList<>();
        
        // Ajustar `p_RefDateIni a las 00:00:00 y_RefDateEnd` a las 23:59:59
        Calendar cal = Calendar.getInstance();
        cal.setTime(p_RefDateIni);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        p_RefDateIni = new Timestamp(cal.getTimeInMillis());
        
        cal.setTime(p_RefDateEnd);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        p_RefDateEnd = new Timestamp(cal.getTimeInMillis());
               
        // Determina el Numero de Registros
        int rowCount = 0;
        String countSql = "SELECT COUNT(*) FROM AMN_Payroll_Assist_Row "
                        + "WHERE amn_datetime >= ? AND amn_datetime <= ? "
                        + "AND AD_Client_ID = ? AND AD_Org_ID = ?";

        PreparedStatement countStmt = DB.prepareStatement(countSql, get_TrxName());
        countStmt.setTimestamp(1, p_RefDateIni);
        countStmt.setTimestamp(2, p_RefDateEnd);
        countStmt.setInt(3, p_AD_Client_ID);
        countStmt.setInt(4, p_AD_Org_ID);

        ResultSet countRs = countStmt.executeQuery();
        if (countRs.next()) {
        	rowCount = countRs.getInt(1);  // Obtiene el total de filas
        }
        DB.close(countRs, countStmt); // Cierra el ResultSet y el PreparedStatement

        // Crea Array de PINs
        String pinQuery = "SELECT PIN, AMN_Employee_ID FROM amn_employee WHERE PIN IN (SELECT DISTINCT PIN FROM AMN_Payroll_Assist_Row)";

        try (PreparedStatement pinStmt = DB.prepareStatement(pinQuery, trxName);
                ResultSet pinRs = pinStmt.executeQuery()) {

               while (pinRs.next()) {
                   pinToEmployeeMap.put(pinRs.getString("PIN"), pinRs.getInt("AMN_Employee_ID"));
               }
        }  // **Se cierran automáticamente el PreparedStatement y ResultSet**
 
        try {

          int batchSize = 1000;  // Número de registros por lote
          int offset = 0;
          boolean hasMoreRows = true;
          processMonitor = Env.getProcessUI(ctx);
          int percent = 0;
          int rowNumber = 0;
          
          while (hasMoreRows) {
        	    // Nueva transacción por lote
        	    Trx trx = Trx.get(Trx.createTrxName("AMN_Import"), true);
        	    // Query Lote
//                String sql = "SELECT * FROM AMN_Payroll_Assist_Row WHERE amn_datetime >= ? AND amn_datetime <= ? "
//                           + "AND AD_Client_ID = ? AND AD_Org_ID = ? LIMIT ? OFFSET ?";
                String sql = "SELECT * FROM ( "
                		+ "SELECT DISTINCT ON (pin, amn_datetime) "
                		+" * "
                		+ "FROM AMN_Payroll_Assist_Row "
                		+ "WHERE amn_datetime >= ? "
                		+ "  AND amn_datetime <= ? "
                		+ "  AND AD_Client_ID = ? "
                		+ "  AND AD_Org_ID = ? "
                		+ "ORDER BY pin, amn_datetime "
                		+ "LIMIT ? OFFSET ?" 
                	+" ) AS aprar ORDER BY aprar.amn_payroll_assist_row_id ";
                
                try (PreparedStatement pstmt = DB.prepareStatement(sql, trxName)) {
	              
	                pstmt.setTimestamp(1, p_RefDateIni);
	                pstmt.setTimestamp(2, p_RefDateEnd);
	                pstmt.setInt(3, p_AD_Client_ID);
	                pstmt.setInt(4, p_AD_Org_ID);
	                pstmt.setInt(5, batchSize);
	                pstmt.setInt(6, offset);
	
	                try (ResultSet rs = pstmt.executeQuery()) {
		                batchList = new ArrayList<>();
		
		                while (rs.next()) {
		                    MAMN_Payroll_Assist_Row row = new MAMN_Payroll_Assist_Row(ctx, rs, get_TrxName());
			                if (!p_IsScheduled) {
				                rowNumber++; // Incrementamos en cada iteración
								// Percentage Monitor
				                percent = (int) (100.0 * rowNumber / rowCount);
								messagetoShow = String.format("%-10s",rowNumber)+"/"+String.format("%-10s",rowCount)+
										" ( "+String.format("%-6s",percent)+"% )  AMN_Payroll_Assist_Row:"+
										String.format("%-10s",row.getAMN_Payroll_Assist_Row_ID())+" - "+
										String.format("%-10s",row.getPIN())+
										String.format("%-20s",row.getAMN_DateTime());
								log.warning(messagetoShow);
			                }
			                
			                // Validación del PIN (sin consultas repetidas)
			                Integer employeeId = pinToEmployeeMap.get(row.getPIN());
			                amnemployee = (employeeId != null) ? new MAMN_Employee(ctx, employeeId, trxName) : null;
		
			                if (amnemployee != null) {
			                    MAMN_Payroll_Assist assist = createAmnPayrollAssistFromRow(ctx, p_AD_Client_ID, p_AD_Org_ID, amnemployee, row, trxName);
			                    if (assist != null) {
			                        assist.set_TrxName(trxName); // Asegura que use la misma transacción
			                        batchList.add(assist);
			                        processedRows.add(row.getAMN_Payroll_Assist_Row_ID());
			                    }
			                } else {
		                    	// If return false Add to array to show at end of process
		                    	PayrollAssistRowImport foundUnit = PayrollAssistRowImport.searchByPin(payrollassistrowRejected, row.getPIN());
		                        if (foundUnit == null) {
		                        	MAMN_Payroll_Assist_Unit amnunit = new MAMN_Payroll_Assist_Unit(ctx, row.getAMN_Payroll_Assist_Unit_ID(),get_TrxName() );
		                        	PayrollAssistRowImport newPRR = new PayrollAssistRowImport(row.getPIN(), amnunit.getName(), 1 );
		                        	payrollassistrowRejected.add(newPRR);
		                        } else {
		                        	PayrollAssistRowImport.incrementQtyByPin(payrollassistrowRejected, row.getPIN());
		                        }
			                }
		                }
	                }
                }
                // Show Screen Monitor
    			if (!p_IsScheduled && processMonitor != null && Env.getProcessUI(getCtx()) != null) {
    				processMonitor.statusUpdate(messagetoShow);
    			}
	            // Guardar en batch
                for (MAMN_Payroll_Assist assist : batchList) {
                        assist.saveEx();
                }
                
                // UPDate Processed Rows.
                rowsUpdated = rowsUpdated + processedRows.size();
                if (processedRows.size() > 0) {
	                String updateSql = "UPDATE AMN_Payroll_Assist_Row SET IsVerified = 'Y' WHERE AMN_Payroll_Assist_Row_ID IN (" +
	                                   processedRows.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
	                DB.executeUpdate(updateSql, get_TrxName());
	                processedRows.clear();
                }
                
                // Verifica si hay mas registros
                hasMoreRows = (rowNumber < rowCount);	
                offset += batchSize;
                
                // **Agregar retardo para liberar recursos**
                try {
                    Thread.sleep(1000); // Espera 1000 ms antes de continuar con el siguiente lote
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                }
          }
	       	// Confirmar la transacción global al final del proceso
          	Trx trx = Trx.get(trxName, false);
          	if (trx != null) {
	              trx.commit();
          	}
          	
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error en el proceso de importación", e);
            return Msg.getMsg(ctx, "ProcessFailed");
        } finally {

        }
        if (!p_IsScheduled) {
        	// Final Message PINs Rejected
        	// HEADER
        	messagetoShow="";
        	messagetoNotify = Msg.getElement(ctx, "PIN")+"(s) "+Msg.translate(ctx, "NotFound");
        	addLog(messagetoNotify);
        	// rowsUpdated rowsUpdated
        	messagetoShow=Msg.translate(ctx, "Row")+"(s) "+Msg.translate(ctx, "Updated")+"(s) = "+
        			String.format("%10s",rowsUpdated)+ "   "+
        			Msg.translate(ctx, "Row")+"(s) "+Msg.translate(ctx, "Total")+"(s) = "+
        			String.format("%10s",rowCount);
        	addLog(messagetoShow);
        	messagetoNotify = messagetoNotify +"\r\n"+ messagetoShow;
        	messagetoShow = String.format("%15s",Msg.getElement(ctx, MAMN_Payroll_Assist_Row.COLUMNNAME_PIN))+ "  " + 
        			String.format("%30s",Msg.getElement(ctx,MAMN_Payroll_Assist_Unit.COLUMNNAME_Name))+ " " +
        			String.format("%15s",Msg.getElement(ctx,"Qty"));
        	addLog(messagetoShow);
        	messagetoNotify = messagetoNotify +"\r\n"+ messagetoShow;
            for (PayrollAssistRowImport unit : payrollassistrowRejected) {
            	messagetoShow = String.format("%15s",unit.getPin())+ "  " +
            			String.format("%30s",unit.getAMN_Payroll_Assist_Unit())+" "+
            			String.format("%15s",unit.getQty());
            	addLog(messagetoShow);
            	messagetoNotify = messagetoNotify +"\r\n"+ messagetoShow;
            }
        }
        // Send Notification
        sendNotification(ctx, "N", messagetoNotify);
        return Msg.getMsg(ctx, "ProcessOK");
    }
    
    
    /**
     * createAmnPayrollAssistFrowRow
     * Creates AMN_Payroll_Assist record from AMN_Payroll_Assist_Row
     * @param ctx
     * @param p_AD_Client_ID
     * @param p_AD_Org_ID
     * @param row
     * @param trxName
     * @return
     */
    public MAMN_Payroll_Assist createAmnPayrollAssistFromRow(Properties ctx,  
			int p_AD_Client_ID, int p_AD_Org_ID, MAMN_Employee amnemployee, 
			MAMN_Payroll_Assist_Row row , String trxName )
	{
    	
    	MAMN_Payroll_Assist retValue= null;
    	String description = "";
    	String amn_assistrecord = "";
        // Verifica Unidad de Marcaje
        int UnitID = row.getAMN_Payroll_Assist_Unit_ID();
        // If Null Creates New
        if (amnemployee != null && row != null) {
           description = amnemployee.getValue()+"-"+amnemployee.getName().trim();
        	if (UnitID != 0) {
        		MAMN_Payroll_Assist_Unit amnunit = new MAMN_Payroll_Assist_Unit(ctx, row.getAMN_Payroll_Assist_Unit_ID(),trxName );
	        	description = "U"+amnunit.getName().trim()+" "+description;            
        	}
        	// Verificar si el registro existe
            MAMN_Payroll_Assist amnpayrollassist = MAMN_Payroll_Assist.findMAMN_Payroll_AssistByRowID(ctx, row.getAMN_Payroll_Assist_Row_ID(), trxName);
        	if (amnpayrollassist == null  ) {
	           	amnpayrollassist = new MAMN_Payroll_Assist(ctx, 0 , trxName);
//	           	amnpayrollassist.setAMN_Payroll_Assist_ID(row.getAMN_Payroll_Assist_Row_ID());
	        	amnpayrollassist.setAMN_Payroll_Assist_Row_ID(row.getAMN_Payroll_Assist_Row_ID());
	        	//amnpayrollassist.setAD_Client_ID(p_AD_Client_ID);
				amnpayrollassist.setAD_Org_ID(p_AD_Org_ID);
				amnpayrollassist.setIsActive(true);
	        } 
        	amn_assistrecord = MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(row.getAMN_DateTime()) + "-"+
            		MAMN_Payroll_Assist.getPayrollAssist_DayofWeekName(row.getAMN_DateTime())+ "-"+
            		row.getAMN_DateTime().toString();
//			log.warning("AMN_Payroll_Assist_Row_ID:"+row.getAMN_Payroll_Assist_Row_ID()+
//					"  amn_assistrecord:"+amn_assistrecord+"  description:"+description);	    
			amnpayrollassist.setDescription(description);
			amnpayrollassist.setAMN_Employee_ID(amnemployee.getAMN_Employee_ID());
			amnpayrollassist.setAMN_Shift_ID(amnemployee.getAMN_Shift_ID()); // ID del turno
			amnpayrollassist.setdayofweek(MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(row.getAMN_DateTime())); // Día de la semana (2 = Lunes)
			amnpayrollassist.setDescanso(false); // No es día de descanso
			amnpayrollassist.setEvent_Date(row.getAMN_DateTime()); // Fecha del evento
			amnpayrollassist.setEvent_Time(row.getAMN_DateTime()); // Hora del evento
			amnpayrollassist.setEvent_Type("I"); // Tipo de evento (I = Entrada)
			amnpayrollassist.setBioCode(amnemployee.getBioCode()); // Código biométrico
			amnpayrollassist.setAMN_AssistRecord(amn_assistrecord);
			retValue= amnpayrollassist;
        }
		
		return retValue;
	}	//	createAmnPayrollAssistFrowRow
    
	/**
	 * sendNotification
	 * N Notice : E Email : B Both - E Email and Notice : X None
	 * @param leave
	 * @param notificationType
	 * @return
	 */
	private boolean sendNotification(Properties ctx, String notificationType , String notifactionMessage) {
		
		boolean retValue = true;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    	// MOre Option (FUTURE
		// N Notice
    	if (notificationType.compareToIgnoreCase("N") == 0) {
        	MNote note = new MNote(ctx, "Import",getAD_User_ID(), null);
        	note.setTextMsg(notifactionMessage);
        	note.setDescription(MMessage.get(ctx, "Import") +
        	        Msg.translate(ctx, "From") + " " +
        	        Msg.getElement(ctx, "RefDateIni") + ":" + dateFormat.format(p_RefDateIni) + " " +
        	        Msg.translate(ctx, "To") + " " +
        	        Msg.getElement(ctx, "RefDateEnd") + ":" + dateFormat.format(p_RefDateEnd));
        	note.setAD_Table_ID(MAMN_Payroll_Assist_Row.Table_ID);
    		note.setRecord_ID(recMAMN_Payroll_Assist_Row_ID);
    		note.setAD_Org_ID(0);
    		note.setAD_User_ID(getAD_User_ID());
    		note.saveEx();	
    	} 
    	// E Email
    	else if (notificationType.compareToIgnoreCase("E") == 0) {
    		
    	}
    	// B Both - E Email and Notice
    	else if(notificationType.compareToIgnoreCase("B") == 0) {
    		
    	}
    	// X None
    	else if (notificationType.compareToIgnoreCase("X") == 0) {
    		
    	}
	
		return retValue;
		
	}
}
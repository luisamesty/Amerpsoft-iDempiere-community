package org.amerp.process.imp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.model.MProcessPara;
import org.adempiere.util.IProcessUI;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll_Assist;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Row;
import org.amerp.amnmodel.MAMN_Payroll_Assist_Unit;
import org.amerp.amnutilities.AmerpMsg;
import org.compiere.model.MClient;
import org.compiere.model.MProcess;
import org.compiere.model.MTable;
import org.compiere.model.PO;


public class AMNImportPayrollAssistRow extends SvrProcess {

	private Timestamp p_RefDateIni = null;
	private Timestamp p_RefDateEnd = null;
	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private boolean	p_IsScheduled = false;
	IProcessUI processMonitor = null;
    
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
    	MAMN_Payroll_Assist_Unit assistunit = null;
        // Verificar si el proceso se ejecuta desde un Scheduler
        boolean isScheduler = false;
        
        String sql = "SELECT * "
        		+ "FROM AMN_Payroll_Assist_Row "
        		+ " WHERE amn_datetime >= '" + p_RefDateIni +"'"
        		+ " AND amn_datetime <= '"+p_RefDateEnd+"'"
        		+ " AND AD_Client_ID =" + p_AD_Client_ID
        		+ " AND AD_Org_ID =" + p_AD_Org_ID
        		+ " "        		;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = DB.prepareStatement( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY , get_TrxName());
            rs = pstmt.executeQuery();
            processMonitor = Env.getProcessUI(getCtx());
            
            int rowCount = 0;
            int percent = 0;
            int rowNumber = 0; // Inicializamos un contador
            if (rs != null) {
                rs.last(); // Mueve el cursor al último registro
                rowCount = rs.getRow(); // Obtiene el número de la fila actual (que es el total de filas)
                rs.beforeFirst(); // Vuelve al inicio del ResultSet para seguir usándolo
            }
          
            while (rs.next()) {
                MAMN_Payroll_Assist_Row row = new MAMN_Payroll_Assist_Row(getCtx(), rs, get_TrxName());
                if (!p_IsScheduled) {
	                rowNumber++; // Incrementamos en cada iteración
					// Percentage Monitor
	                percent = 100 * (rowNumber / rowCount);
					String MessagetoShow = String.format("%-10s",rowNumber)+"/"+String.format("%-10s",rowCount)+
							" ( "+String.format("%-5s",percent)+"% )  AMN_Payroll_Assist_Row:"+
							String.format("%-10s",row.getAMN_Payroll_Assist_Row_ID())+" - "+
							String.format("%-10s",row.getPIN())+
							String.format("%-20s",row.getAMN_DateTime());
	    			if (processMonitor != null) {
	    				processMonitor.statusUpdate(MessagetoShow);
	    			}
                }
                // Creates Assist from ROW
                if (createAmnPayrollAssistFrowRow(getCtx(), p_AD_Client_ID, p_AD_Org_ID, row, get_TrxName())) {
                	//  Updates AMN_Payroll_Assist_Row IsVerified with 'Y'
                	row.updateAMNPayrollAssistRow(getCtx(), row.getAMN_Payroll_Assist_Row_ID(), sql);
                }
                		
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error en el proceso de importación", e);
            return Msg.getMsg(getCtx(), "ProcessFailed");
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }

        return Msg.getMsg(getCtx(), "ProcessOK");
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
    public boolean createAmnPayrollAssistFrowRow(Properties ctx,  
			int p_AD_Client_ID, int p_AD_Org_ID,
			MAMN_Payroll_Assist_Row row , String trxName )
	{
    	
    	boolean retValue= false;
    	String description = "";
    	String amn_assistrecord = "";
    	Trx trx = Trx.get(trxName, true); // Obtener la transacción
    	 // Obtener la tabla correspondiente
        MTable table = MTable.get(Env.getCtx(), MAMN_Payroll_Assist.Table_Name);
        // Verificar si el registro existe
        MAMN_Payroll_Assist amnpayrollassist = MAMN_Payroll_Assist.findMAMN_Payroll_AssistByRowID(Env.getCtx(), row.getAMN_Payroll_Assist_Row_ID());
        // Verifica Trabajador
        MAMN_Employee amnemployee = findAMN_EmployeebyPin(row.getPIN());
        // Verifica Unidad de Marcaje
        int UnitID = row.getAMN_Payroll_Assist_Unit_ID();
        // If Null Creates New
        if (amnemployee != null ) {
         	description = amnemployee.getValue()+"-"+amnemployee.getName().trim();
        	if (UnitID != 0) {
        		MAMN_Payroll_Assist_Unit amnunit = new MAMN_Payroll_Assist_Unit(ctx, row.getAMN_Payroll_Assist_Unit_ID(),trxName );
	        	description = "U"+amnunit.getName().trim()+" "+description;            
        	}
	        if (amnpayrollassist == null  ) {
	           	amnpayrollassist = new MAMN_Payroll_Assist(ctx, 0 , trxName);
	           	amnpayrollassist.setAMN_Payroll_Assist_ID(row.getAMN_Payroll_Assist_Row_ID());
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
			// SAVE RECORD
			if (amnpayrollassist.save()) {
				retValue= true;
				 // Confirmar la transacción (commit)
	            trx.commit();
            }
            
        }
		
		return retValue;
	}	//	createAmnPayrollAssistFrowRow
    
    
	/**
	 * findAMN_EmployeebyPin 
	 * Description: Find AMN_Employee_ID from Pin
	 * @param ctx
	 * @param Pin
	 * @return AMN_Employee or null not found()
	 */
	public static MAMN_Employee findAMN_EmployeebyPin(String p_Pin) {
		
		int AMN_Employee_ID=0;
		MAMN_Employee retvalue= null;
		String sql = "SELECT AMN_Employee_ID "
			+ "FROM amn_employee "
			+ "WHERE pin=? "
			;        		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setString (1, p_Pin);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				AMN_Employee_ID = rs.getInt(1);
			}
		}
	    catch (SQLException e)
	    {
	    	AMN_Employee_ID = 0;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (AMN_Employee_ID != 0)
			retvalue = new MAMN_Employee(Env.getCtx(),AMN_Employee_ID, null);
		return retvalue; 
	}
    
}
package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;

import org.amerp.amnmodel.*;
import org.compiere.util.*;

public class AMNPayrollProcessPayrollAssistProcess {

	
	public AMNPayrollProcessPayrollAssistProcess() {
		super();
		
	}


	// VARS
	private Timestamp Shift_In1=null;
	private Timestamp Shift_In2=null;
	private Timestamp Shift_Out1=null;
	private Timestamp Shift_Out2=null;
	private Timestamp defShift_In1 = null;
	private Timestamp defShift_In2 = null;
	private Timestamp defShift_Out1 = null;
	private Timestamp defShift_Out2 = null;
	private Boolean isError=false;
	private String locMsg_Value="";	
	String locMsg_Value_Events="";
	int assitAMN_Shift_ID=0;
	long lErrorAttendance= (long) 300;		// Subido ( 5 minutos) Antes 120 Seconds (2 Minutes)
	private Boolean Descanso= false;
	private Boolean	Excused = false;
	private Timestamp Event_Time; 
	private int empAMN_Shift_ID=0;
	private int AssistCounter =0;
	private MAMN_Employee amnemployee;
	//private MAMN_Payroll_Assist_Proc atthours;
 	
	static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollAssistProcess.class);

    // Usar un enum es más seguro y legible que un String
    public enum AssistProcessMode {
        CLEAN("0"),
        PROCESS_ASSIST("1"),
        PROCESS_ASSIST_AND_FILL_DEFAULTS("2"),
        PROCESS_DEFAULTS_ONLY("3"),
        // El modo 4 no está implementado, pero lo definimos para claridad
        // No es necesario incluir el modo 4 en el enum si no se va a usar.
        UPDATE_HND_HNN_ETC("4"); 

        private final String code;
        AssistProcessMode(String code) {
            this.code = code;
        }

        public static AssistProcessMode fromCode(String code) {
            for (AssistProcessMode mode : values()) {
                if (mode.code.equals(code)) {
                    return mode;
                }
            }
            // Lanza una excepción si el modo no es válido
            throw new IllegalArgumentException("Modo de procesamiento inválido: " + code);
        }
    }

	/**
	 * CreatePayrollDocumentsAssistProcforEmployeeOneDay
	 * @param p_AMN_Contract_ID
	 * @param p_currentDate
	 * @param p_AMN_Period_ID removed
	 * @param p_AMN_Employee_ID
	 * @param p_AMN_Assist_Process_Mode  
	 * 	"0": Clean Records on AMN_Payroll_Assist_Proc 
	 *  "1": Create or Update Records Only from  AMN_Payroll_Assist
	 *  "2": Create or Update Records from  AMN_Payroll_Assist and Fill Default Values from AMN_Shift_Detail
	 *  "3: Create or Update Records from Only from  AMN_Shift_Detail
	 * @return locMsg_Value
	 */
    // El método principal ahora es mucho más limpio
    public String CreatePayrollDocumentsAssistProcforEmployeeOneDay (Properties p_ctx,  
            int p_AD_Client_ID, int p_AD_Org_ID, int p_AMN_Contract_ID, 
            Timestamp p_Event_Date, int p_AMN_Employee_ID, String p_AMN_Assist_Process_Mode, boolean p_IsScheduled) {


	    // Get p_AD_Client_ID,p_AD_Org_ID from Employee
	    amnemployee = new MAMN_Employee(p_ctx, p_AMN_Employee_ID, null);
	    // Get Employee AMN_Shift_ID by default
	    empAMN_Shift_ID = amnemployee.getAMN_Shift_ID();
		// LOOK FOR Default Shift if exists
		MAMN_Shift shift = new MAMN_Shift(); 
	    if (empAMN_Shift_ID == 0) empAMN_Shift_ID =  shift.sqlGetDefaultAMN_Shift_ID(p_AD_Client_ID);
	    // Verify if defAMN_Shift_ID == 0 
        // Validaciones iniciales
	    isError=true;
        // 
        if (isError) {
        	locMsg_Value="***** "+Msg.getMsg(p_ctx, "found.none")+" "+
					Msg.getElement(p_ctx, "AMN_Shift_ID")+" "+
					Msg.getMsg(p_ctx, "default")+ " *******";
        }
		Shift_In1=null;
		Shift_In2=null;
		Shift_Out1=null;
		Shift_Out2=null;
        // Obtener el modo de procesamiento de manera segura
        AssistProcessMode mode = AssistProcessMode.fromCode(p_AMN_Assist_Process_Mode);

        switch (mode) {
            case CLEAN:
                return processModeClean(p_ctx, p_AD_Client_ID, p_AD_Org_ID, amnemployee, p_Event_Date, p_IsScheduled);
            case PROCESS_ASSIST:
                return processModeAssist(p_ctx, p_AD_Client_ID, p_AD_Org_ID, amnemployee, p_Event_Date, p_IsScheduled);
            case PROCESS_ASSIST_AND_FILL_DEFAULTS:
                return processModeAssistAndFillDefaults(p_ctx, p_AD_Client_ID, p_AD_Org_ID, amnemployee, p_Event_Date, p_IsScheduled);
            case PROCESS_DEFAULTS_ONLY:
                return processModeDefaultsOnly(p_ctx, p_AD_Client_ID, p_AD_Org_ID, amnemployee, p_Event_Date, p_IsScheduled);
            default:
                // Esto no debería ocurrir si se usa el enum, pero es una buena práctica
                return Msg.getMsg(p_ctx, "Mode")+" "+Msg.getMsg(p_ctx, "Process")+" "+Msg.getMsg(p_ctx, "invalid");
        }
    }

    // * 	"0": Clean Records on AMN_Payroll_Assist_Proc 
    private String processModeClean(Properties ctx, int clientId, int orgId, MAMN_Employee amnemployee, Timestamp eventDate, boolean p_IsScheduled) {
        
    	// Lógica del caso "0"
		locMsg_Value=locMsg_Value+Msg.getElement(ctx, "AMN_Shift_ID")+":"+amnemployee.getAMN_Shift_ID()+" "+
				Msg.getMsg(ctx,"Date")+":"+eventDate.toString().substring(0,10);
		 MAMN_Payroll_Assist_Proc atthours = new MAMN_Payroll_Assist_Proc(ctx, 0, 0, null); 
		 boolean success = MAMN_Payroll_Assist_Proc.createAmnPayrollAssistProc(
				ctx, Env.getLanguage(ctx).getLocale(), 
				amnemployee.getAMN_Employee_ID(), eventDate, 
				amnemployee.getAMN_Shift_ID(),
				Descanso, Excused,
				atthours, p_IsScheduled) ;
   
        return formatResult(success,  Msg.getMsg(ctx, "Erase")+" "+Msg.getMsg(ctx, "ShiftMarks") );
    }

    // "1": Create or Update Records Only from  AMN_Payroll_Assist (DEFAULT)
    private String processModeAssist(Properties ctx, int clientId, int orgId, 
            MAMN_Employee amnemployee, Timestamp eventDate, boolean p_IsScheduled ) {

    	return processAttendanceGeneric(ctx, clientId, orgId, amnemployee, eventDate, p_IsScheduled, false);

    }

	// "2": Create or Update Records from  AMN_Payroll_Assist and Fill Default Values from AMN_Shift_Detail
    //      Esta opcion es similar a la "1" solo que utiliza defalt values si son nulos
    private String processModeAssistAndFillDefaults(Properties ctx, int clientId, int orgId, MAMN_Employee amnemployee, Timestamp eventDate, boolean p_IsScheduled) {
        // Lógica del caso "2"
    	   return processAttendanceGeneric(ctx, clientId, orgId, amnemployee, eventDate, p_IsScheduled, true);

    }
    
    // "3: Create or Update Records from Only from  AMN_Shift_Detail
    private String processModeDefaultsOnly(Properties ctx, int clientId, int orgId, MAMN_Employee amnemployee, Timestamp eventDate, boolean p_IsScheduled) {
        // Lógica del caso "3"
    	boolean success = false;
    	MAMN_Shift_Detail amnshiftdetail = MAMN_Shift_Detail.findByEventDate(ctx, empAMN_Shift_ID, eventDate);
    	if (amnshiftdetail!=null && amnshiftdetail.isActive()) {
    		setShiftDetailValues(amnshiftdetail, eventDate);
	        MAMN_Payroll_Assist_Proc atthours = AMNPayrollProcessPayrollAssistUtil.calcAttendanceValuesofPayrollVars(
	                ctx, clientId, orgId, amnemployee.getC_Country_ID(), eventDate, 
	                empAMN_Shift_ID, defShift_In1, defShift_In2, defShift_Out1, defShift_Out2);
	        success = MAMN_Payroll_Assist_Proc.createAmnPayrollAssistProc(
	                ctx, Env.getLanguage(ctx).getLocale(), 
	                amnemployee.getAMN_Employee_ID(), eventDate, 
	                amnemployee.getAMN_Shift_ID(), 
	                Descanso, Excused, 
	                atthours, p_IsScheduled);
    	}
        return formatResult(success, "Proceso de asistencia");
    }

    /**
     * Procesa la asistencia de un Trabajador para una fecha determinada
     * @param ctx contexto
     * @param clientId ID del cliente
     * @param orgId ID de la organización
     * @param amnemployee empleado
     * @param eventDate fecha del evento
     * @param p_IsScheduled true si es un cálculo programado
     * @param useDefaults true si se deben usar los valores de Shift por defecto cuando falten
     * @return mensaje del resultado
     */
    private String processAttendanceGeneric(Properties ctx, int clientId, int orgId,
            MAMN_Employee amnemployee, Timestamp eventDate, boolean p_IsScheduled,
            boolean useDefaults) {

        boolean success = false;
        String resultMsg = "Sin turnos válidos";
        MAMN_Payroll_Assist_Proc atthoursDefault = null;
        // 0. Verificar si ya existe registro protegido
        MAMN_Payroll_Assist_Proc existingRecord = MAMN_Payroll_Assist_Proc.findbyEmployeeandDate(
                ctx, Env.getLanguage(ctx).getLocale(), amnemployee.getAMN_Employee_ID(), eventDate);

        if (existingRecord != null && existingRecord.isProtected()) {
            return formatResult(false, "Registro protegido: no se puede actualizar");
        }
        
        // 1. Obtener todos los turnos del trabajador
        MAMN_Shift[] shifts = MAMN_Employee_Shift.getShiftsForEmployee(ctx, amnemployee.getAMN_Employee_ID(), null);
        int defaultShiftId = amnemployee.getAMN_Shift_ID();

        // 2. Set global VARs desde AMN_Payroll_Assist
        // 		Shift_In1, Shift_Out1, Shift_In2, Shift_Out2
        setShiftValuesFromAMNPayrollAssist(amnemployee.getAMN_Employee_ID(), eventDate, lErrorAttendance);
        //		IF NULL Set defShift_In1, defShift_Out1, defShift_In2,defShift_Out2;
        if (useDefaults) fillDefaultShiftValues();

        int bestShiftId = -1;
        BigDecimal maxHours = BigDecimal.ZERO;
        MAMN_Payroll_Assist_Proc bestAttendance = null;

        // 3. Recorrer todos los turnos
        for (MAMN_Shift shift : shifts) {
            MAMN_Shift_Detail amnshiftdetail = MAMN_Shift_Detail.findByEventDate(ctx, shift.getAMN_Shift_ID(), eventDate);
            if (amnshiftdetail != null && amnshiftdetail.isActive()) {
                setShiftDetailValues(amnshiftdetail, eventDate);
                if (useDefaults) fillDefaultShiftValues();

                // Calc Attendance Columns 
                MAMN_Payroll_Assist_Proc atthours = AMNPayrollProcessPayrollAssistUtil.calcAttendanceValuesofPayrollVars(
                        ctx, clientId, orgId, amnemployee.getC_Country_ID(), eventDate,
                        shift.getAMN_Shift_ID(), Shift_In1, Shift_Out1, Shift_In2, Shift_Out2);
                // Toma el objeto del turno por defecto
                if (shift.getAMN_Shift_ID() == defaultShiftId)
                	atthoursDefault = atthours;
                BigDecimal totalHours = atthours.getShift_HND();
                if (totalHours != null && totalHours.compareTo(maxHours) > 0) {
                    maxHours = totalHours;
                    bestShiftId = shift.getAMN_Shift_ID();
                    bestAttendance = atthours;
                }
            }
        }

        // 4. Grabar turno con más horas o por defecto
        if (bestShiftId > 0 && bestAttendance != null) {
            success = MAMN_Payroll_Assist_Proc.createAmnPayrollAssistProc(
                    ctx, Env.getLanguage(ctx).getLocale(),
                    amnemployee.getAMN_Employee_ID(), eventDate,
                    bestShiftId,
                    Descanso, Excused,
                    bestAttendance, p_IsScheduled);
            resultMsg = "Procesada con turno " + bestShiftId + " (" + maxHours + " horas)";
        } 
        
        if (bestShiftId <= 0 && defaultShiftId > 0) {
        	// Crear un objeto vacío para evitar NPE
            MAMN_Payroll_Assist_Proc emptyAttendance = new MAMN_Payroll_Assist_Proc(ctx, 0, null);
            // Verifica el objeto creado con el turno por defecto
            if (atthoursDefault!=null) {
            	emptyAttendance = atthoursDefault;
            	if(atthoursDefault.countMarks() > 0)
            		emptyAttendance.setDescription(
            				Msg.getElement(ctx, MAMN_Shift.COLUMNNAME_AMN_Shift_ID)+" "+
            				Msg.getMsg(ctx, "Invalid")+" "+atthoursDefault.getDescription().trim());
            }
            success = MAMN_Payroll_Assist_Proc.createAmnPayrollAssistProc(
                    ctx, Env.getLanguage(ctx).getLocale(),
                    amnemployee.getAMN_Employee_ID(), eventDate,
                    defaultShiftId,
                    Descanso, Excused,
                    emptyAttendance, p_IsScheduled);
            resultMsg = "Procesada con turno por defecto " + defaultShiftId;
        }

        return formatResult(success, resultMsg);
    }

    
    /**
     * formatResult
     * @param success
     * @param modeDescription
     * @return
     */
    private static String formatResult(boolean success, String modeDescription) {
        return success ? modeDescription + " OK" : modeDescription + " **ERROR**";
    }

    
    /**
     * setShiftDetailValues
     * Set Global VARS 
     * @param amnshiftdetail
     * @param p_Event_Date
     */
    private void setShiftDetailValues(MAMN_Shift_Detail amnshiftdetail, Timestamp p_Event_Date) {
    	
	    // Creates Shift_in1, Shift_in2, Shift_out1, shift_oou2 from entrytime, breakstart, timeout and breakminutes
	    if (amnshiftdetail.getShift_In1()==null)
	    	amnshiftdetail.setShift_In1(amnshiftdetail.getEntryTime());
	    if (amnshiftdetail.getShift_Out1()==null)
	    	amnshiftdetail.setShift_Out1(amnshiftdetail.getBreakStart());
	    if (amnshiftdetail.getShift_In2()==null) {
	    	GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(amnshiftdetail.getBreakStart());
			cal.set(Calendar.DAY_OF_YEAR, 0);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.YEAR, 0);
			cal.add(Calendar.MINUTE, amnshiftdetail.getBreakMinutes());
	    	amnshiftdetail.setShift_In2(new Timestamp(cal.getTimeInMillis()));
	    }
	    if (amnshiftdetail.getShift_Out2()==null)
	    	amnshiftdetail.setShift_Out2(amnshiftdetail.getTimeOut());
	    // Get Default Assist Input - Output Times referenced to p_Event_date
		defShift_In1 = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_In1());
		defShift_In2 = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_In2());
		defShift_Out1 = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_Out1());
		defShift_Out2 = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_Out2());
	    // Get Descanso from AMN_Shift_Detail
		Descanso= amnshiftdetail.isDescanso();	   
    }
 

	/**
	 * Set Global VARs from AMN_Payroll_Assist
	 * Shift_In1, Shift_Out1, Shift_In2, Shift_Out2
	 * @param p_AMN_Employee_ID
	 * @param p_Event_Date
	 */
    private void setShiftValuesFromAMNPayrollAssist(int p_AMN_Employee_ID, Timestamp p_Event_Date, long lErrorAttendance) {
        // Variables para controlar los errores por marcaje seguidos
        Timestamp lastEvent = null;
        final long errorWindowMillis = lErrorAttendance * 1000; // En milisegundos
        AssistCounter = 0; // Reiniciar contador
        locMsg_Value_Events = ""; // Reiniciar log de eventos
        
        // Query para recuperar eventos del día
        String sql = "SELECT DISTINCT ON (event_time) " +
                     "amn_shift_id, dayofweek, event_time, event_type " +
                     "FROM amn_payroll_assist " +
                     "WHERE amn_employee_id=? AND event_date=? AND isactive='Y' " +
                     "ORDER BY event_time";

        try (PreparedStatement pstmt1 = DB.prepareStatement(sql, null)) {
            pstmt1.setInt(1, p_AMN_Employee_ID);
            pstmt1.setTimestamp(2, p_Event_Date);

            try (ResultSet rs = pstmt1.executeQuery()) {
                while (rs.next()) {
                    if (AssistCounter >= 4) {
                        log.info("Se alcanzó el máximo de 4 marcaciones válidas para empleado " + p_AMN_Employee_ID);
                        break;
                    }

                    int shiftId = rs.getInt("amn_shift_id");
                    Timestamp eventTime = rs.getTimestamp("event_time");

                    // Filtrar eventos nulos
                    if (eventTime == null) {
                        continue;
                    }

                    // Filtrar eventos muy cercanos (< errorWindowMillis)
                    if (lastEvent != null) {
                        long diff = Math.abs(eventTime.toInstant().toEpochMilli() - lastEvent.toInstant().toEpochMilli());
                        if (diff < errorWindowMillis) {
                            log.info("Evento descartado por ventana de error (" + diff + "ms < " + errorWindowMillis + "ms): " + eventTime);
                            continue;
                        }
                    }
                    lastEvent = eventTime; // Actualizar último evento válido
                    assitAMN_Shift_ID = shiftId;

                    // Log de eventos
                    locMsg_Value_Events += eventTime.toString().substring(11, 16) + "  ";
                    AssistCounter++;
                    // Asignar a marcaciones
                    switch (AssistCounter) {
                        case 1: Shift_In1  = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date, eventTime); break;
                        case 2: Shift_Out1 = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date, eventTime); break;
                        case 3: Shift_In2  = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date, eventTime); break;
                        case 4: Shift_Out2 = AMNPayrollProcessPayrollAssistUtil.getTimestampShifdetailEventTime(p_Event_Date, eventTime); break;
                    }
                }
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
        }

        // Trabajador
        MAMN_Employee employee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
        // Evaluar cantidad de eventos obtenidos
        if (AssistCounter == 0) {
            // Caso: sin marcaciones → usar turno por defecto      
            int defaultShiftId = employee.getAMN_Shift_ID();
            if (defaultShiftId > 0) {
                assitAMN_Shift_ID = defaultShiftId;
                log.info("Usando turno por defecto: " + defaultShiftId + " para trabajador " + employee.getValue());
            }
        } else if (AssistCounter == 1 || AssistCounter == 3) {
            // Caso: cantidad inconsistente → loguear advertencia
            log.warning("Marcaciones incompletas (" + AssistCounter + ") para trabajador " + employee.getValue()
                        + " en fecha " + p_Event_Date + " → eventos: " + locMsg_Value_Events);
        }
    }

    /**
     * Reemplaza los valores Shift_* nulos con los valores por defecto defShift_*
     */
    private void fillDefaultShiftValues() {
        if (Shift_In1 == null)  Shift_In1  = defShift_In1;
        if (Shift_Out1 == null) Shift_Out1 = defShift_Out1;
        if (Shift_In2 == null)  Shift_In2  = defShift_In2;
        if (Shift_Out2 == null) Shift_Out2 = defShift_Out2;
    }

	
}

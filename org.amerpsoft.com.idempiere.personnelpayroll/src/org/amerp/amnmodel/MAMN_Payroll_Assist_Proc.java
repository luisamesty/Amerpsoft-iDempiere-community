/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
  *****************************************************************************/
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.adempiere.util.IProcessUI;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */
public class MAMN_Payroll_Assist_Proc extends X_AMN_Payroll_Assist_Proc {


    /**
	 * 
	 */
    private static final long serialVersionUID = -3721855390921872221L;
    
    static CLogger log = CLogger.getCLogger(MAMN_Payroll_Assist_Proc.class);
    
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Assist_Proc> s_cache = new CCache<Integer,MAMN_Payroll_Assist_Proc>(Table_Name, 10);

	/**
	 * Constructor para setear AD_Client_ID y AD_Org_ID
	 * @param ctx
	 * @param AD_Client_ID
	 * @param AD_Org_ID
	 * @param trxName
	 */
	public MAMN_Payroll_Assist_Proc(Properties ctx, int AD_Client_ID, int AD_Org_ID, String trxName) {
		this(ctx, 0, trxName); // ⬅️ Esto ejecuta tu constructor personalizado, con inicialización incluida
	    setAD_Client_ID(AD_Client_ID);  // ← solo visible dentro del modelo
	    setAD_Org_ID(AD_Org_ID);
	    setIsActive(true);
	}

	/**
	 * @param p_ctx
	 * @param AMN_Payroll_Assist_Proc_ID
	 * @param p_trxName
	 */
    public MAMN_Payroll_Assist_Proc(Properties p_ctx, int AMN_Payroll_Assist_Proc_ID, String p_trxName) {
	    super(p_ctx, AMN_Payroll_Assist_Proc_ID, p_trxName);
	    // Inicializar todos los campos en cero o valores por defecto
        if (AMN_Payroll_Assist_Proc_ID == 0) {
            setAD_Client_ID(Env.getAD_Client_ID(p_ctx));
            setAD_Org_ID(0);
            setAMN_Employee_ID(0);
            setAMN_Payroll_Assist_Proc_ID(0);
            setAMN_Payroll_Assist_Proc_UU("");
            setBioCode("");
            setDescanso(false);
            setDescription("");
            setEvent_Date(new Timestamp(System.currentTimeMillis()));
            setExcused(false);
            setIsActive(true);
            setName("");
            setShift_Attendance(BigDecimal.ZERO);
            setShift_AttendanceBonus(BigDecimal.ZERO);
            setShift_EDE(BigDecimal.ZERO);
            setShift_HC(BigDecimal.ZERO);
            setShift_HED(BigDecimal.ZERO);
            setShift_HEF(BigDecimal.ZERO);
            setShift_HEN(BigDecimal.ZERO);
            setShift_HER(BigDecimal.ZERO);
            setShift_HLGT15(BigDecimal.ZERO);
            setShift_HND(BigDecimal.ZERO);
            setShift_HNN(BigDecimal.ZERO);
            setShift_HT(BigDecimal.ZERO);
            setIsNonBusinessDay(false);
            setProtected(false);
            // Inicializar las horas null
	          setShift_In1(null);
	          setShift_In2(null);
	          setShift_Out1(null);
	          setShift_Out2(null);
            setShift_LTA(BigDecimal.ZERO);
            setShift_THL(BigDecimal.ZERO);
            setUpdatedBy(0);

        }
    }
    
    /**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
	public MAMN_Payroll_Assist_Proc(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // 
	}

	// Constructor adicional para crear una instancia sin ID (nuevo registro)
	public MAMN_Payroll_Assist_Proc(Properties ctx, String trxName) {
	    this(ctx, 0, trxName);
	}

	// Método para obtener un Timestamp con la hora en cero (00:00:00)
    private Timestamp getTimestampWithZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Hora en 00
        calendar.set(Calendar.MINUTE, 0);      // Minutos en 00
        calendar.set(Calendar.SECOND, 0);      // Segundos en 00
        calendar.set(Calendar.MILLISECOND, 0); // Milisegundos en 00
        return new Timestamp(calendar.getTimeInMillis());
    }

	/**
	 * Get Payroll_Assist_Proc from Cache
	 * @param ctx context
	 * @param MAMN_Payroll_Assist_Proc_ID id
	 * @return MAMN_Payroll_Assist_Proc
	 */
	public static MAMN_Payroll_Assist_Proc get (Properties ctx, int MAMN_Payroll_Assist_Proc_ID)
	{
		if (MAMN_Payroll_Assist_Proc_ID <= 0)
			return null;
		//
		Integer key = new Integer(MAMN_Payroll_Assist_Proc_ID);
		MAMN_Payroll_Assist_Proc retValue = (MAMN_Payroll_Assist_Proc) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Payroll_Assist_Proc (ctx, MAMN_Payroll_Assist_Proc_ID, null);
		if (retValue.getAMN_Payroll_Assist_Proc_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get
	
	/**
	 * MAMN_Payroll_Assist_Proc findByCalendar
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Period_ID		AMN_Period_ID
	 * @param p_AMN_Employee_ID		Employee
	 * @return MAMN_Period
	 */
	public static MAMN_Payroll_Assist_Proc findbyEmployeeandDate(Properties ctx, Locale locale, 
				 int p_AMN_Employee_ID,Timestamp p_Event_Date) {
				
		MAMN_Payroll_Assist_Proc retValue = null;
		String sql = "SELECT * "
			+ "FROM amn_payroll_assist_proc "
			+ "WHERE  AMN_Employee_ID=?"
			+ " AND event_date=?"
			;        
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Employee_ID);
            pstmt.setTimestamp (2, p_Event_Date);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Payroll_Assist_Proc amnpayrollassistproc = new MAMN_Payroll_Assist_Proc(ctx, rs, null);
				Integer key = new Integer(amnpayrollassistproc.getAMN_Payroll_Assist_Proc_ID());
				s_cache.put (key, amnpayrollassistproc);
				if (amnpayrollassistproc.isActive())
					retValue = amnpayrollassistproc;
			}
		}
	    catch (SQLException e)
	    {
	    	retValue = null;
	    }
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return retValue;
	}

    /**
	 * boolean createAmnPayrollAssistProc
	 * @param ctx
	 * @param locale
	 * @param p_Ad_Org_ID
	 * @param p_AD_Client_ID  		
	 * @param int p_AMN_Employee_ID
	 * @param Timestamp p_Event_Date
	 * @return MAMN_Period_Assist_Proc
	 */
	public static boolean createAmnPayrollAssistProc(Properties ctx, Locale locale, 
			int p_AMN_Employee_ID, Timestamp p_Event_Date , int p_AMN_Shift_ID,
			Boolean p_isDescanso, Boolean p_isExcused ,
			MAMN_Payroll_Assist_Proc payassistrow, boolean p_IsScheduled
			)
	{
		// Verify Locale
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
		//
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		// p_DayOfWeek
		String p_DayOfWeek="1";
		String Shift_Value="";
		String locMessage="";
		p_DayOfWeek=MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(p_Event_Date);
		// Employee Value
		MAMN_Employee amnemployee = new MAMN_Employee(ctx, p_AMN_Employee_ID, null);
		String Employee_Value=amnemployee.getValue().trim();
		locMessage= locMessage+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+" \n";
		// Shift Value if p_AMN_Shift_ID != 0
	    if (p_AMN_Shift_ID > 0) {
	    	MAMN_Shift amnshift = new MAMN_Shift(ctx, p_AMN_Shift_ID, null);
	    	Shift_Value = amnshift.getValue().trim();
		} else {
			Shift_Value = "*****  "+Msg.getElement(ctx, "AMN_Shift_ID")+" = 0   *****";
		}
	    locMessage= locMessage+Shift_Value+ Msg.getMsg(ctx, "Date")+": "+p_Event_Date.toString().substring(0,10)+" \n";
	    MAMN_Payroll_Assist_Proc amnpayrollassistproc = MAMN_Payroll_Assist_Proc.findbyEmployeeandDate(ctx, locale,  p_AMN_Employee_ID, p_Event_Date);
		if (amnpayrollassistproc == null)
		{
			amnpayrollassistproc = new MAMN_Payroll_Assist_Proc(ctx, 0, null);
			amnpayrollassistproc.setAD_Client_ID(amnemployee.getAD_Client_ID());
			amnpayrollassistproc.setAD_Org_ID(amnemployee.getAD_OrgTo_ID());
			amnpayrollassistproc.setdayofweek(p_DayOfWeek);
			amnpayrollassistproc.setAMN_Shift_ID(p_AMN_Shift_ID);
			amnpayrollassistproc.setEvent_Date(p_Event_Date);
			amnpayrollassistproc.setAMN_Employee_ID(p_AMN_Employee_ID);
			amnpayrollassistproc.setDescription(payassistrow.getDescription().trim());
			amnpayrollassistproc.setName(Employee_Value.trim()+"-"+Shift_Value+"-"+p_Event_Date);
			amnpayrollassistproc.setDescanso(p_isDescanso);
			amnpayrollassistproc.setShift_Attendance(payassistrow.getShift_Attendance());
			amnpayrollassistproc.setShift_AttendanceBonus(payassistrow.getShift_AttendanceBonus());
			amnpayrollassistproc.setShift_EDE(payassistrow.getShift_EDE());
			amnpayrollassistproc.setShift_HC(payassistrow.getShift_HC());
			amnpayrollassistproc.setShift_HED(payassistrow.getShift_HED());
			amnpayrollassistproc.setShift_HEF(payassistrow.getShift_HEF());
			amnpayrollassistproc.setShift_HEN(payassistrow.getShift_HEN());
			amnpayrollassistproc.setShift_HER(payassistrow.getShift_HER());
			amnpayrollassistproc.setShift_HLGT15(payassistrow.getShift_HLGT15());
			amnpayrollassistproc.setShift_HLLT15(payassistrow.getShift_HLLT15());
			amnpayrollassistproc.setShift_HND(payassistrow.getShift_HND());
			amnpayrollassistproc.setShift_HNN(payassistrow.getShift_HNN());
			amnpayrollassistproc.setShift_HT(payassistrow.getShift_HT());
			amnpayrollassistproc.setShift_In1(payassistrow.getShift_In1());
			amnpayrollassistproc.setShift_Out1(payassistrow.getShift_Out1());
			amnpayrollassistproc.setShift_In2(payassistrow.getShift_In2());
			amnpayrollassistproc.setShift_Out2(payassistrow.getShift_Out2());
			amnpayrollassistproc.setShift_LTA(payassistrow.getShift_LTA());
			amnpayrollassistproc.setShift_THL(payassistrow.getShift_THL());	
			amnpayrollassistproc.setIsNonBusinessDay(payassistrow.isNonBusinessDay());
			amnpayrollassistproc.setExcused(p_isExcused);
			amnpayrollassistproc.setIsActive(true);	
		}
		else
		{
			//amnpayrollassist.setAMN_AssistRecord(Msg.getMsg(ctx, "Day")+"_"+p_PeriodDate.toString().substring(0, 10)+"_"+p_PeriodDay);
			amnpayrollassistproc.setDescription(payassistrow.getDescription().trim());
			amnpayrollassistproc.setdayofweek(p_DayOfWeek);
			amnpayrollassistproc.setAMN_Shift_ID(p_AMN_Shift_ID);
			amnpayrollassistproc.setName(Employee_Value.trim()+"-"+Shift_Value+"-"+p_Event_Date);
			amnpayrollassistproc.setDescanso(p_isDescanso);
			amnpayrollassistproc.setShift_Attendance(payassistrow.getShift_Attendance());
			amnpayrollassistproc.setShift_AttendanceBonus(payassistrow.getShift_AttendanceBonus());
			amnpayrollassistproc.setShift_EDE(payassistrow.getShift_EDE());
			amnpayrollassistproc.setShift_HC(payassistrow.getShift_HC());
			amnpayrollassistproc.setShift_HED(payassistrow.getShift_HED());
			amnpayrollassistproc.setShift_HEF(payassistrow.getShift_HEF());
			amnpayrollassistproc.setShift_HEN(payassistrow.getShift_HEN());
			amnpayrollassistproc.setShift_HER(payassistrow.getShift_HER());
			amnpayrollassistproc.setShift_HLGT15(payassistrow.getShift_HLGT15());
			amnpayrollassistproc.setShift_HLLT15(payassistrow.getShift_HLLT15());
			amnpayrollassistproc.setShift_HND(payassistrow.getShift_HND());
			amnpayrollassistproc.setShift_HNN(payassistrow.getShift_HNN());
			amnpayrollassistproc.setShift_HT(payassistrow.getShift_HT());
			// If Protected Do not Updated
			if (!amnpayrollassistproc.isProtected()) {
				amnpayrollassistproc.setShift_In1(payassistrow.getShift_In1());
				amnpayrollassistproc.setShift_Out1(payassistrow.getShift_Out1());
				amnpayrollassistproc.setShift_In2(payassistrow.getShift_In2());
				amnpayrollassistproc.setShift_Out2(payassistrow.getShift_Out2());
			}
			amnpayrollassistproc.setShift_LTA(payassistrow.getShift_LTA());
			amnpayrollassistproc.setShift_THL(payassistrow.getShift_THL());	
			amnpayrollassistproc.setIsNonBusinessDay(payassistrow.isNonBusinessDay());
			amnpayrollassistproc.setExcused(p_isExcused);
			amnpayrollassistproc.setIsActive(true);		
		}
		if (!p_IsScheduled &&processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Day")+": "+p_PeriodDate.toString());
			processMonitor.statusUpdate(locMessage);
		}
		amnpayrollassistproc.saveEx();	//	Creates AMN_Payroll_Assist Control
		return true;
		
	}	//	AMN_Payroll_Assist
	
	/**
	 * getEmployeeAllowedShifts: 
	 * Returns Shift from AMN_Employee table and AMN_Employee_Shift table
	 * @param ctx
	 * @param AMN_Employee_ID
	 * @return
	 */
	public static List<Integer> getEmployeeAllowedShifts(Properties ctx, int AMN_Employee_ID) {
	    List<Integer> allowedShiftIds = new ArrayList<>();
	    if (AMN_Employee_ID <= 0)
	        return allowedShiftIds;

	    MAMN_Employee employee = new MAMN_Employee(ctx, AMN_Employee_ID, null);

	    // Turno por defecto
	    int defaultShiftId = employee.getAMN_Shift_ID();
	    if (defaultShiftId > 0)
	        allowedShiftIds.add(defaultShiftId);

	    // Turnos de AMN_Employee_Shift
	    List<MAMN_Employee_Shift> empShifts = new Query(ctx, MAMN_Employee_Shift.Table_Name,
	            "AMN_Employee_ID=?", null)
	            .setParameters(AMN_Employee_ID)
	            .setOnlyActiveRecords(true)
	            .list();

	    for (MAMN_Employee_Shift es : empShifts) {
	        allowedShiftIds.add(es.getAMN_Shift_ID());
	    }

	    return allowedShiftIds;
	}
	
	/**
	 * Cuenta cuántas marcaciones (Shift_In1/Out1/In2/Out2) no son nulas en el objeto de asistencia.
	 * Devuelve un valor entre 0 y 4.
	 */
	public int countMarks() {

	    int count = 0;
	    // usamos get_Value para evitar depender de nombres concretos de getters
	    if (this.get_Value(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In1) != null)  count++;
	    if (this.get_Value(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out1) != null) count++;
	    if (this.get_Value(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_In2) != null)  count++;
	    if (this.get_Value(MAMN_Payroll_Assist_Proc.COLUMNNAME_Shift_Out2) != null) count++;
	    return count;
	}

}

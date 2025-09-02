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
package org.amerp.process;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

import org.amerp.amnmodel.*;
import org.compiere.util.*;

/** AMNPayrollProcessPayrollAssistProc
 * Description: Generic Class AMNPayrollCreatePayrollAssistProc
 * Called from: AMNPayrollProcessPayrollAssistOneEmployee
 * 				AMNPayrollProcessPayrollAssistOnePeriod
 * 				AMNPayrollProcessPayrollAssistOneAttendanceDay
 * Procedures:
 * calcAttendanceValuesofPayrollVars
 * getTimestampShifdetailEventTime
 * secondsOfTime
 * 
 * @author luisamesty
 *
 */
public class AMNPayrollProcessPayrollAssistUtil {
	// PUBLIC VARS

	static CLogger log = CLogger.getCLogger(AMNPayrollProcessPayrollAssistUtil.class);
	/**
	 * Procedure: calcAttendanceValuesofPayrollVars
	 * Description: Calculates Values of HND,HNN,HED,HEN,Attendance,AttendanceBonus
	 * 				From AMN_Payroll_Assist  Values 
	 * 				( p_Event_Date, p_AMN_Shift_ID, p_Shift_In1, p_Shift_Out1, p_Shift_In2, p_Shift_Out2)
	 * @param Timestamp Event_Date
	 * @param Integer p_AMN_Shift_ID
	 * @param Timestamp p_Shift_In1
	 * @param Timestamp p_Shift_Out1
	 * @param Timestamp p_Shift_In2
	 * @param Timestamp p_Shift_Out2
	 * @return AttendanceHours Array
	 * All event Times are Normalized to Event_Date
	 */
	public static MAMN_Payroll_Assist_Proc calcAttendanceValuesofPayrollVars(
	        Properties ctx, int p_AD_Client_ID, int p_AD_Org_ID, int p_C_Country_ID,
	        Timestamp p_Event_Date, Integer p_AMN_Shift_ID, 
	        Timestamp p_Shift_In1, Timestamp p_Shift_Out1, 
	        Timestamp p_Shift_In2, Timestamp p_Shift_Out2) 
	{
	    // *********************************************************
	    // ******* 1️⃣ Variables iniciales
	    // *********************************************************
	    final BigDecimal BDZero = BigDecimal.ZERO;
	    final long lErrorAttendance = 120; // 120 segundos (2 minutos)
	    final int precision = 3;
	    StringBuilder strMessage = new StringBuilder();
	    StringBuilder error = new StringBuilder();
	    
	    // *********************************************************
	    // ******* 2️⃣ Estructura de asistencia
	    // *********************************************************
	    MAMN_Payroll_Assist_Proc attendancehours = new MAMN_Payroll_Assist_Proc(Env.getCtx(), null);
	    attendancehours.setShift_In1(p_Shift_In1);
	    attendancehours.setShift_In2(p_Shift_In2);
	    attendancehours.setShift_Out1(p_Shift_Out1);
	    attendancehours.setShift_Out2(p_Shift_Out2);
	    attendancehours.setEvent_Date(p_Event_Date);
	    attendancehours.setAMN_Shift_ID(p_AMN_Shift_ID);

	    // *********************************************************
	    // ******* 3️⃣ Turnos por defecto
	    // *********************************************************
	    MAMN_Shift_Detail amnshiftdetail = MAMN_Shift_Detail.findByEventDate(ctx, p_AMN_Shift_ID, p_Event_Date);
	    if (amnshiftdetail == null) {
	        attendancehours.setDescription(
	        		Msg.getMsg(ctx, "Error")+" "+
	        		Msg.getElement(ctx, MAMN_Shift_Detail.COLUMNNAME_AMN_Shift_Detail_ID)+" "+p_AMN_Shift_ID);
	        attendancehours.setIsNonBusinessDay(true);
	        attendancehours.setShift_HND(BDZero);
	        attendancehours.setShift_HNN(BDZero);
	        return attendancehours;
	    }

	    MAMN_Payroll_Assist_Proc defattendancehours = calcDefaultAttendanceValuesofPayrollVars(
	            ctx, p_AD_Client_ID, p_AD_Org_ID, p_C_Country_ID, p_Event_Date, p_AMN_Shift_ID, amnshiftdetail);

	    // *********************************************************
	    // ******* 4️⃣ Normalización de horas de turno
	    // *********************************************************
	    normalizeShiftTimes(amnshiftdetail);

	    Timestamp defShift_In1  = getTimestampShifdetailEventTime(p_Event_Date, amnshiftdetail.getShift_In1());
	    Timestamp defShift_Out1 = getTimestampShifdetailEventTime(p_Event_Date, amnshiftdetail.getShift_Out1());
	    Timestamp defShift_In2  = getTimestampShifdetailEventTime(p_Event_Date, amnshiftdetail.getShift_In2());
	    Timestamp defShift_Out2 = getTimestampShifdetailEventTime(p_Event_Date, amnshiftdetail.getShift_Out2());
	    long breakTimeSeconds  = 3600;
	    if ( amnshiftdetail.getBreakMinutes() > 0)
	    	breakTimeSeconds  = amnshiftdetail.getBreakMinutes()*60;
	    boolean bDescanso = amnshiftdetail.isDescanso();

	    // *********************************************************
	    // ******* 5️⃣ Validaciones de datos vacíos
	    // *********************************************************
	    int nonNullCount = 0;
	    if (p_Shift_In1 != null) nonNullCount++;
	    if (p_Shift_Out1 != null) nonNullCount++;
	    if (p_Shift_In2 != null) nonNullCount++;
	    if (p_Shift_Out2 != null) nonNullCount++;
	    // Casos 0, 1 y 3
	    strMessage.append(Msg.getMsg(ctx, "ShiftMarks")+"=");
	    error.append(Msg.getMsg(ctx, "ShiftMarks")+"="+nonNullCount+", ");
	    if (nonNullCount == 0 ) {
	    	error.append(".");
	    } else if (nonNullCount == 1 || nonNullCount == 3) {
	    	error.append(", invalido!");    
	    } else if (nonNullCount == 2) {
	    	if (p_Shift_In1 != null && p_Shift_Out1 != null) {
	            // Llamar a calculo con dos	
	    		strMessage.append(", OK!");  
	    	} else {
	    		error.append(Msg.getMsg(ctx, "invalid"));    
	    	}
	    } else if (nonNullCount == 4) {
	        // Llamar a calculo con 4
	    	strMessage.append(", OK!");   
	    } else {
	    	error.append(Msg.getMsg(ctx, "invalid"));     
	    }
	    // Si hay error la variable tiene contenido
	    if (!error.toString().isEmpty()) {
	        attendancehours.setDescription(error.toString());
	        return attendancehours;
	    }

	    // *********************************************************
	    // ******* 6️⃣ Variables día/noche
	    // *********************************************************
	    Calendar zeroCal = Calendar.getInstance();
	    zeroCal.setTime(p_Event_Date);
	    zeroCal.set(Calendar.HOUR_OF_DAY, 0);
	    zeroCal.set(Calendar.MINUTE, 0);
	    zeroCal.set(Calendar.SECOND, 0);
	    zeroCal.set(Calendar.MILLISECOND, 0);

	    Calendar dayCal   = getDayCalendar(p_C_Country_ID, p_Event_Date);
	    Calendar nightCal = getNightCalendar(p_C_Country_ID, p_Event_Date);

	    long ldayHr   = secondsOfTime(dayCal, zeroCal);
	    long lnightHr = secondsOfTime(nightCal, zeroCal);

	    // *********************************************************
	    // ******* 7️⃣ Cálculo de horas reales y por defecto
	    // *********************************************************
	    long lShift_In1  = toSeconds(p_Shift_In1, zeroCal);
	    long lShift_Out1 = toSeconds(p_Shift_Out1, zeroCal);
	    long lShift_In2  = toSeconds(p_Shift_In2, zeroCal);
	    long lShift_Out2 = toSeconds(p_Shift_Out2, zeroCal);

	    long ldefShift_In1  = toSeconds(defShift_In1, zeroCal);
	    long ldefShift_Out1 = toSeconds(defShift_Out1, zeroCal);
	    long ldefShift_In2  = toSeconds(defShift_In2, zeroCal);
	    long ldefShift_Out2 = toSeconds(defShift_Out2, zeroCal);
	    
	    // *********************************************************
	    // ******* 8️⃣ Cálculo de horas normales, nocturnas y extras
	    // *********************************************************
    	/**
    	 * Calcula todas las horas de un turno completo (dos mitades)
    	 * @param lShift_In1 Entrada primer tramo
    	 * @param lShift_Out1 Salida primer tramo
    	 * @param lShift_In2 Entrada segundo tramo
    	 * @param lShift_Out2 Salida segundo tramo
    	 * @param ldefShift_In1 Entrada por defecto primer tramo
    	 * @param ldefShift_Out1 Salida por defecto primer tramo
    	 * @param ldefShift_In2 Entrada por defecto segundo tramo
    	 * @param ldefShift_Out2 Salida por defecto segundo tramo
    	 * @param ldayHr Inicio del día (segundos)
    	 * @param lnightHr Inicio de la noche (segundos)
    	 * @param lHalfDay Minutos mínimos para contabilizar asistencia
    	 * @param lHalfDayMin Segundos mínimos para asistencia parcial
    	 * @return BigDecimal[] Totales: [0]=HND, [1]=HNN, [2]=HED, [3]=HEN, [4]=Attendance, [5]=AttendanceBonus
    	 */
		BigDecimal[] totals = new BigDecimal[6]; // HND, HNN, HED, HEN, Attendance, Bonus
		// Si hay Mensage la variable tiene contenido
		if (!strMessage.toString().isEmpty()) {
			totals = calcShiftTotals(lShift_In1, lShift_Out1, lShift_In2, lShift_Out2, 
					ldefShift_In1, ldefShift_Out1, ldefShift_In2, ldefShift_Out2, 
					ldayHr, lnightHr, breakTimeSeconds);
		} else {
		    attendancehours.setDescription(error.toString());
		    return attendancehours;
		}

	    // Ajustes: restar extras
	    totals[1] = totals[1].subtract(totals[3]); // HNN -= HEN
	    totals[0] = totals[0].subtract(totals[2]); // HND -= HED

		/**
		 * Calcula indicadores de un turno según marcaciones y horas por defecto.
		 * @param lShift_In1  Entrada primer tramo
		 * @param lShift_Out1 Salida primer tramo
		 * @param lShift_In2  Entrada segundo tramo
		 * @param lShift_Out2 Salida segundo tramo
    	 * @param ldefShift_In1 Entrada por defecto primer tramo
    	 * @param ldefShift_Out1 Salida por defecto primer tramo
    	 * @param ldefShift_In2 Entrada por defecto segundo tramo
    	 * @param ldefShift_Out2 Salida por defecto segundo tramo
		 * @param precision Decimales a usar en los cálculos
		 * @return BigDecimal[] shiftIndicators: 
		 * 	 * Indicadores:
		 * 0 = HT   Total Work Hours		1 = HC   Complete Hours
		 * 2 = THL  Free Hours				3 = HLGT15 Free Hours > 15 min
		 * 4 = HLLT15 Free Hours <= 15 min	5 = LTA  Late Arrivals
		 * 6 = EDE  Early Departure			7 = HER  Extra Clock Hours
		 * 8 = HEF  Extra Holiday Hours		* 9 = HNO  Additional Night Hours
		 */

	    BigDecimal[] shiftIndicators = calcShiftIndicators(lShift_In1, lShift_Out1, lShift_In2, lShift_Out2, 
	    		ldefShift_In1, ldefShift_Out1, ldefShift_In2, ldefShift_Out2, 
	    		ldayHr, lnightHr, precision);
	    
		// *********************************************************
		// ******* 9️⃣ Conversión a BigDecimal (segundos a Horas)
		// *********************************************************
		BigDecimal BD3600 = BigDecimal.valueOf(3600);
		// HND, HNN, HED, HEN, Attendance, Bonus
		BigDecimal Shift_HND = totals[0].divide(BD3600, precision, RoundingMode.CEILING);
		BigDecimal Shift_HNN = totals[1].divide(BD3600, precision, RoundingMode.CEILING);
		BigDecimal Shift_HED = totals[2].divide(BD3600, precision, RoundingMode.CEILING);
		BigDecimal Shift_HEN = totals[3].divide(BD3600, precision, RoundingMode.CEILING);
		BigDecimal Shift_Attendance      = totals[4].divide(BigDecimal.valueOf(2), precision, RoundingMode.CEILING);
		BigDecimal Shift_AttendanceBonus = totals[5].divide(BigDecimal.valueOf(2), precision, RoundingMode.CEILING);
		
	    // Normalizar contra valores máximos
	    if (defattendancehours.getShift_HND().compareTo(BDZero) > 0 && Shift_HND.compareTo(defattendancehours.getShift_HND()) > 0)
	        Shift_HND = defattendancehours.getShift_HND();

	    if (defattendancehours.getShift_HNN().compareTo(BDZero) > 0 && Shift_HNN.compareTo(defattendancehours.getShift_HNN()) > 0)
	        Shift_HNN = defattendancehours.getShift_HNN();

		// shiftIndicators VARs
		// * 0 = HT   Total Work Hours
		// * 1 = HC   Complete Hours
		// * 2 = THL  Free Hours
		// * 3 = HLGT15 Free Hours > 15 min
		// * 4 = HLLT15 Free Hours <= 15 min
		// * 5 = LTA  Late Arrivals
		// * 6 = EDE  Early Departure
		// * 7 = HER  Extra Clock Hours
		// * 8 = HEF  Extra Holiday Hours
		// * 9 = HNO  Additional Night Hours
		BigDecimal HR_HT = shiftIndicators[0]; 	// Total Work Hours (Shift_HT)
		BigDecimal HR_HC = shiftIndicators[1];	// Complete Hours (Shift_HC)
		BigDecimal HR_THL= shiftIndicators[2];  // Free Hour
		BigDecimal HR_HLGT15 = shiftIndicators[3];    // Greater than 15 (Shift_HLGT15)
		BigDecimal HR_HLLT15 = shiftIndicators[4];    // Free Hours Less than 15 (Shift_HLLT15)
		BigDecimal HR_LTA = shiftIndicators[5]; // Late Arrivals (Shift_LTA)
		BigDecimal HR_EDE = shiftIndicators[6]; // Early Departure (Shift_EDE)
		BigDecimal HR_HER = shiftIndicators[7]; // Extra Clock Hours (Shift_HER)
		BigDecimal HR_HEF = shiftIndicators[8];	// Extra Holiday Hours 			(Shift_HEF)
		// Verify if Holliday
		if (MAMN_NonBusinessDay.isHoliday(p_Event_Date, p_AD_Client_ID, p_AD_Org_ID)) {
			attendancehours.setIsNonBusinessDay(true);
			HR_HEF = HR_HC;
		}

		BigDecimal HR_HNO = shiftIndicators[9];	// Additional Nigth Hours 		(Shift_HNO)
		BigDecimal HR_HEA = BigDecimal.ZERO;	// Authorized Extra Hours		(Shift_HEA)
	    // *********************************************************
	    // ******* 🔟 Asignar resultados
	    // *********************************************************
	    attendancehours.setShift_HND(Shift_HND);
	    attendancehours.setShift_HNN(Shift_HNN);
	    attendancehours.setShift_HED(Shift_HED);
	    attendancehours.setShift_HEN(Shift_HEN);
	    attendancehours.setDescription(strMessage.toString());
		// Set shiftIndicators VARs
		attendancehours.setShift_HT(HR_HT) ;		// Total Work Hours				(Shift_HT)
		attendancehours.setShift_HC(HR_HC);        	// Complete Hours 				(Shift_HC)
		attendancehours.setShift_HLGT15(HR_HLGT15); // Free Hours Greater than 15 	(Shift_HLGT15)
		attendancehours.setShift_HLLT15(HR_HLLT15); // Free Hours Less than 15 		(Shift_HLLT15)
		attendancehours.setShift_THL(HR_THL);      	// Free Hours 					(Shift_THL)
		attendancehours.setShift_LTA(HR_LTA);      	// Late Arrivals 				(Shift_LTA)
		attendancehours.setShift_EDE(HR_EDE);      	// Early Departure 				(Shift_EDE)
		attendancehours.setShift_HER(HR_HER);      	// Extra Clock Hours 			(Shift_HER)
		attendancehours.setShift_HEF(HR_HEF);      	// Extra Holiday Hours 			(Shift_HEF)
		// HNO, HEA
		attendancehours.setShift_HNO(HR_HNO);      	// Additional Nigth Hours 		(Shift_HNO)
		attendancehours.setShift_HEA(HR_HEA);      	// Authorized Extra Hours		(Shift_HEA)
	    // *********************************************************
	    // ******* 11️⃣ Cálculos adicionales
	    // *********************************************************
	    //calcAttendanceValuesofPayrollVarsAdditional(ctx, p_AD_Client_ID, p_AD_Org_ID, p_C_Country_ID, amnshiftdetail, attendancehours);

	    return attendancehours;
	}

	

	/**
	 * Procedure: calcDefaultAttendanceValuesofPayrollVars
	 * Description: Calculates Values of HND,HNN,HED,HEN,Attendance,AttendanceBonus
	 * 				From AMN_Shift_detail  Values 
	 * 				( p_amnshiftdetail ---> p_Shift_In1, p_Shift_Out1, p_Shift_In2, p_Shift_Out2)
	 * @param Timestamp Event_Date
	 * @param Integer p_AMN_Shift_ID
	 * @param MAMN_Shift_Detail p_amnshiftdetail
	 * @return AttendanceHours Array
	 * All event Times are Normalized to Event_Date
	 */
	private static MAMN_Payroll_Assist_Proc calcDefaultAttendanceValuesofPayrollVars(
			Properties ctx, int p_AD_Client_ID, int p_AD_Org_ID, int p_C_Country_ID, 
			Timestamp p_Event_Date, Integer p_AMN_Shift_ID, MAMN_Shift_Detail amnshiftdetail)
	{
		Boolean bParamEmpty=false;
		Boolean bDescanso=false;
		String strMessage="";
		BigDecimal BDZero = BigDecimal.valueOf(0);
		MAMN_Payroll_Assist_Proc defattendancehours = new  MAMN_Payroll_Assist_Proc(Env.getCtx(), null);
		defattendancehours.setAMN_Shift_ID(p_AMN_Shift_ID);
		
		// Determines Default Shift Times Values
	    //log.warning("Shift_In1:"+amnshiftdetail.getShift_In1().toString());
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
	    // Get Default Asisst Input-Output Times referenced to p_Event_date
		Timestamp defShift_In1 = getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_In1());;
		Timestamp defShift_In2 = getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_In2());;
		Timestamp defShift_Out1 = getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_Out1());
		Timestamp defShift_Out2 = getTimestampShifdetailEventTime(p_Event_Date,amnshiftdetail.getShift_Out2());		
		bDescanso=amnshiftdetail.isDescanso();
		// Verify if Any Associated Shift Time is null
		if ( defShift_In1 == null || defShift_Out1 == null || defShift_In2 == null || defShift_Out2 == null) {
			bParamEmpty=true;
			if (!bDescanso) {
				strMessage=strMessage+Msg.getMsg(Env.getCtx(), "Error")+"11 "+
						Msg.getElement(Env.getCtx(), "AMN_Shift_ID")+" "+
						Msg.getMsg(Env.getCtx(), "default")+" "+Msg.getMsg(Env.getCtx(), "found.none")+"\n";
			} else { 
				strMessage=strMessage+Msg.getElement(Env.getCtx(),"Descanso"+" ");
			}
		}
		if (bParamEmpty) {
			defattendancehours.setDescription(strMessage);
			defattendancehours.setShift_HND(BDZero);
			defattendancehours.setShift_HED(BDZero);
			defattendancehours.setShift_HNN(BDZero);
			defattendancehours.setShift_HEN(BDZero);	
			return defattendancehours;
		}
		// Local Variables Constants
		GregorianCalendar dCal = new GregorianCalendar();
		dCal.setTime(p_Event_Date);
		// Zero 00:00
		Calendar zeroCal = getZeroCalendar(p_Event_Date);
		// Day Start al 05:00 
		Calendar dayCal = getDayCalendar(p_C_Country_ID,p_Event_Date );
		long ldayHr = secondsOfTime(dayCal, zeroCal);
		// Night Start at 19:00
		Calendar nightCal = getNightCalendar(p_C_Country_ID,p_Event_Date);
		long lnightHr = secondsOfTime(nightCal, zeroCal);
		//
		BigDecimal defShift_HED = BigDecimal.valueOf(0.00);
		BigDecimal defShift_HEN = BigDecimal.valueOf(0.00);
		BigDecimal defShift_HND = BigDecimal.valueOf(0.00);
		BigDecimal defShift_HNN = BigDecimal.valueOf(0.00); 
		BigDecimal defShift_Attendance = BDZero;
		BigDecimal defShift_AttendanceBonus = BDZero;
		long ldefShift_HED = (long) 0;
		long ldefShift_HEN = (long) 0;
		long ldefShift_HND = (long) 0;
		long ldefShift_HNN = (long) 0;
		long ldefShift_Attendance = (long) 0;
		long ldefShift_AttendanceBonus = (long) 0;
		// DEFAULT SHIFT Asisst Input-Output Times VALUES
		// defShift_In1
		Calendar caldefShift_In1 = Calendar.getInstance();
		caldefShift_In1.setTime(defShift_In1);
		long ldefShift_In1=secondsOfTime(caldefShift_In1, zeroCal);
		// defShift_Out1
		Calendar caldefShift_Out1 = Calendar.getInstance();
		caldefShift_Out1.setTime(defShift_Out1);
		long ldefShift_Out1=secondsOfTime(caldefShift_Out1, zeroCal);
		// defShift_In2
		Calendar caldefShift_In2 = Calendar.getInstance();
		caldefShift_In2.setTime(defShift_In2);
		long ldefShift_In2=secondsOfTime(caldefShift_In2, zeroCal);
		// defShift_Out2
		Calendar caldefShift_Out2 = Calendar.getInstance();
		caldefShift_Out2.setTime(defShift_Out2);
		long ldefShift_Out2=secondsOfTime(caldefShift_Out2, zeroCal);
		// FIRST SHIFT Shift_In1 to Shift_Out1
		if ((ldefShift_Out1 -ldefShift_In1 ) > 0 ) {
			// Verify Shift Start Before DayStart
			if (( ldefShift_In1 - ldayHr) < 0) {
				ldefShift_HNN = ldefShift_HNN + (ldayHr - ldefShift_In1 );	
				ldefShift_HND = ldefShift_HND + (ldefShift_Out1 - ldayHr );			
			} else {
				// Verify if Out After Night Start
				if (( ldefShift_Out1 - lnightHr) < 0) {	
					ldefShift_HND = ldefShift_HND + (ldefShift_Out1 - ldefShift_In1 );
				} else {
					ldefShift_HND = ldefShift_HND + (lnightHr - ldefShift_In1 );
					ldefShift_HNN = ldefShift_HNN + (ldefShift_Out1 - lnightHr);
				}
			}
		}
		// SECOND SHIFT Shift_In2 to Shift_Out2
		if ((ldefShift_Out2 - ldefShift_In2 ) > 0 ) {
			// Verify Shift Start Before DayStart
			if (( ldefShift_In2 - ldayHr) < 0) {
				// Verifiy if Out After Night Start
				if (( ldefShift_Out2 - lnightHr) < 0) {	
					ldefShift_HND = ldefShift_HND + (ldefShift_Out2 - ldefShift_In2 );
				} else {
					ldefShift_HND = ldefShift_HND + (lnightHr - ldefShift_In2 );
					ldefShift_HNN = ldefShift_HNN + (ldefShift_Out2 - lnightHr );
				}			
			} else {
				// Verifiy if Out After Night Start
				if (( ldefShift_Out2 - lnightHr) < 0) {	
					ldefShift_HND = ldefShift_HND + (ldefShift_Out2 - ldefShift_In2 );
				} else {
					ldefShift_HND = ldefShift_HND + (lnightHr - ldefShift_In2 );
					ldefShift_HNN = ldefShift_HNN + (ldefShift_Out2 - lnightHr );
				}				
			}
		}	
		// Convert to BigDecimal
		defShift_HED = BigDecimal.valueOf((long) (ldefShift_HED));
		defShift_HEN = BigDecimal.valueOf((long) (ldefShift_HEN));
		defShift_HND = BigDecimal.valueOf((long) (ldefShift_HND));
		defShift_HNN = BigDecimal.valueOf((long) (ldefShift_HNN));
		defShift_Attendance =  BigDecimal.valueOf((long) (ldefShift_Attendance));
		defShift_AttendanceBonus =  BigDecimal.valueOf((long) (ldefShift_AttendanceBonus));	
		
		//log.warning("Calculated Hours in Seconds long HND:"+ldefShift_HND +"  HED:"+ldefShift_HED+"  HNN:"+ldefShift_HNN+"  HEN:"+ldefShift_HEN);
		// DIVIDE : 3600 to GET SECONDS
		// Convert to HOURS
		BigDecimal BD3600=BigDecimal.valueOf((long)3600);
		int precision = 4;
		defShift_HED =  defShift_HED.divide(BD3600,precision, RoundingMode.CEILING);
		defShift_HEN =  defShift_HEN.divide(BD3600,precision, RoundingMode.CEILING);
		defShift_HND =  defShift_HND.divide(BD3600,precision, RoundingMode.CEILING);
		defShift_HNN =  defShift_HNN.divide(BD3600,precision, RoundingMode.CEILING);
		defShift_Attendance = defShift_Attendance.divide(BigDecimal.valueOf((long)2),precision, RoundingMode.CEILING);
		defShift_AttendanceBonus = defShift_AttendanceBonus.divide(BigDecimal.valueOf((long)2),precision, RoundingMode.CEILING);
		// Normalize Values According to Organization Policies
		
		// Return Structure with Maximum Values
		defattendancehours.setShift_HND(defShift_HND);
		defattendancehours.setShift_HED(defShift_HED);
		defattendancehours.setShift_HNN(defShift_HNN);
		defattendancehours.setShift_HEN(defShift_HEN);	
		defattendancehours.setShift_Attendance(defShift_Attendance);	
		defattendancehours.setShift_AttendanceBonus(defShift_AttendanceBonus);
		defattendancehours.setDescription("HND:"+defShift_HND+" HED:"+defShift_HED+" HNN:"+defShift_HNN+" HED:"+defShift_HEN);
		//log.warning("Default Hours Calculated By SHIFT BigDecimal HND:"+defattendancehours.getHR_HND() +"  HED:"+defattendancehours.getHR_HED()+
		//"  HNN:"+defattendancehours.getHR_HNN()+"  HEN:"+defattendancehours.getHR_HEN());
		return defattendancehours;
	}

	
	/**
	 * Procedure: getTimestampShifdetailEventTime
	 * Return Timestamp Concatenated from Event_date + Event_time 
	 * @param p_Event_Date (Timestamp event Date)
	 * @param p_Event_Time (Timestamp event Time)
	 * @return Timestamp
	 */
	public static  Timestamp getTimestampShifdetailEventTime(Timestamp p_Event_Date, Timestamp p_Event_Time)
	{
		
		// Set AssistDate Var
		GregorianCalendar dCal = new GregorianCalendar();
		Timestamp retValue=null;
		if (p_Event_Date == null) {
			retValue=null;
			return retValue;
		}
		dCal.setTime(p_Event_Date);
		// Set AssistTime Var
		Calendar tCal = Calendar.getInstance();
		// Verify if p_Event_Time is null
		if (p_Event_Time == null) {
			tCal.setTime(p_Event_Date);
			dCal.set(Calendar.HOUR_OF_DAY, 0);
			dCal.set(Calendar.MINUTE, 0);
			dCal.set(Calendar.SECOND, 0);
			dCal.set(Calendar.MILLISECOND, 0);
			retValue=null;
		} else {
			tCal.setTime(p_Event_Time);
			// Set AssistTime Values to AssistDate Var
			dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
			dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
			dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
			dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
			retValue=new Timestamp(dCal.getTimeInMillis());
		}		 
		return retValue;
		
	}

	/**
	 * Procedure: minutesOfTime
	 * @param Calendar p_cal
	 * @param Calendar p_zero
	 * @return long
	 * Return Seconds of Day
	 */
	private static long secondsOfTime( Calendar p_cal, Calendar p_zero)
	{
		long lretValue=0;
		lretValue = (p_cal.getTimeInMillis()-p_zero.getTimeInMillis())/1000;
		return lretValue;
		
	}
	
	/**
	 * 
	 * @param p_Event_Date
	 * @return
	 */
	private static Calendar getZeroCalendar(Timestamp p_Event_Date) {
		// Zero 00:00
		Calendar zeroCal = Calendar.getInstance();
		zeroCal.setTime(p_Event_Date);
		zeroCal.set(Calendar.HOUR_OF_DAY,0);
		zeroCal.set(Calendar.MINUTE, 0);
		zeroCal.set(Calendar.SECOND, 0);
		zeroCal.set(Calendar.MILLISECOND, 0);
		return zeroCal;
		
	}
	/**
 		100 USA Default 	Day 06:00 to 19:59
		276 Paraguay		Day 06:00 to 19:59
		339 Venezuela 		Day 05:00 to 18:59
	 * @param p_C_Country_ID
	 * @param p_Event_Date
	 * @return
	 */
	private static Calendar getDayCalendar(int p_C_Country_ID, Timestamp p_Event_Date) {
		
		// Day Start Different Countries
		Calendar dayCal = Calendar.getInstance();
		dayCal.setTime(p_Event_Date);
		if (p_C_Country_ID == 100) 		// USA
			dayCal.set(Calendar.HOUR_OF_DAY, 6);
		else if (p_C_Country_ID == 276) // Paraguay
			dayCal.set(Calendar.HOUR_OF_DAY, 6);
		else if (p_C_Country_ID == 339)	// Venezuela
			dayCal.set(Calendar.HOUR_OF_DAY, 5);
		else 
			dayCal.set(Calendar.HOUR_OF_DAY, 6);
		dayCal.set(Calendar.MINUTE, 0);
		dayCal.set(Calendar.SECOND, 0);
		dayCal.set(Calendar.MILLISECOND, 0);
		return dayCal;
	}
	
	/**
	 * getNightCalendar
		100 USA Default 	Day 06:00 to 19:59
		276 Paraguay		Day 06:00 to 19:59
		339 Venezuela 		Day 05:00 to 18:59
	 * @param p_C_Country_ID
	 * @param p_Event_Date
	 * @return
	 */
	private static Calendar getNightCalendar(int p_C_Country_ID, Timestamp p_Event_Date) {
		// Night Start at 19:00
		Calendar nightCal = Calendar.getInstance();
		nightCal.setTime(p_Event_Date);
		if (p_C_Country_ID == 100) 		// USA
			nightCal.set(Calendar.HOUR_OF_DAY, 19);
		else if (p_C_Country_ID == 276) // Paraguay
			nightCal.set(Calendar.HOUR_OF_DAY, 20);
		else if (p_C_Country_ID == 339)	// Venezuela
			nightCal.set(Calendar.HOUR_OF_DAY, 19);
		else 
			nightCal.set(Calendar.HOUR_OF_DAY, 19);
		nightCal.set(Calendar.MINUTE, 0);
		nightCal.set(Calendar.SECOND, 0);
		nightCal.set(Calendar.MILLISECOND, 0);
		return nightCal;

	}
	

	/**
	 * calcHNO
	 * @param shiftIn
	 * @param shiftOut
	 * @param nightStart
	 * @return
	 */
	private static long calcHNO(long shiftIn, long shiftOut, long nightStart) {
	    if (shiftOut <= shiftIn) return 0;
	    if (shiftOut > nightStart) return shiftOut - Math.max(shiftIn, nightStart);
	    return 0;
	}

	/**
	 * Convierte un Timestamp en segundos desde la medianoche del día base
	 */
	private static long toSeconds(Timestamp ts, Calendar zeroCal) {
	    if (ts == null) return 0;
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(ts);
	    return secondsOfTime(cal, zeroCal);
	}

	/**
	 * Normaliza los valores de horas en MAMN_Shift_Detail para evitar nulos
	 */
	private static void normalizeShiftTimes(MAMN_Shift_Detail shiftdetail) {
	    if (shiftdetail.getShift_In1() == null) shiftdetail.setShift_In1(Timestamp.valueOf("1970-01-01 00:00:00"));
	    if (shiftdetail.getShift_Out1() == null) shiftdetail.setShift_Out1(Timestamp.valueOf("1970-01-01 00:00:00"));
	    if (shiftdetail.getShift_In2() == null) shiftdetail.setShift_In2(Timestamp.valueOf("1970-01-01 00:00:00"));
	    if (shiftdetail.getShift_Out2() == null) shiftdetail.setShift_Out2(Timestamp.valueOf("1970-01-01 00:00:00"));
	}

	/**
	 * Calcula todas las horas de un turno completo o parcial (2 o 4 marcaciones).
	 * @param lShift_In1 Entrada primer tramo
	 * @param lShift_Out1 Salida primer tramo
	 * @param lShift_In2 Entrada segundo tramo
	 * @param lShift_Out2 Salida segundo tramo
	 * @param ldefShift_In1 Entrada por defecto primer tramo
	 * @param ldefShift_Out1 Salida por defecto primer tramo
	 * @param ldefShift_In2 Entrada por defecto segundo tramo
	 * @param ldefShift_Out2 Salida por defecto segundo tramo
	 * @param ldayHr Inicio del día (segundos)
	 * @param lnightHr Inicio de la noche (segundos)
	 * @param lHalfDayMin Segundos mínimos para asistencia parcial
	 * @param breakTimeSeconds Segundos esperados de descanso
	 * @return BigDecimal[] Totales: [0]=HND, [1]=HNN, [2]=HED, [3]=HEN, [4]=Attendance, [5]=AttendanceBonus
	 */
	private static BigDecimal[] calcShiftTotals(
	        long lShift_In1, long lShift_Out1,
	        long lShift_In2, long lShift_Out2,
	        long ldefShift_In1, long ldefShift_Out1,
	        long ldefShift_In2, long ldefShift_Out2,
	        long ldayHr, long lnightHr,
	        long breakTimeSeconds
	) {
	    BigDecimal[] totals = new BigDecimal[6]; // HND,HNN,HED,HEN,Attendance,AttendanceBonus
	    for (int i = 0; i < totals.length; i++) {
	        totals[i] = BigDecimal.ZERO;
	    }

	    // Detectar si hay 2 o 4 marcaciones
	    boolean fourMarks = (lShift_In1 > 0 && lShift_Out1 > 0 && lShift_In2 > 0 && lShift_Out2 > 0);
	    boolean twoMarks  = !fourMarks && (lShift_In1 > 0 && lShift_Out1 > 0 );

	    if (fourMarks) {
	        // ----- CASO 4 MARCACIONES -----
	        long[] firstHalf  = calcShiftHours(lShift_In1, lShift_Out1, ldefShift_In1, ldefShift_Out1,
	                                           ldayHr, lnightHr, new long[6]);
	        long[] secondHalf = calcShiftHours(lShift_In2, lShift_Out2, ldefShift_In2, ldefShift_Out2,
	                                           ldayHr, lnightHr, new long[6]);

	        // sumar resultados de ambas mitades
	        for (int i = 0; i < totals.length; i++) {
	            totals[i] = totals[i].add(BigDecimal.valueOf(firstHalf[i]))
	                                 .add(BigDecimal.valueOf(secondHalf[i]));
	        }

	        // Ajuste por break real
	        long breakReal = lShift_In2 - lShift_Out1;
	        if (breakReal > 0) {
	            if (breakReal > breakTimeSeconds) {
	                // se descuenta exceso de descanso
	                totals[4] = totals[4].subtract(BigDecimal.valueOf(breakReal - breakTimeSeconds));
	            } else if (breakReal < breakTimeSeconds) {
	                // falta de descanso se toma como extra (HED)
	                totals[2] = totals[2].add(BigDecimal.valueOf(breakTimeSeconds - breakReal));
	            }
	        }
	        // 🔹 Calcular horas extras (2=HED, 3=HEN)
	        long planned = ldefShift_Out2  - ldefShift_In1;
	        long realWorked = totals[4].longValue();
	        if (realWorked > planned) {
	            long extra = realWorked - planned;

	            // asumimos exceso al final de la jornada (OUT2 > defOut2)
	            if (lShift_Out2 > ldefShift_Out2) {
	                long extraStart = ldefShift_Out2;
	                long extraEnd   = lShift_Out2;

	                // recorrer exceso en dos partes: día (ldayHr → lnightHr) y noche
	                if (extraStart < lnightHr) {
	                    long endDay = Math.min(extraEnd, lnightHr);
	                    long extraDay = endDay - extraStart;
	                    if (extraDay > 0) {
	                        totals[2] = totals[2].add(BigDecimal.valueOf(extraDay)); // HED
	                        extraStart = endDay;
	                    }
	                }
	                // lo que sobra después de lnightHr es nocturno
	                if (extraStart < extraEnd) {
	                    long extraNight = extraEnd - extraStart;
	                    totals[3] = totals[3].add(BigDecimal.valueOf(extraNight)); // HEN
	                }
	            }

	            // también puede haber exceso al inicio (IN1 < defIn1)
	            if (lShift_In1 < ldefShift_In1) {
	                long extraStart = lShift_In1;
	                long extraEnd   = ldefShift_In1;

	                // primero nocturno (si empieza antes del día)
	                if (extraStart < ldayHr) {
	                    long endNight = Math.min(extraEnd, ldayHr);
	                    long extraNight = endNight - extraStart;
	                    if (extraNight > 0) {
	                        totals[3] = totals[3].add(BigDecimal.valueOf(extraNight)); // HEN
	                        extraStart = endNight;
	                    }
	                }
	                // luego diurno hasta defIn1
	                if (extraStart < extraEnd) {
	                    long extraDay = extraEnd - extraStart;
	                    totals[2] = totals[2].add(BigDecimal.valueOf(extraDay)); // HED
	                }
	            }
	        }
	        
	    } else if (twoMarks) {
	        // ----- CASO 2 MARCACIONES (IN1 / OUT1) -----
	        long worked = lShift_Out1 - lShift_In1;
	        if (worked > 0) {
	        	 // 1) Normales (HND/HNN) + asistencia bruta con tu calcShiftHours
	            long[] partials = calcShiftHours(lShift_In1, lShift_Out1, ldefShift_In1, ldefShift_Out2,
	                                             ldayHr, lnightHr, new long[6]);
	            for (int i = 0; i < totals.length; i++) {
	                totals[i] = totals[i].add(BigDecimal.valueOf(partials[i]));
	            }
	            
	            // 2) Extras por entrada anticipada (IN1 < defIn1) -> repartir día/noche
	            if (lShift_In1 < ldefShift_In1) {
	                long extraStart = lShift_In1;
	                long extraEnd   = ldefShift_In1;

	                // tramo nocturno hasta inicio del día
	                if (extraStart < ldayHr) {
	                    long endNight   = Math.min(extraEnd, ldayHr);
	                    long extraNight = endNight - extraStart;
	                    if (extraNight > 0) {
	                        totals[3] = totals[3].add(BigDecimal.valueOf(extraNight)); // HEN
	                        extraStart = endNight;
	                    }
	                }
	                // tramo diurno hasta defIn1 (y, si quedara, podrías extender lógica)
	                if (extraStart < extraEnd) {
	                    long endDay  = Math.min(extraEnd, lnightHr);
	                    long extraDay = endDay - extraStart;
	                    if (extraDay > 0) {
	                        totals[2] = totals[2].add(BigDecimal.valueOf(extraDay)); // HED
	                        extraStart = endDay;
	                    }
	                }
	                // si aún quedara tramo después de lnightHr sería nocturno
	                if (extraStart < extraEnd) {
	                    long extraNight2 = extraEnd - extraStart;
	                    if (extraNight2 > 0) {
	                        totals[3] = totals[3].add(BigDecimal.valueOf(extraNight2)); // HEN
	                    }
	                }
	            }

	            // 3) Extras por salida tardía (OUT1 > defOut2) -> repartir día/noche
	            if (lShift_Out1 > ldefShift_Out2) {
	                long extraStart = ldefShift_Out2;
	                long extraEnd   = lShift_Out1;

	                // tramo diurno hasta inicio de la noche
	                if (extraStart < lnightHr) {
	                    long endDay  = Math.min(extraEnd, lnightHr);
	                    long extraDay = endDay - extraStart;
	                    if (extraDay > 0) {
	                        totals[2] = totals[2].add(BigDecimal.valueOf(extraDay)); // HED
	                        extraStart = endDay;
	                    }
	                }
	                // lo que reste después de lnightHr es nocturno
	                if (extraStart < extraEnd) {
	                    long extraNight = extraEnd - extraStart;
	                    if (extraNight > 0) {
	                        totals[3] = totals[3].add(BigDecimal.valueOf(extraNight)); // HEN
	                    }
	                }
	            }

	            // 4) Asistencia neta: descontar descanso esperado
	            worked -= breakTimeSeconds;
	            if (worked < 0) worked = 0;
	            totals[4] = BigDecimal.valueOf(worked);

	            // 5) Falta total de descanso (porque no hay pausa intermedia)
	            //    -> lo asignamos como horas normales (según tu criterio: todo a HND)
	            if (breakTimeSeconds > 0) {
	                totals[0]= totals[0].add(BigDecimal.valueOf(breakTimeSeconds)); // HED
	            }
	        }
	    }
	    return totals;
	}

	/**
	 * Calcula horas normales y nocturnas dentro de un turno
	 * [0]=HND, [1]=HNN, [2]=HED, [3]=HEN, [4]=Attendance, [5]=AttendanceBonus
	 */
	private static long[] calcShiftHours(long in, long out, long defIn, long defOut,
	        long ldayHr, long lnightHr,
	        long[] totals) {

	    long worked = out - in;
	    if (worked <= 0) return totals;

	    long cursor = in;

	    // 1) Tramo nocturno antes del día (0 → ldayHr)
	    if (cursor < ldayHr) {
	        long endNight = Math.min(out, ldayHr);
	        long seg = endNight - cursor;
	        if (seg > 0) totals[1] += seg; // HNN
	        cursor = endNight;
	    }

	    // 2) Tramo diurno (ldayHr → lnightHr)
	    if (cursor < lnightHr && cursor < out) {
	        long endDay = Math.min(out, lnightHr);
	        long seg = endDay - cursor;
	        if (seg > 0) totals[0] += seg; // HND
	        cursor = endDay;
	    }

	    // 3) Tramo nocturno después del día (lnightHr → fin)
	    if (cursor < out) {
	        long seg = out - cursor;
	        if (seg > 0) totals[1] += seg; // HNN
	    }

	    // 4) Total asistencia
	    totals[4] += worked;

	    return totals;
	}


	
	/**
	 * calcShiftIndicators
	 * Calcula indicadores de un turno según marcaciones y horas por defecto.
	 * @param lShift_In1  Entrada primer tramo
	 * @param lShift_Out1 Salida primer tramo
	 * @param lShift_In2  Entrada segundo tramo
	 * @param lShift_Out2 Salida segundo tramo
	 * @param ldefShift_In1 Entrada por defecto primer tramo
	 * @param ldefShift_Out1 Salida por defecto primer tramo
	 * @param ldefShift_In2 Entrada por defecto segundo tramo
	 * @param ldefShift_Out2 Salida por defecto segundo tramo
	 * @param precision Decimales a usar en los cálculos
	 * @return BigDecimal[] shiftIndicators: 
	 *         [0]=HC, [1]=THL, [2]=HLGT15, [3]=HLLT15, [4]=LTA, [5]=EDE
	 * Indicadores:
	 * 0 = HT   Total Work Hours
	 * 1 = HC   Complete Hours
	 * 2 = THL  Free Hours
	 * 3 = HLGT15 Free Hours > 15 min
	 * 4 = HLLT15 Free Hours <= 15 min
	 * 5 = LTA  Late Arrivals
	 * 6 = EDE  Early Departure
	 * 7 = HER  Extra Clock Hours
	 * 8 = HEF  Extra Holiday Hours
	 * 9 = HNO  Additional Night Hours
	 */
	private static BigDecimal[] calcShiftIndicators(
	        long lShift_In1, long lShift_Out1, long lShift_In2, long lShift_Out2,
	        long ldefShift_In1, long ldefShift_Out1,
	        long ldefShift_In2, long ldefShift_Out2,
	        long ldayHr, long lnightHr,
	        int precision ) {
		
	    final BigDecimal BD3600 = new BigDecimal("3600");
	    BigDecimal[] shiftIndicators = new BigDecimal[11];
	    for (int i = 0; i < shiftIndicators.length; i++) {
	        shiftIndicators[i] = BigDecimal.ZERO;
	    }

		// Dos marcaciones o cuatro
		boolean fourMarks = (lShift_In1 > 0 && lShift_Out1 > 0 && lShift_In2 > 0 && lShift_Out2 > 0);
		boolean twoMarks  = !fourMarks && (lShift_In1 > 0 && lShift_Out1 > 0 );

		// ====== CASO 4 MARCACIONES ======
	    if (fourMarks) {
		    // 0 - HT: Total Work Hours (dos mitades)
	        if ((lShift_Out1 - lShift_In1) > 0)
	            shiftIndicators[0] = shiftIndicators[0].add(BigDecimal.valueOf(lShift_Out1 - lShift_In1).divide(BD3600, precision, RoundingMode.CEILING));
	        if ((lShift_Out2 - lShift_In2) > 0)
	            shiftIndicators[0] = shiftIndicators[0].add(BigDecimal.valueOf(lShift_Out2 - lShift_In2).divide(BD3600, precision, RoundingMode.CEILING));
	
		    // 1 - HC: Complete Hours
	        // HC: tiempo continuo desde IN1 hasta OUT2
	        if ((lShift_Out2 - lShift_In1) > 0)
	            shiftIndicators[1] = BigDecimal.valueOf(lShift_Out2 - lShift_In1).divide(BD3600, precision, RoundingMode.CEILING);
	
		    // 2 - THL: Free Hours descansos entre OUT1 e IN2
	        if ((lShift_In2 - lShift_Out1) > 0) {
	            shiftIndicators[2] = BigDecimal.valueOf(lShift_In2 - lShift_Out1).divide(BD3600, precision, RoundingMode.CEILING);
	            BigDecimal THL_InMinutes = shiftIndicators[2].multiply(new BigDecimal("60"));
	            if (THL_InMinutes.compareTo(new BigDecimal("15")) > 0) {
	                shiftIndicators[3] = shiftIndicators[2]; // HLGT15
	            } else {
	                shiftIndicators[4] = shiftIndicators[2]; // HLLT15
	            }
	        }
	
		    // 3 / 4 - HLGT15 / HLLT15
		    if (shiftIndicators[2].compareTo(BigDecimal.ZERO) > 0) {
		        BigDecimal THL_InMinutes = shiftIndicators[2].multiply(new BigDecimal("60"));
		        if (THL_InMinutes.compareTo(new BigDecimal("15")) > 0) {
		            shiftIndicators[3] = shiftIndicators[2]; // HLGT15
		        } else {
		            shiftIndicators[4] = shiftIndicators[2]; // HLLT15
		        }
		    }

		    // 5 - LTA: Late Arrivals
		    if (lShift_In1 != 0 && ldefShift_In1 != 0) {
		        if ((lShift_In1 -ldefShift_In1) >= 0) {
		            shiftIndicators[5] = BigDecimal.valueOf(lShift_In1 -ldefShift_In1)
		                    .divide(BD3600, precision, RoundingMode.CEILING);
		        }
		    }
	
		    // 6 - EDE: Early Departure
		    if (lShift_Out2 != 0 && ldefShift_Out2 != 0) {
		        if ((ldefShift_Out2 - lShift_Out2) >= 0) {
		            shiftIndicators[6] = BigDecimal.valueOf(ldefShift_Out2 - lShift_Out2)
		                    .divide(BD3600, precision, RoundingMode.CEILING);
		        }
		    }
	
		    // 7 - HER: Extra Clock Hours (a definir, por ahora en cero)
		    shiftIndicators[7] = BigDecimal.ZERO;
	
		    // 8 - HEF: Extra Holiday Hours (a definir, por ahora en cero)
		    shiftIndicators[8] = BigDecimal.ZERO;
	
		    // 9 - HNO: Additional Night Hours (ejemplo: calcular si OUT2 pasa de nightHr)
		    if (lShift_Out2 > lnightHr) {
		        long extraNight = lShift_Out2 - lnightHr;
		        shiftIndicators[9] = BigDecimal.valueOf(extraNight)
		                .divide(BD3600, precision, RoundingMode.CEILING);
		    }
	    }
	    // ====== CASO 2 MARCACIONES ======
	    // Solo lShift_In1  y lShift_Out1
	    else if (twoMarks) {
	    	// 0 - HT: Total Work Hours 
	        // HT solo con primera franja desde IN1 a OUT1
	        if ((lShift_Out1 - lShift_In1) > 0)
	            shiftIndicators[0] = BigDecimal.valueOf(lShift_Out1 - lShift_In1).divide(BD3600, precision, RoundingMode.CEILING);

	        // 1 - HC: Complete Hours, tiempo total = Out1 - In1 
	        // HC: lo mismo porque no hay segundo bloque
	        shiftIndicators[1] = shiftIndicators[0];
	        
	        // THL = 0 (no existe descanso intermedio)
	        shiftIndicators[2] = BigDecimal.ZERO;
	        
	        // THL, HLGT15, HLLT15 = 0 (no aplica con 2 marcaciones)
		    // 3 / 4 - HLGT15 / HLLT15  ** NO APLICAN **
	        
		    // 5 - LTA: Late Arrivals
		    if (lShift_In1 != 0 && ldefShift_In1 != 0) {
		        if ((lShift_In1 -ldefShift_In1) >= 0) {
		            shiftIndicators[5] = BigDecimal.valueOf(lShift_In1 -ldefShift_In1)
		                    .divide(BD3600, precision, RoundingMode.CEILING);
		        }
		    }
		    
		    // 6 - EDE: Early Departure
		    if (lShift_Out1 != 0 && ldefShift_Out2 != 0) {
		        if ((ldefShift_Out2 - lShift_Out1) >= 0) {
		            shiftIndicators[6] = BigDecimal.valueOf(ldefShift_Out2 - lShift_Out2)
		                    .divide(BD3600, precision, RoundingMode.CEILING);
		        }
		    }
		    // 7 - HER: Extra Clock Hours (a definir, por ahora en cero)
		    shiftIndicators[7] = BigDecimal.ZERO;
	
		    // 8 - HEF: Extra Holiday Hours (a definir, por ahora en cero)
		    shiftIndicators[8] = BigDecimal.ZERO;
	
		    // 9 - HNO: Additional Night Hours (ejemplo: calcular si OUT2 pasa de nightHr)
		    if (lShift_Out1 > lnightHr) {
		        long extraNight = lShift_Out1 - lnightHr;
		        shiftIndicators[9] = BigDecimal.valueOf(extraNight)
		                .divide(BD3600, precision, RoundingMode.CEILING);
		    }
	    }
	    return shiftIndicators;
	}

}
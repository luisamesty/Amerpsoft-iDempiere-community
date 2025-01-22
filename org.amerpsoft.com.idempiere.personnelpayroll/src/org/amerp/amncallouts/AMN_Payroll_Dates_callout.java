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
 ******************************************************************************/
package org.amerp.amncallouts;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.script.ScriptException;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.*;
import org.amerp.amnutilities.AmerpUtilities;
import org.compiere.model.*;
import org.compiere.util.CLogger;
import org.compiere.util.Msg;

/**
 * @author luisamesty
 *
 */
public class AMN_Payroll_Dates_callout implements IColumnCallout {
	
	CLogger log = CLogger.getCLogger(AMN_Payroll_Dates_callout.class);
	// Main Variables
	String sql;
	// AMN_Employee Attributes Variables
	Integer AD_Client_ID = 0;
	Integer AD_Org_ID=0;
	Integer AMN_Payroll_ID = 0;	
	Integer AMN_Employee_ID = 0;
	Integer AMN_Process_ID =0;
	Integer AMN_Contract_ID=0;
	Integer AMN_Period_ID=0;
	Integer AMN_Shift_ID = 0;
	String PayDocumentNO ="";
	String AMN_Process_Value = "NN";
	Integer elapsedDaysVacation=0;
	Integer elapsedDaysVacationCollective=0;
	Timestamp InvDateIni;
	Timestamp InvDateEnd;
	Timestamp DateReEntry;
	Timestamp vacationPeriodIni = null;
	Timestamp vacationPeriodEnd = null;
	String columnName = "";
	String PayrollDescription="";
	MAMN_Process amnprocess = null;
	MAMN_Employee amnemployee = null;
	String DaysMode = "B";  // Business Days
	// AMN Dates
	Date AMNDateIni;		// AMNDateIni
	Date AMNDateEnd;		// AMNDateEnd
	Timestamp receiptDateReEntry;
	Timestamp receiptDateReEntryReal;
	Timestamp receiptDateApplication;
	boolean isModified = false;
	boolean updateRec = true;
	boolean overrideCalc = false;
	boolean isSaturdayBusinessDay = false;
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
    @Override
    public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
	    // 
    	//log.warning(".......AMN_Payroll_Dates_callout.......");
    	columnName = p_mField.getColumnName();
		MAMN_Shift shift = new MAMN_Shift();
		AMN_Shift_ID = shift.sqlGetDefaultAMN_Shift_ID(AD_Client_ID);
		if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Payroll_ID) != null) {
			// AMN_Payroll_ID
			AMN_Payroll_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Payroll_ID);
			AMN_Employee_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Employee_ID);
			AMN_Process_ID = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Process_ID);
			AMN_Contract_ID=(Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Contract_ID);
			AMN_Period_ID=(Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_AMN_Period_ID);
			PayDocumentNO=  (String) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DocumentNo);
			amnprocess = new MAMN_Process(p_ctx, AMN_Process_ID, null);
			AMN_Process_Value = amnprocess.getAMN_Process_Value();
			
			overrideCalc = (boolean) p_mTab.getValue(MAMN_Leaves.COLUMNNAME_IsOverrideCalc);
		}
		// Employee - Shift
		if (p_mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Employee_ID)  != null ) {
			AMN_Employee_ID =  (Integer) p_mTab.getValue(MAMN_Leaves.COLUMNNAME_AMN_Employee_ID) ;
			amnemployee = new MAMN_Employee(p_ctx, AMN_Employee_ID, null);
			if (amnemployee != null) {
				AMN_Shift_ID = amnemployee.getAMN_Shift_ID();
				isSaturdayBusinessDay = shift.isSaturdayBusinessDay(AMN_Shift_ID);
			}
		}
		// Business Logic for each Process
		// COMMON BUSINESSLOGIC LOGIC
		// ***************************************
	 	// FieldRef: InvDateIni - (Verify if null)	    		
	 	// ***************************************
	    if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni) != null) {
	    	AMNDateIni=(Date) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni);
			p_mTab.setValue("InvDateIni", AMNDateIni);
	    }
	    // ***************************************
	 	// FieldRef: InvDateEnd - (Verify if null)	    		
	 	// ***************************************
	    if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateEnd) != null) {
	    	AMNDateEnd=(Date) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateEnd);
			p_mTab.setValue("InvDateEnd", AMNDateEnd);
	    }
	    // PROCESS BUSINESSLOGIC LOGIC
	    // Process depending
		if (AMN_Process_Value.equalsIgnoreCase("NN") ||
				AMN_Process_Value.equalsIgnoreCase("TI") ) {
			// ************************
			// Process NN an TI	
			// ************************		
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NV")) {
			// ************************
			// Process NV	
			// ************************
			// _DaysVacation
			if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_DaysVacation)) {
				if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni) != null &&
						!overrideCalc) {
					InvDateIni = (Timestamp) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni);
					elapsedDaysVacation = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacation);
					if (elapsedDaysVacation != null && elapsedDaysVacation > 0) {
						elapsedDaysVacation = elapsedDaysVacation -1;
					}
					elapsedDaysVacationCollective = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacationCollective);
					if(elapsedDaysVacationCollective != null && elapsedDaysVacationCollective >0 )
						elapsedDaysVacationCollective = elapsedDaysVacationCollective -1;
					// SET New Dates
					if (InvDateIni != null) {	
						InvDateEnd = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation), AD_Client_ID, AD_Org_ID);
						receiptDateReEntry = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateEnd,  BigDecimal.ONE, AD_Client_ID, AD_Org_ID);
						receiptDateReEntryReal = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation).subtract(BigDecimal.valueOf(elapsedDaysVacationCollective)), AD_Client_ID, AD_Org_ID);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_InvDateEnd,InvDateEnd);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntry,receiptDateReEntry);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntryReal,receiptDateReEntryReal);
					}
				}
			} else if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_DaysVacationCollective)) {
				// _DaysVacationCollective
				if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni) != null &&
						!overrideCalc) {
					InvDateIni = (Timestamp) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni);
					elapsedDaysVacation = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacation);
					if (elapsedDaysVacation != null && elapsedDaysVacation > 0) {
						elapsedDaysVacation = elapsedDaysVacation -1;
					}
					elapsedDaysVacationCollective = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacationCollective);
					if(elapsedDaysVacationCollective != null && elapsedDaysVacationCollective >0 )
						elapsedDaysVacationCollective = elapsedDaysVacationCollective -1;
					// SET New Dates
					if (InvDateIni != null) {	
						InvDateEnd = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation), AD_Client_ID, AD_Org_ID);
						receiptDateReEntry = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateEnd,  BigDecimal.ONE, AD_Client_ID, AD_Org_ID);
						receiptDateReEntryReal = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation).subtract(BigDecimal.valueOf(elapsedDaysVacationCollective)), AD_Client_ID, AD_Org_ID);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_InvDateEnd,InvDateEnd);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntry,receiptDateReEntry);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntryReal,receiptDateReEntryReal);
					}
				}
				
			} else if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_month) ||
					columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_year)) {
				// month
				// year
				if(p_mTab.getValue(MAMN_Payroll.COLUMNNAME_month) != null &&
						p_mTab.getValue(MAMN_Payroll.COLUMNNAME_year) != null ) { 
					// Dates Vacation
					LocalDateTime localDTEmployee = amnemployee.getincomedate().toLocalDateTime();
					int dia = localDTEmployee.getDayOfMonth();
					int mes = (int) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_month);
					int anio = (int) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_year);
					// Crear fecha1 usando LocalDate
			        LocalDate fecha1 = LocalDate.of(anio, mes, dia);
			        vacationPeriodIni = Timestamp.valueOf(fecha1.atStartOfDay());
			        // Crear fecha2, un año más tarde
			        LocalDate fecha2 = fecha1.plusYears(1);
			        vacationPeriodEnd = Timestamp.valueOf(fecha2.atStartOfDay());
			        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					PayrollDescription=AmerpUtilities.truncate(amnemployee.getValue()+"- "+
							 Msg.translate(p_ctx, "from") + " "+ fecha1.format(formatter) + " " +
							 Msg.translate(p_ctx, "to") + " " + fecha2.format(formatter),255);
			        // UPDATES RefDateIni and RefDateEnd
			        p_mTab.setValue(MAMN_Payroll.COLUMNNAME_RefDateIni,vacationPeriodIni);
			        p_mTab.setValue(MAMN_Payroll.COLUMNNAME_RefDateEnd,vacationPeriodEnd);
			        p_mTab.setValue(MAMN_Payroll.COLUMNNAME_Description, PayrollDescription);
				}
			} else if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_InvDateIni)) {
				// InvDateIni
				if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni) != null &&
						p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacation) != null &&
						!overrideCalc) {
					InvDateIni = (Timestamp) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateIni);
					// Verify if Businessday
					if (!MAMN_NonBusinessDay.isBusinessDay(isSaturdayBusinessDay, InvDateIni, amnemployee.getAD_Client_ID(), amnemployee.getAD_Org_ID() )) {
				            // Lanza un mensaje de error y detiene el proceso
				            throw new IllegalArgumentException(Msg.translate(p_ctx, "InvDateIni") + 
				            		 " - "+ InvDateIni+ " - "+ Msg.getMsg(p_ctx, "NonBusinessDay"));
				    }
					elapsedDaysVacation = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacation);
					if (elapsedDaysVacation != null && elapsedDaysVacation > 0) {
						elapsedDaysVacation = elapsedDaysVacation -1;
					}
					elapsedDaysVacationCollective = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacationCollective);
					if(elapsedDaysVacationCollective != null && elapsedDaysVacationCollective >0 )
						elapsedDaysVacationCollective = elapsedDaysVacationCollective -1;
					// SET New Dates
					if (InvDateIni != null) {	
						InvDateEnd = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation), AD_Client_ID, AD_Org_ID);
						receiptDateReEntry = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateEnd,  BigDecimal.ONE, AD_Client_ID, AD_Org_ID);
						receiptDateReEntryReal = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation).subtract(BigDecimal.valueOf(elapsedDaysVacationCollective)), AD_Client_ID, AD_Org_ID);
						receiptDateApplication = MAMN_NonBusinessDay.getPreviusCalendarDay(InvDateIni,  BigDecimal.valueOf(15),  AD_Client_ID, AD_Org_ID);
						if (!MAMN_NonBusinessDay.isBusinessDay(isSaturdayBusinessDay, receiptDateApplication, AD_Client_ID, AD_Org_ID)) {
							receiptDateApplication = MAMN_NonBusinessDay.getPreviusBusinessDay(isSaturdayBusinessDay, receiptDateApplication,  BigDecimal.ONE, AD_Client_ID, AD_Org_ID);
						}
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_InvDateEnd,InvDateEnd);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntry,receiptDateReEntry);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntryReal,receiptDateReEntryReal);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateApplication,receiptDateApplication);
					}
				}
			} else if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_InvDateEnd)) {
				// InvDateEnd
				if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateEnd) != null &&
						p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacation) != null &&
						!overrideCalc) {
					InvDateEnd = (Timestamp) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_InvDateEnd);
					// Verify if Businessday
					if (!MAMN_NonBusinessDay.isBusinessDay(isSaturdayBusinessDay, InvDateEnd, amnemployee.getAD_Client_ID(), amnemployee.getAD_Org_ID() )) {
				            // Lanza un mensaje de error y detiene el proceso
				            throw new IllegalArgumentException(Msg.translate(p_ctx, "InvDateEnd") + 
				            		 " - "+ InvDateEnd+ " - "+ Msg.getMsg(p_ctx, "NonBusinessDay"));
				    }
					elapsedDaysVacation = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacation);
					if (elapsedDaysVacation != null && elapsedDaysVacation > 0) {
						elapsedDaysVacation = elapsedDaysVacation -1;
					}
					elapsedDaysVacationCollective = (Integer) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_DaysVacationCollective);
					if(elapsedDaysVacationCollective != null && elapsedDaysVacationCollective >0 )
						elapsedDaysVacationCollective = elapsedDaysVacationCollective -1;
					// Get Next Business Day
					if (InvDateEnd != null) {	
						InvDateIni = MAMN_NonBusinessDay.getPreviusBusinessDay(isSaturdayBusinessDay, InvDateEnd, BigDecimal.valueOf(elapsedDaysVacation) , AD_Client_ID, AD_Org_ID);
						receiptDateReEntry = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateEnd,  BigDecimal.ONE, AD_Client_ID, AD_Org_ID);
						receiptDateReEntryReal = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation).subtract(BigDecimal.valueOf(elapsedDaysVacationCollective)), AD_Client_ID, AD_Org_ID);
						receiptDateApplication = MAMN_NonBusinessDay.getPreviusCalendarDay(InvDateIni,  BigDecimal.valueOf(15),  AD_Client_ID, AD_Org_ID);
						receiptDateReEntryReal = MAMN_NonBusinessDay.getNextBusinessDay(isSaturdayBusinessDay, InvDateIni,  BigDecimal.valueOf(elapsedDaysVacation).subtract(BigDecimal.valueOf(elapsedDaysVacationCollective)), AD_Client_ID, AD_Org_ID);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_InvDateIni,InvDateIni);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_InvDateEnd,InvDateEnd);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntry,receiptDateReEntry);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateReEntryReal,receiptDateReEntryReal);
						p_mTab.setValue(MAMN_Payroll.COLUMNNAME_DateApplication,receiptDateApplication);
					}
				}
			} else if (columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_RefDateIni) ||
					columnName.equalsIgnoreCase(MAMN_Payroll.COLUMNNAME_RefDateEnd)) {
				// FieldRef: RefDateIni 
				// FieldRef: RefDateEnd 
				if (p_mTab.getValue(MAMN_Payroll.COLUMNNAME_RefDateIni) != null &&
						p_mTab.getValue(MAMN_Payroll.COLUMNNAME_RefDateEnd) != null ) {
					vacationPeriodIni = (Timestamp) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_RefDateIni);
			        vacationPeriodEnd = (Timestamp) p_mTab.getValue(MAMN_Payroll.COLUMNNAME_RefDateEnd);
			        // Convertir el Timestamp a LocalDateTime
			        LocalDateTime localDateTime = vacationPeriodIni.toLocalDateTime();
			        // Obtener el mes y el año
			        int mes = localDateTime.getMonthValue(); // Devuelve el mes como entero (1 para enero, 12 para diciembre)
			        int anio = localDateTime.getYear();   
					// Crear un formateador de fecha con el formato deseado
			        SimpleDateFormat tsformatter = new SimpleDateFormat("dd/MM/yyyy");
			        PayrollDescription=AmerpUtilities.truncate(amnemployee.getValue()+"- "+
						Msg.translate(p_ctx, "from") + " "+ tsformatter.format(vacationPeriodIni) + " " +
						Msg.translate(p_ctx, "to") + " " + tsformatter.format(vacationPeriodEnd),255);
			        // UPDATES mes, anio y description
			        p_mTab.setValue(MAMN_Payroll.COLUMNNAME_month,mes);
			        p_mTab.setValue(MAMN_Payroll.COLUMNNAME_year,anio);
			        p_mTab.setValue(MAMN_Payroll.COLUMNNAME_Description, PayrollDescription);
				}
			}
						
		} else if (AMN_Process_Value.equalsIgnoreCase("NP")) {
			// ************************
			// Process NP
			// ************************
			
		} else if (AMN_Process_Value.equalsIgnoreCase("NU")) { 
			// ************************
			// Process NU
			// ************************
		} else {
			
		}

	    return null;
    }
}

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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

import org.amerp.amnmodel.*;
import org.compiere.model.MPeriod;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/** AMNPayrollCreatePeriodAssistPeriods
 * Description: Procedure called from iDempiere AD
 * 			AMN Personnel and Payroll Create Periods of year
 * 			For One Fiscal Year and One Payroll Period
 * 
 * Result:	Create records on AMN_Period_Assist Table 
 * 			according to Fiscal Year and Payroll Period.
 * 
 * @author luisamesty
 *
 */
/**
 *	AMN Personnel and Payroll Create Periods of year
 *	
 * @author luisamesty
*/
public class AMNPayrollCreatePeriodAssistPeriods extends SvrProcess
{
	private int	p_C_Year_ID = 0;
	private String YearName;
	private int	p_C_Period_ID = 0;
	int p_AD_Org_ID=0;
	int p_AD_Client_ID=0;
	private String sql ="";
	Timestamp GCdateAct;
	BigDecimal PeriodDays = BigDecimal.valueOf(30);
	int PeriodDaysInt=30;
	int month=0;
	int contPer,showPer=0;
	Locale locale;
	GregorianCalendar cal = new GregorianCalendar();
	Timestamp PeriodStartDate = null;
	Timestamp PeriodEndDate = null;
	Timestamp PeriodCurrentDate = null;
	String PeriodDay="";
	String PeriodStandardDate="";
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		//log.warning("........Here I'm in the prerare() - method");		
		ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			
			if (paraName.equals("C_Year_ID"))
				p_C_Year_ID = para.getParameterAsInt();
			else if (paraName.equals("C_Period_ID"))
				p_C_Period_ID =  para.getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}
		//p_C_Year_ID = getRecord_ID();
		
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		// log.warning("........Here I'm in the prerare() - method");
		sql="SELECT concat(trim(both from fiscalyear),' ',description) FROM c_year WHERE c_year_id=?";
		YearName=DB.getSQLValueString(null, sql, p_C_Year_ID)+" :"+p_C_Year_ID;
		sql="SELECT startdate from adempiere.c_period where c_period_id=?";
		PeriodStartDate=DB.getSQLValueTS(null, sql, p_C_Period_ID);
		sql="SELECT enddate from adempiere.c_period where c_period_id=?";
		PeriodEndDate=DB.getSQLValueTS(null, sql, p_C_Period_ID);
		addLog("Período Fiscal: "+YearName);
		addLog("Period Ini:"+PeriodStartDate+"  "+"Period End:"+PeriodEndDate);
		addLog("PeriodDaysInt:"+PeriodDaysInt);
		// INIT GregorianCalendar  cal
		if ( PeriodStartDate != null ) {
			cal.setTime(PeriodStartDate);
		}
		else 
		{
			cal.set(Calendar.YEAR, 2000);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// PayrollDays (One Month)
		int month  = cal.get(Calendar.MONTH); // Jan = 0, dec = 11
		while (cal.get(Calendar.MONTH) == month  ) {
			PeriodCurrentDate= new Timestamp(cal.getTimeInMillis());
			SimpleDateFormat DayNameDateFormat = new SimpleDateFormat("EEEEEE");
			SimpleDateFormat StandardDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			PeriodDay = DayNameDateFormat.format(cal.getTimeInMillis());
			PeriodStandardDate = StandardDateFormat.format(cal.getTimeInMillis());
			MPeriod mperiod = new MPeriod(getCtx(), p_C_Period_ID, null);
			p_AD_Client_ID=mperiod.getAD_Client_ID();
			// Verify if Not a Non Busineess Day
			boolean IsNonBusinessDay = false;
			String NonLaborText= "";
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY  && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				IsNonBusinessDay =false;
			} else {
				IsNonBusinessDay =true;
				NonLaborText="** No Labor **";
			}
			Double HOLLIDAYS = 0.00;
			HOLLIDAYS = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(IsNonBusinessDay, PeriodCurrentDate, PeriodCurrentDate, p_AD_Client_ID, p_AD_Org_ID).doubleValue();
			if (!HOLLIDAYS.equals((Double) 0.00)){
				IsNonBusinessDay = true;
				NonLaborText="** Holliday - No Labor **";
			} 
//log.warning("HOLLIDAYS:"+HOLLIDAYS+"  IsNonBusinessDay:"+IsNonBusinessDay+"  p_PeriodDate:"+PeriodCurrentDate.toString() + " "+PeriodDay+"  p_AD_Client_ID:"+p_AD_Client_ID + "  p_AD_Org_ID:"+p_AD_Org_ID);
			// Agrega Texto
			addLog(Msg.getMsg(Env.getCtx(), "Day")+":"+cal.get(Calendar.DAY_OF_MONTH)+" "+Msg.getMsg(Env.getCtx(), "Date")+":"+ PeriodStandardDate+ "  "+PeriodDay.trim()+" "+NonLaborText);
			// Create Assist Period
			MAMN_Period_Assist amnperiodassist = null;
			amnperiodassist = new MAMN_Period_Assist(getCtx(), 0, null);
			amnperiodassist.createAmnPeriodAssist(Env.getCtx(), locale,p_AD_Client_ID, p_AD_Org_ID, p_C_Year_ID, p_C_Period_ID, PeriodCurrentDate,PeriodDay,IsNonBusinessDay);
			// Increment Day
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}


		//	return "@Error@";
		return null;
	}	//	doIt
	
}	//	AMNYearCreatePeriods


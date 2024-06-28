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
/* SEE ALSO TimeUtil.java Class*/
package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.amerp.amnmodel.MAMN_Employee_Tax;
import org.compiere.util.*;


public class AmerpDateUtils {

	static CLogger	log = CLogger.getCLogger (AmerpDateUtils.class);
 
	public static void main(String[] args)    {

		String Message = "";
		System.out.println(" Init....Env");
		// Env
		Env.setContext(Env.getCtx(),"#AD_Role_ID",1000000);
		Env.setContext(Env.getCtx(), "#AD_User_ID",1000000);
		Env.setContext(Env.getCtx(), "#AD_Client_ID", 1000000);
		Env.setContext(Env.getCtx(), "#AD_Org_ID", 1000000);
		Env.setContext(Env.getCtx(),"#AD_Org_Name","Tamanaco");
		Env.setContext(Env.getCtx(), "#M_Warehouse_ID",1000000);
		System.out.println(" Init....Adempiere");
		Ini.setAdempiereHome("/Volumes/Datos/Adempiere/iDempiere2.1srcMac/idempiere");
		Ini.loadProperties("/Volumes/Datos/Adempiere/iDempiere2.1srcMac/idempiere/TAM2.1pro_idempiere.properties");
		//
		System.out.println(" Init....System.getProperties");
		//org.compiere.util.SecureEngine.init (System.getProperties());
		//
		System.out.println(" Init....startupEnvironment");
//		Adempiere.getAdempiereHome();
//		Adempiere.startupEnvironment(true);
		// SecureEngine
//		System.out.println(" Init....SecureEngine");
//		className = System.getProperty(SecureInterface.ADEMPIERE_SECURE_DEFAULT);
//		org.compiere.util.SecureEngine.init(className);	
		//org.compiere.Adempiere.startup(true);
	
		// Today Date
		BigDecimal NYears = BigDecimal.valueOf(0);
		BigDecimal NMonths = BigDecimal.valueOf(0);
		BigDecimal NDays = BigDecimal.valueOf(0);
		//
//		Double DIAS,HORAS,PNRM,DT,DTREC,DTPER,DTOK,NONLABORDAYS,LABORDAYS,HOLLIDAYS;
//		int AD_Client_ID=1000000;
//		int AD_Org_ID=0;
		// Create List
		ArrayList<Integer> list1 = new ArrayList<Integer>(3);
		list1.add(0,0);	// Years elapsed
		list1.add(1,0);	// Month
		list1.add(2,0);	// Days
		ArrayList<Integer> list2 = new ArrayList<Integer>(3);
		list2.add(0,0);	// Years elapsed
		list2.add(1,0);	// Month
		list2.add(2,0);	// Days
		
		Timestamp dateToday = new Timestamp(new java.util.Date().getTime());
		Timestamp dateStart = new Timestamp(new java.util.Date().getTime());
		Timestamp dateEnd = new Timestamp(new java.util.Date().getTime());
		// AMN_Contract
    	dateToday = Timestamp.valueOf("2016-04-30 00:00:00.0");
    	Timestamp dateZero = new Timestamp(new java.util.Date(0).getTime());
    	Timestamp dateEmployee1 = Timestamp.valueOf("2014-05-05 00:00:00.0");
    	Timestamp dateEmployee2 = Timestamp.valueOf("2010-05-19 00:00:00.0");
    			
    	// Output to console
    	System.out.println("Date Zero            : "+dateZero);
		System.out.println("Date Today           : "+dateToday);
		
		// Employee 11111
    	Message=("Employee1 Income Date: "+dateEmployee1);
    	System.out.println(Message);
    	NYears = getYearsBetween( dateEmployee1,dateToday);
    	NMonths = getMonthsBetween( dateEmployee1,dateToday);
    	NDays = getDaysBetween( dateEmployee1,dateToday);
    	// Output to console
    	Message=("  Absolute Years:"+NYears.intValue()+" Absolute Months:"+NMonths.intValue()+" Absolute Days:"+NDays.intValue());
    	// Calculate dateDiff in Timestamp Mode
    	list1 = getYearsMonthDaysElapsedBetween(dateEmployee1, dateToday);
    	// Output to console
    	Message= Message+" - Years:("+list1.get(0)+") Month:("+list1.get(1)+ ") Days:("+list1.get(2)+")";
		System.out.println(Message);
		
		// Employee 22222
		Message=("Employee2 Income Date: "+dateEmployee2);
		System.out.println(Message);
    	NYears = getYearsBetween( dateEmployee2,dateToday);
    	NMonths = getMonthsBetween( dateEmployee2,dateToday);
    	NDays = getDaysBetween( dateEmployee2,dateToday);
    	// Output to console
    	Message=("  Absolute Years:"+NYears.intValue()+" Absolute Months:"+NMonths.intValue()+" Absolute Days:"+NDays.intValue());
    	// Calculate dateDiff in Timestamp Mode
    	list2.clear();
    	list2 =  getYearsMonthDaysElapsedBetween(dateEmployee2, dateToday);
    	// Output to console
    	Message= Message+" - Years:("+list2.get(0)+") Month:("+list2.get(1)+ ") Days:("+list2.get(2)+")";
    	System.out.println(Message);
    	//System.out.println("formatElapsed:"+TimeUtil.formatElapsed(dateEmployee2, dateToday));
    	//
    	Timestamp refTimestamp= Timestamp.valueOf("1997-06-18 00:00:00");
		GregorianCalendar refcal = new GregorianCalendar();
		refcal.setTime(refTimestamp);
		System.out.println("Fecha:"+refTimestamp);
		refcal.add(Calendar.MONTH, 1);
		System.out.println("    Mes1:"+refcal.get(Calendar.MONTH));
		refcal.add(Calendar.MONTH, 3);
		System.out.println("    Mes2:"+refcal.get(Calendar.MONTH));
		refcal.add(Calendar.MONTH, 3);
		System.out.println("    Mes3:"+refcal.get(Calendar.MONTH));
		refcal.add(Calendar.MONTH, 3);
		System.out.println("    Mes4:"+refcal.get(Calendar.MONTH));
		
		// CALCULATE VARIABLES LABORDAYS,HOLLIDAYS,DTREC Double
//    	dateStart = Timestamp.valueOf("2015-12-28 00:00:00.0");
//    	dateEnd = Timestamp.valueOf("2016-01-03 00:00:00.0");
//    	// (double) 2; //
//		HOLLIDAYS = MAMN_NonBusinessDay.sqlGetHolliDaysBetween(dateStart, dateEnd, AD_Client_ID, AD_Org_ID).doubleValue();
//		LABORDAYS =  MAMN_NonBusinessDay.sqlGetNonWeekEndDaysBetween(dateStart, dateEnd, AD_Client_ID, null).doubleValue();
//		DTREC =1.00+ MAMN_NonBusinessDay.getDaysBetween(dateStart, dateEnd).doubleValue();
//		NONLABORDAYS = DTREC - LABORDAYS;
//		System.out.println("Date Start: "+dateStart+"  Date End: "+dateEnd);
//		System.out.println("HOLLIDAYS:"+HOLLIDAYS+"   LABORDAYS:"+LABORDAYS+"   DTREC:"+DTREC+"   NONLABORDAYS:"+NONLABORDAYS);
    	int ID_Year = 2015;
    	Timestamp  ID_PeriodIni = TimeUtil.getDay(ID_Year, 1, 1);
		Timestamp ID_PeriodEnd = TimeUtil.getDay(ID_Year, 12, 31);
		System.out.println("Prueba de TimeUtil:"+2015);
		System.out.println("  ID_PeriodIni:"+ID_PeriodIni);
		System.out.println("  ID_PeriodEnd:"+ID_PeriodEnd);
	Long dias30 = (long) 30;
	System.out.println("  dias30:"+dias30);
	dias30 = -1*(dias30+1);
	System.out.println("  dias30:"+dias30);
	// ACTUAL DATE SAMPLES
	System.out.println("Java current System  Date and Time:");
	//For java.util.Date, just create a new Date()
	System.out.println("For java.util.Date, just create a new Date()");
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
	// Days Elapse Betwen two days
	Timestamp date1 = Timestamp.valueOf("2017-11-01 00:00:00.0");
	Timestamp date2 = Timestamp.valueOf("2018-10-31 00:00:00.0");
	Timestamp date3 = Timestamp.valueOf("2017-11-30 00:00:00.0");
	Timestamp date4 = Timestamp.valueOf("2017-12-31 00:00:00.0");

	NDays = getDaysElapsed( date1,date2);
	Message=("  Date1:"+dateFormat.format(date1)+"  Date2:"+dateFormat.format(date2)+"  Days:"+NDays.intValue());
	System.out.println(Message);
	NDays = getDaysElapsed( date1,date3);
	Message=("  Date1:"+dateFormat.format(date1)+"  Date3:"+dateFormat.format(date3)+"  Days:"+NDays.intValue());
	System.out.println(Message);
	NDays = getDaysElapsed( date1,date4);
	Message=("  Date1:"+dateFormat.format(date1)+"  Date4:"+dateFormat.format(date4)+"  Days:"+NDays.intValue());
	System.out.println(Message);
	NDays = getDaysElapsed( date3,date4);
	Message=("  Date3:"+dateFormat.format(date3)+"  Date4:"+dateFormat.format(date4)+"  Days:"+NDays.intValue());
	System.out.println(Message);

	}
	
	
			
	/**
	 *  getYearsMonthDaysElapsedBetween
	 * 	Calculates the Elapsed Time in terms of Years, Months and Days
	 *  between start and end dates.
	 * 	@param start 	Start Date
	 * 	@param end 		End date
	 * 	@return Integer's list values
	 * 	 0: Years 1:  Month 2: Days
	 */
	static public ArrayList<Integer> getYearsMonthDaysElapsedBetween (Timestamp start, Timestamp end)
	{
		
		ArrayList<Integer> list = new ArrayList<Integer>(3);

		list.add(0,0);	// Years elapsed
		list.add(1,0);	// Month
		list.add(2,0);	// Days
		
		if (end.before(start))
		{
			return list;
		}
    	// Reference Date
		Timestamp refTimestamp;
		GregorianCalendar refcal = new GregorianCalendar();
		refcal.setTime(start);
		refcal.set(Calendar.HOUR_OF_DAY, 0);
		refcal.set(Calendar.MINUTE, 0);
		refcal.set(Calendar.SECOND, 0);
		refcal.set(Calendar.MILLISECOND, 0);
		// Calculates Years
		BigDecimal NYears = getYearsBetween(start,end);
		// Add Acalculated Years to refcal and Set New Reference
		refcal.add(Calendar.YEAR, NYears.intValue());
		refTimestamp = new Timestamp(refcal.getTimeInMillis());
		//System.out.println(" YEAR refTimestamp:"+refTimestamp.toString().substring(0, 10)+" NYears:"+NYears+" int NYears:"+NYears.intValue());
		// Calculates Months
		BigDecimal NMonths = getMonthsBetween( refTimestamp,end);
		// Add Acalculated Months to refcal and Set New Reference
		refcal.add(Calendar.MONTH, NMonths.intValue());
		refTimestamp = new Timestamp(refcal.getTimeInMillis());
		//System.out.println(" MONTH refTimestamp:"+refTimestamp.toString().substring(0, 10)+" NMonths:"+NMonths+" int NMonths:"+NMonths.intValue());
		// Calculate Days
		BigDecimal NDays = getDaysBetween( refTimestamp,end);
		refTimestamp = new Timestamp(refcal.getTimeInMillis());
		//System.out.println(" NDays refTimestamp:"+refTimestamp.toString().substring(0, 10)+" NDays:"+NDays+" int NDays:"+NDays.intValue());
		// UPDATE LIST
		list.set(0, NYears.intValue());
		list.set(1, NMonths.intValue());
		list.set(2, NDays.intValue());
		return list;
	}
		
	/**
	 * 	Calculate the number of Years between start and end.
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return BigDecimal number of Years (0 = same)
	 */
	static public BigDecimal getYearsBetween (Timestamp start, Timestamp end)
	{
		boolean negative = false;
		double difftmp=0;
		if (end.before(start))
		{
			negative = true;
		}
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);
		//	log.warning("Start=" + start + ", End=" + end + ", dayStart=" + cal.get(Calendar.DAY_OF_YEAR) + ", dayEnd=" + calEnd.get(Calendar.DAY_OF_YEAR));
//		}	//	getYearsBetween
		while (cal.before(calEnd))
		{
			cal.add (Calendar.YEAR, 1);
			if (cal.before(calEnd))
					difftmp++;
		}
		if (negative)
			difftmp = 0;
//log.warning("difftmp:"+difftmp+"End:"+calEnd.get(Calendar.YEAR)+"  start:"+cal.get(Calendar.YEAR));
		return BigDecimal.valueOf(difftmp);
	}
	
	/**
	 * 	Calculate the number of Months between start and end.
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return BigDecimal number of Months (0 = same)
	 */
	
	static public BigDecimal getMonthsBetween (Timestamp start, Timestamp end)
	{
		boolean negative = false;
		double difftmp=0;

		if (end.before(start))
		{
			negative = true;
		}
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);
		// 		double nYears=0;
		//	log.warning("Start=" + start + ", End=" + end + ", dayStart=" + cal.get(Calendar.DAY_OF_YEAR) + ", dayEnd=" + calEnd.get(Calendar.DAY_OF_YEAR));
		//while (calEnd.after(cal))
		while (cal.before(calEnd))
		{
			cal.add (Calendar.MONTH, 1);
			if (cal.before(calEnd))
					difftmp++;
		}
		if (negative)
			difftmp = 0;
		return BigDecimal.valueOf(difftmp);
	}
	
	/**
	 * 	Calculate the number of days between start and end.
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return BigDecimal number of days (0 = same)
	 */
	static public BigDecimal getDaysBetween (Timestamp start, Timestamp end)
	{
		boolean negative = false;
		double difftmp=0;
		if (end.before(start))
		{
			negative = true;
		}
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);
		//	log.warning("Start=" + start + ", End=" + end + ", dayStart=" + cal.get(Calendar.DAY_OF_YEAR) + ", dayEnd=" + calEnd.get(Calendar.DAY_OF_YEAR));
		//	in same year
		if (cal.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR))
		{
			difftmp = calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
		} else {
			//	not very efficient, but correct
			//while (calEnd.after(cal))
			while (cal.before(calEnd))
			{
				cal.add (Calendar.DAY_OF_YEAR, 1);
				if (cal.before(calEnd))
					difftmp++;
			}
		}	//	getDaysBetween
		if (negative)
			difftmp = 0;
		return BigDecimal.valueOf(difftmp);
	}
	
	/**
	 * 	Calculate the number of days Elapses In a Document Receipt
	 * 	 start and end.
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return BigDecimal number of days (1 = same)
	 */
	static public BigDecimal getDaysElapsed (Timestamp start, Timestamp end)
	{
		boolean negative = false;
		int counter = 0;
		if (end.before(start))
		{
			negative = true;
			Timestamp temp = start;
			start = end;
			end = temp;
		}
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);

	//	System.out.println("Start=" + start + ", End=" + end + ", dayStart=" + cal.get(Calendar.DAY_OF_YEAR) + ", dayEnd=" + calEnd.get(Calendar.DAY_OF_YEAR));

		//	in same year
		if (cal.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR))
		{
			if (negative) {
				counter = (calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)) * -1;
				counter++;
			} else {
				counter = calEnd.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);
				counter++;
			}
			return BigDecimal.valueOf(counter);
		}

		//	not very efficient, but correct
		
		while (calEnd.after(cal))
		{
			cal.add (Calendar.DAY_OF_YEAR, 1);
			counter++;
		}
		counter++;
		//
		if (negative)
			counter = counter * -1;

		return BigDecimal.valueOf(counter);
	}
	
	/**
	 * 	Calculate the number of defined Weekday days between start and end.
	 *  @param week day to be analized (0:sunday,1:Monday,2:tuesday ...)
	 * 	@param start start date
	 * 	@param end end date
	 * 	@return BigDecimal number of days (0 = same)
	 */
	static public BigDecimal getWeekDaysBetween (int WeekDay, Timestamp start, Timestamp end)
	{
		double nweekdays=0;
		//
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(start);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		GregorianCalendar calEnd = new GregorianCalendar();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);
		//	not very efficient, but correct
		if ( cal.get(Calendar.DAY_OF_WEEK)==WeekDay)
			nweekdays++;
		while (calEnd.after(cal))
		{
			cal.add (Calendar.DAY_OF_YEAR, 1);
			if ( cal.get(Calendar.DAY_OF_WEEK)==WeekDay)
				nweekdays++;
		}
		return BigDecimal.valueOf(nweekdays);
	}
}

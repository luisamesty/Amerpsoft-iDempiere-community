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

package org.amerp.process;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Period;
import org.compiere.model.MPeriod;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 * AMNYearCreatePeriods
 * Description: Procedure called from iDempiere AD
 * Parameters: 
 * 	C_Year_ID 
 * 	C_Calendar_ID
 * 	AMN_Process_ID
 * 	AMN_Contract_ID
 * Result:
 * 		AMN Personnel and Payroll Create Periods of year on AMN_Period Table
 *	
 * @author luisamesty
*/
public class AMNYearCreatePeriodsFromPeriod extends SvrProcess
{
	private int	p_C_Year_ID = 0;
	private int p_C_Period_ID = 0;
	Timestamp PeriodStartDateFrom=null;
	private String YearName;
	private int	p_C_Calendar_ID = 0;
	private int p_AMN_Process_ID = 0;
	private int p_AMN_Contract_ID = 0;
	int p_AD_Org_ID=0;
	private String sql ="";
	String Process_Value="";
	Integer ID_PeriodIni = 0 ;
	Integer ID_PeriodEnd = 0 ;
	Timestamp YearIni;		// Init Date for Fiscal Year
	Timestamp YearEnd;		// End Date for fiscal Year
	Timestamp GCdateAct;
	int InitDow=0;
	int AcctDow=0;
	BigDecimal PayrollDays = BigDecimal.valueOf(30);
	int PayrollDaysInt=30;
	int p_PayRollDays =0;
	int OffsetDays;
	int month=12;
	int contPer,showPer=0;
	Locale locale;
	GregorianCalendar cal = new GregorianCalendar();
	int[] PeriodNo_ID = {1,2,3,4,5,6,7,8,9,10,11,12,13};
	String[] PeriodName = {"Ene","Feb","mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic",""};
	Timestamp[] PeriodStartDate = {null,null,null,null,null,null,null,null,null,null,null,null,null};
	Timestamp[] PeriodEndDate = {null,null,null,null,null,null,null,null,null,null,null,null,null};
	Timestamp[] CPStartDate = {null,null,null,null,null,null,null,null,null,null,null,null,null};
	Timestamp[] CPEndDate = {null,null,null,null,null,null,null,null,null,null,null,null,null};
	int[] CPPeriodNo = {1,2,3,4,5,6,7,8,9,10,11,12,13};
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		// log.warning("........Here I'm in the prerare() - method");		
		ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			
			if (paraName.equals("C_Year_ID"))
				p_C_Year_ID = para.getParameterAsInt();
			else if (paraName.equals("C_Period_ID"))
				p_C_Period_ID =  para.getParameterAsInt();
			else if (paraName.equals("C_Calendar_ID"))
				p_C_Calendar_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Process_ID"))
				p_AMN_Process_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("PayRollDays"))
				p_PayRollDays =  para.getParameterAsInt();			
			//	else if (paraName.equals("StartDate"))
			//		p_StartDate = para.getParameterAsTimestamp();
			//	else if (paraName.equals("EndDate"))
			//		p_EndDate = para.getParameterAsTimestamp();
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
	@SuppressWarnings("deprecation")
    protected String doIt () throws Exception
	{

		// log.warning("........Here I'm in the prerare() - method");
		// Determines Accounting Periods and Start and End Dates
		sql="SELECT concat(trim(both from fiscalyear),' ',description) FROM c_year WHERE c_year_id=?";
		YearName=DB.getSQLValueString(null, sql, p_C_Year_ID)+" :"+p_C_Year_ID;
		sql = "SELECT c_period_id FROM c_period WHERE c_year_id=? order by startdate asc" ;		
		ID_PeriodIni = DB.getSQLValue(null, sql, p_C_Year_ID);
		sql = "SELECT startdate FROM c_period WHERE c_year_id=? order by startdate asc" ;
		YearIni = DB.getSQLValueTS(null, sql, p_C_Year_ID);
		sql = "SELECT c_period_id FROM c_period WHERE c_year_id=? order by startdate desc" ;
		ID_PeriodEnd = DB.getSQLValue(null, sql, p_C_Year_ID);
		sql = "SELECT enddate FROM c_period WHERE c_year_id=? order by startdate desc" ;
		YearEnd = DB.getSQLValueTS(null, sql, p_C_Year_ID);
		// Determines InitDow AcctDow
		sql = "SELECT initdow FROM amn_contract WHERE amn_contract_id=?" ;
		InitDow = DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		sql = "SELECT acctdow FROM amn_contract WHERE amn_contract_id=?" ;
		AcctDow = DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		// Determines Period Length in days
		sql = "SELECT payrolldays FROM amn_contract WHERE amn_contract_id=?" ;
		PayrollDays = DB.getSQLValueBD(null, sql, p_AMN_Contract_ID);
		// Determines AD_Org_ID
		sql = "SELECT ad_org_id FROM amn_contract WHERE amn_contract_id=?" ;
		p_AD_Org_ID=DB.getSQLValue(null, sql, p_AMN_Contract_ID);
		// Determines Process_Value
		sql = "SELECT value FROM amn_process WHERE amn_process_id=?" ;
		Process_Value=DB.getSQLValueString(null, sql, p_AMN_Process_ID);
		addLog(Msg.getElement(Env.getCtx(), "C_Year_ID")+": "+YearName);
		addLog("Period Ini:"+ID_PeriodIni+"  "+"Period End:"+ID_PeriodEnd);
		addLog(Msg.getElement(Env.getCtx(),"PayRollDays")+"  "+ Msg.getElement(Env.getCtx(),"Parameter")+ ": "+PayrollDays);
		addLog(Msg.getElement(Env.getCtx(),"InitDow")+": "+InitDow+" "+
				Msg.getElement(Env.getCtx(),"AcctDow")+": "+AcctDow);
		// INIT GregorianCalendar  cal
		if ( YearIni != null ) {
			cal.setTime(YearIni);
			OffsetDays=cal.get(Calendar.DAY_OF_WEEK)-1;
			// PayrollDays (7,14,15,30)
			// Use parameter if any of (7,14,15,30)  otherwise Contranct's Days
			if (p_PayRollDays==7 || p_PayRollDays==14 || p_PayRollDays==15 || p_PayRollDays==30  ) {
				PayrollDaysInt = p_PayRollDays;
				PayrollDays= new BigDecimal(p_PayRollDays);
			} else {
				PayrollDaysInt =Integer.valueOf(PayrollDays.intValue());
			}
			// Check PayrollDaysInt
			switch (PayrollDaysInt) {
				case 7:  cal.add(Calendar.DAY_OF_YEAR, -OffsetDays+InitDow);
	                     break;
	            case 14: cal.add(Calendar.DAY_OF_YEAR, -OffsetDays+InitDow);;
	                     break;
	            case 15:
	            		break;
	            case (30):
            			break;	            	
			}         
		} else {
			cal.set(Calendar.YEAR, 2000);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// 	ONLY FOR INFO
		//	Init Period Dates for Fiscal Year 
        month = 1;
        // MPeriod period;
        while (month <= 12) {
			if (month >=1 && month <= 12) {				
				sql = "SELECT c_period_id FROM c_period WHERE c_year_id="+p_C_Year_ID+" and periodno=? ";
				PeriodNo_ID[month]=DB.getSQLValue(null, sql,month);
				sql = "SELECT name FROM c_period WHERE c_year_id="+p_C_Year_ID+" and periodno=? ";
				PeriodName[month]=DB.getSQLValueString(null, sql, month);
				// Period Start Date EndDate
				sql = "SELECT startdate FROM c_period WHERE c_year_id="+p_C_Year_ID+" and periodno=? ";
				PeriodStartDate[month]= DB.getSQLValueTS(null, sql, month);
				sql = "SELECT enddate FROM c_period WHERE c_year_id="+p_C_Year_ID+" and periodno=? ";
				PeriodEndDate[month]=DB.getSQLValueTS(null, sql, month);			
				//addLog(PeriodName[month]+":");
				//addLog("Periodo" +month+":"+ PeriodNo_ID[month] + "  " + PeriodName[month].trim()+ " "+ PeriodStartDate[month]+ "-"+ PeriodEndDate[month]);
			} 
			month=month+1;
		}
        // Period Start date
        sql = "SELECT startdate FROM c_period WHERE c_year_id="+p_C_Year_ID+" and c_period_id=? ";
		PeriodStartDateFrom =DB.getSQLValueTS(null, sql, p_C_Period_ID);
		//	Create Contract Period Array
        // 	Start Date EndDate 
        //	From: Payroll Days, YearIni adjusted by OffsetDays using Gregorian Calendar class
        //  Payroll Days applies only for Process NN otherwise Payroll Days = 30
        contPer=1;
        // String STcontPer = String.format("%02d", contPer);
        // NN Process Different PayrollDays, rest of them  30 Days (1 Month)
        // NO Process Added Different Payroll Days
    	// Same PayrollDays
        if (Process_Value.trim().equalsIgnoreCase("NN") ) {
        	addLog(Msg.getElement(Env.getCtx(), "Process_Value")+": ("+Process_Value.trim()+")");
        	addLog(Msg.getElement(Env.getCtx(), "PayRollDays")+" ** REAL NN ** : ("+PayrollDays+") ");
        } else if ( Process_Value.trim().equalsIgnoreCase("NO") ) {
        	addLog(Msg.getElement(Env.getCtx(), "Process_Value")+": ("+Process_Value.trim()+")");
        	addLog(Msg.getElement(Env.getCtx(), "PayRollDays")+" ** REAL NO ** : ("+PayrollDays+") ");
        } else {
        	PayrollDays = BigDecimal.valueOf(30);
        	addLog(Msg.getElement(Env.getCtx(), "Process_Value")+": ("+Process_Value.trim()+") ");
        	addLog(Msg.getElement(Env.getCtx(), "PayRollDays")+" ** REAL ** : ("+PayrollDays+") ");
        	// Set Cal Reset OffsetDays
        	if ( YearIni != null ) 
        		cal.setTime(YearIni);
			//
        }
        addLog(Msg.getElement(Env.getCtx(),"AMN_Process_ID")+":"+Process_Value.trim()+
        		Msg.getElement(Env.getCtx(),"PayRollDays")+":"+PayrollDays);
		// PayrollDays (7,14,15,30)
		// Convert To Integer PayrollDaysInt
		PayrollDaysInt =Integer.valueOf(PayrollDays.intValue());
		switch (PayrollDaysInt) {
			case 7: 
				CPStartDate = new Timestamp[54];
				CPEndDate = new Timestamp[54];
				CPPeriodNo = new int[54];
				while (contPer <= 53) {
					if (contPer >=1 && contPer <= 53) {	
						CPStartDate[contPer]= new Timestamp(cal.getTimeInMillis());
						cal.add(Calendar.DAY_OF_YEAR, PayrollDaysInt -1);
						CPEndDate[contPer]=new Timestamp(cal.getTimeInMillis());
						cal.add(Calendar.DAY_OF_YEAR, 1);
						CPPeriodNo[contPer]=MPeriod.getC_Period_ID(getCtx(), CPEndDate[contPer], p_AD_Org_ID);
						//addLog("CP" + String.format("%02d", contPer) + " ID:"+ CPPeriodNo[contPer] +" FROM:"+CPStartDate[contPer].toString().substring(0, 10)+ "  TO:"+ CPEndDate[contPer].toString().substring(0, 10));
						}
					contPer=contPer+1;				
					}
				break;
			case 14:
				CPStartDate = new Timestamp[28];
				CPEndDate = new Timestamp[28];
				CPPeriodNo = new int[28];
				while (contPer <= 27) {
					if (contPer >=1 && contPer <= 27) {	
						CPStartDate[contPer]= new Timestamp(cal.getTimeInMillis());
						cal.add(Calendar.DAY_OF_YEAR, PayrollDaysInt -1);
						CPEndDate[contPer]=new Timestamp(cal.getTimeInMillis());
						cal.add(Calendar.DAY_OF_YEAR, 1);
						CPPeriodNo[contPer]=MPeriod.getC_Period_ID(getCtx(), CPEndDate[contPer], p_AD_Org_ID);
						//addLog("CP" + String.format("%02d", contPer) + " ID:"+ CPPeriodNo[contPer] +" FROM:"+CPStartDate[contPer].toString().substring(0, 10)+ "  TO:"+ CPEndDate[contPer].toString().substring(0, 10));
					}
					contPer=contPer+1;
				} 
				break;
			case 15:
            	month = 1;
				CPStartDate = new Timestamp[26];
				CPEndDate = new Timestamp[26];
				CPPeriodNo = new int[26];
            	while (month <= 13) {
					if (month >=1 && month <= 12) {	
						contPer=month*2-1;
	    				// Fist day of month
						cal.set(Calendar.DAY_OF_MONTH, 1);
						CPStartDate[contPer]= new Timestamp(cal.getTimeInMillis());
						// Day 15
						cal.set(Calendar.DAY_OF_MONTH, 15);
						CPEndDate[contPer]=new Timestamp(cal.getTimeInMillis());
						CPPeriodNo[contPer]=MPeriod.getC_Period_ID(getCtx(), CPEndDate[contPer], p_AD_Org_ID);
						// LOG
						// addLog("CP" + String.format("%02d", contPer) + " ID:"+ CPPeriodNo[contPer] +" FROM:"+CPStartDate[contPer].toString().substring(0, 10)+ "  TO:"+ CPEndDate[contPer].toString().substring(0, 10));
						// NEXT PERIOD IN MONTH 
						contPer=month*2;
						// Day 16	
						cal.set(Calendar.DAY_OF_MONTH, 16);
						CPStartDate[contPer]= new Timestamp(cal.getTimeInMillis());
						// LAST DAY OF MONTH
						cal.set(Calendar.DAY_OF_MONTH, 1);
						cal.add(Calendar.MONTH, 1);
						cal.add(Calendar.DAY_OF_YEAR, -1);
						CPEndDate[contPer]=new Timestamp(cal.getTimeInMillis());
						CPPeriodNo[contPer]=MPeriod.getC_Period_ID(getCtx(), CPEndDate[contPer], p_AD_Org_ID);
						// BACK to FIST DAY OF NEXT MONTH
						cal.add(Calendar.DAY_OF_YEAR, +1);
						// LOG
						// addLog("CP" + String.format("%02d", contPer) + " ID:"+ CPPeriodNo[contPer] +" FROM:"+CPStartDate[contPer].toString().substring(0, 10)+ "  TO:"+ CPEndDate[contPer].toString().substring(0, 10));
					}
					// PERIOD FOR NEXT MONTH
					month = month +1;
            	}
				break;
			case (30):
				CPStartDate = new Timestamp[13];
				CPEndDate = new Timestamp[13];
				CPPeriodNo = new int[13];
				while (contPer <= 13) {
					if (contPer >=1 && contPer <= 12) {	
						// Fist day of month
						cal.set(Calendar.DAY_OF_MONTH, 1);
						CPStartDate[contPer]= new Timestamp(cal.getTimeInMillis());
						// End day of month
						cal.add(Calendar.MONTH, 1);
						cal.add(Calendar.DAY_OF_YEAR, -1);
						CPEndDate[contPer]=new Timestamp(cal.getTimeInMillis());
						CPPeriodNo[contPer]=MPeriod.getC_Period_ID(getCtx(), CPEndDate[contPer], p_AD_Org_ID);
						// BACK to FIST DAY OF NEXT MONTH
						cal.add(Calendar.DAY_OF_YEAR, +1);
						// addLog("CP" + String.format("%02d", contPer) + " ID:"+ CPPeriodNo[contPer] +" FROM:"+CPStartDate[contPer].toString().substring(0, 10)+ "  TO:"+ CPEndDate[contPer].toString().substring(0, 10));
					} 	
					contPer=contPer+1;
				}
				break;	
		}
		// SHOW PAYROLL PERIODS
        showPer=1;
        // PayrollDays (7,14,15,30)
		// Convert To Integer PayrollDaysInt
		PayrollDaysInt =Integer.valueOf(PayrollDays.intValue());
		switch (PayrollDaysInt) {
			case 7:
				contPer=53;
				break;
			case 14:
				contPer=27;
				break;
			case 15:
				contPer=24;
				break;
			case 30:
				contPer=12;
				break;
		}

		while (showPer <= contPer) {
			// Show Periods to be generated
			addLog("CP" + String.format("%02d", showPer) + " ID:"+ CPPeriodNo[showPer] +
					Msg.getMsg(Env.getCtx(), "From")+":"+CPStartDate[showPer].toString().substring(0, 10)+ 
					Msg.getMsg(Env.getCtx(), "to")+":"+ CPEndDate[showPer].toString().substring(0, 10));
			showPer=showPer+1;				
		}
		// PROCESS
        showPer=1;
        MAMN_Period amnperiod;
		while (showPer <= contPer) {
			//amnperiod =  MAMN_Period.findByCalendar(Env.getCtx(), locale, p_C_Year_ID, p_AMN_Process_ID, p_AMN_Contract_ID, CPStartDate[showPer], CPEndDate[showPer]);
			amnperiod = new MAMN_Period(getCtx(), 0, null);
			int mperiod;
			//mperiod = MPeriod.findByCalendar(getCtx(), CPEndDate[showPer], p_C_Calendar_ID);
			mperiod = CPPeriodNo[showPer] ;
			if (mperiod != 0)
			{
				// ONLY Periods greater or equal than PeriodStartDateFrom
				if (CPStartDate[showPer].compareTo(PeriodStartDateFrom) >= 0 ){
					// Creates or Updates Period on AMN_Period 
					int amper = amnperiod.createAmnPeriod(Env.getCtx(), locale,p_AD_Org_ID, p_C_Year_ID, CPPeriodNo[showPer], p_AMN_Process_ID, p_AMN_Contract_ID, CPStartDate[showPer], CPEndDate[showPer]);
					// UPdates RefDateIni and RefDateEnd on AMN_Period on NN Process
			        if (Process_Value.trim().equalsIgnoreCase("NN") ) {
						amnperiod.updateRefDateAmnPeriodwithSlack(Env.getCtx(), locale,p_AMN_Contract_ID, amper);
			        } else {
			        	amnperiod.updateRefDateAmnPeriodwithoutSlack(Env.getCtx(), locale,p_AMN_Contract_ID, amper);
			        }
				}
			}
			// Show Results Values from Insert
			// addLog(amnperiod.getAMN_Period_ID() + " "+amnperiod.getValue()+ " "+amnperiod.getName() );
			showPer=showPer+1;				
		}        

		//	return "@Error@";
		return null;
	}	//	doIt
	
}	//	AMNYearCreatePeriods


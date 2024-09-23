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
package org.amerp.amnmodel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import org.adempiere.util.IProcessUI;
import org.compiere.model.MClient;
import org.compiere.model.MPeriod;
import org.compiere.util.*;

public class MAMN_Period extends X_AMN_Period {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3751993523635388658L;

	/**	Cache							*/
	private static CCache<Integer,MAMN_Period> s_cache = new CCache<Integer,MAMN_Period>(Table_Name, 10);

	static CLogger log = CLogger.getCLogger(MAMN_Period.class);
	
	public MAMN_Period (Properties ctx, int AMN_Period_ID, String trxName) {
		super(ctx, AMN_Period_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Period (Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Get Period from Cache
	 * @param ctx context
	 * @param MAMN_Period id
	 * @return MPeriod
	 */
	public static MAMN_Period get (Properties ctx, int MAMN_Period_ID)
	{
		if (MAMN_Period_ID <= 0)
			return null;
		//
		Integer key = new Integer(MAMN_Period_ID);
		MAMN_Period retValue = (MAMN_Period) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Period (ctx, MAMN_Period_ID, null);
		if (retValue.getAMN_Period_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get

	/**
	 * Description: Finds MAMN_period from Start Date, End Adte, Process, Contract
	 * 				and Year
	 * @param ctx
	 * @param locale
	 * @param p_C_Year_ID
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_C_Year_ID
	 * @param p_startDate
	 * @param p_endDate
	 * @return MAMN_Period
	 */
	public static MAMN_Period findByCalendar(Properties ctx, Locale locale, 
				int p_C_Year_ID, int p_AMN_Process_ID, int p_AMN_Contract_ID,
				Timestamp p_startDate, Timestamp p_endDate) {
				
		MAMN_Period retValue = null;
		String sql = "SELECT * "
			+ "FROM amn_period "
			+ "WHERE c_year_id=?"
			+ " AND amn_process_id=?"
			+ " AND amn_contract_id=?"
			+ " AND amndateini=?"
			+ " AND amndateend=?"
			;        
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_C_Year_ID);
            pstmt.setInt (2, p_AMN_Process_ID);
            pstmt.setInt (3, p_AMN_Contract_ID);
            pstmt.setTimestamp (4, p_startDate);
            pstmt.setTimestamp (5, p_endDate);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Period amnperiod = new MAMN_Period(ctx, rs, null);
				Integer key = new Integer(amnperiod.getAMN_Period_ID());
				s_cache.put (key, amnperiod);
				if (amnperiod.isActive())
					retValue = amnperiod;
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
	 * Name: createAmnPeriod
	 * Description: Creates or Update AMN_Period
	 * @param ctx
	 * @param locale
	 * @param p_C_Year_ID  		Accounting Fiscal Year 
	 * @param p_AMN_Process_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @param p_startDate
	 * @param p_endDate
	 * @return MAMN_Period
	 */
	public int createAmnPeriod(Properties ctx, Locale locale, 
			int p_Ad_Org_ID,int p_C_Year_ID, int p_C_Period_ID, 
			int p_AMN_Process_ID, int p_AMN_Contract_ID,
				Timestamp p_startDate, Timestamp p_endDate) {
		if (locale == null)
		{
			MClient client = MClient.get(ctx);
			locale = client.getLocale();
		}
		if (locale == null && Language.getLoginLanguage() != null)
			locale = Language.getLoginLanguage().getLocale();
		if (locale == null)
			locale = Env.getLanguage(ctx).getLocale();
		// Process and Contract
		MAMN_Process amnprocess = new MAMN_Process(ctx,p_AMN_Process_ID,null);
		MAMN_Contract amncontract = new MAMN_Contract(ctx,p_AMN_Contract_ID,null);
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Period amnperiod = MAMN_Period.findByCalendar(Env.getCtx(), locale, p_C_Year_ID, p_AMN_Process_ID, p_AMN_Contract_ID, p_startDate, p_endDate);
		if (amnperiod == null)
		{
			amnperiod = new MAMN_Period(getCtx(), getAMN_Period_ID(), get_TrxName());
			amnperiod.setAD_Org_ID(p_Ad_Org_ID);
			amnperiod.setAMN_Contract_ID(p_AMN_Contract_ID);
			amnperiod.setAMN_Process_ID(p_AMN_Process_ID);
			amnperiod.setC_Year_ID(p_C_Year_ID);
			amnperiod.setC_Period_ID(p_C_Period_ID);;
			amnperiod.setIsActive(true);
			amnperiod.setAMN_Period_Status("C");
			amnperiod.setAMN_Period_Processed("N");
			amnperiod.setValue(amnprocess.getAMN_Process_Value().trim()+"-"+amncontract.getValue().trim()+"_"+p_startDate.toString().substring(0, 10)+"_"+p_endDate.toString().substring(0, 10));
			amnperiod.setName(Msg.getElement(Env.getCtx(), "AMN_Period_ID")+" "+
					Msg.getMsg(Env.getCtx(), "From")+":"+p_startDate.toString().substring(0, 10)+" "+
					Msg.getMsg(Env.getCtx(), "to")+":"+p_endDate.toString().substring(0, 10));
			amnperiod.setAMNDateIni(p_startDate);
			amnperiod.setAMNDateEnd(p_endDate);
		}
		else
		{
			amnperiod.setValue(amnprocess.getAMN_Process_Value().trim()+"-"+amncontract.getValue().trim()+"_"+p_startDate.toString().substring(0, 10)+"_"+p_endDate.toString().substring(0, 10));
			amnperiod.setName(Msg.getElement(Env.getCtx(), "AMN_Period_ID")+" "+
				Msg.getMsg(Env.getCtx(), "From")+":"+p_startDate.toString().substring(0, 10)+" "+
				Msg.getMsg(Env.getCtx(), "to")+":"+p_endDate.toString().substring(0, 10));
			//amnperiod.setName("Period From:"+p_startDate.toString().substring(0, 10)+ " To:"+p_endDate.toString().substring(0, 10));
		}
		if (processMonitor != null)
		{
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Period_ID")+": "+p_startDate.toString()+" "+p_endDate);
		}
		
		amnperiod.saveEx(get_TrxName());	//	Creates AMNPeriod 
		return amnperiod.getAMN_Period_ID();
		
	}	//	createAmnPeriod

	
	/**
	 * Name: updateRefDateAmnPeriodwithSlack
	 * Description: Update RefDateIni and RefDateEnd on AMN_Period table
	 * 		With Calculated Attendance Days dependin on Slack Days
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Period_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @return Boolean true or false
	 */
	public boolean updateRefDateAmnPeriodwithSlack(Properties ctx, Locale locale, 
			 int p_AMN_Contract_ID, int p_AMN_Period_ID) {
		String Msg_Value="";
		Timestamp StartAttendanceDate=null;
		Timestamp EndAttendanceDate=null;
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
		MAMN_Period amnperiod = new MAMN_Period(ctx, p_AMN_Period_ID, null);
		// Find Attendance days to Put on RefDateIni and RefDateEnd
		StartAttendanceDate = amnperiod.getStartAttendanceDate(Env.getCtx(),p_AMN_Contract_ID, p_AMN_Period_ID);
		EndAttendanceDate = amnperiod.getEndAttendanceDate(Env.getCtx(),p_AMN_Contract_ID, p_AMN_Period_ID);
		// Check for Null Dates
		if (StartAttendanceDate == null || EndAttendanceDate == null)
		{
			Msg_Value=" *** "+Msg.getMsg(Env.getCtx(),"Error")+" ***";
			return false;
		}
		else
		{
			//log.warning("Set RefDates on p_AMN_Period_ID:"+p_AMN_Period_ID+" StartAttendanceDate:"+StartAttendanceDate+"  EndAttendanceDate:"+EndAttendanceDate);	
			amnperiod.setRefDateIni(StartAttendanceDate);
			amnperiod.setRefDateEnd(EndAttendanceDate);
			Msg_Value=(Msg.getElement(Env.getCtx(), "AMN_Period_ID")+" "+
				Msg.getElement(Env.getCtx(), "RefDateIni")+":"+StartAttendanceDate.toString().substring(0, 10)+" "+
				Msg.getElement(Env.getCtx(), "RefDateEnd")+":"+EndAttendanceDate.toString().substring(0, 10));
			//amnperiod.setName("Period From:"+p_startDate.toString().substring(0, 10)+ " To:"+p_endDate.toString().substring(0, 10));
			amnperiod.saveEx();	//	Update AMN_Period 
		}
		if (processMonitor != null)
		{
			processMonitor.statusUpdate(Msg_Value);
		}
		
		return true;
		
	}	//	updateRefDateAmnPeriod

	/**
	 * Name: updateRefDateAmnPeriodwithoutSlack
	 * Description: Update RefDateIni and RefDateEnd on AMN_Period table
	 * 		With Start and End Period Dates
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Period_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @return Boolean true or false
	 */
	public boolean updateRefDateAmnPeriodwithoutSlack(Properties ctx, Locale locale, 
			 int p_AMN_Contract_ID, int p_AMN_Period_ID) {
		String Msg_Value="";
		Timestamp StartRefDate=null;
		Timestamp EndRefDate=null;
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
		MAMN_Period amnperiod = new MAMN_Period(ctx, p_AMN_Period_ID, null);
		// Find Attendance days to Put on RefDateIni and RefDateEnd
		StartRefDate = amnperiod.getAMNDateIni(); // amnperiod.getStartAttendanceDate(Env.getCtx(),p_AMN_Contract_ID, p_AMN_Period_ID);
		EndRefDate = amnperiod.getAMNDateEnd();   // amnperiod.getEndAttendanceDate(Env.getCtx(),p_AMN_Contract_ID, p_AMN_Period_ID);
		// Check for Null Dates
		if (StartRefDate == null || EndRefDate == null)
		{
			Msg_Value=" *** "+Msg.getMsg(Env.getCtx(),"Error")+" ***";
			return false;
		}
		else
		{
			//log.warning("Set RefDates on p_AMN_Period_ID:"+p_AMN_Period_ID+" StartAttendanceDate:"+StartAttendanceDate+"  EndAttendanceDate:"+EndAttendanceDate);	
			amnperiod.setRefDateIni(StartRefDate);
			amnperiod.setRefDateEnd(EndRefDate);
			Msg_Value=(Msg.getElement(Env.getCtx(), "AMN_Period_ID")+" "+
				Msg.getElement(Env.getCtx(), "RefDateIni")+":"+StartRefDate.toString().substring(0, 10)+" "+
				Msg.getElement(Env.getCtx(), "RefDateEnd")+":"+EndRefDate.toString().substring(0, 10));
			//amnperiod.setName("Period From:"+p_startDate.toString().substring(0, 10)+ " To:"+p_endDate.toString().substring(0, 10));
			amnperiod.saveEx();	//	Update AMN_Period 
		}
		if (processMonitor != null)
		{
			processMonitor.statusUpdate(Msg_Value);
		}
		
		return true;
		
	}	//	updateRefDateAmnPeriod

	
	/**
	 * sqlGetAMNPeriodDays (int PeriodDays)
	 * @param p_AMN_Period_ID	Period ID
	 */
	public static int sqlGetAMNPeriodDays (int p_AMN_Period_ID)
	
	{
		String sql;
		int PeriodDays = 0;
		// AMN_Location
    	sql = "select (age(amndateini) - age(amndateend) +1 ) as perioddays from amn_period WHERE amn_period_id=?" ;
    	PeriodDays = DB.getSQLValue(null, sql, p_AMN_Period_ID);	
		return PeriodDays;	
	}
	
	/**
	 * Description: Finds Previos MAMN_period from AMN_Period_ID
	 * Used for: Attendance calculation on 15 Days Periods and 30 Days Periods
	 * @param ctx
	 * @param p_AMN_Period_ID
	 * @return MAMN_Period
	 */
	public static MAMN_Period findPreviousPeriod(Properties ctx, int p_AMN_Contract_ID,  int p_AMN_Period_ID) {
		
		MAMN_Period amnperiod =  get(ctx, p_AMN_Period_ID);
		GregorianCalendar cal = new GregorianCalendar();
		MAMN_Contract amncontract = new MAMN_Contract(ctx, amnperiod.getAMN_Contract_ID(), null);
		int C_Period_ID = amnperiod.getC_Period_ID();
		MPeriod mperiod = new MPeriod(ctx, C_Period_ID, null);
		MAMN_Period retValue = null;
		Timestamp ppStartDate=null;
		Timestamp ppEndDate=null;

		cal.setTime(amnperiod.getAMNDateIni());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// EndDay of Previous Period is equal Start Day of Period less 1
		ppEndDate = new Timestamp(cal.getTimeInMillis());
		// Contract with Payroll Days 15 and 30 have irregular number of days (28-30-31)
		// Convert To Integer
		int PayrollDaysInt =Integer.valueOf(amncontract.getPayRollDays().intValue());
		switch (PayrollDaysInt) {
			case 7:  
				cal.add(Calendar.DAY_OF_YEAR, -1*amncontract.getPayRollDays().intValue() +1);
				break;
            case 14: 
    			cal.add(Calendar.DAY_OF_YEAR, -1*amncontract.getPayRollDays().intValue() +1);		
    			break;
            case 15:
            	if (cal.get(Calendar.DAY_OF_MONTH) == 15) 
            		cal.set(Calendar.DAY_OF_MONTH, 1);
            	else
            		cal.set(Calendar.DAY_OF_MONTH, 16);
            	break;
            case (30):
            	cal.set(Calendar.DAY_OF_MONTH, 1);
        		break;
        	default:
        		cal.add(Calendar.DAY_OF_YEAR, -1*amncontract.getPayRollDays().intValue() +1);		
    			break;
		}         
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		ppStartDate = new Timestamp(cal.getTimeInMillis());	
		//log.warning(" FindPP - ppStartDate:"+ppStartDate+"  ppEndDate:"+ppEndDate);
		//log.warning(" FindPP - Process:"+ amnperiod.getAMN_Process_ID()+"  Contract:"+ amncontract.getAMN_Contract_ID());
		retValue = MAMN_Period.findByCalendar(Env.getCtx(), null,  mperiod.getC_Year_ID(), amnperiod.getAMN_Process_ID(), amncontract.getAMN_Contract_ID(), ppStartDate, ppEndDate);
		return retValue;
	}
	
	/**getStartAttendanceDate
	 * Description: Returns Valid Init Attendance Day for Transfering to payroll
	 * Used on 15 days Attendance Payroll
	 * @param ctx
	 * @param p_AMN_Period_ID
	 * @return Timestap Start Attendance day 
	 * 
	 */
	public Timestamp getStartAttendanceDate(Properties ctx, int p_AMN_Contract_ID, int p_AMN_Period_ID) {

		Timestamp retValue = null;
		Timestamp ppEndDate= null;
		GregorianCalendar cal = new GregorianCalendar();
		MAMN_Period amnperiod = MAMN_Period.get(ctx, p_AMN_Period_ID);
		MAMN_Contract amncontract = MAMN_Contract.get(ctx, p_AMN_Contract_ID);
		//log.warning("amncontract.getAMN_Contract_ID:"+amncontract.getAMN_Contract_ID());
		if (amncontract.getSlackDays() == 0) {
			retValue = amnperiod.getAMNDateIni();
		} else {
			MAMN_Period pramnperiod = findPreviousPeriod(ctx, p_AMN_Contract_ID, p_AMN_Period_ID);
			if ( pramnperiod != null ){
				// Get End Attendance Date from Previous Period
				ppEndDate = getEndAttendanceDate(ctx, p_AMN_Contract_ID, pramnperiod.getAMN_Period_ID());
				cal.setTime(ppEndDate);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DAY_OF_YEAR, +1);
				retValue = new Timestamp(cal.getTimeInMillis());
			}	
		}
		//log.warning("getStartAttendanceDate retValue:"+retValue);					
		return retValue;
		
	}
	/* getEndAttendanceDate
	 * Description: Returns Valid End Attendance Day for Transfering to payroll
	 * Used on 15 days Attendance Payroll when SlackDays != 0
	 * @param ctx
	 * @param p_AMN_Period_ID
	 * @return Timestap Start Attendance day 
	 * */
	
	public Timestamp getEndAttendanceDate(Properties ctx, int p_AMN_Contract_ID, int p_AMN_Period_ID) {

		Timestamp retValue = null;
		GregorianCalendar calenddow = new GregorianCalendar();
		MAMN_Period amnperiod = new MAMN_Period(ctx, p_AMN_Period_ID, null);
		MAMN_Contract amncontract = new MAMN_Contract(ctx, p_AMN_Contract_ID, null);
		// Slack Days Check
		if (amncontract.getSlackDays() == 0) {
			// If SlackDay is Zero Then EndAttendance Day is equal to Period End Day
			retValue = amnperiod.getAMNDateEnd();
			
		} else {
			// Slack Days not Zero
			// Calculate Period End Day of the week
			int SlackDays =0;
			int PeriodEndDow = 0;
			int PeriodInitDow = Integer.parseInt(amncontract.getInitDow().trim());	 
	        switch (PeriodInitDow) {
	            case 0: PeriodEndDow = 6;
	                    break;
	            case 1: PeriodEndDow = 0;
                		break;
	            case 2: PeriodEndDow = 1;
	            		break;
	            case 3: PeriodEndDow = 2;
	            		break;
	            case 4: PeriodEndDow = 3;
        				break;
	            case 5: PeriodEndDow = 4;
        				break;
	            case 6: PeriodEndDow = 0;
        				break;        				
	        }
			calenddow.setTime(amnperiod.getAMNDateEnd());
			calenddow.set(Calendar.HOUR_OF_DAY, 0);
			calenddow.set(Calendar.MINUTE, 0);
			calenddow.set(Calendar.SECOND, 0);
			calenddow.set(Calendar.MILLISECOND, 0);
			//while (days<7) {
			for ( int days=1; days <=7 ; days++) {
				calenddow.add (Calendar.DAY_OF_YEAR, -1);
				//Timestamp temp = new Timestamp(calenddow.getTimeInMillis());
				//log.warning("1  PeriodInitDow:"+PeriodInitDow+  "PeriodEndDow:"+PeriodEndDow+"  For End Date:"+temp+"  Calendar.DAY_OF_WEEK:"+(calenddow.get(Calendar.DAY_OF_WEEK)-1));			
				if (calenddow.get(Calendar.DAY_OF_WEEK) -1 == PeriodEndDow) {
					break;
				}
				SlackDays = SlackDays+1;
			}
			//log.warning("Period Calculated SlackDays:"+SlackDays+" amncontract.getSlackDays():"+amncontract.getSlackDays());
			if (SlackDays >= amncontract.getSlackDays()) {
				retValue = new Timestamp(calenddow.getTimeInMillis());
			} else {
				SlackDays =0;
				//calenddow.add (Calendar.DAY_OF_YEAR, -1);
				for ( int days=1; days <=7 ; days++) {
					calenddow.add (Calendar.DAY_OF_YEAR, -1);
					//Timestamp temp = new Timestamp(calenddow.getTimeInMillis());
					//log.warning("2 For End Date:"+temp+"  Calendar.DAY_OF_WEEK:"+(calenddow.get(Calendar.DAY_OF_WEEK)-1));			
					if (calenddow.get(Calendar.DAY_OF_WEEK) -1 == PeriodEndDow) {
						break;
					}
					SlackDays = SlackDays++;
				}
				retValue = new Timestamp(calenddow.getTimeInMillis());
			}
			// Verify ..... Incompleted
		}	
		//log.warning("getEndAttendanceDate retValue:"+retValue);					
		return retValue;
	}
}

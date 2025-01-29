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

import java.sql.*;
import java.util.*;

import org.adempiere.util.IProcessUI;
import org.compiere.model.MClient;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */
public class MAMN_Payroll_Assist extends X_AMN_Payroll_Assist {
	
	private static final long serialVersionUID = 3248799250517025407L;
	
    /** DAY_OF_WEEK CONSTANTS */
    public static final String DAYOFWEEK_SUNDAY 	= "1";
    public static final String DAYOFWEEK_MONDAY 	= "2";
    public static final String DAYOFWEEK_TUESDAY 	= "3";
    public static final String DAYOFWEEK_WEDNESDAY 	= "4";
    public static final String DAYOFWEEK_THURSDAY 	= "5";
    public static final String DAYOFWEEK_FRIDAY 	= "6";
    public static final String DAYOFWEEK_SATURDAY 	= "7";
    // Indicates if Saturday is considered Business Day */
    public static boolean SaturdayBusinessDay = false;
    public static boolean SundayBusinessDay = false;
    
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Assist> s_cache = new CCache<Integer,MAMN_Payroll_Assist>(Table_Name, 10);

	/**
	 * @param p_ctx
	 * @param AMN_Payroll_Assist_ID
	 * @param p_trxName
	 */
    public MAMN_Payroll_Assist(Properties p_ctx, int AMN_Payroll_Assist_ID, String p_trxName) {
	    super(p_ctx, AMN_Payroll_Assist_ID, p_trxName);
	    // TODO Auto-generated constructor stub
    }
    
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Payroll_Assist(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // TODO Auto-generated constructor stub
    }

    /*
     * getPayrollAssist_DayofWeek
     * param: p_DateAssit
     * return: retValue (0-6)
     */
	static public String getPayrollAssist_DayofWeek (Timestamp p_DateAssit) {
		String retValue="0";

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(p_DateAssit);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
			retValue = DAYOFWEEK_SUNDAY;
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY )
			retValue = DAYOFWEEK_MONDAY;
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY )
            retValue = DAYOFWEEK_TUESDAY;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY )
            retValue = DAYOFWEEK_WEDNESDAY;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY )
            retValue = DAYOFWEEK_THURSDAY;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY )
            retValue = DAYOFWEEK_FRIDAY;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY )
            retValue = DAYOFWEEK_SATURDAY;

		return retValue;
		
	}

    /*
     * getPayrollAssist_DayofWeekName
     * param: p_DateAssit
     * return: retValue SUNDAY-MONDAY ...
     */
	static public String getPayrollAssist_DayofWeekName (Timestamp p_DateAssit) {
		String retValue = Msg.getMsg(Env.getCtx(), "Sunday");;

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(p_DateAssit);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);		
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
			retValue = Msg.getMsg(Env.getCtx(), "Sunday");
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY )
			retValue = Msg.getMsg(Env.getCtx(), "Monday");
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY )
            retValue = Msg.getMsg(Env.getCtx(), "Tuesday");
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY )
            retValue = Msg.getMsg(Env.getCtx(), "Wednesday");
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY )
             retValue = Msg.getMsg(Env.getCtx(), "Thursday");
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY )
            retValue = Msg.getMsg(Env.getCtx(), "Friday");
         if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY )
            retValue = Msg.getMsg(Env.getCtx(), "Saturday");

		return retValue;
		
	}
	

	/**
	 * 
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Period_ID		AMN_Period_ID
	 * @param p_AMN_Employee_ID		Employee
	 * @return MAMN_Period
	 */
	public static MAMN_Payroll_Assist findByCalendar(Properties ctx, Locale locale, 
				int p_AMN_Period_ID, int p_AMN_Employee_ID,Timestamp p_PeriodDate) {
				
		MAMN_Payroll_Assist retValue = null;
		String sql = "SELECT * "
			+ "FROM amn_payroll_assist "
			+ "WHERE AMN_Period_ID=?"
			+ " AND AMN_Employee_ID=?"
			+ " AND amndateassist=?"
			;        
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Period_ID);
            pstmt.setInt (2, p_AMN_Employee_ID);
            pstmt.setTimestamp (3, p_PeriodDate);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Payroll_Assist amnpayrollassist = new MAMN_Payroll_Assist(ctx, rs, null);
				Integer key = new Integer(amnpayrollassist.getAMN_Payroll_Assist_ID());
				s_cache.put (key, amnpayrollassist);
				if (amnpayrollassist.isActive())
					retValue = amnpayrollassist;
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
	 * 
	 * @param ctx
	 * @param locale
	 * @param p_Ad_Org_ID
	 * @param p_C_Year_ID  		Accounting Fiscal Year 
	 * @param p_C_Period_ID		Period ID
	 * @param p_PeriodDate
	 * @return MAMN_Period_Assist
	 */
	public boolean createAmnPayrollAssist(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,
			int p_AMN_Period_ID, int p_AMN_Employee_ID )
	{

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
		MAMN_Payroll_Assist amnpayrollassist = MAMN_Payroll_Assist.findByCalendar(Env.getCtx(), locale, p_AMN_Period_ID, p_AMN_Employee_ID, null);
		if (amnpayrollassist == null)
		{
			amnpayrollassist = new MAMN_Payroll_Assist(getCtx(), getAMN_Payroll_Assist_ID(), get_TrxName());
			amnpayrollassist.setAD_Org_ID(p_AD_Org_ID);
			amnpayrollassist.setIsActive(true);

		}
		else
		{
			//amnpayrollassist.setAMN_AssistRecord(Msg.getMsg(Env.getCtx(), "Day")+"_"+p_PeriodDate.toString().substring(0, 10)+"_"+p_PeriodDay);
			amnpayrollassist.setDescription("");
		}
		if (processMonitor != null)
		{
			//amnpayrollassist.setAMN_AssistRecord(Msg.getMsg(Env.getCtx(), "Day")+"_"+p_PeriodDate.toString().substring(0, 10)+"_"+p_PeriodDay);
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Day")+": "+p_PeriodDate.toString());
		}
		
		amnpayrollassist.saveEx(get_TrxName());	//	Creates AMN_Payroll_Assist Control
		return true;
		
	}	//	AMN_Payroll_Assist
	
	/**
	 * findMAMN_Payroll_AssistByID
	 * @param ctx
	 * @param p_MAMN_Payroll_Assist_ID
	 * @return
	 */
	public static MAMN_Payroll_Assist findMAMN_Payroll_AssistByRowID(Properties ctx, int p_MAMN_Payroll_Assist_Row_ID) {
			
	MAMN_Payroll_Assist retValue = null;
	
	String sql = "SELECT amn_payroll_assist_id "
		+ "FROM amn_payroll_assist "
		+ "WHERE AMN_Payroll_Assist_Row_ID=?"
		;        
	
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	try
	{
		pstmt = DB.prepareStatement(sql, null);
        pstmt.setInt (1, p_MAMN_Payroll_Assist_Row_ID);
		rs = pstmt.executeQuery();
		if (rs.next())
		{
			retValue = new MAMN_Payroll_Assist(ctx, rs.getInt(1), null);
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
}

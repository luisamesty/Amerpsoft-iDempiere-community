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
    
	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Assist_Proc> s_cache = new CCache<Integer,MAMN_Payroll_Assist_Proc>(Table_Name, 10);

	/**
	 * @param p_ctx
	 * @param AMN_Payroll_Assist_Proc_ID
	 * @param p_trxName
	 */
    public MAMN_Payroll_Assist_Proc(Properties p_ctx, int AMN_Payroll_Assist_Proc_ID, String p_trxName) {
	    super(p_ctx, AMN_Payroll_Assist_Proc_ID, p_trxName);
	    // TODO Auto-generated constructor stub
    }

	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Payroll_Assist_Proc(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // TODO Auto-generated constructor stub
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
			int p_AD_Client_ID, int p_AD_Org_ID,
			int p_AMN_Employee_ID, Timestamp p_Event_Date , int p_AMN_Shift_ID,
			Boolean p_isDescanso, Boolean p_isExcused ,
			Timestamp p_Shift_In1, Timestamp p_Shift_Out1, 
			Timestamp p_Shift_In2, Timestamp p_Shift_Out2,
			BigDecimal p_Shift_HED, BigDecimal p_Shift_HEN, 
			BigDecimal p_Shift_HND, BigDecimal p_Shift_HNN,
			BigDecimal p_Shift_Attendance, BigDecimal p_Shift_AttendanceBonus,
			String p_Description
			)
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
		// p_DayOfWeek
		String p_DayOfWeek="0";
		String Shift_Value="";
		String locMessage="";
		p_DayOfWeek=MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(p_Event_Date);
		// Employee Value
		MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
		String Employee_Value=amnemployee.getValue().trim();
		locMessage= locMessage+amnemployee.getValue().trim()+"-"+amnemployee.getName().trim()+" \n";
		// Shift Value if p_AMN_Shift_ID != 0
	    if (p_AMN_Shift_ID > 0) {
	    	MAMN_Shift amnshift = new MAMN_Shift(Env.getCtx(), p_AMN_Shift_ID, null);
	    	Shift_Value = amnshift.getValue().trim();
		} else {
			Shift_Value = "*****  "+Msg.getElement(Env.getCtx(), "AMN_Shift_ID")+" = 0   *****";
		}
	    locMessage= locMessage+Shift_Value+ Msg.getMsg(Env.getCtx(), "Date")+": "+p_Event_Date.toString().substring(0,10)+" \n";
		MAMN_Payroll_Assist_Proc amnpayrollassistproc = MAMN_Payroll_Assist_Proc.findbyEmployeeandDate(Env.getCtx(), locale,  p_AMN_Employee_ID, p_Event_Date);
		if (amnpayrollassistproc == null)
		{
			amnpayrollassistproc = new MAMN_Payroll_Assist_Proc(Env.getCtx(), 0, null);
			amnpayrollassistproc.setAD_Client_ID(p_AD_Client_ID);
			amnpayrollassistproc.setAD_Org_ID(p_AD_Org_ID);
			amnpayrollassistproc.setdayofweek(p_DayOfWeek);
			amnpayrollassistproc.setAMN_Shift_ID(p_AMN_Shift_ID);
			amnpayrollassistproc.setEvent_Date(p_Event_Date);
			amnpayrollassistproc.setAMN_Employee_ID(p_AMN_Employee_ID);
			amnpayrollassistproc.setDescription(p_Description.trim());
			amnpayrollassistproc.setName(Employee_Value.trim()+"-"+Shift_Value+"-"+p_Event_Date);
			amnpayrollassistproc.setDescanso(p_isDescanso);
			amnpayrollassistproc.setShift_In1(p_Shift_In1);
			amnpayrollassistproc.setShift_Out1(p_Shift_Out1);
			amnpayrollassistproc.setShift_In2(p_Shift_In2);
			amnpayrollassistproc.setShift_Out2(p_Shift_Out2);
			amnpayrollassistproc.setShift_HED(p_Shift_HED);
			amnpayrollassistproc.setShift_HEN(p_Shift_HEN);
			amnpayrollassistproc.setShift_HND(p_Shift_HND);
			amnpayrollassistproc.setShift_HNN(p_Shift_HNN);
			amnpayrollassistproc.setShift_Attendance(p_Shift_Attendance);
			amnpayrollassistproc.setShift_AttendanceBonus(p_Shift_AttendanceBonus);			
			amnpayrollassistproc.setExcused(p_isExcused);
			amnpayrollassistproc.setIsActive(true);	
		}
		else
		{
			//amnpayrollassist.setAMN_AssistRecord(Msg.getMsg(Env.getCtx(), "Day")+"_"+p_PeriodDate.toString().substring(0, 10)+"_"+p_PeriodDay);
			amnpayrollassistproc.setDescription(p_Description.trim());
			amnpayrollassistproc.setName(Employee_Value.trim()+"-"+Shift_Value+"-"+p_Event_Date.toString().substring(0, 10));
			amnpayrollassistproc.setDescanso(p_isDescanso);
			amnpayrollassistproc.setShift_In1(p_Shift_In1);
			amnpayrollassistproc.setShift_Out1(p_Shift_Out1);
			amnpayrollassistproc.setShift_In2(p_Shift_In2);
			amnpayrollassistproc.setShift_Out2(p_Shift_Out2);
			amnpayrollassistproc.setShift_HED(p_Shift_HED);
			amnpayrollassistproc.setShift_HEN(p_Shift_HEN);
			amnpayrollassistproc.setShift_HND(p_Shift_HND);
			amnpayrollassistproc.setShift_HNN(p_Shift_HNN);
			amnpayrollassistproc.setShift_Attendance(p_Shift_Attendance);
			amnpayrollassistproc.setShift_AttendanceBonus(p_Shift_AttendanceBonus);		
			amnpayrollassistproc.setExcused(p_isExcused);
			amnpayrollassistproc.setIsActive(true);	
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Day")+": "+p_PeriodDate.toString());
			processMonitor.statusUpdate(locMessage);
		}
		amnpayrollassistproc.saveEx();	//	Creates AMN_Payroll_Assist Control
		return true;
		
	}	//	AMN_Payroll_Assist
}

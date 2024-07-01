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

/**
 * @author luisamesty
 *
 */
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import org.adempiere.util.IProcessUI;
import org.compiere.model.MClient;
import org.compiere.util.*;


public class MAMN_Period_Assist extends X_AMN_Period_Assist {

	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
	/**	Cache							*/
	private static CCache<Integer,MAMN_Period_Assist> s_cache = new CCache<Integer,MAMN_Period_Assist>(Table_Name, 10);
	
	/**
	 * @param p_ctx
	 * @param AMN_Period_Assist_ID
	 * @param p_trxName
	 */
    public MAMN_Period_Assist(Properties p_ctx, int AMN_Period_Assist_ID, String p_trxName) {
	    super(p_ctx, AMN_Period_Assist_ID, p_trxName);
	    // TODO Auto-generated constructor stub
    }
	/**
	 * @param p_ctx
	 * @param p_rs
	 * @param p_trxName
	 */
    public MAMN_Period_Assist(Properties p_ctx, ResultSet p_rs, String p_trxName) {
	    super(p_ctx, p_rs, p_trxName);
	    // TODO Auto-generated constructor stub
    }


	/**
	 * 
	 * @param ctx
	 * @param locale
	 * @param p_Ad_Org_ID
	 * @param p_C_Year_ID  		Accounting Fiscal Year 
	 * @param p_C_Period_ID		Period ID
	 * @param p_PeriodDate
	 * @return MAMN_Period
	 */
	public static MAMN_Period_Assist findByCalendar(Properties ctx, Locale locale, 
				int p_C_Year_ID, int p_C_Period_ID,Timestamp p_PeriodDate) {
				
		MAMN_Period_Assist retValue = null;
		String sql = "SELECT * "
			+ "FROM amn_period_assist "
			+ "WHERE c_year_id=?"
			+ " AND c_period_id=?"
			+ " AND amndateassist=?"
			;        
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_C_Year_ID);
            pstmt.setInt (2, p_C_Period_ID);
            pstmt.setTimestamp (3, p_PeriodDate);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Period_Assist amnperiodassist = new MAMN_Period_Assist(ctx, rs, null);
				Integer key = new Integer(amnperiodassist.getAMN_Period_Assist_ID());
				s_cache.put (key, amnperiodassist);
				if (amnperiodassist.isActive())
					retValue = amnperiodassist;
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
	public boolean createAmnPeriodAssist(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,int p_C_Year_ID, int p_C_Period_ID, 
				Timestamp p_PeriodDate, String p_PeriodDay, Boolean IsNonBusinessDay) {
		// Translate p_PeriodDay
		String PerDay = Msg.getMsg(Env.getCtx(), p_PeriodDay.trim());
		String Trl_periodDay = PerDay.substring(0, 1).toUpperCase() + PerDay.substring(1);
		//
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
		MAMN_Period_Assist amnperiodassist = MAMN_Period_Assist.findByCalendar(Env.getCtx(), locale, p_C_Year_ID, p_C_Period_ID, p_PeriodDate);
		if (amnperiodassist == null)
		{
			amnperiodassist = new MAMN_Period_Assist(getCtx(), getAMN_Period_Assist_ID(), get_TrxName());
			amnperiodassist.setAD_Org_ID(p_AD_Org_ID);
			amnperiodassist.setC_Year_ID(p_C_Year_ID);
			amnperiodassist.setC_Period_ID(p_C_Period_ID);;
			amnperiodassist.setIsActive(true);
			amnperiodassist.setName(Msg.getMsg(Env.getCtx(), "Day")+"_"+p_PeriodDate.toString().substring(0, 10)+"_"+Trl_periodDay);
			amnperiodassist.setAMNDateAssist(p_PeriodDate);
			amnperiodassist.setIsNonBusinessDay(IsNonBusinessDay);
		}
		else
		{
			amnperiodassist.setName(Msg.getMsg(Env.getCtx(), "Day")+"_"+p_PeriodDate.toString().substring(0, 10)+"_"+Trl_periodDay);
			amnperiodassist.setIsNonBusinessDay(IsNonBusinessDay);
		}
		if (processMonitor != null)
		{
			processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Day")+": "+p_PeriodDate.toString());
		}
		
		amnperiodassist.saveEx(get_TrxName());	//	Creates AMNPeriod Control
		return true;
		
	}	//	createAmnPeriod

}

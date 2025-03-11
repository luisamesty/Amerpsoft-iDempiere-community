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

import java.sql.*;
import java.util.Properties;

import org.compiere.util.*;

public class MAMN_Shift_Detail extends X_AMN_Shift_Detail {

	static CLogger log = CLogger.getCLogger(MAMN_Shift_Detail.class);
	
	/**	Cache							*/
	private static CCache<Integer,MAMN_Shift_Detail> s_cache = new CCache<Integer,MAMN_Shift_Detail>(Table_Name, 10);

	/**
	 * 
	 */
	private static final long serialVersionUID = 6368347091643033115L;

	public MAMN_Shift_Detail(Properties ctx, int AMN_Shift_Detail_ID,
			String trxName) {
		super(ctx, AMN_Shift_Detail_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Shift_Detail(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * MAMN_Shift_Detail findByEventDate
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Shift_ID		AMN_Shift_ID
	 * @param p_AMN_Employee_ID		Employee
	 * @return MAMN_Shift_Detail
	 */
	public static MAMN_Shift_Detail findByEventDate(Properties ctx,
				 int p_AMN_Shift_ID, Timestamp p_Event_Date) {
				
		MAMN_Shift_Detail retValue = null;
		String Event_Dow = MAMN_Payroll_Assist.getPayrollAssist_DayofWeek(p_Event_Date);
		String sql = "SELECT * "
			+ "FROM amn_shift_detail "
			+ "WHERE  AMN_Shift_ID=?"
			+ " AND dayofweek=?"
			;        
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Shift_ID);
            pstmt.setString (2, Event_Dow);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Shift_Detail amnshiftdetail = new MAMN_Shift_Detail(ctx, rs, null);
				Integer key = new Integer(amnshiftdetail.getAMN_Shift_Detail_ID());
				s_cache.put (key, amnshiftdetail);
				if (amnshiftdetail.isActive())
					retValue = amnshiftdetail;
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

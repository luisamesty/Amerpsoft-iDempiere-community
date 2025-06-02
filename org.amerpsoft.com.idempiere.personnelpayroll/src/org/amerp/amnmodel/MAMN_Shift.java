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

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MAMN_Shift extends X_AMN_Shift {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6862941790531908485L;

	/**	Cache		MAMN_Shift					*/
	private static CCache<Integer,MAMN_Shift> s_cache = new CCache<Integer,MAMN_Shift >(Table_Name, 10);

	// Constructor base
    public MAMN_Shift() {
        super(Env.getCtx(), 0, null); // Inicialización básica
    }
    
	public MAMN_Shift(Properties ctx, int AMN_Shift_ID, String trxName) {
		super(ctx, AMN_Shift_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Shift(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get Shift from Cache
	 * @param ctx context
	 * @param p_MAMN_Shift_ID
	 * @return MAMN_Shift
	 */
	public static MAMN_Shift get (Properties ctx, int p_AMN_Shift_ID)
	{
		if (p_AMN_Shift_ID <= 0)
			return null;
		//
		Integer key = new Integer(p_AMN_Shift_ID);
		MAMN_Shift retValue = (MAMN_Shift) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Shift (ctx, p_AMN_Shift_ID, null);
		if (retValue.getAMN_Shift_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get

	/**
	 * sqlGetDefaultAMN_ShiftID ()
	 * @param 
	 * return int AMN_ShiftID
	 */
	public Integer sqlGetDefaultAMN_Shift_ID (int p_AD_Client_ID)
	
	{
		String sql;
		Integer recNo=0;
		Integer AMN_Shift_ID = 0;
		// recNo
    	sql = "select count(*) from amn_shift WHERE isdefault='Y' AND AD_Client_ID=? " ;
    	recNo = DB.getSQLValue(null, sql, p_AD_Client_ID);		
		// AMN_Shift_ID
    	if (recNo > 0) {
	    	sql = "select amn_shift_id from amn_shift WHERE isdefault='Y' AND AD_Client_ID=? " ;
	    	AMN_Shift_ID = DB.getSQLValue(null, sql, p_AD_Client_ID);	
			return AMN_Shift_ID;	
    	} else {
    		return 0;
    	}
	}
	
	/**
	 * getAMN_Shift_Detail_Lines
	 * Return Shift Details based on AMN_Shift_ID and Where clause.
	 * Example of whereClause  "AND dayofweek ='"+MAMN_Payroll_Assist.DAYOFWEEK_SATURDAY+"'"
	 * @param p_AMN_Shift_ID
	 * @param whereClause
	 * @return
	 */
	public MAMN_Shift_Detail[] getAMN_Shift_Detail_Lines (int p_AMN_Shift_ID, String whereClause)	{
		String whereClauseFinal = "AMN_Shift_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MAMN_Shift_Detail> list = null;
		try {
			list = new Query(getCtx(), MAMN_Shift_Detail.Table_Name, whereClauseFinal, get_TrxName())
											.setParameters(p_AMN_Shift_ID)
											.setOrderBy("dayofweek")
											.list();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return list.toArray(new MAMN_Shift_Detail[list.size()]);
	}	//	getLines
	
	/**
	 * boolean isSaturdayBusinessDay
	 * Returns if Saturday is BusinessDay Giving AMN_Shift ID as parameter
	 * @param p_AMN_Shift_ID
	 * @param p_AD_Client_ID
	 * @return
	 */
	public boolean isSaturdayBusinessDay(int p_AMN_Shift_ID) {
		
		MAMN_Shift_Detail[]  msd = null;
		boolean retValue = MAMN_Payroll_Assist.SaturdayBusinessDay;
		try {
			msd = getAMN_Shift_Detail_Lines(p_AMN_Shift_ID, "AND dayofweek ='"+MAMN_Payroll_Assist.DAYOFWEEK_SATURDAY+"'");
		} catch (Exception e) {
			// 
			log.warning("Error Shift detail="+e.getMessage());
		}
		if (msd.length > 0)
			retValue = true;
		return retValue;
	}
	
	/**
	 * isSaturdayBusinessDayfromPayroll
	 * Returns if Staurday is BusinessDay Giving AMN_Payroll ID as parameter
	 * @param p_AMN_Payroll_ID
	 * @return
	 */
	public boolean isSaturdayBusinessDayfromPayroll(Properties ctx, int p_AMN_Payroll_ID) {
		
		boolean retValue = MAMN_Payroll_Assist.SaturdayBusinessDay;
		if (p_AMN_Payroll_ID >0) {
			MAMN_Payroll amnpayroll = new MAMN_Payroll(ctx, p_AMN_Payroll_ID, null);
			int AMN_Shift_ID = sqlGetDefaultAMN_Shift_ID(amnpayroll.getAD_Client_ID());
			MAMN_Employee emp = new MAMN_Employee(ctx, amnpayroll.getAMN_Employee_ID(), null);
			if (emp != null && amnpayroll != null) {
				AMN_Shift_ID = emp.getAMN_Shift_ID();
				retValue = isSaturdayBusinessDay(AMN_Shift_ID);
			}
		}
		return retValue;
	}
}

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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.compiere.util.*;

public class MAMN_Process extends X_AMN_Process {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5895663741916623734L;
	
	/**	Cache		MAMN_Process					*/
	private static CCache<Integer,MAMN_Process> s_cache = new CCache<Integer,MAMN_Process >(Table_Name, 10);

	CLogger log = CLogger.getCLogger(MAMN_Process.class);

	public MAMN_Process(Properties ctx, int AMN_Process_ID, String trxName) {
		super(ctx, AMN_Process_ID, trxName);
		// TODO Auto-generated constructor stub
		//log.warning("---------------");
	}

	public MAMN_Process(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
		//log.warning("---------------");
	}
//
//	@Override
//	protected boolean beforeSave(boolean newRecord) {
//		// TODO Auto-generated method stub
//		//log.warning("---------------");
//		return super.beforeSave(newRecord);
//	}
//	
//	@Override
//	protected boolean afterSave(boolean newRecord, boolean success) {
//		// TODO Auto-generated method stub
//		//log.warning("---------------");
//		return super.afterSave(newRecord, success);
//	}
//	
//	@Override
//	protected boolean beforeDelete() {
//		// TODO Auto-generated method stub
//		//log.warning("---------------");
//		return super.beforeDelete();
//	}
//	
//	@Override
//	protected boolean afterDelete(boolean success) {
//		// TODO Auto-generated method stub
//		//log.warning("---------------");
//		return super.afterDelete(success);
//	}
//	
	/**
	 * Get Process from Cache
	 * @param ctx context
	 * @param MAMN_Process id
	 * @return MProcess
	 */
	public static MAMN_Process get (Properties ctx, int MAMN_Process_ID)
	{
		if (MAMN_Process_ID <= 0)
			return null;
		//
		Integer key = new Integer(MAMN_Process_ID);
		MAMN_Process retValue = (MAMN_Process) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Process (ctx, MAMN_Process_ID, null);
		if (retValue.getAMN_Process_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get
	
	/**
	 * sqlGetAMNProcessValue (int p_AMN_Process_ID)
	 * @param p_AMN_Process_ID	Payroll Process
	 */
	public String sqlGetAMNProcessValue (int p_AMN_Process_ID)
	
	{
		String sql;
		String Process_Value="";
		// AMN_Process
    	sql = "select value from amn_process WHERE amn_process_id=?" ;
    	Process_Value = DB.getSQLValueString(null, sql, p_AMN_Process_ID).trim();	
		return Process_Value;	
	}
	
	/**
	 * Description: Returns AMN_Process_ID from AMN_Process_Value for
	 * 				a given Client
	 * sqlGetAMNProcessIDFromName (String p_AMN_Process_Value, Integer p_AD_Client_ID)
	 * @param p_AMN_Process_Value	'NN','NV', ...
	 */
	public static Integer sqlGetAMNProcessIDFromName (
			String p_AMN_Process_Value, Integer p_AD_Client_ID)
	
	{
		String sql;
		Integer Process_ID=0;
		// AD_client_ID = 
		// AMN_Process
    	sql = "select amn_process_id from amn_process " +
    			"WHERE amn_process_value=? " +
    			"AND ad_client_id=?"
    			;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
	        pstmt1.setString (1,p_AMN_Process_Value );
	        pstmt1.setInt (2, p_AD_Client_ID);
			//locMsg_Value=locMsg_Value+sql+" \n";
			rs = pstmt1.executeQuery();
			while (rs.next())
			{
				Process_ID = rs.getInt(1);
			}
		}
	    	catch (SQLException e)
	    {
	    }
		finally
		{
			DB.close(rs, pstmt1);
			rs = null; pstmt1 = null;
		}
	    return  Process_ID;	
	}
}

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
import java.util.Properties;

import org.compiere.util.DB;

public class MAMN_Department extends X_AMN_Department {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9180952110868972016L;

	public MAMN_Department(Properties ctx, int AMN_Department_ID, String trxName) {
		super(ctx, AMN_Department_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Department(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * sqlGetAMNDepartmentValue (int p_AMN_Department_ID)
	 * @param p_AMN_Department_ID	Payroll Department
	 */
	public String sqlGetAMNProcessValue (int p_AMN_Department_ID)
	
	{
		String sql;
		String Department_Value="";
		// AMN_Process
    	sql = "select value from amn_department WHERE amn_department_id=?" ;
    	Department_Value = DB.getSQLValueString(null, sql, p_AMN_Department_ID).trim();	
		return Department_Value;	
	}
}

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
import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.DB;

public class MAMN_Contract extends X_AMN_Contract {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3371170581802905271L;
	
	/**	Cache		MAMN_Contract					*/
	private static CCache<Integer,MAMN_Contract> s_cache = new CCache<Integer,MAMN_Contract >(Table_Name, 10);

	public MAMN_Contract(Properties ctx, int AMN_Contract_ID, String trxName) {
		super(ctx, AMN_Contract_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Contract(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get Contract from Cache
	 * @param ctx context
	 * @param p_MAMN_Contract_ID
	 * @return MContract
	 */
	public static MAMN_Contract get (Properties ctx, int p_MAMN_Contract_ID)
	{
		if (p_MAMN_Contract_ID <= 0)
			return null;
		//
		Integer key = new Integer(p_MAMN_Contract_ID);
		MAMN_Contract retValue = (MAMN_Contract) s_cache.get (key);
		if (retValue != null)
			return retValue;
		//
		retValue = new MAMN_Contract (ctx, p_MAMN_Contract_ID, null);
		if (retValue.getAMN_Contract_ID() != 0)
			s_cache.put (key, retValue);
		return retValue;
	} 	//	get
		
	/**
	 * sqlGetAMNContractValue (int p_AMN_Contract_ID)
	 * @param p_AMN_Contract_ID	Payroll Contract
	 */
	public String sqlGetAMNProcessValue (int p_AMN_Contract_ID)
	
	{
		String sql;
		String Contract_Value="";
		// AMN_Contract
		sql = "select value from amn_contract WHERE amn_contract_id=?" ;
		Contract_Value = DB.getSQLValueString(null, sql, p_AMN_Contract_ID).trim();	
		return Contract_Value;	
	}
	
	/**
	 * sqlGetAMNContractPayrollDays (int p_AMN_Contract_ID)
	 * @param p_AMN_Contract_ID	Payroll Contract
	 */
	static public BigDecimal sqlGetAMNContractPayrollDays (int p_AMN_Contract_ID)
	
	{
		String sql;

		BigDecimal PayrollDays = BigDecimal.valueOf(0.00);
		// PayrollDays
    	sql = "select payrolldays from amn_contract WHERE amn_contract_id=?" ;
    	
    	PayrollDays = DB.getSQLValueBD(null, sql, p_AMN_Contract_ID);
		return PayrollDays;
		

	}
//	sqlGetAMNContractPayrollDays
}

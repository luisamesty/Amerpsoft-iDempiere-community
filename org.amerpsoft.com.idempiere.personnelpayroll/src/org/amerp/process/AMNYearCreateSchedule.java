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
public class AMNYearCreateSchedule extends SvrProcess
{
	private int	p_C_Year_ID = 0;
	private String YearName;
	private int	p_C_Calendar_ID = 0;
	private int p_AMN_Employee_ID = 0;
	private int p_AMN_Contract_ID = 0;
	int p_AD_Org_ID=0;
	private String sql ="";
	String Process_Value="";
	Integer ID_PeriodIni = 0 ;
	Integer ID_PeriodEnd = 0 ;
	Timestamp GCdateAct;
	int InitDow=0;
	int AcctDow=0;
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
			else if (paraName.equals("C_Calendar_ID"))
				p_C_Calendar_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Contract_ID"))
				p_AMN_Contract_ID =  para.getParameterAsInt();
			else if (paraName.equals("AMN_Employee_ID"))
				p_AMN_Employee_ID =  para.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	@SuppressWarnings("deprecation")
    protected String doIt () throws Exception
	{
		// Verify if One Employee
		if (p_AMN_Employee_ID == 0) {
			
			
		} else {
			
		}

		//	return "@Error@";
		return null;
	}	//	doIt
	
	
}	//	AMNYearCreatePeriods


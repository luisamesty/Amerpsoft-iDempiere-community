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
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;

import javax.script.ScriptException;

import org.amerp.amnmodel.MAMN_Contract;
import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Deferred;
import org.amerp.amnmodel.MAMN_Payroll_Historic;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/** AMNPayrollRefresh
 * Description: Procedure called from iDempiere AD
 * 			Refresh or Recalculate One Payroll Receipt
 * 			For One Process, One Contract and One Period
 * 			One Employee.
 *  Result:	Recalculate One PayrollReceipt according to Payroll Concepts Rules
 * 			Update Header on AMN_Payroll and Lines on AMN_Payroll_detail
 * 			Using AmerpPayrollCalc Class
 * 
 * @author luisamesty
 *
 */
public class AMNPayrollRefresh extends SvrProcess {
	
	int p_AMN_Payroll_ID=0;
	String Msg_Value="";
	String AMN_Payroll_Value="";
	String AMN_Payroll_Name="";
	String AMN_Payroll_Description="";
	String sql="";	
	static BigDecimal SB3Mo = BigDecimal.valueOf(0);
	static BigDecimal SB12Mo = BigDecimal.valueOf(0);
	static BigDecimal SBUtil12Mo = BigDecimal.valueOf(0);
	static BigDecimal UTILIDDEV12Mo = BigDecimal.valueOf(0);

	static CLogger log = CLogger.getCLogger(AMNPayrollRefresh.class);

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
    @Override
    protected void prepare() {
	    // TODO Auto-generated method stub
    	//log.warning("........Here I'm in the prerare() - method");		
		ProcessInfoParameter[] paras = getParameter();
		for (ProcessInfoParameter para : paras)
		{
			String paraName = para.getParameterName();
			if (paraName.equals("AMN_Payroll_ID"))
				p_AMN_Payroll_ID = para.getParameterAsInt();		
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + paraName);
		}
    }

	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
    @Override
    protected  String doIt() throws Exception {
	    // TODO Auto-generated method stub
	    MAMN_Payroll amnpayroll = new MAMN_Payroll(getCtx(), p_AMN_Payroll_ID, null); 
    	MAMN_Process amnprocess = new MAMN_Process(getCtx(), amnpayroll.getAMN_Process_ID(), null);
    	AMN_Payroll_Value = amnpayroll.getValue().trim(); 
    	AMN_Payroll_Name = amnpayroll.getName();
    	AMN_Payroll_Description = amnpayroll.getDescription();
		Msg_Value=Msg_Value+ Msg.getElement(getCtx(),"AMN_Payroll_ID")+AMN_Payroll_Name.trim()+" \r\n";
		addLog(Msg_Value);
		if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
		{	
			// Verify if Process is NP Social Benefits 
			if (amnprocess.getAMN_Process_Value().equalsIgnoreCase("NP")) {
	    		SB3Mo= UPD_SALARYSB(getCtx(),p_AMN_Payroll_ID, get_TrxName());
	    		Msg_Value= " Salario Prestaciones Ultimos 3 Meses: "+SB3Mo.toString()+"\r\n";
	    		SB12Mo = UPD_SALARY12SB(getCtx(),p_AMN_Payroll_ID, get_TrxName());
	    		Msg_Value= Msg_Value+ " Salario Prestaciones Ultimos 12 Meses: "+SB12Mo.toString()+"\r\n";
	    		addLog(Msg_Value);
	    		// Update Quantity if exists
	    		PayrollUpdateSALARYSB(getCtx(), p_AMN_Payroll_ID);
	    	}
			// Verify if Process is NU Utilities 
			if (amnprocess.getAMN_Process_Value().equalsIgnoreCase("NU")) {
	    		SBUtil12Mo = UPD_SALUTIL12SB(getCtx(),p_AMN_Payroll_ID, get_TrxName());
	    		UTILIDDEV12Mo = UPD_UTILIDDEV12MO(getCtx(),p_AMN_Payroll_ID, get_TrxName());
	    		Msg_Value= " Salario Utilidades Ultimos 12 Meses: "+SBUtil12Mo.toString()+"\r\n";
	    		addLog(Msg_Value);
	    		Msg_Value= " Salario Devengado Base para Utilidades Ultimos 12 Meses: "+UTILIDDEV12Mo.toString()+"\r\n";
	    		addLog(Msg_Value);
	    		// Update Quantity if exists
	    		PayrollUpdateSALUTILSB(getCtx(), p_AMN_Payroll_ID);
	    		PayrollUpdateUTILIDDEV(getCtx(), p_AMN_Payroll_ID);
	    	}
			// Verify if Process is PO-PJ 
			if (amnprocess.getAMN_Process_Value().equalsIgnoreCase("PO") ||
					amnprocess.getAMN_Process_Value().equalsIgnoreCase("PJ")) {
				// recalculateCumulativeAmountBalance
				MAMN_Payroll_Deferred.recalculateCumulativeAmountBalance(getCtx(), p_AMN_Payroll_ID,  get_TrxName());
			}
			// Calculate receipt
			//log.warning("---Antes PayrollEvaluationArrayCalculate AMN_Payroll_ID:"+p_AMN_Payroll_ID);
			AmerpPayrollCalc.PayrollEvaluationArrayCalculate(getCtx(), p_AMN_Payroll_ID);
			//log.warning("---Despues PayrollEvaluationArrayCalculate AMN_Payroll_ID:"+p_AMN_Payroll_ID);
			// SALARY HISTORIC
	    	MAMN_Payroll_Historic amnpayrollhistoric = new MAMN_Payroll_Historic(getCtx(), 0, null);
			if (amnpayrollhistoric.createAmnPayrollHistoricV2(getCtx(), null, amnpayroll.getAMN_Employee_ID(), amnpayroll.getInvDateIni(), amnpayroll.getInvDateEnd(), get_TrxName()) ) {
				Msg_Value= Msg_Value+ "  OK \r\n";
			}
			//log.warning("---Despues createAmnPayrollHistoric AMN_Payroll_ID:"+p_AMN_Payroll_ID);
			// SALARY HISTORIC END
			addLog(Msg_Value);
		} else {
			Msg_Value=Msg_Value+" ** ALREADY PROCESSED - CAN'T BE RECALCULATED ** "+
					Msg.getElement(getCtx(),"AMN_Payroll_ID")+":"+AMN_Payroll_Name+" \r\n";
			addLog(Msg_Value);
		}
		//Msg_Value = AmerpPayrollCalc.logVariablesShow();
		//addLog(Msg_Value);
		return null;
    }

    
	/**
	 *  processDefaultValue: UPD_SALARYSB  
	 * 	Description: Return Salary Amount Base Updated to calc Social Benefits 
	 * 		Salary_Socialbenefits_Updated (Prestaciones Sociales)
	 *  	according to Labor Law.according to Labor Law 142 Lit A.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal UPD_SALARYSB (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Updated_Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 3 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 2 Month Before
		cal.add(Calendar.MONTH, -2);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_updated( ? , ? , ? , ? , ?) / 3 ";
		PreparedStatement pstmt = null;
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
           //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Updated_Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Updated_Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("NP Employee_Salary:"+Employee_Salary+"  Historic_Salary:"+Historic_Salary);
		retValue = Updated_Historic_Salary.divide( BigDecimal.valueOf(30) ,2, RoundingMode.CEILING);
		return retValue;
	}
	
	/**
	 *  processDefaultValue: UPD_SALARY12SB
	 * 	Description: Return Salary Amount Base UPDATED to calc Social Benefits 
	 * 		(Prestaciones Sociales) last 12 Months
	 *  	according to Labor Law 142 Lit B.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal UPD_SALARY12SB (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Updated_Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 12 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 11 Month Before
		cal.add(Calendar.MONTH, -11);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_hist_updated( ? , ? , ? , ? , ?) / 12 ";
		PreparedStatement pstmt = null;
//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Updated_Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Updated_Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		//log.warning("Historic_Salary:"+Historic_Salary);
		retValue = Updated_Historic_Salary.divide( BigDecimal.valueOf(30) ,2, RoundingMode.CEILING);
		return retValue;
	}

	/*******************************************************************************************
   	// Payroll UPDATE UPD_SALARYSB AND UPD_SALARY12SB
   	// UPD_SALARYSB: Concept Tags SALARYSB or SALPREST or SALARIOSB
   	// UPD_SALARY12SB: Cocept Tags SALARY12SB or SAL12PREST or SALARIO12P
   	// 
   	//
	// Parameters:
	// 	p_ctx
	// 	p_AMN_Payroll_ID
	//  Notes: this Method is Used by AMNPayrollRefresh Process Called from Payroll Preparing
	//			Window 
	//  Also Called by  PayrollDetail Callout on Concept_Types_Proc Value
	//  Also Called by Event AMN Payroll event
	//***************************************************************************************/
	
	public static void PayrollUpdateSALARYSB (Properties p_ctx, int p_AMN_Payroll_ID ) throws ScriptException
	{
		CLogger.getCLogger(AmerpPayrollCalc.class);
		//new formulaEvaluationResult();
		int AMN_Payroll_Detail_ID=0;
		int AMN_Payroll_ID=0;
		String sql2="",sql3="";
        String Concept_Reference="";
        BigDecimal.valueOf(0);
        Integer Period_ID=0,Process_ID=0,Contract_ID=0;
        // Init Variables
		//VariablesInit();		
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		amnpayroll.getAMN_Employee_ID();
		Period_ID = amnpayroll.getAMN_Period_ID();
		Process_ID = amnpayroll.getAMN_Process_ID();
		amnpayroll.getAMN_Payroll_ID();
		amnpayroll.getAD_Client_ID();
    	// AMN_Period
    	MAMN_Period amnperiod = new MAMN_Period(p_ctx, Period_ID, null);
    	amnperiod.getAMNDateIni();
    	amnperiod.getAMNDateEnd(); 	
    	new MAMN_Process(p_ctx, Process_ID, null);
    	BigDecimal.valueOf(0);
    	BigDecimal.valueOf(0);
    	BigDecimal.valueOf(0);
    	//
    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_ID, null);
    	amncontract.getValue();
	    // Payroll Detail Array
    	sql2 = "SELECT " 
                +	"pad.qtyvalue, "
                +	"cty.calcorder as calcorder, "
                +	"pay.invdateini, "
                +	"pay.invdateend, " 
                +	"CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
                +	"pad.amountallocated, "
                +	"pad.amountdeducted, "
                +	"cty.value as creference, "
                +	"pay.amn_payroll_id, "
                +	"pad.amn_payroll_detail_id "
    			+	"FROM amn_payroll as pay "
                +	"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " 
                +	"LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) " 
                +	"LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) " 
                +	"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
                +	"WHERE pay.amn_payroll_id = ? "
                +	" AND cty.calcorder < 999999999 " 
                +	"ORDER BY cty.calcorder";
	    
    	PreparedStatement pstmtc = null;
	    ResultSet rsc = null;
	    try
	    {
    		pstmtc = DB.prepareStatement(sql2, null);
            pstmtc.setInt(1, p_AMN_Payroll_ID);

            rsc = pstmtc.executeQuery();	            
            // Reference Concepts Only
            while (rsc.next()) {
            	rsc.getDouble(1);
                rsc.getInt(2);
                rsc.getDate(3);
                rsc.getDate(4);
                rsc.getInt(5);
                rsc.getDouble(6);
                rsc.getDouble(7);
                Concept_Reference = rsc.getString(8);
                AMN_Payroll_ID=rsc.getInt(9);
                AMN_Payroll_Detail_ID=rsc.getInt(10);
               	// UPD_SALARYSB: Concept Tags SALARYSB or SALPREST or SALARIOSB
                // log.warning("Concept_Reference:"+Concept_Reference);
                 if (p_AMN_Payroll_ID==AMN_Payroll_ID &&
                		(Concept_Reference.equalsIgnoreCase("SALARYSB") || 
                		Concept_Reference.equalsIgnoreCase("SALPREST") || 
                		Concept_Reference.equalsIgnoreCase("SALARIOSB"))) {
                    sql3 = "UPDATE AMN_Payroll_Detail "
                    		+ " set qtyvalue="+SB3Mo +" "  
        					+ " where amn_payroll_id =" + p_AMN_Payroll_ID + " "
        					+ " and amn_payroll_detail_id =" + AMN_Payroll_Detail_ID + " " ;
                    DB.executeUpdateEx(sql3, null);
                }
               	// UPD_SALARY12SB: Cocept Tags SALARY12SB or SAL12PREST or SALARIO12P
                if (p_AMN_Payroll_ID== AMN_Payroll_ID && 
                		(Concept_Reference.equalsIgnoreCase("SALARY12SB") || 
                		Concept_Reference.equalsIgnoreCase("SAL12PREST") || 
                		Concept_Reference.equalsIgnoreCase("SALARIO12P"))) {
                    sql3 = "UPDATE AMN_Payroll_Detail "
                    		+ " set qtyvalue="+SB12Mo +" "  
        					+ " where amn_payroll_id =" + p_AMN_Payroll_ID + " "
        					+ " and amn_payroll_detail_id =" + AMN_Payroll_Detail_ID + " " ;
                    DB.executeUpdateEx(sql3, null);
                }
             
            }
	    }
	    catch (SQLException e)
	    {
	    }
	    finally
	    {
	            DB.close(rsc, pstmtc);
	            rsc = null; pstmtc = null;
	    }

	}
	// OJO
	/**
	 *  processDefaultValue: UPD_SALUTIL12SB
	 * 	Description: Return Salary Amount Base UPDATED to calc Utilities 
	 * 		(Utilidades) last 12 Months
	 *  	according to Labor Law 142 Lit B.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal UPD_SALUTIL12SB (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Updated_Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 12 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 11 Month Before
		//cal.add(Calendar.MONTH, -11);
		cal.add(Calendar.MONTH, -12);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_utilities_updated( ? , ? , ? , ? , ? ) / 12 ";
		PreparedStatement pstmt = null;
		//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Updated_Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Updated_Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		// log.warning("Updated_Historic_Salary:"+Updated_Historic_Salary);
		retValue = Updated_Historic_Salary.divide( BigDecimal.valueOf(30) ,2, RoundingMode.CEILING);
		return retValue;
	}

	/**
	 *  processDefaultValue: UPD_UTILIDDEV12MO
	 * 	Description: Return Payments Amount Base UPDATED to calc Utilities 
	 * 		(Utilidades) last 12 Months (All Payments)
	 *  	according to Labor Law 142 Lit B.
	 *  Parameters:
	 * 	Properties Ctx, int AMN_Payroll_ID, String trxName
	 */
	public static BigDecimal UPD_UTILIDDEV12MO (Properties Ctx, int AMN_Payroll_ID, String trxName) {

		BigDecimal retValue = BigDecimal.valueOf(0);	
		BigDecimal Updated_Historic_Salary = BigDecimal.valueOf(0);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Ctx, AMN_Payroll_ID, trxName);
		MAMN_Employee amnemployee = new MAMN_Employee(Ctx, amnpayroll.getAMN_Employee_ID(), trxName);
		// Calculates Average last 12 months
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(amnpayroll.getInvDateEnd());
		// SET HOUR 00:00:00 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		//EndDate
		Timestamp EndDate = new Timestamp(cal.getTimeInMillis());
		//StartDate	= First Day next 11 Month Before
		//cal.add(Calendar.MONTH, -11);
		cal.add(Calendar.MONTH, -12);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Timestamp StartDate = new Timestamp(cal.getTimeInMillis());
		// Get 'salary_base' from amp_salary_hist_calc function
    	String sql = "select amp_salary_utilities_updated( ? , ? , ? , ? , ?) ";
		PreparedStatement pstmt = null;
		//log.warning("Dates:"+StartDate+" - "+EndDate+"  sql:"+sql);
		ResultSet rspc = null;		
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, amnemployee.getAMN_Employee_ID());
            pstmt.setTimestamp(2, StartDate);
            pstmt.setTimestamp(3, EndDate);
            pstmt.setInt (4, amnpayroll.getC_Currency_ID());
            pstmt.setInt (5, amnpayroll.getC_ConversionType_ID());
            //log.warning("....SQL:"+sql+"  StartDate:"+StartDate+"  EndDate:"+EndDate);
            rspc = pstmt.executeQuery();
			if (rspc.next())
			{
				Updated_Historic_Salary = rspc.getBigDecimal(1);
			}				
		}
	    catch (SQLException e)
	    {
	    	Updated_Historic_Salary = BigDecimal.valueOf(0);
	    }
		finally
		{
			DB.close(rspc, pstmt);
			rspc = null; pstmt = null;
		}
		// Compare to Salary on Employee Table
		// log.warning("Updated_Historic_Salary:"+Updated_Historic_Salary);
		//retValue = Updated_Historic_Salary.divide( BigDecimal.valueOf(30) ,2, RoundingMode.CEILING);
		retValue = Updated_Historic_Salary;
		return retValue;
	}

	/*******************************************************************************************
   	// Payroll UPDATE UPD_SALUTILSB 
   	// UPD_SALUTILSB: Concept Tags SALARYSB or SALPREST or SALARIOSB
  	//
	// Parameters:
	// 	p_ctx
	// 	p_AMN_Payroll_ID
	//  Notes: this Method is Used by AMNPayrollRefresh Process Called from Payroll Preparing
	//			Window 
	//  Also Called by  PayrollDetail Callout on Concept_Types_Proc Value
	//  Also Called by Event AMN Payroll event
	//***************************************************************************************/
	
	public static void PayrollUpdateSALUTILSB (Properties p_ctx, int p_AMN_Payroll_ID ) throws ScriptException
	{
		CLogger.getCLogger(AmerpPayrollCalc.class);
		//new formulaEvaluationResult();
		int AMN_Payroll_Detail_ID=0;
		int AMN_Payroll_ID=0;
		String sql2="",sql3="";
        String Concept_Reference="";
        BigDecimal.valueOf(0);
        Integer Period_ID=0,Process_ID=0,Contract_ID=0;
        // Init Variables
		//VariablesInit();		
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		amnpayroll.getAMN_Employee_ID();
		Period_ID = amnpayroll.getAMN_Period_ID();
		Process_ID = amnpayroll.getAMN_Process_ID();
		amnpayroll.getAMN_Payroll_ID();
		amnpayroll.getAD_Client_ID();
    	// AMN_Period
    	MAMN_Period amnperiod = new MAMN_Period(p_ctx, Period_ID, null);
    	amnperiod.getAMNDateIni();
    	amnperiod.getAMNDateEnd(); 	
    	new MAMN_Process(p_ctx, Process_ID, null);
    	BigDecimal.valueOf(0);
    	BigDecimal.valueOf(0);
    	BigDecimal.valueOf(0);
    	//
    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_ID, null);
    	amncontract.getValue();
	    // Payroll Detail Array
    	sql2 = "SELECT " 
                +	"pad.qtyvalue, "
                +	"cty.calcorder as calcorder, "
                +	"pay.invdateini, "
                +	"pay.invdateend, " 
                +	"CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
                +	"pad.amountallocated, "
                +	"pad.amountdeducted, "
                +	"cty.value as creference, "
                +	"pay.amn_payroll_id, "
                +	"pad.amn_payroll_detail_id "
    			+	"FROM amn_payroll as pay "
                +	"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " 
                +	"LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) " 
                +	"LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) " 
                +	"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
                +	"WHERE pay.amn_payroll_id = ? "
                +	" AND cty.calcorder < 999999999 " 
                +	"ORDER BY cty.calcorder";
	    
    	PreparedStatement pstmtc = null;
	    ResultSet rsc = null;
	    try
	    {
    		pstmtc = DB.prepareStatement(sql2, null);
            pstmtc.setInt(1, p_AMN_Payroll_ID);

            rsc = pstmtc.executeQuery();	            
            // Reference Concepts Only
            while (rsc.next()) {
            	rsc.getDouble(1);
                rsc.getInt(2);
                rsc.getDate(3);
                rsc.getDate(4);
                rsc.getInt(5);
                rsc.getDouble(6);
                rsc.getDouble(7);
                Concept_Reference = rsc.getString(8);
                AMN_Payroll_ID=rsc.getInt(9);
                AMN_Payroll_Detail_ID=rsc.getInt(10);
               	// UPD_SALARYSB: Concept Tags SALARYSB or SALPREST or SALARIOSB
                // log.warning("Concept_Reference:"+Concept_Reference);
                 if (p_AMN_Payroll_ID==AMN_Payroll_ID &&
                		Concept_Reference.equalsIgnoreCase("SALUTIL") ) {
                    sql3 = "UPDATE AMN_Payroll_Detail "
                    		+ " set qtyvalue="+SBUtil12Mo +" "  
        					+ " where amn_payroll_id =" + p_AMN_Payroll_ID + " "
        					+ " and amn_payroll_detail_id =" + AMN_Payroll_Detail_ID + " " ;
                    DB.executeUpdateEx(sql3, null);
                }            
            }
	    }
	    catch (SQLException e)
	    {
	    }
	    finally
	    {
	            DB.close(rsc, pstmtc);
	            rsc = null; pstmtc = null;
	    }

	}
	// OJO
	
	/*******************************************************************************************
   	// Payroll UPDATE PayrollUpdateUTILIDDEV 
   	// PayrollUpdateUTILIDDEV: Concept Tags UTILIDDEV
  	//
	// Parameters:
	// 	p_ctx
	// 	p_AMN_Payroll_ID
	//  Notes: this Method is Used by AMNPayrollRefresh Process Called from Payroll Preparing
	//			Window 
	//  Also Called by  PayrollDetail Callout on Concept_Types_Proc Value
	//  Also Called by Event AMN Payroll event
	//***************************************************************************************/
	
	public static void PayrollUpdateUTILIDDEV (Properties p_ctx, int p_AMN_Payroll_ID ) throws ScriptException
	{
		CLogger.getCLogger(AmerpPayrollCalc.class);
		//new formulaEvaluationResult();
		int AMN_Payroll_Detail_ID=0;
		int AMN_Payroll_ID=0;
		String sql2="",sql3="";
        String Concept_Reference="";
        BigDecimal.valueOf(0);
        Integer Period_ID=0,Process_ID=0,Contract_ID=0;
        // Init Variables
		//VariablesInit();		
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		amnpayroll.getAMN_Employee_ID();
		Period_ID = amnpayroll.getAMN_Period_ID();
		Process_ID = amnpayroll.getAMN_Process_ID();
		amnpayroll.getAMN_Payroll_ID();
		amnpayroll.getAD_Client_ID();
    	// AMN_Period
    	MAMN_Period amnperiod = new MAMN_Period(p_ctx, Period_ID, null);
    	amnperiod.getAMNDateIni();
    	amnperiod.getAMNDateEnd(); 	
    	new MAMN_Process(p_ctx, Process_ID, null);
    	BigDecimal.valueOf(0);
    	BigDecimal.valueOf(0);
    	BigDecimal.valueOf(0);
    	//
    	MAMN_Contract amncontract = new MAMN_Contract(p_ctx, Contract_ID, null);
    	amncontract.getValue();
	    // Payroll Detail Array
    	sql2 = "SELECT " 
                +	"pad.qtyvalue, "
                +	"cty.calcorder as calcorder, "
                +	"pay.invdateini, "
                +	"pay.invdateend, " 
                +	"CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
                +	"pad.amountallocated, "
                +	"pad.amountdeducted, "
                +	"cty.value as creference, "
                +	"pay.amn_payroll_id, "
                +	"pad.amn_payroll_detail_id "
    			+	"FROM amn_payroll as pay "
                +	"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " 
                +	"LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) " 
                +	"LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) " 
                +	"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
                +	"WHERE pay.amn_payroll_id = ? "
                +	" AND cty.calcorder < 999999999 " 
                +	"ORDER BY cty.calcorder";
	    
    	PreparedStatement pstmtc = null;
	    ResultSet rsc = null;
	    try
	    {
    		pstmtc = DB.prepareStatement(sql2, null);
            pstmtc.setInt(1, p_AMN_Payroll_ID);

            rsc = pstmtc.executeQuery();	            
            // Reference Concepts Only
            while (rsc.next()) {
            	rsc.getDouble(1);
                rsc.getInt(2);
                rsc.getDate(3);
                rsc.getDate(4);
                rsc.getInt(5);
                rsc.getDouble(6);
                rsc.getDouble(7);
                Concept_Reference = rsc.getString(8);
                AMN_Payroll_ID=rsc.getInt(9);
                AMN_Payroll_Detail_ID=rsc.getInt(10);
               	// UPD_SALARYSB: Concept Tags SALARYSB or SALPREST or SALARIOSB
                // log.warning("Concept_Reference:"+Concept_Reference);
                 if (p_AMN_Payroll_ID==AMN_Payroll_ID &&
                		Concept_Reference.equalsIgnoreCase("UTILIDDEV") ) {
                    sql3 = "UPDATE AMN_Payroll_Detail "
                    		+ " set qtyvalue="+UTILIDDEV12Mo +" "  
        					+ " where amn_payroll_id =" + p_AMN_Payroll_ID + " "
        					+ " and amn_payroll_detail_id =" + AMN_Payroll_Detail_ID + " " ;
                    DB.executeUpdateEx(sql3, null);
                }            
            }
	    }
	    catch (SQLException e)
	    {
	    }
	    finally
	    {
	            DB.close(rsc, pstmtc);
	            rsc = null; pstmtc = null;
	    }

	}
	// OJO
}

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

package org.amerp.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import javax.script.ScriptException;

import org.amerp.amnmodel.MAMN_Employee;
import org.amerp.amnmodel.MAMN_Payroll;
import org.amerp.amnmodel.MAMN_Payroll_Detail;
import org.amerp.amnmodel.MAMN_Period;
import org.amerp.amnmodel.MAMN_Process;
import org.amerp.amnutilities.AmerpPayrollCalcArray;
import org.amerp.amnutilities.AttendanceHours;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/** AMNPayrollTransferPayrollAssist
 * Description: Generic Class AMNPayrollTransferPayrollAssist
 * Called from: AMNPayrollTransferPayrollAssistOneEmployee
 * 				AMNPayrollTransferPayrollAssistOnePeriod
 * @author luisamesty
 *
 */
public class AMNPayrollTransferPayrollAssist {
	
	static CLogger log = CLogger.getCLogger(AMNPayrollTransferPayrollAssist.class);
	private ProcessInfo			m_pi;
	
	/**
	 * UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod
	 * Attendance day calculated from p_RefDateIni and p_RefDateEnd
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Period_ID	Period to Transfer Attendance
	 * @param p_AMN_Employee_ID	Employee to Transfer Attendance
	 * @param p_RefDateIni	Init Attendance Day to Transfer
	 * @param p_RefDateEnd	End Attendance Day to Transfer
	 * @return locMsg_Value
	 */
	public static String UpdatePayrollDocumentsTransferFromPayrollAssistOneEmployeeOnePeriod (
		Properties ctx, int p_AMN_Contract_ID, int p_AMN_Period_ID, int p_AMN_Employee_ID , 
		Timestamp p_RefDateIni, Timestamp p_RefDateEnd, String trxName) {
		
		// AmerpPayrollCalcArray
		AmerpPayrollCalcArray amerpPayrollCalcArray = new AmerpPayrollCalcArray();
		// LOCAL VARS
		BigDecimal BDZero = BigDecimal.valueOf(0);
		BigDecimal Tr_HED, Tr_HEN, Tr_HND, Tr_HNN;
		BigDecimal Tr_BONOASIST,Tr_BONOTRANSP;
		String locMsg_Value="";	
		String locMsg_Value_Events="";
		String Concept_Value="";
	    int p_AD_Client_ID=0;
	    int p_AD_Org_ID=0;
	    int p_AMN_Process_ID=0;
	    int AMN_Payroll_ID=0;
	    int AMN_Concept_Types_Proc_ID=0;
	    // Get p_AD_Client_ID,p_AD_Org_ID from Employee
	    MAMN_Employee amnemployee = new MAMN_Employee(Env.getCtx(), p_AMN_Employee_ID, null);
	    p_AD_Org_ID=amnemployee.getAD_Org_ID();
	    p_AD_Client_ID=amnemployee.getAD_Client_ID();
	    // Get p_AMN_Process_ID from AMN_Process
	    p_AMN_Process_ID = MAMN_Process.sqlGetAMNProcessIDFromName("NN", p_AD_Client_ID);
	    // log.warning("AMN_Process_Value=NN AMN_Process_ID="+p_AMN_Process_ID)	;    
		// AMN_Payroll
		MAMN_Payroll amnpayroll =  MAMN_Payroll.findByAMNPayroll(Env.getCtx(), null,
				0, p_AMN_Contract_ID,  p_AMN_Period_ID, p_AMN_Employee_ID);
		// Attendance Hours Array
		AttendanceHours atthours =  new AttendanceHours();
		// GET HOURS PROCESSED (HND,HNN,HED,HEN)
		//atthours = getPayrollAsisstProcValues(p_AMN_Period_ID, p_AMN_Employee_ID);
		atthours = getPayrollAsisstProcValuesBetweenDates( p_AMN_Employee_ID, p_RefDateIni, p_RefDateEnd );
		Tr_HED = atthours.getHR_HED();
		Tr_HEN = atthours.getHR_HEN();
		Tr_HND = atthours.getHR_HND();
		Tr_HNN = atthours.getHR_HNN();
		Tr_BONOASIST = atthours.getDAY_ATTB();
		Tr_BONOTRANSP = atthours.getDAY_ATT();
		//log.warning("1... Tr_HND:"+Tr_HND+"  Tr_BONOASIST"+Tr_BONOASIST+"   Tr_BONOTRANSP"+Tr_BONOTRANSP);
		// GET AMN_Payroll_ID FROM DOCUMENT CREATED
		amnpayroll = MAMN_Payroll.findByAMNPayroll(Env.getCtx(), Env.getLanguage(Env.getCtx()).getLocale(), 
				p_AMN_Process_ID, p_AMN_Contract_ID, p_AMN_Period_ID, p_AMN_Employee_ID);
		AMN_Payroll_ID = amnpayroll.getAMN_Payroll_ID();
		// MAMN_Payroll_Detail
		MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(Env.getCtx(), 0, null);
		// SELECT Concept Types (HND,HNN,HED,HEN) from AMN_Payroll_Detail
		// "WHERE cty.rule = 'F' AND ctp.amn_process_id = ? " + 
	    String sql = "SELECT  " + 
				"cty.calcorder,  " + 
				"cty.value, " + 
				"cty.amn_concept_types_id, " + 
				"ctp.amn_process_id, " + 
				"ctp.amn_concept_types_proc_id,  " + 
				"cty.defaultvalue " + 
				"FROM amn_concept_types as cty " + 
				"LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id = cty.amn_concept_types_id) " + 
				"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) " + 
				"LEFT JOIN amn_concept_types_contract as ctc ON (ctc.amn_concept_types_id = cty.amn_concept_types_id) " + 
				"WHERE ctp.amn_process_id = ? " + 
				"AND ctc.amn_contract_id = ? " + 
				"AND cty.value in ('HND','HNN','HED','HEN','BONOASIST','BONOTRANSP') " + 
				"ORDER BY cty.calcorder " 
				;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Process_ID);
            pstmt1.setInt (2, p_AMN_Contract_ID);
    		//locMsg_Value=locMsg_Value+sql+" \n";
			rs = pstmt1.executeQuery();
			while (rs.next())
			{
				Concept_Value 	 = rs.getString(2).trim(); 
				AMN_Concept_Types_Proc_ID = rs.getInt(5);
	            locMsg_Value_Events = locMsg_Value_Events + 
	            		"  AMN_Payroll_ID:"+AMN_Payroll_ID+
	            		"  AMN_Concept_Types_Proc_ID:"+AMN_Concept_Types_Proc_ID+
	            		"  Concept_Value:"+Concept_Value ;
	            // Tr_HED, Tr_HEN, Tr_HND, Tr_HNN
	            //log.warning("1... Concept_Value:"+Concept_Value+"   Tr_HND"+Tr_HND+"  Tr_BONOASIST"+Tr_BONOASIST+"   Tr_BONOTRANSP"+Tr_BONOTRANSP);
	            if (Concept_Value.equalsIgnoreCase("HND")  ) {
	            	Tr_HND=atthours.getHR_HND();
	            	// Verify if Not Zero
	            	if (Tr_HND.compareTo(BDZero) == 1) {
	            		//log.warning("HND:"+Concept_Value+"  Tr_HND:"+Tr_HND);
	            		//  CREATE MAMN_Payroll Detail
	    				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
	    						p_AD_Client_ID, p_AD_Org_ID, p_AMN_Process_ID, p_AMN_Contract_ID,
	    						AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, Tr_HND, trxName);
	            	}
	            }
	            if (Concept_Value.equalsIgnoreCase("HED")  ) {
	            	Tr_HED=atthours.getHR_HED();
	            	// Verify if Not Zero
	            	if (Tr_HED.compareTo(BDZero) == 1) {
	            		//log.warning("HED:"+Concept_Value+"  Tr_HED:"+Tr_HED);
	            		//  CREATE MAMN_Payroll Detail
	    				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
	    						p_AD_Client_ID, p_AD_Org_ID, p_AMN_Process_ID, p_AMN_Contract_ID,
	    						AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, Tr_HED, trxName);
	            	}
	            }
	            if (Concept_Value.equalsIgnoreCase("HNN")  ) {
	            	Tr_HNN=atthours.getHR_HNN();
	            	// Verify if Not Zero
	            	if (Tr_HNN.compareTo(BDZero) == 1) {
	            		//  CREATE MAMN_Payroll Detail
	    				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
	    						p_AD_Client_ID, p_AD_Org_ID, p_AMN_Process_ID, p_AMN_Contract_ID,
	    						AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, Tr_HNN, trxName);
	            	}
	            }
	            if (Concept_Value.equalsIgnoreCase("HEN")  ) {
	            	Tr_HEN=atthours.getHR_HEN();
	            	// Verify if Not Zero
	            	if (Tr_HEN.compareTo(BDZero) == 1) {
	            		//  CREATE MAMN_Payroll Detail
	    				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
	    						p_AD_Client_ID, p_AD_Org_ID, p_AMN_Process_ID, p_AMN_Contract_ID,
	    						AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, Tr_HEN, trxName);
	            	}
	            }
	            // BONOASIST, BONOTRANSP
	            if (Concept_Value.equalsIgnoreCase("BONOASIST")  ) {
	            	Tr_BONOASIST=atthours.getDAY_ATTB();
	            	// Verify if Not Zero
	            	if (Tr_BONOASIST.compareTo(BDZero) == 1) {
	            		//  CREATE MAMN_Payroll Detail
	    				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
	    						p_AD_Client_ID, p_AD_Org_ID, p_AMN_Process_ID, p_AMN_Contract_ID,
	    						AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, Tr_BONOASIST, trxName);
	            	}
	            }
	            if (Concept_Value.equalsIgnoreCase("BONOTRANSP")  ) {
	            	Tr_BONOTRANSP=atthours.getDAY_ATT();
	            	// Verify if Not Zero
	            	if (Tr_BONOTRANSP.compareTo(BDZero) == 1) {
	            		//  CREATE MAMN_Payroll Detail
	    				amnpayrolldetail.updateAmnPayrollDetail(ctx, Env.getLanguage(Env.getCtx()).getLocale(),
	    						p_AD_Client_ID, p_AD_Org_ID, p_AMN_Process_ID, p_AMN_Contract_ID,
	    						AMN_Payroll_ID, AMN_Concept_Types_Proc_ID, Tr_BONOTRANSP, trxName);
	            	}
	            }
	    		if (!amnpayroll.getDocStatus().equalsIgnoreCase(MAMN_Payroll.STATUS_Completed))
	    		{	
	    	    	amerpPayrollCalcArray.PayrollEvaluationArrayCalculate(Env.getCtx(), AMN_Payroll_ID);
	    		} else {
	    			locMsg_Value = locMsg_Value +" ** ALREADY PROCESSED - CAN'T BE RECALCULATED ** "+
	    					Msg.getElement(Env.getCtx(),"AMN_Payroll_ID")+amnpayroll.getName()+":"+" \r\n";
	    		}

			}	
		}
	    catch (SQLException e)
	    {
	    } 
		catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DB.close(rs, pstmt1);
			rs = null; pstmt1 = null;
		}
		locMsg_Value = locMsg_Value + locMsg_Value_Events;
		return locMsg_Value;
	}

	/**
	 * getPayrollAsisstProcValues
	 * Description: Get Resumes values of  
	 *   (HND,HNN,HED,HEN,Attendance,AttendanceBonus) from One AMN_Period and One employee
	 *   Start Date and end Date is taken from AMN_Period_ID
	 * @param p_AMN_Period_ID
	 * @param p_AMN_Employee_ID
	 * @return AttendanceHours Array (HND,HNN,HED,HEN)
	 */
	public static AttendanceHours getPayrollAsisstProcValues (
		int p_AMN_Period_ID, int p_AMN_Employee_ID )
	{
		
		BigDecimal BDZero = BigDecimal.valueOf(0);
	    Timestamp p_AMNDateIni;
	    Timestamp p_AMNDateEnd;
	    AttendanceHours pr_atthours =  new AttendanceHours();
		pr_atthours.setHR_HED(BDZero);
		pr_atthours.setHR_HEN(BDZero);
		pr_atthours.setHR_HND(BDZero);
		pr_atthours.setHR_HNN(BDZero);
		pr_atthours.setDAY_ATT(BDZero);
		pr_atthours.setDAY_ATTB(BDZero);
		pr_atthours.setHR_Message(" ");
		BigDecimal Tr_HED = BDZero;
		BigDecimal Tr_HEN = BDZero;
		BigDecimal Tr_HND = BDZero;
		BigDecimal Tr_HNN = BDZero;
		BigDecimal Tr_BONOASIST = BDZero;
		BigDecimal Tr_BONOTRANSP = BDZero;
		// Determines AMN_Period - AMN_DateIni and AMNDateEnd
		MAMN_Period amnperiod = new MAMN_Period(Env.getCtx(), p_AMN_Period_ID, null);
		p_AMNDateIni = amnperiod.getAMNDateIni();
		p_AMNDateEnd = amnperiod.getAMNDateEnd();
		// SELECT AMN_Payroll_Assist_Proc (Processed Attendance Hours)
	    String sql = "SELECT "+
	    		"pap.amn_payroll_assist_proc_id, " +
	    		"pap.amn_employee_id, "+
	    		"pap.event_date, " +
	    		"pap.Shift_HND,pap.Shift_HNN,pap.Shift_HED,pap.Shift_HEN,  "+
	    		"pap.Shift_Attendance,pap.shift_AttendanceBonus  "+
	    		"FROM  "+
	    		"amn_payroll_assist_proc as pap  "+
	    		"WHERE "+
	    		"pap.amn_employee_id=? " +
	    		"AND pap.event_date  BETWEEN (?) AND (?) "
	    		;
	    PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Employee_ID);
            pstmt1.setTimestamp(2, p_AMNDateIni);
            pstmt1.setTimestamp(3, p_AMNDateEnd);
    		//locMsg_Value=locMsg_Value+sql+" \n";
			rs = pstmt1.executeQuery();
			while (rs.next())
			{
				//locMsg_Value=locMsg_Value+"  Event_Date:"+Event_Date.toString();
				Tr_HND = Tr_HND.add(rs.getBigDecimal(4));
				//locMsg_Value=locMsg_Value+"  HND:"+Tr_HND.toString();
				Tr_HNN = Tr_HNN.add(rs.getBigDecimal(5));
				//locMsg_Value=locMsg_Value+"  HNN:"+Tr_HNN.toString();
				Tr_HED = Tr_HED.add(rs.getBigDecimal(6));
				//locMsg_Value=locMsg_Value+"  HED:"+Tr_HED.toString();
				Tr_HEN = Tr_HEN.add(rs.getBigDecimal(7));
				//locMsg_Value=locMsg_Value+"  HEN:"+Tr_HEN.toString();
				Tr_BONOTRANSP = Tr_BONOTRANSP.add(rs.getBigDecimal(8));
				//
				Tr_BONOASIST = Tr_BONOASIST.add(rs.getBigDecimal(9));
				//

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
		//log.warning(locMsg_Value);
		pr_atthours.setHR_HED(Tr_HED);
		pr_atthours.setHR_HEN(Tr_HEN);
		pr_atthours.setHR_HND(Tr_HND);
		pr_atthours.setHR_HNN(Tr_HNN);
		pr_atthours.setDAY_ATT(Tr_BONOTRANSP);
		pr_atthours.setDAY_ATTB(Tr_BONOASIST);
		pr_atthours.setHR_Message("** UPDATED **");
		//log.warning("2... Tr_HND:"+Tr_HND+"  Tr_BONOASIST"+Tr_BONOASIST+"   Tr_BONOTRANSP"+Tr_BONOTRANSP);
		return pr_atthours;
	}

	/**
	 * getPayrollAsisstProcValuesBetwenDates
	 * Description: Get Resumes values of  
	 *   (HND,HNN,HED,HEN,Attendance,AttendanceBonus) from One AMN_Period and One employee
	 *   Start Date and end Date is taken from AMN_Period_ID
	 * @param p_AMNDateIni
	 * @param p_AMNDateEnd
	 * @param p_AMN_Employee_ID
	 * @return AttendanceHours Array (HND,HNN,HED,HEN)
	 */
	public static AttendanceHours getPayrollAsisstProcValuesBetweenDates (
		 int p_AMN_Employee_ID, Timestamp p_AMNDateIni, Timestamp p_AMNDateEnd )
	{
		
		BigDecimal BDZero = BigDecimal.valueOf(0);
	    AttendanceHours pr_atthours = new AttendanceHours();
		pr_atthours.setHR_HED(BDZero);
		pr_atthours.setHR_HEN(BDZero);
		pr_atthours.setHR_HND(BDZero);
		pr_atthours.setHR_HNN(BDZero);
		pr_atthours.setDAY_ATT(BDZero);
		pr_atthours.setDAY_ATTB(BDZero);
		pr_atthours.setHR_Message(" ");
		BigDecimal Tr_HED = BDZero;
		BigDecimal Tr_HEN = BDZero;
		BigDecimal Tr_HND = BDZero;
		BigDecimal Tr_HNN = BDZero;
		BigDecimal Tr_BONOASIST = BDZero;
		BigDecimal Tr_BONOTRANSP = BDZero;
		// SELECT AMN_Payroll_Assist_Proc (Processed Attendance Hours)
	    String sql = "SELECT "+
	    		"pap.amn_payroll_assist_proc_id, " +
	    		"pap.amn_employee_id, "+
	    		"pap.event_date, " +
	    		"pap.Shift_HND,pap.Shift_HNN,pap.Shift_HED,pap.Shift_HEN,  "+
	    		"pap.Shift_Attendance,pap.shift_AttendanceBonus  "+
	    		"FROM  "+
	    		"amn_payroll_assist_proc as pap  "+
	    		"WHERE "+
	    		"pap.amn_employee_id=? " +
	    		"AND pap.event_date  BETWEEN (?) AND (?) "
	    		;
	    PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		try
		{
			pstmt1 = DB.prepareStatement(sql, null);
            pstmt1.setInt (1, p_AMN_Employee_ID);
            pstmt1.setTimestamp(2, p_AMNDateIni);
            pstmt1.setTimestamp(3, p_AMNDateEnd);
    		//locMsg_Value=locMsg_Value+sql+" \n";
			rs = pstmt1.executeQuery();
			while (rs.next())
			{
				//locMsg_Value=locMsg_Value+"  Event_Date:"+Event_Date.toString();
				Tr_HND = Tr_HND.add(rs.getBigDecimal(4));
				//locMsg_Value=locMsg_Value+"  HND:"+Tr_HND.toString();
				Tr_HNN = Tr_HNN.add(rs.getBigDecimal(5));
				//locMsg_Value=locMsg_Value+"  HNN:"+Tr_HNN.toString();
				Tr_HED = Tr_HED.add(rs.getBigDecimal(6));
				//locMsg_Value=locMsg_Value+"  HED:"+Tr_HED.toString();
				Tr_HEN = Tr_HEN.add(rs.getBigDecimal(7));
				//locMsg_Value=locMsg_Value+"  HEN:"+Tr_HEN.toString();
				Tr_BONOTRANSP = Tr_BONOTRANSP.add(rs.getBigDecimal(8));
				//
				Tr_BONOASIST = Tr_BONOASIST.add(rs.getBigDecimal(9));
				//

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
		//log.warning(locMsg_Value);
		pr_atthours.setHR_HED(Tr_HED);
		pr_atthours.setHR_HEN(Tr_HEN);
		pr_atthours.setHR_HND(Tr_HND);
		pr_atthours.setHR_HNN(Tr_HNN);
		pr_atthours.setDAY_ATT(Tr_BONOTRANSP);
		pr_atthours.setDAY_ATTB(Tr_BONOASIST);
		pr_atthours.setHR_Message("** UPDATED **");
		//log.warning("2... Tr_HND:"+Tr_HND+"  Tr_BONOASIST"+Tr_BONOASIST+"   Tr_BONOTRANSP"+Tr_BONOTRANSP);
		return pr_atthours;
	}

}

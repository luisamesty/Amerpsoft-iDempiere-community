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
import java.sql.*;
import java.util.*;
import java.util.logging.Level;

import javax.script.*;

import org.adempiere.util.IProcessUI;
import org.amerp.amnutilities.*;
import org.amerp.amnutilities.AmerpPayrollCalc.scriptResult;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MSysConfig;
import org.compiere.util.*;

public class MAMN_Payroll_Detail extends X_AMN_Payroll_Detail {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2742706950161270469L;

	/**	Cache							*/
	private static CCache<Integer,MAMN_Payroll_Detail> s_cache = new CCache<Integer,MAMN_Payroll_Detail>(Table_Name, 10);
	
	static CLogger log = CLogger.getCLogger(MAMN_Payroll_Detail.class);
	
	public MAMN_Payroll_Detail(Properties ctx, int AMN_Payroll_Detail_ID,
			String trxName) {
		super(ctx, AMN_Payroll_Detail_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAMN_Payroll_Detail(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	/**************************************************************************
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if it can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
//log.warning("Before Save (AMN_Payroll_Detail) AMN_Payroll_ID:"+this.getAMN_Payroll_ID()+"AMN_Payroll_Detail_ID:"+this.getAMN_Payroll_Detail_ID()+"  p_newRecord:"+newRecord);
		//	Get Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 FROM AMN_Payroll_Detail WHERE AMN_Payroll_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getAMN_Payroll_ID());
			setLine (ii);
		}
		return true;
	} //	beforeSave
	
	/* (non-Javadoc)
	 * @see org.compiere.model.PO#afterSave(boolean, boolean)
	 */
	@Override
	protected boolean afterSave(boolean p_newRecord, boolean p_success) {
	    // TODO Auto-generated method stub
		
//log.warning("..............AMNPayrollevent.AMN_PAyroll_Detail.......................");
//log.warning("After Save (AMN_Payroll_Detail) AMN_Payroll_ID:"+this.getAMN_Payroll_ID()+"AMN_Payroll_Detail_ID:"+this.getAMN_Payroll_Detail_ID()+"  p_newRecord:"+p_newRecord);
//		try {
//			AmerpPayrollCalc.PayrollEvaluationArrayCalculate(getCtx(), this.getAMN_Payroll_ID());
//		}
//		catch (ScriptException ex) {
//			// TODO Auto-generated catch block
//			ex.printStackTrace();
//		}
//		//return p_success;
		return super.afterSave(p_newRecord, p_success);

	}
	
	/**	
	 * findByAMNPayrollDetail
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @return MAMN_PayrollDetail
	 */
	public static MAMN_Payroll_Detail findAMNPayrollDetailbyAMNPayroll(Properties ctx, Locale locale, 
				int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID) {
				
		MAMN_Payroll_Detail retValue = null;
		String sql = "SELECT * " + 
				"FROM amn_payroll as pay " + 
				"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " + 
				"WHERE pad.amn_payroll_id=? " + 
				"AND pad.amn_concept_types_proc_id=?"
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Payroll_ID);
            pstmt.setInt (2, p_AMN_Concept_Types_Proc_ID);

			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(ctx, rs, null);
				Integer key = new Integer(amnpayrolldetail.getAMN_Payroll_Detail_ID());
				s_cache.put (key, amnpayrolldetail);
				if (amnpayrolldetail.isActive())
					retValue = amnpayrolldetail;
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
	 * findAMNPayrollDetailbyAMNPayrollforDeferred
	 * @param ctx
	 * @param locale
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @param p_DeferredAMN_Payroll_ID
	 * @return MAMN_PayrollDetail
	 */
	public static MAMN_Payroll_Detail findAMNPayrollDetailbyAMNPayrollforDeferred(Properties ctx, Locale locale, 
				int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID,
				int p_DeferredAMN_Payroll_ID) {
				
		MAMN_Payroll_Detail retValue = null;
		String sql = "SELECT * " + 
				"FROM amn_payroll as pay " + 
				"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " + 
				"WHERE pad.amn_payroll_id=? " + 
				"AND pad.amn_concept_types_proc_id=? " +
				"AND pad.amn_payroll_detail_parent_id=? "
			;        
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt (1, p_AMN_Payroll_ID);
            pstmt.setInt (2, p_AMN_Concept_Types_Proc_ID);
            pstmt.setInt (3, p_DeferredAMN_Payroll_ID);

			rs = pstmt.executeQuery();
			while (rs.next())
			{
				MAMN_Payroll_Detail amnpayrolldetail = new MAMN_Payroll_Detail(ctx, rs, null);
				Integer key = new Integer(amnpayrolldetail.getAMN_Payroll_Detail_ID());
				s_cache.put (key, amnpayrolldetail);
				if (amnpayrolldetail.isActive())
					retValue = amnpayrolldetail;
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
	 * createAmnPayrollDetail
	 * @param ctx
	 * @param locale
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_AMN_Process_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @return boolean
	 */
	public boolean createAmnPayrollDetail(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,  int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID, String trxName) {
		
		int Concept_CalcOrder=0;
		String Concept_Value = "" ;
		String Concept_Name = "Nombre del Concepto" ;
		String Concept_Description = "Descripcion del Concepto" ;
		int AMN_Concept_Uom_ID= 0;
		BigDecimal Concept_DefaultValue=BigDecimal.valueOf(0.00);
		scriptResult RetVal = new scriptResult();
		String Concept_DefaultValueST="";
		String Concept_ScriptDefaultValueST="";
		// Rules only on PL
		boolean forceRulesInit=false;
//		if (amnprocess.getAMN_Process_Value().compareToIgnoreCase("PL")== 0)
//			forceRulesInit=true;
		boolean forceDVInit=true;
       	// MSysConfig AMERP_Payroll_Rules_Apply 
		String apra = MSysConfig.getValue("AMERP_Payroll_Rules_Apply","N",p_AD_Client_ID);
//log.warning("AMERP_Payroll_Rules_Apply = "+apra);
		if (apra.compareToIgnoreCase("Y")==0)
			forceRulesInit=true;
		else
			forceRulesInit=false;
		// GET Payrolldays from AMN_Contract (Valid Round Value if NOT Zero)
		MAMN_Contract amncontract = new MAMN_Contract(ctx,p_AMN_Contract_ID,null);
		//BigDecimal PayrolldaysC from AMN_Contract
		BigDecimal PayrolldaysC = amncontract.getPayRollDays();
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		// GET Employee_ID
    	int AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, AMN_Employee_ID, null);
		// GET Salary from AMN_Employee
		//BigDecimal Salary= MAMN_Employee.sqlGetAMNSalary(AMN_Employee_ID);
		BigDecimal Salary=amnemployee.getSalary();
		// GET Payrolldays from AMN_Payroll
		//log.warning(" MAMN_Payroll_Detail---p_AMN_Payroll_ID="+p_AMN_Payroll_ID+"  fechas INI="+amnpayroll.getInvDateIni()+"  End="+amnpayroll.getInvDateEnd());	
		// deprecated BigDecimal Payrolldays = MAMN_Payroll.sqlGetAMNPayrollDays(p_AMN_Payroll_ID);    
		BigDecimal Payrolldays = amnpayroll.getAMNPayrollDays(p_AMN_Payroll_ID ); 
		if (PayrolldaysC.equals(Payrolldays) ) {
			Payrolldays = PayrolldaysC;
		}		
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
		// *****************************************************
    	// * Get Concept's Attributes fromConcept_Types_Proc_ID
    	// ******************************************************
		MAMN_Concept_Types_Proc amnctp = new MAMN_Concept_Types_Proc(ctx, p_AMN_Concept_Types_Proc_ID,null);
		MAMN_Concept_Types amncty  = new MAMN_Concept_Types(ctx, amnctp.getAMN_Concept_Types_ID(),null);
        Concept_Value= amncty.getValue();
        Concept_Name = amncty.getName();
        Concept_Description = amncty.getDescription();
        Concept_CalcOrder =amncty.getCalcOrder();
        Concept_DefaultValueST=amncty.getDefaultValue().trim();
        if (!Util.isEmpty(amncty.getScriptDefaultValue(), true))
        	Concept_ScriptDefaultValueST=amncty.getScriptDefaultValue();
        else 
        	Concept_ScriptDefaultValueST="";
       	//Concept_ScriptDefaultValueST=amncty.getScriptDefaultValue();
        AMN_Concept_Uom_ID = amncty.getAMN_Concept_Uom_ID();
	    //
		if (Concept_Description==null)
			Concept_Description="*** Description Empty ***";
		// QTY DEFAULT VALUE
		Concept_DefaultValue = BigDecimal.valueOf(1.00);;
		// CALCULATE DEFAULT VALUE
		try {
			AmerpPayrollCalc.PayrollEvaluation(p_ctx, p_AMN_Payroll_ID, Concept_CalcOrder, forceRulesInit, forceDVInit);
			// Evauate Concept_ScriptDefaultValueST if Empty
			if (Concept_ScriptDefaultValueST==null || Concept_ScriptDefaultValueST.isEmpty()) {
				RetVal=AmerpPayrollCalc.FormulaEvaluationScript(
						p_AMN_Payroll_ID, Concept_Value, Concept_DefaultValueST, Concept_DefaultValue, Salary, Payrolldays, "");
				Concept_DefaultValue = RetVal.getBDCalcAmnt();
			} else {
				RetVal=AmerpPayrollCalc.FormulaEvaluationScript(
						p_AMN_Payroll_ID, Concept_Value, Concept_ScriptDefaultValueST, Concept_DefaultValue, Salary, Payrolldays, "");
				Concept_DefaultValue = RetVal.getBDCalcAmnt();				
			}
		}
        catch (ScriptException ex) {
            // TODO Auto-generated catch block
        	Concept_DefaultValue = BigDecimal.valueOf(1.00);
            //ex.printStackTrace();
            log.log(Level.WARNING, "** ERROR ON ** FormulaEvaluationScript" + Concept_Value , ex);
        }
		MAMN_Payroll_Detail amnpayrolldetail = MAMN_Payroll_Detail.findAMNPayrollDetailbyAMNPayroll(ctx, locale, 
				p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_ID);
		if (amnpayrolldetail == null) {
			//log.warning("................Values in MAMN_Payroll_Detail (NUEVO)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			//log.warning("ANTES DE EVALUAR Concept_DefaultValueST:"+Concept_DefaultValueST+"  Concept_DefaultValue:"+Concept_DefaultValue);
			// ***
			//log.warning("DESPUES DE EVALUAR Concept_DefaultValueST:"+Concept_DefaultValueST+"  Concept_DefaultValue:"+Concept_DefaultValue);
			amnpayrolldetail = new MAMN_Payroll_Detail(getCtx(), getAMN_Payroll_ID(), get_TrxName());
			amnpayrolldetail.setAD_Client_ID(p_AD_Client_ID);
			amnpayrolldetail.setAD_Org_ID(p_AD_Org_ID);
			amnpayrolldetail.setAMN_Payroll_ID(p_AMN_Payroll_ID);
			amnpayrolldetail.setAMN_Concept_Types_Proc_ID(p_AMN_Concept_Types_Proc_ID);
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(Concept_DefaultValue);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			// p_mTab.setValue("Script",Concept_Script );			
			// SAVES NEW
			amnpayrolldetail.save(get_TrxName());

		} else {
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(Concept_DefaultValue);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			amnpayrolldetail.save(get_TrxName());
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Receipt Lines").replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					String.format("%-50s",amnemployee.getValue()+"_"+amnemployee.getName().trim()).replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Concept_Types_ID")+": "+
					String.format("%-50s",amnpayrolldetail.getValue()+"-"+amnpayrolldetail.getName()).replace(' ', '_'));
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	createAmnPayrollDetail

	
	/**
	 *  createAmnPayrollDetail
	 * @param ctx
	 * @param locale
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_AMN_Process_ID
	 * @param p_AMN_Contract_ID
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @param Concept_Value
	 * @param Concept_CalcOrder
	 * @param Concept_Name
	 * @param Concept_Description
	 * @param AMN_Concept_Uom_ID
	 * @param QtyValue
	 * @param trxName
	 * @return
	 */

	public boolean createAmnPayrollDetail(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,  int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID, 
			String Concept_Value, int Concept_CalcOrder, 
			String Concept_Name, String Concept_Description, int AMN_Concept_Uom_ID,
			BigDecimal QtyValue, String trxName) {
		
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		// GET Employee_ID
    	int AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, AMN_Employee_ID, null);
		// 		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		//Concept_Description = amnpayroll.getDescription();
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		MAMN_Payroll_Detail amnpayrolldetail = MAMN_Payroll_Detail.findAMNPayrollDetailbyAMNPayroll(ctx, locale, 
				p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_ID);
		if (amnpayrolldetail == null) {
			//log.warning("................Values in MAMN_Payroll_Detail (NUEVO)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			//log.warning("ANTES DE EVALUAR QtyValue:"+QtyValue);
			amnpayrolldetail = new MAMN_Payroll_Detail(getCtx(), getAMN_Payroll_ID(), get_TrxName());
			amnpayrolldetail.setAD_Client_ID(p_AD_Client_ID);
			amnpayrolldetail.setAD_Org_ID(p_AD_Org_ID);
			amnpayrolldetail.setAMN_Payroll_ID(p_AMN_Payroll_ID);
			amnpayrolldetail.setAMN_Concept_Types_Proc_ID(p_AMN_Concept_Types_Proc_ID);
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(QtyValue);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			// p_mTab.setValue("Script",Concept_Script );			
			// SAVES NEW
			amnpayrolldetail.save(get_TrxName());

		} else {
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(QtyValue);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			amnpayrolldetail.save(get_TrxName());
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(String.format("%-15s","Receipt Lines").replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Employee_ID")+": "+
					String.format("%-50s",amnemployee.getValue()+"_"+amnemployee.getName().trim()).replace(' ', '_')+
					Msg.getElement(Env.getCtx(), "AMN_Concept_Types_ID")+": "+
					String.format("%-50s",amnpayrolldetail.getValue()+"-"+amnpayrolldetail.getName()).replace(' ', '_'));
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	createAmnPayrollDetail
	/**
	 * createAmnPayrollDetailDeferred for Loan and Employee Deferred Paymens
	 * @param ctx
	 * @param locale
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_AMN_Process_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @param p_DeferredAMN_Payroll_ID (Original LOAN Payroll_ID)
	 * @return boolean
	 */
	public boolean createAmnPayrollDetailDeferred(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,  int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID, 
			BigDecimal p_AmountCalculated, int p_DeferredAMN_Payroll_ID, String trxName) {
		
		int Concept_CalcOrder=0;
		String Concept_Value = "" ;
		String Concept_Name = "Nombre del Concepto" ;
		String Concept_Description = "" ;
		int AMN_Concept_Uom_ID = 0;
		// GET Payrolldays from AMN_Contract (Valid Round Value if NOT Zero)
		MAMN_Contract amncontract = new MAMN_Contract(ctx,p_AMN_Contract_ID,null);
		//BigDecimal PayrolldaysC from AMN_Contract
		BigDecimal PayrolldaysC = amncontract.getPayRollDays();
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		Concept_Description = amnpayroll.getDescription();
		// GET Employee_ID
    	int AMN_Employee_ID = amnpayroll.getAMN_Employee_ID();
		MAMN_Employee amnemployee = new MAMN_Employee(p_ctx, AMN_Employee_ID, null);
		// GET Salary from AMN_Employee
		//BigDecimal Salary= MAMN_Employee.sqlGetAMNSalary(AMN_Employee_ID);
		BigDecimal Salary=amnemployee.getSalary();
		// GET Payrolldays from AMN_Payroll
		// deprecated BigDecimal Payrolldays = MAMN_Payroll.sqlGetAMNPayrollDays(p_AMN_Payroll_ID);    
		BigDecimal Payrolldays = amnpayroll.getAMNPayrollDays(p_AMN_Payroll_ID ); 
		if (PayrolldaysC.equals(Payrolldays) ) {
			Payrolldays = PayrolldaysC;
		}		
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
		// *****************************************************
    	// * Get Concept's Attributes fromConcept_Types_Proc_ID
    	// ******************************************************
		MAMN_Concept_Types_Proc amnctp = new MAMN_Concept_Types_Proc(ctx, p_AMN_Concept_Types_Proc_ID,null);
		MAMN_Concept_Types amncty  = new MAMN_Concept_Types(ctx, amnctp.getAMN_Concept_Types_ID(),null);
        Concept_Value= amncty.getValue();
        Concept_Name = amncty.getName();
        Concept_Description = amncty.getDescription();
        Concept_CalcOrder =amncty.getCalcOrder();
        AMN_Concept_Uom_ID = amncty.getAMN_Concept_Uom_ID();		
//		String sql = "" +
//	    		 "SELECT " + 
//	    			"cty.value, " + 
//	    			"coalesce(cty.name,'') as name, " + 
//	    			"coalesce(cty.description,cty.name,'') as description,  " + 
//	    			"cty.calcorder,  " + 
//	    			"cty.defaultvalue, " +
//	    			"cty.AMN_Concept_Uom_ID " +
//	    		"FROM amn_concept_types as cty " + 
//	    			"LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id = cty.amn_concept_types_id) " + 
//	    			"WHERE ctp.amn_concept_types_proc_id = ?";
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;
//	    try
//	    {
//	            pstmt = DB.prepareStatement(sql, null);
//	            pstmt.setInt(1, p_AMN_Concept_Types_Proc_ID);
//	            rs = pstmt.executeQuery();
//	            if (rs.next())
//	            {
//	                Concept_Value= rs.getString(1).trim();
//	                Concept_Name = rs.getString(2).trim();
//	                if (Concept_Description.isEmpty())
//	                	Concept_Description = rs.getString(3).trim();
//	                Concept_CalcOrder =rs.getInt(4);
//	                AMN_Concept_Uom_ID = rs.getInt(6);
//	            }
//	    }
//	    catch (SQLException e)
//	    {
//	        Concept_Name = "Error: Nombre del Concepto" ;
//	    }
//	    finally
//	    {
//	            DB.close(rs, pstmt);
//	            rs = null; pstmt = null;
//	    }
	    //
		if (Concept_Description==null)
			Concept_Description="*** Description Empty ***";
		// MAMN_Payroll_Detail
		MAMN_Payroll_Detail amnpayrolldetail = MAMN_Payroll_Detail.findAMNPayrollDetailbyAMNPayrollforDeferred(ctx, locale, 
				p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_ID, p_DeferredAMN_Payroll_ID);
		if (amnpayrolldetail == null) {
			//log.warning("................Values in MAMN_Payroll_Detail (NUEVO)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			//log.warning("ANTES DE EVALUAR Concept_DefaultValueST:"+Concept_DefaultValueST+"  Concept_DefaultValue:"+Concept_DefaultValue);
			// ***
			//log.warning("DESPUES DE EVALUAR Concept_DefaultValueST:"+Concept_DefaultValueST+"  Concept_DefaultValue:"+Concept_DefaultValue);
			amnpayrolldetail = new MAMN_Payroll_Detail(getCtx(), getAMN_Payroll_ID(), get_TrxName());
			amnpayrolldetail.setAD_Client_ID(p_AD_Client_ID);
			amnpayrolldetail.setAD_Org_ID(p_AD_Org_ID);
			amnpayrolldetail.setAMN_Payroll_ID(p_AMN_Payroll_ID);
			amnpayrolldetail.setAMN_Concept_Types_Proc_ID(p_AMN_Concept_Types_Proc_ID);
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(p_AmountCalculated);
			amnpayrolldetail.setAMN_Payroll_Detail_Parent_ID(p_DeferredAMN_Payroll_ID);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			// p_mTab.setValue("Script",Concept_Script );			
			// SAVES NEW
			amnpayrolldetail.save(get_TrxName());

		} else {
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(p_AmountCalculated);
			amnpayrolldetail.setAMN_Payroll_Detail_Parent_ID(p_DeferredAMN_Payroll_ID);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			amnpayrolldetail.save(get_TrxName());
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Payroll_Detail_ID")+": "+
					amnpayrolldetail.getValue()+"-"+amnpayrolldetail.getName());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	createAmnPayrollDetailDeferred

	/**
	 * updateAmnPayrollDetail
	 * Used by Update Attendance
	 * @param ctx
	 * @param locale
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_AMN_Process_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @param BigDecimal p_Default_Value
	 * @return boolean
	 */
	public boolean updateAmnPayrollDetail(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,  int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID, BigDecimal p_Default_Value, String trxName) {
		
		int Concept_CalcOrder=0;
		String Concept_Value = "" ;
		String Concept_Name = "Nombre del Concepto" ;
		String Concept_Description = "Descripcion del Concepto" ;
		int AMN_Concept_Uom_ID = 0;
		// GET Employee_ID
		// Deprecated - int Employee_ID = MAMN_Payroll.sqlGetAMNEmployeeID(p_AMN_Payroll_ID);
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(),p_AMN_Payroll_ID, null);
		int Employee_ID = amnpayroll.getAMN_Employee_ID();
		// GET Payrolldays from AMN_Payroll
		// deprecated BigDecimal Payrolldays = MAMN_Payroll.sqlGetAMNPayrollDays(p_AMN_Payroll_ID);    
		BigDecimal Payrolldays = amnpayroll.getAMNPayrollDays(p_AMN_Payroll_ID ); 
		// GET Salary from AMN_Employee
		BigDecimal Salary= MAMN_Employee.sqlGetAMNSalary(Employee_ID);
		// GET Payrolldays from AMN_Contract (Valid Round Value if NOT Zero)
		BigDecimal PayrolldaysC = MAMN_Contract.sqlGetAMNContractPayrollDays(p_AMN_Contract_ID);
		if (PayrolldaysC.equals(Payrolldays) ) {
			Payrolldays = PayrolldaysC;
		}		
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
		// *****************************************************
    	// * Get Concept's Attributes fromConcept_Types_Proc_ID
    	// ******************************************************
		MAMN_Concept_Types_Proc amnctp = new MAMN_Concept_Types_Proc(ctx, p_AMN_Concept_Types_Proc_ID,null);
		MAMN_Concept_Types amncty  = new MAMN_Concept_Types(ctx, amnctp.getAMN_Concept_Types_ID(),null);
        Concept_Value= amncty.getValue();
        Concept_Name = amncty.getName();
        Concept_Description = amncty.getDescription();
        Concept_CalcOrder =amncty.getCalcOrder();
        AMN_Concept_Uom_ID = amncty.getAMN_Concept_Uom_ID();		
//		String sql = "" +
//	    		 "SELECT " + 
//	    			"cty.value, " + 
//	    			"coalesce(cty.name,'') as name, " + 
//	    			"coalesce(cty.description,cty.name,'') as description,  " + 
//	    			"cty.calcorder,  " + 
//	    			"cty.defaultvalue, " +
//	    			"cty.AMN_Concept_Uom_ID " +
//	    		"FROM amn_concept_types as cty " + 
//	    			"LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id = cty.amn_concept_types_id) " + 
//	    			"WHERE ctp.amn_concept_types_proc_id = ?";
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;
//	    try
//	    {
//	            pstmt = DB.prepareStatement(sql, null);
//	            pstmt.setInt(1, p_AMN_Concept_Types_Proc_ID);
//	            rs = pstmt.executeQuery();
//	            if (rs.next())
//	            {
//	                Concept_Value= rs.getString(1).trim();
//	                Concept_Name = rs.getString(2).trim();
//	                Concept_Description = rs.getString(3).trim();
//	                Concept_CalcOrder =rs.getInt(4);
//	                AMN_Concept_Uom_ID = rs.getInt(6);
//	            }
//	    }
//	    catch (SQLException e)
//	    {
//	        Concept_Name = "Error: Nombre del Concepto" ;
//	    }
//	    finally
//	    {
//	            DB.close(rs, pstmt);
//	            rs = null; pstmt = null;
//	    }
	    //
		if (Concept_Description==null)
			Concept_Description="*** Description Empty ***";
	    MAMN_Payroll_Detail amnpayrolldetail = MAMN_Payroll_Detail.findAMNPayrollDetailbyAMNPayroll(ctx, locale, 
				p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_ID);
		if (amnpayrolldetail == null) {
			//log.warning("................Values in MAMN_Payroll_Detail (NUEVO)...................");
			//log.warning("p_AMN_Payroll_ID"+p_AMN_Payroll_ID+"  Concept_Value:"+Concept_Value + "-"+ Concept_Name);
			amnpayrolldetail = new MAMN_Payroll_Detail(getCtx(), getAMN_Payroll_ID(), get_TrxName());
			amnpayrolldetail.setAD_Client_ID(p_AD_Client_ID);
			amnpayrolldetail.setAD_Org_ID(p_AD_Org_ID);
			amnpayrolldetail.setAMN_Payroll_ID(p_AMN_Payroll_ID);
			amnpayrolldetail.setAMN_Concept_Types_Proc_ID(p_AMN_Concept_Types_Proc_ID);
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(p_Default_Value);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			// p_mTab.setValue("Script",Concept_Script );			
			// SAVES NEW
			amnpayrolldetail.saveEx(get_TrxName());

		} else {
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");
			amnpayrolldetail.setValue(Concept_Value);
			amnpayrolldetail.setCalcOrder(Concept_CalcOrder);
			amnpayrolldetail.setName(Concept_Name);
			amnpayrolldetail.setDescription(Concept_Description);
			amnpayrolldetail.setQtyValue(p_Default_Value);
			amnpayrolldetail.setAMN_Concept_Uom_ID(AMN_Concept_Uom_ID);
			amnpayrolldetail.saveEx(get_TrxName());
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "AMN_Payroll_Detail_ID")+": "+
					amnpayrolldetail.getValue()+"-"+amnpayrolldetail.getName());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	updateAmnPayrollDetail

	/**
	 * updateAmnPayrollDetailDescription
	 * Used by Update Attendance
	 * @param ctx
	 * @param locale
	 * @param p_AD_Client_ID
	 * @param p_AD_Org_ID
	 * @param p_AMN_Process_ID	Payroll Process
	 * @param p_AMN_Contract_ID	Payroll Contract
	 * @param p_AMN_Payroll_ID
	 * @param p_AMN_Concept_Types_Proc_ID
	 * @param Strin p_Description
	 * @return boolean
	 */
	public boolean updateAmnPayrollDetailDescription(Properties ctx, Locale locale, 
			int p_AD_Client_ID, int p_AD_Org_ID,  int p_AMN_Process_ID, int p_AMN_Contract_ID,
			int p_AMN_Payroll_ID,  int p_AMN_Concept_Types_Proc_ID, String p_Description, String trxName) {
		
		IProcessUI processMonitor = Env.getProcessUI(ctx);
		if (p_Description==null)
			p_Description="*** Detalle del Concepto ***";
	    MAMN_Payroll_Detail amnpayrolldetail = MAMN_Payroll_Detail.findAMNPayrollDetailbyAMNPayroll(ctx, locale, 
				p_AMN_Payroll_ID, p_AMN_Concept_Types_Proc_ID);
		if (amnpayrolldetail != null) {
			//log.warning("................Values in MAMN_Payroll (UPDATE)...................");

			amnpayrolldetail.setDescription(p_Description);
			amnpayrolldetail.saveEx(get_TrxName());
		}
		if (processMonitor != null)
		{
			//processMonitor.statusUpdate(Msg.getMsg(Env.getCtx(), "Payroll")+": "+amnemployee.getName());
			processMonitor.statusUpdate(Msg.getElement(Env.getCtx(), "p_AMN_Concept_Types_Proc_ID")+": "+
					amnpayrolldetail.getValue()+"-"+amnpayrolldetail.getDescription());
		}
		//amnpayroll.saveEx(get_TrxName());	//	Creates AMNPayroll Control

		return true;
		
	}	//	createAmnPayrollDetail

	/**
	 * getAMN_Deb_Acct
	 * @param p_AMN_Payroll_Detail	Payroll_Detail
	 * return C_ValidCombination_ID
	 * Luis Amesty Jun 2018: Modifyed for C_AcctSchema_ID and AMN_Concept_Types_Acct Table
	 */
	public static int getAMN_Deb_Acct (MAMN_Payroll_Detail p_AMN_Payroll_Detail, MAcctSchema as)
	
	{
//		String sql;
		int C_ValidCombination_ID = 0;
		int AMN_ConceptTypes_Proc_ID=0;
		AMN_ConceptTypes_Proc_ID=p_AMN_Payroll_Detail.getAMN_Concept_Types_Proc_ID();
		// AMN_Payroll_Detail_ID VALID COMBINATION DEB
//		sql = "SELECT " + 
//				"cvc.c_validcombination_id " + 
//				"FROM " + 
//				"amn_concept_types_proc as ctp " + 
//				"LEFT JOIN " + 
//				"amn_concept_types as cty ON (ctp.amn_concept_types_id = cty.amn_concept_types_id ) " + 
//				"LEFT JOIN " + 
//				"c_validcombination as cvc ON (cty.amn_deb_acct = cvc.c_validcombination_id) " + 
//				"WHERE " + 
//				"amn_concept_types_proc_id = ?"
//				;
//		C_ValidCombination_ID = DB.getSQLValue(null, sql, AMN_ConceptTypes_Proc_ID);	
		// NEW  Values Depending on WorkForce
		MAMN_Payroll amnpd = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_Detail.getAMN_Payroll_ID(),  null) ;
		MAMN_Employee amnemp = new MAMN_Employee(Env.getCtx(), amnpd.getAMN_Employee_ID(),  null);
		MAMN_Jobtitle amnjobtitle= new MAMN_Jobtitle(Env.getCtx(), amnemp.getAMN_Jobtitle_ID(),  null);
		MAMN_Concept_Types_Proc amnconcepttypesproc = new MAMN_Concept_Types_Proc(Env.getCtx(), p_AMN_Payroll_Detail.getAMN_Concept_Types_Proc_ID(),  null);
		MAMN_Concept_Types amnconcepttypes= new MAMN_Concept_Types(Env.getCtx(), amnconcepttypesproc.getAMN_Concept_Types_ID(),  null);
		MAMN_Concept_Types_Acct amnconcepttypesacct = MAMN_Concept_Types_Acct.findAMNConceptTypesAcct(Env.getCtx(), amnconcepttypesproc.getAMN_Concept_Types_ID(), as.getC_AcctSchema_ID());
		
		String EmployeeWorkforce = amnjobtitle.getWorkforce().trim();
		// Releation depemding on WorkForce
//log.warning(amnemp.getValue()+"  EmployeeWorkforce:"+EmployeeWorkforce+
//		" Concept:"+amnconcepttypesacct.getValue()+" ID:"+amnconcepttypesacct.getAMN_Concept_Types_ID()+
//		" AW:"+amnconcepttypesacct.getAMN_Deb_Acct()+
//		" DW:"+amnconcepttypesacct.getAMN_Deb_DW_Acct()+
//		" IW:"+amnconcepttypesacct.getAMN_Deb_IW_Acct()+
//		" SW:"+amnconcepttypesacct.getAMN_Deb_SW_Acct()+
//		" MW:"+amnconcepttypesacct.getAMN_Deb_MW_Acct());
		// Administration
		if (EmployeeWorkforce.compareToIgnoreCase("A") == 0) {
			C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_Acct();
//log.warning("A C_ValidCombination_ID:"+C_ValidCombination_ID);
			return C_ValidCombination_ID;	
		}
		// Direct Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("D") == 0 ) { //&& 
			if (amnconcepttypesacct.getAMN_Deb_DW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_DW_Acct(); 
//log.warning(" paso ...D");
//log.warning("C_ValidCombination_ID:"+C_ValidCombination_ID);
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_Acct();
			}
//log.warning("D C_ValidCombination_ID:"+C_ValidCombination_ID);
			return C_ValidCombination_ID;	
		}
		// Indirect Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("I") == 0 ) {
			if (amnconcepttypesacct.getAMN_Deb_IW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_IW_Acct();
//log.warning(" paso ...I");
//log.warning("C_ValidCombination_ID:"+C_ValidCombination_ID);
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_Acct();
			}
//log.warning("I C_ValidCombination_ID:"+C_ValidCombination_ID);
			return C_ValidCombination_ID;	
		}
		// Sales Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("S") == 0  ) { 
			if (amnconcepttypesacct.getAMN_Deb_SW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_SW_Acct();
//log.warning(" paso ...I");
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_Acct();
			}
//log.warning("C_ValidCombination_ID:"+C_ValidCombination_ID);
			return C_ValidCombination_ID;	
		}
		// Directors Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("M") == 0  ) { 
			if (amnconcepttypesacct.getAMN_Deb_MW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_MW_Acct();
//log.warning(" paso ...I");
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Deb_Acct();
			}
//log.warning("M C_ValidCombination_ID:"+C_ValidCombination_ID);
			return C_ValidCombination_ID;	
		}
//log.warning("C_ValidCombination_ID:"+C_ValidCombination_ID);
		return C_ValidCombination_ID;	
	}
	
	/**
	 * getAMN_CT_Acct
	 * @param p_AMN_Payroll_Detail	
	 * return C_ValidCombination_ID
	 * Luis Amesty Jun 2018: Modifyed for C_AcctSchema_ID and AMN_Concept_Types_Acct Table
	 */
	public static int getAMN_Cre_Acct (MAMN_Payroll_Detail p_AMN_Payroll_Detail, MAcctSchema as)
	
	{
//		String sql;
		int C_ValidCombination_ID = 0;
		// AMN_Payroll_Detail_ID VALID COMBINATION DEB
//		sql = "SELECT " + 
//				"cvc.c_validcombination_id " + 
//				"FROM " + 
//				"amn_concept_types_proc as ctp " + 
//				"LEFT JOIN " + 
//				"amn_concept_types as cty ON (ctp.amn_concept_types_id = cty.amn_concept_types_id ) " + 
//				"LEFT JOIN " + 
//				"c_validcombination as cvc ON (cty.amn_cre_acct = cvc.c_validcombination_id) " + 
//				"WHERE " + 
//				"amn_concept_types_proc_id = ?"
//				;
//		C_ValidCombination_ID = DB.getSQLValue(null, sql, AMN_ConceptTypes_Proc_ID);	
		// NEW  Values Depending on WorkForce
		// NEW  Values Depending on WorkForce
		MAMN_Payroll amnpd = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_Detail.getAMN_Payroll_ID(),  null) ;
		MAMN_Employee amnemp = new MAMN_Employee(Env.getCtx(), amnpd.getAMN_Employee_ID(),  null);
		MAMN_Jobtitle amnjobtitle= new MAMN_Jobtitle(Env.getCtx(), amnemp.getAMN_Jobtitle_ID(),  null);
		MAMN_Concept_Types_Proc amnconcepttypesproc = new MAMN_Concept_Types_Proc(Env.getCtx(), p_AMN_Payroll_Detail.getAMN_Concept_Types_Proc_ID(),  null);
		MAMN_Concept_Types amnconcepttypes= new MAMN_Concept_Types(Env.getCtx(), amnconcepttypesproc.getAMN_Concept_Types_ID(),  null);
		MAMN_Concept_Types_Acct amnconcepttypesacct = MAMN_Concept_Types_Acct.findAMNConceptTypesAcct(Env.getCtx(), amnconcepttypesproc.getAMN_Concept_Types_ID(), as.getC_AcctSchema_ID());
		
		String EmployeeWorkforce = amnjobtitle.getWorkforce().trim();
//log.warning(amnemp.getValue()+"  EmployeeWorkforce:"+EmployeeWorkforce+
//		" Concept:"+amnconcepttypes.getValue()+" ID:"+amnconcepttypesacct.getAMN_Concept_Types_ID()+
//				" AW:"+amnconcepttypesacct.getAMN_Cre_Acct()+
//				" DW:"+amnconcepttypesacct.getAMN_Cre_DW_Acct()+
//				" IW:"+amnconcepttypesacct.getAMN_Cre_IW_Acct()+
//				" SW:"+amnconcepttypesacct.getAMN_Cre_SW_Acct()+
//				" MW:"+amnconcepttypesacct.getAMN_Cre_MW_Acct());
		// Releation depemding on WorkForce
//log.setLevel(Level.WARNING);	
//log.warning("Employee: "+amnemp.getValue()+"  EmployeeWorkforce:"+amnjobtitle.getWorkforce()+"  Receipt:"+amnpd.getDocumentNo().trim()+"_"+amnpd.getDateAcct() );
		// Administration
		if (EmployeeWorkforce.compareToIgnoreCase("A") == 0 ) {
			C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_Acct();
		}
		// Direct Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("D") == 0 ) {
			if ( amnconcepttypesacct.getAMN_Cre_DW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_DW_Acct();
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_Acct();
			}
		}
		// Indirect Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("I") == 0) {
			if ( amnconcepttypesacct.getAMN_Cre_IW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_IW_Acct();
			} else { 
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_Acct();
			}
		}
		// Sales Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("S") == 0 ) {
			if ( amnconcepttypesacct.getAMN_Cre_SW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_SW_Acct();
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_Acct();
			}
		}
		// Directors Workforce
		if (EmployeeWorkforce.compareToIgnoreCase("M") == 0 ) {
			if ( amnconcepttypesacct.getAMN_Cre_MW_Acct() > 0) {
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_MW_Acct();
			} else {
				// Default Administration
				C_ValidCombination_ID = amnconcepttypesacct.getAMN_Cre_Acct();
			}
		}
		//log.warning("C_ValidCombination_ID:"+C_ValidCombination_ID);
		return C_ValidCombination_ID;	
	}
	
	
	/**
	 * getAMN_CT_Deb_Acct
	 * @param p_AMN_Payroll_Detail_ID	Payroll_ID
	 * return C_ElementValue_ID
	 */
	public static int getAMN_CT_Deb_Acct (MAMN_Payroll_Detail p_AMN_Payroll_Detail_ID)
	
	{
		String sql;
		int C_ElementValue_ID = 0;
		int AMN_ConceptTypes_Proc_ID=0;
		AMN_ConceptTypes_Proc_ID=p_AMN_Payroll_Detail_ID.getAMN_Concept_Types_Proc_ID();
		// AMN_Payroll_Detail_ID
		sql = "SELECT " + 
				"cev.c_elementvalue_id " + 
				"FROM " + 
				"amn_concept_types_proc as ctp " + 
				"LEFT JOIN " + 
				"amn_concept_types as cty ON (ctp.amn_concept_types_id = cty.amn_concept_types_id ) " + 
				"LEFT JOIN " + 
				"c_elementvalue as cev ON (cty.amn_ct_deb_acct_id = cev.c_elementvalue_id) " + 
				"WHERE " + 
				"amn_concept_types_proc_id = ?" ;
    	C_ElementValue_ID = DB.getSQLValue(null, sql, AMN_ConceptTypes_Proc_ID);	
		return C_ElementValue_ID;	
	}
	
	/**
	 * getAMN_CT_Cre_Acct
	 * @param p_AMN_Payroll_Detail_ID	Payroll_ID
	 * return C_ElementValue_ID
	 */
	public static int getAMN_CT_Cre_Acct (MAMN_Payroll_Detail p_AMN_Payroll_Detail_ID)
	
	{
		String sql;
		int C_ElementValue_ID = 0;
		int AMN_ConceptTypes_Proc_ID=0;
		AMN_ConceptTypes_Proc_ID=p_AMN_Payroll_Detail_ID.getAMN_Concept_Types_Proc_ID();
		// AMN_Payroll_Detail_ID
		sql = "SELECT " + 
				"cev.c_elementvalue_id " + 
				"FROM " + 
				"amn_concept_types_proc as ctp " + 
				"LEFT JOIN " + 
				"amn_concept_types as cty ON (ctp.amn_concept_types_id = cty.amn_concept_types_id ) " + 
				"LEFT JOIN " + 
				"c_elementvalue as cev ON (cty.amn_ct_cre_acct_id = cev.c_elementvalue_id) " + 
				"WHERE " + 
				"amn_concept_types_proc_id = ?" ;
    	C_ElementValue_ID = DB.getSQLValue(null, sql, AMN_ConceptTypes_Proc_ID);	
		return C_ElementValue_ID;		
	}

	@Override
	protected boolean beforeDelete() {
		// TODO Auto-generated method stub
//log.warning("Before Delete (AMN_Payroll_Detail) AMN_Payroll_ID:"+this.getAMN_Payroll_ID()+"AMN_Payroll_Detail_ID:"+this.getAMN_Payroll_Detail_ID());
//log.warning("Value:"+this.getValue()+"  Name:"+this.getName());
//log.warning("QtyValue:"+this.getQtyValue()+"  AmountCalculated:"+this.getAmountCalculated());

		return super.beforeDelete();
	}

	@Override
	protected boolean afterDelete(boolean success) {
		// TODO Auto-generated method stub
		
//log.warning("After Delete (AMN_Payroll_Detail) AMN_Payroll_ID:"+this.getAMN_Payroll_ID()+"AMN_Payroll_Detail_ID:"+this.getAMN_Payroll_Detail_ID());
//log.warning("Value:"+this.getValue()+"  Name:"+this.getName());
//log.warning("QtyValue:"+this.getQtyValue()+"  AmountCalculated:"+this.getAmountCalculated());

//		try {
//			AmerpPayrollCalc.PayrollEvaluationArrayCalculate(getCtx(), this.getAMN_Payroll_ID());
//		}
//		catch (ScriptException ex) {
//			// TODO Auto-generated catch block
//			ex.printStackTrace();
//		}
		return super.afterDelete(success);
	}
	
	
//	/*
//	 * Additinal Filed Variables
//	 */
//	static MAccount AMNPayrollDetailCombination;
//	
//	static int AMNPayrollDetailElementValue;
//
//	/**
//	 * @return the aMNPayrollDetailCombination
//	 */
//	public static MAccount getAMNPayrollDetailCombination() {
//		return AMNPayrollDetailCombination;
//	}
//
//	/**
//	 * @param p_aMNPayrollDetailCombination the aMNPayrollDetailCombination to set
//	 */
//	public static void setAMNPayrollDetailCombination(MAccount p_aMNPayrollDetailCombination) {
//		AMNPayrollDetailCombination = p_aMNPayrollDetailCombination;
//	}
//
//	/**
//	 * @return the aMNPayrollDetailElementValue
//	 */
//	public static int getAMNPayrollDetailElementValue() {
//		return AMNPayrollDetailElementValue;
//	}
//
//	/**
//	 * @param p_aMNPayrollDetailElementValue the aMNPayrollDetailElementValue to set
//	 */
//	public static void setAMNPayrollDetailElementValue(int p_aMNPayrollDetailElementValue) {
//		AMNPayrollDetailElementValue = p_aMNPayrollDetailElementValue;
//	}
//	
}

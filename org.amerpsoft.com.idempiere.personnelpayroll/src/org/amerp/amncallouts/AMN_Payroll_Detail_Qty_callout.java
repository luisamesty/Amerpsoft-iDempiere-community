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
package org.amerp.amncallouts;

import java.math.*;
import java.sql.*;
import java.util.Properties;

import javax.script.*;

import org.adempiere.base.IColumnCallout;
import org.amerp.amnmodel.*;
import org.amerp.amnutilities.AmerpPayrollCalc;
import org.amerp.amnutilities.AmerpPayrollCalc.scriptResult;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.util.*;

/**
 * @author luisamesty
 *
 */
public class AMN_Payroll_Detail_Qty_callout implements IColumnCallout {

	CLogger log = CLogger.getCLogger(AMN_Payroll_Detail_Qty_callout.class);
	// Concept Local Variables
	int contador=0;
	int Concept_CalcOrder=0;
	String Concept_Value = "" ;
	String Concept_Name = "Nombre del Concepto" ;
	String Concept_Formula = "" ;
	String Concept_Description = "Descripcon del Concepto" ;
	String Concept_Script = "Script del Concepto" ;
	String Concept_DefaultValueERROR="";
	String Process_Value="XX";
	int Process_ID=0;
	int Concept_Types_Proc_ID=0;
	int Concept_Types_ID=0;
	String  Concept_UOM ,Concept_OptMode, Concept_Sign, Concept_Rule, Concept_IsRepeat, Concept_IsShow, Concept_Variable="";
	int Concept_DefaultValue=1;
	String Concept_DefaultValueST="";
	// Auxiliary Variables
	BigDecimal qtyValueRead,calcAmnt,AllocAmnt,DeducAmnt,qtyRead,Concept_DefaultValueBD  = BigDecimal.valueOf(0.00);
	Double DAllocAmnt,DDeducAmnt = 0.00; 
	Double salary,workdays,dayini,dayend=0.00;
	BigDecimal workdaysDT;
	// Common,Formula Variables
	BigDecimal va_CN,va_SB,va_DT,va_SSO,va_SOF,va_FAOV,va_ISLR,va_INCE,va_VACACION,va_PRESTACION,va_UTILIDAD = BigDecimal.valueOf(0.00);
	int Employee_ID,Payroll_ID,Contract_ID= 0;
	String formula,script;
	String ErrorMessage="OK";
	scriptResult RetVal = new scriptResult();
	/* (non-Javadoc)
	 * @see org.adempiere.base.IColumnCallout#start(java.util.Properties, int, org.compiere.model.GridTab, org.compiere.model.GridField, java.lang.Object, java.lang.Object)
	 */
	@Override
	public String start(Properties p_ctx, int WindowNo, GridTab p_mTab, GridField p_mField, Object p_value, Object p_oldValue) {
		// *************************************
		// FieldRef: AMN_Concept_Types_Proc_ID
		// *************************************
		if (p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_AMN_Concept_Types_Proc_ID) != null 
				&& p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_QtyValue) != null  )
		{
			// *****************************************************
			// * Get Concept's Attributes fromConcept_Types_Proc_ID
			// ******************************************************
			Concept_Types_Proc_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_AMN_Concept_Types_Proc_ID);
			//log.warning("Concept_Types_Proc_ID:"+Concept_Types_Proc_ID);  
			String sql = "" +
					"SELECT " + 
					"cty.value, " + 
					"coalesce(cty.name,'') as name, " + 
					"coalesce(cty.description,cty.name,'') as description,  " + 
					"coalesce(cty.formula,'') as formula, " + 
					"cty.calcorder,  " + 
					"cty.amn_concept_types_id, " + 
					"ctp.amn_process_id, " + 
					"ctp.amn_concept_types_proc_id, " +
					"cum.value as cuom, " +
					"cty.optmode as coptmode, " +
					"cty.sign as csign, " +
					"cty.rule as crule, " +
					"cty.isrepeat as isrepeat, " +
					"cty.isshow as isshow, " +
					"cty.defaultvalue, " +
					"coalesce(cty.variable,'') as cvariable, " +
					"coalesce(cty.script,'') as script " +
					"FROM amn_concept_types as cty " + 
					"LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id = cty.amn_concept_types_id) " + 
					"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) " +
					"WHERE ctp.amn_concept_types_proc_id = ? " +
	                " AND cty.isactive ='Y' " +
	                " AND ctp.isactive ='Y' ";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, Concept_Types_Proc_ID);
				rs = pstmt.executeQuery();
				if (rs.next())
				{
					Concept_Value= rs.getString(1).trim();
					Concept_Name = rs.getString(2).trim();
					Concept_Description = rs.getString(3).trim();
					Concept_Formula = rs.getString(4).trim();
					Concept_CalcOrder =rs.getInt(5);
					Concept_Types_ID=rs.getInt(6);
					Process_ID=rs.getInt(7);
					Concept_Types_Proc_ID=rs.getInt(8);
					Concept_UOM =rs.getString(9).trim();
					Concept_OptMode=rs.getString(10);
					Concept_Sign=rs.getString(11).trim();
					Concept_Rule=rs.getString(12).trim();
					Concept_IsRepeat=rs.getString(13).trim();
					Concept_IsShow=rs.getString(14).trim();
					Concept_DefaultValueST=rs.getString(15);
					Concept_Variable=rs.getString(16);
					Concept_Script= rs.getString(17).trim();
				}
			}
			catch (SQLException e)
			{
				Concept_Name = "Error en Nombre del Concepto" ;
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
			MAMN_Process amnprocess = new MAMN_Process(p_ctx, Process_ID, null);
			Process_Value=amnprocess.getValue().trim();
			// Rules and DV_
			boolean forceRulesInit=false;
//			if (amnprocess.getAMN_Process_Value().compareToIgnoreCase("PL")== 0)
//				forceRulesInit=true;
			boolean forceDVInit=true;
			// ******************************
			// Set field's Values: 
			// ******************************
			// Set Sequence-Value-Name-Formula
			p_mTab.setValue("CalcOrder", Concept_CalcOrder);
			p_mTab.setValue("Value", Concept_Value);
			// A_Tab.setValue("Value", (Concept_CalcOrder*100));
			p_mTab.setValue("Name", Concept_Name);
			// Eliminated p_mTab.setValue("formula", Concept_Formula);
			p_mTab.setValue("Description",Process_Value + " - "+ Concept_Description);
			p_mTab.setValue("Script",Concept_Script ); 		    
			// ***************************
			// * Get Employee´s Salary SB
			// ***************************
			if (p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_AMN_Payroll_ID) != null) {
				Payroll_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_AMN_Payroll_ID);
				sql = "select distinct  amem.salary "
						+ "from amn_employee as amem "
						+ "left join amn_payroll as ampr "
						+ "on (amem.amn_employee_id = ampr.amn_employee_id ) "
						+ "left join amn_payroll_detail as ampd "
						+ "on (ampr.amn_payroll_id = ampd.amn_payroll_id ) "
						+ "where ampr.amn_payroll_id =?" ;
				va_SB = DB.getSQLValueBD(null, sql, Payroll_ID);    			   
			}
			// ***************************
			// Workingdays 
			// ***************************
			if (p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_AMN_Payroll_ID) != null) {
				Payroll_ID = (Integer) p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_AMN_Payroll_ID);
				//Deprecated workdaysDT = (BigDecimal) MAMN_Payroll.sqlGetAMNPayrollDays(Payroll_ID);
				//log.warning("workdaysDT 1="+workdaysDT);
				MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, Payroll_ID, null);
				workdaysDT = amnpayroll.getAMNPayrollDays(Payroll_ID);
				//log.warning("workdaysDT 2="+workdaysDT);
			}
			// ************************************************
			// * Get Quantity CN  - ACCEPT VALUE IF ZERO
			// ************************************************      	        
			qtyRead =  (BigDecimal) ( p_mTab.getValue(MAMN_Payroll_Detail.COLUMNNAME_QtyValue));       	        
			//************************************************
			// Formula Evaluation using Script Engine Manager
			//************************************************
			formula = Concept_Formula.trim(); 
			script = Concept_Script.trim();
			//log.warning(".....................AMN_Payroll_Detail_callout.java...Concepto.... Cantidad:"+qtyRead);
			//log.warning("formula:"+formula+"  va_SB="+va_SB+"   workdaysDT"+workdaysDT);	
			if ((!formula.isEmpty() && formula!=null) || (!script.isEmpty() && script!=null))  {
				try {
					// *******************************************************
					// Calculate Concepts VARIABLES
					// *******************************************************
					AmerpPayrollCalc.PayrollEvaluation(p_ctx,Payroll_ID,Concept_CalcOrder,forceRulesInit, forceDVInit, false);

					// IF script is not Empty or formula equals ("script")
					if (formula.trim().equalsIgnoreCase("script") || ((!script.isEmpty() && script!=null))) {
						RetVal = AmerpPayrollCalc.FormulaEvaluationScript(
								Payroll_ID, Concept_Value, script, qtyRead, va_SB, workdaysDT,Concept_OptMode, false);
						ErrorMessage = RetVal.getErrorMessage();
						calcAmnt = RetVal.getBDCalcAmnt();
						// 
					} else { 	       				
						RetVal = AmerpPayrollCalc.FormulaEvaluationScript(
								Payroll_ID, Concept_Value, formula, qtyRead, va_SB, workdaysDT,Concept_OptMode, false);
						ErrorMessage = RetVal.getErrorMessage();
						calcAmnt = RetVal.getBDCalcAmnt();
					}
					if (ErrorMessage.equalsIgnoreCase("OK")) {
						p_mTab.setValue("Description",Process_Value + " - "+ Concept_Description);
						// Verifiy is ConceptType Shows in Receipt or
						//  if is a Reference
		                if (Concept_OptMode.equalsIgnoreCase("R")) {
							p_mTab.setValue("AmountAllocated",new BigDecimal("0.00"));
							p_mTab.setValue("AmountDeducted",new BigDecimal("0.00"));
		                } else {
							if (Concept_IsShow.trim().equalsIgnoreCase("N")) {
								// Allocation
								if (Concept_Sign.trim().equalsIgnoreCase("D")) {
									p_mTab.setValue("AmountAllocated",new BigDecimal("0.00"));
									p_mTab.setValue("AmountDeducted",new BigDecimal("0.00"));
									//log.warning("Concept_Sign D:"+Concept_Sign);  
								} else {
									// deduction
									p_mTab.setValue("AmountAllocated",new BigDecimal("0.00"));
									p_mTab.setValue("AmountDeducted",new BigDecimal("0.00"));                
									//log.warning("Concept_Sign C:"+Concept_Sign);  
								}
							} else {
								// Allocation
								if (Concept_Sign.trim().equalsIgnoreCase("D")) {
									p_mTab.setValue("AmountAllocated",calcAmnt);
									p_mTab.setValue("AmountDeducted",new BigDecimal("0.00"));
									//log.warning("Concept_Sign D:"+Concept_Sign);  
								} else {
									// deduction
									p_mTab.setValue("AmountAllocated",new BigDecimal("0.00"));
									p_mTab.setValue("AmountDeducted",calcAmnt);                
									//log.warning("Concept_Sign C:"+Concept_Sign);  
								}
							}
		                }
						// CALCULATED VALUE FROM FORMULA
						p_mTab.setValue("AmountCalculated",calcAmnt);
					} else {
						p_mTab.setValue("Name", "** ERROR IN FORMULA **");
						p_mTab.setValue("Description",ErrorMessage);
						p_mTab.setValue("AmountAllocated",new BigDecimal("0.00"));
						p_mTab.setValue("AmountDeducted",new BigDecimal("0.00"));
						p_mTab.setValue("AmountCalculated",new BigDecimal("0.00"));
					}
					//log.warning(".....................AMN_Payroll_Detail_callout.java...............................................");
					//log.warning("script:"+script+" qtyRead:"+qtyRead+"  va_SB="+va_SB+"   workdaysDT"+workdaysDT);	
				}	       		
				catch (ScriptException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			} else {
				Double DAllocAmnt= 0.00;
				Double DDeducAmnt= 0.00;
				// Converted to BigDecimal for AMN_Payroll_detail Model
				AllocAmnt=BigDecimal.valueOf(DAllocAmnt);
				DeducAmnt=BigDecimal.valueOf(DDeducAmnt);
				// *******************************************************
				// Set field's Values: AmountAllocated AmountDeducted = 0
				// *******************************************************
				p_mTab.setValue("AmountAllocated",AllocAmnt );
				p_mTab.setValue("AmountDeducted",DeducAmnt );
				p_mTab.setValue("AmountCalculated",DDeducAmnt);
			}
			// SAVES QTY CHANGED
			p_mTab.dataSave(true);
			// RECALC ALL DOCUMENT
			try {
//log.warning("........AMN_Payroll_Detail_callout-Payroll_ID:"+Payroll_ID+"  qtyRead:"+qtyRead+"  calcAmnt"+calcAmnt);
				//AmerpPayrollCalc.PayrollEvaluationArrayCalculate(p_ctx, Payroll_ID);
				AmerpPayrollCalc.PayrollEvaluationArrayCalculate(p_ctx, Payroll_ID);
				// Refresh Grid
				p_mTab.dataRefreshAll();
				p_mTab.refreshParentTabs();
				// Refresh Parent Grid
				GridTab p_mTabp = p_mTab.getParentTab();
				p_mTabp.dataRefreshAll();
			}
			catch (ScriptException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}

		return null;
	}

	public Integer colocarValorVariable (String varName, BigDecimal varValue) {


		return 0;

	}
}


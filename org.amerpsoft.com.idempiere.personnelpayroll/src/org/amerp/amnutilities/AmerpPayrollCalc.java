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

package org.amerp.amnutilities;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.amerp.amnmodel.*;
import org.compiere.util.*;
import org.compiere.util.DB;

/**
 * @author luisamesty
 * Payroll Calc Methods for Personnel Plugin
 * 
 */

public class AmerpPayrollCalc  {
	
	public AmerpPayrollCalc() {
		super();
	}

	CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);
	
	// Load Variables Values
	PayrollVariables pyVars = new PayrollVariables(true);
	
	/*******************************************************************************************
   	 * Payroll Evaluation using Script Engine Manager
   	 * Evaluates a Concept based on Calcorder and results from ancestry concepts
	 * Parameters:
	 * 	Properties p_ctx
	 * 	int p_AMN_Payroll_ID
	 *  int p_calcorder ( Calculate all concepts until calcorder)
	 *  boolean forceRulesInit 
	 *  boolean forceDVInit
	 *  (Force Rules and Vars Init, usually true on first payroll 
	 *  	row receipt only)   ** DISABLED BY MOMENT ***
	 *  forceRounding: force when true Currency roundind when false default 2
 	 * Notes: 
 	 * This Method is Used by PayrollDetail Callout on Concept_Types_Proc Value
	 *******************************************************************************************/
	public PayrollVariables PayrollEvaluation(Properties p_ctx, int p_AMN_Payroll_ID , 
			int p_calcorder, boolean forceRulesInit, boolean forceDVInit, boolean forceRounding) //throws ScriptException
	{
		// Variables
		//PayrollVariables pyVars = new PayrollVariables(true);
		Integer CalcOrder;
		String ConceptOptmode,ConceptSign,ConceptIsshow="";	
		String Concept_Variable="";
		String p_script ="";
		BigDecimal.valueOf(0);
		Integer Employee_ID=0;
		Integer C_Currency_ID=0;
		String IS_FAOV,IS_SALARIO,IS_INCES,IS_SSO,IS_ARC, IS_SPF, IS_FERIADO,IS_UTILIDAD,IS_PRESTACION, IS_VACACION = "N";
		Double QtyValue, AmountAD, AmountAllocated, AmountDeducted, AmountCalculated;
		String dbType = "PostgreSQL";	// "PostgreSQL, Oracle or UnKnown
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(p_ctx, p_AMN_Payroll_ID, null);
		int AMN_Concept_Types_ID = MAMN_Concept_Types.getConceptTypeIdByCalcOrder(p_ctx, amnpayroll.getAD_Client_ID(), p_calcorder,null);
		String conceptValue = MAMN_Concept_Types.getConceptValueByCalcOrder(p_ctx,amnpayroll.getAD_Client_ID(), p_calcorder,null);
		MAMN_Concept_Types amnconcepttypes =  new MAMN_Concept_Types(p_ctx, AMN_Concept_Types_ID, null);
		Employee_ID = amnpayroll.getAMN_Employee_ID();
    	C_Currency_ID=amnpayroll.getC_Currency_ID();
   		// Default ConversionType for Contract
   		Integer C_ConversionType_ID = AmerpUtilities.defaultAMNContractConversionType(amnpayroll.getAMN_Contract_ID());
		// Init Rules
   		// Executes only one time per payroll Receipt i forceRulesInit=true
    	if (forceRulesInit)
    		pyVars.RTInit(amnpayroll.getAD_Client_ID(), p_AMN_Payroll_ID, amnpayroll.getAMN_Process_ID(), forceRounding);
		// Init DV_XXXX Variables if forceDVInit=true
    	if(forceDVInit) {
    		pyVars.DVInit(amnpayroll.getAD_Client_ID(),p_AMN_Payroll_ID, amnpayroll.getAMN_Process_ID(), forceRounding);
    		pyVars.setConIndexFT(0);   // Force VariablesInit procedure to force init array 
    	}
    	// Variables allways init
    	pyVars.VariablesInit(amnpayroll.getAD_Client_ID());
   		MAMN_Employee emp = new MAMN_Employee(p_ctx, Employee_ID, null);
    	// PayrollVariablesLoad
		PayrollVariablesLoad pyVarsLoad = new PayrollVariablesLoad();
		// p_script (formula)
		String formula = amnconcepttypes.getformula();
		if (formula == null || formula.trim().isEmpty() || formula.toLowerCase().contains("script")) {
		    if (amnconcepttypes.getScript() != null) {
		        p_script = amnconcepttypes.getScript();
		    }
		} else {
		    p_script = formula;
		}

		// Load main Vars
		pyVarsLoad.setPayrollVariablesMain(p_ctx, pyVars, p_AMN_Payroll_ID, conceptValue, p_script, emp.getSalary(), forceRounding);
		// Load Days related Vars
		pyVarsLoad.setPayrolllVariablesDays(p_ctx, pyVars, p_AMN_Payroll_ID);
		// Load SQL Related Vars
		pyVarsLoad.setPayrolllVariablesSQLValues(p_ctx, pyVars, p_AMN_Payroll_ID, C_Currency_ID, C_ConversionType_ID);
		// Payroll Detail Array
		// Verify Database Type
		if ( DB.isPostgreSQL() ) {
			dbType = "PostgreSQL";
		} else if ( DB.isOracle() ) {
			dbType = "Oracle";
		} else {
			dbType = "UnKnown";
			return null ;
		}
    	String sql1 = "SELECT " 
                +	"pad.qtyvalue, "
                +	"cty.formula,  "
                +	"ctp.name as conceptname, "
                +	"cty.calcorder as calcorder, "
                +	"cty.faov as isfaov, "
                +	"coalesce(cty.feriado,'N') as isferiado, "
                +	"coalesce(cty.ince,'N') as isince, " 
                +	"coalesce(cty.prestacion,'N') as isprestacion, "
                +	"coalesce(cty.salario,'N') as issalario, "
                +	"coalesce(cty.sso,'N') as issso, " 
                +	"coalesce(cty.spf,'N') as isspf, "
                +	"coalesce(cty.arc,'N') as isarc, "
                +	"coalesce(cty.vacacion,'N') as isvacacion, " 
                +	"coalesce(cty.utilidad,'N') as isutilidad, "
                +	"pay.invdateini, "
                +	"pay.invdateend, " 
                +	"CAST(DATE_PART('day', pay.invdateend - pay.invdateini) +1  as Integer)  as workingdays, "
                +	"cum.value as cuom, "
                +	"cty.optmode as coptmode, "
                +	"cty.sign as csign, "
                +	"cty.rule as crule, "
                +	"cty.isrepeat as isrepeat, "
                +	"cty.isshow as isshow, "
                +	"pad.amountallocated, "
                +	"pad.amountdeducted, "
                +	"cty.value as creference, "
                +	"coalesce(cty.variable,cty.value,'') as cvariable, "
                +	"pad.amountcalculated "
                +	"FROM amn_payroll as pay "
                +	"LEFT JOIN amn_payroll_detail as pad on (pay.amn_payroll_id = pad.amn_payroll_id) " 
                +	"LEFT JOIN amn_concept_types_proc as ctp on (ctp.amn_concept_types_proc_id = pad.amn_concept_types_proc_id) " 
                +	"LEFT JOIN amn_concept_types as cty on (cty.amn_concept_types_id = ctp.amn_concept_types_id) " 
                +	"LEFT JOIN amn_concept_uom as cum on (cty.amn_concept_uom_id = cum.amn_concept_uom_id) "
                +	"WHERE pay.amn_payroll_id = ? "
                +	" AND cty.calcorder < ? " 
                +	"ORDER BY cty.calcorder";
	    
	    PreparedStatement pstmte = null;
	    ResultSet rse = null;
	    try
	    {
    		pstmte = DB.prepareStatement(sql1, null);
            pstmte.setInt(1, p_AMN_Payroll_ID);
            pstmte.setInt(2, p_calcorder);
            rse = pstmte.executeQuery();	            
            // Reference Concepts Only
            while (rse.next()) {
            	QtyValue= rse.getDouble(1);
                rse.getString(2);
                rse.getString(3);
                CalcOrder = rse.getInt(4);
                IS_FAOV = rse.getString(5);
                IS_FERIADO = rse.getString(6);
                IS_INCES = rse.getString(7);
                IS_PRESTACION = rse.getString(8);
                IS_SALARIO = rse.getString(9);
                IS_SSO = rse.getString(10);
                IS_SPF = rse.getString(11);
                IS_ARC = rse.getString(12);
                IS_VACACION = rse.getString(13);
                IS_UTILIDAD = rse.getString(14);
                rse.getDate(15);
                rse.getDate(16);
                ConceptOptmode = rse.getString(19);
                ConceptSign = rse.getString(20);
                rse.getString(21);
                rse.getString(22);
                ConceptIsshow = rse.getString(23);
                AmountAllocated=rse.getDouble(24);
                AmountDeducted=rse.getDouble(25);
                conceptValue = rse.getString(26);
                Concept_Variable = rse.getString(27).trim();
                AmountCalculated=rse.getDouble(28);
                // Reference Concepts DIAS,HORAS,PNRM (Only)
            	if (ConceptOptmode.equalsIgnoreCase("R")) {
            		AmountAD = AmountCalculated;
        			if (conceptValue.equalsIgnoreCase("DIAS")) {  
        				pyVars.set("DIAS",QtyValue);  //BigDecimal.valueOf(QtyValue);
        				pyVars.set("DT",QtyValue);
        				pyVars.set("WorkingDaysDT",QtyValue);
        			}         
        			if (conceptValue.equalsIgnoreCase("HORAS")) {  
        				pyVars.set("HORAS",QtyValue);  //BigDecimal.valueOf(QtyValue);
        			}         
        			if (conceptValue.equalsIgnoreCase("PNRM")) {  
        				pyVars.set("PNRM",QtyValue);  //BigDecimal.valueOf(QtyValue);
        			}         
        		} 
                // Evaluate CalcOrder less than p_calcorder
                // For Avoid Circular References
            	//	pyVars.addTo("VAR_NAME", AmountAllocated);
                if (CalcOrder <= p_calcorder) {
                	//log.warning("Concept_Name:"+Concept_Name+"  DT:"+DT);
                	// TOTAL ASSING DEDUC ALLLOCATED DEDUCTED ACCUMULATION
                	pyVars.addTo("R_ASIG", AmountAllocated);
                	pyVars.addTo("R_DEDUC", AmountDeducted);
                	pyVars.addTo("R_TOTAL", AmountAllocated - AmountDeducted);
                	// RA_XXXX ACCOUNTING ALLLOCATED DEDUCTED ACCUMULATION
                	// RE_XXXX Employee Payroll Receipt ALLLOCATED DEDUCTED ACCUMULATION
                	if (ConceptIsshow.equalsIgnoreCase("Y")) {
                		pyVars.addTo("RE_ASIG", AmountAllocated);
	                	pyVars.addTo("RE_DEDUC", AmountDeducted);
	                	pyVars.addTo("RE_TOTAL", AmountAllocated - AmountDeducted);
                	} else {
                		pyVars.addTo("RA_ASIG", AmountAllocated);
	                	pyVars.addTo("RA_DEDUC", AmountDeducted);
	                	pyVars.addTo("RA_TOTAL", AmountAllocated - AmountDeducted);           		
                	}
                	// REST OF ALLLOCATED VARIABLES ACCUMULATION
                	if (ConceptOptmode.equalsIgnoreCase("R")) {
                		AmountAD = AmountCalculated;
                	} else {
	                	if (ConceptSign.equalsIgnoreCase("D")) {
	                		AmountAD = AmountAllocated;
	                	} else {
	                		// Accumulate Value for Credits too
	                		AmountAD = AmountDeducted; //(Double) 0.00;
	                	}
                	}
                	/* Change on Business Logic
                	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
                	 * Variable R_FAOV,R_SALARIO,R_INCES,R_SSO,R_ARC,R_SPF, R_DESCANSO,R_FERIADO,R_UTILIDAD,R_VACACION, R_PRESTACION
					 * Only cummulates on Allocation
                	 */
                	if (IS_FAOV.equalsIgnoreCase("Y")) { 
                		pyVars.addTo("R_FAOV", AmountAllocated);
        			}
                	if (IS_FERIADO.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_FERIADO", AmountAllocated);
                		pyVars.addTo("R_DESCANSO", AmountAllocated);
        			}	                	
                	if (IS_INCES.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_INCES", AmountAllocated);
        			}
                	if (IS_PRESTACION.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_PRESTACION", AmountAllocated);
        			}
                	if (IS_SALARIO.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_SALARIO", AmountAllocated);
                	}
                	if (IS_SSO.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_SSO", AmountAllocated);   		
        			}
                	if (IS_SPF.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_SPF", AmountAllocated);
        			}
                	if (IS_ARC.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_ARC", AmountAllocated);
        			}
                	if (IS_VACACION.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_VACACION", AmountAllocated);
        			}
                	if (IS_UTILIDAD.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("R_UTILIDAD", AmountAllocated);
        			}
                	/* Change on Business Logic
                	 * Variable RA_TAX, RD_TAX, RA_SSO, RD_SSO
                	 * For Version 5.1 and UP ** IMPORTANT Dec 2019
					 * Cummulates on Allocation and Deduction Depending on Sign
                	 */
                	if (IS_ARC.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("RA_TAX", AmountAllocated);
                		pyVars.addTo("RD_TAX", AmountAllocated);
        			}
                	if (IS_SSO.equalsIgnoreCase("Y")) {  
                		pyVars.addTo("RA_SSO", AmountAllocated);
                		pyVars.addTo("RD_SSO", AmountAllocated);
        			}
                	// FIXED CONCEPT VARIABLES
                	if (Concept_Variable.equalsIgnoreCase("HNN")) {
                		pyVars.addTo("QTY_HNN", AmountAllocated);
                		pyVars.addTo("RSU_HNN", AmountAllocated);
                	}
                	if (Concept_Variable.equalsIgnoreCase("HND")) {
                		pyVars.addTo("QTY_HND", AmountAllocated);
                		pyVars.addTo("RSU_HND", AmountAllocated);
                	}	                
                	if (Concept_Variable.equalsIgnoreCase("HED")) {
                		pyVars.addTo("QTY_HED", AmountAllocated);
                		pyVars.addTo("RSU_HED", AmountAllocated);
                	}
                	if (Concept_Variable.equalsIgnoreCase("HEN")) {
                		pyVars.addTo("QTY_HEN", AmountAllocated);
                		pyVars.addTo("RSU_HEN", AmountAllocated);
                	}
                	// ************************************
                	// QT_XXXXX AND RS_XXXXXX
                	// DYNAMIC CONCEPT VARABLES ON ARRAY
                	// ************************************
					//if (Concept_Variable.compareToIgnoreCase("RV_FVACAC190")==0) {
					//	log.warning(".....Concept_Variable ... :"+Concept_Variable+"   TrxName:"+amnpayroll.get_TrxName()+"  p_AMN_Payroll_ID:"+p_AMN_Payroll_ID);
					//	}
    				int index=pyVars.CTcontains(Concept_Variable);
    				if (index > 0) {
    					pyVars.CTupdateConceptResults(Concept_Variable, BigDecimal.valueOf(QtyValue), BigDecimal.valueOf(AmountAD), "");
    					//log.warning(".....Concept_Variable Array UPDATE... :"+Concept_Variable+"  QtyValue="+QtyValue+"  AmountAD="+AmountAD);
    				} else {
    					pyVars.CTaddConceptResults(CalcOrder, Concept_Variable, BigDecimal.valueOf(QtyValue), BigDecimal.valueOf(AmountAD), "Add by PayrollEvaluation ..");
    					//log.warning(".....Concept_Variable Array ADD   ... :"+Concept_Variable+"  QtyValue="+QtyValue+"  AmountAD="+AmountAD);
    				}
            	}
           	}
	    }
	    catch (SQLException e)
	    {
	    	//log.warning("catch");
	    	QtyValue= 0.00;
	    	CalcOrder=0;
	    }
	    finally
	    {
	            DB.close(rse, pstmte);
	            rse = null; 
	            pstmte = null;
	    }
	    
	    // logVariablesShow
		// logVariablesShow(pyVars, pyVars);
		return pyVars;

	}
	
	
	

	/**
	 * logVariablesShow
	 * @param pyVars
	 * @return
	 */
	public String logVariablesShow(PayrollVariables pyVars) {
	    
		CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);
		String Message ="";
		int ConIndex = pyVars.getConIndex();
		log.warning("SBMIN:"+pyVars.get("SBMIN"));
		log.warning("QT_SB:"+pyVars.get("QT_SB"));
		log.warning("RS_SB:"+pyVars.get("RS_SB"));
		log.warning("R_ASIG:" + pyVars.get("R_ASIG"));
		log.warning("R_DEDUC:" + pyVars.get("R_DEDUC"));
		log.warning("R_TOTAL:" + pyVars.get("R_TOTAL"));
		log.warning("RA_ASIG:" + pyVars.get("RA_ASIG"));
		log.warning("RA_DEDUC:" + pyVars.get("RA_DEDUC"));
		log.warning("RA_TOTAL:" + pyVars.get("RA_TOTAL"));
		log.warning("RE_ASIG:" + pyVars.get("RE_ASIG"));
		log.warning("RE_DEDUC:" + pyVars.get("RE_DEDUC"));
		log.warning("RE_TOTAL:" + pyVars.get("RE_TOTAL"));
		log.warning("R_SALARIO:" + pyVars.get("R_SALARIO"));
		log.warning("R_FAOV:" + pyVars.get("R_FAOV"));
		log.warning("R_INCES:" + pyVars.get("R_INCES"));
		log.warning("R_SSO:" + pyVars.get("R_SSO"));
		log.warning("R_ARC:" + pyVars.get("R_ARC"));
		log.warning("R_SPF:" + pyVars.get("R_SPF"));
		log.warning("R_DESCANSO:" + pyVars.get("R_DESCANSO"));
		log.warning("R_FERIADO:" + pyVars.get("R_FERIADO"));
		log.warning("R_UTILIDAD:" + pyVars.get("R_UTILIDAD"));
		log.warning("R_VACACION:" + pyVars.get("R_VACACION"));
		log.warning("R_PRESTACION:" + pyVars.get("R_PRESTACION"));
		log.warning("AM_Currency:" + pyVars.get("AM_Currency"));

		log.warning("Variables list ( "+ConIndex+1+" )");
		for (int index = 0; index < ConIndex; ++index)  {
			if (pyVars.ConceptTypes[index].getQtyValue().compareTo(BigDecimal.ZERO) != 0 ||
					pyVars.ConceptTypes[index].getResultValue().compareTo(BigDecimal.ZERO) != 0 ) {
				Message = Message + "Concepto="+pyVars.ConceptTypes[index].getCalcOrder()+" "+
						pyVars.ConceptTypes[index].getConceptVariable()+
						" QTY="+pyVars.ConceptTypes[index].getQtyValue()+
						" Result="+ pyVars.ConceptTypes[index].getResultValue()+"\r\n" ;
			}
		}
		log.warning(Message);
		return Message;
	}
	
	
	public String logRulesShow(PayrollVariables pyVars) {
	    
		CLogger log = CLogger.getCLogger(AmerpPayrollCalc.class);
		String Message ="\r\n";
		int RulIndex = pyVars.getRulIndex();
		log.warning("Rules list ( "+RulIndex+" )");
		for (int index = 0; index < RulIndex; ++index)  {
			if (pyVars.RulesTypes[index].getQtyValue().compareTo(BigDecimal.ZERO) != 0 ||
					pyVars.RulesTypes[index].getResultValue().compareTo(BigDecimal.ZERO) != 0 ) {
				Message = Message + "AMN_Payroll_ID="+pyVars.RulesTypes[index].getAMN_Payroll_ID()+
						" Order="+pyVars.RulesTypes[index].getRuleOrder()+
						" Variable="+pyVars.RulesTypes[index].getRuleVariable()+
						" QTY="+pyVars.RulesTypes[index].getQtyValue()+
						" Result="+ pyVars.RulesTypes[index].getResultValue()+"\r\n" ;
			}
		}
		log.warning(Message);
		return Message;
	}
		
 

	 
	
	
	/**
     * Returns the <code>indexth item in the collection. The index 
     * starts at 0. 
     * @param index  index into the collection. 
     * @return  QtyValue , ResultValue , ErrorMessage
     * @exception XSException
     *   INDEX_SIZE_ERR: if <code>index is greater than or equal to the 
     *   number of objects in the list.
     */
    /* CalcOrder */
	public int getCalcOrder(int index) 
        throws Exception {
        int ConIndex = pyVars.getConIndex();
        if(index < 0 || index > ConIndex - 1) {
            throw new RuntimeException("CONCEPT INDEX SIZE ERR");
        }
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[index];
		return CT.getCalcOrder();
    }
	
    public void setCalcOrder(int p_index, int p_CalcOrder) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[p_index];
    	CT.setCalcOrder(p_CalcOrder);
    	pyVars.ConceptTypes[p_index]=CT;
    }
    
    /* ConceptVariable */
	public String getConceptVariable(int index) 
	
            throws Exception {
				int ConIndex = pyVars.getConIndex();
	            if(index < 0 || index > ConIndex - 1) {
	                throw new RuntimeException("CONCEPT INDEX SIZE ERR");
	        }
    		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT=pyVars.ConceptTypes[index];
            return CT.ConceptVariable;
    }
	
	public void setConceptVariable(int p_index, String p_ConceptVariable) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[p_index];
		CT.setConceptVariable(p_ConceptVariable);
		pyVars.ConceptTypes[p_index]=CT;
    }

    /* QtyValue */
	public BigDecimal getQtyValue(int index) 
        throws Exception {
		int ConIndex = pyVars.getConIndex();
        if(index < 0 || index > ConIndex - 1) {
            throw new RuntimeException("CONCEPT INDEX SIZE ERR");
        }
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[index];
        return CT.getQtyValue();
    }
	public void setQtyValue(int p_index, BigDecimal p_QtyValue) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[p_index];
    	CT.setQtyValue(p_QtyValue);
    	pyVars.ConceptTypes[p_index]=CT;
    }
    /* ResultValue */
	public BigDecimal getResultValue(int index) 
            throws Exception {
			int ConIndex = pyVars.getConIndex();
            if(index < 0 || index > ConIndex - 1) {
                throw new RuntimeException("CONCEPT INDEX SIZE ERR");
            }
    		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT=pyVars.ConceptTypes[index];
            return CT.getResultValue();
    }
	public void setResultValue(int p_index, BigDecimal p_ResultValue) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[p_index];
    	CT.setResultValue(p_ResultValue);
    	pyVars.ConceptTypes[p_index]=CT;
    }
   
    /* ErrorMessage */
	public String getErrorMessage(int index) 
            throws Exception {
			int ConIndex = pyVars.getConIndex();
            if(index < 0 || index > ConIndex - 1) {
                throw new RuntimeException("CONCEPT INDEX SIZE ERR");
            }
    		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT=pyVars.ConceptTypes[index];
            return CT.getErrorMessage();
    }
	
	public void setErrorMessage(int p_index, String p_ErrorMessage) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		CT=pyVars.ConceptTypes[p_index];
    	CT.setErrorMessage(p_ErrorMessage);
    	pyVars.ConceptTypes[p_index]=CT;
    }

}



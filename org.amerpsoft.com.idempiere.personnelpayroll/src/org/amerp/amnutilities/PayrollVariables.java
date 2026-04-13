package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.amerp.amnmodel.MAMN_Payroll;
import org.compiere.model.MCurrency;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class PayrollVariables {

	// MAP of Variables
    private final Map<String, Double> payrollVars = new HashMap<>();
    private final Map<String, String> payrollStrings = new HashMap<>();

    public PayrollVariables(boolean initValues) {
    	if (initValues)
    		initDefaultPayrollKeys();
    }

	CLogger log = CLogger.getCLogger(PayrollVariables.class);
	
    public void initDefaultPayrollKeys() {
        String[] doubleKeys = {
            "R_ASIG", "R_DEDUC", "R_TOTAL",
            "RA_ASIG", "RA_DEDUC", "RA_TOTAL",
            "RE_ASIG", "RE_DEDUC", "RE_TOTAL",
            "R_FAOV", "R_SALARIO", "R_INCES", "R_SSO", "R_ARC", "R_SPF",
            "R_DESCANSO", "R_FERIADO", "R_UTILIDAD", "R_VACACION", "R_PRESTACION",
            "RA_TAX", "RD_TAX", "RA_SSO", "RD_SSO",
            "WorkingDaysDT", "QT_SB", "RS_SB", "ZEROPLUS",
            "DIAS", "HORAS", "PNRM", "DT", "DTREC", "DTPER", "DTOK",
            "NONLABORDAYS", "LABORDAYS", "HOLLIDAYS",
            "BUSINESSDAYS", "NONBUSINESSDAYS", "WEEKENDDAYS", "NONWEEKENDDAYS",
            "NLUNES", "NMONDAY", "LASTVALUE",
            "CTL_AmountAllocated", "CTL_QtyTimes", "CTL_Rate", "CTL_QtyReceipts",
            "UNIDADTRIBUTARIA", "SBMIN", "TAXRATE",
            "QTY_HND", "QTY_HNN", "QTY_HED", "QTY_HEN",
            "RSU_HND", "RSU_HNN", "RSU_HED", "RSU_HEN",
            "AMN_downwardloads", "AMN_increasingloads"
        };

        for (String key : doubleKeys) {
            payrollVars.put(key, 0.0);
        }

        String[] stringKeys = {
            "ORGSECTOR", "IS_FAOV", "IS_SALARIO", "IS_INCES", "IS_SSO", "IS_ARC",
            "IS_SPF", "IS_DESCANSO", "IS_FERIADO", "IS_UTILIDAD", "IS_PRESTACION", "IS_VACACION",
            "AM_Contract", "AM_Process", "AM_Shift", "AM_PayrollMode", "AM_Status",
            "AM_IncomeDate", "AM_PaymentType", "AM_CivilStatus", "AM_Sex", "AM_Spouse",
            "AM_IsPensioned", "AM_IsStudyng", "AM_IsMedicated", "AM_BirthDate",
            "REC_InitDate", "REC_EndDate", "ACCT_Date", "REF_InitDate", "REF_EndDate",
            "AM_Workforce", "AM_Department", "AM_Location", "AM_Project", "AM_Activity",
            "AM_Jobtitle", "AM_Jobstation", "AM_Jobunit", "AM_Currency"
        };

        for (String key : stringKeys) {
            payrollStrings.put(key, "");
        }

        // Valores por defecto específicos
        payrollVars.put("CTL_QtyTimes", 1.0);
        payrollVars.put("NONLABORDAYS", 1.0);
        payrollVars.put("LABORDAYS", 1.0);
        payrollStrings.put("IS_VACACION", "N");
        payrollStrings.put("AM_Workforce", "A");
    }

    public void initPayrollVar(String key) {
        payrollVars.put(key, 0.0);
    }

    public void addTo(String key, Double amount) {
        double current = payrollVars.getOrDefault(key, 0.0);
        payrollVars.put(key, current + (amount != null ? amount : 0.0));
    }

    public void set(String key, Double amount) {
        payrollVars.put(key, amount != null ? amount : 0.0);
    }

	public void set(String key, String format) {
		 payrollStrings.put(key, format != null ? format : "");
	} 
	
    public Double get(String key) {
        return payrollVars.getOrDefault(key, 0.0);
    }

    public void setString(String key, String value) {
        payrollStrings.put(key, value != null ? value : "");
    }

    public String getString(String key) {
        return payrollStrings.getOrDefault(key, "");
    }

    public Map<String, Double> getPayrollVars() {
        return payrollVars;
    }

    public Map<String, String> getPayrollStrings() {
        return payrollStrings;
    }

	/**
	 * 
	 * @param p_aD_Client_ID
	 */
	public void VariablesInit(Integer p_aD_Client_ID) {
		// 
		initDefaultPayrollKeys(); 
		// 
		CTInit(p_aD_Client_ID);
		
	} // VariablesInit
	
	
	// FORMULA DOUBLE VARIABLES
	public Double R_ASIG, R_DEDUC, R_TOTAL ;
	public Double RA_ASIG, RA_DEDUC, RA_TOTAL, RE_ASIG, RE_DEDUC, RE_TOTAL;	
	public Double WorkingDaysDT=0.00;
	public Double QT_SB=0.00;
	public Double RS_SB=0.00;
	public Double ZEROPLUS=0.00;
	public Double DIAS,HORAS,PNRM,DT,DTREC,DTPER,DTOK,NONLABORDAYS,LABORDAYS,HOLLIDAYS;
	public Double BUSINESSDAYS, NONBUSINESSDAYS, WEEKENDDAYS, NONWEEKENDDAYS;
	public Double NLUNES,NMONDAY;
	public Double LASTVALUE;
	public Double CTL_AmountAllocated;	// Default Value ZERO
	public Double CTL_QtyTimes;  		// Default Value ONE
	public Double CTL_Rate;				// Default Value ZERO
	public Double CTL_QtyReceipts;		// Default Value ZERO
	public String ORGSECTOR=""; 
	public Double UNIDADTRIBUTARIA,SBMIN,TAXRATE;
	public Double QTY_HND,QTY_HNN,QTY_HED,QTY_HEN;
	public Double RSU_HND,RSU_HNN,RSU_HED,RSU_HEN;
	public String IS_FAOV,IS_SALARIO,IS_INCES,IS_SSO,IS_ARC, IS_SPF, IS_DESCANSO, IS_FERIADO,IS_UTILIDAD,IS_PRESTACION, IS_VACACION = "N";
	public String AM_Contract="";
	public String AM_Process="";
	// Employee Variables
	public String AM_Shift ="";
	public String AM_PayrollMode="";
	public String AM_Status ="";
	public String AM_IncomeDate="";
	public String AM_PaymentType="";
	public String AM_CivilStatus="";
	public String AM_Sex="";
	public String AM_Spouse="";
	public String AM_IsPensioned="";
	public String AM_IsStudyng="";
	public String AM_IsMedicated="";
	public String AM_BirthDate="";
	public String REC_InitDate="";
	public String REC_EndDate="";
	public String ACCT_Date="";
	public String REF_InitDate="";
	public String REF_EndDate="";
	public Double AMN_downwardloads;
	public Double AMN_increasingloads;
	// Work Force (M.A.D.I.S), Department Value, Location Value, Project Value, 
	// Activity Value, Jobtitle Value, Jobstation Value
	public String AM_Workforce="A";
	public String AM_Department="";
	public String AM_Location="";
	public String AM_Project="";
	public String AM_Activity="";
	public String AM_Jobtitle="";
	public String AM_Jobstation="";
	public String AM_Jobunit="";
	
	public String AM_Currency="";
	//public AmerpQtyResultConcepts  ConceptTypes  = new  AmerpQtyResultConcepts(null);
	public Double R_FAOV,R_SALARIO,R_INCES,R_SSO,R_ARC,R_SPF, R_DESCANSO,R_FERIADO,R_UTILIDAD,R_VACACION, R_PRESTACION ;
	// Cummulated Values for TAX and SSO  (TAX == ARC)
	public Double RA_TAX, RD_TAX, RA_SSO, RD_SSO;
	
	
	public int ConIndex=0,ConIndexFT=0;
	public AmerpConceptTypes[] ConceptTypes = new AmerpConceptTypes[500];
	public int RulIndex=0,RulIndexFT=0;
	public AmerpRulesTypes[] RulesTypes = new AmerpRulesTypes[50];
	public int DVRulIndex=0,DVRulIndexFT=0;
	public AmerpRulesTypes[] DVRulesTypes = new AmerpRulesTypes[50];
	// listMemoryVariables  Memory Variables Array (futute use)
	public List<PayrollVariablesMemoryVars> listMemoryVariables = new ArrayList<PayrollVariablesMemoryVars>();
	
	public int getConIndex() {
		return ConIndex;
	}
	public void setConIndex(int conIndex) {
		ConIndex = conIndex;
	}
	public int getConIndexFT() {
		return ConIndexFT;
	}
	public void setConIndexFT(int conIndexFT) {
		ConIndexFT = conIndexFT;
	}
	public AmerpConceptTypes[] getConceptTypes() {
		return ConceptTypes;
	}
	public void setConceptTypes(AmerpConceptTypes[] conceptTypes) {
		ConceptTypes = conceptTypes;
	}
	public int getRulIndex() {
		return RulIndex;
	}
	public void setRulIndex(int rulIndex) {
		RulIndex = rulIndex;
	}
	public int getRulIndexFT() {
		return RulIndexFT;
	}
	public void setRulIndexFT(int rulIndexFT) {
		RulIndexFT = rulIndexFT;
	}
	public AmerpRulesTypes[] getRulesTypes() {
		return RulesTypes;
	}
	public void setRulesTypes(AmerpRulesTypes[] rulesTypes) {
		RulesTypes = rulesTypes;
	}
	public int getDVRulIndex() {
		return DVRulIndex;
	}
	public void setDVRulIndex(int dVRulIndex) {
		DVRulIndex = dVRulIndex;
	}
	public int getDVRulIndexFT() {
		return DVRulIndexFT;
	}
	public void setDVRulIndexFT(int dVRulIndexFT) {
		DVRulIndexFT = dVRulIndexFT;
	}
	public AmerpRulesTypes[] getDVRulesTypes() {
		return DVRulesTypes;
	}
	public void setDVRulesTypes(AmerpRulesTypes[] dVRulesTypes) {
		DVRulesTypes = dVRulesTypes;
	}
	public List<PayrollVariablesMemoryVars> getListMemoryVariables() {
		return listMemoryVariables;
	}
	public void setListMemoryVariables(List<PayrollVariablesMemoryVars> listMemoryVariables) {
		this.listMemoryVariables = listMemoryVariables;
	}
	
	
    /**
     * The number of ConceptTypes in the list. 
     */
	public int CTgetLength() {
        return ConIndex;
    }
	
	/**
	 * CTInit.
	 * 
	 * @param p_aD_Client_ID
	 */
	public boolean CTInit(Integer p_aD_Client_ID) {
		int i = 0;
		if (ConIndexFT == 0) {
			AmerpConceptTypes CT = new AmerpConceptTypes(0, "", BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00), "");
			String ConceptVariable;
			// log.warning("........PASO....CTInit....");
			int CalcOrder;
			String sql1 = "SELECT " + "value as codigo, " + "COALESCE(variable,value,'') as ctyvariable, "
					+ "calcorder " + "FROM amn_concept_types " + "WHERE " + "ad_client_id IN (0,?)"
					+ "ORDER BY calcorder ";
			PreparedStatement pstmte = null;
			ResultSet rse = null;
			try {
				pstmte = DB.prepareStatement(sql1, null);
				pstmte.setInt(1, p_aD_Client_ID);
				rse = pstmte.executeQuery();
				// Reference Concepts Only
				while (rse.next()) {
					rse.getString(1).trim();
					ConceptVariable = rse.getString(2).trim();
					CalcOrder = rse.getInt(3);
					CT.setCalcOrder(CalcOrder);
					CT.setConceptVariable(ConceptVariable);
					CT.setErrorMessage("");
					CT.setQtyValue(BigDecimal.valueOf(0.00));
					CT.setResultValue(BigDecimal.valueOf(0.00));
					ConceptTypes[i] = new AmerpConceptTypes(0, "", BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00),
							"");
					// ConceptTypes[i]=CT;
					ConceptTypes[i].setCalcOrder(CalcOrder);
					ConceptTypes[i].setConceptVariable(ConceptVariable);
					ConceptTypes[i].setErrorMessage("");
					ConceptTypes[i].setQtyValue(BigDecimal.valueOf(0.00));
					ConceptTypes[i].setResultValue(BigDecimal.valueOf(0.00));
					i = i + 1;
				}
				ConIndex = i;
				ConIndexFT = i;
			} catch (SQLException e) {
				// log.warning("catch");
			} finally {
				DB.close(rse, pstmte);
				rse = null;
				pstmte = null;
			}

		} else {
			for (i = 0; i < ConIndex; i++) {
				ConceptTypes[i].setErrorMessage("");
				ConceptTypes[i].setQtyValue(BigDecimal.valueOf(0.00));
				ConceptTypes[i].setResultValue(BigDecimal.valueOf(0.00));
			}
		}

		return true;
	}
	/**
	 * 
	 * @param p_CalcOrder
	 * @param p_ConceptVariable
	 * @param p_QtyValue
	 * @param p_ResultValue
	 * @param p_ErrorMessage
	 * @return
	 */
	public boolean CTaddConceptResults (int p_CalcOrder, String p_ConceptVariable, 
    		BigDecimal p_QtyValue, BigDecimal p_ResultValue, String p_ErrorMessage) 
    {
		int Index = 0;
		if (CTcontains(p_ConceptVariable) < 0 ) {
			// CREATE NEW RECORD IF NOT EXISTS
			Index = ConIndex+1;
			ConceptTypes[Index] = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
		} else {
			// UPDATE RECORD IF EXIST
			Index= CTcontains(p_ConceptVariable);
		}
		//ConceptTypes[i]=CT;
		ConceptTypes[Index].setCalcOrder(p_CalcOrder);
		ConceptTypes[Index].setConceptVariable(p_ConceptVariable);
		ConceptTypes[Index].setErrorMessage(p_ErrorMessage);
		ConceptTypes[Index].setQtyValue(p_QtyValue);
		ConceptTypes[Index].setResultValue(p_ResultValue);
		
    	return true;
    }
	
	/**
	 * 
	 * @param p_ConceptVariable
	 * @param p_QtyValue
	 * @param p_ResultValue
	 * @param p_ErrorMessage
	 * @return
	 */
	public boolean CTupdateConceptResults ( String p_ConceptVariable, 
    		BigDecimal p_QtyValue, BigDecimal p_ResultValue, String p_ErrorMessage) 
    {
		int Index = 0;
		Index=CTcontains(p_ConceptVariable);
		if ( Index > 0 ) {
			ConceptTypes[Index].setQtyValue(p_QtyValue);
			ConceptTypes[Index].setResultValue(p_ResultValue);
			if (!p_ErrorMessage.equalsIgnoreCase("") && p_ErrorMessage != null) {
				ConceptTypes[Index].setErrorMessage(p_ErrorMessage);
			}
		}
		return false;
    }

    /**
     * Checks if the ConceptVariable is a member of this list. 
     * @param ConceptVariable  Strig whose presence in this list is to be tested. 
     * @return  index if this list contains the String -1 if not. 
     */
	public int CTcontains(String ConceptVariable) {
		AmerpConceptTypes CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    	for (int i = 0; i < ConIndex; ++i) {
    		CT = new  AmerpConceptTypes(0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    		CT = ConceptTypes[i];
    		//log.warning("....CTcontains...i:"+i+"  ConIndex:"+ConIndex+"   ConceptVariable:"+ConceptVariable+"  CT.ConceptVariable:"+CT.getConceptVariable()+"  CT.QtyValue:"+CT.getQtyValue()+"  CT.ResultValue:"+CT.getResultValue());
            if ( CT.getConceptVariable().equalsIgnoreCase(ConceptVariable) ) {
                return i;
            }
        }
        return -1;
    }
	
	/**
	 *  DVInit
	 * @param p_AD_Client_ID
	 * @param p_AMN_Payroll_ID
	 * @return
	 */
	public boolean DVInit(Integer p_AD_Client_ID, Integer p_AMN_Payroll_ID, Integer p_AMN_Process_ID, boolean forceRounding) {

		// Default Values Init
		int i =0;
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_ID, null);
		BigDecimal VarDefValueBD = BigDecimal.ZERO;
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		// Rounding Mode
		int roundingMode = 2;
		if (forceRounding )  
			roundingMode = curr.getStdPrecision();
		//
		List<String> DV_Variables = AmerpPayrollCalcUtilDVFormulas.initDV_Variables (Env.getCtx(), p_AMN_Process_ID);
		String Mess ="\r\n";
		String errorResult="";
		String element="";
		for (i = 0; i < DV_Variables.size() ; i++) {
			element=DV_Variables.get(i);
			DVRulesTypes[i] = new  AmerpRulesTypes(0,0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
			//ConceptTypes[i]=CT;
			DVRulesTypes[i].setRuleOrder(i);
			DVRulesTypes[i].setAMN_Payroll_ID(p_AMN_Payroll_ID);;
			DVRulesTypes[i].setRuleVariable(element);
			DVRulesTypes[i].setRuleDescription("");
			DVRulesTypes[i].setErrorMessage("");
			DVRulesTypes[i].setQtyValue(BigDecimal.ONE);
			DVRulesTypes[i].setResultValue(BigDecimal.ZERO);
		}
		for (i = 0; i < DV_Variables.size() ; i++) {
			errorResult="OK";
			element=DVRulesTypes[i].getRuleVariable();
			// Calculates element DV_XXXXX
			try {
				VarDefValueBD = AmerpPayrollCalcUtilDVFormulas.processDefaultValue(Env.getCtx(), element, p_AMN_Payroll_ID, amnpayroll.get_TrxName() );
			} catch (Exception e) {
				// 
				e.printStackTrace();
				errorResult="** Default Values Init - Error on calc **";
			}
			if (VarDefValueBD == null) {
				VarDefValueBD = BigDecimal.ZERO;
			}
			DVRulesTypes[i].setResultValue(VarDefValueBD.setScale(roundingMode, RoundingMode.HALF_UP));
			DVRulesTypes[i].setErrorMessage(errorResult);
			Mess = Mess + "No.="+i+"  "+element + "(created)="+VarDefValueBD+"\r\n";
		}
        DVRulIndex=i;
        DVRulIndexFT=i;
        //log.warning(Mess);
	    return true;
	}
	
    /**
     * RTInit. Rules Array Init
     * @param p_aD_Client_ID 
     * @param p_AMN_Payroll_ID
     */
	public boolean RTInit(Integer p_AD_Client_ID, Integer p_AMN_Payroll_ID, Integer p_AMN_Process_ID, boolean forceRounding) {
		int i =0;
		String RuleVariable="";
		String RuleDescription="";
		int RuleOrder=0;
		BigDecimal RTVarMRuleBD = BigDecimal.ZERO;
		// AMN Payroll
		MAMN_Payroll amnpayroll = new MAMN_Payroll(Env.getCtx(), p_AMN_Payroll_ID, null);
		// Rounding Mode
		MCurrency curr = new MCurrency(Env.getCtx(),amnpayroll.getC_Currency_ID(),null);
		// Rounding Mode
		int roundingMode = 2;
		if (forceRounding )  
			roundingMode = curr.getStdPrecision();
		// sql1
		String sql1 = "SELECT "
				+   "name as codigo, "
                +	"description as description, "
                +	"ruleorder as ruleorder "
                +	"FROM AMN_Rules "
                +	"WHERE IsActive='Y' AND ad_client_id = ? " 
                +	"ORDER BY ruleorder "
                ;
		//log.warning("sql="+sql1);
		PreparedStatement pstmte = null;
	    ResultSet rse = null;
	    try
	    {
    		pstmte = DB.prepareStatement(sql1, null);
            pstmte.setInt(1, p_AD_Client_ID);
            rse = pstmte.executeQuery();	            
            // Reference Concepts Only
            while (rse.next()) {
    			RuleVariable = rse.getString(1).trim();
    			RuleDescription = rse.getString(2).trim();
            	RuleOrder= rse.getInt(3);
    			//log.warning("i="+i+ "  RuleVariable="+RuleVariable);
    			RulesTypes[i] = new  AmerpRulesTypes(0,0,"",BigDecimal.valueOf(0.00),BigDecimal.valueOf(0.00),"");
    			//ConceptTypes[i]=CT;
    			RulesTypes[i].setRuleOrder(RuleOrder);
    			RulesTypes[i].setAMN_Payroll_ID(p_AMN_Payroll_ID);;
    			RulesTypes[i].setRuleVariable(RuleVariable);
    			RulesTypes[i].setRuleDescription(RuleDescription);
    			RulesTypes[i].setErrorMessage("");
	    		RulesTypes[i].setQtyValue(BigDecimal.ONE);
	    		RulesTypes[i].setResultValue(RTVarMRuleBD);
	    		//log.warning("....RTInit CReate...i:"+i+"   RuleVariable:"+RuleVariable+"  RuleOrder:"+RulesTypes[i].getRuleOrder()+"  QtyValue:"+RulesTypes[i].getQtyValue()+"  ResultValue:"+RulesTypes[i].getResultValue());
				i=i+1;
            }
            RulIndex=i;
            RulIndexFT=i;
	    }
	    catch (SQLException e)
	    {
	    	//log.warning("catch");
	    }
		finally
		{
			DB.close(rse, pstmte);
			rse = null; 
			pstmte = null;
		}
	    //log.warning("RulIndex="+RulIndex);
		for (i = 0; i < RulIndex; ++i) {
			try {
				RuleVariable = RulesTypes[i].getRuleVariable();
				RTVarMRuleBD = AmerpPayrollCalcRules.processRule(Env.getCtx(), RuleVariable, p_AMN_Payroll_ID, amnpayroll.get_TrxName() );
	    		RulesTypes[i].setResultValue(RTVarMRuleBD.setScale(roundingMode, RoundingMode.HALF_UP));
	    		//log.warning("....RTInit CReate...i:"+i+"   RuleVariable:"+RuleVariable+"  RuleOrder:"+RulesTypes[i].getRuleOrder()+"  QtyValue:"+RulesTypes[i].getQtyValue()+"  ResultValue:("+RTVarMRuleBD+")="+RulesTypes[i].getResultValue());
			} catch (Exception e) {
				// 
				RulesTypes[i].setResultValue(BigDecimal.ZERO);
				RulesTypes[i].setErrorMessage("*** ERROR ON AMN_Rules ***");
				String msg = "AMN_Rules Script Invalid: " + e.toString();
				//log.log(Level.SEVERE, msg, e);
				log.log(Level.WARNING, msg, e);
			}
			//log.warning("....RTInit.Review...i:"+i+"  RulIndex:"+RulIndex+"  RT.RuleOrder:"+RulesTypes[i].getRuleOrder()+"  RT.RuleVariable:"+RulesTypes[i].getRuleVariable()+"  RT.QtyValue:"+RulesTypes[i].getQtyValue()+"  RT.ResultValue:"+RulesTypes[i].getResultValue());
		}
		//log.warning("...Rules after INIT ...");
		//AmerpPayrollCalc.logRulesShow();
	    return true;
	}

}

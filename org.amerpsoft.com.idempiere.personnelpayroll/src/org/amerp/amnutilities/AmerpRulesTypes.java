package org.amerp.amnutilities;

import java.math.BigDecimal;

public class AmerpRulesTypes {

	public int RuleOrder;
	public int AMN_Payroll_ID;
	public String RuleVariable;
	public String RuleName;
	public String RuleDescription;
	public BigDecimal QtyValue;
	public BigDecimal ResultValue;
	public String ErrorMessage;

	/**
	 *  AmerpRulesTypes(int p_RuleOrder, Integer p_AMN_Payroll_ID, String p_RuleVariable, BigDecimal p_QtyValue, BigDecimal p_ResultValue, String p_ErrorMessage) 
	 * @param p_RuleOrder
	 * @param p_AMN_Payroll_ID
	 * @param p_RuleVariable
	 * @param p_QtyValue
	 * @param p_ResultValue
	 * @param p_ErrorMessage
	 */
	
    public AmerpRulesTypes(int p_RuleOrder, Integer p_AMN_Payroll_ID, String p_ConceptVariable, BigDecimal p_QtyValue, BigDecimal p_ResultValue, String p_ErrorMessage) {
        // TODO Auto-generated constructor stub
    }

	public int getRuleOrder() {
		return RuleOrder;
	}

	public void setRuleOrder(int ruleOrder) {
		RuleOrder = ruleOrder;
	}

	public int getAMN_Payroll_ID() {
		return AMN_Payroll_ID;
	}

	public void setAMN_Payroll_ID(int aMN_Payroll_ID) {
		AMN_Payroll_ID = aMN_Payroll_ID;
	}

	public String getRuleVariable() {
		return RuleVariable;
	}

	public void setRuleVariable(String ruleVariable) {
		RuleVariable = ruleVariable;
	}

	public String getRuleName() {
		return RuleName;
	}

	public void setRuleName(String ruleName) {
		RuleName = ruleName;
	}

	public String getRuleDescription() {
		return RuleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		RuleDescription = ruleDescription;
	}

	public BigDecimal getQtyValue() {
		return QtyValue;
	}

	public void setQtyValue(BigDecimal qtyValue) {
		QtyValue = qtyValue;
	}

	public BigDecimal getResultValue() {
		return ResultValue;
	}

	public void setResultValue(BigDecimal resultValue) {
		ResultValue = resultValue;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

}

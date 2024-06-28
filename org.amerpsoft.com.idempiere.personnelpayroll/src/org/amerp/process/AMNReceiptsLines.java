package org.amerp.process;

import java.math.BigDecimal;

/**
 * AMNReceiptsLines
 * @author luisamesty
 *
 */
class AMNReceiptLines {
	
	public int AMN_Concept_Type_ID;
	public int CalcOrder;
	public String ConceptValue;
	public String ConceptName;
	public String ConceptDescription;
	public int AMN_Process_ID;
	public int AMN_Concept_Types_Proc_ID;
	public int AMN_Contract_ID;
	public int AMN_Concept_Uom_ID;
	public String DefaultValue;
	public String ScriptDefaultValue;
	public BigDecimal QtyValue;
	public BigDecimal AmountAllocated;
	public BigDecimal AmountDeducted;
	public BigDecimal AmountCalculated;
	
	public AMNReceiptLines() {
		AMN_Concept_Type_ID = 0;
		CalcOrder = 0;
		ConceptValue = "";
		ConceptName = "";
		ConceptDescription = "";
		AMN_Process_ID = 0;
		AMN_Concept_Types_Proc_ID = 0;
		AMN_Contract_ID = 0;
		AMN_Concept_Uom_ID=0;
		DefaultValue = "";
		ScriptDefaultValue = "";
		QtyValue = BigDecimal.ZERO;
		AmountAllocated = BigDecimal.ZERO;
		AmountDeducted = BigDecimal.ZERO;
		AmountCalculated = BigDecimal.ZERO;
		
	}

	public AMNReceiptLines(int p_AMN_Concept_Types_ID, int calcOrder, String conceptValue, String conceptName,
			int p_AMN_Process_ID, int p_AMN_Concept_Type_Proc_ID, int p_AMN_Contract_ID, String defaultvalue,
			String scriptDefaultvalue) {
		super();

		AMN_Concept_Type_ID = p_AMN_Concept_Types_ID;
		CalcOrder = calcOrder;
		ConceptValue = conceptValue;
		ConceptName = conceptName;
		AMN_Process_ID = p_AMN_Process_ID;
		AMN_Concept_Types_Proc_ID = p_AMN_Concept_Type_Proc_ID;
		AMN_Contract_ID = p_AMN_Contract_ID;
		DefaultValue = defaultvalue;
		ScriptDefaultValue = scriptDefaultvalue;
	}

	public int getAMN_Concept_Type_ID() {
		return AMN_Concept_Type_ID;
	}

	public void setAMN_Concept_Type_ID(int aMN_Concept_Type_ID) {
		AMN_Concept_Type_ID = aMN_Concept_Type_ID;
	}

	public int getCalcOrder() {
		return CalcOrder;
	}

	public void setCalcOrder(int calcOrder) {
		CalcOrder = calcOrder;
	}

	public String getConceptValue() {
		return ConceptValue;
	}

	public void setConceptValue(String conceptValue) {
		ConceptValue = conceptValue;
	}

	public String getConceptName() {
		return ConceptName;
	}

	public void setConceptName(String conceptName) {
		ConceptName = conceptName;
	}

	public String getConceptDescription() {
		return ConceptDescription;
	}

	public void setConceptDescription(String conceptDescription) {
		ConceptDescription = conceptDescription;
	}

	public int getAMN_Process_ID() {
		return AMN_Process_ID;
	}

	public void setAMN_Process_ID(int aMN_Process_ID) {
		AMN_Process_ID = aMN_Process_ID;
	}

	public int getAMN_Concept_Types_Proc_ID() {
		return AMN_Concept_Types_Proc_ID;
	}

	public void setAMN_Concept_Type_Proc_ID(int aMN_Concept_Types_Proc_ID) {
		AMN_Concept_Types_Proc_ID = aMN_Concept_Types_Proc_ID;
	}

	public int getAMN_Contract_ID() {
		return AMN_Contract_ID;
	}

	public void setAMN_Contract_ID(int aMN_Contract_ID) {
		AMN_Contract_ID = aMN_Contract_ID;
	}

	public int getAMN_Concept_Uom_ID() {
		return AMN_Concept_Uom_ID;
	}

	public void setAMN_Concept_Uom_ID(int aMN_Concept_Uom_ID) {
		AMN_Concept_Uom_ID = aMN_Concept_Uom_ID;
	}

	public String getDefaultValue() {
		return DefaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		DefaultValue = defaultValue;
	}

	public String getScriptDefaultValue() {
		return ScriptDefaultValue;
	}

	public void setScriptDefaultValue(String scriptDefaultValue) {
		ScriptDefaultValue = scriptDefaultValue;
	}

	public BigDecimal getQtyValue() {
		return QtyValue;
	}

	public void setQtyValue(BigDecimal qtyValue) {
		QtyValue = qtyValue;
	}

	public BigDecimal getAmountAllocated() {
		return AmountAllocated;
	}

	public void setAmountAllocated(BigDecimal amountAllocated) {
		AmountAllocated = amountAllocated;
	}

	public BigDecimal getAmountDeducted() {
		return AmountDeducted;
	}

	public void setAmountDeducted(BigDecimal amountDeducted) {
		AmountDeducted = amountDeducted;
	}

	public BigDecimal getAmountCalculated() {
		return AmountCalculated;
	}

	public void setAmountCalculated(BigDecimal amountCalculated) {
		AmountCalculated = amountCalculated;
	}

}


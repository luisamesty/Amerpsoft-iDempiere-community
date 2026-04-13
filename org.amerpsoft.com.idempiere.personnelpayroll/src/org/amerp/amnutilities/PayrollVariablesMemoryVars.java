package org.amerp.amnutilities;

/****************************************************
 * memoryVars (FUTURE USE )
 * @author luisamesty
 * Return two parameters:
 * Sring  MemVar_Name 
 * Double MemVar_Value
 * 
 ***************************************************/
public class PayrollVariablesMemoryVars {
	
	String MemVar_Name ;
	Double MemVar_Value;
	
	public PayrollVariablesMemoryVars(String memVar_Name, Double memVar_Value) {
		super();
		MemVar_Name = memVar_Name;
		MemVar_Value = memVar_Value;
	}

	public String getMemVar_Name() {
		return MemVar_Name;
	}

	public void setMemVar_Name(String memVar_Name) {
		MemVar_Name = memVar_Name;
	}

	public Double getMemVar_Value() {
		return MemVar_Value;
	}

	public void setMemVar_Value(Double memVar_Value) {
		MemVar_Value = memVar_Value;
	}
	
	
}

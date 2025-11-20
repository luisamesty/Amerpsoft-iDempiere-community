package org.amerp.amnutilities;

import java.math.BigDecimal;

/**************************************************
 * Array Class Formula evaluation
 * Return two parameters: 
 * BigDecimal BDCalcAmnt; String errorMessage="";
 **************************************************/
public class ScriptResult {

	// Constructor vacío
    public ScriptResult() {
        this.BDCalcAmnt = BigDecimal.ZERO;
        this.errorMessage = "";
    }
    
    // Constructor con parámetros
	public ScriptResult(BigDecimal bDCalcAmnt, String errorMessage) {
		super();
		BDCalcAmnt = bDCalcAmnt;
		this.errorMessage = errorMessage;
	}
	
	public BigDecimal BDCalcAmnt;
	public String errorMessage="";
	/**
	 * @return the bDCalcAmnt
	 */
	public BigDecimal getBDCalcAmnt() {
		return BDCalcAmnt;
	}
	/**
	 * @param p_bDCalcAmnt the bDCalcAmnt to set
	 */
	public void setBDCalcAmnt(BigDecimal p_bDCalcAmnt) {
		BDCalcAmnt = p_bDCalcAmnt;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param p_errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String p_errorMessage) {
		errorMessage = p_errorMessage;
	}

}
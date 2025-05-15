/**
 * 
 */
package org.amerp.amnutilities;

import java.math.BigDecimal;

/**
 * AttendanceHours Class
 * @param HR_HND
 * @param HR_HNN
 * @param HR_HED
 * @param HR_HEN
 */
public class AttendanceHours {
	
	public BigDecimal HR_HND;		// Normal Day Hours		(Shift_HND)
	public BigDecimal HR_HNN;		// Normal Night Hours	(Shift_HNN)
	public BigDecimal HR_HED;		// Extra Day Hours		(Shift_HED)
	public BigDecimal HR_HEN;		// Extra Night Hours	(Shift_HEN)
	public BigDecimal DAY_ATT;		// ATTendance			(Shift_Attendance)
	public BigDecimal DAY_ATTB;		// Athendance Bonus		(Shift_AttendanceBonus)
	public String HR_Message;		// Message				(Name)
	public BigDecimal HR_HT;		// Total Work Hours				(Shift_HT)
	public BigDecimal HR_HC;		// Complete Hours				(Shift_HC)
	public BigDecimal HR_HLGT15;	// Free Hours Greater than 15 '	(Shift_HLGT15)
	public BigDecimal HR_HLLT15;	// Free Hours Less than 15 '	(Shift_HLLT15)
	public BigDecimal HR_THL;		// Free Hours					(Shift_THL)
	public BigDecimal HR_LTA;		// Late Arrivals				(Shift_LTA)
	public BigDecimal HR_EDE;		// Early Departure				(Shift_EDE)
	public BigDecimal HR_HER;		// Extra Clock Hours			(Shift_HER)
	public BigDecimal HR_HEF;		// Extra Holliday Hours			(Shift_HEF)
	
	/**
	 * 
	 * @param HR_HND
	 * @param HR_HNN
	 * @param HR_HED
	 * @param HR_HEN
	 * @param DAY_ATT
	 * @param DAY_ATTB
	 * @param HR_Message
	 * @param HR_HT
	 * @param HR_HC
	 * @param HR_HLGT15
	 * @param HR_HLLT15
	 * @param HR_THL
	 * @param HR_LTA
	 * @param HR_EDE
	 * @param HR_HER
	 * @param HR_HEF
	 */
    public AttendanceHours(
            BigDecimal HR_HND, BigDecimal HR_HNN, BigDecimal HR_HED, BigDecimal HR_HEN,
            BigDecimal DAY_ATT, BigDecimal DAY_ATTB, String HR_Message, BigDecimal HR_HT,
            BigDecimal HR_HC, BigDecimal HR_HLGT15, BigDecimal HR_HLLT15, BigDecimal HR_THL,
            BigDecimal HR_LTA, BigDecimal HR_EDE, BigDecimal HR_HER, BigDecimal HR_HEF) {

        this.HR_HND = HR_HND;
        this.HR_HNN = HR_HNN;
        this.HR_HED = HR_HED;
        this.HR_HEN = HR_HEN;
        this.DAY_ATT = DAY_ATT;
        this.DAY_ATTB = DAY_ATTB;
        this.HR_Message = HR_Message;
        this.HR_HT = HR_HT;
        this.HR_HC = HR_HC;
        this.HR_HLGT15 = HR_HLGT15;
        this.HR_HLLT15 = HR_HLLT15;
        this.HR_THL = HR_THL;
        this.HR_LTA = HR_LTA;
        this.HR_EDE = HR_EDE;
        this.HR_HER = HR_HER;
        this.HR_HEF = HR_HEF;
    }
    
    // Constructor vacío
    public AttendanceHours() {
    	  this.HR_HND = BigDecimal.ZERO;
          this.HR_HNN = BigDecimal.ZERO;
          this.HR_HED = BigDecimal.ZERO;
          this.HR_HEN = BigDecimal.ZERO;
          this.DAY_ATT = BigDecimal.ZERO;
          this.DAY_ATTB = BigDecimal.ZERO;
          this.HR_Message = ""; // Mensaje vacío por defecto
          this.HR_HT = BigDecimal.ZERO;
          this.HR_HC = BigDecimal.ZERO;
          this.HR_HLGT15 = BigDecimal.ZERO;
          this.HR_HLLT15 = BigDecimal.ZERO;
          this.HR_THL = BigDecimal.ZERO;
          this.HR_LTA = BigDecimal.ZERO;
          this.HR_EDE = BigDecimal.ZERO;
          this.HR_HER = BigDecimal.ZERO;
          this.HR_HEF = BigDecimal.ZERO;
    }

	/**
	 * @return the hR_Message
	 */
	public String getHR_Message() {
		return HR_Message;
	}
	/**
	 * @param p_hR_Message the hR_Message to set
	 */
	public void setHR_Message(String p_hR_Message) {
		HR_Message = p_hR_Message;
	}
	/**
	 * @return the hR_HND
	 */
	public BigDecimal getHR_HND() {
		return HR_HND;
	}
	/**
	 * @param p_hR_HND the hR_HND to set
	 */
	public void setHR_HND(BigDecimal p_hR_HND) {
		HR_HND = p_hR_HND;
	}
	/**
	 * @return the hR_HNN
	 */
	public BigDecimal getHR_HNN() {
		return HR_HNN;
	}
	/**
	 * @param p_hR_HNN the hR_HNN to set
	 */
	public void setHR_HNN(BigDecimal p_hR_HNN) {
		HR_HNN = p_hR_HNN;
	}
	/**
	 * @return the hR_HED
	 */
	public BigDecimal getHR_HED() {
		return HR_HED;
	}
	/**
	 * @param p_hR_HED the hR_HED to set
	 */
	public void setHR_HED(BigDecimal p_hR_HED) {
		HR_HED = p_hR_HED;
	}
	/**
	 * @return the hR_HEN
	 */
	public BigDecimal getHR_HEN() {
		return HR_HEN;
	}
	/**
	 * @param p_hR_HEN the hR_HEN to set
	 */
	public void setHR_HEN(BigDecimal p_hR_HEN) {
		HR_HEN = p_hR_HEN;
	}
	
	public BigDecimal getDAY_ATT() {
		return DAY_ATT;
	}
	
	public void setDAY_ATT(BigDecimal dAY_ATT) {
		DAY_ATT = dAY_ATT;
	}
	
	public BigDecimal getDAY_ATTB() {
		return DAY_ATTB;
	}
	
	public void setDAY_ATTB(BigDecimal dAY_ATTB) {
		DAY_ATTB = dAY_ATTB;
	}
	public BigDecimal getHR_HT() {
		return HR_HT;
	}
	public void setHR_HT(BigDecimal hR_HT) {
		HR_HT = hR_HT;
	}
	public BigDecimal getHR_HC() {
		return HR_HC;
	}
	public void setHR_HC(BigDecimal hR_HC) {
		HR_HC = hR_HC;
	}
	public BigDecimal getHR_HLGT15() {
		return HR_HLGT15;
	}
	public void setHR_HLGT15(BigDecimal hR_HLGT15) {
		HR_HLGT15 = hR_HLGT15;
	}
	public BigDecimal getHR_HLLT15() {
		return HR_HLLT15;
	}
	public void setHR_HLLT15(BigDecimal hR_HLLT15) {
		HR_HLLT15 = hR_HLLT15;
	}
	public BigDecimal getHR_THL() {
		return HR_THL;
	}
	public void setHR_THL(BigDecimal hR_THL) {
		HR_THL = hR_THL;
	}
	public BigDecimal getHR_LTA() {
		return HR_LTA;
	}
	public void setHR_LTA(BigDecimal hR_LTA) {
		HR_LTA = hR_LTA;
	}
	public BigDecimal getHR_EDE() {
		return HR_EDE;
	}
	public void setHR_EDE(BigDecimal hR_EDE) {
		HR_EDE = hR_EDE;
	}
	public BigDecimal getHR_HER() {
		return HR_HER;
	}
	public void setHR_HER(BigDecimal hR_HER) {
		HR_HER = hR_HER;
	}
	public BigDecimal getHR_HEF() {
		return HR_HEF;
	}
	public void setHR_HEF(BigDecimal hR_HEF) {
		HR_HEF = hR_HEF;
	}
	
}
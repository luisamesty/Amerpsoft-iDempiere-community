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
	
	public BigDecimal HR_HND;
	public BigDecimal HR_HNN;
	public BigDecimal HR_HED;
	public BigDecimal HR_HEN;
	public BigDecimal DAY_ATT;
	public BigDecimal DAY_ATTB;
	public String HR_Message;

	public AttendanceHours(BigDecimal HR_HND, BigDecimal HR_HNN, BigDecimal HR_HED, BigDecimal HR_HEN, BigDecimal DAY_ATT, BigDecimal DAY_ATTB, String HR_Message) {
        // TODO Auto-generated constructor stub
//		HR_HND = BigDecimal.valueOf(0);
//		HR_HNN = BigDecimal.valueOf(0);
//		HR_HED = BigDecimal.valueOf(0);
//		HR_HEN = BigDecimal.valueOf(0);
//		HR_Message="";
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
	
}
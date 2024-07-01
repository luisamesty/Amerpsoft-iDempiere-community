package org.amerp.amnutilities;

import java.math.BigDecimal;
import java.sql.Timestamp;

/*
 * LoanPeriods
 * 	int LoanCuotaNo
 *	String PeriodValue
 *	Timestamp PeriodDate
 *	BigDecimal CuotaAmount 
 */
public class LoanPeriods {
	
	public int LoanCuotaNo;
	public String PeriodValue;
	public Timestamp PeriodDate;
	public BigDecimal CuotaAmount ;
	
	public LoanPeriods() {
		this.LoanCuotaNo = 0;
		this.PeriodValue = "";
		this.PeriodDate  = null;
		this.CuotaAmount = BigDecimal.valueOf(0);
	}
	public LoanPeriods(int LoanCuotaNo, String PeriodValue, Timestamp PeriodDate,
			BigDecimal CuotaAmount) {
		this.LoanCuotaNo = LoanCuotaNo;
		this.PeriodValue = PeriodValue;
		this.PeriodDate  = PeriodDate;
		this.CuotaAmount = CuotaAmount;
		// TODO Auto-generated constructor stub
	}
	public int getLoanCuotaNo() {
		return LoanCuotaNo;
	}
	public void setLoanCuotaNo(int loanCuotaNo) {
		LoanCuotaNo = loanCuotaNo;
	}
	public String getPeriodValue() {
		return PeriodValue;
	}
	public void setPeriodValue(String periodValue) {
		PeriodValue = periodValue;
	}
	public Timestamp getPeriodDate() {
		return PeriodDate;
	}
	public void setPeriodDate(Timestamp periodDate) {
		PeriodDate = periodDate;
	}
	public BigDecimal getCuotaAmount() {
		return CuotaAmount;
	}
	public void setCuotaAmount(BigDecimal cuotaAmount) {
		CuotaAmount = cuotaAmount;
	}

	
}
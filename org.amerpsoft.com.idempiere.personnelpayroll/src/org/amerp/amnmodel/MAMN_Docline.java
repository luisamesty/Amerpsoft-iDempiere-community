package org.amerp.amnmodel;

import java.math.BigDecimal;

import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.model.PO;

public class MAMN_Docline extends DocLine {

	public MAMN_Docline(PO po, Doc doc) {
		super(po, doc);
		//
	}

	private String Concept_Value="";
	private String Concept_Name="";
	private int Concept_CalcOrder=0;
	// Currency
	private boolean IsOverrideCurrencyRate;
	private int C_Currency_ID_to;
	private int C_ConversionType_ID;
	private BigDecimal CurrencyRate;
	
	public String getConcept_Value() {
		return Concept_Value;
	}
	public void setConcept_Value(String concept_Value) {
		Concept_Value = concept_Value;
	}
	public String getConcept_Name() {
		return Concept_Name;
	}
	public void setConcept_Name(String concept_Name) {
		Concept_Name = concept_Name;
	}
	public int getConcept_CalcOrder() {
		return Concept_CalcOrder;
	}
	public void setConcept_CalcOrder(int concept_CalcOrder) {
		Concept_CalcOrder = concept_CalcOrder;
	}
	public boolean isIsOverrideCurrencyRate() {
		return IsOverrideCurrencyRate;
	}
	public void setIsOverrideCurrencyRate(boolean isOverrideCurrencyRate) {
		IsOverrideCurrencyRate = isOverrideCurrencyRate;
		p_po.set_Attribute("IsOverrideCurrencyRate", isOverrideCurrencyRate);
	}
	public Integer getC_Currency_ID_to() {
		return C_Currency_ID_to;
	}
	public void setC_Currency_ID_to(Integer m_Currency_ID_to) {
		this.C_Currency_ID_to = m_Currency_ID_to;
		p_po.set_Attribute("C_Currency_ID_to", m_Currency_ID_to);
	}
	public int getC_ConversionType_ID() {
		return C_ConversionType_ID;
	}
	public void setC_ConversionType_ID(Integer c_ConversionType_ID) {
		C_ConversionType_ID = c_ConversionType_ID;
		p_po.set_Attribute("C_ConversionType_ID", c_ConversionType_ID);
	}
	public BigDecimal getCurrencyRate() {
		return CurrencyRate;
	}
	public void setCurrencyRate(BigDecimal currencyRate) {
		CurrencyRate = currencyRate;
		p_po.set_Attribute("CurrencyRate", currencyRate);
	}
	
}

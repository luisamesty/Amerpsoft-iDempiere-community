package org.amerp.amnmodel;

import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.model.PO;

public class MAMN_Docline extends DocLine {

	public MAMN_Docline(PO po, Doc doc) {
		super(po, doc);
		// TODO Auto-generated constructor stub
	}

	private String Concept_Value="";
	private String Concept_Name="";
	private int Concept_CalcOrder=0;
	
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
	
}

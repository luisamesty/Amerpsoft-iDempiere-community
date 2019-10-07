package org.amerp.amfmodel;

import org.compiere.acct.Doc;
import org.compiere.acct.DocLine;
import org.compiere.acct.DocLine_Allocation;
import org.compiere.model.MAllocationLine;
import org.compiere.model.PO;

public class MAMF_DocLine_Allocation extends DocLine_Allocation {


	public MAMF_DocLine_Allocation(MAllocationLine line, Doc doc) {
		super(line, doc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 	Set C_ConversionType_ID
	 *	@param C_ConversionType_ID id
	 */
	public void setC_ConversionType_ID(int C_ConversionType_ID)
	{
		super.setC_ConversionType_ID(C_ConversionType_ID);
	}	//	setC_ConversionType_ID
	
	/**
	 * 	Set C_BPartner_ID
	 *	@param C_BPartner_ID id
	 */
	protected void setC_BPartner_ID (int C_BPartner_ID)
	{
		super.setC_BPartner_ID(C_BPartner_ID);
	}	//	setC_BPartner_ID
	

}

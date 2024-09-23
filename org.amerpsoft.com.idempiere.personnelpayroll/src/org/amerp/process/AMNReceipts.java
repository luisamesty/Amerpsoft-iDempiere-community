package org.amerp.process;


/**
 * AMNReceipts class
 * @author luisamesty
 *
 */
class AMNReceipts {

	public int AMN_Payroll_ID;
	public String DocumentNo;
	public String Description;
	public int AMN_Employee_ID;
	public String Employee_Value;
	public String Employee_Name;
	public int NoLines;
	public boolean RecIsPosted;
	public String Error;
	// Invoice Added
	public boolean InvoiceIsCreated;
	public int C_Invoice_ID;
	public int C_DocType_ID;
	public int AD_Org_ID;
	public int C_BPartner_ID;
	public int C_Location_ID;
	
	
	public AMNReceipts() {
		super();
		AMN_Payroll_ID = 0;
		DocumentNo = "";
		Description = "";
		AMN_Employee_ID=0;
		Employee_Value = "";
		Employee_Name = "";
		NoLines = 0;
		RecIsPosted = false;
		Error = "";
		// Invoice Added
		InvoiceIsCreated = false;
		C_Invoice_ID=0;
		C_DocType_ID=0;
		AD_Org_ID=0;
		C_BPartner_ID=0;
		C_Location_ID=0;
	}
	
	public AMNReceipts(int aMN_Payroll_ID, String documentNo, String description, String employee_Value,
			String employee_Name, int noLines, boolean IsPosted, String error) {
		super();
		AMN_Payroll_ID = aMN_Payroll_ID;
		DocumentNo = documentNo;
		Description = description;
		Employee_Value = employee_Value;
		Employee_Name = employee_Name;
		NoLines = noLines;
		RecIsPosted = IsPosted;
		Error = error;
	}
	
	public AMNReceipts(int aMN_Payroll_ID, String documentNo, String description, String employee_Value,
			String employee_Name, int noLines, boolean IsPosted, String error, 
			boolean invoiceIsCreated, int c_Invoice_ID,
			int c_DocType_ID, int aD_Org_ID, int c_BPartner_ID, int c_Location_ID) {
		super();
		AMN_Payroll_ID = aMN_Payroll_ID;
		DocumentNo = documentNo;
		Description = description;
		Employee_Value = employee_Value;
		Employee_Name = employee_Name;
		NoLines = noLines;
		RecIsPosted = IsPosted;
		Error = error;
		// Invoice Added
		InvoiceIsCreated = invoiceIsCreated;
		C_Invoice_ID=c_Invoice_ID;
		C_DocType_ID=c_DocType_ID;
		AD_Org_ID=aD_Org_ID;
		C_BPartner_ID=c_BPartner_ID;
		C_Location_ID=c_Location_ID;
	}
	
	public int getAMN_Payroll_ID() {
		return AMN_Payroll_ID;
	}
	public void setAMN_Payroll_ID(int aMN_Payroll_ID) {
		AMN_Payroll_ID = aMN_Payroll_ID;
	}
	public String getDocumentNo() {
		return DocumentNo;
	}
	public void setDocumentNo(String documentNo) {
		DocumentNo = documentNo;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getAMN_Employee_ID() {
		return AMN_Employee_ID;
	}
	public void setAMN_Employee_ID(int aMN_Employee_ID) {
		AMN_Employee_ID = aMN_Employee_ID;
	}

	public String getEmployee_Value() {
		return Employee_Value;
	}
	public void setEmployee_Value(String employee_Value) {
		Employee_Value = employee_Value;
	}
	public String getEmployee_Name() {
		return Employee_Name;
	}
	public void setEmployee_Name(String employee_Name) {
		Employee_Name = employee_Name;
	}
	public int getNoLines() {
		return NoLines;
	}
	public void setNoLines(int noLines) {
		NoLines = noLines;
	}
	public boolean getRecIsPosted() {
		return RecIsPosted;
	}
	public void setRecIsPosted(boolean isPosted) {
		RecIsPosted = isPosted;
	}
	public String getError() {
		return Error;
	}
	public void setError(String error) {
		Error = error;
	}
	// Invoice Added

	public int getC_Invoice_ID() {
		return C_Invoice_ID;
	}

	public void setC_Invoice_ID(int c_Invoice_ID) {
		C_Invoice_ID = c_Invoice_ID;
	}

	public int getC_DocType_ID() {
		return C_DocType_ID;
	}

	public void setC_DocType_ID(int c_DocType_ID) {
		C_DocType_ID = c_DocType_ID;
	}

	public int getAD_Org_ID() {
		return AD_Org_ID;
	}

	public void setAD_Org_ID(int aD_Org_ID) {
		AD_Org_ID = aD_Org_ID;
	}

	public int getC_BPartner_ID() {
		return C_BPartner_ID;
	}

	public void setC_BPartner_ID(int c_BPartner_ID) {
		C_BPartner_ID = c_BPartner_ID;
	}

	public int getC_Location_ID() {
		return C_Location_ID;
	}

	public void setC_Location_ID(int c_Location_ID) {
		C_Location_ID = c_Location_ID;
	}
	
}
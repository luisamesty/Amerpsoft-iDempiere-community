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
}
UPDATE AD_Table
SET IsChangeLog = 'Y'
WHERE TableName IN ('AMN_Employee', 'AMN_Payroll', 'AMN_Payroll_Detail');
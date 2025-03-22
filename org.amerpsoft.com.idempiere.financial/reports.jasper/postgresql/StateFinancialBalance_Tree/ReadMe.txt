Application Dictinary Changes:
Enable RetainedEarnings_Acct and IncomeSummary_Acct From Account Schema GL
Window: Accounting Schema  Tab_ General Ledger-
Table: C_AcctSchema_GL   
Fields:  RetainedEarnings_Acct and IncomeSummary_Acct
Mandatory OFF in order to Make the changes on Table.
Finally:
Fill correnct values from general ledger Accounting Plan

Add Aprameter on Process  (C_ElementValue_ID)
Put Default Value:
@SQL=SELECT  account_id FROM C_AcctSchema_GL accsch LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = accsch.IncomeSummary_Acct  LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID) WHERE accsch.AD_Client_ID=@AD_Client_ID@ AND accsch.C_acctSchema_ID=@C_AcctSchema_ID@
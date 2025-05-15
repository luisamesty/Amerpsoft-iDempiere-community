SELECT  
account_id 
FROM C_AcctSchema_GL accsch 
LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = accsch.IncomeSummary_Acct  
LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID) 
WHERE accsch.AD_Client_ID=1000000 AND accsch.C_acctSchema_ID=1000000
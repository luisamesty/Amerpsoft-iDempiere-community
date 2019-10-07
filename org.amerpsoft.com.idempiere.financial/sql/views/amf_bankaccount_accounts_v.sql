-- View: amf_bankaccount_accounts_v

-- DROP VIEW amf_bankaccount_accounts_v;

CREATE OR REPLACE VIEW amf_bankaccount_accounts_v AS 

SELECT DISTINCT ON (AD_Client_ID, C_ElementValue_ID)
AD_Client_ID, C_BankAccount_ID, C_ElementValue_ID, Value, Name
FROM (
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_intransit_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_asset_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_expense_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_interestrev_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_interestexp_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_unidentified_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_unallocatedcash_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)	
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_paymentselect_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)	
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_settlementgain_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)	
	UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_settlementloss_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)	
		UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_revaluationgain_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)	
		UNION
	SELECT 
	cbana.AD_Client_ID, cbana.C_BankAccount_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_BankAccount cbana
	LEFT JOIN C_BankAccount_Acct cbacu ON cbacu.C_BankAccount_ID = cbana.C_BankAccount_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbacu.b_revaluationloss_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)	
) as ba_acct
;
ALTER TABLE amf_bankaccount_accounts_v
  OWNER TO postgres;
-- View: amf_bpartner_accounts_v

-- DROP VIEW amf_bpartner_accounts_v;

CREATE OR REPLACE VIEW amf_bpartner_accounts_v AS 
SELECT DISTINCT ON (AD_Client_ID, C_ElementValue_ID)
AD_Client_ID, C_Bpartner_ID, C_ElementValue_ID, Value, Name
FROM (
	SELECT 
	cbpac.AD_Client_ID, cbpac.C_Bpartner_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_Bpartner cbpac
	LEFT JOIN C_BP_Customer_Acct cbpcu ON cbpcu.C_Bpartner_ID = cbpac.C_Bpartner_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbpcu.c_receivable_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbpac.AD_Client_ID, cbpac.C_Bpartner_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_Bpartner cbpac
	LEFT JOIN C_BP_Customer_Acct cbpcu ON cbpcu.C_Bpartner_ID = cbpac.C_Bpartner_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbpcu.c_prepayment_acct 
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbpac.AD_Client_ID, cbpac.C_Bpartner_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_Bpartner cbpac
	LEFT JOIN C_BP_Customer_Acct cbpcu ON cbpcu.C_Bpartner_ID = cbpac.C_Bpartner_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbpcu.c_receivable_services_acct
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)

	UNION

	SELECT 
	cbpac.AD_Client_ID, cbpac.C_Bpartner_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_Bpartner cbpac
	LEFT JOIN C_BP_Vendor_Acct cbpve ON cbpve.C_Bpartner_ID = cbpac.C_Bpartner_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbpve.v_liability_acct
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbpac.AD_Client_ID, cbpac.C_Bpartner_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_Bpartner cbpac
	LEFT JOIN C_BP_Vendor_Acct cbpve ON cbpve.C_Bpartner_ID = cbpac.C_Bpartner_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbpve.v_liability_services_acct
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	UNION
	SELECT 
	cbpac.AD_Client_ID, cbpac.C_Bpartner_ID, cev.Value, cev.Name, account_id as C_ElementValue_ID
	FROM C_Bpartner cbpac
	LEFT JOIN C_BP_Vendor_Acct cbpve ON cbpve.C_Bpartner_ID = cbpac.C_Bpartner_ID
	LEFT JOIN C_ValidCombination cvaco ON cvaco.C_ValidCombination_ID = cbpve.v_prepayment_acct
	LEFT JOIN C_ElementValue cev ON ( cev.C_ElementValue_ID = cvaco.Account_ID)
	) as bp_acct
;

ALTER TABLE amf_bpartner_accounts_v
  OWNER TO postgres;
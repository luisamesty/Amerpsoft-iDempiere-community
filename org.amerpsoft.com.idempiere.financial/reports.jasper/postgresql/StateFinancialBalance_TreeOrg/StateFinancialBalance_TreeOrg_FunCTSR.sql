-- StateFinancialBalance_TreeOrg_FunCTSR
-- SUBREPORT 
-- AD_Clien_ID =1000000
-- AD_Org_ID = 0  (1000004)
-- AD_OrgParent_ID (1000000)
-- C_AcctSchema_ID = 10000000
-- C_Period_ID = 1000072
-- PostingType = 'A'
-- C_ElementValue_ID = 10000000
-- DateFrom '2025-01-01'
-- DateTo '2025-01-31'
SELECT bal1.ad_org_id AS ad_org_id, 
	org1.org_value AS org_value,
	org1.org_name  AS org_name,
	eve1.c_elementvalue_id,
	eve1.codigo, eve1.name, 
	eve1.issummary, eve1.accounttype,
    COALESCE(openbalance,0) AS openbalance, 
	COALESCE(amtacctdr,0) AS amtacctdr, 
	COALESCE(amtacctcr,0) AS amtacctcr, 
	COALESCE(closebalance,0) AS closebalance, 
	COALESCE(amtacctsa,0) AS amtacctsa
FROM amf_element_value_tree_basic($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve1
INNER JOIN adempiere.amf_balance_account_org_flex_orgparent(
	$P{AD_Client_ID}, $P{AD_OrgParent_ID} , $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, 
	$P{PostingType}, $P{SR_ElementValue_ID}, null, null) AS bal1 ON bal1.bal_C_ElementValue_ID = eve1.C_ElementValue_ID
INNER JOIN amf_org_tree($P{AD_Client_ID}, $P{AD_Org_ID}, $P{AD_OrgParent_ID}) AS org1 ON org1.org_ad_org_id = bal1.ad_org_id
WHERE CASE WHEN ( $P{SR_ElementValue_ID} IS NULL OR $P{SR_ElementValue_ID} = 0 OR eve1.c_elementvalue_id= $P{SR_ElementValue_ID} ) THEN 1=1 ELSE 1=0 END 
ORDER BY eve1.codigo, bal1.ad_org_id ;
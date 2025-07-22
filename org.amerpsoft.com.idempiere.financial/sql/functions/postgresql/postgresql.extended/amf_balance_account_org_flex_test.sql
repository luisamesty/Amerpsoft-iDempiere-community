
-- AD_Clien_ID =1000000
-- AD_Org_ID = 1000004
-- C_AcctSchema_ID = 10000000
-- C_Period_ID = 1000072
-- PostingType = 'A'
-- C_ElementValue_ID = 10000000
-- DateFrom '2025-01-01'
-- DateTo '2025-01-31'
SELECT * FROM amf_balance_account_org_flex($P{AD_Client_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, $P{C_ElementValue_ID}, $P{DateFrom}, $P{DateTo} )
ORDER BY ad_org_id ;

select * from amf_org_tree($P{AD_Client_ID},$P{AD_Org_ID},$P{AD_OrgParent_ID});
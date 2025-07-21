-- Pruebas de funciones
-- amf_balance_account_org_test.sql
-- Para una sola cuenta:

-- AD_Client_ID = 1000000
-- AD_Org_ID = 0  (1000004)
-- AD_OrgParent_ID (1000000)
select * from amf_org_tree($P{AD_Client_ID},$P{AD_Org_ID},$P{AD_OrgParent_ID});

-- AD_Clien_ID =1000000
-- AD_Org_ID = 1000004
-- C_AcctSchema_ID = 10000000
-- C_Period_ID = 1000072
-- PostingType = 'A'
-- C_ElementValue_ID = 10000000
SELECT * FROM amf_balance_account_org($P{AD_Client_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, $P{C_ElementValue_ID});

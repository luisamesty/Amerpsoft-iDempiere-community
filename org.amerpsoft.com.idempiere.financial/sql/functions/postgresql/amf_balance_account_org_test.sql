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


-- Matriz de Cuentas y Organizaciones en Arbol con el Saldo
-- Balance
select
	org.org_ad_org_id, org.org_value, org.org_name,
	eve.value, eve.name, eve.description, eve.length, eve.accounttype, eve.accountsign,
	bal.openbalance, bal.amtacctdr, bal.amtacctcr , bal.closebalance, bal.amtacctsa,
    eve.level, eve.node_id, eve.parent_id, eve.c_element_id, eve.c_elementvalue_id,
    eve.ad_client_id, eve.ad_org_id, eve.isactive, 
    eve.isdoccontrolled, eve.issummary, eve.acctparent, eve.pathel, eve.ancestry,
    eve.codigo0, eve.name0, eve.description0, eve.issummary0,
    eve.codigo1, eve.name1, eve.description1, eve.issummary1,
    eve.codigo2, eve.name2, eve.description2, eve.issummary2,
    eve.codigo3, eve.name3, eve.description3, eve.issummary3,
    eve.codigo4, eve.name4, eve.description4, eve.issummary4,
    eve.codigo5, eve.name5, eve.description5, eve.issummary5,
    eve.codigo6, eve.name6, eve.description6, eve.issummary6,
    eve.codigo7, eve.name7, eve.description7, eve.issummary7,
    eve.codigo8, eve.name8, eve.description8, eve.issummary8,
    eve.codigo9, eve.name9, eve.description9, eve.issummary9
FROM amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve
LEFT join amf_org_tree($P{AD_Client_ID},$P{AD_Org_ID},$P{AD_OrgParent_ID}) as org on org.org_ad_client_id = eve.ad_client_id
LEFT join amf_balance_account_org($P{AD_Client_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, $P{C_ElementValue_ID}) 
as bal on bal.c_elementvalue_id = eve.c_elementvalue_id and bal.ad_org_id = org.org_ad_org_id
WHERE eve.isSummary='N'
AND CASE WHEN  $P{isShowZERO} = 'Y' OR ( $P{isShowZERO} = 'N' AND ( bal.openbalance<> 0 or bal.amtacctcr<> 0 or bal.amtacctdr <>0 or bal.closebalance <> 0  ) ) THEN 1=1  ELSE 1=0 END
ORDER BY codigo0 ASC, codigo1 ASC, codigo2 ASC, codigo3 ASC, codigo4 ASC, codigo5 ASC, codigo6 ASC, codigo7 ASC, codigo8 ASC,  codigo9 ASC, value ASC, org_value ASC
;

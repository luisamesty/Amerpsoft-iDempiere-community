-- Pruebas de funciones
-- amf_org_tree
-- amf_org_tree_pruebas.sql

-- AD_Client_ID = 1000000
-- AD_Org_ID = 0  (1000004)
-- AD_OrgParent_ID (1000000)
select * from amf_org_tree($P{AD_Client_ID},$P{AD_Org_ID},$P{AD_OrgParent_ID});


-- ACCOUNT ELEMENTS
-- FROM DEfault AD_Tree FROM A GIVEN AD_Client_ID and C_AcctSchema_ID
-- FOR NEW ACCOUNTING REPORTS
-- Version 4 simplificada con funcion amf_element_value_tree_basic
-- 
SELECT DISTINCT ON (EVB.codigo)
    EVB.level,
    EVB.node_id AS "nodeId",
    EVB.parent_id AS "parentId",
    CLI.name AS cliname,
    COALESCE(CLI.description, CLI.name, '') AS clidescription,
    IMG.binarydata AS cli_logo,
    EVB.c_element_id AS "cElementId",
    EVB. c_elementvalue_id AS "cElementValueId",
    EVB.ad_client_id AS "adClientId",
    NULL AS ad_org_id,
    EVB.isactive AS "isActive",
    EVB.codigo,
    EVB.name,
    EVB.description,
    EVB.length,
    EVB.accounttype AS "accountType",
    EVB.accountsign AS "accountSign",
    EVB.isdoccontrolled AS "isDocControlled",
    EVB. element_c_element_id AS "elementCElementId",
    EVB.issummary AS "isSummary",
    COALESCE(ELVP.value, '') AS value_parent
FROM adempiere.amf_element_value_tree_basic($P{AD_Client_ID}, $P{C_AcctSchema_ID}) EVB
LEFT JOIN AD_Client CLI ON CLI.ad_client_id = EVB.ad_client_id
LEFT JOIN AD_ClientInfo CLF ON CLI.ad_client_id = CLF.ad_client_id
LEFT JOIN AD_Image IMG ON CLF.logoreport_id = IMG.ad_image_id
LEFT JOIN C_ElementValue ELVP ON ELVP.C_ElementValue_ID = EVB.parent_id
ORDER BY EVB.codigo, EVB.ancestry;

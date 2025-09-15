-- StateFinancialIntegralResults_TreeOrg_Fun V2 usando Funciones (isShowOrganization)
-- OrgTree Version4
-- ORG_AccountElement_Tree_V5.sql
-- OrgTreeMaster V5 con parámetros AD_OrgParent_ID y AD_Org_ID
-- AccountType IN ('R','E','M')
-- Parámetro PositiveBalance
-- isShowOrganization
SELECT *
FROM (
	-- Encabezado del Reportes Contabilidad
	SELECT DISTINCT ON (cli.ad_client_id)
		cli.ad_client_id AS rep_client_id,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 0 ELSE $P{AD_Org_ID} END AS rep_org_id,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0)
			THEN concat(COALESCE(cli.name, cli.value), ' - Consolidado')
			ELSE COALESCE(orgh.name, orgh.value, '') END AS rep_name,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0)
			THEN concat(COALESCE(cli.description, cli.name), ' - Consolidado')
			ELSE COALESCE(orgh.description, orgh.name, orgh.value, '') END AS rep_description,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0)
			THEN '' ELSE COALESCE(orginfo.taxid, '') END AS rep_taxid,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0)
			THEN img1.binarydata ELSE img2.binarydata END AS rep_logo,
		CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
		posttype.value as postingtype_value,
		posttype.name as postingtype_name,
		1 AS imp_header
	FROM adempiere.ad_client cli
	INNER JOIN adempiere.ad_org orgh ON orgh.ad_client_id = cli.ad_client_id
	INNER JOIN adempiere.ad_clientinfo cliinfo ON cli.ad_client_id = cliinfo.ad_client_id
	LEFT JOIN adempiere.ad_image img1 ON cliinfo.logoreport_id = img1.ad_image_id
	INNER JOIN adempiere.ad_orginfo orginfo ON orgh.ad_org_id = orginfo.ad_org_id
	LEFT JOIN adempiere.ad_image img2 ON orginfo.logo_id = img2.ad_image_id
	LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = $P{C_AcctSchema_ID})
	LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
	LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	LEFT JOIN (
		SELECT DISTINCT ON (rfl.value) rfl.value, COALESCE(rflt.name,rfl.name,'') as name FROM AD_Reference as rfr
		LEFT JOIN AD_Ref_List as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		LEFT JOIN AD_Ref_List_Trl as rflt ON (rflt.ad_ref_list_id = rfl.ad_ref_list_id AND rflt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}))
		WHERE rfr.name like '%Posting Type%'
	) as posttype ON (posttype.value= $P{PostingType} )
	WHERE cli.ad_client_id = $P{AD_Client_ID}
	  AND ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR orgh.ad_org_id = $P{AD_Org_ID})
) AS header_info
FULL JOIN (
	-- Balance extendido
	SELECT 
	-- eve1 (function:amf_element_value_tree_extended)
    c_elementvalue_id,
    ad_client_id,
    isactive,
    codigo,
    name,
    description,
    length,
    'N' as issummary,
    accounttype,
    accountsign,
    isdoccontrolled,
--    issummary,
    acctparent,
    codigo0, name0, description0, issummary0,
    codigo1, name1, description1, issummary1,
    codigo2, name2, description2, issummary2,
    codigo3, name3, description3, issummary3,
    codigo4, name4, description4, issummary4,
    codigo5, name5, description5, issummary5,
    codigo6, name6, description6, issummary6,
    codigo7, name7, description7, issummary7,
    codigo8, name8, description8, issummary8,
    codigo9, name9, description9, issummary9,
	-- org (function:amf_org_tree) org_id
    CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID}=0) AND $P{isShowOrganization} IS NOT NULL AND $P{isShowOrganization} = 'N' THEN 0 ELSE ad_org_id END AS ad_org_id, 
    -- org (function:amf_org_tree)
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID}=0) AND $P{isShowOrganization} IS NOT NULL AND $P{isShowOrganization} = 'N' THEN 0 ELSE org_ad_org_id END AS org_ad_org_id, 
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID}=0) AND $P{isShowOrganization} IS NOT NULL AND $P{isShowOrganization} = 'N' THEN 'ALL' ELSE org_value END AS org_value,
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID}=0) AND $P{isShowOrganization} IS NOT NULL AND $P{isShowOrganization} = 'N' THEN 'Todas las Organizaciones' ELSE org_name END AS org_name, 
	all_orgs,
	-- bal1 (function:amf_balance_account_org_flex)
	bal_c_elementvalue_id,
	dateini, dateend, 
	 COALESCE(openbalance,0) AS openbalance, 
	 COALESCE(amtacctdr,0) AS amtacctdr, 
	 COALESCE(amtacctcr,0) AS amtacctcr, 
	 COALESCE(closebalance,0) AS closebalance, 
	 COALESCE(amtacctsa,0) AS amtacctsa,
	1 AS imp_balance
	FROM (
		SELECT * 
			FROM amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve1
			LEFT JOIN amf_org_tree($P{AD_Client_ID}, $P{AD_Org_ID}, $P{AD_OrgParent_ID}) AS org1 ON org1.org_ad_client_id = eve1.ad_client_id
			LEFT JOIN amf_balance_account_org_flex_orgparent($P{AD_Client_ID}, $P{AD_OrgParent_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, NULL, NULL, NULL )
		    	AS bal1 ON bal1.bal_c_elementvalue_id = eve1.c_elementvalue_id AND bal1.ad_org_id = org1.org_ad_org_id
			WHERE eve1.issummary = 'N' AND eve1.AccountType IN ('R','E','M') AND ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
							COALESCE(bal1.openbalance, 0) <> 0
							OR COALESCE(bal1.amtacctdr, 0) <> 0
							OR COALESCE(bal1.amtacctcr, 0) <> 0
							OR COALESCE(bal1.closebalance, 0) <> 0 )))
	) AS bal
) AS balances ON 1=0
WHERE header_info.imp_header = 1 OR 
	(ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR balances.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY 
	balances.codigo0, balances.codigo1, balances.codigo2,
	balances.codigo3, balances.codigo4, balances.codigo5,
	balances.codigo6, balances.codigo7, balances.codigo8, balances.codigo9,
	balances.codigo, balances.org_value;

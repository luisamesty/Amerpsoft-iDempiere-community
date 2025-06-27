-- TrialBalance_Tree V9 usando Funciones
-- OrgTree Version4
-- Matriz de Cuentas y Organizaciones en Arbol con el Saldo
-- Balance
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
	*,
	1 AS imp_balance
	FROM (
		SELECT * 
			FROM amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve1
			LEFT JOIN amf_org_tree($P{AD_Client_ID}, $P{AD_Org_ID}, $P{AD_OrgParent_ID}) AS org1 ON org1.org_ad_client_id = eve1.ad_client_id
			LEFT JOIN amf_balance_account_org($P{AD_Client_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, NULL)
				AS bal1 ON bal1.c_elementvalue_id = eve1.c_elementvalue_id AND bal1.ad_org_id = org1.org_ad_org_id
			WHERE eve1.issummary = 'N' AND ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
							COALESCE(bal1.openbalance, 0) <> 0
							OR COALESCE(bal1.amtacctdr, 0) <> 0
							OR COALESCE(bal1.amtacctcr, 0) <> 0
							OR COALESCE(bal1.closebalance, 0) <> 0 )))
			UNION ALL				
			SELECT * 
			FROM amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve2
			LEFT JOIN amf_org_tree($P{AD_Client_ID}, $P{AD_Org_ID}, $P{AD_OrgParent_ID}) AS org2 ON org2.org_ad_client_id = eve2.ad_client_id
			LEFT JOIN amf_balance_account_org($P{AD_Client_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, $P{C_ElementValue_ID})
				AS bal2 ON bal2.c_elementvalue_id = eve2.c_elementvalue_id AND bal2.ad_org_id = org2.org_ad_org_id
			WHERE 
			CASE WHEN ($P{C_ElementValue_ID} IS NOT NULL AND $P{C_ElementValue_ID} = eve2.c_elementvalue_id )  THEN 1=1 ELSE 1=0 END
			AND eve2.issummary = 'N' 
	) AS bal
	
) AS balances ON 1=0
WHERE header_info.imp_header = 1 OR 
	(ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY 
	balances.codigo0, balances.codigo1, balances.codigo2,
	balances.codigo3, balances.codigo4, balances.codigo5,
	balances.codigo6, balances.codigo7, balances.codigo8, balances.codigo9,
	balances.codigo, balances.org_value;

-- AnaliticFinacialState_TreeOrg V3
-- With Functions 
-- One Account
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
        bal.*,
        COALESCE(fasmov.description_mov,'') as description_mov,
		to_char(fasmov.dateacct_mov,'DD-MM-YYYY') as dateacct_mov,
		to_char(fasmov.dateacct_mov,'YYYY-MM-DD') as dateacct_order,
		COALESCE(fasmov.documentno,'') as documentno,
		COALESCE(fasmov.printnamel,'') as printnamel, 
		COALESCE(fasmov.docbasetype,'') as docbasetype,
		COALESCE(fasmov.description_tbl,'') as description_tbl,
		COALESCE(fasmov.amtacctdr,0) as amtacctdr_mov,
		COALESCE(fasmov.amtacctcr,0) as amtacctcr_mov,
		fasmov.bpartner_value, fasmov.bpartner_name, fasmov.prod_value, fasmov.prod_name,
		CASE WHEN $P{C_ElementValue1_ID} IS NULL AND $P{C_ElementValue2_ID} IS NULL THEN 'Y' ELSE 'N' END as shown_resumme,
        1 AS imp_balance
	FROM (
		SELECT eve1.*, org1.*, bal1.*,
		CASE WHEN $P{C_ElementValue_ID} IS NOT NULL THEN eve1.codigo ELSE '' END AS valueacct,
		ce1.valueacct1, ce2.valueacct2		
		FROM amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve1
		LEFT JOIN amf_org_tree($P{AD_Client_ID}, $P{AD_Org_ID}, $P{AD_OrgParent_ID}) AS org1 ON org1.org_ad_client_id = eve1.ad_client_id
		LEFT JOIN amf_balance_account_org_flex($P{AD_Client_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, NULL, $P{DateFrom}, $P{DateTo} )
		    AS bal1 ON bal1.bal_c_elementvalue_id = eve1.c_elementvalue_id AND bal1.ad_org_id = org1.org_ad_org_id
		LEFT JOIN (
			SELECT ad_client_id, value AS valueacct1 FROM C_ElementValue 
			WHERE ad_client_id = $P{AD_Client_ID} AND issummary='N' AND ( $P{C_ElementValue1_ID} IS NULL OR C_ElementValue_ID = $P{C_ElementValue1_ID} )
			ORDER BY value ASC 
			FETCH FIRST 1 ROWS ONLY
		) ce1 ON (eve1.ad_client_id = ce1.ad_client_id )
		LEFT JOIN (
			SELECT ad_client_id, value AS valueacct2 FROM C_ElementValue 
			WHERE ad_client_id = $P{AD_Client_ID} AND issummary='N' AND ( $P{C_ElementValue2_ID} IS NULL OR C_ElementValue_ID = $P{C_ElementValue2_ID} )
			ORDER BY value DESC 
			FETCH FIRST 1 ROWS ONLY
		) ce2 ON (eve1.ad_client_id = ce2.ad_client_id )
		WHERE eve1.codigo >= ce1.valueacct1 AND eve1.codigo <= valueacct2 
				AND eve1.issummary = 'N' AND ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' 
				AND (	COALESCE(bal1.openbalance, 0) <> 0
						OR COALESCE(bal1.amtacctdr, 0) <> 0
						OR COALESCE(bal1.amtacctcr, 0) <> 0
						OR COALESCE(bal1.closebalance, 0) <> 0 )))
			AND CASE WHEN ($P{C_ElementValue_ID} IS NOT NULL AND $P{C_ElementValue_ID} = eve1.c_elementvalue_id )  THEN 1=1 ELSE 1=0 END
	) AS bal
	LEFT JOIN (
		SELECT
		fas2.Movements, fas2.ad_client_id, fas2.ad_org_id, fas2.c_period_id, fas2.c_acctschema_id, fas2.account_id, fas2.c_bpartner_id, fas2.m_product_id, 
		fas2.C_SalesRegion_ID, fas2.C_Campaign_ID,
		fas2.documentno, fas2.c_doctype_id, fas2.docbasetype, fas2.printnamel, fas2.amtacctdr, fas2.amtacctcr,
		fas2.dateacct_mov, fas2.description_mov, fas2.description_tbl,
		fas2.bpartner_value, fas2.bpartner_name, fas2.prod_value, fas2.prod_name
		FROM (
			SELECT
			'1' as Movements,  -- 0 Balance 1 Movements
			fac.ad_client_id, 
			fac.ad_org_id,
			fac.c_period_id, 
			fac.c_acctschema_id, 
			fac.account_id,
			-- *******************************************
			-- Parametro Tipo de resumen
			-- 'T' Resumido por Tipo de Documento 
			-- 'N' Resumido por Número de Documento 
			-- 'D' Resumido por Día + Tipo de Documento
			-- 'X' Sin Resumir
			-- *******************************************
			CASE 	WHEN $P{SummaryType} = 'X' THEN fac.c_bpartner_id ELSE 0 END AS c_bpartner_id,
			CASE 	WHEN $P{SummaryType} = 'X' THEN fac.m_product_id ELSE 0 END AS m_product_id,
			CASE 	WHEN $P{SummaryType} = 'X' THEN fac.C_Project_ID ELSE 0 END AS C_Project_ID,
			CASE 	WHEN $P{SummaryType} = 'X' THEN fac.C_SalesRegion_ID ELSE 0 END AS C_SalesRegion_ID,
			CASE 	WHEN $P{SummaryType} = 'X' THEN fac.C_Activity_ID ELSE 0 END as C_Activity_ID,
			CASE 	WHEN $P{SummaryType} = 'X' THEN fac.C_Campaign_ID ELSE 0 END AS C_Campaign_ID,
			-- documentno
			CASE 	WHEN $P{SummaryType} = 'T' THEN '00000000'
				WHEN $P{SummaryType} = 'N' THEN TRIM(fac.documentno) 
				WHEN $P{SummaryType} = 'D' THEN '00000000' 
				ELSE TRIM(fac.documentno) END AS documentno,
			-- c_doctype_id
			CASE 	WHEN $P{SummaryType} = 'T' THEN fac.c_doctype_id
				WHEN $P{SummaryType} = 'N' THEN fac.c_doctype_id 
				WHEN $P{SummaryType} = 'D' THEN 0 
				ELSE fac.c_doctype_id END c_doctype_id,
			-- docbasetype
			CASE	WHEN fac.c_doctype_id = 9999999999 THEN 'CBS' -- C_BankStatement
					WHEN fac.c_doctype_id = 9999999998 THEN 'CAS' -- C_Cash
					WHEN fac.c_doctype_id = 9999999997 THEN 'CAL' -- C_AllocationHdr
					WHEN fac.c_doctype_id = 9999999996 THEN 'PRO' -- M_Production
					WHEN fac.c_doctype_id = 9999999995 THEN 'MCH' -- M_MatchInv
					WHEN fac.c_doctype_id = 9999999994 THEN 'MPO' -- M_MatchPO
					WHEN fac.c_doctype_id > 0 AND fac.c_doctype_id < 9999999990 THEN COALESCE(doc.docbasetype,'')
					ELSE '' END docbasetype,
			-- c_doctype_trl.printname as printnamel,
			CASE 	WHEN $P{SummaryType} = 'T' THEN '00000000'
				WHEN $P{SummaryType} = 'N' THEN COALESCE(doct.printname,'') 
				WHEN $P{SummaryType} = 'D' THEN '00000000' 
				ELSE COALESCE(doct.printname,'') END AS printnamel,
			-- amtacctdr
			CASE 	WHEN $P{SummaryType} = 'T' THEN sum(fac.amtacctdr) 
				WHEN $P{SummaryType} = 'N' THEN sum(fac.amtacctdr)
				WHEN $P{SummaryType} = 'D' THEN sum(fac.amtacctdr)
				ELSE fac.amtacctdr END AS amtacctdr,
			-- amtacctcr
			CASE 	WHEN $P{SummaryType} = 'T' THEN sum(fac.amtacctcr) 
				WHEN $P{SummaryType} = 'N' THEN sum(fac.amtacctcr)
				WHEN $P{SummaryType} = 'D' THEN sum(fac.amtacctcr)
				ELSE fac.amtacctcr END AS amtacctcr,
			-- dateacct
			CASE 	WHEN $P{SummaryType} = 'T' THEN date_trunc('month',fac.dateacct)+'1month'::interval-'1day'::interval 
				WHEN $P{SummaryType} = 'N' THEN fac.dateacct 
				WHEN $P{SummaryType} = 'D' THEN fac.dateacct
				ELSE fac.dateacct END AS dateacct_mov,
			-- description
			CASE 	WHEN $P{SummaryType} = 'T' THEN ''
				WHEN $P{SummaryType} = 'N' THEN ''
				WHEN $P{SummaryType} = 'D' THEN '' 
				ELSE TRIM(fac.description) END AS description_mov,
			-- BPartner
			CASE 	WHEN $P{SummaryType} = 'T' THEN TRIM(fac.bpartner_value)
				WHEN $P{SummaryType} = 'N' THEN TRIM(fac.bpartner_value)
				WHEN $P{SummaryType} = 'D' THEN TRIM(fac.bpartner_value)
				ELSE TRIM(fac.bpartner_value) END AS bpartner_value,
			CASE 	WHEN $P{SummaryType} = 'T' THEN TRIM(fac.bpartner_name)
				WHEN $P{SummaryType} = 'N' THEN TRIM(fac.bpartner_name)
				WHEN $P{SummaryType} = 'D' THEN TRIM(fac.bpartner_name)
				ELSE TRIM(fac.bpartner_name) END AS bpartner_name,
			-- Product
			CASE 	WHEN $P{SummaryType} = 'T' THEN TRIM(fac.mproduct_value)
				WHEN $P{SummaryType} = 'N' THEN TRIM(fac.mproduct_value)
				WHEN $P{SummaryType} = 'D' THEN TRIM(fac.mproduct_value)
				ELSE TRIM(fac.mproduct_value) END AS prod_value,
			CASE 	WHEN $P{SummaryType} = 'T' THEN TRIM(fac.mproduct_name)
				WHEN $P{SummaryType} = 'N' THEN TRIM(fac.mproduct_name)
				WHEN $P{SummaryType} = 'D' THEN TRIM(fac.mproduct_name)
				ELSE TRIM(fac.mproduct_name) END AS prod_name,
			-- description_tbl
			CASE	WHEN fac.c_doctype_id = 9999999999 THEN 'BankStatement' -- C_BankStatement
					WHEN fac.c_doctype_id = 9999999998 THEN 'Cash' -- C_Cash
					WHEN fac.c_doctype_id = 9999999997 THEN 'Allocation' -- C_AllocationHdr
					WHEN fac.c_doctype_id = 9999999996 THEN 'Production' -- M_Production
					WHEN fac.c_doctype_id = 9999999995 THEN 'MatchInv' -- M_MatchInv
					WHEN fac.c_doctype_id = 9999999994 THEN 'MatchPO' -- M_MatchPO
					WHEN fac.c_doctype_id > 0 AND fac.c_doctype_id < 9999999990 THEN COALESCE(TRIM(doct.printname),TRIM(doc.doc_printname),'')
					ELSE '???' END description_tbl
			FROM amf_fact_accounts_analitic_v fac
			LEFT JOIN ( 
				SELECT c_doctype_id, docbasetype, name as doc_name, printname as doc_printname FROM c_doctype WHERE AD_Client_ID = $P{AD_Client_ID}
			) doc ON fac.c_doctype_id = doc.c_doctype_id
			LEFT JOIN c_doctype_trl doct ON (doct.c_doctype_id = doc.c_doctype_id AND doct.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) )
			WHERE fac.AD_Client_ID = $P{AD_Client_ID} AND fac.C_AcctSchema_ID=$P{C_AcctSchema_ID}	 
			AND fac.c_period_id = $P{C_Period_ID} 
			-- Validar que DateFrom sea nulo o mayor o igual al inicio del periodo
			  AND (
			    CAST($P{DateFrom} AS timestamp) IS NULL
			    OR CAST($P{DateFrom} AS timestamp) >= (SELECT startdate FROM c_period WHERE c_period_id = $P{C_Period_ID})
			  )
			-- Validar que DateTo sea nulo o menor o igual al fin del periodo
			  AND (
			    CAST($P{DateTo} AS timestamp) IS NULL
			    OR CAST($P{DateTo} AS timestamp) <= (SELECT enddate FROM c_period WHERE c_period_id = $P{C_Period_ID})
			  )
			-- Validar que DateTo sea mayor o igual a DateFrom si ambos no son nulos
			  AND (
			    CAST($P{DateFrom} AS timestamp) IS NULL
			    OR CAST($P{DateTo} AS timestamp) IS NULL
			    OR CAST($P{DateTo} AS timestamp) >= CAST($P{DateFrom} AS timestamp)
			  )
			-- Filtrar fac.dateacct según fechas sólo si las fechas existen
			AND fac.dateacct >= COALESCE($P{DateFrom}, fac.dateacct)
			AND fac.dateacct <= COALESCE($P{DateTo}, fac.dateacct)
			GROUP BY  fac.ad_client_id, fac.ad_org_id, c_period_id, c_acctschema_id, account_id, documentno, fac.c_doctype_id, docbasetype, printnamel,
			dateacct, description_mov, amtacctdr, amtacctcr, description_tbl, doct.printname, doc.doc_printname, doc.doc_name,
			c_bpartner_id, bpartner_value, bpartner_name, m_product_id, prod_value, prod_name, c_project_id, c_activity_id, c_SalesRegion_ID, c_campaign_id
		) as fas2 
		LEFT JOIN (
			SELECT AD_Client_ID,C_Activity_ID, Value, Name FROM C_Activity WHERE isSummary='N' AND isActive='Y' AND AD_Client_ID=$P{AD_Client_ID}
		) as ac3 ON (ac3.AD_Client_ID = fas2.AD_Client_ID AND ac3.C_Activity_ID = fas2.C_Activity_ID)
		LEFT JOIN (
			SELECT AD_Client_ID,C_Project_ID, Value, Name FROM C_Project WHERE isSummary='N' AND isActive='Y' AND AD_Client_ID=$P{AD_Client_ID}
		) as pr3 ON (pr3.AD_Client_ID = fas2.AD_Client_ID AND pr3.C_Project_ID = fas2.C_Project_ID)
		LEFT JOIN c_elementvalue el3 ON (el3.c_elementvalue_id = fas2.account_id)
	) as fasmov	ON ( fasmov.account_id = bal.c_elementvalue_id	AND fasmov.ad_org_id = bal.org_ad_org_id)
) AS balances ON 1=0
WHERE header_info.imp_header = 1 OR 
	(ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR org_ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY 
	balances.codigo0, balances.codigo1, balances.codigo2,
	balances.codigo3, balances.codigo4, balances.codigo5,
	balances.codigo6, balances.codigo7, balances.codigo8, balances.codigo9,
	balances.codigo, balances.org_value, dateacct_order ASC


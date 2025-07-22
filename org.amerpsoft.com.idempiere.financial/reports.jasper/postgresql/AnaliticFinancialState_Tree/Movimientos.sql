--
-- MOVEMENTS
--
	SELECT 
	-- NEW VARIABLES
	'0' as balmov,  -- 0 Balance 1 Movements
	to_char(CAST(fas.dateacct as TimeStamp),'YYYY-MM-DD') AS dateacct,
	--
	CLI.value as clivalue,
	CLI.name as cliname,
	coalesce(CLI.description,CLI.name,'') as clidescription,
	IMG.binarydata as cli_logo,
	CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	PAR2.Level,
	PAR2.Node_ID, 
	PAR2.Parent_ID ,
	PAR2.c_element_id,
	PAR2.c_elementvalue_id,
	PAR2.ad_client_id,
	PAR2.ad_org_id,
	PAR2.isactive,
	PAR2."value",
	COALESCE(PAR2.name,'') as name,
	LPAD('', char_length(PAR2.value),' ') || COALESCE(PAR2.description,PAR2.name) as description,
	char_length(PAR2.value) as length,
	PAR2.accounttype,
	PAR2.accountsign,
	PAR2.isdoccontrolled,
	PAR2.issummary,
	COALESCE(PAR2.value_parent,'') as value_parent,
	COALESCE(acctparent[2],'') as Value1,
	COALESCE(acctparent[3],'') as Value2,
	COALESCE(acctparent[4],'') as Value3,
	COALESCE(acctparent[5],'') as Value4,
	COALESCE(acctparent[6],'') as Value5,	
	COALESCE(acctparent[7],'') as Value6,	
	0 as openbalance,
	fas.amtacctdr,
	fas.amtacctcr,
	0 as amtacctsa,
	0 as closebalance,
	to_char(CAST($P{DateFrom} as TimeStamp),'DD-MM-YYYY') AS dateini,
	to_char(CAST($P{DateTo} as TimeStamp),'DD-MM-YYYY') AS dateend,
	COALESCE(prod.value,'') as prod_value,
	COALESCE(prod.name,'') as prod_name,
	COALESCE(cbpa.value,'') as bpartner_value,
	COALESCE(cbpa.name,'') as bpartner_name,	
	COALESCE(proj.value,'') as proj_value,
	COALESCE(proj.name,'') as proj_name,
	COALESCE(acti.value,'') as activity_value,
	COALESCE(acti.name,'') as activity_name,
	COALESCE(sare.value,'') as salesregion_value,
	COALESCE(sare.name,'') as salesregion_name,
	COALESCE(camp.value,'') as campaign_value,
	COALESCE(camp.name,'') as campaign_name,
	posttype.value as postingtype_value,
	posttype.name as postingtype_name
	
	FROM (
		SELECT
		fac.ad_client_id, 
		fac.c_period_id, 
		fac.c_acctschema_id, 
		fac.account_id,
		CASE 	WHEN $P{SummaryType} = 'T' THEN 1000000
			WHEN $P{SummaryType} = 'N' THEN 1000000 
			WHEN $P{SummaryType} = 'D' THEN 1000000 
			ELSE fac.fact_acct_id END AS fact_acct_id,
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
				WHEN fac.c_doctype_id > 0 AND fac.c_doctype_id < 9999999990 THEN COALESCE(doc.docbasetype,'???')
				ELSE '???' END docbasetype,
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
			ELSE fac.dateacct END AS dateacct,
		-- description
		CASE 	WHEN $P{SummaryType} = 'T' THEN 'Resumido por Tipo de Documento'
			WHEN $P{SummaryType} = 'N' THEN 'Resumido por Número de Documento'
			WHEN $P{SummaryType} = 'D' THEN 'Resumido por Días y Tipo de Documento' 
			ELSE TRIM(fac.description) END AS description,
		-- description_tbl
		CASE	WHEN fac.c_doctype_id = 9999999999 THEN 'Estado de Cuenta' -- C_BankStatement
				WHEN fac.c_doctype_id = 9999999998 THEN 'Caja' -- C_Cash
				WHEN fac.c_doctype_id = 9999999997 THEN 'Asignacion' -- C_AllocationHdr
				WHEN fac.c_doctype_id = 9999999996 THEN 'Produccion' -- M_Production
				WHEN fac.c_doctype_id = 9999999995 THEN 'Asignacion Inv' -- M_MatchInv
				WHEN fac.c_doctype_id = 9999999994 THEN 'Asignacion OC' -- M_MatchPO
				WHEN fac.c_doctype_id > 0 AND fac.c_doctype_id < 9999999990 THEN TRIM(doct.printname)
				ELSE '???' END description_tbl
		FROM amf_fact_accounts_analitic_v fac
		LEFT JOIN ( 
			SELECT c_doctype_id, docbasetype FROM c_doctype WHERE AD_Client_ID = $P{AD_Client_ID}
		) doc ON fac.c_doctype_id = doc.c_doctype_id
		LEFT JOIN c_doctype_trl doct ON (doct.c_doctype_id = doc.c_doctype_id AND doct.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) )
		WHERE fac.dateacct >= $P{DateFrom}  AND fac.dateacct <= $P{DateTo}  
		AND fac.AD_Client_ID = $P{AD_Client_ID} AND fac.C_AcctSchema_ID=$P{C_AcctSchema_ID}
		GROUP BY  fac.ad_client_id, c_period_id, c_acctschema_id, fact_acct_id, account_id, documentno, fac.c_doctype_id, docbasetype, printnamel, 
		dateacct, description, amtacctdr, amtacctcr, description_tbl,printname
	) as fas 
	LEFT JOIN Nodos PAR2 ON ( fas.account_id = PAR2.c_elementvalue_id )		
	LEFT JOIN (
		SELECT 
		adcli.AD_Client_ID, 
		accsh.C_AcctSchema_ID, 
		accee.C_Element_ID, 
		accel.name as element_name, 
		tree.AD_Tree_ID, 
		tree.name as tree_name
		FROM AD_Client adcli
		LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
		LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
		LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
		LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
		WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID}
	) as ELE2 ON ELE2.AD_Tree_ID = PAR2.AD_Tree_ID
	LEFT JOIN ad_client AS CLI ON (CLI.ad_client_id = ELE2.ad_client_id)
	LEFT JOIN ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
	LEFT JOIN ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
	LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = $P{C_AcctSchema_ID})
	LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
	LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	LEFT JOIN M_product prod ON prod.M_Product_ID = $P{M_Product_ID}
	LEFT JOIN C_BPartner cbpa ON cbpa.C_BPartner_ID = $P{C_BPartner_ID}
	LEFT JOIN C_Project proj ON proj.C_Project_ID = $P{C_Project_ID}
	LEFT JOIN C_Activity acti ON acti.C_Activity_ID = $P{C_Activity_ID}
	LEFT JOIN C_SalesRegion sare ON sare.C_SalesRegion_ID = $P{C_SalesRegion_ID}
	LEFT JOIN C_Campaign camp ON camp.C_Campaign_ID = $P{C_Campaign_ID}
	LEFT JOIN (
		SELECT DISTINCT ON (rfl.value) rfl.value, COALESCE(rflt.name,rfl.name,'') as name FROM AD_Reference as rfr
		LEFT JOIN AD_Ref_List as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		LEFT JOIN AD_Ref_List_Trl as rflt ON (rflt.ad_ref_list_id = rfl.ad_ref_list_id AND rflt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}))
		WHERE rfr.name like '%Posting Type%'
	) as posttype ON (posttype.value= $P{PostingType} )
	WHERE PAR2.issummary='N'
-- Trial Balance Two Dates
-- FROM DEfault AD_Tree FROM A GIVEN AD_Client_ID and C_AcctSchema_ID
-- FOR NEW ACCOUNTING REPORTS
WITH RECURSIVE OrgTree AS (
    -- Nodo raíz (organizaciones sin padre)
    SELECT 
        org.ad_client_id,
        org.ad_org_id,
        org.issummary,
        org.name,
        org.value,
        COALESCE(org.description, org.name, '') AS description,
        COALESCE(info.taxid, '?') AS taxid,
        img.binarydata AS binarydata,
        tree.ad_tree_id,
        node.node_id,
        0 AS level,
        node.parent_id,
        ARRAY [node.node_id::text] AS ancestry
    FROM ad_treenode node
    JOIN ad_tree tree ON node.ad_tree_id = tree.ad_tree_id
    JOIN ad_org org ON node.node_id = org.ad_org_id
    LEFT JOIN ad_orginfo info ON org.ad_org_id = info.ad_org_id
    LEFT JOIN ad_image img ON info.logo_id = img.ad_image_id
    WHERE tree.treetype = 'OO' -- Árbol de organizaciones
    AND node.parent_id = 0 -- Nodo raíz
    AND org.ad_client_id = $P{AD_Client_ID}
    UNION ALL
    -- Nodos hijos
    SELECT 
        org.ad_client_id,
        org.ad_org_id,
        org.issummary,
        org.name,
        org.value,
        COALESCE(org.description, org.name, '') AS description,
        COALESCE(info.taxid, '?') AS taxid,
        img.binarydata AS binarydata,
        parent.ad_tree_id,
        node.node_id,
        parent.level + 1 AS level,
        node.parent_id,
        parent.ancestry || ARRAY [node.node_id::text] AS ancestry
    FROM ad_treenode node
    JOIN OrgTree parent ON node.parent_id = parent.node_id
    JOIN ad_org org ON node.node_id = org.ad_org_id
    LEFT JOIN ad_orginfo info ON org.ad_org_id = info.ad_org_id
    LEFT JOIN ad_image img ON info.logo_id = img.ad_image_id
    WHERE node.ad_tree_id = parent.ad_tree_id
), 
ElementValueTree AS (
	SELECT 
	ELV.ad_client_id,
	ELV.ad_org_id,
	ELV.c_element_id,
	ELV.c_elementvalue_id,
	ELV.isactive,
	ELV."value",
	COALESCE(ELV.name,'') as name,
	LPAD('', char_length(TRIM(ELV.value)),' ') || COALESCE(ELV.description,ELV.name) as description,
	char_length(TRIM(ELV.value)) as length,
	ELV.accounttype,
	ELV.accountsign,
	ELV.isdoccontrolled,
	ELV.issummary,
	COALESCE(ELVP.value,'') as value_parent ,
	TRN1.AD_Tree_ID,
	TRN1.Node_ID, 
	0 as level, 
	TRN1.Parent_ID, 
	ARRAY [TRN1.Node_ID::text]  AS ancestry, 
	ARRAY [ELVP.value::text]  AS acctparent,
	TRN1.Node_ID as Star_An
	FROM ad_treenode TRN1 
	LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = TRN1.NODE_ID
	LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = TRN1.Parent_ID
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
			LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
			LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
			WHERE accee.ElementType='AC' 
			AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID} 
			AND adcli.AD_client_ID=$P{AD_Client_ID}
	) 
	AND TRN1.isActive='Y' AND TRN1.Parent_ID = 0
    UNION ALL
	SELECT 
	ELV.ad_client_id,
	ELV.ad_org_id,
	ELV.c_element_id,
	ELV.c_elementvalue_id,
	ELV.isactive,
	ELV."value",
	COALESCE(ELV.name,'') as name,
	LPAD('', char_length(ELV.value),' ') || COALESCE(ELV.description,ELV.name) as description,
	char_length(TRIM(ELV.value)) as length,
	ELV.accounttype,
	ELV.accountsign,
	ELV.isdoccontrolled,
	ELV.issummary,
	COALESCE(ELVP.value,'') as value_parent ,
	TRN1.AD_Tree_ID, 
	TRN1.Node_ID, 
	TRN2.level+1 as level,
	TRN1.Parent_ID, 
	TRN2.ancestry || ARRAY[TRN1.Node_ID::text] AS ancestry,
	TRN2.acctparent || ARRAY [ELVP.value::text]  AS acctparent,
	COALESCE(TRN2.Star_An,TRN1.Parent_ID) as Star_An
	FROM ad_treenode TRN1 
	INNER JOIN ElementValueTree TRN2 ON (TRN2.node_id =TRN1.Parent_ID)
	LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = TRN1.NODE_ID
	LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = TRN1.Parent_ID
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
			LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
			LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
			WHERE accee.ElementType='AC' 
			AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID} 
			AND adcli.AD_client_ID=$P{AD_Client_ID}
	)  AND TRN1.isActive='Y' 
)
-- VAriables FOR REPORT
SELECT 
	trial.clivalue,
	trial.cliname,
	trial.clidescription,
	trial.cli_logo,
	trial.org_logo,
	trial.orgname,
	trial.orgdescription,
	trial.orgtaxid,
	trial.all_orgs,
	trial.moneda,
	trial.Level,
	trial.Node_ID, 
	trial.value as codigo,
	trial.name,
	trial.description,
	trial.isactive,
	trial.length,
	trial.accounttype,
	trial.accountsign,
	trial.isdoccontrolled,
	trial.issummary,
	trial.isactive,
	trial.openbalance,
	trial.amtacctdr,
	trial.amtacctcr,
	trial.amtacctsa,
	trial.closebalance,
	trial.dateini,
	trial.dateend,	
	COALESCE(ELVN1.value,'Z') as codigo1,
	COALESCE(ELVN1.name,CONCAT('Tree error value=',trial.value)) as name1,
	ELVN1.description as description1,
	ELVN1.issummary as issummary1,
	CASE WHEN ELVN1.value <> '' THEN ELVN2.value ELSE 'ZZ' END as codigo2,
	ELVN2.name as name2,
	ELVN2.description as description2,
	ELVN2.issummary as issummary2,
	CASE WHEN ELVN1.value <> '' THEN ELVN3.value ELSE 'ZZZ' END as codigo3,
	ELVN3.name as name3,
	ELVN3.description as description3,
	ELVN3.issummary as issummary3,
	CASE WHEN ELVN1.value <> '' THEN ELVN4.value ELSE 'ZZZZ' END as codigo4,
	ELVN4.name as name4,
	ELVN4.description as description4,
	ELVN4.issummary as issummary4,
	CASE WHEN ELVN1.value <> '' THEN ELVN5.value ELSE 'ZZZZZ' END as codigo5,
	ELVN5.name as name5,
	ELVN5.description as description5,
	ELVN5.issummary as issummary5,
	CASE WHEN ELVN1.value <> '' THEN ELVN6.value ELSE 'ZZZZZZ' END as codigo6,
	ELVN6.name as name6,
	ELVN6.description as description6,
	ELVN6.issummary as issummary6,
	CONCAT(trial.value, trial.proj_value) as codigo_project,
	trial.proj_value,
	trial.proj_name,
	CONCAT(trial.value, trial.activity_value) as codigo_activity,
	trial.activity_value,
	trial.activity_name,
	trial.prod_value_sel,
	trial.prod_name_sel,
	trial.bpartner_value_sel,
	trial.bpartner_name_sel,
	trial.proj_value_sel,
	trial.proj_name_sel,
	trial.activity_value_sel,
	trial.activity_name_sel,
	trial.salesregion_value_sel,
	trial.salesregion_name_sel,
	trial.campaign_value_sel,
	trial.campaign_name_sel,
	trial.postingtype_value,
	trial.postingtype_name
FROM (
	SELECT 
	CLI.value as clivalue,
	CLI.name as cliname,
	coalesce(CLI.description,CLI.name,'') as clidescription,
	IMG.binarydata as cli_logo,
	-- ORG
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN 'Consolidado' ELSE ORG.name END as orgname,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN 'Consolidado' ELSE COALESCE(ORG.description,ORG.name) END as orgdescription,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN IMG.binarydata ELSE ORG.binarydata END as org_logo,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN 'Consolidado' ELSE ORG.taxid END as orgtaxid,
	ORG.all_orgs,
	-- ORG
	CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	PAR.Level,
	PAR.Node_ID, 
	PAR.Parent_ID ,
	PAR.c_element_id,
	PAR.c_elementvalue_id,
	PAR.ad_client_id,
	PAR.ad_org_id,
	PAR.isactive,
	PAR."value",
	COALESCE(PAR.name,'') as name,
	LPAD('', char_length(PAR.value),' ') || COALESCE(PAR.description,PAR.name) as description,
	char_length(PAR.value) as length,
	PAR.accounttype,
	PAR.accountsign,
	PAR.isdoccontrolled,
	PAR.issummary,
	COALESCE(PAR.value_parent,'') as value_parent ,
	COALESCE(acctparent[2],'') as Value1,
	COALESCE(acctparent[3],'') as Value2,
	COALESCE(acctparent[4],'') as Value3,
	COALESCE(acctparent[5],'') as Value4,
	COALESCE(acctparent[6],'') as Value5,	
	COALESCE(acctparent[7],'') as Value6,	
	bal.openbalance,
	bal.amtacctdr,
	bal.amtacctcr,
	bal.amtacctsa,
	bal.closebalance,
	to_char(CAST($P{DateFrom} as TimeStamp),'DD-MM-YYYY') AS dateini,
	to_char(CAST($P{DateTo} as TimeStamp),'DD-MM-YYYY') AS dateend,
	COALESCE(prod.value,'') as prod_value_sel,
	COALESCE(prod.name,'') as prod_name_sel,
	COALESCE(cbpa.value,'') as bpartner_value_sel,
	COALESCE(cbpa.name,'') as bpartner_name_sel,
	COALESCE(proj.value,'') as proj_value_sel,
	COALESCE(proj.name,'') as proj_name_sel,
	COALESCE(acti.value,'') as activity_value_sel,
	COALESCE(acti.name,'') as activity_name_sel,	
	COALESCE(sare.value,'') as salesregion_value_sel,
	COALESCE(sare.name,'') as salesregion_name_sel,
	COALESCE(camp.value,'') as campaign_value_sel,
	COALESCE(camp.name,'') as campaign_name_sel,
	bal.c_project_id,
	bal.proj_value, bal.proj_name, 
	bal.c_activity_id,
	bal.activity_value, bal.activity_name,
	posttype.value as postingtype_value,
	posttype.name as postingtype_name
	FROM ElementValueTree PAR
	LEFT JOIN (
		SELECT 
		ad_client_id,
		c_elementvalue_id, 
		value, 
		name, C_AcctSchema_ID, 
		bal2.c_project_id,
		bal2.c_activity_id,
		bal2.proj_value, bal2.proj_name, bal2.activity_value, bal2.activity_name,
		COALESCE(openbalance,0) as openbalance,
		COALESCE(amtacctdr,0) as amtacctdr,
		COALESCE(amtacctcr,0) as amtacctcr,
		COALESCE(amtacctdr - amtacctcr,0) as amtacctsa,
		COALESCE(closebalance,0) as closebalance
		FROM (
			SELECT
			ele.ad_client_id,
			c_elementvalue_id, ele.value, ele.name, C_AcctSchema_ID, 
			fas.c_project_id, fas.c_activity_id,
			fas.proj_value, fas.proj_name, fas.activity_value, fas.activity_name,
			SUM(CASE WHEN (fas.DateAcct < CAST($P{DateFrom} as TimeStamp)) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as openbalance,
					SUM(CASE WHEN (fas.DateAcct >= CAST($P{DateFrom} as TimeStamp) AND fas.DateAcct <= CAST($P{DateTo} as TimeStamp)) THEN COALESCE(fas.amtacctdr,0) ELSE 0 END) as amtacctdr,
					SUM(CASE WHEN (fas.DateAcct >= CAST($P{DateFrom} as TimeStamp) AND fas.DateAcct <= CAST($P{DateTo} as TimeStamp)) THEN COALESCE(fas.amtacctcr,0) ELSE 0 END) as amtacctcr,
					SUM(CASE WHEN (fas.DateAcct <= CAST($P{DateTo} as TimeStamp) ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance
			FROM c_elementvalue ele
			INNER JOIN (
				SELECT fa2.ad_client_id, fa2.ad_org_id, fa2.fact_acct_id,fa2. c_acctschema_id, fa2.DateAcct, fa2.account_id, fa2.amtacctdr, fa2.amtacctcr, 
				fa2.m_product_id, fa2.c_bpartner_id, fa2.c_salesregion_id, fa2.c_campaign_id,
				CASE WHEN $P{isShowProject} = 'Y' AND pr2.c_project_id IS NOT NULL AND el2.AccountType IN ('E','M') THEN pr2.c_project_id ELSE 0 END AS c_project_id, 
				CASE WHEN $P{isShowProject} = 'Y' AND pr2.c_project_id IS NOT NULL AND el2.AccountType IN ('E','M') THEN pr2.name ELSE 'NO Project' END AS proj_name, 
				CASE WHEN $P{isShowProject} = 'Y' AND pr2.c_project_id IS NOT NULL AND el2.AccountType IN ('E','M') THEN pr2.value ELSE '0000' END AS proj_value, 
				CASE WHEN $P{isShowActivity} = 'Y' AND ac2.c_activity_id IS NOT NULL AND el2.AccountType IN ('E','M') THEN ac2.c_activity_id ELSE 0 END AS c_activity_id,
				CASE WHEN $P{isShowActivity} = 'Y' AND ac2.c_activity_id IS NOT NULL AND el2.AccountType IN ('E','M') THEN ac2.name ELSE 'NO Activity' END AS activity_name,
				CASE WHEN $P{isShowActivity} = 'Y' AND ac2.c_activity_id IS NOT NULL AND el2.AccountType IN ('E','M') THEN ac2.value ELSE '0000' END AS activity_value
				FROM fact_acct fa2
				INNER JOIN (
					-- ORGs as from OrgTree
					SELECT DISTINCT ON  (ORG12.ad_client_id, ORG12.node_id, ORG12.name)
					    ORG12.ad_client_id, 
					    ORG12.ad_org_id,
					    ORG12.parent_id as ad_orgparent_id,
					    ORG12.issummary,
					    ORG12.name
					FROM OrgTree AS ORG12
					LEFT JOIN ad_orginfo AS ORF1 ON (ORG12.ad_org_id = ORF1.ad_org_id)		
					LEFT JOIN adempiere.ad_image AS IMB1 ON (ORF1.logo_id = IMB1.ad_image_id)
					WHERE ORG12.ad_client_id = $P{AD_Client_ID}
					AND ORG12.issummary='N'
					AND CASE WHEN $P{AD_Org_ID}=ORG12.ad_org_id OR $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL THEN 1=1 ELSE 1=0 END
					AND CASE WHEN $P{AD_OrgParent_ID}=ORG12.parent_id OR $P{AD_OrgParent_ID}=0 OR $P{AD_OrgParent_ID} IS NULL THEN 1=1 ELSE 1=0 END
					ORDER BY ORG12.ad_client_id, ORG12.node_id, ORG12.name
				) AS org2 ON org2.ad_org_id = fa2.ad_org_id
				LEFT JOIN c_elementvalue el2 ON (el2.c_elementvalue_id = fa2.account_id)
				LEFT JOIN (
					SELECT AD_Client_ID,C_Activity_ID, Value, Name FROM C_Activity WHERE isSummary='N' AND isActive='Y' AND AD_Client_ID=$P{AD_Client_ID}
				) as ac2 ON (ac2.AD_Client_ID = fa2.AD_Client_ID AND ac2.C_Activity_ID = fa2.C_Activity_ID)
				LEFT JOIN (
					SELECT AD_Client_ID,C_Project_ID, Value, Name FROM C_Project WHERE isSummary='N' AND isActive='Y' AND AD_Client_ID=$P{AD_Client_ID}
				) as pr2 ON (pr2.AD_Client_ID = fa2.AD_Client_ID AND pr2.C_Project_ID = fa2.C_Project_ID)
				WHERE fa2.c_acctschema_id = $P{C_AcctSchema_ID}  
				AND fa2.ad_client_id=$P{AD_Client_ID}
				AND ( CASE WHEN  fa2.postingtype = $P{PostingType} THEN 1=1 ELSE 1=0 END ) 
				AND ( CASE WHEN ( $P{M_Product_ID} IS NULL OR fa2.M_Product_ID = $P{M_Product_ID}) THEN 1=1 ELSE 1=0 END ) 
			 	AND ( CASE WHEN ( $P{C_BPartner_ID} IS NULL OR fa2.C_BPartner_ID = $P{C_BPartner_ID}) THEN 1=1 ELSE 1=0 END ) 
			 	AND ( CASE WHEN ( $P{C_Project_ID} IS NULL OR fa2.C_Project_ID = $P{C_Project_ID}) THEN 1=1 ELSE 1=0 END ) 
			 	AND ( CASE WHEN ( $P{C_Activity_ID} IS NULL OR fa2.C_Activity_ID = $P{C_Activity_ID}) THEN 1=1 ELSE 1=0 END ) 
				AND ( CASE WHEN ( $P{C_SalesRegion_ID} IS NULL OR fa2.C_SalesRegion_ID = $P{C_SalesRegion_ID}) THEN 1=1 ELSE 1=0 END ) 
			 	AND ( CASE WHEN ( $P{C_Campaign_ID} IS NULL OR fa2.C_Campaign_ID = $P{C_Campaign_ID}) THEN 1=1 ELSE 1=0 END ) 			
			) fas ON (ele.c_elementvalue_id=fas.account_id )
			LEFT JOIN c_period per01	ON (per01.c_period_id IN (select distinct c_period_id from c_period where to_char(per01.startdate,'YYYYMM') = to_char(CAST($P{DateFrom} as TimeStamp),'YYYYMM') AND ad_client_id=$P{AD_Client_ID} ))		
			LEFT JOIN c_period per02	ON (per02.c_period_id IN (select distinct c_period_id from c_period where to_char(per02.startdate,'YYYYMM') = to_char(CAST($P{DateTo} as TimeStamp),'YYYYMM') AND ad_client_id=$P{AD_Client_ID} ))		
			WHERE ele.issummary='N'
			AND ele.ad_client_id=$P{AD_Client_ID}
			GROUP BY ele.ad_client_id, ele.c_elementvalue_id, fas.C_AcctSchema_ID, fas.c_project_id, fas.c_activity_id,
					fas.proj_value, fas.proj_name, fas.activity_value, fas.activity_name
		) as bal2 
	) as bal ON ( bal.c_elementvalue_id = PAR.c_elementvalue_id )	
	INNER JOIN (
		SELECT 
		adcli.AD_Client_ID, 
		accsh.C_AcctSchema_ID, 
		accee.C_Element_ID, 
		accel.name as element_name, 
		tree.AD_Tree_ID, 
		tree.name as tree_name
		FROM AD_Client adcli
		INNER JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
		INNER JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
		INNER JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
		INNER JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
		WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID}
	) as ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID
	INNER JOIN ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id)
	INNER JOIN ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
	LEFT JOIN ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
	-- ORG
	INNER JOIN (
		-- ORGs as from OrgTree
		SELECT DISTINCT ON  (ORG12.ad_client_id, ORG12.node_id, ORG12.name)
		    ORG12.ad_client_id, 
		    ORG12.ad_org_id,
		    ORG12.parent_id as ad_orgparent_id,
		    ORG12.issummary,
		    ORG12.name, 
		    COALESCE(ORG12.description, ORG12.name,'') as description, 
		    COALESCE(ORF1.taxid,'?') as taxid, 
		    IMB1.binarydata as binarydata,
		    -- Columna con todos los valores únicos concatenados
		    (SELECT STRING_AGG(DISTINCT ORGX.value, '-' ORDER BY ORGX.value) 
		     FROM OrgTree ORGX
		     WHERE ORGX.ad_client_id = ORG12.ad_client_id
		     AND (ORGX.ad_org_id = $P{AD_Org_ID} OR $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL)
		     AND (ORGX.parent_id = $P{AD_OrgParent_ID} OR $P{AD_OrgParent_ID} = 0 OR $P{AD_OrgParent_ID} IS NULL)
		    ) AS all_orgs
		FROM OrgTree AS ORG12
		LEFT JOIN ad_orginfo AS ORF1 ON (ORG12.ad_org_id = ORF1.ad_org_id)		
		LEFT JOIN adempiere.ad_image AS IMB1 ON (ORF1.logo_id = IMB1.ad_image_id)
		WHERE ORG12.ad_client_id = $P{AD_Client_ID}
		AND ORG12.issummary='N'
		AND CASE WHEN $P{AD_Org_ID}=ORG12.ad_org_id OR $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL THEN 1=1 ELSE 1=0 END
		AND CASE WHEN $P{AD_OrgParent_ID}=ORG12.parent_id OR $P{AD_OrgParent_ID}=0 OR $P{AD_OrgParent_ID} IS NULL THEN 1=1 ELSE 1=0 END
		ORDER BY ORG12.ad_client_id, ORG12.node_id, ORG12.name
	) AS ORG ON (ORG.ad_client_id = CLI.ad_client_id) 
	-- ORG
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
	WHERE PAR.issummary='N'
	AND CASE WHEN  $P{isShowZERO} = 'Y' OR ( $P{isShowZERO} = 'N' AND (  bal.openbalance<> 0 or bal.amtacctcr<> 0 or bal.amtacctdr <>0 or bal.closebalance <> 0  ) ) THEN 1=1  ELSE 1=0 END
	ORDER BY PAR.ANCESTRY
) trial
LEFT JOIN c_elementvalue as ELVN1 ON (ELVN1.Value = trial.Value1 AND ELVN1.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN2 ON (ELVN2.Value = trial.Value2 AND ELVN2.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN3 ON (ELVN3.Value = trial.Value3 AND ELVN3.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN4 ON (ELVN4.Value = trial.Value4 AND ELVN4.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN5 ON (ELVN5.Value = trial.Value5 AND ELVN5.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN6 ON (ELVN6.Value = trial.Value6 AND ELVN6.AD_Client_ID= trial.AD_Client_ID)
ORDER BY codigo1 ASC, codigo2 ASC, codigo3 ASC, codigo4 ASC, codigo5 ASC, codigo6 ASC, codigo_activity ASC, codigo ASC
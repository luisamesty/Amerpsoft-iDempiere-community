-- TrialBalance_Tree V7
-- OrgTree Version4
-- ORG_AccountElement_Tree_V5.sql
-- OrgTreeMaster V5 con parámetros AD_OrgParent_ID y AD_Org_ID
-- Removed Activity AND others
WITH OrgTree AS (
    WITH RECURSIVE OrgTreeBase AS (
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
        WHERE tree.treetype = 'OO' 
        AND node.parent_id = 0
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
        JOIN OrgTreeBase parent ON node.parent_id = parent.node_id
        JOIN ad_org org ON node.node_id = org.ad_org_id
        LEFT JOIN ad_orginfo info ON org.ad_org_id = info.ad_org_id
        LEFT JOIN ad_image img ON info.logo_id = img.ad_image_id
        WHERE node.ad_tree_id = parent.ad_tree_id
        AND org.ad_client_id = $P{AD_Client_ID}
    )
    -- Selección final con los datos completos del árbol
    SELECT DISTINCT ON (ad_client_id, value)
        ad_client_id, 
        ad_org_id,
        parent_id AS ad_orgparent_id,
        issummary,
        value,
        name,
        description, 
        taxid, 
        binarydata,
        (SELECT STRING_AGG(DISTINCT ORGX.value, ' - ' ORDER BY ORGX.value) 
         FROM OrgTreeBase ORGX
         WHERE ORGX.issummary = 'N') AS all_orgs
    FROM OrgTreeBase AS orgtb
    WHERE orgtb.ad_client_id = $P{AD_Client_ID}
    AND orgtb.issummary = 'N'
    AND ($P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL OR orgtb.ad_org_id = $P{AD_Org_ID})
    AND ($P{AD_OrgParent_ID} = 0 OR $P{AD_OrgParent_ID} IS NULL OR orgtb.parent_id = $P{AD_OrgParent_ID})
    ORDER BY ad_client_id, value
), ElementValueTree AS (
    -- Lógica para la jerarquía de cuentas contables
    WITH RECURSIVE AccountTreeBase AS (
    -- Nodos raíz (con conversión explícita de tipos)
    SELECT 
    	ev.isactive,
    	ev.ad_client_id,
		ev.accountsign,
		ev.isdoccontrolled,
        tn.AD_Tree_ID,
        tn.Node_ID,
        tn.Parent_ID,
        tn.SeqNo,
        0 AS level,
        ev.Value::VARCHAR(2000) AS path,  -- Conversión explícita con longitud suficiente
        ev.Value::VARCHAR(40) AS AccountValue,
        ev.Name::VARCHAR(60) AS AccountName,
        ev.AccountType::VARCHAR(40) AS AccountType,
        ev.IsSummary,
        t.Name::VARCHAR(60) AS TreeName,
        ARRAY [tn.Node_ID::text] AS ancestry,
        ARRAY [ev.value::text] AS acctparent
    FROM AD_TreeNode tn
    JOIN C_ElementValue ev ON tn.Node_ID = ev.C_ElementValue_ID
    JOIN AD_Tree t ON tn.AD_Tree_ID = t.AD_Tree_ID
    WHERE (tn.Parent_ID IS NULL OR tn.Parent_ID = 0)
        AND tn.AD_Client_ID = $P{AD_Client_ID}
        AND tn.IsActive = 'Y'
        AND ev.IsActive = 'Y'
        AND tn.AD_tree_ID = (
            SELECT tree.AD_Tree_ID
            FROM AD_Client adcli
            LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
            LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
            LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
            LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID = accel.AD_Tree_ID
            WHERE accee.ElementType = 'AC' 
            AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID} 
            AND adcli.AD_client_ID = $P{AD_Client_ID}
        )
    UNION ALL
    -- Nodos hijos (manteniendo los mismos tipos)
    SELECT 
    	child_ev.isactive,
    	child_ev.ad_client_id,
		child_ev.accountsign,
		child_ev.isdoccontrolled,
        child.AD_Tree_ID,
        child.Node_ID,
        child.Parent_ID,
        child.SeqNo,
        parent.level + 1,
        (parent.path || '->' || child_ev.Value)::VARCHAR(2000) AS path,
        child_ev.Value::VARCHAR(40) AS AccountValue,
        child_ev.Name::VARCHAR(60) AS AccountName,
        child_ev.AccountType::VARCHAR(40) AS AccountType,
        child_ev.IsSummary,
        parent.TreeName,
        parent.ancestry || ARRAY[child.Node_ID::text] AS ancestry,
        parent.acctparent || ARRAY[child_ev.value::text] AS acctparent
    FROM AD_TreeNode child
    JOIN C_ElementValue child_ev ON child.Node_ID = child_ev.C_ElementValue_ID
    JOIN AccountTreeBase parent ON child.Parent_ID = parent.Node_ID
    WHERE child.IsActive = 'Y'
        AND child_ev.IsActive = 'Y'
	)
	SELECT * FROM AccountTreeBase
)
-- MAIN QUERY
-- ORG - PAR - ELVN1-7
SELECT
	-- ORG
	ORG.binarydata as cli_logo,
	ORG.value AS org_value,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN 'Consolidado' ELSE ORG.name END as orgname,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN 'Consolidado' ELSE COALESCE(ORG.description,ORG.name) END as orgdescription,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN ORG.binarydata ELSE ORG.binarydata END as org_logo,
	CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} IS NULL THEN 'Consolidado' ELSE ORG.taxid END as orgtaxid,
	ORG.all_orgs,
	CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	-- PAR
	PAR.AccountValue AS codigo,
	COALESCE(PAR.AccountName,'') as name,
	LPAD('', char_length(PAR.AccountValue),' ') || COALESCE(PAR.AccountName,'') as description,
	-- Balances
	to_char(perba.startdate,'DD-MM-YYYY') AS dateini,
	to_char(perba.enddate,'DD-MM-YYYY') AS dateend,
	COALESCE(openbalance,0) as openbalance,
	COALESCE(amtacctdr,0) as amtacctdr,
	COALESCE(amtacctcr,0) as amtacctcr,
	COALESCE(amtacctdr - amtacctcr,0) as amtacctsa,
	COALESCE(closebalance,0) as closebalance,
	-- ELVN1-6
	COALESCE(ELVN1.value,'Z') as codigo1,
	COALESCE(ELVN1.name,CONCAT('Tree error value=',PAR.AccountValue)) as name1,
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
	CASE WHEN ELVN1.value <> '' THEN ELVN7.value ELSE 'ZZZZZZZ' END as codigo7,
	ELVN7.name as name7,
	ELVN7.description as description7,
	ELVN7.issummary as issummary7,
	CASE WHEN ELVN1.value <> '' THEN ELVN8.value ELSE 'ZZZZZZZZ' END as codigo8,
	ELVN8.name as name8,
	ELVN8.description as description8,
	ELVN8.issummary as issummary8,	
	CASE WHEN ELVN1.value <> '' THEN ELVN9.value ELSE 'ZZZZZZZZZ' END as codigo9,
	ELVN9.name as name9,
	ELVN9.description as description9,
	ELVN9.issummary as issummary9,	
	-- ElementValueTree
	--
	PAR.ANCESTRY,
	PAR.Level,
	PAR.Node_ID, 
	PAR.Parent_ID ,
--	PAR.c_element_id,
	PAR.node_id AS c_elementvalue_id,
	PAR.ad_client_id,
	PAR.isactive,
	char_length(PAR.AccountValue) as length,
	PAR.accounttype,
	PAR.accountsign,
	PAR.isdoccontrolled,
	PAR.issummary,
	PAR.AccountValue AS codigo_cuenta,
	COALESCE(ELVPT.value,'') as value_parent,
	COALESCE(PAR.acctparent[2],'') as Value1,
	COALESCE(PAR.acctparent[3],'') as Value2,
	COALESCE(PAR.acctparent[4],'') as Value3,
	COALESCE(PAR.acctparent[5],'') as Value4,
	COALESCE(PAR.acctparent[6],'') as Value5,	
	COALESCE(PAR.acctparent[7],'') as Value6,
	COALESCE(PAR.acctparent[8],'') as Value7,
	COALESCE(PAR.acctparent[9],'') as Value8,
	COALESCE(PAR.acctparent[10],'') as Value9,
	posttype.value as postingtype_value,
	posttype.name as postingtype_name
FROM (
	SELECT DISTINCT ON (ad_client_id, AccountValue) * FROM ElementValueTree
	ORDER BY ad_client_id, AccountValue
) AS PAR 
LEFT JOIN (
	SELECT adcli.AD_Client_ID, accsh.C_AcctSchema_ID, accee.C_Element_ID, accel.name as element_name, tree.AD_Tree_ID, tree.name as tree_name
	FROM AD_Client adcli
	LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
	LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
	LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
	LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
	WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID}
) as ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID
INNER JOIN C_elementValue ELVT ON ELVT.C_ElementValue_ID = PAR.NODE_ID
INNER JOIN C_elementValue ELVPT ON ELVPT.C_ElementValue_ID = PAR.Parent_ID
INNER JOIN (
	-- ORGs as from OrgTree
	SELECT DISTINCT ON  (ORG12.ad_client_id, ORG12.ad_orgparent_id, ORG12.name)
	    ORG12.ad_client_id, 
	    ORG12.ad_org_id,
	    ORG12.ad_orgparent_id,
	    ORG12.issummary,
	    ORG12.value, 
	    ORG12.name, 
	    COALESCE(ORG12.description, ORG12.name,'') as description, 
	    COALESCE(ORG12.taxid,'?') as taxid, 
	    ORG12.binarydata as binarydata,
	    -- Columna con todos los valores únicos concatenados
	    (SELECT STRING_AGG(DISTINCT ORGX.value, '-' ORDER BY ORGX.value) 
	     FROM OrgTree ORGX
	     WHERE ORGX.ad_client_id = ORG12.ad_client_id
	     AND (ORGX.ad_org_id = $P{AD_Org_ID} OR $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL)
	     AND (ORGX.ad_orgparent_id = $P{AD_OrgParent_ID} OR $P{AD_OrgParent_ID} = 0 OR $P{AD_OrgParent_ID} IS NULL)
	    ) AS all_orgs
	FROM OrgTree AS ORG12
) AS ORG ON (ORG.ad_client_id = PAR.ad_client_id) 
LEFT JOIN c_elementvalue as ELVN1 ON (ELVN1.Value = PAR.acctparent[2] AND ELVN1.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN2 ON (ELVN2.Value = PAR.acctparent[3] AND ELVN2.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN3 ON (ELVN3.Value = PAR.acctparent[4] AND ELVN3.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN4 ON (ELVN4.Value = PAR.acctparent[5] AND ELVN4.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN5 ON (ELVN5.Value = PAR.acctparent[6] AND ELVN5.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN6 ON (ELVN6.Value = PAR.acctparent[7] AND ELVN6.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN7 ON (ELVN7.Value = PAR.acctparent[8] AND ELVN7.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN8 ON (ELVN8.Value = PAR.acctparent[9] AND ELVN8.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN9 ON (ELVN9.Value = PAR.acctparent[10] AND ELVN9.AD_Client_ID= PAR.AD_Client_ID)
LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = $P{C_AcctSchema_ID})
LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
LEFT JOIN c_period as perba ON (perba.c_period_id = $P{C_Period_ID})
LEFT JOIN (
	SELECT DISTINCT ON (rfl.value) rfl.value, COALESCE(rflt.name,rfl.name,'') as name FROM AD_Reference as rfr
	LEFT JOIN AD_Ref_List as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
	LEFT JOIN AD_Ref_List_Trl as rflt ON (rflt.ad_ref_list_id = rfl.ad_ref_list_id AND rflt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}))
	WHERE rfr.name like '%Posting Type%'
) as posttype ON (posttype.value= $P{PostingType} )
LEFT JOIN (	
	--- BALANCES
	SELECT
		ele.ad_client_id,
		ele.c_elementvalue_id, 
		ele.value,
		org2.ad_org_id,
		dateini,
		dateend,
		COALESCE(openbalance,0) as openbalance,
		COALESCE(amtacctdr,0) as amtacctdr,
		COALESCE(amtacctcr,0) as amtacctcr,
		COALESCE(amtacctdr - amtacctcr,0) as amtacctsa,
		COALESCE(closebalance,0) as closebalance
		FROM c_elementvalue ele
		INNER JOIN (
			-- ORGs as from OrgTree
			SELECT DISTINCT ON  (ORG12.ad_client_id, ORG12.ad_orgparent_id, ORG12.name)
			    ORG12.ad_client_id, 
			    ORG12.ad_org_id,
			    ORG12.ad_orgparent_id,
			    ORG12.issummary,
			    ORG12.name
			FROM OrgTree AS ORG12
		) AS org2 ON org2.ad_client_id = ele.ad_client_id		
		LEFT JOIN (
			SELECT
			fas.ad_client_id,
			fas.ad_org_id,
			fas.account_id, fas.C_AcctSchema_ID, 
			to_char(per.startdate,'DD-MM-YYYY') AS dateini,
			to_char(per.enddate,'DD-MM-YYYY') AS dateend,
			SUM(CASE WHEN (fas.DateAcct < per.StartDAte) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as openbalance,
					SUM(CASE WHEN (fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctdr,0) ELSE 0 END) as amtacctdr,
					SUM(CASE WHEN (fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctcr,0) ELSE 0 END) as amtacctcr,
					SUM(CASE WHEN (fas.DateAcct <= per.EndDate ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance
			FROM (
				SELECT 
				fa2.ad_client_id, fa2.ad_org_id, fa2.fact_acct_id,
				fa2.c_acctschema_id, fa2.DateAcct, fa2.account_id, 
				fa2.amtacctdr, fa2.amtacctcr
				FROM fact_acct fa2
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
			) fas
			INNER JOIN c_period as per ON (per.c_period_id = $P{C_Period_ID})
			WHERE fas.DateAcct <= per.EndDate
			AND fas.ad_client_id=$P{AD_Client_ID}
			GROUP BY fas.ad_client_id, fas.ad_org_id, fas.account_id, 
					per.startdate, per.enddate, fas.C_AcctSchema_ID
		) as bal2 ON bal2.ad_client_id = ele.ad_client_id AND bal2.account_id = ele.c_elementvalue_id AND bal2.ad_org_id  = org2.ad_org_id
		WHERE ele.ad_client_id = $P{AD_Client_ID}  AND ele.issummary = 'N'
		ORDER BY ele.ad_client_id , bal2.ad_org_id, ele.c_elementvalue_id
-- BALANCES
) AS bal ON bal.ad_client_id = PAR.ad_client_id AND bal.ad_org_id = ORG.ad_org_id AND bal.c_elementvalue_id = PAR.node_id
WHERE PAR.ad_client_id = $P{AD_Client_ID}  AND PAR.issummary='N' 
AND posttype.value = 'A'
AND CASE WHEN  $P{isShowZERO} = 'Y' OR PAR.isSummary='Y' OR ( $P{isShowZERO} = 'N' AND ( PAR.isSummary = 'N' AND bal.openbalance<> 0 or bal.amtacctcr<> 0 or bal.amtacctdr <>0 or bal.closebalance <> 0  ) ) THEN 1=1  ELSE 1=0 END
ORDER BY codigo ASC, org_value ASC

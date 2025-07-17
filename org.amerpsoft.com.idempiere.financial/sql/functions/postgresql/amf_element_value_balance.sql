
-- Function amf_element_value_balance
-- Devueve el Balance de una cuenta de una organizacion en un periodo dado

DROP FUNCTION IF EXISTS amf_element_value_balance(NUMERIC, NUMERIC);

CREATE OR REPLACE FUNCTION amf_element_value_balance(
    p_ad_client_id INTEGER,
    p_ad_org_id INTEGER DEFAULT 0,
    p_ad_orgparent_id INTEGER DEFAULT 0,
    p_c_acctschema_id INTEGER,
    p_postingtype VARCHAR,
    p_c_period_id INTEGER,
    p_positivebalance CHAR,
    p_isshowzero CHAR,
    p_c_elementvalue_id INTEGER DEFAULT NULL,
    p_accounttype VARCHAR DEFAULT 'A,L,O,E,R,M'
)
RETURNS TABLE (
    ad_client_id INTEGER,
    ad_org_id INTEGER,
    org_value VARCHAR,
    orgname VARCHAR,
    all_orgs TEXT,
    moneda TEXT,
    codigo VARCHAR,
    name TEXT,
    openbalance NUMERIC,
    amtacctdr NUMERIC,
    amtacctcr NUMERIC,
    amtacctsa NUMERIC,
    closebalance NUMERIC
)
AS $$
BEGIN
RETURN QUERY
	WITH OrgTree AS (
	    WITH RECURSIVE OrgTreeBase AS (
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
	        AND org.ad_client_id = p_ad_client_id
	        UNION ALL
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
	        AND org.ad_client_id = p_ad_client_id
	    )
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
	    WHERE orgtb.ad_client_id = p_ad_client_id
	    AND orgtb.issummary = 'N'
	    AND (p_ad_org_id = 0 OR p_ad_org_id IS NULL OR orgtb.ad_org_id = p_ad_org_id)
	    AND (p_ad_orgparent_id = 0 OR p_ad_orgparent_id IS NULL OR orgtb.parent_id = p_ad_orgparent_id)
	    ORDER BY ad_client_id, value
	)
	SELECT
	    ORG.ad_client_id,
	    ORG.ad_org_id,
	    ORG.value AS org_value,
	    CASE WHEN p_ad_org_id = 0 OR p_ad_org_id IS NULL THEN 'Consolidado' ELSE ORG.name END as orgname,
	    ORG.all_orgs,
	    CONCAT(curr1.iso_code, '-', currt1.cursymbol, '-', COALESCE(currt1.description, curr1.iso_code, currt1.cursymbol, '')) as moneda,
	    ELEV.Value AS codigo,
	    COALESCE(ELEV.name, '') as name,
	    CASE 
	      WHEN p_positivebalance = 'Y' THEN 
	        (CASE WHEN ELEV.AccountType IN ('A', 'E') THEN 1 ELSE -1 END) * COALESCE(openbalance, 0)
	      ELSE COALESCE(openbalance, 0)
	    END AS openbalance,
	    CASE 
	      WHEN p_positivebalance = 'Y' THEN 
	        (CASE WHEN ELEV.AccountType IN ('A', 'E') THEN 1 ELSE -1 END) * COALESCE(amtacctdr, 0)
	      ELSE COALESCE(amtacctdr, 0)
	    END AS amtacctdr,
	    CASE 
	      WHEN p_positivebalance = 'Y' THEN 
	        (CASE WHEN ELEV.AccountType IN ('A', 'E') THEN 1 ELSE -1 END) * COALESCE(amtacctcr, 0)
	      ELSE COALESCE(amtacctcr, 0)
	    END AS amtacctcr,
	    CASE 
	      WHEN p_positivebalance = 'Y' THEN 
	        (CASE WHEN ELEV.AccountType IN ('A', 'E') THEN 1 ELSE -1 END) * COALESCE(amtacctdr - amtacctcr, 0)
	      ELSE COALESCE(amtacctdr - amtacctcr, 0)
	    END AS amtacctsa,
	    CASE 
	      WHEN p_positivebalance = 'Y' THEN 
	        (CASE WHEN ELEV.AccountType IN ('A', 'E') THEN 1 ELSE -1 END) * COALESCE(closebalance, 0)
	      ELSE COALESCE(closebalance, 0)
	    END AS closebalance
	FROM (
	    SELECT DISTINCT ON (ad_client_id, Value) 
	        ad_client_id, C_Element_ID, C_ElementValue_ID, AccountType, value, name, description 
	    FROM c_elementValue
	    WHERE ad_client_id = p_ad_client_id AND isSummary = 'N'
	    ORDER BY ad_client_id, Value
	) AS ELEV
	LEFT JOIN (
	    SELECT adcli.AD_Client_ID, accsh.C_AcctSchema_ID, accee.C_Element_ID, accel.name as element_name, tree.AD_Tree_ID, tree.name as tree_name
	    FROM AD_Client adcli
	    LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
	    LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
	    LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
	    LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID = accel.AD_Tree_ID
	    WHERE accee.ElementType = 'AC' AND accsh.C_AcctSchema_ID = p_c_acctschema_id
	) AS ELE ON ELE.C_Element_ID = ELEV.C_Element_ID
	INNER JOIN (
	    SELECT DISTINCT ON (ORG12.ad_client_id, ORG12.ad_orgparent_id, ORG12.name)
	        ORG12.ad_client_id, 
	        ORG12.ad_org_id,
	        ORG12.ad_orgparent_id,
	        ORG12.issummary,
	        ORG12.value, 
	        ORG12.name, 
	        COALESCE(ORG12.description, ORG12.name, '') as description, 
	        COALESCE(ORG12.taxid, '?') as taxid, 
	        ORG12.binarydata as binarydata,
	        (SELECT STRING_AGG(DISTINCT ORGX.value, '-' ORDER BY ORGX.value) 
	         FROM OrgTree ORGX
	         WHERE ORGX.ad_client_id = ORG12.ad_client_id
	         AND (ORGX.ad_org_id = p_ad_org_id OR p_ad_org_id = 0 OR p_ad_org_id IS NULL)
	         AND (ORGX.ad_orgparent_id = p_ad_orgparent_id OR p_ad_orgparent_id = 0 OR p_ad_orgparent_id IS NULL)
	        ) AS all_orgs
	    FROM OrgTree AS ORG12
	) AS ORG ON (ORG.ad_client_id = ELEV.ad_client_id)
	LEFT JOIN c_acctschema sch ON sch.c_acctschema_id = p_c_acctschema_id
	LEFT JOIN c_currency curr1 ON sch.c_currency_id = curr1.c_currency_id
	LEFT JOIN c_currency_trl currt1 ON curr1.c_currency_id = currt1.c_currency_id 
	    AND currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID = p_ad_client_id)
	LEFT JOIN (
	    SELECT DISTINCT ON (rfl.value) rfl.value, COALESCE(rflt.name, rfl.name, '') as name 
	    FROM AD_Reference rfr
	    LEFT JOIN AD_Ref_List rfl ON rfl.ad_reference_id = rfr.ad_reference_id
	    LEFT JOIN AD_Ref_List_Trl rflt ON rflt.ad_ref_list_id = rfl.ad_ref_list_id 
	        AND rflt.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID = p_ad_client_id)
	    WHERE rfr.name LIKE '%Posting Type%'
	) AS posttype ON posttype.value = p_postingtype
	LEFT JOIN (
	    SELECT
	        ele.ad_client_id,
	        ele.c_elementvalue_id, 
	        ele.value,
	        org2.ad_org_id,
	        dateini,
	        dateend,
	        COALESCE(openbalance, 0) AS openbalance,
	        COALESCE(amtacctdr, 0) AS amtacctdr,
	        COALESCE(amtacctcr, 0) AS amtacctcr,
	        COALESCE(amtacctdr - amtacctcr, 0) AS amtacctsa,
	        COALESCE(closebalance, 0) AS closebalance
	    FROM c_elementvalue ele
	    INNER JOIN (
	        SELECT DISTINCT ON (ORG12.ad_client_id, ORG12.ad_orgparent_id, ORG12.name)
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
	            fas.account_id, 
	            fas.C_AcctSchema_ID, 
	            TO_CHAR(per.startdate, 'DD-MM-YYYY') AS dateini,
	            TO_CHAR(per.enddate, 'DD-MM-YYYY') AS dateend,
	            SUM(CASE WHEN (fas.DateAcct < per.StartDAte) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) AS openbalance,
	            SUM(CASE WHEN (fas.DateAcct BETWEEN per.StartDAte AND per.EndDate) THEN COALESCE(fas.amtacctdr, 0) ELSE 0 END) AS amtacctdr,
	            SUM(CASE WHEN (fas.DateAcct BETWEEN per.StartDAte AND per.EndDate) THEN COALESCE(fas.amtacctcr, 0) ELSE 0 END) AS amtacctcr,
	            SUM(CASE WHEN (fas.DateAcct <= per.EndDate) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) AS closebalance
	        FROM (
	            SELECT 
	                fa2.ad_client_id, fa2.ad_org_id, fa2.fact_acct_id,
	                fa2.c_acctschema_id, fa2.DateAcct, fa2.account_id, 
	                fa2.amtacctdr, fa2.amtacctcr
	            FROM fact_acct fa2
	            LEFT JOIN c_elementvalue el2 ON el2.c_elementvalue_id = fa2.account_id
	            WHERE fa2.c_acctschema_id = p_c_acctschema_id  
	            AND fa2.ad_client_id = p_ad_client_id
	            AND fa2.postingtype = p_postingtype
	        ) fas
	        INNER JOIN c_period per ON per.c_period_id = p_c_period_id
	        WHERE fas.DateAcct <= per.EndDate
	        GROUP BY fas.ad_client_id, fas.ad_org_id, fas.account_id, per.startdate, per.enddate, fas.C_AcctSchema_ID
	
	        UNION
	
	        SELECT 
	            p_ad_client_id AS ad_client_id, 
	            fas2.ad_org_id,
	            p_c_elementvalue_id AS account_id,
	            fas2.C_AcctSchema_ID, 
	            fas2.dateini,
	            fas2.dateend,
	            SUM(fas2.openbalance) AS openbalance,
	            SUM(fas2.amtacctdr) AS amtacctdr,
	            SUM(fas2.amtacctcr) AS amtacctcr,
	            SUM(fas2.closebalance) AS closebalance
	        FROM (
	            SELECT
	                fa22.ad_client_id,
	                fa22.ad_org_id,
	                fa22.account_id, fa22.C_AcctSchema_ID, 
	                TO_CHAR(per2.startdate, 'DD-MM-YYYY') AS dateini,
	                TO_CHAR(per2.enddate, 'DD-MM-YYYY') AS dateend,
	                SUM(CASE WHEN (fa22.DateAcct < per2.StartDAte) THEN (fa22.amtacctdr - fa22.amtacctcr) ELSE 0 END) AS openbalance,
	                SUM(CASE WHEN (fa22.DateAcct BETWEEN per2.StartDAte AND per2.EndDate) THEN COALESCE(fa22.amtacctdr, 0) ELSE 0 END) AS amtacctdr,
	                SUM(CASE WHEN (fa22.DateAcct BETWEEN per2.StartDAte AND per2.EndDate) THEN COALESCE(fa22.amtacctcr, 0) ELSE 0 END) AS amtacctcr,
	                SUM(CASE WHEN (fa22.DateAcct <= per2.EndDate) THEN (fa22.amtacctdr - fa22.amtacctcr) ELSE 0 END) AS closebalance
	            FROM fact_acct fa22
	            LEFT JOIN c_elementvalue el2 ON el2.c_elementvalue_id = fa22.account_id
	            INNER JOIN c_period per2 ON per2.c_period_id = p_c_period_id
	            WHERE fa22.c_acctschema_id = p_c_acctschema_id  
	            AND fa22.ad_client_id = p_ad_client_id
	            AND fa22.postingtype = p_postingtype
	            AND fa22.DateAcct <= per2.EndDate
	            GROUP BY fa22.ad_client_id, fa22.ad_org_id, fa22.account_id, per2.startdate, per2.enddate, fa22.C_AcctSchema_ID
	        ) fas2
	        LEFT JOIN C_ElementValue cel2 ON cel2.C_ElementValue_ID = p_c_elementvalue_id
	        WHERE openbalance <> 0 OR amtacctcr <> 0 OR amtacctdr <> 0 OR closebalance <> 0
	        GROUP BY fas2.ad_org_id, fas2.C_AcctSchema_ID, fas2.dateini, fas2.dateend
	    ) AS bal2 ON bal2.ad_client_id = ele.ad_client_id 
	             AND bal2.account_id = ele.c_elementvalue_id 
	             AND bal2.ad_org_id = org2.ad_org_id
	    WHERE ele.ad_client_id = p_ad_client_id  
	    AND ele.issummary = 'N' 
	    AND ele.AccountType = ANY(string_to_array(p_accounttype, ','))
	    ORDER BY ele.ad_client_id, bal2.ad_org_id, ele.c_elementvalue_id
	) AS bal ON bal.ad_client_id = ORG.ad_client_id 
	        AND bal.ad_org_id = ORG.ad_org_id 
	        AND bal.c_elementvalue_id = ELEV.c_elementvalue_id
	WHERE ELEV.ad_client_id = p_ad_client_id
	AND posttype.value = 'A'
	AND (
	    p_isshowzero = 'Y' 
	    OR (p_isshowzero = 'N' AND (
	        bal.openbalance <> 0 
	        OR bal.amtacctcr <> 0 
	        OR bal.amtacctdr <> 0 
	        OR bal.closebalance <> 0
	    ))
	)
	ORDER BY codigo ASC, org_value ASC;
;
END;
$$ LANGUAGE plpgsql;

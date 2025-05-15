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
    JOIN AD_Tree t2 ON child.AD_Tree_ID = t2.AD_Tree_ID
    JOIN AccountTreeBase parent ON child.Parent_ID = parent.Node_ID
    WHERE child.IsActive = 'Y'
        AND child_ev.IsActive = 'Y'
        AND t2.AD_tree_ID = (
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
	)
	SELECT * FROM AccountTreeBase
)
-- MAIN QUERY
--SELECT * FROM OrgTree
--UNION ALL
SELECT PAR.*  
FROM ElementValueTree PAR
INNER JOIN (
	SELECT adcli.AD_Client_ID, accsh.C_AcctSchema_ID, accee.C_Element_ID, accel.name as element_name, tree.AD_Tree_ID, tree.name as tree_name
	FROM AD_Client adcli
	LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
	LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
	LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
	LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
	WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID}
) as ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID
LEFT JOIN ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id)
LEFT JOIN ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
LEFT JOIN ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = PAR.NODE_ID
LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = PAR.Parent_ID
ORDER BY ELV.Value, PAR.ANCESTRY

-- AD_ORG 
-- OrgTree V1
WITH RECURSIVE OrgTree AS (
    -- Nodo raíz (organizaciones sin padre)
    SELECT 
        org.ad_client_id,
        org.ad_org_id,
        org.issummary,
        org.name,
        org.value, -- Agregamos el campo value
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
        org.value, -- Agregamos el campo value
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
)
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
    (SELECT STRING_AGG(DISTINCT ORGX.value, ' - ' ORDER BY ORGX.value) 
     FROM OrgTree ORGX
     WHERE ORGX.ad_client_id = $P{AD_Client_ID}
     AND ORGX.issummary='N' 
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
ORDER BY ORG12.ad_client_id, ORG12.node_id, ORG12.name;


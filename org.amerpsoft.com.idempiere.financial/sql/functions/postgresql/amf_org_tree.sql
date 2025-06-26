
-- Function amf_element_value_balance
-- Devueve el Balance de una cuenta de una organizacion en un periodo dado

DROP FUNCTION IF EXISTS amf_org_tree(INTEGER, INTEGER, INTEGER);

CREATE OR REPLACE FUNCTION amf_org_tree(
    p_ad_client_id INTEGER,
    p_ad_org_id INTEGER DEFAULT 0,
    p_ad_orgparent_id INTEGER DEFAULT 0
)
RETURNS TABLE (
    org_ad_client_id NUMERIC,
    org_ad_org_id NUMERIC,
    org_value VARCHAR,
    org_description TEXT,
    org_name VARCHAR,
    all_orgs TEXT,
    org_taxid VARCHAR,
    org_logo BYTEA
) AS $$
BEGIN
RETURN QUERY
WITH OrgTree AS (
    WITH RECURSIVE OrgTreeBase AS (
        SELECT 
 			org.ad_client_id AS ad_client_id,
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
            org.ad_client_id AS ad_client_id,
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
        (
            SELECT STRING_AGG(DISTINCT ORGX.value, ' - ' ORDER BY ORGX.value) 
            FROM OrgTreeBase ORGX
            WHERE ORGX.issummary = 'N'
        ) AS all_orgs
    FROM OrgTreeBase AS orgtb
    WHERE orgtb.ad_client_id = p_ad_client_id
    AND orgtb.issummary = 'N'
	AND (COALESCE(p_ad_org_id, 0) = 0 OR orgtb.ad_org_id = p_ad_org_id)
	AND (COALESCE(p_ad_orgparent_id, 0) = 0 OR orgtb.parent_id = p_ad_orgparent_id)
    ORDER BY ad_client_id, value
)

SELECT
    ORG.ad_client_id,
    ORG.ad_org_id  AS ad_org_id,
    ORG.value AS org_value,
    ORG.description::TEXT AS org_description,
    CASE 
        WHEN p_ad_org_id = 0 THEN 'Consolidado' 
        ELSE ORG.name 
    END AS org_name,
    ORG.all_orgs,
    ORG.taxid AS org_taxid,
    ORG.binarydata AS org_logo
FROM (
    SELECT DISTINCT ON (ORG12.ad_client_id, ORG12.ad_orgparent_id, ORG12.name)
        ORG12.ad_client_id, 
        ORG12.ad_org_id,
        ORG12.ad_orgparent_id,
        ORG12.issummary,
        ORG12.value, 
        ORG12.name, 
        COALESCE(ORG12.description, ORG12.name, '') AS description, 
        COALESCE(ORG12.taxid, 'NA') AS taxid, 
        ORG12.binarydata AS binarydata,
        (
            SELECT STRING_AGG(DISTINCT ORGX.value, '-' ORDER BY ORGX.value) 
            FROM OrgTree ORGX
            WHERE ORGX.ad_client_id = ORG12.ad_client_id
            AND (ORGX.ad_org_id = p_ad_org_id OR p_ad_org_id = 0)
            AND (ORGX.ad_orgparent_id = p_ad_orgparent_id OR p_ad_orgparent_id = 0)
        ) AS all_orgs
    FROM OrgTree AS ORG12
) AS ORG;
END;
$$ LANGUAGE plpgsql;


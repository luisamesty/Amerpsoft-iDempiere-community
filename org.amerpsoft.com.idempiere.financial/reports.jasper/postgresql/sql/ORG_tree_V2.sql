-- AD_ORG
-- NOT PARENT 
-- V2
SELECT DISTINCT ON  (ORG1.ad_client_id, NODE.parent_id, ORG1.name)
    ORG1.ad_client_id, 
    ORG1.ad_org_id, 
    ORG1.name,
    (SELECT STRING_AGG(DISTINCT ORGA.value, ' - ' ORDER BY ORGA.value)
     FROM ad_treenode NODEA
	JOIN ad_tree TREEA ON NODEA.ad_tree_id = TREEA.ad_tree_id
	JOIN ad_org ORGA ON NODEA.node_id = ORGA.ad_org_id
     WHERE ORGA.ad_client_id = $P{AD_Client_ID}
     AND ORGA.issummary='N' 
     AND (ORGA.ad_org_id = $P{AD_Org_ID} OR $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL)
     AND (NODEA.parent_id = $P{AD_OrgParent_ID} OR $P{AD_OrgParent_ID} = 0 OR $P{AD_OrgParent_ID} IS NULL)
    ) AS all_orgs
FROM ad_treenode NODE
JOIN ad_tree TREE ON NODE.ad_tree_id = TREE.ad_tree_id
JOIN ad_org ORG1 ON NODE.node_id = ORG1.ad_org_id
LEFT JOIN ad_orginfo ORF1 ON ORG1.ad_org_id = ORF1.ad_org_id
LEFT JOIN ad_image IMB1 ON ORF1.logo_id = IMB1.ad_image_id
WHERE TREE.treetype = 'OO' -- Árbol de organizaciones
	AND ORG1.isSummary='N'
	AND ORG1.ad_client_id = $P{AD_Client_ID}
	AND CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID}=ORG1.ad_org_id OR $P{AD_Org_ID} IS NULL THEN 1=1 ELSE 1=0 END
	AND CASE WHEN $P{AD_OrgParent_ID}=NODE.parent_id OR $P{AD_OrgParent_ID} = 0 OR $P{AD_OrgParent_ID} IS NULL THEN 1=1 ELSE 1=0 END
ORDER BY ORG1.ad_client_id, NODE.parent_id, ORG1.name;



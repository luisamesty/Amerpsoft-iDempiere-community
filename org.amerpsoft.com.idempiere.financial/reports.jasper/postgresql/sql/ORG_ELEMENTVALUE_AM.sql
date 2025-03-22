-- UNION ALL 
-- OrgTree - ElementValueTree
-- PRUEBA COMBINADA
WITH RECURSIVE OrgTree AS (
    SELECT org.ad_client_id, org.ad_org_id AS node_id, org.name, 
    COALESCE(org.description, org.name, '') AS description, 
    COALESCE(info.taxid, '?') AS taxid, 
    img.binarydata AS binarydata, 
    'ORG' AS node_type, 
    tree.ad_tree_id, node.node_id AS tree_node_id, 0 AS level, node.parent_id, 
    ARRAY [node.node_id::text] AS ancestry
    FROM ad_treenode node
    JOIN ad_tree tree ON node.ad_tree_id = tree.ad_tree_id
    JOIN ad_org org ON node.node_id = org.ad_org_id
    LEFT JOIN ad_orginfo info ON org.ad_org_id = info.ad_org_id
    LEFT JOIN ad_image img ON info.logo_id = img.ad_image_id
    WHERE tree.treetype = 'OO' AND node.parent_id = 0 AND org.ad_client_id = $P{AD_Client_ID}
    UNION ALL
    SELECT org.ad_client_id, org.ad_org_id AS node_id, org.name, 
    COALESCE(org.description, org.name, '') AS description, 
    COALESCE(info.taxid, '?') AS taxid, 
    img.binarydata AS binarydata, 
    'ORG' AS node_type, 
    parent.ad_tree_id, node.node_id AS tree_node_id, parent.level + 1 AS level, node.parent_id, 
    parent.ancestry || ARRAY [node.node_id::text] AS ancestry
    FROM ad_treenode node
    JOIN OrgTree parent ON node.parent_id = parent.tree_node_id
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
--SELECT * FROM OrgTree
--UNION ALL
SELECT elvt.*  FROM ElementValueTree elvt
INNER JOIN OrgTree orgt ON orgt.ad_client_id = elvt.ad_client_id
ORDER BY elvt.value;

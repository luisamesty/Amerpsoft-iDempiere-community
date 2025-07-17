-- Funcion amf_element_value_tree_extended
-- Devuelve el arreglo de cuenta en forma de arbol con su relacion completa de hijos en cada cuenta
--
DROP FUNCTION IF EXISTS amf_element_value_tree_extended(NUMERIC, NUMERIC);

CREATE OR REPLACE FUNCTION amf_element_value_tree_extended(
    p_ad_client_id NUMERIC,
    p_c_acctschema_id NUMERIC
)
RETURNS TABLE (
    level INTEGER,
    node_id NUMERIC,
    parent_id NUMERIC,
    c_element_id NUMERIC,
    c_elementvalue_id NUMERIC,
    ad_client_id NUMERIC,
    ad_org_id NUMERIC,
    isactive CHAR(1),
    value VARCHAR,
    name VARCHAR,
    description TEXT,
    length INTEGER,
    accounttype CHAR(1),
    accountsign CHAR(1),
    isdoccontrolled CHAR(1),
    issummary CHAR(1),
    acctparent VARCHAR[],
	pathel VARCHAR,
	ancestry NUMERIC[],
    -- Nivel 0
    codigo0 VARCHAR,
    name0 VARCHAR,
    description0 TEXT,
    issummary0 CHAR(1),
    -- Nivel 1
    codigo1 VARCHAR,
    name1 VARCHAR,
    description1 TEXT,
    issummary1 CHAR(1),
    -- Nivel 2
    codigo2 VARCHAR,
    name2 VARCHAR,
    description2 TEXT,
    issummary2 CHAR(1),
    -- Nivel 3
    codigo3 VARCHAR,
    name3 VARCHAR,
    description3 TEXT,
    issummary3 CHAR(1),
    -- Nivel 4
    codigo4 VARCHAR,
    name4 VARCHAR,
    description4 TEXT,
    issummary4 CHAR(1),
    -- Nivel 5
    codigo5 VARCHAR,
    name5 VARCHAR,
    description5 TEXT,
    issummary5 CHAR(1),
    -- Nivel 6
    codigo6 VARCHAR,
    name6 VARCHAR,
    description6 TEXT,
    issummary6 CHAR(1),
    -- Nivel 7
    codigo7 VARCHAR,
    name7 VARCHAR,
    description7 TEXT,
    issummary7 CHAR(1),
    -- Nivel 8
    codigo8 VARCHAR,
    name8 VARCHAR,
    description8 TEXT,
    issummary8 CHAR(1),
    -- Nivel 9
    codigo9 VARCHAR,
    name9 VARCHAR,
    description9 TEXT,
    issummary9 CHAR(1)
) AS $$
BEGIN
RETURN QUERY
WITH RECURSIVE AccountTreeBase AS (
    SELECT 
        tn.AD_Client_ID,
        tn.AD_Tree_ID,
        tn.Node_ID,
        tn.Parent_ID,
        tn.SeqNo,
        0 AS level,
        ev.Value::VARCHAR(2000) AS path,
        ev.Value::VARCHAR(40) AS AccountValue,
        ev.Name::VARCHAR(60) AS AccountName,
        ev.AccountType::VARCHAR(1) AS AccountType,
        ev.IsSummary::VARCHAR(1) as IsSummary,
        t.Name::VARCHAR(60) AS TreeName,
        ARRAY[tn.Node_ID::NUMERIC] AS ancestry,
        ARRAY[ev.value::VARCHAR] AS acctparent
    FROM AD_TreeNode tn
    JOIN C_ElementValue ev ON tn.Node_ID = ev.C_ElementValue_ID
    JOIN AD_Tree t ON tn.AD_Tree_ID = t.AD_Tree_ID
    WHERE (tn.Parent_ID IS NULL OR tn.Parent_ID = 0)
        AND tn.AD_Client_ID = p_ad_client_id
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
              AND accsh.C_AcctSchema_ID = p_c_acctschema_id 
              AND adcli.AD_Client_ID = p_ad_client_id
        )
    UNION ALL
    SELECT 
        child.AD_Client_ID,
        child.AD_Tree_ID,
        child.Node_ID,
        child.Parent_ID,
        child.SeqNo,
        parent.level + 1,
        (parent.path || '->' || child_ev.Value)::VARCHAR(2000),
        child_ev.Value::VARCHAR(40),
        child_ev.Name::VARCHAR(60),
        child_ev.AccountType::VARCHAR(1),
        child_ev.IsSummary::VARCHAR(1) as IsSummary,
        parent.TreeName::VARCHAR(60) as TreeName,
		parent.ancestry || child.Node_ID::NUMERIC AS ancestry,
        parent.acctparent || child_ev.value::VARCHAR AS acctparent
    FROM AD_TreeNode child
    JOIN C_ElementValue child_ev ON child.Node_ID = child_ev.C_ElementValue_ID
    JOIN AccountTreeBase parent ON child.Parent_ID = parent.Node_ID
    WHERE child.IsActive = 'Y'
        AND child_ev.IsActive = 'Y'
),
ElementValueTree AS (
    SELECT * FROM AccountTreeBase
)
SELECT DISTINCT ON (ELV.Value)
    PAR.Level,
    PAR.Node_ID, 
    PAR.Parent_ID,
    ELE.c_element_id,
    ELV.c_elementvalue_id,
    ELV.ad_client_id,
    ELV.ad_org_id,
    ELV.isactive,
    ELV.value,
    COALESCE(ELV.name, ''),
    LPAD('', char_length(ELV.value), ' ') || COALESCE(ELV.description, ELV.name),
    char_length(ELV.value),
    ELV.accounttype,
    ELV.accountsign,
    ELV.isdoccontrolled,
    ELV.issummary,
	PAR.acctparent,
	PAR.path as pathel,
	PAR.Ancestry,
	-- ELVN(0-9)
	COALESCE(ELVN0.value,'') as codigo0,
	COALESCE(ELVN0.name,'') as name0,
	ELVN0.description::TEXT as description0,
	ELVN0.issummary as issummary0,
	COALESCE(ELVN1.value,'') as codigo1,
	COALESCE(ELVN1.name,'') as name1,
	ELVN1.description::TEXT as description1,
	ELVN1.issummary as issummary1,
	CASE WHEN ELVN1.value <> '' THEN ELVN2.value ELSE '' END as codigo2,
	ELVN2.name as name2,
	ELVN2.description::TEXT as description2,
	ELVN2.issummary as issummary2,
	CASE WHEN ELVN1.value <> '' THEN ELVN3.value ELSE '' END as codigo3,
	ELVN3.name as name3,
	ELVN3.description::TEXT as description3,
	ELVN3.issummary as issummary3,
	CASE WHEN ELVN1.value <> '' THEN ELVN4.value ELSE '' END as codigo4,
	ELVN4.name as name4,
	ELVN4.description::TEXT as description4,
	ELVN4.issummary as issummary4,
	CASE WHEN ELVN1.value <> '' THEN ELVN5.value ELSE '' END as codigo5,
	ELVN5.name as name5,
	ELVN5.description::TEXT as description5,
	ELVN5.issummary as issummary5,
	CASE WHEN ELVN1.value <> '' THEN ELVN6.value ELSE '' END as codigo6,
	ELVN6.name as name6,
	ELVN6.description::TEXT as description6,
	ELVN6.issummary as issummary6,
	CASE WHEN ELVN1.value <> '' THEN ELVN7.value ELSE '' END as codigo7,
	ELVN7.name as name7,
	ELVN7.description::TEXT as description7,
	ELVN7.issummary as issummary7,
	CASE WHEN ELVN1.value <> '' THEN ELVN8.value ELSE '' END as codigo8,
	ELVN8.name as name8,
	ELVN8.description::TEXT as description8,
	ELVN8.issummary as issummary8,	
	CASE WHEN ELVN1.value <> '' THEN ELVN9.value ELSE '' END as codigo9,
	ELVN9.name as name9,
	ELVN9.description::TEXT as description9,
	ELVN9.issummary as issummary9
FROM ElementValueTree PAR
INNER JOIN (
    SELECT adcli.AD_Client_ID, accsh.C_AcctSchema_ID, accee.C_Element_ID, accel.name AS element_name, tree.AD_Tree_ID, tree.name AS tree_name
    FROM AD_Client adcli
    LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
    LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
    LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
    LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
    WHERE accee.ElementType = 'AC' AND accsh.C_AcctSchema_ID = p_c_acctschema_id
) AS ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID
LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = PAR.NODE_ID
LEFT JOIN C_ElementValue ELVP ON ELVP.C_ElementValue_ID = PAR.Parent_ID
LEFT JOIN C_ElementValue AS ELVN0 ON (ELVN0.Value = PAR.acctparent[1] AND ELVN0.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN1 ON (ELVN1.Value = PAR.acctparent[2] AND ELVN1.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN2 ON (ELVN2.Value = PAR.acctparent[3] AND ELVN2.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN3 ON (ELVN3.Value = PAR.acctparent[4] AND ELVN3.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN4 ON (ELVN4.Value = PAR.acctparent[5] AND ELVN4.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN5 ON (ELVN5.Value = PAR.acctparent[6] AND ELVN5.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN6 ON (ELVN6.Value = PAR.acctparent[7] AND ELVN6.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN7 ON (ELVN7.Value = PAR.acctparent[8] AND ELVN7.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN8 ON (ELVN8.Value = PAR.acctparent[9] AND ELVN8.AD_Client_ID = PAR.AD_Client_ID)
LEFT JOIN C_ElementValue AS ELVN9 ON (ELVN9.Value = PAR.acctparent[10] AND ELVN9.AD_Client_ID = PAR.AD_Client_ID)
ORDER BY ELV.Value, PAR.ancestry;
END;
$$ LANGUAGE plpgsql;




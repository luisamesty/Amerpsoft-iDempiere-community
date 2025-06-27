-- amf_element_value_tree_basic
--
DROP FUNCTION IF EXISTS amf_element_value_tree_basic(NUMERIC, NUMERIC);

CREATE OR REPLACE FUNCTION amf_element_value_tree_basic(
    p_ad_client_id NUMERIC,
    p_c_acctschema_id NUMERIC
)
RETURNS TABLE (
	pathel VARCHAR,
	ancestry NUMERIC[],
    level INTEGER,
    node_id NUMERIC,
    parent_id NUMERIC,
    c_element_id NUMERIC,
    c_elementvalue_id NUMERIC,
    ad_client_id NUMERIC,
    isactive CHAR(1),
    value VARCHAR,
    name VARCHAR,
    description TEXT,
    length INTEGER,
    accounttype CHAR(1),
    accountsign CHAR(1),
    isdoccontrolled CHAR(1),
    element_c_element_id NUMERIC,
    issummary CHAR(1),
    acctparent VARCHAR[]
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
PAR.path as pathel,
PAR.Ancestry,
    PAR.Level,
    PAR.Node_ID, 
    PAR.Parent_ID,
    ELE.c_element_id,
    ELV.c_elementvalue_id,
    ELV.ad_client_id,
    ELV.isactive,
    ELV.value,
    COALESCE(ELV.name, ''),
    LPAD('', char_length(ELV.value), ' ') || COALESCE(ELV.description, ELV.name),
    char_length(ELV.value),
    ELV.accounttype,
    ELV.accountsign,
    ELV.isdoccontrolled,
    ELV.c_element_id,
    ELV.issummary,
	PAR.acctparent
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
ORDER BY ELV.Value, PAR.ancestry;
END;
$$ LANGUAGE plpgsql;



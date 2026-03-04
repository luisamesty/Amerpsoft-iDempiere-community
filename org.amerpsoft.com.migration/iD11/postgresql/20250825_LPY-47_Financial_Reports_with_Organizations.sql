-- Function amf_org_tree
-- Devueve Las Organizaciones
-- Caso 1: p_ad_orgparent_id NO es nulo ni cero
-- 	Subcaso A: p_ad_org_id es nulo o 0
-- 	→ Devuelve todas las organizaciones hijas recursivas de p_ad_orgparent_id.
-- 	Subcaso B: p_ad_org_id es válida
-- 	Si es descendiente de p_ad_orgparent_id
-- 	→ Devuélvela sola.
-- 	Si NO es hija
-- 	→ Devuelve vacío.
-- Caso 2: p_ad_orgparent_id es nulo o cero
-- 	Subcaso A: p_ad_org_id es nulo o 0
-- 	→ Devuelve todas las organizaciones del cliente.
-- 	Subcaso B: p_ad_org_id es válida
-- 	→ Devuélvela sola.

DROP FUNCTION IF EXISTS amf_org_tree(NUMERIC, NUMERIC, NUMERIC);

CREATE OR REPLACE FUNCTION amf_org_tree(
    p_ad_client_id NUMERIC,
    p_ad_org_id NUMERIC DEFAULT 0,
    p_ad_orgparent_id NUMERIC DEFAULT 0
)
RETURNS TABLE (
    org_ad_client_id NUMERIC,
    org_ad_org_id NUMERIC,
    org_ad_orgparent_id NUMERIC,
    issummary CHAR(1),
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
            org.issummary AS org_issummary,
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
            org.issummary AS org_issummary,
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
        org_issummary AS issummary,
        value,
        name,
        description, 
        taxid, 
        binarydata,
        (
            SELECT STRING_AGG(DISTINCT ORGX.value, ' - ' ORDER BY ORGX.value) 
            FROM OrgTreeBase ORGX
            WHERE ORGX.org_issummary = 'N'
        ) AS all_orgs
    FROM OrgTreeBase AS orgtb
    WHERE orgtb.ad_client_id = p_ad_client_id
      AND (COALESCE(p_ad_org_id, 0) = 0 OR orgtb.ad_org_id = p_ad_org_id)
      AND (COALESCE(p_ad_orgparent_id, 0) = 0 OR orgtb.parent_id = p_ad_orgparent_id)
    ORDER BY ad_client_id, value
)
SELECT
    ORG.ad_client_id,
    ORG.ad_org_id,
    ORG.ad_orgparent_id,
    ORG.issummary,
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
        ) AS all_orgs
    FROM OrgTree AS ORG12
) AS ORG;
END;
$$ LANGUAGE plpgsql;


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
    codigo VARCHAR,
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
    ELV.value as codigo,
    COALESCE(ELV.name, '') as name,
    LPAD('', char_length(ELV.value), ' ') || COALESCE(ELV.description, ELV.name) as description,
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
    isactive CHAR(1),
    codigo VARCHAR,
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
    ELV.isactive,
    ELV.value codigo,
    COALESCE(ELV.name, '') as name,
    LPAD('', char_length(ELV.value), ' ') || COALESCE(ELV.description, ELV.name) as description,
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



-- Function amf_balance_account_org
-- Devuelve el balance en un periodo dado de una cuenta y una Organizacion
-- Si no tiene movimiento devuelve un registro con cero

DROP FUNCTION IF EXISTS amf_balance_account_org(
    NUMERIC, NUMERIC, NUMERIC, NUMERIC, VARCHAR, NUMERIC
);

CREATE OR REPLACE FUNCTION amf_balance_account_org(
    p_ad_client_id NUMERIC,
    p_ad_org_id NUMERIC,
    p_c_acctschema_id NUMERIC,
    p_c_period_id NUMERIC,
    p_postingtype VARCHAR,
    p_c_elementvalue_id NUMERIC
)
RETURNS TABLE (
    bal_c_elementvalue_id NUMERIC,
    account_code VARCHAR,
    account_name VARCHAR,
    ad_org_id NUMERIC,
    dateini TEXT,
    dateend TEXT,
    openbalance NUMERIC,
    amtacctdr NUMERIC,
    amtacctcr NUMERIC,
    closebalance NUMERIC,
    amtacctsa NUMERIC
) AS $$
BEGIN
RETURN QUERY
SELECT
    ev.c_elementvalue_id,
    ev.value,
    ev.name,
    CAST(fa.ad_org_id AS NUMERIC),
    to_char(per.startdate, 'DD-MM-YYYY'),
    to_char(per.enddate, 'DD-MM-YYYY'),
    COALESCE(SUM(CASE WHEN fa.DateAcct < per.startdate THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN per.startdate AND per.enddate THEN fa.amtacctdr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN per.startdate AND per.enddate THEN fa.amtacctcr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct <= per.enddate THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN per.startdate AND per.enddate THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0)
FROM c_elementvalue ev
JOIN c_period per ON per.c_period_id = p_c_period_id
LEFT JOIN fact_acct fa ON fa.account_id = ev.c_elementvalue_id
    AND fa.ad_client_id = p_ad_client_id
    AND fa.c_acctschema_id = p_c_acctschema_id
    AND (p_ad_org_id IS NULL OR fa.ad_org_id = p_ad_org_id)
    AND fa.dateacct <= per.enddate
    AND fa.postingtype = p_postingtype
WHERE ev.ad_client_id = p_ad_client_id
  AND ( (p_c_elementvalue_id IS NULL AND ev.issummary = 'N')
        OR ev.c_elementvalue_id = p_c_elementvalue_id  )
  AND CAST(fa.ad_org_id AS NUMERIC) IS NOT NULL
GROUP BY ev.c_elementvalue_id, ev.value, ev.name, fa.ad_org_id, per.startdate, per.enddate;
END;
$$ LANGUAGE plpgsql;

-- Function amf_balance_account_org_flex
-- Devuelve los balances de cuentas en un periodo o rango de fechas dado de una cuenta y una Organizacion
-- Si no tiene movimiento devuelve un registro con cero

DROP FUNCTION IF EXISTS amf_balance_account_org_flex(
    NUMERIC, NUMERIC, NUMERIC, NUMERIC, VARCHAR, NUMERIC, TIMESTAMP, TIMESTAMP
);

CREATE OR REPLACE FUNCTION adempiere.amf_balance_account_org_flex(
    p_ad_client_id numeric,
    p_ad_org_id numeric,
    p_c_acctschema_id numeric,
    p_c_period_id numeric,
    p_postingtype character varying,
    p_c_elementvalue_id numeric,
    p_datefrom timestamp without time zone DEFAULT NULL,
    p_dateto timestamp without time zone DEFAULT NULL
)
RETURNS TABLE(
    bal_c_elementvalue_id numeric,
    account_code character varying,
    account_name character varying,
    ad_org_id numeric,
    dateini text,
    dateend text,
    openbalance numeric,
    amtacctdr numeric,
    amtacctcr numeric,
    closebalance numeric,
    amtacctsa numeric
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_startdate date;
    v_enddate date;
BEGIN
    -- Validaciones
    IF p_c_period_id IS NOT NULL THEN
        SELECT startdate, enddate INTO v_startdate, v_enddate
        FROM c_period WHERE c_period_id = p_c_period_id;

        IF p_datefrom IS NOT NULL AND p_datefrom::date < v_startdate THEN
            RAISE EXCEPTION 'DateFrom % no puede ser menor que inicio del periodo %', p_datefrom, v_startdate;
        END IF;

        IF p_dateto IS NOT NULL AND p_dateto::date > v_enddate THEN
            RAISE EXCEPTION 'DateTo % no puede ser mayor que fin del periodo %', p_dateto, v_enddate;
        END IF;
    END IF;

    IF p_datefrom IS NOT NULL AND p_dateto IS NOT NULL THEN
        IF p_dateto < p_datefrom THEN
            RAISE EXCEPTION 'DateTo % no puede ser menor que DateFrom %', p_dateto, p_datefrom;
        END IF;
    END IF;

    IF p_c_period_id IS NOT NULL THEN
        IF p_datefrom IS NULL THEN
            p_datefrom := v_startdate::timestamp;
        END IF;
        IF p_dateto IS NULL THEN
            p_dateto := v_enddate::timestamp;
        END IF;
    END IF;

    RETURN QUERY
    SELECT
        ev.c_elementvalue_id,
        ev.value,
        ev.name,
        CAST(fa.ad_org_id AS NUMERIC),
        to_char(p_datefrom, 'DD-MM-YYYY'),
        to_char(p_dateto, 'DD-MM-YYYY'),
        COALESCE(SUM(CASE WHEN fa.DateAcct < p_datefrom THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN p_datefrom AND p_dateto THEN fa.amtacctdr ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN p_datefrom AND p_dateto THEN fa.amtacctcr ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN fa.DateAcct <= p_dateto THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN p_datefrom AND p_dateto THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0)
    FROM c_elementvalue ev
    LEFT JOIN fact_acct fa ON fa.account_id = ev.c_elementvalue_id
        AND fa.ad_client_id = p_ad_client_id
        AND fa.c_acctschema_id = p_c_acctschema_id
        AND (p_ad_org_id IS NULL OR fa.ad_org_id = p_ad_org_id)
        AND fa.dateacct <= p_dateto
        AND fa.postingtype = p_postingtype
    WHERE ev.ad_client_id = p_ad_client_id
      AND ( (p_c_elementvalue_id IS NULL AND ev.issummary = 'N')
            OR ev.c_elementvalue_id = p_c_elementvalue_id )
      AND CAST(fa.ad_org_id AS NUMERIC) IS NOT NULL
    GROUP BY ev.c_elementvalue_id, ev.value, ev.name, fa.ad_org_id;

END;
$$;


-- Function amf_balance_account_org_flex
-- Devuelve los balances de cuentas en un periodo o rango de fechas dado de una cuenta y una Organizacion
-- Si no tiene movimiento devuelve un registro con cero
-- Si p_ad_orgparent_id es NULL o 0, se comporta igual que amf_balance_account_org_flex(...).
-- Si p_ad_orgparent_id tiene valor, entonces se ignora p_ad_org_id, se invoca amf_org_tree(...) y se obtiene una lista de organizaciones hijas.
-- Se llama internamente a amf_balance_account_org_flex(...) por cada ad_org_id retornado por amf_org_tree(...) y se combinan los resultados (tipo UNION ALL).

DROP FUNCTION adempiere.amf_balance_account_org_flex_orgparent(numeric, numeric, numeric, numeric, numeric, character varying, numeric, timestamp, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amf_balance_account_org_flex_orgparent(
p_ad_client_id numeric, 
p_ad_orgparent_id numeric, 
p_ad_org_id numeric, 
p_c_acctschema_id numeric, 
p_c_period_id numeric,
p_postingtype character varying,
p_account_id numeric, 
p_datefrom timestamp without time zone,
p_dateto timestamp without time zone)
 RETURNS TABLE(
    bal_c_elementvalue_id numeric,
    account_code character varying,
    account_name character varying,
    ad_org_id numeric,
    dateini text,
    dateend text,
    openbalance numeric,
    amtacctdr numeric,
    amtacctcr numeric,
    closebalance numeric,
    amtacctsa numeric
)
 LANGUAGE plpgsql
AS $function$
DECLARE
    rec_org RECORD;
BEGIN
	IF p_ad_orgparent_id IS NULL OR p_ad_orgparent_id = 0 THEN
	    RETURN QUERY
	    SELECT *
	    FROM adempiere.amf_balance_account_org_flex(
            p_ad_client_id,
            p_ad_org_id,
            p_c_acctschema_id,
	    p_c_period_id, 
	    p_postingtype,
            p_account_id,
            p_datefrom,
            p_dateto
	    );
	ELSE
	    IF p_ad_org_id IS NULL OR p_ad_org_id = 0 THEN
	        -- Devuelve todas las organizaciones hijas de p_ad_orgparent_id
	        FOR rec_org IN
	            SELECT org_ad_org_id
	            FROM adempiere.amf_org_tree(p_ad_client_id, 0, p_ad_orgparent_id)
	        LOOP
	            RETURN QUERY
	            SELECT *
	            FROM adempiere.amf_balance_account_org_flex(
			    p_ad_client_id,
			    rec_org.org_ad_org_id,
			    p_c_acctschema_id,
			    p_c_period_id, 
			    p_postingtype,
			    p_account_id,
			    p_datefrom,
			    p_dateto	                
	            );
	        END LOOP;
	    ELSE
	        -- Devuelve solo p_ad_org_id si es hija (está en el árbol)
	        PERFORM 1
	        FROM adempiere.amf_org_tree(p_ad_client_id, 0, p_ad_orgparent_id)
	        WHERE org_ad_org_id = p_ad_org_id
	        LIMIT 1;
	        
	        IF FOUND THEN
	            RETURN QUERY
	            SELECT *
	            FROM adempiere.amf_balance_account_org_flex(
			    p_ad_client_id,
			    p_ad_org_id,
			    p_c_acctschema_id,
			    p_c_period_id, 
			    p_postingtype,
			    p_account_id,
			    p_datefrom,
			    p_dateto
	            );
	        ELSE
	            -- No es hija, devuelve vacío (no hace RETURN QUERY)
	            RETURN;
	        END IF;
	    END IF;
	END IF;
END;
$function$
;

-- Function amf_balance_account_org_by_dates
-- Devuelve el balance en rango de fechas dado de una cuenta y una Organizacion
-- Si no tiene movimiento devuelve un registro con cero

DROP FUNCTION IF EXISTS amf_balance_account_org_by_dates(
    NUMERIC, NUMERIC, NUMERIC, VARCHAR, NUMERIC, TIMESTAMP, TIMESTAMP
);


CREATE OR REPLACE FUNCTION adempiere.amf_balance_account_org_by_dates(
    p_ad_client_id numeric,
    p_ad_org_id numeric,
    p_c_acctschema_id numeric,
    p_postingtype character varying,
    p_c_elementvalue_id numeric,
    p_datefrom timestamp without time zone,
    p_dateto timestamp without time zone
)
RETURNS TABLE(
    bal_c_elementvalue_id numeric,
    account_code character varying,
    account_name character varying,
    ad_org_id numeric,
    dateini text,
    dateend text,
    openbalance numeric,
    amtacctdr numeric,
    amtacctcr numeric,
    closebalance numeric,
    amtacctsa numeric
)
LANGUAGE plpgsql
AS $function$
BEGIN
RETURN QUERY
SELECT
    ev.c_elementvalue_id,
    ev.value,
    ev.name,
    CAST(fa.ad_org_id AS NUMERIC),
    to_char(p_datefrom, 'DD-MM-YYYY'),
    to_char(p_dateto, 'DD-MM-YYYY'),
    COALESCE(SUM(CASE WHEN fa.DateAcct < p_datefrom THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN p_datefrom AND p_dateto THEN fa.amtacctdr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN p_datefrom AND p_dateto THEN fa.amtacctcr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct <= p_dateto THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0),
    COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN p_datefrom AND p_dateto THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0)
FROM c_elementvalue ev
LEFT JOIN fact_acct fa ON fa.account_id = ev.c_elementvalue_id
    AND fa.ad_client_id = p_ad_client_id
    AND fa.c_acctschema_id = p_c_acctschema_id
    AND (p_ad_org_id IS NULL OR fa.ad_org_id = p_ad_org_id)
    AND fa.dateacct <= p_dateto
    AND fa.postingtype = p_postingtype
WHERE ev.ad_client_id = p_ad_client_id
  AND ( (p_c_elementvalue_id IS NULL AND ev.issummary = 'N')
        OR ev.c_elementvalue_id = p_c_elementvalue_id )
  AND CAST(fa.ad_org_id AS NUMERIC) IS NOT NULL
GROUP BY ev.c_elementvalue_id, ev.value, ev.name, fa.ad_org_id;
END;
$function$;




-- ACCOUNT ELEMENTS
-- FROM DEfault AD_Tree FROM A GIVEN AD_Client_ID and C_AcctSchema_ID
-- FOR NEW ACCOUNTING REPORTS
-- Version 3
WITH ElementValueTree AS (
    -- Lógica para la jerarquía de cuentas contables
    WITH RECURSIVE AccountTreeBase AS (
    -- Nodos raíz (con conversión explícita de tipos)
    SELECT 
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
-- VAriables FOR REPORT
SELECT DISTINCT ON (ELV.Value)
	PAR.Level,
	PAR.Node_ID, 
	PAR.Parent_ID ,
	CLI.value as clivalue,
	CLI.name as cliname,
	coalesce(CLI.description,CLI.name,'') as clidescription,
	IMG.binarydata as cli_logo,
	ELE.c_element_id,
	ELV.c_elementvalue_id,
	ELV.ad_client_id,
	ELV.ad_org_id,
	ELV.isactive,
	ELV."value",
	COALESCE(ELV.name,'') as name,
	LPAD('', char_length(ELV.value),' ') || COALESCE(ELV.description,ELV.name) as description,
	char_length(ELV.value) as length,
	ELV.accounttype,
	ELV.accountsign,
	ELV.isdoccontrolled,
	ELV.c_element_id,
	ELV.issummary,
	COALESCE(ELVP.value,'') as value_parent,
	COALESCE(PAR.acctparent[2],'') as Value1,
	COALESCE(PAR.acctparent[3],'') as Value2,
	COALESCE(PAR.acctparent[4],'') as Value3,
	COALESCE(PAR.acctparent[5],'') as Value4,
	COALESCE(PAR.acctparent[6],'') as Value5,	
	COALESCE(PAR.acctparent[7],'') as Value6,
	COALESCE(PAR.acctparent[8],'') as Value7,
	COALESCE(PAR.acctparent[9],'') as Value8,
	COALESCE(PAR.acctparent[10],'') as Value9
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
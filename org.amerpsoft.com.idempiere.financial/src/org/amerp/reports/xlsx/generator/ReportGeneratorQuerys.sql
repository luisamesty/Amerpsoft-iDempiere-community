-- TrialBalance_Tree V12 usando Funciones (isShowOrganization)
-- OrgTree Version4
-- Matriz de Cuentas y Organizaciones en Arbol con el Saldo
-- Balance
-- isShowOrganization
-- Para Excel
WITH Accounts AS (
    SELECT 
        LEVEL AS nivel,
        node_id,
        parent_id,
        c_element_id,
        c_elementvalue_id,
        ad_client_id,
        isactive,
        codigo,
        name AS cuenta_nombre,
        description,
        length,
        accounttype,
        accountsign,
        isdoccontrolled,
        issummary,
        acctparent,
        pathel,
        ancestry,
        codigo0, name0, description0, issummary0,
        codigo1, name1, description1, issummary1,
        codigo2, name2, description2, issummary2,
        codigo3, name3, description3, issummary3,
        codigo4, name4, description4, issummary4,
        codigo5, name5, description5, issummary5,
        codigo6, name6, description6, issummary6,
        codigo7, name7, description7, issummary7,
        codigo8, name8, description8, issummary8,
        codigo9, name9, description9, issummary9
    FROM adempiere.amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID})
)
,Balances AS (
	-- Balances: 
	-- Balances Non Summary Accounts Orgs from OrgParent
	-- [c_elementvalue_id, ad_org_id]
    SELECT 
        c_elementvalue_id, 
        ad_client_id, 
        codigo0, codigo1, codigo2, codigo3, 
        codigo4, codigo5, codigo6, 
        codigo7, codigo8, codigo9, 
        all_orgs, org_ad_org_id AS ad_org_id, org_value, org_name,
        openbalance, amtacctdr, amtacctcr, closebalance, amtacctsa
    FROM (
        SELECT * FROM adempiere.amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID}) AS eve1
            LEFT JOIN adempiere.amf_org_tree($P{AD_Client_ID}, $P{AD_Org_ID}, $P{AD_OrgParent_ID}) AS org1 ON org1.org_ad_client_id = eve1.ad_client_id
            LEFT JOIN adempiere.amf_balance_account_org_flex_orgparent($P{AD_Client_ID}, $P{AD_OrgParent_ID}, $P{AD_Org_ID}, $P{C_AcctSchema_ID}, $P{C_Period_ID}, $P{PostingType}, $P{C_ElementValue_ID}, $P{DateFrom}, $P{DateTo} )
                AS bal1 ON bal1.bal_c_elementvalue_id = eve1.c_elementvalue_id AND bal1.ad_org_id = org1.org_ad_org_id
            WHERE eve1.issummary = 'N' AND ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
                            COALESCE(bal1.openbalance, 0) <> 0
                            OR COALESCE(bal1.amtacctdr, 0) <> 0
                            OR COALESCE(bal1.amtacctcr, 0) <> 0
                            OR COALESCE(bal1.closebalance, 0) <> 0 )))
            ) AS bal
)
,BalancesDetailOrg AS (
	-- BalancesDetailOrg: 
	-- Balances Non Summary Accounts and Orgs (One Balance for each Org)
	-- [03,c_elementvalue_id, ad_org_id]
	SELECT 
		'60' AS blk, accts.nivel, accts.c_elementvalue_id, 
		accts.codigo, accts.cuenta_nombre, accts.description, 
		accts.accounttype, accts.accountsign, accts.issummary,
		accts.codigo0, accts.codigo1, accts.codigo2, accts.codigo3, 
		accts.codigo4, accts.codigo5, accts.codigo6, 
		accts.codigo7, accts.codigo8, accts.codigo9, 
		bals.ad_org_id, bals.org_value, bals.org_name, bals.all_orgs, 
		COALESCE(bals.openbalance, 0)   AS openbalance,
		COALESCE(bals.amtacctdr, 0)     AS amtacctdr,
		COALESCE(bals.amtacctcr, 0)     AS amtacctcr,
		COALESCE(bals.amtacctsa, 0)     AS amtacctsa,
		COALESCE(bals.closebalance, 0)  AS closebalance
	FROM Accounts accts
	LEFT JOIN Balances bals ON bals.ad_client_id= accts.ad_client_id AND bals.c_elementvalue_id = accts.c_elementvalue_id
	WHERE accts.issummary= 'N'
)
,BalancesDetailAcct AS (
	-- BalancesDetailAcct: 
	-- Balances Non Summary Accounts (One Balance for Account)
	-- ad_org_id set to 0
	-- [02,c_elementvalue_id, 0]
    SELECT 
        '50' AS blk, dborg.nivel, dborg.c_elementvalue_id, 
		dborg.codigo, dborg.cuenta_nombre, dborg.description, 
		dborg.accounttype, dborg.accountsign, dborg.issummary,
		dborg.codigo0, dborg.codigo1, dborg.codigo2, dborg.codigo3, 
		dborg.codigo4, dborg.codigo5, dborg.codigo6, 
		dborg.codigo7, dborg.codigo8, dborg.codigo9,
		0 AS ad_org_id, NULL AS org_value,  NULL AS org_name, dborg.all_orgs, 
        SUM(openbalance) AS openbalance, 
        SUM(amtacctdr) AS amtacctdr, 
        SUM(amtacctcr) AS amtacctcr, 
        SUM(amtacctsa) AS amtacctsa, 
        SUM(closebalance) AS closebalance 
    FROM BalancesDetailOrg dborg
    GROUP BY 
        blk, dborg.nivel, dborg.c_elementvalue_id, 
		dborg.codigo, dborg.cuenta_nombre, dborg.description,
		dborg.codigo0, dborg.codigo1, dborg.codigo2, dborg.codigo3, 
		dborg.codigo4, dborg.codigo5, dborg.codigo6, 
		dborg.codigo7, dborg.codigo8, dborg.codigo9,
		dborg.accounttype, dborg.accountsign, dborg.issummary,
		dborg.all_orgs
)
,BalancesDetailAll AS (
	SELECT * 
	FROM BalancesDetailOrg	AS bdo
    WHERE ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
                        COALESCE(bdo.openbalance, 0) <> 0
                        OR COALESCE(bdo.amtacctdr, 0) <> 0
                        OR COALESCE(bdo.amtacctcr, 0) <> 0
                        OR COALESCE(bdo.closebalance, 0) <> 0 )))
	UNION ALL
	SELECT * 
	FROM BalancesDetailAcct AS bda
    WHERE ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
                            COALESCE(bda.openbalance, 0) <> 0
                            OR COALESCE(bda.amtacctdr, 0) <> 0
                            OR COALESCE(bda.amtacctcr, 0) <> 0
                            OR COALESCE(bda.closebalance, 0) <> 0 )))
	ORDER BY codigo, blk, ad_org_id
)
,SummaryAggregation AS (
    -- Bloque 0: Nivel más alto (ej: ACTIVO, agrupado por codigo0)
    SELECT
        '00' AS blk_summary,
        codigo0 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo0 IS NOT NULL 
    GROUP BY codigo0
    UNION ALL
    -- Bloque 1: Nivel 1 (ej: ACTIVO CORRIENTE, agrupado por codigo1)
    SELECT
        '01' AS blk_summary,
        codigo1 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo1 IS NOT NULL
    GROUP BY codigo1
    UNION ALL
    -- Bloque 2: Nivel 2 (ej: DISPONIBILIDADES, agrupado por codigo2)
    SELECT
        '02' AS blk_summary,
        codigo2 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo2 IS NOT NULL
    GROUP BY codigo2
    UNION ALL
    -- Bloque 3: Nivel 3  agrupado por codigo3
    SELECT
        '03' AS blk_summary,
        codigo3 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo3 IS NOT NULL
    GROUP BY codigo3
    UNION ALL
    -- Bloque 4: Nivel 4  agrupado por codigo4
    SELECT
        '04' AS blk_summary,
        codigo4 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo4 IS NOT NULL
    GROUP BY codigo4
    UNION ALL
    -- Bloque 5: Nivel 5  agrupado por codigo5
    SELECT
        '05' AS blk_summary,
        codigo5 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo5 IS NOT NULL
    GROUP BY codigo5
    UNION ALL
    -- Bloque 6: Nivel 6  agrupado por codigo6
    SELECT
        '06' AS blk_summary,
        codigo6 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo6 IS NOT NULL
    GROUP BY codigo6
    UNION ALL
    -- Bloque 7: Nivel 7  agrupado por codigo7
    SELECT
        '07' AS blk_summary,
        codigo7 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo7 IS NOT NULL
    GROUP BY codigo7
    UNION ALL
    -- Bloque 8: Nivel 8  agrupado por codigo8
    SELECT
        '08' AS blk_summary,
        codigo8 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo8 IS NOT NULL
    GROUP BY codigo8
    UNION ALL
    -- Bloque 9: Nivel 9
    SELECT
        '09' AS blk_summary,
        codigo9 AS codigo_agrupador,
        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr,
        SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, 
        SUM(amtacctsa) AS amtacctsa
    FROM BalancesDetailOrg
    WHERE codigo9 IS NOT NULL
    GROUP BY codigo9
)
-- QUERY FINAL
SELECT
    accts.codigo,
    accts.cuenta_nombre AS nombre,
    accts.accounttype,
	accts.accountsign,
	accts.issummary,
    NULL AS ad_org_id,
    NULL AS org_value,
    agg.openbalance, agg.amtacctdr AS debitos, agg.amtacctcr AS creditos, agg.amtacctsa AS balance_periodo, agg.closebalance,
    '10' AS tipo_registro,
    accts.nivel AS level,
    accts.pathel AS pathel_order
FROM 
    SummaryAggregation agg
JOIN 
    Accounts accts ON accts.codigo = agg.codigo_agrupador
WHERE 
    accts.issummary = 'Y' -- Solo si el código de agrupación es un nodo de resumen válido
    -- *** AÑADIR ESTE FILTRO ***
    AND ($P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
            COALESCE(agg.openbalance, 0) <> 0 
            OR COALESCE(agg.amtacctdr, 0) <> 0
            OR COALESCE(agg.amtacctcr, 0) <> 0
            OR COALESCE(agg.closebalance, 0) <> 0 
        )))
UNION ALL
-- 2. FILAS DE DETALLE (Incluye Total por Cuenta '50' y Detalle por Org '60')
SELECT
    db.codigo,
    db.cuenta_nombre AS nombre,
	db.accounttype,
	db.accountsign,
	db.issummary,
    db.ad_org_id,
    db.org_value,
    db.openbalance, db.amtacctdr AS debitos, db.amtacctcr AS creditos, db.amtacctsa AS balance_periodo, db.closebalance,
    db.blk AS tipo_registro, 
    db.nivel AS level,
    (SELECT pathel FROM Accounts a WHERE a.codigo = db.codigo LIMIT 1) AS pathel_order
FROM
    BalancesDetailAll db
WHERE
    -- ** FILTRO DE CERO PARA DETALLE ('50', '60') **
    -- NOTA: El filtro para '60' ya estaba en BalancesDetailAll, pero lo repetimos
    -- para el caso de '60' (Detalle por Org)
    $P{isShowZERO} = 'Y' OR ($P{isShowZERO} = 'N' AND (
            COALESCE(db.openbalance, 0) <> 0
            OR COALESCE(db.amtacctdr, 0) <> 0
            OR COALESCE(db.amtacctcr, 0) <> 0
            OR COALESCE(db.closebalance, 0) <> 0
        ))
-- ***************************************************************
-- * ORDENAMIENTO FINAL (CRÍTICO) *
-- ***************************************************************
ORDER BY
    pathel_order,
    tipo_registro ASC, -- Ordena Resumen (10) antes de Detalle (02/03)
    org_value;

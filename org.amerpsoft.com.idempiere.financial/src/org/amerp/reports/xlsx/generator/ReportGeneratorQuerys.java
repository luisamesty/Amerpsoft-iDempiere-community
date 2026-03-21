package org.amerp.reports.xlsx.generator;

public class ReportGeneratorQuerys {

    /**
     * Consulta SQL final para el Balance de Comprobación Jerárquico.
     * Incluye lógica de agregación por niveles y filtros de cero condicionales ('Y'/'N').
     */
    public static final String SQL_TRIAL_BALANCE_DATA = 
        "WITH Accounts AS (\n" +
        "    SELECT \n" +
        "        LEVEL AS nivel, node_id, parent_id, c_element_id, c_elementvalue_id, ad_client_id, isactive, codigo, name AS cuenta_nombre, description, length, accounttype, accountsign, isdoccontrolled, issummary, acctparent, pathel, ancestry,\n" +
        "        codigo0, name0, description0, issummary0, codigo1, name1, description1, issummary1, codigo2, name2, description2, issummary2, codigo3, name3, description3, issummary3,\n" +
        "        codigo4, name4, description4, issummary4, codigo5, name5, description5, issummary5, codigo6, name6, description6, issummary6, codigo7, name7, description7, issummary7,\n" +
        "        codigo8, name8, description8, issummary8, codigo9, name9, description9, issummary9\n" +
        "    FROM adempiere.amf_element_value_tree_extended(?, ?)\n" + // 1, 2
        ")\n" +
        ",Balances AS (\n" +
        "	SELECT \n" +
        "        c_elementvalue_id, ad_client_id, codigo0, codigo1, codigo2, codigo3, codigo4, codigo5, codigo6, codigo7, codigo8, codigo9, all_orgs, org_ad_org_id AS ad_org_id, org_value, org_name, openbalance, amtacctdr, amtacctcr, closebalance, amtacctsa\n" +
        "    FROM (\n" +
        "        SELECT * FROM adempiere.amf_element_value_tree_extended(?, ?) AS eve1\n" + // 3, 4
        "            LEFT JOIN adempiere.amf_org_tree(?, ?, ?) AS org1 ON org1.org_ad_client_id = eve1.ad_client_id\n" + // 5, 6, 7
        "            LEFT JOIN adempiere.amf_balance_account_org_flex_orgparent(?, ?, ?, ?, ?, ?, ?, ?, ? ) AS bal1 ON bal1.bal_c_elementvalue_id = eve1.c_elementvalue_id AND bal1.ad_org_id = org1.org_ad_org_id\n" + // 8-16
        "            WHERE eve1.issummary = 'N' AND (? = 'Y' OR (? = 'N' AND (COALESCE(bal1.openbalance, 0) <> 0 OR COALESCE(bal1.amtacctdr, 0) <> 0 OR COALESCE(bal1.amtacctcr, 0) <> 0 OR COALESCE(bal1.closebalance, 0) <> 0 )))\n" + // 17, 18
        "            ) AS bal\n" +
        ")\n" +
        ",BalancesDetailOrg AS (\n" +
        "	SELECT \n" +
        "		'60' AS blk, accts.nivel, accts.c_elementvalue_id, accts.codigo, accts.cuenta_nombre, accts.description, accts.accounttype, accts.accountsign, accts.issummary,\n" +
        "		accts.codigo0, accts.codigo1, accts.codigo2, accts.codigo3, accts.codigo4, accts.codigo5, accts.codigo6, accts.codigo7, accts.codigo8, accts.codigo9, \n" +
        "		bals.ad_org_id, bals.org_value, bals.org_name, bals.all_orgs, \n" +
        "		COALESCE(bals.openbalance, 0) AS openbalance, COALESCE(bals.amtacctdr, 0) AS amtacctdr, COALESCE(bals.amtacctcr, 0) AS amtacctcr, COALESCE(bals.amtacctsa, 0) AS amtacctsa, COALESCE(bals.closebalance, 0) AS closebalance\n" +
        "	FROM Accounts accts\n" +
        "	LEFT JOIN Balances bals ON bals.ad_client_id= accts.ad_client_id AND bals.c_elementvalue_id = accts.c_elementvalue_id\n" +
        "	WHERE accts.issummary= 'N'\n" +
        ")\n" +
        ",BalancesDetailAcct AS (\n" +
        "    SELECT \n" +
        "        '50' AS blk, dborg.nivel, dborg.c_elementvalue_id, dborg.codigo, dborg.cuenta_nombre, dborg.description, dborg.accounttype, dborg.accountsign, dborg.issummary,\n" +
        "		dborg.codigo0, dborg.codigo1, dborg.codigo2, dborg.codigo3, dborg.codigo4, dborg.codigo5, dborg.codigo6, dborg.codigo7, dborg.codigo8, dborg.codigo9,\n" +
        "		0 AS ad_org_id, NULL AS org_value,  NULL AS org_name, dborg.all_orgs, \n" +
        "        SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(amtacctsa) AS amtacctsa, SUM(closebalance) AS closebalance\n" +
        "    FROM BalancesDetailOrg dborg\n" +
        "    GROUP BY \n" +
        "        blk, dborg.nivel, dborg.c_elementvalue_id, dborg.codigo, dborg.cuenta_nombre, dborg.description, dborg.codigo0, dborg.codigo1, dborg.codigo2, dborg.codigo3, \n" +
        "		dborg.codigo4, dborg.codigo5, dborg.codigo6, dborg.codigo7, dborg.codigo8, dborg.codigo9, dborg.accounttype, dborg.accountsign, dborg.issummary, dborg.all_orgs\n" +
        ")\n" +
        ",BalancesDetailAll AS (\n" +
        "	SELECT * \n" +
        "	FROM BalancesDetailOrg	AS bdo\n" +
        "    WHERE (? = 'Y' OR (? = 'N' AND (COALESCE(bdo.openbalance, 0) <> 0 OR COALESCE(bdo.amtacctdr, 0) <> 0 OR COALESCE(bdo.amtacctcr, 0) <> 0 OR COALESCE(bdo.closebalance, 0) <> 0 )))\n" + // 19, 20
        "	UNION ALL\n" +
        "	SELECT * \n" +
        "	FROM BalancesDetailAcct AS bda\n" +
        "    WHERE (? = 'Y' OR (? = 'N' AND (COALESCE(bda.openbalance, 0) <> 0 OR COALESCE(bda.amtacctdr, 0) <> 0 OR COALESCE(bda.amtacctcr, 0) <> 0 OR COALESCE(bda.closebalance, 0) <> 0 )))\n" + // 21, 22
        ")\n" +
        ",SummaryAggregation AS (\n" +
        "    SELECT '00' AS blk_summary, codigo0 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo0 IS NOT NULL GROUP BY codigo0\n" +
        "    UNION ALL\n" +
        "    SELECT '01' AS blk_summary, codigo1 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo1 IS NOT NULL GROUP BY codigo1\n" +
        "    UNION ALL\n" +
        "    SELECT '02' AS blk_summary, codigo2 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo2 IS NOT NULL GROUP BY codigo2\n" +
        "    UNION ALL\n" +
        "    SELECT '03' AS blk_summary, codigo3 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo3 IS NOT NULL GROUP BY codigo3\n" +
        "    UNION ALL\n" +
        "    SELECT '04' AS blk_summary, codigo4 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo4 IS NOT NULL GROUP BY codigo4\n" +
        "    UNION ALL\n" +
        "    SELECT '05' AS blk_summary, codigo5 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo5 IS NOT NULL GROUP BY codigo5\n" +
        "    UNION ALL\n" +
        "    SELECT '06' AS blk_summary, codigo6 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo6 IS NOT NULL GROUP BY codigo6\n" +
        "    UNION ALL\n" +
        "    SELECT '07' AS blk_summary, codigo7 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo7 IS NOT NULL GROUP BY codigo7\n" +
        "    UNION ALL\n" +
        "    SELECT '08' AS blk_summary, codigo8 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo8 IS NOT NULL GROUP BY codigo8\n" +
        "    UNION ALL\n" +
        "    SELECT '09' AS blk_summary, codigo9 AS codigo_agrupador, SUM(openbalance) AS openbalance, SUM(amtacctdr) AS amtacctdr, SUM(amtacctcr) AS amtacctcr, SUM(closebalance) AS closebalance, SUM(amtacctsa) AS amtacctsa FROM BalancesDetailOrg WHERE codigo9 IS NOT NULL GROUP BY codigo9\n" +
        ")\n" +
        "-- QUERY FINAL\n" +
        "SELECT\n" +
        "    accts.codigo,\n" +
        "    accts.cuenta_nombre AS nombre,\n" +
        "    accts.accounttype AS accounttype,\n" +
        "    accts.accountsign AS accountsign,\n" +
        "    accts.issummary AS issummary,\n" +
        "    NULL AS ad_org_id,\n" +
        "    NULL AS org_value,\n" +
        "    agg.openbalance, agg.amtacctdr AS debitos, agg.amtacctcr AS creditos, agg.amtacctsa AS balance_periodo, agg.closebalance,\n" +
        "    '10' AS tipo_registro,\n" +
        "    accts.nivel AS level,\n" +
        "    accts.pathel AS pathel_order\n" +
        "FROM \n" +
        "    SummaryAggregation agg\n" +
        "JOIN \n" +
        "    Accounts accts ON accts.codigo = agg.codigo_agrupador\n" +
        "WHERE \n" +
        "    accts.issummary = 'Y'\n" +
        "    AND (? = 'Y' OR (? = 'N' AND (COALESCE(agg.openbalance, 0) <> 0 OR COALESCE(agg.amtacctdr, 0) <> 0 OR COALESCE(agg.amtacctcr, 0) <> 0 OR COALESCE(agg.closebalance, 0) <> 0 )))\n" + // 23, 24
        "UNION ALL\n" +
        "SELECT\n" +
        "    db.codigo,\n" +
        "    db.cuenta_nombre AS nombre,\n" +
        "    db.accounttype AS accounttype,\n" +
        "    db.accountsign AS accountsign,\n" +
        "    db.issummary AS issummary,\n" +
        "    db.ad_org_id AS ad_org_id,\n" +
        "    db.org_value,\n" +
        "    db.openbalance, db.amtacctdr AS debitos, db.amtacctcr AS creditos, db.amtacctsa AS balance_periodo, db.closebalance,\n" +
        "    db.blk AS tipo_registro, \n" +
        "    db.nivel AS level,\n" +
        "    (SELECT pathel FROM Accounts a WHERE a.codigo = db.codigo LIMIT 1) AS pathel_order\n" +
        "FROM\n" +
        "    BalancesDetailAll db\n" +
        "ORDER BY\n" +
        "    pathel_order, tipo_registro, org_value;\n";
    
    /**
     * Consulta para Balance de 12 Meses + Saldo Final
     * Basada en C_Year_ID
     */

    public static final String SQL_TRIAL_BALANCE_12_PERIODS = 
	    "WITH RECURSIVE Accounts AS (\n" +
	    "    SELECT \n" +
	    "        LEVEL AS nivel, node_id, parent_id, c_element_id, c_elementvalue_id, ad_client_id, isactive, \n" +
	    "        codigo, name AS cuenta_nombre, description, length, accounttype, accountsign, isdoccontrolled, \n" +
	    "        issummary, acctparent, pathel, ancestry, \n" +
	    "        codigo0, name0, codigo1, name1, codigo2, name2, codigo3, name3, \n" +
	    "        codigo4, name4, codigo5, name5, codigo6, name6, codigo7, name7, \n" +
	    "        codigo8, name8, codigo9, name9 \n" +
	    "    FROM adempiere.amf_element_value_tree_extended(?, ?) \n" + // 1. AD_Client_ID, 2. C_AcctSchema_ID
	    ") \n" +
	    ",Periods AS (\n" +
	    "    SELECT c_period_id, periodno \n" +
	    "    FROM adempiere.c_period \n" +
	    "    WHERE c_year_id = ? AND isactive = 'Y' \n" + // 3. C_Year_ID
	    ") \n" +
	    ",Balances AS (\n" +
	    "    SELECT \n" +
	    "        base.c_elementvalue_id, base.ad_client_id, \n" +
	    "        base.org_ad_org_id AS ad_org_id, base.org_value, base.org_name, \n" +
	    "        base.periodno, \n" +
	    "        COALESCE(bal1.amtacctsa, 0) AS amtacctsa, \n" +
	    "        COALESCE(bal1.closebalance, 0) AS closebalance \n" +
	    "    FROM (\n" +
	    "        SELECT eve1.c_elementvalue_id, eve1.ad_client_id, org1.org_ad_org_id, org1.org_value, org1.org_name, p.c_period_id, p.periodno \n" +
	    "        FROM Accounts AS eve1 \n" +
	    "        LEFT JOIN adempiere.amf_org_tree(?, ?, ?) AS org1 ON org1.org_ad_client_id = eve1.ad_client_id \n" + // 4. AD_Client_ID, 5. AD_Org_ID, 6. AD_OrgParent_ID
	    "        CROSS JOIN Periods p \n" +
	    "        WHERE eve1.issummary = 'N' \n" +
	    "    ) AS base \n" +
	    "    LEFT JOIN adempiere.amf_balance_account_org_flex_orgparent(?, ?, ?, ?, base.c_period_id, 'A', NULL, NULL, NULL) AS bal1 \n" + // 7. AD_Client_ID, 8. AD_OrgParent_ID, 9. AD_Org_ID, 10. C_AcctSchema_ID
	    "    ON bal1.bal_c_elementvalue_id = base.c_elementvalue_id AND bal1.ad_org_id = base.org_ad_org_id \n" +
	    ") \n" +
	    ",BalancesDetailOrg AS (\n" +
	    "    SELECT \n" +
	    "        accts.nivel, accts.c_elementvalue_id, accts.codigo, accts.cuenta_nombre, accts.accounttype, accts.accountsign, accts.issummary, \n" +
	    "        accts.codigo0, accts.codigo1, accts.codigo2, accts.codigo3, accts.codigo4, accts.codigo5, accts.codigo6, accts.codigo7, accts.codigo8, accts.codigo9, \n" +
	    "        bals.ad_org_id, bals.org_value, bals.org_name, \n" +
	    "        SUM(CASE WHEN bals.periodno = 1 THEN bals.amtacctsa ELSE 0 END) AS sa_p01, \n" +
	    "        SUM(CASE WHEN bals.periodno = 2 THEN bals.amtacctsa ELSE 0 END) AS sa_p02, \n" +
	    "        SUM(CASE WHEN bals.periodno = 3 THEN bals.amtacctsa ELSE 0 END) AS sa_p03, \n" +
	    "        SUM(CASE WHEN bals.periodno = 4 THEN bals.amtacctsa ELSE 0 END) AS sa_p04, \n" +
	    "        SUM(CASE WHEN bals.periodno = 5 THEN bals.amtacctsa ELSE 0 END) AS sa_p05, \n" +
	    "        SUM(CASE WHEN bals.periodno = 6 THEN bals.amtacctsa ELSE 0 END) AS sa_p06, \n" +
	    "        SUM(CASE WHEN bals.periodno = 7 THEN bals.amtacctsa ELSE 0 END) AS sa_p07, \n" +
	    "        SUM(CASE WHEN bals.periodno = 8 THEN bals.amtacctsa ELSE 0 END) AS sa_p08, \n" +
	    "        SUM(CASE WHEN bals.periodno = 9 THEN bals.amtacctsa ELSE 0 END) AS sa_p09, \n" +
	    "        SUM(CASE WHEN bals.periodno = 10 THEN bals.amtacctsa ELSE 0 END) AS sa_p10, \n" +
	    "        SUM(CASE WHEN bals.periodno = 11 THEN bals.amtacctsa ELSE 0 END) AS sa_p11, \n" +
	    "        SUM(CASE WHEN bals.periodno = 12 THEN bals.amtacctsa ELSE 0 END) AS sa_p12, \n" +
	    "        SUM(bals.amtacctsa) AS sa_anual, \n" +
	    "        SUM(CASE WHEN bals.periodno = 1 THEN bals.closebalance ELSE 0 END) AS bal_p01, \n" +
	    "        SUM(CASE WHEN bals.periodno = 2 THEN bals.closebalance ELSE 0 END) AS bal_p02, \n" +
	    "        SUM(CASE WHEN bals.periodno = 3 THEN bals.closebalance ELSE 0 END) AS bal_p03, \n" +
	    "        SUM(CASE WHEN bals.periodno = 4 THEN bals.closebalance ELSE 0 END) AS bal_p04, \n" +
	    "        SUM(CASE WHEN bals.periodno = 5 THEN bals.closebalance ELSE 0 END) AS bal_p05, \n" +
	    "        SUM(CASE WHEN bals.periodno = 6 THEN bals.closebalance ELSE 0 END) AS bal_p06, \n" +
	    "        SUM(CASE WHEN bals.periodno = 7 THEN bals.closebalance ELSE 0 END) AS bal_p07, \n" +
	    "        SUM(CASE WHEN bals.periodno = 8 THEN bals.closebalance ELSE 0 END) AS bal_p08, \n" +
	    "        SUM(CASE WHEN bals.periodno = 9 THEN bals.closebalance ELSE 0 END) AS bal_p09, \n" +
	    "        SUM(CASE WHEN bals.periodno = 10 THEN bals.closebalance ELSE 0 END) AS bal_p10, \n" +
	    "        SUM(CASE WHEN bals.periodno = 11 THEN bals.closebalance ELSE 0 END) AS bal_p11, \n" +
	    "        SUM(CASE WHEN bals.periodno = 12 THEN bals.closebalance ELSE 0 END) AS bal_p12, \n" +
	    "        SUM(bals.closebalance) AS bal_anual \n" +
	    "    FROM Accounts accts \n" +
	    "    LEFT JOIN Balances bals ON bals.ad_client_id = accts.ad_client_id AND bals.c_elementvalue_id = accts.c_elementvalue_id \n" +
	    "    WHERE accts.issummary = 'N' \n" +
	    "    GROUP BY 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20 \n" +
	    ") \n" +
	    ",SummaryAggregation AS (\n" +
	    "    SELECT blk_summary, codigo_agrupador, sa_p01, sa_p02, sa_p03, sa_p04, sa_p05, sa_p06, sa_p07, sa_p08, sa_p09, sa_p10, sa_p11, sa_p12, sa_anual, bal_p01, bal_p02, bal_p03, bal_p04, bal_p05, bal_p06, bal_p07, bal_p08, bal_p09, bal_p10, bal_p11, bal_p12, bal_anual \n" +
	    "    FROM ( \n" +
	    "        SELECT '00' AS blk_summary, codigo0 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo0 IS NOT NULL GROUP BY codigo0 \n" +
	    "        UNION ALL SELECT '01' AS blk_summary, codigo1 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo1 IS NOT NULL GROUP BY codigo1 \n" +
	    "        UNION ALL SELECT '02' AS blk_summary, codigo2 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo2 IS NOT NULL GROUP BY codigo2 \n" +
	    "        UNION ALL SELECT '03' AS blk_summary, codigo3 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo3 IS NOT NULL GROUP BY codigo3 \n" +
	    "        UNION ALL SELECT '04' AS blk_summary, codigo4 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo4 IS NOT NULL GROUP BY codigo4 \n" +
	    "        UNION ALL SELECT '05' AS blk_summary, codigo5 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo5 IS NOT NULL GROUP BY codigo5 \n" +
	    "        UNION ALL SELECT '06' AS blk_summary, codigo6 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo6 IS NOT NULL GROUP BY codigo6 \n" +
	    "        UNION ALL SELECT '07' AS blk_summary, codigo7 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo7 IS NOT NULL GROUP BY codigo7 \n" +
	    "        UNION ALL SELECT '08' AS blk_summary, codigo8 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo8 IS NOT NULL GROUP BY codigo8 \n" +
	    "        UNION ALL SELECT '09' AS blk_summary, codigo9 AS codigo_agrupador, SUM(sa_p01) AS sa_p01, SUM(sa_p02) AS sa_p02, SUM(sa_p03) AS sa_p03, SUM(sa_p04) AS sa_p04, SUM(sa_p05) AS sa_p05, SUM(sa_p06) AS sa_p06, SUM(sa_p07) AS sa_p07, SUM(sa_p08) AS sa_p08, SUM(sa_p09) AS sa_p09, SUM(sa_p10) AS sa_p10, SUM(sa_p11) AS sa_p11, SUM(sa_p12) AS sa_p12, SUM(sa_anual) AS sa_anual, SUM(bal_p01) AS bal_p01, SUM(bal_p02) AS bal_p02, SUM(bal_p03) AS bal_p03, SUM(bal_p04) AS bal_p04, SUM(bal_p05) AS bal_p05, SUM(bal_p06) AS bal_p06, SUM(bal_p07) AS bal_p07, SUM(bal_p08) AS bal_p08, SUM(bal_p09) AS bal_p09, SUM(bal_p10) AS bal_p10, SUM(bal_p11) AS bal_p11, SUM(bal_p12) AS bal_p12, SUM(bal_anual) AS bal_anual FROM BalancesDetailOrg WHERE codigo9 IS NOT NULL GROUP BY codigo9 \n" +
	    "    ) sub \n" +
	    ") \n" +
	    "SELECT * FROM ( " +
	    "    SELECT accts.codigo, accts.cuenta_nombre AS nombre, accts.accounttype, accts.accountsign, accts.issummary, NULL AS ad_org_id, NULL AS org_value, " +
	    "        agg.sa_p01, agg.sa_p02, agg.sa_p03, agg.sa_p04, agg.sa_p05, agg.sa_p06, agg.sa_p07, agg.sa_p08, agg.sa_p09, agg.sa_p10, agg.sa_p11, agg.sa_p12, agg.sa_anual, " +
	    "        agg.bal_p01, agg.bal_p02, agg.bal_p03, agg.bal_p04, agg.bal_p05, agg.bal_p06, agg.bal_p07, agg.bal_p08, agg.bal_p09, agg.bal_p10, agg.bal_p11, agg.bal_p12, agg.bal_anual, " +
	    "        '10' AS tipo_registro, accts.nivel AS level, accts.pathel AS pathel_order " +
	    "    FROM SummaryAggregation agg " +
	    "    JOIN Accounts accts ON accts.codigo = agg.codigo_agrupador " +
	    "    WHERE accts.issummary = 'Y' " +
	    "    UNION ALL " +
	    "    SELECT db.codigo, db.cuenta_nombre AS nombre, db.accounttype, db.accountsign, db.issummary, db.ad_org_id, db.org_value, " +
	    "        db.sa_p01, db.sa_p02, db.sa_p03, db.sa_p04, db.sa_p05, db.sa_p06, db.sa_p07, db.sa_p08, db.sa_p09, db.sa_p10, db.sa_p11, db.sa_p12, db.sa_anual, " +
	    "        db.bal_p01, db.bal_p02, db.bal_p03, db.bal_p04, db.bal_p05, db.bal_p06, db.bal_p07, db.bal_p08, db.bal_p09, db.bal_p10, db.bal_p11, db.bal_p12, db.bal_anual, " +
	    "        '60' AS tipo_registro, db.nivel AS level, accts_db.pathel AS pathel_order " +
	    "    FROM BalancesDetailOrg db " +
	    "    JOIN Accounts accts_db ON accts_db.codigo = db.codigo " +
	    ") AS final_query " +
	    "WHERE (? = 'Y' OR ( " + 
	    "    ABS(sa_p01) + ABS(sa_p02) + ABS(sa_p03) + ABS(sa_p04) + " +
	    "    ABS(sa_p05) + ABS(sa_p06) + ABS(sa_p07) + ABS(sa_p08) + " +
	    "    ABS(sa_p09) + ABS(sa_p10) + ABS(sa_p11) + ABS(sa_p12) " +
	    ") > 0) " +
	    "ORDER BY pathel_order, tipo_registro, org_value;";
}

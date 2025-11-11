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
}
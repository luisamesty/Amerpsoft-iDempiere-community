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

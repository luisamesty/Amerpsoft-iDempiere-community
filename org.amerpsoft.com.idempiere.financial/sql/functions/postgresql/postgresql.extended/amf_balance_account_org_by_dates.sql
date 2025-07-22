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

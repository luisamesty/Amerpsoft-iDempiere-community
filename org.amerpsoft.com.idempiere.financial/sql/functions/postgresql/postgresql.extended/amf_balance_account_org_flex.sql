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

-- Function amf_balance_account_org_flex
-- Devuelve el balance en un periodo o rango de fechas dado de una cuenta y una Organizacion
-- Si no tiene movimiento devuelve un registro con cero
-- Cuando el parametro p_ad_org_id es cero devuelve el balance ce la cuenta en el cliente.
-- DROP FUNCTION adempiere.amf_balance_account_org_flex(numeric, numeric, numeric, numeric, varchar, numeric, timestamp, timestamp);
CREATE OR REPLACE FUNCTION adempiere.amf_balance_account_org_flex(
    p_ad_client_id numeric,
    p_ad_org_id numeric,
    p_c_acctschema_id numeric,
    p_c_period_id numeric,
    p_postingtype character varying,
    p_c_elementvalue_id numeric,
    p_datefrom timestamp without time zone DEFAULT NULL::timestamp without time zone,
    p_dateto timestamp without time zone DEFAULT NULL::timestamp without time zone)
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
    amtacctsa numeric)
 LANGUAGE plpgsql
AS $function$
DECLARE
    v_startdate date;
    v_enddate date;
    v_sql text;
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

    -- Consulta dinámica
    v_sql := '
    SELECT
        ev.c_elementvalue_id,
        ev.value,
        ev.name,' ||
        CASE WHEN p_ad_org_id IS NULL OR p_ad_org_id = 0 THEN
            '0::numeric'
        ELSE
            'CAST(fa.ad_org_id AS NUMERIC)'
        END || ' AS ad_org_id,
        to_char($1::timestamp, ''DD-MM-YYYY'') AS dateini,
        to_char($2::timestamp, ''DD-MM-YYYY'') AS dateend,
        COALESCE(SUM(CASE WHEN fa.DateAcct < $1 THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0) AS openbalance,
        COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN $1 AND $2 THEN fa.amtacctdr ELSE 0 END), 0) AS amtacctdr,
        COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN $1 AND $2 THEN fa.amtacctcr ELSE 0 END), 0) AS amtacctcr,
        COALESCE(SUM(CASE WHEN fa.DateAcct <= $2 THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0) AS closebalance,
        COALESCE(SUM(CASE WHEN fa.DateAcct BETWEEN $1 AND $2 THEN fa.amtacctdr - fa.amtacctcr ELSE 0 END), 0) AS amtacctsa
    FROM c_elementvalue ev
    LEFT JOIN fact_acct fa ON fa.account_id = ev.c_elementvalue_id
        AND fa.ad_client_id = ' || p_ad_client_id || '
        AND fa.c_acctschema_id = ' || p_c_acctschema_id || '
        AND fa.postingtype = ' || quote_literal(p_postingtype) || '
        AND fa.dateacct <= $2 ';

    IF p_ad_org_id IS NOT NULL AND p_ad_org_id <> 0 THEN
        v_sql := v_sql || ' AND fa.ad_org_id = ' || p_ad_org_id;
    END IF;

    v_sql := v_sql || '
    WHERE ev.ad_client_id = ' || p_ad_client_id || '
      AND (' || 
        CASE 
            WHEN p_c_elementvalue_id IS NULL THEN 'ev.issummary = ''N'''
            ELSE 'ev.c_elementvalue_id = ' || p_c_elementvalue_id
        END || ')
      AND fa.ad_org_id IS NOT NULL ';

    v_sql := v_sql || '
    GROUP BY ev.c_elementvalue_id, ev.value, ev.name';

    IF p_ad_org_id IS NOT NULL AND p_ad_org_id <> 0 THEN
        v_sql := v_sql || ', fa.ad_org_id';
    END IF;

    -- Ejecutar consulta con parámetros
    RETURN QUERY EXECUTE v_sql USING p_datefrom, p_dateto;
END;
$function$;

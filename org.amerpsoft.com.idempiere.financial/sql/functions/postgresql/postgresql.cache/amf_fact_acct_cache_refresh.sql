-- Function amf_fact_acct_cache_refresh
-- Refresca temporal de fact_acct en funcion de los parametros dados

DROP FUNCTION IF EXISTS amf_fact_acct_cache_refresh(NUMERIC,NUMERIC,NUMERIC) ;

CREATE OR REPLACE FUNCTION adempiere.amf_fact_acct_cache_refresh(p_ad_client_id numeric, p_c_acctschema_id numeric, p_c_period_id numeric)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
BEGIN
    -- Eliminar registros cache para cliente y esquema contable y periodo
    DELETE FROM amf_fact_acct_cache c
    USING c_period p
    WHERE c.fac_ad_client_id = p_ad_client_id
      AND c.dateacct BETWEEN p.startdate AND p.enddate
      AND fac_c_period_id = p_c_period_id
      AND c.fac_c_acctschema_id = p_c_acctschema_id;

    -- Insertar registros nuevos
    INSERT INTO amf_fact_acct_cache (
        fact_acct_id, fac_ad_client_id, fac_ad_org_id, fac_c_acctschema_id, account_id, fac_c_period_id, fac_bpartner,
        fac_mproduct, fac_postingtype, c_currency_id, fac_currency, fac_table,
        amtsourcedr, amtsourcecr, amtacctdr, amtacctcr,
        c_uom_id, qty, dateacct, ad_table_id, record_id, line_id, description
    )
    SELECT 
        fac.fact_acct_id, 
        fac.ad_client_id,
        fac.ad_org_id,
		fac.c_acctschema_id,
        fac.account_id,
		per.c_period_id,
        COALESCE(bpa.value,'') || COALESCE(bpa.name,'') AS fac_bpartner,
        COALESCE(mpr.value,'') || COALESCE(mpr.name,'') AS fac_mproduct,
        fac.postingtype,
        fac.c_currency_id,
        CONCAT(curr.iso_code,'-',currt.cursymbol,'-',COALESCE(currt.description,curr.iso_code,curr.cursymbol,'')) as fac_currency,
        COALESCE (tab.tablename,'') AS fac_table,
        fac.amtsourcedr,
        fac.amtsourcecr,
        fac.amtacctdr,
        fac.amtacctcr,
        fac.c_uom_id,
        fac.qty,
        fac.dateacct,
        fac.ad_table_id,
        fac.record_id,
        fac.line_id,
        COALESCE(fac.description, '') AS description
    FROM fact_acct fac
    LEFT JOIN c_bpartner bpa ON fac.c_bpartner_id = bpa.c_bpartner_id
    LEFT JOIN m_product mpr ON fac.m_product_id = mpr.m_product_id
    LEFT JOIN ad_table tab ON fac.ad_table_id = tab.ad_table_id
    INNER JOIN c_period per ON per.c_period_id = p_c_period_id
    INNER JOIN c_currency curr ON curr.c_currency_id = fac.c_currency_id
    LEFT JOIN c_currency_trl currt ON curr.c_currency_id = currt.c_currency_id 
        AND currt.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID = p_ad_client_id)
    WHERE fac.c_acctschema_id = p_c_acctschema_id 
      AND fac.dateacct >= per.startdate 
      AND fac.dateacct <= per.enddate
      AND fac.ad_client_id = p_ad_client_id;
END;
$function$
;

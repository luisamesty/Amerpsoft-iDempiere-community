-- Tabla temporal de fact_acct 
-- Para reportes y BI
-- adempiere.amf_fact_acct_cache definition
-- Drop table
-- DROP TABLE adempiere.amf_fact_acct_cache;

CREATE TABLE adempiere.amf_fact_acct_cache (
	fact_acct_id numeric NOT NULL,
	fac_ad_client_id numeric NOT NULL,
	fac_c_acctschema_id numeric NULL,
	fac_ad_org_id numeric NULL,
	account_id numeric NULL,
	fac_c_period_id NUMERIC NULL,
	fac_bpartner varchar NULL,
	fac_mproduct varchar NULL,
	fac_postingtype varchar NULL,
	c_currency_id numeric NULL,
	fac_currency varchar NULL,
	fac_table varchar NULL,
	amtsourcedr numeric NULL,
	amtsourcecr numeric NULL,
	amtacctdr numeric NULL,
	amtacctcr numeric NULL,
	c_uom_id numeric NULL,
	qty numeric NULL,
	dateacct date NULL,
	ad_table_id numeric NULL,
	record_id numeric NULL,
	line_id numeric NULL,
	description text NULL,
	CONSTRAINT amf_fact_acct_cache_pkey PRIMARY KEY (fact_acct_id)
);

-- Function amf_fact_acct_cache_refresh
-- Refresca valores en tabla amf_fact_acct_cache  en funcion de los parametros dados
-- TO refresh sample:
--   SELECT amf_fact_acct_cache_refresh(1000000,1000000,1000072);
--
SELECT amf_fact_acct_cache_refresh(1000000,1000000,1000072);

-- Function drop
-- Name: amf_fact_acct_cache_refresh
DROP FUNCTION IF EXISTS amf_fact_acct_cache_refresh(NUMERIC,NUMERIC,NUMERIC) ;

-- Function create
-- Name: amf_fact_acct_cache_refresh
CREATE OR REPLACE FUNCTION amf_fact_acct_cache_refresh(
    p_ad_client_id NUMERIC,
    p_c_acctschema_id NUMERIC,
    p_c_period_id NUMERIC
) RETURNS VOID AS $$
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
        fact_acct_id, fac_ad_client_id, fac_ad_org_id, account_id, fac_c_period_id, fac_bpartner,
        fac_mproduct, fac_postingtype, c_currency_id, fac_currency, fac_table,
        amtsourcedr, amtsourcecr, amtacctdr, amtacctcr,
        c_uom_id, qty, dateacct, ad_table_id, record_id, line_id, description
    )
    SELECT 
        fac.fact_acct_id, 
        fac.ad_client_id,
        fac.ad_org_id,
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
$$ LANGUAGE plpgsql;

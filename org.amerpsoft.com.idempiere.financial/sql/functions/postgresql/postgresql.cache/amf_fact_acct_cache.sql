-- Tabla temporal de fact_acct 
-- Para reportes y BI

CREATE TABLE adempiere.amf_fact_acct_cache (
	fact_acct_id numeric NOT NULL,
	fac_ad_client_id numeric NOT NULL,
	fac_c_acctschema_id numeric NULL,
	fac_ad_org_id numeric NULL,
	account_id numeric NULL,
	fac_c_period_id numeric NULL,
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

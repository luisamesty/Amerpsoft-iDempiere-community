-- View: amf_acctschema_v

DROP VIEW IF EXISTS amf_acctschema_v;

CREATE OR REPLACE VIEW amf_acctschema_v AS 

SELECT c_acctschema_id, ad_client_id, ad_org_id, 
	'Y'::bpchar as isactive, name, description, gaap, isaccrual, 
       costingmethod, c_currency_id, autoperiodcontrol, c_period_id, 
       period_openhistory, period_openfuture, separator, hasalias, hascombination, 
       istradediscountposted, isdiscountcorrectstax, m_costtype_id, 
       costinglevel, isadjustcogs, ad_orgonly_id, ispostservices, isexplicitcostadjustment, 
       commitmenttype, processing, taxcorrectiontype, isallownegativeposting, 
       ispostifclearingequal
  FROM c_acctschema;

ALTER TABLE amf_acctschema_v
  OWNER TO adempiere;
  
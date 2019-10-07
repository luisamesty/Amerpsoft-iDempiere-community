-- View: adempiere.amf_accounts_v
-- Change PeriodFrom to PeriodFrom_ID, PeriodTo to PeriodTo_ID
DROP VIEW IF EXISTS adempiere.amf_accounts_v;

CREATE OR REPLACE VIEW adempiere.amf_accounts_v AS 
 SELECT elv0.c_elementvalue_id + 1000::numeric * amf.amf_acctbaldetail_id AS amf_accounts_v_id, 
 amf.amf_acctbaldetail_id, amf.ad_client_id, amf.ad_org_id, amf.created, amf.createdby, amf.updated, amf.updatedby, amf.isactive, 
 amf.ref_org_id, 
 amf.periodfrom_id, 
 amf.periodto_id, 
 amf.accountfrom_id, 
 amf.dateacctfrom,
 amf.dateacctto,
 elv1.value AS accountfrom, 
 amf.accountto_id, 
 elv2.value AS accountto, 
 elv0.c_elementvalue_id, 
 elv0.value AS acctvalue, 
 elv0.name AS acctname, 
 COALESCE(elv0.description, ''::character varying) AS acctdescription, 
 COALESCE(elv0.name2, ''::bpchar) AS acctname2, 
 elv0.accountsign AS acctsign, 
 elv0.accounttype AS accttype, 
 elv0.isactive AS acctisactive
   FROM adempiere.amf_acctbaldetail amf
   LEFT JOIN adempiere.c_element ele ON ele.c_element_id = amf.c_element_id
   LEFT JOIN adempiere.c_elementvalue elv1 ON ele.c_element_id = elv1.c_element_id AND amf.accountfrom_id = elv1.c_elementvalue_id
   LEFT JOIN adempiere.c_elementvalue elv2 ON ele.c_element_id = elv2.c_element_id AND amf.accountto_id = elv2.c_elementvalue_id
   LEFT JOIN adempiere.c_elementvalue elv0 ON elv0.ad_client_id = amf.ad_client_id AND elv0.value::text >= elv1.value::text AND elv0.value::text <= elv2.value::text AND elv0.issummary = 'N'::bpchar
  ORDER BY amf.amf_acctbaldetail_id, elv0.value;

ALTER TABLE adempiere.amf_accounts_v
  OWNER TO adempiere;


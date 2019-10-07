-- View: adempiere.amf_accounts_balance_v

DROP VIEW if EXISTS adempiere.amf_accounts_balance_v;

CREATE OR REPLACE VIEW adempiere.amf_accounts_balance_v AS 
 SELECT 
 elv0.c_elementvalue_id + 100::numeric * per.c_period_id + 10000::numeric * per.periodno + 1000::numeric * amf.amf_acctbaldetail_id AS amf_accounts_balance_v_id, 
 amf.amf_acctbaldetail_id, 
 elv0.c_elementvalue_id + 1000::numeric * amf.amf_acctbaldetail_id AS amf_accounts_v_id, 
 amf.ad_client_id, amf.ad_org_id, amf.created, amf.createdby, amf.updated, amf.updatedby, amf.isactive, amf.ref_org_id, 
 amf.periodfrom_id, 
 amf.periodto_id, 
 amf.accountfrom_id, elv1.value AS accountfrom, 
 amf.accountto_id, elv2.value AS accountto, 
 elv0.c_elementvalue_id, elv0.value AS balvalue, elv0.name AS balname, 
 COALESCE(elv0.description, ''::character varying) AS baldescription, 
 COALESCE(elv0.name2, ''::bpchar) AS balname2, 
 elv0.accountsign AS acctsign, 
 elv0.accounttype AS accttype, 
 elv0.isactive AS acctisactive, 
 per.c_period_id, 
 per.isactive AS perisactive, 
 per.name AS pername, 
 per.startdate, per.enddate, 
 adempiere.amf_acctdate0prevbalance(amf.ad_client_id, amf.ref_org_id, elv0.c_elementvalue_id, per.startdate) AS amf_acctper0prevbalance, 
 adempiere.amf_acctdate1deb(amf.ad_client_id, amf.ref_org_id, elv0.c_elementvalue_id, per.startdate, per.enddate) AS amf_acctper1deb, 
 adempiere.amf_acctdate2cre(amf.ad_client_id, amf.ref_org_id, elv0.c_elementvalue_id, per.startdate, per.enddate) AS amf_acctper2cre, 
 adempiere.amf_acctdate3balance(amf.ad_client_id, amf.ref_org_id, elv0.c_elementvalue_id, per.startdate, per.enddate) AS amf_acctper3balance, 
 adempiere.amf_acctdate4currentbalance(amf.ad_client_id, amf.ref_org_id, elv0.c_elementvalue_id, per.startdate, per.enddate) AS amf_acctper4currentbalance
   FROM adempiere.amf_acctbaldetail amf
   LEFT JOIN adempiere.c_element ele ON ele.c_element_id = amf.c_element_id
   LEFT JOIN adempiere.c_elementvalue elv1 ON ele.c_element_id = elv1.c_element_id AND amf.accountfrom_id = elv1.c_elementvalue_id
   LEFT JOIN adempiere.c_elementvalue elv2 ON ele.c_element_id = elv2.c_element_id AND amf.accountto_id = elv2.c_elementvalue_id
   LEFT JOIN adempiere.c_elementvalue elv0 ON elv0.ad_client_id = amf.ad_client_id AND elv0.value::text >= elv1.value::text AND elv0.value::text <= elv2.value::text AND elv0.issummary = 'N'::bpchar
   LEFT JOIN adempiere.c_period per1 ON amf.periodfrom_id = per1.c_period_id
   LEFT JOIN adempiere.c_period per2 ON amf.periodto_id = per2.c_period_id
   LEFT JOIN adempiere.c_period per ON per.ad_client_id = amf.ad_client_id AND per.startdate >= per1.startdate AND per.enddate <= per2.enddate
  ORDER BY elv0.value, per.startdate;

ALTER TABLE adempiere.amf_accounts_balance_v
  OWNER TO adempiere;


-- View: adempiere.amf_accounts_detail_v

DROP VIEW IF EXISTS adempiere.amf_accounts_detail_v;

CREATE OR REPLACE VIEW adempiere.amf_accounts_detail_v AS 
 SELECT fas.fact_acct_id AS amf_accounts_detail_v_id, 
 amf.c_elementvalue_id + 100::numeric * per.c_period_id + 10000::numeric * per.periodno + 1000::numeric * amf.amf_acctbaldetail_id AS amf_accounts_balance_v_id, 
 amf.amf_acctbaldetail_id, 
 amf.amf_accounts_v_id, 
 amf.ad_client_id, amf.ad_org_id, amf.created, amf.createdby, amf.updated, amf.updatedby, amf.isactive, amf.ref_org_id, 
 amf.periodfrom_id, 
 amf.periodto_id, 
 amf.acctvalue, 
 amf.acctname, 
 amf.acctdescription, 
 amf.acctname2, 
 amf.acctsign, 
 amf.acctisactive, 
 fas.c_acctschema_id, fas.account_id AS fasaccount, fas.datetrx, fas.dateacct, fas.c_period_id AS fasperiod, 
 fas.ad_table_id, fas.record_id, fas.gl_category_id, fas.gl_budget_id, fas.c_tax_id, fas.m_locator_id, fas.postingtype, 
 fas.c_currency_id, fas.amtsourcedr, fas.amtsourcecr, fas.amtacctdr, fas.amtacctcr, fas.c_uom_id, fas.qty, fas.m_product_id, fas.c_bpartner_id, fas.ad_orgtrx_id, fas.c_locfrom_id, fas.c_locto_id, fas.c_salesregion_id, fas.c_project_id, fas.c_campaign_id, fas.c_activity_id, fas.user1_id, fas.user2_id, fas.description, fas.a_asset_id, fas.c_subacct_id, fas.c_projectphase_id, fas.c_projecttask_id, per.c_period_id AS perperiod, per.isactive AS perisactive, per.name AS pername, per.startdate, per.enddate, per1.startdate AS perstartdate, per2.enddate AS perenddate
   FROM adempiere.fact_acct fas
   LEFT JOIN adempiere.amf_accounts_v amf ON fas.account_id = amf.c_elementvalue_id
   LEFT JOIN adempiere.c_period per1 ON amf.periodfrom_id = per1.c_period_id
   LEFT JOIN adempiere.c_period per2 ON amf.periodto_id = per2.c_period_id
   LEFT JOIN adempiere.c_period per ON per.ad_client_id = amf.ad_client_id AND per.c_period_id = fas.c_period_id
  WHERE fas.dateacct >= per1.startdate AND fas.dateacct <= per2.enddate
  ORDER BY amf.acctvalue, fas.dateacct;

ALTER TABLE adempiere.amf_accounts_detail_v
  OWNER TO adempiere;


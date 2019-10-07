-- View: amf_fact_accounts_v 
-- (Version 10 Reconc_payment_ID)
-- (Version 11 Included Payroll Receipt Description )

 DROP VIEW IF EXISTS amf_fact_accounts_v;

CREATE OR REPLACE VIEW amf_fact_accounts_v AS 
 SELECT fac.fact_acct_id, fac.ad_client_id, fac.ad_org_id, fac.isactive, fac.created, fac.createdby, fac.updated, fac.updatedby, fac.c_acctschema_id, 
 fac.account_id, fac.datetrx, fac.dateacct, fac.c_period_id, fac.ad_table_id, fac.record_id, fac.line_id, fac.gl_category_id, fac.gl_budget_id, 
 fac.c_tax_id, fac.m_locator_id, fac.postingtype, fac.c_currency_id, fac.amtsourcedr, fac.amtsourcecr, fac.amtacctdr, fac.amtacctcr, 
 fac.c_uom_id, fac.qty, fac.m_product_id, fac.c_bpartner_id, fac.ad_orgtrx_id, fac.c_locfrom_id, fac.c_locto_id, 
 fac.c_salesregion_id, fac.c_project_id, fac.c_campaign_id, fac.c_activity_id, fac.user1_id, fac.user2_id, 
 CASE WHEN fac.ad_table_id = 1000042::numeric AND pay.amn_payroll_id IS NOT NULL THEN CONCAT(COALESCE(pay.description,pay.name,pay.value,''),'-',fac.description)
	  ELSE fac.description END  AS description, 
 fac.a_asset_id, fac.c_subacct_id, fac.userelement1_id, fac.userelement2_id, fac.c_projectphase_id, fac.c_projecttask_id, fac.fact_acct_uu, 
 bpa.c_bp_group_id, bpa.c_bp_holding_id, bpa.c_bp_channel_id, bpa.isvip, bpa.value AS bpcode, bpa.salesrep_id AS ad_user_id,
 mpr.m_product_category_id, mpr.m_product_brand_id, mpr.m_product_class_id, mpr.m_product_discipline_id, 
 mpr.m_product_family_id, mpr.m_product_line_id, 
 emp.amn_position_id, COALESCE(pay.amn_contract_id, emp.amn_contract_id) AS amn_contract_id, 
 COALESCE(pay.amn_department_id, emp.amn_department_id) AS amn_department_id, 
 COALESCE(pay.amn_location_id, emp.amn_location_id) AS amn_location_id, 
 COALESCE(pay.amn_jobtitle_id, emp.amn_jobtitle_id) AS amn_jobtitle_id, 
 COALESCE(pay.amn_jobstation_id, emp.amn_jobstation_id) AS amn_jobstation_id, pay.amn_payroll_id, 
 pay2.c_payment_id, 
        CASE
            WHEN fac.ad_table_id = 335::numeric THEN pay2.c_bankaccount_id
            WHEN fac.ad_table_id = 392::numeric THEN cbs2.c_bankaccount_id
            ELSE 0::numeric
        END AS c_bankaccount_id, pay2.c_charge_id, 
        CASE
            WHEN reflistr.name IS NULL THEN 'Fuerza de Trabajo *** SIN DEFINIR ***'::character varying
            ELSE reflistr.name
        END AS workforce, COALESCE(pay2.c_invoice_id, 0::numeric) AS c_invoice_id, pay2.reconc_payment_id, 
        CASE
            WHEN pay2.reconc_payment_id IS NOT NULL THEN 
            pg_catalog.concat(to_char(pay3.payamt, '999999999999D99S'::text), '  ', baa.name, ' ', to_char(pay3.dateacct, 'DD/MM/YYYY'::text), ' ', dct.docbasetype, '-', pay3.documentno, '-', pay3.description)
            ELSE ''::text
        END AS reconc_description
   FROM fact_acct fac
   LEFT JOIN c_bpartner bpa ON fac.c_bpartner_id = bpa.c_bpartner_id
   LEFT JOIN m_product mpr ON fac.m_product_id = mpr.m_product_id
   LEFT JOIN amn_employee emp ON emp.c_bpartner_id = bpa.c_bpartner_id
   LEFT JOIN ad_table tbl ON tbl.name::text = 'AMN_Payroll'::text
   LEFT JOIN amn_payroll pay ON pay.amn_payroll_id = fac.record_id
   LEFT JOIN ad_table tbl2 ON tbl.name::text = 'C_Payment'::text
   LEFT JOIN c_payment pay2 ON pay2.c_payment_id = fac.record_id
   LEFT JOIN c_payment pay3 ON pay2.reconc_payment_id = pay3.c_payment_id
   LEFT JOIN c_doctype dct ON dct.c_doctype_id = pay3.c_doctype_id
   LEFT JOIN c_bankaccount baa ON baa.c_bankaccount_id = pay3.c_bankaccount_id
   LEFT JOIN amn_jobtitle crg ON pay.amn_jobtitle_id = crg.amn_jobtitle_id
   LEFT JOIN ad_reference ref ON ref.name::text = 'AMN_Workforce'::text
   LEFT JOIN ad_ref_list reflis ON ref.ad_reference_id = reflis.ad_reference_id AND reflis.value::bpchar = crg.workforce
   LEFT JOIN ad_ref_list_trl reflistr ON reflis.ad_ref_list_id = reflistr.ad_ref_list_id AND reflistr.ad_language::text = 'es_VE'::text
   LEFT JOIN c_bankstatement cbs2 ON cbs2.c_bankstatement_id = fac.record_id;

ALTER TABLE amf_fact_accounts_v
  OWNER TO adempiere;


-- adempiere.amf_fact_accounts_analitic_v source

CREATE OR REPLACE VIEW adempiere.amf_fact_accounts_analitic_v
AS SELECT fac.fact_acct_id,
    fac.ad_client_id,
    fac.ad_org_id,
    fac.isactive,
    fac.created,
    fac.createdby,
    fac.updated,
    fac.updatedby,
    fac.c_acctschema_id,
    fac.gl_category_id,
    fac.gl_budget_id,
    fac.c_tax_id,
    fac.m_locator_id,
    fac.postingtype,
    fac.c_currency_id,
    fac.amtsourcedr,
    fac.amtsourcecr,
    fac.amtacctdr,
    fac.amtacctcr,
    fac.c_uom_id,
    fac.qty,
    fac.dateacct,
    fac.c_period_id,
    fac.ad_table_id,
    fac.record_id,
    fac.line_id,
    fac.account_id,
    COALESCE(fac.description, ''::character varying) AS description,
        CASE
            WHEN fac.ad_table_id = 335::numeric THEN pay.c_bankaccount_id
            WHEN fac.ad_table_id = 392::numeric THEN cbs.c_bankaccount_id
            ELSE 0::numeric
        END AS c_bankaccount_id,
        CASE
            WHEN fac.ad_table_id = 335::numeric THEN pay.c_charge_id
            ELSE 0::numeric
        END AS c_charge_id,
    COALESCE(cha.name, ''::character varying) AS ccharge_name,
    fac.c_bpartner_id,
    COALESCE(bpa.value, ''::character varying) AS bpartner_value,
    COALESCE(bpa.name, ''::character varying) AS bpartner_name,
    fac.m_product_id,
    COALESCE(mpr.value, ''::character varying) AS mproduct_value,
    COALESCE(mpr.name, ''::character varying) AS mproduct_name,
        CASE
            WHEN fac.ad_table_id = 318::numeric THEN inv.documentno::text
            WHEN fac.ad_table_id = 319::numeric THEN min.documentno::text
            WHEN fac.ad_table_id = 335::numeric THEN pay.documentno::text
            WHEN fac.ad_table_id = 259::numeric THEN ord.documentno::text
            WHEN fac.ad_table_id = 392::numeric THEN to_char(cbs.c_bankstatement_id, '9999999999'::text)
            WHEN fac.ad_table_id = 407::numeric THEN to_char(cas.c_cash_id, '9999999999'::text)
            WHEN fac.ad_table_id = 735::numeric THEN cal.documentno::text
            WHEN fac.ad_table_id = 224::numeric THEN jou.documentno::text
            WHEN fac.ad_table_id = 323::numeric THEN mov.documentno::text
            WHEN fac.ad_table_id = 702::numeric THEN mrq.documentno::text
            WHEN fac.ad_table_id = 321::numeric THEN miv.documentno::text
            WHEN fac.ad_table_id = 325::numeric THEN mpo.documentno::text
            WHEN fac.ad_table_id = 472::numeric THEN mma.documentno::text
            WHEN fac.ad_table_id = 473::numeric THEN mmp.documentno::text
            WHEN fac.ad_table_id = 1000042::numeric THEN amn.documentno::text
            ELSE concat(tbl.tablename, '- Doc:', fac.record_id::character varying)
        END AS documentno,
        CASE
            WHEN fac.ad_table_id = 318::numeric THEN COALESCE(inv.description, ''::character varying)::text
            WHEN fac.ad_table_id = 319::numeric THEN COALESCE(min.description, ''::character varying)::text
            WHEN fac.ad_table_id = 335::numeric THEN COALESCE(pay.description, ''::character varying)::text
            WHEN fac.ad_table_id = 259::numeric THEN COALESCE(ord.description, ''::character varying)::text
            WHEN fac.ad_table_id = 392::numeric THEN COALESCE(cbs.description, ''::character varying)::text
            WHEN fac.ad_table_id = 407::numeric THEN COALESCE(cas.description, ''::character varying)::text
            WHEN fac.ad_table_id = 735::numeric THEN COALESCE(cal.description, ''::character varying)::text
            WHEN fac.ad_table_id = 224::numeric THEN COALESCE(jou.description, ''::character varying)::text
            WHEN fac.ad_table_id = 323::numeric THEN COALESCE(mov.description, ''::character varying)::text
            WHEN fac.ad_table_id = 702::numeric THEN COALESCE(mrq.description, ''::character varying)::text
            WHEN fac.ad_table_id = 321::numeric THEN COALESCE(miv.description, ''::character varying)::text
            WHEN fac.ad_table_id = 325::numeric THEN COALESCE(mpo.description, ''::character varying)::text
            WHEN fac.ad_table_id = 472::numeric THEN COALESCE(mma.description, ''::character varying)::text
            WHEN fac.ad_table_id = 473::numeric THEN COALESCE(mmp.description, ''::character varying)::text
            WHEN fac.ad_table_id = 1000042::numeric THEN COALESCE(amn.description, ''::character varying)::text
            ELSE concat(tbl.tablename, '- Rec:', fac.record_id::character varying)
        END AS description_tbl,
        CASE
            WHEN fac.ad_table_id = 318::numeric THEN inv.c_doctype_id
            WHEN fac.ad_table_id = 319::numeric THEN min.c_doctype_id
            WHEN fac.ad_table_id = 335::numeric THEN pay.c_doctype_id
            WHEN fac.ad_table_id = 259::numeric THEN ord.c_doctype_id
            WHEN fac.ad_table_id = 392::numeric THEN '9999999999'::bigint::numeric
            WHEN fac.ad_table_id = 407::numeric THEN '9999999998'::bigint::numeric
            WHEN fac.ad_table_id = 735::numeric THEN '9999999997'::bigint::numeric
            WHEN fac.ad_table_id = 224::numeric THEN jou.c_doctype_id
            WHEN fac.ad_table_id = 323::numeric THEN mov.c_doctype_id
            WHEN fac.ad_table_id = 702::numeric THEN mrq.c_doctype_id
            WHEN fac.ad_table_id = 321::numeric THEN miv.c_doctype_id
            WHEN fac.ad_table_id = 325::numeric THEN '9999999996'::bigint::numeric
            WHEN fac.ad_table_id = 472::numeric THEN '9999999995'::bigint::numeric
            WHEN fac.ad_table_id = 473::numeric THEN '9999999994'::bigint::numeric
            WHEN fac.ad_table_id = 1000042::numeric THEN amn.c_doctype_id
            ELSE 0::numeric
        END AS c_doctype_id,
    cev.value AS account_value,
    cev.name AS account_name,
    fac.c_project_id,
    COALESCE(proj.value, ''::character varying) AS project_value,
    COALESCE(proj.name, ''::character varying) AS project_name,
    fac.c_activity_id,
    COALESCE(acti.value, ''::character varying) AS activity_value,
    COALESCE(acti.name, ''::character varying) AS activity_name,
    fac.c_salesregion_id,
    COALESCE(sare.value, ''::character varying) AS salesregion_value,
    COALESCE(sare.name, ''::character varying) AS salesregion_name,
    fac.c_campaign_id,
    COALESCE(camp.value, ''::character varying) AS campaign_value,
    COALESCE(camp.name, ''::character varying) AS campaign_name
   FROM fact_acct fac
     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = fac.account_id
     LEFT JOIN c_bpartner bpa ON fac.c_bpartner_id = bpa.c_bpartner_id
     LEFT JOIN m_product mpr ON fac.m_product_id = mpr.m_product_id
     LEFT JOIN amn_employee emp ON emp.c_bpartner_id = bpa.c_bpartner_id
     LEFT JOIN amn_payroll amn ON amn.amn_payroll_id = fac.record_id
     LEFT JOIN amn_jobtitle crg ON amn.amn_jobtitle_id = crg.amn_jobtitle_id
     LEFT JOIN ad_reference ref ON ref.name::text = 'AMN_Workforce'::text
     LEFT JOIN ad_ref_list reflis ON ref.ad_reference_id = reflis.ad_reference_id AND reflis.value::bpchar = crg.workforce
     LEFT JOIN ad_ref_list_trl reflistr ON reflis.ad_ref_list_id = reflistr.ad_ref_list_id AND reflistr.ad_language::text = ((( SELECT ad_client.ad_language
           FROM ad_client
          WHERE ad_client.ad_client_id = fac.ad_client_id))::text)
     LEFT JOIN c_invoice inv ON inv.c_invoice_id = fac.record_id
     LEFT JOIN m_inout min ON min.m_inout_id = fac.record_id
     LEFT JOIN c_payment pay ON pay.c_payment_id = fac.record_id
     LEFT JOIN c_order ord ON ord.c_order_id = fac.record_id
     LEFT JOIN c_bankstatement cbs ON cbs.c_bankstatement_id = fac.record_id
     LEFT JOIN c_charge cha ON cha.c_charge_id = pay.c_charge_id
     LEFT JOIN c_cash cas ON cas.c_cash_id = fac.record_id
     LEFT JOIN c_allocationhdr cal ON cal.c_allocationhdr_id = fac.record_id
     LEFT JOIN gl_journal jou ON jou.gl_journal_id = fac.record_id
     LEFT JOIN m_movement mov ON mov.m_movement_id = fac.record_id
     LEFT JOIN m_requisition mrq ON mrq.m_requisition_id = fac.record_id
     LEFT JOIN m_inventory miv ON miv.m_inventory_id = fac.record_id
     LEFT JOIN m_production mpo ON mpo.m_production_id = fac.record_id
     LEFT JOIN m_matchinv mma ON mma.m_matchinv_id = fac.record_id
     LEFT JOIN m_matchpo mmp ON mmp.m_matchpo_id = fac.record_id
     LEFT JOIN ad_table tbl ON tbl.ad_table_id = fac.ad_table_id
     LEFT JOIN c_project proj ON proj.c_project_id = fac.c_project_id
     LEFT JOIN c_activity acti ON acti.c_activity_id = fac.c_activity_id
     LEFT JOIN c_salesregion sare ON sare.c_salesregion_id = fac.c_salesregion_id
     LEFT JOIN c_campaign camp ON camp.c_campaign_id = fac.c_campaign_id;
-- View: amf_fact_accounts_v

DROP VIEW IF EXISTS amf_fact_accounts_analitic_v;

CREATE OR REPLACE VIEW amf_fact_accounts_analitic_v AS 
SELECT
	-- Fact_acct Accounting Transaction
	fac.fact_acct_id, 
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
	COALESCE(fac.description,'') as description,
	-- C_BankAccount Bank Account
	CASE 	WHEN fac.AD_table_ID = 335 THEN pay.c_bankaccount_id --C_Payment
		WHEN fac.AD_table_ID = 392 THEN cbs.c_bankaccount_id -- C_BankStatement
		ELSE 0 END AS  c_bankaccount_id,
	-- C_Charge
	CASE 	WHEN fac.AD_table_ID = 335 THEN pay.c_charge_id --C_Payment
		ELSE 0 END AS  c_charge_id,
	COALESCE(cha.name,'') as ccharge_name,
	-- C_Bpartner BUSINESS PARTNER
	fac.c_bpartner_id as c_bpartner_id,
	COALESCE(bpa.value,'') as bpartner_value,
	COALESCE(bpa.name,'') as bpartner_name,
	-- M_Product Product
	fac.m_product_id as m_product_id,
	COALESCE(mpr.value,'') as mproduct_value,
	COALESCE(mpr.name,'') as mproduct_name,
	-- DocumentNo Document Number Original Table
	CASE 	WHEN fac.AD_table_ID = 318 THEN inv.documentno -- C_Invoice
		WHEN fac.AD_table_ID = 319 THEN min.documentno -- M_InOut
		WHEN fac.AD_table_ID = 335 THEN pay.documentno -- C_Payment
		WHEN fac.AD_table_ID = 259 THEN ord.documentno -- C_Order
		WHEN fac.AD_table_ID = 392 THEN to_char(cbs.C_BankStatement_ID,'9999999999') -- C_BankStatement
		WHEN fac.AD_table_ID = 407 THEN to_char(cas.C_Cash_ID,'9999999999') -- C_Cash
		WHEN fac.AD_table_ID = 735 THEN cal.documentno -- C_AllocationHdr
		WHEN fac.AD_table_ID = 224 THEN jou.documentno -- GLJournal
		WHEN fac.AD_table_ID = 323 THEN mov.documentno -- M_Movement
		WHEN fac.AD_table_ID = 702 THEN mrq.documentno -- M_Requisition
		WHEN fac.AD_table_ID = 321 THEN miv.documentno -- M_Inventory
		WHEN fac.AD_table_ID = 325 THEN mpo.documentno -- M_Production
		WHEN fac.AD_table_ID = 472 THEN mma.documentno -- M_MatchInv
		WHEN fac.AD_table_ID = 473 THEN mmp.documentno -- M_MatchPO
		WHEN fac.AD_table_ID = 1000042 THEN amn.documentno -- AMN_Payroll
		ELSE CONCAT(tbl.tablename,'- Doc:',CAST(fac.record_id AS varchar)) END AS  documentno,
	-- Description  Original Table
	CASE 	WHEN fac.AD_table_ID = 318 THEN COALESCE(inv.description,'') -- C_Invoice
		WHEN fac.AD_table_ID = 319 THEN COALESCE(min.description,'') -- M_InOut
		WHEN fac.AD_table_ID = 335 THEN COALESCE(pay.description,'') -- C_Payment
		WHEN fac.AD_table_ID = 259 THEN COALESCE(ord.description,'') -- C_Order
		WHEN fac.AD_table_ID = 392 THEN COALESCE(cbs.description,'') -- C_BankStatement
		WHEN fac.AD_table_ID = 407 THEN COALESCE(cas.description,'') -- C_Cash
		WHEN fac.AD_table_ID = 735 THEN COALESCE(cal.description,'') -- C_AllocationHdr
		WHEN fac.AD_table_ID = 224 THEN COALESCE(jou.description,'') -- GLJournal
		WHEN fac.AD_table_ID = 323 THEN COALESCE(mov.description,'') -- M_Movement
		WHEN fac.AD_table_ID = 702 THEN COALESCE(mrq.description,'') -- M_Requisition
		WHEN fac.AD_table_ID = 321 THEN COALESCE(miv.description,'') -- M_Inventory
		WHEN fac.AD_table_ID = 325 THEN COALESCE(mpo.description,'') -- M_Production
		WHEN fac.AD_table_ID = 472 THEN COALESCE(mma.description,'') -- M_MatchInv
		WHEN fac.AD_table_ID = 473 THEN COALESCE(mmp.description,'') -- M_MatchPO
		WHEN fac.AD_table_ID = 1000042 THEN COALESCE(amn.description,'') -- AMN_Payroll
		ELSE CONCAT(tbl.tablename,'- Rec:',CAST(fac.record_id AS varchar)) END AS  description_tbl,
	-- Document Type Original Table
	CASE 	WHEN fac.AD_table_ID = 318 THEN inv.c_doctype_id -- C_Invoice
		WHEN fac.AD_table_ID = 319 THEN min.c_doctype_id -- M_InOut
		WHEN fac.AD_table_ID = 335 THEN pay.c_doctype_id -- C_Payment
		WHEN fac.AD_table_ID = 259 THEN ord.c_doctype_id -- C_Order
		WHEN fac.AD_table_ID = 392 THEN 9999999999 -- C_BankStatement
		WHEN fac.AD_table_ID = 407 THEN 9999999998 -- C_Cash
		WHEN fac.AD_table_ID = 735 THEN 9999999997 -- C_AllocationHdr
		WHEN fac.AD_table_ID = 224 THEN jou.c_doctype_id -- GLJournal
		WHEN fac.AD_table_ID = 323 THEN mov.c_doctype_id -- M_Movement
		WHEN fac.AD_table_ID = 702 THEN mrq.c_doctype_id -- M_Requisition
		WHEN fac.AD_table_ID = 321 THEN miv.c_doctype_id -- M_Inventory
		WHEN fac.AD_table_ID = 325 THEN 9999999996 -- M_Production
		WHEN fac.AD_table_ID = 472 THEN 9999999995 -- M_MatchInv
		WHEN fac.AD_table_ID = 473 THEN 9999999994 -- M_MatchPO
		WHEN fac.AD_table_ID = 1000042 THEN amn.c_doctype_id -- AMN_Payroll
		ELSE 0 END AS  c_doctype_id,
	-- C_ElementValue Account Elements
	cev.value as account_value,
	cev.name as account_name
FROM adempiere.fact_acct fac
LEFT JOIN adempiere.c_elementvalue cev ON cev.c_elementvalue_id = fac.account_id
LEFT JOIN adempiere.c_bpartner bpa ON fac.c_bpartner_id = bpa.c_bpartner_id
LEFT JOIN adempiere.m_product mpr ON fac.m_product_id = mpr.m_product_id
LEFT JOIN adempiere.amn_employee emp ON emp.c_bpartner_id = bpa.c_bpartner_id
LEFT JOIN adempiere.amn_payroll amn ON amn.amn_payroll_id = fac.record_id
LEFT JOIN adempiere.amn_jobtitle as crg ON (amn.amn_jobtitle_id= crg.amn_jobtitle_id)
LEFT JOIN adempiere.ad_reference as ref ON(ref.name='AMN_Workforce')
LEFT JOIN adempiere.ad_ref_list as reflis ON (ref.ad_reference_id = reflis.ad_reference_id AND reflis.value =crg.workforce)
LEFT JOIN adempiere.ad_ref_list_trl as reflistr ON (reflis.ad_ref_list_id = reflistr.ad_ref_list_id AND reflistr.ad_language = 'es_VE')
LEFT JOIN adempiere.c_invoice inv ON inv.c_invoice_id = fac.record_id
LEFT JOIN adempiere.m_inout min ON min.m_inout_id = fac.record_id
LEFT JOIN adempiere.c_payment pay ON pay.c_payment_id = fac.record_id
LEFT JOIN adempiere.c_order ord ON ord.c_order_id = fac.record_id
LEFT JOIN adempiere.c_bankstatement cbs ON cbs.c_bankstatement_id = fac.record_id
LEFT JOIN adempiere.c_charge cha ON cha.c_charge_id = pay.c_charge_id
LEFT JOIN adempiere.c_cash cas ON cas.c_cash_id = fac.record_id
LEFT JOIN adempiere.c_allocationhdr cal ON cal.c_allocationhdr_id = fac.record_id
LEFT JOIN adempiere.gl_journal jou ON jou.gl_journal_id = fac.record_id
LEFT JOIN adempiere.m_movement mov ON mov.m_movement_id = fac.record_id
LEFT JOIN adempiere.m_requisition mrq ON mrq.m_requisition_id = fac.record_id
LEFT JOIN adempiere.m_inventory miv ON miv.m_inventory_id = fac.record_id
LEFT JOIN adempiere.m_production mpo ON mpo.m_production_id = fac.record_id
LEFT JOIN adempiere.m_matchinv mma ON mma.m_matchinv_id = fac.record_id
LEFT JOIN adempiere.m_matchpo mmp ON mmp.m_matchpo_id = fac.record_id
LEFT JOIN adempiere.ad_table tbl ON tbl.ad_table_id = fac.ad_table_id
;

ALTER TABLE amf_fact_accounts_analitic_v
  OWNER TO adempiere;
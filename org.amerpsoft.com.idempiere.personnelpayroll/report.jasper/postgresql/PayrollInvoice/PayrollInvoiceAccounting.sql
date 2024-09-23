-- PayrollInvoiceAccounting
-- Invoice C_Invoice_ID
-- Payroyll AMN_payroll
WITH FACTURA_RECEPCION AS (
	-- INVOICE
	SELECT
	-- INVOICE HEADER
	'1-INV' as invoice_recepcion,
	adempiere.c_invoice.c_invoice_id,
	adempiere.c_invoice.documentno as documentno_lin,
	-- FACT_ACCT LINE
	adempiere.c_elementvalue."value",
	adempiere.c_elementvalue.name,
	adempiere.fact_acct.description,
	to_char(adempiere.fact_acct.dateacct,'DD-MM-YYYY')  as dateacct,
	adempiere.c_acctschema.c_acctschema_id,
	adempiere.c_acctschema.name as acctschema_name,
	adempiere.c_acctschema.isactive as acctschema_isactive,
	adempiere.fact_acct.account_id,
	adempiere.fact_acct.amtacctdr,
	adempiere.fact_acct.amtacctcr,
	COALESCE(adempiere.c_doctype_trl.name,adempiere.c_doctype.name) as doctype_lin,
	COALESCE(adempiere.c_doctype_trl.printname,adempiere.c_doctype.printname) as doctype_lin2,
	COALESCE(adempiere.m_product.name,'') as product_name,
	COALESCE(adempiere.m_product.value,'') as product_value
	FROM
	adempiere.c_invoice
	LEFT JOIN adempiere.c_currency on adempiere.c_invoice.c_currency_id = adempiere.c_currency.c_currency_id
	LEFT JOIN adempiere.fact_acct on adempiere.c_invoice.c_invoice_id = adempiere.fact_acct.record_id
	LEFT JOIN adempiere.c_elementvalue on adempiere.fact_acct.account_id = adempiere.c_elementvalue.c_elementvalue_id
	LEFT JOIN adempiere.c_doctype on adempiere.c_doctype.c_doctype_id = adempiere.c_invoice.c_doctype_id
	LEFT JOIN adempiere.c_doctype_trl on adempiere.c_doctype.c_doctype_id = adempiere.c_doctype_trl.c_doctype_id  and adempiere.c_doctype_trl.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{C_Invoice_ID}  ))
	LEFT JOIN adempiere.c_acctschema on adempiere.c_acctschema.c_acctschema_id = adempiere.fact_acct.c_acctschema_id
	LEFT JOIN adempiere.m_product on adempiere.m_product.m_product_id = adempiere.fact_acct.m_product_id
	WHERE
	adempiere.c_invoice.c_invoice_id=$P{C_Invoice_ID} and adempiere.fact_acct.ad_table_id=318  -- Table c_invoice
	-- UNION 
	UNION
	-- UNION 
	-- AMN_Payroll
	SELECT
	-- AMN_Payroll
	'2-PAR' as invoice_recepcion,
	pyr.c_invoice_id,
	pyr.documentno as documentno_lin,
	-- FACT_ACCT LINE
	ele."value",
	ele.name,
	fact.description,
	to_char(fact.dateacct,'DD-MM-YYYY')  as dateacct,
	sch.c_acctschema_id,
	sch.name as acctschema_name,
	sch.isactive as acctschema_isactive,
	fact.account_id,
	fact.amtacctdr,
	fact.amtacctcr,
	COALESCE(dcttrl.name,dct.name) as doctype_lin,
	COALESCE(dcttrl.printname,dct.printname) as doctype_lin2,
	'' as product_name,
	'' as product_value
	FROM 
	(
		SELECT * 
		FROM adempiere.amn_payroll as pyr
		WHERE C_Invoice_id = $P{C_Invoice_ID}
	) as pyr
	INNER JOIN adempiere.c_invoice inv ON inv.c_invoice_id = pyr.c_invoice_id
	LEFT JOIN adempiere.fact_acct as fact ON (fact.record_id = pyr.amn_payroll_id AND fact.ad_table_id = (SELECT AD_Table_ID FROM adempiere.AD_Table WHERE TableName ='AMN_Payroll') )
	LEFT JOIN adempiere.c_elementvalue as ele ON (fact.account_id = ele.c_elementvalue_id)
	LEFT JOIN adempiere.amn_location as lct ON (lct.amn_location_id= pyr.amn_location_id)
	LEFT JOIN adempiere.amn_department as dpt ON (dpt.amn_department_id= pyr.amn_department_id)
	INNER JOIN adempiere.ad_org as org ON ( org.ad_org_id = pyr.ad_org_id)
	INNER JOIN adempiere.ad_client as cli ON (org.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.c_period as cper ON (cper.c_period_id = fact.c_period_id)
	LEFT JOIN adempiere.c_acctschema sch on sch.c_acctschema_id = fact.c_acctschema_id
	LEFT JOIN adempiere.c_doctype dct on dct.c_doctype_id = pyr.c_doctype_id
	LEFT JOIN adempiere.c_doctype_trl dcttrl on dcttrl.c_doctype_id = pyr.c_doctype_id  and dcttrl.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{C_Invoice_ID}  ))
	-- UNION 
)
select 
	-- ORGANIZATION
	adempiere.ad_org.value as org_value,
	coalesce(adempiere.ad_org.name,adempiere.ad_org.value,'') as org_name,
	coalesce(adempiere.ad_org.description,adempiere.ad_org.name,adempiere.ad_org.value,'') as org_description,
	coalesce(adempiere.ad_orginfo.taxid,'') as org_taxid,
	adempiere.ad_image.binarydata as org_logo,
	case  when c_location.address1 is null then '' else c_location.address1  end
	  ||
	case  when c_location.address2 is null then '' else ', ' || c_location.address2   end
	  ||
	case  when c_location.address3 is null then '' else ', ' || c_location.address3   end
	  ||
	case  when c_location.address4 is null then '' else ', ' || c_location.address4   end as org_address1,
	case  when c_location.city is null then '' else c_location.city   end
	  ||
	case  when c_location.regionname is null then '' else ', ' || c_location.regionname  end
	  ||
	case  when c_location.postal is null then '' else ', CP ' || c_location.postal end as org_address2 ,
	case  when ad_orginfo.phone is null then '' else ad_orginfo.phone   end
	  ||
	case  when ad_orginfo.phone2 is null then '' else ', ' || ad_orginfo.phone2   end as org_phone,
	case  when adempiere.ad_orginfo.fax is null then '' else 'Fax:' || adempiere.ad_orginfo.fax end  as org_fax ,
	case  when adempiere.ad_orginfo.email is null then '' else 'e-mail:' || adempiere.ad_orginfo.email end  as org_email ,
	-- BUSINESS PARTNER
	adempiere.c_bpartner.value as value_bp,
	adempiere.c_bpartner.name as name_bp,
	adempiere.c_bpartner.taxid as taxid_bp,
	COALESCE(adempiere.c_bpartner.amerp_nameseniat,adempiere.c_bpartner.name) as amerp_nameseniat,
	COALESCE(adempiere.c_bpartner.amerp_rifseniat,adempiere.c_bpartner.taxid) as amerp_rifseniat,
	-- BUSINESS PARTNER LOCATION
	adempiere.c_bpartner_location.name as bplocname,
	coalesce(adempiere.c_bpartner_location.phone,'') as phone,
	coalesce(adempiere.c_bpartner_location.phone2,'') as phone2,
	coalesce(adempiere.c_bpartner_location.fax,'') as fax,
	coalesce(c_bplocation.address1,'') as address1,
	coalesce(c_bplocation.address2,'') as address2,
	coalesce(c_bplocation.address3,'') as address3,
	coalesce(c_bplocation.address4,'') as address4,
	coalesce(c_bplocation.city,'') as city,
	coalesce(c_bplocation.regionname,'') as regionname,
	coalesce(c_bplocation.postal,'') as postal,
	-- INVOICE HEADER
	adempiere.c_invoice.docstatus,
	adempiere.c_invoice.c_invoice_id,
	adempiere.c_invoice.created,
	adempiere.c_invoice.documentno,
	COALESCE(adempiere.c_doctype_trl.name,adempiere.c_doctype.name) as printname,
	COALESCE(adempiere.c_doctype_trl.printname,adempiere.c_doctype.printname) as printnamel,
	adempiere.c_doctype.docbasetype,
--	case  when adempiere.c_invoice.amerp_documentaffected_id is null then '' else '' || adempiere.c_invoice.amerp_documentaffected_id end as amerp_documentaffected_id ,
	'' as amerp_controlnumber ,
--	case  when c_invoice2.documentno is null then '' else '' || c_invoice2.documentno end as docaffected_documentno ,
	case  when adempiere.c_invoice.description is null then '' else '' || adempiere.c_invoice.description  end  as description_head,
	use.name as usercode,
	use.description as username,
	salesrep."value" as repcode,
	salesrep.name as repname,
	-- INVOICE RECEPCION
	invoice_recepcion,
	-- Account Schema
	far.c_acctschema_id,
	far.acctschema_name,
	-- FACT_ACCT LINE
	far."value",
	far.name,
	far.description,
	far.dateacct,
	far.account_id,
	far.amtacctdr,
	far.amtacctcr,
	far.documentno_lin,
	far.doctype_lin,
	far.doctype_lin2,
	far.product_name,
	far.product_value
FROM
	adempiere.c_invoice
	LEFT JOIN adempiere.c_doctype on adempiere.c_doctype.c_doctype_id = adempiere.c_invoice.c_doctype_id
	LEFT JOIN adempiere.c_doctype_trl on adempiere.c_doctype.c_doctype_id = adempiere.c_doctype_trl.c_doctype_id  and adempiere.c_doctype_trl.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{C_Invoice_ID}  ))
	LEFT JOIN adempiere.ad_org ON adempiere.ad_org.ad_org_id = adempiere.c_invoice.ad_org_id
	LEFT JOIN adempiere.ad_orginfo  ON adempiere.ad_org.ad_org_id = adempiere.ad_orginfo.ad_org_id
	LEFT JOIN adempiere.ad_image ON adempiere.ad_orginfo.logo_id= adempiere.ad_image.ad_image_id
	LEFT JOIN adempiere.c_location ON adempiere.c_location.c_location_id = adempiere.ad_orginfo.c_location_id
	LEFT JOIN adempiere.c_bpartner ON adempiere.c_bpartner.c_bpartner_id = adempiere.c_invoice.c_bpartner_id
	LEFT JOIN adempiere.c_bpartner_location ON (adempiere.c_invoice.c_bpartner_location_id = adempiere.c_bpartner_location.c_bpartner_location_id)
	LEFT JOIN adempiere.c_location as c_bplocation on c_bplocation.c_location_id = adempiere.c_bpartner_location.c_location_id
	LEFT JOIN adempiere.ad_user as use ON use.ad_user_id = adempiere.c_invoice.createdby
	LEFT JOIN adempiere.ad_user as salesrep on salesrep.ad_user_id = adempiere.c_invoice.salesrep_id
	LEFT JOIN FACTURA_RECEPCION as far on far.c_invoice_id = adempiere.c_invoice.c_invoice_id
WHERE
	adempiere.c_invoice.c_invoice_id = $P{C_Invoice_ID} 
	AND far.acctschema_isactive = 'Y'
ORDER by  invoice_recepcion ASC, c_acctschema_id, value ASC
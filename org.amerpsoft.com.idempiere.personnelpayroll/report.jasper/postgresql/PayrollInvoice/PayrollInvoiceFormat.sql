SELECT
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
coalesce(adempiere.c_bpartner.amerp_nameseniat,adempiere.c_bpartner.name,'') as amerp_nameseniat,
coalesce(adempiere.c_bpartner.amerp_rifseniat,adempiere.c_bpartner.taxid,'') as amerp_rifseniat,
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
-- CURRENCY
adempiere.c_currency.iso_code,
adempiere.c_currency_trl.cursymbol,
-- CURRENCY
curr1.iso_code as iso_code1,
COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
curr2.iso_code as iso_code2,
COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2,
-- INVOICE HEADER
cinvo.docstatus,
cinvo.c_invoice_id,
cinvo.created,
adempiere.c_doctype.printname,
adempiere.c_doctype.docbasetype,
adempiere.c_doctype_trl.printname as printnamel,
adempiere.c_doctype_trl.name as docname,
CONCAT(cinvo.documentno,'/',cinvo.documentnoBP)  AS documentno,
cinvo.c_bpartner_id,
to_char(cinvo.created,'YYYY') AS invoiceyear,
to_char(cinvo.created,'MM') AS invoicemonth,
to_char(cinvo.created,'DD') AS invoiceday,
to_char(cinvo.created,'DD-MM-YYYY') AS invoicefecha,
to_char(cinvo.created,'YYYY-MM-DD') AS invoicefecha2,
to_char(cinvo.dateinvoiced,'DD-MM-YYYY') AS dateinvoiced,
to_char(cinvo.dateacct,'DD-MM-YYYY') AS dateacct,
cinvo.grandtotal,
cinvo.totallines,
coalesce(cinvo.withholdingamt,0) as withholdingamt,
cinvo.grandtotal-cinvo.totallines  + coalesce(cinvo.withholdingamt,0)  as taxtotal,
--currencyConvertInvoice(cinvo.c_invoice_id,cinvo.c_currency_id, cinvo.grandtotal, cinvo.dateacct ) as grandtotal2,
--currencyConvertInvoice(cinvo.c_invoice_id,cinvo.c_currency_id, cinvo.totallines, cinvo.dateacct ) as totallines2,
currencyConvertInvoice(cinvo.c_invoice_id,$P{C_Currency_ID}, coalesce(cinvo.withholdingamt,0), cinvo.dateacct ) as withholdingamt2,
currencyConvertInvoice(cinvo.c_invoice_id,$P{C_Currency_ID}, cinvo.grandtotal-cinvo.totallines, cinvo.dateacct ) as taxtotal2,
CASE WHEN conv.MultiplyRate IS NOT NULL
	THEN cinvo.grandtotal  * conv.MultiplyRate
    ELSE cinvo.grandtotal 
    END AS grandtotal2,
adempiere.amf_num2letter(cinvo.grandtotal,'U','es') as grandtotal_letter,
CASE WHEN conv.MultiplyRate IS NOT NULL
	THEN adempiere.amf_num2letter(cinvo.grandtotal * conv.MultiplyRate ,'U','es')
    ELSE adempiere.amf_num2letter(cinvo.grandtotal,'U','es') 
    END AS grandtotal_letter2,
CASE WHEN conv.MultiplyRate IS NOT NULL
	THEN cinvo.totallines * conv.MultiplyRate
	ELSE cinvo.totallines
	END AS totallines2,
--case  when cinvo.amerp_documentaffected_id is null then '' else '' || cinvo.amerp_documentaffected_id end as amerp_documentaffected_id ,
case  when cinvo.amerp_controlnumber is null then '' else '' || cinvo.amerp_controlnumber end as amerp_controlnumber ,
--case  when c_invoice2.documentno is null then '' else '' || c_invoice2.documentno end as docaffected_documentno ,
case  when cinvo.description is null then '' else '' || cinvo.description  end  as description_head,
cinvo.AMERP_DocumentAffected as docaffected_documentno ,
-- INVOICE LINE
cinvlin.m_product_id,
coalesce(adempiere.m_product.value,adempiere.c_charge.name,'') as codigo_prod,
coalesce(adempiere.m_product.name,adempiere.c_charge.description,adempiere.c_charge.name,'') as name_prod,
coalesce(adempiere.m_product.description,'') as description_prod,
case  when cinvlin.description is null then '' else '' || cinvlin.description  end  as description_line,
cinvlin.qtyinvoiced,
cinvlin.pricelist,
cinvlin.priceentered,
cinvlin.linenetamt,
adempiere.c_tax.rate as lintaxrate,
adempiere.c_tax.rate/100 as lintaxfactor,
cinvlin.taxamt as lintaxamt,
cinvlin.linetotalamt,
-- INVOICE LINE 2 Currency
CASE WHEN conv.MultiplyRate IS NOT NULL
	THEN cinvlin.pricelist * conv.MultiplyRate
	ELSE cinvlin.pricelist
END as pricelist2,
CASE WHEN conv.MultiplyRate IS NOT NULL
	THEN cinvlin.priceentered * conv.MultiplyRate
	ELSE cinvlin.priceentered
END as priceentered2,
CASE WHEN conv.MultiplyRate IS NOT NULL
	THEN cinvlin.linenetamt * conv.MultiplyRate
	ELSE cinvlin.linenetamt
END as linenetamt2,
cinvo.ad_user_id,
adempiere.ad_user.name as usercode,
adempiere.ad_user.description as username,
adempiere.c_uom."name" as uom_name,
adempiere.c_tax.rate as taxrate,
taxres.taxbaseamt_gen+taxres.taxbaseamt_red + taxres.taxbaseamt_adi as taxbaseamt,
taxres.taxamt_gen+taxres.taxamt_red + taxres.taxamt_adi as taxamt
FROM
adempiere.c_invoice as cinvo
--LEFT JOIN adempiere.c_invoice as c_invoice2 on cinvo.amerp_documentaffected_id = c_invoice2.c_invoice_id
LEFT JOIN adempiere.c_currency on cinvo.c_currency_id = adempiere.c_currency.c_currency_id
LEFT JOIN adempiere.c_currency_trl on adempiere.c_currency.c_currency_id = adempiere.c_currency_trl.c_currency_id and adempiere.c_currency_trl.ad_language = 'es_VE'
LEFT JOIN adempiere.c_invoiceline as cinvlin on cinvo.c_invoice_id = cinvlin.c_invoice_id
LEFT JOIN adempiere.c_uom on cinvlin.c_uom_id = adempiere.c_uom.c_uom_id
LEFT JOIN adempiere.m_product ON adempiere.m_product.m_product_id = cinvlin.m_product_id
LEFT JOIN adempiere.c_charge ON adempiere.c_charge.c_charge_id = cinvlin.c_charge_id
LEFT JOIN adempiere.c_tax on cinvlin.c_tax_id = adempiere.c_tax.c_tax_id
LEFT JOIN adempiere.c_doctype on adempiere.c_doctype.c_doctype_id = cinvo.c_doctypetarget_id
LEFT JOIN adempiere.c_doctype_trl on adempiere.c_doctype.c_doctype_id = adempiere.c_doctype_trl.c_doctype_id
  AND adempiere.c_doctype_trl.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{RECORD_ID}  )) 
LEFT JOIN adempiere.ad_org ON adempiere.ad_org.ad_org_id = cinvo.ad_org_id
LEFT JOIN adempiere.ad_orginfo  ON adempiere.ad_org.ad_org_id = adempiere.ad_orginfo.ad_org_id
LEFT JOIN adempiere.ad_image ON adempiere.ad_orginfo.logo_id= adempiere.ad_image.ad_image_id
LEFT JOIN adempiere.c_location ON adempiere.c_location.c_location_id = adempiere.ad_orginfo.c_location_id
LEFT JOIN adempiere.c_bpartner ON adempiere.c_bpartner.c_bpartner_id = cinvo.c_bpartner_id
LEFT JOIN adempiere.c_bpartner_location ON adempiere.c_bpartner_location.c_bpartner_location_id = cinvo.c_bpartner_location_id
LEFT JOIN adempiere.c_location as c_bplocation on c_bplocation.c_location_id = adempiere.c_bpartner_location.c_location_id
LEFT JOIN adempiere.ad_user ON adempiere.ad_user.ad_user_id = cinvo.createdby
-- Currency
LEFT JOIN c_currency curr1 on cinvo.c_currency_id = curr1.c_currency_id
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{RECORD_ID}  ))
LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{RECORD_ID}  ))
LEFT JOIN ( 
	SELECT 
	C_invoice_ID,
	$P{C_Currency_ID} as Rep_Currency_ID, 
	CASE WHEN IsOverrideCurrencyRate ='Y'  THEN CurrencyRate 
		ELSE currencyRate (C_Currency_ID, $P{C_Currency_ID}, DateAcct, C_ConversionType_ID, AD_Client_ID, AD_org_ID) END as MultiplyRate
	FROM C_invoice 
) as conv ON conv.C_Invoice_ID=  $P{RECORD_ID} 
LEFT JOIN (
		SELECT 
		c_invoice_id , 
		sum(taxbaseamt_exe) as taxbaseamt_exe, 
		sum(taxbaseamt_gen) as taxbaseamt_gen, 
		sum(taxbaseamt_red) as taxbaseamt_red, 
		sum(taxbaseamt_adi) as taxbaseamt_adi,
		sum(taxamt_gen) as taxamt_gen, 
		sum(taxamt_red) as taxamt_red, 
		sum(taxamt_adi) as taxamt_adi
		FROM (
			SELECT 
				c_invoice_id,
				CASE WHEN taxindicator = 'IVAEXE' then taxbaseamt ELSE 0 END as taxbaseamt_exe,
				CASE WHEN taxindicator = 'IVAGEN' then taxbaseamt ELSE 0 END as taxbaseamt_gen,
				CASE WHEN taxindicator = 'IVARED' then taxbaseamt ELSE 0 END as taxbaseamt_red,
				CASE WHEN taxindicator = 'IVAADI' then taxbaseamt ELSE 0 END as taxbaseamt_adi,
				CASE WHEN taxindicator = 'IVAGEN' then taxamt ELSE 0 END as taxamt_gen,
				CASE WHEN taxindicator = 'IVARED' then taxamt ELSE 0 END as taxamt_red,
				CASE WHEN taxindicator = 'IVARED' then taxamt ELSE 0 END as taxamt_adi
			FROM (
				SELECT C_invoice_id, SUM(linenetamt) as taxbaseamt , SUM(taxamt) as taxamt , rate, taxname, taxindicator
				FROM (
					SELECT
					inv.C_invoice_id, inl.c_tax_id, 
					inl.linenetamt,  
					inl.taxamt, 
					txx.rate, 
					txx.name as taxname, 
					txx.taxindicator
					FROM C_invoice as inv
					LEFT JOIN c_invoiceline as inl ON inl.c_invoice_id= inv.c_invoice_id
					LEFT JOIN C_tax as txx ON txx.c_tax_id = inl.c_tax_id
				) AS imp
			--WHERE imp.c_invoice_id=1000010
			GROUP BY imp.c_invoice_id, imp.c_tax_id, imp.rate, imp.taxname, imp.taxindicator
			) AS impres
		) as impres2
		GROUP BY impres2.c_invoice_id
) as taxres ON cinvlin.c_invoice_id = taxres.c_invoice_id 
WHERE
cinvo.c_invoice_id=$P{RECORD_ID} 
ORDER BY cinvlin.line ASC
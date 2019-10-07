SELECT
-- ORGANIZATION
adempiere.ad_org.value as org_value,
coalesce(adempiere.ad_org.name,adempiere.ad_org.value,'') as org_name,
coalesce(adempiere.ad_org.description,adempiere.ad_org.name,adempiere.ad_org.value,'') as org_description,
coalesce(adempiere.ad_orginfo.taxid,'') as org_taxid,
adempiere.ad_image.binarydata as org_logo,
case  when c_location.address1 is null then '' else c_location.address1  || ', ' end
  ||
case  when c_location.address2 is null then '' else c_location.address2 || ', '  end
  ||
case  when c_location.address3 is null then '' else c_location.address3  || ', '  end
  ||
case  when c_location.address4 is null then '' else c_location.address4  || ', ' end
  ||
case  when c_location.city is null then '' else c_location.city  || ', '  end
  ||
case  when c_location.regionname is null then '' else c_location.regionname || ', '    end
  ||
case  when c_location.postal is null then '' else c_location.postal end as org_address ,
-- Business Partner
adempiere.c_bpartner.name as bpname,
adempiere.c_bpartner.taxid as bptaxid,
adempiere.c_bpartner.amerp_nameseniat,
adempiere.c_bpartner.amerp_rifseniat,
-- LCO_InvoiceWHDoc
to_char(liwdo.datedoc,'DD-MM-YYYY') AS invoicefecha,
to_char(liwdo.datedoc,'YYYY') AS invoiceyear,
to_char(liwdo.datedoc,'MMY') AS invoicemonth,
to_char(liwdo.datedoc,'DD') AS invoiceday,
-- Invoice
cinvo.c_invoice_id,
adempiere.c_doctype.printname,
adempiere.c_doctype.docbasetype,
cinvo.documentno,
cinvo.documentnoBP  AS documentnoBP,
cinvo.dateinvoiced,
cinvo.dateacct,
to_char(cinvo.dateacct,'DD-MM-YYYY') AS accinvoicefecha,
case when to_char(cinvo.dateinvoiced,'YYYYMM') < to_char(cinvo.dateacct,'YYYYMM') then '04 Aju' else '01' end as tipotran,
cinvo.grandtotal,
cinvo.totallines,
cinvo.withholdingamt,
cinvo.amerp_documentaffected_id,
cinvo.amerp_controlnumber,
-- VAT Withholding
coalesce(taxvwh.rate,0) as ratewh,
taxcatvwh.withholdingformat,
coalesce(lcoinvwh1.percent,0) as percentwh,
coalesce(lcoinvwh1.taxamt,0) as taxamtwh,
coalesce(lcoinvwh1.taxbaseamt,0) as taxbaseamtwh,
lcoinvwh1.documentno as documentnowh,
-- INVOICE VAT
cinvt.c_tax_id as taxvat_id,
taxvat.rate as ratevat,
case  when taxvat.rate = 0 then cinvt.taxbaseamt else 0   end as totalsinder ,
cinvt.taxbaseamt as taxbaseamtvat,
cinvt.taxamt as taxamtvat
FROM
-- LCO_InvoiceWHDoc  - LCO_InvoiceWHDocLines
adempiere.lco_invoicewhdoc as liwdo
LEFT JOIN adempiere.lco_invoicewhdoclines as liwdl ON (liwdo.lco_invoicewhdoc_id = liwdl.lco_invoicewhdoc_id)
-- C_Invoice_ID
LEFT JOIN adempiere.c_invoice as cinvo ON (cinvo.c_invoice_id = liwdl.c_invoice_id)
-- VAT Withholding 
LEFT JOIN adempiere.c_invoicetax as cinvt ON ( cinvt.c_invoice_id = cinvo.c_invoice_id )
LEFT JOIN adempiere.c_tax as taxvat ON (cinvt.c_tax_id = taxvat.c_tax_id )
LEFT JOIN adempiere.c_taxcategory as taxcatvat ON taxvat.c_taxcategory_id = taxcatvat.c_taxcategory_id
LEFT JOIN adempiere.lco_invoicewithholding as lcoinvwh1 ON (cinvo.c_invoice_id = lcoinvwh1.c_invoice_id  AND  cinvt.c_tax_id = lcoinvwh1.c_basetax_id )
LEFT JOIN adempiere.c_tax as taxvwh ON lcoinvwh1.c_tax_id = taxvwh.c_tax_id
LEFT JOIN adempiere.c_taxcategory as taxcatvwh ON taxvwh.c_taxcategory_id = taxcatvwh.c_taxcategory_id
-- Doctype Org
LEFT JOIN adempiere.c_doctype on adempiere.c_doctype.c_doctype_id = cinvo.c_doctype_id
LEFT JOIN adempiere.ad_org ON adempiere.ad_org.ad_org_id = cinvo.ad_org_id
LEFT JOIN adempiere.ad_orginfo  ON adempiere.ad_org.ad_org_id = adempiere.ad_orginfo.ad_org_id
LEFT JOIN adempiere.ad_image ON adempiere.ad_orginfo.logo_id= adempiere.ad_image.ad_image_id
LEFT JOIN adempiere.c_location ON adempiere.c_location.c_location_id = adempiere.ad_orginfo.c_location_id
LEFT JOIN adempiere.c_bpartner ON adempiere.c_bpartner.c_bpartner_id = cinvo.c_bpartner_id
WHERE
taxvat.isActive='Y' 
AND taxcatvat.isWithholding='N'
AND liwdo.lco_invoicewhdoc_id=$P{RECORD_ID}
GROUP BY
liwdo.datedoc,
adempiere.ad_org.value ,
adempiere.ad_org.name ,
adempiere.ad_org.description ,
adempiere.ad_orginfo.taxid ,
adempiere.ad_image.binarydata ,
org_address,
cinvo.c_invoice_id,
adempiere.c_doctype.printname,
adempiere.c_doctype.docbasetype,
taxvwh.rate,
taxcatvwh.withholdingformat,
taxvat.rate,
cinvt.c_tax_id,
cinvt.taxbaseamt,
cinvt.taxamt,
adempiere.c_bpartner.name,
adempiere.c_bpartner.taxid,
adempiere.c_bpartner.amerp_nameseniat,
adempiere.c_bpartner.amerp_rifseniat,
lcoinvwh1.percent,
lcoinvwh1.taxamt,
lcoinvwh1.taxbaseamt,
lcoinvwh1.documentno
ORDER BY
cinvo.c_invoice_id ASC, taxvat.rate ASC
-- SELECT ALL adempiere.c_taxcategory fields from compiere.c_taxcategory
SELECT "C_TAXCATEGORY_ID" AS c_taxcategory_id , "AD_CLIENT_ID" AS ad_client_id, "AD_ORG_ID" AS ad_org_id , "ISACTIVE" AS isactive , CAST("CREATED" AS timestamp) AS created, "CREATEDBY" AS createdby,
CAST("UPDATED" AS timestamp) AS updated, "UPDATEDBY" AS updatedby, "NAME" AS name, "DESCRIPTION" AS decription, "COMMODITYCODE" AS commoditycode, "ISDEFAULT" AS isdefault, 
uuid_in(md5(random()::text || random()::text)::cstring) AS c_taxcategory_uu, 'N' AS iswithholding, '' AS withholdingformat
FROM compiere.c_tax_category ctc 
WHERE "AD_CLIENT_ID" =1000000


INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
(
	SELECT "C_TAXCATEGORY_ID" AS c_taxcategory_id , "AD_CLIENT_ID" AS ad_client_id, "AD_ORG_ID" AS ad_org_id , "ISACTIVE" AS isactive , CAST("CREATED" AS timestamp) AS created, "CREATEDBY" AS createdby,
	CAST("UPDATED" AS timestamp) AS updated, "UPDATEDBY" AS updatedby, "NAME" AS name, "DESCRIPTION" AS decription, "COMMODITYCODE" AS commoditycode, "ISDEFAULT" AS isdefault, 
	uuid_in(md5(random()::text || random()::text)::cstring) AS c_taxcategory_uu, 'N' AS iswithholding, '' AS withholdingformat
	FROM compiere.c_tax_category ctc 
	WHERE "AD_CLIENT_ID" =1000000
)

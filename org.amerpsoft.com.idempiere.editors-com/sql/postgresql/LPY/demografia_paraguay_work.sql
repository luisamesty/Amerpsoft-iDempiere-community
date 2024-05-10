-- QUERIES FOR CREATING C_Community, C_region, C_City, C_Municipality, C_Parish
-- FROM demografia_paraguay.sql
-- Temporary datababse
INSERT INTO adempiere.c_country (c_country_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"name",description,countrycode,hasregion,regionname,expressionphone,displaysequence,expressionpostal,haspostal_add,expressionpostal_add,ad_language,c_currency_id,displaysequencelocal,isaddresslinesreverse,isaddresslineslocalreverse,expressionbankroutingno,expressionbankaccountno,mediasize,ispostcodelookup,lookupclassname,lookupclientid,lookuppassword,lookupurl,allowcitiesoutoflist,capturesequence,c_country_uu,countrycode3,hascommunity,hasmunicipality,hasparish,placeholderaddress1,placeholderaddress2,placeholderaddress3,placeholderaddress4,placeholderaddress5,placeholdercity,placeholdercomments,placeholderpostal,placeholderpostal_add) VALUES
	 (276,0,0,'Y','2003-03-09 00:00:00',0,'2024-04-15 12:12:04.494',0,'Paraguay','the Republic of Paraguay','PY','Y',NULL,NULL,'@C@,  @P@',NULL,'N',NULL,'es_PY',344,NULL,'N','N',NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,'Y','@A1@ @A2@ @A3@ @A4@ @C@,  @P@ @CO@','e7fc9e42-fa61-4a20-afc8-590b0a4df58f','PRY','Y','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);	

SELECT DISTINCT ON (c_region_id ) c_region_id , region_name FROM demografia ORDER BY  c_region_id ;
-- C_Country_ID=276

-- TABLE C_Community
DELETE FROM adempiere.c_community WHERE c_country_ID=276;
INSERT INTO adempiere.c_community (c_community_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"name",description,c_country_id,isdefault,c_community_uu) VALUES
	 (1000270,0,0,'Y',now(),0,now(),0,'Comunidad Paraguay','Comunidad Paraguay',276,'N','a457885d-baa2-a51a-0b57-3c74e6b720e7');

-- TABLE C_Region
-- SELECT
SELECT DISTINCT ON (c_region_id ) 
1027600+c_region_id,0 AS ad_client_id,0 AS ad_org_id,'Y' AS isactive,now() AS created,0 AS createdby,now() AS updated,0 AS updatedby,region_name AS "name",region_name AS description,276 AS c_country_id,'N' AS isdefault,
uuid_in(md5(random()::text || random()::text)::cstring) AS c_region_uu,1027600 ASc_community_id 
FROM demografia ORDER BY  c_region_id ;
-- INSERT
INSERT INTO adempiere.c_region (c_region_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"name",description,c_country_id,isdefault,c_region_uu,c_community_id) 
(
	SELECT DISTINCT ON (c_region_id ) 
	1027600+c_region_id,0 AS ad_client_id,0 AS ad_org_id,'Y' AS isactive,now() AS created,0 AS createdby,now() AS updated,0 AS updatedby,region_name AS "name",region_name AS description,276 AS c_country_id,'N' AS isdefault,
	uuid_in(md5(random()::text || random()::text)::cstring) AS c_region_uu,1027600 ASc_community_id 
	FROM demografia ORDER BY  c_region_id
)

-- TABLE C_City
-- SELECT
SELECT DISTINCT ON (c_region_id, c_city_id ) 
12760000+c_city_id,0 AS ad_client_id,0 AS ad_org_id,'Y' AS isactive,now() AS created,0 AS createdby,now() AS updated,0 AS updatedby,city_name  as "name",
NULL AS locode,NULL AS coordinates,NULL AS postal,NULL AS areacode,276 AS c_country_id,1027600+c_region_id AS c_region_id,
uuid_in(md5(random()::text || random()::text)::cstring) AS c_city_uu
FROM demografia 
WHERE c_city_id IS NOT NULL 
ORDER BY  c_region_id , c_city_id;
-- INSERT
INSERT INTO adempiere.c_city (c_city_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"name",locode,coordinates,postal,areacode,c_country_id,c_region_id,c_city_uu) 
(
	SELECT DISTINCT ON (c_region_id, c_city_id ) 
	12760000+c_city_id,0 AS ad_client_id,0 AS ad_org_id,'Y' AS isactive,now() AS created,0 AS createdby,now() AS updated,0 AS updatedby,city_name  as "name",
	NULL AS locode,NULL AS coordinates,NULL AS postal,NULL AS areacode,276 AS c_country_id,1027600+c_region_id AS c_region_id,
	uuid_in(md5(random()::text || random()::text)::cstring) AS c_city_uu
	FROM demografia WHERE c_city_id IS NOT NULL 
	ORDER BY  c_region_id , c_city_id
)


-- TABLE C_Municipality 
-- SELECT
SELECT DISTINCT ON (c_region_id, c_municipality_id) 
12760000+c_municipality_id AS c_municipality_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' AS isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, municipality_name AS "name", 
'' AS capital, 276 AS c_country_id, 1027600+c_region_id AS c_region_id,'N' AS isdefault, uuid_in(md5(random()::text || random()::text)::cstring) AS c_municipality_uu
FROM demografia 
WHERE c_municipality_id IS NOT NULL AND c_city_id IS NOT NULL 
ORDER BY  c_region_id , c_municipality_id;
-- INSERT 
INSERT INTO c_municipality
(c_municipality_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", capital, c_country_id, c_region_id, isdefault, c_municipality_uu)
	(SELECT DISTINCT ON (c_region_id, c_municipality_id) 
12760000+c_municipality_id AS c_municipality_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' AS isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, municipality_name AS "name", 
'' AS capital, 276 AS c_country_id, 1027600+c_region_id AS c_region_id,'N' AS isdefault, 
uuid_in(md5(random()::text || random()::text)::cstring) AS c_municipality_uu
FROM demografia 
WHERE c_municipality_id IS NOT NULL AND c_city_id IS NOT NULL 
ORDER BY  c_region_id , c_municipality_id)
;
UPDATE adempiere.c_municipality  SET capital="name" where c_country_id=276 ;


-- TABLE C_Parish
-- DELETE
DELETE FROM c_parish WHERE AD_Client_ID = 0;
COMMIT;
-- SELECT
SELECT  DISTINCT ON (c_region_id, c_municipality_id, c_parish_id)  
12760000+c_parish_id AS c_parish_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' AS isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, parish_name AS "name", 276 AS c_country_id, 
1027600+c_region_id AS c_region_id, 12760000+c_municipality_id AS c_municipality_id, 'N' AS isdefault, 
uuid_in(md5(random()::text || random()::text)::cstring) AS c_parish_uu
FROM demografia 
WHERE c_municipality_id IS NOT NULL AND c_city_id IS NOT NULL AND C_parish_id IS NOT NULL
ORDER BY  c_region_id , c_municipality_id, c_parish_id;
-- INSERT
INSERT INTO c_parish
(c_parish_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", c_country_id, c_region_id, c_municipality_id, isdefault, c_parish_uu)
(
	SELECT  DISTINCT ON (c_region_id, c_municipality_id, c_parish_id)  
	12760000+c_parish_id AS c_parish_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' AS isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, parish_name AS "name", 276 AS c_country_id, 
	1027600+c_region_id AS c_region_id, 12760000+c_municipality_id AS c_municipality_id, 'N' AS isdefault, 
	uuid_in(md5(random()::text || random()::text)::cstring) AS c_parish_uu
	FROM demografia 
	WHERE c_municipality_id IS NOT NULL AND c_city_id IS NOT NULL AND C_parish_id IS NOT NULL
	ORDER BY  c_region_id , c_municipality_id, c_parish_id
);


-- QUERIES FOR CREATING  C_City
-- FROM WorldDemography
-- Temporary database
INSERT INTO c_country (c_country_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,"name",description,countrycode,hasregion,regionname,expressionphone,displaysequence,expressionpostal,haspostal_add,expressionpostal_add,ad_language,c_currency_id,displaysequencelocal,isaddresslinesreverse,isaddresslineslocalreverse,expressionbankroutingno,expressionbankaccountno,mediasize,ispostcodelookup,lookupclassname,lookupclientid,lookuppassword,lookupurl,allowcitiesoutoflist,capturesequence,c_country_uu,countrycode3,hascommunity,hasmunicipality,hasparish,placeholderaddress1,placeholderaddress2,placeholderaddress3,placeholderaddress4,placeholderaddress5,placeholdercity,placeholdercomments,placeholderpostal,placeholderpostal_add) VALUES
	 (276,0,0,'Y','2003-03-09 00:00:00',0,'2024-04-15 12:12:04.494',0,'Paraguay','the Republic of Paraguay','PY','Y',NULL,NULL,'@C@,  @P@',NULL,'N',NULL,'es_PY',344,NULL,'N','N',NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,'Y','@A1@ @A2@ @A3@ @A4@ @C@,  @P@ @CO@','e7fc9e42-fa61-4a20-afc8-590b0a4df58f','PRY','Y','N','N',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);	

SELECT DISTINCT ON (c_region_id ) c_region_id , region_name FROM demografia ORDER BY  c_region_id ;
-- C_Country_ID=276

-- SELECT
SELECT  * FROM mylan_worldcities_alln mwa ; 
SELECT count(*) AS norecs, count(DISTINCT mwa.id) AS no_distincts FROM mylan_worldcities_alln mwa ;
-- 44691 records in Table
-- SELECT JOIN WITH c_country3
SELECT DISTINCT  wc.id, cc.c_country_id, wc.city, wc.city_ascii, wc.lat, wc.lng, wc.country, wc.iso2, wc.iso3, wc.admin_name, wc.capital, wc.population
FROM public.mylan_worldcities_alln AS wc
INNER JOIN c_country3 cc ON cc.countrycode = wc.iso2 
ORDER BY wc.id;

-- C_City
-- SELECT TEMPLATE
SELECT c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", locode, coordinates, postal, areacode, c_country_id, c_region_id, c_city_uu, value, name2, admin_name, lat, lng, population
FROM adempiere.c_city;
-- SELECT From mylan_worldcities_alln JOIN to c_country3 TO GET c_city Fields
SELECT DISTINCT ON (wc.id) wc.id AS c_city_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' AS isactive, now() AS created, 0 AS createdby,
now() AS updated, 0 AS updatedby, wc.city AS "name", '' AS locode, 
LEFT(CONCAT(TRIM(wc.lat::TEXT),';',TRIM(wc.lng::TEXT)),15) AS  coordinates, NULL AS postal, NULL AS areacode, cc.c_country_id AS c_country_id, 
NULL AS c_region_id, uuid_in(md5(random()::text || random()::text)::cstring) AS c_city_uu, 
NULL AS value, wc.city_ascii AS name2, wc.admin_name AS admin_name, wc.lat AS lat, wc.lng AS lng, wc.population AS population
FROM public.mylan_worldcities_alln AS wc
INNER JOIN c_country3 cc ON cc.countrycode = wc.iso2 ;

-- INSERT into C_City from JOIN View
-- INSERT TEMPLATE
INSERT INTO adempiere.c_city
(c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", locode, coordinates, postal, areacode, c_country_id, c_region_id, c_city_uu, value, name2, admin_name, lat, lng, population)
VALUES(0, 0, 0, 'Y'::bpchar, now(), 0, now(), 0, '', '', '', '', '', 0, 0, NULL::character varying, NULL::character varying, NULL::character varying, NULL::character varying, 0, 0, 0);
-- INSERT WITH SELECT QUERY
INSERT INTO c_city
(c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", locode, coordinates, postal, areacode, c_country_id, c_region_id, c_city_uu, value, name2, admin_name, lat, lng, population)
(
	SELECT DISTINCT ON (wc.id) wc.id AS c_city_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' AS isactive, now() AS created, 0 AS createdby,
	now() AS updated, 0 AS updatedby, wc.city AS "name", '' AS locode, 
	LEFT(CONCAT(TRIM(wc.lat::TEXT),';',TRIM(wc.lng::TEXT)),15) AS  coordinates, NULL AS postal, NULL AS areacode, cc.c_country_id AS c_country_id, 
	NULL AS c_region_id, uuid_in(md5(random()::text || random()::text)::cstring) AS c_city_uu, 
	NULL AS value, wc.city_ascii AS name2, wc.admin_name AS admin_name, wc.lat AS lat, wc.lng AS lng, wc.population AS population
	FROM public.mylan_worldcities_alln AS wc
	INNER JOIN c_country3 cc ON cc.countrycode = wc.iso2
);
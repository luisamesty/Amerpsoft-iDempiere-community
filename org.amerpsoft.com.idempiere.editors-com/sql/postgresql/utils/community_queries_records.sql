-- This Query Creates, Update and Deletes Community Records created by Pack IN
-- For Maintanance purposes only
-- For executing this Query The Table C_Country3 must be creted before usin Pack-IN 
-- 	AMERSOFT Editor -CountryCode3.zip

-- SELECT C_Country3
SELECT c_country_id, countrycode, countrycode3, "name", description, modificado
FROM adempiere.c_country3;

-- SELECT DEfault Communities
SELECT c_country_id+1000000 AS c_community_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, 
CONCAT('Community-',name) AS "name", CONCAT('The Main Community of ',description) AS description, c_country_id, 'N' AS isdefault, NULL::character varying
FROM adempiere.c_country3;

-- SELECT Missing Communities
SELECT c_country_id+1000000 AS c_community_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, 
CONCAT('Community-',name) AS "name", CONCAT('The Main Community of ',description) AS description, c_country_id, 'N' AS isdefault, NULL::character varying
FROM adempiere.c_country3
WHERE c_country_id NOT IN (
	SELECT c_country_id FROM adempiere.c_community 
);

-- INSERT MISSING Communities
INSERT INTO adempiere.c_community (
	SELECT c_country_id+1000000 AS c_community_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' isactive, now() AS created, 0 AS createdby, now() AS updated, 0 AS updatedby, 
	CONCAT('Community-',name) AS "name", CONCAT('The Main Community of ',description) AS description, c_country_id, 'N' AS isdefault, NULL::character varying
	FROM adempiere.c_country3
	WHERE c_country_id NOT IN (
		SELECT c_country_id FROM adempiere.c_community 
	)
);

-- UPDATE Default regions with default Comunnity of the country 
-- ONLY For Nulls values on C_Community ID
UPDATE adempiere.C_Region
SET C_Community_ID = (
	SELECT c_country_id+1000000 
	FROM adempiere.c_country3
	WHERE adempiere.c_country3.c_country_id = adempiere.C_Region.c_country_id
	AND c_community_id IS NULL
);

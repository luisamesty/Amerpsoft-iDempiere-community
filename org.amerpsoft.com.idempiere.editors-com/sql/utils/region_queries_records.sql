-- BEFORE DELETING Communities Must be Unlink Regions Associated

-- SET ALL C_Community_ID to null to avoid relation error
UPDATE c_region SET c_community_id = NULL 
WHERE ad_client_id=0

-- SET ALL C_Community_ID to null ONLY Default Communities to avoid relation error
UPDATE c_region SET c_community_id = NULL 
WHERE ad_client_id=0 AND c_community_id IN (
	SELECT c_country_id+1000000 AS c_community_id
	FROM adempiere.c_country3
)

-- SELECT REGIONS
SELECT * FROM adempiere.c_region

-- UPDATE Default regions with default Comunnity of the country 
-- ONLY For Nulls values on C_Community ID
UPDATE adempiere.C_Region
SET C_Community_ID = (
	SELECT c_country_id+1000000 
	FROM adempiere.c_country3
	WHERE adempiere.c_country3.c_country_id = adempiere.C_Region.c_country_id
	AND c_community_id IS NULL
);




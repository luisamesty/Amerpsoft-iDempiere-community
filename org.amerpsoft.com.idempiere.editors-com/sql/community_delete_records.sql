-- This Query Creates, Update and Deletes Community Records created by Pack IN
-- For Maintanance purposes only
-- For executing this Query The Table C_Country3 must be creted before usin Pack-IN 
-- 	AMERSOFT Editor -CountryCode3.zip

-- ***************  OJO   *********
-- BEGIN DELETE COMMUNITIES
-- ***************  OJO   *********
-- SET ALL C_Community_ID to null to avoid relation error
UPDATE adempiere.c_region SET c_community_id = NULL 
WHERE ad_client_id=0

-- SET ALL C_Community_ID to null ONLY Default Communities to avoid relation error
UPDATE adempiere.c_region SET c_community_id = NULL 
WHERE ad_client_id=0 AND c_community_id IN (
	SELECT c_country_id+1000000 AS c_community_id
	FROM adempiere.c_country3
)
-- DELETE C_COmmunities
DELETE FROM adempiere.C_Community WHERE AD_Client_ID = 0

-- Select C_Community Records
SELECT * FROM adempiere.C_Community
-- ***************  OJO   *********
-- END DELETE COMMUNITIES
-- ***************  OJO   *********


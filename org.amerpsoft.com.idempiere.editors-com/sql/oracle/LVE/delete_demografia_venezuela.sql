-- DELETES DEMOGRAFIA VENEZUELA
-- ONLY TO BE USED IF NECESSARY DURING Intalling.
-- **** CAUTION *****

DELETE FROM adempiere.C_parish  WHERE C_country_id= 339;
COMMIT;

DELETE FROM adempiere.C_Municipality  WHERE C_country_id= 339;
COMMIT;

-- Exclude world cities
DELETE FROM adempiere.C_City  WHERE C_country_id= 339 AND C_City_ID < 1000000000;
COMMIT;

DELETE FROM adempiere.C_region  WHERE C_country_id= 339;
COMMIT;

DELETE FROM c_community  WHERE C_country_id= 339 
AND c_community_id IN (33901,33902,33903,33904,33905,33906,33907,33908,33909);
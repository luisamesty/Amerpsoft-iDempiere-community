-- DELETES DEMOGRAFIA PARAGUAY
-- ONLY TO BE USED IF NECESSARY DURING Intalling.
-- **** CAUTION *****

DELETE FROM adempiere.C_parish  WHERE C_country_id= 276;
COMMIT;

DELETE FROM adempiere.C_Municipality  WHERE C_country_id= 276;
COMMIT;

DELETE FROM adempiere.C_City  WHERE C_country_id= 276;
COMMIT;

DELETE FROM adempiere.C_region  WHERE C_country_id= 276;
COMMIT;
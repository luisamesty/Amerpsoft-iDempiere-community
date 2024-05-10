-- INSERT MISSING Communities from c_country3
INSERT INTO adempiere.c_community (
	SELECT c_country_id+1000000 AS c_community_id, 0 AS ad_client_id, 0 AS ad_org_id, 'Y' isactive, TIMESTAMP'2024-05-09 00:00:00' AS created, 0 AS createdby, TIMESTAMP'2024-05-09 00:00:00' AS updated, 0 AS updatedby,
	concat('Community-' , "name") AS "name", concat('The Main Community of ' , description) AS description, c_country_id, 'N' AS isdefault ,
	ADEMPIERE.generate_uuid
	FROM adempiere.c_country3
	WHERE c_country_id NOT IN (
		SELECT c_country_id FROM adempiere.c_community 
	)
);
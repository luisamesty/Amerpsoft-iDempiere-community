-- DEMOGRAFIA VENEZOLANA
SELECT * FROM C_Community WHERE C_Country_ID=339;
SELECT * FROM c_region WHERE C_Country_ID=339;
SELECT * FROM C_City WHERE C_Country_ID=339;
SELECT * FROM C_Municipality WHERE C_Country_ID=339;
SELECT * FROM C_parish WHERE C_Country_ID=339;

uuid_in(md5(random()::text || random()::text)::cstring)
UPDATE C_Community SET c_community_uu = uuid_in(md5(random()::text || random()::text)::cstring) WHERE C_Country_id = 339;
UPDATE C_region SET C_region_uu = uuid_in(md5(random()::text || random()::text)::cstring) WHERE C_Country_id = 339;
UPDATE C_City SET C_City_uu = uuid_in(md5(random()::text || random()::text)::cstring) WHERE C_Country_id = 339;
UPDATE C_Municipality SET C_Municipality_uu = uuid_in(md5(random()::text || random()::text)::cstring) WHERE C_Country_id = 339;
UPDATE C_Parish SET C_Parish_uu = uuid_in(md5(random()::text || random()::text)::cstring) WHERE C_Country_id = 339;


AD_Package_Exp_ID=1000035
AD_Package_Exp_UU=2b1616df-e6d0-4154-bace-adf785643833

SELECT AD_Package_Exp_UU FROM AD_Package_Exp WHERE AD_Package_Exp_ID = 1000035;
'e9aa5f69-6b0a-4963-971e-eaeca32b18be'
SELECT AD_Package_Exp_Detail_UU FROM AD_Package_Exp_Detail WHERE AD_Package_Exp_ID = 1000035;
'00632b03-4413-472b-a7b0-e6fc75f1f340',
'f108877d-1155-45a8-acab-43cff6182f7e',
'17b061b2-3c8d-4c09-8b68-f4e6ecb2e42b',
'918d700e-d102-432d-b77f-d886463bf1f7',
'4229effe-7dac-4a25-bde9-dc51ed3ad7c4',
'17648a08-a7a1-49a0-aaa7-50a9271ba93c',
'009ceca1-aed0-4a45-b04c-7b5e8565faca'
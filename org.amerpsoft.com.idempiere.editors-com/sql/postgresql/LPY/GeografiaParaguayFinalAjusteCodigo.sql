-- REGION _ DEPARTAMENTO  2Caracteres
SELECT c_region_id, SUBSTR(C_region_Id::varchar(7),6,2) AS ID, value FROM c_region  WHERE C_country_id= 276 ORDER BY C_region_ID; 
UPDATE C_region SET VALUE = SUBSTR(C_region_Id::varchar(7),6,2) WHERE C_country_id= 276 ;

-- CIUDAD 4C
SELECT c_city_id, SUBSTR(C_city_Id::varchar(8),5,4) AS ID, value FROM c_city  WHERE C_country_id= 276 ORDER BY C_city_ID; 
UPDATE c_city SET VALUE = SUBSTR(C_city_Id::varchar(8),5,4) WHERE C_country_id= 276 ;

-- C_MUNICIPALITY (DISTRITO) 3 C
SELECT c_municipality_id, SUBSTR(C_municipality_Id::varchar(8),6,3) AS ID, value FROM c_municipality  WHERE C_country_id= 276 ORDER BY C_municipality_ID; 
UPDATE c_municipality SET VALUE = SUBSTR(C_municipality_Id::varchar(8),6,3) WHERE C_country_id= 276; 

-- PARROQUIA BARRIO 4c
SELECT c_parish_id, SUBSTR(C_parish_Id::varchar(8),5,4) AS ID, value FROM c_parish  WHERE C_country_id= 276 ORDER BY C_parish_ID; 
UPDATE c_parish SET VALUE = SUBSTR(C_parish_Id::varchar(8),5,4) WHERE C_country_id= 276 ;

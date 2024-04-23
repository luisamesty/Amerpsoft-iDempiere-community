-- Crea temporal de codigos de tres digitos
 
-- adempiere.c_country3 definition

-- Drop table

-- DROP TABLE adempiere.c_country3;

CREATE TABLE adempiere.c_country3 (
	c_country_id numeric(10) NULL,
	countrycode bpchar(2) NOT NULL,
	countrycode3 bpchar(3) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	modificado numeric(1) NULL
);


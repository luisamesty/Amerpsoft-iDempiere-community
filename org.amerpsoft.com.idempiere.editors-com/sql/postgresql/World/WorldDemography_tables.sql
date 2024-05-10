---------------------------------------
-- DATABASE WorldDemography 
---------------------------------------
CREATE DATABASE "WorldDemography"
    WITH
    OWNER = adempiere
    ENCODING = 'UTF8'
   TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

---------------------------------------
-- MAIN TABLE
---------------------------------------

-- public.mylan_worldcities_alln definition

-- Drop table

-- DROP TABLE public.mylan_worldcities_alln;

CREATE TABLE public.mylan_worldcities_alln (
	city varchar(250) NULL,
	city_ascii varchar(250) NULL,
	lat varchar(50) NULL,
	lng varchar(50) NULL,
	country varchar(250) NULL,
	iso2 varchar(50) NULL,
	iso3 varchar(50) NULL,
	admin_name varchar(250) NULL,
	capital varchar(250) NULL,
	population int4 NULL,
	id int4 NULL
);

-- DROP TABLE adempiere.c_country;
CREATE TABLE c_country (
	c_country_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	countrycode bpchar(2) NOT NULL,
	hasregion bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	regionname varchar(60) NULL,
	expressionphone varchar(20) NULL,
	displaysequence varchar(20) NOT NULL,
	expressionpostal varchar(20) NULL,
	haspostal_add bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	expressionpostal_add varchar(20) NULL,
	ad_language varchar(6) NULL,
	c_currency_id numeric(10) NULL,
	displaysequencelocal varchar(20) NULL,
	isaddresslinesreverse bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	isaddresslineslocalreverse bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	expressionbankroutingno varchar(20) NULL,
	expressionbankaccountno varchar(20) NULL,
	mediasize varchar(40) NULL,
	ispostcodelookup bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	lookupclassname varchar(255) NULL,
	lookupclientid varchar(50) NULL,
	lookuppassword varchar(50) NULL,
	lookupurl varchar(100) NULL,
	allowcitiesoutoflist bpchar(1) DEFAULT 'Y'::bpchar NULL,
	capturesequence varchar(60) NULL,
	c_country_uu varchar(36) DEFAULT NULL::character varying NULL,
	countrycode3 varchar(3) DEFAULT NULL::character varying NULL,
	hascommunity bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	hasmunicipality bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	hasparish bpchar(1) DEFAULT 'N'::bpchar NOT NULL,
	placeholderaddress1 varchar(255) DEFAULT NULL::character varying NULL,
	placeholderaddress2 varchar(255) DEFAULT NULL::character varying NULL,
	placeholderaddress3 varchar(255) DEFAULT NULL::character varying NULL,
	placeholderaddress4 varchar(255) DEFAULT NULL::character varying NULL,
	placeholderaddress5 varchar(255) DEFAULT NULL::character varying NULL,
	placeholdercity varchar(255) DEFAULT NULL::character varying NULL,
	placeholdercomments varchar(255) DEFAULT NULL::character varying NULL,
	placeholderpostal varchar(255) DEFAULT NULL::character varying NULL,
	placeholderpostal_add varchar(255) DEFAULT NULL::character varying NULL,
	CONSTRAINT c_country_allowcitiesoutoflist_check CHECK ((allowcitiesoutoflist = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_hascommunity_check CHECK ((hascommunity = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_hasmunicipality_check CHECK ((hasmunicipality = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_hasparish_check CHECK ((hasparish = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_haspostal_add_check CHECK ((haspostal_add = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_hasregion_check CHECK ((hasregion = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_ispostcodelookup_check CHECK ((ispostcodelookup = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_country_pkey PRIMARY KEY (c_country_id),
	CONSTRAINT c_country_uu_idx UNIQUE (c_country_uu)
);
CREATE UNIQUE INDEX c_countrycode ON c_country USING btree (countrycode);

-- DROP TABLE adempiere.c_country3;

CREATE TABLE c_country3 (
	c_country_id numeric(10) NULL,
	countrycode bpchar(2) NOT NULL,
	countrycode3 bpchar(3) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	modificado numeric(1) NULL
);

-- DROP TABLE c_city;

CREATE TABLE c_city (
	c_city_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	locode varchar(10) NULL,
	coordinates varchar(20) NULL,
	postal varchar(10) NULL,
	areacode varchar(10) NULL,
	c_country_id numeric(10) NOT NULL,
	c_region_id numeric(10) NULL,
	c_city_uu varchar(36) DEFAULT NULL::character varying NULL,
	value varchar(40) DEFAULT NULL::character varying NULL,
	name2 varchar(60) DEFAULT NULL::character varying NULL,
	admin_name varchar(60) DEFAULT NULL::character varying NULL,
	lat varchar(20) NULL,
	lng varchar(20) NULL,
	population numeric NULL,
	CONSTRAINT c_city_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_city_pkey PRIMARY KEY (c_city_id),
	CONSTRAINT c_city_uu_idx UNIQUE (c_city_uu)
);


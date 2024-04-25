---------------------------------------
-- BASE DE DATOS DemografiaParaguay
---------------------------------------

CREATE DATABASE "DemografiaParaguay"
    WITH
    OWNER = adempiere
    ENCODING = 'UTF8'
   TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
ALTER DATABASE "DemografiaParaguay"
    SET search_path TO adempiere;

------------------------------
-- table c_cOUNTRY
------------------------------

-- DROP TABLE adempiere.c_country;
CREATE TABLE adempiere.c_country (
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
CREATE UNIQUE INDEX c_countrycode ON adempiere.c_country USING btree (countrycode);

------------------------------
-- TABLE C_Community
------------------------------
-- DROP TABLE adempiere.c_community;
CREATE TABLE adempiere.c_community (
	c_community_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) NOT NULL,
	created timestamp DEFAULT statement_timestamp() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT statement_timestamp() NOT NULL,
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) DEFAULT NULL::character varying NULL,
	c_country_id numeric(10) NOT NULL,
	isdefault bpchar(1) DEFAULT NULL::bpchar NULL,
	c_community_uu varchar(36) DEFAULT NULL::character varying NULL,
	CONSTRAINT c_community_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_community_isdefault_check CHECK ((isdefault = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_community_key PRIMARY KEY (c_community_id),
	CONSTRAINT c_community_uu_idx UNIQUE (c_community_uu)
);

------------------------------
-- TABLE C_Region
------------------------------
-- DROP TABLE adempiere.c_region;
CREATE TABLE adempiere.c_region (
	c_region_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	c_country_id numeric(10) NOT NULL,
	isdefault bpchar(1) DEFAULT 'N'::bpchar NULL,
	c_region_uu varchar(36) DEFAULT NULL::character varying NULL,
	c_community_id numeric(10) DEFAULT NULL::numeric NULL,
	CONSTRAINT c_region_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_region_isdefault_check CHECK ((isdefault = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_region_pkey PRIMARY KEY (c_region_id),
	CONSTRAINT c_region_uu_idx UNIQUE (c_region_uu)
);
CREATE UNIQUE INDEX c_region_name ON adempiere.c_region USING btree (c_country_id, name);

------------------------------
-- TABLE C_City
------------------------------
-- DROP TABLE adempiere.c_city;
CREATE TABLE adempiere.c_city (
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
	coordinates varchar(15) NULL,
	postal varchar(10) NULL,
	areacode varchar(10) NULL,
	c_country_id numeric(10) NOT NULL,
	c_region_id numeric(10) NULL,
	c_city_uu varchar(36) DEFAULT NULL::character varying NULL,
	CONSTRAINT c_city_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_city_pkey PRIMARY KEY (c_city_id),
	CONSTRAINT c_city_uu_idx UNIQUE (c_city_uu)
);
-- adempiere.c_city foreign keys
ALTER TABLE adempiere.c_city ADD CONSTRAINT ccountry_ccity FOREIGN KEY (c_country_id) REFERENCES adempiere.c_country(c_country_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE adempiere.c_city ADD CONSTRAINT cregion_ccity FOREIGN KEY (c_region_id) REFERENCES adempiere.c_region(c_region_id) DEFERRABLE INITIALLY DEFERRED;



------------------------------
-- Table C_Municipality
------------------------------
-- DROP TABLE adempiere.c_municipality;
CREATE TABLE adempiere.c_municipality (
	c_municipality_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) NOT NULL,
	created timestamp DEFAULT statement_timestamp() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT statement_timestamp() NOT NULL,
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	capital varchar(60) NOT NULL,
	c_country_id numeric(10) NOT NULL,
	c_region_id numeric(10) NOT NULL,
	isdefault bpchar(1) DEFAULT NULL::bpchar NULL,
	c_municipality_uu varchar(36) DEFAULT NULL::character varying NULL,
	CONSTRAINT c_municipality_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_municipality_isdefault_check CHECK ((isdefault = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_municipality_key PRIMARY KEY (c_municipality_id),
	CONSTRAINT c_municipality_uu_idx UNIQUE (c_municipality_uu)
);
-- adempiere.c_municipality foreign keys
ALTER TABLE adempiere.c_municipality ADD CONSTRAINT ccountry_cmunicipality FOREIGN KEY (c_country_id) REFERENCES adempiere.c_country(c_country_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE adempiere.c_municipality ADD CONSTRAINT cregion_cmunicipality FOREIGN KEY (c_region_id) REFERENCES adempiere.c_region(c_region_id) DEFERRABLE INITIALLY DEFERRED;

------------------------------
-- Table C_Parish
------------------------------
-- DROP TABLE adempiere.c_parish;
CREATE TABLE adempiere.c_parish (
	c_parish_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) NOT NULL,
	created timestamp DEFAULT statement_timestamp() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT statement_timestamp() NOT NULL,
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	c_country_id numeric(10) NOT NULL,
	c_region_id numeric(10) NOT NULL,
	c_municipality_id numeric(10) NOT NULL,
	isdefault bpchar(1) DEFAULT NULL::bpchar NULL,
	c_parish_uu varchar(36) DEFAULT NULL::character varying NULL,
	CONSTRAINT c_parish_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_parish_isdefault_check CHECK ((isdefault = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_parish_key PRIMARY KEY (c_parish_id),
	CONSTRAINT c_parish_uu_idx UNIQUE (c_parish_uu)
);
-- adempiere.c_parish foreign keys
ALTER TABLE adempiere.c_parish ADD CONSTRAINT ccountry_cparish FOREIGN KEY (c_country_id) REFERENCES adempiere.c_country(c_country_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE adempiere.c_parish ADD CONSTRAINT cregion_cparish FOREIGN KEY (c_region_id) REFERENCES adempiere.c_region(c_region_id) DEFERRABLE INITIALLY DEFERRED;

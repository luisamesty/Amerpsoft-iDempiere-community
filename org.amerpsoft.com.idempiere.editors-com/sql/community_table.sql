-- adempiere.c_community definition
-- Drop table
-- DROP TABLE c_community;
CREATE TABLE c_community (
	c_community_id numeric(10) NOT NULL,
	ad_client_id numeric(10) NOT NULL,
	ad_org_id numeric(10) NOT NULL,
	isactive bpchar(1) NOT NULL DEFAULT 'Y'::bpchar,
	created timestamp NOT NULL DEFAULT now(),
	createdby numeric(10) NOT NULL,
	updated timestamp NOT NULL DEFAULT now(),
	updatedby numeric(10) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	c_country_id numeric(10) NOT NULL,
	isdefault bpchar(1) NULL DEFAULT 'N'::bpchar,
	c_community_uu varchar(36) NULL DEFAULT NULL::character varying,
	CONSTRAINT c_community_isactive_check CHECK ((isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_community_isdefault_check CHECK ((isdefault = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))),
	CONSTRAINT c_community_pkey PRIMARY KEY (c_community_id),
	CONSTRAINT c_community_uu_idx UNIQUE (c_community_uu)
);
CREATE UNIQUE INDEX c_community_idx ON adempiere.c_community USING btree (c_community_id, c_country_id);
CREATE UNIQUE INDEX c_community_name ON adempiere.c_community USING btree (c_country_id, name);


-- adempiere.c_community foreign keys

ALTER TABLE adempiere.c_community ADD CONSTRAINT c_communityclient FOREIGN KEY (ad_client_id) REFERENCES ad_client(ad_client_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE adempiere.c_community ADD CONSTRAINT c_communityorg FOREIGN KEY (ad_org_id) REFERENCES ad_org(ad_org_id) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE adempiere.c_community ADD CONSTRAINT ccountry_cregion FOREIGN KEY (c_country_id) REFERENCES c_country(c_country_id) DEFERRABLE INITIALLY DEFERRED;
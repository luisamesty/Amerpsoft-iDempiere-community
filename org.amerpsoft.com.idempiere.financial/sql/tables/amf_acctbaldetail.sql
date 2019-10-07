-- Table: adempiere.amf_acctbaldetail

-- DROP TABLE adempiere.amf_acctbaldetail;

CREATE TABLE adempiere.amf_acctbaldetail
(
  accountfrom_id numeric(10,0),
  accountto_id numeric(10,0),
  ad_client_id numeric(10,0) NOT NULL,
  ad_org_id numeric(10,0) NOT NULL,
  amf_acctbaldetail_id numeric(10,0) NOT NULL,
  amf_acctbaldetail_uu character varying(36),
  c_acctschema_id numeric(10,0) NOT NULL,
  c_calendar_id numeric(10,0) NOT NULL,
  c_element_id numeric(10,0) NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT statement_timestamp(),
  createdby numeric(10,0) NOT NULL,
  c_year_id numeric(10,0) NOT NULL,
  dateacctfrom timestamp without time zone,
  dateacctto timestamp without time zone,
  description character varying(255) NOT NULL,
  isactive character(1) NOT NULL DEFAULT 'Y'::bpchar,
  name character varying(60) NOT NULL,
  periodfrom character varying(10),
  periodto character varying(10),
  ref_org_id numeric(10,0) NOT NULL,
  updated timestamp without time zone NOT NULL DEFAULT statement_timestamp(),
  updatedby numeric(10,0) NOT NULL,
  periodfrom_id numeric(10,0),
  periodto_id numeric(10,0),
  CONSTRAINT amf_acctbaldetail_key PRIMARY KEY (amf_acctbaldetail_id ),
  CONSTRAINT adclient_amfacctbaldetail FOREIGN KEY (ad_client_id)
      REFERENCES adempiere.ad_client (ad_client_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT adorg_amfacctbaldetail FOREIGN KEY (ad_org_id)
      REFERENCES adempiere.ad_org (ad_org_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT cacctschema_amfacctbaldetail FOREIGN KEY (c_acctschema_id)
      REFERENCES adempiere.c_acctschema (c_acctschema_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT ccalendar_amfacctbaldetail FOREIGN KEY (c_calendar_id)
      REFERENCES adempiere.c_calendar (c_calendar_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT celement_amfacctbaldetail FOREIGN KEY (c_element_id)
      REFERENCES adempiere.c_element (c_element_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT celementvalueaccount_from FOREIGN KEY (accountfrom_id)
      REFERENCES adempiere.c_elementvalue (c_elementvalue_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT celementvalueaccount_to FOREIGN KEY (accountto_id)
      REFERENCES adempiere.c_elementvalue (c_elementvalue_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT createdby_amfacctbaldetail FOREIGN KEY (createdby)
      REFERENCES adempiere.ad_user (ad_user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT cyear_amfacctbaldetail FOREIGN KEY (c_year_id)
      REFERENCES adempiere.c_year (c_year_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT periodfrom_amfacctbaldetail FOREIGN KEY (periodfrom_id)
      REFERENCES adempiere.c_period (c_period_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT periodto_amfacctbaldetail FOREIGN KEY (periodto_id)
      REFERENCES adempiere.c_period (c_period_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT reforg_amfacctbaldetail FOREIGN KEY (ref_org_id)
      REFERENCES adempiere.ad_org (ad_org_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT updatedby_amfacctbaldetail FOREIGN KEY (updatedby)
      REFERENCES adempiere.ad_user (ad_user_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED,
  CONSTRAINT amf_acctbaldetail_uu_idx UNIQUE (amf_acctbaldetail_uu ),
  CONSTRAINT amf_acctbaldetail_isactive_check CHECK (isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar]))
)
WITH (
  OIDS=FALSE
);
ALTER TABLE adempiere.amf_acctbaldetail
  OWNER TO adempiere;

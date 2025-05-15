-- adempiere.c_location_org_v source

CREATE OR REPLACE VIEW adempiere.c_location_org_v
AS SELECT org.value,
    org.name,
    org.description,
    org.issummary,
    org.ad_client_id,
    org.ad_org_id,
    orginfo.c_location_id,
    org.isactive
   FROM ad_org org
     JOIN ad_orginfo orginfo ON orginfo.ad_org_id = org.ad_org_id
  WHERE orginfo.c_location_id IS NOT NULL;
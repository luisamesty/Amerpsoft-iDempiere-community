-- View: adempiere.amn_role_access_proc_v

-- DROP VIEW adempiere.amn_role_access_proc_v;

CREATE OR REPLACE VIEW adempiere.amn_role_access_proc_v AS 
 SELECT DISTINCT ampr.amn_process_id AS amn_role_access_proc_v_id, amra.ad_role_id, ampr.ad_client_id, ampr.ad_org_id, ampr.created, ampr.createdby, ampr.updated, ampr.updatedby, ampr.isactive, ampr.amn_process_id, ampr.amn_process_value, ampr.value, ampr.name, ampr.description
   FROM adempiere.amn_process ampr
   LEFT JOIN adempiere.amn_role_access amra ON ampr.amn_process_id = amra.amn_process_id;

ALTER TABLE adempiere.amn_role_access_proc_v
  OWNER TO postgres;


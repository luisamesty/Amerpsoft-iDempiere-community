-- View: adempiere.amn_role_access_contract_v

-- DROP VIEW adempiere.amn_role_access_contract_v;

CREATE OR REPLACE VIEW adempiere.amn_role_access_contract_v AS 
 SELECT DISTINCT amco.amn_contract_id AS amn_role_access_contract_v_id, amra.ad_role_id, amco.ad_client_id, amco.ad_org_id, amco.created, amco.createdby, amco.updated, amco.updatedby, amco.isactive, amco.amn_contract_id, amco.value, amco.name, amco.description, amra.amn_process_id
   FROM adempiere.amn_contract amco
   LEFT JOIN adempiere.amn_role_access amra ON amco.amn_contract_id = amra.amn_contract_id;

ALTER TABLE adempiere.amn_role_access_contract_v
  OWNER TO postgres;


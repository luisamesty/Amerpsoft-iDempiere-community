-- View: adempiere.amn_process_contract_v

-- DROP VIEW adempiere.amn_process_contract_v;

CREATE OR REPLACE VIEW adempiere.amn_process_contract_v AS 
 SELECT pro.amn_process_id + 1000::numeric * amc.amn_contract_id AS amn_process_contract_v_id, pro.amn_process_id, amc.amn_contract_id, pro.ad_client_id, amc.ad_org_id, pro.created, pro.createdby, pro.updated, pro.updatedby, pro.isactive, pro.value, pro.name, pro.description, amc.value AS contractvalue, amc.name AS contractname, amc.description AS contractdescription, amc.payrolldays, amc.initdow, amc.acctdow
   FROM adempiere.amn_process pro, adempiere.amn_contract amc
  ORDER BY pro.value, amc.value;

ALTER TABLE adempiere.amn_process_contract_v
  OWNER TO postgres;


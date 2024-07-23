SELECT * FROM
(
 SELECT
-- CLIENT
	act.ad_client_id,
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
    CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
-- ORGANIZATION
    act.ad_org_id,
    coalesce(org.value,'') as org_value,
    coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description,org.name,org.value,'') as org_description, 
 -- PROCESS
    COALESCE(pro.value,'')  as cod_proceso, 
    COALESCE(pro.name, pro.description,'N/A') as proceso,
    CASE WHEN ($P{AMN_Process_ID} = 0 OR $P{AMN_Process_ID}  IS NULL OR pro.amn_process_id = $P{AMN_Process_ID}) THEN 1 ELSE 0 END AS imprimir_proceso,
 -- CONCEPT TYPES
	 act.value as codigo, act.name as concepto, act.formula as formula, act.calcorder, act.defaultvalue, 
	 COALESCE(act.script,'') as script, 
	 COALESCE(act.scriptdefaultvalue,'') as scriptdefaultvalue,
	 act.arc as islr, 
	 act.utilidad as uti, 
	 act.prestacion as pre, 
	 act.sso as sso, 
	 act.ince as ince, 
	 act.salario as salario,
	 act.feriado,
	 act.faov
 FROM adempiere.amn_concept_types as act
 INNER JOIN adempiere.ad_client as cli ON (act.ad_client_id = cli.ad_client_id)
 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
  LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
 INNER JOIN adempiere.ad_org as org ON (act.ad_org_id = org.ad_org_id)
 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
  LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
  LEFT JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id= act.amn_concept_types_id)
  LEFT JOIN adempiere.amn_process as pro ON (pro.amn_process_id= ctp.amn_process_id)
 WHERE act.ad_client_id = $P{AD_Client_ID}
 AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR act.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
 ) as concept_types
 WHERE imprimir_proceso= 1
 ORDER BY proceso, calcorder
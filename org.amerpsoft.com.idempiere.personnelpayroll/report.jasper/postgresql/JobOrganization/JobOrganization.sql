-- AMN_Jobunit 
-- FROM DEfault AD_Tree FROM A GIVEN AD_Client_ID and table AMN_Jobunit
-- FOR NEW ORGANIZATION REPORTS
WITH RECURSIVE Nodos AS (
    SELECT TRN1.AD_Tree_ID,TRN1.Node_ID, 0 as level, TRN1.Parent_ID, 
	ARRAY [TRN1.Node_ID::text]  AS ancestry, 
	TRN1.Node_ID as Star_An
	FROM ad_treenode TRN1 
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
		FROM AD_Client adcli
		LEFT JOIN AD_Tree tree ON tree.AD_Client_ID= adcli.AD_Client_ID
		left join AD_Table tabl on tabl.ad_table_id = tree.ad_table_id 
		WHERE adcli.AD_client_ID=$P{AD_Client_ID} and tabl.tablename = 'AMN_Jobunit'
	) 
	AND TRN1.isActive='Y' AND TRN1.Parent_ID = 0
    UNION ALL
	SELECT TRN1.AD_Tree_ID, TRN1.Node_ID, TRN2.level+1 as level,TRN1.Parent_ID, 
	TRN2.ancestry || ARRAY[TRN1.Node_ID::text] AS ancestry,
	COALESCE(TRN2.Star_An,TRN1.Parent_ID) as Star_An
	FROM ad_treenode TRN1 
	INNER JOIN Nodos TRN2 ON (TRN2.node_id =TRN1.Parent_ID)
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
		FROM AD_Client adcli
		LEFT JOIN AD_Tree tree ON tree.AD_Client_ID= adcli.AD_Client_ID
		left join AD_Table tabl on tabl.ad_table_id = tree.ad_table_id 
		WHERE adcli.AD_client_ID=$P{AD_Client_ID} and tabl.tablename = 'AMN_Jobunit'
	)  AND TRN1.isActive='Y' 
) 
SELECT 
 org_name, org_description, org_taxid, org_logo,
 cod_unidad,
 -- SE DEFINE LA UNIDAD "PADRE" (RECURSIVIDAD)
 CASE WHEN unidad.parent_uni IS NOT NULL 
     THEN (SELECT COALESCE(name, description) FROM adempiere.amn_jobunit WHERE unidad.parent_uni= amn_jobunit_id ) 
      ELSE '(-)' 
 END as unidad,
 unidad.unidad_h,
 unidad.cod_estacion, unidad.estacion, unidad.recurso,
 unidad.num_plazas, unidad.cod_cargo, unidad.cargo, unidad.fuerza, unidad.employee
FROM
 (SELECT 
    -- ORGANIZACIÓN
		CASE WHEN $P{AD_Org_ID}  =0 THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as org_name,
		CASE WHEN $P{AD_Org_ID} =0 THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as org_description, 
		COALESCE(orginfo.taxid,'') as org_taxid,
		CASE WHEN $P{AD_Org_ID}= 0 THEN img1.binarydata ELSE img2.binarydata END as org_logo,
  	-- UNIDAD
     	uni.value as cod_unidad, -- ORDEN JERARQUICO
   		PAR.Parent_ID as parent_uni,
        COALESCE(uni.name, uni.description) as unidad_h,
        CASE WHEN ($P{AMN_Jobunit_ID} IS NULL OR uni.amn_jobunit_id = $P{AMN_Jobunit_ID}) THEN 1 ELSE 0 END AS imprimir_unidad,
    -- ESTACIÓN
        est.value as cod_estacion,
        COALESCE(est.name, est.description) as estacion, est.numberofjobs as num_plazas,
        CASE WHEN ($P{AMN_Jobstation_ID} IS NULL OR est.amn_jobstation_id = $P{AMN_Jobstation_ID}) THEN 1 ELSE 0 END AS imprimir_estacion,
    -- RECURSO
		COALESCE(rec.name, rec.description) as recurso,
    -- CARGO
        crg.value as cod_cargo,
	    COALESCE(crg.name, crg.description) as cargo,
	    CASE WHEN reflistr.name IS NULL THEN 'Fuerza de Trabajo *** SIN DEFINIR ***' ELSE reflistr.name END  as fuerza,
	-- TRABAJADORES
		CONCAT(empl.value,'-',empl.name) as employee
   FROM Nodos PAR
   LEFT JOIN  adempiere.amn_jobunit as uni ON uni.AMN_Jobunit_ID = PAR.NODE_ID
   INNER JOIN adempiere.ad_client as cli ON (uni.ad_client_id = cli.ad_client_id)
   INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
    LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
   INNER JOIN adempiere.ad_org as org ON (uni.ad_org_id = org.ad_org_id)
   INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
    LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
    LEFT JOIN adempiere.amn_jobstation as est ON (uni.amn_jobunit_id= est.amn_jobunit_id) AND (uni.ad_client_id= est.ad_client_id)
    LEFT JOIN adempiere.s_resource as rec ON (est.s_resource_id = rec.s_resource_id)
    LEFT JOIN adempiere.amn_jobtitle as crg ON (est.amn_jobstation_id= crg.amn_jobstation_id)
    LEFT JOIN adempiere.amn_employee as empl ON (empl.amn_jobtitle_id = crg.amn_jobtitle_id)
    LEFT JOIN adempiere.ad_reference as ref ON(ref.name='AMN_Workforce')
    LEFT JOIN adempiere.ad_ref_list as reflis ON (ref.ad_reference_id = reflis.ad_reference_id AND reflis.value =crg.workforce)
    LEFT JOIN adempiere.ad_ref_list_trl as reflistr ON (reflis.ad_ref_list_id = reflistr.ad_ref_list_id AND reflistr.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) )
   WHERE uni.isactive= 'Y' AND est.isactive= 'Y' AND crg.isactive= 'Y' AND empl.isactive='Y' AND empl.status <> 'R'
     AND uni.ad_client_id= $P{AD_Client_ID} AND uni.ad_org_id=  $P{AD_Org_ID} 
  ) as unidad
 WHERE imprimir_unidad= 1 AND imprimir_estacion= 1
ORDER BY cod_unidad, unidad, unidad.unidad_h, unidad.estacion, unidad.cargo
-- AMN_Concept_Types
-- FROM AD_Tree 1000008  FROM A GIVEN AD_Client_ID
-- FOR NEW Payroll REPORTS
WITH RECURSIVE Nodos AS (
    SELECT 
    TRN1.AD_Tree_ID,
    TRN1.Node_ID, 
    0 as level, 
    TRN1.Parent_ID, 
	ARRAY [TRN1.Node_ID::text]  AS ancestry, 
	ARRAY [ACTP.value::text]  AS valueparent,
	TRN1.Node_ID as Star_An,
	ACT.issummary
	FROM ad_treenode TRN1 
	LEFT JOIN AMN_Concept_Types ACT ON ACT.AMN_Concept_Types_ID = TRN1.Node_ID
	LEFT JOIN AMN_Concept_Types ACTP ON ACTP.AMN_Concept_Types_ID = TRN1.Parent_ID
	WHERE TRN1.AD_tree_ID=(
		SELECT DISTINCT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN AMN_Concept amnc ON adcli.AD_Client_ID = amnc.AD_Client_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
			WHERE adcli.AD_client_ID=$P{AD_Client_ID}	) 
	AND TRN1.isActive='Y' AND TRN1.Parent_ID = 0		
	UNION ALL
	SELECT 
	TRN1.AD_Tree_ID, 
	TRN1.Node_ID, 
	TRN2.level+1 as level,
	TRN1.Parent_ID, 
	TRN2.ancestry || ARRAY[TRN1.Node_ID::text] AS ancestry,
	TRN2.valueparent || ARRAY [ACTP.value::text]  AS valueparent,
	COALESCE(TRN2.Star_An,TRN1.Parent_ID) as Star_An,
	ACT.issummary
	FROM ad_treenode TRN1 
	INNER JOIN Nodos TRN2 ON (TRN2.node_id =TRN1.Parent_ID)
	LEFT JOIN AMN_Concept_Types ACT ON ACT.AMN_Concept_Types_ID = TRN1.Node_ID
	LEFT JOIN AMN_Concept_Types ACTP ON ACTP.AMN_Concept_Types_ID = TRN1.Parent_ID
	WHERE TRN1.AD_tree_ID=(
		SELECT DISTINCT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN AMN_Concept amnc ON adcli.AD_Client_ID = amnc.AD_Client_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
			WHERE adcli.AD_client_ID=$P{AD_Client_ID}
	)  AND TRN1.isActive='Y' 		
) 
-- MAIN SELECT
-- AMN_Concept_types for Level reports
SELECT DISTINCT ON (trial.calcorder, trial.ancestry)
	trial.Level,
	trial.Node_ID, 
	trial.value1 as value1,
	ACTN1.name AS name1,
	trial.value2 as value2,
	LPAD('', trial.LEVEL ,' ') || COALESCE(ACTN2.name,ACTN2.description)  AS name2,
	trial.value3 as value3,
	ACTN3.name AS name3,
	trial.Node_ID as amn_concept_type_id, 
	trial.calcorder,
	trial.concept_value,
	trial.concept_name,
	trial.concept_name_indent,
	trial.concept_description,
	-- AD_Client
	trial.AD_Client_ID,
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
    CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
    -- ORGANIZATION
    trial.ad_org_id,
    coalesce(org.value,'') as org_value,
    coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description,org.name,org.value,'') as org_description, 
	-- PROCESS
    ARRAY[pro.value] AS procesos,
    -- CONTRACT
    ARRAY[ctr.value::text] AS contratos,
 	-- CONCEPT TYPES
	trial.concept_description,
	trial.formula, trial.defaultvalue, 
	trial.script, 
	trial.scriptdefaultvalue,
	trial.islr, 
	trial.uti, 
	trial.pre, 
	trial.sso as sso, 
	trial.ince as ince, 
	trial.salario as salario,
	trial.feriado,
	trial.faov
FROM (
	SELECT 
		PAR.Level,
		PAR.issummary,
		PAR.AD_Tree_ID,
		CNT.tree_name,
		PAR.Node_ID, 
		PAR.Parent_ID ,
		PAR.ancestry,
		PAR.valueparent,
		COALESCE(valueparent[2],'') as Value1,
		COALESCE(valueparent[3],'') as Value2,
		COALESCE(valueparent[4],'') as Value3,
		CNT.AD_client_ID,
		CNT.AD_Org_ID,
		CNT.calcorder,
		CNT.concept_value,
		CNT.concept_name,
		LPAD('', LEVEL ,' ') || COALESCE(CNT.concept_name,CNT.concept_description) as concept_name_indent,
		CNT.concept_description,
		CNT.formula, CNT.defaultvalue, 
		CNT.script, 
		CNT.scriptdefaultvalue,
		CNT.islr, 
		CNT.uti, 
		CNT.pre, 
		CNT.sso as sso, 
		CNT.ince as ince, 
		CNT.salario as salario,
		CNT.feriado,
		CNT.faov
	FROM Nodos PAR
	INNER JOIN (
		SELECT 
		adcli.AD_Client_ID, 
		amnc.AD_Org_ID,
		amnct.AMN_Concept_Types_ID, 
		amnct.value as concept_value,
		amnct.name as concept_name,
		amnct.description as concept_description,
		amnct.calcorder,
		tree.AD_Tree_ID, 
		tree.name as tree_name,
	 -- CONCEPT TYPES
		amnct.formula as formula, amnct.defaultvalue, 
		COALESCE(amnct.script,'') as script, 
		COALESCE(amnct.scriptdefaultvalue,'') as scriptdefaultvalue,
		amnct.arc as islr, 
		amnct.utilidad as uti, 
		amnct.prestacion as pre, 
		amnct.sso as sso, 
		amnct.ince as ince, 
		amnct.salario as salario,
		amnct.feriado,
		amnct.faov
		FROM AD_Client adcli
		LEFT JOIN amn_concept amnc ON amnc.ad_client_id = adcli.ad_client_id 
		LEFT JOIN amn_concept_types amnct ON amnct.amn_concept_id = amnc.amn_concept_id  
		LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
		WHERE adcli.AD_client_ID=$P{AD_Client_ID}
		ORDER BY amnct.calcorder
	) as CNT ON CNT.AMN_Concept_types_ID = PAR.Node_ID
	WHERE PAR.issummary='N'
) trial
LEFT JOIN amn_concept_types as ACTN1 ON (ACTN1.Value = trial.Value1 AND ACTN1.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN amn_concept_types as ACTN2 ON (ACTN2.Value = trial.Value2 AND ACTN2.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN amn_concept_types as ACTN3 ON (ACTN3.Value = trial.Value3 AND ACTN3.AD_Client_ID= trial.AD_Client_ID)
INNER JOIN adempiere.ad_client as cli ON (trial.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
INNER JOIN adempiere.ad_org as org ON (trial.ad_org_id = org.ad_org_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
LEFT JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_id= trial.Node_ID)
LEFT JOIN adempiere.amn_process as pro ON (pro.amn_process_id= ctp.amn_process_id)
LEFT JOIN adempiere.amn_concept_types_contract as ctc ON (ctc.amn_concept_types_id= trial.Node_ID)
LEFT JOIN adempiere.amn_contract as ctr ON (ctr.amn_contract_id= ctc.amn_contract_id)
WHERE trial.ad_client_id = $P{AD_Client_ID}
 AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR trial.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
ORDER BY trial.calcorder, trial.ancestry
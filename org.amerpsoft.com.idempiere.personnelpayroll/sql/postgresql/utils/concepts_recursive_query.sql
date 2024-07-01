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
SELECT 
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
	trial.concept_description
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
		CNT.calcorder,
		CNT.concept_value,
		CNT.concept_name,
		LPAD('', LEVEL ,' ') || COALESCE(CNT.concept_name,CNT.concept_description) as concept_name_indent,
		CNT.concept_description
	FROM Nodos PAR
	INNER JOIN (
		SELECT 
		adcli.AD_Client_ID, 
		amnct.AMN_Concept_Types_ID, 
		amnct.value as concept_value,
		amnct.name as concept_name,
		amnct.description as concept_description,
		amnct.calcorder,
		tree.AD_Tree_ID, 
		tree.name as tree_name
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
ORDER BY trial.calcorder, trial.ANCESTRY

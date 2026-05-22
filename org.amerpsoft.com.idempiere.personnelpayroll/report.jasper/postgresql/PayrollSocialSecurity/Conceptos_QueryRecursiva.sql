-- PayrollSocialSecurityMTESS2025
-- QUERY RECURSIVA V2
WITH Conceptos AS (
	WITH RECURSIVE Nodos AS (
	    SELECT 
	    TRN1.AD_Tree_ID,
	    TRN1.Node_ID, 
	    0 as level, 
	    TRN1.Parent_ID, 
		ARRAY [TRN1.Node_ID::text]  AS ancestry, 
		ARRAY [ACT.value::text]  AS valueparent, -- CAMBIO: Usar ACT.value (Nodo actual)
		ARRAY [ACT.calcorder::int]  AS calcorderparent, -- CAMBIO: Usar ACT.calcorder
		TRN1.Node_ID as Star_An,
		ACT.optmode,
		ACT.issummary
		FROM ad_treenode TRN1 
		LEFT JOIN AMN_Concept_Types ACT ON ACT.AMN_Concept_Types_ID = TRN1.Node_ID
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
		TRN2.valueparent || ARRAY [ACT.value::text]  AS valueparent, -- CAMBIO: Usar ACT.value
		TRN2.calcorderparent || ARRAY [ACT.calcorder::int]  AS calcorderparent, -- CAMBIO: Usar ACT.calcorder
		COALESCE(TRN2.Star_An,TRN1.Parent_ID) as Star_An,
		ACT.optmode,
		ACT.issummary
		FROM ad_treenode TRN1 
		INNER JOIN Nodos TRN2 ON (TRN2.node_id =TRN1.Parent_ID)
		LEFT JOIN AMN_Concept_Types ACT ON ACT.AMN_Concept_Types_ID = TRN1.Node_ID
		WHERE TRN1.AD_tree_ID=(
			SELECT DISTINCT tree.AD_Tree_ID
				FROM AD_Client adcli
				LEFT JOIN AMN_Concept amnc ON adcli.AD_Client_ID = amnc.AD_Client_ID
				LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
				WHERE adcli.AD_client_ID=$P{AD_Client_ID}
		)  AND TRN1.isActive='Y' 		
	) 
	-- MAIN SELECT
	SELECT DISTINCT ON (trial.calcorder, trial.ancestry)
		trial.Level,
		trial.Node_ID, 
		trial.value1 as value1,
		ACTN1.name AS name1,
		ACTN1.calcorder AS calcorder1,
		trial.value2 as value2,
		COALESCE(ACTN2.name,ACTN2.description)  AS name2,
		ACTN2.calcorder AS calcorder2,
		trial.value3 as value3,
		ACTN3.name AS name3,
		ACTN3.calcorder AS calcorder3,
		trial.amn_concept_types_id, 
		trial.calcorder,
		trial.optmode, 
		trial.defaultvalue,
		trial.isshow,
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
			-- Al usar el nodo actual en el array, las posiciones se alinean de raíz a fin:
			COALESCE(valueparent[1],'') as Value1, -- Nivel Raíz (Top)
			COALESCE(valueparent[2],'') as Value2, -- Sub-nivel 1
			COALESCE(valueparent[3],'') as Value3, -- Sub-nivel 2
			CNT.AD_client_ID,
			CNT.AD_Org_ID,
			CNT.AMN_Concept_Types_ID,
			CNT.calcorder,
			CNT.optmode, 
			CNT.defaultvalue,
			CNT.isshow,
			CNT.concept_value,
			CNT.concept_name,
			LPAD('', LEVEL ,' ') || COALESCE(CNT.concept_name,CNT.concept_description) as concept_name_indent,
			CNT.concept_description
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
			amnct.optmode, 
			amnct.defaultvalue,
			amnct.isshow,
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
	-- CORRECCIÓN: Join explícito por Value y Client_ID para evitar nulos o cartesianos
	LEFT JOIN amn_concept_types as ACTN3 ON (ACTN3.Value = trial.Value3 AND ACTN3.AD_Client_ID= trial.AD_Client_ID)
	WHERE trial.ad_client_id = $P{AD_Client_ID}
	 AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR trial.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
	ORDER BY trial.calcorder, trial.ancestry
) 
SELECT * FROM Conceptos
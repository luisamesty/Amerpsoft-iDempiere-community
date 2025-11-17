-- amp_concept_tree (CORRECCIÓN V3: Tipo de dato de concept_description)
--
DROP FUNCTION IF EXISTS amp_concept_tree(NUMERIC, NUMERIC);

CREATE OR REPLACE FUNCTION amp_concept_tree(
    p_ad_client_id NUMERIC,
    p_ad_org_id NUMERIC
)
RETURNS TABLE (
	level INTEGER,
	node_id NUMERIC,
    ancestry TEXT[],
    value1 VARCHAR,
    name1 VARCHAR,
    calcorder1 NUMERIC,
    value2 VARCHAR,
    name2 VARCHAR,
    calcorder2 NUMERIC,
    value3 VARCHAR,
    name3 VARCHAR,
    calcorder3 NUMERIC,
    amn_concept_types_id NUMERIC,
    calcorder NUMERIC,
    optmode CHAR(1),
    isshow CHAR(1),
    concept_value VARCHAR,
    concept_name VARCHAR,
    concept_name_indent TEXT,
    concept_description VARCHAR -- <-- Corregido de TEXT a VARCHAR (Columna 20)
) AS $$
BEGIN
RETURN QUERY
WITH RECURSIVE Nodos AS (
    SELECT 
    TRN1.AD_Tree_ID,
    TRN1.Node_ID, 
    0 as level, 
    TRN1.Parent_ID, 
	ARRAY [TRN1.Node_ID::text]  AS ancestry, 
	ARRAY [ACTP.value::text]  AS valueparent,
	ARRAY [ACTP.calcorder::int]  AS calcorderparent,
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
			WHERE adcli.AD_client_ID = p_ad_client_id
	) 
	AND TRN1.isActive='Y' AND (TRN1.Parent_ID IS NULL OR TRN1.Parent_ID = 0)	
	UNION ALL
	SELECT 
	TRN1.AD_Tree_ID, 
	TRN1.Node_ID, 
	TRN2.level+1 as level,
	TRN1.Parent_ID, 
	TRN2.ancestry || ARRAY[TRN1.Node_ID::text] AS ancestry,
	TRN2.valueparent || ARRAY [ACTP.value::text]  AS valueparent,
	TRN2.calcorderparent || ARRAY [ACTP.calcorder::int]  AS calcorderparent,
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
			WHERE adcli.AD_client_ID = p_ad_client_id
	)  AND TRN1.isActive='Y' 		
),
Conceptos_Base AS (
	SELECT 
        trial.Level,
        trial.Node_ID, 
        trial.ancestry,
        trial.value1 as value1,
        ACTN1.name AS name1,
        ACTN1.calcorder AS calcorder1,
        trial.value2 as value2,
        ACTN2.name AS name2,
        ACTN2.calcorder AS calcorder2,
        trial.value3 as value3,
        ACTN3.name AS name3,
        ACTN3.calcorder AS calcorder3,
        trial.amn_concept_types_id, 
        trial.calcorder,
        trial.optmode, 
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
			PAR.valueparent,
			-- Conversión explícita a VARCHAR (Character Varying)
			(COALESCE(valueparent[2],''))::VARCHAR as Value1,
			(COALESCE(valueparent[3],''))::VARCHAR as Value2,
			(COALESCE(valueparent[4],''))::VARCHAR as Value3,
			CNT.AD_client_ID,
			CNT.AD_Org_ID,
			CNT.AMN_Concept_Types_ID,
			CNT.calcorder,
			CNT.optmode, 
			CNT.isshow,
			CNT.concept_value,
			CNT.concept_name,
			LPAD('', PAR.LEVEL * 2, ' ') || COALESCE(CNT.concept_name,CNT.concept_description) as concept_name_indent,
			CNT.concept_description -- El tipo de dato devuelto aquí es VARCHAR(255)
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
			amnct.isshow,
			tree.AD_Tree_ID, 
			tree.name as tree_name
			FROM AD_Client adcli
			LEFT JOIN amn_concept amnc ON amnc.ad_client_id = adcli.ad_client_id 
			LEFT JOIN amn_concept_types amnct ON amnct.amn_concept_id = amnc.amn_concept_id  
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
			WHERE adcli.AD_client_ID = p_ad_client_id
		) as CNT ON CNT.AMN_Concept_types_ID = PAR.Node_ID
		WHERE PAR.issummary='N'
	) trial
	LEFT JOIN amn_concept_types as ACTN1 ON (ACTN1.Value = trial.Value1 AND ACTN1.AD_Client_ID= trial.AD_Client_ID)
	LEFT JOIN amn_concept_types as ACTN2 ON (ACTN2.Value = trial.Value2 AND ACTN2.AD_Client_ID= trial.AD_Client_ID)
	LEFT JOIN amn_concept_types as ACTN3 ON (ACTN3.Value = trial.Value3 AND ACTN3.AD_Client_ID= trial.AD_Client_ID)
	WHERE trial.ad_client_id = p_ad_client_id
	 AND ( p_ad_org_id = 0 OR p_ad_org_id IS NULL OR trial.ad_org_id = p_ad_org_id )
) 
SELECT DISTINCT ON (Conceptos_Base.calcorder, Conceptos_Base.ancestry) * FROM Conceptos_Base
ORDER BY Conceptos_Base.calcorder, Conceptos_Base.ancestry;
END;
$$ LANGUAGE plpgsql;
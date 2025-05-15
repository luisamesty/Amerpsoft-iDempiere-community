-- ACCOUNT ELEMENTS
-- ORACLE VERSION2
-- FROM DEfault AD_Tree FROM A GIVEN AD_Client_ID and C_AcctSchema_ID
-- FOR NEW ACCOUNTING REPORTS
WITH  Nodos (tree_id, node_id, id_path, nlevel, parent_id) AS (
    SELECT TRN1.AD_tree_ID, TRN1.Node_ID, TO_CHAR(0) , 0 AS nlevel, TRN1.Parent_ID
	FROM adempiere.ad_treenode TRN1
	INNER JOIN ADEMPIERE.C_ELEMENTVALUE ce ON ce.c_elementvalue_id = TRN1.node_id
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
			LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
			LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
			WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID} AND adcli.AD_client_ID=$P{AD_Client_ID}
	)
	AND TRN1.Parent_ID = 0
	AND TRN1.isActive='Y'
    UNION ALL
	SELECT TRN2.AD_tree_ID, TRN2.Node_ID, 
	TRN2.Parent_ID ||','|| Nodos.id_path, 
	Nodos.nlevel+1 as nlevel, TRN2.Parent_ID
	FROM Nodos 
    join adempiere.ad_treenode TRN2 on TRN2.parent_id = Nodos.node_id
    INNER JOIN ADEMPIERE.C_ELEMENTVALUE ce2 ON ce2.c_elementvalue_id = TRN2.parent_id
	WHERE TRN2.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
			LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
			LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
			WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID} AND adcli.AD_client_ID=$P{AD_Client_ID}	)
	AND TRN2.isActive='Y' 
) 
SELECT
	PAR.nLevel nlevel,
	PAR.Node_ID, 
	PAR.Parent_ID,
	CLI.value as clivalue,
	CLI.name as cliname,
	coalesce(CLI.description,CLI.name,'') as clidescription,
	IMG.binarydata as cli_logo,
	ELE.c_element_id,
	ELV.c_elementvalue_id,
	ELV.ad_client_id,
	ELV.ad_org_id,
	ELV.isactive,
	ELV.value,
	COALESCE(ELV.name,'')  name,
	LPAD(ELV.name, LENGTH(ELV.value)+LENGTH(ELV.name),' ') nameind,
	LPAD(COALESCE(ELV.description,ELV.name), LENGTH(ELV.value)+LENGTH(COALESCE(ELV.description,ELV.name)),' ') description,
	LENGTH(ELV.value)  length,
	ELV.accounttype,
	ELV.accountsign,
	ELV.isdoccontrolled,
	ELV.c_element_id,
	ELV.issummary,
	COALESCE(ELVP.value,'')  value_parent 
from Nodos PAR
LEFT OUTER JOIN (
	SELECT adcli.AD_Client_ID, accsh.C_AcctSchema_ID, accee.C_Element_ID, accel.name as element_name, tree.AD_Tree_ID, tree.name as tree_name
	FROM AD_Client adcli
	LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
	LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
	LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
	LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
	WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID}
) ELE ON ELE.AD_Tree_ID = PAR.Tree_ID
LEFT JOIN ad_client CLI ON (CLI.ad_client_id = ELE.ad_client_id)
LEFT JOIN ad_clientinfo CLF ON (CLI.ad_client_id = CLF.ad_client_id)
LEFT JOIN ad_image IMG ON (CLF.logoreport_id = IMG.ad_image_id)
LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = PAR.NODE_ID
LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = PAR.Parent_ID
ORDER BY ELV.value
--
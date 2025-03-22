-- ACCOUNT ELEMENTS
-- FROM DEfault AD_Tree FROM A GIVEN AD_Client_ID and C_AcctSchema_ID
-- FOR NEW ACCOUNTING REPORTS
WITH RECURSIVE Nodos AS (
	SELECT 
	ELV.ad_client_id,
	ELV.ad_org_id,
	ELV.c_element_id,
	ELV.c_elementvalue_id,
	ELV.isactive,
	ELV."value",
	COALESCE(ELV.name,'') as name,
	LPAD('', char_length(TRIM(ELV.value)),' ') || COALESCE(ELV.description,ELV.name) as description,
	char_length(TRIM(ELV.value)) as length,
	ELV.accounttype,
	ELV.accountsign,
	ELV.isdoccontrolled,
	ELV.issummary,
	COALESCE(ELVP.value,'') as value_parent ,
	TRN1.AD_Tree_ID,
	TRN1.Node_ID, 
	0 as level, 
	TRN1.Parent_ID, 
	ARRAY [TRN1.Node_ID::text]  AS ancestry, 
	ARRAY [ELVP.value::text]  AS acctparent,
	TRN1.Node_ID as Star_An
	FROM ad_treenode TRN1 
	LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = TRN1.NODE_ID
	LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = TRN1.Parent_ID
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
			LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
			LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
			WHERE accee.ElementType='AC' 
			AND accsh.C_AcctSchema_ID = 1000000 --$P{C_AcctSchema_ID} 
			AND adcli.AD_client_ID=1000000 --$P{AD_Client_ID}
	) 
	AND TRN1.isActive='Y' AND TRN1.Parent_ID = 0
    UNION ALL
	SELECT 
	ELV.ad_client_id,
	ELV.ad_org_id,
	ELV.c_element_id,
	ELV.c_elementvalue_id,
	ELV.isactive,
	ELV."value",
	COALESCE(ELV.name,'') as name,
	LPAD('', char_length(ELV.value),' ') || COALESCE(ELV.description,ELV.name) as description,
	char_length(TRIM(ELV.value)) as length,
	ELV.accounttype,
	ELV.accountsign,
	ELV.isdoccontrolled,
	ELV.issummary,
	COALESCE(ELVP.value,'') as value_parent ,
	TRN1.AD_Tree_ID, 
	TRN1.Node_ID, 
	TRN2.level+1 as level,
	TRN1.Parent_ID, 
	TRN2.ancestry || ARRAY[TRN1.Node_ID::text] AS ancestry,
	TRN2.acctparent || ARRAY [ELVP.value::text]  AS acctparent,
	COALESCE(TRN2.Star_An,TRN1.Parent_ID) as Star_An
	FROM ad_treenode TRN1 
	INNER JOIN Nodos TRN2 ON (TRN2.node_id =TRN1.Parent_ID)
	LEFT JOIN C_elementValue ELV ON ELV.C_ElementValue_ID = TRN1.NODE_ID
	LEFT JOIN C_elementValue ELVP ON ELVP.C_ElementValue_ID = TRN1.Parent_ID
	WHERE TRN1.AD_tree_ID=(
		SELECT tree.AD_Tree_ID
			FROM AD_Client adcli
			LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
			LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
			LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
			WHERE accee.ElementType='AC' 
			AND accsh.C_AcctSchema_ID = 1000000 --$P{C_AcctSchema_ID} 
			AND adcli.AD_client_ID=1000000 --$P{AD_Client_ID}
	)  AND TRN1.isActive='Y' 
) 
-- VAriables FOR REPORT
SELECT 
	trial.clivalue,
	trial.cliname,
	trial.clidescription,
	trial.cli_logo,
	trial.moneda,
	trial.Level,
	trial.Node_ID, 
	trial.value as codigo,
	trial.name,
	trial.description,
	trial.isactive,
	trial.length,
	trial.accounttype,
	trial.accountsign,
	trial.isdoccontrolled,
	trial.issummary,
	trial.isactive,
	trial.openbalance,
	trial.amtacctdr,
	trial.amtacctcr,
	trial.amtacctsa,
	trial.closebalance,
	trial.dateini,
	trial.dateend,
	COALESCE(ELVN1.value,'Z') as codigo1,
	COALESCE(ELVN1.name,CONCAT('Tree error value=',trial.value)) as name1,
	ELVN1.description as description1,
	ELVN1.issummary as issummary1,
	CASE WHEN ELVN1.value <> '' THEN ELVN2.value ELSE 'ZZ' END as codigo2,
	ELVN2.name as name2,
	ELVN2.description as description2,
	ELVN2.issummary as issummary2,
	CASE WHEN ELVN1.value <> '' THEN ELVN3.value ELSE 'ZZZ' END as codigo3,
	ELVN3.name as name3,
	ELVN3.description as description3,
	ELVN3.issummary as issummary3,
	CASE WHEN ELVN1.value <> '' THEN ELVN4.value ELSE 'ZZZZ' END as codigo4,
	ELVN4.name as name4,
	ELVN4.description as description4,
	ELVN4.issummary as issummary4,
	CASE WHEN ELVN1.value <> '' THEN ELVN5.value ELSE 'ZZZZZ' END as codigo5,
	ELVN5.name as name5,
	ELVN5.description as description5,
	ELVN5.issummary as issummary5,
	CASE WHEN ELVN1.value <> '' THEN ELVN6.value ELSE 'ZZZZZZ' END as codigo6,
	ELVN6.name as name6,
	ELVN6.description as description6,
	ELVN6.issummary as issummary6

FROM (
	SELECT 
	CLI.value as clivalue,
	CLI.name as cliname,
	coalesce(CLI.description,CLI.name,'') as clidescription,
	IMG.binarydata as cli_logo,
	CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	PAR.Level,
	PAR.Node_ID, 
	PAR.Parent_ID ,
	PAR.c_element_id,
	PAR.c_elementvalue_id,
	PAR.ad_client_id,
	PAR.ad_org_id,
	PAR.isactive,
	PAR."value",
	COALESCE(PAR.name,'') as name,
	LPAD('', char_length(PAR.value),' ') || COALESCE(PAR.description,PAR.name) as description,
	char_length(PAR.value) as length,
	PAR.accounttype,
	PAR.accountsign,
	PAR.isdoccontrolled,
	PAR.issummary,
	COALESCE(PAR.value_parent,'') as value_parent ,
	COALESCE(acctparent[2],'') as Value1,
	COALESCE(acctparent[3],'') as Value2,
	COALESCE(acctparent[4],'') as Value3,
	COALESCE(acctparent[5],'') as Value4,
	COALESCE(acctparent[6],'') as Value5,	
	COALESCE(acctparent[7],'') as Value6,	
	bal.openbalance,
	bal.amtacctdr,
	bal.amtacctcr,
	bal.amtacctsa,
	bal.closebalance,
	to_char(per.startdate,'DD-MM-YYYY') AS dateini,
	to_char(per.enddate,'DD-MM-YYYY') AS dateend

FROM (
	SELECT 
	ad_client_id,
	c_elementvalue_id, 
	value, 
	name, C_AcctSchema_ID, 
	COALESCE(openbalance,0) as openbalance,
	COALESCE(amtacctdr,0) as amtacctdr,
	COALESCE(amtacctcr,0) as amtacctcr,
	COALESCE(amtacctdr - amtacctcr,0) as amtacctsa,
	COALESCE(closebalance,0) as closebalance
	FROM (
		SELECT
		ele.ad_client_id,
		c_elementvalue_id, ele.value, ele.name, C_AcctSchema_ID, 
		SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct < per.StartDAte) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as openbalance,
				SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctdr,0) ELSE 0 END) as amtacctdr,
				SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctcr,0) ELSE 0 END) as amtacctcr,
				SUM(CASE WHEN ( fas.postingtype = 'A' AND fas.DateAcct <= per.EndDate ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance
		FROM c_elementvalue ele
		LEFT JOIN (
			SELECT * FROM fact_acct 
			WHERE c_acctschema_id = 1000000 --$P{C_AcctSchema_ID}  
			AND ad_client_id=1000000 --$P{AD_Client_ID}
			-- $P{AD_Org_ID}
			AND CASE WHEN 1000000=0 OR 1000000 = ad_org_id THEN 1=1 ELSE 1=0 END
		) fas ON (ele.c_elementvalue_id=fas.account_id )
		LEFT JOIN c_period as per ON (per.c_period_id = 1000072) --$P{C_Period_ID})
		WHERE ele.issummary='N' AND ele.AccountType IN ('R','E','M')
		AND ele.ad_client_id=1000000 --$P{AD_Client_ID}
		GROUP BY ele.ad_client_id, ele.c_elementvalue_id, fas.C_AcctSchema_ID
	) as bal2 
	WHERE CASE WHEN  'N' = 'Y' OR ( 'N' = 'N' AND (  bal2.openbalance<> 0 or bal2.amtacctcr<> 0 or bal2.amtacctdr <>0 or bal2.closebalance <> 0  ) ) THEN 1=1  ELSE 1=0 END
) as bal 
LEFT JOIN Nodos PAR ON ( bal.c_elementvalue_id = PAR.c_elementvalue_id )
LEFT JOIN (
	SELECT 
	adcli.AD_Client_ID, 
	accsh.C_AcctSchema_ID, 
	accee.C_Element_ID, 
	accel.name as element_name, 
	tree.AD_Tree_ID, 
	tree.name as tree_name
	FROM AD_Client adcli
	LEFT JOIN C_AcctSchema accsh ON adcli.AD_Client_ID = accsh.AD_Client_ID
	LEFT JOIN C_AcctSchema_Element accee ON accee.C_AcctSchema_ID = accsh.C_AcctSchema_ID 
	LEFT JOIN C_Element accel ON accel.C_Element_ID = accee.C_Element_ID
	LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= accel.AD_Tree_ID
	WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = 1000000 --$P{C_AcctSchema_ID}
) as ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID
LEFT JOIN ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id)
LEFT JOIN ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
LEFT JOIN ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = 1000000) --$P{C_AcctSchema_ID})
LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=1000000 ) --$P{AD_Client_ID})
	
LEFT JOIN c_period as per ON (per.c_period_id = 1000072) --$P{C_Period_ID})
WHERE PAR.issummary='N'
ORDER BY PAR.ANCESTRY
) trial
LEFT JOIN c_elementvalue as ELVN1 ON (ELVN1.Value = trial.Value1 AND ELVN1.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN2 ON (ELVN2.Value = trial.Value2 AND ELVN2.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN3 ON (ELVN3.Value = trial.Value3 AND ELVN3.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN4 ON (ELVN4.Value = trial.Value4 AND ELVN4.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN5 ON (ELVN5.Value = trial.Value5 AND ELVN5.AD_Client_ID= trial.AD_Client_ID)
LEFT JOIN c_elementvalue as ELVN6 ON (ELVN6.Value = trial.Value6 AND ELVN6.AD_Client_ID= trial.AD_Client_ID)
--ORDER BY codigo
ORDER BY codigo1 ASC, codigo2 ASC, codigo3 ASC, codigo4 ASC, codigo5 ASC, codigo6 ASC 
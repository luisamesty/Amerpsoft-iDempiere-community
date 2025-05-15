-- CUENTAS TODAS
WITH CUENTAS_TODAS AS (
	 SELECT
	    -- CURRENCY
		CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	 	img.binarydata as cli_logo,
	 	CLI.value as clivalue,
		CLI.name as cliname,
		COALESCE(CLI.description,CLI.name) as clidescription,
		CASE WHEN $P{AD_Org_ID}=0 THEN 'Consolidado' ELSE ORG.value END as orgvalue,
		CASE WHEN $P{AD_Org_ID}=0 THEN 'Consolidado' ELSE ORG.name END as orgname,
		CASE WHEN $P{AD_Org_ID}=0 THEN 'Consolidado' ELSE COALESCE(ORG.description,ORG.name) END as orgdescription,
		ELE.c_element_id,
		ELE.ad_tree_id,
		ELVN1.value as valuen1,
		ELVN1.name	as name1,
		ELVN1.issummary as issummary1,
		ELVN1.accounttype as accounttype1,
		ELVN1.accountsign as accountsign1,
		ELVN1.isdoccontrolled as isdoccontrolled1,
		char_length(ELVN1.value) as lenn1,
		ELVN2.value as valuen2,
		ELVN2.name	as name2,
		ELVN2.issummary as issummary2,
		ELVN2.accounttype as accounttype2,
		ELVN2.accountsign as accountsign2,
		ELVN2.isdoccontrolled as isdoccontrolled2,
		char_length(ELVN2.value) as lenn2,
		ELVN3.value as valuen3,
		ELVN3.name	as name3,
		ELVN3.issummary as issummary3,
		ELVN3.accounttype as accounttype3,
		ELVN3.accountsign as accountsign3,
		ELVN3.isdoccontrolled as isdoccontrolled3,
		char_length(ELVN3.value) as lenn3,
		ELVN4.value as valuen4,
		ELVN4.name	as name4,
		ELVN4.c_elementvalue_id,
		ELVN4.issummary as issummary4,
		ELVN4.accounttype as accounttype4,
		ELVN4.accountsign as accountsign4,
		ELVN4.isdoccontrolled as isdoccontrolled4,
		char_length(ELVN4.value) as lenn4,
		ELVN4.c_elementparent_id,
		bal.openbalance,
		bal.amtacctdr,
		bal.amtacctcr,
		bal.amtacctsa,
		bal.closebalance,
		per.name as pername,
		per.startdate as startdate,
		per.enddate as enddate
	FROM
		adempiere.c_element as ELE
		LEFT JOIN
		adempiere.c_elementvalue as ELVN4 ON (ELE.c_element_id =ELVN4.c_element_id)
		LEFT JOIN
		adempiere.c_elementvalue as ELVN3 ON (ELVN4.c_elementparent_id =ELVN3.c_elementvalue_id)
		LEFT JOIN
		adempiere.c_elementvalue as ELVN2 ON (ELVN3.c_elementparent_id =ELVN2.c_elementvalue_id)
		LEFT JOIN
		adempiere.c_elementvalue as ELVN1 ON (ELVN2.c_elementparent_id =ELVN1.c_elementvalue_id)
		LEFT JOIN (
			SELECT
			ele.ad_client_id,
			c_elementvalue_id, ele.value, ele.name, C_AcctSchema_ID, 
			SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct < per.StartDAte) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as openbalance,
					SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctdr,0) ELSE 0 END) as amtacctdr,
					SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctcr,0) ELSE 0 END) as amtacctcr,
					SUM(COALESCE(fas.amtacctdr,0))- SUM(COALESCE(fas.amtacctcr,0))  as amtacctsa,
					SUM(CASE WHEN ( fas.postingtype = 'A' AND fas.DateAcct <= per.EndDate ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance
			FROM c_elementvalue ele
			LEFT JOIN (
				SELECT * FROM fact_acct 
				WHERE c_acctschema_id = $P{C_AcctSchema_ID}  AND ad_client_id=$P{AD_Client_ID}
					AND CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} = ad_org_id THEN 1=1 ELSE 1=0 END
			) fas ON (ele.c_elementvalue_id=fas.account_id )
			LEFT JOIN c_period as per ON (per.c_period_id = $P{C_Period_ID})
			WHERE ele.issummary='N' AND ele.ad_client_id=$P{AD_Client_ID}
	
			GROUP BY ele.ad_client_id, ele.c_elementvalue_id, fas.C_AcctSchema_ID
		) as bal ON ( bal.c_elementvalue_id = ELVN4.c_elementvalue_id )	
		LEFT JOIN adempiere.c_period as per ON (per.c_period_id = $P{C_Period_ID})
		LEFT JOIN adempiere.ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id)
		LEFT JOIN adempiere.ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
		LEFT JOIN adempiere.ad_org AS ORG ON (ORG.ad_client_id = ELE.ad_client_id AND ORG.ad_org_id = $P{AD_Org_ID})
--		LEFT JOIN adempiere.ad_orginfo AS ORF ON (CLI.ad_client_id = ORF.ad_client_id )
		LEFT JOIN adempiere.ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
		LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = $P{C_AcctSchema_ID})
		LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
		LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE
		ELVN4.issummary ='N' AND ELVN4.AccountType IN ('A','L','O') AND ELE.ad_client_id=$P{AD_Client_ID}

	UNION
	
	SELECT
	    -- CURRENCY
		CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	 	img.binarydata as cli_logo,
	 	CLI.value as clivalue,
		CLI.name as cliname,
		COALESCE(CLI.description,CLI.name) as clidescription,
		CASE WHEN $P{AD_Org_ID}=0 THEN 'Consolidado' ELSE ORG.value END as orgvalue,
		CASE WHEN $P{AD_Org_ID}=0 THEN 'Consolidado' ELSE ORG.name END as orgname,
		CASE WHEN $P{AD_Org_ID}=0 THEN 'Consolidado' ELSE COALESCE(ORG.description,ORG.name) END as orgdescription,
		RELE.c_element_id,
		RELE.ad_tree_id,
		RELVN1.value as valuen1,
		RELVN1.name	as name1,
		RELVN1.issummary as issummary1,
		RELVN1.accounttype as accounttype1,
		RELVN1.accountsign as accountsign1,
		RELVN1.isdoccontrolled as isdoccontrolled1,
		char_length(RELVN1.value) as lenn1,
		RELVN2.value as valuen2,
		RELVN2.name	as name2,
		RELVN2.issummary as issummary2,
		RELVN2.accounttype as accounttype2,
		RELVN2.accountsign as accountsign2,
		RELVN2.isdoccontrolled as isdoccontrolled2,
		char_length(RELVN2.value) as lenn2,
		RELVN3.value as valuen3,
		RELVN3.name	as name3,
		RELVN3.issummary as issummary3,
		RELVN3.accounttype as accounttype3,
		RELVN3.accountsign as accountsign3,
		RELVN3.isdoccontrolled as isdoccontrolled3,
		char_length(RELVN3.value) as lenn3,
		CONCAT(RELVN4.value,'-R') as valuen4,
		CONCAT('(*)',RELVN4.name)	as name4,
		RELVN4.c_elementvalue_id,
		RELVN4.issummary as issummary4,
		RELVN4.accounttype as accounttype4,
		RELVN4.accountsign as accountsign4,
		RELVN4.isdoccontrolled as isdoccontrolled4,
		char_length(RELVN4.value) as lenn4,
		RELVN4.c_elementparent_id,
		bal.openbalance,
		bal.amtacctdr,
		bal.amtacctcr,
		bal.amtacctsa,
		bal.closebalance,
		per.name as pername,
		per.startdate as startdate,
		per.enddate as enddate
	FROM
		adempiere.c_element as RELE
		LEFT JOIN
		adempiere.c_elementvalue as RELVN4 ON (RELE.c_element_id =RELVN4.c_element_id)
		LEFT JOIN
		adempiere.c_elementvalue as RELVN3 ON (RELVN4.c_elementparent_id =RELVN3.c_elementvalue_id)
		LEFT JOIN
		adempiere.c_elementvalue as RELVN2 ON (RELVN3.c_elementparent_id =RELVN2.c_elementvalue_id)
		LEFT JOIN
		adempiere.c_elementvalue as RELVN1 ON (RELVN2.c_elementparent_id =RELVN1.c_elementvalue_id)
		LEFT JOIN adempiere.c_period as per ON (per.c_period_id = $P{C_Period_ID})
		LEFT JOIN adempiere.ad_client AS CLI ON (CLI.ad_client_id = RELE.ad_client_id)
		LEFT JOIN adempiere.ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
		LEFT JOIN adempiere.ad_org AS ORG ON (ORG.ad_client_id = RELE.ad_client_id AND ORG.ad_org_id = $P{AD_Org_ID})
		LEFT JOIN adempiere.ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
		LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = $P{C_AcctSchema_ID})
		LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
		LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		LEFT JOIN (
			SELECT
			ele.ad_client_id,
			eleres.c_elementvalue_id, C_AcctSchema_ID, 
			SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct < per.StartDAte) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as openbalance,
					SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctdr,0) ELSE 0 END) as amtacctdr,
					SUM(CASE WHEN (fas.postingtype = 'A' AND fas.DateAcct >= per.StartDAte AND fas.DateAcct <= per.EndDate) THEN COALESCE(fas.amtacctcr,0) ELSE 0 END) as amtacctcr,
					SUM(COALESCE(fas.amtacctdr,0))- SUM(COALESCE(fas.amtacctcr,0))  as amtacctsa,
					SUM(CASE WHEN ( fas.postingtype = 'A' AND fas.DateAcct <= per.EndDate ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance
			FROM c_elementvalue ele
			LEFT JOIN (
				SELECT * FROM fact_acct 
				WHERE c_acctschema_id = $P{C_AcctSchema_ID}  AND ad_client_id=$P{AD_Client_ID}
					AND CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} = ad_org_id THEN 1=1 ELSE 1=0 END
			) fas ON (ele.c_elementvalue_id=fas.account_id )
			LEFT JOIN c_period as per ON (per.c_period_id = $P{C_Period_ID})
			LEFT JOIN c_elementvalue as eleres ON eleres.c_elementvalue_id = $P{C_ElementValue_ID}													 
			WHERE ele.issummary='N' AND ele.ad_client_id=$P{AD_Client_ID} AND c_acctschema_id = $P{C_AcctSchema_ID}
				AND ele.AccountType IN ('R','E','M')
			GROUP BY ele.ad_client_id, eleres.c_elementvalue_id, fas.C_AcctSchema_ID
		) as bal ON ( bal.c_elementvalue_id = RELVN4.c_elementvalue_id )	

	WHERE RELVN4.C_ElementValue_ID=$P{C_ElementValue_ID} 
	)
-- QUERY GLOBAL
SELECT
	cli_logo, moneda,
	accounttype1,accountsign1,isdoccontrolled1,
	issummary1,pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	lenn1 as lencodigo,
	valuen1 as orden,
	valuen1 as codigo,
	name1 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen1,name,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate,issummary1,accounttype1,accountsign1,isdoccontrolled1,lenn1, cli_logo, moneda
UNION
SELECT
	cli_logo, moneda,
	'Z',' ','N',
	'Y',pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	1 as lencodigo,
	valuen1 || 'ZZZZZ' as orden,
	'' as codigo,
	lpad('Total ',6+6,' ')  || rpad(valuen1,6,' ') || ' ' || name1 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen1,name,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate, cli_logo, moneda
UNION
SELECT
	cli_logo, moneda,
	accounttype2,accountsign2,isdoccontrolled2,
	issummary2,pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	lenn2 as lencodigo,
	valuen2 as orden,
	valuen2 as codigo,
	repeat('  ',2) || name2 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen2,name2,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate,issummary2,accounttype2,accountsign2,isdoccontrolled2,lenn2, cli_logo, moneda
UNION
SELECT
	cli_logo, moneda,
	'Z',' ','N',
	'Y',pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	2 as lencodigo,
	valuen2 || 'ZZZZ' as orden,
	'' as codigo,
	lpad('Total ',6+4,' ') || rpad(valuen2,6,' ') || ' ' || name2 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen2,name,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate, cli_logo, moneda
UNION
SELECT
	cli_logo, moneda,
	accounttype3,accountsign3,isdoccontrolled3,
	issummary3,pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	lenn3 as lencodigo,
	valuen3 as orden,
	valuen3 as codigo,
	repeat('  ',3) || name3 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen3,name3,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate,issummary3,accounttype3,accountsign3,isdoccontrolled3,lenn3, cli_logo, moneda
UNION
SELECT
	cli_logo, moneda,
	'Z',' ','N',
	'Y',pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	3 as lencodigo,
	valuen3 || 'ZZZ' as orden,
	'' as codigo,
	lpad('Total ',6+2,' ') || rpad(valuen3,6,' ') || ' ' || name3 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen3,name,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate, cli_logo, moneda
UNION
SELECT
	cli_logo, moneda,
	accounttype4,accountsign4,isdoccontrolled4,
	issummary4,pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	lenn4 as lencodigo,
	valuen4 as orden,
	valuen4 as codigo,
	repeat('  ',4) || name4 as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
WHERE CASE WHEN  $P{isShowZERO} = 'Y'  OR ( $P{isShowZERO} = 'N' 	AND (  openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  ) ) 	THEN 1=1  END 
	AND AccountType1 IN ('A','L','O') 
group by valuen4,name4,clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate,issummary4,accounttype4,accountsign4,isdoccontrolled4,lenn4, cli_logo, moneda

UNION

SELECT
	cli_logo, moneda,
	'Z',' ','N',
	'Y',pername,startdate,enddate,
	to_char(startdate,'DD-MM-YYYY') AS dateini,
	to_char(enddate,'DD-MM-YYYY') AS dateend,
	0 as lencodigo,
	'ZZZZZZ' as orden,
	'' as codigo,
	'      TOTALES --->' as name,
	sum(openbalance) as openbalance,sum(amtacctdr) as amtacctdr,sum(amtacctcr) as amtacctcr,sum(amtacctsa) as amtacctsa,sum(closebalance) as closebalance,
	clivalue,orgname,clidescription,orgdescription
from CUENTAS_TODAS
group by clivalue,orgname,clidescription,orgdescription,pername,startdate,enddate, cli_logo, moneda


ORDER BY orden


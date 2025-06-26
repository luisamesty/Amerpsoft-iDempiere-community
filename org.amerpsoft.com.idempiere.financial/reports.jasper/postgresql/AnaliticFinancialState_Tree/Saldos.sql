--
-- BALANCES
--
	SELECT 
	-- NEW VARIABLES
	'0' as balmov,  -- 0 Balance 1 Movements
	to_char(CAST($P{DateFrom} as TimeStamp),'YYYY-MM-DD') AS dateacct,
	--
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
	to_char(CAST($P{DateFrom} as TimeStamp),'DD-MM-YYYY') AS dateini,
	to_char(CAST($P{DateTo} as TimeStamp),'DD-MM-YYYY') AS dateend,
	COALESCE(prod.value,'') as prod_value,
	COALESCE(prod.name,'') as prod_name,
	COALESCE(cbpa.value,'') as bpartner_value,
	COALESCE(cbpa.name,'') as bpartner_name,	
	COALESCE(proj.value,'') as proj_value,
	COALESCE(proj.name,'') as proj_name,
	COALESCE(acti.value,'') as activity_value,
	COALESCE(acti.name,'') as activity_name,
	COALESCE(sare.value,'') as salesregion_value,
	COALESCE(sare.name,'') as salesregion_name,
	COALESCE(camp.value,'') as campaign_value,
	COALESCE(camp.name,'') as campaign_name,
	posttype.value as postingtype_value,
	posttype.name as postingtype_name
	
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
			SUM(CASE WHEN (fas.DateAcct < CAST($P{DateFrom} as TimeStamp)) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as openbalance,
					SUM(CASE WHEN (fas.DateAcct >= CAST($P{DateFrom} as TimeStamp) AND fas.DateAcct <= CAST($P{DateTo} as TimeStamp)) THEN COALESCE(fas.amtacctdr,0) ELSE 0 END) as amtacctdr,
					SUM(CASE WHEN (fas.DateAcct >= CAST($P{DateFrom} as TimeStamp) AND fas.DateAcct <= CAST($P{DateTo} as TimeStamp)) THEN COALESCE(fas.amtacctcr,0) ELSE 0 END) as amtacctcr,
					SUM(CASE WHEN (fas.DateAcct <= CAST($P{DateTo} as TimeStamp) ) THEN (fas.amtacctdr - fas.amtacctcr) ELSE 0 END) as closebalance
			FROM c_elementvalue ele
			LEFT JOIN (
				SELECT * FROM fact_acct 
				WHERE c_acctschema_id = $P{C_AcctSchema_ID}  
				AND ad_client_id=$P{AD_Client_ID}
				AND CASE WHEN $P{AD_Org_ID}=0 OR $P{AD_Org_ID} = ad_org_id THEN 1=1 ELSE 1=0 END
			) fas ON (ele.c_elementvalue_id=fas.account_id )
			LEFT JOIN c_period per01	ON (per01.c_period_id IN (select distinct c_period_id from c_period where to_char(per01.startdate,'YYYYMM') = to_char(CAST($P{DateFrom} as TimeStamp),'YYYYMM') AND ad_client_id=$P{AD_Client_ID} ))		
			LEFT JOIN c_period per02	ON (per02.c_period_id IN (select distinct c_period_id from c_period where to_char(per02.startdate,'YYYYMM') = to_char(CAST($P{DateTo} as TimeStamp),'YYYYMM') AND ad_client_id=$P{AD_Client_ID} ))		
			WHERE ele.issummary='N'
			 AND ( CASE WHEN  fas.postingtype = $P{PostingType} THEN 1=1 ELSE 1=0 END ) 
			 AND ( CASE WHEN ( $P{M_Product_ID} IS NULL OR fas.M_Product_ID = $P{M_Product_ID}) THEN 1=1 ELSE 1=0 END ) 
			 AND ( CASE WHEN ( $P{C_BPartner_ID} IS NULL OR fas.C_BPartner_ID = $P{C_BPartner_ID}) THEN 1=1 ELSE 1=0 END ) 
			 AND ( CASE WHEN ( $P{C_Project_ID} IS NULL OR fas.C_Project_ID = $P{C_Project_ID}) THEN 1=1 ELSE 1=0 END ) 
			 AND ( CASE WHEN ( $P{C_Activity_ID} IS NULL OR fas.C_Activity_ID = $P{C_Activity_ID}) THEN 1=1 ELSE 1=0 END ) 
			 AND ( CASE WHEN ( $P{C_SalesRegion_ID} IS NULL OR fas.C_SalesRegion_ID = $P{C_SalesRegion_ID}) THEN 1=1 ELSE 1=0 END ) 
			 AND ( CASE WHEN ( $P{C_Campaign_ID} IS NULL OR fas.C_Campaign_ID = $P{C_Campaign_ID}) THEN 1=1 ELSE 1=0 END ) 
			AND ele.ad_client_id=$P{AD_Client_ID}
			GROUP BY ele.ad_client_id, ele.c_elementvalue_id, fas.C_AcctSchema_ID
		) as bal2 
		WHERE CASE WHEN  $P{isShowZERO} = 'Y' OR ( $P{isShowZERO} = 'N' AND (  bal2.openbalance<> 0 or bal2.amtacctcr<> 0 or bal2.amtacctdr <>0 or bal2.closebalance <> 0  ) ) THEN 1=1  ELSE 1=0 END
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
		WHERE accee.ElementType='AC' AND accsh.C_AcctSchema_ID = $P{C_AcctSchema_ID}
	) as ELE ON ELE.AD_Tree_ID = PAR.AD_Tree_ID
	LEFT JOIN ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id)
	LEFT JOIN ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
	LEFT JOIN ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
	LEFT JOIN c_acctschema sch ON (sch.c_acctschema_id = $P{C_AcctSchema_ID})
	LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
	LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	LEFT JOIN M_product prod ON prod.M_Product_ID = $P{M_Product_ID}
	LEFT JOIN C_BPartner cbpa ON cbpa.C_BPartner_ID = $P{C_BPartner_ID}
	LEFT JOIN C_Project proj ON proj.C_Project_ID = $P{C_Project_ID}
	LEFT JOIN C_Activity acti ON acti.C_Activity_ID = $P{C_Activity_ID}
	LEFT JOIN C_SalesRegion sare ON sare.C_SalesRegion_ID = $P{C_SalesRegion_ID}
	LEFT JOIN C_Campaign camp ON camp.C_Campaign_ID = $P{C_Campaign_ID}
	LEFT JOIN (
		SELECT DISTINCT ON (rfl.value) rfl.value, COALESCE(rflt.name,rfl.name,'') as name FROM AD_Reference as rfr
		LEFT JOIN AD_Ref_List as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		LEFT JOIN AD_Ref_List_Trl as rflt ON (rflt.ad_ref_list_id = rfl.ad_ref_list_id AND rflt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}))
		WHERE rfr.name like '%Posting Type%'
	) as posttype ON (posttype.value= $P{PostingType} )
	WHERE PAR.issummary='N'
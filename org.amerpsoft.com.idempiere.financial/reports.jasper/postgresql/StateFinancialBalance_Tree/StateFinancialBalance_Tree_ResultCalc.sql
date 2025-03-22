		SELECT 
		1000000 as ad_client_id, 
		1000077 as  c_elementvalue_id,
		cel2.value, 
		cel2.name, 
		1000000 as C_AcctSchema_ID,
		SUM(openbalance) as openbalance,
		SUM(amtacctdr) as amtacctdr,
		SUM(amtacctcr) as amtacctcr,
		SUM(closebalance) as closebalance
		FROM (
			SELECT
			ele.ad_client_id,
			c_elementvalue_id, ele.value, ele.name, C_AcctSchema_ID, 
			SUM(CASE WHEN (fasg.postingtype = 'A' AND fasg.DateAcct < per.StartDAte) THEN (fasg.amtacctdr - fasg.amtacctcr) ELSE 0 END) as openbalance,
					SUM(CASE WHEN (fasg.postingtype = 'A' AND fasg.DateAcct >= per.StartDAte AND fasg.DateAcct <= per.EndDate) THEN COALESCE(fasg.amtacctdr,0) ELSE 0 END) as amtacctdr,
					SUM(CASE WHEN (fasg.postingtype = 'A' AND fasg.DateAcct >= per.StartDAte AND fasg.DateAcct <= per.EndDate) THEN COALESCE(fasg.amtacctcr,0) ELSE 0 END) as amtacctcr,
					SUM(CASE WHEN ( fasg.postingtype = 'A' AND fasg.DateAcct <= per.EndDate ) THEN (fasg.amtacctdr - fasg.amtacctcr) ELSE 0 END) as closebalance
			FROM c_elementvalue ele
			LEFT JOIN (
				SELECT * FROM fact_acct 
				WHERE c_acctschema_id = 1000000 --$P{C_AcctSchema_ID}  
				AND ad_client_id=1000000 --$P{AD_Client_ID}
				-- $P{AD_Org_ID}
				AND CASE WHEN 1000000=0 OR 1000000 = ad_org_id THEN 1=1 ELSE 1=0 END
			) fasg ON (ele.c_elementvalue_id=fasg.account_id )
			LEFT JOIN c_period as per ON (per.c_period_id = 1000072) --$P{C_Period_ID})
			WHERE ele.issummary='N' AND ele.AccountType IN ('R','E','M')
			AND ele.ad_client_id=1000000 --$P{AD_Client_ID}
			GROUP BY ele.ad_client_id, ele.c_elementvalue_id, fasg.C_AcctSchema_ID
		) as fas2
		LEFT JOIN C_ElementValue as cel2 ON (cel2.C_ElementValue_ID = 1000077)
		WHERE ( openbalance<> 0 or amtacctcr<> 0 or amtacctdr <>0 or closebalance <> 0  )
		GROUP BY fas2.ad_client_id, cel2.name, cel2.value, cel2.c_elementvalue_id, fas2.C_AcctSchema_ID
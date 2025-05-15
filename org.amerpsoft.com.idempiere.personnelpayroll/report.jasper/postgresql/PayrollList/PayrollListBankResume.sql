-- PayrollListBankResume
-- Payroll List For Bank for Resume Report
SELECT 
rep_logo, client_id, cli_name, org_name, org_description, 
iso_code1, cursymbol1, currname1,
iso_code2, cursymbol2, currname2,
amndateini, amndateend,
SUM(amountnetpaid) AS amountnetpaid
FROM (
	SELECT 
	    -- MODELO ASUNCION
	    -- ORGANIZACIÓN
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
		COALESCE(cli.name,cli.description,org.name,'')  cli_name, 
	    pyr.ad_client_id as client_id, pyr.ad_org_id as org_id, pyr.amn_payroll_id,
	    coalesce(org.value,'')  as org_name,
	    COALESCE(org.name,org.description,org.name,org.value,'')  org_description, 
		COALESCE(orginfo.taxid,'')  as org_taxid,    
	   	pyr.invdateend,
   	    -- PERIOD
	    prd.name as periodo, prd.amndateini, prd.amndateend,
	   	-- CURRENCY
		curr1.iso_code as iso_code1,
		COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
		COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
		curr2.iso_code as iso_code2,
		COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
		COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2,  
	    -- PAYROLL RECEIPT
		currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated, 
		currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted, 
		currencyConvert(pyr.amountnetpaid ,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountnetpaid
	FROM adempiere.amn_payroll as pyr
	INNER JOIN adempiere.amn_period   as prd  ON (prd.amn_period_id= pyr.amn_period_id)
	INNER JOIN adempiere.amn_process AS prc   ON (prc.amn_process_id = pyr.amn_process_id)
	INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	LEFT JOIN adempiere.ad_org as org ON (org.ad_org_id = pyr.ad_org_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
	LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
	LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
	LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE pyr.ad_client_id= $P{AD_Client_ID} 
		AND	( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR pyr.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR pyr.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR pyr.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
	ORDER BY pyr.amn_period_id, pyr.amn_payroll_id ASC
) AS bank
GROUP BY rep_logo, client_id, cli_name, org_name, org_description,
iso_code1, cursymbol1, currname1,
iso_code2, cursymbol2, currname2, amndateini, amndateend
ORDER BY org_name
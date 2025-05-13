-- PayrollAcountingCheck
-- 
SELECT * FROM
(
	-- REPORT HEADER
	SELECT DISTINCT ON (cli.ad_client_id)
		-- Client
		cli.ad_client_id as rep_client_id,
		-- Organization
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 0 ELSE $P{AD_Org_ID} END as rep_org_id,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as rep_description, 
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 
			(SELECT STRING_AGG(DISTINCT ORGX.value, '-' ORDER BY ORGX.value) 
	         FROM AD_Org ORGX
	         WHERE ORGX.Ad_client_ID=$P{AD_Client_ID} AND ORGX.issummary = 'N') 
	         ELSE coalesce(org.name,org.value,'') END AS all_orgs,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
	    CASE WHEN  org.ad_client_id = $P{AD_Client_ID}   AND ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 1
	             WHEN  org.ad_client_id = $P{AD_Client_ID}  AND org.ad_org_id= $P{AD_Org_ID}  THEN 1
	             ELSE 0 END as imp_header
	FROM adempiere.ad_client as cli
		 INNER JOIN adempiere.ad_org as org ON (org.ad_client_id = cli.ad_client_id)
		 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
		  LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
		 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
		  LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	WHERE cli.ad_client_id = $P{AD_Client_ID} AND CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR org.ad_org_id = $P{AD_Org_ID} ) THEN 1 = 1 ELSE 1 = 0 END
) as header_info
FULL JOIN
(
SELECT 
	-- AMN_Process
	prc.amn_process_id,
	prc.value as process_value,
	prc.name as process_name,
	-- AMN_Contract
	amc.amn_contract_id,
	amc.value as contract_value, 
	amc.name as contract_name,
	-- AMN_Period
	amp.amn_period_id,
	amp.value as period_value, 
	amp.name as period_nombre,
	amp.amndateini as amnperiod_startdate,
	amp.amndateend as amnperiod_enddate,
	 -- CURRENCY
	CONCAT(curr2.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'')) as moneda,
     curr1.iso_code as iso_code1,
     currt1.cursymbol as cursymbol1,
     COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
     curr2.iso_code as iso_code2,
     currt2.cursymbol as cursymbol2,
     COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
	-- C_Period
	cper.c_period_id,
	cper.startdate as cperiod_startdate,
	cper.enddate as cperiod_enddate,
	cper.name as cperiod_name,
	-- PAYROLL
	emp.value as emp_value,
	emp.name as emp_name,
	pay.value as pay_value,
	pay.amn_payroll_id as amn_payroll_id,
	pay.name as pay_name,
	COALESCE(pay.docstatus,'') as docstatus,
	CASE WHEN pay.posted='Y' THEN 'SI' WHEN pay.posted='N' THEN 'NO' ELSE ''END as posted,
	COALESCE(pay.documentno,'') as documentno ,
	COALESCE(currencyConvert(pay.amountallocated,pay.c_currency_id, $P{C_Currency_ID}, pay.dateacct, pay.C_ConversionType_ID, pay.AD_Client_ID, pay.AD_Org_ID ),0) as amountallocated, 
	COALESCE(currencyConvert(pay.amountdeducted,pay.c_currency_id, $P{C_Currency_ID}, pay.dateacct, pay.C_ConversionType_ID, pay.AD_Client_ID, pay.AD_Org_ID ),0) as amountdeducted, 
	-- COUNT
	COALESCE(paystdr.paycount_draft,0) as paycount_draft,
	COALESCE(paystto.paycount_total,0) as paycount_total,
	COALESCE(paystco.paycount_completed,0) as paycount_completed,
	-- RECIBO
	0 as amn_period_id,
	0 as amn_employee_id
	FROM adempiere.amn_process as prc 
	INNER JOIN adempiere.amn_contract as amc ON (amc.ad_client_id= prc.ad_client_id )
	INNER JOIN adempiere.amn_period amp ON (amp.amn_process_id = prc.amn_process_id AND amp.amn_contract_id = amc.amn_contract_id  )
	INNER JOIN adempiere.c_period as cper ON (cper.c_period_id = amp.c_period_id)
	INNER JOIN adempiere.amn_payroll pay ON (pay.amn_period_id = amp.amn_period_id)
	INNER JOIN adempiere.amn_employee emp ON (emp.amn_employee_id = pay.amn_employee_id)
	LEFT JOIN (
		SELECT  cp2.c_period_id, pr2.Amn_period_id, coalesce(count(*),0) as paycount_draft
		FROM adempiere.amn_payroll pr2
		INNER JOIN adempiere.amn_period am2 ON (am2.amn_period_id = pr2.amn_period_id)
		INNER JOIN adempiere.c_period cp2 ON (cp2.c_period_id = am2.c_period_id)
		WHERE  docstatus<>'CO'  
		GROUP BY cp2.c_period_id, pr2.Amn_period_id
	) as paystdr ON (paystdr.amn_period_id = amp.amn_period_id AND paystdr.c_period_id = cper.c_period_id)
	LEFT JOIN (
		SELECT  cp2.c_period_id, pr2.Amn_period_id, coalesce(count(*),0) as paycount_completed
		FROM adempiere.amn_payroll pr2
		INNER JOIN adempiere.amn_period am2 ON (am2.amn_period_id = pr2.amn_period_id)
		INNER JOIN adempiere.c_period cp2 ON (cp2.c_period_id = am2.c_period_id)
		WHERE  docstatus='CO'  
		GROUP BY cp2.c_period_id, pr2.Amn_period_id
	) as paystco ON (paystco.amn_period_id = amp.amn_period_id AND paystco.c_period_id = cper.c_period_id)
	LEFT JOIN (
		SELECT  cp2.c_period_id, pr2.Amn_period_id, coalesce(count(*),0) as paycount_total
		FROM adempiere.amn_payroll pr2
		LEFT JOIN adempiere.amn_period am2 ON (am2.amn_period_id = pr2.amn_period_id)
		LEFT JOIN adempiere.c_period cp2 ON (cp2.c_period_id = am2.c_period_id)
		GROUP BY cp2.c_period_id, pr2.Amn_period_id
	) as paystto ON (paystto.amn_period_id = amp.amn_period_id AND paystto.c_period_id = cper.c_period_id)
	INNER JOIN adempiere.ad_org as org ON ( org.ad_org_id = pay.ad_org_id)
	INNER JOIN adempiere.ad_client as cli ON (org.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	INNER JOIN c_currency curr1 on pay.c_currency_id = curr1.c_currency_id
    LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
    LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
    LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE cper.C_Period_ID = $P{C_Period_ID}
	AND CASE WHEN $P{AMN_Process_ID}  IS NULL OR prc.amn_process_id= $P{AMN_Process_ID}	THEN 1=1 ELSE 1=0 END
	AND CASE WHEN $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} THEN 1=1 ELSE 1=0 END
) as pracctck ON (1= 0)
ORDER BY  pracctck.process_value, pracctck.Contract_value, pracctck.amnperiod_enddate ,  pracctck.amn_payroll_ID
	
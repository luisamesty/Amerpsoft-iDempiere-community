-- PayrollAccounting
-- Payroll Accounting.jrxml
WITH asientos AS (
SELECT 
	-- CURRENCY
	CONCAT(curr1.iso_code,'-',currt1.cursymbol,'-',COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'')) as moneda,
	-- C_PERIOD
	cper.startdate as startdate,
	cper.enddate as enddate,
	-- PAYROLL
	pyr.amn_process_id,
	pyr.amn_contract_id,
	-- FACT_ACCT
	fac.ad_client_id,	
	fac.ad_org_id,
	org.value AS org_value,
	org.name AS org_name,
	fac.record_id, 
	fac.amtacctdr, 
	fac.amtacctcr, 
	fac.account_id,
	--   amn_location
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 0  
		ELSE pyr.amn_location_id END as amn_location_id,
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y'THEN 'Todas'  
		ELSE lct.value END as loc_value,
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 'Todas las Localidades'  
		ELSE lct.name  END as location,
	--  amn_department
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 0  
		ELSE pyr.amn_department_id END as amn_department_id,
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 'Todos'  
		ELSE dpt.value END as dpto_value,
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 'Todos los Departamentos'  
		ELSE dpt.name END as dpto_name,	
	-- amn_payroll_id
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 0  
		ELSE pyr.amn_payroll_id END as amn_payroll_id,
	-- amn_employee_id
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 0  
		ELSE pyr.amn_employee_id END as amn_employee_id,
	--  description recibo
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y'  THEN '' 
		ELSE pyr.description END as recibo,
	-- documentno
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN ''
		ELSE pyr.documentno END as documentno,
	-- amn_period_id
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y' THEN 0
		ELSE pyr.amn_period_id END as amn_period_id,
	-- fac.description,
	CASE WHEN $P{isSummaryAll}='Y' OR $P{isSummary}='Y'  THEN 'Resumido por Proceso y Contrato'
		ELSE fac.description END as description,
	-- C_ELEMENTVALUE
	COALESCE(ele.value,'NA') as account_value,
	COALESCE(ele.name,'No Aplica') as account_name
	-- TABLES
	FROM adempiere.amn_payroll as pyr
	INNER JOIN (
		SELECT DISTINCT *  FROM adempiere.AMN_Period
		WHERE amndateend between (select DISTINCT startdate from adempiere.C_Period WHERE C_Period_ID=$P{C_Period_ID} ) 
		AND (select DISTINCT enddate from adempiere.C_Period WHERE C_Period_ID=$P{C_Period_ID} ) 
	) as fper ON (fper.AMN_Period_ID = pyr.amn_period_id)
	INNER JOIN adempiere.fact_acct as fac ON ($P{C_AcctSchema_ID}=fac.c_acctschema_id AND fac.record_id = pyr.amn_payroll_id AND fac.ad_table_id = (SELECT AD_Table_ID FROM AD_Table WHERE TableName ='AMN_Payroll') )
	INNER JOIN adempiere.c_elementvalue as ele ON (fac.account_id = ele.c_elementvalue_id)
	INNER JOIN adempiere.ad_org as org ON ( org.ad_org_id = pyr.ad_org_id)
	INNER JOIN adempiere.ad_client as cli ON (org.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.amn_location as lct ON (lct.amn_location_id= pyr.amn_location_id)
	LEFT JOIN adempiere.amn_department as dpt ON (dpt.amn_department_id= pyr.amn_department_id)
	LEFT JOIN adempiere.c_period as cper ON (cper.c_period_id = fac.c_period_id)
	LEFT JOIN c_acctschema sch ON (sch.C_AcctSchema_ID =$P{C_AcctSchema_ID})
	LEFT JOIN c_currency curr1 on sch.c_currency_id = curr1.c_currency_id
	LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE  fper.C_Period_ID = $P{C_Period_ID}
	AND CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR pyr.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END
	AND CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR pyr.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END 
) 
-- MAIN QUER
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
			(SELECT STRING_AGG(DISTINCT ORGX.value, ' - ' ORDER BY ORGX.value) 
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
	-- CURRENCY
	asi.moneda,
	-- ASIENTOS RESUMEN
	1 as resumen,
	asi.startdate,
	asi.enddate,
	-- AD_Client AD_Org
	asi.ad_client_id as client_id, 
	asi.ad_org_id as ad_org_id,
	coalesce(asi.org_value,'')  as org_value,
	coalesce(asi.org_name,'')  as org_name,
	-- PROCESS
	asi.amn_process_id,
	prc.value as proceso,
	prc.name as proceso_nombre,
	-- CONTRACT
	asi.amn_contract_id,
	amc.value as contrato, 
	amc.name as contrato_nombre,
	-- COUNT
	count(CASE WHEN asi.amn_payroll_id IS NULL THEN 0 ELSE 1 END ) as pay_count,
	-- PAYROLL
	asi.amn_payroll_id as amn_payroll_id,
	-- LOCATION
	asi.amn_location_id as amn_location_id,
	asi.loc_value as loc_value,
	asi.location as localidad,
	-- DEPARTMENT
	asi.amn_department_id as amn_department_id,
	asi.dpto_value as dpto_value,
	asi.dpto_name as dpto_name,
	-- RECIBO
	asi.recibo as recibo,
	asi.documentno as documentno,
	0 as amn_period_id,
	0 as amn_employee_id,
	-- AMOUNT DESCRIPTION DB CR
	asi.description as description,
	sum(asi.amtacctdr) as amtacctdr, 
	sum(asi.amtacctcr) as amtacctcr, 
	-- ACCOUNT
	asi.account_value,
	asi.account_name
	FROM asientos as asi 
	INNER JOIN adempiere.amn_contract as amc ON (amc.ad_client_id= asi.ad_client_id )
	INNER JOIN adempiere.amn_process as prc  ON (prc.amn_process_id= asi.amn_process_id AND amc.amn_contract_id= asi.amn_contract_id)
	INNER JOIN adempiere.ad_org as org ON ( org.ad_org_id = asi.ad_org_id)
	INNER JOIN adempiere.ad_client as cli ON (org.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.ad_image AS IMG ON (cliinfo.logoreport_id = IMG.ad_image_id)
	WHERE 1=1
	GROUP BY asi.ad_client_id , asi.ad_org_id, asi.org_value, asi.org_name, cli.name, cli.value, org.value, org.name,  cli.description, org.description, orginfo.taxid, 
	asi.amn_process_id, prc.value, prc.name, asi.amn_contract_id, amc.value, amc.name, 
	asi.recibo, asi.documentno, asi.description, asi.account_value, asi.account_name,	asi.amn_period_id,
	asi.amn_employee_id, asi.amn_location_id, asi.loc_value, asi.location, asi.amn_department_id, asi.dpto_name, asi.dpto_value,
	asi.amn_payroll_id,asi.startdate,	asi.enddate, asi.moneda, img.binarydata
) as pracct ON (1= 0)
ORDER BY pracct.proceso, pracct.contrato, pracct.loc_value , pracct.dpto_value, pracct.org_value, pracct.documentno


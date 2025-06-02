-- PayrollListBank
-- Payroll List For Bank
SELECT 
    -- MODELO ASUNCION
    -- ORGANIZACIÓN
    pyr.ad_client_id as client_id, pyr.ad_org_id as org_id,
    coalesce(org.name,org.value,'')  as org_name,
    COALESCE(org.description,org.name,org.value,'')  org_description, 
	COALESCE(orginfo.taxid,'')  as org_taxid,    
    -- LOCATION
    lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
    -- PERIOD
    prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
    -- CURRENCY
    curr1.iso_code as iso_code1,
    currt1.cursymbol as cursymbol1,
    COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
    curr2.iso_code as iso_code2,
    currt2.cursymbol as cursymbol2,
    COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
    -- EMPLOYEE
    emp.amn_employee_id, 
    emp.value as codigo_trabajador, 
    emp.name as nombre_trabajador,
    COALESCE(jtt.name, jtt.description,'''') as cargo, 
    COALESCE(emp.idnumber, cbp.taxid,'') as nro_id,
    -- DESCRIPCION PAGO
    CASE WHEN prc.amn_process_value = 'NN' THEN 'SALARIO PAGO'
    	WHEN prc.amn_process_value = 'NU' THEN 'AGUINALDOS PAGO'
    	ELSE 'SALARIO PAGO' END AS descripcion_pago,
    CASE WHEN prc.amn_process_value = 'NN' THEN 'NO'
    	WHEN prc.amn_process_value = 'NU' THEN 'SI'
    	ELSE 'NO' END AS descripcion_aguinaldo_sino,	
   	pyr.invdateend,
   	CASE WHEN $P{PayDate} IS NULL THEN now() ELSE $P{PayDate} END AS paymentdate,
    -- CUENTAS
    employ1.a_name AS cuenta_pagadora, employ1.accountno AS cuenta_pagadora_no,
    employ2.a_name AS cuenta_acreedora, employ2.accountno AS cuenta_acreedora_no,
    -- PAYROLL RECEIPT
	currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated, 
	currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted, 
	currencyConvert(pyr.amountnetpaid ,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountnetpaid,
'D' AS ITIREG,
'01' AS ITITRA,
'147' AS ICDSRV,
RPAD(TRIM(employ1.accountno), 10, ' ') AS ICTDEB,
'017' AS IBCOCR,
RPAD(TRIM(employ2.accountno), 10, ' ') AS ICTCRE,
'C' AS ITCRDB,
RPAD(TRIM(pyr.documentno),50,' ') AS IORDEN,
'0' AS IMONED,
REPLACE(to_char(currencyConvert(pyr.amountnetpaid ,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ), '0000000000000.00'),'.','') AS IMTOTR,
RPAD('',15, '0') AS IMTOT2,
RPAD('',12, ' ') AS INRODO,
'0' AS ITIFAC,
RPAD('',20, ' ') AS INRFAC,
'000' AS INRCUO,
CASE WHEN $P{PayDate} IS NULL THEN TO_CHAR(now(), 'YYYYMMDD') ELSE TO_CHAR(CAST($P{PayDate} AS Timestamp), 'YYYYMMDD') END AS IFCHCR,
'00000000' AS IFCHC2,
RPAD('PAGO SALARIOS',50, ' ') AS ICEPTO,
RPAD('',15, ' ') AS INRREF,
CASE WHEN $P{PayDate} IS NULL THEN TO_CHAR(now(), 'YYYYMMDD') ELSE TO_CHAR(CAST($P{PayDate} AS Timestamp), 'YYYYMMDD') END AS IFECCA,
CASE WHEN $P{PayDate} IS NULL THEN TO_CHAR(now(), 'YYYYMMDD') ELSE TO_CHAR(CAST($P{PayDate} AS Timestamp), 'HH12:MI:SS') END  AS IHORCA,
CASE WHEN $P{UserNamePay} IS NULL THEN 'LAGIOCONDA' ELSE RPAD(TRIM($P{UserNamePay}),10, ' ') END  AS IUSUCA
FROM adempiere.amn_payroll as pyr
INNER JOIN adempiere.amn_employee as emp  ON (emp.amn_employee_id= pyr.amn_employee_id) 
INNER JOIN adempiere.c_bpartner   as cbp  ON (emp.c_bpartner_id= cbp.c_bpartner_id)
LEFT JOIN adempiere.amn_jobtitle as jtt   ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
INNER JOIN adempiere.amn_period   as prd  ON (prd.amn_period_id= pyr.amn_period_id)
INNER JOIN adempiere.amn_process AS prc   ON (prc.amn_process_id = pyr.amn_process_id)
LEFT JOIN adempiere.amn_location as lct	  ON (lct.amn_location_id= pyr.amn_location_id)
LEFT JOIN adempiere.amn_department as dpt ON (dpt.amn_department_id= pyr.amn_department_id)
LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
LEFT JOIN adempiere.ad_org as org ON (org.ad_org_id = emp.ad_orgto_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
LEFT JOIN (
	SELECT DISTINCT ON (emp1.amn_employee_id)
		emp1.amn_employee_id,cbb1.a_name, cbb1.accountno
		FROM adempiere.amn_employee emp1
		LEFT JOIN (
			SELECT ad_client_id, c_bpartner_id, a_name, accountno FROM c_bp_bankaccount  WHERE bpbankacctuse ='N'
		)  cbb1 ON cbb1.C_BPartner_ID = emp1.C_BPartner_ID
		WHERE emp1.AD_Client_ID=$P{AD_Client_ID}
) AS employ1 ON employ1.amn_employee_id = emp.amn_employee_id 
LEFT JOIN (
	SELECT DISTINCT ON (emp2.amn_employee_id)
		emp2.amn_employee_id,cbb2.a_name, cbb2.accountno
		FROM adempiere.amn_employee emp2
		LEFT JOIN (
			SELECT ad_client_id, c_bpartner_id, a_name, accountno FROM c_bp_bankaccount  WHERE bpbankacctuse ='B'
		)  cbb2 ON cbb2.C_BPartner_ID = emp2.C_BPartner_ID
		WHERE emp2.AD_Client_ID=$P{AD_Client_ID}
) AS employ2 ON employ2.amn_employee_id = emp.amn_employee_id 
WHERE pyr.ad_client_id= $P{AD_Client_ID} 
	AND	( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR pyr.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR pyr.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR pyr.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
ORDER BY pyr.amn_period_id, pyr.amn_location_id, emp.value ASC, pyr.amn_payroll_id ASC
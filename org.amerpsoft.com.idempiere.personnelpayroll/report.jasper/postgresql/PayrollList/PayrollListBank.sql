-- PayrollListBank
-- Payroll List For Bank
SELECT 
    -- ORGANIZACIÓN
    pyr.ad_client_id as client_id, pyr.ad_org_id as org_id,
    coalesce(org.name,org.value,'')  as org_name,
    COALESCE(org.description,org.name,org.value,'')  org_description, 
	COALESCE(orginfo.taxid,'')  as org_taxid,    
    -- LOCATION
    lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
     -- DEPARTMENT
    dpt.amn_department_id, dpt.value as dpto_value,COALESCE(dpt.name, dpt.description) as dpto_name,
    -- PERIOD
    prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
    -- TIPO DE CONCEPTO
    cty.amn_concept_types_id, cty.calcorder, cty.isshow, cty.value as cty_value, 
    COALESCE(cty.name, cty.description) as concept_type,
    --COALESCE(pyr_d.name,cty.name, cty.description) as concept_type,
    -- TIPO DE CONCEPTO (PROCESO)
    ctp.value as ctp_value, COALESCE(ctp.name, ctp.description) as concept_type_process, 	 
    -- PROCESS
    prc.amn_process_id, COALESCE(prc.name, prc.description) as proceso,
    -- CONTRACT
     -- CURRENCY
     curr1.iso_code as iso_code1,
     currt1.cursymbol as cursymbol1,
     COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
     curr2.iso_code as iso_code2,
     currt2.cursymbol as cursymbol2,
     COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
    amc.amn_contract_id, amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
    -- EMPLOYEE
    emp.amn_employee_id, emp.value as value_emp, emp.name as empleado,emp.incomedate as fecha_ingreso,
    COALESCE(jtt.name, jtt.description,'''') as cargo, 
    COALESCE(cbp.taxid,'') as nro_id,
     -- PAYROLL
    pyr.amn_payroll_id, pyr.description as recibo,
    --pyr.amountallocated as amountallocated_t, pyr.amountdeducted as amountdeducted_t, pyr.amountcalculated as amountcalculated_t,
    -- PAYROLL DETAIL
    pyr_d.amn_payroll_detail_id,
    pyr_d.qtyvalue as cantidad, 
	currencyConvert(pyr_d.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated, 
	currencyConvert(pyr_d.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted, 
	currencyConvert(pyr_d.amountcalculated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountcalculated
FROM adempiere.amn_payroll as pyr
    LEFT JOIN adempiere.amn_payroll_detail 		as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
    LEFT JOIN adempiere.amn_concept_types_proc as ctp 	 ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
    LEFT JOIN adempiere.amn_concept_types 			as cty 	 ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
    LEFT JOIN adempiere.amn_process  					as prc 	 ON (prc.amn_process_id= ctp.amn_process_id)
    LEFT JOIN adempiere.amn_employee as emp    ON (emp.amn_employee_id= pyr.amn_employee_id) 
    LEFT JOIN adempiere.c_bpartner 						as cbp 	 ON (emp.c_bpartner_id= cbp.c_bpartner_id)
    LEFT JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
    LEFT JOIN adempiere.amn_period   					as prd 	 ON (prd.amn_period_id= pyr.amn_period_id)
    LEFT JOIN adempiere.amn_location 					as lct 	 ON (lct.amn_location_id= pyr.amn_location_id)
    LEFT JOIN adempiere.amn_department 				as dpt 	 ON (dpt.amn_department_id= pyr.amn_department_id)
    LEFT JOIN adempiere.amn_contract 					as amc 	 ON (amc.amn_contract_id= pyr.amn_contract_id)	 	 
	LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
    LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
    LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
    LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	 INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
	 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	 LEFT JOIN adempiere.ad_org as org ON (org.ad_org_id = pyr.ad_org_id)
	 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
WHERE pyr.ad_client_id= $P{AD_Client_ID} 
	AND CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR pyr.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END 
	AND	( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR pyr.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR pyr.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR pyr.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
	AND CASE WHEN ( $P{AMN_Location_ID}  IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1= 1 ELSE 1=0 END 
	AND CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN 1=1 ELSE 1=0 END 
ORDER BY pyr.amn_period_id, pyr.amn_location_id, pyr.amn_department_id,  emp.value ASC, pyr.amn_payroll_id ASC,  pyr_d.calcorder, cty.isshow DESC
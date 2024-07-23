-- Payroll Receipt NV
-- Used for  reports and individual print for Vacation
SELECT * FROM
(
SELECT DISTINCT
-- ORGANIZATION
    org.ad_client_id as org_client, org.ad_org_id as org_org,
    coalesce(org.value,'') as org_value,
	coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description,org.name,org.value,'') as org_description, 
	COALESCE(orginfo.taxid,'') as org_taxid,
	CASE WHEN img1.binarydata IS NOT NULL THEN img1.binarydata ELSE img2.binarydata END as org_logo,
    loc.regionname as region,
    COALESCE(loc.regionname, '') as estado,
	loc.city as ciudad,
    CONCAT(loc.address1,' ', loc.address2) as direccion,
-- LOCATION
	 lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
-- PERIOD
	 prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
	 prd.refdateini, prd.refdateend,
-- TIPO DE CONCEPTO
	 cty.amn_concept_types_id, cty.optmode, cty.calcorder, cty.isshow, cty.value as cty_value, COALESCE(cty.name, cty.description) as concept_type,
-- TIPO DE CONCEPTO (PROCESO)
	 ctp.value as ctp_value, COALESCE(ctp.name, ctp.description) as concept_type_process, 	 
-- PROCESS
	 prc.amn_process_id, COALESCE(prc.name, prc.description) as proceso,
     --CASE WHEN ( {AMN_Process_ID}  IS NULL OR prc.amn_process_id= AMN_Process_ID} ) THEN 1 ELSE 0 END AS imp_proceso,
-- CONTRACT
	 amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
-- DEPARTAMENT
       COALESCE(dep.name,dep.description) as departamento,
-- EMPLOYEE
	 emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso,
	 (DATE_PART('year', CURRENT_TIMESTAMP) - DATE_PART('year', emp.incomedate)) as a_servicio,
   COALESCE(jtt.name, jtt.description,'') as cargo, 
   COALESCE(cbp.taxid,'') as nro_id, 
   COALESCE(histo.salary_hist,CAST(0 as numeric)) as salary_hist, 
   COALESCE(emp.salary,CAST(0 as numeric)) as salario,
-- PAYROLL
   'Original Trabajador' as copia,
   COALESCE(pyr.documentno,'') as documentno,
   	pyr.description as recibo,
	pyr.amn_payroll_id,
	pyr.amountallocated as amountallocated_t, 
	pyr.amountdeducted as amountdeducted_t, 
	pyr.amountcalculated as amountcalculated_t,
	pyr.invdateini, pyr.invdateend,
	pyr.description as payroll_desc,
-- PYR AMOUNTS CONVERTED
	currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated_t2, 
	currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted_t2, 
	currencyConvert(pyr.amountcalculated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountcalculated_t2, 
-- CURRENCY
	curr1.iso_code as iso_code1,
	COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
	COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
	curr2.iso_code as iso_code2,
	COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
	COALESCE(currt2.description,curr2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
-- PAYROLL DETAIL
   -- MONTOS Y CIFRAS	   
  	   CASE WHEN cty.Value='SALVACAC' THEN pyr_d.amountcalculated  ELSE 0 END  as salario_vac,
	 pyr_d.value as detail_value,
	 pyr_d.qtyvalue as cantidad, 
	 pyr_d.amountallocated, 
	 pyr_d.amountdeducted, 
	 pyr_d.amountcalculated,
	currencyConvert(pyr_d.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated2, 
	currencyConvert(pyr_d.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted2, 
	currencyConvert(pyr_d.amountcalculated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountcalculated2 
FROM adempiere.amn_payroll as pyr
LEFT JOIN (
	SELECT DISTINCT ON (pyr.amn_payroll_id) pyr.amn_payroll_id, pyh.salary as salary_hist , pyh.amn_period_yyyymm 
	FROM adempiere.amn_payroll as pyr
	left join AMN_Payroll_Historic pyh on pyr.amn_employee_id = pyh.amn_employee_id  and to_char(pyr.dateacct,'YYYY-MM') = to_char(pyh.validto ,'YYYY-MM')
) as histo on histo.amn_payroll_id = pyr.amn_payroll_id
INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
 LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
INNER JOIN adempiere.ad_org   as org ON (org.ad_org_id = pyr.ad_org_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
 LEFT JOIN adempiere.c_location as loc ON (orginfo.c_location_id = loc.c_location_id)
 LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
 LEFT JOIN adempiere.amn_payroll_detail 		as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
 LEFT JOIN adempiere.amn_concept_types_proc as ctp 	 ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
 LEFT JOIN adempiere.amn_concept_types 			as cty 	 ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
 LEFT JOIN adempiere.amn_process  					as prc 	 ON (prc.amn_process_id= pyr.amn_process_id)
 INNER JOIN adempiere.amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
 LEFT JOIN adempiere.amn_department as dep ON (emp.amn_department_id = dep.amn_department_id)
 LEFT JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
 LEFT JOIN adempiere.c_bpartner 						as cbp 	 ON (emp.c_bpartner_id= cbp.c_bpartner_id)
 LEFT JOIN adempiere.amn_period   					    as prd 	 ON (prd.amn_period_id= pyr.amn_period_id)
 LEFT JOIN adempiere.amn_location 					as lct 	 ON (lct.amn_location_id= pyr.amn_location_id)
 LEFT JOIN adempiere.amn_contract 					as amc 	 ON (amc.amn_contract_id= pyr.amn_contract_id)	 
-- LEFT JOIN adempiere.amn_rates as rat ON (pyr.RefDateIni between rat.startdate AND rat.enddate)
LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = 'es_VE'
LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = 'es_VE'
WHERE prc.value= 'NV'  AND org.ad_client_id=  $P{AD_Client_ID}  
    AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR org.ad_org_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
    AND ( CASE WHEN ( $P{Record_ID} IS NULL OR pyr.amn_payroll_id= $P{Record_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{isShowZERO} = 'Y') OR ($P{isShowZERO} = 'N' 
    			AND (  pyr_d.qtyvalue <> 0 OR pyr_d.amountallocated <> 0 OR pyr_d.amountdeducted<>0  OR pyr_d.amountcalculated<> 0)) THEN 1=1 ELSE 1=0 END )
) as nomina
  ORDER BY  org_name ASC, value_emp ASC,  documentno, copia DESC,  calcorder ASC,  isshow DESC
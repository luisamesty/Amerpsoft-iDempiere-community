-- Payroll Receipt PJ Pagos Judiciales
-- PJ UPDATED FROM Org *
SELECT * FROM
(SELECT DISTINCT
-- ORGANIZATION
    org.ad_client_id as ad_client_id, org.ad_org_id as ad_org_id,
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 0 ELSE $P{AD_Org_ID} END as rep_org_id,
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as rep_description, 
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
    CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE loc.regionname END as region,
    CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN ''   ELSE COALESCE(loc.regionname, '') END as estado,
	CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE loc.city END as ciudad,
    CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE CONCAT(loc.address1,' ', loc.address2) END as direccion,
	CASE WHEN 0 = 0 THEN 1 ELSE 0 END as imp_org,
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
	 emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso, emp.idnumber,
	 (DATE_PART('year', CURRENT_TIMESTAMP) - DATE_PART('year', emp.incomedate)) as a_servicio,
   COALESCE(jtt.name, jtt.description) as cargo, cbp.taxid as nro_id, emp.salary as salario,
-- PAYROLL
   'Original Trabajador' as copia,
   COALESCE(pyr.documentno,'') as documentno,
   	pyr.description as recibo,
	pyr.amn_payroll_id, pyr.dateacct,
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
	 pyr_d.value as detail_value,
	 pyr_d.qtyvalue as cantidad, 
	 pyr_d.amountallocated, 
	 pyr_d.amountdeducted, 
	 pyr_d.amountcalculated,
	 pyr_d.description as paydet_description,
	currencyConvert(pyr_d.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated2, 
	currencyConvert(pyr_d.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted2, 
	currencyConvert(pyr_d.amountcalculated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountcalculated2	 
FROM adempiere.amn_payroll as pyr
INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
 LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
 LEFT JOIN adempiere.ad_org   as org ON (org.ad_org_id = pyr.ad_org_id)
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
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
WHERE prc.value= 'PJ' AND org.ad_client_id=  $P{AD_Client_ID}  
	AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR pyr.ad_org_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )     
    AND ( CASE WHEN ( $P{Record_ID} IS NULL OR pyr.amn_payroll_id= $P{Record_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{isShowZERO} = 'Y') OR ($P{isShowZERO} = 'N' 
    			AND (  pyr_d.qtyvalue <> 0 OR pyr_d.amountallocated <> 0 OR pyr_d.amountdeducted<>0  OR pyr_d.amountcalculated<> 0)) THEN 1=1 ELSE 1=0 END )
) as nomina
LEFT JOIN 
  (
    SELECT DISTINCT pyr_def.amn_payroll_id as payroll_id, pyr_def.amn_payroll_deferred_id as deferred_id,
	       pyr_def.amountcalculated as calculated_def, 
	       pyr_def.amountallocated as allocated_def, 
	       pyr_def.amountdeducted as deducted_def,
	       currencyConvert(pyr_def.amountcalculated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as calculated_def2, 
	       currencyConvert(pyr_def.amountallocated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as allocated_def2, 
		   currencyConvert(pyr_def.amountdeducted,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as deducted_def2, 
		   pyr_def.value as value_def, pyr_def.name AS name_def, pyr_def.description as description_def, 
		   pyr_def.qtyvalue as cantidad_def,
		   pyr_def.line as linea_def, pyr_def.ispaid, 
		   pyr_def.duedate,
		   -- PERIOD DEF.
  	       prd_def.amn_period_id as period_def_id, prd_def.name as periodo_def, 
  	       prd_def.amndateini as period_inidef, prd_def.amndateend as period_enddef, 
  	       prd_def.amn_period_status
    FROM adempiere.amn_payroll_deferred as pyr_def
    LEFT JOIN adempiere.amn_period as prd_def ON (pyr_def.amn_period_id = prd_def.amn_period_id)
    LEFT JOIN adempiere.amn_payroll as pyr2 ON (pyr2.amn_payroll_id=pyr_def.amn_payroll_id)
  ) as diferido ON (nomina.amn_payroll_id = diferido.payroll_id) 
WHERE (imp_org= 1) OR (((ad_client_id=  $P{AD_Client_ID}  AND ad_org_id= $P{AD_Org_ID} ) OR (nomina.amn_payroll_id= $P{Record_ID} )) 
   )
ORDER BY nomina.loc_value, nomina.value_emp, diferido.duedate, nomina.concept_type ASC, nomina.isshow DESC, nomina.calcorder, diferido.value_def, diferido.linea_def
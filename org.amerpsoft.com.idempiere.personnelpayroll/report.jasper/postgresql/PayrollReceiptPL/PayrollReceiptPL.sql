SELECT * FROM
     (SELECT DISTINCT
   -- ORGANIZATION 
      org.ad_client_id as org_client, org.ad_org_id as org_org,
	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') 
  		   ELSE coalesce(org.name,org.value,'') 
  	  END as org_name,
	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') 
  		   ELSE COALESCE(org.description,org.name,org.value,'') 
  	  END as org_description, 
	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN '' 
  		   ELSE COALESCE(orginfo.taxid,'') 
  	  END as org_taxid,
	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN img1.binarydata 
  		   ELSE img2.binarydata 
   	  END as org_logo,  
   	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN '' 
   	        ELSE COALESCE(reg.name, reg.description, loc.regionname, '') 
   	  END as estado,
	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN '' 
	       ELSE CONCAT(COALESCE(loc.address1,' '), ' ', COALESCE(loc.address2,' ')) 
      END as direccion ,
	  CASE WHEN ( $P{AD_Org_ID} = 0) OR ( pyr.ad_org_id = 0 ) THEN '' 	 
	       ELSE loc.city 
	  END as ciudad, 
	  CASE WHEN ((pyr.amn_payroll_id =  $P{Record_ID} ) OR (org.ad_client_id =  $P{AD_Client_ID}  AND  pyr.ad_org_id  = 0 )) THEN 1
	            WHEN (org.ad_client_id =  $P{AD_Client_ID}  AND org.ad_org_id=  $P{AD_Org_ID} )  THEN 1
	            ELSE 0 
	  END as imp_org
   FROM adempiere.amn_payroll as pyr
     INNER JOIN adempiere.ad_org as org ON (pyr.ad_org_id = org.ad_org_id)
	 INNER JOIN adempiere.ad_client as cli ON (org.ad_client_id = cli.ad_client_id)
	 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	  LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	  LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
  	  LEFT JOIN adempiere.c_location as loc ON (orginfo.c_location_id = loc.c_location_id)
	  LEFT JOIN adempiere.c_region as reg ON (loc.c_region_id = reg.c_region_id)
  ) as org_inf
  FULL JOIN
  (SELECT DISTINCT
    -- ORGANIZATION
       pyr.ad_client_id as client_id, pyr.ad_org_id as org_id,
	-- LOCATION
	   lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
       CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1 ELSE 0 END AS imp_localidad,
   -- PERIOD
	   prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
       CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1 ELSE 0 END AS imp_periodo,
   -- TIPO DE CONCEPTO
	   cty.amn_concept_types_id, cty.calcorder, cty.isshow, cty.value as cty_value, cty.name as concept_name
	   COALESCE(pyr_d.name,cty.name, cty.description) as concept_type,
   -- UOM 
	   ctu.value as uom_value, ctu.name as uom_name, 
   -- TIPO DE CONCEPTO (PROCESO)
	   ctp.value as ctp_value, COALESCE(ctp.name, ctp.description) as concept_type_process, 	 
   -- PROCESS
	   prc.amn_process_id, COALESCE(prc.name, prc.description) as proceso,
       --CASE WHEN ( P{AMN_Process_ID}  IS NULL OR prc.amn_process_id= P{AMN_Process_ID} ) THEN 1 ELSE 0 END AS imp_proceso,
   -- CONTRACT
	   amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
       CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1 ELSE 0 END AS imp_contrato,
   -- EMPLOYEE
	   emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso,
	   date_part('year',age(pyr.invdateend, emp.incomedate)) a_servicio, 
       date_part('month',age(pyr.invdateend, emp.incomedate)) m_servicio, 
       date_part('day',age(pyr.invdateend, emp.incomedate)) d_servicio,
       COALESCE(jtt.name, jtt.description) as cargo, cbp.taxid as nro_id, emp.salary as salario,
       CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id=  $P{AMN_Employee_ID}) THEN 1 ELSE 0 END AS imp_empleado, 
   -- DEPARTAMENT
       COALESCE(dep.name,dep.description) as departamento,
   -- PAYROLL
	   pyr.amn_payroll_id, pyr.amountallocated as amountallocated_t, pyr.amountdeducted as amountdeducted_t, pyr.amountcalculated as amountcalculated_t,
	   pyr.invdateini, pyr.invdateend, pyr.refdateini, pyr.refdateend,
   -- PAYROLL DETAIL
       CASE WHEN cty.calcorder >= 400000 AND cty.calcorder <= 400099 THEN  'Salarios y referencias'
   		 WHEN cty.calcorder >= 400100 AND cty.calcorder <= 400199 THEN  'Antiguedad'
   		 WHEN cty.calcorder >= 400200 AND cty.calcorder <= 400299 THEN  'Vacaciones'
   		 WHEN cty.calcorder >= 400300 AND cty.calcorder <= 400399 THEN  'Utilidades'
   		 WHEN cty.calcorder >= 400600 AND cty.calcorder <= 400699 THEN  'Preaviso'
   		 WHEN cty.calcorder >= 400800 AND cty.calcorder <= 400899 THEN  'Anticipos'
   		 WHEN cty.calcorder >= 400900 AND cty.calcorder <= 400999 THEN  'Ajustes'
   		 ELSE 'Otros' END as pyrgroup,
   	   CASE WHEN cty.calcorder >= 400000 AND cty.calcorder <= 400099 THEN  '000'
   		 WHEN cty.calcorder >= 400100 AND cty.calcorder <= 400199 THEN  '100'
   		 WHEN cty.calcorder >= 400200 AND cty.calcorder <= 400299 THEN  '200'
   		 WHEN cty.calcorder >= 400300 AND cty.calcorder <= 400399 THEN  '300'
   		 WHEN cty.calcorder >= 400600 AND cty.calcorder <= 400699 THEN  '600'
   		 WHEN cty.calcorder >= 400800 AND cty.calcorder <= 400899 THEN  '800'
   		 WHEN cty.calcorder >= 400900 AND cty.calcorder <= 400999 THEN  '900'
   		 ELSE '999' END as pyrgroup_order,
   		 pyr_d.name as detail_name, pyr_d.description as detail_description, 
   		 pyr_d.value as detail_value, qtyvalue as cantidad, pyr_d.amountallocated, pyr_d.amountdeducted, pyr_d.amountcalculated
	FROM adempiere.amn_payroll as pyr
	INNER JOIN adempiere.amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
	 LEFT JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
	 LEFT JOIN adempiere.amn_concept_types	as cty ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
	 LEFT JOIN adempiere.amn_concept_uom	as ctu ON (ctu.amn_concept_uom_id= cty.amn_concept_uom_id)
	 LEFT JOIN adempiere.amn_process as prc ON (prc.amn_process_id= ctp.amn_process_id)
	INNER JOIN adempiere.amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
	 LEFT JOIN adempiere.amn_department as dep ON (emp.amn_department_id = dep.amn_department_id)
	 LEFT JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
	 LEFT JOIN adempiere.c_bpartner 	as cbp	 ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	 LEFT JOIN adempiere.amn_period as prd ON (prd.amn_period_id= pyr.amn_period_id)
	 LEFT JOIN adempiere.amn_location as lct ON (emp.amn_location_id= lct.amn_location_id)
	 LEFT JOIN adempiere.amn_contract as amc ON (amc.amn_contract_id= pyr.amn_contract_id)	 
	WHERE prc.value= 'PL'
  ) as nomina ON (1= 0)
WHERE (imp_org= 1) OR (((client_id=  $P{AD_Client_ID}   AND org_id= $P{AD_Org_ID}  ) OR (nomina.amn_payroll_id=  $P{Record_ID} )) 
   AND imp_contrato= 1 AND imp_periodo= 1 AND imp_localidad= 1 AND imp_empleado= 1)

ORDER BY  nomina.amndateini, nomina.loc_value ASC, nomina.pyrgroup_order, nomina.value_emp ASC, nomina.isshow DESC, nomina.calcorder, org_inf ASC
-- Payroll Receipt CrossTab (Column View)
-- Used for  reports and individual print
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
	loc.city as ciudad,
    CONCAT(loc.address1,' ', loc.address2) as direccion,
	-- LOCATION
	 lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
	-- PERIOD
	 prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
	 prd.refdateini, prd.refdateend,
	-- CONTRACT
   amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
	-- DEPARTAMENT
   COALESCE(dep.name,dep.description) as departamento,
	-- EMPLOYEE
   emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso,
   COALESCE(jtt.name, jtt.description,'') as cargo, 
   COALESCE(cbp.taxid,'') as nro_id, 
   COALESCE(emp.salary,CAST(0 as numeric)) as salario,
   COALESCE(histo.salary_hist,CAST(0 as numeric)) as salary_hist,
   -- CURRENCY
	curr1.iso_code as iso_code1,
	COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
	COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
	curr2.iso_code as iso_code2,
	COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
	COALESCE(currt2.description,curr2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
-- PAYROLL
   CASE WHEN $P{isPrintCopy} = 'Y' THEN 'Original Trabajador' Else  'Original ' END as copia,
   '01' as copiaforma,
   COALESCE(pyr.documentno,'') as documentno,
   	pyr.description as recibo,
	pyr.amn_payroll_id,
	-- CONCEPTOS RESUMIDOS
	pyr_d.salariobasico,
	pyr_d.subtotal, 
	pyr_d.cantidad, 
	pyr_d.horasextras,
	pyr_d.otros,
	pyr_d.comisadm,
	pyr_d.comisaph,
	pyr_d.comisplaza,
	pyr_d.reposo,
	pyr_d.otroshaberes,
	pyr_d.boniffam,
	pyr_d.otrosingresos,
	pyr_d.ipstrabajador,
	pyr_d.irptrabajador,
	pyr_d.ausencia,
	pyr_d.otrosd,
	pyr_d.embargo,
	pyr_d.otrosdescuentos,
	pyr_d.ipspatron
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
	 LEFT JOIN (
			SELECT 
				resum.amn_payroll_id,
				sum(resum.salariobasico) AS salariobasico, 
				sum(resum.subtotal) AS subtotal, 
				sum(resum.cantidad) AS cantidad, 
				sum(resum.horasextras) AS horasextras,
				sum(resum.otros) AS otros,
				sum(resum.comisadm) AS comisadm,
				sum(resum.comisaph) AS comisaph,
				sum(resum.comisplaza) AS comisplaza,
				sum(resum.reposo) AS reposo,
				sum(resum.otroshaberes) AS otroshaberes,
				sum(resum.boniffam) AS boniffam,
				sum(resum.otrosingresos) AS otrosingresos,
				sum(resum.ipstrabajador) AS ipstrabajador,
				sum(resum.irptrabajador) AS irptrabajador,
				sum(resum.ausencia) AS ausencia,
				sum(resum.otrosd) AS otrosd,
				sum(resum.embargo) AS embargo,
				sum(resum.otrosdescuentos) AS otrosdescuentos,
				sum(resum.ipspatron) AS ipspatron
			FROM (
				SELECT 	
				-- RECIBO
				pyr.amn_payroll_id,
				-- TIPO DE CONCEPTO
				cty.calcorder, cty.value as cty_value, COALESCE(cty.name, cty.description) as concept_type,
				-- PAYROLL DETAIL
				-- *************
			   	-- ASIGNACIONES	  
				-- *************
				-- cty.sign = 'D'
			   	-- SB		Salario Básico Trabajador 
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SB' THEN pyr_d.amountcalculated  ELSE 0 END  as salariobasico,
			  	--SALARIO		Salario Trabajador
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SALARIO' THEN pyr_d.amountallocated  ELSE 0 END  as subtotal,
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SALARIO' THEN pyr_d.qtyvalue ELSE 0 END  as cantidad,
			  	--HED		Horas Extras
			  	CASE WHEN cty.sign = 'D' AND cty.Value='HED' THEN pyr_d.amountallocated  ELSE 0 END  as horasextras,
				--OTROS		Otros Ingresos
			  	CASE WHEN cty.sign = 'D' AND cty.Value='OTROS' THEN pyr_d.amountallocated  ELSE 0 END  as otros,
				--COMISADM	Comisión Adm
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISADM' THEN pyr_d.amountallocated  ELSE 0 END  as comisadm,
				--COMISAPH	Comisión aph
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISAPH' THEN pyr_d.amountallocated  ELSE 0 END  as comisaph,
				--COMISPLAZA	Comisión Plaza
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISPLAZA' THEN pyr_d.amountallocated  ELSE 0 END  as comisplaza,
				--REPOSO		Reposos
				CASE WHEN cty.sign = 'D' AND cty.Value='REPOSO' THEN pyr_d.amountallocated  ELSE 0 END  as reposo,
				--OTROSHAB	Otros Haberes
				CASE WHEN cty.sign = 'D' AND cty.Value='OTROSHAB' THEN pyr_d.amountallocated  ELSE 0 END  as otroshaberes,
				--BONIFFAM	Bonificación Familiar
				CASE WHEN cty.sign = 'D' AND cty.Value='BONIFFAM' THEN pyr_d.amountallocated  ELSE 0 END  as boniffam,
				-- OTROS CONCEPTOS DE ASIGNACION
				CASE WHEN  cty.sign = 'D' AND cty.Value NOT IN ('SB','SALARIO', 'HED', 'OTROS', 'COMISADM', 'COMISAPH', 'COMISPLAZA', 'REPOSO', 'OTROSHAB', 'BONIFFAM') THEN pyr_d.amountallocated  ELSE 0 END  as otrosingresos,
				-- *************
				-- DEDUCCIONES
				-- *************
				-- cty.sign = 'C'	THEN
				--IPS_T		IPS Trabajador
				CASE WHEN cty.sign = 'C' AND cty.Value='IPS_T' THEN pyr_d.amountdeducted  ELSE 0 END  as ipstrabajador,
				--IRP		IRP
				CASE WHEN cty.sign = 'C' AND cty.Value='IRP' THEN pyr_d.amountdeducted  ELSE 0 END  as irptrabajador,
				--AUSENCIA	Ausencias
				CASE WHEN cty.sign = 'C' AND cty.Value='AUSENCIA' THEN pyr_d.amountdeducted  ELSE 0 END  as ausencia,
				--OTROSD		Otros Descuentos
				CASE WHEN cty.sign = 'C' AND cty.Value='OTROSD' THEN pyr_d.amountdeducted  ELSE 0 END  as otrosd,
				--EMBARGO		Embargo Juducial
				CASE WHEN cty.sign = 'C' AND cty.Value='EMBARGO' THEN pyr_d.amountdeducted  ELSE 0 END  as embargo,
				-- OTROS CONCEPTOS DE DEDUCCION
				CASE WHEN  cty.sign = 'C' AND cty.Value NOT IN ('IPS_T','IRP', 'AUSENCIA', 'OTROS', 'OTROSD', 'EMBARGO') THEN pyr_d.amountdeducted  ELSE 0 END  as otrosdescuentos,
				-- *************
				-- OTROS CALCULOS
				-- *************
				--IPS_P		IPS Patrón
				CASE WHEN cty.sign = 'C' AND cty.Value='IPS_P' THEN pyr_d.amountcalculated  ELSE 0 END  as ipspatron
				FROM adempiere.amn_payroll as pyr
				LEFT JOIN (
					SELECT DISTINCT ON (pyr.amn_payroll_id) pyr.amn_payroll_id, pyh.salary as salary_hist , pyh.amn_period_yyyymm 
					FROM adempiere.amn_payroll as pyr
					left join AMN_Payroll_Historic pyh on pyr.amn_employee_id = pyh.amn_employee_id  and to_char(pyr.dateacct,'YYYY-MM') = to_char(pyh.validto ,'YYYY-MM')
				) as histo on histo.amn_payroll_id = pyr.amn_payroll_id
				INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
				INNER JOIN (
					SELECT 
					pyr2.amn_payroll_id, pyr_d2.amn_payroll_detail_id, pyr_d2.amn_concept_types_proc_id, pyr_d2.qtyvalue,
					currencyConvert(pyr_d2.amountallocated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountallocated, 
				currencyConvert(pyr_d2.amountdeducted,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountdeducted, 
				currencyConvert(pyr_d2.amountcalculated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountcalculated 
					FROM adempiere.amn_payroll_detail AS pyr_d2
					INNER JOIN adempiere.amn_payroll pyr2 ON pyr2.amn_payroll_id = pyr_d2.amn_payroll_id 
					WHERE CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr2.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END 
				) as pyr_d  ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
			 	LEFT JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
				LEFT JOIN adempiere.amn_concept_types 	as cty 	  ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
				LEFT JOIN adempiere.amn_period   as prd 	 ON (prd.amn_period_id= pyr.amn_period_id)
	 			WHERE pyr.ad_client_id=  $P{AD_Client_ID}  
					AND ( CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END )
			     	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
					AND ( CASE WHEN ( $P{isShowZERO} = 'Y') OR ($P{isShowZERO} = 'N' 
			    	AND (  pyr_d.qtyvalue <> 0 OR pyr_d.amountallocated <> 0 OR pyr_d.amountdeducted<>0  OR pyr_d.amountcalculated<> 0)) THEN 1=1 ELSE 1=0 END ) 
				ORDER BY pyr.amn_payroll_id, cty.calcorder
			) AS resum
			GROUP BY resum.amn_payroll_id --, resum.calcorder, resum.cty_value, resum.concept_type --, resum.salariobasico, resum.subtotal,  resum.horasextras, resum.comisadm, resum.ipspatron	 
	 )	as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
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
	WHERE org.ad_client_id=  $P{AD_Client_ID}  
	AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR org.ad_org_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
    AND ( CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Department_ID} IS NULL OR dep.amn_department_id= $P{AMN_Department_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
UNION
	SELECT DISTINCT
	-- ORGANIZATION
    org.ad_client_id as org_client, org.ad_org_id as org_org,
    coalesce(org.value,'') as org_value,
	coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description,org.name,org.value,'') as org_description, 
	COALESCE(orginfo.taxid,'') as org_taxid,
	CASE WHEN img1.binarydata IS NOT NULL THEN img1.binarydata ELSE img2.binarydata END as org_logo,
    loc.regionname as region,
	loc.city as ciudad,
    CONCAT(loc.address1,' ', loc.address2) as direccion,
	-- LOCATION
	 lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
	-- PERIOD
	 prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
	 prd.refdateini, prd.refdateend,
	-- CONTRACT
	 amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
	-- DEPARTAMENT
       COALESCE(dep.name,dep.description) as departamento,
	-- EMPLOYEE
	 emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso,
   COALESCE(jtt.name, jtt.description) as cargo, 
   COALESCE(cbp.taxid,'') as nro_id, 
   COALESCE(emp.salary,CAST(0 as numeric)) as salario,
   COALESCE(histo.salary_hist,CAST(0 as numeric)) as salary_hist,
   -- CURRENCY
	curr1.iso_code as iso_code1,
	COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
	COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
	curr2.iso_code as iso_code2,
	COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
	COALESCE(currt2.description,curr2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
	-- PAYROLL
   CASE WHEN $P{isPrintCopy} = 'Y' THEN 'Copia Empresa' Else  'Copia' END as copia,
   CASE WHEN $P{isPrintCopy} = 'Y' THEN '02' ELSE 'XX' END as copiaforma,
    COALESCE(pyr.documentno,'') as documentno,
   	pyr.description as recibo,
	pyr.amn_payroll_id,
	-- CONCEPTOS RESUMIDOS
	pyr_d.salariobasico,
	pyr_d.subtotal, 
	pyr_d.cantidad,
	pyr_d.horasextras,
	pyr_d.otros,
	pyr_d.comisadm,
	pyr_d.comisaph,
	pyr_d.comisplaza,
	pyr_d.reposo,
	pyr_d.otroshaberes,
	pyr_d.boniffam,
	pyr_d.otrosingresos,
	pyr_d.ipstrabajador,
	pyr_d.irptrabajador,
	pyr_d.ausencia,
	pyr_d.otrosd,
	pyr_d.embargo,
	pyr_d.otrosdescuentos,
	pyr_d.ipspatron	
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
	 LEFT JOIN (
			SELECT 
				resum.amn_payroll_id,
				sum(resum.salariobasico) AS salariobasico, 
				sum(resum.subtotal) AS subtotal, 
				sum(resum.cantidad) AS cantidad, 
				sum(resum.horasextras) AS horasextras,
				sum(resum.otros) AS otros,
				sum(resum.comisadm) AS comisadm,
				sum(resum.comisaph) AS comisaph,
				sum(resum.comisplaza) AS comisplaza,
				sum(resum.reposo) AS reposo,
				sum(resum.otroshaberes) AS otroshaberes,
				sum(resum.boniffam) AS boniffam,
				sum(resum.otrosingresos) AS otrosingresos,
				sum(resum.ipstrabajador) AS ipstrabajador,
				sum(resum.irptrabajador) AS irptrabajador,
				sum(resum.ausencia) AS ausencia,
				sum(resum.otrosd) AS otrosd,
				sum(resum.embargo) AS embargo,
				sum(resum.otrosdescuentos) AS otrosdescuentos,
				sum(resum.ipspatron) AS ipspatron
			FROM (
				SELECT 	
				-- RECIBO
				pyr.amn_payroll_id,
				-- TIPO DE CONCEPTO
				cty.calcorder, cty.value as cty_value, COALESCE(cty.name, cty.description) as concept_type,
				-- PAYROLL DETAIL
				-- *************
			   	-- ASIGNACIONES	  
				-- *************
				-- cty.sign = 'D'
			   	-- SB		Salario Básico Trabajador 
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SB' THEN pyr_d.amountcalculated  ELSE 0 END  as salariobasico,
			  	--SALARIO		Salario Trabajador
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SALARIO' THEN pyr_d.amountallocated  ELSE 0 END  as subtotal,
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SALARIO' THEN pyr_d.qtyvalue ELSE 0 END  as cantidad,
			  	--HED		Horas Extras
			  	CASE WHEN cty.sign = 'D' AND cty.Value='HED' THEN pyr_d.amountallocated  ELSE 0 END  as horasextras,
				--OTROS		Otros Ingresos
			  	CASE WHEN cty.sign = 'D' AND cty.Value='OTROS' THEN pyr_d.amountallocated  ELSE 0 END  as otros,
				--COMISADM	Comisión Adm
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISADM' THEN pyr_d.amountallocated  ELSE 0 END  as comisadm,
				--COMISAPH	Comisión aph
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISAPH' THEN pyr_d.amountallocated  ELSE 0 END  as comisaph,
				--COMISPLAZA	Comisión Plaza
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISPLAZA' THEN pyr_d.amountallocated  ELSE 0 END  as comisplaza,
				--REPOSO		Reposos
				CASE WHEN cty.sign = 'D' AND cty.Value='REPOSO' THEN pyr_d.amountallocated  ELSE 0 END  as reposo,
				--OTROSHAB	Otros Haberes
				CASE WHEN cty.sign = 'D' AND cty.Value='OTROSHAB' THEN pyr_d.amountallocated  ELSE 0 END  as otroshaberes,
				--BONIFFAM	Bonificación Familiar
				CASE WHEN cty.sign = 'D' AND cty.Value='BONIFFAM' THEN pyr_d.amountallocated  ELSE 0 END  as boniffam,
				-- OTROS CONCEPTOS DE ASIGNACION
				CASE WHEN  cty.sign = 'D' AND cty.Value NOT IN ('SB','SALARIO', 'HED', 'OTROS', 'COMISADM', 'COMISAPH', 'COMISPLAZA', 'REPOSO', 'OTROSHAB', 'BONIFFAM') THEN pyr_d.amountallocated  ELSE 0 END  as otrosingresos,
				-- *************
				-- DEDUCCIONES
				-- *************
				-- cty.sign = 'C'	THEN
				--IPS_T		IPS Trabajador
				CASE WHEN cty.sign = 'C' AND cty.Value='IPS_T' THEN pyr_d.amountdeducted  ELSE 0 END  as ipstrabajador,
				--IRP		IRP
				CASE WHEN cty.sign = 'C' AND cty.Value='IRP' THEN pyr_d.amountdeducted  ELSE 0 END  as irptrabajador,
				--AUSENCIA	Ausencias
				CASE WHEN cty.sign = 'C' AND cty.Value='AUSENCIA' THEN pyr_d.amountdeducted  ELSE 0 END  as ausencia,
				--OTROSD		Otros Descuentos
				CASE WHEN cty.sign = 'C' AND cty.Value='OTROSD' THEN pyr_d.amountdeducted  ELSE 0 END  as otrosd,
				--EMBARGO		Embargo Juducial
				CASE WHEN cty.sign = 'C' AND cty.Value='EMBARGO' THEN pyr_d.amountdeducted  ELSE 0 END  as embargo,
				-- OTROS CONCEPTOS DE DEDUCCION
				CASE WHEN  cty.sign = 'C' AND cty.Value NOT IN ('IPS_T','IRP', 'AUSENCIA', 'OTROS', 'OTROSD', 'EMBARGO') THEN pyr_d.amountdeducted  ELSE 0 END  as otrosdescuentos,
				-- *************
				-- OTROS CALCULOS
				-- *************
				--IPS_P		IPS Patrón
				CASE WHEN cty.sign = 'C' AND cty.Value='IPS_P' THEN pyr_d.amountcalculated  ELSE 0 END  as ipspatron
				FROM adempiere.amn_payroll as pyr
				LEFT JOIN (
					SELECT DISTINCT ON (pyr.amn_payroll_id) pyr.amn_payroll_id, pyh.salary as salary_hist , pyh.amn_period_yyyymm 
					FROM adempiere.amn_payroll as pyr
					left join AMN_Payroll_Historic pyh on pyr.amn_employee_id = pyh.amn_employee_id  and to_char(pyr.dateacct,'YYYY-MM') = to_char(pyh.validto ,'YYYY-MM')
				) as histo on histo.amn_payroll_id = pyr.amn_payroll_id
				INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
				INNER JOIN (
					SELECT 
					pyr2.amn_payroll_id, pyr_d2.amn_payroll_detail_id, pyr_d2.amn_concept_types_proc_id, pyr_d2.qtyvalue,
					currencyConvert(pyr_d2.amountallocated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountallocated, 
				currencyConvert(pyr_d2.amountdeducted,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountdeducted, 
				currencyConvert(pyr_d2.amountcalculated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountcalculated 
					FROM adempiere.amn_payroll_detail AS pyr_d2
					INNER JOIN adempiere.amn_payroll pyr2 ON pyr2.amn_payroll_id = pyr_d2.amn_payroll_id 
					WHERE CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr2.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END 
				) as pyr_d  ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
			 	LEFT JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
				LEFT JOIN adempiere.amn_concept_types 	as cty 	  ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
				LEFT JOIN adempiere.amn_period   as prd 	 ON (prd.amn_period_id= pyr.amn_period_id)
	 			WHERE pyr.ad_client_id=  $P{AD_Client_ID}  
					AND ( CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END )
			     	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
					AND ( CASE WHEN ( $P{isShowZERO} = 'Y') OR ($P{isShowZERO} = 'N' 
			    	AND (  pyr_d.qtyvalue <> 0 OR pyr_d.amountallocated <> 0 OR pyr_d.amountdeducted<>0  OR pyr_d.amountcalculated<> 0)) THEN 1=1 ELSE 1=0 END ) 
				ORDER BY pyr.amn_payroll_id, cty.calcorder
			) AS resum
			GROUP BY resum.amn_payroll_id --, resum.calcorder, resum.cty_value, resum.concept_type --, resum.salariobasico, resum.subtotal,  resum.horasextras, resum.comisadm, resum.ipspatron
	 )	as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
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
	WHERE org.ad_client_id=  $P{AD_Client_ID}  
	AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR org.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
    AND ( CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Department_ID} IS NULL OR dep.amn_department_id= $P{AMN_Department_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )    
) as nomina
WHERE copiaforma IN ('01','02')
ORDER BY  org_name ASC, value_emp ASC,  documentno, copia DESC
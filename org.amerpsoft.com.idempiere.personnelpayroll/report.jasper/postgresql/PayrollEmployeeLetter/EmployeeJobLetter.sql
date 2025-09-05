-- EmployeeLetter
-- Employee Letter
-- Updated with average from historic
SELECT * FROM (
-- Employee file 
	SELECT DISTINCT
	-- LOGO
	CASE WHEN emp.ad_orgto_id IS NULL THEN img1.binarydata ELSE img2.binarydata END as org_logo,
	INITCAP(coalesce(loc_n.orgname, org.name,org.value,''))  as org_name,
    INITCAP(COALESCE(loc_n.name, org.description, org.name,org.name,''))  org_description, 
	COALESCE(orginfo.taxid,'')  as org_taxid,    
   -- IMAGE
    COALESCE(img3.binarydata, img4.binarydata) as foto,
   -- BUSINESS PARTNER
    cbp.name as nombre, COALESCE(emp.idnumber, cbp.taxid,'') as cedula, 
   -- EMPLOYEE 
	emp.value as codigo,   
	DATE(emp.birthday) as fecha_nacimiento,
	emp.birthplace as lugar_nacimiento,
	emp.incomedate,
	COALESCE(emp.NAME_IDCARD,'') as nombre_tarjeta,
	CASE WHEN emp.sex = 'F' THEN 'la señora' 
		          ELSE 'el señor'
	END as emp_prefix,
	CASE WHEN $P{Salary} IS NULL OR $P{Salary} = 0 THEN adempiere.amf_num2letterLPY(0, 'U', 'es')
    	ELSE adempiere.amf_num2letterLPY($P{Salary}, 'U', 'es')
	END AS salario_letras_param,
	-- SALARIO
	COALESCE(TRUNC(emp.salary), 0) AS salario_ficha,
	CASE WHEN emp.salary IS NULL OR emp.salary = 0 THEN adempiere.amf_num2letterLPY(0,'U','es')
		 ELSE adempiere.amf_num2letterLPY(TRUNC(emp.salary),'U','es')
	END as salario_letras_ficha,
    -- Cálculo del salario base mensual
    salprom.promedio_ajustado as salary_base_mensual,
    -- Salario base mensual en letras
    adempiere.amf_num2letterLPY( salprom.promedio_ajustado, 'U', 'es') AS salary_base_letras,
    salprom.periodos_utilizados,
    -- CONTRACT
	COALESCE(amn_c.name, amn_c.description, '-') as tipo_contrato,
	-- DEPARTMENT 
	COALESCE(adp.name, adp.description, '-') as departamento,
	-- STATION
	COALESCE(job_s.name, job_s.description, '-') as estacion,
	-- JOBTITLE
	COALESCE(job.name, job.description, '-') as puesto_trabajo,
	-- LOCATION (NÓMINA)
	COALESCE(loc_n.name, '-') as localidad_nomina,
	curr2.iso_code,
	CONCAT(curr2.iso_code,'-',currt2.description) as moneda_sol,
	-- LOCATION
	COALESCE(loc.postal, '-') as cod_postal, 
	COALESCE(loc.address1, '-') as adr1, 
	COALESCE(loc.address2, '-') as adr2, 
	COALESCE(loc.address3, '-') as adr3, 
	COALESCE(loc.address4, '-') as adr4,
	-- CITY
	INITCAP(COALESCE(cit.name, '-')) as ciudad_dir,
	-- MUNICIPALITY
	INITCAP(COALESCE(mun.name, '-')) as municio_dir,
	-- PARISH
	INITCAP(COALESCE(par.name, '-')) as parroquia_dir,
	-- REGION
	INITCAP(COALESCE(reg.name, reg.description, '-')) as region_estado,
	 -- COUNTRY DIR
	INITCAP(COALESCE(ctr.name, ctr.description, '-')) as pais_dir,
	-- PHONE
	orginfo.phone,
	orginfo.fax,
	orginfo.email
	FROM adempiere.amn_employee as emp
	INNER JOIN (
		SELECT 
	    $P{AMN_Employee_ID} AS amn_employee_id,
	    ROUND(COALESCE(AVG(salary_utilities), 0), 2) AS promedio_ajustado,
	    COUNT(*) AS periodos_utilizados
		FROM (
		    SELECT salary_utilities
		    FROM AMN_Payroll_Historic
		    WHERE AMN_Employee_ID = $P{AMN_Employee_ID}
		      AND amn_period_yyyymm < TO_CHAR(CAST($P{DateEnd} AS DATE), 'YYYY-MM')
		    ORDER BY amn_period_yyyymm DESC
		    LIMIT 6
		) sub
	) salprom on salprom.amn_employee_id = emp.amn_employee_id
	INNER JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	INNER JOIN adempiere.ad_client as cli ON (emp.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	 LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	INNER JOIN adempiere.ad_org as org ON (emp.ad_orgto_id = org.ad_org_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	 LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	 LEFT JOIN adempiere.ad_image as img3 ON (img3.ad_image_id= emp.empimg1_id) 
	 LEFT JOIN adempiere.ad_image as img4 ON (img4.ad_image_id= emp.empimg2_id) 
	INNER JOIN adempiere.amn_contract as amn_c ON (amn_c.amn_contract_id= emp.amn_contract_id) 
	INNER JOIN adempiere.amn_department as adp ON (adp.amn_department_id= emp.amn_department_id)
	 LEFT JOIN adempiere.amn_jobtitle as job ON (job.amn_jobtitle_id= emp.amn_jobtitle_id)
	 LEFT JOIN adempiere.amn_jobstation as job_s ON (job_s.amn_jobstation_id= job.amn_jobstation_id)
	INNER JOIN adempiere.amn_location as loc_n ON (loc_n.amn_location_id= emp.amn_location_id)
	LEFT JOIN adempiere.c_country as ctr_e ON (ctr_e.c_country_id= emp.c_country_id)
	LEFT JOIN adempiere.c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
    LEFT JOIN adempiere.c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
    LEFT JOIN adempiere.c_location as loc ON (orginfo.c_location_id= loc.c_location_id)
	LEFT JOIN adempiere.c_country as ctr ON (ctr.c_country_id= loc.c_country_id)
	LEFT JOIN adempiere.c_region as reg ON (reg.c_region_id= loc.c_region_id)
    LEFT JOIN adempiere.c_municipality as mun ON (mun.c_municipality_id= loc.c_municipality_id)
	LEFT JOIN adempiere.c_parish as par ON (par.c_parish_id= loc.c_parish_id)
	LEFT JOIN adempiere.c_city as cit ON (cit.c_city_id= loc.c_city_id)
  WHERE emp.amn_employee_id=  $P{AMN_Employee_ID}
) as empleado
WHERE nombre IS NOT NULL
ORDER BY empleado ASC
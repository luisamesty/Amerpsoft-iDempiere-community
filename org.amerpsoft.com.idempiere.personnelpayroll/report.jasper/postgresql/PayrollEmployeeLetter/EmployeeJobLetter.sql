-- EmployeeLetter
-- Employee Letter
SELECT * FROM (
-- Employee file 
	SELECT DISTINCT
	-- LOGO
	img1.binarydata as org_logo,
	INITCAP(coalesce(org.name,org.value,''))  as org_name,
    INITCAP(COALESCE(org.description,org.name,org.name,''))  org_description, 
	COALESCE(orginfo.taxid,'')  as org_taxid,    
   -- IMAGE
    COALESCE(img3.binarydata, img4.binarydata) as foto,
   -- BUSINESS PARTNER
    cbp.name as nombre, COALESCE(cbp.taxid, cbp.amerp_rifseniat,'') as rif, 
   -- EMPLOYEE 
	emp.value as codigo,   
	DATE(emp.birthday) as fecha_nacimiento,
	emp.birthplace as lugar_nacimiento,
	emp.incomedate,
	COALESCE(emp.NAME_IDCARD,'') as nombre_tarjeta,
	CASE WHEN emp.sex = 'F' THEN 'la señora' 
		          ELSE 'el señor'
	END as emp_prefix,
	CASE WHEN $P{Salary} IS NULL  THEN adempiere.amf_num2letterLPY(0,'U','es')
		 ELSE adempiere.amf_num2letterLPY($P{Salary},'U','es')
	END as salario_letras,
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
	CONCAT(curr2.iso_code,'-',currt2.cursymbol,'-',currt2.description) as moneda_sol,
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
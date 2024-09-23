-- Employees.jrxml
-- Employees Report
SELECT DISTINCT *
FROM (SELECT DISTINCT 
-- REPORT HEADER
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
    CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
-- CLIENT
	emp.ad_client_id,
-- ORGANIZATION
	emp.ad_orgto_id,
    coalesce(org.value,'') as org_value,
    coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description,org.name,org.value,'') as org_description, 
-- BPARTNER GROUP
	 COALESCE(bpg.value, bpg.name, bpg.description) as grupo,
-- BPARTNER
	 cbp.name as nombre, COALESCE(cbp.taxid, cbp.amerp_rifseniat) as rif,
-- EMPLOYEE 
    emp.value as codigo,   
    emp.idnumber as cedula,
    DATE(emp.birthday) as fecha_nacimiento,
    CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id = $P{AD_Org_ID} ) THEN 1 ELSE 0 END AS imprimir_org,
    CASE WHEN 
         (DATE_PART('month', current_date::date) <= DATE_PART('month', emp.birthday::date)) AND (DATE_PART('day', current_date::date) <> DATE_PART('day', emp.birthday::date))
         THEN (DATE_PART('year', current_date::date) - DATE_PART('year', emp.birthday::date) - 1)
         WHEN 
         (DATE_PART('month', current_date::date) = DATE_PART('month', emp.birthday::date)) AND (DATE_PART('day', current_date::date) < DATE_PART('day', emp.birthday::date))
         THEN (DATE_PART('year', current_date::date) - DATE_PART('year', emp.birthday::date) - 1)
         ELSE (DATE_PART('year', current_date::date) - DATE_PART('year', emp.birthday::date))
    END as edad, 
    emp.bloodtype as tipo_sangre, emp.bloodrh as tipo_rh, emp.birthplace as lugar_nacimiento, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_sex' AND rlt.ad_language= cli.ad_language and rfl.value= emp.sex) as sexo,
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_civilstatus' AND rlt.ad_language= cli.ad_language and rfl.value= emp.civilstatus) as estado_civil, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_spouse' AND rlt.ad_language= cli.ad_language and rfl.value= emp.spouse) as conyuge,
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_handuse' AND rlt.ad_language= cli.ad_language and rfl.value= emp.handuse) as mano_dominante, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_spouse' AND rlt.ad_language= cli.ad_language and rfl.value= emp.isstudying) as estudia, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_spouse' AND rlt.ad_language= cli.ad_language and rfl.value= emp.ismedicated) as usa_medicamentos,
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_spouse' AND rlt.ad_language= cli.ad_language and rfl.value= emp.uselenses) as lentes, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_educationalgrade' AND rlt.ad_language= cli.ad_language and rfl.value= emp.educationgrade) as grado_instruccion, 
    emp.educationlevel as nivel_instruccion, emp.profession as profesion,
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_jobcondition' AND rlt.ad_language= cli.ad_language and rfl.value= emp.jobcondition) as condicion_trabajo, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_paymenttype' AND rlt.ad_language= cli.ad_language and rfl.value= emp.paymenttype) as tipo_pago, 
    (SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		 WHERE LOWER(rfr.name) = 'amn_payrollmode' AND rlt.ad_language= cli.ad_language and rfl.value= emp.payrollmode) as tipo_nomina, 
    cok.OK_salary, emp.salary as sueldo, emp.incomedate as fecha_ingreso, emp.Birthday as fecha_nacimiento,
    date_part('year',age(current_date, emp.incomedate)) a_servicio, 
    date_part('month',age(current_date, emp.incomedate)) m_servicio, 
    date_part('day',age(current_date, emp.incomedate)) d_servicio,
    emp.increasingloads as carga_asc, emp.downwardloads as carga_desc, 
    emp.hobbyes as hobby, emp.sports as deportes,
    emp.alergic as alergias, emp.deseases as enfermedades,
    emp.height as estatura, emp.weight as peso , emp.sizepant as talla_p, emp.sizeshirt as talla_c, emp.sizeshoe as calzado_no,
 -- COUNTRY EMPLOYEE
    COALESCE(ctr_e.name, ctr_e.description) as pais_emp,
    CASE WHEN ( $P{AMN_Employee_ID} IS NULL or emp.amn_employee_id = $P{AMN_Employee_ID} ) THEN 1 ELSE 0 END AS imprimir_emp,
 -- BPARTNER LOCATION
    cbp_l.c_bpartner_id as cbpl_id, cbp_l.bplname as zona, cbp_l.bplphone as tel1, cbp_l.bplphone2 as tel2, cbp_l.bplfax as fax, 
 -- LOCATION
    loc.postal as cod_postal, loc.address1 as adr1, loc.address2 as adr2, loc.address3 as adr3, loc.address4 as adr4,
 -- CITY
    cit.name as ciudad_dir,
 -- MUNICIPALITY
    mun.name as municio_dir,
 -- PARISH
    par.name as parroquia_dir,
 -- REGION
    COALESCE(reg.name, reg.description) as region_estado,
 -- COUNTRY DIR
	 COALESCE(ctr_e.name, ctr_e.description) as pais_dir,
 -- CONTRACT
 	cok.value as codigo_contrato,
    COALESCE(cok.value, cok.name, cok.description) as tipo_contrato,
    CASE WHEN ( $P{AMN_Contract_ID} IS NULL or cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1 ELSE 0 END AS imprimir_nom,
 -- DEPARTMENT 
 	COALESCE(adp.value, '') as departamento_value,
    COALESCE(adp.name, adp.description) as departamento,
 -- STATION
    COALESCE(job_s.name, job_s.description) as estacion,
 -- JOBTITLE
    COALESCE(job.name, job.description) as puesto_trabajo,
 -- LOCATION (NÓMINA)
    loc_n.amn_location_id AS amn_location_id,
    loc_n.value AS loc_value ,
    COALESCE(loc_n.name, loc_n.description)  AS loc_name,
 -- SECTOR (NOMINA)
    sector.value AS sector_value ,
    COALESCE(sector.name, sector.description) AS sector_name,
 -- DEPENDENT (CORREO E)
    usr.email as correoe
FROM adempiere.amn_employee as emp
INNER JOIN adempiere.c_bpartner as cbp ON cbp.c_bpartner_id = emp.c_bpartner_id 
INNER JOIN adempiere.ad_client as cli ON (cbp.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
 LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
INNER JOIN adempiere.ad_org as org ON (org.ad_org_id = emp.ad_orgto_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
 LEFT JOIN adempiere.ad_user as usr ON (usr.c_bpartner_id= cbp.c_bpartner_id) AND (usr.password!= '')
 LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
 LEFT JOIN adempiere.c_bp_group as bpg ON (bpg.c_bp_group_id= cbp.c_bp_group_id)
 LEFT JOIN ( 
	SELECT DISTINCT ON (c_bpartner_id) 
	c_bpartner_id, c_bpartner_location_id, c_location_id, name as bplname, phone as bplphone, phone2 as bplphone2, fax as bplfax 
	FROM adempiere.c_bpartner_location as cbp_loc WHERE cbp_loc.isbillto='Y'
 ) as cbp_l ON cbp_l.c_bpartner_id  = cbp.c_bpartner_id
 LEFT JOIN adempiere.c_country as ctr_e ON (ctr_e.c_country_id= emp.c_country_id)
 LEFT JOIN adempiere.c_location as loc ON (cbp_l.c_location_id= loc.c_location_id)
 LEFT JOIN adempiere.c_country as ctr ON (ctr.c_country_id= loc.c_country_id)
 LEFT JOIN adempiere.c_region as reg ON (reg.c_region_id= loc.c_region_id)
 LEFT JOIN adempiere.c_municipality as mun ON (mun.c_municipality_id= loc.c_municipality_id)
 LEFT JOIN adempiere.c_parish as par ON (par.c_parish_id= loc.c_parish_id)
 LEFT JOIN adempiere.c_city as cit ON (cit.c_city_id= loc.c_city_id)
 INNER JOIN (
	SELECT DISTINCT con.AMN_Contract_ID, con.value, con.name, con.description, CASE WHEN rol.AMN_Process_ID IS NULL THEN 'N' ELSE 'Y' END as OK_salary
	FROM adempiere.AMN_Contract con
	INNER JOIN (
		SELECT AMN_Process_ID, AMN_Contract_ID, AD_Role_ID FROM adempiere.AMN_Role_Access 
		WHERE  AMN_Process_ID IN (SELECT AMN_Process_ID FROM adempiere.AMN_Process WHERE AMN_Process_Value='NN') AND AD_Role_ID = $P{AD_Role_ID}
	) rol ON rol.AMN_Contract_ID = con.AMN_Contract_ID
 ) cok on cok.AMN_Contract_ID = emp.AMN_Contract_ID
 INNER JOIN adempiere.amn_department as adp ON (adp.amn_department_id= emp.amn_department_id)
 INNER JOIN adempiere.amn_jobtitle as job ON (job.amn_jobtitle_id= emp.amn_jobtitle_id)
 LEFT JOIN adempiere.amn_jobstation as job_s ON (job_s.amn_jobstation_id= job.amn_jobstation_id)
 INNER JOIN adempiere.amn_location as loc_n ON (loc_n.amn_location_id= emp.amn_location_id)
 INNER JOIN adempiere.amn_sector as sector ON sector.amn_sector_id = emp.amn_sector_id
 LEFT JOIN adempiere.amn_dependent as dep ON (dep.amn_employee_id= emp.amn_employee_id)
 WHERE emp.isactive= 'Y' AND
       emp.ad_client_id=  $P{AD_Client_ID} 
       AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
) as trabajadores
WHERE imprimir_nom= 1 AND imprimir_emp= 1
AND imprimir_org=1 AND ad_client_id = $P{AD_Client_ID}
ORDER BY org_value, tipo_contrato, loc_value, sector_value, codigo, nombre ASC

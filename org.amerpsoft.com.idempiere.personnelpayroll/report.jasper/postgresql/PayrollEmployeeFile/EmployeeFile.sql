-- EmployeeFile
-- Employee file PART I
SELECT * FROM (
-- Employee file PART I
	SELECT DISTINCT
	   /*-- ORGANIZATION
	  CASE WHEN P{AD_Org_ID}= 0 THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as org_name,
	  CASE WHEN P{AD_Org_ID}= 0 THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as org_description, 
	  CASE WHEN P{AD_Org_ID}= 0 THEN '' ELSE COALESCE(orginfo.taxid,'') END as org_taxid,
	  CASE WHEN P{AD_Org_ID}= 0 THEN img1.binarydata ELSE img2.binarydata END as org_logo,*/
   -- IMAGE
    COALESCE(img3.binarydata, img4.binarydata) as foto,
   -- BUSINESS PARTNER
    cbp.name as nombre, COALESCE(cbp.taxid, cbp.amerp_rifseniat,'') as rif, 
   -- EMPLOYEE 
		emp.value as codigo,   
		DATE(emp.birthday) as fecha_nacimiento,
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
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_sex' and rfl.value= emp.sex) as sexo,
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_civilstatus' and rfl.value= emp.civilstatus) as estado_civil, 
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_spouse' and rfl.value= emp.spouse) as conyuge,
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_handuse' and rfl.value= emp.handuse) as mano_dominante, 
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_spouse' and rfl.value= emp.isstudying) as estudia, 
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_spouse' and rfl.value= emp.ismedicated) as usa_medicamentos,
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_spouse' and rfl.value= emp.uselenses) as lentes, 
		(SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_educationalgrade' and rfl.value= emp.educationgrade) as grado_instruccion, 
		COALESCE((SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_jobcondition' and rfl.value= emp.jobcondition), '-') as condicion_trabajo, 
		COALESCE((SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_paymenttype' and rfl.value= emp.paymenttype),'-') as tipo_pago, 
		COALESCE((SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_payrollmode' and rfl.value= emp.payrollmode),'-') as tipo_nomina, 
		 COALESCE((SELECT rlt.name
		 FROM adempiere.ad_reference as rfr
		 INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		 INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id AND rlt.ad_language= (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID} ) ) )
		 WHERE rfr.name = 'amn_privateassit' and rfl.value= emp.privateassist),'-') as seguro, 
		COALESCE(emp.salary, 0) as sueldo, COALESCE(emp.incomedate, '01/01/0001') as fecha_ingreso,
		COALESCE(emp.increasingloads, 0) as carga_asc, COALESCE(emp.downwardloads, 0) as carga_desc, 
		COALESCE(emp.hobbyes, '-') as hobby, COALESCE(emp.sports, '-') as deportes,
		COALESCE(emp.alergic, '-') as alergias, COALESCE(emp.deseases, '-') as enfermedades,
		COALESCE(emp.height, 0) as estatura, COALESCE(emp.weight, 0.0) as peso , COALESCE(emp.sizepant, '-') as talla_p, COALESCE(emp.sizeshirt, '-') as talla_c, COALESCE(emp.sizeshoe, '-') as calzado_no,
	 	COALESCE(emp.educationlevel, '-') as nivel_instruccion, COALESCE(emp.profession, '-') as profesion,
 		-- COUNTRY EMPLOYEE
		COALESCE(ctr_e.name, ctr_e.description, '-') as pais_emp,
 		-- BPARTNER LOCATION
		COALESCE(cbp_l.phone, '-') as tel1, COALESCE(cbp_l.phone2, '-') as tel2, COALESCE(cbp_l.fax, '-') as fax, 
 		-- LOCATION
		COALESCE(loc.postal, '-') as cod_postal, 
		COALESCE(loc.address1, '-') as adr1, COALESCE(loc.address2, '-') as adr2, COALESCE(loc.address3, '-') as adr3, COALESCE(loc.address4, '-') as adr4,
 		-- CITY
		COALESCE(cit.name, '-') as ciudad_dir,
 		-- MUNICIPALITY
		COALESCE(mun.name, '-') as municio_dir,
 		-- PARISH
		COALESCE(par.name, '-') as parroquia_dir,
 		-- REGION
		COALESCE(reg.name, reg.description, '-') as region_estado,
 		-- COUNTRY DIR
		COALESCE(ctr.name, ctr.description, '-') as pais_dir,
 		-- CONTRACT
		COALESCE(amn_c.name, amn_c.description, '-') as tipo_contrato,
 		-- DEPARTMENT 
		COALESCE(adp.name, adp.description, '-') as departamento,
 		-- STATION
		COALESCE(job_s.name, job_s.description, '-') as estacion,
 		-- JOBTITLE
		COALESCE(job.name, job.description, '-') as puesto_trabajo,
 		-- LOCATION (NÓMINA)
		COALESCE(loc_n.name, '-') as localidad_nomina
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
	 LEFT JOIN adempiere.c_bpartner_location as cbp_l ON (cbp_l.c_bpartner_id = cbp.c_bpartner_id)
	 LEFT JOIN adempiere.c_location as loc ON (cbp_l.c_location_id= loc.c_location_id)
	 LEFT JOIN adempiere.c_country as ctr ON (ctr.c_country_id= loc.c_country_id)
	 LEFT JOIN adempiere.c_region as reg ON (reg.c_region_id= loc.c_region_id)
     LEFT JOIN adempiere.c_municipality as mun ON (mun.c_municipality_id= loc.c_municipality_id)
	 LEFT JOIN adempiere.c_parish as par ON (par.c_parish_id= loc.c_parish_id)
	 LEFT JOIN adempiere.c_city as cit ON (cit.c_city_id= loc.c_city_id)
  WHERE emp.amn_employee_id=  $P{AMN_Employee_ID}
) as empleado
FULL JOIN 
(
 SELECT
   -- USER
      COALESCE(usr.name, '-') as nombre_contacto, 
      COALESCE(usr.email, '-') as correo_contacto, 
      COALESCE(usr.fax, '-') as fax_contacto,
      COALESCE(usr.phone, '-') as tel_contacto,  usr.phone2 as tel2_contacto
	FROM adempiere.amn_employee as emp
	 INNER JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	  LEFT JOIN adempiere.ad_user as usr ON (usr.c_bpartner_id= cbp.c_bpartner_id) 
	 WHERE emp.amn_employee_id=  $P{AMN_Employee_ID}
) as contactos ON (1 = 0)
  FULL JOIN
  (SELECT 
  -- DEPENDENT TYPE
     COALESCE(dep_t.value, dep_t.name, dep_t.description) as tipo_dependiente,
	-- DEPENDENT
     COALESCE(dep.name,'-') as nombre_dep, COALESCE(dep.birthday,'01-01-0001') as fnac_dep, 
     COALESCE(dep.taxid, '-') as id_dep, COALESCE(dep.phone, '-') as tel_dependiente, dep.phone2 as tel2_dependiente, 
     COALESCE(dep.email, '-') as correo_dep
	FROM adempiere.amn_employee as emp
	 INNER JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	  LEFT JOIN adempiere.amn_dependent as dep ON (dep.amn_employee_id= emp.amn_employee_id)
	 INNER JOIN adempiere.amn_dependent_type as dep_t ON (dep_t.amn_dependent_type_id= dep.amn_dependent_type_id)
	 WHERE emp.amn_employee_id=  $P{AMN_Employee_ID}
  ) as dependientes ON (1 = 0)
  FULL JOIN
  (SELECT
	-- BANK
     COALESCE(bnk.name, '-') as nombre_banco,
	-- BANK ACCOUNT
     COALESCE(cba.accountno, '-') as nro_cuenta,
		 COALESCE((SELECT rlt.name
		  FROM adempiere.ad_reference as rfr
		  INNER JOIN adempiere.ad_ref_list as rfl ON (rfl.ad_reference_id = rfr.ad_reference_id)
		  INNER JOIN adempiere.ad_ref_list_trl as rlt ON (rlt.ad_ref_list_id = rfl.ad_ref_list_id)
		  WHERE rfr.name = 'C_Bank Account Type' AND rlt.ad_language= 'es_VE' and rfl.value= cba.bankaccounttype), '-') as tipo_cuenta,
     COALESCE(cba.a_country,'-') as pais_cuenta
	FROM adempiere.amn_employee as emp
	 INNER JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	  LEFT JOIN adempiere.c_bp_bankaccount as cba ON (cba.c_bpartner_id = cbp.c_bpartner_id) AND (cba.isactive= 'Y')
	 INNER JOIN adempiere.c_bank as bnk ON (bnk.c_bank_id= cba.c_bank_id)
	 WHERE emp.amn_employee_id=  $P{AMN_Employee_ID}
  ) as inf_bancaria ON (1= 0)
WHERE nombre IS NOT NULL
ORDER BY empleado, contactos, dependientes, inf_bancaria ASC
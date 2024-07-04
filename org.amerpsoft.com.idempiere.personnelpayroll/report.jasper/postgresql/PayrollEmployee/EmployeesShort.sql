SELECT *
FROM (
	SELECT 
		a.ad_client_id,
		CASE WHEN $P{AD_Org_ID} = 0 THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
		CASE WHEN $P{AD_Org_ID}= 0 THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
	    CASE WHEN $P{AD_Org_ID}= 0 THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
		CASE WHEN $P{AD_Org_ID}= 0 THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as org_description, 
	-- ORGANIZATION
	    coalesce(org.value,'') as org_value,
	    coalesce(org.name,org.value,'') as org_name,
		COALESCE(org.description,org.name,org.value,'') as org_description, 
	-- EMPLOYEE 
	    a.amn_employee_id,
	    a.value as codigo,   
	    a.salary as sueldo, a.incomedate as fecha_ingreso, a.Birthday as fecha_nacimiento,
	    a.status, 
	    CASE WHEN ($P{AMN_Status_A} = 'Y' AND a.status='A') THEN 1
	         WHEN ($P{AMN_Status_V} = 'Y' AND a.status='V') THEN 1
	         WHEN ($P{AMN_Status_R} = 'Y' AND a.status='R') THEN 1
	         WHEN ($P{AMN_Status_S} = 'Y' AND a.status='S') THEN 1
	         ELSE 0 END 
	    AS imprimir_status,
	    CASE WHEN a.jobcondition='C' THEN 'Contratado'
	         WHEN a.jobcondition='F' THEN 'Fijo'
	         WHEN a.jobcondition='I' THEN 'Independiente'
	         WHEN a.jobcondition='P' THEN 'Parcial'
	         ELSE 'Indefinido' END 
	     as contrato_condicion,
		CASE WHEN ( $P{AD_Org_ID} IS NULL OR a.ad_org_id = $P{AD_Org_ID} ) THEN 1 ELSE 0 END AS imprimir_org,
		-- CURRENCY
		curr1.iso_code as iso_code1,
		currt1.cursymbol as cursymbol1,
		COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
		curr2.iso_code as iso_code2,
		currt2.cursymbol as cursymbol2,
		COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2,	     
	    COALESCE(prh.salary,a.salary) as salario,
	    prh.validto as salario_fecha,
	    cok.OK_salary,
	-- BUSINESS PARTNER
		 COALESCE(b.name, a.name) as nombre, COALESCE(b.taxid, b.amerp_rifseniat) as rif,
	-- EMPLOYEE
	    CASE WHEN ( $P{AMN_Employee_ID} IS NULL OR a.amn_employee_id = $P{AMN_Employee_ID} ) THEN 1 ELSE 0 END AS imprimir_emp,
	-- CONTRACT
	    c.value as contrato_tipo,
	    COALESCE(c.name, c.description) as contrato_nombre,
	    CASE WHEN ( $P{AMN_Contract_ID} IS NULL OR c.amn_contract_id = $P{AMN_Contract_ID}) THEN 1 ELSE 0 END AS imprimir_con,
	-- DEPARTMENT 
	    CASE WHEN ( $P{AMN_Department_ID} IS NULL or d.amn_department_id = $P{AMN_Department_ID}) THEN 1 ELSE 0 END AS imprimir_dep,
	    CASE WHEN ($P{AMN_Department_ID} IS NULL AND $P{ShowDepartment} = 'N' ) THEN 0
	         ELSE d.amn_department_id 
	         END AS amn_department_id,
	     CASE WHEN ($P{AMN_Department_ID} IS NULL AND $P{ShowDepartment} = 'N' ) THEN 'XX'
	          ELSE d.value  
	          END AS departamento_codigo ,
	     CASE WHEN ($P{AMN_Department_ID} IS NULL AND $P{ShowDepartment} = 'N' ) THEN '** Todas los Departamentos **'
	          ELSE  COALESCE(d.name, d.description)
	          END AS departamento_nombre ,
	-- JobTitle
		j.value as jobtitle_value, 	j.name as jobtitle_name,	j.description as jobtitle_description,
	-- LOCATION
	    CASE WHEN ( $P{AMN_Location_ID} IS NULL or l.amn_location_id = $P{AMN_Location_ID}) THEN 1 ELSE 0 END AS imprimir_loc,
	    CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{ShowLocation} = 'N' ) THEN 0
	         ELSE l.amn_location_id 
	         END AS amn_location_id,
	     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{ShowLocation} = 'N' ) THEN 'XX'
	          ELSE l.value  
	          END AS localidad_codigo ,
	     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{ShowLocation} = 'N' ) THEN '** Todas las Localidades **'
	          ELSE COALESCE(l.name, l.description)
	          END AS localidad_nombre 
	FROM AMN_Employee a
	LEFT OUTER JOIN C_BPartner b ON (b.C_BPartner_ID=a.C_BPartner_ID  AND a.IsActive='Y')
	LEFT OUTER JOIN AMN_Contract c ON (c.AMN_contract_ID=a.AMN_Contract_ID)
	LEFT JOIN (
		SELECT DISTINCT con.AMN_Contract_ID, CASE WHEN rol.AMN_Process_ID IS NULL THEN 'N' ELSE 'Y' END as OK_salary
		FROM AMN_Contract con
		LEFT JOIN (
			SELECT AMN_Process_ID, AMN_Contract_ID, AD_Role_ID FROM AMN_Role_Access 
			WHERE  AMN_Process_ID IN (SELECT AMN_Process_ID FROM AMN_Process WHERE AMN_Process_Value='NN') AND AD_Role_ID = $P{AD_Role_ID}
		) rol ON rol.AMN_Contract_ID = con.AMN_Contract_ID
	) cok on cok.AMN_Contract_ID = c.AMN_Contract_ID
	LEFT OUTER JOIN AMN_Department d ON (d.AMN_Department_ID=a.AMN_Department_ID)
	LEFT OUTER JOIN AMN_Jobtitle j ON(j.AMN_Jobtitle_ID = a.AMN_Jobtitle_ID)
	LEFT OUTER JOIN AMN_Shift s ON(s.AMN_Shift_ID = a.AMN_Shift_ID)
	LEFT OUTER JOIN AMN_Location l ON(l.AMN_Location_ID = a.AMN_Location_ID)
	INNER JOIN adempiere.ad_client as cli ON (a.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	INNER JOIN adempiere.ad_org as org ON (a.ad_orgto_id = org.ad_org_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	-- SALARY HISTORIC MAX VAlidTo
	LEFT JOIN  (
		SELECT DISTINCT ON (AMN_Employee_ID)
		AMN_Employee_ID, MAX(ValidTo) as ValidTo, MAX(Salary) as Salary, C_Currency_ID
		FROM
		AMN_Payroll_Historic
		WHERE C_Currency_ID=$P{C_Currency_ID}
		GROUP BY AMN_Employee_ID, C_Currency_ID
		) prh ON ( prh.AMN_Employee_ID = a.AMN_Employee_ID )
	 LEFT JOIN c_currency curr1 on prh.c_currency_id = curr1.c_currency_id
     LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
     LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
     LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE a.isActive='Y' AND b.isActive='Y'
) trab
WHERE trab.imprimir_status=1 AND trab.imprimir_con=1 AND trab.imprimir_dep =1 
	AND trab.imprimir_loc=1 AND trab.imprimir_org = 1 AND trab.ad_client_id = $P{AD_Client_ID}
ORDER BY trab.localidad_codigo, trab.departamento_codigo, trab.codigo
 
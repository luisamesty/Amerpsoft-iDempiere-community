-- EmployeesShort.jrxml
-- Employees Selective
SELECT *
FROM (
	SELECT 
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
	-- EMPLOYEE 
	    emp.amn_employee_id,
	    emp.value as codigo,  
        emp.idnumber as cedula,
	    emp.salary as sueldo, emp.incomedate as fecha_ingreso, emp.Birthday as fecha_nacimiento,
        date_part('year',age(current_date, emp.incomedate)) a_servicio, 
        date_part('month',age(current_date, emp.incomedate)) m_servicio, 
        date_part('day',age(current_date, emp.incomedate)) d_servicio,
	    emp.status, 
	    CASE WHEN ($P{AMN_Status_A} = 'Y' AND emp.status='A') THEN 1
	         WHEN ($P{AMN_Status_V} = 'Y' AND emp.status='V') THEN 1
	         WHEN ($P{AMN_Status_R} = 'Y' AND emp.status='R') THEN 1
	         WHEN ($P{AMN_Status_S} = 'Y' AND emp.status='S') THEN 1
	         ELSE 0 END 
	    AS imprimir_status,
	    CASE WHEN emp.jobcondition='C' THEN 'Contratado'
	         WHEN emp.jobcondition='F' THEN 'Fijo'
	         WHEN emp.jobcondition='I' THEN 'Independiente'
	         WHEN emp.jobcondition='P' THEN 'Parcial'
	         ELSE 'Indefinido' END 
	     as contrato_condicion,
		CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_org_id = $P{AD_Org_ID} ) THEN 1 ELSE 0 END AS imprimir_org,
		-- CURRENCY
		curr1.iso_code as iso_code1,
		currt1.cursymbol as cursymbol1,
		COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
		curr2.iso_code as iso_code2,
		currt2.cursymbol as cursymbol2,
		COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2,	     
	    COALESCE(prh.salary,emp.salary) as salario,
	    prh.validto as salario_fecha,
	    cok.OK_salary,
	-- BUSINESS PARTNER
		 COALESCE(cbp.name, emp.name) as nombre, COALESCE(cbp.taxid, cbp.amerp_rifseniat) as rif,
	-- EMPLOYEE
	    CASE WHEN ( $P{AMN_Employee_ID} IS NULL OR emp.amn_employee_id = $P{AMN_Employee_ID} ) THEN 1 ELSE 0 END AS imprimir_emp,
	-- CONTRACT
	    cok.value as contrato_tipo,
    	COALESCE(cok.value, cok.name, cok.description) as contrato_nombre,
	    CASE WHEN ( $P{AMN_Contract_ID} IS NULL OR cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1 ELSE 0 END AS imprimir_con,
	-- CONTRACT
    	CASE WHEN ( $P{AMN_Contract_ID} IS NULL or cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1 ELSE 0 END AS imprimir_nom,	    
	-- JobTitle
		j.value as jobtitle_value, 	j.name as jobtitle_name,	j.description as jobtitle_description,
	-- LOCATION
	    CASE WHEN ( $P{AMN_Location_ID} IS NULL or l.amn_location_id = $P{AMN_Location_ID}) THEN 1 ELSE 0 END AS imprimir_loc,
	    CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN 0
	         ELSE l.amn_location_id 
	         END AS amn_location_id,
	     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN 'XX'
	          ELSE l.value  
	          END AS localidad_codigo ,
	     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN '** Todas las Localidades **'
	          ELSE COALESCE(l.name, l.description)
	          END AS localidad_nombre 
	FROM AMN_Employee emp
	LEFT OUTER JOIN C_BPartner cbp ON (cbp.C_BPartner_ID=emp.C_BPartner_ID  AND emp.IsActive='Y')
	INNER JOIN (
		SELECT DISTINCT con.AMN_Contract_ID, con.value, con.name, con.description, CASE WHEN rol.AMN_Process_ID IS NULL THEN 'N' ELSE 'Y' END as OK_salary
		FROM adempiere.AMN_Contract con
		INNER JOIN (
			SELECT AMN_Process_ID, AMN_Contract_ID, AD_Role_ID FROM adempiere.AMN_Role_Access 
			WHERE  AMN_Process_ID IN (SELECT AMN_Process_ID FROM adempiere.AMN_Process WHERE AMN_Process_Value='NN') AND AD_Role_ID = $P{AD_Role_ID}
		) rol ON rol.AMN_Contract_ID = con.AMN_Contract_ID	
	) cok on cok.AMN_Contract_ID = emp.AMN_Contract_ID
	LEFT OUTER JOIN AMN_Department d ON (d.AMN_Department_ID=emp.AMN_Department_ID)
	LEFT OUTER JOIN AMN_Jobtitle j ON(j.AMN_Jobtitle_ID = emp.AMN_Jobtitle_ID)
	LEFT OUTER JOIN AMN_Shift s ON(s.AMN_Shift_ID = emp.AMN_Shift_ID)
	LEFT OUTER JOIN AMN_Location l ON(l.AMN_Location_ID = emp.AMN_Location_ID)
	INNER JOIN adempiere.ad_client as cli ON (emp.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	INNER JOIN adempiere.ad_org as org ON (emp.ad_orgto_id = org.ad_org_id)
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
		) prh ON ( prh.AMN_Employee_ID = emp.AMN_Employee_ID )
	 LEFT JOIN c_currency curr1 on prh.c_currency_id = curr1.c_currency_id
     LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
     LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
     LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE emp.isactive= 'Y' AND
       emp.ad_client_id=  $P{AD_Client_ID} 
       AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
) trab
WHERE trab.imprimir_status=1 AND trab.imprimir_con=1
	AND trab.imprimir_loc=1 AND trab.imprimir_org = 1
ORDER BY  org_value, contrato_tipo, trab.localidad_codigo, trab.codigo
 
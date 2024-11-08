-- EmployeesReview.jrxml
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
	emp.value as emp_value, 
	emp.idnumber AS cedula,
	CONCAT(COALESCE(emp.lastname1,''),' ',COALESCE(emp.lastname2,'')) AS emplastname,
	CONCAT(COALESCE(emp.firstname1,''),' ',COALESCE(emp.firstname2,'')) AS empfirstname,
	emp.name as emp_name, 
	emp.status, emp.incomedate as fecha_ingreso, emp.Birthday as fecha_nacimiento,
	CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_org_id = $P{AD_Org_ID} ) THEN 1 ELSE 0 END AS imprimir_org,
	employ1.accountno AS cuenta_pagadora,
	employ2.accountno AS cuenta_acreedora,
	-- CONTRACT
    cok.value as contrato_tipo,
   	COALESCE(cok.value, cok.name, cok.description) as contrato_nombre,
    CASE WHEN ( $P{AMN_Contract_ID} IS NULL OR cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1 ELSE 0 END AS imprimir_con,
	-- CONTRACT
   	CASE WHEN ( $P{AMN_Contract_ID} IS NULL or cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1 ELSE 0 END AS imprimir_nom,	    
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
	LEFT JOIN (
		SELECT DISTINCT ON (emp1.amn_employee_id)
			emp1.amn_employee_id,cbb1.a_name, cbb1.accountno
			FROM adempiere.amn_employee emp1
			LEFT JOIN (
				SELECT ad_client_id, c_bpartner_id, a_name, accountno FROM c_bp_bankaccount  WHERE bpbankacctuse ='N'
			)  cbb1 ON cbb1.C_BPartner_ID = emp1.C_BPartner_ID
			WHERE emp1.AD_Client_ID=$P{AD_Client_ID}
	) AS employ1 ON employ1.amn_employee_id = emp.amn_employee_id 
	LEFT JOIN (
		SELECT DISTINCT ON (emp2.amn_employee_id)
			emp2.amn_employee_id,cbb2.a_name, cbb2.accountno
			FROM adempiere.amn_employee emp2
			LEFT JOIN (
				SELECT ad_client_id, c_bpartner_id, a_name, accountno FROM c_bp_bankaccount  WHERE bpbankacctuse ='B'
			)  cbb2 ON cbb2.C_BPartner_ID = emp2.C_BPartner_ID
			WHERE emp2.AD_Client_ID=$P{AD_Client_ID}
	) AS employ2 ON employ2.amn_employee_id = emp.amn_employee_id 	
	WHERE emp.isactive= 'Y' AND
       emp.ad_client_id=  $P{AD_Client_ID} 
       AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
) trab
WHERE trab.imprimir_con=1
	AND trab.imprimir_loc=1 AND trab.imprimir_org = 1
ORDER BY  org_value, contrato_tipo, trab.localidad_codigo, trab.emp_value
 
-- Employees Parents
-- EmployeesParents.jrxml
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
    emp.name as nombre_emp,  
    emp.idnumber as cedula,
    emp.incomedate as fecha_ingreso, 
    emp.Birthday as fecha_nacimiento,
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
	-- JobTitle
	COALESCE(j.name,j.value) as jobtitle,
	-- LOCATION
	COALESCE(l.description, l.name, l.value ) localidad_nombre,
	-- Mes y nombre del mes en el idioma del cliente
	TO_CHAR(emp.Birthday, 'MM') AS mes_valor,
	TO_CHAR(emp.Birthday, 'FMMonth') AS mes_nombre,
	-- Dependents
	adp.taxid AS dependent_id,
	adp.name as dependent_name,
	adp.phone as dependent_phone,
	adp.phone2 as dependent_phone2,
	adp.email as dependent_email,
	adp.birthday AS dependent_birthday,
	adp.sex AS dependent_sex,
	-- Dependent Type
	adt.value AS dependent_type_value,
	adt.name AS dependent_type_name
FROM AMN_Employee emp
INNER JOIN AMN_Dependent adp ON adp.AMN_Employee_ID = emp.AMN_Employee_ID
INNER JOIN AMN_Dependent_type adt ON adt.AMN_Dependent_type_ID = adp.AMN_Dependent_type_ID
LEFT OUTER JOIN AMN_Location l ON(l.AMN_Location_ID = emp.AMN_Location_ID)
LEFT OUTER JOIN AMN_Jobtitle j ON(j.AMN_Jobtitle_ID = emp.AMN_Jobtitle_ID)
INNER JOIN adempiere.ad_client as cli ON (emp.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
INNER JOIN adempiere.ad_org as org ON (emp.ad_orgto_id = org.ad_org_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
WHERE emp.isactive= 'Y' 
	AND  emp.ad_client_id =  $P{AD_Client_ID} 
	AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR l.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Dependent_Type_ID} IS NULL OR adp.amn_dependent_type_id = $P{AMN_Dependent_Type_ID} ) THEN 1=1 ELSE 1=0 END )
ORDER BY localidad_nombre, codigo
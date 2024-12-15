-- PayrollEmployeeLeaves
-- Payroll Employee Leaves
SELECT * FROM
	(
		-- REPORT HEADER
		SELECT DISTINCT ON (cli.ad_client_id)
			-- Client
			cli.ad_client_id as rep_client_id,
			-- Organization
			CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 0 ELSE $P{AD_Org_ID} END as rep_org_id,
			CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
			CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as rep_description, 
			CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
			CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
		    CASE WHEN  org.ad_client_id = $P{AD_Client_ID}   AND ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 1
		             WHEN  org.ad_client_id = $P{AD_Client_ID}  AND org.ad_org_id= $P{AD_Org_ID}  THEN 1
		             ELSE 0 END as imp_header
		FROM adempiere.ad_client as cli
			 INNER JOIN adempiere.ad_org as org ON (org.ad_client_id = cli.ad_client_id)
			 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
			  LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
			 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
			  LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
		WHERE cli.ad_client_id = $P{AD_Client_ID} AND CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR org.ad_org_id = $P{AD_Org_ID} ) THEN 1 = 1 ELSE 1 = 0 END
	) as header_info
	FULL JOIN
	( SELECT DISTINCT
		-- ORG 
	    coalesce(org.value,'') as org_value,
		coalesce(org.name,org.value,'') as org_name,
		-- CONTRACT
	   	amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
		-- DEPARTAMENT
	   	COALESCE(dep.name,dep.description) as departamento,
	   	-- LOCATION
	   	emp.amn_location_id, lct.value AS location_value, lct.name AS location_name,
		-- EMPLOYEE
	   	emp.amn_employee_id,
	  	emp.value as emp_value, 
	  	emp.name as emp_name, 
	  	emp.incomedate as fecha_ingreso, emp.paymenttype,
	   	COALESCE(jtt.name, jtt.description,'N/D') as cargo, 
	   	COALESCE(emp.idnumber ,'') as cedula, 
		-- AMN_leaves_types	
		amlt.amn_leaves_types_id ,
		amlt.value AS leaves_value,
		amlt.name AS leaves_name,
		-- AMN_Leaves
		aml.amn_leaves_id, 
		aml.ad_client_id, 
		aml.ad_org_id, 
		aml.name AS leaves_name,
		aml.description AS leaves_description, 
		aml.docstatus, processed, 
		aml.processedon, processing, 
		aml.amn_leaves_types_id, 
		aml.datefrom, dateto, 
		aml.amn_employee_id, 
		aml.datedoc, 
		aml.documentno
	FROM adempiere.amn_leaves aml
	INNER JOIN adempiere.amn_leaves_types amlt ON amlt.amn_leaves_types_id = aml.amn_leaves_types_id
	INNER JOIN adempiere.amn_employee as emp ON (emp.amn_employee_id= aml.amn_employee_id)
	INNER JOIN adempiere.amn_department as dep ON (emp.amn_department_id = dep.amn_department_id)
	LEFT JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
	INNER JOIN adempiere.amn_location as lct 	 ON (lct.amn_location_id= emp.amn_location_id)
	INNER JOIN adempiere.amn_contract as amc 	 ON (amc.amn_contract_id= emp.amn_contract_id)	 
	LEFT JOIN adempiere.ad_org   as org ON (org.ad_org_id = emp.ad_orgto_id)
	WHERE aml.isActive = 'Y' AND aml.AD_Client_ID= $P{AD_Client_ID} 
		AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR emp.ad_orgto_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
		AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
		AND ( CASE WHEN (aml.datefrom BETWEEN DATE( $P{DateIni} ) AND DATE( $P{DateEnd})) THEN 1=1 ELSE 1=0 END )
	ORDER BY aml.amn_leaves_types_id
) as leaves ON (1= 0)
WHERE (imp_header = 1) OR 
	(leaves.ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR leaves.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY  leaves.c_value, leaves.emp_value, leaves.datefrom, leaves.leaves_value, header_info ASC

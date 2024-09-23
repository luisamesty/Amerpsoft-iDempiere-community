-- PayrollEmployeeLeaves
-- Payroll Employee Leaves
SELECT 
	-- CONTRACT
   	amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
	-- DEPARTAMENT
   	COALESCE(dep.name,dep.description) as departamento,
   	-- LOCATION
   	emp.amn_location_id, lct.value AS location_value, lct.name AS location_name,
	-- EMPLOYEE
   	emp.amn_employee_id,
  	emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso, emp.paymenttype,
   	COALESCE(jtt.name, jtt.description,'') as cargo, 
   	COALESCE(emp.idnumber ,'') as cedula, 
	-- AMN_leaves_types	
	amlt.amn_leaves_types_id ,
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
INNER JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
INNER JOIN adempiere.amn_location as lct 	 ON (lct.amn_location_id= emp.amn_location_id)
INNER JOIN adempiere.amn_contract as amc 	 ON (amc.amn_contract_id= emp.amn_contract_id)	 
WHERE aml.isActive = 'Y' AND aml.AD_Client_ID= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR emp.ad_orgto_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
	AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
ORDER BY aml.amn_leaves_types_id
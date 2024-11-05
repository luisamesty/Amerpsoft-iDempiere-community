-- PayrollCommission.jrxml
-- Commission Report based on AMN_CommisssionGroup Table and a given Concept
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
	( SELECT 
		-- AD_Client 
		emp.ad_client_id,
		-- AD_org_id
		emp.ad_org_id,
		-- AMN_Commission
		ac.value AS comm_value,
		-- AMN_Contract
		amc.value AS contract_value,
		-- AMN_Employee
		emp.amn_employee_id,
		emp.value AS emp_value,
		emp.name AS emp_name,
		-- AMN_Concept_Types
		COALESCE(emprec.concepto,'') AS concepto,
		COALESCE(emprec.calcorder,0) AS calcorder,
		-- AMN_Payroll with Commission
		COALESCE(emprec.documentno,'') AS documentno,
		COALESCE(emprec.AMN_Payroll_ID,0) AS amn_payroll_id,
		COALESCE(emprec.comm_value_rec,'N/D') AS comm_value_rec,
		COALESCE(emprec.qtyvalue,0) AS qtyvalue
		FROM amn_employee emp 
		INNER JOIN amn_commissiongroup ac ON ac.amn_commissiongroup_id = emp.amn_commissiongroup_id 
		INNER JOIN amn_contract amc ON amc.amn_contract_id = emp.amn_contract_id 
		LEFT JOIN (
			-- TRABAJADORES CON CONCEPTOS DE COMMISSION EN EL RECIBO
			SELECT 
				emp2.amn_employee_id,
				pyr2.AMN_Payroll_ID , 
				pyr2.documentno,
				ac2.value AS comm_value_rec,
				pyr2_d.qtyvalue,
				ct.value AS concepto,
				ct.calcorder AS calcorder
			FROM amn_employee emp2 
			INNER JOIN amn_commissiongroup ac2 ON ac2.amn_commissiongroup_id = emp2.amn_commissiongroup_id 
			INNER JOIN amn_payroll pyr2 ON emp2.amn_employee_id = pyr2.amn_employee_id 
			INNER JOIN amn_payroll_detail pyr2_d ON pyr2_d.amn_payroll_id = pyr2.amn_payroll_id 
			INNER JOIN (
				SELECT DISTINCT ap.amn_period_id, ap.amndateini, ap.amndateend, amnct.value, amnct.amn_concept_types_id
				FROM amn_period ap 
				INNER JOIN amn_contract cnt ON cnt.amn_contract_id = ap.amn_contract_id 
				INNER JOIN amn_concept_types_limit amctl ON amctl.ad_client_id = ap.ad_client_id 
				INNER JOIN amn_concept_types amnct ON amnct.amn_concept_types_id = amctl.amn_concept_types_id
				WHERE ap.amndateini >= amctl.validfrom AND ap.amndateend <= amctl.validto
					AND ap.amn_contract_id IN (
						SELECT AMN_Contract_ID 	FROM amn_concept_types_contract	WHERE AMN_Concept_Types_ID= $P{AMN_Concept_Types_ID}
					)
					AND ap.amn_process_id IN (
						SELECT AMN_Process_ID FROM amn_concept_types_proc WHERE AMN_Concept_Types_ID=$P{AMN_Concept_Types_ID}
					)
					AND amctl.amn_concept_types_limit_id = (
						SELECT DISTINCT AMN_Concept_Types_Limit_ID 
						FROM amn_concept_types_limit actl 
						WHERE actl.validfrom BETWEEN DATE( $P{DateIni} ) AND DATE( $P{DateEnd} ) 
				)
			) AS periodo ON periodo.amn_period_id = pyr2.amn_period_id
			INNER JOIN amn_concept_types_proc ctp ON ctp.amn_concept_types_proc_id = pyr2_d.amn_concept_types_proc_id
			INNER JOIN amn_concept_types ct ON ct.amn_concept_types_id =ctp.amn_concept_types_id AND ct.amn_concept_types_id = $P{AMN_Concept_Types_ID}
		) emprec ON emprec.amn_employee_id = emp.amn_employee_id
	) as commission ON (1= 0)
WHERE (imp_header = 1) OR 
	(commission.ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR commission.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY commission.comm_value, commission.contract_value, commission.emp_value, header_info ASC


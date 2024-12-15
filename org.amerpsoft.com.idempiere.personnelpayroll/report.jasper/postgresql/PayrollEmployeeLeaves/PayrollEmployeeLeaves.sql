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
	(
	SELECT 
		LTALL.ad_client_id, 
		org.ad_org_id, org.value AS emp_org_value, org.name AS emp_org_name,
		-- EMPLOYEE
		LTALL.amn_employee_id, 
		emp2.value as emp_value, 
	  	emp2.name as emp_name, 
		-- CONTRACT
		amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
		-- LOCATION
		emp2.amn_location_id, lct.value AS location_value, lct.name AS location_name,
		-- Leaves Types
		AJ,
		CO,
		DU,
		DL,
		MA,
		MT,
		LA,
		RM,
		PA,
		PR,
		RM,
		SU,
		VA,
		PS,
		PE,
		RE,
		LI
	FROM (
		SELECT 
			ad_client_id, ad_org_id,
			amn_employee_id, 
			MAX(AJ) AS AJ,
			MAX(CO) AS CO,
			MAX(DU) AS DU,
			MAX(DL) AS DL,
			MAX(MA) AS MA,
			MAX(MT) AS MT,
			MAX(LA) AS LA,
			MAX(RM) AS RM,
			MAX(PA) AS PA,
			MAX(PR) AS PR,
			MAX(SU) AS SU,
			MAX(VA) AS VA,
			MAX(PS) AS PS,
			MAX(PE) AS PE,
			MAX(RE) AS RE,
			MAX(LI) AS LI
		FROM (
			SELECT 
			ad_client_id, ad_org_id,
			amn_employee_id, 
			CASE WHEN leaves_value = 'AJ' THEN qty ELSE 0 END AS AJ,
			CASE WHEN leaves_value = 'CO' THEN qty ELSE 0 END AS CO,
			CASE WHEN leaves_value = 'DU' THEN qty ELSE 0 END AS DU,
			CASE WHEN leaves_value = 'DL' THEN qty ELSE 0 END AS DL,
			CASE WHEN leaves_value = 'MA' THEN qty ELSE 0 END AS MA,
			CASE WHEN leaves_value = 'MT' THEN qty ELSE 0 END AS MT,
			CASE WHEN leaves_value = 'LA' THEN qty ELSE 0 END AS LA,
			CASE WHEN leaves_value = 'RM' THEN qty ELSE 0 END AS RM,
			CASE WHEN leaves_value = 'PA' THEN qty ELSE 0 END AS PA,
			CASE WHEN leaves_value = 'PR' THEN qty ELSE 0 END AS PR,
			CASE WHEN leaves_value = 'SU' THEN qty ELSE 0 END AS SU,
			CASE WHEN leaves_value = 'VA' THEN qty ELSE 0 END AS VA,
			CASE WHEN leaves_value = 'PS' THEN qty ELSE 0 END AS PS,
			CASE WHEN leaves_value = 'PE' THEN qty ELSE 0 END AS PE,
			CASE WHEN leaves_value = 'RE' THEN qty ELSE 0 END AS RE,
			CASE WHEN leaves_value = 'LI' THEN qty ELSE 0 END AS LI
			FROM (
			 	SELECT
			 		-- EMPLOYEE
				   	emp.amn_employee_id,
					-- AMN_leaves_types	
					amlt.amn_leaves_types_id,
					amlt.value AS leaves_value,
					amltt.name AS leaves_name,
					-- AMN_Leaves
					aml.amn_leaves_id, 
					aml.ad_client_id, 
					aml.ad_org_id, 
					aml.description AS leaves_description, 
					aml.docstatus, aml.processed, 
					aml.processedon, aml.processing, 
					aml.datefrom, dateto, 
					aml.datedoc, 
					aml.documentno,
					-- Leaves Count
					COALESCE(SUM(aml.daysto),1) AS qty,
					COALESCE(SUM(aml.hoursday),0) AS qtyhours
				FROM adempiere.amn_employee as emp
				LEFT JOIN adempiere.amn_leaves aml ON (aml.amn_employee_id = emp.amn_employee_id)
				INNER JOIN adempiere.amn_leaves_types amlt ON amlt.amn_leaves_types_id = aml.amn_leaves_types_id
				LEFT JOIN adempiere.amn_leaves_types_trl amltt on amltt.amn_leaves_types_id = amlt.amn_leaves_types_id  
				 AND amltt.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =$P{AD_Client_ID}) 
				LEFT JOIN adempiere.ad_org   as org ON (org.ad_org_id = emp.ad_orgto_id)
				WHERE emp.isActive = 'Y' 
					--AND aml.docstatus IN ('CO','CL') 
					AND aml.AD_Client_ID= $P{AD_Client_ID} 
					AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR emp.ad_orgto_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
					AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
					AND ( CASE WHEN (aml.datefrom BETWEEN DATE( $P{DateIni} ) AND DATE( $P{DateEnd})) THEN 1=1 ELSE 1=0 END )
				GROUP BY emp.amn_employee_id, aml.amn_leaves_types_id, amlt.amn_leaves_types_id, emp.value, emp.name,
					aml.amn_leaves_id, aml.ad_client_id, aml.ad_org_id, 
					amltt.name, aml.name, aml.description, aml.docstatus, aml.processed, aml.processedon, aml.processing, 
					aml.datefrom, dateto, aml.amn_employee_id, aml.datedoc, aml.documentno
				ORDER BY emp.amn_employee_id, aml.amn_leaves_types_id
			) AS LT1
		) AS LT2
		GROUP BY ad_client_id, ad_org_id, amn_employee_id
	) AS LTALL
	INNER JOIN adempiere.amn_employee as emp2 ON emp2.amn_employee_id = LTALL.amn_employee_id
	LEFT JOIN adempiere.amn_jobtitle as jtt ON (emp2.amn_jobtitle_id= jtt.amn_jobtitle_id)
	INNER JOIN adempiere.amn_location as lct 	 ON (lct.amn_location_id= emp2.amn_location_id)
	INNER JOIN adempiere.amn_contract as amc 	 ON (amc.amn_contract_id= emp2.amn_contract_id)	 
	LEFT JOIN adempiere.ad_org   as org ON (org.ad_org_id = emp2.ad_orgto_id)
	WHERE emp2.isActive = 'Y' AND emp2.AD_Client_ID= $P{AD_Client_ID} 
		AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR emp2.ad_orgto_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
		AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp2.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
	) as leaves ON (1= 0)
WHERE (imp_header = 1) OR 
	(leaves.ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR leaves.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY  leaves.emp_org_value, leaves.c_value, leaves.location_value, leaves.emp_value, header_info ASC

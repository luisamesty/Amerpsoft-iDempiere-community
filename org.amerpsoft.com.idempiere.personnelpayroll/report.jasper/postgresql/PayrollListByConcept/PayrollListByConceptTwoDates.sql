-- PayrollListByContractsConcepts
-- Payroll List By Concept in a Period Between two dates
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
	SELECT DISTINCT
	 -- ORGANIZACIÓN
	 pyr.ad_client_id as client_id, pyr.ad_org_id as org_id,
	 -- LOCATION
     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{ShowLocation} = 'N' ) THEN 0
             ELSE lct.amn_location_id 
     END AS amn_location_id,
     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{ShowLocation} = 'N' ) THEN 'XX'
              ELSE lct.value  
     END AS loc_value ,
     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{ShowLocation} = 'N' ) THEN '** Todas las Localidades **'
              ELSE COALESCE(lct.name, lct.description)
     END AS localidad , 
	 --COALESCE(lct.name, lct.description) as localidad,
     CASE WHEN ( $P{AMN_Location_ID}  IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1 ELSE 0 END AS imp_localidad,
	 
     -- PERIOD
	 prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
     CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1 ELSE 0 END AS imp_periodo,
     -- TIPO DE CONCEPTO
	 cty.amn_concept_types_id, cty.calcorder, cty.isshow, cty.value as cty_value, COALESCE(cty.name, cty.description) as concept_type,
     -- TIPO DE CONCEPTO (PROCESO)
	 ctp.value as ctp_value, COALESCE(ctp.name, ctp.description) as concept_type_process, 	 
	 -- PROCESS
	 prc.amn_process_id, COALESCE(prc.name, prc.description) as proceso,
     -- CONTRACT
	 amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
	-- CURRENCY
	curr1.iso_code as iso_code1,
	COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
	COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
	curr2.iso_code as iso_code2,
	COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
	COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2,  
	 -- EMPLOYEE
	 emp.amn_employee_id, emp.value as value_emp, emp.name as empleado,
     CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN 1 ELSE 0 END AS imp_empleado, 
	-- PAYROLL
	 --pyr.amountallocated as amountallocated_t, 
	 --pyr.amountdeducted as amountdeducted_t, 
	 --pyr.amountcalculated as amountcalculated_t,
	-- PAYROLL DETAIL
	 pyr_d.qtyvalue as cantidad, 
	   currencyConvert(pyr_d.amountallocated, pyr.c_currency_id,$P{C_Currency_ID},pyr.dateacct,pyr.C_ConversionType_ID,pyr.ad_client_id,pyr.ad_org_id) as amountallocated,
	   currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id,$P{C_Currency_ID},pyr.dateacct,pyr.C_ConversionType_ID,pyr.ad_client_id,pyr.ad_org_id) as amountdeducted,
	   currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id,$P{C_Currency_ID},pyr.dateacct,pyr.C_ConversionType_ID,pyr.ad_client_id,pyr.ad_org_id) as amountcalculated
 	FROM adempiere.amn_payroll as pyr
    INNER JOIN adempiere.amn_payroll_detail 		as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
    INNER JOIN adempiere.amn_concept_types_proc as ctp 	 ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
    INNER JOIN adempiere.amn_concept_types 			as cty 	 ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
    INNER JOIN adempiere.amn_process  					as prc 	 ON (prc.amn_process_id= ctp.amn_process_id)
    INNER JOIN adempiere.amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
    INNER JOIN (
    SELECT 
		tiper.amn_period_id, tiper.amn_contract_id , tiper.amn_contract_id ,
		tiper.name, tiper.amndateini, tiper.amndateend, tiper.amn_period_status
		FROM AMN_Period tiper
		INNER JOIN C_Period per ON (per.c_period_id = tiper.c_period_id )
		WHERE tiper.amn_process_id IN (Select AMN_process_ID FROM AMN_Process pro WHERE pro.Value = 'NN')
		AND  CASE WHEN (tiper.amndateini BETWEEN $P{AMNDateIni} AND $P{AMNDateEnd} ) THEN 1=1 ELSE 1=0 END
		AND  CASE WHEN (tiper.amndateend BETWEEN $P{AMNDateIni} AND $P{AMNDateEnd} ) THEN 1=1 ELSE 1=0 END
    ) as prd 	 ON (prd.amn_period_id= pyr.amn_period_id)
	LEFT JOIN adempiere.amn_location 					as lct 	 ON (lct.amn_location_id= pyr.amn_location_id)
	INNER JOIN adempiere.amn_contract 					as amc 	 ON (amc.amn_contract_id= pyr.amn_contract_id)	
	INNER JOIN (
		SELECT DISTINCT con.AMN_Contract_ID, con.value, con.name, con.description, CASE WHEN rol.AMN_Process_ID IS NULL THEN 'N' ELSE 'Y' END as OK_salary
		FROM adempiere.AMN_Contract con
		INNER JOIN (
			SELECT AMN_Process_ID, AMN_Contract_ID, AD_Role_ID FROM adempiere.AMN_Role_Access 
			WHERE  AMN_Process_ID IN (SELECT AMN_Process_ID FROM adempiere.AMN_Process WHERE AMN_Process_Value='NN') 
			AND ( CASE WHEN ( $P{AD_Role_ID}  IS NULL OR AD_Role_ID= $P{AD_Role_ID} ) THEN 1=1 ELSE 1=0 END )
		) rol ON rol.AMN_Contract_ID = con.AMN_Contract_ID
	 ) cok on cok.AMN_Contract_ID = emp.AMN_Contract_ID
	 LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
     LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
     LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
     LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
WHERE pyr.AD_Client_ID =$P{AD_Client_ID}
	AND	( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR prc.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Concept_Types_ID}  IS NULL OR ctp.amn_concept_types_id= $P{AMN_Concept_Types_ID} ) THEN 1=1 ELSE 1=0 END )
	) as nomina ON (1= 0)
WHERE (imp_header= 1) OR (client_id= $P{AD_Client_ID}
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY  nomina.proceso, nomina.c_value, nomina.amndateini, nomina.calcorder ASC, nomina.value_emp ASC

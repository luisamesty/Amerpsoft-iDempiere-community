-- Payroll Loan ALL
-- PAGOS CRUZADOS Y NO CRUZADOS
--- V4
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
	-- *********************
	-- *** PAGOS CRUZADOS **
	-- *********************
		-- PERIOD
		prd.amn_period_id, 
		prd.amndateini, 
		prd.amndateend,
		-- TIPO DE CONCEPTO
		cty.sign as ConTyp_Sign,
		-- TIPO DE CONCEPTO (PROCESO)
		-- PROCESS
		CASE WHEN $P{AMN_Process_ID} IS NULL THEN 'Todos los procesos PJ/PO' ELSE COALESCE(prc.name, prc.description) END AS Process_title,
		prc.amn_process_id, 
		prc.AMN_Process_Value as Process_Value,
		COALESCE(prc.name, prc.description) as Process_name,
		-- CONTRACT
		amc.value as Contract_Value, COALESCE(amc.name, amc.description) as Contract_Name, 
		-- DEPARTAMENT
		COALESCE(dep.name,dep.description) as departamento,
		-- EMPLOYEE
		emp.amn_employee_id, emp.value as Employee_Value, 
		emp.name as Employee_Name, 
		emp.idnumber AS Employee_taxid,
	   	COALESCE(jtt.name, jtt.description) as Employee_Jobtitle, 
		-- PAYROLL
		pyr.ad_client_id, pyr.ad_org_id,
		pyr.AMN_Payroll_ID as Pay_ID,
		pyr.Value as Pay_Value,
		pyr.Name as Pay_Name,
		pyr.InvDateIni as Pay_DateIni,
		pyr.InvDateEnd as Pay_DateEnd,
		cty.Value as ConTyp_Value,
		cty.Name as ConTyp_Name,
		cty.sign as ConTyp_Sign,
		pyr_d.AMN_Payroll_Detail_ID as PayDet_ID,
		CONCAT(prc.AMN_Process_Value,'-',pyr.DocumentNo) as PayDet_Doc,
		CASE WHEN pyr_d.AMN_Payroll_Deferred_ID IS NOT NULL THEN  pyr_d.AMN_Payroll_Deferred_ID ELSE  pyr_d.AMN_Payroll_Detail_ID END as PayDet_Deferred_ID,
		pyr_d.Value as PayDet_Value,
		pyr_d.Name as PayDet_Name,
		-- PYR AMOUNTS CONVERTED
		currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated_t2, 
		currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted_t2, 
		-- CURRENCY
		curr1.iso_code as iso_code1,
		COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
		COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
		curr2.iso_code as iso_code2,
		COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
		COALESCE(currt2.description,curr2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
		-- PAYROLL DETAIL
	   	-- MONTOS Y CIFRAS	   
		pyr_d.value as detail_value,
		pyr_d.qtyvalue as cantidad, 
		pyr_d.description as paydet_description,
		currencyConvert(pyr_d.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as PayDet_AmountAllocated2, 
		currencyConvert(pyr_d.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as PayDet_AmountDeducted2, 
		-- DEFERRED
		diferido.Pago_Cruzado,
		diferido.linea_def,
		diferido.allocated_def2, 
		diferido.deducted_def2,
		coalesce(diferido.duedate,now()) AS duedate,
		diferido.description_def,
		diferido.qty_pay,
		diferido.allocated_pay2,
		diferido.deducted_pay2
		FROM adempiere.amn_payroll as pyr
		INNER JOIN adempiere.amn_payroll_detail as pyr_d 	ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
		INNER JOIN adempiere.amn_concept_types_proc as ctp  ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
		INNER JOIN adempiere.amn_concept_types as cty 	 	ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
		INNER JOIN adempiere.amn_process  as prc 	 		ON (prc.amn_process_id= pyr.amn_process_id)
		INNER JOIN adempiere.amn_contract as amc 			ON (amc.amn_contract_id= pyr.amn_contract_id)	 
		INNER JOIN adempiere.amn_employee as emp 			ON (emp.amn_employee_id= pyr.amn_employee_id)
		LEFT JOIN adempiere.amn_department as dep 			ON (emp.amn_department_id = dep.amn_department_id)
		LEFT JOIN adempiere.amn_jobtitle as jtt 			ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
		LEFT JOIN adempiere.amn_period as prd				ON (prd.amn_period_id= pyr.amn_period_id)
		LEFT JOIN adempiere.amn_location as lct 			ON (lct.amn_location_id= pyr.amn_location_id)
		LEFT JOIN c_currency curr1							on pyr.c_currency_id = curr1.c_currency_id
		LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		LEFT JOIN c_currency curr2 							on curr2.c_currency_id = $P{C_Currency_ID}
		LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		LEFT JOIN (
			-- PAGOS CRUZADOS
			SELECT DISTINCT 
			'Y' AS Pago_Cruzado,
			pyr_def.amn_payroll_id as payroll_id, 
			pyr_def.amn_payroll_deferred_id as deferred_id,
			-- SE INVIERTE PARA PRESENTACION
			currencyConvert(pyr_def.amountallocated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as allocated_def2, 
			currencyConvert(pyr_def.amountdeducted,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as deducted_def2, 
			pyr_def.qtyvalue as cantidad_def,
			pyr_def.line as linea_def, 
			pyr_def.ispaid, 
			COALESCE(pyr_def.duedate, prd3.amndateend) AS duedate,
			CASE WHEN pyr3.amn_payroll_id IS NOT NULL THEN CONCAT('Rec:',pyr3.documentno,'-',TO_CHAR(pyr3.invdateend, 'DD/MM/YYYY'),' - ',pyr3.value) ELSE '' END as description_def, 
			pyrd2.qtyvalue AS qty_pay,
			currencyConvert(pyrd2.amountallocated,pyr3.c_currency_id, $P{C_Currency_ID}, pyr3.dateacct, NULL, pyr3.AD_Client_ID, pyr3.AD_Org_ID ) as allocated_pay2, 
			currencyConvert(pyrd2.amountdeducted,pyr3.c_currency_id, $P{C_Currency_ID}, pyr3.dateacct, NULL, pyr3.AD_Client_ID, pyr3.AD_Org_ID ) as deducted_pay2
			FROM adempiere.amn_payroll_deferred as pyr_def
			LEFT JOIN adempiere.amn_payroll as pyr2 ON (pyr2.amn_payroll_id=pyr_def.amn_payroll_id)
			LEFT JOIN adempiere.amn_payroll_detail pyrd2 ON pyrd2.amn_payroll_deferred_id = pyr_def.amn_payroll_deferred_id
			LEFT JOIN adempiere.amn_payroll AS pyr3 ON pyr3.amn_payroll_id = pyrd2.amn_payroll_id
			LEFT JOIN adempiere.amn_period AS prd3 ON prd3.amn_period_id = pyr_def.amn_period_id
			ORDER BY pyr_def.amn_payroll_id ASC, pyr_def.line ASC
		) as diferido ON (diferido.payroll_id = pyr.amn_payroll_id) 
		WHERE prc.value IN ('PJ','PO')
			AND pyr.InvDateIni >= COALESCE(TO_TIMESTAMP($P{DateIni}, 'YYYY-MM-DD HH24:MI:SS'), '2000-12-31 00:00:00'::TIMESTAMP)
			AND pyr.InvDateEnd <= COALESCE(TO_TIMESTAMP($P{DateEnd}, 'YYYY-MM-DD HH24:MI:SS'), Now()::TIMESTAMP)
			AND ( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR prc.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
			AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
			AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
	-- ************************
	-- *** PAGOS NO-CRUZADOS **
	-- ************************
	UNION ALL
	SELECT DISTINCT
		-- PERIOD
		prd.amn_period_id, 
		prd.amndateini, 
		prd.amndateend,
		-- TIPO DE CONCEPTO
		cty.sign as ConTyp_Sign,
		-- TIPO DE CONCEPTO (PROCESO)
		-- PROCESS
		CASE WHEN $P{AMN_Process_ID} IS NULL THEN 'Todos los procesos PJ/PO' ELSE COALESCE(prc.name, prc.description) END AS Process_title,
		99999999999 AS amn_process_id, 
		'ZZ' as Process_Value,
		'** PAGOS NO CRUZADOS **' as Process_name,
		-- CONTRACT
		amc.value as Contract_Value, COALESCE(amc.name, amc.description) as Contract_Name, 
		-- DEPARTAMENT
		COALESCE(dep.name,dep.description) as departamento,
		-- EMPLOYEE
		emp.amn_employee_id, emp.value as Employee_Value, 
		emp.name as Employee_Name, 
		emp.idnumber AS Employee_taxid,
	   	COALESCE(jtt.name, jtt.description) as Employee_Jobtitle, 
		-- PAYROLL
		pyr.ad_client_id, pyr.ad_org_id,
		pyr.AMN_Payroll_ID as Pay_ID,
		pyr.Value as Pay_Value,
		pyr.Name as Pay_Name,
		pyr.InvDateIni as Pay_DateIni,
		pyr.InvDateEnd as Pay_DateEnd,
		cty.Value as ConTyp_Value,
		cty.Name as ConTyp_Name,
		cty.sign as ConTyp_Sign,
		pyr_d.AMN_Payroll_Detail_ID as PayDet_ID,
		CONCAT(prc.AMN_Process_Value,'-',pyr.DocumentNo) as PayDet_Doc,
		pyr_d.AMN_Payroll_Detail_ID as PayDet_Deferred_ID,
		pyr_d.Value as PayDet_Value,
		pyr_d.Name as PayDet_Name,
		-- PYR AMOUNTS CONVERTED
		currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated_t2, 
		currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted_t2, 
		-- CURRENCY
		curr1.iso_code as iso_code1,
		COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
		COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
		curr2.iso_code as iso_code2,
		COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
		COALESCE(currt2.description,curr2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
		-- PAYROLL DETAIL
	   	-- MONTOS Y CIFRAS	   
		pyr_d.value as detail_value,
		pyr_d.qtyvalue as cantidad, 
		pyr_d.description as paydet_description,
		currencyConvert(pyr_d.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as PayDet_AmountAllocated2, 
		currencyConvert(pyr_d.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as PayDet_AmountDeducted2, 
		-- DEFERRED
		diferido2.Pago_Cruzado,
		diferido2.linea_def,
		diferido2.allocated_def2, 
		diferido2.deducted_def2,
		coalesce(diferido2.duedate,now()) AS duedate,
		diferido2.description_def,
		diferido2.qty_pay,
		diferido2.allocated_pay2,
		diferido2.deducted_pay2
		FROM adempiere.amn_payroll as pyr
		INNER JOIN adempiere.amn_payroll_detail as pyr_d 	ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
		INNER JOIN adempiere.amn_concept_types_proc as ctp  ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
		INNER JOIN adempiere.amn_concept_types as cty 	 	ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
		INNER JOIN adempiere.amn_process  as prc 	 		ON (prc.amn_process_id= pyr.amn_process_id)
		INNER JOIN adempiere.amn_contract as amc 			ON (amc.amn_contract_id= pyr.amn_contract_id)	 
		INNER JOIN adempiere.amn_employee as emp 			ON (emp.amn_employee_id= pyr.amn_employee_id)
		LEFT JOIN adempiere.amn_department as dep 			ON (emp.amn_department_id = dep.amn_department_id)
		LEFT JOIN adempiere.amn_jobtitle as jtt 			ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
		LEFT JOIN adempiere.amn_period as prd				ON (prd.amn_period_id= pyr.amn_period_id)
		LEFT JOIN adempiere.amn_location as lct 			ON (lct.amn_location_id= pyr.amn_location_id)
		LEFT JOIN c_currency curr1							on pyr.c_currency_id = curr1.c_currency_id
		LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		LEFT JOIN c_currency curr2 							on curr2.c_currency_id = $P{C_Currency_ID}
		LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		INNER JOIN (
			-- PAGOS NO CRUZADOS
			SELECT DISTINCT 
			'N' AS Pago_Cruzado,
			napay.AMN_Payroll_ID as payroll_id,
			napay_d.AMN_Payroll_Detail_ID AS amn_payroll_detail_id,
			CAST (0 AS NUMERIC)  as deferred_id,
			---- SE INVIERTE PARA PRESENTACION
			currencyConvert(CAST (0 AS NUMERIC),napay.c_currency_id, $P{C_Currency_ID}, napay.dateacct, NULL, napay.AD_Client_ID, napay.AD_Org_ID ) as allocated_def2, 
			currencyConvert(CAST (0 AS NUMERIC),napay.c_currency_id, $P{C_Currency_ID}, napay.dateacct, NULL, napay.AD_Client_ID, napay.AD_Org_ID ) as deducted_def2,
			napay_d.qtyvalue as cantidad_def,
			napay_d.line  as linea_def, 
			'N' AS ispaid, 
			COALESCE(napay.invdateend, napay.dateacct) AS duedate,
			CONCAT('Rec:',napay.documentno,'-',TO_CHAR(napay.invdateend, 'DD/MM/YYYY'),' - ',napay.value) as description_def, 
			napay_d.qtyvalue AS qty_pay,
			currencyConvert(napay_d.amountallocated,napay.c_currency_id, $P{C_Currency_ID}, napay.dateacct, NULL, napay.AD_Client_ID, napay.AD_Org_ID ) as allocated_pay2, 
			currencyConvert(napay_d.amountdeducted,napay.c_currency_id, $P{C_Currency_ID}, napay.dateacct, NULL, napay.AD_Client_ID, napay.AD_Org_ID ) as deducted_pay2
			FROM adempiere.AMN_Payroll_Detail napay_d
			LEFT JOIN adempiere.AMN_Payroll napay ON (napay.AMN_Payroll_ID = napay_d.AMN_Payroll_ID)
			LEFT JOIN adempiere.AMN_Concept_Types_Proc nactyp ON (nactyp.AMN_Concept_Types_Proc_ID = napay_d.AMN_Concept_Types_Proc_ID)
			LEFT JOIN adempiere.AMN_Concept_Types nacty ON (nacty.AMN_Concept_Types_ID = nactyp.AMN_Concept_Types_ID)
			LEFT JOIN adempiere.AMN_Employee naemp ON (naemp.AMN_Employee_ID = napay.AMN_Employee_ID)
			LEFT JOIN adempiere.AMN_Process naprc ON (naprc.AMN_Process_ID = napay.AMN_Process_ID)
				WHERE nacty.sign = 'C'
				AND AMN_Payroll_Deferred_ID IS NULL 
				AND nacty.optmode = 'B'
				AND napay_d.AD_Client_ID = $P{AD_Client_ID}
				AND napay.InvDateIni >= '2020-01-01'
		) as diferido2 ON (diferido2.payroll_id = pyr.amn_payroll_id AND diferido2.amn_payroll_detail_id = pyr_d.amn_payroll_detail_id) 
		WHERE prc.value IN ('NN','NU','NV','TI','NO')
			AND  diferido2.Pago_Cruzado='N'
			AND cty.sign = 'C'
			AND cty.optmode = 'B'
			AND pyr.InvDateIni >= COALESCE(TO_TIMESTAMP($P{DateIni}, 'YYYY-MM-DD HH24:MI:SS'), '2000-12-31 00:00:00'::TIMESTAMP)
			AND pyr.InvDateEnd <= COALESCE(TO_TIMESTAMP($P{DateEnd}, 'YYYY-MM-DD HH24:MI:SS'), Now()::TIMESTAMP)
			AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
			AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
) as nomina ON (1= 0)
WHERE (imp_header = 1) OR 
	(nomina.ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR nomina.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY  Contract_Value, Employee_Value, Process_Value, Pay_ID, duedate, Pago_cruzado


SELECT
	pyr.ad_client_id,
	pyr.ad_org_id,
	c_prd.c_period_id,
	emp.amn_employee_id,
	emp.value AS value_emp,
	emp.name AS empleado,
	COALESCE(jtt.workforce, jtt.workforce, 'A') AS workforce,
	pyr_d.value AS detail_value,
	qtyvalue AS cantidad,
	pyr_d.amountallocated AS amountallocated_src,
	pyr_d.amountdeducted AS amountdeducted_src,
	pyr_d.amountcalculated AS amountcalculated_src,
	currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, 1000001, pyr.dateacct , pyr.c_conversiontype_id, 1000000, 1000000) 
	AS amountallocated,
	currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, 1000001, pyr.dateacct , pyr.c_conversiontype_id, 1000000, 1000000) 
	AS amountdeducted,
	currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, 1000001, pyr.dateacct , pyr.c_conversiontype_id, 1000000, 1000000) 
	AS amountcalculated,
	pyr.c_currency_id,
	pyr.c_conversiontype_id 
FROM
	amn_payroll AS pyr
LEFT JOIN amn_payroll_detail AS pyr_d ON
	(pyr_d.amn_payroll_id = pyr.amn_payroll_id)
LEFT JOIN amn_concept_types_proc AS ctp ON
	(ctp.amn_concept_types_proc_id = pyr_d.amn_concept_types_proc_id)
LEFT JOIN amn_concept_types AS cty ON
	((cty.amn_concept_types_id = ctp.amn_concept_types_id))
LEFT JOIN amn_process AS prc ON
	(prc.amn_process_id = ctp.amn_process_id)
INNER JOIN amn_employee AS emp ON
	(emp.amn_employee_id = pyr.amn_employee_id)
LEFT JOIN amn_jobtitle AS jtt ON
	(pyr.amn_jobtitle_id = jtt.amn_jobtitle_id)
LEFT JOIN amn_jobtitle AS jtt2 ON
	(emp.amn_jobtitle_id = jtt2.amn_jobtitle_id)
LEFT JOIN amn_period AS prd ON
	(prd.amn_period_id = pyr.amn_period_id)
LEFT JOIN c_period AS c_prd ON
	(c_prd.c_period_id = prd.c_period_id)
WHERE
	prc.value = 'NN'
	AND cty.optmode <> 'R'
	AND cty.prestacion = 'Y'
	AND pyr.AD_Client_ID = 1000000
	AND pyr.AD_Org_ID = 1000000
	AND c_prd.C_Period_ID = 1000114 

SELECT
paydet.ad_client_id,
paydet.ad_org_id,
paydet.workforce,
paydet.c_period_id,
paydet.amn_employee_id,
paydet.value_emp,
paydet.empleado,
COALESCE(sum(paydet.amountallocated) - sum(paydet.amountdeducted),0) as payamt,
COALESCE(paydone.paydone,0) as paydone,
COALESCE(sum(paydet.amountallocated) - sum(paydet.amountdeducted) - paydone.paydone,0) as paydiff
FROM (
	SELECT 
	       pyr.ad_client_id, 
	       pyr.ad_org_id,
	      c_prd.c_period_id, 
	      emp.amn_employee_id, 
	      emp.value as value_emp, 
	      emp.name as empleado, 
	      jtt.workforce,
	      pyr_d.value as detail_value, 
	      qtyvalue as cantidad, 
	      pyr_d.amountallocated, 
	      pyr_d.amountdeducted, pyr_d.amountcalculated
	FROM amn_payroll as pyr
	LEFT JOIN amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
	LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
	LEFT JOIN amn_concept_types	as cty ON ((cty.amn_concept_types_id= ctp.amn_concept_types_id))
	LEFT JOIN amn_process as prc ON (prc.amn_process_id= ctp.amn_process_id)
	INNER JOIN amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
	LEFT JOIN amn_jobtitle as jtt ON (pyr.amn_jobtitle_id= jtt.amn_jobtitle_id)
	LEFT JOIN amn_period as prd ON (prd.amn_period_id= pyr.amn_period_id)
	LEFT JOIN c_period as c_prd ON (c_prd.c_period_id= prd.c_period_id)

	WHERE prc.value ='NN' AND cty.optmode <> 'R' AND cty.prestacion ='Y'
	AND c_prd.C_Period_ID = 1000057
) as paydet
LEFT JOIN (
	SELECT 
	amn_employee_id,
	COALESCE(sum(amountallocated) - sum(amountdeducted),0) as paydone
	 FROM (
		SELECT 
		      pyr.ad_client_id, 
		      pyr.ad_org_id ,
		      c_prd.c_period_id, c_prd.startdate, c_prd.enddate,
		      emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, 
		      jtt.workforce,
		      pyr_d.value as detail_value, 
		      qtyvalue as cantidad, 
		      pyr_d.amountallocated, 
		      pyr_d.amountdeducted, pyr_d.amountcalculated
		FROM amn_payroll as pyr
		LEFT JOIN amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
		LEFT JOIN amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
		LEFT JOIN amn_concept_types	as cty ON ((cty.amn_concept_types_id= ctp.amn_concept_types_id))
		LEFT JOIN amn_process as prc ON (prc.amn_process_id= ctp.amn_process_id)
		INNER JOIN amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
		LEFT JOIN amn_jobtitle as jtt ON (pyr.amn_jobtitle_id= jtt.amn_jobtitle_id)
		LEFT JOIN amn_period as prd ON (prd.amn_period_id= pyr.amn_period_id)
		LEFT JOIN c_period as c_prd ON (c_prd.c_period_id= prd.c_period_id)
		WHERE prc.value ='NP' AND cty.optmode != 'R'
		AND c_prd.C_Period_ID = 1000057
	) as paydet
	GROUP BY amn_employee_id
) as paydone ON (paydone.amn_employee_id = paydet.amn_employee_id )
GROUP BY ad_client_id, ad_org_id, workforce, c_period_id, paydet.amn_employee_id, value_emp,empleado, paydone.paydone
ORDER BY paydet.workforce, paydet.value_emp
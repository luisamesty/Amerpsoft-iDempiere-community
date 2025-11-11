SELECT DISTINCT cty.value2 
FROM amp_concept_tree(1000000, 0) AS cty
WHERE cty.calcorder1 = 100000 AND cty.optmode='A'
ORDER BY 1

SELECT *
FROM amp_concept_tree(1000000, 0) AS cty 
WHERE cty.optmode='A'

-- OTROSINGRESOS + BONIFICACIONES + FERIADOS + HORAS EXTRAS
-- COMISIONES + 
-- SALARIOIMPORTE

SELECT *
FROM (
	SELECT 
	emp.amn_employee_id, 
	emp.value,
	cty.value2,
	COALESCE(currt0.description,curr0.iso_code,curr0.cursymbol,'') as currname,
	SUM(pyr_d.amountallocated) AS amountallocated ,  
	SUM(pyr_d.amountdeducted) AS amountdeducted  , 
	SUM(pyr_d.amountcalculated ) AS amountcalculated
	FROM AMN_Employee emp
	INNER JOIN adempiere.amn_payroll as pyr ON pyr.amn_employee_id = emp.amn_employee_id
	INNER JOIN adempiere.c_currency AS curr0 ON curr0.c_currency_id= pyr.c_currency_id
    LEFT JOIN c_currency_trl currt0 on curr0.c_currency_id = currt0.c_currency_id and currt0.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
	INNER JOIN adempiere.amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
	INNER JOIN adempiere.amn_concept_types_proc  as ctp 	 ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
	INNER JOIN amp_concept_tree(1000000, 0)	as cty 	 ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
	WHERE emp.isactive= 'Y' AND cty.calcorder1 = 100000 AND emp.ad_client_id=  $P{AD_Client_ID} 
	AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
	AND pyr.dateacct BETWEEN DATE($P{DateIni} ) AND DATE($P{DateEnd} ) 
	GROUP BY emp.amn_employee_id, emp.value, cty.calcorder1, cty.calcorder2, cty.value2, currt0.cursymbol, currt0.description, curr0.iso_code, curr0.cursymbol
	ORDER BY emp.amn_employee_id, emp.value, cty.calcorder1, cty.calcorder2, cty.value2
) AS ctyhist
WHERE amn_employee_id = 1509370
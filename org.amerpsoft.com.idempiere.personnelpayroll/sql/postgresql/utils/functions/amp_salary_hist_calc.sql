-- Function: adempiere.amp_salary_hist_calc(character varying, numeric, timestamp without time zone, timestamp without time zone, numeric, numeric)
-- Used onClass AmerpPayrollCalcUtilDVFormulas.java (Default Values)
-- Added C_Currency_ID C_ConversionType_ID C_ConversionParameter 
-- Calc is based on Dates Ini and End from C_Period Table Fiscal Year
--
--DROP FUNCTION IF EXISTS adempiere.amp_salary_hist_calc(character varying, numeric, timestamp without time zone, timestamp without time zone, numeric, numeric);
-- String p_concept_type options:
--  'salary_base': Salary all concepts with salary check
--  'salary_gravable': Salary all concepts with arc check for NN, NU and NV
--  'salary_integral': Salary all concepts with salary and utilidad checks for NN, NU and NV 
--  'salary_vacation': Salary all concepts with vacacion check  and NN
--  'salary_utilities': Salary all concepts with utilities check and NN, NV
--  'salary_utilities_nn': Salary all concepts with utilidad check and NN only
--  'salary_utilities_nv': Salary all concepts with utilidad check and NV only
--  'salary_socialbenefits': Salary all concepts with  prestacion
--  'salary_socialbenefits_nn': Salary all concepts with  prestacion and NN
--  'salary_socialbenefits_nv': Salary all concepts with  prestacion and  NV
--  'salary_socialbenefits_nu': Salary all concepts with  prestacion and NU
--
CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_calc(
	p_salary_type character varying, 
	p_employee_id numeric, 
	p_startdate timestamp without time zone, 
	p_enddate timestamp without time zone, 
	p_currency_id numeric, 
	p_conversiontype_id numeric)
  RETURNS numeric AS
$BODY$
DECLARE
	v_returnSalary		numeric :=0; 
BEGIN

SELECT
sum(asignado) into v_returnSalary
FROM (
	SELECT
		CASE
			WHEN p_salary_type = 'salary_base' AND (con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_gravable' AND (con_ty.arc = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text)) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_integral' AND ((con_ty.salario = 'Y'::bpchar OR con_ty.utilidad = 'Y'::bpchar OR con_ty.vacacion = 'Y'::bpchar) AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text)) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_vacation' AND (con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_utilities' AND (con_ty.utilidad = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text)) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_utilities_nn' AND (con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_utilities_nv' AND (con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = 'NV'::text) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_socialbenefits'    AND (con_ty.prestacion = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text)) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_socialbenefits_nn' AND (con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text ) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_socialbenefits_nv' AND (con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NV'::text ) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			WHEN p_salary_type = 'salary_socialbenefits_nu' AND (con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NU'::text ) 
				THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
			ELSE 0::numeric
		END AS asignado
	FROM 
		adempiere.amn_payroll pyr
		LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
		LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
		LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
		LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
		LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
		LEFT JOIN adempiere.c_period cper ON cper.c_period_id = pyr_p.c_period_id
	WHERE pyr.amn_employee_id = p_employee_id AND cper.startdate >= p_startdate AND cper.enddate <= p_enddate
	   ) periodos
	   ;
RETURN	v_returnSalary;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amp_salary_hist_calc(character varying, numeric, timestamp without time zone, timestamp without time zone, numeric, numeric)
  OWNER TO adempiere;

-- Function: adempiere.amp_special_wh_hist_calc(character varying, numeric, timestamp without time zone, timestamp without time zone, numeric, numeric)
-- To be Used Used on Class AmerpPayrollCalcUtilDVFormulas.java (Default Values)
-- Returns Tax, Social Security or any other Withholding Concepts for Allocations or Deductions
-- Added C_Currency_ID C_ConversionType_ID C_ConversionParameter 

--DROP FUNCTION IF EXISTS adempiere.amp_special_wh_hist_calc(character varying, numeric, timestamp without time zone, timestamp without time zone, numeric, numeric);
-- String p_concept_type options:
-- 	 salario: Salary
--   arc: ARC Tax Gravable
--   sso: Social Security Basic
--   spf: Social Security Additional for unemployment
--   vacacion: Vacation
--   prestacion: Social benefits
--   utilidad: End of year bonus
--   feriado: Hollidays extra
--   ince:  Educational Law
--   faov:  Housing Funds
-- String p_alloc_deduc options:
--   allocated: Summarize allocations
--	 deducted: Summarize deductions
--	 calculated: Summarize calculations (all)
-- String p_amn_process options:
--   NN, NU, NV , When null calculated all
--
CREATE OR REPLACE FUNCTION adempiere.amp_special_wh_hist_calc(	
	p_concept_type character varying,
	p_alloc_deduc character varying, 
	p_amn_process character varying,
	p_employee_id numeric, 
	p_startdate timestamp without time zone, 
	p_enddate timestamp without time zone, 
	p_currency_id numeric, 
	p_conversiontype_id numeric)
  RETURNS numeric AS
$BODY$
DECLARE
	v_returnAmount		numeric :=0; 
	v_returnAmountAlloc		numeric :=0; 
	v_returnAmountDeduc		numeric :=0; 
	v_returnAmountCalc		numeric :=0; 
BEGIN

IF(p_amn_process IS NULL)
	THEN
	   -- p_alloc_deduc
	   CASE p_alloc_deduc
	   WHEN 'allocated' THEN
	   
   			SELECT
			sum(rs_allocated) into v_returnAmountAlloc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_allocated
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
	            v_returnAmount := v_returnAmountAlloc;
	   WHEN 'deducted' THEN
   			SELECT
			sum(rs_deducted) into v_returnAmountDeduc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_deducted
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
	            v_returnAmount := v_returnAmountDeduc;
	   WHEN 'calculated' THEN
	   		SELECT
			sum(rs_calculated) into v_returnAmountCalc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_calculated
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
	            v_returnAmount := v_returnAmountCalc;
	   ELSE
		SELECT
			sum(rs_calculated) into v_returnAmountCalc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar 
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_calculated
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
	            v_returnAmount := v_returnAmountCalc;
	   END CASE;
   
ELSE IF(p_amn_process IS NOT NULL)
	THEN
	   -- p_alloc_deduc
	   CASE p_alloc_deduc
	   WHEN 'allocated' THEN
	      			SELECT
			sum(rs_allocated) into v_returnAmountAlloc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_allocated
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
	            v_returnAmount := v_returnAmountAlloc;
	            
	   WHEN 'deducted' THEN
	      			SELECT
			sum(rs_deducted) into v_returnAmountDeduc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_deducted
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
	            v_returnAmount := v_returnAmountDeduc;
	            v_returnAmount := v_returnAmountDeduc;
	   WHEN 'calculated' THEN
	   		SELECT
			sum(rs_calculated) into v_returnAmountCalc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_calculated
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;
				v_returnAmount := v_returnAmountCalc;
	   ELSE
	   		SELECT
			sum(rs_calculated) into v_returnAmountCalc
			FROM (
				SELECT
					CASE
						WHEN p_concept_type = 'salario' AND con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'arc' 	AND con_ty.arc = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'sso' 	AND con_ty.sso = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'spf' 	AND con_ty.spf = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'vacacion' 	AND con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'prestacion' 	AND con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'utilidad' 	AND con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'feriado' 	AND con_ty.feriado = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'ince' 	AND con_ty.ince = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						WHEN p_concept_type = 'faov' 	AND con_ty.faov = 'Y'::bpchar AND pro.amn_process_value::text = p_amn_process::text
							THEN currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
						ELSE 0::numeric
					END AS rs_calculated
					
				FROM 
					adempiere.amn_payroll pyr
					LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
					LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
					LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
					LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
					LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
					LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
				WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
				   ) periodos
				   ;	   
	            v_returnAmount := v_returnAmountCalc;
	   END CASE;
   END IF;
END IF;

RETURN	v_returnAmount;

END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amp_special_wh_hist_calc(
	character varying, 
	p_alloc_deduc character varying, 
	p_amn_process character varying,
	numeric, 
	timestamp without time zone, 
	timestamp without time zone, 
	numeric, 
	numeric
)
  OWNER TO adempiere;


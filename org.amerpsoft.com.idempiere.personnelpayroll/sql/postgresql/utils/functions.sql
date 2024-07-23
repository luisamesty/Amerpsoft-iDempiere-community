-- DROP FUNCTION adempiere.amp_deduction_hist(varchar, numeric, timestamp, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_deduction_hist(deduction_concept character varying, p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$

DECLARE
   v_returnValue   numeric := 0;
BEGIN
   SELECT sum (descontado)
     INTO v_returnValue
     FROM (SELECT 
	        CASE WHEN deduction_concept = 'ISLR' THEN pyr_d.amountdeducted
                     WHEN deduction_concept = 'SSO_T' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'SSO_P' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'SPF_T' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'SPF_P' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'FAOV_T' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'FAOV_P' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'INCES_P' THEN pyr_d.amountdeducted
		     WHEN deduction_concept = 'INCES_T' THEN pyr_d.amountdeducted
                 ELSE 0::numeric
            END AS descontado
           FROM adempiere.amn_payroll_detail pyr_d
            LEFT JOIN adempiere.amn_payroll pyr ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
            LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
            LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
            LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
            LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
            LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr_p.amn_process_id
            LEFT JOIN adempiere.c_period cper ON cper.c_period_id = pyr_p.c_period_id
           WHERE emp.amn_employee_id = p_employee_id 
		     AND cper.startdate >= p_startdate AND cper.enddate <= p_enddate) periodos;

   RETURN v_returnValue;
END;
$function$
;

-- DROP FUNCTION adempiere.amp_deduction_hist(varchar, numeric, timestamp, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_deduction_hist(deduction_concept character varying, p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$

DECLARE
   v_returnValue   numeric := 0;
BEGIN
   SELECT sum (descontado)
     INTO v_returnValue
     FROM (SELECT 
	        CASE WHEN deduction_concept = 'ISLR'
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
             WHEN deduction_concept = 'SSO_T' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'SSO_P' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'SPF_T' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'SPF_P' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'FAOV_T' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'FAOV_P' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'INCES_P' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
		     WHEN deduction_concept = 'INCES_T' 
				THEN currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id)
                 ELSE 0::numeric
            END AS descontado
           FROM adempiere.amn_payroll_detail pyr_d
            LEFT JOIN adempiere.amn_payroll pyr ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
            LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
            LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
            LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
            LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
            LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr_p.amn_process_id
            LEFT JOIN adempiere.c_period cper ON cper.c_period_id = pyr_p.c_period_id
           WHERE emp.amn_employee_id = p_employee_id 
		     AND cper.startdate >= p_startdate AND cper.enddate <= p_enddate) periodos;

   RETURN v_returnValue;
END;
$function$
;

-- DROP FUNCTION adempiere.amp_prestacion_accumulated_actual(numeric, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_prestacion_accumulated_actual(p_employee_id numeric, p_perioddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_Prestacionaccumulated numeric := 0;
	v_Prestacionadvance numeric := 0;	
	v_Prestacionamount numeric := 0;
	v_Prestacionadjustment numeric := 0;
BEGIN
	SELECT 
		sum(prestacionamount), sum(prestacionadvance), sum(prestacionadjustment) 
		INTO
		v_Prestacionamount ,v_Prestacionadvance, v_Prestacionadjustment 
	FROM 	adempiere.amn_employee_salary as ems
	WHERE	ems.validto <= p_perioddate AND ems.amn_employee_id = p_employee_id
	;
	v_Prestacionaccumulated := v_Prestacionamount + v_Prestacionadjustment - v_Prestacionadvance;
	

RETURN	v_Prestacionaccumulated;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_prestacion_accumulated_prev(numeric, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_prestacion_accumulated_prev(p_employee_id numeric, p_perioddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_Prestacionaccumulated numeric := 0;
	v_Prestacionadvance numeric := 0;	
	v_Prestacionamount numeric := 0;
	v_Prestacionadjustment numeric := 0;
BEGIN
	SELECT 
		sum(prestacionamount), sum(prestacionadvance), sum(prestacionadjustment) 
		INTO
		v_Prestacionamount ,v_Prestacionadvance, v_Prestacionadjustment 
	FROM 	adempiere.amn_employee_salary as ems
	WHERE	ems.validfrom < p_perioddate AND ems.amn_employee_id = p_employee_id
	;
	v_Prestacionaccumulated := v_Prestacionamount + v_Prestacionadjustment - v_Prestacionadvance;
	

RETURN	v_Prestacionaccumulated;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_prestacioninterest_accumulated_actual(numeric, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_prestacioninterest_accumulated_actual(p_employee_id numeric, p_perioddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_PrestacionInterestAccumulated numeric :=0; 
	v_Prestacioninterest numeric :=0;
	v_Prestacioninterestadvance numeric :=0;
	v_Prestacioninterestadjustment numeric :=0;
BEGIN
      	SELECT 
		sum(prestacioninterest), sum(prestacioninterestadvance), sum(prestacioninterestadjustment) 
		INTO 
		v_Prestacioninterest ,v_Prestacioninterestadvance, v_Prestacioninterestadjustment 
	FROM 	adempiere.amn_employee_salary as ems
	WHERE	ems.validto <= p_perioddate AND ems.amn_employee_id = p_employee_id
	;
	v_PrestacionInterestAccumulated := v_Prestacioninterest + v_Prestacioninterestadjustment - v_Prestacioninterestadvance;
	
RETURN	v_PrestacionInterestAccumulated;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_hist_calc(varchar, numeric, timestamp, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_calc(p_salary_type character varying, p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_returnSalary		numeric :=0; 
BEGIN

--SELECT
--salary into v_returnSalary
--FROM
--adempiere.amn_employee
--WHERE amn_employee_id = p_employee_id ;
SELECT
sum(asignado) into v_returnSalary
FROM (
	SELECT
		CASE
			WHEN p_salary_type = 'salary_base' AND (con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_gravable' AND (con_ty.arc = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text)) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_integral' AND ((con_ty.salario = 'Y'::bpchar OR con_ty.utilidad = 'Y'::bpchar OR con_ty.vacacion = 'Y'::bpchar) AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text)) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_vacation' AND (con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_utilities' AND (con_ty.utilidad = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text)) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_utilities_nn' AND (con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_utilities_nv' AND (con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = 'NV'::text) THEN pyr_d.amountallocated                                                                
			WHEN p_salary_type = 'salary_socialbenefits'    AND (con_ty.prestacion = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text)) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_socialbenefits_nn' AND (con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text ) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_socialbenefits_nv' AND (con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NV'::text ) THEN pyr_d.amountallocated
			WHEN p_salary_type = 'salary_socialbenefits_nu' AND (con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NU'::text ) THEN pyr_d.amountallocated
			ELSE 0::numeric
		END AS asignado
	FROM 
		adempiere.amn_payroll pyr
		LEFT JOIN adempiere.amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
		LEFT JOIN adempiere.amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
		LEFT JOIN adempiere.amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
		LEFT JOIN adempiere.amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
		LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
		LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr_p.amn_process_id
		LEFT JOIN adempiere.c_period cper ON cper.c_period_id = pyr_p.c_period_id
	WHERE emp.amn_employee_id = p_employee_id AND cper.startdate >= p_startdate AND cper.enddate <= p_enddate
	   ) periodos
	   ;
RETURN	v_returnSalary;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_hist_calc(varchar, numeric, timestamp, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_calc(p_salary_type character varying, p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
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
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_hist_calc_all(varchar, numeric, timestamp, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_calc_all(p_salary_type character varying, p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
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
		LEFT JOIN adempiere.amn_process pro ON pro.amn_process_id = pyr.amn_process_id
	WHERE pyr.amn_employee_id = p_employee_id AND pyr.InvDateIni >= p_startdate AND pyr.InvDateEnd <= p_enddate
	   ) periodos
	   ;
RETURN	v_returnSalary;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_hist_updated(numeric, timestamp, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_updated(p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_returnSalary		numeric :=0; 
BEGIN
SELECT sum(salary_socialbenefits_updated) into v_returnSalary
  FROM adempiere.amn_payroll_historic as prh
  WHERE prh.amn_employee_id = p_employee_id AND prh.validfrom >= p_startdate AND prh.validto <= p_enddate ;

RETURN	v_returnSalary;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_hist_updated(numeric, timestamp, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_updated(p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_returnSalary		numeric :=0; 
BEGIN

SELECT sum(salary_socialbenefits_updated) into v_returnSalary
  FROM (
  SELECT 
  amn_employee_id, validfrom, validto, currencyConvert(salary_socialbenefits_updated, c_currency_id, p_currency_id , validto, p_conversiontype_id, ad_client_id,  ad_org_id)
  as salary_socialbenefits_updated
  FROM adempiere.amn_payroll_historic 

  ) as prh
  WHERE prh.amn_employee_id = p_employee_id AND prh.validfrom >= p_startdate AND prh.validto <= p_enddate ;
  
RETURN	v_returnSalary;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_utilities_updated(numeric, timestamp, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_utilities_updated(p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_returnSalary		numeric :=0; 
BEGIN
SELECT sum(salary_utilities_updated) into v_returnSalary
  FROM adempiere.amn_payroll_historic as prh
  WHERE prh.amn_employee_id = p_employee_id AND prh.validfrom >= p_startdate AND prh.validto <= p_enddate ;

RETURN	v_returnSalary;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_salary_utilities_updated(numeric, timestamp, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_salary_utilities_updated(p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_returnSalary		numeric :=0; 
BEGIN

SELECT sum(salary_utilities_updated) into v_returnSalary
  FROM (
  SELECT 
  amn_employee_id, validfrom, validto, currencyConvert(salary_utilities_updated, c_currency_id, p_currency_id , validto, p_conversiontype_id, ad_client_id,  ad_org_id)
  as salary_utilities_updated
  FROM adempiere.amn_payroll_historic 

  ) as prh
  WHERE prh.amn_employee_id = p_employee_id AND prh.validfrom >= p_startdate AND prh.validto <= p_enddate ;
  

RETURN	v_returnSalary;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_special_wh_hist_calc(varchar, varchar, varchar, numeric, timestamp, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_special_wh_hist_calc(p_concept_type character varying, p_alloc_deduc character varying, p_amn_process character varying, p_employee_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$
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

END;	$function$
;

-- DROP FUNCTION adempiere.amp_tax_amount(numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_tax_amount(p_employee_id numeric, p_aperiod_id numeric, p_cperiod_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$

DECLARE
   v_returnAmount   numeric := 0;
BEGIN

IF(p_aperiod_id IS NULL)
THEN	
   SELECT sum (asignado)
     INTO v_returnAmount
     FROM (SELECT pyr_d.amountallocated AS asignado
           FROM adempiere.amn_payroll_detail pyr_d
            LEFT JOIN adempiere.amn_payroll pyr ON (pyr.amn_payroll_id = pyr_d.amn_payroll_id)
            LEFT JOIN adempiere.amn_concept_types_proc con_tp ON (pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id)
            LEFT JOIN adempiere.amn_concept_types con_t ON (con_tp.amn_concept_types_id = con_t.amn_concept_types_id)
            LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
			LEFT JOIN adempiere.amn_period pyr_p ON ((pyr.amn_period_id = pyr_p.amn_period_id) AND (pyr_p.amn_contract_id = emp.amn_contract_id))
            LEFT JOIN adempiere.c_period cper ON cper.c_period_id = pyr_p.c_period_id
           WHERE emp.amn_employee_id = p_employee_id AND cper.c_period_id= p_cperiod_id
		     AND con_t.arc = 'Y'
		  ) as asignado_t;
ELSE IF(p_cperiod_id IS NULL)
THEN	
   SELECT sum (asignado)
     INTO v_returnAmount
     FROM (SELECT pyr_d.amountallocated AS asignado
           FROM adempiere.amn_payroll_detail pyr_d
            LEFT JOIN adempiere.amn_payroll pyr ON (pyr.amn_payroll_id = pyr_d.amn_payroll_id)
            LEFT JOIN adempiere.amn_concept_types_proc con_tp ON (pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id)
            LEFT JOIN adempiere.amn_concept_types con_t ON (con_tp.amn_concept_types_id = con_t.amn_concept_types_id)
            LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
			LEFT JOIN adempiere.amn_period pyr_p ON ((pyr.amn_period_id = pyr_p.amn_period_id) AND (pyr_p.amn_contract_id = emp.amn_contract_id))
           WHERE emp.amn_employee_id = p_employee_id AND pyr_p.amn_period_id= p_aperiod_id
		     AND con_t.arc = 'Y'
		  ) as asignado_t;		  
	 END IF;
END IF;
 
   RETURN v_returnAmount;
END;
$function$
;

-- DROP FUNCTION adempiere.amp_tax_amount(numeric, numeric, numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_tax_amount(p_employee_id numeric, p_aperiod_id numeric, p_cperiod_id numeric, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS $function$

DECLARE
   v_returnAmount   numeric := 0;
BEGIN

IF(p_aperiod_id IS NULL)
THEN	
   SELECT sum (asignado)
     INTO v_returnAmount
     FROM (SELECT 
	 	currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id) AS asignado
           FROM adempiere.amn_payroll_detail pyr_d
            LEFT JOIN adempiere.amn_payroll pyr ON (pyr.amn_payroll_id = pyr_d.amn_payroll_id)
            LEFT JOIN adempiere.amn_concept_types_proc con_tp ON (pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id)
            LEFT JOIN adempiere.amn_concept_types con_t ON (con_tp.amn_concept_types_id = con_t.amn_concept_types_id)
            LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
            LEFT JOIN adempiere.amn_period pyr_p ON ((pyr.amn_period_id = pyr_p.amn_period_id) AND (pyr_p.amn_contract_id = emp.amn_contract_id))
            LEFT JOIN adempiere.c_period cper ON cper.c_period_id = pyr_p.c_period_id
           WHERE emp.amn_employee_id = p_employee_id AND cper.c_period_id= p_cperiod_id
		     AND con_t.arc = 'Y'
		  ) as asignado_t;
ELSE IF(p_cperiod_id IS NULL)
THEN	
   SELECT sum (asignado)
     INTO v_returnAmount
     FROM (SELECT 
     currencyConvert(pyr_d.amountallocated, pyr.c_currency_id, p_currency_id, pyr.dateacct,p_conversiontype_id,pyr.ad_client_id,pyr.ad_org_id) AS asignado
            FROM adempiere.amn_payroll_detail pyr_d
            LEFT JOIN adempiere.amn_payroll pyr ON (pyr.amn_payroll_id = pyr_d.amn_payroll_id)
            LEFT JOIN adempiere.amn_concept_types_proc con_tp ON (pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id)
            LEFT JOIN adempiere.amn_concept_types con_t ON (con_tp.amn_concept_types_id = con_t.amn_concept_types_id)
            LEFT JOIN adempiere.amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
	    LEFT JOIN adempiere.amn_period pyr_p ON ((pyr.amn_period_id = pyr_p.amn_period_id) AND (pyr_p.amn_contract_id = emp.amn_contract_id))
           WHERE emp.amn_employee_id = p_employee_id AND pyr_p.amn_period_id= p_aperiod_id
		     AND con_t.arc = 'Y'
		  ) as asignado_t;		  
	 END IF;
END IF;
 
   RETURN v_returnAmount;
END;
$function$
;

-- DROP FUNCTION adempiere.amp_to_balanceall(numeric, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amp_to_balanceall(p_employee_id numeric, p_perioddate timestamp without time zone)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_TO_Balance numeric := 0;
	v_TO_Budget numeric := 0;	
	v_TO_Allocation numeric := 0;
	v_TO_Deduction numeric := 0;
	v_AD_Client_ID numeric := 0;
	v_TOMessage varchar(1024);
    
BEGIN

	v_TOMessage := '';
	select string_agg(CONCAT(tocty.concept_value,'-',tocty.concept_name,' Saldo = ',tocty.amountbalance) ,CHR(10) order by tocty.amn_employee_id, tocty.concept_value) as tomessage
		INTO v_TOMessage
	FROM
	(
		-- Concept Budget AND Concept Allocation and Deduction Before Date
		-- Concept Budget
		SELECT DISTINCT ON (empl2.amn_employee_id, coty2.amn_concept_types_id, ctyli2.AMN_Concept_Types_Limit_ID ) 
			--INTO v_TO_Budget
		empl2.AMN_Employee_ID, coty2.AMN_Concept_Types_ID, coty2.value as concept_value, coty2.name as concept_name,
		CASE WHEN ctyli2.qtytimes = 0 OR ctyli2.qtytimes = 1 THEN ctyli2.amountallocated
			WHEN ctyli2.qtytimes > 1 THEN ctyli2.amountallocated* ctyli2.qtytimes
			ELSE 0 END as amountbudget,
		COALESCE(alloc.amountallocated,0) as amountallocated, 
		COALESCE(alloc.amountdeducted,0) as amountdeducted,
		CASE WHEN ctyli2.qtytimes = 0 OR ctyli2.qtytimes = 1 THEN (ctyli2.amountallocated - COALESCE(alloc.amountallocated,0) + COALESCE(alloc.amountdeducted,0) )
			WHEN ctyli2.qtytimes > 1 THEN (ctyli2.amountallocated* ctyli2.qtytimes -COALESCE(alloc.amountallocated,0) +COALESCE(alloc.amountdeducted,0) )
			ELSE 0 END as amountbalance
		FROM AMN_Concept_Types coty2 
		LEFT JOIN AMN_Concept_Types_Limit as ctyli2 ON (ctyli2.AMN_Concept_Types_ID = coty2.AMN_Concept_Types_ID)
		LEFT JOIN AMN_Concept_types_proc as ctp2 ON (ctp2.amn_concept_types_id= coty2.amn_concept_types_id)
		LEFT JOIN AMN_Process as proc2 ON (proc2.AMN_Process_ID = ctp2.AMN_Process_ID)
		LEFT JOIN adempiere.amn_employee as empl2 ON (empl2.ad_client_id= coty2.ad_client_id)
		LEFT JOIN (
			-- Concept Allocation and Deduction Before Date
			SELECT DISTINCT
				pyr20.amn_employee_id,
				cty20.amn_concept_types_id,
				COALESCE(SUM(pyr_d20.amountallocated),0) as amountallocated, 
				COALESCE(SUM(pyr_d20.amountdeducted),0) as amountdeducted
				--INTO v_TO_Allocation, v_TO_Deduction
			FROM AMN_Payroll as pyr20
			LEFT JOIN AMN_Payroll_detail 		as pyr_d20 ON (pyr_d20.amn_payroll_id= pyr20.amn_payroll_id)
			LEFT JOIN AMN_Concept_types_proc as ctp20 	 ON (ctp20.amn_concept_types_proc_id= pyr_d20.amn_concept_types_proc_id)
			LEFT JOIN AMN_Concept_types 			as cty20 	 ON (cty20.amn_concept_types_id= ctp20.amn_concept_types_id)
			LEFT JOIN AMN_Process as proc20 ON (proc20.AMN_Process_ID = ctp20.AMN_Process_ID)
			LEFT JOIN AMN_Concept_Types_Limit as ctyli20 ON (ctyli20.AMN_Concept_Types_ID = cty20.AMN_Concept_Types_ID)

			WHERE ctyli20.AMN_Period_status <> 'C'
				AND pyr20.invdateend <= p_perioddate
			GROUP BY pyr20.amn_employee_id, cty20.amn_concept_types_id
		) alloc ON (alloc.amn_employee_id = empl2.amn_employee_id AND alloc.amn_concept_types_id = coty2.amn_concept_types_id)
		WHERE proc2.value='TO' AND ctyli2.AMN_Period_status <> 'C'
	) as tocty
	WHERE tocty.amn_employee_id = p_employee_id
	GROUP BY tocty.amn_employee_id ;
	
RETURN	v_TOMessage;
END;	$function$
;

-- DROP FUNCTION adempiere.amp_to_balanceall(numeric, timestamp, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_to_balanceall(p_employee_id numeric, p_perioddate timestamp without time zone, p_currency_id numeric, p_conversiontype_id numeric)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
DECLARE
	v_TO_Balance numeric := 0;
	v_TO_Budget numeric := 0;	
	v_TO_Allocation numeric := 0;
	v_TO_Deduction numeric := 0;
	v_AD_Client_ID numeric := 0;
	v_TOMessage varchar(1024);
    
BEGIN

	v_TOMessage := '';
	select string_agg(CONCAT(tocty.concept_value,'-',tocty.concept_name,' Saldo = ',tocty.amountbalance) ,CHR(10) order by tocty.amn_employee_id, tocty.concept_value) as tomessage
		INTO v_TOMessage
	FROM
	(
		-- Concept Budget AND Concept Allocation and Deduction Before Date
		-- Concept Budget
		SELECT DISTINCT ON (empl2.amn_employee_id, coty2.amn_concept_types_id, ctyli2.AMN_Concept_Types_Limit_ID ) 
			--INTO v_TO_Budget
		empl2.AMN_Employee_ID, coty2.AMN_Concept_Types_ID, coty2.value as concept_value, coty2.name as concept_name,
		CASE WHEN ctyli2.qtytimes = 0 OR ctyli2.qtytimes = 1 THEN ctyli2.amountallocated
			WHEN ctyli2.qtytimes > 1 THEN ctyli2.amountallocated* ctyli2.qtytimes
			ELSE 0 END as amountbudget,
		COALESCE(alloc.amountallocated,0) as amountallocated, 
		COALESCE(alloc.amountdeducted,0) as amountdeducted,
		CASE WHEN ctyli2.qtytimes = 0 OR ctyli2.qtytimes = 1 THEN (ctyli2.amountallocated - COALESCE(alloc.amountallocated,0) + COALESCE(alloc.amountdeducted,0) )
			WHEN ctyli2.qtytimes > 1 THEN (ctyli2.amountallocated* ctyli2.qtytimes -COALESCE(alloc.amountallocated,0) +COALESCE(alloc.amountdeducted,0) )
			ELSE 0 END as amountbalance
		FROM AMN_Concept_Types coty2 
		LEFT JOIN AMN_Concept_Types_Limit as ctyli2 ON (ctyli2.AMN_Concept_Types_ID = coty2.AMN_Concept_Types_ID)
		LEFT JOIN AMN_Concept_types_proc as ctp2 ON (ctp2.amn_concept_types_id= coty2.amn_concept_types_id)
		LEFT JOIN AMN_Process as proc2 ON (proc2.AMN_Process_ID = ctp2.AMN_Process_ID)
		LEFT JOIN adempiere.amn_employee as empl2 ON (empl2.ad_client_id= coty2.ad_client_id)
		LEFT JOIN (
			SELECT DISTINCT
				amn_employee_id,
				amn_concept_types_id,
				COALESCE(SUM(amountallocated),0) as amountallocated, 
				COALESCE(SUM(amountdeducted),0) as amountdeducted
			FROM (
			-- Concept Allocation and Deduction Before Date
			SELECT 
				pyr20.amn_employee_id,
				cty20.amn_concept_types_id,
				currencyConvert(pyr_d20.amountallocated, pyr20.c_currency_id, p_currency_id , pyr20.dateacct, p_conversiontype_id, pyr20.ad_client_id,  pyr20.ad_org_id) as amountallocated, 
				currencyConvert(pyr_d20.amountdeducted, pyr20.c_currency_id, p_currency_id , pyr20.dateacct, p_conversiontype_id, pyr20.ad_client_id,  pyr20.ad_org_id) as amountdeducted

			FROM AMN_Payroll as pyr20
			LEFT JOIN AMN_Payroll_detail 		as pyr_d20 ON (pyr_d20.amn_payroll_id= pyr20.amn_payroll_id)
			LEFT JOIN AMN_Concept_types_proc as ctp20 	 ON (ctp20.amn_concept_types_proc_id= pyr_d20.amn_concept_types_proc_id)
			LEFT JOIN AMN_Concept_types 			as cty20 	 ON (cty20.amn_concept_types_id= ctp20.amn_concept_types_id)
			LEFT JOIN AMN_Process as proc20 ON (proc20.AMN_Process_ID = ctp20.AMN_Process_ID)
			LEFT JOIN AMN_Concept_Types_Limit as ctyli20 ON (ctyli20.AMN_Concept_Types_ID = cty20.AMN_Concept_Types_ID)

			WHERE ctyli20.AMN_Period_status <> 'C'
				AND pyr20.invdateend <= p_perioddate
			GROUP BY pyr20.amn_employee_id, cty20.amn_concept_types_id, pyr_d20.amountallocated, pyr_d20.amountdeducted , 
				pyr20.dateacct, pyr20.c_currency_id, pyr20.ad_client_id,  pyr20.ad_org_id

			) as alloc
			GROUP BY amn_employee_id, amn_concept_types_id
		) alloc ON (alloc.amn_employee_id = empl2.amn_employee_id AND alloc.amn_concept_types_id = coty2.amn_concept_types_id)
		WHERE proc2.value='TO' AND ctyli2.AMN_Period_status <> 'C'
	) as tocty
	WHERE tocty.amn_employee_id = p_employee_id
	GROUP BY tocty.amn_employee_id ;
	
RETURN	v_TOMessage;
END;	$function$
;

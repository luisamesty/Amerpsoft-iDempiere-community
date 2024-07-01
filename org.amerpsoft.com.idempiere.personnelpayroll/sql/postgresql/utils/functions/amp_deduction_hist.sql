--DROP FUNCTION IF EXISTS adempiere.amp_deduction_hist(character varying, numeric, timestamp without time zone, timestamp without time zone, numeric, numeric);

CREATE OR REPLACE FUNCTION adempiere.amp_deduction_hist (
   IN deduction_concept   CHARACTER VARYING,
   IN p_employee_id   numeric,
   IN p_startdate     timestamp WITHOUT TIME ZONE,
   IN p_enddate       timestamp WITHOUT TIME ZONE,
   IN p_currency_id numeric, 
   IN p_conversiontype_id numeric)
   RETURNS numeric
AS
$BODY$

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
$BODY$
   LANGUAGE plpgsql
   VOLATILE
   COST 100;

ALTER FUNCTION adempiere.amp_deduction_hist(CHARACTER VARYING,numeric,timestamp WITHOUT TIME ZONE,timestamp WITHOUT TIME ZONE, numeric, numeric) OWNER TO adempiere
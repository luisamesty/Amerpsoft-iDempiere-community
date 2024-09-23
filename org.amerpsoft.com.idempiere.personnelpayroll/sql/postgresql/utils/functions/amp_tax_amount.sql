-- Function: amp_tax_amount(numeric, numeric, numeric, numeric, numeric)
-- USED ON : WithholdingTax.jrxml , WithHoldingTaxSeniat.jrxml
-- DROP FUNCTION amp_tax_amount(numeric, numeric, numeric, numeric, numeric);

CREATE OR REPLACE FUNCTION amp_tax_amount(
	p_employee_id numeric, 
	p_aperiod_id numeric, 
	p_cperiod_id numeric, 
	p_currency_id numeric, 
	p_conversiontype_id numeric)
  RETURNS numeric AS
$BODY$

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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION amp_tax_amount(numeric, numeric, numeric, numeric, numeric)
  OWNER TO adempiere;

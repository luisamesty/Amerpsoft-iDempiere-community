-- Function: amp_to_balanceall(numeric, timestamp without time zone, numeric, numeric)
-- Used on Report PayrollReceiptTO.jrxml

-- DROP FUNCTION amp_to_balanceall(numeric, timestamp without time zone, numeric, numeric);

CREATE OR REPLACE FUNCTION amp_to_balanceall(
	p_employee_id numeric, 
	p_perioddate timestamp without time zone,
	p_currency_id numeric, 
	p_conversiontype_id numeric)
  RETURNS character varying AS
$BODY$
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
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION amp_to_balanceall(numeric, timestamp without time zone, numeric, numeric)
  OWNER TO adempiere;

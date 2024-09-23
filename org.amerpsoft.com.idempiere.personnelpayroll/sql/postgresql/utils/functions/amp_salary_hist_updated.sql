-- Function: adempiere.amp_salary_hist_updated( numeric, timestamp without time zone, timestamp without time zone)
-- Used onClass AMNPayrollRefresh.java 
-- Added C_Currency_ID C_ConversionType_ID C_ConversionParameter 

DROP FUNCTION IF EXISTS adempiere.amp_salary_hist_updated( numeric, timestamp without time zone, timestamp without time zone, numeric, numeric );

CREATE OR REPLACE FUNCTION adempiere.amp_salary_hist_updated(
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

SELECT sum(salary_socialbenefits_updated) into v_returnSalary
  FROM (
  SELECT 
  amn_employee_id, validfrom, validto, currencyConvert(salary_socialbenefits_updated, c_currency_id, p_currency_id , validto, p_conversiontype_id, ad_client_id,  ad_org_id)
  as salary_socialbenefits_updated
  FROM adempiere.amn_payroll_historic 

  ) as prh
  WHERE prh.amn_employee_id = p_employee_id AND prh.validfrom >= p_startdate AND prh.validto <= p_enddate ;
  
RETURN	v_returnSalary;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amp_salary_hist_updated( numeric, timestamp without time zone, timestamp without time zone, numeric,numeric)
  OWNER TO adempiere;

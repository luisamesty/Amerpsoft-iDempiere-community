-- View: adempiere.amn_employee_salary_hist_byfunction_v
-- Used on PayrollSummaryOnePeriod.jrxml, PayrollSummaryReport.jrxml

DROP VIEW IF EXISTS adempiere.amn_employee_salary_hist_byfunction_v;

CREATE OR REPLACE VIEW adempiere.amn_employee_salary_hist_byfunction_v AS 
 SELECT emp.amn_employee_id * 10::numeric AS amn_employee_salary_hist_v_id, 
        emp.ad_client_id, emp.ad_org_id, curr.c_currency_id, sch.c_acctschema_id,
        emp.created, emp.createdby, emp.updated, emp.updatedby, emp.isactive, emp.value, 
        emp.name, emp.amn_employee_id, to_char(cpe.startdate, 'YYYY-MM'::text) AS amn_period_yyyymm, cpe.startdate, cpe.enddate, 
        adempiere.amp_salary_hist_calc('salary_base'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_base, 
        adempiere.amp_salary_hist_calc('salary_integral'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_integral, 
        adempiere.amp_salary_hist_calc('salary_gravable'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_gravable, 
        adempiere.amp_salary_hist_calc('salary_vacation'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_vacation, 
        adempiere.amp_salary_hist_calc('salary_utilities'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_utilities, 
        adempiere.amp_salary_hist_calc('salary_utilities_nn'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_utilities_nn, 
        adempiere.amp_salary_hist_calc('salary_utilities_nv'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_utilities_nv, 
        adempiere.amp_salary_hist_calc('salary_socialbenefits'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_socialbenefits, 
        adempiere.amp_salary_hist_calc('salary_socialbenefits_nn'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_socialbenefits_nn, 
        adempiere.amp_salary_hist_calc('salary_socialbenefits_nv'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_socialbenefits_nv, 
        adempiere.amp_salary_hist_calc('salary_socialbenefits_nu'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114) AS salary_socialbenefits_nu
   FROM adempiere.amn_employee emp
   LEFT JOIN adempiere.c_period cpe ON cpe.ad_client_id = emp.ad_client_id
    LEFT JOIN adempiere.c_acctschema sch ON sch.ad_client_id = emp.ad_client_id
    LEFT JOIN adempiere.c_currency curr ON curr.c_currency_id = sch.c_currency_id

  ORDER BY emp.amn_employee_id, to_char(cpe.startdate, 'YYYY-MM'::text) DESC;

ALTER TABLE adempiere.amn_employee_salary_hist_byfunction_v
  OWNER TO postgres;

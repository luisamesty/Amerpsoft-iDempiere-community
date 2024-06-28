DROP VIEW IF EXISTS adempiere.amn_employee_salary_hist_byfunction_v;
DROP FUNCTION IF EXISTS adempiere.amp_salary_hist_calc(character varying, numeric, timestamp without time zone, timestamp without time zone);
DROP FUNCTION IF EXISTS adempiere.amp_deduction_hist(character varying, numeric, timestamp without time zone, timestamp without time zone);

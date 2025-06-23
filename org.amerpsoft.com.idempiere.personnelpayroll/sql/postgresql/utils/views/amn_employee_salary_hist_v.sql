-- View: adempiere.amn_employee_salary_hist_v
-- Used on Class MAMN_Payroll_Historic
-- adempiere.amn_employee_salary_hist_v source
-- UPDATED Junio 2025 (Duplicity due to conversiontype nulls)
-- agregada la columna salary

DROP VIEW IF EXISTS adempiere.amn_employee_salary_hist_v;

CREATE OR REPLACE VIEW adempiere.amn_employee_salary_hist_v
AS SELECT
	periodosemp.amn_employee_id * 10::NUMERIC AS amn_employee_salary_hist_v_id,
	periodosemp.c_acctschema_id,
	periodosemp.cu_currency_id,
	periodosemp.pr_currency_id,
    periodosemp.c_conversiontype_id::NUMERIC(10,0),
	periodosemp.startdate,
	periodosemp.enddate,
	periodosemp.ad_client_id,
	periodosemp.ad_org_id,
	periodosemp.created,
	periodosemp.createdby,
	periodosemp.updated,
	periodosemp.updatedby,
	periodosemp.isactive,
	periodosemp.value,
	periodosemp.name,
	periodosemp.amn_employee_id,
	periodosemp.amn_period_yyyymm,
	periodosemp.salary,
	periodosemp.salary_base,
	periodosemp.salary_integral,
	periodosemp.salary_gravable,
	periodosemp.salary_vacation,
	periodosemp.salary_utilities,
	periodosemp.salary_utilities_nn,
	periodosemp.salary_utilities_nv,
	periodosemp.salary_socialbenefits,
	periodosemp.salary_socialbenefits_nn,
	periodosemp.salary_socialbenefits_nv,
	periodosemp.salary_socialbenefits_nu
FROM
	(
	SELECT
		periodos.ad_client_id,
		periodos.c_acctschema_id,
		periodos.pr_currency_id,
		periodos.cu_currency_id,
        periodos.c_conversiontype_id,
		periodos.startdate,
		periodos.enddate,
		periodos.ad_org_id,
		periodos.created,
		periodos.createdby,
		periodos.updated,
		periodos.updatedby,
		periodos.isactive,
		periodos.value,
		periodos.name,
		periodos.employee_id AS amn_employee_id,
		periodos.yymm AS amn_period_yyyymm,
		salary,
		sum(periodos.asignadoba) AS salary_base,
		sum(periodos.asignadonn) AS salary_integral,
		sum(periodos.asignadogr) AS salary_gravable,
		sum(periodos.asignadonv) AS salary_vacation,
		sum(periodos.asignadonu) AS salary_utilities,
		sum(periodos.asignadonu_nn) AS salary_utilities_nn,
		sum(periodos.asignadonu_nv) AS salary_utilities_nv,
		sum(periodos.asignadonp) AS salary_socialbenefits,
		sum(periodos.asignadonp_nn) AS salary_socialbenefits_nn,
		sum(periodos.asignadonp_nv) AS salary_socialbenefits_nv,
		sum(periodos.asignadonp_nu) AS salary_socialbenefits_nu
	FROM
		(
		SELECT
			emp.ad_client_id,
			emp.ad_org_id,
			emp.created,
			emp.createdby,
			emp.updated,
			emp.updatedby,
			emp.isactive,
			emp.value,
			emp.name,
			emp.amn_employee_id AS employee_id,
			pyr_ds.salary,
			pyr_p.amn_period_id AS period_id,
			cper.startdate AS period_ini,
			cper.enddate AS period_end,
			to_char(cper.startdate, 'YYYY-MM'::text) AS yymm,
			sch.c_acctschema_id,
			pyr.c_currency_id AS pr_currency_id,
			curr.c_currency_id AS cu_currency_id,
			pyr.c_conversiontype_id,
			pyr.amn_payroll_id,
			pyr.invdateini AS fecha_inicio,
			pyr.invdateend AS fecha_fin,
			pyr_d.qtyvalue,
			CASE
				WHEN con_ty.salario = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadoba,
			CASE
				WHEN (con_ty.salario = 'Y'::bpchar
				OR con_ty.utilidad = 'Y'::bpchar
				OR con_ty.vacacion = 'Y'::bpchar)
				AND (pro.amn_process_value::text = 'NN'::text
				OR pro.amn_process_value::text = 'NV'::text
				OR pro.amn_process_value::text = 'NU'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonn,
			CASE
				WHEN con_ty.arc = 'Y'::bpchar
				AND (pro.amn_process_value::text = 'NN'::text
				OR pro.amn_process_value::text = 'NV'::text
				OR pro.amn_process_value::text = 'NU'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadogr,
			CASE
				WHEN con_ty.vacacion = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonv,
			CASE
				WHEN con_ty.utilidad = 'Y'::bpchar
				AND (pro.amn_process_value::text = 'NN'::text
				OR pro.amn_process_value::text = 'NV'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonu,
			CASE
				WHEN con_ty.utilidad = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonu_nn,
			CASE
				WHEN con_ty.utilidad = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NV'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonu_nv,
			CASE
				WHEN con_ty.prestacion = 'Y'::bpchar
				AND (pro.amn_process_value::text = 'NN'::text
				OR pro.amn_process_value::text = 'NV'::text
				OR pro.amn_process_value::text = 'NU'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonp,
			CASE
				WHEN con_ty.prestacion = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonp_nn,
			CASE
				WHEN con_ty.prestacion = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NV'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonp_nv,
			CASE
				WHEN con_ty.prestacion = 'Y'::bpchar
				AND pro.amn_process_value::text = 'NU'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct, 114::NUMERIC, pyr.ad_client_id, pyr.ad_org_id)
				ELSE 0::NUMERIC
			END AS asignadonp_nu,
			con_ty.value AS concept_value,
			con_ty.salario,
			con_ty.utilidad,
			con_ty.vacacion,
			con_ty.prestacion,
			pro.amn_process_value,
			cper.startdate,
			cper.enddate
		FROM ( 
			SELECT 
			ad_client_id, ad_org_id,
			amn_payroll_id,
			amn_period_id,
			amn_employee_id,
			COALESCE(c_conversiontype_id,CAST(114 AS NUMERIC)) AS c_conversiontype_id,
			c_currency_id,
			invdateini,
			invdateend,
			dateacct,
			amountallocated
			FROM amn_payroll
		)	as pyr
		INNER JOIN (
			SELECT 
			    p.amn_payroll_id,
			    COALESCE(pd.salary, 0) AS salary
			FROM (
			    SELECT p.*
			    FROM amn_payroll p
			    INNER JOIN amn_process pr ON p.amn_process_id = pr.amn_process_id
			    WHERE pr.amn_process_value = 'NN'
			) p
			LEFT JOIN (
			    SELECT 
			        amn_payroll_id,
			        amountcalculated AS salary
			    FROM (
			        SELECT 
			            amn_payroll_id,
			            amountcalculated,
			            ROW_NUMBER() OVER (PARTITION BY amn_payroll_id ORDER BY calcorder ASC) AS rn
			        FROM amn_payroll_detail
			    ) sub
			    WHERE rn = 1
			) pd ON p.amn_payroll_id = pd.amn_payroll_id
		) AS pyr_ds ON pyr.amn_payroll_id = pyr_ds.amn_payroll_id
		INNER JOIN amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
		INNER JOIN amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
		INNER JOIN amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
		INNER JOIN amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
		INNER JOIN amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
		INNER JOIN amn_process pro ON pro.amn_process_id = pyr_p.amn_process_id
		INNER JOIN c_period cper ON cper.c_period_id = pyr_p.c_period_id
		INNER JOIN c_acctschema sch ON sch.ad_client_id = pyr.ad_client_id
		INNER JOIN c_currency curr ON curr.c_currency_id = sch.c_currency_id
		ORDER BY pyr_p.amndateini
		) periodos
	GROUP BY 
		periodos.employee_id,
		periodos.salary,
		periodos.yymm,
		periodos.startdate,
		periodos.enddate,
		periodos.ad_client_id,
		periodos.ad_org_id,
		periodos.created,
		periodos.createdby,
		periodos.updated,
		periodos.updatedby,
		periodos.isactive,
		periodos.value,
		periodos.name,
		periodos.cu_currency_id,
		periodos.pr_currency_id,
		periodos.c_conversiontype_id,
		periodos.c_acctschema_id
	ORDER BY
		periodos.employee_id,
		periodos.startdate DESC) periodosemp;

ALTER TABLE adempiere.amn_employee_salary_hist_v
  OWNER TO postgres;


-- adempiere.amn_bpartner_accounts_v source

CREATE OR REPLACE VIEW adempiere.amn_bpartner_accounts_v
AS SELECT bpacct00.ad_client_id,
    bpacct00.c_bpartner_id,
    bpacct00.c_elementvalue_id,
    bpacct00.value,
    bpacct00.name
   FROM ( SELECT DISTINCT ON (bp_acct.ad_client_id, bp_acct.c_elementvalue_id) bp_acct.ad_client_id,
            bp_acct.c_bpartner_id,
            bp_acct.c_elementvalue_id,
            bp_acct.value,
            bp_acct.name
           FROM ((
                        (
                                 SELECT cbpac.ad_client_id,
                                    cbpac.c_bpartner_id,
                                    cev.value,
                                    cev.name,
                                    cvaco.account_id AS c_elementvalue_id
                                   FROM c_bpartner cbpac
                                     LEFT JOIN c_bp_customer_acct cbpcu ON cbpcu.c_bpartner_id = cbpac.c_bpartner_id
                                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpcu.c_receivable_acct
                                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                                UNION ALL
                                 SELECT cbpac.ad_client_id,
                                    cbpac.c_bpartner_id,
                                    cev.value,
                                    cev.name,
                                    cvaco.account_id AS c_elementvalue_id
                                   FROM c_bpartner cbpac
                                     LEFT JOIN c_bp_customer_acct cbpcu ON cbpcu.c_bpartner_id = cbpac.c_bpartner_id
                                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpcu.c_prepayment_acct
                                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                        ) UNION
                         SELECT cbpac.ad_client_id,
                            cbpac.c_bpartner_id,
                            cev.value,
                            cev.name,
                            cvaco.account_id AS c_elementvalue_id
                           FROM c_bpartner cbpac
                             LEFT JOIN c_bp_customer_acct cbpcu ON cbpcu.c_bpartner_id = cbpac.c_bpartner_id
                             LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpcu.c_receivable_services_acct
                             LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                ) UNION ALL
                 SELECT cbpac.ad_client_id,
                    cbpac.c_bpartner_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM c_bpartner cbpac
                     LEFT JOIN c_bp_vendor_acct cbpve ON cbpve.c_bpartner_id = cbpac.c_bpartner_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.v_liability_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT cbpac.ad_client_id,
                    cbpac.c_bpartner_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM c_bpartner cbpac
                     LEFT JOIN c_bp_vendor_acct cbpve ON cbpve.c_bpartner_id = cbpac.c_bpartner_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.v_liability_services_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT cbpac.ad_client_id,
                    cbpac.c_bpartner_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM c_bpartner cbpac
                     LEFT JOIN c_bp_vendor_acct cbpve ON cbpve.c_bpartner_id = cbpac.c_bpartner_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.v_prepayment_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id) bp_acct) bpacct00
UNION ALL
 SELECT bp_acct01.ad_client_id,
    bp_acct01.c_bpartner_id,
    bp_acct01.c_elementvalue_id,
    bp_acct01.value,
    bp_acct01.name
   FROM ( SELECT DISTINCT ON (bp_acct1.ad_client_id, bp_acct1.c_elementvalue_id) bp_acct1.ad_client_id,
            cbpa12.c_bpartner_id,
            bp_acct1.c_elementvalue_id,
            bp_acct1.value,
            bp_acct1.name
           FROM ( SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_cre_dw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_deb_dw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_cre_iw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_deb_iw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_cre_mw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_deb_mw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_cre_sw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id
                UNION ALL
                 SELECT DISTINCT ON (adcli.ad_client_id, cev.c_elementvalue_id) adcli.ad_client_id,
                    cev.value,
                    cev.name,
                    cvaco.account_id AS c_elementvalue_id
                   FROM ad_client adcli
                     LEFT JOIN amn_concept_types_acct cbpve ON cbpve.ad_client_id = adcli.ad_client_id
                     LEFT JOIN c_validcombination cvaco ON cvaco.c_validcombination_id = cbpve.amn_deb_sw_acct
                     LEFT JOIN c_elementvalue cev ON cev.c_elementvalue_id = cvaco.account_id) bp_acct1
             LEFT JOIN ( SELECT cbpa1.ad_client_id,
                    cbpa1.c_bpartner_id
                   FROM c_bpartner cbpa1
                  WHERE cbpa1.isemployee = 'Y'::bpchar) cbpa12 ON cbpa12.ad_client_id = bp_acct1.ad_client_id) bp_acct01;


-- adempiere.amn_employee_salary_hist_byfunction_v source

CREATE OR REPLACE VIEW adempiere.amn_employee_salary_hist_byfunction_v
AS SELECT emp.amn_employee_id * 10::numeric AS amn_employee_salary_hist_v_id,
    emp.ad_client_id,
    emp.ad_org_id,
    curr.c_currency_id,
    sch.c_acctschema_id,
    emp.created,
    emp.createdby,
    emp.updated,
    emp.updatedby,
    emp.isactive,
    emp.value,
    emp.name,
    emp.amn_employee_id,
    to_char(cpe.startdate, 'YYYY-MM'::text) AS amn_period_yyyymm,
    cpe.startdate,
    cpe.enddate,
    amp_salary_hist_calc('salary_base'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_base,
    amp_salary_hist_calc('salary_integral'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_integral,
    amp_salary_hist_calc('salary_gravable'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_gravable,
    amp_salary_hist_calc('salary_vacation'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_vacation,
    amp_salary_hist_calc('salary_utilities'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_utilities,
    amp_salary_hist_calc('salary_utilities_nn'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_utilities_nn,
    amp_salary_hist_calc('salary_utilities_nv'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_utilities_nv,
    amp_salary_hist_calc('salary_socialbenefits'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_socialbenefits,
    amp_salary_hist_calc('salary_socialbenefits_nn'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_socialbenefits_nn,
    amp_salary_hist_calc('salary_socialbenefits_nv'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_socialbenefits_nv,
    amp_salary_hist_calc('salary_socialbenefits_nu'::character varying, emp.amn_employee_id, cpe.startdate, cpe.enddate, curr.c_currency_id, 114::numeric) AS salary_socialbenefits_nu
   FROM amn_employee emp
     LEFT JOIN c_period cpe ON cpe.ad_client_id = emp.ad_client_id
     LEFT JOIN c_acctschema sch ON sch.ad_client_id = emp.ad_client_id
     LEFT JOIN c_currency curr ON curr.c_currency_id = sch.c_currency_id
  ORDER BY emp.amn_employee_id, (to_char(cpe.startdate, 'YYYY-MM'::text)) DESC;


-- adempiere.amn_employee_salary_hist_v source

CREATE OR REPLACE VIEW adempiere.amn_employee_salary_hist_v
AS SELECT periodosemp.amn_employee_id * 10::numeric AS amn_employee_salary_hist_v_id,
    periodosemp.c_acctschema_id,
    periodosemp.cu_currency_id,
    periodosemp.pr_currency_id,
    periodosemp.c_conversiontype_id,
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
   FROM ( SELECT periodos.ad_client_id,
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
           FROM ( SELECT emp.ad_client_id,
                    emp.ad_org_id,
                    emp.created,
                    emp.createdby,
                    emp.updated,
                    emp.updatedby,
                    emp.isactive,
                    emp.value,
                    emp.name,
                    emp.amn_employee_id AS employee_id,
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
                            WHEN con_ty.salario = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadoba,
                        CASE
                            WHEN (con_ty.salario = 'Y'::bpchar OR con_ty.utilidad = 'Y'::bpchar OR con_ty.vacacion = 'Y'::bpchar) AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonn,
                        CASE
                            WHEN con_ty.arc = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadogr,
                        CASE
                            WHEN con_ty.vacacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonv,
                        CASE
                            WHEN con_ty.utilidad = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonu,
                        CASE
                            WHEN con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonu_nn,
                        CASE
                            WHEN con_ty.utilidad = 'Y'::bpchar AND pro.amn_process_value::text = 'NV'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonu_nv,
                        CASE
                            WHEN con_ty.prestacion = 'Y'::bpchar AND (pro.amn_process_value::text = 'NN'::text OR pro.amn_process_value::text = 'NV'::text OR pro.amn_process_value::text = 'NU'::text) THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonp,
                        CASE
                            WHEN con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NN'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonp_nn,
                        CASE
                            WHEN con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NV'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonp_nv,
                        CASE
                            WHEN con_ty.prestacion = 'Y'::bpchar AND pro.amn_process_value::text = 'NU'::text THEN currencyconvert(pyr_d.amountallocated, pyr.c_currency_id, curr.c_currency_id, pyr.dateacct::timestamp with time zone, 114::numeric, pyr.ad_client_id, pyr.ad_org_id)
                            ELSE 0::numeric
                        END AS asignadonp_nu,
                    con_ty.value AS concept_value,
                    con_ty.salario,
                    con_ty.utilidad,
                    con_ty.vacacion,
                    con_ty.prestacion,
                    pro.amn_process_value,
                    cper.startdate,
                    cper.enddate
                   FROM amn_payroll pyr
                     LEFT JOIN amn_payroll_detail pyr_d ON pyr.amn_payroll_id = pyr_d.amn_payroll_id
                     LEFT JOIN amn_concept_types_proc con_tp ON pyr_d.amn_concept_types_proc_id = con_tp.amn_concept_types_proc_id
                     LEFT JOIN amn_concept_types con_ty ON con_tp.amn_concept_types_id = con_ty.amn_concept_types_id
                     LEFT JOIN amn_period pyr_p ON pyr.amn_period_id = pyr_p.amn_period_id
                     LEFT JOIN amn_employee emp ON pyr.amn_employee_id = emp.amn_employee_id
                     LEFT JOIN amn_process pro ON pro.amn_process_id = pyr_p.amn_process_id
                     LEFT JOIN c_period cper ON cper.c_period_id = pyr_p.c_period_id
                     LEFT JOIN c_acctschema sch ON sch.ad_client_id = pyr.ad_client_id
                     LEFT JOIN c_currency curr ON curr.c_currency_id = sch.c_currency_id
                  ORDER BY pyr_p.amndateini) periodos
          GROUP BY periodos.employee_id, periodos.yymm, periodos.startdate, periodos.enddate, periodos.ad_client_id, periodos.ad_org_id, periodos.created, periodos.createdby, periodos.updated, periodos.updatedby, periodos.isactive, periodos.value, periodos.name, periodos.cu_currency_id, periodos.pr_currency_id, periodos.c_conversiontype_id, periodos.c_acctschema_id
          ORDER BY periodos.employee_id, periodos.startdate DESC) periodosemp;


-- adempiere.amn_process_contract_v source

CREATE OR REPLACE VIEW adempiere.amn_process_contract_v
AS SELECT pro.amn_process_id + 1000::numeric * amc.amn_contract_id AS amn_process_contract_v_id,
    pro.amn_process_id,
    amc.amn_contract_id,
    pro.ad_client_id,
    amc.ad_org_id,
    pro.created,
    pro.createdby,
    pro.updated,
    pro.updatedby,
    pro.isactive,
    pro.value,
    pro.name,
    pro.description,
    amc.value AS contractvalue,
    amc.name AS contractname,
    amc.description AS contractdescription,
    amc.payrolldays,
    amc.initdow,
    amc.acctdow
   FROM amn_process pro,
    amn_contract amc
  ORDER BY pro.value, amc.value;


-- adempiere.amn_role_access_contract_v source

CREATE OR REPLACE VIEW adempiere.amn_role_access_contract_v
AS SELECT DISTINCT amco.amn_contract_id AS amn_role_access_contract_v_id,
    amra.ad_role_id,
    amco.ad_client_id,
    amco.ad_org_id,
    amco.created,
    amco.createdby,
    amco.updated,
    amco.updatedby,
    amco.isactive,
    amco.amn_contract_id,
    amco.value,
    amco.name,
    amco.description,
    amra.amn_process_id
   FROM amn_contract amco
     LEFT JOIN amn_role_access amra ON amco.amn_contract_id = amra.amn_contract_id;


-- adempiere.amn_role_access_proc_v source

CREATE OR REPLACE VIEW adempiere.amn_role_access_proc_v
AS SELECT DISTINCT ampr.amn_process_id AS amn_role_access_proc_v_id,
    amra.ad_role_id,
    ampr.ad_client_id,
    ampr.ad_org_id,
    ampr.created,
    ampr.createdby,
    ampr.updated,
    ampr.updatedby,
    ampr.isactive,
    ampr.amn_process_id,
    ampr.amn_process_value,
    ampr.value,
    ampr.name,
    ampr.description
   FROM amn_process ampr
     LEFT JOIN amn_role_access amra ON ampr.amn_process_id = amra.amn_process_id;

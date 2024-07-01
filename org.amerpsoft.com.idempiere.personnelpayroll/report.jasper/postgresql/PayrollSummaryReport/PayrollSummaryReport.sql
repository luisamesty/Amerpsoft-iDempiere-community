-- PAyroll Summary Report
-- Summary
SELECT * FROM
(
	-- REPORT HEADER
	SELECT DISTINCT ON (cli.ad_client_id)
		-- Client
		cli.ad_client_id as rep_client_id,
		-- Organization
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 0 ELSE $P{AD_Org_ID} END as rep_org_id,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as rep_description, 
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
		CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
	    CASE WHEN  org.ad_client_id = $P{AD_Client_ID}   AND ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN 1
	             WHEN  org.ad_client_id = $P{AD_Client_ID}  AND org.ad_org_id= $P{AD_Org_ID}  THEN 1
	             ELSE 0 END as imp_header
	FROM adempiere.ad_client as cli
		 INNER JOIN adempiere.ad_org as org ON (org.ad_client_id = cli.ad_client_id)
		 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
		  LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
		 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
		  LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	WHERE cli.ad_client_id = $P{AD_Client_ID} AND CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR org.ad_org_id = $P{AD_Org_ID} ) THEN 1 = 1 ELSE 1 = 0 END
) as header_info
FULL JOIN
(
	SELECT DISTINCT
    -- ORGANIZATION
       hist.ad_client_id as client_id, hist.ad_org_id as org_id,
	-- LOCATION
	   lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
   -- CONTRACT
	   amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
    -- CURRENCY
       curr2.iso_code as iso_code2,
       currt2.cursymbol as cursymbol2,
       COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
   -- EMPLOYEE
	   emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso,
	   (DATE_PART('year', CURRENT_TIMESTAMP) - DATE_PART('year', emp.incomedate)) as a_servicio,
       cbp.taxid as nro_id, emp.salary as salario,
   -- DEPARTAMENT
       COALESCE(dep.name,dep.description) as departamento,
   -- DEDUCTION_HIST
        adempiere.amp_deduction_hist ('ISLR', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS islr,
        adempiere.amp_deduction_hist ('SSO_T', emp.amn_employee_id, hist.startdate, hist.enddate,$P{C_Currency_ID}, NULL )  AS sso,
        adempiere.amp_deduction_hist ('SSO_P', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS sso_p,
        adempiere.amp_deduction_hist ('SPF_T', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS spf,
        adempiere.amp_deduction_hist ('SPF_P', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS spf_p,
        adempiere.amp_deduction_hist ('FAOV_T', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS faov,
        adempiere.amp_deduction_hist ('FAOV_P', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS faov_p,
        adempiere.amp_deduction_hist ('INCES_T', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS inces,
        adempiere.amp_deduction_hist ('INCES_P', emp.amn_employee_id, hist.startdate, hist.enddate, $P{C_Currency_ID}, NULL )  AS inces_p,
   -- SALARY_HIST
	   currencyConvert(hist.salary_utilities,hist.pr_currency_id, $P{C_Currency_ID}, hist.enddate, NULL, hist.AD_Client_ID, hist.AD_Org_ID ) as salary_utilities, 
	   currencyConvert(hist.salary_socialbenefits,hist.pr_currency_id, $P{C_Currency_ID}, hist.enddate, NULL, hist.AD_Client_ID, hist.AD_Org_ID ) as salary_socialbenefits, 
	   currencyConvert(hist.salary_vacation,hist.pr_currency_id, $P{C_Currency_ID}, hist.enddate, NULL, hist.AD_Client_ID, hist.AD_Org_ID ) as salary_vacation, 
	   currencyConvert(hist.salary_integral,hist.pr_currency_id, $P{C_Currency_ID}, hist.enddate, NULL, hist.AD_Client_ID, hist.AD_Org_ID ) as salary_integral, 
	   currencyConvert(hist.salary_gravable,hist.pr_currency_id, $P{C_Currency_ID}, hist.enddate, NULL, hist.AD_Client_ID, hist.AD_Org_ID ) as salary_gravable, 
		   hist.amn_period_yyyymm,hist.startdate, hist.enddate
	FROM adempiere.amn_employee as emp
 	 --LEFT JOIN amn_employee_salary_hist_byfunction_v as hist ON (hist.amn_employee_id= emp.amn_employee_id)
	 LEFT JOIN amn_employee_salary_hist_v as hist ON (hist.amn_employee_id= emp.amn_employee_id)
	 LEFT JOIN c_bpartner as cbp	 ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	 LEFT JOIN amn_location as lct ON (emp.amn_location_id= lct.amn_location_id)
	 LEFT JOIN amn_contract as amc ON (emp.amn_contract_id= amc.amn_contract_id) 
	 LEFT JOIN amn_department as dep ON (emp.amn_department_id = dep.amn_department_id)
	 LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
     LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	 WHERE emp.AD_Client_ID=$P{AD_Client_ID}
		AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Location_ID}  IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
		AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
  ) as hist ON (1= 0)
 WHERE (imp_header= 1) OR (client_id= $P{AD_Client_ID}
  AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
      AND startdate BETWEEN DATE( $P{DateIni} ) AND DATE( $P{DateEnd} ))
ORDER BY hist.value_emp, hist.startdate, header_info ASC
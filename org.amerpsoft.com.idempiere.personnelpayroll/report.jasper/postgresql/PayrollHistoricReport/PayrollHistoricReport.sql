-- PayrollHistoricReport
-- Payroll Historic Report by Employees
-- FILTER BY AllocationsOnly if Yes
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
	SELECT 
    -- ORGANIZATION
       pyr.ad_client_id as client_id, pyr.ad_org_id as org_id,
	-- LOCATION
	   lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
   -- CONTRACT
	   amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
   -- PROCESS
	   prc.value as prc_value, prc.amn_process_id, COALESCE(prc.name, prc.description) as proceso,
   -- TIPO DE CONCEPTO
      cty.utilidad, cty.prestacion, cty.vacacion, cty.arc, cty.optmode, cty.ince, cty.sso, cty.spf, cty.faov, 
	   cty.amn_concept_types_id, cty.calcorder, cty.isshow, cty.value as cty_value, COALESCE(cty.name, cty.description) as concept_type,
   -- TIPO DE CONCEPTO (PROCESO)
	   ctp.value as ctp_value, COALESCE(ctp.name, ctp.description) as concept_type_process, 	 
   -- AMN_PERIOD
	   prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
   -- C_PERIOD
       c_prd.c_period_id, c_prd.startdate, c_prd.enddate,
   -- C_Period TITLES
       ptit.c_period_id01, ptit.per01,  ptit.c_period_id02, ptit.per02,  ptit.c_period_id03, ptit.per03,
       ptit.c_period_id04, ptit.per04,  ptit.c_period_id05, ptit.per05,  ptit.c_period_id06, ptit.per06,
       ptit.c_period_id07, ptit.per07,  ptit.c_period_id08, ptit.per08,  ptit.c_period_id09, ptit.per09,
       ptit.c_period_id10, ptit.per10,  ptit.c_period_id11, ptit.per11,  ptit.c_period_id12, ptit.per12,
   -- EMPLOYEE
	   emp.amn_employee_id, emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso, 
	   emp.status as status_emp, emp.isactive as active_emp,
	   (DATE_PART('year', CURRENT_TIMESTAMP) - DATE_PART('year', emp.incomedate)) as a_servicio,
       COALESCE(jtt.name, jtt.description) as cargo, cbp.taxid as nro_id, emp.salary as salario,
   -- DEPARTAMENT
       COALESCE(dep.name,dep.description) as departamento,
   -- CURRENCY
	   curr1.iso_code as iso_code1,
	   currt1.cursymbol as cursymbol1,
	   COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
       curr2.iso_code as iso_code2,
	   currt2.cursymbol as cursymbol2,
	   COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
   -- PAYROLL
	   pyr.amn_payroll_id, 
	  -- pyr.amountallocated as amountallocated_t, 
	  -- pyr.amountdeducted as amountdeducted_t, 
	  -- pyr.amountcalculated as amountcalculated_t,
	   pyr.invdateini, pyr.invdateend,
   -- PAYROLL DETAIL
	   pyr_d.value as detail_value, 
	   qtyvalue as cantidad, 
	   currencyConvert(pyr_d.amountallocated, pyr.c_currency_id,$P{C_Currency_ID},pyr.dateacct,pyr.C_ConversionType_ID,pyr.ad_client_id,pyr.ad_org_id) as amountallocated,
	   currencyConvert(pyr_d.amountdeducted, pyr.c_currency_id,$P{C_Currency_ID},pyr.dateacct,pyr.C_ConversionType_ID,pyr.ad_client_id,pyr.ad_org_id) as amountdeducted,
	   currencyConvert(pyr_d.amountcalculated, pyr.c_currency_id,$P{C_Currency_ID},pyr.dateacct,pyr.C_ConversionType_ID,pyr.ad_client_id,pyr.ad_org_id) as amountcalculated
	FROM adempiere.amn_payroll as pyr
	INNER JOIN adempiere.amn_payroll_detail as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
	INNER JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
	INNER JOIN adempiere.amn_concept_types	as cty ON ((cty.amn_concept_types_id= ctp.amn_concept_types_id))
	INNER JOIN adempiere.amn_process as prc ON (prc.amn_process_id= ctp.amn_process_id)
	INNER JOIN adempiere.amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
	 LEFT JOIN adempiere.amn_department as dep ON (emp.amn_department_id = dep.amn_department_id)
	 LEFT JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
	 LEFT JOIN adempiere.c_bpartner 	as cbp	 ON (emp.c_bpartner_id= cbp.c_bpartner_id)
	 LEFT JOIN adempiere.amn_period as prd ON (prd.amn_period_id= pyr.amn_period_id)
	 LEFT JOIN adempiere.c_period as c_prd ON (c_prd.c_period_id= prd.c_period_id)
	 LEFT JOIN ( 
	 	SELECT DISTINCT ON (per2.AD_Client_ID) 
		per2.AD_Client_ID,
		MAX(per2.c_period_id01) as c_period_id01, MAX(per2.periodtxt01) as per01,  
		MAX(per2.c_period_id02) as c_period_id02, MAX(per2.periodtxt02) as per02,
		MAX(per2.c_period_id03) as c_period_id03, MAX(per2.periodtxt03) as per03,
		MAX(per2.c_period_id04) as c_period_id04, MAX(per2.periodtxt04) as per04,
		MAX(per2.c_period_id05) as c_period_id05, MAX(per2.periodtxt05) as per05,
		MAX(per2.c_period_id06) as c_period_id06, MAX(per2.periodtxt06) as per06,
		MAX(per2.c_period_id07) as c_period_id07, MAX(per2.periodtxt07) as per07,
		MAX(per2.c_period_id08) as c_period_id08, MAX(per2.periodtxt08) as per08,
		MAX(per2.c_period_id09) as c_period_id09, MAX(per2.periodtxt09) as per09,
		MAX(per2.c_period_id10) as c_period_id10, MAX(per2.periodtxt10) as per10,
		MAX(per2.c_period_id11) as c_period_id11, MAX(per2.periodtxt11) as per11,
		MAX(per2.c_period_id12) as c_period_id12, MAX(per2.periodtxt12) as per12
		FROM 
		(SELECT
		cper.AD_Client_ID,
		row_number() over (order by cper.startdate ASC) as rn,
		cper.C_Period_ID , 
		to_char(cper.startdate,'MM-YYYY') as periodtxt,
		CASE WHEN (row_number() over (order by cper.startdate asc))=1 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt01,
		CASE WHEN (row_number() over (order by cper.startdate asc))=2 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt02,
		CASE WHEN (row_number() over (order by cper.startdate asc))=3 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt03,
		CASE WHEN (row_number() over (order by cper.startdate asc))=4 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt04,
		CASE WHEN (row_number() over (order by cper.startdate asc))=5 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt05,
		CASE WHEN (row_number() over (order by cper.startdate asc))=6 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt06,
		CASE WHEN (row_number() over (order by cper.startdate asc))=7 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt07,
		CASE WHEN (row_number() over (order by cper.startdate asc))=8 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt08,
		CASE WHEN (row_number() over (order by cper.startdate asc))=9 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt09,
		CASE WHEN (row_number() over (order by cper.startdate asc))=10 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt10,
		CASE WHEN (row_number() over (order by cper.startdate asc))=11 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt11,
		CASE WHEN (row_number() over (order by cper.startdate asc))=12 THEN to_char(cper.startdate,'MM-YYYY') ELSE '' END as periodtxt12,
		CASE WHEN (row_number() over (order by cper.startdate asc))=1 THEN cper.c_period_id ELSE 0 END as c_period_id01,
		CASE WHEN (row_number() over (order by cper.startdate asc))=2 THEN cper.c_period_id ELSE 0 END as c_period_id02,
		CASE WHEN (row_number() over (order by cper.startdate asc))=3 THEN cper.c_period_id ELSE 0 END as c_period_id03,
		CASE WHEN (row_number() over (order by cper.startdate asc))=4 THEN cper.c_period_id ELSE 0 END as c_period_id04,
		CASE WHEN (row_number() over (order by cper.startdate asc))=5 THEN cper.c_period_id ELSE 0 END as c_period_id05,
		CASE WHEN (row_number() over (order by cper.startdate asc))=6 THEN cper.c_period_id ELSE 0 END as c_period_id06,
		CASE WHEN (row_number() over (order by cper.startdate asc))=7 THEN cper.c_period_id ELSE 0 END as c_period_id07,
		CASE WHEN (row_number() over (order by cper.startdate asc))=8 THEN cper.c_period_id ELSE 0 END as c_period_id08,
		CASE WHEN (row_number() over (order by cper.startdate asc))=9 THEN cper.c_period_id ELSE 0 END as c_period_id09,
		CASE WHEN (row_number() over (order by cper.startdate asc))=10 THEN cper.c_period_id ELSE 0 END as c_period_id10,
		CASE WHEN (row_number() over (order by cper.startdate asc))=11 THEN cper.c_period_id ELSE 0 END as c_period_id11,
		CASE WHEN (row_number() over (order by cper.startdate asc))=12 THEN cper.c_period_id ELSE 0 END as c_period_id12
		FROM C_Period cper WHERE C_Period_ID IN (
		SELECT cpe.C_Period_ID FROM C_Period cpe WHERE cpe.startdate BETWEEN DATE($P{DateIni} ) AND DATE($P{DateEnd} ) 
		 AND cpe.AD_Client_ID= $P{AD_Client_ID}
		ORDER BY cpe.startdate ASC ) 
		ORDER BY rn
		) as per2
		GROUP BY  per2.AD_Client_ID
	 ) as ptit ON (ptit.AD_Client_ID = c_prd.AD_Client_ID)
	 LEFT JOIN adempiere.amn_location as lct ON (emp.amn_location_id= lct.amn_location_id)
	 LEFT JOIN adempiere.amn_contract as amc ON (amc.amn_contract_id= emp.amn_contract_id) 
	 LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
     LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
     LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
     LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE prc.value IN ('NN','NO','NV','NU','PO','TI','TO') 
		--AND emp.isactive = 'Y'
		AND CASE WHEN ($P{AMN_Concept_ValidFor} = 'AA' ) THEN 1=1 
                  WHEN ($P{AMN_Concept_ValidFor} = 'IM' AND  cty.arc='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'NU' AND  cty.utilidad='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'NV' AND  cty.vacacion='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'NP' AND  cty.prestacion='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'IN' AND  cty.ince='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'SS' AND  cty.sso='Y') THEN 1=1 
                  WHEN ($P{AMN_Concept_ValidFor} = 'FA' AND  cty.faov='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'PI' AND  cty.spf='Y') THEN 1=1
                  WHEN ($P{AMN_Concept_ValidFor} = 'SA' AND  cty.salario='Y') THEN 1=1
            END
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Location_ID}  IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{isShowRetired} = 'Y'  OR ($P{isShowRetired} = 'N' AND emp.status IN ('A','S','V')  ) ) THEN 1=1 ELSE 1=0 END)
	AND ( CASE WHEN ( $P{AllocationsOnly} IS NULL OR $P{AllocationsOnly} = 'N' OR ($P{AllocationsOnly} = 'Y' AND cty.sign ='D') ) THEN 1=1 ELSE 1=0 END )
  ) as phis ON (1= 0)
WHERE (imp_header= 1) OR (client_id= $P{AD_Client_ID}
  AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
  AND startdate BETWEEN DATE( $P{DateIni} ) AND DATE( $P{DateEnd} ) AND enddate BETWEEN DATE( $P{DateIni} ) AND DATE( $P{DateEnd} )
  )
ORDER BY phis.c_value, phis.value_emp, phis.prc_value, phis.calcorder, phis.startdate, phis.isshow DESC, phis.calcorder, header_info ASC
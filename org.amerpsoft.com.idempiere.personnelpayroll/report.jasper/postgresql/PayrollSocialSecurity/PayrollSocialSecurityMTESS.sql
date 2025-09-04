-- Payroll Social Security MTESS CrossTab (Column View)
-- PayrollSocialSecurityMTESS
-- Used for  reports and individual print
-- Currency Rate Added and Conversion reviewed
-- ONE PERIOD
WITH Conceptos AS (
	WITH RECURSIVE Nodos AS (
	    SELECT 
	    TRN1.AD_Tree_ID,
	    TRN1.Node_ID, 
	    0 as level, 
	    TRN1.Parent_ID, 
		ARRAY [TRN1.Node_ID::text]  AS ancestry, 
		ARRAY [ACTP.value::text]  AS valueparent,
		ARRAY [ACTP.calcorder::int]  AS calcorderparent,
		TRN1.Node_ID as Star_An,
		ACT.issummary
		FROM ad_treenode TRN1 
		LEFT JOIN AMN_Concept_Types ACT ON ACT.AMN_Concept_Types_ID = TRN1.Node_ID
		LEFT JOIN AMN_Concept_Types ACTP ON ACTP.AMN_Concept_Types_ID = TRN1.Parent_ID
		WHERE TRN1.AD_tree_ID=(
			SELECT DISTINCT tree.AD_Tree_ID
				FROM AD_Client adcli
				LEFT JOIN AMN_Concept amnc ON adcli.AD_Client_ID = amnc.AD_Client_ID
				LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
				WHERE adcli.AD_client_ID=$P{AD_Client_ID}	) 
		AND TRN1.isActive='Y' AND TRN1.Parent_ID = 0		
		UNION ALL
		SELECT 
		TRN1.AD_Tree_ID, 
		TRN1.Node_ID, 
		TRN2.level+1 as level,
		TRN1.Parent_ID, 
		TRN2.ancestry || ARRAY[TRN1.Node_ID::text] AS ancestry,
		TRN2.valueparent || ARRAY [ACTP.value::text]  AS valueparent,
		TRN2.calcorderparent || ARRAY [ACTP.calcorder::int]  AS calcorderparent,
		COALESCE(TRN2.Star_An,TRN1.Parent_ID) as Star_An,
		ACT.issummary
		FROM ad_treenode TRN1 
		INNER JOIN Nodos TRN2 ON (TRN2.node_id =TRN1.Parent_ID)
		LEFT JOIN AMN_Concept_Types ACT ON ACT.AMN_Concept_Types_ID = TRN1.Node_ID
		LEFT JOIN AMN_Concept_Types ACTP ON ACTP.AMN_Concept_Types_ID = TRN1.Parent_ID
		WHERE TRN1.AD_tree_ID=(
			SELECT DISTINCT tree.AD_Tree_ID
				FROM AD_Client adcli
				LEFT JOIN AMN_Concept amnc ON adcli.AD_Client_ID = amnc.AD_Client_ID
				LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
				WHERE adcli.AD_client_ID=$P{AD_Client_ID}
		)  AND TRN1.isActive='Y' 		
	) 
	-- MAIN SELECT
	-- AMN_Concept_types for Level reports
	SELECT DISTINCT ON (trial.calcorder, trial.ancestry)
		trial.Level,
		trial.Node_ID, 
		trial.value1 as value1,
		ACTN1.name AS name1,
		ACTN1.calcorder AS calcorder1,
		trial.value2 as value2,
		COALESCE(ACTN2.name,ACTN2.description)  AS name2,
		ACTN2.calcorder AS calcorder2,
		trial.value3 as value3,
		ACTN3.name AS name3,
		ACTN3.calcorder AS calcorder3,
		trial.amn_concept_types_id, 
		trial.calcorder,
		trial.optmode, 
		trial.isshow,
		trial.concept_value,
		trial.concept_name,
		trial.concept_name_indent,
		trial.concept_description
	FROM (
		SELECT 
			PAR.Level,
			PAR.issummary,
			PAR.AD_Tree_ID,
			CNT.tree_name,
			PAR.Node_ID, 
			PAR.Parent_ID ,
			PAR.ancestry,
			PAR.valueparent,
			COALESCE(valueparent[2],'') as Value1,
			COALESCE(valueparent[3],'') as Value2,
			COALESCE(valueparent[4],'') as Value3,
			CNT.AD_client_ID,
			CNT.AD_Org_ID,
			CNT.AMN_Concept_Types_ID,
			CNT.calcorder,
			CNT.optmode, 
			CNT.isshow,
			CNT.concept_value,
			CNT.concept_name,
			LPAD('', LEVEL ,' ') || COALESCE(CNT.concept_name,CNT.concept_description) as concept_name_indent,
			CNT.concept_description
		FROM Nodos PAR
		INNER JOIN (
			SELECT 
			adcli.AD_Client_ID, 
			amnc.AD_Org_ID,
			amnct.AMN_Concept_Types_ID, 
			amnct.value as concept_value,
			amnct.name as concept_name,
			amnct.description as concept_description,
			amnct.calcorder,
			amnct.optmode, 
			amnct.isshow,
			tree.AD_Tree_ID, 
			tree.name as tree_name
			FROM AD_Client adcli
			LEFT JOIN amn_concept amnc ON amnc.ad_client_id = adcli.ad_client_id 
			LEFT JOIN amn_concept_types amnct ON amnct.amn_concept_id = amnc.amn_concept_id  
			LEFT JOIN AD_Tree tree ON tree.AD_Tree_ID= amnc.AD_Tree_ID
			WHERE adcli.AD_client_ID=$P{AD_Client_ID}
			ORDER BY amnct.calcorder
		) as CNT ON CNT.AMN_Concept_types_ID = PAR.Node_ID
		WHERE PAR.issummary='N'
	) trial
	LEFT JOIN amn_concept_types as ACTN1 ON (ACTN1.Value = trial.Value1 AND ACTN1.AD_Client_ID= trial.AD_Client_ID)
	LEFT JOIN amn_concept_types as ACTN2 ON (ACTN2.Value = trial.Value2 AND ACTN2.AD_Client_ID= trial.AD_Client_ID)
	LEFT JOIN amn_concept_types as ACTN3 ON (ACTN3.Value = trial.Value3 AND ACTN3.AD_Client_ID= trial.AD_Client_ID)
	WHERE trial.ad_client_id = $P{AD_Client_ID}
	 AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR trial.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
	ORDER BY trial.calcorder, trial.ancestry
) 
-- 
-- MAIN SELECT
SELECT 
	org_value,
	org_name,
	rep_logo,
	value2,
	name2,
	calcorder2,
	amndateend,
	isshow,
	c_value,
	c_tipo,
	departamento,
	amn_employee_id,
	value_emp,
	empleado, 
	fecha_ingreso, paymenttype, cedula,
	cargo,
	amn_location_id, location_value, location_name,
	razon_social, socialsecurityid, socialsecuritymtess,
	BusinessActivity, taxid, phone, email, region_sso, direccion_sso, ciudad_loc, 
	nro_id,
	amn_payroll_id,
	amn_process_id,
	amn_payroll_detail_id,
	documentno,
	amn_period_id, 
	periodo,
	amndateini, 
	amndateend,
	amountallocated_t,
	amountdeducted_t,
	iso_code1,
	iso_code2,
	cursymbol1,currname1,
	cursymbol2,currname2, 
	cantidad,
	amountallocated,
	amountdeducted,
	amountcalculated,
	currencyrate,
				day_1, day_2, day_3, day_4, day_5, day_6,
		day_7, day_8, day_9, day_10, day_11, day_12,
		day_13, day_14, day_15, day_16, day_17, day_18,
		day_19, day_20, day_21, day_22, day_23, day_24,
		day_25, day_26, day_27, day_28, day_29, day_30,
		day_31
	FROM (
	SELECT 
		org_value,
		org_name,
		rep_logo,
		value2,
		name2,
		calcorder2,
		isshow,
		c_value, 
		c_tipo, 
		departamento,
		amn_employee_id,
		value_emp,
		empleado, 
		fecha_ingreso, paymenttype,cedula,
		cargo,
		day_1, day_2, day_3, day_4, day_5, day_6,
		day_7, day_8, day_9, day_10, day_11, day_12,
		day_13, day_14, day_15, day_16, day_17, day_18,
		day_19, day_20, day_21, day_22, day_23, day_24,
		day_25, day_26, day_27, day_28, day_29, day_30,
		day_31,
		amn_location_id, location_value, location_name,
		razon_social, socialsecurityid, socialsecuritymtess,
		BusinessActivity, taxid, phone, email, region_sso, direccion_sso, ciudad_loc,
		nro_id,
		amn_payroll_id,
		amn_process_id,
		amn_payroll_detail_id,
		documentno,
		amn_period_id, 
		periodo,
		amndateini, 
		amndateend,
		amountallocated_t,
		amountdeducted_t,
		iso_code1,
		iso_code2,
		cursymbol1,currname1,
		cursymbol2,currname2, 
		SUM(cantidad) AS cantidad,
		SUM(amountallocated) AS amountallocated,
		SUM(amountdeducted) AS amountdeducted, 
		SUM(amountcalculated) AS amountcalculated,
		currencyrate
	FROM 
	(
		SELECT 
			-- ORG
		    CASE WHEN ( $P{isShowOrganization} = 'N' ) THEN 'Todas' ELSE coalesce(org.value,'') END AS org_value ,
		    CASE WHEN ( $P{isShowOrganization} = 'N' ) THEN '** Todas las Organizaciones **' ELSE coalesce(org.name,org.value,'') END AS org_name ,		   	
--		    coalesce(org.value,'') as org_value,
--			coalesce(org.name,org.value,'') as org_name,
			CASE WHEN ($P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
			cty.value2,
			cty.name2,
			cty.calcorder2,
			-- PERIOD
			prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend,
			-- TIPO DE CONCEPTO
			cty.amn_concept_types_id, 
			cty.optmode, 
			cty.calcorder, 
			cty.isshow, 
			cty.concept_value as cty_value, 
			COALESCE(cty.concept_name, cty.concept_description) as concept_type,
			-- TIPO DE CONCEPTO (PROCESO)
			ctp.value as ctp_value, COALESCE(ctp.name, ctp.description) as concept_type_process, 	 
			-- PROCESS
			prc.amn_process_id, COALESCE(prc.name, prc.description) as proceso,
		     --CASE WHEN ( {AMN_Process_ID}  IS NULL OR prc.amn_process_id= AMN_Process_ID} ) THEN 1 ELSE 0 END AS imp_proceso,
			-- CONTRACT
		   	amc.value as c_value, 
		   	CASE
			    WHEN amc.payrolldays = 30 THEN 'MENSUAL'
			    WHEN amc.payrolldays = 15 THEN 'QUINCENAL'
			    WHEN amc.payrolldays = 7 THEN 'SEMANAL'
			    WHEN amc.payrolldays = 1 THEN 'DIARIO'
			    ELSE 'DESCONOCIDO'
			END AS c_tipo,
			-- DEPARTAMENT
		   	COALESCE(dep.name,dep.description) as departamento,
			-- LOCATION
		    lct.amn_location_id AS amn_location_id,
		    CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN 'Todas' ELSE lct.value END AS location_value ,
		    CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN '** Todas las localidades **' ELSE COALESCE(lct.name, lct.description) END AS location_name ,		   	
			lct.orgname AS razon_social, lct.socialsecurityid, lct.socialsecuritymtess, 
			lct.BusinessActivity, lct.taxid, COALESCE(lct.phone,'') AS phone , COALESCE(lct.email,'') AS email,
			-- LOCATION
		    ARRAY_TO_STRING(
		        ARRAY_REMOVE(
		            ARRAY[
		                NULLIF(TRIM(loc.address1), ', '),
		                NULLIF(TRIM(loc.address2), ', '),
		                NULLIF(TRIM(loc.address3), ', '),
		                NULLIF(TRIM(loc.address4), '. '),
		                INITCAP(NULLIF(TRIM(cit.name), ', '))
		            ],
		            NULL
		        ),
		        ', '
		    ) AS direccion_sso,
			-- REGION
			INITCAP(COALESCE(reg.name, reg.description, '')) as region_sso,
			-- CITY
			INITCAP(COALESCE(cit.name, '')) as ciudad_loc,
		    -- EMPLOYEE
		   	emp.amn_employee_id,
		  	emp.value as value_emp, emp.name as empleado, emp.incomedate as fecha_ingreso, emp.paymenttype,
		  	emp.idnumber AS cedula,
		   	COALESCE(jtt.name, jtt.description,'') as cargo, 
		   	COALESCE(cbp.taxid,'') as nro_id, 
		   	day_1, day_2, day_3, day_4, day_5, day_6,
			day_7, day_8, day_9, day_10, day_11, day_12,
			day_13, day_14, day_15, day_16, day_17, day_18,
			day_19, day_20, day_21, day_22, day_23, day_24,
			day_25, day_26, day_27, day_28, day_29, day_30,
			day_31,
			-- PAYROLL
		   	pyr.amn_payroll_id,
		   	COALESCE(pyr.documentno,'') as documentno,
		   	pyr.description as recibo,
			-- PYR AMOUNTS CONVERTED
			currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated_t, 
			currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted_t, 
			currencyConvert(pyr.amountcalculated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountcalculated_t, 
			-- CURRENCY
			curr1.iso_code as iso_code1,
			COALESCE(currt1.cursymbol,curr1.cursymbol,curr1.iso_code,'') as cursymbol1,
			COALESCE(currt1.description,curr1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
			curr2.iso_code as iso_code2,
			COALESCE(currt2.cursymbol,curr2.cursymbol,curr2.iso_code,'') as cursymbol2,
			COALESCE(currt2.description,curr2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
			-- PAYROLL DETAIL
		    -- MONTOS Y CIFRAS cty.concept_value	
		    pyr_d.amn_payroll_detail_id,   
			pyr_d.qtyvalue as cantidad, 
			currencyConvert(pyr_d.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated, 
			currencyConvert(pyr_d.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted, 
			currencyConvert(pyr_d.amountcalculated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, NULL, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountcalculated,
			COALESCE(currencyrate(pyr.c_currency_id, $P{C_Currency_ID},  pyr.dateacct,  pyr.c_conversiontype_id, pyr.AD_Client_ID, pyr.AD_Org_ID ),0) as currencyrate
		FROM adempiere.amn_payroll as pyr
		INNER JOIN adempiere.amn_payroll_detail 		as pyr_d ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
		INNER JOIN adempiere.amn_concept_types_proc  as ctp 	 ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
		INNER JOIN Conceptos 			as cty 	 ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
		INNER JOIN adempiere.amn_process  					as prc 	 ON (prc.amn_process_id= pyr.amn_process_id)
		INNER JOIN adempiere.amn_employee as emp ON (emp.amn_employee_id= pyr.amn_employee_id)
		INNER JOIN(
			SELECT 
			    emp2.amn_employee_id,
			    emp2.value,
			    emp2.name,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 1 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_1,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 2 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_2,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 3 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_3,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 4 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_4,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 5 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_5,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 6 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_6,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 7 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_7,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 8 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_8,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 9 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_9,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 10 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_10,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 11 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_11,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 12 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_12,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 13 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_13,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 14 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_14,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 15 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_15,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 16 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_16,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 17 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_17,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 18 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_18,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 19 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_19,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 20 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_20,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 21 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_21,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 22 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_22,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 23 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_23,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 24 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_24,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 25 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_25,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 26 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_26,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 27 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_27,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 28 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_28,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 29 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_29,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 30 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_30,
			    MAX(CASE WHEN EXTRACT(DAY FROM att_date.report_date) = 31 THEN ROUND(apap.shift_hnd, 0) ELSE 0 END) AS day_31
			FROM amn_employee emp2
			CROSS JOIN (
				SELECT generate_series(
				       COALESCE((SELECT CAST(amndateini AS DATE) FROM amn_period WHERE amn_period_id= $P{AMN_Period_ID}), DATE_TRUNC('month', CURRENT_DATE)::DATE), 
				       COALESCE((SELECT CAST(amndateend as DATE) FROM amn_period WHERE amn_period_id= $P{AMN_Period_ID}), (DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month' - INTERVAL '1 day')::DATE), 
				       '1 day'::interval
				)::DATE AS report_date  ) AS att_date
			LEFT JOIN AMN_payroll_assist_proc apap ON apap.amn_employee_id = emp2.amn_employee_id AND apap.event_date = att_date.report_date
			WHERE 
			    emp2.isactive='Y' AND emp2.ad_client_id=  $P{AD_Client_ID}  
			    AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR emp2.ad_orgto_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 	    
			    AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR emp2.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
			    AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR emp2.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
			    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp2.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
			GROUP BY emp2.amn_employee_id, emp2.value, emp2.name
			ORDER BY emp2.amn_employee_id		
		) AS horas ON horas.amn_employee_id = emp.amn_employee_id
		INNER JOIN adempiere.ad_org   as org ON (org.ad_org_id = emp.ad_orgto_id)
		INNER JOIN adempiere.amn_period   					as prd 	 ON (prd.amn_period_id= pyr.amn_period_id)
		INNER JOIN adempiere.amn_department as dep ON (emp.amn_department_id = dep.amn_department_id)
		INNER JOIN adempiere.amn_jobtitle as jtt ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
		INNER JOIN adempiere.c_bpartner 						as cbp 	 ON (emp.c_bpartner_id= cbp.c_bpartner_id)
		INNER JOIN adempiere.amn_location 					as lct 	 ON (lct.amn_location_id= emp.amn_location_id)
		INNER JOIN adempiere.amn_contract 					as amc 	 ON (amc.amn_contract_id= emp.amn_contract_id)	 
		LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
		LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
		LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
		INNER JOIN adempiere.ad_clientinfo as cliinfo ON (pyr.ad_client_id = cliinfo.ad_client_id)
	    LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	    INNER JOIN adempiere.ad_orginfo as orginfo ON (pyr.ad_org_id = orginfo.ad_org_id)
		LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
    	LEFT JOIN adempiere.c_location as loc ON (lct.c_location_ss_id= loc.c_location_id)
		LEFT JOIN adempiere.c_country as ctr ON (ctr.c_country_id= loc.c_country_id)
		LEFT JOIN adempiere.c_region as reg ON (reg.c_region_id= loc.c_region_id)
   	 	LEFT JOIN adempiere.c_municipality as mun ON (mun.c_municipality_id= loc.c_municipality_id)
		LEFT JOIN adempiere.c_parish as par ON (par.c_parish_id= loc.c_parish_id)
		LEFT JOIN adempiere.c_city as cit ON (cit.c_city_id= loc.c_city_id)
		WHERE pyr.ad_client_id=  $P{AD_Client_ID}  
			AND ( CASE WHEN ( $P{AD_Org_ID} IS NULL OR $P{AD_Org_ID} = 0 OR pyr.ad_org_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) 
		    AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END )
		    AND ( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR prc.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
			AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR emp.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
			AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR prd.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
		    AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN  1=1 ELSE 1=0 END )
		    AND ( CASE WHEN ( $P{isShowZERO} = 'Y') OR ($P{isShowZERO} = 'N' 
		    			AND (  pyr_d.qtyvalue <> 0 OR pyr_d.amountallocated <> 0 OR pyr_d.amountdeducted<>0  OR pyr_d.amountcalculated<> 0)) THEN 1=1 ELSE 1=0 END )
	) AS recibo
	GROUP BY org_value, org_name, rep_logo, value2,name2, calcorder2, amndateend, isshow, c_value, c_tipo,
	departamento, amn_employee_id, value_emp, empleado, fecha_ingreso, paymenttype, cedula, cargo, 
	razon_social, socialsecurityid, socialsecuritymtess, BusinessActivity, taxid, phone, email, region_sso, direccion_sso, ciudad_loc,
	amn_location_id, location_value, location_name, nro_id, amn_payroll_id,
	amn_process_id, amn_payroll_detail_id, documentno, amn_period_id, periodo, amndateini, amndateend, amountallocated_t, amountdeducted_t,
	iso_code1, iso_code2, cursymbol1, currname1, cursymbol2, currname2, currencyrate,
			day_1, day_2, day_3, day_4, day_5, day_6,
		day_7, day_8, day_9, day_10, day_11, day_12,
		day_13, day_14, day_15, day_16, day_17, day_18,
		day_19, day_20, day_21, day_22, day_23, day_24,
		day_25, day_26, day_27, day_28, day_29, day_30,
		day_31
	ORDER BY  org_value, location_value, value_emp, documentno, calcorder2
) AS recibocur

-- EmployeesShort V4_SinFincion.jrxml
-- Employees Selective
-- HISTORIC VALUES Fro SALARIES, COMISSION OTHER INCOME
WITH DateRange AS (
    -- 1. Calcula el número total de meses en el rango
    SELECT 
        DATE($P{DateIni}) AS DateIni,
        DATE($P{DateEnd}) AS DateEnd,
        EXTRACT(YEAR FROM AGE(DATE($P{DateEnd}) + INTERVAL '1 day', DATE($P{DateIni}))) * 12 +
        EXTRACT(MONTH FROM AGE(DATE($P{DateEnd}) + INTERVAL '1 day', DATE($P{DateIni}))) AS NoMeses
),
Conceptos AS (
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
),
EmployeeMonths AS (
    -- 2. IDENTIFICA LOS MESES POR EMPLEADO Y GRUPO DE CONCEPTO (value2)
    SELECT
        pyr.amn_employee_id,
        cty.value2, -- CLAVE: Agregar el grupo de concepto
        DATE_TRUNC('month', pyr.dateacct) AS payroll_month,
        MAX(CASE WHEN pyr_d.amountcalculated > 0 THEN 1 ELSE 0 END) AS has_income
    FROM adempiere.amn_payroll pyr
    INNER JOIN adempiere.amn_payroll_detail pyr_d ON pyr_d.amn_payroll_id = pyr.amn_payroll_id
    
    -- UNIR A CONCEPTOS PARA OBTENER value2
    INNER JOIN adempiere.amn_concept_types_proc AS ctp ON ctp.amn_concept_types_proc_id = pyr_d.amn_concept_types_proc_id
    INNER JOIN Conceptos AS cty ON cty.amn_concept_types_id = ctp.amn_concept_types_id
    
    CROSS JOIN DateRange dr 
    WHERE 
        -- Filtros de Concepto para coincidir con PayrollDetails
        cty.calcorder1 = 100000 
        AND cty.optmode = 'A'
        -- Filtro de Fechas Simple
        AND pyr.dateacct >= dr.DateIni 
        AND pyr.dateacct <= dr.DateEnd
        
    -- AGRUPACIÓN CLAVE: Empleado, Grupo de Concepto, Mes
    GROUP BY 1, 2, 3
),
MonthlyCounts AS (
    -- 3. CUENTA EL NÚMERO DE MESES DEVENGADOS POR EMPLEADO Y GRUPO DE CONCEPTO
    SELECT
        amn_employee_id,
        value2,
        COUNT(payroll_month) FILTER (WHERE has_income = 1)::NUMERIC AS NoMesesDev
    FROM EmployeeMonths
    GROUP BY amn_employee_id, value2 -- AGRUPACIÓN CLAVE: Empleado y Grupo de Concepto
),
PayrollDetails AS (
    -- 4. TU CONSULTA BASE (con los cálculos de meses unidos)
    SELECT 
        emp.amn_employee_id, 
        emp.value,
        cty.value2,
        (SELECT NoMeses FROM DateRange) AS NoMeses,             -- <-- Columna NoMeses
        mc.NoMesesDev AS NoMesesDev,               -- <-- Columna NoMesesDev
        COALESCE(currt0.description,curr0.iso_code,curr0.cursymbol,'') AS currname,
        SUM(pyr_d.amountallocated) AS amountallocated ,  
        SUM(pyr_d.amountdeducted) AS amountdeducted  , 
        SUM(pyr_d.amountcalculated ) AS amountcalculated
    FROM AMN_Employee emp
    INNER JOIN adempiere.amn_payroll AS pyr ON pyr.amn_employee_id = emp.amn_employee_id
    INNER JOIN adempiere.c_currency AS curr0 ON curr0.c_currency_id = pyr.c_currency_id
    LEFT JOIN c_currency_trl currt0 ON curr0.c_currency_id = currt0.c_currency_id 
        AND currt0.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID = $P{AD_Client_ID}) 
    INNER JOIN adempiere.amn_payroll_detail AS pyr_d ON pyr_d.amn_payroll_id = pyr.amn_payroll_id
    INNER JOIN adempiere.amn_concept_types_proc AS ctp 	 ON ctp.amn_concept_types_proc_id = pyr_d.amn_concept_types_proc_id
    INNER JOIN Conceptos AS cty ON cty.amn_concept_types_id = ctp.amn_concept_types_id  
    LEFT JOIN MonthlyCounts mc ON mc.amn_employee_id = emp.amn_employee_id AND mc.value2 = cty.value2
    LEFT JOIN DateRange dr ON 1=1 -- Join para usar el rango de fechas en el filtro
    WHERE 
        emp.isactive = 'Y' 
        AND cty.calcorder1 = 100000 
        AND cty.optmode = 'A' 
        AND emp.ad_client_id = $P{AD_Client_ID} 
        AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id = $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
        AND pyr.dateacct >= dr.DateIni 
        AND pyr.dateacct <= dr.DateEnd
    GROUP BY 
        emp.amn_employee_id, 
        emp.value, 
        cty.value2, 
        currname, 
        mc.NoMesesDev
    ORDER BY 
        emp.amn_employee_id, 
        emp.value, 
        cty.value2
)
SELECT *
FROM (
	SELECT 
	-- REPORT HEADER
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN '' ELSE COALESCE(orginfo.taxid,'') END as rep_taxid,
	CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN img1.binarydata ELSE img2.binarydata END as rep_logo,
    CASE WHEN ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as rep_name,
	-- CLIENT
	emp.ad_client_id,
	-- ORGANIZATION
	emp.ad_orgto_id,
    coalesce(org.value,'') as org_value,
    coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description, org.name, org.value, '') AS org_description,
	-- EMPLOYEE 
	emp.amn_employee_id,
    emp.value AS codigo,  
    emp.idnumber AS cedula,
    emp.salary AS sueldo,
	emp.incomedate AS fecha_ingreso,
	emp.Birthday AS fecha_nacimiento,
    date_part('year', age(current_date, emp.incomedate)) a_servicio, 
    date_part('month', age(current_date, emp.incomedate)) m_servicio, 
    date_part('day', age(current_date, emp.incomedate)) d_servicio,
    emp.status, 
    CASE
		WHEN ($P{AMN_Status_A} = 'Y'
		AND emp.status = 'A') THEN 1
		WHEN ($P{AMN_Status_V} = 'Y'
		AND emp.status = 'V') THEN 1
		WHEN ($P{AMN_Status_R} = 'Y'
		AND emp.status = 'R') THEN 1
		WHEN ($P{AMN_Status_S} = 'Y'
		AND emp.status = 'S') THEN 1
		ELSE 0
	END AS imprimir_status,
    CASE
		WHEN emp.jobcondition = 'C' THEN 'Contratado'
		WHEN emp.jobcondition = 'F' THEN 'Fijo'
		WHEN emp.jobcondition = 'I' THEN 'Independiente'
		WHEN emp.jobcondition = 'P' THEN 'Parcial'
		ELSE 'Indefinido'
	END AS contrato_condicion,
	CASE
		WHEN ( ( $P{AD_Org_ID} = 0
		OR $P{AD_Org_ID} IS NULL )
		OR emp.ad_org_id = $P{AD_Org_ID} ) THEN 1
		ELSE 0
	END AS imprimir_org,
	-- CURRENCY
	curr1.iso_code AS iso_code1,
	currt1.cursymbol AS cursymbol1,
	COALESCE(currt1.description, curr1.iso_code, curr1.cursymbol, '') AS currname1,
	curr2.iso_code AS iso_code2,
	currt2.cursymbol AS cursymbol2,
	COALESCE(currt2.description, curr2.iso_code, curr2.cursymbol, '') AS currname2,	     
    COALESCE(prh.salary, emp.salary) AS salario,
    prh.validto AS salario_fecha,
    cok.OK_salary,
	-- BUSINESS PARTNER
	COALESCE(cbp.name, emp.name) AS nombre,
	COALESCE(cbp.taxid, cbp.amerp_rifseniat) AS rif,
	-- EMPLOYEE
	CASE
		WHEN ( $P{AMN_Employee_ID} IS NULL
		OR emp.amn_employee_id = $P{AMN_Employee_ID} ) THEN 1
		ELSE 0
	END AS imprimir_emp,
	-- CONTRACT
	cok.value AS contrato_tipo,
	COALESCE(cok.value, cok.name, cok.description) AS contrato_nombre,
	CASE
		WHEN ( $P{AMN_Contract_ID} IS NULL
		OR cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1
		ELSE 0
	END AS imprimir_con,
	-- CONTRACT
	CASE
		WHEN ( $P{AMN_Contract_ID} IS NULL
		OR cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1
		ELSE 0
	END AS imprimir_nom,
	-- JobTitle
	j.value AS jobtitle_value,
	j.name AS jobtitle_name,
	j.description AS jobtitle_description,
	-- LOCATION
	CASE
		WHEN ( $P{AMN_Location_ID} IS NULL
		OR l.amn_location_id = $P{AMN_Location_ID}) THEN 1
		ELSE 0
	END AS imprimir_loc,
	CASE
		WHEN ($P{AMN_Location_ID} IS NULL
		AND $P{isShowLocation} = 'N' ) THEN 0
		ELSE l.amn_location_id
	END AS amn_location_id,
	CASE
		WHEN ($P{AMN_Location_ID} IS NULL
		AND $P{isShowLocation} = 'N' ) THEN 'XX'
		ELSE l.value
	END AS localidad_codigo ,
	CASE
		WHEN ($P{AMN_Location_ID} IS NULL
		AND $P{isShowLocation} = 'N' ) THEN '** Todas las Localidades **'
		ELSE COALESCE(l.name, l.description)
	END AS localidad_nombre,
	ctyhist.value2,
	ctyhist.amountcalculated,
	NoMeses,
	NoMesesDev
	FROM AMN_Employee emp
	LEFT OUTER JOIN C_BPartner cbp ON (cbp.C_BPartner_ID=emp.C_BPartner_ID  AND emp.IsActive='Y')
	INNER JOIN (
		SELECT DISTINCT con.AMN_Contract_ID, con.value, con.name, con.description, CASE WHEN rol.AMN_Process_ID IS NULL THEN 'N' ELSE 'Y' END as OK_salary
		FROM adempiere.AMN_Contract con
		INNER JOIN (
			SELECT AMN_Process_ID, AMN_Contract_ID, AD_Role_ID FROM adempiere.AMN_Role_Access 
			WHERE  AMN_Process_ID IN (SELECT AMN_Process_ID FROM adempiere.AMN_Process WHERE AMN_Process_Value='NN') AND AD_Role_ID = $P{AD_Role_ID}
		) rol ON rol.AMN_Contract_ID = con.AMN_Contract_ID	
	) cok on cok.AMN_Contract_ID = emp.AMN_Contract_ID
	LEFT OUTER JOIN AMN_Department d ON (d.AMN_Department_ID=emp.AMN_Department_ID)
	LEFT OUTER JOIN AMN_Jobtitle j ON(j.AMN_Jobtitle_ID = emp.AMN_Jobtitle_ID)
	LEFT OUTER JOIN AMN_Shift s ON(s.AMN_Shift_ID = emp.AMN_Shift_ID)
	LEFT OUTER JOIN AMN_Location l ON(l.AMN_Location_ID = emp.AMN_Location_ID)
	INNER JOIN adempiere.ad_client as cli ON (emp.ad_client_id = cli.ad_client_id)
	INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	INNER JOIN adempiere.ad_org as org ON (emp.ad_orgto_id = org.ad_org_id)
	INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	LEFT JOIN PayrollDetails AS ctyhist ON ctyhist.amn_employee_id = emp.amn_employee_id
	-- SALARY HISTORIC MAX VAlidTo
	LEFT JOIN  (
		SELECT DISTINCT ON (AMN_Employee_ID)
		AMN_Employee_ID, MAX(ValidTo) as ValidTo, MAX(Salary) as Salary, C_Currency_ID
		FROM
		AMN_Payroll_Historic
		WHERE C_Currency_ID=$P{C_Currency_ID}
		GROUP BY AMN_Employee_ID, C_Currency_ID
		) prh ON ( prh.AMN_Employee_ID = emp.AMN_Employee_ID )
	 LEFT JOIN c_currency curr1 on prh.c_currency_id = curr1.c_currency_id
     LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
     LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
     LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE emp.isactive= 'Y' AND
       emp.ad_client_id=  $P{AD_Client_ID} 
       AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
) trab
WHERE trab.imprimir_emp =1 AND trab.imprimir_status=1 AND trab.imprimir_con=1
	AND trab.imprimir_loc=1 AND trab.imprimir_org = 1
ORDER BY  org_value, contrato_tipo, trab.localidad_codigo, trab.codigo
 

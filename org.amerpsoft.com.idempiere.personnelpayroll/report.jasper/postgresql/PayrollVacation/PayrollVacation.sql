-- VACATION Report
-- Related to Leaves
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
   -- CLIENT
	emp.ad_client_id,
	-- ORGANIZATION
	vac.ad_org_id,
	emp.ad_orgto_id,
    coalesce(org.value,'') as org_value,
    coalesce(org.name,org.value,'') as org_name,
	COALESCE(org.description,org.name,org.value,'') as org_description, 	
	-- EMPLOYEE 
    emp.amn_employee_id,
    emp.value as codigo,  
    emp.idnumber as cedula,
    emp.status, 
    emp.name AS emp_nombre, emp.incomedate as fecha_ingreso,
	LPAD(date_part('year', age(current_date, emp.incomedate))::TEXT, 2, ' ') AS a_servicio, 
	LPAD(date_part('month', age(current_date, emp.incomedate))::TEXT, 2, ' ') AS m_servicio, 
	LPAD(date_part('day', age(current_date, emp.incomedate))::TEXT, 2, ' ') AS d_servicio,
	-- Derecho de vacaciones según los años de servicio en su proximo aniversario
	CASE 
        WHEN date_part('year', age(date_trunc('year', emp.incomedate) + (extract(year FROM current_date) - extract(year FROM emp.incomedate)) * INTERVAL '1 year', emp.incomedate)) BETWEEN 1 AND 5 THEN 12
        WHEN date_part('year', age(date_trunc('year', emp.incomedate) + (extract(year FROM current_date) - extract(year FROM emp.incomedate)) * INTERVAL '1 year', emp.incomedate)) BETWEEN 6 AND 10 THEN 18
        WHEN date_part('year', age(date_trunc('year', emp.incomedate) + (extract(year FROM current_date) - extract(year FROM emp.incomedate)) * INTERVAL '1 year', emp.incomedate)) >= 11 THEN 30
        ELSE 0
    END AS derecho_vacaciones,
    CASE WHEN ($P{AMN_Status_A} = 'Y' AND emp.status='A') THEN 1
         WHEN ($P{AMN_Status_V} = 'Y' AND emp.status='V') THEN 1
         WHEN ($P{AMN_Status_R} = 'Y' AND emp.status='R') THEN 1
         WHEN ($P{AMN_Status_S} = 'Y' AND emp.status='S') THEN 1
         ELSE 0 END 
    AS imprimir_status,
	-- CURRENCY
	curr1.iso_code as iso_code1,
	currt1.cursymbol as cursymbol1,
	COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
	curr2.iso_code as iso_code2,
	currt2.cursymbol as cursymbol2,
	COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2,	     
	-- CONTRACT
    cok.value as contrato_tipo,
	COALESCE(cok.value, cok.name, cok.description) as contrato_nombre,    
	-- JobTitle
	jt.value as jobtitle_value, 	jt.name as jobtitle_name,	jt.description as jobtitle_description,
	-- LOCATION
    CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN 0
         ELSE loc.amn_location_id 
         END AS amn_location_id,
     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN 'XX'
          ELSE loc.value  
          END AS localidad_codigo ,
     CASE WHEN ($P{AMN_Location_ID} IS NULL AND $P{isShowLocation} = 'N' ) THEN '** Todas las Localidades **'
          ELSE COALESCE(loc.name, loc.description)
          END AS localidad_nombre,
    -- SECTOR
    COALESCE(sec.value, sec.name) AS sector,
    -- Payroll Receipt
	COALESCE(amn_payroll_id, 0) AS amn_payroll_id,
	COALESCE(documentno,'') AS documentno,
	COALESCE(pyr_value, '') AS pyr_value,
	pyr_name, 
	pyr_description,
	invdateini, invdateend, 
	COALESCE(daysvacation,0) AS daysvacation,  
	COALESCE(daysvacationcollective,0)  AS daysvacationcollective, 
	DateReEntry, DateReEntryReal,
	amn_leaves_id, leave_description, datefrom, dateto, daysto,
	total_days_vacation, total_days_leaves
	FROM AMN_Employee emp
	INNER JOIN adempiere.ad_org as org ON (emp.ad_orgto_id = org.ad_org_id)
	LEFT JOIN (
		SELECT 
	    -- Payroll Receipt
	    pyr.ad_org_id, 
	    pyr.documentno, 
	    pyr.amn_employee_id, 
	    pyr.amn_payroll_id, 
	    pyr.c_currency_id, 
	    pyr.value AS pyr_value, 
	    pyr.name AS pyr_name, 
	    pyr.description AS pyr_description,
	    pyr.invdateini, 
	    pyr.invdateend, 
	    pyr.daysvacation,  
	    pyr.daysvacationcollective, 
	    pyr.DateReEntry, 
	    pyr.DateReEntryReal,
	    -- Leaves
	    lea.amn_leaves_id, 
	    lea.description AS leave_description, 
	    lea.datefrom, 
	    lea.dateto, 
	    lea.daysto,
	    -- Total daysvacation per payroll
	    SUM(pyr.daysvacation) OVER (PARTITION BY pyr.amn_payroll_id) AS total_days_vacation,
	    -- Total daysto per payroll
	    SUM(lea.daysto) OVER (PARTITION BY pyr.amn_payroll_id) AS total_days_leaves
		FROM AMN_Payroll pyr 
		INNER JOIN AMN_Process prc ON prc.AMN_Process_ID = pyr.AMN_Process_ID
		LEFT JOIN AMN_Leaves lea ON lea.amn_payroll_id = pyr.amn_payroll_id
		LEFT JOIN AMN_Leaves_Types leat ON leat.AMN_Leaves_Types_ID = lea.AMN_Leaves_Types_ID
		WHERE prc.value = 'NV' 
		AND pyr.InvDateIni >= COALESCE(TO_TIMESTAMP($P{DateIni}, 'YYYY-MM-DD HH24:MI:SS'), DATE_TRUNC('YEAR', NOW()) ) 
		AND pyr.InvDateEnd <= COALESCE(TO_TIMESTAMP($P{DateEnd}, 'YYYY-MM-DD HH24:MI:SS'), DATE_TRUNC('YEAR', NOW()) + INTERVAL '1 YEAR' - INTERVAL '1 SECOND' )
	) AS vac ON vac.amn_employee_id = emp.amn_employee_id
	INNER JOIN (
		SELECT DISTINCT con.AMN_Contract_ID, con.value, con.name, con.description, CASE WHEN rol.AMN_Process_ID IS NULL THEN 'N' ELSE 'Y' END as OK_salary
		FROM adempiere.AMN_Contract con
		INNER JOIN (
			SELECT AMN_Process_ID, AMN_Contract_ID, AD_Role_ID FROM adempiere.AMN_Role_Access 
			WHERE  AMN_Process_ID IN (SELECT AMN_Process_ID FROM adempiere.AMN_Process WHERE AMN_Process_Value='NN') AND AD_Role_ID = $P{AD_Role_ID}
		) rol ON rol.AMN_Contract_ID = con.AMN_Contract_ID	
	) cok on cok.AMN_Contract_ID = emp.AMN_Contract_ID
	LEFT OUTER JOIN AMN_Department d ON (d.AMN_Department_ID=emp.AMN_Department_ID)
	LEFT OUTER JOIN AMN_Jobtitle jt ON(jt.AMN_Jobtitle_ID = emp.AMN_Jobtitle_ID)
	LEFT OUTER JOIN AMN_Location loc ON(loc.AMN_Location_ID = emp.AMN_Location_ID)
	LEFT OUTER JOIN AMN_sector sec ON (sec.AMN_Sector_ID = emp.AMN_Sector_ID)
	LEFT JOIN c_currency curr1 on vac.c_currency_id = curr1.c_currency_id
    LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
    LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
    LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
	WHERE emp.isactive= 'Y'
       AND emp.ad_client_id=  $P{AD_Client_ID} 
       AND ( CASE WHEN ( ( $P{AD_Org_ID} = 0 OR $P{AD_Org_ID} IS NULL ) OR emp.ad_orgto_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END )
       AND ( CASE WHEN ( $P{AMN_Employee_ID} IS NULL OR emp.amn_employee_id = $P{AMN_Employee_ID} ) THEN 1=1 ELSE 1=0 END )
       AND ( CASE WHEN ( $P{AMN_Contract_ID} IS NULL OR cok.amn_contract_id = $P{AMN_Contract_ID}) THEN 1=1 ELSE 1=0 END )
  	   AND ( CASE WHEN ( $P{AMN_Location_ID} IS NULL or loc.amn_location_id = $P{AMN_Location_ID}) THEN 1=1 ELSE 1=0 END )
) AS vacacion ON (1=0)
WHERE (imp_header = 1) OR 
	(vacacion.ad_client_id= $P{AD_Client_ID} 
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR vacacion.ad_org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
ORDER BY  org_value, contrato_tipo, localidad_codigo, sector, codigo, invdateini

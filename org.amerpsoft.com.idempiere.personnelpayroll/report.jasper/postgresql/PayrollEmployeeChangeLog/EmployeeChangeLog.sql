-- EmployeeChangeLog
-- Changes on Employees
-- Version V3  Agregada tabala AMN_payroll_detail
-- Agregados registros de creacion
-- Seleccion INS, UPT, DEL
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
	SELECT * FROM (
	-- Bloque 1: Cambios en AMN_Employee (UPDATE)
	  SELECT 
	    chl.ad_client_id AS client_id,
	    chl.ad_org_id AS org_id,
	    usr.name AS user_value,
	    usr.description AS user_name,
	    'AMN_Employee - Tabla de Trabajadores' AS tablename,
	    COALESCE(clm_t.name, clm.columnname, '') AS columnname,
	    chl.oldvalue,
	    chl.newvalue,
	    CAST(chl.created AS Timestamp) AS created,
	    CASE 
	      WHEN chl.oldvalue IS NULL AND chl.newvalue IS NOT NULL THEN 'INS'
	      WHEN chl.oldvalue IS NOT NULL AND chl.newvalue IS NOT NULL THEN 'UPD'
	      ELSE 'OTR'
	    END AS cambio_tipo,
	    emp.amn_employee_id,
	    emp.value AS employee_value,
	    emp.name AS employee_name,
	    (SELECT DISTINCT amn_process_id FROM amn_process WHERE amn_process_value='NN' AND ad_client_id = $P{AD_Client_ID}) AS amn_process_id,
	    'NN' AS process_value,
	    contract.amn_contract_id,
	    COALESCE(contract.value, '???') || ' - ' || COALESCE(contract.name, '') AS contract_display,
	    chl.ad_changelog_id,
	    chl.record_id,
	    chl.ad_table_id
	  FROM adempiere.AD_Changelog chl
	  JOIN adempiere.ad_user usr ON usr.ad_user_id = chl.createdby
	  JOIN adempiere.ad_table tbl ON tbl.ad_table_id = chl.ad_table_id
	  JOIN adempiere.ad_column clm ON clm.ad_column_id = chl.ad_column_id
	  LEFT JOIN adempiere.ad_column_trl clm_t ON clm_t.ad_column_id = clm.ad_column_id 
	    AND clm_t.ad_language = (SELECT AD_Language FROM adempiere.AD_Client WHERE AD_Client_ID = chl.ad_client_id)
	  LEFT JOIN adempiere.AMN_Employee emp ON emp.AMN_Employee_ID = chl.Record_ID
	  LEFT JOIN adempiere.AMN_Contract contract ON contract.AMN_Contract_ID = emp.AMN_Contract_ID
	  WHERE tbl.tablename = 'AMN_Employee'
		  AND chl.created BETWEEN CAST($P{DateFrom} AS Timestamp) AND CAST($P{DateTo} AS Timestamp)
	UNION ALL
	-- INICIO DE NUEVO BLOQUE: CREACIÓN DE AMN_Employee (INSERT)
	  SELECT
	    emp.ad_client_id AS client_id,
	    emp.ad_org_id AS org_id,
	    usr.name AS user_value,
	    usr.description AS user_name,
	    'AMN_Employee - Tabla de Trabajadores' AS tablename,
	    COALESCE(clm_t.name, clm.columnname, '') AS columnname, 
	    NULL AS oldvalue,
	    'Registro Creado' AS newvalue,  
	    CAST(emp.created AS Timestamp) AS created,
	    'INS' AS cambio_tipo,         
	    emp.amn_employee_id,
	    emp.value AS employee_value,
	    emp.name AS employee_name,
	    (SELECT DISTINCT amn_process_id FROM amn_process WHERE amn_process_value='NN' AND ad_client_id = $P{AD_Client_ID}) AS amn_process_id,
	    'NN' AS process_value,
	    contract.amn_contract_id,
	    COALESCE(contract.value, '???') || ' - ' || COALESCE(contract.name, '') AS contract_display,
	    NULL AS ad_changelog_id,       
	    emp.AMN_Employee_ID AS record_id,
	    (SELECT ad_table_id FROM ad_table WHERE tablename = 'AMN_Employee') AS ad_table_id
	  FROM adempiere.AMN_Employee emp
	  JOIN adempiere.ad_user usr ON usr.ad_user_id = emp.createdby
  	  JOIN adempiere.ad_table tbl ON tbl.tablename = 'AMN_Employee'
	  -- Filtra solo las columnas con registro activo, generando una fila por cada columna rastreada
	  JOIN adempiere.ad_column clm ON clm.ad_table_id = tbl.ad_table_id AND clm.isallowlogging = 'Y' 
	  LEFT JOIN adempiere.ad_column_trl clm_t ON clm_t.ad_column_id = clm.ad_column_id 
	    AND clm_t.ad_language = (SELECT AD_Language FROM adempiere.AD_Client WHERE AD_Client_ID = emp.ad_client_id)
	  LEFT JOIN adempiere.AMN_Contract contract ON contract.AMN_Contract_ID = emp.AMN_Contract_ID
	  WHERE 
	      emp.created BETWEEN CAST($P{DateFrom} AS Timestamp) AND CAST($P{DateTo} AS Timestamp)
	      AND emp.isactive = 'Y' 
	UNION ALL
	-- Bloque 3: Cambios en AMN_Payroll (UPDATE)
	  SELECT 
	    chl.ad_client_id AS client_id,
	    chl.ad_org_id AS org_id,
	    usr.name AS user_value,
	    usr.description AS user_name,
	    'AMN_Payroll - Tabla de recibos de Nómina' AS tablename,
	    COALESCE(clm_t.name, clm.columnname, '') AS columnname,
	    chl.oldvalue,
	    chl.newvalue,
	    CAST(chl.created AS Timestamp) AS created,
	    CASE 
	      WHEN chl.oldvalue IS NULL AND chl.newvalue IS NOT NULL THEN 'INS'
	      WHEN chl.oldvalue IS NOT NULL AND chl.newvalue IS NOT NULL THEN 'UPD'
	      ELSE 'OTR'
	    END AS cambio_tipo,
   	    emp.amn_employee_id,
	    emp.value AS employee_value,
	    emp.name AS employee_name,
   	    proc.amn_process_id,
	    COALESCE(proc.value, 'SIN PROCESO') AS process_value,
	    contract.amn_contract_id,
	    COALESCE(contract.value, '???') || ' - ' || COALESCE(contract.name, '') AS contract_display,
	    chl.ad_changelog_id,
	    chl.record_id,
	    chl.ad_table_id
	  FROM adempiere.AD_Changelog chl
	  JOIN adempiere.ad_user usr ON usr.ad_user_id = chl.createdby
	  JOIN adempiere.ad_table tbl ON tbl.ad_table_id = chl.ad_table_id
	  JOIN adempiere.ad_column clm ON clm.ad_column_id = chl.ad_column_id
	  LEFT JOIN adempiere.ad_column_trl clm_t ON clm_t.ad_column_id = clm.ad_column_id 
	    AND clm_t.ad_language = (SELECT AD_Language FROM adempiere.AD_Client WHERE AD_Client_ID = chl.ad_client_id)
	  LEFT JOIN adempiere.AMN_Payroll pay ON pay.AMN_Payroll_ID = chl.Record_ID
	  LEFT JOIN adempiere.AMN_Employee emp ON emp.AMN_Employee_ID = pay.AMN_Employee_ID
	  LEFT JOIN adempiere.AMN_Process proc ON proc.AMN_Process_ID = pay.AMN_Process_ID
	  LEFT JOIN adempiere.AMN_Contract contract ON contract.AMN_Contract_ID = pay.AMN_Contract_ID
	  WHERE tbl.tablename = 'AMN_Payroll'
		  AND chl.created BETWEEN CAST($P{DateFrom} AS Timestamp) AND CAST($P{DateTo} AS Timestamp)
	UNION ALL
	-- INICIO DE NUEVO BLOQUE: CREACIÓN DE AMN_Payroll (INSERT)
	  SELECT
	    pay.ad_client_id AS client_id,
	    pay.ad_org_id AS org_id,
	    usr.name AS user_value,
	    usr.description AS user_name,
	    'AMN_Payroll - Tabla de recibos de Nómina' AS tablename,
	    COALESCE(clm_t.name, clm.columnname, '') AS columnname,
	    NULL AS oldvalue,
	    'Registro Creado' AS newvalue,  
	    CAST(pay.created AS Timestamp) AS created,
	    'INS' AS cambio_tipo,         
	    emp.amn_employee_id,
	    emp.value AS employee_value,
	    emp.name AS employee_name,
	    proc.amn_process_id,
	    COALESCE(proc.value, 'SIN PROCESO') AS process_value,
	    contract.amn_contract_id,
	    COALESCE(contract.value, '???') || ' - ' || COALESCE(contract.name, '') AS contract_display,
	    NULL AS ad_changelog_id,       
	    pay.AMN_Payroll_ID AS record_id,
	    (SELECT ad_table_id FROM ad_table WHERE tablename = 'AMN_Payroll') AS ad_table_id
	  FROM adempiere.AMN_Payroll pay
	  JOIN adempiere.ad_user usr ON usr.ad_user_id = pay.createdby 
  	  JOIN adempiere.ad_table tbl ON tbl.tablename = 'AMN_Payroll'
	  JOIN adempiere.ad_column clm ON clm.ad_table_id = tbl.ad_table_id AND clm.isallowlogging = 'Y' -- Filtra solo las columnas con registro activo
	  LEFT JOIN adempiere.ad_column_trl clm_t ON clm_t.ad_column_id = clm.ad_column_id 
	    AND clm_t.ad_language = (SELECT AD_Language FROM adempiere.AD_Client WHERE AD_Client_ID = pay.ad_client_id)
	  LEFT JOIN adempiere.AMN_Employee emp ON emp.AMN_Employee_ID = pay.AMN_Employee_ID
	  LEFT JOIN adempiere.AMN_Process proc ON proc.AMN_Process_ID = pay.AMN_Process_ID
	  LEFT JOIN adempiere.AMN_Contract contract ON contract.AMN_Contract_ID = pay.AMN_Contract_ID
	  WHERE 
	      pay.created BETWEEN CAST($P{DateFrom} AS Timestamp) AND CAST($P{DateTo} AS Timestamp)
	      AND pay.isactive = 'Y' 
	UNION ALL
	-- Bloque 5: Cambios en AMN_Payroll_Detail (UPDATE)
	  SELECT 
	    chl.ad_client_id AS client_id,
	    chl.ad_org_id AS org_id,
	    usr.name AS user_value,
	    usr.description AS user_name,
	    'AMN_Payroll_Detail - Tabla de detalle de recibos de Nómina' AS tablename,
	    COALESCE(clm_t.name, clm.columnname, '') AS columnname,
	    chl.oldvalue,
	    chl.newvalue,
	    CAST(chl.created AS Timestamp) AS created,
	    CASE 
	      WHEN chl.oldvalue IS NULL AND chl.newvalue IS NOT NULL THEN 'INS'
	      WHEN chl.oldvalue IS NOT NULL AND chl.newvalue IS NOT NULL THEN 'UPD'
	      ELSE 'OTR'
	    END AS cambio_tipo,
   	    emp.amn_employee_id,
	    emp.value AS employee_value,
	    emp.name AS employee_name,
   	    proc.amn_process_id,
	    COALESCE(proc.value, 'SIN PROCESO') AS process_value,
	    contract.amn_contract_id,
	    COALESCE(contract.value, '???') || ' - ' || COALESCE(contract.name, '') AS contract_display,
	    chl.ad_changelog_id,
	    chl.record_id,
	    chl.ad_table_id
	  FROM adempiere.AD_Changelog chl
	  JOIN adempiere.ad_user usr ON usr.ad_user_id = chl.createdby
	  JOIN adempiere.ad_table tbl ON tbl.ad_table_id = chl.ad_table_id
	  JOIN adempiere.ad_column clm ON clm.ad_column_id = chl.ad_column_id
	  LEFT JOIN adempiere.ad_column_trl clm_t ON clm_t.ad_column_id = clm.ad_column_id 
	    AND clm_t.ad_language = (SELECT AD_Language FROM adempiere.AD_Client WHERE AD_Client_ID = chl.ad_client_id)
	  LEFT JOIN adempiere.AMN_Payroll_detail payd ON payd.AMN_Payroll_Detail_ID = chl.Record_ID
	  LEFT JOIN adempiere.AMN_Payroll pay ON pay.AMN_Payroll_ID = payd.AMN_Payroll_ID
	  LEFT JOIN adempiere.AMN_Employee emp ON emp.AMN_Employee_ID = pay.AMN_Employee_ID
	  LEFT JOIN adempiere.AMN_Process proc ON proc.AMN_Process_ID = pay.AMN_Process_ID
	  LEFT JOIN adempiere.AMN_Contract contract ON contract.AMN_Contract_ID = pay.AMN_Contract_ID
	  WHERE tbl.tablename = 'AMN_Payroll_Detail'	
	  	AND chl.created BETWEEN CAST($P{DateFrom} AS Timestamp) AND CAST($P{DateTo} AS Timestamp)
	UNION ALL
	-- INICIO DE NUEVO BLOQUE: CREACIÓN DE AMN_Payroll_Detail (INSERT)
	  SELECT
	    payd.ad_client_id AS client_id,
	    payd.ad_org_id AS org_id,
	    usr.name AS user_value,
	    usr.description AS user_name,
	    'AMN_Payroll_Detail - Tabla de detalle de recibos de Nómina' AS tablename,
	    COALESCE(clm_t.name, clm.columnname, '') AS columnname, 
	    NULL AS oldvalue,
	    'Registro Creado' AS newvalue,  -- Valor fijo para indicar creación
	    CAST(payd.created AS Timestamp) AS created,
	    'INS' AS cambio_tipo,          -- Indicador de CREACIÓN
	    emp.amn_employee_id,
	    emp.value AS employee_value,
	    emp.name AS employee_name,
	    proc.amn_process_id,
	    COALESCE(proc.value, 'SIN PROCESO') AS process_value,
	    contract.amn_contract_id,
	    COALESCE(contract.value, '???') || ' - ' || COALESCE(contract.name, '') AS contract_display,
	    NULL AS ad_changelog_id,       -- No hay ID de ChangeLog
	    payd.AMN_Payroll_Detail_ID AS record_id,
	    (SELECT ad_table_id FROM ad_table WHERE tablename = 'AMN_Payroll_Detail') AS ad_table_id
	  FROM adempiere.AMN_Payroll_detail payd
	  JOIN adempiere.ad_user usr ON usr.ad_user_id = payd.createdby -- Usamos createdby para saber quién creó
	  JOIN adempiere.ad_table tbl ON tbl.tablename = 'AMN_Payroll_Detail'
	  JOIN adempiere.ad_column clm ON clm.ad_table_id = tbl.ad_table_id AND clm.IsAllowLogging = 'Y'
	  LEFT JOIN adempiere.ad_column_trl clm_t ON clm_t.ad_column_id = clm.ad_column_id 
		    AND clm_t.ad_language = (SELECT AD_Language FROM adempiere.AD_Client WHERE AD_Client_ID = payd.ad_client_id)
	  LEFT JOIN adempiere.AMN_Payroll pay ON pay.AMN_Payroll_ID = payd.AMN_Payroll_ID
	  LEFT JOIN adempiere.AMN_Employee emp ON emp.AMN_Employee_ID = pay.AMN_Employee_ID
	  LEFT JOIN adempiere.AMN_Process proc ON proc.AMN_Process_ID = pay.AMN_Process_ID
	  LEFT JOIN adempiere.AMN_Contract contract ON contract.AMN_Contract_ID = pay.AMN_Contract_ID
	  WHERE 
	      payd.created BETWEEN CAST($P{DateFrom} AS Timestamp) AND CAST($P{DateTo} AS Timestamp)
	      AND payd.isactive = 'Y' 
	-- FIN DE NUEVO BLOQUE
	) auditoria
	WHERE auditoria.columnname NOT LIKE '%\_UU' ESCAPE '\'
	  AND auditoria.ad_table_id IN ($P!{AD_Table_ID})
	  AND (
        CASE 
            WHEN ($P{isShowInsert} = 'Y' AND auditoria.cambio_tipo='INS') THEN TRUE
            WHEN ($P{isShowUpdate} = 'Y' AND auditoria.cambio_tipo='UPD') THEN TRUE
            WHEN ($P{isShowDelete} = 'Y' AND auditoria.cambio_tipo='DEL') THEN TRUE
            WHEN (auditoria.cambio_tipo='OTR') THEN TRUE
            ELSE FALSE 
        END
    )
) AS empchglog ON (1=0)
WHERE (imp_header= 1) OR (client_id= $P{AD_Client_ID}
	AND ( CASE WHEN ( $P{AD_Org_ID}  IS NULL OR $P{AD_Org_ID} = 0 OR org_id= $P{AD_Org_ID} ) THEN 1=1 ELSE 1=0 END ) )
	AND	( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR empchglog.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR empchglog.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR empchglog.amn_employee_id= $P{AMN_Employee_ID} ) THEN 1=1 ELSE 1=0 END )
ORDER BY empchglog.tablename, empchglog.process_value, empchglog.contract_display, empchglog.employee_value, empchglog.created, empchglog.columnname

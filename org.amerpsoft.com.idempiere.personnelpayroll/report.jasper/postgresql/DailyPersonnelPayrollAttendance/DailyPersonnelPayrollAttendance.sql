SELECT *
FROM
(   SELECT * 
    FROM
    (
        SELECT DISTINCT
	  -- ORGANIZATION
	  org.ad_client_id as org_client, org.ad_org_id as org_org,
	  CASE WHEN $P{AD_Org_ID} = 0 THEN concat(COALESCE(cli.name,cli.value),' - Consolidado') ELSE coalesce(org.name,org.value,'') END as org_name,
	  CASE WHEN $P{AD_Org_ID} = 0 THEN concat(COALESCE(cli.description,cli.name),' - Consolidado') ELSE COALESCE(org.description,org.name,org.value,'') END as org_description, 
	  CASE WHEN $P{AD_Org_ID} = 0 THEN '' ELSE COALESCE(orginfo.taxid,'') END as org_taxid,
	  CASE WHEN $P{AD_Org_ID} = 0 THEN img1.binarydata ELSE img2.binarydata END as org_logo,
	  CASE WHEN  org.ad_client_id = $P{AD_Org_ID} AND $P{AD_Client_ID} = 0 THEN 1
	       WHEN  org.ad_client_id = $P{AD_Org_ID} AND org.ad_org_id= $P{AD_Client_ID} THEN 1
	       ELSE 0 
	  END as imp_org
	 FROM adempiere.ad_org as org
	 INNER JOIN adempiere.ad_client as cli ON (org.ad_client_id = cli.ad_client_id)
	 INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
	  LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
	 INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
	  LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
	) as org_inf
	FULL JOIN
	(SELECT DISTINCT
	  -- ORGANIZACIÓN
	  pyr_asp.ad_client_id as client_id, pyr_asp.ad_org_id as org_id,
	  -- LOCATION
	  lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
	  CASE WHEN ( $P{AMN_Location_ID} IS NULL OR lct.amn_location_id= $P{AMN_Location_ID} ) THEN 1 ELSE 0 END AS imp_localidad,
	  -- SHIFT
	  shf.value as codigo_shift, COALESCE(shf.name, shf.description) as nombre_shift,
	  CASE WHEN ( $P{AMN_Shift_ID} IS NULL OR shf.amn_shift_id= $P{AMN_Shift_ID} ) THEN 1 ELSE 0 END AS imp_shift, 
	  -- CONTRACT
	  amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
	  CASE WHEN ( $P{AMN_Contract_ID} IS NULL OR amc.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1 ELSE 0 END AS imp_contrato, 
	  -- EMPLOYEE
	  emp.amn_employee_id, emp.value as value_emp, emp.name as nombre_emp,
	  CASE WHEN ( $P{AMN_Employee_ID}  IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID}  ) THEN 1 ELSE 0 END AS imp_empleado,
	  -- BPARTNER
	  cbp.taxid as nro_id,
	  -- PAYROLL_ASSIST_PROC
	  pyr_asp.event_date as fecha, pyr_asp.description as observaciones,
	  pyr_asp.shift_hed as hed, pyr_asp.shift_hen as hen, pyr_asp.shift_hnd as hnd, pyr_asp.shift_hnn as hnn,
	  pyr_asp.shift_attendance as bono_asist, pyr_asp.shift_attendancebonus as bono_assistpuntual,
	  CONCAT(
	   CASE WHEN CAST(extract(hour from pyr_asp.shift_in1) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_in1) as text))
	        ELSE CAST(extract(hour from pyr_asp.shift_in1) as text)
	   END, ':', 
	   CASE WHEN CAST(extract(minute from pyr_asp.shift_in1) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_in1) as text))
	        ELSE CAST(extract(minute from pyr_asp.shift_in1) as text)
	   END
	  ) as e1,
	  CONCAT(
	   CASE WHEN CAST(extract(hour from pyr_asp.shift_out1) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_out1) as text))
		ELSE CAST(extract(hour from pyr_asp.shift_out1) as text)
	   END, ':', 
	   CASE WHEN CAST(extract(minute from pyr_asp.shift_out1) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_out1) as text))
		ELSE CAST(extract(minute from pyr_asp.shift_out1) as text)
	   END
	  ) as s1,
	  CONCAT(
	   CASE WHEN CAST(extract(hour from pyr_asp.shift_in2) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_in2) as text))
	        ELSE CAST(extract(hour from pyr_asp.shift_in2) as text)
	   END, ':', 
	   CASE WHEN CAST(extract(minute from pyr_asp.shift_in2) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_in2) as text))
		ELSE CAST(extract(minute from pyr_asp.shift_in2) as text)
	   END
	  ) as e2,  
	  CONCAT(
	   CASE WHEN CAST(extract(hour from pyr_asp.shift_out2) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_out2) as text))
		ELSE CAST(extract(hour from pyr_asp.shift_out2) as text)
	   END, ':', 
	   CASE WHEN CAST(extract(minute from pyr_asp.shift_out2) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_out2) as text))
		ELSE CAST(extract(minute from pyr_asp.shift_out2) as text)
	   END
	  ) as s2
	FROM adempiere.amn_payroll_assist_proc as pyr_asp
	 LEFT JOIN adempiere.amn_employee as emp ON (pyr_asp.amn_employee_id = emp.amn_employee_id)
	 LEFT JOIN adempiere.amn_contract as amc ON (emp.amn_contract_id = amc.amn_contract_id)
	 LEFT JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id = cbp.c_bpartner_id)
	 LEFT JOIN adempiere.amn_location as lct ON (emp.amn_location_id= lct.amn_location_id)
	 LEFT JOIN adempiere.amn_shift as shf ON (shf.amn_shift_id= pyr_asp.amn_shift_id)
	) as asistencia ON (1= 0)
    ) as asistencia_ant_sig
    LEFT JOIN
    (
        SELECT 
         pyr_asp.amn_employee_id as emp_id, 
         CONCAT(
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_in1) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_in1) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_in1) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_in1) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_in1) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_in1) as text)
	  END
          ,' ',
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_out1) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_out1) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_out1) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_out1) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_out1) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_out1) as text)
	  END
	  ,' ',
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_in2) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_in2) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_in2) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_in2) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_in2) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_in2) as text)
	  END
	  ,' ',
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_out2) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_out2) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_out2) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_out2) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_out2) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_out2) as text)
	  END 
	 ) as dia_anterior
        FROM adempiere.amn_payroll_assist_proc as pyr_asp  
        WHERE pyr_asp.event_date = DATE( $P{DateEnd} ) - CAST('1 days' AS INTERVAL)
    ) as fecha_anterior 
    ON (fecha_anterior.emp_id = asistencia_ant_sig.amn_employee_id)
    LEFT JOIN
    (
        SELECT 
        pyr_asp.amn_employee_id as emp_id2, 
        CONCAT(
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_in1) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_in1) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_in1) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_in1) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_in1) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_in1) as text)
	  END
          ,' ',
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_out1) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_out1) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_out1) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_out1) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_out1) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_out1) as text)
	  END
	  ,' ',
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_in2) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_in2) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_in2) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_in2) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_in2) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_in2) as text)
	  END
	  ,' ',
	  CASE WHEN CAST(extract(hour from pyr_asp.shift_out2) as integer) < 10 THEN CONCAT('0', CAST(extract(hour from pyr_asp.shift_out2) as text))
	       ELSE CAST(extract(hour from pyr_asp.shift_out2) as text)
	  END, ':', 
	  CASE WHEN CAST(extract(minute from pyr_asp.shift_out2) as integer) < 10 THEN CONCAT('0', CAST(extract(minute from pyr_asp.shift_out2) as text))
	       ELSE CAST(extract(minute from pyr_asp.shift_out2) as text)
	  END 
	 ) as dia_siguiente
	FROM adempiere.amn_payroll_assist_proc as pyr_asp  
	WHERE pyr_asp.event_date = DATE( $P{DateEnd} ) + CAST('1 days' AS INTERVAL)
    ) as fecha_siguiente
    ON (fecha_siguiente.emp_id2 = asistencia_ant_sig.amn_employee_id)
WHERE (imp_org= 1) 
   OR (client_id= $P{AD_Client_ID} AND org_id= $P{AD_Org_ID}
  AND fecha= DATE( $P{DateEnd} )
  AND imp_localidad= 1 AND imp_shift= 1 AND imp_contrato= 1 AND imp_empleado= 1)
ORDER BY loc_value, codigo_shift, value_emp, fecha, imp_org ASC
-- Personnel Payroll Attendance
--
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
     emp.amn_employee_id, emp.value as value_emp, emp.name as nombre_emp, emp.biocode,
     CASE WHEN ( $P{AMN_Employee_ID} IS NULL OR emp.amn_employee_id= $P{AMN_Employee_ID} ) THEN 1 ELSE 0 END AS imp_empleado,  
     -- BPARTNER
     cbp.taxid as nro_id,
     -- PAYROLL_ASSIST_PROC
     pyr_asp.description as observaciones, pyr_asp.event_date as fecha,
     CASE WHEN pyr_asp.dayofweek= '2' THEN 'Lunes'
          WHEN pyr_asp.dayofweek= '3' THEN 'Martes'
          WHEN pyr_asp.dayofweek= '4' THEN 'Miércoles'
          WHEN pyr_asp.dayofweek= '5' THEN 'Jueves'
          WHEN pyr_asp.dayofweek= '6' THEN 'Viernes'
          WHEN pyr_asp.dayofweek= '7' THEN 'Sábado'
          WHEN pyr_asp.dayofweek= '1' THEN 'Domingo'
     END as dia_semana, 
     CONCAT(
	   CASE WHEN CAST(extract(day from pyr_asp.event_date) as integer) < 10 THEN CONCAT('0', CAST(extract(day from pyr_asp.event_date) as text))
	        ELSE CAST(extract(day from pyr_asp.event_date) as text)
	   END
     ) as dd,
     CONCAT(
	   CASE WHEN CAST(extract(month from pyr_asp.event_date) as integer) < 10 THEN CONCAT('0', CAST(extract(month from pyr_asp.event_date) as text))
	        ELSE CAST(extract(month from pyr_asp.event_date) as text)
	   END
     ) as mm,
     CAST(extract(year from pyr_asp.event_date) as text) as aaaa,
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
	  ) as s2,
	  pyr_asp.descanso, pyr_asp.excused,
	  CASE WHEN nbd.date1 IS NOT NULL THEN 'Y' ELSE 'N' END as feriado,
	  pyr_asp.shift_hed as hed, pyr_asp.shift_hen as hen, pyr_asp.shift_hnd as hnd, pyr_asp.shift_hnn as hnn,
	  pyr_asp.shift_attendance as bono_asist, pyr_asp.shift_attendancebonus as bono_assistpuntual
        FROM adempiere.amn_payroll_assist_proc as pyr_asp
	 LEFT JOIN adempiere.amn_employee as emp ON (pyr_asp.amn_employee_id = emp.amn_employee_id)
	 LEFT JOIN adempiere.amn_contract as amc ON (emp.amn_contract_id = amc.amn_contract_id)
	 LEFT JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id = cbp.c_bpartner_id)
	 LEFT JOIN adempiere.amn_location as lct ON (emp.amn_location_id= lct.amn_location_id)
	 LEFT JOIN adempiere.amn_shift as shf ON (shf.amn_shift_id= pyr_asp.amn_shift_id)
	 LEFT JOIN adempiere.c_nonbusinessday as nbd ON (nbd.date1= pyr_asp.event_date)
	) as asistencia ON (1= 0)
WHERE (imp_header= 1) OR (client_id= $P{AD_Client_ID} AND org_id= $P{AD_Org_ID}
  AND imp_localidad= 1 AND imp_shift= 1 AND imp_contrato= 1 AND imp_empleado= 1
  AND fecha BETWEEN  DATE($P{DateIni})  AND  DATE($P{DateEnd})
  )
ORDER BY asistencia.c_value, asistencia.loc_value, asistencia.value_emp, asistencia.fecha, header_info ASC
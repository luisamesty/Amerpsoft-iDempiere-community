-- Personnel Payroll Attendance
-- Version 2
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
 (SELECT
     -- ORGANIZACIÓN
     emp.ad_client_id as client_id, emp.ad_org_id as org_id,
     -- LOCATION
     lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
     -- CONTRACT
     amc.amn_contract_id, amc.value as c_value, COALESCE(amc.name, amc.description) as c_tipo, 
     -- EMPLOYEE
     emp.amn_employee_id, emp.value as value_emp, emp.name as nombre_emp, emp.biocode, 
     gs.fecha,
     -- BPARTNER
     cbp.taxid as nro_id,
     -- SHIFT
     COALESCE(shf.amn_shift_id, CAST(0 AS int)) AS  amn_shift_id,
     COALESCE(shf.value,'') as codigo_shift, 
     COALESCE(shf.name, shf.description,'') as nombre_shift,
     -- PAYROLL_ASSIST_PROC
     COALESCE(pyr_asp.description,'') as observaciones, 
     COALESCE(pyr_asp.event_date,gs.fecha) as event_date,
     CASE WHEN TO_CHAR(gs.fecha, 'D') = '2' THEN 'Lunes'
          WHEN TO_CHAR(gs.fecha, 'D') = '3' THEN 'Martes'
          WHEN TO_CHAR(gs.fecha, 'D') = '4' THEN 'Miércoles'
          WHEN TO_CHAR(gs.fecha, 'D') = '5' THEN 'Jueves'
          WHEN TO_CHAR(gs.fecha, 'D') = '6' THEN 'Viernes'
          WHEN TO_CHAR(gs.fecha, 'D') = '7' THEN 'Sábado'
          WHEN TO_CHAR(gs.fecha, 'D') = '1' THEN 'Domingo'
     END as dia_semana,
     CONCAT(
	   CASE WHEN CAST(extract(day from gs.fecha) as integer) < 10 THEN CONCAT('0', CAST(extract(day from gs.fecha) as text))
	        ELSE CAST(extract(day from gs.fecha) as text)
	   END
     ) as dd,
     CONCAT(
	   CASE WHEN CAST(extract(month from gs.fecha) as integer) < 10 THEN CONCAT('0', CAST(extract(month from gs.fecha) as text))
	        ELSE CAST(extract(month from gs.fecha) as text)
	   END
     ) as mm,
     CAST(extract(year from gs.fecha) as text) as aaaa,
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
	  COALESCE(pyr_asp.descanso,'N') AS descanso, 
	  COALESCE(pyr_asp.excused,'N') AS excused,
	  CASE WHEN nbd.date1 IS NOT NULL THEN 'Y' ELSE 'N' END as feriado,
	  COALESCE(pyr_asp.shift_hed, CAST(0 AS int)) AS  hed, 
	  COALESCE(pyr_asp.shift_hen, CAST(0 AS int)) as hen, 
	  COALESCE(pyr_asp.shift_hnd, CAST(0 AS int)) as hnd, 
	  COALESCE(pyr_asp.shift_hnn, CAST(0 AS int)) as hnn,
	  COALESCE(pyr_asp.shift_attendance, CAST(0 AS int)) as bono_asist, 
	  COALESCE(pyr_asp.shift_attendancebonus, CAST(0 AS int)) as bono_assistpuntual
     FROM adempiere.amn_employee as emp
	 INNER JOIN adempiere.amn_contract as amc ON (emp.amn_contract_id = amc.amn_contract_id)
	 INNER JOIN adempiere.c_bpartner as cbp ON (emp.c_bpartner_id = cbp.c_bpartner_id)
	 INNER JOIN adempiere.amn_location as lct ON (emp.amn_location_id= lct.amn_location_id)
	 CROSS JOIN generate_series(
	    DATE($P{DateIni}),
	    DATE($P{DateEnd}),
	    INTERVAL '1 day'
	 ) AS gs(fecha)
	 LEFT JOIN adempiere.amn_payroll_assist_proc as pyr_asp ON (pyr_asp.amn_employee_id = emp.amn_employee_id AND pyr_asp.event_date = gs.fecha)
	 LEFT JOIN adempiere.amn_shift as shf ON (shf.amn_shift_id= pyr_asp.amn_shift_id)
	 LEFT JOIN adempiere.c_nonbusinessday as nbd ON (nbd.date1= pyr_asp.event_date)
	) as asistencia ON (1= 0)
WHERE (imp_header= 1) OR (client_id= $P{AD_Client_ID} AND org_id= $P{AD_Org_ID}
  AND CASE WHEN ( $P{AMN_Location_ID} IS NULL OR asistencia.amn_location_id= $P{AMN_Location_ID} ) THEN 1=1 ELSE 1=0 END     
  AND CASE WHEN ( $P{AMN_Contract_ID} IS NULL OR asistencia.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END  
  AND CASE WHEN ( $P{AMN_Employee_ID} IS NULL OR asistencia.amn_employee_id= $P{AMN_Employee_ID} ) THEN 1=1 ELSE 1=0 END  
  AND fecha BETWEEN  DATE($P{DateIni})  AND  DATE($P{DateEnd})
  )
ORDER BY asistencia.c_value, asistencia.loc_value, asistencia.value_emp, asistencia.fecha, header_info ASC
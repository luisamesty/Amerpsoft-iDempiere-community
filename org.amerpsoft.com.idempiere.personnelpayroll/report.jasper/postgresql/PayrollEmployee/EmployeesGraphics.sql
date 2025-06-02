-- EmployeeGraphics
-- Employee Graphics V2
SELECT
	-- REPORT HEADER
	img1.binarydata as rep_logo,
    concat(COALESCE(cli.name,cli.value),' - Consolidado') as rep_name,
	-- GRAPHIC Fields
    loc.orgname AS Empresa,
    CASE WHEN emp.sex = 'M' THEN 'Hombres' ELSE 'Mujeres' END AS sex,
    SUM(CASE WHEN emp.sex = 'M' THEN 1 ELSE 0 END) AS Hombres,
    SUM(CASE WHEN emp.sex = 'F' THEN 1 ELSE 0 END) AS Mujeres,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 18 AND 25 THEN 1 ELSE 0 END) AS edad1,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 26 AND 35 THEN 1 ELSE 0 END) AS edad2,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 36 AND 45 THEN 1 ELSE 0 END) AS edad3,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 46 AND 60 THEN 1 ELSE 0 END) AS edad4,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) > 60 THEN 1 ELSE 0 END) AS edad5
FROM AMN_Employee emp
LEFT JOIN AMN_Location loc ON loc.AMN_Location_ID = emp.AMN_Location_ID
INNER JOIN adempiere.ad_client as cli ON (emp.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
LEFT JOIN adempiere.ad_image as img1 ON (cliinfo.logoreport_id = img1.ad_image_id)
INNER JOIN adempiere.ad_org as org ON (emp.ad_orgto_id = org.ad_org_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
LEFT JOIN adempiere.ad_image as img2 ON (orginfo.logo_id = img2.ad_image_id)
WHERE emp.isactive= 'Y'  AND emp.status IN ('A','V','S')
	AND  emp.ad_client_id =  $P{AD_Client_ID} 
GROUP BY rep_logo, rep_name, loc.orgname, emp.sex
ORDER BY loc.orgname, Empresa, Sex
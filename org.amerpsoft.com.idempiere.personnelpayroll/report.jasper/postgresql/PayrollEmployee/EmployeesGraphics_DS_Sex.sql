SELECT 'Hombres' AS Hombres, SUM(CASE WHEN emp.sex = 'M' THEN 1 ELSE 0 END) AS qty_Hombres,
'Mujeres' AS Mujeres, SUM(CASE WHEN emp.sex = 'F' THEN 1 ELSE 0 END) AS qty_Mujeres
FROM AMN_Employee emp
WHERE emp.isactive= 'Y'  
    AND emp.status IN ('A','V','S') 
    AND emp.ad_client_id = $P{AD_Client_ID}
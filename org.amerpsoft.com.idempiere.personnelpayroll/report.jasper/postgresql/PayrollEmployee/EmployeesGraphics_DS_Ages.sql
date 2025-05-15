SELECT
	'18-25' AS e1, '26-35' AS e2, '36-45' AS e3, '46-60' AS e4, '+ 60' AS e5,  
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 18 AND 25 THEN 1 ELSE 0 END) AS qty_e1,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 26 AND 35 THEN 1 ELSE 0 END) AS qty_e2,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 36 AND 45 THEN 1 ELSE 0 END) AS qty_e3,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) BETWEEN 46 AND 60 THEN 1 ELSE 0 END) AS qty_e4,
    SUM(CASE WHEN date_part('year', age(emp.Birthday)) > 60 THEN 1 ELSE 0 END) AS qty_e5
FROM AMN_Employee emp
WHERE emp.isactive= 'Y'  
    AND emp.status IN ('A','V','S') 
    AND emp.ad_client_id = $P{AD_Client_ID}
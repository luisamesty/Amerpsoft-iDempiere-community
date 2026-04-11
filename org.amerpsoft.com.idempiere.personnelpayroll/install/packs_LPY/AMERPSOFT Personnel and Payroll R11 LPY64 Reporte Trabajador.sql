-- 1. Borrar adjuntos previos (Archivos Jasper .jrxml/.jasper)
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepEmployees',
        'AmperRepEmployeesBySexAges'
    )
);

-- 2. Borrar parámetros previos
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepEmployees',
        'AmperRepEmployeesBySexAges'
    )
);

-- 3. Borrar traducciones de parámetros
DELETE FROM AD_Process_Para_Trl
WHERE AD_Process_Para_ID IN (
    SELECT pp.AD_Process_Para_ID 
    FROM AD_Process_Para pp
    JOIN AD_Process p ON (pp.AD_Process_ID = p.AD_Process_ID)
    WHERE p.Value IN (
        'AmperRepEmployees',
        'AmperRepEmployeesBySexAges'
    )
);
-- 1. Borrar adjuntos previos (Archivos Jasper .jrxml/.jasper)
-- AD_Table_ID 284 corresponde a la tabla AD_Process
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPersonnelPayrollAttendance',
        'AmperRepPersonnelPayrollLeavesAttendance',
        'AmperRepEmployeesBirthday',
        'AmperRepEmployeesAniversary',
        'AmperRepEmployeesDependents',
        'AmperRepEmployeesDependentsSelec'
    )
);

-- 2. Borrar parámetros previos (Evita conflictos de SeqNo y duplicados)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPersonnelPayrollAttendance',
        'AmperRepPersonnelPayrollLeavesAttendance',
        'AmperRepEmployeesBirthday',
        'AmperRepEmployeesAniversary',
        'AmperRepEmployeesDependents',
        'AmperRepEmployeesDependentsSelec'
    )
);

-- 3. Borrar traducciones de los procesos (Opcional, para limpieza profunda)
DELETE FROM AD_Process_Trl
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPersonnelPayrollAttendance',
        'AmperRepPersonnelPayrollLeavesAttendance',
        'AmperRepEmployeesBirthday',
        'AmperRepEmployeesAniversary',
        'AmperRepEmployeesDependents',
        'AmperRepEmployeesDependentsSelec'
    )
);
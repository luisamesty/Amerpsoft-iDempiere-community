-- Crear el Proceso Nuevo
INSERT INTO AD_Process (
    AD_Process_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,
    Value, Name, AD_Process_UU, AccessLevel, EntityType, IsReport, IsDirectPrint, 
    IsServerProcess
) VALUES (
    (SELECT COALESCE(MAX(AD_Process_ID), 0) + 1 FROM AD_Process WHERE AD_Process_ID < 1000000), 
    0, 0, 'Y', now(), 100, now(), 100,
    'AMN_Leaves Process', 'AMN_Leaves Process', '18f41f51-0215-4258-9d53-178df10adf03', 
    '3', 'AMERP', 'N', 'N', 'N'
);

-- 1. Borrar adjuntos previos (Archivos Jasper .jrxml/.jasper)
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
		'AMN_Leaves Process',
        'AMN_Leaves Request',
        'AMN_Leaves RequestReport',
        'AmperRepPersonnelPayrollAttendance',
        'AmperRepDailyPersonnelPayrollAttendance',
        'AmperRepDailyBiometricPersonnelAttendanc',
        'AmperProcPayrollProcessPayrollAssistOPM',
        'AmperRepPersonnelPayrollLeavesAttendance',
        'AmperRepPersonnelPayrollLeavesAttSummary',
        'AmperProcPayrollProcessPayrollAssistOneP',
        'AmperProcPayrollProcessPayrollAssistOneD',
        'AmperProcPayrollProcessPayrollAssistOnDM'
    )
);

-- 2. Borrar parámetros previos (Evita conflictos de SeqNo y duplicados)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
		'AMN_Leaves Process',
        'AMN_Leaves Request',
        'AMN_Leaves RequestReport',
        'AmperRepPersonnelPayrollAttendance',
        'AmperRepDailyPersonnelPayrollAttendance',
        'AmperRepDailyBiometricPersonnelAttendanc',
        'AmperProcPayrollProcessPayrollAssistOPM',
        'AmperRepPersonnelPayrollLeavesAttendance',
        'AmperRepPersonnelPayrollLeavesAttSummary',
        'AmperProcPayrollProcessPayrollAssistOneP',
        'AmperProcPayrollProcessPayrollAssistOneD',
        'AmperProcPayrollProcessPayrollAssistOnDM'
    )
);
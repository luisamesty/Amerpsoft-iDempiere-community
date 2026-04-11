-- 1. Borrar adjuntos previos (Archivos Jasper)
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepEmployeeCardFront',
        'AmperRepEmployeesBirthday',
        'AmperRepEmployeesAniversary',
        'AmperRepEmployeesBySexAges',
        'AmperRepPayrollHistoric',
        'AmperRepPayrollHistoricNU',
        'AmperRepPayrollListbyConcepts',
        'AmperRepPayrollListbyConcepts2Dates',
        'AmperRepPayrollListbyConcepts2DatesTB',
        'AmperRepEmployeesDependents',
        'AmperRepEmployeesDependentsSelec',
        'AmperRepEmployeeJobLetter',
        'AmperRepPayrollIPLReceiptsToolbar',
        'AmperRepPayrollAccounting',
        'AmperRepPayrollworkForce',
        'AmperRepPayrollAccountingCheck',
        'AmperRepPayrollListLandscapeAllPeriods'
    )
);

-- 2. Borrar parámetros previos (Evita conflictos de SeqNo)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepEmployeeCardFront',
        'AmperRepEmployeesBirthday',
        'AmperRepEmployeesAniversary',
        'AmperRepEmployeesBySexAges',
        'AmperRepPayrollHistoric',
        'AmperRepPayrollHistoricNU',
        'AmperRepPayrollListbyConcepts',
        'AmperRepPayrollListbyConcepts2Dates',
        'AmperRepPayrollListbyConcepts2DatesTB',
        'AmperRepEmployeesDependents',
        'AmperRepEmployeesDependentsSelec',
        'AmperRepEmployeeJobLetter',
        'AmperRepPayrollIPLReceiptsToolbar',
        'AmperRepPayrollAccounting',
        'AmperRepPayrollworkForce',
        'AmperRepPayrollAccountingCheck',
        'AmperRepPayrollListLandscapeAllPeriods'
    )
);
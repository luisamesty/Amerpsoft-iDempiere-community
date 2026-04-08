-- 1. Borrar adjuntos previos (Archivos Jasper .jrxml/.jasper)
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmfinAccountElementsJasper',
        'AmfinTrialBalanceJasperTwoDatesOrgTree',
        'AmfinTrialBalanceJasperOnePeriodOrgTree',
        'AmfinStateFinancialBalanceJasperOrg',
        'AmfinStateFinancialIntegralResultsOrgTre',
        'AmfinTrialBalanceJasperTwoDatesOrgTree',
        'AmfinAnaliticFinancialStateJasperOrg'
        
    )
);

-- 2. Borrar parámetros previos (Evita conflictos de SeqNo y duplicados)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmfinAccountElementsJasper',
        'AmfinTrialBalanceJasperTwoDatesOrgTree',
        'AmfinTrialBalanceJasperOnePeriodOrgTree',
        'AmfinStateFinancialBalanceJasperOrg',
        'AmfinStateFinancialIntegralResultsOrgTre',
        'AmfinTrialBalanceJasperTwoDatesOrgTree',
        'AmfinAnaliticFinancialStateJasperOrg'
    )
);
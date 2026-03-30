-- 1. Borrar adjuntos previos (Archivos Jasper .jrxml/.jasper)
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPayrollListforBankResume',
        'AmperRepPayrollINNReceiptToolbar',
        'AmperRepPayrollINNReceipts',
        'AmperRepPayrollICTReceipt',
        'AmperRepPayrollICTReceiptToolbar',
        'AmperRepPayrollListGrid',
        'AmperRepPayrollHistoricGridView'
    )
);

-- 2. Borrar parámetros previos (Evita conflictos de SeqNo)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPayrollListforBankResume',
        'AmperRepPayrollINNReceiptToolbar',
        'AmperRepPayrollINNReceipts',
        'AmperRepPayrollICTReceipt',
        'AmperRepPayrollICTReceiptToolbar',
        'AmperRepPayrollListGrid',
        'AmperRepPayrollHistoricGridView'
    )
);
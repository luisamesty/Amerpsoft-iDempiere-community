-- 1. Borrar adjuntos (Archivos Jasper .jrxml/.jasper) vinculados a estos procesos
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 -- ID de la tabla AD_Process
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPayrollPOReceipts',
        'AmperRepPayrollPOReceiptsToolbar',
        'AmperProcPayrollCreateOneDoc',
        'AmperProcPayrollProcessPayrollDeferredNN',
        'AmperRepPayrollPJReceiptsToolbar',
        'AmperProcPayrollCreateOneDocLiq',
        'AmperRepPayrollPLReceiptsToolbar',
        'AmperRepPayrollLoanAll',
        'AmperRepPayrollLoan',
        'AmperRepPayrollNNReceiptToolbar',
        'AmperRepPayrollNNReceipts'
    )
);

-- 2. Borrar parámetros para evitar errores de "Duplicate Key" en la secuencia (SeqNo)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPayrollPOReceipts',
        'AmperRepPayrollPOReceiptsToolbar',
        'AmperProcPayrollCreateOneDoc',
        'AmperProcPayrollProcessPayrollDeferredNN',
        'AmperRepPayrollPJReceiptsToolbar',
        'AmperProcPayrollCreateOneDocLiq',
        'AmperRepPayrollPLReceiptsToolbar',
        'AmperRepPayrollLoanAll',
        'AmperRepPayrollLoan',
        'AmperRepPayrollNNReceiptToolbar',
        'AmperRepPayrollNNReceipts'
    )
);
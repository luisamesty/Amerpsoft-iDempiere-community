-- 1. Borrar todos los adjuntos (archivos Jasper .jrxml/.jasper) de los procesos de la lista
DELETE FROM AD_Attachment 
WHERE AD_Table_ID = 284 
  AND Record_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPayrollINNReceiptToolbar', 'AmperRepPayrollINNReceipts', 'AmperRepPayrollINVReceipts',
        'AmperRepPayrollINVReceiptsToolbarRep', 'AmperRepPayrollCTReceiptToolbar', 'AmperRepPayrollCTReceipt',
        'AmperRepPayrollINNInvoiceTB', 'AmperRepPayrollPLReceiptsToolbar', 'AmperRepPayrollPLReceipts',
        'AmperRepPayrollNUReceiptsToolbar', 'AmperRepPayrollNUReceipts', 'AmperRepPayrollListbyConcepts',
        'AmperRepPayrollHistoricByConcepts', 'AmperRepPayrollHistoric', 'AmperRepPayrollListByPeriods',
        'AmperRepPayrollSummary', 'AmperRepPayrollListforBankPA', 'AmperRepPayrollListforBankCA',
        'AmperRepPayrollConcepts', 'AmperRepEmployeeFile', 'AmperRepEmployeeCardFront',
        'AmperRepEmployeeCardBack', 'AmperRepEmployeeJobLetter', 'AmperProcConceptTypesCopyAccounts',
        'AmperRepPayrollNVReceiptsToolbarRep', 'AmperRepPayrollNVReceipts', 'AmperRepPayrollNVReceiptsToolbarDoc',
        'AmperRepPayrollNVReceiptsToolbarDocA', 'AmperProcPayrollCreateOneDocReqVac', 'AmperRepPayrollListPortrait',
        'AmperRepPayrollListLandscape', 'AmperRepEmployeesShort', 'AmperRepEmployeesSelective',
        'AmperRepEmployees', 'AmperRepPayrollPOReceiptsToolbar', 'AmperRepPayrollPOReceipts',
        'AmperRepPayrollListGrid', 'AmperRepPayrollListbyConcepts2Dates', 'AmperRepPayrollListbyConcepts2DatesTB',
        'AmperRepPayrollListCommission', 'AmperRepEmployeesReview', 'AmperRepPayrollListSocialSecurity',
        'AmperProcPayrollRefreshOnePeriodConcept', 'AmperRepPayrollHistoricGridView', 'AmperProcPayrollDelete',
        'AmperRepPayrollListforBank', 'AmperRepPayrollListforBankManual', 
        'AmperRepPayrollCTReceipt', 'AmperRepPayrollPLReceipts',
        'AmperRepPayrollNVReceipts', 'AmperRepPayrollNVReceiptsToolbar', 'AmperRepPayrollNVReceiptsToolbarDoc', 'AmperRepPayrollNVReceiptsToolbarDocA', 'AmperRepPayrollNVReceiptsToolbarDocC', 'AmperRepPayrollNVReceiptsToolbarRep', 'AmperRepPayrollNVReceiptsToolbarRepAll',
        'AmperRepPayrollNNReceipts', 'AmperRepPayrollNNInvoiceTB', 'AmperRepPayrollNNReceiptToolbar',
        'AmperRepPayrollPLReceipts', 'AmperRepPayrollPLReceiptsToolbar', 
        'AmperRepPayrollNUReceipts', 'AmperRepPayrollNUReceiptsToolbar', 
        'AmperRepPayrollListGrid', 'AmperRepPayrollListbyConcepts2Dates',  'AmperRepPayrollListSocialSecurity'
    )
);

-- 2. Borrar todos los parámetros de estos procesos para evitar conflictos de SeqNo (Duplicidad)
DELETE FROM AD_Process_Para 
WHERE AD_Process_ID IN (
    SELECT AD_Process_ID 
    FROM AD_Process 
    WHERE Value IN (
        'AmperRepPayrollINNReceiptToolbar', 'AmperRepPayrollINNReceipts', 'AmperRepPayrollINVReceipts',
        'AmperRepPayrollINVReceiptsToolbarRep', 'AmperRepPayrollCTReceiptToolbar', 'AmperRepPayrollCTReceipt',
        'AmperRepPayrollINNInvoiceTB', 'AmperRepPayrollPLReceiptsToolbar', 'AmperRepPayrollPLReceipts',
        'AmperRepPayrollNUReceiptsToolbar', 'AmperRepPayrollNUReceipts', 'AmperRepPayrollListbyConcepts',
        'AmperRepPayrollHistoricByConcepts', 'AmperRepPayrollHistoric', 'AmperRepPayrollListByPeriods',
        'AmperRepPayrollSummary', 'AmperRepPayrollListforBankPA', 'AmperRepPayrollListforBankCA',
        'AmperRepPayrollConcepts', 'AmperRepEmployeeFile', 'AmperRepEmployeeCardFront',
        'AmperRepEmployeeCardBack', 'AmperRepEmployeeJobLetter', 'AmperProcConceptTypesCopyAccounts',
        'AmperRepPayrollNVReceiptsToolbarRep', 'AmperRepPayrollNVReceipts', 'AmperRepPayrollNVReceiptsToolbarDoc',
        'AmperRepPayrollNVReceiptsToolbarDocA', 'AmperProcPayrollCreateOneDocReqVac', 'AmperRepPayrollListPortrait',
        'AmperRepPayrollListLandscape', 'AmperRepEmployeesShort', 'AmperRepEmployeesSelective',
        'AmperRepEmployees', 'AmperRepPayrollPOReceiptsToolbar', 'AmperRepPayrollPOReceipts',
        'AmperRepPayrollListGrid', 'AmperRepPayrollListbyConcepts2Dates', 'AmperRepPayrollListbyConcepts2DatesTB',
        'AmperRepPayrollListCommission', 'AmperRepEmployeesReview', 'AmperRepPayrollListSocialSecurity',
        'AmperProcPayrollRefreshOnePeriodConcept', 'AmperRepPayrollHistoricGridView', 'AmperProcPayrollDelete',
        'AmperRepPayrollListforBank', 'AmperRepPayrollListforBankManual',
        'AmperRepPayrollCTReceipt', 'AmperRepPayrollPLReceipts',
        'AmperRepPayrollNVReceipts', 'AmperRepPayrollNVReceiptsToolbar', 'AmperRepPayrollNVReceiptsToolbarDoc', 'AmperRepPayrollNVReceiptsToolbarDocA', 'AmperRepPayrollNVReceiptsToolbarDocC', 'AmperRepPayrollNVReceiptsToolbarRep', 'AmperRepPayrollNVReceiptsToolbarRepAll',
        'AmperRepPayrollNNReceipts', 'AmperRepPayrollNNInvoiceTB', 'AmperRepPayrollNNReceiptToolbar',
        'AmperRepPayrollPLReceipts', 'AmperRepPayrollPLReceiptsToolbar', 
        'AmperRepPayrollNUReceipts', 'AmperRepPayrollNUReceiptsToolbar', 
        'AmperRepPayrollListGrid', 'AmperRepPayrollListbyConcepts2Dates',  'AmperRepPayrollListSocialSecurity'
    )
);
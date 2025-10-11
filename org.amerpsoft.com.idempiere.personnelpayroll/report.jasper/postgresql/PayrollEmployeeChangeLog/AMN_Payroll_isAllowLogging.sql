-- TABLA  AMN_Payroll (Recibos de Nómina - Encabezado)
-- Campos sensibles de Cambios
--
UPDATE AD_Column
SET IsAllowLogging = CASE
    WHEN ColumnName IN (
        'DocumentNo',    -- Número de documento del recibo
        'C_DocType_ID',  -- Tipo de documento
        'DateDoc',       -- Fecha de la nómina
        'AMN_Process_ID',-- Proceso de nómina asociado
        'AMN_Contract_ID',
        'AMN_Location_ID',
        'DateAcct',
        'InvDateIni',
        'InvDateEnd',
        'C_Currency_ID',
        'C_ConversionType_ID',
        'C_Currency_ID_To',
        'AmountAllocated',
        'AmountDeducted',
        'AmountCalculated',
        'AmountNetpaid',
        'DocStatus',     -- Estado del recibo (Completed, Paid, etc.)
        'IsActive'       -- Estado de activación del registro
    ) THEN 'Y'
    ELSE 'N'
END
WHERE AD_Table_ID = (SELECT AD_Table_ID FROM AD_Table WHERE TableName = 'AMN_Payroll');
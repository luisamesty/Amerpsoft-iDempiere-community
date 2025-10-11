-- TABLA  AMN_Payroll_Detail (Detalle de Recibos de Nómina - Lineas)
-- Campos sensibles de Cambios
--
UPDATE AD_Column
SET IsAllowLogging = CASE
    WHEN ColumnName IN (
    	'AMN_Payroll_Detail_ID',
        'AMN_Concept_Types_Proc_ID',
        'AMN_Concept_Types_ID',
        'AMN_Concept_Uom_ID',
        'AmountAllocated',  
        'AmountBalance',  -- Corregido: asumido como una sola columna (sin espacio)
        'AmountCalculated',
        'AmountDeducted',
        'CalcOrder',       
        'Value',           
        'QtyValue',      
        'IsActive'        
    ) THEN 'Y'
    ELSE 'N'
END
WHERE AD_Table_ID = (SELECT AD_Table_ID FROM AD_Table WHERE TableName = 'AMN_Payroll_Detail');
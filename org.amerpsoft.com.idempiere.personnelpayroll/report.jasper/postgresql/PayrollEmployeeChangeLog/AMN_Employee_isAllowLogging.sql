-- TABLA AMN_Employee
-- Campos sensibles de Cambios
--
UPDATE AD_Column
SET IsAllowLogging = CASE
    WHEN ColumnName IN (
 	'AD_OrgTo_ID',
  	'BioCode',
  	'PIN',
  	'C_BPartner_ID',
  	'Bill_BPartner_ID',
    'Value',        -- Código del empleado
    'Name',         -- Nombre completo
    'IDNumber',     -- Cédula/DNI
    'EMail',        -- Email
    'EMail2',       -- Email
    'Phone',        -- Teléfono
    'Birthday',     -- Fecha de nacimiento
    'JobCondition', -- Condición laboral
    'Status',       -- Estado (Activo, Retirado, etc.)
    'IncomeDate',   -- Fecha de ingreso
    'IsActive',     -- Estado de activación del registro
    'AMN_Contract_ID', -- Cambio de contrato
    'AMN_Jobtitle_ID',  -- Cambio de cargo
    'AMN_Location_ID',
    'AMN_Sector_ID',
    'Status',
    'Salary',
    'AMN_CommissionGroup_ID',
    'AMN_Shift_ID',
    'C_Currency_ID'
    ) THEN 'Y'
    ELSE 'N'
END
WHERE AD_Table_ID = (SELECT AD_Table_ID FROM AD_Table WHERE TableName = 'AMN_Payroll');

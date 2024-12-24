-- PayrollListBank
-- Payroll List For Bank
SELECT 
    -- MODELO ASUNCION
    -- ORGANIZACIÓN
    pyr.ad_client_id as client_id, pyr.ad_org_id as org_id,
    coalesce(org.name,org.value,'')  as org_name,
    COALESCE(org.description,org.name,org.value,'')  org_description, 
	COALESCE(orginfo.taxid,'')  as org_taxid,    
    -- LOCATION
    lct.amn_location_id, lct.value as loc_value, COALESCE(lct.name, lct.description) as localidad,
    -- PERIOD
    prd.amn_period_id, prd.name as periodo, prd.amndateini, prd.amndateend, prd.amn_period_status,
    -- CURRENCY
    curr1.iso_code as iso_code1,
    currt1.cursymbol as cursymbol1,
    COALESCE(currt1.description,curr1.iso_code,curr1.cursymbol,'') as currname1,
    curr2.iso_code as iso_code2,
    currt2.cursymbol as cursymbol2,
    COALESCE(currt2.description,curr2.iso_code,curr2.cursymbol,'') as currname2, 
    -- EMPLOYEE
    emp.amn_employee_id, 
    emp.value as codigo_trabajador, 
    emp.name as nombre_trabajador,
    COALESCE(jtt.name, jtt.description,'''') as cargo, 
    COALESCE(emp.idnumber, cbp.taxid,CONCAT('**-',RTRIM(emp.value),'-**')) as nro_id,
    -- DESCRIPCION PAGO
    CASE WHEN prc.amn_process_value = 'NN' THEN 'SALARIO PAGO'
    	WHEN prc.amn_process_value = 'NU' THEN 'AGUINALDOS PAGO'
    	ELSE 'SALARIO PAGO' END AS descripcion_pago,
    CASE WHEN prc.amn_process_value = 'NN' THEN 'NO'
    	WHEN prc.amn_process_value = 'NU' THEN 'SI'
    	ELSE 'NO' END AS descripcion_aguinaldo_sino,	
   	pyr.invdateend,
    -- CUENTAS
    COALESCE(employ1.a_name,'Cuenta Pagadora') AS cuenta_pagadora, 
    COALESCE(employ1.accountno, '** ERROR CP**') AS cuenta_pagadora_no,
    COALESCE(employ2.a_name,'Cuenta Acreedora') AS cuenta_acreedora, 
    COALESCE(employ2.accountno,'** ERROR CA **') AS cuenta_acreedora_no,
    -- PAYROLL RECEIPT
	currencyConvert(pyr.amountallocated,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountallocated, 
	currencyConvert(pyr.amountdeducted,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountdeducted, 
	currencyConvert(pyr.amountnetpaid ,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ) as amountnetpaid,
-- MODELO ASUNCION
--Campo	Tipo de campo 	Tamaño	Decimales	Posición		Formato	Requerido	Descripción
--					Inicio	Fin		S/N	
--ITIREG	Alfa	1		1	1		S	Tipo de registro enviado ‘D’ Registro de Detalle ‘C’ Registro de Control
'D' AS ITIREG,
--ITITRA	Alfa	2		2	3		S	Tipo Transferencia ‘01’ Pago de Salarios ‘02’ Pago a Proveedores ‘03’ Cobro de Factura/Cuota '09' Débitos comandados
'01' AS ITITRA,
--ICDSRV	Num/A	3	0	4	6		S	Código empresa (asignado por el Banco)
CASE WHEN cnt.value='GIO' THEN   '471' 
	WHEN cnt.value='MO2' THEN '468' END  AS ICDSRV,
--ICTDEB	Num		10	0	7	16		S N Cobro Cuotas	Nro. de cuenta para débito/Cuenta empresa
COALESCE(LPAD(TRIM(employ1.accountno), 10, '0'),RPAD('',10, '*')) AS ICTDEB,
--IBCOCR	Num		3	0	17	19		S	Nro. de Banco para crédito Obs: siempre 017
'017' AS IBCOCR,
--ICTCRE	Num		10	0	20	29		S N Cobro Cuotas	Nro. de cuenta para crédito Obs: Si pago en cheque relleno con ceros
COALESCE(LPAD(TRIM(employ2.accountno), 10, '0'),RPAD('',10, '*')) AS ICTCRE,
--ITCRDB	Alfa	1		30	30		S	Tipo débito/crédito ‘D’ Débito ‘C’ Crédito ‘H’ Cheque ‘F’ Cobro de Factura/Cuota
'C' AS ITCRDB,
--IORDEN	Alfa	50		31	80		S-Pgo.Proveedor. N-demas casos	Cheque a la orden de/Cliente Facturado/Beneficiario/Pagador 
RPAD(TRIM(LEFT(REPLACE(emp.name, ',', ''), 50)),50,' ') AS IORDEN,
--IMONED	Num		1	0	81	81		S	Moneda correspondiente al monto 0 Guaraníes 1 Dolares Obs: Para transferencias la cuenta origen debe ser de la misma moneda que la cuenta destino.
'0' AS IMONED,
--IMTOTR	Num		15	2	82	96		S	Monto Transferencia/Monto Factura, cuota Obs: últimos dos dígitos corresponde a decimales.
TRIM(REPLACE(to_char(currencyConvert(pyr.amountnetpaid ,pyr.c_currency_id, $P{C_Currency_ID}, pyr.dateacct, pyr.C_ConversionType_ID, pyr.AD_Client_ID, pyr.AD_Org_ID ), '0000000000000.00'),'.','')) AS IMTOTR,
--IMTOT2	Num		15	2	97	111		N	Monto Transferencia (segundo vencimiento) Obs: últimos dos dígitos corresponde a decimales. Solo para Cobro de Factura/Cuota.Demas 0
TRIM(RPAD('',15, '0')) AS IMTOT2,
--INRODO	Alfa	12		112	123		S-Cobro Fact/Cuo N-Demás casos	Nro. de documento Obs: cédula de identidad, RUC, Pasaporte, otros. Del beneficiario, proveedor, cliente.
RPAD('',12, ' ') AS INRODO,
--ITIFAC	Num		1		124	124		S-Pgo.Proveedor. N-demas casos	Tipo Factura 1 Factura Contado 2 Factura Crédito Solo para Pago a Proveedores. Demás 0
'0' AS ITIFAC,
--INRFAC	Alfa	20		125	144		N-Pago Salario S-Demás casos	Nro. de Factura Obs:Para pago a proveedores.Demás blancos
RPAD('',20, ' ') AS INRFAC,
--INRCUO	Num		3	0	145	147		S-Cobro Fact/Cuo N-Demás casos	Nro. de Cuota pagada/a cobrar. Solo para ‘F’ Cobro de Factura/Cuota
'000' AS INRCUO,
--IFCHCR	Num		8	0	148	155	Aaaammdd	S	Fecha para realizar el crédito/Fecha vencimiento-
TO_CHAR(CAST($P{PayDate} AS Timestamp), 'YYYYMMDD')  AS IFCHCR,
--IFCHC2	Num		8	0	156	163	Aaaammdd	N	Fecha segundo vencimiento. Solo para ’ F’ Cobro de Factura/Cuota
'00000000' AS IFCHC2,
--ICEPTO	Alfa	50		164	213		N	Comentario de concepto cobrado/pagado
RPAD('PAGO SALARIOS',50, ' ') AS ICEPTO,
--INRREF	Alfa	15		214	228		N	Referencia operación empresa
RPAD('',15, ' ') AS INRREF,
--IFECCA	Num		8	0	229	236	Aaaammdd	N	Fecha de carga de transacción
TO_CHAR(CAST($P{PayDate} AS Timestamp), 'YYYYMMDD')  AS IFECCA,
--IHORCA	Num		6		237	242	Hhmmss	N	Hora de carga de transacción
REPLACE(TO_CHAR(CAST($P{PayDate} AS Timestamp), 'HH12:MI:SS'),':','')   AS IHORCA,
--IUSUCA	Alfa	10		243	252		N	Nombre del usuario que cargó
CASE WHEN cnt.value='GIO' THEN RPAD(TRIM('LAGIOCONDA'),10, ' ')   
	WHEN cnt.value='MO2' THEN RPAD(TRIM('MONALISA'),10, ' ') 
END AS IUSUCA
FROM adempiere.amn_payroll as pyr
INNER JOIN adempiere.amn_employee as emp  ON (emp.amn_employee_id= pyr.amn_employee_id) 
INNER JOIN adempiere.c_bpartner   as cbp  ON (emp.c_bpartner_id= cbp.c_bpartner_id)
LEFT JOIN adempiere.amn_jobtitle as jtt   ON (emp.amn_jobtitle_id= jtt.amn_jobtitle_id)
INNER JOIN adempiere.amn_period   as prd  ON (prd.amn_period_id= pyr.amn_period_id)
INNER JOIN adempiere.amn_process AS prc   ON (prc.amn_process_id = pyr.amn_process_id)
INNER JOIN adempiere.amn_contract AS cnt  ON (cnt.amn_contract_id = emp.amn_contract_id)
LEFT JOIN adempiere.amn_location as lct	  ON (lct.amn_location_id= pyr.amn_location_id)
LEFT JOIN adempiere.amn_department as dpt ON (dpt.amn_department_id= pyr.amn_department_id)
LEFT JOIN c_currency curr1 on pyr.c_currency_id = curr1.c_currency_id
LEFT JOIN c_currency_trl currt1 on curr1.c_currency_id = currt1.c_currency_id and currt1.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID}) 
LEFT JOIN c_currency curr2 on curr2.c_currency_id = $P{C_Currency_ID}
LEFT JOIN c_currency_trl currt2 on curr2.c_currency_id = currt2.c_currency_id and currt2.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID=$P{AD_Client_ID})
INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
INNER JOIN adempiere.ad_clientinfo as cliinfo ON (cli.ad_client_id = cliinfo.ad_client_id)
LEFT JOIN adempiere.ad_org as org ON (org.ad_org_id = emp.ad_orgto_id)
INNER JOIN adempiere.ad_orginfo as orginfo ON (org.ad_org_id = orginfo.ad_org_id)
LEFT JOIN (
	SELECT DISTINCT ON (emp1.amn_employee_id)
		emp1.amn_employee_id,cbb1.a_name, cbb1.accountno
		FROM adempiere.amn_employee emp1
		LEFT JOIN (
			SELECT ad_client_id, c_bpartner_id, a_name, accountno FROM c_bp_bankaccount  WHERE bpbankacctuse ='N'
		)  cbb1 ON cbb1.C_BPartner_ID = emp1.C_BPartner_ID
		WHERE emp1.AD_Client_ID=$P{AD_Client_ID}
) AS employ1 ON employ1.amn_employee_id = emp.amn_employee_id 
LEFT JOIN (
	SELECT DISTINCT ON (emp2.amn_employee_id)
		emp2.amn_employee_id,cbb2.a_name, cbb2.accountno
		FROM adempiere.amn_employee emp2
		LEFT JOIN (
			SELECT ad_client_id, c_bpartner_id, a_name, accountno FROM c_bp_bankaccount  WHERE bpbankacctuse ='B'
		)  cbb2 ON cbb2.C_BPartner_ID = emp2.C_BPartner_ID
		WHERE emp2.AD_Client_ID=$P{AD_Client_ID}
) AS employ2 ON employ2.amn_employee_id = emp.amn_employee_id 
WHERE pyr.ad_client_id= $P{AD_Client_ID} 
	AND	( CASE WHEN ( $P{AMN_Process_ID}  IS NULL OR pyr.amn_process_id= $P{AMN_Process_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Contract_ID}  IS NULL OR pyr.amn_contract_id= $P{AMN_Contract_ID} ) THEN 1=1 ELSE 1=0 END )
	AND ( CASE WHEN ( $P{AMN_Period_ID}  IS NULL OR pyr.amn_period_id= $P{AMN_Period_ID} ) THEN 1=1 ELSE 1=0 END )
ORDER BY pyr.amn_period_id, cuenta_pagadora_no ASC,  emp.value ASC, pyr.amn_payroll_id ASC
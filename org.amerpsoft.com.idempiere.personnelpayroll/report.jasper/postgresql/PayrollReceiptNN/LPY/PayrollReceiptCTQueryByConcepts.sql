-- PayrollReceiptCT Query for Cross Tab Concepts
---
	SELECT * 
	FROM (
			SELECT 
				resum.amn_payroll_id,
				sum(resum.salariobasico) AS salariobasico, 
				sum(resum.subtotal) AS subtotal, 
				sum(resum.horasextras) AS horasextras,
				sum(resum.otros) AS otros,
				sum(resum.comisadm) AS comisadm,
				sum(resum.comisaph) AS comisaph,
				sum(resum.comisplaza) AS comisplaza,
				sum(resum.reposo) AS reposo,
				sum(resum.otroshaberes) AS otroshaberes,
				sum(resum.boniffam) AS boniffam,
				sum(resum.otrosingresos) AS otrosingresos,
				sum(resum.ipstrabajador) AS ipstrabajador,
				sum(resum.irptrabajador) AS irptrabajador,
				sum(resum.ausencia) AS ausencia,
				sum(resum.otrosd) AS otrosd,
				sum(resum.embargo) AS embargo,
				sum(resum.otrosdescuentos) AS otrosdescuentos,
				sum(resum.ipspatron) AS ipspatron
			FROM (
				SELECT 	
				-- RECIBO
				pyr.amn_payroll_id,
				-- TIPO DE CONCEPTO
				cty.calcorder, cty.value as cty_value, COALESCE(cty.name, cty.description) as concept_type,
				-- PAYROLL DETAIL
				-- *************
			   	-- ASIGNACIONES	  
				-- *************
				-- cty.sign = 'D'
			   	-- SB		Salario Básico Trabajador 
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SB' THEN pyr_d.amountcalculated  ELSE 0 END  as salariobasico,
			  	--SALARIO		Salario Trabajador
			  	CASE WHEN cty.sign = 'D' AND cty.Value='SALARIO' THEN pyr_d.amountallocated  ELSE 0 END  as subtotal,
			  	--HED		Horas Extras
			  	CASE WHEN cty.sign = 'D' AND cty.Value='HED' THEN pyr_d.amountallocated  ELSE 0 END  as horasextras,
				--OTROS		Otros Ingresos
			  	CASE WHEN cty.sign = 'D' AND cty.Value='OTROS' THEN pyr_d.amountallocated  ELSE 0 END  as otros,
				--COMISADM	Comisión Adm
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISADM' THEN pyr_d.amountallocated  ELSE 0 END  as comisadm,
				--COMISAPH	Comisión aph
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISAPH' THEN pyr_d.amountallocated  ELSE 0 END  as comisaph,
				--COMISPLAZA	Comisión Plaza
				CASE WHEN cty.sign = 'D' AND cty.Value='COMISPLAZA' THEN pyr_d.amountallocated  ELSE 0 END  as comisplaza,
				--REPOSO		Reposos
				CASE WHEN cty.sign = 'D' AND cty.Value='REPOSO' THEN pyr_d.amountallocated  ELSE 0 END  as reposo,
				--OTROSHAB	Otros Haberes
				CASE WHEN cty.sign = 'D' AND cty.Value='OTROSHAB' THEN pyr_d.amountallocated  ELSE 0 END  as otroshaberes,
				--BONIFFAM	Bonificación Familiar
				CASE WHEN cty.sign = 'D' AND cty.Value='BONIFFAM' THEN pyr_d.amountallocated  ELSE 0 END  as boniffam,
				-- OTROS CONCEPTOS DE ASIGNACION
				CASE WHEN  cty.sign = 'D' AND cty.Value NOT IN ('SB','SALARIO', 'HED', 'OTROS', 'COMISADM', 'COMISAPH', 'COMISPLAZA', 'REPOSO', 'OTROSHAB', 'BONIFFAM') THEN pyr_d.amountallocated  ELSE 0 END  as otrosingresos,
				-- *************
				-- DEDUCCIONES
				-- *************
				-- cty.sign = 'C'	THEN
				--IPS_T		IPS Trabajador
				CASE WHEN cty.sign = 'C' AND cty.Value='IPS_T' THEN pyr_d.amountdeducted  ELSE 0 END  as ipstrabajador,
				--IRP		IRP
				CASE WHEN cty.sign = 'C' AND cty.Value='IRP' THEN pyr_d.amountdeducted  ELSE 0 END  as irptrabajador,
				--AUSENCIA	Ausencias
				CASE WHEN cty.sign = 'C' AND cty.Value='AUSENCIA' THEN pyr_d.amountdeducted  ELSE 0 END  as ausencia,
				--OTROSD		Otros Descuentos
				CASE WHEN cty.sign = 'C' AND cty.Value='OTROSD' THEN pyr_d.amountdeducted  ELSE 0 END  as otrosd,
				--EMBARGO		Embargo Juducial
				CASE WHEN cty.sign = 'C' AND cty.Value='EMBARGO' THEN pyr_d.amountdeducted  ELSE 0 END  as embargo,
				-- OTROS CONCEPTOS DE DEDUCCION
				CASE WHEN  cty.sign = 'C' AND cty.Value NOT IN ('IPS_T','IRP', 'AUSENCIA', 'OTROS', 'OTROSD', 'EMBARGO') THEN pyr_d.amountdeducted  ELSE 0 END  as otrosdescuentos,
				-- *************
				-- OTROS CALCULOS
				-- *************
				--IPS_P		IPS Patrón
				CASE WHEN cty.sign = 'C' AND cty.Value='IPS_P' THEN pyr_d.amountcalculated  ELSE 0 END  as ipspatron
				FROM adempiere.amn_payroll as pyr
				LEFT JOIN (
					SELECT DISTINCT ON (pyr.amn_payroll_id) pyr.amn_payroll_id, pyh.salary as salary_hist , pyh.amn_period_yyyymm 
					FROM adempiere.amn_payroll as pyr
					left join AMN_Payroll_Historic pyh on pyr.amn_employee_id = pyh.amn_employee_id  and to_char(pyr.dateacct,'YYYY-MM') = to_char(pyh.validto ,'YYYY-MM')
				) as histo on histo.amn_payroll_id = pyr.amn_payroll_id
				INNER JOIN adempiere.ad_client as cli ON (pyr.ad_client_id = cli.ad_client_id)
				INNER JOIN (
					SELECT 
					pyr2.amn_payroll_id, pyr_d2.amn_payroll_detail_id, pyr_d2.amn_concept_types_proc_id, pyr_d2.qtyvalue,
					currencyConvert(pyr_d2.amountallocated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountallocated, 
				currencyConvert(pyr_d2.amountdeducted,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountdeducted, 
				currencyConvert(pyr_d2.amountcalculated,pyr2.c_currency_id, $P{C_Currency_ID}, pyr2.dateacct, NULL, pyr2.AD_Client_ID, pyr2.AD_Org_ID ) as amountcalculated 
					FROM adempiere.amn_payroll_detail AS pyr_d2
					INNER JOIN adempiere.amn_payroll pyr2 ON pyr2.amn_payroll_id = pyr_d2.amn_payroll_id 
					WHERE pyr2.amn_payroll_id = $P{AMN_Payroll_ID}
				) as pyr_d  ON (pyr_d.amn_payroll_id= pyr.amn_payroll_id)
			 	LEFT JOIN adempiere.amn_concept_types_proc as ctp ON (ctp.amn_concept_types_proc_id= pyr_d.amn_concept_types_proc_id)
				LEFT JOIN adempiere.amn_concept_types 	as cty 	  ON (cty.amn_concept_types_id= ctp.amn_concept_types_id)
				WHERE pyr.ad_client_id=  $P{AD_Client_ID}  
					AND ( CASE WHEN ( $P{AMN_Payroll_ID} IS NULL OR pyr.amn_payroll_id= $P{AMN_Payroll_ID} ) THEN 1=1 ELSE 1=0 END )
			     	AND ( CASE WHEN ( $P{isShowZERO} = 'Y') OR ($P{isShowZERO} = 'N' 
			    	AND (  pyr_d.qtyvalue <> 0 OR pyr_d.amountallocated <> 0 OR pyr_d.amountdeducted<>0  OR pyr_d.amountcalculated<> 0)) THEN 1=1 ELSE 1=0 END )
				ORDER BY cty.calcorder
			    ) AS resum
			GROUP BY resum.amn_payroll_id --, resum.calcorder, resum.cty_value, resum.concept_type --, resum.salariobasico, resum.subtotal,  resum.horasextras, resum.comisadm, resum.ipspatron
		) resum2
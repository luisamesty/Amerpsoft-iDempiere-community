-- View: adempiere.amf_fact_accounts_banks_v
-- ADDITIONAL FIELD Reconc_payment_ID
-- ADDITINAL FIELD C_AcctSchema_ID
-- Previous Balance Calculation
DROP VIEW if exists adempiere.amf_fact_accounts_banks_v;

CREATE OR REPLACE VIEW adempiere.amf_fact_accounts_banks_v AS 
SELECT
fac.AD_Client_ID,
fac.AD_Org_ID,
CASE WHEN fac.AD_Table_ID = 335 THEN pay.C_BankAccount_ID
     WHEN fac.AD_Table_ID = 392 THEN cbs.C_BankAccount_ID
     ELSE 0 END as C_BankAccount_ID,
--COALESCE(pay.C_BankAccount_ID,cbs.C_BankAccount_ID, 0) as C_BankAccount_ID,
fac.account_id as account_id,
cev.value as account_value,
cev.name as account_name,
fac.amtacctdr,
fac.amtacctcr,
cur.iso_code,
fac.amtsourcedr,
fac.amtsourcecr,
CASE WHEN (fac.AmtSourceDr + fac.AmtSourceCr) = 0 THEN 0
     ELSE (fac.AmtAcctDr + fac.AmtAcctCr) / (fac.AmtSourceDr + fac.AmtSourceCr) END AS Rate,
bpa.c_bpartner_id as c_bpartner_id,
bpa.value as bpvalue,
bpa.name as bpname,
fac.dateacct,
fac.description,
-- C_Payment AD_Table_ID = 335
CASE WHEN fac.AD_Table_ID = 335 THEN fac.Record_ID 
     ELSE 0 END AS C_Payment_ID,
CASE WHEN fac.AD_Table_ID = 335 THEN CONCAT(pay.DocumentNo,'_',pay.datetrx,'_',pay.description) 
     ELSE '' END as pay_description,
-- C_Invoice AD_Table_ID = 318
CASE WHEN fac.AD_Table_ID = 318 THEN fac.Record_ID 
     ELSE 0 END AS C_Invoice_ID,
CASE WHEN fac.AD_Table_ID = 318 THEN CONCAT(inv.DocumentNo,'_',inv.dateinvoiced,'_',inv.description) 
     ELSE '' END as inv_description,
-- C_Charge from Payment AD_Table_ID = 335
CASE WHEN fac.AD_Table_ID = 335 THEN COALESCE(pay.C_Charge_ID , 0)
     ELSE 0 END AS C_Charge_ID,
CASE WHEN fac.AD_Table_ID = 335 THEN COALESCE(chg.Name , '')
     ELSE '' END as charge_description,
-- C_BankStatement AD_Table_ID = 392
CASE WHEN fac.AD_Table_ID = 392 THEN fac.Record_ID 
     ELSE 0 END AS C_BankStatement_ID,
CASE WHEN fac.AD_Table_ID = 392 THEN CONCAT(cbs.dateacct,'_',cbs.description) 
     ELSE '' END as bst_description,
-- C_Project
COALESCE(pro.C_Project_ID,0) as C_Project_ID,
COALESCE(pro.Name,'') as pro_name,
-- C_Campaign
COALESCE(cam.C_Campaign_ID,0) as C_Campaign_ID,
COALESCE(cam.Name,'') as cam_name,
-- C_SalesRegion
COALESCE(reg.C_SalesRegion_ID,0) as C_SalesRegion_ID,
COALESCE(reg.Name,'') as reg_name,
-- C_Activity
COALESCE(act.C_Activity_ID,0) as C_Activity_ID,
COALESCE(act.Name,'') as act_name,
-- 
fac.AD_Table_ID,
pay.C_BankAccount_ID as payC_BankAccount_ID,
cbs.C_BankAccount_ID as cbsC_BankAccount_ID,
pay.reconc_payment_id as Reconc_Payment_ID,
CASE 	WHEN pay.reconc_payment_id IS NOT NULL THEN
		pg_catalog.concat(to_char(pay3.payamt, '999999999999D99S'::text), '  ', baa3.name, ' ', to_char(pay3.dateacct, 'DD/MM/YYYY'::text), ' ', dct3.docbasetype, '-', pay3.documentno, '-', pay3.description) 
	ELSE ''	END AS Reconc_Description,
fac.C_AcctSchema_ID,
fac.Record_ID,
fac.Line_ID,
fac.C_currency_ID

FROM Fact_Acct fac
LEFT JOIN C_Bpartner bpa ON (bpa.C_BPartner_ID = fac.C_BPartner_ID)
LEFT JOIN C_ElementValue cev ON (cev.C_ElementValue_ID = fac.Account_ID)
LEFT JOIN C_Payment pay ON (pay.C_Payment_ID = fac.Record_ID)
LEFT JOIN C_Invoice inv ON (inv.C_Invoice_ID = fac.Record_ID)
LEFT JOIN C_Charge chg ON (chg.C_Charge_ID = pay.C_Charge_ID)
LEFT JOIN C_Project pro ON (pro.C_Project_ID = fac.C_Project_ID)
LEFT JOIN C_Campaign cam ON (cam.C_Campaign_ID = fac.C_Campaign_ID)
LEFT JOIN C_SalesRegion reg ON (reg.C_SalesRegion_ID = fac.C_SalesRegion_ID)
LEFT JOIN C_Activity act ON (act.C_Activity_ID = fac.C_Activity_ID)
LEFT JOIN C_Currency cur ON (cur.C_Currency_ID = fac.C_Currency_ID)
LEFT JOIN C_BankStatement cbs ON (cbs.C_BankStatement_ID = fac.Record_ID)
LEFT JOIN C_payment pay3 ON pay.reconc_payment_id  = pay3.c_payment_id
LEFT JOIN C_BankAccount baa3 ON (baa3.C_BankAccount_ID = pay3.C_BankAccount_ID)
LEFT JOIN c_doctype dct3 ON dct3.c_doctype_id = pay3.c_doctype_id
;
ALTER TABLE adempiere.amf_fact_accounts_banks_v
  OWNER TO adempiere;
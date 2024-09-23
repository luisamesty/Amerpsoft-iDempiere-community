 -- AD_Language Queries
-- $P{AD_Client_ID} 
SELECT AD_Language FROM AD_Client WHERE AD_Client_ID = $P{AD_Client_ID} 
-- C_invoice_ID
SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{C_Invoice_ID}  )
-- AMN_Employee
SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =( SELECT AD_Client_ID FROM C_invoice WHERE C_Invoice_ID=$P{AMN_Employee_ID}  )
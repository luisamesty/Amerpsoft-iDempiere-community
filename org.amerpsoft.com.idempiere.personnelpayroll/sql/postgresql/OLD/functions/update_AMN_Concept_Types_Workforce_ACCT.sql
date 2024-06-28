UPDATE AMN_Concept_types
SET 
amn_cre_dw_acct = amn_cre_acct,
amn_cre_iw_acct = amn_cre_acct,
amn_cre_sw_acct = amn_cre_acct,
amn_deb_dw_acct = amn_deb_acct,
amn_deb_iw_acct = amn_deb_acct,
amn_deb_sw_acct = amn_deb_acct
WHERE AD_Client_ID=1000001

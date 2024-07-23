-- UPDATE_Tables_for_reconversion.sql
-- AMN_Schema
-- UPDATE C_AcctSchema_ID on AMN_Schema to default value = 1000000
UPDATE AMN_Schema
SET C_AcctSchema_ID = 1000000;

-- AMN_Contract
-- UPDATE C_Currency_ID on AMN_Contract to Default Value = 1000000 (VES NEW ONE)
-- UPDATE C_ConversionType_ID on AMN_Contract to Default Value = 114
-- 
UPDATE AMN_Contract
SET C_ConversionType_ID = 114,
C_Currency_ID=205 ;

-- AMN_Payroll
-- UPDATE C_ConversionType_ID on AMN_Payroll to Default Value = 114
-- 
UPDATE AMN_Payroll
SET C_ConversionType_ID = 114 ;

-- AMN_Employee
-- UPDATE C_Currency_ID on AMN_Employee to Default Value = 205
-- 
UPDATE AMN_Employee
SET C_Currency_ID=205 ;

-- AMN_Employee_Salary
-- UPDATE C_ConversionType_ID on AMN_Employee_Salary to Default Value = 114
-- 
UPDATE AMN_Employee_Salary
SET C_ConversionType_ID = 114 ,
C_Currency_ID=205 ,
AD_Org_ID=1000000 WHERE AD_Client_ID=1000000 ;

-- AMN_Payroll_Historic
-- UPDATE C_Currency_ID on AMN_Contract to Default Value = 205
-- UPDATE C_ConversionType_ID on AMN_Payroll to Default Value = 114
-- 
UPDATE AMN_Payroll_Historic
SET C_ConversionType_ID = 114 ,
C_Currency_ID=205 ;

-- AMN_Concept_Types_Limit
-- UPDATE C_ConversionType_ID on AMN_Concept_Types_Limit to Default Value = 114
-- 
UPDATE AMN_Concept_Types_Limit
SET C_ConversionType_ID = 114 ;

-- AMN_Rates
-- UPDATE C_ConversionType_ID on AMN_Rates to Default Value = 114
-- 
UPDATE AMN_Rates
SET C_ConversionType_ID = 114 ;

-- AMN_Concept_Types_Acct
-- UPDATE Accounting Fields from old AMN_Aconcept_Types
-- 			For Current C_AcctSchema_ID=1000000
UPDATE amn_concept_types_acct as acta
   SET  
       amn_cre_acct=acty.amn_cre_acct, amn_cre_dw_acct=acty.amn_cre_dw_acct, amn_cre_iw_acct=acty.amn_cre_iw_acct, 
       amn_cre_mw_acct=acty.amn_cre_mw_acct, amn_cre_sw_acct=acty.amn_cre_sw_acct,
       amn_deb_acct=acty.amn_deb_acct, amn_deb_dw_acct=acty.amn_deb_dw_acct, amn_deb_iw_acct=acty.amn_deb_iw_acct, 
       amn_deb_mw_acct=acty.amn_deb_mw_acct, amn_deb_sw_acct=acty.amn_deb_sw_acct, 
       amn_concept_types_id = acty.amn_concept_types_id, c_acctschema_id=1000000
   FROM( SELECT amn_cre_acct, amn_cre_dw_acct, amn_cre_iw_acct, amn_cre_mw_acct, amn_cre_sw_acct, 
   				amn_deb_acct, amn_deb_dw_acct, amn_deb_iw_acct, amn_deb_mw_acct, amn_deb_sw_acct, 
   				amn_concept_types_id, 1000000
  		FROM amn_concept_types
   ) AS acty
 WHERE acta.amn_concept_types_id = acty.amn_concept_types_id
 AND acta.c_acctschema_id=1000000
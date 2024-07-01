-- ALTER TABLE AMN_Schema
--
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_social DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_provision_vacation DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socben_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socben_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socben_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socben_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socben_sw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_vacation_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_vacation_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_vacation_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_vacation_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_vacation_sw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_utilities_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_utilities_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_utilities_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_utilities_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_utilities_sw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_advances DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_bonus DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_other DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_salary DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_utilities DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_liability_vacation DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_provision_social DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_provision_utilities DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socsec_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socsec_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socsec_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socsec_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_socsec_sw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_salary_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_salary_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_salary_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_salary_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_salary_sw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_education_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_education_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_education_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_education_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_education_sw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_housesaving_aw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_housesaving_dw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_housesaving_iw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_housesaving_mw_expense DROP NOT NULL,
ALTER TABLE adempiere.amn_schema ALTER COLUMN amn_p_housesaving_sw_expense DROP NOT NULL
);

-- Application Dictionary
-- 
UPDATE AD_FIELD
SET ismandatory = 'N' WHERE AD_FIELD.AD_Tab_ID=1000013 AND seqno > 70

UPDATE AD_Column SET ismandatory = 'N' WHERE AD_Table_ID=1000004  AND Columnname like'%AMN_P%' ;


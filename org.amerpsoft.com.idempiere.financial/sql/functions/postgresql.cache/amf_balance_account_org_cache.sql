-- adempiere.amf_balance_account_cache definition
-- Drop table
-- DROP TABLE adempiere.amf_balance_account_org_cache;
CREATE TABLE adempiere.amf_balance_account_org_cache (
	bal_c_elementvalue_id numeric NULL,
	bal_c_acctschema_id numeric NULL,
	bal_ad_client_id numeric NULL,
	bal_ad_org_id numeric NULL,
	bal_c_period_id numeric NULL,
	account_code varchar NULL,
	account_name varchar NULL,
	dateini text NULL,
	dateend text NULL,
	openbalance numeric NULL,
	amtacctdr numeric NULL,
	amtacctcr numeric NULL,
	closebalance numeric NULL,
	amtacctsa numeric NULL,
	bal_postingtype bpchar(1) NOT NULL
);
-- Query: amf_balance_account_org_cache_refresh.sql
-- Fill amf_balance_account_org_cache with function
INSERT INTO amf_balance_account_org_cache (
    bal_c_elementvalue_id,
    bal_c_acctschema_id,
    account_code,
    account_name,
    bal_ad_org_id,
    dateini,
    dateend,
    openbalance,
    amtacctdr,
    amtacctcr,
    closebalance,
    amtacctsa,
    bal_ad_client_id,
    bal_c_period_id,
    bal_postingtype
)
SELECT
    bal_c_elementvalue_id,
    $P{C_AcctSchema_ID},
    account_code,
    account_name,
    ad_org_id,
    dateini,
    dateend,
    openbalance,
    amtacctdr,
    amtacctcr,
    closebalance,
    amtacctsa,
    $P{AD_Client_ID},
    $P{C_Period_ID},
    $P{PostingType}
FROM amf_balance_account_org(
    $P{AD_Client_ID},
    $P{AD_Org_ID},
    $P{C_AcctSchema_ID},
    $P{C_Period_ID},
    $P{PostingType},
    NULL
);
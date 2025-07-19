-- adempiere.amf_org_tree_cache definition
-- Drop table
-- DROP TABLE adempiere.amf_org_tree_cache;
CREATE TABLE adempiere.amf_org_tree_cache (
	org_ad_client_id numeric NOT NULL,
	org_ad_org_id numeric NOT NULL,
	org_ad_orgparent_id numeric NULL,
	issummary bpchar(1) NULL,
	org_value varchar NULL,
	org_description text NULL,
	org_name varchar NULL,
	all_orgs text NULL,
	org_taxid varchar NULL,
	org_logo bytea NULL,
	CONSTRAINT amf_org_tree_cache_pkey PRIMARY KEY (org_ad_client_id, org_ad_org_id)
);


-- Query: amf_org_tree_cache_refresh
-- Fill amf_org_tree_cache with Function amf_org_tree
INSERT INTO amf_org_tree_cache (
    org_ad_client_id,
    org_ad_org_id,
    org_ad_orgparent_id,
    issummary,
    org_value,
    org_description,
    org_name,
    all_orgs,
    org_taxid,
    org_logo
)
SELECT
    org_ad_client_id,
    org_ad_org_id,
    org_ad_orgparent_id,
    issummary,
    org_value,
    org_description,
    org_name,
    all_orgs,
    org_taxid,
    org_logo
FROM amf_org_tree(
    $P{AD_Client_ID},
    $P{AD_Org_ID},
    $P{AD_OrgParent_ID}
);


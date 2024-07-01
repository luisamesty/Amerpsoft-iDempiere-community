--CHANGES on COnCepts Using Tree

-- INSERT TREE
INSERT INTO adempiere.ad_tree
(ad_tree_id, ad_client_id, ad_org_id, created, createdby, updated, updatedby, isactive, "name", description, treetype, isallnodes, processing, isdefault, ad_tree_uu, istreedrivenbyvalue, ad_table_id, isloadallnodesimmediately, isvaluedisplayed, parent_column_id)
VALUES(1000008, 1000000, 0, '2024-08-15 09:53:58.450', 100, '2024-08-15 10:01:47.429', 100, 'Y', 'AMN_Concept_Types', NULL, 'TL', 'Y', 'N', 'N', '240ae112-4f4d-4567-b577-b688f03213fe', 'N', 1000016, 'Y', 'Y', NULL);

-- ALTER TABLE amn_concept
ALTER TABLE adempiere.amn_concept DROP COLUMN script;
ALTER TABLE adempiere.amn_concept DROP COLUMN  amn_concept_types_id ;
ALTER TABLE adempiere.amn_concept DROP COLUMN  seqno;
ALTER TABLE adempiere.amn_concept DROP COLUMN  formula;
ALTER TABLE adempiere.amn_concept DROP COLUMN  amn_process_id;
ALTER TABLE adempiere.amn_concept DROP COLUMN  amn_process_value;
ALTER TABLE adempiere.amn_concept DROP COLUMN  defaultvalue;
ALTER TABLE adempiere.amn_concept DROP COLUMN  amn_concept_types_proc_id;
ALTER TABLE adempiere.amn_concept ADD help varchar(2000) DEFAULT NULL::character varying NULL;
ALTER TABLE adempiere.amn_concept ADD ad_tree_id numeric(10) DEFAULT NULL::numeric NULL;

-- INSERT CONCEPT MASTER
INSERT INTO adempiere.amn_concept
(amn_concept_id, "name", ad_client_id, ad_org_id, created, createdby, updated, updatedby, value, description, isactive, amn_concept_uu, help, ad_tree_id)
VALUES(1000000, 'Conceptos Monalisa', 1000000, 0, '2024-08-15 13:11:01.577', 100, '2024-08-15 13:21:52.033', 100, '1000000', NULL, 'Y', '18f938d8-dc5f-4fb3-adf4-d55e0e1c2822', NULL, 1000008);

--
ALTER TABLE adempiere.amn_concept_types ADD issummary bpchar(1) DEFAULT 'N'::bpchar NOT NULL;
ALTER TABLE adempiere.amn_concept_types ADD amn_concept_id numeric(10) DEFAULT 1000000::numeric NOT NULL;

-- UPDATE CONCEPT TYPES
UPDATE adempiere.amn_concept_types SET AMN_Concept_ID=1000000 WHERE AD_Client_ID =1000000 ;
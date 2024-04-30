DELETE FROM adempiere.c_taxcategory ct WHERE ct.ad_client_id=1000000 AND ct.C_taxcategory_id <> 1000000;

COMMIT;
INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
VALUES(1001202, 1000000, 0, 'Y', '2019-06-18 00:00:00.000', 100, '2023-06-16 00:00:00.000', 100, 'Exentas 1.0', 'Exentas 1.0', '', 'N', '1fb09d5c-2b30-18ab-8a0b-4e6cc161757b', 'N', '');
INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
VALUES(1000102, 1000000, 0, 'Y', '2017-04-03 00:00:00.000', 100, '2020-12-02 00:00:00.000', 100, 'Exentas 1.5', 'Paraguay Tax Excempt', '', 'Y', 'e1db4673-d70a-64b3-1ec5-af37ce031297', 'N', '');
INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
VALUES(1000702, 1000000, 0, 'Y', '2018-06-09 00:00:00.000', 100, '2020-12-02 00:00:00.000', 100, 'Exentas Compras', '', '', 'N', 'f7486f3d-456d-bc41-06ab-65aeec605e34', 'N', '');
INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
VALUES(1000201, 1000000, 0, 'Y', '2017-08-16 00:00:00.000', 100, '2017-08-16 00:00:00.000', 100, 'IRP', 'IRP', 'IRP', 'N', '7e63fb87-641e-b529-ed1e-99d5f6a15b64', 'N', '');
INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
VALUES(1000001, 1000000, 0, 'Y', '2016-11-28 00:00:00.000', 100, '2017-04-03 00:00:00.000', 100, 'IVA 10%', 'IVA 10%', '', 'N', 'e5923c52-7399-7c75-ec9a-f836fe3742d8', 'N', '');
INSERT INTO adempiere.c_taxcategory
(c_taxcategory_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, "name", description, commoditycode, isdefault, c_taxcategory_uu, iswithholding, withholdingformat)
VALUES(1000101, 1000000, 0, 'Y', '2017-04-03 00:00:00.000', 100, '2017-04-03 00:00:00.000', 100, 'IVA 5%', 'IVA 5%', '', 'N', '79ca28e1-ce35-a4d6-c15a-ae4d6142af9d', 'N', '');

* THIS FILE IS NOT COMPLETED
* FOR FUTURE FEATURE
INSERT INTO ad_sequence (ad_sequence_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,vformat,isautosequence,incrementno,startno,currentnext,currentnextsys,isaudited,istableid,prefix,suffix,startnewyear,datecolumn,decimalpattern,ad_sequence_uu,startnewmonth,isorglevelsequence,orgcolumn)
SELECT 1000361,1000000,0,'Y','2016-04-22 15:35:04.312',100,'2019-11-18 09:58:14.737',100,'AP Invoice VAT Withholding Number Multi-Invoice','@DateOrdered<YYYY>@ ','','Y',1,1,1,100,'N','N','@DateDoc<YYYYMM>@','','N','DateAcct','00000000','239c1f1b-d0f0-4e08-a76a-3f2e7e829d6a','N','N','AD_Org_ID'
WHERE NOT EXISTS (SELECT 1 FROM ad_sequence WHERE  ad_sequence_ID=1000361);


INSERT INTO ad_sequence (ad_sequence_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,vformat,isautosequence,incrementno,startno,currentnext,currentnextsys,isaudited,istableid,prefix,suffix,startnewyear,datecolumn,decimalpattern,ad_sequence_uu,startnewmonth,isorglevelsequence,orgcolumn)
SELECT 1000365,1000000,0,'Y','2016-05-16 13:48:42.357',100,'2016-05-16 13:48:42.357',100,'AR Withholding VAT','AR Withholding VAT','','Y',1,150000,1,15000,'N','N','','','N','','','53170d8f-4e58-46fa-9ab3-f8b154b264fc','N','N',''
WHERE NOT EXISTS (SELECT 1 FROM ad_sequence WHERE  ad_sequence_ID=1000365);


INSERT INTO ad_sequence (ad_sequence_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,vformat,isautosequence,incrementno,startno,currentnext,currentnextsys,isaudited,istableid,prefix,suffix,startnewyear,datecolumn,decimalpattern,ad_sequence_uu,startnewmonth,isorglevelsequence,orgcolumn)
SELECT 1000368,1000000,0,'Y','2016-06-01 07:39:50.187',100,'2016-06-01 07:39:50.187',100,'AR Withholding ISLR','AR Withholding ISLR','','Y',1,150000,1,150000,'N','N','','','N','','','0d31637d-068c-4733-afbe-d1aa5ac98aac','N','N',''
WHERE NOT EXISTS (SELECT 1 FROM ad_sequence WHERE  ad_sequence_ID=1000368);

INSERT INTO ad_sequence (ad_sequence_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,vformat,isautosequence,incrementno,startno,currentnext,currentnextsys,isaudited,istableid,prefix,suffix,startnewyear,datecolumn,decimalpattern,ad_sequence_uu,startnewmonth,isorglevelsequence,orgcolumn)
SELECT 1000399,1000000,0,'Y','2019-07-15 14:28:39.579',100,'2019-11-26 15:18:47.565',100,'AP Invoice - Municipal Withholding Number Multi-Invoice','@DateOrdered<YYYY>@ ','','Y',1,1,1,100,'N','N','@DateDoc<YYYYMM>@','','N','DateAcct',0,'f13bcbb8-3a8d-47d6-aa05-830d48285756','N','N','AD_Org_ID'
WHERE NOT EXISTS (SELECT 1 FROM ad_sequence WHERE  ad_sequence_ID=1000399);

INSERT INTO ad_sequence (ad_sequence_id,ad_client_id,ad_org_id,isactive,created,createdby,updated,updatedby,name,description,vformat,isautosequence,incrementno,startno,currentnext,currentnextsys,isaudited,istableid,prefix,suffix,startnewyear,datecolumn,decimalpattern,ad_sequence_uu,startnewmonth,isorglevelsequence,orgcolumn)
SELECT 1000400,1000000,0,'Y','2019-07-17 08:14:51.403',100,'2019-07-17 08:14:51.403',100,'AR Withholding MUNICIPAL','AR Withholding MUNICIPAL','','Y',1,150000,500000,15000,'N','N','','','N','','','822e6e4f-6b8b-4dcc-b79c-d66eecdab5e6','N','N',''
WHERE NOT EXISTS (SELECT 1 FROM ad_sequence WHERE  ad_sequence_ID=1000400);

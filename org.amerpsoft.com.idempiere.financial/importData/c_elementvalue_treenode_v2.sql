-- CREATE SEQUENCE
DROP SEQUENCE IF EXISTS NODEVALUE ;
CREATE SEQUENCE NODEVALUE INCREMENT BY 1 MINVALUE 1000003 ;

-- CREATE TEMPORARY TABLE 
DROP TABLE IF EXISTS adempiere.c_elementvalueparent ; 
CREATE TABLE adempiere.c_elementvalueparent 
(   VALUE character varying(40) NOT NULL,   
parent_value character varying(40) , 
 node_id numeric(10,0) NOT NULL,  
 c_elementvalue_id numeric(10,0) NOT NULL,  
 c_elementparent_id numeric(10,0) ) ; 

-- FILL TEMPORARY TABLE 
INSERT INTO adempiere.c_elementvalueparent 
( SELECT  value,value_parent,0,c_elementvalue_id,0 
FROM adempiere.c_elementvalue 
WHERE ad_client_id=1000003 ORDER BY Value ASC ) ;

commit ;

-- UPDATE TEMPORARY TABLE c_elementvalueparent_ID  NEXTVAL('NODEVALUE')
UPDATE adempiere.c_elementvalueparent 
SET c_elementparent_id = ( select c_elementvalue.c_elementvalue_id  FROM adempiere.c_elementvalue 
WHERE ad_client_id=1000003 
and c_elementvalueparent.parent_value = c_elementvalue.value ) ,
node_id= ( select c_elementvalue.c_elementvalue_id  FROM adempiere.c_elementvalue 
WHERE ad_client_id=1000003 
and c_elementvalueparent.value = c_elementvalue.value ) ;  

commit ;

-- UPDATE c_elementvalue TABLE WITH c_elementparent_ID 
--UPDATE adempiere.c_elementvalue 
--SET c_elementparent_id = ( select c_elementvalueparent.c_elementparent_id 
-- FROM adempiere.c_elementvalueparent 
-- WHERE c_elementvalueparent.value = c_elementvalue.value ) 
-- WHERE adempiere.c_elementvalue.ad_client_id=1000003 ;

--commit;

-- UPDATE ad_treenode TABLE WITH c_elementparent_ID 
UPDATE adempiere.ad_treenode 
SET 
  parent_id = ( select c_elementvalueparent.c_elementparent_id  
  FROM adempiere.c_elementvalueparent 
  WHERE  c_elementvalueparent.node_id = ad_treenode.node_id 
 ) ,seqno = 0
 WHERE ad_treenode.ad_client_id=1000003 and  ad_treenode.ad_tree_id= 1000030 ;

commit ;

-- DROP SEQUENCE NEXTVAL('NODEVALUE')
DROP SEQUENCE IF EXISTS NODEVALUE ;
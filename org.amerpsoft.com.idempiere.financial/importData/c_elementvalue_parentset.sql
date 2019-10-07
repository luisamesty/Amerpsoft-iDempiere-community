UPDATE C_ElementValue
SET value_parent = ''
WHERE AD_Client_ID=1000004 and LENGTH(Value) = 1 ;

UPDATE C_ElementValue
SET value_parent = SUBSTRING(value,1,1) 
WHERE AD_Client_ID=1000004 and LENGTH(Value) = 2 ;

UPDATE C_ElementValue
SET value_parent = SUBSTRING(value,1,3) 
WHERE AD_Client_ID=1000004 and LENGTH(Value) = 6 ;






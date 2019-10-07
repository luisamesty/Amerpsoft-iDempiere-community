SELECT 
	CLI.value as clivalue,
	CLI.name as cliname,
	CLI.description as clidescription,
	CLF.c_acctschema1_id,
	ELE.c_element_id, 
	ELV.c_elementvalue_id, 
	ELV.ad_client_id, 
	ELV.ad_org_id, 
	ELV.isactive, 
	ELV."value", 
	COALESCE(ELV.name,'') as name, 
	COALESCE(ELV.description,ELV.name,'') as description, 
	ELV.accounttype, 
	ELV.accountsign, 
	ELV.isdoccontrolled, 
	ELV.c_element_id, 
	ELV.issummary, 
	COALESCE(ELV.c_elementparent_id,ELE.c_element_id) as c_elementparent_id,
	COALESCE(ELV.value_parent,'') as value_parent ,
	CAST(IMG.binarydata as blob) as logoreport,
	IMG.binarydata
FROM 
	adempiere.c_element as ELE
LEFT JOIN 
    adempiere.ad_client AS CLI ON (CLI.ad_client_id = ELE.ad_client_id)
LEFT JOIN 
    adempiere.ad_clientinfo AS CLF ON (CLI.ad_client_id = CLF.ad_client_id)
LEFT JOIN 
    adempiere.ad_image AS IMG ON (CLF.logoreport_id = IMG.ad_image_id)
LEFT JOIN
	adempiere.c_elementvalue as ELV ON (CLF.c_acctschema1_id =ELV.c_element_id)
WHERE
	ELV.isactive='Y' and ELE.ad_client_id=1000001 
ORDER BY ELV."value"

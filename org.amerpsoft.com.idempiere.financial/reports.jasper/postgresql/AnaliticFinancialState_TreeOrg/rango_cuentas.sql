SELECT ce.ad_client_id, ce1.value AS codigo_from, ce2.value AS codigo_to
FROM C_ElementValue ce
LEFT JOIN (
	SELECT ad_client_id, value FROM C_ElementValue 
	WHERE ad_client_id = $P{AD_Client_ID} AND issummary='N' AND ( $P{C_ElementValue1_ID} IS NULL OR C_ElementValue_ID = $P{C_ElementValue1_ID} )
	ORDER BY value ASC 
	FETCH FIRST 1 ROWS ONLY
) ce1 ON (ce.ad_client_id = ce1.ad_client_id )
LEFT JOIN (
	SELECT ad_client_id, value FROM C_ElementValue 
	WHERE ad_client_id = $P{AD_Client_ID} AND issummary='N' AND ( $P{C_ElementValue2_ID} IS NULL OR C_ElementValue_ID = $P{C_ElementValue2_ID} )
	ORDER BY value DESC 
	FETCH FIRST 1 ROWS ONLY
) ce2 ON (ce.ad_client_id = ce2.ad_client_id )
WHERE ce.ad_client_id = $P{AD_Client_ID}
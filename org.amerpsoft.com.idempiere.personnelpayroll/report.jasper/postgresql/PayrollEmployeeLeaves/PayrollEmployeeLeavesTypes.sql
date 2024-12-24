SELECT STRING_AGG(lt_reference, ', ') AS leaves_all
FROM (
	SELECT CONCAT( amlt.Value,':',amltt.name) AS lt_reference, amlt.Value AS lt_value, amlt.Name AS lt_name, amltt.name AS ltt_name, amltt.description AS ltt_description
	FROM AMN_leaves_Types amlt
	INNER JOIN AMN_leaves_Types_Trl amltt ON amltt.amn_leaves_types_id = amlt.amn_leaves_types_id 
	WHERE amlt.ad_client_id = $P{AD_Client_ID} AND amltt.ad_language = (SELECT AD_Language FROM AD_Client WHERE AD_Client_ID =$P{AD_Client_ID} )
) AS leatyp
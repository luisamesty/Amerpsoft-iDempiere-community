-- Function amf_balance_account_org_flex
-- Devuelve los balances de cuentas en un periodo o rango de fechas dado de una cuenta y una Organizacion
-- Si no tiene movimiento devuelve un registro con cero
-- Si p_ad_orgparent_id es NULL o 0, se comporta igual que amf_balance_account_org_flex(...).
-- Si p_ad_orgparent_id tiene valor, entonces se ignora p_ad_org_id, se invoca amf_org_tree(...) y se obtiene una lista de organizaciones hijas.
-- Se llama internamente a amf_balance_account_org_flex(...) por cada ad_org_id retornado por amf_org_tree(...) y se combinan los resultados (tipo UNION ALL).

DROP FUNCTION adempiere.amf_balance_account_org_flex_orgparent(numeric, numeric, numeric, numeric, numeric, character varying, numeric, timestamp, timestamp);

CREATE OR REPLACE FUNCTION adempiere.amf_balance_account_org_flex_orgparent(
p_ad_client_id numeric, 
p_ad_orgparent_id numeric, 
p_ad_org_id numeric, 
p_c_acctschema_id numeric, 
p_c_period_id numeric,
p_postingtype character varying,
p_account_id numeric, 
p_datefrom timestamp without time zone,
p_dateto timestamp without time zone)
 RETURNS TABLE(
    bal_c_elementvalue_id numeric,
    account_code character varying,
    account_name character varying,
    ad_org_id numeric,
    dateini text,
    dateend text,
    openbalance numeric,
    amtacctdr numeric,
    amtacctcr numeric,
    closebalance numeric,
    amtacctsa numeric
)
 LANGUAGE plpgsql
AS $function$
DECLARE
    rec_org RECORD;
BEGIN
	IF p_ad_orgparent_id IS NULL OR p_ad_orgparent_id = 0 THEN
	    RETURN QUERY
	    SELECT *
	    FROM adempiere.amf_balance_account_org_flex(
            p_ad_client_id,
            p_ad_org_id,
            p_c_acctschema_id,
	    p_c_period_id, 
	    p_postingtype,
            p_account_id,
            p_datefrom,
            p_dateto
	    );
	ELSE
	    IF p_ad_org_id IS NULL OR p_ad_org_id = 0 THEN
	        -- Devuelve todas las organizaciones hijas de p_ad_orgparent_id
	        FOR rec_org IN
	            SELECT org_ad_org_id
	            FROM adempiere.amf_org_tree(p_ad_client_id, 0, p_ad_orgparent_id)
	        LOOP
	            RETURN QUERY
	            SELECT *
	            FROM adempiere.amf_balance_account_org_flex(
			    p_ad_client_id,
			    rec_org.org_ad_org_id,
			    p_c_acctschema_id,
			    p_c_period_id, 
			    p_postingtype,
			    p_account_id,
			    p_datefrom,
			    p_dateto	                
	            );
	        END LOOP;
	    ELSE
	        -- Devuelve solo p_ad_org_id si es hija (está en el árbol)
	        PERFORM 1
	        FROM adempiere.amf_org_tree(p_ad_client_id, 0, p_ad_orgparent_id)
	        WHERE org_ad_org_id = p_ad_org_id
	        LIMIT 1;
	        
	        IF FOUND THEN
	            RETURN QUERY
	            SELECT *
	            FROM adempiere.amf_balance_account_org_flex(
			    p_ad_client_id,
			    p_ad_org_id,
			    p_c_acctschema_id,
			    p_c_period_id, 
			    p_postingtype,
			    p_account_id,
			    p_datefrom,
			    p_dateto
	            );
	        ELSE
	            -- No es hija, devuelve vacío (no hace RETURN QUERY)
	            RETURN;
	        END IF;
	    END IF;
	END IF;
END;
$function$
;

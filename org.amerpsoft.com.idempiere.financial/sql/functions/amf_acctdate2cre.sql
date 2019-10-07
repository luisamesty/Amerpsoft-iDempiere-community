-- Function: adempiere.amf_acctdate2cre(numeric, numeric, numeric, timestamp without time zone, timestamp without time zone)

-- DROP FUNCTION adempiere.amf_acctdate2cre(numeric, numeric, numeric, timestamp without time zone, timestamp without time zone);

CREATE OR REPLACE FUNCTION adempiere.amf_acctdate2cre(p_client_id numeric, p_org_id numeric, p_account_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone)
  RETURNS numeric AS
$BODY$
DECLARE
	v_acctcreperiod	numeric;

BEGIN
		v_acctcreperiod := 0;
	    IF (p_org_id > 0) THEN
			select 
				coalesce(sum(fas.amtacctcr),0.00) INTO v_acctcreperiod
			FROM
				adempiere.fact_acct as fas
			WHERE
				fas.ad_client_id=p_client_id and fas.ad_org_id=p_org_id and fas.account_id = p_account_id 	
				and (fas.dateacct >=  p_startdate and fas.dateacct <= p_enddate )
			;
		ELSE
			select 
				coalesce(sum(fas.amtacctcr),0.00) INTO v_acctcreperiod
			FROM
				adempiere.fact_acct as fas
			WHERE
				fas.ad_client_id=p_client_id  and fas.account_id = p_account_id 
				and (fas.dateacct >=  p_startdate and fas.dateacct <= p_enddate )
			;
	    END IF;
	   
    	RETURN  v_acctcreperiod;
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_acctdate2cre(numeric, numeric, numeric, timestamp without time zone, timestamp without time zone)
  OWNER TO adempiere;

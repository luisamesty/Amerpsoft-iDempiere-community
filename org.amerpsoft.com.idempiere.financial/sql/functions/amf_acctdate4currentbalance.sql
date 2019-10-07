-- Function: adempiere.amf_acctdate4currentbalance(numeric, numeric, numeric, timestamp without time zone, timestamp without time zone)

-- DROP FUNCTION adempiere.amf_acctdate4currentbalance(numeric, numeric, numeric, timestamp without time zone, timestamp without time zone);

CREATE OR REPLACE FUNCTION adempiere.amf_acctdate4currentbalance(p_client_id numeric, p_org_id numeric, p_account_id numeric, p_startdate timestamp without time zone, p_enddate timestamp without time zone)
  RETURNS numeric AS
$BODY$
DECLARE
	v_acctbalperiod	numeric :=0;
	v_acctdebperiod	numeric :=0;
	v_acctcreperiod	numeric :=0;
BEGIN
		v_acctbalperiod := 0;
	    IF (p_org_id > 0) THEN
			select 
				coalesce(sum(fas.amtacctdr),0.00),coalesce(sum(fas.amtacctcr),0.00)  INTO v_acctdebperiod,v_acctcreperiod
			FROM
				adempiere.fact_acct as fas
			WHERE
				fas.ad_client_id=p_client_id and fas.ad_org_id=p_org_id and fas.account_id = p_account_id 	
				and fas.dateacct <= p_enddate
			;
		ELSE
			select 
				coalesce(sum(fas.amtacctdr),0.00),coalesce(sum(fas.amtacctcr),0.00)  INTO v_acctdebperiod,v_acctcreperiod
			FROM
				adempiere.fact_acct as fas
			WHERE
				fas.ad_client_id=p_client_id  and fas.account_id = p_account_id 
				and fas.dateacct <= p_enddate
			;
	    END IF;
	   v_acctbalperiod:=v_acctdebperiod-v_acctcreperiod;
    	RETURN  v_acctbalperiod;
END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_acctdate4currentbalance(numeric, numeric, numeric, timestamp without time zone, timestamp without time zone)
  OWNER TO adempiere;

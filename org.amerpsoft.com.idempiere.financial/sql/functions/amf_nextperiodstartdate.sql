-- Function: adempiere.amf_nextperiodstartdate(numeric)

-- DROP FUNCTION adempiere.amf_nextperiodstartdate(numeric);

CREATE OR REPLACE FUNCTION adempiere.amf_nextperiodstartdate(p_period_id numeric)
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_year_id		numeric :=0;
	v_prevstartDate		timestamp without time zone := null;
BEGIN

SELECT (startdate )  into v_prevstartDate
FROM C_Period
where C_Period_ID = p_period_id
;
v_prevstartDate := v_prevstartDate + interval '1 months';
RETURN	v_prevstartDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_nextperiodstartdate(numeric)
  OWNER TO adempiere;

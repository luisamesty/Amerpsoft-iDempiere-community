-- Function: adempiere.amf_periodstartdate(numeric)

-- DROP FUNCTION adempiere.amf_periodstartdate(numeric);

CREATE OR REPLACE FUNCTION adempiere.amf_periodstartdate(p_period_id numeric)
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_year_id		numeric :=0;
	v_startDate		timestamp without time zone := null;
BEGIN

SELECT startdate into v_startDate
FROM C_Period
where C_Period_ID = p_period_id
;

RETURN	v_startDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_periodstartdate(numeric)
  OWNER TO adempiere;

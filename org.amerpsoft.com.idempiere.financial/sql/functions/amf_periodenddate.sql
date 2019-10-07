-- Function: adempiere.amf_periodenddate(numeric)

-- DROP FUNCTION adempiere.amf_periodenddate(numeric);

CREATE OR REPLACE FUNCTION adempiere.amf_periodenddate(p_period_id numeric)
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_year_id		numeric :=0;
	v_endDate		timestamp without time zone := null;
BEGIN

SELECT enddate into v_endDate
FROM C_Period
where C_Period_ID = p_period_id
;

RETURN	v_endDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_periodenddate(numeric)
  OWNER TO adempiere;

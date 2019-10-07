-- Function: adempiere.amf_prevperiodenddate(numeric)

-- DROP FUNCTION adempiere.amf_prevperiodenddate(numeric);

CREATE OR REPLACE FUNCTION adempiere.amf_prevperiodenddate(p_period_id numeric)
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_year_id		numeric :=0;
	v_endDate		timestamp without time zone := null;
BEGIN

SELECT (startdate - 1)  into v_endDate
FROM C_Period
where C_Period_ID = p_period_id
;

RETURN	v_endDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_prevperiodenddate(numeric)
  OWNER TO adempiere;

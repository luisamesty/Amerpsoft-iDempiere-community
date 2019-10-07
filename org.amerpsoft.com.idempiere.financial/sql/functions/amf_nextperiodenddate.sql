-- Function: adempiere.amf_nextperiodenddate(numeric)

-- DROP FUNCTION adempiere.amf_nextperiodenddate(numeric);

CREATE OR REPLACE FUNCTION adempiere.amf_nextperiodenddate(p_period_id numeric)
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_year_id		numeric :=0;
	v_nextendDate		timestamp without time zone := null;
BEGIN

SELECT enddate into v_nextendDate
FROM C_Period
where C_Period_ID = p_period_id
;

v_nextendDate := v_nextendDate + interval '1 months';
RETURN	v_nextendDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_nextperiodenddate(numeric)
  OWNER TO adempiere;

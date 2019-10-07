-- Function: adempiere.amf_fiscalstartdate(numeric, timestamp without time zone)

-- DROP FUNCTION adempiere.amf_fiscalstartdate(numeric, timestamp without time zone);

CREATE OR REPLACE FUNCTION adempiere.amf_fiscalstartdate(p_calendar_id numeric, p_acctdate timestamp without time zone)
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_year_id		numeric :=0;
	v_startDate		timestamp without time zone := null;
BEGIN

SELECT min(startdate) into v_startDate
FROM C_Period
where C_Year_ID in
(
SELECT p.c_year_id
FROM C_Period p
JOIN C_Year y on (p.c_year_id = y.c_year_id)
WHERE y.c_calendar_id = p_calendar_id
 AND p_acctdate BETWEEN TRUNC(p.StartDate) AND TRUNC(p.EndDate)
 AND p.IsActive='Y' 
 AND p.PeriodType='S'
)
;

RETURN	v_startDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION adempiere.amf_fiscalstartdate(numeric, timestamp without time zone)
  OWNER TO adempiere;

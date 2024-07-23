-- Function: adempiere.amf_reportcurrentdate()

-- DROP FUNCTION adempiere.amf_reportcurrentdate();

CREATE OR REPLACE FUNCTION adempiere.amf_reportcurrentdate()
  RETURNS timestamp without time zone AS
$BODY$DECLARE
	v_currentDate		timestamp without time zone := null;
BEGIN

SELECT 
cast(current_date as timestamp) into v_currentDate ;

RETURN	v_currentDate;
END;	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
ALTER FUNCTION adempiere.amf_reportcurrentdate()
  OWNER TO postgres;

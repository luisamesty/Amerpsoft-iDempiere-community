-- Function: businessdays
-- PArameters: start_date, end_date, p_AD_Client_ID, p_C_Country_ID
--Notas:
-- La función valida que la fecha de inicio sea anterior o igual a la fecha final.
-- Excluye sabados y domingos 
-- La tabla C_NonBusinessDay debe contener los días no laborables específicos para el pais solicitado.
-- Forma de llamar:
-- SELECT adempiere.businessdays('2024-12-01', '2024-12-31',1000000, 276) AS days
--
CREATE OR REPLACE FUNCTION businessdays(start_date timestamp without time zone, end_date timestamp without time zone, p_AD_Client_ID numeric, p_C_Country_ID numeric)
RETURNS INTEGER AS $$
DECLARE
    business_days_count INTEGER;
BEGIN
    -- Validar que la fecha inicial sea menor o igual a la fecha final
    IF start_date > end_date THEN
        RAISE EXCEPTION 'La fecha de inicio debe ser menor o igual a la fecha final.';
    END IF;

    -- Contar los días hábiles excluyendo fines de semana y días no laborales
    SELECT COUNT(*) INTO business_days_count
    FROM generate_series(start_date, end_date, INTERVAL '1 day') AS gs(day)
    WHERE EXTRACT(ISODOW FROM gs.day) NOT IN (6, 7) -- Excluir sábados (6) y domingos (7)
      AND gs.day NOT IN (
			SELECT date1 
					FROM C_NonBusinessDay 
					WHERE AD_Client_ID=p_AD_Client_ID and IsActive ='Y' 
						AND Date1 >= start_date
						AND Date1 <= end_date
					    AND COALESCE(C_Country_ID,0) IN (0, p_C_Country_ID)
					ORDER BY Date1
		);

    RETURN business_days_count;
END;
$$ LANGUAGE plpgsql;

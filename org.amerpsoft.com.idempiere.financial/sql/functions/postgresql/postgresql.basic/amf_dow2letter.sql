-- FUNCTION adempiere.amf_dow2letter(bpchar, bpchar, bpchar);
CREATE OR REPLACE FUNCTION adempiere.amf_dow2letter(conv_dow character, upp_low character, languaje_iso character)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$

DECLARE
    letter_dow varchar(40);
    nullvalue varchar(20);
    notnullvalue varchar(30);
    
BEGIN
    nullvalue := '*** NULL ***';
    notnullvalue := '** NOT NULL *** ';
    languaje_iso := lower(languaje_iso);
    IF (conv_dow IS NULL) 
    THEN 
	letter_dow := nullvalue ;
    ELSEIF (conv_dow IS NOT NULL) 
    THEN
	-- SPANISH
	 case when languaje_iso = 'es' then 
		case when (conv_dow) ='0' then letter_dow := 'Domingo';
		     when (conv_dow) ='1' then letter_dow := 'Lunes'; 
		     when (conv_dow) ='2' then letter_dow := 'Martes'; 
		     when (conv_dow) ='3' then letter_dow := 'Miercoles';
		     when (conv_dow) ='4' then letter_dow := 'Jueves';
		     when (conv_dow) ='5' then letter_dow := 'Viernes'; 
		     when (conv_dow) ='6' then letter_dow := 'Sabado';
		     else letter_dow := ('Dia Invalido') ;
		end  case;
	-- ENGLISH
	      when languaje_iso = 'en' then 
		case when (conv_dow) ='0' then letter_dow := 'Sunday';
		     when (conv_dow) ='1' then letter_dow := 'Monday'; 
		     when (conv_dow) ='2' then letter_dow := 'Tuesday'; 
		     when (conv_dow) ='3' then letter_dow := 'Wednesday';
		     when (conv_dow) ='4' then letter_dow := 'Thursday';
		     when (conv_dow) ='5' then letter_dow := 'Friday'; 
		     when (conv_dow) ='6' then letter_dow := 'Saturday';
		     else letter_dow := ('Invalid Dow') ;
		end  case;		     
	-- DEFAULT SPANISH
	      else 
		case when (conv_dow) ='0' then letter_dow := 'Domingo';
		     when (conv_dow) ='1' then letter_dow := 'Lunes'; 
		     when (conv_dow) ='2' then letter_dow := 'Martes'; 
		     when (conv_dow) ='3' then letter_dow := 'Miercoles';
		     when (conv_dow) ='4' then letter_dow := 'Jueves';
		     when (conv_dow) ='5' then letter_dow := 'Viernes'; 
		     when (conv_dow) ='6' then letter_dow := 'Sabado';
		     else letter_dow := ('Dia Invalido') ;
		end  case;
	      
	 end case;

    END IF;
    if upper(upp_low) = 'U' then
	    letter_dow :=  upper(letter_dow);
    end if;
    if upper(upp_low) = 'L' then
	    letter_dow :=  lower(letter_dow);
    end if;
    RETURN (letter_dow);
END;
$function$
;
-- ALTER
ALTER FUNCTION adempiere.amf_dow2letter(character, character, character)
  OWNER TO adempiere;

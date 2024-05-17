CREATE OR REPLACE FUNCTION adempiere.amf_dow2letter(conv_dow varchar, upp_low varchar, languaje_iso varchar)
    RETURN VARCHAR 
IS
    letter_dow  VARCHAR(40);
    nullvalue VARCHAR(40);

BEGIN
    SELECT '*** NULL ***' INTO nullvalue FROM DUAL;
    IF (conv_dow IS NULL) THEN 
    	letter_dow := nullvalue;
    ELSE 
        -- SPANISH
        IF languaje_iso = 'es' THEN 
                IF (conv_dow) ='0' THEN letter_dow := 'Domingo';
                ELSIF (conv_dow) ='1' then letter_dow := 'Lunes'; 
                ELSIF (conv_dow) ='2' then letter_dow := 'Martes'; 
                ELSIF (conv_dow) ='3' then letter_dow := 'Miercoles';
                ELSIF (conv_dow) ='4' then letter_dow := 'Jueves';
                ELSIF (conv_dow) ='5' then letter_dow := 'Viernes'; 
                ELSIF (conv_dow) ='6' then letter_dow := 'Sabado';
                ELSE letter_dow := ('Dia Invalido') ;
                END IF;
        -- ENGLISH
        ELSIF languaje_iso = 'en' THEN 
                IF (conv_dow) ='0' then letter_dow := 'Sunday';
                ELSIF (conv_dow) ='1' then letter_dow := 'Monday'; 
                ELSIF (conv_dow) ='2' then letter_dow := 'Tuesday'; 
                ELSIF (conv_dow) ='3' then letter_dow := 'Wednesday';
                ELSIF (conv_dow) ='4' then letter_dow := 'Thursday';
                ELSIF (conv_dow) ='5' then letter_dow := 'Friday'; 
                ELSIF (conv_dow) ='6' then letter_dow := 'Saturday';
                ELSE letter_dow := ('Invalid Dow') ;
                END IF;		     
        -- DEFAULT SPANISH
        ELSE 
                IF (conv_dow) ='0' then letter_dow := 'Domingo';
                ELSIF (conv_dow) ='1' then letter_dow := 'Lunes'; 
                ELSIF (conv_dow) ='2' then letter_dow := 'Martes'; 
                ELSIF (conv_dow) ='3' then letter_dow := 'Miercoles';
                ELSIF (conv_dow) ='4' then letter_dow := 'Jueves';
                ELSIF (conv_dow) ='5' then letter_dow := 'Viernes'; 
                ELSIF (conv_dow) ='6' then letter_dow := 'Sabado';
                ELSE letter_dow := ('Dia Invalido') ;
                END IF;
        END IF;
    END IF;
    if upper(upp_low) = 'U' then
	    letter_dow :=  upper(letter_dow);
    END IF;
    if upper(upp_low) = 'L' then
	    letter_dow :=  lower(letter_dow);
    END IF;
    RETURN (letter_dow);
END;

--﻿ Function: adempiere.amf_month2letter(character, character, character)
-- FUNCTION adempiere.amf_month2letter(bpchar, bpchar, bpchar);

CREATE OR REPLACE FUNCTION adempiere.amf_month2letter(conv_month varchar, upp_low varchar, languaje_iso varchar)
    RETURN VARCHAR 
IS
    letter_month varchar(40);
    nullvalue VARCHAR(40);
   
BEGIN
    SELECT '*** NULL ***' INTO nullvalue FROM DUAL;
    IF (conv_month IS NULL) 
    THEN 
	letter_month := nullvalue ;
    ELSIF (conv_month IS NOT NULL) 
    THEN
	-- SPANISH
	 case when languaje_iso = 'es' then 
		case when (conv_month) ='01' then letter_month := 'Enero';
		     when (conv_month) ='02' then letter_month := 'Febrero'; 
		     when (conv_month) ='03' then letter_month := 'Marzo'; 
		     when (conv_month) ='04' then letter_month := 'Abril';
		     when (conv_month) ='05' then letter_month := 'Mayo';
		     when (conv_month) ='06' then letter_month := 'Junio'; 
		     when (conv_month) ='07' then letter_month := 'Julio';
		     when (conv_month) ='08' then letter_month := 'Agosto';
		     when (conv_month) ='09' then letter_month := 'Septiembre'; 
		     when (conv_month) ='10' then letter_month := 'Octubre';
		     when (conv_month) ='11' then letter_month := 'Noviembre'; 
		     when (conv_month) ='12' then letter_month := 'Diciembre';
		     else letter_month := ('Invalid Month') ;
		end  case;
	-- ENGLISH
	      when languaje_iso = 'en' then 
		case when (conv_month) ='01' then letter_month := 'January';
		     when (conv_month) ='02' then letter_month := 'February'; 
		     when (conv_month) ='03' then letter_month := 'March'; 
		     when (conv_month) ='04' then letter_month := 'April';
		     when (conv_month) ='05' then letter_month := 'May';
		     when (conv_month) ='06' then letter_month := 'June'; 
		     when (conv_month) ='07' then letter_month := 'July';
		     when (conv_month) ='08' then letter_month := 'August';
		     when (conv_month) ='09' then letter_month := 'September'; 
		     when (conv_month) ='10' then letter_month := 'October';
		     when (conv_month) ='11' then letter_month := 'November'; 
		     when (conv_month) ='12' then letter_month := 'December';
		     else letter_month := ('Invalid Month') ;
		end  case;
	-- DEFAULT SPANISH
	      else 
		case when (conv_month) ='01' then letter_month := 'Enero';
		     when (conv_month) ='02' then letter_month := 'Febrero'; 
		     when (conv_month) ='03' then letter_month := 'Marzo'; 
		     when (conv_month) ='04' then letter_month := 'Abril';
		     when (conv_month) ='05' then letter_month := 'Mayo';
		     when (conv_month) ='06' then letter_month := 'Junio'; 
		     when (conv_month) ='07' then letter_month := 'Julio';
		     when (conv_month) ='08' then letter_month := 'Agosto';
		     when (conv_month) ='09' then letter_month := 'Septiembre'; 
		     when (conv_month) ='10' then letter_month := 'Octubre';
		     when (conv_month) ='11' then letter_month := 'Noviembre'; 
		     when (conv_month) ='12' then letter_month := 'Diciembre';
		     else letter_month := ('Invalid Month') ;
		end  case;
	      
	 end case;

    END IF;
    if upper(upp_low) = 'U' then
	    letter_month :=  upper(letter_month);
    end if;
    if upper(upp_low) = 'L' then
	    letter_month :=  lower(letter_month);
    end if;
    RETURN (letter_month);
END;


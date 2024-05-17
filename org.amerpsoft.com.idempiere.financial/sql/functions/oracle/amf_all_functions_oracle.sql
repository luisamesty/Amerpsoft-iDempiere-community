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

CREATE OR REPLACE FUNCTION adempiere.amf_num2letter1000(conv_number numeric, languaje_iso varchar)
  RETURN VARCHAR 

IS
    reg numeric;
    letter_number varchar(255) ;
    tmp_number varchar(250) ;
    nullvalue varchar(20);
    notnullvalue varchar(40);
    numero numeric;
    entero numeric;
    centenas numeric;
    decenas numeric;
    unidades numeric;
    unir varchar(2);
    aux varchar(15);
    
BEGIN
    SELECT '*** NULL ***' INTO nullvalue FROM DUAL;
    SELECT '*** NOT NULL ***' INTO notnullvalue FROM DUAL;
    IF (conv_number IS NULL) 
    THEN 
	letter_number := nullvalue ;
    ELSIF (conv_number IS NOT NULL) 
    THEN
       numero := conv_number;

       if numero < 0 or numero > 999 then
           case when languaje_iso = 'es' then return ('fuera_de_rango');
                when languaje_iso = 'en' then return ('out_of_range');
                else return ('fuera_de_rango');
           end case;
       end if;
       centenas := trunc(numero / 100);
       decenas  := trunc((numero - centenas*100)/10);
       unidades := trunc(numero - centenas*100 - decenas*10 );

        if numero = 100 then
           case when languaje_iso = 'es' then letter_number :=  'cien ';
                when languaje_iso = 'en' then letter_number :=  'one hundred ';
                else letter_number :=  'cien ';
           end case;
        elsif numero = 0 then
           case when languaje_iso = 'es' then letter_number :=  'cero ';
                when languaje_iso = 'en' then letter_number :=  'zero ';
                else letter_number :=  'cero '; 
           end case;
        elsif numero = 1 then
           case when languaje_iso = 'es' then letter_number :=  'uno ';
                when languaje_iso = 'en' then letter_number :=  'one ';
                else letter_number :=  'uno '; 
           end case;        
        else
            case when languaje_iso = 'es' then  unir := 'y ';
                when languaje_iso = 'en' then  unir := ' ';
                else  unir := 'y ';
            end case;         
            ---------------
            -- CENTENAS
            ---------------
            -- es: Spanish
            case when languaje_iso = 'es' then 
		    if centenas = 1 then
			letter_number := 'ciento ';
		    elsif centenas = 2 then
			letter_number := 'doscientos ';
		    elsif centenas = 3 then
			letter_number := 'trescientos ';
		    elsif centenas = 4 then
			letter_number := 'cuatrocientos ';
		    elsif centenas = 5 then
			letter_number := 'quinientos ';
		    elsif centenas = 6 then
			letter_number := 'seiscientos ';
		    elsif centenas = 7 then
			letter_number := 'setecientos ';
		    elsif centenas = 8 then
			letter_number := 'ochocientos ';
		    elsif centenas = 9 then
			letter_number := 'novecientos ';
		    elsif centenas = 0 then
			letter_number := '';
		    end if;
            ---------------
            -- CENTENAS
            ---------------
            -- en: Engish
                when languaje_iso = 'en' then 
		    if centenas = 1 then
			letter_number := 'one hundred ';
		    elsif centenas = 2 then
			letter_number := 'two hundred ';
		    elsif centenas = 3 then
			letter_number := 'three hundred ';
		    elsif centenas = 4 then
			letter_number := 'four hundred ';
		    elsif centenas = 5 then
			letter_number := 'five hundred ';
		    elsif centenas = 6 then
			letter_number := 'six hundred ';
		    elsif centenas = 7 then
			letter_number := 'seven hundred ';
		    elsif centenas = 8 then
			letter_number := 'eight hundred ';
		    elsif centenas = 9 then
			letter_number := 'nine hindred ';
		    elsif centenas = 0 then
			letter_number := '';
		    end if;
            ---------------
            -- CENTENAS
            ---------------
            -- default: Spanish
            else 
		    if centenas = 1 then
			letter_number := 'ciento ';
		    elsif centenas = 2 then
			letter_number := 'doscientos ';
		    elsif centenas = 3 then
			letter_number := 'trescientos ';
		    elsif centenas = 4 then
			letter_number := 'cuatrocientos ';
		    elsif centenas = 5 then
			letter_number := 'quinientos ';
		    elsif centenas = 6 then
			letter_number := 'seiscientos ';
		    elsif centenas = 7 then
			letter_number := 'setecientos ';
		    elsif centenas = 8 then
			letter_number := 'ochocientos ';
		    elsif centenas = 9 then
			letter_number := 'novecientos ';
		    elsif centenas = 0 then
			letter_number := '';
		    end if;
            end case; 
            ---------------
            -- DECENAS
            ---------------
            -- es: Spanish
            case when languaje_iso = 'es' then 
		    if decenas = 3 then
			letter_number := letter_number || 'treinta ';
		    elsif decenas = 4 then
			letter_number := letter_number || 'cuarenta ';
		    elsif decenas = 5 then
			letter_number := letter_number || 'cincuenta ';
		    elsif decenas = 6 then
			letter_number := letter_number || 'sesenta ';
		    elsif decenas = 7 then
			letter_number := letter_number || 'setenta ';
		    elsif decenas = 8 then
			letter_number := letter_number || 'ochenta ';
		    elsif decenas = 9 then
			letter_number := letter_number || 'noventa ';
		    elsif decenas = 1 then
			if unidades < 6 then
			    if unidades = 0 then
				letter_number := letter_number || 'diez ';
			    elsif unidades = 1 then
				letter_number := letter_number || 'once ';
			    elsif unidades = 2 then
				letter_number := letter_number || 'doce ';
			    elsif unidades = 3 then
				letter_number := letter_number || 'trece ';
			    elsif unidades = 4 then
				letter_number := letter_number || 'catorce ';
			    elsif unidades = 5 then
				letter_number := letter_number || 'quince ';
			    end if;
			    unidades := 0;
			else
			    letter_number := letter_number || 'dieci';
			    unir := '';
			end if;
		    elsif decenas = 2 then
			if unidades = 0 then
			    letter_number := letter_number || 'veinte ';
			else
			    letter_number := letter_number || 'veinti';
			end if;
			unir := '';
		    elsif decenas = 0 then
			unir := '';
		    end if;            
            ---------------
            -- DECENAS
            ---------------
            -- en: Engish
            when languaje_iso = 'en' then 
		    if decenas = 3 then
			letter_number := letter_number || 'thirty ';
		    elsif decenas = 4 then
			letter_number := letter_number || 'fourty ';
		    elsif decenas = 5 then
			letter_number := letter_number || 'fifty ';
		    elsif decenas = 6 then
			letter_number := letter_number || 'sixty ';
		    elsif decenas = 7 then
			letter_number := letter_number || 'seventy ';
		    elsif decenas = 8 then
			letter_number := letter_number || 'eighty ';
		    elsif decenas = 9 then
			letter_number := letter_number || 'ninety ';
		    elsif decenas = 1 then	
			    if unidades = 0 then
				letter_number := letter_number || 'ten ';
			    elsif unidades = 1 then
				letter_number := letter_number || 'eleven ';
			    elsif unidades = 2 then
				letter_number := letter_number || 'twelve ';
			    elsif unidades = 3 then
				letter_number := letter_number || 'thirteen ';
			    elsif unidades = 4 then
				letter_number := letter_number || 'fourteen ';
			    elsif unidades = 5 then
				letter_number := letter_number || 'fifteen ';
			    elsif unidades = 6 then
				letter_number := letter_number || 'sixteen ';
			    elsif unidades = 7 then
				letter_number := letter_number || 'seventeen ';
			    elsif unidades = 8 then
				letter_number := letter_number || 'eightteen ';
			    elsif unidades = 9 then
				letter_number := letter_number || 'nineteen ';
			    end if;
			    unidades := 0;			
		    elsif decenas = 2 then
			if unidades = 0 then
			    letter_number := letter_number || 'twenty ';
			else
			    letter_number := letter_number || 'twenty';
			end if;
			unir := '';
		    elsif decenas = 0 then
			unir := '';
		    end if;            
            ---------------
            -- DECENAS
            ---------------
            -- default: Spanish
            else 
		    if decenas = 3 then
			letter_number := letter_number || 'treinta ';
		    elsif decenas = 4 then
			letter_number := letter_number || 'cuarenta ';
		    elsif decenas = 5 then
			letter_number := letter_number || 'cincuenta ';
		    elsif decenas = 6 then
			letter_number := letter_number || 'sesenta ';
		    elsif decenas = 7 then
			letter_number := letter_number || 'setenta ';
		    elsif decenas = 8 then
			letter_number := letter_number || 'ochenta ';
		    elsif decenas = 9 then
			letter_number := letter_number || 'noventa ';
		    elsif decenas = 1 then
			if unidades < 6 then
			    if unidades = 0 then
				letter_number := letter_number || 'diez ';
			    elsif unidades = 1 then
				letter_number := letter_number || 'once ';
			    elsif unidades = 2 then
				letter_number := letter_number || 'doce ';
			    elsif unidades = 3 then
				letter_number := letter_number || 'trece ';
			    elsif unidades = 4 then
				letter_number := letter_number || 'catorce ';
			    elsif unidades = 5 then
				letter_number := letter_number || 'quince ';
			    end if;
			    unidades := 0;
			else
			    letter_number := letter_number || 'dieci';
			    unir := '';
			end if;
		    elsif decenas = 2 then
			if unidades = 0 then
			    letter_number := letter_number || 'veinte ';
			else
			    letter_number := letter_number || 'veinti';
			end if;
			unir := '';
		    elsif decenas = 0 then
			unir := '';
		    end if;            
            end case; 
            
            ---------------
            -- UNIDADES
            ---------------
            -- es: Spanish
            case when languaje_iso = 'es' then 
		   if unidades = 1 then
			letter_number := letter_number || unir || 'uno ';
		    elsif unidades = 2 then
			letter_number := letter_number || unir || 'dos ';
		    elsif unidades = 3 then
			letter_number := letter_number || unir || 'tres ';
		    elsif unidades = 4 then
			letter_number := letter_number || unir || 'cuatro ';
		    elsif unidades = 5 then
			letter_number := letter_number || unir || 'cinco ';
		    elsif unidades = 6 then
			letter_number := letter_number || unir || 'seis ';
		    elsif unidades = 7 then
			letter_number := letter_number || unir || 'siete ';
		    elsif unidades = 8 then
			letter_number := letter_number || unir || 'ocho ';
		    elsif unidades = 9 then
			letter_number := letter_number || unir || 'nueve ';
		    end if;
            ---------------
            -- UNIDADES
            ---------------
            -- en: Engish
            when languaje_iso = 'en' then             
		   if unidades = 1 then
			letter_number := letter_number  || 'one ';
		    elsif unidades = 2 then
			letter_number := letter_number  || 'two ';
		    elsif unidades = 3 then
			letter_number := letter_number  || 'three ';
		    elsif unidades = 4 then
			letter_number := letter_number  || 'four ';
		    elsif unidades = 5 then
			letter_number := letter_number  || 'five ';
		    elsif unidades = 6 then
			letter_number := letter_number  || 'six ';
		    elsif unidades = 7 then
			letter_number := letter_number  || 'seven ';
		    elsif unidades = 8 then
			letter_number := letter_number  || 'eight ';
		    elsif unidades = 9 then
			letter_number := letter_number  || 'nine ';
		    end if;
             ---------------
            -- UNIDADES
            ---------------
            -- default: Spanish
            else 
		   if unidades = 1 then
			letter_number := letter_number || unir || 'uno ';
		    elsif unidades = 2 then
			letter_number := letter_number || unir || 'dos ';
		    elsif unidades = 3 then
			letter_number := letter_number || unir || 'tres ';
		    elsif unidades = 4 then
			letter_number := letter_number || unir || 'cuatro ';
		    elsif unidades = 5 then
			letter_number := letter_number || unir || 'cinco ';
		    elsif unidades = 6 then
			letter_number := letter_number || unir || 'seis ';
		    elsif unidades = 7 then
			letter_number := letter_number || unir || 'siete ';
		    elsif unidades = 8 then
			letter_number := letter_number || unir || 'ocho ';
		    elsif unidades = 9 then
			letter_number := letter_number || unir || 'nueve ';
		    end if;
            end case; 
       end if;
 --letter_number := centenas || '' || decenas || '' || unidades || '' || letter_number;
 END IF;

RETURN (letter_number);
END;

create or replace FUNCTION           amf_num2letter(conv_number varchar, upp_low varchar, languaje_iso varchar)
  RETURN VARCHAR 

IS
    reg numeric;
    letter_number varchar(255);
    tmp_number varchar(255) ;
    notnullvalue varchar(30);
    choice char(1);
    millares_de_millon numeric;
    millones numeric;
    millares numeric;
    centenas numeric;
    centimos numeric;
	numero numeric;
    en_letras varchar(200);
    entero numeric;
    aux varchar(15);
    nullvalue VARCHAR(40);

BEGIN
    SELECT '*** NULL ***' INTO nullvalue FROM DUAL;
    SELECT '*** NOT NULL ***' INTO notnullvalue FROM DUAL;
    choice := 'y';
    IF (conv_number IS NULL) 
    THEN 
	letter_number := nullvalue ;
    ELSIF (conv_number IS NOT NULL) 
    THEN
        numero := conv_number;
        if numero < 0 or numero > 999999999999.99 then
            case when languaje_iso = 'es' then return ('fuera_de_rango');
                when languaje_iso = 'en' then return ('out_of_range');
                else return ('fuera_de_rango');
            end case;
            letter_number := en_letras;
            RETURN (letter_number);
        end if;
        entero := trunc(numero);

        millares_de_millon := 0;
	millones := 0;
        millares_de_millon := trunc( numero/ 1000000000);
        millones := trunc((numero - millares_de_millon*1000000000) / 1000000);
        millares := trunc((numero - millares_de_millon*1000000000 - millones*1000000)  / 1000);
        centenas := trunc((numero - millares_de_millon*1000000000 - millones*1000000- millares*1000)  / 1);
        centimos := trunc((numero - trunc(numero))*100);
        -- MILLARES DE MILLON
        if millares_de_millon > 0 then
           if millares_de_millon = 1 then
               if millones = 0 then
                   case when languaje_iso = 'es' then en_letras := 'mil millones ';
                        when languaje_iso = 'en' then en_letras := 'one billion ';
                        else en_letras := 'mil millones ';
                   end case;
               else  --if millones = 0 then
                   case when languaje_iso = 'es' then en_letras := 'mil ';
                        when languaje_iso = 'en' then en_letras := 'one billion ';
                        else en_letras := 'mil ';
                   end case;                 
               end if;
           else
               en_letras := adempiere.amf_num2letter1000(millares_de_millon, languaje_iso);
               if millones = 0 then
                   case when languaje_iso = 'es' then en_letras := concat(en_letras, 'mil millones ');
                        when languaje_iso = 'en' then en_letras := concat(en_letras, 'billion ');
                        else en_letras := concat(en_letras, 'mil millones ');
                   end case;     
               else  --if millones = 0 then
                   case when languaje_iso = 'es' then en_letras := concat(en_letras, 'mil ');
                        when languaje_iso = 'en' then en_letras := concat(en_letras, 'billion ');
                        else en_letras := concat(en_letras, 'mil ');
                   end case;     
               end if;          
           end if;
            --  en_letras := concat('MILLARES DE MILLONES ' , en_letras );
        end if;  
        -- MILLONES
        if millones > 0 then
		if millones = 1 then
		    if millares_de_millon = 0 then
		       case when languaje_iso = 'es' then en_letras := 'un millón ';
			   when languaje_iso = 'en' then en_letras := 'one million ';
			   else en_letras := 'un millón ';
		       end case;
		    else
		       case when languaje_iso = 'es' then en_letras := 'un millones ';
			   when languaje_iso = 'en' then en_letras := 'one million ';
			   else en_letras := 'un millones ';
		       end case;
		    end if;
		else  --if millones > 0 then
		--replace(adempiere.amf_num2letter1000(millones,languaje_iso),'uno ','un ')
                   case when languaje_iso = 'es' then en_letras := concat(concat(en_letras, replace(adempiere.amf_num2letter1000(millones,languaje_iso),'uno ','un ')), 'millones ');
                        when languaje_iso = 'en' then en_letras := concat(concat(en_letras, adempiere.amf_num2letter1000(millones,languaje_iso)), 'million ');
                        else en_letras := concat(concat(en_letras, replace(adempiere.amf_num2letter1000(millones,languaje_iso),'uno ','un ')), 'millones ');
                   end case; 		
		end if;
              --en_letras := 'MILLONES ' || en_letras ;
         end if;        
         -- MILLARES
         if millares > 0 then
             if millares = 1  then
                 case when languaje_iso = 'es' then en_letras := concat(en_letras , 'un mil ');
                      when languaje_iso = 'en' then en_letras := concat(en_letras , 'one thousand ');
                      else en_letras := concat(en_letras , 'un mil ');
                 end case;  
             else
                 case when languaje_iso = 'es' then en_letras := concat(concat(en_letras , replace(adempiere.amf_num2letter1000(millares,languaje_iso),'uno ','un ')) , 'mil ');
                      when languaje_iso = 'en' then en_letras := concat(concat(en_letras , adempiere.amf_num2letter1000(millares,languaje_iso)) , 'thousand ');
                      else en_letras := concat(concat(en_letras , replace(adempiere.amf_num2letter1000(millares,languaje_iso),'uno ','un '))  , 'mil ');
                 end case; 
             end if;
             --en_letras := concat('MILLARES ' , en_letras );
         end if;

        -- CENTENAS
        if centenas > 0  then
           -- case when languaje_iso = 'es' then en_letras := concat(en_letras , adempiere.amf_num2letter1000(centenas,languaje_iso));
            --    when languaje_iso = 'en' then en_letras := concat(en_letras , adempiere.amf_num2letter1000(centenas,languaje_iso));
            --    else en_letras := concat(en_letras , adempiere.amf_num2letter1000(centenas,languaje_iso));
            --end case; 
            en_letras := concat(en_letras , replace(adempiere.amf_num2letter1000(centenas,languaje_iso),'uno ','uno '));
            --en_letras := 'CENTENAS ' || en_letras ;
        end if;
	-- CENTIMOS
        if centimos > 0 then
            if centimos = 1 then
                 case when languaje_iso = 'es' then en_letras := concat(en_letras , 'con un centimo' );
                      when languaje_iso = 'en' then en_letras := concat(en_letras , 'with one cent' );
                      else en_letras := concat(en_letras , 'un mil ');
                 end case;              
            else
		if entero > 0 then
                    case when languaje_iso = 'es' then en_letras := concat(concat(concat(en_letras , 'con ') , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ')) , 'centimos ');
                         when languaje_iso = 'en' then en_letras := concat(concat(concat(en_letras , 'with ') , adempiere.amf_num2letter1000(centimos,languaje_iso)) , 'cents ');
                         else en_letras := concat(concat(concat(en_letras , 'con ') , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ')) , 'centimos ');
                    end case;    		
		else
                    case when languaje_iso = 'es' then en_letras := concat(concat(en_letras , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ')) , 'centimos ');
                         when languaje_iso = 'en' then en_letras := concat(concat(en_letras , adempiere.amf_num2letter1000(centimos,languaje_iso)) , 'cents ');
                         else en_letras := concat(concat(en_letras , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ')) , 'centimos ');
                    end case;   		
		end if;
            end if;
	else
	    if entero > 0 then
                 case when languaje_iso = 'es' then en_letras := concat(en_letras , 'con 00/100 centimos') ;
                      when languaje_iso = 'en' then en_letras := concat(en_letras , 'with 00/100 cents') ;
                      else en_letras := concat(en_letras , 'con 00/100 centimos') ;
                 end case;
	    else
                 case when languaje_iso = 'es' then en_letras := concat(en_letras , 'cero con 00/100 centimos') ;
                      when languaje_iso = 'en' then en_letras := concat(en_letras , 'zero wiyh 00/100 cents') ;
                      else en_letras := concat(en_letras , 'cero con 00/100 centimos') ;
                 end case;	    
 	    end if;
        end if;     
	letter_number :=  en_letras;
        if upper(upp_low) = 'U' then
	    letter_number :=  upper(en_letras);
        end if;
        if upper(upp_low) = 'L' then
	    letter_number :=  lower(en_letras);
         end if;

        -- TRAZA
	--letter_number := concat('entero:',entero,'Millares de Millones:',millares_de_millon,' Millones:',millones,' Millares:',millares,' Centenas:',centenas,' Centimos:',centimos,' ',en_letras) ;

    END IF;

    RETURN (letter_number);
END;

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

-- DROP FUNCTION adempiere.amf_num2letter(numeric, bpchar, bpchar);

CREATE OR REPLACE FUNCTION adempiere.amf_num2letter(conv_number numeric, upp_low character, languaje_iso character)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$

DECLARE
    reg numeric;
    letter_number varchar(255);
    tmp_number varchar ;
    nullvalue varchar(20);
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
    
BEGIN
    nullvalue := '*** NULL ***';
    notnullvalue := '** NOT NULL *** ';
    choice := 'y';
    languaje_iso := lower(languaje_iso);
    IF (conv_number IS NULL) 
    THEN 
	letter_number := nullvalue ;
    ELSEIF (conv_number IS NOT NULL) 
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
                   case when languaje_iso = 'es' then en_letras := concat(en_letras, replace(adempiere.amf_num2letter1000(millones,languaje_iso),'uno ','un '), 'millones ');
                        when languaje_iso = 'en' then en_letras := concat(en_letras, adempiere.amf_num2letter1000(millones,languaje_iso), 'million ');
                        else en_letras := concat(en_letras, replace(adempiere.amf_num2letter1000(millones,languaje_iso),'uno ','un '), 'millones ');
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
                 case when languaje_iso = 'es' then en_letras := concat(en_letras , replace(adempiere.amf_num2letter1000(millares,languaje_iso),'uno ','un ') , 'mil ');
                      when languaje_iso = 'en' then en_letras := concat(en_letras , adempiere.amf_num2letter1000(millares,languaje_iso) , 'thousand ');
                      else en_letras := concat(en_letras , replace(adempiere.amf_num2letter1000(millares,languaje_iso),'uno ','un ')  , 'mil ');
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
                    case when languaje_iso = 'es' then en_letras := concat(en_letras , 'con ' , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ') , 'centimos ');
                         when languaje_iso = 'en' then en_letras := concat(en_letras , 'with ' , adempiere.amf_num2letter1000(centimos,languaje_iso) , 'cents ');
                         else en_letras := concat(en_letras , 'con ' , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ') , 'centimos ');
                    end case;    		
		else
                    case when languaje_iso = 'es' then en_letras := concat(en_letras , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ') , 'centimos ');
                         when languaje_iso = 'en' then en_letras := concat(en_letras , adempiere.amf_num2letter1000(centimos,languaje_iso) , 'cents ');
                         else en_letras := concat(en_letras , replace(adempiere.amf_num2letter1000(centimos,languaje_iso),'uno ','un ') , 'centimos ');
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
                      when languaje_iso = 'en' then en_letras := concat(en_letras , 'zero with 00/100 cents') ;
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

$function$
;

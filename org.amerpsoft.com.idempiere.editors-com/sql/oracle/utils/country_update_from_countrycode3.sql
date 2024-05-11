-- BLOQUE REEMPLAZO
-- Reemplaza el codigo de tres digitos en la tabla c_country
update  adempiere.c_country
set countrycode3=(
select adempiere.c_country3.countrycode3 
from  adempiere.c_country3 
where adempiere.c_country.countrycode=adempiere.c_country3.countrycode );


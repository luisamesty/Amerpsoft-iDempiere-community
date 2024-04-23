/* Agrega campo countrycode3 codigo de tres digitos internacional */

ALTER TABLE "adempiere"."c_country"
  ADD COLUMN "countrycode3" CHAR(3);

ALTER TABLE "adempiere"."c_country"
  ADD COLUMN "hascommunity" CHAR(1) DEFAULT 'Y';

ALTER TABLE "adempiere"."c_country"
  ADD COLUMN "hasmunicipality" CHAR(1) DEFAULT 'Y';
  
ALTER TABLE "adempiere"."c_country"
 ADD COLUMN "hasparish" CHAR(1) DEFAULT 'Y';
  
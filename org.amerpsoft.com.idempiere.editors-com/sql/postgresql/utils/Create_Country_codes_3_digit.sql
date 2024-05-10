--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

/* Agrega campo countrycode3 codigo de tres digitos internacional */

-- NOT NECESSARY BEACURE ALREADY SET ON APPLICATION DICTIONARY
--ALTER TABLE "adempiere"."c_country"
 -- ADD COLUMN "countrycode3" CHAR(3);

--ALTER TABLE "adempiere"."c_country"
 -- ADD COLUMN "hascommunity" CHAR(1) DEFAULT 'Y';

/* Cre temporal de codigos de tres digitos */
 
CREATE TABLE "adempiere"."c_country2" (
  "countrycode" CHAR(2) NOT NULL, 
  "countrycode3" CHAR(3) NOT NULL, 
  "countryid" CHAR(3) NOT NULL, 
  "name" VARCHAR(60) NOT NULL, 
  "detalle1" VARCHAR(20), 
  "detalle2" VARCHAR(20), 
  "modificado" NUMERIC(1,0)
) WITHOUT OIDS;

/* Data for the `public.c_country2` table  (Records 1 - 244) */

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AW', 'ABW', '533', 'Aruba', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AF', 'AFG', '004', 'Afganistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AO', 'AGO', '024', 'Angola', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AI', 'AIA', '660', 'Anguila', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AX', 'ALA', '248', 'Åland', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AL', 'ALB', '008', 'Albania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AD', 'AND', '020', 'Andorra', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AN', 'ANT', '530', 'Antillas Neerlandesas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AE', 'ARE', '784', 'Emiratos Árabes Unidos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AR', 'ARG', '032', 'Argentina', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AM', 'ARM', '051', 'Armenia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AS', 'ASM', '016', 'Samoa Americana', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AQ', 'ATA', '010', 'Antártida', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TF', 'ATF', '260', 'Territorios Australes Franceses', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AG', 'ATG', '028', 'Antigua y Barbuda', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AU', 'AUS', '036', 'Australia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AT', 'AUT', '040', 'Austria', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('AZ', 'AZE', '031', 'Azerbaiyán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BI', 'BDI', '108', 'Burundi', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BE', 'BEL', '056', 'Bélgica', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BJ', 'BEN', '204', 'Benín', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BF', 'BFA', '854', 'Burkina Faso', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BD', 'BGD', '050', 'Bangladesh', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BG', 'BGR', '100', 'Bulgaria', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BH', 'BHR', '048', 'Bahréin', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BS', 'BHS', '044', 'Bahamas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BA', 'BIH', '070', 'Bosnia y Herzegovina', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BY', 'BLR', '112', 'Bielorrusia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BZ', 'BLZ', '084', 'Belice', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BM', 'BMU', '060', 'Bermudas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BO', 'BOL', '068', 'Bolivia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BR', 'BRA', '076', 'Brasil', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BB', 'BRB', '052', 'Barbados', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BN', 'BRN', '096', 'Brunéi', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BT', 'BTN', '064', 'Bután', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BV', 'BVT', '074', 'Isla Bouvet', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('BW', 'BWA', '072', 'Botsuana', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CF', 'CAF', '140', 'República Centroafricana', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CA', 'CAN', '124', 'Canadá', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CC', 'CCK', '166', 'Islas Cocos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CH', 'CHE', '756', 'Suiza', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CL', 'CHL', '152', 'Chile', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CN', 'CHN', '156', 'China', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CI', 'CIV', '384', 'Costa de Marfil', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CM', 'CMR', '120', 'Camerún', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CD', 'COD', '180', 'República Democrática del Congo', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CG', 'COG', '178', 'República del Congo', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CK', 'COK', '184', 'Islas Cook', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CO', 'COL', '170', 'Colombia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KM', 'COM', '174', 'Comoras', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CV', 'CPV', '132', 'Cabo Verde', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CR', 'CRI', '188', 'Costa Rica', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CU', 'CUB', '192', 'Cuba', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CX', 'CXR', '162', 'Isla de Navidad', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KY', 'CYM', '136', 'Islas Caimán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CY', 'CYP', '196', 'Chipre', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('CZ', 'CZE', '203', 'República Checa', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('DE', 'DEU', '276', 'Alemania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('DJ', 'DJI', '262', 'Yibuti', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('DM', 'DMA', '212', 'Dominica', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('DK', 'DNK', '208', 'Dinamarca', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('DO', 'DOM', '214', 'República Dominicana', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('DZ', 'DZA', '012', 'Argelia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('EC', 'ECU', '218', 'Ecuador', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('EG', 'EGY', '818', 'Egipto', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ER', 'ERI', '232', 'Eritrea', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('EH', 'ESH', '732', 'Sahara Occidental', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ES', 'ESP', '724', 'España (Además están reservados EA para Ceuta y M', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('EE', 'EST', '233', 'Estonia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ET', 'ETH', '231', 'Etiopía', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('FI', 'FIN', '246', 'Finlandia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('FJ', 'FJI', '242', 'Fiyi', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('FK', 'FLK', '238', 'Islas Malvinas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('FR', 'FRA', '250', 'Francia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('FO', 'FRO', '234', 'Islas Feroe', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('FM', 'FSM', '583', 'Micronesia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GA', 'GAB', '266', 'Gabón', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GB', 'GBR', '826', 'Reino Unido', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GE', 'GEO', '268', 'Georgia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GG', 'GGY', '831', 'Guernsey', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GH', 'GHA', '288', 'Ghana', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GI', 'GIB', '292', 'Gibraltar', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GN', 'GIN', '324', 'Guinea', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GP', 'GLP', '312', 'Guadalupe', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GM', 'GMB', '270', 'Gambia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GW', 'GNB', '624', 'Guinea-Bissau', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GQ', 'GNQ', '226', 'Guinea Ecuatorial', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GR', 'GRC', '300', 'Grecia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GD', 'GRD', '308', 'Granada', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GL', 'GRL', '304', 'Groenlandia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GT', 'GTM', '320', 'Guatemala', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GF', 'GUF', '254', 'Guayana Francesa', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GU', 'GUM', '316', 'Guam', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GY', 'GUY', '328', 'Guyana', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('HK', 'HKG', '344', 'Hong Kong', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('HM', 'HMD', '334', 'Islas Heard y McDonald', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('HN', 'HND', '340', 'Honduras', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('HR', 'HRV', '191', 'Croacia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('HT', 'HTI', '332', 'Haití', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('HU', 'HUN', '348', 'Hungría', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ID', 'IDN', '360', 'Indonesia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IM', 'IMN', '833', 'Isla de Man', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IN', 'IND', '356', 'India', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IO', 'IOT', '086', 'Territorio Británico del Océano Índico', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IE', 'IRL', '372', 'Irlanda', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IR', 'IRN', '364', 'Irán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IQ', 'IRQ', '368', 'Iraq', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IS', 'ISL', '352', 'Islandia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IL', 'ISR', '376', 'Israel', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('IT', 'ITA', '380', 'Italia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('JM', 'JAM', '388', 'Jamaica', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('JE', 'JEY', '832', 'Jersey', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('JO', 'JOR', '400', 'Jordania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('JP', 'JPN', '392', 'Japón', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KZ', 'KAZ', '398', 'Kazajistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KE', 'KEN', '404', 'Kenia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KG', 'KGZ', '417', 'Kirguistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KH', 'KHM', '116', 'Camboya', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KI', 'KIR', '296', 'Kiribati', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KN', 'KNA', '659', 'San Cristóbal y Nieves', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KR', 'KOR', '410', 'Corea del Sur', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KW', 'KWT', '414', 'Kuwait', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LA', 'LAO', '418', 'Laos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LB', 'LBN', '422', 'Líbano', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LR', 'LBR', '430', 'Liberia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LY', 'LBY', '434', 'Libia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LC', 'LCA', '662', 'Santa Lucía', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LI', 'LIE', '438', 'Liechtenstein', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LK', 'LKA', '144', 'Sri Lanka', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LS', 'LSO', '426', 'Lesoto', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LT', 'LTU', '440', 'Lituania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LU', 'LUX', '442', 'Luxemburgo', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('LV', 'LVA', '428', 'Letonia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MO', 'MAC', '446', 'Macao', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MA', 'MAR', '504', 'Marruecos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MC', 'MCO', '492', 'Mónaco', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MD', 'MDA', '498', 'Moldavia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MG', 'MDG', '450', 'Madagascar', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MV', 'MDV', '462', 'Maldivas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MX', 'MEX', '484', 'México', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MH', 'MHL', '584', 'Islas Marshall', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MK', 'MKD', '807', 'ARY Macedonia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ML', 'MLI', '466', 'Malí', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MT', 'MLT', '470', 'Malta', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MM', 'MMR', '104', 'Birmania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ME', 'MNE', '499', 'Montenegro', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MN', 'MNG', '496', 'Mongolia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MP', 'MNP', '580', 'Islas Marianas del Norte', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MZ', 'MOZ', '508', 'Mozambique', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MR', 'MRT', '478', 'Mauritania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MS', 'MSR', '500', 'Montserrat', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MQ', 'MTQ', '474', 'Martinica', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MU', 'MUS', '480', 'Mauricio', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MW', 'MWI', '454', 'Malaui', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('MY', 'MYS', '458', 'Malasia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('YT', 'MYT', '175', 'Mayotte', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NA', 'NAM', '516', 'Namibia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NC', 'NCL', '540', 'Nueva Caledonia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NE', 'NER', '562', 'Níger', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NF', 'NFK', '574', 'Norfolk', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NG', 'NGA', '566', 'Nigeria', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NI', 'NIC', '558', 'Nicaragua', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NU', 'NIU', '570', 'Niue', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NL', 'NLD', '528', 'Países Bajos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NO', 'NOR', '578', 'Noruega', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NP', 'NPL', '524', 'Nepal', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NR', 'NRU', '520', 'Nauru', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('NZ', 'NZL', '554', 'Nueva Zelanda', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('OM', 'OMN', '512', 'Omán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PK', 'PAK', '586', 'Pakistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PA', 'PAN', '591', 'Panamá', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PN', 'PCN', '612', 'Islas Pitcairn', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PE', 'PER', '604', 'Perú', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PH', 'PHL', '608', 'Filipinas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PW', 'PLW', '585', 'Palaos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PG', 'PNG', '598', 'Papúa Nueva Guinea', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PL', 'POL', '616', 'Polonia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PR', 'PRI', '630', 'Puerto Rico', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('KP', 'PRK', '408', 'Corea del Norte', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PT', 'PRT', '620', 'Portugal', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PY', 'PRY', '600', 'Paraguay', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PS', 'PSE', '275', 'Territorios palestinos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PF', 'PYF', '258', 'Polinesia Francesa', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('QA', 'QAT', '634', 'Qatar', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('RE', 'REU', '638', 'Reunión', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('RO', 'ROU', '642', 'Rumania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('RU', 'RUS', '643', 'Rusia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('RW', 'RWA', '646', 'Ruanda', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SA', 'SAU', '682', 'Arabia Saudita', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SD', 'SDN', '736', 'Sudán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SN', 'SEN', '686', 'Senegal', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SG', 'SGP', '702', 'Singapur', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('GS', 'SGS', '239', 'Islas Georgias del Sur y Sandwich del Sur', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SH', 'SHN', '654', 'Santa Helena', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SJ', 'SJM', '744', 'Svalbard y Jan Mayen', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SB', 'SLB', '090', 'Islas Salomón', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SL', 'SLE', '694', 'Sierra Leona', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SV', 'SLV', '222', 'El Salvador', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SM', 'SMR', '674', 'San Marino', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SO', 'SOM', '706', 'Somalia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('PM', 'SPM', '666', 'San Pedro y Miquelón', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('RS', 'SRB', '688', 'Serbia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ST', 'STP', '678', 'Santo Tomé y Príncipe', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SR', 'SUR', '740', 'Surinam', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SK', 'SVK', '703', 'Eslovaquia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SI', 'SVN', '705', 'Eslovenia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SE', 'SWE', '752', 'Suecia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SZ', 'SWZ', '748', 'Suazilandia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SC', 'SYC', '690', 'Seychelles', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('SY', 'SYR', '760', 'Siria', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TC', 'TCA', '796', 'Islas Turcas y Caicos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TD', 'TCD', '148', 'Chad', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TG', 'TGO', '768', 'Togo', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TH', 'THA', '764', 'Tailandia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TJ', 'TJK', '762', 'Tayikistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TK', 'TKL', '772', 'Tokelau', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TM', 'TKM', '795', 'Turkmenistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TL', 'TLS', '626', 'Timor Oriental', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TO', 'TON', '776', 'Tonga', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TT', 'TTO', '780', 'Trinidad y Tobago', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TN', 'TUN', '788', 'Túnez', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TR', 'TUR', '792', 'Turquía', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TV', 'TUV', '798', 'Tuvalu', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TW', 'TWN', '158', 'República de China', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('TZ', 'TZA', '834', 'Tanzania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('UG', 'UGA', '800', 'Uganda', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('UA', 'UKR', '804', 'Ucrania', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('UM', 'UMI', '581', 'Islas ultramarinas de Estados Unidos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('UY', 'URY', '858', 'Uruguay', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('US', 'USA', '840', 'Estados Unidos', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('UZ', 'UZB', '860', 'Uzbekistán', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VA', 'VAT', '336', 'Ciudad del Vaticano', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VC', 'VCT', '670', 'San Vicente y las Granadinas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VE', 'VEN', '862', 'República Bolivariana de Venezuela', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VG', 'VGB', '092', 'Islas Vírgenes Británicas', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VI', 'VIR', '850', 'Islas Vírgenes Estadounidenses', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VN', 'VNM', '704', 'Vietnam', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('VU', 'VUT', '548', 'Vanuatu', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('WF', 'WLF', '876', 'Wallis y Futuna', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('WS', 'WSM', '882', 'Samoa', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('YE', 'YEM', '887', 'Yemen', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ZA', 'ZAF', '710', 'Sudáfrica', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ZM', 'ZMB', '894', 'Zambia', '', '', 0);

INSERT INTO "adempiere"."c_country2" ("countrycode", "countrycode3", "countryid", "name", "detalle1", "detalle2", "modificado")
VALUES ('ZW', 'ZWE', '716', 'Zimbabue', '', '', 0);

/* Reemplaza el codigo de tres digitos en la tabla c_country*/
update  adempiere.c_country
set countrycode3=(
select adempiere.c_country2.countrycode3 
from  adempiere.c_country2  
where adempiere.c_country.countrycode=adempiere.c_country2.countrycode );
/* Venezuela (339) hasRegion='Y' */
update	adempiere.c_country set hasregion = 'Y' where c_country_id= 339;

/* Elimina la tabla c_country2 */
DROP TABLE "adempiere"."c_country2" ;


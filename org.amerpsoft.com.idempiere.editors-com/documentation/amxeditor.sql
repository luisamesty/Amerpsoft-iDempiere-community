--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

/* Agrega campo countrycode3 codigo de tres digitos internacional */

ALTER TABLE "adempiere"."c_country"
  ADD COLUMN "countrycode3" CHAR(3);

ALTER TABLE "adempiere"."c_country"
  ADD COLUMN "hascommunity" CHAR(1) DEFAULT 'Y';

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

/* CREA TABLA c_community (REGIONES DE PAISES, COMUNIDADES) */

CREATE TABLE "adempiere"."c_community" (
  "c_community_id" NUMERIC(10,0) NOT NULL, 
  "ad_client_id" NUMERIC(10,0) NOT NULL, 
  "ad_org_id" NUMERIC(10,0) NOT NULL, 
  "isactive" CHAR(1) DEFAULT 'Y'::bpchar NOT NULL, 
  "created" TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL, 
  "createdby" NUMERIC(10,0) NOT NULL, 
  "updated" TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL, 
  "updatedby" NUMERIC(10,0) NOT NULL, 
  "name" VARCHAR(60) NOT NULL, 
  "description" VARCHAR(255), 
  "c_country_id" NUMERIC(10,0) NOT NULL, 
  "isdefault" CHAR(1) DEFAULT 'N'::bpchar, 
  CONSTRAINT "c_community_pkey" PRIMARY KEY("c_community_id"), 
  CONSTRAINT "c_community_isactive_check" CHECK (isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar])), 
  CONSTRAINT "c_community_isdefault_check" CHECK (isdefault = ANY (ARRAY['Y'::bpchar, 'N'::bpchar])), 
  CONSTRAINT "c_communityclient" FOREIGN KEY ("ad_client_id")
    REFERENCES "adempiere"."ad_client"("ad_client_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "c_communityorg" FOREIGN KEY ("ad_org_id")
    REFERENCES "adempiere"."ad_org"("ad_org_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "ccountry_cregion" FOREIGN KEY ("c_country_id")
    REFERENCES "adempiere"."c_country"("c_country_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED
) WITHOUT OIDS;

/* Agrega campo c_community_id  a la tabla c_region*/

ALTER TABLE "adempiere"."c_region"
  ADD COLUMN "c_community_id" NUMERIC(10,0);

ALTER TABLE "adempiere"."c_region"
  ALTER COLUMN "c_community_id" SET DEFAULT NULL;
  
/* Agrega restriccion co tabla c_community */
  ALTER TABLE "adempiere"."c_region"
  ADD CONSTRAINT "ccommunity_cregion_fk" FOREIGN KEY ("c_community_id")
    REFERENCES "adempiere"."c_community"("c_community_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE;


CREATE UNIQUE INDEX "c_community_name" ON "adempiere"."c_community"
  USING btree ("c_country_id", "name");
CREATE INDEX "c_region_idx" ON "adempiere"."c_region"
  USING btree ("c_community_id", "c_country_id");  

/*   */    
    
--INICIO DE INCLUSION DE REGIONES (COMMUNITIES) DE VENEZUELA;
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33901,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Capital','Región Capital',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33902,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Central','Región Central',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33903,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Guayanal','Región Guayana',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33904,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Insular','Región Insular',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33905,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Los Andes','Región Los Andes',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33906,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región de los Llanos','Región de los Llanos',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33907,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Nor-Oriental','Región Nor-Oriental',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33908,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Centro-Ocidental','Región Centro-Ocidental',339,'N');
insert into "adempiere"."c_community" (c_community_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault) 
VALUES (33909,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Región Zuliana','Región Zuliana',339,'N');


/* Data for the `adempiere.c_community` table  (Records 1 - 255) */
/* UNA COMMUNIDAD POR CADA PAIS DEL RESTO DEL MUNDO PARA MANTENER CONSISTENCIA */

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100335, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community United States Minor Outlying Islands', 'United States Minor Outlying Islands', 335, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100206, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Hungary', 'Hungary', 206, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100207, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Iceland', 'Iceland', 207, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100252, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Montserrat', 'Montserrat', 252, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100254, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Mozambique', 'Mozambique', 254, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100255, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Myanmar', 'Myanmar', 255, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100257, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Nauru', 'Nauru', 257, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100258, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Nepal', 'Nepal', 258, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100283, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Qatar', 'Qatar', 283, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100284, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Reunion', 'Reunion', 284, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100286, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2010-12-18 12:46:11', 0, 'Community Russian Federation', 'Russian Federation', 286, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100288, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Saint Helena', 'Saint Helena', 288, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100289, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Saint Kitts And Nevis', 'Saint Kitts And Nevis', 289, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100291, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Saint Pierre And Miquelon', 'Saint Pierre And Miquelon', 291, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100292, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Saint Vincent And The Grenadines', 'Saint Vincent And The Grenadines', 292, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100294, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community San Marino', 'San Marino', 294, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100296, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Saudi Arabia', 'Saudi Arabia', 296, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100297, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Senegal', 'Senegal', 297, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100299, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Sierra Leone', 'Sierra Leone', 299, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100300, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Singapore', 'Singapore', 300, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100302, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2008-02-25 13:28:06', 0, 'Community Slovenia', 'Slovenia', 302, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100303, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Solomon Islands', 'Solomon Islands', 303, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100305, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community South Africa', 'South Africa', 305, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100100, 0, 0, 'Y', '1999-12-20 09:55:35', 0, '2000-01-02 00:00:00', 0, 'Community United States', 'United States of America', 100, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100103, 0, 0, 'Y', '2000-11-28 17:23:54', 0, '2000-01-02 00:00:00', 0, 'Community Belgium', NULL, 103, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100105, 0, 0, 'Y', '2000-11-28 17:25:11', 0, '2005-06-27 16:14:58', 0, 'Community Nederland', NULL, 105, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100106, 0, 0, 'Y', '2000-11-28 17:25:42', 0, '2006-04-13 13:29:48', 0, 'Community Spain', NULL, 106, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100107, 0, 0, 'Y', '2000-11-28 17:26:22', 0, '2000-01-02 00:00:00', 0, 'Community Switzerland', NULL, 107, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100108, 0, 0, 'Y', '2000-11-28 17:27:26', 0, '2000-01-02 00:00:00', 0, 'Community Austria', 'Österreich', 108, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100306, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community South Georgia And The South Sandwich Islands', 'South Georgia And The South Sandwich Islands', 306, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100308, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Sri Lanka', 'Sri Lanka', 308, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100309, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Sudan', 'Sudan', 309, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100310, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Suriname', 'Suriname', 310, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100311, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Svalbard And Jan Mayen', 'Svalbard And Jan Mayen', 311, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100312, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Swaziland', 'Swaziland', 312, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100313, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Sweden', 'Sweden', 313, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100315, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Syrian Arab Republic', 'Syrian Arab Republic', 315, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100316, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Taiwan', 'Taiwan', 316, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100317, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Tajikistan', 'Tajikistan', 317, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100318, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Tanzania United Republic Of', 'Tanzania United Republic Of', 318, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100282, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Puerto Rico', 'Puerto Rico', 282, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100157, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Comoros', 'Comoros', 157, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100159, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Congo The Democratic Republic Of The', 'Congo The Democratic Republic Of The', 159, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100164, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cuba', 'Cuba', 164, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100166, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Czech Republic', 'Czech Republic', 166, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100169, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Dominica', 'Dominica', 169, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100171, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Ecuador', 'Ecuador', 171, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100174, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Equatorial Guinea', 'Equatorial Guinea', 174, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100177, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Ethiopia', 'Ethiopia', 177, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100180, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Fiji', 'Fiji', 180, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100183, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community French Guiana', 'French Guiana', 183, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100186, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Gabon', 'Gabon', 186, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100188, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Georgia', 'Georgia', 188, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100192, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Greece', 'Greece', 192, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100194, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Grenada', 'Grenada', 194, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100197, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Guatemala', 'Guatemala', 197, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100200, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Guyana', 'Guyana', 200, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100202, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Heard Island And Mcdonald Islands', 'Heard Island And Mcdonald Islands', 202, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100327, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Turkmenistan', 'Turkmenistan', 327, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100329, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Tuvalu', 'Tuvalu', 329, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100332, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community United Arab Emirates', 'United Arab Emirates', 332, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100337, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Uzbekistan', 'Uzbekistan', 337, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100338, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Vanuatu', 'Vanuatu', 338, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100340, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2005-07-25 10:22:34', 0, 'Community Viet Nam', 'Viet Nam', 340, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100341, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Virgin Islands British', 'Virgin Islands British', 341, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100342, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Virgin Islands U.s.', 'Virgin Islands U.s.', 342, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100343, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Wallis And Futuna', 'Wallis And Futuna', 343, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100344, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Western Sahara', 'Western Sahara', 344, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100346, 0, 0, 'N', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Yugoslavia', 'Yugoslavia', 346, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100348, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Zimbabwe', 'Zimbabwe', 348, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100110, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Afghanistan', 'Afghanistan', 110, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100112, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Algeria', 'Algeria', 112, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100113, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community American Samoa', 'American Samoa', 113, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100115, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Angola', 'Angola', 115, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100117, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Antarctica', 'Antarctica', 117, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100118, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Antigua And Barbuda', 'Antigua And Barbuda', 118, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100120, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Armenia', 'Armenia', 120, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100121, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Aruba', 'Aruba', 121, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100124, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Azerbaijan', 'Azerbaijan', 124, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100125, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bahamas', 'Bahamas', 125, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100127, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bangladesh', 'Bangladesh', 127, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100128, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Barbados', 'Barbados', 128, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100131, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Belize', 'Belize', 131, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100133, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bermuda', 'Bermuda', 133, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100134, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bhutan', 'Bhutan', 134, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100136, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bosnia And Herzegovina', 'Bosnia And Herzegovina', 136, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100138, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bouvet Island', 'Bouvet Island', 138, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100139, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Brazil', 'Brazil', 139, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100141, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Brunei Darussalam', 'Brunei Darussalam', 141, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100142, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bulgaria', 'Bulgaria', 142, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100349, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Serbia', 'Serbia', 349, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100143, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Burkina Faso', 'Burkina Faso', 143, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100144, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Burundi', 'Burundi', 144, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100146, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cameroon', 'Cameroon', 146, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100149, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cayman Islands', 'Cayman Islands', 149, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100150, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Central African Republic', 'Central African Republic', 150, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100152, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Chile', 'Chile', 152, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100154, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Christmas Island', 'Christmas Island', 154, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100261, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community New Caledonia', 'New Caledonia', 261, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100262, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community New Zealand', 'New Zealand', 262, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100263, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Nicaragua', 'Nicaragua', 263, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100264, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Niger', 'Niger', 264, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100265, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Nigeria', 'Nigeria', 265, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100266, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Niue', 'Niue', 266, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100267, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Norfolk Island', 'Norfolk Island', 267, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100268, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Northern Mariana Islands', 'Northern Mariana Islands', 268, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100270, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Oman', 'Oman', 270, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100272, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Palau', 'Palau', 272, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100208, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community India', 'India', 208, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100210, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Iran Islamic Republic Of', 'Iran Islamic Republic Of', 210, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100212, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Ireland', 'Ireland', 212, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100213, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Israel', 'Israel', 213, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100215, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Jamaica', 'Jamaica', 215, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100217, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Jordan', 'Jordan', 217, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100218, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Kazakhstan', 'Kazakhstan', 218, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100219, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Kenya', 'Kenya', 219, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100221, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Korea Democratic People''s Republic Of', 'Korea Democratic People''s Republic Of', 221, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100223, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Kuwait', 'Kuwait', 223, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100224, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Kyrgyzstan', 'Kyrgyzstan', 224, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100226, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Latvia', 'Latvia', 226, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100227, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Lebanon', 'Lebanon', 227, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100228, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Lesotho', 'Lesotho', 228, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100230, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Libyan Arab Jamahiriya', 'Libyan Arab Jamahiriya', 230, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100231, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Liechtenstein', 'Liechtenstein', 231, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100275, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Papua New Guinea', 'Papua New Guinea', 275, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100276, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Paraguay', 'Paraguay', 276, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100277, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Peru', 'Peru', 277, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100278, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Philippines', 'Philippines', 278, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100279, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Pitcairn', 'Pitcairn', 279, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100280, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Poland', 'Poland', 280, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100281, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Portugal', 'Portugal', 281, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100155, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cocos (keeling) Islands', 'Cocos (keeling) Islands', 155, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100156, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2009-09-17 21:57:21', 0, 'Community Colombia', 'Colombia', 156, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100158, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Congo', 'Congo', 158, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100160, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cook Islands', 'Cook Islands', 160, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100161, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Costa Rica', 'Costa Rica', 161, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100162, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cote D''ivoire', 'Cote D''ivoire', 162, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100163, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Croatia', 'Croatia', 163, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100165, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2008-02-25 13:28:26', 0, 'Community Cyprus', 'Cyprus', 165, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100167, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Denmark', 'Denmark', 167, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100168, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Djibouti', 'Djibouti', 168, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100170, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Dominican Republic', 'Dominican Republic', 170, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100172, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Egypt', 'Egypt', 172, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100173, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community El Salvador', 'El Salvador', 173, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100175, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Eritrea', 'Eritrea', 175, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100176, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Estonia', 'Estonia', 176, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100178, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Falkland Islands (malvinas)', 'Falkland Islands (malvinas)', 178, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100179, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Faroe Islands', 'Faroe Islands', 179, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100181, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Finland', 'Finland', 181, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100184, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community French Polynesia', 'French Polynesia', 184, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100185, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community French Southern Territories', 'French Southern Territories', 185, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100187, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Gambia', 'Gambia', 187, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100190, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Ghana', 'Ghana', 190, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100191, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Gibraltar', 'Gibraltar', 191, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100193, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Greenland', 'Greenland', 193, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100195, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Guadeloupe', 'Guadeloupe', 195, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100196, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Guam', 'Guam', 196, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100198, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Guinea', 'Guinea', 198, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100199, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Guinea-bissau', 'Guinea-bissau', 199, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100201, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Haiti', 'Haiti', 201, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100233, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Luxembourg', 'Luxembourg', 233, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100234, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Macao', 'Macao', 234, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100236, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Madagascar', 'Madagascar', 236, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100237, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Malawi', 'Malawi', 237, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100240, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Mali', 'Mali', 240, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100241, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2008-02-25 13:28:46', 0, 'Community Malta', 'Malta', 241, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100243, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Martinique', 'Martinique', 243, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100244, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Mauritania', 'Mauritania', 244, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100246, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Mayotte', 'Mayotte', 246, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100248, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Micronesia Federated States Of', 'Micronesia Federated States Of', 248, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100249, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Moldova Republic Of', 'Moldova Republic Of', 249, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100273, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Palestinian Territory Occupied', 'Palestinian Territory Occupied', 273, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100274, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Panama', 'Panama', 274, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100336, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Uruguay', 'Uruguay', 336, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100251, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Mongolia', 'Mongolia', 251, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100253, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Morocco', 'Morocco', 253, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100256, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Namibia', 'Namibia', 256, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100260, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Netherlands Antilles', 'Netherlands Antilles', 260, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100285, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Romania', 'Romania', 285, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100287, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Rwanda', 'Rwanda', 287, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100290, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Saint Lucia', 'Saint Lucia', 290, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100293, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Samoa', 'Samoa', 293, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100295, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Sao Tome And Principe', 'Sao Tome And Principe', 295, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100298, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Seychelles', 'Seychelles', 298, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100301, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Slovakia', 'Slovakia', 301, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100304, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Somalia', 'Somalia', 304, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100109, 0, 0, 'Y', '2003-01-08 13:06:10', 0, '2000-01-02 00:00:00', 0, 'Community Canada', NULL, 109, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100102, 0, 0, 'Y', '2000-11-28 17:23:06', 0, '2000-01-02 00:00:00', 0, 'Community France', 'France', 102, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100319, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Thailand', 'Thailand', 319, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100345, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Yemen', 'Yemen', 345, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100347, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Zambia', 'Zambia', 347, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100111, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Albania', 'Albania', 111, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100116, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Anguilla', 'Anguilla', 116, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100119, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2005-05-20 17:16:37', 0, 'Community Argentina', 'Argentina', 119, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100122, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Australia', 'Australia', 122, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100126, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bahrain', 'Bahrain', 126, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100129, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Belarus', 'Belarus', 129, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100132, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Benin', 'Benin', 132, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100135, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Bolivia', 'Bolivia', 135, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100137, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Botswana', 'Botswana', 137, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100140, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community British Indian Ocean Territory', 'British Indian Ocean Territory', 140, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100350, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Montenegro', 'Montenegro', 350, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100145, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cambodia', 'Cambodia', 145, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100148, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Cape Verde', 'Cape Verde', 148, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100151, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Chad', 'Chad', 151, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100153, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community China', 'China', 153, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100269, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Norway', 'Norway', 269, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100271, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Pakistan', 'Pakistan', 271, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100209, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2010-03-18 19:08:42', 0, 'Community Indonesia', 'Indonesia', 209, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100211, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Iraq', 'Iraq', 211, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100214, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Italy', 'Italy', 214, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100216, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2006-02-23 16:16:56', 0, 'Community Japan', 'Japan', 216, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100220, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Kiribati', 'Kiribati', 220, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100203, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Holy See (vatican City State)', 'Holy See (vatican City State)', 203, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100204, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Honduras', 'Honduras', 204, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100205, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Hong Kong', 'Hong Kong', 205, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100320, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Timor-leste', 'Timor-leste', 320, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100321, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Togo', 'Togo', 321, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100322, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Tokelau', 'Tokelau', 322, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100323, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Tonga', 'Tonga', 323, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100324, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Trinidad And Tobago', 'Trinidad And Tobago', 324, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100325, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Tunisia', 'Tunisia', 325, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100326, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Turkey', 'Turkey', 326, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100328, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Turks And Caicos Islands', 'Turks And Caicos Islands', 328, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100330, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Uganda', 'Uganda', 330, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100331, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Ukraine', 'Ukraine', 331, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100333, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community United Kingdom', 'United Kingdom', 333, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100222, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Korea Republic Of', 'Korea Republic Of', 222, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100225, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Lao People''s Democratic Republic', 'Lao People''s Democratic Republic', 225, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100229, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Liberia', 'Liberia', 229, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100232, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Lithuania', 'Lithuania', 232, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100235, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Macedonia Former Yugoslav Republic Of', 'Macedonia Former Yugoslav Republic Of', 235, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100239, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Maldives', 'Maldives', 239, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100242, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Marshall Islands', 'Marshall Islands', 242, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100245, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Mauritius', 'Mauritius', 245, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100247, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2005-05-07 20:51:55', 0, 'Community Mexico', 'Mexico', 247, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100250, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2000-01-02 00:00:00', 0, 'Community Monaco', 'Monaco', 250, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (150000, 0, 0, 'Y', '2010-03-18 19:14:35', 100, '2010-03-18 19:15:45', 0, 'Community Åland Islands', 'Åland Islands', 50000, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (150001, 0, 0, 'Y', '2010-03-18 19:16:02', 100, '2010-03-18 19:16:02', 0, 'Community Saint Barthélemy', 'Saint Barthélemy', 50001, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (150002, 0, 0, 'Y', '2010-03-18 19:16:25', 100, '2010-03-18 19:16:25', 0, 'Community Guernsey', NULL, 50002, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (150003, 0, 0, 'Y', '2010-03-18 19:16:46', 100, '2010-03-18 19:16:46', 0, 'Community Isle of Man', 'Isle of Man', 50003, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (150004, 0, 0, 'Y', '2010-03-18 19:17:04', 100, '2010-03-18 19:17:04', 0, 'Community Jersey', 'Jersey', 50004, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (150005, 0, 0, 'Y', '2010-03-18 19:17:19', 100, '2010-03-18 19:17:19', 0, 'Community Saint Martin', 'Saint Martin', 50005, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100238, 0, 0, 'Y', '2003-03-09 00:00:00', 0, '2010-03-18 19:17:36', 0, 'Community Malaysia', 'Malaysia', 238, 'Y');

INSERT INTO "adempiere"."c_community" ("c_community_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "name", "description", "c_country_id", "isdefault")
VALUES (100101, 0, 0, 'Y', '1999-12-20 09:56:40', 0, '2010-04-19 17:30:18', 0, 'Community Germany - Deutschland', NULL, 101, 'Y');

/* Reemplaza el codigo de la region por defecto de todos los paise
   Menos Venezuela 339 */

update  adempiere.c_region
set c_community_id=(
select adempiere.c_community.c_community_id 
from  adempiere.c_community 
where c_region.c_country_id=c_community.c_country_id and c_community.c_country_id <> 339);


/*   */    
--INICIO DE INCLUSION DE ESTADOS DE VENEZUELA;
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100033,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Distrito Capital','Distrito Capital',339,'N',33901);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100034,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Amazonas','Amazonas',339,'N',33903);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100035,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Anzoategui','Anzoategui',339,'N',33907);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100036,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Apure','Apure',339,'N',33906);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100037,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Aragua','Aragua',339,'N',33902);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100038,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barinas','Barinas',339,'N',33906);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100039,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Bolivar','Bolivar',339,'N',33903);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100040,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Carabobo','Carabobo',339,'N',33902);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100041,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cojedes','Cojedes',339,'N',33906);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id)
values (100042,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Delta Amacuro','Delta Amacuro',339,'N',33903);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100043,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Falcon', 'Falcon',339,'N',33908);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100044,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guarico','Guarico',339,'N',33902);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100045,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Lara','Lara',339,'N',33908);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100046,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Merida','Merida',339,'N',33905);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100047,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Miranda','Miranda',339,'N',33902);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100048,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Monagas','Monagas',339,'N',33907);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100049,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Nueva Esparta','Nueva Esparta',339,'N',33907);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100050,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Portuguesa','Portuguesa',339,'N',33906);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100051,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sucre','Sucre',339,'N',33907);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100052,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tachira','Tachira',339,'N',33905);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100053,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Trujillo','Trujillo',339,'N',33905);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100054,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Yaracuy','Yaracuy',339,'N',33908);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100055,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Zulia','Zulia',339,'N',33909);
insert into "adempiere"."c_region" (c_region_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, description, c_country_id, isdefault, c_community_id) 
values (100056,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Vargas','Vargas',339,'N',33901);
--FIN DE INCLUSION DE ESTADOS DE VENEZUELA;


--- TABLA DE MUNICIPIOS (c_municipality)
CREATE TABLE "adempiere"."c_municipality" (
  "c_municipality_id" NUMERIC(10,0) NOT NULL, 
  "ad_client_id" NUMERIC(10,0) NOT NULL, 
  "ad_org_id" NUMERIC(10,0) NOT NULL, 
  "isactive" CHAR(1) DEFAULT 'Y'::bpchar NOT NULL, 
  "created" TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL, 
  "createdby" NUMERIC(10,0) NOT NULL, 
  "updated" TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL, 
  "updatedby" NUMERIC(10,0) NOT NULL, 
  "name" VARCHAR(60) NOT NULL, 
  "capital" VARCHAR(60) NOT NULL, 
  "c_country_id" NUMERIC(10,0), 
  "c_region_id" NUMERIC(10,0), 
  "isdefault" CHAR(1) DEFAULT 'Y'::bpchar, 
  CONSTRAINT "c_municipality_pkey" PRIMARY KEY("c_municipality_id"), 
  CONSTRAINT "c_municipality_isactive_check" CHECK (isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar])), 
  CONSTRAINT "c_municipalityclient" FOREIGN KEY ("ad_client_id")
    REFERENCES "adempiere"."ad_client"("ad_client_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "c_municipalityorg" FOREIGN KEY ("ad_org_id")
    REFERENCES "adempiere"."ad_org"("ad_org_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "ccountry_cmunicipality" FOREIGN KEY ("c_country_id")
    REFERENCES "adempiere"."c_country"("c_country_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "cregion_cmunicipality" FOREIGN KEY ("c_region_id")
    REFERENCES "adempiere"."c_region"("c_region_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED
) WITHOUT OIDS;



/* Data for the `MUNICIPIOS (c_municipality)  */


INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10101, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100033, 'Libertador', 'Caracas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11501, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Acevedo', 'Caucagua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11502, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Andrés Bello', 'San José de Barlovento');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11503, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Baruta', 'Baruta');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11504, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Brión', 'Higuerote');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11505, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Buroz', 'Mamporal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11506, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Carrizal', 'Carrizal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11507, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Chacao', 'Chacao');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11508, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Cristóbal Rojas', 'Charallave');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11509, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'El Hatillo', 'Santa Rosalía de Palermo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11510, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Guaicaipuro', 'Los Teques');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11511, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Independencia', 'Santa Teresa del Tuy');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11512, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Lander', 'Ocumare del Tuy');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11513, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Los Salias', 'San Antonio de los Altos');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11514, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Páez', 'Río Chico');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11515, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Paz Castillo', 'Santa Lucía');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11516, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Pedro Gual', 'Cúpira');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11517, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Plaza', 'Guarenas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11518, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Simón Bolívar', 'San Francisco de Yare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11519, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Sucre', 'Petare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11520, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Urdaneta', 'Cúa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11521, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100047, 'Zamora', 'Guatire');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12401, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100056, 'Vargas', 'La Guaira');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10501, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Bolívar', 'San Mateo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10502, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Camatagua', 'Camatagua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10503, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Francisco Linares Alcántara', 'Santa Rita');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10504, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Girardot', 'Maracay');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10505, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'José Angel Lamas', 'Santa Cruz de Aragua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10506, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'José Félix Ribas', 'La Victoria');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10507, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'José Rafael Revenga', 'El Consejo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10508, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Libertador', 'Palo Negro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10509, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Mario Briceño Iragorry', 'El Limón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10510, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Ocumare de la Costa de Oro', 'Ocumare de la Costa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10511, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'San Casimiro', 'San Casimiro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10512, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'San Sebastián', 'San Sebastián de Los Reyes (Venezuela');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10513, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Santiago Mariño', 'Turmero');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10514, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Santos Michelena', 'Las Tejerías');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10515, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Sucre', 'Cagua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10516, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Tovar', 'Colonia Tovar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10517, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Urdaneta', 'Barbacoas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10518, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100037, 'Zamora', 'Villa de Cura');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10801, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Bejuma', 'Bejuma');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10802, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Carlos Arvelo', 'Güigüe');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10803, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Diego Ibarra', 'Mariara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10804, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Guacara', 'Guacara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10805, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Juan José Mora', 'Morón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10806, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Libertador', 'Tocuyito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10807, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Los Guayos', 'Los Guayos');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10808, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Miranda', 'Miranda');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10809, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Montalbán', 'Montalbán');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10810, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Naguanagua', 'Naguanagua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10811, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Puerto Cabello', 'Puerto Cabello');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10812, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'San Diego', 'San Diego');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10813, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'San Joaquín', 'San Joaquín');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10814, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100040, 'Valencia', 'Valencia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10901, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Anzoátegui', 'Cojedes');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10902, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Falcón', 'Tinaquillo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10903, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Girardot', 'El Baúl');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10904, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Lima Blanco', 'Macapo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10905, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Pao de San Juan Bautista', 'El Pao');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10906, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Ricaurte', 'Libertad');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10907, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Rómulo Gallegos', 'Las Vegas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10908, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'San Carlos', 'San Carlos');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10909, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100041, 'Tinaco', 'Tinaco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10201, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Alto Orinoco', 'La Esmeralda');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10202, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Atabapo', 'San Fernando de Atabapo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10203, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Atures', 'Puerto Ayacucho');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10204, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Autana', 'Isla Ratón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10205, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Manapiare', 'San Juan de Manapiare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10206, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Maroa', 'Maroa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10207, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100034, 'Río Negro', 'San Carlos de Río Negro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10701, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Caroní', 'Ciudad Guayana');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10702, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Cedeño', 'Caicara del Orinoco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10703, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'El Callao', 'El Callao');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10704, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Gran Sabana', 'Santa Elena de Uairén');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10705, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Heres', 'Ciudad Bolívar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10706, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Piar', 'Upata');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10707, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Raúl Leoni', 'Ciudad Piar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10708, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Roscio', 'Guasipati');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10709, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Sifontes', 'El Dorado');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10710, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Sucre', 'Maripa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10711, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100039, 'Padre Pedro Chien', 'El Palmar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11001, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100042, 'Antonio Díaz', 'Curiapo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11002, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100042, 'Casacoima', 'Sierra Imataca');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11003, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100042, 'Pedernales', 'Pedernales');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11004, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100042, 'Tucupita', 'Tucupita');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11701, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Antolín del Campo', 'La Plaza de Paraguachí');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11702, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Arismendi', 'La Asunción');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11703, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Díaz', 'San Juan Bautista');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11704, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'García', 'El Valle del Espíritu Santo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11705, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Gómez', 'Santa Ana');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11706, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Maneiro', 'Pampatar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11707, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Marcano', 'Juan Griego');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11708, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Mariño', 'Porlamar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11709, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Península de Macanao', 'Boca de Río');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11710, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Tubores', 'Punta de Piedras');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11711, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100049, 'Villalba', 'San Pedro de Coche');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11401, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Alberto Adriani', 'El Vigía');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11402, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Andrés Bello', 'La Azulita');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11403, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Antonio Pinto Salinas', 'Santa Cruz de Mora');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11404, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Aricagua', 'Aricagua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11405, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Arzobispo Chacón', 'Canaguá');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11406, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Campo Elías', 'Ejido');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11407, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Caracciolo Parra Olmedo', 'Tucaní');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11408, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Cardenal Quintero', 'Santo Domingo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11409, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Guaraque', 'Guaraque');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11410, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Julio César Salas', 'Arapuey');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11411, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Justo Briceño', 'Torondoy');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11412, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Libertador', 'Mérida');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11413, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Miranda', 'Timotes');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11414, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Obispo Ramos de Lora', 'Santa Elena de Arenales');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11415, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Padre Noguera', 'Santa María de Caparo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11416, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Pueblo Llano', 'Pueblo Llano');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11417, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Rangel', 'Mucuchíes');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11418, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Rivas Dávila', 'Bailadores');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11419, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Santos Marquina', 'Tabay');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11420, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Sucre', 'Lagunillas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11421, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Tovar', 'Tovar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11422, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Tulio Febres Cordero', 'Nueva Bolivia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11423, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100046, 'Zea', 'Zea');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12001, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Andrés Bello', 'Cordero');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12002, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Antonio Rómulo Costa', 'Las Mesas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12003, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Ayacucho', 'Colón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12004, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Bolívar', 'San Antonio del Táchira');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12005, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Cárdenas', 'Táriba');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12006, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Córdoba', 'Santa Ana de Táchira');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12007, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Fernández Feo', 'San Rafael del Piñal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12008, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Francisco de Miranda', 'San José de Bolívar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12009, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'García de Hevia', 'La Fría');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12010, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Guásimos', 'Palmira');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12011, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Independencia', 'Capacho Nuevo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12012, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Jáuregui', 'La Grita');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12013, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'José María Vargas', 'El Cobre');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12014, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Junín', 'Rubio');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12015, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Libertad', 'Capacho Viejo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12016, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Libertador', 'Abejales');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12017, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Lobatera', 'Lobatera');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12018, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Michelena', 'Michelena');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12019, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Panamericano', 'Coloncito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12020, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Pedro María Ureña', 'Ureña');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12021, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Rafael Urdaneta', 'Delicias');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12022, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Samuel Darío Maldonado', 'La Tendida');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12023, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'San Cristóbal', 'San Cristóbal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12024, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Seboruco', 'Seboruco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12025, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Simón Rodríguez', 'San Simón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12026, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Sucre', 'Queniquea');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12027, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Torbes', 'San Josecito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12028, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'Uribante', 'Pregonero');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12029, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100052, 'San Judas Tadeo', 'Umuquena');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12101, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Andrés Bello', 'Santa Isabel');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12102, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Boconó', 'Boconó');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12103, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Bolívar', 'Sabana Grande');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12104, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Candelaria', 'Chejendé');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12105, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Carache', 'Carache');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12106, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Escuque', 'Escuque');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12107, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'José Felipe Márquez Cañizalez', 'El Paradero');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12108, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Juan Vicente Campos Elías', 'Campo Elías');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12109, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'La Ceiba', 'Santa Apolonia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12110, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Miranda', 'El Dividive');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12111, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Monte Carmelo', 'Monte Carmelo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12112, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Motatán', 'Motatán');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12113, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Pampán', 'Pampán');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12114, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Pampanito', 'Pampanito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12115, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Rafael Rangel', 'Betijoque');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12116, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'San Rafael de Carvajal', 'Carvajal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12117, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Sucre', 'Sabana de Mendoza');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12118, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Trujillo', 'Trujillo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12119, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Urdaneta', 'La Quebrada');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12120, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100053, 'Valera', 'Valera');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10401, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'Achaguas', 'Achaguas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10402, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'Biruaca', 'Biruaca');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10403, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'Muñoz', 'Bruzual');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10404, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'Páez', 'Guasdualito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10405, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'Pedro Camejo', 'San Juan de Payara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10406, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'Rómulo Gallegos', 'Elorza');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10407, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100036, 'San Fernando', 'San Fernando de Apure');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10601, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Alberto Arvelo Torrealba', 'Sabaneta');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10602, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Andrés Eloy Blanco', 'El Cantón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10603, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Antonio José de Sucre', 'Socopó');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10604, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Arismendi', 'Arismendi');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10605, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Barinas', 'Barinas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10606, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Bolívar', 'Barinitas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10607, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Cruz Paredes', 'Barrancas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10608, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Ezequiel Zamora', 'Santa Bárbara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10609, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Obispos', 'Obispos');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10610, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Pedraza', 'Ciudad Bolivia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10611, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Rojas', 'Libertad');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10612, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100038, 'Sosa', 'Ciudad de Nutrias');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11201, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Camaguán', 'Camaguán');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11202, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Chaguaramas', 'Chaguaramas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11203, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'El Socorro', 'El Socorro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11204, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Sebastian Francisco de Miranda', 'Calabozo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11205, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'José Félix Ribas', 'Tucupido');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11206, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'José Tadeo Monagas', 'Altagracia de Orituco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11207, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Juan Germán Roscio', 'San Juan de Los Morros');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11208, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Julián Mellado', 'El Sombrero');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11209, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Las Mercedes', 'Las Mercedes');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11210, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Leonardo Infante', 'Valle de La Pascua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11211, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Pedro Zaraza', 'Zaraza');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11212, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Ortiz', 'Ortiz');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11213, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'San Gerónimo de Guayabal', 'Guayabal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11214, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'San José de Guaribe', 'San José de Guaribe');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11215, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100044, 'Santa María de Ipire', 'Santa María de Ipire');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11801, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Agua Blanca', 'Agua Blanca');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11802, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Araure', 'Araure');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11803, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Esteller', 'Píritu');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11804, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Guanare', 'Guanare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11805, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Guanarito', 'Guanarito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11806, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Monseñor José Vicente de Unda', 'Chabasquén de Unda');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11807, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Ospino', 'Ospino');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11808, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Páez', 'Acarigua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11809, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Papelón', 'Papelón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11810, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'San Genaro de Boconoíto', 'Boconoíto');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11811, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'San Rafael de Onoto', 'San Rafael de Onoto');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11812, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Santa Rosalía', 'El Playón');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11813, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Sucre', 'Biscucuy');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11814, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100050, 'Turén', 'Villa Bruzual');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10301, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Anaco', 'Anaco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10302, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Aragua', 'Aragua de Barcelona');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10303, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Bolívar', 'Barcelona');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10304, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Bruzual', 'Clarines');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10305, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Cajigal', 'Onoto');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10306, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Carvajal', 'Valle de Guanape');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10307, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Diego Bautista Urbaneja', 'Lechería');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10308, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Freites', 'Cantaura');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10309, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Guanipa', 'San José de Guanipa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10310, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Guanta', 'Guanta');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10311, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Independencia', 'Soledad');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10312, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Libertad', 'San Mateo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10313, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'McGregor', 'El Chaparro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10314, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Miranda', 'Pariaguán');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10315, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Monagas', 'San Diego de Cabrutica');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10316, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Peñalver', 'Puerto Píritu');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10317, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Píritu', 'Píritu');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10318, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'San Juan de Capistrano', 'Boca de Uchire');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10319, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Santa Ana', 'Santa Ana');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10320, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Simón Rodriguez', 'El Tigre');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (10321, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100035, 'Sotillo', 'Puerto La Cruz');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11601, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Acosta', 'San Antonio de Capayacuar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11602, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Aguasay', 'Aguasay');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11603, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Bolívar', 'Caripito');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11604, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Caripe', 'Caripe');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11605, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Cedeño', 'Caicara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11606, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Ezequiel Zamora', 'Punta de Mata');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11607, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Libertador', 'Temblador');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11608, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Maturín', 'Maturín');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11609, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Piar', 'Aragua de Maturín');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11610, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Punceres', 'Quiriquire');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11611, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Santa Bárbara', 'Santa Bárbara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11612, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Sotillo', 'Barrancas del Orinco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11613, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100048, 'Uracoa', 'Uracoa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11901, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Andrés Eloy Blanco', 'Casanay');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11902, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Andrés Mata', 'San José de Aerocuar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11903, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Arismendi', 'Río Caribe');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11904, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Benítez', 'El Pilar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11905, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Bermúdez', 'Carúpano');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11906, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Bolívar', 'Marigüitar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11907, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Cajigal', 'Yaguaraparo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11908, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Cruz Salmerón Acosta', 'Araya');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11909, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Libertador', 'Tunapuy');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11910, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Mariño', 'Irapa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11911, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Mejía', 'San Antonio del Golfo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11912, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Montes', 'Cumanacoa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11913, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Ribero', 'Cariaco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11914, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Sucre', 'Cumaná');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11915, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100051, 'Valdez', 'Güiria');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11101, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Acosta', 'San Juan de los Cayos');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11102, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Bolívar', 'San Luis');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11103, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Buchivacoa', 'Capatárida');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11104, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Cacique Manaure', 'Yaracal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11105, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Carirubana', 'Punto Fijo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11106, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Colina', 'La Vela de Coro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11107, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Dabajuro', 'Dabajuro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11108, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Democracia', 'Pedregal');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11109, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Falcón', 'Pueblo Nuevo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11110, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Federación', 'Churuguara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11111, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Jacura', 'Jacura');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11112, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Los Taques', 'Santa Cruz de Los Taques');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11113, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Mauroa', 'Mene de Mauroa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11114, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Miranda', 'Santa Ana de Coro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11115, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Monseñor Iturriza', 'Chichiriviche');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11116, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Palmasola', 'Palmasola');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11117, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Petit', 'Cabure');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11118, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Píritu', 'Píritu');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11119, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'San Francisco', 'Mirimire');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11120, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Silva', 'Tucacas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11121, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Sucre', 'La Cruz de Taratara');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11122, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Tocópero', 'Tocópero');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11123, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Unión', 'Santa Cruz de Bucaral');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11124, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Urumaco', 'Urumaco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11125, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100043, 'Zamora', 'Puerto Cumarebo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11301, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Andrés Eloy Blanco', 'Sanare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11302, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Crespo', 'Duaca');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11303, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Iribarren', 'Barquisimeto');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11304, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Jiménez', 'Quibor');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11305, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Morán', 'El Tocuyo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11306, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Palavecino', 'Cabudare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11307, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Simón Planas', 'Sarare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11308, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Torres', 'Carora');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (11309, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100045, 'Urdaneta', 'Siquisique');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12201, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Arístides Bastidas', 'San Pablo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12202, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Bolívar', 'Aroa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12203, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Bruzual', 'Chivacoa');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12204, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Cocorote', 'Cocorote');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12205, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Independencia', 'Independencia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12206, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'José Antonio Páez', 'Sabana de Parra');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12207, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'La Trinidad', 'Boraure');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12208, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Manuel Monge', 'Yumare');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12209, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Nirgua', 'Nirgua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12210, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Peña', 'Yaritagua');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12211, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'San Felipe', 'San Felipe');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12212, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Sucre', 'Guama');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12213, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Urachiche', 'Urachiche');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12214, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100054, 'Veroes', 'Farriar');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12301, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Almirante Padilla', 'El Toro');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12302, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Baralt', 'San Timoteo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12303, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Cabimas', 'Cabimas');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12304, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Catatumbo', 'Encontrados');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12305, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Colón', 'San Carlos del Zulia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12306, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Francisco Javier Pulgar', 'Pueblo Nuevo-El Chivo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12307, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Jesús Enrique Losada', 'La Concepción');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12308, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Jesús María Semprún', 'Casigua El Cubo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12309, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'La Cañada de Urdaneta', 'Concepción');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12310, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Lagunillas', 'Ciudad Ojeda');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12311, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Machiques de Perijá', 'Machiques');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12312, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Mara', 'San Rafael del Moján');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12313, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Maracaibo', 'Maracaibo');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12314, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Miranda', 'Los Puertos de Altagracia');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12315, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Páez', 'Sinamaica');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12316, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Rosario de Perijá', 'La Villa del Rosario');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12317, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'San Francisco', 'San Francisco');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12318, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Santa Rita', 'Santa Rita');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12319, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Simón Bolívar', 'Tía Juana');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12320, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Sucre', 'Bobures');

INSERT INTO "adempiere"."c_municipality"
  ("c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "isdefault", "c_country_id", "c_region_id", "name", "capital")
VALUES (12321, 0, 0, 'Y', '2012-09-02', 0, '2012-09-02', 0, 'N',339, 100055, 'Valmore Rodríguez', 'Bachaquero');

--- FIN DE MUNICIPIOS (c_municipality)


-- TABLA DE PARROQUIAS (c_parish)
CREATE TABLE "adempiere"."c_parish" (
  "c_parish_id" NUMERIC(10,0) NOT NULL, 
  "ad_client_id" NUMERIC(10,0) NOT NULL, 
  "ad_org_id" NUMERIC(10,0) NOT NULL, 
  "isactive" CHAR(1) DEFAULT 'Y'::bpchar NOT NULL, 
  "created" TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL, 
  "createdby" NUMERIC(10,0) NOT NULL, 
  "updated" TIMESTAMP WITHOUT TIME ZONE DEFAULT now() NOT NULL, 
  "updatedby" NUMERIC(10,0) NOT NULL, 
  "name" VARCHAR(60) NOT NULL, 
  "c_country_id" NUMERIC(10,0) NOT NULL, 
  "c_region_id" NUMERIC(10,0) NOT NULL, 
  "c_municipality_id" NUMERIC(10,0) NOT NULL, 
  "isdefault" CHAR(1) DEFAULT 'N'::bpchar, 
  CONSTRAINT "c_parish_pkey" PRIMARY KEY("c_country_id", "c_region_id", "c_municipality_id", "c_parish_id"), 
  CONSTRAINT "c_parish_isactive_check" CHECK (isactive = ANY (ARRAY['Y'::bpchar, 'N'::bpchar])), 
  CONSTRAINT "c_parishclient" FOREIGN KEY ("ad_client_id")
    REFERENCES "adempiere"."ad_client"("ad_client_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "c_parishorg" FOREIGN KEY ("ad_org_id")
    REFERENCES "adempiere"."ad_org"("ad_org_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "ccountry_cparish" FOREIGN KEY ("c_country_id")
    REFERENCES "adempiere"."c_country"("c_country_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "cmunicipality_cparish" FOREIGN KEY ("c_municipality_id")
    REFERENCES "adempiere"."c_municipality"("c_municipality_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED, 
  CONSTRAINT "cregion_cparish" FOREIGN KEY ("c_region_id")
    REFERENCES "adempiere"."c_region"("c_region_id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    DEFERRABLE
    INITIALLY DEFERRED
) WITHOUT OIDS;





/* Data for the `adempiere.c_parish` table  (Records 1 - 334) */
/********************************************************/
/********************************************************/
/* DISTRITO CAPITAL 1000033								*/
/********************************************************/
/********************************************************/

/********************************************/
/* DISTRITO CAPITAL 1000033					*/
/********************************************/
/* MUNICIPIO LIBERTADOR 1000101 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(010101,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', '23 DE ENERO'),
(010102,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'ALTAGRACIA'),
(010103,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'ANTIMANO'),
(010104,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'CANDELARIA'),
(010105,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'CARICUAO'),
(010106,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'CATEDRAL'),
(010107,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'COCHE'),
(010108,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'EL JUNQUITO'),
(010109,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'EL PARAISO'),
(010110,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'EL RECREO'),
(010111,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'EL VALLE'),
(010112,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'LA PASTORA'),
(010113,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'LA VEGA'),
(010114,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'MACARAO'),
(010115,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SAN AGUSTIN'),
(010116,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SAN BERNARDINO'),
(010117,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SAN JOSE'),
(010118,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SAN JUAN'),
(010119,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SAN PEDRO'),
(010120,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SANTA ROSALIA'),
(010121,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SANTA TERESA'),
(010122,10101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100033, 'N', 'SUCRE');

/********************************************************/
/********************************************************/
/* ESTADO MIRANDA  1000047 								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO ACEVEDO 1001501 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150101,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'ARAGUITA'),
(150102,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'AREVALO GONZALEZ'),
(150103,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CAPAYA'),
(150104,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CAUCAGUA'),
(150105,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'EL CAFE'),
(150106,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'MARIZAPA'),
(150107,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'PANAQUIRE'),
(150108,11501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'RIBAS');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO ANDRES BELLO 1001502 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(150201,11502, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CUMBO'),
(150202,11502, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SAN JOSE DE BARLOVENTO');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO BARUTA 1001503 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150301,11503, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'BARUTA'),
(150302,11503, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'EL CAFETAL'),
(150303,11503, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'LAS MINAS DE BARUTA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO BRION 1001504  				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150401, 11504, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CURIEPE'),
(150402, 11504, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'HIGUEROTE'),
(150403, 11504, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'TACARIGUA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO BUROZ 1001505 					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150501, 11505, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'MAMPORAL');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO CARRIZAL 1001506 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150601, 11506, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CARRIZAL');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO CHACAO 1001507 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150701, 11507, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CHACAO');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO CRISTOBAL ROJAS 1001508		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(150801, 11508, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CHARALLAVE'),
(150802, 11508, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'LAS BRISAS');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO EL HATILLO 1001509				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(150901, 11509, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'EL HATILLO');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO GUAICAIPURO 1001510 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151001, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'ALTAGRACIA DE LA MONTAÑA'),
(151002, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CECILIO ACOSTA'),
(151003, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'EL JARILLO'),
(151004, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'LOS TEQUES'),
(151005, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'PARACOTOS'),
(151006, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SAN PEDRO'),
(151007, 11510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'TACATA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO INDEPENDENCIA 1001511 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151101, 11511, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'EL CARTANAL'),
(151102, 11511, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'STA TERESA DEL TUY');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO LANDER 1001512 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151201, 11512, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'LA DEMOCRACIA'),
(151202, 11512, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'OCUMARE DEL TUY'),
(151203, 11512, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SANTA BARBARA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO LOS SALIAS 1001513 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151301, 11513, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SAN ANTONIO LOS ALTOS');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO PAEZ 1001514	 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151401, 11514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'EL GUAPO'),
(151402, 11514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'PAPARO'),
(151403, 11514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'RIO CHICO'),
(151404, 11514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SAN FERNANDO DEL GUAPO'),
(151405, 11514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'TACARIGUA DE LA LAGUNA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO PAZ CASTILLO 1001515			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151501, 11515, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SANTA LUCIA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO PEDRO GUAL 1001516				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151601, 11516, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CUPIRA'),
(151602, 11516, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'MACHURUCUTO');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO PLAZA 1001517	 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151701, 11517, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'GUARENAS');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO SIMON BOLIVAR 1001518			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151801, 11518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SAN ANTONIO DE YARE'),
(151802, 11518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'SAN FRANCISCO DE YARE');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO SUCRE 1001519	 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(151901, 11519, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CAUCAGUITA'),
(151902, 11519, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'FILAS DE MARICHES'),
(151903, 11519, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'LA DOLORITA'),
(151904, 11519, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'LEONCIO MARTINEZ'),
(151905, 11519, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'PETARE');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO URDANETA 1001520 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(152001, 11520, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'CUA'),
(152002, 11520, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'NUEVA CUA');

/********************************************/
/* ESTADO MIRANDA  1000047 					*/
/********************************************/
/* MUNICIPIO ZAMORA 1001521 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(152101, 11521, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'BOLIVAR'),
(152102, 11521, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100047, 'N', 'GUATIRE');

/********************************************************/
/********************************************************/
/* ESTADO VARGAS  1000056 								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO VARGAS  1000056 					*/
/********************************************/
/* MUNICIPIO VARGAS 1002401 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(240101, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'CARABALLEDA'),
(240102, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'CARAYACA'),
(240103, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'CARUAO'),
(240104, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'CATIA LA MAR'),
(240105, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'EL JUNKO'),
(240106, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'LA GUAIRA'),
(240107, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'MACUTO'),
(240108, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'MAIQUETIA'),
(240109, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'NAIGUATA'),
(240110, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'CARLOS SOUBLETTE'),
(240111, 12401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100056, 'N', 'RAUL LEONI');


/********************************************************/
/********************************************************/
/* ESTADO ARAGUA  1000037 								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO BOLIVAR 1000501 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050100, 10501, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAN MATEO');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO CAMATAGUA 1000502 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050201, 10502, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CAMATAGUA'),
(050202, 10502, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CARMEN DE CURA');


/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO Fco. Linares Alcantara 1000503	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050301, 10503, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SANTA RITA'),
(050302, 10503, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'FRANCISCO DE MIRANDA'),
(050303, 10503, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'MONSEÑOR FELICIANO GONZÁLEZ');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO GIRARDOT 1000504 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050401, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'LAS DELICIAS'),
(050402, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'ANDRES ELOY BLANCO'),
(050403, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CHORONI'),
(050404, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'JOAQUIN CRESPO'),
(050405, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'JOSE CASANOVA GODOY'),
(050406, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'LOS TACARIGUAS'),
(050407, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'MADRE MARIA DE SAN JOSE'),
(050408, 10504,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'PEDRO JOSE OVALLES');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO JOSE ANGEL LAMAS 1000505		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050501, 10505, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SANTA CRUZ');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO José Felix Ribas 1000506 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050601, 10506, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'LA VICTORIA'),
(050602, 10506, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CASTOR NIEVES RIOS'),
(050603, 10506, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'LAS GUACAMAYAS'),
(050604, 10506, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'PAO DE ZARATE'),
(050605, 10506, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'ZUATA');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO José Rafel Revenga 1000507		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050701, 10507, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'EL CONSEJO');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO LIBERTADOR 1000508 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050801, 10508, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'PALO NEGRO'),
(050802, 10508, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAN MARTIN DE PORRES');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO Mario Briceño Irragorry 1000509*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(050901, 10509, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'EL LIMON'),
(050902, 10509, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CAÑA DE AZUCAR');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO OCUMARE DE LA COSTA 1000510 	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(051001, 10510, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'OCUMARE DE LA COSTA');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO San Casimiro 1000511			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(051101, 10511, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAN CASIMIRO'),
(051102, 10511, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'GUIRIPA'),
(051103, 10511, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'OLLAS DE CARAMACATE'),
(051104, 10511, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'VALLE MORIN');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO San Sebastian 1000512			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(051201, 10512, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAN SEBASTIÁN');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO Santiago Mariño 1000513 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(051301, 10513, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'TURMERO'),
(051302, 10513, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'ALFREDO PACHECO MIRANDA'),
(051303, 10513, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'AREVALO APONTE'),
(051304, 10513, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CHUAO'),
(051305, 10513, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAMÁN DE GÜERE');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO Santos Michelena 1000514		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(051401, 10514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'LAS TEJERIAS'),
(051402, 10514, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'TIARA');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO SUCRE 1000515	 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(051501, 10515, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'CAGUA'),
(051502, 10515, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'BELLA VISTA');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO TOVAR 1000516	 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(051601, 10516, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'COLONIA TOVAR');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO URDANETA 1000517 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(051701, 10517, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'BARBACOAS'),
(051702, 10517, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'LAS PEÑITAS'),
(051703, 10517, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAN FRANCISCO DE CARA'),
(051704, 10517, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'TAGUAY');

/********************************************/
/* ESTADO ARAGUA  1000037 					*/
/********************************************/
/* MUNICIPIO ZAMORA 1000518 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(051801, 10518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'VILLA DE CURA'),
(051802, 10518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'MAGDALENO'),
(051803, 10518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'PQ AUGUSTO MIJARES'),
(051804, 10518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'SAN FRANCISCO DE ASIS'),
(051805, 10518, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100037, 'N', 'VALLES DE TUCUTUNEMO');


/********************************************************/
/********************************************************/
/* ESTADO CARABOBO  1000040 							*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO  BEJUMA 1000801				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(0801001, 10801, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'BEJUMA'),
(0801002, 10801, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'CANOABO'),
(0801003, 10801, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'SIMON BOLIVAR');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO CARLOS ARVELO 1000802			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080201, 10802, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'BELEN'),
(080202, 10802, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'GUIGUE'),
(080203, 10802, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'TACARIGUA');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO DIEGO IBARRA 1000803			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080301, 10803, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'AGUAS CALIENTES'),
(080302, 10803, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'MARIARA');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO GUACARA 1000804  				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080401, 10804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'CIUDAD ALIANZA'),
(080402, 10804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'GUACARA'),
(080403, 10804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'YAGUA');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO JUAN JOSE MORA 1000805			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080501, 10805, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'MORON'),
(080502, 10805, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'URAMA');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO LIBERTADOR 1000806				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080601, 10806, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'INDEPENDENCIA'),
(080602, 10806, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'TOCUYITO');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO  LOS GUAYOS 1000807			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080701, 10807, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'LOS GUAYOS');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO MIRANDA 1000808  				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080801, 10808, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'MIRANDA');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO MONTALBAN 1000809 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(080901, 10809, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'MONTALBAN');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO  NAGUANAGUA 1000810			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(081001, 10810, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'NAGUANAGUA');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO PUERT CABELLO 1000811			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(081101, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'BARTOLOME SALOM'),
(081102, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'BORBURATA'),
(081103, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'DEMOCRACIA'),
(081104, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'FRATERNIDAD'),
(081105, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'GOAIGOAZA'),
(081106, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'JUAN JOSE FLORES'),
(081107, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'PATANEMO'),
(081108, 10811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'UNION');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO SAN DIEGO 1000812 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(081201, 10812, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'SAN DIEGO');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO  SAN JOAQUIN 1000813			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(081301, 10813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'SAN JOAQUIN');
/********************************************/
/* ESTADO CARABOBO  1000040 				*/
/********************************************/
/* MUNICIPIO VALENCIA 1000814				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(081401, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'CANDELARIA'),
(081402, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'CATEDRAL'),
(081403, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'EL SOCORRO'),
(081404, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'MIGUEL PEÑA'),
(081405, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'NEGRO PRIMERO'),
(081406, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'RAFAEL URDANETA'),
(081407, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'SAN BLAS'),
(081408, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'SAN JOSE'),
(081409, 10814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100040, 'N', 'SANTA ROSA');

/********************************************************/
/********************************************************/
/* ESTADO COJEDES  1000041	 							*/
/********************************************************/
/********************************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090101, 10901, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'COJEDES'),
(090102, 10901, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'JUAN DE MATA SUAREZ');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090201, 10902, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'TINAQUILLO');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090301, 10903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'EL BAUL'),
(090302, 10903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'SUCRE');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090401, 10904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'LA AGUADITA'),
(090402, 10904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'MACAPO');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090501, 10905, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'EL PAO');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090601, 10906, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'EL AMPARO'),
(090602, 10906, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'LIBERTAD DE COJEDES');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090701, 10907, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'RÓMULO GALLEGOS');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090801, 10908, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'JUAN ANGEL BRAVO'),
(090802, 10908, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'MANUEL MANRIQUE'),
(090803, 10908, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'SAN CARLOS DE AUSTRIA');

INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(090901, 10909, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100041, 'N', 'GENERAL EN JEFE JOSÉ LAURENCIO SILVA');


/********************************************************/
/********************************************************/
/* ESTADO AMAZONAS  1000034	 							*/
/********************************************************/
/********************************************************/
/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO ALTO ORINOCO 1000201			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020101, 10201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'LA ESMERALDA'),
(020102, 10201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'HUACHAMACARE'),
(020103, 10201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'MARAWAKA'),
(020104, 10201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'MAVACA');

/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO ATABAPO 1000202			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020201, 10202, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'SAN FERNANDO DE ATABAPO'),
(020202, 10202, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'CANAME'),
(020203, 10202, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'UCATA'),
(020204, 10202, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'YAPACANA');

/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO ATURES 1000203			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020301, 10203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'FERNANDO GIRON TOVAR'),
(020302, 10203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'LUIS ALBERTO GOMEZ'),
(020303, 10203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'PARHUEÑA'),
(020304, 10203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'PLATANILLAL');

/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO AUTANA 1000204			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020401, 10204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'ISLA DE RATON'),
(020402, 10204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'GUAYAPO'),
(020403, 10204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'MUNDUAPO'),
(020404, 10204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'SAMARIAPO'),
(020405, 10204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'SIPAPO');

/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO MANAPIARE 1000205			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020501, 10205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'SAN JUAN DE MANAPIARE'),
(020502, 10205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'ALTO VENTUARI'),
(020503, 10205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'BAJO VENTUARI'),
(020504, 10205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'MEDIO VENTUARI');

/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO MAROA 1000206			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020601, 10206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'MAROA'),
(020602, 10206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'COMUNIDAD'),
(020603, 10206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'VICTORINO');

/********************************************/
/* ESTADO AMAZONAS  1000034 				*/
/********************************************/
/* MUNICIPIO RIO NEGRO 1000207				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(020701, 10207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'SAN CARLOS DE RIO NEGRO'),
(020702, 10207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'COCUY'),
(020703, 10207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100034, 'N', 'SOLANO');


/********************************************************/
/********************************************************/
/* ESTADO BOLIVAR  1000039	 							*/
/********************************************************/
/********************************************************/
/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO CARONI 1000701					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070101, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'CACHAMAY'),
(070102, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'CHIRICA'),
(070103, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'DALLA COSTA'),
(070104, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ONCE DE ABRIL'),
(070105, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'POZO VERDE'),
(070106, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'SIMON BOLIVAR'),
(070107, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'UNARE'),
(070108, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'UNIVERSIDAD'),
(070109, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'VISTA AL SOL'),
(070110, 10701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'YOCOIMA');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO CEDEÑO 1000702					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070201, 10702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'CAICARA DEL ORINOCO'),
(070202, 10702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ALTAGRACIA'),
(070203, 10702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ASCENSION FARRERAS'),
(070204, 10702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'GUANIAMO'),
(070205, 10702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'LA URBANA'),
(070206, 10702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'PIJIGUAOS');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO EL CALLAO 1000703					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070301, 10703, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'EL CALLAO');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO GRANSABAN 1000704					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070401, 10704, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'SANTA ELENA DE UAIREN'),
(070402, 10704, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'IKABARU');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO HERES 1000705					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070501, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'AGUA SALADA'),
(070502, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'CATEDRAL'),
(070503, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'JOSE ANTONIO PAEZ'),
(070504, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'LA SABANITA'),
(070505, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'MARHUANTA'),
(070506, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ORINOCO'),
(070507, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'PANAPANA'),
(070508, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'VISTA HERMOSA'),
(070509, 10705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ZEA');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO PIAR 1000706					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070601, 10706, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'UPATA'),
(070602, 10706, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ANDRES ELOY BLANCO'),
(070603, 10706, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'PEDRO COVA');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO RAUL LEONI 1000707				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070701, 10707, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'CIUDAD PIAR'),
(070702, 10707, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'BARCELONETA'),
(070703, 10707, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'SAN FRANCISCO'),
(070704, 10707, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'SANTA BARBARA');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO ROSCIO 1000708					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070801, 10708, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'GUASIPATI'),
(070802, 10708, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'SALOM');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO SIFONTES 1000709				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(070901, 10709, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'TUMEREMO'),
(070902, 10709, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'DALLA COSTA'),
(070903, 10709, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'SAN ISIDRO');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO SUCRE 1000710					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(071001, 10710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'MARIPA'),
(071002, 10710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'ARIPAO'),
(071003, 10710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'GUARATARO'),
(071004, 10710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'LAS MAJADAS'),
(071005, 10710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'MOITACO');

/********************************************/
/* ESTADO BOLIVAR  1000039	 				*/
/********************************************/
/* MUNICIPIO PADRE PEDRO CHIEN 1000711		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(071101, 10711, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100039, 'N', 'EL PALMAR');



/********************************************************/
/********************************************************/
/* ESTADO DELTA AMACURO  1000042						*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO DELTA AMACURO  1000042			*/
/********************************************/
/* MUNICIPIO ANTONIO DIAZ 1001001			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(100101, 11001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'ALMIRANTE LUIS BRION'),
(100102, 11001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'ANICETO LUGO'),
(100103, 11001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'CURIAPO'),
(100104, 11001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'MANUEL RENAUD'),
(100105, 11001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'PADRE BARRAL'),
(100106, 11001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'SANTOS DE ABELGAS');

/********************************************/
/* ESTADO DELTA AMACURO  1000042			*/
/********************************************/
/* MUNICIPIO CASACOIMA 1001002			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(100201, 11002, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', '5 DE JULIO'),
(100202, 11002, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'IMATACA'),
(100203, 11002, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'JUAN BAUTISTA ARISMENDI'),
(100204, 11002, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'MANUEL PIAR'),
(100205, 11002, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'ROMULO GALLEGOS');

/********************************************/
/* ESTADO DELTA AMACURO  1000042			*/
/********************************************/
/* MUNICIPIO PEDERNALES 1001003				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(100301, 11003, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'LUIS BELTRÁN PRIETO FIGUERORA'),
(100302, 11003, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'PEDERNALES');

/********************************************/
/* ESTADO DELTA AMACURO  1000042			*/
/********************************************/
/* MUNICIPIO TUCUPITA 1001004				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(100401, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'JOSE VIDAL MARCANO'),
(100402, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'JUAN MILLAN'),
(100403, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'LEONARDO RUIZ PINEDA'),
(100404, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'MARISCAL ANTONIO JOSÉ DE SUCRE'),
(100405, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'MONSEÑOR ARGIMIRO GARCIA'),
(100406, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'SAN JOSE'),
(100407, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'SAN RAFAEL'),
(100408, 11004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100042, 'N', 'VIRGEN DEL VALLE');

/********************************************************/
/********************************************************/
/* ESTADO NUEVA ESPARTA  1000049						*/
/********************************************************/
/********************************************************/
/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Antolín del campo 1001701		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170101, 11701, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'LA PLAZA DE PARAGUACHI');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Arismendi 1001702				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170201, 11702, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'LA ASUNCION');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Díaz	1001703					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170301, 11703, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'SAN JUAN BAUTISTA'),
(170302, 11703, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'ZABALA');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO García 1001704					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170401, 11704, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'VALLE DEL ESPIRITU SANTO'),
(170402, 11704, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'FRANCISCO FAJARDO');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Gómez			 1001705		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170501, 11705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'SANTA ANA'),
(170502, 11705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'BOLIVAR'),
(170503, 11705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'GUEVARA'),
(170504, 11705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'MATASIETE'),
(170505, 11705, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'SUCRE');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Maneiro 1001706		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170601, 11706, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'PAMPATAR'),
(170602, 11706, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'AGUIRRE');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO marcano 1001707				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170701, 11707, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'JUAN GRIEGO'),
(170702, 11707, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'ADRIAN');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO mariño 1001708					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170801, 11708, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'PORLAMAR');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Península de Macanao 1001709	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(170901, 11709, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'BOCA DEL RIO'),
(170902, 11709, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'SAN FRANCISCO');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Tubores 1001710				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(171001, 11710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'PUNTA DE PIEDRAS'),
(171002, 11710, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'LOS BARALES');

/********************************************/
/* ESTADO Nueva Esparta  1000049			*/
/********************************************/
/* MUNICIPIO Villalba 1001711				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(171101, 11711, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'SAN PEDRO DE COCHE'),
(171102, 11711, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100049, 'N', 'vicente fuentes');


/********************************************************/
/********************************************************/
/* ESTADO MERIDA  1000046								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO ALBERTO ADRIANI 1001401		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140101, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'GABRIEL PICON G.'),
(140102, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'HECTOR AMABLE MORA'),
(140103, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'JOSE NUCETE SARDI'),
(140104, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PRESIDENTE BETANCOURT'),
(140105, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PRESIDENTE PAEZ'),
(140106, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PRESIDENTE ROMULO GALLEGOS'),
(140107, 11401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PULIDO MENDEZ');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO Andrés Bello 1001402			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140201, 11402, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LA AZULITA');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO ANTONIO PINTO SALINAS 1001403	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140301, 11403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SANTA CRUZ DE MORA'),
(140302, 11403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MESA BOLIVAR'),
(140303, 11403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MESA DE LAS PALMAS');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO ARICAGUA 1001404				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140401, 11404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ARICAGUA'),
(140402, 11404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN ANTONIO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO Arzobispo Chacón 1001405		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140501, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CANAGUA'),
(140502, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CAPURI'),
(140503, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CHACANTA'),
(140504, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'EL MOLINO'),
(140505, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'GUAIMARAL'),
(140506, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MUCUCHACHI'),
(140507, 11405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MUCUTUY');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO CAMPO ELIAS 1001406	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140601, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ACEQUIAS'),
(140602, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'FERNANDEZ PEÑA'),
(140603, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'JAJI'),
(140604, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LA MESA'),
(140605, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MATRIZ'),
(140606, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MONTALBAN'),
(140607, 11406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN JOSE');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO CARRACCIOLO PARRA OLMEDO 1001407*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140701, 11407, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'TUCANI'),
(140702, 11407, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'FLORENCIO RAMIREZ');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO CARDENAL QUINTERO	 1001408	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140801, 11408, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SANTO DOMINGO'),
(140802, 11408, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LAS PIEDRAS');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO GUARAQUE			 1001409	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(140901, 11409, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'GUARAQUE'),
(140902, 11409, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MESA DE QUINTERO'),
(140903, 11409, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'RIO NEGRO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO JULIO CESAR SALAS 1001410		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141001, 11410, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ARAPUEY'),
(141002, 11410, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PALMIRA');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO JUSTO BRICEÑO		 1001411	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141101, 11411, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'TORONDOY'),
(141102, 11411, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN CRISTOBAL DE TORONDOY');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO LIBERTADOR 1001412	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141201, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ANTONIO SPINETTI DINI'),
(141202, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ARIAS'),
(141203, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CARACCIOLO PARRA PEREZ'),
(141204, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'DOMINGO PEÑA'),
(141205, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'EL LLANO'),
(141206, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'EL MORRO'),
(141207, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'GONZALO PICON FEBRES'),
(141208, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'JACINTO PLAZA'),
(141209, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'JUAN RODRIGUEZ SUAREZ'),
(141210, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LASSO DE LA VEGA'),
(141211, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LOS NEVADOS'),
(141212, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MARIANO PICON SALAS'),
(141213, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MILLA'),
(141214, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'OSUNA RODRIGUEZ'),
(141215, 11412, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAGRARIO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO ANATONIO MIRANDA	 1001413	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141301, 11413, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'TIMOTES'),
(141302, 11413, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ANDRES ELOY BLANCO'),
(141303, 11413, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LA VENTA'),
(141304, 11413, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PIÑANGO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO OBISPO RAMOS DE LORA 1001414	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141401, 11414, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SANTA ELENA DE ARENALES'),
(141402, 11414, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ELOY PAREDES'),
(141403, 11414, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN RAFAEL DE ALCAZAR');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO PADRE NOGUERA		 1001415	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141501, 11415, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SANTA MARIA DE CAPARO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO PUEBLO LLANO		 1001416	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES 
(141601, 11416, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PUEBLO LLANO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO RANGEL				 1001417	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141701, 11417, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MUCUCHIES'),
(141702, 11417, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CACUTE'),
(141703, 11417, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LA TOMA'),
(141704, 11417, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MUCURUBA'),
(141705, 11417, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN RAFAEL');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO RIVAS DAVILA		 1001418	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141801, 11418, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'BAILADORES'),
(141802, 11418, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'GERONIMO MALDONADO');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO SANTOS MARQUINA 1001419		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(141901, 11419, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'TABAY');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO SUCRE 		1001420				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(142001, 11420, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LAGUNILLAS'),
(142002, 11420, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CHIGUARA'),
(142003, 11420, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ESTANQUES'),
(142004, 11420, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'LA TRAMPA'),
(142005, 11420, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'PUEBLO NUEVO DEL SUR'),
(142006, 11420, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN JUAN');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO ANATONIO TOVAR 	1001421		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(142101, 11421, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'EL AMPARO'),
(142102, 11421, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'EL LLANO'),
(142103, 11421, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SAN FRANCISCO'),
(142104, 11421, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'TOVAR');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO TULIO FEBRES CORDERO 1001422	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(142201, 11422, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'NUEVA BOLIVIA'),
(142202, 11422, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'INDEPENDENCIA'),
(142203, 11422, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'MARIA DE LA CONCEPCION PALACIOS'),
(142204, 11422, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'SANTA APOLONIA');

/********************************************/
/* ESTADO Merida  1000046					*/
/********************************************/
/* MUNICIPIO ZEA				 1001423	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(142301, 11423, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'ZEA'),
(142302, 11423, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100046, 'N', 'CAÑO EL TIGRE');


/********************************************************/
/********************************************************/
/* ESTADO TACHIRA  1000052								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO ANDRES BELLO		 1002001	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200101, 12001, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'CORDERO');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO ANTONIO ROMULO ACOSTA 1002002	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200201, 12002, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LAS MESAS');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO AYACUCHO		 1002003		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200301, 12003, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'COLON'),
(200302, 12003, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'RIVAS BERTI'),
(200303, 12003, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN PEDRO DEL RIO');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO BOLIVAR		 1002004		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200401, 12004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN ANTONIO DEL TACHIRA'),
(200402, 12004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'ISAIAS MEDINA ANGARITA'),
(200403, 12004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'JUAN VICENTE GOMEZ'),
(200404, 12004, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'PALOTAL');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO CARDENAS		 1002005		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200501, 12005, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'TARIBA'),
(200502, 12005, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'AMENODORO RANGEL LAMU'),
(200503, 12005, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA FLORIDA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO CORDOBA			 1002006	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200601, 12006, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SANTA ANA  DEL TACHIRA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO FERNANDEZ FEO		 1002007	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200701, 12007, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN RAFAEL DEL PINAL'),
(200702, 12007, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'ALBERTO ADRIANI'),
(200703, 12007, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SANTO DOMINGO');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO FRANCISCO DE MIRANDA	1002008	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200801, 12008, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN JOSE DE BOLIVAR');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO GARCIA DE HEVIA	 1002009	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(200901, 12009, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA FRIA'),
(200902, 12009, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'BOCA DE GRITA'),
(200903, 12009, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'JOSE ANTONIO PAEZ');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO GUASIMOS		 1002010		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201001, 12010, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'PALMIRA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO Independencia		 1002001	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201101, 12011, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'CAPACHO NUEVO'),
(201102, 12011, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'JUAN GERMAN ROSCIO'),
(201103, 12011, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'ROMAN CARDENAS');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO JAUREGUI		 1002012	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201201, 12012, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA GRITA'),
(201202, 12012, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'EMILIO CONSTANTINO GUERRERO'),
(201203, 12012, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'MONSEÑOR MIGUEL ANTONIO SALAS');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO JOSE MARIA	VARGAS	 1002013	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201301, 12013, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'EL COBRE');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO JUNIN		 	1002014			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201401, 12014, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'RUBIO'),
(201402, 12014, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'BRAMON'),
(201403, 12014, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA PETROLEA'),
(201404, 12014, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'QUINIMARI');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO LIBERTAD		 	1002015		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201501, 12015, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'CAPACHO VIEJO'),
(201502, 12015, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'CIPRIANO CASTRO'),
(201503, 12015, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'MANUEL FELIPE RUGELES');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO LIBERTADOR			 1002016	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201601, 12016, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'ABEJALES'),
(201602, 12016, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'DORADAS'),
(201603, 12016, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'EMETERIO OCHOA'),
(201604, 12016, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN JOAQUIN DE NAVAY');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO LOBATERA		 1002017		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201701, 12017, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LOBATERA'),
(201702, 12017, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'CONSTITUCION');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO MICHELENA			 1002018	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201801, 12018, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'MICHELENA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO PANAMERICANO		 1002019	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(201901, 12019, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'COLONCITO'),
(201902, 12019, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA PALMITA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO PEDRO MARIA UREÑA	 1002020	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202001, 12020, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'UREÑA'),
(202002, 12020, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'NUEVA ARCADIA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO RAFAEL URDANETA	 1002021	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202101, 12021, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'DELICIAS');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO SAMUEL DARIO MALDONADO 1002022	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202201, 12022, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA TENDIDA'),
(202202, 12022, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'BOCONO'),
(202203, 12022, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'HERNANDEZ');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO SAN CRISOBAL		 1002023	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202301, 12023, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'DR. FRANCISCO ROMERO LOBO'),
(202302, 12023, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'LA CONCORDIA'),
(202303, 12023, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'PEDRO MARIA MORANTES'),
(202304, 12023, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN SEBASTIAN'),
(202305, 12023, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN JUAN BAUTISTA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO SEBORUCO		 1002024		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202401, 12024, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SEBORUCO');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO SIMON RODRIGUEZ	 1002025 	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202501, 12025, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN SIMON');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO SUCRE		 1002026			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202601 ,12026, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'QUENIQUEA'),
(202602 ,12026, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN PABLO'),
(202603 ,12026, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'ELEAZAR LOPEZ CONTRERA');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO TORBES		 1002027			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202701, 12027, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'SAN JOSECITO');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO URIBANTE		 1002028		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202801, 12028, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'PREGONERO'),
(202802, 12028, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'CARDENAS'),
(202803, 12028, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'JUAN PABLO PEÑALOZA'),
(202804, 12028, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'POTOSI');

/********************************************/
/* ESTADO TACHIRA  1000052					*/
/********************************************/
/* MUNICIPIO SAN JUDAS TADEO	 1002029 	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(202901, 12029, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100052, 'N', 'UMUQUENA');

/********************************************************/
/********************************************************/
/* ESTADO TRUJILLO  1000053								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO 	ANDRES BELLO 1002101 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210101, 12101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ARAGUANEY'),
(210102, 12101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL JAGUITO'),
(210103, 12101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA ESPERANZA'),
(210104, 12101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTA ISABEL');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO 	BOCONO 1002102 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210201, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'AYACUCHO'),
(210202, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'BOCONO'),
(210203, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'BURBUSAY'),
(210204, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL CARMEN'),
(210205, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'GENERAL RIVAS'),
(210206, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'GUARAMACAL'),
(210207, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA VEGA DE GUARAMACAL'),
(210208, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MONSEÑOR JAUREGUI'),
(210209, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MOSQUEY'),
(210210, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'RAFAEL RANGEL'),
(210211, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SAN JOSE'),
(210212, 12102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SAN MIGUEL');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO 	BOLIVAR 1002103		 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210301, 12103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CHEREGUE'),
(210302, 12103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'GRANADOS'),
(210303, 12103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SABANA GRANDE');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO 	CANDELARIA 1002104 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210401, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ARNOLDO GABALDON'),
(210402, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'BOLIVIA'),
(210403, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CARRILLO'),
(210404, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CEGARRA'),
(210405, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CHEJENDE'),
(210406, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MANUEL SALVADOR ULLOA'),
(210407, 12104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SAN JOSE');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO 	CARACHE 1002105		 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210501, 12105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CARACHE'),
(210502, 12105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CUICAS'),
(210503, 12105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA CONCEPCION'),
(210504, 12105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'PANAMERICANA'),
(210505, 12105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTA CRUZ');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO 	ESCUQUE 1002106		 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210601, 12106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ESCUQUE'),
(210602, 12106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA UNION'),
(210603, 12106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SABANA LIBRE'),
(210604, 12106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTA RITA');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO JOSE F.MARQUEZ C. 1002107 		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210701, 12107, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ANTONIO JOSE DE SUCRE'),
(210702, 12107, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL SOCORRO'),
(210703, 12107, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LOS CAPRICHOS');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO JUAN V. CAMPOS ELIAS 1002108 	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210801, 12108, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ARNOLDO GABALDON'),
(210802, 12108, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CAMPO ELIAS');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO LA CEIBA 	1002109 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(210901, 12109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL PROGRESO'),
(210902, 12109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA CEIBA'),
(210903, 12109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTA APOLONIA'),
(210904, 12109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'TRES DE FEBRERO');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO MIRANDA 	1002110 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211001, 12110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'AGUA CALIENTE'),
(211002, 12110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'AGUA SANTA'),
(211003, 12110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL CENIZO'),
(211004, 12110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL DIVIDIVE'),
(211005, 12110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'VALERITA');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO MONTE CARMELO 	1002111			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211101, 12111, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'BUENA VISTA'),
(211102, 12111, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MONTE CARMELO'),
(211103, 12111, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTA MARIA DEL HORCON');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO MOTATAN 	1002112 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211201, 12112, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL BAÑO'),
(211202, 12112, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'JALISCO'),
(211203, 12112, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MOTATAN');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO PANPAN 	1002113 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211301, 12113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'FLOR DE PATRIA'),
(211302, 12113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA PAZ'),
(211303, 12113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'PAMPAN'),
(211304, 12113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTA ANA');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO PAMPANITO 	1002114 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211401, 12114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA CONCEPCION'),
(211402, 12114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'PAMPANITO'),
(211403, 12114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'PAMPANITO II');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO RAFAEL RANGEL 	1002115			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211501, 12115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'BETIJOQUE'),
(211502, 12115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL CEDRO'),
(211503, 12115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'JOSE GREGORIO HERNANDEZ'),
(211504, 12115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA PUEBLITA');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO SAN RAFAEL DE CARVAJAL	1002116	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211601, 12116, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ANTONIO N BRICEÑO'),
(211602, 12116, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CAMPO ALEGRE'),
(211603, 12116, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CARVAJAL'),
(211604, 12116, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'JOSE LEONARDO SUAREZ');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO SUCRE 	1002117 				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211701, 12117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'EL PARAISO'),
(211702, 12117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'JUNIN'),
(211703, 12117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SABANA DE MENDOZA'),
(211704, 12117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'VALMORE RODRIGUEZ');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO TRUJILLO 	1002118 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211801, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'ANDRES LINARES'),
(211802, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CHIQUINQUIRA'),
(211803, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CRISTOBAL MENDOZA'),
(211804, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CRUZ CARRILLO'),
(211805, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MATRIZ'),
(211806, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MONSEÑOR CARRILLO'),
(211807, 12118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'TRES ESQUINAS');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO URDANETA 	1002119 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(211901, 12119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'CABIMBU'),
(211902, 12119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'JAJO'),
(211903, 12119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA MESA'),
(211904, 12119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA QUEBRADA'),
(211905, 12119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SANTIAGO'),
(211906, 12119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'TUÑAME');

/********************************************/
/* ESTADO TRUJILLO  1000053					*/
/********************************************/
/* MUNICIPIO VALERA 	1002120 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(212001, 12120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'JUAN IGNACIO MONTILLA'),
(212002, 12120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA BEATRIZ'),
(212003, 12120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'LA PUERTA'),
(212004, 12120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MENDOZA'),
(212005, 12120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'MERCEDES DIAZ'),
(212006, 12120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100053, 'N', 'SAN LUIS');


/********************************************************/
/********************************************************/
/* ESTADO APURE  1000036								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO ACHAGUAS 	1000401 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040101, 10401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'ACHAGUAS'),
(040102, 10401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'APURITO'),
(040103, 10401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'EL YAGUAL'),
(040104, 10401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'GUACHARA'),
(040105, 10401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'MUCURITAS'),
(040106, 10401, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'QUESERAS DEL MEDIO');

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO BIRUACA 	1000402 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040201, 10402, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'BIRUACA');

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO MUÑOZ 	1000403 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040301, 10403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'BRUZUAL'),
(040302, 10403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'MANTECAL'),
(040303, 10403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'QUINTERO'),
(040304, 10403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'RINCON HONDO'),
(040305, 10403, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'SAN VICENTE');

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO PAEZ 	1000404 			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040401, 10404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'ARAMENDI'),
(040402, 10404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'EL AMPARO'),
(040403, 10404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'GUASDUALITO'),
(040404, 10404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'SAN CAMILO'),
(040405, 10404, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'URDANETA');

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO PEDRO CAMEJO 	1000405			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040501, 10405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'CODAZZI'),
(040502, 10405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'CUNAVICHE'),
(040503, 10405, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'SAN JUAN DE PAYARA');

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO ROMULO GALLEGOS 	1000406		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040601, 10406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'ELORZA'),
(040602, 10406, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'LA TRINIDAD');

/********************************************/
/* ESTADO APURE  1000036					*/
/********************************************/
/* MUNICIPIO SAN FERNANDO 	1000407			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(040701, 10407, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'EL RECREO'),
(040702, 10407, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'PEÑALVER'),
(040703, 10407, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'SAN FERNANDO'),
(040704, 10407, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100036, 'N', 'SAN RAFAEL DE ATAMAICA');

/********************************************************/
/********************************************************/
/* ESTADO BARINAS  1000038								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO ALBERTO ARVELO TORREALBA 1000601*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060101, 10601, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'RODRIGUEZ DOMINGUEZ'),
(060102, 10601, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SABANETA');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO ANDRES ELOY BLANCO	1000602		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060201, 10602, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'EL CANTON'),
(060202, 10602, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'PUERTO VIVAS'),
(060203, 10602, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SANTA CRUZ DE GUACAS');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO ANTONIO JOSE DE SUCRE	1000603	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060301, 10603, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'ANDRES BELLO'),
(060302, 10603, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'NICOLAS PULIDO'),
(060303, 10603, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'TICOPORO');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO ARISMENDI	1000604				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060401, 10604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'ARISMENDI'),
(060402, 10604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'GUADARRAMA'),
(060403, 10604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'LA UNION'),
(060404, 10604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SAN ANTONIO');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO BARINAS	1000605				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060501, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'ALFREDO A LARRIVA'),
(060502, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'ALTO BARINAS'),
(060503, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'BARINAS'),
(060504, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'CORAZON DE JESUS'),
(060505, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'DOMINGA ORTIZ P'),
(060506, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'EL CARMEN'),
(060507, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'JUAN A RODRIGUEZ D'),
(060508, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'MANUEL P FAJARDO'),
(060509, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'RAMON I MENDEZ'),
(060510, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'ROMULO BETANCOURT'),
(060511, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SAN SILVESTRE'),
(060512, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SANTA INES'),
(060513, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SANTA LUCIA'),
(060514, 10605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'TORUNOS');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO BOLIVAR	1000606				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060601, 10606, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'ALTAMIRA'),
(060602, 10606, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'BARINITAS'),
(060603, 10606, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'CALDERAS');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO CRUZ PAREDES	1000607	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060701, 10607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'BARRANCAS'),
(060702, 10607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'EL SOCORRO'),
(060703, 10607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'MASPARRITO');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO EZEQUIEL ZAMORA	1000608		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060801, 10608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'JOSE IGNACIO DEL PUMAR'),
(060802, 10608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'PEDRO BRICEÑO MENDEZ'),
(060803, 10608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'RAMON IGNACIO MENDEZ'),
(060804, 10608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SANTA BARBARA');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO OBISPOS	1000609				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(060901, 10609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'EL REAL'),
(060902, 10609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'LA LUZ'),
(060903, 10609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'LOS GUASIMITOS'),
(060904, 10609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'OBISPOS');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO PEDRAZA	1000610				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(061001, 10610, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'CIUDAD BOLIVIA'),
(061002, 10610, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'IGNACIO BRICEÑO'),
(061003, 10610, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'JOSE FELIX RIBAS'),
(061004, 10610, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'PAEZ');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO ROJAS					1000611	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(061101, 10611, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'DOLORES'),
(061102, 10611, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'LIBERTAD'),
(061103, 10611, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'PALACIO FAJARDO'),
(061104, 10611, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SANTA ROSA');

/********************************************/
/* ESTADO BARINAS  1000038					*/
/********************************************/
/* MUNICIPIO SOSA	1000612					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(061201, 10612, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'CIUDAD DE NUTRIAS'),
(061202, 10612, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'EL REGALO'),
(061203, 10612, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'PUERTO DE NUTRIAS'),
(061204, 10612, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100038, 'N', 'SANTA CATALINA');


/********************************************************/
/********************************************************/
/* ESTADO GUARICO  1000044								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO CAMAGUAN	1001201				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120101, 11201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'CAMAGUAN'),
(120102, 11201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'PUERTO MIRANDA'),
(120103, 11201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'UVERITO');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO CHAGUARAMAS	1001202			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120201, 11202, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'CHAGUARAMAS');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO EL SOCORRO	1001203				*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120301, 11203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'EL SOCORRO');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO SEBASTIAN F DE MIRANDA	1001204	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120401, 11204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'CALABOZO'),
(120402, 11204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'EL CALVARIO'),
(120403, 11204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'EL RASTRO'),
(120404, 11204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'GUARDATINAJAS');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO JOSE FELIX RIBAS	1001205		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120501, 11205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN RAFAEL DE LAYA'),
(120502, 11205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'TUCUPIDO');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO JOSE TADEO MONAGAS	1001206		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120601, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'ALTAGRACIA DE ORITUCO'),
(120602, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'LEZAMA'),
(120603, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'LIBERTAD DE ORITUCO'),
(120604, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'PASO REAL DE MACAIRA'),
(120605, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN FRANCISCO DE MACAIRA'),
(120606, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN RAFAEL DE ORITUCO'),
(120607, 11206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SOUBLETTE');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO JOSE GERMAN ROSCIO	1001207		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120701, 11207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'CANTAGALLO'),
(120702, 11207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'PARAPARA'),
(120703, 11207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN JUAN DE LOS MORROS');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO JULIAN MELLADO	1001208			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120801, 11208, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'EL SOMBRERO'),
(120802, 11208, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SOSA');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO LAS MERCEDES	1001209			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(120901, 11209, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'CABRUTA'),
(120902, 11209, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'LAS MERCEDES'),
(120903, 11209, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SANTA RITA DE MANAPIRE');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO LEONARDO INFANTE	1001210		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(121001, 11210, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'ESPINO'),
(121002, 11210, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'VALLE DE LA PASCUA');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO PEDRO ZARAZA	1001211			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(121101, 11211, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN JOSE DE UNARE'),
(121102, 11211, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'ZARAZA');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO ORTIZ	1001212					*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(121201, 11212, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'ORTIZ'),
(121202, 11212, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN LORENZO DE TIZNADOS'),
(121203, 11212, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN FRANCISCO DE TIZNADOS'),
(121204, 11212, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN JOSE DE TIZNADOS');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO SAN GERONIMO DE GUAYABAL 1001213*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(121301, 11213, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'CAZORLA'),
(121302, 11213, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'GUAYABAL');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO SAN JOSE DE GUABIRE 1001214	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(121401, 11214, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SAN JOSE DE GUARIBE');

/********************************************/
/* ESTADO GUARICO  1000044					*/
/********************************************/
/* MUNICIPIO SANTA MARIA DE IPIRE	1001215	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(121501, 11215, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'ALTAMIRA'),
(121502, 11215, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100044, 'N', 'SANTA MARIA DE IPIRE');

/********************************************************/
/********************************************************/
/* ESTADO PORTUGUESA  1000050							*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO AGUA BLANCA			1001801	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180101, 11801, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'AGUA BLANCA');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO ARAURE					1001801	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180201, 11802, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'ARAURE'),
(180202, 11802, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'RIO ACARIGUA');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO ESTELLER			1001803		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180301, 11803, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'PIRITU'),
(180302, 11803, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'UVERAL');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO GUANARE			1001804		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180401, 11804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'GUANARE'),
(180402, 11804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'CORDOBA'),
(180403, 11804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'VIRGEN DE LA COROMOTO'),
(180404, 11804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SAN JOSE DE LA MONTAÑA'),
(180405, 11804, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SAN JUAN GUANAGUANARE');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO GUANARITO			1001805		*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180501, 11805, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'GUANARITO'),
(180502, 11805, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'DIVINA PASTORA'),
(180503, 11805, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'TRINIDAD DE LA CAPILLA');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO MONSEÑOR JOSE V DE UNDA 1001806*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180601, 11806, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'CHABASQUEN'),
(180602, 11806, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'PEÑA BLANCA');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO OSPINO			1001807			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180701, 11807, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'OSPINO'),
(180702, 11807, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'APARICION'),
(180703, 11807, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'LA ESTACION');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO PAEZ			1001808			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180801, 11808, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'ACARIGUA'),
(180802, 11808, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'PAYARA'),
(180803, 11808, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'PIMPINELA'),
(180804, 11808, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'RAMON PERAZA');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO PAELON			1001809			*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(180901, 11809, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'PAPELON'),
(180902, 11809, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'CAÑO DELGADITO');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO SAN GENARO DE BOCONOITO 1001810*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(181001, 11810, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'BOCONOITO'),
(181002, 11810, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'ANTOLIN TOVAR AQUINO');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO SAN RAFAEL DE ONOTO	1001811	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(181101, 11811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SAN RAFAEL DE ONOTO'),
(181102, 11811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SANTA FE'),
(181103, 11811, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'THERMO MORLES');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO SANTA ROSALIA			1001812	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(181201, 11812, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'EL PLAYON'),
(181202, 11812, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'FLORIDA');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO SUCRE					1001813	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(181301, 11813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'BISCUCUY'),
(181302, 11813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'CONCEPCION'),
(181303, 11813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SAN JOSE DE SAGUAZ'),
(181304, 11813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'UVENCIO A VELASQUEZ'),
(181305, 11813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'VILLA ROSA'),
(181306, 11813, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SAN RAFAEL PALO ALZADO');

/********************************************/
/* ESTADO PORTUGUESA 1000050				*/
/********************************************/
/* MUNICIPIO TUREN					1001814	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(181401, 11814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'VILLA BRUZUAL'),
(181402, 11814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'CANELONES'),
(181403, 11814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SAN ISIDRO LABRADOR'),
(181404, 11814, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100050, 'N', 'SANTA CRUZ');


/********************************************************/
/********************************************************/
/* ESTADO ANZOATEGUI  1000035							*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO ANACO					1000301	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030101, 10301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'ANACO'),
(030102, 10301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN JOAQUIN');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO ARAGUA					1000302	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030201, 10302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'ARAGUA DE BARCELONA'),
(030202, 10302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'CACHIPO');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO BOLIVAR				1000303	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030301, 10303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'BERGANTIN'),
(030302, 10303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'CAIGUA'),
(030303, 10303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EL CARMEN'),
(030304, 10303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EL PILAR'),
(030305, 10303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'NARICUAL'),
(030306, 10303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN CRISTOBAL');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO BRUZUAL				1000304	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030401, 10304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'CLARINES'),
(030402, 10304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'GUANAPE'),
(030403, 10304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SABANA DE UCHIRE');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO CAJIGAL				1000305	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030501, 10305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'ONOTO'),
(030502, 10305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN PABLO'),
(030503, 10305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SANTA BARBARA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO CARVAJAL				1000306	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030601, 10306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'VALLE GUANAPE'),
(030602, 10306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SANTA BARBARA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO DIEGO BAUTISTA URBANEJA 1000307*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030701, 10307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EL MORRO'),
(030702, 10307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'LECHERIAS');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO FREITES				1000308	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030801, 10308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'CANTAURA'),
(030802, 10308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'LIBERTADOR'),
(030803, 10308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SANTA ROSA'),
(030804, 10308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'URICA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO GUANIPA				1000309	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(030901,10309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN JOSE DE GUANIPA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO GUANTA					1000310	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031001, 10310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'CHORRERON'),
(031002, 10310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'GUANTA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO INDEPENDENCIA			1000311	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031101, 10311, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SOLEDAD'),
(031102, 10311, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'MAMO');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO LIBERTAD				1000312	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031201, 10312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN MATEO'),
(031202, 10312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EL CARITO'),
(031203, 10312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SANTA INES');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO MCGREGOR				1000313	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031301, 10313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EL CHAPARRO'),
(031302, 10313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'TOMAS ALFARO CALATRAVA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO MIRANDA				1000314	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031401, 10314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'PARIAGUAN'),
(031402, 10314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'ATAPIRIRE'),
(031403, 10314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'BOCA DEL PAO'),
(031404, 10314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EL PAO');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO MONAGAS				1000315	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031501, 10315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'MAPIRE'),
(031502, 10315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'PIAR'),
(031503, 10315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SANTA CLARA'),
(031504, 10315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN DIEGO DE CABRUTICA'),
(031505, 10315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'UVERITO'),
(031506, 10315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'ZUATA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO PEÑALVER				1000316	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031601, 10316, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'PUERTO PIRITU'),
(031602, 10316, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN MIGUEL'),
(031603, 10316, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SUCRE');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO PIRITU					1000317	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031701, 10317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'PIRITU'),
(031702, 10317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SAN FRANCISCO');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO SAN JUAN DE CAPISTRANO	1000318	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031801, 10318, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'BOCA DE CHAVEZ'),
(031802, 10318, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'BOCA UCHIRE');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO SANTA ANA				1000319	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(031901, 10319, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'PUEBLO NUEVO'),
(031902, 10319, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'SANTA ANA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO SIMON RODRIGUEZ		1000320	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(032001, 10320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'EDMUNDO BARRIOS'),
(032002, 10320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'MIGUEL OTERO SILVA');

/********************************************/
/* ESTADO ANZOATEGUI 	1000035				*/
/********************************************/
/* MUNICIPIO SOTILLO				1000321	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(032101, 10321, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'PUERTO LA CRUZ'),
(032102, 10321, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100035, 'N', 'POZUELOS');


/********************************************************/
/********************************************************/
/* ESTADO MONAGAS  1000048								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO ACOSTA					1001601	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160101, 11601, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SAN ANTONIO'),
(160102, 11601, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SAN FRANCISCO');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO AGUASAY				1001602	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160201, 11602, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'AGUASAY');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO BOLIVAR				1001603	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160301, 11603, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'CARIPITO');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO CARIPE					1001604	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160401, 11604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'CARIPE'),
(160402, 11604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'EL GUACHARO'),
(160403, 11604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'LA GUANOTA'),
(160404, 11604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SABANA DE PIEDRA'),
(160405, 11604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SAN AGUSTIN'),
(160406, 11604, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'TERESEN');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO CEDEÑO					1001605	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160501, 11605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'CAICARA'),
(160502, 11605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'AREO'),
(160503, 11605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SAN FELIX'),
(160504, 11605, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'VIENTO FRESCO');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO EZEQUIEL ZAMORA		1001606	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160601, 11606,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'PUNTA DE MATA'),
(160602, 11606,0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'EL TEJERO');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO LIBERTADOR				1001607	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160701, 11607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'TEMBLADOR'),
(160702, 11607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'CHAGUARAMAS'),
(160703, 11607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'LAS ALHUACAS'),
(160704, 11607, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'TABASCA');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO MATURIN				1001608	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160801, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'ALTO DE LOS GODOS'),
(160802, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'BOQUERON'),
(160803, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'EL COROZO'),
(160804, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'EL FURRIAL'),
(160805, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'JUSEPIN'),
(160806, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'LA PICA'),
(160807, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'LAS COCUIZAS'),
(160808, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SAN SIMON'),
(160809, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SAN VICENTE'),
(160810, 11608, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SANTA CRUZ');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO PIAR					1001609	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(160901, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'ARAGUA'),
(160902, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'APARICIO'),
(160903, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'CHAGUARAMAL'),
(160904, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'EL PINTO'),
(160905, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'GUANAGUANA'),
(160906, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'LA TOSCANA'),
(160907, 11609, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'TAGUAYA');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO PUNCERES				1001610	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(161001, 11610, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'QUIRIQUIRE'),
(161002, 11610, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'CACHIPO');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO SANTA BARBARA			1001611	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(161101, 11611, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'SANTA BARBARA');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO SOTILLO				1001612	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(161201, 11612, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'BARRANCAS'),
(161202, 11612, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'LOS BARRANCOS DE FAJARDO');

/********************************************/
/* ESTADO MONAGAS 	1000048					*/
/********************************************/
/* MUNICIPIO URACOA					1001613	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(161301, 11613, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100048, 'N', 'URACOA');


/********************************************************/
/********************************************************/
/* ESTADO SUCRE  1000051								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO ANDRES ELOY BLANCO		1001901	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190101, 11901, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'MARIÑO'),
(190102, 11901, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'ROMULO GALLEGOS');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO ANDRES MATA			1001902	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190201, 11902, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN JOSE DE AREOCUAR'),
(190202, 11902, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'TAVERA ACOSTA');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO ARISMENDI				1001903	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190301, 11903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'ANTONIO JOSE DE SUCRE'),
(190302, 11903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'EL MORRO DE PTO SANTO'),
(190303, 11903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'PUERTO SANTO'),
(190304, 11903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'RIO CARIBE'),
(190305, 11903, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN JUAN GALDONAS');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO BENITEZ				1001904	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190401, 11904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'EL PILAR'),
(190402, 11904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'EL RINCON'),
(190403, 11904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'GENERAL FRANCISCO A VASQUEZ'),
(190404, 11904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'GUARAUNOS'),
(190405, 11904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'TUNAPUICITO'),
(190406, 11904, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'UNION');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO BERMUDEZ				1001905	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190501, 11905, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'BOLIVAR'),
(190502, 11905, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'MACARAPANA'),
(190503, 11905, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SANTA CATALINA'),
(190504, 11905, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SANTA ROSA'),
(190505, 11905, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SANTA TERESA');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO BOLIVAR				1001906	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190601, 11906, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'MARIGUITAR');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO CAJIGAL				1001907	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190701, 11907, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'LIBERTAD'),
(190702, 11907, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'PAUJIL'),
(190703, 11907, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'YAGUARAPARO');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO CRUZ SALMERON ACOSTA	1001908	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190801, 11908, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'ARAYA'),
(190802, 11908, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CHACOPATA'),
(190803, 11908, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'MANICUARE');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO LIBERTADOR				1001909	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(190901, 11909, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CAMPO ELIAS'),
(190902, 11909, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'TUNAPUY');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO MARIÑO					1001910	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(191001, 11910, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CAMPO CLARO'),
(191002, 11910, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'IRAPA'),
(191003, 11910, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'MARABAL'),
(191004, 11910, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN ANTONIO DE IRAPA'),
(191005, 11910, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SORO');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO MEJIA					1001911	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(191101, 11911, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN ANTONIO DEL GOLFO');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO MONTES					1001912	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(191201, 11912, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'ARENAS'),
(191202, 11912, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'ARICAGUA'),
(191203, 11912, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'COCOLLAR'),
(191204, 11912, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CUMANACOA'),
(191205, 11912, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN FERNANDO'),
(191206, 11912, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN LORENZO');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO RIBERO					1001913	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(191301, 11913, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CARIACO'),
(191302, 11913, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CATUARO'),
(191303, 11913, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'RENDON'),
(191304, 11913, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SANTA CRUZ'),
(191305, 11913, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SANTA MARIA');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO SUCRE					1001914	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(191401, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'ALTAGRACIA'),
(191402, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'AYACUCHO'),
(191403, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'GRAN MARISCAL'),
(191404, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'RAUL LEONI'),
(191405, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SAN JUAN'),
(191406, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'SANTA INES'),
(191407, 11914, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'VALENTIN VALIENTE');

/********************************************/
/* ESTADO SUCRE 	1000051					*/
/********************************************/
/* MUNICIPIO VALDEZ					1001915	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(191501, 11915, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'BIDEAU'),
(191502, 11915, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'CRISTOBAL COLON'),
(191503, 11915, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'GUIRIA'),
(191504, 11915, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100051, 'N', 'PUNTA DE PIEDRA');


/********************************************************/
/********************************************************/
/* ESTADO FALCON  1000043								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO ACOSTA					1001101	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110101, 11101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CAPADARE'),
(110102, 11101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LA PASTORA'),
(110103, 11101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LIBERTADOR'),
(110104, 11101, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SAN JUAN DE LOS CAYOS');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO BOLIVA					1001102	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110201, 11102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ARACUA'),
(110202, 11102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LA PEÑA'),
(110203, 11102, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SAN LUIS');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO BUCHIVACOA				1001103	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110301, 11103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BARIRO'),
(110302, 11103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BOROJO'),
(110303, 11103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CAPATARIDA'),
(110304, 11103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'GUAJIRO'),
(110305, 11103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SEQUE'),
(110306, 11103, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ZAZARIDA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO CACIQUE MANAURE		1001104	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110401, 11104, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'YARACAL');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO CARIRUBANA				1001105	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110501, 11105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CARIRUBANA'),
(110502, 11105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'NORTE'),
(110503, 11105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PUNTA CARDON'),
(110504, 11105, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SANTA ANA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO COLINA					1001106	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110601, 11106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ACURIGUA'),
(110602, 11106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'GUAIBACOA'),
(110603, 11106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LA VELA DE CORO'),
(110604, 11106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LAS CALDERAS'),
(110605, 11106, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'MACORUCA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO DABAJURO				1001107	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110701, 11107, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'DABAJURO');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO DEMOCRACIA				1001108	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110801, 11108, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'AVARIA'),
(110802, 11108, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PEDREGAL'),
(110803, 11108, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PIEDRA GRANDE'),
(110804, 11108, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PURURECHE');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO FALCON					1001109	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(110901, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ADAURE'),
(110902, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ADICORA'),
(110903, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BARAIVED'),
(110904, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BUENA VISTA'),
(110905, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'EL HATO'),
(110906, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'EL VINCULO'),
(110907, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'JADACAQUIVA'),
(110908, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'MORUY'),
(110909, 11109, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PUEBLO NUEVO');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO FEDERACION				1001110	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111001, 11110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'AGUA LARGA'),
(111002, 11110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CHURUGUARA'),
(111003, 11110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'EL PAUJI'),
(111004, 11110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'INDEPENDENCIA'),
(111005, 11110, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'MAPARARI');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO JACURA					1001111	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111101, 11111, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'AGUA LINDA'),
(111102, 11111, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ARAURIMA'),
(111103, 11111, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'JACURA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO LOS TEQUES				1001112	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111201, 11112, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'JUDIBANA'),
(111202, 11112, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LOS TAQUES');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO MAUROA					1001113	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111301, 11113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CASIGUA'),
(111302, 11113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'MENE DE MAUROA'),
(111303, 11113, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SAN FELIX');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO MIRANDA				1001114	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111401, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'GUZMAN GUILLERMO'),
(111402, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'MITARE'),
(111403, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'RIO SECO'),
(111404, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SABANETA'),
(111405, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SAN ANTONIO'),
(111406, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SAN GABRIEL'),
(111407, 11114, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SANTA ANA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO MONSEÑOR ITURRIZA		1001115	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111501, 11115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BOCA DE TOCUYO'),
(111502, 11115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CHICHIRIVICHE'),
(111503, 11115, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'TOCUYO DE LA COSTA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO PALMASOLA				1001116	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111601, 11116, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PALMA SOLA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO PETIT					1001117	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111701, 11117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CABURE'),
(111702, 11117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'COLINA'),
(111703, 11117, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'CURIMAGUA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO PIRITU					1001118	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111801, 11118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PIRITU'),
(111802, 11118, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SAN JOSE DE LA COSTA');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO SAN FRANCISCO			1001119	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(111901, 11119, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'MIRIMIRE');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO SILVA					1001120	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(112001, 11120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BOCA DE AROA'),
(112002, 11120, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'TUCACAS');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO SUCRE					1001121	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(112101, 11121, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PECAYA'),
(112102, 11121, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SUCRE');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO TOCOPERO				1001122	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(112201, 11122, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'TOCOPERO');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO UNION					1001123	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(112301, 11123, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'EL CHARAL'),
(112302, 11123, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LAS VEGAS DEL TUY'),
(112303, 11123, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'SANTA CRUZ DE BUCARAL');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO URUMACO				1001124	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(112401, 11124, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'BRUZUAL'),
(112402, 11124, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'URUMACO');

/********************************************/
/* ESTADO FALCON 	1000043					*/
/********************************************/
/* MUNICIPIO ZAMORA					1001125	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(112501, 11125, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LA CIENAGA'),
(112502, 11125, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'LA SOLEDAD'),
(112503, 11125, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PUEBLO CUMAREBO'),
(112504, 11125, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'PUERTO CUMAREBO'),
(112505, 11125, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100043, 'N', 'ZAZARIDA');


/********************************************************/
/********************************************************/
/* ESTADO LARA  1000045									*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO ANDRES ELOY BLANCO		1001301	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130101, 11301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'PIO TAMAYO'),
(130102, 11301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'QUEBRADA HONDA DE GUACHE'),
(130103, 11301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'YACAMBU');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO CRESPO					1001302	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130201, 11302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'FREITEZ'),
(130202, 11302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'JOSE MARIA BLANCO');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO IRIBARREN				1001303	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130301, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'AGUEDO F. ALVARADO'),
(130302, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'BUENA VISTA'),
(130303, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CATEDRAL'),
(130304, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'EL CUJI'),
(130305, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'JUAN DE VILLEGAS'),
(130306, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'JUAREZ'),
(130307, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'LA CONCEPCION'),
(130308, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'SANTA ROSA'),
(130309, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'TAMACA'),
(130310, 11303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'UNION');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO JIMENEZ				1001304	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130401, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CORONEL MARIANO PERAZA'),
(130402, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CUARA'),
(130403, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'DIEGO DE LOZADA'),
(130404, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'JOSE BERNARDO DORANTE'),
(130405, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'JUAN B RODRIGUEZ'),
(130406, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'PARAISO DE SAN JOSE'),
(130407, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'SAN MIGUEL'),
(130408, 11304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'TINTORERO');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO Moran					1001305	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130501, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'ANZOATEGUI'),
(130502, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'BOLIVAR'),
(130503, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'GUARICO'),
(130504, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'HILARIO LUNA Y LUNA'),
(130505, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'HUMOCARO ALTO'),
(130506, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'HUMOCARO BAJO'),
(130507, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'LA CANDELARIA'),
(130508, 11305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'MORAN');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO PALAVECINO				1001306	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130601, 11306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'AGUA VIVA'),
(130602, 11306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CABUDARE'),
(130603, 11306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'JOSE GREGORIO BASTIDAS');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO SIMON PLANAS			1001307	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130701, 11307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'BURIA'),
(130702, 11307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'GUSTAVO VEGAS LEON'),
(130703, 11307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'SARARE');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO TORRES					1001308	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130801, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'ALTAGRACIA'),
(130802, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'ANTONIO DIAZ'),
(130803, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CAMACARO'),
(130804, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CASTAÑEDA'),
(130805, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CECILIO ZUBILLAGA'),
(130806, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'CHIQUINQUIRA'),
(130807, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'EL BLANCO'),
(130808, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'ESPINOZA LOS MONTEROS'),
(130809, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'HERIBERTO ARROYO'),
(130810, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'LARA'),
(130811, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'LAS MERCEDES'),
(130812, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'MANUEL MORILLO'),
(130813, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'MONTA A VERDE'),
(130814, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'MONTES DE OCA'),
(130815, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'REYES VARGAS'),
(130816, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'TORRES'),
(130817, 11308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'TRINIDAD SAMUEL');

/********************************************/
/* ESTADO LARA 	1000045						*/
/********************************************/
/* MUNICIPIO URDANETA				1001309	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(130901, 11309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'MOROTURO'),
(130902, 11309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'SAN MIGUEL'),
(130903, 11309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'SIQUISIQUE'),
(130904, 11309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100045, 'N', 'XAGUAS');


/********************************************************/
/********************************************************/
/* ESTADO YARACUY  1000054								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO ARISTIDES BASTIDAS		1002201	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220101, 12201, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'SAN PABLO');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO BOLIVAR				1002202	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220201, 12202, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'AROA');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO BRUZUAL				1002203	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220301, 12203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'CHIVACOA'),
(220302, 12203, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'CAMPO ELIAS');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO COCOROTE				1002204	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220401, 12204, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'COCOROTE');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO INDEPENDENCIA			1002205	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220501, 12205, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'INDEPENDENCIA');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO JOSE ANTONIO PAEZ		1002206	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220601, 12206, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'SABANA DE PARRA');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO LA TRINIDAD			1002207	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220701, 12207, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'BORAURE');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO MANUEL MONGE			1002208	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220801, 12208, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'YUMARE');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO NIRGUA					1002209	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(220901, 12209, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'NIRGUA'),
(220902, 12209, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'SALOM'),
(220903, 12209, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'TEMERLA');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO PEÑA					1002210	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(221001, 12210, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'YARITAGUA'),
(221002, 12210, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'SAN ANDRES');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO SAN FELIPE				1002211	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(221101, 12211, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'SAN FELIPE'),
(221102, 12211, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'ALBARICO'),
(221103, 12211, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'SAN JAVIER');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO SUCRE					1002212	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(221201, 12212, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'GUAMA');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO URACHICHE				1002213	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(221301, 12213, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'URACHICHE');

/********************************************/
/* ESTADO Yaracuy 	1000054					*/
/********************************************/
/* MUNICIPIO VEROES					1002214	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(221401, 12214, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'FARRIAR'),
(221402, 12214, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100054, 'N', 'EL GUAYABO');

/********************************************************/
/********************************************************/
/* ESTADO ZULIA  1000055								*/
/********************************************************/
/********************************************************/

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO ALMIRANTE PADILLA		1002301	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230101, 12301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ISLA DE TOAS'),
(230102, 12301, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MONAGAS');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO BARALT					1002302	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230201, 12302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'GENERAL URDANETA'),
(230202, 12302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LIBERTADOR'),
(230203, 12302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MANUEL GUANIPA MATOS'),
(230204, 12302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MARCELINO BRICEÑO'),
(230205, 12302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'PUEBLO NUEVO'),
(230206, 12302, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN TIMOTEO');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO CABIMAS				1002303	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230301, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'AMBROSIO'),
(230302, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ARISTIDES CALVANI'),
(230303, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CARMEN HERRERA'),
(230304, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'GERMAN RIOS LINARES'),
(230305, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'JORGE HERNANDEZ'),
(230306, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LA ROSA'),
(230307, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'PUNTA GORDA'),
(230308, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ROMULO BETANCOURT'),
(230309, 12303, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN BENITO');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO CATATUMBO				1002304	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230401, 12304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ENCONTRADOS'),
(230402, 12304, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'UDON PEREZ');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO COLON					1002305	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230501, 12305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MORALITO'),
(230502, 12305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN CARLOS DEL ZULIA'),
(230503, 12305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SANTA BARBARA'),
(230504, 12305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SANTA CRUZ DEL ZULIA'),
(230505, 12305, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'URRIBARRI');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO FRANCISCO JAVIER PULGAR 1002306*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230601, 12306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CARLOS QUEVEDO'),
(230602, 12306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'FRANCISCO J PULGAR'),
(230603, 12306, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SIMON RODRIGUEZ');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO JESUS ENRIQUE LOSADA	1002307	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230701, 12307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'JOSE RAMON YEPEZ'),
(230702, 12307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LA CONCEPCION'),
(230703, 12307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MARIANO PARRA LEON'),
(230704, 12307, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN JOSE');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO JESUS MARIIA SEMPRUN	1002308	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230801, 12308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'BARI'),
(230802, 12308, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'JESUS MARIA SEMPRUN');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO LA CAÑADA DE URDANETA	1002309	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(230901, 12309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ANDRES BELLO KM-48'),
(230902, 12309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CHIQUINQUIRA'),
(230903, 12309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CONCEPCION'),
(230904, 12309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'EL CARMELO'),
(230905, 12309, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'POTRERITOS');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO LAGUNILLAS				1002310	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231001, 12310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ALONSO DE OJEDA'),
(231002, 12310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CAMPO LARA'),
(231003, 12310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ELEAZAR LOPEZ CONTRERAS'),
(231004, 12310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LIBERTAD'),
(231005, 12310, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'VENEZUELA');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO MACHIQUES DE PERIJA	1002311	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231101, 12311, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LIBERTAD'),
(231102, 12311, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RIO NEGRO'),
(231103, 12311, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN JOSE DE PERIJA'),
(231104, 12311, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'BARTOLOME DE LAS CASAS');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO MARA					1002312	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231201, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LA SIERRITA'),
(231202, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LAS PARCELAS'),
(231203, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LUIS DE VICENTE'),
(231204, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MONSEÑOR MARCOS SERGIO GODOY'),
(231205, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RICAURTE'),
(231206, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN RAFAEL'),
(231207, 12312, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'TAMARE');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO MARACAIBO				1002313	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231301, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ANTONIO BORJAS ROMERO'),
(231302, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'BOLIVAR'),
(231303, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CACIQUE MARA'),
(231304, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CECILIO ACOSTA'),
(231305, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CHIQUINQUIRA'),
(231306, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'COQUIVACOA'),
(231307, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CRISTO DE ARANZA'),
(231308, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'FRANCISCO EUGENIO BUSTAMANTE'),
(231309, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'IDELFONZO VASQUEZ'),
(231310, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'JUANA DE AVILA'),
(231311, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LUIS HURTADO HIGUERA'),
(231312, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MANUEL DAGNINO'),
(231313, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'OLEGARIO VILLALOBOS'),
(231314, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RAUL LEONI'),
(231315, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN ISIDRO'),
(231316, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SANTA LUCIA'),
(231317, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'VENANCIO PULGAR'),
(231318, 12313, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'CARACCIOLO PARRA PEREZ');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO MIRANDA				1002314	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231401, 12314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ALTAGRACIA'),
(231402, 12314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ANA MARIA CAMPOS'),
(231403, 12314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'FARIA'),
(231404, 12314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN ANTONIO'),
(231405, 12314, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN JOSE');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO PAEZ					1002315	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231501, 12315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ALTA GUAJIRA'),
(231502, 12315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ELIAS SANCHEZ RUBIO'),
(231503, 12315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'GOAJIRA'),
(231504, 12315, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SINAMAICA');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO ROSARIO DE PERIJA		1002316	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231601, 12316, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'DONALDO GARCIA'),
(231602, 12316, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'EL ROSARIO'),
(231603, 12316, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SIXTO ZAMBRANO');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO SAN FRANCISCO			1002317	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231701, 12317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'DOMITILA FLORES'),
(231702, 12317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'EL BAJO'),
(231703, 12317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'FRANCISCO OCHOA'),
(231704, 12317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LOS CORTIJOS'),
(231705, 12317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MARCIAL HERNANDEZ'),
(231706, 12317, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SAN FRANCISCO');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO SANTA RITA				1002318	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231801, 12318, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'EL MENE'),
(231802, 12318, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'JOSE CENOVIO URRIBARRI'),
(231803, 12318, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'PEDRO LUCAS URRIBARRI'),
(231804, 12318, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'SANTA RITA');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO SIMON BOLIVAR			1002319	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(231901, 12319, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MANUEL MANRIQUE'),
(231902, 12319, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RAFAEL MARIA BARALT'),
(231903, 12319, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RAFAEL URDANETA');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO SUCRE					1002320	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(232001, 12320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'BOBURES'),
(232002, 12320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'EL BATEY'),
(232003, 12320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'GIBRALTAR'),
(232004, 12320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'HERAS'),
(232005, 12320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'MONSEÑOR ARTURO CELESTINO ALVAREZ'),
(232006, 12320, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'ROMULO GALLEGOS');

/********************************************/
/* ESTADO ZULIA 	1000055					*/
/********************************************/
/* MUNICIPIO VALMORE RODRIGUEZ		1002321	*/
/********************************************/
INSERT INTO "adempiere"."c_parish" ("c_parish_id", "c_municipality_id", "ad_client_id", "ad_org_id", "isactive", "created", "createdby", "updated", "updatedby", "c_country_id", "c_region_id", "isdefault", "name")
VALUES
(232101, 12321, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'LA VICTORIA'),
(232102, 12321, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RAFAEL URDANETA'),
(232103, 12321, 0, 0, 'Y', '2012-09-02 00:00:00', 0, '2012-09-02 00:00:00', 0, 339, 100055, 'N', 'RAUL CUENCA');



--COMIENZO DE INCLUSION DE CIUDADES DE VENEZUELA;
/********************************************************/
/********************************************************/
/* DISTRITO CAPITAL 1000033								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id)
values (101120,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Caracas',339,100033);

/********************************************************/
/********************************************************/
/* ESTADO AMAZONAS  1000034								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101121,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Esmeralda', 339,100034),
  (101122,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Fernando de Atabapo', 339,100034),
  (101123,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Puerto Ayacucho', 339,100034),
  (101124,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Isla Ratón', 339,100034),
  (101125,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Maroa', 339,100034),
  (101126,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Juan de Manapiare', 339,100034),
  (101127,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Carlos de Río Negro', 339,100034);

/********************************************************/
/********************************************************/
/* ESTADO ANZOATEGUI  1000035							*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101128,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Anaco', 339,100035),
  (101129,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Aragua de Barcelona', 339,100035),
  (101130,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Puerto Píritu', 339,100035),
  (101131,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Valle de Guanape', 339,100035),
  (101132,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pariaguán', 339,100035),
  (101133,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guanta', 339,100035),
  (101134,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Soledad', 339,100035),
  (101135,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Puerto La Cruz', 339,100035),
  (101136,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Onoto', 339,100035),
  (101137,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mapire', 339,100035),
  (101138,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Mateo', 339,100035),
  (101139,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Clarines', 339,100035),
  (101140,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cantaura', 339,100035),
  (101141,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Píritu', 339,100035),
  (101142,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San José de Guanipa (El Tigrito)', 339,100035),
  (101143,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Boca de Uchire', 339,100035),
  (101144,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Ana', 339,100035),
  (101145,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barcelona', 339,100035),
  (101146,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Tigre', 339,100035),
  (101147,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Chaparro', 339,100035),
  (101148,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Lecherías', 339,100035);
  
/********************************************************/
/********************************************************/
/* ESTADO APURE  1000036								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101149,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Achaguas', 339,100036),
  (101150,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Biruaca', 339,100036),
  (101151,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Bruzual', 339,100036),
  (101152,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guasdualito', 339,100036),
  (101153,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Juan de Payara', 339,100036),
  (101154,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Elorza', 339,100036),
  (101155,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Fernando de Apure', 339,100036);

/********************************************************/
/********************************************************/
/* ESTADO ARAGUA  1000037 								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101156,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Mateo', 339,100037),
  (101157,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Camatagua', 339,100037),
  (101158,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Maracay', 339,100037),
  (101159,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Cruz', 339,100037),
  (101160,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Victoria', 339,100037),
  (101161,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Consejo', 339,100037),
  (101162,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Palo Negro', 339,100037),
  (101163,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Limón', 339,100037),
  (101164,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Casimiro', 339,100037),
  (101165,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Sebastián', 339,100037),
  (101166,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Turmero', 339,100037),
  (101167,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Las Tejerías', 339,100037),
  (101168,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cagua', 339,100037),
  (101169,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Colonia Tovar', 339,100037),
  (101170,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barbacoas', 339,100037),
  (101171,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Villa de Cura', 339,100037),
  (101172,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Rita', 339,100037),
  (101173,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ocumare de la Costa', 339,100037);

/********************************************************/
/********************************************************/
/* ESTADO BARINAS  1000038								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101174,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sabaneta', 339,100038),
  (101175,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Socopó', 339,100038),
  (101176,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Arismendi', 339,100038),
  (101177,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barinas', 339,100038),
  (101178,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barinitas', 339,100038),
  (101179,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barrancas', 339,100038),
  (101180,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Bárbara', 339,100038),
  (101181,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Obispos', 339,100038),
  (101182,0,0,'Y','2010-10-01',0,'2010-10-01',0,'c_city Bolivia', 339,100038),
  (101183,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Libertad', 339,100038),
  (101184,0,0,'Y','2010-10-01',0,'2010-10-01',0,'c_city de Nutrias', 339,100038),
  (101185,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Cantón', 339,100038);


/********************************************************/
/********************************************************/
/* ESTADO BOLIVAR  1000039	 							*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101186,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ciudad Guayana', 339,100039),
  (101187,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Caicara del Orinoco', 339,100039),
  (101188,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Callao', 339,100039),
  (101189,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Elena de Uairén', 339,100039),
  (101190,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ciudad Bolívar', 339,100039),
  (101191,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Upata', 339,100039),
  (101192,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ciudad Piar', 339,100039),
  (101193,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guasipati', 339,100039),
  (101194,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tumeremo', 339,100039),
  (101195,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Maripa', 339,100039),
  (101196,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Palmar', 339,100039);

/********************************************************/
/********************************************************/
/* ESTADO CARABOBO  1000040 							*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101197,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Bejuma', 339,100040),
  (101198,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Güigüe', 339,100040),
  (101199,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mariara', 339,100040),
  (101200,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guacara', 339,100040),
  (101201,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Morón', 339,100040),
  (101202,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tocuyito', 339,100040),
  (101203,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Los Guayos', 339,100040),
  (101204,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Miranda', 339,100040),
  (101205,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Montalbán', 339,100040),
  (101206,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Naguanagua', 339,100040),
  (101207,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Puerto Cabello', 339,100040),
  (101208,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Diego', 339,100040),
  (101209,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Joaquín', 339,100040),
  (101210,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Valencia', 339,100040);


/********************************************************/
/********************************************************/
/* ESTADO COJEDES  1000041	 							*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101211,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cojedes', 339,100041),
  (101212,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tinaquillo', 339,100041),
  (101213,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Baúl', 339,100041),
  (101214,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Macapo', 339,100041),
  (101215,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Pao', 339,100041),
  (101216,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Libertad', 339,100041),
  (101217,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Las Vegas', 339,100041),
  (101218,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Carlos', 339,100041),
  (101219,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tinaco', 339,100041);

/********************************************************/
/********************************************************/
/* ESTADO DELTA AMACURO  1000042						*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101220,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Curiapo', 339,100042),
  (101221,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sierra Imataca', 339,100042),
  (101222,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pedernales', 339,100042),
  (101223,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tucupita', 339,100042);

/********************************************************/
/********************************************************/
/* ESTADO FALCON  1000043								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101224,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Juan de los Cayos', 339,100043),
  (101225,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Luis', 339,100043),
  (101226,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Capatárida', 339,100043),
  (101227,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Yaracal', 339,100043),
  (101228,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Punto Fijo', 339,100043),
  (101229,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Vela de Coro', 339,100043),
  (101230,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Dabajuro', 339,100043),
  (101231,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pedregal', 339,100043),
  (101232,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pueblo Nuevo', 339,100043),
  (101233,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Churuguara', 339,100043),
  (101234,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Jacura', 339,100043),
  (101235,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Cruz de Los Taques', 339,100043),
  (101236,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mene de Mauroa', 339,100043),
  (101237,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Ana de Coro', 339,100043),
  (101238,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Chichiriviche', 339,100043),
  (101239,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Palmasola', 339,100043),
  (101240,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cabure', 339,100043),
  (101241,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Píritu', 339,100043),
  (101242,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mirimire', 339,100043),
  (101243,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tucacas', 339,100043),
  (101244,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Cruz de Taratara', 339,100043),
  (101245,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tocópero', 339,100043),
  (101246,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Cruz de Bucaral', 339,100043),
  (101247,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Urumaco', 339,100043),
  (101248,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Puerto Cumarebo', 339,100043);


/********************************************************/
/********************************************************/
/* ESTADO GUARICO  1000044								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101249,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Camaguán', 339,100044),
  (101250,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Chaguaramas', 339,100044),
  (101251,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Socorro', 339,100044),
  (101252,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guayabal', 339,100044),
  (101253,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Valle de La Pascua', 339,100044),
  (101254,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Las Mercedes', 339,100044),
  (101255,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Sombrero', 339,100044),
  (101256,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Calabozo', 339,100044),
  (101257,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Altagracia de Orituco', 339,100044),
  (101258,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ortiz', 339,100044),
  (101259,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tucupido', 339,100044),
  (101260,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Juan de los Morros', 339,100044),
  (101261,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San José de Guaribe', 339,100044),
  (101262,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa María de Ipire', 339,100044),
  (101263,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Zaraza', 339,100044);

/********************************************************/
/********************************************************/
/* ESTADO LARA  1000045									*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101264,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sanare', 339,100045),
  (101265,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Duaca', 339,100045),
  (101266,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barquisimeto', 339,100045),
  (101267,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Quíbor', 339,100045),
  (101268,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Tocuyo', 339,100045),
  (101269,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cabudare', 339,100045),
  (101270,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sarare', 339,100045),
  (101271,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Carora', 339,100045),
  (101272,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Siquisique', 339,100045);

/********************************************************/
/********************************************************/
/* ESTADO MERIDA  1000046								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101273,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Vigía', 339,100046),
  (101274,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Azulita', 339,100046),
  (101275,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Cruz de Mora', 339,100046),
  (101276,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Aricagua', 339,100046),
  (101277,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Canaguá', 339,100046),
  (101278,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ejido', 339,100046),
  (101279,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tucaní', 339,100046),
  (101280,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santo Domingo', 339,100046),
  (101281,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guaraque', 339,100046),
  (101282,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Arapuey', 339,100046),
  (101283,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Torondoy', 339,100046),
  (101284,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mérida', 339,100046),
  (101285,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Timotes', 339,100046),
  (101286,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Elena de Arenales', 339,100046),
  (101287,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa María de Caparo', 339,100046),
  (101288,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pueblo Llano', 339,100046),
  (101289,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mucuchíes', 339,100046),
  (101290,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Bailadores', 339,100046),
  (101291,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tabay', 339,100046),
  (101292,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Lagunillas', 339,100046),
  (101293,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tovar', 339,100046),
  (101294,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Nueva Bolivia', 339,100046),
  (101295,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Zea', 339,100046);

/********************************************************/
/********************************************************/
/* ESTADO MIRANDA  1000047								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101296,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Caucagua', 339,100047),
  (101297,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San José de Barlovento', 339,100047),
  (101298,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Nuestra Señora del Rosario de Baruta', 339,100047),
  (101299,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Higuerote', 339,100047),
  (101300,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Mamporal', 339,100047),
  (101301,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Carrizal', 339,100047),
  (101302,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Chacao', 339,100047),
  (101303,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Charallave', 339,100047),
  (101304,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Hatillo', 339,100047),
  (101305,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Los Teques', 339,100047),
  (101306,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Teresa del Tuy', 339,100047),
  (101307,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ocumare del Tuy', 339,100047),
  (101308,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Antonio de Los Altos', 339,100047),
  (101309,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Río Chico', 339,100047),
  (101310,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Lucía', 339,100047),
  (101311,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cúpira', 339,100047),
  (101312,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guarenas', 339,100047),
  (101313,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Francisco de Yare', 339,100047),
  (101314,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Petare', 339,100047),
  (101315,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cúa', 339,100047),
  (101316,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guatire', 339,100047);


/********************************************************/
/********************************************************/
/* ESTADO MONAGAS  1000048								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101317,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Caripe', 339,100048),
  (101318,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Caicara', 339,100048),
  (101319,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Punta de Mata', 339,100048),
  (101320,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Temblador', 339,100048),
  (101321,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Maturín', 339,100048),
  (101322,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Aragua', 339,100048),
  (101323,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Quiriquire', 339,100048),
  (101324,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Bárbara', 339,100048),
  (101325,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Barrancas', 339,100048),
  (101326,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Uracoa', 339,100048),
  (101327,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Antonio', 339,100048),
  (101328,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Aguasay', 339,100048),
  (101329,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Caripito', 339,100048);


/********************************************************/
/********************************************************/
/* ESTADO NUEVA ESPARTA 1000049							*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101330,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Valle del Espíritu Santo', 339,100049),
  (101331,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Ana', 339,100049),
  (101332,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pampatar', 339,100049),
  (101333,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Plaza de Paraguachí', 339,100049),
  (101334,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Asunción', 339,100049),
  (101335,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Juan Bautista', 339,100049),
  (101336,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Juangriego', 339,100049),
  (101337,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Porlamar', 339,100049),
  (101338,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Boca del Río', 339,100049),
  (101339,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Punta de Piedras', 339,100049),
  (101340,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Pedro de Coche', 339,100049);


/********************************************************/
/********************************************************/
/* ESTADO PORTUGUESA  1000045							*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101341,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Agua Blanca', 339,100050),
  (101342,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Araure', 339,100050),
  (101343,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Píritu', 339,100050),
  (101344,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guanare', 339,100050),
  (101345,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guanarito', 339,100050),
  (101346,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Paraíso de Chabasquén', 339,100050),
  (101347,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ospino', 339,100050),
  (101348,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Acarigua', 339,100050),
  (101349,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Papelón', 339,100050),
  (101350,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Boconoito', 339,100050),
  (101351,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Rafael de Onoto', 339,100050),
  (101352,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Playón', 339,100050),
  (101353,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Biscucuy', 339,100050),
  (101354,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Villa Bruzual', 339,100050);


/********************************************************/
/********************************************************/
/* ESTADO SUCRE  1000051								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101355,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Casanay', 339,100051),
  (101356,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San José de Aerocuar', 339,100051),
  (101357,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Río Caribe', 339,100051),
  (101358,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Pilar', 339,100051),
  (101359,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Carúpano', 339,100051),
  (101360,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Marigüitar', 339,100051),
  (101361,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Yaguaraparo', 339,100051),
  (101362,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Araya', 339,100051),
  (101363,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tunapuy', 339,100051),
  (101364,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Irapa', 339,100051),
  (101365,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Antonio del Golfo', 339,100051),
  (101366,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cumanacoa', 339,100051),
  (101367,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cariaco', 339,100051),
  (101368,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cumaná', 339,100051),
  (101369,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Güiria', 339,100051);

/********************************************************/
/********************************************************/
/* ESTADO TACHIRA  1000052								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101370,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cordero', 339,100052),
  (101371,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Las Mesas', 339,100052),
  (101372,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Colón', 339,100052),
  (101373,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Antonio del Táchira', 339,100052),
  (101374,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Táriba', 339,100052),
  (101375,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Ana', 339,100052),
  (101376,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Rafael del Piñal', 339,100052),
  (101377,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San José de Bolívar', 339,100052),
  (101378,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Fría', 339,100052),
  (101379,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Palmira', 339,100052),
  (101380,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Capacho Nuevo', 339,100052),
  (101381,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Grita', 339,100052),
  (101382,0,0,'Y','2010-10-01',0,'2010-10-01',0,'EL Cobre', 339,100052),
  (101383,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Rubio', 339,100052),
  (101384,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Capacho Viejo', 339,100052),
  (101385,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Abejales', 339,100052),
  (101386,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Lobatera', 339,100052),
  (101387,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Michelena', 339,100052),
  (101388,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Coloncito', 339,100052),
  (101389,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ureña', 339,100052),
  (101390,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Delicias', 339,100052),
  (101391,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Tendida', 339,100052),
  (101392,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Cristóbal', 339,100052),
  (101393,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Seboruco', 339,100052),
  (101394,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Simón', 339,100052),
  (101395,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Queniquea', 339,100052),
  (101396,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Josecito', 339,100052),
  (101397,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pregonero', 339,100052),
  (101398,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Umuquena', 339,100052);


/********************************************************/
/********************************************************/
/* ESTADO TRUJILLO  1000053								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101399,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Isabel', 339,100053),
  (101400,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Boconó', 339,100053),
  (101401,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sabana Grande', 339,100053),
  (101402,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Chejendé', 339,100053),
  (101403,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Carache', 339,100053),
  (101404,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Escuque', 339,100053),
  (101405,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Paradero', 339,100053),
  (101406,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Campo Elías', 339,100053),
  (101407,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Apolonia', 339,100053),
  (101408,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Dividive', 339,100053),
  (101409,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Monte Carmelo', 339,100053),
  (101410,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Motatán', 339,100053),
  (101411,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pampán', 339,100053),
  (101412,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pampanito', 339,100053),
  (101413,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Betijoque', 339,100053),
  (101414,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Carvajal', 339,100053),
  (101415,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sabana de Mendoza', 339,100053),
  (101416,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Trujillo', 339,100053),
  (101417,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Quebrada', 339,100053),
  (101418,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Valera', 339,100053);


/********************************************************/
/********************************************************/
/* ESTADO YARACUY  1000054								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101419,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Pablo', 339,100054),
  (101420,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Aroa', 339,100054),
  (101421,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Chivacoa', 339,100054),
  (101422,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cocorote', 339,100054),
  (101423,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Independencia', 339,100054),
  (101424,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sabana de Parra', 339,100054),
  (101425,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Boraure', 339,100054),
  (101426,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Yumare', 339,100054),
  (101427,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Nirgua', 339,100054),
  (101428,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Yaritagua', 339,100054),
  (101429,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Felipe', 339,100054),
  (101430,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Guama', 339,100054),
  (101431,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Urachiche', 339,100054),
  (101432,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Farriar', 339,100054);


/********************************************************/
/********************************************************/
/* ESTADO ZULIA  1000055								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101433,0,0,'Y','2010-10-01',0,'2010-10-01',0,'El Toro', 339,100055),
  (101434,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Timoteo', 339,100055),
  (101435,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Cabimas', 339,100055),
  (101436,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Encontrados', 339,100055),
  (101437,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Carlos del Zulia', 339,100055),
  (101438,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Pueblo Nuevo El Chivo', 339,100055),
  (101439,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Concepción', 339,100055),
  (101440,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Casigua El Cubo', 339,100055),
  (101441,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Concepción', 339,100055),
  (101442,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Ciudad Ojeda', 339,100055),
  (101443,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Machiques', 339,100055),
  (101444,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Rafael de El Moján', 339,100055),
  (101445,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Maracaibo', 339,100055),
  (101446,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Los Puertos de Altagracia', 339,100055),
  (101447,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Sinamaica', 339,100055),
  (101448,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Villa del Rosario', 339,100055),
  (101449,0,0,'Y','2010-10-01',0,'2010-10-01',0,'San Francisco', 339,100055),
  (101450,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Santa Rita', 339,100055),
  (101451,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Tía Juana', 339,100055),
  (101452,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Bobures', 339,100055),
  (101453,0,0,'Y','2010-10-01',0,'2010-10-01',0,'Bachaquero', 339,100055);

/********************************************************/
/********************************************************/
/* ESTADO VARGAS  1000056								*/
/********************************************************/
/********************************************************/
insert into "adempiere"."c_city" (c_city_id, ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, name, c_country_id, c_region_id) 
values
  (101454,0,0,'Y','2010-10-01',0,'2010-10-01',0,'La Guaira', 339,100056);
--FIN DE INCLUSION DE CIUDADES DE VENEZUELA;




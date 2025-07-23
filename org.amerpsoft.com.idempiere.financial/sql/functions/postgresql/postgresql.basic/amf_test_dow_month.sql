-- TEST Functions
-- adempiere.amf_dow2letter
-- Spanish UPPER
-- Parameters (conv_dow character, upp_low character, languaje_iso character)
SELECT adempiere.amf_dow2letter('0','U', 'es') AS day_0, adempiere.amf_dow2letter('1','U', 'es') AS day_1, adempiere.amf_dow2letter('2','U', 'es') AS day_2,
adempiere.amf_dow2letter('3','U', 'es') AS day_3, adempiere.amf_dow2letter('4','U', 'es') AS day_4, adempiere.amf_dow2letter('5','U', 'es') AS day_5,
adempiere.amf_dow2letter('6','U', 'es') AS day_6
UNION 
-- Spanish LOWER
SELECT adempiere.amf_dow2letter('0','L', 'es') AS day_0, adempiere.amf_dow2letter('1','L', 'es') AS day_1, adempiere.amf_dow2letter('2','L', 'es') AS day_2,
adempiere.amf_dow2letter('3','L', 'es') AS day_3, adempiere.amf_dow2letter('4','L', 'es') AS day_4, adempiere.amf_dow2letter('5','L', 'es') AS day_5,
adempiere.amf_dow2letter('6','L', 'es') AS day_6
UNION 
-- English UPPER
SELECT adempiere.amf_dow2letter('0','U', 'en') AS day_0, adempiere.amf_dow2letter('1','U', 'en') AS day_1, adempiere.amf_dow2letter('2','U', 'en') AS day_2,
adempiere.amf_dow2letter('3','U', 'en') AS day_3, adempiere.amf_dow2letter('4','U', 'en') AS day_4, adempiere.amf_dow2letter('5','U', 'en') AS day_5,
adempiere.amf_dow2letter('6','U', 'en') AS day_6
UNION 
-- English LOWER
SELECT adempiere.amf_dow2letter('0','L', 'en') AS day_0, adempiere.amf_dow2letter('1','L', 'en') AS day_1, adempiere.amf_dow2letter('2','L', 'en') AS day_2,
adempiere.amf_dow2letter('3','L', 'en') AS day_3, adempiere.amf_dow2letter('4','L', 'en') AS day_4, adempiere.amf_dow2letter('5','L', 'en') AS day_5,
adempiere.amf_dow2letter('6','L', 'en') AS day_6;

--adempiere.amf_month2letter(character, character, character)
-- Parameters conv_month character, upp_low character, languaje_iso character)
-- Spanish UPPER
SELECT adempiere.amf_month2letter('01','U', 'es') AS month_01,adempiere.amf_month2letter('02','U', 'es') AS month_02,adempiere.amf_month2letter('03','U', 'es') AS month_03,
adempiere.amf_month2letter('04','U', 'es') AS month_04,adempiere.amf_month2letter('05','U', 'es') AS month_05,adempiere.amf_month2letter('06','U', 'es') AS month_06,
adempiere.amf_month2letter('07','U', 'es') AS month_07,adempiere.amf_month2letter('08','U', 'es') AS month_08,adempiere.amf_month2letter('09','U', 'es') AS month_09,
adempiere.amf_month2letter('10','U', 'es') AS month_10,adempiere.amf_month2letter('11','U', 'es') AS month_11,adempiere.amf_month2letter('12','U', 'es') AS month_12
UNION
-- Spanish LOWER
SELECT adempiere.amf_month2letter('01','L', 'es') AS month_01,adempiere.amf_month2letter('02','L', 'es') AS month_02,adempiere.amf_month2letter('03','L', 'es') AS month_03,
adempiere.amf_month2letter('04','L', 'es') AS month_04,adempiere.amf_month2letter('05','L', 'es') AS month_05,adempiere.amf_month2letter('06','L', 'es') AS month_06,
adempiere.amf_month2letter('07','L', 'es') AS month_07,adempiere.amf_month2letter('08','L', 'es') AS month_08,adempiere.amf_month2letter('09','L', 'es') AS month_09,
adempiere.amf_month2letter('10','L', 'es') AS month_10,adempiere.amf_month2letter('11','L', 'es') AS month_11,adempiere.amf_month2letter('12','L', 'es') AS month_12
UNION
-- English UPPER
SELECT adempiere.amf_month2letter('01','U', 'en') AS month_01,adempiere.amf_month2letter('02','U', 'en') AS month_02,adempiere.amf_month2letter('03','U', 'en') AS month_03,
adempiere.amf_month2letter('04','U', 'en') AS month_04,adempiere.amf_month2letter('05','U', 'en') AS month_05,adempiere.amf_month2letter('06','U', 'en') AS month_06,
adempiere.amf_month2letter('07','U', 'en') AS month_07,adempiere.amf_month2letter('08','U', 'en') AS month_08,adempiere.amf_month2letter('09','U', 'en') AS month_09,
adempiere.amf_month2letter('10','U', 'en') AS month_10,adempiere.amf_month2letter('11','U', 'en') AS month_11,adempiere.amf_month2letter('12','U', 'en') AS month_12
UNION
-- English LOWER
SELECT adempiere.amf_month2letter('01','L', 'en') AS month_01,adempiere.amf_month2letter('02','L', 'en') AS month_02,adempiere.amf_month2letter('03','L', 'en') AS month_03,
adempiere.amf_month2letter('04','L', 'en') AS month_04,adempiere.amf_month2letter('05','L', 'en') AS month_05,adempiere.amf_month2letter('06','L', 'en') AS month_06,
adempiere.amf_month2letter('07','L', 'en') AS month_07,adempiere.amf_month2letter('08','L', 'en') AS month_08,adempiere.amf_month2letter('09','L', 'en') AS month_09,
adempiere.amf_month2letter('10','L', 'en') AS month_10,adempiere.amf_month2letter('11','L', 'en') AS month_11,adempiere.amf_month2letter('12','L', 'en') AS month_12





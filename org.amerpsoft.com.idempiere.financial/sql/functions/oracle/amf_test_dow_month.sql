-- TEST Functions
-- adempiere.amf_dow2letter
-- Spanish UPPER
-- Parameters (conv_dow character, upp_low character, languaje_iso character)
SELECT adempiere.amf_dow2letter('0','U', 'es') day_0, adempiere.amf_dow2letter('1','U', 'es')  day_1, adempiere.amf_dow2letter('2','U', 'es')  day_2,
adempiere.amf_dow2letter('3','U', 'es')  day_3, adempiere.amf_dow2letter('4','U', 'es')  day_4, adempiere.amf_dow2letter('5','U', 'es')  day_5,
adempiere.amf_dow2letter('6','U', 'es')  day_6 FROM DUAL
UNION 
-- Spanish LOWER
SELECT adempiere.amf_dow2letter('0','L', 'es')  day_0, adempiere.amf_dow2letter('1','L', 'es')  day_1, adempiere.amf_dow2letter('2','L', 'es')  day_2,
adempiere.amf_dow2letter('3','L', 'es')  day_3, adempiere.amf_dow2letter('4','L', 'es')  day_4, adempiere.amf_dow2letter('5','L', 'es')  day_5,
adempiere.amf_dow2letter('6','L', 'es')  day_6 FROM DUAL
UNION 
-- English UPPER
SELECT adempiere.amf_dow2letter('0','U', 'en')  day_0, adempiere.amf_dow2letter('1','U', 'en')  day_1, adempiere.amf_dow2letter('2','U', 'en')  day_2,
adempiere.amf_dow2letter('3','U', 'en')  day_3, adempiere.amf_dow2letter('4','U', 'en')  day_4, adempiere.amf_dow2letter('5','U', 'en')  day_5,
adempiere.amf_dow2letter('6','U', 'en')  day_6 FROM DUAL
UNION 
-- English LOWER
SELECT adempiere.amf_dow2letter('0','L', 'en')  day_0, adempiere.amf_dow2letter('1','L', 'en')  day_1, adempiere.amf_dow2letter('2','L', 'en')  day_2,
adempiere.amf_dow2letter('3','L', 'en')  day_3, adempiere.amf_dow2letter('4','L', 'en')  day_4, adempiere.amf_dow2letter('5','L', 'en')  day_5,
adempiere.amf_dow2letter('6','L', 'en')  day_6 FROM DUAL;

--adempiere.amf_month2letter(character, character, character)
-- Parameters conv_month character, upp_low character, languaje_iso character)
-- Spanish UPPER
SELECT adempiere.amf_month2letter('01','U', 'es')  month_01,adempiere.amf_month2letter('02','U', 'es')  month_02,adempiere.amf_month2letter('03','U', 'es')  month_03,
adempiere.amf_month2letter('04','U', 'es')  month_04,adempiere.amf_month2letter('05','U', 'es')  month_05,adempiere.amf_month2letter('06','U', 'es')  month_06,
adempiere.amf_month2letter('07','U', 'es')  month_07,adempiere.amf_month2letter('08','U', 'es')  month_08,adempiere.amf_month2letter('09','U', 'es')  month_09,
adempiere.amf_month2letter('10','U', 'es')  month_10,adempiere.amf_month2letter('11','U', 'es')  month_11,adempiere.amf_month2letter('12','U', 'es')  month_12
FROM DUAL
UNION
-- Spanish LOWER
SELECT adempiere.amf_month2letter('01','L', 'es')  month_01,adempiere.amf_month2letter('02','L', 'es')  month_02,adempiere.amf_month2letter('03','L', 'es')  month_03,
adempiere.amf_month2letter('04','L', 'es')  month_04,adempiere.amf_month2letter('05','L', 'es')  month_05,adempiere.amf_month2letter('06','L', 'es')  month_06,
adempiere.amf_month2letter('07','L', 'es')  month_07,adempiere.amf_month2letter('08','L', 'es')  month_08,adempiere.amf_month2letter('09','L', 'es')  month_09,
adempiere.amf_month2letter('10','L', 'es')  month_10,adempiere.amf_month2letter('11','L', 'es')  month_11,adempiere.amf_month2letter('12','L', 'es')  month_12
FROM DUAL
UNION
-- English UPPER
SELECT adempiere.amf_month2letter('01','U', 'en')  month_01,adempiere.amf_month2letter('02','U', 'en')  month_02,adempiere.amf_month2letter('03','U', 'en')  month_03,
adempiere.amf_month2letter('04','U', 'en')  month_04,adempiere.amf_month2letter('05','U', 'en')  month_05,adempiere.amf_month2letter('06','U', 'en')  month_06,
adempiere.amf_month2letter('07','U', 'en')  month_07,adempiere.amf_month2letter('08','U', 'en')  month_08,adempiere.amf_month2letter('09','U', 'en')  month_09,
adempiere.amf_month2letter('10','U', 'en')  month_10,adempiere.amf_month2letter('11','U', 'en')  month_11,adempiere.amf_month2letter('12','U', 'en')  month_12
FROM DUAL
UNION
-- English LOWER
SELECT adempiere.amf_month2letter('01','L', 'en')  month_01,adempiere.amf_month2letter('02','L', 'en')  month_02,adempiere.amf_month2letter('03','L', 'en')  month_03,
adempiere.amf_month2letter('04','L', 'en')  month_04,adempiere.amf_month2letter('05','L', 'en')  month_05,adempiere.amf_month2letter('06','L', 'en')  month_06,
adempiere.amf_month2letter('07','L', 'en')  month_07,adempiere.amf_month2letter('08','L', 'en')  month_08,adempiere.amf_month2letter('09','L', 'en')  month_09,
adempiere.amf_month2letter('10','L', 'en')  month_10,adempiere.amf_month2letter('11','L', 'en')  month_11,adempiere.amf_month2letter('12','L', 'en')  month_12
FROM DUAL ;


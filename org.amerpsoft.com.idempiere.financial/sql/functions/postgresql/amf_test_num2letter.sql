-- TEST Functions
--PARAMETROS adempiere.amf_num2letter(conv_number numeric, upp_low character, languaje_iso character)
SELECT 1 AS number2conv, adempiere.amf_num2letter(1, 'U', 'es') AS number_Converted
UNION
SELECT 1 AS number2conv, adempiere.amf_num2letter(1, 'U', 'en') AS number_Converted
UNION
SELECT 1 AS number2conv, adempiere.amf_num2letter(1, 'L', 'es') AS number_Converted
UNION
SELECT 1 AS number2conv, adempiere.amf_num2letter(1, 'L', 'en') AS number_Converted
UNION
SELECT 111 AS number2conv, adempiere.amf_num2letter(111, 'U', 'es') AS number_Converted
UNION
SELECT 111 AS number2conv, adempiere.amf_num2letter(111, 'U', 'en') AS number_Converted
UNION
SELECT 111 AS number2conv, adempiere.amf_num2letter(111, 'L', 'es') AS number_Converted
UNION
SELECT 111 AS number2conv, adempiere.amf_num2letter(111, 'L', 'en') AS number_Converted
UNION
SELECT 222222 AS number2conv, adempiere.amf_num2letter(222222, 'U', 'es') AS number_Converted
UNION
SELECT 222222 AS number2conv, adempiere.amf_num2letter(222222, 'U', 'en') AS number_Converted
UNION
SELECT 222222 AS number2conv, adempiere.amf_num2letter(222222, 'L', 'es') AS number_Converted
UNION
SELECT 222222 AS number2conv, adempiere.amf_num2letter(222222, 'L', 'en') AS number_Converted
UNION
SELECT 5678975 AS number2conv, adempiere.amf_num2letter(5678975, 'U', 'es') AS number_Converted
UNION
SELECT 5678975 AS number2conv, adempiere.amf_num2letter(5678975, 'U', 'en') AS number_Converted
UNION
SELECT 5678975 AS number2conv, adempiere.amf_num2letter(5678975, 'L', 'es') AS number_Converted
UNION
SELECT 5678975 AS number2conv, adempiere.amf_num2letter(5678975, 'L', 'en') AS number_Converted
UNION
SELECT 32455678975 AS number2conv, adempiere.amf_num2letter(32455678975, 'U', 'es') AS number_Converted
UNION
SELECT 32455678975 AS number2conv, adempiere.amf_num2letter(32455678975, 'U', 'en') AS number_Converted
UNION
SELECT 32455678975 AS number2conv, adempiere.amf_num2letter(32455678975, 'L', 'es') AS number_Converted
UNION
SELECT 32455678975 AS number2conv, adempiere.amf_num2letter(32455678975, 'L', 'en') AS number_Converted
UNION
SELECT 245367678975 AS number2conv, adempiere.amf_num2letter(245367678975, 'U', 'es') AS number_Converted
UNION
SELECT 245367678975 AS number2conv, adempiere.amf_num2letter(245367678975, 'U', 'en') AS number_Converted
UNION
SELECT 245367678975 AS number2conv, adempiere.amf_num2letter(245367678975, 'L', 'es') AS number_Converted
UNION
SELECT 245367678975 AS number2conv, adempiere.amf_num2letter(245367678975, 'L', 'en') AS number_Converted
ORDER BY number2conv

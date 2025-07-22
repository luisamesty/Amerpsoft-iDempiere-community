-- Pruebas de funciones
-- amf_element_value_tree_extended
-- AD_Clien_ID =1000000
-- C_AcctSchema_ID = 10000000
-- BASIC
SELECT * FROM amf_element_value_tree_basic($P{AD_Client_ID}, $P{C_AcctSchema_ID});
-- EXTENDED
SELECT * FROM amf_element_value_tree_extended($P{AD_Client_ID}, $P{C_AcctSchema_ID});

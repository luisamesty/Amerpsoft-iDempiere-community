
SELECT 
-- PARAMETERS:
--	p_salary_type character varying, 
--	p_employee_id numeric, 
--	p_startdate timestamp without time zone, 
--	p_enddate timestamp without time zone, 
--	p_currency_id numeric, 
--	p_conversiontype_id numeric)
-- String p_concept_type options:
--  'salary_base': Salary all concepts with salary check
--  'salary_gravable': Salary all concepts with arc check for NN, NU and NV
--  'salary_integral': Salary 
--  'salary_vacation': Salary all concepts with vacacion check  and NN
--  'salary_utilities': Salary all concepts with utilities check and NN, NV
--  'salary_utilities_nn': Salary all concepts with utilidad check and NN only
--  'salary_utilities_nv': Salary all concepts with utilidad check and NV only
--  'salary_socialbenefits': Salary all concepts with  prestacion
--  'salary_socialbenefits_nn': Salary all concepts with  prestacion and NN
--  'salary_socialbenefits_nv': Salary all concepts with  prestacion and  NV
--  'salary_socialbenefits_nu': Salary all concepts with  prestacion and NU
amp_salary_hist_calc( 'salary_base', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_base,
amp_salary_hist_calc( 'salary_gravable', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_gravable,
amp_salary_hist_calc( 'salary_integral', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_integral,
amp_salary_hist_calc( 'salary_vacation', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_vacation,
amp_salary_hist_calc( 'salary_utilities', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_utilities,
amp_salary_hist_calc( 'salary_utilities_nn', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_utilities_nn,
amp_salary_hist_calc( 'salary_utilities_nv', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_utilities_nv,
amp_salary_hist_calc( 'salary_socialbenefits', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_socialbenefits,
amp_salary_hist_calc( 'salary_socialbenefits_nn', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_socialbenefits_nu,
amp_salary_hist_calc( 'salary_socialbenefits_nv', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_socialbenefits_nv,
amp_salary_hist_calc( 'salary_socialbenefits_nu', 1038890, '2024-12-01', '2025-11-30', 344, 114 ) as salary_socialbenefits_nu

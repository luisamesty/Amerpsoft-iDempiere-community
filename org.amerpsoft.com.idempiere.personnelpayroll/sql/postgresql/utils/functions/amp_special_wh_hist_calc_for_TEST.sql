SELECT 
-- 	 salario: Salary
--   arc: ARC Tax Gravable
--   sso: Social Security Basic
--   spf: Social Security Additional for unemployment
--   vacacion: Vacation
--   prestacion: Social benefits
--   utilidad: End of year bonus
--   feriado: Hollidays extra
--   ince:  Educational Law
--   faov:  Housing Funds
amp_special_wh_hist_calc( 'salario', 'deducted','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as salario,
amp_special_wh_hist_calc( 'arc', 'calculated',null, 1000025, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as arc,
amp_special_wh_hist_calc( 'sso', 'calculated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as sso,
amp_special_wh_hist_calc( 'spf', 'calculated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as spf,
amp_special_wh_hist_calc( 'vacacion', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as vacacion,
amp_special_wh_hist_calc( 'prestacion', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as prestacion,
amp_special_wh_hist_calc( 'utilidad', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as utilidad,
amp_special_wh_hist_calc( 'feriado', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as feriado,
amp_special_wh_hist_calc( 'ince', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as ince,
amp_special_wh_hist_calc( 'faov', 'allocated','NN', 1000016, '2019-01-01', '2019-12-31', 1000000, 1000002 ) as faov




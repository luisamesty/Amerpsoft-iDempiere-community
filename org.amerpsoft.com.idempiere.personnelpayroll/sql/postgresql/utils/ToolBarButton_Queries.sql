-- USE This Query to Select ToolBar Button Processes
SELECT at2.ad_toolbarbutton_id, win.name AS  window_name,  ata.name AS tab_name, ap."name" AS process_name
FROM adempiere.ad_toolbarbutton at2 
INNER JOIN adempiere.ad_process ap ON ap.ad_process_id =at2.ad_process_id 
INNER JOIN adempiere.ad_tab ata ON ata.ad_tab_id = at2.ad_tab_id 
INNER JOIN adempiere.ad_window win ON win.ad_window_id = ata.ad_window_id 
WHERE win.name LIKE '%Personnel%' AND win.isactive = 'Y'
ORDER BY at2.ad_toolbarbutton_id, win.name, ata.name

-- IDs May change between Developmnent Databases
SELECT 	* FROM 	AD_ToolBarButton WHERE 	AD_ToolBarButton_ID IN 
(1000000,1000112,1000115,1000117,1000118,1000119,1000121,1000177,1000178,1000179,1000180,1000181,1000182,1000183,1000184,1000185,1000186,1000187,1000188,1000189,1000190,1000191,1000192,1000193,1000194,1000195,1000196,1000197,1000198,1000199,1000200,1000201,1000202,1000203,1000204,1000205,1000206,1000207,1000208,1000209,1000210,1000211,1000212,1000213,1000214,1000215,1000216,1000217,1000218,1000219,1000220,1000221,1000222,1000223)


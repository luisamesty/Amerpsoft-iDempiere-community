-- USE This Query to Select ToolBar Button Processes
SELECT at2.ad_toolbarbutton_id, win.name AS  window_name,  ata.name AS tab_name, ap."name" AS process_name
FROM adempiere.ad_toolbarbutton at2 
INNER JOIN adempiere.ad_process ap ON ap.ad_process_id =at2.ad_process_id 
INNER JOIN adempiere.ad_tab ata ON ata.ad_tab_id = at2.ad_tab_id 
INNER JOIN adempiere.ad_window win ON win.ad_window_id = ata.ad_window_id 
WHERE ap.name LIKE '%Amtools%' AND win.isactive = 'Y'
ORDER BY at2.ad_toolbarbutton_id, win.name, ata.name


-- IDs May change between Developmnent Databases
SELECT 	* FROM 	AD_ToolBarButton WHERE 	AD_ToolBarButton_ID IN 
(1000232,1000233,1000234)



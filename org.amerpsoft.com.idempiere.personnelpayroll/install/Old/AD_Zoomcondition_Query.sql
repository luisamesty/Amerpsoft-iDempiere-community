select 
z.name, t.ad_table_id, t.tablename, t.name as table_description, w.name as window_name
FROM AD_zoomcondition z
 JOIN AD_table t ON t.AD_table_ID=z.AD_table_ID
 JOIN ad_window w ON z.AD_Window_ID = w.AD_Window_ID
ORDER BY Tablename

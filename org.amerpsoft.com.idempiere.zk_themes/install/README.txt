Installation Procedure: 
1. Install Jar Plugin available on p2 site target plugins
	org.amerpsoft.com.idempiere.zk_themes_X.X.0.XXXXXXXXXX.jar
2. Pack in ‘AMERPSOFT Themes.zip’  Select Oracle/Postgresql
    Sys Config Variables 

IF SOMETHING GO WRONG:
REPLACE AD_Sysconfig Record (AD_SysConfig_ID=200021)
Name:ZK_THEME
Value: default
This will put default Idempiere Standard ZK Theme


This values must be set with Plugin Pack-IN
'ZK_THEME',
'ZK_LOGO_LARGE',
'ZK_LOGO_SMALL',
'ZK_BROWSER_TITLE',
'ZK_BROWSER_ICON',
'ZK_THEME_USE_FONT_ICON_FOR_IMAGE'

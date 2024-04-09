&lArr;[AMERPSOFT COMMUNITY PLUGINS](../README.md) | [Home](../README.md)
## <b>AMERPSOFT ZK_Themes</b>
This tutorial is brought to you by Luis Amesty from: [Amerpsoft Consulting](http://amerpsoft.com/index.php/en/). For any question or improvement see me at: [Idempiere WIKI User: Luis Amesty](https://wiki.idempiere.org/en/User:Luisamesty)

Github Project Home: [Amerpsoft-iDempiere-community](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/README.md)

## <b>Description</b>


AMERPSOFT ZK_Themes is a collection of themes from different sources and contributors. 
Joining on a single fragment plugin. Tested on idempiere versions 8.2.
Using the new standard for Theme managent on Idempiere 8.2 as stablished with IDEMPIERE-4421

Based on IDEMPIERE-4421, Some Theme Migration Notes:

Theme resources reside at:
src/web/theme/THEME_NAME 
instead of /theme/THEME_NAME

Within the theme folder, reference to other theme resources must use full path (with “~./” prefix) instead of relative path. 
For e.g, at:
theme/default/zul/login/login-left.zul
The macroURI value is change from vendor-logo.zul
to:
~./theme/default/zul/login/vendor-logo.zul


Same goes for resource reference at *.css.dsp. 
For e.g, at theme/default/css/fragment/grid.css.dsp background-image: url(${c:encodeURL('/theme/default/images/EditRecord16.png')}) 
is replace with:
background-image: url(${c:encodeURL('~./theme/default/images/EditRecord16.png')}). 
Note the added “~./” prefix.
</pre>

<b>*** YOU MAY USE this plugin for idempiere Version 8.2 or Newer *** 


## <b>Features</b>

* <b>Amtheme:</b> Standard Theme similar to ferrythem.
See FerryTheme from Hiep LQ on:
http://wiki.idempiere.org/en/Plugin:_Theme_ferrytheme

* <b>Amthemeblu:</b> Blue Theme, Bigger Icons.

* <b>Ksys Kbs:</b>
Maintainer: Ken_longnan - [ken.longnan@gmail.com]
Status: Beta, up to date with release 5.1
License: GPLv2
Sources: ksys-idempiere-theme

* <b>BusinessTeam and BusinessTeamBlue:</b>
Maintainer: User: PabloValdivia

* <b>Font Icons are supported and tested</b>
Developer: Heng Sin Low , Idempiere 6.2 New Features.
Configuration is simply to set the System Configurator flag ZK_THEME_USE_FONT_ICON_FOR_IMAGE = Y, and then execute a Cache Reset.

* <b>optumTheme:</b>
Maintainer: Optum-OGS
[optumTheme](https://github.com/Optum-OGS/iDempiereOptumTheme)

* <b>iceblue_c:</b>
Maintainer: Heng Sin
[iceblue_c](https://github.com/hengsin/idempiere-examples)

* <b>Updated for idempiere version 8.2</b>

## <b>Documentation</b>


See <b>amerp_themes.docx</b>   or   <b>amerp_themes.pdf</b>


[AMERPSOFT Themes - amerp_themes.pdf ](./documentation/amerp_themes.pdf)

## <b>Installation Procedure</b>

#### <b>Install Jar Plugin </b>
    Available on p2 site target plugins
    org.amerpsoft.com.idempiere.themes-com_X.X.0.XXXXXXXXXX.jar


#### <b>Pack in ‘AMERPSOFT Themes.zip’</b>

    Once plugin is running, then Pack-In:

    <b>'AMERPSOFT Themes.zip'</b>

    It contains SysConfig Variables:
    - ZK_THEME 
    - ZK_LOGO_LARGE
    - ZK_LOGO_SMALL
    - ZK_BROWSER_TITLE
    - ZK_BROWSER_ICON
    - ZK_THEME_USE_FONT_ICON_FOR_IMAGE 
    
    SET ZK_THEME_USE_FONT_ICON_FOR_IMAGE to 'N'
    
#### <b>IF SOMETHING GO WRONG</b>

    REPLACE AD_Sysconfig Record (AD_SysConfig_ID=200021)

    Name:ZK_THEME

    Value: default

    This will put default Idempiere Standard ZK Theme
    
    SQL Commands: 
    UPDATE ad_sysconfig SET value ='default' WHERE AD_SysConfig_ID=200021;
    COMMIT;




#### Release Notes:

- Updated for Idempiere release 8.2 - March 2021
- Updated for Idempiere release 11 - January 2024
- Under Test - See release-11 branch.
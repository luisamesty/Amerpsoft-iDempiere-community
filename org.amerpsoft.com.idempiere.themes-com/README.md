# Welcome to Amerpsoft-Themes!

## <b>AMERPSOFT Themes</b>
This tutorial is brought to you by Luis Amesty from:
[Amerpsoft Consulting](http://amerpsoft.com/index.php/en/)

For any question or improvement see me at:

[Idempiere WIKI User Luis Amesty](https://wiki.idempiere.org/en/User:Luisamesty)

[LinkedIn: Luis Amesty](https://www.linkedin.com/in/luisamesty/)

[email](luisamesty @ gmail.com) to me. <br/>

## <b>Description</b>


AMERPSOFT Themes is a collection of themes from different sources and contributors. 
Joining on a single fragment plugin. Tested on idempiere versions 5.1,  6.2 and 7.1.

<b>***  DO NOT USE this plugin for Version 8.2 *** </b>
<b>USE New one ZK_THEMES.</b>

Github Project Home: [Amerpsoft-iDempiere-community](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/README.md)

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

* <b>Updated for idempiere version 7.1</b>

## <b>Documentation</b>


See <b>amerp_themes.docx</b>   or   <b>amerp_themes.pdf</b>


[AMERPSOFT Themes - amerp_themes.pdf ](https://github.com/luisamesty/Amerpsoft-iDempiere-community/blob/master/org.amerpsoft.com.idempiere.themes-com/documentation/amerp_themes.pdf)

## <b>Installation Procedure</b>

1. <b>Install Jar Plugin </b>
    Available on p2 site target plugins
    org.amerpsoft.com.idempiere.themes-com_X.X.0.XXXXXXXXXX.jar


2. <b>Pack in ‘AMERPSOFT Themes.zip’</b>

    Once plugin is running, then Pack-In:

    <b>'AMERPSOFT Themes.zip'</b>

    It contains SysConfig Variables:
    - ZK_THEME 
    - ZK_LOGO_LARGE
    - ZK_LOGO_SMALL
    - ZK_BROWSER_TITLE
    - ZK_BROWSER_ICON
    - ZK_THEME_USE_FONT_ICON_FOR_IMAGE 
    
3. <b>IF SOMETHING GO WRONG</b>

    REPLACE AD_Sysconfig Record (AD_SysConfig_ID=200021)

    Name:ZK_THEME

    Value: default

    This will put default Idempiere Standard ZK Theme

   Updated for Idempiere release 8.1 - November 2020
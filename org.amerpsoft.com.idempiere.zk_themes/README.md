&lArr;[COMMUNITY PLUGINS](../README.md) | [Home](../README.md)
## <b>ZK_Themes</b>

<a name="readme-top"></a>

## <b>Description</b>

AMERPSOFT ZK_Themes contains two basic themes.
- iceblue_mo
- ksys

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

Additional folders containing images for customers

- web.theme.iceblue_mo.images_mo
- web.theme.iceblue_mo.images_mcc

## <b>Features</b>

* <b>Ksys Kbs:</b>
Maintainer: Ken_longnan - [ken.longnan@gmail.com]
Status: Beta, up to date with release 5.1
License: GPLv2
Sources: ksys-idempiere-theme


* <b>iceblue_c:</b>
Maintainer: Heng Sin
[iceblue_c](https://github.com/hengsin/idempiere-examples)

* <b>Updated for idempiere version 12</b>

## <b>Documentation</b>


See <b>amerp_themes.docx</b>   or   <b>amerp_themes.pdf</b>


[AMERPSOFT Themes - amerp_themes.pdf ](./documentation/amerp_themes.pdf)

## <b>Installation Procedure</b>

### <b>Install Jar Plugin </b>
    Available on p2 site target plugins
    org.amerpsoft.com.idempiere.zk_themes_12.0.0.XXXXXXXXXXXXXX.jar

### <b>Pack in AMERPSOFT ZK_Themes.zip</b>

Once plugin is running, then Pack-In:

<b>AMERPSOFT ZK_Themes.zip</b>

It contains SysConfig Variables:
```text
    - ZK_THEME 
    - ZK_LOGO_LARGE
    - ZK_LOGO_SMALL
    - ZK_BROWSER_TITLE
    - ZK_BROWSER_ICON
    - ZK_THEME_USE_FONT_ICON_FOR_IMAGE 
    
    SET ZK_THEME_USE_FONT_ICON_FOR_IMAGE to 'N'
```

### <b>Manually enter Configured values</b>

If error on Pack-IN you may enter them manually.

(System Admin --> General Rules --> System Rules --> System Configurator)

Configured Values:
| NAME	                          | VALUE	   | DESCRIPTION	                                                           |AD_SYSCONFIG_UU      |
|---------------------------------|------------| ------------------------------------------------------------------------- |-------------------- | 
|ZK_THEME	                      |iceblue_c	       |Available choices ksys iceblue_c  |	054399ad-3705-411d-b79e-7ec7111888c5 |
|ZK_THEME_USE_FONT_ICON_FOR_IMAGE |N	       |Use Font Icon Yes or No	                                                   | 29a0d95d-af05-477a-a047-241c350dc8e3 |
|ZK_BROWSER_ICON	              |~./theme/iceblue_c/images/icon-idempiere.png	| Browse Icon Path	| 4cf766fd-5982-49dd-82f2-e8347214dbf0 |
|ZK_LOGO_LARGE	                  |~./theme/iceblue_c/images/login-logo-amerp2.png	| Login Logo Large	| bfe6284e-00a2-412f-be82-37ed38363768 |
|ZK_LOGO_SMALL	                  |~./theme/iceblue_c/images/header-logo-amerp.png	| Login Logo Small	| 69007345-c36f-4b02-870a-c41d8fb9deea |
|ZK_BROWSER_TITLE	              | iDempiere AMERP |	Browser Title	| c587f6f8-2910-4b27-913e-2e41e8581ea0 |


## <b>IF SOMETHING GO WRONG</b>

REPLACE AD_Sysconfig Record (AD_SysConfig_ID=200021)

Name:ZK_THEME

Value: default

This will put default Idempiere Standard ZK Theme
    
SQL Commands: 
UPDATE ad_sysconfig SET value ='default' WHERE AD_SysConfig_ID=200021;
COMMIT;


<!-- CONTACT -->
## Contact

These plugins and tutorial is brought to you by Luis Amesty from: [Amerpsoft Consulting](http://amerpsoft.com/). 

For any question or improvement see me at: [Idempiere WIKI](https://wiki.idempiere.org/en/User:Luisamesty)


## Release Notes:

- Updated for Idempiere release 8.2 - March 2021
- Updated for Idempiere release 11 - January 2024
- Updated for Idempiere release 12 - June 2025
- Under Test - See release-11 branch.

<p align="left">(<a href="#readme-top">back to top</a>)</p>

== Plugin: Themes Amerpsoft ==

* '''Maintainer:''' [[User:Luisamesty|User:Luisamesty]]  - [http://wiki.idempiere.org/en/User:Luisamesty Luis Amesty]
* '''Copyright:''' [http://amerpsoft.com/index.php/es/ AMERPSOFT and contributors ]
* '''License:''' [http://www.gnu.org/licenses/gpl-2.0.html GPLv2] - Free as in Freedom not Free Lunch
* '''Source:''' [https://bitbucket.org/amerpsoft/amerpsoft-themes-com/src/default/  Bitbucket  https://bitbucket.org/amerpsoft/amerpsoft-themes-com]
* '''Download:''' [https://bitbucket.org/amerpsoft/amerpsoft-themes-com/downloads/amerpsoft-themes.zip amerpsof-themes.zip]
* '''PDF:''' [https://bitbucket.org/amerpsoft/amerpsoft-themes-com/downloads/amerp_themes.pdf  PDF Manual]

__FORCETOC__

== Description ==
This Plugin is A collection of themes from different sources and contributors.
<br/>
'''Amtheme:''' Standard Theme similar to ferrythem.
<br/>
See FerryTheme from Hiep L Q on:
<br/>
http://wiki.idempiere.org/en/Plugin:_Theme_ferrytheme
<br/>
'''Amthemeblu:''' Blue Theme, Bigger Icons.
<br/>
'''Ksys:'''
<br/>
Maintainer: Ken_longnan - [ken.longnan@gmail.com]
<br/>
Status: Beta, up to date with release 5.1
License: GPLv2
Sources: ksys-idempiere-theme
<br/>
'''BusinessTeam''' and '''BusinessTeamBlue:'''
<br/>
Maintainer: User:PabloValdivia
<br/>
Main Copyright: Pablo Valdivia, EGS GROUP, Lima, PERU,
<br/>

== Sysconfig Configurable Values ==
'''ZK_LOGO_LARGE'''
<br/>
This is the image that is shown when User Log in.
<br/>
'''ZK_LOGO_SMALL'''
<br/>
Default Value for image on upper left corner where search field and menu button is located.
It can’t be modified by each Client, in order to have information on what client user is working. 
It has to be very small graphic file, otherwise may override main search field and menu button. 
See included header-logo-amerp.png ( 3 KB) sample on documentation folder.
<br/>
Is similar to default value: LOGIN_LOGO_IMAGE.
<br/>
Configured Value: /theme/amtheme/images/login-logo-amerp.png
<br/>
theme: SIMILAR Default THEME_PATH_PREFIX.
<br/>
amtheme: Theme’s name selected.
<br/>

'''ZK_BROWSER _ICON'''
<br/>
This is icon image locate at the top of navigator:
<br/>
Is similar to default value: BROWSER_ICON_IMAGE.
<br/>
Configured Value: /theme/amtheme/images/icon-amerp.png
<br/>
theme: SIMILAR Default THEME_PATH_PREFIX.
<br/>
amtheme: Theme’s name selected.
<br/>

'''ZK_THEME'''
<br/>
This Variable is already configured on iDempiere. 
<br/>
Option Values:
<br/>
Configured Value: '''default''
<br/>
When using this Plugin, user can select between Themes defined:
<br/>

Values for plugin Themes:
<br/>
'''default'''
<br/>
'''amtheme'''
<br/>
'''amthemeblu'''
<br/>
'''ksys'''
<br/>
'''businessteam'''
<br/>
'''businessTeamBlue'''
<br/>

'''********************* IMPORTANT ********************'''
<br/>
If something go wrong with this plugin, edit AD_SysConfig Table. Locate ZK_THEME record and set Configure Value to '''default'''.
<br/>
Restart Idempiere Server.
<br/>

'''*******************************************************'''
<br/>
== Install Procedure ==
Download plugin:
<br/>
https://bitbucket.org/amerpsoft/amerpsoft-themes-com/downloads/org.amerpsoft.themes_1.0.0.201903111905.jar
<br/>
Install Using Apache Felix Web Console
<br/>
Using System Configuration Window, edit or create if not exists values for:
<br/>
'''ZK_LOGO_LARGE'''
<br/>
'''ZK_LOGO_SMALL'''
<br/>
'''ZK_BROWSER _ICON'''
<br/>
'''ZK_THEME'''
<br/>
For more information:
<br/>
* '''Download:'''  https://bitbucket.org/amerpsoft/amerpsoft-themes-com/downloads/amerp_themes.pdf 
* '''From:'''  https://bitbucket.org/amerpsoft/amerpsoft-themes-com/downloads/  

== Feedback ==
Please rate this plugin:

[[Category:Available_Plugins]]
[[Category:Themes]]

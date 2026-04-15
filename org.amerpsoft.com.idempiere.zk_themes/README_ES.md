&lArr;[COMMUNITY PLUGINS](../README.md) | [Home](../README.md)

  <div>
    🇬🇧 <a href="README.md">English</a> | 🇪🇸 Español</a>
  </div>
  
  
## <b>Plugin ZK_Themes</b>

<a name="readme-top"></a>

## <b>Descripcion</b>

AMERPSOFT ZK_Themes contiene dos temas básicos.

- iceblue_am
- iceblue_mo
- ksys

Basado en IDEMPIERE-4421, Algunas notas de migración:

Los fuentes de los temas residen en:

```text
src/web/theme/THEME_NAME 
```
en lugar de:

```text
/theme/THEME_NAME
```

Dentro de la carpeta del tema, las referencias a otros recursos del tema deben usar la ruta completa (con el prefijo “~./”) en lugar de la ruta relativa.

Por ejemplo, en:

```text
theme/default/zul/login/login-left.zul
```

El valor de macroURI se cambia de vendor-logo.zul a:

```text
~./theme/default/zul/login/vendor-logo.zul
```

Lo mismo ocurre con la referencia a recursos en *.css.dsp.
Por ejemplo, en theme/default/css/fragment/grid.css.dsp

```css 
background-image: url(${c:encodeURL('/theme/default/images/EditRecord16.png')}) 
```

se reemplaza con:

```css
background-image: url(${c:encodeURL('~./theme/default/images/EditRecord16.png')}). 
```

Nótese el prefijo añadido “~./”.

</pre>

Carpetas adicionales que contienen imágenes para los clientes.

- web.theme.iceblue_am.images_tam
- web.theme.iceblue_am.images_mcc
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


Ver <b>amerp_themes.docx</b>   or   <b>amerp_themes.pdf</b>


[AMERPSOFT Themes - amerp_themes.pdf ](./documentation/amerp_themes.pdf)

## <b>Procedimiento de Instalacion</b>

### <b>Instalar Jar Plugin </b>

Instalar con OSGI System Console. 
Disponible en los plugins de destino del sitio p2.

org.amerpsoft.com.idempiere.zk_themes_12.0.0.XXXXXXXXXXXXXX.jar

### <b>Pack in AMERPSOFT ZK_Themes.zip</b>

Una vez que el 'plugin' esté en funcionamiento, entonces 'Pack-In':

<b>AMERPSOFT ZK_Themes.zip</b>

Contiene variables de SysConfig:

```text
- ZK_THEME 
- ZK_LOGO_LARGE
- ZK_LOGO_SMALL
- ZK_BROWSER_TITLE
- ZK_BROWSER_ICON
- ZK_THEME_USE_FONT_ICON_FOR_IMAGE 

SET ZK_THEME_USE_FONT_ICON_FOR_IMAGE to 'N'
```

### <b>Introducir manualmente los valores configurados</b>

Si se produce un error al instalar el paquete, puede introducirlos manualmente.

(Administración del sistema --> Reglas generales --> Reglas del sistema --> Configurador del sistema)

Valores configurados:
| NAME	                          | VALUE	   | DESCRIPTION	                                                           |AD_SYSCONFIG_UU      |
|---------------------------------|------------| ------------------------------------------------------------------------- |-------------------- | 
|ZK_THEME	                      |iceblue_c	       |Available choices ksys iceblue_c  |	054399ad-3705-411d-b79e-7ec7111888c5 |
|ZK_THEME_USE_FONT_ICON_FOR_IMAGE |N	       |Use Font Icon Yes or No	                                                   | 29a0d95d-af05-477a-a047-241c350dc8e3 |
|ZK_BROWSER_ICON	              |~./theme/iceblue_c/images/icon-idempiere.png	| Browse Icon Path	| 4cf766fd-5982-49dd-82f2-e8347214dbf0 |
|ZK_LOGO_LARGE	                  |~./theme/iceblue_c/images/login-logo-amerp2.png	| Login Logo Large	| bfe6284e-00a2-412f-be82-37ed38363768 |
|ZK_LOGO_SMALL	                  |~./theme/iceblue_c/images/header-logo-amerp.png	| Login Logo Small	| 69007345-c36f-4b02-870a-c41d8fb9deea |
|ZK_BROWSER_TITLE	              | iDempiere AMERP |	Browser Title	| c587f6f8-2910-4b27-913e-2e41e8581ea0 |



## <b>SI ALGO VA MAL</b>

REEPLACE AD_Sysconfig Record (AD_SysConfig_ID=200021)

Name:ZK_THEME

Value: default

Esto pondrá el tema predeterminado Imperiere Standard ZK.

Comando SQL:
```sql 
UPDATE ad_sysconfig SET value ='default' WHERE AD_SysConfig_ID=200021;
COMMIT;
```

## Contacto

Estos plugins y tutoriales son cortesía de Luis Amesty de [Amerpsoft Consulting](http://amerpsoft.com/).

Para cualquier pregunta o mejora, contáctame en: [Idempiere WIKI](https://wiki.idempiere.org/en/User:Luisamesty)

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>

## Requiere la versión 12 de Idempiere

Consulta la rama de la versión 12 para más detalles.

<p align="left">(<a href="#readme-top">volver arriba</a>)</p>


## Release Notes:

- Updated for Idempiere release 8.2 - March 2021
- Updated for Idempiere release 11 - January 2024
- Updated for Idempiere release 12 - June 2025
- Under Test - See release-11 branch.

<p align="left">(<a href="#readme-top">back to top</a>)</p>

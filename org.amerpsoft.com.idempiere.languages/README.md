&lArr;[COMMUNITY PLUGINS](../README.md) | [Home](../README.md)
# <b>Languages</b>

## <b>Installation </b>

    1. Pack IN 

(*)Steps 2 and 3 are for Venezuela, skip if not using additional language localization.
	
### <b>1- Set Spanish es_XX as System language</b>
- Login idempiere as System in English
- Go to Menu: /System Admin/General Rules/System Rules/Language
- On Window Language: Locate es_ES, es_VE, es_PY.  Or your preferred Language xx_XX
- On Window Language: Check System Language
- On Window Language: Execute 'Language Mantenance' Process
    ( If you are using other Locale adjust to your)

### <b>2- Create Language extension for es__VE or xx__XX</b>
- Download Script from Repository (See This plugin documentation directory)
- Example:  Create-language-from-es-CO-to-es-VE.sql" Script
- Execute Query from PostgreSQL environment
- You may edit this Query for your favourite Language xx_XX
- Remember to do Syncronize Terminology



AMERP Amerpsoft-editors: 
=======================

Intallation
-----------
1. Pack IN AMERP amxeditor.zip
* Set es_VE as System language
* Create Language extension for es__VE or xx__XX
* Add country code 3 digits
* Pack IN Venezuela ExtendedDemography
* Install Plugin using Apache felix Web Console
* Restart Server
* Modify C__BPartner__location Table
* Modify Country Record

1- Pack IN AMERP amxeditor.zip
- Using AD PackIn
- Download  AMERP amxeditor.zip, 
- Packin IT.
	
2- Set es_VE as System language
- Login idempiere as System in English
- Go to Menu: /System Admin/General Rules/System Rules/Language
- On Window Language: Locate es_VE or your preferred Language xx_XX
- On Window Language: Check System Language
- On Window Language: Execute 'Language Mantenance' Process

3- Create Language extension for es__VE or xx__XX
- Download Script from Repository
- Example:  Create-language-from-es-CO-to-es-VE.sql" Script
- Execute Query from PostgreSQL environment
- You may edit this Query for your favourite Language xx_XX

4- Add country code 3 digits
- Download Script from Repository
- "Create-country-codes-3_digit.sql" Script
- Execute Query from PostgreSQL environment
- This Fill CountryCode3 Fields on C_Country Table

5- Pack IN Venezuela ExtendedDemography
- Using AD PackIn
- Download  Venezuela ExtendedDemography.zip, 
- Packin IT.
- You may look at 'GeografiaVenezolanaCompleta.saql'
- Make your Country Extended Demography Packin

6-Install Plugin using Apache felix Web Console
- Download plugin jar file from Repository
- Install using Osgi Apache Felix Web Console
- or Any Manual procedure
- Verify plugin is running and updated

7- Restart Server
- Restart idempiere Server 
- Verify plugin is Installed and Running

8- C_BPartner_location Table
- Using Application dictionary Modify Table
- Field: C_Location 
- Change Reference 
- with 
- LocationExtended

9- Modify Country Record
- Change Capture Sequence
-    with
- @CO@ @A1!@ @A2@ @A3@ @A4@  @R!@ @MU@ @PA@ @C@, @P@ 
